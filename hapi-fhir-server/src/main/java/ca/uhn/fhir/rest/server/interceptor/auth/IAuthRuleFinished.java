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

import javax.annotation.Nullable;
import java.util.List;

public interface IAuthRuleFinished {

	/**
	 * Start another rule
	 */
	IAuthRuleBuilder andThen();

	/**
	 * Build the rule list
	 */
	List<IAuthRule> build();

	/**
	 * Add an additional tester that will be queried if all other conditions
	 * of this rule already match. For example, given the following rule
	 * <pre>
	 * return new RuleBuilder()
	 *   .allow("Rule 1").operation().named("everything").onInstancesOfType(Patient.class).withTester(myTester)
	 *   .build();
	 * </pre>
	 * ..the tester will be invoked on any $everything operations on Patient
	 * resources as a final check as to whether the rule applies or not. In this
	 * example, the tester is not invoked for other operations.
	 *
	 * @param theTester The tester to add, or <code>null</code>
	 */
	IAuthRuleFinished withTester(@Nullable IAuthRuleTester theTester);

}
