package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.api.IEmpiPersonMergerSvc;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import ca.uhn.fhir.jpa.empi.helper.EmpiLinkHelper;
import ca.uhn.fhir.jpa.empi.interceptor.EmpiStorageInterceptor;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

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
	EmpiStorageInterceptor myEmpiStorageInterceptor;
	@Autowired
	IInterceptorService myInterceptorService;

	private Person myDeletePerson;
	private Person myKeepPerson;
	private IdType myDeletePersonId;
	private IdType myKeepPersonId;
	private Long myKeepPersonPid;
	private Patient myTargetPatient1;
	private Long myPatient1Pid;
	private Patient myTargetPatient2;
	private Long myPatient2Pid;
	private Patient myTargetPatient3;
	private Long myPatient3Pid;

	@Before
	public void before() {
		myDeletePerson = createPerson();
		myDeletePersonId = myDeletePerson.getIdElement().toUnqualifiedVersionless();
		myKeepPerson = createPerson();
		myKeepPersonId = myKeepPerson.getIdElement().toUnqualifiedVersionless();
		myKeepPersonPid = myIdHelperService.getPidOrThrowException(myKeepPersonId);

		myTargetPatient1 = createPatient();
		myPatient1Pid = myIdHelperService.getPidOrThrowException(myTargetPatient1.getIdElement());

		myTargetPatient2 = createPatient();
		myPatient2Pid = myIdHelperService.getPidOrThrowException(myTargetPatient2.getIdElement());

		myTargetPatient3 = createPatient();
		myPatient3Pid = myIdHelperService.getPidOrThrowException(myTargetPatient3.getIdElement());

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

		Person mergedPerson = (Person) myEmpiPersonMergerSvc.mergePersons(myDeletePerson, myKeepPerson);
		assertEquals(myKeepPerson.getIdElement(), mergedPerson.getIdElement());
		assertThat(mergedPerson, is(samePersonAs(mergedPerson)));
		assertEquals(1, getAllPersons().size());
	}

	@Test
	public void fullDeleteEmptyKeep() {
		populatePerson(myDeletePerson);

		Person mergedPerson = (Person) myEmpiPersonMergerSvc.mergePersons(myDeletePerson, myKeepPerson);
		HumanName returnedName = mergedPerson.getNameFirstRep();
		assertEquals(GIVEN_NAME, returnedName.getGivenAsSingleString());
		assertEquals(FAMILY_NAME, returnedName.getFamily());
		assertEquals(POSTAL_CODE, mergedPerson.getAddressFirstRep().getPostalCode());
	}

	@Test
	public void emptyDeleteFullKeep() {
		myDeletePerson.getName().add(new HumanName().addGiven(BAD_GIVEN_NAME));
		populatePerson(myKeepPerson);

		Person mergedPerson = (Person) myEmpiPersonMergerSvc.mergePersons(myDeletePerson, myKeepPerson);
		HumanName returnedName = mergedPerson.getNameFirstRep();
		assertEquals(GIVEN_NAME, returnedName.getGivenAsSingleString());
		assertEquals(FAMILY_NAME, returnedName.getFamily());
		assertEquals(POSTAL_CODE, mergedPerson.getAddressFirstRep().getPostalCode());
	}

	@Test
	public void deleteLinkKeepNoLink() {
		createEmpiLink(myDeletePerson, myTargetPatient1);

		myEmpiPersonMergerSvc.mergePersons(myDeletePerson, myKeepPerson);
		List<EmpiLink> links = myEmpiLinkDaoSvc.findEmpiLinksByPersonId(myKeepPerson);
		assertEquals(1, links.size());
		assertThat(myKeepPerson, is(possibleLinkedTo(myTargetPatient1)));
		assertEquals(1, myKeepPerson.getLink().size());
	}

	@Test
	public void deleteNoLinkKeepLink() {
		createEmpiLink(myKeepPerson, myTargetPatient1);

		myEmpiPersonMergerSvc.mergePersons(myDeletePerson, myKeepPerson);
		List<EmpiLink> links = myEmpiLinkDaoSvc.findEmpiLinksByPersonId(myKeepPerson);
		assertEquals(1, links.size());
		assertThat(myKeepPerson, is(possibleLinkedTo(myTargetPatient1)));
		assertEquals(1, myKeepPerson.getLink().size());
	}

	@Test
	public void delete123Keep1() {
		createEmpiLink(myDeletePerson, myTargetPatient1);
		createEmpiLink(myDeletePerson, myTargetPatient2);
		createEmpiLink(myDeletePerson, myTargetPatient3);
		createEmpiLink(myKeepPerson, myTargetPatient1);

		myEmpiPersonMergerSvc.mergePersons(myDeletePerson, myKeepPerson);
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

		myEmpiPersonMergerSvc.mergePersons(myDeletePerson, myKeepPerson);
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

		myEmpiPersonMergerSvc.mergePersons(myDeletePerson, myKeepPerson);
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

		myEmpiPersonMergerSvc.mergePersons(myDeletePerson, myKeepPerson);
		myEmpiLinkHelper.logEmpiLinks();

		assertThat(myKeepPerson, is(possibleLinkedTo(myTargetPatient1, myTargetPatient2, myTargetPatient3)));
		assertEquals(3, myKeepPerson.getLink().size());
	}

	private void createEmpiLink(Person theTheDeletePerson, Patient theTargetPatient) {
		myEmpiLinkDaoSvc.createOrUpdateLinkEntity(theTheDeletePerson, theTargetPatient, EmpiMatchResultEnum.POSSIBLE_MATCH, EmpiLinkSourceEnum.AUTO, createContextForCreate(theTargetPatient));
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
