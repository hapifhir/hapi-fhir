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

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

public interface IAuthRuleBuilderOperationNamed {

	/**
	 * Rule applies to invocations of this operation at the <code>server</code> level
	 */
	IAuthRuleBuilderOperationNamedAndScoped onServer();

	/**
	 * Rule applies to invocations of this operation at the <code>type</code> level
	 */
	IAuthRuleBuilderOperationNamedAndScoped onType(Class<? extends IBaseResource> theType);

	/**
	 * Rule applies to invocations of this operation at the <code>type</code> level on any type
	 */
	IAuthRuleBuilderOperationNamedAndScoped onAnyType();

	/**
	 * Rule applies to invocations of this operation at the <code>instance</code> level
	 */
	IAuthRuleBuilderOperationNamedAndScoped onInstance(IIdType theInstanceId);

	/**
	 * Rule applies to invocations of this operation at the <code>instance</code> level on any instance of the given type
	 */
	IAuthRuleBuilderOperationNamedAndScoped onInstancesOfType(Class<? extends IBaseResource> theType);

	/**
	 * Rule applies to invocations of this operation at the <code>instance</code> level on any instance
	 */
	IAuthRuleBuilderOperationNamedAndScoped onAnyInstance();

	/**
	 * Rule applies to invocations of this operation at any level (server, type or instance)
	 */
	IAuthRuleBuilderOperationNamedAndScoped atAnyLevel();

}
