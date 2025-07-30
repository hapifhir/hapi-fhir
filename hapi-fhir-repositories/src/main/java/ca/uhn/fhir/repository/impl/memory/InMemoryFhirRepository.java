package ca.uhn.fhir.repository.impl.memory;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.repository.IRepository;
import ca.uhn.fhir.repository.impl.NaiveRepositoryTransactionProcessor;
import ca.uhn.fhir.repository.impl.memory.ResourceStorage.ResourceLookup;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Multimap;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 *     <li>Does not support search beyond all-of-type and _id - no SearchParameters are supported.</li>
 *     <li>Does not support search paging.</li>
 *     <li>Does not support extended operations.</li>
 *     <li>Does not support conditional update or create.</li>
 *     <li>Does not support PATCH operations.</li>
 * </ul>
 */
public class InMemoryFhirRepository implements IRepository {
	// Based on org.opencds.cqf.fhir.utility.repository.InMemoryFhirRepository.
	private static final Logger ourLog = LoggerFactory.getLogger(InMemoryFhirRepository.class);

	private String myBaseUrl;
	private final FhirContext myFhirContext;
	private final ResourceStorage myResourceStorage;

	// Factory methods and constructors

	/**
	 * Creates an empty in-memory FHIR repository.
	 */
	public static InMemoryFhirRepository emptyRepository(@Nonnull FhirContext theFhirContext) {
		return new InMemoryFhirRepository(theFhirContext, new HashMap<>());
	}

	/**
	 * Creates an in-memory FHIR repository from the contents of a bundle.
	 * The bundle's resources are indexed by resource type and ID.
	 *
	 * @param theFhirContext the FHIR context
	 * @param theBundle      the bundle containing resources
	 * @return a new InMemoryFhirRepository instance
	 */
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
		myFhirContext = theContext;
		myResourceStorage = new ResourceStorage(theContents);
	}

	// interface methods
	@Override
	public @Nonnull FhirContext fhirContext() {
		return this.myFhirContext;
	}

	@Override
	@SuppressWarnings("unchecked")
	public synchronized <T extends IBaseResource, I extends IIdType> T read(
			Class<T> theResourceType, I theId, Map<String, String> theUnusedHeaders) {
		var lookup = myResourceStorage.lookupResource(getResourceTypeName(theResourceType), theId);

		var resource = lookup.getResourceOrThrow404();

		return (T) resource;
	}

	@Override
	public synchronized <T extends IBaseResource> MethodOutcome create(
			T theResource, Map<String, String> theUnusedHeaders) {
		ResourceLookup created = myResourceStorage.createResource(theResource);

		MethodOutcome outcome = new MethodOutcome(created.id(), true);
		outcome.setResource(created.getResourceOrThrow404());
		outcome.setResponseStatusCode(Constants.STATUS_HTTP_201_CREATED);

		ourLog.debug("Created resource: {}", formatResource(outcome.getResource()));
		return outcome;
	}

	private String formatResource(IBaseResource theResource) {
		return myFhirContext.newJsonParser().encodeResourceToString(theResource);
	}

	@Override
	public synchronized <I extends IIdType, P extends IBaseParameters> MethodOutcome patch(
			I theId, P thePatchParameters, Map<String, String> theHeaders) {
		throw new NotImplementedOperationException("The PATCH operation is not currently supported");
	}

	@Override
	public synchronized <T extends IBaseResource> MethodOutcome update(
			T theResource, Map<String, String> theUnusedHeaders) {
		ResourceLookup lookup = myResourceStorage.lookupResource(
				getResourceTypeName(theResource.getClass()), theResource.getIdElement());

		boolean isCreate = !lookup.isPresent();
		lookup.put(theResource);
		MethodOutcome outcome = new MethodOutcome(lookup.id(), isCreate);
		if (isCreate) {
			outcome.setResponseStatusCode(Constants.STATUS_HTTP_201_CREATED);
		} else {
			outcome.setResponseStatusCode(Constants.STATUS_HTTP_200_OK);
		}
		outcome.setResource(lookup.getResourceOrThrow404());

		ourLog.debug("Updated resource: {}", formatResource(outcome.getResource()));

		return outcome;
	}

	@Override
	public synchronized <T extends IBaseResource, I extends IIdType> MethodOutcome delete(
			Class<T> theResourceType, I theId, Map<String, String> theUnusedHeaders) {
		ResourceLookup lookup = myResourceStorage.lookupResource(getResourceTypeName(theResourceType), theId);

		MethodOutcome methodOutcome = new MethodOutcome(theId, false);
		methodOutcome.setResponseStatusCode(Constants.STATUS_HTTP_204_NO_CONTENT);
		if (lookup.isPresent()) {
			lookup.remove();
		} else {
			var oo = OperationOutcomeUtil.createOperationOutcome(
					OperationOutcomeUtil.OO_SEVERITY_WARN,
					SUCCESSFUL_DELETE_NOT_FOUND.getDisplay(),
					"not-found",
					fhirContext(),
					SUCCESSFUL_DELETE_NOT_FOUND);

			methodOutcome.setOperationOutcome(oo);
		}
		ourLog.debug("Delete resource: {}", theId.getValueAsString());

		return methodOutcome;
	}

	@Override
	public synchronized <B extends IBaseBundle, T extends IBaseResource> B search(
			Class<B> theBundleType,
			Class<T> theResourceType,
			Multimap<String, List<IQueryParameterType>> theSearchParameters,
			Map<String, String> theHeaders) {

		NaiveSearching search = new NaiveSearching(
				fhirContext(),
				myFhirContext.getResourceType(theResourceType),
				id -> this.myResourceStorage.lookupResource(id).getResource().stream(),
				() -> myResourceStorage.getAllOfType(myFhirContext.getResourceType(theResourceType)));

		return search.search(theSearchParameters);
	}

	@Override
	public synchronized <B extends IBaseBundle> B transaction(
			B theTransactionBundle, Map<String, String> theUnusedHeaders) {
		ourLog.debug("Transaction request: {}", formatResource(theTransactionBundle));

		NaiveRepositoryTransactionProcessor transactionProcessor = new NaiveRepositoryTransactionProcessor(this);
		B result = transactionProcessor.processTransaction(theTransactionBundle);

		ourLog.debug("Transaction result: {}", formatResource(result));

		return result;
	}

	// implementation details
	public String getBaseUrl() {
		return myBaseUrl;
	}

	public void setBaseUrl(String theBaseUrl) {
		myBaseUrl = theBaseUrl;
	}

	@VisibleForTesting
	public ResourceStorage getResourceStorage() {
		return myResourceStorage;
	}

	String getResourceTypeName(Class<? extends IBaseResource> theResourceType) {
		return fhirContext().getResourceType(theResourceType);
	}
}
