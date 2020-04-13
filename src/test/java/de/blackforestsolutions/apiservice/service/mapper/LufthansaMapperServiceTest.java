package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.apiservice.configuration.AirportConfiguration;
import de.blackforestsolutions.apiservice.service.supportservice.UuidService;
import de.blackforestsolutions.datamodel.JourneyStatus;
import de.blackforestsolutions.datamodel.TravelProvider;
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

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    void test_map_jsonObject_and_return_Map_with_journeys() {
        String scheduledResourcesJson = getResourceFileAsString("json/lufthansatest.json");
        when(uuidService.createUUID())
                .thenReturn(TEST_UUID_1).thenReturn(TEST_UUID_2)
                .thenReturn(TEST_UUID_3).thenReturn(TEST_UUID_4)
                .thenReturn(TEST_UUID_5).thenReturn(TEST_UUID_6)
                .thenReturn(TEST_UUID_7).thenReturn(TEST_UUID_8)
                .thenReturn(TEST_UUID_9).thenReturn(TEST_UUID_10)
                .thenReturn(TEST_UUID_11).thenReturn(TEST_UUID_12);

        Map<UUID, JourneyStatus> result = classUnderTest.map(scheduledResourcesJson);

        assertThat(result.size()).isEqualTo(12);
        assertThat("E90").isEqualTo(result.get(TEST_UUID_6).getJourney().get().getVehicleNumber());
        assertThat(TravelProvider.LUFTHANSA).isEqualTo(result.get(TEST_UUID_6).getJourney().get().getTravelProvider());
        assertThat("LH1191").isEqualTo(result.get(TEST_UUID_6).getJourney().get().getProviderId());
    }
}
