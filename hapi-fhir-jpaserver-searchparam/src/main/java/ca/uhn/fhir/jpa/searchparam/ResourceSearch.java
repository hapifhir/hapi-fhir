package ca.uhn.fhir.jpa.searchparam;

/*-
 * #%L
 * HAPI FHIR Search Parameters
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

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 * A resource type along with a search parameter map and partition id.  Everything you need to perform a search!
 */
public class ResourceSearch {
	private final RuntimeResourceDefinition myRuntimeResourceDefinition;
	private final SearchParameterMap mySearchParameterMap;
	private final RequestPartitionId myRequestPartitionId;

	public ResourceSearch(RuntimeResourceDefinition theRuntimeResourceDefinition, SearchParameterMap theSearchParameterMap, RequestPartitionId theRequestPartitionId) {
		myRuntimeResourceDefinition = theRuntimeResourceDefinition;
		mySearchParameterMap = theSearchParameterMap;
		myRequestPartitionId = theRequestPartitionId;
	}

	public RuntimeResourceDefinition getRuntimeResourceDefinition() {
		return myRuntimeResourceDefinition;
	}

	public SearchParameterMap getSearchParameterMap() {
		return mySearchParameterMap;
	}

	public String getResourceName() {
		return myRuntimeResourceDefinition.getName();
	}

	public boolean isDeleteExpunge() {
		return mySearchParameterMap.isDeleteExpunge();
	}

	public Class<? extends IBaseResource> getResourceType() {
		return myRuntimeResourceDefinition.getImplementingClass();
	}

	public RequestPartitionId getRequestPartitionId() {
		return myRequestPartitionId;
	}
}
