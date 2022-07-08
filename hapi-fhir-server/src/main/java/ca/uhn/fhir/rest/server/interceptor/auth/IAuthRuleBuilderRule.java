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
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

public interface IAuthRuleBuilderRule {

	/**
	 * This rule applies to <code>create</code> operations with a <code>conditional</code>
	 * URL as a part of the request. Note that this rule will allow the conditional
	 * operation to proceed, but the server is expected to determine the actual target
	 * of the conditional request and send a subsequent event to the {@link AuthorizationInterceptor}
	 * in order to authorize the actual target.
	 * <p>
	 * In other words, if the server is configured correctly, this chain will allow the
	 * client to perform a conditional update, but a different rule is required to actually
	 * authorize the target that the conditional update is determined to match.
	 * </p>
	 */
	IAuthRuleBuilderRuleConditional createConditional();

	/**
	 * This rule applies to the FHIR delete operation
	 */
	IAuthRuleBuilderRuleOpDelete delete();

	/**
	 * This rule applies to <code>create</code> operations with a <code>conditional</code>
	 * URL as a part of the request. Note that this rule will allow the conditional
	 * operation to proceed, but the server is expected to determine the actual target
	 * of the conditional request and send a subsequent event to the {@link AuthorizationInterceptor}
	 * in order to authorize the actual target.
	 * <p>
	 * In other words, if the server is configured correctly, this chain will allow the
	 * client to perform a conditional update, but a different rule is required to actually
	 * authorize the target that the conditional update is determined to match.
	 * </p>
	 */
	IAuthRuleBuilderRuleConditional deleteConditional();

	/**
	 * This rules applies to the metadata operation (retrieve the
	 * server's conformance statement)
	 * <p>
	 * This call completes the rule and adds the rule to the chain.
	 * </p>
	 */
	IAuthRuleBuilderRuleOpClassifierFinished metadata();

	/**
	 * This rule applies to a FHIR operation (e.g. <code>$validate</code>)
	 */
	IAuthRuleBuilderOperation operation();

	/**
	 * This rule applies to a FHIR patch operation
	 */
	IAuthRuleBuilderPatch patch();

	/**
	 * This rule applies to any FHIR operation involving reading, including
	 * <code>read</code>, <code>vread</code>, <code>search</code>, and
	 * <code>history</code>
	 */
	IAuthRuleBuilderRuleOp read();

	/**
	 * This rule applies to the FHIR transaction operation. Transaction is a special
	 * case in that it bundles other operations. This permission also allows FHIR
	 * batch to be performed.
	 */
	IAuthRuleBuilderRuleTransaction transaction();

	/**
	 * This rule applies to <code>update</code> operations with a <code>conditional</code>
	 * URL as a part of the request. Note that this rule will allow the conditional
	 * operation to proceed, but the server is expected to determine the actual target
	 * of the conditional request and send a subsequent event to the {@link AuthorizationInterceptor}
	 * in order to authorize the actual target.
	 * <p>
	 * In other words, if the server is configured correctly, this chain will allow the
	 * client to perform a conditional update, but a different rule is required to actually
	 * authorize the target that the conditional update is determined to match.
	 * </p>
	 */
	IAuthRuleBuilderRuleConditional updateConditional();

	/**
	 * This rule applies to any FHIR operation involving writing, including
	 * <code>create</code>, and <code>update</code>
	 */
	IAuthRuleBuilderRuleOp write();

	/**
	 * This rule specifically allows a user to perform a FHIR create, but not an update or other write operations
	 *
	 * @see #write()
	 * @since 4.1.0
	 */
	IAuthRuleBuilderRuleOp create();

	/**
	 * Allow a GraphQL query
	 */
	IAuthRuleBuilderGraphQL graphQL();

	/**
	 * This rule permits the user to initiate a FHIR bulk export
	 *
	 * @since 5.5.0
	 */
	IAuthRuleBuilderRuleBulkExport bulkExport();
}
