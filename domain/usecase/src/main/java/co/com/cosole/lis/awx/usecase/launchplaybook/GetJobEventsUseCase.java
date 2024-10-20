package co.com.cosole.lis.awx.usecase.launchplaybook;

import co.com.cosole.lis.awx.model.gateway.JobAwxGateway;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;


@Log
@AllArgsConstructor
public class GetJobEventsUseCase {

    private final JobAwxGateway jobAwxGateway;

    public Mono<String> execute(int jobId) {
        return jobAwxGateway.getJobStatus(jobId)
                .doOnSuccess(hola -> System.out.println("respuesta  " + hola));
    }
}
