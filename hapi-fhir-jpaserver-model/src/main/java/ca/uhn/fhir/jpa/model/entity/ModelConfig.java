package ca.uhn.fhir.jpa.model.entity;

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

import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.dstu2.model.Subscription;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ModelConfig {
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

	public static final String DEFAULT_WEBSOCKET_CONTEXT_PATH = "/websocket";
	/**
	 * update setter javadoc if default changes
	 */
	private boolean myAllowContainsSearches = false;
	private boolean myAllowExternalReferences = false;
	private Set<String> myTreatBaseUrlsAsLocal = new HashSet<>();
	private Set<String> myTreatReferencesAsLogical = new HashSet<>(DEFAULT_LOGICAL_BASE_URLS);
	private boolean myDefaultSearchParamsCanBeOverridden = false;
	private Set<Subscription.SubscriptionChannelType> mySupportedSubscriptionTypes = new HashSet<>();
	private String myEmailFromAddress = "noreply@unknown.com";
	private String myWebsocketContextPath = DEFAULT_WEBSOCKET_CONTEXT_PATH;

	/**
	 * Constructor
	 */
	public ModelConfig() {
		super();
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
	 * The default value for this setting is {@code false}
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
	 * The default value for this setting is {@code false}
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
	 * Note that this property has different behaviour from {@link ModelConfig#getTreatReferencesAsLogical()}
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
		for (String next : ObjectUtils.defaultIfNull(theTreatBaseUrlsAsLocal, new HashSet<String>())) {
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
	public ModelConfig setTreatReferencesAsLogical(Set<String> theTreatReferencesAsLogical) {
		myTreatReferencesAsLogical = theTreatReferencesAsLogical;
		return this;
	}

	/**
	 * This setting indicates which subscription channel types are supported by the server.  Any subscriptions submitted
	 * to the server matching these types will be activated.
	 *
	 */
	public ModelConfig addSupportedSubscriptionType(Subscription.SubscriptionChannelType theSubscriptionChannelType) {
		mySupportedSubscriptionTypes.add(theSubscriptionChannelType);
		return this;
	}

	/**
	 * This setting indicates which subscription channel types are supported by the server.  Any subscriptions submitted
	 * to the server matching these types will be activated.
	 *
	 */
	public Set<Subscription.SubscriptionChannelType> getSupportedSubscriptionTypes() {
		return Collections.unmodifiableSet(mySupportedSubscriptionTypes);
	}

	@VisibleForTesting
	public void clearSupportedSubscriptionTypesForUnitTest() {
		mySupportedSubscriptionTypes.clear();
	}

	/**
	 * If e-mail subscriptions are supported, the From address used when sending e-mails
	 */

	public String getEmailFromAddress() {
		return myEmailFromAddress;
	}

	/**
	 * If e-mail subscriptions are supported, the From address used when sending e-mails
	 */

	public void setEmailFromAddress(String theEmailFromAddress) {
		myEmailFromAddress = theEmailFromAddress;
	}

	/**
	 * If websocket subscriptions are enabled, this specifies the context path that listens to them.  Default value "/websocket".
	 */

	public String getWebsocketContextPath() {
		return myWebsocketContextPath;
	}

	/**
	 * If websocket subscriptions are enabled, this specifies the context path that listens to them.  Default value "/websocket".
	 */

	public void setWebsocketContextPath(String theWebsocketContextPath) {
		myWebsocketContextPath = theWebsocketContextPath;
	}

	private static void validateTreatBaseUrlsAsLocal(String theUrl) {
		Validate.notBlank(theUrl, "Base URL must not be null or empty");

		int starIdx = theUrl.indexOf('*');
		if (starIdx != -1) {
			if (starIdx != theUrl.length() - 1) {
				throw new IllegalArgumentException("Base URL wildcard character (*) can only appear at the end of the string: " + theUrl);
			}
		}

	}
}
