package ca.uhn.fhir.jpa.dao.search.sql;

import ca.uhn.fhir.jpa.dao.search.querystack.QueryStack3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class SqlBuilderFactory {

	@Autowired
	private ApplicationContext myApplicationContext;

	// FIXME: rename all these

	public CompositeUniqueSearchParameterPredicateBuilder newCompositeUniqueSearchParameterPredicateBuilder(SearchSqlBuilder theSearchSqlBuilder) {
		return myApplicationContext.getBean(CompositeUniqueSearchParameterPredicateBuilder.class, theSearchSqlBuilder);
	}

	public CoordsPredicateBuilder coordsIndexTable(SearchSqlBuilder theSearchSqlBuilder) {
		return myApplicationContext.getBean(CoordsPredicateBuilder.class, theSearchSqlBuilder);
	}

	public DatePredicateBuilder dateIndexTable(SearchSqlBuilder theSearchSqlBuilder) {
		return myApplicationContext.getBean(DatePredicateBuilder.class, theSearchSqlBuilder);
	}

	public ForcedIdPredicateBuilder newForcedIdPredicateBuilder(SearchSqlBuilder theSearchSqlBuilder) {
		return myApplicationContext.getBean(ForcedIdPredicateBuilder.class, theSearchSqlBuilder);
	}

	public NumberPredicateBuilder numberIndexTable(SearchSqlBuilder theSearchSqlBuilder) {
		return myApplicationContext.getBean(NumberPredicateBuilder.class, theSearchSqlBuilder);
	}

	public QuantityPredicateBuilder quantityIndexTable(SearchSqlBuilder theSearchSqlBuilder) {
		return myApplicationContext.getBean(QuantityPredicateBuilder.class, theSearchSqlBuilder);
	}

	public ResourceLinkPredicateBuilder referenceIndexTable(QueryStack3 theQueryStack, SearchSqlBuilder theSearchSqlBuilder, boolean theReversed) {
		return myApplicationContext.getBean(ResourceLinkPredicateBuilder.class, theQueryStack, theSearchSqlBuilder, theReversed);
	}

	public ResourceSqlTable resourceTable(SearchSqlBuilder theSearchSqlBuilder) {
		return myApplicationContext.getBean(ResourceSqlTable.class, theSearchSqlBuilder);
	}

	public ResourceIdPredicateBuilder3 resourceId(SearchSqlBuilder theSearchSqlBuilder) {
		return myApplicationContext.getBean(ResourceIdPredicateBuilder3.class, theSearchSqlBuilder);
	}

	public SearchParamPresentPredicateBuilder searchParamPresentPredicateBuilder(SearchSqlBuilder theSearchSqlBuilder) {
		return myApplicationContext.getBean(SearchParamPresentPredicateBuilder.class, theSearchSqlBuilder);
	}

	public StringPredicateBuilder stringIndexTable(SearchSqlBuilder theSearchSqlBuilder) {
		return myApplicationContext.getBean(StringPredicateBuilder.class, theSearchSqlBuilder);
	}

	public TokenPredicateBuilder tokenIndexTable(SearchSqlBuilder theSearchSqlBuilder) {
		return myApplicationContext.getBean(TokenPredicateBuilder.class, theSearchSqlBuilder);
	}

	public UriPredicateBuilder uriIndexTable(SearchSqlBuilder theSearchSqlBuilder) {
		return myApplicationContext.getBean(UriPredicateBuilder.class, theSearchSqlBuilder);
	}

	public TagPredicateBuilder newTagPredicateBuilder(SearchSqlBuilder theSearchSqlBuilder) {
		return myApplicationContext.getBean(TagPredicateBuilder.class, theSearchSqlBuilder);
	}

	public SourcePredicateBuilder newSourcePredicateBuilder(SearchSqlBuilder theSearchSqlBuilder) {
		return myApplicationContext.getBean(SourcePredicateBuilder.class, theSearchSqlBuilder);
	}
}
