package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.util.VersionEnum;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculateHashesTest extends BaseTest {

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testCreateHashes(Supplier<TestDatabaseDetails> theTestDatabaseDetails) {
		before(theTestDatabaseDetails);

		executeSql("create table HFJ_SPIDX_TOKEN (SP_ID bigint not null, SP_MISSING boolean, SP_NAME varchar(100) not null, RES_ID bigint, RES_TYPE varchar(255) not null, SP_UPDATED timestamp, HASH_IDENTITY bigint, HASH_SYS bigint, HASH_SYS_AND_VALUE bigint, HASH_VALUE bigint, SP_SYSTEM varchar(200), SP_VALUE varchar(200), primary key (SP_ID))");
		executeSql("insert into HFJ_SPIDX_TOKEN (SP_MISSING, SP_NAME, RES_ID, RES_TYPE, SP_UPDATED, SP_SYSTEM, SP_VALUE, SP_ID) values (false, 'identifier', 999, 'Patient', '2018-09-03 07:44:49.196', 'urn:oid:1.2.410.100110.10.41308301', '88888888', 1)");
		executeSql("insert into HFJ_SPIDX_TOKEN (SP_MISSING, SP_NAME, RES_ID, RES_TYPE, SP_UPDATED, SP_SYSTEM, SP_VALUE, SP_ID) values (false, 'identifier', 999, 'Patient', '2018-09-03 07:44:49.196', 'urn:oid:1.2.410.100110.10.41308301', '99999999', 2)");

		CalculateHashesTask task = new CalculateHashesTask(VersionEnum.V3_5_0, "1");
		task.setTableName("HFJ_SPIDX_TOKEN");
		task.setColumnName("HASH_IDENTITY");
		task.addCalculator("HASH_IDENTITY", t -> BaseResourceIndexedSearchParam.calculateHashIdentity(new PartitionSettings(), RequestPartitionId.defaultPartition(), t.getResourceType(), t.getString("SP_NAME")));
		task.addCalculator("HASH_SYS", t -> ResourceIndexedSearchParamToken.calculateHashSystem(new PartitionSettings(), RequestPartitionId.defaultPartition(), t.getResourceType(), t.getParamName(), t.getString("SP_SYSTEM")));
		task.addCalculator("HASH_SYS_AND_VALUE", t -> ResourceIndexedSearchParamToken.calculateHashSystemAndValue(new PartitionSettings(), RequestPartitionId.defaultPartition(), t.getResourceType(), t.getParamName(), t.getString("SP_SYSTEM"), t.getString("SP_VALUE")));
		task.addCalculator("HASH_VALUE", t -> ResourceIndexedSearchParamToken.calculateHashValue(new PartitionSettings(), RequestPartitionId.defaultPartition(), t.getResourceType(), t.getParamName(), t.getString("SP_VALUE")));
		task.setBatchSize(1);
		getMigrator().addTask(task);

		getMigrator().migrate();


		getConnectionProperties().getTxTemplate().execute(t -> {
			Map<String, Object> map;
			JdbcTemplate jdbcTemplate = getConnectionProperties().newJdbcTemplate();

			map = jdbcTemplate.queryForMap("select * from HFJ_SPIDX_TOKEN where SP_ID = 1");
			assertEquals(7001889285610424179L, map.get("HASH_IDENTITY"));
			assertEquals(2686400398917843456L, map.get("HASH_SYS"));
			assertEquals(-3943098850992523411L, map.get("HASH_SYS_AND_VALUE"));
			assertEquals(845040519142030272L, map.get("HASH_VALUE"));

			map = jdbcTemplate.queryForMap("select * from HFJ_SPIDX_TOKEN where SP_ID = 2");
			assertEquals(7001889285610424179L, map.get("HASH_IDENTITY"));
			assertEquals(2686400398917843456L, map.get("HASH_SYS"));
			assertEquals(-6583685191951870327L, map.get("HASH_SYS_AND_VALUE"));
			assertEquals(8271382783311609619L, map.get("HASH_VALUE"));

			return null;
		});
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testCreateHashesLargeNumber(Supplier<TestDatabaseDetails> theTestDatabaseDetails) {
		before(theTestDatabaseDetails);

		executeSql("create table HFJ_SPIDX_TOKEN (SP_ID bigint not null, SP_MISSING boolean, SP_NAME varchar(100) not null, RES_ID bigint, RES_TYPE varchar(255) not null, SP_UPDATED timestamp, HASH_IDENTITY bigint, HASH_SYS bigint, HASH_SYS_AND_VALUE bigint, HASH_VALUE bigint, SP_SYSTEM varchar(200), SP_VALUE varchar(200), primary key (SP_ID))");

		for (int i = 0; i < 777; i++) {
			executeSql("insert into HFJ_SPIDX_TOKEN (SP_MISSING, SP_NAME, RES_ID, RES_TYPE, SP_UPDATED, SP_SYSTEM, SP_VALUE, SP_ID) values (false, 'identifier', 999, 'Patient', '2018-09-03 07:44:49.196', 'urn:oid:1.2.410.100110.10.41308301', '8888888" + i + "', " + i + ")");
		}

		Long count = getConnectionProperties().getTxTemplate().execute(t -> {
			JdbcTemplate jdbcTemplate = getConnectionProperties().newJdbcTemplate();
			return jdbcTemplate.queryForObject("SELECT count(*) FROM HFJ_SPIDX_TOKEN WHERE HASH_VALUE IS NULL", Long.class);
		});
		assertEquals(777L, count.longValue());

		CalculateHashesTask task = new CalculateHashesTask(VersionEnum.V3_5_0, "1");
		task.setTableName("HFJ_SPIDX_TOKEN");
		task.setColumnName("HASH_IDENTITY");
		task.addCalculator("HASH_IDENTITY", t -> BaseResourceIndexedSearchParam.calculateHashIdentity(new PartitionSettings(), RequestPartitionId.defaultPartition(), t.getResourceType(), t.getString("SP_NAME")));
		task.addCalculator("HASH_SYS", t -> ResourceIndexedSearchParamToken.calculateHashSystem(new PartitionSettings(), RequestPartitionId.defaultPartition(), t.getResourceType(), t.getParamName(), t.getString("SP_SYSTEM")));
		task.addCalculator("HASH_SYS_AND_VALUE", t -> ResourceIndexedSearchParamToken.calculateHashSystemAndValue(new PartitionSettings(), RequestPartitionId.defaultPartition(), t.getResourceType(), t.getParamName(), t.getString("SP_SYSTEM"), t.getString("SP_VALUE")));
		task.addCalculator("HASH_VALUE", t -> ResourceIndexedSearchParamToken.calculateHashValue(new PartitionSettings(), RequestPartitionId.defaultPartition(), t.getResourceType(), t.getParamName(), t.getString("SP_VALUE")));
		task.setBatchSize(3);
		getMigrator().addTask(task);

		getMigrator().migrate();

		count = getConnectionProperties().getTxTemplate().execute(t -> {
			JdbcTemplate jdbcTemplate = getConnectionProperties().newJdbcTemplate();
			return jdbcTemplate.queryForObject("SELECT count(*) FROM HFJ_SPIDX_TOKEN WHERE HASH_VALUE IS NULL", Long.class);
		});
		assertEquals(0L, count.longValue());
	}
}
