package de.blackforestsolutions.apiservice.service.supportservice.hvv;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.blackforestsolutions.apiservice.configuration.AdditionalHttpConfiguration;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.generatedcontent.hvv.request.FilterType;
import de.blackforestsolutions.generatedcontent.hvv.request.HvvBaseBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Slf4j
class HvvHttpCallBuilder {

    static void setBaseHttpHeaderFor(HttpHeaders httpHeaders, ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add(AdditionalHttpConfiguration.GEO_FEX_AUTH_TYPE, apiTokenAndUrlInformation.getAuthentificationType());
        httpHeaders.add(AdditionalHttpConfiguration.GEO_FEX_AUTH_USER, apiTokenAndUrlInformation.getAuthentificationUser());
        httpHeaders.add(AdditionalHttpConfiguration.X_TRACE_ID, UUID.randomUUID().toString());
    }

    static <T> String combineBaseHttpBodyWithApiCallBody(T specificApiBody, ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        HvvBaseBody hvvBaseBody = buildBaseHttpBodyWith(apiTokenAndUrlInformation);

        BeanUtils.copyProperties(hvvBaseBody, specificApiBody);

        return convertHttpModelToJson(specificApiBody);
    }

    static void setHvvAuthentificationSignatureFor(HttpHeaders httpHeaders, ApiTokenAndUrlInformation apiTokenAndUrlInformation, byte[] requestBody) {
        byte[] key = apiTokenAndUrlInformation.getAuthentificationPassword().getBytes(StandardCharsets.UTF_8);
        SecretKeySpec keySpec = new SecretKeySpec(key, apiTokenAndUrlInformation.getAuthentificationType());
        Mac mac = createMacWith(apiTokenAndUrlInformation);
        initMac(mac, keySpec);
        byte[] signature = mac.doFinal(requestBody);
        httpHeaders.add(AdditionalHttpConfiguration.GEO_FEX_AUTH_SIGNATURE, DatatypeConverter.printBase64Binary(signature));
    }

    private static void initMac(Mac mac, SecretKeySpec keySpec) {
        try {
            mac.init(keySpec);
        } catch (InvalidKeyException e) {
            log.error("Error while initializing Mac because of key spec config: {}", keySpec, e);
            throw new UncheckedIOException(new IOException(e));
        }
    }

    private static Mac createMacWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        try {
            return Mac.getInstance(apiTokenAndUrlInformation.getAuthentificationType());
        } catch (NoSuchAlgorithmException e) {
            log.error("Error while creating Mac because of wrong crypto algorithm in config: {}", apiTokenAndUrlInformation.getAuthentificationType(), e);
            throw new UncheckedIOException(new IOException(e));
        }
    }

    private static <T> String convertHttpModelToJson(T model) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(model);
        } catch (JsonProcessingException e) {
            log.error("Error during mapping http model to json: {}", model, e);
            return null;
        }
    }

    private static HvvBaseBody buildBaseHttpBodyWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        HvvBaseBody hvvBaseBody = new HvvBaseBody();
        hvvBaseBody.setLanguage(apiTokenAndUrlInformation.getLanguage());
        hvvBaseBody.setVersion(Double.parseDouble(apiTokenAndUrlInformation.getApiVersion()));
        hvvBaseBody.setFilterType(FilterType.NO_FILTER);
        return hvvBaseBody;
    }


}
