package ca.uhn.fhir.mdm.rules.matcher;


import ca.uhn.fhir.mdm.rules.matcher.fieldmatchers.ExtensionMatcher;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ExtensionMatcherR4Test extends BaseMatcherR4Test {

	private ExtensionMatcher myExtensionMatcher;

	@BeforeEach
	public void before() {
		super.before();

		myExtensionMatcher = new ExtensionMatcher();
	}

	@Test
	public void testPatientWithMatchingExtension(){
		Patient patient1 = new Patient();
		Patient patient2 = new Patient();

		patient1.addExtension("asd",new StringType("Patient1"));
		patient2.addExtension("asd",new StringType("Patient1"));

		assertThat(match(patient1, patient2)).isTrue();
	}

	@Test
	public void testPatientWithoutMatchingExtension(){
		Patient patient1 = new Patient();
		Patient patient2 = new Patient();

		patient1.addExtension("asd",new StringType("Patient1"));
		patient2.addExtension("asd",new StringType("Patient2"));

		assertThat(match(patient1, patient2)).isFalse();
	}

	@Test
	public void testPatientSameValueDifferentUrl(){
		Patient patient1 = new Patient();
		Patient patient2 = new Patient();

		patient1.addExtension("asd",new StringType("Patient1"));
		patient2.addExtension("asd1",new StringType("Patient1"));

		assertThat(match(patient1, patient2)).isFalse();
	}

	@Test
	public void testPatientWithMultipleExtensionOneMatching(){
		Patient patient1 = new Patient();
		Patient patient2 = new Patient();

		patient1.addExtension("asd",new StringType("Patient1"));
		patient1.addExtension("url1", new StringType("asd"));
		patient2.addExtension("asd",new StringType("Patient1"));
		patient2.addExtension("asdasd", new StringType("some value"));

		assertThat(match(patient1, patient2)).isTrue();
	}

	@Test
	public void testPatientWithoutIntExtension(){
		Patient patient1 = new Patient();
		Patient patient2 = new Patient();

		patient1.addExtension("asd", new IntegerType(123));
		patient2.addExtension("asd", new IntegerType(123));

		assertThat(match(patient1, patient2)).isTrue();
	}

	@Test
	public void testPatientWithNoExtension(){
		Patient patient1 = new Patient();
		Patient patient2 = new Patient();

		assertThat(match(patient1, patient2)).isFalse();
	}

	private boolean match(IBase theFirst, IBase theSecond) {
		return myExtensionMatcher.matches(theFirst, theSecond, myMdmMatcherJson);
	}
}
