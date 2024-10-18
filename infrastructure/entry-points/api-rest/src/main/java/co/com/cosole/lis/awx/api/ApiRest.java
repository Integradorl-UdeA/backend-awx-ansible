package co.com.cosole.lis.awx.api;
import co.com.cosole.lis.awx.model.awxjobresult.AWXJobResult;
import co.com.cosole.lis.awx.model.summary.Summary;
import co.com.cosole.lis.awx.usecase.launchplaybook.GetJobLogsUseCase;
import co.com.cosole.lis.awx.usecase.launchplaybook.LaunchPlaybookUseCase;import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping( produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ApiRest {

    private final LaunchPlaybookUseCase launchPlaybookUseCase;
    private final GetJobLogsUseCase getJobLogsUseCase;

    @PostMapping(path ="/{jobTemplateId}/launch")
    public Mono<ResponseEntity<AWXJobResult>> launchJob(@PathVariable Integer jobTemplateId) {
        return launchPlaybookUseCase.execute(jobTemplateId)
                .map(ResponseEntity::ok);
    }

    @GetMapping(path = "/{jobId}/logs")
    public Mono<ResponseEntity<Summary>> getJobLogs(@PathVariable int jobId) {
        return getJobLogsUseCase.execute(jobId)
                .map(ResponseEntity::ok);
    }

    @GetMapping
    public String holi(){
        return "Holi";
    }
}
