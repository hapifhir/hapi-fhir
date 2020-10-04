package ca.uhn.fhir.jpa.dao.method;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.model.DeleteConflictList;
import ca.uhn.fhir.jpa.api.model.DeleteMethodOutcome;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirResourceDao;
import ca.uhn.fhir.jpa.dao.MatchResourceUrlService;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.delete.DeleteConflictService;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ResourceDeleterTest {
	private static final FhirContext ourFhirContext = FhirContext.forR4();
	@Autowired
	ResourceDeleter myResourceDeleter;

	@MockBean
	protected FhirContext myFhirContext;
	@MockBean
	protected HapiTransactionService myTransactionService;
	@MockBean
	protected IdHelperService myIdHelperService;
	@MockBean
	protected IRequestPartitionHelperSvc myRequestPartitionHelperService;
	@MockBean
	protected IInterceptorBroadcaster myInterceptorBroadcaster;
	@MockBean
	protected DaoConfig myDaoConfig;
	@MockBean
	protected IResourceTableDao myResourceTableDao;

	@MockBean
	protected EntityManagerFactory myEntityManagerFactory;
	@MockBean
	protected EntityManager myEntityManager;
	@MockBean
	private DeleteConflictService myDeleteConflictService;
	@MockBean
	private MatchResourceUrlService myMatchResourceUrlService;

	@Configuration
	public static class SpringConfig {
		@Bean
		FhirContext fhirContext() {
			return ourFhirContext;
		}
		@Bean
		ResourceDeleter resourceDeleter() {
			BaseHapiFhirResourceDao<Patient> patientDao = mock(BaseHapiFhirResourceDao.class);
			return new ResourceDeleter(patientDao);
		}
	}

	@BeforeEach
	public void before() {
		when(myEntityManagerFactory.createEntityManager()).thenReturn(myEntityManager);
	}

	@Test
	void deleteAndExpungePidList() {
		String url = "/Patient?_expunge=true";
		ResourcePersistentId id = new ResourcePersistentId(123L);
		Collection<ResourcePersistentId> list = Arrays.asList(id);
		DeleteConflictList conflicts = new DeleteConflictList();
		RequestDetails request = mock(RequestDetails.class);
		DeleteMethodOutcome result = myResourceDeleter.deleteAndExpungePidList(list, conflicts, request);
		assertEquals(1, result.getDeletedEntities().size());
	}
}
