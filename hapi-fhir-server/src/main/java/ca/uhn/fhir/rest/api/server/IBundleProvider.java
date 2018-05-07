package ca.uhn.fhir.rest.api.server;

import java.util.Date;

/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.List;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;


public interface IBundleProvider {

	/**
	 * Load the given collection of resources by index, plus any additional resources per the
	 * server's processing rules (e.g. _include'd resources, OperationOutcome, etc.). For example,
	 * if the method is invoked with index 0,10 the method might return 10 search results, plus an
	 * additional 20 resources which matched a client's _include specification.
	 * 
	 * @param theFromIndex
	 *           The low index (inclusive) to return
	 * @param theToIndex
	 *           The high index (exclusive) to return
	 * @return A list of resources. The size of this list must be at least <code>theToIndex - theFromIndex</code>.
	 */
	List<IBaseResource> getResources(int theFromIndex, int theToIndex);

	/**
	 * Optionally may be used to signal a preferred page size to the server, e.g. because
	 * the implementing code recognizes that the resources which will be returned by this
	 * implementation are expensive to load so a smaller page size should be used. The value
	 * returned by this method will only be used if the client has not explicitly requested
	 * a page size.
	 * 
	 * @return Returns the preferred page size or <code>null</code>
	 */
	Integer preferredPageSize();

	/**
	 * Returns the total number of results which match the given query (exclusive of any
	 * _include's or OperationOutcome). May return {@literal null} if the total size is not
	 * known or would be too expensive to calculate.
	 */
	Integer size();

	/**
	 * Returns the instant as of which this result was valid
	 */
	IPrimitiveType<Date> getPublished();

	/**
	 * Returns the UUID associated with this search. Note that this
	 * does not need to return a non-null value unless it a
	 * IPagingProvider is being used that requires UUIDs
	 * being returned.
	 * <p>
	 * In other words, if you are using the default FifoMemoryPagingProvider in
	 * your server, it is fine for this method to simply return {@code null} since FifoMemoryPagingProvider
	 * does not use the value anyhow. On the other hand, if you are creating a custom
	 * IPagingProvider implementation you might use this method to communicate
	 * the search ID back to the provider.
	 * </p>
	 */
	public String getUuid();

}
