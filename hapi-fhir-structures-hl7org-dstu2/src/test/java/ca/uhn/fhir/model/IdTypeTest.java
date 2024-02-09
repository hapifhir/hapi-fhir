package ca.uhn.fhir.model;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.dstu2.model.IdType;
import org.hl7.fhir.dstu2.model.Patient;
import org.hl7.fhir.dstu2.model.Reference;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class IdTypeTest {

	private static FhirContext ourCtx;

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(IdTypeTest.class);

	@Test
	public void testDetectLocal() {
		IdType id;
		
		id = new IdType("#123");
		assertThat(id.getValue()).isEqualTo("#123");
		assertThat(id.isLocal()).isTrue();
		
		id = new IdType("#Medication/499059CE-CDD4-48BC-9014-528A35D15CED/_history/1");
		assertThat(id.getValue()).isEqualTo("#Medication/499059CE-CDD4-48BC-9014-528A35D15CED/_history/1");
		assertThat(id.isLocal()).isTrue();

		id = new IdType("http://example.com/Patient/33#123");
		assertThat(id.getValue()).isEqualTo("http://example.com/Patient/33#123");
		assertThat(id.isLocal()).isFalse();
	}
	
	 @Test
	  public void testConstructorsWithNullArguments() {
	    IdType id = new IdType(null, null, null);
			assertThat(id.getValue()).isNull();
	  }

	@Test
	public void testDetectLocalBase() {
		assertThat(new IdType("urn:uuid:180f219f-97a8-486d-99d9-ed631fe4fc57").getValue()).isEqualTo("urn:uuid:180f219f-97a8-486d-99d9-ed631fe4fc57");
		assertThat(new IdType("urn:uuid:180f219f-97a8-486d-99d9-ed631fe4fc57").getBaseUrl()).isEqualTo("urn:uuid:");
		assertThat(new IdType("urn:uuid:180f219f-97a8-486d-99d9-ed631fe4fc57").getIdPart()).isEqualTo("180f219f-97a8-486d-99d9-ed631fe4fc57");

		assertThat(new IdType("cid:180f219f-97a8-486d-99d9-ed631fe4fc57").getValue()).isEqualTo("cid:180f219f-97a8-486d-99d9-ed631fe4fc57");
		assertThat(new IdType("cid:180f219f-97a8-486d-99d9-ed631fe4fc57").getBaseUrl()).isEqualTo("cid:");
		assertThat(new IdType("cid:180f219f-97a8-486d-99d9-ed631fe4fc57").getIdPart()).isEqualTo("180f219f-97a8-486d-99d9-ed631fe4fc57");

		assertThat(new IdType("#180f219f-97a8-486d-99d9-ed631fe4fc57").getValue()).isEqualTo("#180f219f-97a8-486d-99d9-ed631fe4fc57");
		assertThat(new IdType("#180f219f-97a8-486d-99d9-ed631fe4fc57").getBaseUrl()).isEqualTo("#");
		assertThat(new IdType("#180f219f-97a8-486d-99d9-ed631fe4fc57").getIdPart()).isEqualTo("180f219f-97a8-486d-99d9-ed631fe4fc57");
	}
	

	/**
	 * See #67
	 */
	@Test
	public void testComplicatedLocal() {
		IdType id = new IdType("#Patient/cid:Patient-72/_history/1");
		assertThat(id.isLocal()).isTrue();
		assertThat(id.getBaseUrl()).isEqualTo("#");
		assertThat(id.getResourceType()).isNull();
		assertThat(id.getVersionIdPart()).isNull();
		assertThat(id.getIdPart()).isEqualTo("Patient/cid:Patient-72/_history/1");
		
		IdType id2 = new IdType("#Patient/cid:Patient-72/_history/1");
		assertThat(id2).isEqualTo(id);
		
		id2 = id2.toUnqualified();
		assertThat(id2.isLocal()).isFalse();
		assertThat(id2.getBaseUrl()).isNull();
		assertThat(id2.getResourceType()).isNull();
		assertThat(id2.getVersionIdPart()).isNull();
		assertThat(id2.getIdPart()).isEqualTo("Patient/cid:Patient-72/_history/1");

	}

	@Test
	public void testNormal() {
		IdType id = new IdType("foo");
		assertThat(id.getValueAsString()).isEqualTo("foo");
		assertThat(id.getIdPart()).isEqualTo("foo");
		assertThat(id.toUnqualified().getValueAsString()).isEqualTo("foo");
		assertThat(id.toUnqualifiedVersionless().getValueAsString()).isEqualTo("foo");
		assertThat(id.getVersionIdPart()).isNull();
		assertThat(id.getResourceType()).isNull();
		assertThat(id.getBaseUrl()).isNull();

		assertThat(id.withResourceType("Patient").getValue()).isEqualTo("Patient/foo");
		assertThat(id.withServerBase("http://foo", "Patient").getValue()).isEqualTo("http://foo/Patient/foo");
		assertThat(id.withVersion("2").getValue()).isEqualTo("foo/_history/2");
	}

	@Test
	public void testBaseUrlFoo1() {
		IdType id = new IdType("http://my.org/foo");
		assertThat(id.getValueAsString()).isEqualTo("http://my.org/foo");
		assertThat(id.getIdPart()).isNull();
		assertThat(id.toUnqualified().getValueAsString()).isEqualTo("foo");
		assertThat(id.toUnqualifiedVersionless().getValueAsString()).isEqualTo("foo");
		assertThat(id.getVersionIdPart()).isNull();
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
		assertThat(id.getVersionIdPart()).isNull();
		assertThat(id.getResourceType()).isEqualTo("c");
		assertThat(id.getBaseUrl()).isEqualTo("http://my.org/a/b");

		assertThat(id.withResourceType("Patient").getValue()).isEqualTo("Patient/foo");
		assertThat(id.withServerBase("http://foo", "Patient").getValue()).isEqualTo("http://foo/Patient/foo");
		assertThat(id.withVersion("2").getValue()).isEqualTo("http://my.org/a/b/c/foo/_history/2");
	}

	@Test
	public void testDetermineBase() {

		IdType rr;

		rr = new IdType("http://foo/fhir/Organization/123");
		assertThat(rr.getBaseUrl()).isEqualTo("http://foo/fhir");

		rr = new IdType("http://foo/fhir/Organization/123/_history/123");
		assertThat(rr.getBaseUrl()).isEqualTo("http://foo/fhir");
		
		rr = new IdType("Organization/123/_history/123");
		assertThat(rr.getBaseUrl()).isNull();

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
		assertThat(ref.getReferenceElement().getVersionIdPart()).isNull();

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
		assertThat(ref.getReferenceElement().getResourceType()).isNull();
		assertThat(ref.getReferenceElement().getIdPart()).isEqualTo("123");
		assertThat(ref.getReferenceElement().getVersionIdPart()).isNull();

	}

	@Test
	public void testParseValueMissingType1() {
		Patient patient = new Patient();
		IdType rr = new IdType();
		rr.setValue("/123");
		patient.setManagingOrganization(new Reference(rr));

		Patient actual = parseAndEncode(patient);
		Reference ref = actual.getManagingOrganization();
		assertThat(ref.getReferenceElement().getResourceType()).isNull();
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
		assertThat(ref.getReferenceElement().getResourceType()).isNull();
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
		ourCtx = FhirContext.forDstu2Hl7Org();
	}

}
