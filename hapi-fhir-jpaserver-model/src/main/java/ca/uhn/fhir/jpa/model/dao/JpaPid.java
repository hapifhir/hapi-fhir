package ca.uhn.fhir.jpa.model.dao;

import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JpaPid extends ResourcePersistentId<Long> {
	public JpaPid(Long theId) {
		super(theId);
	}

	public JpaPid(Long theId, Long theVersion) {
		super(theId, theVersion);
	}

	public static List<Long> toLongList(Collection<JpaPid> thePids) {
		List<Long> retVal = new ArrayList<>(thePids.size());
		for (JpaPid next : thePids) {
			retVal.add(next.getId());
		}
		return retVal;
	}

	public static List<JpaPid> fromLongList(List<Long> theResultList) {
		List<JpaPid> retVal = new ArrayList<>(theResultList.size());
		for (Long next : theResultList) {
			retVal.add(new JpaPid(next));
		}
		return retVal;
	}
}
