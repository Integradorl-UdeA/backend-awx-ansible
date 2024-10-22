package co.com.cosole.lis.awx.config;

import co.com.cosole.lis.awx.model.awxjobresult.AWXJobResult;
import co.com.cosole.lis.awx.model.gateway.JobAwxGateway;
import co.com.cosole.lis.awx.usecase.launchplaybook.*;
import co.com.cosole.lis.awx.webclient.WebClientService;
import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class UseCasesConfig {

        @Bean
        public GetJobLogsUseCase getJobLogsUseCase(JobAwxGateway jobAwxGateway){
                return new GetJobLogsUseCase(jobAwxGateway);
        }


        @Bean
        @Primary
        public JobAwxGateway getJobAwxGateway(WebClient webClient) {
                return new WebClientService(webClient);
        }

        @Bean
        public GetLogAndLaunchUseCase getLogAndLaunchUseCase(JobAwxGateway jobAwxGateway){
                return new GetLogAndLaunchUseCase(jobAwxGateway);
        }

        @Bean
        public GetGroupInventoryLisUseCase getGroupInventoryLisUseCase(JobAwxGateway jobAwxGateway){
                return new GetGroupInventoryLisUseCase(jobAwxGateway);
        }
        @Bean
        public GetJobEventsUseCase getJobEventsUseCase(JobAwxGateway jobAwxGateway){
                return new GetJobEventsUseCase(jobAwxGateway);
        }

}
