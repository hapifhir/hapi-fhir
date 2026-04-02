package ca.uhn.fhir.jpa.dao.r5.bulkpatch;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.api.ResourceModificationRequest;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.api.ResourceModificationResponse;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base.BaseBulkModifyResourcesIndividuallyStep;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common.BulkModifyResourcesChunkOutcomeJson;
import ca.uhn.fhir.batch2.jobs.bulkmodify.patch.BulkPatchJobParameters;
import ca.uhn.fhir.batch2.jobs.chunk.TypedPidAndVersionListWorkChunkJson;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.r5.BaseJpaR5Test;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Task;
import org.hl7.fhir.r5.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BaseBulkModifyResourcesStepR5Test extends BaseJpaR5Test {

	@Mock
	private IMockStep myMockStep;
	@Mock
	private IJobDataSink<BulkModifyResourcesChunkOutcomeJson> mySink;
	@Captor
	private ArgumentCaptor<BulkModifyResourcesChunkOutcomeJson> myDataCaptor;

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

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testDeleteResource(boolean theDryRun) {
		// start
final List<IResourcePersistentId<?>> resourceIDs = null; //getResourceIDs(theStepExecutionDetails);
Set<String> fhirIds = myIdHelper.translatePidsToFhirResourceIds(new HashSet<>(resourceIDs));

IFhirResourceDao taskDao = myDaoRegistry.getResourceDao("Task");
SearchParameterMap params = new SearchParameterMap();
params.setLoadSynchronous(true);
params.add(IAnyResource.SP_RES_ID, new TokenOrListParam(null, fhirIds.toArray(new String[0])));
List<IBaseResource> allResources = taskDao.search(params).getAllResources();
List<Task> allTasks = allResources.stream().filter(t->t instanceof Task).map(t->(Task)t).toList();

Map<String, String> taskIdToFocusId = new HashMap<>();
Map<String, String> taskIdToReasonId = new HashMap<>();

Multimap<String, String> typeToTargetResources = HashMultimap.create();
for (Task task : allTasks) {
	IIdType reasonReference = task.getReasonReference().getReferenceElement();
	if (reasonReference != null && reasonReference.hasIdPart()) {
		typeToTargetResources.put(reasonReference.getResourceType(), reasonReference.getIdPart());
		taskIdToReasonId.put(task.getIdElement().getIdPart(), reasonReference.toUnqualifiedVersionless().getValue());
	}
	IIdType focusReference = task.getFocus().getReferenceElement();
	if (focusReference != null && focusReference.hasIdPart()) {
		typeToTargetResources.put(reasonReference.getResourceType(), reasonReference.getIdPart());
		taskIdToFocusId.put(task.getIdElement().getIdPart(), focusReference.toUnqualifiedVersionless().getValue());
	}
}

Map<String, IBaseResource> idToTargetResource = new HashMap<>();
for (String resourceType : typeToTargetResources.keySet()) {
	List<String> resourceIds = typeToTargetResources.get(resourceType);
	IFhirResourceDao dao = myDaoRegistry.getResourceDao(resourceType);
	params = new SearchParameterMap();
	params.setLoadSynchronous(true);
	params.add(IAnyResource.SP_RES_ID, new TokenOrListParam(null, resourceIds.toArray(new String[0])));
	dao.search(params).getAllResources().forEach(r->idToTargetResource.put(r.getIdElement().toUnqualifiedVersionless().getValue(), r));
}

for (Task task : allTasks) {
	String reasonId = taskIdToReasonId.get(task.getIdElement().getIdPart());
	if (reasonId != null) {
		IBaseResource reasonResource = idToTargetResource.get(reasonId);
		if (reasonResource != null) {
			// ... add meta to this
		}
	}
	String focusId = taskIdToFocusId.get(task.getIdElement().getIdPart());
	if (focusId != null) {
		IBaseResource reasonResource = idToTargetResource.get(focusId);
		if (reasonResource != null) {
			// ... add meta to this
		}
	}
}
		// end



		createPatient(withId("P1"), withFamily("Family1"));
		createPatient(withId("P2"), withFamily("Family2"));
		TypedPidAndVersionListWorkChunkJson data = createWorkChunkForAllResources();
		BulkPatchJobParameters jobParameters = new BulkPatchJobParameters();
		jobParameters.setDryRun(theDryRun);
		WorkChunk chunk = new WorkChunk().setId("my-chunk-id");
		StepExecutionDetails<BulkPatchJobParameters, TypedPidAndVersionListWorkChunkJson> stepExecutionDetails = new StepExecutionDetails<>(jobParameters, data, new JobInstance(), chunk, myJobStepExecutionServices);

		when(myMockStep.modifyResource(any(), any())).thenAnswer(t -> {
			ResourceModificationRequest request = t.getArgument(1, ResourceModificationRequest.class);
			Patient patient = (Patient) request.getResource();
			// Delete Patient/P2, leave Patient/P1 alone
			if ("P2".equals(patient.getIdElement().getIdPart())) {
				return ResourceModificationResponse.delete();
			}
			if ("P1".equals(patient.getIdElement().getIdPart())) {
				return ResourceModificationResponse.noChange();
			}
			throw new IllegalStateException();
		});

		// Test
		RunOutcome outcome = myStep.run(stepExecutionDetails, mySink);
		assertEquals(2, outcome.getRecordsProcessed());

		// Verify
		assertNotGone("Patient/P1");
		if (theDryRun) {
			assertNotGone("Patient/P2");
		} else {
			assertGone("Patient/P2");
		}

		verify(mySink, times(1)).accept(myDataCaptor.capture());
		BulkModifyResourcesChunkOutcomeJson stepOutcome = myDataCaptor.getValue();
		assertThat(stepOutcome.getDeletedIds()).containsExactlyInAnyOrder("Patient/P2/_history/1");
	}

	@Test
	public void testDeleteResource_RewriteHistoryIsBlocked() {
		createPatient(withId("P1"), withFamily("Family1"));
		TypedPidAndVersionListWorkChunkJson data = createWorkChunkForAllResources();
		WorkChunk chunk = new WorkChunk().setId("my-chunk-id");
		StepExecutionDetails<BulkPatchJobParameters, TypedPidAndVersionListWorkChunkJson> stepExecutionDetails = new StepExecutionDetails<>(new BulkPatchJobParameters(), data, new JobInstance(), chunk, myJobStepExecutionServices);

		when(myMockStep.isRewriteHistory(any(), any())).thenReturn(true);
		when(myMockStep.modifyResource(any(), any())).thenReturn(ResourceModificationResponse.delete());

		// Test & Verify
		assertThatThrownBy(() -> myStep.run(stepExecutionDetails, mySink))
			.isInstanceOf(JobExecutionFailedException.class)
			.hasMessageContaining("Can't store deleted resources as history rewrites");
	}

	@Nonnull
	private TypedPidAndVersionListWorkChunkJson createWorkChunkForAllResources() {
		TypedPidAndVersionListWorkChunkJson data = new TypedPidAndVersionListWorkChunkJson();
		runInTransaction(() -> {
			for (ResourceTable next : myResourceTableDao.findAll()) {
				data.addTypedPidWithNullPartitionForUnitTest(next.getResourceType(), next.getId().getId(), next.getVersion());
			}
		});
		return data;
	}


	interface IMockStep {

		Object preModifyResources(StepExecutionDetails<BulkPatchJobParameters, TypedPidAndVersionListWorkChunkJson> theJobParameters, List<IBaseResource> thePids);

		ResourceModificationResponse modifyResource(Object theModificationContext, @Nonnull ResourceModificationRequest theModificationRequest);

		boolean isRewriteHistory(Object theState, IBaseResource theResource);
	}

	public static class MyBulkModifyResourcesStep extends BaseBulkModifyResourcesIndividuallyStep<BulkPatchJobParameters, Object> {

		private IMockStep myMockStep;

		@Override
		public boolean isRewriteHistory(StepExecutionDetails<BulkPatchJobParameters, TypedPidAndVersionListWorkChunkJson> theStepExecutionDetails, Object theState, IBaseResource theResource) {
			return myMockStep.isRewriteHistory(theState, theResource);
		}

		@Override
		protected String getJobNameForLogging() {
			return "TEST-JOB";
		}

		@Nullable
		@Override
		protected Object preModifyResources(StepExecutionDetails<BulkPatchJobParameters, TypedPidAndVersionListWorkChunkJson> theStepExecutionDetails, List<IBaseResource> theResources) {
			return myMockStep.preModifyResources(theStepExecutionDetails, theResources);
		}

		@Override
		protected ResourceModificationResponse modifyResource(StepExecutionDetails<BulkPatchJobParameters, TypedPidAndVersionListWorkChunkJson> theStepExecutionDetails, Object theModificationContext, @Nonnull ResourceModificationRequest theModificationRequest) {
			return myMockStep.modifyResource(theModificationContext, theModificationRequest);
		}

		void setMockStep(IMockStep theMockStep) {
			assert myMockStep == null;
			myMockStep = theMockStep;
		}
	}

}
