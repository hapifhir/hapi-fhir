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

public enum ProductCategory {

        /**
         * A collection of tissues joined in a structural unit to serve a common function.
         */
        ORGAN, 
        /**
         * An ensemble of similar cells and their extracellular matrix from the same origin that together carry out a specific function.
         */
        TISSUE, 
        /**
         * Body fluid.
         */
        FLUID, 
        /**
         * Collection of cells.
         */
        CELLS, 
        /**
         * Biological agent of unspecified type.
         */
        BIOLOGICALAGENT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ProductCategory fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("organ".equals(codeString))
          return ORGAN;
        if ("tissue".equals(codeString))
          return TISSUE;
        if ("fluid".equals(codeString))
          return FLUID;
        if ("cells".equals(codeString))
          return CELLS;
        if ("biologicalAgent".equals(codeString))
          return BIOLOGICALAGENT;
        throw new FHIRException("Unknown ProductCategory code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ORGAN: return "organ";
            case TISSUE: return "tissue";
            case FLUID: return "fluid";
            case CELLS: return "cells";
            case BIOLOGICALAGENT: return "biologicalAgent";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/product-category";
        }
        public String getDefinition() {
          switch (this) {
            case ORGAN: return "A collection of tissues joined in a structural unit to serve a common function.";
            case TISSUE: return "An ensemble of similar cells and their extracellular matrix from the same origin that together carry out a specific function.";
            case FLUID: return "Body fluid.";
            case CELLS: return "Collection of cells.";
            case BIOLOGICALAGENT: return "Biological agent of unspecified type.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ORGAN: return "Organ";
            case TISSUE: return "Tissue";
            case FLUID: return "Fluid";
            case CELLS: return "Cells";
            case BIOLOGICALAGENT: return "BiologicalAgent";
            default: return "?";
          }
    }


}

