package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.api.IEmpiPersonMergerSvc;
import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
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

	private Person myDeletePerson;
	private Person myKeepPerson;
	private IdType myDeletePersonId;
	private IdType myKeepPersonId;
	private Long myKeepPersonPid;
	private Patient myTargetPatient;
	private Long myPatientPid;

	@Before
	public void before() {
		myDeletePerson = createPerson();
		myDeletePersonId = myDeletePerson.getIdElement().toUnqualifiedVersionless();
		myKeepPerson = createPerson();
		myKeepPersonId = myKeepPerson.getIdElement().toUnqualifiedVersionless();
		myKeepPersonPid = myIdHelperService.getPidOrThrowException(myKeepPersonId);
		myTargetPatient = createPatient();
		myPatientPid = myIdHelperService.getPidOrThrowException(myTargetPatient.getIdElement());

	}
	
	@Test
	public void emptyMerge() {
		Person mergedPerson = (Person) myEmpiPersonMergerSvc.mergePersons(myDeletePerson, myKeepPerson);
		assertEquals(myKeepPerson.getIdElement(), mergedPerson.getIdElement());
		assertThat(mergedPerson, is(samePersonAs(mergedPerson)));
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
		myEmpiLinkDaoSvc.createOrUpdateLinkEntity(myDeletePerson, myTargetPatient, EmpiMatchResultEnum.MATCH, EmpiLinkSourceEnum.MANUAL, null);

		myEmpiPersonMergerSvc.mergePersons(myDeletePerson, myKeepPerson);
		List<EmpiLink> links = myEmpiLinkDao.findAll();
		assertEquals(1, links.size());
		EmpiLink link = links.get(0);
		assertEquals(myKeepPersonPid, link.getPersonPid());
		assertEquals(myPatientPid, link.getTargetPid());
	}

	@Test
	public void deleteNoLinkKeepLink() {
		myEmpiLinkDaoSvc.createOrUpdateLinkEntity(myKeepPerson, myTargetPatient, EmpiMatchResultEnum.MATCH, EmpiLinkSourceEnum.MANUAL, null);

		myEmpiPersonMergerSvc.mergePersons(myDeletePerson, myKeepPerson);
		List<EmpiLink> links = myEmpiLinkDao.findAll();
		assertEquals(1, links.size());
		EmpiLink link = links.get(0);
		assertEquals(myKeepPersonPid, link.getPersonPid());
		assertEquals(myPatientPid, link.getTargetPid());
	}

	// FIXME KHS we've tested 01, 10, now test 21,12,etc

	// FIXME KHS test delete

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
