package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.term.TerminologyTestHelper;
import ca.uhn.fhir.jpa.term.ZipCollectionBuilder;
import ca.uhn.fhir.jpa.validation.JpaValidationSupportChain;
import ca.uhn.fhir.packages.NpmPackageFactory;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.PolicyEnum;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;
import ca.uhn.fhir.util.ParametersUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * ValueSets installed from an implementation guide package over a CodeSystem the server cannot use (#8415): the
 * package ships the CodeSystem as a {@code content: not-present} stub, or the ValueSet names a CodeSystem version the
 * server does not have. Pre-expansion is enabled, as by default.
 */
// Created by Claude Opus 5.5
class PackageInstallerSvcUnknownCodeSystemR4Test extends BaseResourceProviderR4Test {
	private static final String VS_VERSION = "1.0.0";
	private static final String OBSERVATION_A = "Observation/coded-a";
	private static final String OBSERVATION_B = "Observation/coded-b";

	@Autowired
	private TerminologyTestHelper myTerminologyTestHelper;

	@Autowired
	private JpaValidationSupportChain myJpaValidationSupportChain;

	@AfterEach
	void afterResetSettingsAndRules() {
		myInterceptorRegistry.unregisterInterceptorsIf(AuthorizationInterceptor.class::isInstance);
		myStorageSettings.setUseCurrentVersionForUnknownCodeSystemVersion(
				new JpaStorageSettings().isUseCurrentVersionForUnknownCodeSystemVersion());
	}

	/**
	 * The package ships a {@code not-present} stub for the CodeSystem and a ValueSet listing codes {@code A} and
	 * {@code B} from it, as an IG does for a licensed CodeSystem. Unlike a CodeSystem the server has no record of,
	 * the stub is stored, so the server has a CodeSystem resource for the system but no concepts.
	 */
	@Nested
	class NotPresentStub {
		private static final String CS_URL = "http://example.org/CodeSystem/ig-stub";
		private static final String VS_URL = "http://example.org/ValueSet/ig-stub-listed";

		@BeforeEach
		void beforeInstallPackage() throws IOException {
			CodeSystem stub = new CodeSystem();
			stub.setId("ig-stub");
			stub.setUrl(CS_URL);
			stub.setStatus(Enumerations.PublicationStatus.ACTIVE);
			stub.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);

			install("example.ig.stub", stub, newListingValueSet("ig-stub-listed", VS_URL, CS_URL, null));

			createObservation(withId("coded-a"), withObservationCode(CS_URL, "A"));
			myBatch2JobHelper.awaitNoJobsRunning();
		}

		/**
		 * Positive control for the tests below: the package stored both the stub and the ValueSet
		 */
		@Test
		void install_stubAndValueSet_storesBoth() {
			List<IBaseResource> codeSystems = myCodeSystemDao
					.search(SearchParameterMap.newSynchronous(CodeSystem.SP_URL, new UriParam(CS_URL)), mySrd)
					.getAllResources();
			assertThat(codeSystems).hasSize(1);
			assertThat(((CodeSystem) codeSystems.get(0)).getContent())
					.isEqualTo(CodeSystem.CodeSystemContentMode.NOTPRESENT);
			runInTransaction(() -> assertThat(myTermValueSetDao.findTermValueSetByUrlAndVersion(VS_URL, VS_VERSION))
					.isPresent());
		}

		@Test
		void validateCode_codeListedFromStub_rejectsNamingTheSystem() {
			Parameters outcome = validateCode(VS_URL, CS_URL, "A");

			assertThat(((BooleanType) outcome.getParameterValue("result")).booleanValue()).isFalse();
			assertThat(ParametersUtil.getNamedParameterValuesAsString(
							myFhirContext, outcome, IValidationSupport.CodeValidationResult.CAUSED_BY_UNKNOWN_SYSTEM))
					.containsExactly(CS_URL);
		}

		/**
		 * Every rule shape denies a read of a resource coded with a code the ValueSet lists, as for a CodeSystem the
		 * server has no record of
		 */
		@ParameterizedTest
		@EnumSource(CodeRuleEnum.class)
		void read_codeListedFromStub_isDenied(CodeRuleEnum theRule) {
			registerRules(theRule.build(VS_URL));

			assertReadIsDenied(OBSERVATION_A);
		}

		/**
		 * The ValueSet was pre-expanded while its CodeSystem was the stub, so its stored expansion holds no codes.
		 * Storing the CodeSystem with its codes must not leave the ValueSet answered from that empty expansion.
		 */
		@Test
		void storeCodeSystem_afterStub_valueSetAnswersFromTheStoredCodes() {
			storeCodeSystemWithCodes();

			assertAnsweredFromTheStoredCodes();
		}

		/**
		 * A remote terminology server goes first in the validation chain. Finding the expansions to invalidate reads
		 * only the server's own ValueSets, so a remote server that is down does not fail the CodeSystem store, and the
		 * store's transaction does not wait on it.
		 */
		@Test
		void storeCodeSystem_afterStub_remoteTerminologyDown_valueSetAnswersFromTheStoredCodes() {
			IValidationSupport remoteTerminology = mock(IValidationSupport.class);
			when(remoteTerminology.getFhirContext()).thenReturn(myFhirContext);
			when(remoteTerminology.fetchValueSet(any(), any()))
					.thenThrow(new FhirClientConnectionException("Remote terminology server is down"));

			myJpaValidationSupportChain.addValidationSupport(0, remoteTerminology);
			try {
				storeCodeSystemWithCodes();
			} finally {
				myJpaValidationSupportChain.removeValidationSupport(remoteTerminology);
			}

			assertAnsweredFromTheStoredCodes();
		}

		/**
		 * As {@link #storeCodeSystem_afterStub_valueSetAnswersFromTheStoredCodes}, for the rules: answered from the
		 * empty expansion, a code the ValueSet lists would count as not in it, and the not-in rules would allow and the
		 * in rules would not deny a read they must decide the other way
		 */
		@ParameterizedTest
		@CsvSource({"ALLOW_IN, true", "ALLOW_NOT_IN, false", "DENY_IN, false", "DENY_NOT_IN, true"})
		void read_afterStubThenStoreCodeSystem_rulesDecideFromTheStoredCodes(
				CodeRuleEnum theRule, boolean theAllowed) {
			storeCodeSystemWithCodes();
			registerRules(theRule.build(VS_URL));

			if (theAllowed) {
				assertThat(myClient.read()
								.resource(Observation.class)
								.withId(OBSERVATION_A)
								.execute())
						.isNotNull();
			} else {
				assertReadIsDenied(OBSERVATION_A);
			}
		}

		private void storeCodeSystemWithCodes() {
			CodeSystem codeSystem = (CodeSystem) myCodeSystemDao
					.search(SearchParameterMap.newSynchronous(CodeSystem.SP_URL, new UriParam(CS_URL)), mySrd)
					.getAllResources()
					.get(0);
			codeSystem.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
			codeSystem.addConcept().setCode("A");
			codeSystem.addConcept().setCode("B");
			myCodeSystemDao.update(codeSystem, mySrd);
			myTerminologyDeferredStorageSvc.saveAllDeferred();
			myBatch2JobHelper.awaitNoJobsRunning();
		}

		/**
		 * As {@link #storeCodeSystem_afterStub_valueSetAnswersFromTheStoredCodes}, with the codes uploaded by
		 * {@code $upload-external-code-system}
		 */
		@Test
		void uploadTerminology_afterStub_valueSetAnswersFromTheUploadedCodes() throws IOException {
			ZipCollectionBuilder files = new ZipCollectionBuilder(true);
			files.addFileText("CODE,DISPLAY\nA,Code A\nB,Code B\n", TerminologyConstants.CUSTOM_CONCEPTS_FILE);
			myTerminologyTestHelper.startImportCustomJobAndWaitForCompletion(CS_URL, "1", files);
			myBatch2JobHelper.awaitNoJobsRunning();

			assertAnsweredFromTheStoredCodes();
		}

		private void assertAnsweredFromTheStoredCodes() {
			assertThat(((BooleanType) validateCode(VS_URL, CS_URL, "A").getParameterValue("result")).booleanValue())
					.isTrue();
			Bundle found = myClient.search()
					.byUrl("Observation?code:in=" + VS_URL)
					.returnBundle(Bundle.class)
					.execute();
			assertThat(found.getEntry())
					.extracting(entry -> entry.getResource().getIdElement().toUnqualifiedVersionless().getValue())
					.containsExactly(OBSERVATION_A);
		}
	}

	/**
	 * The ValueSet lists code {@code A} from a CodeSystem the server has and code {@code C} from a {@code not-present}
	 * stub, so its stored expansion holds {@code A} and none of the stub's codes.
	 */
	@Nested
	class RealCodeSystemAndNotPresentStub {
		private static final String REAL_CS_URL = "http://example.org/CodeSystem/ig-real";
		private static final String STUB_CS_URL = "http://example.org/CodeSystem/ig-mixed-stub";
		private static final String VS_URL = "http://example.org/ValueSet/ig-mixed";

		@BeforeEach
		void beforeInstallPackage() throws IOException {
			CodeSystem real = new CodeSystem();
			real.setUrl(REAL_CS_URL);
			real.setStatus(Enumerations.PublicationStatus.ACTIVE);
			real.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
			real.addConcept().setCode("A");
			myCodeSystemDao.create(real, mySrd);

			CodeSystem stub = new CodeSystem();
			stub.setId("ig-mixed-stub");
			stub.setUrl(STUB_CS_URL);
			stub.setStatus(Enumerations.PublicationStatus.ACTIVE);
			stub.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);

			ValueSet valueSet = new ValueSet();
			valueSet.setId("ig-mixed");
			valueSet.setUrl(VS_URL);
			valueSet.setVersion(VS_VERSION);
			valueSet.setStatus(Enumerations.PublicationStatus.ACTIVE);
			valueSet.getCompose().addInclude().setSystem(REAL_CS_URL).addConcept().setCode("A");
			valueSet.getCompose().addInclude().setSystem(STUB_CS_URL).addConcept().setCode("C");

			install("example.ig.mixed", stub, valueSet);
			createObservation(withId("coded-a"), withObservationCode(STUB_CS_URL, "C"));
			myBatch2JobHelper.awaitNoJobsRunning();
		}

		@Test
		void storeCodeSystem_expansionHoldsOtherCodes_valueSetAnswersFromTheStoredCodes() {
			runInTransaction(() -> {
				TermValueSet stored = myTermValueSetDao
						.findTermValueSetByUrlAndVersion(VS_URL, VS_VERSION)
						.orElseThrow();
				assertThat(stored.getExpansionStatus()).isEqualTo(TermValueSetPreExpansionStatusEnum.EXPANDED);
				assertThat(stored.getTotalConcepts()).isEqualTo(1L);
			});

			CodeSystem codeSystem = (CodeSystem) myCodeSystemDao
					.search(SearchParameterMap.newSynchronous(CodeSystem.SP_URL, new UriParam(STUB_CS_URL)), mySrd)
					.getAllResources()
					.get(0);
			codeSystem.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
			codeSystem.addConcept().setCode("C");
			myCodeSystemDao.update(codeSystem, mySrd);
			myTerminologyDeferredStorageSvc.saveAllDeferred();
			myBatch2JobHelper.awaitNoJobsRunning();

			assertThat(((BooleanType) validateCode(VS_URL, STUB_CS_URL, "C").getParameterValue("result")).booleanValue())
					.isTrue();
			assertThat(((BooleanType) validateCode(VS_URL, REAL_CS_URL, "A").getParameterValue("result")).booleanValue())
					.isTrue();
			registerRules(CodeRuleEnum.ALLOW_NOT_IN.build(VS_URL));
			assertReadIsDenied(OBSERVATION_A);
		}

		/**
		 * The queries a CodeSystem store runs do not grow with ValueSets that do not use it
		 */
		@Test
		void storeCodeSystem_unrelatedEmptyExpansions_runsTheSameNumberOfSelects() {
			int selectsWithoutUnrelated = countSelectsStoringCodeSystem("http://example.org/CodeSystem/first");

			String otherStubUrl = STUB_CS_URL + "-other";
			CodeSystem otherStub = new CodeSystem();
			otherStub.setUrl(otherStubUrl);
			otherStub.setStatus(Enumerations.PublicationStatus.ACTIVE);
			otherStub.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
			myCodeSystemDao.create(otherStub, mySrd);
			for (int i = 0; i < 3; i++) {
				String url = VS_URL + "-unrelated-" + i;
				myValueSetDao.update(newListingValueSet("ig-unrelated-" + i, url, otherStubUrl, null), mySrd);
			}
			myBatch2JobHelper.awaitNoJobsRunning();
			runInTransaction(() -> assertThat(myTermValueSetDao
							.findTermValueSetByUrlAndVersion(VS_URL + "-unrelated-0", VS_VERSION)
							.orElseThrow()
							.getTotalConcepts())
					.isZero());

			assertThat(countSelectsStoringCodeSystem("http://example.org/CodeSystem/second"))
					.isEqualTo(selectsWithoutUnrelated);
		}

		private int countSelectsStoringCodeSystem(String theUrl) {
			CodeSystem codeSystem = new CodeSystem();
			codeSystem.setUrl(theUrl);
			codeSystem.setStatus(Enumerations.PublicationStatus.ACTIVE);
			codeSystem.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
			codeSystem.addConcept().setCode("A");

			myCaptureQueriesListener.clear();
			myCodeSystemDao.create(codeSystem, mySrd);
			return myCaptureQueriesListener.countSelectQueries();
		}
	}

	/**
	 * The server has version 1.0 of a CodeSystem, holding code {@code A}, uploaded outside the package. The package
	 * ships a ValueSet listing codes {@code A} and {@code B} from version 2.0 of it, which the server does not have.
	 */
	@Nested
	class UnknownCodeSystemVersion {
		private static final String CS_URL = "http://example.org/CodeSystem/ig-versioned";
		private static final String VS_URL = "http://example.org/ValueSet/ig-versioned-listed";

		@BeforeEach
		void beforeCreateCodeSystemAndObservations() {
			CodeSystem codeSystem = new CodeSystem();
			codeSystem.setUrl(CS_URL);
			codeSystem.setVersion("1.0");
			codeSystem.setStatus(Enumerations.PublicationStatus.ACTIVE);
			codeSystem.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
			codeSystem.addConcept().setCode("A");
			myCodeSystemDao.create(codeSystem, mySrd);

			createObservation(withId("coded-a"), withObservationCode(CS_URL, "A"));
			createObservation(withId("coded-b"), withObservationCode(CS_URL, "B"));
			myTerminologyDeferredStorageSvc.saveAllDeferred();
			myBatch2JobHelper.awaitNoJobsRunning();
		}

		@Test
		void install_settingOff_valueSetCannotBeUsed() throws IOException {
			installPackage();

			assertThatThrownBy(() -> expand(VS_URL))
					.isInstanceOf(InvalidRequestException.class)
					.hasMessageContaining(CS_URL);
			assertThat(((BooleanType) validateCode(VS_URL, CS_URL, "A").getParameterValue("result")).booleanValue())
					.isFalse();

			registerRules(CodeRuleEnum.ALLOW_IN.build(VS_URL));
			assertReadIsDenied(OBSERVATION_A);
		}

		/**
		 * The setting is turned on before the install, so the pre-expansion the install queues uses version 1.0
		 */
		@Test
		void install_useCurrentVersion_valueSetUsesVersionTheServerHas() throws IOException {
			myStorageSettings.setUseCurrentVersionForUnknownCodeSystemVersion(true);
			installPackage();

			ValueSet expansion = expand(VS_URL);
			assertThat(expansion.getExpansion().getContains())
					.extracting(ValueSet.ValueSetExpansionContainsComponent::getCode)
					.containsExactly("A");
			assertThat(((BooleanType) validateCode(VS_URL, CS_URL, "A").getParameterValue("result")).booleanValue())
					.isTrue();
			assertThat(((BooleanType) validateCode(VS_URL, CS_URL, "B").getParameterValue("result")).booleanValue())
					.isFalse();

			registerRules(CodeRuleEnum.ALLOW_IN.build(VS_URL));
			assertThat(myClient.read().resource(Observation.class).withId(OBSERVATION_A).execute())
					.isNotNull();
			assertReadIsDenied(OBSERVATION_B);
		}

		private void installPackage() throws IOException {
			install("example.ig.versioned", newListingValueSet("ig-versioned-listed", VS_URL, CS_URL, "2.0"));
		}
	}

	private void install(String thePackageName, IBaseResource... theResources) throws IOException {
		NpmPackageFactory factory = new NpmPackageFactory(myFhirContext).name(thePackageName).version("1.0.0");
		for (IBaseResource resource : theResources) {
			factory.addResource(resource.getIdElement().getIdPart(), resource);
		}
		myPackageInstallerSvc.install(new PackageInstallationSpec()
				.setName(factory.getPackageName())
				.setVersion(factory.getPackageVersion())
				.setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL)
				.setPackageContents(factory.createPackageBytes()));
		myTerminologyDeferredStorageSvc.saveAllDeferred();
		myBatch2JobHelper.awaitNoJobsRunning();
	}

	private ValueSet expand(String theValueSetUrl) {
		return myClient.operation()
				.onType(ValueSet.class)
				.named("expand")
				.withParameter(Parameters.class, "url", new UriType(theValueSetUrl))
				.returnResourceType(ValueSet.class)
				.execute();
	}

	private Parameters validateCode(String theValueSetUrl, String theSystem, String theCode) {
		Parameters outcome = myClient.operation()
				.onType(ValueSet.class)
				.named("validate-code")
				.withParameter(Parameters.class, "url", new UriType(theValueSetUrl))
				.andParameter("code", new CodeType(theCode))
				.andParameter("system", new UriType(theSystem))
				.execute();
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
		return outcome;
	}

	private void registerRules(List<IAuthRule> theRules) {
		myServer.getRestfulServer().registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return theRules;
			}
		}.setValidationSupport(myValidationSupport));
	}

	private void assertReadIsDenied(String theId) {
		var request = myClient.read().resource(Observation.class).withId(theId);
		assertThatThrownBy(request::execute).isInstanceOf(ForbiddenOperationException.class);
	}

	private static ValueSet newListingValueSet(String theId, String theUrl, String theSystem, String theSystemVersion) {
		ValueSet valueSet = new ValueSet();
		valueSet.setId(theId);
		valueSet.setUrl(theUrl);
		valueSet.setVersion(VS_VERSION);
		valueSet.setStatus(Enumerations.PublicationStatus.ACTIVE);
		ValueSet.ConceptSetComponent include =
				valueSet.getCompose().addInclude().setSystem(theSystem).setVersion(theSystemVersion);
		include.addConcept().setCode("A");
		include.addConcept().setCode("B");
		return valueSet;
	}

	private enum CodeRuleEnum {
		ALLOW_IN(theVsUrl -> new RuleBuilder()
				.allow()
				.read()
				.resourcesOfType("Observation")
				.withCodeInValueSet("code", theVsUrl)
				.andThen()
				.build()),
		ALLOW_NOT_IN(theVsUrl -> new RuleBuilder()
				.allow()
				.read()
				.resourcesOfType("Observation")
				.withCodeNotInValueSet("code", theVsUrl)
				.andThen()
				.build()),
		DENY_IN(theVsUrl -> new RuleBuilder()
				.deny()
				.read()
				.resourcesOfType("Observation")
				.withCodeInValueSet("code", theVsUrl)
				.andThen()
				.allow()
				.read()
				.resourcesOfType("Observation")
				.withAnyId()
				.andThen()
				.build()),
		DENY_NOT_IN(theVsUrl -> new RuleBuilder()
				.deny()
				.read()
				.resourcesOfType("Observation")
				.withCodeNotInValueSet("code", theVsUrl)
				.andThen()
				.allow()
				.read()
				.resourcesOfType("Observation")
				.withAnyId()
				.andThen()
				.build());

		private final Function<String, List<IAuthRule>> myRules;

		CodeRuleEnum(Function<String, List<IAuthRule>> theRules) {
			myRules = theRules;
		}

		List<IAuthRule> build(String theValueSetUrl) {
			return myRules.apply(theValueSetUrl);
		}
	}
}
