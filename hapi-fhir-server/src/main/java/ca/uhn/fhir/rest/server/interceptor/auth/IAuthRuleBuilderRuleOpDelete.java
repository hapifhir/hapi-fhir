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

public interface IAuthRuleBuilderRuleOpDelete extends IAuthRuleBuilderRuleOp {

	/**
	 * Specifies that this rule applies to cascading deletes as opposed to regular
	 * deletes. Note that if you want to allow cascading deletes, you will typically
	 * require at least two separate rules: one for the original source resource, and
	 * one for the cascade.
	 */
	IAuthRuleBuilderRuleOp onCascade();

	/**
	 * Specifies that this rule applies to delete expunges as opposed to regular
	 * deletes.  A delete expunge is a delete operation called with the _expunge=true parameter.
	 */
	IAuthRuleBuilderRuleOp onExpunge();
}
