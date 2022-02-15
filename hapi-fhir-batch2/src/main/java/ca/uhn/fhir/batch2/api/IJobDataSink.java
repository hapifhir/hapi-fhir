package ca.uhn.fhir.batch2.api;

import ca.uhn.fhir.batch2.model.WorkChunkData;

public interface IJobDataSink {

	void accept(WorkChunkData theData);

	int getWorkChunkCount();

}
