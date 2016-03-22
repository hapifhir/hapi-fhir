package ca.uhn.fhir.context;

import static org.junit.Assert.assertEquals;

import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.dstu3.model.Patient;
import org.junit.Test;

import ca.uhn.fhir.context.BaseRuntimeChildDatatypeDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.rest.client.MyPatientWithExtensions;

public class FhirContextDstu3Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirContextDstu3Test.class);

	private FhirContext ourCtx = FhirContext.forDstu3();
	
	@Test
	public void testCustomTypeDoesntBecomeDefault() {
		FhirContext ctx = FhirContext.forDstu3();
		
		MyPatientWithExtensions pt = new MyPatientWithExtensions();
		pt.addName().addGiven("FOO");
		ctx.newXmlParser().encodeResourceToString(pt);
		
		assertEquals(Patient.class, ctx.getResourceDefinition("Patient").getImplementingClass());
	}
	
	@Test
	public void testQueryBoundCode() {
		RuntimeResourceDefinition patientType = ourCtx.getResourceDefinition(Patient.class);
		String childName = "gender";
		BaseRuntimeChildDatatypeDefinition genderChild = (BaseRuntimeChildDatatypeDefinition) patientType.getChildByName(childName);
		ourLog.trace(genderChild.getClass().getName());
		
		assertEquals(AdministrativeGender.class, genderChild.getBoundEnumType());
	}
	
	@Test
	public void testQueryNonBoundCode() {
		RuntimeResourceDefinition patientType = ourCtx.getResourceDefinition(Patient.class);
		String childName = "name";
		BaseRuntimeChildDatatypeDefinition genderChild = (BaseRuntimeChildDatatypeDefinition) patientType.getChildByName(childName);
		ourLog.trace(genderChild.getClass().getName());
		
		assertEquals(null, genderChild.getBoundEnumType());
	}

}
