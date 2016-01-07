package ca.uhn.fhir.validation.schematron;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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

import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.oclc.purl.dsdl.svrl.SchematronOutputType;

import com.phloc.commons.error.IResourceError;
import com.phloc.commons.error.IResourceErrorGroup;
import com.phloc.schematron.ISchematronResource;
import com.phloc.schematron.SchematronHelper;
import com.phloc.schematron.xslt.SchematronResourceSCH;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.IValidationContext;
import ca.uhn.fhir.validation.IValidatorModule;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhir.validation.SchemaBaseValidator;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationContext;

/**
 * This class is only used using reflection from {@link SchematronProvider} in order
 * to be truly optional.
 */
public class SchematronBaseValidator implements IValidatorModule {

	private Map<Class<? extends IBaseResource>, ISchematronResource> myClassToSchematron = new HashMap<Class<? extends IBaseResource>, ISchematronResource>();
	private FhirContext myCtx;

	public SchematronBaseValidator(FhirContext theContext) {
		myCtx = theContext;
	}

	@Override
	public void validateResource(IValidationContext<IBaseResource> theCtx) {

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

		IResourceErrorGroup errors = SchematronHelper.convertToResourceErrorGroup(results, theCtx.getFhirContext().getResourceDefinition(theCtx.getResource()).getBaseDefinition().getName());

		if (errors.getAllErrors().containsOnlySuccess()) {
			return;
		}

		for (IResourceError next : errors.getAllErrors().getAllResourceErrors()) {
			ResultSeverityEnum severity;
			switch (next.getErrorLevel()) {
			case ERROR:
				severity = ResultSeverityEnum.ERROR;
				break;
			case FATAL_ERROR:
				severity = ResultSeverityEnum.FATAL;
				break;
			case WARN:
				severity = ResultSeverityEnum.WARNING;
				break;
			case INFO:
			case SUCCESS:
			default:
				continue;
			}

			String details = next.getAsString(Locale.getDefault());

			SingleValidationMessage message = new SingleValidationMessage();
			message.setMessage(details);
			message.setLocationLine(next.getLocation().getLineNumber());
			message.setLocationCol(next.getLocation().getColumnNumber());
			message.setLocationString(next.getLocation().getAsString());
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

			String pathToBase = myCtx.getVersion().getPathToSchemaDefinitions() + '/' + theCtx.getFhirContext().getResourceDefinition(theCtx.getResource()).getBaseDefinition().getName().toLowerCase() + ".sch";
			InputStream baseIs = FhirValidator.class.getResourceAsStream(pathToBase);
			try {
				if (baseIs == null) {
					throw new InternalErrorException("Failed to load schematron for resource '" + theCtx.getFhirContext().getResourceDefinition(theCtx.getResource()).getBaseDefinition().getName() + "'. " + SchemaBaseValidator.RESOURCES_JAR_NOTE);
				}
			} finally {
				IOUtils.closeQuietly(baseIs);
			}

			retVal = SchematronResourceSCH.fromClassPath(pathToBase);
			myClassToSchematron.put(theClass, retVal);
			return retVal;
		}
	}

	@Override
	public void validateBundle(IValidationContext<Bundle> theContext) {
		for (BundleEntry next : theContext.getResource().getEntries()) {
			if (next.getResource() != null) {
				IValidationContext<IBaseResource> ctx = ValidationContext.newChild(theContext, next.getResource());
				validateResource(ctx);
			}
		}
	}

}
