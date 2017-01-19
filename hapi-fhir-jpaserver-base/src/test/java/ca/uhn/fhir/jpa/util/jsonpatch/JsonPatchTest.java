package ca.uhn.fhir.jpa.util.jsonpatch;

import static org.junit.Assert.*;

import org.hl7.fhir.dstu3.model.Patient;
import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.TestUtil;

public class JsonPatchTest {

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	private static FhirContext ourCtx = FhirContext.forDstu3();
	
	@Test
	public void testPatchReplace() {
		Patient p = new Patient();
		p.setActive(true);
		
		//@formatter:off
		String patchBody = "[\n" + 
				"  { \"op\":\"replace\", \"path\":\"/active\", \"value\":false }\n" + 
				"]";
		//@formatter:on
		
		Patient dest = JsonPatchUtils.apply(ourCtx, p, patchBody);
		assertEquals(false, dest.getActive());
	}
	
}
