package ca.uhn.fhir.jpa.search.lastn.json;

/*
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

import ca.uhn.fhir.jpa.model.util.CodeSystemHash;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class ObservationJson {

	@JsonProperty(value = "identifier", required = true)
	private String myIdentifier;

	@JsonProperty(value = "subject", required = true)
	private String mySubject;

	@JsonProperty(value = "categoryconcepttext", required = false)
	private List<String> myCategory_concept_text = new ArrayList<>();

	@JsonProperty(value = "categoryconceptcodingcode", required = false)
	private List<List<String>> myCategory_coding_code = new ArrayList<>();

	@JsonProperty(value = "categoryconceptcodingcode_system_hash", required = false)
	private List<List<String>> myCategory_coding_code_system_hash = new ArrayList<>();

	@JsonProperty(value = "categoryconceptcodingdisplay", required = false)
	private List<List<String>> myCategory_coding_display = new ArrayList<>();

	@JsonProperty(value = "categoryconceptcodingsystem", required = false)
	private List<List<String>> myCategory_coding_system = new ArrayList<>();

	@JsonProperty(value = "codeconceptid", required = false)
	private String myCode_concept_id;

	@JsonProperty(value = "codeconcepttext", required = false)
	private String myCode_concept_text;

	@JsonProperty(value = "codeconceptcodingcode", required = false)
	private String myCode_coding_code;

	@JsonProperty(value = "codeconceptcodingcode_system_hash", required = false)
	private String myCode_coding_code_system_hash;

	@JsonProperty(value = "codeconceptcodingdisplay", required = false)
	private String myCode_coding_display;

	@JsonProperty(value = "codeconceptcodingsystem", required = false)
	private String myCode_coding_system;

	@JsonProperty(value = "effectivedtm", required = true)
	private Date myEffectiveDtm;

	public ObservationJson() {
	}

	public void setIdentifier(String theIdentifier) {
		myIdentifier = theIdentifier;
	}

	public void setSubject(String theSubject) {
		mySubject = theSubject;
	}

	public void setCategories(List<CodeableConcept> theCategories) {
		for (CodeableConcept theConcept : theCategories) {
			myCategory_concept_text.add(theConcept.getText());
			List<String> coding_code_system_hashes = new ArrayList<>();
			List<String> coding_codes = new ArrayList<>();
			List<String> coding_displays = new ArrayList<>();
			List<String> coding_systems = new ArrayList<>();
			for (Coding theCategoryCoding : theConcept.getCoding()) {
				coding_code_system_hashes.add(String.valueOf(CodeSystemHash.hashCodeSystem(theCategoryCoding.getSystem(), theCategoryCoding.getCode())));
				coding_codes.add(theCategoryCoding.getCode());
				coding_displays.add(theCategoryCoding.getDisplay());
				coding_systems.add(theCategoryCoding.getSystem());
			}
			myCategory_coding_code_system_hash.add(coding_code_system_hashes);
			myCategory_coding_code.add(coding_codes);
			myCategory_coding_display.add(coding_displays);
			myCategory_coding_system.add(coding_systems);
		}
	}

	public List<String> getCategory_concept_text() {
		return myCategory_concept_text;
	}

	public List<List<String>> getCategory_coding_code_system_hash() {
		return myCategory_coding_code_system_hash;
	}

	public List<List<String>> getCategory_coding_code() {
		return myCategory_coding_code;
	}

	public List<List<String>> getCategory_coding_display() {
		return myCategory_coding_display;
	}

	public List<List<String>> getCategory_coding_system() {
		return myCategory_coding_system;
	}

	public void setCode(CodeableConcept theCode) {
		myCode_concept_text = theCode.getText();
		for (Coding theCodeCoding : theCode.getCoding()) {
			myCode_coding_code_system_hash = String.valueOf(CodeSystemHash.hashCodeSystem(theCodeCoding.getSystem(), theCodeCoding.getCode()));
			myCode_coding_code = theCodeCoding.getCode();
			myCode_coding_display = theCodeCoding.getDisplay();
			myCode_coding_system = theCodeCoding.getSystem();
		}

	}

	public String getCode_concept_text() {
		return myCode_concept_text;
	}

	public String getCode_coding_code_system_hash() {
		return myCode_coding_code_system_hash;
	}

	public String getCode_coding_code() {
		return myCode_coding_code;
	}

	public String getCode_coding_display() {
		return myCode_coding_display;
	}

	public String getCode_coding_system() {
		return myCode_coding_system;
	}

	public void setCode_concept_id(String theCodeId) {
		myCode_concept_id = theCodeId;
	}

	public String getCode_concept_id() {
		return myCode_concept_id;
	}

	public void setEffectiveDtm(Date theEffectiveDtm) {
		myEffectiveDtm = theEffectiveDtm;
	}

	public Date getEffectiveDtm() {
		return myEffectiveDtm;
	}

	public String getSubject() {
		return mySubject;
	}

	public String getIdentifier() {
		return myIdentifier;
	}

}
