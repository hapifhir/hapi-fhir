package org.hl7.fhir.instance.hapi.validation;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.hl7.fhir.instance.model.Bundle;
import org.hl7.fhir.instance.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.instance.model.IdType;
import org.hl7.fhir.instance.model.OperationOutcome.IssueSeverity;
import org.hl7.fhir.instance.model.ValueSet;
import org.hl7.fhir.instance.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.instance.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

public class DefaultProfileValidationSupport implements IValidationSupport {

  private Map<String, ValueSet> myDefaultValueSets;
  private Map<String, ValueSet> myCodeSystems;

  @Override
  public boolean isCodeSystemSupported(FhirContext theContext, String theSystem) {
    return false;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends IBaseResource> T fetchResource(FhirContext theContext, Class<T> theClass, String theUri) {
    if (theUri.startsWith("http://hl7.org/fhir/StructureDefinition/")) {
      return (T) FhirInstanceValidator.loadProfileOrReturnNull(null, theContext, theUri.substring("http://hl7.org/fhir/StructureDefinition/".length()));
    }
    if (theUri.startsWith("http://hl7.org/fhir/ValueSet/")) {
      Map<String, ValueSet> defaultValueSets = myDefaultValueSets;
      if (defaultValueSets == null) {
        String path = theContext.getVersion().getPathToSchemaDefinitions().replace("/schema", "/valueset") + "/valuesets.xml";
        InputStream valuesetText = DefaultProfileValidationSupport.class.getResourceAsStream(path);
        if (valuesetText == null) {
          return null;
        }
        InputStreamReader reader;
        try {
          reader = new InputStreamReader(valuesetText, "UTF-8");
        } catch (UnsupportedEncodingException e) {
          // Shouldn't happen!
          throw new InternalErrorException("UTF-8 encoding not supported on this platform", e);
        }

        defaultValueSets = new HashMap<String, ValueSet>();

        FhirContext ctx = FhirInstanceValidator.getHl7OrgDstu2Ctx(theContext);
        Bundle bundle = ctx.newXmlParser().parseResource(Bundle.class, reader);
        for (BundleEntryComponent next : bundle.getEntry()) {
          IdType nextId = new IdType(next.getFullUrl());
          if (nextId.isEmpty() || !nextId.getValue().startsWith("http://hl7.org/fhir/ValueSet/")) {
            continue;
          }
          defaultValueSets.put(nextId.toVersionless().getValue(), (ValueSet) next.getResource());
        }

        myDefaultValueSets = defaultValueSets;
      }

      return (T) defaultValueSets.get(theUri);
    }

    return null;
  }

  @Override
  public CodeValidationResult validateCode(FhirContext theContext, String theCodeSystem, String theCode, String theDisplay) {
    return new CodeValidationResult(IssueSeverity.INFORMATION, "Unknown code: " + theCodeSystem + " / " + theCode);
  }

  @Override
  public ValueSet fetchCodeSystem(FhirContext theContext, String theSystem) {
    Map<String, ValueSet> codeSystems = myCodeSystems;
    if (codeSystems == null) {
      codeSystems = new HashMap<String, ValueSet>();

      loadCodeSystems(theContext, codeSystems, "/org/hl7/fhir/instance/model/valueset/valuesets.xml");
      loadCodeSystems(theContext, codeSystems, "/org/hl7/fhir/instance/model/valueset/v2-tables.xml");
      loadCodeSystems(theContext, codeSystems, "/org/hl7/fhir/instance/model/valueset/v3-codesystems.xml");

      myCodeSystems = codeSystems;
    }

    return codeSystems.get(theSystem);
  }

  private void loadCodeSystems(FhirContext theContext, Map<String, ValueSet> codeSystems, String file) {
    InputStream valuesetText = DefaultProfileValidationSupport.class.getResourceAsStream(file);
    if (valuesetText != null) {
      InputStreamReader reader;
      try {
        reader = new InputStreamReader(valuesetText, "UTF-8");
      } catch (UnsupportedEncodingException e) {
        // Shouldn't happen!
        throw new InternalErrorException("UTF-8 encoding not supported on this platform", e);
      }

      FhirContext ctx = FhirInstanceValidator.getHl7OrgDstu2Ctx(theContext);
      Bundle bundle = ctx.newXmlParser().parseResource(Bundle.class, reader);
      for (BundleEntryComponent next : bundle.getEntry()) {
        ValueSet nextValueSet = (ValueSet) next.getResource();
        String system = nextValueSet.getCodeSystem().getSystem();
        if (isNotBlank(system)) {
          codeSystems.put(system, nextValueSet);
        }
      }
    }
  }

  @Override
  public ValueSetExpansionComponent expandValueSet(FhirContext theContext, ConceptSetComponent theInclude) {
    return null;
  }

}
