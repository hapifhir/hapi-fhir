package org.hl7.fhir.r5.hapi.validation;

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.validation.IValidationContext;
import ca.uhn.fhir.validation.IValidatorModule;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.time.DateUtils;
import org.fhir.ucum.UcumService;
import org.hl7.fhir.common.hapi.validation.ValidatorWrapper;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.PathEngineException;
import org.hl7.fhir.exceptions.TerminologyServiceException;
import org.hl7.fhir.r5.context.IWorkerContext;
import org.hl7.fhir.r5.formats.IParser;
import org.hl7.fhir.r5.formats.ParserType;
import org.hl7.fhir.r5.hapi.ctx.DefaultProfileValidationSupport;
import org.hl7.fhir.r5.hapi.ctx.HapiWorkerContext;
import org.hl7.fhir.r5.hapi.ctx.IValidationSupport;
import org.hl7.fhir.r5.model.*;
import org.hl7.fhir.r5.terminologies.ValueSetExpander;
import org.hl7.fhir.r5.terminologies.ValueSetExpander.ValueSetExpansionOutcome;
import org.hl7.fhir.r5.utils.FHIRPathEngine;
import org.hl7.fhir.r5.utils.INarrativeGenerator;
import org.hl7.fhir.r5.utils.IResourceValidator;
import org.hl7.fhir.r5.utils.IResourceValidator.BestPracticeWarningLevel;
import org.hl7.fhir.utilities.TerminologyServiceOptions;
import org.hl7.fhir.utilities.TranslationServices;
import org.hl7.fhir.utilities.validation.ValidationMessage;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueSeverity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.TimeUnit;

@SuppressWarnings({"PackageAccessibility", "Duplicates"})
public class FhirInstanceValidator extends org.hl7.fhir.r5.hapi.validation.BaseValidatorBridge implements IValidatorModule {

	private boolean myAnyExtensionsAllowed = true;
	private BestPracticeWarningLevel myBestPracticeWarningLevel;
	private IValidationSupport myValidationSupport;
	private boolean noTerminologyChecks = false;
	private volatile WorkerContextWrapper myWrappedWorkerContext;
	private boolean errorForUnknownProfiles;
	private List<String> myExtensionDomains = Collections.emptyList();

	/**
	 * Constructor
	 * <p>
	 * Uses {@link DefaultProfileValidationSupport} for {@link IValidationSupport validation support}
	 */
	public FhirInstanceValidator() {
		this(new DefaultProfileValidationSupport());
	}

	/**
	 * Constructor which uses the given validation support
	 *
	 * @param theValidationSupport The validation support
	 */
	public FhirInstanceValidator(IValidationSupport theValidationSupport) {
		myValidationSupport = theValidationSupport;
	}

	/**
	 * Every element in a resource or data type includes an optional <it>extension</it> child element
	 * which is identified by it's {@code url attribute}. There exists a number of predefined
	 * extension urls or extension domains:<ul>
	 * <li>any url which contains {@code example.org}, {@code nema.org}, or {@code acme.com}.</li>
	 * <li>any url which starts with {@code http://hl7.org/fhir/StructureDefinition/}.</li>
	 * </ul>
	 * It is possible to extend this list of known extension by defining custom extensions:
	 * Any url which starts which one of the elements in the list of custom extension domains is
	 * considered as known.
	 * <p>
	 * Any unknown extension domain will result in an information message when validating a resource.
	 * </p>
	 */
	public FhirInstanceValidator setCustomExtensionDomains(List<String> extensionDomains) {
		this.myExtensionDomains = extensionDomains;
		return this;
	}

	/**
	 * Every element in a resource or data type includes an optional <it>extension</it> child element
	 * which is identified by it's {@code url attribute}. There exists a number of predefined
	 * extension urls or extension domains:<ul>
	 * <li>any url which contains {@code example.org}, {@code nema.org}, or {@code acme.com}.</li>
	 * <li>any url which starts with {@code http://hl7.org/fhir/StructureDefinition/}.</li>
	 * </ul>
	 * It is possible to extend this list of known extension by defining custom extensions:
	 * Any url which starts which one of the elements in the list of custom extension domains is
	 * considered as known.
	 * <p>
	 * Any unknown extension domain will result in an information message when validating a resource.
	 * </p>
	 */
	public FhirInstanceValidator setCustomExtensionDomains(String... extensionDomains) {
		this.myExtensionDomains = Arrays.asList(extensionDomains);
		return this;
	}

	/**
	 * Returns the "best practice" warning level (default is {@link BestPracticeWarningLevel#Hint}).
	 * <p>
	 * The FHIR Instance Validator has a number of checks for best practices in terms of FHIR usage. If this setting is
	 * set to {@link BestPracticeWarningLevel#Error}, any resource data which does not meet these best practices will be
	 * reported at the ERROR level. If this setting is set to {@link BestPracticeWarningLevel#Ignore}, best practice
	 * guielines will be ignored.
	 * </p>
	 *
	 * @see #setBestPracticeWarningLevel(BestPracticeWarningLevel)
	 */
	public BestPracticeWarningLevel getBestPracticeWarningLevel() {
		return myBestPracticeWarningLevel;
	}

	/**
	 * Sets the "best practice warning level". When validating, any deviations from best practices will be reported at
	 * this level.
	 * <p>
	 * The FHIR Instance Validator has a number of checks for best practices in terms of FHIR usage. If this setting is
	 * set to {@link BestPracticeWarningLevel#Error}, any resource data which does not meet these best practices will be
	 * reported at the ERROR level. If this setting is set to {@link BestPracticeWarningLevel#Ignore}, best practice
	 * guielines will be ignored.
	 * </p>
	 *
	 * @param theBestPracticeWarningLevel The level, must not be <code>null</code>
	 */
	public void setBestPracticeWarningLevel(BestPracticeWarningLevel theBestPracticeWarningLevel) {
		Validate.notNull(theBestPracticeWarningLevel);
		myBestPracticeWarningLevel = theBestPracticeWarningLevel;
	}

	/**
	 * Returns the {@link IValidationSupport validation support} in use by this validator. Default is an instance of
	 * {@link DefaultProfileValidationSupport} if the no-arguments constructor for this object was used.
	 */
	public IValidationSupport getValidationSupport() {
		return myValidationSupport;
	}

	/**
	 * Sets the {@link IValidationSupport validation support} in use by this validator. Default is an instance of
	 * {@link DefaultProfileValidationSupport} if the no-arguments constructor for this object was used.
	 */
	public void setValidationSupport(IValidationSupport theValidationSupport) {
		myValidationSupport = theValidationSupport;
		myWrappedWorkerContext = null;
	}

	/**
	 * If set to {@literal true} (default is true) extensions which are not known to the
	 * validator (e.g. because they have not been explicitly declared in a profile) will
	 * be validated but will not cause an error.
	 */
	public boolean isAnyExtensionsAllowed() {
		return myAnyExtensionsAllowed;
	}

	/**
	 * If set to {@literal true} (default is true) extensions which are not known to the
	 * validator (e.g. because they have not been explicitly declared in a profile) will
	 * be validated but will not cause an error.
	 */
	public void setAnyExtensionsAllowed(boolean theAnyExtensionsAllowed) {
		myAnyExtensionsAllowed = theAnyExtensionsAllowed;
	}

	public boolean isErrorForUnknownProfiles() {
		return errorForUnknownProfiles;
	}

	public void setErrorForUnknownProfiles(boolean errorForUnknownProfiles) {
		this.errorForUnknownProfiles = errorForUnknownProfiles;
	}

	/**
	 * If set to {@literal true} (default is false) the valueSet will not be validate
	 */
	public boolean isNoTerminologyChecks() {
		return noTerminologyChecks;
	}

	/**
	 * If set to {@literal true} (default is false) the valueSet will not be validate
	 */
	public void setNoTerminologyChecks(final boolean theNoTerminologyChecks) {
		noTerminologyChecks = theNoTerminologyChecks;
	}

	public List<String> getExtensionDomains() {
		return myExtensionDomains;
	}

	@Override
	protected List<ValidationMessage> validate(IValidationContext<?> theValidationCtx) {
		WorkerContextWrapper wrappedWorkerContext = myWrappedWorkerContext;
		if (wrappedWorkerContext == null) {
			HapiWorkerContext workerContext = new HapiWorkerContext(theValidationCtx.getFhirContext(), myValidationSupport);
			wrappedWorkerContext = new WorkerContextWrapper(workerContext);
		}
		myWrappedWorkerContext = wrappedWorkerContext;

		return new ValidatorWrapper()
			.setAnyExtensionsAllowed(isAnyExtensionsAllowed())
			.setBestPracticeWarningLevel(getBestPracticeWarningLevel())
			.setErrorForUnknownProfiles(isErrorForUnknownProfiles())
			.setExtensionDomains(getExtensionDomains())
			.setNoTerminologyChecks(isNoTerminologyChecks())
			.validate(wrappedWorkerContext, theValidationCtx);
	}


	private static class WorkerContextWrapper implements IWorkerContext {
		private final HapiWorkerContext myWrap;
		private volatile List<org.hl7.fhir.r5.model.StructureDefinition> myAllStructures;
		private LoadingCache<ResourceKey, org.hl7.fhir.r5.model.Resource> myFetchResourceCache;
		private org.hl7.fhir.r5.model.Parameters myExpansionProfile;

		WorkerContextWrapper(HapiWorkerContext theWorkerContext) {
			myWrap = theWorkerContext;

			long timeoutMillis = 10 * DateUtils.MILLIS_PER_SECOND;
			if (System.getProperties().containsKey(Constants.TEST_SYSTEM_PROP_VALIDATION_RESOURCE_CACHES_MS)) {
				timeoutMillis = Long.parseLong(System.getProperty(Constants.TEST_SYSTEM_PROP_VALIDATION_RESOURCE_CACHES_MS));
			}

			myFetchResourceCache = Caffeine.newBuilder()
				.expireAfterWrite(timeoutMillis, TimeUnit.MILLISECONDS)
				.maximumSize(10000)
				.build(new CacheLoader<ResourceKey, org.hl7.fhir.r5.model.Resource>() {
					@Override
					public org.hl7.fhir.r5.model.Resource load(ResourceKey key) throws Exception {
						Resource fetched;
						switch (key.getResourceName()) {
							case "StructureDefinition":
								fetched = myWrap.fetchResource(StructureDefinition.class, key.getUri());
								break;
							case "ValueSet":
								fetched = myWrap.fetchResource(ValueSet.class, key.getUri());
								break;
							case "CodeSystem":
								fetched = myWrap.fetchResource(CodeSystem.class, key.getUri());
								break;
							case "Questionnaire":
								fetched = myWrap.fetchResource(Questionnaire.class, key.getUri());
								break;
							case "ImplementationGuide":
								fetched = myWrap.fetchResource(ImplementationGuide.class, key.getUri());
								break;
							default:
								throw new UnsupportedOperationException("Don't know how to fetch " + key.getResourceName());
						}

						if (fetched == null) {
							return null;
						}

						try {
							return fetched;
						} catch (FHIRException e) {
							throw new InternalErrorException(e);
						}
					}
				});
		}

		@Override
		public List<org.hl7.fhir.r5.model.MetadataResource> allConformanceResources() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void generateSnapshot(org.hl7.fhir.r5.model.StructureDefinition p) throws FHIRException {
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
		public List<org.hl7.fhir.r5.model.StructureDefinition> allStructures() {

			List<org.hl7.fhir.r5.model.StructureDefinition> retVal = myAllStructures;
			if (retVal == null) {
				retVal = new ArrayList<>();
				for (StructureDefinition next : myWrap.allStructures()) {
					try {
						retVal.add(next);
					} catch (FHIRException e) {
						throw new InternalErrorException(e);
					}
				}
				myAllStructures = retVal;
			}

			return retVal;
		}

		@Override
		public List<org.hl7.fhir.r5.model.StructureDefinition> getStructures() {
			return allStructures();
		}

		@Override
		public void cacheResource(org.hl7.fhir.r5.model.Resource res) {
			throw new UnsupportedOperationException();
		}

		@Nonnull
		private ValidationResult convertValidationResult(@Nullable org.hl7.fhir.r5.context.IWorkerContext.ValidationResult theResult) {
			ValidationResult retVal = null;
			if (theResult != null) {
				IssueSeverity issueSeverity = theResult.getSeverity();
				String message = theResult.getMessage();
				org.hl7.fhir.r5.model.CodeSystem.ConceptDefinitionComponent conceptDefinition = null;
				if (theResult.asConceptDefinition() != null) {
					try {
						conceptDefinition = (theResult.asConceptDefinition());
					} catch (FHIRException e) {
						throw new InternalErrorException(e);
					}
				}

				retVal = new ValidationResult(issueSeverity, message, conceptDefinition);
			}

			if (retVal == null) {
				retVal = new ValidationResult(IssueSeverity.ERROR, "Validation failed");
			}

			return retVal;
		}

		@Override
		public ValueSetExpansionOutcome expandVS(org.hl7.fhir.r5.model.ValueSet source, boolean cacheOk, boolean heiarchical) {
			ValueSet convertedSource;
			try {
				convertedSource = (source);
			} catch (FHIRException e) {
				throw new InternalErrorException(e);
			}
			ValueSetExpansionOutcome expanded = myWrap.expandVS(convertedSource, cacheOk, heiarchical);

			org.hl7.fhir.r5.model.ValueSet convertedResult = null;
			if (expanded.getValueset() != null) {
				try {
					convertedResult = (expanded.getValueset());
				} catch (FHIRException e) {
					throw new InternalErrorException(e);
				}
			}

			String error = expanded.getError();
			ValueSetExpander.TerminologyServiceErrorClass result = null;

			return new ValueSetExpansionOutcome(convertedResult, error, result);
		}

		@Override
		public ValueSetExpansionOutcome expandVS(org.hl7.fhir.r5.model.ElementDefinition.ElementDefinitionBindingComponent binding, boolean cacheOk, boolean heiarchical) {
			throw new UnsupportedOperationException();
		}

		@Override
		public ValueSetExpansionOutcome expandVS(org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent inc, boolean heirarchical) throws TerminologyServiceException {
			ValueSet.ConceptSetComponent convertedInc = null;
			if (inc != null) {
				try {
					convertedInc = (inc);
				} catch (FHIRException e) {
					throw new InternalErrorException(e);
				}
			}

			ValueSetExpansionOutcome expansion = myWrap.expandVS(convertedInc, heirarchical);
			org.hl7.fhir.r5.model.ValueSet valueSetExpansion = null;
			if (expansion != null) {
				try {
					valueSetExpansion = (expansion.getValueset());
				} catch (FHIRException e) {
					throw new InternalErrorException(e);
				}
			}

			ValueSetExpansionOutcome outcome = new ValueSetExpansionOutcome(valueSetExpansion);
			return outcome;
		}

		@Override
		public org.hl7.fhir.r5.model.CodeSystem fetchCodeSystem(String system) {
			CodeSystem fetched = myWrap.fetchCodeSystem(system);
			if (fetched == null) {
				return null;
			}
			try {
				return (fetched);
			} catch (FHIRException e) {
				throw new InternalErrorException(e);
			}
		}

		@Override
		public <T extends org.hl7.fhir.r5.model.Resource> T fetchResource(Class<T> class_, String uri) {

			ResourceKey key = new ResourceKey(class_.getSimpleName(), uri);
			@SuppressWarnings("unchecked")
			T retVal = (T) myFetchResourceCache.get(key);

			return retVal;
		}

		@Override
		public org.hl7.fhir.r5.model.Resource fetchResourceById(String type, String uri) {
			throw new UnsupportedOperationException();
		}

		@Override
		public <T extends org.hl7.fhir.r5.model.Resource> T fetchResourceWithException(Class<T> class_, String uri) throws FHIRException {
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
			return myWrap.getAbbreviation(name);
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
			return myWrap.getResourceNames();
		}

		@Override
		public Set<String> getResourceNamesAsSet() {
			return new HashSet<>(myWrap.getResourceNames());
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
		public org.hl7.fhir.r5.model.StructureDefinition fetchTypeDefinition(String typeName) {
			return fetchResource(org.hl7.fhir.r5.model.StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/" + typeName);
		}

		@Override
		public List<String> getTypeNames() {
			return myWrap.getTypeNames();
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
			return myWrap.getVersion();
		}

		@Override
		public boolean hasCache() {
			return myWrap.hasCache();
		}

		@Override
		public <T extends org.hl7.fhir.r5.model.Resource> boolean hasResource(Class<T> class_, String uri) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isNoTerminologyServer() {
			return myWrap.isNoTerminologyServer();
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
			return myWrap.oid2Uri(code);
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
			return myWrap.supportsSystem(system);
		}

		@Override
		public TranslationServices translator() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Set<String> typeTails() {
			return myWrap.typeTails();
		}

		@Override
		public ValidationResult validateCode(TerminologyServiceOptions theOptions, String system, String code, String display) {
			org.hl7.fhir.r5.context.IWorkerContext.ValidationResult result = myWrap.validateCode(theOptions, system, code, display);
			return convertValidationResult(result);
		}

		@Override
		public ValidationResult validateCode(TerminologyServiceOptions theOptions, String system, String code, String display, org.hl7.fhir.r5.model.ValueSet vs) {
			ValueSet convertedVs = null;

			try {
				if (vs != null) {
					convertedVs = (vs);
				}
			} catch (FHIRException e) {
				throw new InternalErrorException(e);
			}

			org.hl7.fhir.r5.context.IWorkerContext.ValidationResult result = myWrap.validateCode(theOptions, system, code, display, convertedVs);
			return convertValidationResult(result);
		}

		@Override
		public ValidationResult validateCode(TerminologyServiceOptions theOptions, String code, org.hl7.fhir.r5.model.ValueSet vs) {
			ValueSet convertedVs = null;
			try {
				if (vs != null) {
					convertedVs = (vs);
				}
			} catch (FHIRException e) {
				throw new InternalErrorException(e);
			}

			org.hl7.fhir.r5.context.IWorkerContext.ValidationResult result = myWrap.validateCode(theOptions, null, code, null, convertedVs);
			return convertValidationResult(result);
		}

		@Override
		public ValidationResult validateCode(TerminologyServiceOptions theOptions, org.hl7.fhir.r5.model.Coding code, org.hl7.fhir.r5.model.ValueSet vs) {
			Coding convertedCode = null;
			ValueSet convertedVs = null;

			try {
				if (code != null) {
					convertedCode = (code);
				}
				if (vs != null) {
					convertedVs = (vs);
				}
			} catch (FHIRException e) {
				throw new InternalErrorException(e);
			}

			org.hl7.fhir.r5.context.IWorkerContext.ValidationResult result = myWrap.validateCode(theOptions, convertedCode, convertedVs);
			return convertValidationResult(result);
		}

		@Override
		public ValidationResult validateCode(TerminologyServiceOptions theOptions, org.hl7.fhir.r5.model.CodeableConcept code, org.hl7.fhir.r5.model.ValueSet vs) {
			CodeableConcept convertedCode = null;
			ValueSet convertedVs = null;

			try {
				if (code != null) {
					convertedCode = (code);
				}
				if (vs != null) {
					convertedVs = (vs);
				}
			} catch (FHIRException e) {
				throw new InternalErrorException(e);
			}

			org.hl7.fhir.r5.context.IWorkerContext.ValidationResult result = myWrap.validateCode(theOptions, convertedCode, convertedVs);
			return convertValidationResult(result);
		}

		@Override
		public ValidationResult validateCode(TerminologyServiceOptions theOptions, String system, String code, String display, org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent vsi) {
			ValueSet.ConceptSetComponent conceptSetComponent = null;
			if (vsi != null) {
				try {
					conceptSetComponent = vsi;
				} catch (FHIRException e) {
					throw new InternalErrorException(e);
				}
			}

			org.hl7.fhir.r5.context.IWorkerContext.ValidationResult result = myWrap.validateCode(theOptions, system, code, display, conceptSetComponent);
			return convertValidationResult(result);
		}

	}

	public static class NullEvaluationContext implements FHIRPathEngine.IEvaluationContext {
		@Override
		public Base resolveConstant(Object appContext, String name, boolean beforeContext) throws PathEngineException {
			return null;
		}

		@Override
		public TypeDetails resolveConstantType(Object appContext, String name) throws PathEngineException {
			return null;
		}

		@Override
		public boolean log(String argument, List<Base> focus) {
			return false;
		}

		@Override
		public FunctionDetails resolveFunction(String functionName) {
			return null;
		}

		@Override
		public TypeDetails checkFunction(Object appContext, String functionName, List<TypeDetails> parameters) throws PathEngineException {
			return null;
		}

		@Override
		public List<Base> executeFunction(Object appContext, String functionName, List<List<Base>> parameters) {
			return null;
		}

		@Override
		public Base resolveReference(Object appContext, String url) throws FHIRException {
			return null;
		}

		@Override
		public boolean conformsToProfile(Object appContext, Base item, String url) throws FHIRException {
			return false;
		}

		@Override
		public ValueSet resolveValueSet(Object appContext, String url) {
			return null;
		}
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
}
