package org.hl7.fhir.dstu3.hapi.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;

/**
 * This class is an implementation of {@link IValidationSupport} which may be pre-populated
 * with a collection of validation resources to be used by the validator.
 */
public class PrePopulatedValidationSupport implements IValidationSupport {

	private Map<String, StructureDefinition> myStructureDefinitions;
	private Map<String, ValueSet> myValueSets;
	private Map<String, CodeSystem> myCodeSystems;

	/**
	 * Constructor
	 */
	public PrePopulatedValidationSupport() {
		myStructureDefinitions = new HashMap<String,StructureDefinition>();
		myValueSets = new HashMap<String,ValueSet>();
		myCodeSystems = new HashMap<String,CodeSystem>();
	}
	
	
	/**
	 * Add a new StructureDefinition resource which will be available to the validator. Note that
	 * {@link StructureDefinition#getUrl() the URL field) in this resource must contain a value as this
	 * value will be used as the logical URL.
	 */
	public void addStructureDefinition(StructureDefinition theStructureDefinition) {
		Validate.notBlank(theStructureDefinition.getUrl(), "theStructureDefinition.getUrl() must not return a value");
		myStructureDefinitions.put(theStructureDefinition.getUrl(), theStructureDefinition);
	}
	
	/**
	 * Add a new ValueSet resource which will be available to the validator. Note that
	 * {@link ValueSet#getUrl() the URL field) in this resource must contain a value as this
	 * value will be used as the logical URL.
	 */
	public void addValueSet(ValueSet theValueSet) {
		Validate.notBlank(theValueSet.getUrl(), "theValueSet.getUrl() must not return a value");
		myValueSets.put(theValueSet.getUrl(), theValueSet);
	}

	/**
	 * Add a new CodeSystem resource which will be available to the validator. Note that
	 * {@link CodeSystem#getUrl() the URL field) in this resource must contain a value as this
	 * value will be used as the logical URL.
	 */
	public void addCodeSystem(CodeSystem theCodeSystem) {
		Validate.notBlank(theCodeSystem.getUrl(), "theCodeSystem.getUrl() must not return a value");
		myCodeSystems.put(theCodeSystem.getUrl(), theCodeSystem);
	}

	/**
	 * Constructor
	 * 
	 * @param theStructureDefinitions
	 *           The StructureDefinitions to be returned by this module. Keys are the logical URL for the resource, and
	 *           values are the resource itself.
	 * @param theValueSets
	 *           The ValueSets to be returned by this module. Keys are the logical URL for the resource, and values are
	 *           the resource itself.
	 * @param theCodeSystems
	 *           The CodeSystems to be returned by this module. Keys are the logical URL for the resource, and values are
	 *           the resource itself.
	 */
	public PrePopulatedValidationSupport(Map<String, StructureDefinition> theStructureDefinitions, Map<String, ValueSet> theValueSets, Map<String, CodeSystem> theCodeSystems) {
		myStructureDefinitions = theStructureDefinitions;
		myValueSets = theValueSets;
		myCodeSystems = theCodeSystems;
	}

	@Override
	public ValueSetExpansionComponent expandValueSet(FhirContext theContext, ConceptSetComponent theInclude) {
		return null;
	}

	@Override
	public List<StructureDefinition> fetchAllStructureDefinitions(FhirContext theContext) {
		return new ArrayList<StructureDefinition>(myStructureDefinitions.values());
	}

	@Override
	public CodeSystem fetchCodeSystem(FhirContext theContext, String theSystem) {
		return myCodeSystems.get(theSystem);
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