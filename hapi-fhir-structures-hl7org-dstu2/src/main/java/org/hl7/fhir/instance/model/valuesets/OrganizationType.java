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


public enum OrganizationType {

        /**
         * An organization that provides healthcare services.
         */
        PROV, 
        /**
         * A department or ward within a hospital (Generally is not applicable to top level organizations)
         */
        DEPT, 
        /**
         * An organizational team is usually a grouping of practitioners that perform a specific function within an organization (which could be a top level organization, or a department).
         */
        TEAM, 
        /**
         * A political body, often used when including organization records for government bodies such as a Federal Government, State or Local Government.
         */
        GOVT, 
        /**
         * A company that provides insurance to its subscribers that may include healthcare related policies.
         */
        INS, 
        /**
         * An educational institution that provides education or research facilities.
         */
        EDU, 
        /**
         * An organization that is identified as a part of a religious institution.
         */
        RELI, 
        /**
         * An organization that is identified as a Pharmaceutical/Clinical Research Sponsor.
         */
        CRS, 
        /**
         * An un-incorporated community group.
         */
        CG, 
        /**
         * An organization that is a registered business or corporation but not identified by other types.
         */
        BUS, 
        /**
         * Other type of organization not already specified.
         */
        OTHER, 
        /**
         * added to help the parsers
         */
        NULL;
        public static OrganizationType fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("prov".equals(codeString))
          return PROV;
        if ("dept".equals(codeString))
          return DEPT;
        if ("team".equals(codeString))
          return TEAM;
        if ("govt".equals(codeString))
          return GOVT;
        if ("ins".equals(codeString))
          return INS;
        if ("edu".equals(codeString))
          return EDU;
        if ("reli".equals(codeString))
          return RELI;
        if ("crs".equals(codeString))
          return CRS;
        if ("cg".equals(codeString))
          return CG;
        if ("bus".equals(codeString))
          return BUS;
        if ("other".equals(codeString))
          return OTHER;
        throw new Exception("Unknown OrganizationType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PROV: return "prov";
            case DEPT: return "dept";
            case TEAM: return "team";
            case GOVT: return "govt";
            case INS: return "ins";
            case EDU: return "edu";
            case RELI: return "reli";
            case CRS: return "crs";
            case CG: return "cg";
            case BUS: return "bus";
            case OTHER: return "other";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/organization-type";
        }
        public String getDefinition() {
          switch (this) {
            case PROV: return "An organization that provides healthcare services.";
            case DEPT: return "A department or ward within a hospital (Generally is not applicable to top level organizations)";
            case TEAM: return "An organizational team is usually a grouping of practitioners that perform a specific function within an organization (which could be a top level organization, or a department).";
            case GOVT: return "A political body, often used when including organization records for government bodies such as a Federal Government, State or Local Government.";
            case INS: return "A company that provides insurance to its subscribers that may include healthcare related policies.";
            case EDU: return "An educational institution that provides education or research facilities.";
            case RELI: return "An organization that is identified as a part of a religious institution.";
            case CRS: return "An organization that is identified as a Pharmaceutical/Clinical Research Sponsor.";
            case CG: return "An un-incorporated community group.";
            case BUS: return "An organization that is a registered business or corporation but not identified by other types.";
            case OTHER: return "Other type of organization not already specified.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PROV: return "Healthcare Provider";
            case DEPT: return "Hospital Department";
            case TEAM: return "Organizational team";
            case GOVT: return "Government";
            case INS: return "Insurance Company";
            case EDU: return "Educational Institute";
            case RELI: return "Religious Institution";
            case CRS: return "Clinical Research Sponsor";
            case CG: return "Community Group";
            case BUS: return "Non-Healthcare Business or Corporation";
            case OTHER: return "Other";
            default: return "?";
          }
    }


}

