package ca.uhn.fhir.cr.common;

import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.DateRangeParam;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.opencds.cqf.cql.evaluator.fhir.util.Ids;
import org.opencds.cqf.cql.evaluator.fhir.util.Parameters;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * This class provides utilities for implementing FHIR operations
 */
public class Operations {

	private Operations() {
	}

	public static final Pattern PATIENT_OR_GROUP_REFERENCE = Pattern
			.compile("(Patient|Group)/[A-Za-z\\d\\-.]{1,64}");

	public static final Pattern PRACTITIONER_REFERENCE = Pattern
			.compile("Practitioner/[A-Za-z\\d\\-.]{1,64}");

	public static final Pattern ORGANIZATION_REFERENCE = Pattern
			.compile("Organization/[A-Za-z\\d\\-.]{1,64}");

	public static final Pattern FHIR_DATE = Pattern
			.compile("-?\\d{4}(-(0[1-9]|1[0-2])(-(0\\d|[1-2]\\d|3[0-1]))?)?");

	/**
	 * This function returns a fullUrl for a resource.
	 *
	 * @param serverAddress the address of the server
	 * @param fhirType      the type of the resource
	 * @param elementId     the id of the resource
	 * @return the full url as a String
	 */
	public static String getFullUrl(String serverAddress, String fhirType, String elementId) {
		return String.format("%s%s/%s", serverAddress + (serverAddress.endsWith("/") ? "" : "/"), fhirType,
				elementId);
	}

	/**
	 * This function returns a fullUrl for a resource.
	 *
	 * @param serverAddress the address of the server
	 * @param resource      the resource
	 * @return the full url as a String
	 */
	public static String getFullUrl(String serverAddress, IBaseResource resource) {
		checkArgument(resource.getIdElement().hasIdPart(),
				"Cannot generate a fullUrl because the resource does not have an id.");
		return getFullUrl(serverAddress, resource.fhirType(), Ids.simplePart(resource));
	}

	/**
	 * This function validates a string as a representation of a FHIR date.
	 *
	 * @param theParameter the name of the parameter
	 * @param theValue     the value of the parameter
	 */
	public static void validateDate(String theParameter, String theValue) {
		checkArgument(FHIR_DATE.matcher(theValue).matches(), "Parameter '%s' must be a valid FHIR date.",
				theParameter);
	}

	/**
	 * This function validates a parameter as a string representation of a FHIR
	 * date.
	 * Precondition: the parameter has one and only one value.
	 *
	 * @param theRequestDetails metadata about the current request being processed.
	 *                          Generally auto-populated by the HAPI FHIR server
	 *                          framework.
	 * @param theParameter      the name of the parameter
	 */
	public static void validateSingularDate(RequestDetails theRequestDetails, String theParameter) {
		validateCardinality(theRequestDetails, theParameter, 1, 1);
		if (theRequestDetails.getRequestType() == RequestTypeEnum.GET) {
			validateDate(theParameter, theRequestDetails.getParameters().get(theParameter)[0]);
		} else {
			Optional<String> theValue = Parameters.getSingularStringPart(
					theRequestDetails.getFhirContext(), theRequestDetails.getResource(), theParameter);
			theValue.ifPresent(s -> validateDate(theParameter, s));
		}
	}

	/**
	 * This function returns the number of input parameters for a given request.
	 *
	 * @param requestDetails metadata about the current request being processed.
	 *                       Generally auto-populated by the HAPI FHIR server
	 *                       framework.
	 * @param parameter      the name of the parameter
	 * @return the number of input parameters as an integer
	 */
	public static int getNumberOfParametersFromRequest(RequestDetails requestDetails, String parameter) {
		if (requestDetails.getRequestType() == RequestTypeEnum.POST) {
			List<?> parameterList = Parameters.getPartsByName(requestDetails.getFhirContext(),
					requestDetails.getResource(), parameter);
			if (parameterList == null) {
				return 0;
			}
			return parameterList.size();
		} else {
			String[] parameterArr = requestDetails.getParameters().get(parameter);
			if (parameterArr == null) {
				return 0;
			}
			return parameterArr.length;
		}
	}

	/**
	 * This function validates the minimal and maximum number (inclusive) of values
	 * (cardinality) of a parameter.
	 *
	 * @param theRequestDetails metadata about the current request being processed.
	 *                          Generally auto-populated by the HAPI FHIR server
	 *                          framework.
	 * @param theParameter      the name of the parameter
	 * @param min               the minimum number of values for the
	 *                          parameter
	 * @param max               the maximum number of values for the
	 *                          parameter
	 */
	public static void validateCardinality(RequestDetails theRequestDetails, String theParameter, int min, int max) {
		validateCardinality(theRequestDetails, theParameter, min);
		checkArgument(getNumberOfParametersFromRequest(theRequestDetails, theParameter) <= max,
				"Parameter '%s' must be provided a maximum of %s time(s).", theParameter,
				String.valueOf(max));
	}

	/**
	 * This function validates the minimal number (inclusive) of values
	 * (cardinality) of a parameter.
	 *
	 * @param theRequestDetails metadata about the current request being processed.
	 *                          Generally auto-populated by the HAPI FHIR server
	 *                          framework.
	 * @param theParameter      the name of the parameter
	 * @param min               the minimum number of values for the
	 *                          parameter
	 */
	public static void validateCardinality(RequestDetails theRequestDetails, String theParameter, int min) {
		if (min <= 0) {
			return;
		}
		checkArgument(getNumberOfParametersFromRequest(theRequestDetails, theParameter) >= min,
				"Parameter '%s' must be provided a minimum of %s time(s).", theParameter,
				String.valueOf(min));
	}

	/**
	 * This function validates the parameters are valid string representations of
	 * FHIR dates and that they are a valid interval.
	 * This includes that the start value is before the end value.
	 * Precondition: the start and end parameters have one and only one value.
	 *
	 * @param theRequestDetails metadata about the current request being processed.
	 *                          Generally auto-populated by the HAPI FHIR server
	 *                          framework.
	 * @param theStartParameter the name of the start parameter
	 * @param theEndParameter   the name of the end parameter
	 *                          parameter
	 */
	public static void validatePeriod(RequestDetails theRequestDetails, String theStartParameter,
			String theEndParameter) {
		validateCardinality(theRequestDetails, theStartParameter, 1, 1);
		validateCardinality(theRequestDetails, theEndParameter, 1, 1);
		if (theRequestDetails.getRequestType() == RequestTypeEnum.GET) {
			new DateRangeParam(theRequestDetails.getParameters().get(theStartParameter)[0],
					theRequestDetails.getParameters().get(theEndParameter)[0]);
		} else {
			Optional<String> start = Parameters.getSingularStringPart(
					theRequestDetails.getFhirContext(), theRequestDetails.getResource(), theStartParameter);
			Optional<String> end = Parameters.getSingularStringPart(
					theRequestDetails.getFhirContext(), theRequestDetails.getResource(), theEndParameter);
			if (start.isPresent() && end.isPresent()) {
				new DateRangeParam(start.get(), end.get());
			}
		}
	}

	/**
	 * This function validates the value provided for a parameter matches a
	 * specified regex pattern.
	 *
	 * @param theParameter the name of the parameter
	 * @param theValue     the value of the parameter
	 * @param thePattern   the regex pattern to match
	 */
	public static void validatePattern(String theParameter, String theValue, Pattern thePattern) {
		checkArgument(thePattern.matcher(theValue).matches(), "Parameter '%s' must match the pattern: %s", theParameter,
				thePattern);
	}

	/**
	 * This function validates the value of a named parameter matches a specified
	 * regex pattern.
	 * Pattern matching is only enforced if the parameter has a non null/empty
	 * value.
	 * Precondition: the parameter has one and only one value.
	 *
	 * @param theRequestDetails metadata about the current request being processed.
	 *                          Generally auto-populated by the HAPI FHIR server
	 *                          framework.
	 * @param theParameter      the name of the parameter
	 * @param thePattern        the regex pattern to match
	 */
	public static void validateSingularPattern(RequestDetails theRequestDetails, String theParameter,
			Pattern thePattern) {
		if (getNumberOfParametersFromRequest(theRequestDetails, theParameter) == 0) {
			return;
		}
		if (theRequestDetails.getRequestType() == RequestTypeEnum.GET) {
			validatePattern(theParameter, theRequestDetails.getParameters().get(theParameter)[0], thePattern);
		} else {
			Optional<String> theValue = Parameters.getSingularStringPart(theRequestDetails.getFhirContext(),
					theRequestDetails.getResource(), theParameter);
			theValue.ifPresent(s -> validatePattern(theParameter, s, thePattern));
		}
	}

	/**
	 * This function validates a parameter is included exclusive to a set of other
	 * parameters.
	 * Exclusivity is only enforced if the exclusive parameter has at least one
	 * value.
	 *
	 * @param theRequestDetails     metadata about the current request being
	 *                              processed.
	 *                              Generally auto-populated by the HAPI FHIR server
	 *                              framework.
	 * @param theParameter          the name of the parameter that is exclusive
	 * @param theExcludedParameters the set of parameter(s) that are required to be
	 *                              excluded
	 */
	public static void validateExclusive(RequestDetails theRequestDetails, String theParameter,
			String... theExcludedParameters) {
		if (getNumberOfParametersFromRequest(theRequestDetails, theParameter) == 0) {
			return;
		}
		for (String excludedParameter : theExcludedParameters) {
			checkArgument(getNumberOfParametersFromRequest(theRequestDetails, excludedParameter) <= 0,
					"Parameter '%s' cannot be included with parameter '%s'.", excludedParameter, theParameter);
		}
	}

	/**
	 * This function validates a set of parameters are included with the use of a
	 * parameter.
	 * Inclusivity is only enforced if the inclusive parameter has at least one
	 * value.
	 *
	 * @param theRequestDetails     metadata about the current request being
	 *                              processed.
	 *                              Generally auto-populated by the HAPI FHIR server
	 *                              framework.
	 * @param theParameter          the name of the parameter that is inclusive
	 * @param theIncludedParameters the set of parameter(s) that are required to be
	 *                              included
	 */
	public static void validateInclusive(RequestDetails theRequestDetails, String theParameter,
			String... theIncludedParameters) {
		if (getNumberOfParametersFromRequest(theRequestDetails, theParameter) == 0) {
			return;
		}
		for (String includedParameter : theIncludedParameters) {
			checkArgument(getNumberOfParametersFromRequest(theRequestDetails, includedParameter) > 0,
					"Parameter '%s' must be included with parameter '%s'.", includedParameter, theParameter);
		}
	}

	/**
	 * This function validates that one and only one of two parameters has a
	 * value.
	 * It is an exception that neither of the parameters has a value.
	 * It is also an exception that both of the parameters has a value.
	 *
	 * @param theRequestDetails metadata about the current request being
	 *                          processed.
	 *                          Generally auto-populated by the HAPI FHIR server
	 *                          framework.
	 * @param theLeftParameter  one of the two parameters
	 * @param theRightParameter the other of the two parameters
	 */
	public static void validateExclusiveOr(RequestDetails theRequestDetails, String theLeftParameter,
			String theRightParameter) {
		checkArgument(
				(getNumberOfParametersFromRequest(theRequestDetails, theLeftParameter) > 0)
						^ (getNumberOfParametersFromRequest(theRequestDetails, theRightParameter) > 0),
				"Either one of parameter '%s' or parameter '%s' must be included, but not both.", theLeftParameter,
				theRightParameter);
	}

	/**
	 * This function validates that at least one of a set of parameters has a value.
	 * It is an exception that none of the parameters has a value.
	 * It is not an exception that some or all of the parameters have a value.
	 *
	 * @param theRequestDetails metadata about the current request being
	 *                          processed.
	 *                          Generally auto-populated by the HAPI FHIR server
	 *                          framework.
	 * @param theParameters     the set of parameters to validate
	 */
	public static void validateAtLeastOne(RequestDetails theRequestDetails, String... theParameters) {
		for (String includedParameter : theParameters) {
			if (getNumberOfParametersFromRequest(theRequestDetails, includedParameter) > 0) {
				return;
			}
		}
		throw new IllegalArgumentException(String
				.format("At least one of the following parameters must be included: %s.",
						StringUtils.join(theParameters, ", ")));
	}

}
