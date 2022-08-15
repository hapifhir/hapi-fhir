package ca.uhn.fhir.jpa.term.snomedct;

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
import ca.uhn.fhir.jpa.term.IZipContentsHandlerCsv;
import ca.uhn.fhir.jpa.term.TermLoaderSvcImpl;
import org.apache.commons.csv.CSVRecord;

import java.util.Map;
import java.util.Set;

public final class SctHandlerDescription implements IZipContentsHandlerCsv {
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

      TermConcept concept = TermLoaderSvcImpl.getOrCreateConcept(myId2concept, id);
      concept.setCode(conceptId);
      concept.setDisplay(term);
      concept.setCodeSystemVersion(myCodeSystemVersion);
      myCode2concept.put(conceptId, concept);
   }
}
