package ca.uhn.fhir.jpa.migrate.tasks.api;

/*-
 * #%L
 * HAPI FHIR JPA Server - Migration
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.taskdef.*;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.Validate;
import org.intellij.lang.annotations.Language;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BaseMigrationTasks<T extends Enum> {
	private Multimap<T, BaseTask<?>> myTasks = MultimapBuilder.hashKeys().arrayListValues().build();

	@SuppressWarnings("unchecked")
	public List<BaseTask<?>> getTasks(@Nonnull T theFrom, @Nonnull T theTo) {
		Validate.notNull(theFrom);
		Validate.notNull(theTo);
		Validate.isTrue(theFrom.ordinal() < theTo.ordinal(), "From version must be lower than to version");

		List<BaseTask<?>> retVal = new ArrayList<>();
		for (Object nextVersion : EnumUtils.getEnumList(theFrom.getClass())) {
			if (((T) nextVersion).ordinal() <= theFrom.ordinal()) {
				continue;
			}
			if (((T) nextVersion).ordinal() > theTo.ordinal()) {
				continue;
			}

			Collection<BaseTask<?>> nextValues = myTasks.get((T) nextVersion);
			if (nextValues != null) {
				retVal.addAll(nextValues);
			}
		}

		return retVal;
	}

	public Builder forVersion(T theRelease) {
		IAcceptsTasks sink = theTask -> {
			theTask.validate();
			myTasks.put(theRelease, theTask);
		};
		return new Builder(theRelease.name(), sink);
	}

	public interface IAcceptsTasks {
		void addTask(BaseTask<?> theTask);
	}

	public static class Builder {

		private final String myRelease;
		private final IAcceptsTasks mySink;

		public Builder(String theRelease, IAcceptsTasks theSink) {
			myRelease = theRelease;
			mySink = theSink;
		}

		public BuilderWithTableName onTable(String theTableName) {
			return new BuilderWithTableName(myRelease, mySink, theTableName);
		}

		public void addTask(BaseTask<?> theTask) {
			mySink.addTask(theTask);
		}

		public BuilderAddTableRawSql addTableRawSql(String theVersion, String theTableName) {
			return new BuilderAddTableRawSql(theVersion, theTableName);
		}

		public Builder executeRawSql(String theVersion, @Language("SQL") String theSql) {
			mySink.addTask(new ExecuteRawSqlTask(myRelease, theVersion).addSql(theSql));
			return this;
		}

		public Builder executeRawSql(String theVersion, DriverTypeEnum theDriver, @Language("SQL") String theSql) {
			mySink.addTask(new ExecuteRawSqlTask(myRelease, theVersion).addSql(theDriver, theSql));
			return this;
		}

		public Builder startSectionWithMessage(String theMessage) {
			Validate.notBlank(theMessage);
			addTask(new LogStartSectionWithMessageTask(myRelease, "log message", theMessage));
			return this;
		}

		public BuilderAddTableByColumns addTableByColumns(String theVersion, String theTableName, String thePkColumnName) {
			return new BuilderAddTableByColumns(myRelease, theVersion, mySink, theTableName, thePkColumnName);
		}

		public void addIdGenerator(String theVersion, String theGeneratorName) {
			AddIdGeneratorTask task = new AddIdGeneratorTask(myRelease, theVersion, theGeneratorName);
			addTask(task);
		}

		public void dropIdGenerator(String theVersion, String theIdGeneratorName) {
			DropIdGeneratorTask task = new DropIdGeneratorTask(myRelease, theVersion, theIdGeneratorName);
			addTask(task);
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

		public class BuilderAddTableByColumns extends BuilderWithTableName implements IAcceptsTasks {
			private final String myVersion;
			private final AddTableByColumnTask myTask;

			public BuilderAddTableByColumns(String theRelease, String theVersion, IAcceptsTasks theSink, String theTableName, String thePkColumnName) {
				super(theRelease, theSink, theTableName);
				myVersion = theVersion;
				myTask = new AddTableByColumnTask(myRelease, theVersion);
				myTask.setTableName(theTableName);
				myTask.setPkColumn(thePkColumnName);
				theSink.addTask(myTask);
			}

			public BuilderWithTableName.BuilderAddColumnWithName addColumn(String theColumnName) {
				return new BuilderWithTableName.BuilderAddColumnWithName(myRelease, myVersion, theColumnName, this);
			}

			@Override
			public void addTask(BaseTask<?> theTask) {
				if (theTask instanceof AddColumnTask) {
					myTask.addAddColumnTask((AddColumnTask) theTask);
				} else {
					super.addTask(theTask);
				}
			}
		}

		public static class BuilderWithTableName implements IAcceptsTasks {
			private final String myRelease;
			private final IAcceptsTasks mySink;
			private final String myTableName;

			public BuilderWithTableName(String theRelease, IAcceptsTasks theSink, String theTableName) {
				myRelease = theRelease;
				mySink = theSink;
				myTableName = theTableName;
			}

			public String getTableName() {
				return myTableName;
			}

			public void dropIndex(String theVersion, String theIndexName) {
				DropIndexTask task = new DropIndexTask(myRelease, theVersion);
				task.setIndexName(theIndexName);
				task.setTableName(myTableName);
				addTask(task);
			}

			public void dropThisTable(String theVersion) {
				DropTableTask task = new DropTableTask(myRelease, theVersion);
				task.setTableName(myTableName);
				addTask(task);
			}

			public BuilderAddIndexWithName addIndex(String theIndexName) {
				return new BuilderAddIndexWithName(theIndexName);
			}

			public BuilderAddColumnWithName addColumn(String theVersion, String theColumnName) {
				return new BuilderAddColumnWithName(myRelease, theVersion, theColumnName, this);
			}

			public void dropColumn(String theVersion, String theColumnName) {
				Validate.notBlank(theColumnName);
				DropColumnTask task = new DropColumnTask(myRelease, theVersion);
				task.setTableName(myTableName);
				task.setColumnName(theColumnName);
				addTask(task);
			}

			@Override
			public void addTask(BaseTask<?> theTask) {
				((BaseTableTask<?>) theTask).setTableName(myTableName);
				mySink.addTask(theTask);
			}

			public BuilderModifyColumnWithName modifyColumn(String theColumnName) {
				return new BuilderModifyColumnWithName(theColumnName);
			}

			public BuilderAddForeignKey addForeignKey(String theForeignKeyName) {
				return new BuilderAddForeignKey(theForeignKeyName);
			}

			public BuilderWithTableName renameColumn(String theVersion, String theOldName, String theNewName) {
				return renameColumn(theVersion, theOldName, theNewName, false, false);
			}

			/**
			 * @param theOldName                            The old column name
			 * @param theNewName                            The new column name
			 * @param theAllowNeitherColumnToExist          Setting this to true means that it's not an error if neither column exists
			 * @param theDeleteTargetColumnFirstIfBothEixst Setting this to true causes the migrator to be ok with the target column existing. It will make sure that there is no data in the column with the new name, then delete it if so in order to make room for the renamed column. If there is data it will still bomb out.
			 */
			public BuilderWithTableName renameColumn(String theVersion, String theOldName, String theNewName, boolean theAllowNeitherColumnToExist, boolean theDeleteTargetColumnFirstIfBothEixst) {
				RenameColumnTask task = new RenameColumnTask(myRelease, theVersion);
				task.setTableName(myTableName);
				task.setOldName(theOldName);
				task.setNewName(theNewName);
				task.setAllowNeitherColumnToExist(theAllowNeitherColumnToExist);
				task.setDeleteTargetColumnFirstIfBothExist(theDeleteTargetColumnFirstIfBothEixst);
				addTask(task);
				return this;
			}

			/**
			 *
			 * @param theFkName the name of the foreign key
			 * @param theParentTableName the name of the table that exports the foreign key
			 */
			public void dropForeignKey(String theVersion, String theFkName, String theParentTableName) {
				DropForeignKeyTask task = new DropForeignKeyTask(myRelease, theVersion);
				task.setConstraintName(theFkName);
				task.setTableName(getTableName());
				task.setParentTableName(theParentTableName);
				addTask(task);
			}

			public class BuilderAddIndexWithName {
				private final String myIndexName;

				public BuilderAddIndexWithName(String theIndexName) {
					myIndexName = theIndexName;
				}

				public BuilderAddIndexUnique unique(boolean theUnique) {
					return new BuilderAddIndexUnique(theUnique);
				}

				public class BuilderAddIndexUnique {
					private final boolean myUnique;

					public BuilderAddIndexUnique(boolean theUnique) {
						myUnique = theUnique;
					}

					public void withColumns(String theVersion, String... theColumnNames) {
						AddIndexTask task = new AddIndexTask(myRelease, theVersion);
						task.setTableName(myTableName);
						task.setIndexName(myIndexName);
						task.setUnique(myUnique);
						task.setColumns(theColumnNames);
						addTask(task);
					}
				}
			}

			public class BuilderModifyColumnWithName {

				private final String myColumnName;

				public BuilderModifyColumnWithName(String theColumnName) {
					myColumnName = theColumnName;
				}

				public String getColumnName() {
					return myColumnName;
				}

				public BuilderModifyColumnWithNameAndNullable nullable() {
					return new BuilderModifyColumnWithNameAndNullable(true);
				}

				public BuilderModifyColumnWithNameAndNullable nonNullable() {
					return new BuilderModifyColumnWithNameAndNullable(false);
				}

				public class BuilderModifyColumnWithNameAndNullable {

					private final boolean myNullable;

					public BuilderModifyColumnWithNameAndNullable(boolean theNullable) {
						myNullable = theNullable;
					}

					public void withType(String theVersion, BaseTableColumnTypeTask.ColumnTypeEnum theColumnType) {
						withType(theVersion, theColumnType, null);
					}

					public void withType(String theVersion, BaseTableColumnTypeTask.ColumnTypeEnum theColumnType, Integer theLength) {
						if (theColumnType == BaseTableColumnTypeTask.ColumnTypeEnum.STRING) {
							if (theLength == null || theLength == 0) {
								throw new IllegalArgumentException("Can not specify length 0 for column of type " + theColumnType);
							}
						} else {
							if (theLength != null) {
								throw new IllegalArgumentException("Can not specify length for column of type " + theColumnType);
							}
						}

						ModifyColumnTask task = new ModifyColumnTask(myRelease, theVersion);
						task.setColumnName(myColumnName);
						task.setTableName(myTableName);
						if (theLength != null) {
							task.setColumnLength(theLength);
						}
						task.setNullable(myNullable);
						task.setColumnType(theColumnType);
						addTask(task);
					}
				}
			}

			public class BuilderAddForeignKey {
				private final String myForeignKeyName;

				public BuilderAddForeignKey(String theForeignKeyName) {
					myForeignKeyName = theForeignKeyName;
				}

				public BuilderAddForeignKeyToColumn toColumn(String theColumnName) {
					return new BuilderAddForeignKeyToColumn(theColumnName);
				}

				public class BuilderAddForeignKeyToColumn extends BuilderModifyColumnWithName {
					public BuilderAddForeignKeyToColumn(String theColumnName) {
						super(theColumnName);
					}

					public void references(String theVersion, String theForeignTable, String theForeignColumn) {
						AddForeignKeyTask task = new AddForeignKeyTask(myRelease, theVersion);
						task.setTableName(myTableName);
						task.setConstraintName(myForeignKeyName);
						task.setColumnName(getColumnName());
						task.setForeignTableName(theForeignTable);
						task.setForeignColumnName(theForeignColumn);
						addTask(task);
					}
				}
			}

			public static class BuilderAddColumnWithName {
				private final String myRelease;
				private final String myVersion;
				private final String myColumnName;
				private final IAcceptsTasks myTaskSink;

				public BuilderAddColumnWithName(String theRelease, String theVersion, String theColumnName, IAcceptsTasks theTaskSink) {
					myRelease = theRelease;
					myVersion = theVersion;
					myColumnName = theColumnName;
					myTaskSink = theTaskSink;
				}

				public BuilderAddColumnWithNameNullable nullable() {
					return new BuilderAddColumnWithNameNullable(myRelease, myVersion, true);
				}

				public BuilderAddColumnWithNameNullable nonNullable() {
					return new BuilderAddColumnWithNameNullable(myRelease, myVersion, false);
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

					public void type(AddColumnTask.ColumnTypeEnum theColumnType) {
						type(theColumnType, null);
					}

					public void type(AddColumnTask.ColumnTypeEnum theColumnType, Integer theLength) {
						AddColumnTask task = new AddColumnTask(myRelease, myVersion);
						task.setColumnName(myColumnName);
						task.setNullable(myNullable);
						task.setColumnType(theColumnType);
						if (theLength != null) {
							task.setColumnLength(theLength);
						}
						myTaskSink.addTask(task);
					}
				}
			}
		}

	}

}
