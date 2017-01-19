package ca.uhn.fhir.context;

import static org.junit.Assert.assertEquals;

import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.rest.client.MyPatientWithExtensions;
import ca.uhn.fhir.util.TestUtil;

public class FhirContextDstu3Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirContextDstu3Test.class);

	private static FhirContext ourCtx = FhirContext.forDstu3();
	
	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}
		
	@SuppressWarnings("deprecation")
	@Test
	public void testAutoDetectVersion() {
		FhirContext ctx = new FhirContext();
		assertEquals(FhirVersionEnum.DSTU3, ctx.getVersion().getVersion());
	}

	/**
	 * See #344
	 */
	@Test
	public void testGetElementDefinitionCaseInsensitive() {
		assertEquals(Reference.class, ourCtx.getElementDefinition("reference").getImplementingClass());
		assertEquals(Reference.class, ourCtx.getElementDefinition("Reference").getImplementingClass());
		assertEquals(Reference.class, ourCtx.getElementDefinition("REFerence").getImplementingClass());
	}
	
	/**
	 * See #344
	 */
	@Test
	public void testGetResourceDefinitionCaseInsensitive() {
		assertEquals(Patient.class, ourCtx.getResourceDefinition("patient").getImplementingClass());
		assertEquals(Patient.class, ourCtx.getResourceDefinition("Patient").getImplementingClass());
		assertEquals(Patient.class, ourCtx.getResourceDefinition("PATient").getImplementingClass());
		assertEquals(StructureDefinition.class, ourCtx.getResourceDefinition("structuredefinition").getImplementingClass());
	}

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
