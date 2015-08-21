package ca.uhn.fhir.validation;

import static org.apache.commons.lang3.StringUtils.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.OperationOutcome.IssueSeverity;
import org.hl7.fhir.instance.model.StructureDefinition;
import org.hl7.fhir.instance.utils.WorkerContext;
import org.hl7.fhir.instance.validation.IResourceValidator.BestPracticeWarningLevel;
import org.hl7.fhir.instance.validation.ValidationMessage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

public class FhirInstanceValidator extends BaseValidatorBridge implements IValidatorModule {

	private static FhirContext ourHl7OrgCtx;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirInstanceValidator.class);
	private BestPracticeWarningLevel myBestPracticeWarningLevel;
	private DocumentBuilderFactory myDocBuilderFactory;

	public FhirInstanceValidator() {
		myDocBuilderFactory = DocumentBuilderFactory.newInstance();
		myDocBuilderFactory.setNamespaceAware(true);
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

	/**
	 * Returns the "best practice" warning level (default is {@link BestPracticeWarningLevel#Hint})
	 * 
	 * @see #setBestPracticeWarningLevel(BestPracticeWarningLevel) for more information on this value
	 */
	public BestPracticeWarningLevel getBestPracticeWarningLevel() {
		return myBestPracticeWarningLevel;
	}

	private FhirContext getHl7OrgDstu2Ctx(FhirContext theCtx) {
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

	private StructureDefinition loadProfileOrReturnNull(List<ValidationMessage> theMessages, FhirContext theCtx, String theResourceName) {
		if (isBlank(theResourceName)) {
			if (theMessages != null) {
				theMessages.add(new ValidationMessage().setLevel(IssueSeverity.FATAL).setMessage("Could not determine resource type from request. Content appears invalid."));
			}
			return null;
		}

		String profileCpName = "/org/hl7/fhir/instance/model/profile/" + theResourceName.toLowerCase() + ".profile.xml";
		String profileText;
		try {
			InputStream inputStream = FhirInstanceValidator.class.getResourceAsStream(profileCpName);
			if (inputStream == null) {
				if (theMessages != null) {
					theMessages.add(new ValidationMessage().setLevel(IssueSeverity.FATAL).setMessage("No profile found for resource type " + theResourceName));
				} else {
					return null;
				}
			}
			profileText = IOUtils.toString(inputStream, "UTF-8");
		} catch (IOException e1) {
			if (theMessages != null) {
				theMessages.add(new ValidationMessage().setLevel(IssueSeverity.FATAL).setMessage("No profile found for resource type " + theResourceName));
			}
			return null;
		}
		StructureDefinition profile = getHl7OrgDstu2Ctx(theCtx).newXmlParser().parseResource(StructureDefinition.class, profileText);
		return profile;
	}

	/**
	 * Sets the "best practice warning level". When validating, any deviations from best practices will be reported at
	 * this level. {@link BestPracticeWarningLevel#Ignore} means that best practice deviations will not be reported,
	 * {@link BestPracticeWarningLevel#Warning} means that best practice deviations will be reported as warnings, etc.
	 * Default is {@link BestPracticeWarningLevel#Hint}
	 * 
	 * @param theBestPracticeWarningLevel
	 *           The level, must not be <code>null</code>
	 */
	public void setBestPracticeWarningLevel(BestPracticeWarningLevel theBestPracticeWarningLevel) {
		Validate.notNull(theBestPracticeWarningLevel);
		myBestPracticeWarningLevel = theBestPracticeWarningLevel;
	}

	protected List<ValidationMessage> validate(final FhirContext theCtx, String theInput, EncodingEnum theEncoding) {
		WorkerContext workerContext = new HapiWorkerContext(theCtx);

		org.hl7.fhir.instance.validation.InstanceValidator v;
		try {
			v = new org.hl7.fhir.instance.validation.InstanceValidator(workerContext);
		} catch (Exception e) {
			throw new ConfigurationException(e);
		}

		v.setBestPracticeWarningLevel(myBestPracticeWarningLevel);

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

			String resourceName = determineResourceName(document);
			StructureDefinition profile = loadProfileOrReturnNull(messages, theCtx, resourceName);
			if (profile != null) {
				try {
					v.validate(messages, document, profile);
				} catch (Exception e) {
					throw new InternalErrorException("Unexpected failure while validating resource", e);
				}
			}
		} else if (theEncoding == EncodingEnum.JSON) {
			Gson gson = new GsonBuilder().create();
			JsonObject json = gson.fromJson(theInput, JsonObject.class);

			String resourceName = json.get("resourceType").getAsString();
			StructureDefinition profile = loadProfileOrReturnNull(messages, theCtx, resourceName);
			if (profile != null) {
				try {
					v.validate(messages, json, profile);
				} catch (Exception e) {
					throw new InternalErrorException("Unexpected failure while validating resource", e);
				}
			}
		} else {
			throw new IllegalArgumentException("Unknown encoding: " + theEncoding);
		}

		return messages;
	}

	@Override
	protected List<ValidationMessage> validate(IValidationContext<?> theCtx) {
		return validate(theCtx.getFhirContext(), theCtx.getResourceAsString(), theCtx.getResourceAsStringEncoding());
	}

	private final class HapiWorkerContext extends WorkerContext {
		private final FhirContext myCtx;

		private HapiWorkerContext(FhirContext theCtx) {
			myCtx = theCtx;
		}

		@Override
		public StructureDefinition getProfile(String theId) {
			StructureDefinition retVal = super.getProfile(theId);
			if (retVal == null) {
				if (theId.startsWith("http://hl7.org/fhir/StructureDefinition/")) {
					retVal = loadProfileOrReturnNull(null, getHl7OrgDstu2Ctx(myCtx), theId.substring("http://hl7.org/fhir/StructureDefinition/".length()));
					if (retVal != null) {
						seeProfile(theId, retVal);
					}
				}
			}
			return retVal;
		}
	}

}
