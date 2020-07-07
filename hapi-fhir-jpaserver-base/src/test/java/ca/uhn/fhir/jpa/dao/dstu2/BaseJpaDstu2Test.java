package ca.uhn.fhir.jpa.dao.dstu2;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoPatient;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoSubscription;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.bulk.api.IBulkDataExportSvc;
import ca.uhn.fhir.jpa.config.TestDstu2Config;
import ca.uhn.fhir.jpa.dao.BaseJpaTest;
import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamStringDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamTokenDao;
import ca.uhn.fhir.jpa.dao.data.IResourceLinkDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.provider.JpaSystemProviderDstu2;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.jpa.search.reindex.IResourceReindexingSvc;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.jpa.sp.ISearchParamPresenceSvc;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionLoader;
import ca.uhn.fhir.jpa.util.ResourceCountCache;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.MetaDt;
import ca.uhn.fhir.model.dstu2.resource.Appointment;
import ca.uhn.fhir.model.dstu2.resource.Binary;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Communication;
import ca.uhn.fhir.model.dstu2.resource.ConceptMap;
import ca.uhn.fhir.model.dstu2.resource.Conformance;
import ca.uhn.fhir.model.dstu2.resource.Device;
import ca.uhn.fhir.model.dstu2.resource.DiagnosticOrder;
import ca.uhn.fhir.model.dstu2.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu2.resource.Encounter;
import ca.uhn.fhir.model.dstu2.resource.Group;
import ca.uhn.fhir.model.dstu2.resource.Immunization;
import ca.uhn.fhir.model.dstu2.resource.Location;
import ca.uhn.fhir.model.dstu2.resource.Media;
import ca.uhn.fhir.model.dstu2.resource.Medication;
import ca.uhn.fhir.model.dstu2.resource.MedicationAdministration;
import ca.uhn.fhir.model.dstu2.resource.MedicationOrder;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Organization;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.resource.Practitioner;
import ca.uhn.fhir.model.dstu2.resource.Questionnaire;
import ca.uhn.fhir.model.dstu2.resource.QuestionnaireResponse;
import ca.uhn.fhir.model.dstu2.resource.SearchParameter;
import ca.uhn.fhir.model.dstu2.resource.StructureDefinition;
import ca.uhn.fhir.model.dstu2.resource.Subscription;
import ca.uhn.fhir.model.dstu2.resource.Substance;
import ca.uhn.fhir.model.dstu2.resource.ValueSet;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.provider.ResourceProviderFactory;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestDstu2Config.class})
public abstract class BaseJpaDstu2Test extends BaseJpaTest {
	@Autowired
	@Qualifier("myResourceCountsCache")
	protected ResourceCountCache myResourceCountsCache;
	@Autowired
	protected ISearchParamRegistry mySearchParamRegistry;
	@Autowired
	protected ApplicationContext myAppCtx;
	@Autowired
	protected IResourceReindexingSvc myResourceReindexingSvc;
	@Autowired
	@Qualifier("myAppointmentDaoDstu2")
	protected IFhirResourceDao<Appointment> myAppointmentDao;
	@Autowired
	@Qualifier("mySearchParameterDaoDstu2")
	protected IFhirResourceDao<SearchParameter> mySearchParameterDao;
	@Autowired
	@Qualifier("myCommunicationDaoDstu2")
	protected IFhirResourceDao<Communication> myCommunicationDao;
	@Autowired
	@Qualifier("myBundleDaoDstu2")
	protected IFhirResourceDao<Bundle> myBundleDao;
	@Autowired
	@Qualifier("myConceptMapDaoDstu2")
	protected IFhirResourceDao<ConceptMap> myConceptMapDao;
	@Autowired
	protected DaoConfig myDaoConfig;
	@Autowired
	protected ModelConfig myModelConfig;
	@Autowired
	@Qualifier("myDeviceDaoDstu2")
	protected IFhirResourceDao<Device> myDeviceDao;
	@Autowired
	@Qualifier("myDiagnosticOrderDaoDstu2")
	protected IFhirResourceDao<DiagnosticOrder> myDiagnosticOrderDao;
	@Autowired
	@Qualifier("myDiagnosticReportDaoDstu2")
	protected IFhirResourceDao<DiagnosticReport> myDiagnosticReportDao;
	@Autowired
	@Qualifier("myBinaryDaoDstu2")
	protected IFhirResourceDao<Binary> myBinaryDao;
	@Autowired
	@Qualifier("myEncounterDaoDstu2")
	protected IFhirResourceDao<Encounter> myEncounterDao;
	//	@PersistenceContext()
	@Autowired
	protected EntityManager myEntityManager;
	@Autowired
	@Qualifier("myFhirContextDstu2")
	protected FhirContext myFhirCtx;
	@Autowired
	@Qualifier("myImmunizationDaoDstu2")
	protected IFhirResourceDao<Immunization> myImmunizationDao;
	@Autowired
	@Qualifier("myLocationDaoDstu2")
	protected IFhirResourceDao<Location> myLocationDao;
	@Autowired
	@Qualifier("myMediaDaoDstu2")
	protected IFhirResourceDao<Media> myMediaDao;
	@Autowired
	@Qualifier("myMedicationAdministrationDaoDstu2")
	protected IFhirResourceDao<MedicationAdministration> myMedicationAdministrationDao;
	@Autowired
	@Qualifier("myMedicationDaoDstu2")
	protected IFhirResourceDao<Medication> myMedicationDao;
	@Autowired
	@Qualifier("myMedicationOrderDaoDstu2")
	protected IFhirResourceDao<MedicationOrder> myMedicationOrderDao;
	@Autowired
	@Qualifier("myObservationDaoDstu2")
	protected IFhirResourceDao<Observation> myObservationDao;
	@Autowired
	@Qualifier("myOrganizationDaoDstu2")
	protected IFhirResourceDao<Organization> myOrganizationDao;
	@Autowired
	protected DatabaseBackedPagingProvider myPagingProvider;
	@Autowired
	@Qualifier("myPatientDaoDstu2")
	protected IFhirResourceDaoPatient<Patient> myPatientDao;
	@Autowired
	@Qualifier("myConformanceDaoDstu2")
	protected IFhirResourceDao<Conformance> myConformanceDao;
	@Autowired
	@Qualifier("myGroupDaoDstu2")
	protected IFhirResourceDao<Group> myGroupDao;
	@Autowired
	@Qualifier("myPractitionerDaoDstu2")
	protected IFhirResourceDao<Practitioner> myPractitionerDao;
	@Autowired
	@Qualifier("myQuestionnaireDaoDstu2")
	protected IFhirResourceDao<Questionnaire> myQuestionnaireDao;
	@Autowired
	@Qualifier("myQuestionnaireResponseDaoDstu2")
	protected IFhirResourceDao<QuestionnaireResponse> myQuestionnaireResponseDao;
	@Autowired
	@Qualifier("myResourceProvidersDstu2")
	protected ResourceProviderFactory myResourceProviders;
	@Autowired
	protected ISearchCoordinatorSvc mySearchCoordinatorSvc;
	@Autowired(required = false)
	protected IFulltextSearchSvc mySearchDao;
	@Autowired
	protected ISearchParamPresenceSvc mySearchParamPresenceSvc;
	@Autowired
	@Qualifier("myStructureDefinitionDaoDstu2")
	protected IFhirResourceDao<StructureDefinition> myStructureDefinitionDao;
	@Autowired
	@Qualifier("mySubscriptionDaoDstu2")
	protected IFhirResourceDaoSubscription<Subscription> mySubscriptionDao;
	@Autowired
	@Qualifier("mySubstanceDaoDstu2")
	protected IFhirResourceDao<Substance> mySubstanceDao;
	@Autowired
	protected IResourceIndexedSearchParamStringDao myResourceIndexedSearchParamStringDao;
	@Autowired
	protected IResourceIndexedSearchParamTokenDao myResourceIndexedSearchParamTokenDao;
	@Autowired
	protected IResourceLinkDao myResourceLinkDao;
	@Autowired
	protected IResourceTableDao myResourceTableDao;
	@Autowired
	@Qualifier("mySystemDaoDstu2")
	protected IFhirSystemDao<Bundle, MetaDt> mySystemDao;
	@Autowired
	@Qualifier("mySystemProviderDstu2")
	protected JpaSystemProviderDstu2 mySystemProvider;
	@Autowired
	protected PlatformTransactionManager myTxManager;
	@Autowired
	@Qualifier("myValueSetDaoDstu2")
	protected IFhirResourceDaoValueSet<ValueSet, CodingDt, CodeableConceptDt> myValueSetDao;
	@Autowired
	protected SubscriptionLoader mySubscriptionLoader;
	@Autowired
	private IBulkDataExportSvc myBulkDataExportSvc;

	@BeforeEach
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
		myDaoConfig.setAllowExternalReferences(new DaoConfig().isAllowExternalReferences());
	}

	@AfterEach
	public void afterResetInterceptors() {
		myInterceptorRegistry.unregisterAllInterceptors();
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

	@Override
	public TransactionTemplate newTxTemplate() {
		TransactionTemplate retVal = new TransactionTemplate(myTxManager);
		retVal.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		retVal.afterPropertiesSet();
		return retVal;
	}


}
