package co.com.cosole.lis.awx.webclient.configuration;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import io.netty.resolver.DefaultAddressResolverGroup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    @Value("${webclient.base-url}")
    private String baseUrl;

    @Value("${webclient.token}")
    private String token;

    private static final int BYTE_COUNT = 1024*16*1024;

    @Bean
    public WebClient createWebClient() throws SSLException{
        return WebClient.builder()
                .codecs(configurer -> configurer.defaultCodecs()
                        .maxInMemorySize(BYTE_COUNT))
                .clientConnector(getClientHttpConnector())
                .baseUrl(baseUrl)
                .filter(bearerAuthFilter())
                .build();


    }
    private ClientHttpConnector getClientHttpConnector() throws SSLException {
        var httpClient = HttpClient.create()
                .resolver(DefaultAddressResolverGroup.INSTANCE)
                .compress(true)
                .keepAlive(true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 100000)
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(100000, TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(100000, TimeUnit.MILLISECONDS));
                });

        return new ReactorClientHttpConnector(httpClient);
    }

    private ExchangeFilterFunction bearerAuthFilter() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest ->
                Mono.just(
                        ClientRequest.from(clientRequest)
                                .header("Authorization", "Bearer " + token)
                                .build()
                )
        );
    }
}
