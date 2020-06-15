package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.apiservice.configuration.AirportConfiguration;
import de.blackforestsolutions.apiservice.service.supportservice.UuidService;
import de.blackforestsolutions.datamodel.JourneyStatus;
import de.blackforestsolutions.datamodel.TravelProvider;
import de.blackforestsolutions.generatedcontent.lufthansa.LufthansaAuthorization;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import static de.blackforestsolutions.apiservice.objectmothers.UUIDObjectMother.*;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.getResourceFileAsString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LufthansaMapperServiceTest {

    private final AirportConfiguration airportConfiguration = new AirportConfiguration();

    private final UuidService uuidService = mock(UuidService.class);

    private final LufthansaMapperService classUnderTest = new LufthansaMapperServiceImpl(airportConfiguration.airports(), uuidService);

    LufthansaMapperServiceTest() throws IOException {
    }

    @BeforeEach
    void init() {
        when(uuidService.createUUID())
                .thenReturn(TEST_UUID_1)
                .thenReturn(TEST_UUID_2)
                .thenReturn(TEST_UUID_3)
                .thenReturn(TEST_UUID_4)
                .thenReturn(TEST_UUID_5)
                .thenReturn(TEST_UUID_6)
                .thenReturn(TEST_UUID_7)
                .thenReturn(TEST_UUID_8)
                .thenReturn(TEST_UUID_9)
                .thenReturn(TEST_UUID_10)
                .thenReturn(TEST_UUID_11)
                .thenReturn(TEST_UUID_12)
                .thenReturn(TEST_UUID_13)
                .thenReturn(TEST_UUID_14)
                .thenReturn(TEST_UUID_15)
                .thenReturn(TEST_UUID_16)
                .thenReturn(TEST_UUID_17)
                .thenReturn(TEST_UUID_18)
                .thenReturn(TEST_UUID_19)
                .thenReturn(TEST_UUID_20)
                .thenReturn(TEST_UUID_21)
                .thenReturn(TEST_UUID_22);

    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    void test_map_jsonObject_and_return_Map_with_journeys() {
        String scheduledResourcesJson = getResourceFileAsString("json/lufthansaJourneyTest.json");

        Map<UUID, JourneyStatus> result = (Map<UUID, JourneyStatus>) classUnderTest.map(scheduledResourcesJson).getCalledObject();

        assertThat(result.size()).isEqualTo(12);
        assertThat("E90").isEqualTo(result.get(TEST_UUID_11).getJourney().get().getLegs().get(TEST_UUID_12).getVehicleNumber());
        assertThat(TravelProvider.LUFTHANSA).isEqualTo(result.get(TEST_UUID_11).getJourney().get().getLegs().get(TEST_UUID_12).getTravelProvider());
        assertThat("LH1191").isEqualTo(result.get(TEST_UUID_11).getJourney().get().getLegs().get(TEST_UUID_12).getProviderId());
    }

    @Test
    void test_mapToAuthorization_with_json_return_correct_pojo() {
        String authorizationResourceJson = getResourceFileAsString("json/lufthansaAuthorizationTest.json");

        LufthansaAuthorization result =  (LufthansaAuthorization) classUnderTest.mapToAuthorization(authorizationResourceJson).getCalledObject();

        assertThat(result.getAccessToken()).isEqualTo("6rgpt9j3u6k7befb2eea4fqx");
        assertThat(result.getExpiresIn()).isEqualTo(129600);
        assertThat(result.getTokenType()).isEqualTo("bearer");
    }
}
