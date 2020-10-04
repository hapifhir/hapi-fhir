package ca.uhn.fhir.jpa.search.builder.sql;

import ca.uhn.fhir.jpa.search.builder.QueryStack;
import ca.uhn.fhir.jpa.search.builder.predicate.CompositeUniqueSearchParameterPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.CoordsPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.DatePredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ForcedIdPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.NumberPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.QuantityPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ResourceIdPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ResourceLinkPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ResourceTablePredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.SearchParamPresentPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.SourcePredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.StringPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.TagPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.TokenPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.UriPredicateBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class SqlObjectFactory {

	@Autowired
	private ApplicationContext myApplicationContext;

	public CompositeUniqueSearchParameterPredicateBuilder newCompositeUniqueSearchParameterPredicateBuilder(SearchSqlBuilder theSearchSqlBuilder) {
		return myApplicationContext.getBean(CompositeUniqueSearchParameterPredicateBuilder.class, theSearchSqlBuilder);
	}

	public CoordsPredicateBuilder coordsPredicateBuilder(SearchSqlBuilder theSearchSqlBuilder) {
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

	public ResourceLinkPredicateBuilder referenceIndexTable(QueryStack theQueryStack, SearchSqlBuilder theSearchSqlBuilder, boolean theReversed) {
		return myApplicationContext.getBean(ResourceLinkPredicateBuilder.class, theQueryStack, theSearchSqlBuilder, theReversed);
	}

	public ResourceTablePredicateBuilder resourceTable(SearchSqlBuilder theSearchSqlBuilder) {
		return myApplicationContext.getBean(ResourceTablePredicateBuilder.class, theSearchSqlBuilder);
	}

	public ResourceIdPredicateBuilder resourceId(SearchSqlBuilder theSearchSqlBuilder) {
		return myApplicationContext.getBean(ResourceIdPredicateBuilder.class, theSearchSqlBuilder);
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
