package ca.uhn.fhir.jpa.model.config;

/*-
 * #%L
 * HAPI FHIR JPA Model
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

/**
 * @since 5.0.0
 */
public class PartitionSettings {

	private boolean myPartitioningEnabled = false;
	private CrossPartitionReferenceMode myAllowReferencesAcrossPartitions = CrossPartitionReferenceMode.NOT_ALLOWED;
	private boolean myIncludePartitionInSearchHashes = false;
	private boolean myUnnamedPartitionMode;
	private Integer myDefaultPartitionId;

	/**
	 * If set to <code>true</code> (default is <code>false</code>) the <code>PARTITION_ID</code> value will be factored into the
	 * hash values used in the <code>HFJ_SPIDX_xxx</code> tables, removing the need to explicitly add a selector
	 * on this column in queries. If set to <code>false</code>, an additional selector is used instead, which may perform
	 * better when using native database partitioning features.
	 * <p>
	 * This setting has no effect if partitioning is not enabled via {@link #isPartitioningEnabled()}.
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


	public enum CrossPartitionReferenceMode {

		/**
		 * References between resources are not allowed to cross partition boundaries
		 */
		NOT_ALLOWED,

		/**
		 * References can cross partition boundaries, in a way that hides the existence of partitions to the end user
		 */
		ALLOWED_UNQUALIFIED

	}

	/**
	 * If enabled the JPA server will allow unqualified cross partition reference
	 *
	 */
	public boolean isAllowUnqualifiedCrossPartitionReference() {
		return myAllowReferencesAcrossPartitions.equals(PartitionSettings.CrossPartitionReferenceMode.ALLOWED_UNQUALIFIED);
	}

}
