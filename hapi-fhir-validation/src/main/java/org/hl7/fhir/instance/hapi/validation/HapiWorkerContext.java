package org.hl7.fhir.instance.hapi.validation;

import ca.uhn.fhir.context.FhirContext;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.dstu2.formats.IParser;
import org.hl7.fhir.dstu2.formats.ParserType;
import org.hl7.fhir.instance.hapi.validation.IValidationSupport.CodeValidationResult;
import org.hl7.fhir.dstu2.model.*;
import org.hl7.fhir.dstu2.model.ValueSet.ConceptDefinitionComponent;
import org.hl7.fhir.dstu2.model.ValueSet.ConceptReferenceComponent;
import org.hl7.fhir.dstu2.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.dstu2.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.dstu2.terminologies.ValueSetExpander;
import org.hl7.fhir.dstu2.terminologies.ValueSetExpanderFactory;
import org.hl7.fhir.dstu2.terminologies.ValueSetExpanderSimple;
import org.hl7.fhir.dstu2.utils.INarrativeGenerator;
import org.hl7.fhir.dstu2.utils.IResourceValidator;
import org.hl7.fhir.dstu2.utils.IWorkerContext;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueSeverity;

import java.io.IOException;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public final class HapiWorkerContext implements IWorkerContext, ValueSetExpanderFactory, ValueSetExpander {
	private final FhirContext myCtx;
	private IValidationSupport myValidationSupport;

	public HapiWorkerContext(FhirContext theCtx, IValidationSupport theValidationSupport) {
		myCtx = theCtx;
		myValidationSupport = theValidationSupport;
	}

	@Override
	public List<StructureDefinition> allStructures() {
		return myValidationSupport.allStructures();
	}

	@Override
	public StructureDefinition fetchTypeDefinition(String theUri) {
		return fetchResource(StructureDefinition.class, theUri);
	}

	@Override
	public ValueSetExpansionOutcome expand(ValueSet theSource) throws IOException, ETooCostly {
		ValueSetExpander vse = new ValueSetExpanderSimple(this, this);
		ValueSetExpansionOutcome vso = vse.expand(theSource);
		if (vso.getError() != null) {
			return null;
		} else {
			return vso;
		}
	}

	@Override
	public ValueSetExpansionComponent expandVS(ConceptSetComponent theInc) {
		return myValidationSupport.expandValueSet(myCtx, theInc);
	}

	@Override
	public ValueSetExpansionOutcome expandVS(ValueSet theSource, boolean theCacheOk) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ValueSet fetchCodeSystem(String theSystem) {
		if (myValidationSupport == null) {
			return null;
		} else {
			return myValidationSupport.fetchCodeSystem(myCtx, theSystem);
		}
	}

	@Override
	public <T extends Resource> T fetchResource(Class<T> theClass, String theUri) {
		if (myValidationSupport == null) {
			return null;
		} else {
			return myValidationSupport.fetchResource(myCtx, theClass, theUri);
		}
	}

	@Override
	public List<ConceptMap> findMapsForSource(String theUrl) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getAbbreviation(String theName) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ValueSetExpander getExpander() {
		return this;
	}

	@Override
	public INarrativeGenerator getNarrativeGenerator(String thePrefix, String theBasePath) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IParser getParser(ParserType theType) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IParser getParser(String theType) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<String> getResourceNames() {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T extends Resource> boolean hasResource(Class<T> theClass_, String theUri) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IParser newJsonParser() {
		throw new UnsupportedOperationException();
	}

	@Override
	public IResourceValidator newValidator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public IParser newXmlParser() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsSystem(String theSystem) {
		if (myValidationSupport == null) {
			return false;
		} else {
			return myValidationSupport.isCodeSystemSupported(myCtx, theSystem);
		}
	}

	@Override
	public ValidationResult validateCode(CodeableConcept theCode, ValueSet theVs) {
		for (Coding next : theCode.getCoding()) {
			ValidationResult retVal = validateCode(next, theVs);
			if (retVal != null && retVal.isOk()) {
				return retVal;
			}
		}

		return new ValidationResult(null, null);
	}

	@Override
	public ValidationResult validateCode(Coding theCode, ValueSet theVs) {
		String system = theCode.getSystem();
		String code = theCode.getCode();
		String display = theCode.getDisplay();
		return validateCode(system, code, display, theVs);
	}

	@Override
	public ValidationResult validateCode(String theSystem, String theCode, String theDisplay) {
		CodeValidationResult result = myValidationSupport.validateCode(myCtx, theSystem, theCode, theDisplay);
		if (result == null) {
			return null;
		}
		return new ValidationResult(result.getSeverity(), result.getMessage(), result.asConceptDefinition());
	}

	@Override
	public ValidationResult validateCode(String theSystem, String theCode, String theDisplay, ConceptSetComponent theVsi) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ValidationResult validateCode(String theSystem, String theCode, String theDisplay, ValueSet theVs) {

		if (theSystem == null || StringUtils.equals(theSystem, theVs.getCodeSystem().getSystem())) {
			for (ConceptDefinitionComponent next : theVs.getCodeSystem().getConcept()) {
				ValidationResult retVal = validateCodeSystem(theCode, next);
				if (retVal != null && retVal.isOk()) {
					return retVal;
				}
			}
		}

		for (ConceptSetComponent nextComposeConceptSet : theVs.getCompose().getInclude()) {

			String nextSystem = theSystem;
			if (nextSystem == null && isNotBlank(nextComposeConceptSet.getSystem())) {
				nextSystem = nextComposeConceptSet.getSystem();
			}

			if (StringUtils.equals(nextSystem, nextComposeConceptSet.getSystem())) {
				for (ConceptReferenceComponent nextComposeCode : nextComposeConceptSet.getConcept()) {
					ConceptDefinitionComponent conceptDef = new ConceptDefinitionComponent();
					conceptDef.setCode(nextComposeCode.getCode());
					conceptDef.setDisplay(nextComposeCode.getDisplay());
					ValidationResult retVal = validateCodeSystem(theCode, conceptDef);
					if (retVal != null && retVal.isOk()) {
						return retVal;
					}
				}

				if (nextComposeConceptSet.getConcept().isEmpty()){
					ValidationResult result = validateCode(nextSystem, theCode, null);
					if (result.isOk()){
						return result;
					}
				}
			}
		}

		return new ValidationResult(IssueSeverity.ERROR, "Unknown code[" + theCode + "] in system[" + theSystem + "]");
	}

	private ValidationResult validateCodeSystem(String theCode, ConceptDefinitionComponent theConcept) {
		if (StringUtils.equals(theCode, theConcept.getCode())) {
			return new ValidationResult(theConcept);
		} else {
			for (ConceptDefinitionComponent next : theConcept.getConcept()) {
				ValidationResult retVal = validateCodeSystem(theCode, next);
				if (retVal != null && retVal.isOk()) {
					return retVal;
				}
			}
			return null;
		}
	}
}
