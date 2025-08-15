package ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.api.ResourceModificationRequest;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.api.ResourceModificationResponse;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common.BulkModifyResourcesChunkOutcomeJson;
import ca.uhn.fhir.batch2.jobs.chunk.TypedPidAndVersionListWorkChunkJson;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoPatient;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.support.SimpleTransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.function.Function;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unused")
@ExtendWith(MockitoExtension.class)
class BaseBulkModifyResourcesStepTest {

	@Mock
	private Function<ResourceModificationRequest, ResourceModificationResponse> myFunction;
	@Spy
	private IHapiTransactionService myTransactionService = new MyMockTxService();
	@Mock
	private DaoRegistry myDaoRegistry;
	@SuppressWarnings("rawtypes")
	@Mock
	private IFhirSystemDao mySystemDao;
	@Mock
	private IIdHelperService<IResourcePersistentId<?>> myIdHelperService;
	@Mock
	private IJobDataSink<BulkModifyResourcesChunkOutcomeJson> mySink;
	@Spy
	private FhirContext myFhirContext = FhirContext.forR4Cached();
	@InjectMocks
	private MySvc mySvc = new MySvc();
	@Mock
	private IFhirResourceDaoPatient<Patient> myResourceDao;
	@Captor
	private ArgumentCaptor<BulkModifyResourcesChunkOutcomeJson> myDataCaptor;

	@ParameterizedTest
	@CsvSource(delimiter = '|', textBlock = """
		{"resourceType":"Patient","id":"ZZZ","meta":{"versionId":"123"}} | HAPI-2783: Modification for Resource[Patient/ABC] attempted to change the resource ID
		{"resourceType":"Patient","id":"ABC","meta":{"versionId":"111"}} | HAPI-2784: Modification for Resource[Patient/ABC] attempted to change the resource version
		{"resourceType":"Basic","id":"ABC","meta":{"versionId":"123"}}   | HAPI-2782: Modification for Resource[Patient/ABC/_history/123] returned wrong resource type, expected Patient but was Basic
		                                                                 | HAPI-2782: Null response from Modification for Resource[Patient/ABC/_history/123]
		""")
	public void testModificationChangingResourceIdBlocked(String theOutputResource, String theExpectedMessage) {
		// Setup
		MyParameters params = new MyParameters();
		JobInstance instance = new JobInstance();
		TypedPidAndVersionListWorkChunkJson data = new TypedPidAndVersionListWorkChunkJson();
		data.addTypedPidWithNullPartitionForUnitTest("Patient", 1L, null);

		Patient inputPatient = new Patient();
		inputPatient.setId("Patient/ABC/_history/123");
		inputPatient.setActive(true);

		when(myDaoRegistry.getResourceDao(eq("Patient"))).thenReturn(myResourceDao);
		when(myResourceDao.readByPid(any())).thenReturn(inputPatient);

		if (isNotBlank(theOutputResource)) {
			IBaseResource outputPatient = myFhirContext.newJsonParser().parseResource(theOutputResource);
			ResourceModificationResponse response = ResourceModificationResponse.updateResource(outputPatient);
			when(myFunction.apply(any())).thenReturn(response);
		} else {
			when(myFunction.apply(any())).thenReturn(null);
		}

		// Test
		assertThatThrownBy(() -> mySvc.run(new StepExecutionDetails<>(params, data, instance, new WorkChunk()), mySink))
			.isInstanceOf(JobExecutionFailedException.class)
			.hasMessage(theExpectedMessage);

	}

	@Test
	public void testNoopResponse() {
		// Setup
		MyParameters params = new MyParameters();
		JobInstance instance = new JobInstance();
		TypedPidAndVersionListWorkChunkJson data = new TypedPidAndVersionListWorkChunkJson();
		data.addTypedPidWithNullPartitionForUnitTest("Patient", 1L, null);

		Patient inputPatient = new Patient();
		inputPatient.setId("Patient/ABC/_history/123");
		inputPatient.setActive(true);

		when(myDaoRegistry.getResourceDao(eq("Patient"))).thenReturn(myResourceDao);
		when(myResourceDao.readByPid(any())).thenReturn(inputPatient);

		when(myFunction.apply(any())).thenReturn(ResourceModificationResponse.noChange());

		// Test
		mySvc.run(new StepExecutionDetails<>(params, data, instance, new WorkChunk()), mySink);

		// Verify
		verify(mySink, times(1)).accept(myDataCaptor.capture());
		BulkModifyResourcesChunkOutcomeJson outputData = myDataCaptor.getValue();
		assertThat(outputData.getUnchangedIds()).contains("Patient/ABC/_history/123");
		assertThat(outputData.getChangedIds()).isEmpty();
		assertThat(outputData.getFailures()).isEmpty();
	}


	private static class MyParameters extends BaseBulkModifyJobParameters {

	}

	private static class MyMockTxService extends HapiTransactionService {

		@Nullable
		@Override
		public <T> T doExecute(ExecutionBuilder theExecutionBuilder, TransactionCallback<T> theCallback) {
			boolean initialState = TransactionSynchronizationManager.isActualTransactionActive();
			try {
				if (!initialState) {
					TransactionSynchronizationManager.setActualTransactionActive(true);
				}
				return theCallback.doInTransaction(new SimpleTransactionStatus());
			} finally {
				if (!initialState) {
					TransactionSynchronizationManager.setActualTransactionActive(false);
				}
			}
		}
	}

	private class MySvc extends BaseBulkModifyResourcesStep<MyParameters, Integer> {
		@Override
		protected ResourceModificationResponse modifyResource(MyParameters theJobParameters, Integer theModificationContext, @Nonnull ResourceModificationRequest theModificationRequest) {
			return myFunction.apply(theModificationRequest);
		}
	}

}
