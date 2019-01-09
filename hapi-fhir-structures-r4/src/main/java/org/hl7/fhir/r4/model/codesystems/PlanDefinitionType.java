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

public enum PlanDefinitionType {

        /**
         * A pre-defined and approved group of orders related to a particular clinical condition (e.g. hypertension treatment and monitoring) or stage of care (e.g. hospital admission to Coronary Care Unit). An order set is used as a checklist for the clinician when managing a patient with a specific condition. It is a structured collection of orders relevant to that condition and presented to the clinician in a computerized provider order entry (CPOE) system.
         */
        ORDERSET, 
        /**
         * Defines a desired/typical sequence of clinical activities including preconditions, triggers and temporal relationships.
         */
        CLINICALPROTOCOL, 
        /**
         * A decision support rule of the form [on Event] if Condition then Action. It is intended to be a shareable, computable definition of actions that should be taken whenever some condition is met in response to a particular event or events.
         */
        ECARULE, 
        /**
         * Defines the steps for a group of one or more systems in an event flow process along with the step constraints, sequence, pre-conditions and decision points to complete a particular objective.
         */
        WORKFLOWDEFINITION, 
        /**
         * added to help the parsers
         */
        NULL;
        public static PlanDefinitionType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("order-set".equals(codeString))
          return ORDERSET;
        if ("clinical-protocol".equals(codeString))
          return CLINICALPROTOCOL;
        if ("eca-rule".equals(codeString))
          return ECARULE;
        if ("workflow-definition".equals(codeString))
          return WORKFLOWDEFINITION;
        throw new FHIRException("Unknown PlanDefinitionType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ORDERSET: return "order-set";
            case CLINICALPROTOCOL: return "clinical-protocol";
            case ECARULE: return "eca-rule";
            case WORKFLOWDEFINITION: return "workflow-definition";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/plan-definition-type";
        }
        public String getDefinition() {
          switch (this) {
            case ORDERSET: return "A pre-defined and approved group of orders related to a particular clinical condition (e.g. hypertension treatment and monitoring) or stage of care (e.g. hospital admission to Coronary Care Unit). An order set is used as a checklist for the clinician when managing a patient with a specific condition. It is a structured collection of orders relevant to that condition and presented to the clinician in a computerized provider order entry (CPOE) system.";
            case CLINICALPROTOCOL: return "Defines a desired/typical sequence of clinical activities including preconditions, triggers and temporal relationships.";
            case ECARULE: return "A decision support rule of the form [on Event] if Condition then Action. It is intended to be a shareable, computable definition of actions that should be taken whenever some condition is met in response to a particular event or events.";
            case WORKFLOWDEFINITION: return "Defines the steps for a group of one or more systems in an event flow process along with the step constraints, sequence, pre-conditions and decision points to complete a particular objective.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ORDERSET: return "Order Set";
            case CLINICALPROTOCOL: return "Clinical Protocol";
            case ECARULE: return "ECA Rule";
            case WORKFLOWDEFINITION: return "Workflow Definition";
            default: return "?";
          }
    }


}

