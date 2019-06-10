package ca.uhn.fhir.rest.server.interceptor.consent;

import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IPreResourceShowDetails;
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
	 * This method is called if a user is about to see a resource, either completely
	 * or partially. In other words, if the user is going to see any part of this resource
	 * other than its ID via READ operations, SEARCH operations, etc., this method is
	 * called.
	 * <p>
	 * <b>Performance note:</b> Note that this method should be efficient, since it will be called once
	 * for every resource potentially returned (e.g. by searches). If this method
	 * takes a significant amount of time to execute, performance on the server
	 * will suffer.
	 * </p>
	 * <p>
	 *     Implementations should make no attempt to modify the returned result within
	 *     this method. For modification use cases (e.g. masking for consent rules) the
	 *     user should use the
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

	// FIXME: JA document
	/**
	 *
	 * @param theRequestDetails
	 * @param theResource
	 * @return
	 */
	ConsentOutcome seeResource(RequestDetails theRequestDetails, IBaseResource theResource);

}
