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

// Generated on Tue, Jul 12, 2016 12:04-0400 for FHIR v1.5.0

import java.util.*;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.dstu3.exceptions.FHIRException;
/**
 * Specifies various attributes of the patient population for whom and/or environment of care in which a knowledge module is applicable.
 */
@DatatypeDef(name="UsageContext")
public class UsageContext extends Type implements ICompositeType {

    /**
     * Specifies the focus of the usage attribute.
     */
    @Child(name = "focus", type = {Coding.class}, order=0, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="patient-gender | patient-age-group | clinical-focus | target-user | workflow-setting | workflow-task | clinical-venue | jurisdiction", formalDefinition="Specifies the focus of the usage attribute." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/usage-context-focus")
    protected Coding focus;

    /**
     * Provides a value for the usage attribute. Different values are appropriate in different focus areas, as specified in the description of values for focus.
     */
    @Child(name = "value", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Value of the usage attribute", formalDefinition="Provides a value for the usage attribute. Different values are appropriate in different focus areas, as specified in the description of values for focus." )
    protected CodeableConcept value;

    private static final long serialVersionUID = 1672032668L;

  /**
   * Constructor
   */
    public UsageContext() {
      super();
    }

  /**
   * Constructor
   */
    public UsageContext(Coding focus, CodeableConcept value) {
      super();
      this.focus = focus;
      this.value = value;
    }

    /**
     * @return {@link #focus} (Specifies the focus of the usage attribute.)
     */
    public Coding getFocus() { 
      if (this.focus == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create UsageContext.focus");
        else if (Configuration.doAutoCreate())
          this.focus = new Coding(); // cc
      return this.focus;
    }

    public boolean hasFocus() { 
      return this.focus != null && !this.focus.isEmpty();
    }

    /**
     * @param value {@link #focus} (Specifies the focus of the usage attribute.)
     */
    public UsageContext setFocus(Coding value) { 
      this.focus = value;
      return this;
    }

    /**
     * @return {@link #value} (Provides a value for the usage attribute. Different values are appropriate in different focus areas, as specified in the description of values for focus.)
     */
    public CodeableConcept getValue() { 
      if (this.value == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create UsageContext.value");
        else if (Configuration.doAutoCreate())
          this.value = new CodeableConcept(); // cc
      return this.value;
    }

    public boolean hasValue() { 
      return this.value != null && !this.value.isEmpty();
    }

    /**
     * @param value {@link #value} (Provides a value for the usage attribute. Different values are appropriate in different focus areas, as specified in the description of values for focus.)
     */
    public UsageContext setValue(CodeableConcept value) { 
      this.value = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("focus", "Coding", "Specifies the focus of the usage attribute.", 0, java.lang.Integer.MAX_VALUE, focus));
        childrenList.add(new Property("value", "CodeableConcept", "Provides a value for the usage attribute. Different values are appropriate in different focus areas, as specified in the description of values for focus.", 0, java.lang.Integer.MAX_VALUE, value));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 97604824: /*focus*/ return this.focus == null ? new Base[0] : new Base[] {this.focus}; // Coding
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 97604824: // focus
          this.focus = castToCoding(value); // Coding
          break;
        case 111972721: // value
          this.value = castToCodeableConcept(value); // CodeableConcept
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("focus"))
          this.focus = castToCoding(value); // Coding
        else if (name.equals("value"))
          this.value = castToCodeableConcept(value); // CodeableConcept
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 97604824:  return getFocus(); // Coding
        case 111972721:  return getValue(); // CodeableConcept
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("focus")) {
          this.focus = new Coding();
          return this.focus;
        }
        else if (name.equals("value")) {
          this.value = new CodeableConcept();
          return this.value;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "UsageContext";

  }

      public UsageContext copy() {
        UsageContext dst = new UsageContext();
        copyValues(dst);
        dst.focus = focus == null ? null : focus.copy();
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      protected UsageContext typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof UsageContext))
          return false;
        UsageContext o = (UsageContext) other;
        return compareDeep(focus, o.focus, true) && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof UsageContext))
          return false;
        UsageContext o = (UsageContext) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(focus, value);
      }


}

