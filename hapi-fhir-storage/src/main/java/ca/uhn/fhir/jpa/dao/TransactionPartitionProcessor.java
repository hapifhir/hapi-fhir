/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.ResourceReferenceInfo;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * This class invokes the {@link Pointcut#STORAGE_TRANSACTION_PRE_PARTITION} hook to slice a transaction request
 * bundle into partitions. It then executes each partition as a separate transaction and aggregates the results
 * into a single cohesive response bundle. The response bundle will have entries which correspond to the original
 * request bundle entries.
 * <p>
 * Note that this does break the FHIR transaction semantics, since it is possible for one partition to succeed
 * and then a later partition to fail. This should therefore only be used in cases where this is understood
 * and desired.
 */
@SuppressWarnings("ClassCanBeRecord")
public class TransactionPartitionProcessor<BUNDLE extends IBaseBundle> {

	private final RequestDetails myRequestDetails;
	private final boolean myNestedMode;
	private final IInterceptorBroadcaster myInterceptorBroadcaster;
	private final String myActionName;
	private final FhirContext myFhirContext;
	private final BaseTransactionProcessor myTransactionProcessor;

	/**
	 * Constructor
	 */
	public TransactionPartitionProcessor(
			BaseTransactionProcessor theTransactionProcessor,
			FhirContext theFhirContext,
			RequestDetails theRequestDetails,
			boolean theNestedMode,
			IInterceptorBroadcaster theCompositeBroadcaster,
			String theActionName) {
		myTransactionProcessor = theTransactionProcessor;
		myFhirContext = theFhirContext;
		myRequestDetails = theRequestDetails;
		myNestedMode = theNestedMode;
		myInterceptorBroadcaster = theCompositeBroadcaster;
		myActionName = theActionName;
	}

	/**
	 * Invoke the {@link Pointcut#STORAGE_TRANSACTION_PRE_PARTITION} hook to slice the transaction request
	 * bundle into partitions, execute each slice, and aggregate the results.
	 */
	public BUNDLE execute(BUNDLE theRequest) {
		HookParams hookParams = new HookParams()
				.add(RequestDetails.class, myRequestDetails)
				.addIfMatchesType(ServletRequestDetails.class, myRequestDetails)
				.add(IBaseBundle.class, theRequest);
		TransactionPrePartitionResponse partitionResponse =
				(TransactionPrePartitionResponse) myInterceptorBroadcaster.callHooksAndReturnObject(
						Pointcut.STORAGE_TRANSACTION_PRE_PARTITION, hookParams);
		Validate.isTrue(
				partitionResponse != null && partitionResponse.splitBundles() != null,
				"Hook for pointcut STORAGE_TRANSACTION_PRE_PARTITION did not return a value");
		List<IBaseBundle> partitionRequestBundles = partitionResponse.splitBundles();

		return processPartitionedBundles(theRequest, partitionRequestBundles);
	}

	@Nonnull
	@SuppressWarnings("unchecked")
	private BUNDLE processPartitionedBundles(BUNDLE theRequest, List<IBaseBundle> thePartitionedRequests) {

		RuntimeResourceDefinition bundleDefinition = myFhirContext.getResourceDefinition("Bundle");
		BaseRuntimeChildDefinition bundleEntryChild = bundleDefinition.getChildByName("entry");
		FhirTerser terser = myFhirContext.newTerser();

		IdentityHashMap<IBase, Integer> originalEntryToIndex = new IdentityHashMap<>();
		List<IBase> originalEntries = bundleEntryChild.getAccessor().getValues(theRequest);
		for (int i = 0; i < originalEntries.size(); i++) {
			originalEntryToIndex.put(originalEntries.get(i), i);
		}

		List<Boolean> entryFoundInPartitions = createListOfNulls(originalEntries.size());

		List<IBase> responseEntries = createListOfNulls(originalEntries.size());

		List<IBaseBundle> partitionedRequests = thePartitionedRequests.stream()
				.filter(t -> !bundleEntryChild.getAccessor().getValues(t).isEmpty())
				.toList();
		for (IBaseBundle singlePartitionRequest : partitionedRequests) {
			for (IBase requestEntry : bundleEntryChild.getAccessor().getValues(singlePartitionRequest)) {
				Integer originalEntryIndex = originalEntryToIndex.get(requestEntry);
				Boolean previousValue = entryFoundInPartitions.set(originalEntryIndex, true);
				if (previousValue != null) {
					throw new InternalErrorException(
							Msg.code(2816) + "Interceptor for pointcut " + Pointcut.STORAGE_TRANSACTION_PRE_PARTITION
									+ " produced multiple partitions for Bundle.entry[" + originalEntryIndex + "]");
				}
			}
		}

		TransactionDetails transactionDetails = new TransactionDetails();
		Map<String, IIdType> idSubstitutions = new HashMap<>();
		for (IBaseBundle singlePartitionRequest : partitionedRequests) {

			// Apply any placeholder ID substitutions from previous partition executions
			for (IBaseResource resource : terser.getAllEmbeddedResources(singlePartitionRequest, true)) {
				List<ResourceReferenceInfo> allRefs = terser.getAllResourceReferences(resource);
				for (ResourceReferenceInfo reference : allRefs) {
					IIdType refElement = reference.getResourceReference().getReferenceElement();
					IIdType substitution = idSubstitutions.get(refElement.getValue());
					if (substitution != null) {
						reference.getResourceReference().setReference(substitution.getValue());
					}
				}
			}

			IBaseBundle singlePartitionResponse = myTransactionProcessor.processTransactionAsSubRequest(
					myRequestDetails, transactionDetails, singlePartitionRequest, myActionName, myNestedMode);

			// Capture any placeholder ID substitutions from this partition
			TransactionUtil.TransactionResponse singlePartitionResponseParsed =
					TransactionUtil.parseTransactionResponse(
							myFhirContext, singlePartitionRequest, singlePartitionResponse);
			for (TransactionUtil.StorageOutcome outcome : singlePartitionResponseParsed.getStorageOutcomes()) {
				if (outcome.getSourceId() != null && outcome.getSourceId().isUuid()) {
					idSubstitutions.put(outcome.getSourceId().getValue(), outcome.getTargetId());
				}
			}

			List<IBase> partitionRequestEntries = bundleEntryChild.getAccessor().getValues(singlePartitionRequest);
			List<IBase> partitionResponseEntries =
					bundleEntryChild.getAccessor().getValues(singlePartitionResponse);
			Validate.isTrue(
					partitionRequestEntries.size() == partitionResponseEntries.size(),
					"Partitioned request and response bundles have different number of entries");

			for (int i = 0; i < partitionRequestEntries.size(); i++) {
				IBase partitionRequestEntry = partitionRequestEntries.get(i);
				IBase partitionResponseEntry = partitionResponseEntries.get(i);
				Integer originalIndex = originalEntryToIndex.get(partitionRequestEntry);
				if (originalIndex != null) {
					responseEntries.set(originalIndex, partitionResponseEntry);
				}
			}
		}

		BUNDLE response = (BUNDLE) bundleDefinition.newInstance();
		BundleUtil.setBundleType(myFhirContext, response, BundleTypeEnum.TRANSACTION_RESPONSE.getCode());
		for (IBase responseEntry : responseEntries) {
			bundleEntryChild.getMutator().addValue(response, responseEntry);
		}
		return response;
	}

	@Nonnull
	private static <T> List<T> createListOfNulls(int theSize) {
		List<T> responseEntries = new ArrayList<>(theSize);
		while (responseEntries.size() < theSize) {
			responseEntries.add(null);
		}
		return responseEntries;
	}
}
