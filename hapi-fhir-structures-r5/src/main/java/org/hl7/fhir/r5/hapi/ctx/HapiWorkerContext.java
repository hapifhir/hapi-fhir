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
import org.hl7.fhir.exceptions.TerminologyServiceException;
import org.hl7.fhir.r5.context.IContextResourceLoader;
import org.hl7.fhir.r5.context.IWorkerContext;
import org.hl7.fhir.r5.context.IWorkerContextManager;
import org.hl7.fhir.r5.model.CodeSystem;
import org.hl7.fhir.r5.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.r5.model.CodeableConcept;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.ElementDefinition.ElementDefinitionBindingComponent;
import org.hl7.fhir.r5.model.NamingSystem;
import org.hl7.fhir.r5.model.PackageInformation;
import org.hl7.fhir.r5.model.Parameters;
import org.hl7.fhir.r5.model.Resource;
import org.hl7.fhir.r5.model.ResourceType;
import org.hl7.fhir.r5.model.StructureDefinition;
import org.hl7.fhir.r5.model.ValueSet;
import org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.r5.profilemodel.PEBuilder;
import org.hl7.fhir.r5.terminologies.expansion.ValueSetExpansionOutcome;
import org.hl7.fhir.r5.terminologies.utilities.CodingValidationRequest;
import org.hl7.fhir.r5.terminologies.utilities.ValidationResult;
import org.hl7.fhir.r5.utils.validation.IResourceValidator;
import org.hl7.fhir.r5.utils.validation.ValidationContextCarrier;
import org.hl7.fhir.utilities.FhirPublication;
import org.hl7.fhir.utilities.TimeTracker;
import org.hl7.fhir.utilities.i18n.I18nBase;
import org.hl7.fhir.utilities.npm.BasePackageCacheManager;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueSeverity;
import org.hl7.fhir.utilities.validation.ValidationOptions;

import java.io.FileNotFoundException;
import java.io.IOException;
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

		long timeoutMillis = HapiSystemProperties.getTestValidationResourceCachesMs();

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
	public CodeSystem fetchCodeSystem(String theSystem, String version) {
		if (myValidationSupport == null) {
			return null;
		} else {
			return (CodeSystem) myValidationSupport.fetchCodeSystem(theSystem);
		}
	}

	@Override
	public CodeSystem fetchCodeSystem(String system, FhirPublication fhirVersion) {
		throw new UnsupportedOperationException(Msg.code(2456));
	}

	@Override
	public CodeSystem fetchCodeSystem(String system, String version, FhirPublication fhirVersion) {
		throw new UnsupportedOperationException(Msg.code(2457));
	}

	@Override
	public CodeSystem fetchSupplementedCodeSystem(String theS) {
		return null;
	}

	@Override
	public CodeSystem fetchSupplementedCodeSystem(String theS, String theS1) {
		return null;
	}

	@Override
	public CodeSystem fetchSupplementedCodeSystem(String system, FhirPublication fhirVersion) {
		throw new UnsupportedOperationException(Msg.code(2458));
	}

	@Override
	public CodeSystem fetchSupplementedCodeSystem(String system, String version, FhirPublication fhirVersion) {
		throw new UnsupportedOperationException(Msg.code(2459));
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
	public List<String> getResourceNames(FhirPublication fhirVersion) {
		throw new UnsupportedOperationException(Msg.code(2460));
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
	public boolean supportsSystem(String theSystem) {
		if (myValidationSupport == null) {
			return false;
		} else {
			return myValidationSupport.isCodeSystemSupported(
					new ValidationSupportContext(myValidationSupport), theSystem);
		}
	}

	@Override
	public boolean supportsSystem(String system, FhirPublication fhirVersion) throws TerminologyServiceException {
		if (!fhirVersion.equals(FhirPublication.R5)) {
			throw new UnsupportedOperationException(Msg.code(2461));
		}
		return supportsSystem(system);
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
			ValidationOptions options, List<? extends CodingValidationRequest> codes, ValueSet vs) {
		throw new UnsupportedOperationException(Msg.code(209));
	}

	@Override
	public void validateCodeBatchByRef(
			ValidationOptions validationOptions, List<? extends CodingValidationRequest> list, String s) {
		throw new UnsupportedOperationException(Msg.code(2430));
	}

	@Override
	public ValueSetExpansionOutcome expandVS(
			ValueSet theValueSet, boolean cacheOk, boolean heiarchical, boolean incompleteOk) {
		return null;
	}

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

	@Override
	public void setExpansionParameters(Parameters expParameters) {
		setExpansionProfile(expParameters);
	}

	public void setExpansionProfile(Parameters theExpParameters) {
		myExpansionProfile = theExpParameters;
	}

	@Override
	public ValueSetExpansionOutcome expandVS(ValueSet theSource, boolean theCacheOk, boolean theHierarchical) {
		throw new UnsupportedOperationException(Msg.code(2128));
	}

	@Override
	public ValueSetExpansionOutcome expandVS(ConceptSetComponent theInc, boolean theHierarchical, boolean theNoInactive)
			throws TerminologyServiceException {
		ValueSet input = new ValueSet();
		input.getCompose().setInactive(!theNoInactive); // TODO GGG/DO is this valid?
		input.getCompose().addInclude(theInc);
		IValidationSupport.ValueSetExpansionOutcome output =
				myValidationSupport.expandValueSet(new ValidationSupportContext(myValidationSupport), null, input);
		return new ValueSetExpansionOutcome(
				(ValueSet) output.getValueSet(), output.getError(), null, output.getErrorIsFromServer());
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
	public void setLogger(org.hl7.fhir.r5.context.ILoggingService theLogger) {
		throw new UnsupportedOperationException(Msg.code(214));
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
	public StructureDefinition fetchTypeDefinition(String typeName, FhirPublication fhirVersion) {
		throw new UnsupportedOperationException(Msg.code(2464));
	}

	@Override
	public List<StructureDefinition> fetchTypeDefinitions(String n) {
		throw new UnsupportedOperationException(Msg.code(234));
	}

	@Override
	public List<StructureDefinition> fetchTypeDefinitions(String n, FhirPublication fhirPublication) {
		throw new UnsupportedOperationException(Msg.code(2465));
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
	public <T extends Resource> T fetchResourceWithException(Class<T> theClass, String uri, Resource sourceOfReference)
			throws FHIRException {
		throw new UnsupportedOperationException(Msg.code(2213));
	}

	@Override
	public <T extends Resource> T fetchResource(Class<T> theClass, String theUri, String theVersion) {
		return fetchResource(theClass, theUri + "|" + theVersion);
	}

	@Override
	public <T extends Resource> T fetchResource(
			Class<T> class_, String uri, String version, FhirPublication fhirVersion) {
		throw new UnsupportedOperationException(Msg.code(2467));
	}

	@Override
	public <T extends Resource> T fetchResource(Class<T> class_, String uri, Resource canonicalForSource) {
		return fetchResource(class_, uri);
	}

	@Override
	public <T extends Resource> List<T> fetchResourcesByType(Class<T> class_, FhirPublication fhirVersion) {
		throw new UnsupportedOperationException(Msg.code(2468));
	}

	@Override
	public org.hl7.fhir.r5.model.Resource fetchResourceById(String theType, String theUri) {
		throw new UnsupportedOperationException(Msg.code(226));
	}

	@Override
	public Resource fetchResourceById(String type, String uri, FhirPublication fhirVersion) {
		throw new UnsupportedOperationException(Msg.code(2469));
	}

	@Override
	public <T extends org.hl7.fhir.r5.model.Resource> boolean hasResource(Class<T> theClass_, String theUri) {
		throw new UnsupportedOperationException(Msg.code(227));
	}

	@Override
	public <T extends Resource> boolean hasResource(Class<T> class_, String uri, Resource sourceOfReference) {
		throw new UnsupportedOperationException(Msg.code(2470));
	}

	@Override
	public <T extends Resource> boolean hasResource(Class<T> class_, String uri, FhirPublication fhirVersion) {
		throw new UnsupportedOperationException(Msg.code(2471));
	}

	@Override
	public void cacheResource(org.hl7.fhir.r5.model.Resource theRes) throws FHIRException {
		throw new UnsupportedOperationException(Msg.code(228));
	}

	@Override
	public void cacheResourceFromPackage(Resource res, PackageInformation packageDetails) throws FHIRException {
		throw new UnsupportedOperationException(Msg.code(229));
	}

	@Override
	public void cachePackage(PackageInformation packageInformation) {}

	@Override
	public Set<String> getResourceNamesAsSet() {
		return myCtx.getResourceTypes();
	}

	@Override
	public Set<String> getResourceNamesAsSet(FhirPublication fhirVersion) {
		throw new UnsupportedOperationException(Msg.code(2472));
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
	public int loadFromPackage(NpmPackage pi, IContextResourceLoader loader) throws FHIRException {
		throw new UnsupportedOperationException(Msg.code(233));
	}

	@Override
	public int loadFromPackage(NpmPackage pi, IContextResourceLoader loader, List<String> types)
			throws FileNotFoundException, IOException, FHIRException {
		throw new UnsupportedOperationException(Msg.code(2328));
	}

	@Override
	public int loadFromPackageAndDependencies(NpmPackage pi, IContextResourceLoader loader, BasePackageCacheManager pcm)
			throws FHIRException {
		throw new UnsupportedOperationException(Msg.code(235));
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
	public <T extends Resource> List<T> fetchResourcesByUrl(Class<T> class_, String url) {
		throw new UnsupportedOperationException(Msg.code(2508) + "Can't fetch all resources of url: " + url);
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
	public OIDSummary urlsForOid(String oid, String resourceType) {
		throw new UnsupportedOperationException(Msg.code(2473));
	}

	@Override
	public <T extends Resource> T findTxResource(Class<T> class_, String canonical, Resource sourceOfReference) {
		throw new UnsupportedOperationException(Msg.code(2491));
	}

	@Override
	public <T extends Resource> T findTxResource(Class<T> class_, String canonical) {
		throw new UnsupportedOperationException(Msg.code(2492));
	}

	@Override
	public <T extends Resource> T findTxResource(Class<T> class_, String canonical, String version) {
		throw new UnsupportedOperationException(Msg.code(2493));
	}

	@Override
	public Boolean subsumes(ValidationOptions optionsArg, Coding parent, Coding child) {
		throw new UnsupportedOperationException(Msg.code(2488));
	}

	@Override
	public boolean isServerSideSystem(String url) {
		return false;
	}
}
