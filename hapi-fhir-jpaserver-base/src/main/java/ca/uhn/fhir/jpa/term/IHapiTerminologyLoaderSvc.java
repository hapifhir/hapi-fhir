package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.rest.method.RequestDetails;

public interface IHapiTerminologyLoaderSvc {

	String SCT_URL = "http://snomed.info/sct";

	UploadStatistics loadSnomedCt(byte[] theZipBytes, RequestDetails theRequestDetails);

	public static class UploadStatistics {
		private int myConceptCount;

		public int getConceptCount() {
			return myConceptCount;
		}

		public UploadStatistics setConceptCount(int theConceptCount) {
			myConceptCount = theConceptCount;
			return this;
		}
	}

}
