package ca.uhn.fhir.jpa.mdm.provider;

import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.util.MdmUtil;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class MdmProviderMergePersonsR4Test extends BaseProviderR4Test {

	private Patient myFromSourcePatient;
	private StringType myFromSourcePatientId;
	private Patient myToSourcePatient;
	private StringType myToSourcePatientId;

	@Override
	@BeforeEach
	public void before() {
		super.before();
		super.loadMdmSearchParameters();

		myFromSourcePatient = createGoldenPatient();
		myFromSourcePatientId = new StringType(myFromSourcePatient.getIdElement().getValue());
		myToSourcePatient = createGoldenPatient();
		myToSourcePatientId = new StringType(myToSourcePatient.getIdElement().getValue());
	}

	@Test
	public void testMerge() {
		Patient mergedSourcePatient = (Patient) myMdmProviderR4.mergeGoldenResources(myFromSourcePatientId,
			myToSourcePatientId, myRequestDetails);

		assertTrue(MdmUtil.isGoldenRecord(myFromSourcePatient));
		assertEquals(myToSourcePatient.getIdElement(), mergedSourcePatient.getIdElement());
		assertThat(mergedSourcePatient, is(sameSourceResourceAs(myToSourcePatient)));
		assertEquals(1, getAllRedirectedGoldenPatients().size());
		assertEquals(1, getAllGoldenPatients().size());

		Patient fromSourcePatient = myPatientDao.read(myFromSourcePatient.getIdElement().toUnqualifiedVersionless());
		assertThat(fromSourcePatient.getActive(), is(false));
		assertTrue(MdmUtil.isGoldenRecordRedirected(fromSourcePatient));

		//TODO GGG eventually this will need to check a redirect... this is a hack which doesnt work
		// Optional<Identifier> redirect = fromSourcePatient.getIdentifier().stream().filter(theIdentifier -> theIdentifier.getSystem().equals("REDIRECT")).findFirst();
		// assertThat(redirect.get().getValue(), is(equalTo(myToSourcePatient.getIdElement().toUnqualified().getValue())));

		List<MdmLink> links = myMdmLinkDaoSvc.findMdmLinksByTarget(myFromSourcePatient);
		assertThat(links, hasSize(1));

		MdmLink link = links.get(0);
		assertEquals(link.getTargetPid(), myFromSourcePatient.getIdElement().toUnqualifiedVersionless().getIdPartAsLong());
		assertEquals(link.getGoldenResourcePid(), myToSourcePatient.getIdElement().toUnqualifiedVersionless().getIdPartAsLong());
		assertEquals(link.getMatchResult(), MdmMatchResultEnum.REDIRECT);
		assertEquals(link.getLinkSource(), MdmLinkSourceEnum.MANUAL);
		// assertThat(links.get(0).getAssurance(), is (AssuranceLevelUtil.getAssuranceLevel(EmpiMatchResultEnum.REDIRECT, EmpiLinkSourceEnum.MANUAL).toR4()));
		//List<Person.PersonLinkComponent> links = fromSourcePatient.getLink();
		//assertThat(links, hasSize(1));
		//assertThat(links.get(0).getTarget().getReference(), is (myToSourcePatient.getIdElement().toUnqualifiedVersionless().getValue()));
		//assertThat(links.get(0).getAssurance(), is (AssuranceLevelUtil.getAssuranceLevel(EmpiMatchResultEnum.REDIRECT, EmpiLinkSourceEnum.MANUAL).toR4()));
	}

	@Test
	public void testUnmanagedMerge() {
		StringType fromPersonId = new StringType(createPatient().getIdElement().getValue());
		StringType toPersonId = new StringType(createPatient().getIdElement().getValue());
		try {
			myMdmProviderR4.mergeGoldenResources(fromPersonId, toPersonId, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Only MDM managed resources can be merged. MDM managed resources must have the HAPI-MDM tag.", e.getMessage());
		}
	}

//	INVALID ANYMORE - we support merging patients to patients now
//	@Test
//	public void testMergePatients() {
//		try {
//			StringType patientId = new StringType(createPatient().getIdElement().getValue());
//			StringType otherPatientId = new StringType(createPatient().getIdElement().getValue());
//			myEmpiProviderR4.mergeGoldenResources(patientId, otherPatientId, myRequestDetails);
//			fail();
//		} catch (InvalidRequestException e) {
//			assertThat(e.getMessage(), endsWith("must have form Person/<id> where <id> is the id of the person"));
//		}
//
//	}

	@Test
	public void testNullParams() {
		try {
			myMdmProviderR4.mergeGoldenResources(null, null, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("fromGoldenResourceId cannot be null", e.getMessage());
		}
		try {
			myMdmProviderR4.mergeGoldenResources(null, myToSourcePatientId, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("fromGoldenResourceId cannot be null", e.getMessage());
		}
		try {
			myMdmProviderR4.mergeGoldenResources(myFromSourcePatientId, null, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("toGoldenResourceId cannot be null", e.getMessage());
		}
	}

	@Test
	public void testBadParams() {
		print(myFromSourcePatient);
		print(myToSourcePatient);

//		TODO NG - THESE ARE NOW INVALID
//		try {
//			myEmpiProviderR4.mergeGoldenResources(new StringType("Patient/1"), new StringType("Patient/2"), myRequestDetails);
//			fail();
//		} catch (InvalidRequestException e) {
//			assertThat(e.getMessage(), endsWith(" must have form Person/<id> where <id> is the id of the person"));
//		}
//
//		try {
//			myEmpiProviderR4.mergeGoldenResources(myFromSourcePatientId, new StringType("Patient/2"), myRequestDetails);
//			fail();
//		} catch (InvalidRequestException e) {
//			assertThat(e.getMessage(), endsWith(" must have form Person/<id> where <id> is the id of the person"));
//		}

		try {
			myMdmProviderR4.mergeGoldenResources(new StringType("Person/1"), new StringType("Person/1"), myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("fromPersonId must be different from toPersonId", e.getMessage());
		}

		try {
			myMdmProviderR4.mergeGoldenResources(new StringType("Person/abc"), myToSourcePatientId, myRequestDetails);
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals("Resource Person/abc is not known", e.getMessage());
		}

		try {
			myMdmProviderR4.mergeGoldenResources(myFromSourcePatientId, new StringType("Person/abc"), myRequestDetails);
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals("Resource Person/abc is not known", e.getMessage());
		}
	}
}
