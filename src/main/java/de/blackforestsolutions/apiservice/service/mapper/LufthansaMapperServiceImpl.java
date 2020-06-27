package de.blackforestsolutions.apiservice.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.blackforestsolutions.apiservice.service.supportservice.UuidService;
import de.blackforestsolutions.datamodel.*;
import de.blackforestsolutions.generatedcontent.lufthansa.Flight;
import de.blackforestsolutions.generatedcontent.lufthansa.LufthansaAuthorization;
import de.blackforestsolutions.generatedcontent.lufthansa.ScheduleResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.EMPTY_LIST;

@Slf4j
@Service
public class LufthansaMapperServiceImpl implements LufthansaMapperService {

    private final Map<String, TravelPoint.TravelPointBuilder> airports;
    private final UuidService uuidService;

    @Autowired
    public LufthansaMapperServiceImpl(Map<String, TravelPoint.TravelPointBuilder> airports, UuidService uuidService) {
        this.airports = airports;
        this.uuidService = uuidService;
    }

    @Override
    public Map<UUID, JourneyStatus> map(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        ScheduleResource scheduleResource;
        try {
            scheduleResource = mapper.readValue(jsonString, ScheduleResource.class);
        } catch (JsonProcessingException e) {
            return Collections.singletonMap(uuidService.createUUID(), JourneyStatusBuilder.createJourneyStatusProblemWith(List.of(e), EMPTY_LIST));
        }
        return mapScheduledResourceToJourneyList(scheduleResource);
    }

    @Override
    public CallStatus<LufthansaAuthorization> mapToAuthorization(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return new CallStatus<>(mapper.readValue(jsonString, LufthansaAuthorization.class), Status.SUCCESS, null);
        } catch (JsonProcessingException e) {
            return new CallStatus<>(null, Status.FAILED, e);
        }
    }

    private Map<UUID, JourneyStatus> mapScheduledResourceToJourneyList(ScheduleResource scheduleResource) {
        return scheduleResource.getScheduleResource().getSchedule()
                .stream()
                .map(entry -> entry.getFlight().stream().findAny())
                .flatMap(Optional::stream)
                .map(this::buildJourneyWith)
                .map(JourneyStatusBuilder::createJourneyStatusWith)
                .collect(Collectors.toMap(JourneyStatusBuilder::extractJourneyUuidFrom, journeyStatus -> journeyStatus));
    }

    private Journey buildJourneyWith(Flight flight) {
        Journey.JourneyBuilder journey = new Journey.JourneyBuilder(uuidService.createUUID());
        LinkedHashMap<UUID, Leg> legs = new LinkedHashMap<>();
        Leg leg = buildLegWith(flight);
        legs.put(leg.getId(), leg);
        journey.setLegs(legs);
        return journey.build();
    }

    private Leg buildLegWith(Flight flight) {
        Leg.LegBuilder leg = new Leg.LegBuilder(uuidService.createUUID());
        leg.setStart(buildTravelPointWith(flight, flight.getDeparture().getAirportCode(), true));
        leg.setDestination(buildTravelPointWith(flight, flight.getArrival().getAirportCode(), false));
        leg.setStartTime(buildDateFrom(flight.getDeparture().getScheduledTimeLocal().getDateTime()));
        leg.setArrivalTime(buildDateFrom(flight.getArrival().getScheduledTimeLocal().getDateTime()));
        leg.setDuration(Duration.between(convertToLocalDateTime(leg.getStartTime()), convertToLocalDateTime(leg.getArrivalTime())));
        leg.setVehicleNumber(flight.getEquipment().getAircraftCode());
        leg.setTravelProvider(TravelProvider.map(flight.getMarketingCarrier().getAirlineID()));
        leg.setProviderId(flight.getMarketingCarrier().getAirlineID().concat(flight.getMarketingCarrier().getFlightNumber().toString()));
        return leg.build();
    }

    private TravelPoint buildTravelPointWith(Flight flight, String airportCode, boolean departure) {
        TravelPoint.TravelPointBuilder travelPoint = airports.get(airportCode);
        if (departure && flight.getDeparture() != null && flight.getDeparture().getTerminal() != null && flight.getDeparture().getTerminal().getName() != null) {
            travelPoint.setTerminal(String.valueOf(flight.getDeparture().getTerminal().getName()));
        }
        if (!departure && flight.getArrival() != null && flight.getArrival().getTerminal() != null && flight.getArrival().getTerminal().getName() != null) {
            travelPoint.setTerminal(String.valueOf(flight.getArrival().getTerminal().getName()));
        }
        return travelPoint.build();
    }

    private Date buildDateFrom(String dateTime) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd'T'mm:ss").parse(dateTime);
        } catch (ParseException e) {
            log.error("Error while parsing Date and was replaced by new Date(): ", e);
            return new Date();
        }
    }

    private LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return LocalDateTime.ofInstant(
                dateToConvert.toInstant(), ZoneId.systemDefault());
    }
}
