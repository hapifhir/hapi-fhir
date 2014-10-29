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

import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.xml.transform.stream.StreamSource;

import org.oclc.purl.dsdl.svrl.SchematronOutputType;

import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.base.resource.BaseOperationOutcome.BaseIssue;
import ca.uhn.fhir.model.dstu.valueset.IssueSeverityEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

import com.phloc.commons.error.IResourceError;
import com.phloc.commons.error.IResourceErrorGroup;
import com.phloc.schematron.ISchematronResource;
import com.phloc.schematron.SchematronHelper;
import com.phloc.schematron.xslt.SchematronResourceSCH;

public class SchematronBaseValidator implements IValidator {

	private Map<Class<? extends IResource>, ISchematronResource> myClassToSchematron = new HashMap<Class<? extends IResource>, ISchematronResource>();

	@Override
	public void validateResource(ValidationContext<IResource> theCtx) {

		ISchematronResource sch = getSchematron(theCtx);
		StreamSource source = new StreamSource(new StringReader(theCtx.getXmlEncodedResource()));

		SchematronOutputType results = SchematronHelper.applySchematron(sch, source);
		if (results == null) {
			return;
		}

		IResourceErrorGroup errors = SchematronHelper.convertToResourceErrorGroup(results, theCtx.getFhirContext().getResourceDefinition(theCtx.getResource()).getBaseDefinition().getName());

		if (errors.getAllErrors().containsOnlySuccess()) {
			return;
		}

		for (IResourceError next : errors.getAllErrors().getAllResourceErrors()) {
			BaseIssue issue = theCtx.getOperationOutcome().addIssue();
			switch (next.getErrorLevel()) {
			case ERROR:
				issue.getSeverityElement().setValue("error");
				break;
			case FATAL_ERROR:
				issue.getSeverityElement().setValue("fatal");
				break;
			case WARN:
				issue.getSeverityElement().setValue("warning");
				break;
			case INFO:
			case SUCCESS:
				continue;
			}

			issue.getDetailsElement().setValue(next.getAsString(Locale.getDefault()));
		}

	}

	private ISchematronResource getSchematron(ValidationContext<IResource> theCtx) {
		Class<? extends IResource> resource = theCtx.getResource().getClass();
		Class<? extends IResource> baseResourceClass = theCtx.getFhirContext().getResourceDefinition(resource).getBaseDefinition().getImplementingClass();

		return getSchematronAndCache(theCtx, "dstu", baseResourceClass);
	}

	private ISchematronResource getSchematronAndCache(ValidationContext<IResource> theCtx, String theVersion, Class<? extends IResource> theClass) {
		synchronized (myClassToSchematron) {
			ISchematronResource retVal = myClassToSchematron.get(theClass);
			if (retVal != null) {
				return retVal;
			}

			String pathToBase = "ca/uhn/fhir/model/" + theVersion + "/schema/" + theCtx.getFhirContext().getResourceDefinition(theCtx.getResource()).getBaseDefinition().getName().toLowerCase()
					+ ".sch";
			InputStream baseIs = FhirValidator.class.getClassLoader().getResourceAsStream(pathToBase);
			if (baseIs == null) {
				throw new InternalErrorException("No schematron found for resource type: "
						+ theCtx.getFhirContext().getResourceDefinition(theCtx.getResource()).getBaseDefinition().getImplementingClass().getCanonicalName());
			}

			retVal = SchematronResourceSCH.fromClassPath(pathToBase);
			myClassToSchematron.put(theClass, retVal);
			return retVal;
		}
	}

	@Override
	public void validateBundle(ValidationContext<Bundle> theContext) {
		for (BundleEntry next : theContext.getResource().getEntries()) {
			if (next.getResource() != null) {
				ValidationContext<IResource> ctx = ValidationContext.newChild(theContext, next.getResource());
				validateResource(ctx);
			}
		}
	}

}
