package org.hl7.fhir.r5.hapi.ctx;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.sl.cache.Cache;
import ca.uhn.fhir.sl.cache.CacheFactory;
import ca.uhn.fhir.system.HapiSystemProperties;
import org.apache.commons.lang3.Validate;
import org.fhir.ucum.UcumService;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r5.context.ExpansionOptions;
import org.hl7.fhir.r5.context.IOIDServices;
import org.hl7.fhir.r5.context.IWorkerContext;
import org.hl7.fhir.r5.context.IWorkerContextManager;
import org.hl7.fhir.r5.model.CodeSystem;
import org.hl7.fhir.r5.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.r5.model.CodeableConcept;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.ElementDefinition.ElementDefinitionBindingComponent;
import org.hl7.fhir.r5.model.NamingSystem;
import org.hl7.fhir.r5.model.OperationOutcome;
import org.hl7.fhir.r5.model.PackageInformation;
import org.hl7.fhir.r5.model.Parameters;
import org.hl7.fhir.r5.model.Resource;
import org.hl7.fhir.r5.model.ResourceType;
import org.hl7.fhir.r5.model.StructureDefinition;
import org.hl7.fhir.r5.model.ValueSet;
import org.hl7.fhir.r5.profilemodel.PEBuilder;
import org.hl7.fhir.r5.terminologies.expansion.ValueSetExpansionOutcome;
import org.hl7.fhir.r5.terminologies.utilities.CodingValidationRequest;
import org.hl7.fhir.r5.terminologies.utilities.ValidationResult;
import org.hl7.fhir.r5.utils.validation.IResourceValidator;
import org.hl7.fhir.r5.utils.validation.ValidationContextCarrier;
import org.hl7.fhir.utilities.FhirPublication;
import org.hl7.fhir.utilities.TimeTracker;
import org.hl7.fhir.utilities.i18n.I18nBase;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueSeverity;
import org.hl7.fhir.utilities.validation.ValidationOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

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

		long timeoutMillis = HapiSystemProperties.getValidationResourceCacheTimeoutMillis();

		myFetchedResourceCache = CacheFactory.build(timeoutMillis);

		// Set a default locale
		setValidationMessageLanguage(getLocale());
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
	public CodeSystem fetchCodeSystem(String theSystem, String version, Resource sourceOfReference) {
		if (myValidationSupport == null) {
			return null;
		} else {
			return (CodeSystem) myValidationSupport.fetchCodeSystem(theSystem);
		}
	}

	@Override
	public CodeSystem fetchSupplementedCodeSystem(String theS) {
		return null;
	}

	@Override
	public CodeSystem fetchSupplementedCodeSystem(String system, String version, Resource sourceOfReference) {
		return null;
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
	public IResourceValidator newValidator() {
		throw new UnsupportedOperationException(Msg.code(206));
	}

	@Override
	public Map<String, NamingSystem> getNSUrlMap() {
		throw new UnsupportedOperationException(Msg.code(2241));
	}

	@Override
	public SystemSupportInformation getTxSupportInfo(String system, String version) {
		return null;
	}

	@Override
	public ValidationResult validateCode(ValidationOptions theOptions, CodeableConcept theCode, ValueSet theVs) {
		for (Coding next : theCode.getCoding()) {
			ValidationResult retVal = validateCode(theOptions, next, theVs);
			if (retVal.isOk()) {
				return retVal;
			}
		}

		return new ValidationResult(IssueSeverity.ERROR, null, null);
	}

	@Override
	public ValidationResult validateCode(ValidationOptions theOptions, Coding theCode, ValueSet theVs) {
		String system = theCode.getSystem();
		String code = theCode.getCode();
		String display = theCode.getDisplay();
		return validateCode(theOptions, system, null, code, display, theVs);
	}

	@Override
	public ValidationResult validateCode(
			ValidationOptions options, Coding code, ValueSet vs, ValidationContextCarrier ctxt) {
		return validateCode(options, code, vs);
	}

	@Override
	public void validateCodeBatch(
			ValidationOptions options, List<? extends CodingValidationRequest> codes, ValueSet vs, boolean passVS) {}

	@Override
	public ValidationResult validateCode(
			ValidationOptions theOptions, String theSystem, String theVersion, String theCode, String theDisplay) {
		IValidationSupport.CodeValidationResult result = myValidationSupport.validateCode(
				new ValidationSupportContext(myValidationSupport),
				convertConceptValidationOptions(theOptions),
				theSystem,
				theCode,
				theDisplay,
				null);
		if (result == null) {
			return null;
		}
		IssueSeverity severity = null;
		if (result.getSeverity() != null) {
			severity = IssueSeverity.fromCode(result.getSeverityCode());
		}
		ConceptDefinitionComponent definition = new ConceptDefinitionComponent().setCode(result.getCode());
		return new ValidationResult(severity, result.getMessage(), theSystem, theVersion, definition, null, null);
	}

	@Override
	public ValidationResult validateCode(
			ValidationOptions theOptions,
			String theSystem,
			String theVersion,
			String theCode,
			String theDisplay,
			ValueSet theVs) {
		IValidationSupport.CodeValidationResult outcome;
		if (isNotBlank(theVs.getUrl())) {
			outcome = myValidationSupport.validateCode(
					new ValidationSupportContext(myValidationSupport),
					convertConceptValidationOptions(theOptions),
					theSystem,
					theCode,
					theDisplay,
					theVs.getUrl());
		} else {
			outcome = myValidationSupport.validateCodeInValueSet(
					new ValidationSupportContext(myValidationSupport),
					convertConceptValidationOptions(theOptions),
					theSystem,
					theCode,
					theDisplay,
					theVs);
		}

		if (outcome != null && outcome.isOk()) {
			ConceptDefinitionComponent definition = new ConceptDefinitionComponent();
			definition.setCode(theCode);
			definition.setDisplay(outcome.getDisplay());
			return new ValidationResult(theSystem, theVersion, definition, null);
		}

		return new ValidationResult(
				IssueSeverity.ERROR,
				"Unknown code[" + theCode + "] in system[" + Constants.codeSystemWithDefaultDescription(theSystem)
						+ "]",
				null);
	}

	@Override
	public ValidationResult validateCode(ValidationOptions theOptions, String code, ValueSet vs) {
		return validateCode(theOptions, null, null, code, null, vs);
	}

	@Override
	public Parameters getExpansionParameters() {
		return myExpansionProfile;
	}

	public void setExpansionProfile(Parameters theExpParameters) {
		myExpansionProfile = theExpParameters;
	}

	@Override
	public ValueSetExpansionOutcome expandVS(ValueSet theSource, boolean theCacheOk, boolean theHierarchical) {
		throw new UnsupportedOperationException(Msg.code(2128));
	}

	@Override
	public ValueSetExpansionOutcome expandVS(ExpansionOptions options, ValueSet source) {
		throw new UnsupportedOperationException(Msg.code(2823));
	}

	@Override
	public ValueSetExpansionOutcome expandVS(ValueSet theSource, boolean theCacheOk, boolean theHierarchical, int i) {
		throw new UnsupportedOperationException(Msg.code(2650));
	}

	@Override
	public ValueSetExpansionOutcome expandVS(ExpansionOptions options, String uri) {
		throw new UnsupportedOperationException(Msg.code(2824));
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
	public org.hl7.fhir.r5.context.ILoggingService getLogger() {
		throw new UnsupportedOperationException(Msg.code(213));
	}

	@Override
	public String getVersion() {
		return myCtx.getVersion().getVersion().getFhirVersionString();
	}

	@Override
	public UcumService getUcumService() {
		throw new UnsupportedOperationException(Msg.code(216));
	}

	@Override
	public IOIDServices oidServices() {
		throw new UnsupportedOperationException(Msg.code(2825));
	}

	@Override
	public IWorkerContextManager getManager() {
		throw new UnsupportedOperationException(Msg.code(2826));
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
	public StructureDefinition fetchTypeDefinition(String typeName) {
		return fetchResource(StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/" + typeName);
	}

	@Override
	public boolean isPrimitiveType(String s) {
		throw new UnsupportedOperationException(Msg.code(2462));
	}

	@Override
	public boolean isDataType(String s) {
		throw new UnsupportedOperationException(Msg.code(2463));
	}

	@Override
	public List<StructureDefinition> fetchTypeDefinitions(String n) {
		throw new UnsupportedOperationException(Msg.code(234));
	}

	@Override
	public <T extends Resource> T fetchResourceRaw(Class<T> class_, String uri) {
		return fetchResource(class_, uri);
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

	public <T extends Resource> T fetchResource(Class<T> class_, String uri, FhirPublication fhirVersion) {
		throw new UnsupportedOperationException(Msg.code(2466));
	}

	@Override
	public <T extends org.hl7.fhir.r5.model.Resource> T fetchResourceWithException(Class<T> theClass, String theUri)
			throws FHIRException {
		T retVal = fetchResource(theClass, theUri);
		if (retVal == null) {
			throw new FHIRException(Msg.code(224) + "Could not find resource: " + theUri);
		}
		return retVal;
	}

	@Override
	public <T extends Resource> T fetchResourceWithException(
			Class<T> theClass, String uri, String version, Resource sourceOfReference) throws FHIRException {
		throw new UnsupportedOperationException(Msg.code(2213));
	}

	@Override
	public <T extends Resource> T fetchResource(
			Class<T> theClass, String theUri, String theVersion, Resource sourceOfReference) {
		if (theVersion == null) {
			return fetchResource(theClass, theUri);
		}
		return fetchResource(theClass, theUri + "|" + theVersion);
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
	public <T extends Resource> boolean hasResource(
			Class<T> class_, String uri, String version, Resource sourceOfReference) {
		throw new UnsupportedOperationException(Msg.code(2470));
	}

	@Override
	public Set<String> getResourceNamesAsSet() {
		return myCtx.getResourceTypes();
	}

	@Override
	public ValueSetExpansionOutcome expandVS(
			Resource src, ElementDefinitionBindingComponent theBinding, boolean theCacheOk, boolean theHierarchical)
			throws FHIRException {
		throw new UnsupportedOperationException(Msg.code(230));
	}

	@Override
	public Set<String> getBinaryKeysAsSet() {
		throw new UnsupportedOperationException(Msg.code(2115));
	}

	@Override
	public boolean hasBinaryKey(String s) {
		throw new UnsupportedOperationException(Msg.code(2129));
	}

	@Override
	public byte[] getBinaryForKey(String s) {
		throw new UnsupportedOperationException(Msg.code(2199));
	}

	@Override
	public boolean hasPackage(String id, String ver) {
		throw new UnsupportedOperationException(Msg.code(236));
	}

	@Override
	public boolean hasPackage(PackageInformation packageVersion) {
		return false;
	}

	@Override
	public PackageInformation getPackage(String id, String ver) {
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
	public IWorkerContextManager.IPackageLoadingTracker getPackageTracker() {
		throw new UnsupportedOperationException(Msg.code(2112));
	}

	@Override
	public PackageInformation getPackageForUrl(String s) {
		return null;
	}

	public static ConceptValidationOptions convertConceptValidationOptions(ValidationOptions theOptions) {
		ConceptValidationOptions retVal = new ConceptValidationOptions();
		if (theOptions.isGuessSystem()) {
			retVal = retVal.setInferSystem(true);
		}
		return retVal;
	}

	@Override
	public <T extends Resource> List<T> fetchResourcesByType(Class<T> theClass) {
		if (theClass.equals(StructureDefinition.class)) {
			return myValidationSupport.fetchAllStructureDefinitions();
		}

		throw new UnsupportedOperationException(Msg.code(2113) + "Can't fetch all resources of type: " + theClass);
	}

	@Override
	public <T extends Resource> List<T> fetchResourceVersions(Class<T> class_, String url) {
		throw new UnsupportedOperationException(Msg.code(2827));
	}

	@Override
	public IWorkerContext setPackageTracker(IWorkerContextManager.IPackageLoadingTracker theIPackageLoadingTracker) {
		throw new UnsupportedOperationException(Msg.code(220));
	}

	@Override
	public String getSpecUrl() {
		return "";
	}

	@Override
	public PEBuilder getProfiledElementBuilder(
			PEBuilder.PEElementPropertiesPolicy thePEElementPropertiesPolicy, boolean theB) {
		throw new UnsupportedOperationException(Msg.code(2261));
	}

	@Override
	public boolean isForPublication() {
		return false;
	}

	@Override
	public void setForPublication(boolean b) {
		throw new UnsupportedOperationException(Msg.code(2350));
	}

	@Override
	public <T extends Resource> T findTxResource(
			Class<T> class_, String canonical, String version, Resource sourceOfReference) {
		throw new UnsupportedOperationException(Msg.code(2829));
	}

	@Override
	public <T extends Resource> T findTxResource(Class<T> class_, String canonical) {
		throw new UnsupportedOperationException(Msg.code(2492));
	}

	@Override
	public Boolean subsumes(ValidationOptions optionsArg, Coding parent, Coding child) {
		throw new UnsupportedOperationException(Msg.code(2488));
	}

	@Override
	public OperationOutcome validateTxResource(ValidationOptions options, Resource resource) {
		throw new UnsupportedOperationException(Msg.code(2734));
	}
}
