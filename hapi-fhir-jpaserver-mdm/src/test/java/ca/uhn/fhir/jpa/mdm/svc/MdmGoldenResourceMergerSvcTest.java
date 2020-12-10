package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchOutcome;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.api.IGoldenResourceMergerSvc;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.mdm.helper.MdmLinkHelper;
import ca.uhn.fhir.jpa.mdm.interceptor.IMdmStorageInterceptor;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.rest.server.TransactionLogMessages;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class MdmGoldenResourceMergerSvcTest extends BaseMdmR4Test {

	public static final String GIVEN_NAME = "Jenn";
	public static final String FAMILY_NAME = "Chan";
	public static final String POSTAL_CODE = "M6G 1B4";
	private static final String BAD_GIVEN_NAME = "Bob";
	private static final MdmMatchOutcome POSSIBLE_MATCH = new MdmMatchOutcome(null, null).setMatchResultEnum(MdmMatchResultEnum.POSSIBLE_MATCH);

	@Autowired
	IGoldenResourceMergerSvc myGoldenResourceMergerSvc;
	@Autowired
	MdmLinkHelper myMdmLinkHelper;
	@Autowired
	IMdmStorageInterceptor myMdmStorageInterceptor;
	@Autowired
	IInterceptorService myInterceptorService;

	private Patient myFromGoldenPatient;
	private Patient myToGoldenPatient;
	private Long myFromGoldenPatientPid;
	private Long myToGoldenPatientPid;
	private Patient myTargetPatient1;
	private Patient myTargetPatient2;
	private Patient myTargetPatient3;

	@BeforeEach
	public void before() {
		myFromGoldenPatient = createGoldenPatient();
		IdType fromSourcePatientId = myFromGoldenPatient.getIdElement().toUnqualifiedVersionless();
		myFromGoldenPatientPid = myIdHelperService.getPidOrThrowException(fromSourcePatientId);
		myToGoldenPatient = createGoldenPatient();
		IdType toGoldenPatientId = myToGoldenPatient.getIdElement().toUnqualifiedVersionless();
		myToGoldenPatientPid = myIdHelperService.getPidOrThrowException(toGoldenPatientId);

		myTargetPatient1 = createPatient();
		myTargetPatient2 = createPatient();
		myTargetPatient3 = createPatient();

		// Register the mdm storage interceptor after the creates so the delete hook is fired when we merge
		myInterceptorService.registerInterceptor(myMdmStorageInterceptor);
	}

	@Override
	@AfterEach
	public void after() throws IOException {
		myInterceptorService.unregisterInterceptor(myMdmStorageInterceptor);
		super.after();
	}
	
	@Test
	public void emptyMerge() {
		assertEquals(2, getAllGoldenPatients().size());
		assertEquals(0, getAllRedirectedGoldenPatients().size());

		Patient mergedGoldenPatient = mergeGoldenPatients();

		assertEquals(myToGoldenPatient.getIdElement(), mergedGoldenPatient.getIdElement());
		assertThat(mergedGoldenPatient, is(sameGoldenResourceAs(mergedGoldenPatient)));
		assertEquals(1, getAllGoldenPatients().size());
		assertEquals(1, getAllRedirectedGoldenPatients().size());
	}

	private Patient mergeGoldenPatients() {
		assertEquals(0, redirectLinkCount());
		Patient retval = (Patient) myGoldenResourceMergerSvc.mergeGoldenResources(myFromGoldenPatient, myToGoldenPatient, createMdmContext());
		assertEquals(1, redirectLinkCount());
		return retval;
	}

	private int redirectLinkCount() {
		MdmLink mdmLink = new MdmLink().setMatchResult(MdmMatchResultEnum.REDIRECT);
		Example<MdmLink> example = Example.of(mdmLink);
		return myMdmLinkDao.findAll(example).size();
	}

	private MdmTransactionContext createMdmContext() {
		MdmTransactionContext mdmTransactionContext = new MdmTransactionContext(TransactionLogMessages.createFromTransactionGuid(UUID.randomUUID().toString()), MdmTransactionContext.OperationType.MERGE_GOLDEN_RESOURCES);
		mdmTransactionContext.setResourceType("Patient");
		return mdmTransactionContext;
	}

	@Test
	public void mergeRemovesPossibleDuplicatesLink() {
		MdmLink mdmLink = myMdmLinkDaoSvc.newMdmLink()
			.setGoldenResourcePid(myToGoldenPatientPid)
			.setSourcePid(myFromGoldenPatientPid)
			.setMdmSourceType("Patient")
			.setMatchResult(MdmMatchResultEnum.POSSIBLE_DUPLICATE)
			.setLinkSource(MdmLinkSourceEnum.AUTO);

		saveLink(mdmLink);

		{
			List<MdmLink> foundLinks = myMdmLinkDao.findAll();
			assertEquals(1, foundLinks.size());
			assertEquals(MdmMatchResultEnum.POSSIBLE_DUPLICATE, foundLinks.get(0).getMatchResult());
		}

		myMdmLinkHelper.logMdmLinks();

		mergeGoldenPatients();

		{
			List<MdmLink> foundLinks = myMdmLinkDao.findAll();
			assertEquals(1, foundLinks.size());
			assertEquals(MdmMatchResultEnum.REDIRECT, foundLinks.get(0).getMatchResult());
		}
	}

	@Test
	public void fullFromEmptyTo() {
		populatePatient(myFromGoldenPatient);

		Patient mergedSourcePatient = mergeGoldenPatients();
		// TODO NG - Revisit when rules are ready
//		HumanName returnedName = mergedSourcePatient.getNameFirstRep();
//		assertEquals(GIVEN_NAME, returnedName.getGivenAsSingleString());
//		assertEquals(FAMILY_NAME, returnedName.getFamily());
//		assertEquals(POSTAL_CODE, mergedSourcePatient.getAddressFirstRep().getPostalCode());
	}

	@Test
	public void emptyFromFullTo() {
		myFromGoldenPatient.getName().add(new HumanName().addGiven(BAD_GIVEN_NAME));
		populatePatient(myToGoldenPatient);

		Patient mergedSourcePatient = mergeGoldenPatients();
		HumanName returnedName = mergedSourcePatient.getNameFirstRep();
		assertEquals(GIVEN_NAME, returnedName.getGivenAsSingleString());
		assertEquals(FAMILY_NAME, returnedName.getFamily());
		assertEquals(POSTAL_CODE, mergedSourcePatient.getAddressFirstRep().getPostalCode());
	}

	@Test
	public void fromLinkToNoLink() {
		createMdmLink(myFromGoldenPatient, myTargetPatient1);

		Patient mergedGoldenPatient = mergeGoldenPatients();
		List<MdmLink> links = getNonRedirectLinksByGoldenResource(mergedGoldenPatient);
		assertEquals(1, links.size());
		assertThat(mergedGoldenPatient, is(possibleLinkedTo(myTargetPatient1)));
	}

	@Test
	public void fromNoLinkToLink() {
		createMdmLink(myToGoldenPatient, myTargetPatient1);

		Patient mergedSourcePatient = mergeGoldenPatients();
		List<MdmLink> links = getNonRedirectLinksByGoldenResource(mergedSourcePatient);
		assertEquals(1, links.size());
		assertThat(mergedSourcePatient, is(possibleLinkedTo(myTargetPatient1)));
	}

	@Test
	public void fromManualLinkOverridesAutoToLink() {
		MdmLink fromLink = createMdmLink(myFromGoldenPatient, myTargetPatient1);
		fromLink.setLinkSource(MdmLinkSourceEnum.MANUAL);
		fromLink.setMatchResult(MdmMatchResultEnum.MATCH);
		saveLink(fromLink);

		createMdmLink(myToGoldenPatient, myTargetPatient1);

		mergeGoldenPatients();
		List<MdmLink> links = getNonRedirectLinksByGoldenResource(myToGoldenPatient);
		assertEquals(1, links.size());
		assertEquals(MdmLinkSourceEnum.MANUAL, links.get(0).getLinkSource());
	}

	private List<MdmLink> getNonRedirectLinksByGoldenResource(Patient theGoldenPatient) {
		return myMdmLinkDaoSvc.findMdmLinksByGoldenResource(theGoldenPatient).stream()
			.filter(link -> !link.isRedirect())
			.collect(Collectors.toList());
	}

	@Test
	public void fromManualNoMatchLinkOverridesAutoToLink() {
		MdmLink fromLink = createMdmLink(myFromGoldenPatient, myTargetPatient1);
		fromLink.setLinkSource(MdmLinkSourceEnum.MANUAL);
		fromLink.setMatchResult(MdmMatchResultEnum.NO_MATCH);

		saveLink(fromLink);

		createMdmLink(myToGoldenPatient, myTargetPatient1);

		mergeGoldenPatients();
		List<MdmLink> links = getNonRedirectLinksByGoldenResource(myToGoldenPatient);
		assertEquals(1, links.size());
		assertEquals(MdmLinkSourceEnum.MANUAL, links.get(0).getLinkSource());
		assertEquals(MdmMatchResultEnum.NO_MATCH, links.get(0).getMatchResult());
	}

	@Test
	public void fromManualAutoMatchLinkNoOverridesManualToLink() {
		createMdmLink(myFromGoldenPatient, myTargetPatient1);

		MdmLink toLink = createMdmLink(myToGoldenPatient, myTargetPatient1);
		toLink.setLinkSource(MdmLinkSourceEnum.MANUAL);
		toLink.setMatchResult(MdmMatchResultEnum.NO_MATCH);
		saveLink(toLink);

		mergeGoldenPatients();
		List<MdmLink> links = getNonRedirectLinksByGoldenResource(myToGoldenPatient);
		assertEquals(1, links.size());
		assertEquals(MdmLinkSourceEnum.MANUAL, links.get(0).getLinkSource());
		assertEquals(MdmMatchResultEnum.NO_MATCH, links.get(0).getMatchResult());
	}

	@Test
	public void fromNoMatchMergeToManualMatchIsError() {
		MdmLink fromLink = createMdmLink(myFromGoldenPatient, myTargetPatient1);
		fromLink.setLinkSource(MdmLinkSourceEnum.MANUAL);
		fromLink.setMatchResult(MdmMatchResultEnum.NO_MATCH);
		saveLink(fromLink);

		MdmLink toLink = createMdmLink(myToGoldenPatient, myTargetPatient1);
		toLink.setLinkSource(MdmLinkSourceEnum.MANUAL);
		toLink.setMatchResult(MdmMatchResultEnum.MATCH);
		saveLink(toLink);

		try {
			mergeGoldenPatients();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("A MANUAL NO_MATCH link may not be merged into a MANUAL MATCH link for the same target", e.getMessage());
		}
	}

	@Test
	public void fromMatchMergeToManualNoMatchIsError() {
		MdmLink fromLink = createMdmLink(myFromGoldenPatient, myTargetPatient1);
		fromLink.setLinkSource(MdmLinkSourceEnum.MANUAL);
		fromLink.setMatchResult(MdmMatchResultEnum.MATCH);
		saveLink(fromLink);

		MdmLink toLink = createMdmLink(myToGoldenPatient, myTargetPatient1);
		toLink.setLinkSource(MdmLinkSourceEnum.MANUAL);
		toLink.setMatchResult(MdmMatchResultEnum.NO_MATCH);
		saveLink(toLink);

		try {
			mergeGoldenPatients();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("A MANUAL MATCH link may not be merged into a MANUAL NO_MATCH link for the same target", e.getMessage());
		}
	}

	@Test
	public void fromNoMatchMergeToManualMatchDifferentPatientIsOk() {
		MdmLink fromLink = createMdmLink(myFromGoldenPatient, myTargetPatient1);
		fromLink.setLinkSource(MdmLinkSourceEnum.MANUAL);
		fromLink.setMatchResult(MdmMatchResultEnum.NO_MATCH);
		saveLink(fromLink);

		MdmLink toLink = createMdmLink(myToGoldenPatient, myTargetPatient2);
		toLink.setLinkSource(MdmLinkSourceEnum.MANUAL);
		toLink.setMatchResult(MdmMatchResultEnum.MATCH);
		saveLink(toLink);

		mergeGoldenPatients();

		assertResourceHasLinkCount(myToGoldenPatient, 3);
		assertResourceHasLinkCount(myFromGoldenPatient, 0);
		// TODO ENSURE PROPER LINK TYPES
		assertEquals(3, myMdmLinkDao.count());
	}

	@Test
	public void from123To1() {
		createMdmLink(myFromGoldenPatient, myTargetPatient1);
		createMdmLink(myFromGoldenPatient, myTargetPatient2);
		createMdmLink(myFromGoldenPatient, myTargetPatient3);
		createMdmLink(myToGoldenPatient, myTargetPatient1);

		mergeGoldenPatients();
		myMdmLinkHelper.logMdmLinks();

		assertThat(myToGoldenPatient, is(possibleLinkedTo(myTargetPatient1, myTargetPatient2, myTargetPatient3)));
		assertResourceHasAutoLinkCount(myToGoldenPatient, 3);
	}


	private void assertResourceHasLinkCount(IBaseResource theResource, int theCount) {
		List<MdmLink> links = myMdmLinkDaoSvc.findMdmLinksByGoldenResource(theResource);
		assertEquals(theCount, links.size());
	}

	@Test
	public void from1To123() {
		createMdmLink(myFromGoldenPatient, myTargetPatient1);
		createMdmLink(myToGoldenPatient, myTargetPatient1);
		createMdmLink(myToGoldenPatient, myTargetPatient2);
		createMdmLink(myToGoldenPatient, myTargetPatient3);

		mergeGoldenPatients();
		myMdmLinkHelper.logMdmLinks();

		assertThat(myToGoldenPatient, is(possibleLinkedTo(myTargetPatient1, myTargetPatient2, myTargetPatient3)));
		assertResourceHasAutoLinkCount(myToGoldenPatient, 3);
	}

	private void assertResourceHasAutoLinkCount(Patient myToGoldenPatient, int theCount) {
		List<MdmLink> links = myMdmLinkDaoSvc.findMdmLinksByGoldenResource(myToGoldenPatient);
		assertEquals(theCount, links.stream().filter(MdmLink::isAuto).count());
	}

	@Test
	public void from123To123() {
		createMdmLink(myFromGoldenPatient, myTargetPatient1);
		createMdmLink(myFromGoldenPatient, myTargetPatient2);
		createMdmLink(myFromGoldenPatient, myTargetPatient3);
		createMdmLink(myToGoldenPatient, myTargetPatient1);
		createMdmLink(myToGoldenPatient, myTargetPatient2);
		createMdmLink(myToGoldenPatient, myTargetPatient3);

		mergeGoldenPatients();

		assertThat(myToGoldenPatient, is(possibleLinkedTo(myTargetPatient1, myTargetPatient2, myTargetPatient3)));

		assertResourceHasAutoLinkCount(myToGoldenPatient, 3);
	}

	@Test
	public void from12To23() {
		createMdmLink(myFromGoldenPatient, myTargetPatient1);
		createMdmLink(myFromGoldenPatient, myTargetPatient2);
		createMdmLink(myToGoldenPatient, myTargetPatient2);
		createMdmLink(myToGoldenPatient, myTargetPatient3);

		mergeGoldenPatients();
		myMdmLinkHelper.logMdmLinks();

		assertThat(myToGoldenPatient, is(possibleLinkedTo(myTargetPatient1, myTargetPatient2, myTargetPatient3)));

		assertResourceHasAutoLinkCount(myToGoldenPatient, 3);
	}

	@Test
	public void testMergeNames() {
		// TODO NG - Revisit when rules are available
//		myFromSourcePatient.addName().addGiven("Jim");
//		myFromSourcePatient.getNameFirstRep().addGiven("George");
//		assertThat(myFromSourcePatient.getName(), hasSize(1));
//		assertThat(myFromSourcePatient.getName().get(0).getGiven(), hasSize(2));
//
//		myToSourcePatient.addName().addGiven("Jeff");
//		myToSourcePatient.getNameFirstRep().addGiven("George");
//		assertThat(myToSourcePatient.getName(), hasSize(1));
//		assertThat(myToSourcePatient.getName().get(0).getGiven(), hasSize(2));
//
//		Patient mergedSourcePatient = mergeSourcePatients();
//		assertThat(mergedSourcePatient.getName(), hasSize(2));
//		assertThat(mergedSourcePatient.getName().get(0).getGiven(), hasSize(2));
//		assertThat(mergedSourcePatient.getName().get(1).getGiven(), hasSize(2));
	}

	@Test
	public void testMergeNamesAllSame() {
		// TODO NG - Revisit when rules are available
//		myFromSourcePatient.addName().addGiven("Jim");
//		myFromSourcePatient.getNameFirstRep().addGiven("George");
//		assertThat(myFromSourcePatient.getName(), hasSize(1));
//		assertThat(myFromSourcePatient.getName().get(0).getGiven(), hasSize(2));
//
//		myToSourcePatient.addName().addGiven("Jim");
//		myToSourcePatient.getNameFirstRep().addGiven("George");
//		assertThat(myToSourcePatient.getName(), hasSize(1));
//		assertThat(myToSourcePatient.getName().get(0).getGiven(), hasSize(2));
//
//		mergeSourcePatients();
//		assertThat(myToSourcePatient.getName(), hasSize(1));
//		assertThat(myToSourcePatient.getName().get(0).getGiven(), hasSize(2));
	}

	@Test
	public void testMergeIdentifiers() {
		myFromGoldenPatient.addIdentifier().setValue("aaa").setSystem("SYSTEM1");
		myFromGoldenPatient.addIdentifier().setValue("bbb").setSystem("SYSTEM1");
		myFromGoldenPatient.addIdentifier().setValue("ccc").setSystem("SYSTEM2");
		assertThat(myFromGoldenPatient.getIdentifier(), hasSize(3));

		myToGoldenPatient.addIdentifier().setValue("aaa").setSystem("SYSTEM1");
		myToGoldenPatient.addIdentifier().setValue("ccc").setSystem("SYSTEM1");
		assertThat(myToGoldenPatient.getIdentifier(), hasSize(2));

		mergeGoldenPatients();

		assertThat(myToGoldenPatient.getIdentifier(), hasSize(4));
	}

	private MdmLink createMdmLink(Patient theSourcePatient, Patient theTargetPatient) {
		//TODO GGG Ensure theis comment can be safely removed
		//theSourcePatient.addLink().setTarget(new Reference(theTargetPatient));
		return myMdmLinkDaoSvc.createOrUpdateLinkEntity(theSourcePatient, theTargetPatient, POSSIBLE_MATCH, MdmLinkSourceEnum.AUTO, createContextForCreate("Patient"));
	}

	private void populatePatient(Patient theSourcePatient) {
		theSourcePatient.addName(new HumanName().addGiven(GIVEN_NAME).setFamily(FAMILY_NAME));
		theSourcePatient.setGender(Enumerations.AdministrativeGender.FEMALE);
		theSourcePatient.setBirthDateElement(new DateType("1981-01-01"));
		Address address = new Address();
		address.addLine("622 College St");
		address.addLine("Suite 401");
		address.setDistrict("Little Italy");
		address.setCity("Toronto");
		address.setCountry("Canada");
		address.setPostalCode(POSTAL_CODE);
		theSourcePatient.setAddress(Collections.singletonList(address));
	}
}
