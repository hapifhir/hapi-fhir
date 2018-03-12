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
@ResourceDef(name="ImplementationGuideInput", profile="http://hl7.org/fhir/Profile/ImplementationGuideInput")
@ChildOrder(names={"url", "version", "name", "status", "experimental", "date", "publisher", "contact", "description", "useContext", "jurisdiction", "copyright", "fhirVersion", "dependsOn", "global", "definition"})
public class ImplementationGuideInput extends MetadataResource {

    public enum GuideParameterCode {
        /**
         * If the value of this boolean 0..1 parameter is "true" then all conformance resources will have any specified [Resource].version overwritten with the ImplementationGuide.version
         */
        APPLYBUSINESSVERSION, 
        /**
         * If the value of this boolean 0..1 parameter is "true" then all conformance resources will have any specified [Resource].jurisdiction overwritten with the ImplementationGuide.jurisdiction
         */
        APPLYJURISDICTION, 
        /**
         * The value of this string 0..* parameter is a subfolder of the build context's location that is to be scanned to load resources. Scope is (if present) a particular resource type
         */
        PATHRESOURCE, 
        /**
         * The value of this string 0..1 parameter is a subfolder of the build context's location that contains files that are part of the html content processed by the builder
         */
        PATHPAGES, 
        /**
         * The value of this string 0..1 parameter is a subfolder of the build context's location that is used as the terminology cache. If this is not present, the terminology cache is on the local system, not under version control
         */
        PATHTXCACHE, 
        /**
         * The value of this string 0..1 parameter is a path to the ExpansionProfile used when expanding value sets for this implementation guide. This is particularly used to specify the versions of published terminologies such as SNOMED CT
         */
        EXPANSIONPROFILE, 
        /**
         * The value of this string 0..1 parameter is either "warning" or "error" (default = "error"). If the value is "warning" then IG build tools allow the IG to be considered successfully build even when there is no internal broken links
         */
        RULEBROKENLINKS, 
        /**
         * The value of this boolean 0..1 parameter specifies whether the IG publisher creates examples in XML format. If not present, the Publication Tool decides whether to generate XML
         */
        GENERATEXML, 
        /**
         * The value of this boolean 0..1 parameter specifies whether the IG publisher creates examples in JSON format. If not present, the Publication Tool decides whether to generate JSON
         */
        GENERATEJSON, 
        /**
         * The value of this boolean 0..1 parameter specifies whether the IG publisher creates examples in Turtle format. If not present, the Publication Tool decides whether to generate Turtle
         */
        GENERATETURTLE, 
        /**
         * The value of this string singleton parameter is the name of the file to use as the builder template for each generated page (see templating)
         */
        HTMLTEMPLATE, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static GuideParameterCode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("apply-business-version".equals(codeString))
          return APPLYBUSINESSVERSION;
        if ("apply-jurisdiction".equals(codeString))
          return APPLYJURISDICTION;
        if ("path-resource".equals(codeString))
          return PATHRESOURCE;
        if ("path-pages".equals(codeString))
          return PATHPAGES;
        if ("path-tx-cache".equals(codeString))
          return PATHTXCACHE;
        if ("expansion-profile".equals(codeString))
          return EXPANSIONPROFILE;
        if ("rule-broken-links".equals(codeString))
          return RULEBROKENLINKS;
        if ("generate-xml".equals(codeString))
          return GENERATEXML;
        if ("generate-json".equals(codeString))
          return GENERATEJSON;
        if ("generate-turtle".equals(codeString))
          return GENERATETURTLE;
        if ("html-template".equals(codeString))
          return HTMLTEMPLATE;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown GuideParameterCode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case APPLYBUSINESSVERSION: return "apply-business-version";
            case APPLYJURISDICTION: return "apply-jurisdiction";
            case PATHRESOURCE: return "path-resource";
            case PATHPAGES: return "path-pages";
            case PATHTXCACHE: return "path-tx-cache";
            case EXPANSIONPROFILE: return "expansion-profile";
            case RULEBROKENLINKS: return "rule-broken-links";
            case GENERATEXML: return "generate-xml";
            case GENERATEJSON: return "generate-json";
            case GENERATETURTLE: return "generate-turtle";
            case HTMLTEMPLATE: return "html-template";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case APPLYBUSINESSVERSION: return "http://hl7.org/fhir/guide-parameter-code";
            case APPLYJURISDICTION: return "http://hl7.org/fhir/guide-parameter-code";
            case PATHRESOURCE: return "http://hl7.org/fhir/guide-parameter-code";
            case PATHPAGES: return "http://hl7.org/fhir/guide-parameter-code";
            case PATHTXCACHE: return "http://hl7.org/fhir/guide-parameter-code";
            case EXPANSIONPROFILE: return "http://hl7.org/fhir/guide-parameter-code";
            case RULEBROKENLINKS: return "http://hl7.org/fhir/guide-parameter-code";
            case GENERATEXML: return "http://hl7.org/fhir/guide-parameter-code";
            case GENERATEJSON: return "http://hl7.org/fhir/guide-parameter-code";
            case GENERATETURTLE: return "http://hl7.org/fhir/guide-parameter-code";
            case HTMLTEMPLATE: return "http://hl7.org/fhir/guide-parameter-code";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case APPLYBUSINESSVERSION: return "If the value of this boolean 0..1 parameter is \"true\" then all conformance resources will have any specified [Resource].version overwritten with the ImplementationGuide.version";
            case APPLYJURISDICTION: return "If the value of this boolean 0..1 parameter is \"true\" then all conformance resources will have any specified [Resource].jurisdiction overwritten with the ImplementationGuide.jurisdiction";
            case PATHRESOURCE: return "The value of this string 0..* parameter is a subfolder of the build context's location that is to be scanned to load resources. Scope is (if present) a particular resource type";
            case PATHPAGES: return "The value of this string 0..1 parameter is a subfolder of the build context's location that contains files that are part of the html content processed by the builder";
            case PATHTXCACHE: return "The value of this string 0..1 parameter is a subfolder of the build context's location that is used as the terminology cache. If this is not present, the terminology cache is on the local system, not under version control";
            case EXPANSIONPROFILE: return "The value of this string 0..1 parameter is a path to the ExpansionProfile used when expanding value sets for this implementation guide. This is particularly used to specify the versions of published terminologies such as SNOMED CT";
            case RULEBROKENLINKS: return "The value of this string 0..1 parameter is either \"warning\" or \"error\" (default = \"error\"). If the value is \"warning\" then IG build tools allow the IG to be considered successfully build even when there is no internal broken links";
            case GENERATEXML: return "The value of this boolean 0..1 parameter specifies whether the IG publisher creates examples in XML format. If not present, the Publication Tool decides whether to generate XML";
            case GENERATEJSON: return "The value of this boolean 0..1 parameter specifies whether the IG publisher creates examples in JSON format. If not present, the Publication Tool decides whether to generate JSON";
            case GENERATETURTLE: return "The value of this boolean 0..1 parameter specifies whether the IG publisher creates examples in Turtle format. If not present, the Publication Tool decides whether to generate Turtle";
            case HTMLTEMPLATE: return "The value of this string singleton parameter is the name of the file to use as the builder template for each generated page (see templating)";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case APPLYBUSINESSVERSION: return "Apply Business Version";
            case APPLYJURISDICTION: return "Apply Jurisdiction";
            case PATHRESOURCE: return "Resource Path";
            case PATHPAGES: return "Pages Path";
            case PATHTXCACHE: return "Terminology Cache Path";
            case EXPANSIONPROFILE: return "Expansion Profile";
            case RULEBROKENLINKS: return "Broken Links Rule";
            case GENERATEXML: return "Generate XML";
            case GENERATEJSON: return "Generate JSON";
            case GENERATETURTLE: return "Generate Turtle";
            case HTMLTEMPLATE: return "HTML Template";
            default: return "?";
          }
        }
    }

  public static class GuideParameterCodeEnumFactory implements EnumFactory<GuideParameterCode> {
    public GuideParameterCode fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("apply-business-version".equals(codeString))
          return GuideParameterCode.APPLYBUSINESSVERSION;
        if ("apply-jurisdiction".equals(codeString))
          return GuideParameterCode.APPLYJURISDICTION;
        if ("path-resource".equals(codeString))
          return GuideParameterCode.PATHRESOURCE;
        if ("path-pages".equals(codeString))
          return GuideParameterCode.PATHPAGES;
        if ("path-tx-cache".equals(codeString))
          return GuideParameterCode.PATHTXCACHE;
        if ("expansion-profile".equals(codeString))
          return GuideParameterCode.EXPANSIONPROFILE;
        if ("rule-broken-links".equals(codeString))
          return GuideParameterCode.RULEBROKENLINKS;
        if ("generate-xml".equals(codeString))
          return GuideParameterCode.GENERATEXML;
        if ("generate-json".equals(codeString))
          return GuideParameterCode.GENERATEJSON;
        if ("generate-turtle".equals(codeString))
          return GuideParameterCode.GENERATETURTLE;
        if ("html-template".equals(codeString))
          return GuideParameterCode.HTMLTEMPLATE;
        throw new IllegalArgumentException("Unknown GuideParameterCode code '"+codeString+"'");
        }
        public Enumeration<GuideParameterCode> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<GuideParameterCode>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("apply-business-version".equals(codeString))
          return new Enumeration<GuideParameterCode>(this, GuideParameterCode.APPLYBUSINESSVERSION);
        if ("apply-jurisdiction".equals(codeString))
          return new Enumeration<GuideParameterCode>(this, GuideParameterCode.APPLYJURISDICTION);
        if ("path-resource".equals(codeString))
          return new Enumeration<GuideParameterCode>(this, GuideParameterCode.PATHRESOURCE);
        if ("path-pages".equals(codeString))
          return new Enumeration<GuideParameterCode>(this, GuideParameterCode.PATHPAGES);
        if ("path-tx-cache".equals(codeString))
          return new Enumeration<GuideParameterCode>(this, GuideParameterCode.PATHTXCACHE);
        if ("expansion-profile".equals(codeString))
          return new Enumeration<GuideParameterCode>(this, GuideParameterCode.EXPANSIONPROFILE);
        if ("rule-broken-links".equals(codeString))
          return new Enumeration<GuideParameterCode>(this, GuideParameterCode.RULEBROKENLINKS);
        if ("generate-xml".equals(codeString))
          return new Enumeration<GuideParameterCode>(this, GuideParameterCode.GENERATEXML);
        if ("generate-json".equals(codeString))
          return new Enumeration<GuideParameterCode>(this, GuideParameterCode.GENERATEJSON);
        if ("generate-turtle".equals(codeString))
          return new Enumeration<GuideParameterCode>(this, GuideParameterCode.GENERATETURTLE);
        if ("html-template".equals(codeString))
          return new Enumeration<GuideParameterCode>(this, GuideParameterCode.HTMLTEMPLATE);
        throw new FHIRException("Unknown GuideParameterCode code '"+codeString+"'");
        }
    public String toCode(GuideParameterCode code) {
      if (code == GuideParameterCode.APPLYBUSINESSVERSION)
        return "apply-business-version";
      if (code == GuideParameterCode.APPLYJURISDICTION)
        return "apply-jurisdiction";
      if (code == GuideParameterCode.PATHRESOURCE)
        return "path-resource";
      if (code == GuideParameterCode.PATHPAGES)
        return "path-pages";
      if (code == GuideParameterCode.PATHTXCACHE)
        return "path-tx-cache";
      if (code == GuideParameterCode.EXPANSIONPROFILE)
        return "expansion-profile";
      if (code == GuideParameterCode.RULEBROKENLINKS)
        return "rule-broken-links";
      if (code == GuideParameterCode.GENERATEXML)
        return "generate-xml";
      if (code == GuideParameterCode.GENERATEJSON)
        return "generate-json";
      if (code == GuideParameterCode.GENERATETURTLE)
        return "generate-turtle";
      if (code == GuideParameterCode.HTMLTEMPLATE)
        return "html-template";
      return "?";
      }
    public String toSystem(GuideParameterCode code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class ImplementationGuideInputDependsOnComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The canonical reference of the definition of the IG that is depended on.
         */
        @Child(name = "definition", type = {ImplementationGuideOutput.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Canonical Reference to output resource for IG", formalDefinition="The canonical reference of the definition of the IG that is depended on." )
        protected Reference definition;

        /**
         * The actual object that is the target of the reference (The canonical reference of the definition of the IG that is depended on.)
         */
        protected ImplementationGuideOutput definitionTarget;

        /**
         * Version of IG.
         */
        @Child(name = "version", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Version of IG", formalDefinition="Version of IG." )
        protected StringType version;

        private static final long serialVersionUID = 1045480564L;

    /**
     * Constructor
     */
      public ImplementationGuideInputDependsOnComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ImplementationGuideInputDependsOnComponent(Reference definition) {
        super();
        this.definition = definition;
      }

        /**
         * @return {@link #definition} (The canonical reference of the definition of the IG that is depended on.)
         */
        public Reference getDefinition() { 
          if (this.definition == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideInputDependsOnComponent.definition");
            else if (Configuration.doAutoCreate())
              this.definition = new Reference(); // cc
          return this.definition;
        }

        public boolean hasDefinition() { 
          return this.definition != null && !this.definition.isEmpty();
        }

        /**
         * @param value {@link #definition} (The canonical reference of the definition of the IG that is depended on.)
         */
        public ImplementationGuideInputDependsOnComponent setDefinition(Reference value) { 
          this.definition = value;
          return this;
        }

        /**
         * @return {@link #definition} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The canonical reference of the definition of the IG that is depended on.)
         */
        public ImplementationGuideOutput getDefinitionTarget() { 
          if (this.definitionTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideInputDependsOnComponent.definition");
            else if (Configuration.doAutoCreate())
              this.definitionTarget = new ImplementationGuideOutput(); // aa
          return this.definitionTarget;
        }

        /**
         * @param value {@link #definition} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The canonical reference of the definition of the IG that is depended on.)
         */
        public ImplementationGuideInputDependsOnComponent setDefinitionTarget(ImplementationGuideOutput value) { 
          this.definitionTarget = value;
          return this;
        }

        /**
         * @return {@link #version} (Version of IG.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
         */
        public StringType getVersionElement() { 
          if (this.version == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideInputDependsOnComponent.version");
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
         * @param value {@link #version} (Version of IG.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
         */
        public ImplementationGuideInputDependsOnComponent setVersionElement(StringType value) { 
          this.version = value;
          return this;
        }

        /**
         * @return Version of IG.
         */
        public String getVersion() { 
          return this.version == null ? null : this.version.getValue();
        }

        /**
         * @param value Version of IG.
         */
        public ImplementationGuideInputDependsOnComponent setVersion(String value) { 
          if (Utilities.noString(value))
            this.version = null;
          else {
            if (this.version == null)
              this.version = new StringType();
            this.version.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("definition", "Reference(ImplementationGuideOutput)", "The canonical reference of the definition of the IG that is depended on.", 0, 1, definition));
          children.add(new Property("version", "string", "Version of IG.", 0, 1, version));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1014418093: /*definition*/  return new Property("definition", "Reference(ImplementationGuideOutput)", "The canonical reference of the definition of the IG that is depended on.", 0, 1, definition);
          case 351608024: /*version*/  return new Property("version", "string", "Version of IG.", 0, 1, version);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1014418093: /*definition*/ return this.definition == null ? new Base[0] : new Base[] {this.definition}; // Reference
        case 351608024: /*version*/ return this.version == null ? new Base[0] : new Base[] {this.version}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1014418093: // definition
          this.definition = castToReference(value); // Reference
          return value;
        case 351608024: // version
          this.version = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("definition")) {
          this.definition = castToReference(value); // Reference
        } else if (name.equals("version")) {
          this.version = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1014418093:  return getDefinition(); 
        case 351608024:  return getVersionElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1014418093: /*definition*/ return new String[] {"Reference"};
        case 351608024: /*version*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("definition")) {
          this.definition = new Reference();
          return this.definition;
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.version");
        }
        else
          return super.addChild(name);
      }

      public ImplementationGuideInputDependsOnComponent copy() {
        ImplementationGuideInputDependsOnComponent dst = new ImplementationGuideInputDependsOnComponent();
        copyValues(dst);
        dst.definition = definition == null ? null : definition.copy();
        dst.version = version == null ? null : version.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideInputDependsOnComponent))
          return false;
        ImplementationGuideInputDependsOnComponent o = (ImplementationGuideInputDependsOnComponent) other_;
        return compareDeep(definition, o.definition, true) && compareDeep(version, o.version, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideInputDependsOnComponent))
          return false;
        ImplementationGuideInputDependsOnComponent o = (ImplementationGuideInputDependsOnComponent) other_;
        return compareValues(version, o.version, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(definition, version);
      }

  public String fhirType() {
    return "ImplementationGuideInput.dependsOn";

  }

  }

    @Block()
    public static class ImplementationGuideInputGlobalComponent extends BackboneElement implements IBaseBackboneElement {
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
      public ImplementationGuideInputGlobalComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ImplementationGuideInputGlobalComponent(CodeType type, CanonicalType profile) {
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
              throw new Error("Attempt to auto-create ImplementationGuideInputGlobalComponent.type");
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
        public ImplementationGuideInputGlobalComponent setTypeElement(CodeType value) { 
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
        public ImplementationGuideInputGlobalComponent setType(String value) { 
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
              throw new Error("Attempt to auto-create ImplementationGuideInputGlobalComponent.profile");
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
        public ImplementationGuideInputGlobalComponent setProfileElement(CanonicalType value) { 
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
        public ImplementationGuideInputGlobalComponent setProfile(String value) { 
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
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.type");
        }
        else if (name.equals("profile")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.profile");
        }
        else
          return super.addChild(name);
      }

      public ImplementationGuideInputGlobalComponent copy() {
        ImplementationGuideInputGlobalComponent dst = new ImplementationGuideInputGlobalComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.profile = profile == null ? null : profile.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideInputGlobalComponent))
          return false;
        ImplementationGuideInputGlobalComponent o = (ImplementationGuideInputGlobalComponent) other_;
        return compareDeep(type, o.type, true) && compareDeep(profile, o.profile, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideInputGlobalComponent))
          return false;
        ImplementationGuideInputGlobalComponent o = (ImplementationGuideInputGlobalComponent) other_;
        return compareValues(type, o.type, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, profile);
      }

  public String fhirType() {
    return "ImplementationGuideInput.global";

  }

  }

    @Block()
    public static class ImplementationGuideInputDefinitionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A logical group of resources. Logical groups can be used when building pages.
         */
        @Child(name = "package", type = {}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Group of resources as used in .page.package", formalDefinition="A logical group of resources. Logical groups can be used when building pages." )
        protected List<ImplementationGuideInputDefinitionPackageComponent> package_;

        /**
         * A resource that is part of the implementation guide. Conformance resources (value set, structure definition, capability statements etc.) are obvious candidates for inclusion, but any kind of resource can be included as an example resource.
         */
        @Child(name = "resource", type = {}, order=2, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Resource in the implementation guide", formalDefinition="A resource that is part of the implementation guide. Conformance resources (value set, structure definition, capability statements etc.) are obvious candidates for inclusion, but any kind of resource can be included as an example resource." )
        protected List<ImplementationGuideInputDefinitionResourceComponent> resource;

        /**
         * A page / section in the implementation guide. The root page is the implementation guide home page.
         */
        @Child(name = "page", type = {}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Page/Section in the Guide", formalDefinition="A page / section in the implementation guide. The root page is the implementation guide home page." )
        protected ImplementationGuideInputDefinitionPageComponent page;

        /**
         * Defines how IG is built by tools.
         */
        @Child(name = "parameter", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Defines how IG is built by tools", formalDefinition="Defines how IG is built by tools." )
        protected List<ImplementationGuideInputDefinitionParameterComponent> parameter;

        /**
         * A template for building resources.
         */
        @Child(name = "template", type = {}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="A template for building resources", formalDefinition="A template for building resources." )
        protected List<ImplementationGuideInputDefinitionTemplateComponent> template;

        private static final long serialVersionUID = 1165608405L;

    /**
     * Constructor
     */
      public ImplementationGuideInputDefinitionComponent() {
        super();
      }

        /**
         * @return {@link #package_} (A logical group of resources. Logical groups can be used when building pages.)
         */
        public List<ImplementationGuideInputDefinitionPackageComponent> getPackage() { 
          if (this.package_ == null)
            this.package_ = new ArrayList<ImplementationGuideInputDefinitionPackageComponent>();
          return this.package_;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ImplementationGuideInputDefinitionComponent setPackage(List<ImplementationGuideInputDefinitionPackageComponent> thePackage) { 
          this.package_ = thePackage;
          return this;
        }

        public boolean hasPackage() { 
          if (this.package_ == null)
            return false;
          for (ImplementationGuideInputDefinitionPackageComponent item : this.package_)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ImplementationGuideInputDefinitionPackageComponent addPackage() { //3
          ImplementationGuideInputDefinitionPackageComponent t = new ImplementationGuideInputDefinitionPackageComponent();
          if (this.package_ == null)
            this.package_ = new ArrayList<ImplementationGuideInputDefinitionPackageComponent>();
          this.package_.add(t);
          return t;
        }

        public ImplementationGuideInputDefinitionComponent addPackage(ImplementationGuideInputDefinitionPackageComponent t) { //3
          if (t == null)
            return this;
          if (this.package_ == null)
            this.package_ = new ArrayList<ImplementationGuideInputDefinitionPackageComponent>();
          this.package_.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #package_}, creating it if it does not already exist
         */
        public ImplementationGuideInputDefinitionPackageComponent getPackageFirstRep() { 
          if (getPackage().isEmpty()) {
            addPackage();
          }
          return getPackage().get(0);
        }

        /**
         * @return {@link #resource} (A resource that is part of the implementation guide. Conformance resources (value set, structure definition, capability statements etc.) are obvious candidates for inclusion, but any kind of resource can be included as an example resource.)
         */
        public List<ImplementationGuideInputDefinitionResourceComponent> getResource() { 
          if (this.resource == null)
            this.resource = new ArrayList<ImplementationGuideInputDefinitionResourceComponent>();
          return this.resource;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ImplementationGuideInputDefinitionComponent setResource(List<ImplementationGuideInputDefinitionResourceComponent> theResource) { 
          this.resource = theResource;
          return this;
        }

        public boolean hasResource() { 
          if (this.resource == null)
            return false;
          for (ImplementationGuideInputDefinitionResourceComponent item : this.resource)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ImplementationGuideInputDefinitionResourceComponent addResource() { //3
          ImplementationGuideInputDefinitionResourceComponent t = new ImplementationGuideInputDefinitionResourceComponent();
          if (this.resource == null)
            this.resource = new ArrayList<ImplementationGuideInputDefinitionResourceComponent>();
          this.resource.add(t);
          return t;
        }

        public ImplementationGuideInputDefinitionComponent addResource(ImplementationGuideInputDefinitionResourceComponent t) { //3
          if (t == null)
            return this;
          if (this.resource == null)
            this.resource = new ArrayList<ImplementationGuideInputDefinitionResourceComponent>();
          this.resource.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #resource}, creating it if it does not already exist
         */
        public ImplementationGuideInputDefinitionResourceComponent getResourceFirstRep() { 
          if (getResource().isEmpty()) {
            addResource();
          }
          return getResource().get(0);
        }

        /**
         * @return {@link #page} (A page / section in the implementation guide. The root page is the implementation guide home page.)
         */
        public ImplementationGuideInputDefinitionPageComponent getPage() { 
          if (this.page == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideInputDefinitionComponent.page");
            else if (Configuration.doAutoCreate())
              this.page = new ImplementationGuideInputDefinitionPageComponent(); // cc
          return this.page;
        }

        public boolean hasPage() { 
          return this.page != null && !this.page.isEmpty();
        }

        /**
         * @param value {@link #page} (A page / section in the implementation guide. The root page is the implementation guide home page.)
         */
        public ImplementationGuideInputDefinitionComponent setPage(ImplementationGuideInputDefinitionPageComponent value) { 
          this.page = value;
          return this;
        }

        /**
         * @return {@link #parameter} (Defines how IG is built by tools.)
         */
        public List<ImplementationGuideInputDefinitionParameterComponent> getParameter() { 
          if (this.parameter == null)
            this.parameter = new ArrayList<ImplementationGuideInputDefinitionParameterComponent>();
          return this.parameter;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ImplementationGuideInputDefinitionComponent setParameter(List<ImplementationGuideInputDefinitionParameterComponent> theParameter) { 
          this.parameter = theParameter;
          return this;
        }

        public boolean hasParameter() { 
          if (this.parameter == null)
            return false;
          for (ImplementationGuideInputDefinitionParameterComponent item : this.parameter)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ImplementationGuideInputDefinitionParameterComponent addParameter() { //3
          ImplementationGuideInputDefinitionParameterComponent t = new ImplementationGuideInputDefinitionParameterComponent();
          if (this.parameter == null)
            this.parameter = new ArrayList<ImplementationGuideInputDefinitionParameterComponent>();
          this.parameter.add(t);
          return t;
        }

        public ImplementationGuideInputDefinitionComponent addParameter(ImplementationGuideInputDefinitionParameterComponent t) { //3
          if (t == null)
            return this;
          if (this.parameter == null)
            this.parameter = new ArrayList<ImplementationGuideInputDefinitionParameterComponent>();
          this.parameter.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #parameter}, creating it if it does not already exist
         */
        public ImplementationGuideInputDefinitionParameterComponent getParameterFirstRep() { 
          if (getParameter().isEmpty()) {
            addParameter();
          }
          return getParameter().get(0);
        }

        /**
         * @return {@link #template} (A template for building resources.)
         */
        public List<ImplementationGuideInputDefinitionTemplateComponent> getTemplate() { 
          if (this.template == null)
            this.template = new ArrayList<ImplementationGuideInputDefinitionTemplateComponent>();
          return this.template;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ImplementationGuideInputDefinitionComponent setTemplate(List<ImplementationGuideInputDefinitionTemplateComponent> theTemplate) { 
          this.template = theTemplate;
          return this;
        }

        public boolean hasTemplate() { 
          if (this.template == null)
            return false;
          for (ImplementationGuideInputDefinitionTemplateComponent item : this.template)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ImplementationGuideInputDefinitionTemplateComponent addTemplate() { //3
          ImplementationGuideInputDefinitionTemplateComponent t = new ImplementationGuideInputDefinitionTemplateComponent();
          if (this.template == null)
            this.template = new ArrayList<ImplementationGuideInputDefinitionTemplateComponent>();
          this.template.add(t);
          return t;
        }

        public ImplementationGuideInputDefinitionComponent addTemplate(ImplementationGuideInputDefinitionTemplateComponent t) { //3
          if (t == null)
            return this;
          if (this.template == null)
            this.template = new ArrayList<ImplementationGuideInputDefinitionTemplateComponent>();
          this.template.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #template}, creating it if it does not already exist
         */
        public ImplementationGuideInputDefinitionTemplateComponent getTemplateFirstRep() { 
          if (getTemplate().isEmpty()) {
            addTemplate();
          }
          return getTemplate().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("package", "", "A logical group of resources. Logical groups can be used when building pages.", 0, java.lang.Integer.MAX_VALUE, package_));
          children.add(new Property("resource", "", "A resource that is part of the implementation guide. Conformance resources (value set, structure definition, capability statements etc.) are obvious candidates for inclusion, but any kind of resource can be included as an example resource.", 0, java.lang.Integer.MAX_VALUE, resource));
          children.add(new Property("page", "", "A page / section in the implementation guide. The root page is the implementation guide home page.", 0, 1, page));
          children.add(new Property("parameter", "", "Defines how IG is built by tools.", 0, java.lang.Integer.MAX_VALUE, parameter));
          children.add(new Property("template", "", "A template for building resources.", 0, java.lang.Integer.MAX_VALUE, template));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -807062458: /*package*/  return new Property("package", "", "A logical group of resources. Logical groups can be used when building pages.", 0, java.lang.Integer.MAX_VALUE, package_);
          case -341064690: /*resource*/  return new Property("resource", "", "A resource that is part of the implementation guide. Conformance resources (value set, structure definition, capability statements etc.) are obvious candidates for inclusion, but any kind of resource can be included as an example resource.", 0, java.lang.Integer.MAX_VALUE, resource);
          case 3433103: /*page*/  return new Property("page", "", "A page / section in the implementation guide. The root page is the implementation guide home page.", 0, 1, page);
          case 1954460585: /*parameter*/  return new Property("parameter", "", "Defines how IG is built by tools.", 0, java.lang.Integer.MAX_VALUE, parameter);
          case -1321546630: /*template*/  return new Property("template", "", "A template for building resources.", 0, java.lang.Integer.MAX_VALUE, template);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -807062458: /*package*/ return this.package_ == null ? new Base[0] : this.package_.toArray(new Base[this.package_.size()]); // ImplementationGuideInputDefinitionPackageComponent
        case -341064690: /*resource*/ return this.resource == null ? new Base[0] : this.resource.toArray(new Base[this.resource.size()]); // ImplementationGuideInputDefinitionResourceComponent
        case 3433103: /*page*/ return this.page == null ? new Base[0] : new Base[] {this.page}; // ImplementationGuideInputDefinitionPageComponent
        case 1954460585: /*parameter*/ return this.parameter == null ? new Base[0] : this.parameter.toArray(new Base[this.parameter.size()]); // ImplementationGuideInputDefinitionParameterComponent
        case -1321546630: /*template*/ return this.template == null ? new Base[0] : this.template.toArray(new Base[this.template.size()]); // ImplementationGuideInputDefinitionTemplateComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -807062458: // package
          this.getPackage().add((ImplementationGuideInputDefinitionPackageComponent) value); // ImplementationGuideInputDefinitionPackageComponent
          return value;
        case -341064690: // resource
          this.getResource().add((ImplementationGuideInputDefinitionResourceComponent) value); // ImplementationGuideInputDefinitionResourceComponent
          return value;
        case 3433103: // page
          this.page = (ImplementationGuideInputDefinitionPageComponent) value; // ImplementationGuideInputDefinitionPageComponent
          return value;
        case 1954460585: // parameter
          this.getParameter().add((ImplementationGuideInputDefinitionParameterComponent) value); // ImplementationGuideInputDefinitionParameterComponent
          return value;
        case -1321546630: // template
          this.getTemplate().add((ImplementationGuideInputDefinitionTemplateComponent) value); // ImplementationGuideInputDefinitionTemplateComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("package")) {
          this.getPackage().add((ImplementationGuideInputDefinitionPackageComponent) value);
        } else if (name.equals("resource")) {
          this.getResource().add((ImplementationGuideInputDefinitionResourceComponent) value);
        } else if (name.equals("page")) {
          this.page = (ImplementationGuideInputDefinitionPageComponent) value; // ImplementationGuideInputDefinitionPageComponent
        } else if (name.equals("parameter")) {
          this.getParameter().add((ImplementationGuideInputDefinitionParameterComponent) value);
        } else if (name.equals("template")) {
          this.getTemplate().add((ImplementationGuideInputDefinitionTemplateComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -807062458:  return addPackage(); 
        case -341064690:  return addResource(); 
        case 3433103:  return getPage(); 
        case 1954460585:  return addParameter(); 
        case -1321546630:  return addTemplate(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -807062458: /*package*/ return new String[] {};
        case -341064690: /*resource*/ return new String[] {};
        case 3433103: /*page*/ return new String[] {};
        case 1954460585: /*parameter*/ return new String[] {};
        case -1321546630: /*template*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("package")) {
          return addPackage();
        }
        else if (name.equals("resource")) {
          return addResource();
        }
        else if (name.equals("page")) {
          this.page = new ImplementationGuideInputDefinitionPageComponent();
          return this.page;
        }
        else if (name.equals("parameter")) {
          return addParameter();
        }
        else if (name.equals("template")) {
          return addTemplate();
        }
        else
          return super.addChild(name);
      }

      public ImplementationGuideInputDefinitionComponent copy() {
        ImplementationGuideInputDefinitionComponent dst = new ImplementationGuideInputDefinitionComponent();
        copyValues(dst);
        if (package_ != null) {
          dst.package_ = new ArrayList<ImplementationGuideInputDefinitionPackageComponent>();
          for (ImplementationGuideInputDefinitionPackageComponent i : package_)
            dst.package_.add(i.copy());
        };
        if (resource != null) {
          dst.resource = new ArrayList<ImplementationGuideInputDefinitionResourceComponent>();
          for (ImplementationGuideInputDefinitionResourceComponent i : resource)
            dst.resource.add(i.copy());
        };
        dst.page = page == null ? null : page.copy();
        if (parameter != null) {
          dst.parameter = new ArrayList<ImplementationGuideInputDefinitionParameterComponent>();
          for (ImplementationGuideInputDefinitionParameterComponent i : parameter)
            dst.parameter.add(i.copy());
        };
        if (template != null) {
          dst.template = new ArrayList<ImplementationGuideInputDefinitionTemplateComponent>();
          for (ImplementationGuideInputDefinitionTemplateComponent i : template)
            dst.template.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideInputDefinitionComponent))
          return false;
        ImplementationGuideInputDefinitionComponent o = (ImplementationGuideInputDefinitionComponent) other_;
        return compareDeep(package_, o.package_, true) && compareDeep(resource, o.resource, true) && compareDeep(page, o.page, true)
           && compareDeep(parameter, o.parameter, true) && compareDeep(template, o.template, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideInputDefinitionComponent))
          return false;
        ImplementationGuideInputDefinitionComponent o = (ImplementationGuideInputDefinitionComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(package_, resource, page
          , parameter, template);
      }

  public String fhirType() {
    return "ImplementationGuideInput.definition";

  }

  }

    @Block()
    public static class ImplementationGuideInputDefinitionPackageComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The name for the group, as used in page.package.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Name used .page.package", formalDefinition="The name for the group, as used in page.package." )
        protected StringType name;

        /**
         * Human readable text describing the package.
         */
        @Child(name = "description", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Human readable text describing the package", formalDefinition="Human readable text describing the package." )
        protected StringType description;

        private static final long serialVersionUID = -1105523499L;

    /**
     * Constructor
     */
      public ImplementationGuideInputDefinitionPackageComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ImplementationGuideInputDefinitionPackageComponent(StringType name) {
        super();
        this.name = name;
      }

        /**
         * @return {@link #name} (The name for the group, as used in page.package.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideInputDefinitionPackageComponent.name");
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
         * @param value {@link #name} (The name for the group, as used in page.package.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public ImplementationGuideInputDefinitionPackageComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return The name for the group, as used in page.package.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value The name for the group, as used in page.package.
         */
        public ImplementationGuideInputDefinitionPackageComponent setName(String value) { 
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          return this;
        }

        /**
         * @return {@link #description} (Human readable text describing the package.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideInputDefinitionPackageComponent.description");
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
         * @param value {@link #description} (Human readable text describing the package.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public ImplementationGuideInputDefinitionPackageComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return Human readable text describing the package.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value Human readable text describing the package.
         */
        public ImplementationGuideInputDefinitionPackageComponent setDescription(String value) { 
          if (Utilities.noString(value))
            this.description = null;
          else {
            if (this.description == null)
              this.description = new StringType();
            this.description.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("name", "string", "The name for the group, as used in page.package.", 0, 1, name));
          children.add(new Property("description", "string", "Human readable text describing the package.", 0, 1, description));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3373707: /*name*/  return new Property("name", "string", "The name for the group, as used in page.package.", 0, 1, name);
          case -1724546052: /*description*/  return new Property("description", "string", "Human readable text describing the package.", 0, 1, description);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707:  return getNameElement();
        case -1724546052:  return getDescriptionElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return new String[] {"string"};
        case -1724546052: /*description*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.name");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.description");
        }
        else
          return super.addChild(name);
      }

      public ImplementationGuideInputDefinitionPackageComponent copy() {
        ImplementationGuideInputDefinitionPackageComponent dst = new ImplementationGuideInputDefinitionPackageComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.description = description == null ? null : description.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideInputDefinitionPackageComponent))
          return false;
        ImplementationGuideInputDefinitionPackageComponent o = (ImplementationGuideInputDefinitionPackageComponent) other_;
        return compareDeep(name, o.name, true) && compareDeep(description, o.description, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideInputDefinitionPackageComponent))
          return false;
        ImplementationGuideInputDefinitionPackageComponent o = (ImplementationGuideInputDefinitionPackageComponent) other_;
        return compareValues(name, o.name, true) && compareValues(description, o.description, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(name, description);
      }

  public String fhirType() {
    return "ImplementationGuideInput.definition.package";

  }

  }

    @Block()
    public static class ImplementationGuideInputDefinitionResourceComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Where this resource is found.
         */
        @Child(name = "reference", type = {Reference.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Location of the resource", formalDefinition="Where this resource is found." )
        protected Reference reference;

        /**
         * The actual object that is the target of the reference (Where this resource is found.)
         */
        protected Resource referenceTarget;

        /**
         * A human assigned name for the resource. All resources SHOULD have a name, but the name may be extracted from the resource (e.g. ValueSet.name).
         */
        @Child(name = "name", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Human Name for the resource", formalDefinition="A human assigned name for the resource. All resources SHOULD have a name, but the name may be extracted from the resource (e.g. ValueSet.name)." )
        protected StringType name;

        /**
         * A description of the reason that a resource has been included in the implementation guide.
         */
        @Child(name = "description", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Reason why included in guide", formalDefinition="A description of the reason that a resource has been included in the implementation guide." )
        protected StringType description;

        /**
         * If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.
         */
        @Child(name = "example", type = {BooleanType.class, StructureDefinition.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Is an example/What is this an example of?", formalDefinition="If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile." )
        protected Type example;

        /**
         * Reference to the id of the pack this resource appears in.
         */
        @Child(name = "package", type = {IdType.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Pack this is part of", formalDefinition="Reference to the id of the pack this resource appears in." )
        protected IdType package_;

        private static final long serialVersionUID = 1199251259L;

    /**
     * Constructor
     */
      public ImplementationGuideInputDefinitionResourceComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ImplementationGuideInputDefinitionResourceComponent(Reference reference) {
        super();
        this.reference = reference;
      }

        /**
         * @return {@link #reference} (Where this resource is found.)
         */
        public Reference getReference() { 
          if (this.reference == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideInputDefinitionResourceComponent.reference");
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
        public ImplementationGuideInputDefinitionResourceComponent setReference(Reference value) { 
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
        public ImplementationGuideInputDefinitionResourceComponent setReferenceTarget(Resource value) { 
          this.referenceTarget = value;
          return this;
        }

        /**
         * @return {@link #name} (A human assigned name for the resource. All resources SHOULD have a name, but the name may be extracted from the resource (e.g. ValueSet.name).). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideInputDefinitionResourceComponent.name");
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
         * @param value {@link #name} (A human assigned name for the resource. All resources SHOULD have a name, but the name may be extracted from the resource (e.g. ValueSet.name).). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public ImplementationGuideInputDefinitionResourceComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return A human assigned name for the resource. All resources SHOULD have a name, but the name may be extracted from the resource (e.g. ValueSet.name).
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value A human assigned name for the resource. All resources SHOULD have a name, but the name may be extracted from the resource (e.g. ValueSet.name).
         */
        public ImplementationGuideInputDefinitionResourceComponent setName(String value) { 
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
         * @return {@link #description} (A description of the reason that a resource has been included in the implementation guide.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideInputDefinitionResourceComponent.description");
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
         * @param value {@link #description} (A description of the reason that a resource has been included in the implementation guide.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public ImplementationGuideInputDefinitionResourceComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return A description of the reason that a resource has been included in the implementation guide.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value A description of the reason that a resource has been included in the implementation guide.
         */
        public ImplementationGuideInputDefinitionResourceComponent setDescription(String value) { 
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
        public ImplementationGuideInputDefinitionResourceComponent setExample(Type value) { 
          this.example = value;
          return this;
        }

        /**
         * @return {@link #package_} (Reference to the id of the pack this resource appears in.). This is the underlying object with id, value and extensions. The accessor "getPackage" gives direct access to the value
         */
        public IdType getPackageElement() { 
          if (this.package_ == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideInputDefinitionResourceComponent.package_");
            else if (Configuration.doAutoCreate())
              this.package_ = new IdType(); // bb
          return this.package_;
        }

        public boolean hasPackageElement() { 
          return this.package_ != null && !this.package_.isEmpty();
        }

        public boolean hasPackage() { 
          return this.package_ != null && !this.package_.isEmpty();
        }

        /**
         * @param value {@link #package_} (Reference to the id of the pack this resource appears in.). This is the underlying object with id, value and extensions. The accessor "getPackage" gives direct access to the value
         */
        public ImplementationGuideInputDefinitionResourceComponent setPackageElement(IdType value) { 
          this.package_ = value;
          return this;
        }

        /**
         * @return Reference to the id of the pack this resource appears in.
         */
        public String getPackage() { 
          return this.package_ == null ? null : this.package_.getValue();
        }

        /**
         * @param value Reference to the id of the pack this resource appears in.
         */
        public ImplementationGuideInputDefinitionResourceComponent setPackage(String value) { 
          if (Utilities.noString(value))
            this.package_ = null;
          else {
            if (this.package_ == null)
              this.package_ = new IdType();
            this.package_.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("reference", "Reference(Any)", "Where this resource is found.", 0, 1, reference));
          children.add(new Property("name", "string", "A human assigned name for the resource. All resources SHOULD have a name, but the name may be extracted from the resource (e.g. ValueSet.name).", 0, 1, name));
          children.add(new Property("description", "string", "A description of the reason that a resource has been included in the implementation guide.", 0, 1, description));
          children.add(new Property("example[x]", "boolean|Reference(StructureDefinition)", "If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.", 0, 1, example));
          children.add(new Property("package", "id", "Reference to the id of the pack this resource appears in.", 0, 1, package_));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -925155509: /*reference*/  return new Property("reference", "Reference(Any)", "Where this resource is found.", 0, 1, reference);
          case 3373707: /*name*/  return new Property("name", "string", "A human assigned name for the resource. All resources SHOULD have a name, but the name may be extracted from the resource (e.g. ValueSet.name).", 0, 1, name);
          case -1724546052: /*description*/  return new Property("description", "string", "A description of the reason that a resource has been included in the implementation guide.", 0, 1, description);
          case -2002328874: /*example[x]*/  return new Property("example[x]", "boolean|Reference(StructureDefinition)", "If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.", 0, 1, example);
          case -1322970774: /*example*/  return new Property("example[x]", "boolean|Reference(StructureDefinition)", "If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.", 0, 1, example);
          case 159803230: /*exampleBoolean*/  return new Property("example[x]", "boolean|Reference(StructureDefinition)", "If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.", 0, 1, example);
          case 263472385: /*exampleReference*/  return new Property("example[x]", "boolean|Reference(StructureDefinition)", "If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.", 0, 1, example);
          case -807062458: /*package*/  return new Property("package", "id", "Reference to the id of the pack this resource appears in.", 0, 1, package_);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -925155509: /*reference*/ return this.reference == null ? new Base[0] : new Base[] {this.reference}; // Reference
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case -1322970774: /*example*/ return this.example == null ? new Base[0] : new Base[] {this.example}; // Type
        case -807062458: /*package*/ return this.package_ == null ? new Base[0] : new Base[] {this.package_}; // IdType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -925155509: // reference
          this.reference = castToReference(value); // Reference
          return value;
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        case -1322970774: // example
          this.example = castToType(value); // Type
          return value;
        case -807062458: // package
          this.package_ = castToId(value); // IdType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("reference")) {
          this.reference = castToReference(value); // Reference
        } else if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else if (name.equals("example[x]")) {
          this.example = castToType(value); // Type
        } else if (name.equals("package")) {
          this.package_ = castToId(value); // IdType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -925155509:  return getReference(); 
        case 3373707:  return getNameElement();
        case -1724546052:  return getDescriptionElement();
        case -2002328874:  return getExample(); 
        case -1322970774:  return getExample(); 
        case -807062458:  return getPackageElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -925155509: /*reference*/ return new String[] {"Reference"};
        case 3373707: /*name*/ return new String[] {"string"};
        case -1724546052: /*description*/ return new String[] {"string"};
        case -1322970774: /*example*/ return new String[] {"boolean", "Reference"};
        case -807062458: /*package*/ return new String[] {"id"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("reference")) {
          this.reference = new Reference();
          return this.reference;
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.name");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.description");
        }
        else if (name.equals("exampleBoolean")) {
          this.example = new BooleanType();
          return this.example;
        }
        else if (name.equals("exampleReference")) {
          this.example = new Reference();
          return this.example;
        }
        else if (name.equals("package")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.package");
        }
        else
          return super.addChild(name);
      }

      public ImplementationGuideInputDefinitionResourceComponent copy() {
        ImplementationGuideInputDefinitionResourceComponent dst = new ImplementationGuideInputDefinitionResourceComponent();
        copyValues(dst);
        dst.reference = reference == null ? null : reference.copy();
        dst.name = name == null ? null : name.copy();
        dst.description = description == null ? null : description.copy();
        dst.example = example == null ? null : example.copy();
        dst.package_ = package_ == null ? null : package_.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideInputDefinitionResourceComponent))
          return false;
        ImplementationGuideInputDefinitionResourceComponent o = (ImplementationGuideInputDefinitionResourceComponent) other_;
        return compareDeep(reference, o.reference, true) && compareDeep(name, o.name, true) && compareDeep(description, o.description, true)
           && compareDeep(example, o.example, true) && compareDeep(package_, o.package_, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideInputDefinitionResourceComponent))
          return false;
        ImplementationGuideInputDefinitionResourceComponent o = (ImplementationGuideInputDefinitionResourceComponent) other_;
        return compareValues(name, o.name, true) && compareValues(description, o.description, true) && compareValues(package_, o.package_, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(reference, name, description
          , example, package_);
      }

  public String fhirType() {
    return "ImplementationGuideInput.definition.resource";

  }

  }

    @Block()
    public static class ImplementationGuideInputDefinitionPageComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The source address for the page.
         */
        @Child(name = "name", type = {UriType.class, Binary.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Where to find that page", formalDefinition="The source address for the page." )
        protected Type name;

        /**
         * A short title used to represent this page in navigational structures such as table of contents, bread crumbs, etc.
         */
        @Child(name = "title", type = {StringType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Short title shown for navigational assistance", formalDefinition="A short title used to represent this page in navigational structures such as table of contents, bread crumbs, etc." )
        protected StringType title;

        /**
         * Nested Pages/Sections under this page.
         */
        @Child(name = "page", type = {ImplementationGuideInputDefinitionPageComponent.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Nested Pages / Sections", formalDefinition="Nested Pages/Sections under this page." )
        protected List<ImplementationGuideInputDefinitionPageComponent> page;

        private static final long serialVersionUID = -865550706L;

    /**
     * Constructor
     */
      public ImplementationGuideInputDefinitionPageComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ImplementationGuideInputDefinitionPageComponent(Type name, StringType title) {
        super();
        this.name = name;
        this.title = title;
      }

        /**
         * @return {@link #name} (The source address for the page.)
         */
        public Type getName() { 
          return this.name;
        }

        /**
         * @return {@link #name} (The source address for the page.)
         */
        public UriType getNameUriType() throws FHIRException { 
          if (!(this.name instanceof UriType))
            throw new FHIRException("Type mismatch: the type UriType was expected, but "+this.name.getClass().getName()+" was encountered");
          return (UriType) this.name;
        }

        public boolean hasNameUriType() { 
          return this.name instanceof UriType;
        }

        /**
         * @return {@link #name} (The source address for the page.)
         */
        public Reference getNameReference() throws FHIRException { 
          if (!(this.name instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.name.getClass().getName()+" was encountered");
          return (Reference) this.name;
        }

        public boolean hasNameReference() { 
          return this.name instanceof Reference;
        }

        public boolean hasName() { 
          return this.name != null && !this.name.isEmpty();
        }

        /**
         * @param value {@link #name} (The source address for the page.)
         */
        public ImplementationGuideInputDefinitionPageComponent setName(Type value) { 
          this.name = value;
          return this;
        }

        /**
         * @return {@link #title} (A short title used to represent this page in navigational structures such as table of contents, bread crumbs, etc.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
         */
        public StringType getTitleElement() { 
          if (this.title == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideInputDefinitionPageComponent.title");
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
         * @param value {@link #title} (A short title used to represent this page in navigational structures such as table of contents, bread crumbs, etc.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
         */
        public ImplementationGuideInputDefinitionPageComponent setTitleElement(StringType value) { 
          this.title = value;
          return this;
        }

        /**
         * @return A short title used to represent this page in navigational structures such as table of contents, bread crumbs, etc.
         */
        public String getTitle() { 
          return this.title == null ? null : this.title.getValue();
        }

        /**
         * @param value A short title used to represent this page in navigational structures such as table of contents, bread crumbs, etc.
         */
        public ImplementationGuideInputDefinitionPageComponent setTitle(String value) { 
            if (this.title == null)
              this.title = new StringType();
            this.title.setValue(value);
          return this;
        }

        /**
         * @return {@link #page} (Nested Pages/Sections under this page.)
         */
        public List<ImplementationGuideInputDefinitionPageComponent> getPage() { 
          if (this.page == null)
            this.page = new ArrayList<ImplementationGuideInputDefinitionPageComponent>();
          return this.page;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ImplementationGuideInputDefinitionPageComponent setPage(List<ImplementationGuideInputDefinitionPageComponent> thePage) { 
          this.page = thePage;
          return this;
        }

        public boolean hasPage() { 
          if (this.page == null)
            return false;
          for (ImplementationGuideInputDefinitionPageComponent item : this.page)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ImplementationGuideInputDefinitionPageComponent addPage() { //3
          ImplementationGuideInputDefinitionPageComponent t = new ImplementationGuideInputDefinitionPageComponent();
          if (this.page == null)
            this.page = new ArrayList<ImplementationGuideInputDefinitionPageComponent>();
          this.page.add(t);
          return t;
        }

        public ImplementationGuideInputDefinitionPageComponent addPage(ImplementationGuideInputDefinitionPageComponent t) { //3
          if (t == null)
            return this;
          if (this.page == null)
            this.page = new ArrayList<ImplementationGuideInputDefinitionPageComponent>();
          this.page.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #page}, creating it if it does not already exist
         */
        public ImplementationGuideInputDefinitionPageComponent getPageFirstRep() { 
          if (getPage().isEmpty()) {
            addPage();
          }
          return getPage().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("name[x]", "uri|Reference(Binary)", "The source address for the page.", 0, 1, name));
          children.add(new Property("title", "string", "A short title used to represent this page in navigational structures such as table of contents, bread crumbs, etc.", 0, 1, title));
          children.add(new Property("page", "@ImplementationGuideInput.definition.page", "Nested Pages/Sections under this page.", 0, java.lang.Integer.MAX_VALUE, page));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 1721948693: /*name[x]*/  return new Property("name[x]", "uri|Reference(Binary)", "The source address for the page.", 0, 1, name);
          case 3373707: /*name*/  return new Property("name[x]", "uri|Reference(Binary)", "The source address for the page.", 0, 1, name);
          case 1721942753: /*nameUri*/  return new Property("name[x]", "uri|Reference(Binary)", "The source address for the page.", 0, 1, name);
          case 1833144576: /*nameReference*/  return new Property("name[x]", "uri|Reference(Binary)", "The source address for the page.", 0, 1, name);
          case 110371416: /*title*/  return new Property("title", "string", "A short title used to represent this page in navigational structures such as table of contents, bread crumbs, etc.", 0, 1, title);
          case 3433103: /*page*/  return new Property("page", "@ImplementationGuideInput.definition.page", "Nested Pages/Sections under this page.", 0, java.lang.Integer.MAX_VALUE, page);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // Type
        case 110371416: /*title*/ return this.title == null ? new Base[0] : new Base[] {this.title}; // StringType
        case 3433103: /*page*/ return this.page == null ? new Base[0] : this.page.toArray(new Base[this.page.size()]); // ImplementationGuideInputDefinitionPageComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToType(value); // Type
          return value;
        case 110371416: // title
          this.title = castToString(value); // StringType
          return value;
        case 3433103: // page
          this.getPage().add((ImplementationGuideInputDefinitionPageComponent) value); // ImplementationGuideInputDefinitionPageComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name[x]")) {
          this.name = castToType(value); // Type
        } else if (name.equals("title")) {
          this.title = castToString(value); // StringType
        } else if (name.equals("page")) {
          this.getPage().add((ImplementationGuideInputDefinitionPageComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1721948693:  return getName(); 
        case 3373707:  return getName(); 
        case 110371416:  return getTitleElement();
        case 3433103:  return addPage(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return new String[] {"uri", "Reference"};
        case 110371416: /*title*/ return new String[] {"string"};
        case 3433103: /*page*/ return new String[] {"@ImplementationGuideInput.definition.page"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("nameUri")) {
          this.name = new UriType();
          return this.name;
        }
        else if (name.equals("nameReference")) {
          this.name = new Reference();
          return this.name;
        }
        else if (name.equals("title")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.title");
        }
        else if (name.equals("page")) {
          return addPage();
        }
        else
          return super.addChild(name);
      }

      public ImplementationGuideInputDefinitionPageComponent copy() {
        ImplementationGuideInputDefinitionPageComponent dst = new ImplementationGuideInputDefinitionPageComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.title = title == null ? null : title.copy();
        if (page != null) {
          dst.page = new ArrayList<ImplementationGuideInputDefinitionPageComponent>();
          for (ImplementationGuideInputDefinitionPageComponent i : page)
            dst.page.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideInputDefinitionPageComponent))
          return false;
        ImplementationGuideInputDefinitionPageComponent o = (ImplementationGuideInputDefinitionPageComponent) other_;
        return compareDeep(name, o.name, true) && compareDeep(title, o.title, true) && compareDeep(page, o.page, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideInputDefinitionPageComponent))
          return false;
        ImplementationGuideInputDefinitionPageComponent o = (ImplementationGuideInputDefinitionPageComponent) other_;
        return compareValues(title, o.title, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(name, title, page);
      }

  public String fhirType() {
    return "ImplementationGuideInput.definition.page";

  }

  }

    @Block()
    public static class ImplementationGuideInputDefinitionParameterComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * apply-business-version | apply-jurisdiction | path-resource | path-pages | path-tx-cache | expansion-profile | rule-broken-links | generate-xml | generate-json | generate-turtle | html-template.
         */
        @Child(name = "code", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="apply-business-version | apply-jurisdiction | path-resource | path-pages | path-tx-cache | expansion-profile | rule-broken-links | generate-xml | generate-json | generate-turtle | html-template", formalDefinition="apply-business-version | apply-jurisdiction | path-resource | path-pages | path-tx-cache | expansion-profile | rule-broken-links | generate-xml | generate-json | generate-turtle | html-template." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/guide-parameter-code")
        protected Enumeration<GuideParameterCode> code;

        /**
         * Value for named type.
         */
        @Child(name = "value", type = {StringType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Value for named type", formalDefinition="Value for named type." )
        protected StringType value;

        private static final long serialVersionUID = 1188999138L;

    /**
     * Constructor
     */
      public ImplementationGuideInputDefinitionParameterComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ImplementationGuideInputDefinitionParameterComponent(Enumeration<GuideParameterCode> code, StringType value) {
        super();
        this.code = code;
        this.value = value;
      }

        /**
         * @return {@link #code} (apply-business-version | apply-jurisdiction | path-resource | path-pages | path-tx-cache | expansion-profile | rule-broken-links | generate-xml | generate-json | generate-turtle | html-template.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public Enumeration<GuideParameterCode> getCodeElement() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideInputDefinitionParameterComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new Enumeration<GuideParameterCode>(new GuideParameterCodeEnumFactory()); // bb
          return this.code;
        }

        public boolean hasCodeElement() { 
          return this.code != null && !this.code.isEmpty();
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (apply-business-version | apply-jurisdiction | path-resource | path-pages | path-tx-cache | expansion-profile | rule-broken-links | generate-xml | generate-json | generate-turtle | html-template.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public ImplementationGuideInputDefinitionParameterComponent setCodeElement(Enumeration<GuideParameterCode> value) { 
          this.code = value;
          return this;
        }

        /**
         * @return apply-business-version | apply-jurisdiction | path-resource | path-pages | path-tx-cache | expansion-profile | rule-broken-links | generate-xml | generate-json | generate-turtle | html-template.
         */
        public GuideParameterCode getCode() { 
          return this.code == null ? null : this.code.getValue();
        }

        /**
         * @param value apply-business-version | apply-jurisdiction | path-resource | path-pages | path-tx-cache | expansion-profile | rule-broken-links | generate-xml | generate-json | generate-turtle | html-template.
         */
        public ImplementationGuideInputDefinitionParameterComponent setCode(GuideParameterCode value) { 
            if (this.code == null)
              this.code = new Enumeration<GuideParameterCode>(new GuideParameterCodeEnumFactory());
            this.code.setValue(value);
          return this;
        }

        /**
         * @return {@link #value} (Value for named type.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public StringType getValueElement() { 
          if (this.value == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideInputDefinitionParameterComponent.value");
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
         * @param value {@link #value} (Value for named type.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public ImplementationGuideInputDefinitionParameterComponent setValueElement(StringType value) { 
          this.value = value;
          return this;
        }

        /**
         * @return Value for named type.
         */
        public String getValue() { 
          return this.value == null ? null : this.value.getValue();
        }

        /**
         * @param value Value for named type.
         */
        public ImplementationGuideInputDefinitionParameterComponent setValue(String value) { 
            if (this.value == null)
              this.value = new StringType();
            this.value.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("code", "code", "apply-business-version | apply-jurisdiction | path-resource | path-pages | path-tx-cache | expansion-profile | rule-broken-links | generate-xml | generate-json | generate-turtle | html-template.", 0, 1, code));
          children.add(new Property("value", "string", "Value for named type.", 0, 1, value));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3059181: /*code*/  return new Property("code", "code", "apply-business-version | apply-jurisdiction | path-resource | path-pages | path-tx-cache | expansion-profile | rule-broken-links | generate-xml | generate-json | generate-turtle | html-template.", 0, 1, code);
          case 111972721: /*value*/  return new Property("value", "string", "Value for named type.", 0, 1, value);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // Enumeration<GuideParameterCode>
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3059181: // code
          value = new GuideParameterCodeEnumFactory().fromType(castToCode(value));
          this.code = (Enumeration) value; // Enumeration<GuideParameterCode>
          return value;
        case 111972721: // value
          this.value = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code")) {
          value = new GuideParameterCodeEnumFactory().fromType(castToCode(value));
          this.code = (Enumeration) value; // Enumeration<GuideParameterCode>
        } else if (name.equals("value")) {
          this.value = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181:  return getCodeElement();
        case 111972721:  return getValueElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return new String[] {"code"};
        case 111972721: /*value*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.code");
        }
        else if (name.equals("value")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.value");
        }
        else
          return super.addChild(name);
      }

      public ImplementationGuideInputDefinitionParameterComponent copy() {
        ImplementationGuideInputDefinitionParameterComponent dst = new ImplementationGuideInputDefinitionParameterComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideInputDefinitionParameterComponent))
          return false;
        ImplementationGuideInputDefinitionParameterComponent o = (ImplementationGuideInputDefinitionParameterComponent) other_;
        return compareDeep(code, o.code, true) && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideInputDefinitionParameterComponent))
          return false;
        ImplementationGuideInputDefinitionParameterComponent o = (ImplementationGuideInputDefinitionParameterComponent) other_;
        return compareValues(code, o.code, true) && compareValues(value, o.value, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(code, value);
      }

  public String fhirType() {
    return "ImplementationGuideInput.definition.parameter";

  }

  }

    @Block()
    public static class ImplementationGuideInputDefinitionTemplateComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Type of template specified.
         */
        @Child(name = "code", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Type of template specified", formalDefinition="Type of template specified." )
        protected CodeType code;

        /**
         * The source of the template.
         */
        @Child(name = "source", type = {StringType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The source of the template", formalDefinition="The source of the template." )
        protected StringType source;

        /**
         * The scope in which the template applies.
         */
        @Child(name = "scope", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The scope in which the template applies", formalDefinition="The scope in which the template applies." )
        protected StringType scope;

        private static final long serialVersionUID = 923832457L;

    /**
     * Constructor
     */
      public ImplementationGuideInputDefinitionTemplateComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ImplementationGuideInputDefinitionTemplateComponent(CodeType code, StringType source) {
        super();
        this.code = code;
        this.source = source;
      }

        /**
         * @return {@link #code} (Type of template specified.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public CodeType getCodeElement() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideInputDefinitionTemplateComponent.code");
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
         * @param value {@link #code} (Type of template specified.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public ImplementationGuideInputDefinitionTemplateComponent setCodeElement(CodeType value) { 
          this.code = value;
          return this;
        }

        /**
         * @return Type of template specified.
         */
        public String getCode() { 
          return this.code == null ? null : this.code.getValue();
        }

        /**
         * @param value Type of template specified.
         */
        public ImplementationGuideInputDefinitionTemplateComponent setCode(String value) { 
            if (this.code == null)
              this.code = new CodeType();
            this.code.setValue(value);
          return this;
        }

        /**
         * @return {@link #source} (The source of the template.). This is the underlying object with id, value and extensions. The accessor "getSource" gives direct access to the value
         */
        public StringType getSourceElement() { 
          if (this.source == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideInputDefinitionTemplateComponent.source");
            else if (Configuration.doAutoCreate())
              this.source = new StringType(); // bb
          return this.source;
        }

        public boolean hasSourceElement() { 
          return this.source != null && !this.source.isEmpty();
        }

        public boolean hasSource() { 
          return this.source != null && !this.source.isEmpty();
        }

        /**
         * @param value {@link #source} (The source of the template.). This is the underlying object with id, value and extensions. The accessor "getSource" gives direct access to the value
         */
        public ImplementationGuideInputDefinitionTemplateComponent setSourceElement(StringType value) { 
          this.source = value;
          return this;
        }

        /**
         * @return The source of the template.
         */
        public String getSource() { 
          return this.source == null ? null : this.source.getValue();
        }

        /**
         * @param value The source of the template.
         */
        public ImplementationGuideInputDefinitionTemplateComponent setSource(String value) { 
            if (this.source == null)
              this.source = new StringType();
            this.source.setValue(value);
          return this;
        }

        /**
         * @return {@link #scope} (The scope in which the template applies.). This is the underlying object with id, value and extensions. The accessor "getScope" gives direct access to the value
         */
        public StringType getScopeElement() { 
          if (this.scope == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideInputDefinitionTemplateComponent.scope");
            else if (Configuration.doAutoCreate())
              this.scope = new StringType(); // bb
          return this.scope;
        }

        public boolean hasScopeElement() { 
          return this.scope != null && !this.scope.isEmpty();
        }

        public boolean hasScope() { 
          return this.scope != null && !this.scope.isEmpty();
        }

        /**
         * @param value {@link #scope} (The scope in which the template applies.). This is the underlying object with id, value and extensions. The accessor "getScope" gives direct access to the value
         */
        public ImplementationGuideInputDefinitionTemplateComponent setScopeElement(StringType value) { 
          this.scope = value;
          return this;
        }

        /**
         * @return The scope in which the template applies.
         */
        public String getScope() { 
          return this.scope == null ? null : this.scope.getValue();
        }

        /**
         * @param value The scope in which the template applies.
         */
        public ImplementationGuideInputDefinitionTemplateComponent setScope(String value) { 
          if (Utilities.noString(value))
            this.scope = null;
          else {
            if (this.scope == null)
              this.scope = new StringType();
            this.scope.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("code", "code", "Type of template specified.", 0, 1, code));
          children.add(new Property("source", "string", "The source of the template.", 0, 1, source));
          children.add(new Property("scope", "string", "The scope in which the template applies.", 0, 1, scope));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3059181: /*code*/  return new Property("code", "code", "Type of template specified.", 0, 1, code);
          case -896505829: /*source*/  return new Property("source", "string", "The source of the template.", 0, 1, source);
          case 109264468: /*scope*/  return new Property("scope", "string", "The scope in which the template applies.", 0, 1, scope);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeType
        case -896505829: /*source*/ return this.source == null ? new Base[0] : new Base[] {this.source}; // StringType
        case 109264468: /*scope*/ return this.scope == null ? new Base[0] : new Base[] {this.scope}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3059181: // code
          this.code = castToCode(value); // CodeType
          return value;
        case -896505829: // source
          this.source = castToString(value); // StringType
          return value;
        case 109264468: // scope
          this.scope = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code")) {
          this.code = castToCode(value); // CodeType
        } else if (name.equals("source")) {
          this.source = castToString(value); // StringType
        } else if (name.equals("scope")) {
          this.scope = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181:  return getCodeElement();
        case -896505829:  return getSourceElement();
        case 109264468:  return getScopeElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return new String[] {"code"};
        case -896505829: /*source*/ return new String[] {"string"};
        case 109264468: /*scope*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.code");
        }
        else if (name.equals("source")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.source");
        }
        else if (name.equals("scope")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.scope");
        }
        else
          return super.addChild(name);
      }

      public ImplementationGuideInputDefinitionTemplateComponent copy() {
        ImplementationGuideInputDefinitionTemplateComponent dst = new ImplementationGuideInputDefinitionTemplateComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.source = source == null ? null : source.copy();
        dst.scope = scope == null ? null : scope.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideInputDefinitionTemplateComponent))
          return false;
        ImplementationGuideInputDefinitionTemplateComponent o = (ImplementationGuideInputDefinitionTemplateComponent) other_;
        return compareDeep(code, o.code, true) && compareDeep(source, o.source, true) && compareDeep(scope, o.scope, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideInputDefinitionTemplateComponent))
          return false;
        ImplementationGuideInputDefinitionTemplateComponent o = (ImplementationGuideInputDefinitionTemplateComponent) other_;
        return compareValues(code, o.code, true) && compareValues(source, o.source, true) && compareValues(scope, o.scope, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(code, source, scope);
      }

  public String fhirType() {
    return "ImplementationGuideInput.definition.template";

  }

  }

    /**
     * A copyright statement relating to the implementation guide input and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the implementation guide input.
     */
    @Child(name = "copyright", type = {MarkdownType.class}, order=0, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Use and/or publishing restrictions", formalDefinition="A copyright statement relating to the implementation guide input and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the implementation guide input." )
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
    @Child(name = "dependsOn", type = {}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Another Implementation guide this depends on", formalDefinition="Another implementation guide that this implementation depends on. Typically, an implementation guide uses value sets, profiles etc.defined in other implementation guides." )
    protected List<ImplementationGuideInputDependsOnComponent> dependsOn;

    /**
     * A set of profiles that all resources covered by this implementation guide must conform to.
     */
    @Child(name = "global", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Profiles that apply globally", formalDefinition="A set of profiles that all resources covered by this implementation guide must conform to." )
    protected List<ImplementationGuideInputGlobalComponent> global;

    /**
     * Information used when generating the IG.
     */
    @Child(name = "definition", type = {}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Information used when generating the IG", formalDefinition="Information used when generating the IG." )
    protected ImplementationGuideInputDefinitionComponent definition;

    private static final long serialVersionUID = -961018847L;

  /**
   * Constructor
   */
    public ImplementationGuideInput() {
      super();
    }

  /**
   * Constructor
   */
    public ImplementationGuideInput(UriType url, StringType name, Enumeration<PublicationStatus> status) {
      super();
      this.url = url;
      this.name = name;
      this.status = status;
    }

    /**
     * @return {@link #url} (An absolute URI that is used to identify this implementation guide input when it is referenced in a specification, model, design or an instance. This SHOULD be globally unique, and SHOULD be a literal address at which this implementation guide input is (or will be) published.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public UriType getUrlElement() { 
      if (this.url == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuideInput.url");
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
     * @param value {@link #url} (An absolute URI that is used to identify this implementation guide input when it is referenced in a specification, model, design or an instance. This SHOULD be globally unique, and SHOULD be a literal address at which this implementation guide input is (or will be) published.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public ImplementationGuideInput setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return An absolute URI that is used to identify this implementation guide input when it is referenced in a specification, model, design or an instance. This SHOULD be globally unique, and SHOULD be a literal address at which this implementation guide input is (or will be) published.
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value An absolute URI that is used to identify this implementation guide input when it is referenced in a specification, model, design or an instance. This SHOULD be globally unique, and SHOULD be a literal address at which this implementation guide input is (or will be) published.
     */
    public ImplementationGuideInput setUrl(String value) { 
        if (this.url == null)
          this.url = new UriType();
        this.url.setValue(value);
      return this;
    }

    /**
     * @return {@link #version} (The identifier that is used to identify this version of the implementation guide input when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the implementation guide input author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public StringType getVersionElement() { 
      if (this.version == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuideInput.version");
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
     * @param value {@link #version} (The identifier that is used to identify this version of the implementation guide input when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the implementation guide input author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public ImplementationGuideInput setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return The identifier that is used to identify this version of the implementation guide input when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the implementation guide input author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value The identifier that is used to identify this version of the implementation guide input when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the implementation guide input author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.
     */
    public ImplementationGuideInput setVersion(String value) { 
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
     * @return {@link #name} (A natural language name identifying the implementation guide input. This name should be usable as an identifier for the module by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuideInput.name");
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
     * @param value {@link #name} (A natural language name identifying the implementation guide input. This name should be usable as an identifier for the module by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public ImplementationGuideInput setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return A natural language name identifying the implementation guide input. This name should be usable as an identifier for the module by machine processing applications such as code generation.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value A natural language name identifying the implementation guide input. This name should be usable as an identifier for the module by machine processing applications such as code generation.
     */
    public ImplementationGuideInput setName(String value) { 
        if (this.name == null)
          this.name = new StringType();
        this.name.setValue(value);
      return this;
    }

    /**
     * @return {@link #status} (The status of this implementation guide input. Enables tracking the life-cycle of the content.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<PublicationStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuideInput.status");
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
     * @param value {@link #status} (The status of this implementation guide input. Enables tracking the life-cycle of the content.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public ImplementationGuideInput setStatusElement(Enumeration<PublicationStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of this implementation guide input. Enables tracking the life-cycle of the content.
     */
    public PublicationStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of this implementation guide input. Enables tracking the life-cycle of the content.
     */
    public ImplementationGuideInput setStatus(PublicationStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<PublicationStatus>(new PublicationStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #experimental} (A boolean value to indicate that this implementation guide input is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public BooleanType getExperimentalElement() { 
      if (this.experimental == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuideInput.experimental");
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
     * @param value {@link #experimental} (A boolean value to indicate that this implementation guide input is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public ImplementationGuideInput setExperimentalElement(BooleanType value) { 
      this.experimental = value;
      return this;
    }

    /**
     * @return A boolean value to indicate that this implementation guide input is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public boolean getExperimental() { 
      return this.experimental == null || this.experimental.isEmpty() ? false : this.experimental.getValue();
    }

    /**
     * @param value A boolean value to indicate that this implementation guide input is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public ImplementationGuideInput setExperimental(boolean value) { 
        if (this.experimental == null)
          this.experimental = new BooleanType();
        this.experimental.setValue(value);
      return this;
    }

    /**
     * @return {@link #date} (The date  (and optionally time) when the implementation guide input was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the implementation guide input changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuideInput.date");
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
     * @param value {@link #date} (The date  (and optionally time) when the implementation guide input was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the implementation guide input changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public ImplementationGuideInput setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date  (and optionally time) when the implementation guide input was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the implementation guide input changes.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date  (and optionally time) when the implementation guide input was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the implementation guide input changes.
     */
    public ImplementationGuideInput setDate(Date value) { 
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
     * @return {@link #publisher} (The name of the individual or organization that published the implementation guide input.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public StringType getPublisherElement() { 
      if (this.publisher == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuideInput.publisher");
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
     * @param value {@link #publisher} (The name of the individual or organization that published the implementation guide input.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public ImplementationGuideInput setPublisherElement(StringType value) { 
      this.publisher = value;
      return this;
    }

    /**
     * @return The name of the individual or organization that published the implementation guide input.
     */
    public String getPublisher() { 
      return this.publisher == null ? null : this.publisher.getValue();
    }

    /**
     * @param value The name of the individual or organization that published the implementation guide input.
     */
    public ImplementationGuideInput setPublisher(String value) { 
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
    public ImplementationGuideInput setContact(List<ContactDetail> theContact) { 
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

    public ImplementationGuideInput addContact(ContactDetail t) { //3
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
     * @return {@link #description} (A free text natural language description of the implementation guide input from a consumer's perspective.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public MarkdownType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuideInput.description");
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
     * @param value {@link #description} (A free text natural language description of the implementation guide input from a consumer's perspective.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public ImplementationGuideInput setDescriptionElement(MarkdownType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A free text natural language description of the implementation guide input from a consumer's perspective.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A free text natural language description of the implementation guide input from a consumer's perspective.
     */
    public ImplementationGuideInput setDescription(String value) { 
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
     * @return {@link #useContext} (The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching for appropriate implementation guide input instances.)
     */
    public List<UsageContext> getUseContext() { 
      if (this.useContext == null)
        this.useContext = new ArrayList<UsageContext>();
      return this.useContext;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ImplementationGuideInput setUseContext(List<UsageContext> theUseContext) { 
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

    public ImplementationGuideInput addUseContext(UsageContext t) { //3
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
     * @return {@link #jurisdiction} (A legal or geographic region in which the implementation guide input is intended to be used.)
     */
    public List<CodeableConcept> getJurisdiction() { 
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      return this.jurisdiction;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ImplementationGuideInput setJurisdiction(List<CodeableConcept> theJurisdiction) { 
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

    public ImplementationGuideInput addJurisdiction(CodeableConcept t) { //3
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
     * @return {@link #copyright} (A copyright statement relating to the implementation guide input and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the implementation guide input.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public MarkdownType getCopyrightElement() { 
      if (this.copyright == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuideInput.copyright");
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
     * @param value {@link #copyright} (A copyright statement relating to the implementation guide input and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the implementation guide input.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public ImplementationGuideInput setCopyrightElement(MarkdownType value) { 
      this.copyright = value;
      return this;
    }

    /**
     * @return A copyright statement relating to the implementation guide input and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the implementation guide input.
     */
    public String getCopyright() { 
      return this.copyright == null ? null : this.copyright.getValue();
    }

    /**
     * @param value A copyright statement relating to the implementation guide input and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the implementation guide input.
     */
    public ImplementationGuideInput setCopyright(String value) { 
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
          throw new Error("Attempt to auto-create ImplementationGuideInput.fhirVersion");
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
    public ImplementationGuideInput setFhirVersionElement(IdType value) { 
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
    public ImplementationGuideInput setFhirVersion(String value) { 
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
    public List<ImplementationGuideInputDependsOnComponent> getDependsOn() { 
      if (this.dependsOn == null)
        this.dependsOn = new ArrayList<ImplementationGuideInputDependsOnComponent>();
      return this.dependsOn;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ImplementationGuideInput setDependsOn(List<ImplementationGuideInputDependsOnComponent> theDependsOn) { 
      this.dependsOn = theDependsOn;
      return this;
    }

    public boolean hasDependsOn() { 
      if (this.dependsOn == null)
        return false;
      for (ImplementationGuideInputDependsOnComponent item : this.dependsOn)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ImplementationGuideInputDependsOnComponent addDependsOn() { //3
      ImplementationGuideInputDependsOnComponent t = new ImplementationGuideInputDependsOnComponent();
      if (this.dependsOn == null)
        this.dependsOn = new ArrayList<ImplementationGuideInputDependsOnComponent>();
      this.dependsOn.add(t);
      return t;
    }

    public ImplementationGuideInput addDependsOn(ImplementationGuideInputDependsOnComponent t) { //3
      if (t == null)
        return this;
      if (this.dependsOn == null)
        this.dependsOn = new ArrayList<ImplementationGuideInputDependsOnComponent>();
      this.dependsOn.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #dependsOn}, creating it if it does not already exist
     */
    public ImplementationGuideInputDependsOnComponent getDependsOnFirstRep() { 
      if (getDependsOn().isEmpty()) {
        addDependsOn();
      }
      return getDependsOn().get(0);
    }

    /**
     * @return {@link #global} (A set of profiles that all resources covered by this implementation guide must conform to.)
     */
    public List<ImplementationGuideInputGlobalComponent> getGlobal() { 
      if (this.global == null)
        this.global = new ArrayList<ImplementationGuideInputGlobalComponent>();
      return this.global;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ImplementationGuideInput setGlobal(List<ImplementationGuideInputGlobalComponent> theGlobal) { 
      this.global = theGlobal;
      return this;
    }

    public boolean hasGlobal() { 
      if (this.global == null)
        return false;
      for (ImplementationGuideInputGlobalComponent item : this.global)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ImplementationGuideInputGlobalComponent addGlobal() { //3
      ImplementationGuideInputGlobalComponent t = new ImplementationGuideInputGlobalComponent();
      if (this.global == null)
        this.global = new ArrayList<ImplementationGuideInputGlobalComponent>();
      this.global.add(t);
      return t;
    }

    public ImplementationGuideInput addGlobal(ImplementationGuideInputGlobalComponent t) { //3
      if (t == null)
        return this;
      if (this.global == null)
        this.global = new ArrayList<ImplementationGuideInputGlobalComponent>();
      this.global.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #global}, creating it if it does not already exist
     */
    public ImplementationGuideInputGlobalComponent getGlobalFirstRep() { 
      if (getGlobal().isEmpty()) {
        addGlobal();
      }
      return getGlobal().get(0);
    }

    /**
     * @return {@link #definition} (Information used when generating the IG.)
     */
    public ImplementationGuideInputDefinitionComponent getDefinition() { 
      if (this.definition == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuideInput.definition");
        else if (Configuration.doAutoCreate())
          this.definition = new ImplementationGuideInputDefinitionComponent(); // cc
      return this.definition;
    }

    public boolean hasDefinition() { 
      return this.definition != null && !this.definition.isEmpty();
    }

    /**
     * @param value {@link #definition} (Information used when generating the IG.)
     */
    public ImplementationGuideInput setDefinition(ImplementationGuideInputDefinitionComponent value) { 
      this.definition = value;
      return this;
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("url", "uri", "An absolute URI that is used to identify this implementation guide input when it is referenced in a specification, model, design or an instance. This SHOULD be globally unique, and SHOULD be a literal address at which this implementation guide input is (or will be) published.", 0, 1, url));
        children.add(new Property("version", "string", "The identifier that is used to identify this version of the implementation guide input when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the implementation guide input author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.", 0, 1, version));
        children.add(new Property("name", "string", "A natural language name identifying the implementation guide input. This name should be usable as an identifier for the module by machine processing applications such as code generation.", 0, 1, name));
        children.add(new Property("status", "code", "The status of this implementation guide input. Enables tracking the life-cycle of the content.", 0, 1, status));
        children.add(new Property("experimental", "boolean", "A boolean value to indicate that this implementation guide input is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.", 0, 1, experimental));
        children.add(new Property("date", "dateTime", "The date  (and optionally time) when the implementation guide input was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the implementation guide input changes.", 0, 1, date));
        children.add(new Property("publisher", "string", "The name of the individual or organization that published the implementation guide input.", 0, 1, publisher));
        children.add(new Property("contact", "ContactDetail", "Contact details to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact));
        children.add(new Property("description", "markdown", "A free text natural language description of the implementation guide input from a consumer's perspective.", 0, 1, description));
        children.add(new Property("useContext", "UsageContext", "The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching for appropriate implementation guide input instances.", 0, java.lang.Integer.MAX_VALUE, useContext));
        children.add(new Property("jurisdiction", "CodeableConcept", "A legal or geographic region in which the implementation guide input is intended to be used.", 0, java.lang.Integer.MAX_VALUE, jurisdiction));
        children.add(new Property("copyright", "markdown", "A copyright statement relating to the implementation guide input and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the implementation guide input.", 0, 1, copyright));
        children.add(new Property("fhirVersion", "id", "The version of the FHIR specification on which this ImplementationGuide is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 3.2.0 for this version.", 0, 1, fhirVersion));
        children.add(new Property("dependsOn", "", "Another implementation guide that this implementation depends on. Typically, an implementation guide uses value sets, profiles etc.defined in other implementation guides.", 0, java.lang.Integer.MAX_VALUE, dependsOn));
        children.add(new Property("global", "", "A set of profiles that all resources covered by this implementation guide must conform to.", 0, java.lang.Integer.MAX_VALUE, global));
        children.add(new Property("definition", "", "Information used when generating the IG.", 0, 1, definition));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case 116079: /*url*/  return new Property("url", "uri", "An absolute URI that is used to identify this implementation guide input when it is referenced in a specification, model, design or an instance. This SHOULD be globally unique, and SHOULD be a literal address at which this implementation guide input is (or will be) published.", 0, 1, url);
        case 351608024: /*version*/  return new Property("version", "string", "The identifier that is used to identify this version of the implementation guide input when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the implementation guide input author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.", 0, 1, version);
        case 3373707: /*name*/  return new Property("name", "string", "A natural language name identifying the implementation guide input. This name should be usable as an identifier for the module by machine processing applications such as code generation.", 0, 1, name);
        case -892481550: /*status*/  return new Property("status", "code", "The status of this implementation guide input. Enables tracking the life-cycle of the content.", 0, 1, status);
        case -404562712: /*experimental*/  return new Property("experimental", "boolean", "A boolean value to indicate that this implementation guide input is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.", 0, 1, experimental);
        case 3076014: /*date*/  return new Property("date", "dateTime", "The date  (and optionally time) when the implementation guide input was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the implementation guide input changes.", 0, 1, date);
        case 1447404028: /*publisher*/  return new Property("publisher", "string", "The name of the individual or organization that published the implementation guide input.", 0, 1, publisher);
        case 951526432: /*contact*/  return new Property("contact", "ContactDetail", "Contact details to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact);
        case -1724546052: /*description*/  return new Property("description", "markdown", "A free text natural language description of the implementation guide input from a consumer's perspective.", 0, 1, description);
        case -669707736: /*useContext*/  return new Property("useContext", "UsageContext", "The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching for appropriate implementation guide input instances.", 0, java.lang.Integer.MAX_VALUE, useContext);
        case -507075711: /*jurisdiction*/  return new Property("jurisdiction", "CodeableConcept", "A legal or geographic region in which the implementation guide input is intended to be used.", 0, java.lang.Integer.MAX_VALUE, jurisdiction);
        case 1522889671: /*copyright*/  return new Property("copyright", "markdown", "A copyright statement relating to the implementation guide input and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the implementation guide input.", 0, 1, copyright);
        case 461006061: /*fhirVersion*/  return new Property("fhirVersion", "id", "The version of the FHIR specification on which this ImplementationGuide is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 3.2.0 for this version.", 0, 1, fhirVersion);
        case -1109214266: /*dependsOn*/  return new Property("dependsOn", "", "Another implementation guide that this implementation depends on. Typically, an implementation guide uses value sets, profiles etc.defined in other implementation guides.", 0, java.lang.Integer.MAX_VALUE, dependsOn);
        case -1243020381: /*global*/  return new Property("global", "", "A set of profiles that all resources covered by this implementation guide must conform to.", 0, java.lang.Integer.MAX_VALUE, global);
        case -1014418093: /*definition*/  return new Property("definition", "", "Information used when generating the IG.", 0, 1, definition);
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
        case -1109214266: /*dependsOn*/ return this.dependsOn == null ? new Base[0] : this.dependsOn.toArray(new Base[this.dependsOn.size()]); // ImplementationGuideInputDependsOnComponent
        case -1243020381: /*global*/ return this.global == null ? new Base[0] : this.global.toArray(new Base[this.global.size()]); // ImplementationGuideInputGlobalComponent
        case -1014418093: /*definition*/ return this.definition == null ? new Base[0] : new Base[] {this.definition}; // ImplementationGuideInputDefinitionComponent
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
          this.getDependsOn().add((ImplementationGuideInputDependsOnComponent) value); // ImplementationGuideInputDependsOnComponent
          return value;
        case -1243020381: // global
          this.getGlobal().add((ImplementationGuideInputGlobalComponent) value); // ImplementationGuideInputGlobalComponent
          return value;
        case -1014418093: // definition
          this.definition = (ImplementationGuideInputDefinitionComponent) value; // ImplementationGuideInputDefinitionComponent
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
          this.getDependsOn().add((ImplementationGuideInputDependsOnComponent) value);
        } else if (name.equals("global")) {
          this.getGlobal().add((ImplementationGuideInputGlobalComponent) value);
        } else if (name.equals("definition")) {
          this.definition = (ImplementationGuideInputDefinitionComponent) value; // ImplementationGuideInputDefinitionComponent
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
        case -1243020381:  return addGlobal(); 
        case -1014418093:  return getDefinition(); 
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
        case -1109214266: /*dependsOn*/ return new String[] {};
        case -1243020381: /*global*/ return new String[] {};
        case -1014418093: /*definition*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.url");
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.version");
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.name");
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.status");
        }
        else if (name.equals("experimental")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.experimental");
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.date");
        }
        else if (name.equals("publisher")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.publisher");
        }
        else if (name.equals("contact")) {
          return addContact();
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.description");
        }
        else if (name.equals("useContext")) {
          return addUseContext();
        }
        else if (name.equals("jurisdiction")) {
          return addJurisdiction();
        }
        else if (name.equals("copyright")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.copyright");
        }
        else if (name.equals("fhirVersion")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.fhirVersion");
        }
        else if (name.equals("dependsOn")) {
          return addDependsOn();
        }
        else if (name.equals("global")) {
          return addGlobal();
        }
        else if (name.equals("definition")) {
          this.definition = new ImplementationGuideInputDefinitionComponent();
          return this.definition;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "ImplementationGuideInput";

  }

      public ImplementationGuideInput copy() {
        ImplementationGuideInput dst = new ImplementationGuideInput();
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
          dst.dependsOn = new ArrayList<ImplementationGuideInputDependsOnComponent>();
          for (ImplementationGuideInputDependsOnComponent i : dependsOn)
            dst.dependsOn.add(i.copy());
        };
        if (global != null) {
          dst.global = new ArrayList<ImplementationGuideInputGlobalComponent>();
          for (ImplementationGuideInputGlobalComponent i : global)
            dst.global.add(i.copy());
        };
        dst.definition = definition == null ? null : definition.copy();
        return dst;
      }

      protected ImplementationGuideInput typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideInput))
          return false;
        ImplementationGuideInput o = (ImplementationGuideInput) other_;
        return compareDeep(copyright, o.copyright, true) && compareDeep(fhirVersion, o.fhirVersion, true)
           && compareDeep(dependsOn, o.dependsOn, true) && compareDeep(global, o.global, true) && compareDeep(definition, o.definition, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideInput))
          return false;
        ImplementationGuideInput o = (ImplementationGuideInput) other_;
        return compareValues(copyright, o.copyright, true) && compareValues(fhirVersion, o.fhirVersion, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(copyright, fhirVersion, dependsOn
          , global, definition);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ImplementationGuideInput;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>The implementation guide input publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ImplementationGuideInput.date</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="ImplementationGuideInput.date", description="The implementation guide input publication date", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>The implementation guide input publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ImplementationGuideInput.date</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>dependency</b>
   * <p>
   * Description: <b>Canonical Reference to output resource for IG</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImplementationGuideInput.dependsOn.definition</b><br>
   * </p>
   */
  @SearchParamDefinition(name="dependency", path="ImplementationGuideInput.dependsOn.definition", description="Canonical Reference to output resource for IG", type="reference", target={ImplementationGuideOutput.class } )
  public static final String SP_DEPENDENCY = "dependency";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>dependency</b>
   * <p>
   * Description: <b>Canonical Reference to output resource for IG</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImplementationGuideInput.dependsOn.definition</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam DEPENDENCY = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_DEPENDENCY);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ImplementationGuideInput:dependency</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_DEPENDENCY = new ca.uhn.fhir.model.api.Include("ImplementationGuideInput:dependency").toLocked();

 /**
   * Search parameter: <b>resource</b>
   * <p>
   * Description: <b>Location of the resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImplementationGuideInput.definition.resource.reference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="resource", path="ImplementationGuideInput.definition.resource.reference", description="Location of the resource", type="reference" )
  public static final String SP_RESOURCE = "resource";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>resource</b>
   * <p>
   * Description: <b>Location of the resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImplementationGuideInput.definition.resource.reference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam RESOURCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_RESOURCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ImplementationGuideInput:resource</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_RESOURCE = new ca.uhn.fhir.model.api.Include("ImplementationGuideInput:resource").toLocked();

 /**
   * Search parameter: <b>jurisdiction</b>
   * <p>
   * Description: <b>Intended jurisdiction for the implementation guide input</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImplementationGuideInput.jurisdiction</b><br>
   * </p>
   */
  @SearchParamDefinition(name="jurisdiction", path="ImplementationGuideInput.jurisdiction", description="Intended jurisdiction for the implementation guide input", type="token" )
  public static final String SP_JURISDICTION = "jurisdiction";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>jurisdiction</b>
   * <p>
   * Description: <b>Intended jurisdiction for the implementation guide input</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImplementationGuideInput.jurisdiction</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam JURISDICTION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_JURISDICTION);

 /**
   * Search parameter: <b>name</b>
   * <p>
   * Description: <b>Computationally friendly name of the implementation guide input</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ImplementationGuideInput.name</b><br>
   * </p>
   */
  @SearchParamDefinition(name="name", path="ImplementationGuideInput.name", description="Computationally friendly name of the implementation guide input", type="string" )
  public static final String SP_NAME = "name";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>name</b>
   * <p>
   * Description: <b>Computationally friendly name of the implementation guide input</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ImplementationGuideInput.name</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam NAME = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_NAME);

 /**
   * Search parameter: <b>description</b>
   * <p>
   * Description: <b>The description of the implementation guide input</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ImplementationGuideInput.description</b><br>
   * </p>
   */
  @SearchParamDefinition(name="description", path="ImplementationGuideInput.description", description="The description of the implementation guide input", type="string" )
  public static final String SP_DESCRIPTION = "description";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>description</b>
   * <p>
   * Description: <b>The description of the implementation guide input</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ImplementationGuideInput.description</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam DESCRIPTION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_DESCRIPTION);

 /**
   * Search parameter: <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the implementation guide input</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ImplementationGuideInput.publisher</b><br>
   * </p>
   */
  @SearchParamDefinition(name="publisher", path="ImplementationGuideInput.publisher", description="Name of the publisher of the implementation guide input", type="string" )
  public static final String SP_PUBLISHER = "publisher";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the implementation guide input</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ImplementationGuideInput.publisher</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam PUBLISHER = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_PUBLISHER);

 /**
   * Search parameter: <b>experimental</b>
   * <p>
   * Description: <b>For testing purposes, not real usage</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImplementationGuideInput.experimental</b><br>
   * </p>
   */
  @SearchParamDefinition(name="experimental", path="ImplementationGuideInput.experimental", description="For testing purposes, not real usage", type="token" )
  public static final String SP_EXPERIMENTAL = "experimental";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>experimental</b>
   * <p>
   * Description: <b>For testing purposes, not real usage</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImplementationGuideInput.experimental</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam EXPERIMENTAL = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_EXPERIMENTAL);

 /**
   * Search parameter: <b>version</b>
   * <p>
   * Description: <b>The business version of the implementation guide input</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImplementationGuideInput.version</b><br>
   * </p>
   */
  @SearchParamDefinition(name="version", path="ImplementationGuideInput.version", description="The business version of the implementation guide input", type="token" )
  public static final String SP_VERSION = "version";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>version</b>
   * <p>
   * Description: <b>The business version of the implementation guide input</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImplementationGuideInput.version</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam VERSION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_VERSION);

 /**
   * Search parameter: <b>url</b>
   * <p>
   * Description: <b>The uri that identifies the implementation guide input</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ImplementationGuideInput.url</b><br>
   * </p>
   */
  @SearchParamDefinition(name="url", path="ImplementationGuideInput.url", description="The uri that identifies the implementation guide input", type="uri" )
  public static final String SP_URL = "url";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>url</b>
   * <p>
   * Description: <b>The uri that identifies the implementation guide input</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ImplementationGuideInput.url</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam URL = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_URL);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The current status of the implementation guide input</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImplementationGuideInput.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="ImplementationGuideInput.status", description="The current status of the implementation guide input", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The current status of the implementation guide input</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImplementationGuideInput.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

