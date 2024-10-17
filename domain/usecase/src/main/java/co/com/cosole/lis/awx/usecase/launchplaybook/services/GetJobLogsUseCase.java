package co.com.cosole.lis.awx.usecase.launchplaybook.services;

import co.com.cosole.lis.awx.model.summary.Summary;
import reactor.core.publisher.Mono;


public interface GetJobLogsUseCase {
    Mono<Summary> execute(Integer jobId);
}
