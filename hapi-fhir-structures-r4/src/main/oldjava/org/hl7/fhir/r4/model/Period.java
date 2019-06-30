package org.hl7.fhir.r4.model;

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

import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Description;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.ICompositeType;

import java.util.Date;
import java.util.List;
/**
 * A time period defined by a start and end date and optionally time.
 */
@DatatypeDef(name="Period")
public class Period extends Type implements ICompositeType {

    /**
     * The start of the period. The boundary is inclusive.
     */
    @Child(name = "start", type = {DateTimeType.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Starting time with inclusive boundary", formalDefinition="The start of the period. The boundary is inclusive." )
    protected DateTimeType start;

    /**
     * The end of the period. If the end of the period is missing, it means no end was known or planned at the time the instance was created. The start may be in the past, and the end date in the future, which means that period is expected/planned to end at that time.
     */
    @Child(name = "end", type = {DateTimeType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="End time with inclusive boundary, if not ongoing", formalDefinition="The end of the period. If the end of the period is missing, it means no end was known or planned at the time the instance was created. The start may be in the past, and the end date in the future, which means that period is expected/planned to end at that time." )
    protected DateTimeType end;

    private static final long serialVersionUID = 649791751L;

  /**
   * Constructor
   */
    public Period() {
      super();
    }

    /**
     * @return {@link #start} (The start of the period. The boundary is inclusive.). This is the underlying object with id, value and extensions. The accessor "getStart" gives direct access to the value
     */
    public DateTimeType getStartElement() { 
      if (this.start == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Period.start");
        else if (Configuration.doAutoCreate())
          this.start = new DateTimeType(); // bb
      return this.start;
    }

    public boolean hasStartElement() { 
      return this.start != null && !this.start.isEmpty();
    }

    public boolean hasStart() { 
      return this.start != null && !this.start.isEmpty();
    }

    /**
     * @param value {@link #start} (The start of the period. The boundary is inclusive.). This is the underlying object with id, value and extensions. The accessor "getStart" gives direct access to the value
     */
    public Period setStartElement(DateTimeType value) { 
      this.start = value;
      return this;
    }

    /**
     * @return The start of the period. The boundary is inclusive.
     */
    public Date getStart() { 
      return this.start == null ? null : this.start.getValue();
    }

    /**
     * @param value The start of the period. The boundary is inclusive.
     */
    public Period setStart(Date value) { 
      if (value == null)
        this.start = null;
      else {
        if (this.start == null)
          this.start = new DateTimeType();
        this.start.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #end} (The end of the period. If the end of the period is missing, it means no end was known or planned at the time the instance was created. The start may be in the past, and the end date in the future, which means that period is expected/planned to end at that time.). This is the underlying object with id, value and extensions. The accessor "getEnd" gives direct access to the value
     */
    public DateTimeType getEndElement() { 
      if (this.end == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Period.end");
        else if (Configuration.doAutoCreate())
          this.end = new DateTimeType(); // bb
      return this.end;
    }

    public boolean hasEndElement() { 
      return this.end != null && !this.end.isEmpty();
    }

    public boolean hasEnd() { 
      return this.end != null && !this.end.isEmpty();
    }

    /**
     * @param value {@link #end} (The end of the period. If the end of the period is missing, it means no end was known or planned at the time the instance was created. The start may be in the past, and the end date in the future, which means that period is expected/planned to end at that time.). This is the underlying object with id, value and extensions. The accessor "getEnd" gives direct access to the value
     */
    public Period setEndElement(DateTimeType value) { 
      this.end = value;
      return this;
    }

    /**
     * @return The end of the period. If the end of the period is missing, it means no end was known or planned at the time the instance was created. The start may be in the past, and the end date in the future, which means that period is expected/planned to end at that time.
     */
    public Date getEnd() { 
      return this.end == null ? null : this.end.getValue();
    }

    /**
     * @param value The end of the period. If the end of the period is missing, it means no end was known or planned at the time the instance was created. The start may be in the past, and the end date in the future, which means that period is expected/planned to end at that time.
     */
    public Period setEnd(Date value) { 
      if (value == null)
        this.end = null;
      else {
        if (this.end == null)
          this.end = new DateTimeType();
        this.end.setValue(value);
      }
      return this;
    }

   /**
   * Sets the value for <b>start</b> ()
   *
     * <p>
     * <b>Definition:</b>
     * The start of the period. The boundary is inclusive.
     * </p> 
   */
  public Period setStart( Date theDate,  TemporalPrecisionEnum thePrecision) {
    start = new DateTimeType(theDate, thePrecision); 
    return this; 
  }

   /**
   * Sets the value for <b>end</b> ()
   *
     * <p>
     * <b>Definition:</b>
     * The end of the period. The boundary is inclusive.
     * </p> 
   */
  public Period setEnd( Date theDate,  TemporalPrecisionEnum thePrecision) {
    end = new DateTimeType(theDate, thePrecision); 
    return this; 
  }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("start", "dateTime", "The start of the period. The boundary is inclusive.", 0, 1, start));
        children.add(new Property("end", "dateTime", "The end of the period. If the end of the period is missing, it means no end was known or planned at the time the instance was created. The start may be in the past, and the end date in the future, which means that period is expected/planned to end at that time.", 0, 1, end));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case 109757538: /*start*/  return new Property("start", "dateTime", "The start of the period. The boundary is inclusive.", 0, 1, start);
        case 100571: /*end*/  return new Property("end", "dateTime", "The end of the period. If the end of the period is missing, it means no end was known or planned at the time the instance was created. The start may be in the past, and the end date in the future, which means that period is expected/planned to end at that time.", 0, 1, end);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 109757538: /*start*/ return this.start == null ? new Base[0] : new Base[] {this.start}; // DateTimeType
        case 100571: /*end*/ return this.end == null ? new Base[0] : new Base[] {this.end}; // DateTimeType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 109757538: // start
          this.start = castToDateTime(value); // DateTimeType
          return value;
        case 100571: // end
          this.end = castToDateTime(value); // DateTimeType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("start")) {
          this.start = castToDateTime(value); // DateTimeType
        } else if (name.equals("end")) {
          this.end = castToDateTime(value); // DateTimeType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 109757538:  return getStartElement();
        case 100571:  return getEndElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 109757538: /*start*/ return new String[] {"dateTime"};
        case 100571: /*end*/ return new String[] {"dateTime"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("start")) {
          throw new FHIRException("Cannot call addChild on a primitive type Period.start");
        }
        else if (name.equals("end")) {
          throw new FHIRException("Cannot call addChild on a primitive type Period.end");
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Period";

  }

      public Period copy() {
        Period dst = new Period();
        copyValues(dst);
        dst.start = start == null ? null : start.copy();
        dst.end = end == null ? null : end.copy();
        return dst;
      }

      protected Period typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof Period))
          return false;
        Period o = (Period) other_;
        return compareDeep(start, o.start, true) && compareDeep(end, o.end, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof Period))
          return false;
        Period o = (Period) other_;
        return compareValues(start, o.start, true) && compareValues(end, o.end, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(start, end);
      }


}

