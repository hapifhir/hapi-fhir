package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.MissingCodeSystemPolicy;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDao;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.context.support.MissingCodeSystemPolicyEvaluator;
import ca.uhn.fhir.i18n.HapiLocalizer;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Created by Claude Opus 5.5
class TermValueSetInstalledVersionSvcTest {

	private static final String SYSTEM = "http://example.org/cs";
	private static final String NAMED_VERSION = "1";
	private static final String INSTALLED_VERSION = "2";
	private static final long INSTALLED_VERSION_PID = 22L;

	private final JpaStorageSettings myStorageSettings = new JpaStorageSettings();
	private final ITermCodeSystemDao myCodeSystemDao = mock(ITermCodeSystemDao.class);
	private final ITermConceptDao myConceptDao = mock(ITermConceptDao.class);
	private final IValidationSupport myValidationSupport = mock(IValidationSupport.class);
	private final IValueSetConceptAccumulator myAccumulator = mock(IValueSetConceptAccumulator.class);
	private final MissingCodeSystemPolicyEvaluator myPolicyEvaluator = new MissingCodeSystemPolicyEvaluator(
				new HapiLocalizer(), myStorageSettings::getMissingCodeSystemPolicy);
	private final TermValueSetInstalledVersionSvc mySvc =
			new TermValueSetInstalledVersionSvc(myPolicyEvaluator, myCodeSystemDao, myConceptDao);

	@Test
	void applyInstalledVersionPolicy_reject_returnsIncludeUnchanged() {
		// Given
		myStorageSettings.setMissingCodeSystemPolicy(MissingCodeSystemPolicy.REJECT);
		givenInstalledVersion();
		ValueSet.ConceptSetComponent include = includeOfNamedVersion();

		// When
		Optional<ValueSet.ConceptSetComponent> result = callApplyInstalledVersionPolicy(include);

		// Then
		assertThat(result).containsSame(include);
		verify(myAccumulator, never()).addMessage(any());
	}

	@ParameterizedTest
	@EnumSource(
			value = MissingCodeSystemPolicy.class,
			names = {"USE_INSTALLED_VERSION", "ACCEPT_LISTED_CODES"})
	void applyInstalledVersionPolicy_wholeCodeSystemInclude_returnsCopyNamingInstalledVersion(
			MissingCodeSystemPolicy thePolicy) {
		// Given
		myStorageSettings.setMissingCodeSystemPolicy(thePolicy);
		givenInstalledVersion();
		ValueSet.ConceptSetComponent include = includeOfNamedVersion();

		// When
		Optional<ValueSet.ConceptSetComponent> result = callApplyInstalledVersionPolicy(include);

		// Then
		assertThat(result).isPresent();
		assertThat(result.get()).isNotSameAs(include);
		assertThat(result.get().getSystem()).isEqualTo(SYSTEM);
		assertThat(result.get().getVersion()).isEqualTo(INSTALLED_VERSION);
		assertThat(include.getVersion()).isEqualTo(NAMED_VERSION);
		verify(myAccumulator)
				.addMessage(myPolicyEvaluator.createVersionReplacedMessage(SYSTEM, NAMED_VERSION, INSTALLED_VERSION));
	}

	@Test
	void applyInstalledVersionPolicy_versionSupportedByChain_returnsIncludeUnchanged() {
		// Given
		myStorageSettings.setMissingCodeSystemPolicy(MissingCodeSystemPolicy.USE_INSTALLED_VERSION);
		givenInstalledVersion();
		when(myValidationSupport.isCodeSystemSupported(any(), eq(SYSTEM), eq(NAMED_VERSION)))
				.thenReturn(true);
		ValueSet.ConceptSetComponent include = includeOfNamedVersion();

		// When
		Optional<ValueSet.ConceptSetComponent> result = callApplyInstalledVersionPolicy(include);

		// Then
		assertThat(result).containsSame(include);
	}

	@Test
	void applyInstalledVersionPolicy_noInstalledCodeSystem_returnsIncludeUnchanged() {
		// Given
		myStorageSettings.setMissingCodeSystemPolicy(MissingCodeSystemPolicy.USE_INSTALLED_VERSION);
		ValueSet.ConceptSetComponent include = includeOfNamedVersion();

		// When
		Optional<ValueSet.ConceptSetComponent> result = callApplyInstalledVersionPolicy(include);

		// Then
		assertThat(result).containsSame(include);
		verify(myAccumulator, never()).addMessage(any());
	}

	@Test
	void applyInstalledVersionPolicy_namedVersionIsInstalled_returnsIncludeUnchanged() {
		// Given
		myStorageSettings.setMissingCodeSystemPolicy(MissingCodeSystemPolicy.USE_INSTALLED_VERSION);
		givenInstalledVersion();
		ValueSet.ConceptSetComponent include = includeOfNamedVersion().setVersion(INSTALLED_VERSION);

		// When
		Optional<ValueSet.ConceptSetComponent> result = callApplyInstalledVersionPolicy(include);

		// Then
		assertThat(result).containsSame(include);
		verify(myAccumulator, never()).addMessage(any());
	}

	@Test
	void applyInstalledVersionPolicy_listedCodes_keepsOnlyCodesOfInstalledVersion() {
		// Given
		myStorageSettings.setMissingCodeSystemPolicy(MissingCodeSystemPolicy.USE_INSTALLED_VERSION);
		givenInstalledVersion();
		when(myConceptDao.findByCodeSystemAndCodeList(eq(INSTALLED_VERSION_PID), any()))
				.thenReturn(List.of(new TermConcept().setCode("A")));
		ValueSet.ConceptSetComponent include = includeOfNamedVersion();
		include.addConcept().setCode("A");
		include.addConcept().setCode("Z");

		// When
		Optional<ValueSet.ConceptSetComponent> result = callApplyInstalledVersionPolicy(include);

		// Then
		assertThat(result).isPresent();
		assertThat(result.get().getConcept())
				.extracting(ValueSet.ConceptReferenceComponent::getCode)
				.containsExactly("A");
		assertThat(include.getConcept())
				.extracting(ValueSet.ConceptReferenceComponent::getCode)
				.containsExactly("A", "Z");
	}

	@Test
	void applyInstalledVersionPolicy_noListedCodeInInstalledVersion_returnsEmpty() {
		// Given
		myStorageSettings.setMissingCodeSystemPolicy(MissingCodeSystemPolicy.USE_INSTALLED_VERSION);
		givenInstalledVersion();
		when(myConceptDao.findByCodeSystemAndCodeList(eq(INSTALLED_VERSION_PID), any()))
				.thenReturn(List.of());
		ValueSet.ConceptSetComponent include = includeOfNamedVersion();
		include.addConcept().setCode("Z");

		// When
		Optional<ValueSet.ConceptSetComponent> result = callApplyInstalledVersionPolicy(include);

		// Then
		assertThat(result).isEmpty();
	}

	private Optional<ValueSet.ConceptSetComponent> callApplyInstalledVersionPolicy(
			ValueSet.ConceptSetComponent theInclude) {
		return mySvc.applyInstalledVersionPolicy(myValidationSupport, theInclude, myAccumulator);
	}

	private void givenInstalledVersion() {
		TermCodeSystem codeSystem = mock(TermCodeSystem.class);
		TermCodeSystemVersion installedVersion = new TermCodeSystemVersion()
				.setId(INSTALLED_VERSION_PID)
				.setCodeSystemVersionId(INSTALLED_VERSION);
		when(codeSystem.getCurrentVersion()).thenReturn(installedVersion);
		when(myCodeSystemDao.findByCodeSystemUri(SYSTEM)).thenReturn(codeSystem);
	}

	private static ValueSet.ConceptSetComponent includeOfNamedVersion() {
		return new ValueSet.ConceptSetComponent().setSystem(SYSTEM).setVersion(NAMED_VERSION);
	}
}
