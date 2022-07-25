package ca.uhn.fhir.batch2.importpull.svc;

import ca.uhn.fhir.jpa.bulk.imprt.model.BulkImportJobFileJson;
import ca.uhn.fhir.jpa.bulk.imprt.model.BulkImportJobJson;

public interface IBulkImportPullSvc {

	BulkImportJobJson fetchJobById(String theId);

	String fetchJobDescription(String theId, int theFileIndex);

	BulkImportJobFileJson fetchFileByIdAndIndex(String theId, int theFileIndex);


}
