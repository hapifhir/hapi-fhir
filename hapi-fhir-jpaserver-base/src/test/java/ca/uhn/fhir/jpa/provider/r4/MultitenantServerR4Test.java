package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.util.ProviderConstants;
import ca.uhn.fhir.jpa.partition.PartitionManagementProvider;
import ca.uhn.fhir.rest.server.interceptor.partition.RequestTenantPartitionInterceptor;
import ca.uhn.fhir.jpa.util.TestUtil;
import ca.uhn.fhir.rest.client.interceptor.CapturingInterceptor;
import ca.uhn.fhir.rest.client.interceptor.UrlTenantSelectionInterceptor;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.tenant.UrlBaseTenantIdentificationStrategy;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static ca.uhn.fhir.jpa.partition.PartitionLookupSvcImpl.DEFAULT_PERSISTED_PARTITION_NAME;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@SuppressWarnings("Duplicates")
public class MultitenantServerR4Test extends BaseResourceProviderR4Test {

	@Autowired
	private RequestTenantPartitionInterceptor myRequestTenantPartitionInterceptor;
	@Autowired
	private PartitionManagementProvider myPartitionManagementProvider;

	private CapturingInterceptor myCapturingInterceptor;
	private UrlTenantSelectionInterceptor myTenantInterceptor;

	@Override
	@Before
	public void before() throws Exception {
		super.before();

		myPartitionSettings.setPartitioningEnabled(true);
		ourRestServer.registerInterceptor(myRequestTenantPartitionInterceptor);
		ourRestServer.registerProvider(myPartitionManagementProvider);
		ourRestServer.setTenantIdentificationStrategy(new UrlBaseTenantIdentificationStrategy());

		myCapturingInterceptor = new CapturingInterceptor();
		ourClient.getInterceptorService().registerInterceptor(myCapturingInterceptor);

		myTenantInterceptor = new UrlTenantSelectionInterceptor();
		ourClient.getInterceptorService().registerInterceptor(myTenantInterceptor);

		createTenants();
	}

	@Override
	@After
	public void after() throws Exception {
		super.after();

		myPartitionSettings.setPartitioningEnabled(new PartitionSettings().isPartitioningEnabled());
		ourRestServer.unregisterInterceptor(myRequestTenantPartitionInterceptor);
		ourRestServer.unregisterProvider(myPartitionManagementProvider);
		ourRestServer.setTenantIdentificationStrategy(null);

		ourClient.getInterceptorService().unregisterAllInterceptors();
	}

	@Override
	protected boolean shouldLogClient() {
		return true;
	}

	@Test
	public void testFetchCapabilityStatement() {
		myTenantInterceptor.setTenantId("TENANT-A");
		CapabilityStatement cs = ourClient.capabilities().ofType(CapabilityStatement.class).execute();

		assertEquals("HAPI FHIR Server", cs.getSoftware().getName());
		assertEquals(ourServerBase + "/TENANT-A/metadata", myCapturingInterceptor.getLastRequest().getUri());
	}

	@Test
	public void testCreateAndRead() {

		myTenantInterceptor.setTenantId("TENANT-A");
		Patient patientA = new Patient();
		patientA.setActive(true);
		IIdType idA = ourClient.create().resource(patientA).execute().getId().toUnqualifiedVersionless();

		myTenantInterceptor.setTenantId("TENANT-B");
		Patient patientB = new Patient();
		patientB.setActive(true);
		ourClient.create().resource(patientB).execute();

		// Now read back

		myTenantInterceptor.setTenantId("TENANT-A");
		Patient response = ourClient.read().resource(Patient.class).withId(idA).execute();
		assertTrue(response.getActive());

		myTenantInterceptor.setTenantId("TENANT-B");
		try {
			ourClient.read().resource(Patient.class).withId(idA).execute();
			fail();
		} catch (ResourceNotFoundException e) {
			// good
		}
	}

	@Test
	public void testCreate_InvalidTenant() {

		myTenantInterceptor.setTenantId("TENANT-ZZZ");
		Patient patientA = new Patient();
		patientA.setActive(true);
		try {
			ourClient.create().resource(patientA).execute();
			fail();
		} catch (ResourceNotFoundException e) {
			assertThat(e.getMessage(), containsString("Unknown partition name: TENANT-ZZZ"));
		}

	}

	private void createTenants() {
		myTenantInterceptor.setTenantId(DEFAULT_PERSISTED_PARTITION_NAME);

		ourClient
			.operation()
			.onServer()
			.named(ProviderConstants.PARTITION_MANAGEMENT_ADD_PARTITION)
			.withParameter(Parameters.class, ProviderConstants.PARTITION_MANAGEMENT_PARTITION_ID, new IntegerType(1))
			.andParameter(ProviderConstants.PARTITION_MANAGEMENT_PARTITION_NAME, new CodeType("TENANT-A"))
			.execute();

		ourClient
			.operation()
			.onServer()
			.named(ProviderConstants.PARTITION_MANAGEMENT_ADD_PARTITION)
			.withParameter(Parameters.class, ProviderConstants.PARTITION_MANAGEMENT_PARTITION_ID, new IntegerType(2))
			.andParameter(ProviderConstants.PARTITION_MANAGEMENT_PARTITION_NAME, new CodeType("TENANT-B"))
			.execute();
	}


	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
