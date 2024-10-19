package co.com.cosole.lis.awx.usecase.launchplaybook;


import co.com.cosole.lis.awx.model.gateway.JobAwxGateway;
import co.com.cosole.lis.awx.model.summary.Summary;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;
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

        return summary;
    }
}
