package ca.uhn.fhir.jpa.api.svc;

import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class BatchIdChunk {

	final List<ResourcePersistentId> myIds;
	@Nullable
	final List<String> myResourceTypes;
	@Nullable
	final String myOnlyResourceType;
	@Nullable
	final Date myLastDate;
	private static final BatchIdChunk EMPTY_CHUNK = new BatchIdChunk(Collections.emptyList(), Collections.emptyList(), null);

	public BatchIdChunk(List<ResourcePersistentId> theIds, List<String> theResourceTypes, Date theLastDate) {
		myIds = theIds;
		myResourceTypes = theResourceTypes;
		myOnlyResourceType = null;
		myLastDate = theLastDate;
	}

	public BatchIdChunk(List<ResourcePersistentId> theIds, String theResourceType, Date theLastDate) {
		myIds = theIds;
		myResourceTypes = null;
		myOnlyResourceType = theResourceType;
		myLastDate = theLastDate;
	}

	public Date getLastDate() {
		return myLastDate;
	}

	public int size() {
		return myIds.size();
	}


	@Nonnull
	public List<BatchResourceId> getBatchResourceIds() {
		List<BatchResourceId> retval = new ArrayList<>();
		for (int i = 0; i < myIds.size(); ++i) {
			retval.add(new BatchResourceId(getResourceType(i), myIds.get(i)));
		}
		return Collections.unmodifiableList(retval);
	}

	private String getResourceType(int i) {
		if (myOnlyResourceType != null) {
			return myOnlyResourceType;
		}
		return myResourceTypes.get(i);
	}

	public boolean isEmpty() {
		return myIds.isEmpty();
	}

	// FIXME KHS test
	public static BatchIdChunk fromChunksAndDate(List<BatchIdChunk> theChunks, Date theEnd) {
		if (theChunks.isEmpty()) {
			return BatchIdChunk.empty();
		}

		List<ResourcePersistentId> ids = new ArrayList<>();
		String onlyResourceType = null;
		List<String> types = new ArrayList<>();

		Date endDate = null;
		for (BatchIdChunk chunk : theChunks) {
			ids.addAll(chunk.myIds);
			endDate = getLatestDate(chunk, endDate, theEnd);
			if (chunk.myOnlyResourceType != null) {
				onlyResourceType = chunk.myOnlyResourceType;
			} else {
				types.addAll(chunk.myResourceTypes);
			}
		}

		if (onlyResourceType != null) {
			return new BatchIdChunk(ids, onlyResourceType, endDate);
		} else {
			return new BatchIdChunk(ids, types, endDate);
		}
	}

	private static BatchIdChunk empty() {
		return EMPTY_CHUNK;
	}

	private static Date getLatestDate(BatchIdChunk theChunk, Date theCurrentEndDate, Date thePassedInEndDate) {
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
