package ca.uhn.fhir.jpa.fql.executor;

import ca.uhn.fhir.jpa.fql.parser.FqlStatement;
import ca.uhn.fhir.rest.api.server.RequestDetails;

public interface IFqlExecutor {

	/**
	 * Execute a FQL query and return the first page of data
	 *
	 * @param theStatement      The FQL statement to execute
	 * @param theLimit          The maximum number of records to retrieve
	 * @param theRequestDetails The request details associated with the request
	 * @return Returns a {@link IFqlResult result object}. Note that the returned object is not thread safe.
	 */
	IFqlResult executeInitialSearch(String theStatement, Integer theLimit, RequestDetails theRequestDetails);

	/**
	 * Load a subsequent page of data
	 *
	 * @param theStatement
	 * @param theSearchId
	 * @param theLimit
	 * @param theRequestDetails
	 * @return
	 */
	IFqlResult executeContinuation(FqlStatement theStatement, String theSearchId, int theStartingOffset, Integer theLimit, RequestDetails theRequestDetails);
}
