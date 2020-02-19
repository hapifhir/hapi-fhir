package org.hl7.fhir.common.hapi.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IContextValidationSupport;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.util.VersionIndependentConcept;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.dstu3.model.ExpansionProfile;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.hapi.ctx.IValidationSupport;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This class is an implementation of {@link IValidationSupport} which may be pre-populated
 * with a collection of validation resources to be used by the validator.
 */
public class PrePopulatedValidationSupport implements IContextValidationSupport {

	private Map<String, IBaseResource> myCodeSystems;
	private Map<String, IBaseResource> myStructureDefinitions;
	private Map<String, IBaseResource> myValueSets;

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
	public PrePopulatedValidationSupport(Map<String, IBaseResource> theStructureDefinitions, Map<String, IBaseResource> theValueSets, Map<String, IBaseResource> theCodeSystems) {
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
	public void addCodeSystem(IBaseResource theCodeSystem) {
		String url;

		switch (theCodeSystem.getStructureFhirVersionEnum()) {
			case DSTU3:
				url = ((org.hl7.fhir.dstu3.model.CodeSystem) theCodeSystem).getUrl();
				break;
			case R4:
				url = ((org.hl7.fhir.r4.model.CodeSystem) theCodeSystem).getUrl();
				break;
			case R5:
				url = ((org.hl7.fhir.r5.model.CodeSystem) theCodeSystem).getUrl();
				break;
			case DSTU2:
			case DSTU2_HL7ORG:
			case DSTU2_1:
			default:
				throw new IllegalArgumentException("Can not add for version: " + theCodeSystem.getStructureFhirVersionEnum());
		}


		Validate.notBlank(url, "theCodeSystem.getUrl() must return a value");
		addToMap(theCodeSystem, myCodeSystems, url);
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

	private <T extends IBaseResource> void addToMap(T theStructureDefinition, Map<String, T> map, String theUrl) {
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
	public ValueSetExpansionOutcome expandValueSet(IContextValidationSupport theRootValidationSupport, FhirContext theContext, IBaseResource theValueSet) {

		IBaseResource expansion;
		switch (theContext.getVersion().getVersion()) {
			case DSTU3: {
				org.hl7.fhir.dstu3.terminologies.ValueSetExpanderSimple expander = new org.hl7.fhir.dstu3.terminologies.ValueSetExpanderSimple(new org.hl7.fhir.dstu3.hapi.ctx.HapiWorkerContext(theContext, theRootValidationSupport), null);
				expansion = expander.expand((org.hl7.fhir.dstu3.model.ValueSet)theValueSet, new ExpansionProfile()).getValueset();
				break;
			}
			case R4: {
				org.hl7.fhir.r4.terminologies.ValueSetExpanderSimple expander = new org.hl7.fhir.r4.terminologies.ValueSetExpanderSimple(new org.hl7.fhir.r4.hapi.ctx.HapiWorkerContext(theContext, theRootValidationSupport));
				expansion = expander.expand((org.hl7.fhir.r4.model.ValueSet)theValueSet, new org.hl7.fhir.r4.model.Parameters()).getValueset();
				break;
			}
			case R5: {
				org.hl7.fhir.r5.terminologies.ValueSetExpanderSimple expander = new org.hl7.fhir.r5.terminologies.ValueSetExpanderSimple(new org.hl7.fhir.r5.hapi.ctx.HapiWorkerContext(theContext, theRootValidationSupport));
				expansion = expander.expand((org.hl7.fhir.r5.model.ValueSet)theValueSet, new org.hl7.fhir.r5.model.Parameters()).getValueset();
				break;
			}
			case DSTU2:
			case DSTU2_1:
			case DSTU2_HL7ORG:
			default:
				throw new IllegalArgumentException("Can not handle version: " + theContext.getVersion().getVersion());
		}

		return new ValueSetExpansionOutcome(expansion, null);
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
	public <T extends IBaseResource> List<T> fetchAllStructureDefinitions(FhirContext theContext, Class<T> theStructureDefinitionClass) {
		return toList(myStructureDefinitions, theStructureDefinitionClass);
	}

	private <T extends IBaseResource> ArrayList<T> toList(Map<String, IBaseResource> theMap, Class<T> theType) {
		ArrayList<T> retVal = new ArrayList<>(theMap.size());
		for (IBaseResource next : theMap.values()) {
			retVal.add(theType.cast(next));
		}
		return retVal;
	}

	@Override
	public <T extends IBaseResource> T fetchCodeSystem(FhirContext theContext, String theSystem, Class<T> theCodeSystemType) {
		return (T) myCodeSystems.get(theSystem);
	}

	@Override
	public IBaseResource fetchValueSet(FhirContext theContext, String theUri) {
		return myValueSets.get(theUri);
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
	public IBaseResource fetchStructureDefinition(FhirContext theCtx, String theUrl) {
		return myStructureDefinitions.get(theUrl);
	}

	@Override
	public boolean isCodeSystemSupported(FhirContext theContext, String theSystem) {
		return myCodeSystems.containsKey(theSystem);
	}

	@Override
	public boolean isValueSetSupported(FhirContext theContext, String theValueSetUrl) {
		return myValueSets.containsKey(theValueSetUrl);
	}

	@Override
	public CodeValidationResult validateCode(IContextValidationSupport theRootValidationSupport, FhirContext theContext, String theCodeSystem, String theCode, String theDisplay, String theValueSetUrl) {
		IBaseResource vs;
		if (isNotBlank(theValueSetUrl)) {
			vs = myValueSets.get(theValueSetUrl);
		} else {
			switch (theContext.getVersion().getVersion()) {
				case DSTU3:
					vs = new org.hl7.fhir.dstu3.model.ValueSet()
						.setCompose(new org.hl7.fhir.dstu3.model.ValueSet.ValueSetComposeComponent()
							.addInclude(new org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent().setSystem(theCodeSystem)));
					break;
				case R4:
					vs = new org.hl7.fhir.r4.model.ValueSet()
						.setCompose(new org.hl7.fhir.r4.model.ValueSet.ValueSetComposeComponent()
							.addInclude(new org.hl7.fhir.r4.model.ValueSet.ConceptSetComponent().setSystem(theCodeSystem)));
					break;
				case R5:
					vs = new org.hl7.fhir.r5.model.ValueSet()
						.setCompose(new org.hl7.fhir.r5.model.ValueSet.ValueSetComposeComponent()
							.addInclude(new org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent().setSystem(theCodeSystem)));
					break;
				case DSTU2:
				case DSTU2_1:
				case DSTU2_HL7ORG:
				default:
					throw new IllegalArgumentException("Can not handle version: " + theContext.getVersion().getVersion());
			}
		}

		if (vs == null) {
			return null;
		}


		IBaseResource expansion = expandValueSet(theRootValidationSupport, theContext, vs).getValueSet();

		List<VersionIndependentConcept> codes;
		switch (theContext.getVersion().getVersion()) {
			case DSTU3: {
				org.hl7.fhir.dstu3.model.ValueSet expansionVs = (org.hl7.fhir.dstu3.model.ValueSet)expansion;
				List<org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionContainsComponent> contains = expansionVs.getExpansion().getContains();
				codes = contains.stream().map(t -> new VersionIndependentConcept(t.getSystem(), t.getCode())).collect(Collectors.toList());
				break;
			}
			case R4: {
				org.hl7.fhir.r4.model.ValueSet expansionVs = (org.hl7.fhir.r4.model.ValueSet)expansion;
				List<ValueSet.ValueSetExpansionContainsComponent> contains = expansionVs.getExpansion().getContains();
				codes = contains.stream().map(t -> new VersionIndependentConcept(t.getSystem(), t.getCode())).collect(Collectors.toList());
				break;
			}
			case R5: {
				org.hl7.fhir.r5.model.ValueSet expansionVs = (org.hl7.fhir.r5.model.ValueSet)expansion;
				List<org.hl7.fhir.r5.model.ValueSet.ValueSetExpansionContainsComponent> contains = expansionVs.getExpansion().getContains();
				codes = contains.stream().map(t -> new VersionIndependentConcept(t.getSystem(), t.getCode())).collect(Collectors.toList());
				break;
			}
			case DSTU2:
			case DSTU2_1:
			case DSTU2_HL7ORG:
			default:
				throw new IllegalArgumentException("Can not handle version: " + theContext.getVersion().getVersion());
		}

		for (VersionIndependentConcept nextExpansionCode : codes) {

			if (theCode.equals(nextExpansionCode.getCode())) {
				if (Constants.codeSystemNotNeeded(theCodeSystem) || nextExpansionCode.getSystem().equals(theCodeSystem)) {
					return new CodeValidationResult(new CodeSystem.ConceptDefinitionComponent(new CodeType(theCode)));
				}
			}
		}

		return null;
	}

	@Override
	public LookupCodeResult lookupCode(FhirContext theContext, String theSystem, String theCode) {
		return null;
	}

}
