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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hl7.fhir.instance.model.Enumerations.BindingStrength;
import org.hl7.fhir.instance.model.Enumerations.BindingStrengthEnumFactory;
import org.hl7.fhir.instance.model.Enumerations.ConformanceResourceStatus;
import org.hl7.fhir.instance.model.Enumerations.ConformanceResourceStatusEnumFactory;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.instance.utilities.Utilities;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
/**
 * A formal computable definition of an operation (on the RESTful interface) or a named query (using the search interaction).
 */
@ResourceDef(name="OperationDefinition", profile="http://hl7.org/fhir/Profile/OperationDefinition")
public class OperationDefinition extends DomainResource {

    public enum OperationKind {
        /**
         * This operation is invoked as an operation.
         */
        OPERATION, 
        /**
         * This operation is a named query, invoked using the search mechanism.
         */
        QUERY, 
        /**
         * added to help the parsers
         */
        NULL;
        public static OperationKind fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("operation".equals(codeString))
          return OPERATION;
        if ("query".equals(codeString))
          return QUERY;
        throw new Exception("Unknown OperationKind code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case OPERATION: return "operation";
            case QUERY: return "query";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case OPERATION: return "http://hl7.org/fhir/operation-kind";
            case QUERY: return "http://hl7.org/fhir/operation-kind";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case OPERATION: return "This operation is invoked as an operation.";
            case QUERY: return "This operation is a named query, invoked using the search mechanism.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case OPERATION: return "Operation";
            case QUERY: return "Query";
            default: return "?";
          }
        }
    }

  public static class OperationKindEnumFactory implements EnumFactory<OperationKind> {
    public OperationKind fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("operation".equals(codeString))
          return OperationKind.OPERATION;
        if ("query".equals(codeString))
          return OperationKind.QUERY;
        throw new IllegalArgumentException("Unknown OperationKind code '"+codeString+"'");
        }
    public String toCode(OperationKind code) {
      if (code == OperationKind.OPERATION)
        return "operation";
      if (code == OperationKind.QUERY)
        return "query";
      return "?";
      }
    }

    public enum OperationParameterUse {
        /**
         * This is an input parameter.
         */
        IN, 
        /**
         * This is an output parameter.
         */
        OUT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static OperationParameterUse fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("in".equals(codeString))
          return IN;
        if ("out".equals(codeString))
          return OUT;
        throw new Exception("Unknown OperationParameterUse code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case IN: return "in";
            case OUT: return "out";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case IN: return "http://hl7.org/fhir/operation-parameter-use";
            case OUT: return "http://hl7.org/fhir/operation-parameter-use";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case IN: return "This is an input parameter.";
            case OUT: return "This is an output parameter.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case IN: return "In";
            case OUT: return "Out";
            default: return "?";
          }
        }
    }

  public static class OperationParameterUseEnumFactory implements EnumFactory<OperationParameterUse> {
    public OperationParameterUse fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("in".equals(codeString))
          return OperationParameterUse.IN;
        if ("out".equals(codeString))
          return OperationParameterUse.OUT;
        throw new IllegalArgumentException("Unknown OperationParameterUse code '"+codeString+"'");
        }
    public String toCode(OperationParameterUse code) {
      if (code == OperationParameterUse.IN)
        return "in";
      if (code == OperationParameterUse.OUT)
        return "out";
      return "?";
      }
    }

    @Block()
    public static class OperationDefinitionContactComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The name of an individual to contact regarding the operation definition.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Name of a individual to contact", formalDefinition="The name of an individual to contact regarding the operation definition." )
        protected StringType name;

        /**
         * Contact details for individual (if a name was provided) or the publisher.
         */
        @Child(name = "telecom", type = {ContactPoint.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Contact details for individual or publisher", formalDefinition="Contact details for individual (if a name was provided) or the publisher." )
        protected List<ContactPoint> telecom;

        private static final long serialVersionUID = -1179697803L;

    /*
     * Constructor
     */
      public OperationDefinitionContactComponent() {
        super();
      }

        /**
         * @return {@link #name} (The name of an individual to contact regarding the operation definition.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OperationDefinitionContactComponent.name");
            else if (Configuration.doAutoCreate())
              this.name = new StringType(); // bb
          return this.name;
        }

        public boolean hasNameElement() { 
          return this.name != null && !this.name.isEmpty();
        }

        public boolean hasName() { 
          return this.name != null && !this.name.isEmpty();
        }

        /**
         * @param value {@link #name} (The name of an individual to contact regarding the operation definition.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public OperationDefinitionContactComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return The name of an individual to contact regarding the operation definition.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value The name of an individual to contact regarding the operation definition.
         */
        public OperationDefinitionContactComponent setName(String value) { 
          if (Utilities.noString(value))
            this.name = null;
          else {
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #telecom} (Contact details for individual (if a name was provided) or the publisher.)
         */
        public List<ContactPoint> getTelecom() { 
          if (this.telecom == null)
            this.telecom = new ArrayList<ContactPoint>();
          return this.telecom;
        }

        public boolean hasTelecom() { 
          if (this.telecom == null)
            return false;
          for (ContactPoint item : this.telecom)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #telecom} (Contact details for individual (if a name was provided) or the publisher.)
         */
    // syntactic sugar
        public ContactPoint addTelecom() { //3
          ContactPoint t = new ContactPoint();
          if (this.telecom == null)
            this.telecom = new ArrayList<ContactPoint>();
          this.telecom.add(t);
          return t;
        }

    // syntactic sugar
        public OperationDefinitionContactComponent addTelecom(ContactPoint t) { //3
          if (t == null)
            return this;
          if (this.telecom == null)
            this.telecom = new ArrayList<ContactPoint>();
          this.telecom.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "string", "The name of an individual to contact regarding the operation definition.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("telecom", "ContactPoint", "Contact details for individual (if a name was provided) or the publisher.", 0, java.lang.Integer.MAX_VALUE, telecom));
        }

      public OperationDefinitionContactComponent copy() {
        OperationDefinitionContactComponent dst = new OperationDefinitionContactComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        if (telecom != null) {
          dst.telecom = new ArrayList<ContactPoint>();
          for (ContactPoint i : telecom)
            dst.telecom.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof OperationDefinitionContactComponent))
          return false;
        OperationDefinitionContactComponent o = (OperationDefinitionContactComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(telecom, o.telecom, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof OperationDefinitionContactComponent))
          return false;
        OperationDefinitionContactComponent o = (OperationDefinitionContactComponent) other;
        return compareValues(name, o.name, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (telecom == null || telecom.isEmpty())
          ;
      }

  }

    @Block()
    public static class OperationDefinitionParameterComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The name of used to identify the parameter.
         */
        @Child(name = "name", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Name in Parameters.parameter.name or in URL", formalDefinition="The name of used to identify the parameter." )
        protected CodeType name;

        /**
         * Whether this is an input or an output parameter.
         */
        @Child(name = "use", type = {CodeType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="in | out", formalDefinition="Whether this is an input or an output parameter." )
        protected Enumeration<OperationParameterUse> use;

        /**
         * The minimum number of times this parameter SHALL appear in the request or response.
         */
        @Child(name = "min", type = {IntegerType.class}, order=3, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Minimum Cardinality", formalDefinition="The minimum number of times this parameter SHALL appear in the request or response." )
        protected IntegerType min;

        /**
         * The maximum number of times this element is permitted to appear in the request or response.
         */
        @Child(name = "max", type = {StringType.class}, order=4, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Maximum Cardinality (a number or *)", formalDefinition="The maximum number of times this element is permitted to appear in the request or response." )
        protected StringType max;

        /**
         * Describes the meaning or use of this parameter.
         */
        @Child(name = "documentation", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Description of meaning/use", formalDefinition="Describes the meaning or use of this parameter." )
        protected StringType documentation;

        /**
         * The type for this parameter.
         */
        @Child(name = "type", type = {CodeType.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="What type this parameter has", formalDefinition="The type for this parameter." )
        protected CodeType type;

        /**
         * A profile the specifies the rules that this parameter must conform to.
         */
        @Child(name = "profile", type = {StructureDefinition.class}, order=7, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Profile on the type", formalDefinition="A profile the specifies the rules that this parameter must conform to." )
        protected Reference profile;

        /**
         * The actual object that is the target of the reference (A profile the specifies the rules that this parameter must conform to.)
         */
        protected StructureDefinition profileTarget;

        /**
         * Binds to a value set if this parameter is coded (code, Coding, CodeableConcept).
         */
        @Child(name = "binding", type = {}, order=8, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="ValueSet details if this is coded", formalDefinition="Binds to a value set if this parameter is coded (code, Coding, CodeableConcept)." )
        protected OperationDefinitionParameterBindingComponent binding;

        /**
         * The parts of a Tuple Parameter.
         */
        @Child(name = "part", type = {OperationDefinitionParameterComponent.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Parts of a Tuple Parameter", formalDefinition="The parts of a Tuple Parameter." )
        protected List<OperationDefinitionParameterComponent> part;

        private static final long serialVersionUID = -1514145741L;

    /*
     * Constructor
     */
      public OperationDefinitionParameterComponent() {
        super();
      }

    /*
     * Constructor
     */
      public OperationDefinitionParameterComponent(CodeType name, Enumeration<OperationParameterUse> use, IntegerType min, StringType max) {
        super();
        this.name = name;
        this.use = use;
        this.min = min;
        this.max = max;
      }

        /**
         * @return {@link #name} (The name of used to identify the parameter.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public CodeType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OperationDefinitionParameterComponent.name");
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
         * @param value {@link #name} (The name of used to identify the parameter.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public OperationDefinitionParameterComponent setNameElement(CodeType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return The name of used to identify the parameter.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value The name of used to identify the parameter.
         */
        public OperationDefinitionParameterComponent setName(String value) { 
            if (this.name == null)
              this.name = new CodeType();
            this.name.setValue(value);
          return this;
        }

        /**
         * @return {@link #use} (Whether this is an input or an output parameter.). This is the underlying object with id, value and extensions. The accessor "getUse" gives direct access to the value
         */
        public Enumeration<OperationParameterUse> getUseElement() { 
          if (this.use == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OperationDefinitionParameterComponent.use");
            else if (Configuration.doAutoCreate())
              this.use = new Enumeration<OperationParameterUse>(new OperationParameterUseEnumFactory()); // bb
          return this.use;
        }

        public boolean hasUseElement() { 
          return this.use != null && !this.use.isEmpty();
        }

        public boolean hasUse() { 
          return this.use != null && !this.use.isEmpty();
        }

        /**
         * @param value {@link #use} (Whether this is an input or an output parameter.). This is the underlying object with id, value and extensions. The accessor "getUse" gives direct access to the value
         */
        public OperationDefinitionParameterComponent setUseElement(Enumeration<OperationParameterUse> value) { 
          this.use = value;
          return this;
        }

        /**
         * @return Whether this is an input or an output parameter.
         */
        public OperationParameterUse getUse() { 
          return this.use == null ? null : this.use.getValue();
        }

        /**
         * @param value Whether this is an input or an output parameter.
         */
        public OperationDefinitionParameterComponent setUse(OperationParameterUse value) { 
            if (this.use == null)
              this.use = new Enumeration<OperationParameterUse>(new OperationParameterUseEnumFactory());
            this.use.setValue(value);
          return this;
        }

        /**
         * @return {@link #min} (The minimum number of times this parameter SHALL appear in the request or response.). This is the underlying object with id, value and extensions. The accessor "getMin" gives direct access to the value
         */
        public IntegerType getMinElement() { 
          if (this.min == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OperationDefinitionParameterComponent.min");
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
        public OperationDefinitionParameterComponent setMinElement(IntegerType value) { 
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
        public OperationDefinitionParameterComponent setMin(int value) { 
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
              throw new Error("Attempt to auto-create OperationDefinitionParameterComponent.max");
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
        public OperationDefinitionParameterComponent setMaxElement(StringType value) { 
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
        public OperationDefinitionParameterComponent setMax(String value) { 
            if (this.max == null)
              this.max = new StringType();
            this.max.setValue(value);
          return this;
        }

        /**
         * @return {@link #documentation} (Describes the meaning or use of this parameter.). This is the underlying object with id, value and extensions. The accessor "getDocumentation" gives direct access to the value
         */
        public StringType getDocumentationElement() { 
          if (this.documentation == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OperationDefinitionParameterComponent.documentation");
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
         * @param value {@link #documentation} (Describes the meaning or use of this parameter.). This is the underlying object with id, value and extensions. The accessor "getDocumentation" gives direct access to the value
         */
        public OperationDefinitionParameterComponent setDocumentationElement(StringType value) { 
          this.documentation = value;
          return this;
        }

        /**
         * @return Describes the meaning or use of this parameter.
         */
        public String getDocumentation() { 
          return this.documentation == null ? null : this.documentation.getValue();
        }

        /**
         * @param value Describes the meaning or use of this parameter.
         */
        public OperationDefinitionParameterComponent setDocumentation(String value) { 
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
         * @return {@link #type} (The type for this parameter.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public CodeType getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OperationDefinitionParameterComponent.type");
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
         * @param value {@link #type} (The type for this parameter.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public OperationDefinitionParameterComponent setTypeElement(CodeType value) { 
          this.type = value;
          return this;
        }

        /**
         * @return The type for this parameter.
         */
        public String getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value The type for this parameter.
         */
        public OperationDefinitionParameterComponent setType(String value) { 
          if (Utilities.noString(value))
            this.type = null;
          else {
            if (this.type == null)
              this.type = new CodeType();
            this.type.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #profile} (A profile the specifies the rules that this parameter must conform to.)
         */
        public Reference getProfile() { 
          if (this.profile == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OperationDefinitionParameterComponent.profile");
            else if (Configuration.doAutoCreate())
              this.profile = new Reference(); // cc
          return this.profile;
        }

        public boolean hasProfile() { 
          return this.profile != null && !this.profile.isEmpty();
        }

        /**
         * @param value {@link #profile} (A profile the specifies the rules that this parameter must conform to.)
         */
        public OperationDefinitionParameterComponent setProfile(Reference value) { 
          this.profile = value;
          return this;
        }

        /**
         * @return {@link #profile} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A profile the specifies the rules that this parameter must conform to.)
         */
        public StructureDefinition getProfileTarget() { 
          if (this.profileTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OperationDefinitionParameterComponent.profile");
            else if (Configuration.doAutoCreate())
              this.profileTarget = new StructureDefinition(); // aa
          return this.profileTarget;
        }

        /**
         * @param value {@link #profile} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A profile the specifies the rules that this parameter must conform to.)
         */
        public OperationDefinitionParameterComponent setProfileTarget(StructureDefinition value) { 
          this.profileTarget = value;
          return this;
        }

        /**
         * @return {@link #binding} (Binds to a value set if this parameter is coded (code, Coding, CodeableConcept).)
         */
        public OperationDefinitionParameterBindingComponent getBinding() { 
          if (this.binding == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OperationDefinitionParameterComponent.binding");
            else if (Configuration.doAutoCreate())
              this.binding = new OperationDefinitionParameterBindingComponent(); // cc
          return this.binding;
        }

        public boolean hasBinding() { 
          return this.binding != null && !this.binding.isEmpty();
        }

        /**
         * @param value {@link #binding} (Binds to a value set if this parameter is coded (code, Coding, CodeableConcept).)
         */
        public OperationDefinitionParameterComponent setBinding(OperationDefinitionParameterBindingComponent value) { 
          this.binding = value;
          return this;
        }

        /**
         * @return {@link #part} (The parts of a Tuple Parameter.)
         */
        public List<OperationDefinitionParameterComponent> getPart() { 
          if (this.part == null)
            this.part = new ArrayList<OperationDefinitionParameterComponent>();
          return this.part;
        }

        public boolean hasPart() { 
          if (this.part == null)
            return false;
          for (OperationDefinitionParameterComponent item : this.part)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #part} (The parts of a Tuple Parameter.)
         */
    // syntactic sugar
        public OperationDefinitionParameterComponent addPart() { //3
          OperationDefinitionParameterComponent t = new OperationDefinitionParameterComponent();
          if (this.part == null)
            this.part = new ArrayList<OperationDefinitionParameterComponent>();
          this.part.add(t);
          return t;
        }

    // syntactic sugar
        public OperationDefinitionParameterComponent addPart(OperationDefinitionParameterComponent t) { //3
          if (t == null)
            return this;
          if (this.part == null)
            this.part = new ArrayList<OperationDefinitionParameterComponent>();
          this.part.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "code", "The name of used to identify the parameter.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("use", "code", "Whether this is an input or an output parameter.", 0, java.lang.Integer.MAX_VALUE, use));
          childrenList.add(new Property("min", "integer", "The minimum number of times this parameter SHALL appear in the request or response.", 0, java.lang.Integer.MAX_VALUE, min));
          childrenList.add(new Property("max", "string", "The maximum number of times this element is permitted to appear in the request or response.", 0, java.lang.Integer.MAX_VALUE, max));
          childrenList.add(new Property("documentation", "string", "Describes the meaning or use of this parameter.", 0, java.lang.Integer.MAX_VALUE, documentation));
          childrenList.add(new Property("type", "code", "The type for this parameter.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("profile", "Reference(StructureDefinition)", "A profile the specifies the rules that this parameter must conform to.", 0, java.lang.Integer.MAX_VALUE, profile));
          childrenList.add(new Property("binding", "", "Binds to a value set if this parameter is coded (code, Coding, CodeableConcept).", 0, java.lang.Integer.MAX_VALUE, binding));
          childrenList.add(new Property("part", "@OperationDefinition.parameter", "The parts of a Tuple Parameter.", 0, java.lang.Integer.MAX_VALUE, part));
        }

      public OperationDefinitionParameterComponent copy() {
        OperationDefinitionParameterComponent dst = new OperationDefinitionParameterComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.use = use == null ? null : use.copy();
        dst.min = min == null ? null : min.copy();
        dst.max = max == null ? null : max.copy();
        dst.documentation = documentation == null ? null : documentation.copy();
        dst.type = type == null ? null : type.copy();
        dst.profile = profile == null ? null : profile.copy();
        dst.binding = binding == null ? null : binding.copy();
        if (part != null) {
          dst.part = new ArrayList<OperationDefinitionParameterComponent>();
          for (OperationDefinitionParameterComponent i : part)
            dst.part.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof OperationDefinitionParameterComponent))
          return false;
        OperationDefinitionParameterComponent o = (OperationDefinitionParameterComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(use, o.use, true) && compareDeep(min, o.min, true)
           && compareDeep(max, o.max, true) && compareDeep(documentation, o.documentation, true) && compareDeep(type, o.type, true)
           && compareDeep(profile, o.profile, true) && compareDeep(binding, o.binding, true) && compareDeep(part, o.part, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof OperationDefinitionParameterComponent))
          return false;
        OperationDefinitionParameterComponent o = (OperationDefinitionParameterComponent) other;
        return compareValues(name, o.name, true) && compareValues(use, o.use, true) && compareValues(min, o.min, true)
           && compareValues(max, o.max, true) && compareValues(documentation, o.documentation, true) && compareValues(type, o.type, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (use == null || use.isEmpty())
           && (min == null || min.isEmpty()) && (max == null || max.isEmpty()) && (documentation == null || documentation.isEmpty())
           && (type == null || type.isEmpty()) && (profile == null || profile.isEmpty()) && (binding == null || binding.isEmpty())
           && (part == null || part.isEmpty());
      }

  }

    @Block()
    public static class OperationDefinitionParameterBindingComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Indicates the degree of conformance expectations associated with this binding - that is, the degree to which the provided value set must be adhered to in the instances.
         */
        @Child(name = "strength", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="required | extensible | preferred | example", formalDefinition="Indicates the degree of conformance expectations associated with this binding - that is, the degree to which the provided value set must be adhered to in the instances." )
        protected Enumeration<BindingStrength> strength;

        /**
         * Points to the value set or external definition (e.g. implicit value set) that identifies the set of codes to be used.
         */
        @Child(name = "valueSet", type = {UriType.class, ValueSet.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Source of value set", formalDefinition="Points to the value set or external definition (e.g. implicit value set) that identifies the set of codes to be used." )
        protected Type valueSet;

        private static final long serialVersionUID = 857140521L;

    /*
     * Constructor
     */
      public OperationDefinitionParameterBindingComponent() {
        super();
      }

    /*
     * Constructor
     */
      public OperationDefinitionParameterBindingComponent(Enumeration<BindingStrength> strength, Type valueSet) {
        super();
        this.strength = strength;
        this.valueSet = valueSet;
      }

        /**
         * @return {@link #strength} (Indicates the degree of conformance expectations associated with this binding - that is, the degree to which the provided value set must be adhered to in the instances.). This is the underlying object with id, value and extensions. The accessor "getStrength" gives direct access to the value
         */
        public Enumeration<BindingStrength> getStrengthElement() { 
          if (this.strength == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OperationDefinitionParameterBindingComponent.strength");
            else if (Configuration.doAutoCreate())
              this.strength = new Enumeration<BindingStrength>(new BindingStrengthEnumFactory()); // bb
          return this.strength;
        }

        public boolean hasStrengthElement() { 
          return this.strength != null && !this.strength.isEmpty();
        }

        public boolean hasStrength() { 
          return this.strength != null && !this.strength.isEmpty();
        }

        /**
         * @param value {@link #strength} (Indicates the degree of conformance expectations associated with this binding - that is, the degree to which the provided value set must be adhered to in the instances.). This is the underlying object with id, value and extensions. The accessor "getStrength" gives direct access to the value
         */
        public OperationDefinitionParameterBindingComponent setStrengthElement(Enumeration<BindingStrength> value) { 
          this.strength = value;
          return this;
        }

        /**
         * @return Indicates the degree of conformance expectations associated with this binding - that is, the degree to which the provided value set must be adhered to in the instances.
         */
        public BindingStrength getStrength() { 
          return this.strength == null ? null : this.strength.getValue();
        }

        /**
         * @param value Indicates the degree of conformance expectations associated with this binding - that is, the degree to which the provided value set must be adhered to in the instances.
         */
        public OperationDefinitionParameterBindingComponent setStrength(BindingStrength value) { 
            if (this.strength == null)
              this.strength = new Enumeration<BindingStrength>(new BindingStrengthEnumFactory());
            this.strength.setValue(value);
          return this;
        }

        /**
         * @return {@link #valueSet} (Points to the value set or external definition (e.g. implicit value set) that identifies the set of codes to be used.)
         */
        public Type getValueSet() { 
          return this.valueSet;
        }

        /**
         * @return {@link #valueSet} (Points to the value set or external definition (e.g. implicit value set) that identifies the set of codes to be used.)
         */
        public UriType getValueSetUriType() throws Exception { 
          if (!(this.valueSet instanceof UriType))
            throw new Exception("Type mismatch: the type UriType was expected, but "+this.valueSet.getClass().getName()+" was encountered");
          return (UriType) this.valueSet;
        }

        public boolean hasValueSetUriType() throws Exception { 
          return this.valueSet instanceof UriType;
        }

        /**
         * @return {@link #valueSet} (Points to the value set or external definition (e.g. implicit value set) that identifies the set of codes to be used.)
         */
        public Reference getValueSetReference() throws Exception { 
          if (!(this.valueSet instanceof Reference))
            throw new Exception("Type mismatch: the type Reference was expected, but "+this.valueSet.getClass().getName()+" was encountered");
          return (Reference) this.valueSet;
        }

        public boolean hasValueSetReference() throws Exception { 
          return this.valueSet instanceof Reference;
        }

        public boolean hasValueSet() { 
          return this.valueSet != null && !this.valueSet.isEmpty();
        }

        /**
         * @param value {@link #valueSet} (Points to the value set or external definition (e.g. implicit value set) that identifies the set of codes to be used.)
         */
        public OperationDefinitionParameterBindingComponent setValueSet(Type value) { 
          this.valueSet = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("strength", "code", "Indicates the degree of conformance expectations associated with this binding - that is, the degree to which the provided value set must be adhered to in the instances.", 0, java.lang.Integer.MAX_VALUE, strength));
          childrenList.add(new Property("valueSet[x]", "uri|Reference(ValueSet)", "Points to the value set or external definition (e.g. implicit value set) that identifies the set of codes to be used.", 0, java.lang.Integer.MAX_VALUE, valueSet));
        }

      public OperationDefinitionParameterBindingComponent copy() {
        OperationDefinitionParameterBindingComponent dst = new OperationDefinitionParameterBindingComponent();
        copyValues(dst);
        dst.strength = strength == null ? null : strength.copy();
        dst.valueSet = valueSet == null ? null : valueSet.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof OperationDefinitionParameterBindingComponent))
          return false;
        OperationDefinitionParameterBindingComponent o = (OperationDefinitionParameterBindingComponent) other;
        return compareDeep(strength, o.strength, true) && compareDeep(valueSet, o.valueSet, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof OperationDefinitionParameterBindingComponent))
          return false;
        OperationDefinitionParameterBindingComponent o = (OperationDefinitionParameterBindingComponent) other;
        return compareValues(strength, o.strength, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (strength == null || strength.isEmpty()) && (valueSet == null || valueSet.isEmpty())
          ;
      }

  }

    /**
     * An absolute URL that is used to identify this operation definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this operation definition is (or will be) published.
     */
    @Child(name = "url", type = {UriType.class}, order=0, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Logical URL to reference this operation definition", formalDefinition="An absolute URL that is used to identify this operation definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this operation definition is (or will be) published." )
    protected UriType url;

    /**
     * The identifier that is used to identify this version of the profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp.
     */
    @Child(name = "version", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Logical id for this version of the operation definition", formalDefinition="The identifier that is used to identify this version of the profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp." )
    protected StringType version;

    /**
     * A free text natural language name identifying the operation.
     */
    @Child(name = "name", type = {StringType.class}, order=2, min=1, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Informal name for this operation", formalDefinition="A free text natural language name identifying the operation." )
    protected StringType name;

    /**
     * The status of the profile.
     */
    @Child(name = "status", type = {CodeType.class}, order=3, min=1, max=1, modifier=true, summary=false)
    @Description(shortDefinition="draft | active | retired", formalDefinition="The status of the profile." )
    protected Enumeration<ConformanceResourceStatus> status;

    /**
     * Whether this is an operation or a named query.
     */
    @Child(name = "kind", type = {CodeType.class}, order=4, min=1, max=1, modifier=false, summary=false)
    @Description(shortDefinition="operation | query", formalDefinition="Whether this is an operation or a named query." )
    protected Enumeration<OperationKind> kind;

    /**
     * This profile was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    @Child(name = "experimental", type = {BooleanType.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="If for testing purposes, not real usage", formalDefinition="This profile was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage." )
    protected BooleanType experimental;

    /**
     * The name of the individual or organization that published the operation definition.
     */
    @Child(name = "publisher", type = {StringType.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Name of the publisher (Organization or individual)", formalDefinition="The name of the individual or organization that published the operation definition." )
    protected StringType publisher;

    /**
     * Contacts to assist a user in finding and communicating with the publisher.
     */
    @Child(name = "contact", type = {}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Contact details of the publisher", formalDefinition="Contacts to assist a user in finding and communicating with the publisher." )
    protected List<OperationDefinitionContactComponent> contact;

    /**
     * The date this version of the operation definition was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the Operation Definition changes.
     */
    @Child(name = "date", type = {DateTimeType.class}, order=8, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Date for this version of the operation definition", formalDefinition="The date this version of the operation definition was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the Operation Definition changes." )
    protected DateTimeType date;

    /**
     * A free text natural language description of the profile and its use.
     */
    @Child(name = "description", type = {StringType.class}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Natural language description of the operation", formalDefinition="A free text natural language description of the profile and its use." )
    protected StringType description;

    /**
     * Explains why this operation definition is needed and why it's been constrained as it has.
     */
    @Child(name = "requirements", type = {StringType.class}, order=10, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Why is this needed?", formalDefinition="Explains why this operation definition is needed and why it's been constrained as it has." )
    protected StringType requirements;

    /**
     * Operations that are idempotent (see [HTTP specification definition of idempotent](http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html)) may be invoked by performing an HTTP GET operation instead of a POST.
     */
    @Child(name = "idempotent", type = {BooleanType.class}, order=11, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Whether content is unchanged by operation", formalDefinition="Operations that are idempotent (see [HTTP specification definition of idempotent](http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html)) may be invoked by performing an HTTP GET operation instead of a POST." )
    protected BooleanType idempotent;

    /**
     * The name used to invoke the operation.
     */
    @Child(name = "code", type = {CodeType.class}, order=12, min=1, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Name used to invoke the operation", formalDefinition="The name used to invoke the operation." )
    protected CodeType code;

    /**
     * Additional information about how to use this operation or named query.
     */
    @Child(name = "notes", type = {StringType.class}, order=13, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Additional information about use", formalDefinition="Additional information about how to use this operation or named query." )
    protected StringType notes;

    /**
     * Indicates that this operation definition is a constraining profile on the base.
     */
    @Child(name = "base", type = {OperationDefinition.class}, order=14, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Marks this as a profile of the base", formalDefinition="Indicates that this operation definition is a constraining profile on the base." )
    protected Reference base;

    /**
     * The actual object that is the target of the reference (Indicates that this operation definition is a constraining profile on the base.)
     */
    protected OperationDefinition baseTarget;

    /**
     * Indicates whether this operation or named query can be invoked at the system level (e.g. without needing to choose a resource type for the context).
     */
    @Child(name = "system", type = {BooleanType.class}, order=15, min=1, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Invoke at the system level?", formalDefinition="Indicates whether this operation or named query can be invoked at the system level (e.g. without needing to choose a resource type for the context)." )
    protected BooleanType system;

    /**
     * Indicates whether this operation or named query can be invoked at the resource type level for any given resource type level (e.g. without needing to choose a resource type for the context).
     */
    @Child(name = "type", type = {CodeType.class}, order=16, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Invoke at resource level for these type", formalDefinition="Indicates whether this operation or named query can be invoked at the resource type level for any given resource type level (e.g. without needing to choose a resource type for the context)." )
    protected List<CodeType> type;

    /**
     * Indicates whether this operation can be invoked on a particular instance of one of the given types.
     */
    @Child(name = "instance", type = {BooleanType.class}, order=17, min=1, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Invoke on an instance?", formalDefinition="Indicates whether this operation can be invoked on a particular instance of one of the given types." )
    protected BooleanType instance;

    /**
     * The parameters for the operation/query.
     */
    @Child(name = "parameter", type = {}, order=18, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Parameters for the operation/query", formalDefinition="The parameters for the operation/query." )
    protected List<OperationDefinitionParameterComponent> parameter;

    private static final long serialVersionUID = 148203484L;

  /*
   * Constructor
   */
    public OperationDefinition() {
      super();
    }

  /*
   * Constructor
   */
    public OperationDefinition(StringType name, Enumeration<ConformanceResourceStatus> status, Enumeration<OperationKind> kind, CodeType code, BooleanType system, BooleanType instance) {
      super();
      this.name = name;
      this.status = status;
      this.kind = kind;
      this.code = code;
      this.system = system;
      this.instance = instance;
    }

    /**
     * @return {@link #url} (An absolute URL that is used to identify this operation definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this operation definition is (or will be) published.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public UriType getUrlElement() { 
      if (this.url == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OperationDefinition.url");
        else if (Configuration.doAutoCreate())
          this.url = new UriType(); // bb
      return this.url;
    }

    public boolean hasUrlElement() { 
      return this.url != null && !this.url.isEmpty();
    }

    public boolean hasUrl() { 
      return this.url != null && !this.url.isEmpty();
    }

    /**
     * @param value {@link #url} (An absolute URL that is used to identify this operation definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this operation definition is (or will be) published.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public OperationDefinition setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return An absolute URL that is used to identify this operation definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this operation definition is (or will be) published.
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value An absolute URL that is used to identify this operation definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this operation definition is (or will be) published.
     */
    public OperationDefinition setUrl(String value) { 
      if (Utilities.noString(value))
        this.url = null;
      else {
        if (this.url == null)
          this.url = new UriType();
        this.url.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #version} (The identifier that is used to identify this version of the profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public StringType getVersionElement() { 
      if (this.version == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OperationDefinition.version");
        else if (Configuration.doAutoCreate())
          this.version = new StringType(); // bb
      return this.version;
    }

    public boolean hasVersionElement() { 
      return this.version != null && !this.version.isEmpty();
    }

    public boolean hasVersion() { 
      return this.version != null && !this.version.isEmpty();
    }

    /**
     * @param value {@link #version} (The identifier that is used to identify this version of the profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public OperationDefinition setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return The identifier that is used to identify this version of the profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value The identifier that is used to identify this version of the profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp.
     */
    public OperationDefinition setVersion(String value) { 
      if (Utilities.noString(value))
        this.version = null;
      else {
        if (this.version == null)
          this.version = new StringType();
        this.version.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #name} (A free text natural language name identifying the operation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OperationDefinition.name");
        else if (Configuration.doAutoCreate())
          this.name = new StringType(); // bb
      return this.name;
    }

    public boolean hasNameElement() { 
      return this.name != null && !this.name.isEmpty();
    }

    public boolean hasName() { 
      return this.name != null && !this.name.isEmpty();
    }

    /**
     * @param value {@link #name} (A free text natural language name identifying the operation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public OperationDefinition setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return A free text natural language name identifying the operation.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value A free text natural language name identifying the operation.
     */
    public OperationDefinition setName(String value) { 
        if (this.name == null)
          this.name = new StringType();
        this.name.setValue(value);
      return this;
    }

    /**
     * @return {@link #status} (The status of the profile.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<ConformanceResourceStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OperationDefinition.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<ConformanceResourceStatus>(new ConformanceResourceStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of the profile.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public OperationDefinition setStatusElement(Enumeration<ConformanceResourceStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the profile.
     */
    public ConformanceResourceStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the profile.
     */
    public OperationDefinition setStatus(ConformanceResourceStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<ConformanceResourceStatus>(new ConformanceResourceStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #kind} (Whether this is an operation or a named query.). This is the underlying object with id, value and extensions. The accessor "getKind" gives direct access to the value
     */
    public Enumeration<OperationKind> getKindElement() { 
      if (this.kind == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OperationDefinition.kind");
        else if (Configuration.doAutoCreate())
          this.kind = new Enumeration<OperationKind>(new OperationKindEnumFactory()); // bb
      return this.kind;
    }

    public boolean hasKindElement() { 
      return this.kind != null && !this.kind.isEmpty();
    }

    public boolean hasKind() { 
      return this.kind != null && !this.kind.isEmpty();
    }

    /**
     * @param value {@link #kind} (Whether this is an operation or a named query.). This is the underlying object with id, value and extensions. The accessor "getKind" gives direct access to the value
     */
    public OperationDefinition setKindElement(Enumeration<OperationKind> value) { 
      this.kind = value;
      return this;
    }

    /**
     * @return Whether this is an operation or a named query.
     */
    public OperationKind getKind() { 
      return this.kind == null ? null : this.kind.getValue();
    }

    /**
     * @param value Whether this is an operation or a named query.
     */
    public OperationDefinition setKind(OperationKind value) { 
        if (this.kind == null)
          this.kind = new Enumeration<OperationKind>(new OperationKindEnumFactory());
        this.kind.setValue(value);
      return this;
    }

    /**
     * @return {@link #experimental} (This profile was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public BooleanType getExperimentalElement() { 
      if (this.experimental == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OperationDefinition.experimental");
        else if (Configuration.doAutoCreate())
          this.experimental = new BooleanType(); // bb
      return this.experimental;
    }

    public boolean hasExperimentalElement() { 
      return this.experimental != null && !this.experimental.isEmpty();
    }

    public boolean hasExperimental() { 
      return this.experimental != null && !this.experimental.isEmpty();
    }

    /**
     * @param value {@link #experimental} (This profile was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public OperationDefinition setExperimentalElement(BooleanType value) { 
      this.experimental = value;
      return this;
    }

    /**
     * @return This profile was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public boolean getExperimental() { 
      return this.experimental == null || this.experimental.isEmpty() ? false : this.experimental.getValue();
    }

    /**
     * @param value This profile was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public OperationDefinition setExperimental(boolean value) { 
        if (this.experimental == null)
          this.experimental = new BooleanType();
        this.experimental.setValue(value);
      return this;
    }

    /**
     * @return {@link #publisher} (The name of the individual or organization that published the operation definition.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public StringType getPublisherElement() { 
      if (this.publisher == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OperationDefinition.publisher");
        else if (Configuration.doAutoCreate())
          this.publisher = new StringType(); // bb
      return this.publisher;
    }

    public boolean hasPublisherElement() { 
      return this.publisher != null && !this.publisher.isEmpty();
    }

    public boolean hasPublisher() { 
      return this.publisher != null && !this.publisher.isEmpty();
    }

    /**
     * @param value {@link #publisher} (The name of the individual or organization that published the operation definition.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public OperationDefinition setPublisherElement(StringType value) { 
      this.publisher = value;
      return this;
    }

    /**
     * @return The name of the individual or organization that published the operation definition.
     */
    public String getPublisher() { 
      return this.publisher == null ? null : this.publisher.getValue();
    }

    /**
     * @param value The name of the individual or organization that published the operation definition.
     */
    public OperationDefinition setPublisher(String value) { 
      if (Utilities.noString(value))
        this.publisher = null;
      else {
        if (this.publisher == null)
          this.publisher = new StringType();
        this.publisher.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #contact} (Contacts to assist a user in finding and communicating with the publisher.)
     */
    public List<OperationDefinitionContactComponent> getContact() { 
      if (this.contact == null)
        this.contact = new ArrayList<OperationDefinitionContactComponent>();
      return this.contact;
    }

    public boolean hasContact() { 
      if (this.contact == null)
        return false;
      for (OperationDefinitionContactComponent item : this.contact)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #contact} (Contacts to assist a user in finding and communicating with the publisher.)
     */
    // syntactic sugar
    public OperationDefinitionContactComponent addContact() { //3
      OperationDefinitionContactComponent t = new OperationDefinitionContactComponent();
      if (this.contact == null)
        this.contact = new ArrayList<OperationDefinitionContactComponent>();
      this.contact.add(t);
      return t;
    }

    // syntactic sugar
    public OperationDefinition addContact(OperationDefinitionContactComponent t) { //3
      if (t == null)
        return this;
      if (this.contact == null)
        this.contact = new ArrayList<OperationDefinitionContactComponent>();
      this.contact.add(t);
      return this;
    }

    /**
     * @return {@link #date} (The date this version of the operation definition was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the Operation Definition changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OperationDefinition.date");
        else if (Configuration.doAutoCreate())
          this.date = new DateTimeType(); // bb
      return this.date;
    }

    public boolean hasDateElement() { 
      return this.date != null && !this.date.isEmpty();
    }

    public boolean hasDate() { 
      return this.date != null && !this.date.isEmpty();
    }

    /**
     * @param value {@link #date} (The date this version of the operation definition was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the Operation Definition changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public OperationDefinition setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date this version of the operation definition was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the Operation Definition changes.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date this version of the operation definition was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the Operation Definition changes.
     */
    public OperationDefinition setDate(Date value) { 
      if (value == null)
        this.date = null;
      else {
        if (this.date == null)
          this.date = new DateTimeType();
        this.date.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #description} (A free text natural language description of the profile and its use.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OperationDefinition.description");
        else if (Configuration.doAutoCreate())
          this.description = new StringType(); // bb
      return this.description;
    }

    public boolean hasDescriptionElement() { 
      return this.description != null && !this.description.isEmpty();
    }

    public boolean hasDescription() { 
      return this.description != null && !this.description.isEmpty();
    }

    /**
     * @param value {@link #description} (A free text natural language description of the profile and its use.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public OperationDefinition setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A free text natural language description of the profile and its use.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A free text natural language description of the profile and its use.
     */
    public OperationDefinition setDescription(String value) { 
      if (Utilities.noString(value))
        this.description = null;
      else {
        if (this.description == null)
          this.description = new StringType();
        this.description.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #requirements} (Explains why this operation definition is needed and why it's been constrained as it has.). This is the underlying object with id, value and extensions. The accessor "getRequirements" gives direct access to the value
     */
    public StringType getRequirementsElement() { 
      if (this.requirements == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OperationDefinition.requirements");
        else if (Configuration.doAutoCreate())
          this.requirements = new StringType(); // bb
      return this.requirements;
    }

    public boolean hasRequirementsElement() { 
      return this.requirements != null && !this.requirements.isEmpty();
    }

    public boolean hasRequirements() { 
      return this.requirements != null && !this.requirements.isEmpty();
    }

    /**
     * @param value {@link #requirements} (Explains why this operation definition is needed and why it's been constrained as it has.). This is the underlying object with id, value and extensions. The accessor "getRequirements" gives direct access to the value
     */
    public OperationDefinition setRequirementsElement(StringType value) { 
      this.requirements = value;
      return this;
    }

    /**
     * @return Explains why this operation definition is needed and why it's been constrained as it has.
     */
    public String getRequirements() { 
      return this.requirements == null ? null : this.requirements.getValue();
    }

    /**
     * @param value Explains why this operation definition is needed and why it's been constrained as it has.
     */
    public OperationDefinition setRequirements(String value) { 
      if (Utilities.noString(value))
        this.requirements = null;
      else {
        if (this.requirements == null)
          this.requirements = new StringType();
        this.requirements.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #idempotent} (Operations that are idempotent (see [HTTP specification definition of idempotent](http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html)) may be invoked by performing an HTTP GET operation instead of a POST.). This is the underlying object with id, value and extensions. The accessor "getIdempotent" gives direct access to the value
     */
    public BooleanType getIdempotentElement() { 
      if (this.idempotent == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OperationDefinition.idempotent");
        else if (Configuration.doAutoCreate())
          this.idempotent = new BooleanType(); // bb
      return this.idempotent;
    }

    public boolean hasIdempotentElement() { 
      return this.idempotent != null && !this.idempotent.isEmpty();
    }

    public boolean hasIdempotent() { 
      return this.idempotent != null && !this.idempotent.isEmpty();
    }

    /**
     * @param value {@link #idempotent} (Operations that are idempotent (see [HTTP specification definition of idempotent](http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html)) may be invoked by performing an HTTP GET operation instead of a POST.). This is the underlying object with id, value and extensions. The accessor "getIdempotent" gives direct access to the value
     */
    public OperationDefinition setIdempotentElement(BooleanType value) { 
      this.idempotent = value;
      return this;
    }

    /**
     * @return Operations that are idempotent (see [HTTP specification definition of idempotent](http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html)) may be invoked by performing an HTTP GET operation instead of a POST.
     */
    public boolean getIdempotent() { 
      return this.idempotent == null || this.idempotent.isEmpty() ? false : this.idempotent.getValue();
    }

    /**
     * @param value Operations that are idempotent (see [HTTP specification definition of idempotent](http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html)) may be invoked by performing an HTTP GET operation instead of a POST.
     */
    public OperationDefinition setIdempotent(boolean value) { 
        if (this.idempotent == null)
          this.idempotent = new BooleanType();
        this.idempotent.setValue(value);
      return this;
    }

    /**
     * @return {@link #code} (The name used to invoke the operation.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
     */
    public CodeType getCodeElement() { 
      if (this.code == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OperationDefinition.code");
        else if (Configuration.doAutoCreate())
          this.code = new CodeType(); // bb
      return this.code;
    }

    public boolean hasCodeElement() { 
      return this.code != null && !this.code.isEmpty();
    }

    public boolean hasCode() { 
      return this.code != null && !this.code.isEmpty();
    }

    /**
     * @param value {@link #code} (The name used to invoke the operation.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
     */
    public OperationDefinition setCodeElement(CodeType value) { 
      this.code = value;
      return this;
    }

    /**
     * @return The name used to invoke the operation.
     */
    public String getCode() { 
      return this.code == null ? null : this.code.getValue();
    }

    /**
     * @param value The name used to invoke the operation.
     */
    public OperationDefinition setCode(String value) { 
        if (this.code == null)
          this.code = new CodeType();
        this.code.setValue(value);
      return this;
    }

    /**
     * @return {@link #notes} (Additional information about how to use this operation or named query.). This is the underlying object with id, value and extensions. The accessor "getNotes" gives direct access to the value
     */
    public StringType getNotesElement() { 
      if (this.notes == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OperationDefinition.notes");
        else if (Configuration.doAutoCreate())
          this.notes = new StringType(); // bb
      return this.notes;
    }

    public boolean hasNotesElement() { 
      return this.notes != null && !this.notes.isEmpty();
    }

    public boolean hasNotes() { 
      return this.notes != null && !this.notes.isEmpty();
    }

    /**
     * @param value {@link #notes} (Additional information about how to use this operation or named query.). This is the underlying object with id, value and extensions. The accessor "getNotes" gives direct access to the value
     */
    public OperationDefinition setNotesElement(StringType value) { 
      this.notes = value;
      return this;
    }

    /**
     * @return Additional information about how to use this operation or named query.
     */
    public String getNotes() { 
      return this.notes == null ? null : this.notes.getValue();
    }

    /**
     * @param value Additional information about how to use this operation or named query.
     */
    public OperationDefinition setNotes(String value) { 
      if (Utilities.noString(value))
        this.notes = null;
      else {
        if (this.notes == null)
          this.notes = new StringType();
        this.notes.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #base} (Indicates that this operation definition is a constraining profile on the base.)
     */
    public Reference getBase() { 
      if (this.base == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OperationDefinition.base");
        else if (Configuration.doAutoCreate())
          this.base = new Reference(); // cc
      return this.base;
    }

    public boolean hasBase() { 
      return this.base != null && !this.base.isEmpty();
    }

    /**
     * @param value {@link #base} (Indicates that this operation definition is a constraining profile on the base.)
     */
    public OperationDefinition setBase(Reference value) { 
      this.base = value;
      return this;
    }

    /**
     * @return {@link #base} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Indicates that this operation definition is a constraining profile on the base.)
     */
    public OperationDefinition getBaseTarget() { 
      if (this.baseTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OperationDefinition.base");
        else if (Configuration.doAutoCreate())
          this.baseTarget = new OperationDefinition(); // aa
      return this.baseTarget;
    }

    /**
     * @param value {@link #base} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Indicates that this operation definition is a constraining profile on the base.)
     */
    public OperationDefinition setBaseTarget(OperationDefinition value) { 
      this.baseTarget = value;
      return this;
    }

    /**
     * @return {@link #system} (Indicates whether this operation or named query can be invoked at the system level (e.g. without needing to choose a resource type for the context).). This is the underlying object with id, value and extensions. The accessor "getSystem" gives direct access to the value
     */
    public BooleanType getSystemElement() { 
      if (this.system == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OperationDefinition.system");
        else if (Configuration.doAutoCreate())
          this.system = new BooleanType(); // bb
      return this.system;
    }

    public boolean hasSystemElement() { 
      return this.system != null && !this.system.isEmpty();
    }

    public boolean hasSystem() { 
      return this.system != null && !this.system.isEmpty();
    }

    /**
     * @param value {@link #system} (Indicates whether this operation or named query can be invoked at the system level (e.g. without needing to choose a resource type for the context).). This is the underlying object with id, value and extensions. The accessor "getSystem" gives direct access to the value
     */
    public OperationDefinition setSystemElement(BooleanType value) { 
      this.system = value;
      return this;
    }

    /**
     * @return Indicates whether this operation or named query can be invoked at the system level (e.g. without needing to choose a resource type for the context).
     */
    public boolean getSystem() { 
      return this.system == null || this.system.isEmpty() ? false : this.system.getValue();
    }

    /**
     * @param value Indicates whether this operation or named query can be invoked at the system level (e.g. without needing to choose a resource type for the context).
     */
    public OperationDefinition setSystem(boolean value) { 
        if (this.system == null)
          this.system = new BooleanType();
        this.system.setValue(value);
      return this;
    }

    /**
     * @return {@link #type} (Indicates whether this operation or named query can be invoked at the resource type level for any given resource type level (e.g. without needing to choose a resource type for the context).)
     */
    public List<CodeType> getType() { 
      if (this.type == null)
        this.type = new ArrayList<CodeType>();
      return this.type;
    }

    public boolean hasType() { 
      if (this.type == null)
        return false;
      for (CodeType item : this.type)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #type} (Indicates whether this operation or named query can be invoked at the resource type level for any given resource type level (e.g. without needing to choose a resource type for the context).)
     */
    // syntactic sugar
    public CodeType addTypeElement() {//2 
      CodeType t = new CodeType();
      if (this.type == null)
        this.type = new ArrayList<CodeType>();
      this.type.add(t);
      return t;
    }

    /**
     * @param value {@link #type} (Indicates whether this operation or named query can be invoked at the resource type level for any given resource type level (e.g. without needing to choose a resource type for the context).)
     */
    public OperationDefinition addType(String value) { //1
      CodeType t = new CodeType();
      t.setValue(value);
      if (this.type == null)
        this.type = new ArrayList<CodeType>();
      this.type.add(t);
      return this;
    }

    /**
     * @param value {@link #type} (Indicates whether this operation or named query can be invoked at the resource type level for any given resource type level (e.g. without needing to choose a resource type for the context).)
     */
    public boolean hasType(String value) { 
      if (this.type == null)
        return false;
      for (CodeType v : this.type)
        if (v.equals(value)) // code
          return true;
      return false;
    }

    /**
     * @return {@link #instance} (Indicates whether this operation can be invoked on a particular instance of one of the given types.). This is the underlying object with id, value and extensions. The accessor "getInstance" gives direct access to the value
     */
    public BooleanType getInstanceElement() { 
      if (this.instance == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OperationDefinition.instance");
        else if (Configuration.doAutoCreate())
          this.instance = new BooleanType(); // bb
      return this.instance;
    }

    public boolean hasInstanceElement() { 
      return this.instance != null && !this.instance.isEmpty();
    }

    public boolean hasInstance() { 
      return this.instance != null && !this.instance.isEmpty();
    }

    /**
     * @param value {@link #instance} (Indicates whether this operation can be invoked on a particular instance of one of the given types.). This is the underlying object with id, value and extensions. The accessor "getInstance" gives direct access to the value
     */
    public OperationDefinition setInstanceElement(BooleanType value) { 
      this.instance = value;
      return this;
    }

    /**
     * @return Indicates whether this operation can be invoked on a particular instance of one of the given types.
     */
    public boolean getInstance() { 
      return this.instance == null || this.instance.isEmpty() ? false : this.instance.getValue();
    }

    /**
     * @param value Indicates whether this operation can be invoked on a particular instance of one of the given types.
     */
    public OperationDefinition setInstance(boolean value) { 
        if (this.instance == null)
          this.instance = new BooleanType();
        this.instance.setValue(value);
      return this;
    }

    /**
     * @return {@link #parameter} (The parameters for the operation/query.)
     */
    public List<OperationDefinitionParameterComponent> getParameter() { 
      if (this.parameter == null)
        this.parameter = new ArrayList<OperationDefinitionParameterComponent>();
      return this.parameter;
    }

    public boolean hasParameter() { 
      if (this.parameter == null)
        return false;
      for (OperationDefinitionParameterComponent item : this.parameter)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #parameter} (The parameters for the operation/query.)
     */
    // syntactic sugar
    public OperationDefinitionParameterComponent addParameter() { //3
      OperationDefinitionParameterComponent t = new OperationDefinitionParameterComponent();
      if (this.parameter == null)
        this.parameter = new ArrayList<OperationDefinitionParameterComponent>();
      this.parameter.add(t);
      return t;
    }

    // syntactic sugar
    public OperationDefinition addParameter(OperationDefinitionParameterComponent t) { //3
      if (t == null)
        return this;
      if (this.parameter == null)
        this.parameter = new ArrayList<OperationDefinitionParameterComponent>();
      this.parameter.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("url", "uri", "An absolute URL that is used to identify this operation definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this operation definition is (or will be) published.", 0, java.lang.Integer.MAX_VALUE, url));
        childrenList.add(new Property("version", "string", "The identifier that is used to identify this version of the profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp.", 0, java.lang.Integer.MAX_VALUE, version));
        childrenList.add(new Property("name", "string", "A free text natural language name identifying the operation.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("status", "code", "The status of the profile.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("kind", "code", "Whether this is an operation or a named query.", 0, java.lang.Integer.MAX_VALUE, kind));
        childrenList.add(new Property("experimental", "boolean", "This profile was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.", 0, java.lang.Integer.MAX_VALUE, experimental));
        childrenList.add(new Property("publisher", "string", "The name of the individual or organization that published the operation definition.", 0, java.lang.Integer.MAX_VALUE, publisher));
        childrenList.add(new Property("contact", "", "Contacts to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact));
        childrenList.add(new Property("date", "dateTime", "The date this version of the operation definition was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the Operation Definition changes.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("description", "string", "A free text natural language description of the profile and its use.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("requirements", "string", "Explains why this operation definition is needed and why it's been constrained as it has.", 0, java.lang.Integer.MAX_VALUE, requirements));
        childrenList.add(new Property("idempotent", "boolean", "Operations that are idempotent (see [HTTP specification definition of idempotent](http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html)) may be invoked by performing an HTTP GET operation instead of a POST.", 0, java.lang.Integer.MAX_VALUE, idempotent));
        childrenList.add(new Property("code", "code", "The name used to invoke the operation.", 0, java.lang.Integer.MAX_VALUE, code));
        childrenList.add(new Property("notes", "string", "Additional information about how to use this operation or named query.", 0, java.lang.Integer.MAX_VALUE, notes));
        childrenList.add(new Property("base", "Reference(OperationDefinition)", "Indicates that this operation definition is a constraining profile on the base.", 0, java.lang.Integer.MAX_VALUE, base));
        childrenList.add(new Property("system", "boolean", "Indicates whether this operation or named query can be invoked at the system level (e.g. without needing to choose a resource type for the context).", 0, java.lang.Integer.MAX_VALUE, system));
        childrenList.add(new Property("type", "code", "Indicates whether this operation or named query can be invoked at the resource type level for any given resource type level (e.g. without needing to choose a resource type for the context).", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("instance", "boolean", "Indicates whether this operation can be invoked on a particular instance of one of the given types.", 0, java.lang.Integer.MAX_VALUE, instance));
        childrenList.add(new Property("parameter", "", "The parameters for the operation/query.", 0, java.lang.Integer.MAX_VALUE, parameter));
      }

      public OperationDefinition copy() {
        OperationDefinition dst = new OperationDefinition();
        copyValues(dst);
        dst.url = url == null ? null : url.copy();
        dst.version = version == null ? null : version.copy();
        dst.name = name == null ? null : name.copy();
        dst.status = status == null ? null : status.copy();
        dst.kind = kind == null ? null : kind.copy();
        dst.experimental = experimental == null ? null : experimental.copy();
        dst.publisher = publisher == null ? null : publisher.copy();
        if (contact != null) {
          dst.contact = new ArrayList<OperationDefinitionContactComponent>();
          for (OperationDefinitionContactComponent i : contact)
            dst.contact.add(i.copy());
        };
        dst.date = date == null ? null : date.copy();
        dst.description = description == null ? null : description.copy();
        dst.requirements = requirements == null ? null : requirements.copy();
        dst.idempotent = idempotent == null ? null : idempotent.copy();
        dst.code = code == null ? null : code.copy();
        dst.notes = notes == null ? null : notes.copy();
        dst.base = base == null ? null : base.copy();
        dst.system = system == null ? null : system.copy();
        if (type != null) {
          dst.type = new ArrayList<CodeType>();
          for (CodeType i : type)
            dst.type.add(i.copy());
        };
        dst.instance = instance == null ? null : instance.copy();
        if (parameter != null) {
          dst.parameter = new ArrayList<OperationDefinitionParameterComponent>();
          for (OperationDefinitionParameterComponent i : parameter)
            dst.parameter.add(i.copy());
        };
        return dst;
      }

      protected OperationDefinition typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof OperationDefinition))
          return false;
        OperationDefinition o = (OperationDefinition) other;
        return compareDeep(url, o.url, true) && compareDeep(version, o.version, true) && compareDeep(name, o.name, true)
           && compareDeep(status, o.status, true) && compareDeep(kind, o.kind, true) && compareDeep(experimental, o.experimental, true)
           && compareDeep(publisher, o.publisher, true) && compareDeep(contact, o.contact, true) && compareDeep(date, o.date, true)
           && compareDeep(description, o.description, true) && compareDeep(requirements, o.requirements, true)
           && compareDeep(idempotent, o.idempotent, true) && compareDeep(code, o.code, true) && compareDeep(notes, o.notes, true)
           && compareDeep(base, o.base, true) && compareDeep(system, o.system, true) && compareDeep(type, o.type, true)
           && compareDeep(instance, o.instance, true) && compareDeep(parameter, o.parameter, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof OperationDefinition))
          return false;
        OperationDefinition o = (OperationDefinition) other;
        return compareValues(url, o.url, true) && compareValues(version, o.version, true) && compareValues(name, o.name, true)
           && compareValues(status, o.status, true) && compareValues(kind, o.kind, true) && compareValues(experimental, o.experimental, true)
           && compareValues(publisher, o.publisher, true) && compareValues(date, o.date, true) && compareValues(description, o.description, true)
           && compareValues(requirements, o.requirements, true) && compareValues(idempotent, o.idempotent, true)
           && compareValues(code, o.code, true) && compareValues(notes, o.notes, true) && compareValues(system, o.system, true)
           && compareValues(type, o.type, true) && compareValues(instance, o.instance, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (url == null || url.isEmpty()) && (version == null || version.isEmpty())
           && (name == null || name.isEmpty()) && (status == null || status.isEmpty()) && (kind == null || kind.isEmpty())
           && (experimental == null || experimental.isEmpty()) && (publisher == null || publisher.isEmpty())
           && (contact == null || contact.isEmpty()) && (date == null || date.isEmpty()) && (description == null || description.isEmpty())
           && (requirements == null || requirements.isEmpty()) && (idempotent == null || idempotent.isEmpty())
           && (code == null || code.isEmpty()) && (notes == null || notes.isEmpty()) && (base == null || base.isEmpty())
           && (system == null || system.isEmpty()) && (type == null || type.isEmpty()) && (instance == null || instance.isEmpty())
           && (parameter == null || parameter.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.OperationDefinition;
   }

  @SearchParamDefinition(name="date", path="OperationDefinition.date", description="Date for this version of the operation definition", type="date" )
  public static final String SP_DATE = "date";
  @SearchParamDefinition(name="code", path="OperationDefinition.code", description="Name used to invoke the operation", type="token" )
  public static final String SP_CODE = "code";
  @SearchParamDefinition(name="instance", path="OperationDefinition.instance", description="Invoke on an instance?", type="token" )
  public static final String SP_INSTANCE = "instance";
  @SearchParamDefinition(name="kind", path="OperationDefinition.kind", description="operation | query", type="token" )
  public static final String SP_KIND = "kind";
  @SearchParamDefinition(name="profile", path="OperationDefinition.parameter.profile", description="Profile on the type", type="reference" )
  public static final String SP_PROFILE = "profile";
  @SearchParamDefinition(name="type", path="OperationDefinition.type", description="Invoke at resource level for these type", type="token" )
  public static final String SP_TYPE = "type";
  @SearchParamDefinition(name="version", path="OperationDefinition.version", description="Logical id for this version of the operation definition", type="token" )
  public static final String SP_VERSION = "version";
  @SearchParamDefinition(name="url", path="OperationDefinition.url", description="Logical URL to reference this operation definition", type="uri" )
  public static final String SP_URL = "url";
  @SearchParamDefinition(name="system", path="OperationDefinition.system", description="Invoke at the system level?", type="token" )
  public static final String SP_SYSTEM = "system";
  @SearchParamDefinition(name="name", path="OperationDefinition.name", description="Informal name for this operation", type="string" )
  public static final String SP_NAME = "name";
  @SearchParamDefinition(name="publisher", path="OperationDefinition.publisher", description="Name of the publisher (Organization or individual)", type="string" )
  public static final String SP_PUBLISHER = "publisher";
  @SearchParamDefinition(name="status", path="OperationDefinition.status", description="draft | active | retired", type="token" )
  public static final String SP_STATUS = "status";
  @SearchParamDefinition(name="base", path="OperationDefinition.base", description="Marks this as a profile of the base", type="reference" )
  public static final String SP_BASE = "base";

}

