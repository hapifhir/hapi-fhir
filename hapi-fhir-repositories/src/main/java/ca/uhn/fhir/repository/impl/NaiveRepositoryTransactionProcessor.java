package ca.uhn.fhir.repository.impl;

import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.repository.IRepository;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.bundle.BundleEntryParts;
import ca.uhn.fhir.util.bundle.BundleResponseEntryParts;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

/**
 * Implements a naive transaction processor for repositories in terms of crud primitives.
 */
public class NaiveRepositoryTransactionProcessor {
	// SOMEDAY implement GET, PATCH, and other methods as needed.
	// SOMEDAY order entries
	// SOMEDAY distinguish batch and transaction processing.  Not really a problem yet since we can't fail any of our
	// operations.

	protected final IRepository myRepository;
	private final BaseRuntimeElementDefinition<IPrimitiveType<Date>> myInstantDefinition;

	/**
	 * @param theRepository the repository to use for crud operations during transaction processing.
	 */
	public NaiveRepositoryTransactionProcessor(IRepository theRepository) {
		myRepository = theRepository;
		//noinspection unchecked
		myInstantDefinition = (BaseRuntimeElementDefinition<IPrimitiveType<Date>>)
				requireNonNull(myRepository.fhirContext().getElementDefinition("Instant"));
	}

	/**
	 * Processes a transaction bundle by executing the contained entries as create, update, or delete operations against the delegate repository.
	 */
	public <B extends IBaseBundle> B processTransaction(B theTransactionBundle) {
		BundleBuilder bundleBuilder = new BundleBuilder(myRepository.fhirContext());

		bundleBuilder.setType(BundleUtil.BUNDLE_TYPE_TRANSACTION_RESPONSE);

		Function<BundleResponseEntryParts, IBase> responseEntryBuilder =
				BundleResponseEntryParts.builder(myRepository.fhirContext());

		IPrimitiveType<Date> now = getCurrentInstant();

		// SOMEDAY: sort the entries by method and dependency order to match the spec
		// SOMEDAY: validate there aren't any unsupported entries before we start to avoid partial-evaluation
		List<BundleEntryParts> entries = BundleUtil.toListOfEntries(myRepository.fhirContext(), theTransactionBundle);
		for (BundleEntryParts e : entries) {
			BundleResponseEntryParts responseEntry =
					switch (e.getMethod()) {
						case POST -> processPost(e, now);
						case PUT -> processPut(e, now);
						case DELETE -> processDelete(e, now);
						default -> throw new NotImplementedOperationException(
								"Transaction stub only supports POST, PUT, or DELETE");
					};
			bundleBuilder.addEntry(responseEntryBuilder.apply(responseEntry));
		}

		return bundleBuilder.getBundleTyped();
	}

	@Nonnull
	protected BundleResponseEntryParts processPost(
			BundleEntryParts theBundleEntryParts, IPrimitiveType<Date> theInstant) {
		// we assume POST is always "create", not an operation invocation
		var responseOutcome = myRepository.create(theBundleEntryParts.getResource());
		var location = responseOutcome.getId().getValue();

		return new BundleResponseEntryParts(
				theBundleEntryParts.getFullUrl(),
				responseOutcome.getResource(),
				statusCodeToStatusLine(responseOutcome.getResponseStatusCode()),
				location,
				null,
				theInstant,
				responseOutcome.getOperationOutcome());
	}

	@Nonnull
	protected BundleResponseEntryParts processPut(
			BundleEntryParts theBundleEntryParts, IPrimitiveType<Date> theInstant) {
		MethodOutcome methodOutcome = myRepository.update(theBundleEntryParts.getResource());
		String location = null;
		if (methodOutcome.getResponseStatusCode() == Constants.STATUS_HTTP_201_CREATED) {
			location = methodOutcome.getId().getValue();
		}

		return new BundleResponseEntryParts(
				theBundleEntryParts.getFullUrl(),
				methodOutcome.getResource(),
				statusCodeToStatusLine(methodOutcome.getResponseStatusCode()),
				location,
				null,
				theInstant,
				methodOutcome.getOperationOutcome());
	}

	@Nonnull
	protected BundleResponseEntryParts processDelete(
			BundleEntryParts theBundleEntryParts, IPrimitiveType<Date> theInstant) {
		IdDt idDt = new IdDt(theBundleEntryParts.getUrl());
		String resourceType = idDt.getResourceType();
		Validate.notBlank(resourceType, "Missing resource type for deletion %s", theBundleEntryParts.getUrl());

		MethodOutcome responseOutcome = myRepository.delete(
				myRepository.fhirContext().getResourceDefinition(resourceType).getImplementingClass(), idDt);
		return new BundleResponseEntryParts(
				theBundleEntryParts.getFullUrl(),
				null,
				statusCodeToStatusLine(responseOutcome.getResponseStatusCode()),
				null,
				null,
				theInstant,
				responseOutcome.getOperationOutcome());
	}

	@Nonnull
	protected IPrimitiveType<Date> getCurrentInstant() {
		return myInstantDefinition.newInstance();
	}

	// SOMEDAY find a home for this.  We must do something similar in RestfulServer
	protected static String statusCodeToStatusLine(int theResponseStatusCode) {
		return switch (theResponseStatusCode) {
			case Constants.STATUS_HTTP_200_OK, 0 -> "200 OK";
			case Constants.STATUS_HTTP_201_CREATED -> "201 Created";
			case Constants.STATUS_HTTP_409_CONFLICT -> "409 Conflict";
			case Constants.STATUS_HTTP_204_NO_CONTENT -> "204 No Content";
			case Constants.STATUS_HTTP_404_NOT_FOUND -> "404 Not Found";
			default -> throw new IllegalArgumentException("Unsupported response status code: " + theResponseStatusCode);
		};
	}
}
