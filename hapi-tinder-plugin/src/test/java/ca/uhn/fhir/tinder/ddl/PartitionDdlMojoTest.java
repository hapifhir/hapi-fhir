package ca.uhn.fhir.tinder.ddl;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.util.SqlUtil;
import ca.uhn.fhir.util.FileUtil;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PartitionDdlMojoTest {

	private static final Logger ourLog = LoggerFactory.getLogger(PartitionDdlMojoTest.class);
	private File myInputFile;
	private File myOutputFile;
	private PartitionDdlMojo myMojo;

	@BeforeEach
	public void beforeEach() {
		myInputFile = new File("target", "temp-input.sql");
		myOutputFile = new File("target/sql/output", "temp-output.sql");

		myMojo = new PartitionDdlMojo();
		myMojo.inputFile = myInputFile.getAbsolutePath();
		myMojo.outputFile = myOutputFile;
		myMojo.tuningStatementsFile = "../hapi-fhir-jpaserver-base/src/main/resources/ca/uhn/fhir/jpa/docs/database/hapifhirpostgres94-init01.sql";
		myMojo.driverType = DriverTypeEnum.POSTGRES_9_4;
	}

	@AfterEach
	public void afterEach() {
		FileUtils.deleteQuietly(myInputFile);
		FileUtils.deleteQuietly(myOutputFile);
	}

	@Test
	public void testAddPartition() throws IOException, MojoExecutionException {
		// Setup
		String input = """
			    create table HFJ_RESOURCE (
			        RES_ID bigint not null,
			        PARTITION_ID integer not null,
			        primary key (RES_ID, PARTITION_ID),
			        constraint IDX_RES_TYPE_FHIR_ID unique (PARTITON_ID, RES_TYPE, FHIR_ID)
			    );
			
				create table HFJ_BLK_EXPORT_COLFILE (
					PID bigint not null,
					RES_ID varchar(100) not null,
					primary key (PID)
				);
			""";
		try (FileWriter fw = new FileWriter(myInputFile)) {
			fw.write(input);
		}

		// Test
		myMojo.execute();

		// Verify
		String actual = FileUtil.loadFileAsString(myOutputFile);
		logSql(actual);

		List<String> statements = SqlUtil.splitSqlFileIntoStatements(actual);
		assertThat(statements.get(0)).endsWith(") PARTITION BY HASH (PARTITION_ID)");
		assertEquals("CREATE TABLE HFJ_RESOURCE_PART0 PARTITION OF HFJ_RESOURCE FOR VALUES WITH (MODULUS 5, REMAINDER 0)", statements.get(1));
		assertEquals("CREATE TABLE HFJ_RESOURCE_PART1 PARTITION OF HFJ_RESOURCE FOR VALUES WITH (MODULUS 5, REMAINDER 1)", statements.get(2));
		assertThat(statements.get(11)).startsWith("create table HFJ_BLK_EXPORT_COLFILE ");
		assertThat(statements.get(11)).doesNotContain("PARTITION");
	}

	private static void logSql(String actual) {
		ourLog.info("Output SQL:\n{}", actual);
	}

	@Test
	public void testStatsAreAdded() throws IOException, MojoExecutionException {
		// Setup
		String input = """
			    create table HFJ_SPIDX_COORDS (
			        SP_ID bigint not null,
			        PARTITION_ID integer not null,
			        primary key (SP_ID, PARTITION_ID)
			    );
			
				create table HFJ_BLK_EXPORT_COLFILE (
					PID bigint not null,
					RES_ID varchar(100) not null,
					primary key (PID)
				);
			""";
		try (FileWriter fw = new FileWriter(myInputFile)) {
			fw.write(input);
		}

		// Test
		myMojo.execute();

		// Verify
		String actual = FileUtil.loadFileAsString(myOutputFile);
		logSql(actual);

		assertThat(actual).startsWith("-- Create partitioned table HFJ_SPIDX_COORDS\ncreate table HFJ_SPIDX_COORDS (");
		assertThat(actual).contains("CREATE TABLE HFJ_SPIDX_COORDS_PART0 PARTITION OF HFJ_SPIDX_COORDS FOR VALUES WITH (MODULUS 5, REMAINDER 0);");
		assertThat(actual).contains("CREATE TABLE HFJ_SPIDX_COORDS_PART1 PARTITION OF HFJ_SPIDX_COORDS FOR VALUES WITH (MODULUS 5, REMAINDER 1);");
		assertThat(actual).contains("ALTER TABLE HFJ_SPIDX_COORDS_PART0 SET (AUTOVACUUM_VACUUM_SCALE_FACTOR = 0.01);");
		assertThat(actual).contains("ALTER TABLE HFJ_SPIDX_COORDS_PART1 SET (AUTOVACUUM_VACUUM_SCALE_FACTOR = 0.01);");
		assertThat(actual).contains("ALTER TABLE HFJ_SPIDX_COORDS_PART0 ALTER COLUMN HASH_IDENTITY SET STATISTICS 1000;");
		assertThat(actual).contains("ALTER TABLE HFJ_SPIDX_COORDS_PART1 ALTER COLUMN HASH_IDENTITY SET STATISTICS 1000;");

	}
}
