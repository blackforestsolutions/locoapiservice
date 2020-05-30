package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.apiservice.service.supportservice.UuidService;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.Journey;
import de.blackforestsolutions.datamodel.JourneyStatus;
import de.blackforestsolutions.datamodel.PriceCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Map;
import java.util.UUID;

import static de.blackforestsolutions.apiservice.objectmothers.JourneyObjectMother.getLorchhausenOberfleckenToFrankfurtHauptbahnhofJourney;
import static de.blackforestsolutions.apiservice.objectmothers.UUIDObjectMother.*;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.getResourceFileAsString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RMVMapperServiceTest {

    private final UuidService uuidService = mock(UuidService.class);

    @InjectMocks
    private final RMVMapperService classUnderTest = new RMVMapperServiceImpl(uuidService);

    @BeforeEach
    void init() {
        when(uuidService.createUUID())
                .thenReturn(TEST_UUID_1).thenReturn(TEST_UUID_2).thenReturn(TEST_UUID_3)
                .thenReturn(TEST_UUID_4).thenReturn(TEST_UUID_5).thenReturn(TEST_UUID_6)
                .thenReturn(TEST_UUID_7).thenReturn(TEST_UUID_8).thenReturn(TEST_UUID_9)
                .thenReturn(TEST_UUID_10).thenReturn(TEST_UUID_11).thenReturn(TEST_UUID_12)
                .thenReturn(TEST_UUID_13).thenReturn(TEST_UUID_14).thenReturn(TEST_UUID_15)
                .thenReturn(TEST_UUID_16).thenReturn(TEST_UUID_17).thenReturn(TEST_UUID_18)
                .thenReturn(TEST_UUID_19).thenReturn(TEST_UUID_20).thenReturn(TEST_UUID_21)
                .thenReturn(TEST_UUID_22);
    }

    @Test
    void test_getIdFrom_with_safed_xml_return_id_Lorch() {
        String locationListXml = getResourceFileAsString("xml/LocationList.xml");

        CallStatus result = classUnderTest.getIdFrom(locationListXml);
        String callResult = (String) result.getCalledObject();

        assertThat(callResult).isEqualTo("A%3D2%40O%3DLorch+-+Lorchhausen%2C+Oberflecken%40X%3D7785108%40Y%3D50053277%40U%3D103%40b%3D990117421%40B%3D1%40V%3D6.9%2C%40p%3D1530862110%40");
    }

    @Test
    void test_getIdFrom_with_safed_xml_return_id_Main_station() {
        String locationListXml = getResourceFileAsString("xml/LocationList-frankfurt.xml");

        CallStatus result = classUnderTest.getIdFrom(locationListXml);
        String callResult = (String) result.getCalledObject();

        assertThat(callResult).isEqualTo("A=1@O=Frankfurt (Main) Hauptbahnhof@X=8662653@Y=50106808@U=80@L=003000010@B=1@V=6.9,@p=1575313337@");
    }

    @Test
    void test_getJourneysFrom_with_mocked_xml_returns_correct_size_of_journeys() throws ParseException {
        String tripListXml = getResourceFileAsString("xml/TripList.xml");

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysFrom(tripListXml);

        assertThat(result.size()).isEqualTo(6);
    }

    @Test
    void test_test_getJourneysFrom_with_mocked_xml_is_mapping_first_journey_correctly() throws ParseException {
        String tripListXml = getResourceFileAsString("xml/TripList.xml");
        Journey expectedJourney = getLorchhausenOberfleckenToFrankfurtHauptbahnhofJourney();

        Journey result = classUnderTest.getJourneysFrom(tripListXml).get(TEST_UUID_1).getJourney().get();

        assertThat(result).isEqualToIgnoringGivenFields(expectedJourney, "legs");
        assertThat(result.getLegs().size()).isEqualTo(3);
        assertThat(result.getLegs().get(TEST_UUID_2)).isEqualToIgnoringGivenFields(expectedJourney.getLegs().get(TEST_UUID_2), "price");
        assertThat(result.getLegs().get(TEST_UUID_2).getPrice()).isEqualToComparingFieldByField(expectedJourney.getLegs().get(TEST_UUID_2).getPrice());
        assertThat(result.getLegs().get(TEST_UUID_3)).isEqualToComparingFieldByField(expectedJourney.getLegs().get(TEST_UUID_3));
        assertThat(result.getLegs().get(TEST_UUID_4)).isEqualToIgnoringGivenFields(expectedJourney.getLegs().get(TEST_UUID_4), "travelLine");
        assertThat(result.getLegs().get(TEST_UUID_4).getTravelLine()).isEqualToComparingFieldByField(expectedJourney.getLegs().get(TEST_UUID_4).getTravelLine());
    }


    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    void test_getJourneyFrom_with_xml_stub_and_return_correct_price_edge_case() {
        String tripListXml = getResourceFileAsString("xml/TripList-edge-case.xml");

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysFrom(tripListXml);

        assertThat(result.size()).isEqualTo(5);
        assertThat(result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_2).getPrice().getValues().get(PriceCategory.ADULT)).isEqualTo(new BigDecimal("36.00"));
        assertThat(result.get(TEST_UUID_7).getJourney().get().getLegs().get(TEST_UUID_8).getPrice().getValues().get(PriceCategory.ADULT)).isEqualTo(new BigDecimal("36.00"));
        assertThat(result.get(TEST_UUID_13).getJourney().get().getLegs().get(TEST_UUID_14).getPrice().getValues().get(PriceCategory.ADULT)).isEqualTo(new BigDecimal("12.35"));
        assertThat(result.get(TEST_UUID_13).getJourney().get().getLegs().get(TEST_UUID_14).getPrice().getValues().get(PriceCategory.CHILD)).isEqualTo(new BigDecimal("7.30"));
    }

}
