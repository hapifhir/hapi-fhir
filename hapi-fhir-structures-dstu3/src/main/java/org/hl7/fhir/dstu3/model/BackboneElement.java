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

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * Base definition for all elements that are defined inside a resource - but not those in a data type.
 */
@DatatypeDef(name="BackboneElement")
public abstract class BackboneElement extends Element implements IBaseBackboneElement {

    /**
     * May be used to represent additional information that is not part of the basic definition of the element, and that modifies the understanding of the element that contains it. Usually modifier elements provide negation or qualification. In order to make the use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the definition of the extension. Applications processing a resource are required to check for modifier extensions.
     */
    @Child(name = "modifierExtension", type = {Extension.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=true, summary=true)
    @Description(shortDefinition="Extensions that cannot be ignored", formalDefinition="May be used to represent additional information that is not part of the basic definition of the element, and that modifies the understanding of the element that contains it. Usually modifier elements provide negation or qualification. In order to make the use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the definition of the extension. Applications processing a resource are required to check for modifier extensions." )
    protected List<Extension> modifierExtension;

    private static final long serialVersionUID = -1431673179L;

  /**
   * Constructor
   */
    public BackboneElement() {
      super();
    }

    /**
     * @return {@link #modifierExtension} (May be used to represent additional information that is not part of the basic definition of the element, and that modifies the understanding of the element that contains it. Usually modifier elements provide negation or qualification. In order to make the use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the definition of the extension. Applications processing a resource are required to check for modifier extensions.)
     */
    public List<Extension> getModifierExtension() { 
      if (this.modifierExtension == null)
        this.modifierExtension = new ArrayList<Extension>();
      return this.modifierExtension;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public BackboneElement setModifierExtension(List<Extension> theModifierExtension) { 
      this.modifierExtension = theModifierExtension;
      return this;
    }

    public boolean hasModifierExtension() { 
      if (this.modifierExtension == null)
        return false;
      for (Extension item : this.modifierExtension)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Extension addModifierExtension() { //3
      Extension t = new Extension();
      if (this.modifierExtension == null)
        this.modifierExtension = new ArrayList<Extension>();
      this.modifierExtension.add(t);
      return t;
    }

    public BackboneElement addModifierExtension(Extension t) { //3
      if (t == null)
        return this;
      if (this.modifierExtension == null)
        this.modifierExtension = new ArrayList<Extension>();
      this.modifierExtension.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #modifierExtension}, creating it if it does not already exist
     */
    public Extension getModifierExtensionFirstRep() { 
      if (getModifierExtension().isEmpty()) {
        addModifierExtension();
      }
      return getModifierExtension().get(0);
    }

      protected void listChildren(List<Property> childrenList) {
        childrenList.add(new Property("modifierExtension", "Extension", "May be used to represent additional information that is not part of the basic definition of the element, and that modifies the understanding of the element that contains it. Usually modifier elements provide negation or qualification. In order to make the use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the definition of the extension. Applications processing a resource are required to check for modifier extensions.", 0, java.lang.Integer.MAX_VALUE, modifierExtension));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -298878168: /*modifierExtension*/ return this.modifierExtension == null ? new Base[0] : this.modifierExtension.toArray(new Base[this.modifierExtension.size()]); // Extension
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -298878168: // modifierExtension
          this.getModifierExtension().add(castToExtension(value)); // Extension
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("modifierExtension")) {
          this.getModifierExtension().add(castToExtension(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -298878168:  return addModifierExtension(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -298878168: /*modifierExtension*/ return new String[] {"Extension"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("modifierExtension")) {
          return addModifierExtension();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "BackboneElement";

  }

      public abstract BackboneElement copy();

      public void copyValues(BackboneElement dst) {
        super.copyValues(dst);
        if (modifierExtension != null) {
          dst.modifierExtension = new ArrayList<Extension>();
          for (Extension i : modifierExtension)
            dst.modifierExtension.add(i.copy());
        };
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof BackboneElement))
          return false;
        BackboneElement o = (BackboneElement) other;
        return compareDeep(modifierExtension, o.modifierExtension, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof BackboneElement))
          return false;
        BackboneElement o = (BackboneElement) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(modifierExtension);
      }


}

