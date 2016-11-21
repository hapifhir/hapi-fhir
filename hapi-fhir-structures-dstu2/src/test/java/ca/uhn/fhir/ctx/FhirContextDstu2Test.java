package ca.uhn.fhir.ctx;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.context.BaseRuntimeChildDatatypeDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.dstu2.valueset.MaritalStatusCodesEnum;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.util.TestUtil;

public class FhirContextDstu2Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirContextDstu2Test.class);

	private static FhirContext ourCtx = FhirContext.forDstu2();

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@Test(expected=DataFormatException.class)
	public void testScanInvalid() {
		FhirContext ctx = FhirContext.forDstu2();
		ctx.getResourceDefinition("InvalidResource");
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testAutoDetectVersion() {
		FhirContext ctx = new FhirContext();
		assertEquals(FhirVersionEnum.DSTU2, ctx.getVersion().getVersion());
	}

	
	@Test
	public void testQueryBoundCode() {
		RuntimeResourceDefinition patientType = ourCtx.getResourceDefinition(Patient.class);
		String childName = "gender";
		BaseRuntimeChildDatatypeDefinition genderChild = (BaseRuntimeChildDatatypeDefinition) patientType.getChildByName(childName);
		ourLog.trace(genderChild.getClass().getName());
		
		assertEquals(AdministrativeGenderEnum.class, genderChild.getBoundEnumType());
	}
	
	@Test
	public void testQueryBoundCodeableConcept() {
		RuntimeResourceDefinition patientType = ourCtx.getResourceDefinition(Patient.class);
		String childName = "maritalStatus";
		BaseRuntimeChildDatatypeDefinition genderChild = (BaseRuntimeChildDatatypeDefinition) patientType.getChildByName(childName);
		ourLog.trace(genderChild.getClass().getName());
		
		assertEquals(MaritalStatusCodesEnum.class, genderChild.getBoundEnumType());
	}

}
