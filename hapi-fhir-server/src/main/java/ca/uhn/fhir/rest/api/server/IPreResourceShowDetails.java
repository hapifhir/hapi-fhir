/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.rest.api.server;

import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;

/**
 * This interface is a parameter type for the {@link ca.uhn.fhir.interceptor.api.Pointcut#STORAGE_PRESHOW_RESOURCES}
 * hook.
 */
public interface IPreResourceShowDetails extends Iterable<IBaseResource> {

	/**
	 * @return Returns the number of resources being shown
	 */
	int size();

	/**
	 * @return Returns the resource at the given index. If you wish to make modifications
	 * to any resources
	 */
	IBaseResource getResource(int theIndex);

	/**
	 * Replace the resource being returned at index
	 *
	 * @param theIndex    The resource index
	 * @param theResource The resource at index
	 */
	void setResource(int theIndex, IBaseResource theResource);

	/**
	 * Indicates that data is being masked from within the resource at the given index.
	 * This generally flags to the rest of the stack that the resource should include
	 * a SUBSET tag as an indication to consumers that some data has been removed.
	 *
	 * @param theIndex The resource index
	 */
	void markResourceAtIndexAsSubset(int theIndex);

	/**
	 * Returns a {@link List} containing all resources that will be shown.
	 * The returned list will have the same relative ordering as if the resources
	 * were retrieved using {@link #getResource(int)}, but any {@literal null} entries
	 * will be filtered out.
	 * <p>
	 * The returned List may not be modified. Use this method only if you are not
	 * looking to make changes.
	 *
	 * @since 6.6.0
	 */
	List<IBaseResource> getAllResources();
}
