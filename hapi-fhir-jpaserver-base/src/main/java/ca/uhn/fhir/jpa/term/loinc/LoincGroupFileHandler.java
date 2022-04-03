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

import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.term.IZipContentsHandlerCsv;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_CODESYSTEM_VERSION;
import static org.apache.commons.lang3.StringUtils.trim;

public class LoincGroupFileHandler extends BaseLoincHandler implements IZipContentsHandlerCsv {

	public static final String VS_URI_PREFIX = "http://loinc.org/vs/";

	public LoincGroupFileHandler(Map<String, TermConcept> theCode2concept, List<ValueSet> theValueSets,
			List<ConceptMap> theConceptMaps, Properties theUploadProperties, String theCopyrightStatement) {
		super(theCode2concept, theValueSets, theConceptMaps, theUploadProperties, theCopyrightStatement);
	}

	@Override
	public void accept(CSVRecord theRecord) {
		//"ParentGroupId","GroupId","Group","Archetype","Status","VersionFirstReleased"
		String parentGroupId = trim(theRecord.get("ParentGroupId"));
		String groupId = trim(theRecord.get("GroupId"));
		String groupName = trim(theRecord.get("Group"));

		String codeSystemVersionId = myUploadProperties.getProperty(LOINC_CODESYSTEM_VERSION.getCode());
		String parentGroupValueSetId;
		String groupValueSetId;
		if (codeSystemVersionId != null) {
			parentGroupValueSetId = parentGroupId + "-" + codeSystemVersionId;
			groupValueSetId = groupId + "-" + codeSystemVersionId;
		} else {
			parentGroupValueSetId = parentGroupId;
			groupValueSetId = groupId;
		}

		ValueSet parentValueSet = getValueSet(parentGroupValueSetId, VS_URI_PREFIX + parentGroupId, null, null);
		parentValueSet
			.getCompose()
			.getIncludeFirstRep()
			.addValueSet(VS_URI_PREFIX + groupId);

		// Create group to set its name (terms are added in a different
		// handler)
		getValueSet(groupValueSetId, VS_URI_PREFIX + groupId, groupName, null);
	}


}
