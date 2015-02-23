package org.hl7.fhir.instance.model;

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

// Generated on Wed, Feb 18, 2015 12:09-0500 for FHIR v0.4.0

import java.util.*;

import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
import org.hl7.fhir.instance.model.annotations.DatatypeDef;
/**
 * A set of ordered Quantities defined by a low and high limit.
 */
@DatatypeDef(name="Range")
public class Range extends Type implements ICompositeType {

    /**
     * The low limit. The boundary is inclusive.
     */
    @Child(name = "low", type = {Quantity.class}, order = 0, min = 0, max = 1)
    @Description(shortDefinition="Low limit", formalDefinition="The low limit. The boundary is inclusive." )
    protected Quantity low;

    /**
     * The high limit. The boundary is inclusive.
     */
    @Child(name = "high", type = {Quantity.class}, order = 1, min = 0, max = 1)
    @Description(shortDefinition="High limit", formalDefinition="The high limit. The boundary is inclusive." )
    protected Quantity high;

    private static final long serialVersionUID = -474933350L;

    public Range() {
      super();
    }

    /**
     * @return {@link #low} (The low limit. The boundary is inclusive.)
     */
    public Quantity getLow() { 
      if (this.low == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Range.low");
        else if (Configuration.doAutoCreate())
          this.low = new Quantity(); // cc
      return this.low;
    }

    public boolean hasLow() { 
      return this.low != null && !this.low.isEmpty();
    }

    /**
     * @param value {@link #low} (The low limit. The boundary is inclusive.)
     */
    public Range setLow(Quantity value) { 
      this.low = value;
      return this;
    }

    /**
     * @return {@link #high} (The high limit. The boundary is inclusive.)
     */
    public Quantity getHigh() { 
      if (this.high == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Range.high");
        else if (Configuration.doAutoCreate())
          this.high = new Quantity(); // cc
      return this.high;
    }

    public boolean hasHigh() { 
      return this.high != null && !this.high.isEmpty();
    }

    /**
     * @param value {@link #high} (The high limit. The boundary is inclusive.)
     */
    public Range setHigh(Quantity value) { 
      this.high = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("low", "Quantity", "The low limit. The boundary is inclusive.", 0, java.lang.Integer.MAX_VALUE, low));
        childrenList.add(new Property("high", "Quantity", "The high limit. The boundary is inclusive.", 0, java.lang.Integer.MAX_VALUE, high));
      }

      public Range copy() {
        Range dst = new Range();
        copyValues(dst);
        dst.low = low == null ? null : low.copy();
        dst.high = high == null ? null : high.copy();
        return dst;
      }

      protected Range typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Range))
          return false;
        Range o = (Range) other;
        return compareDeep(low, o.low, true) && compareDeep(high, o.high, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Range))
          return false;
        Range o = (Range) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (low == null || low.isEmpty()) && (high == null || high.isEmpty())
          ;
      }


}

