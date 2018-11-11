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

public enum V3EntityDeterminer {

        /**
         * Description:A determiner that specifies that the Entity object represents a particular physical thing (as opposed to a universal, kind, or class of physical thing).

                        
                           Discussion: It does not matter whether an INSTANCE still exists as a whole at the point in time (or process) when we mention it, for example, a drug product lot is an INSTANCE even though it has been portioned out for retail purpose.
         */
        INSTANCE, 
        /**
         * A determiner that specifies that the Entity object represents a particular collection of physical things (as opposed to a universal, kind, or class of physical thing).  While the collection may resolve to having only a single individual (or even no individuals), the potential should exist for it to cover multiple individuals.
         */
        GROUP, 
        /**
         * Description:A determiner that specifies that the Entity object represents a universal, kind or class of physical thing (as opposed to a particular thing).
         */
        KIND, 
        /**
         * A determiner that specifies that the Entity object represents a universal, kind or class of collections physical things.  While the collection may resolve to having only a single individual (or even no individuals), the potential should exist for it to cover multiple individuals.
         */
        GROUPKIND, 
        /**
         * The described quantified determiner indicates that the given Entity is taken as a general description of a specific amount of a thing. For example, QUANTIFIED_KIND of syringe (quantity = 3,) stands for exactly three syringes.
         */
        QUANTIFIEDKIND, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3EntityDeterminer fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("INSTANCE".equals(codeString))
          return INSTANCE;
        if ("GROUP".equals(codeString))
          return GROUP;
        if ("KIND".equals(codeString))
          return KIND;
        if ("GROUPKIND".equals(codeString))
          return GROUPKIND;
        if ("QUANTIFIED_KIND".equals(codeString))
          return QUANTIFIEDKIND;
        throw new FHIRException("Unknown V3EntityDeterminer code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case INSTANCE: return "INSTANCE";
            case GROUP: return "GROUP";
            case KIND: return "KIND";
            case GROUPKIND: return "GROUPKIND";
            case QUANTIFIEDKIND: return "QUANTIFIED_KIND";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/v3-EntityDeterminer";
        }
        public String getDefinition() {
          switch (this) {
            case INSTANCE: return "Description:A determiner that specifies that the Entity object represents a particular physical thing (as opposed to a universal, kind, or class of physical thing).\r\n\n                        \n                           Discussion: It does not matter whether an INSTANCE still exists as a whole at the point in time (or process) when we mention it, for example, a drug product lot is an INSTANCE even though it has been portioned out for retail purpose.";
            case GROUP: return "A determiner that specifies that the Entity object represents a particular collection of physical things (as opposed to a universal, kind, or class of physical thing).  While the collection may resolve to having only a single individual (or even no individuals), the potential should exist for it to cover multiple individuals.";
            case KIND: return "Description:A determiner that specifies that the Entity object represents a universal, kind or class of physical thing (as opposed to a particular thing).";
            case GROUPKIND: return "A determiner that specifies that the Entity object represents a universal, kind or class of collections physical things.  While the collection may resolve to having only a single individual (or even no individuals), the potential should exist for it to cover multiple individuals.";
            case QUANTIFIEDKIND: return "The described quantified determiner indicates that the given Entity is taken as a general description of a specific amount of a thing. For example, QUANTIFIED_KIND of syringe (quantity = 3,) stands for exactly three syringes.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INSTANCE: return "specific";
            case GROUP: return "specific group";
            case KIND: return "described";
            case GROUPKIND: return "described group";
            case QUANTIFIEDKIND: return "described quantified";
            default: return "?";
          }
    }


}

