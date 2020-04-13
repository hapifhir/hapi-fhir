package ca.uhn.fhir.jpa.model.config;

/**
 * @since 5.0.0
 */
public class PartitionConfig {

	private boolean myPartitioningEnabled = false;
	private boolean myAllowReferencesAcrossPartitions = false;
	private boolean myIncludePartitionInSearchHashes = true;

	/**
	 * If set to <code>true</code> (default is <code>true</code>) the <code>PARTITION_ID</code> value will be factored into the
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
	 * If set to <code>true</code> (default is <code>true</code>) the <code>PARTITION_ID</code> value will be factored into the
	 * hash values used in the <code>HFJ_SPIDX_xxx</code> tables, removing the need to explicitly add a selector
	 * on this column in queries. If set to <code>false</code>, an additional selector is used instead, which may perform
	 * better when using native database partitioning features.
	 * <p>
	 * This setting has no effect if partitioning is not enabled via {@link #isPartitioningEnabled()}.
	 * </p>
	 */
	public void setIncludePartitionInSearchHashes(boolean theIncludePartitionInSearchHashes) {
		myIncludePartitionInSearchHashes = theIncludePartitionInSearchHashes;
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
	 * Should resources references be permitted to cross partition boundaries. Default is <code>false</code>.
	 *
	 * @since 5.0.0
	 */
	public boolean isAllowReferencesAcrossPartitions() {
		return myAllowReferencesAcrossPartitions;
	}

	/**
	 * Should resources references be permitted to cross partition boundaries. Default is <code>false</code>.
	 *
	 * @since 5.0.0
	 */
	public void setAllowReferencesAcrossPartitions(boolean theAllowReferencesAcrossPartitions) {
		myAllowReferencesAcrossPartitions = theAllowReferencesAcrossPartitions;
	}
}
