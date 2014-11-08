package ca.uhn.fhir.model.primitive;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Patient;

public class IdDtTest {

	private static FhirContext ourCtx;

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(IdDtTest.class);

	@Test
	public void testDetectLocal() {
		
		IdDt id = new IdDt("#123");
		assertEquals("#123", id.getValue());
		assertTrue(id.isLocal());
		
	}
	
	@Test
	public void testDetermineBase() {

		IdDt rr;

		rr = new IdDt("http://foo/fhir/Organization/123");
		assertEquals("http://foo/fhir", rr.getBaseUrl());

		rr = new IdDt("http://foo/fhir/Organization/123/_history/123");
		assertEquals("http://foo/fhir", rr.getBaseUrl());
		
		rr = new IdDt("Organization/123/_history/123");
		assertEquals(null, rr.getBaseUrl());

	}

	@Test
	public void testParseValueAbsolute() {
		Patient patient = new Patient();
		IdDt rr = new IdDt();
		rr.setValue("http://foo/fhir/Organization/123");

		patient.setManagingOrganization(new ResourceReferenceDt(rr));

		Patient actual = parseAndEncode(patient);
		ResourceReferenceDt ref = actual.getManagingOrganization();
		assertEquals("Organization", ref.getReference().getResourceType());
		assertEquals("123", ref.getReference().getIdPart());

	}

	@Test
	public void testBigDecimalIds() {

		IdDt id = new IdDt(new BigDecimal("123"));
		assertEquals(id.getIdPartAsBigDecimal(), new BigDecimal("123"));

	}

	@Test
	public void testParseValueAbsoluteWithVersion() {
		Patient patient = new Patient();
		IdDt rr = new IdDt();
		rr.setValue("http://foo/fhir/Organization/123/_history/999");
		patient.setManagingOrganization(new ResourceReferenceDt(rr));

		Patient actual = parseAndEncode(patient);
		ResourceReferenceDt ref = actual.getManagingOrganization();
		assertEquals("Organization", ref.getReference().getResourceType());
		assertEquals("123", ref.getReference().getIdPart());
		assertEquals("999", ref.getReference().getVersionIdPart());

	}

	@Test
	public void testParseValueWithVersion() {
		Patient patient = new Patient();
		IdDt rr = new IdDt();
		rr.setValue("/123/_history/999");
		patient.setManagingOrganization(new ResourceReferenceDt(rr));

		Patient actual = parseAndEncode(patient);
		ResourceReferenceDt ref = actual.getManagingOrganization();
		assertEquals(null, ref.getReference().getResourceType());
		assertEquals("123", ref.getReference().getIdPart());
		assertEquals("999", ref.getReference().getVersionIdPart());

	}

	@Test
	public void testParseValueMissingType1() {
		Patient patient = new Patient();
		IdDt rr = new IdDt();
		rr.setValue("/123");
		patient.setManagingOrganization(new ResourceReferenceDt(rr));

		Patient actual = parseAndEncode(patient);
		ResourceReferenceDt ref = actual.getManagingOrganization();
		assertEquals(null, ref.getReference().getResourceType());
		assertEquals("123", ref.getReference().getIdPart());

	}

	@Test
	public void testParseValueMissingType2() {
		Patient patient = new Patient();
		IdDt rr = new IdDt();
		rr.setValue("123");
		patient.setManagingOrganization(new ResourceReferenceDt(rr));

		Patient actual = parseAndEncode(patient);
		ResourceReferenceDt ref = actual.getManagingOrganization();
		assertEquals(null, ref.getReference().getResourceType());
		assertEquals("123", ref.getReference().getIdPart());

	}

	@Test
	public void testParseValueRelative1() {
		Patient patient = new Patient();
		IdDt rr = new IdDt();
		rr.setValue("Organization/123");
		patient.setManagingOrganization(new ResourceReferenceDt(rr));

		Patient actual = parseAndEncode(patient);
		ResourceReferenceDt ref = actual.getManagingOrganization();
		assertEquals("Organization", ref.getReference().getResourceType());
		assertEquals("123", ref.getReference().getIdPart());

	}

	@Test
	public void testParseValueRelative2() {
		Patient patient = new Patient();
		IdDt rr = new IdDt();
		rr.setValue("/Organization/123");
		patient.setManagingOrganization(new ResourceReferenceDt(rr));

		Patient actual = parseAndEncode(patient);
		ResourceReferenceDt ref = actual.getManagingOrganization();
		assertEquals("Organization", ref.getReference().getResourceType());
		assertEquals("123", ref.getReference().getIdPart());

	}

	private Patient parseAndEncode(Patient patient) {
		String encoded = ourCtx.newXmlParser().encodeResourceToString(patient);
		ourLog.info("\n" + encoded);
		return ourCtx.newXmlParser().parseResource(Patient.class, encoded);
	}

	@BeforeClass
	public static void beforeClass() {
		ourCtx = new FhirContext();
	}

}
