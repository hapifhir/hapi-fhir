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

// Generated on Wed, Nov 11, 2015 10:54-0500 for FHIR v1.0.2
import java.util.List;

import org.hl7.fhir.instance.model.api.ICompositeType;
import org.hl7.fhir.instance.utilities.Utilities;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Description;
/**
 * A technical identifier - identifies some entity uniquely and unambiguously.
 */
@DatatypeDef(name="Identifier")
public class Identifier extends Type implements ICompositeType {

    public enum IdentifierUse {
        /**
         * The identifier recommended for display and use in real-world interactions.
         */
        USUAL, 
        /**
         * The identifier considered to be most trusted for the identification of this item.
         */
        OFFICIAL, 
        /**
         * A temporary identifier.
         */
        TEMP, 
        /**
         * An identifier that was assigned in secondary use - it serves to identify the object in a relative context, but cannot be consistently assigned to the same object again in a different context.
         */
        SECONDARY, 
        /**
         * added to help the parsers
         */
        NULL;
        public static IdentifierUse fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("usual".equals(codeString))
          return USUAL;
        if ("official".equals(codeString))
          return OFFICIAL;
        if ("temp".equals(codeString))
          return TEMP;
        if ("secondary".equals(codeString))
          return SECONDARY;
        throw new Exception("Unknown IdentifierUse code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case USUAL: return "usual";
            case OFFICIAL: return "official";
            case TEMP: return "temp";
            case SECONDARY: return "secondary";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case USUAL: return "http://hl7.org/fhir/identifier-use";
            case OFFICIAL: return "http://hl7.org/fhir/identifier-use";
            case TEMP: return "http://hl7.org/fhir/identifier-use";
            case SECONDARY: return "http://hl7.org/fhir/identifier-use";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case USUAL: return "The identifier recommended for display and use in real-world interactions.";
            case OFFICIAL: return "The identifier considered to be most trusted for the identification of this item.";
            case TEMP: return "A temporary identifier.";
            case SECONDARY: return "An identifier that was assigned in secondary use - it serves to identify the object in a relative context, but cannot be consistently assigned to the same object again in a different context.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case USUAL: return "Usual";
            case OFFICIAL: return "Official";
            case TEMP: return "Temp";
            case SECONDARY: return "Secondary";
            default: return "?";
          }
        }
    }

  public static class IdentifierUseEnumFactory implements EnumFactory<IdentifierUse> {
    public IdentifierUse fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("usual".equals(codeString))
          return IdentifierUse.USUAL;
        if ("official".equals(codeString))
          return IdentifierUse.OFFICIAL;
        if ("temp".equals(codeString))
          return IdentifierUse.TEMP;
        if ("secondary".equals(codeString))
          return IdentifierUse.SECONDARY;
        throw new IllegalArgumentException("Unknown IdentifierUse code '"+codeString+"'");
        }
    public String toCode(IdentifierUse code) {
      if (code == IdentifierUse.USUAL)
        return "usual";
      if (code == IdentifierUse.OFFICIAL)
        return "official";
      if (code == IdentifierUse.TEMP)
        return "temp";
      if (code == IdentifierUse.SECONDARY)
        return "secondary";
      return "?";
      }
    }

    /**
     * The purpose of this identifier.
     */
    @Child(name = "use", type = {CodeType.class}, order=0, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="usual | official | temp | secondary (If known)", formalDefinition="The purpose of this identifier." )
    protected Enumeration<IdentifierUse> use;

    /**
     * A coded type for the identifier that can be used to determine which identifier to use for a specific purpose.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Description of identifier", formalDefinition="A coded type for the identifier that can be used to determine which identifier to use for a specific purpose." )
    protected CodeableConcept type;

    /**
     * Establishes the namespace in which set of possible id values is unique.
     */
    @Child(name = "system", type = {UriType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The namespace for the identifier", formalDefinition="Establishes the namespace in which set of possible id values is unique." )
    protected UriType system;

    /**
     * The portion of the identifier typically displayed to the user and which is unique within the context of the system.
     */
    @Child(name = "value", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The value that is unique", formalDefinition="The portion of the identifier typically displayed to the user and which is unique within the context of the system." )
    protected StringType value;

    /**
     * Time period during which identifier is/was valid for use.
     */
    @Child(name = "period", type = {Period.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Time period when id is/was valid for use", formalDefinition="Time period during which identifier is/was valid for use." )
    protected Period period;

    /**
     * Organization that issued/manages the identifier.
     */
    @Child(name = "assigner", type = {Organization.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Organization that issued id (may be just text)", formalDefinition="Organization that issued/manages the identifier." )
    protected Reference assigner;

    /**
     * The actual object that is the target of the reference (Organization that issued/manages the identifier.)
     */
    protected Organization assignerTarget;

    private static final long serialVersionUID = -478840981L;

  /*
   * Constructor
   */
    public Identifier() {
      super();
    }

    /**
     * @return {@link #use} (The purpose of this identifier.). This is the underlying object with id, value and extensions. The accessor "getUse" gives direct access to the value
     */
    public Enumeration<IdentifierUse> getUseElement() { 
      if (this.use == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Identifier.use");
        else if (Configuration.doAutoCreate())
          this.use = new Enumeration<IdentifierUse>(new IdentifierUseEnumFactory()); // bb
      return this.use;
    }

    public boolean hasUseElement() { 
      return this.use != null && !this.use.isEmpty();
    }

    public boolean hasUse() { 
      return this.use != null && !this.use.isEmpty();
    }

    /**
     * @param value {@link #use} (The purpose of this identifier.). This is the underlying object with id, value and extensions. The accessor "getUse" gives direct access to the value
     */
    public Identifier setUseElement(Enumeration<IdentifierUse> value) { 
      this.use = value;
      return this;
    }

    /**
     * @return The purpose of this identifier.
     */
    public IdentifierUse getUse() { 
      return this.use == null ? null : this.use.getValue();
    }

    /**
     * @param value The purpose of this identifier.
     */
    public Identifier setUse(IdentifierUse value) { 
      if (value == null)
        this.use = null;
      else {
        if (this.use == null)
          this.use = new Enumeration<IdentifierUse>(new IdentifierUseEnumFactory());
        this.use.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #type} (A coded type for the identifier that can be used to determine which identifier to use for a specific purpose.)
     */
    public CodeableConcept getType() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Identifier.type");
        else if (Configuration.doAutoCreate())
          this.type = new CodeableConcept(); // cc
      return this.type;
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (A coded type for the identifier that can be used to determine which identifier to use for a specific purpose.)
     */
    public Identifier setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #system} (Establishes the namespace in which set of possible id values is unique.). This is the underlying object with id, value and extensions. The accessor "getSystem" gives direct access to the value
     */
    public UriType getSystemElement() { 
      if (this.system == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Identifier.system");
        else if (Configuration.doAutoCreate())
          this.system = new UriType(); // bb
      return this.system;
    }

    public boolean hasSystemElement() { 
      return this.system != null && !this.system.isEmpty();
    }

    public boolean hasSystem() { 
      return this.system != null && !this.system.isEmpty();
    }

    /**
     * @param value {@link #system} (Establishes the namespace in which set of possible id values is unique.). This is the underlying object with id, value and extensions. The accessor "getSystem" gives direct access to the value
     */
    public Identifier setSystemElement(UriType value) { 
      this.system = value;
      return this;
    }

    /**
     * @return Establishes the namespace in which set of possible id values is unique.
     */
    public String getSystem() { 
      return this.system == null ? null : this.system.getValue();
    }

    /**
     * @param value Establishes the namespace in which set of possible id values is unique.
     */
    public Identifier setSystem(String value) { 
      if (Utilities.noString(value))
        this.system = null;
      else {
        if (this.system == null)
          this.system = new UriType();
        this.system.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #value} (The portion of the identifier typically displayed to the user and which is unique within the context of the system.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
     */
    public StringType getValueElement() { 
      if (this.value == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Identifier.value");
        else if (Configuration.doAutoCreate())
          this.value = new StringType(); // bb
      return this.value;
    }

    public boolean hasValueElement() { 
      return this.value != null && !this.value.isEmpty();
    }

    public boolean hasValue() { 
      return this.value != null && !this.value.isEmpty();
    }

    /**
     * @param value {@link #value} (The portion of the identifier typically displayed to the user and which is unique within the context of the system.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
     */
    public Identifier setValueElement(StringType value) { 
      this.value = value;
      return this;
    }

    /**
     * @return The portion of the identifier typically displayed to the user and which is unique within the context of the system.
     */
    public String getValue() { 
      return this.value == null ? null : this.value.getValue();
    }

    /**
     * @param value The portion of the identifier typically displayed to the user and which is unique within the context of the system.
     */
    public Identifier setValue(String value) { 
      if (Utilities.noString(value))
        this.value = null;
      else {
        if (this.value == null)
          this.value = new StringType();
        this.value.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #period} (Time period during which identifier is/was valid for use.)
     */
    public Period getPeriod() { 
      if (this.period == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Identifier.period");
        else if (Configuration.doAutoCreate())
          this.period = new Period(); // cc
      return this.period;
    }

    public boolean hasPeriod() { 
      return this.period != null && !this.period.isEmpty();
    }

    /**
     * @param value {@link #period} (Time period during which identifier is/was valid for use.)
     */
    public Identifier setPeriod(Period value) { 
      this.period = value;
      return this;
    }

    /**
     * @return {@link #assigner} (Organization that issued/manages the identifier.)
     */
    public Reference getAssigner() { 
      if (this.assigner == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Identifier.assigner");
        else if (Configuration.doAutoCreate())
          this.assigner = new Reference(); // cc
      return this.assigner;
    }

    public boolean hasAssigner() { 
      return this.assigner != null && !this.assigner.isEmpty();
    }

    /**
     * @param value {@link #assigner} (Organization that issued/manages the identifier.)
     */
    public Identifier setAssigner(Reference value) { 
      this.assigner = value;
      return this;
    }

    /**
     * @return {@link #assigner} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Organization that issued/manages the identifier.)
     */
    public Organization getAssignerTarget() { 
      if (this.assignerTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Identifier.assigner");
        else if (Configuration.doAutoCreate())
          this.assignerTarget = new Organization(); // aa
      return this.assignerTarget;
    }

    /**
     * @param value {@link #assigner} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Organization that issued/manages the identifier.)
     */
    public Identifier setAssignerTarget(Organization value) { 
      this.assignerTarget = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("use", "code", "The purpose of this identifier.", 0, java.lang.Integer.MAX_VALUE, use));
        childrenList.add(new Property("type", "CodeableConcept", "A coded type for the identifier that can be used to determine which identifier to use for a specific purpose.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("system", "uri", "Establishes the namespace in which set of possible id values is unique.", 0, java.lang.Integer.MAX_VALUE, system));
        childrenList.add(new Property("value", "string", "The portion of the identifier typically displayed to the user and which is unique within the context of the system.", 0, java.lang.Integer.MAX_VALUE, value));
        childrenList.add(new Property("period", "Period", "Time period during which identifier is/was valid for use.", 0, java.lang.Integer.MAX_VALUE, period));
        childrenList.add(new Property("assigner", "Reference(Organization)", "Organization that issued/manages the identifier.", 0, java.lang.Integer.MAX_VALUE, assigner));
      }

      public Identifier copy() {
        Identifier dst = new Identifier();
        copyValues(dst);
        dst.use = use == null ? null : use.copy();
        dst.type = type == null ? null : type.copy();
        dst.system = system == null ? null : system.copy();
        dst.value = value == null ? null : value.copy();
        dst.period = period == null ? null : period.copy();
        dst.assigner = assigner == null ? null : assigner.copy();
        return dst;
      }

      protected Identifier typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Identifier))
          return false;
        Identifier o = (Identifier) other;
        return compareDeep(use, o.use, true) && compareDeep(type, o.type, true) && compareDeep(system, o.system, true)
           && compareDeep(value, o.value, true) && compareDeep(period, o.period, true) && compareDeep(assigner, o.assigner, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Identifier))
          return false;
        Identifier o = (Identifier) other;
        return compareValues(use, o.use, true) && compareValues(system, o.system, true) && compareValues(value, o.value, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (use == null || use.isEmpty()) && (type == null || type.isEmpty())
           && (system == null || system.isEmpty()) && (value == null || value.isEmpty()) && (period == null || period.isEmpty())
           && (assigner == null || assigner.isEmpty());
      }


}

