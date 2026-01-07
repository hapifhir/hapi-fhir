package ca.uhn.fhir.interceptor.model;

import ca.uhn.fhir.rest.api.PatchTypeEnum;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 * Request data object for the {@link ca.uhn.fhir.interceptor.api.Pointcut#STORAGE_PRESTORAGE_RESOURCE_PREPATCH}
 * pointcut.
 *
 * @since 8.8.0
 */
public class PrePatchDetails {
	private final IBaseResource myResource;
	private final PatchTypeEnum myPatchType;
	private final String myPatchBody;
	private final IBaseParameters myFhirPatchBody;

	/**
	 * Constructor
	 */
	public PrePatchDetails(IBaseResource theResource, PatchTypeEnum thePatchType, String thePatchBody, IBaseParameters theFhirPatchBody) {
		myResource = theResource;
		myPatchType = thePatchType;
		myPatchBody = thePatchBody;
		myFhirPatchBody = theFhirPatchBody;
	}

	/**
	 * Returns the resource that is about to be patched. Hooks may modify this resource.
	 */
	@Nonnull
	public IBaseResource getResource() {
		return myResource;
	}

	/**
	 * Returns the type of patch body that was supplied. The result of this method determines whether
	 * {@link #getPatchBody()} or {@link #getFhirPatchBody()} will return a non-null value and ultimately
	 * be used for performing the patch.
	 */
	public PatchTypeEnum getPatchType() {
		return myPatchType;
	}

	/**
	 * If the {@link #getPatchType()} is {@link PatchTypeEnum#JSON_PATCH} or {@link PatchTypeEnum#XML_PATCH},
	 * this method returns the raw patch body as a JSON or XML string.
	 * If the {@link #getPatchType()} is {@link PatchTypeEnum#FHIR_PATCH_JSON} or {@link PatchTypeEnum#FHIR_PATCH_XML},
	 * this method returns the FHIR patch body as a FHIR Parameters resource encoded in either JSON or XML.
	 */
	public String getPatchBody() {
		return myPatchBody;
	}

	/**
	 * If the {@link #getPatchType()} is {@link PatchTypeEnum#FHIR_PATCH_JSON} or {@link PatchTypeEnum#FHIR_PATCH_XML},
	 * this method returns the FHIR patch body as a FHIR Parameters resource. Hooks should not modify the
	 * patch document, as unspecified behavior may result.
	 */
	public IBaseParameters getFhirPatchBody() {
		return myFhirPatchBody;
	}

}
