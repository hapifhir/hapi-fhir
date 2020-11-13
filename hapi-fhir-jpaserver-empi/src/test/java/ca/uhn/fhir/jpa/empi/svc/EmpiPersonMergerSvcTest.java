package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.api.EmpiMatchOutcome;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.api.IGoldenResourceMergerSvc;
import ca.uhn.fhir.empi.model.MdmTransactionContext;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import ca.uhn.fhir.jpa.empi.helper.EmpiLinkHelper;
import ca.uhn.fhir.jpa.empi.interceptor.IEmpiStorageInterceptor;
import ca.uhn.fhir.jpa.entity.EmpiLink;
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

public class EmpiPersonMergerSvcTest extends BaseEmpiR4Test {
	public static final String GIVEN_NAME = "Jenn";
	public static final String FAMILY_NAME = "Chan";
	public static final String POSTAL_CODE = "M6G 1B4";
	private static final String BAD_GIVEN_NAME = "Bob";
	private static final EmpiMatchOutcome POSSIBLE_MATCH = new EmpiMatchOutcome(null, null).setMatchResultEnum(EmpiMatchResultEnum.POSSIBLE_MATCH);

	@Autowired
	IGoldenResourceMergerSvc myEmpiPersonMergerSvc;
	@Autowired
	EmpiLinkHelper myEmpiLinkHelper;
	@Autowired
	IEmpiStorageInterceptor myEmpiStorageInterceptor;
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
		super.loadEmpiSearchParameters();

		myFromGoldenPatient = createGoldenPatient();
		IdType fromSourcePatientId = myFromGoldenPatient.getIdElement().toUnqualifiedVersionless();
		myFromGoldenPatientPid = myIdHelperService.getPidOrThrowException(fromSourcePatientId);
		myToGoldenPatient = createGoldenPatient();
		IdType toGoldenPatientId = myToGoldenPatient.getIdElement().toUnqualifiedVersionless();
		myToGoldenPatientPid = myIdHelperService.getPidOrThrowException(toGoldenPatientId);

		myTargetPatient1 = createPatient();
		myTargetPatient2 = createPatient();
		myTargetPatient3 = createPatient();

		// Register the empi storage interceptor after the creates so the delete hook is fired when we merge
		myInterceptorService.registerInterceptor(myEmpiStorageInterceptor);
	}

	@Override
	@AfterEach
	public void after() throws IOException {
		myInterceptorService.unregisterInterceptor(myEmpiStorageInterceptor);
		super.after();
	}
	
	@Test
	public void emptyMerge() {
		assertEquals(2, getAllGoldenPatients().size());
		assertEquals(0, getAllRedirectedGoldenPatients().size());

		Patient mergedGoldenPatient = mergeGoldenPatients();

		assertEquals(myToGoldenPatient.getIdElement(), mergedGoldenPatient.getIdElement());
		assertThat(mergedGoldenPatient, is(sameSourceResourceAs(mergedGoldenPatient)));
		assertEquals(1, getAllGoldenPatients().size());
		assertEquals(1, getAllRedirectedGoldenPatients().size());
	}

	private Patient mergeGoldenPatients() {
		assertEquals(0, redirectLinkCount());
		Patient retval = (Patient) myEmpiPersonMergerSvc.mergeGoldenResources(myFromGoldenPatient, myToGoldenPatient, createEmpiContext());
		assertEquals(1, redirectLinkCount());
		return retval;
	}

	private int redirectLinkCount() {
		EmpiLink empiLink = new EmpiLink().setMatchResult(EmpiMatchResultEnum.REDIRECT);
		Example<EmpiLink> example = Example.of(empiLink);
		return myEmpiLinkDao.findAll(example).size();
	}

	private MdmTransactionContext createEmpiContext() {
		MdmTransactionContext mdmTransactionContext = new MdmTransactionContext(TransactionLogMessages.createFromTransactionGuid(UUID.randomUUID().toString()), MdmTransactionContext.OperationType.MERGE_PERSONS);
		mdmTransactionContext.setResourceType("Patient");
		return mdmTransactionContext;
	}

	@Test
	public void mergeRemovesPossibleDuplicatesLink() {
		EmpiLink empiLink = myEmpiLinkDaoSvc.newEmpiLink()
			.setGoldenResourcePid(myToGoldenPatientPid)
			.setTargetPid(myFromGoldenPatientPid)
			.setEmpiTargetType("Patient")
			.setMatchResult(EmpiMatchResultEnum.POSSIBLE_DUPLICATE)
			.setLinkSource(EmpiLinkSourceEnum.AUTO);

		saveLink(empiLink);

		{
			List<EmpiLink> foundLinks = myEmpiLinkDao.findAll();
			assertEquals(1, foundLinks.size());
			assertEquals(EmpiMatchResultEnum.POSSIBLE_DUPLICATE, foundLinks.get(0).getMatchResult());
		}

		myEmpiLinkHelper.logEmpiLinks();

		mergeGoldenPatients();

		{
			List<EmpiLink> foundLinks = myEmpiLinkDao.findAll();
			assertEquals(1, foundLinks.size());
			assertEquals(EmpiMatchResultEnum.REDIRECT, foundLinks.get(0).getMatchResult());
		}
	}

	@Test
	public void fullFromEmptyTo() {
		populatePerson(myFromGoldenPatient);

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
		populatePerson(myToGoldenPatient);

		Patient mergedSourcePatient = mergeGoldenPatients();
		HumanName returnedName = mergedSourcePatient.getNameFirstRep();
		assertEquals(GIVEN_NAME, returnedName.getGivenAsSingleString());
		assertEquals(FAMILY_NAME, returnedName.getFamily());
		assertEquals(POSTAL_CODE, mergedSourcePatient.getAddressFirstRep().getPostalCode());
	}

	@Test
	public void fromLinkToNoLink() {
		createEmpiLink(myFromGoldenPatient, myTargetPatient1);

		Patient mergedGoldenPatient = mergeGoldenPatients();
		List<EmpiLink> links = getNonRedirectLinksByPerson(mergedGoldenPatient);
		assertEquals(1, links.size());
		assertThat(mergedGoldenPatient, is(possibleLinkedTo(myTargetPatient1)));
	}

	@Test
	public void fromNoLinkToLink() {
		createEmpiLink(myToGoldenPatient, myTargetPatient1);

		Patient mergedSourcePatient = mergeGoldenPatients();
		List<EmpiLink> links = getNonRedirectLinksByPerson(mergedSourcePatient);
		assertEquals(1, links.size());
		assertThat(mergedSourcePatient, is(possibleLinkedTo(myTargetPatient1)));
	}

	@Test
	public void fromManualLinkOverridesAutoToLink() {
		EmpiLink fromLink = createEmpiLink(myFromGoldenPatient, myTargetPatient1);
		fromLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		fromLink.setMatchResult(EmpiMatchResultEnum.MATCH);
		saveLink(fromLink);

		createEmpiLink(myToGoldenPatient, myTargetPatient1);

		mergeGoldenPatients();
		List<EmpiLink> links = getNonRedirectLinksByPerson(myToGoldenPatient);
		assertEquals(1, links.size());
		assertEquals(EmpiLinkSourceEnum.MANUAL, links.get(0).getLinkSource());
	}

	private List<EmpiLink> getNonRedirectLinksByPerson(Patient theGoldenPatient) {
		return myEmpiLinkDaoSvc.findEmpiLinksByGoldenResource(theGoldenPatient).stream()
			.filter(link -> !link.isRedirect())
			.collect(Collectors.toList());
	}

	@Test
	public void fromManualNoMatchLinkOverridesAutoToLink() {
		EmpiLink fromLink = createEmpiLink(myFromGoldenPatient, myTargetPatient1);
		fromLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		fromLink.setMatchResult(EmpiMatchResultEnum.NO_MATCH);

		saveLink(fromLink);

		createEmpiLink(myToGoldenPatient, myTargetPatient1);

		mergeGoldenPatients();
		List<EmpiLink> links = getNonRedirectLinksByPerson(myToGoldenPatient);
		assertEquals(1, links.size());
		assertEquals(EmpiLinkSourceEnum.MANUAL, links.get(0).getLinkSource());
		assertEquals(EmpiMatchResultEnum.NO_MATCH, links.get(0).getMatchResult());
	}

	@Test
	public void fromManualAutoMatchLinkNoOverridesManualToLink() {
		createEmpiLink(myFromGoldenPatient, myTargetPatient1);

		EmpiLink toLink = createEmpiLink(myToGoldenPatient, myTargetPatient1);
		toLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		toLink.setMatchResult(EmpiMatchResultEnum.NO_MATCH);
		saveLink(toLink);

		mergeGoldenPatients();
		List<EmpiLink> links = getNonRedirectLinksByPerson(myToGoldenPatient);
		assertEquals(1, links.size());
		assertEquals(EmpiLinkSourceEnum.MANUAL, links.get(0).getLinkSource());
		assertEquals(EmpiMatchResultEnum.NO_MATCH, links.get(0).getMatchResult());
	}

	@Test
	public void fromNoMatchMergeToManualMatchIsError() {
		EmpiLink fromLink = createEmpiLink(myFromGoldenPatient, myTargetPatient1);
		fromLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		fromLink.setMatchResult(EmpiMatchResultEnum.NO_MATCH);
		saveLink(fromLink);

		EmpiLink toLink = createEmpiLink(myToGoldenPatient, myTargetPatient1);
		toLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		toLink.setMatchResult(EmpiMatchResultEnum.MATCH);
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
		EmpiLink fromLink = createEmpiLink(myFromGoldenPatient, myTargetPatient1);
		fromLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		fromLink.setMatchResult(EmpiMatchResultEnum.MATCH);
		saveLink(fromLink);

		EmpiLink toLink = createEmpiLink(myToGoldenPatient, myTargetPatient1);
		toLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		toLink.setMatchResult(EmpiMatchResultEnum.NO_MATCH);
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
		EmpiLink fromLink = createEmpiLink(myFromGoldenPatient, myTargetPatient1);
		fromLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		fromLink.setMatchResult(EmpiMatchResultEnum.NO_MATCH);
		saveLink(fromLink);

		EmpiLink toLink = createEmpiLink(myToGoldenPatient, myTargetPatient2);
		toLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		toLink.setMatchResult(EmpiMatchResultEnum.MATCH);
		saveLink(toLink);

		mergeGoldenPatients();

		assertResourceHasLinkCount(myToGoldenPatient, 3);
		assertResourceHasLinkCount(myFromGoldenPatient, 0);
		// TODO ENSURE PROPER LINK TYPES
		assertEquals(3, myEmpiLinkDao.count());
	}

	@Test
	public void from123To1() {
		createEmpiLink(myFromGoldenPatient, myTargetPatient1);
		createEmpiLink(myFromGoldenPatient, myTargetPatient2);
		createEmpiLink(myFromGoldenPatient, myTargetPatient3);
		createEmpiLink(myToGoldenPatient, myTargetPatient1);

		mergeGoldenPatients();
		myEmpiLinkHelper.logEmpiLinks();

		assertThat(myToGoldenPatient, is(possibleLinkedTo(myTargetPatient1, myTargetPatient2, myTargetPatient3)));
		assertResourceHasAutoLinkCount(myToGoldenPatient, 3);
	}


	private void assertResourceHasLinkCount(IBaseResource theResource, int theCount) {
		List<EmpiLink> links = myEmpiLinkDaoSvc.findEmpiLinksByGoldenResource(theResource);
		assertEquals(theCount, links.size());
	}

	@Test
	public void from1To123() {
		createEmpiLink(myFromGoldenPatient, myTargetPatient1);
		createEmpiLink(myToGoldenPatient, myTargetPatient1);
		createEmpiLink(myToGoldenPatient, myTargetPatient2);
		createEmpiLink(myToGoldenPatient, myTargetPatient3);

		mergeGoldenPatients();
		myEmpiLinkHelper.logEmpiLinks();

		assertThat(myToGoldenPatient, is(possibleLinkedTo(myTargetPatient1, myTargetPatient2, myTargetPatient3)));
		assertResourceHasAutoLinkCount(myToGoldenPatient, 3);
	}

	private void assertResourceHasAutoLinkCount(Patient myToGoldenPatient, int theCount) {
		List<EmpiLink> links = myEmpiLinkDaoSvc.findEmpiLinksByGoldenResource(myToGoldenPatient);
		assertEquals(theCount, links.stream().filter(EmpiLink::isAuto).count());
	}

	@Test
	public void from123To123() {
		createEmpiLink(myFromGoldenPatient, myTargetPatient1);
		createEmpiLink(myFromGoldenPatient, myTargetPatient2);
		createEmpiLink(myFromGoldenPatient, myTargetPatient3);
		createEmpiLink(myToGoldenPatient, myTargetPatient1);
		createEmpiLink(myToGoldenPatient, myTargetPatient2);
		createEmpiLink(myToGoldenPatient, myTargetPatient3);

		mergeGoldenPatients();

		assertThat(myToGoldenPatient, is(possibleLinkedTo(myTargetPatient1, myTargetPatient2, myTargetPatient3)));

		assertResourceHasAutoLinkCount(myToGoldenPatient, 3);
	}

	@Test
	public void from12To23() {
		createEmpiLink(myFromGoldenPatient, myTargetPatient1);
		createEmpiLink(myFromGoldenPatient, myTargetPatient2);
		createEmpiLink(myToGoldenPatient, myTargetPatient2);
		createEmpiLink(myToGoldenPatient, myTargetPatient3);

		mergeGoldenPatients();
		myEmpiLinkHelper.logEmpiLinks();

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

	private EmpiLink createEmpiLink(Patient theSourcePatient, Patient theTargetPatient) {
		//TODO GGG Ensure theis comment can be safely removed
		//theSourcePatient.addLink().setTarget(new Reference(theTargetPatient));
		return myEmpiLinkDaoSvc.createOrUpdateLinkEntity(theSourcePatient, theTargetPatient, POSSIBLE_MATCH, EmpiLinkSourceEnum.AUTO, createContextForCreate("Patient"));
	}

	private void populatePerson(Patient theSourcePatient) {
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
