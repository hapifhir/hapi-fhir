package ca.uhn.fhir.validation;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.instance.model.ValueSet;
import org.hl7.fhir.instance.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.instance.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;

public class ValidationSupportChain implements IValidationSupport {

  private List<IValidationSupport> myChain;

  public ValidationSupportChain(IValidationSupport... theValidationSupportModules) {
    myChain = new ArrayList<IValidationSupport>();
    for (IValidationSupport next : theValidationSupportModules) {
      if (next != null) {
        myChain.add(next);
      }
    }
  }

  @Override
  public ValueSetExpansionComponent expandValueSet(ConceptSetComponent theInclude) {
    for (IValidationSupport next : myChain) {
      if (next.isCodeSystemSupported(theInclude.getSystem())) {
        return next.expandValueSet(theInclude);
      }
    }
    return myChain.get(0).expandValueSet(theInclude);
  }

  @Override
  public ValueSet fetchCodeSystem(String theSystem) {
    for (IValidationSupport next : myChain) {
      ValueSet retVal = next.fetchCodeSystem(theSystem);
      if (retVal != null) {
        return retVal;
      }
    }
    return null;
  }

  @Override
  public <T extends IBaseResource> T fetchResource(FhirContext theContext, Class<T> theClass, String theUri) {
    for (IValidationSupport next : myChain) {
      T retVal = next.fetchResource(theContext, theClass, theUri);
      if (retVal != null) {
        return retVal;
      }
    }
    return null;
  }

  @Override
  public boolean isCodeSystemSupported(String theSystem) {
    for (IValidationSupport next : myChain) {
      if (next.isCodeSystemSupported(theSystem)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public CodeValidationResult validateCode(String theCodeSystem, String theCode, String theDisplay) {
    for (IValidationSupport next : myChain) {
      if (next.isCodeSystemSupported(theCodeSystem)) {
        return next.validateCode(theCodeSystem, theCode, theDisplay);
      }
    }
    return myChain.get(0).validateCode(theCodeSystem, theCode, theDisplay);
  }

}
