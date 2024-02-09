package ca.uhn.fhir.model;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.dstu2016may.model.CodeableConcept;
import org.hl7.fhir.dstu2016may.model.Element;
import org.hl7.fhir.dstu2016may.model.Enumerations;
import org.hl7.fhir.dstu2016may.model.HumanName;
import org.hl7.fhir.dstu2016may.model.Patient;
import org.hl7.fhir.dstu2016may.model.Practitioner;
import org.hl7.fhir.dstu2016may.model.Practitioner.PractitionerPractitionerRoleComponent;
import org.hl7.fhir.instance.model.api.IBaseElement;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ModelDstu2_1Test {

	private static FhirContext ourCtx = FhirContext.forDstu2_1();

	@Test
	public void testElementHasInterface() {
		assertThat(IBaseElement.class.isAssignableFrom(Element.class)).isTrue();
	}
	
	
	/**
	 * See #320
	 */
	@Test
	public void testDontUseBoundCodeForExampleBinding() {
		Practitioner p = new Practitioner();
		PractitionerPractitionerRoleComponent role = p.addPractitionerRole();
		CodeableConcept roleField = role.getRole();
		assertThat(roleField.getClass()).isEqualTo(CodeableConcept.class);
	}

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}


	/**
	 * See #304
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testPopulateWrongGenericType() {
		Patient p = new Patient();
		List names = Arrays.asList("name");

		List existingNames = p.getName();
		existingNames.addAll(names);

		try {
			ourCtx.newXmlParser().encodeResourceToString(p);
		} catch (ClassCastException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1794) + "Found instance of class java.lang.String - Did you set a field value to the incorrect type? Expected org.hl7.fhir.instance.model.api.IBase");
		}
	}
	
	
//	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ModelDstu2_1Test.class);

	
	/**
	 * See #325
	 */
	@Test
	@Disabled
	public void testEqualsDeep() throws Exception {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Date date = dateFormat.parse("19920925");
		FhirContext context = FhirContext.forDstu2_1();

		Patient patient1 = new Patient();
		patient1.setBirthDate(date)
		        .setGender(Enumerations.AdministrativeGender.MALE)
		        .addName().setUse(HumanName.NameUse.OFFICIAL).addGiven("first").addGiven("second").addFamily("family");

		Patient patient2 = context.newJsonParser().parseResource(Patient.class,
		        context.newJsonParser().encodeResourceToString(patient1));

		assertThat(patient1.equalsDeep(patient2)).isTrue();
		assertThat(patient1.equalsShallow(patient2)).isTrue();

		Patient patient3 = new Patient();
		patient3.setBirthDate(date)
		        .setGender(Enumerations.AdministrativeGender.MALE)
		        .addName().setUse(HumanName.NameUse.OFFICIAL).addGiven("first").addGiven("second").addFamily("family");

		assertThat(patient1.equalsDeep(patient3)).isTrue();
		assertThat(patient1.equalsShallow(patient3)).isTrue();

		Patient patient4 = new Patient();
		patient4.setBirthDate(date)
		        .setGender(Enumerations.AdministrativeGender.MALE)
		        .addName().setUse(HumanName.NameUse.OFFICIAL).addGiven("first").addGiven("second").addFamily("family2");

		assertThat(patient1.equalsShallow(patient4)).isTrue();
		assertThat(patient1.equalsDeep(patient4)).isFalse();

		Patient patient5 = new Patient();
		patient5.setBirthDate(date)
		        .setGender(Enumerations.AdministrativeGender.FEMALE)
		        .addName().setUse(HumanName.NameUse.OFFICIAL).addGiven("first").addGiven("second").addFamily("family2");

		assertThat(patient1.equalsShallow(patient5)).isFalse();
		assertThat(patient1.equalsDeep(patient5)).isFalse();

	}

}
