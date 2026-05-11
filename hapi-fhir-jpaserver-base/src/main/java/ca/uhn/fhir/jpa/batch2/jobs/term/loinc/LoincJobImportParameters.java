package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseTerminologyImportParameters;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

import static org.apache.commons.lang3.ObjectUtils.getIfNull;

public class LoincJobImportParameters extends BaseTerminologyImportParameters {

	private Properties myParsedProperties;

	public Properties getProperties() {
		if (myParsedProperties == null) {
			Properties parsedProperties = new Properties();
			try {
				parsedProperties.load(new StringReader(getIfNull(myProperties, "")));
			} catch (IOException e) {
				// FIXME: add code
				throw new InternalErrorException(Msg.code(1) + "Failed to parse properties: " + e.getMessage(), e);
			}
			myParsedProperties = parsedProperties;
		}
		return myParsedProperties;
	}

	public void setProperties(String theProperties) {
		myProperties = theProperties;
		myParsedProperties = null;
	}

	@JsonProperty("properties")
	private String myProperties;
}
