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

public enum MeasureReportType {

        /**
         * An individual report that provides information on the performance for a given measure with respect to a single subject.
         */
        INDIVIDUAL, 
        /**
         * A subject list report that includes a listing of subjects that satisfied each population criteria in the measure.
         */
        SUBJECTLIST, 
        /**
         * A summary report that returns the number of members in each population criteria for the measure.
         */
        SUMMARY, 
        /**
         * A data collection report that contains data-of-interest for the measure.
         */
        DATACOLLECTION, 
        /**
         * added to help the parsers
         */
        NULL;
        public static MeasureReportType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("individual".equals(codeString))
          return INDIVIDUAL;
        if ("subject-list".equals(codeString))
          return SUBJECTLIST;
        if ("summary".equals(codeString))
          return SUMMARY;
        if ("data-collection".equals(codeString))
          return DATACOLLECTION;
        throw new FHIRException("Unknown MeasureReportType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case INDIVIDUAL: return "individual";
            case SUBJECTLIST: return "subject-list";
            case SUMMARY: return "summary";
            case DATACOLLECTION: return "data-collection";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/measure-report-type";
        }
        public String getDefinition() {
          switch (this) {
            case INDIVIDUAL: return "An individual report that provides information on the performance for a given measure with respect to a single subject.";
            case SUBJECTLIST: return "A subject list report that includes a listing of subjects that satisfied each population criteria in the measure.";
            case SUMMARY: return "A summary report that returns the number of members in each population criteria for the measure.";
            case DATACOLLECTION: return "A data collection report that contains data-of-interest for the measure.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INDIVIDUAL: return "Individual";
            case SUBJECTLIST: return "Subject List";
            case SUMMARY: return "Summary";
            case DATACOLLECTION: return "Data Collection";
            default: return "?";
          }
    }


}

