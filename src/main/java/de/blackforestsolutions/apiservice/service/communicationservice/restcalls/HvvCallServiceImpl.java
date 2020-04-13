package de.blackforestsolutions.apiservice.service.communicationservice.restcalls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HvvCallServiceImpl implements HvvCallService {

    private final RestTemplate restTemplate;

    @Autowired
    public HvvCallServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public ResponseEntity<String> postStationList(String url, HttpEntity<?> requestEntity) {
        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
    }

    @Override
    public ResponseEntity<String> postTravelPoint(String url, HttpEntity<?> requestEntity) {
        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
    }

    @Override
    public ResponseEntity<String> postJourney(String url, HttpEntity<?> requestEntity) {
        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
    }
}

