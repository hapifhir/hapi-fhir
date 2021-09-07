package ca.uhn.fhir.mdm.api;

public interface IMdmBatchJobSubmitterFactory {
	IMdmClearJobSubmitter getClearJobSubmitter();
}
