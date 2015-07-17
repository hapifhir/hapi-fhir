package ca.uhn.fhir.validation;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.instance.model.OperationOutcome.IssueSeverity;
import org.hl7.fhir.instance.model.StructureDefinition;
import org.hl7.fhir.instance.utils.WorkerContext;
import org.hl7.fhir.instance.validation.ValidationMessage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class FhirInstanceValidator extends BaseValidatorBridge implements IValidatorModule {

	static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirInstanceValidator.class);

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

	private StructureDefinition loadProfileOrReturnNull(List<ValidationMessage> theMessages, FhirContext theCtx, String theResourceName) {
		if (isBlank(theResourceName)) {
			theMessages.add(new ValidationMessage().setLevel(IssueSeverity.FATAL).setMessage("Could not determine resource type from request. Content appears invalid."));
			return null;
		}

		String profileCpName = "/org/hl7/fhir/instance/model/profile/" + theResourceName.toLowerCase() + ".profile.xml";
		String profileText;
		try {
			profileText = IOUtils.toString(FhirInstanceValidator.class.getResourceAsStream(profileCpName), "UTF-8");
		} catch (IOException e1) {
			theMessages.add(new ValidationMessage().setLevel(IssueSeverity.FATAL).setMessage("No profile found for resource type " + theResourceName));
			return null;
		}
		StructureDefinition profile = theCtx.newXmlParser().parseResource(StructureDefinition.class, profileText);
		return profile;
	}

	protected List<ValidationMessage> validate(FhirContext theCtx, String theInput, EncodingEnum theEncoding) {
		WorkerContext workerContext = new WorkerContext();
		org.hl7.fhir.instance.validation.InstanceValidator v;
		try {
			v = new org.hl7.fhir.instance.validation.InstanceValidator(workerContext);
		} catch (Exception e) {
			throw new ConfigurationException(e);
		}

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

}
