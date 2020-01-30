package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.bulk.IBulkDataExportSvc;
import ca.uhn.fhir.jpa.config.TestR4WithLuceneDisabledConfig;
import ca.uhn.fhir.jpa.dao.BaseJpaTest;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.dao.dstu2.FhirResourceDaoDstu2SearchNoFtTest;
import ca.uhn.fhir.jpa.search.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.search.reindex.IResourceReindexingSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.jpa.sp.ISearchParamPresenceSvc;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.hapi.ctx.IValidationSupport;
import org.hl7.fhir.r4.model.*;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestR4WithLuceneDisabledConfig.class})
@DirtiesContext
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
	@Qualifier("myValueSetDaoR4")
	private IFhirResourceDaoValueSet<ValueSet, ?, ?> myValueSetDao;
	@Autowired
	@Qualifier("myObservationDaoR4")
	private IFhirResourceDao<Observation> myObservationDao;
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
	@Autowired
	private IBulkDataExportSvc myBulkDataExportSvc;
	@Autowired
	private ITermReadSvc myTermSvc;

	@Before
	@Transactional()
	public void beforePurgeDatabase() {
		purgeDatabase(myDaoConfig, mySystemDao, myResourceReindexingSvc, mySearchCoordinatorSvc, mySearchParamRegistry, myBulkDataExportSvc);
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

	@Test
	public void testExpandValueSet() {
		CodeSystem cs = new CodeSystem();
		cs.setUrl("http://fooCS");
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		cs.addConcept().setCode("CODEA");
		cs.addConcept().setCode("CODEB");
		myCodeSystemDao.create(cs);

		ValueSet vs = new ValueSet();
		vs.setUrl("http://fooVS");
		vs.getCompose()
			.addInclude()
			.setSystem("http://fooCS")
			.addConcept(new ValueSet.ConceptReferenceComponent().setCode("CODEA"));

		// Explicit expand
		ValueSet outcome = myValueSetDao.expand(vs, null);
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
		assertEquals("CODEA", outcome.getExpansion().getContains().get(0).getCode());

		// Deferred expand
		IIdType id = myValueSetDao.create(vs).getId().toUnqualifiedVersionless();
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();
		outcome = myValueSetDao.expand(id, null, mySrd);
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
		assertEquals("CODEA", outcome.getExpansion().getContains().get(0).getCode());
	}


	@Test
	@Ignore
	public void testExpandValueSetWithFilter() {
		CodeSystem cs = new CodeSystem();
		cs.setUrl("http://fooCS");
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		cs.addConcept().setCode("CODEA");
		cs.addConcept().setCode("CODEB");
		myCodeSystemDao.create(cs);

		ValueSet vs = new ValueSet();
		vs.setUrl("http://fooVS");
		vs.getCompose()
			.addInclude()
			.setSystem("http://fooCS")
			.addFilter()
			.setOp(ValueSet.FilterOperator.EQUAL)
			.setProperty("code")
			.setValue("CODEA");

		try {
			myValueSetDao.expand(vs, null);
		} catch (NullPointerException e) {
			assertEquals("", e.getMessage());
		}
	}

	@Test
	public void testSearchByCodeIn() {
		CodeSystem cs = new CodeSystem();
		cs.setUrl("http://fooCS");
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		cs.addConcept().setCode("CODEA");
		cs.addConcept().setCode("CODEB");
		myCodeSystemDao.create(cs);

		ValueSet vs = new ValueSet();
		vs.setUrl("http://fooVS");
		vs.getCompose()
			.addInclude()
			.setSystem("http://fooCS")
			.addConcept(new ValueSet.ConceptReferenceComponent().setCode("CODEA"));
		myValueSetDao.create(vs);


		Observation obs = new Observation();
		obs.getCode().addCoding().setSystem("http://fooCS").setCode("CODEA");
		String obs1id = myObservationDao.create(obs).getId().toUnqualifiedVersionless().getValue();

		obs = new Observation();
		obs.getCode().addCoding().setSystem("http://fooCS").setCode("CODEB");
		myObservationDao.create(obs).getId().toUnqualifiedVersionless().getValue();

		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add("code", new TokenParam("http://fooVS").setModifier(TokenParamModifier.IN));
		IBundleProvider results = myObservationDao.search(map);
		List<IBaseResource> resultsList = results.getResources(0, 10);
		assertEquals(1, resultsList.size());
		assertEquals(obs1id, resultsList.get(0).getIdElement().toUnqualifiedVersionless().getValue());

	}

	protected <T extends IBaseResource> T loadResourceFromClasspath(Class<T> type, String resourceName) throws IOException {
		InputStream stream = FhirResourceDaoDstu2SearchNoFtTest.class.getResourceAsStream(resourceName);
		if (stream == null) {
			fail("Unable to load resource: " + resourceName);
		}
		String string = IOUtils.toString(stream, StandardCharsets.UTF_8);
		IParser newJsonParser = EncodingEnum.detectEncodingNoDefault(string).newParser(myFhirCtx);
		return newJsonParser.parseResource(type, string);
	}


	/**
	 * A valueset that includes a whole system (i.e. no properties) should expand
	 */
	@Test
	public void testExpandValueSetContainingSystemIncludeWithNoCodes() throws IOException {
		CodeSystem cs = loadResourceFromClasspath(CodeSystem.class, "/dstu3/iar/CodeSystem-iar-citizenship-status.xml");
		myCodeSystemDao.create(cs);

		ValueSet vs = loadResourceFromClasspath(ValueSet.class, "/dstu3/iar/ValueSet-iar-citizenship-status.xml");
		myValueSetDao.create(vs);

		ValueSet expansion = myValueSetDao.expandByIdentifier("http://ccim.on.ca/fhir/iar/ValueSet/iar-citizenship-status", null);
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expansion));

		assertEquals(6, expansion.getExpansion().getContains().size());

	}


	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
