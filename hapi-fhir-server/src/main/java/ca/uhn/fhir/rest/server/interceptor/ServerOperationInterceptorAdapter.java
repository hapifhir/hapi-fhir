package ca.uhn.fhir.rest.server.interceptor;

/*
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

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.rest.api.server.RequestDetails;

/**
 * NOP implementation of {@link IServerOperationInterceptor}
 */
public class ServerOperationInterceptorAdapter extends InterceptorAdapter implements IServerOperationInterceptor {

	@Override
	public void resourceDeleted(RequestDetails theRequest, IBaseResource theResource) {
		// nothing
	}

	@Override
	public void resourcePreCreate(RequestDetails theRequest, IBaseResource theResource) {
		// nothing
	}

	@Override
	public void resourcePreDelete(RequestDetails theRequest, IBaseResource theResource) {
		// nothing
	}

	@Override
	public void resourcePreUpdate(RequestDetails theRequest, IBaseResource theOldResource, IBaseResource theNewResource) {
		// nothing
	}

	@Override
	public void resourceCreated(RequestDetails theRequest, IBaseResource theResource) {
		// nothing
	}

	/**
	 * @deprecated Deprecated in HAPI FHIR 2.6 in favour of {@link #resourceUpdated(RequestDetails, IBaseResource, IBaseResource)}
	 */
	@Deprecated
	@Override
	public void resourceUpdated(RequestDetails theRequest, IBaseResource theResource) {
		// nothing
	}

	@Override
	public void resourceUpdated(RequestDetails theRequest, IBaseResource theOldResource, IBaseResource theNewResource) {
		// nothing
	}

}
