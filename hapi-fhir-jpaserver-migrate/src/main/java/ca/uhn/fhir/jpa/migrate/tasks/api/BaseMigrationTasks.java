package ca.uhn.fhir.jpa.migrate.tasks.api;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.taskdef.*;
import ca.uhn.fhir.jpa.migrate.tasks.HapiFhirJpaMigrationTasks;
import ca.uhn.fhir.util.VersionEnum;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import org.apache.commons.lang3.Validate;
import org.intellij.lang.annotations.Language;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BaseMigrationTasks {
	private Multimap<VersionEnum, BaseTask<?>> myTasks = MultimapBuilder.hashKeys().arrayListValues().build();

	public List<BaseTask<?>> getTasks(@Nonnull VersionEnum theFrom, @Nonnull VersionEnum theTo) {
		Validate.notNull(theFrom);
		Validate.notNull(theTo);
		Validate.isTrue(theFrom.ordinal() < theTo.ordinal(), "From version must be lower than to version");

		List<BaseTask<?>> retVal = new ArrayList<>();
		for (VersionEnum nextVersion : VersionEnum.values()) {
			if (nextVersion.ordinal() <= theFrom.ordinal()) {
				continue;
			}
			if (nextVersion.ordinal() > theTo.ordinal()) {
				continue;
			}

			Collection<BaseTask<?>> nextValues = myTasks.get(nextVersion);
			if (nextValues != null) {
				retVal.addAll(nextValues);
			}
		}

		return retVal;
	}

	protected HapiFhirJpaMigrationTasks.Builder forVersion(VersionEnum theVersion) {
		return new HapiFhirJpaMigrationTasks.Builder(theVersion);
	}

	protected class Builder {

		private final VersionEnum myVersion;
		private String myTableName;

		Builder(VersionEnum theVersion) {
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

		public void startSectionWithMessage(String theMessage) {
			Validate.notBlank(theMessage);
			addTask(new LogStartSectionWithMessageTask(theMessage));
		}

		public class BuilderWithTableName {
			private String myIndexName;
			private String myColumnName;

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

					public void withType(BaseTableColumnTypeTask.ColumnTypeEnum theColumnType, int theLength) {
						if (theColumnType == BaseTableColumnTypeTask.ColumnTypeEnum.STRING) {
							ModifyColumnTask task = new ModifyColumnTask();
							task.setColumnName(myColumnName);
							task.setTableName(myTableName);
							task.setColumnLength(theLength);
							task.setNullable(myNullable);
							task.setColumnType(theColumnType);
							addTask(task);
						} else {
							throw new IllegalArgumentException("Can not specify length for column of type " + theColumnType);
						}
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
