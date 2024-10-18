package co.com.cosole.lis.awx.usecase.launchplaybook;

import co.com.cosole.lis.awx.model.gateway.JobAwxGateway;
import co.com.cosole.lis.awx.model.hoststatus.HostStatus;
import co.com.cosole.lis.awx.model.summary.Summary;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log
@AllArgsConstructor
public class GetLogAndLaunchUseCase {
    private final JobAwxGateway jobAwxGateway;
    private final static int MAX_RETRIES = 4;  // Número máximo de intentos
    private final static Duration RETRY_INTERVAL = Duration.ofSeconds(15);

    public Mono<Summary> execute(int jobTemplateId) {
        return jobAwxGateway.launchJob(jobTemplateId)
                .flatMap(jobResult -> pollForLogs(jobResult.getJob(), 0));
    }

    private Mono<Summary> pollForLogs(int jobId, int attempts) {
        return jobAwxGateway.getJobLogs(jobId)
                .flatMap(logs -> Mono.just(parseLogs(logs)))
                .switchIfEmpty(handleNoLogs(jobId, attempts));
    }

    private Mono<Summary> handleNoLogs(int jobId, int attempts) {
        if (attempts < MAX_RETRIES) {
            log.info("No logs available yet for jobId "+  jobId + "Retrying ..." + attempts + 1 + "/" +MAX_RETRIES);
            return Mono.delay(RETRY_INTERVAL)  // Espera antes de reintentar
                    .flatMap(aLong -> pollForLogs(jobId, attempts + 1));
        } else {
            log.info("Max retries reached for jobId "+ jobId+  " Returning empty summary.");
            return Mono.just(new Summary());  // Devuelve un resumen vacío si se alcanzan los intentos máximos
        }
    }

    private Summary parseLogs(String logs) {
        Summary summary = new Summary();
        String[] lines = logs.split("\n");
        Pattern pattern = Pattern.compile("^(\\S+)\\s+:\\s+ok=(\\d+)");

        boolean playRecapFound = false;

        for (String line : lines) {
            if (line.startsWith("PLAY RECAP")) {
                playRecapFound = true;
                continue;
            }

            if (playRecapFound) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    HostStatus hostStatus = HostStatus.builder()
                            .host(matcher.group(1))
                            .ok(Integer.parseInt(matcher.group(2)))
                            .build();
                    if (hostStatus.getOk() > 0) {
                        summary.getSuccessfulHosts().add(hostStatus);
                    } else {
                        summary.getFailedHosts().add(hostStatus);
                    }
                }
            }
        }

        return summary;
    }
}
