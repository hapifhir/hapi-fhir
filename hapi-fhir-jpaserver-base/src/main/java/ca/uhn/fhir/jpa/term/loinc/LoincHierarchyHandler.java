package ca.uhn.fhir.jpa.term.loinc;

import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import ca.uhn.fhir.jpa.term.IRecordHandler;
import org.apache.commons.csv.CSVRecord;

import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public class LoincHierarchyHandler implements IRecordHandler {

   private Map<String, TermConcept> myCode2Concept;
   private TermCodeSystemVersion myCodeSystemVersion;

   public LoincHierarchyHandler(TermCodeSystemVersion theCodeSystemVersion, Map<String, TermConcept> theCode2concept) {
      myCodeSystemVersion = theCodeSystemVersion;
      myCode2Concept = theCode2concept;
   }

   @Override
   public void accept(CSVRecord theRecord) {
      String parentCode = trim(theRecord.get("IMMEDIATE_PARENT"));
      String childCode = trim(theRecord.get("CODE"));
      String childCodeText = trim(theRecord.get("CODE_TEXT"));

      if (isNotBlank(parentCode) && isNotBlank(childCode)) {
         TermConcept parent = getOrCreate(parentCode, "(unknown)");
         TermConcept child = getOrCreate(childCode, childCodeText);

         parent.addChild(child, TermConceptParentChildLink.RelationshipTypeEnum.ISA);
      }
   }

   private TermConcept getOrCreate(String theCode, String theDisplay) {
      TermConcept retVal = myCode2Concept.get(theCode);
      if (retVal == null) {
         retVal = new TermConcept();
         retVal.setCodeSystemVersion(myCodeSystemVersion);
         retVal.setCode(theCode);
         retVal.setDisplay(theDisplay);
         myCode2Concept.put(theCode, retVal);
      }
      return retVal;
   }

}
