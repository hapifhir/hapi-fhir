package ca.uhn.fhir.jpa.migrate.tasks;

import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamCoords;
import ca.uhn.fhir.jpa.migrate.taskdef.AddIndexTask;
import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;
import ca.uhn.fhir.jpa.migrate.taskdef.CalculateHashesTask;
import ca.uhn.fhir.jpa.migrate.taskdef.DropIndexTask;
import ca.uhn.fhir.util.VersionEnum;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

@SuppressWarnings("UnstableApiUsage")
public class HapiFhirJpaMigrationTasks {

	private Multimap<VersionEnum, BaseTask<?>> myTasks = MultimapBuilder.hashKeys().arrayListValues().build();

	/**
	 * Constructor
	 */
	public HapiFhirJpaMigrationTasks() {

		// Forced ID changes
		forVersion(VersionEnum.V3_5_0)
			.onTable("HFJ_FORCED_ID")
			.dropIndex("IDX_FORCEDID_TYPE_FORCEDID");
		forVersion(VersionEnum.V3_5_0)
			.onTable("HFJ_FORCED_ID")
			.dropIndex("IDX_FORCEDID_TYPE_RESID");
		forVersion(VersionEnum.V3_5_0)
			.onTable("HFJ_FORCED_ID")
			.addIndex("IDX_FORCEDID_TYPE_FID")
			.unique(true)
			.withColumns("RESOURCE_TYPE", "FORCED_ID");

		// Indexes - Coords
		forVersion(VersionEnum.V3_5_0)
			.onTable("HFJ_SPIDX_COORDS")
			.dropIndex("IDX_SP_COORDS_HASH");
		forVersion(VersionEnum.V3_5_0)
			.onTable("HFJ_SPIDX_COORDS")
			.addIndex("IDX_SP_COORDS_HASH")
			.unique(false)
			.withColumns("HASH_IDENTITY", "SP_VALUE", "SP_LATITUDE", "SP_LONGITUDE");
		forVersion(VersionEnum.V3_5_0)
			.addTask(new CalculateHashesTask().calculator(()->{
				return ResourceIndexedSearchParamCoords.calculateHashIdentity("resourceType", "paramName");
			}));

	}

	private Builder forVersion(VersionEnum theVersion) {
		return new Builder(theVersion);
	}


	private class Builder {

		private final VersionEnum myVersion;
		private String myTableName;

		public Builder(VersionEnum theVersion) {
			myVersion = theVersion;
		}

		public BuilderWithTableName onTable(String theTableName) {
			myTableName = theTableName;
			return new BuilderWithTableName();
		}

		private void addTask(BaseTask theTask) {
			theTask.validate();
			myTasks.put(myVersion, theTask);
		}

		private class BuilderWithTableName {
			private String myIndexName;

			void dropIndex(String theIndexName) {
				DropIndexTask task = new DropIndexTask();
				task.setIndexName(theIndexName);
				task.setTableName(myTableName);
				addTask(task);
			}

			public BuilderAddIndexWithName addIndex(String theIndexName) {
				myIndexName = theIndexName;
				return new BuilderAddIndexWithName();
			}

			private class BuilderAddIndexWithName {
				private boolean myUnique;

				public BuilderAddIndexUnique unique(boolean theUnique) {
					myUnique = theUnique;
					return new BuilderAddIndexUnique();
				}

				private class BuilderAddIndexUnique {
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
		}
	}

}
