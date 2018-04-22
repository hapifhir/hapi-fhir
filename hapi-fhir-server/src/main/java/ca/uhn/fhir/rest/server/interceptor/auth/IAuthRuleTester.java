package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

/**
 * Allows user-supplied logic for authorization rules.
 * <p>
 * THIS IS AN EXPERIMENTAL API! Feedback is welcome, and this API
 * may change.
 *
 * @since 3.4.0
 */
public interface IAuthRuleTester {

	/**
	 * Allows user-supplied logic for authorization rules.
	 * <p>
	 * THIS IS AN EXPERIMENTAL API! Feedback is welcome, and this API
	 * may change.
	 *
	 * @since 3.4.0
	 */
	boolean matches(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IIdType theInputResourceId, IBaseResource theInputResource);

}
