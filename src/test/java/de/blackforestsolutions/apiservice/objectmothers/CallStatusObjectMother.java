package de.blackforestsolutions.apiservice.objectmothers;

import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.Status;
import de.blackforestsolutions.generatedcontent.lufthansa.LufthansaAuthorization;

public class CallStatusObjectMother {

    public static CallStatus<LufthansaAuthorization> getLufthansaAuthorizationResponse() {
        return new CallStatus<>(
                getLufthansaAuthorization(),
                Status.SUCCESS,
                null
        );
    }

    private static LufthansaAuthorization getLufthansaAuthorization() {
        LufthansaAuthorization authorization = new LufthansaAuthorization();
        authorization.setAccessToken("xnwudvw9d2chsbku3ezmrbuy");
        authorization.setTokenType("bearer");
        authorization.setExpiresIn(129600);
        return authorization;
    }
}
