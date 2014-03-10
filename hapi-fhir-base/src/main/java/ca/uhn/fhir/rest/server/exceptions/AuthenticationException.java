package ca.uhn.fhir.rest.server.exceptions;

import javax.servlet.ServletException;

/**
 * Created by dsotnikov on 3/10/2014.
 */
public class AuthenticationException extends BaseServerResponseException {

    private static final long serialVersionUID = 1L;

    public AuthenticationException() {
        super(401, "Client unauthorized");
    }

    public AuthenticationException(String theMessage) {
        super(401, theMessage);
    }

    public AuthenticationException(int theStatusCode, String theMessage) {
        super(theStatusCode, theMessage);
    }
}