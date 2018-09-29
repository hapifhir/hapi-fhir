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

// Generated on Thu, Sep 13, 2018 09:04-0400 for FHIR v3.5.0


import org.hl7.fhir.exceptions.FHIRException;

public enum V3TriggerEventID {

        /**
         * Description:
         */
        POLBTE004000UV, 
        /**
         * Description:
         */
        POLBTE004001UV, 
        /**
         * Description:
         */
        POLBTE004002UV, 
        /**
         * Description:
         */
        POLBTE004007UV, 
        /**
         * Description:
         */
        POLBTE004100UV, 
        /**
         * Description:
         */
        POLBTE004102UV, 
        /**
         * Description:
         */
        POLBTE004200UV, 
        /**
         * Description:
         */
        POLBTE004201UV, 
        /**
         * Description:
         */
        POLBTE004202UV, 
        /**
         * Description:
         */
        POLBTE004301UV, 
        /**
         * Description:
         */
        POLBTE004500UV, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3TriggerEventID fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("POLB_TE004000UV".equals(codeString))
          return POLBTE004000UV;
        if ("POLB_TE004001UV".equals(codeString))
          return POLBTE004001UV;
        if ("POLB_TE004002UV".equals(codeString))
          return POLBTE004002UV;
        if ("POLB_TE004007UV".equals(codeString))
          return POLBTE004007UV;
        if ("POLB_TE004100UV".equals(codeString))
          return POLBTE004100UV;
        if ("POLB_TE004102UV".equals(codeString))
          return POLBTE004102UV;
        if ("POLB_TE004200UV".equals(codeString))
          return POLBTE004200UV;
        if ("POLB_TE004201UV".equals(codeString))
          return POLBTE004201UV;
        if ("POLB_TE004202UV".equals(codeString))
          return POLBTE004202UV;
        if ("POLB_TE004301UV".equals(codeString))
          return POLBTE004301UV;
        if ("POLB_TE004500UV".equals(codeString))
          return POLBTE004500UV;
        throw new FHIRException("Unknown V3TriggerEventID code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case POLBTE004000UV: return "POLB_TE004000UV";
            case POLBTE004001UV: return "POLB_TE004001UV";
            case POLBTE004002UV: return "POLB_TE004002UV";
            case POLBTE004007UV: return "POLB_TE004007UV";
            case POLBTE004100UV: return "POLB_TE004100UV";
            case POLBTE004102UV: return "POLB_TE004102UV";
            case POLBTE004200UV: return "POLB_TE004200UV";
            case POLBTE004201UV: return "POLB_TE004201UV";
            case POLBTE004202UV: return "POLB_TE004202UV";
            case POLBTE004301UV: return "POLB_TE004301UV";
            case POLBTE004500UV: return "POLB_TE004500UV";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/v3-triggerEventID";
        }
        public String getDefinition() {
          switch (this) {
            case POLBTE004000UV: return "Description:";
            case POLBTE004001UV: return "Description:";
            case POLBTE004002UV: return "Description:";
            case POLBTE004007UV: return "Description:";
            case POLBTE004100UV: return "Description:";
            case POLBTE004102UV: return "Description:";
            case POLBTE004200UV: return "Description:";
            case POLBTE004201UV: return "Description:";
            case POLBTE004202UV: return "Description:";
            case POLBTE004301UV: return "Description:";
            case POLBTE004500UV: return "Description:";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case POLBTE004000UV: return "Result Status";
            case POLBTE004001UV: return "Result Confirm";
            case POLBTE004002UV: return "Result Reject";
            case POLBTE004007UV: return "Result Tracking";
            case POLBTE004100UV: return "Result in Progress";
            case POLBTE004102UV: return "Result Activate";
            case POLBTE004200UV: return "Result Complete with Fulfillment";
            case POLBTE004201UV: return "Result Corrected";
            case POLBTE004202UV: return "Result Complete";
            case POLBTE004301UV: return "Result Abort";
            case POLBTE004500UV: return "Result Nullify";
            default: return "?";
          }
    }


}

