package org.hl7.fhir.instance.model.valuesets;

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

// Generated on Wed, Nov 11, 2015 10:54-0500 for FHIR v1.0.2


public enum ContractSignerType {

        /**
         * the signature of the primary or sole author of a health information document. There can be only one primary author of a health information document.
         */
        OID_1_2_840_10065_1_12_1_1, 
        /**
         * the signature of a health information document coauthor. There can be multiple coauthors of a health information document.
         */
        OID_1_2_840_10065_1_12_1_2, 
        /**
         * the signature of an individual who is a participant in the health information document but is not an author or coauthor. (Example a surgeon who is required by institutional, regulatory, or legal rules to sign an operative report, but who was not involved in the authorship of that report.)
         */
        OID_1_2_840_10065_1_12_1_3, 
        /**
         * the signature of an individual who has transcribed a dictated document or recorded written text into a digital machine readable format.
         */
        OID_1_2_840_10065_1_12_1_4, 
        /**
         * a signature verifying the information contained in a document. (Example a physician is required to countersign a verbal order that has previously been recorded in the medical record by a registered nurse who has carried out the verbal order.)
         */
        OID_1_2_840_10065_1_12_1_5, 
        /**
         * a signature validating a health information document for inclusion in the patient record. (Example a medical student or resident is credentialed to perform history or physical examinations and to write progress notes. The attending physician signs the history and physical examination to validate the entry for inclusion in the patient's medical record.)
         */
        OID_1_2_840_10065_1_12_1_6, 
        /**
         * the signature of an individual consenting to what is described in a health information document.
         */
        OID_1_2_840_10065_1_12_1_7, 
        /**
         * the signature of a witness to any other signature.
         */
        OID_1_2_840_10065_1_12_1_8, 
        /**
         * the signature of a witness to an event. (Example the witness has observed a procedure and is attesting to this fact.)
         */
        OID_1_2_840_10065_1_12_1_9, 
        /**
         * the signature of an individual who has witnessed another individual who is known to them signing a document. (Example the identity witness is a notary public.)
         */
        OID_1_2_840_10065_1_12_1_10, 
        /**
         * the signature of an individual who has witnessed the health care provider counselling a patient.
         */
        OID_1_2_840_10065_1_12_1_11, 
        /**
         * the signature of an individual who has translated health care information during an event or the obtaining of consent to a treatment.
         */
        OID_1_2_840_10065_1_12_1_12, 
        /**
         * the signature of a person, device, or algorithm that has reviewed or filtered data for inclusion into the patient record. ( Examples: (1) a medical records clerk who scans a document for inclusion in the medical record, enters header information, or catalogues and classifies the data, or a combination thereof; (2) a gateway that receives data from another computer system and interprets that data or changes its format, or both, before entering it into the patient record.)
         */
        OID_1_2_840_10065_1_12_1_13, 
        /**
         * the signature of an automated data source. (Examples: (1) the signature for an image that is generated by a device for inclusion in the patient record; (2) the signature for an ECG derived by an ECG system for inclusion in the patient record; (3) the data from a biomedical monitoring device or system that is for inclusion in the patient record.)
         */
        OID_1_2_840_10065_1_12_1_14, 
        /**
         * the signature on a new amended document of an individual who has corrected, edited, or amended an original health information document. An addendum signature can either be a signature type or a signature sub-type (see 8.1). Any document with an addendum signature shall have a companion document that is the original document with its original, unaltered content, and original signatures. The original document shall be referenced via an attribute in the new document, which contains, for example, the digest of the old document. Whether the original, unaltered, document is always displayed with the addended document is a local matter, but the original, unaltered, document must remain as part of the patient record and be retrievable on demand.
         */
        OID_1_2_840_10065_1_12_1_15, 
        /**
         * the signature on an original document of an individual who has generated a new amended document. This (original) document shall reference the new document via an additional signature purpose. This is the inverse of an addendum signature and provides a pointer from the original to the amended document.
         */
        OID_1_2_840_10065_1_12_1_16, 
        /**
         * the signature of an individual who is certifying that the document is invalidated by an error(s), or is placed in the wrong chart. An administrative (error/edit) signature must include an addendum to the document and therefore shall have an addendum signature sub-type (see 8.1). This signature is reserved for the highest health information system administrative classification, since it is a statement that the entire document is invalidated by the error and that the document should no longer be used for patient care, although for legal reasons the document must remain part of the permanent patient record.
         */
        OID_1_2_840_10065_1_12_1_17, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ContractSignerType fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("1.2.840.10065.1.12.1.1".equals(codeString))
          return OID_1_2_840_10065_1_12_1_1;
        if ("1.2.840.10065.1.12.1.2".equals(codeString))
          return OID_1_2_840_10065_1_12_1_2;
        if ("1.2.840.10065.1.12.1.3".equals(codeString))
          return OID_1_2_840_10065_1_12_1_3;
        if ("1.2.840.10065.1.12.1.4".equals(codeString))
          return OID_1_2_840_10065_1_12_1_4;
        if ("1.2.840.10065.1.12.1.5".equals(codeString))
          return OID_1_2_840_10065_1_12_1_5;
        if ("1.2.840.10065.1.12.1.6".equals(codeString))
          return OID_1_2_840_10065_1_12_1_6;
        if ("1.2.840.10065.1.12.1.7".equals(codeString))
          return OID_1_2_840_10065_1_12_1_7;
        if ("1.2.840.10065.1.12.1.8".equals(codeString))
          return OID_1_2_840_10065_1_12_1_8;
        if ("1.2.840.10065.1.12.1.9".equals(codeString))
          return OID_1_2_840_10065_1_12_1_9;
        if ("1.2.840.10065.1.12.1.10".equals(codeString))
          return OID_1_2_840_10065_1_12_1_10;
        if ("1.2.840.10065.1.12.1.11".equals(codeString))
          return OID_1_2_840_10065_1_12_1_11;
        if ("1.2.840.10065.1.12.1.12".equals(codeString))
          return OID_1_2_840_10065_1_12_1_12;
        if ("1.2.840.10065.1.12.1.13".equals(codeString))
          return OID_1_2_840_10065_1_12_1_13;
        if ("1.2.840.10065.1.12.1.14".equals(codeString))
          return OID_1_2_840_10065_1_12_1_14;
        if ("1.2.840.10065.1.12.1.15".equals(codeString))
          return OID_1_2_840_10065_1_12_1_15;
        if ("1.2.840.10065.1.12.1.16".equals(codeString))
          return OID_1_2_840_10065_1_12_1_16;
        if ("1.2.840.10065.1.12.1.17".equals(codeString))
          return OID_1_2_840_10065_1_12_1_17;
        throw new Exception("Unknown ContractSignerType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case OID_1_2_840_10065_1_12_1_1: return "1.2.840.10065.1.12.1.1";
            case OID_1_2_840_10065_1_12_1_2: return "1.2.840.10065.1.12.1.2";
            case OID_1_2_840_10065_1_12_1_3: return "1.2.840.10065.1.12.1.3";
            case OID_1_2_840_10065_1_12_1_4: return "1.2.840.10065.1.12.1.4";
            case OID_1_2_840_10065_1_12_1_5: return "1.2.840.10065.1.12.1.5";
            case OID_1_2_840_10065_1_12_1_6: return "1.2.840.10065.1.12.1.6";
            case OID_1_2_840_10065_1_12_1_7: return "1.2.840.10065.1.12.1.7";
            case OID_1_2_840_10065_1_12_1_8: return "1.2.840.10065.1.12.1.8";
            case OID_1_2_840_10065_1_12_1_9: return "1.2.840.10065.1.12.1.9";
            case OID_1_2_840_10065_1_12_1_10: return "1.2.840.10065.1.12.1.10";
            case OID_1_2_840_10065_1_12_1_11: return "1.2.840.10065.1.12.1.11";
            case OID_1_2_840_10065_1_12_1_12: return "1.2.840.10065.1.12.1.12";
            case OID_1_2_840_10065_1_12_1_13: return "1.2.840.10065.1.12.1.13";
            case OID_1_2_840_10065_1_12_1_14: return "1.2.840.10065.1.12.1.14";
            case OID_1_2_840_10065_1_12_1_15: return "1.2.840.10065.1.12.1.15";
            case OID_1_2_840_10065_1_12_1_16: return "1.2.840.10065.1.12.1.16";
            case OID_1_2_840_10065_1_12_1_17: return "1.2.840.10065.1.12.1.17";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/contractsignertypecodes";
        }
        public String getDefinition() {
          switch (this) {
            case OID_1_2_840_10065_1_12_1_1: return "the signature of the primary or sole author of a health information document. There can be only one primary author of a health information document.";
            case OID_1_2_840_10065_1_12_1_2: return "the signature of a health information document coauthor. There can be multiple coauthors of a health information document.";
            case OID_1_2_840_10065_1_12_1_3: return "the signature of an individual who is a participant in the health information document but is not an author or coauthor. (Example a surgeon who is required by institutional, regulatory, or legal rules to sign an operative report, but who was not involved in the authorship of that report.)";
            case OID_1_2_840_10065_1_12_1_4: return "the signature of an individual who has transcribed a dictated document or recorded written text into a digital machine readable format.";
            case OID_1_2_840_10065_1_12_1_5: return "a signature verifying the information contained in a document. (Example a physician is required to countersign a verbal order that has previously been recorded in the medical record by a registered nurse who has carried out the verbal order.)";
            case OID_1_2_840_10065_1_12_1_6: return "a signature validating a health information document for inclusion in the patient record. (Example a medical student or resident is credentialed to perform history or physical examinations and to write progress notes. The attending physician signs the history and physical examination to validate the entry for inclusion in the patient's medical record.)";
            case OID_1_2_840_10065_1_12_1_7: return "the signature of an individual consenting to what is described in a health information document.";
            case OID_1_2_840_10065_1_12_1_8: return "the signature of a witness to any other signature.";
            case OID_1_2_840_10065_1_12_1_9: return "the signature of a witness to an event. (Example the witness has observed a procedure and is attesting to this fact.)";
            case OID_1_2_840_10065_1_12_1_10: return "the signature of an individual who has witnessed another individual who is known to them signing a document. (Example the identity witness is a notary public.)";
            case OID_1_2_840_10065_1_12_1_11: return "the signature of an individual who has witnessed the health care provider counselling a patient.";
            case OID_1_2_840_10065_1_12_1_12: return "the signature of an individual who has translated health care information during an event or the obtaining of consent to a treatment.";
            case OID_1_2_840_10065_1_12_1_13: return "the signature of a person, device, or algorithm that has reviewed or filtered data for inclusion into the patient record. ( Examples: (1) a medical records clerk who scans a document for inclusion in the medical record, enters header information, or catalogues and classifies the data, or a combination thereof; (2) a gateway that receives data from another computer system and interprets that data or changes its format, or both, before entering it into the patient record.)";
            case OID_1_2_840_10065_1_12_1_14: return "the signature of an automated data source. (Examples: (1) the signature for an image that is generated by a device for inclusion in the patient record; (2) the signature for an ECG derived by an ECG system for inclusion in the patient record; (3) the data from a biomedical monitoring device or system that is for inclusion in the patient record.)";
            case OID_1_2_840_10065_1_12_1_15: return "the signature on a new amended document of an individual who has corrected, edited, or amended an original health information document. An addendum signature can either be a signature type or a signature sub-type (see 8.1). Any document with an addendum signature shall have a companion document that is the original document with its original, unaltered content, and original signatures. The original document shall be referenced via an attribute in the new document, which contains, for example, the digest of the old document. Whether the original, unaltered, document is always displayed with the addended document is a local matter, but the original, unaltered, document must remain as part of the patient record and be retrievable on demand.";
            case OID_1_2_840_10065_1_12_1_16: return "the signature on an original document of an individual who has generated a new amended document. This (original) document shall reference the new document via an additional signature purpose. This is the inverse of an addendum signature and provides a pointer from the original to the amended document.";
            case OID_1_2_840_10065_1_12_1_17: return "the signature of an individual who is certifying that the document is invalidated by an error(s), or is placed in the wrong chart. An administrative (error/edit) signature must include an addendum to the document and therefore shall have an addendum signature sub-type (see 8.1). This signature is reserved for the highest health information system administrative classification, since it is a statement that the entire document is invalidated by the error and that the document should no longer be used for patient care, although for legal reasons the document must remain part of the permanent patient record.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case OID_1_2_840_10065_1_12_1_1: return "AuthorID";
            case OID_1_2_840_10065_1_12_1_2: return "Co-AuthorID";
            case OID_1_2_840_10065_1_12_1_3: return "Co-Participated";
            case OID_1_2_840_10065_1_12_1_4: return "Transcriptionist";
            case OID_1_2_840_10065_1_12_1_5: return "Verification";
            case OID_1_2_840_10065_1_12_1_6: return "Validation";
            case OID_1_2_840_10065_1_12_1_7: return "Consent";
            case OID_1_2_840_10065_1_12_1_8: return "Witness";
            case OID_1_2_840_10065_1_12_1_9: return "Event-Witness";
            case OID_1_2_840_10065_1_12_1_10: return "Identity-Witness";
            case OID_1_2_840_10065_1_12_1_11: return "Consent-Witness";
            case OID_1_2_840_10065_1_12_1_12: return "Interpreter";
            case OID_1_2_840_10065_1_12_1_13: return "Review";
            case OID_1_2_840_10065_1_12_1_14: return "Source";
            case OID_1_2_840_10065_1_12_1_15: return "Addendum";
            case OID_1_2_840_10065_1_12_1_16: return "Administrative";
            case OID_1_2_840_10065_1_12_1_17: return "Timestamp";
            default: return "?";
          }
    }


}

