package de.blackforestsolutions.apiservice.service.mapper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.blackforestsolutions.datamodel.Coordinates;
import de.blackforestsolutions.datamodel.TravelPoint;
import de.blackforestsolutions.datamodel.TravelPointStatus;
import de.blackforestsolutions.datamodel.exception.NoExternalResultFoundException;
import de.blackforestsolutions.generatedcontent.osm.Address;
import de.blackforestsolutions.generatedcontent.osm.OsmTravelPoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static de.blackforestsolutions.apiservice.service.mapper.TravelPointStatusBuilder.createTravelPointStatusProblemWith;
import static de.blackforestsolutions.apiservice.service.mapper.TravelPointStatusBuilder.createTravelPointStatusWith;

@Slf4j
@Service
public class OsmMapperServiceImpl implements OsmMapperService {

    private static final int FIRST_INDEX = 0;

    @Override
    public TravelPointStatus mapOsmJsonToTravelPoint(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            List<OsmTravelPoint> osmTravelPointList = objectMapper.readValue(jsonString, objectMapper.getTypeFactory().constructCollectionType(List.class, OsmTravelPoint.class));
            return createTravelPointStatusWith(extractTravelPointFrom(osmTravelPointList.get(FIRST_INDEX)));
        } catch (IndexOutOfBoundsException e) {
            return createTravelPointStatusProblemWith(Collections.singletonList(new NoExternalResultFoundException()), Collections.emptyList());
        } catch (Exception e) {
            log.error("Unable to map Pojo: ", e);
            return createTravelPointStatusProblemWith(Collections.singletonList(e), Collections.emptyList());
        }

    }

    private TravelPoint extractTravelPointFrom(OsmTravelPoint osmTravelPoint) {
        return new TravelPoint.TravelPointBuilder()
                .setStationName(osmTravelPoint.getDisplayName())
                .setCountry(Optional.ofNullable(osmTravelPoint.getAddress().getCountryCode()).map(Locale::forLanguageTag).orElse(null))
                .setPostalCode(Optional.ofNullable(osmTravelPoint.getAddress().getPostcode()).orElse(""))
                .setCity(extractCityFrom(osmTravelPoint.getAddress()))
                .setStreet(extractRoadFrom(osmTravelPoint.getAddress()))
                .setGpsCoordinates(extractCoordinatesFrom(osmTravelPoint))
                .build();
    }

    private Coordinates extractCoordinatesFrom(OsmTravelPoint osmTravelPoint) {
        Coordinates.CoordinatesBuilder coordinates = new Coordinates.CoordinatesBuilder();
        coordinates.setLatitude(Double.parseDouble(osmTravelPoint.getLat()));
        coordinates.setLongitude(Double.parseDouble(osmTravelPoint.getLon()));
        return coordinates.build();
    }

    private String extractCityFrom(Address address) {
        Optional<String> optionalCity = Optional.ofNullable(address.getCity());
        Optional<String> optionalTown = Optional.ofNullable(address.getTown());
        return optionalCity.orElseGet(() -> optionalTown.orElse(""));
    }

    private String extractRoadFrom(Address address) {
        Optional<String> optionalRoad = Optional.ofNullable(address.getRoad());
        Optional<String> optionalHouseNumber = Optional.ofNullable(address.getHouseNumber());
        if (optionalRoad.isPresent() && optionalHouseNumber.isPresent()) {
            return optionalRoad.get().concat(" ").concat(address.getHouseNumber());
        }
        return optionalRoad.orElse("");
    }
}
