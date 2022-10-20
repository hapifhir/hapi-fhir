package ca.uhn.fhir.jpa.searchparam.util;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParameterCanonicalizer;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class SearchParameterHelper {
	private final SearchParameterCanonicalizer mySearchParameterCanonicalizer;

	public SearchParameterHelper(SearchParameterCanonicalizer theSearchParameterCanonicalizer) {
		mySearchParameterCanonicalizer = theSearchParameterCanonicalizer;
	}


	public Optional<SearchParameterMap> buildSearchParameterMapFromCanonical(IBaseResource theRuntimeSearchParam) {
		RuntimeSearchParam canonicalSearchParam = mySearchParameterCanonicalizer.canonicalizeSearchParameter(theRuntimeSearchParam);
		if (canonicalSearchParam == null) {
			return Optional.empty();
		}

		SearchParameterMap retVal = new SearchParameterMap();

		String theCode = canonicalSearchParam.getName();
		List<String> theBases = List.copyOf(canonicalSearchParam.getBase());

		TokenAndListParam codeParam = new TokenAndListParam().addAnd(new TokenParam(theCode));
		TokenAndListParam basesParam = toTokenAndList(theBases);

		retVal.add("code", codeParam);
		retVal.add("base", basesParam);

		return Optional.of(retVal);
	}


	private TokenAndListParam toTokenAndList(List<String> theBases) {
		TokenAndListParam retVal = new TokenAndListParam();

		if (theBases != null) {

			TokenOrListParam tokenOrListParam = new TokenOrListParam();
			retVal.addAnd(tokenOrListParam);

			for (String next : theBases) {
				if (isNotBlank(next)) {
					tokenOrListParam.addOr(new TokenParam(next));
				}
			}
		}

		return retVal.getValuesAsQueryTokens().isEmpty() ? null : retVal;
	}


}
