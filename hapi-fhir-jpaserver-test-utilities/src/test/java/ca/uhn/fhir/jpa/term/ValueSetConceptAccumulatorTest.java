package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.dao.data.ITermValueSetConceptDao;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetConceptDesignationDao;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetDao;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.entity.TermValueSetConcept;
import ca.uhn.fhir.jpa.entity.TermValueSetConceptDesignation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
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
	private ITermValueSetDao myValueSetDao;
	@Mock
	private ITermValueSetConceptDesignationDao myValueSetDesignationDao;
	@Mock
	private ITermValueSetConceptDao myValueSetConceptDao;

	@BeforeEach
	public void before() {
		myValueSet = new TermValueSet();
		myAccumulator = new ValueSetConceptAccumulator(myValueSet, myValueSetDao, myValueSetConceptDao, myValueSetDesignationDao);
	}

	@Test
	public void testIncludeConcept() {
		for (int i = 0; i < 1000; i++) {
			myAccumulator.includeConcept("sys", "code", "display", null, null, null);
		}
		verify(myValueSetConceptDao, times(1000)).save(any());
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

		verify(myValueSetConceptDao, times(1)).save(captor.capture());

		TermValueSetConcept capturedTermValueSetConcept = captor.getValue();

		assertThat(capturedTermValueSetConcept.hasSourceConceptDirectParentPidsLob(), equalTo(theSupportLegacyLob));
		assertThat(capturedTermValueSetConcept.getSourceConceptDirectParentPids(), equalTo(sourceConceptDirectParentPids));

	}

}
