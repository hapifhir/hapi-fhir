package ca.uhn.hapi.fhir.sql.hibernatesvc;

import org.hibernate.service.Service;

/**
 * A hibernate {@link Service} which provides the HAPI FHIR Storage Settings. This
 * class is registered into the Hibernate {@link org.hibernate.service.ServiceRegistry},
 * and provides a way of passing information from the startup context (e.g.
 * configuration that comes from outside) into internal hibernate services,
 * since these are instantiated by Hibernate itself by class name and therefore
 * can't easily have config data injected in.
 */
public class HapiHibernateDialectSettingsService implements Service {

	private boolean myDatabasePartitionMode;

	/**
	 * Constructor
	 */
	public HapiHibernateDialectSettingsService() {
		super();
	}

	/**
	 * Are we in Database Partition Mode?
	 */
	public boolean isDatabasePartitionMode() {
		return myDatabasePartitionMode;
	}

	/**
	 * Are we in Database Partition Mode?
	 */
	public void setDatabasePartitionMode(boolean theDatabasePartitionMode) {
		myDatabasePartitionMode = theDatabasePartitionMode;
	}
}
