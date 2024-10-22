package co.com.cosole.lis.awx.usecase.launchplaybook;


import co.com.cosole.lis.awx.model.awxjobresult.AWXJobResult;
import co.com.cosole.lis.awx.model.extravars.RequestBodyWhitExtraVars;
import co.com.cosole.lis.awx.model.gateway.JobAwxGateway;
import co.com.cosole.lis.awx.model.inventories.GroupsInventories;
import co.com.cosole.lis.awx.model.summary.Summary;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.Duration;


@AllArgsConstructor
public class GetGroupInventoryLisUseCase {

    private final JobAwxGateway jobAwxGateway;


    public Mono<GroupsInventories> execute(){
        return jobAwxGateway.getGroupInventoryLis();
    }




}
