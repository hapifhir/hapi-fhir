package ca.uhn.fhir.model;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.dstu2016may.model.IdType;
import org.hl7.fhir.dstu2016may.model.Patient;
import org.hl7.fhir.dstu2016may.model.Reference;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class IdTypeDstu2_1Test {

	private static FhirContext ourCtx;

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(IdTypeDstu2_1Test.class);

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

	@Test
	public void testUuid() {
		IdType id = new IdType("urn:uuid:1234-5678");
		assertThat(id.getValueAsString()).isEqualTo("urn:uuid:1234-5678");
		assertThat(id.getIdPart()).isEqualTo("urn:uuid:1234-5678");
		assertThat(id.toUnqualified().getValueAsString()).isEqualTo("urn:uuid:1234-5678");
		assertThat(id.toUnqualifiedVersionless().getValueAsString()).isEqualTo("urn:uuid:1234-5678");
		assertNull(id.getVersionIdPart());
		assertNull(id.getResourceType());
		assertNull(id.getBaseUrl());

		assertThat(id.withResourceType("Patient").getValue()).isEqualTo("urn:uuid:1234-5678");
		assertThat(id.withServerBase("http://foo", "Patient").getValue()).isEqualTo("urn:uuid:1234-5678");
		assertThat(id.withVersion("2").getValue()).isEqualTo("urn:uuid:1234-5678");
	}
	
	@Test
	public void testOid() {
		IdType id = new IdType("urn:oid:1.2.3.4");
		assertThat(id.getValueAsString()).isEqualTo("urn:oid:1.2.3.4");
		assertThat(id.getIdPart()).isEqualTo("urn:oid:1.2.3.4");
		assertThat(id.toUnqualified().getValueAsString()).isEqualTo("urn:oid:1.2.3.4");
		assertThat(id.toUnqualifiedVersionless().getValueAsString()).isEqualTo("urn:oid:1.2.3.4");
		assertNull(id.getVersionIdPart());
		assertNull(id.getResourceType());
		assertNull(id.getBaseUrl());

		assertThat(id.withResourceType("Patient").getValue()).isEqualTo("urn:oid:1.2.3.4");
		assertThat(id.withServerBase("http://foo", "Patient").getValue()).isEqualTo("urn:oid:1.2.3.4");
		assertThat(id.withVersion("2").getValue()).isEqualTo("urn:oid:1.2.3.4");
	}

	@Test
	public void testLocal() {
		IdType id = new IdType("#foo");
		assertThat(id.getValueAsString()).isEqualTo("#foo");
		assertThat(id.getIdPart()).isEqualTo("#foo");
		assertThat(id.toUnqualified().getValueAsString()).isEqualTo("#foo");
		assertThat(id.toUnqualifiedVersionless().getValueAsString()).isEqualTo("#foo");
		assertNull(id.getVersionIdPart());
		assertNull(id.getResourceType());
		assertNull(id.getBaseUrl());

		assertThat(id.withResourceType("Patient").getValue()).isEqualTo("#foo");
		assertThat(id.withServerBase("http://foo", "Patient").getValue()).isEqualTo("#foo");
		assertThat(id.withVersion("2").getValue()).isEqualTo("#foo");
	}

	@Test
	public void testNormal() {
		IdType id = new IdType("foo");
		assertThat(id.getValueAsString()).isEqualTo("foo");
		assertThat(id.getIdPart()).isEqualTo("foo");
		assertThat(id.toUnqualified().getValueAsString()).isEqualTo("foo");
		assertThat(id.toUnqualifiedVersionless().getValueAsString()).isEqualTo("foo");
		assertNull(id.getVersionIdPart());
		assertNull(id.getResourceType());
		assertNull(id.getBaseUrl());

		assertThat(id.withResourceType("Patient").getValue()).isEqualTo("Patient/foo");
		assertThat(id.withServerBase("http://foo", "Patient").getValue()).isEqualTo("http://foo/Patient/foo");
		assertThat(id.withVersion("2").getValue()).isEqualTo("foo/_history/2");
	}

	@Test
	public void testBaseUrlFoo1() {
		IdType id = new IdType("http://my.org/foo");
		assertThat(id.getValueAsString()).isEqualTo("http://my.org/foo");
		assertNull(id.getIdPart());
		assertThat(id.toUnqualified().getValueAsString()).isEqualTo("foo");
		assertThat(id.toUnqualifiedVersionless().getValueAsString()).isEqualTo("foo");
		assertNull(id.getVersionIdPart());
		assertThat(id.getResourceType()).isEqualTo("foo");
		assertThat(id.getBaseUrl()).isEqualTo("http://my.org");

		assertThat(id.withResourceType("Patient").getValue()).isEqualTo("Patient");
		assertThat(id.withServerBase("http://foo", "Patient").getValue()).isEqualTo("http://foo/Patient");
		assertThat(id.withVersion("2").getValue()).isEqualTo("http://my.org/foo//_history/2");
	}

	@Test
	public void testBaseUrlFoo2() {
		IdType id = new IdType("http://my.org/a/b/c/foo");
		assertThat(id.getValueAsString()).isEqualTo("http://my.org/a/b/c/foo");
		assertThat(id.getIdPart()).isEqualTo("foo");
		assertThat(id.toUnqualified().getValueAsString()).isEqualTo("c/foo");
		assertThat(id.toUnqualifiedVersionless().getValueAsString()).isEqualTo("c/foo");
		assertNull(id.getVersionIdPart());
		assertThat(id.getResourceType()).isEqualTo("c");
		assertThat(id.getBaseUrl()).isEqualTo("http://my.org/a/b");

		assertThat(id.withResourceType("Patient").getValue()).isEqualTo("Patient/foo");
		assertThat(id.withServerBase("http://foo", "Patient").getValue()).isEqualTo("http://foo/Patient/foo");
		assertThat(id.withVersion("2").getValue()).isEqualTo("http://my.org/a/b/c/foo/_history/2");
	}

	@Test
	public void testDetectLocal() {
		IdType id;
		
		id = new IdType("#123");
		assertThat(id.getValue()).isEqualTo("#123");
		assertTrue(id.isLocal());
		
		id = new IdType("#Medication/499059CE-CDD4-48BC-9014-528A35D15CED/_history/1");
		assertThat(id.getValue()).isEqualTo("#Medication/499059CE-CDD4-48BC-9014-528A35D15CED/_history/1");
		assertTrue(id.isLocal());

		id = new IdType("http://example.com/Patient/33#123");
		assertThat(id.getValue()).isEqualTo("http://example.com/Patient/33#123");
		assertFalse(id.isLocal());
	}
	
	 @Test
	  public void testConstructorsWithNullArguments() {
	    IdType id = new IdType(null, null, null);
			assertNull(id.getValue());
	  }

	@Test
	public void testDetectLocalBase() {
		assertThat(new IdType("urn:uuid:180f219f-97a8-486d-99d9-ed631fe4fc57").getValue()).isEqualTo("urn:uuid:180f219f-97a8-486d-99d9-ed631fe4fc57");
		assertNull(new IdType("urn:uuid:180f219f-97a8-486d-99d9-ed631fe4fc57").getBaseUrl());
		assertThat(new IdType("urn:uuid:180f219f-97a8-486d-99d9-ed631fe4fc57").getIdPart()).isEqualTo("urn:uuid:180f219f-97a8-486d-99d9-ed631fe4fc57");

		assertThat(new IdType("cid:180f219f-97a8-486d-99d9-ed631fe4fc57").getValue()).isEqualTo("cid:180f219f-97a8-486d-99d9-ed631fe4fc57");
		assertNull(new IdType("cid:180f219f-97a8-486d-99d9-ed631fe4fc57").getBaseUrl());
		assertThat(new IdType("cid:180f219f-97a8-486d-99d9-ed631fe4fc57").getIdPart()).isEqualTo("cid:180f219f-97a8-486d-99d9-ed631fe4fc57");

		assertThat(new IdType("#180f219f-97a8-486d-99d9-ed631fe4fc57").getValue()).isEqualTo("#180f219f-97a8-486d-99d9-ed631fe4fc57");
		assertNull(new IdType("#180f219f-97a8-486d-99d9-ed631fe4fc57").getBaseUrl());
		assertThat(new IdType("#180f219f-97a8-486d-99d9-ed631fe4fc57").getIdPart()).isEqualTo("#180f219f-97a8-486d-99d9-ed631fe4fc57");
	}
	

	/**
	 * See #67
	 */
	@Test
	public void testComplicatedLocal() {
		IdType id = new IdType("#Patient/cid:Patient-72/_history/1");
		assertTrue(id.isLocal());
		assertNull(id.getBaseUrl());
		assertNull(id.getResourceType());
		assertNull(id.getVersionIdPart());
		assertThat(id.getIdPart()).isEqualTo("#Patient/cid:Patient-72/_history/1");
		
		IdType id2 = new IdType("#Patient/cid:Patient-72/_history/1");
		assertThat(id2).isEqualTo(id);
		
		id2 = id2.toUnqualified();
		assertTrue(id2.isLocal());
		assertNull(id2.getBaseUrl());
		assertNull(id2.getResourceType());
		assertNull(id2.getVersionIdPart());
		assertThat(id2.getIdPart()).isEqualTo("#Patient/cid:Patient-72/_history/1");

	}
	
	@Test
	public void testDetermineBase() {

		IdType rr;

		rr = new IdType("http://foo/fhir/Organization/123");
		assertThat(rr.getBaseUrl()).isEqualTo("http://foo/fhir");

		rr = new IdType("http://foo/fhir/Organization/123/_history/123");
		assertThat(rr.getBaseUrl()).isEqualTo("http://foo/fhir");
		
		rr = new IdType("Organization/123/_history/123");
		assertNull(rr.getBaseUrl());

	}

	@Test
	public void testParseValueAbsolute() {
		Patient patient = new Patient();
		IdType rr = new IdType();
		rr.setValue("http://foo/fhir/Organization/123");

		patient.setManagingOrganization(new Reference(rr));

		Patient actual = parseAndEncode(patient);
		Reference ref = actual.getManagingOrganization();
		assertThat(ref.getReferenceElement().getResourceType()).isEqualTo("Organization");
		assertThat(ref.getReferenceElement().getIdPart()).isEqualTo("123");

	}

	@Test
	public void testBigDecimalIds() {

		IdType id = new IdType(new BigDecimal("123"));
		assertThat(new BigDecimal("123")).isEqualTo(id.getIdPartAsBigDecimal());

	}

	@Test
	public void testParseValueAbsoluteWithVersion() {
		Patient patient = new Patient();
		IdType rr = new IdType();
		rr.setValue("http://foo/fhir/Organization/123/_history/999");
		patient.setManagingOrganization(new Reference(rr));

		Patient actual = parseAndEncode(patient);
		Reference ref = actual.getManagingOrganization();
		assertThat(ref.getReferenceElement().getResourceType()).isEqualTo("Organization");
		assertThat(ref.getReferenceElement().getIdPart()).isEqualTo("123");
		assertNull(ref.getReferenceElement().getVersionIdPart());

	}

	
	@Test
	public void testViewMethods() {
		IdType i = new IdType("http://foo/fhir/Organization/123/_history/999");
		assertThat(i.toUnqualified().getValue()).isEqualTo("Organization/123/_history/999");
		assertThat(i.toVersionless().getValue()).isEqualTo("http://foo/fhir/Organization/123");
		assertThat(i.toUnqualifiedVersionless().getValue()).isEqualTo("Organization/123");
	}

	@Test
	public void testParseValueWithVersion() {
		Patient patient = new Patient();
		IdType rr = new IdType();
		rr.setValue("/123/_history/999");
		patient.setManagingOrganization(new Reference(rr));

		Patient actual = parseAndEncode(patient);
		Reference ref = actual.getManagingOrganization();
		assertNull(ref.getReferenceElement().getResourceType());
		assertThat(ref.getReferenceElement().getIdPart()).isEqualTo("123");
		assertNull(ref.getReferenceElement().getVersionIdPart());

	}

	@Test
	public void testParseValueMissingType1() {
		Patient patient = new Patient();
		IdType rr = new IdType();
		rr.setValue("/123");
		patient.setManagingOrganization(new Reference(rr));

		Patient actual = parseAndEncode(patient);
		Reference ref = actual.getManagingOrganization();
		assertNull(ref.getReferenceElement().getResourceType());
		assertThat(ref.getReferenceElement().getIdPart()).isEqualTo("123");

	}

	@Test
	public void testParseValueMissingType2() {
		Patient patient = new Patient();
		IdType rr = new IdType();
		rr.setValue("123");
		patient.setManagingOrganization(new Reference(rr));

		Patient actual = parseAndEncode(patient);
		Reference ref = actual.getManagingOrganization();
		assertNull(ref.getReferenceElement().getResourceType());
		assertThat(ref.getReferenceElement().getIdPart()).isEqualTo("123");

	}

	@Test
	public void testParseValueRelative1() {
		Patient patient = new Patient();
		IdType rr = new IdType();
		rr.setValue("Organization/123");
		patient.setManagingOrganization(new Reference(rr));

		Patient actual = parseAndEncode(patient);
		Reference ref = actual.getManagingOrganization();
		assertThat(ref.getReferenceElement().getResourceType()).isEqualTo("Organization");
		assertThat(ref.getReferenceElement().getIdPart()).isEqualTo("123");

	}

	@Test
	public void testParseValueRelative2() {
		Patient patient = new Patient();
		IdType rr = new IdType();
		rr.setValue("/Organization/123");
		patient.setManagingOrganization(new Reference(rr));

		Patient actual = parseAndEncode(patient);
		Reference ref = actual.getManagingOrganization();
		assertThat(ref.getReferenceElement().getResourceType()).isEqualTo("Organization");
		assertThat(ref.getReferenceElement().getIdPart()).isEqualTo("123");

	}

	private Patient parseAndEncode(Patient patient) {
		String encoded = ourCtx.newXmlParser().encodeResourceToString(patient);
		ourLog.info("\n" + encoded);
		return ourCtx.newXmlParser().parseResource(Patient.class, encoded);
	}

	@BeforeAll
	public static void beforeClass() {
		ourCtx = FhirContext.forDstu2_1();
	}

}
