package ca.uhn.fhir.jpa.dao.dstu3;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoCodeSystem;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoConceptMap;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoPatient;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoStructureDefinition;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoSubscription;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.batch.api.IBatchJobSubmitter;
import ca.uhn.fhir.jpa.bulk.api.IBulkDataExportSvc;
import ca.uhn.fhir.jpa.config.TestDstu3Config;
import ca.uhn.fhir.jpa.dao.BaseJpaTest;
import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedCompositeStringUniqueDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamStringDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamTokenDao;
import ca.uhn.fhir.jpa.dao.data.IResourceReindexJobDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTagDao;
import ca.uhn.fhir.jpa.dao.data.ITagDefinitionDao;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemDao;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemVersionDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptMapDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptMapGroupElementTargetDao;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetDao;
import ca.uhn.fhir.jpa.dao.dstu2.FhirResourceDaoDstu2SearchNoFtTest;
import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.provider.dstu3.JpaSystemProviderDstu3;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.jpa.search.IStaleSearchDeletingSvc;
import ca.uhn.fhir.jpa.search.reindex.IResourceReindexingSvc;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.jpa.sp.ISearchParamPresenceSvc;
import ca.uhn.fhir.jpa.term.BaseTermReadSvcImpl;
import ca.uhn.fhir.jpa.term.TermDeferredStorageSvcImpl;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.jpa.util.ResourceCountCache;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.provider.ResourceProviderFactory;
import ca.uhn.fhir.util.UrlUtil;
import org.apache.commons.io.IOUtils;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.hl7.fhir.dstu3.model.AllergyIntolerance;
import org.hl7.fhir.dstu3.model.Appointment;
import org.hl7.fhir.dstu3.model.AuditEvent;
import org.hl7.fhir.dstu3.model.Binary;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.CarePlan;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Communication;
import org.hl7.fhir.dstu3.model.CompartmentDefinition;
import org.hl7.fhir.dstu3.model.Composition;
import org.hl7.fhir.dstu3.model.ConceptMap;
import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.Consent;
import org.hl7.fhir.dstu3.model.Coverage;
import org.hl7.fhir.dstu3.model.Device;
import org.hl7.fhir.dstu3.model.DiagnosticReport;
import org.hl7.fhir.dstu3.model.Encounter;
import org.hl7.fhir.dstu3.model.Group;
import org.hl7.fhir.dstu3.model.Immunization;
import org.hl7.fhir.dstu3.model.ImmunizationRecommendation;
import org.hl7.fhir.dstu3.model.Location;
import org.hl7.fhir.dstu3.model.Media;
import org.hl7.fhir.dstu3.model.Medication;
import org.hl7.fhir.dstu3.model.MedicationAdministration;
import org.hl7.fhir.dstu3.model.MedicationRequest;
import org.hl7.fhir.dstu3.model.MedicationStatement;
import org.hl7.fhir.dstu3.model.Meta;
import org.hl7.fhir.dstu3.model.NamingSystem;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.OperationDefinition;
import org.hl7.fhir.dstu3.model.Organization;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.hl7.fhir.dstu3.model.PractitionerRole;
import org.hl7.fhir.dstu3.model.ProcedureRequest;
import org.hl7.fhir.dstu3.model.Questionnaire;
import org.hl7.fhir.dstu3.model.QuestionnaireResponse;
import org.hl7.fhir.dstu3.model.SearchParameter;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.hl7.fhir.dstu3.model.Subscription;
import org.hl7.fhir.dstu3.model.Substance;
import org.hl7.fhir.dstu3.model.Task;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.AopTestUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.hl7.fhir.convertors.conv30_40.ConceptMap30_40.convertConceptMap;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestDstu3Config.class})
public abstract class BaseJpaDstu3Test extends BaseJpaTest {

	private static IValidationSupport ourJpaValidationSupportChainDstu3;
	private static IFhirResourceDaoValueSet<ValueSet, Coding, CodeableConcept> ourValueSetDao;

	@Autowired
	protected IBatchJobSubmitter myBatchJobSubmitter;
	@Autowired
	protected ITermDeferredStorageSvc myTerminologyDeferredStorageSvc;
	@Autowired
	@Qualifier("myResourceCountsCache")
	protected ResourceCountCache myResourceCountsCache;
	@Autowired
	protected IResourceReindexingSvc myResourceReindexingSvc;
	@Autowired
	protected IResourceReindexJobDao myResourceReindexJobDao;
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
	protected ITermConceptDao myConceptDao;
	@Autowired
	@Qualifier("myConditionDaoDstu3")
	protected IFhirResourceDao<Condition> myConditionDao;
	@Autowired
	protected DaoConfig myDaoConfig;
	@Autowired
	protected ModelConfig myModelConfig;
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
	@Autowired
	@Qualifier("myLocationDaoDstu3")
	protected IFhirResourceDao<Location> myLocationDao;
	@Autowired
	@Qualifier("myPractitionerRoleDaoDstu3")
	protected IFhirResourceDao<PractitionerRole> myPractitionerRoleDao;
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
	protected ResourceProviderFactory myResourceProviders;
	@Autowired
	protected IResourceIndexedSearchParamStringDao myResourceIndexedSearchParamStringDao;
	@Autowired
	protected IResourceIndexedSearchParamTokenDao myResourceIndexedSearchParamTokenDao;
	@Autowired
	protected IResourceTableDao myResourceTableDao;
	@Autowired
	protected IResourceTagDao myResourceTagDao;
	@Autowired
	protected ISearchCoordinatorSvc mySearchCoordinatorSvc;
	@Autowired(required = false)
	protected IFulltextSearchSvc mySearchDao;
	@Autowired
	@Qualifier("mySearchParameterDaoDstu3")
	protected IFhirResourceDao<SearchParameter> mySearchParameterDao;
	@Autowired
	protected ISearchParamPresenceSvc mySearchParamPresenceSvc;
	@Autowired
	protected ISearchParamRegistry mySearchParamRegistry;
	@Autowired
	protected IStaleSearchDeletingSvc myStaleSearchDeletingSvc;
	@Autowired
	@Qualifier("myStructureDefinitionDaoDstu3")
	protected IFhirResourceDaoStructureDefinition<StructureDefinition> myStructureDefinitionDao;
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
	protected ITermConceptDao myTermConceptDao;
	@Autowired
	protected ITermCodeSystemDao myTermCodeSystemDao;
	@Autowired
	protected ITermCodeSystemVersionDao myTermCodeSystemVersionDao;
	@Autowired
	protected ITermReadSvc myTermSvc;
	@Autowired
	protected PlatformTransactionManager myTransactionMgr;
	@Autowired
	protected PlatformTransactionManager myTxManager;
	@Autowired
	@Qualifier("myJpaValidationSupportChain")
	protected IValidationSupport myValidationSupport;
	@Autowired
	@Qualifier("myValueSetDaoDstu3")
	protected IFhirResourceDaoValueSet<ValueSet, Coding, CodeableConcept> myValueSetDao;
	@Autowired
	protected ITermConceptMapDao myTermConceptMapDao;
	@Autowired
	protected ITermConceptMapGroupElementTargetDao myTermConceptMapGroupElementTargetDao;
	@Autowired
	protected ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;
	@Autowired
	private IValidationSupport myJpaValidationSupportChainDstu3;
	@Autowired
	private IBulkDataExportSvc myBulkDataExportSvc;
	@Autowired
	protected ITermValueSetDao myTermValueSetDao;

	@AfterEach()
	public void afterCleanupDao() {
		myDaoConfig.setExpireSearchResults(new DaoConfig().isExpireSearchResults());
		myDaoConfig.setExpireSearchResultsAfterMillis(new DaoConfig().getExpireSearchResultsAfterMillis());
		myDaoConfig.setReuseCachedSearchResultsForMillis(new DaoConfig().getReuseCachedSearchResultsForMillis());
		myDaoConfig.setSuppressUpdatesWithNoChange(new DaoConfig().isSuppressUpdatesWithNoChange());
	}

	@AfterEach
	public void afterResetInterceptors() {
		myInterceptorRegistry.unregisterAllInterceptors();
	}

	@AfterEach
	public void afterClearTerminologyCaches() {
		BaseTermReadSvcImpl baseHapiTerminologySvc = AopTestUtils.getTargetObject(myTermSvc);
		baseHapiTerminologySvc.clearCaches();
		BaseTermReadSvcImpl.clearOurLastResultsFromTranslationCache();
		BaseTermReadSvcImpl.clearOurLastResultsFromTranslationWithReverseCache();
		TermDeferredStorageSvcImpl deferredSvc = AopTestUtils.getTargetObject(myTerminologyDeferredStorageSvc);
		deferredSvc.clearDeferred();
	}

	@AfterEach()
	public void afterGrabCaches() {
		ourValueSetDao = myValueSetDao;
		ourJpaValidationSupportChainDstu3 = myJpaValidationSupportChainDstu3;
	}

	@BeforeEach
	public void beforeFlushFT() {
		runInTransaction(() -> {
			SearchSession searchSession  = Search.session(myEntityManager);
			searchSession.workspace(ResourceTable.class).purge();
//			searchSession.workspace(ResourceIndexedSearchParamString.class).purge();
			searchSession.indexingPlan().execute();
		});

		myDaoConfig.setSchedulingDisabled(true);
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.ENABLED);
	}

	@BeforeEach
	@Transactional()
	public void beforePurgeDatabase() {
		purgeDatabase(myDaoConfig, mySystemDao, myResourceReindexingSvc, mySearchCoordinatorSvc, mySearchParamRegistry, myBulkDataExportSvc);
	}

	@BeforeEach
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
		String string = IOUtils.toString(stream, StandardCharsets.UTF_8);
		IParser newJsonParser = EncodingEnum.detectEncodingNoDefault(string).newParser(myFhirCtx);
		return newJsonParser.parseResource(type, string);
	}

	@Override
	public TransactionTemplate newTxTemplate() {
		TransactionTemplate retVal = new TransactionTemplate(myTxManager);
		retVal.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		retVal.afterPropertiesSet();
		return retVal;
	}

	@AfterAll
	public static void afterClassClearContextBaseJpaDstu3Test() {
		if (ourValueSetDao != null) {
			ourValueSetDao.purgeCaches();
		}
		if (ourJpaValidationSupportChainDstu3 != null) {
			ourJpaValidationSupportChainDstu3.invalidateCaches();
		}
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
			return convertConceptMap(BaseJpaR4Test.createConceptMap());
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
