package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class EmpiPersonMergerSvcTest extends BaseEmpiR4Test {
	public static final String GIVEN_NAME = "Jenn";
	public static final String FAMILY_NAME = "Chan";
	public static final String POSTAL_CODE = "M6G 1B4";
	private static final String BAD_GIVEN_NAME = "Bob";

	@Autowired
	IEmpiPersonMergerSvc myEmpiPersonMergerSvc;
	@Autowired
	EmpiLinkHelper myEmpiLinkHelper;
	@Autowired
	IEmpiStorageInterceptor myEmpiStorageInterceptor;
	@Autowired
	IInterceptorService myInterceptorService;

	private Person myDeletePerson;
	private Person myKeepPerson;
	private IdType myDeletePersonId;
	private IdType myKeepPersonId;
	private Long myDeletePersonPid;
	private Long myKeepPersonPid;
	private Patient myTargetPatient1;
	private Patient myTargetPatient2;
	private Patient myTargetPatient3;

	@Before
	public void before() {
		myDeletePerson = createPerson();
		myDeletePersonId = myDeletePerson.getIdElement().toUnqualifiedVersionless();
		myDeletePersonPid = myIdHelperService.getPidOrThrowException(myDeletePersonId);
		myKeepPerson = createPerson();
		myKeepPersonId = myKeepPerson.getIdElement().toUnqualifiedVersionless();
		myKeepPersonPid = myIdHelperService.getPidOrThrowException(myKeepPersonId);

		myTargetPatient1 = createPatient();

		myTargetPatient2 = createPatient();

		myTargetPatient3 = createPatient();

		// Register the empi storage interceptor after the creates so the delete hook is fired when we merge
		myInterceptorService.registerInterceptor(myEmpiStorageInterceptor);
	}

	@After
	public void after() {
		myInterceptorService.unregisterInterceptor(myEmpiStorageInterceptor);
		super.after();
	}
	
	@Test
	public void emptyMerge() {
		assertEquals(2, getAllPersons().size());

		Person mergedPerson = mergePersons();
		assertEquals(myKeepPerson.getIdElement(), mergedPerson.getIdElement());
		assertThat(mergedPerson, is(samePersonAs(mergedPerson)));
		assertEquals(1, getAllPersons().size());
	}

	private Person mergePersons() {
		return (Person) myEmpiPersonMergerSvc.mergePersons(myDeletePerson, myKeepPerson, createEmpiContext());
	}

	private EmpiTransactionContext createEmpiContext() {
		return new EmpiTransactionContext(TransactionLogMessages.createFromTransactionGuid(UUID.randomUUID().toString()), EmpiTransactionContext.OperationType.MERGE_PERSONS);
	}

	@Test
	public void mergeRemovesPossibleDuplicatesLink() {
		EmpiLink empiLink = new EmpiLink().setPersonPid(myKeepPersonPid).setTargetPid(myDeletePersonPid).setMatchResult(EmpiMatchResultEnum.POSSIBLE_DUPLICATE).setLinkSource(EmpiLinkSourceEnum.AUTO);
		myEmpiLinkDaoSvc.save(empiLink);
		assertEquals(1, myEmpiLinkDao.count());
		mergePersons();
		assertEquals(0, myEmpiLinkDao.count());
	}

	@Test
	public void fullDeleteEmptyKeep() {
		populatePerson(myDeletePerson);

		Person mergedPerson = mergePersons();
		HumanName returnedName = mergedPerson.getNameFirstRep();
		assertEquals(GIVEN_NAME, returnedName.getGivenAsSingleString());
		assertEquals(FAMILY_NAME, returnedName.getFamily());
		assertEquals(POSTAL_CODE, mergedPerson.getAddressFirstRep().getPostalCode());
	}

	@Test
	public void emptyDeleteFullKeep() {
		myDeletePerson.getName().add(new HumanName().addGiven(BAD_GIVEN_NAME));
		populatePerson(myKeepPerson);

		Person mergedPerson = mergePersons();
		HumanName returnedName = mergedPerson.getNameFirstRep();
		assertEquals(GIVEN_NAME, returnedName.getGivenAsSingleString());
		assertEquals(FAMILY_NAME, returnedName.getFamily());
		assertEquals(POSTAL_CODE, mergedPerson.getAddressFirstRep().getPostalCode());
	}

	@Test
	public void deleteLinkKeepNoLink() {
		createEmpiLink(myDeletePerson, myTargetPatient1);

		mergePersons();
		List<EmpiLink> links = myEmpiLinkDaoSvc.findEmpiLinksByPersonId(myKeepPerson);
		assertEquals(1, links.size());
		assertThat(myKeepPerson, is(possibleLinkedTo(myTargetPatient1)));
		assertEquals(1, myKeepPerson.getLink().size());
	}

	@Test
	public void deleteNoLinkKeepLink() {
		createEmpiLink(myKeepPerson, myTargetPatient1);

		mergePersons();
		List<EmpiLink> links = myEmpiLinkDaoSvc.findEmpiLinksByPersonId(myKeepPerson);
		assertEquals(1, links.size());
		assertThat(myKeepPerson, is(possibleLinkedTo(myTargetPatient1)));
		assertEquals(1, myKeepPerson.getLink().size());
	}

	@Test
	public void deleteManualLinkOverridesAutoKeepLink() {
		EmpiLink deleteLink = createEmpiLink(myDeletePerson, myTargetPatient1);
		deleteLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		deleteLink.setMatchResult(EmpiMatchResultEnum.MATCH);
		myEmpiLinkDaoSvc.save(deleteLink);

		createEmpiLink(myKeepPerson, myTargetPatient1);

		mergePersons();
		List<EmpiLink> links = myEmpiLinkDaoSvc.findEmpiLinksByPersonId(myKeepPerson);
		assertEquals(1, links.size());
		assertEquals(EmpiLinkSourceEnum.MANUAL, links.get(0).getLinkSource());
	}

	@Test
	public void deleteManualNoMatchLinkOverridesAutoKeepLink() {
		EmpiLink deleteLink = createEmpiLink(myDeletePerson, myTargetPatient1);
		deleteLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		deleteLink.setMatchResult(EmpiMatchResultEnum.NO_MATCH);
		myEmpiLinkDaoSvc.save(deleteLink);

		createEmpiLink(myKeepPerson, myTargetPatient1);

		mergePersons();
		List<EmpiLink> links = myEmpiLinkDaoSvc.findEmpiLinksByPersonId(myKeepPerson);
		assertEquals(1, links.size());
		assertEquals(EmpiLinkSourceEnum.MANUAL, links.get(0).getLinkSource());
	}

	@Test
	public void deleteManualAutoMatchLinkNoOverridesManualKeepLink() {
		createEmpiLink(myDeletePerson, myTargetPatient1);

		EmpiLink keepLink = createEmpiLink(myKeepPerson, myTargetPatient1);
		keepLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		keepLink.setMatchResult(EmpiMatchResultEnum.NO_MATCH);
		myEmpiLinkDaoSvc.save(keepLink);

		mergePersons();
		List<EmpiLink> links = myEmpiLinkDaoSvc.findEmpiLinksByPersonId(myKeepPerson);
		assertEquals(1, links.size());
		assertEquals(EmpiLinkSourceEnum.MANUAL, links.get(0).getLinkSource());
	}

	@Test
	public void deleteNoMatchMergeToManualMatchIsError() {
		EmpiLink deleteLink = createEmpiLink(myDeletePerson, myTargetPatient1);
		deleteLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		deleteLink.setMatchResult(EmpiMatchResultEnum.NO_MATCH);
		myEmpiLinkDaoSvc.save(deleteLink);

		EmpiLink keepLink = createEmpiLink(myKeepPerson, myTargetPatient1);
		keepLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		keepLink.setMatchResult(EmpiMatchResultEnum.MATCH);
		myEmpiLinkDaoSvc.save(keepLink);

		try {
			mergePersons();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("A MANUAL NO_MATCH link may not be merged into a MANUAL MATCH link for the same target", e.getMessage());
		}
	}

	@Test
	public void deleteMatchMergeToManualNoMatchIsError() {
		EmpiLink deleteLink = createEmpiLink(myDeletePerson, myTargetPatient1);
		deleteLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		deleteLink.setMatchResult(EmpiMatchResultEnum.MATCH);
		myEmpiLinkDaoSvc.save(deleteLink);

		EmpiLink keepLink = createEmpiLink(myKeepPerson, myTargetPatient1);
		keepLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		keepLink.setMatchResult(EmpiMatchResultEnum.NO_MATCH);
		myEmpiLinkDaoSvc.save(keepLink);

		try {
			mergePersons();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("A MANUAL MATCH link may not be merged into a MANUAL NO_MATCH link for the same target", e.getMessage());
		}
	}

	@Test
	public void deleteNoMatchMergeToManualMatchDifferentPatientIsOk() {
		EmpiLink deleteLink = createEmpiLink(myDeletePerson, myTargetPatient1);
		deleteLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		deleteLink.setMatchResult(EmpiMatchResultEnum.NO_MATCH);
		myEmpiLinkDaoSvc.save(deleteLink);

		EmpiLink keepLink = createEmpiLink(myKeepPerson, myTargetPatient2);
		keepLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		keepLink.setMatchResult(EmpiMatchResultEnum.MATCH);
		myEmpiLinkDaoSvc.save(keepLink);

		mergePersons();
		assertEquals(1, myKeepPerson.getLink().size());
		assertEquals(2, myEmpiLinkDao.count());
	}

	@Test
	public void delete123Keep1() {
		createEmpiLink(myDeletePerson, myTargetPatient1);
		createEmpiLink(myDeletePerson, myTargetPatient2);
		createEmpiLink(myDeletePerson, myTargetPatient3);
		createEmpiLink(myKeepPerson, myTargetPatient1);

		mergePersons();
		myEmpiLinkHelper.logEmpiLinks();

		assertThat(myKeepPerson, is(possibleLinkedTo(myTargetPatient1, myTargetPatient2, myTargetPatient3)));
		assertEquals(3, myKeepPerson.getLink().size());
	}

	@Test
	public void delete1Keep123() {
		createEmpiLink(myDeletePerson, myTargetPatient1);
		createEmpiLink(myKeepPerson, myTargetPatient1);
		createEmpiLink(myKeepPerson, myTargetPatient2);
		createEmpiLink(myKeepPerson, myTargetPatient3);

		mergePersons();
		myEmpiLinkHelper.logEmpiLinks();

		assertThat(myKeepPerson, is(possibleLinkedTo(myTargetPatient1, myTargetPatient2, myTargetPatient3)));
		assertEquals(3, myKeepPerson.getLink().size());
	}

	@Test
	public void delete123Keep123() {
		createEmpiLink(myDeletePerson, myTargetPatient1);
		createEmpiLink(myDeletePerson, myTargetPatient2);
		createEmpiLink(myDeletePerson, myTargetPatient3);
		createEmpiLink(myKeepPerson, myTargetPatient1);
		createEmpiLink(myKeepPerson, myTargetPatient2);
		createEmpiLink(myKeepPerson, myTargetPatient3);

		mergePersons();
		myEmpiLinkHelper.logEmpiLinks();

		assertThat(myKeepPerson, is(possibleLinkedTo(myTargetPatient1, myTargetPatient2, myTargetPatient3)));
		assertEquals(3, myKeepPerson.getLink().size());
	}

	@Test
	public void delete12Keep23() {
		createEmpiLink(myDeletePerson, myTargetPatient1);
		createEmpiLink(myDeletePerson, myTargetPatient2);
		createEmpiLink(myKeepPerson, myTargetPatient2);
		createEmpiLink(myKeepPerson, myTargetPatient3);

		mergePersons();
		myEmpiLinkHelper.logEmpiLinks();

		assertThat(myKeepPerson, is(possibleLinkedTo(myTargetPatient1, myTargetPatient2, myTargetPatient3)));
		assertEquals(3, myKeepPerson.getLink().size());
	}

	@Test
	public void testMergeNames() {
		myDeletePerson.addName().addGiven("Jim");
		myDeletePerson.getNameFirstRep().addGiven("George");
		assertThat(myDeletePerson.getName(), hasSize(1));
		assertThat(myDeletePerson.getName().get(0).getGiven(), hasSize(2));

		myKeepPerson.addName().addGiven("Jeff");
		myKeepPerson.getNameFirstRep().addGiven("George");
		assertThat(myKeepPerson.getName(), hasSize(1));
		assertThat(myKeepPerson.getName().get(0).getGiven(), hasSize(2));

		mergePersons();
		assertThat(myKeepPerson.getName(), hasSize(2));
		assertThat(myKeepPerson.getName().get(0).getGiven(), hasSize(2));
		assertThat(myKeepPerson.getName().get(1).getGiven(), hasSize(2));
	}

	@Test
	public void testMergeNamesAllSame() {
		myDeletePerson.addName().addGiven("Jim");
		myDeletePerson.getNameFirstRep().addGiven("George");
		assertThat(myDeletePerson.getName(), hasSize(1));
		assertThat(myDeletePerson.getName().get(0).getGiven(), hasSize(2));

		myKeepPerson.addName().addGiven("Jim");
		myKeepPerson.getNameFirstRep().addGiven("George");
		assertThat(myKeepPerson.getName(), hasSize(1));
		assertThat(myKeepPerson.getName().get(0).getGiven(), hasSize(2));

		mergePersons();
		assertThat(myKeepPerson.getName(), hasSize(1));
		assertThat(myKeepPerson.getName().get(0).getGiven(), hasSize(2));
	}

	@Test
	public void testMergeIdentities() {
		myDeletePerson.addIdentifier().setValue("aaa");
		myDeletePerson.addIdentifier().setValue("bbb");
		assertThat(myDeletePerson.getIdentifier(), hasSize(2));

		myKeepPerson.addIdentifier().setValue("aaa");
		myKeepPerson.addIdentifier().setValue("ccc");
		assertThat(myKeepPerson.getIdentifier(), hasSize(2));

		mergePersons();
		assertThat(myKeepPerson.getIdentifier(), hasSize(3));
	}

	private EmpiLink createEmpiLink(Person thePerson, Patient theTargetPatient) {
		thePerson.addLink().setTarget(new Reference(theTargetPatient));
		return myEmpiLinkDaoSvc.createOrUpdateLinkEntity(thePerson, theTargetPatient, EmpiMatchResultEnum.POSSIBLE_MATCH, EmpiLinkSourceEnum.AUTO, createContextForCreate());
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
