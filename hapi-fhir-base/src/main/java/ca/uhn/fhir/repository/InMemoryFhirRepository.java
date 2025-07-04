package ca.uhn.fhir.repository;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.repository.matcher.IResourceMatcher;
import ca.uhn.fhir.repository.matcher.MultiVersionResourceMatcher;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import com.google.common.collect.Multimap;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseConformance;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.opencds.cqf.fhir.utility.BundleHelper;
import org.opencds.cqf.fhir.utility.Canonicals;
import org.opencds.cqf.fhir.utility.Ids;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ca.uhn.fhir.model.api.StorageResponseCodeEnum.SUCCESSFUL_DELETE_NOT_FOUND;
import static org.opencds.cqf.fhir.utility.BundleHelper.newBundle;

/**
 * An in-memory implementation of the FHIR repository interface.
 * Based on org.opencds.cqf.fhir.utility.repository.InMemoryFhirRepository.
 * This repository stores resources in memory
 * and provides basic CRUD operations, search, and transaction support.
 */
public class InMemoryFhirRepository implements IRepository {

	private final Map<String, Map<IIdType, IBaseResource>> resourceMap;
	private final FhirContext context;
	private final IResourceMatcher resourceMatcher;

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
		resourceMatcher = new MultiVersionResourceMatcher(context);
	}

	record ResourceLookup(Map<IIdType, IBaseResource> resources, IIdType id) {
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
	}

	ResourceLookup lookupResource(Class<? extends IBaseResource> theResourceType, IIdType theId) {
		Validate.notNull(theResourceType, "Resource type must not be null");
		Validate.notNull(theId, "Id must not be null");

		String resourceTypeName = fhirContext().getResourceType(theResourceType);
		Map<IIdType, IBaseResource> resources = getResourceMapForType(resourceTypeName);

		return new ResourceLookup(resources, theId.toUnqualifiedVersionless());
	}

	@Nonnull
	private Map<IIdType, IBaseResource> getResourceMapForType(String resourceTypeName) {
		return resourceMap.computeIfAbsent(resourceTypeName, x -> new HashMap<>());
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends IBaseResource, I extends IIdType> T read(
			Class<T> resourceType, I id, Map<String, String> headers) {
		var lookup = lookupResource(resourceType, id);

		var resource = lookup.getResourceOrThrow404();

		return (T) resource;
	}

	@Override
	public <T extends IBaseResource> MethodOutcome create(T resource, Map<String, String> headers) {
		var resources = getResourceMapForType(resource.fhirType());

		IIdType theId;
		do {
			theId = Ids.newRandomId(context, resource.fhirType());
		} while (resources.containsKey(theId));
		resource.setId(theId);

		resources.put(theId.toUnqualifiedVersionless(), resource);

		return new MethodOutcome(theId, true);
	}

	@Override
	public <I extends IIdType, P extends IBaseParameters> MethodOutcome patch(
			I id, P patchParameters, Map<String, String> headers) {
		throw new NotImplementedOperationException("The PATCH operation is not currently supported");
	}

	@Override
	public <T extends IBaseResource> MethodOutcome update(T resource, Map<String, String> headers) {
		var resources = getResourceMapForType(resource.fhirType());
		var theId = resource.getIdElement().toUnqualifiedVersionless();
		var outcome = new MethodOutcome(theId, false);
		if (!resources.containsKey(theId)) {
			outcome.setCreated(true);
		}
		if (resource.fhirType().equals("SearchParameter")) {
			this.resourceMatcher.addCustomParameter(BundleHelper.resourceToRuntimeSearchParam(resource));
		}
		resources.put(theId, resource);

		return outcome;
	}

	@Override
	public <T extends IBaseResource, I extends IIdType> MethodOutcome delete(
			Class<T> resourceType, I id, Map<String, String> headers) {
		var lookup = lookupResource(resourceType, id);

		if (lookup.isPresent()) {
			var resource = lookup.getResourceOrThrow404();
			lookup.remove();
			return new MethodOutcome(id, false).setResource(resource);
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
	public <B extends IBaseBundle, T extends IBaseResource> B search(
			Class<B> bundleType,
			Class<T> resourceType,
			Multimap<String, List<IQueryParameterType>> searchParameters,
			Map<String, String> headers) {
		BundleBuilder builder = new BundleBuilder(this.context);
		var resourceIdMap = resourceMap.computeIfAbsent(resourceType.getSimpleName(), r -> new HashMap<>());

		if (searchParameters == null || searchParameters.isEmpty()) {
			resourceIdMap.values().forEach(builder::addCollectionEntry);
			builder.setType("searchset");
			return (B) builder.getBundle();
		}

		Collection<IBaseResource> candidates = resourceIdMap.values();
		// fixme
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
		return (B) builder.getBundle();
	}

	@Override
	public <B extends IBaseBundle> B link(Class<B> bundleType, String url, Map<String, String> headers) {
		throw new NotImplementedOperationException("Paging is not currently supported");
	}

	@Override
	public <C extends IBaseConformance> C capabilities(Class<C> resourceType, Map<String, String> headers) {
		throw new NotImplementedOperationException("The capabilities interaction is not currently supported");
	}

	@Override
	public <B extends IBaseBundle> B transaction(B transaction, Map<String, String> headers) {
		var version = transaction.getStructureFhirVersionEnum();

		@SuppressWarnings("unchecked")
		var returnBundle = (B) newBundle(version);
		BundleHelper.getEntry(transaction).forEach(e -> {
			if (BundleHelper.isEntryRequestPut(version, e)) {
				var outcome = this.update(BundleHelper.getEntryResource(version, e));
				var location = outcome.getId().getValue();
				BundleHelper.addEntry(
						returnBundle,
						BundleHelper.newEntryWithResponse(
								version, BundleHelper.newResponseWithLocation(version, location)));
			} else if (BundleHelper.isEntryRequestPost(version, e)) {
				var outcome = this.create(BundleHelper.getEntryResource(version, e));
				var location = outcome.getId().getValue();
				BundleHelper.addEntry(
						returnBundle,
						BundleHelper.newEntryWithResponse(
								version, BundleHelper.newResponseWithLocation(version, location)));
			} else if (BundleHelper.isEntryRequestDelete(version, e)) {
				if (BundleHelper.getEntryRequestId(version, e).isPresent()) {
					var resourceType = Canonicals.getResourceType(
							((BundleEntryComponent) e).getRequest().getUrl());
					var resourceClass =
							this.context.getResourceDefinition(resourceType).getImplementingClass();
					var res = this.delete(
							resourceClass,
							BundleHelper.getEntryRequestId(version, e).get().withResourceType(resourceType));
					BundleHelper.addEntry(returnBundle, BundleHelper.newEntryWithResource(res.getResource()));
				} else {
					throw new ResourceNotFoundException("Trying to delete an entry without id");
				}

			} else {
				throw new NotImplementedOperationException("Transaction stub only supports PUT, POST or DELETE");
			}
		});

		return returnBundle;
	}

	@Override
	public <R extends IBaseResource, P extends IBaseParameters, T extends IBaseResource> R invoke(
			Class<T> resourceType, String name, P parameters, Class<R> returnType, Map<String, String> headers) {
		return null;
	}

	@Override
	public <R extends IBaseResource, P extends IBaseParameters, I extends IIdType> R invoke(
			I id, String name, P parameters, Class<R> returnType, Map<String, String> headers) {
		return null;
	}

	@Override
	public <B extends IBaseBundle, P extends IBaseParameters> B history(
			P parameters, Class<B> returnType, Map<String, String> headers) {
		throw new NotImplementedOperationException("The history interaction is not currently supported");
	}

	@Override
	public <B extends IBaseBundle, P extends IBaseParameters, T extends IBaseResource> B history(
			Class<T> resourceType, P parameters, Class<B> returnType, Map<String, String> headers) {
		throw new NotImplementedOperationException("The history interaction is not currently supported");
	}

	@Override
	public <B extends IBaseBundle, P extends IBaseParameters, I extends IIdType> B history(
			I id, P parameters, Class<B> returnType, Map<String, String> headers) {
		throw new NotImplementedOperationException("The history interaction is not currently supported");
	}

	@Override
	public @Nonnull FhirContext fhirContext() {
		return this.context;
	}
}
