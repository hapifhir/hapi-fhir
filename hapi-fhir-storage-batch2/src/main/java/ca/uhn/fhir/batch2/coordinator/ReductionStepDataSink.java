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

public class ReductionStepDataSink<PT extends IModelJson, IT extends IModelJson, OT extends IModelJson>
	extends BaseDataSink<PT, IT, OT> {
	private static final Logger ourLog = LoggerFactory.getLogger(ReductionStepDataSink.class);

	private final IJobPersistence myJobPersistence;

	protected ReductionStepDataSink(String theInstanceId,
											  JobWorkCursor<PT, IT, OT> theJobWorkCursor,
											  JobDefinition<PT> theDefinition,
											  IJobPersistence thePersistence) {
		super(theInstanceId, theJobWorkCursor);
		myJobPersistence = thePersistence;
	}

	@Override
	public void accept(WorkChunkData<OT> theData) {
		String instanceId = getInstanceId();
		Optional<JobInstance> instanceOp = myJobPersistence.fetchInstance(instanceId);
		if (instanceOp.isPresent()) {
			JobInstance instance = instanceOp.get();

			if (instance.getReport() != null) {
				// last in wins - so we won't throw
				ourLog.error(
					"Report has already been set. Now it is being overwritten. Last in will win!");
			}

			OT data = theData.getData();
			String dataString = JsonUtil.serialize(data, false);
			instance.setReport(dataString);
			ourLog.debug(JsonUtil.serialize(instance));
			myJobPersistence.updateInstance(instance);
		} else {
			String msg = "No instance found with Id " + instanceId;
			ourLog.error(msg);

			throw new JobExecutionFailedException(Msg.code(2097) + msg);
		}
	}

	@Override
	public int getWorkChunkCount() {
		return 0;
	}
}
