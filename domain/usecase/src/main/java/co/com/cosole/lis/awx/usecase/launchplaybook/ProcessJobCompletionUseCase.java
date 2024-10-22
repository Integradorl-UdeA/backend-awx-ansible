package co.com.cosole.lis.awx.usecase.launchplaybook;

import co.com.cosole.lis.awx.model.awxjobresult.AWXJobResult;
import co.com.cosole.lis.awx.model.awxjobresult.JobCompletationNotification;
import co.com.cosole.lis.awx.model.extravars.RequestBodyWhitExtraVars;
import co.com.cosole.lis.awx.model.gateway.JobAwxGateway;
import co.com.cosole.lis.awx.model.hoststatus.HostsStatus;
import co.com.cosole.lis.awx.model.summary.Summary;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log
@AllArgsConstructor
public class ProcessJobCompletionUseCase {


    private JobAwxGateway jobAwxGateway;

    public Mono<Summary> summaryJobs(JobCompletationNotification jobCompletationNotification) {
        Summary summary = new Summary();
        Map<String, HostsStatus> hostsStatus = jobCompletationNotification.getHosts();

        for (Map.Entry<String, HostsStatus> host : hostsStatus.entrySet()) {
            String ip = host.getKey();
            HostsStatus status = host.getValue();
            if (status.isFailed()) {
                summary.getSuccessfulHosts().add(ip);
            } else {
                summary.getFailedHosts().add(ip);
            }

        }
        log.info("resumen de la ejecución" + summary);
        return Mono.just(summary);
    }

    public Mono<AWXJobResult> execute(int jobTemplateId, RequestBodyWhitExtraVars requestBody) {
        return jobAwxGateway.launchJob(jobTemplateId, requestBody);
    }







}
