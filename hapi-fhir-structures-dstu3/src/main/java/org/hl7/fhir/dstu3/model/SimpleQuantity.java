package org.hl7.fhir.dstu3.model;

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

// Generated on Sat, Jan 30, 2016 09:18-0500 for FHIR v1.3.0

import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Block;

import org.hl7.fhir.dstu3.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.*;
/**
 * A measured amount (or an amount that can potentially be measured). Note that measured amounts include amounts that are not precisely quantified, including amounts involving arbitrary units and floating currencies.
 */
@DatatypeDef(name="SimpleQuantity", profileOf=Quantity.class)
public class SimpleQuantity extends Quantity {

    private static final long serialVersionUID = 1069574054L;

      public SimpleQuantity copy() {
        SimpleQuantity dst = new SimpleQuantity();
        copyValues(dst);
        dst.value = value == null ? null : value.copy();
        dst.comparator = comparator == null ? null : comparator.copy();
        dst.unit = unit == null ? null : unit.copy();
        dst.system = system == null ? null : system.copy();
        dst.code = code == null ? null : code.copy();
        return dst;
      }

      protected SimpleQuantity typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SimpleQuantity))
          return false;
        SimpleQuantity o = (SimpleQuantity) other;
        return compareDeep(value, o.value, true) && compareDeep(comparator, o.comparator, true) && compareDeep(unit, o.unit, true)
           && compareDeep(system, o.system, true) && compareDeep(code, o.code, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SimpleQuantity))
          return false;
        SimpleQuantity o = (SimpleQuantity) other;
        return compareValues(value, o.value, true) && compareValues(comparator, o.comparator, true) && compareValues(unit, o.unit, true)
           && compareValues(system, o.system, true) && compareValues(code, o.code, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (value == null || value.isEmpty()) && (comparator == null || comparator.isEmpty())
           && (unit == null || unit.isEmpty()) && (system == null || system.isEmpty()) && (code == null || code.isEmpty())
          ;
      }


}

