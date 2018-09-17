package ca.uhn.fhir.jpa.migrate.tasks.api;

/*-
 * #%L
 * HAPI FHIR JPA Server - Migration
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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
			if (((T)nextVersion).ordinal() <= theFrom.ordinal()) {
				continue;
			}
			if (((T)nextVersion).ordinal() > theTo.ordinal()) {
				continue;
			}

			Collection<BaseTask<?>> nextValues = myTasks.get((T)nextVersion);
			if (nextValues != null) {
				retVal.addAll(nextValues);
			}
		}

		return retVal;
	}

	protected Builder forVersion(T theVersion) {
		return new Builder(theVersion);
	}

	protected class Builder {

		private final T myVersion;
		private String myTableName;

		Builder(T theVersion) {
			myVersion = theVersion;
		}

		public BuilderWithTableName onTable(String theTableName) {
			myTableName = theTableName;
			return new BuilderWithTableName();
		}

		public void addTask(BaseTask theTask) {
			theTask.validate();
			myTasks.put(myVersion, theTask);
		}

		public BuilderAddTable addTable(String theTableName) {
			myTableName = theTableName;
			return new BuilderAddTable();
		}

		public Builder startSectionWithMessage(String theMessage) {
			Validate.notBlank(theMessage);
			addTask(new LogStartSectionWithMessageTask(theMessage));
			return this;
		}

		public class BuilderWithTableName {
			private String myIndexName;
			private String myColumnName;
			private String myForeignKeyName;

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
				myIndexName = theIndexName;
				return new BuilderAddIndexWithName();
			}

			public BuilderAddColumnWithName addColumn(String theColumnName) {
				myColumnName = theColumnName;
				return new BuilderAddColumnWithName();
			}

			public void addTask(BaseTableTask<?> theTask) {
				theTask.setTableName(myTableName);
				Builder.this.addTask(theTask);
			}

			public BuilderModifyColumnWithName modifyColumn(String theColumnName) {
				myColumnName = theColumnName;
				return new BuilderModifyColumnWithName();
			}

			public BuilderAddForeignKey addForeignKey(String theForeignKeyName) {
				myForeignKeyName = theForeignKeyName;
				return new BuilderAddForeignKey();
			}

			public class BuilderAddIndexWithName {
				private boolean myUnique;

				public BuilderAddIndexUnique unique(boolean theUnique) {
					myUnique = theUnique;
					return new BuilderAddIndexUnique();
				}

				public class BuilderAddIndexUnique {
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

			public class BuilderAddColumnWithName {
				private boolean myNullable;

				public BuilderAddColumnWithNameNullable nullable() {
					myNullable = true;
					return new BuilderAddColumnWithNameNullable();
				}

				public class BuilderAddColumnWithNameNullable {
					public void type(AddColumnTask.ColumnTypeEnum theColumnType) {
						AddColumnTask task = new AddColumnTask();
						task.setColumnName(myColumnName);
						task.setNullable(myNullable);
						task.setColumnType(theColumnType);
						addTask(task);
					}
				}
			}

			public class BuilderModifyColumnWithName {

				private boolean myNullable;

				public BuilderModifyColumnWithNameAndNullable nullable() {
					myNullable = true;
					return new BuilderModifyColumnWithNameAndNullable();
				}

				public BuilderModifyColumnWithNameAndNullable nonNullable() {
					myNullable = false;
					return new BuilderModifyColumnWithNameAndNullable();
				}

				public class BuilderModifyColumnWithNameAndNullable {

					public void withType(BaseTableColumnTypeTask.ColumnTypeEnum theColumnType) {
						withType(theColumnType, 0);
					}

					public void withType(BaseTableColumnTypeTask.ColumnTypeEnum theColumnType, int theLength) {
						if (theColumnType == BaseTableColumnTypeTask.ColumnTypeEnum.STRING) {
							if (theLength == 0) {
								throw new IllegalArgumentException("Can not specify length 0 for column of type " + theColumnType);
							}
							ModifyColumnTask task = new ModifyColumnTask();
							task.setColumnName(myColumnName);
							task.setTableName(myTableName);
							task.setColumnLength(theLength);
							task.setNullable(myNullable);
							task.setColumnType(theColumnType);
							addTask(task);
						} else if (theLength > 0){
							throw new IllegalArgumentException("Can not specify length for column of type " + theColumnType);
						}
					}

				}
			}

			public class BuilderAddForeignKey extends BuilderModifyColumnWithName {
				public BuilderAddForeignKeyToColumn toColumn(String theColumnName) {
					myColumnName = theColumnName;
					return new BuilderAddForeignKeyToColumn();
				}

				public class BuilderAddForeignKeyToColumn {
					public void references(String theForeignTable, String theForeignColumn) {
						AddForeignKeyTask task = new AddForeignKeyTask();
						task.setTableName(myTableName);
						task.setConstraintName(myForeignKeyName);
						task.setColumnName(myColumnName);
						task.setForeignTableName(theForeignTable);
						task.setForeignColumnName(theForeignColumn);
						addTask(task);
					}
				}
			}
		}

		public class BuilderAddTable {

			private final AddTableTask myTask;

			protected BuilderAddTable() {
				myTask = new AddTableTask();
				myTask.setTableName(myTableName);
				addTask(myTask);
			}


			public BuilderAddTable addSql(DriverTypeEnum theDriverTypeEnum, @Language("SQL") String theSql) {
				myTask.addSql(theDriverTypeEnum, theSql);
				return this;
			}
		}
	}


}
