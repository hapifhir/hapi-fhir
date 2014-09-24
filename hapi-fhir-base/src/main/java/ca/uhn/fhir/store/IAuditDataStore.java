package ca.uhn.fhir.store;

import ca.uhn.fhir.model.dstu.resource.SecurityEvent;

/**
 * This interface provides a way to persist FHIR SecurityEvents to any kind of data store
 */
public interface IAuditDataStore {
	
	/**
	 * Take in a SecurityEvent object and handle storing it to a persistent data store (database, JMS, file, etc).
	 * @param auditEvent a FHIR SecurityEvent to be persisted
	 * @throws Exception if there is an error while persisting the data
	 */
	public void store(SecurityEvent auditEvent) throws Exception;

}
