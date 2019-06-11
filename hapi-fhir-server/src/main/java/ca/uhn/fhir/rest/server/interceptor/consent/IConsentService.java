package ca.uhn.fhir.rest.server.interceptor.consent;

import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import org.hl7.fhir.instance.model.api.IBaseResource;

public interface IConsentService {

	/**
	 * This method is called when an operation is initially beginning, before any
	 * significant processing occurs. The service may use this method to decide
	 * whether the request needs to be reviewed further or not.
	 *
	 * @param theRequestDetails Contains details about the operation that is
	 *                          beginning, including details about the request type,
	 *                          URL, etc. Note that the RequestDetails has a generic
	 *                          Map (see {@link RequestDetails#getUserData()}) that
	 *                          can be used to store information and state to be
	 *                          passed between methods in the consent service.
	 * @return An outcome object. See {@link ConsentOutcome}
	 */
	ConsentOutcome startOperation(RestOperationTypeEnum theOperationTypeEnum, IServerInterceptor.ActionRequestDetails theRequestDetails);

	/**
	 * This method is called if a user may potentially see a resource via READ
	 * operations, SEARCH operations, etc. This method may make decisions about
	 * whether or not the user should be permitted to see the resource.
	 * <p>
	 * Implementations should make no attempt to modify the returned result within
	 * this method. For modification use cases (e.g. masking for consent rules) the
	 * user should use the {@link #seeResource(RequestDetails, IBaseResource)}
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
	 * @return An outcome object. See {@link ConsentOutcome}
	 */
	ConsentOutcome canSeeResource(RequestDetails theRequestDetails, IBaseResource theResource);

	/**
	 * This method is called if a user may potentially see a resource, either completely
	 * or partially. In other words, if the user is going to see any part of this resource
	 * via READ operations, SEARCH operations, etc., this method is
	 * called. This method may modify the resource in order to filter/mask aspects of
	 * the contents, or even to enrich it.
	 * <p>
	 *
	 * </p>
	 *
	 * @param theRequestDetails Contains details about the operation that is
	 *                          beginning, including details about the request type,
	 *                          URL, etc. Note that the RequestDetails has a generic
	 *                          Map (see {@link RequestDetails#getUserData()}) that
	 *                          can be used to store information and state to be
	 *                          passed between methods in the consent service.
	 * @param theResource       The resource that will be exposed
	 * @return An outcome object. See {@link ConsentOutcome}
	 */
	ConsentOutcome seeResource(RequestDetails theRequestDetails, IBaseResource theResource);

	/**
	 * This method is called when an operation is complete. It can be used to perform
	 * any necessary cleanup, flush audit events, etc.
	 */
	ConsentOutcome completeOperation(RequestDetails theRequestDetails);

}
