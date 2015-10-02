package ca.uhn.fhir.validation;

import java.util.List;

import org.hl7.fhir.instance.formats.IParser;
import org.hl7.fhir.instance.formats.ParserType;
import org.hl7.fhir.instance.model.ConceptMap;
import org.hl7.fhir.instance.model.Resource;
import org.hl7.fhir.instance.model.ValueSet;
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
import ca.uhn.fhir.validation.IValidationSupport.CodeValidationResult;

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
  public ValueSetExpansionOutcome expandVS(ValueSet theSource) {
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
  public ValidationResult validateCode(String theSystem, String theCode, String theDisplay,
      ConceptSetComponent theVsi) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ValidationResult validateCode(String theSystem, String theCode, String theDisplay, ValueSet theVs) {
    throw new UnsupportedOperationException();
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
}