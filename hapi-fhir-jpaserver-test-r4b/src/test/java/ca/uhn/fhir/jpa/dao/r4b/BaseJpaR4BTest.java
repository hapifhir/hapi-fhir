package ca.uhn.fhir.jpa.dao.r4b;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoCodeSystem;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoConceptMap;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoPatient;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoStructureDefinition;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoSubscription;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.binary.interceptor.BinaryStorageInterceptor;
import ca.uhn.fhir.jpa.binary.provider.BinaryAccessProvider;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkDataExportJobSchedulingHelper;
import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;
import ca.uhn.fhir.jpa.dao.data.IResourceHistoryTableDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedComboStringUniqueDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamDateDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamQuantityDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamStringDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamTokenDao;
import ca.uhn.fhir.jpa.dao.data.IResourceLinkDao;
import ca.uhn.fhir.jpa.dao.data.IResourceReindexJobDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTagDao;
import ca.uhn.fhir.jpa.dao.data.ISearchDao;
import ca.uhn.fhir.jpa.dao.data.ISearchParamPresentDao;
import ca.uhn.fhir.jpa.dao.data.ITagDefinitionDao;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemDao;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemVersionDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDesignationDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptMapDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptMapGroupElementTargetDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptParentChildLinkDao;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetConceptDao;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetConceptDesignationDao;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetDao;
import ca.uhn.fhir.jpa.interceptor.PerformanceTracingLoggingInterceptor;
import ca.uhn.fhir.jpa.provider.JpaSystemProvider;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.jpa.search.IStaleSearchDeletingSvc;
import ca.uhn.fhir.jpa.search.reindex.IResourceReindexingSvc;
import ca.uhn.fhir.jpa.search.warm.ICacheWarmingSvc;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParamRegistryImpl;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionRegistry;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.test.BaseJpaTest;
import ca.uhn.fhir.jpa.test.config.TestR4BConfig;
import ca.uhn.fhir.jpa.util.ResourceCountCache;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.BasePagingProvider;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.provider.ResourceProviderFactory;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;
import jakarta.persistence.EntityManager;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4b.model.AllergyIntolerance;
import org.hl7.fhir.r4b.model.Appointment;
import org.hl7.fhir.r4b.model.AuditEvent;
import org.hl7.fhir.r4b.model.Binary;
import org.hl7.fhir.r4b.model.Bundle;
import org.hl7.fhir.r4b.model.CarePlan;
import org.hl7.fhir.r4b.model.ChargeItem;
import org.hl7.fhir.r4b.model.CodeSystem;
import org.hl7.fhir.r4b.model.Communication;
import org.hl7.fhir.r4b.model.CommunicationRequest;
import org.hl7.fhir.r4b.model.CompartmentDefinition;
import org.hl7.fhir.r4b.model.ConceptMap;
import org.hl7.fhir.r4b.model.Condition;
import org.hl7.fhir.r4b.model.Consent;
import org.hl7.fhir.r4b.model.Coverage;
import org.hl7.fhir.r4b.model.Device;
import org.hl7.fhir.r4b.model.DiagnosticReport;
import org.hl7.fhir.r4b.model.Encounter;
import org.hl7.fhir.r4b.model.Group;
import org.hl7.fhir.r4b.model.Immunization;
import org.hl7.fhir.r4b.model.ImmunizationRecommendation;
import org.hl7.fhir.r4b.model.Location;
import org.hl7.fhir.r4b.model.Medication;
import org.hl7.fhir.r4b.model.MedicationAdministration;
import org.hl7.fhir.r4b.model.MedicationRequest;
import org.hl7.fhir.r4b.model.Meta;
import org.hl7.fhir.r4b.model.MolecularSequence;
import org.hl7.fhir.r4b.model.NamingSystem;
import org.hl7.fhir.r4b.model.Observation;
import org.hl7.fhir.r4b.model.OperationDefinition;
import org.hl7.fhir.r4b.model.Organization;
import org.hl7.fhir.r4b.model.Patient;
import org.hl7.fhir.r4b.model.Practitioner;
import org.hl7.fhir.r4b.model.PractitionerRole;
import org.hl7.fhir.r4b.model.Procedure;
import org.hl7.fhir.r4b.model.Questionnaire;
import org.hl7.fhir.r4b.model.QuestionnaireResponse;
import org.hl7.fhir.r4b.model.RiskAssessment;
import org.hl7.fhir.r4b.model.SearchParameter;
import org.hl7.fhir.r4b.model.ServiceRequest;
import org.hl7.fhir.r4b.model.StructureDefinition;
import org.hl7.fhir.r4b.model.Subscription;
import org.hl7.fhir.r4b.model.Substance;
import org.hl7.fhir.r4b.model.Task;
import org.hl7.fhir.r4b.model.ValueSet;
import org.hl7.fhir.r5.utils.validation.constants.BestPracticeWarningLevel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestR4BConfig.class})
public abstract class BaseJpaR4BTest extends BaseJpaTest implements ITestDataBuilder {
	private static IValidationSupport ourJpaValidationSupportChainR4B;
	private static IFhirResourceDaoValueSet<ValueSet> ourValueSetDao;
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
	protected IResourceIndexedComboStringUniqueDao myResourceIndexedCompositeStringUniqueDao;
	@Autowired
	@Qualifier("myAllergyIntoleranceDaoR4B")
	protected IFhirResourceDao<AllergyIntolerance> myAllergyIntoleranceDao;
	@Autowired
	protected BinaryAccessProvider myBinaryAccessProvider;
	@Autowired
	protected BinaryStorageInterceptor myBinaryStorageInterceptor;
	@Autowired
	protected ApplicationContext myAppCtx;
	@Autowired
	@Qualifier("myAppointmentDaoR4B")
	protected IFhirResourceDao<Appointment> myAppointmentDao;
	@Autowired
	@Qualifier("myAuditEventDaoR4B")
	protected IFhirResourceDao<AuditEvent> myAuditEventDao;
	@Autowired
	@Qualifier("myBundleDaoR4B")
	protected IFhirResourceDao<Bundle> myBundleDao;
	@Autowired
	@Qualifier("myCommunicationDaoR4B")
	protected IFhirResourceDao<Communication> myCommunicationDao;
	@Autowired
	@Qualifier("myCommunicationRequestDaoR4B")
	protected IFhirResourceDao<CommunicationRequest> myCommunicationRequestDao;
	@Autowired
	@Qualifier("myCarePlanDaoR4B")
	protected IFhirResourceDao<CarePlan> myCarePlanDao;
	@Autowired
	@Qualifier("myCodeSystemDaoR4B")
	protected IFhirResourceDaoCodeSystem<CodeSystem> myCodeSystemDao;
	@Autowired
	protected ITermCodeSystemDao myTermCodeSystemDao;
	@Autowired
	protected ITermConceptParentChildLinkDao myTermConceptParentChildLinkDao;
	@Autowired
	protected ITermCodeSystemVersionDao myTermCodeSystemVersionDao;
	@Autowired
	@Qualifier("myCompartmentDefinitionDaoR4B")
	protected IFhirResourceDao<CompartmentDefinition> myCompartmentDefinitionDao;
	@Autowired
	@Qualifier("myConceptMapDaoR4B")
	protected IFhirResourceDaoConceptMap<ConceptMap> myConceptMapDao;
	@Autowired
	protected ITermConceptDao myTermConceptDao;
	@Autowired
	protected ITermConceptDesignationDao myTermConceptDesignationDao;
	@Autowired
	@Qualifier("myConditionDaoR4B")
	protected IFhirResourceDao<Condition> myConditionDao;
	@Autowired
	@Qualifier("myDeviceDaoR4B")
	protected IFhirResourceDao<Device> myDeviceDao;
	@Autowired
	@Qualifier("myDiagnosticReportDaoR4B")
	protected IFhirResourceDao<DiagnosticReport> myDiagnosticReportDao;
	@Autowired
	@Qualifier("myEncounterDaoR4B")
	protected IFhirResourceDao<Encounter> myEncounterDao;
	// @PersistenceContext()
	@Autowired
	protected EntityManager myEntityManager;
	@Autowired
	protected FhirContext myFhirCtx;
	@Autowired
	@Qualifier("myGroupDaoR4B")
	protected IFhirResourceDao<Group> myGroupDao;
	@Autowired
	@Qualifier("myMolecularSequenceDaoR4B")
	protected IFhirResourceDao<MolecularSequence> myMolecularSequenceDao;
	@Autowired
	@Qualifier("myImmunizationDaoR4B")
	protected IFhirResourceDao<Immunization> myImmunizationDao;
	@Autowired
	@Qualifier("myImmunizationRecommendationDaoR4B")
	protected IFhirResourceDao<ImmunizationRecommendation> myImmunizationRecommendationDao;
	@Autowired
	@Qualifier("myRiskAssessmentDaoR4B")
	protected IFhirResourceDao<RiskAssessment> myRiskAssessmentDao;
	@Autowired
	protected IInterceptorService myInterceptorRegistry;
	@Autowired
	@Qualifier("myLocationDaoR4B")
	protected IFhirResourceDao<Location> myLocationDao;
	@Autowired
	@Qualifier("myMedicationAdministrationDaoR4B")
	protected IFhirResourceDao<MedicationAdministration> myMedicationAdministrationDao;
	@Autowired
	@Qualifier("myMedicationDaoR4B")
	protected IFhirResourceDao<Medication> myMedicationDao;
	@Autowired
	@Qualifier("myMedicationRequestDaoR4B")
	protected IFhirResourceDao<MedicationRequest> myMedicationRequestDao;
	@Autowired
	@Qualifier("myProcedureDaoR4B")
	protected IFhirResourceDao<Procedure> myProcedureDao;
	@Autowired
	@Qualifier("myNamingSystemDaoR4B")
	protected IFhirResourceDao<NamingSystem> myNamingSystemDao;
	@Autowired
	@Qualifier("myChargeItemDaoR4B")
	protected IFhirResourceDao<ChargeItem> myChargeItemDao;
	@Autowired
	@Qualifier("myObservationDaoR4B")
	protected IFhirResourceDao<Observation> myObservationDao;
	@Autowired
	@Qualifier("myOperationDefinitionDaoR4B")
	protected IFhirResourceDao<OperationDefinition> myOperationDefinitionDao;
	@Autowired
	@Qualifier("myOrganizationDaoR4B")
	protected IFhirResourceDao<Organization> myOrganizationDao;
	@Autowired
	protected DatabaseBackedPagingProvider myPagingProvider;
	@Autowired
	@Qualifier("myBinaryDaoR4B")
	protected IFhirResourceDao<Binary> myBinaryDao;
	@Autowired
	@Qualifier("myPatientDaoR4B")
	protected IFhirResourceDaoPatient<Patient> myPatientDao;
	@Autowired
	protected IResourceTableDao myResourceTableDao;
	@Autowired
	protected IResourceHistoryTableDao myResourceHistoryTableDao;
	@Autowired
	@Qualifier("myCoverageDaoR4B")
	protected IFhirResourceDao<Coverage> myCoverageDao;
	@Autowired
	@Qualifier("myPractitionerDaoR4B")
	protected IFhirResourceDao<Practitioner> myPractitionerDao;
	@Autowired
	@Qualifier("myPractitionerRoleDaoR4B")
	protected IFhirResourceDao<PractitionerRole> myPractitionerRoleDao;
	@Autowired
	@Qualifier("myServiceRequestDaoR4B")
	protected IFhirResourceDao<ServiceRequest> myServiceRequestDao;
	@Autowired
	@Qualifier("myQuestionnaireDaoR4B")
	protected IFhirResourceDao<Questionnaire> myQuestionnaireDao;
	@Autowired
	@Qualifier("myQuestionnaireResponseDaoR4B")
	protected IFhirResourceDao<QuestionnaireResponse> myQuestionnaireResponseDao;
	@Autowired
	@Qualifier("myResourceProvidersR4B")
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
	@Qualifier("mySearchParameterDaoR4B")
	protected IFhirResourceDao<SearchParameter> mySearchParameterDao;
	@Autowired
	protected SearchParamRegistryImpl mySearchParamRegistry;
	@Autowired
	protected IStaleSearchDeletingSvc myStaleSearchDeletingSvc;
	@Autowired
	@Qualifier("myStructureDefinitionDaoR4B")
	protected IFhirResourceDaoStructureDefinition<StructureDefinition> myStructureDefinitionDao;
	@Autowired
	@Qualifier("myConsentDaoR4B")
	protected IFhirResourceDao<Consent> myConsentDao;
	@Autowired
	@Qualifier("mySubscriptionDaoR4B")
	protected IFhirResourceDaoSubscription<Subscription> mySubscriptionDao;
	@Autowired
	@Qualifier("mySubstanceDaoR4B")
	protected IFhirResourceDao<Substance> mySubstanceDao;
	@Autowired
	@Qualifier("mySystemDaoR4B")
	protected IFhirSystemDao<Bundle, Meta> mySystemDao;
	@Autowired
	protected IResourceReindexingSvc myResourceReindexingSvc;
	@Autowired
	protected JpaSystemProvider<Bundle, Meta> mySystemProvider;
	@Autowired
	protected ITagDefinitionDao myTagDefinitionDao;
	@Autowired
	@Qualifier("myTaskDaoR4B")
	protected IFhirResourceDao<Task> myTaskDao;
	@Autowired
	protected PlatformTransactionManager myTransactionMgr;
	@Autowired
	protected PlatformTransactionManager myTxManager;
	@Autowired
	@Qualifier("myJpaValidationSupportChain")
	protected IValidationSupport myValidationSupport;
	@Autowired
	@Qualifier("myValueSetDaoR4B")
	protected IFhirResourceDaoValueSet<ValueSet> myValueSetDao;
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
	protected ITermDeferredStorageSvc myTermDeferredStorageSvc;
	@Autowired
	private IValidationSupport myJpaValidationSupportChain;
	protected PerformanceTracingLoggingInterceptor myPerformanceTracingLoggingInterceptor;
	private List<Object> mySystemInterceptors;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private IBulkDataExportJobSchedulingHelper myBulkDataSchedulerHelper;

	@Override
	public IIdType doCreateResource(IBaseResource theResource) {
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(theResource.getClass());
		return dao.create(theResource, mySrd).getId().toUnqualifiedVersionless();
	}

	@Override
	public IIdType doUpdateResource(IBaseResource theResource) {
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(theResource.getClass());
		return dao.update(theResource, mySrd).getId().toUnqualifiedVersionless();
	}

	@Override
	public FhirContext getFhirContext() {
		return myFhirCtx;
	}

	@AfterEach()
	public void afterCleanupDao() {
		this.myStorageSettings.setExpireSearchResults(new JpaStorageSettings().isExpireSearchResults());
		this.myStorageSettings.setEnforceReferentialIntegrityOnDelete(new JpaStorageSettings().isEnforceReferentialIntegrityOnDelete());
		this.myStorageSettings.setExpireSearchResultsAfterMillis(new JpaStorageSettings().getExpireSearchResultsAfterMillis());
		this.myStorageSettings.setReuseCachedSearchResultsForMillis(new JpaStorageSettings().getReuseCachedSearchResultsForMillis());
		this.myStorageSettings.setSuppressUpdatesWithNoChange(new JpaStorageSettings().isSuppressUpdatesWithNoChange());
		this.myStorageSettings.setAllowContainsSearches(new JpaStorageSettings().isAllowContainsSearches());

		myPagingProvider.setDefaultPageSize(BasePagingProvider.DEFAULT_DEFAULT_PAGE_SIZE);
		myPagingProvider.setMaximumPageSize(BasePagingProvider.DEFAULT_MAX_PAGE_SIZE);
	}

	@Override
	@AfterEach
	public void afterResetInterceptors() {
		super.afterResetInterceptors();
		myInterceptorRegistry.unregisterInterceptor(myPerformanceTracingLoggingInterceptor);
	}

	@AfterEach()
	public void afterGrabCaches() {
		ourValueSetDao = myValueSetDao;
		ourJpaValidationSupportChainR4B = myJpaValidationSupportChain;
	}

	@BeforeEach
	public void beforeCreateInterceptor() {
		mySystemInterceptors = myInterceptorRegistry.getAllRegisteredInterceptors();

		myInterceptor = mock(IServerInterceptor.class);

		myPerformanceTracingLoggingInterceptor = new PerformanceTracingLoggingInterceptor();
		myInterceptorRegistry.registerInterceptor(myPerformanceTracingLoggingInterceptor);
	}

	@BeforeEach
	public void beforeFlushFT() {
		purgeHibernateSearch(myEntityManager);

		this.myStorageSettings.setSchedulingDisabled(true);
		this.myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.ENABLED);
	}

	@BeforeEach
	@Transactional()
	public void beforePurgeDatabase() {
		purgeDatabase(this.myStorageSettings, mySystemDao, myResourceReindexingSvc, mySearchCoordinatorSvc, mySearchParamRegistry, myBulkDataSchedulerHelper);
	}

	@BeforeEach
	public void beforeResetConfig() {
		myFhirCtx.setParserErrorHandler(new StrictErrorHandler());
	}

	@Override
	protected PlatformTransactionManager getTxManager() {
		return myTxManager;
	}

	protected void validate(IBaseResource theResource) {
		FhirValidator validatorModule = myFhirCtx.newValidator();
		FhirInstanceValidator instanceValidator = new FhirInstanceValidator(myValidationSupport);
		instanceValidator.setBestPracticeWarningLevel(BestPracticeWarningLevel.Ignore);
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

	protected ValueSet.ValueSetExpansionContainsComponent assertExpandedValueSetContainsConcept(ValueSet theValueSet, String theSystem, String theCode, String theDisplay, Integer theDesignationCount) {
		List<ValueSet.ValueSetExpansionContainsComponent> contains = theValueSet.getExpansion().getContains();

		Stream<ValueSet.ValueSetExpansionContainsComponent> stream = contains.stream();
		if (theSystem != null) {
			stream = stream.filter(concept -> theSystem.equalsIgnoreCase(concept.getSystem()));
		}
		if (theCode != null) {
			stream = stream.filter(concept -> theCode.equalsIgnoreCase(concept.getCode()));
		}
		if (theDisplay != null) {
			stream = stream.filter(concept -> theDisplay.equalsIgnoreCase(concept.getDisplay()));
		}
		if (theDesignationCount != null) {
			stream = stream.filter(concept -> concept.getDesignation().size() == theDesignationCount);
		}

		Optional<ValueSet.ValueSetExpansionContainsComponent> first = stream.findFirst();
		if (!first.isPresent()) {
			String failureMessage = String.format("Expanded ValueSet %s did not contain concept [%s|%s|%s] with [%d] designations", theValueSet.getId(), theSystem, theCode, theDisplay, theDesignationCount);
			fail(failureMessage);
			return null;
		} else {
			return first.get();
		}
	}

	@AfterEach
	public void afterEachClearCaches() {
		myJpaValidationSupportChain.invalidateCaches();
	}


}
