package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.config.TestR4WithoutLuceneConfig;
import ca.uhn.fhir.jpa.dao.*;
import ca.uhn.fhir.jpa.search.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.search.reindex.IResourceReindexingSvc;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.sp.ISearchParamPresenceSvc;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.hapi.ctx.IValidationSupport;
import org.hl7.fhir.r4.model.*;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestR4WithoutLuceneConfig.class})
public class FhirResourceDaoR4SearchWithLuceneDisabledTest extends BaseJpaTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4SearchWithLuceneDisabledTest.class);
	@Autowired
	protected DaoConfig myDaoConfig;
	@Autowired
	protected PlatformTransactionManager myTxManager;
	@Autowired
	protected ISearchParamPresenceSvc mySearchParamPresenceSvc;
	@Autowired
	protected ISearchCoordinatorSvc mySearchCoordinatorSvc;
	@Autowired
	protected ISearchParamRegistry mySearchParamRegistry;
	@Autowired
	@Qualifier("myAllergyIntoleranceDaoR4")
	private IFhirResourceDao<AllergyIntolerance> myAllergyIntoleranceDao;
	@Autowired
	@Qualifier("myAppointmentDaoR4")
	private IFhirResourceDao<Appointment> myAppointmentDao;
	@Autowired
	@Qualifier("myAuditEventDaoR4")
	private IFhirResourceDao<AuditEvent> myAuditEventDao;
	@Autowired
	@Qualifier("myBundleDaoR4")
	private IFhirResourceDao<Bundle> myBundleDao;
	@Autowired
	@Qualifier("myCarePlanDaoR4")
	private IFhirResourceDao<CarePlan> myCarePlanDao;
	@Autowired
	@Qualifier("myCodeSystemDaoR4")
	private IFhirResourceDao<CodeSystem> myCodeSystemDao;
	@Autowired
	@Qualifier("myCompartmentDefinitionDaoR4")
	private IFhirResourceDao<CompartmentDefinition> myCompartmentDefinitionDao;
	@Autowired
	@Qualifier("myConceptMapDaoR4")
	private IFhirResourceDao<ConceptMap> myConceptMapDao;
	@Autowired
	@Qualifier("myConditionDaoR4")
	private IFhirResourceDao<Condition> myConditionDao;
	@Autowired
	@Qualifier("myDeviceDaoR4")
	private IFhirResourceDao<Device> myDeviceDao;
	@Autowired
	@Qualifier("myDiagnosticReportDaoR4")
	private IFhirResourceDao<DiagnosticReport> myDiagnosticReportDao;
	@Autowired
	@Qualifier("myEncounterDaoR4")
	private IFhirResourceDao<Encounter> myEncounterDao;
	// @PersistenceContext()
	@Autowired
	private EntityManager myEntityManager;
	@Autowired
	private FhirContext myFhirCtx;
	@Autowired
	@Qualifier("myOrganizationDaoR4")
	private IFhirResourceDao<Organization> myOrganizationDao;
	@Autowired
	@Qualifier("myJpaValidationSupportChainR4")
	private IValidationSupport myValidationSupport;
	@Autowired
	private IFhirSystemDao<Bundle, Meta> mySystemDao;
	@Autowired
	private IResourceReindexingSvc myResourceReindexingSvc;

	@Before
	@Transactional()
	public void beforePurgeDatabase() {
		purgeDatabase(myDaoConfig, mySystemDao, myResourceReindexingSvc, mySearchCoordinatorSvc, mySearchParamRegistry);
	}

	@Before
	public void beforeResetConfig() {
		myDaoConfig.setHardSearchLimit(1000);
		myDaoConfig.setHardTagListLimit(1000);
		myDaoConfig.setIncludeLimit(2000);
	}

	@Override
	protected PlatformTransactionManager getTxManager() {
		return myTxManager;
	}

	@Override
	protected FhirContext getContext() {
		return myFhirCtx;
	}

	@Test
	public void testSearchWithContent() {
		String methodName = "testEverythingIncludesBackReferences";

		Organization org = new Organization();
		org.setName(methodName);
		IIdType orgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap map = new SearchParameterMap();
		map.add(ca.uhn.fhir.rest.api.Constants.PARAM_CONTENT, new StringParam(methodName));
		try {
			myOrganizationDao.search(map).size();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Fulltext search is not enabled on this service, can not process parameter: _content", e.getMessage());
		}
	}

	@Test
	public void testSearchWithRegularParam() {
		String methodName = "testEverythingIncludesBackReferences";

		Organization org = new Organization();
		org.setName(methodName);
		IIdType orgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap map = new SearchParameterMap();
		map.add(Organization.SP_NAME, new StringParam(methodName));
		myOrganizationDao.search(map);

	}

	@Test
	public void testSearchWithText() {
		String methodName = "testEverythingIncludesBackReferences";

		Organization org = new Organization();
		org.setName(methodName);
		IIdType orgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap map = new SearchParameterMap();
		map.add(ca.uhn.fhir.rest.api.Constants.PARAM_TEXT, new StringParam(methodName));
		try {
			myOrganizationDao.search(map).size();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Fulltext search is not enabled on this service, can not process parameter: _text", e.getMessage());
		}
	}


	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
