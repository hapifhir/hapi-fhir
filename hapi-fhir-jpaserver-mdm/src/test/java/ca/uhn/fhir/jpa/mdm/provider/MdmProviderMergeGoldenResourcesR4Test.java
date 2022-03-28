package ca.uhn.fhir.jpa.mdm.provider;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.util.MdmResourceUtil;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.TerserUtil;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class MdmProviderMergeGoldenResourcesR4Test extends BaseProviderR4Test {

	private Patient myFromGoldenPatient;
	private StringType myFromGoldenPatientId;
	private Patient myToGoldenPatient;
	private StringType myToGoldenPatientId;

	@Override
	@BeforeEach
	public void before() {
		super.before();

		myFromGoldenPatient = createGoldenPatient();
		myFromGoldenPatientId = new StringType(myFromGoldenPatient.getIdElement().getValue());
		myToGoldenPatient = createGoldenPatient();
		myToGoldenPatientId = new StringType(myToGoldenPatient.getIdElement().getValue());
	}

	@AfterEach
	public void after() throws IOException {
		myPartitionSettings.setPartitioningEnabled(new PartitionSettings().isPartitioningEnabled());
		super.after();
	}

	@Test
	public void testMergeWithOverride() {
		myFromGoldenPatient.getName().clear();
		myFromGoldenPatient.addName().setFamily("Family").addGiven("Given");
		myFromGoldenPatient.addCommunication();
		myFromGoldenPatient.addExtension();

		Patient mergedSourcePatient = (Patient) myMdmProvider.mergeGoldenResources(myFromGoldenPatientId,
			myToGoldenPatientId, myFromGoldenPatient, myRequestDetails);

		assertEquals(myFromGoldenPatient.getName().size(), mergedSourcePatient.getName().size());
		assertEquals(myFromGoldenPatient.getName().get(0).getNameAsSingleString(), mergedSourcePatient.getName().get(0).getNameAsSingleString());
		assertEquals(myFromGoldenPatient.getCommunication().size(), mergedSourcePatient.getCommunication().size());
		assertEquals(myFromGoldenPatient.getExtension().size(), mergedSourcePatient.getExtension().size());

		Patient toGoldenPatient = myPatientDao.read(myToGoldenPatient.getIdElement().toUnqualifiedVersionless());
		assertEquals(toGoldenPatient.getName().size(), mergedSourcePatient.getName().size());
		assertEquals(toGoldenPatient.getName().get(0).getNameAsSingleString(), mergedSourcePatient.getName().get(0).getNameAsSingleString());
	}


	@Test
	public void testMerge() {
		Patient mergedSourcePatient = (Patient) myMdmProvider.mergeGoldenResources(myFromGoldenPatientId,
			myToGoldenPatientId, null, myRequestDetails);

		// we do not check setActive anymore - as not all types support that
		assertTrue(MdmResourceUtil.isGoldenRecord(mergedSourcePatient));
		assertFalse(MdmResourceUtil.isGoldenRecordRedirected(mergedSourcePatient));

		assertEquals(myToGoldenPatient.getIdElement(), mergedSourcePatient.getIdElement());
		assertThat(mergedSourcePatient, is(sameGoldenResourceAs(myToGoldenPatient)));
		assertEquals(1, getAllRedirectedGoldenPatients().size());
		assertEquals(1, getAllGoldenPatients().size());

		Patient fromSourcePatient = myPatientDao.read(myFromGoldenPatient.getIdElement().toUnqualifiedVersionless());

		assertFalse(MdmResourceUtil.isGoldenRecord(fromSourcePatient));
		assertTrue(MdmResourceUtil.isGoldenRecordRedirected(fromSourcePatient));

		//TODO GGG eventually this will need to check a redirect... this is a hack which doesnt work
		// Optional<Identifier> redirect = fromSourcePatient.getIdentifier().stream().filter(theIdentifier -> theIdentifier.getSystem().equals("REDIRECT")).findFirst();
		// assertThat(redirect.get().getValue(), is(equalTo(myToSourcePatient.getIdElement().toUnqualified().getValue())));

		List<MdmLink> links = myMdmLinkDaoSvc.findMdmLinksBySourceResource(myFromGoldenPatient);
		assertThat(links, hasSize(1));

		MdmLink link = links.get(0);
		assertEquals(link.getSourcePid(), myFromGoldenPatient.getIdElement().toUnqualifiedVersionless().getIdPartAsLong());
		assertEquals(link.getGoldenResourcePid(), myToGoldenPatient.getIdElement().toUnqualifiedVersionless().getIdPartAsLong());
		assertEquals(link.getMatchResult(), MdmMatchResultEnum.REDIRECT);
		assertEquals(link.getLinkSource(), MdmLinkSourceEnum.MANUAL);
	}

	@Test
	public void testMergeOnSamePartition() {
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionLookupSvc.createPartition(new PartitionEntity().setId(1).setName(PARTITION_1));
		RequestPartitionId requestPartitionId = RequestPartitionId.fromPartitionId(1);
		Patient fromGoldenPatient = createPatientOnPartition(new Patient(), true, false, requestPartitionId);
		StringType fromGoldenPatientId = new StringType(fromGoldenPatient.getIdElement().getValue());
		Patient toGoldenPatient = createPatientOnPartition(new Patient(), true, false, requestPartitionId);
		StringType toGoldenPatientId = new StringType(toGoldenPatient.getIdElement().getValue());

		Patient mergedSourcePatient = (Patient) myMdmProvider.mergeGoldenResources(fromGoldenPatientId,
			toGoldenPatientId, null, myRequestDetails);

		assertTrue(MdmResourceUtil.isGoldenRecord(mergedSourcePatient));
		assertFalse(MdmResourceUtil.isGoldenRecordRedirected(mergedSourcePatient));

		assertEquals(toGoldenPatient.getIdElement(), mergedSourcePatient.getIdElement());
		assertThat(mergedSourcePatient, is(sameGoldenResourceAs(toGoldenPatient)));
		assertEquals(1, getAllRedirectedGoldenPatients().size());
		// 2 from the set-up and only one from this test should be golden resource
		assertEquals(3, getAllGoldenPatients().size());

		List<MdmLink> links = myMdmLinkDaoSvc.findMdmLinksBySourceResource(fromGoldenPatient);
		assertThat(links, hasSize(1));

		MdmLink link = links.get(0);
		assertEquals(link.getSourcePid(), fromGoldenPatient.getIdElement().toUnqualifiedVersionless().getIdPartAsLong());
		assertEquals(link.getGoldenResourcePid(), toGoldenPatient.getIdElement().toUnqualifiedVersionless().getIdPartAsLong());
		assertEquals(link.getMatchResult(), MdmMatchResultEnum.REDIRECT);
		assertEquals(link.getLinkSource(), MdmLinkSourceEnum.MANUAL);
	}

	@Test
	public void testMergeOnDifferentPartitions() {
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionLookupSvc.createPartition(new PartitionEntity().setId(1).setName(PARTITION_1));
		RequestPartitionId requestPartitionId1 = RequestPartitionId.fromPartitionId(1);
		myPartitionLookupSvc.createPartition(new PartitionEntity().setId(2).setName(PARTITION_2));
		RequestPartitionId requestPartitionId2 = RequestPartitionId.fromPartitionId(2);
		Patient fromGoldenPatient = createPatientOnPartition(new Patient(), true, false, requestPartitionId1);
		StringType fromGoldenPatientId = new StringType(fromGoldenPatient.getIdElement().getValue());
		Patient toGoldenPatient = createPatientOnPartition(new Patient(), true, false, requestPartitionId2);
		StringType toGoldenPatientId = new StringType(toGoldenPatient.getIdElement().getValue());

		try {
			myMdmProvider.mergeGoldenResources(fromGoldenPatientId, toGoldenPatientId, null, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), endsWith("This operation is only available for resources on the same partition."));
		}
	}

	@Test
	public void testMergeWithManualOverride() {
		Patient patient = TerserUtil.clone(myFhirContext, myFromGoldenPatient);
		patient.setIdElement(null);

		Patient mergedSourcePatient = (Patient) myMdmProvider.mergeGoldenResources(myFromGoldenPatientId,
			myToGoldenPatientId, patient, myRequestDetails);

		assertEquals(myToGoldenPatient.getIdElement(), mergedSourcePatient.getIdElement());
		assertThat(mergedSourcePatient, is(sameGoldenResourceAs(myToGoldenPatient)));
		assertEquals(1, getAllRedirectedGoldenPatients().size());
		assertEquals(1, getAllGoldenPatients().size());

		Patient fromSourcePatient = myPatientDao.read(myFromGoldenPatient.getIdElement().toUnqualifiedVersionless());
		assertFalse(MdmResourceUtil.isGoldenRecord(fromSourcePatient));
		assertTrue(MdmResourceUtil.isGoldenRecordRedirected(fromSourcePatient));

		List<MdmLink> links = myMdmLinkDaoSvc.findMdmLinksBySourceResource(myFromGoldenPatient);
		assertThat(links, hasSize(1));

		MdmLink link = links.get(0);
		assertEquals(link.getSourcePid(), myFromGoldenPatient.getIdElement().toUnqualifiedVersionless().getIdPartAsLong());
		assertEquals(link.getGoldenResourcePid(), myToGoldenPatient.getIdElement().toUnqualifiedVersionless().getIdPartAsLong());
		assertEquals(link.getMatchResult(), MdmMatchResultEnum.REDIRECT);
		assertEquals(link.getLinkSource(), MdmLinkSourceEnum.MANUAL);
	}

	@Test
	public void testUnmanagedMerge() {
		StringType fromGoldenResourceId = new StringType(createPatient().getIdElement().getValue());
		StringType toGoldenResourceId = new StringType(createPatient().getIdElement().getValue());
		try {
			myMdmProvider.mergeGoldenResources(fromGoldenResourceId, toGoldenResourceId, null, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			String message = myMessageHelper.getMessageForFailedGoldenResourceLoad("fromGoldenResourceId", fromGoldenResourceId.getValue());
			assertEquals(e.getMessage(), Msg.code(1502) + message);
		}
	}

	@Test
	public void testNullParams() {
		try {
			myMdmProvider.mergeGoldenResources(null, null, null, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(1494) + "fromGoldenResourceId cannot be null", e.getMessage());
		}
		try {
			myMdmProvider.mergeGoldenResources(null, myToGoldenPatientId, null, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(1494) + "fromGoldenResourceId cannot be null", e.getMessage());
		}
		try {
			myMdmProvider.mergeGoldenResources(myFromGoldenPatientId, null, null, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(1494) + "toGoldenResourceId cannot be null", e.getMessage());
		}
	}

	@Test
	public void testBadParams() {
		try {
			myMdmProvider.mergeGoldenResources(new StringType("Patient/1"), new StringType("Patient/1"), null, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(1493) + "fromGoldenResourceId must be different from toGoldenResourceId", e.getMessage());
		}

		try {
			myMdmProvider.mergeGoldenResources(new StringType("Patient/abc"), myToGoldenPatientId, null, myRequestDetails);
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals(Msg.code(2001) + "Resource Patient/abc is not known", e.getMessage());
		}

		try {
			myMdmProvider.mergeGoldenResources(new StringType("Patient/abc"), myToGoldenPatientId, null, myRequestDetails);
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals(Msg.code(2001) + "Resource Patient/abc is not known", e.getMessage());
		}

		try {
			myMdmProvider.mergeGoldenResources(new StringType("Organization/abc"), myToGoldenPatientId, null, myRequestDetails);
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals(Msg.code(2001) + "Resource Organization/abc is not known", e.getMessage());
		}

		try {
			myMdmProvider.mergeGoldenResources(myFromGoldenPatientId, new StringType("Patient/abc"), null, myRequestDetails);
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals(Msg.code(2001) + "Resource Patient/abc is not known", e.getMessage());
		}
	}
}
