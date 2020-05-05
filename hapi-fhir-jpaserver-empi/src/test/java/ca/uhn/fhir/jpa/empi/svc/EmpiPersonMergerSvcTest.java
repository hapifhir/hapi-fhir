package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Person;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class EmpiPersonMergerSvcTest extends BaseEmpiR4Test {
	public static final String GIVEN_NAME = "Jenn";
	public static final String FAMILY_NAME = "Chan";
	public static final String POSTAL_CODE = "M6G 1B4";
	@Autowired
	EmpiPersonMergerSvcImpl myEmpiPersonMergerSvc;

	@Test
	public void emptyMerge() {
		Person deletePerson = createPerson();
		Person keepPerson = createPerson();
		Person returnedPerson = (Person) myEmpiPersonMergerSvc.mergePersons(deletePerson, keepPerson);
		assertEquals(keepPerson.getIdElement(), returnedPerson.getIdElement());
		assertThat(returnedPerson, is(samePersonAs(returnedPerson)));
	}

	@Test
	public void fullDeleteEmptyKeep() {
		Person deletePerson = createPerson();
		populatePerson(deletePerson);
		Person keepPerson = createPerson();
		Person returnedPerson = (Person) myEmpiPersonMergerSvc.mergePersons(deletePerson, keepPerson);
		HumanName returnedName = returnedPerson.getNameFirstRep();
		assertEquals(GIVEN_NAME, returnedName.getGivenAsSingleString());
		assertEquals(FAMILY_NAME, returnedName.getFamily());
		assertEquals(POSTAL_CODE, returnedPerson.getAddressFirstRep().getPostalCode());
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
