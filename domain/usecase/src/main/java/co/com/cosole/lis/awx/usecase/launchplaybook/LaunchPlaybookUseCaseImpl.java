package co.com.cosole.lis.awx.usecase.launchplaybook;

import co.com.cosole.lis.awx.model.awxjobresult.AWXJobResult;
import co.com.cosole.lis.awx.usecase.launchplaybook.services.LaunchPlaybookUseCase;
import co.com.cosole.lis.awx.webclient.WebClientService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Service
public class LaunchPlaybookUseCaseImpl implements LaunchPlaybookUseCase {

    private final WebClientService webClientService;

    @Override
    public Mono<AWXJobResult> execute(Integer jobTemplateId) {
        return webClientService.launchJob(jobTemplateId);
    }
}
