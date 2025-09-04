package ca.uhn.fhir.jpa.dao.r5.bulkpatch;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.api.ResourceModificationRequest;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.api.ResourceModificationResponse;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base.BaseBulkModifyResourcesStep;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common.BulkModifyResourcesChunkOutcomeJson;
import ca.uhn.fhir.batch2.jobs.bulkmodify.patch.BulkPatchJobParameters;
import ca.uhn.fhir.batch2.jobs.chunk.TypedPidAndVersionJson;
import ca.uhn.fhir.batch2.jobs.chunk.TypedPidAndVersionListWorkChunkJson;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.r5.BaseJpaR5Test;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class BaseBulkModifyResourcesStepR5Test extends BaseJpaR5Test {

	@Mock
	private IMockStep myMockStep;
	@Mock
	private IJobDataSink<BulkModifyResourcesChunkOutcomeJson> mySink;

	private MyBulkModifyResourcesStep myStep;

	@Autowired
	private IIdHelperService<IResourcePersistentId<?>> myIdHelper;

	@BeforeEach
	public void before() {
		myStep = new MyBulkModifyResourcesStep();
		myStep.setSystemDaoForUnitTest(mySystemDao);
		myStep.setDaoRegistryForUnitTest(myDaoRegistry);
		myStep.setTransactionServiceForUnitTest(myTxService);
		myStep.setFhirContextForUnitTest(myFhirCtx);
		myStep.setIdHelperServiceForUnitTest(myIdHelper);
		myStep.setMockStep(myMockStep);
	}

	@Test
	public void testDeleteResource() {
		createPatient(withId("P1"), withFamily("Family1"));
		createPatient(withId("P2"), withFamily("Family2"));
		TypedPidAndVersionListWorkChunkJson data = createWorkChunkForAllResources();
		StepExecutionDetails<BulkPatchJobParameters, TypedPidAndVersionListWorkChunkJson> stepExecutionDetails = new StepExecutionDetails<>(new BulkPatchJobParameters(), data, new JobInstance(), new WorkChunk());

		when(myMockStep.modifyResource(any(), any(), any())).thenAnswer(t->{
			ResourceModificationRequest request = t.getArgument(2, ResourceModificationRequest.class);
			Patient patient = (Patient) request.getResource();
			if ("P1".equals(patient.getIdElement().getIdPart())) {
				return ResourceModificationResponse.noChange();
			}
			if ("P2".equals(patient.getIdElement().getIdPart())) {
				return ResourceModificationResponse.delete();
			}
			throw new IllegalStateException();
		});

		// Test
		RunOutcome outcome = myStep.run(stepExecutionDetails, mySink);
		assertEquals(0, outcome.getRecordsProcessed());

		// Verify
		assertNotGone("Patient/P1");
		assertGone("Patient/P2");
	}

	@Test
	public void testDeleteResource_RewriteHistoryIsBlocked() {
		createPatient(withId("P1"), withFamily("Family1"));
		TypedPidAndVersionListWorkChunkJson data = createWorkChunkForAllResources();
		StepExecutionDetails<BulkPatchJobParameters, TypedPidAndVersionListWorkChunkJson> stepExecutionDetails = new StepExecutionDetails<>(new BulkPatchJobParameters(), data, new JobInstance(), new WorkChunk());

		when(myMockStep.isRewriteHistory(any(), any())).thenReturn(true);
		when(myMockStep.modifyResource(any(), any(), any())).thenReturn(ResourceModificationResponse.delete());

		// Test & Verify
		assertThatThrownBy(()->myStep.run(stepExecutionDetails, mySink))
			.isInstanceOf(JobExecutionFailedException.class)
			.hasMessageContaining("Can't store deleted resources as history rewrites");
	}

	@Nonnull
	private TypedPidAndVersionListWorkChunkJson createWorkChunkForAllResources() {
		TypedPidAndVersionListWorkChunkJson data = new TypedPidAndVersionListWorkChunkJson();
		runInTransaction(()->{
			for (ResourceTable next : myResourceTableDao.findAll()) {
				data.addTypedPidWithNullPartitionForUnitTest(next.getResourceType(), next.getId().getId(), next.getVersion());
			}
		});
		return data;
	}


	public static class MyBulkModifyResourcesStep extends BaseBulkModifyResourcesStep<BulkPatchJobParameters, Object> {

		private IMockStep myMockStep;

		@Override
		public boolean isRewriteHistory(Object theState, IBaseResource theResource) {
			return myMockStep.isRewriteHistory(theState, theResource);
		}

		@Nullable
		@Override
		protected Object preModifyResources(BulkPatchJobParameters theJobParameters, List<TypedPidAndVersionJson> thePids) {
			return myMockStep.preModifyResources(theJobParameters, thePids);
		}

		@Override
		protected ResourceModificationResponse modifyResource(BulkPatchJobParameters theJobParameters, Object theModificationContext, @Nonnull ResourceModificationRequest theModificationRequest) {
			return myMockStep.modifyResource(theJobParameters, theModificationContext, theModificationRequest);
		}

		public void setMockStep(IMockStep theMockStep) {
			assert myMockStep == null;
			myMockStep = theMockStep;
		}
	}


	interface IMockStep {

		Object preModifyResources(BulkPatchJobParameters theJobParameters, List<TypedPidAndVersionJson> thePids);

		ResourceModificationResponse modifyResource(BulkPatchJobParameters theJobParameters, Object theModificationContext, @Nonnull ResourceModificationRequest theModificationRequest);

		boolean isRewriteHistory(Object theState, IBaseResource theResource);
	}

}
