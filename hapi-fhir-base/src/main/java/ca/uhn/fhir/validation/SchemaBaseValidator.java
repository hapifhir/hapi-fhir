package ca.uhn.fhir.validation;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.OperationOutcome.Issue;
import ca.uhn.fhir.model.dstu.valueset.IssueSeverityEnum;

class SchemaBaseValidator implements IValidator {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SchemaBaseValidator.class);
	private static final Set<String> SCHEMA_NAMES;

	static {
		HashSet<String> sn = new HashSet<String>();
		sn.add("xml.xsd");
		sn.add("xhtml1-strict.xsd");
		sn.add("fhir-single.xsd");
		sn.add("tombstone.xsd");
		sn.add("opensearch.xsd");
		sn.add("opensearchscore.xsd");
		sn.add("xmldsig-core-schema.xsd");
		SCHEMA_NAMES = Collections.unmodifiableSet(sn);
	}

	private Map<String, Schema> myKeyToSchema = new HashMap<String, Schema>();

	public void validateBundle(ValidationContext<Bundle> theContext) {
		doValidate(theContext, "fhir-atom-single.xsd");
	}

	@Override
	public void validateResource(ValidationContext<IResource> theContext) {
		doValidate(theContext, "fhir-single.xsd");
	}

	private void doValidate(ValidationContext<?> theContext, String schemaName) {
		Schema schema = loadSchema("dstu", schemaName);

		try {
			Validator validator = schema.newValidator();
			MyErrorHandler handler = new MyErrorHandler(theContext);
			validator.setErrorHandler(handler);
			validator.validate(new StreamSource(new StringReader(theContext.getXmlEncodedResource())));
		} catch (SAXException e) {
			throw new ConfigurationException("Could not apply schema file", e);
		} catch (IOException e) {
			// This shouldn't happen since we're using a string source
			throw new ConfigurationException("Could not load/parse schema file", e);
		}
	}

	private Schema loadSchema(String theVersion, String theSchemaName) {
		String key = theVersion + "-" + theSchemaName;

		synchronized (myKeyToSchema) {
			Schema schema = myKeyToSchema.get(key);
			if (schema != null) {
				return schema;
			}

			Source baseSource = loadXml("dstu", null, theSchemaName);

			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			schemaFactory.setResourceResolver(new MyResourceResolver("dstu"));

			try {
				schema = schemaFactory.newSchema(new Source[] { baseSource });
			} catch (SAXException e) {
				throw new ConfigurationException("Could not load/parse schema file", e);
			}
			myKeyToSchema.put(key, schema);
			return schema;
		}
	}

	private Source loadXml(String theVersion, String theSystemId, String theSchemaName) {
		String pathToBase = "ca/uhn/fhir/model/" + theVersion + "/schema/" + theSchemaName;
		InputStream baseIs = FhirValidator.class.getClassLoader().getResourceAsStream(pathToBase);
		if (baseIs == null) {
			throw new ValidationFailureException("No FHIR-BASE schema found");
		}
		Source baseSource = new StreamSource(baseIs, theSystemId);
		return baseSource;
	}

	private static class MyErrorHandler implements org.xml.sax.ErrorHandler {

		private ValidationContext<?> myContext;

		public MyErrorHandler(ValidationContext<?> theContext) {
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

	private static final class MyResourceResolver implements LSResourceResolver {
		private String myVersion;

		private MyResourceResolver(String theVersion) {
			myVersion = theVersion;
		}

		@Override
		public LSInput resolveResource(String theType, String theNamespaceURI, String thePublicId, String theSystemId, String theBaseURI) {
			if (theSystemId != null && SCHEMA_NAMES.contains(theSystemId)) {
				LSInputImpl input = new LSInputImpl();
				input.setPublicId(thePublicId);
				input.setSystemId(theSystemId);
				input.setBaseURI(theBaseURI);
				String pathToBase = "ca/uhn/fhir/model/" + myVersion + "/schema/" + theSystemId;
				InputStream baseIs = FhirValidator.class.getClassLoader().getResourceAsStream(pathToBase);
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
	}

}
