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
import org.hl7.fhir.utilities.Utilities;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Description;
/**
 * The parameters to the module. This collection specifies both the input and output parameters. Input parameters are provided by the caller as part of the $evaluate operation. Output parameters are included in the GuidanceResponse.
 */
@DatatypeDef(name="ParameterDefinition")
public class ParameterDefinition extends Type implements ICompositeType {

    /**
     * The name of the parameter.
     */
    @Child(name = "name", type = {CodeType.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Parameter name", formalDefinition="The name of the parameter." )
    protected CodeType name;

    /**
     * Whether the parameter is input or output for the module.
     */
    @Child(name = "use", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="", formalDefinition="Whether the parameter is input or output for the module." )
    protected CodeType use;

    /**
     * The minimum number of times this parameter SHALL appear in the request or response.
     */
    @Child(name = "min", type = {IntegerType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Minimum cardinality", formalDefinition="The minimum number of times this parameter SHALL appear in the request or response." )
    protected IntegerType min;

    /**
     * The maximum number of times this element is permitted to appear in the request or response.
     */
    @Child(name = "max", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Maximum cardinality (a number of *)", formalDefinition="The maximum number of times this element is permitted to appear in the request or response." )
    protected StringType max;

    /**
     * A brief discussion of what the parameter is for and how it is used by the module.
     */
    @Child(name = "documentation", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="A brief description of the parameter", formalDefinition="A brief discussion of what the parameter is for and how it is used by the module." )
    protected StringType documentation;

    /**
     * The type of the parameter.
     */
    @Child(name = "type", type = {CodeType.class}, order=5, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="", formalDefinition="The type of the parameter." )
    protected CodeType type;

    /**
     * If specified, this indicates a profile that the input data must conform to, or that the output data will conform to.
     */
    @Child(name = "profile", type = {StructureDefinition.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The profile of the parameter, any", formalDefinition="If specified, this indicates a profile that the input data must conform to, or that the output data will conform to." )
    protected Reference profile;

    /**
     * The actual object that is the target of the reference (If specified, this indicates a profile that the input data must conform to, or that the output data will conform to.)
     */
    protected StructureDefinition profileTarget;

    private static final long serialVersionUID = -1284894445L;

  /**
   * Constructor
   */
    public ParameterDefinition() {
      super();
    }

  /**
   * Constructor
   */
    public ParameterDefinition(CodeType use, CodeType type) {
      super();
      this.use = use;
      this.type = type;
    }

    /**
     * @return {@link #name} (The name of the parameter.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public CodeType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ParameterDefinition.name");
        else if (Configuration.doAutoCreate())
          this.name = new CodeType(); // bb
      return this.name;
    }

    public boolean hasNameElement() { 
      return this.name != null && !this.name.isEmpty();
    }

    public boolean hasName() { 
      return this.name != null && !this.name.isEmpty();
    }

    /**
     * @param value {@link #name} (The name of the parameter.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public ParameterDefinition setNameElement(CodeType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return The name of the parameter.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value The name of the parameter.
     */
    public ParameterDefinition setName(String value) { 
      if (Utilities.noString(value))
        this.name = null;
      else {
        if (this.name == null)
          this.name = new CodeType();
        this.name.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #use} (Whether the parameter is input or output for the module.). This is the underlying object with id, value and extensions. The accessor "getUse" gives direct access to the value
     */
    public CodeType getUseElement() { 
      if (this.use == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ParameterDefinition.use");
        else if (Configuration.doAutoCreate())
          this.use = new CodeType(); // bb
      return this.use;
    }

    public boolean hasUseElement() { 
      return this.use != null && !this.use.isEmpty();
    }

    public boolean hasUse() { 
      return this.use != null && !this.use.isEmpty();
    }

    /**
     * @param value {@link #use} (Whether the parameter is input or output for the module.). This is the underlying object with id, value and extensions. The accessor "getUse" gives direct access to the value
     */
    public ParameterDefinition setUseElement(CodeType value) { 
      this.use = value;
      return this;
    }

    /**
     * @return Whether the parameter is input or output for the module.
     */
    public String getUse() { 
      return this.use == null ? null : this.use.getValue();
    }

    /**
     * @param value Whether the parameter is input or output for the module.
     */
    public ParameterDefinition setUse(String value) { 
        if (this.use == null)
          this.use = new CodeType();
        this.use.setValue(value);
      return this;
    }

    /**
     * @return {@link #min} (The minimum number of times this parameter SHALL appear in the request or response.). This is the underlying object with id, value and extensions. The accessor "getMin" gives direct access to the value
     */
    public IntegerType getMinElement() { 
      if (this.min == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ParameterDefinition.min");
        else if (Configuration.doAutoCreate())
          this.min = new IntegerType(); // bb
      return this.min;
    }

    public boolean hasMinElement() { 
      return this.min != null && !this.min.isEmpty();
    }

    public boolean hasMin() { 
      return this.min != null && !this.min.isEmpty();
    }

    /**
     * @param value {@link #min} (The minimum number of times this parameter SHALL appear in the request or response.). This is the underlying object with id, value and extensions. The accessor "getMin" gives direct access to the value
     */
    public ParameterDefinition setMinElement(IntegerType value) { 
      this.min = value;
      return this;
    }

    /**
     * @return The minimum number of times this parameter SHALL appear in the request or response.
     */
    public int getMin() { 
      return this.min == null || this.min.isEmpty() ? 0 : this.min.getValue();
    }

    /**
     * @param value The minimum number of times this parameter SHALL appear in the request or response.
     */
    public ParameterDefinition setMin(int value) { 
        if (this.min == null)
          this.min = new IntegerType();
        this.min.setValue(value);
      return this;
    }

    /**
     * @return {@link #max} (The maximum number of times this element is permitted to appear in the request or response.). This is the underlying object with id, value and extensions. The accessor "getMax" gives direct access to the value
     */
    public StringType getMaxElement() { 
      if (this.max == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ParameterDefinition.max");
        else if (Configuration.doAutoCreate())
          this.max = new StringType(); // bb
      return this.max;
    }

    public boolean hasMaxElement() { 
      return this.max != null && !this.max.isEmpty();
    }

    public boolean hasMax() { 
      return this.max != null && !this.max.isEmpty();
    }

    /**
     * @param value {@link #max} (The maximum number of times this element is permitted to appear in the request or response.). This is the underlying object with id, value and extensions. The accessor "getMax" gives direct access to the value
     */
    public ParameterDefinition setMaxElement(StringType value) { 
      this.max = value;
      return this;
    }

    /**
     * @return The maximum number of times this element is permitted to appear in the request or response.
     */
    public String getMax() { 
      return this.max == null ? null : this.max.getValue();
    }

    /**
     * @param value The maximum number of times this element is permitted to appear in the request or response.
     */
    public ParameterDefinition setMax(String value) { 
      if (Utilities.noString(value))
        this.max = null;
      else {
        if (this.max == null)
          this.max = new StringType();
        this.max.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #documentation} (A brief discussion of what the parameter is for and how it is used by the module.). This is the underlying object with id, value and extensions. The accessor "getDocumentation" gives direct access to the value
     */
    public StringType getDocumentationElement() { 
      if (this.documentation == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ParameterDefinition.documentation");
        else if (Configuration.doAutoCreate())
          this.documentation = new StringType(); // bb
      return this.documentation;
    }

    public boolean hasDocumentationElement() { 
      return this.documentation != null && !this.documentation.isEmpty();
    }

    public boolean hasDocumentation() { 
      return this.documentation != null && !this.documentation.isEmpty();
    }

    /**
     * @param value {@link #documentation} (A brief discussion of what the parameter is for and how it is used by the module.). This is the underlying object with id, value and extensions. The accessor "getDocumentation" gives direct access to the value
     */
    public ParameterDefinition setDocumentationElement(StringType value) { 
      this.documentation = value;
      return this;
    }

    /**
     * @return A brief discussion of what the parameter is for and how it is used by the module.
     */
    public String getDocumentation() { 
      return this.documentation == null ? null : this.documentation.getValue();
    }

    /**
     * @param value A brief discussion of what the parameter is for and how it is used by the module.
     */
    public ParameterDefinition setDocumentation(String value) { 
      if (Utilities.noString(value))
        this.documentation = null;
      else {
        if (this.documentation == null)
          this.documentation = new StringType();
        this.documentation.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #type} (The type of the parameter.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public CodeType getTypeElement() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ParameterDefinition.type");
        else if (Configuration.doAutoCreate())
          this.type = new CodeType(); // bb
      return this.type;
    }

    public boolean hasTypeElement() { 
      return this.type != null && !this.type.isEmpty();
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (The type of the parameter.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public ParameterDefinition setTypeElement(CodeType value) { 
      this.type = value;
      return this;
    }

    /**
     * @return The type of the parameter.
     */
    public String getType() { 
      return this.type == null ? null : this.type.getValue();
    }

    /**
     * @param value The type of the parameter.
     */
    public ParameterDefinition setType(String value) { 
        if (this.type == null)
          this.type = new CodeType();
        this.type.setValue(value);
      return this;
    }

    /**
     * @return {@link #profile} (If specified, this indicates a profile that the input data must conform to, or that the output data will conform to.)
     */
    public Reference getProfile() { 
      if (this.profile == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ParameterDefinition.profile");
        else if (Configuration.doAutoCreate())
          this.profile = new Reference(); // cc
      return this.profile;
    }

    public boolean hasProfile() { 
      return this.profile != null && !this.profile.isEmpty();
    }

    /**
     * @param value {@link #profile} (If specified, this indicates a profile that the input data must conform to, or that the output data will conform to.)
     */
    public ParameterDefinition setProfile(Reference value) { 
      this.profile = value;
      return this;
    }

    /**
     * @return {@link #profile} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (If specified, this indicates a profile that the input data must conform to, or that the output data will conform to.)
     */
    public StructureDefinition getProfileTarget() { 
      if (this.profileTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ParameterDefinition.profile");
        else if (Configuration.doAutoCreate())
          this.profileTarget = new StructureDefinition(); // aa
      return this.profileTarget;
    }

    /**
     * @param value {@link #profile} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (If specified, this indicates a profile that the input data must conform to, or that the output data will conform to.)
     */
    public ParameterDefinition setProfileTarget(StructureDefinition value) { 
      this.profileTarget = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("name", "code", "The name of the parameter.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("use", "code", "Whether the parameter is input or output for the module.", 0, java.lang.Integer.MAX_VALUE, use));
        childrenList.add(new Property("min", "integer", "The minimum number of times this parameter SHALL appear in the request or response.", 0, java.lang.Integer.MAX_VALUE, min));
        childrenList.add(new Property("max", "string", "The maximum number of times this element is permitted to appear in the request or response.", 0, java.lang.Integer.MAX_VALUE, max));
        childrenList.add(new Property("documentation", "string", "A brief discussion of what the parameter is for and how it is used by the module.", 0, java.lang.Integer.MAX_VALUE, documentation));
        childrenList.add(new Property("type", "code", "The type of the parameter.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("profile", "Reference(StructureDefinition)", "If specified, this indicates a profile that the input data must conform to, or that the output data will conform to.", 0, java.lang.Integer.MAX_VALUE, profile));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // CodeType
        case 116103: /*use*/ return this.use == null ? new Base[0] : new Base[] {this.use}; // CodeType
        case 108114: /*min*/ return this.min == null ? new Base[0] : new Base[] {this.min}; // IntegerType
        case 107876: /*max*/ return this.max == null ? new Base[0] : new Base[] {this.max}; // StringType
        case 1587405498: /*documentation*/ return this.documentation == null ? new Base[0] : new Base[] {this.documentation}; // StringType
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeType
        case -309425751: /*profile*/ return this.profile == null ? new Base[0] : new Base[] {this.profile}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToCode(value); // CodeType
          break;
        case 116103: // use
          this.use = castToCode(value); // CodeType
          break;
        case 108114: // min
          this.min = castToInteger(value); // IntegerType
          break;
        case 107876: // max
          this.max = castToString(value); // StringType
          break;
        case 1587405498: // documentation
          this.documentation = castToString(value); // StringType
          break;
        case 3575610: // type
          this.type = castToCode(value); // CodeType
          break;
        case -309425751: // profile
          this.profile = castToReference(value); // Reference
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name"))
          this.name = castToCode(value); // CodeType
        else if (name.equals("use"))
          this.use = castToCode(value); // CodeType
        else if (name.equals("min"))
          this.min = castToInteger(value); // IntegerType
        else if (name.equals("max"))
          this.max = castToString(value); // StringType
        else if (name.equals("documentation"))
          this.documentation = castToString(value); // StringType
        else if (name.equals("type"))
          this.type = castToCode(value); // CodeType
        else if (name.equals("profile"))
          this.profile = castToReference(value); // Reference
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: throw new FHIRException("Cannot make property name as it is not a complex type"); // CodeType
        case 116103: throw new FHIRException("Cannot make property use as it is not a complex type"); // CodeType
        case 108114: throw new FHIRException("Cannot make property min as it is not a complex type"); // IntegerType
        case 107876: throw new FHIRException("Cannot make property max as it is not a complex type"); // StringType
        case 1587405498: throw new FHIRException("Cannot make property documentation as it is not a complex type"); // StringType
        case 3575610: throw new FHIRException("Cannot make property type as it is not a complex type"); // CodeType
        case -309425751:  return getProfile(); // Reference
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type ParameterDefinition.name");
        }
        else if (name.equals("use")) {
          throw new FHIRException("Cannot call addChild on a primitive type ParameterDefinition.use");
        }
        else if (name.equals("min")) {
          throw new FHIRException("Cannot call addChild on a primitive type ParameterDefinition.min");
        }
        else if (name.equals("max")) {
          throw new FHIRException("Cannot call addChild on a primitive type ParameterDefinition.max");
        }
        else if (name.equals("documentation")) {
          throw new FHIRException("Cannot call addChild on a primitive type ParameterDefinition.documentation");
        }
        else if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type ParameterDefinition.type");
        }
        else if (name.equals("profile")) {
          this.profile = new Reference();
          return this.profile;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "ParameterDefinition";

  }

      public ParameterDefinition copy() {
        ParameterDefinition dst = new ParameterDefinition();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.use = use == null ? null : use.copy();
        dst.min = min == null ? null : min.copy();
        dst.max = max == null ? null : max.copy();
        dst.documentation = documentation == null ? null : documentation.copy();
        dst.type = type == null ? null : type.copy();
        dst.profile = profile == null ? null : profile.copy();
        return dst;
      }

      protected ParameterDefinition typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ParameterDefinition))
          return false;
        ParameterDefinition o = (ParameterDefinition) other;
        return compareDeep(name, o.name, true) && compareDeep(use, o.use, true) && compareDeep(min, o.min, true)
           && compareDeep(max, o.max, true) && compareDeep(documentation, o.documentation, true) && compareDeep(type, o.type, true)
           && compareDeep(profile, o.profile, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ParameterDefinition))
          return false;
        ParameterDefinition o = (ParameterDefinition) other;
        return compareValues(name, o.name, true) && compareValues(use, o.use, true) && compareValues(min, o.min, true)
           && compareValues(max, o.max, true) && compareValues(documentation, o.documentation, true) && compareValues(type, o.type, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (use == null || use.isEmpty())
           && (min == null || min.isEmpty()) && (max == null || max.isEmpty()) && (documentation == null || documentation.isEmpty())
           && (type == null || type.isEmpty()) && (profile == null || profile.isEmpty());
      }


}

