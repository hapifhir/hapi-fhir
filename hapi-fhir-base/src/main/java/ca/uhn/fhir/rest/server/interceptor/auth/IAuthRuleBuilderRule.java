package ca.uhn.fhir.rest.server.interceptor.auth;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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

public interface IAuthRuleBuilderRule {

	/**
	 * This rule applies to any FHIR operation involving reading, including
	 * <code>read</code>, <code>vread</code>, <code>search</code>, and
	 * <code>history</code>
	 */
	IAuthRuleBuilderRuleOp read();

	/**
	 * This rule applies to any FHIR operation involving writing, including
	 * <code>create</code>, and <code>update</code>
	 */
	IAuthRuleBuilderRuleOp write();

	/**
	 * This rule applies to the FHIR transaction operation. Transaction is a special
	 * case in that it bundles other operations
	 */
	IAuthRuleBuilderRuleTransaction transaction();

}
