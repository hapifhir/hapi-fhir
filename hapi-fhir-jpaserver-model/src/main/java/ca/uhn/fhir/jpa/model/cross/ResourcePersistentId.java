package ca.uhn.fhir.jpa.model.cross;

import ca.uhn.fhir.util.ObjectUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class is an abstraction for however primary keys are stored in the underlying storage engine. This might be
 * a Long, a String, or something else.
 */
public class ResourcePersistentId {

	private Object myId;

	public ResourcePersistentId(Object theId) {
		myId = theId;
	}

	@Override
	public boolean equals(Object theO) {
		if (!(theO instanceof ResourcePersistentId)) {
			return false;
		}
		ResourcePersistentId that = (ResourcePersistentId) theO;

		return ObjectUtil.equals(myId, that.myId);
	}

	@Override
	public int hashCode() {
		return myId.hashCode();
	}

	public Object getId() {
		return myId;
	}

	public void setId(Object theId) {
		myId = theId;
	}

	public Long getIdAsLong() {
		return (Long) myId;
	}

	@Override
	public String toString() {
		return myId.toString();
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
			retVal.add(new ResourcePersistentId(next));
		}
		return retVal;
	}
}
