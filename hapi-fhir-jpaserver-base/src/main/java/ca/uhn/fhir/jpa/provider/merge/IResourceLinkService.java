// Created by claude-sonnet-4-5
package ca.uhn.fhir.jpa.provider.merge;

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
	boolean hasReplacedByLink(IBaseResource theResource);

	/**
	 * Check if resource has a specific "replaces" link to target.
	 * Used to validate result resource has correct link to source.
	 * Compares IDs without version for flexibility.
	 *
	 * @param theResource the resource to check
	 * @param theTargetId the target resource ID to look for
	 * @return true if resource has a replaces link to the target
	 */
	boolean hasReplacesLinkTo(IBaseResource theResource, IIdType theTargetId);
}
