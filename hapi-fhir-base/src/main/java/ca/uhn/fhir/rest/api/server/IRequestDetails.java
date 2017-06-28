package ca.uhn.fhir.rest.api.server;

import java.util.*;

import ca.uhn.fhir.context.FhirContext;

public interface IRequestDetails {

	Map<String, String[]> getParameters();

	Map<String, List<String>> getUnqualifiedToQualifiedNames();

	FhirContext getFhirContext();

}
