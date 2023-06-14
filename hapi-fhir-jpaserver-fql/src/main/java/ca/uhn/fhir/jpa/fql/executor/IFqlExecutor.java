/*-
 * #%L
 * HAPI FHIR JPA Server - Firely Query Language
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
