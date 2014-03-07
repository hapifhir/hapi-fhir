package ca.uhn.fhir.parser;

import java.io.Reader;
import java.io.Writer;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;

public interface IParser {

	String encodeBundleToString(Bundle theBundle) throws DataFormatException;

	void encodeBundleToWriter(Bundle theBundle, Writer theWriter);

	String encodeResourceToString(IResource theResource) throws DataFormatException;

	void encodeResourceToWriter(IResource theResource, Writer stringWriter);

	void encodeResourceToXmlStreamWriter(IResource theResource, XMLStreamWriter eventWriter) throws XMLStreamException, DataFormatException;

	Bundle parseBundle(Reader theReader);

	Bundle parseBundle(String theMessageString) throws ConfigurationException, DataFormatException;

	IResource parseResource(String theMessageString) throws ConfigurationException, DataFormatException;

	IResource parseResource(Reader theReader) throws ConfigurationException, DataFormatException;

	IResource parseResource(XMLEventReader theStreamReader);

	<T extends IResource> T parseResource(Class<T> theResourceType, String theMessageString);

	IResource parseResource(Class<? extends IResource> theResourceType, Reader theReader);

}