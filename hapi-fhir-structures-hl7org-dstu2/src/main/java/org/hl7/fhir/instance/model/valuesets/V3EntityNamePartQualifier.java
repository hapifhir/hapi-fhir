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

// Generated on Sat, Aug 22, 2015 23:00-0400 for FHIR v0.5.0


public enum V3EntityNamePartQualifier {

        /**
         * OrganizationNamePartQualifier
         */
        _ORGANIZATIONNAMEPARTQUALIFIER, 
        /**
         * Indicates that a prefix like "Dr." or a suffix like "M.D." or "Ph.D." is an academic title.
         */
        AC, 
        /**
         * The name the person was given at the time of adoption.
         */
        AD, 
        /**
         * A name that a person had shortly after being born. Usually for family names but may be used to mark given names at birth that may have changed later.
         */
        BR, 
        /**
         * A callme name is (usually a given name) that is preferred when a person is directly addressed.
         */
        CL, 
        /**
         * Indicates that a name part is just an initial. Initials do not imply a trailing period since this would not work with non-Latin scripts.  Initials may consist of more than one letter, e.g., "Ph." could stand for "Philippe" or "Th." for "Thomas".
         */
        IN, 
        /**
         * For organizations a suffix indicating the legal status, e.g., "Inc.", "Co.", "AG", "GmbH", "B.V." "S.A.",  "Ltd." etc.
         */
        LS, 
        /**
         * In Europe and Asia, there are still people with nobility titles (aristocrats).  German "von" is generally a nobility title, not a mere voorvoegsel.  Others are "Earl of" or "His Majesty King of..." etc.  Rarely used nowadays, but some systems do keep track of this.
         */
        NB, 
        /**
         * Primarily in the British Imperial culture people tend to have an abbreviation of their professional organization as part of their credential suffices.
         */
        PR, 
        /**
         * The name assumed from the partner in a marital relationship (hence the "SP"). Usually the spouse's family name. Note that no inference about gender can be made from the existence of spouse names.
         */
        SP, 
        /**
         * Indicates that a prefix or a suffix is a title that applies to the whole name, not just the adjacent name part.
         */
        TITLE, 
        /**
         * A Dutch "voorvoegsel" is something like "van" or "de" that might have indicated nobility in the past but no longer so. Similar prefixes exist in other languages such as Spanish, French or Portugese.
         */
        VV, 
        /**
         * Description: Medication Name Parts are a means of specifying a range of acceptable "official" forms of the name of a product.  They are used as patterns against which input name strings may be matched for automatic identification of products from input text reports.   While they cover the concepts held under "doseForm" or "route" or "strength" the name parts are not the same and do not fit into a controlled vocabulary in the same way. By specifying up to 8 name parts a much larger range of possible names can be generated.
         */
        PHARMACEUTICALENTITYNAMEPARTQUALIFIERS, 
        /**
         * Description: This refers to the container if present in the medicinal product name.

                        EXAMPLES: 

                        
                           
                              For Optaflu suspension for injection in pre-filled syringe Influenza vaccine (surface antigen, inactivated, prepared in cell culture) (2007/2008 season): pre-filled syringe
         */
        CON, 
        /**
         * Description: This refers to the qualifiers in the name for devices and is at the moment mainly applicable to insulins and inhalation products.

                        EXAMPLES: 

                        
                           
                              For the medicinal product Actrapid FlexPen 100 IU/ml Solution for injection Subcutaneous use: FlexPen.
         */
        DEV, 
        /**
         * Description: This refers to a flavor of the medicinal product if present in the medicinal product name.

                        
                           Examples:
                        

                        
                           For 'CoughCure Linctus Orange Flavor', the flavor part is "Orange"
                           For 'Wonderdrug Syrup Cherry Flavor', the flavor part is "Cherry"
         */
        FLAV, 
        /**
         * Description: This refers to the formulation of the medicinal product if present in the medicinal product name.

                        
                           Examples:
                        

                        
                           For 'SpecialMed Sugar Free Cough Syrup', the formulation name part is "Sugar Free"
                           For 'QuickCure Gluten-free Bulk Fibre', the formulation name part is "gluten-free"
         */
        FORMUL, 
        /**
         * Description: This refers to the pharmaceutical form/ if present in the medicinal product name.

                        EXAMPLES: 

                        
                           
                              For Agenerase 50 mg soft capsules: Soft Capsules

                           
                           
                              For Ludiomil 25mg-Filmtabletten: Filmtabletten

                           
                           
                              For Optaflu suspension for injection in pre-filled syringe Influenza vaccine (surface antigen, inactivated, prepared in cell culture) (2007/2008 season): suspension for injection
         */
        FRM, 
        /**
         * Description: This refers to the product name without the trademark or the name of the marketing authorization holder or any other descriptor reflected in the product name and, if appropriate, whether it is intended e.g. for babies, children or adults. 

                        EXAMPLES: 

                        
                           
                              Agenerase

                           
                           
                              Optaflu

                           
                           
                              Ludiomil
         */
        INV, 
        /**
         * Description: This refers to the target population for the medicinal product if present in the medicinal product name

                        
                           Examples:
                        

                        
                           For 'Broncho-Drug 3.5 mg-capsules for children', the target population part is "children"
                           For 'Adult Chesty Cough Syrup', the target population part is "adult"
         */
        POPUL, 
        /**
         * Description: This refers to the product common or scientific name without the trademark or the name of the marketing authorization holder or any other descriptor reflected in the product name.

                        EXAMPLES: 

                        
                           
                              For Agenerase: N/A

                           
                           
                              For Optaflu: Influenza vaccine (surface antigen, inactivated, prepared in cell culture) (2007/2008 season)

                           
                           
                              For Ludiomil: N/A
         */
        SCI, 
        /**
         * Description: This refers to the strength if present in the medicinal product name. The use of decimal points should be accommodated if required.

                        EXAMPLES:

                        
                           
                              For Agenerase 50 mg soft capsules: 50mg

                           
                           
                              For Ludiomil 25mg-Filmtabletten: 25 mg

                           
                           
                              For Optaflu suspension for injection in pre-filled syringe Influenza vaccine (surface antigen, inactivated, prepared in cell culture) (2007/2008 season): N/A
         */
        STR, 
        /**
         * Description: This refers to a time or time period that may be specified in the text of the medicinal product name

                        
                           Example:
                        

                        
                           For an influenza vaccine 'Drug-FLU season 2008/2009', the time/period part is "2008/2009 season"
         */
        TIME, 
        /**
         * Description: This refers to trademark/company element if present in the medicinal product name.

                        EXAMPLES: 

                        
                           
                              for Insulin Human Winthrop Comb 15: Winthrop
         */
        TMK, 
        /**
         * Description: This refers to the intended use if present in the medicinal product name without the trademark or the name of the marketing authorization holder or any other descriptor reflected in the product name.


                        
                           Examples:
                        

                        
                           For 'Drug-BI Caplets - Heartburn Relief', the intended use part is: "Heartburn Relief"
                           For 'Medicine Honey Syrup for Soothing Coughs' the intended use part is "Soothing Coughs"
         */
        USE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3EntityNamePartQualifier fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("_OrganizationNamePartQualifier".equals(codeString))
          return _ORGANIZATIONNAMEPARTQUALIFIER;
        if ("AC".equals(codeString))
          return AC;
        if ("AD".equals(codeString))
          return AD;
        if ("BR".equals(codeString))
          return BR;
        if ("CL".equals(codeString))
          return CL;
        if ("IN".equals(codeString))
          return IN;
        if ("LS".equals(codeString))
          return LS;
        if ("NB".equals(codeString))
          return NB;
        if ("PR".equals(codeString))
          return PR;
        if ("SP".equals(codeString))
          return SP;
        if ("TITLE".equals(codeString))
          return TITLE;
        if ("VV".equals(codeString))
          return VV;
        if ("PharmaceuticalEntityNamePartQualifiers".equals(codeString))
          return PHARMACEUTICALENTITYNAMEPARTQUALIFIERS;
        if ("CON".equals(codeString))
          return CON;
        if ("DEV".equals(codeString))
          return DEV;
        if ("FLAV".equals(codeString))
          return FLAV;
        if ("FORMUL".equals(codeString))
          return FORMUL;
        if ("FRM".equals(codeString))
          return FRM;
        if ("INV".equals(codeString))
          return INV;
        if ("POPUL".equals(codeString))
          return POPUL;
        if ("SCI".equals(codeString))
          return SCI;
        if ("STR".equals(codeString))
          return STR;
        if ("TIME".equals(codeString))
          return TIME;
        if ("TMK".equals(codeString))
          return TMK;
        if ("USE".equals(codeString))
          return USE;
        throw new Exception("Unknown V3EntityNamePartQualifier code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _ORGANIZATIONNAMEPARTQUALIFIER: return "_OrganizationNamePartQualifier";
            case AC: return "AC";
            case AD: return "AD";
            case BR: return "BR";
            case CL: return "CL";
            case IN: return "IN";
            case LS: return "LS";
            case NB: return "NB";
            case PR: return "PR";
            case SP: return "SP";
            case TITLE: return "TITLE";
            case VV: return "VV";
            case PHARMACEUTICALENTITYNAMEPARTQUALIFIERS: return "PharmaceuticalEntityNamePartQualifiers";
            case CON: return "CON";
            case DEV: return "DEV";
            case FLAV: return "FLAV";
            case FORMUL: return "FORMUL";
            case FRM: return "FRM";
            case INV: return "INV";
            case POPUL: return "POPUL";
            case SCI: return "SCI";
            case STR: return "STR";
            case TIME: return "TIME";
            case TMK: return "TMK";
            case USE: return "USE";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/EntityNamePartQualifier";
        }
        public String getDefinition() {
          switch (this) {
            case _ORGANIZATIONNAMEPARTQUALIFIER: return "OrganizationNamePartQualifier";
            case AC: return "Indicates that a prefix like \"Dr.\" or a suffix like \"M.D.\" or \"Ph.D.\" is an academic title.";
            case AD: return "The name the person was given at the time of adoption.";
            case BR: return "A name that a person had shortly after being born. Usually for family names but may be used to mark given names at birth that may have changed later.";
            case CL: return "A callme name is (usually a given name) that is preferred when a person is directly addressed.";
            case IN: return "Indicates that a name part is just an initial. Initials do not imply a trailing period since this would not work with non-Latin scripts.  Initials may consist of more than one letter, e.g., \"Ph.\" could stand for \"Philippe\" or \"Th.\" for \"Thomas\".";
            case LS: return "For organizations a suffix indicating the legal status, e.g., \"Inc.\", \"Co.\", \"AG\", \"GmbH\", \"B.V.\" \"S.A.\",  \"Ltd.\" etc.";
            case NB: return "In Europe and Asia, there are still people with nobility titles (aristocrats).  German \"von\" is generally a nobility title, not a mere voorvoegsel.  Others are \"Earl of\" or \"His Majesty King of...\" etc.  Rarely used nowadays, but some systems do keep track of this.";
            case PR: return "Primarily in the British Imperial culture people tend to have an abbreviation of their professional organization as part of their credential suffices.";
            case SP: return "The name assumed from the partner in a marital relationship (hence the \"SP\"). Usually the spouse's family name. Note that no inference about gender can be made from the existence of spouse names.";
            case TITLE: return "Indicates that a prefix or a suffix is a title that applies to the whole name, not just the adjacent name part.";
            case VV: return "A Dutch \"voorvoegsel\" is something like \"van\" or \"de\" that might have indicated nobility in the past but no longer so. Similar prefixes exist in other languages such as Spanish, French or Portugese.";
            case PHARMACEUTICALENTITYNAMEPARTQUALIFIERS: return "Description: Medication Name Parts are a means of specifying a range of acceptable \"official\" forms of the name of a product.  They are used as patterns against which input name strings may be matched for automatic identification of products from input text reports.   While they cover the concepts held under \"doseForm\" or \"route\" or \"strength\" the name parts are not the same and do not fit into a controlled vocabulary in the same way. By specifying up to 8 name parts a much larger range of possible names can be generated.";
            case CON: return "Description: This refers to the container if present in the medicinal product name.\r\n\n                        EXAMPLES: \r\n\n                        \n                           \n                              For Optaflu suspension for injection in pre-filled syringe Influenza vaccine (surface antigen, inactivated, prepared in cell culture) (2007/2008 season): pre-filled syringe";
            case DEV: return "Description: This refers to the qualifiers in the name for devices and is at the moment mainly applicable to insulins and inhalation products.\r\n\n                        EXAMPLES: \r\n\n                        \n                           \n                              For the medicinal product Actrapid FlexPen 100 IU/ml Solution for injection Subcutaneous use: FlexPen.";
            case FLAV: return "Description: This refers to a flavor of the medicinal product if present in the medicinal product name.\r\n\n                        \n                           Examples:\n                        \r\n\n                        \n                           For 'CoughCure Linctus Orange Flavor', the flavor part is \"Orange\"\n                           For 'Wonderdrug Syrup Cherry Flavor', the flavor part is \"Cherry\"";
            case FORMUL: return "Description: This refers to the formulation of the medicinal product if present in the medicinal product name.\r\n\n                        \n                           Examples:\n                        \r\n\n                        \n                           For 'SpecialMed Sugar Free Cough Syrup', the formulation name part is \"Sugar Free\"\n                           For 'QuickCure Gluten-free Bulk Fibre', the formulation name part is \"gluten-free\"";
            case FRM: return "Description: This refers to the pharmaceutical form/ if present in the medicinal product name.\r\n\n                        EXAMPLES: \r\n\n                        \n                           \n                              For Agenerase 50 mg soft capsules: Soft Capsules\r\n\n                           \n                           \n                              For Ludiomil 25mg-Filmtabletten: Filmtabletten\r\n\n                           \n                           \n                              For Optaflu suspension for injection in pre-filled syringe Influenza vaccine (surface antigen, inactivated, prepared in cell culture) (2007/2008 season): suspension for injection";
            case INV: return "Description: This refers to the product name without the trademark or the name of the marketing authorization holder or any other descriptor reflected in the product name and, if appropriate, whether it is intended e.g. for babies, children or adults. \r\n\n                        EXAMPLES: \r\n\n                        \n                           \n                              Agenerase\r\n\n                           \n                           \n                              Optaflu\r\n\n                           \n                           \n                              Ludiomil";
            case POPUL: return "Description: This refers to the target population for the medicinal product if present in the medicinal product name\r\n\n                        \n                           Examples:\n                        \r\n\n                        \n                           For 'Broncho-Drug 3.5 mg-capsules for children', the target population part is \"children\"\n                           For 'Adult Chesty Cough Syrup', the target population part is \"adult\"";
            case SCI: return "Description: This refers to the product common or scientific name without the trademark or the name of the marketing authorization holder or any other descriptor reflected in the product name.\r\n\n                        EXAMPLES: \r\n\n                        \n                           \n                              For Agenerase: N/A\r\n\n                           \n                           \n                              For Optaflu: Influenza vaccine (surface antigen, inactivated, prepared in cell culture) (2007/2008 season)\r\n\n                           \n                           \n                              For Ludiomil: N/A";
            case STR: return "Description: This refers to the strength if present in the medicinal product name. The use of decimal points should be accommodated if required.\r\n\n                        EXAMPLES:\r\n\n                        \n                           \n                              For Agenerase 50 mg soft capsules: 50mg\r\n\n                           \n                           \n                              For Ludiomil 25mg-Filmtabletten: 25 mg\r\n\n                           \n                           \n                              For Optaflu suspension for injection in pre-filled syringe Influenza vaccine (surface antigen, inactivated, prepared in cell culture) (2007/2008 season): N/A";
            case TIME: return "Description: This refers to a time or time period that may be specified in the text of the medicinal product name\r\n\n                        \n                           Example:\n                        \r\n\n                        \n                           For an influenza vaccine 'Drug-FLU season 2008/2009', the time/period part is \"2008/2009 season\"";
            case TMK: return "Description: This refers to trademark/company element if present in the medicinal product name.\r\n\n                        EXAMPLES: \r\n\n                        \n                           \n                              for Insulin Human Winthrop Comb 15: Winthrop";
            case USE: return "Description: This refers to the intended use if present in the medicinal product name without the trademark or the name of the marketing authorization holder or any other descriptor reflected in the product name.\n\r\n\n                        \n                           Examples:\n                        \r\n\n                        \n                           For 'Drug-BI Caplets - Heartburn Relief', the intended use part is: \"Heartburn Relief\"\n                           For 'Medicine Honey Syrup for Soothing Coughs' the intended use part is \"Soothing Coughs\"";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _ORGANIZATIONNAMEPARTQUALIFIER: return "OrganizationNamePartQualifier";
            case AC: return "academic";
            case AD: return "adopted";
            case BR: return "birth";
            case CL: return "callme";
            case IN: return "initial";
            case LS: return "Legal status";
            case NB: return "nobility";
            case PR: return "professional";
            case SP: return "spouse";
            case TITLE: return "title";
            case VV: return "voorvoegsel";
            case PHARMACEUTICALENTITYNAMEPARTQUALIFIERS: return "PharmaceuticalEntityNamePartQualifiers";
            case CON: return "container name";
            case DEV: return "device name";
            case FLAV: return "FlavorName";
            case FORMUL: return "FormulationPartName";
            case FRM: return "form name";
            case INV: return "invented name";
            case POPUL: return "TargetPopulationName";
            case SCI: return "scientific name";
            case STR: return "strength name";
            case TIME: return "TimeOrPeriodName";
            case TMK: return "trademark name";
            case USE: return "intended use name";
            default: return "?";
          }
    }


}

