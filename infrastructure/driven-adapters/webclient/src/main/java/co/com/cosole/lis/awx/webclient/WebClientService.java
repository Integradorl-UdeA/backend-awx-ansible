package co.com.cosole.lis.awx.webclient;



import co.com.cosole.lis.awx.model.awxjobresult.AWXJobResult;
import co.com.cosole.lis.awx.model.gateway.JobAwxGateway;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Log4j2
@AllArgsConstructor
@Service
public class WebClientService implements JobAwxGateway {

    private final WebClient webClient;
    private static HttpStatus statusCode;

    @Override
    public Mono<AWXJobResult> launchJob(Integer jobTemplateId) {
        return webClient.post()
                .uri("job_templates/{jobTemplateId}/launch/", jobTemplateId)
                .retrieve()
                .bodyToMono(AWXJobResult.class)
                .doFirst(() -> log.info("Iniciando el Job a las {} ", LocalDateTime.now()));
    }

    @Override
    public Mono<String> getJobLogs(Integer jobId) {
        return webClient.get()
                .uri("jobs/{jobId}/stdout/", jobId)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), response -> {
                    return response.bodyToMono(String.class)
                            .flatMap(errorBody -> Mono.error(new RuntimeException("Error al lanzar el job: " + response.statusCode() + " - " + errorBody)));
                })
                .bodyToMono(String.class)
                .retry(3)
                .doFirst(() -> log.info("Obteniendo logs del job {}", jobId));
    }


}
