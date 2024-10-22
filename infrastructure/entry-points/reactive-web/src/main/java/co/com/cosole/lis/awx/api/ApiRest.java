package co.com.cosole.lis.awx.api;

import co.com.cosole.lis.awx.model.awxjobresult.AWXJobResult;
import co.com.cosole.lis.awx.model.awxjobresult.JobCompletationNotification;
import co.com.cosole.lis.awx.model.extravars.RequestBodyWhitExtraVars;
import co.com.cosole.lis.awx.model.inventories.ExtraVarsGroup;
import co.com.cosole.lis.awx.model.inventories.GroupsInventories;
import co.com.cosole.lis.awx.model.summary.Summary;
import co.com.cosole.lis.awx.usecase.launchplaybook.*;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;

@Log4j2
@RestController
@RequestMapping( produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ApiRest {

    private final GetJobLogsUseCase getJobLogsUseCase;
    private final GetLogAndLaunchUseCase getLogAndLaunchUseCase;
    private final GetGroupInventoryLisUseCase getGroupInventoryLisUseCase;
    private final GetJobEventsUseCase getJobEventsUseCase;
    private final ProcessJobCompletionUseCase processJobCompletionUseCase;

    private static final int JOB_TEMPLATE_PING = 9;


    @GetMapping(path = "/{jobId}/logs")
    public Mono<ResponseEntity<Summary>> getJobLogs(@PathVariable("jobId") int jobId) {
        return getJobLogsUseCase.execute(jobId)
                .map(ResponseEntity::ok);
    }




    @PostMapping(path ="/ping/launch")
    public Mono<ResponseEntity<Summary>> lunchAndLogPing(@RequestBody RequestBodyWhitExtraVars requestBody) {
        LocalDateTime startTime = LocalDateTime.now();
        return getLogAndLaunchUseCase.execute(JOB_TEMPLATE_PING, requestBody)
                .map(ResponseEntity::ok)
                .doFinally(signalType -> {
                    Duration duration = Duration.between(startTime, LocalDateTime.now());
                    log.info("Job finalizado. Duración total: {} segundos", duration.getSeconds());
                });
    }

    @GetMapping(path ="/groups/inventory/lis")
    public Mono<ResponseEntity<GroupsInventories>> groupInventory() {
        return getGroupInventoryLisUseCase.execute()
                .map(ResponseEntity::ok);
    }

    @GetMapping(path="job/{id}/events")
    public Mono<ResponseEntity<String>> getJobEvents(@PathVariable("id") int jobId) {
        return getJobEventsUseCase.execute(jobId)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/jobCompleted")
    public Mono<ResponseEntity<Summary>> jobCompleted(@RequestBody JobCompletationNotification notification) {
        log.info("Webhook recibido para el job: " + notification.getId());
        log.info("este es {} ", notification.toString());
        return  processJobCompletionUseCase.summaryJobs(notification)
                .map(ResponseEntity::ok);

    }

    @PostMapping(path ="/ping")
    public Mono<ResponseEntity<AWXJobResult>> lunchAndPing(@RequestBody RequestBodyWhitExtraVars requestBody) {
        LocalDateTime startTime = LocalDateTime.now();
        return processJobCompletionUseCase.execute(JOB_TEMPLATE_PING, requestBody)
                .map(ResponseEntity::ok)
                .doFinally(signalType -> {
                    Duration duration = Duration.between(startTime, LocalDateTime.now());
                    log.info("Job finalizado. Duración total: {} segundos", duration.getSeconds());
                });
    }


}



