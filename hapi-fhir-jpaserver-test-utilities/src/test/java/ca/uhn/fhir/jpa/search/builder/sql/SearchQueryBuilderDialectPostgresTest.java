package ca.uhn.fhir.jpa.search.builder.sql;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.predicate.SearchFilterParser;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirPostgresDialect;
import ca.uhn.fhir.jpa.search.builder.predicate.DatePredicateBuilder;
import ca.uhn.fhir.rest.param.DateParam;
import com.healthmarketscience.sqlbuilder.Condition;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.dialect.Dialect;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SearchQueryBuilderDialectPostgresTest extends BaseSearchQueryBuilderDialectTest {

	/**
	 * Make sure we're using integers and not strings as bind variables
	 * for ordinals
	 */
	@Test
	public void testOrdinalSearchesUseIntegerParameters() {
		JpaStorageSettings storageSettings = new JpaStorageSettings();
		storageSettings.setUseOrdinalDatesForDayPrecisionSearches(true);

		SearchQueryBuilder searchQueryBuilder = createSearchQueryBuilder();
		when(mySqlObjectFactory.dateIndexTable(any())).thenReturn(new DatePredicateBuilder(searchQueryBuilder));

		DatePredicateBuilder datePredicateBuilder = searchQueryBuilder.addDatePredicateBuilder(null);
		datePredicateBuilder.setStorageSettingsForUnitTest(storageSettings);

		Condition datePredicate = datePredicateBuilder.createPredicateDateWithoutIdentityPredicate(new DateParam("2022"), SearchFilterParser.CompareOperation.eq);
		Condition comboPredicate = datePredicateBuilder.combineWithHashIdentityPredicate("Observation", "date", datePredicate);

		searchQueryBuilder.addPredicate(comboPredicate);

		GeneratedSql generatedSql = searchQueryBuilder.generate(0, 500);
		logSql(generatedSql);

		String sql = generatedSql.getSql();
		assertEquals("SELECT t0.RES_ID FROM HFJ_SPIDX_DATE t0 WHERE ((t0.HASH_IDENTITY = ?) AND ((t0.SP_VALUE_LOW_DATE_ORDINAL >= ?) AND (t0.SP_VALUE_HIGH_DATE_ORDINAL <= ?))) fetch first ? rows only", sql);

		assertEquals(4, StringUtils.countMatches(sql, "?"));
		assertThat(generatedSql.getBindVariables()).hasSize(4);
		assertEquals(123682819940570799L, generatedSql.getBindVariables().get(0));
		assertEquals(20220101, generatedSql.getBindVariables().get(1));
		assertEquals(20221231, generatedSql.getBindVariables().get(2));
		assertEquals(500, generatedSql.getBindVariables().get(3));
	}

	@Nonnull
	@Override
	protected Dialect createDialect() {
		return new HapiFhirPostgresDialect();
	}
}
