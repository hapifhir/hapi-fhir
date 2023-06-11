package ca.uhn.fhir.jpa.fql.executor;

import ca.uhn.fhir.rest.api.server.RequestDetails;

public interface IFqlExecutor {

	/**
	 * @param theStatement      The FQL statement to execute
	 * @param theLimit          The maximum number of records to retrieve
	 * @param theRequestDetails The request details associated with the request
	 * @return Returns a {@link IFqlResult result object}. Note that the returned object is not thread safe.
	 */
	IFqlResult execute(String theStatement, Integer theLimit, RequestDetails theRequestDetails);

}
