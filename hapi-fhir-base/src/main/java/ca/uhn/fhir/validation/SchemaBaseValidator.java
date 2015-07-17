package ca.uhn.fhir.validation;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

class SchemaBaseValidator implements IValidatorModule {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SchemaBaseValidator.class);
	private static final Set<String> SCHEMA_NAMES;

	static {
		HashSet<String> sn = new HashSet<String>();
		sn.add("xml.xsd");
		sn.add("xhtml1-strict.xsd");
		sn.add("fhir-single.xsd");
		sn.add("fhir-xhtml.xsd");
		sn.add("tombstone.xsd");
		sn.add("opensearch.xsd");
		sn.add("opensearchscore.xsd");
		sn.add("xmldsig-core-schema.xsd");
		SCHEMA_NAMES = Collections.unmodifiableSet(sn);
	}

	private Map<String, Schema> myKeyToSchema = new HashMap<String, Schema>();
	private FhirContext myCtx;

	SchemaBaseValidator(FhirContext theContext) {
		myCtx = theContext;
	}

	private void doValidate(IValidationContext<?> theContext, String schemaName) {
		Schema schema = loadSchema("dstu", schemaName);

		try {
			Validator validator = schema.newValidator();
			MyErrorHandler handler = new MyErrorHandler(theContext);
			validator.setErrorHandler(handler);
			String encodedResource;
			if (theContext.getResourceAsStringEncoding() == EncodingEnum.XML) {
				encodedResource = theContext.getResourceAsString();
			} else {
				encodedResource = theContext.getFhirContext().newXmlParser().encodeResourceToString((IBaseResource) theContext.getResource());
			}

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

			Source baseSource = loadXml(null, theSchemaName);

			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			schemaFactory.setResourceResolver(new MyResourceResolver());

			try {
				schema = schemaFactory.newSchema(new Source[] { baseSource });
			} catch (SAXException e) {
				throw new ConfigurationException("Could not load/parse schema file: " + theSchemaName, e);
			}
			myKeyToSchema.put(key, schema);
			return schema;
		}
	}

	private Source loadXml(String theSystemId, String theSchemaName) {
		String pathToBase = myCtx.getVersion().getPathToSchemaDefinitions() + '/' + theSchemaName;
		ourLog.debug("Going to load resource: {}", pathToBase);
		InputStream baseIs = FhirValidator.class.getResourceAsStream(pathToBase);
		if (baseIs == null) {
			throw new InternalErrorException("No FHIR-BASE schema found");
		}
		baseIs = new BOMInputStream(baseIs, false);
		InputStreamReader baseReader = new InputStreamReader(baseIs, Charset.forName("UTF-8"));
		Source baseSource = new StreamSource(baseReader, theSystemId);

		return baseSource;
	}

	@Override
	public void validateBundle(IValidationContext<Bundle> theContext) {
		if (myCtx.getVersion().getVersion().isNewerThan(FhirVersionEnum.DSTU1)) {
			doValidate(theContext, "fhir-single.xsd");
		} else {
			doValidate(theContext, "fhir-atom-single.xsd");
		}
	}

	@Override
	public void validateResource(IValidationContext<IBaseResource> theContext) {
		doValidate(theContext, "fhir-single.xsd");
	}

	private static class MyErrorHandler implements org.xml.sax.ErrorHandler {

		private IValidationContext<?> myContext;

		public MyErrorHandler(IValidationContext<?> theContext) {
			myContext = theContext;
		}

		private void addIssue(SAXParseException theException, ResultSeverityEnum theSeverity) {
			SingleValidationMessage message = new SingleValidationMessage();
			message.setLocationLine(theException.getLineNumber());
			message.setLocationCol(theException.getColumnNumber());
			message.setMessage(theException.getLocalizedMessage());
			message.setSeverity(theSeverity);
			myContext.addValidationMessage(message);
		}

		@Override
		public void error(SAXParseException theException) {
			addIssue(theException, ResultSeverityEnum.ERROR);
		}

		@Override
		public void fatalError(SAXParseException theException) {
			addIssue(theException, ResultSeverityEnum.FATAL);
		}

		@Override
		public void warning(SAXParseException theException) {
			addIssue(theException, ResultSeverityEnum.WARNING);
		}

	}

	private final class MyResourceResolver implements LSResourceResolver {
		private MyResourceResolver() {
		}

		@Override
		public LSInput resolveResource(String theType, String theNamespaceURI, String thePublicId, String theSystemId, String theBaseURI) {
			if (theSystemId != null && SCHEMA_NAMES.contains(theSystemId)) {
				LSInputImpl input = new LSInputImpl();
				input.setPublicId(thePublicId);
				input.setSystemId(theSystemId);
				input.setBaseURI(theBaseURI);
				// String pathToBase = "ca/uhn/fhir/model/" + myVersion + "/schema/" + theSystemId;
				String pathToBase = myCtx.getVersion().getPathToSchemaDefinitions() + '/' + theSystemId;

				ourLog.debug("Loading referenced schema file: " + pathToBase);

				InputStream baseIs = FhirValidator.class.getResourceAsStream(pathToBase);
				if (baseIs == null) {
					throw new InternalErrorException("Schema file not found: " + pathToBase);
				}

				input.setByteStream(baseIs);

				return input;

			}

			throw new ConfigurationException("Unknown schema: " + theSystemId);
		}
	}

}
