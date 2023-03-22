package ca.uhn.fhir.cr.r4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.cr.repo.SearchConverter;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.UriParam;

class SearchConverterTest {
	private SearchConverter myFixture;

	@BeforeEach
	public void setupFixture() {
		myFixture = new SearchConverter();
	}

	@Test
	void isSearchParameterShouldReturnTrue() {
		boolean result = myFixture.isSearchParameter("_elements");
		assertTrue(result);
	}

	@Test
	void isSearchParameterShouldReturnFalse() {
		boolean result = myFixture.isSearchParameter("_id");
		assertFalse(result);
	}

	@Test
	void separateParameterTypesShouldSeparateSearchAndResultParams() {
		myFixture.separateParameterTypes(withParamList());
		assertEquals(3, myFixture.separatedSearchParameters.size());
		assertEquals(2, myFixture.separatedResultParameters.size());
	}

	@Test
	void convertToStringMapShouldConvert() {
		Map<String, String[]> expected = withParamListAsStrings();
		Map<String, String[]> result =
				myFixture.convertToStringMap(withParamList(), withFhirContext());
		assertEquals(result.keySet(), expected.keySet());
		assertTrue(result.entrySet().stream()
				.allMatch(e -> Arrays.equals(e.getValue(), expected.get(e.getKey()))));
	}

	Map<String, List<IQueryParameterType>> withParamList() {
		Map<String, List<IQueryParameterType>> paramList = new HashMap<>();
		paramList.put("_id", withParam(1));
		paramList.put("_elements", withParam(2));
		paramList.put("_lastUpdated", withParam(1));
		paramList.put("_total", withParam(1));
		paramList.put("_count", withParam(3));
		return paramList;
	}

	Map<String, String[]> withParamListAsStrings() {
		Map<String, String[]> paramList = new HashMap<>();
		paramList.put("_id", withStringParam(1));
		paramList.put("_elements", withStringParam(2));
		paramList.put("_lastUpdated", withStringParam(1));
		paramList.put("_total", withStringParam(1));
		paramList.put("_count", withStringParam(3));
		return paramList;
	}

	List<IQueryParameterType> withParam(int theNumberOfParams) {
		List<IQueryParameterType> paramList = new ArrayList<>();
		for (int i = 0; i < theNumberOfParams; i++) {
			paramList.add(new UriParam(Integer.toString(i)));
		}
		return paramList;
	}

	String[] withStringParam(int theNumberOfParams) {
		String[] paramList = new String[theNumberOfParams];
		for (int i = 0; i < theNumberOfParams; i++) {
			paramList[i] = Integer.toString(i);
		}
		return paramList;
	}

	FhirContext withFhirContext() {
		return new FhirContext();
	}
}
