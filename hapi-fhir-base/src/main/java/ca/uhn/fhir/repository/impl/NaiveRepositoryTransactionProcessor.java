package ca.uhn.fhir.repository.impl;

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.repository.IRepository;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.bundle.BundleEntryParts;
import ca.uhn.fhir.util.bundle.BundleResponseEntryParts;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Implements a naive transaction processor for repositories in terms of crud primitives.
 * SOMEDAY implement GET, PATCH, and other methods as needed.
 * SOMEDAY order entries
 */
public class NaiveRepositoryTransactionProcessor {

	IRepository myRepository;
	public NaiveRepositoryTransactionProcessor(IRepository theRepository) {
		myRepository = theRepository;
	}

	<B extends IBaseBundle> B processTransaction(B transaction, Map<String, String> theHeaders) {
		BundleBuilder bundleBuilder = new BundleBuilder(myRepository.fhirContext());

		bundleBuilder.setType(BundleUtil.BUNDLE_TYPE_TRANSACTION_RESPONSE);

		Function<BundleResponseEntryParts, IBase> responseEntryBuilder =
				BundleResponseEntryParts.builder(myRepository.fhirContext());

		IPrimitiveType<Date> now = (IPrimitiveType<Date>)
				myRepository.fhirContext().getElementDefinition("Instant").newInstance();

		List<BundleEntryParts> entries = BundleUtil.toListOfEntries(myRepository.fhirContext(), transaction);
		for (BundleEntryParts e : entries) {
			switch (e.getMethod()) {
				case PUT -> {
					MethodOutcome methodOutcome = myRepository.update(e.getResource());
					String location = null;
					if (methodOutcome.getResponseStatusCode() == Constants.STATUS_HTTP_201_CREATED) {
						location = methodOutcome.getId().getValue();
					}

					BundleResponseEntryParts response = new BundleResponseEntryParts(
							e.getFullUrl(),
							methodOutcome.getResource(),
							InMemoryFhirRepository.statusCodeToStatusLine(methodOutcome.getResponseStatusCode()),
							location,
							null,
							now,
							methodOutcome.getOperationOutcome());
					bundleBuilder.addEntry(responseEntryBuilder.apply(response));
				}
				case POST -> {
					var responseOutcome = myRepository.create(e.getResource());
					var location = responseOutcome.getId().getValue();

					BundleResponseEntryParts response = new BundleResponseEntryParts(
							e.getFullUrl(),
							responseOutcome.getResource(),
							InMemoryFhirRepository.statusCodeToStatusLine(responseOutcome.getResponseStatusCode()),
							location,
							null,
							now,
							responseOutcome.getOperationOutcome());
					bundleBuilder.addEntry(responseEntryBuilder.apply(response));

				}
				case DELETE -> {
					IdDt idDt = new IdDt(e.getUrl());
					String resourceType = idDt.getResourceType();
					Validate.notBlank(resourceType, "Missing resource type for deletion %s", e.getUrl());

					MethodOutcome responseOutcome = myRepository.delete(myRepository.fhirContext().getResourceDefinition(resourceType).getImplementingClass(), idDt);
					BundleResponseEntryParts response = new BundleResponseEntryParts(
						e.getFullUrl(),
						null,
						InMemoryFhirRepository.statusCodeToStatusLine(responseOutcome.getResponseStatusCode()),
						null,
						null,
						now,
						responseOutcome.getOperationOutcome());
					bundleBuilder.addEntry(responseEntryBuilder.apply(response));
				}
				default -> throw new NotImplementedOperationException(
						"Transaction stub only supports PUT, POST or DELETE");
			}
		}

		return bundleBuilder.getBundleTyped();
	}
}
