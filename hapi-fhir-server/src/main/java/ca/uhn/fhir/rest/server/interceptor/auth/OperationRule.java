package ca.uhn.fhir.rest.server.interceptor.auth;

/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor.Verdict;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.HashSet;
import java.util.List;

class OperationRule extends BaseRule implements IAuthRule {

	private RuleBuilder.ITenantApplicabilityChecker myTenentApplicabilityChecker;
	private String myOperationName;
	private boolean myAppliesToServer;
	private HashSet<Class<? extends IBaseResource>> myAppliesToTypes;
	private List<IIdType> myAppliesToIds;
	private HashSet<Class<? extends IBaseResource>> myAppliesToInstancesOfType;
	private boolean myAppliesToAnyType;
	private boolean myAppliesToAnyInstance;
	private boolean myAppliesAtAnyLevel;

	public OperationRule(String theRuleName) {
		super(theRuleName);
	}

	public void appliesAtAnyLevel(boolean theAppliesAtAnyLevel) {
		myAppliesAtAnyLevel = theAppliesAtAnyLevel;
	}

	public void appliesToAnyInstance() {
		myAppliesToAnyInstance = true;
	}

	public void appliesToAnyType() {
		myAppliesToAnyType = true;
	}

	public void appliesToInstances(List<IIdType> theAppliesToIds) {
		myAppliesToIds = theAppliesToIds;
	}

	public void appliesToInstancesOfType(HashSet<Class<? extends IBaseResource>> theAppliesToTypes) {
		myAppliesToInstancesOfType = theAppliesToTypes;
	}

	public void appliesToServer() {
		myAppliesToServer = true;
	}

	public void appliesToTypes(HashSet<Class<? extends IBaseResource>> theAppliesToTypes) {
		myAppliesToTypes = theAppliesToTypes;
	}

	@Override
	public Verdict applyRule(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IBaseResource theInputResource, IIdType theInputResourceId, IBaseResource theOutputResource, IRuleApplier theRuleApplier) {
		FhirContext ctx = theRequestDetails.getServer().getFhirContext();

		if (myTenentApplicabilityChecker != null) {
			if (!myTenentApplicabilityChecker.applies(theRequestDetails)) {
				return null;
			}
		}

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
						String resName = ctx.getResourceDefinition(next).getName();
						if (resName.equals(theRequestDetails.getResourceName())) {
							applies = true;
							break;
						}
					}
				}
				break;
			case EXTENDED_OPERATION_INSTANCE:
				if (myAppliesToAnyInstance || myAppliesAtAnyLevel) {
					applies = true;
				} else if (theInputResourceId != null) {
					if (myAppliesToIds != null) {
						String instanceId = theInputResourceId.toUnqualifiedVersionless().getValue();
						for (IIdType next : myAppliesToIds) {
							if (next.toUnqualifiedVersionless().getValue().equals(instanceId)) {
								applies = true;
								break;
							}
						}
					}
					if (myAppliesToInstancesOfType != null) {
						// TODO: Convert to a map of strings and keep the result
						for (Class<? extends IBaseResource> next : myAppliesToInstancesOfType) {
							String resName = ctx.getResourceDefinition(next).getName();
							if (resName.equals(theInputResourceId.getResourceType())) {
								applies = true;
								break;
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

		if (!applyTesters(theOperation, theRequestDetails, theInputResourceId, theInputResource, theOutputResource)) {
			return null;
		}

		return newVerdict();
	}

	public String getOperationName() {
		return myOperationName;
	}

	/**
	 * Must include the leading $
	 */
	public void setOperationName(String theOperationName) {
		myOperationName = theOperationName;
	}

	public void setTenentApplicabilityChecker(RuleBuilder.ITenantApplicabilityChecker theTenentApplicabilityChecker) {
		myTenentApplicabilityChecker = theTenentApplicabilityChecker;
	}

}
