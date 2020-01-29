package ca.uhn.fhir.jpa.term.loinc;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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
import ca.uhn.fhir.jpa.term.IRecordHandler;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.apache.commons.lang3.StringUtils.trim;

public class LoincGroupFileHandler extends BaseLoincHandler implements IRecordHandler {

	public static final String VS_URI_PREFIX = "http://loinc.org/vs/";

	public LoincGroupFileHandler(Map<String, TermConcept> theCode2concept, List<ValueSet> theValueSets, List<ConceptMap> theConceptMaps, Properties theUploadProperties) {
		super(theCode2concept, theValueSets, theConceptMaps, theUploadProperties);
	}

	@Override
	public void accept(CSVRecord theRecord) {
		//"ParentGroupId","GroupId","Group","Archetype","Status","VersionFirstReleased"
		String parentGroupId = trim(theRecord.get("ParentGroupId"));
		String groupId = trim(theRecord.get("GroupId"));
		String groupName = trim(theRecord.get("Group"));

		ValueSet parentValueSet = getValueSet(parentGroupId, VS_URI_PREFIX + parentGroupId, null, null);
		parentValueSet
			.getCompose()
			.getIncludeFirstRep()
			.addValueSet(VS_URI_PREFIX + groupId);

		// Create group to set its name (terms are added in a different
		// handler)
		getValueSet(groupId, VS_URI_PREFIX + groupId, groupName, null);
	}


}
