package ca.uhn.fhir.batch2.impl;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobDefinitionParameter;
import ca.uhn.fhir.batch2.model.JobDefinitionStep;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceParameter;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.JobWorkNotification;
import ca.uhn.fhir.batch2.model.JobWorkNotificationJsonMessage;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelProducer;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelReceiver;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class JobCoordinatorImpl extends BaseJobService implements IJobCoordinator {

	private static final Logger ourLog = LoggerFactory.getLogger(JobCoordinatorImpl.class);
	private final IChannelProducer myWorkChannelProducer;
	private final IChannelReceiver myWorkChannelReceiver;
	private final JobDefinitionRegistry myJobDefinitionRegistry;
	private final MessageHandler myReceiverHandler = new WorkChannelMessageHandler();

	/**
	 * Constructor
	 */
	public JobCoordinatorImpl(
		@Nonnull IChannelProducer theWorkChannelProducer,
		@Nonnull IChannelReceiver theWorkChannelReceiver,
		@Nonnull IJobPersistence theJobPersistence,
		@Nonnull JobDefinitionRegistry theJobDefinitionRegistry) {
		super(theJobPersistence);
		myWorkChannelProducer = theWorkChannelProducer;
		myWorkChannelReceiver = theWorkChannelReceiver;
		myJobDefinitionRegistry = theJobDefinitionRegistry;
	}

	@Override
	public String startJob(JobInstanceStartRequest theStartRequest) {
		JobDefinition jobDefinition = myJobDefinitionRegistry
			.getLatestJobDefinition(theStartRequest.getJobDefinitionId())
			.orElseThrow(() -> new IllegalArgumentException("Unknown job definition ID: " + theStartRequest.getJobDefinitionId()));

		String firstStepId = jobDefinition.getSteps().get(0).getStepId();

		validateParameters(jobDefinition.getParameters(), theStartRequest.getParameters());

		String jobDefinitionId = jobDefinition.getJobDefinitionId();
		int jobDefinitionVersion = jobDefinition.getJobDefinitionVersion();

		JobInstance instance = new JobInstance();
		instance.setJobDefinitionId(jobDefinitionId);
		instance.setJobDefinitionVersion(jobDefinitionVersion);
		instance.setStatus(StatusEnum.QUEUED);
		instance.getParameters().addAll(theStartRequest.getParameters());

		String instanceId = myJobPersistence.storeNewInstance(instance);
		String chunkId = myJobPersistence.storeWorkChunk(jobDefinitionId, jobDefinitionVersion, firstStepId, instanceId, 0, null);

		sendWorkChannelMessage(jobDefinitionId, jobDefinitionVersion, instanceId, firstStepId, chunkId);

		return instanceId;
	}

	@Override
	public JobInstance getInstance(String theInstanceId) {
		return myJobPersistence
			.fetchInstance(theInstanceId)
			.orElseThrow(()->new ResourceNotFoundException("Unknown instance ID: " + UrlUtil.escapeUrlParam(theInstanceId)));
	}

	private void executeStep(@Nonnull WorkChunk theWorkChunk, String jobDefinitionId, String targetStepId, ListMultimap<String, JobInstanceParameter> parameters, IJobStepWorker worker, IJobDataSink dataSink) {
		Map<String, Object> data = theWorkChunk.getData();

		StepExecutionDetails stepExecutionDetails = new StepExecutionDetails(parameters, data);
		IJobStepWorker.RunOutcome outcome;
		try {
			outcome = worker.run(stepExecutionDetails, dataSink);
			Validate.notNull(outcome, "Step worker returned null: %s", worker.getClass());
		} catch (JobExecutionFailedException e) {
			ourLog.error("Unrecoverable failure executing job {} step {}", jobDefinitionId, targetStepId, e);
			myJobPersistence.markWorkChunkAsFailed(theWorkChunk.getId(), e.toString());
			return;
		} catch (Exception e) {
			ourLog.error("Failure executing job {} step {}", jobDefinitionId, targetStepId, e);
			myJobPersistence.markWorkChunkAsErroredAndIncrementErrorCount(theWorkChunk.getId(), e.toString());
			throw new InternalErrorException(e);
		}

		int recordsProcessed = outcome.getRecordsProcessed();
		myJobPersistence.markWorkChunkAsCompletedAndClearData(theWorkChunk.getId(), recordsProcessed);

	}

	@PostConstruct
	public void start() {
		myWorkChannelReceiver.subscribe(myReceiverHandler);
	}

	@PreDestroy
	public void stop() {
		myWorkChannelReceiver.unsubscribe(myReceiverHandler);
	}


	private void sendWorkChannelMessage(String theJobDefinitionId, int jobDefinitionVersion, String theInstanceId, String theTargetStepId, String theChunkId) {
		JobWorkNotificationJsonMessage message = new JobWorkNotificationJsonMessage();
		JobWorkNotification workNotification = new JobWorkNotification();
		workNotification.setJobDefinitionId(theJobDefinitionId);
		workNotification.setJobDefinitionVersion(jobDefinitionVersion);
		workNotification.setChunkId(theChunkId);
		workNotification.setInstanceId(theInstanceId);
		workNotification.setTargetStepId(theTargetStepId);
		message.setPayload(workNotification);

		ourLog.info("Sending work notification for job[{}] instance[{}] step[{}] chunk[{}]", theJobDefinitionId, theInstanceId, theTargetStepId, theChunkId);
		myWorkChannelProducer.send(message);
	}

	private void handleWorkChannelMessage(JobWorkNotificationJsonMessage theMessage) {
		JobWorkNotification payload = theMessage.getPayload();

		String chunkId = payload.getChunkId();
		Validate.notNull(chunkId);
		Optional<WorkChunk> chunkOpt = myJobPersistence.fetchWorkChunkSetStartTimeAndMarkInProgress(chunkId);
		if (!chunkOpt.isPresent()) {
			ourLog.error("Unable to find chunk with ID {} - Aborting", chunkId);
			return;
		}
		WorkChunk chunk = chunkOpt.get();

		String jobDefinitionId = payload.getJobDefinitionId();
		int jobDefinitionVersion = payload.getJobDefinitionVersion();
		Optional<JobDefinition> opt = myJobDefinitionRegistry.getJobDefinition(jobDefinitionId, jobDefinitionVersion);
		if (!opt.isPresent()) {
			String msg = "Unknown job definition ID[" + jobDefinitionId + "] version[" + jobDefinitionVersion + "]";
			ourLog.warn(msg);
			throw new InternalErrorException(msg);
		}
		JobDefinition definition = opt.get();

		JobDefinitionStep targetStep = null;
		JobDefinitionStep nextStep = null;
		String targetStepId = payload.getTargetStepId();
		boolean firstStep = false;
		for (int i = 0; i < definition.getSteps().size(); i++) {
			JobDefinitionStep step = definition.getSteps().get(i);
			if (step.getStepId().equals(targetStepId)) {
				targetStep = step;
				if (i == 0) {
					firstStep = true;
				}
				if (i < (definition.getSteps().size() - 1)) {
					nextStep = definition.getSteps().get(i + 1);
				}
				break;
			}
		}

		if (targetStep == null) {
			String msg = "Unknown step[" + targetStepId + "] for job definition ID[" + jobDefinitionId + "] version[" + jobDefinitionVersion + "]";
			ourLog.warn(msg);
			throw new InternalErrorException(msg);
		}

		Validate.isTrue(chunk.getTargetStepId().equals(targetStep.getStepId()), "Chunk %s has target step %s but expected %s", chunkId, chunk.getTargetStepId(), targetStep.getStepId());

		Optional<JobInstance> instanceOpt = myJobPersistence.fetchInstanceAndMarkInProgress(payload.getInstanceId());
		JobInstance instance = instanceOpt.orElseThrow(() -> new InternalErrorException("Unknown instance: " + payload.getInstanceId()));
		String instanceId = instance.getInstanceId();

		ListMultimap<String, JobInstanceParameter> parameters = validateParameters(definition.getParameters(), instance.getParameters());
		IJobStepWorker worker = targetStep.getJobStepWorker();

		IJobDataSink dataSink;
		if (nextStep != null) {
			dataSink = new JobDataSink(jobDefinitionId, jobDefinitionVersion, nextStep, instanceId);
		} else {
			dataSink = new FinalStepDataSink(jobDefinitionId);
		}

		executeStep(chunk, jobDefinitionId, targetStepId, parameters, worker, dataSink);

		int workChunkCount = dataSink.getWorkChunkCount();
		if (firstStep && workChunkCount == 0) {
			ourLog.info("First step of job instance {} produced no work chunks, marking as completed", instanceId);
			myJobPersistence.markInstanceAsCompleted(instanceId);
		}
	}

	static ListMultimap<String, JobInstanceParameter> validateParameters(List<JobDefinitionParameter> theDefinitionParameters, List<JobInstanceParameter> theInstanceParameters) {
		ListMultimap<String, JobInstanceParameter> retVal = ArrayListMultimap.create();
		Set<String> paramNames = new HashSet<>();
		for (JobDefinitionParameter nextDefinition : theDefinitionParameters) {
			paramNames.add(nextDefinition.getName());

			List<JobInstanceParameter> instances = theInstanceParameters
				.stream()
				.filter(t -> nextDefinition.getName().equals(t.getName()))
				.filter(t -> isNotBlank(t.getValue()))
				.collect(Collectors.toList());

			if (nextDefinition.isRequired() && instances.size() < 1) {
				throw new InvalidRequestException("Missing required parameter: " + nextDefinition.getName());
			}

			if (!nextDefinition.isRepeating() && instances.size() > 1) {
				throw new InvalidRequestException("Illegal repeating parameter: " + nextDefinition.getName());
			}

			retVal.putAll(nextDefinition.getName(), instances);
		}

		for (JobInstanceParameter next : theInstanceParameters) {
			if (!paramNames.contains(next.getName())) {
				throw new InvalidRequestException("Unexpected parameter: " + next.getName());
			}
		}

		return retVal;
	}

	private class WorkChannelMessageHandler implements MessageHandler {
		@Override
		public void handleMessage(Message<?> theMessage) throws MessagingException {
			handleWorkChannelMessage((JobWorkNotificationJsonMessage) theMessage);
		}
	}

	private class JobDataSink implements IJobDataSink {
		private final String myJobDefinitionId;
		private final int myJobDefinitionVersion;
		private final JobDefinitionStep mySecondStep;
		private final String myInstanceId;
		private final AtomicInteger myChunkCounter = new AtomicInteger(0);

		public JobDataSink(String theJobDefinitionId, int theJobDefinitionVersion, JobDefinitionStep theSecondStep, String theInstanceId) {
			myJobDefinitionId = theJobDefinitionId;
			myJobDefinitionVersion = theJobDefinitionVersion;
			mySecondStep = theSecondStep;
			myInstanceId = theInstanceId;
		}

		@Override
		public void accept(Map<String, Object> theData) {
			String jobDefinitionId = myJobDefinitionId;
			int jobDefinitionVersion = myJobDefinitionVersion;
			String instanceId = myInstanceId;
			String targetStepId = mySecondStep.getStepId();
			int sequence = myChunkCounter.getAndIncrement();
			String chunkId = myJobPersistence.storeWorkChunk(jobDefinitionId, jobDefinitionVersion, targetStepId, instanceId, sequence, theData);

			sendWorkChannelMessage(jobDefinitionId, jobDefinitionVersion, instanceId, targetStepId, chunkId);
		}

		@Override
		public int getWorkChunkCount() {
			return myChunkCounter.get();
		}

	}

	private static class FinalStepDataSink implements IJobDataSink {
		private final String myJobDefinitionId;

		/**
		 * Constructor
		 */
		private FinalStepDataSink(String theJobDefinitionId) {
			myJobDefinitionId = theJobDefinitionId;
		}

		@Override
		public void accept(Map<String, Object> theData) {
			String msg = "Illegal attempt to store data during final step of job " + myJobDefinitionId;
			ourLog.error(msg);
			throw new JobExecutionFailedException(msg);
		}

		@Override
		public int getWorkChunkCount() {
			return 0;
		}
	}
}
