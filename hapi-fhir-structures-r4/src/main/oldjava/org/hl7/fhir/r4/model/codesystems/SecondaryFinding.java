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

public enum SecondaryFinding {

        /**
         * First release (2013): ACMG Recommendations for Reporting of Incidental Findings in Clinical Exome and Genome Sequencing.  https://www.ncbi.nlm.nih.gov/pmc/articles/PMC3727274/
         */
        ACMGVERSION1, 
        /**
         * Second release (2016): Recommendations for reporting of secondary findings in clinical exome and genome sequencing, 2016 update (ACMG SF v2.0): a policy statement of the American College of Medical Genetics and Genomics. https://www.ncbi.nlm.nih.gov/pubmed/27854360
         */
        ACMGVERSION2, 
        /**
         * added to help the parsers
         */
        NULL;
        public static SecondaryFinding fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("acmg-version1".equals(codeString))
          return ACMGVERSION1;
        if ("acmg-version2".equals(codeString))
          return ACMGVERSION2;
        throw new FHIRException("Unknown SecondaryFinding code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ACMGVERSION1: return "acmg-version1";
            case ACMGVERSION2: return "acmg-version2";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/secondary-finding";
        }
        public String getDefinition() {
          switch (this) {
            case ACMGVERSION1: return "First release (2013): ACMG Recommendations for Reporting of Incidental Findings in Clinical Exome and Genome Sequencing.  https://www.ncbi.nlm.nih.gov/pmc/articles/PMC3727274/";
            case ACMGVERSION2: return "Second release (2016): Recommendations for reporting of secondary findings in clinical exome and genome sequencing, 2016 update (ACMG SF v2.0): a policy statement of the American College of Medical Genetics and Genomics. https://www.ncbi.nlm.nih.gov/pubmed/27854360";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACMGVERSION1: return "ACMG Version 1";
            case ACMGVERSION2: return "ACMG Version 2";
            default: return "?";
          }
    }


}

