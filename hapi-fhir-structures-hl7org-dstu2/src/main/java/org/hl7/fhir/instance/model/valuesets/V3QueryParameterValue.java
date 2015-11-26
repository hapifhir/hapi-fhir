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


public enum V3QueryParameterValue {

        /**
         * Description:Filter codes used to manage volume of dispenses returned by  a parameter-based queries.
         */
        _DISPENSEQUERYFILTERCODE, 
        /**
         * Description:Returns all dispenses to date for a prescription.
         */
        ALLDISP, 
        /**
         * Description:Returns the most recent dispense for a prescription.
         */
        LASTDISP, 
        /**
         * Description:Returns no dispense for a prescription.
         */
        NODISP, 
        /**
         * Filter codes used to manage types of orders being returned by a parameter-based query.
         */
        _ORDERFILTERCODE, 
        /**
         * Return all orders.
         */
        AO, 
        /**
         * Return only those orders that do not have results.
         */
        ONR, 
        /**
         * Return only those orders that have results.
         */
        OWR, 
        /**
         * A "helper" vocabulary used to construct complex query filters based on how and whether a prescription has been dispensed.
         */
        _PRESCRIPTIONDISPENSEFILTERCODE, 
        /**
         * Filter to only include SubstanceAdministration orders which have no remaining quantity authorized to be dispensed.
         */
        C, 
        /**
         * Filter to only include SubstanceAdministration orders which have no fulfilling supply events performed.
         */
        N, 
        /**
         * Filter to only include SubstanceAdministration orders which have had at least one fulfilling supply event, but which still have outstanding quantity remaining to be authorized.
         */
        R, 
        /**
         * Description:Indicates how result sets should be filtered based on whether they have associated issues.
         */
        _QUERYPARAMETERVALUE, 
        /**
         * Description:Result set should not be filtered based on the presence of issues.
         */
        ISSFA, 
        /**
         * Description:Result set should be filtered to only include records with associated issues.
         */
        ISSFI, 
        /**
         * Description:Result set should be filtered to only include records with associated unmanaged issues.
         */
        ISSFU, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3QueryParameterValue fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("_DispenseQueryFilterCode".equals(codeString))
          return _DISPENSEQUERYFILTERCODE;
        if ("ALLDISP".equals(codeString))
          return ALLDISP;
        if ("LASTDISP".equals(codeString))
          return LASTDISP;
        if ("NODISP".equals(codeString))
          return NODISP;
        if ("_OrderFilterCode".equals(codeString))
          return _ORDERFILTERCODE;
        if ("AO".equals(codeString))
          return AO;
        if ("ONR".equals(codeString))
          return ONR;
        if ("OWR".equals(codeString))
          return OWR;
        if ("_PrescriptionDispenseFilterCode".equals(codeString))
          return _PRESCRIPTIONDISPENSEFILTERCODE;
        if ("C".equals(codeString))
          return C;
        if ("N".equals(codeString))
          return N;
        if ("R".equals(codeString))
          return R;
        if ("_QueryParameterValue".equals(codeString))
          return _QUERYPARAMETERVALUE;
        if ("ISSFA".equals(codeString))
          return ISSFA;
        if ("ISSFI".equals(codeString))
          return ISSFI;
        if ("ISSFU".equals(codeString))
          return ISSFU;
        throw new Exception("Unknown V3QueryParameterValue code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _DISPENSEQUERYFILTERCODE: return "_DispenseQueryFilterCode";
            case ALLDISP: return "ALLDISP";
            case LASTDISP: return "LASTDISP";
            case NODISP: return "NODISP";
            case _ORDERFILTERCODE: return "_OrderFilterCode";
            case AO: return "AO";
            case ONR: return "ONR";
            case OWR: return "OWR";
            case _PRESCRIPTIONDISPENSEFILTERCODE: return "_PrescriptionDispenseFilterCode";
            case C: return "C";
            case N: return "N";
            case R: return "R";
            case _QUERYPARAMETERVALUE: return "_QueryParameterValue";
            case ISSFA: return "ISSFA";
            case ISSFI: return "ISSFI";
            case ISSFU: return "ISSFU";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/QueryParameterValue";
        }
        public String getDefinition() {
          switch (this) {
            case _DISPENSEQUERYFILTERCODE: return "Description:Filter codes used to manage volume of dispenses returned by  a parameter-based queries.";
            case ALLDISP: return "Description:Returns all dispenses to date for a prescription.";
            case LASTDISP: return "Description:Returns the most recent dispense for a prescription.";
            case NODISP: return "Description:Returns no dispense for a prescription.";
            case _ORDERFILTERCODE: return "Filter codes used to manage types of orders being returned by a parameter-based query.";
            case AO: return "Return all orders.";
            case ONR: return "Return only those orders that do not have results.";
            case OWR: return "Return only those orders that have results.";
            case _PRESCRIPTIONDISPENSEFILTERCODE: return "A \"helper\" vocabulary used to construct complex query filters based on how and whether a prescription has been dispensed.";
            case C: return "Filter to only include SubstanceAdministration orders which have no remaining quantity authorized to be dispensed.";
            case N: return "Filter to only include SubstanceAdministration orders which have no fulfilling supply events performed.";
            case R: return "Filter to only include SubstanceAdministration orders which have had at least one fulfilling supply event, but which still have outstanding quantity remaining to be authorized.";
            case _QUERYPARAMETERVALUE: return "Description:Indicates how result sets should be filtered based on whether they have associated issues.";
            case ISSFA: return "Description:Result set should not be filtered based on the presence of issues.";
            case ISSFI: return "Description:Result set should be filtered to only include records with associated issues.";
            case ISSFU: return "Description:Result set should be filtered to only include records with associated unmanaged issues.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _DISPENSEQUERYFILTERCODE: return "dispense query filter code";
            case ALLDISP: return "all dispenses";
            case LASTDISP: return "last dispense";
            case NODISP: return "no dispense";
            case _ORDERFILTERCODE: return "_OrderFilterCode";
            case AO: return "all orders";
            case ONR: return "orders without results";
            case OWR: return "orders with results";
            case _PRESCRIPTIONDISPENSEFILTERCODE: return "Prescription Dispense Filter Code";
            case C: return "Completely dispensed";
            case N: return "Never Dispensed";
            case R: return "Dispensed with remaining fills";
            case _QUERYPARAMETERVALUE: return "QueryParameterValue";
            case ISSFA: return "all";
            case ISSFI: return "with issues";
            case ISSFU: return "with unmanaged issues";
            default: return "?";
          }
    }


}

