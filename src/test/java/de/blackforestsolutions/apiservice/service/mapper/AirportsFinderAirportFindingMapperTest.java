package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.apiservice.objectmothers.MapperObjectMother;
import de.blackforestsolutions.generatedcontent.airportsfinder.AirportsFinding;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;


public class AirportsFinderAirportFindingMapperTest {

    private final AirportsFinderAirportsFindingMapper classUnderTest = new AirportsFinderAirportsFindingMapper();

    @Test
    public void test_map_List_Of_LinkedHashMaps_To_List_Of_AirportsFindings() {
        List<LinkedHashMap<String, Object>> testData = MapperObjectMother.getAirportsFinderListOfLinkedHashMaps();

        List<AirportsFinding> result = AirportsFinderAirportsFindingMapper.map(testData);

        Assertions.assertThat(result.size()).isEqualTo(3);
        Assertions.assertThat("VTL").isEqualTo(result.get(0).getCode());
        Assertions.assertThat("Vittel Champ De Course Airport").isEqualTo(result.get(0).getName());
        Assertions.assertThat("Luxeuil-les-Bains").isEqualTo(result.get(0).getCity());
        Assertions.assertThat("FR").isEqualTo(result.get(0).getCountryCode());
        Assertions.assertThat(6.381111).isEqualTo(result.get(0).getLocation().getLongitude());
        Assertions.assertThat(47.8168409).isEqualTo(result.get(0).getLocation().getLatitude());
        Assertions.assertThat("QFB").isEqualTo(result.get(1).getCode());
        Assertions.assertThat("DLE").isEqualTo(result.get(2).getCode());
        Assertions.assertThat("Dole-Tavaux Airport").isEqualTo(result.get(2).getName());
        Assertions.assertThat(5.4212699).isEqualTo(result.get(2).getLocation().getLongitude());
        Assertions.assertThat(47.0436856).isEqualTo(result.get(2).getLocation().getLatitude());
    }
}
