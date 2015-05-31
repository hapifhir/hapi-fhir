package ca.uhn.fhir.validation;

import java.io.ByteArrayInputStream;
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
import org.hl7.fhir.utilities.xml.XMLUtil;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

public class InstanceValidator {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(InstanceValidator.class);

	private FhirContext myCtx;

	private DocumentBuilderFactory myDocBuilderFactory;

	InstanceValidator(FhirContext theContext) {
		myCtx = theContext;
		
	    myDocBuilderFactory = DocumentBuilderFactory.newInstance();
	    myDocBuilderFactory.setNamespaceAware(true);
	}

	public List<ValidationMessage> validate(String theInput, Class<? extends IBaseResource> theResourceType) {
		WorkerContext workerContext = new WorkerContext();
		org.hl7.fhir.instance.validation.InstanceValidator v;
		try {
			v = new org.hl7.fhir.instance.validation.InstanceValidator(workerContext);
		} catch (Exception e) {
			throw new ConfigurationException(e);
		}

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

		String profileCpName = "/org/hl7/fhir/instance/model/profile/" + myCtx.getResourceDefinition(theResourceType).getName().toLowerCase() + ".profile.xml";
		String profileText;
		try {
			profileText = IOUtils.toString(InstanceValidator.class.getResourceAsStream(profileCpName), "UTF-8");
		} catch (IOException e1) {
			throw new ConfigurationException("Failed to load profile from classpath: " + profileCpName, e1);
		}
		StructureDefinition profile = myCtx.newXmlParser().parseResource(StructureDefinition.class, profileText);

		try {
			List<ValidationMessage> results = v.validate(document, profile);
			return results;
		} catch (Exception e) {
			throw new InternalErrorException("Unexpected failure while validating resource", e);
		}
	}

}
