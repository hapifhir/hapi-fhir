package ca.uhn.fhir.jpa.search.builder.sql;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.predicate.SearchFilterParser;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirPostgresDialect;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
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
		datePredicateBuilder.setSearchParamIdentityCacheSvcForUnitTest(mySearchParamIdentityCacheSvc);

		Condition datePredicate = datePredicateBuilder.createPredicateDateWithoutIdentityPredicate(new DateParam("2022"), SearchFilterParser.CompareOperation.eq);
		Condition comboPredicate = datePredicateBuilder.combineWithHashIdentityPredicate("Observation", "date", datePredicate);

		searchQueryBuilder.addPredicate(comboPredicate);

		GeneratedSql generatedSql = searchQueryBuilder.generate(0, 500);
		logSql(generatedSql);

		String expected = "SELECT t0.RES_ID FROM HFJ_SPIDX_DATE t0 WHERE ((t0.HASH_IDENTITY = ?) AND (((t0.SP_VALUE_LOW_DATE_ORDINAL >= ?) AND (t0.SP_VALUE_LOW_DATE_ORDINAL <= ?)) AND ((t0.SP_VALUE_HIGH_DATE_ORDINAL <= ?) AND (t0.SP_VALUE_HIGH_DATE_ORDINAL >= ?)))) fetch first ? rows only";
		String sql = generatedSql.getSql();
		assertEquals(expected, sql);

		assertEquals(6, StringUtils.countMatches(sql, "?"));
		assertThat(generatedSql.getBindVariables()).hasSize(6);
		assertEquals(123682819940570799L, generatedSql.getBindVariables().get(0));
		assertEquals(20220101, generatedSql.getBindVariables().get(1));
		assertEquals(20221231, generatedSql.getBindVariables().get(2));
		assertEquals(500, generatedSql.getBindVariables().get(5));
	}

	/**
	 * When the ID list handed to the <code>_id</code> predicate is larger than
	 * {@link StorageSettings#getLargeIdListJsonThreshold()}, PostgreSQL must bind the IDs as a single
	 * JSON array string which is unpacked by <code>jsonb_array_elements_text</code>, instead of
	 * emitting one bind variable per ID (which overruns PostgreSQL's 65,535 parameter ceiling).
	 */
	@Test
	void testResourceIdsOverThreshold_bindsSingleJsonArray() {
		StorageSettings storageSettings = new StorageSettings();
		storageSettings.setLargeIdListJsonThreshold(3);

		SearchQueryBuilder searchQueryBuilder = createSearchQueryBuilder(storageSettings);
		GeneratedSql generatedSql = generateResourceIdsPredicate(searchQueryBuilder, 1L, 2L, 3L, 4L, 5L);
		logSql(generatedSql);

		String sql = generatedSql.getSql();
		assertThat(sql).contains("t0.RES_ID IN (SELECT CAST(j.value AS BIGINT) FROM jsonb_array_elements_text(CAST(? AS jsonb)) AS j)");
		assertThat(StringUtils.countMatches(sql, "?")).as(sql).isEqualTo(2);
		assertThat(generatedSql.getBindVariables()).containsExactly("Patient", "[1,2,3,4,5]");
	}

	@Nonnull
	@Override
	protected Dialect createDialect() {
		return new HapiFhirPostgresDialect();
	}
}
