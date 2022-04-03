package ca.uhn.fhir.util;

/*-
 * #%L
 * HAPI FHIR - Core Library
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

public class HapiExtensions {

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
	 * For subscription, deliver a bundle containing a search result instead of just a single resource
	 */
	public static final String EXT_SUBSCRIPTION_PAYLOAD_SEARCH_CRITERIA = "http://hapifhir.io/fhir/StructureDefinition/subscription-payload-search-criteria";

	/**
	 * Message added to expansion valueset
	 */
	public static final String EXT_VALUESET_EXPANSION_MESSAGE = "http://hapifhir.io/fhir/StructureDefinition/valueset-expansion-message";

	/**
	 * Extension URL for extension on a SearchParameter indicating that text values should not be indexed
	 */
	public static final String EXT_SEARCHPARAM_TOKEN_SUPPRESS_TEXT_INDEXING = "http://hapifhir.io/fhir/StructureDefinition/searchparameter-token-suppress-text-index";
	/**
	 * <p>
	 * This extension represents the equivalent of the
	 * <code>Resource.meta.source</code> field within R4+ resources, and is for
	 * use in DSTU3 resources. It should contain a value of type <code>uri</code>
	 * and will be located on the Resource.meta
	 * </p>
	 */
	public static final String EXT_META_SOURCE = "http://hapifhir.io/fhir/StructureDefinition/resource-meta-source";
	public static final String EXT_SP_UNIQUE = "http://hapifhir.io/fhir/StructureDefinition/sp-unique";

	/**
	 * URL for extension on a Phonetic String SearchParameter indicating that text values should be phonetically indexed with the named encoder
	 */
	public static final String EXT_SEARCHPARAM_PHONETIC_ENCODER = "http://hapifhir.io/fhir/StructureDefinition/searchparameter-phonetic-encoder";

	/**
	 * URL for boolean extension added to all placeholder resources
	 */
	public static final String EXT_RESOURCE_PLACEHOLDER = "http://hapifhir.io/fhir/StructureDefinition/resource-placeholder";

	/**
	 * URL for extension in a Group Bulk Export which identifies the golden patient of a given exported resource.
	 */
    public static final String ASSOCIATED_GOLDEN_RESOURCE_EXTENSION_URL = "https://hapifhir.org/associated-patient-golden-resource/";

	/**
	 * This extension provides an example value for a parameter value for
	 * a REST operation (eg for an OperationDefinition)
	 */
	public static final String EXT_OP_PARAMETER_EXAMPLE_VALUE = "http://hapifhir.io/fhir/StructureDefinition/op-parameter-example-value";

	/**
	 * This extension provides a way for subscribers to provide
	 * a "retry-count".
	 * If provided, subscriptions will be retried this many times
	 * (to a total of retry-count + 1 (for original attempt)
	 */
	public static final String EX_RETRY_COUNT = "http://hapifhir.io/fhir/StructureDefinition/subscription-delivery-retry-count";

	/**
	 * This extension provides a way for subscribers to indicate if DELETE messages must be sent (default is ignoring them)
	 */
	public static final String EX_SEND_DELETE_MESSAGES = "http://hapifhir.io/fhir/StructureDefinition/subscription-send-delete-messages";

	/**
	 * This entension allows subscriptions to be marked as cross partition and with correct settings, listen to incoming resources from all partitions.
	 */
	public static final String EXTENSION_SUBSCRIPTION_CROSS_PARTITION = "https://smilecdr.com/fhir/ns/StructureDefinition/subscription-cross-partition";

	/**
	 * Non instantiable
	 */
	private HapiExtensions() {
	}


}
