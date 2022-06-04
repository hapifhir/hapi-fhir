package ca.uhn.fhir.jpa.api.svc;

import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

abstract public class BaseBatchIdChunk implements IBatchIdChunk {
	private static final IBatchIdChunk EMPTY_CHUNK = new EmptyBatchIdChunk();

	final List<ResourcePersistentId> myIds;

	@Nullable
	final Date myLastDate;

	BaseBatchIdChunk(List<ResourcePersistentId> theIds, Date theLastDate) {
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
	public List<BatchResourceId> getBatchResourceIds() {
		List<BatchResourceId> retval = new ArrayList<>();
		for (int i = 0; i < myIds.size(); ++i) {
			retval.add(new BatchResourceId(getResourceType(i), myIds.get(i)));
		}
		return Collections.unmodifiableList(retval);
	}

	public boolean isEmpty() {
		return myIds.isEmpty();
	}

	@Override
	public List<ResourcePersistentId> getIds() {
		return Collections.unmodifiableList(myIds);
	}

	// FIXME KHS test
	public static IBatchIdChunk fromChunksAndDate(List<IBatchIdChunk> theChunks, Date theEnd) {
		if (theChunks.isEmpty()) {
			return BaseBatchIdChunk.empty();
		}

		List<ResourcePersistentId> ids = new ArrayList<>();

		Date endDate = null;
		boolean containsMixed = false;
		for (IBatchIdChunk chunk : theChunks) {
			ids.addAll(chunk.getIds());
			endDate = getLatestDate(chunk, endDate, theEnd);
			if (chunk instanceof MixedBatchIdChunk) {
				containsMixed = true;
			}
		}

		if (containsMixed) {
			List<String> types = new ArrayList<>();
			for (IBatchIdChunk chunk : theChunks) {
				for (int i = 0; i < chunk.size(); ++i) {
					types.add(chunk.getResourceType(i));
				}
			}
			return new MixedBatchIdChunk(ids, types, endDate);
		} else {
			IBatchIdChunk firstChunk = theChunks.get(0);
			String onlyResourceType = firstChunk.getResourceType(0);
			return new HomogeneousBatchIdChunk(ids, onlyResourceType, endDate);
		}
	}

	private static IBatchIdChunk empty() {
		return EMPTY_CHUNK;
	}

	private static Date getLatestDate(IBatchIdChunk theChunk, Date theCurrentEndDate, Date thePassedInEndDate) {
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

