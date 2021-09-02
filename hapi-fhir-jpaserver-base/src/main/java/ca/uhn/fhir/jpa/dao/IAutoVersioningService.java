package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Collection;
import java.util.Map;

public interface IAutoVersioningService {
	/**
	 * Takes in a list of IIdType and returns a map of
	 * IIdType -> ResourcePersistentId.
	 * ResourcePersistentId will contain the history version
	 * but the returned IIdType will not (this is to allow consumers
	 * to use their passed in IIdType as a lookup value).
	 *
	 * If the returned map does not return an IIdType -> ResourcePersistentId
	 * then it means that it is a non-existing resource in the DB
	 * @param theIds
	 * @return
	 */
	Map<IIdType, ResourcePersistentId> getAutoversionsForIds(Collection<IIdType> theIds);
}
