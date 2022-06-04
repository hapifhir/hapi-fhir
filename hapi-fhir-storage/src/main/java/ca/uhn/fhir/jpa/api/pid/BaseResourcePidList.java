package ca.uhn.fhir.jpa.api.pid;

import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

abstract public class BaseResourcePidList implements IResourcePidList {
	private static final IResourcePidList EMPTY_CHUNK = new EmptyResourcePidList();

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
	public List<TypedResourcePid> getBatchResourceIds() {
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

	// FIXME KHS test
	public static IResourcePidList fromChunksAndDate(List<IResourcePidList> theChunks, Date theEnd) {
		if (theChunks.isEmpty()) {
			return BaseResourcePidList.empty();
		}

		List<ResourcePersistentId> ids = new ArrayList<>();

		Date endDate = null;
		boolean containsMixed = false;
		for (IResourcePidList chunk : theChunks) {
			ids.addAll(chunk.getIds());
			endDate = getLatestDate(chunk, endDate, theEnd);
			if (chunk instanceof MixedResourcePidList) {
				containsMixed = true;
			}
		}

		if (containsMixed) {
			List<String> types = new ArrayList<>();
			for (IResourcePidList chunk : theChunks) {
				for (int i = 0; i < chunk.size(); ++i) {
					types.add(chunk.getResourceType(i));
				}
			}
			return new MixedResourcePidList(ids, types, endDate);
		} else {
			IResourcePidList firstChunk = theChunks.get(0);
			String onlyResourceType = firstChunk.getResourceType(0);
			return new HomogeneousResourcePidList(ids, onlyResourceType, endDate);
		}
	}

	private static IResourcePidList empty() {
		return EMPTY_CHUNK;
	}

	private static Date getLatestDate(IResourcePidList theChunk, Date theCurrentEndDate, Date thePassedInEndDate) {
		Date endDate = theCurrentEndDate;
		if (theCurrentEndDate == null) {
			endDate = theChunk.getLastDate();
		} else if (theChunk.getLastDate().after(endDate)
			&& theChunk.getLastDate().before(thePassedInEndDate)) {
			endDate = theChunk.getLastDate();
		}
		return endDate;
	}
}

