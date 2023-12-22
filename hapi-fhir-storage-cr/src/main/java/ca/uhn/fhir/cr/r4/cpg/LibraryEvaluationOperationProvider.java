package ca.uhn.fhir.cr.r4.cpg;

import ca.uhn.fhir.cr.r4.ILibraryEvaluationServiceFactory;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Endpoint;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Library;
import org.hl7.fhir.r4.model.Parameters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class LibraryEvaluationOperationProvider {
	@Autowired
	ILibraryEvaluationServiceFactory myLibraryEvaluationServiceFactory;
	/**
	 * Evaluates a CQL library and returns the results as a Parameters resource.
	 *
	 * @param theRequestDetails      the {@link RequestDetails RequestDetails}
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
	 * theUseServerData       ***Not Yet Implemented*** Whether to use data from the server performing the
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
	 *                            provided)
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
			RequestDetails theRequestDetails,
			@IdParam IdType theId,
			@OperationParam(name = "subject") String theSubject,
			@OperationParam(name = "expression") List<String> theExpression,
			@OperationParam(name = "parameters") Parameters theParameters,
			@OperationParam(name = "data") Bundle theData,
			@OperationParam(name = "prefetchData") List<Parameters> thePrefetchData,
			@OperationParam(name = "dataEndpoint") Endpoint theDataEndpoint,
			@OperationParam(name = "contentEndpoint") Endpoint theContentEndpoint,
			@OperationParam(name = "terminologyEndpoint") Endpoint theTerminologyEndpoint) {
		return myLibraryEvaluationServiceFactory
				.create(theRequestDetails)
				.evaluate(
						theId,
						theSubject,
						theExpression,
						theParameters,
						theData,
						thePrefetchData,
						theDataEndpoint,
						theContentEndpoint,
						theTerminologyEndpoint);
	}
}
