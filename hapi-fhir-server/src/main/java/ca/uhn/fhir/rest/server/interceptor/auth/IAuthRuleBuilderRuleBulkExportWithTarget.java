package ca.uhn.fhir.rest.server.interceptor.auth;

/*-
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

import java.util.Collection;

/**
 * @since 5.5.0
 */
public interface IAuthRuleBuilderRuleBulkExportWithTarget extends IAuthRuleFinished {

	/**
	 * If specified, rule will only apply to bulk export requests that explicitly specify a list
	 * of resource types where the list is equal to, or a subset of the supplied collection.
	 *
	 * This this method is not called, there will be no restriction on the resource types
	 * that a user can initiate a bulk export on.
	 *
	 * @since 5.5.0
	 */
	IAuthRuleBuilderRuleBulkExportWithTarget withResourceTypes(Collection<String> theResourceTypes);

}
