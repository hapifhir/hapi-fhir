package ca.uhn.fhir.jpa.dao.dstu3;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.config.TestDstu3Config;
import ca.uhn.fhir.jpa.dao.*;
import ca.uhn.fhir.jpa.dao.data.*;
import ca.uhn.fhir.jpa.dao.dstu2.FhirResourceDaoDstu2SearchNoFtTest;
import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.provider.dstu3.JpaSystemProviderDstu3;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.jpa.search.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.search.IStaleSearchDeletingSvc;
import ca.uhn.fhir.jpa.sp.ISearchParamPresenceSvc;
import ca.uhn.fhir.jpa.term.BaseHapiTerminologySvcImpl;
import ca.uhn.fhir.jpa.term.IHapiTerminologySvc;
import ca.uhn.fhir.jpa.util.ResourceCountCache;
import ca.uhn.fhir.jpa.validation.JpaValidationSupportChainDstu3;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.UrlUtil;
import org.apache.commons.io.IOUtils;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hl7.fhir.convertors.VersionConvertor_30_40;
import org.hl7.fhir.dstu3.hapi.ctx.IValidationSupport;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.exceptions.FHIRException;
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
import org.springframework.test.util.AopTestUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestDstu3Config.class})
public abstract class BaseJpaDstu3Test extends BaseJpaTest {

	private static JpaValidationSupportChainDstu3 ourJpaValidationSupportChainDstu3;
	private static IFhirResourceDaoValueSet<ValueSet, Coding, CodeableConcept> ourValueSetDao;

	@Autowired
	@Qualifier("myResourceCountsCache")
	protected ResourceCountCache myResourceCountsCache;
	@Autowired
	@Qualifier("myCoverageDaoDstu3")
	protected IFhirResourceDao<Coverage> myCoverageDao;
	@Autowired
	protected IResourceIndexedCompositeStringUniqueDao myResourceIndexedCompositeStringUniqueDao;
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
	@Qualifier("myCodeSystemDaoDstu3")
	protected IFhirResourceDaoCodeSystem<CodeSystem, Coding, CodeableConcept> myCodeSystemDao;
	@Autowired
	@Qualifier("myCompartmentDefinitionDaoDstu3")
	protected IFhirResourceDao<CompartmentDefinition> myCompartmentDefinitionDao;
	@Autowired
	@Qualifier("myConceptMapDaoDstu3")
	protected IFhirResourceDaoConceptMap<ConceptMap> myConceptMapDao;
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
	@Qualifier("myBinaryDaoDstu3")
	protected IFhirResourceDao<Binary> myBinaryDao;
	@Autowired
	@Qualifier("myEncounterDaoDstu3")
	protected IFhirResourceDao<Encounter> myEncounterDao;
	// @PersistenceContext()
	@Autowired
	protected EntityManager myEntityManager;
	@Autowired
	protected FhirContext myFhirCtx;
	@Autowired
	@Qualifier("myGroupDaoDstu3")
	protected IFhirResourceDao<Group> myGroupDao;
	@Autowired
	@Qualifier("myImmunizationDaoDstu3")
	protected IFhirResourceDao<Immunization> myImmunizationDao;
	@Autowired
	@Qualifier("myImmunizationRecommendationDaoDstu3")
	protected IFhirResourceDao<ImmunizationRecommendation> myImmunizationRecommendationDao;
	protected IServerInterceptor myInterceptor;
	@Autowired
	@Qualifier("myLocationDaoDstu3")
	protected IFhirResourceDao<Location> myLocationDao;
	@Autowired
	@Qualifier("myMediaDaoDstu3")
	protected IFhirResourceDao<Media> myMediaDao;
	@Autowired
	@Qualifier("myMedicationAdministrationDaoDstu3")
	protected IFhirResourceDao<MedicationAdministration> myMedicationAdministrationDao;
	@Autowired
	@Qualifier("myMedicationDaoDstu3")
	protected IFhirResourceDao<Medication> myMedicationDao;
	@Autowired
	@Qualifier("myMedicationRequestDaoDstu3")
	protected IFhirResourceDao<MedicationRequest> myMedicationRequestDao;
	@Autowired
	@Qualifier("myMedicationStatementDaoDstu3")
	protected IFhirResourceDao<MedicationStatement> myMedicationStatementDao;
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
	@Qualifier("myConsentDaoDstu3")
	protected IFhirResourceDao<Consent> myConsentDao;
	@Autowired
	protected DatabaseBackedPagingProvider myPagingProvider;
	@Autowired
	@Qualifier("myPatientDaoDstu3")
	protected IFhirResourceDaoPatient<Patient> myPatientDao;
	@Autowired
	@Qualifier("myCompositionDaoDstu3")
	protected IFhirResourceDao<Composition> myCompositionDao;
	@Autowired
	@Qualifier("myCommunicationDaoDstu3")
	protected IFhirResourceDao<Communication> myCommunicationDao;
	@Autowired
	@Qualifier("myPractitionerDaoDstu3")
	protected IFhirResourceDao<Practitioner> myPractitionerDao;
	@Autowired
	@Qualifier("myProcedureRequestDaoDstu3")
	protected IFhirResourceDao<ProcedureRequest> myProcedureRequestDao;
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
	protected IResourceIndexedSearchParamStringDao myResourceIndexedSearchParamStringDao;
	@Autowired
	protected IResourceTableDao myResourceTableDao;
	@Autowired
	protected IResourceTagDao myResourceTagDao;
	@Autowired
	protected ISearchCoordinatorSvc mySearchCoordinatorSvc;
	@Autowired(required = false)
	protected IFulltextSearchSvc mySearchDao;
	@Autowired
	protected ISearchDao mySearchEntityDao;
	@Autowired
	@Qualifier("mySearchParameterDaoDstu3")
	protected IFhirResourceDao<SearchParameter> mySearchParameterDao;
	@Autowired
	protected ISearchParamPresenceSvc mySearchParamPresenceSvc;
	@Autowired
	protected ISearchParamRegistry mySearchParamRegsitry;
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
	protected ITagDefinitionDao myTagDefinitionDao;
	@Autowired
	@Qualifier("myTaskDaoDstu3")
	protected IFhirResourceDao<Task> myTaskDao;
	@Autowired
	protected IHapiTerminologySvc myTermSvc;
	@Autowired
	protected PlatformTransactionManager myTransactionMgr;
	@Autowired
	protected PlatformTransactionManager myTxManager;
	@Autowired
	@Qualifier("myJpaValidationSupportChainDstu3")
	protected IValidationSupport myValidationSupport;
	@Autowired
	@Qualifier("myValueSetDaoDstu3")
	protected IFhirResourceDaoValueSet<ValueSet, Coding, CodeableConcept> myValueSetDao;
	@Autowired
	protected ITermConceptMapDao myTermConceptMapDao;
	@Autowired
	protected ITermConceptMapGroupElementTargetDao myTermConceptMapGroupElementTargetDao;
	@Autowired
	private JpaValidationSupportChainDstu3 myJpaValidationSupportChainDstu3;

	@After()
	public void afterCleanupDao() {
		myDaoConfig.setExpireSearchResults(new DaoConfig().isExpireSearchResults());
		myDaoConfig.setExpireSearchResultsAfterMillis(new DaoConfig().getExpireSearchResultsAfterMillis());
		myDaoConfig.setReuseCachedSearchResultsForMillis(new DaoConfig().getReuseCachedSearchResultsForMillis());
		myDaoConfig.setSuppressUpdatesWithNoChange(new DaoConfig().isSuppressUpdatesWithNoChange());
		myDaoConfig.getInterceptors().clear();
	}

	@After
	public void afterClearTerminologyCaches() {
		BaseHapiTerminologySvcImpl baseHapiTerminologySvc = AopTestUtils.getTargetObject(myTermSvc);
		baseHapiTerminologySvc.clearTranslationCache();
		baseHapiTerminologySvc.clearTranslationWithReverseCache();
		baseHapiTerminologySvc.clearDeferred();
		BaseHapiTerminologySvcImpl.clearOurLastResultsFromTranslationCache();
		BaseHapiTerminologySvcImpl.clearOurLastResultsFromTranslationWithReverseCache();
	}

	@After()
	public void afterGrabCaches() {
		ourValueSetDao = myValueSetDao;
		ourJpaValidationSupportChainDstu3 = myJpaValidationSupportChainDstu3;
	}

	@Before
	public void beforeCreateInterceptor() {
		myInterceptor = mock(IServerInterceptor.class);
		myDaoConfig.getInterceptors().add(myInterceptor);
	}

	@Before
	public void beforeFlushFT() {
		runInTransaction(() -> {
			FullTextEntityManager ftem = Search.getFullTextEntityManager(myEntityManager);
			ftem.purgeAll(ResourceTable.class);
			ftem.purgeAll(ResourceIndexedSearchParamString.class);
			ftem.flushToIndexes();
		});

		myDaoConfig.setSchedulingDisabled(true);
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.ENABLED);
	}

	@Before
	@Transactional()
	public void beforePurgeDatabase() {
		purgeDatabase(myDaoConfig, mySystemDao, mySearchParamPresenceSvc, mySearchCoordinatorSvc, mySearchParamRegsitry);
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

	@Override
	protected PlatformTransactionManager getTxManager() {
		return myTxManager;
	}

	protected <T extends IBaseResource> T loadResourceFromClasspath(Class<T> type, String resourceName) throws IOException {
		InputStream stream = FhirResourceDaoDstu2SearchNoFtTest.class.getResourceAsStream(resourceName);
		if (stream == null) {
			fail("Unable to load resource: " + resourceName);
		}
		String string = IOUtils.toString(stream, "UTF-8");
		IParser newJsonParser = EncodingEnum.detectEncodingNoDefault(string).newParser(myFhirCtx);
		return newJsonParser.parseResource(type, string);
	}

	public TransactionTemplate newTxTemplate() {
		TransactionTemplate retVal = new TransactionTemplate(myTxManager);
		retVal.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		retVal.afterPropertiesSet();
		return retVal;
	}

	@AfterClass
	public static void afterClassClearContextBaseJpaDstu3Test() {
		ourValueSetDao.purgeCaches();
		ourJpaValidationSupportChainDstu3.flush();
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	/**
	 * Creates a single {@link org.hl7.fhir.dstu3.model.ConceptMap} entity that includes:
	 * <br>
	 * <ul>
	 * <li>
	 * One group with two elements, each identifying one target apiece.
	 * </li>
	 * <li>
	 * One group with one element, identifying two targets.
	 * </li>
	 * <li>
	 * One group with one element, identifying a target that also appears
	 * in the first element of the first group.
	 * </li>
	 * </ul>
	 * </br>
	 * The first two groups identify the same source code system and different target code systems.
	 * </br>
	 * The first two groups also include an element with the same source code.
	 * </br>
	 *
	 * @return A {@link org.hl7.fhir.dstu3.model.ConceptMap} entity for testing.
	 */
	public static ConceptMap createConceptMap() {
		try {
			return VersionConvertor_30_40.convertConceptMap(BaseJpaR4Test.createConceptMap());
		} catch (FHIRException fe) {
			throw new InternalErrorException(fe);
		}
	}

	public static String toSearchUuidFromLinkNext(Bundle theBundle) {
		String linkNext = theBundle.getLink("next").getUrl();
		linkNext = linkNext.substring(linkNext.indexOf('?'));
		Map<String, String[]> params = UrlUtil.parseQueryString(linkNext);
		String[] uuidParams = params.get(Constants.PARAM_PAGINGACTION);
		String uuid = uuidParams[0];
		return uuid;
	}
}
