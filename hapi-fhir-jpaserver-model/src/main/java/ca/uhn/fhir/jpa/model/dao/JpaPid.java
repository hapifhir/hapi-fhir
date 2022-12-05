package ca.uhn.fhir.jpa.model.dao;

import ca.uhn.fhir.rest.api.server.storage.BaseResourcePersistentId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class JpaPid extends BaseResourcePersistentId<Long> {
	private final Long myId;

	public JpaPid(Long theId) {
		super(null);
		myId = theId;
	}

	public JpaPid(Long theId, Long theVersion) {
		super(theVersion, null);
		myId = theId;
	}

	public JpaPid(Long theId, String theResourceType) {
		super(theResourceType);
		myId = theId;
	}

	public JpaPid(Long theId, Long theVersion, String theResourceType) {
		super(theVersion, theResourceType);
		myId = theId;
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

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;
		if (theO == null || getClass() != theO.getClass()) return false;
		if (!super.equals(theO)) return false;
		JpaPid jpaPid = (JpaPid) theO;
		return myId.equals(jpaPid.myId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), myId);
	}

	@Override
	public Long getId() {
		return myId;
	}

	@Override
	public String toString() {
		return myId.toString();
	}
}
