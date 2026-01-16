/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
// Created by claude-sonnet-4-5
package ca.uhn.fhir.merge;

import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.List;

/**
 * Interface for managing resource merge links.
 *
 * Provides abstraction for tracking "replaces" and "replaced-by" relationships between
 * resources during merge operations. Implementations may use different strategies such as
 * FHIR extensions or native resource fields.
 */
public interface IResourceLinkService {

	/**
	 * Add a "replaces" link from target to source.
	 * Indicates that the target resource replaces the source resource.
	 *
	 * @param theTarget the resource that replaces another
	 * @param theSourceRef reference to the resource being replaced
	 */
	void addReplacesLink(IBaseResource theTarget, IBaseReference theSourceRef);

	/**
	 * Add a "replaced-by" link from source to target.
	 * Indicates that the source resource was replaced by the target resource.
	 *
	 * @param theSource the resource that was replaced
	 * @param theTargetRef reference to the replacement resource
	 */
	void addReplacedByLink(IBaseResource theSource, IBaseReference theTargetRef);

	/**
	 * Get all "replaces" links from a resource.
	 *
	 * @param theResource the resource to check
	 * @return list of references to resources that this resource replaces
	 */
	List<IBaseReference> getReplacesLinks(IBaseResource theResource);

	/**
	 * Get all "replaced-by" links from a resource.
	 *
	 * @param theResource the resource to check
	 * @return list of references to resources that replaced this resource
	 */
	List<IBaseReference> getReplacedByLinks(IBaseResource theResource);

	/**
	 * Check if resource has any "replaced-by" links.
	 * Used to determine if resource was already merged.
	 *
	 * @param theResource the resource to check
	 * @return true if resource has been replaced
	 */
	default boolean hasReplacedByLink(IBaseResource theResource) {
		List<IBaseReference> links = getReplacedByLinks(theResource);
		return !links.isEmpty();
	}

	/**
	 * Check if resource has a specific "replaces" link to target.
	 * Used to validate result resource has correct link to source.
	 * Compares versionless ids.
	 *
	 * @param theResource the resource to check
	 * @param theTargetId the target resource ID to look for
	 * @return true if resource has a replaces link to the target
	 */
	default boolean hasReplacesLinkTo(IBaseResource theResource, IIdType theTargetId) {
		List<IBaseReference> replacesLinks = getReplacesLinks(theResource);

		String targetIdValue = theTargetId.toUnqualifiedVersionless().getValue();

		for (IBaseReference link : replacesLinks) {
			IIdType linkRefElement = link.getReferenceElement();
			if (linkRefElement == null) {
				continue;
			}

			String linkIdValue = linkRefElement.toUnqualifiedVersionless().getValue();

			if (targetIdValue.equals(linkIdValue)) {
				return true;
			}
		}

		return false;
	}
}
