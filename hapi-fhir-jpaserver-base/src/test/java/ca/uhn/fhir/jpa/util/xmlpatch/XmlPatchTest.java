package ca.uhn.fhir.jpa.util.xmlpatch;

import static org.junit.Assert.assertEquals;

import org.hl7.fhir.dstu3.model.Patient;
import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.util.xmlpatch.XmlPatchUtils;
import ca.uhn.fhir.util.TestUtil;

public class XmlPatchTest {

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
		String patchBody = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><diff xmlns:fhir=\"http://hl7.org/fhir\"><replace sel=\"fhir:Patient/fhir:active/@value\">false</replace></diff>";
		//@formatter:on
		
		Patient dest = XmlPatchUtils.apply(ourCtx, p, patchBody);
		assertEquals(false, dest.getActive());
	}
	
}
