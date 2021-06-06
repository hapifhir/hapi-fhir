package ca.uhn.fhir.rest.server.interceptor.auth;

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
}
