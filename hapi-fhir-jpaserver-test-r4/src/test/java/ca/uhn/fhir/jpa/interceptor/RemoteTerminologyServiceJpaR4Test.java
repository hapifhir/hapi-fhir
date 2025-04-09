package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ElementDefinition;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.r5.utils.validation.constants.BestPracticeWarningLevel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This test verifies how {@link RemoteTerminologyServiceValidationSupport} interacts with
 * the rest of the ValidationSupportChain. The aim here is that we should perform as few
 * interactions across the network as we can, so any caching that avoids a lookup through
 * the remote module is a good thing. We're also testing that we don't open more database
 * connections than we need to, since every connection is a delay.
 */
public class RemoteTerminologyServiceJpaR4Test extends BaseJpaR4Test {

	private static final MyCodeSystemProvider ourCodeSystemProvider = new MyCodeSystemProvider();
	private static final MyValueSetProvider ourValueSetProvider = new MyValueSetProvider();
	@RegisterExtension
	private static final RestfulServerExtension ourTerminologyServer = new RestfulServerExtension(FhirContext.forR4Cached())
		.registerProvider(ourCodeSystemProvider)
		.registerProvider(ourValueSetProvider);
	@Autowired
	private ValidationSupportChain myValidationSupportChain;
	@Autowired
	private FhirInstanceValidator myFhirInstanceValidator;
	private RemoteTerminologyServiceValidationSupport myRemoteTerminologyService;
	private ValidationSupportChain myInternalValidationSupport;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();

		myFhirInstanceValidator.setBestPracticeWarningLevel(BestPracticeWarningLevel.Ignore);

		List<IValidationSupport> original = myValidationSupportChain.getValidationSupports();
		myInternalValidationSupport = new ValidationSupportChain(original);

		myRemoteTerminologyService = new RemoteTerminologyServiceValidationSupport(myFhirContext, ourTerminologyServer.getBaseUrl());
		myValidationSupportChain.addValidationSupport(0, myRemoteTerminologyService);
		myValidationSupportChain.invalidateCaches();

		// Warm this as it's needed once by the FhirPath evaluator on startup
		// so this avoids having different connection counts depending on
		// which test method is called first. This is a non-expiring cache, so
		// pre-warming here isn't affecting anything meaningful.
		myValidationSupportChain.fetchAllStructureDefinitions();
	}

	@AfterEach
	public void after() throws Exception {
		myValidationSupportChain.logCacheSizes();
		myValidationSupportChain.removeValidationSupport(myRemoteTerminologyService);

		ourValueSetProvider.clearAll();
		ourCodeSystemProvider.clearAll();
	}

	@Test
	public void testValidateSimpleCode() {
		Patient p = new Patient();
		p.setGender(Enumerations.AdministrativeGender.FEMALE);

		// Test 1
		ourCodeSystemProvider.clearCalls();
		ourValueSetProvider.clearCalls();
		myCaptureQueriesListener.clear();
		IBaseOperationOutcome outcome = validate(p);
		assertSuccess(outcome);

		// Verify 1
		Assertions.assertEquals(2, myCaptureQueriesListener.countGetConnections());
		assertThat(ourValueSetProvider.mySearchUrls).asList().containsExactlyInAnyOrder(
			"http://hl7.org/fhir/ValueSet/administrative-gender|4.0.1",
			"http://hl7.org/fhir/ValueSet/administrative-gender","http://hl7.org/fhir/ValueSet/administrative-gender"
		);
		assertThat(ourCodeSystemProvider.mySearchUrls).asList().containsExactlyInAnyOrder(
			"http://hl7.org/fhir/administrative-gender",
			"http://hl7.org/fhir/administrative-gender"
		);

		// Test 2 (should rely on caches)
		ourCodeSystemProvider.clearCalls();
		ourValueSetProvider.clearCalls();
		myCaptureQueriesListener.clear();
		outcome = validate(p);
		assertSuccess(outcome);

		// Verify 2
		Assertions.assertEquals(0, myCaptureQueriesListener.countGetConnections());
		assertThat(ourValueSetProvider.mySearchUrls).asList().isEmpty();
		assertThat(ourCodeSystemProvider.mySearchUrls).asList().isEmpty();

	}

	@Test
	public void testValidateSimpleCode_SupportedByRemoteService() {
		Patient p = new Patient();
		p.setGender(Enumerations.AdministrativeGender.FEMALE);
		ourValueSetProvider.add((ValueSet) requireNonNull(myInternalValidationSupport.fetchValueSet("http://hl7.org/fhir/ValueSet/administrative-gender")));
		ourCodeSystemProvider.add((CodeSystem) requireNonNull(myInternalValidationSupport.fetchCodeSystem("http://hl7.org/fhir/administrative-gender")));

		// Test 1
		ourCodeSystemProvider.clearCalls();
		ourValueSetProvider.clearCalls();
		myCaptureQueriesListener.clear();
		IBaseOperationOutcome outcome = validate(p);
		assertSuccess(outcome);

		// Verify 1
		Assertions.assertEquals(0, myCaptureQueriesListener.countGetConnections());
		assertThat(ourValueSetProvider.mySearchUrls).asList().containsExactlyInAnyOrder(
			"http://hl7.org/fhir/ValueSet/administrative-gender|4.0.1",
			"http://hl7.org/fhir/ValueSet/administrative-gender"
		);
		assertThat(ourValueSetProvider.myValidatedCodes).asList().containsExactlyInAnyOrder(
			"http://hl7.org/fhir/ValueSet/administrative-gender#http://hl7.org/fhir/administrative-gender#female"
		);
		assertThat(ourCodeSystemProvider.mySearchUrls).asList().containsExactlyInAnyOrder(
			"http://hl7.org/fhir/administrative-gender"
		);
		assertThat(ourCodeSystemProvider.myValidatedCodes).asList().containsExactlyInAnyOrder(
			"http://hl7.org/fhir/administrative-gender#female#null"
		);

		// Test 2 (should rely on caches)
		ourCodeSystemProvider.clearCalls();
		ourValueSetProvider.clearCalls();
		myCaptureQueriesListener.clear();
		outcome = validate(p);
		assertSuccess(outcome);

		// Verify 2
		Assertions.assertEquals(0, myCaptureQueriesListener.countGetConnections());
		assertThat(ourValueSetProvider.mySearchUrls).asList().isEmpty();
		assertThat(ourValueSetProvider.myValidatedCodes).asList().isEmpty();
		assertThat(ourCodeSystemProvider.mySearchUrls).asList().isEmpty();
		assertThat(ourCodeSystemProvider.myValidatedCodes).asList().isEmpty();

	}

	/**
	 * If the remote terminology service is serving up stub ValueSet and CodeSystem
	 * resources, make sure we still behave in a sane way. This probably wouldn't
	 * happen exactly like this, but the idea here is that the server could
	 * serve up weird contents where our internal services couldn't determine
	 * the implicit system from the ValueSet.
	 */
	@Test
	public void testValidateSimpleCode_SupportedByRemoteService_EmptyValueSet() {
		Patient p = new Patient();
		p.setGender(Enumerations.AdministrativeGender.FEMALE);
		ourValueSetProvider.add((ValueSet) new ValueSet().setUrl("http://hl7.org/fhir/ValueSet/administrative-gender").setId("gender"));
		ourCodeSystemProvider.add((CodeSystem) new CodeSystem().setUrl("http://hl7.org/fhir/administrative-gender").setId("gender"));

		// Test 1
		ourCodeSystemProvider.clearCalls();
		ourValueSetProvider.clearCalls();
		myCaptureQueriesListener.clear();
		IBaseOperationOutcome outcome = validate(p);
		assertSuccess(outcome);

		// Verify 1
		Assertions.assertEquals(0, myCaptureQueriesListener.countGetConnections());
		assertThat(ourValueSetProvider.mySearchUrls).asList().containsExactlyInAnyOrder(
			"http://hl7.org/fhir/ValueSet/administrative-gender|4.0.1",
			"http://hl7.org/fhir/ValueSet/administrative-gender"
		);
		assertThat(ourValueSetProvider.myValidatedCodes).asList().containsExactlyInAnyOrder(
			"http://hl7.org/fhir/ValueSet/administrative-gender#http://hl7.org/fhir/administrative-gender#female"
		);
		assertThat(ourCodeSystemProvider.mySearchUrls).asList().containsExactlyInAnyOrder(
			"http://hl7.org/fhir/administrative-gender"
		);
		assertThat(ourCodeSystemProvider.myValidatedCodes).asList().containsExactlyInAnyOrder(
			"http://hl7.org/fhir/administrative-gender#female#null"
		);

		// Test 2 (should rely on caches)
		ourCodeSystemProvider.clearCalls();
		ourValueSetProvider.clearCalls();
		myCaptureQueriesListener.clear();
		outcome = validate(p);
		assertSuccess(outcome);

		// Verify 2
		Assertions.assertEquals(0, myCaptureQueriesListener.countGetConnections());
		assertThat(ourValueSetProvider.mySearchUrls).asList().isEmpty();
		assertThat(ourValueSetProvider.myValidatedCodes).asList().isEmpty();
		assertThat(ourCodeSystemProvider.mySearchUrls).asList().isEmpty();
		assertThat(ourCodeSystemProvider.myValidatedCodes).asList().isEmpty();

		myValidationSupportChain.logCacheSizes();
	}

	@Test
	public void testValidateSimpleExtension() {
		// Setup
		myStructureDefinitionDao.create(createFooExtensionStructureDefinition(), mySrd);
		Patient p = new Patient();
		p.addExtension("http://foo", new StringType("BAR"));

		// Test 1
		ourCodeSystemProvider.clearCalls();
		ourValueSetProvider.clearCalls();
		myCaptureQueriesListener.clear();
		IBaseOperationOutcome outcome = validate(p);
		assertSuccess(outcome);

		// Verify 1
		myCaptureQueriesListener.logSelectQueries();
		Assertions.assertEquals(3, myCaptureQueriesListener.countGetConnections());
		assertThat(ourValueSetProvider.mySearchUrls).asList().isEmpty();
		assertThat(ourCodeSystemProvider.mySearchUrls).asList().isEmpty();

		// Test 2 (should rely on caches)
		ourCodeSystemProvider.clearCalls();
		ourValueSetProvider.clearCalls();
		myCaptureQueriesListener.clear();
		outcome = validate(p);
		assertSuccess(outcome);

		// Verify 2
		Assertions.assertEquals(0, myCaptureQueriesListener.countGetConnections());
		assertThat(ourValueSetProvider.mySearchUrls).asList().isEmpty();
		assertThat(ourCodeSystemProvider.mySearchUrls).asList().isEmpty();

	}

	@Test
	public void testValidateMultipleCodings() {
		// Setup
		Patient p = new Patient();
		p.addIdentifier()
			// Valid type
			.setType(new CodeableConcept().addCoding(new Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "DL", null)))
			.setSystem("http://my-system-1")
			.setValue("1");
		p.addIdentifier()
			// Valid type
			.setType(new CodeableConcept().addCoding(new Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "PPN", null)))
			.setSystem("http://my-system-2")
			.setValue("2");
		p.addIdentifier()
			// Invalid type
			.setType(new CodeableConcept().addCoding(new Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "FOO", null)))
			.setSystem("http://my-system-3")
			.setValue("3");

		// Test 1
		ourCodeSystemProvider.clearCalls();
		ourValueSetProvider.clearCalls();
		myCaptureQueriesListener.clear();
		IBaseOperationOutcome outcome = validate(p);
		assertHasIssuesContainingMessages(outcome,
			"Unknown code 'http://terminology.hl7.org/CodeSystem/v2-0203#FOO'",
			"None of the codings provided are in the value set 'IdentifierType'");

		// Verify 1
		Assertions.assertEquals(2, myCaptureQueriesListener.countGetConnections());
		assertThat(ourValueSetProvider.mySearchUrls).asList().containsExactlyInAnyOrder(
			"http://hl7.org/fhir/ValueSet/identifier-type",
			"http://hl7.org/fhir/ValueSet/identifier-type"
		);
		assertThat(ourCodeSystemProvider.mySearchUrls).asList().containsExactlyInAnyOrder(
			"http://terminology.hl7.org/CodeSystem/v2-0203",
			"http://terminology.hl7.org/CodeSystem/v2-0203"
		);
		assertEquals(0, ourValueSetProvider.myValidatedCodes.size());
		assertEquals(0, ourCodeSystemProvider.myValidatedCodes.size());

		// Test 2 (should rely on caches)
		ourCodeSystemProvider.clearCalls();
		ourValueSetProvider.clearCalls();
		myCaptureQueriesListener.clear();
		validate(p);

		// Verify 2
		Assertions.assertEquals(0, myCaptureQueriesListener.countGetConnections());
		assertThat(ourValueSetProvider.mySearchUrls).asList().isEmpty();
		assertThat(ourCodeSystemProvider.mySearchUrls).asList().isEmpty();
		assertEquals(0, ourValueSetProvider.myValidatedCodes.size());
		assertEquals(0, ourCodeSystemProvider.myValidatedCodes.size());

	}

	private void assertSuccess(IBaseOperationOutcome theOutcome) {
		OperationOutcome oo = (OperationOutcome) theOutcome;
		assertEquals(1, oo.getIssue().size(), () -> encode(oo));
		assertThat(oo.getIssue().get(0).getDiagnostics()).as(() -> encode(oo)).contains("No issues detected");
	}

	private void assertHasIssuesContainingMessages(IBaseOperationOutcome theOutcome, String... theDiagnosticMessageFragments) {
		OperationOutcome oo = (OperationOutcome) theOutcome;
		assertEquals(theDiagnosticMessageFragments.length, oo.getIssue().size(), () -> encode(oo));
		for (int i = 0; i < theDiagnosticMessageFragments.length; i++) {
			assertThat(oo.getIssue().get(i).getDiagnostics()).as(() -> encode(oo)).contains(theDiagnosticMessageFragments[i]);
		}
	}

	private IBaseOperationOutcome validate(Patient p) {
		return myPatientDao.validate(p, null, myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(p), EncodingEnum.JSON, ValidationModeEnum.CREATE, null, mySrd).getOperationOutcome();
	}

	/**
	 * Create a StructureDefinition for an extension with URL <a href="http://foo">http://foo</a>
	 */
	@Nonnull
	private static StructureDefinition createFooExtensionStructureDefinition() {
		StructureDefinition sd = new StructureDefinition();
		sd.setUrl("http://foo");
		sd.setFhirVersion(Enumerations.FHIRVersion._4_0_1);
		sd.setKind(StructureDefinition.StructureDefinitionKind.COMPLEXTYPE);
		sd.setAbstract(false);
		sd.addContext().setType(StructureDefinition.ExtensionContextType.ELEMENT).setExpression("Patient");
		sd.setType("Extension");
		sd.setBaseDefinition("http://hl7.org/fhir/StructureDefinition/Extension");
		sd.setDerivation(StructureDefinition.TypeDerivationRule.CONSTRAINT);
		ElementDefinition e0 = sd.getDifferential().addElement();
		e0.setId("Extension");
		e0.setPath("Extension");
		ElementDefinition e1 = sd.getDifferential().addElement();
		e1.setId("Extension.url");
		e1.setPath("Extension.url");
		e1.setFixed(new UriType("http://foo"));
		ElementDefinition e2 = sd.getDifferential().addElement();
		e2.setId("Extension.value[x]");
		e2.setPath("Extension.value[x]");
		e2.addType().setCode("string");
		return sd;
	}

	private static String toValue(IPrimitiveType<String> theUrlType) {
		return theUrlType != null ? theUrlType.getValue() : null;
	}

	private static class MyCodeSystemProvider implements IResourceProvider {
		private final ListMultimap<String, CodeSystem> myUrlToCodeSystems = MultimapBuilder.hashKeys().arrayListValues().build();
		private final List<String> mySearchUrls = new ArrayList<>();
		private final List<String> myValidatedCodes = new ArrayList<>();

		public void clearAll() {
			myUrlToCodeSystems.clear();
			clearCalls();
		}

		public void clearCalls() {
			mySearchUrls.clear();
			myValidatedCodes.clear();
		}

		@Operation(name = "validate-code", idempotent = true, returnParameters = {
			@OperationParam(name = "result", type = BooleanType.class, min = 1),
			@OperationParam(name = "message", type = StringType.class),
			@OperationParam(name = "display", type = StringType.class)
		})
		public Parameters validateCode(
			HttpServletRequest theServletRequest,
			@IdParam(optional = true) IdType theId,
			@OperationParam(name = "url", min = 0, max = 1) UriType theCodeSystemUrl,
			@OperationParam(name = "code", min = 0, max = 1) CodeType theCode,
			@OperationParam(name = "display", min = 0, max = 1) StringType theDisplay
		) {
			myValidatedCodes.add(toValue(theCodeSystemUrl) + "#" + toValue(theCode) + "#" + toValue(theDisplay));

			Parameters retVal = new Parameters();
			retVal.addParameter("result", new BooleanType(true));
			return retVal;
		}

		@Search
		public List<CodeSystem> find(@RequiredParam(name = "url") UriParam theUrlParam) {
			String url = theUrlParam != null ? theUrlParam.getValue() : null;
			mySearchUrls.add(url);
			return myUrlToCodeSystems.get(defaultString(url));
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return CodeSystem.class;
		}

		public void add(CodeSystem theCs) {
			assert theCs != null;
			assert isNotBlank(theCs.getUrl());
			myUrlToCodeSystems.put(theCs.getUrl(), theCs);
		}
	}

	@SuppressWarnings("unused")
	private static class MyValueSetProvider implements IResourceProvider {

		private final ListMultimap<String, ValueSet> myUrlToValueSets = MultimapBuilder.hashKeys().arrayListValues().build();
		private final List<String> mySearchUrls = new ArrayList<>();
		private final List<String> myValidatedCodes = new ArrayList<>();

		public void clearAll() {
			myUrlToValueSets.clear();
			clearCalls();
		}

		public void clearCalls() {
			mySearchUrls.clear();
			myValidatedCodes.clear();
		}

		@Operation(name = "validate-code", idempotent = true, returnParameters = {
			@OperationParam(name = "result", type = BooleanType.class, min = 1),
			@OperationParam(name = "message", type = StringType.class),
			@OperationParam(name = "display", type = StringType.class)
		})
		public Parameters validateCode(
			HttpServletRequest theServletRequest,
			@OperationParam(name = "url", min = 0, max = 1) UriType theValueSetUrl,
			@OperationParam(name = "code", min = 0, max = 1) CodeType theCode,
			@OperationParam(name = "system", min = 0, max = 1) UriType theSystem,
			@OperationParam(name = "display", min = 0, max = 1) StringType theDisplay,
			@OperationParam(name = "valueSet") ValueSet theValueSet
		) {
			myValidatedCodes.add(toValue(theValueSetUrl) + "#" + toValue(theSystem) + "#" + toValue(theCode));

			Parameters retVal = new Parameters();
			retVal.addParameter("result", new BooleanType(true));
			return retVal;
		}

		@Search
		public List<ValueSet> find(@OptionalParam(name = "url") UriParam theUrlParam) {
			String url = theUrlParam != null ? theUrlParam.getValue() : null;
			mySearchUrls.add(url);
			List<ValueSet> retVal = myUrlToValueSets.get(defaultString(url));
			ourLog.info("Remote terminology fetch ValueSet[{}] - Found: {}", url, !retVal.isEmpty());
			return retVal;
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return ValueSet.class;
		}

		public void add(ValueSet theVs) {
			assert theVs != null;
			assert isNotBlank(theVs.getUrl());
			myUrlToValueSets.put(theVs.getUrl(), theVs);
		}
	}

}
