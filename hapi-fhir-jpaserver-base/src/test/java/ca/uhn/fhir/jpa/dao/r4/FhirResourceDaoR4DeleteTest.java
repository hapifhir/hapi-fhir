package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.*;
import org.junit.AfterClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
			ResourceHistoryTable resourceTable = myResourceHistoryTableDao.findForIdAndVersion(id.getIdPartAsLong(), 1);
			assertNull(resourceTable.getDeleted());
		});
		runInTransaction(()->{
			ResourceHistoryTable resourceTable = myResourceHistoryTableDao.findForIdAndVersion(id.getIdPartAsLong(), 2);
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
			ResourceHistoryTable resourceTable = myResourceHistoryTableDao.findForIdAndVersion(id.getIdPartAsLong(), 2);
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
