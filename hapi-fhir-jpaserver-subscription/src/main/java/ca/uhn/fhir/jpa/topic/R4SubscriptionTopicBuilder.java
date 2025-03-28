/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.topic;

import ca.uhn.fhir.subscription.SubscriptionConstants;
import org.hl7.fhir.r4.model.Basic;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Enumerations.PublicationStatus;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.MarkdownType;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UriType;

import java.util.Date;

/**
 * Builder class for creating and configuring FHIR R4 SubscriptionTopic resources.
 * <p/>
 * In R4, SubscriptionTopic is represented as a Basic resource with extensions,
 * following the pattern outlined in the FHIR Subscriptions Backport implementation guide.
 * This builder provides a fluent API to create these resources without needing to
 * handle extension management directly.
 *
 * @see SubscriptionTopicCanonicalizer For conversion between R4 Basic and R5 SubscriptionTopic
 */
public class R4SubscriptionTopicBuilder {

	private final Basic myTopic;
	private Extension myCurrentResourceTrigger;
	private Extension myCurrentCanFilterBy;
	private Extension myCurrentNotificationShape;

	/**
	 * Creates a new builder with a Basic resource having the SubscriptionTopic code
	 */
	public R4SubscriptionTopicBuilder() {
		myTopic = new Basic();

		// Set the Basic.code to indicate this is a SubscriptionTopic
		CodeableConcept code = new CodeableConcept();
		code.addCoding(new Coding().setSystem("http://hl7.org/fhir/fhir-types").setCode("SubscriptionTopic"));
		myTopic.setCode(code);
	}

	/**
	 * Set the logical ID of the SubscriptionTopic
	 */
	public R4SubscriptionTopicBuilder setId(String theId) {
		myTopic.setId(theId);
		return this;
	}

	/**
	 * Set the canonical URL of the topic
	 */
	public R4SubscriptionTopicBuilder setUrl(String theUrl) {
		addExtension(SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_TOPIC_URL, new UriType(theUrl));
		return this;
	}

	/**
	 * Set the version of the topic
	 */
	public R4SubscriptionTopicBuilder setVersion(String theVersion) {
		addExtension(SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_TOPIC_VERSION, new StringType(theVersion));
		return this;
	}

	/**
	 * Set the name of the topic
	 */
	public R4SubscriptionTopicBuilder setName(String theName) {
		addExtension(SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_TOPIC_NAME, new StringType(theName));
		return this;
	}

	/**
	 * Set the title of the topic
	 */
	public R4SubscriptionTopicBuilder setTitle(String theTitle) {
		addExtension(SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_TOPIC_TITLE, new StringType(theTitle));
		return this;
	}

	/**
	 * Set the date the topic was last updated
	 */
	public R4SubscriptionTopicBuilder setDate(Date theDate) {
		addExtension(SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_TOPIC_DATE, new DateTimeType(theDate));
		return this;
	}

	/**
	 * Set the description of the topic
	 */
	public R4SubscriptionTopicBuilder setDescription(String theDescription) {
		addExtension(
				SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_TOPIC_DESCRIPTION, new MarkdownType(theDescription));
		return this;
	}

	/**
	 * Set the status of the topic (note: uses modifier extension)
	 */
	public R4SubscriptionTopicBuilder setStatus(PublicationStatus theStatus) {
		Extension statusExtension = new Extension()
				.setUrl(SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_TOPIC_STATUS)
				.setValue(new StringType(theStatus.toCode()));
		myTopic.addModifierExtension(statusExtension);
		return this;
	}

	/**
	 * Start defining a resource trigger
	 */
	public R4SubscriptionTopicBuilder addResourceTrigger() {
		myCurrentResourceTrigger =
				new Extension().setUrl(SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_RESOURCE_TRIGGER);
		myTopic.addExtension(myCurrentResourceTrigger);
		return this;
	}

	/**
	 * Set the description for the current resource trigger
	 */
	public R4SubscriptionTopicBuilder setResourceTriggerDescription(String theDescription) {
		checkCurrentResourceTrigger();
		addNestedExtension(
				myCurrentResourceTrigger,
				SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_DESCRIPTION,
				new MarkdownType(theDescription));
		return this;
	}

	/**
	 * Set the resource type for the current resource trigger
	 */
	public R4SubscriptionTopicBuilder setResourceTriggerResource(String theResourceType) {
		checkCurrentResourceTrigger();
		addNestedExtension(
				myCurrentResourceTrigger,
				SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_RESOURCE,
				new UriType(theResourceType));
		return this;
	}

	/**
	 * Add a supported interaction to the current resource trigger
	 */
	public R4SubscriptionTopicBuilder addResourceTriggerSupportedInteraction(String theInteractionCode) {
		checkCurrentResourceTrigger();
		addNestedExtension(
				myCurrentResourceTrigger,
				SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_SUPPORTED_INTERACTION,
				new StringType(theInteractionCode));
		return this;
	}

	/**
	 * Add FHIRPath criteria to the current resource trigger
	 */
	public R4SubscriptionTopicBuilder setResourceTriggerFhirPathCriteria(String theFhirPathExpression) {
		checkCurrentResourceTrigger();
		addNestedExtension(
				myCurrentResourceTrigger,
				SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_FHIRPATH_CRITERIA,
				new StringType(theFhirPathExpression));
		return this;
	}

	/**
	 * Start defining a query criteria extension for the current resource trigger
	 */
	public R4SubscriptionTopicBuilder addResourceTriggerQueryCriteria() {
		checkCurrentResourceTrigger();
		Extension queryCriteria =
				new Extension().setUrl(SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_QUERY_CRITERIA);
		myCurrentResourceTrigger.addExtension(queryCriteria);
		return this;
	}

	/**
	 * Set the previous query string for the current resource trigger's query criteria
	 */
	public R4SubscriptionTopicBuilder setResourceTriggerQueryCriteriaPrevious(String thePreviousQuery) {
		checkCurrentResourceTrigger();
		Extension queryCriteria = getOrCreateQueryCriteriaExtension();
		queryCriteria.addExtension(new Extension()
				.setUrl(SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_QUERY_CRITERIA_PREVIOUS)
				.setValue(new StringType(thePreviousQuery)));
		return this;
	}

	/**
	 * Set the current query string for the current resource trigger's query criteria
	 */
	public R4SubscriptionTopicBuilder setResourceTriggerQueryCriteriaCurrent(String theCurrentQuery) {
		checkCurrentResourceTrigger();
		Extension queryCriteria = getOrCreateQueryCriteriaExtension();
		queryCriteria.addExtension(new Extension()
				.setUrl(SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_QUERY_CRITERIA_CURRENT)
				.setValue(new StringType(theCurrentQuery)));
		return this;
	}

	/**
	 * Set requireBoth flag for the current resource trigger's query criteria
	 */
	public R4SubscriptionTopicBuilder setResourceTriggerQueryCriteriaRequireBoth(boolean theRequireBoth) {
		checkCurrentResourceTrigger();
		Extension queryCriteria = getOrCreateQueryCriteriaExtension();
		queryCriteria.addExtension(new Extension()
				.setUrl(SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_QUERY_CRITERIA_REQUIRE_BOTH)
				.setValue(new BooleanType(theRequireBoth)));
		return this;
	}

	/**
	 * Start defining a can-filter-by extension
	 */
	public R4SubscriptionTopicBuilder addCanFilterBy() {
		myCurrentCanFilterBy = new Extension().setUrl(SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_CAN_FILTER_BY);
		myTopic.addExtension(myCurrentCanFilterBy);
		return this;
	}

	/**
	 * Set the description for the current can-filter-by
	 */
	public R4SubscriptionTopicBuilder setCanFilterByDescription(String theDescription) {
		checkCurrentCanFilterBy();
		addNestedExtension(
				myCurrentCanFilterBy,
				SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_DESCRIPTION,
				new MarkdownType(theDescription));
		return this;
	}

	/**
	 * Set the resource type for the current can-filter-by
	 */
	public R4SubscriptionTopicBuilder setCanFilterByResource(String theResourceType) {
		checkCurrentCanFilterBy();
		addNestedExtension(
				myCurrentCanFilterBy,
				SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_RESOURCE,
				new UriType(theResourceType));
		return this;
	}

	/**
	 * Set the filter parameter for the current can-filter-by
	 */
	public R4SubscriptionTopicBuilder setCanFilterByParameter(String theFilterParameter) {
		checkCurrentCanFilterBy();
		addNestedExtension(
				myCurrentCanFilterBy,
				SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_FILTER_PARAMETER,
				new StringType(theFilterParameter));
		return this;
	}

	/**
	 * Start defining a notification shape extension
	 */
	public R4SubscriptionTopicBuilder addNotificationShape() {
		myCurrentNotificationShape =
				new Extension().setUrl(SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_NOTIFICATION_SHAPE);
		myTopic.addExtension(myCurrentNotificationShape);
		return this;
	}

	/**
	 * Set the resource type for the current notification shape
	 */
	public R4SubscriptionTopicBuilder setNotificationShapeResource(String theResourceType) {
		checkCurrentNotificationShape();
		addNestedExtension(
				myCurrentNotificationShape,
				SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_RESOURCE,
				new UriType(theResourceType));
		return this;
	}

	/**
	 * Add an include parameter to the current notification shape
	 */
	public R4SubscriptionTopicBuilder addNotificationShapeInclude(String theIncludeParam) {
		checkCurrentNotificationShape();
		addNestedExtension(
				myCurrentNotificationShape,
				SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_INCLUDE,
				new StringType(theIncludeParam));
		return this;
	}

	/**
	 * Add a revInclude parameter to the current notification shape
	 */
	public R4SubscriptionTopicBuilder addNotificationShapeRevInclude(String theRevIncludeParam) {
		checkCurrentNotificationShape();
		addNestedExtension(
				myCurrentNotificationShape,
				SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_REVINCLUDE,
				new StringType(theRevIncludeParam));
		return this;
	}

	/**
	 * Return the built Basic resource representing the SubscriptionTopic
	 */
	public Basic build() {
		return myTopic;
	}

	// Helper methods

	private void checkCurrentResourceTrigger() {
		if (myCurrentResourceTrigger == null) {
			throw new IllegalStateException("No current resource trigger defined. Call addResourceTrigger() first.");
		}
	}

	private void checkCurrentCanFilterBy() {
		if (myCurrentCanFilterBy == null) {
			throw new IllegalStateException("No current can-filter-by defined. Call addCanFilterBy() first.");
		}
	}

	private void checkCurrentNotificationShape() {
		if (myCurrentNotificationShape == null) {
			throw new IllegalStateException(
					"No current notification shape defined. Call addNotificationShape() first.");
		}
	}

	private Extension getOrCreateQueryCriteriaExtension() {
		String queryExtensionUrl = SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_QUERY_CRITERIA;

		// Try to find existing extension
		return myCurrentResourceTrigger.getExtension().stream()
			.filter(extension -> queryExtensionUrl.equals(extension.getUrl()))
			.findFirst()
			.orElseGet(() -> {
				// Create and add new extension if none exists
				Extension queryCriteria = new Extension().setUrl(queryExtensionUrl);
				myCurrentResourceTrigger.addExtension(queryCriteria);
				return queryCriteria;
			});
	}

	private void addExtension(String theUrl, org.hl7.fhir.r4.model.Type theValue) {
		Extension extension = new Extension().setUrl(theUrl).setValue(theValue);
		myTopic.addExtension(extension);
	}

	private void addNestedExtension(Extension theParent, String theUrl, org.hl7.fhir.r4.model.Type theValue) {
		Extension nestedExtension = new Extension().setUrl(theUrl).setValue(theValue);
		theParent.addExtension(nestedExtension);
	}
}
