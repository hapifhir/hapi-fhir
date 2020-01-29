package ca.uhn.fhir.jpa.dao.predicate;

import ca.uhn.fhir.model.api.IQueryParameterType;

import javax.annotation.Nullable;
import javax.persistence.criteria.Predicate;
import java.util.List;

public interface IPredicateBuilder {
	@Nullable
	Predicate addPredicate(String theResourceName,
								  String theParamName,
								  List<? extends IQueryParameterType> theList,
								  SearchFilterParser.CompareOperation operation);
}
