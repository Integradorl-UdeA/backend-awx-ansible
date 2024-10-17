package co.com.cosole.lis.awx.usecase.launchplaybook;


import co.com.cosole.lis.awx.model.hoststatus.HostStatus;
import co.com.cosole.lis.awx.model.summary.Summary;
import co.com.cosole.lis.awx.webclient.WebClientService;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@AllArgsConstructor
public class GetJobLogsUseCase {

    private final WebClientService webClientService;


    public Mono<Summary> execute(Integer jobId) {
        return webClientService.getJobLogs(jobId)
                .flatMap(logs -> Mono.just(parseLogs(logs)))
                .defaultIfEmpty(new Summary());
    }

    private Summary parseLogs(String logs) {
        Summary summary = new Summary();

        String[] lines = logs.split("\n");
        for (String line : lines) {
            if (line.startsWith("PLAY RECAP")) {
                continue;
            }

            String[] parts = line.trim().split("\\s+");
            if (parts.length >= 5) {
                String host = parts[0];
                int ok = Integer.parseInt(parts[1].split("=")[1]);
                HostStatus status = new HostStatus(host, ok);
                if (ok > 0) {
                    summary.getFailedHosts().add(status);
                } else{
                    summary.getSuccessfulHosts().add(status);
                }
            }
        }
        return summary;
    }
}
