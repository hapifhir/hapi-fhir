package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.executor.InterceptorService;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.api.model.DeleteMethodOutcome;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.cache.IResourceVersionSvc;
import ca.uhn.fhir.jpa.dao.r4.FhirResourceDaoObservationR4;
import ca.uhn.fhir.jpa.dao.r4.FhirResourceDaoPatientR4;
import ca.uhn.fhir.jpa.dao.r4.TransactionProcessorVersionAdapterR4;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryResourceMatcher;
import ca.uhn.fhir.jpa.searchparam.matcher.SearchParamMatcher;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.MedicationKnowledge;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TransactionProcessorTest.MyConfig.class)
public class TransactionProcessorTest {

	private static final Logger ourLog = LoggerFactory.getLogger(TransactionProcessorTest.class);
	@Autowired
	private TransactionProcessor myTransactionProcessor;
	@MockBean
	private EntityManagerFactory myEntityManagerFactory;
	@MockBean(answer = Answers.RETURNS_DEEP_STUBS)
	private EntityManager myEntityManager;
	@MockBean
	private PlatformTransactionManager myPlatformTransactionManager;
	@MockBean
	private MatchResourceUrlService myMatchResourceUrlService;
	@MockBean
	private HapiTransactionService myHapiTransactionService;
	@MockBean
	private ModelConfig myModelConfig;
	@MockBean
	private InMemoryResourceMatcher myInMemoryResourceMatcher;
	@MockBean
	private IIdHelperService myIdHelperService;
	@MockBean
	private PartitionSettings myPartitionSettings;
	@MockBean
	private MatchUrlService myMatchUrlService;
	@MockBean
	private IRequestPartitionHelperSvc myRequestPartitionHelperSvc;
	@MockBean
	private IResourceVersionSvc myResourceVersionSvc;
	@MockBean
	private SearchParamMatcher mySearchParamMatcher;
	@MockBean(answer = Answers.RETURNS_DEEP_STUBS)
	private SessionImpl mySession;
	@MockBean
	private IFhirSystemDao<Bundle, Meta> mySystemDao;
	@MockBean
	private DaoRegistry myDaoRegistry;
	@MockBean
	private FhirResourceDaoObservationR4 myObservationDao;
	@MockBean
	private FhirResourceDaoPatientR4 myPatientDao;
	@MockBean
	private DeleteMethodOutcome myDeleteMethodOutcome;
	@MockBean
	private DaoMethodOutcome myDaoMethodOutcome;

	@BeforeEach
	public void before() {
		when(myHapiTransactionService.execute(any(), any(), any())).thenAnswer(t -> {
			TransactionCallback<?> callback = t.getArgument(2, TransactionCallback.class);
			return callback.doInTransaction(mock(TransactionStatus.class));
		});

		myTransactionProcessor.setEntityManagerForUnitTest(myEntityManager);

		when(myEntityManager.unwrap(eq(Session.class))).thenReturn(mySession);
	}


	@Test
	public void testTransactionWithDisabledResourceType() {

		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.TRANSACTION);

		MedicationKnowledge medKnowledge = new MedicationKnowledge();
		medKnowledge.setStatus(MedicationKnowledge.MedicationKnowledgeStatus.ACTIVE);
		input
			.addEntry()
			.setResource(medKnowledge)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("/MedicationKnowledge");

		try {
			myTransactionProcessor.transaction(null, input, false);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(544) + "Resource MedicationKnowledge is not supported on this server. Supported resource types: []", e.getMessage());
		}
	}

	@Test
	public void testTransactionWithDuplicateConditionalCreateAndDelete() {
		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.TRANSACTION);

		String duplicateUrl = "/Patient";
		String duplicateIfNoneExist = "identifier=http://acme.org/mrns|12345";
		String firstPatientFullUrl = "urn:uuid:3ac4fde3-089d-4a2d-829b-f3ef68cae371";
		String secondPatientFullUrl = "urn:uuid:2ab44de3-019d-432d-829b-f3ee08cae395";

		Patient firstPatient = new Patient();
		Identifier firstPatientIdentifier = new Identifier();
		firstPatientIdentifier.setSystem("http://acme.org/mrns");
		firstPatientIdentifier.setValue("12345");
		firstPatient.setIdentifier(List.of(firstPatientIdentifier));

		input
			.addEntry()
			.setResource(firstPatient)
			.setFullUrl(firstPatientFullUrl)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl(duplicateUrl)
			.setIfNoneExist(duplicateIfNoneExist);

		Patient secondPatient = new Patient();
		Identifier secondPatientIdentifier = new Identifier();
		secondPatientIdentifier.setSystem("http://acme.org/mrns");
		secondPatientIdentifier.setValue("12346");
		secondPatient.setIdentifier(List.of(secondPatientIdentifier));

		input
			.addEntry()
			.setResource(secondPatient)
			.setFullUrl(secondPatientFullUrl)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl(duplicateUrl)
			.setIfNoneExist(duplicateIfNoneExist);

		String deleteUrl = "Observation?patient=urn:uuid:2ac34de3-069d-442d-829b-f3ef68cae371";

		input
			.addEntry()
			.getRequest()
			.setMethod(Bundle.HTTPVerb.DELETE)
			.setUrl(deleteUrl);

		DaoConfig config = new DaoConfig();
		config.setDeleteEnabled(true);

		myTransactionProcessor.setDaoRegistry(myDaoRegistry);

		myObservationDao.myTransactionService = myHapiTransactionService;
		myPatientDao.myTransactionService = myHapiTransactionService;

		when(myDaoRegistry.getResourceDao(eq(Observation.class))).thenReturn(myObservationDao);
		when(myObservationDao.deleteByUrl(any(), any(), any())).thenReturn(myDeleteMethodOutcome);
		when(myDeleteMethodOutcome.getOperationOutcome()).thenReturn(new OperationOutcome());

		IdType idType = new IdType();
		idType.setId("Patient/1");
		idType.setValue(firstPatientFullUrl);

		when(myDaoRegistry.getResourceDaoOrNull(eq(Patient.class))).thenReturn(myPatientDao);
		when(myPatientDao.create(any(), anyString(), eq(false), any(), any())).thenReturn(myDaoMethodOutcome);
		when(myDaoMethodOutcome.getId()).thenReturn(idType);
		when(myDaoMethodOutcome.getCreated()).thenReturn(false);

		when(myInMemoryResourceMatcher.canBeEvaluatedInMemory(anyString())).thenReturn(InMemoryMatchResult.successfulMatch());

		Bundle resultBundle = myTransactionProcessor.transaction(null, input, false);

		int expectedEntries = 2;
		String status200 = "200 OK";
		String status204 = "204 No Content";

		List<Bundle.BundleEntryComponent> resultEntries = resultBundle.getEntry();

		assertEquals(expectedEntries, resultEntries.size());
		assertEquals(status200, resultEntries.get(0).getResponse().getStatus());
		assertEquals(status204, resultEntries.get(1).getResponse().getStatus());
	}


	@Configuration
	public static class MyConfig {

		@Bean
		public DaoRegistry daoRegistry() {
			return new DaoRegistry();
		}

		@Bean
		public FhirContext fhirContext() {
			return FhirContext.forR4Cached();
		}

		@Bean
		public TransactionProcessor transactionProcessor() {
			return new TransactionProcessor();
		}

		@Bean
		public InterceptorService interceptorService() {
			return new InterceptorService();
		}

		@Bean
		public DaoConfig daoConfig() {
			return new DaoConfig();
		}

		@Bean
		public ITransactionProcessorVersionAdapter<Bundle, Bundle.BundleEntryComponent> versionAdapter() {
			return new TransactionProcessorVersionAdapterR4();
		}


	}
}
