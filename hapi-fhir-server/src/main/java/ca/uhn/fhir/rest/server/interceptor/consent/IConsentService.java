package ca.uhn.fhir.rest.server.interceptor.consent;

/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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
	ConsentOutcome startOperation(RequestDetails theRequestDetails, IConsentContextServices theContextServices);

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
	 * <b>Performance note:</b> Note that this method should be efficient, since it will be called once
	 * for every resource potentially returned (e.g. by searches). If this method
	 * takes a significant amount of time to execute, performance on the server
	 * will suffer.
	 * </p>
	 *
	 * @param theRequestDetails Contains details about the operation that is
	 *                          beginning, including details about the request type,
	 *                          URL, etc. Note that the RequestDetails has a generic
	 *                          Map (see {@link RequestDetails#getUserData()}) that
	 *                          can be used to store information and state to be
	 *                          passed between methods in the consent service.
	 * @param theResource       The resource that will be exposed
	 * @param theContextServices An object passed in by the consent framework that
	 *                           provides utility functions relevant to acting on
	 *                           consent directives.
	 * @return An outcome object. See {@link ConsentOutcome}
	 */
	ConsentOutcome canSeeResource(RequestDetails theRequestDetails, IBaseResource theResource, IConsentContextServices theContextServices);

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
	 * <li>{@link ConsentOperationStatusEnum#AUTHORIZED}: The resource will be returned to the client.</li>
	 * <li>{@link ConsentOperationStatusEnum#PROCEED}: The resource will be returned to the client. Any embedded resources contained within the resource will also be checked by {@link #willSeeResource(RequestDetails, IBaseResource, IConsentContextServices)}.</li>
	 * <li>{@link ConsentOperationStatusEnum#REJECT}: The resource will not be returned to the client. If the resource supplied to the </li>
	 * </ul>
	 *
	 * @param theRequestDetails Contains details about the operation that is
	 *                          beginning, including details about the request type,
	 *                          URL, etc. Note that the RequestDetails has a generic
	 *                          Map (see {@link RequestDetails#getUserData()}) that
	 *                          can be used to store information and state to be
	 *                          passed between methods in the consent service.
	 * @param theResource       The resource that will be exposed
	 * @param theContextServices An object passed in by the consent framework that
	 *                           provides utility functions relevant to acting on
	 *                           consent directives.
	 * @return An outcome object. See method documentation for a description.
	 */
	ConsentOutcome willSeeResource(RequestDetails theRequestDetails, IBaseResource theResource, IConsentContextServices theContextServices);

	/**
	 * This method is called when an operation is complete. It can be used to perform
	 * any necessary cleanup, flush audit events, etc.
	 * <p>
	 * This method is not called if the request failed. {@link #completeOperationFailure(RequestDetails, BaseServerResponseException, IConsentContextServices)}
	 * will be called instead in that case.
	 * </p>
	 *
	 * @param theRequestDetails Contains details about the operation that is
	 *                          beginning, including details about the request type,
	 *                          URL, etc. Note that the RequestDetails has a generic
	 *                          Map (see {@link RequestDetails#getUserData()}) that
	 *                          can be used to store information and state to be
	 *                          passed between methods in the consent service.
	 * @param theContextServices An object passed in by the consent framework that
	 *                           provides utility functions relevant to acting on
	 *                           consent directives.
	 * @see #completeOperationFailure(RequestDetails, BaseServerResponseException, IConsentContextServices)
	 */
	void completeOperationSuccess(RequestDetails theRequestDetails, IConsentContextServices theContextServices);

	/**
	 * This method is called when an operation is complete. It can be used to perform
	 * any necessary cleanup, flush audit events, etc.
	 * <p>
	 * This method will be called if the request did not complete successfully, instead of
	 * {@link #completeOperationSuccess(RequestDetails, IConsentContextServices)}. Typically this means that
	 * the operation failed and a failure is being returned to the client.
	 * </p>
	 *
	 * @param theRequestDetails Contains details about the operation that is
	 *                          beginning, including details about the request type,
	 *                          URL, etc. Note that the RequestDetails has a generic
	 *                          Map (see {@link RequestDetails#getUserData()}) that
	 *                          can be used to store information and state to be
	 *                          passed between methods in the consent service.
	 * @param theContextServices An object passed in by the consent framework that
	 *                           provides utility functions relevant to acting on
	 *                           consent directives.
	 * @see #completeOperationSuccess(RequestDetails, IConsentContextServices)
	 */
	void completeOperationFailure(RequestDetails theRequestDetails, BaseServerResponseException theException, IConsentContextServices theContextServices);
}
