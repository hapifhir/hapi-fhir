package ca.uhn.fhir.jpa.search.lastn.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class ObservationJson implements Comparable<ObservationJson>{

	@Override
	public String toString() {
		return "ObservationJson [myIdentifier=" + myIdentifier + "]";
	}

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
	private List<String> myCode_coding_code = new ArrayList<>();

	@JsonProperty(value = "codeconceptcodingcode_system_hash", required = false)
	private List<String> myCode_coding_code_system_hash = new ArrayList<>();

	@JsonProperty(value = "codeconceptcodingdisplay", required = false)
	private List<String>  myCode_coding_display = new ArrayList<>();

	@JsonProperty(value = "codeconceptcodingsystem", required = false)
	private List<String> myCode_coding_system =  new ArrayList<>();

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

	public void setCategories(List<CodeJson> theCategories) {
		for (CodeJson theConcept : theCategories) {
			myCategory_concept_text.add(theConcept.getCodeableConceptText());
			List<String> coding_code_system_hashes = new ArrayList<>();
			List<String> coding_codes = new ArrayList<>();
			List<String> coding_displays = new ArrayList<>();
			List<String> coding_systems = new ArrayList<>();
			for (String theCategoryCoding_code : theConcept.getCoding_code()) {
				coding_codes.add(theCategoryCoding_code);
			}
			for (String theCategoryCoding_system : theConcept.getCoding_system()) {
				coding_systems.add(theCategoryCoding_system);
			}
			for (String theCategoryCoding_code_system_hash : theConcept.getCoding_code_system_hash()) {
				coding_code_system_hashes.add(theCategoryCoding_code_system_hash);
			}
			for (String theCategoryCoding_display : theConcept.getCoding_display()) {
				coding_displays.add(theCategoryCoding_display);
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

	public void setCode(CodeJson theCode) {
		myCode_concept_text = theCode.getCodeableConceptText();
		// Currently can only support one Coding for Observation Code
		myCode_coding_code_system_hash.addAll(theCode.getCoding_code_system_hash());
		myCode_coding_code.addAll(theCode.getCoding_code());
		myCode_coding_display.addAll(theCode.getCoding_display());
		myCode_coding_system.addAll(theCode.getCoding_system());

	}

	public String getCode_concept_text() {
		return myCode_concept_text;
	}

	public List<String> getCode_coding_code_system_hash() {
		return myCode_coding_code_system_hash;
	}

	public List<String> getCode_coding_code() {
		return myCode_coding_code;
	}

	public List<String> getCode_coding_display() {
		return myCode_coding_display;
	}

	public List<String> getCode_coding_system() {
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


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((myIdentifier == null) ? 0 : myIdentifier.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ObservationJson other = (ObservationJson) obj;
		if (myIdentifier == null) {
			if (other.myIdentifier != null)
				return false;
		} else if (!myIdentifier.equals(other.myIdentifier))
			return false;
		return true;
	}
	
	@Override
	public int compareTo(ObservationJson o) {		
		if (o == null || o.getEffectiveDtm() == null || getEffectiveDtm() == null)
			return 0;
		return -getEffectiveDtm().compareTo(o.getEffectiveDtm());
	}

}
