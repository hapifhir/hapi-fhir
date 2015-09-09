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

// Generated on Thu, Jul 23, 2015 16:50-0400 for FHIR v0.5.0


public enum TransactionProcessingMode {

        /**
         * If a matching resource is found, then ignore the resource and do not process it as part of the transaction (and return the matching resource information in the transaction response)
         */
        IGNORE, 
        /**
         * If a matching resource is found, then update it with this resource, otherwise create a new one
         */
        UPDATE, 
        /**
         * If a resource matching the information provided is found, delete it. If no matching resource is found, the transaction fails
         */
        DELETE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static TransactionProcessingMode fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("ignore".equals(codeString))
          return IGNORE;
        if ("update".equals(codeString))
          return UPDATE;
        if ("delete".equals(codeString))
          return DELETE;
        throw new Exception("Unknown TransactionProcessingMode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case IGNORE: return "ignore";
            case UPDATE: return "update";
            case DELETE: return "delete";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/transaction-processing-mode";
        }
        public String getDefinition() {
          switch (this) {
            case IGNORE: return "If a matching resource is found, then ignore the resource and do not process it as part of the transaction (and return the matching resource information in the transaction response)";
            case UPDATE: return "If a matching resource is found, then update it with this resource, otherwise create a new one";
            case DELETE: return "If a resource matching the information provided is found, delete it. If no matching resource is found, the transaction fails";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case IGNORE: return "Ignore";
            case UPDATE: return "Update";
            case DELETE: return "Delete";
            default: return "?";
          }
    }


}

