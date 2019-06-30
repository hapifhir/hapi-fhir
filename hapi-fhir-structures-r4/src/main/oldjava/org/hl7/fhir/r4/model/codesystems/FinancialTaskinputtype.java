package org.hl7.fhir.r4.model.codesystems;

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

// Generated on Thu, Dec 27, 2018 10:06-0500 for FHIR v4.0.0


import org.hl7.fhir.exceptions.FHIRException;

public enum FinancialTaskinputtype {

        /**
         * The name of a resource to include in a selection.
         */
        INCLUDE, 
        /**
         * The name of a resource to not include in a selection.
         */
        EXCLUDE, 
        /**
         * A reference to the response resource to the original processing of a resource.
         */
        ORIGRESPONSE, 
        /**
         * A reference value which must be quoted to authorize an action.
         */
        REFERENCE, 
        /**
         * The sequence number associated with an item for reprocessing.
         */
        ITEM, 
        /**
         * The reference period for the action being requested.
         */
        PERIOD, 
        /**
         * The processing status from a check on the processing status of a resource such as the adjudication of a claim.
         */
        STATUS, 
        /**
         * added to help the parsers
         */
        NULL;
        public static FinancialTaskinputtype fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("include".equals(codeString))
          return INCLUDE;
        if ("exclude".equals(codeString))
          return EXCLUDE;
        if ("origresponse".equals(codeString))
          return ORIGRESPONSE;
        if ("reference".equals(codeString))
          return REFERENCE;
        if ("item".equals(codeString))
          return ITEM;
        if ("period".equals(codeString))
          return PERIOD;
        if ("status".equals(codeString))
          return STATUS;
        throw new FHIRException("Unknown FinancialTaskinputtype code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case INCLUDE: return "include";
            case EXCLUDE: return "exclude";
            case ORIGRESPONSE: return "origresponse";
            case REFERENCE: return "reference";
            case ITEM: return "item";
            case PERIOD: return "period";
            case STATUS: return "status";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/financialtaskinputtype";
        }
        public String getDefinition() {
          switch (this) {
            case INCLUDE: return "The name of a resource to include in a selection.";
            case EXCLUDE: return "The name of a resource to not include in a selection.";
            case ORIGRESPONSE: return "A reference to the response resource to the original processing of a resource.";
            case REFERENCE: return "A reference value which must be quoted to authorize an action.";
            case ITEM: return "The sequence number associated with an item for reprocessing.";
            case PERIOD: return "The reference period for the action being requested.";
            case STATUS: return "The processing status from a check on the processing status of a resource such as the adjudication of a claim.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INCLUDE: return "Include";
            case EXCLUDE: return "Exclude";
            case ORIGRESPONSE: return "Original Response";
            case REFERENCE: return "Reference Number";
            case ITEM: return "Item Number";
            case PERIOD: return "Period";
            case STATUS: return "Status code";
            default: return "?";
          }
    }


}

