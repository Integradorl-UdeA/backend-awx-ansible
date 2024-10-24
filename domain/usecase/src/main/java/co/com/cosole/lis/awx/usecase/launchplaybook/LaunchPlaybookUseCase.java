package co.com.cosole.lis.awx.usecase.launchplaybook;

import co.com.cosole.lis.awx.model.awxjobresult.AWXJobResult;
import co.com.cosole.lis.awx.model.gateway.JobAwxGateway;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class LaunchPlaybookUseCase {

    private final JobAwxGateway jobAwxGateway;

    public Mono<AWXJobResult> execute(int jobTemplateId, String limit) {
        return jobAwxGateway.launchJob(jobTemplateId, limit);
    }
}
