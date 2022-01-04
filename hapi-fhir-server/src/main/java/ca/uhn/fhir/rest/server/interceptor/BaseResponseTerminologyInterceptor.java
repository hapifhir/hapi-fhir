package ca.uhn.fhir.rest.server.interceptor;

/*-
 * #%L
 * HAPI FHIR - Server Framework
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
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.util.BundleUtil;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class BaseResponseTerminologyInterceptor {
	protected final IValidationSupport myValidationSupport;
	protected final FhirContext myContext;

	/**
	 * Constructor
	 *
	 * @param theValidationSupport The validation support module
	 */
	public BaseResponseTerminologyInterceptor(@Nonnull IValidationSupport theValidationSupport) {
		myValidationSupport = theValidationSupport;
		Validate.notNull(theValidationSupport, "The validation support must not be null");

		myContext = theValidationSupport.getFhirContext();
		Validate.notNull(myContext, "The validation support must not return a null context");
	}


	@Nonnull
	protected List<IBaseResource> toListForProcessing(RequestDetails theRequestDetails, IBaseResource theResource) {

		switch (theRequestDetails.getRestOperationType()) {
			// Don't apply to these operations
			case ADD_TAGS:
			case DELETE_TAGS:
			case GET_TAGS:
			case GET_PAGE:
			case GRAPHQL_REQUEST:
			case EXTENDED_OPERATION_SERVER:
			case EXTENDED_OPERATION_TYPE:
			case EXTENDED_OPERATION_INSTANCE:
			case CREATE:
			case DELETE:
			case TRANSACTION:
			case UPDATE:
			case VALIDATE:
			case METADATA:
			case META_ADD:
			case META:
			case META_DELETE:
			case PATCH:
			default:
				return Collections.emptyList();

			// Do apply to these operations
			case HISTORY_INSTANCE:
			case HISTORY_SYSTEM:
			case HISTORY_TYPE:
			case SEARCH_SYSTEM:
			case SEARCH_TYPE:
			case READ:
			case VREAD:
				break;
		}

		List<IBaseResource> resources;
		if (theResource instanceof IBaseBundle) {
			resources = BundleUtil.toListOfResources(myContext, (IBaseBundle) theResource);
		} else {
			resources = Collections.singletonList(theResource);
		}
		return resources;
	}

}
