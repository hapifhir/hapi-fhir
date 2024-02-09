package ca.uhn.fhir.mdm.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.FhirTerser;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class PrimitiveTypeEqualsPredicateTest {

	private static FhirContext myFhirContext;

	private FhirTerser myTerser;

	private IBase myPositiveTest1;

	private IBase myPositiveTest2;

	private IBase myPositiveTest3;

	private IBase myNegativeTest;

	private PrimitiveTypeEqualsPredicate cut = new PrimitiveTypeEqualsPredicate();

	@BeforeAll
	public static void initContext() {
		myFhirContext = FhirContext.forR4();
	}

	@BeforeEach
	public void init() {
		myTerser = myFhirContext.newTerser();

		myPositiveTest1 = newPatient();
		myPositiveTest2 = newPatient();
		myPositiveTest3 = newPatient();

		Patient inactivePatientForNegativeTest = newPatient();
		inactivePatientForNegativeTest.setActive(false);
		inactivePatientForNegativeTest.setMultipleBirth(new BooleanType(false));
		myNegativeTest = inactivePatientForNegativeTest;
	}

	private Patient newPatient() {
		Patient patient;
		patient = new Patient();
		patient.setActive(true);
		patient.setGender(Enumerations.AdministrativeGender.FEMALE);
		patient.setBirthDateElement(new DateType("1901-01-01"));

		Address address = new Address();
		address.addLine("Somwhere");
		address.setCity("Toronto");
		address.setCountry("Canada");
		patient.setAddress(Collections.singletonList(address));
		return patient;
	}

	@Test
	public void testNegativeMatchOnTheSameType() {
		assertThat(cut.test(myPositiveTest1, myNegativeTest)).isFalse();
		assertThat(cut.test(myNegativeTest, myPositiveTest1)).isFalse();
	}

	@Test
	public void testNegativeMatchOnDifferentTypes() {
		Person person = new Person();
		person.addName().addGiven("John");
		assertThat(cut.test(myNegativeTest, person)).isFalse();
	}

	@Test
	public void testNulls() {
		assertThat(cut.test(null, null)).isTrue();
		assertThat(cut.test(myPositiveTest1, null)).isFalse();
		assertThat(cut.test(null, myPositiveTest1)).isFalse();
	}

	@Test
	public void testPositiveMatchOnTheSameType() {
		assertThat(cut.test(myPositiveTest1, myPositiveTest2)).isTrue();
		assertThat(cut.test(myPositiveTest1, myPositiveTest1)).isTrue();
	}

}
