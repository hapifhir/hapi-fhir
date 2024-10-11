package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.term.TermReadSvcImpl;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.jpa.term.custom.CustomTerminologySet;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.validation.JpaValidationSupportChain;
import ca.uhn.fhir.jpa.validation.ValidationSettings;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.LenientErrorHandler;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.validation.IValidatorModule;
import ca.uhn.test.util.LogbackTestExtensionAssert;
import ch.qos.logback.classic.Level;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.UnknownCodeSystemWarningValidationSupport;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.ElementDefinition;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Group;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Location;
import org.hl7.fhir.r4.model.Narrative;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Observation.ObservationStatus;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Questionnaire;
import org.hl7.fhir.r4.model.QuestionnaireResponse;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.SearchParameter;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.r5.utils.validation.constants.BestPracticeWarningLevel;
import org.hl7.fhir.r5.utils.validation.constants.ReferenceValidationPolicy;
import org.hl7.fhir.utilities.i18n.I18nConstants;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.AopTestUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ca.uhn.fhir.rest.api.Constants.JAVA_VALIDATOR_DETAILS_SYSTEM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService.CURRENCIES_CODESYSTEM_URL;
import static org.hl7.fhir.common.hapi.validation.support.ValidationConstants.LOINC_LOW;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FhirResourceDaoR4ValidateTest extends BaseJpaR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4ValidateTest.class);
	@Autowired
	private IValidatorModule myValidatorModule;
	@Autowired
	private ITermReadSvc myTermReadSvc;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private JpaValidationSupportChain myJpaValidationSupportChain;
	@Autowired
	private ValidationSettings myValidationSettings;
	@Autowired
	private UnknownCodeSystemWarningValidationSupport myUnknownCodeSystemWarningValidationSupport;
	@Autowired
	private InMemoryTerminologyServerValidationSupport myInMemoryTerminologyServerValidationSupport;

	@AfterEach
	public void after() {
		FhirInstanceValidator val = AopTestUtils.getTargetObject(myValidatorModule);
		val.setBestPracticeWarningLevel(BestPracticeWarningLevel.Warning);

		JpaStorageSettings defaults = new JpaStorageSettings();
		myStorageSettings.setAllowExternalReferences(defaults.isAllowExternalReferences());
		myStorageSettings.setMaximumExpansionSize(defaults.getMaximumExpansionSize());
		myStorageSettings.setPreExpandValueSets(defaults.isPreExpandValueSets());
		myStorageSettings.setIssueSeverityForCodeDisplayMismatch(defaults.getIssueSeverityForCodeDisplayMismatch());

		myInMemoryTerminologyServerValidationSupport.setIssueSeverityForCodeDisplayMismatch(defaults.getIssueSeverityForCodeDisplayMismatch());

		TermReadSvcImpl.setInvokeOnNextCallForUnitTest(null);

		myValidationSettings.setLocalReferenceValidationDefaultPolicy(ReferenceValidationPolicy.IGNORE);
		myFhirContext.setParserErrorHandler(new StrictErrorHandler());

		myUnknownCodeSystemWarningValidationSupport.setNonExistentCodeSystemSeverity(UnknownCodeSystemWarningValidationSupport.DEFAULT_SEVERITY);
	}

	@Test
	public void testValidateCodeInValueSetWithUnknownCodeSystem_FailValidation() {
		createStructureDefWithBindingToUnknownCs(true);

		Observation obs = createObservationForUnknownCodeSystemTest();

		OperationOutcome oo;

		// Valid code
		obs.setValue(new Quantity().setSystem("http://cs").setCode("code1").setValue(123));
		oo = validateAndReturnOutcome(obs);
		String encoded = encode(oo);
		ourLog.info(encoded);
		assertThat(oo.getIssueFirstRep().getDiagnostics()).as(encoded).isEqualTo("No issues detected during validation");

		// Invalid code
		obs.setValue(new Quantity().setSystem("http://cs").setCode("code99").setValue(123));
		oo = validateAndReturnOutcome(obs);
		encoded = encode(oo);
		ourLog.info(encoded);
		assertThat(oo.getIssue().size()).as(encoded).isEqualTo(1);
		assertThat(oo.getIssue().get(0).getDiagnostics()).contains("provided (http://cs#code99) was not found in the value set");
		assertThat(oo.getIssue().get(0).getDiagnostics()).contains("Unknown code 'http://cs#code99' for in-memory expansion of ValueSet 'http://vs'");
		assertThat(oo.getIssueFirstRep().getSeverity()).as(encoded).isEqualTo(OperationOutcome.IssueSeverity.ERROR);

	}

	@Test
	public void testValidateCodeInEnumeratedValueSetWithUnknownCodeSystem_Information() {
		myUnknownCodeSystemWarningValidationSupport.setNonExistentCodeSystemSeverity(IValidationSupport.IssueSeverity.INFORMATION);

		createStructureDefWithBindingToUnknownCs(true);

		Observation obs = createObservationForUnknownCodeSystemTest();

		OperationOutcome oo;
		String encoded;

		// Valid code
		obs.setValue(new Quantity().setSystem("http://cs").setCode("code1").setValue(123));
		oo = validateAndReturnOutcome(obs, false);
		encoded = encode(oo);
		ourLog.info(encoded);
		assertThat(oo.getIssue()).hasSize(1);
		assertEquals("No issues detected during validation", oo.getIssueFirstRep().getDiagnostics());
		assertEquals(OperationOutcome.IssueSeverity.INFORMATION, oo.getIssueFirstRep().getSeverity());

		// Invalid code
		obs.setValue(new Quantity().setSystem("http://cs").setCode("code99").setValue(123));
		oo = validateAndReturnOutcome(obs, true);
		encoded = encode(oo);
		ourLog.info(encoded);
		assertThat(oo.getIssue()).hasSize(1);
		assertThat(oo.getIssueFirstRep().getDiagnostics()).contains("provided (http://cs#code99) was not found in the value set");
		assertThat(oo.getIssueFirstRep().getDiagnostics()).contains("Unknown code 'http://cs#code99' for in-memory expansion of ValueSet 'http://vs'");
		assertEquals(OperationOutcome.IssueSeverity.ERROR, oo.getIssueFirstRep().getSeverity());
	}

	/**
	 * By default, an unknown code system should fail validation
	 */
	@Test
	public void testValidateCodeInEnumeratedValueSetWithUnknownCodeSystem_Warning() {
		// set to warning
		myUnknownCodeSystemWarningValidationSupport.setNonExistentCodeSystemSeverity(IValidationSupport.IssueSeverity.WARNING);

		createStructureDefWithBindingToUnknownCs(true);

		Observation obs = createObservationForUnknownCodeSystemTest();

		OperationOutcome oo;
		String encoded;

		// Valid code
		obs.setValue(new Quantity().setSystem("http://cs").setCode("code1").setValue(123));
		oo = validateAndReturnOutcome(obs, false);
		encoded = encode(oo);
		ourLog.info(encoded);
		assertThat(oo.getIssue()).hasSize(1);
		assertEquals("CodeSystem is unknown and can't be validated: http://cs for 'http://cs#code1' (error because this is a required binding)", oo.getIssueFirstRep().getDiagnostics());
		assertEquals(OperationOutcome.IssueSeverity.WARNING, oo.getIssueFirstRep().getSeverity());

		// Invalid code
		obs.setValue(new Quantity().setSystem("http://cs").setCode("code99").setValue(123));
		oo = validateAndReturnOutcome(obs, true);
		encoded = encode(oo);
		ourLog.info(encoded);
		assertThat(oo.getIssue()).hasSize(2);
		assertThat(oo.getIssue().get(0).getDiagnostics()).contains("CodeSystem is unknown and can't be validated: http://cs for 'http://cs#code99'");
		assertEquals(OperationOutcome.IssueSeverity.WARNING, oo.getIssue().get(0).getSeverity());
		assertThat(oo.getIssue().get(1).getDiagnostics()).contains("Unknown code 'http://cs#code99' for in-memory expansion of ValueSet 'http://vs'");
		assertEquals(OperationOutcome.IssueSeverity.ERROR, oo.getIssue().get(1).getSeverity());
	}

	@Test
	public void testValidateCodeInEnumeratedValueSetWithUnknownCodeSystem_Error() {
		myUnknownCodeSystemWarningValidationSupport.setNonExistentCodeSystemSeverity(IValidationSupport.IssueSeverity.ERROR);

		createStructureDefWithBindingToUnknownCs(true);

		Observation obs = new Observation();
		obs.getMeta().addProfile("http://sd");
		obs.getText().setDivAsString("<div>Hello</div>");
		obs.getText().setStatus(Narrative.NarrativeStatus.GENERATED);
		obs.getCategoryFirstRep().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/observation-category").setCode("vital-signs");
		obs.getCode().setText("hello");
		obs.setSubject(new Reference("Patient/123"));
		obs.addPerformer(new Reference("Practitioner/123"));
		obs.setEffective(DateTimeType.now());
		obs.setStatus(ObservationStatus.FINAL);

		OperationOutcome oo;
		String encoded;

		// Valid code
		obs.setValue(new Quantity().setSystem("http://cs").setCode("code1").setValue(123));
		oo = validateAndReturnOutcome(obs, false);
		encoded = encode(oo);
		ourLog.info(encoded);
		assertThat(oo.getIssue()).hasSize(1);
		assertThat(oo.getIssueFirstRep().getDiagnostics()).contains("No issues detected during validation");
		assertEquals(OperationOutcome.IssueSeverity.INFORMATION, oo.getIssueFirstRep().getSeverity());

		// Invalid code
		obs.setValue(new Quantity().setSystem("http://cs").setCode("code99").setValue(123));
		oo = validateAndReturnOutcome(obs, true);
		encoded = encode(oo);
		ourLog.info(encoded);
		assertThat(oo.getIssue()).hasSize(1);
		assertThat(oo.getIssue().get(0).getDiagnostics()).contains("provided (http://cs#code99) was not found in the value set");
		assertEquals(OperationOutcome.IssueSeverity.ERROR, oo.getIssueFirstRep().getSeverity());
	}


	@Test
	public void testValidateCodeInNonEnumeratedValueSetWithUnknownCodeSystem_Information() {
		myUnknownCodeSystemWarningValidationSupport.setNonExistentCodeSystemSeverity(IValidationSupport.IssueSeverity.INFORMATION);

		createStructureDefWithBindingToUnknownCs(false);

		Observation obs = createObservationForUnknownCodeSystemTest();

		OperationOutcome oo;
		String encoded;

		// Valid code
		obs.setValue(new Quantity().setSystem("http://cs").setCode("code1").setValue(123));
		oo = validateAndReturnOutcome(obs, false);
		encoded = encode(oo);
		ourLog.info(encoded);
		assertThat(oo.getIssue()).hasSize(1);
		assertEquals("No issues detected during validation", oo.getIssueFirstRep().getDiagnostics());
		assertEquals(OperationOutcome.IssueSeverity.INFORMATION, oo.getIssueFirstRep().getSeverity());

		// Invalid code
		obs.setValue(new Quantity().setSystem("http://cs").setCode("code99").setValue(123));
		oo = validateAndReturnOutcome(obs, false);
		encoded = encode(oo);
		ourLog.info(encoded);
		assertThat(oo.getIssue()).hasSize(1);
		assertEquals("No issues detected during validation", oo.getIssueFirstRep().getDiagnostics());
		assertEquals(OperationOutcome.IssueSeverity.INFORMATION, oo.getIssueFirstRep().getSeverity());
	}

	/**
	 * By default, an unknown code system should fail validation
	 */
	@Test
	public void testValidateCodeInNonEnumeratedValueSetWithUnknownCodeSystem_Warning() {
		// set to warning
		myUnknownCodeSystemWarningValidationSupport.setNonExistentCodeSystemSeverity(IValidationSupport.IssueSeverity.WARNING);

		createStructureDefWithBindingToUnknownCs(false);

		Observation obs = createObservationForUnknownCodeSystemTest();

		OperationOutcome oo;
		String encoded;

		// Valid code
		obs.setValue(new Quantity().setSystem("http://cs").setCode("code1").setValue(123));
		oo = validateAndReturnOutcome(obs, false);
		encoded = encode(oo);
		ourLog.info(encoded);
		assertThat(oo.getIssue()).hasSize(1);
		assertEquals("CodeSystem is unknown and can't be validated: http://cs for 'http://cs#code1' (error because this is a required binding)", oo.getIssueFirstRep().getDiagnostics());
		assertEquals(OperationOutcome.IssueSeverity.WARNING, oo.getIssueFirstRep().getSeverity());

		// Invalid code
		obs.setValue(new Quantity().setSystem("http://cs").setCode("code99").setValue(123));
		oo = validateAndReturnOutcome(obs, false);
		encoded = encode(oo);
		ourLog.info(encoded);
		assertThat(oo.getIssue()).hasSize(1);
		assertEquals("CodeSystem is unknown and can't be validated: http://cs for 'http://cs#code99' (error because this is a required binding)", oo.getIssue().get(0).getDiagnostics());
		assertEquals(OperationOutcome.IssueSeverity.WARNING, oo.getIssue().get(0).getSeverity());
	}

	@Test
	public void testValidateCodeInNonEnumeratedValueSetWithUnknownCodeSystem_Error() {
		myUnknownCodeSystemWarningValidationSupport.setNonExistentCodeSystemSeverity(IValidationSupport.IssueSeverity.ERROR);

		createStructureDefWithBindingToUnknownCs(false);

		Observation obs = new Observation();
		obs.getMeta().addProfile("http://sd");
		obs.getText().setDivAsString("<div>Hello</div>");
		obs.getText().setStatus(Narrative.NarrativeStatus.GENERATED);
		obs.getCategoryFirstRep().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/observation-category").setCode("vital-signs");
		obs.getCode().setText("hello");
		obs.setSubject(new Reference("Patient/123"));
		obs.addPerformer(new Reference("Practitioner/123"));
		obs.setEffective(DateTimeType.now());
		obs.setStatus(ObservationStatus.FINAL);

		OperationOutcome oo;
		String encoded;

		// Valid code
		obs.setValue(new Quantity().setSystem("http://cs").setCode("code1").setValue(123));
		oo = validateAndReturnOutcome(obs, true);
		encoded = encode(oo);
		ourLog.info(encoded);
		assertThat(oo.getIssue()).hasSize(2);
		OperationOutcome.OperationOutcomeIssueComponent unableToExpandError = oo.getIssue().get(0);
		assertThat(unableToExpandError.getDiagnostics()).contains("Unable to expand ValueSet because CodeSystem could not be found: http://cs");
		assertEquals(OperationOutcome.IssueSeverity.ERROR, oo.getIssueFirstRep().getSeverity());

		OperationOutcome.OperationOutcomeIssueComponent notInValueSetError = oo.getIssue().get(1);
		assertThat(notInValueSetError.getDiagnostics()).contains("provided (http://cs#code1) was not found in the value set");
		assertThat(notInValueSetError.getDiagnostics()).contains("Failed to expand ValueSet 'http://vs' (in-memory). Could not validate code http://cs#code1");
		assertThat(notInValueSetError.getDiagnostics()).contains("HAPI-0702: Unable to expand ValueSet because CodeSystem could not be found: http://cs");
		assertEquals(OperationOutcome.IssueSeverity.ERROR, oo.getIssueFirstRep().getSeverity());
		assertEquals(27, ((IntegerType) notInValueSetError.getExtensionByUrl("http://hl7.org/fhir/StructureDefinition/operationoutcome-issue-line").getValue()).getValue());
		assertEquals(4, ((IntegerType) notInValueSetError.getExtensionByUrl("http://hl7.org/fhir/StructureDefinition/operationoutcome-issue-col").getValue()).getValue());
		assertEquals("Terminology_TX_NoValid_12", ((StringType) notInValueSetError.getExtensionByUrl("http://hl7.org/fhir/StructureDefinition/operationoutcome-message-id").getValue()).getValue());
		assertEquals(OperationOutcome.IssueType.PROCESSING, notInValueSetError.getCode());
		assertEquals(OperationOutcome.IssueSeverity.ERROR, notInValueSetError.getSeverity());
		assertThat(notInValueSetError.getLocation()).hasSize(2);
		assertEquals("Observation.value.ofType(Quantity)", notInValueSetError.getLocation().get(0).getValue());
		assertEquals("Line[27] Col[4]", notInValueSetError.getLocation().get(1).getValue());


	}


	private Observation createObservationForUnknownCodeSystemTest() {
		Observation obs = new Observation();
		obs.getMeta().addProfile("http://sd");
		obs.getText().setDivAsString("<div>Hello</div>");
		obs.getText().setStatus(Narrative.NarrativeStatus.GENERATED);
		obs.getCategoryFirstRep().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/observation-category").setCode("vital-signs");
		obs.getCode().setText("hello");
		obs.setSubject(new Reference("Patient/123"));
		obs.addPerformer(new Reference("Practitioner/123"));
		obs.setEffective(DateTimeType.now());
		obs.setStatus(ObservationStatus.FINAL);
		return obs;
	}

	@Test
	public void testValidateCodeInValueSet_InferredCodeSystem_WarningOnUnknown() {
		// set to warning
		myUnknownCodeSystemWarningValidationSupport.setNonExistentCodeSystemSeverity(IValidationSupport.IssueSeverity.WARNING);

		OperationOutcome oo;
		String encoded;

		Binary binary = new Binary();
		binary.setContentType("application/text");
		binary.setContent("hello".getBytes(StandardCharsets.UTF_8));

		// Valid code
		oo = validateAndReturnOutcome(binary);
		encoded = encode(oo);
		ourLog.info(encoded);
		assertThat(oo.getIssueFirstRep().getDiagnostics()).contains("No issues detected during validation");

	}

	@Test
	public void testValidateCodeInValueSet_InferredCodeSystem_ErrorOnUnknown() {
		// set to warning
		myUnknownCodeSystemWarningValidationSupport.setNonExistentCodeSystemSeverity(IValidationSupport.IssueSeverity.ERROR);

		OperationOutcome oo;
		String encoded;

		Binary binary = new Binary();
		binary.setContentType("application/text");
		binary.setContent("hello".getBytes(StandardCharsets.UTF_8));

		// Valid code
		oo = validateAndReturnOutcome(binary);
		encoded = encode(oo);
		ourLog.info(encoded);
		assertThat(oo.getIssueFirstRep().getDiagnostics()).contains("No issues detected during validation");

	}


	public void createStructureDefWithBindingToUnknownCs(boolean theEnumeratedCodeSystem) {
		myValidationSupport.fetchCodeSystem("http://not-exist"); // preload DefaultProfileValidationSupport

		ValueSet vs = new ValueSet();
		vs.setUrl("http://vs");
		ValueSet.ConceptSetComponent include = vs
			.getCompose()
			.addInclude()
			.setSystem("http://cs");
		if (theEnumeratedCodeSystem) {
			include.addConcept(new ValueSet.ConceptReferenceComponent(new CodeType("code1")));
			include.addConcept(new ValueSet.ConceptReferenceComponent(new CodeType("code2")));
		}
		myValueSetDao.create(vs);

		StructureDefinition sd = new StructureDefinition();
		sd.setDerivation(StructureDefinition.TypeDerivationRule.CONSTRAINT);
		sd.setType("Observation");
		sd.setUrl("http://sd");
		sd.setBaseDefinition("http://hl7.org/fhir/StructureDefinition/Observation");
		sd.getDifferential()
			.addElement()
			.setPath("Observation.value[x]")
			.addType(new ElementDefinition.TypeRefComponent(new UriType("Quantity")))
			.setBinding(new ElementDefinition.ElementDefinitionBindingComponent().setStrength(Enumerations.BindingStrength.REQUIRED).setValueSet("http://vs"))
			.setId("Observation.value[x]");
		myStructureDefinitionDao.create(sd);
	}

	@Test
	public void testGenerateSnapshotOnStructureDefinitionWithNoBase() {

		// No base populated here, which isn't valid
		StructureDefinition sd = new StructureDefinition();
		sd.setDerivation(StructureDefinition.TypeDerivationRule.CONSTRAINT);
		sd.setUrl("http://sd");
		sd.getDifferential()
			.addElement()
			.setPath("Observation.value[x]")
			.addType(new ElementDefinition.TypeRefComponent(new UriType("string")))
			.setId("Observation.value[x]");

		try {
			myStructureDefinitionDao.generateSnapshot(sd, null, null, null);
			fail();
		} catch (PreconditionFailedException e) {
			assertEquals(Msg.code(704) + "StructureDefinition[id=null, url=http://sd] has no base", e.getMessage());
		}

		myStructureDefinitionDao.create(sd);

		Observation obs = new Observation();
		obs.getMeta().addProfile("http://sd");
		obs.getText().setDivAsString("<div>Hello</div>");
		obs.getText().setStatus(Narrative.NarrativeStatus.GENERATED);
		obs.getCategoryFirstRep().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/observation-category").setCode("vital-signs");
		obs.getCode().setText("hello");
		obs.setSubject(new Reference("Patient/123"));
		obs.addPerformer(new Reference("Practitioner/123"));
		obs.setEffective(DateTimeType.now());
		obs.setStatus(ObservationStatus.FINAL);

		// Valid code
		obs.setValue(new Quantity().setSystem("http://cs").setCode("code1").setValue(123));
		try {
			myObservationDao.validate(obs, null, null, null, ValidationModeEnum.CREATE, null, mySrd);
			fail();
		} catch (PreconditionFailedException e) {
			assertEquals(Msg.code(704) + "StructureDefinition[id=null, url=http://sd] has no base", e.getMessage());
		}
	}

	/**
	 * Use a valueset that explicitly brings in some UCUM codes
	 */
	@Test
	public void testValidateCodeInValueSetWithBuiltInCodeSystem() throws IOException {
		myValueSetDao.create(loadResourceFromClasspath(ValueSet.class, "/r4/bl/bb-vs.json"));
		myStructureDefinitionDao.create(loadResourceFromClasspath(StructureDefinition.class, "/r4/bl/bb-sd.json"));

		runInTransaction(() -> {
			TermValueSet vs = myTermValueSetDao.findByUrl("https://bb/ValueSet/BBDemographicAgeUnit").orElseThrow(() -> new IllegalArgumentException());
			assertEquals(TermValueSetPreExpansionStatusEnum.NOT_EXPANDED, vs.getExpansionStatus());
		});

		OperationOutcome outcome;

		// Use a code that's in the ValueSet
		{
			outcome = (OperationOutcome) myObservationDao.validate(loadResourceFromClasspath(Observation.class, "/r4/bl/bb-obs-code-in-valueset.json"), null, null, null, null, null, mySrd).getOperationOutcome();
			String outcomeStr = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome);
			ourLog.info("Validation outcome: {}", outcomeStr);
			assertThat(outcomeStr).doesNotContain("\"error\"");
		}

		// Use a code that's not in the ValueSet
		outcome = (OperationOutcome) myObservationDao.validate(loadResourceFromClasspath(Observation.class, "/r4/bl/bb-obs-code-not-in-valueset.json"), null, null, null, null, null, mySrd).getOperationOutcome();
		assertHasErrors(outcome);
		String outcomeStr = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome);
		ourLog.info("Validation outcome: {}", outcomeStr);
		assertThat(outcomeStr).contains("provided (http://unitsofmeasure.org#cm) was not found in the value set");

		// Before, the VS wasn't pre-expanded. Try again with it pre-expanded
		runInTransaction(() -> {
			TermValueSet vs = myTermValueSetDao.findByUrl("https://bb/ValueSet/BBDemographicAgeUnit").orElseThrow(() -> new IllegalArgumentException());
			assertEquals(TermValueSetPreExpansionStatusEnum.NOT_EXPANDED, vs.getExpansionStatus());
		});

		myTermReadSvc.preExpandDeferredValueSetsToTerminologyTables();

		runInTransaction(() -> {
			TermValueSet vs = myTermValueSetDao.findByUrl("https://bb/ValueSet/BBDemographicAgeUnit").orElseThrow(() -> new IllegalArgumentException());
			assertEquals(TermValueSetPreExpansionStatusEnum.EXPANDED, vs.getExpansionStatus());
		});

		// Use a code that's in the ValueSet
		{
			outcome = (OperationOutcome) myObservationDao.validate(loadResourceFromClasspath(Observation.class, "/r4/bl/bb-obs-code-in-valueset.json"), null, null, null, null, null, mySrd).getOperationOutcome();
			outcomeStr = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome);
			ourLog.info("Validation outcome: {}", outcomeStr);
			assertThat(outcomeStr).doesNotContain("\"error\"");
		}

		// Use a code that's not in the ValueSet
		outcome = (OperationOutcome) myObservationDao.validate(loadResourceFromClasspath(Observation.class, "/r4/bl/bb-obs-code-not-in-valueset.json"), null, null, null, null, null, mySrd).getOperationOutcome();
		assertHasErrors(outcome);
		outcomeStr = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome);
		ourLog.info("Validation outcome: {}", outcomeStr);
		assertThat(outcomeStr).contains("provided (http://unitsofmeasure.org#cm) was not found in the value set");

	}


	@Test
	public void testValidateCodeUsingQuantityBinding() throws IOException {
		myValueSetDao.create(loadResourceFromClasspath(ValueSet.class, "/r4/bl/bb-vs.json"));
		myStructureDefinitionDao.create(loadResourceFromClasspath(StructureDefinition.class, "/r4/bl/bb-sd.json"));

		runInTransaction(() -> {
			TermValueSet vs = myTermValueSetDao.findByUrl("https://bb/ValueSet/BBDemographicAgeUnit").orElseThrow(() -> new IllegalArgumentException());
			assertEquals(TermValueSetPreExpansionStatusEnum.NOT_EXPANDED, vs.getExpansionStatus());
		});

		OperationOutcome outcome;

		// Use the wrong datatype
		myFhirContext.setParserErrorHandler(new LenientErrorHandler());
		Observation resource = loadResourceFromClasspath(Observation.class, "/r4/bl/bb-obs-value-is-not-quantity2.json");
		outcome = (OperationOutcome) myObservationDao.validate(resource, null, null, null, null, null, mySrd).getOperationOutcome();
		assertHasErrors(outcome);
		String outcomeStr = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome);
		ourLog.info("Validation outcome: {}", outcomeStr);
		assertThat(outcomeStr).contains("\"error\"");

		// Use the wrong datatype
		myFhirContext.setParserErrorHandler(new LenientErrorHandler());
		resource = loadResourceFromClasspath(Observation.class, "/r4/bl/bb-obs-value-is-not-quantity.json");
		outcome = (OperationOutcome) myObservationDao.validate(resource, null, null, null, null, null, mySrd).getOperationOutcome();
		assertHasErrors(outcome);
		outcomeStr = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome);
		ourLog.info("Validation outcome: {}", outcomeStr);
		assertThat(outcomeStr).contains("The Profile 'https://bb/StructureDefinition/BBDemographicAge' definition allows for the type Quantity but found type string");
	}

	/**
	 * Create a loinc valueset that expands to more results than the expander is willing to do
	 * in memory, and make sure we can still validate correctly, even if we're using
	 * the in-memory expander
	 */
	@Test
	public void testValidateCode_InMemoryExpansionAgainstHugeValueSet() throws Exception {
		myStorageSettings.setPreExpandValueSets(false);

		ValueSet vs = new ValueSet();
		vs.setUrl("http://example.com/fhir/ValueSet/observation-vitalsignresult");
		vs.getCompose().addInclude().setSystem("http://loinc.org");
		myValueSetDao.create(vs);

		assertFalse(myTermReadSvc.isValueSetPreExpandedForCodeValidation(vs));

		// Load the profile, which is just the Vital Signs profile modified to accept all loinc codes
		// and not just certain ones
		StructureDefinition profile = loadResourceFromClasspath(StructureDefinition.class, "/r4/profile-vitalsigns-all-loinc.json");
		myStructureDefinitionDao.create(profile, mySrd);

		// Add a bunch of codes
		CustomTerminologySet codesToAdd = new CustomTerminologySet();
		for (int i = 0; i < 100; i++) {
			codesToAdd.addRootConcept("CODE" + i, "Display " + i);
		}
		myTermCodeSystemStorageSvc.applyDeltaCodeSystemsAdd("http://loinc.org", codesToAdd);

		myStorageSettings.setMaximumExpansionSize(50);

		Observation obs = new Observation();
		obs.getMeta().addProfile("http://example.com/fhir/StructureDefinition/vitalsigns-2");
		obs.getText().setDivAsString("<div>Hello</div>");
		obs.getCategoryFirstRep().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/observation-category").setCode("vital-signs");
		obs.setSubject(new Reference("Patient/123"));
		obs.addPerformer(new Reference("Practitioner/123"));
		obs.setEffective(DateTimeType.now());
		obs.setStatus(ObservationStatus.FINAL);
		obs.setValue(new StringType("This is the value"));

		OperationOutcome oo;

		// Valid code
		obs.getText().setStatus(Narrative.NarrativeStatus.GENERATED);
		obs.getCode().getCodingFirstRep().setSystem("http://loinc.org").setCode("CODE3").setDisplay("Display 3");
		oo = validateAndReturnOutcome(obs);
		assertThat(oo.getIssueFirstRep().getDiagnostics()).as(encode(oo)).isEqualTo("No issues detected during validation");

		// Invalid code
		obs.getText().setStatus(Narrative.NarrativeStatus.GENERATED);
		obs.getCode().getCodingFirstRep().setSystem("http://loinc.org").setCode("non-existing-code").setDisplay("Display 3");
		oo = validateAndReturnOutcome(obs);
		assertThat(oo.getIssue().get(1).getDiagnostics()).as(encode(oo)).isEqualTo("None of the codings provided are in the value set 'ValueSet[http://example.com/fhir/ValueSet/observation-vitalsignresult]' (http://example.com/fhir/ValueSet/observation-vitalsignresult), and a coding from this value set is required) (codes = http://loinc.org#non-existing-code)");

		// Valid code with no system
		obs.getText().setStatus(Narrative.NarrativeStatus.GENERATED);
		obs.getCode().getCodingFirstRep().setSystem(null).setCode("CODE3").setDisplay("Display 3");
		oo = validateAndReturnOutcome(obs);
		assertThat(encode(oo)).contains("None of the codings provided are in the value set 'ValueSet[http://example.com/fhir/ValueSet/observation-vitalsignresult]' (http://example.com/fhir/ValueSet/observation-vitalsignresult)");

		// Valid code with wrong system
		obs.getText().setStatus(Narrative.NarrativeStatus.GENERATED);
		obs.getCode().getCodingFirstRep().setSystem("http://foo").setCode("CODE3").setDisplay("Display 3");
		oo = validateAndReturnOutcome(obs);
		assertThat(oo.getIssueFirstRep().getDiagnostics()).as(encode(oo)).isEqualTo("None of the codings provided are in the value set 'ValueSet[http://example.com/fhir/ValueSet/observation-vitalsignresult]' (http://example.com/fhir/ValueSet/observation-vitalsignresult), and a coding from this value set is required) (codes = http://foo#CODE3)");

		// Code that exists but isn't in the valueset
		obs.getText().setStatus(Narrative.NarrativeStatus.GENERATED);
		obs.getCode().getCodingFirstRep().setSystem("http://terminology.hl7.org/CodeSystem/observation-category").setCode("vital-signs").setDisplay("Vital Signs");
		oo = validateAndReturnOutcome(obs);
		assertThat(oo.getIssueFirstRep().getDiagnostics()).as(encode(oo)).isEqualTo("None of the codings provided are in the value set 'ValueSet[http://example.com/fhir/ValueSet/observation-vitalsignresult]' (http://example.com/fhir/ValueSet/observation-vitalsignresult), and a coding from this value set is required) (codes = http://terminology.hl7.org/CodeSystem/observation-category#vital-signs)");

		// Invalid code in built-in VS/CS
		obs.getText().setStatus(Narrative.NarrativeStatus.GENERATED);
		obs.getCode().getCodingFirstRep().setSystem("http://loinc.org").setCode("CODE3").setDisplay("Display 3");
		obs.getCategoryFirstRep().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/observation-category").setCode("FOO");
		oo = validateAndReturnOutcome(obs);
		assertThat(oo.getIssueFirstRep().getDiagnostics()).as(encode(oo)).isEqualTo("Unknown code 'http://terminology.hl7.org/CodeSystem/observation-category#FOO'");

		// Make sure we're caching the validations as opposed to hitting the DB every time
		myCaptureQueriesListener.clear();
		obs.getText().setStatus(Narrative.NarrativeStatus.GENERATED);
		obs.getCode().getCoding().clear();
		obs.getCategory().clear();
		obs.getCategoryFirstRep().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/observation-category").setCode("vital-signs");
		obs.getCode().getCodingFirstRep().setSystem("http://loinc.org").setCode("CODE4").setDisplay("Display 4");
		oo = validateAndReturnOutcome(obs);
		assertThat(oo.getIssueFirstRep().getDiagnostics()).as(encode(oo)).isEqualTo("No issues detected during validation");
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();

		myCaptureQueriesListener.clear();
		obs.getText().setStatus(Narrative.NarrativeStatus.GENERATED);
		obs.getCode().getCodingFirstRep().setSystem("http://loinc.org").setCode("CODE4").setDisplay("Display 4");
		oo = validateAndReturnOutcome(obs);
		assertThat(oo.getIssueFirstRep().getDiagnostics()).as(encode(oo)).isEqualTo("No issues detected during validation");
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();


	}

	@Test
	public void testValidateProfileTargetType_PolicyCheckValid() throws IOException {
		myValidationSettings.setLocalReferenceValidationDefaultPolicy(ReferenceValidationPolicy.CHECK_VALID);

		StructureDefinition profile = loadResourceFromClasspath(StructureDefinition.class, "/r4/profile-vitalsigns-all-loinc.json");
		myStructureDefinitionDao.create(profile, mySrd);

		ValueSet vs = new ValueSet();
		vs.setUrl("http://example.com/fhir/ValueSet/observation-vitalsignresult");
		vs.getCompose().addInclude().setSystem("http://loinc.org");
		myValueSetDao.create(vs);

		CodeSystem cs = new CodeSystem();
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		cs.setUrl("http://loinc.org");
		cs.addConcept().setCode("123-4").setDisplay("Code 123 4");
		cs.setId(LOINC_LOW);
		myCodeSystemDao.update(cs);

		Group group = new Group();
		group.setId("ABC");
		group.setActive(true);
		myGroupDao.update(group);

		Patient patient = new Patient();
		patient.getText().setStatus(Narrative.NarrativeStatus.GENERATED).setDivAsString("<div>Hello</div>");
		patient.setId("DEF");
		patient.setActive(true);
		myPatientDao.update(patient);

		Practitioner practitioner = new Practitioner();
		practitioner.setId("P");
		practitioner.setActive(true);
		myPractitionerDao.update(practitioner);

		Observation obs = new Observation();
		obs.getMeta().addProfile("http://example.com/fhir/StructureDefinition/vitalsigns-2");
		obs.getText().setStatus(Narrative.NarrativeStatus.GENERATED).setDivAsString("<div>Hello</div>");
		obs.getCategoryFirstRep().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/observation-category").setCode("vital-signs");
		obs.addPerformer(new Reference("Practitioner/P"));
		obs.setEffective(DateTimeType.now());
		obs.setStatus(ObservationStatus.FINAL);
		obs.setValue(new StringType("This is the value"));
		obs.getText().setStatus(Narrative.NarrativeStatus.GENERATED);
		obs.getCode().getCodingFirstRep().setSystem("http://loinc.org").setCode("123-4").setDisplay("Code 123 4");

		OperationOutcome oo;

		// Non-existent target
		obs.setSubject(new Reference("Group/123"));
		oo = validateAndReturnOutcome(obs);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
		Coding expectedIssueCode = new Coding();
		expectedIssueCode.setSystem(JAVA_VALIDATOR_DETAILS_SYSTEM).setCode(I18nConstants.REFERENCE_REF_CANTRESOLVE);
		assertThat(expectedIssueCode.equalsDeep(oo.getIssueFirstRep().getDetails().getCodingFirstRep())).as(encode(oo)).isTrue();
		assertThat(oo.getIssueFirstRep().getDiagnostics()).contains(obs.getSubject().getReference());

		// Target of wrong type
		obs.setSubject(new Reference("Group/ABC"));
		oo = validateAndReturnOutcome(obs);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
		assertThat(oo.getIssueFirstRep().getDiagnostics()).as(encode(oo)).isEqualTo("Invalid Resource target type. Found Group, but expected one of ([Patient])");

		// Target of right type
		obs.setSubject(new Reference("Patient/DEF"));
		oo = validateAndReturnOutcome(obs);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
		assertThat(oo.getIssueFirstRep().getDiagnostics()).as(encode(oo)).isEqualTo("No issues detected during validation");

	}

	@Test
	public void testValidateProfileTargetType_PolicyCheckExistsAndType() throws IOException {
		myValidationSettings.setLocalReferenceValidationDefaultPolicy(ReferenceValidationPolicy.CHECK_EXISTS_AND_TYPE);

		StructureDefinition profile = loadResourceFromClasspath(StructureDefinition.class, "/r4/profile-vitalsigns-all-loinc.json");
		myStructureDefinitionDao.create(profile, mySrd);

		ValueSet vs = new ValueSet();
		vs.setUrl("http://example.com/fhir/ValueSet/observation-vitalsignresult");
		vs.getCompose().addInclude().setSystem("http://loinc.org");
		myValueSetDao.create(vs);

		CodeSystem cs = new CodeSystem();
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		cs.setUrl("http://loinc.org");
		cs.addConcept().setCode("123-4").setDisplay("Code 123 4");
		cs.setId(LOINC_LOW);
		myCodeSystemDao.update(cs);

		Group group = new Group();
		group.setId("ABC");
		group.setActive(true);
		myGroupDao.update(group);

		Patient patient = new Patient();
		patient.setId("DEF");
		patient.setActive(true);
		myPatientDao.update(patient);

		Practitioner practitioner = new Practitioner();
		practitioner.setId("P");
		practitioner.setActive(true);
		myPractitionerDao.update(practitioner);

		Observation obs = new Observation();
		obs.getMeta().addProfile("http://example.com/fhir/StructureDefinition/vitalsigns-2");
		obs.getText().setDivAsString("<div>Hello</div>");
		obs.getCategoryFirstRep().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/observation-category").setCode("vital-signs");
		obs.addPerformer(new Reference("Practitioner/P"));
		obs.setEffective(DateTimeType.now());
		obs.setStatus(ObservationStatus.FINAL);
		obs.setValue(new StringType("This is the value"));
		obs.getText().setStatus(Narrative.NarrativeStatus.GENERATED);
		obs.getCode().getCodingFirstRep().setSystem("http://loinc.org").setCode("123-4").setDisplay("Code 123 4");

		OperationOutcome oo;

		// Non-existent target
		obs.setSubject(new Reference("Group/123"));
		oo = validateAndReturnOutcome(obs);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
		Coding expectedIssueCode = new Coding();
		expectedIssueCode.setSystem(JAVA_VALIDATOR_DETAILS_SYSTEM).setCode(I18nConstants.REFERENCE_REF_CANTRESOLVE);
		assertThat(expectedIssueCode.equalsDeep(oo.getIssueFirstRep().getDetails().getCodingFirstRep())).as(encode(oo)).isTrue();
		assertThat(oo.getIssueFirstRep().getDiagnostics()).contains(obs.getSubject().getReference());

		// Target of wrong type
		obs.setSubject(new Reference("Group/ABC"));
		oo = validateAndReturnOutcome(obs);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
		assertThat(oo.getIssueFirstRep().getDiagnostics()).as(encode(oo)).isEqualTo("Unable to find a match for profile Group/ABC (by type) among choices: ; [CanonicalType[http://hl7.org/fhir/StructureDefinition/Patient]]");

		// Target of right type
		obs.setSubject(new Reference("Patient/DEF"));
		oo = validateAndReturnOutcome(obs);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
		assertThat(oo.getIssueFirstRep().getDiagnostics()).as(encode(oo)).isEqualTo("No issues detected during validation");

	}


	@Test
	public void testValidateProfileTargetType_PolicyCheckExists() throws IOException {
		myValidationSettings.setLocalReferenceValidationDefaultPolicy(ReferenceValidationPolicy.CHECK_EXISTS);

		StructureDefinition profile = loadResourceFromClasspath(StructureDefinition.class, "/r4/profile-vitalsigns-all-loinc.json");
		myStructureDefinitionDao.create(profile, mySrd);

		ValueSet vs = new ValueSet();
		vs.setUrl("http://example.com/fhir/ValueSet/observation-vitalsignresult");
		vs.getCompose().addInclude().setSystem("http://loinc.org");
		myValueSetDao.create(vs);

		CodeSystem cs = new CodeSystem();
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		cs.setUrl("http://loinc.org");
		cs.addConcept().setCode("123-4").setDisplay("Code 123 4");
		cs.setId(LOINC_LOW);
		myCodeSystemDao.update(cs);

		Group group = new Group();
		group.setId("ABC");
		group.setActive(true);
		myGroupDao.update(group);

		Patient patient = new Patient();
		patient.setId("DEF");
		patient.setActive(true);
		myPatientDao.update(patient);

		Practitioner practitioner = new Practitioner();
		practitioner.setId("P");
		practitioner.setActive(true);
		myPractitionerDao.update(practitioner);

		Observation obs = new Observation();
		obs.getMeta().addProfile("http://example.com/fhir/StructureDefinition/vitalsigns-2");
		obs.getText().setDivAsString("<div>Hello</div>");
		obs.getCategoryFirstRep().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/observation-category").setCode("vital-signs");
		obs.addPerformer(new Reference("Practitioner/P"));
		obs.setEffective(DateTimeType.now());
		obs.setStatus(ObservationStatus.FINAL);
		obs.setValue(new StringType("This is the value"));
		obs.getText().setStatus(Narrative.NarrativeStatus.GENERATED);
		obs.getCode().getCodingFirstRep().setSystem("http://loinc.org").setCode("123-4").setDisplay("Code 123 4");

		// Non-existent target
		obs.setSubject(new Reference("Group/123"));
		OperationOutcome oo = validateAndReturnOutcome(obs);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
		Coding expectedIssueCode = new Coding();
		expectedIssueCode.setSystem(JAVA_VALIDATOR_DETAILS_SYSTEM).setCode(I18nConstants.REFERENCE_REF_CANTRESOLVE);
		assertThat(expectedIssueCode.equalsDeep(oo.getIssueFirstRep().getDetails().getCodingFirstRep())).as(encode(oo)).isTrue();
		assertThat(oo.getIssueFirstRep().getDiagnostics()).contains(obs.getSubject().getReference());

		// Target of wrong type
		obs.setSubject(new Reference("Group/ABC"));
		oo = validateAndReturnOutcome(obs);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
		assertThat(oo.getIssueFirstRep().getDiagnostics()).as(encode(oo)).isEqualTo("No issues detected during validation");

		// Target of right type
		obs.setSubject(new Reference("Patient/DEF"));
		oo = validateAndReturnOutcome(obs);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
		assertThat(oo.getIssueFirstRep().getDiagnostics()).as(encode(oo)).isEqualTo("No issues detected during validation");

	}


	@Test
	public void testValidateValueSet() {
		String input = "{\n" +
			"  \"resourceType\": \"ValueSet\",\n" +
			"  \"meta\": {\n" +
			"    \"profile\": [\n" +
			"      \"https://foo\"\n" +
			"    ]\n" +
			"  },\n" +
			"  \"text\": {\n" +
			"    \"status\": \"generated\",\n" +
			"    \"div\": \"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\">HELLO</div>\"\n" +
			"  },\n" +
			"  \"url\": \"https://foo/bb\",\n" +
			"  \"name\": \"BBBehaviourType\",\n" +
			"  \"title\": \"BBBehaviour\",\n" +
			"  \"status\": \"draft\",\n" +
			"  \"version\": \"20190731\",\n" +
			"  \"experimental\": false,\n" +
			"  \"description\": \"alcohol habits.\",\n" +
			"  \"publisher\": \"BB\",\n" +
			"  \"immutable\": false,\n" +
			"  \"compose\": {\n" +
			"    \"include\": [\n" +
			"      {\n" +
			"        \"system\": \"https://bb\",\n" +
			"        \"concept\": [\n" +
			"          {\n" +
			"            \"code\": \"123\",\n" +
			"            \"display\": \"Current drinker\"\n" +
			"          },\n" +
			"          {\n" +
			"            \"code\": \"456\",\n" +
			"            \"display\": \"Ex-drinker\"\n" +
			"          },\n" +
			"          {\n" +
			"            \"code\": \"789\",\n" +
			"            \"display\": \"Lifetime non-drinker (finding)\"\n" +
			"          }\n" +
			"        ]\n" +
			"      }\n" +
			"    ]\n" +
			"  }\n" +
			"}";

		ValueSet vs = myFhirContext.newJsonParser().parseResource(ValueSet.class, input);
		OperationOutcome oo = validateAndReturnOutcome(vs);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));

		assertEquals("The code '123' is not valid in the system https://bb (Validation failed)", oo.getIssue().get(0).getDiagnostics());
	}

	@Test
	public void testValidateWithFragmentCodeSystem_NoDirectBinding() throws IOException {
		myCodeSystemDao.create(loadResourceFromClasspath(CodeSystem.class, "/r4/fragment/codesystem.json"));

		Location location = new Location();
		location.getPhysicalType().addCoding()
			.setSystem("http://example.com/codesystem")
			.setCode("foo")
			.setDisplay("Foo Code");

		MethodOutcome outcome = myLocationDao.validate(location, null, null, null, ValidationModeEnum.CREATE, null, null);

		OperationOutcome oo = (OperationOutcome) outcome.getOperationOutcome();
		String ooString = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo);
		ourLog.info(ooString);

		assertThat(ooString).contains("Unknown code in fragment CodeSystem 'http://example.com/codesystem#foo'");

		assertThat(oo.getIssue().stream().map(t -> t.getSeverity().toCode()).collect(Collectors.toList())).containsExactly("warning", "warning");
	}


	/**
	 * Per: https://chat.fhir.org/#narrow/stream/179166-implementers/topic/Handling.20incomplete.20CodeSystems
	 * <p>
	 * We should generate a warning if a code can't be found but the codesystem is a fragment
	 */
	@Test
	public void testValidateWithFragmentCodeSystem_WithDirectBinding() throws IOException {
		myStructureDefinitionDao.create(loadResourceFromClasspath(StructureDefinition.class, "/r4/fragment/structuredefinition.json"));
		myCodeSystemDao.create(loadResourceFromClasspath(CodeSystem.class, "/r4/fragment/codesystem.json"));
		myValueSetDao.create(loadResourceFromClasspath(ValueSet.class, "/r4/fragment/valueset.json"));

		createPatient(withId("A"), withActiveTrue());

		Observation obs = new Observation();
		obs.setStatus(ObservationStatus.FINAL);
		obs.getSubject().setReference("Patient/A");
		obs.getText().setStatus(Narrative.NarrativeStatus.GENERATED);
		obs.getText().getDiv().setValue("<div>hello</div>");
		obs.setValue(new StringType("hello"));
		obs.getPerformerFirstRep().setReference("Patient/A");
		obs.setEffective(new DateTimeType("2020-01-01"));

		OperationOutcome outcome;

		// Correct codesystem, but code not in codesystem
		obs.getCode().getCodingFirstRep().setSystem("http://example.com/codesystem");
		obs.getCode().getCodingFirstRep().setCode("foo-foo");
		obs.getCode().getCodingFirstRep().setDisplay("Some Code");
		outcome = (OperationOutcome) myObservationDao.validate(obs, null, null, null, ValidationModeEnum.CREATE, "http://example.com/structuredefinition", mySrd).getOperationOutcome();
		ourLog.debug("Outcome: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
		assertEquals("Unknown code in fragment CodeSystem 'http://example.com/codesystem#foo-foo'", outcome.getIssueFirstRep().getDiagnostics());
		assertEquals(OperationOutcome.IssueSeverity.WARNING, outcome.getIssueFirstRep().getSeverity());

		// Correct codesystem, Code in codesystem
		obs.getCode().getCodingFirstRep().setSystem("http://example.com/codesystem");
		obs.getCode().getCodingFirstRep().setCode("some-code");
		obs.getCode().getCodingFirstRep().setDisplay("Some Code");
		outcome = (OperationOutcome) myObservationDao.validate(obs, null, null, null, ValidationModeEnum.CREATE, "http://example.com/structuredefinition", mySrd).getOperationOutcome();
		ourLog.debug("Outcome: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
		assertEquals("No issues detected during validation", outcome.getIssueFirstRep().getDiagnostics());
		assertEquals(OperationOutcome.IssueSeverity.INFORMATION, outcome.getIssueFirstRep().getSeverity());

		// Code in wrong codesystem
		obs.getCode().getCodingFirstRep().setSystem("http://example.com/foo-foo");
		obs.getCode().getCodingFirstRep().setCode("some-code");
		obs.getCode().getCodingFirstRep().setDisplay("Some Code");
		outcome = (OperationOutcome) myObservationDao.validate(obs, null, null, null, ValidationModeEnum.CREATE, "http://example.com/structuredefinition", mySrd).getOperationOutcome();
		assertHasErrors(outcome);
		ourLog.debug("Outcome: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
		assertThat(outcome.getIssueFirstRep().getDiagnostics()).contains("None of the codings provided are in the value set 'MessageCategory'");
		assertEquals(OperationOutcome.IssueSeverity.ERROR, outcome.getIssueFirstRep().getSeverity());
	}

	@Test
	public void testIsCodeSystemSupported() {
		ValidationSupportContext ctx = new ValidationSupportContext(myValidationSupport);

		boolean outcome = myValidationSupport.isCodeSystemSupported(ctx, "http://terminology.hl7.org/CodeSystem/v2-0203-FOO");
		assertFalse(outcome);

		outcome = myValidationSupport.isCodeSystemSupported(ctx, "http://terminology.hl7.org/CodeSystem/v2-0203");
		assertTrue(outcome);

		outcome = myValidationSupport.isCodeSystemSupported(ctx, "http://terminology.hl7.org/CodeSystem/v2-0203-BLAH");
		assertFalse(outcome);
	}

	/**
	 * Create a loinc valueset that expands to more results than the expander is willing to do
	 * in memory, and make sure we can still validate correctly, even if we're using
	 * the in-memory expander
	 */
	@Test
	public void testValidateCode_PreExpansionAgainstHugeValueSet() throws Exception {
		myStorageSettings.setPreExpandValueSets(true);

		// Add a bunch of codes
		CustomTerminologySet codesToAdd = new CustomTerminologySet();
		for (int i = 0; i < 100; i++) {
			codesToAdd.addRootConcept("CODE" + i, "Display " + i);
		}
		myTermCodeSystemStorageSvc.applyDeltaCodeSystemsAdd("http://loinc.org", codesToAdd);

		myTerminologyDeferredStorageSvc.saveAllDeferred();

		// Create a valueset
		ValueSet vs = new ValueSet();
		vs.setUrl("http://example.com/fhir/ValueSet/observation-vitalsignresult");
		vs.getCompose().addInclude().setSystem("http://loinc.org");
		myValueSetDao.create(vs);
		myTermReadSvc.preExpandDeferredValueSetsToTerminologyTables();

		await().until(() -> myTermReadSvc.isValueSetPreExpandedForCodeValidation(vs));

		// Load the profile, which is just the Vital Signs profile modified to accept all loinc codes
		// and not just certain ones
		StructureDefinition profile = loadResourceFromClasspath(StructureDefinition.class, "/r4/profile-vitalsigns-all-loinc.json");
		myStructureDefinitionDao.create(profile, mySrd);

		Observation obs = new Observation();
		obs.getMeta().addProfile("http://example.com/fhir/StructureDefinition/vitalsigns-2");
		obs.getText().setDivAsString("<div>Hello</div>");
		obs.getCategoryFirstRep().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/observation-category").setCode("vital-signs");
		obs.setSubject(new Reference("Patient/123"));
		obs.addPerformer(new Reference("Practitioner/123"));
		obs.setEffective(DateTimeType.now());
		obs.setStatus(ObservationStatus.FINAL);
		obs.setValue(new StringType("This is the value"));

		OperationOutcome oo;

		// Valid code
		obs.getText().setStatus(Narrative.NarrativeStatus.GENERATED);
		obs.getCode().getCodingFirstRep().setSystem("http://loinc.org").setCode("CODE3").setDisplay("Display 3");
		oo = validateAndReturnOutcome(obs);
		assertThat(oo.getIssueFirstRep().getDiagnostics()).as(encode(oo)).isEqualTo("No issues detected during validation");

		// Invalid code
		obs.getText().setStatus(Narrative.NarrativeStatus.GENERATED);
		obs.getCode().getCodingFirstRep().setSystem("http://loinc.org").setCode("non-existing-code").setDisplay("Display 3");
		oo = validateAndReturnOutcome(obs);
		assertThat(oo.getIssue().get(2).getDiagnostics()).as(encode(oo)).isEqualTo("None of the codings provided are in the value set 'ValueSet[http://example.com/fhir/ValueSet/observation-vitalsignresult]' (http://example.com/fhir/ValueSet/observation-vitalsignresult), and a coding from this value set is required) (codes = http://loinc.org#non-existing-code)");

		// Valid code with no system
		obs.getText().setStatus(Narrative.NarrativeStatus.GENERATED);
		obs.getCode().getCodingFirstRep().setSystem(null).setCode("CODE3").setDisplay("Display 3");
		oo = validateAndReturnOutcome(obs);
		assertThat(encode(oo)).contains("None of the codings provided are in the value set 'ValueSet[http://example.com/fhir/ValueSet/observation-vitalsignresult]' (http://example.com/fhir/ValueSet/observation-vitalsignresult)");

		// Valid code with wrong system
		obs.getText().setStatus(Narrative.NarrativeStatus.GENERATED);
		obs.getCode().getCodingFirstRep().setSystem("http://foo").setCode("CODE3").setDisplay("Display 3");
		oo = validateAndReturnOutcome(obs);
		assertThat(oo.getIssue().get(1).getDiagnostics()).as(encode(oo)).isEqualTo("None of the codings provided are in the value set 'ValueSet[http://example.com/fhir/ValueSet/observation-vitalsignresult]' (http://example.com/fhir/ValueSet/observation-vitalsignresult), and a coding from this value set is required) (codes = http://foo#CODE3)");

		// Code that exists but isn't in the valueset
		obs.getText().setStatus(Narrative.NarrativeStatus.GENERATED);
		obs.getCode().getCodingFirstRep().setSystem("http://terminology.hl7.org/CodeSystem/observation-category").setCode("vital-signs").setDisplay("Vital Signs");
		oo = validateAndReturnOutcome(obs);
		assertThat(oo.getIssue().get(1).getDiagnostics()).as(encode(oo)).isEqualTo("None of the codings provided are in the value set 'ValueSet[http://example.com/fhir/ValueSet/observation-vitalsignresult]' (http://example.com/fhir/ValueSet/observation-vitalsignresult), and a coding from this value set is required) (codes = http://terminology.hl7.org/CodeSystem/observation-category#vital-signs)");

		// Invalid code in built-in VS/CS
		obs.getText().setStatus(Narrative.NarrativeStatus.GENERATED);
		obs.getCode().getCodingFirstRep().setSystem("http://loinc.org").setCode("CODE3").setDisplay("Display 3");
		obs.getCategoryFirstRep().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/observation-category").setCode("FOO");
		oo = validateAndReturnOutcome(obs);
		assertThat(oo.getIssueFirstRep().getDiagnostics()).as(encode(oo)).isEqualTo("Unknown code 'http://terminology.hl7.org/CodeSystem/observation-category#FOO'");

	}

	/**
	 * Make sure that we do something sane when validating throws an unexpected exception
	 */
	@Test
	public void testValidate_ValidationSupportThrowsException() {
		IValidationSupport validationSupport = mock(IValidationSupport.class);
		when(validationSupport.validateCodeInValueSet(any(), any(), any(), any(), any(), any())).thenAnswer(t -> {
			// This will fail with a constraint error
			try {
				myResourceTableDao.save(new ResourceTable());
				myResourceTableDao.flush();
			} catch (Exception e) {
				ourLog.info("Hit expected exception: {}", e.toString());
			}
			return null;
		});
		when(validationSupport.getFhirContext()).thenReturn(myFhirContext);

		myJpaValidationSupportChain.addValidationSupport(0, validationSupport);
		try {

			Observation obs = new Observation();
			obs.getText().setDivAsString("<div>Hello</div>");
			obs.getCategoryFirstRep().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/observation-category").setCode("vital-signs");
			obs.setSubject(new Reference("Patient/123"));
			obs.addPerformer(new Reference("Practitioner/123"));
			obs.setEffective(DateTimeType.now());
			obs.setStatus(ObservationStatus.FINAL);
			obs.setValue(new StringType("This is the value"));

			OperationOutcome oo;

			// Valid code
			obs.getText().setStatus(Narrative.NarrativeStatus.GENERATED);
			obs.getCode().getCodingFirstRep().setSystem("http://loinc.org").setCode("CODE3").setDisplay("Display 3");
			oo = validateAndReturnOutcome(obs);
			assertThat(oo.getIssueFirstRep().getDiagnostics()).as(encode(oo)).isEqualTo("No issues detected during validation");

		} finally {
			myJpaValidationSupportChain.removeValidationSupport(validationSupport);
		}
	}

	/**
	 * Make sure that we do something sane when validating throws an unexpected exception
	 */
	@Test
	@Disabled
	public void testValidate_TermSvcHasDatabaseRollback() {
		TermReadSvcImpl.setInvokeOnNextCallForUnitTest(() -> {
			try {
				myResourceTableDao.save(new ResourceTable());
				myResourceTableDao.flush();
			} catch (Exception e) {
				ourLog.info("Hit expected exception: {}", e.toString());
			}
		});

		Observation obs = new Observation();
		obs.getText().setDivAsString("<div>Hello</div>");
		obs.getCategoryFirstRep().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/observation-category").setCode("vital-signs");
		obs.setSubject(new Reference("Patient/123"));
		obs.addPerformer(new Reference("Practitioner/123"));
		obs.setEffective(DateTimeType.now());
		obs.setStatus(ObservationStatus.FINAL);
		obs.setValue(new StringType("This is the value"));

		OperationOutcome oo;

		// Valid code
		obs.getText().setStatus(Narrative.NarrativeStatus.GENERATED);
		obs.getCode().getCodingFirstRep().setSystem("http://loinc.org").setCode("CODE3").setDisplay("Display 3");
		oo = validateAndReturnOutcome(obs);
		assertThat("No issues detected during validation").as(oo.getIssueFirstRep().getDiagnostics()).isEqualTo(encode(oo));

	}

	/**
	 * Make sure that we do something sane when validating throws an unexpected exception
	 */
	@Test
	public void testValidate_TermSvcHasNpe() {

		CodeSystem cs = new CodeSystem();
		cs.setUrl("http://FOO");
		cs.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		myCodeSystemDao.create(cs);

		TermReadSvcImpl.setInvokeOnNextCallForUnitTest(() -> {
			throw new NullPointerException("MY ERROR");
		});

		Observation obs = new Observation();
		obs.getText().setDivAsString("<div>Hello</div>");
		obs.getCategoryFirstRep().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/observation-category").setCode("vital-signs");
		obs.setSubject(new Reference("Patient/123"));
		obs.addPerformer(new Reference("Practitioner/123"));
		obs.setEffective(DateTimeType.now());
		obs.setStatus(ObservationStatus.FINAL);
		obs.setValue(new StringType("This is the value"));


		// Valid code
		obs.getText().setStatus(Narrative.NarrativeStatus.GENERATED);
		obs.getCode().getCodingFirstRep().setSystem("http://FOO").setCode("CODE99999").setDisplay("Display 3");

		OperationOutcome oo = validateAndReturnOutcome(obs);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
		assertEquals("Error MY ERROR validating CodeableConcept", oo.getIssueFirstRep().getDiagnostics());
		assertEquals(OperationOutcome.IssueSeverity.WARNING, oo.getIssueFirstRep().getSeverity());
	}

	@Test
	public void testValidateCodeableConceptWithNoSystem() {
		AllergyIntolerance allergy = new AllergyIntolerance();
		allergy.getText().setStatus(Narrative.NarrativeStatus.GENERATED).getDiv().setValue("<div>hi!</div>");
		allergy.getClinicalStatus().addCoding().setSystem(null).setCode("active").setDisplay("Active");
		allergy.getVerificationStatus().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/allergyintolerance-verification").setCode("confirmed").setDisplay("Confirmed");
		allergy.setPatient(new Reference("Patient/123"));

		allergy.addNote()
			.setText("This is text")
			.setAuthor(new Reference("Patient/123"));

		ourLog.debug(myFhirContext.newJsonParser().encodeResourceToString(allergy));

		OperationOutcome oo = validateAndReturnOutcome(allergy);
		assertThat(encode(oo)).contains("None of the codings provided are in the value set 'AllergyIntolerance Clinical Status Codes' (http://hl7.org/fhir/ValueSet/allergyintolerance-clinical|4.0.1)");
	}

	@SuppressWarnings("unchecked")
	private <T extends IBaseResource> OperationOutcome validateAndReturnOutcome(T theObs) {
		IFhirResourceDao<T> dao = (IFhirResourceDao<T>) myDaoRegistry.getResourceDao(theObs.getClass());
		try {
			MethodOutcome outcome = dao.validate(theObs, null, null, null, ValidationModeEnum.CREATE, null, mySrd);
			return (OperationOutcome) outcome.getOperationOutcome();
		} catch (PreconditionFailedException e) {
			return (OperationOutcome) e.getOperationOutcome();
		}
	}

	private <T extends IBaseResource> OperationOutcome validateAndReturnOutcome(T theObs, Boolean theWantError) {
		IFhirResourceDao<T> dao = (IFhirResourceDao<T>) myDaoRegistry.getResourceDao(theObs.getClass());
		String encoded = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(theObs);
		MethodOutcome outcome = dao.validate(theObs, null, encoded, EncodingEnum.JSON, ValidationModeEnum.CREATE, null, mySrd);
		OperationOutcome oo = (OperationOutcome) outcome.getOperationOutcome();
		if (theWantError) {
			assertHasErrors(oo);
		} else {
			assertHasNoErrors(oo);
		}
		return oo;
	}

	@Test
	public void testValidateStructureDefinition() throws Exception {
		String input = IOUtils.toString(getClass().getResourceAsStream("/r4/sd-david-dhtest7.json"), StandardCharsets.UTF_8);
		StructureDefinition sd = myFhirContext.newJsonParser().parseResource(StructureDefinition.class, input);


		ourLog.info("Starting validation");
		try {
			myStructureDefinitionDao.validate(sd, null, null, null, ValidationModeEnum.UPDATE, null, mySrd);
		} catch (PreconditionFailedException e) {
			ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(e.getOperationOutcome()));
		}
		ourLog.info("Done validation");

		StopWatch sw = new StopWatch();
		ourLog.info("Starting validation");
		try {
			myStructureDefinitionDao.validate(sd, null, null, null, ValidationModeEnum.UPDATE, null, mySrd);
		} catch (PreconditionFailedException e) {
			// ok
		}
		ourLog.info("Done validation in {}ms", sw.getMillis());

	}

	@Test
	public void testValidateDocument() throws Exception {
		String input = IOUtils.toString(getClass().getResourceAsStream("/r4/document-bundle.json"), StandardCharsets.UTF_8);
		Bundle document = myFhirContext.newJsonParser().parseResource(Bundle.class, input);

		ourLog.info("Starting validation");
		MethodOutcome outcome = myBundleDao.validate(document, null, null, null, ValidationModeEnum.CREATE, null, mySrd);
		OperationOutcome oo = (OperationOutcome) outcome.getOperationOutcome();
		assertHasErrors(oo);
		String encodedResponse = myFhirContext.newJsonParser().encodeResourceToString(oo);
		ourLog.info("Validation result: {}", encodedResponse);
	}

	@Test
	@Disabled
	public void testValidateResourceContainingProfileDeclarationJson() throws Exception {
		String methodName = "testValidateResourceContainingProfileDeclarationJson";
		OperationOutcome outcome = doTestValidateResourceContainingProfileDeclaration(methodName, EncodingEnum.JSON);

		String ooString = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome);
		ourLog.info(ooString);
		assertThat(ooString).contains("Element '.subject': minimum required = 1, but only found 0");
		assertThat(ooString).contains("Element encounter @ : max allowed = 0, but found 1");
		assertThat(ooString).contains("Element '.device': minimum required = 1, but only found 0");
	}

	@Test
	@Disabled
	public void testValidateResourceContainingProfileDeclarationXml() throws Exception {
		String methodName = "testValidateResourceContainingProfileDeclarationXml";
		OperationOutcome outcome = doTestValidateResourceContainingProfileDeclaration(methodName, EncodingEnum.XML);

		String ooString = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome);
		ourLog.info(ooString);
		assertThat(ooString).contains("Element '/f:Observation.subject': minimum required = 1, but only found 0");
		assertThat(ooString).contains("Element encounter @ /f:Observation: max allowed = 0, but found 1");
		assertThat(ooString).contains("Element '/f:Observation.device': minimum required = 1, but only found 0");
	}

	@Test
	public void testValidateUsingExternallyDefinedCode() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl("http://foo");
		codeSystem.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		IIdType csId = myCodeSystemDao.create(codeSystem).getId();

		TermCodeSystemVersion csv = new TermCodeSystemVersion();
		csv.addConcept().setCode("bar").setDisplay("Bar Code");
		myTermCodeSystemStorageSvc.storeNewCodeSystemVersion(codeSystem, csv, mySrd, Collections.emptyList(), Collections.emptyList());

		// Validate a resource containing this codesystem in a field with an extendable binding
		Patient patient = new Patient();
		patient.getText().setStatus(Narrative.NarrativeStatus.GENERATED).setDivAsString("<div>hello</div>");
		patient
			.addIdentifier()
			.setSystem("http://example.com")
			.setValue("12345")
			.getType()
			.addCoding()
			.setSystem("http://foo")
			.setCode("bar");
		MethodOutcome outcome = myPatientDao.validate(patient, null, encode(patient), EncodingEnum.JSON, ValidationModeEnum.CREATE, null, mySrd);
		IBaseOperationOutcome oo = outcome.getOperationOutcome();
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));

		// It would be ok for this to produce 0 issues, or just an information message too
		assertEquals(1, OperationOutcomeUtil.getIssueCount(myFhirContext, oo));
		assertThat(OperationOutcomeUtil.getFirstIssueDetails(myFhirContext, oo)).contains("None of the codings provided are in the value set 'IdentifierType'");
		assertThat(OperationOutcomeUtil.getFirstIssueDetails(myFhirContext, oo)).contains("a coding should come from this value set unless it has no suitable code (note that the validator cannot judge what is suitable) (codes = http://foo#bar)");

	}

	@Test
	public void testValidateUsingExternallyDefinedCodeMisMatchDisplay_InMemory_ShouldLogWarning() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl("http://foo");
		codeSystem.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		IIdType csId = myCodeSystemDao.create(codeSystem).getId();

		TermCodeSystemVersion csv = new TermCodeSystemVersion();
		csv.addConcept().setCode("bar").setDisplay("Bar Code");
		IIdType updatedCsId = myTermCodeSystemStorageSvc.storeNewCodeSystemVersion(codeSystem, csv, mySrd, Collections.emptyList(), Collections.emptyList());



		// Validate a resource containing this codesystem in a field with an extendable binding
		Patient patient = new Patient();
		patient.getText().setStatus(Narrative.NarrativeStatus.GENERATED).setDivAsString("<div>hello</div>");
		patient
			.addIdentifier()
			.setSystem("http://example.com")
			.setValue("12345")
			.getType()
			.addCoding()
			.setSystem("http://foo")
			.setCode("bar")
			.setDisplay("not bar code");
		MethodOutcome outcome = myPatientDao.validate(patient, null, encode(patient), EncodingEnum.JSON, ValidationModeEnum.CREATE, null, mySrd);
		OperationOutcome oo = (OperationOutcome) outcome.getOperationOutcome();
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));

		// It would be ok for this to produce 0 issues, or just an information message too
		assertEquals(2, OperationOutcomeUtil.getIssueCount(myFhirContext, oo));
		OperationOutcome.OperationOutcomeIssueComponent notInValueSetIssue = oo.getIssue().get(1);
		assertThat(notInValueSetIssue.getDiagnostics()).contains("None of the codings provided are in the value set 'IdentifierType'");
		assertThat(notInValueSetIssue.getDiagnostics()).contains("a coding should come from this value set unless it has no suitable code (note that the validator cannot judge what is suitable) (codes = http://foo#bar)");
		assertEquals(OperationOutcome.IssueSeverity.WARNING, notInValueSetIssue.getSeverity());
		assertEquals("Concept Display \"not bar code\" does not match expected \"Bar Code\" for 'http://foo#bar'", oo.getIssue().get(0).getDiagnostics());
	}

	private OperationOutcome doTestValidateResourceContainingProfileDeclaration(String methodName, EncodingEnum enc) throws IOException {
		Bundle vss = loadResourceFromClasspath(Bundle.class, "/org/hl7/fhir/r4/model/valueset/valuesets.xml");
		myValueSetDao.update((ValueSet) findResourceByIdInBundle(vss, "observation-status"), mySrd);
		myValueSetDao.update((ValueSet) findResourceByIdInBundle(vss, "observation-category"), mySrd);
		myValueSetDao.update((ValueSet) findResourceByIdInBundle(vss, "observation-codes"), mySrd);
		myValueSetDao.update((ValueSet) findResourceByIdInBundle(vss, "observation-methods"), mySrd);
		myValueSetDao.update((ValueSet) findResourceByIdInBundle(vss, "observation-valueabsentreason"), mySrd);
		myValueSetDao.update((ValueSet) findResourceByIdInBundle(vss, "observation-interpretation"), mySrd);
		myValueSetDao.update((ValueSet) findResourceByIdInBundle(vss, "body-site"), mySrd);
		myValueSetDao.update((ValueSet) findResourceByIdInBundle(vss, "referencerange-meaning"), mySrd);
		myValueSetDao.update((ValueSet) findResourceByIdInBundle(vss, "observation-relationshiptypes"), mySrd);

		StructureDefinition sd = loadResourceFromClasspath(StructureDefinition.class, "/org/hl7/fhir/r4/model/profile/devicemetricobservation.profile.xml");
		sd.setId(new IdType());
		sd.setUrl("http://example.com/foo/bar/" + methodName);
		myStructureDefinitionDao.create(sd, mySrd);

		Observation input = new Observation();
		input.getMeta().getProfile().add(new CanonicalType(sd.getUrl()));

		input.addIdentifier().setSystem("http://acme").setValue("12345");
		input.getEncounter().setReference("http://foo.com/Encounter/9");
		input.setStatus(ObservationStatus.FINAL);
		input.getCode().addCoding().setSystem("http://loinc.org").setCode("12345");

		String encoded = null;
		MethodOutcome outcome = null;
		ValidationModeEnum mode = ValidationModeEnum.CREATE;
		switch (enc) {
			case JSON:
				encoded = myFhirContext.newJsonParser().encodeResourceToString(input);
				try {
					myObservationDao.validate(input, null, encoded, EncodingEnum.JSON, mode, null, mySrd);
					fail();
				} catch (PreconditionFailedException e) {
					return (OperationOutcome) e.getOperationOutcome();
				}
				break;
			case XML:
				encoded = myFhirContext.newXmlParser().encodeResourceToString(input);
				try {
					myObservationDao.validate(input, null, encoded, EncodingEnum.XML, mode, null, mySrd);
					fail();
				} catch (PreconditionFailedException e) {
					return (OperationOutcome) e.getOperationOutcome();
				}
				break;
		}

		throw new IllegalStateException(); // shouldn't get here
	}

	@Test
	public void validateResource_withUnknownMetaProfileurl_validatesButLogsWarning() {
		// setup
		IParser parser = myFhirContext.newJsonParser();

		myLogbackTestExtension.setUp(Level.WARN);

		String obsStr ="""
					{
				    "resourceType": "Observation",
				    "meta": {
				  		"profile": [
				              "http://example.com/StructureDefinition|a|b|c"
				    	]
				  	}
				  }
			""";
		Observation observation = parser.parseResource(Observation.class, obsStr);

		// test
		ValidationModeEnum mode = ValidationModeEnum.CREATE;
		MethodOutcome outcome = myObservationDao.validate(observation, null, obsStr, EncodingEnum.JSON, mode, null, mySrd);

		// validator
		assertNotNull(outcome);
		assertInstanceOf(OperationOutcome.class, outcome.getOperationOutcome());
		List<OperationOutcome.OperationOutcomeIssueComponent> issues = ((OperationOutcome) outcome.getOperationOutcome()).getIssue();
		assertFalse(issues.isEmpty());
		List<OperationOutcome.OperationOutcomeIssueComponent> errors = issues.stream()
			.filter(i -> i.getSeverity() == OperationOutcome.IssueSeverity.ERROR)
			.toList();
		// we have errors
		assertFalse(errors.isEmpty());

		LogbackTestExtensionAssert.assertThat(myLogbackTestExtension).hasWarnMessage("Unrecognized profile uri");
	}

	@Test
	public void validateResource_withMetaProfileWithVersion_validatesAsExpected() {
		// setup
		IParser parser = myFhirContext.newJsonParser();

		// create our structure definition
		@Language("JSON")
		String strDefStr =
			"""
			{
   				"resourceType": "StructureDefinition",
   				"id": "example-profile",
   				"url": "http://example.com/StructureDefinition",
   				"version": "1.0.0",
   				"name": "observation-example",
   				"title": "Example Profile",
   				"status": "active",
  			 	"experimental": false,
   				"date": "2016-03-25",
   				"description": "Example Profile",
   				"fhirVersion": "4.0.1",
   				"kind": "resource",
  			 	"abstract": false,
   				"type": "Observation",
   				"baseDefinition": "http://hl7.org/fhir/StructureDefinition/Observation",
   				"derivation": "constraint",
   				"differential": {
  			 		"element": [
   						{
   							"id": "Observation",
   							"path": "Observation",
   							"short": "Example Profile",
   							"alias": [
   								"Example"
   							],
   							"min": 0,
   							"max": "*"
   						},
   						{
   							"id": "Observation.code",
   							"path": "Observation.code",
   							"short": "Coded Responses from C-CDA Vital Sign Results",
   							"definition": "Coded Responses from C-CDA Vital Sign Results.",
   							"requirements": "5. SHALL contain exactly one [1..1] code, where the @code SHOULD be selected from ValueSet Example",
   							"min": 1,
   							"max": "1",
   							"type": [
   								{
   									"code": "CodeableConcept"
   								}
   							],
   							"mustSupport": true,
   							"binding": {
   								"strength": "required",
   								"description": "This identifies the vital sign result type.",
   								"valueSet": "http://example.com/valueset"
   							}
   						}
   					]
   				}
   			}
			""";
		StructureDefinition sd = parser.parseResource(StructureDefinition.class, strDefStr);
		myStructureDefinitionDao.create(sd, mySrd);

		@Language("JSON")
		String obsStr ="""
					{
				    "resourceType": "Observation",
				    "meta": {
				  		"profile": [
				              "http://example.com/StructureDefinition|1.0.0"
				    	]
				  	},
				    "identifier": [
				      {
				        "use": "official",
				        "system": "http://www.bmc.nl/zorgportal/identifiers/observations",
				        "value": "6323"
				      }
				    ],
				    "status": "final",
				    "code": {
				      "coding": [
				        {
				          "system": "http://example.com/codesystem",
				          "code": "some-code",
				          "display": "Some Code"
				        }
				      ]
				    },
				  	"subject": {
				  		"reference": "Patient/1452"
				  	},
				    "effectiveDateTime": "2013-05-02T09:30:10+01:00",
				    "issued": "2013-04-03T15:30:10+01:00",
				    "valueQuantity": {
				      "value": 6.3,
				      "unit": "mmol/l",
				      "system": "http://unitsofmeasure.org",
				      "code": "mmol/L"
				    },
				    "interpretation": [
				      {
				        "coding": [
				          {
				            "system": "http://terminology.hl7.org/CodeSystem/v3-ObservationInterpretation",
				            "code": "H",
				            "display": "High"
				          }
				        ]
				      }
				    ]
				  }
			""";
		Observation observation = parser.parseResource(Observation.class, obsStr);

		// test
		ValidationModeEnum mode = ValidationModeEnum.CREATE;
		MethodOutcome outcome = myObservationDao.validate(observation, null, obsStr, EncodingEnum.JSON, mode, null, mySrd);

		// verify
		assertNotNull(outcome);
		assertInstanceOf(OperationOutcome.class, outcome.getOperationOutcome());
		List<OperationOutcome.OperationOutcomeIssueComponent> issues = ((OperationOutcome) outcome.getOperationOutcome()).getIssue();
		assertFalse(issues.isEmpty());
		List<OperationOutcome.OperationOutcomeIssueComponent> errors = issues.stream()
			.filter(i -> i.getSeverity() == OperationOutcome.IssueSeverity.ERROR)
			.toList();
		// no errors - just warnings
		assertTrue(errors.isEmpty(), errors.stream().map(OperationOutcome.OperationOutcomeIssueComponent::getDiagnostics).collect(Collectors.joining(",")));
	}

	@Test
	public void testValidateResourceContainingProfileDeclarationInvalid() {
		String methodName = "testValidateResourceContainingProfileDeclarationInvalid";

		Observation input = new Observation();
		String profileUri = "http://example.com/StructureDefinition/" + methodName;
		input.getMeta().getProfile().add(new CanonicalType(profileUri));

		input.addIdentifier().setSystem("http://acme").setValue("12345");
		input.getEncounter().setReference("http://foo.com/Encounter/9");
		input.setStatus(ObservationStatus.FINAL);
		input.getCode().addCoding().setSystem("http://loinc.org").setCode("12345");

		ValidationModeEnum mode = ValidationModeEnum.CREATE;
		String encoded = myFhirContext.newJsonParser().encodeResourceToString(input);

		MethodOutcome result = myObservationDao.validate(input, null, encoded, EncodingEnum.JSON, mode, null, mySrd);
		OperationOutcome oo = (OperationOutcome) result.getOperationOutcome();
		assertHasErrors(oo);
		String outputString = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo);
		ourLog.info(outputString);
		assertThat(outputString).contains("Profile reference 'http://example.com/StructureDefinition/testValidateResourceContainingProfileDeclarationInvalid' has not been checked because it could not be found");
	}

	@Test
	public void testValidateBundleContainingResourceContainingProfileDeclarationInvalid() {
		String methodName = "testValidateResourceContainingProfileDeclarationInvalid";

		Observation observation = new Observation();
		String profileUri = "http://example.com/StructureDefinition/" + methodName;
		observation.getMeta().getProfile().add(new CanonicalType(profileUri));
		observation.addIdentifier().setSystem("http://acme").setValue("12345");
		observation.getEncounter().setReference("http://foo.com/Encounter/9");
		observation.setStatus(ObservationStatus.FINAL);
		observation.getCode().addCoding().setSystem("http://loinc.org").setCode("12345");

		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.TRANSACTION);
		input.addEntry()
			.setResource(observation)
			.setFullUrl("http://example.com/Observation")
			.getRequest()
			.setUrl("http://example.com/Observation")
			.setMethod(Bundle.HTTPVerb.POST);

		ValidationModeEnum mode = ValidationModeEnum.CREATE;
		String encoded = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(input);
		ourLog.info(encoded);

		MethodOutcome methodOutcome = myBundleDao.validate(input, null, encoded, EncodingEnum.JSON, mode, null, mySrd);
		org.hl7.fhir.r4.model.OperationOutcome oo = (org.hl7.fhir.r4.model.OperationOutcome) methodOutcome.getOperationOutcome();
		assertHasErrors(oo);
		String outputString = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo);
		ourLog.info(outputString);
		assertThat(outputString).contains("Profile reference 'http://example.com/StructureDefinition/testValidateResourceContainingProfileDeclarationInvalid' has not been checked because it could not be found");
	}

	@Test
	public void testValidateWithCanonicalReference() {
		FhirInstanceValidator val = AopTestUtils.getTargetObject(myValidatorModule);
		val.setBestPracticeWarningLevel(BestPracticeWarningLevel.Ignore);

		ValueSet vs = new ValueSet();
		vs.setId("MYVS");
		vs.setUrl("http://myvs");
		vs.getCompose()
			.addInclude()
			.setSystem("http://hl7.org/fhir/administrative-gender")
			.addConcept(new ValueSet.ConceptReferenceComponent().setCode("male"))
			.addConcept(new ValueSet.ConceptReferenceComponent().setCode("female"));
		myValueSetDao.update(vs);

		Questionnaire q = new Questionnaire();
		q.setId("MYQ");
		q.setUrl("http://myquestionnaire");
		q.addItem()
			.setLinkId("LINKID")
			.setType(Questionnaire.QuestionnaireItemType.CHOICE)
			.setAnswerValueSet("ValueSet/MYVS");
		myQuestionnaireDao.update(q);

		// Validate with matching code
		QuestionnaireResponse qr = new QuestionnaireResponse();
		qr.setStatus(QuestionnaireResponse.QuestionnaireResponseStatus.COMPLETED);
		qr.setQuestionnaire("Questionnaire/MYQ");
		qr.addItem()
			.setLinkId("LINKID")
			.addAnswer()
			.setValue(new Coding().setSystem("http://hl7.org/fhir/administrative-gender").setCode("aaa"));
		MethodOutcome outcome = myQuestionnaireResponseDao.validate(qr, null, null, null, ValidationModeEnum.CREATE, null, mySrd);
		OperationOutcome oo = (OperationOutcome) outcome.getOperationOutcome();
		assertHasErrors(oo);
		String encoded = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo);
		ourLog.info(encoded);
		assertThat(encoded).contains("is not in the options value set");
	}

	@Test
	public void testValidateCapabilityStatement() {

		SearchParameter sp = new SearchParameter();
		sp.setUrl("http://example.com/name");
		sp.setId("name");
		sp.setCode("name");
		sp.setType(Enumerations.SearchParamType.STRING);
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		sp.setExpression("Patient.name");
		mySearchParameterDao.update(sp);

		CapabilityStatement cs = new CapabilityStatement();
		cs.getText().setStatus(Narrative.NarrativeStatus.GENERATED).getDiv().setValue("<div>aaaa</div>");
		CapabilityStatement.CapabilityStatementRestComponent rest = cs.addRest();
		CapabilityStatement.CapabilityStatementRestResourceComponent patient = rest.addResource();
		patient.setType("Patient");
		patient.addSearchParam().setName("foo").setType(Enumerations.SearchParamType.DATE).setDefinition("http://example.com/name");

		MethodOutcome result = myCapabilityStatementDao.validate(cs, null, null, null, ValidationModeEnum.CREATE, null, mySrd);
		OperationOutcome oo = (OperationOutcome) result.getOperationOutcome();
		assertHasErrors(oo);
		String ooString = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo);
		ourLog.info(ooString);
		assertThat(ooString).as(ooString).contains("Type mismatch - SearchParameter 'http://example.com/name' type is string, but type here is date");
	}


	@Test
	void testValidateCommonCodes_Ucum_ErrorMessageIsPreserved() {

		String loincCode = "1234";
		addLoincCodeToCodeSystemDao(loincCode);

		Observation input = new Observation();
		input.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		input.setStatus(ObservationStatus.AMENDED);
		input.getCode().addCoding().setSystem("http://loinc.org").setCode(loincCode).setDisplay("FOO");
		input.setValue(new Quantity(
				null,
				123,
				"http://unitsofmeasure.org",
				"MG/DL",
				"MG/DL"
		));

		String inputString = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(input);
		MethodOutcome result = myObservationDao.validate(input, null, inputString, EncodingEnum.JSON, ValidationModeEnum.CREATE, null, mySrd);
		OperationOutcome oo = (OperationOutcome) result.getOperationOutcome();
		assertHasErrors(oo);

		assertEquals(15, ((IntegerType) oo.getIssue().get(0).getExtensionByUrl("http://hl7.org/fhir/StructureDefinition/operationoutcome-issue-line").getValue()).getValue());
		assertEquals(4, ((IntegerType) oo.getIssue().get(0).getExtensionByUrl("http://hl7.org/fhir/StructureDefinition/operationoutcome-issue-col").getValue()).getValue());
		assertEquals("Terminology_PassThrough_TX_Message", ((StringType) oo.getIssue().get(0).getExtensionByUrl("http://hl7.org/fhir/StructureDefinition/operationoutcome-message-id").getValue()).getValue());
		assertEquals("Error processing unit 'MG/DL': The unit 'DL' is unknown' at position 3 (for 'http://unitsofmeasure.org#MG/DL')", oo.getIssue().get(0).getDiagnostics());
		assertEquals(OperationOutcome.IssueType.PROCESSING, oo.getIssue().get(0).getCode());
		assertEquals(OperationOutcome.IssueSeverity.ERROR, oo.getIssue().get(0).getSeverity());
		assertThat(oo.getIssue().get(0).getLocation()).hasSize(2);
		assertEquals("Observation.value.ofType(Quantity)", oo.getIssue().get(0).getLocation().get(0).getValue());
		assertEquals("Line[15] Col[4]", oo.getIssue().get(0).getLocation().get(1).getValue());
	}

	@Test
	void testValidateCommonCodes_Currency_ErrorMessageIsPreserved() {
		String loincCode = "1234";

		addLoincCodeToCodeSystemDao(loincCode);

		Observation input = new Observation();
		input.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		input.setStatus(ObservationStatus.AMENDED);
		input.getCode().addCoding().setSystem("http://loinc.org").setCode(loincCode).setDisplay("FOO");
		input.setValue(new Quantity(
				null,
				123,
				CURRENCIES_CODESYSTEM_URL,
				"blah",
				"blah"
		));

		String inputString = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(input);
		MethodOutcome result = myObservationDao.validate(input, null, inputString, EncodingEnum.JSON, ValidationModeEnum.CREATE, null, mySrd);
		OperationOutcome oo = (OperationOutcome) result.getOperationOutcome();
		assertHasErrors(oo);

		assertEquals(15, ((IntegerType) oo.getIssue().get(0).getExtensionByUrl("http://hl7.org/fhir/StructureDefinition/operationoutcome-issue-line").getValue()).getValue());
		assertEquals(4, ((IntegerType) oo.getIssue().get(0).getExtensionByUrl("http://hl7.org/fhir/StructureDefinition/operationoutcome-issue-col").getValue()).getValue());
		assertEquals("Terminology_PassThrough_TX_Message", ((StringType) oo.getIssue().get(0).getExtensionByUrl("http://hl7.org/fhir/StructureDefinition/operationoutcome-message-id").getValue()).getValue());
		assertEquals("Unknown code 'urn:iso:std:iso:4217#blah'", oo.getIssue().get(0).getDiagnostics());
		assertEquals(OperationOutcome.IssueType.PROCESSING, oo.getIssue().get(0).getCode());
		assertEquals(OperationOutcome.IssueSeverity.ERROR, oo.getIssue().get(0).getSeverity());
		assertThat(oo.getIssue().get(0).getLocation()).hasSize(2);
		assertEquals("Observation.value.ofType(Quantity)", oo.getIssue().get(0).getLocation().get(0).getValue());
		assertEquals("Line[15] Col[4]", oo.getIssue().get(0).getLocation().get(1).getValue());
	}

	@Test
	public void testValidateForCreate() {
		String methodName = "testValidateForCreate";

		Patient pat = new Patient();
		pat.setId("Patient/123");
		pat.addName().setFamily(methodName);

		try {
			myPatientDao.validate(pat, null, null, null, ValidationModeEnum.CREATE, null, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage()).contains("ID must not be populated");
		}

		pat.setId("");
		myPatientDao.validate(pat, null, null, null, ValidationModeEnum.CREATE, null, mySrd);

	}

	@Test
	public void testValidateForUpdate() {
		String methodName = "testValidateForUpdate";

		Patient pat = new Patient();
		pat.setId("Patient/123");
		pat.addName().setFamily(methodName);
		myPatientDao.validate(pat, null, null, null, ValidationModeEnum.UPDATE, null, mySrd);

		pat.setId("");

		try {
			myPatientDao.validate(pat, null, null, null, ValidationModeEnum.UPDATE, null, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage()).contains("ID must be populated");
		}

	}

	@Test
	public void testValidateRawResourceForUpdateWithId() {
		String methodName = "testValidateForUpdate";
		Patient pat = new Patient();
		pat.setId("Patient/123");
		pat.addName().setFamily(methodName);
		Parameters params = new Parameters();
		params.addParameter().setName("resource").setResource(pat);
		String rawResource = myFhirContext.newJsonParser().encodeResourceToString(params);
		myPatientDao.validate(pat, null, rawResource, EncodingEnum.JSON, ValidationModeEnum.UPDATE, null, mySrd);
	}

	@Test
	public void testValidateRawResourceForUpdateWithNoId() {
		String methodName = "testValidateForUpdate";
		Patient pat = new Patient();
		pat.addName().setFamily(methodName);
		Parameters params = new Parameters();
		params.addParameter().setName("resource").setResource(pat);
		String rawResource = myFhirContext.newJsonParser().encodeResourceToString(params);
		try {
			myPatientDao.validate(pat, null, rawResource, EncodingEnum.JSON, ValidationModeEnum.UPDATE, null, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage()).contains("ID must be populated");
		}

	}

	@Test
	public void testValidateForUpdateWithContained() {
		String methodName = "testValidateForUpdate";

		Organization org = new Organization();
		org.setId("#123");

		Patient pat = new Patient();
		pat.setId("Patient/123");
		pat.addName().setFamily(methodName);
		myPatientDao.validate(pat, null, null, null, ValidationModeEnum.UPDATE, null, mySrd);

		pat.setId("");

		try {
			myPatientDao.validate(pat, null, null, null, ValidationModeEnum.UPDATE, null, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage()).contains("ID must be populated");
		}

	}

	@Test
	public void testValidateForDelete() {
		String methodName = "testValidateForDelete";

		Organization org = new Organization();
		org.setName(methodName);
		IIdType orgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();

		Patient pat = new Patient();
		pat.addName().setFamily(methodName);
		pat.getManagingOrganization().setReference(orgId.getValue());
		IIdType patId = myPatientDao.create(pat, mySrd).getId().toUnqualifiedVersionless();

		OperationOutcome outcome = null;
		try {
			myOrganizationDao.validate(null, orgId, null, null, ValidationModeEnum.DELETE, null, mySrd);
			fail();
		} catch (ResourceVersionConflictException e) {
			outcome = (OperationOutcome) e.getOperationOutcome();
		}

		String ooString = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome);
		ourLog.info(ooString);
		assertThat(ooString).contains("Unable to delete Organization");

		pat.setId(patId);
		pat.getManagingOrganization().setReference("");
		myPatientDao.update(pat, mySrd);

		outcome = (OperationOutcome) myOrganizationDao.validate(null, orgId, null, null, ValidationModeEnum.DELETE, null, mySrd).getOperationOutcome();
		ooString = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome);
		ourLog.info(ooString);
		assertThat(ooString).contains("Ok to delete");

	}

	@Test
	public void testValidateForDeleteWithReferentialIntegrityDisabled() {
		myStorageSettings.setEnforceReferentialIntegrityOnDelete(false);
		String methodName = "testValidateForDelete";

		Organization org = new Organization();
		org.setName(methodName);
		IIdType orgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();

		Patient pat = new Patient();
		pat.addName().setFamily(methodName);
		pat.getManagingOrganization().setReference(orgId.getValue());
		IIdType patId = myPatientDao.create(pat, mySrd).getId().toUnqualifiedVersionless();

		myOrganizationDao.validate(null, orgId, null, null, ValidationModeEnum.DELETE, null, mySrd);

		myStorageSettings.setEnforceReferentialIntegrityOnDelete(true);
		try {
			myOrganizationDao.validate(null, orgId, null, null, ValidationModeEnum.DELETE, null, mySrd);
			fail();
		} catch (ResourceVersionConflictException e) {
			// good
		}

		myStorageSettings.setEnforceReferentialIntegrityOnDelete(false);


		myOrganizationDao.read(orgId);

		myOrganizationDao.delete(orgId);

		try {
			myOrganizationDao.read(orgId);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

	}

	@Test
	public void testValidateUsCoreR4Content() throws IOException {
		myStorageSettings.setAllowExternalReferences(true);

		upload("/r4/uscore/CodeSystem-cdcrec.json");
		upload("/r4/uscore/StructureDefinition-us-core-birthsex.json");
		upload("/r4/uscore/StructureDefinition-us-core-ethnicity.json");
		upload("/r4/uscore/StructureDefinition-us-core-patient.json");
		upload("/r4/uscore/StructureDefinition-us-core-race.json");
		upload("/r4/uscore/StructureDefinition-us-core-observation-lab.json");
		upload("/r4/uscore/ValueSet-birthsex.json");
		upload("/r4/uscore/ValueSet-detailed-ethnicity.json");
		upload("/r4/uscore/ValueSet-detailed-race.json");
		upload("/r4/uscore/ValueSet-omb-ethnicity-category.json");
		upload("/r4/uscore/ValueSet-omb-race-category.json");
		upload("/r4/uscore/ValueSet-us-core-usps-state.json");

		myTerminologyDeferredStorageSvc.saveAllDeferred();

		{
			String resource = loadResource("/r4/uscore/patient-resource-badcode.json");
			IBaseResource parsedResource = myFhirContext.newJsonParser().parseResource(resource);
			MethodOutcome result = myPatientDao.validate((Patient) parsedResource, null, resource, null, null, null, mySrd);
			OperationOutcome oo = (OperationOutcome) result.getOperationOutcome();
			assertHasErrors(oo);
			String encoded = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo);
			ourLog.info("Outcome:\n{}", encoded);
			assertThat(encoded).contains("Unable to validate code urn:oid:2.16.840.1.113883.6.238#2106-3AAA");
		}
		{
			String resource = loadResource("/r4/uscore/patient-resource-good.json");
			IBaseResource parsedResource = myFhirContext.newJsonParser().parseResource(resource);
			MethodOutcome outcome = myPatientDao.validate((Patient) parsedResource, null, resource, null, null, null, mySrd);
			OperationOutcome oo = (OperationOutcome) outcome.getOperationOutcome();
			assertHasNoErrors(oo);
			String encoded = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo);
			ourLog.info("Outcome:\n{}", encoded);
			assertThat(encoded).contains("No issues detected");
		}
		{
			String resource = loadResource("/r4/uscore/observation-resource-good.json");
			IBaseResource parsedResource = myFhirContext.newJsonParser().parseResource(resource);
			MethodOutcome outcome = myObservationDao.validate((Observation) parsedResource, null, resource, null, null, null, mySrd);
			OperationOutcome oo = (OperationOutcome) outcome.getOperationOutcome();
			assertHasNoErrors(oo);
			String encoded = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo);
			ourLog.info("Outcome:\n{}", encoded);
			assertThat(encoded).doesNotContain("error");
		}
	}

	@Test
	public void testValidateQuestionnaireResponseWithCanonicalReference() {

		Questionnaire q = new Questionnaire();
		q.setId("q");
		q.addItem().setLinkId("link0").setRequired(true).setType(Questionnaire.QuestionnaireItemType.STRING);
		q.addItem().setLinkId("link1").setRequired(true).setType(Questionnaire.QuestionnaireItemType.STRING);
		q.setUrl("http://foo/q");
		myQuestionnaireDao.update(q);

		QuestionnaireResponse qa = new QuestionnaireResponse();
		qa.getText().setStatus(Narrative.NarrativeStatus.GENERATED).setDivAsString("<div>aaa</div>");
		qa.setStatus(QuestionnaireResponse.QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaireElement().setValue("http://foo/q");
		qa.addItem().setLinkId("link1").addAnswer().setValue(new StringType("FOO"));

		MethodOutcome validationOutcome = myQuestionnaireResponseDao.validate(qa, null, null, null, null, null, null);
		OperationOutcome oo = (OperationOutcome) validationOutcome.getOperationOutcome();
		assertHasErrors(oo);
		String encode = encode(oo);
		ourLog.info(encode);
		assertEquals("No response answer found for required item 'link0'", oo.getIssueFirstRep().getDiagnostics());
	}

	@Test
	public void testValidateQuestionnaireResponseWithLocalReference() {

		Questionnaire q = new Questionnaire();
		q.setId("q");
		q.addItem().setLinkId("link0").setRequired(true).setType(Questionnaire.QuestionnaireItemType.STRING);
		q.addItem().setLinkId("link1").setRequired(true).setType(Questionnaire.QuestionnaireItemType.STRING);
		q.setUrl("http://foo/q");
		myQuestionnaireDao.update(q);

		QuestionnaireResponse qa = new QuestionnaireResponse();
		qa.getText().setStatus(Narrative.NarrativeStatus.GENERATED).setDivAsString("<div>aaa</div>");
		qa.setStatus(QuestionnaireResponse.QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaireElement().setValue("http://foo/q");
		qa.addItem().setLinkId("link1").addAnswer().setValue(new StringType("FOO"));

		MethodOutcome validationOutcome = myQuestionnaireResponseDao.validate(qa, null, null, null, null, null, null);
		OperationOutcome oo = (OperationOutcome) validationOutcome.getOperationOutcome();
		assertHasErrors(oo);
		String encode = encode(oo);
		ourLog.info(encode);
		assertEquals("No response answer found for required item 'link0'", oo.getIssueFirstRep().getDiagnostics());
	}

	@Test
	public void testValidateQuestionnaireResponseWithUnknownReference() {

		Questionnaire q = new Questionnaire();
		q.setId("q");
		q.addItem().setLinkId("link0").setRequired(true).setType(Questionnaire.QuestionnaireItemType.STRING);
		q.addItem().setLinkId("link1").setRequired(true).setType(Questionnaire.QuestionnaireItemType.STRING);
		q.setUrl("http://foo/q");
		myQuestionnaireDao.update(q);

		QuestionnaireResponse qa = new QuestionnaireResponse();
		qa.getText().setStatus(Narrative.NarrativeStatus.GENERATED).setDivAsString("<div>aaa</div>");
		qa.setStatus(QuestionnaireResponse.QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaireElement().setValue("http://foo/Questionnaire/DOES_NOT_EXIST");
		qa.addItem().setLinkId("link1").addAnswer().setValue(new StringType("FOO"));

		try {
			MethodOutcome validationOutcome = myQuestionnaireResponseDao.validate(qa, null, null, null, null, null, null);
			OperationOutcome oo = (OperationOutcome) validationOutcome.getOperationOutcome();
			assertEquals("The questionnaire 'http://foo/Questionnaire/DOES_NOT_EXIST' could not be resolved, so no validation can be performed against the base questionnaire", oo.getIssueFirstRep().getDiagnostics());
		} catch (PreconditionFailedException e) {
			fail(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(e.getOperationOutcome()));
		}
	}


	@Test
	public void testValidateCodeInUnknownCodeSystemWithRequiredBinding() throws IOException {
		Condition condition = loadResourceFromClasspath(Condition.class, "/r4/code-in-unknown-system-with-required-binding.xml");

		MethodOutcome result = myConditionDao.validate(condition, null, null, null, null, null, null);
		OperationOutcome oo = (OperationOutcome) result.getOperationOutcome();
		assertHasErrors(oo);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
		assertThat(oo.getIssueFirstRep().getDiagnostics()).contains("None of the codings provided are in the value set 'Condition Clinical Status Codes' (http://hl7.org/fhir/ValueSet/condition-clinical|4.0.1), and a coding from this value set is required) (codes = http://terminology.hl7.org/CodeSystem/condition-clinical/wrong-system#notrealcode)");
	}

	private IBaseResource findResourceByIdInBundle(Bundle vss, String name) {
		IBaseResource retVal = null;
		for (BundleEntryComponent next : vss.getEntry()) {
			if (next.getResource().getIdElement().getIdPart().equals(name)) {
				retVal = next.getResource();
				break;
			}
		}
		if (retVal == null) {
			fail("Can't find VS: " + name);
		}
		return retVal;
	}

	/**
	 * Format has changed, this is out of date
	 */
	@Test
	@Disabled
	public void testValidateNewQuestionnaireFormat() throws Exception {
		String input = IOUtils.toString(FhirResourceDaoR4ValidateTest.class.getResourceAsStream("/questionnaire_r4.xml"));
		try {
			MethodOutcome results = myQuestionnaireDao.validate(null, null, input, EncodingEnum.XML, ValidationModeEnum.UPDATE, null, mySrd);
			OperationOutcome oo = (OperationOutcome) results.getOperationOutcome();
			ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
		} catch (PreconditionFailedException e) {
			// this is a failure of the test
			ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(e.getOperationOutcome()));
			throw e;
		}
	}

	@Nested
	class TestValidateUsingDifferentialProfile {
		private static final String PROFILE_URL = "http://example.com/fhir/StructureDefinition/patient-1a-extensions";

		private static final Patient PATIENT_WITH_REAL_URL = createPatient(PROFILE_URL);
		private static final Patient PATIENT_WITH_FAKE_URL = createPatient("https://www.i.do.not.exist.com");

		@Test
		public void createStructDefThenValidatePatientWithRealUrl() throws IOException {
			// setup
			createStructureDefinitionInDao();

			// execute
			final String outcomePatientValidate = validate(PATIENT_WITH_REAL_URL);

			// verify
			assertExpectedOutcome(outcomePatientValidate);
		}

		@Test
		public void validatePatientWithFakeUrlStructDefThenValidatePatientWithRealUrl() throws IOException {
			// setup
			final String outcomePatientValidateFakeUrl = validate(PATIENT_WITH_FAKE_URL);
			assertTrue(outcomePatientValidateFakeUrl.contains(I18nConstants.VALIDATION_VAL_PROFILE_UNKNOWN_NOT_POLICY));
			createStructureDefinitionInDao();

			// execute
			final String outcomePatientValidateRealUrl = validate(PATIENT_WITH_REAL_URL);

			// verify
			assertExpectedOutcome(outcomePatientValidateRealUrl);
		}

		@Test
		public void validatePatientRealUrlThenCreateStructDefThenValidatePatientWithRealUrl() throws IOException {
			// setup
			final String outcomePatientValidateInitial = validate(PATIENT_WITH_REAL_URL);
			assertTrue(outcomePatientValidateInitial.contains(I18nConstants.VALIDATION_VAL_PROFILE_UNKNOWN_NOT_POLICY));
			createStructureDefinitionInDao();

			// execute
			final String outcomePatientValidateAfterStructDef = validate(PATIENT_WITH_REAL_URL);

			// verify
			assertExpectedOutcome(outcomePatientValidateAfterStructDef);
		}

		private static void assertExpectedOutcome(String outcomeJson) {
			assertThat(outcomeJson).doesNotContain(I18nConstants.VALIDATION_VAL_PROFILE_UNKNOWN_NOT_POLICY);
			assertThat(outcomeJson).contains("No issues detected");
		}

		private String validate(Patient thePatient) {
			final MethodOutcome validateOutcome = myPatientDao.validate(thePatient, null, myFhirContext.newJsonParser().encodeResourceToString(thePatient), EncodingEnum.JSON, null, null, mySrd);
			return myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(validateOutcome.getOperationOutcome());
		}

		private void createStructureDefinitionInDao() throws IOException {
			final StructureDefinition structureDefinition = loadResourceFromClasspath(StructureDefinition.class, "/r4/profile-differential-patient-r4.json");
			myStructureDefinitionDao.create(structureDefinition, new SystemRequestDetails());
		}

		private static Patient createPatient(String theUrl) {
			final Patient patient = new Patient();
			patient.getText().setStatus(Narrative.NarrativeStatus.GENERATED);
			patient.getText().getDiv().setValue("<div>hello</div>");
			patient.getMeta().addProfile(theUrl);
			patient.setActive(true);
			return patient;
		}
	}

	@ParameterizedTest
	@CsvSource(value = {
			"INFORMATION, false",
			"INFORMATION, true",
			"WARNING,     false",
			"WARNING,     true",
			"ERROR,       false",
			"ERROR,       true",
	})
	public void testValidateWrongDisplayOnRequiredBinding(IValidationSupport.IssueSeverity theDisplayCodeMismatchIssueSeverity, boolean thePreCalculateExpansion) {
		myStorageSettings.setIssueSeverityForCodeDisplayMismatch(theDisplayCodeMismatchIssueSeverity);
		myInMemoryTerminologyServerValidationSupport.setIssueSeverityForCodeDisplayMismatch(theDisplayCodeMismatchIssueSeverity);

		StructureDefinition sd = new StructureDefinition();
		sd.setUrl("http://profile");
		sd.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sd.setType("Observation");
		sd.setAbstract(false);
		sd.setDerivation(StructureDefinition.TypeDerivationRule.CONSTRAINT);
		sd.setBaseDefinition("http://hl7.org/fhir/StructureDefinition/Observation");
		ElementDefinition codeElement = sd.getDifferential().addElement();
		codeElement.setId("Observation.code");
		codeElement.setPath("Observation.code");
		codeElement.addType().setCode("CodeableConcept");
		codeElement.getBinding().setStrength(Enumerations.BindingStrength.REQUIRED);
		codeElement.getBinding().setValueSet("http://vs");
		String encodedStructureDefinition = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(sd);

		myStructureDefinitionDao.create(sd, new SystemRequestDetails());

		CodeSystem cs = new CodeSystem();
		cs.setUrl("http://cs");
		cs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		cs.addConcept()
			.setCode("8302-2")
			.setDisplay("Body Height");
		String encodedCodeSystem = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(cs);

		myCodeSystemDao.create(cs, new SystemRequestDetails());

		ValueSet vs = new ValueSet();
		vs.setUrl("http://vs");
		vs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		vs.getCompose().addInclude().setSystem("http://cs");
		String encodedValueSet = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(vs);


		myValueSetDao.create(vs, new SystemRequestDetails());

		if (thePreCalculateExpansion) {
			myTermReadSvc.preExpandDeferredValueSetsToTerminologyTables();
		}

		Observation obs = new Observation();
		obs.getText().setStatus(Narrative.NarrativeStatus.GENERATED);
		obs.getText().setDivAsString("<div>hello</div>");
		obs.getMeta().addProfile("http://profile");
		obs.setStatus(Observation.ObservationStatus.FINAL);
		obs.getCode().addCoding()
			.setSystem("http://cs")
			.setCode("8302-2")
			.setDisplay("Body height2");
		obs.setEffective(DateTimeType.now());
		obs.addPerformer(new Reference("Practitioner/123"));
		obs.setSubject(new Reference("Patient/123"));
		obs.setValue(new Quantity(null, 123, "http://unitsofmeasure.org", "[in_i]", "in"));

		String encodedResource = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs);
		MethodOutcome outcome = myObservationDao.validate(obs, null, encodedResource, EncodingEnum.JSON, ValidationModeEnum.CREATE, null, new SystemRequestDetails());

		OperationOutcome oo = (OperationOutcome) outcome.getOperationOutcome();
		ourLog.info("Outcome: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo).replace("\"resourceType\"", "\"resType\""));

		OperationOutcome.OperationOutcomeIssueComponent badDisplayIssue;
		if (theDisplayCodeMismatchIssueSeverity == IValidationSupport.IssueSeverity.ERROR) {

			assertThat(oo.getIssue()).hasSize(2);
			badDisplayIssue = oo.getIssue().get(1);

			OperationOutcome.OperationOutcomeIssueComponent noGoodCodings = oo.getIssue().get(1);
			assertEquals("error", noGoodCodings.getSeverity().toCode());
			assertEquals("None of the codings provided are in the value set 'ValueSet[http://vs]' (http://vs), and a coding from this value set is required) (codes = http://cs#8302-2)", noGoodCodings.getDiagnostics());

		} else if (theDisplayCodeMismatchIssueSeverity == IValidationSupport.IssueSeverity.WARNING) {

			assertThat(oo.getIssue()).hasSize(1);
			badDisplayIssue = oo.getIssue().get(0);
			assertThat(badDisplayIssue.getDiagnostics()).contains("Concept Display \"Body height2\" does not match expected \"Body Height\"");
			assertEquals(OperationOutcome.IssueType.PROCESSING, badDisplayIssue.getCode());
			assertEquals(theDisplayCodeMismatchIssueSeverity.name().toLowerCase(), badDisplayIssue.getSeverity().toCode());

		} else {

			assertThat(oo.getIssue()).hasSize(1);
			badDisplayIssue = oo.getIssue().get(0);
			assertThat(badDisplayIssue.getDiagnostics()).contains("No issues detected during validation");
			assertEquals(OperationOutcome.IssueType.INFORMATIONAL, badDisplayIssue.getCode());
			assertEquals(theDisplayCodeMismatchIssueSeverity.name().toLowerCase(), badDisplayIssue.getSeverity().toCode());

		}

	}

	/**
	 * See #1780
	 */
	@Test
	public void testExpand() {

		ValueSet vs = new ValueSet();
		vs.setUrl("test.com/testValueSet");
		vs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		vs.getCompose()
			.addInclude().setSystem("http://hl7.org/fhir/action-cardinality-behavior");
		IIdType id = myValueSetDao.create(vs).getId().toUnqualifiedVersionless();

		myTermReadSvc.preExpandDeferredValueSetsToTerminologyTables();

		ValueSetExpansionOptions options = ValueSetExpansionOptions.forOffsetAndCount(0, 10000);
		ValueSet expansion = myValueSetDao.expand(id, options, mySrd);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expansion));

		assertThat(expansion.getExpansion().getContains()).hasSize(2);
	}


	@Test
	public void testKnownCodeSystemUnknownValueSetUri() {
		String loincCode = "10013-1";

		addLoincCodeToCodeSystemDao(loincCode);

		IValidationSupport.CodeValidationResult result = myValueSetDao.validateCode(new UriType("http://fooVs"), null, new StringType(loincCode), new StringType(ITermLoaderSvc.LOINC_URI), null, null, null, mySrd);

		assertFalse(result.isOk());
		assertEquals("Validator is unable to provide validation for 10013-1#http://loinc.org - Unknown or unusable ValueSet[http://fooVs]", result.getMessage());
	}

	private void addLoincCodeToCodeSystemDao(String loincCode) {
		CodeSystem cs = new CodeSystem();
		cs.setUrl(ITermLoaderSvc.LOINC_URI);
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		cs.addConcept().setCode(loincCode);
		cs.setId(LOINC_LOW);
		myCodeSystemDao.update(cs);
	}

	@Test
	public void testValidateObservationWithVitalSignsLoincCode() {
		Observation obs = new Observation();
		obs.setStatus(ObservationStatus.FINAL);
		obs.setCode(
			new CodeableConcept().addCoding(
				new Coding().setSystem("http://loinc.org").setCode("8302-2").setDisplay("Body height")
			));
		obs.getSubject().setReference("Patient/A");
		obs.getText().setStatus(Narrative.NarrativeStatus.GENERATED);
		obs.getText().getDiv().setValue("<div>hello</div>");

		obs.setEffective(new DateTimeType("2020-01-01"));

		obs.setValue(new Quantity().setUnit("cm").setValue(51));
		OperationOutcome oo = validateAndReturnOutcome(obs);
		assertHasNoErrors(oo);
	}

}
