package ca.uhn.fhir.jpa.dao.dstu21;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.io.InputStream;

import javax.persistence.EntityManager;

import org.apache.commons.io.IOUtils;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.dao.IFhirResourceDao;
import ca.uhn.fhir.dao.IFhirResourceDaoPatient;
import ca.uhn.fhir.dao.IFhirResourceDaoSubscription;
import ca.uhn.fhir.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.dao.IFhirSystemDao;
import ca.uhn.fhir.dao.ISearchDao;
import ca.uhn.fhir.jpa.config.TestDstu21Config;
import ca.uhn.fhir.jpa.dao.BaseJpaTest;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.JpaDaoFactory;
import ca.uhn.fhir.jpa.dao.dstu2.FhirResourceDaoDstu2SearchNoFtTest;
import ca.uhn.fhir.jpa.entity.ForcedId;
import ca.uhn.fhir.jpa.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.entity.ResourceHistoryTag;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamCoords;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamNumber;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamQuantity;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamUri;
import ca.uhn.fhir.jpa.entity.ResourceLink;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.entity.ResourceTag;
import ca.uhn.fhir.jpa.entity.SubscriptionFlaggedResource;
import ca.uhn.fhir.jpa.entity.SubscriptionTable;
import ca.uhn.fhir.jpa.entity.TagDefinition;
import ca.uhn.fhir.model.dstu21.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu21.composite.CodingDt;
import ca.uhn.fhir.model.dstu21.composite.MetaDt;
import ca.uhn.fhir.model.dstu21.resource.Bundle;
import ca.uhn.fhir.model.dstu21.resource.ConceptMap;
import ca.uhn.fhir.model.dstu21.resource.Device;
import ca.uhn.fhir.model.dstu21.resource.DiagnosticOrder;
import ca.uhn.fhir.model.dstu21.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu21.resource.Encounter;
import ca.uhn.fhir.model.dstu21.resource.Immunization;
import ca.uhn.fhir.model.dstu21.resource.Location;
import ca.uhn.fhir.model.dstu21.resource.Media;
import ca.uhn.fhir.model.dstu21.resource.Medication;
import ca.uhn.fhir.model.dstu21.resource.MedicationOrder;
import ca.uhn.fhir.model.dstu21.resource.Observation;
import ca.uhn.fhir.model.dstu21.resource.Organization;
import ca.uhn.fhir.model.dstu21.resource.Patient;
import ca.uhn.fhir.model.dstu21.resource.Practitioner;
import ca.uhn.fhir.model.dstu21.resource.Questionnaire;
import ca.uhn.fhir.model.dstu21.resource.QuestionnaireResponse;
import ca.uhn.fhir.model.dstu21.resource.StructureDefinition;
import ca.uhn.fhir.model.dstu21.resource.Subscription;
import ca.uhn.fhir.model.dstu21.resource.Substance;
import ca.uhn.fhir.model.dstu21.resource.ValueSet;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.method.MethodUtil;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.provider.impl.SystemProviderDstu21;

//@formatter:off
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestDstu21Config.class, ca.uhn.fhir.provider.config.BaseJavaConfigDstu21.class, ca.uhn.fhir.jpa.config.WebsocketDstu2Config.class})
//@formatter:on
public abstract class BaseJpaDstu21Test extends BaseJpaTest {

	@Autowired
	protected ApplicationContext myAppCtx;
	@Autowired
	protected ISearchDao mySearchDao;
	@Autowired
	@Qualifier("myConceptMapDaoDstu21")
	protected IFhirResourceDao<ConceptMap> myConceptMapDao;
	@Autowired
	@Qualifier("myMedicationDaoDstu21")
	protected IFhirResourceDao<Medication> myMedicationDao;
	@Autowired
	@Qualifier("myMedicationOrderDaoDstu21")
	protected IFhirResourceDao<MedicationOrder> myMedicationOrderDao;
	@Autowired
	protected DaoConfig myDaoConfig;
	@Autowired
	@Qualifier("myDeviceDaoDstu21")
	protected IFhirResourceDao<Device> myDeviceDao;
	@Autowired
	@Qualifier("myDiagnosticOrderDaoDstu21")
	protected IFhirResourceDao<DiagnosticOrder> myDiagnosticOrderDao;
	@Autowired
	@Qualifier("myDiagnosticReportDaoDstu21")
	protected IFhirResourceDao<DiagnosticReport> myDiagnosticReportDao;
	@Autowired
	@Qualifier("myEncounterDaoDstu21")
	protected IFhirResourceDao<Encounter> myEncounterDao;
	
//	@PersistenceContext()
	@Autowired
	protected EntityManager myEntityManager;
	
	@Autowired
	@Qualifier("myFhirContextDstu21")
	protected FhirContext myFhirCtx;
	@Autowired
	@Qualifier("myImmunizationDaoDstu21")
	protected IFhirResourceDao<Immunization> myImmunizationDao;
	protected IServerInterceptor myInterceptor;
	@Autowired
	@Qualifier("myLocationDaoDstu21")
	protected IFhirResourceDao<Location> myLocationDao;
	@Autowired
	@Qualifier("myObservationDaoDstu21")
	protected IFhirResourceDao<Observation> myObservationDao;
	@Autowired
	@Qualifier("myOrganizationDaoDstu21")
	protected IFhirResourceDao<Organization> myOrganizationDao;
	@Autowired
	@Qualifier("myPatientDaoDstu21")
	protected IFhirResourceDaoPatient<Patient> myPatientDao;
	@Autowired
	@Qualifier("myMediaDaoDstu21")
	protected IFhirResourceDao<Media> myMediaDao;
	@Autowired
	@Qualifier("myPractitionerDaoDstu21")
	protected IFhirResourceDao<Practitioner> myPractitionerDao;
	@Autowired
	@Qualifier("myQuestionnaireDaoDstu21")
	protected IFhirResourceDao<Questionnaire> myQuestionnaireDao;
	@Autowired
	@Qualifier("myQuestionnaireResponseDaoDstu21")
	protected IFhirResourceDao<QuestionnaireResponse> myQuestionnaireResponseDao;
	@Autowired
	@Qualifier("myResourceProvidersDstu21")
	protected Object myResourceProviders;
	@Autowired
	@Qualifier("myStructureDefinitionDaoDstu21")
	protected IFhirResourceDao<StructureDefinition> myStructureDefinitionDao;
	@Autowired
	@Qualifier("mySubscriptionDaoDstu21")
	protected IFhirResourceDaoSubscription<Subscription> mySubscriptionDao;
	@Autowired
	@Qualifier("mySubstanceDaoDstu21")
	protected IFhirResourceDao<Substance> mySubstanceDao;
	@Autowired
	@Qualifier("mySystemDaoDstu21")
	protected IFhirSystemDao<Bundle, MetaDt> mySystemDao;
	@Autowired
	@Qualifier("mySystemProviderDstu21")
	protected SystemProviderDstu21 mySystemProvider;
	@Autowired
	protected PlatformTransactionManager myTxManager;
	@Autowired
	@Qualifier("myValueSetDaoDstu21")
	protected IFhirResourceDaoValueSet<ValueSet, CodingDt, CodeableConceptDt> myValueSetDao;
	@Autowired
	@Qualifier("myDaoFactoryDstu21")
	protected JpaDaoFactory myDaoFactory;

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
	public void beforeResetConfig() {
		myDaoConfig.setHardSearchLimit(1000);
		myDaoConfig.setHardTagListLimit(1000);
		myDaoConfig.setIncludeLimit(2000);
	}


	@Before
	@Transactional()
	public void beforePurgeDatabase() {
		final EntityManager entityManager = this.myEntityManager;
		purgeDatabase(entityManager, myTxManager);
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

	public static void purgeDatabase(final EntityManager entityManager, PlatformTransactionManager theTxManager) {
		TransactionTemplate txTemplate = new TransactionTemplate(theTxManager);
		txTemplate.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRED);
		txTemplate.execute(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(TransactionStatus theStatus) {
				entityManager.createQuery("UPDATE " + ResourceHistoryTable.class.getSimpleName() + " d SET d.myForcedId = null").executeUpdate();
				entityManager.createQuery("UPDATE " + ResourceTable.class.getSimpleName() + " d SET d.myForcedId = null").executeUpdate();
				return null;
			}
		});
		txTemplate.execute(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(TransactionStatus theStatus) {
				entityManager.createQuery("DELETE from " + SubscriptionFlaggedResource.class.getSimpleName() + " d").executeUpdate();
				entityManager.createQuery("DELETE from " + ForcedId.class.getSimpleName() + " d").executeUpdate();
				entityManager.createQuery("DELETE from " + ResourceIndexedSearchParamDate.class.getSimpleName() + " d").executeUpdate();
				entityManager.createQuery("DELETE from " + ResourceIndexedSearchParamNumber.class.getSimpleName() + " d").executeUpdate();
				entityManager.createQuery("DELETE from " + ResourceIndexedSearchParamQuantity.class.getSimpleName() + " d").executeUpdate();
				entityManager.createQuery("DELETE from " + ResourceIndexedSearchParamString.class.getSimpleName() + " d").executeUpdate();
				entityManager.createQuery("DELETE from " + ResourceIndexedSearchParamToken.class.getSimpleName() + " d").executeUpdate();
				entityManager.createQuery("DELETE from " + ResourceIndexedSearchParamUri.class.getSimpleName() + " d").executeUpdate();
				entityManager.createQuery("DELETE from " + ResourceIndexedSearchParamCoords.class.getSimpleName() + " d").executeUpdate();
				entityManager.createQuery("DELETE from " + ResourceLink.class.getSimpleName() + " d").executeUpdate();
				return null;
			}
		});
		txTemplate.execute(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(TransactionStatus theStatus) {
				entityManager.createQuery("DELETE from " + SubscriptionTable.class.getSimpleName() + " d").executeUpdate();
				entityManager.createQuery("DELETE from " + ResourceHistoryTag.class.getSimpleName() + " d").executeUpdate();
				entityManager.createQuery("DELETE from " + ResourceTag.class.getSimpleName() + " d").executeUpdate();
				entityManager.createQuery("DELETE from " + TagDefinition.class.getSimpleName() + " d").executeUpdate();
				entityManager.createQuery("DELETE from " + ResourceHistoryTable.class.getSimpleName() + " d").executeUpdate();
				entityManager.createQuery("DELETE from " + ResourceTable.class.getSimpleName() + " d").executeUpdate();
				return null;
			}
		});
	}

}
