package ca.uhn.fhir.rest.param;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.TestUtil;

public class ReferenceParamTest {

	private FhirContext ourCtx = FhirContext.forDstu3();

	@Test
	public void testWithResourceType() {
		
		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(ourCtx, null, null, "Location/123");
		assertEquals("Location", rp.getResourceType());
		assertEquals("123", rp.getIdPart());
		assertEquals("Location/123", rp.getValue());
		assertEquals(null, rp.getQueryParameterQualifier());
		
	}

	@Test
	public void testWithResourceType_AbsoluteUrl() {
		
		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(ourCtx, null, null, "http://a.b/c/d/e");
		assertEquals("d", rp.getResourceType());
		assertEquals("e", rp.getIdPart());
		assertEquals("http://a.b/c/d/e", rp.getValue());
		assertEquals(null, rp.getQueryParameterQualifier());
		
	}

	@Test
	public void testWithNoResourceTypeAsQualifierAndChain() {

		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(ourCtx, null, ".name", "FOO");
		assertEquals(null, rp.getResourceType());
		assertEquals("FOO", rp.getIdPart());
		assertEquals("FOO", rp.getValue());
		assertEquals(".name", rp.getQueryParameterQualifier());
		assertEquals("name", rp.getChain());

	}

	@Test
	public void testWithNoResourceTypeAsQualifierAndChain_RelativeUrl() {

		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(ourCtx, null, ".name", "Patient/1233");
		assertEquals(null, rp.getResourceType());
		assertEquals("Patient/1233", rp.getIdPart());
		assertEquals("Patient/1233", rp.getValue());
		assertEquals(".name", rp.getQueryParameterQualifier());
		assertEquals("name", rp.getChain());

	}

	@Test
	public void testWithNoResourceTypeAsQualifierAndChain_AbsoluteUrl() {

		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(ourCtx, null, ".name", "http://something.strange/a/b/c");
		assertEquals(null, rp.getResourceType());
		assertEquals("http://something.strange/a/b/c", rp.getIdPart());
		assertEquals("http://something.strange/a/b/c", rp.getValue());
		assertEquals(".name", rp.getQueryParameterQualifier());
		assertEquals("name", rp.getChain());

	}
	
	@Test
	public void testWithResourceTypeAsQualifier() {
		
		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(ourCtx, null, ":Location", "123");
		assertEquals("Location", rp.getResourceType());
		assertEquals("123", rp.getIdPart());
		assertEquals("Location/123", rp.getValue());
		assertEquals(null, rp.getQueryParameterQualifier());

	}

	// TODO: verify this behavior is correct. If type is explicitly specified (i.e. :Location), should it be
	// an error if it gets overriden by the resourceType in the url?
	@Test
	public void testWithResourceTypeAsQualifier_RelativeUrl() {
		
		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(ourCtx, null, ":Location", "Patient/123");
		assertEquals("Patient", rp.getResourceType());
		assertEquals("123", rp.getIdPart());
		assertEquals("Patient/123", rp.getValue());
		assertEquals(null, rp.getQueryParameterQualifier());

	}

	// TODO: verify this behavior is correct. Same case as testWithResourceTypeAsQualifier_RelativeUrl()
	@Test
	public void testWithResourceTypeAsQualifier_AbsoluteUrl() {
		
		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(ourCtx, null, ":Location", "http://a.b/c/d/e");
		assertEquals("d", rp.getResourceType());
		assertEquals("e", rp.getIdPart());
		assertEquals("http://a.b/c/d/e", rp.getValue());
		assertEquals(null, rp.getQueryParameterQualifier());

	}


	@Test
	public void testWithResourceTypeAsQualifierAndChain() {

		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(ourCtx, null, ":Location.name", "FOO");
		assertEquals("Location", rp.getResourceType());
		assertEquals("FOO", rp.getIdPart());
		assertEquals("Location/FOO", rp.getValue());
		assertEquals(":Location.name", rp.getQueryParameterQualifier());
		assertEquals("name", rp.getChain());

	}

	@Test
	public void testWithResourceTypeAsQualifierAndChain_IdentifierUrlAndValue() {

		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(ourCtx, null, ":Patient.identifier", "http://hey.there/a/b|123");
		assertEquals("Patient", rp.getResourceType());
		assertEquals("http://hey.there/a/b|123", rp.getIdPart());
		assertEquals("Patient/http://hey.there/a/b|123", rp.getValue());
		assertEquals(":Patient.identifier", rp.getQueryParameterQualifier());
		assertEquals("identifier", rp.getChain());

	}

	@Test
	public void testWithResourceTypeAsQualifierAndChain_IdentifierUrlOnly() {

		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(ourCtx, null, ":Patient.identifier", "http://hey.there/a/b|");
		assertEquals("Patient", rp.getResourceType());
		assertEquals("http://hey.there/a/b|", rp.getIdPart());
		assertEquals("Patient/http://hey.there/a/b|", rp.getValue());
		assertEquals(":Patient.identifier", rp.getQueryParameterQualifier());
		assertEquals("identifier", rp.getChain());

	}

	@Test
	public void testWithResourceTypeAsQualifierAndChain_ValueOnlyNoUrl() {

		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(ourCtx, null, ":Patient.identifier", "|abc");
		assertEquals("Patient", rp.getResourceType());
		assertEquals("|abc", rp.getIdPart());
		assertEquals("Patient/|abc", rp.getValue());
		assertEquals(":Patient.identifier", rp.getQueryParameterQualifier());
		assertEquals("identifier", rp.getChain());

	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
