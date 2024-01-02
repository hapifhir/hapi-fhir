/*
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

import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor.Verdict;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

abstract class BaseRule implements IAuthRule {
	private String myName;
	private PolicyEnum myMode;
	private List<IAuthRuleTester> myTesters;

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

	private boolean applyTesters(
			RestOperationTypeEnum theOperation,
			RequestDetails theRequestDetails,
			IIdType theInputResourceId,
			IBaseResource theInputResource,
			IBaseResource theOutputResource,
			IRuleApplier theRuleApplier) {
		assert !(theInputResource != null && theOutputResource != null);

		boolean retVal = true;
		if (theOutputResource == null) {
			IAuthRuleTester.RuleTestRequest inputRequest = new IAuthRuleTester.RuleTestRequest(
					myMode, theOperation, theRequestDetails, theInputResourceId, theInputResource, theRuleApplier);

			for (IAuthRuleTester next : getTesters()) {
				if (!next.matches(inputRequest)) {
					retVal = false;
					break;
				}
			}
		} else {
			IAuthRuleTester.RuleTestRequest outputRequest = new IAuthRuleTester.RuleTestRequest(
					myMode,
					theOperation,
					theRequestDetails,
					theOutputResource.getIdElement(),
					theOutputResource,
					theRuleApplier);
			for (IAuthRuleTester next : getTesters()) {
				if (!next.matchesOutput(outputRequest)) {
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

	public List<IAuthRuleTester> getTesters() {
		if (myTesters == null) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableList(myTesters);
	}

	Verdict newVerdict(
			RestOperationTypeEnum theOperation,
			RequestDetails theRequestDetails,
			IBaseResource theInputResource,
			IIdType theInputResourceId,
			IBaseResource theOutputResource,
			IRuleApplier theRuleApplier) {
		if (!applyTesters(
				theOperation,
				theRequestDetails,
				theInputResourceId,
				theInputResource,
				theOutputResource,
				theRuleApplier)) {
			return null;
		}
		return new Verdict(myMode, this);
	}

	protected boolean isResourceAccess(Pointcut thePointcut) {
		return thePointcut.equals(Pointcut.STORAGE_PREACCESS_RESOURCES)
				|| thePointcut.equals(Pointcut.STORAGE_PRESHOW_RESOURCES);
	}

	@Override
	public String toString() {
		ToStringBuilder builder = toStringBuilder();
		return builder.toString();
	}

	ToStringBuilder toStringBuilder() {
		ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		builder.append("testers", myTesters);
		return builder;
	}
}
