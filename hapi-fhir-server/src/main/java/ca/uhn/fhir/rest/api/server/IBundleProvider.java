package ca.uhn.fhir.rest.api.server;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.ConfigurationException;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


public interface IBundleProvider {

	/**
	 * If this method is implemented, provides an ID for the current
	 * page of results. This ID should be unique (at least within
	 * the current search as identified by {@link #getUuid()})
	 * so that it can be used to look up a specific page of results.
	 * <p>
	 * This can be used in order to allow the
	 * server paging mechanism to work using completely
	 * opaque links (links that do not encode any index/offset
	 * information), which can be useful on some servers.
	 * </p>
	 *
	 * @since 3.5.0
	 */
	default String getCurrentPageId() {
		return null;
	}

	/**
	 * If this method is implemented, provides an ID for the next
	 * page of results. This ID should be unique (at least within
	 * the current search as identified by {@link #getUuid()})
	 * so that it can be used to look up a specific page of results.
	 * <p>
	 * This can be used in order to allow the
	 * server paging mechanism to work using completely
	 * opaque links (links that do not encode any index/offset
	 * information), which can be useful on some servers.
	 * </p>
	 *
	 * @since 3.5.0
	 */
	default String getNextPageId() {
		return null;
	}

	/**
	 * If this method is implemented, provides an ID for the previous
	 * page of results. This ID should be unique (at least within
	 * the current search as identified by {@link #getUuid()})
	 * so that it can be used to look up a specific page of results.
	 * <p>
	 * This can be used in order to allow the
	 * server paging mechanism to work using completely
	 * opaque links (links that do not encode any index/offset
	 * information), which can be useful on some servers.
	 * </p>
	 *
	 * @since 3.5.0
	 */
	default String getPreviousPageId() {
		return null;
	}

	/**
	 * If the results in this bundle were produced using an offset query (as opposed to a query using
	 * continuation pointers, page IDs, etc.) the page offset can be returned here. The server
	 * should then attempt to form paging links that use <code>_offset</code> instead of
	 * opaque page IDs.
	 */
	default Integer getCurrentPageOffset() {
		return null;
	}

	/**
	 * If {@link #getCurrentPageOffset()} returns a non-null value, this method must also return
	 * the actual page size used
	 */
	default Integer getCurrentPageSize() {
		return null;
	}


	/**
	 * Returns the instant as of which this result was created. The
	 * result of this value is used to populate the <code>lastUpdated</code>
	 * value on search result/history result bundles.
	 */
	IPrimitiveType<Date> getPublished();

	/**
	 * Load the given collection of resources by index, plus any additional resources per the
	 * server's processing rules (e.g. _include'd resources, OperationOutcome, etc.). For example,
	 * if the method is invoked with index 0,10 the method might return 10 search results, plus an
	 * additional 20 resources which matched a client's _include specification.
	 * <p>
	 * Note that if this bundle provider was loaded using a
	 * page ID (i.e. via {@link ca.uhn.fhir.rest.server.IPagingProvider#retrieveResultList(RequestDetails, String, String)}
	 * because {@link #getNextPageId()} provided a value on the
	 * previous page, then the indexes should be ignored and the
	 * whole page returned.
	 * </p>
	 *
	 * @param theFromIndex The low index (inclusive) to return
	 * @param theToIndex   The high index (exclusive) to return
	 * @return A list of resources. The size of this list must be at least <code>theToIndex - theFromIndex</code>.
	 */
	@Nonnull
	List<IBaseResource> getResources(int theFromIndex, int theToIndex);

	/**
	 * Get all resources
	 *
	 * @return <code>getResources(0, this.size())</code>.  Return an empty list if <code>this.size()</code> is zero.
	 * @throws ConfigurationException if size() is null
	 */
	@Nonnull
	default List<IBaseResource> getAllResources() {
		List<IBaseResource> retval = new ArrayList<>();

		Integer size = size();
		if (size == null) {
			throw new ConfigurationException(Msg.code(464) + "Attempt to request all resources from an asynchronous search result.  The SearchParameterMap for this search probably should have been synchronous.");
		}
		if (size > 0) {
			retval.addAll(getResources(0, size));
		}
		return retval;
	}

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
	 * <p>
	 * Note that the UUID returned by this method corresponds to
	 * the search, and not to the individual page.
	 * </p>
	 */
	@Nullable
	String getUuid();

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
	@Nullable
	Integer size();

	/**
	 * This method returns <code>true</code> if the bundle provider knows that at least
	 * one result exists.
	 */
	default boolean isEmpty() {
		Integer size = size();
		if (size != null) {
			return size == 0;
		}
		return getResources(0, 1).isEmpty();
	}

	/**
	 * @return the value of {@link #size()} and throws a {@link NullPointerException} of it is null
	 */
	default int sizeOrThrowNpe() {
		Integer retVal = size();
		Validate.notNull(retVal, "size() returned null");
		return retVal;
	}

	/**
	 * @return the list of ids of all resources in the bundle
	 */
	default List<String> getAllResourceIds() {
		return getAllResources().stream().map(resource -> resource.getIdElement().getIdPart()).collect(Collectors.toList());
	}
}
