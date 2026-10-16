package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.LookupCodeRequest;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.FhirTerser;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UriType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// Created by Claude Opus 5.5
class JpaResourceDaoCodeSystemLookupTest {

	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private static final String SYSTEM = "http://example.org/cs";

	/**
	 * The version reaches each module of the chain as its own field, not packed into the system, so a module
	 * which sends the system on to a terminology server sends a url that server can resolve.
	 */
	@Test
	void doLookupCode_versionAsParameterOnCodingOrPackedIntoSystem_requestCarriesUrlAndVersionApart() {
		CapturingValidationSupport support = new CapturingValidationSupport(true);
		FhirTerser terser = ourCtx.newTerser();

		JpaResourceDaoCodeSystem.doLookupCode(
				ourCtx, terser, support, new CodeType("a"), new UriType(SYSTEM), new StringType("1.0.0"), null, null, null);
		JpaResourceDaoCodeSystem.doLookupCode(
				ourCtx, terser, support, null, null, null, new Coding(SYSTEM, "a", null).setVersion("2.0.0"), null, null);
		// an overload without a version parameter can only receive the version packed into the system
		JpaResourceDaoCodeSystem.doLookupCode(
				ourCtx, terser, support, new CodeType("a"), new UriType(SYSTEM + "|3.0.0"), null, null, null);
		IValidationSupport.LookupCodeResult notFound = JpaResourceDaoCodeSystem.doLookupCode(
				ourCtx,
				terser,
				new CapturingValidationSupport(false),
				new CodeType("a"),
				new UriType(SYSTEM),
				new StringType("4.0.0"),
				null,
				null,
				null);

		assertThat(support.myRequests).extracting(LookupCodeRequest::getSystem).containsOnly(SYSTEM);
		assertThat(support.myRequests)
				.extracting(LookupCodeRequest::getVersion)
				.containsExactly("1.0.0", "2.0.0", "3.0.0");
		assertThat(support.mySupportedChecks)
				.containsExactly(SYSTEM + "|1.0.0", SYSTEM + "|2.0.0", SYSTEM + "|3.0.0");
		assertThat(notFound.getSearchedForSystem()).isEqualTo(SYSTEM + "|4.0.0");
	}

	@Test
	void doLookupCode_codingVersionDiffersFromVersionParameter_throws() {
		CapturingValidationSupport support = new CapturingValidationSupport(true);
		Coding coding = new Coding(SYSTEM, "a", null).setVersion("2.0.0");

		assertThatThrownBy(() -> JpaResourceDaoCodeSystem.doLookupCode(
						ourCtx, ourCtx.newTerser(), support, null, null, new StringType("1.0.0"), coding, null, null))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageContaining("HAPI-2952");
		assertThat(support.myRequests).isEmpty();
	}

	/**
	 * The system and version of a lookup can arrive as separate parameters, packed together as
	 * <code>url|version</code>, on a coding, or in more than one of these places at once.
	 */
	@ParameterizedTest(name = "{0}")
	@CsvSource(
			delimiter = ';',
			textBlock =
					"""
		# case                             ; system                  ; version ; coding system           ; coding version ; expected version
		system alone                       ; http://example.org/cs   ;         ;                         ;                ;
		blank version parameter            ; http://example.org/cs   ; ''      ;                         ;                ;
		version parameter                  ; http://example.org/cs   ; 1       ;                         ;                ; 1
		version packed into system         ; http://example.org/cs|1 ;         ;                         ;                ; 1
		empty version packed into system   ; http://example.org/cs|  ;         ;                         ;                ;
		packed and parameter agree         ; http://example.org/cs|1 ; 1       ;                         ;                ; 1
		coding alone                       ;                         ;         ; http://example.org/cs   ;                ;
		coding with version                ;                         ;         ; http://example.org/cs   ; 1              ; 1
		coding and version parameter       ;                         ; 1       ; http://example.org/cs   ;                ; 1
		coding version and parameter agree ;                         ; 1       ; http://example.org/cs   ; 1              ; 1
		version packed into coding system  ;                         ;         ; http://example.org/cs|1 ;                ; 1
		""")
	void doLookupCode_systemAndVersionSource_requestCarriesUrlAndExpectedVersion(
			String theCase,
			String theSystem,
			String theVersion,
			String theCodingSystem,
			String theCodingVersion,
			String theExpectedVersion) {
		CapturingValidationSupport support = new CapturingValidationSupport(true);
		Coding coding = toCoding(theCodingSystem, theCodingVersion);

		JpaResourceDaoCodeSystem.doLookupCode(
				ourCtx,
				ourCtx.newTerser(),
				support,
				coding == null ? new CodeType("a") : null,
				theSystem == null ? null : new UriType(theSystem),
				theVersion == null ? null : new StringType(theVersion),
				coding,
				null,
				null);

		assertThat(support.myRequests).singleElement().satisfies(request -> {
			assertThat(request.getSystem()).isEqualTo(SYSTEM);
			assertThat(request.getVersion()).isEqualTo(theExpectedVersion);
		});
	}

	@ParameterizedTest(name = "{0}")
	@CsvSource(
			delimiter = ';',
			textBlock =
					"""
		# case                                       ; system                  ; version ; coding system           ; coding version
		packed into system and parameter             ; http://example.org/cs|1 ; 2       ;                         ;
		coding and parameter                         ;                         ; 2       ; http://example.org/cs   ; 1
		packed into coding system and coding version ;                         ;         ; http://example.org/cs|1 ; 2
		packed into coding system and parameter      ;                         ; 2       ; http://example.org/cs|1 ;
		""")
	void doLookupCode_versionsInTwoPlacesDiffer_throws(
			String theCase, String theSystem, String theVersion, String theCodingSystem, String theCodingVersion) {
		CapturingValidationSupport support = new CapturingValidationSupport(true);
		Coding coding = toCoding(theCodingSystem, theCodingVersion);

		assertThatThrownBy(() -> JpaResourceDaoCodeSystem.doLookupCode(
						ourCtx,
						ourCtx.newTerser(),
						support,
						coding == null ? new CodeType("a") : null,
						theSystem == null ? null : new UriType(theSystem),
						theVersion == null ? null : new StringType(theVersion),
						coding,
						null,
						null))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageContaining("HAPI-2952");
		assertThat(support.myRequests).isEmpty();
	}

	private static Coding toCoding(String theSystem, String theVersion) {
		return theSystem == null ? null : new Coding(theSystem, "a", null).setVersion(theVersion);
	}

	private static class CapturingValidationSupport implements IValidationSupport {
		private final boolean mySupported;
		private final List<LookupCodeRequest> myRequests = new ArrayList<>();
		private final List<String> mySupportedChecks = new ArrayList<>();

		CapturingValidationSupport(boolean theSupported) {
			mySupported = theSupported;
		}

		@Override
		public FhirContext getFhirContext() {
			return ourCtx;
		}

		@Override
		public boolean isCodeSystemSupported(
				ValidationSupportContext theValidationSupportContext, String theSystem, String theVersion) {
			mySupportedChecks.add(theSystem + "|" + theVersion);
			return mySupported;
		}

		@Override
		public LookupCodeResult lookupCode(
				ValidationSupportContext theValidationSupportContext, @Nonnull LookupCodeRequest theLookupCodeRequest) {
			myRequests.add(theLookupCodeRequest);
			LookupCodeResult result = new LookupCodeResult();
			result.setFound(true);
			return result;
		}
	}
}
