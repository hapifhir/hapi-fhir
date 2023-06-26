package org.hl7.fhir.r4.model;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.TestUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IdTypeR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(IdTypeR4Test.class);
	private static FhirContext ourCtx;

	private Patient parseAndEncode(Patient patient) {
		String encoded = ourCtx.newXmlParser().encodeResourceToString(patient);
		ourLog.info("\n" + encoded);
		return ourCtx.newXmlParser().parseResource(Patient.class, encoded);
	}

	@Test
	public void testBaseUrlFoo1() {
		IdType id = new IdType("http://my.org/foo");
		assertEquals("http://my.org/foo", id.getValueAsString());
		assertEquals(null, id.getIdPart());
		assertEquals("foo", id.toUnqualified().getValueAsString());
		assertEquals("foo", id.toUnqualifiedVersionless().getValueAsString());
		assertEquals(null, id.getVersionIdPart());
		assertEquals("foo", id.getResourceType());
		assertEquals("http://my.org", id.getBaseUrl());

		assertEquals("Patient", id.withResourceType("Patient").getValue());
		assertEquals("http://foo/Patient", id.withServerBase("http://foo", "Patient").getValue());
		assertEquals("http://my.org/foo//_history/2", id.withVersion("2").getValue());
	}

	@Test
	public void testBaseUrlFoo2() {
		IdType id = new IdType("http://my.org/a/b/c/foo");
		assertEquals("http://my.org/a/b/c/foo", id.getValueAsString());
		assertEquals("foo", id.getIdPart());
		assertEquals("c/foo", id.toUnqualified().getValueAsString());
		assertEquals("c/foo", id.toUnqualifiedVersionless().getValueAsString());
		assertEquals(null, id.getVersionIdPart());
		assertEquals("c", id.getResourceType());
		assertEquals("http://my.org/a/b", id.getBaseUrl());

		assertEquals("Patient/foo", id.withResourceType("Patient").getValue());
		assertEquals("http://foo/Patient/foo", id.withServerBase("http://foo", "Patient").getValue());
		assertEquals("http://my.org/a/b/c/foo/_history/2", id.withVersion("2").getValue());
	}

	@Test
	public void testBigDecimalIds() {

		IdType id = new IdType(new BigDecimal("123"));
		assertEquals(id.getIdPartAsBigDecimal(), new BigDecimal("123"));

	}

	/**
	 * See #67
	 */
	@Test
	public void testComplicatedLocal() {
		IdType id = new IdType("#Patient/cid:Patient-72/_history/1");
		assertTrue(id.isLocal());
		assertEquals(null, id.getBaseUrl());
		assertNull(id.getResourceType());
		assertNull(id.getVersionIdPart());
		assertEquals("#Patient/cid:Patient-72/_history/1", id.getIdPart());

		IdType id2 = new IdType("#Patient/cid:Patient-72/_history/1");
		assertEquals(id, id2);

		id2 = id2.toUnqualified();
		assertTrue(id2.isLocal());
		assertNull(id2.getBaseUrl());
		assertNull(id2.getResourceType());
		assertNull(id2.getVersionIdPart());
		assertEquals("#Patient/cid:Patient-72/_history/1", id2.getIdPart());

	}

	@Test
	public void testConstructorsWithNullArguments() {
		IdType id = new IdType(null, null, null);
		assertEquals(null, id.getValue());
	}

	@Test
	public void testDetectLocal() {
		IdType id;

		id = new IdType("#123");
		assertEquals("#123", id.getValue());
		assertTrue(id.isLocal());

		id = new IdType("#Medication/499059CE-CDD4-48BC-9014-528A35D15CED/_history/1");
		assertEquals("#Medication/499059CE-CDD4-48BC-9014-528A35D15CED/_history/1", id.getValue());
		assertTrue(id.isLocal());

		id = new IdType("http://example.com/Patient/33#123");
		assertEquals("http://example.com/Patient/33#123", id.getValue());
		assertFalse(id.isLocal());
	}

	@Test
	public void testDetectLocalBase() {
		assertEquals("urn:uuid:180f219f-97a8-486d-99d9-ed631fe4fc57", new IdType("urn:uuid:180f219f-97a8-486d-99d9-ed631fe4fc57").getValue());
		assertEquals(null, new IdType("urn:uuid:180f219f-97a8-486d-99d9-ed631fe4fc57").getBaseUrl());
		assertEquals("urn:uuid:180f219f-97a8-486d-99d9-ed631fe4fc57", new IdType("urn:uuid:180f219f-97a8-486d-99d9-ed631fe4fc57").getIdPart());

		assertEquals("cid:180f219f-97a8-486d-99d9-ed631fe4fc57", new IdType("cid:180f219f-97a8-486d-99d9-ed631fe4fc57").getValue());
		assertEquals(null, new IdType("cid:180f219f-97a8-486d-99d9-ed631fe4fc57").getBaseUrl());
		assertEquals("cid:180f219f-97a8-486d-99d9-ed631fe4fc57", new IdType("cid:180f219f-97a8-486d-99d9-ed631fe4fc57").getIdPart());

		assertEquals("#180f219f-97a8-486d-99d9-ed631fe4fc57", new IdType("#180f219f-97a8-486d-99d9-ed631fe4fc57").getValue());
		assertEquals(null, new IdType("#180f219f-97a8-486d-99d9-ed631fe4fc57").getBaseUrl());
		assertEquals("#180f219f-97a8-486d-99d9-ed631fe4fc57", new IdType("#180f219f-97a8-486d-99d9-ed631fe4fc57").getIdPart());
	}

	@Test
	public void testDetermineBase() {

		IdType rr;

		rr = new IdType("http://foo/fhir/Organization/123");
		assertEquals("http://foo/fhir", rr.getBaseUrl());

		rr = new IdType("http://foo/fhir/Organization/123/_history/123");
		assertEquals("http://foo/fhir", rr.getBaseUrl());

		rr = new IdType("Organization/123/_history/123");
		assertEquals(null, rr.getBaseUrl());

	}

	@Test
	public void testEncodeParts() {
		IdType id = new IdType("http://foo", "Patient", "123", "456");
		assertEquals("http://foo/Patient/123/_history/456", id.getValue());
		assertEquals("http://foo/Patient/123/_history/9", id.withVersion("9").getValue());
	}

	@Test
	public void testLocal() {
		IdType id = new IdType("#foo");
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
		IdType id = new IdType("foo");
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
	public void testOid() {
		IdType id = new IdType("urn:oid:1.2.3.4");
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
	public void testParseValueAbsolute() {
		Patient patient = new Patient();
		IdType rr = new IdType();
		rr.setValue("http://foo/fhir/Organization/123");

		patient.setManagingOrganization(new Reference(rr));

		Patient actual = parseAndEncode(patient);
		Reference ref = actual.getManagingOrganization();
		assertEquals("Organization", ref.getReferenceElement().getResourceType());
		assertEquals("123", ref.getReferenceElement().getIdPart());

	}

	@Test
	public void testParseValueAbsoluteWithVersion() {
		Patient patient = new Patient();
		IdType rr = new IdType();
		rr.setValue("http://foo/fhir/Organization/123/_history/999");
		patient.setManagingOrganization(new Reference(rr));

		Patient actual = parseAndEncode(patient);
		Reference ref = actual.getManagingOrganization();
		assertEquals("Organization", ref.getReferenceElement().getResourceType());
		assertEquals("123", ref.getReferenceElement().getIdPart());
		assertEquals(null, ref.getReferenceElement().getVersionIdPart());

	}

	@Test
	public void testParseValueMissingType1() {
		Patient patient = new Patient();
		IdType rr = new IdType();
		rr.setValue("/123");
		patient.setManagingOrganization(new Reference(rr));

		Patient actual = parseAndEncode(patient);
		Reference ref = actual.getManagingOrganization();
		assertEquals(null, ref.getReferenceElement().getResourceType());
		assertEquals("123", ref.getReferenceElement().getIdPart());

	}

	@Test
	public void testParseValueMissingType2() {
		Patient patient = new Patient();
		IdType rr = new IdType();
		rr.setValue("123");
		patient.setManagingOrganization(new Reference(rr));

		Patient actual = parseAndEncode(patient);
		Reference ref = actual.getManagingOrganization();
		assertEquals(null, ref.getReferenceElement().getResourceType());
		assertEquals("123", ref.getReferenceElement().getIdPart());

	}

	@Test
	public void testParseValueRelative1() {
		Patient patient = new Patient();
		IdType rr = new IdType();
		rr.setValue("Organization/123");
		patient.setManagingOrganization(new Reference(rr));

		Patient actual = parseAndEncode(patient);
		Reference ref = actual.getManagingOrganization();
		assertEquals("Organization", ref.getReferenceElement().getResourceType());
		assertEquals("123", ref.getReferenceElement().getIdPart());

	}

	@Test
	public void testParseValueRelative2() {
		Patient patient = new Patient();
		IdType rr = new IdType();
		rr.setValue("/Organization/123");
		patient.setManagingOrganization(new Reference(rr));

		Patient actual = parseAndEncode(patient);
		Reference ref = actual.getManagingOrganization();
		assertEquals("Organization", ref.getReferenceElement().getResourceType());
		assertEquals("123", ref.getReferenceElement().getIdPart());

	}

	@Test
	public void testParseValueWithVersion() {
		Patient patient = new Patient();
		IdType rr = new IdType();
		rr.setValue("/123/_history/999");
		patient.setManagingOrganization(new Reference(rr));

		Patient actual = parseAndEncode(patient);
		Reference ref = actual.getManagingOrganization();
		assertEquals(null, ref.getReferenceElement().getResourceType());
		assertEquals("123", ref.getReferenceElement().getIdPart());
		assertEquals(null, ref.getReferenceElement().getVersionIdPart());

	}

	@Test
	public void testUuid() {
		IdType id = new IdType("urn:uuid:1234-5678");
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
	public void testViewMethods() {
		IdType i = new IdType("http://foo/fhir/Organization/123/_history/999");
		assertEquals("Organization/123/_history/999", i.toUnqualified().getValue());
		assertEquals("http://foo/fhir/Organization/123", i.toVersionless().getValue());
		assertEquals("Organization/123", i.toUnqualifiedVersionless().getValue());
	}

	@Test
	public void testWithVersionNull() {
		assertEquals("Patient/123", new IdType("Patient/123/_history/2").withVersion("").getValue());
	}

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

	@BeforeAll
	public static void beforeClass() {
		ourCtx = FhirContext.forR4();
	}

}
