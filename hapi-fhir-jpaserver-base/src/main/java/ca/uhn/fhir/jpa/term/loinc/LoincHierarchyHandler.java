package ca.uhn.fhir.jpa.term.loinc;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.term.IZipContentsHandlerCsv;
import org.apache.commons.csv.CSVRecord;

import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public class LoincHierarchyHandler implements IZipContentsHandlerCsv {

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

         parent.addPropertyCoding(
         	"child",
				ITermLoaderSvc.LOINC_URI,
				child.getCode(),
				child.getDisplay());

         child.addPropertyCoding(
         	"parent",
				ITermLoaderSvc.LOINC_URI,
				parent.getCode(),
				parent.getDisplay());
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
