package com.em248.bosh.config;

import com.em248.bosh.entity.Token;
import com.em248.bosh.http.SocketFactoryRegistry;
import com.em248.bosh.http.TokenClient;
import org.apache.http.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2017/12/5.
 */
@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties({BoshProperties.class})
@ComponentScan("com.em248.bosh")
public class BoshAutoConfiguration {

    private final BoshProperties properties;


    public BoshAutoConfiguration(BoshProperties properties) {
        this.properties = properties;
    }


    public String getBoshUrl() {
        if (properties.getBasicAuth()) {
            return String.format("https://%s:%s@%s:%s/", properties.getUsername(), properties.getPassword(), properties.getHost(), properties.getPort());
        } else {
            return String.format("https://%s:%s/", properties.getHost(), properties.getPort());
        }
    }

    @Bean
    public PoolingHttpClientConnectionManager getPoolingHttpClientConnectionManager() {
        try {
            PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(SocketFactoryRegistry.acceptsUntrustedCertsRegistry());
            poolingHttpClientConnectionManager.setDefaultMaxPerRoute(50);
            poolingHttpClientConnectionManager.setMaxTotal(200);
            return poolingHttpClientConnectionManager;
        } catch (KeyStoreException | NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }


    @ConditionalOnMissingBean
    @Bean
    public HttpResponseInterceptor getHttpResponseInterceptor() {
        return new HttpResponseInterceptor() {
            @Override
            public void process(HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
                Header location = httpResponse.getFirstHeader("Location");
                if (location != null) {
                    String url = location.getValue();

                    httpResponse.removeHeader(location);
                    httpResponse.setHeader("Location", url.replace(properties.getHost(), properties.getHost() + ":" + properties.getPort()));
                }
            }
        };
    }


    @ConditionalOnProperty(
            prefix = "spring.bosh",
            value = "basicAuth",
            havingValue = "true"
    )
    @Bean("boshHttpClient")
    public CloseableHttpClient defaultHttpClient(PoolingHttpClientConnectionManager p,HttpResponseInterceptor responseInterceptor) {
        CloseableHttpClient closeableHttpClient = HttpClients.custom().addInterceptorLast(responseInterceptor)
                .setConnectionManager(p)
                .build();
        return closeableHttpClient;
    }


    @ConditionalOnProperty(
            prefix = "spring.bosh",
            value = "basicAuth",
            havingValue = "false"
    )
    @Bean("boshHttpClient")
    public CloseableHttpClient uaaHttpClient(PoolingHttpClientConnectionManager p,
                                             HttpResponseInterceptor responseInterceptor,
                                             HttpRequestInterceptor requestInterceptor) {
        CloseableHttpClient closeableHttpClient = HttpClients.custom()
                .addInterceptorLast(responseInterceptor)
                .addInterceptorFirst(requestInterceptor)
                .setConnectionManager(p)
                .build();
        return closeableHttpClient;
    }



    @ConditionalOnProperty(
            prefix = "spring.bosh",
            value = "basicAuth",
            havingValue = "false"
    )
    @Configuration
    protected class RequestInterceptor implements HttpRequestInterceptor {

        private  final String AUTHORIZATION_HEADER_KEY = "Authorization";
        Token token;

        @Override
        public void process(HttpRequest httpRequest, HttpContext httpContext) {
            if (token == null || !token.checkExpires()) { // 50 seconds
                try {
                    token = TokenClient.createToken(properties);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            String authorization = token.getToken_type() + " " + token.getAccess_token();
            httpRequest.setHeader(AUTHORIZATION_HEADER_KEY, authorization);
        }
    }

    @ConditionalOnProperty(
            prefix = "spring.bosh",
            value = "basicAuth",
            havingValue = "false"
    )
    @ConditionalOnMissingBean(
            name = {"token"}
    )
    @Bean
    public Token getBoshToken() {
        return new Token();
    }

}
