package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseTerminologyImportParameters;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Properties;

public class ImportLoincJobParameters extends BaseTerminologyImportParameters {

	@JsonIgnore
	private Properties myJobProperties;

	public Properties getJobProperties() {
		return myJobProperties;
	}

	public void setJobProperties(Properties theJobProperties) {
		myJobProperties = theJobProperties;
	}

	// FIXME: remove
	/*
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

	 */
}
