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

import org.hl7.fhir.instance.model.api.IIdType;

import javax.annotation.Nonnull;

/**
 * @since 5.5.0
 */
public interface IAuthRuleBuilderRuleBulkExport {

	/**
	 * Allow/deny <b>group-level</b> export rule applies to the Group with the given resource ID, e.g. <code>Group/123</code>
	 *
	 * @since 5.5.0
	 */
	default IAuthRuleBuilderRuleBulkExportWithTarget groupExportOnGroup(@Nonnull IIdType theFocusResourceId) {
		return groupExportOnGroup(theFocusResourceId.getValue());
	}

	/**
	 * Allow/deny <b>group-level</b> export rule applies to the Group with the given resource ID, e.g. <code>Group/123</code>
	 *
	 * @since 5.5.0
	 */
	IAuthRuleBuilderRuleBulkExportWithTarget groupExportOnGroup(@Nonnull String theFocusResourceId);

	/**
	 * Allow/deny <b>patient-level</b> export rule applies to the Group with the given resource ID, e.g. <code>Group/123</code>
	 *
	 * @since 5.5.0
	 */
	default IAuthRuleBuilderRuleBulkExportWithTarget patientExportOnGroup(@Nonnull IIdType theFocusResourceId) {
		return patientExportOnGroup(theFocusResourceId.getValue());
	}

	/**
	 * Allow/deny <b>patient-level</b> export rule applies to the Group with the given resource ID, e.g. <code>Group/123</code>
	 *
	 * @since 5.5.0
	 */
	IAuthRuleBuilderRuleBulkExportWithTarget patientExportOnGroup(@Nonnull String theFocusResourceId);

	/**
	 * Allow/deny <b>system-level</b> export rule applies to the Group with the given resource ID, e.g. <code>Group/123</code>
	 *
	 * @since 5.5.0
	 */
	IAuthRuleBuilderRuleBulkExportWithTarget systemExport();

	/**
	 * Allow/deny <b>any bulk export</b> operation
	 */
	IAuthRuleBuilderRuleBulkExportWithTarget any();
}
