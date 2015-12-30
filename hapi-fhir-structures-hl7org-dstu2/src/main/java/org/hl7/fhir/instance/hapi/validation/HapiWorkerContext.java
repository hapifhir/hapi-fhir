package org.hl7.fhir.instance.hapi.validation;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.formats.IParser;
import org.hl7.fhir.instance.formats.ParserType;
import org.hl7.fhir.instance.hapi.validation.IValidationSupport.CodeValidationResult;
import org.hl7.fhir.instance.model.CodeableConcept;
import org.hl7.fhir.instance.model.Coding;
import org.hl7.fhir.instance.model.ConceptMap;
import org.hl7.fhir.instance.model.OperationOutcome.IssueSeverity;
import org.hl7.fhir.instance.model.Resource;
import org.hl7.fhir.instance.model.ValueSet;
import org.hl7.fhir.instance.model.ValueSet.ConceptDefinitionComponent;
import org.hl7.fhir.instance.model.ValueSet.ConceptReferenceComponent;
import org.hl7.fhir.instance.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.instance.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.instance.terminologies.ValueSetExpander;
import org.hl7.fhir.instance.terminologies.ValueSetExpanderFactory;
import org.hl7.fhir.instance.terminologies.ValueSetExpanderSimple;
import org.hl7.fhir.instance.utils.EOperationOutcome;
import org.hl7.fhir.instance.utils.INarrativeGenerator;
import org.hl7.fhir.instance.utils.IWorkerContext;
import org.hl7.fhir.instance.validation.IResourceValidator;

import ca.uhn.fhir.context.FhirContext;

public final class HapiWorkerContext implements IWorkerContext, ValueSetExpanderFactory, ValueSetExpander {
  private final FhirContext myCtx;
  private IValidationSupport myValidationSupport;

  public HapiWorkerContext(FhirContext theCtx, IValidationSupport theValidationSupport) {
    myCtx = theCtx;
    myValidationSupport = theValidationSupport;
  }

  @Override
  public ValueSetExpansionComponent expandVS(ConceptSetComponent theInc) {
    return myValidationSupport.expandValueSet(myCtx, theInc);
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
  public <T extends Resource> T fetchResource(Class<T> theClass, String theUri) throws EOperationOutcome, Exception {
    if (myValidationSupport == null) {
      return null;
    } else {
      return myValidationSupport.fetchResource(myCtx, theClass, theUri);
    }
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
  public <T extends Resource> boolean hasResource(Class<T> theClass_, String theUri) {
    throw new UnsupportedOperationException();
  }

  @Override
  public IParser newJsonParser() {
    throw new UnsupportedOperationException();
  }

  @Override
  public IResourceValidator newValidator() throws Exception {
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
      if (StringUtils.equals(theSystem, nextComposeConceptSet.getSystem())) {
        for (ConceptReferenceComponent nextComposeCode : nextComposeConceptSet.getConcept()) {
          ConceptDefinitionComponent conceptDef = new ConceptDefinitionComponent();
          conceptDef.setCode(nextComposeCode.getCode());
          conceptDef.setDisplay(nextComposeCode.getDisplay());
          ValidationResult retVal = validateCodeSystem(theCode, conceptDef);
          if (retVal != null && retVal.isOk()) {
            return retVal;
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

  @Override
  public ValueSetExpander getExpander() {
    return this;
  }

  @Override
  public ValueSetExpansionOutcome expand(ValueSet theSource) throws ETooCostly, Exception {
    ValueSetExpander vse = new ValueSetExpanderSimple(this, this);
    ValueSetExpansionOutcome vso = vse.expand(theSource);
    if (vso.getError() != null) {
      return null;
    } else {
      return vso;
    }
  }

  @Override
  public List<ConceptMap> findMapsForSource(String theUrl) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ValueSetExpansionOutcome expandVS(ValueSet theSource, boolean theCacheOk) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ValidationResult validateCode(Coding theCode, ValueSet theVs) {
    String system = theCode.getSystem();
    String code = theCode.getCode();
    String display = theCode.getDisplay();
    return validateCode(system, code, display, theVs);
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
}