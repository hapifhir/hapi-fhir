package ca.uhn.fhir.validation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.OperationOutcome.Issue;
import ca.uhn.fhir.model.dstu.valueset.IssueSeverityEnum;

class SchemaBaseValidator implements IValidator {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SchemaBaseValidator.class);

	private Schema loadSchema(final IResource theResource, ValidationContext theValidationCtx) {
		Source baseSource = loadXml(theValidationCtx, theResource, null, "fhir-single.xsd");

		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		schemaFactory.setResourceResolver(new LSResourceResolver() {
			@Override
			public LSInput resolveResource(String theType, String theNamespaceURI, String thePublicId, String theSystemId, String theBaseURI) {
				if ("xml.xsd".equals(theSystemId) || "xhtml1-strict.xsd".equals(theSystemId)) {
					LSInputImpl input = new LSInputImpl();
					input.setPublicId(thePublicId);
					input.setSystemId(theSystemId);
					input.setBaseURI(theBaseURI);
					String pathToBase = theResource.getClass().getPackage().getName().replace('.', '/') + '/' + theSystemId;
					InputStream baseIs = ResourceValidator.class.getClassLoader().getResourceAsStream(pathToBase);
					if (baseIs == null) {
						throw new ValidationFailureException("No FHIR-BASE schema found");
					}

					ourLog.debug("Loading schema: {}", theSystemId);
					byte[] schema;
					try {
						schema = IOUtils.toByteArray(new InputStreamReader(baseIs, "UTF-8"));
					} catch (IOException e) {
						throw new ValidationFailureException("Failed to load schema " + theSystemId, e);
					}

					// Account for BOM in UTF-8 text (this seems to choke Java 6's built in XML reader)
					int offset = 0;
					if (schema[0] == (byte) 0xEF && schema[1] == (byte) 0xBB && schema[2] == (byte) 0xBF) {
						offset = 3;
					}

					try {
						input.setCharacterStream(new InputStreamReader(new ByteArrayInputStream(schema, offset, schema.length - offset), "UTF-8"));
					} catch (UnsupportedEncodingException e) {
						throw new ValidationFailureException("Failed to load schema " + theSystemId, e);
					}

					return input;

				}

				throw new ConfigurationException("Unknown schema: " + theBaseURI);
			}
		});

		Schema schema;
		try {
			schema = schemaFactory.newSchema(new Source[] { baseSource });
		} catch (SAXException e) {
			throw new ConfigurationException("Could not load/parse schema file", e);
		}
		return schema;
	}

	private Source loadXml(ValidationContext theCtx, IResource theResource, String theSystemId, String theSchemaName) {
		Class<? extends IResource> baseResourceClass = theCtx.getFhirContext().getResourceDefinition(theResource).getBaseDefinition().getImplementingClass();
		Package pack = baseResourceClass.getPackage();
		String pathToBase = pack.getName().replace('.', '/') + '/' + theSchemaName;
		InputStream baseIs = ResourceValidator.class.getClassLoader().getResourceAsStream(pathToBase);
		if (baseIs == null) {
			throw new ValidationFailureException("No FHIR-BASE schema found");
		}
		Source baseSource = new StreamSource(baseIs, theSystemId);
		return baseSource;
	}

	@Override
	public void validate(ValidationContext theContext) {
		Schema schema = loadSchema(theContext.getOperationOutcome(), theContext);
		try {
			Validator validator = schema.newValidator();
			ErrorHandler handler = new ErrorHandler(theContext);
			validator.setErrorHandler(handler);
			validator.validate(new StreamSource(new StringReader(theContext.getXmlEncodedResource())));
		} catch (SAXException e) {
			throw new ConfigurationException("Could not apply schema file", e);
		} catch (IOException e) {
			// This shouldn't happen since we're using a string source
			throw new ConfigurationException("Could not load/parse schema file", e);
		}
	}

	public class ErrorHandler implements org.xml.sax.ErrorHandler {

		private ValidationContext myContext;

		public ErrorHandler(ValidationContext theContext) {
			myContext = theContext;
		}

		@Override
		public void error(SAXParseException theException) throws SAXException {
			addIssue(theException, IssueSeverityEnum.ERROR);
		}

		@Override
		public void fatalError(SAXParseException theException) throws SAXException {
			addIssue(theException, IssueSeverityEnum.FATAL);
		}

		@Override
		public void warning(SAXParseException theException) throws SAXException {
			addIssue(theException, IssueSeverityEnum.WARNING);
		}

		private void addIssue(SAXParseException theException, IssueSeverityEnum severity) {
			Issue issue = myContext.getOperationOutcome().addIssue();
			issue.setSeverity(severity);
			issue.setDetails(theException.getLocalizedMessage());
			issue.addLocation().setValue("Line[" + theException.getLineNumber() + "] Col[" + theException.getColumnNumber() + "]");
		}

	}

}
