package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.embedded.PostgresEmbeddedDatabase;
import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.HapiMigrator;
import ca.uhn.fhir.jpa.migrate.SchemaMigrator;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

public class MigratePostgresOidToBinaryTaskTest extends BaseTest{

	private static PostgresEmbeddedDatabase ourPostgresEmbeddedDatabase;

	private Supplier<TestDatabaseDetails> myTestDatabaseDetailsSupplier;

	@BeforeAll
	public static void beforeAll(){
		ourPostgresEmbeddedDatabase = new PostgresEmbeddedDatabase();
	}

	@BeforeEach
	public void beforeEach(){
		DataSource dataSource = ourPostgresEmbeddedDatabase.getDataSource();
		DriverTypeEnum.ConnectionProperties connectionProperties = ourPostgresEmbeddedDatabase.getConnectionProperties();
		String url = ourPostgresEmbeddedDatabase.getUrl();
		HapiMigrator migrator = new HapiMigrator(SchemaMigrator.HAPI_FHIR_MIGRATION_TABLENAME, dataSource, DriverTypeEnum.POSTGRES_9_4);
		myTestDatabaseDetailsSupplier = () -> new TestDatabaseDetails(url, connectionProperties, (BasicDataSource) dataSource, migrator);
	}

	@Test
	public void testOidValueIsCopiedIntoBytea(){
		before(myTestDatabaseDetailsSupplier);

		executeSql("create table HFJ_BINARY_STORAGE_BLOB (BLOB_DATA oid, STORAGE_CONTENT_BIN bytea)");
		executeSql("select lo_put(1234, 0, '\\x48656c6c6f20776f726c64210a')");
		executeSql("insert into HFJ_BINARY_STORAGE_BLOB (BLOB_DATA) values (1234)");

		List<Map<String, Object>> rows = executeQuery("select * from HFJ_BINARY_STORAGE_BLOB");

		assertThat(rows, hasSize(1));

	}

	@AfterEach
	public void afterEach(){
		ourLog.info("Clearing PostgreSql database");
		ourPostgresEmbeddedDatabase.clearDatabase();
	}

	@AfterAll
	public static void afterAll(){
		ourPostgresEmbeddedDatabase.stop();

	}

}
