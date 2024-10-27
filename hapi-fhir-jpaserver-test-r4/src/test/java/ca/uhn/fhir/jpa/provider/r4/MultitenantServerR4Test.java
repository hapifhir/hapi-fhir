package ca.uhn.fhir.jpa.provider.r4;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.jobs.export.BulkDataExportProvider;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.model.BulkExportJobResults;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.bulk.export.model.BulkExportResponseJson;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.interceptor.auth.SearchNarrowingInterceptor;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import ca.uhn.fhir.util.JsonUtil;
import com.google.common.collect.Sets;
import jakarta.servlet.http.HttpServletResponse;
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
import org.hl7.fhir.r4.model.Resource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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
			PartitionEntity partition = myPartitionDao.findForName(TENANT_A).orElseThrow(IllegalStateException::new);
			ResourceTable resourceTable = myResourceTableDao.findById(idA.getIdPartAsLong()).orElseThrow(IllegalStateException::new);
            assert resourceTable.getPartitionId() != null;
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
			ResourceTable resourceTable = myResourceTableDao.findById(idA.getIdPartAsLong()).orElseThrow(IllegalStateException::new);
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
			Collection<Object[]> forcedIds = myResourceTableDao.findAndResolveByForcedIdWithNoTypeIncludeDeleted(
				"Patient", List.of(patientId)
			);
			assertContainsSingleForcedId(forcedIds, patientId);
		});

		// Search and filter deleted
		runInTransaction(() -> {
			Collection<Object[]> forcedIds = myResourceTableDao.findAndResolveByForcedIdWithNoType(
				"Patient", List.of(patientId), true
			);
			assertContainsSingleForcedId(forcedIds, patientId);
		});

		// Delete resource
		deletePatient(JpaConstants.DEFAULT_PARTITION_NAME, idA);

		// Search and include deleted
		runInTransaction(() -> {
			Collection<Object[]> forcedIds = myResourceTableDao.findAndResolveByForcedIdWithNoTypeIncludeDeleted(
				"Patient", List.of(patientId)
			);
			assertContainsSingleForcedId(forcedIds, patientId);
		});

		// Search and filter deleted
		runInTransaction(() -> {
			Collection<Object[]> forcedIds = myResourceTableDao.findAndResolveByForcedIdWithNoType(
				"Patient", List.of(patientId), true
			);
			assertThat(forcedIds).hasSize(0);
		});
	}

	@Test
	public void findAndResolveByForcedIdWithNoTypeInPartitionNull() {
		// Create patients
		String patientId = "AAA";
		IIdType idA = createPatient(withId(patientId));

		// Search and include deleted
		runInTransaction(() -> {
			Collection<Object[]> forcedIds = myResourceTableDao.findAndResolveByForcedIdWithNoTypeInPartitionNull(
				"Patient", List.of(patientId), false
			);
			assertContainsSingleForcedId(forcedIds, patientId);
		});

		// Search and filter deleted
		runInTransaction(() -> {
			Collection<Object[]> forcedIds = myResourceTableDao.findAndResolveByForcedIdWithNoTypeInPartitionNull(
				"Patient", List.of(patientId), true
			);
			assertContainsSingleForcedId(forcedIds, patientId);
		});

		// Delete resource
		deletePatient(JpaConstants.DEFAULT_PARTITION_NAME, idA);

		// Search and include deleted
		runInTransaction(() -> {
			Collection<Object[]> forcedIds = myResourceTableDao.findAndResolveByForcedIdWithNoTypeInPartitionNull(
				"Patient", List.of(patientId), false
			);
			assertContainsSingleForcedId(forcedIds, patientId);
		});

		// Search and filter deleted
		runInTransaction(() -> {
			Collection<Object[]> forcedIds = myResourceTableDao.findAndResolveByForcedIdWithNoTypeInPartitionNull(
				"Patient", List.of(patientId), true
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
			Collection<Object[]> forcedIds = myResourceTableDao.findAndResolveByForcedIdWithNoTypeInPartitionIdOrNullPartitionId(
				"Patient", List.of(patientId), List.of(TENANT_A_ID), false
			);
			assertContainsSingleForcedId(forcedIds, patientId);
		});

		// Search and filter deleted
		runInTransaction(() -> {
			Collection<Object[]> forcedIds = myResourceTableDao.findAndResolveByForcedIdWithNoTypeInPartitionIdOrNullPartitionId(
				"Patient", List.of(patientId), List.of(TENANT_A_ID), true
			);
			assertContainsSingleForcedId(forcedIds, patientId);
		});

		deletePatient(TENANT_A, idA);

		// Search and include deleted
		runInTransaction(() -> {
			Collection<Object[]> forcedIds = myResourceTableDao.findAndResolveByForcedIdWithNoTypeInPartitionIdOrNullPartitionId(
				"Patient", List.of(patientId), List.of(TENANT_A_ID), false
			);
			assertContainsSingleForcedId(forcedIds, patientId);
		});

		// Search and filter deleted
		runInTransaction(() -> {
			Collection<Object[]> forcedIds = myResourceTableDao.findAndResolveByForcedIdWithNoTypeInPartitionIdOrNullPartitionId(
				"Patient", List.of(patientId), List.of(TENANT_A_ID), true
			);
			assertEquals(0, forcedIds.size());
		});
	}

	@Test
	public void testFindAndResolveByForcedIdWithNoTypeInPartition() {
		// Create patients
		String patientId = "AAA";
		IIdType idA = createPatient(withTenant(TENANT_A), withId(patientId));

		createPatient(withTenant(TENANT_B), withId("BBB"));

		// Search and include deleted
		runInTransaction(() -> {
			Collection<Object[]> forcedIds = myResourceTableDao.findAndResolveByForcedIdWithNoTypeInPartition(
				"Patient", List.of(patientId), Arrays.asList(TENANT_A_ID, TENANT_B_ID), false
			);
			assertContainsSingleForcedId(forcedIds, patientId);
		});

		// Search and filter deleted
		runInTransaction(() -> {
			Collection<Object[]> forcedIds = myResourceTableDao.findAndResolveByForcedIdWithNoTypeInPartition(
				"Patient", List.of(patientId), Arrays.asList(TENANT_A_ID, TENANT_B_ID), true
			);
			assertContainsSingleForcedId(forcedIds, patientId);
		});

		deletePatient(TENANT_A, idA);

		// Search and include deleted
		runInTransaction(() -> {
			Collection<Object[]> forcedIds = myResourceTableDao.findAndResolveByForcedIdWithNoTypeInPartition(
				"Patient", List.of(patientId), Arrays.asList(TENANT_A_ID, TENANT_B_ID), false
			);
			assertContainsSingleForcedId(forcedIds, patientId);
		});

		// Search and filter deleted
		runInTransaction(() -> {
			Collection<Object[]> forcedIds = myResourceTableDao.findAndResolveByForcedIdWithNoTypeInPartition(
				"Patient", List.of(patientId), Arrays.asList(TENANT_A_ID, TENANT_B_ID), true
			);
			assertEquals(0, forcedIds.size());
		});
	}

	private void assertContainsSingleForcedId(Collection<Object[]> forcedIds, String patientId){
		assertThat(forcedIds).hasSize(1);
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
			ResourceTable resourceTable = myResourceTableDao.findById(idA.getIdPartAsLong()).orElseThrow(IllegalStateException::new);
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
			assertThat(e.getMessage()).contains("Partition name \"TENANT-ZZZ\" is not valid");
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
			ResourceTable resourceTable = myResourceTableDao.findById(idA.getIdPartAsLong()).orElseThrow(IllegalStateException::new);
            assert resourceTable.getPartitionId() != null;
            assertEquals(1, resourceTable.getPartitionId().getPartitionId());
			resourceTable = myResourceTableDao.findById(idB.getIdPartAsLong()).orElseThrow(IllegalStateException::new);
            assert resourceTable.getPartitionId() != null;
            assertEquals(1, resourceTable.getPartitionId().getPartitionId());
		});

	}

	@Test
	public void testTransactionPut_withSearchNarrowingInterceptor_createsPatient() {
		// setup
		IBaseResource patientA = buildPatient(withTenant(TENANT_B), withActiveTrue(), withId("1234a"),
			withFamily("Family"), withGiven("Given"));

		Bundle transactioBundle = new Bundle();
		transactioBundle.setType(Bundle.BundleType.TRANSACTION);
		transactioBundle.addEntry()
			.setFullUrl("http://localhost:8000/TENANT-A/Patient/1234a")
			.setResource((Resource) patientA)
			.getRequest().setUrl("Patient/1234a").setMethod(Bundle.HTTPVerb.PUT);

		myServer.registerInterceptor(new SearchNarrowingInterceptor());

		// execute
		myClient.transaction().withBundle(transactioBundle).execute();

		// verify - read back using DAO
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setTenantId(TENANT_B);
		Patient patient1 = myPatientDao.read(new IdType("Patient/1234a"), requestDetails);
		assertEquals("Family", patient1.getName().get(0).getFamily());
	}

	@ParameterizedTest
	@ValueSource(strings = {"Patient/1234a", "TENANT-B/Patient/1234a"})
	public void testTransactionGet_withSearchNarrowingInterceptor_retrievesPatient(String theEntryUrl) {
		// setup
		createPatient(withTenant(TENANT_B), withActiveTrue(), withId("1234a"),
			withFamily("Family"), withGiven("Given"));

		Bundle transactioBundle = new Bundle();
		transactioBundle.setType(Bundle.BundleType.TRANSACTION);
		transactioBundle.addEntry()
			.getRequest().setUrl(theEntryUrl).setMethod(Bundle.HTTPVerb.GET);

		myServer.registerInterceptor(new SearchNarrowingInterceptor());

		// execute
		Bundle result = myClient.transaction().withBundle(transactioBundle).execute();

		// verify
		assertThat(result.getEntry()).hasSize(1);
		Patient retrievedPatient = (Patient) result.getEntry().get(0).getResource();
		assertNotNull(retrievedPatient);
		assertEquals("Family", retrievedPatient.getName().get(0).getFamily());
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
			ResourceTable resourceTable = myResourceTableDao.findById(idA.getIdPartAsLong()).orElseThrow(IllegalStateException::new);
			assertNull(resourceTable.getPartitionId());
			resourceTable = myResourceTableDao.findById(idB.getIdPartAsLong()).orElseThrow(IllegalStateException::new);
            assert resourceTable.getPartitionId() != null;
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
			ResourceTable resourceTable = myResourceTableDao.findById(idA.getIdPartAsLong()).orElseThrow(IllegalStateException::new);
			assertNull(resourceTable.getPartitionId());
			resourceTable = myResourceTableDao.findById(idB.getIdPartAsLong()).orElseThrow(IllegalStateException::new);
            assert resourceTable.getPartitionId() != null;
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
			ResourceTable resourceTable = myResourceTableDao.findById(idA.getIdPartAsLong()).orElseThrow(IllegalStateException::new);
            assert resourceTable.getPartitionId() != null;
            assertEquals(1, resourceTable.getPartitionId().getPartitionId());
			resourceTable = myResourceTableDao.findById(idB.getIdPartAsLong()).orElseThrow(IllegalStateException::new);
            assert resourceTable.getPartitionId() != null;
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
	public void testIncludeInTenantWithAssignedID() {
		IIdType idA = createResource("Patient", withTenant(JpaConstants.DEFAULT_PARTITION_NAME), withId("test"), withFamily("Smith"), withActiveTrue());
		createConditionWithAllowedUnqualified(idA);
		Bundle response = myClient.search().byUrl(myClient.getServerBase() + "/" + TENANT_A + "/Condition?subject=Patient/" + idA.getIdPart() + "&_include=Condition:subject").returnBundle(Bundle.class).execute();
		assertThat(response.getEntry()).hasSize(2);
	}

	@Test
	public void testIncludeInTenantWithAutoGeneratedID() {
		IIdType idA = createResource("Patient", withTenant(JpaConstants.DEFAULT_PARTITION_NAME), withFamily("Smith"), withActiveTrue());
		createConditionWithAllowedUnqualified(idA);
		Bundle response = myClient.search().byUrl(myClient.getServerBase() + "/" + TENANT_A + "/Condition?subject=Patient/" + idA.getIdPart() + "&_include=Condition:subject").returnBundle(Bundle.class).execute();
		assertThat(response.getEntry()).hasSize(2);
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

		String myTenantName = null;

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

			//perform export-poll-status
			myTenantName = createInPartition;
			HttpGet get = new HttpGet(buildExportUrl(createInPartition, jobId));
			try (CloseableHttpResponse response = ourHttpClient.execute(get)) {
				String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
				BulkExportResponseJson responseJson = JsonUtil.deserialize(responseString, BulkExportResponseJson.class);
				assertThat(responseJson.getOutput().get(0).getUrl()).contains(createInPartition + "/Binary/");
			}
		}

		@BeforeEach
		public void setBulkDataExportProvider() {
			myServer.getRestfulServer().registerProvider(myProvider);
		}

		private String buildExportUrl(String createInPartition, String jobId) {
			return myClient.getServerBase() + "/" + createInPartition + "/" + ProviderConstants.OPERATION_EXPORT_POLL_STATUS + "?"
				+ JpaConstants.PARAM_EXPORT_POLL_STATUS_JOB_ID + "=" + jobId;
		}
	}
}
