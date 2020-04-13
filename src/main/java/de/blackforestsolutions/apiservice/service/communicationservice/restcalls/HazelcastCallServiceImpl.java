package de.blackforestsolutions.apiservice.service.communicationservice.restcalls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.Map;

@Service
public class HazelcastCallServiceImpl implements HazelCallService {

    private static final String KEY_PARAM = "?key={0}";
    private static final String VALUE_PARAM = "&value={0}";
    private final RestTemplate restTemplate;

    @Autowired
    public HazelcastCallServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public String getEntryByKey(String targetUrl, String key) {
        targetUrl = targetUrl.concat(MessageFormat.format(KEY_PARAM, key));
        return restTemplate.getForObject(targetUrl, String.class, key);
    }

    @Override
    public String saveEntry(String targetUrl, HttpHeaders headers, String key, String value) {
        targetUrl = targetUrl.concat(MessageFormat.format(KEY_PARAM, key).concat(MessageFormat.format(VALUE_PARAM, value)));
        return restTemplate.postForObject(targetUrl, headers, String.class, key, value);
    }

    @Override
    public Map<String, String> getAllEntries(String url) {
        //noinspection unchecked (justification: we know this type not until runtime)
        return restTemplate.getForObject(url, Map.class);
    }
}
