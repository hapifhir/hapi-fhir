package ca.uhn.fhir.util;

/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;

/**
 * Wrapper class holding context-related instances, and the resource being operated on.
 */
public class TerserUtilHelper {

	public static TerserUtilHelper newHelper(FhirContext theFhirContext, String theResource) {
		return newHelper(theFhirContext, (IBaseResource) TerserUtil.newResource(theFhirContext, theResource));
	}

	public static TerserUtilHelper newHelper(FhirContext theFhirContext, IBaseResource theResource) {
		TerserUtilHelper retVal = new TerserUtilHelper(theFhirContext, theResource);
		return retVal;
	}

	FhirContext myContext;
	FhirTerser myTerser;
	IBaseResource myResource;

	protected TerserUtilHelper(FhirContext theFhirContext, IBaseResource theResource) {
		myContext = theFhirContext;
		myResource = theResource;
	}

	/**
	 * Sets string field at the specified FHIR path
	 *
	 * @param theField The FHIR Path to set the values at
	 * @param theValue The string value to be set
	 * @return Returns current instance
	 */
	public TerserUtilHelper setField(String theField, String theValue) {
		IBase value = newStringElement(theValue);
		TerserUtil.setFieldByFhirPath(myContext, getTerser(), theField, myResource, value);
		return this;
	}

	protected IBase newStringElement(String theValue) {
		IBase value = TerserUtil.newElement(myContext, "string", theValue);
		return value;
	}

	public List<IBase> getFieldValues(String theField) {
		return TerserUtil.getValues(myContext, myResource, theField);
	}

	public FhirTerser getTerser() {
		if (myTerser == null) {
			myTerser = myContext.newTerser();
		}
		return myTerser;
	}

	/**
	 * Gets resource that this helper operates on
	 *
	 * @param <T> Instance type of the resource
	 * @return Returns the resources
	 */
	public <T extends IBaseResource> T getResource() {
		return (T) myResource;
	}

	/**
	 * Gets runtime definition for the resource
	 *
	 * @return Returns resource definition.
	 */
	public RuntimeResourceDefinition getResourceDefinition() {
		return myContext.getResourceDefinition(myResource);
	}

	public IBase newElement(String theElementName) {
		return TerserUtil.newElement(myContext, theElementName);
	}

	public FhirContext getContext() {
		return myContext;
	}

}
