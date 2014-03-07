package ca.uhn.fhir.rest.server;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by dsotnikov on 3/7/2014.
 */
public interface ISecurityManager {
    public boolean authenticate(HttpServletRequest request);
}
