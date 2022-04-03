package ca.uhn.fhir.jpa.search.builder.sql;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.config.HibernatePropertiesProvider;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.search.builder.predicate.BaseJoiningPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.DatePredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ResourceTablePredicateBuilder;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.OrderObject;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.jdbc.internal.BasicFormatterImpl;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public abstract class BaseSearchQueryBuilderDialectTest {

	private static final Logger ourLog = LoggerFactory.getLogger(BaseSearchQueryBuilderDialectTest.class);
	protected final FhirContext myFhirContext = FhirContext.forR4Cached();
	@Mock
	protected SqlObjectFactory mySqlObjectFactory;
	@Mock
	protected HibernatePropertiesProvider myHibernatePropertiesProvider;

	@BeforeEach
	public void beforeInitMocks() {
		when(myHibernatePropertiesProvider.getDialect())
			.thenReturn(createDialect());
	}

	@Nonnull
	protected abstract Dialect createDialect();

	protected SearchQueryBuilder createSearchQueryBuilder() {
		return new SearchQueryBuilder(myFhirContext, new ModelConfig(), new PartitionSettings(), RequestPartitionId.allPartitions(), "Patient", mySqlObjectFactory, myHibernatePropertiesProvider, false);
	}

	protected GeneratedSql buildSqlWithNumericSort(Boolean theAscending, OrderObject.NullOrder theNullOrder) {
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

		return searchQueryBuilder.generate(0, 500);

	}

	public void logSql(GeneratedSql theGeneratedSql) {
		String output = new BasicFormatterImpl().format(theGeneratedSql.getSql());
		ourLog.info("SQL: {}", output);
	}
}
