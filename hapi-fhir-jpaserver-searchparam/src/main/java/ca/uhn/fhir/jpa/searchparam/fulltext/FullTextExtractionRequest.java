/*-
 * #%L
 * HAPI FHIR JPA - Search Parameters
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.searchparam.fulltext;

import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.function.Supplier;
import javax.annotation.Nullable;

/**
 * This is a request object containing a request to extract the FullText index data from
 * a resource during storage.
 *
 * @see ca.uhn.fhir.interceptor.api.Pointcut#JPA_INDEX_EXTRACT_FULLTEXT for a description of how this should be used.
 * @since 8.4.0
 */
public class FullTextExtractionRequest {

	@Nullable
	private final IIdType myResourceId;

	@Nullable
	private final IBaseResource myResource;

	@Nonnull
	private final String myResourceType;

	@Nonnull
	private final Supplier<String> myDefaultSupplier;

	/**
	 * Constructor
	 */
	public FullTextExtractionRequest(
			@Nullable IIdType theResourceId,
			@Nullable IBaseResource theResource,
			@Nonnull String theResourceType,
			@Nonnull Supplier<String> theDefaultSupplier) {
		myResourceId = theResourceId;
		myResource = theResource;
		myResourceType = theResourceType;
		myDefaultSupplier = theDefaultSupplier;
	}

	/**
	 * @return Returns {@literal true} if this request represents a resource deletion
	 */
	public boolean isDelete() {
		return myResource == null;
	}

	/**
	 * @return Returns the ID of the resource being indexed. This may be <code>null</code> if a new resource is being created, and a type isn't assigned yet
	 */
	@Nullable
	public IIdType getResourceId() {
		return myResourceId;
	}

	/**
	 * @return Returns the resource being indexed. May be <code>null</code> if the operation is a resource deletion.
	 */
	@Nullable
	public IBaseResource getResource() {
		return myResource;
	}

	/**
	 * @return Returns the resource type being indexed.
	 */
	@Nonnull
	public String getResourceType() {
		return myResourceType;
	}

	/**
	 * @return Returns the extracted content/text string that is automatically extracted from the resource
	 */
	public String getDefaultString() {
		return myDefaultSupplier.get();
	}
}
