package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.dao.data.ITermValueSetConceptDao;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetConceptDesignationDao;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetDao;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.entity.TermValueSetConcept;
import ca.uhn.fhir.jpa.entity.TermValueSetConceptDesignation;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ValueSetConceptAccumulatorTest {

	private ValueSetConceptAccumulator myAccumulator;
	private TermValueSet myValueSet;
	@Mock
	private ITermValueSetConceptDesignationDao myValueSetDesignationDao;
	@Mock
	private ITermValueSetConceptDao myValueSetConceptDao;
	@Mock
	private EntityManager myEntityManager;

	@BeforeEach
	public void before() {
		myValueSet = new TermValueSet();
		myAccumulator = new ValueSetConceptAccumulator(myValueSet, myEntityManager, myValueSetConceptDao, myValueSetDesignationDao);
	}

	@Test
	public void testIncludeConcept() {
		for (int i = 0; i < 1000; i++) {
			myAccumulator.includeConcept("sys", "code", "display", null, null, null);
		}
		verify(myEntityManager, times(1000)).persist(any(TermValueSetConcept.class));
	}

	@Test
	public void testExcludeBlankConcept() {
		myAccumulator.excludeConcept("", "");
		verifyNoInteractions(myValueSetConceptDao);
	}

	@Test
	public void testAddMessage() {
		myAccumulator.addMessage("foo");
		verifyNoInteractions(myValueSetConceptDao);
	}

	@Test
	public void testExcludeConceptWithDesignations() {
		for (int i = 0; i <1000; i++) {

			TermValueSetConcept value = new TermValueSetConcept();
			value.setCode("code");
			value.getDesignations().add(new TermValueSetConceptDesignation().setValue("foo"));

			when(myValueSetConceptDao.findByTermValueSetIdSystemAndCode(any(), eq("sys"), eq("code"+i))).thenReturn(Optional.of(value));

			myAccumulator.excludeConcept("sys", "code"+i);
		}

	}

	@ParameterizedTest
	@ValueSource(booleans = {false, true})
	public void testPersistValueSetConcept_whenSupportLegacyLob(boolean theSupportLegacyLob){
		final String sourceConceptDirectParentPids = "1 2 3 4 5 6 7";
		ArgumentCaptor<TermValueSetConcept> captor = ArgumentCaptor.forClass(TermValueSetConcept.class);

		myAccumulator.setSupportLegacyLob(theSupportLegacyLob);
		myAccumulator.includeConcept("sys", "code", "display", null, sourceConceptDirectParentPids, null);

		verify(myEntityManager, times(1)).persist(captor.capture());

		TermValueSetConcept capturedTermValueSetConcept = captor.getValue();

		assertEquals(theSupportLegacyLob, capturedTermValueSetConcept.hasSourceConceptDirectParentPidsLob());
		assertEquals(sourceConceptDirectParentPids, capturedTermValueSetConcept.getSourceConceptDirectParentPids());

	}

}
