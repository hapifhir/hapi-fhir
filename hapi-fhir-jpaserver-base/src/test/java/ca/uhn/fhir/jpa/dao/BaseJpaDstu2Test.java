package ca.uhn.fhir.jpa.dao;

import static org.mockito.Mockito.mock;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import ca.uhn.fhir.context.FhirContext;
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
import ca.uhn.fhir.jpa.entity.TagDefinition;
import ca.uhn.fhir.jpa.provider.JpaSystemProviderDstu2;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Device;
import ca.uhn.fhir.model.dstu2.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu2.resource.Encounter;
import ca.uhn.fhir.model.dstu2.resource.Location;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Organization;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.resource.Practitioner;
import ca.uhn.fhir.model.dstu2.resource.Questionnaire;
import ca.uhn.fhir.model.dstu2.resource.QuestionnaireResponse;
import ca.uhn.fhir.model.dstu2.resource.ValueSet;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;

//@formatter:off
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
	"classpath:hapi-fhir-server-resourceproviders-dstu2.xml", 
	"classpath:fhir-jpabase-spring-test-config.xml"})
@TransactionConfiguration(defaultRollback=false)
//@formatter:on
public class BaseJpaDstu2Test extends BaseJpaTest {

	@Autowired
	protected DaoConfig myDaoConfig;
	@Autowired
	@Qualifier("myDeviceDaoDstu2")
	protected IFhirResourceDao<Device> myDeviceDao;
	@Autowired
	@Qualifier("myDiagnosticReportDaoDstu2")
	protected IFhirResourceDao<DiagnosticReport> myDiagnosticReportDao;
	@Autowired
	@Qualifier("myEncounterDaoDstu2")
	protected IFhirResourceDao<Encounter> myEncounterDao;
	@PersistenceContext()
	protected EntityManager myEntityManager;
	@Autowired
	protected FhirContext myFhirCtx;
	protected IServerInterceptor myInterceptor;
	@Autowired
	@Qualifier("myLocationDaoDstu2")
	protected IFhirResourceDao<Location> myLocationDao;
	@Autowired
	@Qualifier("myObservationDaoDstu2")
	protected IFhirResourceDao<Observation> myObservationDao;
	@Autowired
	@Qualifier("myOrganizationDaoDstu2")
	protected IFhirResourceDao<Organization> myOrganizationDao;
	@Autowired
	@Qualifier("myPatientDaoDstu2")
	protected IFhirResourceDao<Patient> myPatientDao;
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
	@Qualifier("mySystemDaoDstu2")
	protected IFhirSystemDao<Bundle> mySystemDao;
	@Autowired
	@Qualifier("mySystemProviderDstu2")
	protected JpaSystemProviderDstu2 mySystemProvider;
	@Autowired
	protected PlatformTransactionManager myTxManager;
	@Autowired
	@Qualifier("myValueSetDaoDstu2")
	protected IFhirResourceDao<ValueSet> myValueSetDao;

	@Before
	public void beforeCreateInterceptor() {
		myInterceptor = mock(IServerInterceptor.class);
		myDaoConfig.setInterceptors(myInterceptor);
	}

	@Before
	@Transactional()
	public void beforePurgeDatabase() {
		final EntityManager entityManager = this.myEntityManager;
		purgeDatabase(entityManager, myTxManager);
	}

	/**
	 * Just so that JUnit doesn't complain about no test methods in this class
	 */
	@Test
	public void doNothing() {
		// nothing
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
