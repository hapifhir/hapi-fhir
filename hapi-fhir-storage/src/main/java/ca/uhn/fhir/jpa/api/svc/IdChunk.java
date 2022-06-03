package ca.uhn.fhir.jpa.api.svc;

import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class IdChunk {

	final List<ResourcePersistentId> myIds;
	// FIXME KHS support single resource type
	final List<String> myResourceTypes;
	final Date myLastDate;

	public IdChunk(List<ResourcePersistentId> theIds, List<String> theResourceTypes, Date theLastDate) {
		myIds = theIds;
		myResourceTypes = theResourceTypes;
		myLastDate = theLastDate;
	}

	public static IdChunk fromChunksAndDate(List<IdChunk> theChunks, Date theEnd) {
		List<ResourcePersistentId> ids = new ArrayList<>();
		List<String> types = new ArrayList<>();

		Date endDate = null;
		for (IdChunk chunk : theChunks) {
			ids.addAll(chunk.getIds());
			types.addAll(chunk.myResourceTypes);
			endDate = getLatestDate(chunk, endDate, theEnd);
		}

		return new IdChunk(ids, types, endDate);
	}

	private static Date getLatestDate(IdChunk theChunk, Date theCurrentEndDate, Date thePassedInEndDate) {
		Date endDate = theCurrentEndDate;
		if (theCurrentEndDate == null) {
			endDate = theChunk.getLastDate();
		} else if (theChunk.getLastDate().after(endDate)
			&& theChunk.getLastDate().before(thePassedInEndDate)) {
			endDate = theChunk.getLastDate();
		}
		return endDate;
	}

	public String getResourceType(int index) {
		return myResourceTypes.get(index);
	}

	public List<ResourcePersistentId> getIds() {
		return Collections.unmodifiableList(myIds);
	}

	public Date getLastDate() {
		return myLastDate;
	}

	public int size() {
		return myIds.size();
	}

	public List<String> getResourceTypes() {
		return Collections.unmodifiableList(myResourceTypes);
	}
}
