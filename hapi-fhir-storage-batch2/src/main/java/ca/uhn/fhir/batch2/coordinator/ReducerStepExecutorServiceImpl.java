package ca.uhn.fhir.batch2.coordinator;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.IReducerStepExecutorService;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.model.JobDefinitionStep;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobWorkCursor;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.IHasScheduledJobs;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.model.api.IModelJson;
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class ReducerStepExecutorServiceImpl implements IReducerStepExecutorService, IHasScheduledJobs {
	public static final String SCHEDULED_JOB_ID = ReducerStepExecutorScheduledJob.class.getName();
	private static final Logger ourLog = LoggerFactory.getLogger(ReducerStepExecutorServiceImpl.class);
	private final Map<String, JobWorkCursor> myInstanceIdToJobWorkCursor = Collections.synchronizedMap(new LinkedHashMap<>());
	private final ExecutorService myReducerExecutor;
	private final IJobPersistence myJobPersistence;
	private final IHapiTransactionService myTransactionService;
	private final Semaphore myCurrentlyExecuting = new Semaphore(1);


	/**
	 * Constructor
	 */
	public ReducerStepExecutorServiceImpl(IJobPersistence theJobPersistence, IHapiTransactionService theTransactionService) {
		myJobPersistence = theJobPersistence;
		myTransactionService = theTransactionService;

		myReducerExecutor = Executors.newSingleThreadExecutor(new CustomizableThreadFactory("batch2-reducer"));
	}

	@Override
	public void triggerReductionStep(String theInstanceId, JobWorkCursor<?, ?, ?> theJobWorkCursor) {
		myInstanceIdToJobWorkCursor.putIfAbsent(theInstanceId, theJobWorkCursor);
		if (myCurrentlyExecuting.availablePermits() > 0) {
			myReducerExecutor.submit(() -> reducerPass());
		}
	}

	@Override
	public void reducerPass() {
		if (myCurrentlyExecuting.tryAcquire()) {
			try {

				String[] instanceIds = myInstanceIdToJobWorkCursor.keySet().toArray(new String[0]);
				if (instanceIds.length > 0) {
					String instanceId = instanceIds[0];
					JobWorkCursor<?, ?, ?> jobWorkCursor = myInstanceIdToJobWorkCursor.get(instanceId);
					runReducerStep(instanceId, jobWorkCursor);
				}

			} finally {
				myCurrentlyExecuting.release();
			}
		}
	}

	private <PT extends IModelJson, IT extends IModelJson, OT extends IModelJson> void runReducerStep(String theInstanceId, JobWorkCursor<PT, IT, OT> theJobWorkCursor) {

		Optional<JobInstance> instanceOpt = myJobPersistence.fetchInstance(theInstanceId);
		if (instanceOpt.isEmpty()) {
			return;
		}
		JobInstance instance = instanceOpt.get();

		JobDefinitionStep<PT, IT, OT> step = theJobWorkCursor.getCurrentStep();
		PT parameters = instance.getParameters(theJobWorkCursor.getJobDefinition().getParametersType());
		StepExecutionDetails<PT, IT> details = new StepExecutionDetails<>(parameters, null, instance, "REDUCER");
		IJobDataSink<OT> sink = new ReductionStepDataSink<>(theInstanceId, theJobWorkCursor, myJobPersistence);
		IJobStepWorker<PT, IT, OT> jobStepWorker = step.getJobStepWorker();

		try {
			jobStepWorker.run(details, sink);
		} catch (Throwable e) {
			ourLog.error("Failure running reducer", e);
		}
	}


	@Override
	public void scheduleJobs(ISchedulerService theSchedulerService) {
		theSchedulerService.scheduleClusteredJob(10 * DateUtils.MILLIS_PER_SECOND, buildJobDefinition());
	}

	@Nonnull
	private ScheduledJobDefinition buildJobDefinition() {
		ScheduledJobDefinition jobDefinition = new ScheduledJobDefinition();
		jobDefinition.setId(SCHEDULED_JOB_ID);
		jobDefinition.setJobClass(ReducerStepExecutorScheduledJob.class);
		return jobDefinition;
	}


	public static class ReducerStepExecutorScheduledJob implements HapiJob {
		@Autowired
		private IReducerStepExecutorService myTarget;

		@Override
		public void execute(JobExecutionContext theContext) {
			myTarget.reducerPass();
		}
	}


}
