package ca.uhn.fhir.jpa.search.lastn.json;

/*
 * #%L
 * Smile CDR - CDR
 * %%
 * Copyright (C) 2016 - 2019 Simpatico Intelligent Systems Inc
 * %%
 * All rights reserved.
 * #L%
 */

import ca.uhn.fhir.jpa.search.lastn.util.CodeSystemHash;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class CodeJson {

    @JsonProperty(value = "codeable_concept_id", required = false)
    private String myCodeableConceptId;

    @JsonProperty(value = "codeable_concept_text", required = false)
    private String myCodeableConceptText;

    @JsonProperty(value = "codingcode", required = false)
    private List<String> myCoding_code = new ArrayList<>();

    @JsonProperty(value = "codingcode_system_hash", required = true)
    private List<String> myCoding_code_system_hash = new ArrayList<>();

    @JsonProperty(value = "codingdisplay", required = false)
    private List<String> myCoding_display = new ArrayList<>();

    @JsonProperty(value = "codingsystem", required = false)
    private List<String> myCoding_system = new ArrayList<>();

    public CodeJson(){
    }

    public CodeJson(CodeableConcept theCodeableConcept, String theCodeableConceptId) {
        myCodeableConceptText = theCodeableConcept.getText();
        myCodeableConceptId = theCodeableConceptId;
        for (Coding theCoding : theCodeableConcept.getCoding()) {
            myCoding_code.add(theCoding.getCode());
            myCoding_system.add(theCoding.getSystem());
            myCoding_display.add(theCoding.getDisplay());
            myCoding_code_system_hash.add(String.valueOf(CodeSystemHash.hashCodeSystem(theCoding.getSystem(), theCoding.getCode())));
        }
    }

    public String getCodeableConceptId() {
        return myCodeableConceptId;
    }

    public String getCodeableConceptText() {
        return myCodeableConceptText;
    }

    public List<String> getCoding_code() {
        return myCoding_code;
    }

    public List<String> getCoding_code_system_hash() {
        return myCoding_code_system_hash;
    }

    public List<String> getCoding_display() {
        return myCoding_display;
    }

    public List<String> getCoding_system() {
        return myCoding_system;
    }
}
