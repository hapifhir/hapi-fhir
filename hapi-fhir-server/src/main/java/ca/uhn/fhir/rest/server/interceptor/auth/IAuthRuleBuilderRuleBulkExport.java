/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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

import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Collection;
import java.util.stream.Collectors;

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

	IAuthRuleBuilderRuleBulkExportWithTarget patientExportOnPatient(@Nonnull String theFocusResourceId);

	IAuthRuleBuilderRuleBulkExportWithTarget patientExportOnAllPatients();

	default IAuthRuleBuilderRuleBulkExportWithTarget patientExportOnPatient(@Nonnull IIdType theFocusResourceId) {
		return patientExportOnPatient(theFocusResourceId.getValue());
	}

	default IAuthRuleBuilderRuleBulkExportWithTarget patientExportOnPatients(
			@Nonnull Collection<IIdType> theFocusResourceIds) {
		return patientExportOnPatientStrings(
				theFocusResourceIds.stream().map(IIdType::getValue).collect(Collectors.toList()));
	}

	IAuthRuleBuilderRuleBulkExportWithTarget patientExportOnPatientStrings(Collection<String> theFocusResourceIds);

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
