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
package ca.uhn.fhir.jpa.model.entity;

import ca.uhn.fhir.context.ParserOptions;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.util.ISequenceValueMassager;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.rest.server.interceptor.ResponseTerminologyTranslationSvc;
import ca.uhn.fhir.util.HapiExtensions;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.DateTimeType;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

/**
 * This class contains configuration options common to all hapi-fhir-storage implementations.
 * Ultimately it should live in that project
 */
public class StorageSettings {
	/**
	 * @since 5.6.0
	 */
	// Thread Pool size used by batch in bundle
	public static final int DEFAULT_BUNDLE_BATCH_POOL_SIZE = 20; // 1 for single thread

	public static final int DEFAULT_BUNDLE_BATCH_MAX_POOL_SIZE = 100; // 1 for single thread
	/**
	 * Default {@link #getTreatReferencesAsLogical() logical URL bases}. Includes the following
	 * values:
	 * <ul>
	 * <li><code>"http://hl7.org/fhir/valueset-*"</code></li>
	 * <li><code>"http://hl7.org/fhir/codesystem-*"</code></li>
	 * <li><code>"http://hl7.org/fhir/StructureDefinition/*"</code></li>
	 * </ul>
	 */
	public static final Set<String> DEFAULT_LOGICAL_BASE_URLS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
			"http://hl7.org/fhir/ValueSet/*",
			"http://hl7.org/fhir/CodeSystem/*",
			"http://hl7.org/fhir/valueset-*",
			"http://hl7.org/fhir/codesystem-*",
			"http://hl7.org/fhir/StructureDefinition/*")));

	/*
	 * <p>
	 * Note the following database documented limitations:
	 *    <ul>
	 *       <li>JDBC Timestamp Datatype Low Value -4713 and High Value 9999</li>
	 *       <li>MySQL 8: the range for DATETIME values is '1000-01-01 00:00:00.000000' to '9999-12-31 23:59:59.999999`</li>
	 *       <li>Postgresql 12: Timestamp [without time zone] Low Value 4713 BC and High Value 294276 AD</li>
	 *       <li>Oracle: Timestamp Low Value 4712 BC and High Value 9999 CE</li>
	 *       <li>H2: datetime2 Low Value -4713 and High Value 9999</li>
	 *     </ul>
	 * </p>
	 */
	protected static final String DEFAULT_PERIOD_INDEX_START_OF_TIME = "1001-01-01";
	protected static final String DEFAULT_PERIOD_INDEX_END_OF_TIME = "9000-01-01";
	private static final Integer DEFAULT_MAXIMUM_TRANSACTION_BUNDLE_SIZE = null;
	/**
	 * update setter javadoc if default changes
	 */
	private boolean myAllowContainsSearches = false;

	private boolean myAllowExternalReferences = false;
	private Set<String> myTreatBaseUrlsAsLocal = new HashSet<>();
	private Set<String> myTreatReferencesAsLogical = new HashSet<>(DEFAULT_LOGICAL_BASE_URLS);
	private boolean myDefaultSearchParamsCanBeOverridden = true;
	private boolean myAutoCreatePlaceholderReferenceTargets;
	private Integer myBundleBatchPoolSize = DEFAULT_BUNDLE_BATCH_POOL_SIZE;
	private Integer myBundleBatchMaxPoolSize = DEFAULT_BUNDLE_BATCH_MAX_POOL_SIZE;
	private boolean myMassIngestionMode;
	private Integer myMaximumTransactionBundleSize = DEFAULT_MAXIMUM_TRANSACTION_BUNDLE_SIZE;
	private boolean myNormalizeTerminologyForBulkExportJobs = false;
	/**
	 * Update setter javadoc if default changes.
	 */
	private boolean myUseOrdinalDatesForDayPrecisionSearches = true;

	private boolean mySuppressStringIndexingInTokens = false;
	private Class<? extends ISequenceValueMassager> mySequenceValueMassagerClass;
	private IPrimitiveType<Date> myPeriodIndexStartOfTime;
	private IPrimitiveType<Date> myPeriodIndexEndOfTime;
	private NormalizedQuantitySearchLevel myNormalizedQuantitySearchLevel;
	private Set<String> myAutoVersionReferenceAtPaths = Collections.emptySet();
	private Map<String, Set<String>> myTypeToAutoVersionReferenceAtPaths = Collections.emptyMap();
	private boolean myRespectVersionsForSearchIncludes;
	private boolean myIndexOnUpliftedRefchains = false;
	private boolean myIndexOnContainedResources = false;
	private boolean myIndexOnContainedResourcesRecursively = false;
	private boolean myAllowMdmExpansion = false;
	private boolean myAutoSupportDefaultSearchParams = true;
	private boolean myIndexIdentifierOfType = false;
	private IndexEnabledEnum myIndexMissingFieldsEnabled = IndexEnabledEnum.DISABLED;

	/**
	 * Should the {@literal _lamguage} SearchParameter be supported
	 * on this server?
	 *
	 * @since 7.0.0
	 */
	private boolean myLanguageSearchParameterEnabled = false;

	/**
	 * If set to false, all resource types will be installed via package installer, regardless of their status.
	 * Otherwise, resources will be filtered based on status according to some criteria which can be found in
	 * <code>PackageInstallerSvcImpl#isValidResourceStatusForPackageUpload<code>
	 * @since 7.0.0
	 */
	private boolean myValidateResourceStatusForPackageUpload = true;

	/**
	 * If set to <code>true</code>, the server will not write data to the <code>SP_NAME, RES_TYPE, SP_UPDATED</code>
	 * columns for all HFJ_SPIDX tables.
	 *
	 * @since 7.4.0
	 */
	private boolean myIndexStorageOptimized = false;

	/**
	 * Constructor
	 */
	public StorageSettings() {
		setSequenceValueMassagerClass(ISequenceValueMassager.NoopSequenceValueMassager.class);
		setPeriodIndexStartOfTime(new DateTimeType(DEFAULT_PERIOD_INDEX_START_OF_TIME));
		setPeriodIndexEndOfTime(new DateTimeType(DEFAULT_PERIOD_INDEX_END_OF_TIME));
		setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_NOT_SUPPORTED);
	}

	/**
	 * When creating or updating a resource: If this property is set to <code>true</code>
	 * (default is <code>false</code>), if the resource has a reference to another resource
	 * on the local server but that reference does not exist, a placeholder resource will be
	 * created.
	 * <p>
	 * In other words, if an observation with subject <code>Patient/FOO</code> is created, but
	 * there is no resource called <code>Patient/FOO</code> on the server, this property causes
	 * an empty patient with ID "FOO" to be created in order to prevent this operation
	 * from failing.
	 * </p>
	 * <p>
	 * This property can be useful in cases where replication between two servers is wanted.
	 * Note however that references containing purely numeric IDs will not be auto-created
	 * as they are never allowed to be client supplied in HAPI FHIR JPA.
	 * <p>
	 * All placeholder resources created in this way have an extension
	 * with the URL {@link HapiExtensions#EXT_RESOURCE_PLACEHOLDER} and the value "true".
	 * </p>
	 */
	public boolean isAutoCreatePlaceholderReferenceTargets() {
		return myAutoCreatePlaceholderReferenceTargets;
	}

	/**
	 * When creating or updating a resource: If this property is set to <code>true</code>
	 * (default is <code>false</code>), if the resource has a reference to another resource
	 * on the local server but that reference does not exist, a placeholder resource will be
	 * created.
	 * <p>
	 * In other words, if an observation with subject <code>Patient/FOO</code> is created, but
	 * there is no resource called <code>Patient/FOO</code> on the server, this property causes
	 * an empty patient with ID "FOO" to be created in order to prevent this operation
	 * from failing.
	 * </p>
	 * <p>
	 * This property can be useful in cases where replication between two servers is wanted.
	 * Note however that references containing purely numeric IDs will not be auto-created
	 * as they are never allowed to be client supplied in HAPI FHIR JPA.
	 * <p>
	 * All placeholder resources created in this way have an extension
	 * with the URL {@link HapiExtensions#EXT_RESOURCE_PLACEHOLDER} and the value "true".
	 * </p>
	 */
	public void setAutoCreatePlaceholderReferenceTargets(boolean theAutoCreatePlaceholderReferenceTargets) {
		myAutoCreatePlaceholderReferenceTargets = theAutoCreatePlaceholderReferenceTargets;
	}

	/**
	 * Get the batch transaction thread pool size.
	 *
	 * @since 5.6.0
	 */
	public Integer getBundleBatchPoolSize() {
		return myBundleBatchPoolSize;
	}

	/**
	 * Set the batch transaction thread pool size. The default is @see {@link #DEFAULT_BUNDLE_BATCH_POOL_SIZE}
	 * set pool size to 1 for single thread
	 *
	 * @since 5.6.0
	 */
	public void setBundleBatchPoolSize(Integer theBundleBatchPoolSize) {
		this.myBundleBatchPoolSize = theBundleBatchPoolSize;
	}

	/**
	 * Get the batch transaction thread max pool size.
	 * set max pool size to 1 for single thread
	 *
	 * @since 5.6.0
	 */
	public Integer getBundleBatchMaxPoolSize() {
		return myBundleBatchMaxPoolSize;
	}

	/**
	 * Set the batch transaction thread pool size. The default is @see {@link #DEFAULT_BUNDLE_BATCH_MAX_POOL_SIZE}
	 *
	 * @since 5.6.0
	 */
	public void setBundleBatchMaxPoolSize(Integer theBundleBatchMaxPoolSize) {
		this.myBundleBatchMaxPoolSize = theBundleBatchMaxPoolSize;
	}

	/**
	 * If set to {@link IndexEnabledEnum#DISABLED} (default is {@link IndexEnabledEnum#DISABLED})
	 * the server will not create search indexes for search parameters with no values in resources.
	 * <p>
	 * Disabling this feature means that the <code>:missing</code> search modifier will not be
	 * supported on the server, but also means that storage and indexing (i.e. writes to the
	 * database) may be much faster on servers which have lots of search parameters and need
	 * to write quickly.
	 * </p>
	 * <p>
	 * This feature may be enabled on servers where supporting the use of the :missing parameter is
	 * of higher importance than raw write performance
	 * </p>
	 */
	public IndexEnabledEnum getIndexMissingFields() {
		return myIndexMissingFieldsEnabled;
	}

	/**
	 * If set to {@link IndexEnabledEnum#DISABLED} (default is {@link IndexEnabledEnum#DISABLED})
	 * the server will not create search indexes for search parameters with no values in resources.
	 * <p>
	 * Disabling this feature means that the <code>:missing</code> search modifier will not be
	 * supported on the server, but also means that storage and indexing (i.e. writes to the
	 * database) may be much faster on servers which have lots of search parameters and need
	 * to write quickly.
	 * </p>
	 * <p>
	 * This feature may be enabled on servers where supporting the use of the :missing parameter is
	 * of higher importance than raw write performance
	 * </p>
	 * <p>
	 * Note that this setting also has an impact on sorting (i.e. using the
	 * <code>_sort</code> parameter on searches): If the server is configured
	 * to not index missing field.
	 * </p>
	 * <p>
	 * The following index may need to be added into the indexed tables such as <code>HFJ_SPIDX_TOKEN</code>
	 * to improve the search performance while <code>:missing</code> is enabled.
	 * <code>RES_TYPE, SP_NAME, SP_MISSING</code>
	 * </p>
	 */
	public void setIndexMissingFields(IndexEnabledEnum theIndexMissingFields) {
		Validate.notNull(theIndexMissingFields, "theIndexMissingFields must not be null");
		myIndexMissingFieldsEnabled = theIndexMissingFields;
	}

	/**
	 * If set to <code>true</code> (default is false), the server will not write data
	 * to the <code>SP_NAME, RES_TYPE, SP_UPDATED</code> columns for all HFJ_SPIDX tables.
	 * <p>
	 * This feature may be enabled on servers where HFJ_SPIDX tables are expected
	 * to have a large amount of data (millions of rows) in order to reduce overall storage size.
	 * </p>
	 * <p>
	 * Note that this setting only applies to newly inserted and updated rows in HFJ_SPIDX tables.
	 * In order to apply this optimization setting to existing HFJ_SPIDX index rows,
	 * <code>$reindex</code> operation should be executed at the instance or server level.
	 * <p>
	 * <p>
	 * If this setting is enabled, {@link PartitionSettings#isIncludePartitionInSearchHashes()} should be disabled.
	 * </p>
	 * <p>
	 * If {@link StorageSettings#getIndexMissingFields()} is enabled, the following index may need to be added
	 * into the HFJ_SPIDX tables to improve the search performance: <code>HASH_IDENTITY, SP_MISSING, RES_ID, PARTITION_ID</code>
	 * </p>
	 *
	 * @since 7.4.0
	 */
	public boolean isIndexStorageOptimized() {
		return myIndexStorageOptimized;
	}

	/**
	 * If set to <code>true</code> (default is false), the server will not write data
	 * to the <code>SP_NAME, RES_TYPE, SP_UPDATED</code> columns for all HFJ_SPIDX tables.
	 * <p>
	 * This feature may be enabled on servers where HFJ_SPIDX tables are expected
	 * to have a large amount of data (millions of rows) in order to reduce overall storage size.
	 * </p>
	 * <p>
	 * Note that this setting only applies to newly inserted and updated rows in HFJ_SPIDX tables.
	 * In order to apply this optimization setting to existing HFJ_SPIDX index rows,
	 * <code>$reindex</code> operation should be executed at the instance or server level.
	 * <p>
	 * <p>
	 * If this setting is enabled, {@link PartitionSettings#isIncludePartitionInSearchHashes()} should be set to <code>false</code>.
	 * </p>
	 * <p>
	 * If {@link StorageSettings#getIndexMissingFields()} ()} is enabled, the following index may need to be added
	 * into the HFJ_SPIDX tables to improve the search performance: <code>HASH_IDENTITY, SP_MISSING, RES_ID, PARTITION_ID</code>
	 * </p>
	 *
	 * @since 7.4.0
	 */
	public void setIndexStorageOptimized(boolean theIndexStorageOptimized) {
		myIndexStorageOptimized = theIndexStorageOptimized;
	}

	/**
	 * If this is enabled (disabled by default), Mass Ingestion Mode is enabled. In this mode, a number of
	 * runtime checks are disabled. This mode is designed for rapid backloading of data while the system is not
	 * being otherwise used.
	 * <p>
	 * In this mode:
	 * <p>
	 * - Tags/Profiles/Security Labels will not be updated on existing resources that already have them
	 * - Resources modification checks will be skipped in favour of a simple hash check
	 * - Extra resource ID caching is enabled
	 *
	 * @since 5.5.0
	 */
	public boolean isMassIngestionMode() {
		return myMassIngestionMode;
	}

	/**
	 * If this is enabled (disabled by default), Mass Ingestion Mode is enabled. In this mode, a number of
	 * runtime checks are disabled. This mode is designed for rapid backloading of data while the system is not
	 * being otherwise used.
	 * <p>
	 * In this mode:
	 * <p>
	 * - Tags/Profiles/Security Labels will not be updated on existing resources that already have them
	 * - Resources modification checks will be skipped in favour of a simple hash check
	 * - Extra resource ID caching is enabled
	 *
	 * @since 5.5.0
	 */
	public void setMassIngestionMode(boolean theMassIngestionMode) {
		myMassIngestionMode = theMassIngestionMode;
	}

	/**
	 * Specifies the maximum number of resources permitted within a single transaction bundle.
	 * If a transaction bundle is submitted with more than this number of resources, it will be
	 * rejected with a PayloadTooLarge exception.
	 * <p>
	 * The default value is <code>null</code>, which means that there is no limit.
	 * </p>
	 */
	public Integer getMaximumTransactionBundleSize() {
		return myMaximumTransactionBundleSize;
	}

	/**
	 * Specifies the maximum number of resources permitted within a single transaction bundle.
	 * If a transaction bundle is submitted with more than this number of resources, it will be
	 * rejected with a PayloadTooLarge exception.
	 * <p>
	 * The default value is <code>null</code>, which means that there is no limit.
	 * </p>
	 */
	public StorageSettings setMaximumTransactionBundleSize(Integer theMaximumTransactionBundleSize) {
		myMaximumTransactionBundleSize = theMaximumTransactionBundleSize;
		return this;
	}

	/**
	 * If set to true, attempt to map terminology for bulk export jobs using the
	 * logic in
	 * {@link ResponseTerminologyTranslationSvc}. Default is <code>false</code>.
	 *
	 * @since 6.3.0
	 */
	public boolean isNormalizeTerminologyForBulkExportJobs() {
		return myNormalizeTerminologyForBulkExportJobs;
	}

	/**
	 * If set to true, attempt to map terminology for bulk export jobs using the
	 * logic in
	 * {@link ResponseTerminologyTranslationSvc}. Default is <code>false</code>.
	 *
	 * @since 6.3.0
	 */
	public void setNormalizeTerminologyForBulkExportJobs(boolean theNormalizeTerminologyForBulkExportJobs) {
		myNormalizeTerminologyForBulkExportJobs = theNormalizeTerminologyForBulkExportJobs;
	}

	/**
	 * This is an internal API and may change or disappear without notice
	 *
	 * @since 6.2.0
	 */
	public Class<? extends ISequenceValueMassager> getSequenceValueMassagerClass() {
		return mySequenceValueMassagerClass;
	}

	/**
	 * This is an internal API and may change or disappear without notice
	 *
	 * @since 6.2.0
	 */
	public void setSequenceValueMassagerClass(Class<? extends ISequenceValueMassager> theSequenceValueMassagerClass) {
		Validate.notNull(theSequenceValueMassagerClass, "theSequenceValueMassagerClass must not be null");
		mySequenceValueMassagerClass = theSequenceValueMassagerClass;
	}

	/**
	 * If set to <code>true</code> (default is <code>false</code>) the
	 * <code>:of-type</code> modifier on token search parameters for
	 * identifiers will be supported. Enabling this causes additional
	 * indexing overhead (although very minor) so it is disabled unless it is
	 * actually needed.
	 *
	 * @since 5.7.0
	 */
	public boolean isIndexIdentifierOfType() {
		return myIndexIdentifierOfType;
	}

	/**
	 * If set to <code>true</code> (default is <code>false</code>) the
	 * <code>:of-type</code> modifier on token search parameters for
	 * identifiers will be supported. Enabling this causes additional
	 * indexing overhead (although very minor) so it is disabled unless it is
	 * actually needed.
	 *
	 * @since 5.7.0
	 */
	public void setIndexIdentifierOfType(boolean theIndexIdentifierOfType) {
		myIndexIdentifierOfType = theIndexIdentifierOfType;
	}

	/**
	 * If set to {@code true} the default search params (i.e. the search parameters that are
	 * defined by the FHIR specification itself) may be overridden by uploading search
	 * parameters to the server with the same code as the built-in search parameter.
	 * <p>
	 * This can be useful if you want to be able to disable or alter
	 * the behaviour of the default search parameters.
	 * </p>
	 * <p>
	 * The default value for this setting is {@code true}
	 * </p>
	 */
	public boolean isDefaultSearchParamsCanBeOverridden() {
		return myDefaultSearchParamsCanBeOverridden;
	}

	/**
	 * If set to {@code true} the default search params (i.e. the search parameters that are
	 * defined by the FHIR specification itself) may be overridden by uploading search
	 * parameters to the server with the same code as the built-in search parameter.
	 * <p>
	 * This can be useful if you want to be able to disable or alter
	 * the behaviour of the default search parameters.
	 * </p>
	 * <p>
	 * The default value for this setting is {@code true}
	 * </p>
	 */
	public void setDefaultSearchParamsCanBeOverridden(boolean theDefaultSearchParamsCanBeOverridden) {
		myDefaultSearchParamsCanBeOverridden = theDefaultSearchParamsCanBeOverridden;
	}

	/**
	 * If enabled, the server will support the use of :contains searches,
	 * which are helpful but can have adverse effects on performance.
	 * <p>
	 * Default is <code>false</code> (Note that prior to HAPI FHIR
	 * 3.5.0 the default was <code>true</code>)
	 * </p>
	 * <p>
	 * Note: If you change this value after data already has
	 * already been stored in the database, you must for a reindexing
	 * of all data in the database or resources may not be
	 * searchable.
	 * </p>
	 */
	public boolean isAllowContainsSearches() {
		return myAllowContainsSearches;
	}

	/**
	 * If enabled, the server will support the use of :contains searches,
	 * which are helpful but can have adverse effects on performance.
	 * <p>
	 * Default is <code>false</code> (Note that prior to HAPI FHIR
	 * 3.5.0 the default was <code>true</code>)
	 * </p>
	 * <p>
	 * Note: If you change this value after data already has
	 * already been stored in the database, you must for a reindexing
	 * of all data in the database or resources may not be
	 * searchable.
	 * </p>
	 */
	public void setAllowContainsSearches(boolean theAllowContainsSearches) {
		this.myAllowContainsSearches = theAllowContainsSearches;
	}

	/**
	 * If enabled, the server will support the use of :mdm search parameter qualifier on Reference Search Parameters.
	 * This Parameter Qualifier is HAPI-specific, and not defined anywhere in the FHIR specification. Using this qualifier
	 * will result in an MDM expansion being done on the reference, which will expand the search scope. For example, if Patient/1
	 * is MDM-matched to Patient/2 and you execute the search:
	 * Observation?subject:mdm=Patient/1 , you will receive observations for both Patient/1 and Patient/2.
	 * <p>
	 * Default is <code>false</code>
	 * </p>
	 *
	 * @since 5.4.0
	 */
	public boolean isAllowMdmExpansion() {
		return myAllowMdmExpansion;
	}

	/**
	 * If enabled, the server will support the use of :mdm search parameter qualifier on Reference Search Parameters.
	 * This Parameter Qualifier is HAPI-specific, and not defined anywhere in the FHIR specification. Using this qualifier
	 * will result in an MDM expansion being done on the reference, which will expand the search scope. For example, if Patient/1
	 * is MDM-matched to Patient/2 and you execute the search:
	 * Observation?subject:mdm=Patient/1 , you will receive observations for both Patient/1 and Patient/2.
	 * <p>
	 * Default is <code>false</code>
	 * </p>
	 *
	 * @since 5.4.0
	 */
	public void setAllowMdmExpansion(boolean theAllowMdmExpansion) {
		myAllowMdmExpansion = theAllowMdmExpansion;
	}

	/**
	 * If set to <code>true</code> (default is <code>false</code>) the server will allow
	 * resources to have references to external servers. For example if this server is
	 * running at <code>http://example.com/fhir</code> and this setting is set to
	 * <code>true</code> the server will allow a Patient resource to be saved with a
	 * Patient.organization value of <code>http://foo.com/Organization/1</code>.
	 * <p>
	 * Under the default behaviour if this value has not been changed, the above
	 * resource would be rejected by the server because it requires all references
	 * to be resolvable on the local server.
	 * </p>
	 * <p>
	 * Note that external references will be indexed by the server and may be searched
	 * (e.g. <code>Patient:organization</code>), but
	 * chained searches (e.g. <code>Patient:organization.name</code>) will not work across
	 * these references.
	 * </p>
	 * <p>
	 * It is recommended to also set {@link #setTreatBaseUrlsAsLocal(Set)} if this value
	 * is set to <code>true</code>
	 * </p>
	 *
	 * @see #setTreatBaseUrlsAsLocal(Set)
	 * @see #setAllowExternalReferences(boolean)
	 */
	public boolean isAllowExternalReferences() {
		return myAllowExternalReferences;
	}

	/**
	 * If set to <code>true</code> (default is <code>false</code>) the server will allow
	 * resources to have references to external servers. For example if this server is
	 * running at <code>http://example.com/fhir</code> and this setting is set to
	 * <code>true</code> the server will allow a Patient resource to be saved with a
	 * Patient.organization value of <code>http://foo.com/Organization/1</code>.
	 * <p>
	 * Under the default behaviour if this value has not been changed, the above
	 * resource would be rejected by the server because it requires all references
	 * to be resolvable on the local server.
	 * </p>
	 * <p>
	 * Note that external references will be indexed by the server and may be searched
	 * (e.g. <code>Patient:organization</code>), but
	 * chained searches (e.g. <code>Patient:organization.name</code>) will not work across
	 * these references.
	 * </p>
	 * <p>
	 * It is recommended to also set {@link #setTreatBaseUrlsAsLocal(Set)} if this value
	 * is set to <code>true</code>
	 * </p>
	 *
	 * @see #setTreatBaseUrlsAsLocal(Set)
	 * @see #setAllowExternalReferences(boolean)
	 */
	public void setAllowExternalReferences(boolean theAllowExternalReferences) {
		myAllowExternalReferences = theAllowExternalReferences;
	}

	/**
	 * This setting may be used to advise the server that any references found in
	 * resources that have any of the base URLs given here will be replaced with
	 * simple local references.
	 * <p>
	 * For example, if the set contains the value <code>http://example.com/base/</code>
	 * and a resource is submitted to the server that contains a reference to
	 * <code>http://example.com/base/Patient/1</code>, the server will automatically
	 * convert this reference to <code>Patient/1</code>
	 * </p>
	 * <p>
	 * Note that this property has different behaviour from {@link StorageSettings#getTreatReferencesAsLogical()}
	 * </p>
	 *
	 * @see #getTreatReferencesAsLogical()
	 */
	public Set<String> getTreatBaseUrlsAsLocal() {
		return myTreatBaseUrlsAsLocal;
	}

	/**
	 * This setting may be used to advise the server that any references found in
	 * resources that have any of the base URLs given here will be replaced with
	 * simple local references.
	 * <p>
	 * For example, if the set contains the value <code>http://example.com/base/</code>
	 * and a resource is submitted to the server that contains a reference to
	 * <code>http://example.com/base/Patient/1</code>, the server will automatically
	 * convert this reference to <code>Patient/1</code>
	 * </p>
	 *
	 * @param theTreatBaseUrlsAsLocal The set of base URLs. May be <code>null</code>, which
	 *                                means no references will be treated as external
	 */
	public void setTreatBaseUrlsAsLocal(Set<String> theTreatBaseUrlsAsLocal) {
		if (theTreatBaseUrlsAsLocal != null) {
			for (String next : theTreatBaseUrlsAsLocal) {
				validateTreatBaseUrlsAsLocal(next);
			}
		}

		HashSet<String> treatBaseUrlsAsLocal = new HashSet<>();
		for (String next : defaultIfNull(theTreatBaseUrlsAsLocal, new HashSet<String>())) {
			while (next.endsWith("/")) {
				next = next.substring(0, next.length() - 1);
			}
			treatBaseUrlsAsLocal.add(next);
		}
		myTreatBaseUrlsAsLocal = treatBaseUrlsAsLocal;
	}

	/**
	 * Add a value to the {@link #setTreatReferencesAsLogical(Set) logical references list}.
	 *
	 * @see #setTreatReferencesAsLogical(Set)
	 */
	public void addTreatReferencesAsLogical(String theTreatReferencesAsLogical) {
		validateTreatBaseUrlsAsLocal(theTreatReferencesAsLogical);

		if (myTreatReferencesAsLogical == null) {
			myTreatReferencesAsLogical = new HashSet<>();
		}
		myTreatReferencesAsLogical.add(theTreatReferencesAsLogical);
	}

	/**
	 * This setting may be used to advise the server that any references found in
	 * resources that have any of the base URLs given here will be treated as logical
	 * references instead of being treated as real references.
	 * <p>
	 * A logical reference is a reference which is treated as an identifier, and
	 * does not neccesarily resolve. See <a href="http://hl7.org/fhir/references.html">references</a> for
	 * a description of logical references. For example, the valueset
	 * <a href="http://hl7.org/fhir/valueset-quantity-comparator.html">valueset-quantity-comparator</a> is a logical
	 * reference.
	 * </p>
	 * <p>
	 * Values for this field may take either of the following forms:
	 * </p>
	 * <ul>
	 * <li><code>http://example.com/some-url</code> <b>(will be matched exactly)</b></li>
	 * <li><code>http://example.com/some-base*</code> <b>(will match anything beginning with the part before the *)</b></li>
	 * </ul>
	 *
	 * @see #DEFAULT_LOGICAL_BASE_URLS Default values for this property
	 */
	public Set<String> getTreatReferencesAsLogical() {
		return myTreatReferencesAsLogical;
	}

	/**
	 * This setting may be used to advise the server that any references found in
	 * resources that have any of the base URLs given here will be treated as logical
	 * references instead of being treated as real references.
	 * <p>
	 * A logical reference is a reference which is treated as an identifier, and
	 * does not neccesarily resolve. See <a href="http://hl7.org/fhir/references.html">references</a> for
	 * a description of logical references. For example, the valueset
	 * <a href="http://hl7.org/fhir/valueset-quantity-comparator.html">valueset-quantity-comparator</a> is a logical
	 * reference.
	 * </p>
	 * <p>
	 * Values for this field may take either of the following forms:
	 * </p>
	 * <ul>
	 * <li><code>http://example.com/some-url</code> <b>(will be matched exactly)</b></li>
	 * <li><code>http://example.com/some-base*</code> <b>(will match anything beginning with the part before the *)</b></li>
	 * </ul>
	 *
	 * @see #DEFAULT_LOGICAL_BASE_URLS Default values for this property
	 */
	public StorageSettings setTreatReferencesAsLogical(Set<String> theTreatReferencesAsLogical) {
		myTreatReferencesAsLogical = theTreatReferencesAsLogical;
		return this;
	}

	/**
	 * <p>
	 * Should searches use the integer field {@code SP_VALUE_LOW_DATE_ORDINAL} and {@code SP_VALUE_HIGH_DATE_ORDINAL} in
	 * {@link ResourceIndexedSearchParamDate} when resolving searches where all predicates are using
	 * precision of {@link TemporalPrecisionEnum#DAY}.
	 * <p>
	 * For example, if enabled, the search of {@code Observation?date=2020-02-25} will cause the date to be collapsed down to an
	 * integer representing the ordinal date {@code 20200225}. It would then be compared against {@link ResourceIndexedSearchParamDate#getValueLowDateOrdinal()}
	 * and {@link ResourceIndexedSearchParamDate#getValueHighDateOrdinal()}
	 * </p>
	 * Default is {@literal true} beginning in HAPI FHIR 5.0.0
	 * </p>
	 *
	 * @since 5.0.0
	 */
	public boolean getUseOrdinalDatesForDayPrecisionSearches() {
		return myUseOrdinalDatesForDayPrecisionSearches;
	}

	/**
	 * <p>
	 * Should searches use the integer field {@code SP_VALUE_LOW_DATE_ORDINAL} and {@code SP_VALUE_HIGH_DATE_ORDINAL} in
	 * {@link ResourceIndexedSearchParamDate} when resolving searches where all predicates are using
	 * precision of {@link TemporalPrecisionEnum#DAY}.
	 * <p>
	 * For example, if enabled, the search of {@code Observation?date=2020-02-25} will cause the date to be collapsed down to an
	 * ordinal {@code 20200225}. It would then be compared against {@link ResourceIndexedSearchParamDate#getValueLowDateOrdinal()}
	 * and {@link ResourceIndexedSearchParamDate#getValueHighDateOrdinal()}
	 * </p>
	 * Default is {@literal true} beginning in HAPI FHIR 5.0.0
	 * </p>
	 *
	 * @since 5.0.0
	 */
	public void setUseOrdinalDatesForDayPrecisionSearches(boolean theUseOrdinalDates) {
		myUseOrdinalDatesForDayPrecisionSearches = theUseOrdinalDates;
	}

	/**
	 * If set to <code>true</code> (default is <code>false</code>), when indexing SearchParameter values for token SearchParameter,
	 * the string component to support the <code>:text</code> modifier will be disabled. This means that the following fields
	 * will not be indexed for tokens:
	 * <ul>
	 *    <li>CodeableConcept.text</li>
	 *    <li>Coding.display</li>
	 *    <li>Identifier.use.text</li>
	 * </ul>
	 *
	 * @since 5.0.0
	 */
	public boolean isSuppressStringIndexingInTokens() {
		return mySuppressStringIndexingInTokens;
	}

	/**
	 * If set to <code>true</code> (default is <code>false</code>), when indexing SearchParameter values for token SearchParameter,
	 * the string component to support the <code>:text</code> modifier will be disabled. This means that the following fields
	 * will not be indexed for tokens:
	 * <ul>
	 *    <li>CodeableConcept.text</li>
	 *    <li>Coding.display</li>
	 *    <li>Identifier.use.text</li>
	 * </ul>
	 *
	 * @since 5.0.0
	 */
	public void setSuppressStringIndexingInTokens(boolean theSuppressStringIndexingInTokens) {
		mySuppressStringIndexingInTokens = theSuppressStringIndexingInTokens;
	}

	/**
	 * When indexing a Period (e.g. Encounter.period) where the period has an upper bound
	 * but not a lower bound, a canned "start of time" value can be used as the lower bound
	 * in order to allow range searches to correctly identify all values in the range.
	 * <p>
	 * The default value for this is {@link #DEFAULT_PERIOD_INDEX_START_OF_TIME} which
	 * is probably good enough for almost any application, but this can be changed if
	 * needed.
	 * </p>
	 * <p>
	 * Note the following database documented limitations:
	 *    <ul>
	 *       <li>JDBC Timestamp Datatype Low Value -4713 and High Value 9999</li>
	 *       <li>MySQL 8: the range for DATETIME values is '1000-01-01 00:00:00.000000' to '9999-12-31 23:59:59.999999`</li>
	 *       <li>Postgresql 12: Timestamp [without time zone] Low Value 4713 BC and High Value 294276 AD</li>
	 *       <li>Oracle: Timestamp Low Value 4712 BC and High Value 9999 CE</li>
	 *       <li>H2: datetime2 Low Value -4713 and High Value 9999</li>
	 *     </ul>
	 * </p>
	 *
	 * @see #getPeriodIndexEndOfTime()
	 * @since 5.1.0
	 */
	public IPrimitiveType<Date> getPeriodIndexStartOfTime() {
		return myPeriodIndexStartOfTime;
	}

	/**
	 * When indexing a Period (e.g. Encounter.period) where the period has an upper bound
	 * but not a lower bound, a canned "start of time" value can be used as the lower bound
	 * in order to allow range searches to correctly identify all values in the range.
	 * <p>
	 * The default value for this is {@link #DEFAULT_PERIOD_INDEX_START_OF_TIME} which
	 * is probably good enough for almost any application, but this can be changed if
	 * needed.
	 * </p>
	 * <p>
	 * Note the following database documented limitations:
	 *    <ul>
	 *       <li>JDBC Timestamp Datatype Low Value -4713 and High Value 9999</li>
	 *       <li>MySQL 8: the range for DATETIME values is '1000-01-01 00:00:00.000000' to '9999-12-31 23:59:59.999999`</li>
	 *       <li>Postgresql 12: Timestamp [without time zone] Low Value 4713 BC and High Value 294276 AD</li>
	 *       <li>Oracle: Timestamp Low Value 4712 BC and High Value 9999 CE</li>
	 *       <li>H2: datetime2 Low Value -4713 and High Value 9999</li>
	 *     </ul>
	 * </p>
	 *
	 * @see #getPeriodIndexEndOfTime()
	 * @since 5.1.0
	 */
	public void setPeriodIndexStartOfTime(IPrimitiveType<Date> thePeriodIndexStartOfTime) {
		Validate.notNull(thePeriodIndexStartOfTime, "thePeriodIndexStartOfTime must not be null");
		myPeriodIndexStartOfTime = thePeriodIndexStartOfTime;
	}

	/**
	 * When indexing a Period (e.g. Encounter.period) where the period has a lower bound
	 * but not an upper bound, a canned "end of time" value can be used as the upper bound
	 * in order to allow range searches to correctly identify all values in the range.
	 * <p>
	 * The default value for this is {@link #DEFAULT_PERIOD_INDEX_START_OF_TIME} which
	 * is probably good enough for almost any application, but this can be changed if
	 * needed.
	 * </p>
	 * <p>
	 * Note the following database documented limitations:
	 *    <ul>
	 *       <li>JDBC Timestamp Datatype Low Value -4713 and High Value 9999</li>
	 *       <li>MySQL 8: the range for DATETIME values is '1000-01-01 00:00:00.000000' to '9999-12-31 23:59:59.999999`</li>
	 *       <li>Postgresql 12: Timestamp [without time zone] Low Value 4713 BC and High Value 294276 AD</li>
	 *       <li>Oracle: Timestamp Low Value 4712 BC and High Value 9999 CE</li>
	 *       <li>H2: datetime2 Low Value -4713 and High Value 9999</li>
	 *     </ul>
	 * </p>
	 *
	 * @see #getPeriodIndexStartOfTime()
	 * @since 5.1.0
	 */
	public IPrimitiveType<Date> getPeriodIndexEndOfTime() {
		return myPeriodIndexEndOfTime;
	}

	/**
	 * When indexing a Period (e.g. Encounter.period) where the period has an upper bound
	 * but not a lower bound, a canned "start of time" value can be used as the lower bound
	 * in order to allow range searches to correctly identify all values in the range.
	 * <p>
	 * The default value for this is {@link #DEFAULT_PERIOD_INDEX_START_OF_TIME} which
	 * is probably good enough for almost any application, but this can be changed if
	 * needed.
	 * </p>
	 * <p>
	 * Note the following database documented limitations:
	 *    <ul>
	 *       <li>JDBC Timestamp Datatype Low Value -4713 and High Value 9999</li>
	 *       <li>MySQL 8: the range for DATETIME values is '1000-01-01 00:00:00.000000' to '9999-12-31 23:59:59.999999`</li>
	 *       <li>Postgresql 12: Timestamp [without time zone] Low Value 4713 BC and High Value 294276 AD</li>
	 *       <li>Oracle: Timestamp Low Value 4712 BC and High Value 9999 CE</li>
	 *       <li>H2: datetime2 Low Value -4713 and High Value 9999</li>
	 *     </ul>
	 * </p>
	 *
	 * @see #getPeriodIndexStartOfTime()
	 * @since 5.1.0
	 */
	public void setPeriodIndexEndOfTime(IPrimitiveType<Date> thePeriodIndexEndOfTime) {
		Validate.notNull(thePeriodIndexEndOfTime, "thePeriodIndexEndOfTime must not be null");
		myPeriodIndexEndOfTime = thePeriodIndexEndOfTime;
	}

	/**
	 * Toggles whether Quantity searches support value normalization when using valid UCUM coded values.
	 *
	 * <p>
	 * The default value is {@link NormalizedQuantitySearchLevel#NORMALIZED_QUANTITY_SEARCH_NOT_SUPPORTED} which is current behavior.
	 * </p>
	 * <p>
	 * Here is the UCUM service support level
	 *    <ul>
	 *       <li>{@link NormalizedQuantitySearchLevel#NORMALIZED_QUANTITY_SEARCH_NOT_SUPPORTED}, default, Quantity is stored in {@link ResourceIndexedSearchParamQuantity} only and it is used by searching.</li>
	 *       <li>{@link NormalizedQuantitySearchLevel#NORMALIZED_QUANTITY_STORAGE_SUPPORTED}, Quantity is stored in both {@link ResourceIndexedSearchParamQuantity} and {@link ResourceIndexedSearchParamQuantityNormalized}, but {@link ResourceIndexedSearchParamQuantity} is used by searching.</li>
	 *       <li>{@link NormalizedQuantitySearchLevel#NORMALIZED_QUANTITY_SEARCH_SUPPORTED}, Quantity is stored in both {@link ResourceIndexedSearchParamQuantity} and {@link ResourceIndexedSearchParamQuantityNormalized}, {@link ResourceIndexedSearchParamQuantityNormalized} is used by searching.</li>
	 *     </ul>
	 * </p>
	 *
	 * @since 5.3.0
	 */
	public NormalizedQuantitySearchLevel getNormalizedQuantitySearchLevel() {
		return myNormalizedQuantitySearchLevel;
	}

	/**
	 * Toggles whether Quantity searches support value normalization when using valid UCUM coded values.
	 *
	 * <p>
	 * The default value is {@link NormalizedQuantitySearchLevel#NORMALIZED_QUANTITY_SEARCH_NOT_SUPPORTED} which is current behavior.
	 * </p>
	 * <p>
	 * Here is the UCUM service support level
	 *    <ul>
	 *       <li>{@link NormalizedQuantitySearchLevel#NORMALIZED_QUANTITY_SEARCH_NOT_SUPPORTED}, default, Quantity is stored in {@link ResourceIndexedSearchParamQuantity} only and it is used by searching.</li>
	 *       <li>{@link NormalizedQuantitySearchLevel#NORMALIZED_QUANTITY_STORAGE_SUPPORTED}, Quantity is stored in both {@link ResourceIndexedSearchParamQuantity} and {@link ResourceIndexedSearchParamQuantityNormalized}, but {@link ResourceIndexedSearchParamQuantity} is used by searching.</li>
	 *       <li>{@link NormalizedQuantitySearchLevel#NORMALIZED_QUANTITY_SEARCH_SUPPORTED}, Quantity is stored in both {@link ResourceIndexedSearchParamQuantity} and {@link ResourceIndexedSearchParamQuantityNormalized}, {@link ResourceIndexedSearchParamQuantityNormalized} is used by searching.</li>
	 *     </ul>
	 * </p>
	 *
	 * @since 5.3.0
	 */
	public void setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel theNormalizedQuantitySearchLevel) {
		myNormalizedQuantitySearchLevel = theNormalizedQuantitySearchLevel;
	}

	/**
	 * When set with resource paths (e.g. <code>"Observation.subject"</code>), any references found at the given paths
	 * will automatically have versions appended. The version used will be the current version of the given resource.
	 *
	 * @since 5.3.0
	 */
	public Set<String> getAutoVersionReferenceAtPaths() {
		return myAutoVersionReferenceAtPaths;
	}

	/**
	 * When set with resource paths (e.g. <code>"Observation.subject"</code>), any references found at the given paths
	 * will automatically have versions appended. The version used will be the current version of the given resource.
	 * <p>
	 * Versions will only be added if the reference does not already have a version, so any versioned references
	 * supplied by the client will take precedence over the automatic current version.
	 * </p>
	 * <p>
	 * Note that for this setting to be useful, the {@link ParserOptions}
	 * {@link ParserOptions#getDontStripVersionsFromReferencesAtPaths() DontStripVersionsFromReferencesAtPaths}
	 * option must also be set.
	 * </p>
	 *
	 * @param thePaths A collection of reference paths for which the versions will be appended automatically
	 *                 when serializing, e.g. "Patient.managingOrganization" or "AuditEvent.object.reference". Note that
	 *                 only resource name and field names with dots separating is allowed here (no repetition
	 *                 indicators, FluentPath expressions, etc.)
	 * @since 5.3.0
	 */
	public void setAutoVersionReferenceAtPaths(String... thePaths) {
		Set<String> paths = Collections.emptySet();
		if (thePaths != null) {
			paths = new HashSet<>(Arrays.asList(thePaths));
		}
		setAutoVersionReferenceAtPaths(paths);
	}

	/**
	 * When set with resource paths (e.g. <code>"Observation.subject"</code>), any references found at the given paths
	 * will automatically have versions appended. The version used will be the current version of the given resource.
	 * <p>
	 * Versions will only be added if the reference does not already have a version, so any versioned references
	 * supplied by the client will take precedence over the automatic current version.
	 * </p>
	 * <p>
	 * Note that for this setting to be useful, the {@link ParserOptions}
	 * {@link ParserOptions#getDontStripVersionsFromReferencesAtPaths() DontStripVersionsFromReferencesAtPaths}
	 * option must also be set
	 * </p>
	 *
	 * @param thePaths A collection of reference paths for which the versions will be appended automatically
	 *                 when serializing, e.g. "Patient.managingOrganization" or "AuditEvent.object.reference". Note that
	 *                 only resource name and field names with dots separating is allowed here (no repetition
	 *                 indicators, FluentPath expressions, etc.)
	 * @since 5.3.0
	 */
	public void setAutoVersionReferenceAtPaths(Set<String> thePaths) {
		Set<String> paths = defaultIfNull(thePaths, Collections.emptySet());
		Map<String, Set<String>> byType = new HashMap<>();
		for (String nextPath : paths) {
			int doxIdx = nextPath.indexOf('.');
			Validate.isTrue(doxIdx > 0, "Invalid path for auto-version reference at path: %s", nextPath);
			String type = nextPath.substring(0, doxIdx);
			byType.computeIfAbsent(type, t -> new HashSet<>()).add(nextPath);
		}

		myAutoVersionReferenceAtPaths = paths;
		myTypeToAutoVersionReferenceAtPaths = byType;
	}

	/**
	 * Returns a sub-collection of {@link #getAutoVersionReferenceAtPaths()} containing only paths
	 * for the given resource type.
	 *
	 * @since 5.3.0
	 */
	public Set<String> getAutoVersionReferenceAtPathsByResourceType(String theResourceType) {
		Validate.notEmpty(theResourceType, "theResourceType must not be null or empty");
		Set<String> retVal = myTypeToAutoVersionReferenceAtPaths.get(theResourceType);
		retVal = defaultIfNull(retVal, Collections.emptySet());
		return retVal;
	}

	/**
	 * Should searches with <code>_include</code> respect versioned references, and pull the specific requested version.
	 * This may have performance impacts on heavily loaded systems.
	 *
	 * @since 5.3.0
	 */
	public boolean isRespectVersionsForSearchIncludes() {
		return myRespectVersionsForSearchIncludes;
	}

	/**
	 * Should searches with <code>_include</code> respect versioned references, and pull the specific requested version.
	 * This may have performance impacts on heavily loaded systems.
	 *
	 * @since 5.3.0
	 */
	public void setRespectVersionsForSearchIncludes(boolean theRespectVersionsForSearchIncludes) {
		myRespectVersionsForSearchIncludes = theRespectVersionsForSearchIncludes;
	}

	/**
	 * If enabled, "Uplifted Refchains" will be enabled. This feature causes
	 * HAPI FHIR to generate indexes for stored resources that include the current
	 * value of the target of a chained reference, such as "Encounter?subject.name".
	 *
	 * @since 6.6.0
	 */
	public boolean isIndexOnUpliftedRefchains() {
		return myIndexOnUpliftedRefchains;
	}

	/**
	 * If enabled, "Uplifted Refchains" will be enabled. This feature causes
	 * HAPI FHIR to generate indexes for stored resources that include the current
	 * value of the target of a chained reference, such as "Encounter?subject.name".
	 *
	 * @since 6.6.0
	 */
	public void setIndexOnUpliftedRefchains(boolean theIndexOnUpliftedRefchains) {
		myIndexOnUpliftedRefchains = theIndexOnUpliftedRefchains;
	}

	/**
	 * Should indexing and searching on contained resources be enabled on this server.
	 * This may have performance impacts, and should be enabled only if it is needed. Default is <code>false</code>.
	 *
	 * @since 5.4.0
	 */
	public boolean isIndexOnContainedResources() {
		return myIndexOnContainedResources;
	}

	/**
	 * Should indexing and searching on contained resources be enabled on this server.
	 * This may have performance impacts, and should be enabled only if it is needed. Default is <code>false</code>.
	 *
	 * @since 5.4.0
	 */
	public void setIndexOnContainedResources(boolean theIndexOnContainedResources) {
		myIndexOnContainedResources = theIndexOnContainedResources;
	}

	/**
	 * Should recursive indexing and searching on contained resources be enabled on this server.
	 * This may have performance impacts, and should be enabled only if it is needed. Default is <code>false</code>.
	 *
	 * @since 5.6.0
	 */
	public boolean isIndexOnContainedResourcesRecursively() {
		return myIndexOnContainedResourcesRecursively;
	}

	/**
	 * Should recursive indexing and searching on contained resources be enabled on this server.
	 * This may have performance impacts, and should be enabled only if it is needed. Default is <code>false</code>.
	 *
	 * @since 5.6.0
	 */
	public void setIndexOnContainedResourcesRecursively(boolean theIndexOnContainedResourcesRecursively) {
		myIndexOnContainedResourcesRecursively = theIndexOnContainedResourcesRecursively;
	}

	/**
	 * If this is disabled by setting this to {@literal false} (default is {@literal true}),
	 * the server will not automatically implement and support search parameters that
	 * are not explcitly created in the repository.
	 * <p>
	 * Disabling this can have a dramatic improvement on performance (especially write performance)
	 * in servers that only need to support a small number of search parameters, or no search parameters at all.
	 * Disabling this obviously reduces the options for searching however.
	 * </p>
	 *
	 * @since 5.7.0
	 */
	public boolean isAutoSupportDefaultSearchParams() {
		return myAutoSupportDefaultSearchParams;
	}

	/**
	 * If this is disabled by setting this to {@literal false} (default is {@literal true}),
	 * the server will not automatically implement and support search parameters that
	 * are not explcitly created in the repository.
	 * <p>
	 * Disabling this can have a dramatic improvement on performance (especially write performance)
	 * in servers that only need to support a small number of search parameters, or no search parameters at all.
	 * Disabling this obviously reduces the options for searching however.
	 * </p>
	 *
	 * @since 5.7.0
	 */
	public void setAutoSupportDefaultSearchParams(boolean theAutoSupportDefaultSearchParams) {
		myAutoSupportDefaultSearchParams = theAutoSupportDefaultSearchParams;
	}

	/**
	 * @return Should the {@literal _lamguage} SearchParameter be supported on this server? Defaults to {@literal false}.
	 * @since 7.0.0
	 */
	public boolean isLanguageSearchParameterEnabled() {
		return myLanguageSearchParameterEnabled;
	}

	/**
	 * Should the {@literal _lamguage} SearchParameter be supported on this server? Defaults to {@literal false}.
	 *
	 * @since 7.0.0
	 */
	public void setLanguageSearchParameterEnabled(boolean theLanguageSearchParameterEnabled) {
		myLanguageSearchParameterEnabled = theLanguageSearchParameterEnabled;
	}

	/**
	 * @return true if the filter is enabled for resources installed via package installer, false otherwise
	 * @since 7.0.0
	 */
	public boolean isValidateResourceStatusForPackageUpload() {
		return myValidateResourceStatusForPackageUpload;
	}

	/**
	 * Should resources being installed via package installer be filtered.
	 * @since 7.0.0
	 */
	public void setValidateResourceStatusForPackageUpload(boolean theValidateResourceStatusForPackageUpload) {
		myValidateResourceStatusForPackageUpload = theValidateResourceStatusForPackageUpload;
	}

	private static void validateTreatBaseUrlsAsLocal(String theUrl) {
		Validate.notBlank(theUrl, "Base URL must not be null or empty");

		int starIdx = theUrl.indexOf('*');
		if (starIdx != -1) {
			if (starIdx != theUrl.length() - 1) {
				throw new IllegalArgumentException(Msg.code(1525)
						+ "Base URL wildcard character (*) can only appear at the end of the string: " + theUrl);
			}
		}
	}

	public enum IndexEnabledEnum {
		ENABLED,
		DISABLED
	}
}
