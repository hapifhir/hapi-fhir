package ca.uhn.fhir.jpa.searchparam.matcher;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.searchparam.models.SearchMatchParameters;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.Constants;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;

public interface IParameterMatchHandler {

	static final Set<String> UNSUPPORTED_PARAMETER_NAMES = Sets.newHashSet(Constants.PARAM_HAS);

	void registerServices(IMatchingServices theServices);

	InMemoryMatchResult checkForUnsupportedParameters(
		String theParamName, RuntimeSearchParam theParamDef, List<List<IQueryParameterType>> theAndOrParams);

	InMemoryMatchResult matchResourceByParameters(
		String theParamName,
		RuntimeSearchParam theParamDef,
		List<List<IQueryParameterType>> theAndOrParams,
		SearchMatchParameters theParameters
	);
}
