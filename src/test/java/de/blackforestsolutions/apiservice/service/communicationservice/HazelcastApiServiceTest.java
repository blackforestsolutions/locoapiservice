package de.blackforestsolutions.apiservice.service.communicationservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.HazelCallService;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.HazelcastCallServiceImpl;
import de.blackforestsolutions.apiservice.stubs.RestTemplateBuilderStub;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.Journey;
import de.blackforestsolutions.datamodel.util.LocoJsonMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static de.blackforestsolutions.apiservice.objectmothers.UUIDObjectMother.TEST_UUID_1;
import static org.mockito.Mockito.*;

class HazelcastApiServiceTest {

    private static final String KEY_PARAM = "?key=";
    private static final String VALUE_PARAM = "&value=";

    private static final RestTemplate restTemplate = mock(RestTemplate.class);

    private final RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilderStub(restTemplate);

    private final HazelCallService hazelcastWriteService = new HazelcastCallServiceImpl(restTemplateBuilder);

    @InjectMocks
    private HazelcastApiService classUnderTest = new HazelcastApiServiceImpl(hazelcastWriteService);

    @Test
    void test_readFromHazelcast_with_key_returns_string() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getHazelcastTokenAndUrl();
        String testKey = "testkey";
        String testBaseUrl = "http://localhost:8081/hazelcast/read-data";
        when(restTemplate.getForObject(testBaseUrl.concat(KEY_PARAM + testKey), String.class, testKey)).thenReturn("test successful");

        String result = classUnderTest.readFromHazelcast(testKey, testData);

        Assertions.assertThat("test successful").isEqualTo(result);
    }


    @Test
    void test_writeToHazelcast_with_key_value_returns_string() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getHazelcastTokenAndUrl();
        String testKey = "testkey";
        String testvalue = "testvalue";
        String testBaseUrl = "http://localhost:8081/hazelcast/write-data";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_XML_VALUE);
        headers.setContentType(MediaType.APPLICATION_XML);
        when(restTemplate.postForObject(testBaseUrl.concat(KEY_PARAM + testKey).concat(VALUE_PARAM + testvalue), headers, String.class, testKey, testvalue)).thenReturn("test successful");

        String result = classUnderTest.writeToHazelcast(testKey, testvalue, testData);

        Assertions.assertThat("test successful").isEqualTo(result);
    }

    @Test
    void test_writeAllToHazelcast_with_key_value_returns_string() throws JsonProcessingException {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getHazelcastTokenAndUrl();
        String testKey = "testkey";
        String testvalue = "testvalue";
        String testBaseUrl = "http://localhost:8081/hazelcast/write-data";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_XML_VALUE);
        headers.setContentType(MediaType.APPLICATION_XML);
        Journey.JourneyBuilder journey = new Journey.JourneyBuilder(TEST_UUID_1);
        String url = testBaseUrl.concat(MessageFormat.format(KEY_PARAM, testKey).concat(MessageFormat.format(VALUE_PARAM, testvalue)));
        when(restTemplate.postForObject(url, headers, String.class, testKey, testvalue)).thenReturn("test successful");
        String assertUrl = testBaseUrl.concat(KEY_PARAM + TEST_UUID_1.toString()).concat(VALUE_PARAM + new LocoJsonMapper().map(journey.build()));

        classUnderTest.writeAllToHazelcast(Collections.singletonMap(UUID.randomUUID(), journey.build()), testData);

        verify(restTemplate, times(1)).postForObject(assertUrl, headers, String.class, TEST_UUID_1.toString(), new LocoJsonMapper().map(journey.build()));
    }


    @Test
    void test_readAllFromHazelcast_returns_all_data_in_cache() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getHazelcastTokenAndUrl();
        String testBaseUrl = "http://localhost:8081/hazelcast/read-all-data";
        when(restTemplate.getForObject(testBaseUrl, Map.class)).thenReturn(new HashMap<String, String>() {{
            put("test", "test successful");
        }});

        Map<String, String> result = classUnderTest.readAllFromHazelcast(testData);

        Assertions.assertThat("test successful").isEqualTo(result.get("test"));
    }
}
