package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class FhirResourceDaoR4DeleteTest extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(FhirResourceDaoR4DeleteTest.class);

	@AfterEach
	public void after() {
		myDaoConfig.setDeleteEnabled(new DaoConfig().isDeleteEnabled());
	}

	@Test
	public void testDeleteWithHas() {
		Observation obs1 = new Observation();
		obs1.setStatus(Observation.ObservationStatus.FINAL);
		IIdType obs1id = myObservationDao.create(obs1).getId().toUnqualifiedVersionless();

		Observation obs2 = new Observation();
		obs2.setStatus(Observation.ObservationStatus.FINAL);
		IIdType obs2id = myObservationDao.create(obs2).getId().toUnqualifiedVersionless();

		DiagnosticReport rpt = new DiagnosticReport();
		rpt.addIdentifier().setSystem("foo").setValue("IDENTIFIER");
		rpt.addResult(new Reference(obs2id));
		myDiagnosticReportDao.create(rpt).getId().toUnqualifiedVersionless();

		myObservationDao.read(obs1id);
		myObservationDao.read(obs2id);

		try {
			myObservationDao.deleteByUrl("Observation?_has:DiagnosticReport:result:identifier=foo|IDENTIFIER", mySrd);
			fail();
		} catch (ResourceVersionConflictException e) {
			assertConflictException("DiagnosticReport", e);
		}

		myObservationDao.read(obs1id);
		myObservationDao.read(obs2id);
	}

	@Test
	public void testDeleteWithMatchUrl() {
		String methodName = "testDeleteWithMatchUrl";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id = myPatientDao.create(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		Bundle request = new Bundle();
		request.addEntry().setResource(p).getRequest().setMethod(Bundle.HTTPVerb.DELETE).setUrl("Patient?identifier=urn%3Asystem%7C" + methodName);

		myPatientDao.deleteByUrl("Patient?identifier=urn%3Asystem%7C" + methodName, mySrd);

		try {
			myPatientDao.read(id.toVersionless(), mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// ok
		}

		try {
			myPatientDao.read(new IdType("Patient/" + methodName), mySrd);
			fail();
		} catch (ResourceNotFoundException e) {
			// ok
		}

		IBundleProvider history = myPatientDao.history(id, null, null, mySrd);
		assertEquals(2, history.size().intValue());

		assertNotNull(ResourceMetadataKeyEnum.DELETED_AT.get((IAnyResource) history.getResources(0, 1).get(0)));
		assertNotNull(ResourceMetadataKeyEnum.DELETED_AT.get((IAnyResource) history.getResources(0, 1).get(0)).getValue());
		assertNull(ResourceMetadataKeyEnum.DELETED_AT.get((IAnyResource) history.getResources(1, 2).get(0)));

	}

	@Test
	public void testDeleteExpungeWithMatchUrl() {
		String methodName = "testDeleteWithMatchUrl";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id = myPatientDao.create(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		Bundle request = new Bundle();
		request.addEntry().setResource(p).getRequest().setMethod(Bundle.HTTPVerb.DELETE).setUrl("Patient?identifier=urn%3Asystem%7C" + methodName + "&" +  JpaConstants.PARAM_DELETE_EXPUNGE + "=true");

		myPatientDao.deleteByUrl("Patient?identifier=urn%3Asystem%7C" + methodName + "&" +  JpaConstants.PARAM_DELETE_EXPUNGE + "=true", mySrd);

		assertExpunged(id);
	}

	@Test
	public void testDeleteWithMatchUrlChainedIdentifier() {
		String methodName = "testDeleteWithMatchUrlChainedIdentifer";

		Organization org = new Organization();
		org.setName(methodName);
		org.addIdentifier().setSystem("http://example.com").setValue(methodName);
		IIdType orgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.getManagingOrganization().setReferenceElement(orgId);
		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		ourLog.info("Created patient, got it: {}", id);

		myPatientDao.deleteByUrl("Patient?organization.identifier=http://example.com|" + methodName, mySrd);
		assertGone(id);
		assertNotGone(orgId);

		myOrganizationDao.deleteByUrl("Organization?identifier=http://example.com|" + methodName, mySrd);
		assertGone(id);
		assertGone(orgId);

	}

	@Test
	public void testDeleteWithMatchUrlChainedProfile() {
		String methodName = "testDeleteWithMatchUrlChainedProfile";

		Organization org = new Organization();

		org.getMeta().getProfile().add(new CanonicalType("http://foo"));
		org.setName(methodName);

		IIdType orgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.getManagingOrganization().setReferenceElement(orgId);
		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		ourLog.info("Created patient, got it: {}", id);

		myPatientDao.deleteByUrl("Patient?organization._profile=http://foo", mySrd);
		assertGone(id);

		myOrganizationDao.deleteByUrl("Organization?_profile=http://foo", mySrd);
		try {
			myOrganizationDao.read(orgId, mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

		try {
			myPatientDao.deleteByUrl("Patient?organization._profile.identifier=http://foo", mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Invalid parameter chain: organization._profile.identifier", e.getMessage());
		}

		try {
			myOrganizationDao.deleteByUrl("Organization?_profile.identifier=http://foo", mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Invalid parameter chain: _profile.identifier", e.getMessage());
		}

	}

	@Test
	public void testDeleteWithMatchUrlChainedString() {
		String methodName = "testDeleteWithMatchUrlChainedString";

		Organization org = new Organization();
		org.setName(methodName);
		IIdType orgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.getManagingOrganization().setReferenceElement(orgId);
		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		ourLog.info("Created patient, got it: {}", id);

		myPatientDao.deleteByUrl("Patient?organization.name=" + methodName, mySrd);

		assertGone(id);
	}

	@Test
	public void testDeleteWithMatchUrlChainedTag() {
		String methodName = "testDeleteWithMatchUrlChainedString";

		Organization org = new Organization();
		org.getMeta().addTag().setSystem("http://foo").setCode("term");

		org.setName(methodName);

		IIdType orgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.getManagingOrganization().setReferenceElement(orgId);
		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		ourLog.info("Created patient, got it: {}", id);

		myPatientDao.deleteByUrl("Patient?organization._tag=http://foo|term", mySrd);
		assertGone(id);

		myOrganizationDao.deleteByUrl("Organization?_tag=http://foo|term", mySrd);
		try {
			myOrganizationDao.read(orgId, mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

		try {
			myPatientDao.deleteByUrl("Patient?organization._tag.identifier=http://foo|term", mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Invalid parameter chain: organization._tag.identifier", e.getMessage());
		}

		try {
			myOrganizationDao.deleteByUrl("Organization?_tag.identifier=http://foo|term", mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Invalid parameter chain: _tag.identifier", e.getMessage());
		}

	}

	@Test
	public void testDeleteByTagWrongType() {
		Organization org = new Organization();
		org.getMeta().addTag().setCode("term");
		IIdType orgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();
		myPatientDao.deleteByUrl("Patient?_tag=term", mySrd);
		// The organization is still there
		myOrganizationDao.read(orgId);
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
			assertTrue(resourceTable.isDeleted());
		});

		// Current version should be marked as deleted
		runInTransaction(() -> {
			ResourceHistoryTable resourceTable = myResourceHistoryTableDao.findForIdAndVersionAndFetchProvenance(id.getIdPartAsLong(), 1);
			assertNull(resourceTable.getDeleted());
			assertNotNull(resourceTable.getPersistentId());
		});
		runInTransaction(() -> {
			ResourceHistoryTable resourceTable = myResourceHistoryTableDao.findForIdAndVersionAndFetchProvenance(id.getIdPartAsLong(), 2);
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
		myDaoConfig.setDeleteEnabled(false);

		Patient p = new Patient();
		p.setActive(true);
		IIdType pId = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		try {
			myPatientDao.delete(pId);
			fail();
		} catch (PreconditionFailedException e) {
			assertEquals("Resource deletion is not permitted on this server", e.getMessage());
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
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(createResponse));

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
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(deleteTransaction));
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
			ResourceHistoryTable resourceTable = myResourceHistoryTableDao.findForIdAndVersionAndFetchProvenance(id.getIdPartAsLong(), 2);
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
	public void testDeleteIgnoreReferentialIntegrityForPaths() {



	}


}
