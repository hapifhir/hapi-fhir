package ca.uhn.fhir.jpa.term.snomedct;

import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.term.IRecordHandler;
import ca.uhn.fhir.jpa.term.TerminologyLoaderSvcImpl;
import org.apache.commons.csv.CSVRecord;

import java.util.Map;
import java.util.Set;

public final class SctHandlerDescription implements IRecordHandler {
   private final Map<String, TermConcept> myCode2concept;
   private final TermCodeSystemVersion myCodeSystemVersion;
   private final Map<String, TermConcept> myId2concept;
   private Set<String> myValidConceptIds;

   public SctHandlerDescription(Set<String> theValidConceptIds, Map<String, TermConcept> theCode2concept, Map<String, TermConcept> theId2concept, TermCodeSystemVersion theCodeSystemVersion) {
      myCode2concept = theCode2concept;
      myId2concept = theId2concept;
      myCodeSystemVersion = theCodeSystemVersion;
      myValidConceptIds = theValidConceptIds;
   }

   @Override
   public void accept(CSVRecord theRecord) {
      String id = theRecord.get("id");
      boolean active = "1".equals(theRecord.get("active"));
      if (!active) {
         return;
      }
      String conceptId = theRecord.get("conceptId");
      if (!myValidConceptIds.contains(conceptId)) {
         return;
      }

      String term = theRecord.get("term");

      TermConcept concept = TerminologyLoaderSvcImpl.getOrCreateConcept(myCodeSystemVersion, myId2concept, id);
      concept.setCode(conceptId);
      concept.setDisplay(term);
      myCode2concept.put(conceptId, concept);
   }
}
