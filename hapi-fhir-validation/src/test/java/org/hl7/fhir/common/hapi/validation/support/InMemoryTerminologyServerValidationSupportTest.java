package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTerminologyServerValidationSupportTest {

	private InMemoryTerminologyServerValidationSupport mySvc;
	private FhirContext myCtx = FhirContext.forR4();
	private DefaultProfileValidationSupport myDefaultSupport;
	private ValidationSupportChain myChain;
	private PrePopulatedValidationSupport myPrePopulated;

	@BeforeEach
	public void before( ){
		mySvc = new InMemoryTerminologyServerValidationSupport(myCtx);
		myDefaultSupport = new DefaultProfileValidationSupport(myCtx);
		myPrePopulated = new PrePopulatedValidationSupport(myCtx);
		myChain = new ValidationSupportChain(mySvc,myPrePopulated, myDefaultSupport);

		// Force load
		myDefaultSupport.fetchCodeSystem("http://foo");
	}

	@Test
	public void testValidateCodeInUnknownCodeSystemWithEnumeratedValueSet() {
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

		IValidationSupport.CodeValidationResult outcome = myChain.validateCodeInValueSet(valCtx, options, "http://cs", "code1", null, vs);
		assertTrue(outcome.isOk());

		outcome = myChain.validateCodeInValueSet(valCtx, options, "http://cs", "code99", null, vs);
		assertNull(outcome);

	}


}
