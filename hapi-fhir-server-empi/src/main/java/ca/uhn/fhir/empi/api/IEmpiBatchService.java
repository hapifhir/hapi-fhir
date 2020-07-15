package ca.uhn.fhir.empi.api;

import org.hl7.fhir.r4.model.StringType;

public interface IEmpiBatchService {

	void runEmpiOnAllTargets(StringType theCriteria);

	void runEmpiOnTargetType(String theTargetType, StringType theCriteria);
}
