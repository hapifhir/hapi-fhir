package org.hl7.fhir.instance.hapi.validation;

import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimePrimitiveDatatypeDefinition;
import ca.uhn.fhir.context.support.IContextValidationSupport;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.XmlUtil;
import ca.uhn.fhir.validation.IInstanceValidatorModule;
import ca.uhn.fhir.validation.IValidationContext;
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
import org.hl7.fhir.converter.NullVersionConverterAdvisor50;
import org.hl7.fhir.convertors.VersionConvertor_10_50;
import org.hl7.fhir.convertors.VersionConvertor_30_50;
import org.hl7.fhir.convertors.conv10_50.ValueSet10_50;
import org.hl7.fhir.dstu2.model.CodeableConcept;
import org.hl7.fhir.dstu2.model.Coding;
import org.hl7.fhir.dstu2.model.Questionnaire;
import org.hl7.fhir.dstu2.model.StructureDefinition;
import org.hl7.fhir.dstu2.model.ValueSet;
import org.hl7.fhir.dstu3.hapi.validation.VersionSpecificWorkerContextWrapper;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.TerminologyServiceException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.context.IWorkerContext;
import org.hl7.fhir.r5.formats.IParser;
import org.hl7.fhir.r5.formats.ParserType;
import org.hl7.fhir.r5.model.CanonicalResource;
import org.hl7.fhir.r5.model.CodeSystem;
import org.hl7.fhir.r5.model.Parameters;
import org.hl7.fhir.r5.model.Resource;
import org.hl7.fhir.r5.terminologies.ValueSetExpander;
import org.hl7.fhir.r5.utils.FHIRPathEngine;
import org.hl7.fhir.r5.utils.INarrativeGenerator;
import org.hl7.fhir.r5.utils.IResourceValidator;
import org.hl7.fhir.r5.utils.IResourceValidator.BestPracticeWarningLevel;
import org.hl7.fhir.r5.utils.IResourceValidator.IdStatus;
import org.hl7.fhir.r5.validation.InstanceValidator;
import org.hl7.fhir.utilities.TranslationServices;
import org.hl7.fhir.utilities.validation.ValidationMessage;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueSeverity;
import org.hl7.fhir.utilities.validation.ValidationOptions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.hl7.fhir.convertors.VersionConvertor_10_50.convertCoding;
import static org.hl7.fhir.convertors.conv10_50.StructureDefinition10_50.convertStructureDefinition;
import static org.hl7.fhir.convertors.conv10_50.ValueSet10_50.convertConceptSetComponent;
import static org.hl7.fhir.convertors.conv10_50.ValueSet10_50.convertValueSet;
import static org.hl7.fhir.convertors.conv10_50.ValueSet10_50.convertValueSetExpansionComponent;

public class FhirInstanceValidator extends BaseValidatorBridge implements IInstanceValidatorModule {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirInstanceValidator.class);
	private static final FhirContext FHIR_CONTEXT = FhirContext.forDstu2();
	private static FhirContext ourHl7OrgCtx;

	private boolean myAnyExtensionsAllowed = true;
	private BestPracticeWarningLevel myBestPracticeWarningLevel;
	private StructureDefinition myStructureDefintion;
	private final IContextValidationSupport myValidationSupport;
	private boolean noTerminologyChecks = false;
	private boolean assumeValidRestReferences;
	private volatile VersionSpecificWorkerContextWrapper myWrappedWorkerContext;
	private IResourceValidator.IValidatorResourceFetcher validatorResourceFetcher;
	/**
	 * Constructor
	 * <p>
	 * Uses DefaultProfileValidationSupport for {@link IValidationSupport validation support}
	 */
	public FhirInstanceValidator(FhirContext theContext) {
		this(theContext.getValidationSupport());
	}
	/**
	 * Constructor which uses the given validation support
	 *
	 * @param theValidationSupport The validation support
	 */
	public FhirInstanceValidator(IContextValidationSupport theValidationSupport) {
		if (theValidationSupport.getFhirContext().getVersion().getVersion() == FhirVersionEnum.DSTU2) {
			myValidationSupport = new HapiToHl7OrgDstu2ValidatingSupportWrapper(theValidationSupport.getFhirContext(), theValidationSupport);
		} else {
			myValidationSupport = theValidationSupport;
		}
	}

	public boolean isAssumeValidRestReferences() {
		return assumeValidRestReferences;
	}

	public void setAssumeValidRestReferences(boolean assumeValidRestReferences) {
		this.assumeValidRestReferences = assumeValidRestReferences;
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

	private StructureDefinition findStructureDefinitionForResourceName(String resourceName) {
		String sdName;
		try {
			// Test if a URL was passed in specifying the structure definition and test if "StructureDefinition" is part of the URL
			URL testIfUrl = new URL(resourceName);
			sdName = resourceName;
		} catch (MalformedURLException e) {
			sdName = "http://hl7.org/fhir/StructureDefinition/" + resourceName;
		}
		StructureDefinition profile;
		if (myStructureDefintion != null) {
			profile = myStructureDefintion;
		} else {
			profile = myValidationSupport.fetchResource(StructureDefinition.class, sdName);
		}
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
	 * DefaultProfileValidationSupport if the no-arguments constructor for this object was used.
	 *
	 * @return
	 */
	public IContextValidationSupport getValidationSupport() {
		return myValidationSupport;
	}

// FIXME: remove
//	/**
//	 * Sets the {@link IValidationSupport validation support} in use by this validator. Default is an instance of
//	 * DefaultProfileValidationSupport if the no-arguments constructor for this object was used.
//	 */
//	public void setValidationSupport(IContextValidationSupport theValidationSupport) {
//		myValidationSupport = theValidationSupport;
//		myWrappedWorkerContext = null;
//	}

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

		VersionSpecificWorkerContextWrapper wrappedWorkerContext = myWrappedWorkerContext;
		if (wrappedWorkerContext == null) {
			VersionSpecificWorkerContextWrapper.IVersionTypeConverter converter = new VersionSpecificWorkerContextWrapper.IVersionTypeConverter() {
				@Override
				public Resource toCanonical(IBaseResource theNonCanonical) {
					return VersionConvertor_10_50.convertResource((org.hl7.fhir.dstu2.model.Resource)theNonCanonical);
				}

				@Override
				public IBaseResource fromCanonical(Resource theCanonical) {
					return VersionConvertor_10_50.convertResource(theCanonical);
				}
			};
			wrappedWorkerContext = new VersionSpecificWorkerContextWrapper(myValidationSupport, converter);
		}
		myWrappedWorkerContext = wrappedWorkerContext;

		InstanceValidator v;
		FHIRPathEngine.IEvaluationContext evaluationCtx = new org.hl7.fhir.r5.hapi.validation.FhirInstanceValidator.NullEvaluationContext();
		try {
			v = new InstanceValidator(wrappedWorkerContext, evaluationCtx);
		} catch (Exception e) {
			throw new ConfigurationException(e);
		}

		v.setBestPracticeWarningLevel(getBestPracticeWarningLevel());
		v.setAnyExtensionsAllowed(isAnyExtensionsAllowed());
		v.setResourceIdRule(IdStatus.OPTIONAL);
		v.setNoTerminologyChecks(isNoTerminologyChecks());
		v.setFetcher(getValidatorResourceFetcher());
		v.setAssumeValidRestReferences(isAssumeValidRestReferences());

		List<ValidationMessage> messages = new ArrayList<>();

		if (theEncoding == EncodingEnum.XML) {
			Document document;
			try {
				document = XmlUtil.parseDocument(theInput);
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
				StructureDefinition profile = findStructureDefinitionForResourceName(resourceName);
				if (profile != null) {
					try {
						v.validate(null, messages, document, profile.getUrl());
					} catch (Exception e) {
						ourLog.error("Failure during validation", e);
						throw new InternalErrorException("Unexpected failure while validating resource", e);
					}
				} else {
					profile = findStructureDefinitionForResourceName(determineResourceName(document));
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
				StructureDefinition profile = findStructureDefinitionForResourceName(resourceName);
				if (profile != null) {
					try {
						v.validate(null, messages, json, profile.getUrl());
					} catch (Exception e) {
						throw new InternalErrorException("Unexpected failure while validating resource", e);
					}
				} else {
					profile = findStructureDefinitionForResourceName(json.get("resourceType").getAsString());
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

	public IResourceValidator.IValidatorResourceFetcher getValidatorResourceFetcher() {
		return validatorResourceFetcher;
	}

	public void setValidatorResourceFetcher(IResourceValidator.IValidatorResourceFetcher validatorResourceFetcher) {
		this.validatorResourceFetcher = validatorResourceFetcher;
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
			profileText = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
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
}
