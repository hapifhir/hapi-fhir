package ca.uhn.fhir.mdm.batch2.clear;

import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.mdm.helper.MdmHelperR4;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;


class MdmClearStepTest extends BaseMdmR4Test {
	private static final String GOLDEN_ID = "Patient/GOLDEN-ID";
	private static final String SOURCE_ID = "Patient/SOURCE-ID";
	@Autowired
	MdmClearStep myMdmClearStep;
	@Autowired
	MdmHelperR4 myMdmHelperR4;

	private Long mySourcePid;
	private Long myGoldenPid;
	private MdmLink myLink;
	private String myGoldenId;
	private String mySourceId;

	@BeforeEach
	public void before() {
		Patient sourcePatient = new Patient();
		mySourceId = SOURCE_ID + "1";
		sourcePatient.setId(mySourceId);
		myPatientDao.update(sourcePatient);

		Patient goldenPatient = myMdmHelperR4.buildGoldenPatient();
		myGoldenId = GOLDEN_ID + "1";
		goldenPatient.setId(myGoldenId);
		myPatientDao.update(goldenPatient);

		mySourcePid = myIdHelperService.getPidOrThrowException(sourcePatient).getId();
		myGoldenPid = myIdHelperService.getPidOrThrowException(goldenPatient).getId();

		myLink = buildMdmLink(mySourcePid, myGoldenPid);
		myMdmLinkDaoSvc.save(myLink);
	}

	@Test
	public void testSimpleCase() {
		assertPatientCount(2);
		assertLinkCount(1);

		mdmClearGoldenResource();

		assertLinkCount(0);
		assertPatientCount(1);
		assertPatientExists(mySourceId);
	}

	@Test
	public void testWithReferenceToGoldenResource() {
		Patient husband = new Patient();
		husband.addLink().setOther(new Reference(myGoldenId));
		String husbandId = myPatientDao.create(husband).getId().toUnqualifiedVersionless().getValue();

		assertPatientCount(3);
		assertLinkCount(1);

		try {
			mdmClearGoldenResource();
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).isEqualTo(String.format("HAPI-0822: DELETE with _expunge=true failed.  Unable to delete %s because %s refers to it via the path Patient.link.other",
				myGoldenId,
				husbandId
			));
		}
	}

	@Test
	public void testGoldenResourceGetsExpunged() {
		mdmClearGoldenResource();
		try {
			assertPatientExists(myGoldenId);
			fail("Resource cannot be found");
		} catch (ResourceNotFoundException e) {
			assertEquals("HAPI-2001: Resource " + myGoldenId + " is not known", e.getMessage());
		}
	}

	private void mdmClearGoldenResource() {
		ResourceIdListWorkChunkJson chunk = new ResourceIdListWorkChunkJson();
		chunk.addTypedPid("Patient", myGoldenPid);

		RequestDetails requestDetails = new SystemRequestDetails();
		TransactionDetails transactionDetails = new TransactionDetails();
		StepExecutionDetails<MdmClearJobParameters, ResourceIdListWorkChunkJson> stepExecutionDetails = buildStepExecutionDetails(chunk);

		myMdmClearStep.myHapiTransactionService.execute(requestDetails, transactionDetails, myMdmClearStep.buildJob(requestDetails, transactionDetails, stepExecutionDetails));
	}

	@Nonnull
	private StepExecutionDetails<MdmClearJobParameters, ResourceIdListWorkChunkJson> buildStepExecutionDetails(ResourceIdListWorkChunkJson theListWorkChunkJson) {
		String instanceId = UUID.randomUUID().toString();
		JobInstance jobInstance = JobInstance.fromInstanceId(instanceId);
		String chunkid = UUID.randomUUID().toString();
		MdmClearJobParameters parms = new MdmClearJobParameters();

		StepExecutionDetails<MdmClearJobParameters, ResourceIdListWorkChunkJson> stepExecutionDetails = new StepExecutionDetails<>(parms, theListWorkChunkJson, jobInstance, new WorkChunk().setId(chunkid));
		return stepExecutionDetails;
	}

	private MdmLink buildMdmLink(Long sourcePid, Long goldenPid) {
		return new MdmLink()
			.setSourcePid(sourcePid)
			.setGoldenResourcePid(goldenPid)
			.setLinkSource(MdmLinkSourceEnum.MANUAL)
			.setMatchResult(MdmMatchResultEnum.MATCH)
			.setVersion("1");
	}

	private void assertPatientExists(String theSourceId) {
		assertNotNull(myPatientDao.read(new IdDt(theSourceId)));
	}

	private void assertPatientCount(int theExpectedCount) {
		assertEquals(theExpectedCount, myPatientDao.search(SearchParameterMap.newSynchronous()).size());
	}
}
