package ca.uhn.fhir.mdm.blocklist.svc;

import org.hl7.fhir.instance.model.api.IAnyResource;

public interface IBlockRuleEvaluationSvc {

	/**
	 * Determines if the provided resource is blocked from
	 * mdm matching or not.
	 * @param theResource - the resource to assess
	 * @return - true: no mdm matching should be done
	 * 				(a golden resource should still be created)
	 * 			false: mdm matching should continue as normal
	 */
	boolean isMdmMatchingBlocked(IAnyResource theResource);
}
