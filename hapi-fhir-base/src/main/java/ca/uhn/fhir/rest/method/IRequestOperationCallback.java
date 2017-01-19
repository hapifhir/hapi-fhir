package ca.uhn.fhir.rest.method;

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

import java.util.Collection;

import org.hl7.fhir.instance.model.api.IBaseResource;

public interface IRequestOperationCallback {

	void resourceCreated(IBaseResource theResource);

	void resourceDeleted(IBaseResource theResource);

	void resourcesCreated(Collection<? extends IBaseResource> theResource);

	void resourcesDeleted(Collection<? extends IBaseResource> theResource);

	void resourcesUpdated(Collection<? extends IBaseResource> theResource);

	void resourceUpdated(IBaseResource theResource);
}
