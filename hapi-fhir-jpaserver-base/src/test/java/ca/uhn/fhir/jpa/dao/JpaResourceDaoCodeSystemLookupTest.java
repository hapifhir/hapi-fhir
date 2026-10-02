package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.LookupCodeRequest;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.util.FhirTerser;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UriType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
