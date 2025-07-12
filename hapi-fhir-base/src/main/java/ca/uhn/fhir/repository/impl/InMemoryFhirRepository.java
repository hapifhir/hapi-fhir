package ca.uhn.fhir.repository.impl;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.repository.IRepository;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Multimap;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ca.uhn.fhir.model.api.StorageResponseCodeEnum.SUCCESSFUL_DELETE_NOT_FOUND;

/**
 * An in-memory implementation of the FHIR repository interface.
 * This repository stores resources in memory
 * and provides basic CRUD operations, search, and transaction support.
 * Limitations:
 * <ul>
 *     <li>Does not support versioning of resources.</li>
 *     <li>Does not search beyond all-of-type - no SearchParameters are supported.</li>
 *     <li>Does not support extended operations.</li>
 *     <li>Does not support conditional update or create.</li>
 *     <li>Does not support PATCH operations.</li>
 * </ul>
 */
public class InMemoryFhirRepository implements IRepository {
	// Based on org.opencds.cqf.fhir.utility.repository.InMemoryFhirRepository.

	private String myBaseUrl;
	private final Map<String, Map<IIdType, IBaseResource>> resourceMap;
	private final FhirContext context;

	public static InMemoryFhirRepository emptyRepository(@Nonnull FhirContext theFhirContext) {
		return new InMemoryFhirRepository(theFhirContext, new HashMap<>());
	}

	public static InMemoryFhirRepository fromBundleContents(FhirContext theFhirContext, IBaseBundle theBundle) {

		List<IBaseResource> resources = BundleUtil.toListOfResources(theFhirContext, theBundle);
		var bundleContents = resources.stream()
				.collect(Collectors.groupingBy(
						IBaseResource::fhirType,
						Collectors.toMap(r -> r.getIdElement().toUnqualifiedVersionless(), Function.identity())));

		return new InMemoryFhirRepository(theFhirContext, new HashMap<>(bundleContents));
	}

	InMemoryFhirRepository(
			@Nonnull FhirContext theContext, @Nonnull Map<String, Map<IIdType, IBaseResource>> theContents) {
		context = theContext;
		resourceMap = theContents;
	}

	@Override
	public @Nonnull FhirContext fhirContext() {
		return this.context;
	}

	@Override
	@SuppressWarnings("unchecked")
	public synchronized <T extends IBaseResource, I extends IIdType> T read(
			Class<T> resourceType, I id, Map<String, String> headers) {
		var lookup = lookupResource(resourceType, id);

		var resource = lookup.getResourceOrThrow404();

		return (T) resource;
	}

	@Override
	public synchronized <T extends IBaseResource> MethodOutcome create(T resource, Map<String, String> headers) {
		var resources = getResourceMapForType(resource.fhirType());

		IIdType theId;
		do {
			theId = new IdDt(resource.fhirType(), UUID.randomUUID().toString());
		} while (resources.containsKey(theId));
		resource.setId(theId);

		resources.put(theId.toUnqualifiedVersionless(), resource);

		MethodOutcome methodOutcome = new MethodOutcome(theId, true);
		methodOutcome.setResource(resource);
		methodOutcome.setResponseStatusCode(Constants.STATUS_HTTP_201_CREATED);
		return methodOutcome;
	}

	@Override
	public synchronized <I extends IIdType, P extends IBaseParameters> MethodOutcome patch(
			I id, P patchParameters, Map<String, String> headers) {
		throw new NotImplementedOperationException("The PATCH operation is not currently supported");
	}

	@Override
	public synchronized <T extends IBaseResource> MethodOutcome update(T resource, Map<String, String> headers) {
		var lookup = lookupResource(resource.getClass(), resource.getIdElement());

		boolean isCreate = !lookup.isPresent();
		lookup.put(resource);
		var outcome = new MethodOutcome(lookup.id, isCreate);
		if (isCreate) {
			outcome.setResponseStatusCode(Constants.STATUS_HTTP_201_CREATED);
		} else {
			outcome.setResponseStatusCode(Constants.STATUS_HTTP_200_OK);
		}

		return outcome;
	}

	@Override
	public synchronized <T extends IBaseResource, I extends IIdType> MethodOutcome delete(
			Class<T> resourceType, I id, Map<String, String> headers) {
		var lookup = lookupResource(resourceType, id);

		if (lookup.isPresent()) {
			var resource = lookup.getResourceOrThrow404();
			lookup.remove();
			MethodOutcome methodOutcome = new MethodOutcome(id, false).setResource(resource);
			methodOutcome.setResponseStatusCode(Constants.STATUS_HTTP_204_NO_CONTENT);
			return methodOutcome;
		} else {
			var oo = OperationOutcomeUtil.createOperationOutcome(
					OperationOutcomeUtil.OO_SEVERITY_WARN,
					SUCCESSFUL_DELETE_NOT_FOUND.getDisplay(),
					"not-found",
					fhirContext(),
					SUCCESSFUL_DELETE_NOT_FOUND);

			MethodOutcome methodOutcome = new MethodOutcome(id, false).setOperationOutcome(oo);
			methodOutcome.setResponseStatusCode(Constants.STATUS_HTTP_404_NOT_FOUND);
			return methodOutcome;
		}
	}

	@Override
	public synchronized <B extends IBaseBundle, T extends IBaseResource> B search(
			Class<B> bundleType,
			Class<T> resourceType,
			Multimap<String, List<IQueryParameterType>> searchParameters,
			Map<String, String> headers) {
		BundleBuilder builder = new BundleBuilder(this.context);

		String searchType = context.getResourceType(resourceType);
		var resourceIdMap = getResourceMapForType(searchType);

		if (searchParameters == null || searchParameters.isEmpty()) {
			resourceIdMap.values().forEach(builder::addCollectionEntry);
			builder.setType("searchset");
			//noinspection unchecked
			return (B) builder.getBundle();
		}

		Collection<IBaseResource> candidates = resourceIdMap.values();
		// todo implement more search parameters
		//        if (searchParameters.containsKey("_id")) {
		//            // We are consuming the _id parameter in this if statement
		//            var idQueries = searchParameters.get("_id");
		//            searchParameters.remove("_id");
		//
		//            // The _id param can be a list of ids
		//            var idResources = new ArrayList<IBaseResource>(idQueries.size());
		//            for (var idQuery : idQueries) {
		//                var idToken = (TokenParam) idQuery;
		//                // Need to construct the equivalent "UnqualifiedVersionless" id that the map is
		//                // indexed by. If an id has a version it won't match. Need apples-to-apples Ids types
		//                var id = Ids.newId(context, resourceType.getSimpleName(), idToken.getValue());
		//                var r = resourceIdMap.get(id);
		//                if (r != null) {
		//                    idResources.add(r);
		//                }
		//            }
		//
		//            candidates = idResources;
		//        } else {
		//            candidates = resourceIdMap.values();
		//        }

		// Apply the rest of the filters
		//        for (var resource : candidates) {
		//            boolean include = true;
		//            for (var nextEntry : searchParameters.entrySet()) {
		//                var paramName = nextEntry.getKey();
		//                if (!this.resourceMatcher.matches(paramName, nextEntry.getValue(), resource)) {
		//                    include = false;
		//                    break;
		//                }
		//            }
		//
		//            if (include) {
		//                builder.addCollectionEntry(resource);
		//            }
		//        }

		builder.setType("searchset");
		//noinspection unchecked
		return (B) builder.getBundle();
	}

	@Override
	public synchronized <B extends IBaseBundle> B transaction(B transaction, Map<String, String> headers) {
		NaiveRepositoryTransactionProcessor transactionProcessor = new NaiveRepositoryTransactionProcessor(this);
		return transactionProcessor.processTransaction(transaction, headers);
	}

	// SOMEDAY find a home for this
	public static String statusCodeToStatusLine(int theResponseStatusCode) {
		return switch (theResponseStatusCode) {
			case Constants.STATUS_HTTP_200_OK -> "200 OK";
			case Constants.STATUS_HTTP_201_CREATED -> "201 Created";
			case Constants.STATUS_HTTP_409_CONFLICT -> "409 Conflict";
			case Constants.STATUS_HTTP_204_NO_CONTENT -> "204 No Content";
			case Constants.STATUS_HTTP_404_NOT_FOUND -> "404 Not Found";
			default -> throw new IllegalArgumentException("Unsupported response status code: " + theResponseStatusCode);
		};
	}

	/**
	 * The map of resources for each resource type.
	 */
	@VisibleForTesting
	@Nonnull
	public Map<IIdType, IBaseResource> getResourceMapForType(String resourceTypeName) {
		return resourceMap.computeIfAbsent(resourceTypeName, x -> new HashMap<>());
	}

	public String getBaseUrl() {
		return myBaseUrl;
	}

	public void setBaseUrl(String theBaseUrl) {
		myBaseUrl = theBaseUrl;
	}

	/**
	 * Abstract "pointer" to a resource id in the repository.
	 * @param resources the map of resources for a specific type
	 * @param id the id of the resource to look up
	 */
	private record ResourceLookup(Map<IIdType, IBaseResource> resources, IIdType id) {
		@Nonnull
		IBaseResource getResourceOrThrow404() {
			var resource = resources.get(id);

			if (resource == null) {
				throw new ResourceNotFoundException("Resource not found with id " + id);
			}
			return resource;
		}

		void remove() {
			resources.remove(id);
		}

		boolean isPresent() {
			return resources.containsKey(id);
		}

		public <T extends IBaseResource> void put(T theResource) {
			resources.put(id, theResource);
		}
	}

	private ResourceLookup lookupResource(IIdType theId) {
		Validate.notNull(theId, "Id must not be null");
		Validate.notNull(theId.getResourceType(), "Resource type must not be null");

		Map<IIdType, IBaseResource> resources = getResourceMapForType(theId.getResourceType());

		return new ResourceLookup(resources, new IdDt(theId));
	}

	private ResourceLookup lookupResource(Class<? extends IBaseResource> theResourceType, IIdType theId) {
		Validate.notNull(theResourceType, "Resource type must not be null");
		Validate.notNull(theId, "Id must not be null");

		String resourceTypeName = fhirContext().getResourceType(theResourceType);

		IIdType unqualifiedVersionless = theId.toUnqualifiedVersionless();
		String idResourceType = unqualifiedVersionless.getResourceType();
		if (idResourceType == null) {
			unqualifiedVersionless = unqualifiedVersionless.withResourceType(resourceTypeName);
		} else if (!idResourceType.equals(resourceTypeName)) {
			throw new IllegalArgumentException(
					"Resource type mismatch: resource is " + resourceTypeName + " but id type is " + idResourceType);
		}

		return lookupResource(unqualifiedVersionless);
	}
}
