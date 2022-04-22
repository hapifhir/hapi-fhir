package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkDataExportJobSchedulingHelper;
import ca.uhn.fhir.jpa.test.config.TestR4Config;
import ca.uhn.fhir.jpa.dao.dstu2.FhirResourceDaoDstu2SearchNoFtTest;
import ca.uhn.fhir.jpa.search.reindex.IResourceReindexingSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.sp.ISearchParamPresenceSvc;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.jpa.test.BaseJpaTest;
import ca.uhn.fhir.jpa.test.config.TestHibernateSearchAddInConfig;
import ca.uhn.fhir.jpa.util.ValueSetTestUtil;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.Appointment;
import org.hl7.fhir.r4.model.AuditEvent;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.CompartmentDefinition;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Device;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static ca.uhn.fhir.util.HapiExtensions.EXT_VALUESET_EXPANSION_MESSAGE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestR4Config.class, TestHibernateSearchAddInConfig.NoFT.class})
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
	@Qualifier("myJpaValidationSupportChain")
	private IValidationSupport myValidationSupport;
	@Autowired
	private IFhirSystemDao<Bundle, Meta> mySystemDao;
	@Autowired
	private IResourceReindexingSvc myResourceReindexingSvc;
	@Autowired
	private IBulkDataExportJobSchedulingHelper myBulkDataScheduleHelper;
	@Autowired
	private ITermReadSvc myTermSvc;

	@BeforeEach
	@Transactional()
	public void beforePurgeDatabase() {
		purgeDatabase(myDaoConfig, mySystemDao, myResourceReindexingSvc, mySearchCoordinatorSvc, mySearchParamRegistry, myBulkDataScheduleHelper);
	}

	@Override
	protected PlatformTransactionManager getTxManager() {
		return myTxManager;
	}

	@Override
	protected FhirContext getFhirContext() {
		return myFhirCtx;
	}

	@Test
	public void testSearchWithContent() {
		String methodName = "testEverythingIncludesBackReferences";

		Organization org = new Organization();
		org.setName(methodName);
		myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap map = new SearchParameterMap();
		map.add(ca.uhn.fhir.rest.api.Constants.PARAM_CONTENT, new StringParam(methodName));
		try {
			myOrganizationDao.search(map).size();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(1192) + "Fulltext search is not enabled on this service, can not process parameter: _content", e.getMessage());
		}
	}

	@Test
	public void testSearchWithRegularParam() {
		String methodName = "testEverythingIncludesBackReferences";

		Organization org = new Organization();
		org.setName(methodName);
		myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();

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
			assertEquals(Msg.code(1192) + "Fulltext search is not enabled on this service, can not process parameter: _text", e.getMessage());
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
	@Disabled
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
	public void testExpandValueSetPreservesExplicitOrder() {
		CodeSystem cs = new CodeSystem();
		cs.setId("cs");
		cs.setUrl("http://cs");
		cs.addConcept().setCode("code1");
		cs.addConcept().setCode("code2");
		cs.addConcept().setCode("code3");
		cs.addConcept().setCode("code4");
		cs.addConcept().setCode("code5");
		myCodeSystemDao.update(cs);

		// Vs in reverse order
		ValueSet vs = new ValueSet();
		vs.setId("vs");
		vs.setUrl("http://vs");
		// Add some codes in separate compose sections, and some more codes in a single compose section.
		// Order should be preserved for all of them.
		vs.getCompose().addInclude().setSystem("http://cs")
			.addConcept(new ValueSet.ConceptReferenceComponent(new CodeType("code5")));
		vs.getCompose().addInclude().setSystem("http://cs")
			.addConcept(new ValueSet.ConceptReferenceComponent(new CodeType("code4")));
		vs.getCompose().addInclude().setSystem("http://cs")
			.addConcept(new ValueSet.ConceptReferenceComponent(new CodeType("code3")))
			.addConcept(new ValueSet.ConceptReferenceComponent(new CodeType("code2")))
			.addConcept(new ValueSet.ConceptReferenceComponent(new CodeType("code1")));
		myValueSetDao.update(vs);

		// Non Pre-Expanded
		ValueSet outcome = myValueSetDao.expand(vs, new ValueSetExpansionOptions());
		assertEquals("ValueSet \"ValueSet.url[http://vs]\" has not yet been pre-expanded. Performing in-memory expansion without parameters. Current status: NOT_EXPANDED | The ValueSet is waiting to be picked up and pre-expanded by a scheduled task.", outcome.getMeta().getExtensionString(EXT_VALUESET_EXPANSION_MESSAGE));
		assertThat(ValueSetTestUtil.toCodes(outcome).toString(), ValueSetTestUtil.toCodes(outcome), contains(
			"code5", "code4", "code3", "code2", "code1"
		));


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

	/**
	 * A valueset that includes a whole system (i.e. no properties) should expand
	 */
	@Test
	public void testExpandValueSetContainingSystemIncludeWithNoCodes() throws IOException {
		CodeSystem cs = loadResourceFromClasspath(CodeSystem.class, "/r4/iar/CodeSystem-iar-citizenship-status.xml");
		myCodeSystemDao.create(cs);

		ValueSet vs = loadResourceFromClasspath(ValueSet.class, "/r4/iar/ValueSet-iar-citizenship-status.xml");
		myValueSetDao.create(vs);

		ValueSet expansion = myValueSetDao.expandByIdentifier("http://ccim.on.ca/fhir/iar/ValueSet/iar-citizenship-status", null);
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expansion));

		assertEquals(6, expansion.getExpansion().getContains().size());

	}

	@Test
	public void testExpandValueSetContainingSystemIncludeAndExcludeWithNoCodes() throws IOException {
		CodeSystem cs = loadResourceFromClasspath(CodeSystem.class, "/r4/iar/CodeSystem-iar-citizenship-status.xml");
		myCodeSystemDao.create(cs);

		ValueSet vs = loadResourceFromClasspath(ValueSet.class, "/r4/iar/ValueSet-iar-citizenship-status.xml");
		ValueSet.ConceptSetComponent excludeComponent = new ValueSet.ConceptSetComponent().setSystem("http://ccim.on.ca/fhir/iar/CodeSystem/iar-citizenship-status");
		excludeComponent.addConcept().setCode("REF");
		vs.getCompose().addExclude(excludeComponent);
		myValueSetDao.create(vs);

		CodeSystem cs2 = loadResourceFromClasspath(CodeSystem.class, "/extensional-case-3-cs.xml");
		myCodeSystemDao.create(cs2);

		ValueSet vs2 = loadResourceFromClasspath(ValueSet.class, "/extensional-case-3-vs-with-exclude.xml");
		myValueSetDao.create(vs2);

		ValueSet expansion = myValueSetDao.expandByIdentifier("http://ccim.on.ca/fhir/iar/ValueSet/iar-citizenship-status", null);
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expansion));

		assertEquals(5, expansion.getExpansion().getContains().size());

		ValueSet expansion2 = myValueSetDao.expandByIdentifier("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2", null);
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expansion2));

		assertEquals(22, expansion2.getExpansion().getContains().size());

	}


}
