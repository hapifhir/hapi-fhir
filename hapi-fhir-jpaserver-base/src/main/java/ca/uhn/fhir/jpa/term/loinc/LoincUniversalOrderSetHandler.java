package ca.uhn.fhir.jpa.term.loinc;

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

import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.term.IHapiTerminologyLoaderSvc;
import ca.uhn.fhir.jpa.term.IRecordHandler;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.*;

import static org.apache.commons.lang3.StringUtils.trim;

public class LoincUniversalOrderSetHandler extends BaseLoincHandler implements IRecordHandler {

	public static final String VS_ID = "loinc-universal-order-set-vs";
	public static final String VS_URI = "http://loinc.org/fhir/loinc-universal-order-set";
	public static final String VS_NAME = "LOINC Universal Order Set";

	public LoincUniversalOrderSetHandler(Map<String, TermConcept> theCode2concept, List<ValueSet> theValueSets, List<ConceptMap> theConceptMaps, Properties theUploadProperties) {
		super(theCode2concept, theValueSets, theConceptMaps, theUploadProperties);
	}

	@Override
	public void accept(CSVRecord theRecord) {
		String loincNumber = trim(theRecord.get("LOINC_NUM"));
		String displayName = trim(theRecord.get("LONG_COMMON_NAME"));
		String orderObs = trim(theRecord.get("ORDER_OBS"));

		ValueSet valueSet = getValueSet(VS_ID, VS_URI, VS_NAME, null);
		addCodeAsIncludeToValueSet(valueSet, IHapiTerminologyLoaderSvc.LOINC_URI, loincNumber, displayName);
	}


}
