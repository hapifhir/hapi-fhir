/*-
 * #%L
 * HAPI FHIR JPA Model
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.model.config;

import ca.uhn.fhir.jpa.model.entity.StorageSettings;

/**
 * @since 5.0.0
 */
public class PartitionSettings {

	private boolean myPartitioningEnabled = false;
	private CrossPartitionReferenceMode myAllowReferencesAcrossPartitions = CrossPartitionReferenceMode.NOT_ALLOWED;
	private boolean myIncludePartitionInSearchHashes = false;
	private boolean myUnnamedPartitionMode;
	private Integer myDefaultPartitionId;
	private boolean myAlwaysOpenNewTransactionForDifferentPartition;
	private boolean myConditionalCreateDuplicateIdentifiersEnabled = false;
	private boolean myPartitionIdsInPrimaryKeys = false;

	public PartitionSettings() {
		super();
	}

	/**
	 * This flag activates partition IDs in PKs mode, which is newly introduced in HAPI FHIR 8.0.0.
	 * This mode causes partition IDs to be included in all primary keys, joins, and emitted
	 * SQL. It also affects the generated schema and migrations. This setting should not be changed
	 * after the database has been initialized, unless you have performed an appropriate migration.
	 *
	 * @since 8.0.0
	 */
	public boolean isPartitionIdsInPrimaryKeys() {
		return myPartitionIdsInPrimaryKeys;
	}

	/**
	 * This flag activates partition IDs in PKs mode, which is newly introduced in HAPI FHIR 8.0.0.
	 * This mode causes partition IDs to be included in all primary keys, joins, and emitted
	 * SQL. It also affects the generated schema and migrations. This setting should not be changed
	 * after the database has been initialized, unless you have performed an appropriate migration.
	 *
	 * @since 8.0.0
	 */
	public void setPartitionIdsInPrimaryKeys(boolean thePartitionIdsInPrimaryKeys) {
		myPartitionIdsInPrimaryKeys = thePartitionIdsInPrimaryKeys;
	}

	/**
	 * Should we always open a new database transaction if the partition context changes
	 *
	 * @since 6.6.0
	 */
	public boolean isAlwaysOpenNewTransactionForDifferentPartition() {
		return myAlwaysOpenNewTransactionForDifferentPartition;
	}

	/**
	 * Should we always open a new database transaction if the partition context changes
	 *
	 * @since 6.6.0
	 */
	public void setAlwaysOpenNewTransactionForDifferentPartition(
			boolean theAlwaysOpenNewTransactionForDifferentPartition) {
		myAlwaysOpenNewTransactionForDifferentPartition = theAlwaysOpenNewTransactionForDifferentPartition;
	}

	/**
	 * If set to <code>true</code> (default is <code>false</code>) the <code>PARTITION_ID</code> value will be factored into the
	 * hash values used in the <code>HFJ_SPIDX_xxx</code> tables, removing the need to explicitly add a selector
	 * on this column in queries. If set to <code>false</code>, an additional selector is used instead, which may perform
	 * better when using native database partitioning features.
	 * <p>
	 * This setting has no effect if partitioning is not enabled via {@link #isPartitioningEnabled()}.
	 * </p>
	 * <p>
	 * If {@link StorageSettings#isIndexStorageOptimized()} is enabled this setting should be set to <code>false</code>.
	 * </p>
	 */
	public boolean isIncludePartitionInSearchHashes() {
		return myIncludePartitionInSearchHashes;
	}

	/**
	 * If set to <code>true</code> (default is <code>false</code>) the <code>PARTITION_ID</code> value will be factored into the
	 * hash values used in the <code>HFJ_SPIDX_xxx</code> tables, removing the need to explicitly add a selector
	 * on this column in queries. If set to <code>false</code>, an additional selector is used instead, which may perform
	 * better when using native database partitioning features.
	 * <p>
	 * This setting has no effect if partitioning is not enabled via {@link #isPartitioningEnabled()}.
	 * </p>
	 * <p>
	 * If {@link StorageSettings#isIndexStorageOptimized()} is enabled this setting should be set to <code>false</code>.
	 * </p>
	 */
	public PartitionSettings setIncludePartitionInSearchHashes(boolean theIncludePartitionInSearchHashes) {
		myIncludePartitionInSearchHashes = theIncludePartitionInSearchHashes;
		return this;
	}

	/**
	 * If enabled (default is <code>false</code>) the JPA server will support data partitioning
	 *
	 * @since 5.0.0
	 */
	public boolean isPartitioningEnabled() {
		return myPartitioningEnabled;
	}

	/**
	 * If enabled (default is <code>false</code>) the JPA server will support data partitioning
	 *
	 * @since 5.0.0
	 */
	public void setPartitioningEnabled(boolean theMultiTenancyEnabled) {
		myPartitioningEnabled = theMultiTenancyEnabled;
	}

	/**
	 * Should resources references be permitted to cross partition boundaries. Default is {@link CrossPartitionReferenceMode#NOT_ALLOWED}.
	 *
	 * @since 5.0.0
	 */
	public CrossPartitionReferenceMode getAllowReferencesAcrossPartitions() {
		return myAllowReferencesAcrossPartitions;
	}

	/**
	 * Should resources references be permitted to cross partition boundaries. Default is {@link CrossPartitionReferenceMode#NOT_ALLOWED}.
	 *
	 * @since 5.0.0
	 */
	public void setAllowReferencesAcrossPartitions(CrossPartitionReferenceMode theAllowReferencesAcrossPartitions) {
		myAllowReferencesAcrossPartitions = theAllowReferencesAcrossPartitions;
	}

	/**
	 * If set to <code>true</code> (default is <code>false</code>), partitions will be unnamed and all IDs from {@link Integer#MIN_VALUE} through
	 * {@link Integer#MAX_VALUE} will be allowed without needing to be created ahead of time.
	 *
	 * @since 5.5.0
	 */
	public boolean isUnnamedPartitionMode() {
		return myUnnamedPartitionMode;
	}

	/**
	 * If set to <code>true</code> (default is <code>false</code>), partitions will be unnamed and all IDs from {@link Integer#MIN_VALUE} through
	 * {@link Integer#MAX_VALUE} will be allowed without needing to be created ahead of time.
	 *
	 * @since 5.5.0
	 */
	public void setUnnamedPartitionMode(boolean theUnnamedPartitionMode) {
		myUnnamedPartitionMode = theUnnamedPartitionMode;
	}

	/**
	 * If set, the given ID will be used for the default partition. The default is
	 * <code>null</code> which will result in the use of a null value for default
	 * partition items.
	 *
	 * @since 5.5.0
	 */
	public Integer getDefaultPartitionId() {
		return myDefaultPartitionId;
	}

	/**
	 * If set, the given ID will be used for the default partition. The default is
	 * <code>null</code> which will result in the use of a null value for default
	 * partition items.
	 *
	 * @since 5.5.0
	 */
	public void setDefaultPartitionId(Integer theDefaultPartitionId) {
		myDefaultPartitionId = theDefaultPartitionId;
	}

	/**
	 * If enabled the JPA server will allow unqualified cross partition reference
	 */
	public boolean isAllowUnqualifiedCrossPartitionReference() {
		return myAllowReferencesAcrossPartitions.equals(
				PartitionSettings.CrossPartitionReferenceMode.ALLOWED_UNQUALIFIED);
	}

	/**
	 * The {@link ca.uhn.fhir.jpa.model.entity.ResourceSearchUrlEntity}
	 * table is used to prevent accidental concurrent conditional create/update operations
	 * from creating duplicate resources by inserting a row in that table as a part
	 * of the database transaction performing the write operation. If this setting
	 * is set to {@literal false} (which is the default), the partition
	 * ID is not written to this table, meaning that duplicates are prevented across
	 * partitions. If this setting is {@literal true}, duplicates will not be
	 * prevented if they appear on different partitions.
	 */
	public boolean isConditionalCreateDuplicateIdentifiersEnabled() {
		return myConditionalCreateDuplicateIdentifiersEnabled;
	}

	/**
	 * The {@link ca.uhn.fhir.jpa.model.entity.ResourceSearchUrlEntity}
	 * table is used to prevent accidental concurrent conditional create/update operations
	 * from creating duplicate resources by inserting a row in that table as a part
	 * of the database transaction performing the write operation. If this setting
	 * is set to {@literal false} (which is the default), the partition
	 * ID is not written to this table, meaning that duplicates are prevented across
	 * partitions. If this setting is {@literal true}, duplicates will not be
	 * prevented if they appear on different partitions.
	 */
	public void setConditionalCreateDuplicateIdentifiersEnabled(
			boolean theConditionalCreateDuplicateIdentifiersEnabled) {
		myConditionalCreateDuplicateIdentifiersEnabled = theConditionalCreateDuplicateIdentifiersEnabled;
	}

	public enum CrossPartitionReferenceMode {

		/**
		 * References between resources are not allowed to cross partition boundaries
		 */
		NOT_ALLOWED,

		/**
		 * References can cross partition boundaries, with an assumption that boundaries
		 * will be managed by the database.
		 */
		ALLOWED_UNQUALIFIED,
	}

	public enum BlockPatientCompartmentUpdateMode {
		/**
		 * Resource updates which would change resource's patient compartment are blocked.
		 */
		ALWAYS,

		/**
		 * Resource updates which would change resource's patient compartment are blocked
		 * when Partition Selection Mode is PATIENT_ID
		 */
		DEFAULT,

		/**
		 * Resource updates which would change resource's patient compartment are allowed.
		 */
		NEVER,
	}
}
