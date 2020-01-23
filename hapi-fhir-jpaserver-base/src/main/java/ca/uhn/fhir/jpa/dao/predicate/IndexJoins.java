package ca.uhn.fhir.jpa.dao.predicate;

import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import com.google.common.collect.Maps;

import javax.persistence.criteria.Join;
import java.util.Map;

public class IndexJoins {
	Map<SearchBuilderJoinKey, Join<?, ?>> myIndexJoins = Maps.newHashMap();

	public void put(SearchBuilderJoinKey theKey, Join<ResourceTable, ResourceIndexedSearchParamDate> theJoin) {
		myIndexJoins.put(theKey, theJoin);
	}

	public boolean haveIndexJoins() {
		return !myIndexJoins.isEmpty();
	}

	public Join<?,?> get(SearchBuilderJoinKey theKey) {
		myIndexJoins.get(theKey);
	}
}
