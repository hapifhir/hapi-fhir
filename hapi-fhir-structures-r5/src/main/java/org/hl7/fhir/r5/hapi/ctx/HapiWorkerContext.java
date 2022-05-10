package org.hl7.fhir.r5.hapi.ctx;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
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
import org.hl7.fhir.r5.model.CanonicalResource;
import org.hl7.fhir.r5.model.CodeSystem;
import org.hl7.fhir.r5.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.r5.model.CodeableConcept;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.ConceptMap;
import org.hl7.fhir.r5.model.ElementDefinition.ElementDefinitionBindingComponent;
import org.hl7.fhir.r5.model.Parameters;
import org.hl7.fhir.r5.model.Resource;
import org.hl7.fhir.r5.model.ResourceType;
import org.hl7.fhir.r5.model.StructureDefinition;
import org.hl7.fhir.r5.model.StructureMap;
import org.hl7.fhir.r5.model.ValueSet;
import org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.r5.terminologies.ValueSetExpander;
import org.hl7.fhir.r5.utils.validation.IResourceValidator;
import org.hl7.fhir.r5.utils.validation.ValidationContextCarrier;
import org.hl7.fhir.utilities.TimeTracker;
import org.hl7.fhir.utilities.TranslationServices;
import org.hl7.fhir.utilities.i18n.I18nBase;
import org.hl7.fhir.utilities.npm.BasePackageCacheManager;
import org.hl7.fhir.utilities.npm.NpmPackage;
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
	private final IValidationSupport myValidationSupport;
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
	public CodeSystem fetchCodeSystem(String theSystem, String version) {
		if (myValidationSupport == null) {
			return null;
		} else {
			return (CodeSystem) myValidationSupport.fetchCodeSystem(theSystem);
		}
	}

	@Override
	public List<ConceptMap> findMapsForSource(String theUrl) {
		throw new UnsupportedOperationException(Msg.code(201));
	}

	@Override
	public String getAbbreviation(String theName) {
		throw new UnsupportedOperationException(Msg.code(202));
	}

	@Override
	public IParser getParser(ParserType theType) {
		throw new UnsupportedOperationException(Msg.code(203));
	}

	@Override
	public IParser getParser(String theType) {
		throw new UnsupportedOperationException(Msg.code(204));
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
		throw new UnsupportedOperationException(Msg.code(205));
	}

	@Override
	public IResourceValidator newValidator() {
		throw new UnsupportedOperationException(Msg.code(206));
	}

	@Override
	public IParser newXmlParser() {
		throw new UnsupportedOperationException(Msg.code(207));
	}

	@Override
	public String oid2Uri(String theCode) {
		throw new UnsupportedOperationException(Msg.code(208));
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
		return validateCode(theOptions, system, null, code, display, theVs);
	}

	@Override
	public ValidationResult validateCode(ValidationOptions options, Coding code, ValueSet vs, ValidationContextCarrier ctxt) {
		return validateCode(options, code, vs);
	}

	@Override
	public void validateCodeBatch(ValidationOptions options, List<? extends CodingValidationRequest> codes, ValueSet vs) {
		throw new UnsupportedOperationException(Msg.code(209));
	}

	@Override
	public ValueSetExpander.ValueSetExpansionOutcome expandVS(ValueSet theValueSet, boolean cacheOk, boolean heiarchical, boolean incompleteOk) {
		return null;
	}

	@Override
	public ValidationResult validateCode(ValidationOptions theOptions, String theSystem, String theVersion,
													 String theCode, String theDisplay) {
		IValidationSupport.CodeValidationResult result = myValidationSupport.validateCode(new ValidationSupportContext(myValidationSupport),
			convertConceptValidationOptions(theOptions), theSystem, theCode, theDisplay, null);
		if (result == null) {
			return null;
		}
		IssueSeverity severity = null;
		if (result.getSeverity() != null) {
			severity = IssueSeverity.fromCode(result.getSeverityCode());
		}
		ConceptDefinitionComponent definition = new ConceptDefinitionComponent().setCode(result.getCode());
		return new ValidationResult(severity, result.getMessage(), theSystem, definition);
	}

	@Override
	public ValidationResult validateCode(ValidationOptions theOptions, String theSystem, String theVersion,
													 String theCode, String theDisplay, ValueSet theVs) {
		IValidationSupport.CodeValidationResult outcome;
		if (isNotBlank(theVs.getUrl())) {
			outcome = myValidationSupport.validateCode(new ValidationSupportContext(myValidationSupport),
				convertConceptValidationOptions(theOptions), theSystem, theCode, theDisplay, theVs.getUrl());
		} else {
			outcome = myValidationSupport.validateCodeInValueSet(new ValidationSupportContext(myValidationSupport),
				convertConceptValidationOptions(theOptions), theSystem, theCode, theDisplay, theVs);
		}

		if (outcome != null && outcome.isOk()) {
			ConceptDefinitionComponent definition = new ConceptDefinitionComponent();
			definition.setCode(theCode);
			definition.setDisplay(outcome.getDisplay());
			return new ValidationResult(theSystem, definition);
		}

		return new ValidationResult(IssueSeverity.ERROR, "Unknown code[" + theCode + "] in system[" +
			Constants.codeSystemWithDefaultDescription(theSystem) + "]");
	}

	@Override
	public ValidationResult validateCode(ValidationOptions theOptions, String code, ValueSet vs) {
		return validateCode(theOptions, null, null, code, null, vs);
	}

	@Override
	@CoverageIgnore
	public List<CanonicalResource> allConformanceResources() {
		throw new UnsupportedOperationException(Msg.code(210));
	}

	@Override
	public void generateSnapshot(StructureDefinition p) throws FHIRException {
		myValidationSupport.generateSnapshot(new ValidationSupportContext(myValidationSupport), p, "", "", "");
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
		throw new UnsupportedOperationException(Msg.code(211));
	}

	@Override
	public ValueSetExpander.ValueSetExpansionOutcome expandVS(ValueSet theSource, boolean theCacheOk, boolean theHierarchical) {
		throw new UnsupportedOperationException(Msg.code(212));
	}

	@Override
	public ValueSetExpander.ValueSetExpansionOutcome expandVS(ConceptSetComponent theInc, boolean theHierarchical, boolean theNoInactive) throws TerminologyServiceException {
		ValueSet input = new ValueSet();
		input.getCompose().setInactive(!theNoInactive); //TODO GGG/DO is this valid?
		input.getCompose().addInclude(theInc);
		IValidationSupport.ValueSetExpansionOutcome output = myValidationSupport.expandValueSet(new ValidationSupportContext(myValidationSupport), null, input);
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
		throw new UnsupportedOperationException(Msg.code(213));
	}

	@Override
	public void setLogger(ILoggingService theLogger) {
		throw new UnsupportedOperationException(Msg.code(214));
	}

	@Override
	public String getVersion() {
		return myCtx.getVersion().getVersion().getFhirVersionString();
	}

	@Override
	public String getSpecUrl() {
		throw new UnsupportedOperationException(Msg.code(215));
	}

	@Override
	public UcumService getUcumService() {
		throw new UnsupportedOperationException(Msg.code(216));
	}

	@Override
	public void setUcumService(UcumService ucumService) {
		throw new UnsupportedOperationException(Msg.code(217));
	}

	@Override
	public boolean isNoTerminologyServer() {
		return false;
	}

	@Override
	public Set<String> getCodeSystemsUsed() {
		throw new UnsupportedOperationException(Msg.code(218));
	}

	@Override
	public TranslationServices translator() {
		throw new UnsupportedOperationException(Msg.code(219));
	}

	@Override
	public List<StructureMap> listTransforms() {
		throw new UnsupportedOperationException(Msg.code(220));
	}

	@Override
	public StructureMap getTransform(String url) {
		throw new UnsupportedOperationException(Msg.code(221));
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
		throw new UnsupportedOperationException(Msg.code(222));
	}

	@Override
	public List<String> getTypeNames() {
		throw new UnsupportedOperationException(Msg.code(223));
	}

	@Override
	public <T extends org.hl7.fhir.r5.model.Resource> T fetchResource(Class<T> theClass, String theUri) {
		if (myValidationSupport == null || theUri == null) {
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
			throw new FHIRException(Msg.code(224) + "Could not find resource: " + theUri);
		}
		return retVal;
	}

	@Override
	public <T extends Resource> T fetchResource(Class<T> theClass, String theUri, String theVersion) {
		return fetchResource(theClass, theUri + "|" + theVersion);
	}

	@Override
	public <T extends Resource> T fetchResource(Class<T> class_, String uri, CanonicalResource canonicalForSource) {
		throw new UnsupportedOperationException(Msg.code(225));
	}

	@Override
	public org.hl7.fhir.r5.model.Resource fetchResourceById(String theType, String theUri) {
		throw new UnsupportedOperationException(Msg.code(226));
	}

	@Override
	public <T extends org.hl7.fhir.r5.model.Resource> boolean hasResource(Class<T> theClass_, String theUri) {
		throw new UnsupportedOperationException(Msg.code(227));
	}

	@Override
	public void cacheResource(org.hl7.fhir.r5.model.Resource theRes) throws FHIRException {
		throw new UnsupportedOperationException(Msg.code(228));
	}

	@Override
	public void cacheResourceFromPackage(Resource res, PackageVersion packageDetails) throws FHIRException {
		throw new UnsupportedOperationException(Msg.code(229));
	}

	@Override
	public void cachePackage(PackageDetails packageDetails, List<PackageVersion> list) {

	}

	@Override
	public Set<String> getResourceNamesAsSet() {
		return myCtx.getResourceTypes();
	}

	@Override
	public ValueSetExpander.ValueSetExpansionOutcome expandVS(ElementDefinitionBindingComponent theBinding, boolean theCacheOk, boolean theHierarchical) throws FHIRException {
		throw new UnsupportedOperationException(Msg.code(230));
	}


	@Override
	public String getLinkForUrl(String corePath, String url) {
		throw new UnsupportedOperationException(Msg.code(231));
	}

	@Override
	public Map<String, byte[]> getBinaries() {
		throw new UnsupportedOperationException(Msg.code(232));
	}

	@Override
	public int loadFromPackage(NpmPackage pi, IContextResourceLoader loader) throws FHIRException {
		throw new UnsupportedOperationException(Msg.code(233));
	}

	@Override
	public int loadFromPackage(NpmPackage pi, IContextResourceLoader loader, String[] types) throws FHIRException {
		throw new UnsupportedOperationException(Msg.code(234));
	}

	@Override
	public int loadFromPackageAndDependencies(NpmPackage pi, IContextResourceLoader loader, BasePackageCacheManager pcm) throws FHIRException {
		throw new UnsupportedOperationException(Msg.code(235));
	}

	@Override
	public boolean hasPackage(String id, String ver) {
		throw new UnsupportedOperationException(Msg.code(236));
	}

	@Override
	public boolean hasPackage(PackageVersion packageVersion) {
		return false;
	}

	@Override
	public PackageDetails getPackage(PackageVersion packageVersion) {
		return null;
	}

	@Override
	public int getClientRetryCount() {
		throw new UnsupportedOperationException(Msg.code(237));
	}

	@Override
	public IWorkerContext setClientRetryCount(int value) {
		throw new UnsupportedOperationException(Msg.code(238));
	}

	@Override
	public TimeTracker clock() {
		return null;
	}

	@Override
	public PackageVersion getPackageForUrl(String s) {
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
