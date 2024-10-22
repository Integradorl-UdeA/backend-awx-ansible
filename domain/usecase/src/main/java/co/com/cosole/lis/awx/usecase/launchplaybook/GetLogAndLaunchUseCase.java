package co.com.cosole.lis.awx.usecase.launchplaybook;

import co.com.cosole.lis.awx.model.extravars.RequestBodyWhitExtraVars;
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
    private static final int MAX_RETRIES = 4;
    private static final Duration RETRY_INTERVAL = Duration.ofSeconds(10);

    public Mono<Summary> execute(int jobTemplateId, RequestBodyWhitExtraVars requestBody) {
        return jobAwxGateway.launchJob(jobTemplateId, requestBody)
                .flatMap(jobResult -> Mono.delay(Duration.ofSeconds(20))
                        .flatMap(ignored -> waitForJobCompletion(jobResult.getJob(), 0)));
    }

    private Mono<Summary> waitForJobCompletion(int jobId, int attempts) {
        return jobAwxGateway.getJobStatus(jobId)
                .doOnSuccess(status -> log.info("Estado del job: " + status))
                .flatMap(status -> {
                    if ("running".equalsIgnoreCase(status)) {
                        if (attempts < MAX_RETRIES) {
                            log.info("El job sigue en ejecución. Intento " + (attempts + 1) + "/" + MAX_RETRIES);
                            return Mono.delay(RETRY_INTERVAL)
                                    .flatMap(aLong -> waitForJobCompletion(jobId, attempts + 1));
                        } else {
                            log.warning("Máximo de intentos alcanzado para el jobId " + jobId + ". Devolviendo summary vacío.");
                            return Mono.just(new Summary());
                        }
                    } else {
                        return pollForLogs(jobId);
                    }
                });
    }

    private Mono<Summary> pollForLogs(int jobId) {
        return jobAwxGateway.getJobLogs(jobId)
                .flatMap(logs -> Mono.just(parseLogs(logs)))
                .switchIfEmpty(Mono.just(new Summary()));
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
                    if (Integer.parseInt(matcher.group(2))> 0) {
                        summary.getSuccessfulHosts().add(matcher.group(1));
                    } else {
                        summary.getFailedHosts().add(matcher.group(1));
                    }
                }
            }
        }

        return summary;
    }
}
