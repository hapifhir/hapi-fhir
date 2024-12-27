package ca.uhn.hapi.fhir.sql.hibernatesvc;

import org.hibernate.service.Service;

/**
 * A hibernate {@link Service} which provides the HAPI FHIR Storage Settings.
 */
public class HapiHibernateDialectSettingsService implements Service {

	private boolean myDatabasePartitionMode;

	public boolean isDatabasePartitionMode() {
		return myDatabasePartitionMode;
	}

	public void setDatabasePartitionMode(boolean theDatabasePartitionMode) {
		myDatabasePartitionMode = theDatabasePartitionMode;
	}
}
