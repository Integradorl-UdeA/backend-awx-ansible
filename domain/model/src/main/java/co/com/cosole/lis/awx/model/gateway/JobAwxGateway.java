package co.com.cosole.lis.awx.model.gateway;

import co.com.cosole.lis.awx.model.awxjobresult.AWXJobResult;
import co.com.cosole.lis.awx.model.awxjobresult.JobCompletationNotification;
import co.com.cosole.lis.awx.model.extravars.RequestBodyWhitExtraVars;
import co.com.cosole.lis.awx.model.inventories.GroupsInventories;
import reactor.core.publisher.Mono;

import java.util.List;

public interface JobAwxGateway {
    Mono<String> getJobLogs(int jobId);
    Mono<AWXJobResult> launchJob(int jobTemplateId, RequestBodyWhitExtraVars requestBody);
    Mono<GroupsInventories> getGroupInventoryLis();
    Mono<String> getJobStatus(int jobId);



}
