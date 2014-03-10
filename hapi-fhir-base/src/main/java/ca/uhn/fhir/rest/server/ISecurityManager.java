package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by dsotnikov on 3/7/2014.
 */
public interface ISecurityManager {
    public void authenticate(HttpServletRequest request) throws AuthenticationException;
}
