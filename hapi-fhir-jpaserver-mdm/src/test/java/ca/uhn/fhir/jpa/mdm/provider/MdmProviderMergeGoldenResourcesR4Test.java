package ca.uhn.fhir.jpa.mdm.provider;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

public class MdmProviderMergeGoldenResourcesR4Test extends BaseProviderR4Test {

	private Patient myFromGoldenPatient;
	private StringType myFromGoldenPatientId;
	private Patient myToGoldenPatient;
	private StringType myToGoldenPatientId;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();

		myFromGoldenPatient = createGoldenPatient();
		myFromGoldenPatientId = new StringType(myFromGoldenPatient.getIdElement().getValue());
		myToGoldenPatient = createGoldenPatient();
		myToGoldenPatientId = new StringType(myToGoldenPatient.getIdElement().getValue());
	}

	@Override
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

		assertThat(mergedSourcePatient.getName().size()).isEqualTo(myFromGoldenPatient.getName().size());
		assertThat(mergedSourcePatient.getName().get(0).getNameAsSingleString()).isEqualTo(myFromGoldenPatient.getName().get(0).getNameAsSingleString());
		assertThat(mergedSourcePatient.getCommunication().size()).isEqualTo(myFromGoldenPatient.getCommunication().size());
		assertThat(mergedSourcePatient.getExtension().size()).isEqualTo(myFromGoldenPatient.getExtension().size());

		Patient toGoldenPatient = myPatientDao.read(myToGoldenPatient.getIdElement().toUnqualifiedVersionless());
		assertThat(mergedSourcePatient.getName().size()).isEqualTo(toGoldenPatient.getName().size());
		assertThat(mergedSourcePatient.getName().get(0).getNameAsSingleString()).isEqualTo(toGoldenPatient.getName().get(0).getNameAsSingleString());
	}


	@Test
	public void testMerge() {
		Patient mergedSourcePatient = (Patient) myMdmProvider.mergeGoldenResources(
			myFromGoldenPatientId, // from
			myToGoldenPatientId, // to
			null, myRequestDetails);

		// we do not check setActive anymore - as not all types support that
		assertThat(MdmResourceUtil.isGoldenRecord(mergedSourcePatient)).isTrue();
		assertThat(MdmResourceUtil.isGoldenRecordRedirected(mergedSourcePatient)).isFalse();

		assertThat(mergedSourcePatient.getIdElement()).isEqualTo(myToGoldenPatient.getIdElement());
		assertThat(mergedSourcePatient, sameGoldenResourceAs(myToGoldenPatient));
		assertThat(getAllRedirectedGoldenPatients().size()).isEqualTo(1);
		assertThat(getAllGoldenPatients().size()).isEqualTo(1);

		Patient fromSourcePatient = myPatientDao.read(myFromGoldenPatient.getIdElement().toUnqualifiedVersionless());

		assertThat(MdmResourceUtil.isGoldenRecord(fromSourcePatient)).isFalse();
		assertThat(MdmResourceUtil.isGoldenRecordRedirected(fromSourcePatient)).isTrue();

		//TODO GGG eventually this will need to check a redirect... this is a hack which doesnt work
		// Optional<Identifier> redirect = fromSourcePatient.getIdentifier().stream().filter(theIdentifier -> theIdentifier.getSystem().equals("REDIRECT")).findFirst();
		// assertThat(redirect.get().getValue(), is(equalTo(myToSourcePatient.getIdElement().toUnqualified().getValue())));

		List<MdmLink> links = (List<MdmLink>) myMdmLinkDaoSvc.findMdmLinksBySourceResource(myToGoldenPatient);
		assertThat(links).hasSize(1);

		MdmLink link = links.get(0);
		assertThat(myToGoldenPatient.getIdElement().toUnqualifiedVersionless().getIdPartAsLong()).isEqualTo(link.getSourcePid());
		assertThat(myFromGoldenPatient.getIdElement().toUnqualifiedVersionless().getIdPartAsLong()).isEqualTo(link.getGoldenResourcePid());
		assertThat(MdmMatchResultEnum.REDIRECT).isEqualTo(link.getMatchResult());
		assertThat(MdmLinkSourceEnum.MANUAL).isEqualTo(link.getLinkSource());
	}

	@Test
	public void testMergeOnSamePartition() {
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionLookupSvc.createPartition(new PartitionEntity().setId(1).setName(PARTITION_1), null);
		RequestPartitionId requestPartitionId = RequestPartitionId.fromPartitionId(1);
		Patient fromGoldenPatient = createPatientOnPartition(new Patient(), true, false, requestPartitionId);
		StringType fromGoldenPatientId = new StringType(fromGoldenPatient.getIdElement().getValue());
		Patient toGoldenPatient = createPatientOnPartition(new Patient(), true, false, requestPartitionId);
		StringType toGoldenPatientId = new StringType(toGoldenPatient.getIdElement().getValue());

		// test
		Patient mergedSourcePatient = (Patient) myMdmProvider.mergeGoldenResources(
			fromGoldenPatientId,
			toGoldenPatientId,
			null, myRequestDetails);

		assertThat(MdmResourceUtil.isGoldenRecord(mergedSourcePatient)).isTrue();
		assertThat(MdmResourceUtil.isGoldenRecordRedirected(mergedSourcePatient)).isFalse();

		assertThat(mergedSourcePatient.getIdElement()).isEqualTo(toGoldenPatient.getIdElement());
		assertThat(mergedSourcePatient, sameGoldenResourceAs(toGoldenPatient));
		assertThat(getAllRedirectedGoldenPatients().size()).isEqualTo(1);
		// 2 from the set-up and only one from this test should be golden resource
		assertThat(getAllGoldenPatients().size()).isEqualTo(3);

		List<MdmLink> links = (List<MdmLink>) myMdmLinkDaoSvc.findMdmLinksBySourceResource(toGoldenPatient);
		assertThat(links).hasSize(1);

		MdmLink link = links.get(0);
		assertThat(toGoldenPatient.getIdElement().toUnqualifiedVersionless().getIdPartAsLong()).isEqualTo(link.getSourcePid());
		assertThat(fromGoldenPatient.getIdElement().toUnqualifiedVersionless().getIdPartAsLong()).isEqualTo(link.getGoldenResourcePid());
		assertThat(MdmMatchResultEnum.REDIRECT).isEqualTo(link.getMatchResult());
		assertThat(MdmLinkSourceEnum.MANUAL).isEqualTo(link.getLinkSource());
	}

	@Test
	public void testMergeOnDifferentPartitions() {
		myPartitionSettings.setPartitioningEnabled(true);
		myMdmSettings.setSearchAllPartitionForMatch(false);
		myPartitionLookupSvc.createPartition(new PartitionEntity().setId(1).setName(PARTITION_1), null);
		RequestPartitionId requestPartitionId1 = RequestPartitionId.fromPartitionId(1);
		myPartitionLookupSvc.createPartition(new PartitionEntity().setId(2).setName(PARTITION_2), null);
		RequestPartitionId requestPartitionId2 = RequestPartitionId.fromPartitionId(2);
		Patient fromGoldenPatient = createPatientOnPartition(new Patient(), true, false, requestPartitionId1);
		StringType fromGoldenPatientId = new StringType(fromGoldenPatient.getIdElement().getValue());
		Patient toGoldenPatient = createPatientOnPartition(new Patient(), true, false, requestPartitionId2);
		StringType toGoldenPatientId = new StringType(toGoldenPatient.getIdElement().getValue());

		try {
			myMdmProvider.mergeGoldenResources(fromGoldenPatientId, toGoldenPatientId, null, myRequestDetails);
			fail("");
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).endsWith("This operation is only available for resources on the same partition.");
		}
	}

	@Test
	public void testMergeWithManualOverride() {
		Patient patient = TerserUtil.clone(myFhirContext, myFromGoldenPatient);
		patient.setIdElement(null);

		Patient mergedSourcePatient = (Patient) myMdmProvider.mergeGoldenResources(
			myFromGoldenPatientId, // from
			myToGoldenPatientId, // to
			patient, myRequestDetails);

		assertThat(mergedSourcePatient.getIdElement()).isEqualTo(myToGoldenPatient.getIdElement());
		assertThat(mergedSourcePatient, sameGoldenResourceAs(myToGoldenPatient));
		assertThat(getAllRedirectedGoldenPatients().size()).isEqualTo(1);
		assertThat(getAllGoldenPatients().size()).isEqualTo(1);

		Patient fromSourcePatient = myPatientDao.read(myFromGoldenPatient.getIdElement().toUnqualifiedVersionless());
		assertThat(MdmResourceUtil.isGoldenRecord(fromSourcePatient)).isFalse();
		assertThat(MdmResourceUtil.isGoldenRecordRedirected(fromSourcePatient)).isTrue();

		List<MdmLink> links = (List<MdmLink>) myMdmLinkDaoSvc.findMdmLinksBySourceResource(myToGoldenPatient);
		assertThat(links).hasSize(1);

		MdmLink link = links.get(0);
		assertThat(myToGoldenPatient.getIdElement().toUnqualifiedVersionless().getIdPartAsLong()).isEqualTo(link.getSourcePid());
		assertThat(myFromGoldenPatient.getIdElement().toUnqualifiedVersionless().getIdPartAsLong()).isEqualTo(link.getGoldenResourcePid());
		assertThat(MdmMatchResultEnum.REDIRECT).isEqualTo(link.getMatchResult());
		assertThat(MdmLinkSourceEnum.MANUAL).isEqualTo(link.getLinkSource());
	}

	@Test
	public void testUnmanagedMerge() {
		StringType fromGoldenResourceId = new StringType(createPatient().getIdElement().getValue());
		StringType toGoldenResourceId = new StringType(createPatient().getIdElement().getValue());
		try {
			myMdmProvider.mergeGoldenResources(fromGoldenResourceId, toGoldenResourceId, null, myRequestDetails);
			fail("");
		} catch (InvalidRequestException e) {
			String message = myMessageHelper.getMessageForFailedGoldenResourceLoad("fromGoldenResourceId", fromGoldenResourceId.getValue());
			assertThat(Msg.code(1502) + message).isEqualTo(e.getMessage());
		}
	}

	@Test
	public void testNullParams() {
		try {
			myMdmProvider.mergeGoldenResources(null, null, null, myRequestDetails);
			fail("");
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1494) + "fromGoldenResourceId cannot be null");
		}
		try {
			myMdmProvider.mergeGoldenResources(null, myToGoldenPatientId, null, myRequestDetails);
			fail("");
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1494) + "fromGoldenResourceId cannot be null");
		}
		try {
			myMdmProvider.mergeGoldenResources(myFromGoldenPatientId, null, null, myRequestDetails);
			fail("");
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1494) + "toGoldenResourceId cannot be null");
		}
	}

	@Test
	public void testBadParams() {
		try {
			myMdmProvider.mergeGoldenResources(new StringType("Patient/1"), new StringType("Patient/1"), null, myRequestDetails);
			fail("");
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1493) + "fromGoldenResourceId must be different from toGoldenResourceId");
		}

		try {
			myMdmProvider.mergeGoldenResources(new StringType("Patient/abc"), myToGoldenPatientId, null, myRequestDetails);
			fail("");
		} catch (ResourceNotFoundException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(2001) + "Resource Patient/abc is not known");
		}

		try {
			myMdmProvider.mergeGoldenResources(new StringType("Patient/abc"), myToGoldenPatientId, null, myRequestDetails);
			fail("");
		} catch (ResourceNotFoundException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(2001) + "Resource Patient/abc is not known");
		}

		try {
			myMdmProvider.mergeGoldenResources(new StringType("Organization/abc"), myToGoldenPatientId, null, myRequestDetails);
			fail("");
		} catch (ResourceNotFoundException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(2001) + "Resource Organization/abc is not known");
		}

		try {
			myMdmProvider.mergeGoldenResources(myFromGoldenPatientId, new StringType("Patient/abc"), null, myRequestDetails);
			fail("");
		} catch (ResourceNotFoundException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(2001) + "Resource Patient/abc is not known");
		}
	}
}
