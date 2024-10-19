package co.com.cosole.lis.awx.usecase.launchplaybook;


import co.com.cosole.lis.awx.model.gateway.JobAwxGateway;
import co.com.cosole.lis.awx.model.inventories.GroupsInventories;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;


@AllArgsConstructor
public class GetGroupInventoryLisUseCase {

    private final JobAwxGateway jobAwxGateway;

    public Mono<GroupsInventories> execute(){
        return jobAwxGateway.getGroupInventoryLis();
    }

}
