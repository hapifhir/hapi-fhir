package org.hl7.fhir.common.hapi.validation.validator;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.util.XmlUtil;
import ca.uhn.fhir.validation.IValidationContext;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.codec.Charsets;
import org.apache.commons.io.input.ReaderInputStream;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r5.context.IWorkerContext;
import org.hl7.fhir.r5.elementmodel.Manager;
import org.hl7.fhir.r5.model.StructureDefinition;
import org.hl7.fhir.r5.utils.FHIRPathEngine;
import org.hl7.fhir.r5.utils.XVerExtensionManager;
import org.hl7.fhir.r5.utils.validation.IValidationPolicyAdvisor;
import org.hl7.fhir.r5.utils.validation.IValidatorResourceFetcher;
import org.hl7.fhir.r5.utils.validation.constants.BestPracticeWarningLevel;
import org.hl7.fhir.r5.utils.validation.constants.IdStatus;
import org.hl7.fhir.utilities.validation.ValidationMessage;
import org.hl7.fhir.validation.instance.InstanceValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class ValidatorWrapper {

	private static final Logger ourLog = LoggerFactory.getLogger(ValidatorWrapper.class);
	private BestPracticeWarningLevel myBestPracticeWarningLevel;
	private boolean myAnyExtensionsAllowed;
	private boolean myErrorForUnknownProfiles;
	private boolean myNoTerminologyChecks;
	private boolean myAssumeValidRestReferences;
	private boolean myNoExtensibleWarnings;
	private boolean myNoBindingMsgSuppressed;
	private Collection<? extends String> myExtensionDomains;
	private IValidatorResourceFetcher myValidatorResourceFetcher;
	private IValidationPolicyAdvisor myValidationPolicyAdvisor;

	/**
	 * Constructor
	 */
	public ValidatorWrapper() {
		super();
	}

	public boolean isAssumeValidRestReferences() {
		return myAssumeValidRestReferences;
	}

	public ValidatorWrapper setAssumeValidRestReferences(boolean assumeValidRestReferences) {
		this.myAssumeValidRestReferences = assumeValidRestReferences;
		return this;
	}

	public ValidatorWrapper setBestPracticeWarningLevel(BestPracticeWarningLevel theBestPracticeWarningLevel) {
		myBestPracticeWarningLevel = theBestPracticeWarningLevel;
		return this;
	}

	public ValidatorWrapper setAnyExtensionsAllowed(boolean theAnyExtensionsAllowed) {
		myAnyExtensionsAllowed = theAnyExtensionsAllowed;
		return this;
	}

	public ValidatorWrapper setErrorForUnknownProfiles(boolean theErrorForUnknownProfiles) {
		myErrorForUnknownProfiles = theErrorForUnknownProfiles;
		return this;
	}

	public ValidatorWrapper setNoTerminologyChecks(boolean theNoTerminologyChecks) {
		myNoTerminologyChecks = theNoTerminologyChecks;
		return this;
	}

	public ValidatorWrapper setNoExtensibleWarnings(boolean theNoExtensibleWarnings) {
		myNoExtensibleWarnings = theNoExtensibleWarnings;
		return this;
	}

	public ValidatorWrapper setNoBindingMsgSuppressed(boolean theNoBindingMsgSuppressed) {
		myNoBindingMsgSuppressed = theNoBindingMsgSuppressed;
		return this;
	}

	public ValidatorWrapper setExtensionDomains(Collection<? extends String> theExtensionDomains) {
		myExtensionDomains = theExtensionDomains;
		return this;
	}

	public ValidatorWrapper setValidationPolicyAdvisor(IValidationPolicyAdvisor validationPolicyAdvisor) {
		this.myValidationPolicyAdvisor = validationPolicyAdvisor;
		return this;
	}

	public ValidatorWrapper setValidatorResourceFetcher(IValidatorResourceFetcher validatorResourceFetcher) {
		this.myValidatorResourceFetcher = validatorResourceFetcher;
		return this;
	}

	public List<ValidationMessage> validate(IWorkerContext theWorkerContext, IValidationContext<?> theValidationContext) {
		InstanceValidator v;
		FHIRPathEngine.IEvaluationContext evaluationCtx = new FhirInstanceValidator.NullEvaluationContext();
		XVerExtensionManager xverManager = new XVerExtensionManager(theWorkerContext);
		try {
			v = new InstanceValidator(theWorkerContext, evaluationCtx, xverManager);
		} catch (Exception e) {
			throw new ConfigurationException(Msg.code(648) + e);
		}

		v.setAssumeValidRestReferences(isAssumeValidRestReferences());
		v.setBestPracticeWarningLevel(myBestPracticeWarningLevel);
		v.setAnyExtensionsAllowed(myAnyExtensionsAllowed);
		v.setResourceIdRule(IdStatus.OPTIONAL);
		v.setNoTerminologyChecks(myNoTerminologyChecks);
		v.setErrorForUnknownProfiles(myErrorForUnknownProfiles);
		v.getExtensionDomains().addAll(myExtensionDomains);
		v.setFetcher(myValidatorResourceFetcher);
		v.setPolicyAdvisor(myValidationPolicyAdvisor);
		v.setNoExtensibleWarnings(myNoExtensibleWarnings);
		v.setNoBindingMsgSuppressed(myNoBindingMsgSuppressed);
		v.setAllowXsiLocation(true);

		List<ValidationMessage> messages = new ArrayList<>();

		List<StructureDefinition> profileUrls = new ArrayList<>();
		for (String next : theValidationContext.getOptions().getProfiles()) {
			fetchAndAddProfile(theWorkerContext, profileUrls, next);
		}

		String input = theValidationContext.getResourceAsString();
		EncodingEnum encoding = theValidationContext.getResourceAsStringEncoding();
		if (encoding == EncodingEnum.XML) {
			Document document;
			try {
				document = XmlUtil.parseDocument(input);
			} catch (Exception e2) {
				ourLog.error("Failure to parse XML input", e2);
				ValidationMessage m = new ValidationMessage();
				m.setLevel(ValidationMessage.IssueSeverity.FATAL);
				m.setMessage("Failed to parse input, it does not appear to be valid XML:" + e2.getMessage());
				messages.add(m);
				return messages;
			}

			// Determine if meta/profiles are present...
			ArrayList<String> profiles = determineIfProfilesSpecified(document);
			for (String nextProfile : profiles) {
				fetchAndAddProfile(theWorkerContext, profileUrls, nextProfile);
			}

			String resourceAsString = theValidationContext.getResourceAsString();
			InputStream inputStream = new ReaderInputStream(new StringReader(resourceAsString), Charsets.UTF_8);

			Manager.FhirFormat format = Manager.FhirFormat.XML;
			v.validate(null, messages, inputStream, format, profileUrls);

		} else if (encoding == EncodingEnum.JSON) {

			Gson gson = new GsonBuilder().create();
			JsonObject json = gson.fromJson(input, JsonObject.class);

			JsonObject meta = json.getAsJsonObject("meta");
			if (meta != null) {
				JsonElement profileElement = meta.get("profile");
				if (profileElement != null && profileElement.isJsonArray()) {
					JsonArray profiles = profileElement.getAsJsonArray();
					for (JsonElement element : profiles) {
						String nextProfile = element.getAsString();
						fetchAndAddProfile(theWorkerContext, profileUrls, nextProfile);
					}
				}
			}

			String resourceAsString = theValidationContext.getResourceAsString();
			InputStream inputStream = new ReaderInputStream(new StringReader(resourceAsString), Charsets.UTF_8);

			Manager.FhirFormat format = Manager.FhirFormat.JSON;
			v.validate(null, messages, inputStream, format, profileUrls);

		} else {
			throw new IllegalArgumentException(Msg.code(649) + "Unknown encoding: " + encoding);
		}

		for (int i = 0; i < messages.size(); i++) {
			ValidationMessage next = messages.get(i);
			String message = next.getMessage();

			// TODO: are these still needed?
			if ("Binding has no source, so can't be checked".equals(message) ||
				"ValueSet http://hl7.org/fhir/ValueSet/mimetypes not found".equals(message)) {
				messages.remove(i);
				i--;
			}

			if (
				myErrorForUnknownProfiles &&
				next.getLevel() == ValidationMessage.IssueSeverity.WARNING &&
				message.contains("Profile reference '") &&
				message.contains("' has not been checked because it is unknown")
			) {
				next.setLevel(ValidationMessage.IssueSeverity.ERROR);
			}

		}

		return messages;
	}

	private void fetchAndAddProfile(IWorkerContext theWorkerContext, List<StructureDefinition> theProfileStructureDefinitions, String theUrl) throws org.hl7.fhir.exceptions.FHIRException {
		try {

			// NOTE: We expect the following call to generate a snapshot if needed
			StructureDefinition structureDefinition = theWorkerContext.fetchRawProfile(theUrl);

			theProfileStructureDefinitions.add(structureDefinition);
		} catch (FHIRException e) {
			ourLog.debug("Failed to load profile: {}", theUrl);
		}
	}


	private ArrayList<String> determineIfProfilesSpecified(Document theDocument) {
		ArrayList<String> profileNames = new ArrayList<>();
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

}
