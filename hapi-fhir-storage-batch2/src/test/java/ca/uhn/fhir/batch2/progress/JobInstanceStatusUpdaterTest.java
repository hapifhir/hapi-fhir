package ca.uhn.fhir.batch2.progress;

import ca.uhn.fhir.batch2.api.IJobCompletionHandler;
import ca.uhn.fhir.batch2.api.IJobInstance;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.JobCompletionDetails;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobInstanceStatusUpdaterTest {
	private static final String TEST_INSTANCE_ID = "test-instance-id";
	private static final String TEST_NAME = "test name";
	private static final String TEST_ERROR_MESSAGE = "test error message";
	private static final int TEST_ERROR_COUNT = 729;
	@Mock
	IJobPersistence myJobPersistence;
	@Mock
	private JobDefinition<TestParameters> myJobDefinition;

	@InjectMocks
	JobInstanceStatusUpdater mySvc;
	private JobInstance myInstance;
	private TestParameters myTestParameters;
	private AtomicReference<JobCompletionDetails> myDetails;

	@BeforeEach
	public void before() {
		myInstance = JobInstance.fromInstanceId(TEST_INSTANCE_ID);
		myInstance.setStatus(StatusEnum.IN_PROGRESS);
		myInstance.setJobDefinition(myJobDefinition);
		myTestParameters = new TestParameters();
		myTestParameters.name = TEST_NAME;
		myInstance.setParameters(myTestParameters);
		myInstance.setErrorMessage(TEST_ERROR_MESSAGE);
		myInstance.setErrorCount(TEST_ERROR_COUNT);

		when(myJobDefinition.getParametersType()).thenReturn(TestParameters.class);
	}

	@Test
	public void testCompletionHandler() {
		AtomicReference<JobCompletionDetails> calledDetails = new AtomicReference<>();

		// setup
		when(myJobPersistence.updateInstance(myInstance)).thenReturn(true);
		IJobCompletionHandler<TestParameters> completionHandler = details -> calledDetails.set(details);
		when(myJobDefinition.getCompletionHandler()).thenReturn(completionHandler);

		// execute
		mySvc.updateInstanceStatus(myInstance, StatusEnum.COMPLETED);

		JobCompletionDetails<TestParameters> receivedDetails = calledDetails.get();
		assertEquals(TEST_INSTANCE_ID, receivedDetails.getInstance().getInstanceId());
		assertEquals(TEST_NAME, receivedDetails.getParameters().name);
	}

	@Test
	public void testErrorHandler_ERROR() {
		setupErrorCallback();

		// execute
		mySvc.updateInstanceStatus(myInstance, StatusEnum.ERRORED);

		assertErrorCallbackCalled(StatusEnum.ERRORED);
	}

	@Test
	public void testErrorHandler_FAILED() {
		setupErrorCallback();

		// execute
		mySvc.updateInstanceStatus(myInstance, StatusEnum.FAILED);

		assertErrorCallbackCalled(StatusEnum.FAILED);
	}

	@Test
	public void testErrorHandler_CANCELLED() {
		setupErrorCallback();

		// execute
		mySvc.updateInstanceStatus(myInstance, StatusEnum.CANCELLED);

		assertErrorCallbackCalled(StatusEnum.CANCELLED);
	}

	private void assertErrorCallbackCalled(StatusEnum expectedStatus) {
		JobCompletionDetails<TestParameters> receivedDetails = myDetails.get();
		assertEquals(TEST_NAME, receivedDetails.getParameters().name);
		IJobInstance instance = receivedDetails.getInstance();
		assertEquals(TEST_INSTANCE_ID, instance.getInstanceId());
		assertEquals(TEST_ERROR_MESSAGE, instance.getErrorMessage());
		assertEquals(TEST_ERROR_COUNT, instance.getErrorCount());
		assertEquals(expectedStatus, instance.getStatus());
	}

	private void setupErrorCallback() {
		myDetails = new AtomicReference<>();

		// setup
		when(myJobPersistence.updateInstance(myInstance)).thenReturn(true);
		IJobCompletionHandler<TestParameters> errorHandler = details -> myDetails.set(details);
		when(myJobDefinition.getErrorHandler()).thenReturn(errorHandler);
	}


	static class TestParameters implements IModelJson {
		@JsonProperty
		public String name;
	}
}
