/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor.Verdict;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

class OperationRule extends BaseRule implements IAuthRule {
	private static final Logger ourLog = LoggerFactory.getLogger(OperationRule.class);
	private String myOperationName;
	private boolean myAppliesToServer;
	private HashSet<Class<? extends IBaseResource>> myAppliesToTypes;
	private List<IIdType> myAppliesToIds;
	private HashSet<Class<? extends IBaseResource>> myAppliesToInstancesOfType;
	private boolean myAppliesToAnyType;
	private boolean myAppliesToAnyInstance;
	private boolean myAppliesAtAnyLevel;
	private boolean myAllowAllResponses;
	private boolean myAllowAllResourcesAccess;

	@Nullable
	private String myInstanceFilter;

	OperationRule(String theRuleName) {
		super(theRuleName);
	}

	void appliesAtAnyLevel(boolean theAppliesAtAnyLevel) {
		myAppliesAtAnyLevel = theAppliesAtAnyLevel;
	}

	public void allowAllResponses() {
		myAllowAllResponses = true;
	}

	public void allowAllResourcesAccess() {
		myAllowAllResourcesAccess = true;
	}

	void appliesToAnyInstance() {
		myAppliesToAnyInstance = true;
	}

	void appliesToAnyInstanceMatchingFilter(@Nullable String theFilter) {
		myAppliesToAnyInstance = true;
		myInstanceFilter = theFilter;
	}

	void appliesToAnyType() {
		myAppliesToAnyType = true;
	}

	void appliesToInstances(List<IIdType> theAppliesToIds) {
		myAppliesToIds = theAppliesToIds;
	}

	void appliesToInstancesOfType(HashSet<Class<? extends IBaseResource>> theAppliesToTypes) {
		myAppliesToInstancesOfType = theAppliesToTypes;
	}

	void appliesToInstancesOfTypeMatchingFilter(
			HashSet<Class<? extends IBaseResource>> theAppliesToTypes, @Nullable String theFilter) {
		myAppliesToInstancesOfType = theAppliesToTypes;
		myInstanceFilter = theFilter;
	}

	void appliesToServer() {
		myAppliesToServer = true;
	}

	void appliesToTypes(HashSet<Class<? extends IBaseResource>> theAppliesToTypes) {
		myAppliesToTypes = theAppliesToTypes;
	}

	@SuppressWarnings("EnumSwitchStatementWhichMissesCases")
	@Override
	public Verdict applyRule(
			RestOperationTypeEnum theOperation,
			RequestDetails theRequestDetails,
			IBaseResource theInputResource,
			IIdType theInputResourceId,
			IBaseResource theOutputResource,
			IRuleApplier theRuleApplier,
			Set<AuthorizationFlagsEnum> theFlags,
			Pointcut thePointcut) {

		validateState();

		FhirContext ctx = theRequestDetails.getServer().getFhirContext();

		boolean applies = false;
		switch (theOperation) {
			case EXTENDED_OPERATION_SERVER:
				if (myAppliesToServer || myAppliesAtAnyLevel) {
					applies = true;
				}
				break;
			case EXTENDED_OPERATION_TYPE:
				if (myAppliesToAnyType || myAppliesAtAnyLevel) {
					applies = true;
				} else if (myAppliesToTypes != null) {
					// TODO: Convert to a map of strings and keep the result
					for (Class<? extends IBaseResource> next : myAppliesToTypes) {
						String resName = ctx.getResourceType(next);
						if (resName.equals(theRequestDetails.getResourceName())) {
							applies = true;
							break;
						}
					}
				}
				break;
			case EXTENDED_OPERATION_INSTANCE:
				IIdType requestResourceId = getRequestResourceId(theRequestDetails);

				if (myAppliesToAnyInstance || myAppliesAtAnyLevel) {
					applies = true;
					if (isBlockedByInstanceFilter(
							theRequestDetails, requestResourceId, theInputResource, theOperation, theRuleApplier)) {
						applies = false;
					}
				} else {
					if (requestResourceId != null) {
						if (myAppliesToIds != null) {
							String instanceId =
									requestResourceId.toUnqualifiedVersionless().getValue();
							for (IIdType next : myAppliesToIds) {
								if (next.toUnqualifiedVersionless().getValue().equals(instanceId)) {
									applies = true;
									if (isBlockedByInstanceFilter(
											theRequestDetails,
											requestResourceId,
											theInputResource,
											theOperation,
											theRuleApplier)) {
										applies = false;
									} else {
										break;
									}
								}
							}
						}
						if (myAppliesToInstancesOfType != null) {
							// TODO: Convert to a map of strings and keep the result
							for (Class<? extends IBaseResource> next : myAppliesToInstancesOfType) {
								String resName = ctx.getResourceType(next);
								if (resName.equals(requestResourceId.getResourceType())) {
									applies = true;
									if (isBlockedByInstanceFilter(
											theRequestDetails,
											requestResourceId,
											theInputResource,
											theOperation,
											theRuleApplier)) {
										applies = false;
									} else {
										break;
									}
								}
							}
						}
					}
				}
				break;
			default:
				return null;
		}

		if (!applies) {
			return null;
		}

		if (myOperationName != null && !myOperationName.equals(theRequestDetails.getOperation())) {
			return null;
		}

		if (theOutputResource == null) {
			// This is the request part
			return newVerdict(
					theOperation,
					theRequestDetails,
					theInputResource,
					theInputResourceId,
					theOutputResource,
					theRuleApplier);
		} else {
			// This is the response part, so we might want to check all of the
			// resources in the response
			if (myAllowAllResponses) {
				return newVerdict(
						theOperation,
						theRequestDetails,
						theInputResource,
						theInputResourceId,
						theOutputResource,
						theRuleApplier);
			} else {
				List<IBaseResource> outputResources = AuthorizationInterceptor.toListOfResourcesAndExcludeContainer(
						theOutputResource, theRequestDetails.getFhirContext());
				return RuleImplOp.applyRulesToResponseResources(
						theRequestDetails, theRuleApplier, thePointcut, outputResources);
			}
		}
	}

	private void validateState() {
		if (isNotBlank(myInstanceFilter)) {
			Validate.isTrue(
					myAppliesToAnyInstance || isNotEmpty(myAppliesToInstancesOfType) || isNotEmpty(myAppliesToIds),
					"Instance filter is only supported for instance-level operations.");
		}
	}

	/**
	 * Must include the leading $
	 */
	public void setOperationName(String theOperationName) {
		myOperationName = theOperationName;
	}

	String getOperationName() {
		return myOperationName;
	}

	boolean isAppliesToServer() {
		return myAppliesToServer;
	}

	HashSet<Class<? extends IBaseResource>> getAppliesToTypes() {
		return myAppliesToTypes;
	}

	List<IIdType> getAppliesToIds() {
		return myAppliesToIds;
	}

	HashSet<Class<? extends IBaseResource>> getAppliesToInstancesOfType() {
		return myAppliesToInstancesOfType;
	}

	boolean isAppliesToAnyType() {
		return myAppliesToAnyType;
	}

	boolean isAppliesToAnyInstance() {
		return myAppliesToAnyInstance;
	}

	boolean isAppliesAtAnyLevel() {
		return myAppliesAtAnyLevel;
	}

	boolean isAllowAllResponses() {
		return myAllowAllResponses;
	}

	boolean isAllowAllResourcesAccess() {
		return myAllowAllResourcesAccess;
	}

	@Nullable
	public String getInstanceFilter() {
		return myInstanceFilter;
	}

	@Override
	@Nonnull
	protected ToStringBuilder toStringBuilder() {
		ToStringBuilder builder = super.toStringBuilder();
		builder.append("op", myOperationName);
		builder.append("appliesToServer", myAppliesToServer);
		builder.append("appliesToTypes", myAppliesToTypes);
		builder.append("appliesToIds", myAppliesToIds);
		builder.append("appliesToInstancesOfType", myAppliesToInstancesOfType);
		builder.append("appliesToAnyType", myAppliesToAnyType);
		builder.append("appliesToAnyInstance", myAppliesToAnyInstance);
		builder.append("appliesAtAnyLevel", myAppliesAtAnyLevel);
		builder.append("allowAllResponses", myAllowAllResponses);
		builder.append("allowAllResourcesAccess", myAllowAllResourcesAccess);
		return builder;
	}

	private boolean isBlockedByInstanceFilter(
			RequestDetails theRequestDetails,
			IIdType theTargetResourceId,
			IBaseResource theInputResource,
			RestOperationTypeEnum theOperation,
			IRuleApplier theRuleApplier) {

		if (isBlank(myInstanceFilter) || theTargetResourceId == null) {
			// nothing to block
			return false;
		}

		Optional<IBaseResource> oResource =
				getResourceForFilterCheck(theTargetResourceId, theInputResource, theRequestDetails, theRuleApplier);
		if (oResource.isEmpty()) {
			// could not find resource, block to be safe
			ourLog.debug("Could not find resource [{}] to apply filter [{}].", theTargetResourceId, myInstanceFilter);
			return true;
		}

		IBaseResource resource = oResource.get();
		FhirQueryRuleTester tester = new FhirQueryRuleTester(myInstanceFilter);

		IAuthRuleTester.RuleTestRequest ruleTestRequest =
				createRuleTestRequest(theOperation, theRequestDetails, theTargetResourceId, resource, theRuleApplier);

		// blocked if the resource does not match the filter
		boolean blocked = !tester.matches(ruleTestRequest);
		ourLog.debug(
				"Instance filter result: resourceId={}, filter={}, blocked={}",
				theTargetResourceId,
				myInstanceFilter,
				blocked);

		return blocked;
	}

	/**
	 * <p>Returns the resource that will be used for the instance filter check.</p>
	 * <p>Outcomes:</p>
	 * <ol>
	 *     <li>theTargetResourceId == theInputResource -> use theInputResource</li>
	 *     <li>theTargetResourceId != theInputResource -> try to fetch the full resource body using theTargetResourceId.</li>
	 * </ol>
	 */
	private Optional<IBaseResource> getResourceForFilterCheck(
			@Nonnull IIdType theTargetResourceId,
			@Nullable IBaseResource theInputResource,
			RequestDetails theRequestDetails,
			IRuleApplier theRuleApplier) {

		// see if resource body is available from request
		if (theInputResource != null && targetResourceIdMatchesInputResource(theTargetResourceId, theInputResource)) {
			return Optional.of(theInputResource);
		}

		// resource body not available, try to resolve
		IAuthResourceResolver resourceResolver = theRuleApplier.getAuthResourceResolver();
		if (resourceResolver == null) {
			return Optional.empty();
		}

		// This would happen for Observation/123/$meta-add:
		// theTargetResourceId = Observation/123
		// theInputResource = Parameters
		IBaseResource resource = resourceResolver.resolveResourceById(theRequestDetails, theTargetResourceId);
		return Optional.ofNullable(resource);
	}

	private boolean targetResourceIdMatchesInputResource(
			@Nonnull IIdType theTargetResourceId, @Nonnull IBaseResource theInputResource) {
		IIdType inputResourceId = theInputResource.getIdElement();
		if (inputResourceId == null || !inputResourceId.hasIdPart()) {
			return false;
		}

		return Objects.equals(
				theTargetResourceId.toUnqualifiedVersionless().getValue(),
				inputResourceId.toUnqualifiedVersionless().getValue());
	}

	private @Nullable IIdType getRequestResourceId(RequestDetails theRequestDetails) {
		if (theRequestDetails != null && theRequestDetails.getId() != null) {
			return theRequestDetails.getId();
		}
		return null;
	}
}
