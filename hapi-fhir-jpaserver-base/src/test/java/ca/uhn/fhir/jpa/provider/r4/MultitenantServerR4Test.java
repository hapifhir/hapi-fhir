package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.util.TestUtil;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.junit.AfterClass;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.in;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@SuppressWarnings("Duplicates")
public class MultitenantServerR4Test extends BaseMultitenantResourceProviderR4Test implements ITestDataBuilder {

	@Test
	public void testFetchCapabilityStatement() {
		myTenantClientInterceptor.setTenantId(TENANT_A);
		CapabilityStatement cs = ourClient.capabilities().ofType(CapabilityStatement.class).execute();

		assertEquals("HAPI FHIR Server", cs.getSoftware().getName());
		assertEquals(ourServerBase + "/TENANT-A/metadata", myCapturingInterceptor.getLastRequest().getUri());
	}

	@Test
	public void testCreateAndRead() {

		// Create patients

		IIdType idA = createPatient(withTenant(TENANT_A), withActiveTrue());
		createPatient(withTenant(TENANT_B), withActiveFalse());

		// Now read back

		myTenantClientInterceptor.setTenantId(TENANT_A);
		Patient response = ourClient.read().resource(Patient.class).withId(idA).execute();
		assertTrue(response.getActive());

		myTenantClientInterceptor.setTenantId(TENANT_B);
		try {
			ourClient.read().resource(Patient.class).withId(idA).execute();
			fail();
		} catch (ResourceNotFoundException e) {
			// good
		}
	}

	@Test
	public void testCreate_InvalidTenant() {

		myTenantClientInterceptor.setTenantId("TENANT-ZZZ");
		Patient patientA = new Patient();
		patientA.setActive(true);
		try {
			ourClient.create().resource(patientA).execute();
			fail();
		} catch (ResourceNotFoundException e) {
			assertThat(e.getMessage(), containsString("Partition name \"TENANT-ZZZ\" is not valid"));
		}

	}


	@Test
	public void testTransaction() {
		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.TRANSACTION);

		Organization org = new Organization();
		org.setId(IdType.newRandomUuid());
		org.setName("org");
		input.addEntry()
			.setFullUrl(org.getId())
			.setResource(org)
			.getRequest().setUrl("Organization").setMethod(Bundle.HTTPVerb.POST);

		Patient p = new Patient();
		p.getMeta().addTag("http://system", "code", "diisplay");
		p.addName().setFamily("FAM");
		p.addIdentifier().setSystem("system").setValue("value");
		p.setBirthDate(new Date());
		p.getManagingOrganization().setReference(org.getId());
		input.addEntry()
			.setFullUrl(p.getId())
			.setResource(p)
			.getRequest().setUrl("Patient").setMethod(Bundle.HTTPVerb.POST);

		myTenantClientInterceptor.setTenantId(TENANT_A);
		Bundle response = ourClient.transaction().withBundle(input).execute();

	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
