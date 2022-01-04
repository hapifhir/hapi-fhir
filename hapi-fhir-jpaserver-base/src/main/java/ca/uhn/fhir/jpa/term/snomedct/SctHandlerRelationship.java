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
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import ca.uhn.fhir.jpa.term.IZipContentsHandlerCsv;
import org.apache.commons.csv.CSVRecord;

import java.util.*;

public final class SctHandlerRelationship implements IZipContentsHandlerCsv {
   private final Map<String, TermConcept> myCode2concept;
   private final TermCodeSystemVersion myCodeSystemVersion;
   private final Map<String, TermConcept> myRootConcepts;

   public SctHandlerRelationship(TermCodeSystemVersion theCodeSystemVersion, HashMap<String, TermConcept> theRootConcepts, Map<String, TermConcept> theCode2concept) {
      myCodeSystemVersion = theCodeSystemVersion;
      myRootConcepts = theRootConcepts;
      myCode2concept = theCode2concept;
   }

   @Override
   public void accept(CSVRecord theRecord) {
      Set<String> ignoredTypes = new HashSet<String>();
      ignoredTypes.add("Method (attribute)");
      ignoredTypes.add("Direct device (attribute)");
      ignoredTypes.add("Has focus (attribute)");
      ignoredTypes.add("Access instrument");
      ignoredTypes.add("Procedure site (attribute)");
      ignoredTypes.add("Causative agent (attribute)");
      ignoredTypes.add("Course (attribute)");
      ignoredTypes.add("Finding site (attribute)");
      ignoredTypes.add("Has definitional manifestation (attribute)");

      String sourceId = theRecord.get("sourceId");
      String destinationId = theRecord.get("destinationId");
      String typeId = theRecord.get("typeId");
      boolean active = "1".equals(theRecord.get("active"));

      TermConcept typeConcept = myCode2concept.get(typeId);
      TermConcept sourceConcept = myCode2concept.get(sourceId);
      TermConcept targetConcept = myCode2concept.get(destinationId);
      if (sourceConcept != null && targetConcept != null && typeConcept != null) {
         if (typeConcept.getDisplay().equals("Is a (attribute)")) {
            TermConceptParentChildLink.RelationshipTypeEnum relationshipType = TermConceptParentChildLink.RelationshipTypeEnum.ISA;
            if (!sourceId.equals(destinationId)) {
               if (active) {
                  TermConceptParentChildLink link = new TermConceptParentChildLink();
                  link.setChild(sourceConcept);
                  link.setParent(targetConcept);
                  link.setRelationshipType(relationshipType);
                  link.setCodeSystem(myCodeSystemVersion);

                  targetConcept.addChild(sourceConcept, relationshipType);
               } else {
                  // not active, so we're removing any existing links
                  for (TermConceptParentChildLink next : new ArrayList<TermConceptParentChildLink>(targetConcept.getChildren())) {
                     if (next.getRelationshipType() == relationshipType) {
                        if (next.getChild().getCode().equals(sourceConcept.getCode())) {
                           next.getParent().getChildren().remove(next);
                           next.getChild().getParents().remove(next);
                        }
                     }
                  }
               }
            }
         } else if (ignoredTypes.contains(typeConcept.getDisplay())) {
            // ignore
         } else {
            // ourLog.warn("Unknown relationship type: {}/{}", typeId, typeConcept.getDisplay());
         }
      }
   }

}
