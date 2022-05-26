package ca.uhn.fhir.jpa.batch2.jobs.mdm;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunk;
import ca.uhn.fhir.batch2.jobs.mdm.MdmClearStep;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.mdm.helper.MdmHelperR4;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MdmClearStepTest extends BaseMdmR4Test {
	private static final String GOLDEN_ID = "Patient/GOLDEN-ID";
	private static final String SOURCE_ID = "Patient/SOURCE-ID";
	@Autowired
	MdmClearStep myMdmClearStep;
	@Autowired
	MdmHelperR4 myMdmHelperR4;

	@Mock
	IJobDataSink<VoidModel> myDataSink;

	@Test
	public void testSimpleCase() {
		Patient sourcePatient = new Patient();
		String sourceId = SOURCE_ID + "1";
		sourcePatient.setId(sourceId);
		myPatientDao.update(sourcePatient);

		Patient goldenPatient = myMdmHelperR4.buildGoldenPatient();
		String goldenId = GOLDEN_ID + "1";
		goldenPatient.setId(GOLDEN_ID);
		myPatientDao.update(goldenPatient);

		Long sourcePid = myIdHelperService.getPidOrThrowException(sourcePatient);
		Long goldenPid = myIdHelperService.getPidOrThrowException(goldenPatient);

		MdmLink link = buildMdmLink(sourcePid, goldenPid);
		myMdmLinkDaoSvc.save(link);

		assertPatientCount(2);
		assertLinkCount(1);

		ResourceIdListWorkChunk chunk = new ResourceIdListWorkChunk();
		chunk.addResourceId(goldenPatient.getIdElement());
		String instanceId = UUID.randomUUID().toString();
		String chunkid = UUID.randomUUID().toString();
		myMdmClearStep.doMdmClear(chunk, myDataSink, instanceId, chunkid);

		assertPatientCount(1);
		assertLinkCount(0);
		assertPatientExists(sourcePatient.getIdElement());
	}

	private MdmLink buildMdmLink(Long sourcePid, Long goldenPid) {
		return new MdmLink()
			.setSourcePid(sourcePid)
			.setGoldenResourcePid(goldenPid)
			.setLinkSource(MdmLinkSourceEnum.MANUAL)
			.setMatchResult(MdmMatchResultEnum.MATCH)
			.setVersion("1");
	}

	private void assertPatientExists(IdType theSourceId) {
		assertNotNull(myPatientDao.read(theSourceId));
	}

	private void assertPatientCount(int theExpectedCount) {
		assertEquals(theExpectedCount, myPatientDao.search(SearchParameterMap.newSynchronous()).size());
	}

	@Test
	public void testMdmGoldenResourcesForSameLinkInTwoChunks() {
		// FIXME KHS
	}

}
