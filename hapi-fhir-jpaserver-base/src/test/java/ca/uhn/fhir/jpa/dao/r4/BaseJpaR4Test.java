package ca.uhn.fhir.jpa.dao.r4;

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
import ca.uhn.fhir.jpa.config.TestR4Config;
import ca.uhn.fhir.jpa.dao.BaseJpaTest;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;
import ca.uhn.fhir.jpa.dao.data.*;
import ca.uhn.fhir.jpa.dao.dstu2.FhirResourceDaoDstu2SearchNoFtTest;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.interceptor.PerformanceTracingLoggingInterceptor;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.provider.r4.JpaSystemProviderR4;
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
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.jpa.term.api.ITermReadSvcR4;
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
import org.hl7.fhir.common.hapi.validation.support.CachingValidationSupport;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.ConceptMap.ConceptMapGroupComponent;
import org.hl7.fhir.r4.model.ConceptMap.SourceElementComponent;
import org.hl7.fhir.r4.model.ConceptMap.TargetElementComponent;
import org.hl7.fhir.r4.model.Enumerations.ConceptMapEquivalence;
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

import javax.persistence.EntityManager;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestR4Config.class})
public abstract class BaseJpaR4Test extends BaseJpaTest {
	private static IValidationSupport ourJpaValidationSupportChainR4;
	private static IFhirResourceDaoValueSet<ValueSet, Coding, CodeableConcept> ourValueSetDao;

	@Autowired
	protected ITermReadSvc myHapiTerminologySvc;
	@Autowired
	protected CachingValidationSupport myCachingValidationSupport;
	@Autowired
	protected ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;
	@Autowired
	protected ISearchDao mySearchEntityDao;
	@Autowired
	protected ISearchResultDao mySearchResultDao;
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
	@Qualifier("myAllergyIntoleranceDaoR4")
	protected IFhirResourceDao<AllergyIntolerance> myAllergyIntoleranceDao;
	@Autowired
	protected BinaryAccessProvider myBinaryAccessProvider;
	@Autowired
	protected BinaryStorageInterceptor myBinaryStorageInterceptor;
	@Autowired
	protected ApplicationContext myAppCtx;
	@Autowired
	@Qualifier("myAppointmentDaoR4")
	protected IFhirResourceDao<Appointment> myAppointmentDao;
	@Autowired
	@Qualifier("myAuditEventDaoR4")
	protected IFhirResourceDao<AuditEvent> myAuditEventDao;
	@Autowired
	@Qualifier("myBundleDaoR4")
	protected IFhirResourceDao<Bundle> myBundleDao;
	@Autowired
	@Qualifier("myCommunicationDaoR4")
	protected IFhirResourceDao<Communication> myCommunicationDao;
	@Autowired
	@Qualifier("myCommunicationRequestDaoR4")
	protected IFhirResourceDao<CommunicationRequest> myCommunicationRequestDao;
	@Autowired
	@Qualifier("myCarePlanDaoR4")
	protected IFhirResourceDao<CarePlan> myCarePlanDao;
	@Autowired
	@Qualifier("myCareTeamDaoR4")
	protected IFhirResourceDao<CareTeam> myCareTeamDao;
	@Autowired
	@Qualifier("myCodeSystemDaoR4")
	protected IFhirResourceDaoCodeSystem<CodeSystem, Coding, CodeableConcept> myCodeSystemDao;
	@Autowired
	protected ITermCodeSystemDao myTermCodeSystemDao;
	@Autowired
	protected ITermConceptParentChildLinkDao myTermConceptParentChildLinkDao;
	@Autowired
	protected ITermCodeSystemVersionDao myTermCodeSystemVersionDao;
	@Autowired
	@Qualifier("myCompartmentDefinitionDaoR4")
	protected IFhirResourceDao<CompartmentDefinition> myCompartmentDefinitionDao;
	@Autowired
	@Qualifier("myConceptMapDaoR4")
	protected IFhirResourceDaoConceptMap<ConceptMap> myConceptMapDao;
	@Autowired
	protected ITermConceptDao myTermConceptDao;
	@Autowired
	protected ITermConceptDesignationDao myTermConceptDesignationDao;
	@Autowired
	@Qualifier("myConditionDaoR4")
	protected IFhirResourceDao<Condition> myConditionDao;
	@Autowired
	@Qualifier("myEpisodeOfCareDaoR4")
	protected IFhirResourceDao<EpisodeOfCare> myEpisodeOfCareDao;
	@Autowired
	protected DaoConfig myDaoConfig;
	@Autowired
	protected ModelConfig myModelConfig;
	@Autowired
	@Qualifier("myDeviceDaoR4")
	protected IFhirResourceDao<Device> myDeviceDao;
	@Autowired
	@Qualifier("myProvenanceDaoR4")
	protected IFhirResourceDao<Provenance> myProvenanceDao;
	@Autowired
	@Qualifier("myDiagnosticReportDaoR4")
	protected IFhirResourceDao<DiagnosticReport> myDiagnosticReportDao;
	@Autowired
	@Qualifier("myEncounterDaoR4")
	protected IFhirResourceDao<Encounter> myEncounterDao;
	// @PersistenceContext()
	@Autowired
	protected EntityManager myEntityManager;
	@Autowired
	protected FhirContext myFhirCtx;
	@Autowired
	@Qualifier("myGroupDaoR4")
	protected IFhirResourceDao<Group> myGroupDao;
	@Autowired
	@Qualifier("myMolecularSequenceDaoR4")
	protected IFhirResourceDao<MolecularSequence> myMolecularSequenceDao;
	@Autowired
	@Qualifier("myImmunizationDaoR4")
	protected IFhirResourceDao<Immunization> myImmunizationDao;
	@Autowired
	@Qualifier("myImmunizationRecommendationDaoR4")
	protected IFhirResourceDao<ImmunizationRecommendation> myImmunizationRecommendationDao;
	@Autowired
	@Qualifier("myRiskAssessmentDaoR4")
	protected IFhirResourceDao<RiskAssessment> myRiskAssessmentDao;
	@Autowired
	protected IInterceptorService myInterceptorRegistry;
	@Autowired
	@Qualifier("myLocationDaoR4")
	protected IFhirResourceDao<Location> myLocationDao;
	@Autowired
	@Qualifier("myPractitionerRoleDaoR4")
	protected IFhirResourceDao<PractitionerRole> myPractitionerRoleDao;
	@Autowired
	@Qualifier("myMediaDaoR4")
	protected IFhirResourceDao<Media> myMediaDao;
	@Autowired
	@Qualifier("myMedicationAdministrationDaoR4")
	protected IFhirResourceDao<MedicationAdministration> myMedicationAdministrationDao;
	@Autowired
	@Qualifier("myMedicationDaoR4")
	protected IFhirResourceDao<Medication> myMedicationDao;
	@Autowired
	@Qualifier("myMedicationRequestDaoR4")
	protected IFhirResourceDao<MedicationRequest> myMedicationRequestDao;
	@Autowired
	@Qualifier("myProcedureDaoR4")
	protected IFhirResourceDao<Procedure> myProcedureDao;
	@Autowired
	@Qualifier("myNamingSystemDaoR4")
	protected IFhirResourceDao<NamingSystem> myNamingSystemDao;
	@Autowired
	@Qualifier("myChargeItemDaoR4")
	protected IFhirResourceDao<ChargeItem> myChargeItemDao;
	@Autowired
	@Qualifier("myObservationDaoR4")
	protected IFhirResourceDao<Observation> myObservationDao;
	@Autowired
	@Qualifier("myOperationDefinitionDaoR4")
	protected IFhirResourceDao<OperationDefinition> myOperationDefinitionDao;
	@Autowired
	@Qualifier("myOrganizationDaoR4")
	protected IFhirResourceDao<Organization> myOrganizationDao;
	@Autowired
	protected DatabaseBackedPagingProvider myPagingProvider;
	@Autowired
	@Qualifier("myBinaryDaoR4")
	protected IFhirResourceDao<Binary> myBinaryDao;
	@Autowired
	@Qualifier("myDocumentReferenceDaoR4")
	protected IFhirResourceDao<DocumentReference> myDocumentReferenceDao;
	@Autowired
	@Qualifier("myPatientDaoR4")
	protected IFhirResourceDaoPatient<Patient> myPatientDao;
	@Autowired
	protected IResourceTableDao myResourceTableDao;
	@Autowired
	protected IResourceHistoryTableDao myResourceHistoryTableDao;
	@Autowired
	protected IForcedIdDao myForcedIdDao;
	@Autowired
	@Qualifier("myCoverageDaoR4")
	protected IFhirResourceDao<Coverage> myCoverageDao;
	@Autowired
	@Qualifier("myPractitionerDaoR4")
	protected IFhirResourceDao<Practitioner> myPractitionerDao;
	@Autowired
	@Qualifier("myServiceRequestDaoR4")
	protected IFhirResourceDao<ServiceRequest> myServiceRequestDao;
	@Autowired
	@Qualifier("myQuestionnaireDaoR4")
	protected IFhirResourceDao<Questionnaire> myQuestionnaireDao;
	@Autowired
	@Qualifier("myQuestionnaireResponseDaoR4")
	protected IFhirResourceDao<QuestionnaireResponse> myQuestionnaireResponseDao;
	@Autowired
	@Qualifier("myResourceProvidersR4")
	protected ResourceProviderFactory myResourceProviders;
	@Autowired
	protected IResourceTagDao myResourceTagDao;
	@Autowired
	protected ISearchCoordinatorSvc mySearchCoordinatorSvc;
	@Autowired(required = false)
	protected IFulltextSearchSvc mySearchDao;
	@Autowired
	protected IResourceReindexJobDao myResourceReindexJobDao;
	@Autowired
	@Qualifier("mySearchParameterDaoR4")
	protected IFhirResourceDao<SearchParameter> mySearchParameterDao;
	@Autowired
	protected SearchParamRegistryImpl mySearchParamRegistry;
	@Autowired
	protected IStaleSearchDeletingSvc myStaleSearchDeletingSvc;
	@Autowired
	@Qualifier("myStructureDefinitionDaoR4")
	protected IFhirResourceDaoStructureDefinition<StructureDefinition> myStructureDefinitionDao;
	@Autowired
	@Qualifier("myConsentDaoR4")
	protected IFhirResourceDao<Consent> myConsentDao;
	@Autowired
	@Qualifier("mySubscriptionDaoR4")
	protected IFhirResourceDaoSubscription<Subscription> mySubscriptionDao;
	@Autowired
	@Qualifier("mySubstanceDaoR4")
	protected IFhirResourceDao<Substance> mySubstanceDao;
	@Autowired
	@Qualifier("mySystemDaoR4")
	protected IFhirSystemDao<Bundle, Meta> mySystemDao;
	@Autowired
	protected IResourceReindexingSvc myResourceReindexingSvc;
	@Autowired
	@Qualifier("mySystemProviderR4")
	protected JpaSystemProviderR4 mySystemProvider;
	@Autowired
	protected ITagDefinitionDao myTagDefinitionDao;
	@Autowired
	@Qualifier("myTaskDaoR4")
	protected IFhirResourceDao<Task> myTaskDao;
	@Autowired
	protected ITermReadSvcR4 myTermSvc;
	@Autowired
	protected ITermDeferredStorageSvc myTerminologyDeferredStorageSvc;
	@Autowired
	protected ITermLoaderSvc myTerminologyLoaderSvc;
	@Autowired
	protected PlatformTransactionManager myTransactionMgr;
	@Autowired
	protected PlatformTransactionManager myTxManager;
	@Autowired
	@Qualifier("myJpaValidationSupportChain")
	protected IValidationSupport myValidationSupport;
	@Autowired
	@Qualifier("myValueSetDaoR4")
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
	private IValidationSupport myJpaValidationSupportChainR4;
	private PerformanceTracingLoggingInterceptor myPerformanceTracingLoggingInterceptor;
	private List<Object> mySystemInterceptors;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private IBulkDataExportSvc myBulkDataExportSvc;
	@Autowired
	private IdHelperService myIdHelperService;

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
		TermDeferredStorageSvcImpl termDeferredStorageSvc = AopTestUtils.getTargetObject(myTerminologyDeferredStorageSvc);
		termDeferredStorageSvc.clearDeferred();

		myIdHelperService.clearCache();
	}

	@After()
	public void afterGrabCaches() {
		ourValueSetDao = myValueSetDao;
		ourJpaValidationSupportChainR4 = myJpaValidationSupportChainR4;
	}

	@Before
	public void beforeCreateInterceptor() {
		mySystemInterceptors = myInterceptorRegistry.getAllRegisteredInterceptors();

		myInterceptor = mock(IServerInterceptor.class);

		myPerformanceTracingLoggingInterceptor = new PerformanceTracingLoggingInterceptor();
		myInterceptorRegistry.registerInterceptor(myPerformanceTracingLoggingInterceptor);
	}

	@Before
	public void beforeUnregisterAllSubscriptions() {
		mySubscriptionRegistry.unregisterAllSubscriptions();
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
	public void beforePurgeDatabase() {
		purgeDatabase(myDaoConfig, mySystemDao, myResourceReindexingSvc, mySearchCoordinatorSvc, mySearchParamRegistry, myBulkDataExportSvc);
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


	protected void assertHierarchyContains(String... theStrings) {
		List<String> hierarchy = runInTransaction(() -> {
			List<String> hierarchyHolder = new ArrayList<>();
			TermCodeSystem codeSystem = myTermCodeSystemDao.findAll().iterator().next();
			TermCodeSystemVersion csv = codeSystem.getCurrentVersion();
			List<TermConcept> codes = myTermConceptDao.findByCodeSystemVersion(csv);
			List<TermConcept> rootCodes = codes.stream().filter(t -> t.getParents().isEmpty()).collect(Collectors.toList());
			flattenExpansionHierarchy(hierarchyHolder, rootCodes, "");
			return hierarchyHolder;
		});
		if (theStrings.length == 0) {
			assertThat("\n" + String.join("\n", hierarchy), hierarchy, empty());
		} else {
			assertThat("\n" + String.join("\n", hierarchy), hierarchy, contains(theStrings));
		}
	}

	private static void flattenExpansionHierarchy(List<String> theFlattenedHierarchy, List<TermConcept> theCodes, String thePrefix) {
		theCodes.sort((o1, o2) -> {
			int s1 = o1.getSequence() != null ? o1.getSequence() : o1.getCode().hashCode();
			int s2 = o2.getSequence() != null ? o2.getSequence() : o2.getCode().hashCode();
			return s1 - s2;
		});

		for (TermConcept nextCode : theCodes) {
			String hierarchyEntry = thePrefix + nextCode.getCode() + " seq=" + nextCode.getSequence();
			theFlattenedHierarchy.add(hierarchyEntry);

			List<TermConcept> children = nextCode.getChildCodes();
			flattenExpansionHierarchy(theFlattenedHierarchy, children, thePrefix + " ");
		}
	}

	@AfterClass
	public static void afterClassClearContextBaseJpaR4Test() {
		ourValueSetDao.purgeCaches();
		ourJpaValidationSupportChainR4.invalidateCaches();
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	/**
	 * Creates a single {@link org.hl7.fhir.r4.model.ConceptMap} entity that includes:
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
	 * @return A {@link org.hl7.fhir.r4.model.ConceptMap} entity for testing.
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
		target.setEquivalence(ConceptMapEquivalence.EQUAL);

		element = group.addElement();
		element.setCode("23456");
		element.setDisplay("Source Code 23456");

		target = element.addTarget();
		target.setCode("45678");
		target.setDisplay("Target Code 45678");
		target.setEquivalence(ConceptMapEquivalence.WIDER);

		// Add a duplicate
		target = element.addTarget();
		target.setCode("45678");
		target.setDisplay("Target Code 45678");
		target.setEquivalence(ConceptMapEquivalence.WIDER);

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
		target.setEquivalence(ConceptMapEquivalence.EQUAL);

		target = element.addTarget();
		target.setCode("67890");
		target.setDisplay("Target Code 67890");
		target.setEquivalence(ConceptMapEquivalence.WIDER);

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
		target.setEquivalence(ConceptMapEquivalence.NARROWER);

		return conceptMap;
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
