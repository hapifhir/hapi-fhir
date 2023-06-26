package org.hl7.fhir.dstu2016may.hapi.validation;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.rest.api.Constants;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.dstu2016may.formats.IParser;
import org.hl7.fhir.dstu2016may.formats.ParserType;
import org.hl7.fhir.dstu2016may.model.CodeSystem;
import org.hl7.fhir.dstu2016may.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.dstu2016may.model.CodeType;
import org.hl7.fhir.dstu2016may.model.CodeableConcept;
import org.hl7.fhir.dstu2016may.model.Coding;
import org.hl7.fhir.dstu2016may.model.ConceptMap;
import org.hl7.fhir.dstu2016may.model.OperationOutcome;
import org.hl7.fhir.dstu2016may.model.Resource;
import org.hl7.fhir.dstu2016may.model.ResourceType;
import org.hl7.fhir.dstu2016may.model.StructureDefinition;
import org.hl7.fhir.dstu2016may.model.ValueSet;
import org.hl7.fhir.dstu2016may.model.ValueSet.ConceptReferenceComponent;
import org.hl7.fhir.dstu2016may.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.dstu2016may.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.dstu2016may.model.ValueSet.ValueSetExpansionContainsComponent;
import org.hl7.fhir.dstu2016may.terminologies.ValueSetExpander.ValueSetExpansionOutcome;
import org.hl7.fhir.dstu2016may.utils.INarrativeGenerator;
import org.hl7.fhir.dstu2016may.utils.IWorkerContext;
import org.hl7.fhir.utilities.i18n.I18nBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public final class HapiWorkerContext extends I18nBase implements IWorkerContext {
	private final FhirContext myCtx;
	private Map<String, Resource> myFetchedResourceCache = new HashMap<>();
	private IValidationSupport myValidationSupport;

	public HapiWorkerContext(FhirContext theCtx, IValidationSupport theValidationSupport) {
		Validate.notNull(theCtx, "theCtx must not be null");
		Validate.notNull(theValidationSupport, "theValidationSupport must not be null");
		myCtx = theCtx;
		myValidationSupport = theValidationSupport;

		setValidationMessageLanguage(getLocale());
	}

	@Override
	public List<StructureDefinition> allStructures() {
		return myValidationSupport.fetchAllStructureDefinitions();
	}

	@Override
	public CodeSystem fetchCodeSystem(String theSystem) {
		if (myValidationSupport == null) {
			return null;
		} else {
			return (CodeSystem) myValidationSupport.fetchCodeSystem(theSystem);
		}
	}

	@Override
	public <T extends Resource> T fetchResource(Class<T> theClass, String theUri) {
		if (myValidationSupport == null) {
			return null;
		} else {
			@SuppressWarnings("unchecked")
			T retVal = (T) myFetchedResourceCache.get(theUri);
			if (retVal == null) {
				retVal = myValidationSupport.fetchResource(theClass, theUri);
				if (retVal != null) {
					myFetchedResourceCache.put(theUri, retVal);
				}
			}
			return retVal;
		}
	}

	@Override
	public List<ConceptMap> findMapsForSource(String theUrl) {
		throw new UnsupportedOperationException(Msg.code(470));
	}

	@Override
	public String getAbbreviation(String theName) {
		throw new UnsupportedOperationException(Msg.code(471));
	}


	@Override
	public IParser getParser(ParserType theType) {
		throw new UnsupportedOperationException(Msg.code(472));
	}

	@Override
	public IParser getParser(String theType) {
		throw new UnsupportedOperationException(Msg.code(473));
	}

	@Override
	public List<String> getResourceNames() {
		List<String> result = new ArrayList<>();
		for (ResourceType next : ResourceType.values()) {
			result.add(next.name());
		}
		Collections.sort(result);
		return result;
	}

	@Override
	public <T extends Resource> boolean hasResource(Class<T> theClass_, String theUri) {
		throw new UnsupportedOperationException(Msg.code(474));
	}

	@Override
	public IParser newJsonParser() {
		throw new UnsupportedOperationException(Msg.code(475));
	}

	@Override
	public IParser newXmlParser() {
		throw new UnsupportedOperationException(Msg.code(476));
	}

	@Override
	public INarrativeGenerator getNarrativeGenerator(String theS, String theS1) {
		throw new UnsupportedOperationException(Msg.code(477));
	}

	@Override
	public String oid2Uri(String theCode) {
		throw new UnsupportedOperationException(Msg.code(478));
	}

	@Override
	public StructureDefinition fetchTypeDefinition(String typeName) {
		return fetchResource(org.hl7.fhir.dstu2016may.model.StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/" + typeName);
	}

	@Override
	public boolean supportsSystem(String theSystem) {
		if (myValidationSupport == null) {
			return false;
		} else {
			return myValidationSupport.isCodeSystemSupported(new ValidationSupportContext(myValidationSupport), theSystem);
		}
	}

	@Override
	public Set<String> typeTails() {
		return new HashSet<>(Arrays.asList("Integer", "UnsignedInt", "PositiveInt", "Decimal", "DateTime", "Date", "Time", "Instant", "String", "Uri", "Oid", "Uuid", "Id", "Boolean", "Code",
			"Markdown", "Base64Binary", "Coding", "CodeableConcept", "Attachment", "Identifier", "Quantity", "SampledData", "Range", "Period", "Ratio", "HumanName", "Address", "ContactPoint",
			"Timing", "Reference", "Annotation", "Signature", "Meta"));
	}

	@Override
	public ValidationResult validateCode(CodeableConcept theCode, ValueSet theVs) {
		for (Coding next : theCode.getCoding()) {
			ValidationResult retVal = validateCode(next, theVs);
			if (retVal.isOk()) {
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
		IValidationSupport.CodeValidationResult result = myValidationSupport.validateCode(new ValidationSupportContext(myValidationSupport), new ConceptValidationOptions(), theSystem, theCode, theDisplay, null);
		if (result == null) {
			return null;
		}
		OperationOutcome.IssueSeverity severity = null;
		if (result.getSeverity() != null) {
			severity = OperationOutcome.IssueSeverity.fromCode(result.getSeverityCode());
		}
		ConceptDefinitionComponent definition = result.getCode() != null ? new ConceptDefinitionComponent().setCode(result.getCode()) : null;
		return new ValidationResult(severity, result.getMessage(), definition);
	}

	@Override
	public ValidationResult validateCode(String theSystem, String theCode, String theDisplay, ConceptSetComponent theVsi) {
		throw new UnsupportedOperationException(Msg.code(479));
	}

	@Override
	public ValidationResult validateCode(String theSystem, String theCode, String theDisplay, ValueSet theVs) {

		if (theVs != null && isNotBlank(theCode)) {
			for (ConceptSetComponent next : theVs.getCompose().getInclude()) {
				if (isBlank(theSystem) || theSystem.equals(next.getSystem())) {
					for (ConceptReferenceComponent nextCode : next.getConcept()) {
						if (theCode.equals(nextCode.getCode())) {
							CodeType code = new CodeType(theCode);
							return new ValidationResult(new ConceptDefinitionComponent(code));
						}
					}
				}
			}
		}


		boolean caseSensitive = true;
		if (isNotBlank(theSystem)) {
			CodeSystem system = fetchCodeSystem(theSystem);
			if (system == null) {
				return new ValidationResult(OperationOutcome.IssueSeverity.INFORMATION, "Code " + Constants.codeSystemWithDefaultDescription(theSystem) + "/" + theCode + " was not validated because the code system is not present");
			}

			if (system.hasCaseSensitive()) {
				caseSensitive = system.getCaseSensitive();
			}
		}

		String wantCode = theCode;
		if (!caseSensitive) {
			wantCode = wantCode.toUpperCase();
		}

		ValueSetExpansionOutcome expandedValueSet = null;

		/*
		 * The following valueset is a special case, since the BCP codesystem is very difficult to expand
		 */
		if (theVs != null && "http://hl7.org/fhir/ValueSet/languages".equals(theVs.getUrl())) {
			ValueSet expansion = new ValueSet();
			for (ConceptSetComponent nextInclude : theVs.getCompose().getInclude()) {
				for (ConceptReferenceComponent nextConcept : nextInclude.getConcept()) {
					expansion.getExpansion().addContains().setCode(nextConcept.getCode()).setDisplay(nextConcept.getDisplay());
				}
			}
			expandedValueSet = new ValueSetExpansionOutcome(expansion);
		}

		if (expandedValueSet == null) {
			expandedValueSet = expandVS(theVs, true);
		}

		for (ValueSetExpansionContainsComponent next : expandedValueSet.getValueset().getExpansion().getContains()) {
			String nextCode = next.getCode();
			if (!caseSensitive) {
				nextCode = nextCode.toUpperCase();
			}

			if (nextCode.equals(wantCode)) {
				if (theSystem == null || next.getSystem().equals(theSystem)) {
					ConceptDefinitionComponent definition = new ConceptDefinitionComponent();
					definition.setCode(next.getCode());
					definition.setDisplay(next.getDisplay());
					return new ValidationResult(definition);
				}
			}
		}

		return new ValidationResult(OperationOutcome.IssueSeverity.ERROR, "Unknown code[" + theCode + "] in system[" + Constants.codeSystemWithDefaultDescription(theSystem) + "]");
	}

	@Override
	public ValueSetExpansionOutcome expandVS(ValueSet theSource, boolean theCacheOk) {
		throw new UnsupportedOperationException(Msg.code(480));
	}

	@Override
	public ValueSetExpansionComponent expandVS(ConceptSetComponent theInc) {
		throw new UnsupportedOperationException(Msg.code(481));
	}


}
