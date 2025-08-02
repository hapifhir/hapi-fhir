package ca.uhn.fhir.repository.impl;

import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.TokenParam;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MultiMapRepositoryRestQueryBuilderTest {
	MultiMapRepositoryRestQueryBuilder myBuilder = new MultiMapRepositoryRestQueryBuilder();

	@Test
	void testAddAllMultimap() {
	    // given
		Multimap<String, List<IQueryParameterType>> map = ArrayListMultimap.create();
		map.put("param1", List.of(new TokenParam("value1"), new TokenParam("value2")));
		map.put("param1", List.of(new TokenParam("value3")));
		map.put("param2", List.of(new TokenParam("value4")));

	    // when
		myBuilder.addAll(map);

	    // then
		assertEquals(map, myBuilder.toMultiMap());
	}


	@Test
	void testAddAllMap() {
		// given
		Map<String, List<IQueryParameterType>> map = new HashMap<>();
		map.put("param1", List.of(new TokenParam("value1"), new TokenParam("value2")));
		map.put("param2", List.of(new TokenParam("value4")));

		// when
		myBuilder.addAll(map);

		// then
		// use a set for comparison.
		var actualAsSetMultiMap = LinkedHashMultimap.create(myBuilder.toMultiMap());
		assertEquals(Multimaps.forMap(map), actualAsSetMultiMap);
	}

}
