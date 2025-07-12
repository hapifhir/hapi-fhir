package ca.uhn.fhir.repository.impl.memory;

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * In-memory storage for FHIR resources, indexed by resource type and ID.
 */
public class ResourceStorage {
	final Map<String, Map<IIdType, IBaseResource>> resourceMap;

	ResourceStorage(Map<String, Map<IIdType, IBaseResource>> theResourceMap) {
		resourceMap = theResourceMap;
	}

	ResourceLookup lookupResource(IIdType theId) {
		Validate.notNull(theId, "Id must not be null");
		Validate.notNull(theId.getResourceType(), "Resource type must not be null");

		Map<IIdType, IBaseResource> resources = getResourceMapForType(theId.getResourceType());

		return new ResourceLookup(resources, new IdDt(theId));
	}

	ResourceLookup lookupResource(String resourceTypeName, IIdType theId) {
		Validate.notBlank(resourceTypeName, "Resource type must not be blank");
		Validate.notNull(theId, "Id must not be null");

		IIdType unqualifiedVersionless = ResourceLookup.normalizeIdForLookup(theId, resourceTypeName);

		return lookupResource(unqualifiedVersionless);
	}

	@Nonnull
	ResourceLookup createResource(IBaseResource resource) {
		Map<IIdType, IBaseResource> resources = getResourceMapForType(resource.fhirType());

		IIdType theId;
		do {
			theId = new IdDt(resource.fhirType(), UUID.randomUUID().toString());
		} while (resources.containsKey(theId));

		resource.setId(theId);
		resources.put(theId.toUnqualifiedVersionless(), resource);

		return lookupResource(theId);
	}

	@VisibleForTesting
	public @Nonnull Collection<IBaseResource> getAllOfType(String theResourceType) {
		return getResourceMapForType(theResourceType).values();
	}

	@Nonnull
	private Map<IIdType, IBaseResource> getResourceMapForType(String resourceTypeName) {
		return resourceMap.computeIfAbsent(resourceTypeName, x -> new HashMap<>());
	}

	/**
	 * Abstract "pointer" to a resource id in the repository.
	 *
	 * @param resources the map of resources for a specific type
	 * @param id        the id of the resource to look up
	 */
	record ResourceLookup(Map<IIdType, IBaseResource> resources, IIdType id) {

		private static IIdType normalizeIdForLookup(IIdType theId, String resourceTypeName) {
			IIdType unqualifiedVersionless = theId.toUnqualifiedVersionless();

			if (unqualifiedVersionless.getResourceType() == null) {
				unqualifiedVersionless = unqualifiedVersionless.withResourceType(resourceTypeName);
			}

			if (!resourceTypeName.equals(unqualifiedVersionless.getResourceType())) {
				throw new IllegalArgumentException("Resource type mismatch: resource is " + resourceTypeName
						+ " but id type is " + unqualifiedVersionless.getResourceType());
			}

			return unqualifiedVersionless;
		}

		@Nonnull
		Optional<IBaseResource> getResource() {
			return Optional.ofNullable(resources.get(id));
		}

		@Nonnull
		IBaseResource getResourceOrThrow404() {
			return getResource().orElseThrow(() -> new ResourceNotFoundException("Resource not found with id " + id));
		}

		void remove() {
			resources.remove(id);
		}

		boolean isPresent() {
			return resources.containsKey(id);
		}

		<T extends IBaseResource> void put(T theResource) {
			resources.put(id, theResource);
		}
	}
}
