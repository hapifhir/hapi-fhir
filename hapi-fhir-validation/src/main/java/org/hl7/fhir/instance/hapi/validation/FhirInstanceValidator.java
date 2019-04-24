package org.hl7.fhir.instance.hapi.validation;

import static org.apache.commons.lang3.StringUtils.isBlank;

import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimePrimitiveDatatypeDefinition;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.validation.IValidationContext;
import ca.uhn.fhir.validation.IValidatorModule;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.fhir.ucum.UcumService;
import org.hl7.fhir.convertors.NullVersionConverterAdvisor40;
import org.hl7.fhir.convertors.VersionConvertorAdvisor40;
import org.hl7.fhir.convertors.VersionConvertor_10_40;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.TerminologyServiceException;
import org.hl7.fhir.instance.model.CodeableConcept;
import org.hl7.fhir.instance.model.Coding;
import org.hl7.fhir.instance.model.Questionnaire;
import org.hl7.fhir.instance.model.Resource;
import org.hl7.fhir.instance.model.StructureDefinition;
import org.hl7.fhir.instance.model.ValueSet;
import org.hl7.fhir.r4.context.IWorkerContext;
import org.hl7.fhir.r4.formats.IParser;
import org.hl7.fhir.r4.formats.ParserType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Parameters;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class FhirInstanceValidator extends BaseValidatorBridge implements IValidatorModule {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirInstanceValidator.class);
	private static final FhirContext FHIR_CONTEXT = FhirContext.forDstu2();
	private static FhirContext ourHl7OrgCtx;

	private boolean myAnyExtensionsAllowed = true;
	private BestPracticeWarningLevel myBestPracticeWarningLevel;
	private DocumentBuilderFactory myDocBuilderFactory;
	private StructureDefinition myStructureDefintion;
	private IValidationSupport myValidationSupport;
	private boolean noTerminologyChecks = false;
	private volatile WorkerContextWrapper myWrappedWorkerContext;
	private VersionConvertorAdvisor40 myAdvisor = new NullAdvisor();

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

	private CodeSystem convertCodeSystem(ValueSet theFetched) {
		CodeSystem retVal = new CodeSystem();

		retVal.setUrl(theFetched.getCodeSystem().getSystem());
		retVal.setVersion(theFetched.getVersion());

		List<ValueSet.ConceptDefinitionComponent> sourceConceptList = theFetched.getCodeSystem().getConcept();
		List<CodeSystem.ConceptDefinitionComponent> targetConceptList = retVal.getConcept();
		convertConceptList(sourceConceptList, targetConceptList);

		return retVal;
	}

	private CodeSystem.ConceptDefinitionComponent convertConceptDefinition(ValueSet.ConceptDefinitionComponent next) {
		CodeSystem.ConceptDefinitionComponent convertedConceptDef = new CodeSystem.ConceptDefinitionComponent();
		convertedConceptDef.setCode(next.getCode());
		convertedConceptDef.setDisplay(next.getDisplay());

		convertConceptList(next.getConcept(), convertedConceptDef.getConcept());
		return convertedConceptDef;
	}

	private void convertConceptList(List<ValueSet.ConceptDefinitionComponent> theSourceConceptList, List<CodeSystem.ConceptDefinitionComponent> theTargetConceptList) {
		for (ValueSet.ConceptDefinitionComponent next : theSourceConceptList) {
			CodeSystem.ConceptDefinitionComponent convertedConceptDef = convertConceptDefinition(next);
			theTargetConceptList.add(convertedConceptDef);
		}
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

	private ArrayList<String> determineIfProfilesSpecified(Document theDocument)
	{
		ArrayList<String> profileNames = new ArrayList<String>();
		NodeList list = theDocument.getChildNodes().item(0).getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i).getNodeName().compareToIgnoreCase("meta") == 0)
			{
				NodeList metaList = list.item(i).getChildNodes();
				for (int j = 0; j < metaList.getLength(); j++)
				{
					if (metaList.item(j).getNodeName().compareToIgnoreCase("profile") == 0)
					{
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
		}
		catch (MalformedURLException e)
		{
			sdName = "http://hl7.org/fhir/StructureDefinition/" + resourceName;
		}
		StructureDefinition profile = myStructureDefintion != null ? myStructureDefintion : myValidationSupport.fetchResource(theCtx, StructureDefinition.class, sdName);
		return profile;
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
			if (resourceNames.isEmpty())
			{
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
				}
				else
				{
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
				for (JsonElement element : profiles)
				{
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
				}
				else
				{
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
			if (next.getLocation().contains("text")) {
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

	static FhirContext getHl7OrgDstu2Ctx(FhirContext theCtx) {
		if (theCtx.getVersion().getVersion() == FhirVersionEnum.DSTU2_HL7ORG) {
			return theCtx;
		}
		FhirContext retVal = ourHl7OrgCtx;
		if (retVal == null) {
			retVal = FhirContext.forDstu2Hl7Org();
			ourHl7OrgCtx = retVal;
		}
		return retVal;
	}

	static StructureDefinition loadProfileOrReturnNull(List<ValidationMessage> theMessages, FhirContext theCtx,
																		String theResourceName) {
		if (isBlank(theResourceName)) {
			if (theMessages != null) {
				theMessages.add(new ValidationMessage().setLevel(IssueSeverity.FATAL)
					.setMessage("Could not determine resource type from request. Content appears invalid."));
			}
			return null;
		}

		String profileClasspath = theCtx.getVersion().getPathToSchemaDefinitions().replace("/schema", "/profile");
		String profileCpName = profileClasspath + '/' + theResourceName.toLowerCase() + ".profile.xml";
		String profileText;
		try (InputStream inputStream = FhirInstanceValidator.class.getResourceAsStream(profileCpName)) {
			if (inputStream == null) {
				if (theMessages != null) {
					theMessages.add(new ValidationMessage().setLevel(IssueSeverity.FATAL)
						.setMessage("No profile found for resource type " + theResourceName));
					return null;
				} else {
					return null;
				}
			}
			profileText = IOUtils.toString(inputStream, "UTF-8");
		} catch (IOException e1) {
			if (theMessages != null) {
				theMessages.add(new ValidationMessage().setLevel(IssueSeverity.FATAL)
					.setMessage("No profile found for resource type " + theResourceName));
			}
			return null;
		}
		StructureDefinition profile = getHl7OrgDstu2Ctx(theCtx).newXmlParser().parseResource(StructureDefinition.class,
			profileText);
		return profile;
	}

	private class WorkerContextWrapper implements IWorkerContext {
		private final HapiWorkerContext myWrap;
		private final VersionConvertor_10_40 myConverter;
		private volatile List<org.hl7.fhir.r4.model.StructureDefinition> myAllStructures;
		private LoadingCache<ResourceKey, org.hl7.fhir.r4.model.Resource> myFetchResourceCache
			= Caffeine.newBuilder()
			.expireAfterWrite(10, TimeUnit.SECONDS)
			.maximumSize(10000)
			.build(new CacheLoader<ResourceKey, org.hl7.fhir.r4.model.Resource>() {
				@Override
				public org.hl7.fhir.r4.model.Resource load(FhirInstanceValidator.ResourceKey key) throws Exception {
					org.hl7.fhir.instance.model.Resource fetched;
					switch (key.getResourceName()) {
						case "StructureDefinition":
							fetched = myWrap.fetchResource(StructureDefinition.class, key.getUri());
							break;
						case "ValueSet":
							fetched = myWrap.fetchResource(ValueSet.class, key.getUri());
							break;
						case "CodeSystem":
							fetched = myWrap.fetchResource(ValueSet.class, key.getUri());
							break;
						case "Questionnaire":
							fetched = myWrap.fetchResource(Questionnaire.class, key.getUri());
							break;
						default:
							throw new UnsupportedOperationException("Don't know how to fetch " + key.getResourceName());
					}

					if (fetched == null) {
						if (key.getUri().equals("http://hl7.org/fhir/StructureDefinition/xhtml")) {
							return null;
						}
					}

					try {
						org.hl7.fhir.r4.model.Resource converted = new VersionConvertor_10_40(myAdvisor).convertResource(fetched);

						if (fetched instanceof StructureDefinition) {
							StructureDefinition fetchedSd = (StructureDefinition) fetched;
							StructureDefinition.StructureDefinitionKind kind = fetchedSd.getKind();
							if (kind == StructureDefinition.StructureDefinitionKind.DATATYPE) {
								BaseRuntimeElementDefinition<?> element = FHIR_CONTEXT.getElementDefinition(fetchedSd.getName());
								if (element instanceof RuntimePrimitiveDatatypeDefinition) {
									org.hl7.fhir.r4.model.StructureDefinition convertedSd = (org.hl7.fhir.r4.model.StructureDefinition) converted;
									convertedSd.setKind(org.hl7.fhir.r4.model.StructureDefinition.StructureDefinitionKind.PRIMITIVETYPE);
								}
							}
						}

						return converted;
					} catch (FHIRException e) {
						throw new InternalErrorException(e);
					}
				}
			});
		private Parameters myExpansionProfile;

		public WorkerContextWrapper(HapiWorkerContext theWorkerContext) {
			myWrap = theWorkerContext;
			myConverter = new VersionConvertor_10_40(myAdvisor);
		}

		@Override
		public List<org.hl7.fhir.r4.model.MetadataResource> allConformanceResources() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Parameters getExpansionParameters() {
			return myExpansionProfile;
		}

		@Override
		public void setExpansionProfile(Parameters expParameters) {
			 myExpansionProfile = expParameters;
		}

		@Override
		public List<org.hl7.fhir.r4.model.StructureDefinition> allStructures() {

			List<org.hl7.fhir.r4.model.StructureDefinition> retVal = myAllStructures;
			if (retVal == null) {
				retVal = new ArrayList<>();
				for (StructureDefinition next : myWrap.allStructures()) {
					try {
						org.hl7.fhir.r4.model.StructureDefinition converted = new VersionConvertor_10_40(myAdvisor).convertStructureDefinition(next);
						if (converted != null) {
							retVal.add(converted);
						}
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

		private ValidationResult convertValidationResult(org.hl7.fhir.instance.utils.IWorkerContext.ValidationResult theResult) {
			IssueSeverity issueSeverity = theResult.getSeverity();
			String message = theResult.getMessage();
			org.hl7.fhir.r4.model.CodeSystem.ConceptDefinitionComponent conceptDefinition = null;
			if (theResult.asConceptDefinition() != null) {
				conceptDefinition = convertConceptDefinition(theResult.asConceptDefinition());
			}

			ValidationResult retVal = new ValidationResult(issueSeverity, message, conceptDefinition);
			return retVal;
		}

		@Override
		public ValueSetExpander.ValueSetExpansionOutcome expandVS(org.hl7.fhir.r4.model.ValueSet source, boolean cacheOk, boolean heiarchical) {
			ValueSet convertedSource = null;
			try {
				convertedSource = new VersionConvertor_10_40(myAdvisor).convertValueSet(source);
			} catch (FHIRException e) {
				throw new InternalErrorException(e);
			}
			org.hl7.fhir.instance.terminologies.ValueSetExpander.ValueSetExpansionOutcome expanded = myWrap.expandVS(convertedSource, cacheOk);

			org.hl7.fhir.r4.model.ValueSet convertedResult = null;
			if (expanded.getValueset() != null) {
				try {
					convertedResult = new VersionConvertor_10_40(myAdvisor).convertValueSet(expanded.getValueset());
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
		public ValueSetExpander.ValueSetExpansionOutcome expandVS(org.hl7.fhir.r4.model.ValueSet.ConceptSetComponent inc, boolean heirarchical) throws TerminologyServiceException {
			ValueSet.ConceptSetComponent convertedInc = null;
			if (inc != null) {
				try {
					convertedInc = new VersionConvertor_10_40(myAdvisor).convertConceptSetComponent(inc);
				} catch (FHIRException e) {
					throw new InternalErrorException(e);
				}
			}

			ValueSet.ValueSetExpansionComponent expansion = myWrap.expandVS(convertedInc);
			org.hl7.fhir.r4.model.ValueSet.ValueSetExpansionComponent valueSetExpansionComponent = null;
			if (expansion != null) {
				try {
					valueSetExpansionComponent = new VersionConvertor_10_40(myAdvisor).convertValueSetExpansionComponent(expansion);
				} catch (FHIRException e) {
					throw new InternalErrorException(e);
				}
			}

			ValueSetExpander.ValueSetExpansionOutcome outcome = new ValueSetExpander.ValueSetExpansionOutcome(new org.hl7.fhir.r4.model.ValueSet());
			outcome.getValueset().setExpansion(valueSetExpansionComponent);
			return outcome;
		}

		@Override
		public org.hl7.fhir.r4.model.CodeSystem fetchCodeSystem(String system) {
			ValueSet fetched = myWrap.fetchCodeSystem(system);
			if (fetched == null) {
				return null;
			}

			return convertCodeSystem(fetched);
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

		public VersionConvertor_10_40 getConverter() {
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
		public org.hl7.fhir.r4.model.StructureMap getTransform(String url) {
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
		public org.hl7.fhir.r4.model.StructureDefinition fetchTypeDefinition(String typeName) {
			return fetchResource(org.hl7.fhir.r4.model.StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/"+typeName);
		}

		@Override
		public void setUcumService(UcumService ucumService) {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<String> getTypeNames() {
			throw new UnsupportedOperationException();
		}

		@Override
		public String getVersion() {
			return FhirVersionEnum.DSTU2.getFhirVersionString();
		}

		@Override
		public UcumService getUcumService() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean hasCache() {
			return false;
		}

		@Override
		public <T extends org.hl7.fhir.r4.model.Resource> boolean hasResource(Class<T> class_, String uri) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isNoTerminologyServer() {
			return true;
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
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLogger(ILoggingService logger) {
			throw new UnsupportedOperationException();
		}

		@Override
		public ILoggingService getLogger() {
			return null;
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
			throw new UnsupportedOperationException();
		}

		@Override
		public ValidationResult validateCode(String system, String code, String display) {
			org.hl7.fhir.instance.utils.IWorkerContext.ValidationResult result = myWrap.validateCode(system, code, display);
			return convertValidationResult(result);
		}

		@Override
		public ValidationResult validateCode(String system, String code, String display, org.hl7.fhir.r4.model.ValueSet vs) {
			ValueSet convertedVs = null;

			try {
				if (vs != null) {
					convertedVs = new VersionConvertor_10_40(myAdvisor).convertValueSet(vs);
				}
			} catch (FHIRException e) {
				throw new InternalErrorException(e);
			}

			org.hl7.fhir.instance.utils.IWorkerContext.ValidationResult result = myWrap.validateCode(system, code, display, convertedVs);
			return convertValidationResult(result);
		}

		@Override
		public ValidationResult validateCode(String code, org.hl7.fhir.r4.model.ValueSet vs) {
			ValueSet convertedVs = null;
			try {
				if (vs != null) {
					VersionConvertorAdvisor40 advisor40 = new NullVersionConverterAdvisor40();
					convertedVs = new VersionConvertor_10_40(advisor40).convertValueSet(vs);
				}
			} catch (FHIRException e) {
				throw new InternalErrorException(e);
			}

			org.hl7.fhir.instance.utils.IWorkerContext.ValidationResult result = myWrap.validateCode(null, code, null, convertedVs);
			return convertValidationResult(result);
		}

		@Override
		public ValidationResult validateCode(org.hl7.fhir.r4.model.Coding code, org.hl7.fhir.r4.model.ValueSet vs) {
			Coding convertedCode = null;
			ValueSet convertedVs = null;

			try {
				if (code != null) {
					convertedCode = new VersionConvertor_10_40(myAdvisor).convertCoding(code);
				}
				if (vs != null) {
					convertedVs = new VersionConvertor_10_40(myAdvisor).convertValueSet(vs);
				}
			} catch (FHIRException e) {
				throw new InternalErrorException(e);
			}

			org.hl7.fhir.instance.utils.IWorkerContext.ValidationResult result = myWrap.validateCode(convertedCode, convertedVs);
			return convertValidationResult(result);
		}

		@Override
		public ValidationResult validateCode(org.hl7.fhir.r4.model.CodeableConcept code, org.hl7.fhir.r4.model.ValueSet vs) {
			CodeableConcept convertedCode = null;
			ValueSet convertedVs = null;

			try {
				if (code != null) {
					convertedCode = new VersionConvertor_10_40(myAdvisor).convertCodeableConcept(code);
				}
				if (vs != null) {
					convertedVs = new VersionConvertor_10_40(myAdvisor).convertValueSet(vs);
				}
			} catch (FHIRException e) {
				throw new InternalErrorException(e);
			}

			org.hl7.fhir.instance.utils.IWorkerContext.ValidationResult result = myWrap.validateCode(convertedCode, convertedVs);
			return convertValidationResult(result);
		}

		@Override
		public ValidationResult validateCode(String system, String code, String display, org.hl7.fhir.r4.model.ValueSet.ConceptSetComponent vsi) {
			ValueSet.ConceptSetComponent conceptSetComponent = null;
			if (vsi != null) {
				try {
					conceptSetComponent = new VersionConvertor_10_40(myAdvisor).convertConceptSetComponent(vsi);
				} catch (FHIRException e) {
					throw new InternalErrorException(e);
				}
			}

			org.hl7.fhir.instance.utils.IWorkerContext.ValidationResult result = myWrap.validateCode(system, code, display, conceptSetComponent);
			return convertValidationResult(result);
		}

	}

	private class NullAdvisor implements VersionConvertorAdvisor40 {
		@Override
		public Resource convertR2(org.hl7.fhir.r4.model.Resource resource) throws FHIRException {
			return null;
		}

		@Override
		public org.hl7.fhir.dstu3.model.Resource convertR3(org.hl7.fhir.r4.model.Resource resource) throws FHIRException {
			return null;
		}

		@Override
		public CodeSystem getCodeSystem(org.hl7.fhir.r4.model.ValueSet src) {
			return null;
		}

		@Override
		public void handleCodeSystem(CodeSystem tgtcs, org.hl7.fhir.r4.model.ValueSet source) {
			// ignore
		}

		@Override
		public boolean ignoreEntry(org.hl7.fhir.r4.model.Bundle.BundleEntryComponent src) {
			return false;
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
