package ca.uhn.fhir.rest.server.interceptor.auth;

/*
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

import java.util.Collection;
import java.util.List;

import org.hl7.fhir.instance.model.api.IIdType;

import javax.annotation.Nonnull;

public interface IAuthRuleBuilderRuleOpClassifier {

	/**
	 * Rule applies to resources in the given compartment.
	 * <p>
	 * For example, to apply the rule to any observations in the patient compartment
	 * belonging to patient "123", you would invoke this with</br>
	 * <code>inCompartment("Patient", new IdType("Patient", "123"))</code>
	 * </p>
	 * <p>
	 * This call completes the rule and adds the rule to the chain. 
	 * </p>
	 * 
	 * @param theCompartmentName The name of the compartment (must not be null or blank)
	 * @param theOwner The owner of the compartment. Note that both the resource type and ID must be populated in this ID.
	 */
	IAuthRuleBuilderRuleOpClassifierFinished inCompartment(String theCompartmentName, IIdType theOwner);

	/**
	 * Rule applies to resources in the given compartment.
	 * <p>
	 * For example, to apply the rule to any observations in the patient compartment
	 * belonging to patient "123", you would invoke this with</br>
	 * <code>inCompartment("Patient", new IdType("Patient", "123"))</code>
	 *
	 * This call also allows you to pass additional search parameters that count as being included in the given compartment,
	 * passed in as a list of `resourceType:search-parameter-name`. For example, if you select a compartment name of "patient",
	 * you could pass in a singleton list consisting of the string "device:patient", which would cause any devices belonging
	 * to the patient to be permitted by the authorization rule.
	 *
	 * </p>
	 * <p>
	 * This call completes the rule and adds the rule to the chain.
	 * </p>
	 *
	 * @param theCompartmentName The name of the compartment (must not be null or blank)
	 * @param theOwner The owner of the compartment. Note that both the resource type and ID must be populated in this ID.
	 * @param theAdditionalTypeSearchParamNames A list of strings for additional resource types and search parameters which count as being in the compartment, in the form "resourcetype:search-parameter-name".
	 */
	IAuthRuleBuilderRuleOpClassifierFinished inCompartmentWithAdditionalSearchParams(String theCompartmentName, IIdType theOwner, AdditionalCompartmentSearchParameters theAdditionalTypeSearchParamNames);


	/**
	 * Rule applies to resources in the given compartment.
	 * <p>
	 * For example, to apply the rule to any observations in the patient compartment
	 * belonging to patient "123", you would invoke this with</br>
	 * <code>inCompartment("Patient", new IdType("Patient", "123"))</code>
	 * </p>
	 * <p>
	 * This call completes the rule and adds the rule to the chain. 
	 * </p>
	 * 
	 * @param theCompartmentName The name of the compartment (must not be null or blank)
	 * @param theOwners The owner of the compartment. Note that both the resource type and ID must be populated in this ID.
	 */
	IAuthRuleBuilderRuleOpClassifierFinished inCompartment(String theCompartmentName, Collection<? extends IIdType> theOwners);


	/**
	 * Rule applies to resources in the given compartment.
	 * <p>
	 * For example, to apply the rule to any observations in the patient compartment
	 * belonging to patient "123", you would invoke this with</br>
	 * <code>inCompartment("Patient", new IdType("Patient", "123"))</code>
	 *
	 * This call also allows you to pass additional search parameters that count as being included in the given compartment,
	 * passed in as a list of `resourceType:search-parameter-name`. For example, if you select a compartment name of "patient",
	 * you could pass in a singleton list consisting of the string "device:patient", which would cause any devices belonging
	 * to the patient to be permitted by the authorization rule.
	 *
	 * </p>
	 * <p>
	 * This call completes the rule and adds the rule to the chain.
	 * </p>
	 *
	 * @param theCompartmentName The name of the compartment (must not be null or blank)
	 * @param theOwners The owners of the compartment. Note that both the resource type and ID must be populated in these IDs.
	 * @param theAdditionalTypeSearchParamNames A {@link AdditionalCompartmentSearchParameters} which allows you to expand the search space for what is considered "in" the compartment.
	 *
	 **/
	IAuthRuleBuilderRuleOpClassifierFinished inCompartmentWithAdditionalSearchParams(String theCompartmentName, Collection<? extends IIdType> theOwners, AdditionalCompartmentSearchParameters theAdditionalTypeSearchParamNames);


	/**
	 * Rule applies to any resource instances
	 * <p>
	 * This call completes the rule and adds the rule to the chain. 
	 * </p>
	 */
	IAuthRuleBuilderRuleOpClassifierFinished withAnyId();

	/**
	 * Rule applies to resources where the given search parameter would be satisfied by a code in the given ValueSet
	 * @param theSearchParameterName The search parameter name, e.g. <code>"code"</code>
	 * @param theValueSetUrl The valueset URL, e.g. <code>"http://my-value-set"</code>
	 * @since 6.0.0
	 */
	IAuthRuleBuilderRuleOpClassifierFinished withCodeInValueSet(@Nonnull String theSearchParameterName, @Nonnull String theValueSetUrl);

	/**
	 * Rule applies to resources where the given search parameter would be satisfied by a code not in the given ValueSet
	 * @param theSearchParameterName The search parameter name, e.g. <code>"code"</code>
	 * @param theValueSetUrl The valueset URL, e.g. <code>"http://my-value-set"</code>
	 * @since 6.0.0
	 */
	IAuthRuleFinished withCodeNotInValueSet(@Nonnull String theSearchParameterName, @Nonnull String theValueSetUrl);
}
