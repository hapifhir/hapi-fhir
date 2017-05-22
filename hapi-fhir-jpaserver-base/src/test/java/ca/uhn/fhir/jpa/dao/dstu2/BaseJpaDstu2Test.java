package ca.uhn.fhir.jpa.dao.dstu2;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.io.InputStream;

import javax.persistence.EntityManager;

import org.apache.commons.io.IOUtils;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.config.TestDstu2Config;
import ca.uhn.fhir.jpa.dao.BaseJpaTest;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.IFhirResourceDaoPatient;
import ca.uhn.fhir.jpa.dao.IFhirResourceDaoSubscription;
import ca.uhn.fhir.jpa.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.provider.JpaSystemProviderDstu2;
import ca.uhn.fhir.jpa.search.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.sp.ISearchParamPresenceSvc;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.MetaDt;
import ca.uhn.fhir.model.dstu2.resource.Appointment;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.ConceptMap;
import ca.uhn.fhir.model.dstu2.resource.Device;
import ca.uhn.fhir.model.dstu2.resource.DiagnosticOrder;
import ca.uhn.fhir.model.dstu2.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu2.resource.Encounter;
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
import ca.uhn.fhir.model.dstu2.resource.StructureDefinition;
import ca.uhn.fhir.model.dstu2.resource.Subscription;
import ca.uhn.fhir.model.dstu2.resource.Substance;
import ca.uhn.fhir.model.dstu2.resource.ValueSet;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.method.MethodUtil;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.util.TestUtil;

//@formatter:off
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestDstu2Config.class, ca.uhn.fhir.jpa.config.WebsocketDstu2DispatcherConfig.class})
//@formatter:on
public abstract class BaseJpaDstu2Test extends BaseJpaTest {

	@Autowired
	protected ApplicationContext myAppCtx;
	@Autowired
	@Qualifier("myAppointmentDaoDstu2")
	protected IFhirResourceDao<Appointment> myAppointmentDao;
	@Autowired
	@Qualifier("myBundleDaoDstu2")
	protected IFhirResourceDao<Bundle> myBundleDao;
	@Autowired
	@Qualifier("myConceptMapDaoDstu2")
	protected IFhirResourceDao<ConceptMap> myConceptMapDao;
	@Autowired
	protected DaoConfig myDaoConfig;
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
	protected IServerInterceptor myInterceptor;
	@Autowired
	@Qualifier("myLocationDaoDstu2")
	protected IFhirResourceDao<Location> myLocationDao;
	
@Autowired
	@Qualifier("myMediaDaoDstu2")
	protected IFhirResourceDao<Media> myMediaDao;
	
	@Autowired
	@Qualifier("myMedicationDaoDstu2")
	protected IFhirResourceDao<Medication> myMedicationDao;
	@Autowired
	@Qualifier("myMedicationAdministrationDaoDstu2")
	protected IFhirResourceDao<MedicationAdministration> myMedicationAdministrationDao;
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
	@Qualifier("myPatientDaoDstu2")
	protected IFhirResourceDaoPatient<Patient> myPatientDao;
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
	protected Object myResourceProviders;
	@Autowired
	protected IFulltextSearchSvc mySearchDao;
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
	protected ISearchParamPresenceSvc mySearchParamPresenceSvc;
	@Autowired
	protected ISearchCoordinatorSvc mySearchCoordinatorSvc;

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
		purgeDatabase(entityManager, myTxManager, mySearchParamPresenceSvc, mySearchCoordinatorSvc);
	}

	@Before
	public void beforeResetConfig() {
		myDaoConfig.setHardSearchLimit(1000);
		myDaoConfig.setHardTagListLimit(1000);
		myDaoConfig.setIncludeLimit(2000);
		myDaoConfig.setAllowExternalReferences(new DaoConfig().isAllowExternalReferences());
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
		retVal.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRES_NEW);
		retVal.afterPropertiesSet();
		return retVal;
	}

	@AfterClass
	public static void afterClassClearContext() throws Exception {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
