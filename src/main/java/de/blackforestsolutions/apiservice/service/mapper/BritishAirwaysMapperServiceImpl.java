package de.blackforestsolutions.apiservice.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.blackforestsolutions.apiservice.configuration.TimeConfiguration;
import de.blackforestsolutions.apiservice.service.supportservice.UuidService;
import de.blackforestsolutions.datamodel.*;
import de.blackforestsolutions.generatedcontent.britishairways.FlightsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static de.blackforestsolutions.apiservice.service.mapper.JourneyStatusBuilder.createJourneyStatusProblemWith;
import static de.blackforestsolutions.apiservice.service.mapper.JourneyStatusBuilder.createJourneyStatusWith;

@SuppressWarnings("rawtypes")
@Slf4j
@Service
public class BritishAirwaysMapperServiceImpl implements BritishAirwaysMapperService {

    private final Map<String, TravelPoint.TravelPointBuilder> airports;
    private final UuidService uuidService;

    @Autowired
    public BritishAirwaysMapperServiceImpl(Map<String, TravelPoint.TravelPointBuilder> airports, UuidService uuidService) {
        this.airports = airports;
        this.uuidService = uuidService;
    }

    private static List<Object> mapLinkedHashMapToFlightObject(LinkedHashMap entry) {
        List<Object> values = new ArrayList<>();
        for (Object o : entry.entrySet()) {
            Map.Entry me = (Map.Entry) o;
            if (me.getValue().getClass() == ArrayList.class) {
                values.add(me.getValue());
            }
        }
        return values;
    }

    @Override
    public Map<UUID, JourneyStatus> map(String jsonString) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        return britishAirwaysMapFlightResponseToJourneyList(mapper.readValue(jsonString, FlightsResponse.class));
    }

    @SuppressWarnings("unchecked")
    private Map<UUID, JourneyStatus> britishAirwaysMapFlightResponseToJourneyList(FlightsResponse flightsResponse) {
        return flightsResponse
                .getAdditionalProperties()
                .values()
                .stream()
                .map(entry -> (LinkedHashMap) entry)
                .map(BritishAirwaysMapperServiceImpl::mapLinkedHashMapToFlightObject)
                .flatMap(List::stream)
                .map(flights -> (ArrayList<LinkedHashMap>) flights)
                .flatMap(ArrayList::stream)
                .map(this::createJourneysWith)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    }

    private Map.Entry<UUID, JourneyStatus> createJourneysWith(LinkedHashMap singleFlight) {
        try {
            Journey.JourneyBuilder journey = new Journey.JourneyBuilder(uuidService.createUUID());
            LinkedHashMap<UUID, Leg> legs = new LinkedHashMap<>();
            Leg leg = createLegWith(singleFlight);
            legs.put(leg.getId(), leg);
            journey.setLegs(legs);
            return Map.entry(journey.getId(), createJourneyStatusWith(journey.build()));
        } catch (Exception e) {
            log.error("Unable to map Pojo: ", e);
            return Map.entry(uuidService.createUUID(), createJourneyStatusProblemWith(List.of(e), Collections.emptyList()));
        }
    }

    private Leg createLegWith(LinkedHashMap singleFlight) {
        Leg.LegBuilder leg = new Leg.LegBuilder(uuidService.createUUID());
        leg.setTravelProvider(TravelProvider.map((String) singleFlight.get("MarketingCarrierCode")));
        leg.setProviderId(getLegProviderId(singleFlight));
        LinkedHashMap sector = (LinkedHashMap) singleFlight.get("Sector");
        setLegReportedDepartureDateTime(leg, sector);
        setLegReportedArrivalDateTime(leg, sector);
        setLegTravelProvider(leg, singleFlight);
        setLegUnknownTravelProvider(leg, sector);
        setLegVehicleNumber(sector, leg);
        setLegDestination(sector, leg);
        setLegStart(sector, leg);
        return leg.build();
    }

    private void setLegVehicleNumber(LinkedHashMap sector, Leg.LegBuilder leg) {
        if (sector.get("AircraftTypeCode") != null) {
            String aircraftTypeCode = null;
            if (sector.get("AircraftTypeCode").getClass().equals(Integer.class)) {
                int aircraftTypeCodeInt = (Integer) sector.get("AircraftTypeCode");
                aircraftTypeCode = String.valueOf(aircraftTypeCodeInt);
            }
            if (sector.get("AircraftTypeCode").getClass().equals(String.class)) {
                aircraftTypeCode = (String) sector.get("AircraftTypeCode");
            }
            leg.setVehicleNumber(aircraftTypeCode);
        }
    }

    private void setLegStart(LinkedHashMap sector, Leg.LegBuilder leg) {
        String departureAirport = (String) sector.get("DepartureAirport");
        if (sector.get("DepartureTerminal") != null) {
            int departureTerminal = (int) sector.get("DepartureTerminal");
            leg.setStart(buildTravelPointWith(departureAirport, departureTerminal, true));
        } else {
            leg.setStart(buildTravelPointWithoutTerminal(departureAirport, true));
        }

    }

    private void setLegDestination(LinkedHashMap sector, Leg.LegBuilder leg) {
        String arrivalAirport = (String) sector.get("ArrivalAirport");
        if (sector.get("ArrivalTerminal") != null) {
            int arrivalTerminal = (int) sector.get("ArrivalTerminal");
            leg.setDestination(buildTravelPointWith(arrivalAirport, arrivalTerminal, false));
        } else {
            leg.setDestination(buildTravelPointWithoutTerminal(arrivalAirport, false));
        }
    }

    private TravelPoint buildTravelPointWithoutTerminal(String airportCode, boolean isDeparture) {
        TravelPoint travelPoint;
        if (!isDeparture && airportCode != null) {
            travelPoint = new TravelPoint(airports.get(airportCode).build());
            return travelPoint;
        }
        if (isDeparture && airportCode != null) {
            travelPoint = new TravelPoint(airports.get(airportCode).build());
            return travelPoint;
        }
        return new TravelPoint.TravelPointBuilder().build();
    }


    private TravelPoint buildTravelPointWith(String airportCode, int terminal, boolean isDeparture) {
        TravelPoint.TravelPointBuilder travelPoint;
        if (isDeparture && airportCode != null) {
            travelPoint = airports.get(airportCode);
            travelPoint.setTerminal(String.valueOf(terminal));
            return travelPoint.build();
        }
        if (!isDeparture && airportCode != null) {
            travelPoint = airports.get(airportCode);
            travelPoint.setTerminal(String.valueOf(terminal));
            return travelPoint.build();
        }
        return new TravelPoint.TravelPointBuilder().build();
    }


    private String getLegProviderId(LinkedHashMap singleFlight) {
        if (singleFlight.get("FlightNumber") != null) {
            String flightNumber = null;
            if (singleFlight.get("FlightNumber").getClass().equals(Integer.class)) {
                int flightNumberInt = (Integer) singleFlight.get("FlightNumber");
                flightNumber = String.valueOf(flightNumberInt);
            }
            if (singleFlight.get("FlightNumber").getClass().equals(String.class)) {
                flightNumber = (String) singleFlight.get("FlightNumber");
            }
            return flightNumber;
        }
        return null;
    }

    private void setLegTravelProvider(Leg.LegBuilder leg, LinkedHashMap singleFlight) {
        TravelProvider travelprovider = TravelProvider.map((String) singleFlight.get("MarketingCarrierCode"));
        leg.setTravelProvider(travelprovider);
    }

    private void setLegUnknownTravelProvider(Leg.LegBuilder leg, LinkedHashMap sector) {
        String operatingCarrierCode = (String) sector.get("OperatingCarrierCode");
        leg.setUnknownTravelProvider(operatingCarrierCode);
    }

    private void setLegReportedArrivalDateTime(Leg.LegBuilder leg, LinkedHashMap sector) {
        String reportedArrivalDateTime = (String) sector.get("ReportedArrivalDateTime");
        Optional.ofNullable(reportedArrivalDateTime).ifPresent(arrivalTime -> leg.setArrivalTime(buildDateFrom(reportedArrivalDateTime)));
        if (!Optional.ofNullable(leg.getArrivalTime()).isPresent()) {
            String scheduledArrivalDateTime = (String) sector.get("ScheduledArrivalDateTime");
            leg.setArrivalTime(buildDateFrom(scheduledArrivalDateTime));
        }
    }

    private void setLegReportedDepartureDateTime(Leg.LegBuilder leg, LinkedHashMap sector) {
        String reportedDepartureDateTime = (String) sector.get("ReportedDepartureDateTime");
        Optional.ofNullable(reportedDepartureDateTime).ifPresent(startTime -> leg.setStartTime(buildDateFrom(reportedDepartureDateTime)));
        if (!Optional.ofNullable(leg.getStartTime()).isPresent()) {
            String scheduledDepartureDateTime = (String) sector.get("ScheduledDepartureDateTime");
            leg.setStartTime(buildDateFrom(scheduledDepartureDateTime));
        }
    }

    private ZonedDateTime buildDateFrom(String dateTime) {
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_DATE_TIME).atZone(TimeConfiguration.GERMAN_TIME_ZONE);
    }
}
