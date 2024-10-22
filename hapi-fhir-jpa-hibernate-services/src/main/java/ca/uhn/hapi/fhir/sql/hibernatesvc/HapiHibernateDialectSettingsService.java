package ca.uhn.hapi.fhir.sql.hibernatesvc;

import org.hibernate.service.Service;

/**
 * A hibernate {@link Service} which provides the HAPI FHIR Storage Settings.
 */
public class HapiHibernateDialectSettingsService implements Service {

	private boolean myTrimConditionalIdsFromPrimaryKeys;

	public boolean isTrimConditionalIdsFromPrimaryKeys() {
		return myTrimConditionalIdsFromPrimaryKeys;
	}

	public void setTrimConditionalIdsFromPrimaryKeys(boolean theTrimConditionalIdsFromPrimaryKeys) {
		myTrimConditionalIdsFromPrimaryKeys = theTrimConditionalIdsFromPrimaryKeys;
	}
}
