package ca.uhn.fhir.jpa.search.builder.sql;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.config.HibernatePropertiesProvider;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.search.builder.predicate.BaseJoiningPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.DatePredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ResourceTablePredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.StringPredicateBuilder;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.OrderObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class SearchQueryBuilderMySqlTest {

	@Mock
	private SqlObjectFactory mySqlObjectFactory;
	@Mock
	private HibernatePropertiesProvider myHibernatePropertiesProvider;

	private final FhirContext myFhirContext = FhirContext.forR4();

	@BeforeEach
	public void beforeInitMocks() {
		MockitoAnnotations.initMocks(this);
		when(myHibernatePropertiesProvider.getDialect()).thenReturn(new org.hibernate.dialect.MySQL57Dialect());
	}

	private SearchQueryBuilder createSearchQueryBuilder() {
		return new SearchQueryBuilder(myFhirContext, new ModelConfig(), new PartitionSettings(), RequestPartitionId.allPartitions(), "Patient", mySqlObjectFactory, myHibernatePropertiesProvider, false);
	}

	@Test
	public void testAddSortNumericNoNullOrder() {
		GeneratedSql generatedSql = buildSqlWithNumericSort(true,null);
		assertTrue(generatedSql.getSql().endsWith("ORDER BY -t1.SP_VALUE_LOW DESC limit ?"));

		generatedSql =  buildSqlWithNumericSort(false,null);
		assertTrue(generatedSql.getSql().endsWith("ORDER BY t1.SP_VALUE_LOW DESC limit ?"));

	}

	private GeneratedSql buildSqlWithNumericSort(Boolean theAscending, OrderObject.NullOrder theNullOrder) {
		SearchQueryBuilder searchQueryBuilder = createSearchQueryBuilder();
		when(mySqlObjectFactory.resourceTable(any())).thenReturn(new ResourceTablePredicateBuilder(searchQueryBuilder));
		when(mySqlObjectFactory.dateIndexTable(any())).thenReturn(new DatePredicateBuilder(searchQueryBuilder));

		BaseJoiningPredicateBuilder firstPredicateBuilder = searchQueryBuilder.getOrCreateFirstPredicateBuilder();
		DatePredicateBuilder sortPredicateBuilder = searchQueryBuilder.addDatePredicateBuilder(firstPredicateBuilder.getResourceIdColumn());

		Condition hashIdentityPredicate = sortPredicateBuilder.createHashIdentityPredicate("MolecularSequence", "variant-start");
		searchQueryBuilder.addPredicate(hashIdentityPredicate);
		if (theNullOrder == null) {
			searchQueryBuilder.addSortNumeric(sortPredicateBuilder.getColumnValueLow(), theAscending);
		} else {
			searchQueryBuilder.addSortNumeric(sortPredicateBuilder.getColumnValueLow(), theAscending, theNullOrder);
		}

		return searchQueryBuilder.generate(0,500);

	}

	@Test
	public void testAddSortNumericWithNullOrder() {
		GeneratedSql generatedSql =  buildSqlWithNumericSort(true, OrderObject.NullOrder.FIRST);
		assertTrue(generatedSql.getSql().endsWith("ORDER BY t1.SP_VALUE_LOW ASC limit ?"));

		generatedSql = buildSqlWithNumericSort(false, OrderObject.NullOrder.FIRST);
		assertTrue(generatedSql.getSql().endsWith("ORDER BY -t1.SP_VALUE_LOW ASC limit ?"));

		generatedSql = buildSqlWithNumericSort(true, OrderObject.NullOrder.LAST);
		assertTrue(generatedSql.getSql().endsWith("ORDER BY -t1.SP_VALUE_LOW DESC limit ?"));

		generatedSql = buildSqlWithNumericSort(false, OrderObject.NullOrder.LAST);
		assertTrue(generatedSql.getSql().endsWith("ORDER BY t1.SP_VALUE_LOW DESC limit ?"));

	}

	@Test
	public void testAddSortStringNoNullOrder() {
		GeneratedSql generatedSql = buildSqlWithStringSort(true,null);
//		assertTrue(generatedSql.getSql().endsWith("ORDER BY CASE WHEN t1.SP_VALUE_NORMALIZED IS NULL THEN 1 ELSE 0 END ASC, t1.SP_VALUE_NORMALIZED ASC limit ?"));
		assertTrue(generatedSql.getSql().endsWith("ORDER BY t1.SP_VALUE_NORMALIZED ASC limit ?"));

		generatedSql = buildSqlWithStringSort(false,null);
		assertTrue(generatedSql.getSql().endsWith("ORDER BY t1.SP_VALUE_NORMALIZED DESC limit ?"));

	}

	private GeneratedSql buildSqlWithStringSort(Boolean theAscending, OrderObject.NullOrder theNullOrder) {
		SearchQueryBuilder searchQueryBuilder = createSearchQueryBuilder();
		when(mySqlObjectFactory.resourceTable(any())).thenReturn(new ResourceTablePredicateBuilder(searchQueryBuilder));
		when(mySqlObjectFactory.stringIndexTable(any())).thenReturn(new StringPredicateBuilder(searchQueryBuilder));

		BaseJoiningPredicateBuilder firstPredicateBuilder = searchQueryBuilder.getOrCreateFirstPredicateBuilder();
		StringPredicateBuilder sortPredicateBuilder = searchQueryBuilder.addStringPredicateBuilder(firstPredicateBuilder.getResourceIdColumn());

		Condition hashIdentityPredicate = sortPredicateBuilder.createHashIdentityPredicate("patient", "family");
		searchQueryBuilder.addPredicate(hashIdentityPredicate);
		if (theNullOrder == null) {
			searchQueryBuilder.addSortString(sortPredicateBuilder.getColumnValueNormalized(), theAscending);
		} else {
			searchQueryBuilder.addSortString(sortPredicateBuilder.getColumnValueNormalized(), theAscending, theNullOrder);
		}

		return searchQueryBuilder.generate(0,500);

	}

	@Test
	public void testAddSortStringWithNullOrder() {
		GeneratedSql generatedSql =  buildSqlWithStringSort(true, OrderObject.NullOrder.FIRST);
		assertTrue(generatedSql.getSql().endsWith("ORDER BY t1.SP_VALUE_NORMALIZED ASC limit ?"));

		generatedSql = buildSqlWithStringSort(false, OrderObject.NullOrder.FIRST);
//		assertTrue(generatedSql.getSql().endsWith("ORDER BY CASE WHEN t1.SP_VALUE_NORMALIZED IS NULL THEN 1 ELSE 0 END DESC, t1.SP_VALUE_NORMALIZED DESC limit ?"));
		assertTrue(generatedSql.getSql().endsWith("ORDER BY t1.SP_VALUE_NORMALIZED DESC limit ?"));

		generatedSql = buildSqlWithStringSort(true, OrderObject.NullOrder.LAST);
//		assertTrue(generatedSql.getSql().endsWith("ORDER BY CASE WHEN t1.SP_VALUE_NORMALIZED IS NULL THEN 1 ELSE 0 END ASC, t1.SP_VALUE_NORMALIZED ASC limit ?"));
		assertTrue(generatedSql.getSql().endsWith("ORDER BY t1.SP_VALUE_NORMALIZED ASC limit ?"));

		generatedSql = buildSqlWithStringSort(false, OrderObject.NullOrder.LAST);
		assertTrue(generatedSql.getSql().endsWith("ORDER BY t1.SP_VALUE_NORMALIZED DESC limit ?"));

	}

	@Test
	public void testAddSortDateNoNullOrder() {
		GeneratedSql generatedSql = buildSqlWithDateSort(true,null);
//		assertTrue(generatedSql.getSql().endsWith("ORDER BY CASE WHEN t1.SP_VALUE_LOW IS NULL THEN 1 ELSE 0 END ASC, t1.SP_VALUE_LOW ASC limit ?"));
		assertTrue(generatedSql.getSql().endsWith("ORDER BY t1.SP_VALUE_LOW ASC limit ?"));

		generatedSql = buildSqlWithDateSort(false,null);
		assertTrue(generatedSql.getSql().endsWith("ORDER BY t1.SP_VALUE_LOW DESC limit ?"));

	}

	private GeneratedSql buildSqlWithDateSort(Boolean theAscending, OrderObject.NullOrder theNullOrder) {
		SearchQueryBuilder searchQueryBuilder = createSearchQueryBuilder();
		when(mySqlObjectFactory.resourceTable(any())).thenReturn(new ResourceTablePredicateBuilder(searchQueryBuilder));
		when(mySqlObjectFactory.dateIndexTable(any())).thenReturn(new DatePredicateBuilder(searchQueryBuilder));

		BaseJoiningPredicateBuilder firstPredicateBuilder = searchQueryBuilder.getOrCreateFirstPredicateBuilder();
		DatePredicateBuilder sortPredicateBuilder = searchQueryBuilder.addDatePredicateBuilder(firstPredicateBuilder.getResourceIdColumn());

		Condition hashIdentityPredicate = sortPredicateBuilder.createHashIdentityPredicate("patient", "birthdate");
		searchQueryBuilder.addPredicate(hashIdentityPredicate);
		if (theNullOrder == null) {
			searchQueryBuilder.addSortDate(sortPredicateBuilder.getColumnValueLow(), theAscending);
		} else {
			searchQueryBuilder.addSortDate(sortPredicateBuilder.getColumnValueLow(), theAscending, theNullOrder);
		}

		return searchQueryBuilder.generate(0,500);

	}

	@Test
	public void testAddSortDateWithNullOrder() {
		GeneratedSql generatedSql =  buildSqlWithDateSort(true, OrderObject.NullOrder.FIRST);
		assertTrue(generatedSql.getSql().endsWith("ORDER BY t1.SP_VALUE_LOW ASC limit ?"));

		generatedSql = buildSqlWithDateSort(false, OrderObject.NullOrder.FIRST);
//		assertTrue(generatedSql.getSql().endsWith("ORDER BY CASE WHEN t1.SP_VALUE_LOW IS NULL THEN 1 ELSE 0 END DESC, t1.SP_VALUE_LOW DESC limit ?"));
		assertTrue(generatedSql.getSql().endsWith("ORDER BY t1.SP_VALUE_LOW DESC limit ?"));

		generatedSql = buildSqlWithDateSort(true, OrderObject.NullOrder.LAST);
//		assertTrue(generatedSql.getSql().endsWith("ORDER BY CASE WHEN t1.SP_VALUE_LOW IS NULL THEN 1 ELSE 0 END ASC, t1.SP_VALUE_LOW ASC limit ?"));
		assertTrue(generatedSql.getSql().endsWith("ORDER BY t1.SP_VALUE_LOW ASC limit ?"));

		generatedSql = buildSqlWithDateSort(false, OrderObject.NullOrder.LAST);
		assertTrue(generatedSql.getSql().endsWith("ORDER BY t1.SP_VALUE_LOW DESC limit ?"));

	}
}
