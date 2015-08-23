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


public enum V3EntityNamePartQualifierR2 {

        /**
         * Description:A name part a person acquired.  The name part may be acquired by adoption, or the person may have chosen to use the name part for some other reason.

                        
                           Note: this differs from an Other/Psuedonym/Alias in that an acquired name part is acquired on a formal basis rather than an informal one (e.g. registered as part of the official name).
         */
        AD, 
        /**
         * Description:The name assumed from the partner in a marital relationship.  Usually the spouse's family name.  Note that no inference about gender can be made from the existence of spouse names.
         */
        SP, 
        /**
         * Description:A name that a person was given at birth or established as a consequence of adoption. 

                        
                           Note: This is not used for temporary names assigned at birth such as "Baby of Smith" a" which is just a name with a use code of "TEMP".
         */
        BR, 
        /**
         * Description:Used to indicate which of the various name parts is used when interacting with the person.
         */
        CL, 
        /**
         * Description:Indicates that a name part is just an initial.  Initials do not imply a trailing period since this would not work with non-Latin scripts.  In some languages, initials may consist of more than one letter, e.g., "Ph" could stand for "Philippe" or "Th" For "Thomas".
         */
        IN, 
        /**
         * Description:For organizations a suffix indicating the legal status, e.g., "Inc.", "Co.", "AG", "GmbH", "B.V." "S.A.", "Ltd." etc.
         */
        LS, 
        /**
         * Description:Indicates that the name part is a middle name.

                        
                           Usage Notes: In general, the english "middle name" concept is all of the given names after the first. This qualifier may be used to explicitly indicate which given names are considered to be middle names. The middle name qualifier may also be used with family names. This is a Scandinavian use case, matching the concept of "mellomnavn","mellannamn". Note that there are specific rules that indicate what names may be taken as a mellannamn in different Scandinavian countries.
         */
        MID, 
        /**
         * Description:A prefix has a strong association to the immediately following name part. A prefix has no implicit trailing white space (it has implicit leading white space though).
         */
        PFX, 
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
         * Description:A suffix has a strong association to the immediately preceding name part. A suffix has no implicit leading white space (it has implicit trailing white space though).
         */
        SFX, 
        /**
         * Description:Extra information about the style of a title
         */
        TITLESTYLES, 
        /**
         * Description:Indicates that a title like "Dr.", "M.D." or "Ph.D." is an academic title.
         */
        AC, 
        /**
         * Description:A honorific such as "The Right Honourable" or "Weledelgeleerde Heer".
         */
        HON, 
        /**
         * Description:A nobility title such as Sir, Count, Grafin.
         */
        NB, 
        /**
         * Description:Primarily in the British Imperial culture people tend to have an abbreviation of their professional organization as part of their credential titles.
         */
        PR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3EntityNamePartQualifierR2 fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("AD".equals(codeString))
          return AD;
        if ("SP".equals(codeString))
          return SP;
        if ("BR".equals(codeString))
          return BR;
        if ("CL".equals(codeString))
          return CL;
        if ("IN".equals(codeString))
          return IN;
        if ("LS".equals(codeString))
          return LS;
        if ("MID".equals(codeString))
          return MID;
        if ("PFX".equals(codeString))
          return PFX;
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
        if ("SFX".equals(codeString))
          return SFX;
        if ("TitleStyles".equals(codeString))
          return TITLESTYLES;
        if ("AC".equals(codeString))
          return AC;
        if ("HON".equals(codeString))
          return HON;
        if ("NB".equals(codeString))
          return NB;
        if ("PR".equals(codeString))
          return PR;
        throw new Exception("Unknown V3EntityNamePartQualifierR2 code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case AD: return "AD";
            case SP: return "SP";
            case BR: return "BR";
            case CL: return "CL";
            case IN: return "IN";
            case LS: return "LS";
            case MID: return "MID";
            case PFX: return "PFX";
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
            case SFX: return "SFX";
            case TITLESTYLES: return "TitleStyles";
            case AC: return "AC";
            case HON: return "HON";
            case NB: return "NB";
            case PR: return "PR";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/EntityNamePartQualifierR2";
        }
        public String getDefinition() {
          switch (this) {
            case AD: return "Description:A name part a person acquired.  The name part may be acquired by adoption, or the person may have chosen to use the name part for some other reason.\r\n\n                        \n                           Note: this differs from an Other/Psuedonym/Alias in that an acquired name part is acquired on a formal basis rather than an informal one (e.g. registered as part of the official name).";
            case SP: return "Description:The name assumed from the partner in a marital relationship.  Usually the spouse's family name.  Note that no inference about gender can be made from the existence of spouse names.";
            case BR: return "Description:A name that a person was given at birth or established as a consequence of adoption. \r\n\n                        \n                           Note: This is not used for temporary names assigned at birth such as \"Baby of Smith\" a\" which is just a name with a use code of \"TEMP\".";
            case CL: return "Description:Used to indicate which of the various name parts is used when interacting with the person.";
            case IN: return "Description:Indicates that a name part is just an initial.  Initials do not imply a trailing period since this would not work with non-Latin scripts.  In some languages, initials may consist of more than one letter, e.g., \"Ph\" could stand for \"Philippe\" or \"Th\" For \"Thomas\".";
            case LS: return "Description:For organizations a suffix indicating the legal status, e.g., \"Inc.\", \"Co.\", \"AG\", \"GmbH\", \"B.V.\" \"S.A.\", \"Ltd.\" etc.";
            case MID: return "Description:Indicates that the name part is a middle name.\r\n\n                        \n                           Usage Notes: In general, the english \"middle name\" concept is all of the given names after the first. This qualifier may be used to explicitly indicate which given names are considered to be middle names. The middle name qualifier may also be used with family names. This is a Scandinavian use case, matching the concept of \"mellomnavn\",\"mellannamn\". Note that there are specific rules that indicate what names may be taken as a mellannamn in different Scandinavian countries.";
            case PFX: return "Description:A prefix has a strong association to the immediately following name part. A prefix has no implicit trailing white space (it has implicit leading white space though).";
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
            case SFX: return "Description:A suffix has a strong association to the immediately preceding name part. A suffix has no implicit leading white space (it has implicit trailing white space though).";
            case TITLESTYLES: return "Description:Extra information about the style of a title";
            case AC: return "Description:Indicates that a title like \"Dr.\", \"M.D.\" or \"Ph.D.\" is an academic title.";
            case HON: return "Description:A honorific such as \"The Right Honourable\" or \"Weledelgeleerde Heer\".";
            case NB: return "Description:A nobility title such as Sir, Count, Grafin.";
            case PR: return "Description:Primarily in the British Imperial culture people tend to have an abbreviation of their professional organization as part of their credential titles.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case AD: return "acquired";
            case SP: return "spouse";
            case BR: return "birth";
            case CL: return "callme";
            case IN: return "initial";
            case LS: return "legal status";
            case MID: return "middle name";
            case PFX: return "prefix";
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
            case SFX: return "suffix";
            case TITLESTYLES: return "TitleStyles";
            case AC: return "academic";
            case HON: return "honorific";
            case NB: return "nobility";
            case PR: return "professional";
            default: return "?";
          }
    }


}

