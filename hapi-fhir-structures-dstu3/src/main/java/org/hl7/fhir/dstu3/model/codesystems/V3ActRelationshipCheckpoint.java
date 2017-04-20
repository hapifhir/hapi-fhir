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

public enum V3ActRelationshipCheckpoint {

        /**
         * Condition is tested every time before execution of the service (WHILE condition DO service).
         */
        B, 
        /**
         * Condition is tested at the end of a repeated service execution.  The service is repeated only if the condition is true (DO service WHILE condition).
         */
        E, 
        /**
         * Condition is tested once before the service is executed (IF condition THEN service).
         */
        S, 
        /**
         * Condition must be true throughout the execution and the service is interrupted (asynchronously) as soon as the condition turns false (asynchronous WHILE loop).  The service must be interruptible.
         */
        T, 
        /**
         * Condition is a loop checkpoint, i.e. it is a step of an activity plan and, if negative causes the containing loop to exit.
         */
        X, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ActRelationshipCheckpoint fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("B".equals(codeString))
          return B;
        if ("E".equals(codeString))
          return E;
        if ("S".equals(codeString))
          return S;
        if ("T".equals(codeString))
          return T;
        if ("X".equals(codeString))
          return X;
        throw new FHIRException("Unknown V3ActRelationshipCheckpoint code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case B: return "B";
            case E: return "E";
            case S: return "S";
            case T: return "T";
            case X: return "X";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ActRelationshipCheckpoint";
        }
        public String getDefinition() {
          switch (this) {
            case B: return "Condition is tested every time before execution of the service (WHILE condition DO service).";
            case E: return "Condition is tested at the end of a repeated service execution.  The service is repeated only if the condition is true (DO service WHILE condition).";
            case S: return "Condition is tested once before the service is executed (IF condition THEN service).";
            case T: return "Condition must be true throughout the execution and the service is interrupted (asynchronously) as soon as the condition turns false (asynchronous WHILE loop).  The service must be interruptible.";
            case X: return "Condition is a loop checkpoint, i.e. it is a step of an activity plan and, if negative causes the containing loop to exit.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case B: return "beginning";
            case E: return "end";
            case S: return "entry";
            case T: return "through";
            case X: return "exit";
            default: return "?";
          }
    }


}

