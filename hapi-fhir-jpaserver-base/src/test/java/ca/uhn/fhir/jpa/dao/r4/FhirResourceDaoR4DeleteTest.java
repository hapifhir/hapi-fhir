package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.*;
import org.junit.AfterClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.junit.Assert.*;

public class FhirResourceDaoR4DeleteTest extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(FhirResourceDaoR4DeleteTest.class);

	@Test
	public void testDeleteMarksResourceAndVersionAsDeleted() {

		Patient p = new Patient();
		p.setActive(true);
		IIdType id = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		myPatientDao.delete(id);

		// Table should be marked as deleted
		runInTransaction(()->{
			ResourceTable resourceTable = myResourceTableDao.findById(id.getIdPartAsLong()).get();
			assertNotNull(resourceTable.getDeleted());
		});

		// Current version should be marked as deleted
		runInTransaction(()->{
			ResourceHistoryTable resourceTable = myResourceHistoryTableDao.findForIdAndVersionAndFetchProvenance(id.getIdPartAsLong(), 1);
			assertNull(resourceTable.getDeleted());
		});
		runInTransaction(()->{
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
	public void testDeleteCircularReferenceInTransaction() throws IOException {

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
		runInTransaction(()->{
			ResourceTable resourceTable = myResourceTableDao.findById(id.getIdPartAsLong()).get();
			assertNotNull(resourceTable.getDeleted());
		});

		// Mark the current history version as not-deleted even though the actual resource
		// table entry is marked deleted
		runInTransaction(()->{
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

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
