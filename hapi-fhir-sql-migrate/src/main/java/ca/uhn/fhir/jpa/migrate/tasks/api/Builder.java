package ca.uhn.fhir.jpa.migrate.tasks.api;

/*-
 * #%L
 * HAPI FHIR Server - SQL Migration
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.taskdef.AddColumnTask;
import ca.uhn.fhir.jpa.migrate.taskdef.AddForeignKeyTask;
import ca.uhn.fhir.jpa.migrate.taskdef.AddIdGeneratorTask;
import ca.uhn.fhir.jpa.migrate.taskdef.AddIndexTask;
import ca.uhn.fhir.jpa.migrate.taskdef.AddTableByColumnTask;
import ca.uhn.fhir.jpa.migrate.taskdef.AddTableRawSqlTask;
import ca.uhn.fhir.jpa.migrate.taskdef.BaseTableTask;
import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;
import ca.uhn.fhir.jpa.migrate.taskdef.ColumnTypeEnum;
import ca.uhn.fhir.jpa.migrate.taskdef.DropColumnTask;
import ca.uhn.fhir.jpa.migrate.taskdef.DropForeignKeyTask;
import ca.uhn.fhir.jpa.migrate.taskdef.DropIdGeneratorTask;
import ca.uhn.fhir.jpa.migrate.taskdef.DropIndexTask;
import ca.uhn.fhir.jpa.migrate.taskdef.DropTableTask;
import ca.uhn.fhir.jpa.migrate.taskdef.ExecuteRawSqlTask;
import ca.uhn.fhir.jpa.migrate.taskdef.InitializeSchemaTask;
import ca.uhn.fhir.jpa.migrate.taskdef.MigratePostgresTextClobToBinaryClobTask;
import ca.uhn.fhir.jpa.migrate.taskdef.ModifyColumnTask;
import ca.uhn.fhir.jpa.migrate.taskdef.NopTask;
import ca.uhn.fhir.jpa.migrate.taskdef.RenameColumnTask;
import ca.uhn.fhir.jpa.migrate.taskdef.RenameIndexTask;
import org.apache.commons.lang3.Validate;
import org.intellij.lang.annotations.Language;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Builder {

	private final String myRelease;
	private final BaseMigrationTasks.IAcceptsTasks mySink;

	public Builder(String theRelease, BaseMigrationTasks.IAcceptsTasks theSink) {
		myRelease = theRelease;
		mySink = theSink;
	}

	public BuilderWithTableName onTable(String theTableName) {
		return new BuilderWithTableName(myRelease, mySink, theTableName);
	}

	public void addTask(BaseTask theTask) {
		mySink.addTask(theTask);
	}

	public BuilderAddTableRawSql addTableRawSql(String theVersion, String theTableName) {
		return new BuilderAddTableRawSql(theVersion, theTableName);
	}

	public BuilderCompleteTask executeRawSql(String theVersion, @Language("SQL") String theSql) {
		ExecuteRawSqlTask task = executeRawSqlOptional(false, theVersion, theSql);
		return new BuilderCompleteTask(task);
	}

	public void executeRawSqlStub(String theVersion, @Language("SQL") String theSql) {
		executeRawSqlOptional(true, theVersion, theSql);
	}

	private ExecuteRawSqlTask executeRawSqlOptional(boolean theDoNothing, String theVersion, @Language("SQL") String theSql) {
		ExecuteRawSqlTask task = new ExecuteRawSqlTask(myRelease, theVersion).addSql(theSql);
		task.setDoNothing(theDoNothing);
		mySink.addTask(task);
		return task;
	}

	public Builder initializeSchema(String theVersion, ISchemaInitializationProvider theSchemaInitializationProvider) {
		mySink.addTask(new InitializeSchemaTask(myRelease, theVersion, theSchemaInitializationProvider));
		return this;
	}

	@SuppressWarnings("unused")
	public Builder initializeSchema(String theVersion, String theSchemaName, ISchemaInitializationProvider theSchemaInitializationProvider) {
		InitializeSchemaTask task = new InitializeSchemaTask(myRelease, theVersion, theSchemaInitializationProvider);
		task.setDescription("Initialize " + theSchemaName + " schema");
		mySink.addTask(task);
		return this;
	}

	public Builder executeRawSql(String theVersion, DriverTypeEnum theDriver, @Language("SQL") String theSql) {
		mySink.addTask(new ExecuteRawSqlTask(myRelease, theVersion).addSql(theDriver, theSql));
		return this;
	}

	/**
	 * Builder method to define a raw SQL execution migration that needs to take place against multiple database types,
	 * and the SQL they need to use is not equal. Provide a map of driver types to SQL statements.
	 *
	 * @param theVersion The version of the migration.
	 * @param theDriverToSql Map of driver types to SQL statements.
	 */
	public Builder executeRawSql(String theVersion, Map<DriverTypeEnum, String> theDriverToSql) {
		Map<DriverTypeEnum, List<String>> singleSqlStatementMap = new HashMap<>();
		theDriverToSql.entrySet().stream()
			.forEach(entry -> {
					singleSqlStatementMap.put(entry.getKey(), Collections.singletonList(entry.getValue()));
				});
		return executeRawSqls(theVersion, singleSqlStatementMap);
	}

	/**
	 * Builder method to define a raw SQL execution migration that needs to take place against multiple database types,
	 * and the SQL they need to use is not equal, and there are multiple sql commands for a given database.
	 * Provide a map of driver types to list of SQL statements.
	 *
	 * @param theVersion The version of the migration.
	 * @param theDriverToSqls Map of driver types to list of SQL statements.
	 */
	public Builder executeRawSqls(String theVersion, Map<DriverTypeEnum, List<String>> theDriverToSqls) {
		ExecuteRawSqlTask executeRawSqlTask = new ExecuteRawSqlTask(myRelease, theVersion);
		theDriverToSqls.entrySet().stream()
			.forEach(entry -> {
				entry.getValue().forEach(sql -> executeRawSqlTask.addSql(entry.getKey(), sql));
			});
		mySink.addTask(executeRawSqlTask);
		return this;
	}

	// Flyway doesn't support these kinds of migrations
	@Deprecated
	public Builder startSectionWithMessage(String theMessage) {
		// Do nothing
		return this;
	}

	public BuilderAddTableByColumns addTableByColumns(String theVersion, String theTableName, String... thePkColumnNames) {
		return new BuilderAddTableByColumns(myRelease, theVersion, mySink, theTableName, Arrays.asList(thePkColumnNames));
	}

	public void addIdGenerator(String theVersion, String theGeneratorName) {
		AddIdGeneratorTask task = new AddIdGeneratorTask(myRelease, theVersion, theGeneratorName);
		addTask(task);
	}

	public void dropIdGenerator(String theVersion, String theIdGeneratorName) {
		DropIdGeneratorTask task = new DropIdGeneratorTask(myRelease, theVersion, theIdGeneratorName);
		addTask(task);
	}

	public void addNop(String theVersion) {
		addTask(new NopTask(myRelease, theVersion));
	}

	public static class BuilderWithTableName implements BaseMigrationTasks.IAcceptsTasks {
		private final String myRelease;
		private final BaseMigrationTasks.IAcceptsTasks mySink;
		private final String myTableName;

		public BuilderWithTableName(String theRelease, BaseMigrationTasks.IAcceptsTasks theSink, String theTableName) {
			myRelease = theRelease;
			mySink = theSink;
			myTableName = theTableName;
		}

		public String getTableName() {
			return myTableName;
		}

		public BuilderCompleteTask dropIndex(String theVersion, String theIndexName) {
			BaseTask task = dropIndexOptional(false, theVersion, theIndexName);
			return new BuilderCompleteTask(task);
		}

		/**
		 * Drop index without taking write lock on PG, Oracle, MSSQL.
		 */
		public BuilderCompleteTask dropIndexOnline(String theVersion, String theIndexName) {
			DropIndexTask task = dropIndexOptional(false, theVersion, theIndexName);
			task.setOnline(true);
			return new BuilderCompleteTask(task);
		}

		public void dropIndexStub(String theVersion, String theIndexName) {
			dropIndexOptional(true, theVersion, theIndexName);
		}

		private DropIndexTask dropIndexOptional(boolean theDoNothing, String theVersion, String theIndexName) {
			DropIndexTask task = new DropIndexTask(myRelease, theVersion);
			task.setIndexName(theIndexName);
			task.setTableName(myTableName);
			task.setDoNothing(theDoNothing);
			addTask(task);
			return task;
		}

		/**
		 * @deprecated Do not rename indexes - It is too hard to figure out what happened if something goes wrong
		 */
		@Deprecated
		public void renameIndex(String theVersion, String theOldIndexName, String theNewIndexName) {
			renameIndexOptional(false, theVersion, theOldIndexName, theNewIndexName);
		}

		/**
		 * @deprecated Do not rename indexes - It is too hard to figure out what happened if something goes wrong
		 */
		public void renameIndexStub(String theVersion, String theOldIndexName, String theNewIndexName) {
			renameIndexOptional(true, theVersion, theOldIndexName, theNewIndexName);
		}

		private void renameIndexOptional(boolean theDoNothing, String theVersion, String theOldIndexName, String theNewIndexName) {
			RenameIndexTask task = new RenameIndexTask(myRelease, theVersion);
			task.setOldIndexName(theOldIndexName);
			task.setNewIndexName(theNewIndexName);
			task.setTableName(myTableName);
			task.setDoNothing(theDoNothing);
			addTask(task);
		}

		public void dropThisTable(String theVersion) {
			DropTableTask task = new DropTableTask(myRelease, theVersion);
			task.setTableName(myTableName);
			addTask(task);
		}

		public BuilderWithTableName.BuilderAddIndexWithName addIndex(String theVersion, String theIndexName) {
			return new BuilderWithTableName.BuilderAddIndexWithName(theVersion, theIndexName);
		}

		public BuilderWithTableName.BuilderAddColumnWithName addColumn(String theVersion, String theColumnName) {
			return new BuilderWithTableName.BuilderAddColumnWithName(myRelease, theVersion, theColumnName, this);
		}

		public BuilderCompleteTask dropColumn(String theVersion, String theColumnName) {
			Validate.notBlank(theColumnName);
			DropColumnTask task = new DropColumnTask(myRelease, theVersion);
			task.setTableName(myTableName);
			task.setColumnName(theColumnName);
			addTask(task);
			return new BuilderCompleteTask(task);
		}

		@Override
		public void addTask(BaseTask theTask) {
			((BaseTableTask) theTask).setTableName(myTableName);
			mySink.addTask(theTask);
		}

		public BuilderWithTableName.BuilderModifyColumnWithName modifyColumn(String theVersion, String theColumnName) {
			return new BuilderWithTableName.BuilderModifyColumnWithName(theVersion, theColumnName);
		}

		public BuilderWithTableName.BuilderAddForeignKey addForeignKey(String theVersion, String theForeignKeyName) {
			return new BuilderWithTableName.BuilderAddForeignKey(theVersion, theForeignKeyName);
		}

		public BuilderWithTableName renameColumn(String theVersion, String theOldName, String theNewName) {
			return renameColumn(theVersion, theOldName, theNewName, false, false);
		}

		/**
		 * @param theOldName                            The old column name
		 * @param theNewName                            The new column name
		 * @param isOkayIfNeitherColumnExists           Setting this to true means that it's not an error if neither column exists
		 * @param theDeleteTargetColumnFirstIfBothExist Setting this to true causes the migrator to be ok with the target column existing. It will make sure that there is no data in the column with the new name, then delete it if so in order to make room for the renamed column. If there is data it will still bomb out.
		 */
		public BuilderWithTableName renameColumn(String theVersion, String theOldName, String theNewName, boolean isOkayIfNeitherColumnExists, boolean theDeleteTargetColumnFirstIfBothExist) {
			RenameColumnTask task = new RenameColumnTask(myRelease, theVersion);
			task.setTableName(myTableName);
			task.setOldName(theOldName);
			task.setNewName(theNewName);
			task.setOkayIfNeitherColumnExists(isOkayIfNeitherColumnExists);
			task.setDeleteTargetColumnFirstIfBothExist(theDeleteTargetColumnFirstIfBothExist);
			addTask(task);
			return this;
		}

		/**
		 * @param theFkName          the name of the foreign key
		 * @param theParentTableName the name of the table that exports the foreign key
		 */
		public void dropForeignKey(String theVersion, String theFkName, String theParentTableName) {
			DropForeignKeyTask task = new DropForeignKeyTask(myRelease, theVersion);
			task.setConstraintName(theFkName);
			task.setTableName(getTableName());
			task.setParentTableName(theParentTableName);
			addTask(task);
		}

		public void migratePostgresTextClobToBinaryClob(String theVersion, String theColumnName) {
			MigratePostgresTextClobToBinaryClobTask task = new MigratePostgresTextClobToBinaryClobTask(myRelease, theVersion);
			task.setTableName(getTableName());
			task.setColumnName(theColumnName);
			addTask(task);
		}

		public class BuilderAddIndexWithName {
			private final String myVersion;
			private final String myIndexName;

			public BuilderAddIndexWithName(String theVersion, String theIndexName) {
				myVersion = theVersion;
				myIndexName = theIndexName;
			}

			public BuilderWithTableName.BuilderAddIndexWithName.BuilderAddIndexUnique unique(boolean theUnique) {
				return new BuilderWithTableName.BuilderAddIndexWithName.BuilderAddIndexUnique(myVersion, theUnique);
			}

			public class BuilderAddIndexUnique {
				private final String myVersion;
				private final boolean myUnique;
				private String[] myIncludeColumns;
				private boolean myOnline;

				public BuilderAddIndexUnique(String theVersion, boolean theUnique) {
					myVersion = theVersion;
					myUnique = theUnique;
				}

				public void withColumnsStub(String... theColumnNames) {
					withColumnsOptional(true, theColumnNames);
				}

				public BuilderCompleteTask withColumns(String... theColumnNames) {
					BaseTask task = withColumnsOptional(false, theColumnNames);
					return new BuilderCompleteTask(task);
				}

				private AddIndexTask withColumnsOptional(boolean theDoNothing, String... theColumnNames) {
					AddIndexTask task = new AddIndexTask(myRelease, myVersion);
					task.setTableName(myTableName);
					task.setIndexName(myIndexName);
					task.setUnique(myUnique);
					task.setColumns(theColumnNames);
					task.setDoNothing(theDoNothing);
					task.setOnline(myOnline);
					if (myIncludeColumns != null) {
						task.setIncludeColumns(myIncludeColumns);
					}
					addTask(task);
					return task;
				}

				public BuilderAddIndexUnique includeColumns(String... theIncludeColumns) {
					myIncludeColumns = theIncludeColumns;
					return this;
				}

				/**
				 * Add the index without locking the table.
				 */
				public BuilderAddIndexUnique online(boolean theOnlineFlag) {
					myOnline = theOnlineFlag;
					return this;
				}
			}
		}

		public class BuilderModifyColumnWithName {
			private final String myVersion;
			private final String myColumnName;

			public BuilderModifyColumnWithName(String theVersion, String theColumnName) {
				myVersion = theVersion;
				myColumnName = theColumnName;
			}

			public String getColumnName() {
				return myColumnName;
			}

			public BuilderWithTableName.BuilderModifyColumnWithName.BuilderModifyColumnWithNameAndNullable nullable() {
				return new BuilderWithTableName.BuilderModifyColumnWithName.BuilderModifyColumnWithNameAndNullable(myVersion, true);
			}

			public BuilderWithTableName.BuilderModifyColumnWithName.BuilderModifyColumnWithNameAndNullable nonNullable() {
				return new BuilderWithTableName.BuilderModifyColumnWithName.BuilderModifyColumnWithNameAndNullable(myVersion, false);
			}

			public class BuilderModifyColumnWithNameAndNullable {
				private final String myVersion;
				private final boolean myNullable;
				private boolean myFailureAllowed;

				public BuilderModifyColumnWithNameAndNullable(String theVersion, boolean theNullable) {
					myVersion = theVersion;
					myNullable = theNullable;
				}

				public void withType(ColumnTypeEnum theColumnType) {
					withType(theColumnType, null);
				}

				public void withType(ColumnTypeEnum theColumnType, Integer theLength) {
					if (theColumnType == ColumnTypeEnum.STRING) {
						if (theLength == null || theLength == 0) {
							throw new IllegalArgumentException(Msg.code(52) + "Can not specify length 0 for column of type " + theColumnType);
						}
					} else {
						if (theLength != null) {
							throw new IllegalArgumentException(Msg.code(53) + "Can not specify length for column of type " + theColumnType);
						}
					}

					ModifyColumnTask task = new ModifyColumnTask(myRelease, myVersion);
					task.setColumnName(myColumnName);
					task.setTableName(myTableName);
					if (theLength != null) {
						task.setColumnLength(theLength);
					}
					task.setNullable(myNullable);
					task.setColumnType(theColumnType);
					task.setFailureAllowed(myFailureAllowed);
					addTask(task);
				}

				public BuilderModifyColumnWithNameAndNullable failureAllowed() {
					myFailureAllowed = true;
					return this;
				}
			}
		}

		public class BuilderAddForeignKey {
			private final String myVersion;
			private final String myForeignKeyName;

			public BuilderAddForeignKey(String theVersion, String theForeignKeyName) {
				myVersion = theVersion;
				myForeignKeyName = theForeignKeyName;
			}

			public BuilderWithTableName.BuilderAddForeignKey.BuilderAddForeignKeyToColumn toColumn(String theColumnName) {
				return new BuilderWithTableName.BuilderAddForeignKey.BuilderAddForeignKeyToColumn(myVersion, theColumnName);
			}

			public class BuilderAddForeignKeyToColumn extends BuilderWithTableName.BuilderModifyColumnWithName {
				public BuilderAddForeignKeyToColumn(String theVersion, String theColumnName) {
					super(theVersion, theColumnName);
				}

				public BuilderCompleteTask references(String theForeignTable, String theForeignColumn) {
					AddForeignKeyTask task = new AddForeignKeyTask(myRelease, myVersion);
					task.setTableName(myTableName);
					task.setConstraintName(myForeignKeyName);
					task.setColumnName(getColumnName());
					task.setForeignTableName(theForeignTable);
					task.setForeignColumnName(theForeignColumn);
					addTask(task);
					return new BuilderCompleteTask(task);
				}
			}
		}

		public static class BuilderAddColumnWithName {
			private final String myRelease;
			private final String myVersion;
			private final String myColumnName;
			private final BaseMigrationTasks.IAcceptsTasks myTaskSink;

			public BuilderAddColumnWithName(String theRelease, String theVersion, String theColumnName, BaseMigrationTasks.IAcceptsTasks theTaskSink) {
				myRelease = theRelease;
				myVersion = theVersion;
				myColumnName = theColumnName;
				myTaskSink = theTaskSink;
			}

			public BuilderWithTableName.BuilderAddColumnWithName.BuilderAddColumnWithNameNullable nullable() {
				return new BuilderWithTableName.BuilderAddColumnWithName.BuilderAddColumnWithNameNullable(myRelease, myVersion, true);
			}

			public BuilderWithTableName.BuilderAddColumnWithName.BuilderAddColumnWithNameNullable nonNullable() {
				return new BuilderWithTableName.BuilderAddColumnWithName.BuilderAddColumnWithNameNullable(myRelease, myVersion, false);
			}

			public class BuilderAddColumnWithNameNullable {
				private final boolean myNullable;
				private final String myRelease;
				private final String myVersion;

				public BuilderAddColumnWithNameNullable(String theRelease, String theVersion, boolean theNullable) {
					myRelease = theRelease;
					myVersion = theVersion;
					myNullable = theNullable;
				}

				public BuilderCompleteTask type(ColumnTypeEnum theColumnType) {
					return type(theColumnType, null);
				}

				public BuilderCompleteTask type(ColumnTypeEnum theColumnType, Integer theLength) {
					AddColumnTask task = new AddColumnTask(myRelease, myVersion);
					task.setColumnName(myColumnName);
					task.setNullable(myNullable);
					task.setColumnType(theColumnType);
					if (theLength != null) {
						task.setColumnLength(theLength);
					}
					myTaskSink.addTask(task);

					return new BuilderCompleteTask(task);
				}

			}
		}
	}

	public static class BuilderCompleteTask {

		private final BaseTask myTask;

		public BuilderCompleteTask(BaseTask theTask) {
			myTask = theTask;
		}

		public BuilderCompleteTask failureAllowed() {
			myTask.setFailureAllowed(true);
			return this;
		}

		public BuilderCompleteTask doNothing() {
			myTask.setDoNothing(true);
			return this;
		}

		public BuilderCompleteTask onlyAppliesToPlatforms(DriverTypeEnum... theTypes) {
			Set<DriverTypeEnum> typesSet = Arrays.stream(theTypes).collect(Collectors.toSet());
			myTask.setOnlyAppliesToPlatforms(typesSet);
			return this;
		}

		public BuilderCompleteTask runEvenDuringSchemaInitialization() {
			myTask.setRunDuringSchemaInitialization(true);
			return this;
		}
	}

	public class BuilderAddTableRawSql {

		private final AddTableRawSqlTask myTask;

		protected BuilderAddTableRawSql(String theVersion, String theTableName) {
			myTask = new AddTableRawSqlTask(myRelease, theVersion);
			myTask.setTableName(theTableName);
			addTask(myTask);
		}


		public BuilderAddTableRawSql addSql(DriverTypeEnum theDriverTypeEnum, @Language("SQL") String theSql) {
			myTask.addSql(theDriverTypeEnum, theSql);
			return this;
		}

		public void addSql(@Language("SQL") String theSql) {
			myTask.addSql(theSql);
		}
	}

	public class BuilderAddTableByColumns extends BuilderWithTableName implements BaseMigrationTasks.IAcceptsTasks {
		private final String myVersion;
		private final AddTableByColumnTask myTask;

		public BuilderAddTableByColumns(String theRelease, String theVersion, BaseMigrationTasks.IAcceptsTasks theSink, String theTableName, List<String> thePkColumnNames) {
			super(theRelease, theSink, theTableName);
			myVersion = theVersion;
			myTask = new AddTableByColumnTask(myRelease, theVersion);
			myTask.setTableName(theTableName);
			myTask.setPkColumns(thePkColumnNames);
			theSink.addTask(myTask);
		}

		public BuilderAddColumnWithName addColumn(String theColumnName) {
			return new BuilderAddColumnWithName(myRelease, myVersion, theColumnName, this);
		}

		@Override
		public void addTask(BaseTask theTask) {
			if (theTask instanceof AddColumnTask) {
				myTask.addAddColumnTask((AddColumnTask) theTask);
			} else {
				super.addTask(theTask);
			}
		}

		public BuilderAddTableByColumns failureAllowed() {
			myTask.setFailureAllowed(true);
			return this;
		}
	}

}
