package ca.uhn.fhir.validation.schematron;

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
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.IValidationContext;
import ca.uhn.fhir.validation.IValidatorModule;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhir.validation.SchemaBaseValidator;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationContext;
import com.helger.commons.error.IError;
import com.helger.commons.error.list.IErrorList;
import com.helger.schematron.ISchematronResource;
import com.helger.schematron.SchematronHelper;
import com.helger.schematron.svrl.jaxb.SchematronOutputType;
import com.helger.schematron.xslt.SchematronResourceSCH;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * This class is only used using reflection from {@link SchematronProvider} in order
 * to be truly optional.
 */
public class SchematronBaseValidator implements IValidatorModule {

	private static final Logger ourLog = LoggerFactory.getLogger(SchematronBaseValidator.class);
	private final Map<Class<? extends IBaseResource>, ISchematronResource> myClassToSchematron = new HashMap<>();
	private FhirContext myCtx;

	/**
	 * Constructor
	 */
	public SchematronBaseValidator(FhirContext theContext) {
		myCtx = theContext;
	}

	@Override
	public void validateResource(IValidationContext<IBaseResource> theCtx) {

		if (theCtx.getResource() instanceof IBaseBundle) {
			IBaseBundle bundle = (IBaseBundle) theCtx.getResource();
			List<IBaseResource> subResources = BundleUtil.toListOfResources(myCtx, bundle);
			for (IBaseResource nextSubResource : subResources) {
				validateResource(ValidationContext.subContext(theCtx, nextSubResource, theCtx.getOptions()));
			}
		}

		ISchematronResource sch = getSchematron(theCtx);
		String resourceAsString;
		if (theCtx.getResourceAsStringEncoding() == EncodingEnum.XML) {
			resourceAsString = theCtx.getResourceAsString();
		} else {
			resourceAsString = theCtx.getFhirContext().newXmlParser().encodeResourceToString(theCtx.getResource());
		}
		StreamSource source = new StreamSource(new StringReader(resourceAsString));

		SchematronOutputType results = SchematronHelper.applySchematron(sch, source);
		if (results == null) {
			return;
		}

		IErrorList errors = SchematronHelper.convertToErrorList(results, theCtx.getFhirContext().getResourceDefinition(theCtx.getResource()).getBaseDefinition().getName());

		if (errors.getAllErrors().containsOnlySuccess()) {
			return;
		}

		for (IError next : errors) {
			ResultSeverityEnum severity;
			if (next.isFailure()) {
				severity = ResultSeverityEnum.ERROR;
			} else if (next.isError()) {
				severity = ResultSeverityEnum.FATAL;
			} else if (next.isNoError()) {
				severity = ResultSeverityEnum.WARNING;
			} else {
				continue;
			}

			String details = next.getAsString(Locale.getDefault());

			SingleValidationMessage message = new SingleValidationMessage();
			message.setMessage(details);
			message.setLocationLine(next.getErrorLocation().getLineNumber());
			message.setLocationCol(next.getErrorLocation().getColumnNumber());
			message.setLocationString(next.getErrorLocation().getAsString());
			message.setSeverity(severity);
			theCtx.addValidationMessage(message);
		}

	}

	private ISchematronResource getSchematron(IValidationContext<IBaseResource> theCtx) {
		Class<? extends IBaseResource> resource = theCtx.getResource().getClass();
		Class<? extends IBaseResource> baseResourceClass = theCtx.getFhirContext().getResourceDefinition(resource).getBaseDefinition().getImplementingClass();

		return getSchematronAndCache(theCtx, baseResourceClass);
	}

	private ISchematronResource getSchematronAndCache(IValidationContext<IBaseResource> theCtx, Class<? extends IBaseResource> theClass) {
		synchronized (myClassToSchematron) {
			ISchematronResource retVal = myClassToSchematron.get(theClass);
			if (retVal != null) {
				return retVal;
			}

			String pathToBase = myCtx.getVersion().getPathToSchemaDefinitions() + '/' + theCtx.getFhirContext().getResourceDefinition(theCtx.getResource()).getBaseDefinition().getName().toLowerCase()
				+ ".sch";
			try (InputStream baseIs = FhirValidator.class.getResourceAsStream(pathToBase)) {
				if (baseIs == null) {
					throw new InternalErrorException(Msg.code(1972) + "Failed to load schematron for resource '" + theCtx.getFhirContext().getResourceDefinition(theCtx.getResource()).getBaseDefinition().getName() + "'. "
						+ SchemaBaseValidator.RESOURCES_JAR_NOTE);
				}
			} catch (IOException e) {
				ourLog.error("Failed to close stream", e);
			}

			retVal = SchematronResourceSCH.fromClassPath(pathToBase);
			myClassToSchematron.put(theClass, retVal);
			return retVal;
		}
	}
}
