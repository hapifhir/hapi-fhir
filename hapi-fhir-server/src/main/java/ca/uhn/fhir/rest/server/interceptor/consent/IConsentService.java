package ca.uhn.fhir.rest.server.interceptor.consent;

/*-
 * #%L
 * HAPI FHIR - Server Framework
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

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 * This interface is intended to be implemented as the user-defined contract for
 * the {@link ConsentInterceptor}.
 * <p>
 * Note: Since HAPI FHIR 5.1.0, methods in this interface have default methods that return {@link ConsentOutcome#PROCEED}
 * </p>
 * <p>
 * See <a href="https://hapifhir.io/hapi-fhir/docs/security/consent_interceptor.html">Consent Interceptor</a> for
 * more information on this interceptor.
 * </p>
 */
public interface IConsentService {

	/**
	 * This method is called when an operation is initially beginning, before any
	 * significant processing occurs. The service may use this method to decide
	 * whether the request needs to be reviewed further or not.
	 *
	 * @param theRequestDetails  Contains details about the operation that is
	 *                           beginning, including details about the request type,
	 *                           URL, etc. Note that the RequestDetails has a generic
	 *                           Map (see {@link RequestDetails#getUserData()}) that
	 *                           can be used to store information and state to be
	 *                           passed between methods in the consent service.
	 * @param theContextServices An object passed in by the consent framework that
	 *                           provides utility functions relevant to acting on
	 *                           consent directives.
	 * @return An outcome object. See {@link ConsentOutcome}
	 */
	default ConsentOutcome startOperation(RequestDetails theRequestDetails, IConsentContextServices theContextServices) {
		return ConsentOutcome.PROCEED;
	}

	/**
	 * This method will be invoked once prior to invoking {@link #canSeeResource(RequestDetails, IBaseResource, IConsentContextServices)}
	 * and can be used to skip that phase.
	 * <p>
	 * If this method returns {@literal false} (default is {@literal true}) {@link #willSeeResource(RequestDetails, IBaseResource, IConsentContextServices)}
	 * will be invoked for this request, but {@link #canSeeResource(RequestDetails, IBaseResource, IConsentContextServices)} will not.
	 * </p>
	 *
	 * @param theRequestDetails  Contains details about the operation that is
	 *                           beginning, including details about the request type,
	 *                           URL, etc. Note that the RequestDetails has a generic
	 *                           Map (see {@link RequestDetails#getUserData()}) that
	 *                           can be used to store information and state to be
	 *                           passed between methods in the consent service.
	 * @param theContextServices An object passed in by the consent framework that
	 *                           provides utility functions relevant to acting on
	 *                           consent directives.
	 * @return Returns {@literal false} to avoid calling {@link #canSeeResource(RequestDetails, IBaseResource, IConsentContextServices)}
	 * @since 6.0.0
	 */
	default boolean shouldProcessCanSeeResource(RequestDetails theRequestDetails, IConsentContextServices theContextServices) {
		return true;
	}

	/**
	 * This method is called if a user may potentially see a resource via READ
	 * operations, SEARCH operations, etc. This method may make decisions about
	 * whether or not the user should be permitted to see the resource.
	 * <p>
	 * Implementations should make no attempt to modify the returned result within
	 * this method. For modification use cases (e.g. masking for consent rules) the
	 * user should use the {@link #willSeeResource(RequestDetails, IBaseResource, IConsentContextServices)}
	 * method to actually make changes. This method is intended to only
	 * to make decisions.
	 * </p>
	 * <p>
	 * In addition, the {@link ConsentOutcome} must return one of the following
	 * statuses:
	 * </p>
	 * <ul>
	 * <li>{@link ConsentOperationStatusEnum#AUTHORIZED}: The resource will be returned to the client. If multiple consent service implementation are present, no further implementations will be invoked for this resource. {@link #willSeeResource(RequestDetails, IBaseResource, IConsentContextServices)} will not be invoked for this resource.</li>
	 * <li>{@link ConsentOperationStatusEnum#PROCEED}: The resource will be returned to the client.</li>
	 * <li>{@link ConsentOperationStatusEnum#REJECT}: The resource will be stripped from the response. If multiple consent service implementation are present, no further implementations will be invoked for this resource. {@link #willSeeResource(RequestDetails, IBaseResource, IConsentContextServices)} will not be invoked for this resource.</li>
	 * </ul>
	 * </p>
	 * <p>
	 *    There are two methods the consent service may use to suppress or modify response resources:
	 * </p>
	 * <ul>
	 *    <li>{@link #canSeeResource(RequestDetails, IBaseResource, IConsentContextServices)} should be used to remove resources from results in scenarios where it is important to not reveal existence of those resources. It is called prior to any paging logic, so result pages will still be normal sized even if results are filtered.</li>
	 *    <li>{@link #willSeeResource(RequestDetails, IBaseResource, IConsentContextServices)} should be used to filter individual elements from resources, or to remove entire resources in cases where it is not important to conceal their existence. It is called after paging logic, so any resources removed by this method may result in abnormally sized result pages. However, removing resourced using this method may also perform better so it is preferable for use in cases where revealing resource existence is not a concern.</li>
	 * </ul>
	 * <p>
	 * <b>Performance note:</b> Note that this method should be efficient, since it will be called once
	 * for every resource potentially returned (e.g. by searches). If this method
	 * takes a significant amount of time to execute, performance on the server
	 * will suffer.
	 * </p>
	 *
	 *
	 * @param theRequestDetails  Contains details about the operation that is
	 *                           beginning, including details about the request type,
	 *                           URL, etc. Note that the RequestDetails has a generic
	 *                           Map (see {@link RequestDetails#getUserData()}) that
	 *                           can be used to store information and state to be
	 *                           passed between methods in the consent service.
	 * @param theResource        The resource that will be exposed
	 * @param theContextServices An object passed in by the consent framework that
	 *                           provides utility functions relevant to acting on
	 *                           consent directives.
	 * @return An outcome object. See {@link ConsentOutcome}. Note that this method is not allowed
	 * to modify the response object, so an error will be thrown if {@link ConsentOutcome#getResource()}
	 * returns a non-null response.
	 */
	default ConsentOutcome canSeeResource(RequestDetails theRequestDetails, IBaseResource theResource, IConsentContextServices theContextServices) {
		return ConsentOutcome.PROCEED;
	}

	/**
	 * This method is called if a user is about to see a resource, either completely
	 * or partially. In other words, if the user is going to see any part of this resource
	 * via READ operations, SEARCH operations, etc., this method is
	 * called. This method may modify the resource in order to filter/mask aspects of
	 * the contents, or even to enrich it.
	 * <p>
	 * The returning {@link ConsentOutcome} may optionally replace the resource
	 * with a different resource (including an OperationOutcome) by calling the
	 * resource property on the {@link ConsentOutcome}.
	 * </p>
	 * <p>
	 * In addition, the {@link ConsentOutcome} must return one of the following
	 * statuses:
	 * </p>
	 * <ul>
	 * <li>{@link ConsentOperationStatusEnum#AUTHORIZED}: The resource will be returned to the client. If multiple consent service implementation are present, no further implementations will be invoked for this resource.</li>
	 * <li>{@link ConsentOperationStatusEnum#PROCEED}: The resource will be returned to the client.</li>
	 * <li>{@link ConsentOperationStatusEnum#REJECT}: The resource will not be returned to the client. If multiple consent service implementation are present, no further implementations will be invoked for this resource.</li>
	 * </ul>
	 *
	 * @param theRequestDetails  Contains details about the operation that is
	 *                           beginning, including details about the request type,
	 *                           URL, etc. Note that the RequestDetails has a generic
	 *                           Map (see {@link RequestDetails#getUserData()}) that
	 *                           can be used to store information and state to be
	 *                           passed between methods in the consent service.
	 * @param theResource        The resource that will be exposed
	 * @param theContextServices An object passed in by the consent framework that
	 *                           provides utility functions relevant to acting on
	 *                           consent directives.
	 * @return An outcome object. See method documentation for a description.
	 * @see #canSeeResource(RequestDetails, IBaseResource, IConsentContextServices) for a description of the difference between these two methods.
	 */
	default ConsentOutcome willSeeResource(RequestDetails theRequestDetails, IBaseResource theResource, IConsentContextServices theContextServices) {
		return ConsentOutcome.PROCEED;
	}

	/**
	 * This method is called when an operation is complete. It can be used to perform
	 * any necessary cleanup, flush audit events, etc.
	 * <p>
	 * This method is not called if the request failed. {@link #completeOperationFailure(RequestDetails, BaseServerResponseException, IConsentContextServices)}
	 * will be called instead in that case.
	 * </p>
	 *
	 * @param theRequestDetails  Contains details about the operation that is
	 *                           beginning, including details about the request type,
	 *                           URL, etc. Note that the RequestDetails has a generic
	 *                           Map (see {@link RequestDetails#getUserData()}) that
	 *                           can be used to store information and state to be
	 *                           passed between methods in the consent service.
	 * @param theContextServices An object passed in by the consent framework that
	 *                           provides utility functions relevant to acting on
	 *                           consent directives.
	 * @see #completeOperationFailure(RequestDetails, BaseServerResponseException, IConsentContextServices)
	 */
	default void completeOperationSuccess(RequestDetails theRequestDetails, IConsentContextServices theContextServices) {
	}

	/**
	 * This method is called when an operation is complete. It can be used to perform
	 * any necessary cleanup, flush audit events, etc.
	 * <p>
	 * This method will be called if the request did not complete successfully, instead of
	 * {@link #completeOperationSuccess(RequestDetails, IConsentContextServices)}. Typically this means that
	 * the operation failed and a failure is being returned to the client.
	 * </p>
	 *
	 * @param theRequestDetails  Contains details about the operation that is
	 *                           beginning, including details about the request type,
	 *                           URL, etc. Note that the RequestDetails has a generic
	 *                           Map (see {@link RequestDetails#getUserData()}) that
	 *                           can be used to store information and state to be
	 *                           passed between methods in the consent service.
	 * @param theContextServices An object passed in by the consent framework that
	 *                           provides utility functions relevant to acting on
	 *                           consent directives.
	 * @see #completeOperationSuccess(RequestDetails, IConsentContextServices)
	 */
	default void completeOperationFailure(RequestDetails theRequestDetails, BaseServerResponseException theException, IConsentContextServices theContextServices) {
	}
}
