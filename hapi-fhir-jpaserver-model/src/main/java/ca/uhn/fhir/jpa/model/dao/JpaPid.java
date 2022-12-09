package ca.uhn.fhir.jpa.model.dao;

import ca.uhn.fhir.rest.api.server.storage.BaseResourcePersistentId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * JPA implementation of IResourcePersistentId.  JPA uses a Long as the primary key.  This class should be used in any
 * context where the pid is known to be a Long.
 */
public class JpaPid extends BaseResourcePersistentId<Long> {
	private final Long myId;

	private JpaPid(Long theId) {
		super(null);
		myId = theId;
	}

	private JpaPid(Long theId, Long theVersion) {
		super(theVersion, null);
		myId = theId;
	}

	private JpaPid(Long theId, String theResourceType) {
		super(theResourceType);
		myId = theId;
	}

	private JpaPid(Long theId, Long theVersion, String theResourceType) {
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
			retVal.add(fromId(next));
		}
		return retVal;
	}

	public static JpaPid fromId(Long theId) {
		return new JpaPid(theId);
	}

	public static JpaPid fromIdAndVersion(Long theId, Long theVersion) {
		return new JpaPid(theId, theVersion);
	}

	public static JpaPid fromIdAndResourceType(Long theId, String theResourceType) {
		return new JpaPid(theId, theResourceType);
	}

	public static JpaPid fromIdAndVersionAndResourceType(Long theId, Long theVersion, String theResourceType) {
		return new JpaPid(theId, theVersion, theResourceType);
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
