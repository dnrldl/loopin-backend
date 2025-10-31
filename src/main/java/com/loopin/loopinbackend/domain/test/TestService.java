package com.loopin.loopinbackend.domain.test;

import com.google.gson.Gson;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class TestService {
    public TestDto getTestData() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<TestDto> exchange = restTemplate.exchange("http://localhost:8080/api/pub/test/get", HttpMethod.GET, entity, new ParameterizedTypeReference<TestDto>() {});

        return exchange.getBody();
    }
}
