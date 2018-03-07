package ca.uhn.fhir.jpa.term.snomedct;

import ca.uhn.fhir.jpa.term.IRecordHandler;
import org.apache.commons.csv.CSVRecord;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class SctHandlerConcept implements IRecordHandler {

   private Set<String> myValidConceptIds;
   private Map<String, String> myConceptIdToMostRecentDate = new HashMap<String, String>();

   public SctHandlerConcept(Set<String> theValidConceptIds) {
      myValidConceptIds = theValidConceptIds;
   }

   @Override
   public void accept(CSVRecord theRecord) {
      String id = theRecord.get("id");
      String date = theRecord.get("effectiveTime");

      if (!myConceptIdToMostRecentDate.containsKey(id) || myConceptIdToMostRecentDate.get(id).compareTo(date) < 0) {
         boolean active = "1".equals(theRecord.get("active"));
         if (active) {
            myValidConceptIds.add(id);
         } else {
            myValidConceptIds.remove(id);
         }
         myConceptIdToMostRecentDate.put(id, date);
      }

   }
}
