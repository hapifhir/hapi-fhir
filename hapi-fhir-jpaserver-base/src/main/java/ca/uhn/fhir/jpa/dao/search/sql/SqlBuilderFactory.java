package ca.uhn.fhir.jpa.dao.search.sql;

import ca.uhn.fhir.jpa.dao.search.querystack.QueryStack3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class SqlBuilderFactory {

	@Autowired
	private ApplicationContext myApplicationContext;

	// FIXME: rename all these

	public CoordsIndexTable coordsIndexTable(SearchSqlBuilder theSearchSqlBuilder) {
		return myApplicationContext.getBean(CoordsIndexTable.class, theSearchSqlBuilder);
	}

	public DateIndexTable dateIndexTable(SearchSqlBuilder theSearchSqlBuilder) {
		return myApplicationContext.getBean(DateIndexTable.class, theSearchSqlBuilder);
	}

	public NumberIndexTable numberIndexTable(SearchSqlBuilder theSearchSqlBuilder) {
		return myApplicationContext.getBean(NumberIndexTable.class, theSearchSqlBuilder);
	}

	public QuantityIndexTable quantityIndexTable(SearchSqlBuilder theSearchSqlBuilder) {
		return myApplicationContext.getBean(QuantityIndexTable.class, theSearchSqlBuilder);
	}

	public ResourceLinkIndexTable referenceIndexTable(QueryStack3 theQueryStack, SearchSqlBuilder theSearchSqlBuilder, boolean theReversed) {
		return myApplicationContext.getBean(ResourceLinkIndexTable.class, theQueryStack, theSearchSqlBuilder, theReversed);
	}

	public ResourceSqlTable resourceTable(SearchSqlBuilder theSearchSqlBuilder) {
		return myApplicationContext.getBean(ResourceSqlTable.class, theSearchSqlBuilder);
	}

	public ResourceIdPredicateBuilder3 resourceId(SearchSqlBuilder theSearchSqlBuilder) {
		return myApplicationContext.getBean(ResourceIdPredicateBuilder3.class, theSearchSqlBuilder);
	}

	public SearchParamPresenceTable searchParamPresenceTable(SearchSqlBuilder theSearchSqlBuilder) {
		return myApplicationContext.getBean(SearchParamPresenceTable.class, theSearchSqlBuilder);
	}

	public StringIndexTable stringIndexTable(SearchSqlBuilder theSearchSqlBuilder) {
		return myApplicationContext.getBean(StringIndexTable.class, theSearchSqlBuilder);
	}

	public TokenIndexTable tokenIndexTable(SearchSqlBuilder theSearchSqlBuilder) {
		return myApplicationContext.getBean(TokenIndexTable.class, theSearchSqlBuilder);
	}

	public UriIndexTable uriIndexTable(SearchSqlBuilder theSearchSqlBuilder) {
		return myApplicationContext.getBean(UriIndexTable.class, theSearchSqlBuilder);
	}

	public TagPredicateBuilder newTagPredicateBuilder(SearchSqlBuilder theSearchSqlBuilder) {
		return myApplicationContext.getBean(TagPredicateBuilder.class, theSearchSqlBuilder);
	}

	public SourcePredicateBuilder newSourcePredicateBuilder(SearchSqlBuilder theSearchSqlBuilder) {
		return myApplicationContext.getBean(SourcePredicateBuilder.class, theSearchSqlBuilder);
	}
}
