package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.i18n.Msg;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InMemoryTerminologyServerValidationSupportTest {

	private static final Logger ourLog = LoggerFactory.getLogger(InMemoryTerminologyServerValidationSupportTest.class);
	private InMemoryTerminologyServerValidationSupport mySvc;
	private FhirContext myCtx = FhirContext.forR4();
	private DefaultProfileValidationSupport myDefaultSupport;
	private ValidationSupportChain myChain;
	private PrePopulatedValidationSupport myPrePopulated;
	private CommonCodeSystemsTerminologyService myCommonCodeSystemsTermSvc;

	@BeforeEach
	public void before() {
		mySvc = new InMemoryTerminologyServerValidationSupport(myCtx);
		myDefaultSupport = new DefaultProfileValidationSupport(myCtx);
		myPrePopulated = new PrePopulatedValidationSupport(myCtx);
		myCommonCodeSystemsTermSvc = new CommonCodeSystemsTerminologyService(myCtx);
		myChain = new ValidationSupportChain(mySvc, myPrePopulated, myDefaultSupport, myCommonCodeSystemsTermSvc);

		// Force load
		myDefaultSupport.fetchCodeSystem("http://foo");
	}

	@Test
	public void testValidateCodeWithInferredSystem_CommonCodeSystemsCs_BuiltInVs() {

		ValidationSupportContext valCtx = new ValidationSupportContext(myChain);
		ConceptValidationOptions options = new ConceptValidationOptions().setInferSystem(true);
		IValidationSupport.CodeValidationResult outcome;

		String valueSetUrl = "http://hl7.org/fhir/ValueSet/mimetypes";

		// ValidateCode
		outcome = myChain.validateCode(valCtx, options, null, "txt", null, valueSetUrl);
		assertTrue(outcome.isOk());
		assertEquals("Code was validated against in-memory expansion of ValueSet: http://hl7.org/fhir/ValueSet/mimetypes", outcome.getMessage());
		assertEquals("txt", outcome.getCode());

		// ValidateCodeInValueSet
		IBaseResource valueSet = myChain.fetchValueSet(valueSetUrl);
		assertNotNull(valueSet);
		outcome = myChain.validateCodeInValueSet(valCtx, options, null, "txt", null, valueSet);
		assertTrue(outcome.isOk());
		assertEquals("Code was validated against in-memory expansion of ValueSet: http://hl7.org/fhir/ValueSet/mimetypes", outcome.getMessage());
		assertEquals("txt", outcome.getCode());
	}

	@Test
	public void testValidateCode_UnknownCodeSystem_EnumeratedValueSet() {
		ValueSet vs = new ValueSet();
		vs.setUrl("http://vs");
		vs
			.getCompose()
			.addInclude()
			.setSystem("http://cs")
			.addConcept(new ValueSet.ConceptReferenceComponent(new CodeType("code1")))
			.addConcept(new ValueSet.ConceptReferenceComponent(new CodeType("code2")));
		myPrePopulated.addValueSet(vs);

		ValidationSupportContext valCtx = new ValidationSupportContext(myChain);
		ConceptValidationOptions options = new ConceptValidationOptions();
		IValidationSupport.CodeValidationResult outcome;

		outcome = myChain.validateCodeInValueSet(valCtx, options, "http://cs", "code1", null, vs);
		assertEquals("Code was validated against in-memory expansion of ValueSet: http://vs", outcome.getMessage());
		assertTrue(outcome.isOk());

		outcome = myChain.validateCodeInValueSet(valCtx, options, "http://cs", "code99", null, vs);
		assertNotNull(outcome);
		assertFalse(outcome.isOk());
		assertEquals("Unknown code 'http://cs#code99' for in-memory expansion of ValueSet 'http://vs'", outcome.getMessage());
		assertEquals(IValidationSupport.IssueSeverity.ERROR, outcome.getSeverity());

	}

	@Test
	public void testValidateCode_UnknownCodeSystem_EnumeratedValueSet_MultipleIncludes() {
		ValueSet vs = new ValueSet();
		vs.setUrl("http://vs");
		vs
			.getCompose()
			.addInclude()
			.setSystem("http://cs")
			.addFilter()
			.setProperty("parent")
			.setOp(ValueSet.FilterOperator.EQUAL)
			.setValue("blah");
		vs
			.getCompose()
			.addInclude()
			.setSystem("http://cs")
			.addConcept(new ValueSet.ConceptReferenceComponent(new CodeType("code1")))
			.addConcept(new ValueSet.ConceptReferenceComponent(new CodeType("code2")));
		myPrePopulated.addValueSet(vs);

		ValidationSupportContext valCtx = new ValidationSupportContext(myChain);
		ConceptValidationOptions options = new ConceptValidationOptions();
		IValidationSupport.CodeValidationResult outcome;

		outcome = myChain.validateCodeInValueSet(valCtx, options, "http://cs", "code1", null, vs);
		assertEquals("Code was validated against in-memory expansion of ValueSet: http://vs", outcome.getMessage());
		assertTrue(outcome.isOk());

		outcome = myChain.validateCodeInValueSet(valCtx, options, "http://cs", "code99", null, vs);
		assertNotNull(outcome);
		assertFalse(outcome.isOk());
		assertEquals("Failed to expand ValueSet 'http://vs' (in-memory). Could not validate code http://cs#code99. Error was: " + Msg.code(702) + "Unable to expand ValueSet because CodeSystem could not be found: http://cs", outcome.getMessage());
		assertEquals(IValidationSupport.IssueSeverity.ERROR, outcome.getSeverity());

	}

	@Test
	public void testValidateCode_UnknownCodeSystem_NonEnumeratedValueSet() {
		ValueSet vs = new ValueSet();
		vs.setUrl("http://vs");
		vs
			.getCompose()
			.addInclude()
			.setSystem("http://cs");
		myPrePopulated.addValueSet(vs);

		ValidationSupportContext valCtx = new ValidationSupportContext(myChain);
		ConceptValidationOptions options = new ConceptValidationOptions();
		IValidationSupport.CodeValidationResult outcome;

		outcome = myChain.validateCodeInValueSet(valCtx, options, "http://cs", "code99", null, vs);
		assertNotNull(outcome);
		assertFalse(outcome.isOk());
		assertEquals("Failed to expand ValueSet 'http://vs' (in-memory). Could not validate code http://cs#code99. Error was: "+ Msg.code(702) + "Unable to expand ValueSet because CodeSystem could not be found: http://cs", outcome.getMessage());
		assertEquals(IValidationSupport.IssueSeverity.ERROR, outcome.getSeverity());

	}

	@Test
	public void testValidateCodeDstu2() {
		FhirContext ctxDstu2 = FhirContext.forDstu2Hl7Org();
		PrePopulatedValidationSupportDstu2 dstu2PrePopulated = new PrePopulatedValidationSupportDstu2(ctxDstu2);
		mySvc = new InMemoryTerminologyServerValidationSupport(ctxDstu2);
		myDefaultSupport = new DefaultProfileValidationSupport(ctxDstu2);
		myChain = new ValidationSupportChain(mySvc, dstu2PrePopulated, myDefaultSupport);

		org.hl7.fhir.dstu2.model.ValueSet vs = new org.hl7.fhir.dstu2.model.ValueSet();
		vs.setUrl("http://vs");
		vs
			.getCompose()
			.addInclude()
			.setSystem("http://cs")
			.addConcept(new org.hl7.fhir.dstu2.model.ValueSet.ConceptReferenceComponent(new org.hl7.fhir.dstu2.model.CodeType("code1")))
			.addConcept(new org.hl7.fhir.dstu2.model.ValueSet.ConceptReferenceComponent(new org.hl7.fhir.dstu2.model.CodeType("code2")));
		vs.getCodeSystem()
			.addConcept(new org.hl7.fhir.dstu2.model.ValueSet.ConceptDefinitionComponent(new org.hl7.fhir.dstu2.model.CodeType("code1")))
			.addConcept(new org.hl7.fhir.dstu2.model.ValueSet.ConceptDefinitionComponent(new org.hl7.fhir.dstu2.model.CodeType("code2")));

		dstu2PrePopulated.addValueSet(vs, "http://vs", "http://cs");

		ValidationSupportContext valCtx = new ValidationSupportContext(myChain);
		ConceptValidationOptions options = new ConceptValidationOptions();

		IValidationSupport.CodeValidationResult outcome = mySvc.validateCode(valCtx, options, "http://cs", "code1", null, "http://vs");
		assertTrue(outcome.isOk());

	}



	@Test
	public void testExpandValueSet_VsIsEnumeratedWithVersionedSystem_CsOnlyDifferentVersionPresent() {
		CodeSystem cs = new CodeSystem();
		cs.setId("snomed-ct-ca-imm");
		cs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		cs.setContent(CodeSystem.CodeSystemContentMode.FRAGMENT);
		cs.setUrl("http://snomed.info/sct");
		cs.setVersion("http://snomed.info/sct/20611000087101/version/20210331");
		cs.addConcept().setCode("28571000087109").setDisplay("MODERNA COVID-19 mRNA-1273");
		myPrePopulated.addCodeSystem(cs);

		ValueSet vs = new ValueSet();
		vs.setId("vaccinecode");
		vs.setUrl("http://ehealthontario.ca/fhir/ValueSet/vaccinecode");
		vs.setVersion("0.1.17");
		vs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		ValueSet.ConceptSetComponent vsInclude = vs.getCompose().addInclude();
		vsInclude.setSystem("http://snomed.info/sct");
		vsInclude.setVersion("0.17"); // different version
		vsInclude.addConcept().setCode("28571000087109").setDisplay("MODERNA COVID-19 mRNA-1273");
		myPrePopulated.addValueSet(vs);

		ValidationSupportContext valCtx = new ValidationSupportContext(myChain);
		ConceptValidationOptions options = new ConceptValidationOptions();
		options.setValidateDisplay(true);

		String codeSystemUrl;
		String valueSetUrl;
		String code;

		IValidationSupport.ValueSetExpansionOutcome expansion = mySvc.expandValueSet(valCtx, new ValueSetExpansionOptions(), vs);
		assertNotNull(expansion.getValueSet());
		assertEquals(1, ((ValueSet)expansion.getValueSet()).getExpansion().getContains().size());

		// Validate code - good
		codeSystemUrl = "http://snomed.info/sct";
		valueSetUrl = "http://ehealthontario.ca/fhir/ValueSet/vaccinecode";
		code = "28571000087109";
		String display = null;
		IValidationSupport.CodeValidationResult outcome = mySvc.validateCode(valCtx, options, codeSystemUrl, code, display, valueSetUrl);
		assertTrue(outcome.isOk());
		assertEquals("28571000087109", outcome.getCode());
		assertEquals("MODERNA COVID-19 mRNA-1273", outcome.getDisplay());
		assertEquals("0.17", outcome.getCodeSystemVersion());

		// Validate code - good code, bad display
		codeSystemUrl = "http://snomed.info/sct";
		valueSetUrl = "http://ehealthontario.ca/fhir/ValueSet/vaccinecode";
		code = "28571000087109";
		display = "BLAH";
		outcome = mySvc.validateCode(valCtx, options, codeSystemUrl, code, display, valueSetUrl);
		assertFalse(outcome.isOk());
		assertEquals(null, outcome.getCode());
		assertEquals("MODERNA COVID-19 mRNA-1273", outcome.getDisplay());
		assertEquals("0.17", outcome.getCodeSystemVersion());

		// Validate code - good code, good display
		codeSystemUrl = "http://snomed.info/sct";
		valueSetUrl = "http://ehealthontario.ca/fhir/ValueSet/vaccinecode";
		code = "28571000087109";
		display = "MODERNA COVID-19 mRNA-1273";
		outcome = mySvc.validateCode(valCtx, options, codeSystemUrl, code, display, valueSetUrl);
		assertTrue(outcome.isOk());
		assertEquals("28571000087109", outcome.getCode());
		assertEquals("MODERNA COVID-19 mRNA-1273", outcome.getDisplay());
		assertEquals("0.17", outcome.getCodeSystemVersion());

		// Validate code - bad code
		codeSystemUrl = "http://snomed.info/sct";
		valueSetUrl = "http://ehealthontario.ca/fhir/ValueSet/vaccinecode";
		code = "BLAH";
		outcome = mySvc.validateCode(valCtx, options, codeSystemUrl, code, null, valueSetUrl);
		assertFalse(outcome.isOk());
		assertEquals(null, outcome.getCode());
		assertEquals(null, outcome.getDisplay());
		assertEquals(null, outcome.getCodeSystemVersion());
	}




	@Test
	public void testExpandValueSet_VsUsesVersionedSystem_CsIsFragmentWithoutCode() {
		CodeSystem cs = new CodeSystem();
		cs.setId("snomed-ct-ca-imm");
		cs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		cs.setContent(CodeSystem.CodeSystemContentMode.FRAGMENT);
		cs.setUrl("http://snomed.info/sct");
		cs.setVersion("http://snomed.info/sct/20611000087101/version/20210331");
		cs.addConcept().setCode("28571000087109").setDisplay("MODERNA COVID-19 mRNA-1273");
		myPrePopulated.addCodeSystem(cs);

		ValueSet vs = new ValueSet();
		vs.setId("vaccinecode");
		vs.setUrl("http://ehealthontario.ca/fhir/ValueSet/vaccinecode");
		vs.setVersion("0.1.17");
		vs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		ValueSet.ConceptSetComponent vsInclude = vs.getCompose().addInclude();
		vsInclude.setSystem("http://snomed.info/sct");
		vsInclude.setVersion("http://snomed.info/sct/20611000087101/version/20210331");
		vsInclude.addConcept().setCode("28571000087109").setDisplay("MODERNA COVID-19 mRNA-1273");
		myPrePopulated.addValueSet(vs);

		ValidationSupportContext valCtx = new ValidationSupportContext(myChain);
		ConceptValidationOptions options = new ConceptValidationOptions();

		String codeSystemUrl;
		String valueSetUrl;
		String code;

		IValidationSupport.ValueSetExpansionOutcome expansion = mySvc.expandValueSet(valCtx, new ValueSetExpansionOptions(), vs);
		assertNull(expansion.getError());
		ValueSet valueSet = (ValueSet) expansion.getValueSet();
		assertNotNull(valueSet);
		assertEquals(1, valueSet.getExpansion().getContains().size());
		assertEquals("28571000087109", valueSet.getExpansion().getContains().get(0).getCode());
		assertEquals("MODERNA COVID-19 mRNA-1273", valueSet.getExpansion().getContains().get(0).getDisplay());

		codeSystemUrl = "http://snomed.info/sct";
		valueSetUrl = "http://ehealthontario.ca/fhir/ValueSet/vaccinecode";
		code = "28571000087109";
		IValidationSupport.CodeValidationResult outcome = mySvc.validateCode(valCtx, options, codeSystemUrl, code, null, valueSetUrl);
		assertTrue(outcome.isOk());
	}

	@Test
	public void testExpandValueSet_VsUsesVersionedSystem_CsIsFragmentWithCode() {
		CodeSystem cs = new CodeSystem();
		cs.setId("snomed-ct-ca-imm");
		cs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		cs.setContent(CodeSystem.CodeSystemContentMode.FRAGMENT);
		cs.setUrl("http://snomed.info/sct");
		cs.setVersion("http://snomed.info/sct/20611000087101/version/20210331");
		cs.addConcept().setCode("28571000087109").setDisplay("MODERNA COVID-19 mRNA-1273");
		myPrePopulated.addCodeSystem(cs);

		ValueSet vs = new ValueSet();
		vs.setId("vaccinecode");
		vs.setUrl("http://ehealthontario.ca/fhir/ValueSet/vaccinecode");
		vs.setVersion("http://snomed.info/sct/20611000087101/version/20210331");
		vs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		ValueSet.ConceptSetComponent vsInclude = vs.getCompose().addInclude();
		vsInclude.setSystem("http://snomed.info/sct");
		vsInclude.setVersion("http://snomed.info/sct/20611000087101/version/20210331");
		vsInclude.addConcept().setCode("28571000087109").setDisplay("MODERNA COVID-19 mRNA-1273");
		myPrePopulated.addValueSet(vs);

		ValidationSupportContext valCtx = new ValidationSupportContext(myChain);
		ConceptValidationOptions options = new ConceptValidationOptions();

		String codeSystemUrl;
		String valueSetUrl;
		String code;
		IValidationSupport.CodeValidationResult outcome;

		// Good code
		codeSystemUrl = "http://snomed.info/sct";
		valueSetUrl = "http://ehealthontario.ca/fhir/ValueSet/vaccinecode";
		code = "28571000087109";
		outcome = mySvc.validateCode(valCtx, options, codeSystemUrl, code, null, valueSetUrl);
		assertTrue(outcome.isOk());
		assertEquals("MODERNA COVID-19 mRNA-1273", outcome.getDisplay());

		// Bad code
		codeSystemUrl = "http://snomed.info/sct";
		valueSetUrl = "http://ehealthontario.ca/fhir/ValueSet/vaccinecode";
		code = "123";
		outcome = mySvc.validateCode(valCtx, options, codeSystemUrl, code, null, valueSetUrl);
		assertFalse(outcome.isOk());

		IValidationSupport.ValueSetExpansionOutcome expansion = mySvc.expandValueSet(valCtx, new ValueSetExpansionOptions(), vs);
		assertNull(expansion.getError());
		ValueSet valueSet = (ValueSet) expansion.getValueSet();
		assertNotNull(valueSet);
		assertEquals(1, valueSet.getExpansion().getContains().size());
		assertEquals("28571000087109", valueSet.getExpansion().getContains().get(0).getCode());
		assertEquals("MODERNA COVID-19 mRNA-1273", valueSet.getExpansion().getContains().get(0).getDisplay());
	}


	@Test
	public void testExpandValueSet_VsUsesVersionedSystem_CsIsCompleteWithCode() {
		CodeSystem cs = new CodeSystem();
		cs.setId("snomed-ct-ca-imm");
		cs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		cs.setUrl("http://snomed.info/sct");
		cs.setVersion("http://snomed.info/sct/20611000087101/version/20210331");
		cs.addConcept().setCode("28571000087109").setDisplay("MODERNA COVID-19 mRNA-1273");
		myPrePopulated.addCodeSystem(cs);

		ValueSet vs = new ValueSet();
		vs.setId("vaccinecode");
		vs.setUrl("http://ehealthontario.ca/fhir/ValueSet/vaccinecode");
		vs.setVersion("http://snomed.info/sct/20611000087101/version/20210331");
		vs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		ValueSet.ConceptSetComponent vsInclude = vs.getCompose().addInclude();
		vsInclude.setSystem("http://snomed.info/sct");
		vsInclude.setVersion("http://snomed.info/sct/20611000087101/version/20210331");
		vsInclude.addConcept().setCode("28571000087109").setDisplay("MODERNA COVID-19 mRNA-1273");
		myPrePopulated.addValueSet(vs);

		ValidationSupportContext valCtx = new ValidationSupportContext(myChain);
		ConceptValidationOptions options = new ConceptValidationOptions();

		String codeSystemUrl;
		String valueSetUrl;
		String code;
		IValidationSupport.CodeValidationResult outcome;

		// Good code
		codeSystemUrl = "http://snomed.info/sct";
		valueSetUrl = "http://ehealthontario.ca/fhir/ValueSet/vaccinecode";
		code = "28571000087109";
		outcome = mySvc.validateCode(valCtx, options, codeSystemUrl, code, null, valueSetUrl);
		assertTrue(outcome.isOk());
		assertEquals("MODERNA COVID-19 mRNA-1273", outcome.getDisplay());

		// Bad code
		codeSystemUrl = "http://snomed.info/sct";
		valueSetUrl = "http://ehealthontario.ca/fhir/ValueSet/vaccinecode";
		code = "123";
		outcome = mySvc.validateCode(valCtx, options, codeSystemUrl, code, null, valueSetUrl);
		assertFalse(outcome.isOk());

		IValidationSupport.ValueSetExpansionOutcome expansion = mySvc.expandValueSet(valCtx, new ValueSetExpansionOptions(), vs);
		ValueSet valueSet = (ValueSet) expansion.getValueSet();
		assertNotNull(valueSet);
		assertEquals(1, valueSet.getExpansion().getContains().size());
		assertEquals("28571000087109", valueSet.getExpansion().getContains().get(0).getCode());
		assertEquals("MODERNA COVID-19 mRNA-1273", valueSet.getExpansion().getContains().get(0).getDisplay());
	}

    @ParameterizedTest
	 @ValueSource(strings = {"http://terminology.hl7.org/CodeSystem/v2-0360|2.7","http://terminology.hl7.org/CodeSystem/v2-0360"})
    void testValidateCodeInValueSet_VsExpandedWithIncludes(String theCodeSystemUri) {
		 ConceptValidationOptions options = new ConceptValidationOptions();
		 ValidationSupportContext valCtx = new ValidationSupportContext(myChain);
		 String codeMD = "MD";

		 CodeSystem cs = new CodeSystem();
		 cs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		 cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		 cs.setUrl(theCodeSystemUri);
		 cs.addConcept()
			 .setCode(codeMD)
			 .setDisplay("Doctor of Medicine");
		 myPrePopulated.addCodeSystem(cs);

		 ValueSet theValueSet = new ValueSet();
		 theValueSet.setUrl("http://someValueSetURL");
		 theValueSet.setVersion("0360");
		 theValueSet.getCompose().addInclude().setSystem(theCodeSystemUri);

		 String theCodeToValidateCodeSystemUrl = theCodeSystemUri;
		 String theCodeToValidate = codeMD;

		 IValidationSupport.CodeValidationResult codeValidationResult = mySvc.validateCodeInValueSet(
			 valCtx,
			 options,
			 theCodeToValidateCodeSystemUrl,
			 theCodeToValidate,
			 null,
			 theValueSet);

		 assertTrue(codeValidationResult.isOk());
	 }

    private static class PrePopulatedValidationSupportDstu2 extends PrePopulatedValidationSupport {
		private final Map<String, IBaseResource> myDstu2ValueSets;

		PrePopulatedValidationSupportDstu2(FhirContext theFhirContext) {
			super(theFhirContext);
			myDstu2ValueSets = new HashMap<>();
		}

		public void addValueSet(org.hl7.fhir.dstu2.model.ValueSet theValueSet, String valueSetUrl, String codeSystemUrl) {
			myDstu2ValueSets.put(valueSetUrl, theValueSet);
			myDstu2ValueSets.put(codeSystemUrl, theValueSet);
		}

		@Override
		public IBaseResource fetchValueSet(String theUri) {
			return myDstu2ValueSets.get(theUri);
		}

		@Override
		public IBaseResource fetchCodeSystem(String theSystem) {
			return myDstu2ValueSets.get(theSystem);
		}

	}

}
