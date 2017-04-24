package org.hl7.fhir.dstu3.hapi.validation;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.Validate;
import org.hl7.fhir.dstu3.context.IWorkerContext;
import org.hl7.fhir.dstu3.formats.IParser;
import org.hl7.fhir.dstu3.formats.ParserType;
import org.hl7.fhir.dstu3.hapi.validation.IValidationSupport.CodeValidationResult;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.ConceptMap;
import org.hl7.fhir.dstu3.model.ExpansionProfile;
import org.hl7.fhir.dstu3.model.MetadataResource;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.ResourceType;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.dstu3.model.ValueSet.ConceptReferenceComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionContainsComponent;
import org.hl7.fhir.dstu3.terminologies.ValueSetExpander;
import org.hl7.fhir.dstu3.terminologies.ValueSetExpanderFactory;
import org.hl7.fhir.dstu3.terminologies.ValueSetExpanderSimple;
import org.hl7.fhir.dstu3.utils.INarrativeGenerator;
import org.hl7.fhir.dstu3.utils.IResourceValidator;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.TerminologyServiceException;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueSeverity;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.CoverageIgnore;

public final class HapiWorkerContext implements IWorkerContext, ValueSetExpander, ValueSetExpanderFactory {
	private final FhirContext myCtx;
	private Map<String, Resource> myFetchedResourceCache = new HashMap<String, Resource>();
	private IValidationSupport myValidationSupport;
	private ExpansionProfile myExpansionProfile;

	public HapiWorkerContext(FhirContext theCtx, IValidationSupport theValidationSupport) {
		Validate.notNull(theCtx, "theCtx must not be null");
		Validate.notNull(theValidationSupport, "theValidationSupport must not be null");
		myCtx = theCtx;
		myValidationSupport = theValidationSupport;
	}

	@Override
	public List<StructureDefinition> allStructures() {
		return myValidationSupport.fetchAllStructureDefinitions(myCtx);
	}

	@Override
	public CodeSystem fetchCodeSystem(String theSystem) {
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
			@SuppressWarnings("unchecked")
			T retVal = (T) myFetchedResourceCache.get(theUri);
			if (retVal == null) {
				retVal = myValidationSupport.fetchResource(myCtx, theClass, theUri);
				if (retVal != null) {
					myFetchedResourceCache.put(theUri, retVal);
				}
			}
			return retVal;
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
		ValueSetExpanderSimple retVal = new ValueSetExpanderSimple(this, this);
		retVal.setMaxExpansionSize(Integer.MAX_VALUE);
		return retVal;
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
		List<String> result = new ArrayList<String>();
		for (ResourceType next : ResourceType.values()) {
			result.add(next.name());
		}
		Collections.sort(result);
		return result;
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
	public String oid2Uri(String theCode) {
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
	public Set<String> typeTails() {
		return new HashSet<String>(Arrays.asList("Integer", "UnsignedInt", "PositiveInt", "Decimal", "DateTime", "Date", "Time", "Instant", "String", "Uri", "Oid", "Uuid", "Id", "Boolean", "Code",
				"Markdown", "Base64Binary", "Coding", "CodeableConcept", "Attachment", "Identifier", "Quantity", "SampledData", "Range", "Period", "Ratio", "HumanName", "Address", "ContactPoint",
				"Timing", "Reference", "Annotation", "Signature", "Meta"));
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
				return new ValidationResult(IssueSeverity.INFORMATION, "Code " + theSystem + "/" + theCode + " was not validated because the code system is not present");
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
		if (theVs != null && "http://hl7.org/fhir/ValueSet/languages".equals(theVs.getId())) {
			ValueSet expansion = new ValueSet();
			for (ConceptSetComponent nextInclude : theVs.getCompose().getInclude()) {
				for (ConceptReferenceComponent nextConcept : nextInclude.getConcept()) {
					expansion.getExpansion().addContains().setCode(nextConcept.getCode()).setDisplay(nextConcept.getDisplay());
				}
			}
			expandedValueSet = new ValueSetExpansionOutcome(expansion);
		}

		if (expandedValueSet == null) {
			expandedValueSet = expand(theVs, null);
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
					ValidationResult retVal = new ValidationResult(definition);
					return retVal;
				}
			}
		}

		return new ValidationResult(IssueSeverity.ERROR, "Unknown code[" + theCode + "] in system[" + theSystem + "]");
	}

	@Override
	@CoverageIgnore
	public List<MetadataResource> allConformanceResources() {
		throw new UnsupportedOperationException();
	}

	@Override
	@CoverageIgnore
	public boolean hasCache() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ValueSetExpansionOutcome expand(ValueSet theSource, ExpansionProfile theProfile) {
		ValueSetExpansionOutcome vso;
		try {
			vso = getExpander().expand(theSource, theProfile);
		} catch (InvalidRequestException e) {
			throw e;
		} catch (Exception e) {
			throw new InternalErrorException(e);
		}
		if (vso.getError() != null) {
			throw new InvalidRequestException(vso.getError());
		} else {
			return vso;
		}
	}

	@Override
	public ExpansionProfile getExpansionProfile() {
		return myExpansionProfile;
	}

	@Override
	public void setExpansionProfile(ExpansionProfile theExpProfile) {
		myExpansionProfile = theExpProfile;
	}

	@Override
	public ValueSetExpansionOutcome expandVS(ValueSet theSource, boolean theCacheOk, boolean theHeiarchical) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ValueSetExpansionComponent expandVS(ConceptSetComponent theInc, boolean theHeiarchical) throws TerminologyServiceException {
		return myValidationSupport.expandValueSet(myCtx, theInc);
	}

	@Override
	public void setLogger(ILoggingService theLogger) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getVersion() {
		return myCtx.getVersion().getVersion().getFhirVersionString();
	}

	@Override
	public boolean isNoTerminologyServer() {
		return false;
	}

	@Override
	public <T extends Resource> T fetchResourceWithException(Class<T> theClass_, String theUri) throws FHIRException {
		T retVal = fetchResource(theClass_, theUri);
		if (retVal == null) {
			throw new FHIRException("Unable to fetch " + theUri);
		}
		return retVal;
	}

  @Override
  public List<String> getTypeNames() {
    throw new UnsupportedOperationException();
  }

}