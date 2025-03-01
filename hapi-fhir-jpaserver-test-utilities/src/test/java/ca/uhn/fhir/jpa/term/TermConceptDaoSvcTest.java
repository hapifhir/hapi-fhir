package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.entity.TermConcept;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TermConceptDaoSvcTest {

	@Mock
	private EntityManager myEntityManager;

	@InjectMocks
	private TermConceptDaoSvc myTermConceptDaoSvc;

	@ParameterizedTest
	@ValueSource(booleans = {false, true})
	public void testSaveConcept_withSupportLegacyLob(boolean theSupportLegacyLob){
		final String parentPids = "1 2 3 4 5 6 7 8 9";
		doAnswer(t->{
			TermConcept codeSystem = (TermConcept) t.getArguments()[0];
			codeSystem.prePersist();
			return null;
		}).when(myEntityManager).persist(any());

		ArgumentCaptor<TermConcept> captor = ArgumentCaptor.forClass(TermConcept.class);

		// given
		TermConcept termConcept = new TermConcept().setParentPids(parentPids);

		// when
		myTermConceptDaoSvc.setSupportLegacyLob(theSupportLegacyLob);
		myTermConceptDaoSvc.saveConcept(termConcept);

		// then
		verify(myEntityManager, times(1)).persist(captor.capture());
		TermConcept capturedTermConcept = captor.getValue();

		assertEquals(theSupportLegacyLob, capturedTermConcept.hasParentPidsLobForTesting());
		assertEquals(parentPids, capturedTermConcept.getParentPidsAsString());
	}

}
