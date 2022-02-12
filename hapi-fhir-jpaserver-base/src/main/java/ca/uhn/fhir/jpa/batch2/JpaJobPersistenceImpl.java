package ca.uhn.fhir.jpa.batch2;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceParameter;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.jpa.dao.data.IBatch2JobInstanceRepository;
import ca.uhn.fhir.jpa.dao.data.IBatch2WorkChunkRepository;
import ca.uhn.fhir.jpa.entity.Batch2JobInstanceEntity;
import ca.uhn.fhir.jpa.entity.Batch2WorkChunkEntity;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Transactional
public class JpaJobPersistenceImpl implements IJobPersistence {

	@Autowired
	private IBatch2JobInstanceRepository myJobInstanceRepository;
	@Autowired
	private IBatch2WorkChunkRepository myWorkChunkRepository;

	@Override
	public String storeWorkChunk(String theJobDefinitionId, int theJobDefinitionVersion, String theTargetStepId, String theInstanceId,Map<String, Object> theData) {
		Batch2WorkChunkEntity entity = new Batch2WorkChunkEntity();
		entity.setId(UUID.randomUUID().toString());
		entity.setJobDefinitionId(theJobDefinitionId);
		entity.setJobDefinitionVersion(theJobDefinitionVersion);
		entity.setTargetStepId(theTargetStepId);
		entity.setInstanceId(theInstanceId);
		entity.setSerializedData(JsonUtil.serialize(theData, false));
		entity.setStatus(StatusEnum.QUEUED);
		myWorkChunkRepository.save(entity);
		return entity.getId();
	}

	@Override
	public Optional<WorkChunk> fetchWorkChunkAndMarkInProgress(String theChunkId) {
		myWorkChunkRepository.updateChunkStatus(theChunkId, StatusEnum.IN_PROGRESS);
		Optional<Batch2WorkChunkEntity> chunk = myWorkChunkRepository.findById(theChunkId);
		return chunk.map(t -> toChunk(t));
	}

	@Override
	public void storeNewInstance(JobInstance theInstance) {
		Batch2JobInstanceEntity entity = new Batch2JobInstanceEntity();
		entity.setId(theInstance.getInstanceId());
		entity.setDefinitionId(theInstance.getJobDefinitionId());
		entity.setDefinitionVersion(theInstance.getJobDefinitionVersion());
		entity.setStatus(theInstance.getStatus());
		entity.setParams(JsonUtil.serialize(theInstance.getParameters(), false));

		myJobInstanceRepository.save(entity);
	}

	@Override
	public Optional<JobInstance> fetchInstance(String theInstanceId) {
		return myJobInstanceRepository.findById(theInstanceId).map(t -> toInstance(t));
	}

	@Override
	public Optional<JobInstance> fetchInstanceAndMarkInProgress(String theInstanceId) {
		myJobInstanceRepository.updateInstanceStatus(theInstanceId, StatusEnum.IN_PROGRESS);
		return fetchInstance(theInstanceId);
	}

	private WorkChunk toChunk(Batch2WorkChunkEntity theEntity) {
		WorkChunk retVal = new WorkChunk();
		retVal.setJobDefinitionId(theEntity.getJobDefinitionId());
		retVal.setJobDefinitionVersion(theEntity.getJobDefinitionVersion());
		retVal.setInstanceId(theEntity.getInstanceId());
		retVal.setTargetStepId(theEntity.getTargetStepId());
		retVal.setStatus(theEntity.getStatus());
		retVal.setData(JsonUtil.deserialize(theEntity.getSerializedData(), Map.class));
		return retVal;
	}

	private JobInstance toInstance(Batch2JobInstanceEntity theEntity) {
		JobInstance retVal = new JobInstance();
		retVal.setInstanceId(theEntity.getId());
		retVal.setJobDefinitionId(theEntity.getDefinitionId());
		retVal.setJobDefinitionVersion(theEntity.getDefinitionVersion());
		retVal.setStatus(theEntity.getStatus());

		String params = theEntity.getParams();
		try {
			List<JobInstanceParameter> paramsList = JsonUtil.deserializeList(params, JobInstanceParameter.class);
			retVal.getParameters().addAll(paramsList);
		} catch (IOException e) {
			throw new InternalErrorException(e);
		}
		return retVal;
	}

	@Override
	public void markWorkChunkAsErrored(String theChunkId, String theErrorMessage) {

	}

	@Override
	public void markWorkChunkAsCompleted(String theChunkId) {

	}
}
