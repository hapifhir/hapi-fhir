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

// Generated on Thu, Mar 1, 2018 20:26+1100 for FHIR v3.2.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.r4.model.Enumerations.*;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * A set of rules of how FHIR is used to solve a particular problem. This resource is used to gather all the parts of an implementation guide into a logical whole and to publish a computable definition of all the parts.
 */
@ResourceDef(name="ImplementationGuideOutput", profile="http://hl7.org/fhir/Profile/ImplementationGuideOutput")
@ChildOrder(names={"url", "version", "name", "status", "experimental", "date", "publisher", "contact", "description", "useContext", "jurisdiction", "copyright", "fhirVersion", "dependsOn", "resource", "global", "rendering", "page", "image", "other"})
public class ImplementationGuideOutput extends MetadataResource {

    @Block()
    public static class ImplementationGuideOutputResourceComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Where this resource is found.
         */
        @Child(name = "reference", type = {Reference.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Location of the resource", formalDefinition="Where this resource is found." )
        protected Reference reference;

        /**
         * The actual object that is the target of the reference (Where this resource is found.)
         */
        protected Resource referenceTarget;

        /**
         * If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.
         */
        @Child(name = "example", type = {BooleanType.class, StructureDefinition.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Is an example/What is this an example of?", formalDefinition="If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile." )
        protected Type example;

        /**
         * The relative path for primary page for this resource within the IG.
         */
        @Child(name = "relativePath", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Relative path for page in IG", formalDefinition="The relative path for primary page for this resource within the IG." )
        protected StringType relativePath;

        private static final long serialVersionUID = -1567587618L;

    /**
     * Constructor
     */
      public ImplementationGuideOutputResourceComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ImplementationGuideOutputResourceComponent(Reference reference) {
        super();
        this.reference = reference;
      }

        /**
         * @return {@link #reference} (Where this resource is found.)
         */
        public Reference getReference() { 
          if (this.reference == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideOutputResourceComponent.reference");
            else if (Configuration.doAutoCreate())
              this.reference = new Reference(); // cc
          return this.reference;
        }

        public boolean hasReference() { 
          return this.reference != null && !this.reference.isEmpty();
        }

        /**
         * @param value {@link #reference} (Where this resource is found.)
         */
        public ImplementationGuideOutputResourceComponent setReference(Reference value) { 
          this.reference = value;
          return this;
        }

        /**
         * @return {@link #reference} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Where this resource is found.)
         */
        public Resource getReferenceTarget() { 
          return this.referenceTarget;
        }

        /**
         * @param value {@link #reference} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Where this resource is found.)
         */
        public ImplementationGuideOutputResourceComponent setReferenceTarget(Resource value) { 
          this.referenceTarget = value;
          return this;
        }

        /**
         * @return {@link #example} (If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.)
         */
        public Type getExample() { 
          return this.example;
        }

        /**
         * @return {@link #example} (If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.)
         */
        public BooleanType getExampleBooleanType() throws FHIRException { 
          if (!(this.example instanceof BooleanType))
            throw new FHIRException("Type mismatch: the type BooleanType was expected, but "+this.example.getClass().getName()+" was encountered");
          return (BooleanType) this.example;
        }

        public boolean hasExampleBooleanType() { 
          return this.example instanceof BooleanType;
        }

        /**
         * @return {@link #example} (If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.)
         */
        public Reference getExampleReference() throws FHIRException { 
          if (!(this.example instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.example.getClass().getName()+" was encountered");
          return (Reference) this.example;
        }

        public boolean hasExampleReference() { 
          return this.example instanceof Reference;
        }

        public boolean hasExample() { 
          return this.example != null && !this.example.isEmpty();
        }

        /**
         * @param value {@link #example} (If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.)
         */
        public ImplementationGuideOutputResourceComponent setExample(Type value) { 
          this.example = value;
          return this;
        }

        /**
         * @return {@link #relativePath} (The relative path for primary page for this resource within the IG.). This is the underlying object with id, value and extensions. The accessor "getRelativePath" gives direct access to the value
         */
        public StringType getRelativePathElement() { 
          if (this.relativePath == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideOutputResourceComponent.relativePath");
            else if (Configuration.doAutoCreate())
              this.relativePath = new StringType(); // bb
          return this.relativePath;
        }

        public boolean hasRelativePathElement() { 
          return this.relativePath != null && !this.relativePath.isEmpty();
        }

        public boolean hasRelativePath() { 
          return this.relativePath != null && !this.relativePath.isEmpty();
        }

        /**
         * @param value {@link #relativePath} (The relative path for primary page for this resource within the IG.). This is the underlying object with id, value and extensions. The accessor "getRelativePath" gives direct access to the value
         */
        public ImplementationGuideOutputResourceComponent setRelativePathElement(StringType value) { 
          this.relativePath = value;
          return this;
        }

        /**
         * @return The relative path for primary page for this resource within the IG.
         */
        public String getRelativePath() { 
          return this.relativePath == null ? null : this.relativePath.getValue();
        }

        /**
         * @param value The relative path for primary page for this resource within the IG.
         */
        public ImplementationGuideOutputResourceComponent setRelativePath(String value) { 
          if (Utilities.noString(value))
            this.relativePath = null;
          else {
            if (this.relativePath == null)
              this.relativePath = new StringType();
            this.relativePath.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("reference", "Reference(Any)", "Where this resource is found.", 0, 1, reference));
          children.add(new Property("example[x]", "boolean|Reference(StructureDefinition)", "If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.", 0, 1, example));
          children.add(new Property("relativePath", "string", "The relative path for primary page for this resource within the IG.", 0, 1, relativePath));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -925155509: /*reference*/  return new Property("reference", "Reference(Any)", "Where this resource is found.", 0, 1, reference);
          case -2002328874: /*example[x]*/  return new Property("example[x]", "boolean|Reference(StructureDefinition)", "If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.", 0, 1, example);
          case -1322970774: /*example*/  return new Property("example[x]", "boolean|Reference(StructureDefinition)", "If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.", 0, 1, example);
          case 159803230: /*exampleBoolean*/  return new Property("example[x]", "boolean|Reference(StructureDefinition)", "If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.", 0, 1, example);
          case 263472385: /*exampleReference*/  return new Property("example[x]", "boolean|Reference(StructureDefinition)", "If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.", 0, 1, example);
          case -70808303: /*relativePath*/  return new Property("relativePath", "string", "The relative path for primary page for this resource within the IG.", 0, 1, relativePath);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -925155509: /*reference*/ return this.reference == null ? new Base[0] : new Base[] {this.reference}; // Reference
        case -1322970774: /*example*/ return this.example == null ? new Base[0] : new Base[] {this.example}; // Type
        case -70808303: /*relativePath*/ return this.relativePath == null ? new Base[0] : new Base[] {this.relativePath}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -925155509: // reference
          this.reference = castToReference(value); // Reference
          return value;
        case -1322970774: // example
          this.example = castToType(value); // Type
          return value;
        case -70808303: // relativePath
          this.relativePath = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("reference")) {
          this.reference = castToReference(value); // Reference
        } else if (name.equals("example[x]")) {
          this.example = castToType(value); // Type
        } else if (name.equals("relativePath")) {
          this.relativePath = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -925155509:  return getReference(); 
        case -2002328874:  return getExample(); 
        case -1322970774:  return getExample(); 
        case -70808303:  return getRelativePathElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -925155509: /*reference*/ return new String[] {"Reference"};
        case -1322970774: /*example*/ return new String[] {"boolean", "Reference"};
        case -70808303: /*relativePath*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("reference")) {
          this.reference = new Reference();
          return this.reference;
        }
        else if (name.equals("exampleBoolean")) {
          this.example = new BooleanType();
          return this.example;
        }
        else if (name.equals("exampleReference")) {
          this.example = new Reference();
          return this.example;
        }
        else if (name.equals("relativePath")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideOutput.relativePath");
        }
        else
          return super.addChild(name);
      }

      public ImplementationGuideOutputResourceComponent copy() {
        ImplementationGuideOutputResourceComponent dst = new ImplementationGuideOutputResourceComponent();
        copyValues(dst);
        dst.reference = reference == null ? null : reference.copy();
        dst.example = example == null ? null : example.copy();
        dst.relativePath = relativePath == null ? null : relativePath.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideOutputResourceComponent))
          return false;
        ImplementationGuideOutputResourceComponent o = (ImplementationGuideOutputResourceComponent) other_;
        return compareDeep(reference, o.reference, true) && compareDeep(example, o.example, true) && compareDeep(relativePath, o.relativePath, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideOutputResourceComponent))
          return false;
        ImplementationGuideOutputResourceComponent o = (ImplementationGuideOutputResourceComponent) other_;
        return compareValues(relativePath, o.relativePath, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(reference, example, relativePath
          );
      }

  public String fhirType() {
    return "ImplementationGuideOutput.resource";

  }

  }

    @Block()
    public static class ImplementationGuideOutputGlobalComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The type of resource that all instances must conform to.
         */
        @Child(name = "type", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Type this profiles applies to", formalDefinition="The type of resource that all instances must conform to." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/resource-types")
        protected CodeType type;

        /**
         * A reference to the profile that all instances must conform to.
         */
        @Child(name = "profile", type = {CanonicalType.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Profile that all resources must conform to", formalDefinition="A reference to the profile that all instances must conform to." )
        protected CanonicalType profile;

        private static final long serialVersionUID = 33894666L;

    /**
     * Constructor
     */
      public ImplementationGuideOutputGlobalComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ImplementationGuideOutputGlobalComponent(CodeType type, CanonicalType profile) {
        super();
        this.type = type;
        this.profile = profile;
      }

        /**
         * @return {@link #type} (The type of resource that all instances must conform to.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public CodeType getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideOutputGlobalComponent.type");
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
         * @param value {@link #type} (The type of resource that all instances must conform to.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public ImplementationGuideOutputGlobalComponent setTypeElement(CodeType value) { 
          this.type = value;
          return this;
        }

        /**
         * @return The type of resource that all instances must conform to.
         */
        public String getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value The type of resource that all instances must conform to.
         */
        public ImplementationGuideOutputGlobalComponent setType(String value) { 
            if (this.type == null)
              this.type = new CodeType();
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #profile} (A reference to the profile that all instances must conform to.). This is the underlying object with id, value and extensions. The accessor "getProfile" gives direct access to the value
         */
        public CanonicalType getProfileElement() { 
          if (this.profile == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideOutputGlobalComponent.profile");
            else if (Configuration.doAutoCreate())
              this.profile = new CanonicalType(); // bb
          return this.profile;
        }

        public boolean hasProfileElement() { 
          return this.profile != null && !this.profile.isEmpty();
        }

        public boolean hasProfile() { 
          return this.profile != null && !this.profile.isEmpty();
        }

        /**
         * @param value {@link #profile} (A reference to the profile that all instances must conform to.). This is the underlying object with id, value and extensions. The accessor "getProfile" gives direct access to the value
         */
        public ImplementationGuideOutputGlobalComponent setProfileElement(CanonicalType value) { 
          this.profile = value;
          return this;
        }

        /**
         * @return A reference to the profile that all instances must conform to.
         */
        public String getProfile() { 
          return this.profile == null ? null : this.profile.getValue();
        }

        /**
         * @param value A reference to the profile that all instances must conform to.
         */
        public ImplementationGuideOutputGlobalComponent setProfile(String value) { 
            if (this.profile == null)
              this.profile = new CanonicalType();
            this.profile.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("type", "code", "The type of resource that all instances must conform to.", 0, 1, type));
          children.add(new Property("profile", "canonical(StructureDefinition)", "A reference to the profile that all instances must conform to.", 0, 1, profile));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3575610: /*type*/  return new Property("type", "code", "The type of resource that all instances must conform to.", 0, 1, type);
          case -309425751: /*profile*/  return new Property("profile", "canonical(StructureDefinition)", "A reference to the profile that all instances must conform to.", 0, 1, profile);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeType
        case -309425751: /*profile*/ return this.profile == null ? new Base[0] : new Base[] {this.profile}; // CanonicalType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCode(value); // CodeType
          return value;
        case -309425751: // profile
          this.profile = castToCanonical(value); // CanonicalType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToCode(value); // CodeType
        } else if (name.equals("profile")) {
          this.profile = castToCanonical(value); // CanonicalType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getTypeElement();
        case -309425751:  return getProfileElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"code"};
        case -309425751: /*profile*/ return new String[] {"canonical"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideOutput.type");
        }
        else if (name.equals("profile")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideOutput.profile");
        }
        else
          return super.addChild(name);
      }

      public ImplementationGuideOutputGlobalComponent copy() {
        ImplementationGuideOutputGlobalComponent dst = new ImplementationGuideOutputGlobalComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.profile = profile == null ? null : profile.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideOutputGlobalComponent))
          return false;
        ImplementationGuideOutputGlobalComponent o = (ImplementationGuideOutputGlobalComponent) other_;
        return compareDeep(type, o.type, true) && compareDeep(profile, o.profile, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideOutputGlobalComponent))
          return false;
        ImplementationGuideOutputGlobalComponent o = (ImplementationGuideOutputGlobalComponent) other_;
        return compareValues(type, o.type, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, profile);
      }

  public String fhirType() {
    return "ImplementationGuideOutput.global";

  }

  }

    @Block()
    public static class ImplementationGuideOutputPageComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Relative path to the page.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="HTML page name", formalDefinition="Relative path to the page." )
        protected StringType name;

        /**
         * Label for the page intended for human display.
         */
        @Child(name = "title", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Title of the page", formalDefinition="Label for the page intended for human display." )
        protected StringType title;

        /**
         * The name of an anchor available on the page.
         */
        @Child(name = "anchor", type = {StringType.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Anchor available on the page", formalDefinition="The name of an anchor available on the page." )
        protected List<StringType> anchor;

        private static final long serialVersionUID = 1920576611L;

    /**
     * Constructor
     */
      public ImplementationGuideOutputPageComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ImplementationGuideOutputPageComponent(StringType name) {
        super();
        this.name = name;
      }

        /**
         * @return {@link #name} (Relative path to the page.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideOutputPageComponent.name");
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
         * @param value {@link #name} (Relative path to the page.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public ImplementationGuideOutputPageComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return Relative path to the page.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value Relative path to the page.
         */
        public ImplementationGuideOutputPageComponent setName(String value) { 
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          return this;
        }

        /**
         * @return {@link #title} (Label for the page intended for human display.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
         */
        public StringType getTitleElement() { 
          if (this.title == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideOutputPageComponent.title");
            else if (Configuration.doAutoCreate())
              this.title = new StringType(); // bb
          return this.title;
        }

        public boolean hasTitleElement() { 
          return this.title != null && !this.title.isEmpty();
        }

        public boolean hasTitle() { 
          return this.title != null && !this.title.isEmpty();
        }

        /**
         * @param value {@link #title} (Label for the page intended for human display.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
         */
        public ImplementationGuideOutputPageComponent setTitleElement(StringType value) { 
          this.title = value;
          return this;
        }

        /**
         * @return Label for the page intended for human display.
         */
        public String getTitle() { 
          return this.title == null ? null : this.title.getValue();
        }

        /**
         * @param value Label for the page intended for human display.
         */
        public ImplementationGuideOutputPageComponent setTitle(String value) { 
          if (Utilities.noString(value))
            this.title = null;
          else {
            if (this.title == null)
              this.title = new StringType();
            this.title.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #anchor} (The name of an anchor available on the page.)
         */
        public List<StringType> getAnchor() { 
          if (this.anchor == null)
            this.anchor = new ArrayList<StringType>();
          return this.anchor;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ImplementationGuideOutputPageComponent setAnchor(List<StringType> theAnchor) { 
          this.anchor = theAnchor;
          return this;
        }

        public boolean hasAnchor() { 
          if (this.anchor == null)
            return false;
          for (StringType item : this.anchor)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #anchor} (The name of an anchor available on the page.)
         */
        public StringType addAnchorElement() {//2 
          StringType t = new StringType();
          if (this.anchor == null)
            this.anchor = new ArrayList<StringType>();
          this.anchor.add(t);
          return t;
        }

        /**
         * @param value {@link #anchor} (The name of an anchor available on the page.)
         */
        public ImplementationGuideOutputPageComponent addAnchor(String value) { //1
          StringType t = new StringType();
          t.setValue(value);
          if (this.anchor == null)
            this.anchor = new ArrayList<StringType>();
          this.anchor.add(t);
          return this;
        }

        /**
         * @param value {@link #anchor} (The name of an anchor available on the page.)
         */
        public boolean hasAnchor(String value) { 
          if (this.anchor == null)
            return false;
          for (StringType v : this.anchor)
            if (v.getValue().equals(value)) // string
              return true;
          return false;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("name", "string", "Relative path to the page.", 0, 1, name));
          children.add(new Property("title", "string", "Label for the page intended for human display.", 0, 1, title));
          children.add(new Property("anchor", "string", "The name of an anchor available on the page.", 0, java.lang.Integer.MAX_VALUE, anchor));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3373707: /*name*/  return new Property("name", "string", "Relative path to the page.", 0, 1, name);
          case 110371416: /*title*/  return new Property("title", "string", "Label for the page intended for human display.", 0, 1, title);
          case -1413299531: /*anchor*/  return new Property("anchor", "string", "The name of an anchor available on the page.", 0, java.lang.Integer.MAX_VALUE, anchor);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case 110371416: /*title*/ return this.title == null ? new Base[0] : new Base[] {this.title}; // StringType
        case -1413299531: /*anchor*/ return this.anchor == null ? new Base[0] : this.anchor.toArray(new Base[this.anchor.size()]); // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case 110371416: // title
          this.title = castToString(value); // StringType
          return value;
        case -1413299531: // anchor
          this.getAnchor().add(castToString(value)); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("title")) {
          this.title = castToString(value); // StringType
        } else if (name.equals("anchor")) {
          this.getAnchor().add(castToString(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707:  return getNameElement();
        case 110371416:  return getTitleElement();
        case -1413299531:  return addAnchorElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return new String[] {"string"};
        case 110371416: /*title*/ return new String[] {"string"};
        case -1413299531: /*anchor*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideOutput.name");
        }
        else if (name.equals("title")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideOutput.title");
        }
        else if (name.equals("anchor")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideOutput.anchor");
        }
        else
          return super.addChild(name);
      }

      public ImplementationGuideOutputPageComponent copy() {
        ImplementationGuideOutputPageComponent dst = new ImplementationGuideOutputPageComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.title = title == null ? null : title.copy();
        if (anchor != null) {
          dst.anchor = new ArrayList<StringType>();
          for (StringType i : anchor)
            dst.anchor.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideOutputPageComponent))
          return false;
        ImplementationGuideOutputPageComponent o = (ImplementationGuideOutputPageComponent) other_;
        return compareDeep(name, o.name, true) && compareDeep(title, o.title, true) && compareDeep(anchor, o.anchor, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideOutputPageComponent))
          return false;
        ImplementationGuideOutputPageComponent o = (ImplementationGuideOutputPageComponent) other_;
        return compareValues(name, o.name, true) && compareValues(title, o.title, true) && compareValues(anchor, o.anchor, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(name, title, anchor);
      }

  public String fhirType() {
    return "ImplementationGuideOutput.page";

  }

  }

    /**
     * A copyright statement relating to the implementation guide output and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the implementation guide output.
     */
    @Child(name = "copyright", type = {MarkdownType.class}, order=0, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Use and/or publishing restrictions", formalDefinition="A copyright statement relating to the implementation guide output and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the implementation guide output." )
    protected MarkdownType copyright;

    /**
     * The version of the FHIR specification on which this ImplementationGuide is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 3.2.0 for this version.
     */
    @Child(name = "fhirVersion", type = {IdType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="FHIR Version this Implementation Guide targets", formalDefinition="The version of the FHIR specification on which this ImplementationGuide is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 3.2.0 for this version." )
    protected IdType fhirVersion;

    /**
     * Another implementation guide that this implementation depends on. Typically, an implementation guide uses value sets, profiles etc.defined in other implementation guides.
     */
    @Child(name = "dependsOn", type = {ImplementationGuideOutput.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Another Implementation guide this depends on", formalDefinition="Another implementation guide that this implementation depends on. Typically, an implementation guide uses value sets, profiles etc.defined in other implementation guides." )
    protected List<Reference> dependsOn;
    /**
     * The actual objects that are the target of the reference (Another implementation guide that this implementation depends on. Typically, an implementation guide uses value sets, profiles etc.defined in other implementation guides.)
     */
    protected List<ImplementationGuideOutput> dependsOnTarget;


    /**
     * A resource that is part of the implementation guide. Conformance resources (value set, structure definition, capability statements etc.) are obvious candidates for inclusion, but any kind of resource can be included as an example resource.
     */
    @Child(name = "resource", type = {}, order=3, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Resource in the implementation guide", formalDefinition="A resource that is part of the implementation guide. Conformance resources (value set, structure definition, capability statements etc.) are obvious candidates for inclusion, but any kind of resource can be included as an example resource." )
    protected List<ImplementationGuideOutputResourceComponent> resource;

    /**
     * A set of profiles that all resources covered by this implementation guide must conform to.
     */
    @Child(name = "global", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Profiles that apply globally", formalDefinition="A set of profiles that all resources covered by this implementation guide must conform to." )
    protected List<ImplementationGuideOutputGlobalComponent> global;

    /**
     * A pointer to official web page, PDF or other rendering of the implementation guide.
     */
    @Child(name = "rendering", type = {UriType.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Location of rendered implementation guide", formalDefinition="A pointer to official web page, PDF or other rendering of the implementation guide." )
    protected UriType rendering;

    /**
     * Information about a page within the IG.
     */
    @Child(name = "page", type = {}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="HTML page within the parent IG", formalDefinition="Information about a page within the IG." )
    protected List<ImplementationGuideOutputPageComponent> page;

    /**
     * Indicates a relative path to an image that exists within the IG.
     */
    @Child(name = "image", type = {StringType.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Image within the IG", formalDefinition="Indicates a relative path to an image that exists within the IG." )
    protected List<StringType> image;

    /**
     * Indicates the relative path of an additional non-page, non-image file that is part of the IG - e.g. zip, jar and similar files that could be the target of a hyperlink in a derived IG.
     */
    @Child(name = "other", type = {StringType.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Additional linkable file in IG", formalDefinition="Indicates the relative path of an additional non-page, non-image file that is part of the IG - e.g. zip, jar and similar files that could be the target of a hyperlink in a derived IG." )
    protected List<StringType> other;

    private static final long serialVersionUID = -1963803314L;

  /**
   * Constructor
   */
    public ImplementationGuideOutput() {
      super();
    }

  /**
   * Constructor
   */
    public ImplementationGuideOutput(UriType url, StringType name, Enumeration<PublicationStatus> status) {
      super();
      this.url = url;
      this.name = name;
      this.status = status;
    }

    /**
     * @return {@link #url} (An absolute URI that is used to identify this implementation guide output when it is referenced in a specification, model, design or an instance. This SHOULD be globally unique, and SHOULD be a literal address at which this implementation guide output is (or will be) published.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public UriType getUrlElement() { 
      if (this.url == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuideOutput.url");
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
     * @param value {@link #url} (An absolute URI that is used to identify this implementation guide output when it is referenced in a specification, model, design or an instance. This SHOULD be globally unique, and SHOULD be a literal address at which this implementation guide output is (or will be) published.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public ImplementationGuideOutput setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return An absolute URI that is used to identify this implementation guide output when it is referenced in a specification, model, design or an instance. This SHOULD be globally unique, and SHOULD be a literal address at which this implementation guide output is (or will be) published.
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value An absolute URI that is used to identify this implementation guide output when it is referenced in a specification, model, design or an instance. This SHOULD be globally unique, and SHOULD be a literal address at which this implementation guide output is (or will be) published.
     */
    public ImplementationGuideOutput setUrl(String value) { 
        if (this.url == null)
          this.url = new UriType();
        this.url.setValue(value);
      return this;
    }

    /**
     * @return {@link #version} (The identifier that is used to identify this version of the implementation guide output when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the implementation guide output author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public StringType getVersionElement() { 
      if (this.version == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuideOutput.version");
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
     * @param value {@link #version} (The identifier that is used to identify this version of the implementation guide output when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the implementation guide output author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public ImplementationGuideOutput setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return The identifier that is used to identify this version of the implementation guide output when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the implementation guide output author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value The identifier that is used to identify this version of the implementation guide output when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the implementation guide output author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.
     */
    public ImplementationGuideOutput setVersion(String value) { 
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
     * @return {@link #name} (A natural language name identifying the implementation guide output. This name should be usable as an identifier for the module by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuideOutput.name");
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
     * @param value {@link #name} (A natural language name identifying the implementation guide output. This name should be usable as an identifier for the module by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public ImplementationGuideOutput setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return A natural language name identifying the implementation guide output. This name should be usable as an identifier for the module by machine processing applications such as code generation.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value A natural language name identifying the implementation guide output. This name should be usable as an identifier for the module by machine processing applications such as code generation.
     */
    public ImplementationGuideOutput setName(String value) { 
        if (this.name == null)
          this.name = new StringType();
        this.name.setValue(value);
      return this;
    }

    /**
     * @return {@link #status} (The status of this implementation guide output. Enables tracking the life-cycle of the content.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<PublicationStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuideOutput.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<PublicationStatus>(new PublicationStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of this implementation guide output. Enables tracking the life-cycle of the content.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public ImplementationGuideOutput setStatusElement(Enumeration<PublicationStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of this implementation guide output. Enables tracking the life-cycle of the content.
     */
    public PublicationStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of this implementation guide output. Enables tracking the life-cycle of the content.
     */
    public ImplementationGuideOutput setStatus(PublicationStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<PublicationStatus>(new PublicationStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #experimental} (A boolean value to indicate that this implementation guide output is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public BooleanType getExperimentalElement() { 
      if (this.experimental == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuideOutput.experimental");
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
     * @param value {@link #experimental} (A boolean value to indicate that this implementation guide output is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public ImplementationGuideOutput setExperimentalElement(BooleanType value) { 
      this.experimental = value;
      return this;
    }

    /**
     * @return A boolean value to indicate that this implementation guide output is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public boolean getExperimental() { 
      return this.experimental == null || this.experimental.isEmpty() ? false : this.experimental.getValue();
    }

    /**
     * @param value A boolean value to indicate that this implementation guide output is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public ImplementationGuideOutput setExperimental(boolean value) { 
        if (this.experimental == null)
          this.experimental = new BooleanType();
        this.experimental.setValue(value);
      return this;
    }

    /**
     * @return {@link #date} (The date  (and optionally time) when the implementation guide output was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the implementation guide output changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuideOutput.date");
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
     * @param value {@link #date} (The date  (and optionally time) when the implementation guide output was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the implementation guide output changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public ImplementationGuideOutput setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date  (and optionally time) when the implementation guide output was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the implementation guide output changes.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date  (and optionally time) when the implementation guide output was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the implementation guide output changes.
     */
    public ImplementationGuideOutput setDate(Date value) { 
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
     * @return {@link #publisher} (The name of the individual or organization that published the implementation guide output.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public StringType getPublisherElement() { 
      if (this.publisher == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuideOutput.publisher");
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
     * @param value {@link #publisher} (The name of the individual or organization that published the implementation guide output.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public ImplementationGuideOutput setPublisherElement(StringType value) { 
      this.publisher = value;
      return this;
    }

    /**
     * @return The name of the individual or organization that published the implementation guide output.
     */
    public String getPublisher() { 
      return this.publisher == null ? null : this.publisher.getValue();
    }

    /**
     * @param value The name of the individual or organization that published the implementation guide output.
     */
    public ImplementationGuideOutput setPublisher(String value) { 
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
     * @return {@link #contact} (Contact details to assist a user in finding and communicating with the publisher.)
     */
    public List<ContactDetail> getContact() { 
      if (this.contact == null)
        this.contact = new ArrayList<ContactDetail>();
      return this.contact;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ImplementationGuideOutput setContact(List<ContactDetail> theContact) { 
      this.contact = theContact;
      return this;
    }

    public boolean hasContact() { 
      if (this.contact == null)
        return false;
      for (ContactDetail item : this.contact)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ContactDetail addContact() { //3
      ContactDetail t = new ContactDetail();
      if (this.contact == null)
        this.contact = new ArrayList<ContactDetail>();
      this.contact.add(t);
      return t;
    }

    public ImplementationGuideOutput addContact(ContactDetail t) { //3
      if (t == null)
        return this;
      if (this.contact == null)
        this.contact = new ArrayList<ContactDetail>();
      this.contact.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #contact}, creating it if it does not already exist
     */
    public ContactDetail getContactFirstRep() { 
      if (getContact().isEmpty()) {
        addContact();
      }
      return getContact().get(0);
    }

    /**
     * @return {@link #description} (A free text natural language description of the implementation guide output from a consumer's perspective.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public MarkdownType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuideOutput.description");
        else if (Configuration.doAutoCreate())
          this.description = new MarkdownType(); // bb
      return this.description;
    }

    public boolean hasDescriptionElement() { 
      return this.description != null && !this.description.isEmpty();
    }

    public boolean hasDescription() { 
      return this.description != null && !this.description.isEmpty();
    }

    /**
     * @param value {@link #description} (A free text natural language description of the implementation guide output from a consumer's perspective.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public ImplementationGuideOutput setDescriptionElement(MarkdownType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A free text natural language description of the implementation guide output from a consumer's perspective.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A free text natural language description of the implementation guide output from a consumer's perspective.
     */
    public ImplementationGuideOutput setDescription(String value) { 
      if (value == null)
        this.description = null;
      else {
        if (this.description == null)
          this.description = new MarkdownType();
        this.description.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #useContext} (The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching for appropriate implementation guide output instances.)
     */
    public List<UsageContext> getUseContext() { 
      if (this.useContext == null)
        this.useContext = new ArrayList<UsageContext>();
      return this.useContext;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ImplementationGuideOutput setUseContext(List<UsageContext> theUseContext) { 
      this.useContext = theUseContext;
      return this;
    }

    public boolean hasUseContext() { 
      if (this.useContext == null)
        return false;
      for (UsageContext item : this.useContext)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public UsageContext addUseContext() { //3
      UsageContext t = new UsageContext();
      if (this.useContext == null)
        this.useContext = new ArrayList<UsageContext>();
      this.useContext.add(t);
      return t;
    }

    public ImplementationGuideOutput addUseContext(UsageContext t) { //3
      if (t == null)
        return this;
      if (this.useContext == null)
        this.useContext = new ArrayList<UsageContext>();
      this.useContext.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #useContext}, creating it if it does not already exist
     */
    public UsageContext getUseContextFirstRep() { 
      if (getUseContext().isEmpty()) {
        addUseContext();
      }
      return getUseContext().get(0);
    }

    /**
     * @return {@link #jurisdiction} (A legal or geographic region in which the implementation guide output is intended to be used.)
     */
    public List<CodeableConcept> getJurisdiction() { 
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      return this.jurisdiction;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ImplementationGuideOutput setJurisdiction(List<CodeableConcept> theJurisdiction) { 
      this.jurisdiction = theJurisdiction;
      return this;
    }

    public boolean hasJurisdiction() { 
      if (this.jurisdiction == null)
        return false;
      for (CodeableConcept item : this.jurisdiction)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addJurisdiction() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      this.jurisdiction.add(t);
      return t;
    }

    public ImplementationGuideOutput addJurisdiction(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      this.jurisdiction.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #jurisdiction}, creating it if it does not already exist
     */
    public CodeableConcept getJurisdictionFirstRep() { 
      if (getJurisdiction().isEmpty()) {
        addJurisdiction();
      }
      return getJurisdiction().get(0);
    }

    /**
     * @return {@link #copyright} (A copyright statement relating to the implementation guide output and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the implementation guide output.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public MarkdownType getCopyrightElement() { 
      if (this.copyright == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuideOutput.copyright");
        else if (Configuration.doAutoCreate())
          this.copyright = new MarkdownType(); // bb
      return this.copyright;
    }

    public boolean hasCopyrightElement() { 
      return this.copyright != null && !this.copyright.isEmpty();
    }

    public boolean hasCopyright() { 
      return this.copyright != null && !this.copyright.isEmpty();
    }

    /**
     * @param value {@link #copyright} (A copyright statement relating to the implementation guide output and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the implementation guide output.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public ImplementationGuideOutput setCopyrightElement(MarkdownType value) { 
      this.copyright = value;
      return this;
    }

    /**
     * @return A copyright statement relating to the implementation guide output and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the implementation guide output.
     */
    public String getCopyright() { 
      return this.copyright == null ? null : this.copyright.getValue();
    }

    /**
     * @param value A copyright statement relating to the implementation guide output and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the implementation guide output.
     */
    public ImplementationGuideOutput setCopyright(String value) { 
      if (value == null)
        this.copyright = null;
      else {
        if (this.copyright == null)
          this.copyright = new MarkdownType();
        this.copyright.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #fhirVersion} (The version of the FHIR specification on which this ImplementationGuide is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 3.2.0 for this version.). This is the underlying object with id, value and extensions. The accessor "getFhirVersion" gives direct access to the value
     */
    public IdType getFhirVersionElement() { 
      if (this.fhirVersion == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuideOutput.fhirVersion");
        else if (Configuration.doAutoCreate())
          this.fhirVersion = new IdType(); // bb
      return this.fhirVersion;
    }

    public boolean hasFhirVersionElement() { 
      return this.fhirVersion != null && !this.fhirVersion.isEmpty();
    }

    public boolean hasFhirVersion() { 
      return this.fhirVersion != null && !this.fhirVersion.isEmpty();
    }

    /**
     * @param value {@link #fhirVersion} (The version of the FHIR specification on which this ImplementationGuide is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 3.2.0 for this version.). This is the underlying object with id, value and extensions. The accessor "getFhirVersion" gives direct access to the value
     */
    public ImplementationGuideOutput setFhirVersionElement(IdType value) { 
      this.fhirVersion = value;
      return this;
    }

    /**
     * @return The version of the FHIR specification on which this ImplementationGuide is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 3.2.0 for this version.
     */
    public String getFhirVersion() { 
      return this.fhirVersion == null ? null : this.fhirVersion.getValue();
    }

    /**
     * @param value The version of the FHIR specification on which this ImplementationGuide is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 3.2.0 for this version.
     */
    public ImplementationGuideOutput setFhirVersion(String value) { 
      if (Utilities.noString(value))
        this.fhirVersion = null;
      else {
        if (this.fhirVersion == null)
          this.fhirVersion = new IdType();
        this.fhirVersion.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #dependsOn} (Another implementation guide that this implementation depends on. Typically, an implementation guide uses value sets, profiles etc.defined in other implementation guides.)
     */
    public List<Reference> getDependsOn() { 
      if (this.dependsOn == null)
        this.dependsOn = new ArrayList<Reference>();
      return this.dependsOn;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ImplementationGuideOutput setDependsOn(List<Reference> theDependsOn) { 
      this.dependsOn = theDependsOn;
      return this;
    }

    public boolean hasDependsOn() { 
      if (this.dependsOn == null)
        return false;
      for (Reference item : this.dependsOn)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addDependsOn() { //3
      Reference t = new Reference();
      if (this.dependsOn == null)
        this.dependsOn = new ArrayList<Reference>();
      this.dependsOn.add(t);
      return t;
    }

    public ImplementationGuideOutput addDependsOn(Reference t) { //3
      if (t == null)
        return this;
      if (this.dependsOn == null)
        this.dependsOn = new ArrayList<Reference>();
      this.dependsOn.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #dependsOn}, creating it if it does not already exist
     */
    public Reference getDependsOnFirstRep() { 
      if (getDependsOn().isEmpty()) {
        addDependsOn();
      }
      return getDependsOn().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<ImplementationGuideOutput> getDependsOnTarget() { 
      if (this.dependsOnTarget == null)
        this.dependsOnTarget = new ArrayList<ImplementationGuideOutput>();
      return this.dependsOnTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public ImplementationGuideOutput addDependsOnTarget() { 
      ImplementationGuideOutput r = new ImplementationGuideOutput();
      if (this.dependsOnTarget == null)
        this.dependsOnTarget = new ArrayList<ImplementationGuideOutput>();
      this.dependsOnTarget.add(r);
      return r;
    }

    /**
     * @return {@link #resource} (A resource that is part of the implementation guide. Conformance resources (value set, structure definition, capability statements etc.) are obvious candidates for inclusion, but any kind of resource can be included as an example resource.)
     */
    public List<ImplementationGuideOutputResourceComponent> getResource() { 
      if (this.resource == null)
        this.resource = new ArrayList<ImplementationGuideOutputResourceComponent>();
      return this.resource;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ImplementationGuideOutput setResource(List<ImplementationGuideOutputResourceComponent> theResource) { 
      this.resource = theResource;
      return this;
    }

    public boolean hasResource() { 
      if (this.resource == null)
        return false;
      for (ImplementationGuideOutputResourceComponent item : this.resource)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ImplementationGuideOutputResourceComponent addResource() { //3
      ImplementationGuideOutputResourceComponent t = new ImplementationGuideOutputResourceComponent();
      if (this.resource == null)
        this.resource = new ArrayList<ImplementationGuideOutputResourceComponent>();
      this.resource.add(t);
      return t;
    }

    public ImplementationGuideOutput addResource(ImplementationGuideOutputResourceComponent t) { //3
      if (t == null)
        return this;
      if (this.resource == null)
        this.resource = new ArrayList<ImplementationGuideOutputResourceComponent>();
      this.resource.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #resource}, creating it if it does not already exist
     */
    public ImplementationGuideOutputResourceComponent getResourceFirstRep() { 
      if (getResource().isEmpty()) {
        addResource();
      }
      return getResource().get(0);
    }

    /**
     * @return {@link #global} (A set of profiles that all resources covered by this implementation guide must conform to.)
     */
    public List<ImplementationGuideOutputGlobalComponent> getGlobal() { 
      if (this.global == null)
        this.global = new ArrayList<ImplementationGuideOutputGlobalComponent>();
      return this.global;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ImplementationGuideOutput setGlobal(List<ImplementationGuideOutputGlobalComponent> theGlobal) { 
      this.global = theGlobal;
      return this;
    }

    public boolean hasGlobal() { 
      if (this.global == null)
        return false;
      for (ImplementationGuideOutputGlobalComponent item : this.global)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ImplementationGuideOutputGlobalComponent addGlobal() { //3
      ImplementationGuideOutputGlobalComponent t = new ImplementationGuideOutputGlobalComponent();
      if (this.global == null)
        this.global = new ArrayList<ImplementationGuideOutputGlobalComponent>();
      this.global.add(t);
      return t;
    }

    public ImplementationGuideOutput addGlobal(ImplementationGuideOutputGlobalComponent t) { //3
      if (t == null)
        return this;
      if (this.global == null)
        this.global = new ArrayList<ImplementationGuideOutputGlobalComponent>();
      this.global.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #global}, creating it if it does not already exist
     */
    public ImplementationGuideOutputGlobalComponent getGlobalFirstRep() { 
      if (getGlobal().isEmpty()) {
        addGlobal();
      }
      return getGlobal().get(0);
    }

    /**
     * @return {@link #rendering} (A pointer to official web page, PDF or other rendering of the implementation guide.). This is the underlying object with id, value and extensions. The accessor "getRendering" gives direct access to the value
     */
    public UriType getRenderingElement() { 
      if (this.rendering == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuideOutput.rendering");
        else if (Configuration.doAutoCreate())
          this.rendering = new UriType(); // bb
      return this.rendering;
    }

    public boolean hasRenderingElement() { 
      return this.rendering != null && !this.rendering.isEmpty();
    }

    public boolean hasRendering() { 
      return this.rendering != null && !this.rendering.isEmpty();
    }

    /**
     * @param value {@link #rendering} (A pointer to official web page, PDF or other rendering of the implementation guide.). This is the underlying object with id, value and extensions. The accessor "getRendering" gives direct access to the value
     */
    public ImplementationGuideOutput setRenderingElement(UriType value) { 
      this.rendering = value;
      return this;
    }

    /**
     * @return A pointer to official web page, PDF or other rendering of the implementation guide.
     */
    public String getRendering() { 
      return this.rendering == null ? null : this.rendering.getValue();
    }

    /**
     * @param value A pointer to official web page, PDF or other rendering of the implementation guide.
     */
    public ImplementationGuideOutput setRendering(String value) { 
      if (Utilities.noString(value))
        this.rendering = null;
      else {
        if (this.rendering == null)
          this.rendering = new UriType();
        this.rendering.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #page} (Information about a page within the IG.)
     */
    public List<ImplementationGuideOutputPageComponent> getPage() { 
      if (this.page == null)
        this.page = new ArrayList<ImplementationGuideOutputPageComponent>();
      return this.page;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ImplementationGuideOutput setPage(List<ImplementationGuideOutputPageComponent> thePage) { 
      this.page = thePage;
      return this;
    }

    public boolean hasPage() { 
      if (this.page == null)
        return false;
      for (ImplementationGuideOutputPageComponent item : this.page)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ImplementationGuideOutputPageComponent addPage() { //3
      ImplementationGuideOutputPageComponent t = new ImplementationGuideOutputPageComponent();
      if (this.page == null)
        this.page = new ArrayList<ImplementationGuideOutputPageComponent>();
      this.page.add(t);
      return t;
    }

    public ImplementationGuideOutput addPage(ImplementationGuideOutputPageComponent t) { //3
      if (t == null)
        return this;
      if (this.page == null)
        this.page = new ArrayList<ImplementationGuideOutputPageComponent>();
      this.page.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #page}, creating it if it does not already exist
     */
    public ImplementationGuideOutputPageComponent getPageFirstRep() { 
      if (getPage().isEmpty()) {
        addPage();
      }
      return getPage().get(0);
    }

    /**
     * @return {@link #image} (Indicates a relative path to an image that exists within the IG.)
     */
    public List<StringType> getImage() { 
      if (this.image == null)
        this.image = new ArrayList<StringType>();
      return this.image;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ImplementationGuideOutput setImage(List<StringType> theImage) { 
      this.image = theImage;
      return this;
    }

    public boolean hasImage() { 
      if (this.image == null)
        return false;
      for (StringType item : this.image)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #image} (Indicates a relative path to an image that exists within the IG.)
     */
    public StringType addImageElement() {//2 
      StringType t = new StringType();
      if (this.image == null)
        this.image = new ArrayList<StringType>();
      this.image.add(t);
      return t;
    }

    /**
     * @param value {@link #image} (Indicates a relative path to an image that exists within the IG.)
     */
    public ImplementationGuideOutput addImage(String value) { //1
      StringType t = new StringType();
      t.setValue(value);
      if (this.image == null)
        this.image = new ArrayList<StringType>();
      this.image.add(t);
      return this;
    }

    /**
     * @param value {@link #image} (Indicates a relative path to an image that exists within the IG.)
     */
    public boolean hasImage(String value) { 
      if (this.image == null)
        return false;
      for (StringType v : this.image)
        if (v.getValue().equals(value)) // string
          return true;
      return false;
    }

    /**
     * @return {@link #other} (Indicates the relative path of an additional non-page, non-image file that is part of the IG - e.g. zip, jar and similar files that could be the target of a hyperlink in a derived IG.)
     */
    public List<StringType> getOther() { 
      if (this.other == null)
        this.other = new ArrayList<StringType>();
      return this.other;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ImplementationGuideOutput setOther(List<StringType> theOther) { 
      this.other = theOther;
      return this;
    }

    public boolean hasOther() { 
      if (this.other == null)
        return false;
      for (StringType item : this.other)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #other} (Indicates the relative path of an additional non-page, non-image file that is part of the IG - e.g. zip, jar and similar files that could be the target of a hyperlink in a derived IG.)
     */
    public StringType addOtherElement() {//2 
      StringType t = new StringType();
      if (this.other == null)
        this.other = new ArrayList<StringType>();
      this.other.add(t);
      return t;
    }

    /**
     * @param value {@link #other} (Indicates the relative path of an additional non-page, non-image file that is part of the IG - e.g. zip, jar and similar files that could be the target of a hyperlink in a derived IG.)
     */
    public ImplementationGuideOutput addOther(String value) { //1
      StringType t = new StringType();
      t.setValue(value);
      if (this.other == null)
        this.other = new ArrayList<StringType>();
      this.other.add(t);
      return this;
    }

    /**
     * @param value {@link #other} (Indicates the relative path of an additional non-page, non-image file that is part of the IG - e.g. zip, jar and similar files that could be the target of a hyperlink in a derived IG.)
     */
    public boolean hasOther(String value) { 
      if (this.other == null)
        return false;
      for (StringType v : this.other)
        if (v.getValue().equals(value)) // string
          return true;
      return false;
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("url", "uri", "An absolute URI that is used to identify this implementation guide output when it is referenced in a specification, model, design or an instance. This SHOULD be globally unique, and SHOULD be a literal address at which this implementation guide output is (or will be) published.", 0, 1, url));
        children.add(new Property("version", "string", "The identifier that is used to identify this version of the implementation guide output when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the implementation guide output author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.", 0, 1, version));
        children.add(new Property("name", "string", "A natural language name identifying the implementation guide output. This name should be usable as an identifier for the module by machine processing applications such as code generation.", 0, 1, name));
        children.add(new Property("status", "code", "The status of this implementation guide output. Enables tracking the life-cycle of the content.", 0, 1, status));
        children.add(new Property("experimental", "boolean", "A boolean value to indicate that this implementation guide output is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.", 0, 1, experimental));
        children.add(new Property("date", "dateTime", "The date  (and optionally time) when the implementation guide output was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the implementation guide output changes.", 0, 1, date));
        children.add(new Property("publisher", "string", "The name of the individual or organization that published the implementation guide output.", 0, 1, publisher));
        children.add(new Property("contact", "ContactDetail", "Contact details to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact));
        children.add(new Property("description", "markdown", "A free text natural language description of the implementation guide output from a consumer's perspective.", 0, 1, description));
        children.add(new Property("useContext", "UsageContext", "The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching for appropriate implementation guide output instances.", 0, java.lang.Integer.MAX_VALUE, useContext));
        children.add(new Property("jurisdiction", "CodeableConcept", "A legal or geographic region in which the implementation guide output is intended to be used.", 0, java.lang.Integer.MAX_VALUE, jurisdiction));
        children.add(new Property("copyright", "markdown", "A copyright statement relating to the implementation guide output and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the implementation guide output.", 0, 1, copyright));
        children.add(new Property("fhirVersion", "id", "The version of the FHIR specification on which this ImplementationGuide is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 3.2.0 for this version.", 0, 1, fhirVersion));
        children.add(new Property("dependsOn", "Reference(ImplementationGuideOutput)", "Another implementation guide that this implementation depends on. Typically, an implementation guide uses value sets, profiles etc.defined in other implementation guides.", 0, java.lang.Integer.MAX_VALUE, dependsOn));
        children.add(new Property("resource", "", "A resource that is part of the implementation guide. Conformance resources (value set, structure definition, capability statements etc.) are obvious candidates for inclusion, but any kind of resource can be included as an example resource.", 0, java.lang.Integer.MAX_VALUE, resource));
        children.add(new Property("global", "", "A set of profiles that all resources covered by this implementation guide must conform to.", 0, java.lang.Integer.MAX_VALUE, global));
        children.add(new Property("rendering", "uri", "A pointer to official web page, PDF or other rendering of the implementation guide.", 0, 1, rendering));
        children.add(new Property("page", "", "Information about a page within the IG.", 0, java.lang.Integer.MAX_VALUE, page));
        children.add(new Property("image", "string", "Indicates a relative path to an image that exists within the IG.", 0, java.lang.Integer.MAX_VALUE, image));
        children.add(new Property("other", "string", "Indicates the relative path of an additional non-page, non-image file that is part of the IG - e.g. zip, jar and similar files that could be the target of a hyperlink in a derived IG.", 0, java.lang.Integer.MAX_VALUE, other));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case 116079: /*url*/  return new Property("url", "uri", "An absolute URI that is used to identify this implementation guide output when it is referenced in a specification, model, design or an instance. This SHOULD be globally unique, and SHOULD be a literal address at which this implementation guide output is (or will be) published.", 0, 1, url);
        case 351608024: /*version*/  return new Property("version", "string", "The identifier that is used to identify this version of the implementation guide output when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the implementation guide output author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.", 0, 1, version);
        case 3373707: /*name*/  return new Property("name", "string", "A natural language name identifying the implementation guide output. This name should be usable as an identifier for the module by machine processing applications such as code generation.", 0, 1, name);
        case -892481550: /*status*/  return new Property("status", "code", "The status of this implementation guide output. Enables tracking the life-cycle of the content.", 0, 1, status);
        case -404562712: /*experimental*/  return new Property("experimental", "boolean", "A boolean value to indicate that this implementation guide output is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.", 0, 1, experimental);
        case 3076014: /*date*/  return new Property("date", "dateTime", "The date  (and optionally time) when the implementation guide output was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the implementation guide output changes.", 0, 1, date);
        case 1447404028: /*publisher*/  return new Property("publisher", "string", "The name of the individual or organization that published the implementation guide output.", 0, 1, publisher);
        case 951526432: /*contact*/  return new Property("contact", "ContactDetail", "Contact details to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact);
        case -1724546052: /*description*/  return new Property("description", "markdown", "A free text natural language description of the implementation guide output from a consumer's perspective.", 0, 1, description);
        case -669707736: /*useContext*/  return new Property("useContext", "UsageContext", "The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching for appropriate implementation guide output instances.", 0, java.lang.Integer.MAX_VALUE, useContext);
        case -507075711: /*jurisdiction*/  return new Property("jurisdiction", "CodeableConcept", "A legal or geographic region in which the implementation guide output is intended to be used.", 0, java.lang.Integer.MAX_VALUE, jurisdiction);
        case 1522889671: /*copyright*/  return new Property("copyright", "markdown", "A copyright statement relating to the implementation guide output and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the implementation guide output.", 0, 1, copyright);
        case 461006061: /*fhirVersion*/  return new Property("fhirVersion", "id", "The version of the FHIR specification on which this ImplementationGuide is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 3.2.0 for this version.", 0, 1, fhirVersion);
        case -1109214266: /*dependsOn*/  return new Property("dependsOn", "Reference(ImplementationGuideOutput)", "Another implementation guide that this implementation depends on. Typically, an implementation guide uses value sets, profiles etc.defined in other implementation guides.", 0, java.lang.Integer.MAX_VALUE, dependsOn);
        case -341064690: /*resource*/  return new Property("resource", "", "A resource that is part of the implementation guide. Conformance resources (value set, structure definition, capability statements etc.) are obvious candidates for inclusion, but any kind of resource can be included as an example resource.", 0, java.lang.Integer.MAX_VALUE, resource);
        case -1243020381: /*global*/  return new Property("global", "", "A set of profiles that all resources covered by this implementation guide must conform to.", 0, java.lang.Integer.MAX_VALUE, global);
        case 1839654540: /*rendering*/  return new Property("rendering", "uri", "A pointer to official web page, PDF or other rendering of the implementation guide.", 0, 1, rendering);
        case 3433103: /*page*/  return new Property("page", "", "Information about a page within the IG.", 0, java.lang.Integer.MAX_VALUE, page);
        case 100313435: /*image*/  return new Property("image", "string", "Indicates a relative path to an image that exists within the IG.", 0, java.lang.Integer.MAX_VALUE, image);
        case 106069776: /*other*/  return new Property("other", "string", "Indicates the relative path of an additional non-page, non-image file that is part of the IG - e.g. zip, jar and similar files that could be the target of a hyperlink in a derived IG.", 0, java.lang.Integer.MAX_VALUE, other);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        case 351608024: /*version*/ return this.version == null ? new Base[0] : new Base[] {this.version}; // StringType
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<PublicationStatus>
        case -404562712: /*experimental*/ return this.experimental == null ? new Base[0] : new Base[] {this.experimental}; // BooleanType
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateTimeType
        case 1447404028: /*publisher*/ return this.publisher == null ? new Base[0] : new Base[] {this.publisher}; // StringType
        case 951526432: /*contact*/ return this.contact == null ? new Base[0] : this.contact.toArray(new Base[this.contact.size()]); // ContactDetail
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // MarkdownType
        case -669707736: /*useContext*/ return this.useContext == null ? new Base[0] : this.useContext.toArray(new Base[this.useContext.size()]); // UsageContext
        case -507075711: /*jurisdiction*/ return this.jurisdiction == null ? new Base[0] : this.jurisdiction.toArray(new Base[this.jurisdiction.size()]); // CodeableConcept
        case 1522889671: /*copyright*/ return this.copyright == null ? new Base[0] : new Base[] {this.copyright}; // MarkdownType
        case 461006061: /*fhirVersion*/ return this.fhirVersion == null ? new Base[0] : new Base[] {this.fhirVersion}; // IdType
        case -1109214266: /*dependsOn*/ return this.dependsOn == null ? new Base[0] : this.dependsOn.toArray(new Base[this.dependsOn.size()]); // Reference
        case -341064690: /*resource*/ return this.resource == null ? new Base[0] : this.resource.toArray(new Base[this.resource.size()]); // ImplementationGuideOutputResourceComponent
        case -1243020381: /*global*/ return this.global == null ? new Base[0] : this.global.toArray(new Base[this.global.size()]); // ImplementationGuideOutputGlobalComponent
        case 1839654540: /*rendering*/ return this.rendering == null ? new Base[0] : new Base[] {this.rendering}; // UriType
        case 3433103: /*page*/ return this.page == null ? new Base[0] : this.page.toArray(new Base[this.page.size()]); // ImplementationGuideOutputPageComponent
        case 100313435: /*image*/ return this.image == null ? new Base[0] : this.image.toArray(new Base[this.image.size()]); // StringType
        case 106069776: /*other*/ return this.other == null ? new Base[0] : this.other.toArray(new Base[this.other.size()]); // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 116079: // url
          this.url = castToUri(value); // UriType
          return value;
        case 351608024: // version
          this.version = castToString(value); // StringType
          return value;
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case -892481550: // status
          value = new PublicationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<PublicationStatus>
          return value;
        case -404562712: // experimental
          this.experimental = castToBoolean(value); // BooleanType
          return value;
        case 3076014: // date
          this.date = castToDateTime(value); // DateTimeType
          return value;
        case 1447404028: // publisher
          this.publisher = castToString(value); // StringType
          return value;
        case 951526432: // contact
          this.getContact().add(castToContactDetail(value)); // ContactDetail
          return value;
        case -1724546052: // description
          this.description = castToMarkdown(value); // MarkdownType
          return value;
        case -669707736: // useContext
          this.getUseContext().add(castToUsageContext(value)); // UsageContext
          return value;
        case -507075711: // jurisdiction
          this.getJurisdiction().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 1522889671: // copyright
          this.copyright = castToMarkdown(value); // MarkdownType
          return value;
        case 461006061: // fhirVersion
          this.fhirVersion = castToId(value); // IdType
          return value;
        case -1109214266: // dependsOn
          this.getDependsOn().add(castToReference(value)); // Reference
          return value;
        case -341064690: // resource
          this.getResource().add((ImplementationGuideOutputResourceComponent) value); // ImplementationGuideOutputResourceComponent
          return value;
        case -1243020381: // global
          this.getGlobal().add((ImplementationGuideOutputGlobalComponent) value); // ImplementationGuideOutputGlobalComponent
          return value;
        case 1839654540: // rendering
          this.rendering = castToUri(value); // UriType
          return value;
        case 3433103: // page
          this.getPage().add((ImplementationGuideOutputPageComponent) value); // ImplementationGuideOutputPageComponent
          return value;
        case 100313435: // image
          this.getImage().add(castToString(value)); // StringType
          return value;
        case 106069776: // other
          this.getOther().add(castToString(value)); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("url")) {
          this.url = castToUri(value); // UriType
        } else if (name.equals("version")) {
          this.version = castToString(value); // StringType
        } else if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("status")) {
          value = new PublicationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<PublicationStatus>
        } else if (name.equals("experimental")) {
          this.experimental = castToBoolean(value); // BooleanType
        } else if (name.equals("date")) {
          this.date = castToDateTime(value); // DateTimeType
        } else if (name.equals("publisher")) {
          this.publisher = castToString(value); // StringType
        } else if (name.equals("contact")) {
          this.getContact().add(castToContactDetail(value));
        } else if (name.equals("description")) {
          this.description = castToMarkdown(value); // MarkdownType
        } else if (name.equals("useContext")) {
          this.getUseContext().add(castToUsageContext(value));
        } else if (name.equals("jurisdiction")) {
          this.getJurisdiction().add(castToCodeableConcept(value));
        } else if (name.equals("copyright")) {
          this.copyright = castToMarkdown(value); // MarkdownType
        } else if (name.equals("fhirVersion")) {
          this.fhirVersion = castToId(value); // IdType
        } else if (name.equals("dependsOn")) {
          this.getDependsOn().add(castToReference(value));
        } else if (name.equals("resource")) {
          this.getResource().add((ImplementationGuideOutputResourceComponent) value);
        } else if (name.equals("global")) {
          this.getGlobal().add((ImplementationGuideOutputGlobalComponent) value);
        } else if (name.equals("rendering")) {
          this.rendering = castToUri(value); // UriType
        } else if (name.equals("page")) {
          this.getPage().add((ImplementationGuideOutputPageComponent) value);
        } else if (name.equals("image")) {
          this.getImage().add(castToString(value));
        } else if (name.equals("other")) {
          this.getOther().add(castToString(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116079:  return getUrlElement();
        case 351608024:  return getVersionElement();
        case 3373707:  return getNameElement();
        case -892481550:  return getStatusElement();
        case -404562712:  return getExperimentalElement();
        case 3076014:  return getDateElement();
        case 1447404028:  return getPublisherElement();
        case 951526432:  return addContact(); 
        case -1724546052:  return getDescriptionElement();
        case -669707736:  return addUseContext(); 
        case -507075711:  return addJurisdiction(); 
        case 1522889671:  return getCopyrightElement();
        case 461006061:  return getFhirVersionElement();
        case -1109214266:  return addDependsOn(); 
        case -341064690:  return addResource(); 
        case -1243020381:  return addGlobal(); 
        case 1839654540:  return getRenderingElement();
        case 3433103:  return addPage(); 
        case 100313435:  return addImageElement();
        case 106069776:  return addOtherElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116079: /*url*/ return new String[] {"uri"};
        case 351608024: /*version*/ return new String[] {"string"};
        case 3373707: /*name*/ return new String[] {"string"};
        case -892481550: /*status*/ return new String[] {"code"};
        case -404562712: /*experimental*/ return new String[] {"boolean"};
        case 3076014: /*date*/ return new String[] {"dateTime"};
        case 1447404028: /*publisher*/ return new String[] {"string"};
        case 951526432: /*contact*/ return new String[] {"ContactDetail"};
        case -1724546052: /*description*/ return new String[] {"markdown"};
        case -669707736: /*useContext*/ return new String[] {"UsageContext"};
        case -507075711: /*jurisdiction*/ return new String[] {"CodeableConcept"};
        case 1522889671: /*copyright*/ return new String[] {"markdown"};
        case 461006061: /*fhirVersion*/ return new String[] {"id"};
        case -1109214266: /*dependsOn*/ return new String[] {"Reference"};
        case -341064690: /*resource*/ return new String[] {};
        case -1243020381: /*global*/ return new String[] {};
        case 1839654540: /*rendering*/ return new String[] {"uri"};
        case 3433103: /*page*/ return new String[] {};
        case 100313435: /*image*/ return new String[] {"string"};
        case 106069776: /*other*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideOutput.url");
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideOutput.version");
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideOutput.name");
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideOutput.status");
        }
        else if (name.equals("experimental")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideOutput.experimental");
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideOutput.date");
        }
        else if (name.equals("publisher")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideOutput.publisher");
        }
        else if (name.equals("contact")) {
          return addContact();
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideOutput.description");
        }
        else if (name.equals("useContext")) {
          return addUseContext();
        }
        else if (name.equals("jurisdiction")) {
          return addJurisdiction();
        }
        else if (name.equals("copyright")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideOutput.copyright");
        }
        else if (name.equals("fhirVersion")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideOutput.fhirVersion");
        }
        else if (name.equals("dependsOn")) {
          return addDependsOn();
        }
        else if (name.equals("resource")) {
          return addResource();
        }
        else if (name.equals("global")) {
          return addGlobal();
        }
        else if (name.equals("rendering")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideOutput.rendering");
        }
        else if (name.equals("page")) {
          return addPage();
        }
        else if (name.equals("image")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideOutput.image");
        }
        else if (name.equals("other")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideOutput.other");
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "ImplementationGuideOutput";

  }

      public ImplementationGuideOutput copy() {
        ImplementationGuideOutput dst = new ImplementationGuideOutput();
        copyValues(dst);
        dst.url = url == null ? null : url.copy();
        dst.version = version == null ? null : version.copy();
        dst.name = name == null ? null : name.copy();
        dst.status = status == null ? null : status.copy();
        dst.experimental = experimental == null ? null : experimental.copy();
        dst.date = date == null ? null : date.copy();
        dst.publisher = publisher == null ? null : publisher.copy();
        if (contact != null) {
          dst.contact = new ArrayList<ContactDetail>();
          for (ContactDetail i : contact)
            dst.contact.add(i.copy());
        };
        dst.description = description == null ? null : description.copy();
        if (useContext != null) {
          dst.useContext = new ArrayList<UsageContext>();
          for (UsageContext i : useContext)
            dst.useContext.add(i.copy());
        };
        if (jurisdiction != null) {
          dst.jurisdiction = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : jurisdiction)
            dst.jurisdiction.add(i.copy());
        };
        dst.copyright = copyright == null ? null : copyright.copy();
        dst.fhirVersion = fhirVersion == null ? null : fhirVersion.copy();
        if (dependsOn != null) {
          dst.dependsOn = new ArrayList<Reference>();
          for (Reference i : dependsOn)
            dst.dependsOn.add(i.copy());
        };
        if (resource != null) {
          dst.resource = new ArrayList<ImplementationGuideOutputResourceComponent>();
          for (ImplementationGuideOutputResourceComponent i : resource)
            dst.resource.add(i.copy());
        };
        if (global != null) {
          dst.global = new ArrayList<ImplementationGuideOutputGlobalComponent>();
          for (ImplementationGuideOutputGlobalComponent i : global)
            dst.global.add(i.copy());
        };
        dst.rendering = rendering == null ? null : rendering.copy();
        if (page != null) {
          dst.page = new ArrayList<ImplementationGuideOutputPageComponent>();
          for (ImplementationGuideOutputPageComponent i : page)
            dst.page.add(i.copy());
        };
        if (image != null) {
          dst.image = new ArrayList<StringType>();
          for (StringType i : image)
            dst.image.add(i.copy());
        };
        if (other != null) {
          dst.other = new ArrayList<StringType>();
          for (StringType i : other)
            dst.other.add(i.copy());
        };
        return dst;
      }

      protected ImplementationGuideOutput typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideOutput))
          return false;
        ImplementationGuideOutput o = (ImplementationGuideOutput) other_;
        return compareDeep(copyright, o.copyright, true) && compareDeep(fhirVersion, o.fhirVersion, true)
           && compareDeep(dependsOn, o.dependsOn, true) && compareDeep(resource, o.resource, true) && compareDeep(global, o.global, true)
           && compareDeep(rendering, o.rendering, true) && compareDeep(page, o.page, true) && compareDeep(image, o.image, true)
           && compareDeep(other, o.other, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideOutput))
          return false;
        ImplementationGuideOutput o = (ImplementationGuideOutput) other_;
        return compareValues(copyright, o.copyright, true) && compareValues(fhirVersion, o.fhirVersion, true)
           && compareValues(rendering, o.rendering, true) && compareValues(image, o.image, true) && compareValues(other, o.other, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(copyright, fhirVersion, dependsOn
          , resource, global, rendering, page, image, other);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ImplementationGuideOutput;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>The implementation guide output publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ImplementationGuideOutput.date</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="ImplementationGuideOutput.date", description="The implementation guide output publication date", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>The implementation guide output publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ImplementationGuideOutput.date</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>dependency</b>
   * <p>
   * Description: <b>Another Implementation guide this depends on</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImplementationGuideOutput.dependsOn</b><br>
   * </p>
   */
  @SearchParamDefinition(name="dependency", path="ImplementationGuideOutput.dependsOn", description="Another Implementation guide this depends on", type="reference", target={ImplementationGuideOutput.class } )
  public static final String SP_DEPENDENCY = "dependency";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>dependency</b>
   * <p>
   * Description: <b>Another Implementation guide this depends on</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImplementationGuideOutput.dependsOn</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam DEPENDENCY = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_DEPENDENCY);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ImplementationGuideOutput:dependency</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_DEPENDENCY = new ca.uhn.fhir.model.api.Include("ImplementationGuideOutput:dependency").toLocked();

 /**
   * Search parameter: <b>resource</b>
   * <p>
   * Description: <b>Location of the resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImplementationGuideOutput.resource.reference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="resource", path="ImplementationGuideOutput.resource.reference", description="Location of the resource", type="reference" )
  public static final String SP_RESOURCE = "resource";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>resource</b>
   * <p>
   * Description: <b>Location of the resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImplementationGuideOutput.resource.reference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam RESOURCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_RESOURCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ImplementationGuideOutput:resource</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_RESOURCE = new ca.uhn.fhir.model.api.Include("ImplementationGuideOutput:resource").toLocked();

 /**
   * Search parameter: <b>jurisdiction</b>
   * <p>
   * Description: <b>Intended jurisdiction for the implementation guide output</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImplementationGuideOutput.jurisdiction</b><br>
   * </p>
   */
  @SearchParamDefinition(name="jurisdiction", path="ImplementationGuideOutput.jurisdiction", description="Intended jurisdiction for the implementation guide output", type="token" )
  public static final String SP_JURISDICTION = "jurisdiction";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>jurisdiction</b>
   * <p>
   * Description: <b>Intended jurisdiction for the implementation guide output</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImplementationGuideOutput.jurisdiction</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam JURISDICTION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_JURISDICTION);

 /**
   * Search parameter: <b>name</b>
   * <p>
   * Description: <b>Computationally friendly name of the implementation guide output</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ImplementationGuideOutput.name</b><br>
   * </p>
   */
  @SearchParamDefinition(name="name", path="ImplementationGuideOutput.name", description="Computationally friendly name of the implementation guide output", type="string" )
  public static final String SP_NAME = "name";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>name</b>
   * <p>
   * Description: <b>Computationally friendly name of the implementation guide output</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ImplementationGuideOutput.name</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam NAME = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_NAME);

 /**
   * Search parameter: <b>description</b>
   * <p>
   * Description: <b>The description of the implementation guide output</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ImplementationGuideOutput.description</b><br>
   * </p>
   */
  @SearchParamDefinition(name="description", path="ImplementationGuideOutput.description", description="The description of the implementation guide output", type="string" )
  public static final String SP_DESCRIPTION = "description";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>description</b>
   * <p>
   * Description: <b>The description of the implementation guide output</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ImplementationGuideOutput.description</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam DESCRIPTION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_DESCRIPTION);

 /**
   * Search parameter: <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the implementation guide output</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ImplementationGuideOutput.publisher</b><br>
   * </p>
   */
  @SearchParamDefinition(name="publisher", path="ImplementationGuideOutput.publisher", description="Name of the publisher of the implementation guide output", type="string" )
  public static final String SP_PUBLISHER = "publisher";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the implementation guide output</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ImplementationGuideOutput.publisher</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam PUBLISHER = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_PUBLISHER);

 /**
   * Search parameter: <b>experimental</b>
   * <p>
   * Description: <b>For testing purposes, not real usage</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImplementationGuideOutput.experimental</b><br>
   * </p>
   */
  @SearchParamDefinition(name="experimental", path="ImplementationGuideOutput.experimental", description="For testing purposes, not real usage", type="token" )
  public static final String SP_EXPERIMENTAL = "experimental";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>experimental</b>
   * <p>
   * Description: <b>For testing purposes, not real usage</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImplementationGuideOutput.experimental</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam EXPERIMENTAL = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_EXPERIMENTAL);

 /**
   * Search parameter: <b>version</b>
   * <p>
   * Description: <b>The business version of the implementation guide output</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImplementationGuideOutput.version</b><br>
   * </p>
   */
  @SearchParamDefinition(name="version", path="ImplementationGuideOutput.version", description="The business version of the implementation guide output", type="token" )
  public static final String SP_VERSION = "version";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>version</b>
   * <p>
   * Description: <b>The business version of the implementation guide output</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImplementationGuideOutput.version</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam VERSION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_VERSION);

 /**
   * Search parameter: <b>url</b>
   * <p>
   * Description: <b>The uri that identifies the implementation guide output</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ImplementationGuideOutput.url</b><br>
   * </p>
   */
  @SearchParamDefinition(name="url", path="ImplementationGuideOutput.url", description="The uri that identifies the implementation guide output", type="uri" )
  public static final String SP_URL = "url";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>url</b>
   * <p>
   * Description: <b>The uri that identifies the implementation guide output</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ImplementationGuideOutput.url</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam URL = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_URL);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The current status of the implementation guide output</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImplementationGuideOutput.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="ImplementationGuideOutput.status", description="The current status of the implementation guide output", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The current status of the implementation guide output</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImplementationGuideOutput.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

