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
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    @Value("${webclient.baseurl}")
    private String BASE_URL;
    private static final int BYTE_COUNT = 1024*16*1024;

    @Bean
    public WebClient createWebClient() throws SSLException{
        return WebClient.builder()
                .codecs(configurer -> configurer.defaultCodecs()
                        .maxInMemorySize(BYTE_COUNT))
                .clientConnector(getClientHttpConnector())
                .baseUrl(BASE_URL).build();


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
}
