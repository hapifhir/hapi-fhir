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

public enum V3HL7StandardVersionCode {

        /**
         * The complete set of normative, DSTU, proposed (under ballot) and draft artifacts as published in the ballot whose ballot cycle ended in January 2008.
         */
        BALLOT2008JAN, 
        /**
         * The complete set of normative, DSTU, proposed (under ballot) and draft artifacts as published in the ballot whose ballot cycle ended in May 2008.
         */
        BALLOT2008MAY, 
        /**
         * The complete set of normative, DSTU, proposed (under ballot) and draft artifacts as published in the ballot whose ballot cycle ended in September 2008.
         */
        BALLOT2008SEP, 
        /**
         * The complete set of normative, DSTU, proposed (under ballot) and draft artifacts as published in the ballot whose ballot cycle ended in January 2009.
         */
        BALLOT2009JAN, 
        /**
         * The complete set of normative, DSTU, proposed (under ballot) and draft artifacts as published in the ballot whose ballot cycle ended in May 2009.
         */
        BALLOT2009MAY, 
        /**
         * The complete set of normative, DSTU, proposed (under ballot) and draft artifacts as published in the ballot whose ballot cycle ended in September 2009.
         */
        BALLOT2009SEP, 
        /**
         * Definition: The complete set of normative, DSTU, proposed (under ballot) and draft artifacts as published in the ballot whose ballot cycle ended in January 2010.
         */
        BALLOT2010JAN, 
        /**
         * Definition: The complete set of normative, DSTU, proposed (under ballot) and draft artifacts as published in the ballot whose ballot cycle ended in May 2010.
         */
        BALLOT2010MAY, 
        /**
         * Definition: The complete set of normative, DSTU, proposed (under ballot) and draft artifacts as published in the ballot whose ballot cycle ended in September 2010.
         */
        BALLOT2010SEP, 
        /**
         * Definition: The complete set of normative, DSTU, proposed (under ballot) and draft artifacts as published in the ballot whose ballot cycle ended in January 2011.
         */
        BALLOT2011JAN, 
        /**
         * Definition: The complete set of normative, DSTU, proposed (under ballot) and draft artifacts as published in the ballot whose ballot cycle ended in May 2011.
         */
        BALLOT2011MAY, 
        /**
         * Definition: The complete set of normative, DSTU, proposed (under ballot) and draft artifacts as published in the ballot whose ballot cycle ended in September 2011.
         */
        BALLOT2011SEP, 
        /**
         * Definition: The complete set of normative, DSTU, proposed (under ballot) and draft artifacts as published in the ballot whose ballot cycle ended in January 2012.
         */
        BALLOT2012JAN, 
        /**
         * Definition: The complete set of normative, DSTU, proposed (under ballot) and draft artifacts as published in the ballot whose ballot cycle ended in May 2012.
         */
        BALLOT2012MAY, 
        /**
         * Definition: The complete set of normative, DSTU, proposed (under ballot) and draft artifacts as published in the ballot whose ballot cycle ended in September 2012.
         */
        BALLOT2012SEP, 
        /**
         * The consistent set of messaging artefacts as published or contained in repositories in December of 2003, based on the latest version of any V3 models or artefacts (RIM, Datatypes, CMETS, Common Messages, Vocabularies) as available in December of 2003. Note: This versioncode does not cover the version of the XML ITS.
         */
        V3200312, 
        /**
         * Description:The consistent set of normative and DSTU messaging artifacts as published or contained in repositories in December of 2004, based on the latest version of any V3 models or artifacts (RIM, Datatypes, CMETS, Common Messages, Vocabularies) as published under the title of Normative Edition 2005. Note: This versioncode does not cover the version of the XML ITS.
         */
        V32005N, 
        /**
         * Description:The consistent set of normative and DSTU messaging artifacts as published or contained in repositories in December of 2005, based on the latest version of any V3 models or artifacts (RIM, Datatypes, CMETS, Common Messages, Vocabularies) as published under the title of Normative Edition 2006. Note: This versioncode does not cover the version of the XML ITS.
         */
        V32006N, 
        /**
         * Definition: The consistent set of normative and DSTU messaging artifacts as published or contained in repositories in December of 2007, based on the latest version of any V3 models or artifacts (RIM, Data Types, CMETS, Common Messages, Vocabularies) published under the title of Normative Edition 2008. Note: This version code does not cover the version of the XML ITS.
         */
        V32008N, 
        /**
         * Description: The consistent set of normative and DSTU messaging artifacts as published or contained in repositories in December of 2008, based on the latest version of any V3 models or artifacts (RIM, Data Types, CMETS, Common Messages, Vocabularies) published under the title of Normative Edition 2009. Note: This version code does not cover the version of the XML ITS.
         */
        V32009N, 
        /**
         * Definition: The consistent set of normative and DSTU messaging artifacts as published or contained in repositories in December of 2009, based on the latest version of any V3 models or artifacts (RIM, Data Types, CMETS, Common Messages, Vocabularies) published under the title of Normative Edition 2010. Note: This version code does not cover the version of the XML ITS.
         */
        V32010N, 
        /**
         * Definition: The consistent set of normative and DSTU messaging artifacts as published or contained in repositories in December of 2010, based on the latest version of any V3 models or artifacts (RIM, Data Types, CMETS, Common Messages, Vocabularies) published under the title of Normative Edition 2011. Note: This version code does not cover the version of the XML ITS.
         */
        V32011N, 
        /**
         * Definition: The consistent set of normative and DSTU messaging artifacts as published or contained in repositories in December of 2011, based on the latest version of any V3 models or artifacts (RIM, Data Types, CMETS, Common Messages, Vocabularies) published under the title of Normative Edition 2012. Note: This version code does not cover the version of the XML ITS.
         */
        V32012N, 
        /**
         * Includes all material published as part of the ballot package released for vote in July-August 2003.
         */
        V3PR1, 
        /**
         * Description:The consistent set of normative and DSTU messaging artifacts as published or contained in repositories in December of 2006, based on the latest version of any V3 models or artifacts (RIM, Datatypes, CMETS, Common Messages, Vocabularies) published under the title of Normative Edition 2007. Note: This versioncode does not cover the version of the XML ITS.
         */
        V32007N, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3HL7StandardVersionCode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("Ballot2008Jan".equals(codeString))
          return BALLOT2008JAN;
        if ("Ballot2008May".equals(codeString))
          return BALLOT2008MAY;
        if ("Ballot2008Sep".equals(codeString))
          return BALLOT2008SEP;
        if ("Ballot2009Jan".equals(codeString))
          return BALLOT2009JAN;
        if ("Ballot2009May".equals(codeString))
          return BALLOT2009MAY;
        if ("Ballot2009Sep".equals(codeString))
          return BALLOT2009SEP;
        if ("Ballot2010Jan".equals(codeString))
          return BALLOT2010JAN;
        if ("Ballot2010May".equals(codeString))
          return BALLOT2010MAY;
        if ("Ballot2010Sep".equals(codeString))
          return BALLOT2010SEP;
        if ("Ballot2011Jan".equals(codeString))
          return BALLOT2011JAN;
        if ("Ballot2011May".equals(codeString))
          return BALLOT2011MAY;
        if ("Ballot2011Sep".equals(codeString))
          return BALLOT2011SEP;
        if ("Ballot2012Jan".equals(codeString))
          return BALLOT2012JAN;
        if ("Ballot2012May".equals(codeString))
          return BALLOT2012MAY;
        if ("Ballot2012Sep".equals(codeString))
          return BALLOT2012SEP;
        if ("V3-2003-12".equals(codeString))
          return V3200312;
        if ("V3-2005N".equals(codeString))
          return V32005N;
        if ("V3-2006N".equals(codeString))
          return V32006N;
        if ("V3-2008N".equals(codeString))
          return V32008N;
        if ("V3-2009N".equals(codeString))
          return V32009N;
        if ("V3-2010N".equals(codeString))
          return V32010N;
        if ("V3-2011N".equals(codeString))
          return V32011N;
        if ("V3-2012N".equals(codeString))
          return V32012N;
        if ("V3PR1".equals(codeString))
          return V3PR1;
        if ("V3-2007N".equals(codeString))
          return V32007N;
        throw new FHIRException("Unknown V3HL7StandardVersionCode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case BALLOT2008JAN: return "Ballot2008Jan";
            case BALLOT2008MAY: return "Ballot2008May";
            case BALLOT2008SEP: return "Ballot2008Sep";
            case BALLOT2009JAN: return "Ballot2009Jan";
            case BALLOT2009MAY: return "Ballot2009May";
            case BALLOT2009SEP: return "Ballot2009Sep";
            case BALLOT2010JAN: return "Ballot2010Jan";
            case BALLOT2010MAY: return "Ballot2010May";
            case BALLOT2010SEP: return "Ballot2010Sep";
            case BALLOT2011JAN: return "Ballot2011Jan";
            case BALLOT2011MAY: return "Ballot2011May";
            case BALLOT2011SEP: return "Ballot2011Sep";
            case BALLOT2012JAN: return "Ballot2012Jan";
            case BALLOT2012MAY: return "Ballot2012May";
            case BALLOT2012SEP: return "Ballot2012Sep";
            case V3200312: return "V3-2003-12";
            case V32005N: return "V3-2005N";
            case V32006N: return "V3-2006N";
            case V32008N: return "V3-2008N";
            case V32009N: return "V3-2009N";
            case V32010N: return "V3-2010N";
            case V32011N: return "V3-2011N";
            case V32012N: return "V3-2012N";
            case V3PR1: return "V3PR1";
            case V32007N: return "V3-2007N";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/v3-HL7StandardVersionCode";
        }
        public String getDefinition() {
          switch (this) {
            case BALLOT2008JAN: return "The complete set of normative, DSTU, proposed (under ballot) and draft artifacts as published in the ballot whose ballot cycle ended in January 2008.";
            case BALLOT2008MAY: return "The complete set of normative, DSTU, proposed (under ballot) and draft artifacts as published in the ballot whose ballot cycle ended in May 2008.";
            case BALLOT2008SEP: return "The complete set of normative, DSTU, proposed (under ballot) and draft artifacts as published in the ballot whose ballot cycle ended in September 2008.";
            case BALLOT2009JAN: return "The complete set of normative, DSTU, proposed (under ballot) and draft artifacts as published in the ballot whose ballot cycle ended in January 2009.";
            case BALLOT2009MAY: return "The complete set of normative, DSTU, proposed (under ballot) and draft artifacts as published in the ballot whose ballot cycle ended in May 2009.";
            case BALLOT2009SEP: return "The complete set of normative, DSTU, proposed (under ballot) and draft artifacts as published in the ballot whose ballot cycle ended in September 2009.";
            case BALLOT2010JAN: return "Definition: The complete set of normative, DSTU, proposed (under ballot) and draft artifacts as published in the ballot whose ballot cycle ended in January 2010.";
            case BALLOT2010MAY: return "Definition: The complete set of normative, DSTU, proposed (under ballot) and draft artifacts as published in the ballot whose ballot cycle ended in May 2010.";
            case BALLOT2010SEP: return "Definition: The complete set of normative, DSTU, proposed (under ballot) and draft artifacts as published in the ballot whose ballot cycle ended in September 2010.";
            case BALLOT2011JAN: return "Definition: The complete set of normative, DSTU, proposed (under ballot) and draft artifacts as published in the ballot whose ballot cycle ended in January 2011.";
            case BALLOT2011MAY: return "Definition: The complete set of normative, DSTU, proposed (under ballot) and draft artifacts as published in the ballot whose ballot cycle ended in May 2011.";
            case BALLOT2011SEP: return "Definition: The complete set of normative, DSTU, proposed (under ballot) and draft artifacts as published in the ballot whose ballot cycle ended in September 2011.";
            case BALLOT2012JAN: return "Definition: The complete set of normative, DSTU, proposed (under ballot) and draft artifacts as published in the ballot whose ballot cycle ended in January 2012.";
            case BALLOT2012MAY: return "Definition: The complete set of normative, DSTU, proposed (under ballot) and draft artifacts as published in the ballot whose ballot cycle ended in May 2012.";
            case BALLOT2012SEP: return "Definition: The complete set of normative, DSTU, proposed (under ballot) and draft artifacts as published in the ballot whose ballot cycle ended in September 2012.";
            case V3200312: return "The consistent set of messaging artefacts as published or contained in repositories in December of 2003, based on the latest version of any V3 models or artefacts (RIM, Datatypes, CMETS, Common Messages, Vocabularies) as available in December of 2003. Note: This versioncode does not cover the version of the XML ITS.";
            case V32005N: return "Description:The consistent set of normative and DSTU messaging artifacts as published or contained in repositories in December of 2004, based on the latest version of any V3 models or artifacts (RIM, Datatypes, CMETS, Common Messages, Vocabularies) as published under the title of Normative Edition 2005. Note: This versioncode does not cover the version of the XML ITS.";
            case V32006N: return "Description:The consistent set of normative and DSTU messaging artifacts as published or contained in repositories in December of 2005, based on the latest version of any V3 models or artifacts (RIM, Datatypes, CMETS, Common Messages, Vocabularies) as published under the title of Normative Edition 2006. Note: This versioncode does not cover the version of the XML ITS.";
            case V32008N: return "Definition: The consistent set of normative and DSTU messaging artifacts as published or contained in repositories in December of 2007, based on the latest version of any V3 models or artifacts (RIM, Data Types, CMETS, Common Messages, Vocabularies) published under the title of Normative Edition 2008. Note: This version code does not cover the version of the XML ITS.";
            case V32009N: return "Description: The consistent set of normative and DSTU messaging artifacts as published or contained in repositories in December of 2008, based on the latest version of any V3 models or artifacts (RIM, Data Types, CMETS, Common Messages, Vocabularies) published under the title of Normative Edition 2009. Note: This version code does not cover the version of the XML ITS.";
            case V32010N: return "Definition: The consistent set of normative and DSTU messaging artifacts as published or contained in repositories in December of 2009, based on the latest version of any V3 models or artifacts (RIM, Data Types, CMETS, Common Messages, Vocabularies) published under the title of Normative Edition 2010. Note: This version code does not cover the version of the XML ITS.";
            case V32011N: return "Definition: The consistent set of normative and DSTU messaging artifacts as published or contained in repositories in December of 2010, based on the latest version of any V3 models or artifacts (RIM, Data Types, CMETS, Common Messages, Vocabularies) published under the title of Normative Edition 2011. Note: This version code does not cover the version of the XML ITS.";
            case V32012N: return "Definition: The consistent set of normative and DSTU messaging artifacts as published or contained in repositories in December of 2011, based on the latest version of any V3 models or artifacts (RIM, Data Types, CMETS, Common Messages, Vocabularies) published under the title of Normative Edition 2012. Note: This version code does not cover the version of the XML ITS.";
            case V3PR1: return "Includes all material published as part of the ballot package released for vote in July-August 2003.";
            case V32007N: return "Description:The consistent set of normative and DSTU messaging artifacts as published or contained in repositories in December of 2006, based on the latest version of any V3 models or artifacts (RIM, Datatypes, CMETS, Common Messages, Vocabularies) published under the title of Normative Edition 2007. Note: This versioncode does not cover the version of the XML ITS.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case BALLOT2008JAN: return "Ballot 2008 January";
            case BALLOT2008MAY: return "Ballot 2008 May";
            case BALLOT2008SEP: return "Ballot 2008 September";
            case BALLOT2009JAN: return "Ballot 2009 January";
            case BALLOT2009MAY: return "Ballot 2009 May";
            case BALLOT2009SEP: return "Ballot 2009 September";
            case BALLOT2010JAN: return "Ballot 2010 Jan";
            case BALLOT2010MAY: return "Ballot 2010 May";
            case BALLOT2010SEP: return "Ballot 2010 Sep";
            case BALLOT2011JAN: return "Ballot 2011 Jan";
            case BALLOT2011MAY: return "Ballot 2011 May";
            case BALLOT2011SEP: return "Ballot 2011 Sep";
            case BALLOT2012JAN: return "Ballot 2012 Jan";
            case BALLOT2012MAY: return "Ballot 2012 May";
            case BALLOT2012SEP: return "Ballot 2012 Sep";
            case V3200312: return "HL7 Version V3-2003-12";
            case V32005N: return "2005 Normative Edition";
            case V32006N: return "2006 Normative Edition";
            case V32008N: return "2008 Normative Edition";
            case V32009N: return "2009 Normative Edition";
            case V32010N: return "2010 Normative Edition";
            case V32011N: return "2011 Normative Edition";
            case V32012N: return "2012 Normative Edition";
            case V3PR1: return "Version3 Pre-release #1";
            case V32007N: return "2007 Normative Edition";
            default: return "?";
          }
    }


}

