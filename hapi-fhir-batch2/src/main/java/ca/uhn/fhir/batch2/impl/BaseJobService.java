package ca.uhn.fhir.batch2.impl;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;

public abstract class BaseJobService {
	protected final IJobPersistence myJobPersistence;

	/**
	 * Constructor
	 *
	 * @param theJobPersistence The persistence service
	 */
	public BaseJobService(@Nonnull IJobPersistence theJobPersistence) {
		Validate.notNull(theJobPersistence);
		myJobPersistence = theJobPersistence;
	}
}
