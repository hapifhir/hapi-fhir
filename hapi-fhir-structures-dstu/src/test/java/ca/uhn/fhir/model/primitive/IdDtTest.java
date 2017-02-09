package ca.uhn.fhir.model.primitive;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.util.TestUtil;

public class IdDtTest {

	private static FhirContext ourCtx;

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(IdDtTest.class);

	@Test
	public void testUuid() {
		IdDt id = new IdDt("urn:uuid:1234-5678");
		assertEquals("urn:uuid:1234-5678", id.getValueAsString());
		assertEquals("urn:uuid:1234-5678", id.getIdPart());
		assertEquals("urn:uuid:1234-5678", id.toUnqualified().getValueAsString());
		assertEquals("urn:uuid:1234-5678", id.toUnqualifiedVersionless().getValueAsString());
		assertEquals(null, id.getVersionIdPart());
		assertEquals(null, id.getResourceType());
		assertEquals(null, id.getBaseUrl());
		
		assertEquals("urn:uuid:1234-5678", id.withResourceType("Patient").getValue());
		assertEquals("urn:uuid:1234-5678", id.withServerBase("http://foo", "Patient").getValue());
		assertEquals("urn:uuid:1234-5678", id.withVersion("2").getValue());
	}
	
	@Test
	public void testOid() {
		IdDt id = new IdDt("urn:oid:1.2.3.4");
		assertEquals("urn:oid:1.2.3.4", id.getValueAsString());
		assertEquals("urn:oid:1.2.3.4", id.getIdPart());
		assertEquals("urn:oid:1.2.3.4", id.toUnqualified().getValueAsString());
		assertEquals("urn:oid:1.2.3.4", id.toUnqualifiedVersionless().getValueAsString());
		assertEquals(null, id.getVersionIdPart());
		assertEquals(null, id.getResourceType());
		assertEquals(null, id.getBaseUrl());
		
		assertEquals("urn:oid:1.2.3.4", id.withResourceType("Patient").getValue());
		assertEquals("urn:oid:1.2.3.4", id.withServerBase("http://foo", "Patient").getValue());
		assertEquals("urn:oid:1.2.3.4", id.withVersion("2").getValue());
	}

	@Test
	public void testLocal() {
		IdDt id = new IdDt("#foo");
		assertEquals("#foo", id.getValueAsString());
		assertEquals("#foo", id.getIdPart());
		assertEquals("#foo", id.toUnqualified().getValueAsString());
		assertEquals("#foo", id.toUnqualifiedVersionless().getValueAsString());
		assertEquals(null, id.getVersionIdPart());
		assertEquals(null, id.getResourceType());
		assertEquals(null, id.getBaseUrl());
		
		assertEquals("#foo", id.withResourceType("Patient").getValue());
		assertEquals("#foo", id.withServerBase("http://foo", "Patient").getValue());
		assertEquals("#foo", id.withVersion("2").getValue());
	}

	@Test
	public void testNormal() {
		IdDt id = new IdDt("foo");
		assertEquals("foo", id.getValueAsString());
		assertEquals("foo", id.getIdPart());
		assertEquals("foo", id.toUnqualified().getValueAsString());
		assertEquals("foo", id.toUnqualifiedVersionless().getValueAsString());
		assertEquals(null, id.getVersionIdPart());
		assertEquals(null, id.getResourceType());
		assertEquals(null, id.getBaseUrl());
		
		assertEquals("Patient/foo", id.withResourceType("Patient").getValue());
		assertEquals("http://foo/Patient/foo", id.withServerBase("http://foo", "Patient").getValue());
		assertEquals("foo/_history/2", id.withVersion("2").getValue());
	}

	
	@Test
	public void testDetectIsIdPartValid() {
		assertTrue(new IdDt("0").isIdPartValid());
		assertTrue(new IdDt("0a").isIdPartValid());
		assertTrue(new IdDt("0abgZZ").isIdPartValid());
		assertTrue(new IdDt("---").isIdPartValid());
		assertTrue(new IdDt("1.2.3.4").isIdPartValid());
		
		assertFalse(new IdDt(" 1").isIdPartValid());
		assertFalse(new IdDt("1:1").isIdPartValid());
		assertFalse(new IdDt(StringUtils.leftPad("", 65, '0')).isIdPartValid());
	}
	
	@Test
	public void testConstructorsWithNullArguments() {
		IdDt id = new IdDt(null, null, null);
		assertEquals(null, id.getValue());
	}
	
	@Test
	public void testDetectLocal() {
		IdDt id;
		
		id = new IdDt("#123");
		assertEquals("#123", id.getValue());
		assertEquals("#123", id.toUnqualifiedVersionless().getValue());
		assertTrue(id.isLocal());
		
		id = new IdDt("#Medication/499059CE-CDD4-48BC-9014-528A35D15CED/_history/1");
		assertEquals("#Medication/499059CE-CDD4-48BC-9014-528A35D15CED/_history/1", id.getValue());
		assertTrue(id.isLocal());

		id = new IdDt("http://example.com/Patient/33#123");
		assertEquals("http://example.com/Patient/33#123", id.getValue());
		assertFalse(id.isLocal());
	}
	
	@Test
	public void testDetectLocalBase() {
		assertEquals("urn:uuid:180f219f-97a8-486d-99d9-ed631fe4fc57", new IdDt("urn:uuid:180f219f-97a8-486d-99d9-ed631fe4fc57").getValue());
		assertEquals(null, new IdDt("urn:uuid:180f219f-97a8-486d-99d9-ed631fe4fc57").getBaseUrl());
		assertEquals("urn:uuid:180f219f-97a8-486d-99d9-ed631fe4fc57", new IdDt("urn:uuid:180f219f-97a8-486d-99d9-ed631fe4fc57").getIdPart());

		assertEquals("cid:180f219f-97a8-486d-99d9-ed631fe4fc57", new IdDt("cid:180f219f-97a8-486d-99d9-ed631fe4fc57").getValue());
		assertEquals(null, new IdDt("cid:180f219f-97a8-486d-99d9-ed631fe4fc57").getBaseUrl());
		assertEquals("cid:180f219f-97a8-486d-99d9-ed631fe4fc57", new IdDt("cid:180f219f-97a8-486d-99d9-ed631fe4fc57").getIdPart());

		assertEquals("#180f219f-97a8-486d-99d9-ed631fe4fc57", new IdDt("#180f219f-97a8-486d-99d9-ed631fe4fc57").getValue());
		assertEquals(null, new IdDt("#180f219f-97a8-486d-99d9-ed631fe4fc57").getBaseUrl());
		assertEquals("#180f219f-97a8-486d-99d9-ed631fe4fc57", new IdDt("#180f219f-97a8-486d-99d9-ed631fe4fc57").getIdPart());
	}
	

	/**
	 * See #67
	 */
	@Test
	public void testComplicatedLocal() {
		IdDt id = new IdDt("#Patient/cid:Patient-72/_history/1");
		assertTrue(id.isLocal());
		assertEquals(null, id.getBaseUrl());
		assertNull(id.getResourceType());
		assertNull(id.getVersionIdPart());
		assertEquals("#Patient/cid:Patient-72/_history/1", id.getIdPart());
		
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
		assertEquals(null, ref.getReference().getVersionIdPart());

	}

	
	@Test
	public void testViewMethods() {
		IdDt i = new IdDt("http://foo/fhir/Organization/123/_history/999");
		assertEquals("Organization/123/_history/999", i.toUnqualified().getValue());
		assertEquals("http://foo/fhir/Organization/123", i.toVersionless().getValue());
		assertEquals("Organization/123", i.toUnqualifiedVersionless().getValue());
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
		assertEquals(null, ref.getReference().getVersionIdPart());

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
		ourCtx = FhirContext.forDstu1();
	}


	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
