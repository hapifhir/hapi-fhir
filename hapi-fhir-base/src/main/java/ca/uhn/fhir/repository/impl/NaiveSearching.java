package ca.uhn.fhir.repository.impl;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.util.BundleBuilder;
import com.google.common.base.Predicates;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implement the minimum of search functionality.
 * This is a naive implementation that does not support anything except _id and all-of-type searches.
 */
class NaiveSearching {

	// SOMEDAY: Add a matcher that can handle the rest of the search parameters.

	private final FhirContext myFhirContext;

	/** the resource type we are searching for */
	private final String myResourceType;

	/** lookup up by _id, with empty ok. */
	private final Function<IdDt, Stream<IBaseResource>> mySafeRead;

	/** a supplier of all resources of this type, with empty ok. */
	private final Supplier<Collection<IBaseResource>> myAllResourcesSupplier;

	public NaiveSearching(
			FhirContext theFhirContext,
			String theResourceType,
			Function<IdDt, Stream<IBaseResource>> theSafeRead,
			Supplier<Collection<IBaseResource>> theAllResourcesSupplier) {
		myResourceType = theResourceType;
		mySafeRead = theSafeRead;
		myFhirContext = theFhirContext;
		myAllResourcesSupplier = theAllResourcesSupplier;
	}

	@Nonnull
	public <B extends IBaseBundle> B search(Multimap<String, List<IQueryParameterType>> theSearchParameters) {
		// our refining list of possible results
		Collection<IBaseResource> candidates;

		// we need a mutable copy
		Multimap<String, List<IQueryParameterType>> searchParameters = getMutableCopy(theSearchParameters);

		// Remove the _id parameter if present
		Optional<Set<IdDt>> ids = removeAndIntersectIdsIfPresent(searchParameters);

		if (ids.isPresent()) {
			// pluck out results by id
			candidates = ids.get().stream().flatMap(mySafeRead).collect(Collectors.toList());
		} else {
			// no _id parameter, so search all resources of this type
			candidates = myAllResourcesSupplier.get();
		}

		// apply the rest of the search parameters
		candidates =
				candidates.stream().filter(matchPredicate(searchParameters)).toList();

		return buildResultBundle(candidates);
	}

	@Nonnull
	static Multimap<String, List<IQueryParameterType>> getMutableCopy(
			Multimap<String, List<IQueryParameterType>> theSearchParameters) {
		Multimap<String, List<IQueryParameterType>> searchParameters;
		if (theSearchParameters == null) {
			searchParameters = ArrayListMultimap.create();
		} else {
			searchParameters = ArrayListMultimap.create(theSearchParameters);
		}
		return searchParameters;
	}

	@Nonnull
	Optional<Set<IdDt>> removeAndIntersectIdsIfPresent(Multimap<String, List<IQueryParameterType>> searchParameters) {
		Collection<List<IQueryParameterType>> idParams = searchParameters.removeAll("_id");
		// make sure we match on resource type if it is specified, and then qualify the resource type
		return idParams.stream()
				.map(orList -> orList.stream()
						.map(this::normalizeIdParamToIdPart)
						// make sure we match on resource type if it is specified, and then qualify the resource type
						.filter(id -> id.getResourceType() == null || myResourceType.equals(id.getResourceType()))
						.map(id -> id.withResourceType(myResourceType))
						.collect(Collectors.toSet()))
				.reduce(Sets::intersection);
	}

	IdDt normalizeIdParamToIdPart(IQueryParameterType theIdParam) {
		if (theIdParam instanceof IIdType idType) {
			return new IdDt(idType.getValue());
		} else if (theIdParam instanceof ReferenceParam refParam) {
			return new IdDt(refParam.getIdPart());
		} else if (theIdParam instanceof TokenParam tokenParam) {
			return new IdDt(tokenParam.getValue());
		} else if (theIdParam instanceof StringParam stringParam) {
			return new IdDt(stringParam.getValue());
		} else {
			throw new IllegalArgumentException(
					"Unsupported _id parameter type: " + theIdParam.getClass().getName());
		}
	}

	Predicate<IBaseResource> matchPredicate(Multimap<String, List<IQueryParameterType>> theSearchParameters) {
		// SOMEDAY Apply the rest of the params with a matcher
		return Predicates.alwaysTrue();
	}

	<B extends IBaseBundle> B buildResultBundle(Collection<IBaseResource> candidates) {
		// build the result
		BundleBuilder builder = new BundleBuilder(myFhirContext);

		candidates.forEach(builder::addSearchMatchEntry);

		//noinspection unchecked
		return (B) builder.getBundle();
	}
}
