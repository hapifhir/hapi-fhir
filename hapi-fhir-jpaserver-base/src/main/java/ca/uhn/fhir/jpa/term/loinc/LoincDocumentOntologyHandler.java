package ca.uhn.fhir.jpa.term.loinc;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.apache.commons.lang3.StringUtils.trim;

public class LoincDocumentOntologyHandler extends BaseLoincHandler implements IRecordHandler {

	public static final String DOCUMENT_ONTOLOGY_CODES_VS_ID = "loinc-document-ontology";
	public static final String DOCUMENT_ONTOLOGY_CODES_VS_URI = "http://loinc.org/vs/loinc-document-ontology";
	public static final String DOCUMENT_ONTOLOGY_CODES_VS_NAME = "LOINC Document Ontology Codes";
	private final Map<String, TermConcept> myCode2Concept;

	public LoincDocumentOntologyHandler(Map<String, TermConcept> theCode2concept, Map<String, CodeSystem.PropertyType> thePropertyNames, List<ValueSet> theValueSets, List<ConceptMap> theConceptMaps, Properties theUploadProperties) {
		super(theCode2concept, theValueSets, theConceptMaps, theUploadProperties);
		myCode2Concept = theCode2concept;
	}

	@Override
	public void accept(CSVRecord theRecord) {

		String loincNumber = trim(theRecord.get("LoincNumber"));
		String partNumber = trim(theRecord.get("PartNumber"));
		String partTypeName = trim(theRecord.get("PartTypeName"));
		String partSequenceOrder = trim(theRecord.get("PartSequenceOrder"));
		String partName = trim(theRecord.get("PartName"));

		// RSNA Codes VS
		ValueSet vs = getValueSet(DOCUMENT_ONTOLOGY_CODES_VS_ID, DOCUMENT_ONTOLOGY_CODES_VS_URI, DOCUMENT_ONTOLOGY_CODES_VS_NAME, null);
		addCodeAsIncludeToValueSet(vs, IHapiTerminologyLoaderSvc.LOINC_URI, loincNumber, null);

		// Part Properties
		String loincCodePropName;
		switch (partTypeName) {
			case "Document.Kind":
				loincCodePropName = "document-kind";
				break;
			case "Document.Role":
				loincCodePropName = "document-role";
				break;
			case "Document.Setting":
				loincCodePropName = "document-setting";
				break;
			case "Document.SubjectMatterDomain":
				loincCodePropName = "document-subject-matter-domain";
				break;
			case "Document.TypeOfService":
				loincCodePropName = "document-type-of-service";
				break;
			default:
				throw new InternalErrorException("Unknown PartTypeName: " + partTypeName);
		}

		TermConcept code = myCode2Concept.get(loincNumber);
		if (code != null) {
			code.addPropertyCoding(loincCodePropName, IHapiTerminologyLoaderSvc.LOINC_URI, partNumber, partName);
		}

	}



}
