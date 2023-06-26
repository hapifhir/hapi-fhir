package ca.uhn.fhir.jpa.search.builder.sql;

import ca.uhn.fhir.jpa.search.builder.predicate.ResourceTablePredicateBuilder;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.SQLServer2012Dialect;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.annotation.Nonnull;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SearchQueryBuilderDialectSqlServerTest extends BaseSearchQueryBuilderDialectTest {

	@Test
	public void testAddSort() {
		GeneratedSql generatedSql = buildSqlWithNumericSort(true, null);
		logSql(generatedSql);

		String sql = generatedSql.getSql();
		assertTrue(sql.endsWith("ORDER BY -t1.SP_VALUE_LOW DESC offset 0 rows fetch next ? rows only"), sql);

		assertEquals(3, StringUtils.countMatches(sql, "?"));
		assertEquals(3, generatedSql.getBindVariables().size());
	}

	@Test
	public void testRangeWithOffset() {
		SearchQueryBuilder searchQueryBuilder = createSearchQueryBuilder();
		when(mySqlObjectFactory.resourceTable(any())).thenReturn(new ResourceTablePredicateBuilder(searchQueryBuilder));

		GeneratedSql generatedSql = searchQueryBuilder.generate(10, 500);
		logSql(generatedSql);

		String sql = generatedSql.getSql();
		assertTrue(sql.endsWith("select page0_ from query where __row__ >= ? and __row__ < ?"), sql);

		assertEquals(3, StringUtils.countMatches(sql, "?"));
		assertEquals(3, generatedSql.getBindVariables().size());
	}

	@Test
	public void testRangeWithoutOffset() {
		SearchQueryBuilder searchQueryBuilder = createSearchQueryBuilder();
		when(mySqlObjectFactory.resourceTable(any())).thenReturn(new ResourceTablePredicateBuilder(searchQueryBuilder));

		GeneratedSql generatedSql = searchQueryBuilder.generate(0, 500);
		logSql(generatedSql);

		String sql = generatedSql.getSql();
		assertTrue(sql.toUpperCase(Locale.ROOT).contains("SELECT TOP(?) T0.RES_ID FROM"), sql);

		assertEquals(2, StringUtils.countMatches(sql, "?"));
		assertEquals(2, generatedSql.getBindVariables().size());
	}

	@Nonnull
	@Override
	protected Dialect createDialect() {
		return new SQLServer2012Dialect();
	}
}
