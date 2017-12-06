package com.em248.bosh.http;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by geekyzk on 05/12/2017.
 */
public class CustomRestTemplate extends RestTemplate {


    public CustomRestTemplate() {
    }

    public CustomRestTemplate(ClientHttpRequestFactory requestFactory) {
        super(requestFactory);
    }

    @Override
    public void setMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
        List<HttpMessageConverter<?>> defaultConverters = super.getMessageConverters();
        defaultConverters.add(new MappingJackson2HttpMessageConverter());
    }
}
