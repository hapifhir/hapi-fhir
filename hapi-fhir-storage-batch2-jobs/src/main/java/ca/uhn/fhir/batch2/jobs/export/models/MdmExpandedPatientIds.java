package ca.uhn.fhir.batch2.jobs.export.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class MdmExpandedPatientIds extends BulkExportJobBase {

	/**
	 * List of Id objects for serialization;
	 * Using TypedPidJson, but all types will be Patient
	 */
	@JsonProperty("patientIds")
	private List<PatientIdAndPidJson> myExpandedPatientIds;

	public List<PatientIdAndPidJson> getExpandedPatientIds() {
		if (myExpandedPatientIds == null) {
			myExpandedPatientIds = new ArrayList<>();
		}
		return myExpandedPatientIds;
	}

	public void setExpandedPatientIds(List<PatientIdAndPidJson> theExpandedPatientIds) {
		myExpandedPatientIds = theExpandedPatientIds;
	}

	public void addExpandedPatientId(PatientIdAndPidJson theId) {
		getExpandedPatientIds().add(theId);
	}
}
