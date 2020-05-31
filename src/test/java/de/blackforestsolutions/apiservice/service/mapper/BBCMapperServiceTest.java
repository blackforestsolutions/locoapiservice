package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.apiservice.objectmothers.UUIDObjectMother;
import de.blackforestsolutions.apiservice.service.supportservice.UuidService;
import de.blackforestsolutions.apiservice.service.supportservice.UuidServiceImpl;
import de.blackforestsolutions.datamodel.JourneyStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;
import java.util.UUID;

import static de.blackforestsolutions.apiservice.testutils.TestUtils.getResourceFileAsString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BBCMapperServiceTest {

    private final UuidService uuidService = mock(UuidService.class);

    private final BBCMapperService classUnderTest = new BBCMapperServiceImpl(uuidService);

    @BeforeEach
    void init() {
        when(uuidService.createUUID())
                .thenReturn(UUIDObjectMother.TEST_UUID_1)
                .thenReturn(UUIDObjectMother.TEST_UUID_2)
                .thenReturn(UUIDObjectMother.TEST_UUID_3)
                .thenReturn(UUIDObjectMother.TEST_UUID_4)
                .thenReturn(UUIDObjectMother.TEST_UUID_5)
                .thenReturn(UUIDObjectMother.TEST_UUID_6)
                .thenReturn(UUIDObjectMother.TEST_UUID_7)
                .thenReturn(UUIDObjectMother.TEST_UUID_8);
    }


    @Test
    void test_mapJsonToJourneys_with_mocked_json_and_apiToken_returns() {
        String json = getResourceFileAsString("json/bbcTest.json");

        Map<UUID, JourneyStatus> result = classUnderTest.mapJsonToJourneys(json);

        Assertions.assertThat(result.size()).isEqualTo(4);
    }
}
