/*-
 * #%L
 * HAPI-FHIR Storage Batch2 Jobs
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
package ca.uhn.fhir.batch2.jobs.bulkmodify.framework.api;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 * Implementations of bulk modification jobs will subclass {@link ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base.BaseBulkModifyResourcesStep}
 * and will return this object for each resource candidate to be modified.
 *
 * @since 8.6.0
 * @see ResourceModificationRequest
 */
public class ResourceModificationResponse {

	@Nullable
	private final IBaseResource myResource;

	/**
	 * Use static factory methods to instantiate this class
	 */
	private ResourceModificationResponse(@Nullable IBaseResource theResource) {
		myResource = theResource;
	}

	@Nullable
	public IBaseResource getResource() {
		return myResource;
	}

	public static ResourceModificationResponse updateResource(@Nonnull IBaseResource theResource) {
		Validate.notNull(theResource, "theResource must not be null");
		return new ResourceModificationResponse(theResource);
	}

	public static ResourceModificationResponse noChange() {
		return new ResourceModificationResponse(null);
	}
}
