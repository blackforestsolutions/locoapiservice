package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.apiservice.objectmothers.MapperObjectMother;
import de.blackforestsolutions.generatedcontent.airportsfinder.AirportsFinding;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AirportFindingMapperTest {


    private final AirportsFindingMapper classUnderTest = new AirportsFindingMapper();


    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void test_map_List_Of_LinkedHashMaps_To_List_Of_AirportsFindings() {
        List<LinkedHashMap<String, Object>> testData = MapperObjectMother.getAirportsFinderListOfLinkedHashMaps();

        List<AirportsFinding> result = AirportsFindingMapper.map(testData);

        assertThat(result.size()).isEqualTo(3);
        assertThat("VTL").isEqualTo(result.get(0).getCode());
        assertThat("Vittel Champ De Course Airport").isEqualTo(result.get(0).getName());
        assertThat("Luxeuil-les-Bains").isEqualTo(result.get(0).getCity());
        assertThat("FR").isEqualTo(result.get(0).getCountryCode());
        assertThat(6.381111).isEqualTo(result.get(0).getLocation().getLongitude());
        assertThat(47.8168409).isEqualTo(result.get(0).getLocation().getLatitude());
        assertThat("QFB").isEqualTo(result.get(1).getCode());
        assertThat("DLE").isEqualTo(result.get(2).getCode());
        assertThat("Dole-Tavaux Airport").isEqualTo(result.get(2).getName());
        assertThat(5.4212699).isEqualTo(result.get(2).getLocation().getLongitude());
        assertThat(47.0436856).isEqualTo(result.get(2).getLocation().getLatitude());
    }
}
