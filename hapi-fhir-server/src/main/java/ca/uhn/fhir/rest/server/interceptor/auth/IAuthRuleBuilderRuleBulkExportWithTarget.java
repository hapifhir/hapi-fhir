package ca.uhn.fhir.rest.server.interceptor.auth;

import java.util.Collection;

/**
 * @since 5.5.0
 */
public interface IAuthRuleBuilderRuleBulkExportWithTarget extends IAuthRuleFinished {

	/**
	 * If specified, rule will only apply to bulk export requests that explicitly specify a list
	 * of resource types where the list is equal to, or a subset of the supplied collection.
	 *
	 * @since 5.5.0
	 */
	IAuthRuleBuilderRuleBulkExportWithTarget withResourceTypes(Collection<String> theResourceTypes);

}
