package ca.uhn.fhir.jpa.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.config.JpaConfig;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.test.utilities.validation.IValidationProviders;
import ca.uhn.fhir.test.utilities.validation.IValidationProvidersR4;
import ca.uhn.fhir.util.ClasspathUtil;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Procedure;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

import static ca.uhn.fhir.jpa.model.util.JpaConstants.OPERATION_VALIDATE_CODE;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests resource validation with Remote Terminology bindings.
 * To create a new test, you need to do 3 things:
 * (1) the resource profile, if any custom one is needed should be stored in the FHIR repository
 * (2) all the CodeSystem and ValueSet terminology resources need to be added to the corresponding resource provider.
 * At the moment only placeholder CodeSystem/ValueSet resources are returned with id and url populated. For the moment
 * there was no need to load the full resource, but that can be done if there is logic run which requires it.
 * This is a minimal setup.
 * (3) the Remote Terminology operation responses that are needed for the test need to be added to the corresponding
 * resource provider. The intention is to record and use the responses of an actual terminology server
 * e.g. <a href="https://r4.ontoserver.csiro.au/fhir/">OntoServer</a>.
 * This is done as a result of the fact that unit test cannot always catch bugs which are introduced as a result of
 * changes in the OntoServer or FHIR Validator library, or both.
 * @see #setupValueSetValidateCode
 * @see #setupCodeSystemValidateCode
 * The responses are in Parameters resource format where issues is an OperationOutcome resource.
 */
public class ValidateWithRemoteTerminologyTest extends BaseResourceProviderR4Test {
	private static final FhirContext ourCtx = FhirContext.forR4Cached();

	@RegisterExtension
	protected static RestfulServerExtension ourRestfulServerExtension = new RestfulServerExtension(ourCtx);
	private RemoteTerminologyServiceValidationSupport mySvc;
	@Autowired
	@Qualifier(JpaConfig.JPA_VALIDATION_SUPPORT_CHAIN)
	private ValidationSupportChain myValidationSupportChain;
	private IValidationProviders.MyValidationProvider<CodeSystem> myCodeSystemProvider;
	private IValidationProviders.MyValidationProvider<ValueSet> myValueSetProvider;

	@BeforeEach
	public void before() {
		String baseUrl = "http://localhost:" + ourRestfulServerExtension.getPort();
		mySvc = new RemoteTerminologyServiceValidationSupport(ourCtx, baseUrl);
		myValidationSupportChain.addValidationSupport(0, mySvc);
		myCodeSystemProvider = new IValidationProvidersR4.MyCodeSystemProviderR4();
		myValueSetProvider = new IValidationProvidersR4.MyValueSetProviderR4();
		ourRestfulServerExtension.registerProvider(myCodeSystemProvider);
		ourRestfulServerExtension.registerProvider(myValueSetProvider);
	}

	@AfterEach
	public void after() {
		myValidationSupportChain.removeValidationSupport(mySvc);
		ourRestfulServerExtension.getRestfulServer().getInterceptorService().unregisterAllInterceptors();
		ourRestfulServerExtension.unregisterProvider(myCodeSystemProvider);
		ourRestfulServerExtension.unregisterProvider(myValueSetProvider);
	}

	@Test
	public void validate_withProfileWithValidCodesFromAllBindingTypes_returnsNoErrors() {
		// setup
		final StructureDefinition profileEncounter = ClasspathUtil.loadResource(ourCtx, StructureDefinition.class, "validation/encounter/profile-encounter-custom.json");
		myClient.update().resource(profileEncounter).execute();

		final String statusCode = "planned";
		final String classCode = "IMP";
		final String identifierTypeCode = "VN";

		final String statusSystem = "http://hl7.org/fhir/encounter-status"; // implied system
		final String classSystem = "http://terminology.hl7.org/CodeSystem/v3-ActCode";
		final String identifierTypeSystem = "http://terminology.hl7.org/CodeSystem/v2-0203";

		setupValueSetValidateCode("http://hl7.org/fhir/ValueSet/encounter-status",  "4.0.1","http://hl7.org/fhir/encounter-status", statusCode, "validation/encounter/validateCode-ValueSet-encounter-status.json");
		setupValueSetValidateCode("http://terminology.hl7.org/ValueSet/v3-ActEncounterCode", "http://terminology.hl7.org/CodeSystem/v3-ActCode", classCode, "validation/encounter/validateCode-ValueSet-v3-ActEncounterCode.json");
		setupValueSetValidateCode("http://hl7.org/fhir/ValueSet/identifier-type", "http://hl7.org/fhir/identifier-type", identifierTypeCode, "validation/encounter/validateCode-ValueSet-identifier-type.json");

		setupCodeSystemValidateCode(statusSystem, statusCode, "validation/encounter/validateCode-CodeSystem-encounter-status.json");
		setupCodeSystemValidateCode(classSystem, classCode, "validation/encounter/validateCode-CodeSystem-v3-ActCode.json");
		setupCodeSystemValidateCode(identifierTypeSystem, identifierTypeCode, "validation/encounter/validateCode-CodeSystem-v2-0203.json");

		Encounter encounter = new Encounter();
		encounter.getMeta().addProfile("http://example.ca/fhir/StructureDefinition/profile-encounter");

		// required binding
		encounter.setStatus(Encounter.EncounterStatus.fromCode(statusCode));

		// preferred binding
		encounter.getClass_()
				.setSystem(classSystem)
				.setCode(classCode)
				.setDisplay("inpatient encounter");

		// extensible binding
		encounter.addIdentifier()
				.getType().addCoding()
				.setSystem(identifierTypeSystem)
				.setCode(identifierTypeCode)
				.setDisplay("Visit number");

		// execute
		List<String> errors = getValidationErrors(encounter);

		// verify
		assertThat(errors).isEmpty();
	}

	@Test
	public void validate_withInvalidCode_returnsErrors() {
		// setup
		final String statusCode = "final";
		final String code = "10xx";

		final String statusSystem = "http://hl7.org/fhir/observation-status";
		final String loincSystem = "http://loinc.org";
		final String system = "http://fhir.infoway-inforoute.ca/io/psca/CodeSystem/ICD9CM";

		setupValueSetValidateCode("http://hl7.org/fhir/ValueSet/observation-status", "4.0.1", statusSystem, statusCode, "validation/observation/validateCode-ValueSet-observation-status.json");
		setupValueSetValidateCode("http://hl7.org/fhir/ValueSet/observation-codes", loincSystem, statusCode, "validation/observation/validateCode-ValueSet-codes.json");

		setupCodeSystemValidateCode(statusSystem, statusCode, "validation/observation/validateCode-CodeSystem-observation-status.json");
		setupCodeSystemValidateCode(system, code, "validation/observation/validateCode-CodeSystem-ICD9CM.json");

		Observation obs = new Observation();
		obs.setStatus(Observation.ObservationStatus.fromCode(statusCode));
		obs.getCode().addCoding().setCode(code).setSystem(system);

		// execute
		List<String> errors = getValidationErrors(obs);
		assertThat(errors).hasSize(1);

		// verify
		assertThat(errors.get(0))
				.contains("Unknown code '10xx' in the CodeSystem 'http://fhir.infoway-inforoute.ca/io/psca/CodeSystem/ICD9CM");
	}

	@Test
	public void validate_withProfileWithInvalidCode_returnsErrors() {
		// setup
		String profile = "http://example.ca/fhir/StructureDefinition/profile-procedure";
		StructureDefinition profileProcedure = ClasspathUtil.loadResource(myFhirContext, StructureDefinition.class, "validation/procedure/profile-procedure.json");
		myClient.update().resource(profileProcedure).execute();

		final String statusCode = "completed";
		final String procedureCode1 = "417005";
		final String procedureCode2 = "xx417005";

		final String statusSystem = "http://hl7.org/fhir/event-status";
		final String snomedSystem = "http://snomed.info/sct";

		setupValueSetValidateCode("http://hl7.org/fhir/ValueSet/event-status", "4.0.1", statusSystem, statusCode, "validation/procedure/validateCode-ValueSet-event-status.json");
		setupValueSetValidateCode("http://hl7.org/fhir/ValueSet/procedure-code", snomedSystem, procedureCode1, "validation/procedure/validateCode-ValueSet-procedure-code-valid.json");
		setupValueSetValidateCode("http://hl7.org/fhir/ValueSet/procedure-code", snomedSystem, procedureCode2, "validation/procedure/validateCode-ValueSet-procedure-code-invalid.json");

		setupCodeSystemValidateCode(statusSystem, statusCode, "validation/procedure/validateCode-CodeSystem-event-status.json");
		setupCodeSystemValidateCode(snomedSystem, procedureCode1, "validation/procedure/validateCode-CodeSystem-snomed-valid.json");
		setupCodeSystemValidateCode(snomedSystem, procedureCode2, "validation/procedure/validateCode-CodeSystem-snomed-invalid.json");

		Procedure procedure = new Procedure();
		procedure.setSubject(new Reference("Patient/P1"));
		procedure.setStatus(Procedure.ProcedureStatus.fromCode(statusCode));
		procedure.getCode().addCoding().setSystem(snomedSystem).setCode(procedureCode1);
		procedure.getCode().addCoding().setSystem(snomedSystem).setCode(procedureCode2);
		procedure.getMeta().addProfile(profile);

		// execute
		List<String> errors = getValidationErrors(procedure);
		// TODO: there is currently some duplication in the errors returned. This needs to be investigated and fixed.
		// assertThat(errors).hasSize(1);

		// verify
		// note that we're not selecting an explicit versions (using latest) so the message verification does not include it.
		assertThat(StringUtils.join("", errors))
				.contains("Unknown code 'xx417005' in the CodeSystem 'http://snomed.info/sct'")
				.doesNotContain("The provided code 'http://snomed.info/sct#xx417005' was not found in the value set 'http://hl7.org/fhir/ValueSet/procedure-code")
				.doesNotContain("http://snomed.info/sct#417005");
	}

	@Test
	public void validate_withProfileWithSlicingWithValidCode_returnsNoErrors() {
		// setup
		String profile = "http://example.ca/fhir/StructureDefinition/profile-procedure-with-slicing";
		StructureDefinition profileProcedure = ClasspathUtil.loadResource(myFhirContext, StructureDefinition.class, "validation/procedure/profile-procedure-slicing.json");
		myClient.update().resource(profileProcedure).execute();

		final String statusCode = "completed";
		final String procedureCode = "no-procedure-info";

		final String statusSystem = "http://hl7.org/fhir/event-status";
		final String snomedSystem = "http://snomed.info/sct";
		final String absentUnknownSystem = "http://hl7.org/fhir/uv/ips/CodeSystem/absent-unknown-uv-ips";

		setupValueSetValidateCode("http://hl7.org/fhir/ValueSet/event-status", "4.0.1", statusSystem, statusCode, "validation/procedure/validateCode-ValueSet-event-status.json");
		setupValueSetValidateCode("http://hl7.org/fhir/ValueSet/procedure-code", snomedSystem, procedureCode, "validation/procedure/validateCode-ValueSet-procedure-code-invalid-slice.json");
		setupValueSetValidateCode("http://hl7.org/fhir/uv/ips/ValueSet/absent-or-unknown-procedures-uv-ips", absentUnknownSystem, procedureCode, "validation/procedure/validateCode-ValueSet-absent-or-unknown-procedure.json");

		setupCodeSystemValidateCode(statusSystem, statusCode, "validation/procedure/validateCode-CodeSystem-event-status.json");
		setupCodeSystemValidateCode(absentUnknownSystem, procedureCode, "validation/procedure/validateCode-CodeSystem-absent-or-unknown.json");

		Procedure procedure = new Procedure();
		procedure.setSubject(new Reference("Patient/P1"));
		procedure.setStatus(Procedure.ProcedureStatus.fromCode(statusCode));
		procedure.getCode().addCoding().setSystem(absentUnknownSystem).setCode(procedureCode);
		procedure.getMeta().addProfile(profile);

		// execute
		List<String> errors = getValidationErrors(procedure);
		assertThat(errors).hasSize(0);
	}

	private void setupValueSetValidateCode(String theUrl, String theSystem, String theCode, String theTerminologyResponseFile) {
		ValueSet valueSet = myValueSetProvider.addTerminologyResource(theUrl);
		myCodeSystemProvider.addTerminologyResource(theSystem);
		myValueSetProvider.addTerminologyResponse(OPERATION_VALIDATE_CODE, valueSet.getUrl(), theCode, ourCtx, theTerminologyResponseFile);

		// we currently do this because VersionSpecificWorkerContextWrapper has logic to infer the system when missing
		// based on the ValueSet by calling ValidationSupportUtils#extractCodeSystemForCode.
		valueSet.getCompose().addInclude().setSystem(theSystem);

		// you will notice each of these calls require also a call to setupCodeSystemValidateCode
		// that is necessary because VersionSpecificWorkerContextWrapper#validateCodeInValueSet
		// which also attempts a validateCode against the CodeSystem after the validateCode against the ValueSet
	}

	private void setupValueSetValidateCode(String theUrl, String theVersion, String theSystem, String theCode, String theTerminologyResponseFile) {
		ValueSet valueSet = myValueSetProvider.addTerminologyResource(theUrl, theVersion);
		myValueSetProvider.addTerminologyResource(theSystem, theVersion);
		myValueSetProvider.addTerminologyResponse(OPERATION_VALIDATE_CODE, valueSet.getUrl(), theCode, ourCtx, theTerminologyResponseFile);

		valueSet.getCompose().addInclude().setSystem(theSystem);
	}
	private void setupCodeSystemValidateCode(String theUrl, String theCode, String theTerminologyResponseFile) {
		CodeSystem codeSystem = myCodeSystemProvider.addTerminologyResource(theUrl);
		myCodeSystemProvider.addTerminologyResponse(OPERATION_VALIDATE_CODE, codeSystem.getUrl(), theCode, ourCtx, theTerminologyResponseFile);
	}

	private List<String> getValidationErrors(IBaseResource theResource) {
		MethodOutcome resultProcedure = myClient.validate().resource(theResource).execute();
		OperationOutcome operationOutcome = (OperationOutcome) resultProcedure.getOperationOutcome();
		return operationOutcome.getIssue().stream()
				.filter(issue -> issue.getSeverity() == OperationOutcome.IssueSeverity.ERROR)
				.map(OperationOutcome.OperationOutcomeIssueComponent::getDiagnostics)
				.toList();
	}
}
