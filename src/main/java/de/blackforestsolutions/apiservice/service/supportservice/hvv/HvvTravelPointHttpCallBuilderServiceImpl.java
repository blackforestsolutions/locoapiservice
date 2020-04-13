package de.blackforestsolutions.apiservice.service.supportservice.hvv;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.generatedcontent.hvv.request.HvvStation;
import de.blackforestsolutions.generatedcontent.hvv.request.HvvTravelPointBody;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.setFormatToJsonFor;

@Service
public class HvvTravelPointHttpCallBuilderServiceImpl implements HvvTravelPointHttpCallBuilderService {

    @SuppressWarnings("rawtypes")
    @Override
    public HttpEntity buildTravelPointHttpEntityForHvv(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String station) {
        return new HttpEntity<>(
                buildTravelPointHttpBodyForHvv(apiTokenAndUrlInformation, station),
                buildHttpHeadersForHvvTravelPointWith(apiTokenAndUrlInformation, station)
        );
    }

    @Override
    public String buildTravelPointPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getPathVariable(), "path variable is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getTravelPointPathVariable(), "travelpoint path variable is not allowed to be null");
        return "/"
                .concat(apiTokenAndUrlInformation.getPathVariable())
                .concat(apiTokenAndUrlInformation.getTravelPointPathVariable());
    }

    private HttpHeaders buildHttpHeadersForHvvTravelPointWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String station) {
        HttpHeaders httpHeaders = new HttpHeaders();
        setFormatToJsonFor(httpHeaders);
        HvvHttpCallBuilder.setBaseHttpHeaderFor(httpHeaders, apiTokenAndUrlInformation);

        String jsonBody = buildTravelPointHttpBodyForHvv(apiTokenAndUrlInformation, station);
        HvvHttpCallBuilder.setHvvAuthentificationSignatureFor(httpHeaders, apiTokenAndUrlInformation, jsonBody.getBytes(StandardCharsets.UTF_8));

        return httpHeaders;
    }


    private String buildTravelPointHttpBodyForHvv(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String station) {
        HvvTravelPointBody hvvTravelPointBody = new HvvTravelPointBody();

        hvvTravelPointBody.setTheName(new HvvStation(station));
        hvvTravelPointBody.setAllowTypeSwitch(apiTokenAndUrlInformation.getHvvAllowTypeSwitch());
        hvvTravelPointBody.setMaxList(apiTokenAndUrlInformation.getResultLength());
        hvvTravelPointBody.setMaxDistance(apiTokenAndUrlInformation.getDistanceFromTravelPoint());
        hvvTravelPointBody.setTariffDetails(apiTokenAndUrlInformation.getAllowTariffDetails());

        return HvvHttpCallBuilder.combineBaseHttpBodyWithApiCallBody(hvvTravelPointBody, apiTokenAndUrlInformation);

    }
}
