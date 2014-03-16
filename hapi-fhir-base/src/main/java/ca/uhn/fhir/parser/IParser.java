package ca.uhn.fhir.parser;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;

public interface IParser {

	String encodeBundleToString(Bundle theBundle) throws DataFormatException, IOException;

	void encodeBundleToWriter(Bundle theBundle, Writer theWriter) throws IOException;

	String encodeResourceToString(IResource theResource) throws DataFormatException, IOException;

	void encodeResourceToWriter(IResource theResource, Writer stringWriter) throws IOException;

	Bundle parseBundle(Reader theReader);

	Bundle parseBundle(String theMessageString) throws ConfigurationException, DataFormatException;

	IResource parseResource(String theMessageString) throws ConfigurationException, DataFormatException;

	IResource parseResource(Reader theReader) throws ConfigurationException, DataFormatException;

	<T extends IResource> T parseResource(Class<T> theResourceType, String theMessageString);

	IResource parseResource(Class<? extends IResource> theResourceType, Reader theReader);

	IParser setPrettyPrint(boolean thePrettyPrint);

}