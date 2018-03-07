package ca.uhn.fhir.jpa.term;

import org.apache.commons.csv.CSVRecord;

public interface IRecordHandler {
   void accept(CSVRecord theRecord);
}
