package ca.uhn.fhir.validation;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.util.ClasspathUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SchemaBaseValidator implements IValidatorModule {
	public static final String RESOURCES_JAR_NOTE = "Note that as of HAPI FHIR 1.2, DSTU2 validation files are kept in a separate JAR (hapi-fhir-validation-resources-XXX.jar) which must be added to your classpath. See the HAPI FHIR download page for more information.";

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SchemaBaseValidator.class);
	private static final Set<String> SCHEMA_NAMES;
	private static boolean ourJaxp15Supported;

	static {
		HashSet<String> sn = new HashSet<>();
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

	private final Map<String, Schema> myKeyToSchema = new HashMap<>();
	private FhirContext myCtx;

	public SchemaBaseValidator(FhirContext theContext) {
		myCtx = theContext;
	}

	private void doValidate(IValidationContext<?> theContext) {
		Schema schema = loadSchema();

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

			try {
				/*
				 * See https://github.com/hapifhir/hapi-fhir/issues/339
				 * https://www.owasp.org/index.php/XML_External_Entity_(XXE)_Processing
				 */
				validator.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
				validator.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
			} catch (SAXNotRecognizedException ex) {
				ourLog.debug("Jaxp 1.5 Support not found.", ex);
			}

			validator.validate(new StreamSource(new StringReader(encodedResource)));
		} catch (SAXParseException e) {
			SingleValidationMessage message = new SingleValidationMessage();
			message.setLocationLine(e.getLineNumber());
			message.setLocationCol(e.getColumnNumber());
			message.setMessage(e.getLocalizedMessage());
			message.setSeverity(ResultSeverityEnum.FATAL);
			theContext.addValidationMessage(message);
		} catch (SAXException | IOException e) {
			// Catch all
			throw new ConfigurationException(Msg.code(1967) + "Could not load/parse schema file", e);
		}
	}

	private Schema loadSchema() {
		String key = "fhir-single.xsd";

		synchronized (myKeyToSchema) {
			Schema schema = myKeyToSchema.get(key);
			if (schema != null) {
				return schema;
			}

			Source baseSource = loadXml("fhir-single.xsd");

			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			schemaFactory.setResourceResolver(new MyResourceResolver());

			try {
				try {
					/*
					 * See https://github.com/hapifhir/hapi-fhir/issues/339
					 * https://www.owasp.org/index.php/XML_External_Entity_(XXE)_Processing
					 */
					schemaFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
					ourJaxp15Supported = true;
				} catch (SAXNotRecognizedException e) {
					ourJaxp15Supported = false;
					ourLog.warn("Jaxp 1.5 Support not found.", e);
				}
				schema = schemaFactory.newSchema(new Source[]{baseSource});
			} catch (SAXException e) {
				throw new ConfigurationException(Msg.code(1968) + "Could not load/parse schema file: " + "fhir-single.xsd", e);
			}
			myKeyToSchema.put(key, schema);
			return schema;
		}
	}

	Source loadXml(String theSchemaName) {
		String pathToBase = myCtx.getVersion().getPathToSchemaDefinitions() + '/' + theSchemaName;
		ourLog.debug("Going to load resource: {}", pathToBase);

		String contents = ClasspathUtil.loadResource(pathToBase, ClasspathUtil.withBom());
		return new StreamSource(new StringReader(contents), null);
	}

	@Override
	public void validateResource(IValidationContext<IBaseResource> theContext) {
		doValidate(theContext);
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
				String pathToBase = myCtx.getVersion().getPathToSchemaDefinitions() + '/' + theSystemId;

				ourLog.debug("Loading referenced schema file: " + pathToBase);

				byte[] bytes = ClasspathUtil.loadResourceAsByteArray(pathToBase);
				input.setByteStream(new ByteArrayInputStream(bytes));
				return input;

			}

			throw new ConfigurationException(Msg.code(1969) + "Unknown schema: " + theSystemId);
		}
	}

	private static class MyErrorHandler implements org.xml.sax.ErrorHandler {

		private IValidationContext<?> myContext;

		MyErrorHandler(IValidationContext<?> theContext) {
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

	public static boolean isJaxp15Supported() {
		return ourJaxp15Supported;
	}

}
