package ca.uhn.fhir.jpa.term;

import java.util.List;

import ca.uhn.fhir.rest.method.RequestDetails;

public interface IHapiTerminologyLoaderSvc {

	String LOINC_URL = "http://loinc.org";
	String SCT_URL = "http://snomed.info/sct";

	UploadStatistics loadLoinc(List<byte[]> theZipBytes, RequestDetails theRequestDetails);

	UploadStatistics loadSnomedCt(List<byte[]> theZipBytes, RequestDetails theRequestDetails);

	public static class UploadStatistics {
		private final int myConceptCount;

		public UploadStatistics(int theConceptCount) {
			myConceptCount = theConceptCount;
		}

		public int getConceptCount() {
			return myConceptCount;
		}

	}

}
