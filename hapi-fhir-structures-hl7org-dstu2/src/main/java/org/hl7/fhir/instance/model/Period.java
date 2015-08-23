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

// Generated on Sat, Aug 22, 2015 23:00-0400 for FHIR v0.5.0

import java.util.*;

import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
import org.hl7.fhir.instance.model.annotations.DatatypeDef;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.api.*;
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
     * The end of the period. If the end of the period is missing, it means that the period is ongoing. The start mey be in the past, and the end date in the future, which means that period is expected/planned to end at that time.
     */
    @Child(name = "end", type = {DateTimeType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="End time with inclusive boundary, if not ongoing", formalDefinition="The end of the period. If the end of the period is missing, it means that the period is ongoing. The start mey be in the past, and the end date in the future, which means that period is expected/planned to end at that time." )
    protected DateTimeType end;

    private static final long serialVersionUID = 649791751L;

  /*
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
     * @return {@link #end} (The end of the period. If the end of the period is missing, it means that the period is ongoing. The start mey be in the past, and the end date in the future, which means that period is expected/planned to end at that time.). This is the underlying object with id, value and extensions. The accessor "getEnd" gives direct access to the value
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
     * @param value {@link #end} (The end of the period. If the end of the period is missing, it means that the period is ongoing. The start mey be in the past, and the end date in the future, which means that period is expected/planned to end at that time.). This is the underlying object with id, value and extensions. The accessor "getEnd" gives direct access to the value
     */
    public Period setEndElement(DateTimeType value) { 
      this.end = value;
      return this;
    }

    /**
     * @return The end of the period. If the end of the period is missing, it means that the period is ongoing. The start mey be in the past, and the end date in the future, which means that period is expected/planned to end at that time.
     */
    public Date getEnd() { 
      return this.end == null ? null : this.end.getValue();
    }

    /**
     * @param value The end of the period. If the end of the period is missing, it means that the period is ongoing. The start mey be in the past, and the end date in the future, which means that period is expected/planned to end at that time.
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

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("start", "dateTime", "The start of the period. The boundary is inclusive.", 0, java.lang.Integer.MAX_VALUE, start));
        childrenList.add(new Property("end", "dateTime", "The end of the period. If the end of the period is missing, it means that the period is ongoing. The start mey be in the past, and the end date in the future, which means that period is expected/planned to end at that time.", 0, java.lang.Integer.MAX_VALUE, end));
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
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Period))
          return false;
        Period o = (Period) other;
        return compareDeep(start, o.start, true) && compareDeep(end, o.end, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Period))
          return false;
        Period o = (Period) other;
        return compareValues(start, o.start, true) && compareValues(end, o.end, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (start == null || start.isEmpty()) && (end == null || end.isEmpty())
          ;
      }


}

