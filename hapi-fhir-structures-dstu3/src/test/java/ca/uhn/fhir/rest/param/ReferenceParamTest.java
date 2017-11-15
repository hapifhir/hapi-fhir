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
	public void testWithResourceTypeAsQualifier() {
		
		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(ourCtx, null, ":Location", "123");
		assertEquals("Location", rp.getResourceType());
		assertEquals("123", rp.getIdPart());
		assertEquals("Location/123", rp.getValue());
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
