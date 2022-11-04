package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JpaPid extends ResourcePersistentId {

	public JpaPid(Long theId) {
		super(theId);
	}

	public JpaPid(Long theId, Long theVersion) {
		super(theId, theVersion);
	}

	public static List<Long> toLongList(Collection<ResourcePersistentId> thePids) {
		List<Long> retVal = new ArrayList<>(thePids.size());
		for (ResourcePersistentId next : thePids) {
			retVal.add(next.getIdAsLong());
		}
		return retVal;
	}

	public static List<ResourcePersistentId> fromLongList(List<Long> theResultList) {
		List<ResourcePersistentId> retVal = new ArrayList<>(theResultList.size());
		for (Long next : theResultList) {
			retVal.add(new JpaPid(next));
		}
		return retVal;
	}
}
