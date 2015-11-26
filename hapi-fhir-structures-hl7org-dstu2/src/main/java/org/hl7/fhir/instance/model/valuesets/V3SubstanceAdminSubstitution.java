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


public enum V3SubstanceAdminSubstitution {

        /**
         * Description: Substitution occurred or is permitted with another product that may potentially have different ingredients, but having the same biological and therapeutic effects.
         */
        _ACTSUBSTANCEADMINSUBSTITUTIONCODE, 
        /**
         * Description: Substitution occurred or is permitted with another bioequivalent and therapeutically equivalent product.
         */
        E, 
        /**
         * Description: 
                        

                        Substitution occurred or is permitted with another product that is a:

                        
                           pharmaceutical alternative containing the same active ingredient but is formulated with different salt, ester
                           pharmaceutical equivalent that has the same active ingredient, strength, dosage form and route of administration
                        
                        
                           Examples: 
                        

                        
                           
                              Pharmaceutical alternative: Erythromycin Ethylsuccinate for Erythromycin Stearate
                           
                              Pharmaceutical equivalent: Lisonpril for Zestril
         */
        EC, 
        /**
         * Description: 
                        

                        Substitution occurred or is permitted between equivalent Brands but not Generics

                        
                           Examples: 
                        

                        
                           Zestril  for Prinivil
                           Coumadin for Jantoven
         */
        BC, 
        /**
         * Description: Substitution occurred or is permitted between equivalent Generics but not Brands

                        
                           Examples: 
                        

                        
                           Lisnopril (Lupin Corp) for Lisnopril (Wockhardt Corp)
         */
        G, 
        /**
         * Description: Substitution occurred or is permitted with another product having the same therapeutic objective and safety profile.

                        
                           Examples: 
                        

                        
                           ranitidine for Tagamet
         */
        TE, 
        /**
         * Description: Substitution occurred or is permitted between therapeutically equivalent Brands but not Generics
>
                           Examples: 
                        

                        
                           Zantac for Tagamet
         */
        TB, 
        /**
         * Description: Substitution occurred or is permitted between therapeutically equivalent Generics but not Brands
>
                           Examples: 
                        

                        
                           Ranitidine  for cimetidine
         */
        TG, 
        /**
         * Description: This substitution was performed or is permitted based on formulary guidelines.
         */
        F, 
        /**
         * No substitution occurred or is permitted.
         */
        N, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3SubstanceAdminSubstitution fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("_ActSubstanceAdminSubstitutionCode".equals(codeString))
          return _ACTSUBSTANCEADMINSUBSTITUTIONCODE;
        if ("E".equals(codeString))
          return E;
        if ("EC".equals(codeString))
          return EC;
        if ("BC".equals(codeString))
          return BC;
        if ("G".equals(codeString))
          return G;
        if ("TE".equals(codeString))
          return TE;
        if ("TB".equals(codeString))
          return TB;
        if ("TG".equals(codeString))
          return TG;
        if ("F".equals(codeString))
          return F;
        if ("N".equals(codeString))
          return N;
        throw new Exception("Unknown V3SubstanceAdminSubstitution code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _ACTSUBSTANCEADMINSUBSTITUTIONCODE: return "_ActSubstanceAdminSubstitutionCode";
            case E: return "E";
            case EC: return "EC";
            case BC: return "BC";
            case G: return "G";
            case TE: return "TE";
            case TB: return "TB";
            case TG: return "TG";
            case F: return "F";
            case N: return "N";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/substanceAdminSubstitution";
        }
        public String getDefinition() {
          switch (this) {
            case _ACTSUBSTANCEADMINSUBSTITUTIONCODE: return "Description: Substitution occurred or is permitted with another product that may potentially have different ingredients, but having the same biological and therapeutic effects.";
            case E: return "Description: Substitution occurred or is permitted with another bioequivalent and therapeutically equivalent product.";
            case EC: return "Description: \n                        \r\n\n                        Substitution occurred or is permitted with another product that is a:\r\n\n                        \n                           pharmaceutical alternative containing the same active ingredient but is formulated with different salt, ester\n                           pharmaceutical equivalent that has the same active ingredient, strength, dosage form and route of administration\n                        \n                        \n                           Examples: \n                        \r\n\n                        \n                           \n                              Pharmaceutical alternative: Erythromycin Ethylsuccinate for Erythromycin Stearate\n                           \n                              Pharmaceutical equivalent: Lisonpril for Zestril";
            case BC: return "Description: \n                        \r\n\n                        Substitution occurred or is permitted between equivalent Brands but not Generics\r\n\n                        \n                           Examples: \n                        \r\n\n                        \n                           Zestril  for Prinivil\n                           Coumadin for Jantoven";
            case G: return "Description: Substitution occurred or is permitted between equivalent Generics but not Brands\r\n\n                        \n                           Examples: \n                        \r\n\n                        \n                           Lisnopril (Lupin Corp) for Lisnopril (Wockhardt Corp)";
            case TE: return "Description: Substitution occurred or is permitted with another product having the same therapeutic objective and safety profile.\r\n\n                        \n                           Examples: \n                        \r\n\n                        \n                           ranitidine for Tagamet";
            case TB: return "Description: Substitution occurred or is permitted between therapeutically equivalent Brands but not Generics\r\n>\n                           Examples: \n                        \r\n\n                        \n                           Zantac for Tagamet";
            case TG: return "Description: Substitution occurred or is permitted between therapeutically equivalent Generics but not Brands\r\n>\n                           Examples: \n                        \r\n\n                        \n                           Ranitidine  for cimetidine";
            case F: return "Description: This substitution was performed or is permitted based on formulary guidelines.";
            case N: return "No substitution occurred or is permitted.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _ACTSUBSTANCEADMINSUBSTITUTIONCODE: return "ActSubstanceAdminSubstitutionCode";
            case E: return "equivalent";
            case EC: return "equivalent composition";
            case BC: return "brand composition";
            case G: return "generic composition";
            case TE: return "therapeutic alternative";
            case TB: return "therapeutic brand";
            case TG: return "therapeutic generic";
            case F: return "formulary";
            case N: return "none";
            default: return "?";
          }
    }


}

