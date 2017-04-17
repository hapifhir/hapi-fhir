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
import org.hl7.fhir.dstu3.model.Enumerations.*;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * Resource to define constraints on the Expansion of a FHIR ValueSet.
 */
@ResourceDef(name="ExpansionProfile", profile="http://hl7.org/fhir/Profile/ExpansionProfile")
@ChildOrder(names={"url", "identifier", "version", "name", "status", "experimental", "date", "publisher", "contact", "description", "useContext", "jurisdiction", "fixedVersion", "excludedSystem", "includeDesignations", "designation", "includeDefinition", "activeOnly", "excludeNested", "excludeNotForUI", "excludePostCoordinated", "displayLanguage", "limitedExpansion"})
public class ExpansionProfile extends MetadataResource {

    public enum SystemVersionProcessingMode {
        /**
         * Use this version of the code system if a value set doesn't specify a version
         */
        DEFAULT, 
        /**
         * Use this version of the code system. If a value set specifies a different version, the expansion operation should fail
         */
        CHECK, 
        /**
         * Use this version of the code system irrespective of which version is specified by a value set. Note that this has obvious safety issues, in that it may result in a value set expansion giving a different list of codes that is both wrong and unsafe, and implementers should only use this capability reluctantly. It primarily exists to deal with situations where specifications have fallen into decay as time passes. If a  version is override, the version used SHALL explicitly be represented in the expansion parameters
         */
        OVERRIDE, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static SystemVersionProcessingMode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("default".equals(codeString))
          return DEFAULT;
        if ("check".equals(codeString))
          return CHECK;
        if ("override".equals(codeString))
          return OVERRIDE;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown SystemVersionProcessingMode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DEFAULT: return "default";
            case CHECK: return "check";
            case OVERRIDE: return "override";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case DEFAULT: return "http://hl7.org/fhir/system-version-processing-mode";
            case CHECK: return "http://hl7.org/fhir/system-version-processing-mode";
            case OVERRIDE: return "http://hl7.org/fhir/system-version-processing-mode";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case DEFAULT: return "Use this version of the code system if a value set doesn't specify a version";
            case CHECK: return "Use this version of the code system. If a value set specifies a different version, the expansion operation should fail";
            case OVERRIDE: return "Use this version of the code system irrespective of which version is specified by a value set. Note that this has obvious safety issues, in that it may result in a value set expansion giving a different list of codes that is both wrong and unsafe, and implementers should only use this capability reluctantly. It primarily exists to deal with situations where specifications have fallen into decay as time passes. If a  version is override, the version used SHALL explicitly be represented in the expansion parameters";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DEFAULT: return "Default Version";
            case CHECK: return "Check ValueSet Version";
            case OVERRIDE: return "Override ValueSet Version";
            default: return "?";
          }
        }
    }

  public static class SystemVersionProcessingModeEnumFactory implements EnumFactory<SystemVersionProcessingMode> {
    public SystemVersionProcessingMode fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("default".equals(codeString))
          return SystemVersionProcessingMode.DEFAULT;
        if ("check".equals(codeString))
          return SystemVersionProcessingMode.CHECK;
        if ("override".equals(codeString))
          return SystemVersionProcessingMode.OVERRIDE;
        throw new IllegalArgumentException("Unknown SystemVersionProcessingMode code '"+codeString+"'");
        }
        public Enumeration<SystemVersionProcessingMode> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<SystemVersionProcessingMode>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("default".equals(codeString))
          return new Enumeration<SystemVersionProcessingMode>(this, SystemVersionProcessingMode.DEFAULT);
        if ("check".equals(codeString))
          return new Enumeration<SystemVersionProcessingMode>(this, SystemVersionProcessingMode.CHECK);
        if ("override".equals(codeString))
          return new Enumeration<SystemVersionProcessingMode>(this, SystemVersionProcessingMode.OVERRIDE);
        throw new FHIRException("Unknown SystemVersionProcessingMode code '"+codeString+"'");
        }
    public String toCode(SystemVersionProcessingMode code) {
      if (code == SystemVersionProcessingMode.DEFAULT)
        return "default";
      if (code == SystemVersionProcessingMode.CHECK)
        return "check";
      if (code == SystemVersionProcessingMode.OVERRIDE)
        return "override";
      return "?";
      }
    public String toSystem(SystemVersionProcessingMode code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class ExpansionProfileFixedVersionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The specific system for which to fix the version.
         */
        @Child(name = "system", type = {UriType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="System to have its version fixed", formalDefinition="The specific system for which to fix the version." )
        protected UriType system;

        /**
         * The version of the code system from which codes in the expansion should be included.
         */
        @Child(name = "version", type = {StringType.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Specific version of the code system referred to", formalDefinition="The version of the code system from which codes in the expansion should be included." )
        protected StringType version;

        /**
         * How to manage the intersection between a fixed version in a value set, and this fixed version of the system in the expansion profile.
         */
        @Child(name = "mode", type = {CodeType.class}, order=3, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="default | check | override", formalDefinition="How to manage the intersection between a fixed version in a value set, and this fixed version of the system in the expansion profile." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/system-version-processing-mode")
        protected Enumeration<SystemVersionProcessingMode> mode;

        private static final long serialVersionUID = 1818466753L;

    /**
     * Constructor
     */
      public ExpansionProfileFixedVersionComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ExpansionProfileFixedVersionComponent(UriType system, StringType version, Enumeration<SystemVersionProcessingMode> mode) {
        super();
        this.system = system;
        this.version = version;
        this.mode = mode;
      }

        /**
         * @return {@link #system} (The specific system for which to fix the version.). This is the underlying object with id, value and extensions. The accessor "getSystem" gives direct access to the value
         */
        public UriType getSystemElement() { 
          if (this.system == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExpansionProfileFixedVersionComponent.system");
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
         * @param value {@link #system} (The specific system for which to fix the version.). This is the underlying object with id, value and extensions. The accessor "getSystem" gives direct access to the value
         */
        public ExpansionProfileFixedVersionComponent setSystemElement(UriType value) { 
          this.system = value;
          return this;
        }

        /**
         * @return The specific system for which to fix the version.
         */
        public String getSystem() { 
          return this.system == null ? null : this.system.getValue();
        }

        /**
         * @param value The specific system for which to fix the version.
         */
        public ExpansionProfileFixedVersionComponent setSystem(String value) { 
            if (this.system == null)
              this.system = new UriType();
            this.system.setValue(value);
          return this;
        }

        /**
         * @return {@link #version} (The version of the code system from which codes in the expansion should be included.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
         */
        public StringType getVersionElement() { 
          if (this.version == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExpansionProfileFixedVersionComponent.version");
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
         * @param value {@link #version} (The version of the code system from which codes in the expansion should be included.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
         */
        public ExpansionProfileFixedVersionComponent setVersionElement(StringType value) { 
          this.version = value;
          return this;
        }

        /**
         * @return The version of the code system from which codes in the expansion should be included.
         */
        public String getVersion() { 
          return this.version == null ? null : this.version.getValue();
        }

        /**
         * @param value The version of the code system from which codes in the expansion should be included.
         */
        public ExpansionProfileFixedVersionComponent setVersion(String value) { 
            if (this.version == null)
              this.version = new StringType();
            this.version.setValue(value);
          return this;
        }

        /**
         * @return {@link #mode} (How to manage the intersection between a fixed version in a value set, and this fixed version of the system in the expansion profile.). This is the underlying object with id, value and extensions. The accessor "getMode" gives direct access to the value
         */
        public Enumeration<SystemVersionProcessingMode> getModeElement() { 
          if (this.mode == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExpansionProfileFixedVersionComponent.mode");
            else if (Configuration.doAutoCreate())
              this.mode = new Enumeration<SystemVersionProcessingMode>(new SystemVersionProcessingModeEnumFactory()); // bb
          return this.mode;
        }

        public boolean hasModeElement() { 
          return this.mode != null && !this.mode.isEmpty();
        }

        public boolean hasMode() { 
          return this.mode != null && !this.mode.isEmpty();
        }

        /**
         * @param value {@link #mode} (How to manage the intersection between a fixed version in a value set, and this fixed version of the system in the expansion profile.). This is the underlying object with id, value and extensions. The accessor "getMode" gives direct access to the value
         */
        public ExpansionProfileFixedVersionComponent setModeElement(Enumeration<SystemVersionProcessingMode> value) { 
          this.mode = value;
          return this;
        }

        /**
         * @return How to manage the intersection between a fixed version in a value set, and this fixed version of the system in the expansion profile.
         */
        public SystemVersionProcessingMode getMode() { 
          return this.mode == null ? null : this.mode.getValue();
        }

        /**
         * @param value How to manage the intersection between a fixed version in a value set, and this fixed version of the system in the expansion profile.
         */
        public ExpansionProfileFixedVersionComponent setMode(SystemVersionProcessingMode value) { 
            if (this.mode == null)
              this.mode = new Enumeration<SystemVersionProcessingMode>(new SystemVersionProcessingModeEnumFactory());
            this.mode.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("system", "uri", "The specific system for which to fix the version.", 0, java.lang.Integer.MAX_VALUE, system));
          childrenList.add(new Property("version", "string", "The version of the code system from which codes in the expansion should be included.", 0, java.lang.Integer.MAX_VALUE, version));
          childrenList.add(new Property("mode", "code", "How to manage the intersection between a fixed version in a value set, and this fixed version of the system in the expansion profile.", 0, java.lang.Integer.MAX_VALUE, mode));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -887328209: /*system*/ return this.system == null ? new Base[0] : new Base[] {this.system}; // UriType
        case 351608024: /*version*/ return this.version == null ? new Base[0] : new Base[] {this.version}; // StringType
        case 3357091: /*mode*/ return this.mode == null ? new Base[0] : new Base[] {this.mode}; // Enumeration<SystemVersionProcessingMode>
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -887328209: // system
          this.system = castToUri(value); // UriType
          return value;
        case 351608024: // version
          this.version = castToString(value); // StringType
          return value;
        case 3357091: // mode
          value = new SystemVersionProcessingModeEnumFactory().fromType(castToCode(value));
          this.mode = (Enumeration) value; // Enumeration<SystemVersionProcessingMode>
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("system")) {
          this.system = castToUri(value); // UriType
        } else if (name.equals("version")) {
          this.version = castToString(value); // StringType
        } else if (name.equals("mode")) {
          value = new SystemVersionProcessingModeEnumFactory().fromType(castToCode(value));
          this.mode = (Enumeration) value; // Enumeration<SystemVersionProcessingMode>
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -887328209:  return getSystemElement();
        case 351608024:  return getVersionElement();
        case 3357091:  return getModeElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -887328209: /*system*/ return new String[] {"uri"};
        case 351608024: /*version*/ return new String[] {"string"};
        case 3357091: /*mode*/ return new String[] {"code"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("system")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExpansionProfile.system");
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExpansionProfile.version");
        }
        else if (name.equals("mode")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExpansionProfile.mode");
        }
        else
          return super.addChild(name);
      }

      public ExpansionProfileFixedVersionComponent copy() {
        ExpansionProfileFixedVersionComponent dst = new ExpansionProfileFixedVersionComponent();
        copyValues(dst);
        dst.system = system == null ? null : system.copy();
        dst.version = version == null ? null : version.copy();
        dst.mode = mode == null ? null : mode.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ExpansionProfileFixedVersionComponent))
          return false;
        ExpansionProfileFixedVersionComponent o = (ExpansionProfileFixedVersionComponent) other;
        return compareDeep(system, o.system, true) && compareDeep(version, o.version, true) && compareDeep(mode, o.mode, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ExpansionProfileFixedVersionComponent))
          return false;
        ExpansionProfileFixedVersionComponent o = (ExpansionProfileFixedVersionComponent) other;
        return compareValues(system, o.system, true) && compareValues(version, o.version, true) && compareValues(mode, o.mode, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(system, version, mode);
      }

  public String fhirType() {
    return "ExpansionProfile.fixedVersion";

  }

  }

    @Block()
    public static class ExpansionProfileExcludedSystemComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * An absolute URI which is the code system to be excluded.
         */
        @Child(name = "system", type = {UriType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The specific code system to be excluded", formalDefinition="An absolute URI which is the code system to be excluded." )
        protected UriType system;

        /**
         * The version of the code system from which codes in the expansion should be excluded.
         */
        @Child(name = "version", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Specific version of the code system referred to", formalDefinition="The version of the code system from which codes in the expansion should be excluded." )
        protected StringType version;

        private static final long serialVersionUID = 1145288774L;

    /**
     * Constructor
     */
      public ExpansionProfileExcludedSystemComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ExpansionProfileExcludedSystemComponent(UriType system) {
        super();
        this.system = system;
      }

        /**
         * @return {@link #system} (An absolute URI which is the code system to be excluded.). This is the underlying object with id, value and extensions. The accessor "getSystem" gives direct access to the value
         */
        public UriType getSystemElement() { 
          if (this.system == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExpansionProfileExcludedSystemComponent.system");
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
         * @param value {@link #system} (An absolute URI which is the code system to be excluded.). This is the underlying object with id, value and extensions. The accessor "getSystem" gives direct access to the value
         */
        public ExpansionProfileExcludedSystemComponent setSystemElement(UriType value) { 
          this.system = value;
          return this;
        }

        /**
         * @return An absolute URI which is the code system to be excluded.
         */
        public String getSystem() { 
          return this.system == null ? null : this.system.getValue();
        }

        /**
         * @param value An absolute URI which is the code system to be excluded.
         */
        public ExpansionProfileExcludedSystemComponent setSystem(String value) { 
            if (this.system == null)
              this.system = new UriType();
            this.system.setValue(value);
          return this;
        }

        /**
         * @return {@link #version} (The version of the code system from which codes in the expansion should be excluded.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
         */
        public StringType getVersionElement() { 
          if (this.version == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExpansionProfileExcludedSystemComponent.version");
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
         * @param value {@link #version} (The version of the code system from which codes in the expansion should be excluded.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
         */
        public ExpansionProfileExcludedSystemComponent setVersionElement(StringType value) { 
          this.version = value;
          return this;
        }

        /**
         * @return The version of the code system from which codes in the expansion should be excluded.
         */
        public String getVersion() { 
          return this.version == null ? null : this.version.getValue();
        }

        /**
         * @param value The version of the code system from which codes in the expansion should be excluded.
         */
        public ExpansionProfileExcludedSystemComponent setVersion(String value) { 
          if (Utilities.noString(value))
            this.version = null;
          else {
            if (this.version == null)
              this.version = new StringType();
            this.version.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("system", "uri", "An absolute URI which is the code system to be excluded.", 0, java.lang.Integer.MAX_VALUE, system));
          childrenList.add(new Property("version", "string", "The version of the code system from which codes in the expansion should be excluded.", 0, java.lang.Integer.MAX_VALUE, version));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -887328209: /*system*/ return this.system == null ? new Base[0] : new Base[] {this.system}; // UriType
        case 351608024: /*version*/ return this.version == null ? new Base[0] : new Base[] {this.version}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -887328209: // system
          this.system = castToUri(value); // UriType
          return value;
        case 351608024: // version
          this.version = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("system")) {
          this.system = castToUri(value); // UriType
        } else if (name.equals("version")) {
          this.version = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -887328209:  return getSystemElement();
        case 351608024:  return getVersionElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -887328209: /*system*/ return new String[] {"uri"};
        case 351608024: /*version*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("system")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExpansionProfile.system");
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExpansionProfile.version");
        }
        else
          return super.addChild(name);
      }

      public ExpansionProfileExcludedSystemComponent copy() {
        ExpansionProfileExcludedSystemComponent dst = new ExpansionProfileExcludedSystemComponent();
        copyValues(dst);
        dst.system = system == null ? null : system.copy();
        dst.version = version == null ? null : version.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ExpansionProfileExcludedSystemComponent))
          return false;
        ExpansionProfileExcludedSystemComponent o = (ExpansionProfileExcludedSystemComponent) other;
        return compareDeep(system, o.system, true) && compareDeep(version, o.version, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ExpansionProfileExcludedSystemComponent))
          return false;
        ExpansionProfileExcludedSystemComponent o = (ExpansionProfileExcludedSystemComponent) other;
        return compareValues(system, o.system, true) && compareValues(version, o.version, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(system, version);
      }

  public String fhirType() {
    return "ExpansionProfile.excludedSystem";

  }

  }

    @Block()
    public static class ExpansionProfileDesignationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Designations to be included.
         */
        @Child(name = "include", type = {}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Designations to be included", formalDefinition="Designations to be included." )
        protected DesignationIncludeComponent include;

        /**
         * Designations to be excluded.
         */
        @Child(name = "exclude", type = {}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Designations to be excluded", formalDefinition="Designations to be excluded." )
        protected DesignationExcludeComponent exclude;

        private static final long serialVersionUID = -2080476436L;

    /**
     * Constructor
     */
      public ExpansionProfileDesignationComponent() {
        super();
      }

        /**
         * @return {@link #include} (Designations to be included.)
         */
        public DesignationIncludeComponent getInclude() { 
          if (this.include == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExpansionProfileDesignationComponent.include");
            else if (Configuration.doAutoCreate())
              this.include = new DesignationIncludeComponent(); // cc
          return this.include;
        }

        public boolean hasInclude() { 
          return this.include != null && !this.include.isEmpty();
        }

        /**
         * @param value {@link #include} (Designations to be included.)
         */
        public ExpansionProfileDesignationComponent setInclude(DesignationIncludeComponent value) { 
          this.include = value;
          return this;
        }

        /**
         * @return {@link #exclude} (Designations to be excluded.)
         */
        public DesignationExcludeComponent getExclude() { 
          if (this.exclude == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExpansionProfileDesignationComponent.exclude");
            else if (Configuration.doAutoCreate())
              this.exclude = new DesignationExcludeComponent(); // cc
          return this.exclude;
        }

        public boolean hasExclude() { 
          return this.exclude != null && !this.exclude.isEmpty();
        }

        /**
         * @param value {@link #exclude} (Designations to be excluded.)
         */
        public ExpansionProfileDesignationComponent setExclude(DesignationExcludeComponent value) { 
          this.exclude = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("include", "", "Designations to be included.", 0, java.lang.Integer.MAX_VALUE, include));
          childrenList.add(new Property("exclude", "", "Designations to be excluded.", 0, java.lang.Integer.MAX_VALUE, exclude));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1942574248: /*include*/ return this.include == null ? new Base[0] : new Base[] {this.include}; // DesignationIncludeComponent
        case -1321148966: /*exclude*/ return this.exclude == null ? new Base[0] : new Base[] {this.exclude}; // DesignationExcludeComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1942574248: // include
          this.include = (DesignationIncludeComponent) value; // DesignationIncludeComponent
          return value;
        case -1321148966: // exclude
          this.exclude = (DesignationExcludeComponent) value; // DesignationExcludeComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("include")) {
          this.include = (DesignationIncludeComponent) value; // DesignationIncludeComponent
        } else if (name.equals("exclude")) {
          this.exclude = (DesignationExcludeComponent) value; // DesignationExcludeComponent
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1942574248:  return getInclude(); 
        case -1321148966:  return getExclude(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1942574248: /*include*/ return new String[] {};
        case -1321148966: /*exclude*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("include")) {
          this.include = new DesignationIncludeComponent();
          return this.include;
        }
        else if (name.equals("exclude")) {
          this.exclude = new DesignationExcludeComponent();
          return this.exclude;
        }
        else
          return super.addChild(name);
      }

      public ExpansionProfileDesignationComponent copy() {
        ExpansionProfileDesignationComponent dst = new ExpansionProfileDesignationComponent();
        copyValues(dst);
        dst.include = include == null ? null : include.copy();
        dst.exclude = exclude == null ? null : exclude.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ExpansionProfileDesignationComponent))
          return false;
        ExpansionProfileDesignationComponent o = (ExpansionProfileDesignationComponent) other;
        return compareDeep(include, o.include, true) && compareDeep(exclude, o.exclude, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ExpansionProfileDesignationComponent))
          return false;
        ExpansionProfileDesignationComponent o = (ExpansionProfileDesignationComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(include, exclude);
      }

  public String fhirType() {
    return "ExpansionProfile.designation";

  }

  }

    @Block()
    public static class DesignationIncludeComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A data group for each designation to be included.
         */
        @Child(name = "designation", type = {}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="The designation to be included", formalDefinition="A data group for each designation to be included." )
        protected List<DesignationIncludeDesignationComponent> designation;

        private static final long serialVersionUID = -1989669274L;

    /**
     * Constructor
     */
      public DesignationIncludeComponent() {
        super();
      }

        /**
         * @return {@link #designation} (A data group for each designation to be included.)
         */
        public List<DesignationIncludeDesignationComponent> getDesignation() { 
          if (this.designation == null)
            this.designation = new ArrayList<DesignationIncludeDesignationComponent>();
          return this.designation;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public DesignationIncludeComponent setDesignation(List<DesignationIncludeDesignationComponent> theDesignation) { 
          this.designation = theDesignation;
          return this;
        }

        public boolean hasDesignation() { 
          if (this.designation == null)
            return false;
          for (DesignationIncludeDesignationComponent item : this.designation)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public DesignationIncludeDesignationComponent addDesignation() { //3
          DesignationIncludeDesignationComponent t = new DesignationIncludeDesignationComponent();
          if (this.designation == null)
            this.designation = new ArrayList<DesignationIncludeDesignationComponent>();
          this.designation.add(t);
          return t;
        }

        public DesignationIncludeComponent addDesignation(DesignationIncludeDesignationComponent t) { //3
          if (t == null)
            return this;
          if (this.designation == null)
            this.designation = new ArrayList<DesignationIncludeDesignationComponent>();
          this.designation.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #designation}, creating it if it does not already exist
         */
        public DesignationIncludeDesignationComponent getDesignationFirstRep() { 
          if (getDesignation().isEmpty()) {
            addDesignation();
          }
          return getDesignation().get(0);
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("designation", "", "A data group for each designation to be included.", 0, java.lang.Integer.MAX_VALUE, designation));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -900931593: /*designation*/ return this.designation == null ? new Base[0] : this.designation.toArray(new Base[this.designation.size()]); // DesignationIncludeDesignationComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -900931593: // designation
          this.getDesignation().add((DesignationIncludeDesignationComponent) value); // DesignationIncludeDesignationComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("designation")) {
          this.getDesignation().add((DesignationIncludeDesignationComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -900931593:  return addDesignation(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -900931593: /*designation*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("designation")) {
          return addDesignation();
        }
        else
          return super.addChild(name);
      }

      public DesignationIncludeComponent copy() {
        DesignationIncludeComponent dst = new DesignationIncludeComponent();
        copyValues(dst);
        if (designation != null) {
          dst.designation = new ArrayList<DesignationIncludeDesignationComponent>();
          for (DesignationIncludeDesignationComponent i : designation)
            dst.designation.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof DesignationIncludeComponent))
          return false;
        DesignationIncludeComponent o = (DesignationIncludeComponent) other;
        return compareDeep(designation, o.designation, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DesignationIncludeComponent))
          return false;
        DesignationIncludeComponent o = (DesignationIncludeComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(designation);
      }

  public String fhirType() {
    return "ExpansionProfile.designation.include";

  }

  }

    @Block()
    public static class DesignationIncludeDesignationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The language this designation is defined for.
         */
        @Child(name = "language", type = {CodeType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Human language of the designation to be included", formalDefinition="The language this designation is defined for." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/languages")
        protected CodeType language;

        /**
         * Which kinds of designation to include in the expansion.
         */
        @Child(name = "use", type = {Coding.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="What kind of Designation to include", formalDefinition="Which kinds of designation to include in the expansion." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/designation-use")
        protected Coding use;

        private static final long serialVersionUID = 242239292L;

    /**
     * Constructor
     */
      public DesignationIncludeDesignationComponent() {
        super();
      }

        /**
         * @return {@link #language} (The language this designation is defined for.). This is the underlying object with id, value and extensions. The accessor "getLanguage" gives direct access to the value
         */
        public CodeType getLanguageElement() { 
          if (this.language == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DesignationIncludeDesignationComponent.language");
            else if (Configuration.doAutoCreate())
              this.language = new CodeType(); // bb
          return this.language;
        }

        public boolean hasLanguageElement() { 
          return this.language != null && !this.language.isEmpty();
        }

        public boolean hasLanguage() { 
          return this.language != null && !this.language.isEmpty();
        }

        /**
         * @param value {@link #language} (The language this designation is defined for.). This is the underlying object with id, value and extensions. The accessor "getLanguage" gives direct access to the value
         */
        public DesignationIncludeDesignationComponent setLanguageElement(CodeType value) { 
          this.language = value;
          return this;
        }

        /**
         * @return The language this designation is defined for.
         */
        public String getLanguage() { 
          return this.language == null ? null : this.language.getValue();
        }

        /**
         * @param value The language this designation is defined for.
         */
        public DesignationIncludeDesignationComponent setLanguage(String value) { 
          if (Utilities.noString(value))
            this.language = null;
          else {
            if (this.language == null)
              this.language = new CodeType();
            this.language.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #use} (Which kinds of designation to include in the expansion.)
         */
        public Coding getUse() { 
          if (this.use == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DesignationIncludeDesignationComponent.use");
            else if (Configuration.doAutoCreate())
              this.use = new Coding(); // cc
          return this.use;
        }

        public boolean hasUse() { 
          return this.use != null && !this.use.isEmpty();
        }

        /**
         * @param value {@link #use} (Which kinds of designation to include in the expansion.)
         */
        public DesignationIncludeDesignationComponent setUse(Coding value) { 
          this.use = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("language", "code", "The language this designation is defined for.", 0, java.lang.Integer.MAX_VALUE, language));
          childrenList.add(new Property("use", "Coding", "Which kinds of designation to include in the expansion.", 0, java.lang.Integer.MAX_VALUE, use));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1613589672: /*language*/ return this.language == null ? new Base[0] : new Base[] {this.language}; // CodeType
        case 116103: /*use*/ return this.use == null ? new Base[0] : new Base[] {this.use}; // Coding
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1613589672: // language
          this.language = castToCode(value); // CodeType
          return value;
        case 116103: // use
          this.use = castToCoding(value); // Coding
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("language")) {
          this.language = castToCode(value); // CodeType
        } else if (name.equals("use")) {
          this.use = castToCoding(value); // Coding
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1613589672:  return getLanguageElement();
        case 116103:  return getUse(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1613589672: /*language*/ return new String[] {"code"};
        case 116103: /*use*/ return new String[] {"Coding"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("language")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExpansionProfile.language");
        }
        else if (name.equals("use")) {
          this.use = new Coding();
          return this.use;
        }
        else
          return super.addChild(name);
      }

      public DesignationIncludeDesignationComponent copy() {
        DesignationIncludeDesignationComponent dst = new DesignationIncludeDesignationComponent();
        copyValues(dst);
        dst.language = language == null ? null : language.copy();
        dst.use = use == null ? null : use.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof DesignationIncludeDesignationComponent))
          return false;
        DesignationIncludeDesignationComponent o = (DesignationIncludeDesignationComponent) other;
        return compareDeep(language, o.language, true) && compareDeep(use, o.use, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DesignationIncludeDesignationComponent))
          return false;
        DesignationIncludeDesignationComponent o = (DesignationIncludeDesignationComponent) other;
        return compareValues(language, o.language, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(language, use);
      }

  public String fhirType() {
    return "ExpansionProfile.designation.include.designation";

  }

  }

    @Block()
    public static class DesignationExcludeComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A data group for each designation to be excluded.
         */
        @Child(name = "designation", type = {}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="The designation to be excluded", formalDefinition="A data group for each designation to be excluded." )
        protected List<DesignationExcludeDesignationComponent> designation;

        private static final long serialVersionUID = 1045849752L;

    /**
     * Constructor
     */
      public DesignationExcludeComponent() {
        super();
      }

        /**
         * @return {@link #designation} (A data group for each designation to be excluded.)
         */
        public List<DesignationExcludeDesignationComponent> getDesignation() { 
          if (this.designation == null)
            this.designation = new ArrayList<DesignationExcludeDesignationComponent>();
          return this.designation;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public DesignationExcludeComponent setDesignation(List<DesignationExcludeDesignationComponent> theDesignation) { 
          this.designation = theDesignation;
          return this;
        }

        public boolean hasDesignation() { 
          if (this.designation == null)
            return false;
          for (DesignationExcludeDesignationComponent item : this.designation)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public DesignationExcludeDesignationComponent addDesignation() { //3
          DesignationExcludeDesignationComponent t = new DesignationExcludeDesignationComponent();
          if (this.designation == null)
            this.designation = new ArrayList<DesignationExcludeDesignationComponent>();
          this.designation.add(t);
          return t;
        }

        public DesignationExcludeComponent addDesignation(DesignationExcludeDesignationComponent t) { //3
          if (t == null)
            return this;
          if (this.designation == null)
            this.designation = new ArrayList<DesignationExcludeDesignationComponent>();
          this.designation.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #designation}, creating it if it does not already exist
         */
        public DesignationExcludeDesignationComponent getDesignationFirstRep() { 
          if (getDesignation().isEmpty()) {
            addDesignation();
          }
          return getDesignation().get(0);
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("designation", "", "A data group for each designation to be excluded.", 0, java.lang.Integer.MAX_VALUE, designation));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -900931593: /*designation*/ return this.designation == null ? new Base[0] : this.designation.toArray(new Base[this.designation.size()]); // DesignationExcludeDesignationComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -900931593: // designation
          this.getDesignation().add((DesignationExcludeDesignationComponent) value); // DesignationExcludeDesignationComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("designation")) {
          this.getDesignation().add((DesignationExcludeDesignationComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -900931593:  return addDesignation(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -900931593: /*designation*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("designation")) {
          return addDesignation();
        }
        else
          return super.addChild(name);
      }

      public DesignationExcludeComponent copy() {
        DesignationExcludeComponent dst = new DesignationExcludeComponent();
        copyValues(dst);
        if (designation != null) {
          dst.designation = new ArrayList<DesignationExcludeDesignationComponent>();
          for (DesignationExcludeDesignationComponent i : designation)
            dst.designation.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof DesignationExcludeComponent))
          return false;
        DesignationExcludeComponent o = (DesignationExcludeComponent) other;
        return compareDeep(designation, o.designation, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DesignationExcludeComponent))
          return false;
        DesignationExcludeComponent o = (DesignationExcludeComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(designation);
      }

  public String fhirType() {
    return "ExpansionProfile.designation.exclude";

  }

  }

    @Block()
    public static class DesignationExcludeDesignationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The language this designation is defined for.
         */
        @Child(name = "language", type = {CodeType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Human language of the designation to be excluded", formalDefinition="The language this designation is defined for." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/languages")
        protected CodeType language;

        /**
         * Which kinds of designation to exclude from the expansion.
         */
        @Child(name = "use", type = {Coding.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="What kind of Designation to exclude", formalDefinition="Which kinds of designation to exclude from the expansion." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/designation-use")
        protected Coding use;

        private static final long serialVersionUID = 242239292L;

    /**
     * Constructor
     */
      public DesignationExcludeDesignationComponent() {
        super();
      }

        /**
         * @return {@link #language} (The language this designation is defined for.). This is the underlying object with id, value and extensions. The accessor "getLanguage" gives direct access to the value
         */
        public CodeType getLanguageElement() { 
          if (this.language == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DesignationExcludeDesignationComponent.language");
            else if (Configuration.doAutoCreate())
              this.language = new CodeType(); // bb
          return this.language;
        }

        public boolean hasLanguageElement() { 
          return this.language != null && !this.language.isEmpty();
        }

        public boolean hasLanguage() { 
          return this.language != null && !this.language.isEmpty();
        }

        /**
         * @param value {@link #language} (The language this designation is defined for.). This is the underlying object with id, value and extensions. The accessor "getLanguage" gives direct access to the value
         */
        public DesignationExcludeDesignationComponent setLanguageElement(CodeType value) { 
          this.language = value;
          return this;
        }

        /**
         * @return The language this designation is defined for.
         */
        public String getLanguage() { 
          return this.language == null ? null : this.language.getValue();
        }

        /**
         * @param value The language this designation is defined for.
         */
        public DesignationExcludeDesignationComponent setLanguage(String value) { 
          if (Utilities.noString(value))
            this.language = null;
          else {
            if (this.language == null)
              this.language = new CodeType();
            this.language.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #use} (Which kinds of designation to exclude from the expansion.)
         */
        public Coding getUse() { 
          if (this.use == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DesignationExcludeDesignationComponent.use");
            else if (Configuration.doAutoCreate())
              this.use = new Coding(); // cc
          return this.use;
        }

        public boolean hasUse() { 
          return this.use != null && !this.use.isEmpty();
        }

        /**
         * @param value {@link #use} (Which kinds of designation to exclude from the expansion.)
         */
        public DesignationExcludeDesignationComponent setUse(Coding value) { 
          this.use = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("language", "code", "The language this designation is defined for.", 0, java.lang.Integer.MAX_VALUE, language));
          childrenList.add(new Property("use", "Coding", "Which kinds of designation to exclude from the expansion.", 0, java.lang.Integer.MAX_VALUE, use));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1613589672: /*language*/ return this.language == null ? new Base[0] : new Base[] {this.language}; // CodeType
        case 116103: /*use*/ return this.use == null ? new Base[0] : new Base[] {this.use}; // Coding
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1613589672: // language
          this.language = castToCode(value); // CodeType
          return value;
        case 116103: // use
          this.use = castToCoding(value); // Coding
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("language")) {
          this.language = castToCode(value); // CodeType
        } else if (name.equals("use")) {
          this.use = castToCoding(value); // Coding
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1613589672:  return getLanguageElement();
        case 116103:  return getUse(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1613589672: /*language*/ return new String[] {"code"};
        case 116103: /*use*/ return new String[] {"Coding"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("language")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExpansionProfile.language");
        }
        else if (name.equals("use")) {
          this.use = new Coding();
          return this.use;
        }
        else
          return super.addChild(name);
      }

      public DesignationExcludeDesignationComponent copy() {
        DesignationExcludeDesignationComponent dst = new DesignationExcludeDesignationComponent();
        copyValues(dst);
        dst.language = language == null ? null : language.copy();
        dst.use = use == null ? null : use.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof DesignationExcludeDesignationComponent))
          return false;
        DesignationExcludeDesignationComponent o = (DesignationExcludeDesignationComponent) other;
        return compareDeep(language, o.language, true) && compareDeep(use, o.use, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DesignationExcludeDesignationComponent))
          return false;
        DesignationExcludeDesignationComponent o = (DesignationExcludeDesignationComponent) other;
        return compareValues(language, o.language, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(language, use);
      }

  public String fhirType() {
    return "ExpansionProfile.designation.exclude.designation";

  }

  }

    /**
     * A formal identifier that is used to identify this expansion profile when it is represented in other formats, or referenced in a specification, model, design or an instance.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Additional identifier for the expansion profile", formalDefinition="A formal identifier that is used to identify this expansion profile when it is represented in other formats, or referenced in a specification, model, design or an instance." )
    protected Identifier identifier;

    /**
     * Fix use of a particular code system to a particular version.
     */
    @Child(name = "fixedVersion", type = {}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Fix use of a code system to a particular version", formalDefinition="Fix use of a particular code system to a particular version." )
    protected List<ExpansionProfileFixedVersionComponent> fixedVersion;

    /**
     * Code system, or a particular version of a code system to be excluded from value set expansions.
     */
    @Child(name = "excludedSystem", type = {}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Systems/Versions to be exclude", formalDefinition="Code system, or a particular version of a code system to be excluded from value set expansions." )
    protected ExpansionProfileExcludedSystemComponent excludedSystem;

    /**
     * Controls whether concept designations are to be included or excluded in value set expansions.
     */
    @Child(name = "includeDesignations", type = {BooleanType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Whether the expansion should include concept designations", formalDefinition="Controls whether concept designations are to be included or excluded in value set expansions." )
    protected BooleanType includeDesignations;

    /**
     * A set of criteria that provide the constraints imposed on the value set expansion by including or excluding designations.
     */
    @Child(name = "designation", type = {}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When the expansion profile imposes designation contraints", formalDefinition="A set of criteria that provide the constraints imposed on the value set expansion by including or excluding designations." )
    protected ExpansionProfileDesignationComponent designation;

    /**
     * Controls whether the value set definition is included or excluded in value set expansions.
     */
    @Child(name = "includeDefinition", type = {BooleanType.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Include or exclude the value set definition in the expansion", formalDefinition="Controls whether the value set definition is included or excluded in value set expansions." )
    protected BooleanType includeDefinition;

    /**
     * Controls whether inactive concepts are included or excluded in value set expansions.
     */
    @Child(name = "activeOnly", type = {BooleanType.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Include or exclude inactive concepts in the expansion", formalDefinition="Controls whether inactive concepts are included or excluded in value set expansions." )
    protected BooleanType activeOnly;

    /**
     * Controls whether or not the value set expansion nests codes or not (i.e. ValueSet.expansion.contains.contains).
     */
    @Child(name = "excludeNested", type = {BooleanType.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Nested codes in the expansion or not", formalDefinition="Controls whether or not the value set expansion nests codes or not (i.e. ValueSet.expansion.contains.contains)." )
    protected BooleanType excludeNested;

    /**
     * Controls whether or not the value set expansion includes codes which cannot be displayed in user interfaces.
     */
    @Child(name = "excludeNotForUI", type = {BooleanType.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Include or exclude codes which cannot be rendered in user interfaces in the value set expansion", formalDefinition="Controls whether or not the value set expansion includes codes which cannot be displayed in user interfaces." )
    protected BooleanType excludeNotForUI;

    /**
     * Controls whether or not the value set expansion includes post coordinated codes.
     */
    @Child(name = "excludePostCoordinated", type = {BooleanType.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Include or exclude codes which are post coordinated expressions in the value set expansion", formalDefinition="Controls whether or not the value set expansion includes post coordinated codes." )
    protected BooleanType excludePostCoordinated;

    /**
     * Specifies the language to be used for description in the expansions i.e. the language to be used for ValueSet.expansion.contains.display.
     */
    @Child(name = "displayLanguage", type = {CodeType.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Specify the language for the display element of codes in the value set expansion", formalDefinition="Specifies the language to be used for description in the expansions i.e. the language to be used for ValueSet.expansion.contains.display." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/languages")
    protected CodeType displayLanguage;

    /**
     * If the value set being expanded is incomplete (because it is too big to expand), return a limited expansion (a subset) with an indicator that expansion is incomplete, using the extension [http://hl7.org/fhir/StructureDefinition/valueset-toocostly](extension-valueset-toocostly.html).
     */
    @Child(name = "limitedExpansion", type = {BooleanType.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Controls behaviour of the value set expand operation when value sets are too large to be completely expanded", formalDefinition="If the value set being expanded is incomplete (because it is too big to expand), return a limited expansion (a subset) with an indicator that expansion is incomplete, using the extension [http://hl7.org/fhir/StructureDefinition/valueset-toocostly](extension-valueset-toocostly.html)." )
    protected BooleanType limitedExpansion;

    private static final long serialVersionUID = 1067457001L;

  /**
   * Constructor
   */
    public ExpansionProfile() {
      super();
    }

  /**
   * Constructor
   */
    public ExpansionProfile(Enumeration<PublicationStatus> status) {
      super();
      this.status = status;
    }

    /**
     * @return {@link #url} (An absolute URI that is used to identify this expansion profile when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this expansion profile is (or will be) published. The URL SHOULD include the major version of the expansion profile. For more information see [Technical and Business Versions](resource.html#versions).). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public UriType getUrlElement() { 
      if (this.url == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExpansionProfile.url");
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
     * @param value {@link #url} (An absolute URI that is used to identify this expansion profile when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this expansion profile is (or will be) published. The URL SHOULD include the major version of the expansion profile. For more information see [Technical and Business Versions](resource.html#versions).). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public ExpansionProfile setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return An absolute URI that is used to identify this expansion profile when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this expansion profile is (or will be) published. The URL SHOULD include the major version of the expansion profile. For more information see [Technical and Business Versions](resource.html#versions).
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value An absolute URI that is used to identify this expansion profile when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this expansion profile is (or will be) published. The URL SHOULD include the major version of the expansion profile. For more information see [Technical and Business Versions](resource.html#versions).
     */
    public ExpansionProfile setUrl(String value) { 
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
     * @return {@link #identifier} (A formal identifier that is used to identify this expansion profile when it is represented in other formats, or referenced in a specification, model, design or an instance.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExpansionProfile.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new Identifier(); // cc
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (A formal identifier that is used to identify this expansion profile when it is represented in other formats, or referenced in a specification, model, design or an instance.)
     */
    public ExpansionProfile setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #version} (The identifier that is used to identify this version of the expansion profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the expansion profile author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public StringType getVersionElement() { 
      if (this.version == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExpansionProfile.version");
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
     * @param value {@link #version} (The identifier that is used to identify this version of the expansion profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the expansion profile author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public ExpansionProfile setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return The identifier that is used to identify this version of the expansion profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the expansion profile author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value The identifier that is used to identify this version of the expansion profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the expansion profile author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.
     */
    public ExpansionProfile setVersion(String value) { 
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
     * @return {@link #name} (A natural language name identifying the expansion profile. This name should be usable as an identifier for the module by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExpansionProfile.name");
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
     * @param value {@link #name} (A natural language name identifying the expansion profile. This name should be usable as an identifier for the module by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public ExpansionProfile setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return A natural language name identifying the expansion profile. This name should be usable as an identifier for the module by machine processing applications such as code generation.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value A natural language name identifying the expansion profile. This name should be usable as an identifier for the module by machine processing applications such as code generation.
     */
    public ExpansionProfile setName(String value) { 
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
     * @return {@link #status} (The status of this expansion profile. Enables tracking the life-cycle of the content.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<PublicationStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExpansionProfile.status");
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
     * @param value {@link #status} (The status of this expansion profile. Enables tracking the life-cycle of the content.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public ExpansionProfile setStatusElement(Enumeration<PublicationStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of this expansion profile. Enables tracking the life-cycle of the content.
     */
    public PublicationStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of this expansion profile. Enables tracking the life-cycle of the content.
     */
    public ExpansionProfile setStatus(PublicationStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<PublicationStatus>(new PublicationStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #experimental} (A boolean value to indicate that this expansion profile is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public BooleanType getExperimentalElement() { 
      if (this.experimental == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExpansionProfile.experimental");
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
     * @param value {@link #experimental} (A boolean value to indicate that this expansion profile is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public ExpansionProfile setExperimentalElement(BooleanType value) { 
      this.experimental = value;
      return this;
    }

    /**
     * @return A boolean value to indicate that this expansion profile is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public boolean getExperimental() { 
      return this.experimental == null || this.experimental.isEmpty() ? false : this.experimental.getValue();
    }

    /**
     * @param value A boolean value to indicate that this expansion profile is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public ExpansionProfile setExperimental(boolean value) { 
        if (this.experimental == null)
          this.experimental = new BooleanType();
        this.experimental.setValue(value);
      return this;
    }

    /**
     * @return {@link #date} (The date  (and optionally time) when the expansion profile was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the expansion profile changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExpansionProfile.date");
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
     * @param value {@link #date} (The date  (and optionally time) when the expansion profile was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the expansion profile changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public ExpansionProfile setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date  (and optionally time) when the expansion profile was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the expansion profile changes.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date  (and optionally time) when the expansion profile was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the expansion profile changes.
     */
    public ExpansionProfile setDate(Date value) { 
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
     * @return {@link #publisher} (The name of the individual or organization that published the expansion profile.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public StringType getPublisherElement() { 
      if (this.publisher == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExpansionProfile.publisher");
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
     * @param value {@link #publisher} (The name of the individual or organization that published the expansion profile.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public ExpansionProfile setPublisherElement(StringType value) { 
      this.publisher = value;
      return this;
    }

    /**
     * @return The name of the individual or organization that published the expansion profile.
     */
    public String getPublisher() { 
      return this.publisher == null ? null : this.publisher.getValue();
    }

    /**
     * @param value The name of the individual or organization that published the expansion profile.
     */
    public ExpansionProfile setPublisher(String value) { 
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
    public ExpansionProfile setContact(List<ContactDetail> theContact) { 
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

    public ExpansionProfile addContact(ContactDetail t) { //3
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
     * @return {@link #description} (A free text natural language description of the expansion profile from a consumer's perspective.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public MarkdownType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExpansionProfile.description");
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
     * @param value {@link #description} (A free text natural language description of the expansion profile from a consumer's perspective.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public ExpansionProfile setDescriptionElement(MarkdownType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A free text natural language description of the expansion profile from a consumer's perspective.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A free text natural language description of the expansion profile from a consumer's perspective.
     */
    public ExpansionProfile setDescription(String value) { 
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
     * @return {@link #useContext} (The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching for appropriate expansion profile instances.)
     */
    public List<UsageContext> getUseContext() { 
      if (this.useContext == null)
        this.useContext = new ArrayList<UsageContext>();
      return this.useContext;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ExpansionProfile setUseContext(List<UsageContext> theUseContext) { 
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

    public ExpansionProfile addUseContext(UsageContext t) { //3
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
     * @return {@link #jurisdiction} (A legal or geographic region in which the expansion profile is intended to be used.)
     */
    public List<CodeableConcept> getJurisdiction() { 
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      return this.jurisdiction;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ExpansionProfile setJurisdiction(List<CodeableConcept> theJurisdiction) { 
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

    public ExpansionProfile addJurisdiction(CodeableConcept t) { //3
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
     * @return {@link #fixedVersion} (Fix use of a particular code system to a particular version.)
     */
    public List<ExpansionProfileFixedVersionComponent> getFixedVersion() { 
      if (this.fixedVersion == null)
        this.fixedVersion = new ArrayList<ExpansionProfileFixedVersionComponent>();
      return this.fixedVersion;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ExpansionProfile setFixedVersion(List<ExpansionProfileFixedVersionComponent> theFixedVersion) { 
      this.fixedVersion = theFixedVersion;
      return this;
    }

    public boolean hasFixedVersion() { 
      if (this.fixedVersion == null)
        return false;
      for (ExpansionProfileFixedVersionComponent item : this.fixedVersion)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ExpansionProfileFixedVersionComponent addFixedVersion() { //3
      ExpansionProfileFixedVersionComponent t = new ExpansionProfileFixedVersionComponent();
      if (this.fixedVersion == null)
        this.fixedVersion = new ArrayList<ExpansionProfileFixedVersionComponent>();
      this.fixedVersion.add(t);
      return t;
    }

    public ExpansionProfile addFixedVersion(ExpansionProfileFixedVersionComponent t) { //3
      if (t == null)
        return this;
      if (this.fixedVersion == null)
        this.fixedVersion = new ArrayList<ExpansionProfileFixedVersionComponent>();
      this.fixedVersion.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #fixedVersion}, creating it if it does not already exist
     */
    public ExpansionProfileFixedVersionComponent getFixedVersionFirstRep() { 
      if (getFixedVersion().isEmpty()) {
        addFixedVersion();
      }
      return getFixedVersion().get(0);
    }

    /**
     * @return {@link #excludedSystem} (Code system, or a particular version of a code system to be excluded from value set expansions.)
     */
    public ExpansionProfileExcludedSystemComponent getExcludedSystem() { 
      if (this.excludedSystem == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExpansionProfile.excludedSystem");
        else if (Configuration.doAutoCreate())
          this.excludedSystem = new ExpansionProfileExcludedSystemComponent(); // cc
      return this.excludedSystem;
    }

    public boolean hasExcludedSystem() { 
      return this.excludedSystem != null && !this.excludedSystem.isEmpty();
    }

    /**
     * @param value {@link #excludedSystem} (Code system, or a particular version of a code system to be excluded from value set expansions.)
     */
    public ExpansionProfile setExcludedSystem(ExpansionProfileExcludedSystemComponent value) { 
      this.excludedSystem = value;
      return this;
    }

    /**
     * @return {@link #includeDesignations} (Controls whether concept designations are to be included or excluded in value set expansions.). This is the underlying object with id, value and extensions. The accessor "getIncludeDesignations" gives direct access to the value
     */
    public BooleanType getIncludeDesignationsElement() { 
      if (this.includeDesignations == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExpansionProfile.includeDesignations");
        else if (Configuration.doAutoCreate())
          this.includeDesignations = new BooleanType(); // bb
      return this.includeDesignations;
    }

    public boolean hasIncludeDesignationsElement() { 
      return this.includeDesignations != null && !this.includeDesignations.isEmpty();
    }

    public boolean hasIncludeDesignations() { 
      return this.includeDesignations != null && !this.includeDesignations.isEmpty();
    }

    /**
     * @param value {@link #includeDesignations} (Controls whether concept designations are to be included or excluded in value set expansions.). This is the underlying object with id, value and extensions. The accessor "getIncludeDesignations" gives direct access to the value
     */
    public ExpansionProfile setIncludeDesignationsElement(BooleanType value) { 
      this.includeDesignations = value;
      return this;
    }

    /**
     * @return Controls whether concept designations are to be included or excluded in value set expansions.
     */
    public boolean getIncludeDesignations() { 
      return this.includeDesignations == null || this.includeDesignations.isEmpty() ? false : this.includeDesignations.getValue();
    }

    /**
     * @param value Controls whether concept designations are to be included or excluded in value set expansions.
     */
    public ExpansionProfile setIncludeDesignations(boolean value) { 
        if (this.includeDesignations == null)
          this.includeDesignations = new BooleanType();
        this.includeDesignations.setValue(value);
      return this;
    }

    /**
     * @return {@link #designation} (A set of criteria that provide the constraints imposed on the value set expansion by including or excluding designations.)
     */
    public ExpansionProfileDesignationComponent getDesignation() { 
      if (this.designation == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExpansionProfile.designation");
        else if (Configuration.doAutoCreate())
          this.designation = new ExpansionProfileDesignationComponent(); // cc
      return this.designation;
    }

    public boolean hasDesignation() { 
      return this.designation != null && !this.designation.isEmpty();
    }

    /**
     * @param value {@link #designation} (A set of criteria that provide the constraints imposed on the value set expansion by including or excluding designations.)
     */
    public ExpansionProfile setDesignation(ExpansionProfileDesignationComponent value) { 
      this.designation = value;
      return this;
    }

    /**
     * @return {@link #includeDefinition} (Controls whether the value set definition is included or excluded in value set expansions.). This is the underlying object with id, value and extensions. The accessor "getIncludeDefinition" gives direct access to the value
     */
    public BooleanType getIncludeDefinitionElement() { 
      if (this.includeDefinition == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExpansionProfile.includeDefinition");
        else if (Configuration.doAutoCreate())
          this.includeDefinition = new BooleanType(); // bb
      return this.includeDefinition;
    }

    public boolean hasIncludeDefinitionElement() { 
      return this.includeDefinition != null && !this.includeDefinition.isEmpty();
    }

    public boolean hasIncludeDefinition() { 
      return this.includeDefinition != null && !this.includeDefinition.isEmpty();
    }

    /**
     * @param value {@link #includeDefinition} (Controls whether the value set definition is included or excluded in value set expansions.). This is the underlying object with id, value and extensions. The accessor "getIncludeDefinition" gives direct access to the value
     */
    public ExpansionProfile setIncludeDefinitionElement(BooleanType value) { 
      this.includeDefinition = value;
      return this;
    }

    /**
     * @return Controls whether the value set definition is included or excluded in value set expansions.
     */
    public boolean getIncludeDefinition() { 
      return this.includeDefinition == null || this.includeDefinition.isEmpty() ? false : this.includeDefinition.getValue();
    }

    /**
     * @param value Controls whether the value set definition is included or excluded in value set expansions.
     */
    public ExpansionProfile setIncludeDefinition(boolean value) { 
        if (this.includeDefinition == null)
          this.includeDefinition = new BooleanType();
        this.includeDefinition.setValue(value);
      return this;
    }

    /**
     * @return {@link #activeOnly} (Controls whether inactive concepts are included or excluded in value set expansions.). This is the underlying object with id, value and extensions. The accessor "getActiveOnly" gives direct access to the value
     */
    public BooleanType getActiveOnlyElement() { 
      if (this.activeOnly == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExpansionProfile.activeOnly");
        else if (Configuration.doAutoCreate())
          this.activeOnly = new BooleanType(); // bb
      return this.activeOnly;
    }

    public boolean hasActiveOnlyElement() { 
      return this.activeOnly != null && !this.activeOnly.isEmpty();
    }

    public boolean hasActiveOnly() { 
      return this.activeOnly != null && !this.activeOnly.isEmpty();
    }

    /**
     * @param value {@link #activeOnly} (Controls whether inactive concepts are included or excluded in value set expansions.). This is the underlying object with id, value and extensions. The accessor "getActiveOnly" gives direct access to the value
     */
    public ExpansionProfile setActiveOnlyElement(BooleanType value) { 
      this.activeOnly = value;
      return this;
    }

    /**
     * @return Controls whether inactive concepts are included or excluded in value set expansions.
     */
    public boolean getActiveOnly() { 
      return this.activeOnly == null || this.activeOnly.isEmpty() ? false : this.activeOnly.getValue();
    }

    /**
     * @param value Controls whether inactive concepts are included or excluded in value set expansions.
     */
    public ExpansionProfile setActiveOnly(boolean value) { 
        if (this.activeOnly == null)
          this.activeOnly = new BooleanType();
        this.activeOnly.setValue(value);
      return this;
    }

    /**
     * @return {@link #excludeNested} (Controls whether or not the value set expansion nests codes or not (i.e. ValueSet.expansion.contains.contains).). This is the underlying object with id, value and extensions. The accessor "getExcludeNested" gives direct access to the value
     */
    public BooleanType getExcludeNestedElement() { 
      if (this.excludeNested == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExpansionProfile.excludeNested");
        else if (Configuration.doAutoCreate())
          this.excludeNested = new BooleanType(); // bb
      return this.excludeNested;
    }

    public boolean hasExcludeNestedElement() { 
      return this.excludeNested != null && !this.excludeNested.isEmpty();
    }

    public boolean hasExcludeNested() { 
      return this.excludeNested != null && !this.excludeNested.isEmpty();
    }

    /**
     * @param value {@link #excludeNested} (Controls whether or not the value set expansion nests codes or not (i.e. ValueSet.expansion.contains.contains).). This is the underlying object with id, value and extensions. The accessor "getExcludeNested" gives direct access to the value
     */
    public ExpansionProfile setExcludeNestedElement(BooleanType value) { 
      this.excludeNested = value;
      return this;
    }

    /**
     * @return Controls whether or not the value set expansion nests codes or not (i.e. ValueSet.expansion.contains.contains).
     */
    public boolean getExcludeNested() { 
      return this.excludeNested == null || this.excludeNested.isEmpty() ? false : this.excludeNested.getValue();
    }

    /**
     * @param value Controls whether or not the value set expansion nests codes or not (i.e. ValueSet.expansion.contains.contains).
     */
    public ExpansionProfile setExcludeNested(boolean value) { 
        if (this.excludeNested == null)
          this.excludeNested = new BooleanType();
        this.excludeNested.setValue(value);
      return this;
    }

    /**
     * @return {@link #excludeNotForUI} (Controls whether or not the value set expansion includes codes which cannot be displayed in user interfaces.). This is the underlying object with id, value and extensions. The accessor "getExcludeNotForUI" gives direct access to the value
     */
    public BooleanType getExcludeNotForUIElement() { 
      if (this.excludeNotForUI == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExpansionProfile.excludeNotForUI");
        else if (Configuration.doAutoCreate())
          this.excludeNotForUI = new BooleanType(); // bb
      return this.excludeNotForUI;
    }

    public boolean hasExcludeNotForUIElement() { 
      return this.excludeNotForUI != null && !this.excludeNotForUI.isEmpty();
    }

    public boolean hasExcludeNotForUI() { 
      return this.excludeNotForUI != null && !this.excludeNotForUI.isEmpty();
    }

    /**
     * @param value {@link #excludeNotForUI} (Controls whether or not the value set expansion includes codes which cannot be displayed in user interfaces.). This is the underlying object with id, value and extensions. The accessor "getExcludeNotForUI" gives direct access to the value
     */
    public ExpansionProfile setExcludeNotForUIElement(BooleanType value) { 
      this.excludeNotForUI = value;
      return this;
    }

    /**
     * @return Controls whether or not the value set expansion includes codes which cannot be displayed in user interfaces.
     */
    public boolean getExcludeNotForUI() { 
      return this.excludeNotForUI == null || this.excludeNotForUI.isEmpty() ? false : this.excludeNotForUI.getValue();
    }

    /**
     * @param value Controls whether or not the value set expansion includes codes which cannot be displayed in user interfaces.
     */
    public ExpansionProfile setExcludeNotForUI(boolean value) { 
        if (this.excludeNotForUI == null)
          this.excludeNotForUI = new BooleanType();
        this.excludeNotForUI.setValue(value);
      return this;
    }

    /**
     * @return {@link #excludePostCoordinated} (Controls whether or not the value set expansion includes post coordinated codes.). This is the underlying object with id, value and extensions. The accessor "getExcludePostCoordinated" gives direct access to the value
     */
    public BooleanType getExcludePostCoordinatedElement() { 
      if (this.excludePostCoordinated == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExpansionProfile.excludePostCoordinated");
        else if (Configuration.doAutoCreate())
          this.excludePostCoordinated = new BooleanType(); // bb
      return this.excludePostCoordinated;
    }

    public boolean hasExcludePostCoordinatedElement() { 
      return this.excludePostCoordinated != null && !this.excludePostCoordinated.isEmpty();
    }

    public boolean hasExcludePostCoordinated() { 
      return this.excludePostCoordinated != null && !this.excludePostCoordinated.isEmpty();
    }

    /**
     * @param value {@link #excludePostCoordinated} (Controls whether or not the value set expansion includes post coordinated codes.). This is the underlying object with id, value and extensions. The accessor "getExcludePostCoordinated" gives direct access to the value
     */
    public ExpansionProfile setExcludePostCoordinatedElement(BooleanType value) { 
      this.excludePostCoordinated = value;
      return this;
    }

    /**
     * @return Controls whether or not the value set expansion includes post coordinated codes.
     */
    public boolean getExcludePostCoordinated() { 
      return this.excludePostCoordinated == null || this.excludePostCoordinated.isEmpty() ? false : this.excludePostCoordinated.getValue();
    }

    /**
     * @param value Controls whether or not the value set expansion includes post coordinated codes.
     */
    public ExpansionProfile setExcludePostCoordinated(boolean value) { 
        if (this.excludePostCoordinated == null)
          this.excludePostCoordinated = new BooleanType();
        this.excludePostCoordinated.setValue(value);
      return this;
    }

    /**
     * @return {@link #displayLanguage} (Specifies the language to be used for description in the expansions i.e. the language to be used for ValueSet.expansion.contains.display.). This is the underlying object with id, value and extensions. The accessor "getDisplayLanguage" gives direct access to the value
     */
    public CodeType getDisplayLanguageElement() { 
      if (this.displayLanguage == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExpansionProfile.displayLanguage");
        else if (Configuration.doAutoCreate())
          this.displayLanguage = new CodeType(); // bb
      return this.displayLanguage;
    }

    public boolean hasDisplayLanguageElement() { 
      return this.displayLanguage != null && !this.displayLanguage.isEmpty();
    }

    public boolean hasDisplayLanguage() { 
      return this.displayLanguage != null && !this.displayLanguage.isEmpty();
    }

    /**
     * @param value {@link #displayLanguage} (Specifies the language to be used for description in the expansions i.e. the language to be used for ValueSet.expansion.contains.display.). This is the underlying object with id, value and extensions. The accessor "getDisplayLanguage" gives direct access to the value
     */
    public ExpansionProfile setDisplayLanguageElement(CodeType value) { 
      this.displayLanguage = value;
      return this;
    }

    /**
     * @return Specifies the language to be used for description in the expansions i.e. the language to be used for ValueSet.expansion.contains.display.
     */
    public String getDisplayLanguage() { 
      return this.displayLanguage == null ? null : this.displayLanguage.getValue();
    }

    /**
     * @param value Specifies the language to be used for description in the expansions i.e. the language to be used for ValueSet.expansion.contains.display.
     */
    public ExpansionProfile setDisplayLanguage(String value) { 
      if (Utilities.noString(value))
        this.displayLanguage = null;
      else {
        if (this.displayLanguage == null)
          this.displayLanguage = new CodeType();
        this.displayLanguage.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #limitedExpansion} (If the value set being expanded is incomplete (because it is too big to expand), return a limited expansion (a subset) with an indicator that expansion is incomplete, using the extension [http://hl7.org/fhir/StructureDefinition/valueset-toocostly](extension-valueset-toocostly.html).). This is the underlying object with id, value and extensions. The accessor "getLimitedExpansion" gives direct access to the value
     */
    public BooleanType getLimitedExpansionElement() { 
      if (this.limitedExpansion == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExpansionProfile.limitedExpansion");
        else if (Configuration.doAutoCreate())
          this.limitedExpansion = new BooleanType(); // bb
      return this.limitedExpansion;
    }

    public boolean hasLimitedExpansionElement() { 
      return this.limitedExpansion != null && !this.limitedExpansion.isEmpty();
    }

    public boolean hasLimitedExpansion() { 
      return this.limitedExpansion != null && !this.limitedExpansion.isEmpty();
    }

    /**
     * @param value {@link #limitedExpansion} (If the value set being expanded is incomplete (because it is too big to expand), return a limited expansion (a subset) with an indicator that expansion is incomplete, using the extension [http://hl7.org/fhir/StructureDefinition/valueset-toocostly](extension-valueset-toocostly.html).). This is the underlying object with id, value and extensions. The accessor "getLimitedExpansion" gives direct access to the value
     */
    public ExpansionProfile setLimitedExpansionElement(BooleanType value) { 
      this.limitedExpansion = value;
      return this;
    }

    /**
     * @return If the value set being expanded is incomplete (because it is too big to expand), return a limited expansion (a subset) with an indicator that expansion is incomplete, using the extension [http://hl7.org/fhir/StructureDefinition/valueset-toocostly](extension-valueset-toocostly.html).
     */
    public boolean getLimitedExpansion() { 
      return this.limitedExpansion == null || this.limitedExpansion.isEmpty() ? false : this.limitedExpansion.getValue();
    }

    /**
     * @param value If the value set being expanded is incomplete (because it is too big to expand), return a limited expansion (a subset) with an indicator that expansion is incomplete, using the extension [http://hl7.org/fhir/StructureDefinition/valueset-toocostly](extension-valueset-toocostly.html).
     */
    public ExpansionProfile setLimitedExpansion(boolean value) { 
        if (this.limitedExpansion == null)
          this.limitedExpansion = new BooleanType();
        this.limitedExpansion.setValue(value);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("url", "uri", "An absolute URI that is used to identify this expansion profile when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this expansion profile is (or will be) published. The URL SHOULD include the major version of the expansion profile. For more information see [Technical and Business Versions](resource.html#versions).", 0, java.lang.Integer.MAX_VALUE, url));
        childrenList.add(new Property("identifier", "Identifier", "A formal identifier that is used to identify this expansion profile when it is represented in other formats, or referenced in a specification, model, design or an instance.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("version", "string", "The identifier that is used to identify this version of the expansion profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the expansion profile author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.", 0, java.lang.Integer.MAX_VALUE, version));
        childrenList.add(new Property("name", "string", "A natural language name identifying the expansion profile. This name should be usable as an identifier for the module by machine processing applications such as code generation.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("status", "code", "The status of this expansion profile. Enables tracking the life-cycle of the content.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("experimental", "boolean", "A boolean value to indicate that this expansion profile is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.", 0, java.lang.Integer.MAX_VALUE, experimental));
        childrenList.add(new Property("date", "dateTime", "The date  (and optionally time) when the expansion profile was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the expansion profile changes.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("publisher", "string", "The name of the individual or organization that published the expansion profile.", 0, java.lang.Integer.MAX_VALUE, publisher));
        childrenList.add(new Property("contact", "ContactDetail", "Contact details to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact));
        childrenList.add(new Property("description", "markdown", "A free text natural language description of the expansion profile from a consumer's perspective.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("useContext", "UsageContext", "The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching for appropriate expansion profile instances.", 0, java.lang.Integer.MAX_VALUE, useContext));
        childrenList.add(new Property("jurisdiction", "CodeableConcept", "A legal or geographic region in which the expansion profile is intended to be used.", 0, java.lang.Integer.MAX_VALUE, jurisdiction));
        childrenList.add(new Property("fixedVersion", "", "Fix use of a particular code system to a particular version.", 0, java.lang.Integer.MAX_VALUE, fixedVersion));
        childrenList.add(new Property("excludedSystem", "", "Code system, or a particular version of a code system to be excluded from value set expansions.", 0, java.lang.Integer.MAX_VALUE, excludedSystem));
        childrenList.add(new Property("includeDesignations", "boolean", "Controls whether concept designations are to be included or excluded in value set expansions.", 0, java.lang.Integer.MAX_VALUE, includeDesignations));
        childrenList.add(new Property("designation", "", "A set of criteria that provide the constraints imposed on the value set expansion by including or excluding designations.", 0, java.lang.Integer.MAX_VALUE, designation));
        childrenList.add(new Property("includeDefinition", "boolean", "Controls whether the value set definition is included or excluded in value set expansions.", 0, java.lang.Integer.MAX_VALUE, includeDefinition));
        childrenList.add(new Property("activeOnly", "boolean", "Controls whether inactive concepts are included or excluded in value set expansions.", 0, java.lang.Integer.MAX_VALUE, activeOnly));
        childrenList.add(new Property("excludeNested", "boolean", "Controls whether or not the value set expansion nests codes or not (i.e. ValueSet.expansion.contains.contains).", 0, java.lang.Integer.MAX_VALUE, excludeNested));
        childrenList.add(new Property("excludeNotForUI", "boolean", "Controls whether or not the value set expansion includes codes which cannot be displayed in user interfaces.", 0, java.lang.Integer.MAX_VALUE, excludeNotForUI));
        childrenList.add(new Property("excludePostCoordinated", "boolean", "Controls whether or not the value set expansion includes post coordinated codes.", 0, java.lang.Integer.MAX_VALUE, excludePostCoordinated));
        childrenList.add(new Property("displayLanguage", "code", "Specifies the language to be used for description in the expansions i.e. the language to be used for ValueSet.expansion.contains.display.", 0, java.lang.Integer.MAX_VALUE, displayLanguage));
        childrenList.add(new Property("limitedExpansion", "boolean", "If the value set being expanded is incomplete (because it is too big to expand), return a limited expansion (a subset) with an indicator that expansion is incomplete, using the extension [http://hl7.org/fhir/StructureDefinition/valueset-toocostly](extension-valueset-toocostly.html).", 0, java.lang.Integer.MAX_VALUE, limitedExpansion));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
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
        case 807748292: /*fixedVersion*/ return this.fixedVersion == null ? new Base[0] : this.fixedVersion.toArray(new Base[this.fixedVersion.size()]); // ExpansionProfileFixedVersionComponent
        case 2125282457: /*excludedSystem*/ return this.excludedSystem == null ? new Base[0] : new Base[] {this.excludedSystem}; // ExpansionProfileExcludedSystemComponent
        case 461507620: /*includeDesignations*/ return this.includeDesignations == null ? new Base[0] : new Base[] {this.includeDesignations}; // BooleanType
        case -900931593: /*designation*/ return this.designation == null ? new Base[0] : new Base[] {this.designation}; // ExpansionProfileDesignationComponent
        case 127972379: /*includeDefinition*/ return this.includeDefinition == null ? new Base[0] : new Base[] {this.includeDefinition}; // BooleanType
        case 2043813842: /*activeOnly*/ return this.activeOnly == null ? new Base[0] : new Base[] {this.activeOnly}; // BooleanType
        case 424992625: /*excludeNested*/ return this.excludeNested == null ? new Base[0] : new Base[] {this.excludeNested}; // BooleanType
        case 667582980: /*excludeNotForUI*/ return this.excludeNotForUI == null ? new Base[0] : new Base[] {this.excludeNotForUI}; // BooleanType
        case 563335154: /*excludePostCoordinated*/ return this.excludePostCoordinated == null ? new Base[0] : new Base[] {this.excludePostCoordinated}; // BooleanType
        case 1486237242: /*displayLanguage*/ return this.displayLanguage == null ? new Base[0] : new Base[] {this.displayLanguage}; // CodeType
        case 597771333: /*limitedExpansion*/ return this.limitedExpansion == null ? new Base[0] : new Base[] {this.limitedExpansion}; // BooleanType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 116079: // url
          this.url = castToUri(value); // UriType
          return value;
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
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
        case 807748292: // fixedVersion
          this.getFixedVersion().add((ExpansionProfileFixedVersionComponent) value); // ExpansionProfileFixedVersionComponent
          return value;
        case 2125282457: // excludedSystem
          this.excludedSystem = (ExpansionProfileExcludedSystemComponent) value; // ExpansionProfileExcludedSystemComponent
          return value;
        case 461507620: // includeDesignations
          this.includeDesignations = castToBoolean(value); // BooleanType
          return value;
        case -900931593: // designation
          this.designation = (ExpansionProfileDesignationComponent) value; // ExpansionProfileDesignationComponent
          return value;
        case 127972379: // includeDefinition
          this.includeDefinition = castToBoolean(value); // BooleanType
          return value;
        case 2043813842: // activeOnly
          this.activeOnly = castToBoolean(value); // BooleanType
          return value;
        case 424992625: // excludeNested
          this.excludeNested = castToBoolean(value); // BooleanType
          return value;
        case 667582980: // excludeNotForUI
          this.excludeNotForUI = castToBoolean(value); // BooleanType
          return value;
        case 563335154: // excludePostCoordinated
          this.excludePostCoordinated = castToBoolean(value); // BooleanType
          return value;
        case 1486237242: // displayLanguage
          this.displayLanguage = castToCode(value); // CodeType
          return value;
        case 597771333: // limitedExpansion
          this.limitedExpansion = castToBoolean(value); // BooleanType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("url")) {
          this.url = castToUri(value); // UriType
        } else if (name.equals("identifier")) {
          this.identifier = castToIdentifier(value); // Identifier
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
        } else if (name.equals("fixedVersion")) {
          this.getFixedVersion().add((ExpansionProfileFixedVersionComponent) value);
        } else if (name.equals("excludedSystem")) {
          this.excludedSystem = (ExpansionProfileExcludedSystemComponent) value; // ExpansionProfileExcludedSystemComponent
        } else if (name.equals("includeDesignations")) {
          this.includeDesignations = castToBoolean(value); // BooleanType
        } else if (name.equals("designation")) {
          this.designation = (ExpansionProfileDesignationComponent) value; // ExpansionProfileDesignationComponent
        } else if (name.equals("includeDefinition")) {
          this.includeDefinition = castToBoolean(value); // BooleanType
        } else if (name.equals("activeOnly")) {
          this.activeOnly = castToBoolean(value); // BooleanType
        } else if (name.equals("excludeNested")) {
          this.excludeNested = castToBoolean(value); // BooleanType
        } else if (name.equals("excludeNotForUI")) {
          this.excludeNotForUI = castToBoolean(value); // BooleanType
        } else if (name.equals("excludePostCoordinated")) {
          this.excludePostCoordinated = castToBoolean(value); // BooleanType
        } else if (name.equals("displayLanguage")) {
          this.displayLanguage = castToCode(value); // CodeType
        } else if (name.equals("limitedExpansion")) {
          this.limitedExpansion = castToBoolean(value); // BooleanType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116079:  return getUrlElement();
        case -1618432855:  return getIdentifier(); 
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
        case 807748292:  return addFixedVersion(); 
        case 2125282457:  return getExcludedSystem(); 
        case 461507620:  return getIncludeDesignationsElement();
        case -900931593:  return getDesignation(); 
        case 127972379:  return getIncludeDefinitionElement();
        case 2043813842:  return getActiveOnlyElement();
        case 424992625:  return getExcludeNestedElement();
        case 667582980:  return getExcludeNotForUIElement();
        case 563335154:  return getExcludePostCoordinatedElement();
        case 1486237242:  return getDisplayLanguageElement();
        case 597771333:  return getLimitedExpansionElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116079: /*url*/ return new String[] {"uri"};
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
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
        case 807748292: /*fixedVersion*/ return new String[] {};
        case 2125282457: /*excludedSystem*/ return new String[] {};
        case 461507620: /*includeDesignations*/ return new String[] {"boolean"};
        case -900931593: /*designation*/ return new String[] {};
        case 127972379: /*includeDefinition*/ return new String[] {"boolean"};
        case 2043813842: /*activeOnly*/ return new String[] {"boolean"};
        case 424992625: /*excludeNested*/ return new String[] {"boolean"};
        case 667582980: /*excludeNotForUI*/ return new String[] {"boolean"};
        case 563335154: /*excludePostCoordinated*/ return new String[] {"boolean"};
        case 1486237242: /*displayLanguage*/ return new String[] {"code"};
        case 597771333: /*limitedExpansion*/ return new String[] {"boolean"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExpansionProfile.url");
        }
        else if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExpansionProfile.version");
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExpansionProfile.name");
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExpansionProfile.status");
        }
        else if (name.equals("experimental")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExpansionProfile.experimental");
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExpansionProfile.date");
        }
        else if (name.equals("publisher")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExpansionProfile.publisher");
        }
        else if (name.equals("contact")) {
          return addContact();
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExpansionProfile.description");
        }
        else if (name.equals("useContext")) {
          return addUseContext();
        }
        else if (name.equals("jurisdiction")) {
          return addJurisdiction();
        }
        else if (name.equals("fixedVersion")) {
          return addFixedVersion();
        }
        else if (name.equals("excludedSystem")) {
          this.excludedSystem = new ExpansionProfileExcludedSystemComponent();
          return this.excludedSystem;
        }
        else if (name.equals("includeDesignations")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExpansionProfile.includeDesignations");
        }
        else if (name.equals("designation")) {
          this.designation = new ExpansionProfileDesignationComponent();
          return this.designation;
        }
        else if (name.equals("includeDefinition")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExpansionProfile.includeDefinition");
        }
        else if (name.equals("activeOnly")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExpansionProfile.activeOnly");
        }
        else if (name.equals("excludeNested")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExpansionProfile.excludeNested");
        }
        else if (name.equals("excludeNotForUI")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExpansionProfile.excludeNotForUI");
        }
        else if (name.equals("excludePostCoordinated")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExpansionProfile.excludePostCoordinated");
        }
        else if (name.equals("displayLanguage")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExpansionProfile.displayLanguage");
        }
        else if (name.equals("limitedExpansion")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExpansionProfile.limitedExpansion");
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "ExpansionProfile";

  }

      public ExpansionProfile copy() {
        ExpansionProfile dst = new ExpansionProfile();
        copyValues(dst);
        dst.url = url == null ? null : url.copy();
        dst.identifier = identifier == null ? null : identifier.copy();
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
        if (fixedVersion != null) {
          dst.fixedVersion = new ArrayList<ExpansionProfileFixedVersionComponent>();
          for (ExpansionProfileFixedVersionComponent i : fixedVersion)
            dst.fixedVersion.add(i.copy());
        };
        dst.excludedSystem = excludedSystem == null ? null : excludedSystem.copy();
        dst.includeDesignations = includeDesignations == null ? null : includeDesignations.copy();
        dst.designation = designation == null ? null : designation.copy();
        dst.includeDefinition = includeDefinition == null ? null : includeDefinition.copy();
        dst.activeOnly = activeOnly == null ? null : activeOnly.copy();
        dst.excludeNested = excludeNested == null ? null : excludeNested.copy();
        dst.excludeNotForUI = excludeNotForUI == null ? null : excludeNotForUI.copy();
        dst.excludePostCoordinated = excludePostCoordinated == null ? null : excludePostCoordinated.copy();
        dst.displayLanguage = displayLanguage == null ? null : displayLanguage.copy();
        dst.limitedExpansion = limitedExpansion == null ? null : limitedExpansion.copy();
        return dst;
      }

      protected ExpansionProfile typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ExpansionProfile))
          return false;
        ExpansionProfile o = (ExpansionProfile) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(fixedVersion, o.fixedVersion, true)
           && compareDeep(excludedSystem, o.excludedSystem, true) && compareDeep(includeDesignations, o.includeDesignations, true)
           && compareDeep(designation, o.designation, true) && compareDeep(includeDefinition, o.includeDefinition, true)
           && compareDeep(activeOnly, o.activeOnly, true) && compareDeep(excludeNested, o.excludeNested, true)
           && compareDeep(excludeNotForUI, o.excludeNotForUI, true) && compareDeep(excludePostCoordinated, o.excludePostCoordinated, true)
           && compareDeep(displayLanguage, o.displayLanguage, true) && compareDeep(limitedExpansion, o.limitedExpansion, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ExpansionProfile))
          return false;
        ExpansionProfile o = (ExpansionProfile) other;
        return compareValues(includeDesignations, o.includeDesignations, true) && compareValues(includeDefinition, o.includeDefinition, true)
           && compareValues(activeOnly, o.activeOnly, true) && compareValues(excludeNested, o.excludeNested, true)
           && compareValues(excludeNotForUI, o.excludeNotForUI, true) && compareValues(excludePostCoordinated, o.excludePostCoordinated, true)
           && compareValues(displayLanguage, o.displayLanguage, true) && compareValues(limitedExpansion, o.limitedExpansion, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, fixedVersion, excludedSystem
          , includeDesignations, designation, includeDefinition, activeOnly, excludeNested, excludeNotForUI
          , excludePostCoordinated, displayLanguage, limitedExpansion);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ExpansionProfile;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>The expansion profile publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ExpansionProfile.date</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="ExpansionProfile.date", description="The expansion profile publication date", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>The expansion profile publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ExpansionProfile.date</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>External identifier for the expansion profile</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ExpansionProfile.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="ExpansionProfile.identifier", description="External identifier for the expansion profile", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>External identifier for the expansion profile</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ExpansionProfile.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>jurisdiction</b>
   * <p>
   * Description: <b>Intended jurisdiction for the expansion profile</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ExpansionProfile.jurisdiction</b><br>
   * </p>
   */
  @SearchParamDefinition(name="jurisdiction", path="ExpansionProfile.jurisdiction", description="Intended jurisdiction for the expansion profile", type="token" )
  public static final String SP_JURISDICTION = "jurisdiction";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>jurisdiction</b>
   * <p>
   * Description: <b>Intended jurisdiction for the expansion profile</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ExpansionProfile.jurisdiction</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam JURISDICTION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_JURISDICTION);

 /**
   * Search parameter: <b>name</b>
   * <p>
   * Description: <b>Computationally friendly name of the expansion profile</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ExpansionProfile.name</b><br>
   * </p>
   */
  @SearchParamDefinition(name="name", path="ExpansionProfile.name", description="Computationally friendly name of the expansion profile", type="string" )
  public static final String SP_NAME = "name";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>name</b>
   * <p>
   * Description: <b>Computationally friendly name of the expansion profile</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ExpansionProfile.name</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam NAME = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_NAME);

 /**
   * Search parameter: <b>description</b>
   * <p>
   * Description: <b>The description of the expansion profile</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ExpansionProfile.description</b><br>
   * </p>
   */
  @SearchParamDefinition(name="description", path="ExpansionProfile.description", description="The description of the expansion profile", type="string" )
  public static final String SP_DESCRIPTION = "description";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>description</b>
   * <p>
   * Description: <b>The description of the expansion profile</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ExpansionProfile.description</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam DESCRIPTION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_DESCRIPTION);

 /**
   * Search parameter: <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the expansion profile</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ExpansionProfile.publisher</b><br>
   * </p>
   */
  @SearchParamDefinition(name="publisher", path="ExpansionProfile.publisher", description="Name of the publisher of the expansion profile", type="string" )
  public static final String SP_PUBLISHER = "publisher";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the expansion profile</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ExpansionProfile.publisher</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam PUBLISHER = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_PUBLISHER);

 /**
   * Search parameter: <b>version</b>
   * <p>
   * Description: <b>The business version of the expansion profile</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ExpansionProfile.version</b><br>
   * </p>
   */
  @SearchParamDefinition(name="version", path="ExpansionProfile.version", description="The business version of the expansion profile", type="token" )
  public static final String SP_VERSION = "version";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>version</b>
   * <p>
   * Description: <b>The business version of the expansion profile</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ExpansionProfile.version</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam VERSION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_VERSION);

 /**
   * Search parameter: <b>url</b>
   * <p>
   * Description: <b>The uri that identifies the expansion profile</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ExpansionProfile.url</b><br>
   * </p>
   */
  @SearchParamDefinition(name="url", path="ExpansionProfile.url", description="The uri that identifies the expansion profile", type="uri" )
  public static final String SP_URL = "url";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>url</b>
   * <p>
   * Description: <b>The uri that identifies the expansion profile</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ExpansionProfile.url</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam URL = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_URL);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The current status of the expansion profile</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ExpansionProfile.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="ExpansionProfile.status", description="The current status of the expansion profile", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The current status of the expansion profile</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ExpansionProfile.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

