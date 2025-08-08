package ca.uhn.fhir.jpa.dao.r5.bulkpatch;

import ca.uhn.fhir.batch2.jobs.bulkmodify.patch.BulkPatchJobAppCtx;
import ca.uhn.fhir.batch2.jobs.bulkmodify.patch.BulkPatchJobParameters;
import ca.uhn.fhir.batch2.jobs.bulkmodify.patchrewrite.BulkPatchRewriteJobAppCtx;
import ca.uhn.fhir.batch2.jobs.bulkmodify.patchrewrite.BulkPatchRewriteJobParameters;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.dao.r5.BaseJpaR5Test;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.TokenParam;
import net.sourceforge.plantuml.klimt.creole.Sea;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.Parameters;
import org.hl7.fhir.r5.model.Patient;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static ca.uhn.fhir.jpa.dao.index.IdHelperService.RESOURCE_PID;
import static ca.uhn.fhir.jpa.dao.r5.bulkpatch.BulkPatchJobR5Test.createPatchWithModifyPatientIdentifierSystem;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BulkPatchRewriteJobR5Test extends BaseJpaR5Test {

	@Test
	public void testBulkPatchRewrite() {
		// Setup
		myStorageSettings.setUpdateWithHistoryRewriteEnabled(true);
		for (int resourceIdx = 0; resourceIdx < 5; resourceIdx++) {
			for (int version = 1; version <= 5; version++) {
				createPatient(withId("P" + resourceIdx), withFamily("Resource-" + resourceIdx + "-Version-" + version), withIdentifier("http://old", "123"));
			}
		}

		// Test
		Parameters patchDocument = createPatchWithModifyPatientIdentifierSystem();

		BulkPatchRewriteJobParameters jobParameters = new BulkPatchRewriteJobParameters();
		jobParameters.addUrl("Patient?");
		jobParameters.setFhirPatch(myFhirContext, patchDocument);

		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(BulkPatchRewriteJobAppCtx.JOB_ID);
		startRequest.setParameters(jobParameters);
		Batch2JobStartResponse jobId = myJobCoordinator.startInstance(new SystemRequestDetails(), startRequest);
		myBatch2JobHelper.awaitJobCompletion(jobId);

		// Verify that resources were updated correctly
		AtomicReference<JpaPid> jpaPid = new AtomicReference<>();
		for (int resourceIdx = 0; resourceIdx < 5; resourceIdx++) {
			for (int version = 1; version <= 5; version++) {
				Patient resource = myPatientDao.read(new IdType("Patient/P" + resourceIdx + "/_history/" + version), newSrd());
				if (jpaPid.get() == null) {
					jpaPid.set((JpaPid) resource.getUserData(RESOURCE_PID));
				}

				assertEquals("http://foo", resource.getIdentifier().get(0).getSystem());
				assertEquals(Integer.toString(version), resource.getIdElement().getVersionIdPart());
				assertEquals("Resource-" + resourceIdx + "-Version-" + version, resource.getNameFirstRep().getFamily());
			}
		}

		runInTransaction(()->{
			// Verify that no new versions were created
			assertEquals(25, myResourceHistoryTableDao.count());

			// Verify that versions still point to the current version
			assertEquals(5L, myResourceTableDao.getReferenceById(jpaPid.get()).getVersion());

		});

		// Verify that the current version is indexed
		SearchParameterMap params = SearchParameterMap.newSynchronous("identifier", new TokenParam("http://foo", "123"));
		List<String> actual = toUnqualifiedIdValues(myPatientDao.search(params, newSrd()));
		assertThat(actual).containsExactlyInAnyOrder(
			"Patient/P0/_history/5",
			"Patient/P1/_history/5",
			"Patient/P2/_history/5",
			"Patient/P3/_history/5",
			"Patient/P4/_history/5"
		);
	}

}
