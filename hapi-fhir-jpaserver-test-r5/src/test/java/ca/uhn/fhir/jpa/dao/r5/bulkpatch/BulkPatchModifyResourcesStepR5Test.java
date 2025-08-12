package ca.uhn.fhir.jpa.dao.r5.bulkpatch;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base.BaseBulkModifyResourcesStep;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common.BulkModifyResourcesChunkOutcomeJson;
import ca.uhn.fhir.batch2.jobs.bulkmodify.patch.BulkPatchJobParameters;
import ca.uhn.fhir.batch2.jobs.bulkmodify.patch.BulkPatchModifyResourcesStep;
import ca.uhn.fhir.batch2.jobs.bulkmodify.patchrewrite.BulkPatchRewriteJobParameters;
import ca.uhn.fhir.batch2.jobs.chunk.TypedPidAndVersionListWorkChunkJson;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.dao.r5.BaseJpaR5Test;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static ca.uhn.fhir.jpa.dao.r5.bulkpatch.BulkPatchJobR5Test.createPatchWithModifyPatientIdentifierSystem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class BulkPatchModifyResourcesStepR5Test extends BaseJpaR5Test {

	@Autowired
	private BulkPatchModifyResourcesStep<BulkPatchJobParameters> mySvcNonRewrite;
	@Autowired
	private BulkPatchModifyResourcesStep<BulkPatchRewriteJobParameters> mySvcRewrite;

	@Mock
	private IJobDataSink<BulkModifyResourcesChunkOutcomeJson> myDataSink;
	@Captor
	private ArgumentCaptor<BulkModifyResourcesChunkOutcomeJson> myDataCaptor;

	@Test
	public void testQueryCounts_Unversioned() {
		// Setup
		TypedPidAndVersionListWorkChunkJson data = new TypedPidAndVersionListWorkChunkJson();
		List<IIdType> ids = create10TestPatients(data);

		// Test
		BulkPatchJobParameters jobParameters = new BulkPatchJobParameters();
		jobParameters.setFhirPatch(myFhirContext, createPatchWithModifyPatientIdentifierSystem());

		WorkChunk workChunk = new WorkChunk();
		JobInstance jobInstance = new JobInstance();

		StepExecutionDetails<BulkPatchJobParameters, TypedPidAndVersionListWorkChunkJson> parameters = new StepExecutionDetails<>(jobParameters, data, jobInstance, workChunk);
		myCaptureQueriesListener.clear();
		mySvcNonRewrite.run(parameters, myDataSink);

		// Verify

		// Select HFJ_RESOURCE+HFJ_RES_VER, Select HFJ_SPIDX_TOKEN
		assertEquals(2, myCaptureQueriesListener.countSelectQueries());
		// 10 insert new HFJ_RES_VER
		assertEquals(10, myCaptureQueriesListener.countInsertQueries());
		// 10 update HFJ_SPIDX_TOKEN, 10 update HFJ_RESOURCE
		assertEquals(20, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());

		Patient actualPatient = myPatientDao.read(ids.get(0), newSrd());
		assertEquals("2", actualPatient.getIdElement().getVersionIdPart());
		assertEquals("http://foo", actualPatient.getIdentifier().get(0).getSystem());
		assertEquals("bar0", actualPatient.getIdentifier().get(0).getValue());
	}

	@Test
	public void testQueryCounts_Versioned() {
		// Setup
		myStorageSettings.setUpdateWithHistoryRewriteEnabled(true);
		TypedPidAndVersionListWorkChunkJson data = new TypedPidAndVersionListWorkChunkJson();
		List<IIdType> ids = new ArrayList<>();
		for (int resIdx = 0; resIdx < 10; resIdx++) {
			IIdType id = createPatient(withIdentifier("http://blah", "bar" + resIdx));
			ids.add(id);
			data.addTypedPidWithNullPartitionForUnitTest("Patient", id.getIdPartAsLong(), 1L);
			for (long versionIdx = 2; versionIdx <= 5; versionIdx++) {
				createPatient(withId(id.getIdPart()), withIdentifier("http://blah", "bar" + resIdx + "v" + versionIdx));
				data.addTypedPidWithNullPartitionForUnitTest("Patient", id.getIdPartAsLong(), versionIdx);
			}
		}

		// Test
		BulkPatchRewriteJobParameters jobParameters = new BulkPatchRewriteJobParameters();
		jobParameters.setFhirPatch(myFhirContext, createPatchWithModifyPatientIdentifierSystem());

		WorkChunk workChunk = new WorkChunk();
		JobInstance jobInstance = new JobInstance();

		StepExecutionDetails<BulkPatchRewriteJobParameters, TypedPidAndVersionListWorkChunkJson> parameters = new StepExecutionDetails<>(jobParameters, data, jobInstance, workChunk);
		myCaptureQueriesListener.clear();
		mySvcRewrite.run(parameters, myDataSink);

		// Verify

		// This could definitely be optimized more
		assertEquals(82, myCaptureQueriesListener.countSelectQueries());
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(70, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());

		Patient actualPatient = myPatientDao.read(ids.get(0).withVersion("4"), newSrd());
		assertEquals("4", actualPatient.getIdElement().getVersionIdPart());
		assertEquals("http://foo", actualPatient.getIdentifier().get(0).getSystem());
		assertEquals("bar0v4", actualPatient.getIdentifier().get(0).getValue());
		actualPatient = myPatientDao.read(ids.get(0), newSrd());
		assertEquals("5", actualPatient.getIdElement().getVersionIdPart());
		assertEquals("http://foo", actualPatient.getIdentifier().get(0).getSystem());
		assertEquals("bar0v5", actualPatient.getIdentifier().get(0).getValue());
	}


	@ParameterizedTest
	@CsvSource(
		//  FailureCount
		textBlock = """
			2
			100
			""")
	public void testStorageFailure(int theErrorCount) {
		// Setup
		TypedPidAndVersionListWorkChunkJson data = new TypedPidAndVersionListWorkChunkJson();
		List<IIdType> ids = create10TestPatients(data);

		AtomicLong failuresRemaining = new AtomicLong(theErrorCount);
		IIdType succeedingId = ids.get(0);
		IIdType failingId = ids.get(1);
		IIdType succeedingId2 = ids.get(2);
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED, (pointcut, args) -> {
			IIdType resourceId = args.get(IBaseResource.class).getIdElement();
			if (resourceId.getIdPartAsLong().equals(failingId.getIdPartAsLong())) {
				if (failuresRemaining.getAndDecrement() > 0) {
					throw new RuntimeException("Simulated storage failure");
				}
			}
		});

		// Test
		BulkPatchJobParameters jobParameters = new BulkPatchJobParameters();
		jobParameters.setFhirPatch(myFhirContext, createPatchWithModifyPatientIdentifierSystem());

		WorkChunk workChunk = new WorkChunk();
		JobInstance jobInstance = new JobInstance();

		StepExecutionDetails<BulkPatchJobParameters, TypedPidAndVersionListWorkChunkJson> parameters = new StepExecutionDetails<>(jobParameters, data, jobInstance, workChunk);
		myCaptureQueriesListener.clear();
		mySvcNonRewrite.run(parameters, myDataSink);

		// Verify
		Patient actualPatient = myPatientDao.read(failingId, newSrd());
		if (theErrorCount == 2) {
			assertEquals("2", actualPatient.getIdElement().getVersionIdPart());
			assertEquals("http://foo", actualPatient.getIdentifier().get(0).getSystem());
		} else if (theErrorCount == 100) {
			assertEquals("1", actualPatient.getIdElement().getVersionIdPart());
			assertEquals("http://blah", actualPatient.getIdentifier().get(0).getSystem());
		} else {
			fail("Unexpected error count " + theErrorCount);
		}
		assertEquals("bar1", actualPatient.getIdentifier().get(0).getValue());

		logAllResources();
		logAllResourceVersions();

		actualPatient = myPatientDao.read(succeedingId.withVersion("2"), newSrd());
		assertEquals("http://foo", actualPatient.getIdentifier().get(0).getSystem());
		assertEquals("bar0", actualPatient.getIdentifier().get(0).getValue());
		assertEquals("2", actualPatient.getIdElement().getVersionIdPart());
		actualPatient = myPatientDao.read(succeedingId2.withVersion("2"), newSrd());
		assertEquals("http://foo", actualPatient.getIdentifier().get(0).getSystem());
		assertEquals("bar2", actualPatient.getIdentifier().get(0).getValue());
		assertEquals("2", actualPatient.getIdElement().getVersionIdPart());

		verify(myDataSink, times(1)).accept(myDataCaptor.capture());
		BulkModifyResourcesChunkOutcomeJson outcome = myDataCaptor.getValue();
		assertEquals(2, outcome.getChunkRetryCount());
		assertEquals(11, outcome.getResourceRetryCount());

	}

	private List<IIdType> create10TestPatients(TypedPidAndVersionListWorkChunkJson data) {
		int count = 10;
		return createTestPatients(data, count);
	}

	@Nonnull
	private List<IIdType> createTestPatients(TypedPidAndVersionListWorkChunkJson data, int count) {
		List<IIdType> ids = new ArrayList<>();
		for (int resIdx = 0; resIdx < count; resIdx++) {
			IIdType id = createPatient(withIdentifier("http://blah", "bar" + resIdx));
			ids.add(id);
			data.addTypedPidWithNullPartitionForUnitTest("Patient", id.getIdPartAsLong(), null);
		}
		return ids;
	}

}
