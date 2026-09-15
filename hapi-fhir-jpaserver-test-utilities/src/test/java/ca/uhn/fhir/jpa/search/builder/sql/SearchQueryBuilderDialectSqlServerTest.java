package ca.uhn.fhir.jpa.search.builder.sql;

import ca.uhn.fhir.jpa.config.HibernatePropertiesProvider;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirSQLServerDialect;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.search.builder.predicate.ResourceTablePredicateBuilder;
import ca.uhn.fhir.rest.api.SearchIncludeDeletedEnum;
import ca.uhn.test.util.LogbackTestExtension;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.dialect.Dialect;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SearchQueryBuilderDialectSqlServerTest extends BaseSearchQueryBuilderDialectTest {

	@RegisterExtension
	public LogbackTestExtension myLogCapture = new LogbackTestExtension(Level.WARN);

	@Test
	public void testAddSort() {
		GeneratedSql generatedSql = buildSqlWithNumericSort(true, null);
		logSql(generatedSql);

		String sql = generatedSql.getSql();
		sql = massageSql(sql);
		assertThat(sql.endsWith("ORDER BY -t1.SP_VALUE_LOW DESC offset 0 rows fetch first ? rows only")).as(sql).isTrue();

		assertEquals(3, StringUtils.countMatches(sql, "?"));
		assertThat(generatedSql.getBindVariables()).hasSize(3);
	}

	@Nonnull
	private static String massageSql(String sql) {
		sql = sql.replace("\n", " ").replaceAll(" +", " ");
		return sql;
	}

	@Test
	public void testRangeWithOffset() {
		SearchQueryBuilder searchQueryBuilder = createSearchQueryBuilder();
		when(mySqlObjectFactory.resourceTable(any(), any())).thenReturn(new ResourceTablePredicateBuilder(searchQueryBuilder, SearchIncludeDeletedEnum.NEVER));

		GeneratedSql generatedSql = searchQueryBuilder.generate(10, 500);
		logSql(generatedSql);

		String sql = generatedSql.getSql();
		sql = massageSql(sql);
		assertThat(sql.endsWith("order by RES_ID offset ? rows fetch next ? rows only")).as(sql).isTrue();

		assertEquals(3, StringUtils.countMatches(sql, "?"));
		assertThat(generatedSql.getBindVariables()).hasSize(3);
	}

	@Test
	public void testRangeWithoutOffset() {
		SearchQueryBuilder searchQueryBuilder = createSearchQueryBuilder();
		when(mySqlObjectFactory.resourceTable(any(), any())).thenReturn(new ResourceTablePredicateBuilder(searchQueryBuilder, SearchIncludeDeletedEnum.NEVER));

		GeneratedSql generatedSql = searchQueryBuilder.generate(0, 500);
		logSql(generatedSql);

		String sql = generatedSql.getSql();
		sql = massageSql(sql);
		assertThat(sql.endsWith("order by RES_ID offset 0 rows fetch first ? rows only")).as(sql).isTrue();

		assertEquals(2, StringUtils.countMatches(sql, "?"));
		assertThat(generatedSql.getBindVariables()).hasSize(2);
	}

	/**
	 * GL-9268: with database compatibility level 130 or higher, SQL Server unpacks a large ID list
	 * with OPENJSON against a single JSON array bind.
	 */
	@Test
	void testResourceIdsOverThreshold_withJsonSupport_bindsSingleJsonArray() {
		HibernatePropertiesProvider dialectProvider = createDialectProvider(true);
		StorageSettings storageSettings = new StorageSettings();
		storageSettings.setLargeIdListJsonThreshold(3);
		SearchQueryBuilder searchQueryBuilder = createSearchQueryBuilder(storageSettings, dialectProvider);

		GeneratedSql generatedSql = generateResourceIdsPredicate(searchQueryBuilder, 1L, 2L, 3L, 4L, 5L);
		logSql(generatedSql);

		String sql = generatedSql.getSql();
		assertThat(sql).contains("OPENJSON(?)");
		assertThat(StringUtils.countMatches(sql, "?")).as(sql).isEqualTo(2);
		assertThat(generatedSql.getBindVariables()).containsExactly("Patient", "[1,2,3,4,5]");
	}

	/**
	 * GL-9268: OPENJSON requires database compatibility level 130 (SQL Server 2016). Below that the
	 * predicate must keep rendering today's IN list rather than emitting SQL the database cannot parse -
	 * and the fallback must not be silent, but it must also not flood the log: the probe result is
	 * cached on the HibernatePropertiesProvider, so a second SearchQueryBuilder built on the same
	 * provider does not warn again.
	 */
	@Test
	void testResourceIdsOverThreshold_withoutJsonSupport_keepsInListAndWarnsOnce() {
		HibernatePropertiesProvider dialectProvider = createDialectProvider(false);
		StorageSettings storageSettings = new StorageSettings();
		storageSettings.setLargeIdListJsonThreshold(3);

		GeneratedSql generatedSql = generateResourceIdsPredicate(createSearchQueryBuilder(storageSettings, dialectProvider), 1L, 2L, 3L, 4L, 5L);
		logSql(generatedSql);

		String sql = generatedSql.getSql();
		assertThat(sql).contains("t0.RES_ID IN (?,?,?,?,?)");
		assertThat(sql.toUpperCase(Locale.ROOT)).doesNotContain("OPENJSON");
		assertThat(generatedSql.getBindVariables()).containsExactly("Patient", 1L, 2L, 3L, 4L, 5L);

		List<ILoggingEvent> compatibilityLevelWarnings = compatibilityLevelWarnings();
		assertThat(compatibilityLevelWarnings)
			.as("Exactly one compatibility level warning is expected after the first fallback")
			.hasSize(1);
		assertThat(compatibilityLevelWarnings.get(0).getFormattedMessage()).contains("130");

		// A second builder on the same provider - the probe result is cached, so no new warning is logged.
		generateResourceIdsPredicate(createSearchQueryBuilder(storageSettings, dialectProvider), 1L, 2L, 3L, 4L, 5L);
		assertThat(compatibilityLevelWarnings())
			.as("A second builder on the same provider must not warn again")
			.hasSize(1);
	}

	@Nonnull
	private List<ILoggingEvent> compatibilityLevelWarnings() {
		return myLogCapture.getLogEvents().stream()
			.filter(t -> t.getLevel() == Level.WARN)
			.filter(t -> t.getFormattedMessage().toLowerCase(Locale.ROOT).contains("compatibility level"))
			.toList();
	}

	@Nonnull
	private HibernatePropertiesProvider createDialectProvider(boolean theJsonSupported) {
		HibernatePropertiesProvider dialectProvider = new HibernatePropertiesProvider();
		dialectProvider.setDialectForUnitTest(createDialect());
		dialectProvider.setSqlServerJsonSupportedForUnitTest(theJsonSupported);
		return dialectProvider;
	}

	@Nonnull
	@Override
	protected Dialect createDialect() {
		return new HapiFhirSQLServerDialect();
	}
}
