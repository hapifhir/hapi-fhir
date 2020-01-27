package ca.uhn.fhir.rest.server.interceptor.auth;

/*
 * #%L
 * HAPI FHIR - Server Framework
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

import java.util.Collection;

import org.hl7.fhir.instance.model.api.IIdType;

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
	 * Rule applies to any resource instances
	 * <p>
	 * This call completes the rule and adds the rule to the chain. 
	 * </p>
	 */
	IAuthRuleBuilderRuleOpClassifierFinished withAnyId();
}
