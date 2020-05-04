package ca.uhn.fhir.jpa.model.util;

/*-
 * #%L
 * HAPI FHIR Model
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.rest.api.Constants;

public class JpaConstants {

	/**
	 * Userdata key for tracking the fact that a resource ID was assigned by the server
	 */
	public static final String RESOURCE_ID_SERVER_ASSIGNED = JpaConstants.class.getName() + "_RESOURCE_ID_SERVER_ASSIGNED";
	/**
	 * Operation name for the $apply-codesystem-delta-add operation
	 */
	public static final String OPERATION_APPLY_CODESYSTEM_DELTA_ADD = "$apply-codesystem-delta-add";
	/**
	 * Operation name for the $apply-codesystem-delta-remove operation
	 */
	public static final String OPERATION_APPLY_CODESYSTEM_DELTA_REMOVE = "$apply-codesystem-delta-remove";
	/**
	 * Operation name for the $expunge operation
	 */
	public static final String OPERATION_EXPUNGE = "$expunge";
	/**
	 * Operation name for the $match operation
	 */
	public static final String OPERATION_MATCH = "$match";
	/**
	 * @deprecated Replace with {@link #OPERATION_EXPUNGE}
	 */
	@Deprecated
	public static final String OPERATION_NAME_EXPUNGE = OPERATION_EXPUNGE;
	/**
	 * Parameter name for the $expunge operation
	 */
	public static final String OPERATION_EXPUNGE_PARAM_LIMIT = "limit";
	/**
	 * Parameter name for the $expunge operation
	 */
	public static final String OPERATION_EXPUNGE_PARAM_EXPUNGE_DELETED_RESOURCES = "expungeDeletedResources";
	/**
	 * Parameter name for the $expunge operation
	 */
	public static final String OPERATION_EXPUNGE_PARAM_EXPUNGE_PREVIOUS_VERSIONS = "expungePreviousVersions";
	/**
	 * Parameter name for the $expunge operation
	 */
	public static final String OPERATION_EXPUNGE_PARAM_EXPUNGE_EVERYTHING = "expungeEverything";
	/**
	 * Output parameter name for the $expunge operation
	 */
	public static final String OPERATION_EXPUNGE_OUT_PARAM_EXPUNGE_COUNT = "count";
	/**
	 * Header name for the "X-Meta-Snapshot-Mode" header, which
	 * specifies that properties in meta (tags, profiles, security labels)
	 * should be treated as a snapshot, meaning that these things will
	 * be removed if they are nt explicitly included in updates
	 */
	public static final String HEADER_META_SNAPSHOT_MODE = "X-Meta-Snapshot-Mode";
	/**
	 * Operation name for the $lookup operation
	 */
	public static final String OPERATION_LOOKUP = "$lookup";
	/**
	 * Operation name for the $expand operation
	 */
	public static final String OPERATION_EXPAND = "$expand";
	/**
	 * Operation name for the $validate-code operation
	 */
	public static final String OPERATION_VALIDATE_CODE = "$validate-code";
	/**
	 * Operation name for the $get-resource-counts operation
	 */
	public static final String OPERATION_GET_RESOURCE_COUNTS = "$get-resource-counts";
	/**
	 * Operation name for the $meta operation
	 */
	public static final String OPERATION_META = "$meta";
	/**
	 * Operation name for the $validate operation
	 */
	// NB don't delete this, it's used in Smile as well, even though hapi-fhir-server uses the version from Constants.java
	public static final String OPERATION_VALIDATE = Constants.EXTOP_VALIDATE;
	/**
	 * Operation name for the $suggest-keywords operation
	 */
	public static final String OPERATION_SUGGEST_KEYWORDS = "$suggest-keywords";
	/**
	 * Operation name for the $everything operation
	 */
	public static final String OPERATION_EVERYTHING = "$everything";
	/**
	 * Operation name for the $process-message operation
	 */
	public static final String OPERATION_PROCESS_MESSAGE = "$process-message";
	/**
	 * Operation name for the $meta-delete operation
	 */
	public static final String OPERATION_META_DELETE = "$meta-delete";
	/**
	 * Operation name for the $meta-add operation
	 */
	public static final String OPERATION_META_ADD = "$meta-add";
	/**
	 * Operation name for the $translate operation
	 */
	public static final String OPERATION_TRANSLATE = "$translate";
	/**
	 * Operation name for the $document operation
	 */
	public static final String OPERATION_DOCUMENT = "$document";
	/**
	 * Trigger a subscription manually for a given resource
	 */
	public static final String OPERATION_TRIGGER_SUBSCRIPTION = "$trigger-subscription";
	/**
	 * Operation name for the "$subsumes" operation
	 */
	public static final String OPERATION_SUBSUMES = "$subsumes";
	/**
	 * Operation name for the "$snapshot" operation
	 */
	public static final String OPERATION_SNAPSHOT = "$snapshot";
	/**
	 * Operation name for the "$binary-access" operation
	 */
	public static final String OPERATION_BINARY_ACCESS_READ = "$binary-access-read";
	/**
	 * Operation name for the "$binary-access" operation
	 */
	public static final String OPERATION_BINARY_ACCESS_WRITE = "$binary-access-write";
	/**
	 * Operation name for the "$upload-external-code-system" operation
	 */
	public static final String OPERATION_UPLOAD_EXTERNAL_CODE_SYSTEM = "$upload-external-code-system";
	/**
	 * Operation name for the "$export" operation
	 */
	public static final String OPERATION_EXPORT = "$export";
	/**
	 * Operation name for the "$export-poll-status" operation
	 */
	public static final String OPERATION_EXPORT_POLL_STATUS = "$export-poll-status";
	/**
	 * <p>
	 * This extension should be of type <code>string</code> and should be
	 * placed on the <code>Subscription.channel</code> element
	 * </p>
	 */
	public static final String EXT_SUBSCRIPTION_SUBJECT_TEMPLATE = "http://hapifhir.io/fhir/StructureDefinition/subscription-email-subject-template";
	/**
	 * This extension URL indicates whether a REST HOOK delivery should
	 * include the version ID when delivering.
	 * <p>
	 * This extension should be of type <code>boolean</code> and should be
	 * placed on the <code>Subscription.channel</code> element.
	 * </p>
	 */
	public static final String EXT_SUBSCRIPTION_RESTHOOK_STRIP_VERSION_IDS = "http://hapifhir.io/fhir/StructureDefinition/subscription-resthook-strip-version-ids";
	/**
	 * This extension URL indicates whether a REST HOOK delivery should
	 * reload the resource and deliver the latest version always. This
	 * could be useful for example if a resource which triggers a
	 * subscription gets updated many times in short succession and there
	 * is no value in delivering the older versions.
	 * <p>
	 * Note that if the resource is now deleted, this may cause
	 * the delivery to be cancelled altogether.
	 * </p>
	 *
	 * <p>
	 * This extension should be of type <code>boolean</code> and should be
	 * placed on the <code>Subscription.channel</code> element.
	 * </p>
	 */
	public static final String EXT_SUBSCRIPTION_RESTHOOK_DELIVER_LATEST_VERSION = "http://hapifhir.io/fhir/StructureDefinition/subscription-resthook-deliver-latest-version";
	/**
	 * Indicate which strategy will be used to match this subscription
	 */
	public static final String EXT_SUBSCRIPTION_MATCHING_STRATEGY = "http://hapifhir.io/fhir/StructureDefinition/subscription-matching-strategy";
	/**
	 * <p>
	 * This extension should be of type <code>string</code> and should be
	 * placed on the <code>Subscription.channel</code> element
	 * </p>
	 */
	public static final String EXT_SUBSCRIPTION_EMAIL_FROM = "http://hapifhir.io/fhir/StructureDefinition/subscription-email-from";
	/**
	 * Extension ID for external binary references
	 */
	public static final String EXT_EXTERNALIZED_BINARY_ID = "http://hapifhir.io/fhir/StructureDefinition/externalized-binary-id";
	/**
	 * Placed in system-generated extensions
	 */
	public static final String EXTENSION_EXT_SYSTEMDEFINED = JpaConstants.class.getName() + "_EXTENSION_EXT_SYSTEMDEFINED";
	/**
	 * Message added to expansion valueset
	 */
	public static final String EXT_VALUESET_EXPANSION_MESSAGE = "http://hapifhir.io/fhir/StructureDefinition/valueset-expansion-message";
	/**
	 * Parameter for the $export operation
	 */
	public static final String PARAM_EXPORT_POLL_STATUS_JOB_ID = "_jobId";
	/**
	 * Parameter for the $export operation
	 */
	public static final String PARAM_EXPORT_OUTPUT_FORMAT = "_outputFormat";
	/**
	 * Parameter for the $export operation
	 */
	public static final String PARAM_EXPORT_TYPE = "_type";
	/**
	 * Parameter for the $export operation
	 */
	public static final String PARAM_EXPORT_SINCE = "_since";
	/**
	 * Parameter for the $export operation
	 */
	public static final String PARAM_EXPORT_TYPE_FILTER = "_typeFilter";

	/**
	 * Non-instantiable
	 */
	private JpaConstants() {
		// nothing
	}
}
