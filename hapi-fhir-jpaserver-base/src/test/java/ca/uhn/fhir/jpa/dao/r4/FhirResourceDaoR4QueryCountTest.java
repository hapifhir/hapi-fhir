package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.TestUtil;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.*;
import org.junit.*;
import org.springframework.test.context.TestPropertySource;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.reset;

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

	@Before
	public void before() {
		myInterceptorRegistry.registerInterceptor(myInterceptor);
	}


	@Test
	public void testUpdateWithNoChanges() {
		IIdType id = runInTransaction(() -> {
			Patient p = new Patient();
			p.addIdentifier().setSystem("urn:system").setValue("2");
			return myPatientDao.create(p).getId().toUnqualified();
		});

		myCaptureQueriesListener.clear();
		runInTransaction(() -> {
			Patient p = new Patient();
			p.setId(id.getIdPart());
			p.addIdentifier().setSystem("urn:system").setValue("2");
			myPatientDao.update(p).getResource();
		});
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(5, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getUpdateQueriesForCurrentThread().size());
		assertThat(myCaptureQueriesListener.getInsertQueriesForCurrentThread(), empty());
		assertThat(myCaptureQueriesListener.getDeleteQueriesForCurrentThread(), empty());
	}


	@Test
	public void testUpdateWithChanges() {
		IIdType id = runInTransaction(() -> {
			Patient p = new Patient();
			p.addIdentifier().setSystem("urn:system").setValue("2");
			return myPatientDao.create(p).getId().toUnqualified();
		});

		myCaptureQueriesListener.clear();
		runInTransaction(() -> {
			Patient p = new Patient();
			p.setId(id.getIdPart());
			p.addIdentifier().setSystem("urn:system").setValue("3");
			myPatientDao.update(p).getResource();
		});
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(6, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertEquals(2, myCaptureQueriesListener.getUpdateQueriesForCurrentThread().size());
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertEquals(1, myCaptureQueriesListener.getInsertQueriesForCurrentThread().size());
		myCaptureQueriesListener.logDeleteQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getDeleteQueriesForCurrentThread().size());
	}

	@Test
	public void testRead() {
		IIdType id = runInTransaction(() -> {
			Patient p = new Patient();
			p.addIdentifier().setSystem("urn:system").setValue("2");
			return myPatientDao.create(p).getId().toUnqualified();
		});

		myCaptureQueriesListener.clear();
		runInTransaction(() -> {
			myPatientDao.read(id.toVersionless());
		});
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(2, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getUpdateQueriesForCurrentThread().size());
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getInsertQueriesForCurrentThread().size());
		myCaptureQueriesListener.logDeleteQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getDeleteQueriesForCurrentThread().size());
	}

	@Test
	public void testVRead() {
		IIdType id = runInTransaction(() -> {
			Patient p = new Patient();
			p.addIdentifier().setSystem("urn:system").setValue("2");
			return myPatientDao.create(p).getId().toUnqualified();
		});

		myCaptureQueriesListener.clear();
		runInTransaction(() -> {
			myPatientDao.read(id.withVersion("1"));
		});
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(2, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getUpdateQueriesForCurrentThread().size());
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getInsertQueriesForCurrentThread().size());
		myCaptureQueriesListener.logDeleteQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getDeleteQueriesForCurrentThread().size());
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
