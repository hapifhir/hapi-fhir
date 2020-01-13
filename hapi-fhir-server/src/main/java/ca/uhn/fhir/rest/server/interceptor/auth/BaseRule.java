package ca.uhn.fhir.rest.server.interceptor.auth;

/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor.Verdict;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

abstract class BaseRule implements IAuthRule {
	private String myName;
	private PolicyEnum myMode;
	private List<IAuthRuleTester> myTesters;
	private RuleBuilder.ITenantApplicabilityChecker myTenantApplicabilityChecker;

	BaseRule(String theRuleName) {
		myName = theRuleName;
	}

	public void addTester(IAuthRuleTester theTester) {
		Validate.notNull(theTester, "theTester must not be null");
		if (myTesters == null) {
			myTesters = new ArrayList<>();
		}
		myTesters.add(theTester);
	}

	public void addTesters(List<IAuthRuleTester> theTesters) {
		theTesters.forEach(this::addTester);
	}

	boolean applyTesters(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IIdType theInputResourceId, IBaseResource theInputResource, IBaseResource theOutputResource) {
		boolean retVal = true;
		if (theOutputResource == null) {
			for (IAuthRuleTester next : getTesters()) {
				if (!next.matches(theOperation, theRequestDetails, theInputResourceId, theInputResource)) {
					retVal = false;
					break;
				}
			}
		}
		return retVal;
	}

	PolicyEnum getMode() {
		return myMode;
	}

	BaseRule setMode(PolicyEnum theRuleMode) {
		myMode = theRuleMode;
		return this;
	}

	@Override
	public String getName() {
		return myName;
	}

	public RuleBuilder.ITenantApplicabilityChecker getTenantApplicabilityChecker() {
		return myTenantApplicabilityChecker;
	}

	public final void setTenantApplicabilityChecker(RuleBuilder.ITenantApplicabilityChecker theTenantApplicabilityChecker) {
		myTenantApplicabilityChecker = theTenantApplicabilityChecker;
	}

	public List<IAuthRuleTester> getTesters() {
		if (myTesters == null) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableList(myTesters);
	}

	public boolean isOtherTenant(RequestDetails theRequestDetails) {
		boolean otherTenant = false;
		if (getTenantApplicabilityChecker() != null) {
			if (!getTenantApplicabilityChecker().applies(theRequestDetails)) {
				otherTenant = true;
			}
		}
		return otherTenant;
	}

	Verdict newVerdict() {
		return new Verdict(myMode, this);
	}
}
