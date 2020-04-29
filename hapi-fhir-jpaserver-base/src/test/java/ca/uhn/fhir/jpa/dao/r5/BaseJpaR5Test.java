package ca.uhn.fhir.jpa.dao.r5;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoCodeSystem;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoConceptMap;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoPatient;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoStructureDefinition;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoSubscription;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.binstore.BinaryAccessProvider;
import ca.uhn.fhir.jpa.binstore.BinaryStorageInterceptor;
import ca.uhn.fhir.jpa.bulk.IBulkDataExportSvc;
import ca.uhn.fhir.jpa.config.TestR5Config;
import ca.uhn.fhir.jpa.dao.BaseJpaTest;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;
import ca.uhn.fhir.jpa.dao.data.*;
import ca.uhn.fhir.jpa.dao.dstu2.FhirResourceDaoDstu2SearchNoFtTest;
import ca.uhn.fhir.jpa.interceptor.PerformanceTracingLoggingInterceptor;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.provider.r5.JpaSystemProviderR5;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.search.IStaleSearchDeletingSvc;
import ca.uhn.fhir.jpa.search.reindex.IResourceReindexingSvc;
import ca.uhn.fhir.jpa.search.warm.ICacheWarmingSvc;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParamRegistryImpl;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionRegistry;
import ca.uhn.fhir.jpa.term.BaseTermReadSvcImpl;
import ca.uhn.fhir.jpa.term.TermDeferredStorageSvcImpl;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermReadSvcR5;
import ca.uhn.fhir.jpa.util.ResourceCountCache;
import ca.uhn.fhir.jpa.api.rp.ResourceProviderFactory;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.BasePagingProvider;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;
import org.apache.commons.io.IOUtils;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.*;
import org.hl7.fhir.r5.model.ConceptMap.ConceptMapGroupComponent;
import org.hl7.fhir.r5.model.ConceptMap.SourceElementComponent;
import org.hl7.fhir.r5.model.ConceptMap.TargetElementComponent;
import org.hl7.fhir.r5.utils.IResourceValidator;
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
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestR5Config.class})
public abstract class BaseJpaR5Test extends BaseJpaTest {
	private static IValidationSupport ourJpaValidationSupportChainR5;
	private static IFhirResourceDaoValueSet<ValueSet, Coding, CodeableConcept> ourValueSetDao;

	@Autowired
	protected ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;
	@Autowired
	@Qualifier("myResourceCountsCache")
	protected ResourceCountCache myResourceCountsCache;
	@Autowired
	protected IResourceLinkDao myResourceLinkDao;
	@Autowired
	protected ISearchParamPresentDao mySearchParamPresentDao;
	@Autowired
	protected IResourceIndexedSearchParamStringDao myResourceIndexedSearchParamStringDao;
	@Autowired
	protected IResourceIndexedSearchParamTokenDao myResourceIndexedSearchParamTokenDao;
	@Autowired
	protected IResourceIndexedSearchParamQuantityDao myResourceIndexedSearchParamQuantityDao;
	@Autowired
	protected IResourceIndexedSearchParamDateDao myResourceIndexedSearchParamDateDao;
	@Autowired
	protected IResourceIndexedCompositeStringUniqueDao myResourceIndexedCompositeStringUniqueDao;
	@Autowired
	@Qualifier("myAllergyIntoleranceDaoR5")
	protected IFhirResourceDao<AllergyIntolerance> myAllergyIntoleranceDao;
	@Autowired
	protected BinaryAccessProvider myBinaryAccessProvider;
	@Autowired
	protected BinaryStorageInterceptor myBinaryStorageInterceptor;
	@Autowired
	protected ApplicationContext myAppCtx;
	@Autowired
	@Qualifier("myAppointmentDaoR5")
	protected IFhirResourceDao<Appointment> myAppointmentDao;
	@Autowired
	@Qualifier("myAuditEventDaoR5")
	protected IFhirResourceDao<AuditEvent> myAuditEventDao;
	@Autowired
	@Qualifier("myBundleDaoR5")
	protected IFhirResourceDao<Bundle> myBundleDao;
	@Autowired
	@Qualifier("myCommunicationDaoR5")
	protected IFhirResourceDao<Communication> myCommunicationDao;
	@Autowired
	@Qualifier("myCommunicationRequestDaoR5")
	protected IFhirResourceDao<CommunicationRequest> myCommunicationRequestDao;
	@Autowired
	@Qualifier("myCarePlanDaoR5")
	protected IFhirResourceDao<CarePlan> myCarePlanDao;
	@Autowired
	@Qualifier("myCodeSystemDaoR5")
	protected IFhirResourceDaoCodeSystem<CodeSystem, Coding, CodeableConcept> myCodeSystemDao;
	@Autowired
	protected ITermCodeSystemDao myTermCodeSystemDao;
	@Autowired
	protected ITermConceptParentChildLinkDao myTermConceptParentChildLinkDao;
	@Autowired
	protected ITermCodeSystemVersionDao myTermCodeSystemVersionDao;
	@Autowired
	@Qualifier("myCompartmentDefinitionDaoR5")
	protected IFhirResourceDao<CompartmentDefinition> myCompartmentDefinitionDao;
	@Autowired
	@Qualifier("myConceptMapDaoR5")
	protected IFhirResourceDaoConceptMap<ConceptMap> myConceptMapDao;
	@Autowired
	protected ITermConceptDao myTermConceptDao;
	@Autowired
	protected ITermConceptDesignationDao myTermConceptDesignationDao;
	@Autowired
	@Qualifier("myConditionDaoR5")
	protected IFhirResourceDao<Condition> myConditionDao;
	@Autowired
	protected DaoConfig myDaoConfig;
	@Autowired
	protected ModelConfig myModelConfig;
	@Autowired
	@Qualifier("myDeviceDaoR5")
	protected IFhirResourceDao<Device> myDeviceDao;
	@Autowired
	@Qualifier("myDiagnosticReportDaoR5")
	protected IFhirResourceDao<DiagnosticReport> myDiagnosticReportDao;
	@Autowired
	@Qualifier("myEncounterDaoR5")
	protected IFhirResourceDao<Encounter> myEncounterDao;
	// @PersistenceContext()
	@Autowired
	protected EntityManager myEntityManager;
	@Autowired
	protected FhirContext myFhirCtx;
	@Autowired
	@Qualifier("myGroupDaoR5")
	protected IFhirResourceDao<Group> myGroupDao;
	@Autowired
	@Qualifier("myMolecularSequenceDaoR5")
	protected IFhirResourceDao<MolecularSequence> myMolecularSequenceDao;
	@Autowired
	@Qualifier("myImmunizationDaoR5")
	protected IFhirResourceDao<Immunization> myImmunizationDao;
	@Autowired
	@Qualifier("myImmunizationRecommendationDaoR5")
	protected IFhirResourceDao<ImmunizationRecommendation> myImmunizationRecommendationDao;
	@Autowired
	@Qualifier("myRiskAssessmentDaoR5")
	protected IFhirResourceDao<RiskAssessment> myRiskAssessmentDao;
	@Autowired
	protected IInterceptorService myInterceptorRegistry;
	@Autowired
	@Qualifier("myLocationDaoR5")
	protected IFhirResourceDao<Location> myLocationDao;
	@Autowired
	@Qualifier("myMedicationAdministrationDaoR5")
	protected IFhirResourceDao<MedicationAdministration> myMedicationAdministrationDao;
	@Autowired
	@Qualifier("myMedicationDaoR5")
	protected IFhirResourceDao<Medication> myMedicationDao;
	@Autowired
	@Qualifier("myMedicationRequestDaoR5")
	protected IFhirResourceDao<MedicationRequest> myMedicationRequestDao;
	@Autowired
	@Qualifier("myProcedureDaoR5")
	protected IFhirResourceDao<Procedure> myProcedureDao;
	@Autowired
	@Qualifier("myNamingSystemDaoR5")
	protected IFhirResourceDao<NamingSystem> myNamingSystemDao;
	@Autowired
	@Qualifier("myChargeItemDaoR5")
	protected IFhirResourceDao<ChargeItem> myChargeItemDao;
	@Autowired
	@Qualifier("myObservationDaoR5")
	protected IFhirResourceDao<Observation> myObservationDao;
	@Autowired
	@Qualifier("myOperationDefinitionDaoR5")
	protected IFhirResourceDao<OperationDefinition> myOperationDefinitionDao;
	@Autowired
	@Qualifier("myOrganizationDaoR5")
	protected IFhirResourceDao<Organization> myOrganizationDao;
	@Autowired
	protected DatabaseBackedPagingProvider myPagingProvider;
	@Autowired
	@Qualifier("myBinaryDaoR5")
	protected IFhirResourceDao<Binary> myBinaryDao;
	@Autowired
	@Qualifier("myPatientDaoR5")
	protected IFhirResourceDaoPatient<Patient> myPatientDao;
	@Autowired
	protected IResourceTableDao myResourceTableDao;
	@Autowired
	protected IResourceHistoryTableDao myResourceHistoryTableDao;
	@Autowired
	protected IForcedIdDao myForcedIdDao;
	@Autowired
	@Qualifier("myCoverageDaoR5")
	protected IFhirResourceDao<Coverage> myCoverageDao;
	@Autowired
	@Qualifier("myPractitionerDaoR5")
	protected IFhirResourceDao<Practitioner> myPractitionerDao;
	@Autowired
	@Qualifier("myPractitionerRoleDaoR5")
	protected IFhirResourceDao<PractitionerRole> myPractitionerRoleDao;
	@Autowired
	@Qualifier("myServiceRequestDaoR5")
	protected IFhirResourceDao<ServiceRequest> myServiceRequestDao;
	@Autowired
	@Qualifier("myQuestionnaireDaoR5")
	protected IFhirResourceDao<Questionnaire> myQuestionnaireDao;
	@Autowired
	@Qualifier("myQuestionnaireResponseDaoR5")
	protected IFhirResourceDao<QuestionnaireResponse> myQuestionnaireResponseDao;
	@Autowired
	@Qualifier("myResourceProvidersR5")
	protected ResourceProviderFactory myResourceProviders;
	@Autowired
	protected IResourceTagDao myResourceTagDao;
	@Autowired
	protected ISearchCoordinatorSvc mySearchCoordinatorSvc;
	@Autowired(required = false)
	protected IFulltextSearchSvc mySearchDao;
	@Autowired(required = false)
	protected ISearchDao mySearchEntityDao;
	@Autowired
	protected IResourceReindexJobDao myResourceReindexJobDao;
	@Autowired
	@Qualifier("mySearchParameterDaoR5")
	protected IFhirResourceDao<SearchParameter> mySearchParameterDao;
	@Autowired
	protected SearchParamRegistryImpl mySearchParamRegistry;
	@Autowired
	protected IStaleSearchDeletingSvc myStaleSearchDeletingSvc;
	@Autowired
	@Qualifier("myStructureDefinitionDaoR5")
	protected IFhirResourceDaoStructureDefinition<StructureDefinition> myStructureDefinitionDao;
	@Autowired
	@Qualifier("myConsentDaoR5")
	protected IFhirResourceDao<Consent> myConsentDao;
	@Autowired
	@Qualifier("mySubscriptionDaoR5")
	protected IFhirResourceDaoSubscription<Subscription> mySubscriptionDao;
	@Autowired
	@Qualifier("mySubstanceDaoR5")
	protected IFhirResourceDao<Substance> mySubstanceDao;
	@Autowired
	@Qualifier("mySystemDaoR5")
	protected IFhirSystemDao<Bundle, Meta> mySystemDao;
	@Autowired
	protected IResourceReindexingSvc myResourceReindexingSvc;
	@Autowired
	@Qualifier("mySystemProviderR5")
	protected JpaSystemProviderR5 mySystemProvider;
	@Autowired
	protected ITagDefinitionDao myTagDefinitionDao;
	@Autowired
	@Qualifier("myTaskDaoR5")
	protected IFhirResourceDao<Task> myTaskDao;
	@Autowired
	protected ITermReadSvcR5 myTermSvc;
	@Autowired
	protected PlatformTransactionManager myTransactionMgr;
	@Autowired
	protected PlatformTransactionManager myTxManager;
	@Autowired
	@Qualifier("myJpaValidationSupportChain")
	protected IValidationSupport myValidationSupport;
	@Autowired
	@Qualifier("myValueSetDaoR5")
	protected IFhirResourceDaoValueSet<ValueSet, Coding, CodeableConcept> myValueSetDao;
	@Autowired
	protected ITermValueSetDao myTermValueSetDao;
	@Autowired
	protected ITermValueSetConceptDao myTermValueSetConceptDao;
	@Autowired
	protected ITermValueSetConceptDesignationDao myTermValueSetConceptDesignationDao;
	@Autowired
	protected ITermConceptMapDao myTermConceptMapDao;
	@Autowired
	protected ITermConceptMapGroupElementTargetDao myTermConceptMapGroupElementTargetDao;
	@Autowired
	protected ICacheWarmingSvc myCacheWarmingSvc;
	@Autowired
	protected SubscriptionRegistry mySubscriptionRegistry;
	protected IServerInterceptor myInterceptor;
	@Autowired
	private IValidationSupport myJpaValidationSupportChain;
	private PerformanceTracingLoggingInterceptor myPerformanceTracingLoggingInterceptor;
	private List<Object> mySystemInterceptors;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private IBulkDataExportSvc myBulkDataExportSvc;
	@Autowired
	protected ITermDeferredStorageSvc myTermDeferredStorageSvc;

	@After()
	public void afterCleanupDao() {
		myDaoConfig.setExpireSearchResults(new DaoConfig().isExpireSearchResults());
		myDaoConfig.setEnforceReferentialIntegrityOnDelete(new DaoConfig().isEnforceReferentialIntegrityOnDelete());
		myDaoConfig.setExpireSearchResultsAfterMillis(new DaoConfig().getExpireSearchResultsAfterMillis());
		myDaoConfig.setReuseCachedSearchResultsForMillis(new DaoConfig().getReuseCachedSearchResultsForMillis());
		myDaoConfig.setSuppressUpdatesWithNoChange(new DaoConfig().isSuppressUpdatesWithNoChange());
		myDaoConfig.setAllowContainsSearches(new DaoConfig().isAllowContainsSearches());

		myPagingProvider.setDefaultPageSize(BasePagingProvider.DEFAULT_DEFAULT_PAGE_SIZE);
		myPagingProvider.setMaximumPageSize(BasePagingProvider.DEFAULT_MAX_PAGE_SIZE);
	}

	@After
	public void afterResetInterceptors() {
		myInterceptorRegistry.unregisterAllInterceptors();
	}

	@After
	public void afterClearTerminologyCaches() {
		BaseTermReadSvcImpl baseHapiTerminologySvc = AopTestUtils.getTargetObject(myTermSvc);
		baseHapiTerminologySvc.clearTranslationCache();
		baseHapiTerminologySvc.clearTranslationWithReverseCache();
		BaseTermReadSvcImpl.clearOurLastResultsFromTranslationCache();
		BaseTermReadSvcImpl.clearOurLastResultsFromTranslationWithReverseCache();
		TermDeferredStorageSvcImpl deferredStorageSvc = AopTestUtils.getTargetObject(myTermDeferredStorageSvc);
		deferredStorageSvc.clearDeferred();
	}

	@After()
	public void afterGrabCaches() {
		ourValueSetDao = myValueSetDao;
		ourJpaValidationSupportChainR5 = myJpaValidationSupportChain;
	}

	@Before
	public void beforeCreateInterceptor() {
		mySystemInterceptors = myInterceptorRegistry.getAllRegisteredInterceptors();

		myInterceptor = mock(IServerInterceptor.class);

		myPerformanceTracingLoggingInterceptor = new PerformanceTracingLoggingInterceptor();
		myInterceptorRegistry.registerInterceptor(myPerformanceTracingLoggingInterceptor);
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
		purgeDatabase(myDaoConfig, mySystemDao, myResourceReindexingSvc, mySearchCoordinatorSvc, mySearchParamRegistry, myBulkDataExportSvc);
	}

	@Before
	public void beforeResetConfig() {
		myDaoConfig.setHardTagListLimit(1000);
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
		String string = IOUtils.toString(stream, StandardCharsets.UTF_8);
		IParser newJsonParser = EncodingEnum.detectEncodingNoDefault(string).newParser(myFhirCtx);
		return newJsonParser.parseResource(type, string);
	}

	protected void validate(IBaseResource theResource) {
		FhirValidator validatorModule = myFhirCtx.newValidator();
		FhirInstanceValidator instanceValidator = new FhirInstanceValidator(myValidationSupport);
		instanceValidator.setBestPracticeWarningLevel(IResourceValidator.BestPracticeWarningLevel.Ignore);
		validatorModule.registerValidatorModule(instanceValidator);
		ValidationResult result = validatorModule.validateWithResult(theResource);
		if (!result.isSuccessful()) {
			fail(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(result.toOperationOutcome()));
		}
	}

	@SuppressWarnings("unchecked")
	protected void upload(String theClasspath) throws IOException {
		String resource = loadResource(theClasspath);
		IParser parser = EncodingEnum.detectEncoding(resource).newParser(myFhirCtx);
		IBaseResource resourceParsed = parser.parseResource(resource);
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(resourceParsed.getIdElement().getResourceType());
		dao.update(resourceParsed);
	}

	@AfterClass
	public static void afterClassClearContextBaseJpaR5Test() {
		ourValueSetDao.purgeCaches();
		ourJpaValidationSupportChainR5.invalidateCaches();
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	/**
	 * Creates a single {@link ConceptMap} entity that includes:
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
	 *
	 * @return A {@link ConceptMap} entity for testing.
	 */
	public static ConceptMap createConceptMap() {
		ConceptMap conceptMap = new ConceptMap();
		conceptMap.setUrl(CM_URL);

		conceptMap.setSource(new UriType(VS_URL));
		conceptMap.setTarget(new UriType(VS_URL_2));

		ConceptMapGroupComponent group = conceptMap.addGroup();
		group.setSource(CS_URL);
		group.setSourceVersion("Version 1");
		group.setTarget(CS_URL_2);
		group.setTargetVersion("Version 2");

		SourceElementComponent element = group.addElement();
		element.setCode("12345");
		element.setDisplay("Source Code 12345");

		TargetElementComponent target = element.addTarget();
		target.setCode("34567");
		target.setDisplay("Target Code 34567");
		target.setRelationship(Enumerations.ConceptMapRelationship.EQUIVALENT);

		element = group.addElement();
		element.setCode("23456");
		element.setDisplay("Source Code 23456");

		target = element.addTarget();
		target.setCode("45678");
		target.setDisplay("Target Code 45678");
		target.setRelationship(Enumerations.ConceptMapRelationship.BROADER);

		// Add a duplicate
		target = element.addTarget();
		target.setCode("45678");
		target.setDisplay("Target Code 45678");
		target.setRelationship(Enumerations.ConceptMapRelationship.BROADER);

		group = conceptMap.addGroup();
		group.setSource(CS_URL);
		group.setSourceVersion("Version 3");
		group.setTarget(CS_URL_3);
		group.setTargetVersion("Version 4");

		element = group.addElement();
		element.setCode("12345");
		element.setDisplay("Source Code 12345");

		target = element.addTarget();
		target.setCode("56789");
		target.setDisplay("Target Code 56789");
		target.setRelationship(Enumerations.ConceptMapRelationship.EQUIVALENT);

		target = element.addTarget();
		target.setCode("67890");
		target.setDisplay("Target Code 67890");
		target.setRelationship(Enumerations.ConceptMapRelationship.BROADER);

		group = conceptMap.addGroup();
		group.setSource(CS_URL_4);
		group.setSourceVersion("Version 5");
		group.setTarget(CS_URL_2);
		group.setTargetVersion("Version 2");

		element = group.addElement();
		element.setCode("78901");
		element.setDisplay("Source Code 78901");

		target = element.addTarget();
		target.setCode("34567");
		target.setDisplay("Target Code 34567");
		target.setRelationship(Enumerations.ConceptMapRelationship.NARROWER);

		return conceptMap;
	}

	public static String toSearchUuidFromLinkNext(Bundle theBundle) {
		String linkNext = theBundle.getLink("next").getUrl();
		linkNext = linkNext.substring(linkNext.indexOf('?'));
		Map<String, String[]> params = UrlUtil.parseQueryString(linkNext);
		String[] uuidParams = params.get(Constants.PARAM_PAGINGACTION);
		return uuidParams[0];
	}

}
