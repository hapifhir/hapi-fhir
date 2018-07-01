package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;

public interface IAuthRuleBuilderPatch {

	/**
	 * With this setting, all <a href="http://hl7.org/fhir/http.html#patch">patch</a> requests will be permitted
	 * to proceed. This rule will not permit the
	 * {@link ca.uhn.fhir.rest.server.interceptor.IServerOperationInterceptor#resourceCreated(RequestDetails, IBaseResource)}
	 * and
	 * {@link ca.uhn.fhir.rest.server.interceptor.IServerOperationInterceptor#resourceUpdated(RequestDetails, IBaseResource, IBaseResource)}
	 * methods if your server supports {@link ca.uhn.fhir.rest.server.interceptor.IServerOperationInterceptor}.
	 * In that case, additional rules are generally required in order to
	 * permit write operations.
	 */
	IAuthRuleFinished allRequests();

}
