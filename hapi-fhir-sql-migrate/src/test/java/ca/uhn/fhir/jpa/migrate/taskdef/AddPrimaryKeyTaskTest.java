package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Arrays;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

class AddPrimaryKeyTaskTest {

	@Nested
	public class EqualsAndHashCodeGeneration {
		AddPrimaryKeyTask myTask = makeTask();
		AddPrimaryKeyTask myComparisonTask = makeTask();

		@Test
		void allSame_isEqual() {
			assertEquals(myTask,myComparisonTask);
		}

		@Test
		public void differentColumns_unequal() {
			myComparisonTask.setColumns(asList("A","B","C"));
			assertNotEquals(myTask,myComparisonTask);
		}

		@Test
		public void differentName_unequal() {
			myComparisonTask.setConstraintName("Another_name");
			assertNotEquals(myTask,myComparisonTask);
		}

		@Test
		void allSame_sameHash() {
			assertEquals(myTask.hashCode(), myComparisonTask.hashCode());
			assertNotEquals(0, myTask.hashCode());
		}

		@Test
		public void differentColumns_differentHash() {
			myComparisonTask.setColumns(asList("A","B","C"));
			assertNotEquals(myTask.hashCode(), myComparisonTask.hashCode());
		}

		@Test
		public void differentName_differentHash() {
			myComparisonTask.setConstraintName("Another_name");
			assertNotEquals(myTask.hashCode(), myComparisonTask.hashCode());
		}


	}

	@ParameterizedTest(name = "{index}: {0}")
	@EnumSource()
	public void sqlGeneration_matches(DriverTypeEnum theDriver) {
		AddPrimaryKeyTask task = makeTask();
		task.setDriverType(theDriver);

		String expectedSql = switch (theDriver) {
			case POSTGRES_9_4, ORACLE_12C, MSSQL_2012, H2_EMBEDDED, DERBY_EMBEDDED, COCKROACHDB_21_1
				-> "ALTER TABLE HFJ_SEARCH_RESULT ADD CONSTRAINT HFJ_SEARCH_RESULT_PKEY PRIMARY KEY (PID)";
			case MYSQL_5_7, MARIADB_10_1 -> "ALTER TABLE HFJ_SEARCH_RESULT ADD CONSTRAINT `HFJ_SEARCH_RESULT_PKEY` PRIMARY KEY (PID)";
		};
		String sql = task.generateSql();

		assertEquals(expectedSql, sql);
	}


	AddPrimaryKeyTask makeTask() {
		return new AddPrimaryKeyTask("6.0.0", "123.1", "HFJ_SEARCH_RESULT", "HFJ_SEARCH_RESULT_PKEY")
			.setColumns(asList("PID"));
	}
}
