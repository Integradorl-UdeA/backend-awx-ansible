package co.com.cosole.lis.awx.usecase.launchplaybook;

import co.com.cosole.lis.awx.model.awxjobresult.JobCompletationNotification;
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


    public Mono<Summary> summaryJobs(JobCompletationNotification jobCompletationNotification) {
        Summary summary = new Summary();
        Map<String, HostsStatus> hostsStatus = jobCompletationNotification.getHosts();
        log.info("esta parte " + hostsStatus);

        for (Map.Entry<String, HostsStatus> host : hostsStatus.entrySet()) {
            String ip = host.getKey();
            HostsStatus status = host.getValue();
            if (status.isFailed()) {
                summary.getSuccessfulHosts().add(ip);
            } else {
                summary.getFailedHosts().add(ip);
            }
            System.out.println("IP: " + ip + ", Failed: " + status.isFailed());
        }
        return Mono.just(summary);
    }







}
