package ca.uhn.fhir.jpa.dao.dstu3;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.io.InputStream;

import javax.persistence.EntityManager;

import org.apache.commons.io.IOUtils;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hl7.fhir.dstu3.hapi.validation.IValidationSupport;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.config.TestDstu3Config;
import ca.uhn.fhir.jpa.dao.*;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.dao.dstu2.FhirResourceDaoDstu2SearchNoFtTest;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.provider.dstu3.JpaSystemProviderDstu3;
import ca.uhn.fhir.jpa.search.IStaleSearchDeletingSvc;
import ca.uhn.fhir.jpa.term.IHapiTerminologySvc;
import ca.uhn.fhir.jpa.validation.JpaValidationSupportChainDstu3;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.method.MethodUtil;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.util.TestUtil;

//@formatter:off
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestDstu3Config.class})
//@formatter:on
public abstract class BaseJpaDstu3Test extends BaseJpaTest {

	private static JpaValidationSupportChainDstu3 ourJpaValidationSupportChainDstu3;
	private static IFhirResourceDaoValueSet<ValueSet, Coding, CodeableConcept> ourValueSetDao;

	
	@Autowired
	@Qualifier("mySearchParameterDaoDstu3")
	protected IFhirResourceDao<SearchParameter> mySearchParameterDao;
	@Autowired
	protected ISearchParamRegistry mySearchParamRegsitry;
//	@Autowired
//	protected HapiWorkerContext myHapiWorkerContext;
	@Autowired
	@Qualifier("myAllergyIntoleranceDaoDstu3")
	protected IFhirResourceDao<AllergyIntolerance> myAllergyIntoleranceDao;
	@Autowired
	protected ApplicationContext myAppCtx;
	@Autowired
	@Qualifier("myAppointmentDaoDstu3")
	protected IFhirResourceDao<Appointment> myAppointmentDao;
	@Autowired
	@Qualifier("myAuditEventDaoDstu3")
	protected IFhirResourceDao<AuditEvent> myAuditEventDao;
	@Autowired
	@Qualifier("myBundleDaoDstu3")
	protected IFhirResourceDao<Bundle> myBundleDao;
	@Autowired
	@Qualifier("myCarePlanDaoDstu3")
	protected IFhirResourceDao<CarePlan> myCarePlanDao;
	@Autowired
	@Qualifier("myProcedureRequestDaoDstu3")
	protected IFhirResourceDao<ProcedureRequest> myProcedureRequestDao;
	@Autowired
	@Qualifier("myCodeSystemDaoDstu3")
	protected IFhirResourceDaoCodeSystem<CodeSystem, Coding, CodeableConcept> myCodeSystemDao;
	@Autowired
	@Qualifier("myCompartmentDefinitionDaoDstu3")
	protected IFhirResourceDao<CompartmentDefinition> myCompartmentDefinitionDao;
	@Autowired
	@Qualifier("myConceptMapDaoDstu3")
	protected IFhirResourceDao<ConceptMap> myConceptMapDao;
	@Autowired
	@Qualifier("myConditionDaoDstu3")
	protected IFhirResourceDao<Condition> myConditionDao;
	@Autowired
	protected DaoConfig myDaoConfig;
	@Autowired
	@Qualifier("myDeviceDaoDstu3")
	protected IFhirResourceDao<Device> myDeviceDao;
	@Autowired
	@Qualifier("myDiagnosticReportDaoDstu3")
	protected IFhirResourceDao<DiagnosticReport> myDiagnosticReportDao;
	@Autowired
	@Qualifier("myEncounterDaoDstu3")
	protected IFhirResourceDao<Encounter> myEncounterDao;
	// @PersistenceContext()
	@Autowired
	protected EntityManager myEntityManager;
	@Autowired
	protected FhirContext myFhirCtx;
	@Autowired
	@Qualifier("myImmunizationDaoDstu3")
	protected IFhirResourceDao<Immunization> myImmunizationDao;
	protected IServerInterceptor myInterceptor;
	@Autowired
	private JpaValidationSupportChainDstu3 myJpaValidationSupportChainDstu3;
	@Autowired
	@Qualifier("myLocationDaoDstu3")
	protected IFhirResourceDao<Location> myLocationDao;
	@Autowired
	@Qualifier("myMediaDaoDstu3")
	protected IFhirResourceDao<Media> myMediaDao;
	@Autowired
	@Qualifier("myMedicationDaoDstu3")
	protected IFhirResourceDao<Medication> myMedicationDao;
	@Autowired
	@Qualifier("myMedicationAdministrationDaoDstu3")
	protected IFhirResourceDao<MedicationAdministration> myMedicationAdministrationDao;
	@Autowired
	@Qualifier("myMedicationRequestDaoDstu3")
	protected IFhirResourceDao<MedicationRequest> myMedicationRequestDao;
	@Autowired
	@Qualifier("myNamingSystemDaoDstu3")
	protected IFhirResourceDao<NamingSystem> myNamingSystemDao;
	@Autowired
	@Qualifier("myObservationDaoDstu3")
	protected IFhirResourceDao<Observation> myObservationDao;
	@Autowired
	@Qualifier("myOperationDefinitionDaoDstu3")
	protected IFhirResourceDao<OperationDefinition> myOperationDefinitionDao;
	@Autowired
	@Qualifier("myOrganizationDaoDstu3")
	protected IFhirResourceDao<Organization> myOrganizationDao;
	@Autowired
	@Qualifier("myPatientDaoDstu3")
	protected IFhirResourceDaoPatient<Patient> myPatientDao;
	@Autowired
	@Qualifier("myPractitionerDaoDstu3")
	protected IFhirResourceDao<Practitioner> myPractitionerDao;
	@Autowired
	@Qualifier("myQuestionnaireDaoDstu3")
	protected IFhirResourceDao<Questionnaire> myQuestionnaireDao;
	@Autowired
	@Qualifier("myQuestionnaireResponseDaoDstu3")
	protected IFhirResourceDao<QuestionnaireResponse> myQuestionnaireResponseDao;
	@Autowired
	@Qualifier("myResourceProvidersDstu3")
	protected Object myResourceProviders;
	@Autowired
	protected IResourceTableDao myResourceTableDao;
	@Autowired
	protected IFulltextSearchSvc mySearchDao;
	@Autowired
	protected IStaleSearchDeletingSvc myStaleSearchDeletingSvc;
	@Autowired
	@Qualifier("myStructureDefinitionDaoDstu3")
	protected IFhirResourceDao<StructureDefinition> myStructureDefinitionDao;
	@Autowired
	@Qualifier("mySubscriptionDaoDstu3")
	protected IFhirResourceDaoSubscription<Subscription> mySubscriptionDao;
	@Autowired
	@Qualifier("mySubstanceDaoDstu3")
	protected IFhirResourceDao<Substance> mySubstanceDao;
	@Autowired
	@Qualifier("mySystemDaoDstu3")
	protected IFhirSystemDao<Bundle, Meta> mySystemDao;
	@Autowired
	@Qualifier("mySystemProviderDstu3")
	protected JpaSystemProviderDstu3 mySystemProvider;
	@Autowired
	protected IHapiTerminologySvc myTermSvc;
	@Autowired
	protected PlatformTransactionManager myTxManager;
	@Autowired
	@Qualifier("myJpaValidationSupportChainDstu3")
	protected IValidationSupport myValidationSupport;
	@Autowired
	@Qualifier("myValueSetDaoDstu3")
	protected IFhirResourceDaoValueSet<ValueSet, Coding, CodeableConcept> myValueSetDao;
	@Autowired
	protected PlatformTransactionManager myTransactionMgr;
	@After()
	public void afterGrabCaches() {
		ourValueSetDao = myValueSetDao;
		ourJpaValidationSupportChainDstu3 = myJpaValidationSupportChainDstu3;
	}

	@Before
	public void beforeCreateInterceptor() {
		myInterceptor = mock(IServerInterceptor.class);
		myDaoConfig.setInterceptors(myInterceptor);
	}

	@Before
	@Transactional
	public void beforeFlushFT() {
		FullTextEntityManager ftem = Search.getFullTextEntityManager(myEntityManager);
		ftem.purgeAll(ResourceTable.class);
		ftem.purgeAll(ResourceIndexedSearchParamString.class);
		ftem.flushToIndexes();

		myDaoConfig.setSchedulingDisabled(true);
	}

	@Before
	@Transactional()
	public void beforePurgeDatabase() {
		final EntityManager entityManager = this.myEntityManager;
		purgeDatabase(entityManager, myTxManager);
	}

	@Before
	public void beforeResetConfig() {
		myDaoConfig.setHardSearchLimit(1000);
		myDaoConfig.setHardTagListLimit(1000);
		myDaoConfig.setIncludeLimit(2000);
		myFhirCtx.setParserErrorHandler(new StrictErrorHandler());
	}

	@Override
	protected FhirContext getContext() {
		return myFhirCtx;
	}

	protected <T extends IBaseResource> T loadResourceFromClasspath(Class<T> type, String resourceName) throws IOException {
		InputStream stream = FhirResourceDaoDstu2SearchNoFtTest.class.getResourceAsStream(resourceName);
		if (stream == null) {
			fail("Unable to load resource: " + resourceName);
		}
		String string = IOUtils.toString(stream, "UTF-8");
		IParser newJsonParser = MethodUtil.detectEncodingNoDefault(string).newParser(myFhirCtx);
		return newJsonParser.parseResource(type, string);
	}

	public TransactionTemplate newTxTemplate() {
		TransactionTemplate retVal = new TransactionTemplate(myTxManager);
		retVal.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		retVal.afterPropertiesSet();
		return retVal;
	}

	@AfterClass
	public static void afterClassClearContextBaseJpaDstu3Test() throws Exception {
		ourValueSetDao.purgeCaches();
		ourJpaValidationSupportChainDstu3.flush();
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
