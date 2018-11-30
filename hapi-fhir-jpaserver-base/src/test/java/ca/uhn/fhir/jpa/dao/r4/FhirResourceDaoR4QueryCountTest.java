package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.util.TestUtil;
import net.ttddyy.dsproxy.QueryCount;
import net.ttddyy.dsproxy.listener.SingleQueryCountHolder;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import static org.junit.Assert.assertEquals;

@TestPropertySource(properties = {
	"scheduling_disabled=true"
})
public class FhirResourceDaoR4QueryCountTest extends BaseJpaR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4QueryCountTest.class);
	@Autowired
	private SingleQueryCountHolder myCountHolder;

	@After
	public void afterResetDao() {
		myDaoConfig.setResourceMetaCountHardLimit(new DaoConfig().getResourceMetaCountHardLimit());
		myDaoConfig.setIndexMissingFields(new DaoConfig().getIndexMissingFields());
	}

	@Test
	public void testWritesPerformMinimalSqlStatements() {
		Patient p = new Patient();
		p.addIdentifier().setSystem("sys1").setValue("val1");
		p.addIdentifier().setSystem("sys2").setValue("val2");

		ourLog.info("** About to perform write");
		myCountHolder.clear();

		IIdType id = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		ourLog.info("** Done performing write");

		assertEquals(6, getQueryCount().getInsert());
		assertEquals(0, getQueryCount().getUpdate());

		/*
		 * Not update the value
		 */

		p = new Patient();
		p.setId(id);
		p.addIdentifier().setSystem("sys1").setValue("val3");
		p.addIdentifier().setSystem("sys2").setValue("val4");

		ourLog.info("** About to perform write 2");
		myCountHolder.clear();

		myPatientDao.update(p).getId().toUnqualifiedVersionless();

		ourLog.info("** Done performing write 2");

		assertEquals(2, getQueryCount().getInsert());
		assertEquals(1, getQueryCount().getUpdate());
		assertEquals(1, getQueryCount().getDelete());
	}

	@Test
	public void testSearch() {

		for (int i = 0; i < 20; i++) {
			Patient p = new Patient();
			p.addIdentifier().setSystem("sys1").setValue("val" + i);
			myPatientDao.create(p);
		}

		myCountHolder.clear();

		ourLog.info("** About to perform search");
		IBundleProvider search = myPatientDao.search(new SearchParameterMap());
		ourLog.info("** About to retrieve resources");
		search.getResources(0, 20);
		ourLog.info("** Done retrieving resources");

		assertEquals(4, getQueryCount().getSelect());
		assertEquals(2, getQueryCount().getInsert());
		assertEquals(1, getQueryCount().getUpdate());
		assertEquals(0, getQueryCount().getDelete());

	}

	private QueryCount getQueryCount() {
		return myCountHolder.getQueryCountMap().get("");
	}

	@Test
	public void testCreateClientAssignedId() {
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.DISABLED);

		myCountHolder.clear();
		ourLog.info("** Starting Update Non-Existing resource with client assigned ID");
		Patient p = new Patient();
		p.setId("A");
		p.getPhotoFirstRep().setCreationElement(new DateTimeType("2011")); // non-indexed field
		myPatientDao.update(p).getId().toUnqualifiedVersionless();

		assertEquals(1, getQueryCount().getSelect());
		assertEquals(4, getQueryCount().getInsert());
		assertEquals(0, getQueryCount().getDelete());
		// Because of the forced ID's bidirectional link HFJ_RESOURCE <-> HFJ_FORCED_ID
		assertEquals(1, getQueryCount().getUpdate());
		runInTransaction(() -> {
			assertEquals(1, myResourceTableDao.count());
			assertEquals(1, myResourceHistoryTableDao.count());
			assertEquals(1, myForcedIdDao.count());
			assertEquals(1, myResourceIndexedSearchParamTokenDao.count());
		});

		// Ok how about an update

		myCountHolder.clear();
		ourLog.info("** Starting Update Existing resource with client assigned ID");
		p = new Patient();
		p.setId("A");
		p.getPhotoFirstRep().setCreationElement(new DateTimeType("2012")); // non-indexed field
		myPatientDao.update(p).getId().toUnqualifiedVersionless();

		assertEquals(5, getQueryCount().getSelect());
		assertEquals(1, getQueryCount().getInsert());
		assertEquals(0, getQueryCount().getDelete());
		assertEquals(1, getQueryCount().getUpdate());
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

		myCountHolder.clear();
		Patient p = new Patient();
		p.getPhotoFirstRep().setCreationElement(new DateTimeType("2011")); // non-indexed field
		IIdType id = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		assertEquals(3, getQueryCount().getInsert());
		runInTransaction(() -> {
			assertEquals(1, myResourceTableDao.count());
			assertEquals(1, myResourceHistoryTableDao.count());
		});

		myCountHolder.clear();
		p = new Patient();
		p.setId(id);
		p.getPhotoFirstRep().setCreationElement(new DateTimeType("2012")); // non-indexed field
		myPatientDao.update(p).getId().toUnqualifiedVersionless();

		assertEquals(1, getQueryCount().getInsert());
		runInTransaction(() -> {
			assertEquals(1, myResourceTableDao.count());
			assertEquals(2, myResourceHistoryTableDao.count());
		});

	}


	@Test
	public void testUpdateReusesIndexes() {
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.DISABLED);

		myCountHolder.clear();

		Patient pt = new Patient();
		pt.setActive(true);
		pt.addName().setFamily("FAMILY1").addGiven("GIVEN1A").addGiven("GIVEN1B");
		IIdType id = myPatientDao.create(pt).getId().toUnqualifiedVersionless();

		ourLog.info("Now have {} deleted", getQueryCount().getDelete());
		ourLog.info("Now have {} inserts", getQueryCount().getInsert());
		myCountHolder.clear();

		ourLog.info("** About to update");

		pt.setId(id);
		pt.getNameFirstRep().addGiven("GIVEN1C");
		myPatientDao.update(pt);

		ourLog.info("Now have {} deleted", getQueryCount().getDelete());
		ourLog.info("Now have {} inserts", getQueryCount().getInsert());
		assertEquals(0, getQueryCount().getDelete());
		assertEquals(2, getQueryCount().getInsert());
	}


	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
