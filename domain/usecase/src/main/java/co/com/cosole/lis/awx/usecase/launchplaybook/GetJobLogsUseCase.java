package co.com.cosole.lis.awx.usecase.launchplaybook;


import co.com.cosole.lis.awx.model.gateway.JobAwxGateway;
import co.com.cosole.lis.awx.model.hoststatus.HostStatus;
import co.com.cosole.lis.awx.model.summary.Summary;
import lombok.AllArgsConstructor;


import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log
@AllArgsConstructor
public class GetJobLogsUseCase {

    private final JobAwxGateway jobAwxGateway;


    public Mono<Summary> execute(Integer jobId) {
        return jobAwxGateway.getJobLogs(jobId)
                .flatMap(logs -> Mono.just(parseLogs(logs)))
                .defaultIfEmpty(new Summary());
    }

    private Summary parseLogs(String logs) {
        System.out.printf("dffdg" + logs);
        Summary summary = new Summary();
        String[] lines = logs.split("\n");
        Pattern pattern = Pattern.compile("^(\\S+)\\s+:\\s+ok=(\\d+)");

        boolean playRecapFound = false;

        for (String line : lines) {
            // Ignorar líneas hasta encontrar "PLAY RECAP"
            if (line.startsWith("PLAY RECAP")) {
                playRecapFound = true;
                continue;
            }

            if(playRecapFound) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()){
                    HostStatus hostStatus = HostStatus.builder()
                            .host(matcher.group(1))
                            .ok(Integer.parseInt(matcher.group(2)))
                            .build();
                    System.out.printf("ok "+ hostStatus.getOk());
                    if(hostStatus.getOk() > 0){
                        summary.getSuccessfulHosts().add(hostStatus);
                    }else summary.getFailedHosts().add(hostStatus);
                }
            }
        }

        return summary;
    }
}
