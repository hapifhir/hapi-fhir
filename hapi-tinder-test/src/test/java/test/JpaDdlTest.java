package test;

import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirH2Dialect;
import ca.uhn.fhir.tinder.ddl.GenerateDdlMojo;
import jakarta.annotation.Nonnull;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class JpaDdlTest {

	/**
	 * See {@link HapiFhirH2Dialect#supportsColumnCheck()}
	 */
	@ParameterizedTest
	@CsvSource({
		"ca.uhn.fhir.jpa.model.dialect.HapiFhirDerbyDialect",
		"ca.uhn.fhir.jpa.model.dialect.HapiFhirH2Dialect",
		"ca.uhn.fhir.jpa.model.dialect.HapiFhirMariaDBDialect",
		"ca.uhn.fhir.jpa.model.dialect.HapiFhirMySQLDialect",
		"ca.uhn.fhir.jpa.model.dialect.HapiFhirSQLServerDialect",
		"ca.uhn.fhir.jpa.model.dialect.HapiFhirPostgresDialect",
		"ca.uhn.fhir.jpa.model.dialect.HapiFhirOracleDialect",
		"ca.uhn.fhir.jpa.model.dialect.HapiFhirCockroachDialect",
	})
	public void testNoEnums(String theDialectName) throws Exception {
		// Setup
		GenerateDdlMojo m = new GenerateDdlMojo();
		m.packageNames = List.of(
			"ca.uhn.fhir.jpa.entity",
			"ca.uhn.fhir.jpa.model.entity"
		);
		m.outputDirectory = "target/schema";
		m.dialects = List.of(new GenerateDdlMojo.Dialect(theDialectName, "schema.sql"));

		// Test
		m.execute();

		// Verify
		List<String> sqlStatements = loadDdlAndExtractStatements("target/schema/schema.sql");

		String sql = findCreateTable(sqlStatements, "BT2_JOB_INSTANCE");
		assertThat(sql).doesNotContain(WorkChunkStatusEnum.IN_PROGRESS.name());
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
