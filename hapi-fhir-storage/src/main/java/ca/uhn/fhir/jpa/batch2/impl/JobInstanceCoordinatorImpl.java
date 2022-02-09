package ca.uhn.fhir.jpa.batch2.impl;

import ca.uhn.fhir.jpa.batch2.api.IBatchJobDefinition;
import ca.uhn.fhir.jpa.batch2.api.IJobDefinitionRegistry;
import ca.uhn.fhir.jpa.batch2.api.IJobInstanceCoordinator;
import ca.uhn.fhir.jpa.batch2.api.IJobInstancePersister;
import ca.uhn.fhir.jpa.batch2.model.JobDefinitionParameter;
import ca.uhn.fhir.jpa.batch2.model.JobInstanceParameter;
import ca.uhn.fhir.jpa.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelProducer;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelReceiver;

import javax.annotation.Nonnull;
import java.util.List;

public class JobInstanceCoordinatorImpl implements IJobInstanceCoordinator {

	private final IChannelProducer myWorkChannelProducer;
	private final IChannelReceiver myWorkChannelReceiver;
	private final IJobInstancePersister myJobInstancePersister;
	private final IJobDefinitionRegistry myJobDefinitionRegistry;

	/**
	 * Constructor
	 */
	public JobInstanceCoordinatorImpl(
		@Nonnull IChannelProducer theWorkChannelProducer,
		@Nonnull IChannelReceiver theWorkChannelReceiver,
		@Nonnull IJobInstancePersister theJobInstancePersister,
		@Nonnull IJobDefinitionRegistry theJobDefinitionRegistry) {
		myWorkChannelProducer = theWorkChannelProducer;
		myWorkChannelReceiver = theWorkChannelReceiver;
		myJobInstancePersister = theJobInstancePersister;
		myJobDefinitionRegistry = theJobDefinitionRegistry;
	}

	@Override
	public void startJob(JobInstanceStartRequest theStartRequest) {

		IBatchJobDefinition jobDefinition = myJobDefinitionRegistry
			.getLatestJobDefinition(theStartRequest.getJobDefinitionId())
			.orElseThrow(() -> new IllegalArgumentException("Unknown job definition ID: " + theStartRequest.getJobDefinitionId()));

		validateParameters(jobDefinition.getParameters(), theStartRequest.getParameters());

	}

	static void validateParameters(List<JobDefinitionParameter> theDefinitionParameters, List<JobInstanceParameter> theInstanceParameters) {

		for (JobDefinitionParameter nextDefinition : theDefinitionParameters) {
			
		}

	}


}
