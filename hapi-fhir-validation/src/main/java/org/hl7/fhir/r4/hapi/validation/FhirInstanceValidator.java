package org.hl7.fhir.r4.hapi.validation;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.validation.IValidationContext;
import ca.uhn.fhir.validation.IValidatorModule;
import com.google.gson.*;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.exceptions.PathEngineException;
import org.hl7.fhir.r4.hapi.ctx.DefaultProfileValidationSupport;
import org.hl7.fhir.r4.hapi.ctx.HapiWorkerContext;
import org.hl7.fhir.r4.hapi.ctx.IValidationSupport;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.TypeDetails;
import org.hl7.fhir.r4.utils.FHIRPathEngine.IEvaluationContext;
import org.hl7.fhir.r4.utils.IResourceValidator.BestPracticeWarningLevel;
import org.hl7.fhir.r4.utils.IResourceValidator.IdStatus;
import org.hl7.fhir.r4.validation.InstanceValidator;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FhirInstanceValidator extends BaseValidatorBridge implements IValidatorModule {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirInstanceValidator.class);

	private boolean myAnyExtensionsAllowed = true;
	private BestPracticeWarningLevel myBestPracticeWarningLevel;
	private DocumentBuilderFactory myDocBuilderFactory;
	private boolean myNoTerminologyChecks;
	private StructureDefinition myStructureDefintion;

	private IValidationSupport myValidationSupport;

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
		return myNoTerminologyChecks;
	}

	/**
	 * If set to {@literal true} (default is false) the valueSet will not be validate
	 */
	public void setNoTerminologyChecks(final boolean theNoTerminologyChecks) {
		myNoTerminologyChecks = theNoTerminologyChecks;
	}

	public void setStructureDefintion(StructureDefinition theStructureDefintion) {
		myStructureDefintion = theStructureDefintion;
	}

	protected List<ValidationMessage> validate(final FhirContext theCtx, String theInput, EncodingEnum theEncoding) {
		HapiWorkerContext workerContext = new HapiWorkerContext(theCtx, myValidationSupport);

		InstanceValidator v;
		IEvaluationContext evaluationCtx = new NullEvaluationContext();
		try {
			v = new InstanceValidator(workerContext, evaluationCtx);
		} catch (Exception e) {
			throw new ConfigurationException(e);
		}

		v.setBestPracticeWarningLevel(getBestPracticeWarningLevel());
		v.setAnyExtensionsAllowed(isAnyExtensionsAllowed());
		v.setResourceIdRule(IdStatus.OPTIONAL);
		v.setNoTerminologyChecks(isNoTerminologyChecks());

		List<ValidationMessage> messages = new ArrayList<ValidationMessage>();

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

	public static class NullEvaluationContext implements IEvaluationContext {

		@Override
		public TypeDetails checkFunction(Object theAppContext, String theFunctionName, List<TypeDetails> theParameters) throws PathEngineException {
			return null;
		}

		@Override
		public List<Base> executeFunction(Object theAppContext, String theFunctionName, List<List<Base>> theParameters) {
			return null;
		}

		@Override
		public boolean log(String theArgument, List<Base> theFocus) {
			return false;
		}

		@Override
		public Base resolveConstant(Object theAppContext, String theName) throws PathEngineException {
			return null;
		}

		@Override
		public TypeDetails resolveConstantType(Object theAppContext, String theName) throws PathEngineException {
			return null;
		}

		@Override
		public FunctionDetails resolveFunction(String theFunctionName) {
			return null;
		}

		@Override
		public Base resolveReference(Object theAppContext, String theUrl) {
			return null;
		}

	}

}
