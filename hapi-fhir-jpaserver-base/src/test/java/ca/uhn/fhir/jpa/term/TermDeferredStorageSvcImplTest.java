package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemVersionDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDao;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TermDeferredStorageSvcImplTest {


	@Mock
	private PlatformTransactionManager myTxManager;
	@Mock
	private ITermCodeSystemStorageSvc myTermConceptStorageSvc;
	@Mock
	private ITermConceptDao myConceptDao;
	@Mock
	private ITermCodeSystemVersionDao myTermCodeSystemVersionDao;

	@Test
	public void testSaveDeferredWithExecutionSuspended() {
		TermDeferredStorageSvcImpl svc = new TermDeferredStorageSvcImpl();
		svc.setProcessDeferred(false);
		svc.saveDeferred();
	}


	@Test
	public void testSaveDeferred_Concept() {
		TermConcept concept = new TermConcept();
		concept.setCode("CODE_A");

		TermCodeSystemVersion myTermCodeSystemVersion = new TermCodeSystemVersion();
		myTermCodeSystemVersion.setId(1L);
		concept.setCodeSystemVersion(myTermCodeSystemVersion);

		TermDeferredStorageSvcImpl svc = new TermDeferredStorageSvcImpl();
		svc.setTransactionManagerForUnitTest(myTxManager);
		svc.setCodeSystemStorageSvcForUnitTest(myTermConceptStorageSvc);
		svc.setDaoConfigForUnitTest(new DaoConfig());

		when(myTermCodeSystemVersionDao.findById(anyLong())).thenReturn(Optional.of(myTermCodeSystemVersion));
		svc.setCodeSystemVersionDaoForUnitTest(myTermCodeSystemVersionDao);
		svc.setProcessDeferred(true);
		svc.addConceptToStorageQueue(concept);
		svc.saveDeferred();
		verify(myTermConceptStorageSvc, times(1)).saveConcept(same(concept));
		verifyNoMoreInteractions(myTermConceptStorageSvc);

	}

	@Test
	public void testSaveDeferred_Concept_StaleCodeSystemVersion() {
		TermConcept concept = new TermConcept();
		concept.setCode("CODE_A");

		TermCodeSystemVersion myTermCodeSystemVersion = new TermCodeSystemVersion();
		concept.setCodeSystemVersion(myTermCodeSystemVersion);

		TermDeferredStorageSvcImpl svc = new TermDeferredStorageSvcImpl();
		svc.setTransactionManagerForUnitTest(myTxManager);
		svc.setCodeSystemStorageSvcForUnitTest(myTermConceptStorageSvc);
		svc.setDaoConfigForUnitTest(new DaoConfig());

		svc.setCodeSystemVersionDaoForUnitTest(myTermCodeSystemVersionDao);
		svc.setProcessDeferred(true);
		svc.addConceptToStorageQueue(concept);
		svc.saveDeferred();
		verify(myTermConceptStorageSvc, times(0)).saveConcept(same(concept));
		verifyNoMoreInteractions(myTermConceptStorageSvc);

	}

	@Test
	public void testSaveDeferred_ConceptParentChildLink_ConceptsMissing() {
		TermConceptParentChildLink conceptLink = new TermConceptParentChildLink();
		conceptLink.setChild(new TermConcept().setId(111L));
		conceptLink.setParent(new TermConcept().setId(222L));

		TermDeferredStorageSvcImpl svc = new TermDeferredStorageSvcImpl();
		svc.setTransactionManagerForUnitTest(myTxManager);
		svc.setCodeSystemStorageSvcForUnitTest(myTermConceptStorageSvc);
		svc.setConceptDaoForUnitTest(myConceptDao);
		svc.setDaoConfigForUnitTest(new DaoConfig());
		svc.setProcessDeferred(true);
		svc.addConceptLinkToStorageQueue(conceptLink);
		svc.saveDeferred();

		verifyNoMoreInteractions(myTermConceptStorageSvc);
	}

}
