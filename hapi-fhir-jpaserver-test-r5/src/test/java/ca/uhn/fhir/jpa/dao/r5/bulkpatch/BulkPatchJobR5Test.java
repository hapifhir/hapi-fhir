package ca.uhn.fhir.jpa.dao.r5.bulkpatch;

import ca.uhn.fhir.batch2.jobs.bulkmodify.patch.BulkPatchJobAppCtx;
import ca.uhn.fhir.batch2.jobs.bulkmodify.patch.BulkPatchJobParameters;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.dao.r5.BaseJpaR5Test;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r5.model.CodeType;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.Parameters;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.StringType;
import org.hl7.fhir.r5.model.UriType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BulkPatchJobR5Test extends BaseJpaR5Test {

	@Test
	public void testBulkPatch() {
		// Setup
		createPatient(withId("A"), withIdentifier("http://blah", "A1"));
		createPatient(withId("B"), withIdentifier("http://blah", "B1"));
		createPatient(withId("C"), withIdentifier("http://blah", "C1"));

		// Test
		Parameters patchDocument = createPatchWithModifyPatientIdentifierSystem();

		BulkPatchJobParameters jobParameters = new BulkPatchJobParameters();
		jobParameters.addUrl("Patient?");
		jobParameters.setFhirPatch(myFhirContext, patchDocument);

		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(BulkPatchJobAppCtx.JOB_ID);
		startRequest.setParameters(jobParameters);
		Batch2JobStartResponse jobId = myJobCoordinator.startInstance(new SystemRequestDetails(), startRequest);
		myBatch2JobHelper.awaitJobCompletion(jobId);

		// Verify
		Patient actualPatientA = myPatientDao.read(new IdType("Patient/A"), newSrd());
		assertEquals("http://foo", actualPatientA.getIdentifier().get(0).getSystem());
		assertEquals("A1", actualPatientA.getIdentifier().get(0).getValue());
		Patient actualPatientB = myPatientDao.read(new IdType("Patient/B"), newSrd());
		assertEquals("http://foo", actualPatientB.getIdentifier().get(0).getSystem());
		assertEquals("B1", actualPatientB.getIdentifier().get(0).getValue());
	}

	@Nonnull
	static Parameters createPatchWithModifyPatientIdentifierSystem() {
		Parameters patchDocument = new Parameters();
		Parameters.ParametersParameterComponent comp = patchDocument.addParameter().setName("operation");
		comp.addPart().setName("type").setValue(new CodeType("replace"));
		comp.addPart().setName("path").setValue(new StringType("Patient.identifier.system"));
		comp.addPart().setName("value").setValue(new UriType("http://foo"));
		return patchDocument;
	}

}
