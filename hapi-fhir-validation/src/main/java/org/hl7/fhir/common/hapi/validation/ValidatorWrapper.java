package org.hl7.fhir.common.hapi.validation;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.validation.IValidationContext;
import com.google.gson.*;
import org.hl7.fhir.r5.context.IWorkerContext;
import org.hl7.fhir.r5.utils.FHIRPathEngine;
import org.hl7.fhir.r5.utils.IResourceValidator;
import org.hl7.fhir.r5.utils.ValidationProfileSet;
import org.hl7.fhir.r5.validation.InstanceValidator;
import org.hl7.fhir.utilities.validation.ValidationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ValidatorWrapper {

	private static final Logger ourLog = LoggerFactory.getLogger(ValidatorWrapper.class);
	private final DocumentBuilderFactory myDocBuilderFactory;
	private IResourceValidator.BestPracticeWarningLevel myBestPracticeWarningLevel;
	private boolean myAnyExtensionsAllowed;
	private boolean myErrorForUnknownProfiles;
	private boolean myNoTerminologyChecks;
	private Collection<? extends String> myExtensionDomains;

	/**
	 * Constructor
	 */
	public ValidatorWrapper() {
		myDocBuilderFactory = DocumentBuilderFactory.newInstance();
		myDocBuilderFactory.setNamespaceAware(true);
	}

	public ValidatorWrapper setBestPracticeWarningLevel(IResourceValidator.BestPracticeWarningLevel theBestPracticeWarningLevel) {
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

	public ValidatorWrapper setExtensionDomains(Collection<? extends String> theExtensionDomains) {
		myExtensionDomains = theExtensionDomains;
		return this;
	}

	public List<ValidationMessage> validate(IWorkerContext theWorkerContext, IValidationContext<?> theValidationContext) {
		InstanceValidator v;
		FHIRPathEngine.IEvaluationContext evaluationCtx = new org.hl7.fhir.r5.hapi.validation.FhirInstanceValidator.NullEvaluationContext();
		try {
			v = new InstanceValidator(theWorkerContext, evaluationCtx);
		} catch (Exception e) {
			throw new ConfigurationException(e);
		}

		v.setBestPracticeWarningLevel(myBestPracticeWarningLevel);
		v.setAnyExtensionsAllowed(myAnyExtensionsAllowed);
		v.setResourceIdRule(IResourceValidator.IdStatus.OPTIONAL);
		v.setNoTerminologyChecks(myNoTerminologyChecks);
		v.setErrorForUnknownProfiles(myErrorForUnknownProfiles);
		v.getExtensionDomains().addAll(myExtensionDomains);

		List<ValidationMessage> messages = new ArrayList<>();

		ValidationProfileSet profileSet = new ValidationProfileSet();
		for (String next : theValidationContext.getOptions().getProfiles()) {
			profileSet.getCanonical().add(new ValidationProfileSet.ProfileRegistration(next, true));
		}

		String input = theValidationContext.getResourceAsString();
		EncodingEnum encoding = theValidationContext.getResourceAsStringEncoding();
		if (encoding == EncodingEnum.XML) {
			Document document;
			try {
				DocumentBuilder builder = myDocBuilderFactory.newDocumentBuilder();
				InputSource src = new InputSource(new StringReader(input));
				document = builder.parse(src);
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
				profileSet.getCanonical().add(new ValidationProfileSet.ProfileRegistration(nextProfile, true));
			}

			v.validate(null, messages, document, profileSet);

		} else if (encoding == EncodingEnum.JSON) {

			Gson gson = new GsonBuilder().create();
			JsonObject json = gson.fromJson(input, JsonObject.class);

			JsonObject meta = json.getAsJsonObject("meta");
			if (meta != null) {
				JsonElement profileElement = meta.get("profile");
				if (profileElement != null && profileElement.isJsonArray()) {
					JsonArray profiles = profileElement.getAsJsonArray();
					for (JsonElement element : profiles) {
						profileSet.getCanonical().add(new ValidationProfileSet.ProfileRegistration(element.getAsString(), true));
					}
				}
			}

			v.validate(null, messages, json, profileSet);

		} else {
			throw new IllegalArgumentException("Unknown encoding: " + encoding);
		}

		for (int i = 0; i < messages.size(); i++) {
			ValidationMessage next = messages.get(i);
			String message = next.getMessage();
			if ("Binding has no source, so can't be checked".equals(message) ||
				"ValueSet http://hl7.org/fhir/ValueSet/mimetypes not found".equals(message)) {
				messages.remove(i);
				i--;
			}
		}

		return messages;
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
