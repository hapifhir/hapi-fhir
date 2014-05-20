package ca.uhn.fhir.model.dstu.composite;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;

public class ResourceReferenceDtTest {

	private static FhirContext ourCtx;

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceReferenceDtTest.class);

	@Test
	public void testParseValueAbsolute() {
		Patient patient = new Patient();
		ResourceReferenceDt rr = new ResourceReferenceDt();
		rr.setReference("http://foo/fhir/Organization/123");
		patient.setManagingOrganization(rr);

		Patient actual = parseAndEncode(patient);
		rr = actual.getManagingOrganization();
		assertEquals(Organization.class, rr.getResourceType());
		assertEquals("123", rr.getResourceId());

	}

	@Test
	public void testParseValueMissingType1() {
		Patient patient = new Patient();
		ResourceReferenceDt rr = new ResourceReferenceDt();
		rr.setReference("/123");
		patient.setManagingOrganization(rr);

		Patient actual = parseAndEncode(patient);
		rr = actual.getManagingOrganization();
		assertEquals(null, rr.getResourceType());
		assertEquals("123", rr.getResourceId());

	}

	@Test
	public void testParseValueMissingType2() {
		Patient patient = new Patient();
		ResourceReferenceDt rr = new ResourceReferenceDt();
		rr.setReference("123");
		patient.setManagingOrganization(rr);

		Patient actual = parseAndEncode(patient);
		rr = actual.getManagingOrganization();
		assertEquals(null, rr.getResourceType());
		assertEquals("123", rr.getResourceId());

	}

	@Test
	public void testParseValueRelative1() {
		Patient patient = new Patient();
		ResourceReferenceDt rr = new ResourceReferenceDt();
		rr.setReference("Organization/123");
		patient.setManagingOrganization(rr);

		Patient actual = parseAndEncode(patient);
		rr = actual.getManagingOrganization();
		assertEquals(Organization.class, rr.getResourceType());
		assertEquals("123", rr.getResourceId());

	}

	@Test
	public void testParseValueRelative2() {
		Patient patient = new Patient();
		ResourceReferenceDt rr = new ResourceReferenceDt();
		rr.setReference("/Organization/123");
		patient.setManagingOrganization(rr);

		Patient actual = parseAndEncode(patient);
		rr = actual.getManagingOrganization();
		assertEquals(Organization.class, rr.getResourceType());
		assertEquals("123", rr.getResourceId());

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
