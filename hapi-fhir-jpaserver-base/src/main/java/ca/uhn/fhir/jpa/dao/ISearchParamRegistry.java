package ca.uhn.fhir.jpa.dao;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.search.JpaRuntimeSearchParam;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ISearchParamRegistry {

	/**
	 * Request that the cache be refreshed now, in the current thread
	 */
	void forceRefresh();

	/**
	 * @return Returns {@literal null} if no match
	 */
	RuntimeSearchParam getActiveSearchParam(String theResourceName, String theParamName);

	Map<String, Map<String, RuntimeSearchParam>> getActiveSearchParams();

	Map<String, RuntimeSearchParam> getActiveSearchParams(String theResourceName);

	List<JpaRuntimeSearchParam> getActiveUniqueSearchParams(String theResourceName, Set<String> theParamNames);

	List<JpaRuntimeSearchParam> getActiveUniqueSearchParams(String theResourceName);

	void refreshCacheIfNecessary();

	/**
	 * Request that the cache be refreshed at the next convenient time (in a different thread)
	 */
	void requestRefresh();
}
