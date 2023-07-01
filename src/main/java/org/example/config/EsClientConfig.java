package org.example.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author ldd
 * elasticsearch 客户端自动注入
 */
//@Configuration
public class EsClientConfig {
    @Value("${spring.datasource.dynamic.datasource.es.url}")
    public String urlStr;
    @Bean
    public RestClient restClient() throws MalformedURLException {
        URL url = new URL(urlStr);
        String host = url.getHost();
        int port = url.getPort();
        RestClient restClient = RestClient.builder(
                new HttpHost(host, port, "http")).build();
        return restClient;
    }
}
