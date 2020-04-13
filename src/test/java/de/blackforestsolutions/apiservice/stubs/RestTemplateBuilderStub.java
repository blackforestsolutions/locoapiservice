package de.blackforestsolutions.apiservice.stubs;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

public class RestTemplateBuilderStub extends RestTemplateBuilder {

    private final RestTemplate restTemplate;

    public RestTemplateBuilderStub(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public RestTemplate build() {
        return restTemplate;
    }
}
