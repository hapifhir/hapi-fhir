package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.api.EmpiMatchOutcome;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.api.IEmpiPersonMergerSvc;
import ca.uhn.fhir.empi.model.EmpiTransactionContext;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import ca.uhn.fhir.jpa.empi.helper.EmpiLinkHelper;
import ca.uhn.fhir.jpa.empi.interceptor.IEmpiStorageInterceptor;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import ca.uhn.fhir.rest.server.TransactionLogMessages;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
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
	IEmpiPersonMergerSvc myEmpiPersonMergerSvc;
	@Autowired
	EmpiLinkHelper myEmpiLinkHelper;
	@Autowired
	IEmpiStorageInterceptor myEmpiStorageInterceptor;
	@Autowired
	IInterceptorService myInterceptorService;

	private Patient myFromSourcePatient;
	private Patient myToSourcePatient;
	private Long myFromSourcePatientPid;
	private Long myToSourcePatientPid;
	private Patient myTargetPatient1;
	private Patient myTargetPatient2;
	private Patient myTargetPatient3;

	@BeforeEach
	public void before() {
		super.loadEmpiSearchParameters();

		myFromSourcePatient = createSourceResourcePatient();
		IdType fromSourcePatientId = myFromSourcePatient.getIdElement().toUnqualifiedVersionless();
		myFromSourcePatientPid = myIdHelperService.getPidOrThrowException(fromSourcePatientId);
		myToSourcePatient = createSourceResourcePatient();
		IdType toSourcePatientId = myToSourcePatient.getIdElement().toUnqualifiedVersionless();
		myToSourcePatientPid = myIdHelperService.getPidOrThrowException(toSourcePatientId);

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
		assertEquals(2, getAllSourcePatients().size());
		assertEquals(2, getAllActiveSourcePatients().size());

		Patient mergedSourcePatients = mergeSourcePatients();
		assertEquals(myToSourcePatient.getIdElement(), mergedSourcePatients.getIdElement());
		assertThat(mergedSourcePatients, is(sameSourceResourceAs(mergedSourcePatients)));
		assertEquals(2, getAllSourcePatients().size());
		assertEquals(1, getAllActiveSourcePatients().size());
	}

	private Patient mergeSourcePatients() {
		assertEquals(0, redirectLinkCount());
		Patient retval = (Patient) myEmpiPersonMergerSvc.mergePersons(myFromSourcePatient, myToSourcePatient, createEmpiContext());
		assertEquals(1, redirectLinkCount());
		return retval;
	}

	private int redirectLinkCount() {
		EmpiLink empiLink = new EmpiLink().setMatchResult(EmpiMatchResultEnum.REDIRECT);
		Example<EmpiLink> example = Example.of(empiLink);
		return myEmpiLinkDao.findAll(example).size();
	}

	private EmpiTransactionContext createEmpiContext() {
		return new EmpiTransactionContext(TransactionLogMessages.createFromTransactionGuid(UUID.randomUUID().toString()), EmpiTransactionContext.OperationType.MERGE_PERSONS);
	}

	@Test
	public void mergeRemovesPossibleDuplicatesLink() {
		EmpiLink empiLink = myEmpiLinkDaoSvc.newEmpiLink().setSourceResourcePid(myToSourcePatientPid).setTargetPid(myFromSourcePatientPid).setMatchResult(EmpiMatchResultEnum.POSSIBLE_DUPLICATE).setLinkSource(EmpiLinkSourceEnum.AUTO);
		saveLink(empiLink);

		{
			List<EmpiLink> foundLinks = myEmpiLinkDao.findAll();
			assertEquals(1, foundLinks.size());
			assertEquals(EmpiMatchResultEnum.POSSIBLE_DUPLICATE, foundLinks.get(0).getMatchResult());
		}

		mergeSourcePatients();

		{
			List<EmpiLink> foundLinks = myEmpiLinkDao.findAll();
			assertEquals(1, foundLinks.size());
			assertEquals(EmpiMatchResultEnum.REDIRECT, foundLinks.get(0).getMatchResult());
		}
	}

	@Test
	public void fullFromEmptyTo() {
		populatePerson(myFromSourcePatient);

		Patient mergedSourcePatient = mergeSourcePatients();
		HumanName returnedName = mergedSourcePatient.getNameFirstRep();
		assertEquals(GIVEN_NAME, returnedName.getGivenAsSingleString());
		assertEquals(FAMILY_NAME, returnedName.getFamily());
		assertEquals(POSTAL_CODE, mergedSourcePatient.getAddressFirstRep().getPostalCode());
	}

	@Test
	public void emptyFromFullTo() {
		myFromSourcePatient.getName().add(new HumanName().addGiven(BAD_GIVEN_NAME));
		populatePerson(myToSourcePatient);

		Patient mergedSourcePatient = mergeSourcePatients();
		HumanName returnedName = mergedSourcePatient.getNameFirstRep();
		assertEquals(GIVEN_NAME, returnedName.getGivenAsSingleString());
		assertEquals(FAMILY_NAME, returnedName.getFamily());
		assertEquals(POSTAL_CODE, mergedSourcePatient.getAddressFirstRep().getPostalCode());
	}

	@Test
	public void fromLinkToNoLink() {
		createEmpiLink(myFromSourcePatient, myTargetPatient1);

		Patient mergedSourcePatient = mergeSourcePatients();
		List<EmpiLink> links = getNonRedirectLinksByPerson(mergedSourcePatient);
		assertEquals(1, links.size());
		assertThat(mergedSourcePatient, is(possibleLinkedTo(myTargetPatient1)));
		fail("FIXME");
	}

	@Test
	public void fromNoLinkToLink() {
		createEmpiLink(myToSourcePatient, myTargetPatient1);

		Patient mergedSourcePatient = mergeSourcePatients();
		List<EmpiLink> links = getNonRedirectLinksByPerson(mergedSourcePatient);
		assertEquals(1, links.size());
		assertThat(mergedSourcePatient, is(possibleLinkedTo(myTargetPatient1)));
		fail("FIXME");
	}

	@Test
	public void fromManualLinkOverridesAutoToLink() {
		EmpiLink fromLink = createEmpiLink(myFromSourcePatient, myTargetPatient1);
		fromLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		fromLink.setMatchResult(EmpiMatchResultEnum.MATCH);
		saveLink(fromLink);

		createEmpiLink(myToSourcePatient, myTargetPatient1);

		mergeSourcePatients();
		List<EmpiLink> links = getNonRedirectLinksByPerson(myToSourcePatient);
		assertEquals(1, links.size());
		assertEquals(EmpiLinkSourceEnum.MANUAL, links.get(0).getLinkSource());
	}

	private List<EmpiLink> getNonRedirectLinksByPerson(Patient theSourcePatient) {
		return myEmpiLinkDaoSvc.findEmpiLinksBySourceResource(theSourcePatient).stream()
			.filter(link -> !link.isRedirect())
			.collect(Collectors.toList());
	}

	@Test
	public void fromManualNoMatchLinkOverridesAutoToLink() {
		EmpiLink fromLink = createEmpiLink(myFromSourcePatient, myTargetPatient1);
		fromLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		fromLink.setMatchResult(EmpiMatchResultEnum.NO_MATCH);

		saveLink(fromLink);

		createEmpiLink(myToSourcePatient, myTargetPatient1);

		mergeSourcePatients();
		List<EmpiLink> links = getNonRedirectLinksByPerson(myToSourcePatient);
		assertEquals(1, links.size());
		assertEquals(EmpiLinkSourceEnum.MANUAL, links.get(0).getLinkSource());
	}

	@Test
	public void fromManualAutoMatchLinkNoOverridesManualToLink() {
		createEmpiLink(myFromSourcePatient, myTargetPatient1);

		EmpiLink toLink = createEmpiLink(myToSourcePatient, myTargetPatient1);
		toLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		toLink.setMatchResult(EmpiMatchResultEnum.NO_MATCH);
		saveLink(toLink);

		mergeSourcePatients();
		List<EmpiLink> links = getNonRedirectLinksByPerson(myToSourcePatient);
		assertEquals(1, links.size());
		assertEquals(EmpiLinkSourceEnum.MANUAL, links.get(0).getLinkSource());
	}

	@Test
	public void fromNoMatchMergeToManualMatchIsError() {
		EmpiLink fromLink = createEmpiLink(myFromSourcePatient, myTargetPatient1);
		fromLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		fromLink.setMatchResult(EmpiMatchResultEnum.NO_MATCH);
		saveLink(fromLink);

		EmpiLink toLink = createEmpiLink(myToSourcePatient, myTargetPatient1);
		toLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		toLink.setMatchResult(EmpiMatchResultEnum.MATCH);
		saveLink(toLink);

		try {
			mergeSourcePatients();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("A MANUAL NO_MATCH link may not be merged into a MANUAL MATCH link for the same target", e.getMessage());
		}
	}

	@Test
	public void fromMatchMergeToManualNoMatchIsError() {
		EmpiLink fromLink = createEmpiLink(myFromSourcePatient, myTargetPatient1);
		fromLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		fromLink.setMatchResult(EmpiMatchResultEnum.MATCH);
		saveLink(fromLink);

		EmpiLink toLink = createEmpiLink(myToSourcePatient, myTargetPatient1);
		toLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		toLink.setMatchResult(EmpiMatchResultEnum.NO_MATCH);
		saveLink(toLink);

		try {
			mergeSourcePatients();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("A MANUAL MATCH link may not be merged into a MANUAL NO_MATCH link for the same target", e.getMessage());
		}
	}

	@Test
	public void fromNoMatchMergeToManualMatchDifferentPatientIsOk() {
		EmpiLink fromLink = createEmpiLink(myFromSourcePatient, myTargetPatient1);
		fromLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		fromLink.setMatchResult(EmpiMatchResultEnum.NO_MATCH);
		saveLink(fromLink);

		EmpiLink toLink = createEmpiLink(myToSourcePatient, myTargetPatient2);
		toLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		toLink.setMatchResult(EmpiMatchResultEnum.MATCH);
		saveLink(toLink);

		mergeSourcePatients();
		assertEquals(1, myToSourcePatient.getLink().size());
		assertEquals(3, myEmpiLinkDao.count());
	}

	@Test
	public void from123To1() {
		createEmpiLink(myFromSourcePatient, myTargetPatient1);
		createEmpiLink(myFromSourcePatient, myTargetPatient2);
		createEmpiLink(myFromSourcePatient, myTargetPatient3);
		createEmpiLink(myToSourcePatient, myTargetPatient1);

		mergeSourcePatients();
		myEmpiLinkHelper.logEmpiLinks();

		assertThat(myToSourcePatient, is(possibleLinkedTo(myTargetPatient1, myTargetPatient2, myTargetPatient3)));
		assertEquals(3, myToSourcePatient.getLink().size());
	}

	@Test
	public void from1To123() {
		createEmpiLink(myFromSourcePatient, myTargetPatient1);
		createEmpiLink(myToSourcePatient, myTargetPatient1);
		createEmpiLink(myToSourcePatient, myTargetPatient2);
		createEmpiLink(myToSourcePatient, myTargetPatient3);

		mergeSourcePatients();
		myEmpiLinkHelper.logEmpiLinks();

		assertThat(myToSourcePatient, is(possibleLinkedTo(myTargetPatient1, myTargetPatient2, myTargetPatient3)));
		assertEquals(3, myToSourcePatient.getLink().size());
	}

	@Test
	public void from123To123() {
		createEmpiLink(myFromSourcePatient, myTargetPatient1);
		createEmpiLink(myFromSourcePatient, myTargetPatient2);
		createEmpiLink(myFromSourcePatient, myTargetPatient3);
		createEmpiLink(myToSourcePatient, myTargetPatient1);
		createEmpiLink(myToSourcePatient, myTargetPatient2);
		createEmpiLink(myToSourcePatient, myTargetPatient3);

		mergeSourcePatients();
		myEmpiLinkHelper.logEmpiLinks();

		assertThat(myToSourcePatient, is(possibleLinkedTo(myTargetPatient1, myTargetPatient2, myTargetPatient3)));
		assertEquals(3, myToSourcePatient.getLink().size());
	}

	@Test
	public void from12To23() {
		createEmpiLink(myFromSourcePatient, myTargetPatient1);
		createEmpiLink(myFromSourcePatient, myTargetPatient2);
		createEmpiLink(myToSourcePatient, myTargetPatient2);
		createEmpiLink(myToSourcePatient, myTargetPatient3);

		mergeSourcePatients();
		myEmpiLinkHelper.logEmpiLinks();

		assertThat(myToSourcePatient, is(possibleLinkedTo(myTargetPatient1, myTargetPatient2, myTargetPatient3)));
		assertEquals(3, myToSourcePatient.getLink().size());
	}

	@Test
	public void testMergeNames() {
		myFromSourcePatient.addName().addGiven("Jim");
		myFromSourcePatient.getNameFirstRep().addGiven("George");
		assertThat(myFromSourcePatient.getName(), hasSize(1));
		assertThat(myFromSourcePatient.getName().get(0).getGiven(), hasSize(2));

		myToSourcePatient.addName().addGiven("Jeff");
		myToSourcePatient.getNameFirstRep().addGiven("George");
		assertThat(myToSourcePatient.getName(), hasSize(1));
		assertThat(myToSourcePatient.getName().get(0).getGiven(), hasSize(2));

		Patient mergedSourcePatient = mergeSourcePatients();
		assertThat(mergedSourcePatient.getName(), hasSize(2));
		assertThat(mergedSourcePatient.getName().get(0).getGiven(), hasSize(2));
		assertThat(mergedSourcePatient.getName().get(1).getGiven(), hasSize(2));
	}

	@Test
	public void testMergeNamesAllSame() {
		myFromSourcePatient.addName().addGiven("Jim");
		myFromSourcePatient.getNameFirstRep().addGiven("George");
		assertThat(myFromSourcePatient.getName(), hasSize(1));
		assertThat(myFromSourcePatient.getName().get(0).getGiven(), hasSize(2));

		myToSourcePatient.addName().addGiven("Jim");
		myToSourcePatient.getNameFirstRep().addGiven("George");
		assertThat(myToSourcePatient.getName(), hasSize(1));
		assertThat(myToSourcePatient.getName().get(0).getGiven(), hasSize(2));

		mergeSourcePatients();
		assertThat(myToSourcePatient.getName(), hasSize(1));
		assertThat(myToSourcePatient.getName().get(0).getGiven(), hasSize(2));
	}

	@Test
	public void testMergeIdentities() {
		myFromSourcePatient.addIdentifier().setValue("aaa");
		myFromSourcePatient.addIdentifier().setValue("bbb");
		assertThat(myFromSourcePatient.getIdentifier(), hasSize(2));

		myToSourcePatient.addIdentifier().setValue("aaa");
		myToSourcePatient.addIdentifier().setValue("ccc");
		assertThat(myToSourcePatient.getIdentifier(), hasSize(2));

		mergeSourcePatients();
		assertThat(myToSourcePatient.getIdentifier(), hasSize(3));
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
