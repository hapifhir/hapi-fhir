package ca.uhn.fhir.rest.server.interceptor.auth;

import java.util.List;

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

/**
 * Used by {@link AuthorizationInterceptor} in order to allow user code to define authorization
 * rules.
 * 
 * @see AuthorizationInterceptor
 */
public interface IAuthRuleBuilder {

	/**
	 * Start a new rule to allow a given operation
	 */
	IAuthRuleBuilderRule allow();

	/**
	 * Start a new rule to allow a given operation
	 * 
	 * @param theRuleName
	 *           The name of this rule. The rule name is used for logging and error messages,
	 *           and could be shown to the client, but has no semantic meaning within
	 *           HAPI FHIR.
	 */
	IAuthRuleBuilderRule allow(String theRuleName);

	/**
	 * This rule allows any invocation to proceed. It is intended to be
	 * used at the end of a chain that contains {@link #deny()} rules in
	 * order to specify a blacklist chain.
	 * <p>
	 * This call completes the rule and adds the rule to the chain.
	 * </p>
	 */
	IAuthRuleBuilderRuleOpClassifierFinished allowAll();

	/**
	 * This rule allows any invocation to proceed. It is intended to be
	 * used at the end of a chain that contains {@link #deny()} rules in
	 * order to specify a blacklist chain.
	 * <p>
	 * This call completes the rule and adds the rule to the chain.
	 * </p>
	 * @param theRuleName
	 *           The name of this rule. The rule name is used for logging and error messages,
	 *           and could be shown to the client, but has no semantic meaning within
	 *           HAPI FHIR.
	 */
	IAuthRuleBuilderRuleOpClassifierFinished allowAll(String theRuleName);

	/**
	 * Build the rule list
	 */
	List<IAuthRule> build();

	/**
	 * Start a new rule to deny a given operation
	 */
	IAuthRuleBuilderRule deny();

	/**
	 * Start a new rule to deny a given operation
	 * 
	 * @param theRuleName
	 *           The name of this rule. The rule name is used for logging and error messages,
	 *           and could be shown to the client, but has no semantic meaning within
	 *           HAPI FHIR.
	 */
	IAuthRuleBuilderRule deny(String theRuleName);

	/**
	 * This rule allows any invocation to proceed. It is intended to be
	 * used at the end of a chain that contains {@link #allow()} rules in
	 * order to specify a whitelist chain.
	 * <p>
	 * This call completes the rule and adds the rule to the chain.
	 * </p>
	 */
	IAuthRuleBuilderRuleOpClassifierFinished denyAll();

	/**
	 * This rule allows any invocation to proceed. It is intended to be
	 * used at the end of a chain that contains {@link #allow()} rules in
	 * order to specify a whitelist chain.
	 * <p>
	 * This call completes the rule and adds the rule to the chain.
	 * </p>
	 * @param theRuleName
	 *           The name of this rule. The rule name is used for logging and error messages,
	 *           and could be shown to the client, but has no semantic meaning within
	 *           HAPI FHIR.
	 */
	IAuthRuleBuilderRuleOpClassifierFinished denyAll(String theRuleName);

}
