package ca.uhn.fhir.rest.server.interceptor.auth;

import java.util.Collection;
import java.util.Set;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.util.FhirTerser;

class Rule implements IAuthRule {

	private AppliesTypeEnum myAppliesTo;
	private Set<?> myAppliesToTypes;
	private String myClassifierCompartmentName;
	private Collection<? extends IIdType> myClassifierCompartmentOwners;
	private ClassifierTypeEnum myClassifierType;
	private RuleModeEnum myMode;
	private String myName;
	private RuleOpEnum myOp;
	private TransactionAppliesToEnum myTransactionAppliesToOp;

	public Rule(String theRuleName) {
		myName = theRuleName;
	}

	@Override
	public RuleModeEnum applyRule(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IBaseResource theInputResource, IBaseResource theOutputResource) {
		FhirContext ctx = theRequestDetails.getServer().getFhirContext();

		IBaseResource appliesTo;
		switch (myOp) {
		case READ:
			appliesTo = theOutputResource;
			break;
		case WRITE:
			appliesTo = theInputResource;
			break;
		case TRANSACTION:
			return myMode;
		case ALLOW_ALL:
			return RuleModeEnum.ALLOW;
		case DENY_ALL:
			return RuleModeEnum.DENY;
		default:
			// Should not happen
			throw new IllegalStateException("Unable to apply security to event of type " + theOperation);
		}

		switch (myAppliesTo) {
		case ALL_RESOURCES:
			break;
		case TYPES:
			if (myAppliesToTypes.contains(appliesTo.getClass()) == false) {
				return RuleModeEnum.NO_DECISION;
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
				return RuleModeEnum.NO_DECISION;
			}
			break;
		default:
			throw new IllegalStateException("Unable to apply security to event of applies to type " + myAppliesTo);
		}

		return myMode;
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

	public void setMode(RuleModeEnum theRuleMode) {
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
