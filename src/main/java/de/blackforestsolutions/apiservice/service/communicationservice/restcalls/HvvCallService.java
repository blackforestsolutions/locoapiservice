package de.blackforestsolutions.apiservice.service.communicationservice.restcalls;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

public interface HvvCallService {
    ResponseEntity<String> postStationList(String url, HttpEntity<?> requestEntity);

    ResponseEntity<String> postTravelPoint(String url, HttpEntity<?> requestEntity);

    ResponseEntity<String> postJourney(String url, HttpEntity<?> requestEntity);
}
