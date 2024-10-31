package ca.uhn.fhir.jpa.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.config.JpaConfig;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.test.utilities.validation.IValidationProvidersR4;
import ca.uhn.fhir.util.ClasspathUtil;
import org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
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
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidationWithRemoteTerminologyTest extends BaseResourceProviderR4Test {
	private static final FhirContext ourCtx = FhirContext.forR4Cached();

	@RegisterExtension
	protected static RestfulServerExtension ourRestfulServerExtension = new RestfulServerExtension(ourCtx);
	private RemoteTerminologyServiceValidationSupport mySvc;
	@Autowired
	@Qualifier(JpaConfig.JPA_VALIDATION_SUPPORT_CHAIN)
	private ValidationSupportChain myValidationSupportChain;
	private IValidationProvidersR4.MyCodeSystemProviderR4 myCodeSystemProvider;
	private IValidationProvidersR4.MyValueSetProviderR4 myValueSetProvider;

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
	public void validate_withValidCodesFromAllBindingTypes_returnsNoErrors() {
		// setup
		StructureDefinition profileEncounter = ClasspathUtil.loadResource(ourCtx, StructureDefinition.class, "validation/profile-encounter-custom.json");
		myClient.update().resource(profileEncounter).execute();

		ValueSet valueSetIdentifierType = new ValueSet();
		valueSetIdentifierType.setId("identifier-type");
		valueSetIdentifierType.setUrl("http://hl7.org/fhir/ValueSet/identifier-type");
		valueSetIdentifierType.getCompose().addInclude().setSystem("http://terminology.hl7.org/CodeSystem/v2-0203");

		ValueSet valueSetStatus = new ValueSet();
		valueSetStatus.setId("encounter-status");
		valueSetStatus.setUrl("http://hl7.org/fhir/ValueSet/encounter-status");
		valueSetStatus.getCompose().addInclude().setSystem("http://hl7.org/fhir/encounter-status");

		ValueSet valueSetV3ActCode = new ValueSet();
		valueSetV3ActCode.setId("v3-ActEncounterCode");
		valueSetV3ActCode.setUrl("http://terminology.hl7.org/ValueSet/v3-ActEncounterCode");
		valueSetV3ActCode.getCompose().addInclude().setSystem("http://terminology.hl7.org/CodeSystem/v3-ActCode");

		CodeSystem codeSystemV20203 = new CodeSystem();
		codeSystemV20203.setId("v2-0203");
		codeSystemV20203.setUrl("http://terminology.hl7.org/CodeSystem/v2-0203");

		CodeSystem codeSystemStatus = new CodeSystem();
		codeSystemStatus.setId("encounter-status");
		codeSystemStatus.setUrl("http://hl7.org/fhir/encounter-status");

		CodeSystem codeSystemV3ActCode = new CodeSystem();
		codeSystemV3ActCode.setId("v3-ActCode");
		codeSystemV3ActCode.setUrl("http://terminology.hl7.org/CodeSystem/v3-ActCode");

		Parameters validateCodeIdentifierType = ClasspathUtil.loadResource(ourCtx, Parameters.class, "validation/validateCode-ValueSet-identifier-type.json");
		Parameters validateCodeEncounterStatus = ClasspathUtil.loadResource(ourCtx, Parameters.class, "validation/validateCode-ValueSet-status.json");
		Parameters validateCodeActEncounterCode = ClasspathUtil.loadResource(ourCtx, Parameters.class, "validation/validateCode-ValueSet-v3-ActEncounterCode.json");
		myValueSetProvider.addReturnParams(OPERATION_VALIDATE_CODE, valueSetIdentifierType.getUrl(), "VN", validateCodeIdentifierType);
		myValueSetProvider.addReturnParams(OPERATION_VALIDATE_CODE, valueSetStatus.getUrl(), "planned", validateCodeEncounterStatus);
		myValueSetProvider.addReturnParams(OPERATION_VALIDATE_CODE, valueSetStatus.getUrl(), "IMP", validateCodeActEncounterCode);
		myValueSetProvider.addReturnValueSet(valueSetIdentifierType);
		myValueSetProvider.addReturnValueSet(valueSetStatus);
		myValueSetProvider.addReturnValueSet(valueSetV3ActCode);

		Parameters validateCodeV20203 = ClasspathUtil.loadResource(ourCtx, Parameters.class, "validation/validateCode-CodeSystem-v2-0203.json");
		Parameters validateCodeStatus = ClasspathUtil.loadResource(ourCtx, Parameters.class, "validation/validateCode-CodeSystem-status.json");
		Parameters validateCodeActCode = ClasspathUtil.loadResource(ourCtx, Parameters.class, "validation/validateCode-CodeSystem-v3-ActCode.json");
		myCodeSystemProvider.addReturnParams(OPERATION_VALIDATE_CODE, codeSystemV20203.getUrl(), "VN", validateCodeV20203);
		myCodeSystemProvider.addReturnParams(OPERATION_VALIDATE_CODE, codeSystemStatus.getUrl(), "planned", validateCodeStatus);
		myCodeSystemProvider.addReturnParams(OPERATION_VALIDATE_CODE, codeSystemV3ActCode.getUrl(), "IMP", validateCodeActCode);
		myCodeSystemProvider.addReturnCodeSystem(codeSystemV20203);
		myCodeSystemProvider.addReturnCodeSystem(codeSystemStatus);
		myCodeSystemProvider.addReturnCodeSystem(codeSystemV3ActCode);

		Encounter encounter = new Encounter();
		encounter.getMeta().addProfile("http://example.ca/fhir/StructureDefinition/profile-encounter");

		// required binding
		encounter.setStatus(Encounter.EncounterStatus.PLANNED);

		// preferred binding
		/* encounter.getClass_()
				.setSystem("http://terminology.hl7.org/CodeSystem/v3-ActCode")
				.setCode("IMP")
				.setDisplay("inpatient encounter");*/

		// extensible binding
		encounter.addIdentifier()
				.getType().addCoding()
				.setSystem(codeSystemV20203.getUrl())
				.setCode("VN")
				.setDisplay("Visit number");

		// execute
		List<OperationOutcome.OperationOutcomeIssueComponent> errors = getErrors(encounter);
		errors.forEach(issue -> ourLog.info(issue.getDiagnostics()));

		assertEquals(0, errors.size());
	}
	private List<OperationOutcome.OperationOutcomeIssueComponent> getErrors(IBaseResource theResource) {
		MethodOutcome resultProcedure = myClient.validate().resource(theResource).execute();
		OperationOutcome operationOutcome = (OperationOutcome) resultProcedure.getOperationOutcome();
		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(operationOutcome));
		return operationOutcome.getIssue().stream()
				.filter(issue -> issue.getSeverity() == OperationOutcome.IssueSeverity.ERROR)
				.toList();
	}
}
