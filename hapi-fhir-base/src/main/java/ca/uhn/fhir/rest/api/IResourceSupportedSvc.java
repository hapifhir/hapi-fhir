package ca.uhn.fhir.rest.api;

public interface IResourceSupportedSvc {
	boolean isSupported(String theResourceName);
}
