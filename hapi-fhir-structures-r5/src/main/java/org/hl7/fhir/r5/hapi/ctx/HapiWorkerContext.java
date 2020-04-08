package org.hl7.fhir.r5.hapi.ctx;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.util.CoverageIgnore;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;
import org.fhir.ucum.UcumService;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.TerminologyServiceException;
import org.hl7.fhir.r5.context.IWorkerContext;
import org.hl7.fhir.r5.formats.IParser;
import org.hl7.fhir.r5.formats.ParserType;
import org.hl7.fhir.r5.model.*;
import org.hl7.fhir.r5.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.r5.model.ElementDefinition.ElementDefinitionBindingComponent;
import org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.r5.terminologies.ValueSetExpander;
import org.hl7.fhir.r5.utils.IResourceValidator;
import org.hl7.fhir.utilities.TranslationServices;
import org.hl7.fhir.utilities.i18n.I18nBase;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueSeverity;
import org.hl7.fhir.utilities.validation.ValidationOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public final class HapiWorkerContext extends I18nBase implements IWorkerContext {
	private final FhirContext myCtx;
	private final Cache<String, Resource> myFetchedResourceCache;
	private IValidationSupport myValidationSupport;
	private Parameters myExpansionProfile;
	private String myOverrideVersionNs;

	public HapiWorkerContext(FhirContext theCtx, IValidationSupport theValidationSupport) {
		Validate.notNull(theCtx, "theCtx must not be null");
		Validate.notNull(theValidationSupport, "theValidationSupport must not be null");
		myCtx = theCtx;
		myValidationSupport = theValidationSupport;

		long timeoutMillis = 10 * DateUtils.MILLIS_PER_SECOND;
		if (System.getProperties().containsKey(Constants.TEST_SYSTEM_PROP_VALIDATION_RESOURCE_CACHES_MS)) {
			timeoutMillis = Long.parseLong(System.getProperty(Constants.TEST_SYSTEM_PROP_VALIDATION_RESOURCE_CACHES_MS));
		}

		myFetchedResourceCache = Caffeine.newBuilder().expireAfterWrite(timeoutMillis, TimeUnit.MILLISECONDS).build();

		// Set a default locale
		setValidationMessageLanguage(getLocale());
	}

	@Override
	public List<StructureDefinition> allStructures() {
		return myValidationSupport.fetchAllStructureDefinitions();
	}

	@Override
	public List<StructureDefinition> getStructures() {
		return allStructures();
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
	public List<ConceptMap> findMapsForSource(String theUrl) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getAbbreviation(String theName) {
		throw new UnsupportedOperationException();
	}

	@Override
	public org.hl7.fhir.r5.utils.INarrativeGenerator getNarrativeGenerator(String thePrefix, String theBasePath) {
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
		List<String> result = new ArrayList<>();
		for (ResourceType next : ResourceType.values()) {
			result.add(next.name());
		}
		Collections.sort(result);
		return result;
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
			return myValidationSupport.isCodeSystemSupported(myValidationSupport, theSystem);
		}
	}


	@Override
	public ValidationResult validateCode(ValidationOptions theOptions, CodeableConcept theCode, ValueSet theVs) {
		for (Coding next : theCode.getCoding()) {
			ValidationResult retVal = validateCode(theOptions, next, theVs);
			if (retVal.isOk()) {
				return retVal;
			}
		}

		return new ValidationResult(IssueSeverity.ERROR, null);
	}

	@Override
	public ValidationResult validateCode(ValidationOptions theOptions, Coding theCode, ValueSet theVs) {
		String system = theCode.getSystem();
		String code = theCode.getCode();
		String display = theCode.getDisplay();
		return validateCode(theOptions, system, code, display, theVs);
	}

	@Override
	public ValidationResult validateCode(ValidationOptions theOptions, String theSystem, String theCode, String theDisplay) {
		IValidationSupport.CodeValidationResult result = myValidationSupport.validateCode(myValidationSupport, convertConceptValidationOptions(theOptions), theSystem, theCode, theDisplay, null);
		if (result == null) {
			return null;
		}
		IssueSeverity severity = null;
		if (result.getSeverity() != null) {
			severity = IssueSeverity.fromCode(result.getSeverityCode());
		}
		ConceptDefinitionComponent definition = new ConceptDefinitionComponent().setCode(result.getCode());
		return new ValidationResult(severity, result.getMessage(), definition);
	}

	@Override
	public ValidationResult validateCode(ValidationOptions theOptions, String theSystem, String theCode, String theDisplay, ValueSet theVs) {

		IValidationSupport.CodeValidationResult outcome;
		if (isNotBlank(theVs.getUrl())) {
			outcome = myValidationSupport.validateCode(myValidationSupport, convertConceptValidationOptions(theOptions), theSystem, theCode, theDisplay, theVs.getUrl());
		} else {
			outcome = myValidationSupport.validateCodeInValueSet(myValidationSupport, convertConceptValidationOptions(theOptions), theSystem, theCode, theDisplay, theVs);
		}

		if (outcome != null && outcome.isOk()) {
			ConceptDefinitionComponent definition = new ConceptDefinitionComponent();
			definition.setCode(theCode);
			definition.setDisplay(outcome.getDisplay());
			return new ValidationResult(definition);
		}

		return new ValidationResult(IssueSeverity.ERROR, "Unknown code[" + theCode + "] in system[" + Constants.codeSystemWithDefaultDescription(theSystem) + "]");
	}

	@Override
	public ValidationResult validateCode(ValidationOptions theOptions, String code, ValueSet vs) {
		return validateCode(theOptions, null, code, null, vs);
	}

	@Override
	@CoverageIgnore
	public List<CanonicalResource> allConformanceResources() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void generateSnapshot(StructureDefinition p) throws FHIRException {
		myValidationSupport.generateSnapshot(myValidationSupport, p, "", "", "");
	}

	@Override
	public void generateSnapshot(StructureDefinition mr, boolean ifLogical) {

	}

	@Override
	public Parameters getExpansionParameters() {
		return myExpansionProfile;
	}

	@Override
	public void setExpansionProfile(Parameters theExpParameters) {
		myExpansionProfile = theExpParameters;
	}

	@Override
	@CoverageIgnore
	public boolean hasCache() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ValueSetExpander.ValueSetExpansionOutcome expandVS(ValueSet theSource, boolean theCacheOk, boolean theHierarchical) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ValueSetExpander.ValueSetExpansionOutcome expandVS(ConceptSetComponent theInc, boolean theHierarchical) throws TerminologyServiceException {
		ValueSet input = new ValueSet();
		input.getCompose().addInclude(theInc);
		IValidationSupport.ValueSetExpansionOutcome output = myValidationSupport.expandValueSet(myValidationSupport, null, input);
		return new ValueSetExpander.ValueSetExpansionOutcome((ValueSet) output.getValueSet(), output.getError(), null);
	}

	@Override
	public Locale getLocale() {
		return Locale.getDefault();
	}

	@Override
	public void setLocale(Locale locale) {
		// ignore
	}

	@Override
	public ILoggingService getLogger() {
		throw new UnsupportedOperationException();
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
	public UcumService getUcumService() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setUcumService(UcumService ucumService) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isNoTerminologyServer() {
		return false;
	}

	@Override
	public TranslationServices translator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<StructureMap> listTransforms() {
		throw new UnsupportedOperationException();
	}

	@Override
	public StructureMap getTransform(String url) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getOverrideVersionNs() {
		return myOverrideVersionNs;
	}

	@Override
	public void setOverrideVersionNs(String value) {
		myOverrideVersionNs = value;
	}

	@Override
	public StructureDefinition fetchTypeDefinition(String typeName) {
		return fetchResource(StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/" + typeName);
	}

	@Override
	public StructureDefinition fetchRawProfile(String url) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<String> getTypeNames() {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T extends org.hl7.fhir.r5.model.Resource> T fetchResource(Class<T> theClass, String theUri) {
		if (myValidationSupport == null) {
			return null;
		} else {
			@SuppressWarnings("unchecked")
			T retVal = (T) myFetchedResourceCache.get(theUri, t -> myValidationSupport.fetchResource(theClass, theUri));
			return retVal;
		}
	}

	@Override
	public <T extends org.hl7.fhir.r5.model.Resource> T fetchResourceWithException(Class<T> theClass, String theUri) throws FHIRException {
		T retVal = fetchResource(theClass, theUri);
		if (retVal == null) {
			throw new FHIRException("Could not find resource: " + theUri);
		}
		return retVal;
	}

	@Override
	public org.hl7.fhir.r5.model.Resource fetchResourceById(String theType, String theUri) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T extends org.hl7.fhir.r5.model.Resource> boolean hasResource(Class<T> theClass_, String theUri) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void cacheResource(org.hl7.fhir.r5.model.Resource theRes) throws FHIRException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<String> getResourceNamesAsSet() {
		return myCtx.getResourceNames();
	}

	@Override
	public ValueSetExpander.ValueSetExpansionOutcome expandVS(ElementDefinitionBindingComponent theBinding, boolean theCacheOk, boolean theHierarchical) throws FHIRException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getLinkForUrl(String corePath, String url) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Map<String, byte[]> getBinaries() {
		return null;
	}

	public static ConceptValidationOptions convertConceptValidationOptions(ValidationOptions theOptions) {
		ConceptValidationOptions retVal = new ConceptValidationOptions();
		if (theOptions.isGuessSystem()) {
			retVal = retVal.setInferSystem(true);
		}
		return retVal;
	}

}
