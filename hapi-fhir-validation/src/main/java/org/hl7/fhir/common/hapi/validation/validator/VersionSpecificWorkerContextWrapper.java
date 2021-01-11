package org.hl7.fhir.common.hapi.validation.validator;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.time.DateUtils;
import org.fhir.ucum.UcumService;
import org.hl7.fhir.convertors.VersionConvertor_10_50;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.TerminologyServiceException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.context.IWorkerContext;
import org.hl7.fhir.r5.formats.IParser;
import org.hl7.fhir.r5.formats.ParserType;
import org.hl7.fhir.r5.model.CanonicalResource;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.Resource;
import org.hl7.fhir.r5.model.StructureDefinition;
import org.hl7.fhir.r5.model.ValueSet;
import org.hl7.fhir.r5.terminologies.ValueSetExpander;
import org.hl7.fhir.r5.utils.IResourceValidator;
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
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class VersionSpecificWorkerContextWrapper extends I18nBase implements IWorkerContext {
	public static final IVersionTypeConverter IDENTITY_VERSION_TYPE_CONVERTER = new VersionTypeConverterR5();
	private static final Logger ourLog = LoggerFactory.getLogger(VersionSpecificWorkerContextWrapper.class);
	private static final FhirContext ourR5Context = FhirContext.forR5();
	private final ValidationSupportContext myValidationSupportContext;
	private final IVersionTypeConverter myModelConverter;
	private final LoadingCache<ResourceKey, IBaseResource> myFetchResourceCache;
	private volatile List<StructureDefinition> myAllStructures;
	private org.hl7.fhir.r5.model.Parameters myExpansionProfile;

	public VersionSpecificWorkerContextWrapper(ValidationSupportContext theValidationSupportContext, IVersionTypeConverter theModelConverter) {
		myValidationSupportContext = theValidationSupportContext;
		myModelConverter = theModelConverter;

		long timeoutMillis = 10 * DateUtils.MILLIS_PER_SECOND;
		if (System.getProperties().containsKey(ca.uhn.fhir.rest.api.Constants.TEST_SYSTEM_PROP_VALIDATION_RESOURCE_CACHES_MS)) {
			timeoutMillis = Long.parseLong(System.getProperty(Constants.TEST_SYSTEM_PROP_VALIDATION_RESOURCE_CACHES_MS));
		}

		myFetchResourceCache = Caffeine.newBuilder()
			.expireAfterWrite(timeoutMillis, TimeUnit.MILLISECONDS)
			.maximumSize(10000)
			.build(key -> {

				String fetchResourceName = key.getResourceName();
				if (myValidationSupportContext.getRootValidationSupport().getFhirContext().getVersion().getVersion() == FhirVersionEnum.DSTU2) {
					if ("CodeSystem".equals(fetchResourceName)) {
						fetchResourceName = "ValueSet";
					}
				}
				Class<? extends IBaseResource> fetchResourceType = myValidationSupportContext.getRootValidationSupport().getFhirContext().getResourceDefinition(fetchResourceName).getImplementingClass();
				IBaseResource fetched = myValidationSupportContext.getRootValidationSupport().fetchResource(fetchResourceType, key.getUri());

				if (fetched == null) {
					return null;
				}


				Resource canonical = myModelConverter.toCanonical(fetched);

				if (canonical instanceof StructureDefinition) {
					StructureDefinition canonicalSd = (StructureDefinition) canonical;
					if (canonicalSd.getSnapshot().isEmpty()) {
						ourLog.info("Generating snapshot for StructureDefinition: {}", canonicalSd.getUrl());
						fetched = myValidationSupportContext.getRootValidationSupport().generateSnapshot(theValidationSupportContext, fetched, "", null, "");
						Validate.isTrue(fetched != null, "StructureDefinition %s has no snapshot, and no snapshot generator is configured", key.getUri());
						canonical = myModelConverter.toCanonical(fetched);
					}
				}

				return canonical;
			});

		setValidationMessageLanguage(getLocale());
	}

	@Override
	public List<CanonicalResource> allConformanceResources() {
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

	@Override
	public int loadFromPackage(NpmPackage pi, IContextResourceLoader loader) throws FHIRException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int loadFromPackage(NpmPackage pi, IContextResourceLoader loader, String[] types) throws FHIRException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int loadFromPackageAndDependencies(NpmPackage pi, IContextResourceLoader loader, BasePackageCacheManager pcm) throws FHIRException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean hasPackage(String id, String ver) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getClientRetryCount() {
		throw new UnsupportedOperationException();
	}

	@Override
	public IWorkerContext setClientRetryCount(int value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public TimeTracker clock() {
		return null;
	}

	@Override
	public void generateSnapshot(StructureDefinition input) throws FHIRException {
		if (input.hasSnapshot()) {
			return;
		}

		org.hl7.fhir.r5.conformance.ProfileUtilities.ProfileKnowledgeProvider profileKnowledgeProvider = new ProfileKnowledgeWorkerR5(ourR5Context);
		ArrayList<ValidationMessage> messages = new ArrayList<>();
		org.hl7.fhir.r5.model.StructureDefinition base = fetchResource(StructureDefinition.class, input.getBaseDefinition());
		if (base == null) {
			throw new PreconditionFailedException("Unknown base definition: " + input.getBaseDefinition());
		}
		new org.hl7.fhir.r5.conformance.ProfileUtilities(this, messages, profileKnowledgeProvider).generateSnapshot(base, input, "", null, "");

	}

	@Override
	public void generateSnapshot(StructureDefinition theStructureDefinition, boolean theB) {
		// nothing yet
	}

	@Override
	public org.hl7.fhir.r5.model.Parameters getExpansionParameters() {
		return myExpansionProfile;
	}

	@Override
	public void setExpansionProfile(org.hl7.fhir.r5.model.Parameters expParameters) {
		myExpansionProfile = expParameters;
	}

	@Override
	public List<StructureDefinition> allStructures() {

		List<StructureDefinition> retVal = myAllStructures;
		if (retVal == null) {
			retVal = new ArrayList<>();
			for (IBaseResource next : myValidationSupportContext.getRootValidationSupport().fetchAllStructureDefinitions()) {
				try {
					Resource converted = myModelConverter.toCanonical(next);
					retVal.add((StructureDefinition) converted);
				} catch (FHIRException e) {
					throw new InternalErrorException(e);
				}
			}
			myAllStructures = retVal;
		}

		return retVal;
	}

	@Override
	public List<StructureDefinition> getStructures() {
		return allStructures();
	}

	@Override
	public void cacheResource(Resource res) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void cacheResourceFromPackage(Resource res, PackageVersion packageDetails) throws FHIRException {

	}

	@Override
	public void cachePackage(PackageVersion packageDetails, List<PackageVersion> dependencies) {

	}

	@Nonnull
	private ValidationResult convertValidationResult(@Nullable IValidationSupport.CodeValidationResult theResult) {
		ValidationResult retVal = null;
		if (theResult != null) {
			String code = theResult.getCode();
			String display = theResult.getDisplay();
			String issueSeverity = theResult.getSeverityCode();
			String message = theResult.getMessage();
			if (isNotBlank(code)) {
				retVal = new ValidationResult(new org.hl7.fhir.r5.model.CodeSystem.ConceptDefinitionComponent()
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
			convertedSource = myModelConverter.fromCanonical(source);
		} catch (FHIRException e) {
			throw new InternalErrorException(e);
		}
		IValidationSupport.ValueSetExpansionOutcome expanded = myValidationSupportContext.getRootValidationSupport().expandValueSet(myValidationSupportContext, null, convertedSource);

		org.hl7.fhir.r5.model.ValueSet convertedResult = null;
		if (expanded.getValueSet() != null) {
			try {
				convertedResult = (ValueSet) myModelConverter.toCanonical(expanded.getValueSet());
			} catch (FHIRException e) {
				throw new InternalErrorException(e);
			}
		}

		String error = expanded.getError();
		ValueSetExpander.TerminologyServiceErrorClass result = null;

		return new ValueSetExpander.ValueSetExpansionOutcome(convertedResult, error, result);
	}

	@Override
	public ValueSetExpander.ValueSetExpansionOutcome expandVS(org.hl7.fhir.r5.model.ElementDefinition.ElementDefinitionBindingComponent binding, boolean cacheOk, boolean Hierarchical) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ValueSetExpander.ValueSetExpansionOutcome expandVS(org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent inc, boolean heirarchical) throws TerminologyServiceException {
		throw new UnsupportedOperationException();
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
			return (org.hl7.fhir.r5.model.CodeSystem) myModelConverter.toCanonical(fetched);
		} catch (FHIRException e) {
			throw new InternalErrorException(e);
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
		throw new UnsupportedOperationException();
	}

	@Override
	public <T extends Resource> T fetchResourceWithException(Class<T> class_, String uri) throws FHIRException {
		T retVal = fetchResource(class_, uri);
		if (retVal == null) {
			throw new FHIRException("Can not find resource of type " + class_.getSimpleName() + " with uri " + uri);
		}
		return retVal;
	}

	@Override
	public <T extends Resource> T fetchResource(Class<T> class_, String uri, CanonicalResource canonicalForSource) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<org.hl7.fhir.r5.model.ConceptMap> findMapsForSource(String url) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getAbbreviation(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IParser getParser(ParserType type) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IParser getParser(String type) {
		throw new UnsupportedOperationException();
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
	public org.hl7.fhir.r5.model.StructureMap getTransform(String url) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getOverrideVersionNs() {
		return null;
	}

	@Override
	public void setOverrideVersionNs(String value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public StructureDefinition fetchTypeDefinition(String typeName) {
		return fetchResource(StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/" + typeName);
	}

	@Override
	public StructureDefinition fetchRawProfile(String url) {
		StructureDefinition retVal = fetchResource(StructureDefinition.class, url);

		if (retVal != null && retVal.getSnapshot().isEmpty()) {
			generateSnapshot(retVal);
		}

		return retVal;
	}

	@Override
	public List<String> getTypeNames() {
		throw new UnsupportedOperationException();
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
	public String getVersion() {
		return myValidationSupportContext.getRootValidationSupport().getFhirContext().getVersion().getVersion().getFhirVersionString();
	}

	@Override
	public boolean hasCache() {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T extends Resource> boolean hasResource(Class<T> class_, String uri) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isNoTerminologyServer() {
		return false;
	}

	@Override
	public List<org.hl7.fhir.r5.model.StructureMap> listTransforms() {
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
	public String oid2Uri(String code) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ILoggingService getLogger() {
		return null;
	}

	@Override
	public void setLogger(ILoggingService logger) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsSystem(String system) {
		return myValidationSupportContext.getRootValidationSupport().isCodeSystemSupported(myValidationSupportContext, system);
	}

	@Override
	public TranslationServices translator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ValidationResult validateCode(ValidationOptions theOptions, String system, String code, String display) {
		ConceptValidationOptions validationOptions = convertConceptValidationOptions(theOptions);

		return doValidation(null, validationOptions, system, code, display);
	}

	@Override
	public ValidationResult validateCode(ValidationOptions theOptions, String theSystem, String theCode, String display, org.hl7.fhir.r5.model.ValueSet theValueSet) {
		IBaseResource convertedVs = null;

		try {
			if (theValueSet != null) {
				convertedVs = myModelConverter.fromCanonical(theValueSet);
			}
		} catch (FHIRException e) {
			throw new InternalErrorException(e);
		}

		ConceptValidationOptions validationOptions = convertConceptValidationOptions(theOptions);

		return doValidation(convertedVs, validationOptions, theSystem, theCode, display);
	}

	@Override
	public ValidationResult validateCode(ValidationOptions theOptions, String code, org.hl7.fhir.r5.model.ValueSet theValueSet) {
		IBaseResource convertedVs = null;
		try {
			if (theValueSet != null) {
				convertedVs = myModelConverter.fromCanonical(theValueSet);
			}
		} catch (FHIRException e) {
			throw new InternalErrorException(e);
		}

		ConceptValidationOptions validationOptions = convertConceptValidationOptions(theOptions).setInferSystem(true);

		return doValidation(convertedVs, validationOptions, null, code, null);
	}

	@Override
	public ValidationResult validateCode(ValidationOptions theOptions, org.hl7.fhir.r5.model.Coding theCoding, org.hl7.fhir.r5.model.ValueSet theValueSet) {
		IBaseResource convertedVs = null;

		try {
			if (theValueSet != null) {
				convertedVs = myModelConverter.fromCanonical(theValueSet);
			}
		} catch (FHIRException e) {
			throw new InternalErrorException(e);
		}

		ConceptValidationOptions validationOptions = convertConceptValidationOptions(theOptions);
		String system = theCoding.getSystem();
		String code = theCoding.getCode();
		String display = theCoding.getDisplay();

		return doValidation(convertedVs, validationOptions, system, code, display);
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
		return convertValidationResult(result);
	}

	@Override
	public ValidationResult validateCode(ValidationOptions theOptions, org.hl7.fhir.r5.model.CodeableConcept code, org.hl7.fhir.r5.model.ValueSet theVs) {
		for (Coding next : code.getCoding()) {
			ValidationResult retVal = validateCode(theOptions, next, theVs);
			if (retVal.isOk()) {
				return retVal;
			}
		}

		return new ValidationResult(ValidationMessage.IssueSeverity.ERROR, null);
	}

	public void invalidateCaches() {
		myFetchResourceCache.invalidateAll();
	}

	public interface IVersionTypeConverter {

		org.hl7.fhir.r5.model.Resource toCanonical(IBaseResource theNonCanonical);

		IBaseResource fromCanonical(org.hl7.fhir.r5.model.Resource theCanonical);

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

	private static class VersionTypeConverterR5 implements IVersionTypeConverter {
		@Override
		public Resource toCanonical(IBaseResource theNonCanonical) {
			return (Resource) theNonCanonical;
		}

		@Override
		public IBaseResource fromCanonical(Resource theCanonical) {
			return theCanonical;
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
		 IVersionTypeConverter converter;

        switch (theValidationSupport.getFhirContext().getVersion().getVersion()) {
            case DSTU2:
            case DSTU2_HL7ORG: {
                converter = new IVersionTypeConverter() {
                    @Override
                    public Resource toCanonical(IBaseResource theNonCanonical) {
							  Resource retVal = VersionConvertor_10_50.convertResource((org.hl7.fhir.dstu2.model.Resource) theNonCanonical);
                        if (theNonCanonical instanceof org.hl7.fhir.dstu2.model.ValueSet) {
                            org.hl7.fhir.dstu2.model.ValueSet valueSet = (org.hl7.fhir.dstu2.model.ValueSet) theNonCanonical;
                            if (valueSet.hasCodeSystem() && valueSet.getCodeSystem().hasSystem()) {
                                if (!valueSet.hasCompose()) {
                                    ValueSet valueSetR5 = (ValueSet) retVal;
                                    valueSetR5.getCompose().addInclude().setSystem(valueSet.getCodeSystem().getSystem());
                                }
                            }
                        }
                        return retVal;
                    }

                    @Override
                    public IBaseResource fromCanonical(Resource theCanonical) {
							  return VersionConvertor_10_50.convertResource(theCanonical);
                    }
                };
                break;
            }

            case DSTU2_1: {
                converter = new VersionTypeConverterDstu21();
                break;
            }

            case DSTU3: {
                converter = new VersionTypeConverterDstu3();
                break;
            }

            case R4: {
                converter = new VersionTypeConverterR4();
                break;
            }

            case R5: {
                converter = IDENTITY_VERSION_TYPE_CONVERTER;
                break;
            }

            default:
                throw new IllegalStateException();
        }

		 return new VersionSpecificWorkerContextWrapper(new ValidationSupportContext(theValidationSupport), converter);
    }
}



