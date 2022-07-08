package ca.uhn.fhir.util;

/*-
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;

/**
 * Wrapper class holding context-related instances, and the resource being operated on. Sample use case is
 *
 * <pre>{@code
 * TerserUtilHelper helper = TerserUtilHelper.newHelper(ourFhirContext, "Patient");
 * helper.setField("identifier.system", "http://org.com/sys");
 * helper.setField("identifier.value", "123");
 * ...
 * Patient patient = helper.getResource();
 * }</pre>
 */
public class TerserUtilHelper {

	/**
	 * Factory method for creating a new instance of the wrapper
	 *
	 * @param theFhirContext  FHIR Context to be used for all further operations
	 * @param theResourceName Name of the resource type
	 * @return Returns a new helper instance
	 */
	public static TerserUtilHelper newHelper(FhirContext theFhirContext, String theResourceName) {
		return newHelper(theFhirContext, (IBaseResource) TerserUtil.newResource(theFhirContext, theResourceName));
	}

	/**
	 * Factory method for creating a new instance of the wrapper
	 *
	 * @param theFhirContext FHIR Context to be used for all further operations
	 * @param theResource    The resource to operate on
	 * @return Returns a new helper instance
	 */
	public static TerserUtilHelper newHelper(FhirContext theFhirContext, IBaseResource theResource) {
		TerserUtilHelper retVal = new TerserUtilHelper(theFhirContext, theResource);
		return retVal;
	}

	private FhirContext myContext;
	private FhirTerser myTerser;
	private IBaseResource myResource;

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
		TerserUtil.setFieldByFhirPath(getTerser(), theField, myResource, value);
		return this;
	}

	/**
	 * Sets field at the specified FHIR path
	 *
	 * @param theField The FHIR Path to set the values at
	 * @param theValue The value to be set
	 * @return Returns current instance
	 */
	public TerserUtilHelper setField(String theField, String theFieldType, Object theValue) {
		IBase value = newElement(theFieldType, theValue);
		TerserUtil.setFieldByFhirPath(getTerser(), theField, myResource, value);
		return this;
	}

	protected IBase newStringElement(String theValue) {
		return newElement("string", theValue);
	}

	protected IBase newElement(String theElementType, Object theValue) {
		IBase value = TerserUtil.newElement(myContext, theElementType, theValue);
		return value;
	}

	/**
	 * Gets values for the specified child field.
	 *
	 * @param theField The field to get values from
	 * @return Returns a list of retrieved values or null if the specified field doesn't exist
	 */
	public List<IBase> getFieldValues(String theField) {
		return TerserUtil.getValues(myContext, myResource, theField);
	}

	/**
	 * Gets values for the specified field values by FHIRPath.
	 *
	 * @param theFhirPath The FHIR path expression to get the values from
	 * @return Returns a collection of values or null if the specified field doesn't exist
	 */
	public List<IBase> getFieldValuesByFhirPath(String theFhirPath) {
		return TerserUtil.getFieldByFhirPath(myContext, theFhirPath, myResource);
	}

	/**
	 * Gets first available value for the specified field values by FHIRPath.
	 *
	 * @param theFhirPath The FHIR path expression to get the values from
	 * @return Returns the value or null if the specified field doesn't exist or is empty
	 */
	public IBase getFieldValueByFhirPath(String theFhirPath) {
		return TerserUtil.getFirstFieldByFhirPath(myContext, theFhirPath, myResource);
	}

	/**
	 * Gets first available values of the specified field.
	 *
	 * @param theField The field to get values from
	 * @return Returns the first available value for the field name or null if the
	 * specified field doesn't exist or has no values
	 */
	public IBase getFieldValue(String theField) {
		List<IBase> values = getFieldValues(theField);
		if (values == null || values.isEmpty()) {
			return null;
		}
		return values.get(0);
	}

	/**
	 * Gets the terser instance, creating one if necessary.
	 *
	 * @return Returns the terser
	 */
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

	/**
	 * Creates a new element
	 *
	 * @param theElementName Name of the element to create
	 * @return Returns a new element
	 */
	public IBase newElement(String theElementName) {
		return TerserUtil.newElement(myContext, theElementName);
	}

	/**
	 * Gets context holding resource definition.
	 *
	 * @return Returns the current FHIR context.
	 */
	public FhirContext getContext() {
		return myContext;
	}

}
