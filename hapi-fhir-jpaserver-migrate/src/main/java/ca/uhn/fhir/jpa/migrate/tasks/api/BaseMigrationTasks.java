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

	protected Builder forVersion(T theVersion) {
		IAcceptsTasks sink = theTask -> {
			theTask.validate();
			myTasks.put(theVersion, theTask);
		};
		return new Builder(sink);
	}

	public interface IAcceptsTasks {
		void addTask(BaseTask<?> theTask);
	}

	protected static class Builder {

		private final IAcceptsTasks mySink;

		public Builder(IAcceptsTasks theSink) {
			mySink = theSink;
		}

		public BuilderWithTableName onTable(String theTableName) {
			return new BuilderWithTableName(mySink, theTableName);
		}

		public void addTask(BaseTask<?> theTask) {
			mySink.addTask(theTask);
		}

		public BuilderAddTableRawSql addTableRawSql(String theTableName) {
			return new BuilderAddTableRawSql(theTableName);
		}

		public Builder startSectionWithMessage(String theMessage) {
			Validate.notBlank(theMessage);
			addTask(new LogStartSectionWithMessageTask(theMessage));
			return this;
		}

		public BuilderAddTableByColumns addTableByColumns(String theTableName, String thePkColumnName) {
			return new BuilderAddTableByColumns(mySink, theTableName, thePkColumnName);
		}

		public void addIdGenerator(String theGeneratorName) {
			AddIdGeneratorTask task = new AddIdGeneratorTask(theGeneratorName);
			addTask(task);
		}

		public static class BuilderWithTableName implements IAcceptsTasks {
			private final String myTableName;
			private final IAcceptsTasks mySink;

			public BuilderWithTableName(IAcceptsTasks theSink, String theTableName) {
				mySink = theSink;
				myTableName = theTableName;
			}

			public String getTableName() {
				return myTableName;
			}

			public void dropIndex(String theIndexName) {
				DropIndexTask task = new DropIndexTask();
				task.setIndexName(theIndexName);
				task.setTableName(myTableName);
				addTask(task);
			}

			public BuilderAddIndexWithName addIndex(String theIndexName) {
				return new BuilderAddIndexWithName(theIndexName);
			}

			public BuilderAddColumnWithName addColumn(String theColumnName) {
				return new BuilderAddColumnWithName(theColumnName, this);
			}

			public void dropColumn(String theColumnName) {
				Validate.notBlank(theColumnName);
				DropColumnTask task = new DropColumnTask();
				task.setTableName(myTableName);
				task.setColumnName(theColumnName);
				addTask(task);
			}

			@Override
			public void addTask(BaseTask<?> theTask) {
				((BaseTableTask<?>)theTask).setTableName(myTableName);
				mySink.addTask(theTask);
			}

			public BuilderModifyColumnWithName modifyColumn(String theColumnName) {
				return new BuilderModifyColumnWithName(theColumnName);
			}

			public BuilderAddForeignKey addForeignKey(String theForeignKeyName) {
				return new BuilderAddForeignKey(theForeignKeyName);
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

					public void withColumns(String... theColumnNames) {
						AddIndexTask task = new AddIndexTask();
						task.setTableName(myTableName);
						task.setIndexName(myIndexName);
						task.setUnique(myUnique);
						task.setColumns(theColumnNames);
						addTask(task);
					}
				}
			}

			public static class BuilderAddColumnWithName {
				private final String myColumnName;
				private final IAcceptsTasks myTaskSink;

				public BuilderAddColumnWithName(String theColumnName, IAcceptsTasks theTaskSink) {
					myColumnName = theColumnName;
					myTaskSink = theTaskSink;
				}

				public BuilderAddColumnWithNameNullable nullable() {
					return new BuilderAddColumnWithNameNullable(true);
				}

				public BuilderAddColumnWithNameNullable nonNullable() {
					return new BuilderAddColumnWithNameNullable(false);
				}

				public class BuilderAddColumnWithNameNullable {
					private final boolean myNullable;

					public BuilderAddColumnWithNameNullable(boolean theNullable) {
						myNullable = theNullable;
					}

					public void type(AddColumnTask.ColumnTypeEnum theColumnType) {
						type(theColumnType, null);
					}

					public void type(AddColumnTask.ColumnTypeEnum theColumnType, Integer theLength) {
						AddColumnTask task = new AddColumnTask();
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

					public void withType(BaseTableColumnTypeTask.ColumnTypeEnum theColumnType) {
						withType(theColumnType, null);
					}

					public void withType(BaseTableColumnTypeTask.ColumnTypeEnum theColumnType, Integer theLength) {
						if (theColumnType == BaseTableColumnTypeTask.ColumnTypeEnum.STRING) {
							if (theLength == null || theLength == 0) {
								throw new IllegalArgumentException("Can not specify length 0 for column of type " + theColumnType);
							}
						} else {
							if (theLength != null) {
								throw new IllegalArgumentException("Can not specify length for column of type " + theColumnType);
							}
						}

						ModifyColumnTask task = new ModifyColumnTask();
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

					public void references(String theForeignTable, String theForeignColumn) {
						AddForeignKeyTask task = new AddForeignKeyTask();
						task.setTableName(myTableName);
						task.setConstraintName(myForeignKeyName);
						task.setColumnName(getColumnName());
						task.setForeignTableName(theForeignTable);
						task.setForeignColumnName(theForeignColumn);
						addTask(task);
					}
				}
			}
		}

		public class BuilderAddTableRawSql {

			private final AddTableRawSqlTask myTask;

			protected BuilderAddTableRawSql(String theTableName) {
				myTask = new AddTableRawSqlTask();
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

		public class BuilderAddTableByColumns implements IAcceptsTasks {
			private final AddTableByColumnTask myTask;

			public BuilderAddTableByColumns(IAcceptsTasks theSink, String theTableName, String thePkColumnName) {
				myTask = new AddTableByColumnTask();
				myTask.setTableName(theTableName);
				myTask.setPkColumn(thePkColumnName);
				theSink.addTask(myTask);
			}

			public BuilderWithTableName.BuilderAddColumnWithName addColumn(String theColumnName) {
				return new BuilderWithTableName.BuilderAddColumnWithName(theColumnName, this);
			}

			@Override
			public void addTask(BaseTask<?> theTask) {
				myTask.addAddColumnTask((AddColumnTask) theTask);
			}
		}

	}

}
