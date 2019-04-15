package org.hl7.fhir.r4.hapi.validation;

import ca.uhn.fhir.context.FhirContext;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.hapi.ctx.IValidationSupport;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.MetadataResource;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.r4.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.r4.terminologies.ValueSetExpander;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This class is an implementation of {@link IValidationSupport} which may be pre-populated
 * with a collection of validation resources to be used by the validator.
 */
public class PrePopulatedValidationSupport implements IValidationSupport {

  private Map<String, CodeSystem> myCodeSystems;
  private Map<String, StructureDefinition> myStructureDefinitions;
  private Map<String, ValueSet> myValueSets;

  /**
   * Constructor
   */
  public PrePopulatedValidationSupport() {
    myStructureDefinitions = new HashMap<>();
    myValueSets = new HashMap<>();
    myCodeSystems = new HashMap<>();
  }


  /**
   * Constructor
   *
   * @param theStructureDefinitions The StructureDefinitions to be returned by this module. Keys are the logical URL for the resource, and
   *                                values are the resource itself.
   * @param theValueSets            The ValueSets to be returned by this module. Keys are the logical URL for the resource, and values are
   *                                the resource itself.
   * @param theCodeSystems          The CodeSystems to be returned by this module. Keys are the logical URL for the resource, and values are
   *                                the resource itself.
   */
  public PrePopulatedValidationSupport(Map<String, StructureDefinition> theStructureDefinitions, Map<String, ValueSet> theValueSets, Map<String, CodeSystem> theCodeSystems) {
    myStructureDefinitions = theStructureDefinitions;
    myValueSets = theValueSets;
    myCodeSystems = theCodeSystems;
  }

  /**
   * Add a new CodeSystem resource which will be available to the validator. Note that
   * {@link CodeSystem#getUrl() the URL field) in this resource must contain a value as this
   * value will be used as the logical URL.
   * <p>
   * Note that if the URL is a canonical FHIR URL (e.g. http://hl7.org/StructureDefinition/Extension),
   * it will be stored in three ways:
   * <ul>
   * <li>Extension</li>
   * <li>StructureDefinition/Extension</li>
   * <li>http://hl7.org/StructureDefinition/Extension</li>
   * </ul>
   * </p>
   */
  public void addCodeSystem(CodeSystem theCodeSystem) {
    Validate.notBlank(theCodeSystem.getUrl(), "theCodeSystem.getUrl() must not return a value");
    addToMap(theCodeSystem, myCodeSystems, theCodeSystem.getUrl());
  }

  /**
   * Add a new StructureDefinition resource which will be available to the validator. Note that
   * {@link StructureDefinition#getUrl() the URL field) in this resource must contain a value as this
   * value will be used as the logical URL.
   * <p>
   * Note that if the URL is a canonical FHIR URL (e.g. http://hl7.org/StructureDefinition/Extension),
   * it will be stored in three ways:
   * <ul>
   * <li>Extension</li>
   * <li>StructureDefinition/Extension</li>
   * <li>http://hl7.org/StructureDefinition/Extension</li>
   * </ul>
   * </p>
   */
  public void addStructureDefinition(StructureDefinition theStructureDefinition) {
    Validate.notBlank(theStructureDefinition.getUrl(), "theStructureDefinition.getUrl() must not return a value");
    addToMap(theStructureDefinition, myStructureDefinitions, theStructureDefinition.getUrl());
  }

  private <T extends MetadataResource> void addToMap(T theStructureDefinition, Map<String, T> map, String theUrl) {
    if (isNotBlank(theUrl)) {
      map.put(theUrl, theStructureDefinition);

      int lastSlashIdx = theUrl.lastIndexOf('/');
      if (lastSlashIdx != -1) {
        map.put(theUrl.substring(lastSlashIdx + 1), theStructureDefinition);
        int previousSlashIdx = theUrl.lastIndexOf('/', lastSlashIdx - 1);
        if (previousSlashIdx != -1) {
          map.put(theUrl.substring(previousSlashIdx + 1), theStructureDefinition);
        }
      }

    }
  }

  /**
   * Add a new ValueSet resource which will be available to the validator. Note that
   * {@link ValueSet#getUrl() the URL field) in this resource must contain a value as this
   * value will be used as the logical URL.
   * <p>
   * Note that if the URL is a canonical FHIR URL (e.g. http://hl7.org/StructureDefinition/Extension),
   * it will be stored in three ways:
   * <ul>
   * <li>Extension</li>
   * <li>StructureDefinition/Extension</li>
   * <li>http://hl7.org/StructureDefinition/Extension</li>
   * </ul>
   * </p>
   */
  public void addValueSet(ValueSet theValueSet) {
    Validate.notBlank(theValueSet.getUrl(), "theValueSet.getUrl() must not return a value");
    addToMap(theValueSet, myValueSets, theValueSet.getUrl());
  }

  @Override
  public ValueSetExpander.ValueSetExpansionOutcome expandValueSet(FhirContext theContext, ConceptSetComponent theInclude) {
    return null;
  }

  @Override
  public List<IBaseResource> fetchAllConformanceResources(FhirContext theContext) {
    ArrayList<IBaseResource> retVal = new ArrayList<>();
    retVal.addAll(myCodeSystems.values());
    retVal.addAll(myStructureDefinitions.values());
    retVal.addAll(myValueSets.values());
    return retVal;
  }

  @Override
  public List<StructureDefinition> fetchAllStructureDefinitions(FhirContext theContext) {
    return new ArrayList<StructureDefinition>(myStructureDefinitions.values());
  }

 	@Override
	public CodeSystem fetchCodeSystem(FhirContext theContext, String uri) {
		return myCodeSystems.get(uri);
	}

	@Override
	public ValueSet fetchValueSet(FhirContext theContext, String uri) {
		return myValueSets.get(uri);
	}


	@SuppressWarnings("unchecked")
  @Override
  public <T extends IBaseResource> T fetchResource(FhirContext theContext, Class<T> theClass, String theUri) {
    if (theClass.equals(StructureDefinition.class)) {
      return (T) myStructureDefinitions.get(theUri);
    }
    if (theClass.equals(ValueSet.class)) {
      return (T) myValueSets.get(theUri);
    }
    if (theClass.equals(CodeSystem.class)) {
      return (T) myCodeSystems.get(theUri);
    }
    return null;
  }

  @Override
  public StructureDefinition fetchStructureDefinition(FhirContext theCtx, String theUrl) {
    return myStructureDefinitions.get(theUrl);
  }

  @Override
  public boolean isCodeSystemSupported(FhirContext theContext, String theSystem) {
    return false;
  }

  @Override
  public CodeValidationResult validateCode(FhirContext theContext, String theCodeSystem, String theCode, String theDisplay) {
    return null;
  }

}
