package de.blackforestsolutions.apiservice.service.communicationservice.restcalls;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

public interface BBCCallService {
    ResponseEntity<String> getRide(String url, HttpEntity<?> requestEntity);
}
