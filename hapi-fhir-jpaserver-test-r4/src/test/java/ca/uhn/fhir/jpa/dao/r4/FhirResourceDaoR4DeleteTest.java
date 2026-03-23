package ca.uhn.fhir.jpa.dao.r4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.model.dao.JpaPidFk;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FhirResourceDaoR4DeleteTest extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(FhirResourceDaoR4DeleteTest.class);

	@Test
	public void testPreventUnDeleteIfDeletesDisabled() {
		// Setup
		createPatient(withId("A"), withActiveTrue());
		myPatientDao.delete(new IdType("Patient/A"), mySrd);

		// Test
		myStorageSettings.setDeleteEnabled(false);
		try {
			createPatient(withId("A"), withActiveFalse());
			fail();
		} catch (InvalidRequestException e) {
			// Verify
			assertThat(e.getMessage()).contains("HAPI-2573: Unable to restore previously deleted resource as deletes are disabled");
		}
	}


	@Test
	public void testDeleteMarksResourceAndVersionAsDeleted() {

		Patient p = new Patient();
		p.setActive(true);
		IIdType id = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		myPatientDao.delete(id);

		// Table should be marked as deleted
		runInTransaction(() -> {
			ResourceTable resourceTable = myResourceTableDao.findById(id.getIdPartAsLong()).get();
			assertNotNull(resourceTable.getDeleted());
		});

		// Current version should be marked as deleted
		runInTransaction(() -> {
			ResourceHistoryTable resourceTable = myResourceHistoryTableDao.findForIdAndVersion(JpaPidFk.fromId(id.getIdPartAsLong()), 1);
			assertNull(resourceTable.getDeleted());
			assertNotNull(resourceTable.getPersistentId());
		});
		runInTransaction(() -> {
			ResourceHistoryTable resourceTable = myResourceHistoryTableDao.findForIdAndVersion(JpaPidFk.fromId(id.getIdPartAsLong()), 2);
			assertNotNull(resourceTable.getDeleted());
		});

		try {
			myPatientDao.read(id.toUnqualifiedVersionless());
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

		myPatientDao.read(id.toUnqualifiedVersionless().withVersion("1"));

		try {
			myPatientDao.read(id.toUnqualifiedVersionless().withVersion("2"));
			fail();
		} catch (ResourceGoneException e) {
			// good
		}


	}

	@Test
	public void testDeleteDisabled() {
		myStorageSettings.setDeleteEnabled(false);

		Patient p = new Patient();
		p.setActive(true);
		IIdType pId = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		try {
			myPatientDao.delete(pId);
			fail();
		} catch (PreconditionFailedException e) {
			assertEquals(Msg.code(966) + "Resource deletion is not permitted on this server", e.getMessage());
		}
	}

	@Test
	public void testDeleteCircularReferenceInTransaction() {

		// Create two resources with a circular reference
		Organization org1 = new Organization();
		org1.setId(IdType.newRandomUuid());
		Organization org2 = new Organization();
		org2.setId(IdType.newRandomUuid());
		org1.getPartOf().setReference(org2.getId());
		org2.getPartOf().setReference(org1.getId());

		// Upload them in a transaction
		Bundle createTransaction = new Bundle();
		createTransaction.setType(Bundle.BundleType.TRANSACTION);
		createTransaction
			.addEntry()
			.setResource(org1)
			.setFullUrl(org1.getId())
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("Organization");
		createTransaction
			.addEntry()
			.setResource(org2)
			.setFullUrl(org2.getId())
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("Organization");

		Bundle createResponse = mySystemDao.transaction(mySrd, createTransaction);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createResponse));

		IdType orgId1 = new IdType(createResponse.getEntry().get(0).getResponse().getLocation()).toUnqualifiedVersionless();
		IdType orgId2 = new IdType(createResponse.getEntry().get(1).getResponse().getLocation()).toUnqualifiedVersionless();

		// Nope, can't delete 'em!
		try {
			myOrganizationDao.delete(orgId1);
			fail();
		} catch (ResourceVersionConflictException e) {
			// good
		}
		try {
			myOrganizationDao.delete(orgId2);
			fail();
		} catch (ResourceVersionConflictException e) {
			// good
		}

		// Now in a transaction
		Bundle deleteTransaction = new Bundle();
		deleteTransaction.setType(Bundle.BundleType.TRANSACTION);
		deleteTransaction.addEntry()
			.getRequest()
			.setMethod(Bundle.HTTPVerb.DELETE)
			.setUrl(orgId1.getValue());
		deleteTransaction.addEntry()
			.getRequest()
			.setMethod(Bundle.HTTPVerb.DELETE)
			.setUrl(orgId2.getValue());
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(deleteTransaction));
		mySystemDao.transaction(mySrd, deleteTransaction);

		// Make sure they were deleted
		try {
			myOrganizationDao.read(orgId1);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}
		try {
			myOrganizationDao.read(orgId2);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}


	}

	@Test
	public void testResourceIsConsideredDeletedIfOnlyResourceTableEntryIsDeleted() {

		Patient p = new Patient();
		p.setActive(true);
		IIdType id = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		myPatientDao.delete(id);

		// Table should be marked as deleted
		runInTransaction(() -> {
			ResourceTable resourceTable = myResourceTableDao.findById(id.getIdPartAsLong()).get();
			assertNotNull(resourceTable.getDeleted());
		});

		// Mark the current history version as not-deleted even though the actual resource
		// table entry is marked deleted
		runInTransaction(() -> {
			ResourceHistoryTable resourceTable = myResourceHistoryTableDao.findForIdAndVersion(JpaPidFk.fromId(id.getIdPartAsLong()), 2);
			resourceTable.setDeleted(null);
			myResourceHistoryTableDao.save(resourceTable);
		});

		try {
			myPatientDao.read(id.toUnqualifiedVersionless());
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

		myPatientDao.read(id.toUnqualifiedVersionless().withVersion("1"));

		try {
			myPatientDao.read(id.toUnqualifiedVersionless().withVersion("2"));
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

	}

	@Test
	public void testDeleteResourceCreatedWithConditionalUrl_willRemoveEntryInSearchUrlTable() {
		String identifierCode = "20210427133226.4440+800";
		String matchUrl = "identifier=20210427133226.4440%2B800";
		Observation obs = new Observation();
		obs.addIdentifier().setValue(identifierCode);
		IIdType firstObservationId = myObservationDao.create(obs, matchUrl, new SystemRequestDetails()).getId();
		long originalId = firstObservationId.getIdPartAsLong();
		assertThat(myResourceSearchUrlDao.findAll()).hasSize(1);

		myObservationDao.delete(obs.getIdElement(), mySrd);
		logAllResources();
		logAllResourceSearchUrls();

		// when
		DaoMethodOutcome daoMethodOutcome = myObservationDao.create(obs, matchUrl, new SystemRequestDetails());

		// then
		logAllResources();
		logAllResourceSearchUrls();
		assertNotEquals(originalId, daoMethodOutcome.getId().getIdPartAsLong());
		assertTrue(daoMethodOutcome.getCreated().booleanValue());
		assertThat(firstObservationId.getIdPart()).isNotEqualTo(daoMethodOutcome.getId());
	}

	/**
	 * When deleting a resource with a non-numeric version ID (e.g. from a corrupted or
	 * non-numeric ETag), the server should throw a ResourceVersionConflictException (409)
	 * rather than a NumberFormatException (500).
	 * See SMILE-8708.
	 */
	@Test
	void testDeleteWithNonNumericVersionId_returnsConflict() {
		// Create a patient
		Patient p = new Patient();
		p.setActive(true);
		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		// Attempt to delete with a non-numeric version in the ID
		IdType idWithNonNumericVersion = new IdType("Patient", id.getIdPart(), "A23");

		assertThatThrownBy(() -> myPatientDao.delete(idWithNonNumericVersion, mySrd))
			.isInstanceOf(ResourceVersionConflictException.class)
			.hasMessageContaining("Trying to delete");
	}

	@Test
	public void testDeletedResourceInPrecommitHookHasCorrectLastUpdated() {
		// Setup - create a patient
		Patient p = new Patient();
		p.setActive(true);
		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		// Register interceptor to capture the resource from the PRECOMMIT hook
		AtomicReference<Date> hookLastUpdated = new AtomicReference<>();
		myInterceptorRegistry.registerAnonymousInterceptor(
			Pointcut.STORAGE_PRECOMMIT_RESOURCE_DELETED, (thePointcut, theParams) -> {
				IBaseResource resource = theParams.get(IBaseResource.class, 0);
				hookLastUpdated.set(resource.getMeta().getLastUpdated());
			});

		// Test - delete the patient
		myPatientDao.delete(id, mySrd);

		// Verify - the hook resource's lastUpdated should match the DB entity
		assertNotNull(hookLastUpdated.get(), "Hook should have captured lastUpdated");
		runInTransaction(() -> {
			ResourceTable entity = myResourceTableDao.findById(id.getIdPartAsLong()).orElseThrow();
			assertEquals(entity.getUpdatedDate(), hookLastUpdated.get(),
				"Resource lastUpdated in STORAGE_PRECOMMIT_RESOURCE_DELETED hook should match the entity's updated timestamp in the DB");
		});
	}

	@Test
	void testDeleteByUrl_PrecommitHookHasCorrectLastUpdated() {
		// Setup - create a patient with a searchable attribute
		Patient p = new Patient();
		p.setActive(true);
		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		// Register interceptor to capture the resource from the PRECOMMIT hook
		AtomicReference<Date> hookLastUpdated = new AtomicReference<>();
		myInterceptorRegistry.registerAnonymousInterceptor(
			Pointcut.STORAGE_PRECOMMIT_RESOURCE_DELETED, (thePointcut, theParams) -> {
				IBaseResource resource = theParams.get(IBaseResource.class, 0);
				hookLastUpdated.set(resource.getMeta().getLastUpdated());
			});

		// Test - delete by URL (triggers deletePidList path)
		myPatientDao.deleteByUrl("Patient?active=true", mySrd);

		// Verify - the hook resource's lastUpdated should match the DB entity
		assertNotNull(hookLastUpdated.get(), "Hook should have captured lastUpdated");
		runInTransaction(() -> {
			ResourceTable entity = myResourceTableDao.findById(id.getIdPartAsLong()).orElseThrow();
			assertEquals(entity.getUpdatedDate(), hookLastUpdated.get(),
				"Resource lastUpdated in STORAGE_PRECOMMIT_RESOURCE_DELETED hook should match the entity's updated timestamp in the DB");
		});
	}
}
