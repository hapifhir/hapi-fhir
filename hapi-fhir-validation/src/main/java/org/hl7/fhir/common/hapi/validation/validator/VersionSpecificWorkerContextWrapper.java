package org.hl7.fhir.common.hapi.validation.validator;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.time.DateUtils;
import org.fhir.ucum.UcumService;
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
import org.hl7.fhir.r5.utils.INarrativeGenerator;
import org.hl7.fhir.r5.utils.IResourceValidator;
import org.hl7.fhir.utilities.TranslationServices;
import org.hl7.fhir.utilities.i18n.I18nBase;
import org.hl7.fhir.utilities.validation.ValidationMessage;
import org.hl7.fhir.utilities.validation.ValidationOptions;

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

class VersionSpecificWorkerContextWrapper extends I18nBase implements IWorkerContext {
	public static final IVersionTypeConverter IDENTITY_VERSION_TYPE_CONVERTER = new VersionTypeConverterR5();
	private static FhirContext ourR5Context = FhirContext.forR5();
	private final IValidationSupport myValidationSupport;
	private final IVersionTypeConverter myModelConverter;
	private volatile List<StructureDefinition> myAllStructures;
	private LoadingCache<ResourceKey, IBaseResource> myFetchResourceCache;
	private org.hl7.fhir.r5.model.Parameters myExpansionProfile;

	public VersionSpecificWorkerContextWrapper(IValidationSupport theValidationSupport, IVersionTypeConverter theModelConverter) {
		myValidationSupport = theValidationSupport;
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
				if (myValidationSupport.getFhirContext().getVersion().getVersion() == FhirVersionEnum.DSTU2) {
					if ("CodeSystem".equals(fetchResourceName)) {
						fetchResourceName = "ValueSet";
					}
				}
				Class<? extends IBaseResource> fetchResourceType = myValidationSupport.getFhirContext().getResourceDefinition(fetchResourceName).getImplementingClass();
				IBaseResource fetched = myValidationSupport.fetchResource(fetchResourceType, key.getUri());

				if (fetched == null) {
					return null;
				}


				Resource canonical = myModelConverter.toCanonical(fetched);

				if (canonical instanceof StructureDefinition) {
					if (((StructureDefinition) canonical).getSnapshot().isEmpty()) {
						fetched = myValidationSupport.generateSnapshot(myValidationSupport, fetched, "", null, "");
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
	public void generateSnapshot(StructureDefinition input) throws FHIRException {
		if (input.hasSnapshot()) {
			return;
		}

		org.hl7.fhir.r5.conformance.ProfileUtilities.ProfileKnowledgeProvider profileKnowledgeProvider = new ProfileKnowledgeWorkerR5(ourR5Context);
		ArrayList<ValidationMessage> messages = new ArrayList<>();
		org.hl7.fhir.r5.model.StructureDefinition base = (org.hl7.fhir.r5.model.StructureDefinition) fetchResource(StructureDefinition.class, input.getBaseDefinition());
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
			for (IBaseResource next : myValidationSupport.fetchAllStructureDefinitions()) {
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
		IValidationSupport.ValueSetExpansionOutcome expanded = myValidationSupport.expandValueSet(myValidationSupport, null, convertedSource);

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
		return myValidationSupport.getFhirContext().getLocalizer().getLocale();
	}

	@Override
	public void setLocale(Locale locale) {
		// ignore
	}

	@Override
	public org.hl7.fhir.r5.model.CodeSystem fetchCodeSystem(String system) {
		IBaseResource fetched = myValidationSupport.fetchCodeSystem(system);
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
	public List<org.hl7.fhir.r5.model.ConceptMap> findMapsForSource(String url) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getAbbreviation(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public INarrativeGenerator getNarrativeGenerator(String prefix, String basePath) {
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
		return new ArrayList<>(myValidationSupport.getFhirContext().getResourceNames());
	}

	@Override
	public Set<String> getResourceNamesAsSet() {
		return myValidationSupport.getFhirContext().getResourceNames();
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
		return myValidationSupport.getFhirContext().getVersion().getVersion().getFhirVersionString();
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
		return myValidationSupport.isCodeSystemSupported(myValidationSupport, system);
	}

	@Override
	public TranslationServices translator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ValidationResult validateCode(ValidationOptions theOptions, String system, String code, String display) {
		IValidationSupport.CodeValidationResult result = myValidationSupport.validateCode(myValidationSupport, convertConceptValidationOptions(theOptions), system, code, display, null);
		return convertValidationResult(result);
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

		IValidationSupport.CodeValidationResult result = myValidationSupport.validateCodeInValueSet(myValidationSupport, convertConceptValidationOptions(theOptions), theSystem, theCode, display, convertedVs);
		return convertValidationResult(result);
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

		IValidationSupport.CodeValidationResult result = myValidationSupport.validateCodeInValueSet(myValidationSupport, convertConceptValidationOptions(theOptions).setInferSystem(true), null, code, null, convertedVs);
		return convertValidationResult(result);
	}

	@Override
	public ValidationResult validateCode(ValidationOptions theOptions, org.hl7.fhir.r5.model.Coding code, org.hl7.fhir.r5.model.ValueSet theValueSet) {
		IBaseResource convertedVs = null;

		try {
			if (theValueSet != null) {
				convertedVs = myModelConverter.fromCanonical(theValueSet);
			}
		} catch (FHIRException e) {
			throw new InternalErrorException(e);
		}

		IValidationSupport.CodeValidationResult result = myValidationSupport.validateCodeInValueSet(myValidationSupport, convertConceptValidationOptions(theOptions), code.getSystem(), code.getCode(), code.getDisplay(), convertedVs);
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
		private String myResourceName;
		private String myUri;

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

}



