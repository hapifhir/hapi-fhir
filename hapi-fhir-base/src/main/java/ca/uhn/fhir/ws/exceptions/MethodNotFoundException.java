package ca.uhn.fhir.ws.exceptions;

/**
 * Created by dsotnikov on 2/27/2014.
 */
public class MethodNotFoundException extends Exception {
    public MethodNotFoundException(String error) { super(error); }
}
