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
     /**
       * Gets the response body as an InputStream. 
       * 
       * <p><strong>Warning:</strong> The returned stream can only be read once.
       * Subsequent calls to this method will return the same exhausted stream.
       * Consider copying the content to a byte array if multiple reads are needed.</p>
       * 
       * @return The response body as an InputStream (single-use only)
       */
	InputStream getInputStream();

	int getStatusCode();

	@NonNull
	Map<String, List<String>> getHeaders();
}
