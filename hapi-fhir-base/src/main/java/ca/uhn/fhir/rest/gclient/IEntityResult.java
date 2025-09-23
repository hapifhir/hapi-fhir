package ca.uhn.fhir.rest.gclient;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface IEntityResult{
	String getMimeType();
	InputStream getInputStream();
	int getStatusCode();
	Map<String, List<String>> getHeaders();
}
