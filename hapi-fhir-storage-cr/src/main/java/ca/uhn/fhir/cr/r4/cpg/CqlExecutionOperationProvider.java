/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.cr.r4.cpg;

import ca.uhn.fhir.cr.r4.ICqlExecutionServiceFactory;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Endpoint;
import org.hl7.fhir.r4.model.Parameters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CqlExecutionOperationProvider {

	@Autowired
	ICqlExecutionServiceFactory myCqlExecutionServiceFactory;

	/**
	 * Evaluates a CQL expression and returns the results as a Parameters resource.
	 *
	 * @param theRequestDetails      the {@link RequestDetails RequestDetails}
	 * @param theSubject             Subject for which the expression will be
	 *                            evaluated. This corresponds to the context in
	 *                            which the expression will be evaluated and is
	 *                            represented as a relative FHIR id (e.g.
	 *                            Patient/123), which establishes both the context
	 *                            and context value for the evaluation
	 * @param theExpression          Expression to be evaluated. Note that this is an
	 *                            expression of CQL, not the text of a library with
	 *                            definition statements. If the content parameter is
	 *                            set, the expression will be the name of the
	 *                            expression to be evaluated.
	 * @param theParameters          Any input parameters for the expression.
	 *                            {@link Parameters} Parameters defined in this
	 *                            input will be made available by name to the CQL
	 *                            expression. Parameter types are mapped to CQL as
	 *                            specified in the Using CQL section of the CPG
	 *                            Implementation guide. If a parameter appears more
	 *                            than once in the input Parameters resource, it is
	 *                            represented with a List in the input CQL. If a
	 *                            parameter has parts, it is represented as a Tuple
	 *                            in the input CQL.
	 * @param theLibrary             A library to be included. The
	 *                            {@link org.hl7.fhir.r4.model.Library}
	 *                            library is resolved by url and made available by
	 *                            name within the expression to be evaluated.
	 * @param theUseServerData       Whether to use data from the server performing the
	 *                            evaluation. If this parameter is true (the
	 *                            default), then the operation will use data first
	 *                            from any bundles provided as parameters (through
	 *                            the data and prefetch parameters), second data
	 *                            from the server performing the operation, and
	 *                            third, data from the dataEndpoint parameter (if
	 *                            provided). If this parameter is false, the
	 *                            operation will use data first from the bundles
	 *                            provided in the data or prefetch parameters, and
	 *                            second from the dataEndpoint parameter (if
	 *                            provided).
	 * @param theData                Data to be made available to the library
	 *                            evaluation. This parameter is exclusive with the
	 *                            prefetchData parameter (i.e. either provide all
	 *                            data as a single bundle, or provide data using
	 *                            multiple bundles with prefetch descriptions).
	 * @param thePrefetchData        ***Not Yet Implemented***
	 * @param theDataEndpoint        An {@link Endpoint} endpoint to use to access data
	 *                            referenced by retrieve operations in the library.
	 *                            If provided, this endpoint is used after the data
	 *                            or prefetchData bundles, and the server, if the
	 *                            useServerData parameter is true.
	 * @param theContentEndpoint     An {@link Endpoint} endpoint to use to access
	 *                            content (i.e. libraries) referenced by the
	 *                            library. If no content endpoint is supplied, the
	 *                            evaluation will attempt to retrieve content from
	 *                            the server on which the operation is being
	 *                            performed.
	 * @param theTerminologyEndpoint An {@link Endpoint} endpoint to use to access
	 *                            terminology (i.e. valuesets, codesystems, and
	 *                            membership testing) referenced by the library. If
	 *                            no terminology endpoint is supplied, the
	 *                            evaluation will attempt to use the server on which
	 *                            the operation is being performed as the
	 *                            terminology server.
	 * @param theContent           non-spec parameter that will be excluded from this scope.
	 * @return The result of evaluating the given expression, returned as a FHIR
	 *         type, either a {@link org.hl7.fhir.r4.model.Resource} resource, or a
	 *         FHIR-defined type
	 *         corresponding to the CQL return type, as defined in the Using CQL
	 *         section of the CPG Implementation guide. If the result is a List of
	 *         resources, the result will be a {@link Bundle} Bundle . If the result
	 *         is a CQL system-defined or FHIR-defined type, the result is returned
	 *         as a {@link Parameters} Parameters resource
	 */
	@Operation(name = ProviderConstants.CR_OPERATION_CQL, idempotent = true)
	@Description(
			shortDefinition = "$cql",
			value =
					"Evaluates a CQL expression and returns the results as a Parameters resource. Defined: http://build.fhir.org/ig/HL7/cqf-recommendations/OperationDefinition-cpg-cql.html",
			example = "$cql?expression=5*5")
	public Parameters evaluate(
			RequestDetails theRequestDetails,
			@OperationParam(name = "subject", max = 1) String theSubject,
			@OperationParam(name = "expression", max = 1) String theExpression,
			@OperationParam(name = "parameters", max = 1) Parameters theParameters,
			@OperationParam(name = "library") List<Parameters> theLibrary,
			@OperationParam(name = "useServerData", max = 1) BooleanType theUseServerData,
			@OperationParam(name = "data", max = 1) Bundle theData,
			@OperationParam(name = "prefetchData") List<Parameters> thePrefetchData,
			@OperationParam(name = "dataEndpoint", max = 1) Endpoint theDataEndpoint,
			@OperationParam(name = "contentEndpoint", max = 1) Endpoint theContentEndpoint,
			@OperationParam(name = "terminologyEndpoint", max = 1) Endpoint theTerminologyEndpoint,
			@OperationParam(name = "content", max = 1) String theContent) {
		return myCqlExecutionServiceFactory
				.create(theRequestDetails)
				.evaluate(
						theSubject,
						theExpression,
						theParameters,
						theLibrary,
						theUseServerData,
						theData,
						thePrefetchData,
						theDataEndpoint,
						theContentEndpoint,
						theTerminologyEndpoint,
						theContent);
	}
}
