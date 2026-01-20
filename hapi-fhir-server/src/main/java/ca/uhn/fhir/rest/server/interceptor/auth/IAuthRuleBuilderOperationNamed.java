/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.rest.server.interceptor.auth;

import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Collection;

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

	IAuthRuleBuilderOperationNamedAndScoped onInstances(Collection<IIdType> theInstanceIds);

	/**
	 * Rule applies to invocations of this operation at the <code>instance</code> level on any instance of the given type
	 */
	IAuthRuleBuilderOperationNamedAndScoped onInstancesOfType(Class<? extends IBaseResource> theType);

	/**
	 * Rule applies to invocations of this operation at the <code>instance</code> level on any instance of the given type.
	 * An optional FHIR query filter can be added to ensure instances meet certain criteria.
	 *
	 * @param theType The resource type (i.e. {@code org.hl7.fhir.r4.model.Observation.class})
	 * @param theFilterRestriction The FHIR query filter to apply (i.e. {@code category=vital-signs})
	 */
	IAuthRuleBuilderOperationNamedAndScoped onInstancesOfTypeMatchingOptionalFilter(
			Class<? extends IBaseResource> theType, @Nullable String theFilterRestriction);

	/**
	 * Rule applies to invocations of this operation at the <code>instance</code> level on any instance
	 */
	IAuthRuleBuilderOperationNamedAndScoped onAnyInstance();

	/**
	 * Rule applies to invocations of this operation at the <code>instance</code> level on any instance.
	 * An optional FHIR query filter can be added to ensure instances meet certain criteria.
	 *
	 * @param theFilterRestriction The FHIR query filter to apply (i.e. {@code _tag=http://example.org|some-tag})
	 */
	IAuthRuleBuilderOperationNamedAndScoped onAnyInstanceMatchingOptionalFilter(@Nullable String theFilterRestriction);

	/**
	 * Rule applies to invocations of this operation at any level (server, type or instance)
	 */
	IAuthRuleBuilderOperationNamedAndScoped atAnyLevel();
}
