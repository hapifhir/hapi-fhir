package ca.uhn.fhir.model.dstu2;

import ca.uhn.fhir.context.BaseRuntimeDeclaredChildDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.composite.MetaDt;
import ca.uhn.fhir.model.dstu2.resource.Appointment;
import ca.uhn.fhir.model.dstu2.resource.Claim;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.resource.Practitioner;
import ca.uhn.fhir.model.dstu2.resource.Practitioner.PractitionerRole;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.TestUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ModelDstu2Test {

	private static FhirContext ourCtx = FhirContext.forDstu2();

	@Test
	public void testCompositeNames() {
		assertEquals(MetaDt.class, ourCtx.getElementDefinition("meta").getImplementingClass());
	}

	@Test
	public void testModelBindings() {
		FhirTerser t = ourCtx.newTerser();
		RuntimeResourceDefinition def = ourCtx.getResourceDefinition(Patient.class);
		assertEquals("http://hl7.org/fhir/ValueSet/administrative-gender", ((BaseRuntimeDeclaredChildDefinition)def.getChildByName("gender")).getBindingValueSet());
		assertEquals("http://hl7.org/fhir/ValueSet/link-type", ((BaseRuntimeDeclaredChildDefinition)t.getDefinition(Patient.class, "Patient.link.type")).getBindingValueSet());

		def = ourCtx.getResourceDefinition(Appointment.class);
		assertEquals("http://hl7.org/fhir/ValueSet/appointmentstatus", ((BaseRuntimeDeclaredChildDefinition)def.getChildByName("status")).getBindingValueSet());
	}

	/**
	 * See #320
	 */
	@Test
	public void testDontUseBoundCodeForExampleBinding() {
		Practitioner p = new Practitioner();
		PractitionerRole role = p.addPractitionerRole();
		CodeableConceptDt roleField = role.getRole();
		assertEquals(CodeableConceptDt.class, roleField.getClass());
	}

	/**
	 * See #304
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testPopulateWrongGenericType() {
		Patient p = new Patient();
		List names = Arrays.asList("name");
		p.setName(names);

		try {
			ourCtx.newXmlParser().encodeResourceToString(p);
		} catch (ClassCastException e) {
			assertEquals(Msg.code(1748) + "Found instance of class java.lang.String - Did you set a field value to the incorrect type? Expected org.hl7.fhir.instance.model.api.IBase", e.getMessage());
		}
	}


	@Test
	public void testInstantPrecision() {
		new InstantDt("2019-01-01T00:00:00Z");
		new InstantDt("2019-01-01T00:00:00.0Z");
		new InstantDt("2019-01-01T00:00:00.000Z");
		try {
			new InstantDt("2019-01-01T00:00Z");
			fail();
		} catch (DataFormatException e) {
			// good
		}
	}


	/**
	 * See #354
	 */
	@Test
	public void testSetters() {
		Claim claim = new Claim();
		claim.setIdentifier(new ArrayList<IdentifierDt>()).setCondition(new ArrayList<CodingDt>());
	}
	
	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}


}
