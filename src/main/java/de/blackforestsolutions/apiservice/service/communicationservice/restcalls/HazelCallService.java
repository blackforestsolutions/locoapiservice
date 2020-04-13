package de.blackforestsolutions.apiservice.service.communicationservice.restcalls;

import org.springframework.http.HttpHeaders;

import java.util.Map;

public interface HazelCallService {


    String getEntryByKey(String targetUrl, String key);

    String saveEntry(String targetUrl, HttpHeaders headers, String key, String value);

    Map<String, String> getAllEntries(String url);
}
