package co.com.cosole.lis.awx.api;

import co.com.cosole.lis.awx.model.awxjobresult.AWXJobResult;
import co.com.cosole.lis.awx.model.inventories.GroupsInventories;
import co.com.cosole.lis.awx.model.summary.Summary;
import co.com.cosole.lis.awx.usecase.launchplaybook.GetGroupInventoryLisUseCase;
import co.com.cosole.lis.awx.usecase.launchplaybook.GetJobLogsUseCase;
import co.com.cosole.lis.awx.usecase.launchplaybook.GetLogAndLaunchUseCase;
import co.com.cosole.lis.awx.usecase.launchplaybook.LaunchPlaybookUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Log
@RestController
@RequestMapping( produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ApiRest {

    private final LaunchPlaybookUseCase launchPlaybookUseCase;
    private final GetJobLogsUseCase getJobLogsUseCase;
    private final GetLogAndLaunchUseCase getLogAndLaunchUseCase;
    private final GetGroupInventoryLisUseCase getGroupInventoryLisUseCase;

    @PostMapping(path ="/{jobTemplateId}/launch")
    public Mono<ResponseEntity<AWXJobResult>> launchJob(@PathVariable("jobTemplateId") int jobTemplateId) {
        return launchPlaybookUseCase.execute(jobTemplateId)
                .map(ResponseEntity::ok);
    }

    @GetMapping(path = "/{jobId}/logs")
    public Mono<ResponseEntity<Summary>> getJobLogs(@PathVariable("jobId") int jobId) {
        return getJobLogsUseCase.execute(jobId)
                .map(ResponseEntity::ok);
    }

    @PostMapping(path ="/{jobTemplateId}/la")
    public Mono<ResponseEntity<Summary>> lunchAndLog(@PathVariable("jobTemplateId") int jobTemplateId) {
        return getLogAndLaunchUseCase.execute(jobTemplateId)
                .map(ResponseEntity::ok);
    }

    @GetMapping(path ="/groups/inventory/lis")
    public Mono<ResponseEntity<GroupsInventories>> groupInventory() {
        return getGroupInventoryLisUseCase.execute()
                .map(ResponseEntity::ok);
    }

}
