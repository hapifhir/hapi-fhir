package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.util.TestUtil;
import net.ttddyy.dsproxy.QueryCountHolder;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import org.springframework.test.context.TestPropertySource;

import static org.junit.Assert.assertEquals;

@TestPropertySource(properties = {
	"scheduling_disabled=true"
})
public class FhirResourceDaoR4QueryCountTest extends BaseJpaR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4QueryCountTest.class);

	@After
	public void afterResetDao() {
		myDaoConfig.setResourceMetaCountHardLimit(new DaoConfig().getResourceMetaCountHardLimit());
		myDaoConfig.setIndexMissingFields(new DaoConfig().getIndexMissingFields());
	}

	@Test
	public void testCreateClientAssignedId() {
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.DISABLED);

		QueryCountHolder.clear();
		ourLog.info("** Starting Update Non-Existing resource with client assigned ID");
		Patient p = new Patient();
		p.setId("A");
		p.getPhotoFirstRep().setCreationElement(new DateTimeType("2011")); // non-indexed field
		myPatientDao.update(p).getId().toUnqualifiedVersionless();

		assertEquals(1, QueryCountHolder.getGrandTotal().getSelect());
		assertEquals(4, QueryCountHolder.getGrandTotal().getInsert());
		assertEquals(0, QueryCountHolder.getGrandTotal().getDelete());
		// Because of the forced ID's bidirectional link HFJ_RESOURCE <-> HFJ_FORCED_ID
		assertEquals(1, QueryCountHolder.getGrandTotal().getUpdate());
		runInTransaction(() -> {
			assertEquals(1, myResourceTableDao.count());
			assertEquals(1, myResourceHistoryTableDao.count());
			assertEquals(1, myForcedIdDao.count());
			assertEquals(1, myResourceIndexedSearchParamTokenDao.count());
		});

		// Ok how about an update

		QueryCountHolder.clear();
		ourLog.info("** Starting Update Existing resource with client assigned ID");
		p = new Patient();
		p.setId("A");
		p.getPhotoFirstRep().setCreationElement(new DateTimeType("2012")); // non-indexed field
		myPatientDao.update(p).getId().toUnqualifiedVersionless();

		assertEquals(5, QueryCountHolder.getGrandTotal().getSelect());
		assertEquals(1, QueryCountHolder.getGrandTotal().getInsert());
		assertEquals(0, QueryCountHolder.getGrandTotal().getDelete());
		assertEquals(1, QueryCountHolder.getGrandTotal().getUpdate());
		runInTransaction(() -> {
			assertEquals(1, myResourceTableDao.count());
			assertEquals(2, myResourceHistoryTableDao.count());
			assertEquals(1, myForcedIdDao.count());
			assertEquals(1, myResourceIndexedSearchParamTokenDao.count());
		});

	}


	@Test
	public void testOneRowPerUpdate() {
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.DISABLED);

		QueryCountHolder.clear();
		Patient p = new Patient();
		p.getPhotoFirstRep().setCreationElement(new DateTimeType("2011")); // non-indexed field
		IIdType id = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		assertEquals(3, QueryCountHolder.getGrandTotal().getInsert());
		runInTransaction(() -> {
			assertEquals(1, myResourceTableDao.count());
			assertEquals(1, myResourceHistoryTableDao.count());
		});

		QueryCountHolder.clear();
		p = new Patient();
		p.setId(id);
		p.getPhotoFirstRep().setCreationElement(new DateTimeType("2012")); // non-indexed field
		myPatientDao.update(p).getId().toUnqualifiedVersionless();

		assertEquals(1, QueryCountHolder.getGrandTotal().getInsert());
		runInTransaction(() -> {
			assertEquals(1, myResourceTableDao.count());
			assertEquals(2, myResourceHistoryTableDao.count());
		});

	}


	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
