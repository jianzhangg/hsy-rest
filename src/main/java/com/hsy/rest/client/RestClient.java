package com.hsy.rest.client;
import java.io.IOException;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.hsy.common.constant.CommonConstant;
import com.hsy.common.utils.JsonUtils;
import com.hsy.common.utils.ObjectUtils;

/**
 * @author 张梓枫
 * @Description
 * @date:   2019年1月3日 下午5:30:21
 */
@Configuration
public class RestClient {

    @Autowired
    private RestTemplate restTemplate;
    
    private HttpHeaders headers;
    
    @Autowired
    private CloseableHttpClient httpClient;
    
    @Autowired
    private RequestConfig requestConfig;
    
    public <Q,R> R callForObject(String url, Q q, HttpMethod method, Class<R> clazz) {
        HttpEntity<Q> httpEntity = new HttpEntity<Q>(q,this.getHeaders());
        ResponseEntity<R> entity = restTemplate.exchange(url, method, httpEntity, clazz);
        return entity.getBody();
    }

    public <Q,R> R callForPost(String url, Q q,Class<R> clazz) {
        HttpEntity<Q> httpEntity = new HttpEntity<Q>(q,this.getHeaders());
        ResponseEntity<R> entity = restTemplate.postForEntity(url, httpEntity, clazz);
        return entity.getBody();
    }
    
    public <R> R httpGetObject(String url, Class<R> clazz){
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);
        try {
            CloseableHttpResponse entity = httpClient.execute(httpGet);
            String result = EntityUtils.toString(entity.getEntity());
            return JsonUtils.toBean(result, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public String httpGetString(String url){
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);
        try {
            CloseableHttpResponse entity = httpClient.execute(httpGet);
            String result = EntityUtils.toString(entity.getEntity());
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public RestClient createHeaders(String tokenValue) {
        this.headers = new HttpHeaders();
        this.headers.setContentType(MediaType.valueOf(CommonConstant.CONTENT_TYPE));
        this.headers.add(CommonConstant.SESSION_TOKEN, tokenValue);
        return this;
    }

    public RestClient createHeaders(String contentType, String tokenValue) {
        this.headers = new HttpHeaders();
        this.headers.setContentType(MediaType.valueOf(contentType));
        if (ObjectUtils.isNotEmpty(tokenValue)){
            this.headers.add(CommonConstant.SESSION_TOKEN, tokenValue);
        }
        return this;
    }
    
    public HttpHeaders getHeaders() {
        if (ObjectUtils.isNotEmpty(headers)) {
            return this.headers;
        }
        return this.getHeaders(CommonConstant.CONTENT_TYPE);
    }

    private HttpHeaders getHeaders(String contentType) {
        this.headers = new HttpHeaders();
        this.headers.setContentType(MediaType.valueOf(contentType));
        return this.headers;
    } 
}
