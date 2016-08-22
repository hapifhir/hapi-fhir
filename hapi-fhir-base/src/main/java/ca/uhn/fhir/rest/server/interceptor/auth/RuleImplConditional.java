package ca.uhn.fhir.rest.server.interceptor.auth;

import java.util.Set;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor.Verdict;

public class RuleImplConditional extends BaseRule implements IAuthRule {

	private AppliesTypeEnum myAppliesTo;
	private Set<?> myAppliesToTypes;
	private RestOperationTypeEnum myOperationType;

	public RuleImplConditional(String theRuleName) {
		super(theRuleName);
	}

	@Override
	public Verdict applyRule(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IBaseResource theInputResource, IIdType theInputResourceId, IBaseResource theOutputResource,
			IRuleApplier theRuleApplier) {

		if (theInputResourceId != null) {
			return null;
		}
		
		if (theOperation == myOperationType) {
			switch (myAppliesTo) {
			case ALL_RESOURCES:
				break;
			case TYPES:
				if (theInputResource == null || !myAppliesToTypes.contains(theInputResource.getClass())) {
					return null;
				}
				break;
			}

			if (theRequestDetails.getConditionalUrl(myOperationType) == null) {
				return null;
			}

			return newVerdict();
		}

		return null;
	}

	void setAppliesTo(AppliesTypeEnum theAppliesTo) {
		myAppliesTo = theAppliesTo;
	}

	void setAppliesToTypes(Set<?> theAppliesToTypes) {
		myAppliesToTypes = theAppliesToTypes;
	}

	void setOperationType(RestOperationTypeEnum theOperationType) {
		myOperationType = theOperationType;
	}

}
