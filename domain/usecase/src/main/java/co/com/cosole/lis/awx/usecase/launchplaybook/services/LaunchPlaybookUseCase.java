package co.com.cosole.lis.awx.usecase.launchplaybook.services;

import co.com.cosole.lis.awx.model.awxjobresult.AWXJobResult;
import reactor.core.publisher.Mono;

public interface LaunchPlaybookUseCase {
    Mono<AWXJobResult> execute(Integer jobTemplateId);
}
