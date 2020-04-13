package de.blackforestsolutions.apiservice.service.communicationservice.restcalls;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HafasCallServiceImpl implements HafasCallService {

    private final RestTemplate restTemplate;

    public HafasCallServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public ResponseEntity<String> getStationId(String url, HttpEntity<?> requestEntity) {
        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
    }

    @Override
    public ResponseEntity<String> getJourney(String url, HttpEntity<?> requestEntity) {
        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
    }


}
