package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.jobs.export.BulkDataExportProvider;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.model.BulkExportJobResults;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.bulk.export.model.BulkExportResponseJson;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.partition.RequestPartitionHelperSvc;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import ca.uhn.fhir.util.JsonUtil;
import com.google.common.collect.Sets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@SuppressWarnings("Duplicates")
public class MultitenantServerR4Test extends BaseMultitenantResourceProviderR4Test implements ITestDataBuilder {

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
		assertEquals(myServerBase + "/TENANT-A/metadata", myCapturingInterceptor.getLastRequest().getUri());
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
	public void testFindAndResolveByForcedIdWithNoType() {
		// Create patients
		String patientId = "AAA";
		IIdType idA = createPatient(withId(patientId));

		// Search and include deleted
		runInTransaction(() -> {
			Collection<Object[]> forcedIds = myForcedIdDao.findAndResolveByForcedIdWithNoTypeIncludeDeleted(
				"Patient", Arrays.asList(patientId)
			);
			assertContainsSingleForcedId(forcedIds, patientId);
		});

		// Search and filter deleted
		runInTransaction(() -> {
			Collection<Object[]> forcedIds = myForcedIdDao.findAndResolveByForcedIdWithNoType(
				"Patient", Arrays.asList(patientId), true
			);
			assertContainsSingleForcedId(forcedIds, patientId);
		});

		// Delete resource
		deletePatient(JpaConstants.DEFAULT_PARTITION_NAME, idA);

		// Search and include deleted
		runInTransaction(() -> {
			Collection<Object[]> forcedIds = myForcedIdDao.findAndResolveByForcedIdWithNoTypeIncludeDeleted(
				"Patient", Arrays.asList(patientId)
			);
			assertContainsSingleForcedId(forcedIds, patientId);
		});

		// Search and filter deleted
		runInTransaction(() -> {
			Collection<Object[]> forcedIds = myForcedIdDao.findAndResolveByForcedIdWithNoType(
				"Patient", Arrays.asList(patientId), true
			);
			assertThat(forcedIds, hasSize(0));
		});
	}

	@Test
	public void findAndResolveByForcedIdWithNoTypeInPartitionNull() {
		// Create patients
		String patientId = "AAA";
		IIdType idA = createPatient(withId(patientId));

		// Search and include deleted
		runInTransaction(() -> {
			Collection<Object[]> forcedIds = myForcedIdDao.findAndResolveByForcedIdWithNoTypeInPartitionNull(
				"Patient", Arrays.asList(patientId), false
			);
			assertContainsSingleForcedId(forcedIds, patientId);
		});

		// Search and filter deleted
		runInTransaction(() -> {
			Collection<Object[]> forcedIds = myForcedIdDao.findAndResolveByForcedIdWithNoTypeInPartitionNull(
				"Patient", Arrays.asList(patientId), true
			);
			assertContainsSingleForcedId(forcedIds, patientId);
		});

		// Delete resource
		deletePatient(JpaConstants.DEFAULT_PARTITION_NAME, idA);

		// Search and include deleted
		runInTransaction(() -> {
			Collection<Object[]> forcedIds = myForcedIdDao.findAndResolveByForcedIdWithNoTypeInPartitionNull(
				"Patient", Arrays.asList(patientId), false
			);
			assertContainsSingleForcedId(forcedIds, patientId);
		});

		// Search and filter deleted
		runInTransaction(() -> {
			Collection<Object[]> forcedIds = myForcedIdDao.findAndResolveByForcedIdWithNoTypeInPartitionNull(
				"Patient", Arrays.asList(patientId), true
			);
			assertEquals(0, forcedIds.size());
		});
	}

	@Test
	public void testFindAndResolveByForcedIdWithNoTypeInPartitionIdOrNullPartitionId(){
		// Create patients
		String patientId = "AAA";
		IIdType idA = createPatient(withTenant(TENANT_A), withId(patientId));

		createPatient(withId("BBB"));

		// Search and include deleted
		runInTransaction(() -> {
			Collection<Object[]> forcedIds = myForcedIdDao.findAndResolveByForcedIdWithNoTypeInPartitionIdOrNullPartitionId(
				"Patient", Arrays.asList(patientId), Arrays.asList(TENANT_A_ID), false
			);
			assertContainsSingleForcedId(forcedIds, patientId);
		});

		// Search and filter deleted
		runInTransaction(() -> {
			Collection<Object[]> forcedIds = myForcedIdDao.findAndResolveByForcedIdWithNoTypeInPartitionIdOrNullPartitionId(
				"Patient", Arrays.asList(patientId), Arrays.asList(TENANT_A_ID), true
			);
			assertContainsSingleForcedId(forcedIds, patientId);
		});

		deletePatient(TENANT_A, idA);

		// Search and include deleted
		runInTransaction(() -> {
			Collection<Object[]> forcedIds = myForcedIdDao.findAndResolveByForcedIdWithNoTypeInPartitionIdOrNullPartitionId(
				"Patient", Arrays.asList(patientId), Arrays.asList(TENANT_A_ID), false
			);
			assertContainsSingleForcedId(forcedIds, patientId);
		});

		// Search and filter deleted
		runInTransaction(() -> {
			Collection<Object[]> forcedIds = myForcedIdDao.findAndResolveByForcedIdWithNoTypeInPartitionIdOrNullPartitionId(
				"Patient", Arrays.asList(patientId), Arrays.asList(TENANT_A_ID), true
			);
			assertEquals(0, forcedIds.size());
		});
	}

	@Test
	public void testFindAndResolveByForcedIdWithNoTypeInPartition() throws IOException {
		// Create patients
		String patientId = "AAA";
		IIdType idA = createPatient(withTenant(TENANT_A), withId(patientId));

		createPatient(withTenant(TENANT_B), withId("BBB"));

		// Search and include deleted
		runInTransaction(() -> {
			Collection<Object[]> forcedIds = myForcedIdDao.findAndResolveByForcedIdWithNoTypeInPartition(
				"Patient", Arrays.asList(patientId), Arrays.asList(TENANT_A_ID, TENANT_B_ID), false
			);
			assertContainsSingleForcedId(forcedIds, patientId);
		});

		// Search and filter deleted
		runInTransaction(() -> {
			Collection<Object[]> forcedIds = myForcedIdDao.findAndResolveByForcedIdWithNoTypeInPartition(
				"Patient", Arrays.asList(patientId), Arrays.asList(TENANT_A_ID, TENANT_B_ID), true
			);
			assertContainsSingleForcedId(forcedIds, patientId);
		});

		deletePatient(TENANT_A, idA);

		// Search and include deleted
		runInTransaction(() -> {
			Collection<Object[]> forcedIds = myForcedIdDao.findAndResolveByForcedIdWithNoTypeInPartition(
				"Patient", Arrays.asList(patientId), Arrays.asList(TENANT_A_ID, TENANT_B_ID), false
			);
			assertContainsSingleForcedId(forcedIds, patientId);
		});

		// Search and filter deleted
		runInTransaction(() -> {
			Collection<Object[]> forcedIds = myForcedIdDao.findAndResolveByForcedIdWithNoTypeInPartition(
				"Patient", Arrays.asList(patientId), Arrays.asList(TENANT_A_ID, TENANT_B_ID), true
			);
			assertEquals(0, forcedIds.size());
		});
	}

	private void assertContainsSingleForcedId(Collection<Object[]> forcedIds, String patientId){
		assertEquals(1, forcedIds.size());
		assertEquals(patientId, forcedIds.stream().toList().get(0)[2]);
	}

	private void deletePatient(String tenantId, IIdType patientId){
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setTenantId(tenantId);
		myPatientDao.delete(patientId, requestDetails);
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

	private void createConditionWithAllowedUnqualified(IIdType idA) {
		myPartitionSettings.setAllowReferencesAcrossPartitions(PartitionSettings.CrossPartitionReferenceMode.ALLOWED_UNQUALIFIED);
		IIdType idB = createResource("Condition", withTenant(TENANT_A), withObservationCode("http://cs", "A"));
		Condition theCondition = myClient.read().resource(Condition.class).withId(idB).execute();
		theCondition.getSubject().setReference("Patient/" + idA.getIdPart());
		doUpdateResource(theCondition);
	}

	@Nested
	public class PartitionTesting {

		@InjectMocks
		private BulkDataExportProvider myProvider;

		@Mock
		private IJobCoordinator myJobCoordinator;

		@Spy
		private RequestPartitionHelperSvc myRequestPartitionHelperSvc = new MultitenantServerR4Test.PartitionTesting.MyRequestPartitionHelperSvc();

		String myTenantName = null;

		private class MyRequestPartitionHelperSvc extends RequestPartitionHelperSvc {

			@Override
			public RequestPartitionId determineReadPartitionForRequest(RequestDetails theRequest, ReadPartitionIdRequestDetails theDetails) {
				return RequestPartitionId.fromPartitionName(myTenantName);
			}

			@Override
			public void validateHasPartitionPermissions(RequestDetails theRequest, String theResourceType, RequestPartitionId theRequestPartitionId) {
				return;
			}

		}

		@Test
		public void testBulkExportForDifferentPartitions() throws IOException {
			setBulkDataExportProvider();
			testBulkExport(TENANT_A);
			testBulkExport(TENANT_B);
			testBulkExport(JpaConstants.DEFAULT_PARTITION_NAME);
		}

		private void testBulkExport(String createInPartition) throws IOException {
			// setup
			String jobId = "jobId";
			RestfulServer mockServer = mock(RestfulServer.class);
			HttpServletResponse mockResponse = mock(HttpServletResponse.class);

			BulkExportJobResults results = new BulkExportJobResults();
			HashMap<String, List<String>> map = new HashMap<>();
			map.put("Patient", Arrays.asList("Binary/1", "Binary/2"));
			results.setResourceTypeToBinaryIds(map);

			JobInstance jobInfo = new JobInstance();
			jobInfo.setInstanceId(jobId);
			jobInfo.setStatus(StatusEnum.COMPLETED);
			jobInfo.setReport(JsonUtil.serialize(results));
			jobInfo.setParameters(new BulkExportJobParameters());

			// Create a bulk job
			BulkExportJobParameters options = new BulkExportJobParameters();
			options.setResourceTypes(Sets.newHashSet("Patient"));
			options.setExportStyle(BulkExportJobParameters.ExportStyle.SYSTEM);

			Batch2JobStartResponse startResponse = new Batch2JobStartResponse();
			startResponse.setInstanceId(jobId);
			when(myJobCoordinator.startInstance(isNotNull(), any()))
				.thenReturn(startResponse);
			when(myJobCoordinator.getInstance(anyString()))
				.thenReturn(jobInfo);

			// mocking
			ServletRequestDetails servletRequestDetails = spy(new ServletRequestDetails());
			MockHttpServletRequest reqDetails = new MockHttpServletRequest();
			reqDetails.addHeader(Constants.HEADER_PREFER,
				"respond-async");
			servletRequestDetails.setServletRequest(reqDetails);
			when(servletRequestDetails.getServer())
				.thenReturn(mockServer);
			when(servletRequestDetails.getServletResponse())
				.thenReturn(mockResponse);

			List<IPrimitiveType<String>> filters = new ArrayList<>();
			if (options.getFilters() != null) {
				for (String v : options.getFilters()) {
					filters.add(new StringType(v));
				}
			}

			//perform export-poll-status
			myTenantName = createInPartition;
			HttpGet get = new HttpGet(buildExportUrl(createInPartition, jobId));
			try (CloseableHttpResponse response = ourHttpClient.execute(get)) {
				String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
				BulkExportResponseJson responseJson = JsonUtil.deserialize(responseString, BulkExportResponseJson.class);
				assertThat(responseJson.getOutput().get(0).getUrl(), containsString(createInPartition + "/Binary/"));
			}
		}

		@BeforeEach
		public void setBulkDataExportProvider() {
			myServer.getRestfulServer().registerProvider(myProvider);
		}

		private String buildExportUrl(String createInPartition, String jobId) {
			return myClient.getServerBase() + "/" + createInPartition + "/" + JpaConstants.OPERATION_EXPORT_POLL_STATUS + "?"
				+ JpaConstants.PARAM_EXPORT_POLL_STATUS_JOB_ID + "=" + jobId;
		}
	}
}
