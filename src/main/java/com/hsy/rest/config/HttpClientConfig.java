package com.hsy.rest.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 张梓枫
 * @Description 集成http请求
 * @date: 2019年1月24日 下午4:55:30
 */
@Configuration
public class HttpClientConfig {

    private final static Integer MAX_TOTAL = 100;

    private final static Integer DEFAULT_MAX_PER_ROUTE = 20;

    private final static Integer CONNECT_TIMEOUT = 1000;

    private final static Integer CONNECTION_REQUEST_TIMEOUT = 500;

    private final static Integer SOCKET_TIMEOUT = 10000;


    @Bean(name = "builder")
    public RequestConfig.Builder getBuilder() {
        RequestConfig.Builder builder = RequestConfig.custom();
        return builder.setConnectTimeout(CONNECT_TIMEOUT).setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT);
    }
    
    @Bean
    public RequestConfig getRequestConfig(@Qualifier("builder") RequestConfig.Builder builder) {
        return builder.build();
    }
    
    @Bean(name = "httpClientConnectionManager")
    public PoolingHttpClientConnectionManager getHttpClientConnectionManager() {
        PoolingHttpClientConnectionManager httpClientConnectionManager = new PoolingHttpClientConnectionManager();
        // 最大连接数
        httpClientConnectionManager.setMaxTotal(MAX_TOTAL);
        // 并发数
        httpClientConnectionManager.setDefaultMaxPerRoute(DEFAULT_MAX_PER_ROUTE);
        return httpClientConnectionManager;
    }

    @Bean(name = "httpClientBuilder")
    public HttpClientBuilder getHttpClientBuilder(
            @Qualifier("httpClientConnectionManager") PoolingHttpClientConnectionManager httpClientConnectionManager) {
        HttpClientBuilder builder = HttpClientBuilder.create();
        builder.setConnectionManager(httpClientConnectionManager);
        return builder;
    }

    @Bean
    public CloseableHttpClient getCloseableHttpClient(
            @Qualifier("httpClientBuilder") HttpClientBuilder HttpClientBuilder) {
        return HttpClientBuilder.build();
    }
}
