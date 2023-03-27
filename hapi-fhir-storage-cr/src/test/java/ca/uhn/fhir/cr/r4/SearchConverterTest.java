package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.cr.repo.SearchConverter;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.UriParam;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
	void convertToSearchParameterMapShouldConvertWithOneEntry() {
		Map<String, List<IQueryParameterType>> paramMap = new HashMap<>();
		List<IQueryParameterType> paramList = withUriParam(1);
		paramMap.put("theKey", paramList);
		myFixture.convertToSearchParameterMap(paramMap);
		final String expected = "?theKey=0";
		final String result = myFixture.searchParameterMap.toNormalizedQueryString(withFhirContext());
		assertEquals(expected, result);
	}

	@Test
	void convertToSearchParameterMapShouldConvertWithMultipleEntries() {
		Map<String, List<IQueryParameterType>> paramMap = new HashMap<>();
		List<IQueryParameterType> firstParamList = withUriParam(1);
		List<IQueryParameterType> secondParamList = withUriParam(2);
		paramMap.put("theFirstKey", firstParamList);
		paramMap.put("theSecondKey", secondParamList);
		myFixture.convertToSearchParameterMap(paramMap);
		final String expected = "?theFirstKey=0&theSecondKey=0&theSecondKey=1";
		final String result = myFixture.searchParameterMap.toNormalizedQueryString(withFhirContext());
		assertEquals(expected, result);
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
		myFixture.convertToStringMap(withParamList(), withFhirContext());
		Map<String, String[]> result = myFixture.resultParameters;
		assertEquals(result.keySet(), expected.keySet());
		assertTrue(result.entrySet().stream().allMatch(e -> Arrays.equals(e.getValue(), expected.get(e.getKey()))));
	}

	Map<String, List<IQueryParameterType>> withParamList() {
		Map<String, List<IQueryParameterType>> paramList = new HashMap<>();
		paramList.put("_id", withUriParam(1));
		paramList.put("_elements", withUriParam(2));
		paramList.put("_lastUpdated", withUriParam(1));
		paramList.put("_total", withUriParam(1));
		paramList.put("_count", withUriParam(3));
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

	List<IQueryParameterType> withUriParam(int theNumberOfParams) {
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

	FhirContext withFhirContext() {return new FhirContext();}
}
