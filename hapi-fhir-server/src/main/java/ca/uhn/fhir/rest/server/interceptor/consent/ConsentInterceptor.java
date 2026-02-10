/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.rest.server.interceptor.consent;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IPreResourceAccessDetails;
import ca.uhn.fhir.rest.api.server.IPreResourceShowDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.ResponseDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationConstants;
import ca.uhn.fhir.rest.server.util.ICachedSearchDetails;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.IModelVisitor2;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ca.uhn.fhir.rest.api.Constants.URL_TOKEN_METADATA;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_META;

/**
 * The ConsentInterceptor can be used to apply arbitrary consent rules and data access policies
 * on responses from a FHIR server.
 * <p>
 * See <a href="https://hapifhir.io/hapi-fhir/docs/security/consent_interceptor.html">Consent Interceptor</a> for
 * more information on this interceptor.
 * </p>
 */
@Interceptor(order = AuthorizationConstants.ORDER_CONSENT_INTERCEPTOR)
public class ConsentInterceptor {
	private static final AtomicInteger ourInstanceCount = new AtomicInteger(0);
	private final int myInstanceIndex = ourInstanceCount.incrementAndGet();
	private final String myRequestAuthorizedKey =
			ConsentInterceptor.class.getName() + "_" + myInstanceIndex + "_AUTHORIZED";
	private final String myRequestCompletedKey =
			ConsentInterceptor.class.getName() + "_" + myInstanceIndex + "_COMPLETED";
	private final String myRequestSeenResourcesKey =
			ConsentInterceptor.class.getName() + "_" + myInstanceIndex + "_SEENRESOURCES";

	private static final String USER_DATA_SHOULD_SKIP_CONSENT_FOR_SYSTEM_OPERATIONS =
			"request_details_user_data_should_skip_consent";

	private volatile List<IConsentService> myConsentService = Collections.emptyList();
	private IConsentContextServices myContextConsentServices = IConsentContextServices.NULL_IMPL;

	/**
	 * Constructor
	 */
	public ConsentInterceptor() {
		super();
	}

	/**
	 * Constructor
	 *
	 * @param theConsentService Must not be <code>null</code>
	 */
	public ConsentInterceptor(IConsentService theConsentService) {
		this(theConsentService, IConsentContextServices.NULL_IMPL);
	}

	/**
	 * Constructor
	 *
	 * @param theConsentService         Must not be <code>null</code>
	 * @param theContextConsentServices Must not be <code>null</code>
	 */
	public ConsentInterceptor(IConsentService theConsentService, IConsentContextServices theContextConsentServices) {
		setConsentService(theConsentService);
		setContextConsentServices(theContextConsentServices);
	}

	public void setContextConsentServices(IConsentContextServices theContextConsentServices) {
		Validate.notNull(theContextConsentServices, "theContextConsentServices must not be null");
		myContextConsentServices = theContextConsentServices;
	}

	/**
	 * @deprecated Use {@link #registerConsentService(IConsentService)} instead
	 */
	@Deprecated
	public void setConsentService(IConsentService theConsentService) {
		Validate.notNull(theConsentService, "theConsentService must not be null");
		myConsentService = Collections.singletonList(theConsentService);
	}

	/**
	 * Adds a consent service to the chain.
	 * <p>
	 * Thread safety note: This method can be called while the service is actively processing requestes
	 *
	 * @param theConsentService The service to register. Must not be <code>null</code>.
	 * @since 6.0.0
	 */
	public ConsentInterceptor registerConsentService(IConsentService theConsentService) {
		Validate.notNull(theConsentService, "theConsentService must not be null");
		List<IConsentService> newList = new ArrayList<>(myConsentService.size() + 1);
		newList.addAll(myConsentService);
		newList.add(theConsentService);
		myConsentService = newList;
		return this;
	}

	/**
	 * Removes a consent service from the chain.
	 * <p>
	 * Thread safety note: This method can be called while the service is actively processing requestes
	 *
	 * @param theConsentService The service to unregister. Must not be <code>null</code>.
	 * @since 6.0.0
	 */
	public ConsentInterceptor unregisterConsentService(IConsentService theConsentService) {
		Validate.notNull(theConsentService, "theConsentService must not be null");
		List<IConsentService> newList =
				myConsentService.stream().filter(t -> t != theConsentService).collect(Collectors.toList());
		myConsentService = newList;
		return this;
	}

	@VisibleForTesting
	public List<IConsentService> getConsentServices() {
		return Collections.unmodifiableList(myConsentService);
	}

	@Hook(value = Pointcut.SERVER_INCOMING_REQUEST_PRE_HANDLED)
	public void interceptPreHandled(RequestDetails theRequestDetails) {
		if (isSkipServiceForRequest(theRequestDetails)) {
			return;
		}

		validateParameter(theRequestDetails.getParameters());

		for (IConsentService nextService : myConsentService) {
			ConsentOutcome outcome = nextService.startOperation(theRequestDetails, myContextConsentServices);
			Validate.notNull(outcome, "Consent service returned null outcome");

			switch (outcome.getStatus()) {
				case REJECT:
					throw toForbiddenOperationException(outcome);
				case PROCEED:
					continue;
				case AUTHORIZED:
					authorizeRequest(theRequestDetails);
					return;
			}
		}
	}

	protected void authorizeRequest(RequestDetails theRequestDetails) {
		Map<Object, Object> userData = theRequestDetails.getUserData();
		userData.put(myRequestAuthorizedKey, Boolean.TRUE);
	}

	/**
	 * Check if this request is eligible for cached search results.
	 * We can't use a cached result if consent may use canSeeResource.
	 * This checks for AUTHORIZED requests, and the responses from shouldProcessCanSeeResource()
	 * to see if this holds.
	 * @return may the request be satisfied from cache.
	 */
	@Hook(value = Pointcut.STORAGE_PRECHECK_FOR_CACHED_SEARCH)
	public boolean interceptPreCheckForCachedSearch(@Nonnull RequestDetails theRequestDetails) {
		return !isProcessCanSeeResource(theRequestDetails, null);
	}

	/**
	 * Check if the search results from this request might be reused by later searches.
	 * We can't use a cached result if consent may use canSeeResource.
	 * This checks for AUTHORIZED requests, and the responses from shouldProcessCanSeeResource()
	 * to see if this holds.
	 * If not, marks the result as single-use.
	 */
	@Hook(value = Pointcut.STORAGE_PRESEARCH_REGISTERED)
	public void interceptPreSearchRegistered(
			RequestDetails theRequestDetails, ICachedSearchDetails theCachedSearchDetails) {
		if (isProcessCanSeeResource(theRequestDetails, null)) {
			theCachedSearchDetails.setCannotBeReused();
		}
	}

	@Hook(value = Pointcut.STORAGE_PREACCESS_RESOURCES)
	public void interceptPreAccess(
			RequestDetails theRequestDetails, IPreResourceAccessDetails thePreResourceAccessDetails) {

		// Flags for each service
		boolean[] processConsentSvcs = new boolean[myConsentService.size()];
		boolean processAnyConsentSvcs = isProcessCanSeeResource(theRequestDetails, processConsentSvcs);

		if (!processAnyConsentSvcs) {
			return;
		}

		IdentityHashMap<IBaseResource, ConsentOperationStatusEnum> alreadySeenResources =
				getAlreadySeenResourcesMap(theRequestDetails);
		for (int resourceIdx = 0; resourceIdx < thePreResourceAccessDetails.size(); resourceIdx++) {
			IBaseResource nextResource = thePreResourceAccessDetails.getResource(resourceIdx);
			for (int consentSvcIdx = 0; consentSvcIdx < myConsentService.size(); consentSvcIdx++) {
				IConsentService nextService = myConsentService.get(consentSvcIdx);

				if (!processConsentSvcs[consentSvcIdx]) {
					continue;
				}

				ConsentOutcome outcome =
						nextService.canSeeResource(theRequestDetails, nextResource, myContextConsentServices);
				Validate.notNull(outcome, "Consent service returned null outcome");
				Validate.isTrue(
						outcome.getResource() == null,
						"Consent service returned a resource in its outcome. This is not permitted in canSeeResource(..)");

				boolean skipSubsequentServices = false;
				switch (outcome.getStatus()) {
					case PROCEED:
						break;
					case AUTHORIZED:
						alreadySeenResources.put(nextResource, ConsentOperationStatusEnum.AUTHORIZED);
						skipSubsequentServices = true;
						break;
					case REJECT:
						alreadySeenResources.put(nextResource, ConsentOperationStatusEnum.REJECT);
						thePreResourceAccessDetails.setDontReturnResourceAtIndex(resourceIdx);
						skipSubsequentServices = true;
						break;
				}

				if (skipSubsequentServices) {
					break;
				}
			}
		}
	}

	/**
	 * Is canSeeResource() active in any services?
	 * @param theProcessConsentSvcsFlags filled in with the responses from shouldProcessCanSeeResource each service
	 * @return true of any service responded true to shouldProcessCanSeeResource()
	 */
	private boolean isProcessCanSeeResource(
			@Nonnull RequestDetails theRequestDetails, @Nullable boolean[] theProcessConsentSvcsFlags) {
		if (isRequestAuthorized(theRequestDetails)) {
			return false;
		}
		if (isSkipServiceForRequest(theRequestDetails)) {
			return false;
		}
		if (myConsentService.isEmpty()) {
			return false;
		}

		if (theProcessConsentSvcsFlags == null) {
			theProcessConsentSvcsFlags = new boolean[myConsentService.size()];
		}
		Validate.isTrue(theProcessConsentSvcsFlags.length == myConsentService.size());
		boolean processAnyConsentSvcs = false;
		for (int consentSvcIdx = 0; consentSvcIdx < myConsentService.size(); consentSvcIdx++) {
			IConsentService nextService = myConsentService.get(consentSvcIdx);

			boolean shouldCallCanSeeResource =
					nextService.shouldProcessCanSeeResource(theRequestDetails, myContextConsentServices);
			processAnyConsentSvcs |= shouldCallCanSeeResource;
			theProcessConsentSvcsFlags[consentSvcIdx] = shouldCallCanSeeResource;
		}
		return processAnyConsentSvcs;
	}

	@Hook(value = Pointcut.STORAGE_PRESHOW_RESOURCES)
	public void interceptPreShow(RequestDetails theRequestDetails, IPreResourceShowDetails thePreResourceShowDetails) {
		if (isRequestAuthorized(theRequestDetails)) {
			return;
		}
		if (isAllowListedRequest(theRequestDetails)) {
			return;
		}
		if (isSkipServiceForRequest(theRequestDetails)) {
			return;
		}
		if (myConsentService.isEmpty()) {
			return;
		}

		IdentityHashMap<IBaseResource, ConsentOperationStatusEnum> alreadySeenResources =
				getAlreadySeenResourcesMap(theRequestDetails);

		for (int i = 0; i < thePreResourceShowDetails.size(); i++) {

			IBaseResource resource = thePreResourceShowDetails.getResource(i);
			if (resource == null
					|| alreadySeenResources.putIfAbsent(resource, ConsentOperationStatusEnum.PROCEED) != null) {
				continue;
			}

			for (IConsentService nextService : myConsentService) {
				ConsentOutcome nextOutcome =
						nextService.willSeeResource(theRequestDetails, resource, myContextConsentServices);
				IBaseResource newResource = nextOutcome.getResource();

				switch (nextOutcome.getStatus()) {
					case PROCEED:
						if (newResource != null) {
							thePreResourceShowDetails.setResource(i, newResource);
							resource = newResource;
						}
						continue;
					case AUTHORIZED:
						alreadySeenResources.put(resource, ConsentOperationStatusEnum.AUTHORIZED);
						if (newResource != null) {
							thePreResourceShowDetails.setResource(i, newResource);
						}
						continue;
					case REJECT:
						alreadySeenResources.put(resource, ConsentOperationStatusEnum.REJECT);
						if (nextOutcome.getOperationOutcome() != null) {
							IBaseOperationOutcome newOperationOutcome = nextOutcome.getOperationOutcome();
							thePreResourceShowDetails.setResource(i, newOperationOutcome);
							alreadySeenResources.put(newOperationOutcome, ConsentOperationStatusEnum.PROCEED);
						} else {
							resource = null;
							thePreResourceShowDetails.setResource(i, null);
						}
						continue;
				}
			}
		}
	}

	@Hook(value = Pointcut.SERVER_OUTGOING_RESPONSE)
	public void interceptOutgoingResponse(RequestDetails theRequestDetails, ResponseDetails theResponseDetails) {
		if (theResponseDetails.getResponseResource() == null) {
			return;
		}
		if (isRequestAuthorized(theRequestDetails)) {
			return;
		}
		if (isAllowListedRequest(theRequestDetails)) {
			return;
		}
		if (isSkipServiceForRequest(theRequestDetails)) {
			return;
		}
		if (myConsentService.isEmpty()) {
			return;
		}

		// Take care of outer resource first
		IdentityHashMap<IBaseResource, ConsentOperationStatusEnum> alreadySeenResources =
				getAlreadySeenResourcesMap(theRequestDetails);
		if (alreadySeenResources.containsKey(theResponseDetails.getResponseResource())) {
			// we've already seen this resource before
			ConsentOperationStatusEnum decisionOnResource =
					alreadySeenResources.get(theResponseDetails.getResponseResource());

			if (ConsentOperationStatusEnum.AUTHORIZED.equals(decisionOnResource)
					|| ConsentOperationStatusEnum.REJECT.equals(decisionOnResource)) {
				// the consent service decision on the resource was AUTHORIZED or REJECT.
				// In both cases, we can immediately return without checking children
				return;
			}
		} else {
			// we haven't seen this resource before
			// mark it as seen now, set the initial consent decision value to PROCEED by default,
			// we will update if it changes another value below
			alreadySeenResources.put(theResponseDetails.getResponseResource(), ConsentOperationStatusEnum.PROCEED);

			for (IConsentService next : myConsentService) {
				final ConsentOutcome outcome = next.willSeeResource(
						theRequestDetails, theResponseDetails.getResponseResource(), myContextConsentServices);
				if (outcome.getResource() != null) {
					theResponseDetails.setResponseResource(outcome.getResource());
				}

				// Clear the total
				if (theResponseDetails.getResponseResource() instanceof IBaseBundle) {
					BundleUtil.setTotal(
							theRequestDetails.getFhirContext(),
							(IBaseBundle) theResponseDetails.getResponseResource(),
							null);
				}

				switch (outcome.getStatus()) {
					case REJECT:
						alreadySeenResources.put(
								theResponseDetails.getResponseResource(), ConsentOperationStatusEnum.REJECT);
						if (outcome.getOperationOutcome() != null) {
							theResponseDetails.setResponseResource(outcome.getOperationOutcome());
						} else {
							theResponseDetails.setResponseResource(null);
							theResponseDetails.setResponseCode(Constants.STATUS_HTTP_204_NO_CONTENT);
						}
						// Return immediately
						return;
					case AUTHORIZED:
						alreadySeenResources.put(
								theResponseDetails.getResponseResource(), ConsentOperationStatusEnum.AUTHORIZED);
						// Don't check children, so return immediately
						return;
					case PROCEED:
						// Check children, so proceed
						break;
				}
			}
		}

		// See child resources
		IBaseResource outerResource = theResponseDetails.getResponseResource();
		FhirContext ctx = theRequestDetails.getServer().getFhirContext();
		IModelVisitor2 visitor = new IModelVisitor2() {
			@Override
			public boolean acceptElement(
					IBase theElement,
					List<IBase> theContainingElementPath,
					List<BaseRuntimeChildDefinition> theChildDefinitionPath,
					List<BaseRuntimeElementDefinition<?>> theElementDefinitionPath) {

				// Clear the total
				if (theElement instanceof IBaseBundle) {
					BundleUtil.setTotal(theRequestDetails.getFhirContext(), (IBaseBundle) theElement, null);
				}

				if (theElement == outerResource) {
					return true;
				}

				// Primitive elements can't contain any embedded resources, so we don't need to
				// descend into them (and any extensions they might hold)
				if (theElement instanceof IPrimitiveType<?>) {
					return false;
				}

				if (theElement instanceof IBaseResource) {
					IBaseResource resource = (IBaseResource) theElement;
					if (alreadySeenResources.putIfAbsent(resource, ConsentOperationStatusEnum.PROCEED) != null) {
						return true;
					}

					boolean shouldCheckChildren = true;
					for (IConsentService next : myConsentService) {
						ConsentOutcome childOutcome =
								next.willSeeResource(theRequestDetails, resource, myContextConsentServices);

						IBaseResource replacementResource = null;
						boolean shouldReplaceResource = false;

						switch (childOutcome.getStatus()) {
							case REJECT:
								replacementResource = childOutcome.getOperationOutcome();
								shouldReplaceResource = true;
								alreadySeenResources.put(resource, ConsentOperationStatusEnum.REJECT);
								break;
							case PROCEED:
								replacementResource = childOutcome.getResource();
								shouldReplaceResource = replacementResource != null;
								break;
							case AUTHORIZED:
								replacementResource = childOutcome.getResource();
								shouldReplaceResource = replacementResource != null;
								shouldCheckChildren = false;
								alreadySeenResources.put(resource, ConsentOperationStatusEnum.AUTHORIZED);
								break;
						}

						if (shouldReplaceResource) {
							IBase container = theContainingElementPath.get(theContainingElementPath.size() - 2);
							BaseRuntimeChildDefinition containerChildElement =
									theChildDefinitionPath.get(theChildDefinitionPath.size() - 1);
							containerChildElement.getMutator().setValue(container, replacementResource);
							resource = replacementResource;
						}
					}

					return shouldCheckChildren;
				}

				return true;
			}

			@Override
			public boolean acceptUndeclaredExtension(
					IBaseExtension<?, ?> theNextExt,
					List<IBase> theContainingElementPath,
					List<BaseRuntimeChildDefinition> theChildDefinitionPath,
					List<BaseRuntimeElementDefinition<?>> theElementDefinitionPath) {
				return true;
			}
		};
		ctx.newTerser().visit(outerResource, visitor);
	}

	@Hook(value = Pointcut.SERVER_HANDLE_EXCEPTION)
	public void requestFailed(RequestDetails theRequest, BaseServerResponseException theException) {
		theRequest.getUserData().put(myRequestCompletedKey, Boolean.TRUE);
		for (IConsentService next : myConsentService) {
			next.completeOperationFailure(theRequest, theException, myContextConsentServices);
		}
	}

	@Hook(value = Pointcut.SERVER_PROCESSING_COMPLETED_NORMALLY)
	public void requestSucceeded(RequestDetails theRequest) {
		if (Boolean.TRUE.equals(theRequest.getUserData().get(myRequestCompletedKey))) {
			return;
		}
		for (IConsentService next : myConsentService) {
			next.completeOperationSuccess(theRequest, myContextConsentServices);
		}
	}

	protected RequestDetails getRequestDetailsForCurrentExportOperation(
			BulkExportJobParameters theParameters, IBaseResource theBaseResource) {
		// bulk exports are system operations
		SystemRequestDetails details = new SystemRequestDetails();
		return details;
	}

	@Hook(value = Pointcut.STORAGE_BULK_EXPORT_RESOURCE_INCLUSION)
	public boolean shouldBulkExportIncludeResource(BulkExportJobParameters theParameters, IBaseResource theResource) {
		RequestDetails requestDetails = getRequestDetailsForCurrentExportOperation(theParameters, theResource);

		for (IConsentService next : myConsentService) {
			ConsentOutcome nextOutcome = next.canSeeResource(requestDetails, theResource, myContextConsentServices);
			ConsentOperationStatusEnum status = nextOutcome.getStatus();
			if (ConsentOperationStatusEnum.REJECT.equals(status)) {
				// if any consent service rejects, reject the resource
				return false;
			}

			nextOutcome = next.willSeeResource(requestDetails, theResource, myContextConsentServices);
			status = nextOutcome.getStatus();
			if (ConsentOperationStatusEnum.REJECT.equals(status)) {
				// if any consent service rejects, reject the resource
				return false;
			}
		}

		// default is to include the resource
		return true;
	}

	private boolean isRequestAuthorized(RequestDetails theRequestDetails) {
		boolean retVal = false;
		if (theRequestDetails != null) {
			Object authorizedObj = theRequestDetails.getUserData().get(myRequestAuthorizedKey);
			retVal = Boolean.TRUE.equals(authorizedObj);
		}
		return retVal;
	}

	private boolean isSkipServiceForRequest(RequestDetails theRequestDetails) {
		// TODO MM: we could potentially aggregate all checks to skip consent into a single method
		// isRequestAuthorized, isAllowListed into isSkipServiceForRequest
		return isMetadataPath(theRequestDetails)
				|| isMetaOperation(theRequestDetails)
				|| shouldSkipAllConsent(theRequestDetails);
	}

	private boolean isAllowListedRequest(RequestDetails theRequestDetails) {
		return isMetadataPath(theRequestDetails) || isMetaOperation(theRequestDetails);
	}

	private boolean isMetaOperation(RequestDetails theRequestDetails) {
		return theRequestDetails != null && OPERATION_META.equals(theRequestDetails.getOperation());
	}

	private boolean isMetadataPath(RequestDetails theRequestDetails) {
		return theRequestDetails != null && URL_TOKEN_METADATA.equals(theRequestDetails.getRequestPath());
	}

	/**
	 * Call this method to bypass consent checking for a particular request {@link RequestDetails}.
	 * Skipping consent is needed for resources that are modified in async system processing
	 * e.g. SearchParameter initialization with subscriptions and subscription (matching) messages enabled.
	 * This is a short term solution and is to be replaced by a long term solution.
	 * {@see https://github.com/hapifhir/hapi-fhir/issues/7542}
	 * @param theRequestDetails the request
	 */
	public static void skipAllConsentForRequest(@Nonnull RequestDetails theRequestDetails) {
		theRequestDetails.getUserData().put(USER_DATA_SHOULD_SKIP_CONSENT_FOR_SYSTEM_OPERATIONS, true);
	}

	private static boolean shouldSkipAllConsent(@Nullable RequestDetails theRequestDetails) {
		return theRequestDetails != null
				&& (Boolean) theRequestDetails
						.getUserData()
						.getOrDefault(USER_DATA_SHOULD_SKIP_CONSENT_FOR_SYSTEM_OPERATIONS, Boolean.FALSE);
	}

	private void validateParameter(Map<String, String[]> theParameterMap) {
		if (theParameterMap != null) {
			if (theParameterMap.containsKey(Constants.PARAM_SEARCH_TOTAL_MODE)
					&& Arrays.stream(theParameterMap.get("_total")).anyMatch("accurate"::equals)) {
				throw new InvalidRequestException(Msg.code(2037) + Constants.PARAM_SEARCH_TOTAL_MODE
						+ "=accurate is not permitted on this server");
			}
			if (theParameterMap.containsKey(Constants.PARAM_SUMMARY)
					&& Arrays.stream(theParameterMap.get("_summary")).anyMatch("count"::equals)) {
				throw new InvalidRequestException(
						Msg.code(2038) + Constants.PARAM_SUMMARY + "=count is not permitted on this server");
			}
		}
	}

	/**
	 * The map returned by this method keeps track of the resources already processed by ConsentInterceptor in the
	 * context of a request.
	 * If the map contains a particular resource, it means that the resource has already been processed and the value
	 * is the status returned by consent services for that resource.
	 * @param theRequestDetails
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private IdentityHashMap<IBaseResource, ConsentOperationStatusEnum> getAlreadySeenResourcesMap(
			RequestDetails theRequestDetails) {
		if (theRequestDetails == null) {
			return new IdentityHashMap<>();
		}
		IdentityHashMap<IBaseResource, ConsentOperationStatusEnum> alreadySeenResources =
				(IdentityHashMap<IBaseResource, ConsentOperationStatusEnum>)
						theRequestDetails.getUserData().get(myRequestSeenResourcesKey);
		if (alreadySeenResources == null) {
			alreadySeenResources = new IdentityHashMap<>();
			theRequestDetails.getUserData().put(myRequestSeenResourcesKey, alreadySeenResources);
		}
		return alreadySeenResources;
	}

	private static ForbiddenOperationException toForbiddenOperationException(ConsentOutcome theOutcome) {
		IBaseOperationOutcome operationOutcome = null;
		if (theOutcome.getOperationOutcome() != null) {
			operationOutcome = theOutcome.getOperationOutcome();
		}
		return new ForbiddenOperationException("Rejected by consent service", operationOutcome);
	}
}
