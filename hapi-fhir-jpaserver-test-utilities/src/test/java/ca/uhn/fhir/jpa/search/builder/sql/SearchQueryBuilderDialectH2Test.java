package ca.uhn.fhir.jpa.search.builder.sql;

import ca.uhn.fhir.jpa.model.dialect.HapiFhirH2Dialect;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.dialect.Dialect;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

// Created by claude-opus-5
@ExtendWith(MockitoExtension.class)
public class SearchQueryBuilderDialectH2Test extends BaseSearchQueryBuilderDialectTest {

	/**
	 * H2 has no supported JSON unpacking function in this design, so a large ID list keeps
	 * rendering as a plain IN list with one bind variable per ID.
	 */
	@Test
	void testResourceIdsOverThreshold_keepsInList() {
		StorageSettings storageSettings = new StorageSettings();
		storageSettings.setLargeIdListJsonThreshold(3);

		SearchQueryBuilder searchQueryBuilder = createSearchQueryBuilder(storageSettings);
		GeneratedSql generatedSql = generateResourceIdsPredicate(searchQueryBuilder, 1L, 2L, 3L, 4L, 5L);
		logSql(generatedSql);

		String sql = generatedSql.getSql();
		assertThat(sql).contains("t0.RES_ID IN (?,?,?,?,?)");
		assertThat(sql).doesNotContain("jsonb_array_elements_text");
		assertThat(sql).doesNotContain("JSON_TABLE");
		assertThat(sql).doesNotContain("OPENJSON");
		assertThat(StringUtils.countMatches(sql, "?")).as(sql).isEqualTo(6);
		assertThat(generatedSql.getBindVariables()).containsExactly("Patient", 1L, 2L, 3L, 4L, 5L);
	}

	@Nonnull
	@Override
	protected Dialect createDialect() {
		return new HapiFhirH2Dialect();
	}
}
