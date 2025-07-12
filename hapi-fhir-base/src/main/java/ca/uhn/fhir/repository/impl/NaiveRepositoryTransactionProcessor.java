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
 * SOMEDAY implement GET, PATCH, and other methods as needed.
 * SOMEDAY order entries
 */
class NaiveRepositoryTransactionProcessor {

	private final IRepository myRepository;
	private final BaseRuntimeElementDefinition<IPrimitiveType<Date>> myInstantDefinition;

	NaiveRepositoryTransactionProcessor(IRepository theRepository) {
		myRepository = theRepository;
		//noinspection unchecked
		myInstantDefinition = (BaseRuntimeElementDefinition<IPrimitiveType<Date>>)
				requireNonNull(myRepository.fhirContext().getElementDefinition("Instant"));
	}

	<B extends IBaseBundle> B processTransaction(B transaction) {
		BundleBuilder bundleBuilder = new BundleBuilder(myRepository.fhirContext());

		bundleBuilder.setType(BundleUtil.BUNDLE_TYPE_TRANSACTION_RESPONSE);

		Function<BundleResponseEntryParts, IBase> responseEntryBuilder =
				BundleResponseEntryParts.builder(myRepository.fhirContext());

		IPrimitiveType<Date> now = getCurrentInstant();

		// SOMEDAY: sort the entries by method and dependency order to match the spec
		// SOMEDAY: validate there aren't any unsupported entries before we start to avoid partial-evaluation
		List<BundleEntryParts> entries = BundleUtil.toListOfEntries(myRepository.fhirContext(), transaction);
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
	private BundleResponseEntryParts processPost(BundleEntryParts e, IPrimitiveType<Date> now) {
		// we assume POST is always "create", not an operation invocation
		var responseOutcome = myRepository.create(e.getResource());
		var location = responseOutcome.getId().getValue();

		return new BundleResponseEntryParts(
				e.getFullUrl(),
				responseOutcome.getResource(),
				statusCodeToStatusLine(responseOutcome.getResponseStatusCode()),
				location,
				null,
				now,
				responseOutcome.getOperationOutcome());
	}

	@Nonnull
	private BundleResponseEntryParts processPut(BundleEntryParts e, IPrimitiveType<Date> now) {
		MethodOutcome methodOutcome = myRepository.update(e.getResource());
		String location = null;
		if (methodOutcome.getResponseStatusCode() == Constants.STATUS_HTTP_201_CREATED) {
			location = methodOutcome.getId().getValue();
		}

		return new BundleResponseEntryParts(
				e.getFullUrl(),
				methodOutcome.getResource(),
				statusCodeToStatusLine(methodOutcome.getResponseStatusCode()),
				location,
				null,
				now,
				methodOutcome.getOperationOutcome());
	}

	@Nonnull
	private BundleResponseEntryParts processDelete(BundleEntryParts e, IPrimitiveType<Date> now) {
		IdDt idDt = new IdDt(e.getUrl());
		String resourceType = idDt.getResourceType();
		Validate.notBlank(resourceType, "Missing resource type for deletion %s", e.getUrl());

		MethodOutcome responseOutcome = myRepository.delete(
				myRepository.fhirContext().getResourceDefinition(resourceType).getImplementingClass(), idDt);
		return new BundleResponseEntryParts(
				e.getFullUrl(),
				null,
				statusCodeToStatusLine(responseOutcome.getResponseStatusCode()),
				null,
				null,
				now,
				responseOutcome.getOperationOutcome());
	}

	@Nonnull
	private IPrimitiveType<Date> getCurrentInstant() {
		return myInstantDefinition.newInstance();
	}

	// SOMEDAY find a home for this.  We must do something similar in RestfulServer
	private static String statusCodeToStatusLine(int theResponseStatusCode) {
		return switch (theResponseStatusCode) {
			case Constants.STATUS_HTTP_200_OK -> "200 OK";
			case Constants.STATUS_HTTP_201_CREATED -> "201 Created";
			case Constants.STATUS_HTTP_409_CONFLICT -> "409 Conflict";
			case Constants.STATUS_HTTP_204_NO_CONTENT -> "204 No Content";
			case Constants.STATUS_HTTP_404_NOT_FOUND -> "404 Not Found";
			default -> throw new IllegalArgumentException("Unsupported response status code: " + theResponseStatusCode);
		};
	}
}
