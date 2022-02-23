package ca.uhn.fhir.jpa.term.custom;

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

import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import ca.uhn.fhir.jpa.term.IZipContentsHandlerCsv;
import ca.uhn.fhir.util.ValidateUtil;
import org.apache.commons.csv.CSVRecord;

import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public class HierarchyHandler implements IZipContentsHandlerCsv {

	public static final String PARENT = "PARENT";
	public static final String CHILD = "CHILD";
	private final Map<String, TermConcept> myCode2Concept;

	public HierarchyHandler(Map<String, TermConcept> theCode2concept) {
		myCode2Concept = theCode2concept;
	}

	@Override
	public void accept(CSVRecord theRecord) {
		String parent = trim(theRecord.get(PARENT));
		String child = trim(theRecord.get(CHILD));
		if (isNotBlank(parent) && isNotBlank(child)) {

			TermConcept childConcept = myCode2Concept.get(child);
			ValidateUtil.isNotNullOrThrowUnprocessableEntity(childConcept, "Child code %s not found in file", child);

			TermConcept parentConcept = myCode2Concept.get(parent);
			ValidateUtil.isNotNullOrThrowUnprocessableEntity(parentConcept, "Parent code %s not found in file", child);

			parentConcept.addChild(childConcept, TermConceptParentChildLink.RelationshipTypeEnum.ISA);
		}
	}
}
