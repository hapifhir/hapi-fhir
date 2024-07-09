package ca.uhn.fhir.jpa.search.builder.sql;

import ca.uhn.fhir.jpa.model.dialect.HapiFhirSQLServerDialect;
import ca.uhn.fhir.jpa.search.builder.predicate.ResourceTablePredicateBuilder;
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
public class SearchQueryBuilderDialectSqlServerTest extends BaseSearchQueryBuilderDialectTest {

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
		when(mySqlObjectFactory.resourceTable(any())).thenReturn(new ResourceTablePredicateBuilder(searchQueryBuilder));

		GeneratedSql generatedSql = searchQueryBuilder.generate(10, 500);
		logSql(generatedSql);

		String sql = generatedSql.getSql();
		sql = massageSql(sql);
		assertThat(sql.endsWith("order by @@version offset ? rows fetch next ? rows only")).as(sql).isTrue();

		assertEquals(3, StringUtils.countMatches(sql, "?"));
		assertThat(generatedSql.getBindVariables()).hasSize(3);
	}

	@Test
	public void testRangeWithoutOffset() {
		SearchQueryBuilder searchQueryBuilder = createSearchQueryBuilder();
		when(mySqlObjectFactory.resourceTable(any())).thenReturn(new ResourceTablePredicateBuilder(searchQueryBuilder));

		GeneratedSql generatedSql = searchQueryBuilder.generate(0, 500);
		logSql(generatedSql);

		String sql = generatedSql.getSql();
		sql = massageSql(sql);
		assertThat(sql.endsWith("order by @@version offset 0 rows fetch first ? rows only")).as(sql).isTrue();

		assertEquals(2, StringUtils.countMatches(sql, "?"));
		assertThat(generatedSql.getBindVariables()).hasSize(2);
	}

	@Nonnull
	@Override
	protected Dialect createDialect() {
		return new HapiFhirSQLServerDialect();
	}
}
