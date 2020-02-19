package org.hl7.fhir.common.hapi.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IContextValidationSupport;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.util.VersionIndependentConcept;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.hapi.ctx.DefaultProfileValidationSupport;
import org.hl7.fhir.r4.hapi.ctx.IValidationSupport;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.r4.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.r4.terminologies.ValueSetExpander;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This class is an implementation of {@link IValidationSupport} which may be pre-populated
 * with a collection of validation resources to be used by the validator.
 */
public class PrePopulatedValidationSupport implements IContextValidationSupport {

	private Map<String, IBaseResource> myCodeSystems;
	private Map<String, IBaseResource> myStructureDefinitions;
	private Map<String, IBaseResource> myValueSets;
	private DefaultProfileValidationSupport myDefaultProfileValidationSupport = new DefaultProfileValidationSupport();

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
	public ValueSetExpander.ValueSetExpansionOutcome expandValueSet(FhirContext theContext, ConceptSetComponent theInclude) {
		ValueSetExpander.ValueSetExpansionOutcome retVal = new ValueSetExpander.ValueSetExpansionOutcome(new ValueSet());

		Set<String> wantCodes = new HashSet<>();
		for (ValueSet.ConceptReferenceComponent next : theInclude.getConcept()) {
			wantCodes.add(next.getCode());
		}

		CodeSystem system = fetchCodeSystem(theContext, theInclude.getSystem());
		if (system != null) {
			List<CodeSystem.ConceptDefinitionComponent> concepts = system.getConcept();
			addConcepts(theInclude, retVal.getValueset().getExpansion(), wantCodes, concepts);
		}

		for (UriType next : theInclude.getValueSet()) {
			ValueSet vs = myValueSets.get(defaultString(next.getValueAsString()));
			if (vs != null) {
				for (ConceptSetComponent nextInclude : vs.getCompose().getInclude()) {
					ValueSetExpander.ValueSetExpansionOutcome contents = expandValueSet(theContext, nextInclude);
					retVal.getValueset().getExpansion().getContains().addAll(contents.getValueset().getExpansion().getContains());
				}
			}
		}

		return retVal;
	}

	private void addConcepts(ConceptSetComponent theInclude, ValueSet.ValueSetExpansionComponent theRetVal, Set<String> theWantCodes, List<CodeSystem.ConceptDefinitionComponent> theConcepts) {
		for (CodeSystem.ConceptDefinitionComponent next : theConcepts) {
			if (theWantCodes.isEmpty() || theWantCodes.contains(next.getCode())) {
				theRetVal
					.addContains()
					.setSystem(theInclude.getSystem())
					.setCode(next.getCode())
					.setDisplay(next.getDisplay());
			}
			addConcepts(theInclude, theRetVal, theWantCodes, next.getConcept());
		}
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
		return new ArrayList<>(myStructureDefinitions.values());
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

		List<VersionIndependentConcept> codes;

		switch (theContext.getVersion().getVersion()) {
			case DSTU3: {
				org.hl7.fhir.dstu3.terminologies.ValueSetExpanderSimple expander = new org.hl7.fhir.dstu3.terminologies.ValueSetExpanderSimple(new org.hl7.fhir.dstu3.hapi.ctx.HapiWorkerContext(theContext, theRootValidationSupport), null);
				ValueSetExpander.ValueSetExpansionOutcome expansion = expander.expand(vs, new Parameters());
				List<ValueSet.ValueSetExpansionContainsComponent> contains = expansion.getValueset().getExpansion().getContains();
				codes = contains.stream().map(t -> new VersionIndependentConcept(t.getSystem(), t.getCode())).collect(Collectors.toList());
				break;
			}
			case R4: {
				org.hl7.fhir.r4.terminologies.ValueSetExpanderSimple expander = new org.hl7.fhir.r4.terminologies.ValueSetExpanderSimple(new org.hl7.fhir.r4.hapi.ctx.HapiWorkerContext(theContext, theRootValidationSupport));
				ValueSetExpander.ValueSetExpansionOutcome expansion = expander.expand(vs, new Parameters());
				List<ValueSet.ValueSetExpansionContainsComponent> contains = expansion.getValueset().getExpansion().getContains();
				codes = contains.stream().map(t -> new VersionIndependentConcept(t.getSystem(), t.getCode())).collect(Collectors.toList());
				break;
			}
			case R5: {
				org.hl7.fhir.r5.terminologies.ValueSetExpanderSimple expander = new org.hl7.fhir.r5.terminologies.ValueSetExpanderSimple(new org.hl7.fhir.r5.hapi.ctx.HapiWorkerContext(theContext, theRootValidationSupport));
				ValueSetExpander.ValueSetExpansionOutcome expansion = expander.expand(vs, new Parameters());
				List<ValueSet.ValueSetExpansionContainsComponent> contains = expansion.getValueset().getExpansion().getContains();
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
