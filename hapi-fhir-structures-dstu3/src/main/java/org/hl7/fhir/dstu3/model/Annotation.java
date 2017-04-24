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

// Generated on Mon, Apr 17, 2017 17:38-0400 for FHIR v3.0.1

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * A  text note which also  contains information about who made the statement and when.
 */
@DatatypeDef(name="Annotation")
public class Annotation extends Type implements ICompositeType {

    /**
     * The individual responsible for making the annotation.
     */
    @Child(name = "author", type = {Practitioner.class, Patient.class, RelatedPerson.class, StringType.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Individual responsible for the annotation", formalDefinition="The individual responsible for making the annotation." )
    protected Type author;

    /**
     * Indicates when this particular annotation was made.
     */
    @Child(name = "time", type = {DateTimeType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When the annotation was made", formalDefinition="Indicates when this particular annotation was made." )
    protected DateTimeType time;

    /**
     * The text of the annotation.
     */
    @Child(name = "text", type = {StringType.class}, order=2, min=1, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The annotation  - text content", formalDefinition="The text of the annotation." )
    protected StringType text;

    private static final long serialVersionUID = -575590381L;

  /**
   * Constructor
   */
    public Annotation() {
      super();
    }

  /**
   * Constructor
   */
    public Annotation(StringType text) {
      super();
      this.text = text;
    }

    /**
     * @return {@link #author} (The individual responsible for making the annotation.)
     */
    public Type getAuthor() { 
      return this.author;
    }

    /**
     * @return {@link #author} (The individual responsible for making the annotation.)
     */
    public Reference getAuthorReference() throws FHIRException { 
      if (!(this.author instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.author.getClass().getName()+" was encountered");
      return (Reference) this.author;
    }

    public boolean hasAuthorReference() { 
      return this.author instanceof Reference;
    }

    /**
     * @return {@link #author} (The individual responsible for making the annotation.)
     */
    public StringType getAuthorStringType() throws FHIRException { 
      if (!(this.author instanceof StringType))
        throw new FHIRException("Type mismatch: the type StringType was expected, but "+this.author.getClass().getName()+" was encountered");
      return (StringType) this.author;
    }

    public boolean hasAuthorStringType() { 
      return this.author instanceof StringType;
    }

    public boolean hasAuthor() { 
      return this.author != null && !this.author.isEmpty();
    }

    /**
     * @param value {@link #author} (The individual responsible for making the annotation.)
     */
    public Annotation setAuthor(Type value) { 
      this.author = value;
      return this;
    }

    /**
     * @return {@link #time} (Indicates when this particular annotation was made.). This is the underlying object with id, value and extensions. The accessor "getTime" gives direct access to the value
     */
    public DateTimeType getTimeElement() { 
      if (this.time == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Annotation.time");
        else if (Configuration.doAutoCreate())
          this.time = new DateTimeType(); // bb
      return this.time;
    }

    public boolean hasTimeElement() { 
      return this.time != null && !this.time.isEmpty();
    }

    public boolean hasTime() { 
      return this.time != null && !this.time.isEmpty();
    }

    /**
     * @param value {@link #time} (Indicates when this particular annotation was made.). This is the underlying object with id, value and extensions. The accessor "getTime" gives direct access to the value
     */
    public Annotation setTimeElement(DateTimeType value) { 
      this.time = value;
      return this;
    }

    /**
     * @return Indicates when this particular annotation was made.
     */
    public Date getTime() { 
      return this.time == null ? null : this.time.getValue();
    }

    /**
     * @param value Indicates when this particular annotation was made.
     */
    public Annotation setTime(Date value) { 
      if (value == null)
        this.time = null;
      else {
        if (this.time == null)
          this.time = new DateTimeType();
        this.time.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #text} (The text of the annotation.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
     */
    public StringType getTextElement() { 
      if (this.text == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Annotation.text");
        else if (Configuration.doAutoCreate())
          this.text = new StringType(); // bb
      return this.text;
    }

    public boolean hasTextElement() { 
      return this.text != null && !this.text.isEmpty();
    }

    public boolean hasText() { 
      return this.text != null && !this.text.isEmpty();
    }

    /**
     * @param value {@link #text} (The text of the annotation.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
     */
    public Annotation setTextElement(StringType value) { 
      this.text = value;
      return this;
    }

    /**
     * @return The text of the annotation.
     */
    public String getText() { 
      return this.text == null ? null : this.text.getValue();
    }

    /**
     * @param value The text of the annotation.
     */
    public Annotation setText(String value) { 
        if (this.text == null)
          this.text = new StringType();
        this.text.setValue(value);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("author[x]", "Reference(Practitioner|Patient|RelatedPerson)|string", "The individual responsible for making the annotation.", 0, java.lang.Integer.MAX_VALUE, author));
        childrenList.add(new Property("time", "dateTime", "Indicates when this particular annotation was made.", 0, java.lang.Integer.MAX_VALUE, time));
        childrenList.add(new Property("text", "string", "The text of the annotation.", 0, java.lang.Integer.MAX_VALUE, text));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1406328437: /*author*/ return this.author == null ? new Base[0] : new Base[] {this.author}; // Type
        case 3560141: /*time*/ return this.time == null ? new Base[0] : new Base[] {this.time}; // DateTimeType
        case 3556653: /*text*/ return this.text == null ? new Base[0] : new Base[] {this.text}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1406328437: // author
          this.author = castToType(value); // Type
          return value;
        case 3560141: // time
          this.time = castToDateTime(value); // DateTimeType
          return value;
        case 3556653: // text
          this.text = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("author[x]")) {
          this.author = castToType(value); // Type
        } else if (name.equals("time")) {
          this.time = castToDateTime(value); // DateTimeType
        } else if (name.equals("text")) {
          this.text = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1475597077:  return getAuthor(); 
        case -1406328437:  return getAuthor(); 
        case 3560141:  return getTimeElement();
        case 3556653:  return getTextElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1406328437: /*author*/ return new String[] {"Reference", "string"};
        case 3560141: /*time*/ return new String[] {"dateTime"};
        case 3556653: /*text*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("authorReference")) {
          this.author = new Reference();
          return this.author;
        }
        else if (name.equals("authorString")) {
          this.author = new StringType();
          return this.author;
        }
        else if (name.equals("time")) {
          throw new FHIRException("Cannot call addChild on a primitive type Annotation.time");
        }
        else if (name.equals("text")) {
          throw new FHIRException("Cannot call addChild on a primitive type Annotation.text");
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Annotation";

  }

      public Annotation copy() {
        Annotation dst = new Annotation();
        copyValues(dst);
        dst.author = author == null ? null : author.copy();
        dst.time = time == null ? null : time.copy();
        dst.text = text == null ? null : text.copy();
        return dst;
      }

      protected Annotation typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Annotation))
          return false;
        Annotation o = (Annotation) other;
        return compareDeep(author, o.author, true) && compareDeep(time, o.time, true) && compareDeep(text, o.text, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Annotation))
          return false;
        Annotation o = (Annotation) other;
        return compareValues(time, o.time, true) && compareValues(text, o.text, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(author, time, text);
      }


}

