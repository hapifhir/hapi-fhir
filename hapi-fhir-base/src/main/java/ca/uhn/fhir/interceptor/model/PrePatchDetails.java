/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
	public PrePatchDetails(
			IBaseResource theResource,
			PatchTypeEnum thePatchType,
			String thePatchBody,
			IBaseParameters theFhirPatchBody) {
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
