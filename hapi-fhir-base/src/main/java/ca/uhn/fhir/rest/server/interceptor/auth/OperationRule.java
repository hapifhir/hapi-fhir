package ca.uhn.fhir.rest.server.interceptor.auth;

import java.util.HashSet;
import java.util.List;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor.Verdict;

class OperationRule extends BaseRule implements IAuthRule {

	public OperationRule(String theRuleName) {
		super(theRuleName);
	}

	private String myOperationName;
	private boolean myAppliesToServer;
	private HashSet<Class<? extends IBaseResource>> myAppliesToTypes;
	private List<IIdType> myAppliesToIds;

	/**
	 * Must include the leading $
	 */
	public void setOperationName(String theOperationName) {
		myOperationName = theOperationName;
	}

	public String getOperationName() {
		return myOperationName;
	}

	@Override
	public Verdict applyRule(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IBaseResource theInputResource, IBaseResource theOutputResource, IRuleApplier theRuleApplier) {
		FhirContext ctx = theRequestDetails.getServer().getFhirContext();

		boolean applies = false;
		switch (theOperation) {
		case EXTENDED_OPERATION_SERVER:
			if (myAppliesToServer) {
				applies = true;
			}
			break;
		case EXTENDED_OPERATION_TYPE:
			if (myAppliesToTypes != null) {
				for (Class<? extends IBaseResource> next : myAppliesToTypes) {
					String resName = ctx.getResourceDefinition(theRequestDetails.getResourceName()).getName();
					if (resName.equals(theRequestDetails.getResourceName())) {
						applies = true;
						break;
					}
				}
			}
			break;
		case EXTENDED_OPERATION_INSTANCE:
			if (myAppliesToIds != null) {
				String instanceId = theRequestDetails.getId().toUnqualifiedVersionless().getValue();
				for (IIdType next : myAppliesToIds) {
					if (next.toUnqualifiedVersionless().getValue().equals(instanceId)) {
						applies = true;
						break;
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
		
		return newVerdict();
	}

	public void appliesToServer() {
		myAppliesToServer = true;
	}

	public void appliesToTypes(HashSet<Class<? extends IBaseResource>> theAppliesToTypes) {
		myAppliesToTypes = theAppliesToTypes;
	}

	public void appliesToInstances(List<IIdType> theAppliesToIds) {
		myAppliesToIds = theAppliesToIds;
	}

}
