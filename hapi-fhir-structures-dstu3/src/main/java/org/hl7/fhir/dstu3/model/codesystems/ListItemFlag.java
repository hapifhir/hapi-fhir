package org.hl7.fhir.dstu3.model.codesystems;

/*
  Copyright (c) 2011+, HL7, Inc.
  All rights reserved.
  
  Redistribution and use in source and binary forms, with or without modification, 
  are permitted provided that the following conditions are met:
  
   * Redistributions of source code must retain the above copyright notice, this 
     list of conditions and the following disclaimer.
   * Redistributions in binary form must reproduce the above copyright notice, 
     this list of conditions and the following disclaimer in the documentation 
     and/or other materials provided with the distribution.
   * Neither the name of HL7 nor the names of its contributors may be used to 
     endorse or promote products derived from this software without specific 
     prior written permission.
  
  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
  IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
  INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT 
  NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
  WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
  POSSIBILITY OF SUCH DAMAGE.
  
*/

// Generated on Mon, Apr 17, 2017 17:38-0400 for FHIR v3.0.1


import org.hl7.fhir.exceptions.FHIRException;

public enum ListItemFlag {

        /**
         * No change has been made to the status of this medicine item.
         */
        _01, 
        /**
         * The medicine item has changed. The change may be described in an extension (not defined yet)
         */
        _02, 
        /**
         * The prescription for this medicine item was cancelled by an authorized health care provider. The patient may be advised to complete the course of the prescribed medicine. This advice is a clinical decision made based on assessment of the patient's clinical condition.
         */
        _03, 
        /**
         * A new medicine item has been prescribed
         */
        _04, 
        /**
         * Administration of this medication item that the patient is currently taking is stopped or recommended to be stopped (i.e. instructed to be ceased by a health care provider). This cessation is anticipated to be permanent. The Change Description should describe the reason for cessation. Example uses: the medication in question is considered ineffective or has caused serious adverse effects. This value applies both to the cessation of a medication that is prescribed by another healthcare provider or patient self-administration of OTC medicines.
         */
        _05, 
        /**
         * Administration of this medication item that the patient is currently taking is on hold, or instructed or recommended by a health care provider to be temporarily stopped, or subject to clinical review (i.e. the stop may be temporary or permanent depending on the outcome of clinical review), or temporarily suspended as a pre-requisite to certain surgical or diagnostic procedures.
         */
        _06, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ListItemFlag fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("01".equals(codeString))
          return _01;
        if ("02".equals(codeString))
          return _02;
        if ("03".equals(codeString))
          return _03;
        if ("04".equals(codeString))
          return _04;
        if ("05".equals(codeString))
          return _05;
        if ("06".equals(codeString))
          return _06;
        throw new FHIRException("Unknown ListItemFlag code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _01: return "01";
            case _02: return "02";
            case _03: return "03";
            case _04: return "04";
            case _05: return "05";
            case _06: return "06";
            default: return "?";
          }
        }
        public String getSystem() {
          return "urn:oid:1.2.36.1.2001.1001.101.104.16592";
        }
        public String getDefinition() {
          switch (this) {
            case _01: return "No change has been made to the status of this medicine item.";
            case _02: return "The medicine item has changed. The change may be described in an extension (not defined yet)";
            case _03: return "The prescription for this medicine item was cancelled by an authorized health care provider. The patient may be advised to complete the course of the prescribed medicine. This advice is a clinical decision made based on assessment of the patient's clinical condition.";
            case _04: return "A new medicine item has been prescribed";
            case _05: return "Administration of this medication item that the patient is currently taking is stopped or recommended to be stopped (i.e. instructed to be ceased by a health care provider). This cessation is anticipated to be permanent. The Change Description should describe the reason for cessation. Example uses: the medication in question is considered ineffective or has caused serious adverse effects. This value applies both to the cessation of a medication that is prescribed by another healthcare provider or patient self-administration of OTC medicines.";
            case _06: return "Administration of this medication item that the patient is currently taking is on hold, or instructed or recommended by a health care provider to be temporarily stopped, or subject to clinical review (i.e. the stop may be temporary or permanent depending on the outcome of clinical review), or temporarily suspended as a pre-requisite to certain surgical or diagnostic procedures.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _01: return "Unchanged";
            case _02: return "Changed";
            case _03: return "Cancelled";
            case _04: return "Prescribed";
            case _05: return "Ceased";
            case _06: return "Suspended";
            default: return "?";
          }
    }


}

