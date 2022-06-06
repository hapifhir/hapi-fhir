package ca.uhn.fhir.batch2.coordinator;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobWorkCursor;
import ca.uhn.fhir.batch2.model.WorkChunkData;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class ReductionStepDataSink<PT extends IModelJson, IT extends IModelJson, OT extends IModelJson>
	extends BaseDataSink<PT, IT, OT> {
	private static final Logger ourLog = LoggerFactory.getLogger(ReductionStepDataSink.class);

	private final IJobPersistence myJobPersistence;
	private final AtomicInteger myCounter = new AtomicInteger();

	protected ReductionStepDataSink(String theInstanceId,
											  JobWorkCursor<PT, IT, OT> theJobWorkCursor,
											  JobDefinition<PT> theDefinition,
											  IJobPersistence thePersistence) {
		super(theInstanceId, theJobWorkCursor, theDefinition.getJobDefinitionId());
		myJobPersistence = thePersistence;
	}

	@Override
	public void accept(WorkChunkData<OT> theData) {
		int count = myCounter.getAndIncrement();
		if (count == 0) {
			String instanceId = getInstanceId();
			Optional<JobInstance> instanceOp = myJobPersistence.fetchInstance(instanceId);
			if (instanceOp.isPresent()) {
				JobInstance instance = instanceOp.get();
				OT data = theData.getData();
				String dataString = JsonUtil.serialize(data, false);
				instance.setRecord(dataString);
				ourLog.debug(JsonUtil.serialize(instance));
				myJobPersistence.updateInstance(instance);
			} else {
				String msg = "No instance found with Id " + instanceId;
				ourLog.error(msg);
				// TODO - the code could be wrong - depending on when PR merges
				throw new JobExecutionFailedException(Msg.code(2084) + msg);
			}
		} else {
			// first in win - we won't throw
			ourLog.error(
				"Expected a single data sink for reduction step, but received {} instead. Only the first is accepted!",
				count);
		}
	}

	@Override
	public int getWorkChunkCount() {
		return 0;
	}
}
