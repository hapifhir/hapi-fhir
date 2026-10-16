package org.hl7.fhir.r4.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.LookupCodeRequest;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.PrePopulatedValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A ValueSet stored locally that lists codes from a CodeSystem only the remote terminology server knows. The chain
 * treats the CodeSystem as known only when the remote's {@code CodeSystem} search returns it, even though a server can
 * answer {@code $lookup} for a system it does not expose as a resource.
 */
// Created by Claude Opus 5.5
class RemoteOnlyCodeSystemValueSetR4Test {
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private static final String CS_URL = "http://example.org/cs/remote-only";
	private static final String VS_URL = "http://example.org/vs/local-over-remote";

	private static final RemoteCodeSystemProvider ourCodeSystemProvider = new RemoteCodeSystemProvider();

	@RegisterExtension
	static final RestfulServerExtension ourRemote = new RestfulServerExtension(ourCtx)
			.registerProvider(ourCodeSystemProvider)
			.registerProvider(new RemoteValueSetProvider());

	private ValidationSupportChain myChain;

	@BeforeEach
	void before() {
		// A new chain per test, so lookups cached by one test do not answer the next
		myChain = newChainWithLocalValueSet();
	}

	@AfterEach
	void after() {
		ourCodeSystemProvider.myExposeCodeSystemResource = false;
		ourCodeSystemProvider.myLookupCount = 0;
	}

	@Test
	void validateCode_codeSystemOnTheRemote_acceptsAListedCodeTheRemoteKnows() {
		// Setup
		ourCodeSystemProvider.myExposeCodeSystemResource = true;

		// Test
		IValidationSupport.CodeValidationResult outcome = myChain.validateCode(
				new ValidationSupportContext(myChain), new ConceptValidationOptions(), CS_URL, "A", null, VS_URL);

		// Verify
		assertThat(outcome).isNotNull();
		assertThat(outcome.getMessage()).isNull();
		assertThat(outcome.isOk()).isTrue();
	}

	@Test
	void validateCode_codeSystemOnTheRemote_rejectsAListedCodeTheRemoteDoesNotKnow() {
		// Setup
		ourCodeSystemProvider.myExposeCodeSystemResource = true;

		// Test
		IValidationSupport.CodeValidationResult outcome = myChain.validateCode(
				new ValidationSupportContext(myChain), new ConceptValidationOptions(), CS_URL, "B", null, VS_URL);

		// Verify
		assertThat(outcome).isNotNull();
		assertThat(outcome.isOk()).isFalse();
		assertThat(outcome.getUnknownSystems()).isEmpty();
	}

	/**
	 * A CodeSystem the remote does not return from its search is unknown, like any other: the listed code is rejected
	 * and the CodeSystem named, without asking the remote's {@code $lookup}.
	 */
	@Test
	void validateCode_codeSystemTheRemoteDoesNotExpose_rejectsTheListedCodeNamingTheCodeSystem() {
		// Test
		IValidationSupport.CodeValidationResult outcome = myChain.validateCode(
				new ValidationSupportContext(myChain), new ConceptValidationOptions(), CS_URL, "A", null, VS_URL);

		// Verify
		assertThat(outcome).isNotNull();
		assertThat(outcome.isOk()).isFalse();
		assertThat(outcome.getUnknownSystems()).containsExactly(CS_URL);
		assertThat(ourCodeSystemProvider.myLookupCount).isZero();
	}

	@Test
	void expandValueSet_codeSystemTheRemoteDoesNotExpose_failsWithoutAskingTheRemote() {
		// Test
		IValidationSupport.ValueSetExpansionOutcome outcome =
				myChain.expandValueSet(new ValidationSupportContext(myChain), null, myChain.fetchValueSet(VS_URL));

		// Verify
		assertThat(outcome.getError()).contains(CS_URL);
		assertThat(ourCodeSystemProvider.myLookupCount).isZero();
	}

	/**
	 * A lookup goes only to the modules that claim the code system, so it never reaches a remote that does not list it.
	 */
	@Test
	void lookupCode_systemTheRemoteDoesNotReturnFromItsSearch_isNotSentToTheRemote() {
		// Test
		IValidationSupport.LookupCodeResult outcome =
				myChain.lookupCode(new ValidationSupportContext(myChain), new LookupCodeRequest(CS_URL, "A"));

		// Verify
		assertThat(outcome).isNull();
		assertThat(ourCodeSystemProvider.myLookupCount).isZero();
	}

	private static ValidationSupportChain newChainWithLocalValueSet() {
		ValueSet vs = new ValueSet();
		vs.setUrl(VS_URL);
		vs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		ValueSet.ConceptSetComponent include = vs.getCompose().addInclude().setSystem(CS_URL);
		include.addConcept().setCode("A");
		include.addConcept().setCode("B");
		PrePopulatedValidationSupport prePopulated = new PrePopulatedValidationSupport(ourCtx);
		prePopulated.addValueSet(vs);

		return new ValidationSupportChain(
				new RemoteTerminologyServiceValidationSupport(ourCtx, ourRemote.getBaseUrl()),
				new DefaultProfileValidationSupport(ourCtx),
				prePopulated,
				new InMemoryTerminologyServerValidationSupport(ourCtx),
				new CommonCodeSystemsTerminologyService(ourCtx));
	}

	/**
	 * A remote that knows code {@code A} (not {@code B}) of {@link #CS_URL}, and optionally exposes the CodeSystem as a resource.
	 */
	public static class RemoteCodeSystemProvider implements IResourceProvider {
		private boolean myExposeCodeSystemResource;
		private int myLookupCount;

		@Override
		public Class<CodeSystem> getResourceType() {
			return CodeSystem.class;
		}

		@Search
		public List<CodeSystem> search(
				@OptionalParam(name = CodeSystem.SP_URL) UriParam theUrl,
				@OptionalParam(name = CodeSystem.SP_VERSION) StringParam theVersion) {
			if (!myExposeCodeSystemResource || theUrl == null || !CS_URL.equals(theUrl.getValue())) {
				return List.of();
			}
			CodeSystem cs = new CodeSystem();
			cs.setId("remote-only");
			cs.setUrl(CS_URL);
			cs.setStatus(Enumerations.PublicationStatus.ACTIVE);
			cs.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
			return List.of(cs);
		}

		@Operation(name = "$lookup", idempotent = true)
		public Parameters lookup(
				@OperationParam(name = "code") CodeType theCode,
				@OperationParam(name = "system") UriType theSystem,
				@OperationParam(name = "version") StringType theVersion) {
			myLookupCount++;
			if (!CS_URL.equals(theSystem.getValue()) || !"A".equals(theCode.getValue())) {
				throw new ResourceNotFoundException("Unknown code");
			}
			Parameters retVal = new Parameters();
			retVal.addParameter("name", "Remote only");
			retVal.addParameter("display", "Code A");
			return retVal;
		}

		@Operation(name = "$validate-code", idempotent = true)
		public Parameters validateCode(
				@OperationParam(name = "url") UriType theUrl,
				@OperationParam(name = "code") CodeType theCode,
				@OperationParam(name = "version") StringType theVersion,
				@OperationParam(name = "display") StringType theDisplay) {
			boolean known = CS_URL.equals(theUrl.getValue()) && "A".equals(theCode.getValue());
			Parameters retVal = new Parameters();
			retVal.addParameter("result", known);
			if (known) {
				retVal.addParameter("display", "Code A");
			} else {
				retVal.addParameter("message", "Unknown code");
			}
			return retVal;
		}
	}

	/**
	 * The remote holds no ValueSets, so the local ValueSet is expanded locally.
	 */
	public static class RemoteValueSetProvider implements IResourceProvider {
		@Override
		public Class<ValueSet> getResourceType() {
			return ValueSet.class;
		}

		@Search
		public List<ValueSet> search(
				@OptionalParam(name = ValueSet.SP_URL) UriParam theUrl,
				@OptionalParam(name = ValueSet.SP_VERSION) StringParam theVersion) {
			return List.of();
		}
	}
}
