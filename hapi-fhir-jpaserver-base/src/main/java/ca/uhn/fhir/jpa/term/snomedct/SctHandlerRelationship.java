/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.term.snomedct;

import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import ca.uhn.fhir.jpa.term.IZipContentsHandlerCsv;
import org.apache.commons.csv.CSVRecord;

import java.util.ArrayList;
import java.util.Map;

public final class SctHandlerRelationship implements IZipContentsHandlerCsv {
	private final Map<String, TermConcept> myCode2concept;
	private final TermCodeSystemVersion myCodeSystemVersion;

	public SctHandlerRelationship(
			TermCodeSystemVersion theCodeSystemVersion, Map<String, TermConcept> theCode2concept) {
		myCodeSystemVersion = theCodeSystemVersion;
		myCode2concept = theCode2concept;
	}

	@Override
	public void accept(CSVRecord theRecord) {
		String sourceId = theRecord.get("sourceId");
		String destinationId = theRecord.get("destinationId");
		String typeId = theRecord.get("typeId");
		boolean active = "1".equals(theRecord.get("active"));

		TermConcept sourceConcept = myCode2concept.get(sourceId);
		TermConcept targetConcept = myCode2concept.get(destinationId);
		// The concept with ID "116680003" denotes the concept "Is a (attribute)".
		// https://docs.snomed.org/snomed-international-documents/snomed-ct-glossary/r/relationship-type
		if (sourceConcept != null
				&& targetConcept != null
				&& "116680003".equals(typeId)
				&& !sourceId.equals(destinationId)) {
			TermConceptParentChildLink.RelationshipTypeEnum relationshipType =
					TermConceptParentChildLink.RelationshipTypeEnum.ISA;
			if (active) {
				TermConceptParentChildLink link = new TermConceptParentChildLink();
				link.setChild(sourceConcept);
				link.setParent(targetConcept);
				link.setRelationshipType(relationshipType);
				link.setCodeSystem(myCodeSystemVersion);

				targetConcept.addChild(sourceConcept, relationshipType);
			} else {
				// not active, so we're removing any existing links
				for (TermConceptParentChildLink next : new ArrayList<>(targetConcept.getChildren())) {
					if (next.getRelationshipType() == relationshipType) {
						if (next.getChild().getCode().equals(sourceConcept.getCode())) {
							next.getParent().getChildren().remove(next);
							next.getChild().getParents().remove(next);
						}
					}
				}
			}
		}
	}
}
