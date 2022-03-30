package ca.uhn.fhir.jpa.mdm.provider;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.mdm.api.IMdmLinkSvc;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchOutcome;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class MdmProviderNotDuplicateGoldenResourceR4Test extends BaseProviderR4Test {
	@Autowired
	IMdmLinkSvc myMdmLinkSvc;
	private Patient myGoldenPatient;
	private StringType myGoldenPatientId;
	private Patient myTargetPatient;
	private StringType myTargetPatientId;

	@Override
	@BeforeEach
	public void before() {
		super.before();

		myGoldenPatient = createGoldenPatient();
		myGoldenPatientId = new StringType(myGoldenPatient.getIdElement().getValue());
		myTargetPatient = createGoldenPatient();
		myTargetPatientId = new StringType(myTargetPatient.getIdElement().getValue());
	}

	@AfterEach
	public void after() throws IOException {
		myPartitionSettings.setPartitioningEnabled(false);
		super.after();
	}

	@Test
	public void testNotDuplicateGoldenResource() {
		myMdmLinkSvc.updateLink(myGoldenPatient, myTargetPatient, MdmMatchOutcome.POSSIBLE_DUPLICATE, MdmLinkSourceEnum.AUTO, createContextForCreate("Patient"));
		assertLinkCount(1);
		myMdmProvider.notDuplicate(myGoldenPatientId, myTargetPatientId, myRequestDetails);
		assertLinkCount(1);

		List<MdmLink> links = myMdmLinkDaoSvc.findMdmLinksBySourceResource(myTargetPatient);
		assertEquals(MdmLinkSourceEnum.MANUAL, links.get(0).getLinkSource());
		assertEquals(MdmMatchResultEnum.NO_MATCH, links.get(0).getMatchResult());
	}

	@Test
	public void testNotDuplicateGoldenResourceNoLinkBetweenResources() {
		try {
			myMdmProvider.notDuplicate(myGoldenPatientId, myTargetPatientId, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), startsWith("HAPI-0745: No link exists between"));
		}
	}

	@Test
	public void testNotDuplicateGoldenResourceNotPossibleDuplicate() {
		myMdmLinkSvc.updateLink(myGoldenPatient, myTargetPatient, MdmMatchOutcome.POSSIBLE_MATCH, MdmLinkSourceEnum.AUTO, createContextForCreate("Patient"));
		assertLinkCount(1);
		try {
			myMdmProvider.notDuplicate(myGoldenPatientId, myTargetPatientId, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), endsWith("are not linked as POSSIBLE_DUPLICATE."));
		}
	}

	@Test
	public void testNotDuplicateGoldenResourceOnSamePartition() {
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionLookupSvc.createPartition(new PartitionEntity().setId(1).setName(PARTITION_1));
		RequestPartitionId requestPartitionId = RequestPartitionId.fromPartitionId(1);
		Patient goldenPatient = createPatientOnPartition(new Patient(), true, false, requestPartitionId);
		StringType goldenPatientId = new StringType(goldenPatient.getIdElement().getValue());
		Patient targetPatient = createPatientOnPartition(new Patient(), true, false, requestPartitionId);
		StringType targetPatientId = new StringType(targetPatient.getIdElement().getValue());

		myMdmLinkSvc.updateLink(goldenPatient, targetPatient, MdmMatchOutcome.POSSIBLE_DUPLICATE, MdmLinkSourceEnum.AUTO, createContextForCreate("Patient"));
		assertLinkCount(1);
		myMdmProvider.notDuplicate(goldenPatientId, targetPatientId, myRequestDetails);
		assertLinkCount(1);

		List<MdmLink> links = myMdmLinkDaoSvc.findMdmLinksBySourceResource(targetPatient);
		assertEquals(MdmLinkSourceEnum.MANUAL, links.get(0).getLinkSource());
		assertEquals(MdmMatchResultEnum.NO_MATCH, links.get(0).getMatchResult());
	}

	@Test
	public void testNotDuplicateGoldenResourceOnDifferentPartitions() {
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionLookupSvc.createPartition(new PartitionEntity().setId(1).setName(PARTITION_1));
		RequestPartitionId requestPartitionId1 = RequestPartitionId.fromPartitionId(1);
		myPartitionLookupSvc.createPartition(new PartitionEntity().setId(2).setName(PARTITION_2));
		RequestPartitionId requestPartitionId2 = RequestPartitionId.fromPartitionId(2);
		Patient goldenPatient = createPatientOnPartition(new Patient(), true, false, requestPartitionId1);
		StringType goldenPatientId = new StringType(goldenPatient.getIdElement().getValue());
		Patient targetPatient = createPatientOnPartition(new Patient(), true, false, requestPartitionId2);
		StringType targetPatientId = new StringType(targetPatient.getIdElement().getValue());

		try {
			myMdmProvider.notDuplicate(goldenPatientId, targetPatientId, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), startsWith("HAPI-0745: No link exists between"));
		}
	}
}
