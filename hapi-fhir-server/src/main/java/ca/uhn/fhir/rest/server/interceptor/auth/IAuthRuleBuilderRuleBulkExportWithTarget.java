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
	 * This this method is not called, there will be no restriction on the resource types
	 * that a user can initiate a bulk export on.
	 *
	 * @since 5.5.0
	 */
	IAuthRuleBuilderRuleBulkExportWithTarget withResourceTypes(Collection<String> theResourceTypes);

}
