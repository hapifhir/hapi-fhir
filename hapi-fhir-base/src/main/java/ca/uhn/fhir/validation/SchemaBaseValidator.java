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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.Charset;
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

import org.apache.commons.io.input.BOMInputStream;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.base.resource.BaseOperationOutcome.BaseIssue;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

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

	private void doValidate(ValidationContext<?> theContext, String schemaName) {
		Schema schema = loadSchema("dstu", schemaName);

		try {
			Validator validator = schema.newValidator();
			MyErrorHandler handler = new MyErrorHandler(theContext);
			validator.setErrorHandler(handler);
			String encodedResource = theContext.getXmlEncodedResource();

			// ourLog.info(new FhirContext().newXmlParser().setPrettyPrint(true).encodeBundleToString((Bundle) theContext.getResource()));

			validator.validate(new StreamSource(new StringReader(encodedResource)));
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
		ourLog.debug("Going to load resource: {}", pathToBase);
		InputStream baseIs = FhirValidator.class.getClassLoader().getResourceAsStream(pathToBase);
		if (baseIs == null) {
			throw new InternalErrorException("No FHIR-BASE schema found");
		}
		 baseIs = new BOMInputStream(baseIs, false);
		 InputStreamReader baseReader = new InputStreamReader(baseIs, Charset.forName("UTF-8"));
		 Source baseSource = new StreamSource(baseReader, theSystemId);

//		String schema;
//		try {
//			schema = IOUtils.toString(baseIs, Charset.forName("UTF-8"));
//		} catch (IOException e) {
//			throw new InternalErrorException(e);
//		}
//
//		ourLog.info("Schema is:\n{}", schema);
//		
//		Source baseSource = new StreamSource(new StringReader(schema), theSystemId);
//		Source baseSource = new StreamSource(baseIs, theSystemId);
		return baseSource;
	}

	@Override
	public void validateBundle(ValidationContext<Bundle> theContext) {
		doValidate(theContext, "fhir-atom-single.xsd");
	}

	@Override
	public void validateResource(ValidationContext<IResource> theContext) {
		doValidate(theContext, "fhir-single.xsd");
	}

	private static class MyErrorHandler implements org.xml.sax.ErrorHandler {

		private ValidationContext<?> myContext;

		public MyErrorHandler(ValidationContext<?> theContext) {
			myContext = theContext;
		}

		private void addIssue(SAXParseException theException, String severity) {
			BaseIssue issue = myContext.getOperationOutcome().addIssue();
			issue.getSeverityElement().setValue(severity);
			issue.getDetailsElement().setValue(theException.getLocalizedMessage());
			issue.addLocation("Line[" + theException.getLineNumber() + "] Col[" + theException.getColumnNumber() + "]");
		}

		@Override
		public void error(SAXParseException theException) throws SAXException {
			addIssue(theException, "error");
		}

		@Override
		public void fatalError(SAXParseException theException) throws SAXException {
			addIssue(theException, "fatal");
		}

		@Override
		public void warning(SAXParseException theException) throws SAXException {
			addIssue(theException, "warning");
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
				
				ourLog.debug("Loading referenced schema file: " + pathToBase);
				
				InputStream baseIs = FhirValidator.class.getClassLoader().getResourceAsStream(pathToBase);
				if (baseIs == null) {
					throw new InternalErrorException("No FHIR-BASE schema found");
				}

				input.setByteStream(baseIs);
				
				return input;

			}

			throw new ConfigurationException("Unknown schema: " + theBaseURI);
		}
	}

}
