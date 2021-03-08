package ca.uhn.fhir.util;

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

	public TerserUtilHelper setField(String theField, String theValue) {
		IBase value = TerserUtil.newElement(myContext, "string", theValue);
		TerserUtil.setField(myContext, getTerser(), theField, myResource, value);
		return this;
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

	public FhirContext getContext() {
		return myContext;
	}

}
