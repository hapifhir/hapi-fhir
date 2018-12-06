package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.util.TestUtil;
import net.ttddyy.dsproxy.QueryCount;
import net.ttddyy.dsproxy.listener.SingleQueryCountHolder;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Organization;
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

		assertEquals(1, getQueryCount().getInsert());
		assertEquals(2, getQueryCount().getUpdate());
		assertEquals(0, getQueryCount().getDelete());
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

		assertEquals(4, getQueryCount().getSelect());
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

		myCountHolder.clear();

		ourLog.info("** About to update");

		pt.setId(id);
		pt.getNameFirstRep().addGiven("GIVEN1C");
		myPatientDao.update(pt);

		assertEquals(0, getQueryCount().getDelete());
		assertEquals(2, getQueryCount().getInsert());
	}


	@Test
	public void testUpdateReusesIndexesString() {
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.DISABLED);
		SearchParameterMap m1 = new SearchParameterMap().add("family", new StringParam("family1")).setLoadSynchronous(true);
		SearchParameterMap m2 = new SearchParameterMap().add("family", new StringParam("family2")).setLoadSynchronous(true);

		myCountHolder.clear();

		Patient pt = new Patient();
		pt.addName().setFamily("FAMILY1");
		IIdType id = myPatientDao.create(pt).getId().toUnqualifiedVersionless();

		myCountHolder.clear();

		assertEquals(1, myPatientDao.search(m1).size().intValue());
		assertEquals(0, myPatientDao.search(m2).size().intValue());

		ourLog.info("** About to update");

		pt = new Patient();
		pt.setId(id);
		pt.addName().setFamily("FAMILY2");
		myPatientDao.update(pt);

		assertEquals(0, getQueryCount().getDelete());
		assertEquals(1, getQueryCount().getInsert()); // Add an entry to HFJ_RES_VER
		assertEquals(2, getQueryCount().getUpdate()); // Update SPIDX_STRING and HFJ_RESOURCE

		assertEquals(0, myPatientDao.search(m1).size().intValue());
		assertEquals(1, myPatientDao.search(m2).size().intValue());
	}


	@Test
	public void testUpdateReusesIndexesToken() {
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.DISABLED);
		SearchParameterMap m1 = new SearchParameterMap().add("gender", new TokenParam("male")).setLoadSynchronous(true);
		SearchParameterMap m2 = new SearchParameterMap().add("gender", new TokenParam("female")).setLoadSynchronous(true);

		myCountHolder.clear();

		Patient pt = new Patient();
		pt.setGender(Enumerations.AdministrativeGender.MALE);
		IIdType id = myPatientDao.create(pt).getId().toUnqualifiedVersionless();

		assertEquals(0, getQueryCount().getSelect());
		assertEquals(0, getQueryCount().getDelete());
		assertEquals(3, getQueryCount().getInsert());
		assertEquals(0, getQueryCount().getUpdate());
		assertEquals(1, myPatientDao.search(m1).size().intValue());
		assertEquals(0, myPatientDao.search(m2).size().intValue());

		/*
		 * Change a value
		 */

		ourLog.info("** About to update");
		myCountHolder.clear();

		pt = new Patient();
		pt.setId(id);
		pt.setGender(Enumerations.AdministrativeGender.FEMALE);
		myPatientDao.update(pt);

		/*
		 * Current SELECTs:
		 *   Select the resource from HFJ_RESOURCE
		 *   Select the version from HFJ_RES_VER
		 *   Select the current token indexes
		 */
		assertEquals(3, getQueryCount().getSelect());
		assertEquals(0, getQueryCount().getDelete());
		assertEquals(1, getQueryCount().getInsert()); // Add an entry to HFJ_RES_VER
		assertEquals(2, getQueryCount().getUpdate()); // Update SPIDX_STRING and HFJ_RESOURCE

		assertEquals(0, myPatientDao.search(m1).size().intValue());
		assertEquals(1, myPatientDao.search(m2).size().intValue());
		myCountHolder.clear();

		/*
		 * Drop a value
		 */

		ourLog.info("** About to update again");

		pt = new Patient();
		pt.setId(id);
		myPatientDao.update(pt);

		assertEquals(1, getQueryCount().getDelete());
		assertEquals(1, getQueryCount().getInsert());
		assertEquals(1, getQueryCount().getUpdate());

		assertEquals(0, myPatientDao.search(m1).size().intValue());
		assertEquals(0, myPatientDao.search(m2).size().intValue());

	}

	@Test
	public void testUpdateReusesIndexesResourceLink() {
		Organization org1 = new Organization();
		org1.setName("org1");
		IIdType orgId1 = myOrganizationDao.create(org1).getId().toUnqualifiedVersionless();
		Organization org2 = new Organization();
		org2.setName("org2");
		IIdType orgId2 = myOrganizationDao.create(org2).getId().toUnqualifiedVersionless();

		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.DISABLED);
		SearchParameterMap m1 = new SearchParameterMap().add("organization", new ReferenceParam(orgId1.getValue())).setLoadSynchronous(true);
		SearchParameterMap m2 = new SearchParameterMap().add("organization", new ReferenceParam(orgId2.getValue())).setLoadSynchronous(true);

		myCountHolder.clear();

		Patient pt = new Patient();
		pt.getManagingOrganization().setReference(orgId1.getValue());
		IIdType id = myPatientDao.create(pt).getId().toUnqualifiedVersionless();

		myCountHolder.clear();

		assertEquals(1, myPatientDao.search(m1).size().intValue());
		assertEquals(0, myPatientDao.search(m2).size().intValue());

		ourLog.info("** About to update");

		pt = new Patient();
		pt.setId(id);
		pt.getManagingOrganization().setReference(orgId2.getValue());
		myPatientDao.update(pt);

		assertEquals(0, getQueryCount().getDelete());
		assertEquals(1, getQueryCount().getInsert()); // Add an entry to HFJ_RES_VER
		assertEquals(2, getQueryCount().getUpdate()); // Update SPIDX_STRING and HFJ_RESOURCE

		assertEquals(0, myPatientDao.search(m1).size().intValue());
		assertEquals(1, myPatientDao.search(m2).size().intValue());
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
