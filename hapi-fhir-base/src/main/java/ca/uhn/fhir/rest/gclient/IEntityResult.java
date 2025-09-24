package ca.uhn.fhir.rest.gclient;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Response to a Raw HTTP call done through IGenericClient.
 */
public interface IEntityResult {

	String getMimeType();

	@NonNull
	InputStream getInputStream();

	int getStatusCode();

	@NonNull
	Map<String, List<String>> getHeaders();
}
