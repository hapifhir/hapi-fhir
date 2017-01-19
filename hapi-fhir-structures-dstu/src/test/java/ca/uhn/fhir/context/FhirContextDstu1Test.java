package ca.uhn.fhir.context;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.ValueSet;
import ca.uhn.fhir.util.TestUtil;

public class FhirContextDstu1Test {

	@SuppressWarnings("deprecation")
	@Test
	public void testAutoDetectVersion() {
		FhirContext ctx = new FhirContext();
		assertEquals(FhirVersionEnum.DSTU1, ctx.getVersion().getVersion());
	}
	
	@Test
	public void testIncrementalScan() {
		FhirContext ctx = FhirContext.forDstu1();
		RuntimeResourceDefinition vsDef = ctx.getResourceDefinition(ValueSet.class);
		RuntimeResourceDefinition ptDef = ctx.getResourceDefinition(Patient.class);
		assertNotNull(ptDef);

		RuntimeResourceDefinition vsDef2 = ctx.getResourceDefinition(ValueSet.class);
		assertSame(vsDef, vsDef2);
	}

	@Test
	public void testFindBinary() {
		RuntimeResourceDefinition def = FhirContext.forDstu1().getResourceDefinition("Binary");
		assertEquals("Binary", def.getName());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetResourceDefinitionFails() {
		FhirContext.forDstu1().getResourceDefinition(IResource.class);
	}

	@Test
	public void testUnknownVersion() {
		
		try {
			Class.forName("org.hl7.fhir.dstu3.model.Patient");
			
			/*
			 * If we found this class, DSTU3 structures are on the classpath so we're probably doing
			 * the cobertura tests.. This one won't work since all structures are on the classpath for 
			 * cobertura tests
			 */
			return;
			
		} catch (ClassNotFoundException e1) {
			// good
		}
		
		try {
			new FhirContext(FhirVersionEnum.DSTU2);
			fail();
		} catch (IllegalStateException e) {
			assertThat(e.getMessage(), containsString("Could not find the HAPI-FHIR structure JAR on the classpath for version DSTU2"));
		}
	}


	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
