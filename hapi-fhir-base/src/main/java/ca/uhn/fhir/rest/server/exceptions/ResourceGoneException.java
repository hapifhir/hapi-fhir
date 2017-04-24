package ca.uhn.fhir.rest.server.exceptions;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.base.composite.BaseIdentifierDt;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.util.CoverageIgnore;

/**
 * Represents an <b>HTTP 410 Resource Gone</b> response, which geenerally
 * indicates that the resource has been deleted
 */
@CoverageIgnore
public class ResourceGoneException extends BaseServerResponseException {

	public static final int STATUS_CODE = Constants.STATUS_HTTP_410_GONE;

	/**
	 * Constructor which creates an error message based on a given resource ID
	 * 
	 * @param theResourceId
	 *           The ID of the resource that could not be found
	 */
	public ResourceGoneException(IIdType theResourceId) {
		super(STATUS_CODE, "Resource " + (theResourceId != null ? theResourceId.getValue() : "") + " is gone/deleted");
	}

	/**
	 * @deprecated This constructor has a dependency on a specific model version and will be removed. Deprecated in HAPI
	 *             1.6 - 2016-07-02
	 */
	@Deprecated
	public ResourceGoneException(Class<? extends IResource> theClass, BaseIdentifierDt thePatientId) {
		super(STATUS_CODE, "Resource of type " + theClass.getSimpleName() + " with ID " + thePatientId + " is gone/deleted");
	}

	/**
	 * Constructor which creates an error message based on a given resource ID
	 * 
	 * @param theClass
	 *           The type of resource that could not be found
	 * @param theResourceId
	 *           The ID of the resource that could not be found
	 */
	public ResourceGoneException(Class<? extends IResource> theClass, IIdType theResourceId) {
		super(STATUS_CODE, "Resource of type " + theClass.getSimpleName() + " with ID " + theResourceId + " is gone/deleted");
	}

	/**
	 * Constructor
	 * 
	 * @param theMessage
	 *           The message
	 * @param theOperationOutcome
	 *           The OperationOutcome resource to return to the client
	 */
	public ResourceGoneException(String theMessage, IBaseOperationOutcome theOperationOutcome) {
		super(STATUS_CODE, theMessage, theOperationOutcome);
	}

	public ResourceGoneException(String theMessage) {
		super(STATUS_CODE, theMessage);
	}

	private static final long serialVersionUID = 1L;

}
