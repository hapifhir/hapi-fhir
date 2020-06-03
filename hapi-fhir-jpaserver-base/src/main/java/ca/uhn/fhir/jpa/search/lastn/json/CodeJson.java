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
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class CodeJson {

    @JsonProperty(value = "codeable_concept_id", required = false)
    private String myCodeableConceptId;

    @JsonProperty(value = "text", required = false)
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
