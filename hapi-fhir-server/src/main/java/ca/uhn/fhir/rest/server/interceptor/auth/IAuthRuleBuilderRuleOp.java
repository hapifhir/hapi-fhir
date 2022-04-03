package ca.uhn.fhir.rest.server.interceptor.auth;

import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Collection;

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

public interface IAuthRuleBuilderRuleOp extends IAuthRuleBuilderAppliesTo<IAuthRuleBuilderRuleOpClassifier> {

	/**
	 * Rule applies to the resource with the given ID (e.g. <code>Patient/123</code>)
	 * <p>
	 * See the following examples which show how theId is interpreted:
	 * </p>
	 * <ul>
	 * <li><b><code>http://example.com/Patient/123</code></b> - Any Patient resource with the ID "123" will be matched (note: the base URL part is ignored)</li>
	 * <li><b><code>Patient/123</code></b> - Any Patient resource with the ID "123" will be matched</li>
	 * <li><b><code>123</code></b> - Any resource of any type with the ID "123" will be matched</li>
	 * </ul>
	 *
	 * @param theId The ID of the resource to apply  (e.g. <code>Patient/123</code>)
	 * @throws IllegalArgumentException If theId does not contain an ID with at least an ID part
	 * @throws NullPointerException     If theId is null
	 */
	IAuthRuleFinished instance(String theId);

	/**
	 * Rule applies to the resource with the given ID (e.g. <code>Patient/123</code>)
	 * <p>
	 * See the following examples which show how theId is interpreted:
	 * </p>
	 * <ul>
	 * <li><b><code>http://example.com/Patient/123</code></b> - Any Patient resource with the ID "123" will be matched (note: the base URL part is ignored)</li>
	 * <li><b><code>Patient/123</code></b> - Any Patient resource with the ID "123" will be matched</li>
	 * <li><b><code>123</code></b> - Any resource of any type with the ID "123" will be matched</li>
	 * </ul>
	 >*
	 * @param theId The ID of the resource to apply  (e.g. <code>Patient/123</code>)
	 * @throws IllegalArgumentException If theId does not contain an ID with at least an ID part
	 * @throws NullPointerException     If theId is null
	 */
	IAuthRuleFinished instance(IIdType theId);

	/**
	 * Rule applies to the resource with the given ID (e.g. <code>Patient/123</code>)
	 * <p>
	 * See the following examples which show how theId is interpreted:
	 * </p>
	 * <ul>
	 * <li><b><code>http://example.com/Patient/123</code></b> - Any Patient resource with the ID "123" will be matched (note: the base URL part is ignored)</li>
	 * <li><b><code>Patient/123</code></b> - Any Patient resource with the ID "123" will be matched</li>
	 * <li><b><code>123</code></b> - Any resource of any type with the ID "123" will be matched</li>
	 * </ul>
	 *
	 * @param theIds The IDs of the resource to apply  (e.g. <code>Patient/123</code>)
	 * @throws IllegalArgumentException If theId does not contain an ID with at least an ID part
	 * @throws NullPointerException     If theId is null
	 */
	IAuthRuleFinished instances(Collection<IIdType> theIds);

}
