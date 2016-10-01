package ca.uhn.fhir.model;

import static org.junit.Assert.*;

import org.hl7.fhir.dstu3.model.ExplanationOfBenefit;
import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.TestUtil;

public class XhtmlNodeTest {


	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	private static FhirContext ourCtx = FhirContext.forDstu3();
	
	
	/**
	 * See #443
	 */
	@Test
	public void testDeepEquals() {
		String input = 
			"<ExplanationOfBenefit xmlns=\"http://hl7.org/fhir\">" +
			"<text>" +
			  "<status value=\"generated\"/>" + 
			  "<div xmlns=\"http://www.w3.org/1999/xhtml\">A human-readable rendering of the ExplanationOfBenefit</div>" +
			"</text>" +
			"</ExplanationOfBenefit>";
		
		ExplanationOfBenefit copy1 = ourCtx.newXmlParser().parseResource(ExplanationOfBenefit.class, input);
		ExplanationOfBenefit copy2 = ourCtx.newXmlParser().parseResource(ExplanationOfBenefit.class, input);
		
		assertTrue(copy1.equalsDeep(copy2));
		assertTrue(copy1.equalsShallow(copy2));
		
	}
	
}
