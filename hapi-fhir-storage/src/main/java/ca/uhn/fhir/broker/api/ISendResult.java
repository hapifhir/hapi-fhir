package ca.uhn.fhir.broker.api;

public interface ISendResult {
	ISendResult FAILURE = () -> false;

	boolean isSuccessful();
}
