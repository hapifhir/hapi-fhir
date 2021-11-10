package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemVersionDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDao;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.JobExecution;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TermDeferredStorageSvcImplTest {

	@Mock
	private PlatformTransactionManager myTxManager;
	@Mock
	private ITermCodeSystemStorageSvc myTermConceptStorageSvc;
	@Mock
	private ITermConceptDao myConceptDao;
	@Mock
	private ITermCodeSystemVersionDao myTermCodeSystemVersionDao;

	@Mock
	private JobExecution myJobExecution;

	@Test
	public void testSaveDeferredWithExecutionSuspended() {
		TermDeferredStorageSvcImpl svc = new TermDeferredStorageSvcImpl();
		svc.setProcessDeferred(false);
		svc.saveDeferred();
	}


	@Test
	public void testStorageNotEmptyWhileJobsExecuting() {
		TermDeferredStorageSvcImpl svc = new TermDeferredStorageSvcImpl();
		ReflectionTestUtils.setField(svc, "currentJobExecutions", Collections.singletonList(myJobExecution));

		when(myJobExecution.isRunning()).thenReturn(true, false);
		assertFalse(svc.isStorageQueueEmpty());
		assertTrue(svc.isStorageQueueEmpty());
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
		myTermCodeSystemVersion.setId(1L);
		concept.setCodeSystemVersion(myTermCodeSystemVersion);

		TermDeferredStorageSvcImpl svc = new TermDeferredStorageSvcImpl();
		svc.setTransactionManagerForUnitTest(myTxManager);
		svc.setCodeSystemStorageSvcForUnitTest(myTermConceptStorageSvc);

		when(myTermCodeSystemVersionDao.findById(anyLong())).thenReturn(Optional.empty());
		svc.setCodeSystemVersionDaoForUnitTest(myTermCodeSystemVersionDao);
		svc.setProcessDeferred(true);
		svc.addConceptToStorageQueue(concept);
		svc.saveDeferred();

		verify(myTermConceptStorageSvc, times(0)).saveConcept(same(concept));
		verifyNoMoreInteractions(myTermConceptStorageSvc);

	}

	@Test
	public void testSaveDeferred_Concept_Exception() {
		// There is a small
		TermConcept concept = new TermConcept();
		concept.setCode("CODE_A");

		TermCodeSystemVersion myTermCodeSystemVersion = new TermCodeSystemVersion();
		myTermCodeSystemVersion.setId(1L);
		concept.setCodeSystemVersion(myTermCodeSystemVersion);

		TermDeferredStorageSvcImpl svc = new TermDeferredStorageSvcImpl();
		svc.setTransactionManagerForUnitTest(myTxManager);
		svc.setCodeSystemStorageSvcForUnitTest(myTermConceptStorageSvc);

		// Simulate the case where an exception is thrown despite a valid code system version.
		when(myTermCodeSystemVersionDao.findById(anyLong())).thenReturn(Optional.of(myTermCodeSystemVersion));
		when(myTermConceptStorageSvc.saveConcept(concept)).thenThrow(new RuntimeException("Foreign Constraint Violation"));
		svc.setCodeSystemVersionDaoForUnitTest(myTermCodeSystemVersionDao);
		svc.setProcessDeferred(true);
		svc.addConceptToStorageQueue(concept);
		svc.saveDeferred();

		verify(myTermConceptStorageSvc, times(1)).saveConcept(same(concept));
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
		svc.setProcessDeferred(true);
		svc.addConceptLinkToStorageQueue(conceptLink);
		svc.saveDeferred();

		verifyNoMoreInteractions(myTermConceptStorageSvc);
	}

}
