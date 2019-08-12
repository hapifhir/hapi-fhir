package org.hl7.fhir.dstu2016may.model;

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

// Generated on Sun, May 8, 2016 03:05+1000 for FHIR v1.4.0
import java.util.List;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.ICompositeType;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Description;
/**
 * A set of ordered Quantities defined by a low and high limit.
 */
@DatatypeDef(name="Range")
public class Range extends Type implements ICompositeType {

    /**
     * The low limit. The boundary is inclusive.
     */
    @Child(name = "low", type = {SimpleQuantity.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Low limit", formalDefinition="The low limit. The boundary is inclusive." )
    protected SimpleQuantity low;

    /**
     * The high limit. The boundary is inclusive.
     */
    @Child(name = "high", type = {SimpleQuantity.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="High limit", formalDefinition="The high limit. The boundary is inclusive." )
    protected SimpleQuantity high;

    private static final long serialVersionUID = 1699187994L;

  /**
   * Constructor
   */
    public Range() {
      super();
    }

    /**
     * @return {@link #low} (The low limit. The boundary is inclusive.)
     */
    public SimpleQuantity getLow() { 
      if (this.low == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Range.low");
        else if (Configuration.doAutoCreate())
          this.low = new SimpleQuantity(); // cc
      return this.low;
    }

    public boolean hasLow() { 
      return this.low != null && !this.low.isEmpty();
    }

    /**
     * @param value {@link #low} (The low limit. The boundary is inclusive.)
     */
    public Range setLow(SimpleQuantity value) { 
      this.low = value;
      return this;
    }

    /**
     * @return {@link #high} (The high limit. The boundary is inclusive.)
     */
    public SimpleQuantity getHigh() { 
      if (this.high == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Range.high");
        else if (Configuration.doAutoCreate())
          this.high = new SimpleQuantity(); // cc
      return this.high;
    }

    public boolean hasHigh() { 
      return this.high != null && !this.high.isEmpty();
    }

    /**
     * @param value {@link #high} (The high limit. The boundary is inclusive.)
     */
    public Range setHigh(SimpleQuantity value) { 
      this.high = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("low", "SimpleQuantity", "The low limit. The boundary is inclusive.", 0, java.lang.Integer.MAX_VALUE, low));
        childrenList.add(new Property("high", "SimpleQuantity", "The high limit. The boundary is inclusive.", 0, java.lang.Integer.MAX_VALUE, high));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 107348: /*low*/ return this.low == null ? new Base[0] : new Base[] {this.low}; // SimpleQuantity
        case 3202466: /*high*/ return this.high == null ? new Base[0] : new Base[] {this.high}; // SimpleQuantity
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 107348: // low
          this.low = castToSimpleQuantity(value); // SimpleQuantity
          break;
        case 3202466: // high
          this.high = castToSimpleQuantity(value); // SimpleQuantity
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("low"))
          this.low = castToSimpleQuantity(value); // SimpleQuantity
        else if (name.equals("high"))
          this.high = castToSimpleQuantity(value); // SimpleQuantity
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 107348:  return getLow(); // SimpleQuantity
        case 3202466:  return getHigh(); // SimpleQuantity
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("low")) {
          this.low = new SimpleQuantity();
          return this.low;
        }
        else if (name.equals("high")) {
          this.high = new SimpleQuantity();
          return this.high;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Range";

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

