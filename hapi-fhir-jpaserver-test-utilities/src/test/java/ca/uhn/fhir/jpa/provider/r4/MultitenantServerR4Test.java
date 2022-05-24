package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.batch.config.BatchConstants;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkDataExportJobSchedulingHelper;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkDataExportSvc;
import ca.uhn.fhir.jpa.bulk.export.model.BulkExportJobStatusEnum;
import ca.uhn.fhir.jpa.bulk.export.model.BulkExportResponseJson;
import ca.uhn.fhir.jpa.bulk.export.provider.BulkDataExportProvider;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import ca.uhn.fhir.util.JsonUtil;
import com.google.common.collect.Sets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@SuppressWarnings("Duplicates")
public class MultitenantServerR4Test extends BaseMultitenantResourceProviderR4Test implements ITestDataBuilder {

	@Autowired
	private IBulkDataExportSvc myBulkDataExportSvc;
	@Autowired
	private IBulkDataExportJobSchedulingHelper myBulkDataExportJobSchedulingHelper;

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();
		myPartitionSettings.setAllowReferencesAcrossPartitions(PartitionSettings.CrossPartitionReferenceMode.NOT_ALLOWED);
		assertFalse(myPartitionSettings.isAllowUnqualifiedCrossPartitionReference());
	}

	@Test
	public void testFetchCapabilityStatement() {
		myTenantClientInterceptor.setTenantId(TENANT_A);
		CapabilityStatement cs = myClient.capabilities().ofType(CapabilityStatement.class).execute();

		assertEquals("HAPI FHIR Server", cs.getSoftware().getName());
		assertEquals(ourServerBase + "/TENANT-A/metadata", myCapturingInterceptor.getLastRequest().getUri());
	}

	@Test
	public void testCreateAndRead_NamedTenant() {

		// Create patients

		IIdType idA = createPatient(withTenant(TENANT_A), withActiveTrue());
		createPatient(withTenant(TENANT_B), withActiveFalse());

		runInTransaction(() -> {
			PartitionEntity partition = myPartitionDao.findForName(TENANT_A).orElseThrow(() -> new IllegalStateException());
			ResourceTable resourceTable = myResourceTableDao.findById(idA.getIdPartAsLong()).orElseThrow(() -> new IllegalStateException());
			assertEquals(partition.getId(), resourceTable.getPartitionId().getPartitionId());
		});

		// Now read back

		myTenantClientInterceptor.setTenantId(TENANT_A);
		Patient response = myClient.read().resource(Patient.class).withId(idA).execute();
		assertTrue(response.getActive());

		// Update resource (should remain in correct partition)

		createPatient(withActiveFalse(), withId(idA));

		// Now read back

		response = myClient.read().resource(Patient.class).withId(idA.withVersion("2")).execute();
		assertFalse(response.getActive());

		myTenantClientInterceptor.setTenantId(TENANT_B);
		try {
			myClient.read().resource(Patient.class).withId(idA).execute();
			fail();
		} catch (ResourceNotFoundException e) {
			// good
		}
	}

	@Test
	public void testCreateAndRead_DefaultTenant() {

		// Create patients

		IIdType idA = createPatient(withTenant(JpaConstants.DEFAULT_PARTITION_NAME), withActiveTrue());
		createPatient(withTenant(TENANT_B), withActiveFalse());

		runInTransaction(() -> {
			ResourceTable resourceTable = myResourceTableDao.findById(idA.getIdPartAsLong()).orElseThrow(() -> new IllegalStateException());
			assertNull(resourceTable.getPartitionId());
		});


		// Now read back

		myTenantClientInterceptor.setTenantId(JpaConstants.DEFAULT_PARTITION_NAME);
		Patient response = myClient.read().resource(Patient.class).withId(idA).execute();
		assertTrue(response.getActive());

		// Update resource (should remain in correct partition)

		createPatient(withActiveFalse(), withId(idA));

		// Now read back

		response = myClient.read().resource(Patient.class).withId(idA.withVersion("2")).execute();
		assertFalse(response.getActive());

		// Try reading from wrong partition

		myTenantClientInterceptor.setTenantId(TENANT_B);
		try {
			myClient.read().resource(Patient.class).withId(idA).execute();
			fail();
		} catch (ResourceNotFoundException e) {
			// good
		}
	}

	@Test
	public void testCreateAndRead_NonPartitionableResource_DefaultTenant() {

		// Create patients

		IIdType idA = createResource("NamingSystem", withTenant(JpaConstants.DEFAULT_PARTITION_NAME), withStatus("draft"));

		runInTransaction(() -> {
			ResourceTable resourceTable = myResourceTableDao.findById(idA.getIdPartAsLong()).orElseThrow(() -> new IllegalStateException());
			assertNull(resourceTable.getPartitionId());
		});

	}


	@Test
	public void testCreate_InvalidTenant() {

		myTenantClientInterceptor.setTenantId("TENANT-ZZZ");
		Patient patientA = new Patient();
		patientA.setActive(true);
		try {
			myClient.create().resource(patientA).execute();
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
		Bundle response = myClient.transaction().withBundle(input).execute();

		IdType idA = new IdType(response.getEntry().get(0).getResponse().getLocation());
		IdType idB = new IdType(response.getEntry().get(1).getResponse().getLocation());

		runInTransaction(() -> {
			ResourceTable resourceTable = myResourceTableDao.findById(idA.getIdPartAsLong()).orElseThrow(() -> new IllegalStateException());
			assertEquals(1, resourceTable.getPartitionId().getPartitionId());
			resourceTable = myResourceTableDao.findById(idB.getIdPartAsLong()).orElseThrow(() -> new IllegalStateException());
			assertEquals(1, resourceTable.getPartitionId().getPartitionId());
		});

	}

	@Test
	public void testDirectDaoAccess_PartitionInRequestDetails_Create() {

		// Create patients
		IBaseResource patientA = buildPatient(withActiveTrue());
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setTenantId(JpaConstants.DEFAULT_PARTITION_NAME);
		IIdType idA = myPatientDao.create((Patient) patientA, requestDetails).getId();

		IBaseResource patientB = buildPatient(withActiveFalse());
		requestDetails = new SystemRequestDetails();
		requestDetails.setTenantId(TENANT_B);
		IIdType idB = myPatientDao.create((Patient) patientB, requestDetails).getId();

		runInTransaction(() -> {
			ResourceTable resourceTable = myResourceTableDao.findById(idA.getIdPartAsLong()).orElseThrow(() -> new IllegalStateException());
			assertNull(resourceTable.getPartitionId());
			resourceTable = myResourceTableDao.findById(idB.getIdPartAsLong()).orElseThrow(() -> new IllegalStateException());
			assertEquals(2, resourceTable.getPartitionId().getPartitionId());
		});


		// Now read back

		myTenantClientInterceptor.setTenantId(JpaConstants.DEFAULT_PARTITION_NAME);
		Patient response = myClient.read().resource(Patient.class).withId(idA).execute();
		assertTrue(response.getActive());

		myTenantClientInterceptor.setTenantId(TENANT_B);
		response = myClient.read().resource(Patient.class).withId(idB).execute();
		assertFalse(response.getActive());

		// Read back using DAO

		requestDetails = new SystemRequestDetails();
		requestDetails.setTenantId(JpaConstants.DEFAULT_PARTITION_NAME);
		response = myPatientDao.read(idA, requestDetails);
		assertTrue(response.getActive());

		requestDetails = new SystemRequestDetails();
		requestDetails.setTenantId(TENANT_B);
		response = myPatientDao.read(idB, requestDetails);
		assertFalse(response.getActive());

	}

	@Test
	public void testPartitionInRequestDetails_UpdateWithWrongTenantId() {
		IIdType idA = createPatient(withTenant(TENANT_A), withActiveTrue()).toVersionless();
		IBaseResource patientA = buildPatient(withId(idA), withActiveTrue());
		RequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setTenantId(TENANT_B);
		try {
			myPatientDao.update((Patient) patientA, requestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(2079) + "Resource " + ((Patient) patientA).getResourceType() + "/" + ((Patient) patientA).getIdElement().getIdPart() + " is not known", e.getMessage());
		}
	}

	@Test
	public void testDirectDaoAccess_PartitionInRequestDetails_Update() {

		IIdType idA = createPatient(withTenant(JpaConstants.DEFAULT_PARTITION_NAME), withActiveFalse()).toVersionless();
		IIdType idB = createPatient(withTenant(TENANT_B), withActiveTrue()).toVersionless();

		// Create patients
		IBaseResource patientA = buildPatient(withId(idA), withActiveTrue());
		RequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setTenantId(JpaConstants.DEFAULT_PARTITION_NAME);
		myPatientDao.update((Patient) patientA, requestDetails);

		IBaseResource patientB = buildPatient(withId(idB), withActiveFalse());
		requestDetails = new SystemRequestDetails();
		requestDetails.setTenantId(TENANT_B);
		myPatientDao.update((Patient) patientB, requestDetails);

		runInTransaction(() -> {
			ResourceTable resourceTable = myResourceTableDao.findById(idA.getIdPartAsLong()).orElseThrow(() -> new IllegalStateException());
			assertNull(resourceTable.getPartitionId());
			resourceTable = myResourceTableDao.findById(idB.getIdPartAsLong()).orElseThrow(() -> new IllegalStateException());
			assertEquals(2, resourceTable.getPartitionId().getPartitionId());
		});


		// Now read back

		myTenantClientInterceptor.setTenantId(JpaConstants.DEFAULT_PARTITION_NAME);
		Patient response = myClient.read().resource(Patient.class).withId(idA).execute();
		assertTrue(response.getActive());

		myTenantClientInterceptor.setTenantId(TENANT_B);
		response = myClient.read().resource(Patient.class).withId(idB).execute();
		assertFalse(response.getActive());

		// Read back using DAO

		requestDetails = new SystemRequestDetails();
		requestDetails.setTenantId(JpaConstants.DEFAULT_PARTITION_NAME);
		response = myPatientDao.read(idA, requestDetails);
		assertTrue(response.getActive());

		requestDetails = new SystemRequestDetails();
		requestDetails.setTenantId(TENANT_B);
		response = myPatientDao.read(idB, requestDetails);
		assertFalse(response.getActive());

	}

	@Test
	public void testDirectDaoAccess_PartitionInRequestDetails_Transaction() {
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

		RequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setTenantId(TENANT_A);
		Bundle response = mySystemDao.transaction(requestDetails, input);

		IdType idA = new IdType(response.getEntry().get(0).getResponse().getLocation());
		IdType idB = new IdType(response.getEntry().get(1).getResponse().getLocation());

		runInTransaction(() -> {
			ResourceTable resourceTable = myResourceTableDao.findById(idA.getIdPartAsLong()).orElseThrow(() -> new IllegalStateException());
			assertEquals(1, resourceTable.getPartitionId().getPartitionId());
			resourceTable = myResourceTableDao.findById(idB.getIdPartAsLong()).orElseThrow(() -> new IllegalStateException());
			assertEquals(1, resourceTable.getPartitionId().getPartitionId());
		});

	}

	@Test
	public void testDirectDaoAccess_PartitionInRequestDetails_TransactionWithGet() {
		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.TRANSACTION);
		input.addEntry()
			.getRequest().setUrl("Patient").setMethod(Bundle.HTTPVerb.GET);

		try {
			RequestDetails requestDetails = new SystemRequestDetails();
			requestDetails.setTenantId(TENANT_A);
			mySystemDao.transaction(requestDetails, input);
			fail();
		} catch (MethodNotAllowedException e) {
			assertEquals(Msg.code(531) + "Can not call transaction GET methods from this context", e.getMessage());
		}

	}

	@Test
	public void testIncludeInTenantWithAssignedID() throws Exception {
		IIdType idA = createResource("Patient", withTenant(JpaConstants.DEFAULT_PARTITION_NAME), withId("test"), withFamily("Smith"), withActiveTrue());
		createConditionWithAllowedUnqualified(idA);
		Bundle response = myClient.search().byUrl(myClient.getServerBase() + "/" + TENANT_A + "/Condition?subject=Patient/" + idA.getIdPart() + "&_include=Condition:subject").returnBundle(Bundle.class).execute();
		assertThat(response.getEntry(), hasSize(2));
	}

	@Test
	public void testIncludeInTenantWithAutoGeneratedID() throws Exception {
		IIdType idA = createResource("Patient", withTenant(JpaConstants.DEFAULT_PARTITION_NAME), withFamily("Smith"), withActiveTrue());
		createConditionWithAllowedUnqualified(idA);
		Bundle response = myClient.search().byUrl(myClient.getServerBase() + "/" + TENANT_A + "/Condition?subject=Patient/" + idA.getIdPart() + "&_include=Condition:subject").returnBundle(Bundle.class).execute();
		assertThat(response.getEntry(), hasSize(2));
	}

	@Test
	public void testBulkExportForDifferentPartitions() throws IOException {
		setBulkDataExportProvider();
		testBulkExport(TENANT_A);
		testBulkExport(TENANT_B);
		testBulkExport(JpaConstants.DEFAULT_PARTITION_NAME);
	}

	private void testBulkExport(String createInPartition) throws IOException {
		// Create a patient
		IBaseResource patientA = buildPatient(withActiveTrue());
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setTenantId(createInPartition);
		myPatientDao.create((Patient) patientA, requestDetails);

		// Create a bulk job
		BulkDataExportOptions options = new BulkDataExportOptions();
		options.setResourceTypes(Sets.newHashSet("Patient"));
		options.setExportStyle(BulkDataExportOptions.ExportStyle.SYSTEM);

		IBulkDataExportSvc.JobInfo jobDetails = myBulkDataExportSvc.submitJob(options, false, requestDetails);
		assertNotNull(jobDetails.getJobId());

		// Run a scheduled pass to build the export and wait for completion
		myBulkDataExportJobSchedulingHelper.startSubmittedJobs();
		myBatchJobHelper.awaitAllBulkJobCompletions(
			BatchConstants.BULK_EXPORT_JOB_NAME
		);

		//perform export-poll-status
		HttpGet get = new HttpGet(buildExportUrl(createInPartition, jobDetails.getJobId()));
		try (CloseableHttpResponse response = ourHttpClient.execute(get)) {
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			BulkExportResponseJson responseJson = JsonUtil.deserialize(responseString, BulkExportResponseJson.class);
			assertThat(responseJson.getOutput().get(0).getUrl(), containsString(JpaConstants.DEFAULT_PARTITION_NAME + "/Binary/"));
		}
	}

	private void setBulkDataExportProvider() {
		BulkDataExportProvider provider = new BulkDataExportProvider();
		provider.setBulkDataExportSvcForUnitTests(myBulkDataExportSvc);
		provider.setFhirContextForUnitTest(myFhirContext);
		ourRestServer.registerProvider(provider);
	}

	private String buildExportUrl(String createInPartition, String jobId) {
		return myClient.getServerBase() + "/" + createInPartition + "/" + JpaConstants.OPERATION_EXPORT_POLL_STATUS + "?"
			+ JpaConstants.PARAM_EXPORT_POLL_STATUS_JOB_ID + "=" + jobId;
	}

	private void createConditionWithAllowedUnqualified(IIdType idA) {
		myPartitionSettings.setAllowReferencesAcrossPartitions(PartitionSettings.CrossPartitionReferenceMode.ALLOWED_UNQUALIFIED);
		IIdType idB = createResource("Condition", withTenant(TENANT_A), withObservationCode("http://cs", "A"));
		Condition theCondition = myClient.read().resource(Condition.class).withId(idB).execute();
		theCondition.getSubject().setReference("Patient/" + idA.getIdPart());
		doUpdateResource(theCondition);
	}
}
