package ca.uhn.fhir.jpa.term.snomedct;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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
