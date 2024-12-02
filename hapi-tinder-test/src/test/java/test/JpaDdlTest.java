package test;

import ca.uhn.fhir.tinder.ddl.GenerateDdlMojo;
import jakarta.annotation.Nonnull;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class JpaDdlTest {

	/**
	 * This test uses the generate-ddl plugin to generate the schema. This can be
	 * used to debug issues in the conditional ID PK mapping work.
	 */
	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testGenerateSchema(boolean theTrimConditionalIdsFromPrimaryKeys) throws Exception {
		// Setup
		GenerateDdlMojo m = new GenerateDdlMojo();
		m.packageNames = List.of(
			"ca.uhn.fhir.jpa.entity",
			"ca.uhn.fhir.jpa.model.entity"
		);
		m.outputDirectory = "target/schema";
		m.dialects = List.of(
			new GenerateDdlMojo.Dialect("ca.uhn.fhir.jpa.model.dialect.HapiFhirH2Dialect", "h2.sql"),
			new GenerateDdlMojo.Dialect("ca.uhn.fhir.jpa.model.dialect.HapiFhirPostgresDialect", "postgres.sql")
		);
		m.trimConditionalIdsFromPrimaryKeys = theTrimConditionalIdsFromPrimaryKeys;

		// Test
		m.execute();

		// Verify
		List<String> sqlStatements = loadDdlAndExtractStatements("target/schema/postgres.sql");

		String sql = findCreateTable(sqlStatements, "HFJ_RESOURCE");
		if (theTrimConditionalIdsFromPrimaryKeys) {
			assertThat(sql).contains("primary key (RES_ID)");
			assertThat(sql).contains("constraint IDX_RES_TYPE_FHIR_ID unique (RES_TYPE, FHIR_ID)");
		} else {
			assertThat(sql).contains("primary key (RES_ID, PARTITION_ID)");
			assertThat(sql).contains("constraint IDX_RES_TYPE_FHIR_ID unique (PARTITION_ID, RES_TYPE, FHIR_ID)");
		}

		sql = findCreateTable(sqlStatements, "HFJ_RES_VER");
		if (theTrimConditionalIdsFromPrimaryKeys) {
			assertThat(sql).contains("primary key (PID)");
			assertThat(sql).contains("constraint IDX_RESVER_ID_VER unique (RES_ID, RES_VER)");
		} else {
			assertThat(sql).contains("primary key (PARTITION_ID, PID)");
			assertThat(sql).contains("constraint IDX_RESVER_ID_VER unique (PARTITION_ID, RES_ID, RES_VER)");
		}

		sql = findCreateConstraint(sqlStatements, "HFJ_RES_VER", "FK_RESOURCE_HISTORY_RESOURCE");
		if (theTrimConditionalIdsFromPrimaryKeys) {
			assertThat(sql).contains("foreign key (RES_ID)");
		} else {
			assertThat(sql).contains("foreign key (RES_ID, PARTITION_ID)");
		}

		sql = findCreateTable(sqlStatements, "HFJ_HISTORY_TAG");
		if (theTrimConditionalIdsFromPrimaryKeys) {
			assertThat(sql).contains("constraint IDX_RESHISTTAG_TAGID unique (RES_VER_PID, TAG_ID)");
		} else {
			assertThat(sql).contains("constraint IDX_RESHISTTAG_TAGID unique (PARTITION_ID, RES_VER_PID, TAG_ID)");
		}

		sql = findCreateTable(sqlStatements, "HFJ_RES_TAG");
		if (theTrimConditionalIdsFromPrimaryKeys) {
			assertThat(sql).contains("constraint IDX_RESTAG_TAGID unique (RES_ID, TAG_ID)");
		} else {
			assertThat(sql).contains("constraint IDX_RESTAG_TAGID unique (PARTITION_ID, RES_ID, TAG_ID)");
		}
	}

	private static String findCreateConstraint(List<String> sqlStatements, String tableName, String constraintName) {
		return sqlStatements.stream().filter(t->t.startsWith("alter table if exists "+ tableName +" add constraint "+ constraintName +" ")).findFirst().orElseThrow(()->new IllegalStateException("Couldn't find create FK constraint "+ constraintName +". Statements: " + sqlStatements));
	}

	private static String findCreateTable(List<String> sqlStatements, String tableName) {
		return sqlStatements.stream().filter(t->t.startsWith("create table " + tableName + " ")).findFirst().orElseThrow(()->new IllegalStateException("Couldn't find create "+ tableName +". Statements: " + sqlStatements));
	}

	@Nonnull
	private static List<String> loadDdlAndExtractStatements(String fileName) throws IOException {
		List<String> sqlStatements;
		try (FileReader reader = new FileReader(fileName)) {
			String schema = IOUtils.toString(reader);
			sqlStatements = Arrays.stream(schema.replaceAll("\\s+", " ").split(";"))
				.map(StringUtils::trim)
				.filter(StringUtils::isNotBlank)
				.toList();
		}
		return sqlStatements;
	}


}
