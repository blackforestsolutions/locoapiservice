package de.blackforestsolutions.apiservice.service.supportservice.hvv;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.generatedcontent.hvv.request.HvvStationListBody;
import de.blackforestsolutions.generatedcontent.hvv.request.ModificationType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.setFormatToJsonFor;

@Service
public class HvvStationListHttpCallBuilderServiceImpl implements HvvStationListHttpCallBuilderService {

    @SuppressWarnings("rawtypes")
    @Override
    public HttpEntity buildStationListHttpEntityForHvv(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        return new HttpEntity<>(
                buildStationListHttpBodyForHvv(apiTokenAndUrlInformation),
                buildHttpHeadersForHvvStationListWith(apiTokenAndUrlInformation)
        );
    }

    @Override
    public String buildStationListPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getPathVariable(), "path variable is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getStationListPathVariable(), "station list path variable is not allowed to be null");
        return "/"
                .concat(apiTokenAndUrlInformation.getPathVariable())
                .concat(apiTokenAndUrlInformation.getStationListPathVariable());
    }

    private HttpHeaders buildHttpHeadersForHvvStationListWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        HttpHeaders httpHeaders = new HttpHeaders();
        setFormatToJsonFor(httpHeaders);
        HvvHttpCallBuilder.setBaseHttpHeaderFor(httpHeaders, apiTokenAndUrlInformation);

        String jsonBody = buildStationListHttpBodyForHvv(apiTokenAndUrlInformation);
        HvvHttpCallBuilder.setHvvAuthentificationSignatureFor(httpHeaders, apiTokenAndUrlInformation, jsonBody.getBytes(StandardCharsets.UTF_8));

        return httpHeaders;
    }

    private String buildStationListHttpBodyForHvv(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        HvvStationListBody hvvStationListBody = new HvvStationListBody();
        hvvStationListBody.setFilterEquivalent(apiTokenAndUrlInformation.getHvvFilterEquivalent());
        hvvStationListBody.setModificationTypes(ModificationType.MAIN);

        return HvvHttpCallBuilder.combineBaseHttpBodyWithApiCallBody(hvvStationListBody, apiTokenAndUrlInformation);
    }

}
