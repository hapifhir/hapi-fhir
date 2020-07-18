package ca.uhn.fhir.empi.api;

import org.hl7.fhir.instance.model.api.IIdType;

public interface IEmpiBatchService {

	void runEmpiOnAllTargets(String theCriteria);

	void runEmpiOnTargetType(String theTargetType, String theCriteria);

	void runEmpiOnTarget(IIdType theId, String theTargetType);

}
