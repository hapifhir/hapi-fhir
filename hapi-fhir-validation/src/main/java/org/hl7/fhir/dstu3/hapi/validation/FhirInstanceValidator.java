package org.hl7.fhir.dstu3.hapi.validation;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.validation.IValidationContext;
import ca.uhn.fhir.validation.IValidatorModule;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.gson.*;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.time.DateUtils;
import org.fhir.ucum.UcumService;
import org.hl7.fhir.convertors.VersionConvertor_30_40;
import org.hl7.fhir.dstu3.hapi.ctx.HapiWorkerContext;
import org.hl7.fhir.dstu3.hapi.ctx.IValidationSupport;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.TerminologyServiceException;
import org.hl7.fhir.r4.context.IWorkerContext;
import org.hl7.fhir.r4.formats.IParser;
import org.hl7.fhir.r4.formats.ParserType;
import org.hl7.fhir.r4.terminologies.ValueSetExpander;
import org.hl7.fhir.r4.utils.FHIRPathEngine;
import org.hl7.fhir.r4.utils.INarrativeGenerator;
import org.hl7.fhir.r4.utils.IResourceValidator;
import org.hl7.fhir.r4.utils.IResourceValidator.BestPracticeWarningLevel;
import org.hl7.fhir.r4.utils.IResourceValidator.IdStatus;
import org.hl7.fhir.r4.validation.InstanceValidator;
import org.hl7.fhir.utilities.TranslationServices;
import org.hl7.fhir.utilities.validation.ValidationMessage;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueSeverity;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
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

	private String determineResourceName(Document theDocument) {
		Element root = null;

		NodeList list = theDocument.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i) instanceof Element) {
				root = (Element) list.item(i);
				break;
			}
		}
		root = theDocument.getDocumentElement();
		return root.getLocalName();
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
	 * @see {@link #setBestPracticeWarningLevel(BestPracticeWarningLevel)}
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

	protected List<ValidationMessage> validate(final FhirContext theCtx, String theInput, EncodingEnum theEncoding) {

		WorkerContextWrapper wrappedWorkerContext = myWrappedWorkerContext;
		if (wrappedWorkerContext == null) {
			HapiWorkerContext workerContext = new HapiWorkerContext(theCtx, myValidationSupport);
			wrappedWorkerContext = new WorkerContextWrapper(workerContext);
		}
		myWrappedWorkerContext = wrappedWorkerContext;

		InstanceValidator v;
		FHIRPathEngine.IEvaluationContext evaluationCtx = new org.hl7.fhir.r4.hapi.validation.FhirInstanceValidator.NullEvaluationContext();
		try {
			v = new InstanceValidator(wrappedWorkerContext, evaluationCtx);
		} catch (Exception e) {
			throw new ConfigurationException(e);
		}

		v.setBestPracticeWarningLevel(getBestPracticeWarningLevel());
		v.setAnyExtensionsAllowed(isAnyExtensionsAllowed());
		v.setResourceIdRule(IdStatus.OPTIONAL);
		v.setNoTerminologyChecks(isNoTerminologyChecks());

		List<ValidationMessage> messages = new ArrayList<>();

		if (theEncoding == EncodingEnum.XML) {
			Document document;
			try {
				DocumentBuilder builder = myDocBuilderFactory.newDocumentBuilder();
				InputSource src = new InputSource(new StringReader(theInput));
				document = builder.parse(src);
			} catch (Exception e2) {
				ourLog.error("Failure to parse XML input", e2);
				ValidationMessage m = new ValidationMessage();
				m.setLevel(IssueSeverity.FATAL);
				m.setMessage("Failed to parse input, it does not appear to be valid XML:" + e2.getMessage());
				return Collections.singletonList(m);
			}

			// Determine if meta/profiles are present...
			ArrayList<String> resourceNames = determineIfProfilesSpecified(document);
			if (resourceNames.isEmpty()) {
				resourceNames.add(determineResourceName(document));
			}

			for (String resourceName : resourceNames) {
				StructureDefinition profile = findStructureDefinitionForResourceName(theCtx, resourceName);
				if (profile != null) {
					try {
						v.validate(null, messages, document, profile.getUrl());
					} catch (Exception e) {
						ourLog.error("Failure during validation", e);
						throw new InternalErrorException("Unexpected failure while validating resource", e);
					}
				} else {
					profile = findStructureDefinitionForResourceName(theCtx, determineResourceName(document));
					if (profile != null) {
						try {
							v.validate(null, messages, document, profile.getUrl());
						} catch (Exception e) {
							ourLog.error("Failure during validation", e);
							throw new InternalErrorException("Unexpected failure while validating resource", e);
						}
					}
				}
			}
		} else if (theEncoding == EncodingEnum.JSON) {
			Gson gson = new GsonBuilder().create();
			JsonObject json = gson.fromJson(theInput, JsonObject.class);

			ArrayList<String> resourceNames = new ArrayList<String>();
			JsonArray profiles = null;
			try {
				profiles = json.getAsJsonObject("meta").getAsJsonArray("profile");
				for (JsonElement element : profiles) {
					resourceNames.add(element.getAsString());
				}
			} catch (Exception e) {
				resourceNames.add(json.get("resourceType").getAsString());
			}

			for (String resourceName : resourceNames) {
				StructureDefinition profile = findStructureDefinitionForResourceName(theCtx, resourceName);
				if (profile != null) {
					try {
						v.validate(null, messages, json, profile.getUrl());
					} catch (Exception e) {
						throw new InternalErrorException("Unexpected failure while validating resource", e);
					}
				} else {
					profile = findStructureDefinitionForResourceName(theCtx, json.get("resourceType").getAsString());
					if (profile != null) {
						try {
							v.validate(null, messages, json, profile.getUrl());
						} catch (Exception e) {
							ourLog.error("Failure during validation", e);
							throw new InternalErrorException("Unexpected failure while validating resource", e);
						}
					}
				}
			}
		} else {
			throw new IllegalArgumentException("Unknown encoding: " + theEncoding);
		}

		for (int i = 0; i < messages.size(); i++) {
			ValidationMessage next = messages.get(i);
			if ("Binding has no source, so can't be checked".equals(next.getMessage())) {
				messages.remove(i);
				i--;
			}
		}
		return messages;
	}

	@Override
	protected List<ValidationMessage> validate(IValidationContext<?> theCtx) {
		return validate(theCtx.getFhirContext(), theCtx.getResourceAsString(), theCtx.getResourceAsStringEncoding());
	}


	private class WorkerContextWrapper implements IWorkerContext {
		private final HapiWorkerContext myWrap;
		private final VersionConvertor_30_40 myConverter;
		private volatile List<org.hl7.fhir.r4.model.StructureDefinition> myAllStructures;
		private LoadingCache<ResourceKey, org.hl7.fhir.r4.model.Resource> myFetchResourceCache;

		public WorkerContextWrapper(HapiWorkerContext theWorkerContext) {
			myWrap = theWorkerContext;
			myConverter = new VersionConvertor_30_40();

			long timeoutMillis = 10 * DateUtils.MILLIS_PER_SECOND;
			if (System.getProperties().containsKey(ca.uhn.fhir.rest.api.Constants.TEST_SYSTEM_PROP_VALIDATION_RESOURCE_CACHES_MS)) {
				timeoutMillis = Long.parseLong(System.getProperty(Constants.TEST_SYSTEM_PROP_VALIDATION_RESOURCE_CACHES_MS));
			}

			myFetchResourceCache = Caffeine.newBuilder()
				.expireAfterWrite(timeoutMillis, TimeUnit.MILLISECONDS)
				.maximumSize(10000)
				.build(new CacheLoader<ResourceKey, org.hl7.fhir.r4.model.Resource>() {
					@Override
					public org.hl7.fhir.r4.model.Resource load(ResourceKey key) throws Exception {
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
							default:
								throw new UnsupportedOperationException("Don't know how to fetch " + key.getResourceName());
						}

						if (fetched == null) {
							return null;
						}

						try {
							return VersionConvertor_30_40.convertResource(fetched);
						} catch (FHIRException e) {
							throw new InternalErrorException(e);
						}
					}
				});
		}

		@Override
		public List<org.hl7.fhir.r4.model.MetadataResource> allConformanceResources() {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<org.hl7.fhir.r4.model.StructureDefinition> allStructures() {

			List<org.hl7.fhir.r4.model.StructureDefinition> retVal = myAllStructures;
			if (retVal == null) {
				retVal = new ArrayList<>();
				for (StructureDefinition next : myWrap.allStructures()) {
					try {
						retVal.add(VersionConvertor_30_40.convertStructureDefinition(next));
					} catch (FHIRException e) {
						throw new InternalErrorException(e);
					}
				}
				myAllStructures = retVal;
			}

			return retVal;
		}

		@Override
		public void cacheResource(org.hl7.fhir.r4.model.Resource res) throws FHIRException {
			throw new UnsupportedOperationException();
		}

		private ValidationResult convertValidationResult(org.hl7.fhir.dstu3.context.IWorkerContext.ValidationResult theResult) {
			IssueSeverity issueSeverity = theResult.getSeverity();
			String message = theResult.getMessage();
			org.hl7.fhir.r4.model.CodeSystem.ConceptDefinitionComponent conceptDefinition = null;
			if (theResult.asConceptDefinition() != null) {
				try {
					conceptDefinition = VersionConvertor_30_40.convertConceptDefinitionComponent(theResult.asConceptDefinition());
				} catch (FHIRException e) {
					throw new InternalErrorException(e);
				}
			}

			ValidationResult retVal = new ValidationResult(issueSeverity, message, conceptDefinition);
			return retVal;
		}

		@Override
		public ValueSetExpander.ValueSetExpansionOutcome expandVS(org.hl7.fhir.r4.model.ValueSet source, boolean cacheOk, boolean heiarchical) {
			ValueSet convertedSource = null;
			try {
				convertedSource = VersionConvertor_30_40.convertValueSet(source);
			} catch (FHIRException e) {
				throw new InternalErrorException(e);
			}
			org.hl7.fhir.dstu3.terminologies.ValueSetExpander.ValueSetExpansionOutcome expanded = myWrap.expandVS(convertedSource, cacheOk, heiarchical);

			org.hl7.fhir.r4.model.ValueSet convertedResult = null;
			if (expanded.getValueset() != null) {
				try {
					convertedResult = VersionConvertor_30_40.convertValueSet(expanded.getValueset());
				} catch (FHIRException e) {
					throw new InternalErrorException(e);
				}
			}

			String error = expanded.getError();
			ValueSetExpander.TerminologyServiceErrorClass result = null;

			return new ValueSetExpander.ValueSetExpansionOutcome(convertedResult, error, result);
		}

		@Override
		public ValueSetExpander.ValueSetExpansionOutcome expandVS(org.hl7.fhir.r4.model.ElementDefinition.ElementDefinitionBindingComponent binding, boolean cacheOk, boolean heiarchical) throws FHIRException {
			throw new UnsupportedOperationException();
		}

		@Override
		public org.hl7.fhir.r4.model.ValueSet.ValueSetExpansionComponent expandVS(org.hl7.fhir.r4.model.ValueSet.ConceptSetComponent inc, boolean heirarchical) throws TerminologyServiceException {
			ValueSet.ConceptSetComponent convertedInc = null;
			if (inc != null) {
				try {
					convertedInc = VersionConvertor_30_40.convertConceptSetComponent(inc);
				} catch (FHIRException e) {
					throw new InternalErrorException(e);
				}
			}

			ValueSet.ValueSetExpansionComponent expansion = myWrap.expandVS(convertedInc, heirarchical);
			org.hl7.fhir.r4.model.ValueSet.ValueSetExpansionComponent retVal = null;
			if (expansion != null) {
				try {
					retVal = VersionConvertor_30_40.convertValueSetExpansionComponent(expansion);
				} catch (FHIRException e) {
					throw new InternalErrorException(e);
				}
			}
			return retVal;
		}

		@Override
		public org.hl7.fhir.r4.model.CodeSystem fetchCodeSystem(String system) {
			CodeSystem fetched = myWrap.fetchCodeSystem(system);
			if (fetched == null) {
				return null;
			}
			try {
				return VersionConvertor_30_40.convertCodeSystem(fetched);
			} catch (FHIRException e) {
				throw new InternalErrorException(e);
			}
		}

		@Override
		public <T extends org.hl7.fhir.r4.model.Resource> T fetchResource(Class<T> class_, String uri) {

			ResourceKey key = new ResourceKey(class_.getSimpleName(), uri);
			@SuppressWarnings("unchecked")
			T retVal = (T) myFetchResourceCache.get(key);

			return retVal;
		}

		@Override
		public org.hl7.fhir.r4.model.Resource fetchResourceById(String type, String uri) {
			throw new UnsupportedOperationException();
		}

		@Override
		public <T extends org.hl7.fhir.r4.model.Resource> T fetchResourceWithException(Class<T> class_, String uri) throws FHIRException {
			T retVal = fetchResource(class_, uri);
			if (retVal == null) {
				throw new FHIRException("Can not find resource of type " + class_.getSimpleName() + " with uri " + uri);
			}
			return retVal;
		}

		@Override
		public List<org.hl7.fhir.r4.model.ConceptMap> findMapsForSource(String url) {
			throw new UnsupportedOperationException();
		}

		@Override
		public String getAbbreviation(String name) {
			return myWrap.getAbbreviation(name);
		}

		public VersionConvertor_30_40 getConverter() {
			return myConverter;
		}

		@Override
		public org.hl7.fhir.r4.model.ExpansionProfile getExpansionProfile() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setExpansionProfile(org.hl7.fhir.r4.model.ExpansionProfile expProfile) {
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
			return myWrap.getResourceNames();
		}

		@Override
		public Set<String> getResourceNamesAsSet() {
			return new HashSet<>(myWrap.getResourceNames());
		}

		@Override
		public org.hl7.fhir.r4.model.StructureMap getTransform(String url) {
			throw new UnsupportedOperationException();
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
		public String getVersion() {
			return myWrap.getVersion();
		}

		@Override
		public boolean hasCache() {
			return myWrap.hasCache();
		}

		@Override
		public <T extends org.hl7.fhir.r4.model.Resource> boolean hasResource(Class<T> class_, String uri) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isNoTerminologyServer() {
			return myWrap.isNoTerminologyServer();
		}

		@Override
		public List<org.hl7.fhir.r4.model.StructureMap> listTransforms() {
			throw new UnsupportedOperationException();
		}

		@Override
		public IParser newJsonParser() {
			throw new UnsupportedOperationException();
		}

		@Override
		public IResourceValidator newValidator() throws FHIRException {
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
		public void setLogger(ILoggingService logger) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean supportsSystem(String system) throws TerminologyServiceException {
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
		public ValidationResult validateCode(String system, String code, String display) {
			org.hl7.fhir.dstu3.context.IWorkerContext.ValidationResult result = myWrap.validateCode(system, code, display);
			return convertValidationResult(result);
		}

		@Override
		public ValidationResult validateCode(String system, String code, String display, org.hl7.fhir.r4.model.ValueSet vs) {
			ValueSet convertedVs = null;

			try {
				if (vs != null) {
					convertedVs = VersionConvertor_30_40.convertValueSet(vs);
				}
			} catch (FHIRException e) {
				throw new InternalErrorException(e);
			}

			org.hl7.fhir.dstu3.context.IWorkerContext.ValidationResult result = myWrap.validateCode(system, code, display, convertedVs);
			return convertValidationResult(result);
		}

		@Override
		public ValidationResult validateCode(org.hl7.fhir.r4.model.Coding code, org.hl7.fhir.r4.model.ValueSet vs) {
			Coding convertedCode = null;
			ValueSet convertedVs = null;

			try {
				if (code != null) {
					convertedCode = VersionConvertor_30_40.convertCoding(code);
				}
				if (vs != null) {
					convertedVs = VersionConvertor_30_40.convertValueSet(vs);
				}
			} catch (FHIRException e) {
				throw new InternalErrorException(e);
			}

			org.hl7.fhir.dstu3.context.IWorkerContext.ValidationResult result = myWrap.validateCode(convertedCode, convertedVs);
			return convertValidationResult(result);
		}

		@Override
		public ValidationResult validateCode(org.hl7.fhir.r4.model.CodeableConcept code, org.hl7.fhir.r4.model.ValueSet vs) {
			CodeableConcept convertedCode = null;
			ValueSet convertedVs = null;

			try {
				if (code != null) {
					convertedCode = VersionConvertor_30_40.convertCodeableConcept(code);
				}
				if (vs != null) {
					convertedVs = VersionConvertor_30_40.convertValueSet(vs);
				}
			} catch (FHIRException e) {
				throw new InternalErrorException(e);
			}

			org.hl7.fhir.dstu3.context.IWorkerContext.ValidationResult result = myWrap.validateCode(convertedCode, convertedVs);
			return convertValidationResult(result);
		}

		@Override
		public ValidationResult validateCode(String system, String code, String display, org.hl7.fhir.r4.model.ValueSet.ConceptSetComponent vsi) {
			ValueSet.ConceptSetComponent conceptSetComponent = null;
			if (vsi != null) {
				try {
					conceptSetComponent = VersionConvertor_30_40.convertConceptSetComponent(vsi);
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
