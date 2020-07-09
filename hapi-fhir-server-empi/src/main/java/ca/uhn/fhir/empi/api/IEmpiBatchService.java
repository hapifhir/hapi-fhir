package ca.uhn.fhir.empi.api;

public interface IEmpiBatchService {

	void runEmpiOnAllTargets();

	void runEmpiOnTargetType(String theTargetType);
}
