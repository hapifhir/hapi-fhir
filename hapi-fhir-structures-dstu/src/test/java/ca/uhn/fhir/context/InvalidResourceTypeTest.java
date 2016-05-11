package ca.uhn.fhir.context;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.util.TestUtil;

public class InvalidResourceTypeTest {

	private static FhirContext ourCtx = FhirContext.forDstu1();
	
	@Test
	public void testNonInstantiableType() {
		try {
		ourCtx.getResourceDefinition(NonInstantiableType.class);
		fail();
		} catch (ConfigurationException e) {
			assertThat(e.getMessage(), containsString("Check that this class has a no-argument"));
		}
	}
	
    @ResourceDef(name = "Patient", id="CustomPatient")
	class NonInstantiableType extends Patient
	{
    	// nothing
	}


 	@AfterClass
 	public static void afterClassClearContext() {
 		TestUtil.clearAllStaticFieldsForUnitTest();
 	}

}
