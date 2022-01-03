package ca.uhn.fhir.jpa.model.dialect;

import org.hibernate.dialect.H2Dialect;

/**
 * HAPI FHIR dialect for H2 database
 */
public class HapiFhirH2Dialect extends H2Dialect {

	/**
	 * Workaround until this bug is fixed:
	 * https://hibernate.atlassian.net/browse/HHH-15002
	 */
	@Override
	public String toBooleanValueString(boolean bool) {
		return bool ? "true" : "false";
	}
}
