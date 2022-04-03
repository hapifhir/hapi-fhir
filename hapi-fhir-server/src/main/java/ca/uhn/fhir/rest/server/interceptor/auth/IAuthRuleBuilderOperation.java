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

public interface IAuthRuleBuilderOperation {

	/**
	 * This rule applies to the operation with the given name
	 * 
	 * @param The operation name, e.g. "validate" or "$validate" (either form may be used here)
	 */
	IAuthRuleBuilderOperationNamed named(String theOperationName);

	/**
	 * This rule applies to any operation
	 */
	IAuthRuleBuilderOperationNamed withAnyName();

}
