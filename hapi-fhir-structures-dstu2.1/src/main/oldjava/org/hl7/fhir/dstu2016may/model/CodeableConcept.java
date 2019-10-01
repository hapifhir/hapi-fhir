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
import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.ICompositeType;
import org.hl7.fhir.utilities.Utilities;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Description;
/**
 * A concept that may be defined by a formal reference to a terminology or ontology or may be provided by text.
 */
@DatatypeDef(name="CodeableConcept")
public class CodeableConcept extends Type implements ICompositeType {

    /**
     * A reference to a code defined by a terminology system.
     */
    @Child(name = "coding", type = {Coding.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Code defined by a terminology system", formalDefinition="A reference to a code defined by a terminology system." )
    protected List<Coding> coding;

    /**
     * A human language representation of the concept as seen/selected/uttered by the user who entered the data and/or which represents the intended meaning of the user.
     */
    @Child(name = "text", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Plain text representation of the concept", formalDefinition="A human language representation of the concept as seen/selected/uttered by the user who entered the data and/or which represents the intended meaning of the user." )
    protected StringType text;

    private static final long serialVersionUID = 760353246L;

  /**
   * Constructor
   */
    public CodeableConcept() {
      super();
    }

    /**
     * @return {@link #coding} (A reference to a code defined by a terminology system.)
     */
    public List<Coding> getCoding() { 
      if (this.coding == null)
        this.coding = new ArrayList<Coding>();
      return this.coding;
    }

    public boolean hasCoding() { 
      if (this.coding == null)
        return false;
      for (Coding item : this.coding)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #coding} (A reference to a code defined by a terminology system.)
     */
    // syntactic sugar
    public Coding addCoding() { //3
      Coding t = new Coding();
      if (this.coding == null)
        this.coding = new ArrayList<Coding>();
      this.coding.add(t);
      return t;
    }

    // syntactic sugar
    public CodeableConcept addCoding(Coding t) { //3
      if (t == null)
        return this;
      if (this.coding == null)
        this.coding = new ArrayList<Coding>();
      this.coding.add(t);
      return this;
    }

    /**
     * @return {@link #text} (A human language representation of the concept as seen/selected/uttered by the user who entered the data and/or which represents the intended meaning of the user.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
     */
    public StringType getTextElement() { 
      if (this.text == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CodeableConcept.text");
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
     * @param value {@link #text} (A human language representation of the concept as seen/selected/uttered by the user who entered the data and/or which represents the intended meaning of the user.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
     */
    public CodeableConcept setTextElement(StringType value) { 
      this.text = value;
      return this;
    }

    /**
     * @return A human language representation of the concept as seen/selected/uttered by the user who entered the data and/or which represents the intended meaning of the user.
     */
    public String getText() { 
      return this.text == null ? null : this.text.getValue();
    }

    /**
     * @param value A human language representation of the concept as seen/selected/uttered by the user who entered the data and/or which represents the intended meaning of the user.
     */
    public CodeableConcept setText(String value) { 
      if (Utilities.noString(value))
        this.text = null;
      else {
        if (this.text == null)
          this.text = new StringType();
        this.text.setValue(value);
      }
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("coding", "Coding", "A reference to a code defined by a terminology system.", 0, java.lang.Integer.MAX_VALUE, coding));
        childrenList.add(new Property("text", "string", "A human language representation of the concept as seen/selected/uttered by the user who entered the data and/or which represents the intended meaning of the user.", 0, java.lang.Integer.MAX_VALUE, text));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1355086998: /*coding*/ return this.coding == null ? new Base[0] : this.coding.toArray(new Base[this.coding.size()]); // Coding
        case 3556653: /*text*/ return this.text == null ? new Base[0] : new Base[] {this.text}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1355086998: // coding
          this.getCoding().add(castToCoding(value)); // Coding
          break;
        case 3556653: // text
          this.text = castToString(value); // StringType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("coding"))
          this.getCoding().add(castToCoding(value));
        else if (name.equals("text"))
          this.text = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1355086998:  return addCoding(); // Coding
        case 3556653: throw new FHIRException("Cannot make property text as it is not a complex type"); // StringType
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("coding")) {
          return addCoding();
        }
        else if (name.equals("text")) {
          throw new FHIRException("Cannot call addChild on a primitive type CodeableConcept.text");
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "CodeableConcept";

  }

      public CodeableConcept copy() {
        CodeableConcept dst = new CodeableConcept();
        copyValues(dst);
        if (coding != null) {
          dst.coding = new ArrayList<Coding>();
          for (Coding i : coding)
            dst.coding.add(i.copy());
        };
        dst.text = text == null ? null : text.copy();
        return dst;
      }

      protected CodeableConcept typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CodeableConcept))
          return false;
        CodeableConcept o = (CodeableConcept) other;
        return compareDeep(coding, o.coding, true) && compareDeep(text, o.text, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CodeableConcept))
          return false;
        CodeableConcept o = (CodeableConcept) other;
        return compareValues(text, o.text, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (coding == null || coding.isEmpty()) && (text == null || text.isEmpty())
          ;
      }


}

