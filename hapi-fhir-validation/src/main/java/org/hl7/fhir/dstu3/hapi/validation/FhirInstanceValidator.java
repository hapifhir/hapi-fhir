package org.hl7.fhir.dstu3.hapi.validation;

import ca.uhn.fhir.context.FhirContext;
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
import org.hl7.fhir.convertors.VersionConvertor_30_50;
import org.hl7.fhir.dstu3.hapi.ctx.DefaultProfileValidationSupport;
import org.hl7.fhir.dstu3.hapi.ctx.HapiWorkerContext;
import org.hl7.fhir.dstu3.hapi.ctx.IValidationSupport;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.TerminologyServiceException;
import org.hl7.fhir.r5.context.IWorkerContext;
import org.hl7.fhir.r5.formats.IParser;
import org.hl7.fhir.r5.formats.ParserType;
import org.hl7.fhir.r5.terminologies.ValueSetExpander;
import org.hl7.fhir.r5.utils.INarrativeGenerator;
import org.hl7.fhir.r5.utils.IResourceValidator;
import org.hl7.fhir.r5.utils.IResourceValidator.BestPracticeWarningLevel;
import org.hl7.fhir.utilities.TerminologyServiceOptions;
import org.hl7.fhir.utilities.TranslationServices;
import org.hl7.fhir.utilities.validation.ValidationMessage;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueSeverity;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;

@SuppressWarnings({"PackageAccessibility", "Duplicates"})
public class FhirInstanceValidator extends BaseValidatorBridge implements IValidatorModule {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirInstanceValidator.class);

	private boolean myAnyExtensionsAllowed = true;
	private BestPracticeWarningLevel myBestPracticeWarningLevel;
	private DocumentBuilderFactory myDocBuilderFactory;
	private StructureDefinition myStructureDefintion;
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
		myDocBuilderFactory = DocumentBuilderFactory.newInstance();
		myDocBuilderFactory.setNamespaceAware(true);
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

	private String determineResourceName(Document theDocument) {
		NodeList list = theDocument.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i) instanceof Element) {
				return list.item(i).getLocalName();
			}
		}
		return theDocument.getDocumentElement().getLocalName();
	}

	private ArrayList<String> determineIfProfilesSpecified(Document theDocument) {
		ArrayList<String> profileNames = new ArrayList<String>();
		NodeList list = theDocument.getChildNodes().item(0).getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i).getNodeName().compareToIgnoreCase("meta") == 0) {
				NodeList metaList = list.item(i).getChildNodes();
				for (int j = 0; j < metaList.getLength(); j++) {
					if (metaList.item(j).getNodeName().compareToIgnoreCase("profile") == 0) {
						profileNames.add(metaList.item(j).getAttributes().item(0).getNodeValue());
					}
				}
				break;
			}
		}
		return profileNames;
	}

	private StructureDefinition findStructureDefinitionForResourceName(final FhirContext theCtx, String resourceName) {
		String sdName = null;
		try {
			// Test if a URL was passed in specifying the structure definition and test if "StructureDefinition" is part of the URL
			URL testIfUrl = new URL(resourceName);
			sdName = resourceName;
		} catch (MalformedURLException e) {
			sdName = "http://hl7.org/fhir/StructureDefinition/" + resourceName;
		}
		StructureDefinition profile = myStructureDefintion != null ? myStructureDefintion : myValidationSupport.fetchStructureDefinition(theCtx, sdName);
		return profile;
	}

	public void flushCaches() {
		myWrappedWorkerContext = null;
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

	public void setStructureDefintion(StructureDefinition theStructureDefintion) {
		myStructureDefintion = theStructureDefintion;
	}

	private List<String> getExtensionDomains() {
		return myExtensionDomains;
	}

	@Override
	protected List<ValidationMessage> validate(IValidationContext<?> theValidationCtx) {
		final FhirContext ctx = theValidationCtx.getFhirContext();

		WorkerContextWrapper wrappedWorkerContext = myWrappedWorkerContext;
		if (wrappedWorkerContext == null) {
			HapiWorkerContext workerContext = new HapiWorkerContext(ctx, myValidationSupport);
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
		private final VersionConvertor_30_50 myConverter;
		private volatile List<org.hl7.fhir.r5.model.StructureDefinition> myAllStructures;
		private LoadingCache<ResourceKey, org.hl7.fhir.r5.model.Resource> myFetchResourceCache;
		private org.hl7.fhir.r5.model.Parameters myExpansionProfile;

		WorkerContextWrapper(HapiWorkerContext theWorkerContext) {
			myWrap = theWorkerContext;
			myConverter = new VersionConvertor_30_50();

			long timeoutMillis = 10 * DateUtils.MILLIS_PER_SECOND;
			if (System.getProperties().containsKey(ca.uhn.fhir.rest.api.Constants.TEST_SYSTEM_PROP_VALIDATION_RESOURCE_CACHES_MS)) {
				timeoutMillis = Long.parseLong(System.getProperty(Constants.TEST_SYSTEM_PROP_VALIDATION_RESOURCE_CACHES_MS));
			}

			myFetchResourceCache = Caffeine.newBuilder()
				.expireAfterWrite(timeoutMillis, TimeUnit.MILLISECONDS)
				.maximumSize(10000)
				.build(key -> {
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
						return VersionConvertor_30_50.convertResource(fetched, true);
					} catch (FHIRException e) {
						throw new InternalErrorException(e);
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
						retVal.add(VersionConvertor_30_50.convertStructureDefinition(next));
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
		private ValidationResult convertValidationResult(@Nullable  org.hl7.fhir.dstu3.context.IWorkerContext.ValidationResult theResult) {
			ValidationResult retVal = null;
			if (theResult != null) {
				IssueSeverity issueSeverity = theResult.getSeverity();
				String message = theResult.getMessage();
				org.hl7.fhir.r5.model.CodeSystem.ConceptDefinitionComponent conceptDefinition = null;
				if (theResult.asConceptDefinition() != null) {
					try {
						conceptDefinition = VersionConvertor_30_50.convertConceptDefinitionComponent(theResult.asConceptDefinition());
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
		public ValueSetExpander.ValueSetExpansionOutcome expandVS(org.hl7.fhir.r5.model.ValueSet source, boolean cacheOk, boolean heiarchical) {
			ValueSet convertedSource;
			try {
				convertedSource = VersionConvertor_30_50.convertValueSet(source);
			} catch (FHIRException e) {
				throw new InternalErrorException(e);
			}
			org.hl7.fhir.dstu3.terminologies.ValueSetExpander.ValueSetExpansionOutcome expanded = myWrap.expandVS(convertedSource, cacheOk, heiarchical);

			org.hl7.fhir.r5.model.ValueSet convertedResult = null;
			if (expanded.getValueset() != null) {
				try {
					convertedResult = VersionConvertor_30_50.convertValueSet(expanded.getValueset());
				} catch (FHIRException e) {
					throw new InternalErrorException(e);
				}
			}

			String error = expanded.getError();
			ValueSetExpander.TerminologyServiceErrorClass result = null;

			return new ValueSetExpander.ValueSetExpansionOutcome(convertedResult, error, result);
		}

		@Override
		public ValueSetExpander.ValueSetExpansionOutcome expandVS(org.hl7.fhir.r5.model.ElementDefinition.ElementDefinitionBindingComponent binding, boolean cacheOk, boolean heiarchical) {
			throw new UnsupportedOperationException();
		}

		@Override
		public ValueSetExpander.ValueSetExpansionOutcome expandVS(org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent inc, boolean heirarchical) throws TerminologyServiceException {
			ValueSet.ConceptSetComponent convertedInc = null;
			if (inc != null) {
				try {
					convertedInc = VersionConvertor_30_50.convertConceptSetComponent(inc);
				} catch (FHIRException e) {
					throw new InternalErrorException(e);
				}
			}

			ValueSet.ValueSetExpansionComponent expansion = myWrap.expandVS(convertedInc, heirarchical);
			org.hl7.fhir.r5.model.ValueSet.ValueSetExpansionComponent valueSetExpansionComponent = null;
			if (expansion != null) {
				try {
					valueSetExpansionComponent = VersionConvertor_30_50.convertValueSetExpansionComponent(expansion);
				} catch (FHIRException e) {
					throw new InternalErrorException(e);
				}
			}

			ValueSetExpander.ValueSetExpansionOutcome outcome = new ValueSetExpander.ValueSetExpansionOutcome(new org.hl7.fhir.r5.model.ValueSet());
			outcome.getValueset().setExpansion(valueSetExpansionComponent);
			return outcome;
		}

		@Override
		public org.hl7.fhir.r5.model.CodeSystem fetchCodeSystem(String system) {
			CodeSystem fetched = myWrap.fetchCodeSystem(system);
			if (fetched == null) {
				return null;
			}
			try {
				return VersionConvertor_30_50.convertCodeSystem(fetched);
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

		public VersionConvertor_30_50 getConverter() {
			return myConverter;
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
			org.hl7.fhir.dstu3.context.IWorkerContext.ValidationResult result = myWrap.validateCode(system, code, display);
			return convertValidationResult(result);
		}

		@Override
		public ValidationResult validateCode(TerminologyServiceOptions theOptions, String system, String code, String display, org.hl7.fhir.r5.model.ValueSet vs) {
			ValueSet convertedVs = null;

			try {
				if (vs != null) {
					convertedVs = VersionConvertor_30_50.convertValueSet(vs);
				}
			} catch (FHIRException e) {
				throw new InternalErrorException(e);
			}

			org.hl7.fhir.dstu3.context.IWorkerContext.ValidationResult result = myWrap.validateCode(system, code, display, convertedVs);
			return convertValidationResult(result);
		}

		@Override
		public ValidationResult validateCode(TerminologyServiceOptions theOptions, String code, org.hl7.fhir.r5.model.ValueSet vs) {
			ValueSet convertedVs = null;
			try {
				if (vs != null) {
					convertedVs = VersionConvertor_30_50.convertValueSet(vs);
				}
			} catch (FHIRException e) {
				throw new InternalErrorException(e);
			}

			org.hl7.fhir.dstu3.context.IWorkerContext.ValidationResult result = myWrap.validateCode(null, code, null, convertedVs);
			return convertValidationResult(result);
		}

		@Override
		public ValidationResult validateCode(TerminologyServiceOptions theOptions, org.hl7.fhir.r5.model.Coding code, org.hl7.fhir.r5.model.ValueSet vs) {
			Coding convertedCode = null;
			ValueSet convertedVs = null;

			try {
				if (code != null) {
					convertedCode = VersionConvertor_30_50.convertCoding(code);
				}
				if (vs != null) {
					convertedVs = VersionConvertor_30_50.convertValueSet(vs);
				}
			} catch (FHIRException e) {
				throw new InternalErrorException(e);
			}

			org.hl7.fhir.dstu3.context.IWorkerContext.ValidationResult result = myWrap.validateCode(convertedCode, convertedVs);
			return convertValidationResult(result);
		}

		@Override
		public ValidationResult validateCode(TerminologyServiceOptions theOptions, org.hl7.fhir.r5.model.CodeableConcept code, org.hl7.fhir.r5.model.ValueSet vs) {
			CodeableConcept convertedCode = null;
			ValueSet convertedVs = null;

			try {
				if (code != null) {
					convertedCode = VersionConvertor_30_50.convertCodeableConcept(code);
				}
				if (vs != null) {
					convertedVs = VersionConvertor_30_50.convertValueSet(vs);
				}
			} catch (FHIRException e) {
				throw new InternalErrorException(e);
			}

			org.hl7.fhir.dstu3.context.IWorkerContext.ValidationResult result = myWrap.validateCode(convertedCode, convertedVs);
			return convertValidationResult(result);
		}

		@Override
		public ValidationResult validateCode(TerminologyServiceOptions theOptions, String system, String code, String display, org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent vsi) {
			ValueSet.ConceptSetComponent conceptSetComponent = null;
			if (vsi != null) {
				try {
					conceptSetComponent = VersionConvertor_30_50.convertConceptSetComponent(vsi);
				} catch (FHIRException e) {
					throw new InternalErrorException(e);
				}
			}

			org.hl7.fhir.dstu3.context.IWorkerContext.ValidationResult result = myWrap.validateCode(system, code, display, conceptSetComponent);
			return convertValidationResult(result);
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
