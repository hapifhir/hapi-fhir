package ca.uhn.fhir.rest.server.interceptor.auth;

/*-
 * #%L
 * HAPI FHIR - Server Framework
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

import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Return type for {@link SearchNarrowingInterceptor#buildAuthorizedList(RequestDetails)}
 */
public class AuthorizedList {

	private List<String> myAllowedCompartments;
	private List<String> myAllowedInstances;
	private List<AllowedCodeInValueSet> myAllowedCodeInValueSets;

	@Nullable
	List<String> getAllowedCompartments() {
		return myAllowedCompartments;
	}

	@Nullable
	List<AllowedCodeInValueSet> getAllowedCodeInValueSets() {
		return myAllowedCodeInValueSets;
	}

	@Nullable
	List<String> getAllowedInstances() {
		return myAllowedInstances;
	}

	/**
	 * Adds a compartment that the user should be allowed to access
	 *
	 * @param theCompartment The compartment name, e.g. "Patient/123" (in this example the user would be allowed to access Patient/123 as well as Observations where Observation.subject="Patient/123"m, etc.
	 * @return Returns <code>this</code> for easy method chaining
	 */
	public AuthorizedList addCompartment(String theCompartment) {
		Validate.notNull(theCompartment, "theCompartment must not be null");
		if (myAllowedCompartments == null) {
			myAllowedCompartments = new ArrayList<>();
		}
		myAllowedCompartments.add(theCompartment);

		return this;
	}

	/**
	 * Adds a compartment that the user should be allowed to access
	 *
	 * @param theCompartments The compartment names, e.g. "Patient/123" (in this example the user would be allowed to access Patient/123 as well as Observations where Observation.subject="Patient/123"m, etc.
	 * @return Returns <code>this</code> for easy method chaining
	 */
	public AuthorizedList addCompartments(String... theCompartments) {
		Validate.notNull(theCompartments, "theCompartments must not be null");
		for (String next : theCompartments) {
			addCompartment(next);
		}
		return this;
	}

	/**
	 * Adds a resource that the user should be allowed to access
	 *
	 * @param theResource The resource name, e.g. "Patient/123" (in this example the user would be allowed to access Patient/123 but not Observations where Observation.subject="Patient/123"m, etc.
	 * @return Returns <code>this</code> for easy method chaining
	 */
	public AuthorizedList addResource(String theResource) {
		Validate.notNull(theResource, "theResource must not be null");
		if (myAllowedInstances == null) {
			myAllowedInstances = new ArrayList<>();
		}
		myAllowedInstances.add(theResource);

		return this;
	}

	/**
	 * Adds a resource that the user should be allowed to access
	 *
	 * @param theResources The resource names, e.g. "Patient/123" (in this example the user would be allowed to access Patient/123 but not Observations where Observation.subject="Patient/123"m, etc.
	 * @return Returns <code>this</code> for easy method chaining
	 */
	public AuthorizedList addResources(String... theResources) {
		Validate.notNull(theResources, "theResources must not be null");
		for (String next : theResources) {
			addResource(next);
		}
		return this;
	}

	/**
	 * If specified, any search for <code>theResourceName</code> will automatically include a parameter indicating that
	 * the token search parameter <code>theSearchParameterName</code> must have a value in the ValueSet with URL <code>theValueSetUrl</code>.
	 *
	 * @param theResourceName        The resource name, e.g. <code>Observation</code>
	 * @param theSearchParameterName The search parameter name, e.g. <code>code</code>
	 * @param theValueSetUrl         The valueset URL, e.g. <code>http://my-value-set</code>
	 * @return Returns a reference to <code>this</code> for easy chaining
	 * @see AuthorizationInterceptor If search narrowing by code is being used for security reasons, consider also using AuthorizationInterceptor as a failsafe to ensure that no inapproproiate resources are returned
	 * @since 6.0.0
	 */
	public AuthorizedList addCodeInValueSet(@Nonnull String theResourceName, @Nonnull String theSearchParameterName, @Nonnull String theValueSetUrl) {
		Validate.notBlank(theResourceName, "theResourceName must not be missing or null");
		Validate.notBlank(theSearchParameterName, "theSearchParameterName must not be missing or null");
		Validate.notBlank(theValueSetUrl, "theResourceUrl must not be missing or null");

		return doAddCodeInValueSet(theResourceName, theSearchParameterName, theValueSetUrl, false);
	}

	/**
	 * If specified, any search for <code>theResourceName</code> will automatically include a parameter indicating that
	 * the token search parameter <code>theSearchParameterName</code> must have a value not in the ValueSet with URL <code>theValueSetUrl</code>.
	 *
	 * @param theResourceName        The resource name, e.g. <code>Observation</code>
	 * @param theSearchParameterName The search parameter name, e.g. <code>code</code>
	 * @param theValueSetUrl         The valueset URL, e.g. <code>http://my-value-set</code>
	 * @return Returns a reference to <code>this</code> for easy chaining
	 * @see AuthorizationInterceptor If search narrowing by code is being used for security reasons, consider also using AuthorizationInterceptor as a failsafe to ensure that no inapproproiate resources are returned
	 * @since 6.0.0
	 */
	public AuthorizedList addCodeNotInValueSet(@Nonnull String theResourceName, @Nonnull String theSearchParameterName, @Nonnull String theValueSetUrl) {
		Validate.notBlank(theResourceName, "theResourceName must not be missing or null");
		Validate.notBlank(theSearchParameterName, "theSearchParameterName must not be missing or null");
		Validate.notBlank(theValueSetUrl, "theResourceUrl must not be missing or null");

		return doAddCodeInValueSet(theResourceName, theSearchParameterName, theValueSetUrl, true);
	}

	private AuthorizedList doAddCodeInValueSet(String theResourceName, String theSearchParameterName, String theValueSetUrl, boolean negate) {
		if (myAllowedCodeInValueSets == null) {
			myAllowedCodeInValueSets = new ArrayList<>();
		}
		myAllowedCodeInValueSets.add(new AllowedCodeInValueSet(theResourceName, theSearchParameterName, theValueSetUrl, negate));

		return this;
	}
}
