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
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.hl7.fhir.instance.model.api.IBaseResource;

public interface IAuthRuleBuilderAppliesTo<T> {

	/**
	 * Rule applies to all resources
	 */
	T allResources();

	/**
	 * Rule applies to resources of the given type
	 */
	T resourcesOfType(Class<? extends IBaseResource> theType);

	/**
	 * Rule applies to resources of the given type
	 *
	 * @param theType E.g. "Patient"
	 */
	T resourcesOfType(String theType);

}
