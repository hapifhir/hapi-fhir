package ca.uhn.fhir.rest.api.server;

import java.util.*;

public interface IRequestDetails {

	Map<String, String[]> getParameters();

	Map<String, List<String>> getUnqualifiedToQualifiedNames();

	IRestfulServer getServer();

}
