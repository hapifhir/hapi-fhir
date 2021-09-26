package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.executor.InterceptorService;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.cache.IResourceVersionSvc;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.dao.r4.TransactionProcessorVersionAdapterR4;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryResourceMatcher;
import ca.uhn.fhir.jpa.searchparam.matcher.SearchParamMatcher;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.BundleBuilder;
import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.MedicationKnowledge;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
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
	private IdHelperService myIdHelperService;
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
	private FhirContext myFhirCtx = FhirContext.forR4Cached();

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
	public void testTransactionWithPlaceholderIds() {


		BundleBuilder bb = new BundleBuilder(myFhirCtx);
		for (int i = 0; i < 100; i++) {
			Patient pt = new Patient();
			pt.setId(IdType.newRandomUuid());
			pt.addIdentifier().setSystem("http://foo").setValue("val" + i);
			bb.addTransactionCreateEntry(pt);

			Observation obs = new Observation();
			obs.setId(IdType.newRandomUuid());
			obs.setSubject(new Reference(pt.getId()));
			bb.addTransactionCreateEntry(obs);
		}
		Bundle bundle = (Bundle) bb.getBundle();

		Bundle outcome = myTransactionProcessor.transaction(null, bundle, false);

		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
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
			assertEquals("Resource MedicationKnowledge is not supported on this server. Supported resource types: []", e.getMessage());
		}
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
