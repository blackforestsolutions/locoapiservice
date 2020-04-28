package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.apiservice.service.supportservice.UuidService;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.JourneyStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import java.util.Map;
import java.util.UUID;

import static de.blackforestsolutions.apiservice.objectmothers.UUIDObjectMother.*;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.getResourceFileAsString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class RMVMapperServiceTest {

    private final UuidService uuidService = Mockito.mock(UuidService.class);

    @InjectMocks
    private final RMVMapperService classUnderTest = new RMVMapperServiceImpl(uuidService);

    @BeforeEach
    void init() {
        when(uuidService.createUUID())
                .thenReturn(TEST_UUID_1)
                .thenReturn(TEST_UUID_2)
                .thenReturn(TEST_UUID_3)
                .thenReturn(TEST_UUID_4)
                .thenReturn(TEST_UUID_5);
    }

    @Test
    void test_getIdFrom_with_safed_xml_return_id_Lorch() {
        String locationListXml = getResourceFileAsString("xml/LocationList.xml");

        CallStatus result = classUnderTest.getIdFrom(locationListXml);
        String callResult = (String) result.getCalledObject();

        assertThat(callResult).isEqualTo("A=1@O=Lorch-Lorchhausen Bahnhof@X=7779822@Y=50055165@U=80@L=003011037@B=1@V=6.9,@p=1574794859@");
    }

    @Test
    void test_getIdFrom_with_safed_xml_return_id_Main_station() {
        String locationListXml = getResourceFileAsString("xml/LocationList-frankfurt.xml");

        CallStatus result = classUnderTest.getIdFrom(locationListXml);
        String callResult = (String) result.getCalledObject();

        assertThat(callResult).isEqualTo("A=1@O=Frankfurt (Main) Hauptbahnhof@X=8662653@Y=50106808@U=80@L=003000010@B=1@V=6.9,@p=1575313337@");
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    void test_getJourneyFrom_with_xml_stub_and_return_Journey_Frankfurt_Lorch() {
        String tripListXml = getResourceFileAsString("xml/TripList.xml");

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysFrom(tripListXml);

        assertThat(result.size()).isEqualTo(5);
        assertThat(result.get(TEST_UUID_1).getJourney().get().getStart().getStationName()).isEqualTo("Frankfurt (Main) Hauptbahnhof");
        assertThat(result.get(TEST_UUID_1).getJourney().get().getDestination().getStationName()).isEqualTo("Lorch-Lorchhausen Bahnhof");
        assertThat(result.get(TEST_UUID_1).getJourney().get().getTravelLine().getBetweenHolds().size()).isEqualTo(2);
        assertThat(result.get(TEST_UUID_1).getJourney().get().getPrice().getValue()).isEqualTo(1235.0);
        assertThat(result.get(TEST_UUID_2).getJourney().get().getStart().getStationName()).isEqualTo("Frankfurt (Main) Hauptbahnhof");
        assertThat(result.get(TEST_UUID_2).getJourney().get().getDestination().getStationName()).isEqualTo("Lorch-Lorchhausen Bahnhof");
        assertThat(result.get(TEST_UUID_2).getJourney().get().getPrice().getValue()).isEqualTo(1235.0);
        assertThat(result.get(TEST_UUID_2).getJourney().get().getTravelLine().getBetweenHolds().size()).isEqualTo(2);
        assertThat(result.get(TEST_UUID_3).getJourney().get().getStart().getStationName()).isEqualTo("Frankfurt (Main) Hauptbahnhof");
        assertThat(result.get(TEST_UUID_3).getJourney().get().getDestination().getStationName()).isEqualTo("Lorch-Lorchhausen Bahnhof");
        assertThat(result.get(TEST_UUID_3).getJourney().get().getPrice().getValue()).isEqualTo(1235.0);
        assertThat(result.get(TEST_UUID_3).getJourney().get().getTravelLine().getBetweenHolds().size()).isEqualTo(2);
        assertThat(result.get(TEST_UUID_4).getJourney().get().getStart().getStationName()).isEqualTo("Frankfurt (Main) Hauptbahnhof");
        assertThat(result.get(TEST_UUID_4).getJourney().get().getDestination().getStationName()).isEqualTo("Lorch-Lorchhausen Bahnhof");
        assertThat(result.get(TEST_UUID_4).getJourney().get().getPrice().getValue()).isEqualTo(1235.0);
        assertThat(result.get(TEST_UUID_4).getJourney().get().getTravelLine().getBetweenHolds().size()).isEqualTo(2);
        assertThat(result.get(TEST_UUID_5).getJourney().get().getStart().getStationName()).isEqualTo("Frankfurt (Main) Hauptbahnhof tief");
        assertThat(result.get(TEST_UUID_5).getJourney().get().getDestination().getStationName()).isEqualTo("Lorch-Lorchhausen Bahnhof");
        assertThat(result.get(TEST_UUID_5).getJourney().get().getPrice().getValue()).isEqualTo(1235.0);
        assertThat(result.get(TEST_UUID_1).getJourney().get().getTravelLine().getBetweenHolds().size()).isEqualTo(2);
    }



    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    void test_getJourneyFrom_with_xml_stub_and_return_Journey_Ulrichstein_Endbach() {
        String tripListXml = getResourceFileAsString("xml/TripList-edge-case.xml");

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysFrom(tripListXml);

        assertThat(result.size()).isEqualTo(5);
        assertThat(result.get(TEST_UUID_1).getJourney().get().getStart().getStationName()).isEqualTo("Ulrichstein Wiesenhof");
        assertThat(result.get(TEST_UUID_1).getJourney().get().getDestination().getStationName()).isEqualTo("Bad Endbach Sportzentrum");
        assertThat(result.get(TEST_UUID_1).getJourney().get().getTravelLine().getBetweenHolds().size()).isEqualTo(6);
        assertThat(result.get(TEST_UUID_1).getJourney().get().getPrice().getValue()).isEqualTo(3600.0);
        assertThat(result.get(TEST_UUID_2).getJourney().get().getStart().getStationName()).isEqualTo("Ulrichstein Wiesenhof");
        assertThat(result.get(TEST_UUID_2).getJourney().get().getDestination().getStationName()).isEqualTo("Bad Endbach Sportzentrum");
        assertThat(result.get(TEST_UUID_2).getJourney().get().getPrice().getValue()).isEqualTo(3600.0);
        assertThat(result.get(TEST_UUID_2).getJourney().get().getTravelLine().getBetweenHolds().size()).isEqualTo(6);
        assertThat(result.get(TEST_UUID_3).getJourney().get().getStart().getStationName()).isEqualTo("Ulrichstein Wiesenhof");
        assertThat(result.get(TEST_UUID_3).getJourney().get().getDestination().getStationName()).isEqualTo("Bad Endbach Sportzentrum");
        assertThat(result.get(TEST_UUID_3).getJourney().get().getPrice().getValue()).isEqualTo(1235.0);
        assertThat(result.get(TEST_UUID_3).getJourney().get().getTravelLine().getBetweenHolds().size()).isEqualTo(7);
        assertThat(result.get(TEST_UUID_4).getJourney().get().getStart().getStationName()).isEqualTo("Ulrichstein Wiesenhof");
        assertThat(result.get(TEST_UUID_4).getJourney().get().getDestination().getStationName()).isEqualTo("Bad Endbach Sportzentrum");
        assertThat(result.get(TEST_UUID_4).getJourney().get().getPrice().getValue()).isEqualTo(0.0);
        assertThat(result.get(TEST_UUID_4).getJourney().get().getTravelLine().getBetweenHolds().size()).isEqualTo(7);
        assertThat(result.get(TEST_UUID_5).getJourney().get().getStart().getStationName()).isEqualTo("Ulrichstein Wiesenhof");
        assertThat(result.get(TEST_UUID_5).getJourney().get().getDestination().getStationName()).isEqualTo("Bad Endbach Sportzentrum");
        assertThat(result.get(TEST_UUID_5).getJourney().get().getPrice().getValue()).isEqualTo(3600.0);
        assertThat(result.get(TEST_UUID_1).getJourney().get().getTravelLine().getBetweenHolds().size()).isEqualTo(6);
    }
}
