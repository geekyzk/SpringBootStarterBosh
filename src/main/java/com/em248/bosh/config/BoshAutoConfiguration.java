package com.em248.bosh.config;

import com.alibaba.fastjson.JSON;
import com.em248.bosh.entity.Info;
import com.em248.bosh.http.SocketFactoryRegistry;
import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

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


    class ResponseInterceptor implements HttpResponseInterceptor {

        @Override
        public void process(HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
            Header location = httpResponse.getFirstHeader("Location");
            if (location != null) {
                String url = location.getValue();

                httpResponse.removeHeader(location);
                httpResponse.setHeader("Location", url.replace(properties.getHost(), properties.getHost() + ":" + properties.getPort()));
            }

        }
    }

    @ConditionalOnProperty(
            prefix = "spring.bosh",
            value = "basicAuth",
            havingValue = "true"
    )
    @Bean
    public CloseableHttpClient defaultHttpClient(PoolingHttpClientConnectionManager p) {
        CloseableHttpClient closeableHttpClient = HttpClients.custom().addInterceptorLast(new ResponseInterceptor())
                .setConnectionManager(p)
                .build();
        return closeableHttpClient;
    }


    @ConditionalOnProperty(
            prefix = "spring.bosh",
            value = "basicAuth",
            havingValue = "false"
    )
    @Bean
    public CloseableHttpClient uaaHttpClient(PoolingHttpClientConnectionManager p) {
        CloseableHttpClient closeableHttpClient = HttpClients.custom().addInterceptorLast(new ResponseInterceptor())
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
    class RequestInterceptor implements HttpRequestInterceptor {

        private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
        @Autowired
        TokenClient tokenClient;

        @Autowired
        BoshToken boshToken = new BoshToken();

        @Override
        public void process(HttpRequest httpRequest, HttpContext httpContext) {
            if (boshToken == null|| !boshToken.checkExpires()) { // 50 seconds
                try {
                    boshToken = tokenClient.createToken();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            String authorization=boshToken.getToken_type() + " " + boshToken.getAccess_token();
            httpRequest.setHeader(AUTHORIZATION_HEADER_KEY,authorization);
        }
    }

    @ConditionalOnProperty(
            prefix = "spring.bosh",
            value = "basicAuth",
            havingValue = "false"
    )
    @Configuration
    public class BoshToken {

        String access_token;

        String token_type;

        String refresh_token;

        Integer expires_in;

        String scope;

        String jti;

        Long  createTime = System.currentTimeMillis();

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getToken_type() {
            return token_type;
        }

        public void setToken_type(String token_type) {
            this.token_type = token_type;
        }

        public String getRefresh_token() {
            return refresh_token;
        }

        public void setRefresh_token(String refresh_token) {
            this.refresh_token = refresh_token;
        }

        public Integer getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(Integer expires_in) {
            this.expires_in = expires_in;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public String getJti() {
            return jti;
        }

        public void setJti(String jti) {
            this.jti = jti;
        }

        public boolean checkExpires(){
            if(getExpires_in() != null && (System.currentTimeMillis() - this.createTime)< getExpires_in()){
                return true;
            }else{
                return false;
            }
        }

        @Override
        public String toString() {
            return "BoshToken{" +
                    "access_token='" + access_token + '\'' +
                    ", token_type='" + token_type + '\'' +
                    ", refresh_token='" + refresh_token + '\'' +
                    ", expires_in=" + expires_in +
                    ", scope='" + scope + '\'' +
                    ", jti='" + jti + '\'' +
                    ", createTime=" + createTime +
                    '}';
        }
    }

    @ConditionalOnProperty(
            prefix = "spring.bosh",
            value = "basicAuth",
            havingValue = "false"
    )
    @Configuration
    class TokenClient {

        protected final Logger logger = LoggerFactory.getLogger(this.getClass());

        @Autowired
        BoshToken boshToken;

        Info info;
        CloseableHttpClient httpClient = HttpClients.createDefault();

        /**
         * 初始化tokenClient对象时时执行该方法
         * 去访问CF服务器然后获得需要的验证地址参数等信息
         */
        @PostConstruct
        public void init() throws Exception{
                HttpGet httpGet = new HttpGet("https://" + properties.getHost() + ":"+properties.getPort()+"/info");
                HttpResponse execute = httpClient.execute(httpGet);
                this.info = JSON.parseObject(EntityUtils.toString(execute.getEntity()), Info.class);
        }

        /**
         * 根据用户名和密码生成口令
         *
         * @return
         */
        public BoshToken createToken() throws  Exception{
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            formparams.add(new BasicNameValuePair("client_id", properties.getClientId()));
            formparams.add(new BasicNameValuePair("client_secret", properties.getClientSecret()));
            formparams.add(new BasicNameValuePair("password",properties.getPassword()));
            formparams.add(new BasicNameValuePair("username",properties.getUsername()));
            formparams.add(new BasicNameValuePair("grant_type", "password"));
            HttpPost httpPost = new HttpPost("https://" + properties.getHost() + ":8443/oauth/token");
            httpPost.setEntity(new UrlEncodedFormEntity(formparams));
            HttpResponse execute = httpClient.execute(httpPost);
            boshToken = JSON.parseObject(EntityUtils.toString(execute.getEntity()), BoshToken.class);
            logger.info("infoMap" + boshToken.toString());
            return boshToken;
        }

    }
}
