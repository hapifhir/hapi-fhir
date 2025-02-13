package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import ca.uhn.fhir.jpa.migrate.taskdef.containertests.BaseMigrationTaskTestSuite;
import ca.uhn.fhir.jpa.migrate.tasks.api.Builder;
import org.assertj.core.api.Assertions;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration tests for PopulateSearchParameterIdentityTableTask.
 */
public interface PopulateSearchParameterIdentityTableTaskITTestSuite extends BaseMigrationTaskTestSuite {

	@ParameterizedTest
	@EnumSource(SearchParameterTableName.class)
	default void testPopulateSearchParameterIdentityTableTask_addsGenerator(SearchParameterTableName theSearchParameterTableName) throws SQLException {
		// setup
		Builder builder = getSupport().getBuilder();
		builder.addIdGenerator("1", "SEQ_SPIDX_IDENTITY", 1);

		Builder.BuilderAddTableByColumns spidxIdentity =
			builder.addTableByColumns("20250115.2", "HFJ_SPIDX_IDENTITY", "SP_IDENTITY_ID");
		spidxIdentity.addColumn("SP_IDENTITY_ID").nonNullable().type(ColumnTypeEnum.INT);
		spidxIdentity.addColumn("HASH_IDENTITY").nonNullable().type(ColumnTypeEnum.LONG);
		spidxIdentity.addColumn("RES_TYPE").nonNullable().type(ColumnTypeEnum.STRING, 100);
		spidxIdentity.addColumn("SP_NAME").nonNullable().type(ColumnTypeEnum.STRING, 100);


		Builder.BuilderAddTableByColumns tableBuilder = builder.addTableByColumns("2", theSearchParameterTableName.name(), "SP_ID");
		tableBuilder.addColumn("SP_ID").nonNullable().type(ColumnTypeEnum.LONG);
		tableBuilder.addColumn("HASH_IDENTITY").nullable().type(ColumnTypeEnum.LONG);
		tableBuilder.addColumn("SP_NAME").nullable().type(ColumnTypeEnum.STRING, 100);
		tableBuilder.addColumn("RES_TYPE").nullable().type(ColumnTypeEnum.STRING, 100);
		getSupport().executeAndClearPendingTasks();
		Assertions.assertThat(JdbcUtils.getTableNames(getSupport().getConnectionProperties()))
			.contains(theSearchParameterTableName.name());

		@Language("SQL") String insertSql = String.format("insert into %s (SP_ID, HASH_IDENTITY, SP_NAME, RES_TYPE) " +
			"VALUES (1, 1234, 'status', 'Device')", theSearchParameterTableName.name());
		JdbcUtils.executeSql(getSupport().getConnectionProperties(), insertSql);

		insertSql = String.format("insert into %s (SP_ID, HASH_IDENTITY, SP_NAME, RES_TYPE) " +
			"VALUES (2, 4321, 'phone', 'Patient')", theSearchParameterTableName.name());
		JdbcUtils.executeSql(getSupport().getConnectionProperties(), insertSql);

		builder.populateSearchParamIdentityTable("3", theSearchParameterTableName);
		getSupport().executeAndClearPendingTasks();

		// verify
		List<Map<String, Object>> output = JdbcUtils.executeQuery(getSupport().getConnectionProperties(),"SELECT * FROM HFJ_SPIDX_IDENTITY");
		assertEquals(2, output.size());
	}
}
