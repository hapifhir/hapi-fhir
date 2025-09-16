package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.ResourceReferenceInfo;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionPartitionProcessor<BUNDLE extends IBaseBundle> {
	private final RequestDetails myRequestDetails;
	private final BUNDLE myRequest;
	private final boolean myNestedMode;
	private final IInterceptorBroadcaster myInterceptorBroadcaster;
	private final String myActionName;
	private final FhirContext myFhirContext;
	private final BaseTransactionProcessor myTransactionProcessor;

	public TransactionPartitionProcessor(BaseTransactionProcessor theTransactionProcessor, FhirContext theFhirContext, RequestDetails theRequestDetails, BUNDLE theRequest, boolean theNestedMode, IInterceptorBroadcaster theCompositeBroadcaster, String theActionName) {
		myTransactionProcessor = theTransactionProcessor;
		myFhirContext = theFhirContext;
		myRequestDetails = theRequestDetails;
		myRequest = theRequest;
		myNestedMode = theNestedMode;
		myInterceptorBroadcaster = theCompositeBroadcaster;
		myActionName = theActionName;
	}

	public BUNDLE execute() {
			HookParams hookParams = new HookParams()
				.add(RequestDetails.class, myRequestDetails)
				.addIfMatchesType(ServletRequestDetails.class, myRequestDetails)
				.add(IBaseBundle.class, myRequest);
			TransactionPrePartitionResponse partitionResponse =
				(TransactionPrePartitionResponse) myInterceptorBroadcaster.callHooksAndReturnObject(
					Pointcut.STORAGE_TRANSACTION_PRE_PARTITION, hookParams);
			Validate.isTrue(
				partitionResponse != null && partitionResponse.splitBundles() != null,
				"Hook for pointcut STORAGE_TRANSACTION_PRE_PARTITION did not return a value");

			BaseRuntimeChildDefinition bundleEntryChild =
				myFhirContext.getResourceDefinition("Bundle").getChildByName("entry");
			FhirTerser terser = myFhirContext.newTerser();

			IBaseBundle response = null;
			Map<String, IIdType> idSubstitutions = new HashMap<>();
			for (IBaseBundle singlePartitionRequest : partitionResponse.splitBundles()) {

				// Apply any placeholder ID substitutions from previous partition executions
				for (IBaseResource resource :
					terser.getAllEmbeddedResources(singlePartitionRequest, true)) {
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
					myRequestDetails, singlePartitionRequest, myActionName, myNestedMode);

				// Capture any placeholder ID substitutions from this partition
				TransactionUtil.TransactionResponse singlePartitionResponseParsed =
					TransactionUtil.parseTransactionResponse(
						myFhirContext, singlePartitionRequest, singlePartitionResponse);
				for (TransactionUtil.StorageOutcome outcome : singlePartitionResponseParsed.getStorageOutcomes()) {
					if (outcome.getSourceId() != null && outcome.getSourceId().isUuid()) {
						idSubstitutions.put(outcome.getSourceId().getValue(), outcome.getTargetId());
					}
				}

				if (response == null) {
					response = singlePartitionResponse;
				} else {
					List<IBase> entries = bundleEntryChild.getAccessor().getValues(singlePartitionResponse);
					for (IBase entry : entries) {
						bundleEntryChild.getMutator().addValue(response, entry);
					}
				}
			}
			return (BUNDLE) response;

	}
}
