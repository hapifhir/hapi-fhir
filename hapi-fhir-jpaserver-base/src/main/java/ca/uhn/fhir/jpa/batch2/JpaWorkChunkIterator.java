package ca.uhn.fhir.jpa.batch2;

import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.jpa.entity.Batch2WorkChunkEntity;
import ca.uhn.fhir.jpa.util.JobInstanceUtil;

import java.util.Iterator;

public class JpaWorkChunkIterator implements Iterator<WorkChunk> {

	private final Iterator<Batch2WorkChunkEntity> myWorkChunkEntities;

	public JpaWorkChunkIterator(Iterator<Batch2WorkChunkEntity> theWorkChunkEntities) {
		myWorkChunkEntities = theWorkChunkEntities;
	}

	@Override
	public boolean hasNext() {
		return myWorkChunkEntities.hasNext();
	}

	@Override
	public WorkChunk next() {
		Batch2WorkChunkEntity next = myWorkChunkEntities.next();
		return JobInstanceUtil.fromEntityToWorkChunk(next, true);
	}
}
