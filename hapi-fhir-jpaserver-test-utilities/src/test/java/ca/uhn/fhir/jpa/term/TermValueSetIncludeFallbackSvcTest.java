package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.MissingCodeSystemPolicy;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDao;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportUtils;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Created by Claude Opus 5.5
class TermValueSetIncludeFallbackSvcTest {

	private static final String SYSTEM = "http://example.org/cs";
	private static final String NAMED_VERSION = "1";
	private static final String INSTALLED_VERSION = "2";
	private static final long INSTALLED_VERSION_PID = 22L;

	private final JpaStorageSettings myStorageSettings = new JpaStorageSettings();
	private final ITermCodeSystemDao myCodeSystemDao = mock(ITermCodeSystemDao.class);
	private final ITermConceptDao myConceptDao = mock(ITermConceptDao.class);
	private final IValidationSupport myValidationSupport = mock(IValidationSupport.class);
	private final IValueSetConceptAccumulator myAccumulator = mock(IValueSetConceptAccumulator.class);
	private final FhirContext myFhirContext = FhirContext.forR4Cached();
	private final TermValueSetIncludeFallbackSvc mySvc =
			new TermValueSetIncludeFallbackSvc(myFhirContext, myStorageSettings, myCodeSystemDao, myConceptDao);

	@Nested
	class UseInstalledCodeSystemVersionIfNamedOneIsMissing {

		@Test
		void useInstalledCodeSystemVersionIfNamedOneIsMissing_reject_returnsIncludeUnchanged() {
			// Given
			myStorageSettings.setMissingCodeSystemPolicy(MissingCodeSystemPolicy.REJECT);
			givenInstalledVersion();
			ValueSet.ConceptSetComponent include = includeOfNamedVersion();

			// When
			ValueSet.ConceptSetComponent result = callUseInstalledVersion(include);

			// Then
			assertThat(result).isSameAs(include);
			verify(myAccumulator, never()).addMessage(any());
		}

		@ParameterizedTest
		@EnumSource(
				value = MissingCodeSystemPolicy.class,
				names = {"USE_INSTALLED_VERSION", "ACCEPT_LISTED_CODES"})
		void useInstalledCodeSystemVersionIfNamedOneIsMissing_wholeCodeSystemInclude_returnsCopyNamingInstalledVersion(
				MissingCodeSystemPolicy thePolicy) {
			// Given
			myStorageSettings.setMissingCodeSystemPolicy(thePolicy);
			givenInstalledVersion();
			ValueSet.ConceptSetComponent include = includeOfNamedVersion();

			// When
			ValueSet.ConceptSetComponent result = callUseInstalledVersion(include);

			// Then
			assertThat(result).isNotSameAs(include);
			assertThat(result.getSystem()).isEqualTo(SYSTEM);
			assertThat(result.getVersion()).isEqualTo(INSTALLED_VERSION);
			assertThat(include.getVersion()).isEqualTo(NAMED_VERSION);
			verify(myAccumulator)
					.addMessage(ValidationSupportUtils.createCodeSystemVersionReplacedMessage(
							myFhirContext,
							SYSTEM, NAMED_VERSION, INSTALLED_VERSION));
		}

		@Test
		void useInstalledCodeSystemVersionIfNamedOneIsMissing_versionSupportedByChain_returnsIncludeUnchanged() {
			// Given
			myStorageSettings.setMissingCodeSystemPolicy(MissingCodeSystemPolicy.USE_INSTALLED_VERSION);
			givenInstalledVersion();
			when(myValidationSupport.isCodeSystemSupported(any(), eq(SYSTEM), eq(NAMED_VERSION)))
					.thenReturn(true);
			ValueSet.ConceptSetComponent include = includeOfNamedVersion();

			// When
			ValueSet.ConceptSetComponent result = callUseInstalledVersion(include);

			// Then
			assertThat(result).isSameAs(include);
		}

		@Test
		void useInstalledCodeSystemVersionIfNamedOneIsMissing_noInstalledCodeSystem_returnsIncludeUnchanged() {
			// Given
			myStorageSettings.setMissingCodeSystemPolicy(MissingCodeSystemPolicy.USE_INSTALLED_VERSION);
			ValueSet.ConceptSetComponent include = includeOfNamedVersion();

			// When
			ValueSet.ConceptSetComponent result = callUseInstalledVersion(include);

			// Then
			assertThat(result).isSameAs(include);
			verify(myAccumulator, never()).addMessage(any());
		}

		@Test
		void useInstalledCodeSystemVersionIfNamedOneIsMissing_namedVersionIsInstalled_returnsIncludeUnchanged() {
			// Given
			myStorageSettings.setMissingCodeSystemPolicy(MissingCodeSystemPolicy.USE_INSTALLED_VERSION);
			givenInstalledVersion();
			ValueSet.ConceptSetComponent include = includeOfNamedVersion().setVersion(INSTALLED_VERSION);

			// When
			ValueSet.ConceptSetComponent result = callUseInstalledVersion(include);

			// Then
			assertThat(result).isSameAs(include);
		}

		@Test
		void useInstalledCodeSystemVersionIfNamedOneIsMissing_listedCodes_keepsOnlyCodesOfInstalledVersion() {
			// Given
			myStorageSettings.setMissingCodeSystemPolicy(MissingCodeSystemPolicy.USE_INSTALLED_VERSION);
			givenInstalledVersion();
			when(myConceptDao.findByCodeSystemAndCodeList(eq(INSTALLED_VERSION_PID), any()))
					.thenReturn(List.of(new TermConcept().setCode("A")));
			ValueSet.ConceptSetComponent include = includeOfNamedVersion();
			include.addConcept().setCode("A");
			include.addConcept().setCode("Z");

			// When
			ValueSet.ConceptSetComponent result = callUseInstalledVersion(include);

			// Then
			assertThat(result.getConcept())
					.extracting(ValueSet.ConceptReferenceComponent::getCode)
					.containsExactly("A");
			assertThat(include.getConcept())
					.extracting(ValueSet.ConceptReferenceComponent::getCode)
					.containsExactly("A", "Z");
		}

		@Test
		void useInstalledCodeSystemVersionIfNamedOneIsMissing_noListedCodeInInstalledVersion_returnsNull() {
			// Given
			myStorageSettings.setMissingCodeSystemPolicy(MissingCodeSystemPolicy.USE_INSTALLED_VERSION);
			givenInstalledVersion();
			when(myConceptDao.findByCodeSystemAndCodeList(eq(INSTALLED_VERSION_PID), any()))
					.thenReturn(List.of());
			ValueSet.ConceptSetComponent include = includeOfNamedVersion();
			include.addConcept().setCode("Z");

			// When
			ValueSet.ConceptSetComponent result = callUseInstalledVersion(include);

			// Then
			assertThat(result).isNull();
		}

		private ValueSet.ConceptSetComponent callUseInstalledVersion(ValueSet.ConceptSetComponent theInclude) {
			return mySvc.useInstalledCodeSystemVersionIfNamedOneIsMissing(
					myValidationSupport, theInclude, myAccumulator);
		}
	}

	@Nested
	class IsListedCodesOfEmptyCodeSystemVersionAccepted {

		@Test
		void isListedCodesOfEmptyCodeSystemVersionAccepted_acceptListedCodesAndNoConcepts_returnsTrue() {
			// Given
			myStorageSettings.setMissingCodeSystemPolicy(MissingCodeSystemPolicy.ACCEPT_LISTED_CODES);
			when(myConceptDao.findPidsByCodeSystemVersionPid(any(), anyLong())).thenReturn(List.of());

			// When
			boolean result = mySvc.isListedCodesOfEmptyCodeSystemVersionAccepted(
					includeListingCodeA(), installedVersion());

			// Then
			assertThat(result).isTrue();
		}

		@Test
		void isListedCodesOfEmptyCodeSystemVersionAccepted_versionHasConcepts_returnsFalse() {
			// Given
			myStorageSettings.setMissingCodeSystemPolicy(MissingCodeSystemPolicy.ACCEPT_LISTED_CODES);
			when(myConceptDao.findPidsByCodeSystemVersionPid(any(), anyLong()))
					.thenReturn(List.of(new TermConcept.TermConceptPk(1L, null)));

			// When
			boolean result = mySvc.isListedCodesOfEmptyCodeSystemVersionAccepted(
					includeListingCodeA(), installedVersion());

			// Then
			assertThat(result).isFalse();
		}

		@Test
		void isListedCodesOfEmptyCodeSystemVersionAccepted_noListedCodes_returnsFalse() {
			// Given
			myStorageSettings.setMissingCodeSystemPolicy(MissingCodeSystemPolicy.ACCEPT_LISTED_CODES);

			// When
			boolean result = mySvc.isListedCodesOfEmptyCodeSystemVersionAccepted(
					new ValueSet.ConceptSetComponent().setSystem(SYSTEM), installedVersion());

			// Then
			assertThat(result).isFalse();
		}

		@ParameterizedTest
		@EnumSource(
				value = MissingCodeSystemPolicy.class,
				names = {"REJECT", "USE_INSTALLED_VERSION"})
		void isListedCodesOfEmptyCodeSystemVersionAccepted_otherPolicy_returnsFalse(MissingCodeSystemPolicy thePolicy) {
			// Given
			myStorageSettings.setMissingCodeSystemPolicy(thePolicy);

			// When
			boolean result = mySvc.isListedCodesOfEmptyCodeSystemVersionAccepted(
					includeListingCodeA(), installedVersion());

			// Then
			assertThat(result).isFalse();
		}

		private ValueSet.ConceptSetComponent includeListingCodeA() {
			ValueSet.ConceptSetComponent retVal = new ValueSet.ConceptSetComponent().setSystem(SYSTEM);
			retVal.addConcept().setCode("A");
			return retVal;
		}
	}

	@Nested
	class HasNoConcepts {

		@Test
		void hasNoConcepts_versionWithoutConcepts_returnsTrue() {
			// Given
			when(myConceptDao.findPidsByCodeSystemVersionPid(any(), eq(INSTALLED_VERSION_PID)))
					.thenReturn(List.of());

			// When
			boolean result = mySvc.hasNoConcepts(installedVersion());

			// Then
			assertThat(result).isTrue();
		}

		@Test
		void hasNoConcepts_versionWithConcepts_returnsFalse() {
			// Given
			when(myConceptDao.findPidsByCodeSystemVersionPid(any(), eq(INSTALLED_VERSION_PID)))
					.thenReturn(List.of(new TermConcept.TermConceptPk(1L, null)));

			// When
			boolean result = mySvc.hasNoConcepts(installedVersion());

			// Then
			assertThat(result).isFalse();
		}
	}

	private void givenInstalledVersion() {
		TermCodeSystem codeSystem = mock(TermCodeSystem.class);
		TermCodeSystemVersion installedVersion = installedVersion();
		when(codeSystem.getCurrentVersion()).thenReturn(installedVersion);
		when(myCodeSystemDao.findByCodeSystemUri(SYSTEM)).thenReturn(codeSystem);
	}

	private static TermCodeSystemVersion installedVersion() {
		return new TermCodeSystemVersion().setId(INSTALLED_VERSION_PID).setCodeSystemVersionId(INSTALLED_VERSION);
	}

	private static ValueSet.ConceptSetComponent includeOfNamedVersion() {
		return new ValueSet.ConceptSetComponent().setSystem(SYSTEM).setVersion(NAMED_VERSION);
	}
}
