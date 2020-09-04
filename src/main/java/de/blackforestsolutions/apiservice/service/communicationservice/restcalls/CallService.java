package de.blackforestsolutions.apiservice.service.communicationservice.restcalls;

import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface CallService {
    ResponseEntity<String> getOld(String url, HttpEntity<?> requestEntity);

    ResponseEntity<String> post(String url, HttpEntity<?> requestEntity);

    Mono<ResponseEntity<String>> get(String url, HttpEntity<?> request);

    Mono<ResponseEntity<String>> post(String url, HttpEntity<?> request, MediaType typeOfRequestBody);

}
