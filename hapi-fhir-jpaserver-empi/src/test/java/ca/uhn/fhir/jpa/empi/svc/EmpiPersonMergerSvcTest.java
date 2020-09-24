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
import org.hl7.fhir.r4.model.Person;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

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

	private Person myFromPerson;
	private Person myToPerson;
	private Long myFromPersonPid;
	private Long myToPersonPid;
	private Patient myTargetPatient1;
	private Patient myTargetPatient2;
	private Patient myTargetPatient3;

	@BeforeEach
	public void before() {
		super.loadEmpiSearchParameters();

		myFromPerson = createPerson();
		IdType fromPersonId = myFromPerson.getIdElement().toUnqualifiedVersionless();
		myFromPersonPid = myIdHelperService.getPidOrThrowException(fromPersonId);
		myToPerson = createPerson();
		IdType toPersonId = myToPerson.getIdElement().toUnqualifiedVersionless();
		myToPersonPid = myIdHelperService.getPidOrThrowException(toPersonId);

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
		assertEquals(2, getAllPersons().size());
		assertEquals(2, getAllActivePersons().size());

		Person mergedPerson = mergePersons();
		assertEquals(myToPerson.getIdElement(), mergedPerson.getIdElement());
		assertThat(mergedPerson, is(samePersonAs(mergedPerson)));
		assertEquals(2, getAllPersons().size());
		assertEquals(1, getAllActivePersons().size());
	}

	private Person mergePersons() {
		return (Person) myEmpiPersonMergerSvc.mergePersons(myFromPerson, myToPerson, createEmpiContext());
	}

	private EmpiTransactionContext createEmpiContext() {
		return new EmpiTransactionContext(TransactionLogMessages.createFromTransactionGuid(UUID.randomUUID().toString()), EmpiTransactionContext.OperationType.MERGE_PERSONS);
	}

	@Test
	public void mergeRemovesPossibleDuplicatesLink() {
		EmpiLink empiLink = myEmpiLinkDaoSvc.newEmpiLink().setPersonPid(myToPersonPid).setTargetPid(myFromPersonPid).setMatchResult(EmpiMatchResultEnum.POSSIBLE_DUPLICATE).setLinkSource(EmpiLinkSourceEnum.AUTO);
		saveLink(empiLink);
		assertEquals(1, myEmpiLinkDao.count());
		mergePersons();
		assertEquals(0, myEmpiLinkDao.count());
	}

	@Test
	public void fullFromEmptyTo() {
		populatePerson(myFromPerson);

		Person mergedPerson = mergePersons();
		HumanName returnedName = mergedPerson.getNameFirstRep();
		assertEquals(GIVEN_NAME, returnedName.getGivenAsSingleString());
		assertEquals(FAMILY_NAME, returnedName.getFamily());
		assertEquals(POSTAL_CODE, mergedPerson.getAddressFirstRep().getPostalCode());
	}

	@Test
	public void emptyFromFullTo() {
		myFromPerson.getName().add(new HumanName().addGiven(BAD_GIVEN_NAME));
		populatePerson(myToPerson);

		Person mergedPerson = mergePersons();
		HumanName returnedName = mergedPerson.getNameFirstRep();
		assertEquals(GIVEN_NAME, returnedName.getGivenAsSingleString());
		assertEquals(FAMILY_NAME, returnedName.getFamily());
		assertEquals(POSTAL_CODE, mergedPerson.getAddressFirstRep().getPostalCode());
	}

	@Test
	public void fromLinkToNoLink() {
		createEmpiLink(myFromPerson, myTargetPatient1);

		Person mergedPerson = mergePersons();
		List<EmpiLink> links = myEmpiLinkDaoSvc.findEmpiLinksByPerson(mergedPerson);
		assertEquals(1, links.size());
		assertThat(mergedPerson, is(possibleLinkedTo(myTargetPatient1)));
		assertEquals(1, myToPerson.getLink().size());
	}

	@Test
	public void fromNoLinkToLink() {
		createEmpiLink(myToPerson, myTargetPatient1);

		Person mergedPerson = mergePersons();
		List<EmpiLink> links = myEmpiLinkDaoSvc.findEmpiLinksByPerson(mergedPerson);
		assertEquals(1, links.size());
		assertThat(mergedPerson, is(possibleLinkedTo(myTargetPatient1)));
		assertEquals(1, myToPerson.getLink().size());
	}

	@Test
	public void fromManualLinkOverridesAutoToLink() {
		EmpiLink fromLink = createEmpiLink(myFromPerson, myTargetPatient1);
		fromLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		fromLink.setMatchResult(EmpiMatchResultEnum.MATCH);
		saveLink(fromLink);

		createEmpiLink(myToPerson, myTargetPatient1);

		mergePersons();
		List<EmpiLink> links = myEmpiLinkDaoSvc.findEmpiLinksByPerson(myToPerson);
		assertEquals(1, links.size());
		assertEquals(EmpiLinkSourceEnum.MANUAL, links.get(0).getLinkSource());
	}

	@Test
	public void fromManualNoMatchLinkOverridesAutoToLink() {
		EmpiLink fromLink = createEmpiLink(myFromPerson, myTargetPatient1);
		fromLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		fromLink.setMatchResult(EmpiMatchResultEnum.NO_MATCH);

		saveLink(fromLink);

		createEmpiLink(myToPerson, myTargetPatient1);

		mergePersons();
		List<EmpiLink> links = myEmpiLinkDaoSvc.findEmpiLinksByPerson(myToPerson);
		assertEquals(1, links.size());
		assertEquals(EmpiLinkSourceEnum.MANUAL, links.get(0).getLinkSource());
	}

	@Test
	public void fromManualAutoMatchLinkNoOverridesManualToLink() {
		createEmpiLink(myFromPerson, myTargetPatient1);

		EmpiLink toLink = createEmpiLink(myToPerson, myTargetPatient1);
		toLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		toLink.setMatchResult(EmpiMatchResultEnum.NO_MATCH);
		saveLink(toLink);

		mergePersons();
		List<EmpiLink> links = myEmpiLinkDaoSvc.findEmpiLinksByPerson(myToPerson);
		assertEquals(1, links.size());
		assertEquals(EmpiLinkSourceEnum.MANUAL, links.get(0).getLinkSource());
	}

	@Test
	public void fromNoMatchMergeToManualMatchIsError() {
		EmpiLink fromLink = createEmpiLink(myFromPerson, myTargetPatient1);
		fromLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		fromLink.setMatchResult(EmpiMatchResultEnum.NO_MATCH);
		saveLink(fromLink);

		EmpiLink toLink = createEmpiLink(myToPerson, myTargetPatient1);
		toLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		toLink.setMatchResult(EmpiMatchResultEnum.MATCH);
		saveLink(toLink);

		try {
			mergePersons();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("A MANUAL NO_MATCH link may not be merged into a MANUAL MATCH link for the same target", e.getMessage());
		}
	}

	@Test
	public void fromMatchMergeToManualNoMatchIsError() {
		EmpiLink fromLink = createEmpiLink(myFromPerson, myTargetPatient1);
		fromLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		fromLink.setMatchResult(EmpiMatchResultEnum.MATCH);
		saveLink(fromLink);

		EmpiLink toLink = createEmpiLink(myToPerson, myTargetPatient1);
		toLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		toLink.setMatchResult(EmpiMatchResultEnum.NO_MATCH);
		saveLink(toLink);

		try {
			mergePersons();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("A MANUAL MATCH link may not be merged into a MANUAL NO_MATCH link for the same target", e.getMessage());
		}
	}

	@Test
	public void fromNoMatchMergeToManualMatchDifferentPatientIsOk() {
		EmpiLink fromLink = createEmpiLink(myFromPerson, myTargetPatient1);
		fromLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		fromLink.setMatchResult(EmpiMatchResultEnum.NO_MATCH);
		saveLink(fromLink);

		EmpiLink toLink = createEmpiLink(myToPerson, myTargetPatient2);
		toLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		toLink.setMatchResult(EmpiMatchResultEnum.MATCH);
		saveLink(toLink);

		mergePersons();
		assertEquals(1, myToPerson.getLink().size());
		assertEquals(2, myEmpiLinkDao.count());
	}

	@Test
	public void from123To1() {
		createEmpiLink(myFromPerson, myTargetPatient1);
		createEmpiLink(myFromPerson, myTargetPatient2);
		createEmpiLink(myFromPerson, myTargetPatient3);
		createEmpiLink(myToPerson, myTargetPatient1);

		mergePersons();
		myEmpiLinkHelper.logEmpiLinks();

		assertThat(myToPerson, is(possibleLinkedTo(myTargetPatient1, myTargetPatient2, myTargetPatient3)));
		assertEquals(3, myToPerson.getLink().size());
	}

	@Test
	public void from1To123() {
		createEmpiLink(myFromPerson, myTargetPatient1);
		createEmpiLink(myToPerson, myTargetPatient1);
		createEmpiLink(myToPerson, myTargetPatient2);
		createEmpiLink(myToPerson, myTargetPatient3);

		mergePersons();
		myEmpiLinkHelper.logEmpiLinks();

		assertThat(myToPerson, is(possibleLinkedTo(myTargetPatient1, myTargetPatient2, myTargetPatient3)));
		assertEquals(3, myToPerson.getLink().size());
	}

	@Test
	public void from123To123() {
		createEmpiLink(myFromPerson, myTargetPatient1);
		createEmpiLink(myFromPerson, myTargetPatient2);
		createEmpiLink(myFromPerson, myTargetPatient3);
		createEmpiLink(myToPerson, myTargetPatient1);
		createEmpiLink(myToPerson, myTargetPatient2);
		createEmpiLink(myToPerson, myTargetPatient3);

		mergePersons();
		myEmpiLinkHelper.logEmpiLinks();

		assertThat(myToPerson, is(possibleLinkedTo(myTargetPatient1, myTargetPatient2, myTargetPatient3)));
		assertEquals(3, myToPerson.getLink().size());
	}

	@Test
	public void from12To23() {
		createEmpiLink(myFromPerson, myTargetPatient1);
		createEmpiLink(myFromPerson, myTargetPatient2);
		createEmpiLink(myToPerson, myTargetPatient2);
		createEmpiLink(myToPerson, myTargetPatient3);

		mergePersons();
		myEmpiLinkHelper.logEmpiLinks();

		assertThat(myToPerson, is(possibleLinkedTo(myTargetPatient1, myTargetPatient2, myTargetPatient3)));
		assertEquals(3, myToPerson.getLink().size());
	}

	@Test
	public void testMergeNames() {
		myFromPerson.addName().addGiven("Jim");
		myFromPerson.getNameFirstRep().addGiven("George");
		assertThat(myFromPerson.getName(), hasSize(1));
		assertThat(myFromPerson.getName().get(0).getGiven(), hasSize(2));

		myToPerson.addName().addGiven("Jeff");
		myToPerson.getNameFirstRep().addGiven("George");
		assertThat(myToPerson.getName(), hasSize(1));
		assertThat(myToPerson.getName().get(0).getGiven(), hasSize(2));

		Person mergedPerson = mergePersons();
		assertThat(mergedPerson.getName(), hasSize(2));
		assertThat(mergedPerson.getName().get(0).getGiven(), hasSize(2));
		assertThat(mergedPerson.getName().get(1).getGiven(), hasSize(2));
	}

	@Test
	public void testMergeNamesAllSame() {
		myFromPerson.addName().addGiven("Jim");
		myFromPerson.getNameFirstRep().addGiven("George");
		assertThat(myFromPerson.getName(), hasSize(1));
		assertThat(myFromPerson.getName().get(0).getGiven(), hasSize(2));

		myToPerson.addName().addGiven("Jim");
		myToPerson.getNameFirstRep().addGiven("George");
		assertThat(myToPerson.getName(), hasSize(1));
		assertThat(myToPerson.getName().get(0).getGiven(), hasSize(2));

		mergePersons();
		assertThat(myToPerson.getName(), hasSize(1));
		assertThat(myToPerson.getName().get(0).getGiven(), hasSize(2));
	}

	@Test
	public void testMergeIdentities() {
		myFromPerson.addIdentifier().setValue("aaa");
		myFromPerson.addIdentifier().setValue("bbb");
		assertThat(myFromPerson.getIdentifier(), hasSize(2));

		myToPerson.addIdentifier().setValue("aaa");
		myToPerson.addIdentifier().setValue("ccc");
		assertThat(myToPerson.getIdentifier(), hasSize(2));

		mergePersons();
		assertThat(myToPerson.getIdentifier(), hasSize(3));
	}

	private EmpiLink createEmpiLink(Person thePerson, Patient theTargetPatient) {
		thePerson.addLink().setTarget(new Reference(theTargetPatient));
		return myEmpiLinkDaoSvc.createOrUpdateLinkEntity(thePerson, theTargetPatient, POSSIBLE_MATCH, EmpiLinkSourceEnum.AUTO, createContextForCreate());
	}

	private void populatePerson(Person thePerson) {
		thePerson.addName(new HumanName().addGiven(GIVEN_NAME).setFamily(FAMILY_NAME));
		thePerson.setGender(Enumerations.AdministrativeGender.FEMALE);
		thePerson.setBirthDateElement(new DateType("1981-01-01"));
		Address address = new Address();
		address.addLine("622 College St");
		address.addLine("Suite 401");
		address.setDistrict("Little Italy");
		address.setCity("Toronto");
		address.setCountry("Canada");
		address.setPostalCode(POSTAL_CODE);
		thePerson.setAddress(Collections.singletonList(address));
	}
}
