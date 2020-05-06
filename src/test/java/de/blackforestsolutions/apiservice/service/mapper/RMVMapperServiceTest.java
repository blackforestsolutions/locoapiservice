package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.apiservice.service.supportservice.UuidService;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.JourneyStatus;
import de.blackforestsolutions.datamodel.PriceCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import java.math.BigDecimal;
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
                .thenReturn(TEST_UUID_5)
                .thenReturn(TEST_UUID_6)
                .thenReturn(TEST_UUID_7)
                .thenReturn(TEST_UUID_8)
                .thenReturn(TEST_UUID_9)
                .thenReturn(TEST_UUID_10)
                .thenReturn(TEST_UUID_11).thenReturn(TEST_UUID_12).thenReturn(TEST_UUID_13).thenReturn(TEST_UUID_14).thenReturn(TEST_UUID_15).thenReturn(TEST_UUID_16).thenReturn(TEST_UUID_17).thenReturn(TEST_UUID_18).thenReturn(TEST_UUID_19)
                .thenReturn(TEST_UUID_20).thenReturn(TEST_UUID_21).thenReturn(TEST_UUID_22);
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
        assertThat(result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_2).getStart().getStationName()).isEqualTo("Frankfurt (Main) Hauptbahnhof");
        assertThat(result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_2).getDestination().getStationName()).isEqualTo("Lorch-Lorchhausen Bahnhof");
        assertThat(result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_2).getPrice().getValues().get(PriceCategory.ADULT)).isEqualTo(new BigDecimal(1235));
        assertThat(result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_2).getPrice().getValues().get(PriceCategory.CHILD)).isEqualTo(new BigDecimal(730));
        assertThat(result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_2).isHasPrice()).isEqualTo(true);

        assertThat(result.get(TEST_UUID_3).getJourney().get().getLegs().get(TEST_UUID_4).getStart().getStationName()).isEqualTo("Frankfurt (Main) Hauptbahnhof");
        assertThat(result.get(TEST_UUID_3).getJourney().get().getLegs().get(TEST_UUID_4).getDestination().getStationName()).isEqualTo("Lorch-Lorchhausen Bahnhof");
        assertThat(result.get(TEST_UUID_3).getJourney().get().getLegs().get(TEST_UUID_4).getPrice().getValues().get(PriceCategory.ADULT)).isEqualTo(new BigDecimal(1235));
        assertThat(result.get(TEST_UUID_3).getJourney().get().getLegs().get(TEST_UUID_4).getPrice().getValues().get(PriceCategory.CHILD)).isEqualTo(new BigDecimal(730));

        assertThat(result.get(TEST_UUID_5).getJourney().get().getLegs().get(TEST_UUID_6).getStart().getStationName()).isEqualTo("Frankfurt (Main) Hauptbahnhof");
        assertThat(result.get(TEST_UUID_5).getJourney().get().getLegs().get(TEST_UUID_6).getDestination().getStationName()).isEqualTo("Lorch-Lorchhausen Bahnhof");
        assertThat(result.get(TEST_UUID_5).getJourney().get().getLegs().get(TEST_UUID_6).getPrice().getValues().get(PriceCategory.ADULT)).isEqualTo(new BigDecimal(1235));
        assertThat(result.get(TEST_UUID_5).getJourney().get().getLegs().get(TEST_UUID_6).getPrice().getValues().get(PriceCategory.CHILD)).isEqualTo(new BigDecimal(730));

        assertThat(result.get(TEST_UUID_7).getJourney().get().getLegs().get(TEST_UUID_8).getStart().getStationName()).isEqualTo("Frankfurt (Main) Hauptbahnhof");
        assertThat(result.get(TEST_UUID_7).getJourney().get().getLegs().get(TEST_UUID_8).getDestination().getStationName()).isEqualTo("Lorch-Lorchhausen Bahnhof");
        assertThat(result.get(TEST_UUID_7).getJourney().get().getLegs().get(TEST_UUID_8).getPrice().getValues().get(PriceCategory.ADULT)).isEqualTo(new BigDecimal(1235));
        assertThat(result.get(TEST_UUID_7).getJourney().get().getLegs().get(TEST_UUID_8).getPrice().getValues().get(PriceCategory.CHILD)).isEqualTo(new BigDecimal(730));

        assertThat(result.get(TEST_UUID_9).getJourney().get().getLegs().get(TEST_UUID_10).getStart().getStationName()).isEqualTo("Frankfurt (Main) Hauptbahnhof tief");
        assertThat(result.get(TEST_UUID_9).getJourney().get().getLegs().get(TEST_UUID_10).getDestination().getStationName()).isEqualTo("Wiesbaden Hauptbahnhof");
        assertThat(result.get(TEST_UUID_9).getJourney().get().getLegs().get(TEST_UUID_10).getPrice().getValues().get(PriceCategory.ADULT)).isEqualTo(new BigDecimal(1235));
        assertThat(result.get(TEST_UUID_9).getJourney().get().getLegs().get(TEST_UUID_10).getPrice().getValues().get(PriceCategory.CHILD)).isEqualTo(new BigDecimal(730));
        assertThat(result.get(TEST_UUID_9).getJourney().get().getLegs().get(TEST_UUID_11).getStart().getStationName()).isEqualTo("Wiesbaden Hauptbahnhof");
        assertThat(result.get(TEST_UUID_9).getJourney().get().getLegs().get(TEST_UUID_11).getDestination().getStationName()).isEqualTo("Lorch-Lorchhausen Bahnhof");
    }


    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    void test_getJourneyFrom_with_xml_stub_and_return_Journey_Ulrichstein_Endbach() {
        String tripListXml = getResourceFileAsString("xml/TripList-edge-case.xml");

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysFrom(tripListXml);

        assertThat(result.size()).isEqualTo(5);
        assertThat(result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_2).getStart().getStationName()).isEqualTo("Ulrichstein Wiesenhof");
        assertThat(result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_2).getDestination().getStationName()).isEqualTo("Lauterbach (Hessen) Nordbahnhof");
        assertThat(result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_2).getPrice().getValues().get(PriceCategory.ADULT)).isEqualTo(new BigDecimal(3600));
        assertThat(result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_6).getDestination().getStationName()).isEqualTo("Bad Endbach Sportzentrum");
        assertThat(result.get(TEST_UUID_1).getJourney().get().getLegs().size()).isEqualTo(5);

        assertThat(result.get(TEST_UUID_7).getJourney().get().getLegs().get(TEST_UUID_8).getStart().getStationName()).isEqualTo("Ulrichstein Wiesenhof");
        assertThat(result.get(TEST_UUID_7).getJourney().get().getLegs().get(TEST_UUID_8).getDestination().getStationName()).isEqualTo("Lauterbach (Hessen) Nordbahnhof");
        assertThat(result.get(TEST_UUID_7).getJourney().get().getLegs().get(TEST_UUID_8).getPrice().getValues().get(PriceCategory.ADULT)).isEqualTo(new BigDecimal(3600));
        assertThat(result.get(TEST_UUID_7).getJourney().get().getLegs().get(TEST_UUID_12).getDestination().getStationName()).isEqualTo("Bad Endbach Sportzentrum");
        assertThat(result.get(TEST_UUID_7).getJourney().get().getLegs().size()).isEqualTo(5);

        assertThat(result.get(TEST_UUID_13).getJourney().get().getLegs().get(TEST_UUID_14).getStart().getStationName()).isEqualTo("Ulrichstein Wiesenhof");
        assertThat(result.get(TEST_UUID_13).getJourney().get().getLegs().get(TEST_UUID_14).getDestination().getStationName()).isEqualTo("Schotten Vulkaneum");
        assertThat(result.get(TEST_UUID_13).getJourney().get().getLegs().get(TEST_UUID_14).getPrice().getValues().get(PriceCategory.ADULT)).isEqualTo(new BigDecimal(1235));
        assertThat(result.get(TEST_UUID_13).getJourney().get().getLegs().get(TEST_UUID_14).getPrice().getValues().get(PriceCategory.CHILD)).isEqualTo(new BigDecimal(730));
        assertThat(result.get(TEST_UUID_13).getJourney().get().getLegs().get(TEST_UUID_19).getDestination().getStationName()).isEqualTo("Bad Endbach Sportzentrum");
        assertThat(result.get(TEST_UUID_13).getJourney().get().getLegs().size()).isEqualTo(6);
    }


    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    void test_getJourneyFrom_with_xml_stub_and_return_Journey_Frankfurt_Lorch_() {
        String tripListXml = getResourceFileAsString("xml/RequestWithDate.xml");

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysFrom(tripListXml);

        assertThat(result.size()).isEqualTo(5);
        assertThat(result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_2).getStart().getStationName()).isEqualTo("Lorch-Lorchhausen Bahnhof");
        assertThat(result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_4).getDestination().getStationName()).isEqualTo("Frankfurt (Main) Flughafen Fernbahnhof");
        assertThat(result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_2).getPrice().getValues().get(PriceCategory.ADULT)).isEqualTo(new BigDecimal(1250));
        assertThat(result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_2).getPrice().getValues().get(PriceCategory.CHILD)).isEqualTo(new BigDecimal(730));
        assertThat(result.get(TEST_UUID_1).getJourney().get().getLegs().size()).isEqualTo(3);

        assertThat(result.get(TEST_UUID_13).getJourney().get().getLegs().get(TEST_UUID_14).getStart().getStationName()).isEqualTo("Lorch-Lorchhausen Bahnhof");
        assertThat(result.get(TEST_UUID_13).getJourney().get().getLegs().get(TEST_UUID_16).getDestination().getStationName()).isEqualTo("Frankfurt (Main) Flughafen Fernbahnhof");
        assertThat(result.get(TEST_UUID_13).getJourney().get().getLegs().get(TEST_UUID_14).getPrice().getValues().get(PriceCategory.ADULT)).isEqualTo(new BigDecimal(870));
        assertThat(result.get(TEST_UUID_13).getJourney().get().getLegs().get(TEST_UUID_14).getPrice().getValues().get(PriceCategory.CHILD)).isEqualTo(new BigDecimal(510));
        assertThat(result.get(TEST_UUID_13).getJourney().get().getLegs().size()).isEqualTo(3);
    }


}
