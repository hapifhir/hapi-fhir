package ca.uhn.fhir.rest.server.interceptor.auth;

/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions;

public final class OperationRuleTestUtil {
	private OperationRuleTestUtil() {}

	public static String getOperationName(IAuthRule theRule) {
		return ((OperationRule)theRule).getOperationName();
	}

	public static boolean isAppliesToServer(IAuthRule theRule) {
		return ((OperationRule)theRule).isAppliesToServer();
	}

	public static boolean isAllowAllResponses(IAuthRule theRule) {
		return ((OperationRule)theRule).isAllowAllResponses();
	}

	public static boolean isAppliesToAnyType(IAuthRule theRule) {
		return ((OperationRule)theRule).isAppliesToAnyType();
	}

	public static String getGroupId(IAuthRule theRule) {
		return ((RuleBulkExportImpl)theRule).getGroupId();
	}

	public static BulkDataExportOptions.ExportStyle getWantExportStyle(IAuthRule theRule) {
		return ((RuleBulkExportImpl)theRule).getWantExportStyle();
	}
}
