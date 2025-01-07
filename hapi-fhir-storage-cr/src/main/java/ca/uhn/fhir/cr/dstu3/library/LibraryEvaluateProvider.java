/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.cr.dstu3.library;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.cr.common.ILibraryProcessorFactory;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Endpoint;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Library;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.StringType;
import org.opencds.cqf.fhir.utility.monad.Eithers;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static ca.uhn.fhir.cr.common.CanonicalHelper.getCanonicalType;

public class LibraryEvaluateProvider {
	@Autowired
	ILibraryProcessorFactory myLibraryProcessorFactory;

	/**
	 * Evaluates a CQL library and returns the results as a Parameters resource.
	 *
	 * @param theId					the library resource's Id
	 * @param theSubject             Subject for which the library will be evaluated.
	 *                            This corresponds to the context in which the
	 *                            library
	 *                            will be evaluated and is represented as a relative
	 *                            FHIR id (e.g. Patient/123), which establishes both
	 *                            the context and context value for the evaluation
	 * @param theExpression          Expression(s) to be evaluated. If no expression
	 *                            names
	 *                            are provided, the operation evaluates all public
	 *                            expression definitions in the library
	 * @param theParameters          Any input parameters for the expression.
	 *                            {@link Parameters} Parameters defined in this
	 *                            input will be made available by name to the CQL
	 *                            expression. Parameter types are mapped to CQL as
	 *                            specified in the Using CQL section of the CPG
	 *                            Implementation guide. If a parameter appears more
	 *                            than once in the input Parameters resource, it is
	 *                            represented with a List in the input CQL. If a
	 *                            parameter has parts, it is represented as a Tuple
	 *                            in the input CQL
	 * @param theUseServerData 	  Whether to use data from the server performing the
	 *                             evaluation. If this parameter is true (the default),
	 *                             then the operation will use data first from any
	 *                             bundles provided as parameters (through the data
	 *                             and prefetch parameters), second data from the
	 *                             server performing the operation, and third, data
	 *                             from the dataEndpoint parameter (if provided).
	 *                             If this parameter is false, the operation will use
	 *                             data first from the bundles provided in the data or
	 *                             prefetch parameters, and second from the dataEndpoint
	 *                             parameter (if provided).
	 * @param theData                Data to be made available to the library
	 *                            evaluation. This parameter is exclusive with the
	 *                            prefetchData parameter (i.e. either provide all
	 *                            data as a single bundle, or provide data using
	 *                            multiple bundles with prefetch descriptions)
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
	 *                            performed
	 * @param theTerminologyEndpoint An {@link Endpoint} endpoint to use to access
	 *                            terminology (i.e. valuesets, codesystems, and
	 *                            membership testing) referenced by the library. If
	 *                            no terminology endpoint is supplied, the
	 *                            evaluation will attempt to use the server on which
	 *                            the operation is being performed as the
	 *                            terminology server
	 * @param theRequestDetails      the {@link RequestDetails RequestDetails}
	 * @return The results of the library evaluation, returned as a
	 *         {@link Parameters} resource
	 *         with a parameter for each named expression defined in the library.
	 *         The value of
	 *         each expression is returned as a FHIR type, either a resource, or a
	 *         FHIR-defined
	 *         type corresponding to the CQL return type, as defined in the Using
	 *         CQL section of
	 *         this implementation guide. If the result of an expression is a list
	 *         of resources,
	 *         that parameter will be repeated for each element in the result
	 */
	@Operation(name = ProviderConstants.CR_OPERATION_EVALUATE, idempotent = true, type = Library.class)
	public Parameters evaluate(
			@IdParam IdType theId,
			@OperationParam(name = "subject") String theSubject,
			@OperationParam(name = "expression") List<String> theExpression,
			@OperationParam(name = "parameters") Parameters theParameters,
			@OperationParam(name = "useServerData") BooleanType theUseServerData,
			@OperationParam(name = "data") Bundle theData,
			@OperationParam(name = "prefetchData") List<Parameters.ParametersParameterComponent> thePrefetchData,
			@OperationParam(name = "dataEndpoint") Endpoint theDataEndpoint,
			@OperationParam(name = "contentEndpoint") Endpoint theContentEndpoint,
			@OperationParam(name = "terminologyEndpoint") Endpoint theTerminologyEndpoint,
			RequestDetails theRequestDetails) {
		return (Parameters) myLibraryProcessorFactory
				.create(theRequestDetails)
				.evaluate(
						Eithers.forMiddle3(theId),
						theSubject,
						theExpression,
						theParameters,
						theUseServerData == null ? Boolean.TRUE : theUseServerData.booleanValue(),
						theData,
						thePrefetchData,
						theDataEndpoint,
						theContentEndpoint,
						theTerminologyEndpoint);
	}

	@Operation(name = ProviderConstants.CR_OPERATION_EVALUATE, idempotent = true, type = Library.class)
	public Parameters evaluate(
			@OperationParam(name = "library") String theLibrary,
			@OperationParam(name = "subject") String theSubject,
			@OperationParam(name = "expression") List<String> theExpression,
			@OperationParam(name = "parameters") Parameters theParameters,
			@OperationParam(name = "useServerData") BooleanType theUseServerData,
			@OperationParam(name = "data") Bundle theData,
			@OperationParam(name = "prefetchData") List<Parameters.ParametersParameterComponent> thePrefetchData,
			@OperationParam(name = "dataEndpoint") Endpoint theDataEndpoint,
			@OperationParam(name = "contentEndpoint") Endpoint theContentEndpoint,
			@OperationParam(name = "terminologyEndpoint") Endpoint theTerminologyEndpoint,
			RequestDetails theRequestDetails) {
		StringType canonicalType = getCanonicalType(FhirVersionEnum.DSTU3, theLibrary, null, null);
		return (Parameters) myLibraryProcessorFactory
				.create(theRequestDetails)
				.evaluate(
						Eithers.forLeft3(canonicalType),
						theSubject,
						theExpression,
						theParameters,
						theUseServerData == null ? Boolean.TRUE : theUseServerData.booleanValue(),
						theData,
						thePrefetchData,
						theDataEndpoint,
						theContentEndpoint,
						theTerminologyEndpoint);
	}
}
