package test;

import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirH2Dialect;
import ca.uhn.fhir.tinder.ddl.GenerateDdlMojo;
import jakarta.annotation.Nonnull;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

    /**
     * This test uses the generate-ddl plugin to generate the schema. This can be
     * used to debug issues in the conditional ID PK mapping work.
     */
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void testGenerateSchema(boolean theDatabasePartitionMode) throws Exception {
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
        m.databasePartitionMode = theDatabasePartitionMode;
        
        // Test
        m.execute();
        
        // Verify
        List<String> sqlStatements = loadDdlAndExtractStatements("target/schema/postgres.sql");

		@Language("SQL")
        String sql = findCreateTable(sqlStatements, "HFJ_RESOURCE");
		if (theDatabasePartitionMode) {
			assertThat(sql).contains("primary key (RES_ID, PARTITION_ID)");
			assertThat(sql).contains("constraint IDX_RES_TYPE_FHIR_ID unique (PARTITION_ID, RES_TYPE, FHIR_ID)");
		} else {
			assertThat(sql).contains("primary key (RES_ID)");
			assertThat(sql).contains("constraint IDX_RES_TYPE_FHIR_ID unique (RES_TYPE, FHIR_ID)");
		}

		sql = findCreateTable(sqlStatements, "HFJ_RES_VER");
		if (theDatabasePartitionMode) {
			assertThat(sql).contains("primary key (PARTITION_ID, PID)");
			assertThat(sql).contains("constraint IDX_RESVER_ID_VER unique (PARTITION_ID, RES_ID, RES_VER)");
		} else {
			assertThat(sql).contains("primary key (PID)");
			assertThat(sql).contains("constraint IDX_RESVER_ID_VER unique (RES_ID, RES_VER)");
		}

		sql = findCreateConstraint(sqlStatements, "HFJ_RES_VER", "FK_RESOURCE_HISTORY_RESOURCE");
		if (theDatabasePartitionMode) {
			assertThat(sql).contains("foreign key (RES_ID, PARTITION_ID)");
		} else {
			assertThat(sql).contains("foreign key (RES_ID)");
		}

		sql = findCreateTable(sqlStatements, "HFJ_HISTORY_TAG");
		if (theDatabasePartitionMode) {
			assertThat(sql).contains("constraint IDX_RESHISTTAG_TAGID unique (PARTITION_ID, RES_VER_PID, TAG_ID)");
		} else {
			assertThat(sql).contains("constraint IDX_RESHISTTAG_TAGID unique (RES_VER_PID, TAG_ID)");
		}

		sql = findCreateTable(sqlStatements, "HFJ_RES_TAG");
		if (theDatabasePartitionMode) {
			assertThat(sql).contains("constraint IDX_RESTAG_TAGID unique (PARTITION_ID, RES_ID, TAG_ID)");
		} else {
			assertThat(sql).contains("constraint IDX_RESTAG_TAGID unique (RES_ID, TAG_ID)");
		}

		sql = findCreateIndex(sqlStatements, "IDX_IDXCMPSTRUNIQ_RESOURCE");
		if (theDatabasePartitionMode) {
			assertEquals("create index IDX_IDXCMPSTRUNIQ_RESOURCE on HFJ_IDX_CMP_STRING_UNIQ (PARTITION_ID, RES_ID)", sql);
		} else {
			assertEquals("create index IDX_IDXCMPSTRUNIQ_RESOURCE on HFJ_IDX_CMP_STRING_UNIQ (RES_ID)", sql);
		}

		sql = findCreateTable(sqlStatements, "HFJ_IDX_CMP_STRING_UNIQ");
		if (theDatabasePartitionMode) {
			assertThat(sql).contains("constraint IDX_IDXCMPSTRUNIQ_STRING unique (PARTITION_ID, IDX_STRING)");
		} else {
			assertThat(sql).contains("constraint IDX_IDXCMPSTRUNIQ_STRING unique (IDX_STRING)");
		}


	}

	private static String findCreateIndex(List<String> sqlStatements, String indexName) {
		return sqlStatements.stream().filter(t -> t.startsWith("create index " + indexName + " ")).findFirst().orElseThrow(() -> new IllegalStateException("Couldn't find index " + indexName + ". Statements: " + sqlStatements));
	}

	private static final Logger ourLog = LoggerFactory.getLogger(JpaDdlTest.class);

    @SuppressWarnings("SameParameterValue")
	private static String findCreateConstraint(List<String> theSqlStatements, String theTableName, String theConstraintName) {
        return theSqlStatements.stream().filter(t->t.startsWith("alter table if exists "+ theTableName +" add constraint "+ theConstraintName +" ")).findFirst().orElseThrow(()->new IllegalStateException("Couldn't find create FK constraint "+ theConstraintName +". Statements: " + theSqlStatements));
    }
    
	private static String findCreateTable(List<String> theSqlStatements, String theTableName) {
		return theSqlStatements.stream().filter(t->t.startsWith("create table " + theTableName + " ")).findFirst().orElseThrow(()->new IllegalStateException("Couldn't find create "+ theTableName +". Statements: " + theSqlStatements));
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
