package ca.uhn.fhir.validation;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.instance.model.OperationOutcome.IssueSeverity;
import org.hl7.fhir.instance.model.StructureDefinition;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.utils.WorkerContext;
import org.hl7.fhir.instance.validation.ValidationMessage;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

public class FhirInstanceValidator implements IValidator {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirInstanceValidator.class);

	private DocumentBuilderFactory myDocBuilderFactory;

	FhirInstanceValidator() {
		myDocBuilderFactory = DocumentBuilderFactory.newInstance();
		myDocBuilderFactory.setNamespaceAware(true);
	}

	List<ValidationMessage> validate(FhirContext theCtx, String theInput, EncodingEnum theEncoding, String theResourceName) {
		WorkerContext workerContext = new WorkerContext();
		org.hl7.fhir.instance.validation.InstanceValidator v;
		try {
			v = new org.hl7.fhir.instance.validation.InstanceValidator(workerContext);
		} catch (Exception e) {
			throw new ConfigurationException(e);
		}

		String profileCpName = "/org/hl7/fhir/instance/model/profile/" + theResourceName.toLowerCase() + ".profile.xml";
		String profileText;
		try {
			profileText = IOUtils.toString(FhirInstanceValidator.class.getResourceAsStream(profileCpName), "UTF-8");
		} catch (IOException e1) {
			throw new ConfigurationException("Failed to load profile from classpath: " + profileCpName, e1);
		}
		StructureDefinition profile = theCtx.newXmlParser().parseResource(StructureDefinition.class, profileText);

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
			try {
				List<ValidationMessage> results = v.validate(document, profile);
				return results;
			} catch (Exception e) {
				throw new InternalErrorException("Unexpected failure while validating resource", e);
			}
		} else if (theEncoding == EncodingEnum.JSON) {
			Gson gson = new GsonBuilder().create();
			JsonObject json = gson.fromJson(theInput, JsonObject.class);
			try {
				return v.validate(json, profile);
			} catch (Exception e) {
				throw new InternalErrorException("Unexpected failure while validating resource", e);
			}
		} else {
			throw new IllegalArgumentException("Unknown encoding: " + theEncoding);
		}

	}

	@Override
	public void validateResource(IValidationContext<IBaseResource> theCtx) {
		String resourceName = theCtx.getResourceName();
		String resourceBody = theCtx.getResourceAsString();
		validate(theCtx.getFhirContext(), resourceBody, theCtx.getResourceAsStringEncoding(), resourceName);
	}

	@Override
	public void validateBundle(IValidationContext<Bundle> theContext) {
		// nothing for now
	}

}
