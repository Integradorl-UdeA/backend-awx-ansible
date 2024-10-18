package co.com.cosole.lis.awx.model.gateway;

import co.com.cosole.lis.awx.model.awxjobresult.AWXJobResult;
import reactor.core.publisher.Mono;

public interface JobAwxGateway {
    Mono<String> getJobLogs(Integer jobId);
    Mono<AWXJobResult> launchJob(Integer jobTemplateId);

}
