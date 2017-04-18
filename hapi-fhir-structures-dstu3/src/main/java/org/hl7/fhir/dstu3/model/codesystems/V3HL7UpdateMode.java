package org.hl7.fhir.dstu3.model.codesystems;

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

// Generated on Mon, Apr 17, 2017 17:38-0400 for FHIR v3.0.1


import org.hl7.fhir.exceptions.FHIRException;

public enum V3HL7UpdateMode {

        /**
         * Description:The item was (or is to be) added, having not been present immediately before. (If it is already present, this may be treated as an error condition.)
         */
        A, 
        /**
         * Description:The item was (or is to be) either added or replaced.
         */
        AR, 
        /**
         * Description:The item was (or is to be) removed (sometimes referred to as deleted). If the item is part of a collection, delete any matching items.
         */
        D, 
        /**
         * Description:This item is part of the identifying information for this object.
         */
        K, 
        /**
         * Description:There was (or is to be) no change to the item. This is primarily used when this element has not changed, but other attributes in the instance have changed.
         */
        N, 
        /**
         * Description:The item existed previously and has (or is to be) revised. (If an item does not already exist, this may be treated as an error condition.)
         */
        R, 
        /**
         * Description:This item provides enough information to allow a processing system to locate the full applicable record by identifying the object.
         */
        REF, 
        /**
         * Description:Description:</b>It is not specified whether or what kind of change has occurred to the item, or whether the item is present as a reference or identifying property.
         */
        U, 
        /**
         * These concepts apply when the element and/or message is updating a set of items.
         */
        _SETUPDATEMODE, 
        /**
         * Add the message element to the collection of items on the receiving system that correspond to the message element.
         */
        ESA, 
        /**
         * Change the item on the receiving system that corresponds to this message element; if a matching element does not exist, add a new one created with the values in the message.
         */
        ESAC, 
        /**
         * Change the item on the receiving system that corresponds to this message element; do not process if a matching element does not exist.
         */
        ESC, 
        /**
         * Delete the item on the receiving system that corresponds to this message element.
         */
        ESD, 
        /**
         * Description: AU: If this item exists, update it with these values. If it does not exist, create it with these values. If the item is part of the collection, update each item that matches this item, and if no items match, add a new item to the collection.
         */
        AU, 
        /**
         * Ignore this role, it is not relevant to the update.
         */
        I, 
        /**
         * Verify - this message element must match a value already in the receiving systems database in order to process the message.
         */
        V, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3HL7UpdateMode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("A".equals(codeString))
          return A;
        if ("AR".equals(codeString))
          return AR;
        if ("D".equals(codeString))
          return D;
        if ("K".equals(codeString))
          return K;
        if ("N".equals(codeString))
          return N;
        if ("R".equals(codeString))
          return R;
        if ("REF".equals(codeString))
          return REF;
        if ("U".equals(codeString))
          return U;
        if ("_SetUpdateMode".equals(codeString))
          return _SETUPDATEMODE;
        if ("ESA".equals(codeString))
          return ESA;
        if ("ESAC".equals(codeString))
          return ESAC;
        if ("ESC".equals(codeString))
          return ESC;
        if ("ESD".equals(codeString))
          return ESD;
        if ("AU".equals(codeString))
          return AU;
        if ("I".equals(codeString))
          return I;
        if ("V".equals(codeString))
          return V;
        throw new FHIRException("Unknown V3HL7UpdateMode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case A: return "A";
            case AR: return "AR";
            case D: return "D";
            case K: return "K";
            case N: return "N";
            case R: return "R";
            case REF: return "REF";
            case U: return "U";
            case _SETUPDATEMODE: return "_SetUpdateMode";
            case ESA: return "ESA";
            case ESAC: return "ESAC";
            case ESC: return "ESC";
            case ESD: return "ESD";
            case AU: return "AU";
            case I: return "I";
            case V: return "V";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/HL7UpdateMode";
        }
        public String getDefinition() {
          switch (this) {
            case A: return "Description:The item was (or is to be) added, having not been present immediately before. (If it is already present, this may be treated as an error condition.)";
            case AR: return "Description:The item was (or is to be) either added or replaced.";
            case D: return "Description:The item was (or is to be) removed (sometimes referred to as deleted). If the item is part of a collection, delete any matching items.";
            case K: return "Description:This item is part of the identifying information for this object.";
            case N: return "Description:There was (or is to be) no change to the item. This is primarily used when this element has not changed, but other attributes in the instance have changed.";
            case R: return "Description:The item existed previously and has (or is to be) revised. (If an item does not already exist, this may be treated as an error condition.)";
            case REF: return "Description:This item provides enough information to allow a processing system to locate the full applicable record by identifying the object.";
            case U: return "Description:Description:</b>It is not specified whether or what kind of change has occurred to the item, or whether the item is present as a reference or identifying property.";
            case _SETUPDATEMODE: return "These concepts apply when the element and/or message is updating a set of items.";
            case ESA: return "Add the message element to the collection of items on the receiving system that correspond to the message element.";
            case ESAC: return "Change the item on the receiving system that corresponds to this message element; if a matching element does not exist, add a new one created with the values in the message.";
            case ESC: return "Change the item on the receiving system that corresponds to this message element; do not process if a matching element does not exist.";
            case ESD: return "Delete the item on the receiving system that corresponds to this message element.";
            case AU: return "Description: AU: If this item exists, update it with these values. If it does not exist, create it with these values. If the item is part of the collection, update each item that matches this item, and if no items match, add a new item to the collection.";
            case I: return "Ignore this role, it is not relevant to the update.";
            case V: return "Verify - this message element must match a value already in the receiving systems database in order to process the message.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case A: return "Add";
            case AR: return "Add or Replace";
            case D: return "Remove";
            case K: return "Key";
            case N: return "No Change";
            case R: return "Replace";
            case REF: return "Reference";
            case U: return "Unknown";
            case _SETUPDATEMODE: return "SetUpdateMode";
            case ESA: return "Set Add";
            case ESAC: return "Set Add or Change";
            case ESC: return "Set Change";
            case ESD: return "Set Delete";
            case AU: return "Add or Update";
            case I: return "Ignore";
            case V: return "Verify";
            default: return "?";
          }
    }


}

