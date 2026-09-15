package ca.uhn.fhir.jpa.search.builder.sql;

import ca.uhn.fhir.jpa.model.dialect.HapiFhirOracleDialect;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.dialect.Dialect;
import org.hibernate.query.TypedParameterValue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Clob;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// Created by claude-opus-5
@ExtendWith(MockitoExtension.class)
public class SearchQueryBuilderDialectOracleTest extends BaseSearchQueryBuilderDialectTest {

	/**
	 * GL-9268: Oracle must unpack a large ID list with JSON_TABLE and bind the JSON array as a CLOB.
	 * A plain String bind is a VARCHAR2 SQL bind, which is capped at 4,000 bytes by default - already
	 * exceeded by the JSON array at the shipped threshold - and raises ORA-01461.
	 */
	@Test
	void testResourceIdsOverThreshold_bindsJsonArrayAsClob() throws SQLException {
		StorageSettings storageSettings = new StorageSettings();
		storageSettings.setLargeIdListJsonThreshold(3);

		SearchQueryBuilder searchQueryBuilder = createSearchQueryBuilder(storageSettings);
		GeneratedSql generatedSql = generateResourceIdsPredicate(searchQueryBuilder, 1L, 2L, 3L, 4L, 5L);
		logSql(generatedSql);

		String sql = generatedSql.getSql();
		assertThat(sql).contains("JSON_TABLE(?, '$[*]' COLUMNS (id NUMBER PATH '$'))");
		assertThat(StringUtils.countMatches(sql, "?")).as(sql).isEqualTo(2);

		List<Object> bindVariables = generatedSql.getBindVariables();
		assertThat(bindVariables).hasSize(2);
		assertThat(bindVariables.get(0)).isEqualTo("Patient");

		Object idListBind = bindVariables.get(1);
		assertThat(idListBind)
			.as("Oracle must bind the JSON array as a CLOB, not as a plain String: %s", idListBind)
			.isInstanceOfAny(Clob.class, TypedParameterValue.class);
		assertThat(extractCharacterContent(idListBind)).isEqualTo("[1,2,3,4,5]");
	}

	@Nonnull
	private static String extractCharacterContent(Object theBindVariable) throws SQLException {
		if (theBindVariable instanceof Clob clob) {
			return clob.getSubString(1, (int) clob.length());
		}
		if (theBindVariable instanceof TypedParameterValue<?> typedParameterValue) {
			return String.valueOf(typedParameterValue.getValue());
		}
		throw new AssertionError("Not a CLOB-carrying bind variable: " + theBindVariable);
	}

	@Nonnull
	@Override
	protected Dialect createDialect() {
		return new HapiFhirOracleDialect();
	}
}
