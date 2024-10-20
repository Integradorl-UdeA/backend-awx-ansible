package co.com.cosole.lis.awx.webclient;



import co.com.cosole.lis.awx.model.awxjobresult.AWXJobResult;
import co.com.cosole.lis.awx.model.gateway.JobAwxGateway;
import co.com.cosole.lis.awx.model.inventories.GroupsInventories;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Log4j2
@AllArgsConstructor
@Service
public class WebClientService implements JobAwxGateway {

    private final WebClient webClient;
    private static HttpStatus statusCode;

    @Override
    public Mono<AWXJobResult> launchJob(int jobTemplateId, String limit) {
        return webClient.post()
                .uri("job_templates/{jobTemplateId}/launch/", jobTemplateId)
                .bodyValue(Map.of("limit",limit))
                .retrieve()
                .bodyToMono(AWXJobResult.class)
                .doFirst(() -> log.info("Iniciando el Job a las {} ", LocalDateTime.now()));
    }

    @Override
    public Mono<GroupsInventories> getGroupInventoryLis() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return webClient.get()
                .uri("inventories/2/groups/")
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(response -> {
                    try {
                        GroupsInventories groupsInventories= objectMapper.readValue(response, GroupsInventories.class);
                        log.info("Resultado mapeado: {}", groupsInventories);
                        return Mono.just(groupsInventories);
                    } catch (Exception e) {
                        log.error("Error al mapear la respuesta", e);
                        return Mono.error(new RuntimeException("Error al mapear la respuesta a GroupsInventories", e));
                    }
                })
                .doFirst(() -> log.info("Iniciando listar grupos inventario lis a las {} ", LocalDateTime.now()));
    }

    @Override
    public Mono<String> getJobStatus(int jobId) {
        ObjectMapper objectMapper = new ObjectMapper();

        return webClient.get()
                .uri("jobs/{jobId}/job_events/", jobId)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(response -> {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(response);
                        JsonNode statusNode = jsonNode.path("results").get(0)
                                .path("summary_fields").path("job").path("status");

                        if (statusNode.isMissingNode()) {
                            return Mono.error(new RuntimeException("No se encontró el estado del trabajo."));
                        } else {
                            return Mono.just(statusNode.asText());
                        }
                    } catch (Exception e) {
                        return Mono.error(new RuntimeException("Error al procesar la respuesta JSON.", e));
                    }
                });
    }


    @Override
    public Mono<String> getJobLogs(int jobId) {
        return webClient.get()
                .uri("jobs/{jobId}/stdout/?format=txt_download", jobId)
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
