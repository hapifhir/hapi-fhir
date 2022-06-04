package ca.uhn.fhir.jpa.api.svc;

import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class BatchIdChunk {

	final List<ResourcePersistentId> myIds;
	// FIXME KHS support single resource type
	final List<String> myResourceTypes;
	final Date myLastDate;

	public BatchIdChunk(List<ResourcePersistentId> theIds, List<String> theResourceTypes, Date theLastDate) {
		myIds = theIds;
		myResourceTypes = theResourceTypes;
		myLastDate = theLastDate;
	}

	public static BatchIdChunk fromChunksAndDate(List<BatchIdChunk> theChunks, Date theEnd) {
		List<ResourcePersistentId> ids = new ArrayList<>();
		List<String> types = new ArrayList<>();

		Date endDate = null;
		for (BatchIdChunk chunk : theChunks) {
			ids.addAll(chunk.myIds);
			types.addAll(chunk.myResourceTypes);
			endDate = getLatestDate(chunk, endDate, theEnd);
		}

		return new BatchIdChunk(ids, types, endDate);
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


	public Date getLastDate() {
		return myLastDate;
	}

	public int size() {
		return myIds.size();
	}

	// FIXME KHS remove
	public List<String> getResourceTypes() {
		return Collections.unmodifiableList(myResourceTypes);
	}

	@Nonnull
	public List<BatchResourceId> getBatchResourceIds() {
		List<BatchResourceId> retval = new ArrayList<>();
		for (int i = 0; i < myIds.size(); ++i) {
			retval.add(new BatchResourceId(myResourceTypes.get(i), myIds.get(i)));
		}
		return Collections.unmodifiableList(retval);
	}

	public boolean isEmpty() {
		return myIds.isEmpty();
	}
}
