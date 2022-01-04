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
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.term.IZipContentsHandlerCsv;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_CODESYSTEM_VERSION;
import static org.apache.commons.lang3.StringUtils.trim;

public class BaseLoincTop2000LabResultsHandler extends BaseLoincHandler implements IZipContentsHandlerCsv {

	private String myValueSetId;
	private String myValueSetUri;
	private String myValueSetName;

	public BaseLoincTop2000LabResultsHandler(Map<String, TermConcept> theCode2concept, List<ValueSet> theValueSets,
			String theValueSetId, String theValueSetUri, String theValueSetName, List<ConceptMap> theConceptMaps,
			Properties theUploadProperties, String theCopyrightStatement) {
		super(theCode2concept, theValueSets, theConceptMaps, theUploadProperties, theCopyrightStatement);
		String versionId = myUploadProperties.getProperty(LOINC_CODESYSTEM_VERSION.getCode());
		if (versionId != null) {
			myValueSetId = theValueSetId + "-" + versionId;
		} else {
			myValueSetId = theValueSetId;
		}
		myValueSetUri = theValueSetUri;
		myValueSetName = theValueSetName;
	}

	@Override
	public void accept(CSVRecord theRecord) {
		String loincNumber = trim(theRecord.get("LOINC #"));
		String displayName = trim(theRecord.get("Long Common Name"));

		ValueSet valueSet = getValueSet(myValueSetId, myValueSetUri, myValueSetName, null);
		addCodeAsIncludeToValueSet(valueSet, ITermLoaderSvc.LOINC_URI, loincNumber, displayName);
	}

}
