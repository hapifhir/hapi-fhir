package ca.uhn.fhir.rest.api.server;

/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import java.util.Collection;

public interface IRequestOperationCallback {

	void resourceCreated(IBaseResource theResource);

	void resourceDeleted(IBaseResource theResource);

	void resourcePreCreate(IBaseResource theResource);

	void resourcePreDelete(IBaseResource theResource);

	void resourcePreUpdate(IBaseResource theOldResource, IBaseResource theNewResource);

	/**
	 * @deprecated Deprecated in HAPI FHIR 2.6 - Use {@link IRequestOperationCallback#resourceUpdated(IBaseResource, IBaseResource)} instead
	 */
	@Deprecated
	void resourceUpdated(IBaseResource theResource);

	void resourceUpdated(IBaseResource theOldResource, IBaseResource theNewResource);

	void resourcesCreated(Collection<? extends IBaseResource> theResource);

	void resourcesDeleted(Collection<? extends IBaseResource> theResource);

	/**
	 * @deprecated Deprecated in HAPI FHIR 2.6 - Use {@link IRequestOperationCallback#resourceUpdated(IBaseResource, IBaseResource)} instead
	 */
	@Deprecated
	void resourcesUpdated(Collection<? extends IBaseResource> theResource);
}
