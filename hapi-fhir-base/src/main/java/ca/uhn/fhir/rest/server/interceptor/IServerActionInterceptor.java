package ca.uhn.fhir.rest.server.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

/**
 * Action interceptors are invoked by the server upon specific fhir operations, such as "read" (HTTP GET) or "create" (HTTP POST). They can be thought of as being a layer "above"
 * {@link IServerInterceptor} interceptors.
 * <p>
 * These interceptors are useful as a means of adding authentication checks or audit operations on top of a server, since the HAPI RestfulServer translates the incoming requests into higher level
 * operations.
 * </p>
 * <p>
 * Note that unlike {@link IServerInterceptor}s, {@link IServerActionInterceptor}s do not have the ability to handle a request themselves and stop processing.
 * </p>
 */
public interface IServerActionInterceptor extends IServerInterceptor {

	/**
	 * Invoked before an incoming request is processed
	 * 
	 * @param theServletRequest
	 *           The incoming servlet request as provided by the servlet container
	 * @param theOperation
	 *           The type of operation that the FHIR server has determined that the client is trying to invoke
	 * @param theRequestDetails
	 *           An object which will be populated with any relevant details about the incoming request
	 */
	void preAction(HttpServletRequest theServletRequest, ActionOperationEnum theOperation, ActionRequestDetails theRequestDetails);

	/**
	 * Represents the type of operation being invoked for a {@link IServerActionInterceptor#preAction(HttpServletRequest, ActionOperationEnum, ActionRequestDetails) preAction} call
	 */
	public static enum ActionOperationEnum {
		READ, VREAD
	}

	public static class ActionRequestDetails {
		private final IIdType myId;
		private final IBaseResource myRequestResource;

		public ActionRequestDetails(IIdType theId, IBaseResource theRequestResource) {
			super();
			myId = theId;
			myRequestResource = theRequestResource;
		}

		/**
		 * Returns the ID of the incoming request (typically this is from the request URL)
		 */
		public IIdType getId() {
			return myId;
		}

		/**
		 * Returns the incoming resource from the request (this will be populated only for operations which receive a resource, such as "create" and "update")
		 */
		public IBaseResource getRequestResource() {
			return myRequestResource;
		}
	}
}
