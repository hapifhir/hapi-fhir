package ca.uhn.fhir.jpa.subscription.match.deliver.resthook;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.subscription.match.deliver.BaseSubscriptionDeliverySubscriber;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.client.api.Header;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.IHttpClient;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.interceptor.SimpleRequestHeaderInterceptor;
import ca.uhn.fhir.rest.gclient.IClientExecutable;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.messaging.BaseResourceModifiedMessage;
import ca.uhn.fhir.util.BundleBuilder;
import org.apache.commons.text.StringSubstitutor;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.MessagingException;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ca.uhn.fhir.jpa.subscription.util.SubscriptionUtil.createRequestDetailForPartitionedRequest;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Scope("prototype")
public class SubscriptionDeliveringRestHookSubscriber extends BaseSubscriptionDeliverySubscriber {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionDeliveringRestHookSubscriber.class);

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private MatchUrlService myMatchUrlService;

	/**
	 * Constructor
	 */
	public SubscriptionDeliveringRestHookSubscriber() {
		super();
	}

	protected void deliverPayload(ResourceDeliveryMessage theMsg, CanonicalSubscription theSubscription, EncodingEnum thePayloadType, IGenericClient theClient) {
		IBaseResource payloadResource = getAndMassagePayload(theMsg, theSubscription);

		// Regardless of whether we have a payload, the rest-hook should be sent.
		doDelivery(theMsg, theSubscription, thePayloadType, theClient, payloadResource);
	}

	protected void doDelivery(ResourceDeliveryMessage theMsg, CanonicalSubscription theSubscription, EncodingEnum thePayloadType, IGenericClient theClient, IBaseResource thePayloadResource) {
		IClientExecutable<?, ?> operation;

		if (isNotBlank(theSubscription.getPayloadSearchCriteria())) {
			operation = createDeliveryRequestTransaction(theSubscription, theClient, thePayloadResource);
		} else if (thePayloadType != null) {
			operation = createDeliveryRequestNormal(theMsg, theClient, thePayloadResource);
		} else {
			sendNotification(theMsg);
			operation = null;
		}

		if (operation != null) {

			if (thePayloadType != null) {
				operation.encoded(thePayloadType);
			}

			String payloadId = thePayloadResource.getIdElement().toUnqualified().getValue();
			ourLog.info("Delivering {} rest-hook payload {} for {}", theMsg.getOperationType(), payloadId, theSubscription.getIdElement(myFhirContext).toUnqualifiedVersionless().getValue());

			try {
				operation.execute();
			} catch (ResourceNotFoundException e) {
				ourLog.error("Cannot reach {} ", theMsg.getSubscription().getEndpointUrl());
				ourLog.error("Exception: ", e);
				throw e;
			}

		}
	}

	@Nullable
	private IClientExecutable<?, ?> createDeliveryRequestNormal(ResourceDeliveryMessage theMsg, IGenericClient theClient, IBaseResource thePayloadResource) {
		IClientExecutable<?, ?> operation;
		switch (theMsg.getOperationType()) {
			case CREATE:
			case UPDATE:
				operation = theClient.update().resource(thePayloadResource);
				break;
			case DELETE:
				operation = theClient.delete().resourceById(theMsg.getPayloadId(myFhirContext));
				break;
			default:
				ourLog.warn("Ignoring delivery message of type: {}", theMsg.getOperationType());
				operation = null;
				break;
		}
		return operation;
	}

	private IClientExecutable<?, ?> createDeliveryRequestTransaction(CanonicalSubscription theSubscription, IGenericClient theClient, IBaseResource thePayloadResource) {
		IClientExecutable<?, ?> operation;
		String resType = theSubscription.getPayloadSearchCriteria().substring(0, theSubscription.getPayloadSearchCriteria().indexOf('?'));
		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(resType);
		RuntimeResourceDefinition resourceDefinition = myFhirContext.getResourceDefinition(resType);

		String payloadUrl = theSubscription.getPayloadSearchCriteria();
		Map<String, String> valueMap = new HashMap<>(1);
		valueMap.put("matched_resource_id", thePayloadResource.getIdElement().toUnqualifiedVersionless().getValue());
		payloadUrl = new StringSubstitutor(valueMap).replace(payloadUrl);
		SearchParameterMap payloadSearchMap = myMatchUrlService.translateMatchUrl(payloadUrl, resourceDefinition, MatchUrlService.processIncludes());
		payloadSearchMap.setLoadSynchronous(true);

		IBundleProvider searchResults = dao.search(payloadSearchMap, createRequestDetailForPartitionedRequest(theSubscription));

		BundleBuilder builder = new BundleBuilder(myFhirContext);
		for (IBaseResource next : searchResults.getAllResources()) {
			builder.addTransactionUpdateEntry(next);
		}

		operation = theClient.transaction().withBundle(builder.getBundle());
		return operation;
	}

	public IBaseResource getResource(IIdType payloadId, RequestPartitionId thePartitionId, boolean theDeletedOK) throws ResourceGoneException {
		RuntimeResourceDefinition resourceDef = myFhirContext.getResourceDefinition(payloadId.getResourceType());
		SystemRequestDetails systemRequestDetails = new SystemRequestDetails().setRequestPartitionId(thePartitionId);
		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(resourceDef.getImplementingClass());
		return dao.read(payloadId.toVersionless(), systemRequestDetails, theDeletedOK);
	}


	protected IBaseResource getAndMassagePayload(ResourceDeliveryMessage theMsg, CanonicalSubscription theSubscription) {
		IBaseResource payloadResource = theMsg.getPayload(myFhirContext);

		if (payloadResource == null || theSubscription.getRestHookDetails().isDeliverLatestVersion()) {
			IIdType payloadId = theMsg.getPayloadId(myFhirContext);

			try {
				if (payloadId != null) {
					boolean deletedOK = theMsg.getOperationType() == BaseResourceModifiedMessage.OperationTypeEnum.DELETE;
					payloadResource = getResource(payloadId.toVersionless(), theMsg.getRequestPartitionId(), deletedOK);
				} else {
					return null;
				}
			} catch (ResourceGoneException e) {
				ourLog.warn("Resource {} is deleted, not going to deliver for subscription {}", payloadId.toVersionless(), theSubscription.getIdElement(myFhirContext));
				return null;
			}
		}

		IIdType resourceId = payloadResource.getIdElement();
		if (theSubscription.getRestHookDetails().isStripVersionId()) {
			resourceId = resourceId.toVersionless();
			payloadResource.setId(resourceId);
		}
		return payloadResource;
	}

	@Override
	public void handleMessage(ResourceDeliveryMessage theMessage) throws MessagingException {
		CanonicalSubscription subscription = theMessage.getSubscription();

		// Interceptor call: SUBSCRIPTION_BEFORE_REST_HOOK_DELIVERY
		HookParams params = new HookParams()
			.add(CanonicalSubscription.class, subscription)
			.add(ResourceDeliveryMessage.class, theMessage);
		if (!getInterceptorBroadcaster().callHooks(Pointcut.SUBSCRIPTION_BEFORE_REST_HOOK_DELIVERY, params)) {
			return;
		}

		// Grab the endpoint from the subscription
		String endpointUrl = subscription.getEndpointUrl();

		// Grab the payload type (encoding mimetype) from the subscription
		String payloadString = subscription.getPayloadString();
		EncodingEnum payloadType = null;
		if (payloadString != null) {
			payloadType = EncodingEnum.forContentType(payloadString);
		}

		// Create the client request
		myFhirContext.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		IGenericClient client = null;
		if (isNotBlank(endpointUrl)) {
			client = myFhirContext.newRestfulGenericClient(endpointUrl);

			// Additional headers specified in the subscription
			List<String> headers = subscription.getHeaders();
			for (String next : headers) {
				if (isNotBlank(next)) {
					client.registerInterceptor(new SimpleRequestHeaderInterceptor(next));
				}
			}
		}

		deliverPayload(theMessage, subscription, payloadType, client);

		// Interceptor call: SUBSCRIPTION_AFTER_REST_HOOK_DELIVERY
		params = new HookParams()
			.add(CanonicalSubscription.class, subscription)
			.add(ResourceDeliveryMessage.class, theMessage);
		if (!getInterceptorBroadcaster().callHooks(Pointcut.SUBSCRIPTION_AFTER_REST_HOOK_DELIVERY, params)) {
			//noinspection UnnecessaryReturnStatement
			return;
		}

	}

	/**
	 * Sends a POST notification without a payload
	 */
	protected void sendNotification(ResourceDeliveryMessage theMsg) {
		Map<String, List<String>> params = new HashMap<>();
		CanonicalSubscription subscription = theMsg.getSubscription();
		List<Header> headers = parseHeadersFromSubscription(subscription);

		StringBuilder url = new StringBuilder(subscription.getEndpointUrl());
		IHttpClient client = myFhirContext.getRestfulClientFactory().getHttpClient(url, params, "", RequestTypeEnum.POST, headers);
		IHttpRequest request = client.createParamRequest(myFhirContext, params, null);
		try {
			IHttpResponse response = request.execute();
			// close connection in order to return a possible cached connection to the connection pool
			response.close();
		} catch (IOException e) {
			ourLog.error("Error trying to reach {}: {}", theMsg.getSubscription().getEndpointUrl(), e.toString());
			throw new ResourceNotFoundException(Msg.code(5) + e.getMessage());
		}
	}

	public static List<Header> parseHeadersFromSubscription(CanonicalSubscription subscription) {
		List<Header> headers = null;
		if (subscription != null) {
			for (String h : subscription.getHeaders()) {
				if (h != null) {
					final int sep = h.indexOf(':');
					if (sep > 0) {
						final String name = h.substring(0, sep);
						final String value = h.substring(sep + 1);
						if (isNotBlank(name)) {
							if (headers == null) {
								headers = new ArrayList<>();
							}
							headers.add(new Header(name.trim(), value.trim()));
						}
					}
				}
			}
		}
		if (headers == null) {
			headers = Collections.emptyList();
		} else {
			headers = Collections.unmodifiableList(headers);
		}
		return headers;
	}

}
