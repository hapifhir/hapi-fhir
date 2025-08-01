package ca.uhn.fhir.repository.impl;

import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MultiMapSearchQueryBuilderTest {
	MultiMapSearchQueryBuilder myMultiMapSearchQueryBuilder = new  MultiMapSearchQueryBuilder();

	@Test
	void testAddAllMultiMap() {
	    // given
		Multimap<String, List<IQueryParameterType>> map = ArrayListMultimap.create();
		map.put("key1", List.of(new StringParam("value1")));


	    // when
		myMultiMapSearchQueryBuilder.addAll(map);

		// then
	    assertEquals(map, myMultiMapSearchQueryBuilder.toMultiMap());
	}


	@Test
	void testAddAllMap() {
		// given
		Map<String, List<IQueryParameterType>> map = new HashMap<>();
		map.put("key1", List.of(new StringParam("value1")));


		// when
		myMultiMapSearchQueryBuilder.addAll(map);

		// then
		Multimap<String, List<IQueryParameterType>> multiMap = myMultiMapSearchQueryBuilder.toMultiMap();
		assertThat(multiMap.size()).isEqualTo(1);
		Collection<List<IQueryParameterType>> values = multiMap.get("key1");
		assertThat(values).hasSize(1);
		assertThat(values.iterator().next()).isEqualTo(List.of(new StringParam("value1")));
	}

	@Test
	void testAddOrList() {
	    // given

	    // when
		myMultiMapSearchQueryBuilder.addOrList("code", new TokenParam("system", "code1"), new TokenParam("system", "code2"));
		myMultiMapSearchQueryBuilder.addOrList("code", new TokenParam("system", "code3"), new TokenParam("system", "code4"));

	    // then
		Multimap<String, List<IQueryParameterType>> multiMap = myMultiMapSearchQueryBuilder.toMultiMap();

		Multimap<String, List<IQueryParameterType>> expected = ArrayListMultimap.create();
		expected.put("code", List.of(new TokenParam("system", "code1"), new TokenParam("system", "code2")));
		expected.put("code", List.of(new TokenParam("system", "code3"), new TokenParam("system", "code4")));

		assertThat(multiMap).isEqualTo(expected);
	}
	
	@Test
	void testOrListWithMixedTypesThrowsException() {
		IQueryParameterType[] mixedParams = {new TokenParam("system", "code1"), new StringParam("string value")};
		assertThrows(IllegalArgumentException.class, () -> myMultiMapSearchQueryBuilder.addOrList("code", mixedParams));
	}
	

}
