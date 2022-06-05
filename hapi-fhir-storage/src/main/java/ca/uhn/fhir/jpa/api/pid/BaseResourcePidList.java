package ca.uhn.fhir.jpa.api.pid;

import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

abstract public class BaseResourcePidList implements IResourcePidList {

	final List<ResourcePersistentId> myIds;

	@Nullable
	final Date myLastDate;

	BaseResourcePidList(List<ResourcePersistentId> theIds, Date theLastDate) {
		myIds = theIds;
		myLastDate = theLastDate;
	}

	@Override
	public Date getLastDate() {
		return myLastDate;
	}

	@Override
	public int size() {
		return myIds.size();
	}

	@Override
	@Nonnull
	public List<TypedResourcePid> getTypedResourcePids() {
		List<TypedResourcePid> retval = new ArrayList<>();
		for (int i = 0; i < myIds.size(); ++i) {
			retval.add(new TypedResourcePid(getResourceType(i), myIds.get(i)));
		}
		return Collections.unmodifiableList(retval);
	}

	@Override
	public boolean isEmpty() {
		return myIds.isEmpty();
	}

	@Override
	public List<ResourcePersistentId> getIds() {
		return Collections.unmodifiableList(myIds);
	}

}

