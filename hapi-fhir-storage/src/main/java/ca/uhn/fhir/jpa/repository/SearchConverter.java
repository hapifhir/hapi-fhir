package ca.uhn.fhir.jpa.repository;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterAnd;
import ca.uhn.fhir.model.api.IQueryParameterOr;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import jakarta.annotation.Nonnull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The IGenericClient API represents searches with OrLists, while the FhirRepository API uses nested
 * lists. This class (will eventually) convert between them
 */
public class SearchConverter {
	// hardcoded list from FHIR specs: https://www.hl7.org/fhir/search.html
	private final List<String> searchResultParameters = Arrays.asList(
			"_sort",
			"_count",
			"_include",
			"_revinclude",
			"_summary",
			"_total",
			"_elements",
			"_contained",
			"_containedType");
	public final Multimap<String, List<IQueryParameterType>> separatedSearchParameters = ArrayListMultimap.create();
	public final Multimap<String, List<IQueryParameterType>> separatedResultParameters = ArrayListMultimap.create();
	public final SearchParameterMap searchParameterMap = new SearchParameterMap();
	public final Map<String, String[]> resultParameters = new HashMap<>();

	public void convertParameters(Multimap<String, List<IQueryParameterType>> parameters, FhirContext fhirContext) {
		if (parameters == null) {
			return;
		}
		separateParameterTypes(parameters);
		convertToSearchParameterMap(separatedSearchParameters);
		convertToStringMap(separatedResultParameters, fhirContext);
	}

	public void convertToStringMap(
			@Nonnull Multimap<String, List<IQueryParameterType>> parameters, @Nonnull FhirContext fhirContext) {
		for (var entry : parameters.entries()) {
			String[] values = new String[entry.getValue().size()];
			for (int i = 0; i < entry.getValue().size(); i++) {
				values[i] = entry.getValue().get(i).getValueAsQueryToken(fhirContext);
			}
			resultParameters.put(entry.getKey(), values);
		}
	}

	public void convertToSearchParameterMap(Multimap<String, List<IQueryParameterType>> searchMap) {
		if (searchMap == null) {
			return;
		}
		for (var entry : searchMap.entries()) {
			// if list of parameters is the value
			if (entry.getValue().size() > 1 && !isOrList(entry.getValue()) && !isAndList(entry.getValue())) {
				// is value a TokenParam
				addTokenToSearchIfNeeded(entry);

				// parameter type is single value list
			} else {
				for (IQueryParameterType value : entry.getValue()) {
					setParameterTypeValue(entry.getKey(), value);
				}
			}
		}
	}

	private void addTokenToSearchIfNeeded(Map.Entry<String, List<IQueryParameterType>> entry) {
		if (isTokenParam(entry.getValue().get(0))) {
			var tokenKey = entry.getKey();
			var tokenList = new TokenOrListParam();
			for (IQueryParameterType rec : entry.getValue()) {
				tokenList.add((TokenParam) rec);
			}
			searchParameterMap.add(tokenKey, tokenList);
		}
	}

	public <T> void setParameterTypeValue(@Nonnull String key, @Nonnull T parameterType) {
		if (isOrList(parameterType)) {
			searchParameterMap.add(key, (IQueryParameterOr<?>) parameterType);
		} else if (isAndList(parameterType)) {
			searchParameterMap.add(key, (IQueryParameterAnd<?>) parameterType);
		} else {
			searchParameterMap.add(key, (IQueryParameterType) parameterType);
		}
	}

	public void separateParameterTypes(@Nonnull Multimap<String, List<IQueryParameterType>> parameters) {
		for (var entry : parameters.entries()) {
			if (isSearchResultParameter(entry.getKey())) {
				separatedResultParameters.put(entry.getKey(), entry.getValue());
			} else {
				separatedSearchParameters.put(entry.getKey(), entry.getValue());
			}
		}
	}

	public boolean isSearchResultParameter(String parameterName) {
		return searchResultParameters.contains(parameterName);
	}

	public <T> boolean isOrList(@Nonnull T parameterType) {
		return IQueryParameterOr.class.isAssignableFrom(parameterType.getClass());
	}

	public <T> boolean isAndList(@Nonnull T parameterType) {
		return IQueryParameterAnd.class.isAssignableFrom(parameterType.getClass());
	}

	public <T> boolean isTokenParam(@Nonnull T parameterType) {
		return TokenParam.class.isAssignableFrom(parameterType.getClass());
	}
}
