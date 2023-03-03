package org.hl7.fhir.common.hapi.validation.validator;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.sl.cache.CacheFactory;
import ca.uhn.fhir.sl.cache.LoadingCache;
import ca.uhn.fhir.system.HapiSystemProperties;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.fhir.ucum.UcumService;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.TerminologyServiceException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.context.IWorkerContext;
import org.hl7.fhir.r5.context.IWorkerContextManager;
import org.hl7.fhir.r5.model.CodeSystem;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.NamingSystem;
import org.hl7.fhir.r5.model.PackageInformation;
import org.hl7.fhir.r5.model.Resource;
import org.hl7.fhir.r5.model.StructureDefinition;
import org.hl7.fhir.r5.model.ValueSet;
import org.hl7.fhir.r5.profilemodel.PEBuilder;
import org.hl7.fhir.r5.terminologies.ValueSetExpander;
import org.hl7.fhir.r5.utils.validation.IResourceValidator;
import org.hl7.fhir.r5.utils.validation.ValidationContextCarrier;
import org.hl7.fhir.utilities.TimeTracker;
import org.hl7.fhir.utilities.TranslationServices;
import org.hl7.fhir.utilities.i18n.I18nBase;
import org.hl7.fhir.utilities.npm.BasePackageCacheManager;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.hl7.fhir.utilities.validation.ValidationMessage;
import org.hl7.fhir.utilities.validation.ValidationOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class VersionSpecificWorkerContextWrapper extends I18nBase implements IWorkerContext {
	private static final Logger ourLog = LoggerFactory.getLogger(VersionSpecificWorkerContextWrapper.class);
	private final ValidationSupportContext myValidationSupportContext;
	private final VersionCanonicalizer myVersionCanonicalizer;
	private final LoadingCache<ResourceKey, IBaseResource> myFetchResourceCache;
	private volatile List<StructureDefinition> myAllStructures;
	private org.hl7.fhir.r5.model.Parameters myExpansionProfile;

	public VersionSpecificWorkerContextWrapper(ValidationSupportContext theValidationSupportContext, VersionCanonicalizer theVersionCanonicalizer) {
		myValidationSupportContext = theValidationSupportContext;
		myVersionCanonicalizer = theVersionCanonicalizer;

		long timeoutMillis = HapiSystemProperties.getTestValidationResourceCachesMs();

		myFetchResourceCache = CacheFactory.build(timeoutMillis, 10000, key -> {

			String fetchResourceName = key.getResourceName();
			if (myValidationSupportContext.getRootValidationSupport().getFhirContext().getVersion().getVersion() == FhirVersionEnum.DSTU2) {
				if ("CodeSystem".equals(fetchResourceName)) {
					fetchResourceName = "ValueSet";
				}
			}

			Class<? extends IBaseResource> fetchResourceType;
			if (fetchResourceName.equals("Resource")) {
				fetchResourceType = null;
			} else {
				fetchResourceType = myValidationSupportContext.getRootValidationSupport().getFhirContext().getResourceDefinition(fetchResourceName).getImplementingClass();
			}

			IBaseResource fetched = myValidationSupportContext.getRootValidationSupport().fetchResource(fetchResourceType, key.getUri());

			Resource canonical = myVersionCanonicalizer.resourceToValidatorCanonical(fetched);

			if (canonical instanceof StructureDefinition) {
				StructureDefinition canonicalSd = (StructureDefinition) canonical;
				if (canonicalSd.getSnapshot().isEmpty()) {
					ourLog.info("Generating snapshot for StructureDefinition: {}", canonicalSd.getUrl());
					fetched = myValidationSupportContext.getRootValidationSupport().generateSnapshot(theValidationSupportContext, fetched, "", null, "");
					Validate.isTrue(fetched != null, "StructureDefinition %s has no snapshot, and no snapshot generator is configured", key.getUri());
					canonical = myVersionCanonicalizer.resourceToValidatorCanonical(fetched);
				}
			}

			return canonical;
		});

		setValidationMessageLanguage(getLocale());
	}

	@Override
	public Set<String> getBinaryKeysAsSet() {
		throw new UnsupportedOperationException(Msg.code(2118));
	}

	@Override
	public boolean hasBinaryKey(String s) {
		return myValidationSupportContext.getRootValidationSupport().fetchBinary(s) != null;
	}

	@Override
	public byte[] getBinaryForKey(String s) {
		return myValidationSupportContext.getRootValidationSupport().fetchBinary(s);
	}

	@Override
	public int loadFromPackage(NpmPackage pi, IContextResourceLoader loader) throws FHIRException {
		throw new UnsupportedOperationException(Msg.code(652));
	}

	@Override
	public int loadFromPackage(NpmPackage pi, IContextResourceLoader loader, String[] types) throws FHIRException {
		throw new UnsupportedOperationException(Msg.code(653));
	}

	@Override
	public int loadFromPackageAndDependencies(NpmPackage pi, IContextResourceLoader loader, BasePackageCacheManager pcm) throws FHIRException {
		throw new UnsupportedOperationException(Msg.code(654));
	}

	@Override
	public boolean hasPackage(String id, String ver) {
		throw new UnsupportedOperationException(Msg.code(655));
	}

	@Override
	public boolean hasPackage(PackageInformation packageInformation) {
		return false;
	}

	@Override
	public PackageInformation getPackage(String id, String ver) {
		return null;
	}

	@Override
	public int getClientRetryCount() {
		throw new UnsupportedOperationException(Msg.code(656));
	}

	@Override
	public IWorkerContext setClientRetryCount(int value) {
		throw new UnsupportedOperationException(Msg.code(657));
	}

	@Override
	public TimeTracker clock() {
		return null;
	}

	@Override
	public IWorkerContextManager.IPackageLoadingTracker getPackageTracker() {
		throw new UnsupportedOperationException(Msg.code(2235));
	}

	@Override
	public IWorkerContext setPackageTracker(
		IWorkerContextManager.IPackageLoadingTracker packageTracker) {
		throw new UnsupportedOperationException(Msg.code(2266));
	}

	@Override
	public String getSpecUrl() {
			return "";
	}

	@Override
	public PEBuilder getProfiledElementBuilder(PEBuilder.PEElementPropertiesPolicy thePEElementPropertiesPolicy, boolean theB) {
		throw new UnsupportedOperationException(Msg.code(2264));
	}

	@Override
	public PackageInformation getPackageForUrl(String s) {
		throw new UnsupportedOperationException(Msg.code(2236));
	}

	@Override
	public org.hl7.fhir.r5.model.Parameters getExpansionParameters() {
		return myExpansionProfile;
	}

	@Override
	public void setExpansionProfile(org.hl7.fhir.r5.model.Parameters expParameters) {
		myExpansionProfile = expParameters;
	}

	private List<StructureDefinition> allStructures() {

		List<StructureDefinition> retVal = myAllStructures;
		if (retVal == null) {
			retVal = new ArrayList<>();
			for (IBaseResource next : myValidationSupportContext.getRootValidationSupport().fetchAllStructureDefinitions()) {
				try {
					StructureDefinition converted = myVersionCanonicalizer.structureDefinitionToCanonical(next);
					retVal.add(converted);
				} catch (FHIRException e) {
					throw new InternalErrorException(Msg.code(659) + e);
				}
			}
			myAllStructures = retVal;
		}

		return retVal;
	}

	@Override
	public void cacheResource(Resource res) {

	}

	@Override
	public void cacheResourceFromPackage(Resource res, PackageInformation packageDetails) throws FHIRException {

	}

	@Override
	public void cachePackage(PackageInformation packageInformation) {

	}

	@Nonnull
	private ValidationResult convertValidationResult(String theSystem, @Nullable IValidationSupport.CodeValidationResult theResult) {
		ValidationResult retVal = null;
		if (theResult != null) {
			String code = theResult.getCode();
			String display = theResult.getDisplay();
			String issueSeverity = theResult.getSeverityCode();
			String message = theResult.getMessage();
			if (isNotBlank(code)) {
				retVal = new ValidationResult(theSystem, new org.hl7.fhir.r5.model.CodeSystem.ConceptDefinitionComponent()
					.setCode(code)
					.setDisplay(display));
			} else if (isNotBlank(issueSeverity)) {
				retVal = new ValidationResult(ValidationMessage.IssueSeverity.fromCode(issueSeverity), message, ValueSetExpander.TerminologyServiceErrorClass.UNKNOWN);
			}

		}

		if (retVal == null) {
			retVal = new ValidationResult(ValidationMessage.IssueSeverity.ERROR, "Validation failed");
		}

		return retVal;
	}

	@Override
	public ValueSetExpander.ValueSetExpansionOutcome expandVS(org.hl7.fhir.r5.model.ValueSet source, boolean cacheOk, boolean Hierarchical) {
		IBaseResource convertedSource;
		try {
			convertedSource = myVersionCanonicalizer.valueSetFromValidatorCanonical(source);
		} catch (FHIRException e) {
			throw new InternalErrorException(Msg.code(661) + e);
		}
		IValidationSupport.ValueSetExpansionOutcome expanded = myValidationSupportContext.getRootValidationSupport().expandValueSet(myValidationSupportContext, null, convertedSource);

		org.hl7.fhir.r5.model.ValueSet convertedResult = null;
		if (expanded.getValueSet() != null) {
			try {
				convertedResult = myVersionCanonicalizer.valueSetToValidatorCanonical(expanded.getValueSet());
			} catch (FHIRException e) {
				throw new InternalErrorException(Msg.code(662) + e);
			}
		}

		String error = expanded.getError();
		ValueSetExpander.TerminologyServiceErrorClass result = null;

		return new ValueSetExpander.ValueSetExpansionOutcome(convertedResult, error, result);
	}

	@Override
	public ValueSetExpander.ValueSetExpansionOutcome expandVS(Resource src, org.hl7.fhir.r5.model.ElementDefinition.ElementDefinitionBindingComponent binding, boolean cacheOk, boolean Hierarchical) {
		throw new UnsupportedOperationException(Msg.code(663));
	}

	@Override
	public ValueSetExpander.ValueSetExpansionOutcome expandVS(ValueSet.ConceptSetComponent inc, boolean hierarchical, boolean noInactive) throws TerminologyServiceException {
		throw new UnsupportedOperationException(Msg.code(664));
	}

	@Override
	public Locale getLocale() {
		return myValidationSupportContext.getRootValidationSupport().getFhirContext().getLocalizer().getLocale();
	}

	@Override
	public void setLocale(Locale locale) {
		// ignore
	}

	@Override
	public org.hl7.fhir.r5.model.CodeSystem fetchCodeSystem(String system) {
		IBaseResource fetched = myValidationSupportContext.getRootValidationSupport().fetchCodeSystem(system);
		if (fetched == null) {
			return null;
		}
		try {
			return (org.hl7.fhir.r5.model.CodeSystem) myVersionCanonicalizer.codeSystemToValidatorCanonical(fetched);
		} catch (FHIRException e) {
			throw new InternalErrorException(Msg.code(665) + e);
		}
	}

	@Override
	public CodeSystem fetchCodeSystem(String system, String verison) {
		IBaseResource fetched = myValidationSupportContext.getRootValidationSupport().fetchCodeSystem(system);
		if (fetched == null) {
			return null;
		}
		try {
			return (org.hl7.fhir.r5.model.CodeSystem) myVersionCanonicalizer.codeSystemToValidatorCanonical(fetched);
		} catch (FHIRException e) {
			throw new InternalErrorException(Msg.code(1992) + e);
		}
	}

	@Override
	public <T extends Resource> T fetchResource(Class<T> class_, String uri) {

		if (isBlank(uri)) {
			return null;
		}

		ResourceKey key = new ResourceKey(class_.getSimpleName(), uri);
		@SuppressWarnings("unchecked")
		T retVal = (T) myFetchResourceCache.get(key);

		return retVal;
	}

	@Override
	public Resource fetchResourceById(String type, String uri) {
		throw new UnsupportedOperationException(Msg.code(666));
	}

	@Override
	public <T extends Resource> T fetchResourceWithException(Class<T> class_, String uri) throws FHIRException {
		T retVal = fetchResource(class_, uri);
		if (retVal == null) {
			throw new FHIRException(Msg.code(667) + "Can not find resource of type " + class_.getSimpleName() + " with uri " + uri);
		}
		return retVal;
	}

	@Override
	public <T extends Resource> T fetchResource(Class<T> class_, String uri, String version) {
		return fetchResource(class_, uri + "|" + version);
	}

	@Override
	public <T extends Resource> T fetchResource(Class<T> class_, String uri, Resource canonicalForSource) {
		return fetchResource(class_, uri);
	}

	@Override
	public <T extends Resource> T fetchResourceWithException(Class<T> class_, String uri, Resource sourceOfReference) throws FHIRException {
		throw new UnsupportedOperationException(Msg.code(2214));
	}

	@Override
	public List<String> getResourceNames() {
		return new ArrayList<>(myValidationSupportContext.getRootValidationSupport().getFhirContext().getResourceTypes());
	}

	@Override
	public Set<String> getResourceNamesAsSet() {
		return myValidationSupportContext.getRootValidationSupport().getFhirContext().getResourceTypes();
	}

	@Override
	public StructureDefinition fetchTypeDefinition(String typeName) {
		return fetchResource(StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/" + typeName);
	}

	@Override
	public UcumService getUcumService() {
		throw new UnsupportedOperationException(Msg.code(676));
	}

	@Override
	public void setUcumService(UcumService ucumService) {
		throw new UnsupportedOperationException(Msg.code(677));
	}

	@Override
	public String getVersion() {
		return myValidationSupportContext.getRootValidationSupport().getFhirContext().getVersion().getVersion().getFhirVersionString();
	}

	@Override
	public <T extends Resource> boolean hasResource(Class<T> class_, String uri) {
		throw new UnsupportedOperationException(Msg.code(680));
	}

	@Override
	public boolean isNoTerminologyServer() {
		return false;
	}

	@Override
	public Set<String> getCodeSystemsUsed() {
		throw new UnsupportedOperationException(Msg.code(681));
	}

	@Override
	public IResourceValidator newValidator() {
		throw new UnsupportedOperationException(Msg.code(684));
	}

	@Override
	public Map<String, NamingSystem> getNSUrlMap() {
		throw new UnsupportedOperationException(Msg.code(2265));
	}

	@Override
	public ILoggingService getLogger() {
		return null;
	}

	@Override
	public void setLogger(ILoggingService logger) {
		throw new UnsupportedOperationException(Msg.code(687));
	}

	@Override
	public boolean supportsSystem(String system) {
		return myValidationSupportContext.getRootValidationSupport().isCodeSystemSupported(myValidationSupportContext, system);
	}

	@Override
	public TranslationServices translator() {
		throw new UnsupportedOperationException(Msg.code(688));
	}

	@Override
	public ValueSetExpander.ValueSetExpansionOutcome expandVS(ValueSet source, boolean cacheOk, boolean heiarchical, boolean incompleteOk) {
		return null;
	}

	@Override
	public ValidationResult validateCode(ValidationOptions theOptions, String system, String version, String code, String display) {
		ConceptValidationOptions validationOptions = convertConceptValidationOptions(theOptions);
		return doValidation(null, validationOptions, system, code, display);
	}

	@Override
	public ValidationResult validateCode(ValidationOptions theOptions, String theSystem, String version, String theCode, String display, ValueSet theValueSet) {
		IBaseResource convertedVs = null;

		try {
			if (theValueSet != null) {
				convertedVs = myVersionCanonicalizer.valueSetFromValidatorCanonical(theValueSet);
			}
		} catch (FHIRException e) {
			throw new InternalErrorException(Msg.code(689) + e);
		}

		ConceptValidationOptions validationOptions = convertConceptValidationOptions(theOptions);

		return doValidation(convertedVs, validationOptions, theSystem, theCode, display);
	}

	@Override
	public ValidationResult validateCode(ValidationOptions theOptions, String code, org.hl7.fhir.r5.model.ValueSet theValueSet) {
		IBaseResource convertedVs = null;
		try {
			if (theValueSet != null) {
				convertedVs = myVersionCanonicalizer.valueSetFromValidatorCanonical(theValueSet);
			}
		} catch (FHIRException e) {
			throw new InternalErrorException(Msg.code(690) + e);
		}

		ConceptValidationOptions validationOptions = convertConceptValidationOptions(theOptions).setInferSystem(true);

		return doValidation(convertedVs, validationOptions, null, code, null);
	}

	@Override
	public ValidationResult validateCode(ValidationOptions theOptions, org.hl7.fhir.r5.model.Coding theCoding, org.hl7.fhir.r5.model.ValueSet theValueSet) {
		IBaseResource convertedVs = null;

		try {
			if (theValueSet != null) {
				convertedVs = myVersionCanonicalizer.valueSetFromValidatorCanonical(theValueSet);
			}
		} catch (FHIRException e) {
			throw new InternalErrorException(Msg.code(691) + e);
		}

		ConceptValidationOptions validationOptions = convertConceptValidationOptions(theOptions);
		String system = theCoding.getSystem();
		String code = theCoding.getCode();
		String display = theCoding.getDisplay();

		return doValidation(convertedVs, validationOptions, system, code, display);
	}

	@Override
	public ValidationResult validateCode(ValidationOptions options, Coding code, ValueSet vs, ValidationContextCarrier ctxt) {
		return validateCode(options, code, vs);
	}

	@Override
	public void validateCodeBatch(ValidationOptions options, List<? extends CodingValidationRequest> codes, ValueSet vs) {
		for (CodingValidationRequest next : codes) {
			ValidationResult outcome = validateCode(options, next.getCoding(), vs);
			next.setResult(outcome);
		}
	}

	@Nonnull
	private ValidationResult doValidation(IBaseResource theValueSet, ConceptValidationOptions theValidationOptions, String theSystem, String theCode, String theDisplay) {
		IValidationSupport.CodeValidationResult result;
		if (theValueSet != null) {
			result = myValidationSupportContext.getRootValidationSupport().validateCodeInValueSet(myValidationSupportContext, theValidationOptions, theSystem, theCode, theDisplay, theValueSet);
		} else {
			result = myValidationSupportContext.getRootValidationSupport().validateCode(myValidationSupportContext, theValidationOptions, theSystem, theCode, theDisplay, null);
		}
		return convertValidationResult(theSystem, result);
	}

	@Override
	public ValidationResult validateCode(ValidationOptions theOptions, org.hl7.fhir.r5.model.CodeableConcept code, org.hl7.fhir.r5.model.ValueSet theVs) {
		List<ValidationResult> validationResultsOk = new ArrayList<>();
		for (Coding next : code.getCoding()) {
			ValidationResult retVal = validateCode(theOptions, next, theVs);
			if (retVal.isOk()) {
				if (myValidationSupportContext.isEnabledValidationForCodingsLogicalAnd()) {
					validationResultsOk.add(retVal);
				} else {
					return retVal;
				}
			}
		}

		if (code.getCoding().size() > 0 && validationResultsOk.size() == code.getCoding().size()) {
			return validationResultsOk.get(0);
		}

		return new ValidationResult(ValidationMessage.IssueSeverity.ERROR, null);
	}

	public void invalidateCaches() {
		myFetchResourceCache.invalidateAll();
	}

	@Override
	public <T extends Resource> List<T> fetchResourcesByType(Class<T> theClass) {
		if (theClass.equals(StructureDefinition.class)) {
			return (List<T>) allStructures();
		}
		throw new UnsupportedOperationException(Msg.code(650) + "Unable to fetch resources of type: " + theClass);
	}

	private static class ResourceKey {
		private final int myHashCode;
		private final String myResourceName;
		private final String myUri;

		private ResourceKey(String theResourceName, String theUri) {
			myResourceName = theResourceName;
			myUri = theUri;
			myHashCode = new HashCodeBuilder(17, 37)
				.append(myResourceName)
				.append(myUri)
				.toHashCode();
		}

		@Override
		public boolean equals(Object theO) {
			if (this == theO) {
				return true;
			}

			if (theO == null || getClass() != theO.getClass()) {
				return false;
			}

			ResourceKey that = (ResourceKey) theO;

			return new EqualsBuilder()
				.append(myResourceName, that.myResourceName)
				.append(myUri, that.myUri)
				.isEquals();
		}

		public String getResourceName() {
			return myResourceName;
		}

		public String getUri() {
			return myUri;
		}

		@Override
		public int hashCode() {
			return myHashCode;
		}
	}

	public static ConceptValidationOptions convertConceptValidationOptions(ValidationOptions theOptions) {
		ConceptValidationOptions retVal = new ConceptValidationOptions();
		if (theOptions.isGuessSystem()) {
			retVal = retVal.setInferSystem(true);
		}
		return retVal;
	}

	@Nonnull
	public static VersionSpecificWorkerContextWrapper newVersionSpecificWorkerContextWrapper(IValidationSupport theValidationSupport) {
		VersionCanonicalizer versionCanonicalizer = new VersionCanonicalizer(theValidationSupport.getFhirContext());
		return new VersionSpecificWorkerContextWrapper(new ValidationSupportContext(theValidationSupport), versionCanonicalizer);
	}
}



