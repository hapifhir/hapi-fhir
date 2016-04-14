package ca.uhn.fhir.rest.server.interceptor.auth;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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

import java.util.Collection;
import java.util.Set;

import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.FhirTerser;

class Rule implements IAuthRule {

	private AppliesTypeEnum myAppliesTo;
	private Set<?> myAppliesToTypes;
	private String myClassifierCompartmentName;
	private Collection<? extends IIdType> myClassifierCompartmentOwners;
	private ClassifierTypeEnum myClassifierType;
	private RuleVerdictEnum myMode;
	private String myName;
	private RuleOpEnum myOp;
	private TransactionAppliesToEnum myTransactionAppliesToOp;

	public Rule(String theRuleName) {
		myName = theRuleName;
	}

	@Override
	public RuleVerdictEnum applyRule(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IBaseResource theInputResource, IBaseResource theOutputResource) {
		FhirContext ctx = theRequestDetails.getServer().getFhirContext();

		IBaseResource appliesTo;
		switch (myOp) {
		case READ:
			appliesTo = theOutputResource;
			break;
		case WRITE:
			appliesTo = theInputResource;
			break;
		case BATCH:
		case TRANSACTION:
			if (requestAppliesToTransaction(ctx, myOp, theInputResource)) {
				return myMode;
			} else {
				return RuleVerdictEnum.NO_DECISION;
			}
		case ALLOW_ALL:
			return RuleVerdictEnum.ALLOW;
		case DENY_ALL:
			return RuleVerdictEnum.DENY;
		case METADATA:
			if (theOperation == RestOperationTypeEnum.METADATA) {
				return myMode;
			} else {
				return RuleVerdictEnum.NO_DECISION;
			}
		default:
			// Should not happen
			throw new IllegalStateException("Unable to apply security to event of type " + theOperation);
		}

		switch (myAppliesTo) {
		case ALL_RESOURCES:
			break;
		case TYPES:
			if (myAppliesToTypes.contains(appliesTo.getClass()) == false) {
				return RuleVerdictEnum.NO_DECISION;
			}
			break;
		default:
			throw new IllegalStateException("Unable to apply security to event of applies to type " + myAppliesTo);
		}

		switch (myClassifierType) {
		case ANY_ID:
			break;
		case IN_COMPARTMENT:
			FhirTerser t = ctx.newTerser();
			boolean foundMatch = false;
			for (IIdType next : myClassifierCompartmentOwners) {
				if (t.isSourceInCompartmentForTarget(myClassifierCompartmentName, appliesTo, next)) {
					foundMatch = true;
					break;
				}
			}
			if (!foundMatch) {
				return RuleVerdictEnum.NO_DECISION;
			}
			break;
		default:
			throw new IllegalStateException("Unable to apply security to event of applies to type " + myAppliesTo);
		}

		return myMode;
	}

	private boolean requestAppliesToTransaction(FhirContext theContext, RuleOpEnum theOp, IBaseResource theInputResource) {
		IBaseBundle request = (IBaseBundle) theInputResource;
		String bundleType = BundleUtil.getBundleType(theContext, request);
		switch (theOp) {
		case TRANSACTION:
			return "transaction".equals(bundleType);
		case BATCH:
			return "batch".equals(bundleType);
		default:
			return false;
		}
	}

	@Override
	public String getName() {
		return myName;
	}

	public TransactionAppliesToEnum getTransactionAppliesToOp() {
		return myTransactionAppliesToOp;
	}

	public void setAppliesTo(AppliesTypeEnum theAppliesTo) {
		myAppliesTo = theAppliesTo;
	}

	public void setAppliesToTypes(Set<?> theAppliesToTypes) {
		myAppliesToTypes = theAppliesToTypes;
	}

	public void setClassifierCompartmentName(String theClassifierCompartmentName) {
		myClassifierCompartmentName = theClassifierCompartmentName;
	}

	public void setClassifierCompartmentOwners(Collection<? extends IIdType> theInCompartmentOwners) {
		myClassifierCompartmentOwners = theInCompartmentOwners;
	}

	public void setClassifierType(ClassifierTypeEnum theClassifierType) {
		myClassifierType = theClassifierType;
	}

	public void setMode(RuleVerdictEnum theRuleMode) {
		myMode = theRuleMode;
	}

	public Rule setOp(RuleOpEnum theRuleOp) {
		myOp = theRuleOp;
		return this;
	}

	public void setTransactionAppliesToOp(TransactionAppliesToEnum theOp) {
		myTransactionAppliesToOp = theOp;
	}
}
