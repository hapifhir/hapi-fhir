package org.hl7.fhir.dstu21.model;

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

// Generated on Mon, Dec 21, 2015 20:18-0500 for FHIR v1.2.0
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hl7.fhir.dstu21.exceptions.FHIRException;
import org.hl7.fhir.dstu21.model.Enumerations.ConformanceResourceStatus;
import org.hl7.fhir.dstu21.model.Enumerations.ConformanceResourceStatusEnumFactory;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.utilities.Utilities;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
/**
 * Resource to define constraints on the Expansion of a FHIR ValueSet.
 */
@ResourceDef(name="ExpansionProfile", profile="http://hl7.org/fhir/Profile/ExpansionProfile")
public class ExpansionProfile extends DomainResource {

    @Block()
    public static class ExpansionProfileContactComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The name of an individual to contact regarding the expansion profile.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Name of an individual to contact", formalDefinition="The name of an individual to contact regarding the expansion profile." )
        protected StringType name;

        /**
         * Contact details for individual (if a name was provided) or the publisher.
         */
        @Child(name = "telecom", type = {ContactPoint.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Contact details for individual or publisher", formalDefinition="Contact details for individual (if a name was provided) or the publisher." )
        protected List<ContactPoint> telecom;

        private static final long serialVersionUID = -1179697803L;

    /**
     * Constructor
     */
      public ExpansionProfileContactComponent() {
        super();
      }

        /**
         * @return {@link #name} (The name of an individual to contact regarding the expansion profile.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExpansionProfileContactComponent.name");
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
         * @param value {@link #name} (The name of an individual to contact regarding the expansion profile.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public ExpansionProfileContactComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return The name of an individual to contact regarding the expansion profile.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value The name of an individual to contact regarding the expansion profile.
         */
        public ExpansionProfileContactComponent setName(String value) { 
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
        public ExpansionProfileContactComponent addTelecom(ContactPoint t) { //3
          if (t == null)
            return this;
          if (this.telecom == null)
            this.telecom = new ArrayList<ContactPoint>();
          this.telecom.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "string", "The name of an individual to contact regarding the expansion profile.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("telecom", "ContactPoint", "Contact details for individual (if a name was provided) or the publisher.", 0, java.lang.Integer.MAX_VALUE, telecom));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name"))
          this.name = castToString(value); // StringType
        else if (name.equals("telecom"))
          this.getTelecom().add(castToContactPoint(value));
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExpansionProfile.name");
        }
        else if (name.equals("telecom")) {
          return addTelecom();
        }
        else
          return super.addChild(name);
      }

      public ExpansionProfileContactComponent copy() {
        ExpansionProfileContactComponent dst = new ExpansionProfileContactComponent();
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
        if (!(other instanceof ExpansionProfileContactComponent))
          return false;
        ExpansionProfileContactComponent o = (ExpansionProfileContactComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(telecom, o.telecom, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ExpansionProfileContactComponent))
          return false;
        ExpansionProfileContactComponent o = (ExpansionProfileContactComponent) other;
        return compareValues(name, o.name, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (telecom == null || telecom.isEmpty())
          ;
      }

  public String fhirType() {
    return "ExpansionProfile.contact";

  }

  }

    @Block()
    public static class ExpansionProfileCodeSystemComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Code systems to be included in value set expansions.
         */
        @Child(name = "include", type = {}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Code systems to be included", formalDefinition="Code systems to be included in value set expansions." )
        protected ExpansionProfileCodeSystemIncludeComponent include;

        /**
         * Code systems to be excluded from value set expansions.
         */
        @Child(name = "exclude", type = {}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Code systems to be excluded", formalDefinition="Code systems to be excluded from value set expansions." )
        protected ExpansionProfileCodeSystemExcludeComponent exclude;

        private static final long serialVersionUID = 141288544L;

    /**
     * Constructor
     */
      public ExpansionProfileCodeSystemComponent() {
        super();
      }

        /**
         * @return {@link #include} (Code systems to be included in value set expansions.)
         */
        public ExpansionProfileCodeSystemIncludeComponent getInclude() { 
          if (this.include == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExpansionProfileCodeSystemComponent.include");
            else if (Configuration.doAutoCreate())
              this.include = new ExpansionProfileCodeSystemIncludeComponent(); // cc
          return this.include;
        }

        public boolean hasInclude() { 
          return this.include != null && !this.include.isEmpty();
        }

        /**
         * @param value {@link #include} (Code systems to be included in value set expansions.)
         */
        public ExpansionProfileCodeSystemComponent setInclude(ExpansionProfileCodeSystemIncludeComponent value) { 
          this.include = value;
          return this;
        }

        /**
         * @return {@link #exclude} (Code systems to be excluded from value set expansions.)
         */
        public ExpansionProfileCodeSystemExcludeComponent getExclude() { 
          if (this.exclude == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExpansionProfileCodeSystemComponent.exclude");
            else if (Configuration.doAutoCreate())
              this.exclude = new ExpansionProfileCodeSystemExcludeComponent(); // cc
          return this.exclude;
        }

        public boolean hasExclude() { 
          return this.exclude != null && !this.exclude.isEmpty();
        }

        /**
         * @param value {@link #exclude} (Code systems to be excluded from value set expansions.)
         */
        public ExpansionProfileCodeSystemComponent setExclude(ExpansionProfileCodeSystemExcludeComponent value) { 
          this.exclude = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("include", "", "Code systems to be included in value set expansions.", 0, java.lang.Integer.MAX_VALUE, include));
          childrenList.add(new Property("exclude", "", "Code systems to be excluded from value set expansions.", 0, java.lang.Integer.MAX_VALUE, exclude));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("include"))
          this.include = (ExpansionProfileCodeSystemIncludeComponent) value; // ExpansionProfileCodeSystemIncludeComponent
        else if (name.equals("exclude"))
          this.exclude = (ExpansionProfileCodeSystemExcludeComponent) value; // ExpansionProfileCodeSystemExcludeComponent
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("include")) {
          this.include = new ExpansionProfileCodeSystemIncludeComponent();
          return this.include;
        }
        else if (name.equals("exclude")) {
          this.exclude = new ExpansionProfileCodeSystemExcludeComponent();
          return this.exclude;
        }
        else
          return super.addChild(name);
      }

      public ExpansionProfileCodeSystemComponent copy() {
        ExpansionProfileCodeSystemComponent dst = new ExpansionProfileCodeSystemComponent();
        copyValues(dst);
        dst.include = include == null ? null : include.copy();
        dst.exclude = exclude == null ? null : exclude.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ExpansionProfileCodeSystemComponent))
          return false;
        ExpansionProfileCodeSystemComponent o = (ExpansionProfileCodeSystemComponent) other;
        return compareDeep(include, o.include, true) && compareDeep(exclude, o.exclude, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ExpansionProfileCodeSystemComponent))
          return false;
        ExpansionProfileCodeSystemComponent o = (ExpansionProfileCodeSystemComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (include == null || include.isEmpty()) && (exclude == null || exclude.isEmpty())
          ;
      }

  public String fhirType() {
    return "ExpansionProfile.codeSystem";

  }

  }

    @Block()
    public static class ExpansionProfileCodeSystemIncludeComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A data group for each code system to be included.
         */
        @Child(name = "codeSystem", type = {}, order=1, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="The code systems to be included", formalDefinition="A data group for each code system to be included." )
        protected List<ExpansionProfileCodeSystemIncludeCodeSystemComponent> codeSystem;

        private static final long serialVersionUID = -676240849L;

    /**
     * Constructor
     */
      public ExpansionProfileCodeSystemIncludeComponent() {
        super();
      }

        /**
         * @return {@link #codeSystem} (A data group for each code system to be included.)
         */
        public List<ExpansionProfileCodeSystemIncludeCodeSystemComponent> getCodeSystem() { 
          if (this.codeSystem == null)
            this.codeSystem = new ArrayList<ExpansionProfileCodeSystemIncludeCodeSystemComponent>();
          return this.codeSystem;
        }

        public boolean hasCodeSystem() { 
          if (this.codeSystem == null)
            return false;
          for (ExpansionProfileCodeSystemIncludeCodeSystemComponent item : this.codeSystem)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #codeSystem} (A data group for each code system to be included.)
         */
    // syntactic sugar
        public ExpansionProfileCodeSystemIncludeCodeSystemComponent addCodeSystem() { //3
          ExpansionProfileCodeSystemIncludeCodeSystemComponent t = new ExpansionProfileCodeSystemIncludeCodeSystemComponent();
          if (this.codeSystem == null)
            this.codeSystem = new ArrayList<ExpansionProfileCodeSystemIncludeCodeSystemComponent>();
          this.codeSystem.add(t);
          return t;
        }

    // syntactic sugar
        public ExpansionProfileCodeSystemIncludeComponent addCodeSystem(ExpansionProfileCodeSystemIncludeCodeSystemComponent t) { //3
          if (t == null)
            return this;
          if (this.codeSystem == null)
            this.codeSystem = new ArrayList<ExpansionProfileCodeSystemIncludeCodeSystemComponent>();
          this.codeSystem.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("codeSystem", "", "A data group for each code system to be included.", 0, java.lang.Integer.MAX_VALUE, codeSystem));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("codeSystem"))
          this.getCodeSystem().add((ExpansionProfileCodeSystemIncludeCodeSystemComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("codeSystem")) {
          return addCodeSystem();
        }
        else
          return super.addChild(name);
      }

      public ExpansionProfileCodeSystemIncludeComponent copy() {
        ExpansionProfileCodeSystemIncludeComponent dst = new ExpansionProfileCodeSystemIncludeComponent();
        copyValues(dst);
        if (codeSystem != null) {
          dst.codeSystem = new ArrayList<ExpansionProfileCodeSystemIncludeCodeSystemComponent>();
          for (ExpansionProfileCodeSystemIncludeCodeSystemComponent i : codeSystem)
            dst.codeSystem.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ExpansionProfileCodeSystemIncludeComponent))
          return false;
        ExpansionProfileCodeSystemIncludeComponent o = (ExpansionProfileCodeSystemIncludeComponent) other;
        return compareDeep(codeSystem, o.codeSystem, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ExpansionProfileCodeSystemIncludeComponent))
          return false;
        ExpansionProfileCodeSystemIncludeComponent o = (ExpansionProfileCodeSystemIncludeComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (codeSystem == null || codeSystem.isEmpty());
      }

  public String fhirType() {
    return "ExpansionProfile.codeSystem.include";

  }

  }

    @Block()
    public static class ExpansionProfileCodeSystemIncludeCodeSystemComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * An absolute URI which is the code system to be included.
         */
        @Child(name = "system", type = {UriType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The specific code system to be included", formalDefinition="An absolute URI which is the code system to be included." )
        protected UriType system;

        /**
         * The version of the code system from which codes in the expansion should be included.
         */
        @Child(name = "version", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Specific version of the code system referred to", formalDefinition="The version of the code system from which codes in the expansion should be included." )
        protected StringType version;

        private static final long serialVersionUID = 1145288774L;

    /**
     * Constructor
     */
      public ExpansionProfileCodeSystemIncludeCodeSystemComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ExpansionProfileCodeSystemIncludeCodeSystemComponent(UriType system) {
        super();
        this.system = system;
      }

        /**
         * @return {@link #system} (An absolute URI which is the code system to be included.). This is the underlying object with id, value and extensions. The accessor "getSystem" gives direct access to the value
         */
        public UriType getSystemElement() { 
          if (this.system == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExpansionProfileCodeSystemIncludeCodeSystemComponent.system");
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
         * @param value {@link #system} (An absolute URI which is the code system to be included.). This is the underlying object with id, value and extensions. The accessor "getSystem" gives direct access to the value
         */
        public ExpansionProfileCodeSystemIncludeCodeSystemComponent setSystemElement(UriType value) { 
          this.system = value;
          return this;
        }

        /**
         * @return An absolute URI which is the code system to be included.
         */
        public String getSystem() { 
          return this.system == null ? null : this.system.getValue();
        }

        /**
         * @param value An absolute URI which is the code system to be included.
         */
        public ExpansionProfileCodeSystemIncludeCodeSystemComponent setSystem(String value) { 
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
              throw new Error("Attempt to auto-create ExpansionProfileCodeSystemIncludeCodeSystemComponent.version");
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
        public ExpansionProfileCodeSystemIncludeCodeSystemComponent setVersionElement(StringType value) { 
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
        public ExpansionProfileCodeSystemIncludeCodeSystemComponent setVersion(String value) { 
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
          childrenList.add(new Property("system", "uri", "An absolute URI which is the code system to be included.", 0, java.lang.Integer.MAX_VALUE, system));
          childrenList.add(new Property("version", "string", "The version of the code system from which codes in the expansion should be included.", 0, java.lang.Integer.MAX_VALUE, version));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("system"))
          this.system = castToUri(value); // UriType
        else if (name.equals("version"))
          this.version = castToString(value); // StringType
        else
          super.setProperty(name, value);
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

      public ExpansionProfileCodeSystemIncludeCodeSystemComponent copy() {
        ExpansionProfileCodeSystemIncludeCodeSystemComponent dst = new ExpansionProfileCodeSystemIncludeCodeSystemComponent();
        copyValues(dst);
        dst.system = system == null ? null : system.copy();
        dst.version = version == null ? null : version.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ExpansionProfileCodeSystemIncludeCodeSystemComponent))
          return false;
        ExpansionProfileCodeSystemIncludeCodeSystemComponent o = (ExpansionProfileCodeSystemIncludeCodeSystemComponent) other;
        return compareDeep(system, o.system, true) && compareDeep(version, o.version, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ExpansionProfileCodeSystemIncludeCodeSystemComponent))
          return false;
        ExpansionProfileCodeSystemIncludeCodeSystemComponent o = (ExpansionProfileCodeSystemIncludeCodeSystemComponent) other;
        return compareValues(system, o.system, true) && compareValues(version, o.version, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (system == null || system.isEmpty()) && (version == null || version.isEmpty())
          ;
      }

  public String fhirType() {
    return "ExpansionProfile.codeSystem.include.codeSystem";

  }

  }

    @Block()
    public static class ExpansionProfileCodeSystemExcludeComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A data group for each code system to be excluded.
         */
        @Child(name = "codeSystem", type = {}, order=1, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="The code systems to be excluded", formalDefinition="A data group for each code system to be excluded." )
        protected List<ExpansionProfileCodeSystemExcludeCodeSystemComponent> codeSystem;

        private static final long serialVersionUID = 207363809L;

    /**
     * Constructor
     */
      public ExpansionProfileCodeSystemExcludeComponent() {
        super();
      }

        /**
         * @return {@link #codeSystem} (A data group for each code system to be excluded.)
         */
        public List<ExpansionProfileCodeSystemExcludeCodeSystemComponent> getCodeSystem() { 
          if (this.codeSystem == null)
            this.codeSystem = new ArrayList<ExpansionProfileCodeSystemExcludeCodeSystemComponent>();
          return this.codeSystem;
        }

        public boolean hasCodeSystem() { 
          if (this.codeSystem == null)
            return false;
          for (ExpansionProfileCodeSystemExcludeCodeSystemComponent item : this.codeSystem)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #codeSystem} (A data group for each code system to be excluded.)
         */
    // syntactic sugar
        public ExpansionProfileCodeSystemExcludeCodeSystemComponent addCodeSystem() { //3
          ExpansionProfileCodeSystemExcludeCodeSystemComponent t = new ExpansionProfileCodeSystemExcludeCodeSystemComponent();
          if (this.codeSystem == null)
            this.codeSystem = new ArrayList<ExpansionProfileCodeSystemExcludeCodeSystemComponent>();
          this.codeSystem.add(t);
          return t;
        }

    // syntactic sugar
        public ExpansionProfileCodeSystemExcludeComponent addCodeSystem(ExpansionProfileCodeSystemExcludeCodeSystemComponent t) { //3
          if (t == null)
            return this;
          if (this.codeSystem == null)
            this.codeSystem = new ArrayList<ExpansionProfileCodeSystemExcludeCodeSystemComponent>();
          this.codeSystem.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("codeSystem", "", "A data group for each code system to be excluded.", 0, java.lang.Integer.MAX_VALUE, codeSystem));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("codeSystem"))
          this.getCodeSystem().add((ExpansionProfileCodeSystemExcludeCodeSystemComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("codeSystem")) {
          return addCodeSystem();
        }
        else
          return super.addChild(name);
      }

      public ExpansionProfileCodeSystemExcludeComponent copy() {
        ExpansionProfileCodeSystemExcludeComponent dst = new ExpansionProfileCodeSystemExcludeComponent();
        copyValues(dst);
        if (codeSystem != null) {
          dst.codeSystem = new ArrayList<ExpansionProfileCodeSystemExcludeCodeSystemComponent>();
          for (ExpansionProfileCodeSystemExcludeCodeSystemComponent i : codeSystem)
            dst.codeSystem.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ExpansionProfileCodeSystemExcludeComponent))
          return false;
        ExpansionProfileCodeSystemExcludeComponent o = (ExpansionProfileCodeSystemExcludeComponent) other;
        return compareDeep(codeSystem, o.codeSystem, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ExpansionProfileCodeSystemExcludeComponent))
          return false;
        ExpansionProfileCodeSystemExcludeComponent o = (ExpansionProfileCodeSystemExcludeComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (codeSystem == null || codeSystem.isEmpty());
      }

  public String fhirType() {
    return "ExpansionProfile.codeSystem.exclude";

  }

  }

    @Block()
    public static class ExpansionProfileCodeSystemExcludeCodeSystemComponent extends BackboneElement implements IBaseBackboneElement {
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
      public ExpansionProfileCodeSystemExcludeCodeSystemComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ExpansionProfileCodeSystemExcludeCodeSystemComponent(UriType system) {
        super();
        this.system = system;
      }

        /**
         * @return {@link #system} (An absolute URI which is the code system to be excluded.). This is the underlying object with id, value and extensions. The accessor "getSystem" gives direct access to the value
         */
        public UriType getSystemElement() { 
          if (this.system == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExpansionProfileCodeSystemExcludeCodeSystemComponent.system");
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
        public ExpansionProfileCodeSystemExcludeCodeSystemComponent setSystemElement(UriType value) { 
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
        public ExpansionProfileCodeSystemExcludeCodeSystemComponent setSystem(String value) { 
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
              throw new Error("Attempt to auto-create ExpansionProfileCodeSystemExcludeCodeSystemComponent.version");
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
        public ExpansionProfileCodeSystemExcludeCodeSystemComponent setVersionElement(StringType value) { 
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
        public ExpansionProfileCodeSystemExcludeCodeSystemComponent setVersion(String value) { 
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
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("system"))
          this.system = castToUri(value); // UriType
        else if (name.equals("version"))
          this.version = castToString(value); // StringType
        else
          super.setProperty(name, value);
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

      public ExpansionProfileCodeSystemExcludeCodeSystemComponent copy() {
        ExpansionProfileCodeSystemExcludeCodeSystemComponent dst = new ExpansionProfileCodeSystemExcludeCodeSystemComponent();
        copyValues(dst);
        dst.system = system == null ? null : system.copy();
        dst.version = version == null ? null : version.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ExpansionProfileCodeSystemExcludeCodeSystemComponent))
          return false;
        ExpansionProfileCodeSystemExcludeCodeSystemComponent o = (ExpansionProfileCodeSystemExcludeCodeSystemComponent) other;
        return compareDeep(system, o.system, true) && compareDeep(version, o.version, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ExpansionProfileCodeSystemExcludeCodeSystemComponent))
          return false;
        ExpansionProfileCodeSystemExcludeCodeSystemComponent o = (ExpansionProfileCodeSystemExcludeCodeSystemComponent) other;
        return compareValues(system, o.system, true) && compareValues(version, o.version, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (system == null || system.isEmpty()) && (version == null || version.isEmpty())
          ;
      }

  public String fhirType() {
    return "ExpansionProfile.codeSystem.exclude.codeSystem";

  }

  }

    @Block()
    public static class ExpansionProfileDesignationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Designations to be included.
         */
        @Child(name = "include", type = {}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Designations to be included", formalDefinition="Designations to be included." )
        protected ExpansionProfileDesignationIncludeComponent include;

        /**
         * Designations to be excluded.
         */
        @Child(name = "exclude", type = {}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Designations to be excluded", formalDefinition="Designations to be excluded." )
        protected ExpansionProfileDesignationExcludeComponent exclude;

        private static final long serialVersionUID = -1075298816L;

    /**
     * Constructor
     */
      public ExpansionProfileDesignationComponent() {
        super();
      }

        /**
         * @return {@link #include} (Designations to be included.)
         */
        public ExpansionProfileDesignationIncludeComponent getInclude() { 
          if (this.include == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExpansionProfileDesignationComponent.include");
            else if (Configuration.doAutoCreate())
              this.include = new ExpansionProfileDesignationIncludeComponent(); // cc
          return this.include;
        }

        public boolean hasInclude() { 
          return this.include != null && !this.include.isEmpty();
        }

        /**
         * @param value {@link #include} (Designations to be included.)
         */
        public ExpansionProfileDesignationComponent setInclude(ExpansionProfileDesignationIncludeComponent value) { 
          this.include = value;
          return this;
        }

        /**
         * @return {@link #exclude} (Designations to be excluded.)
         */
        public ExpansionProfileDesignationExcludeComponent getExclude() { 
          if (this.exclude == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExpansionProfileDesignationComponent.exclude");
            else if (Configuration.doAutoCreate())
              this.exclude = new ExpansionProfileDesignationExcludeComponent(); // cc
          return this.exclude;
        }

        public boolean hasExclude() { 
          return this.exclude != null && !this.exclude.isEmpty();
        }

        /**
         * @param value {@link #exclude} (Designations to be excluded.)
         */
        public ExpansionProfileDesignationComponent setExclude(ExpansionProfileDesignationExcludeComponent value) { 
          this.exclude = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("include", "", "Designations to be included.", 0, java.lang.Integer.MAX_VALUE, include));
          childrenList.add(new Property("exclude", "", "Designations to be excluded.", 0, java.lang.Integer.MAX_VALUE, exclude));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("include"))
          this.include = (ExpansionProfileDesignationIncludeComponent) value; // ExpansionProfileDesignationIncludeComponent
        else if (name.equals("exclude"))
          this.exclude = (ExpansionProfileDesignationExcludeComponent) value; // ExpansionProfileDesignationExcludeComponent
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("include")) {
          this.include = new ExpansionProfileDesignationIncludeComponent();
          return this.include;
        }
        else if (name.equals("exclude")) {
          this.exclude = new ExpansionProfileDesignationExcludeComponent();
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
        return super.isEmpty() && (include == null || include.isEmpty()) && (exclude == null || exclude.isEmpty())
          ;
      }

  public String fhirType() {
    return "ExpansionProfile.designation";

  }

  }

    @Block()
    public static class ExpansionProfileDesignationIncludeComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A data group for each designation to be included.
         */
        @Child(name = "designation", type = {}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="The designation to be included", formalDefinition="A data group for each designation to be included." )
        protected List<ExpansionProfileDesignationIncludeDesignationComponent> designation;

        private static final long serialVersionUID = 999939824L;

    /**
     * Constructor
     */
      public ExpansionProfileDesignationIncludeComponent() {
        super();
      }

        /**
         * @return {@link #designation} (A data group for each designation to be included.)
         */
        public List<ExpansionProfileDesignationIncludeDesignationComponent> getDesignation() { 
          if (this.designation == null)
            this.designation = new ArrayList<ExpansionProfileDesignationIncludeDesignationComponent>();
          return this.designation;
        }

        public boolean hasDesignation() { 
          if (this.designation == null)
            return false;
          for (ExpansionProfileDesignationIncludeDesignationComponent item : this.designation)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #designation} (A data group for each designation to be included.)
         */
    // syntactic sugar
        public ExpansionProfileDesignationIncludeDesignationComponent addDesignation() { //3
          ExpansionProfileDesignationIncludeDesignationComponent t = new ExpansionProfileDesignationIncludeDesignationComponent();
          if (this.designation == null)
            this.designation = new ArrayList<ExpansionProfileDesignationIncludeDesignationComponent>();
          this.designation.add(t);
          return t;
        }

    // syntactic sugar
        public ExpansionProfileDesignationIncludeComponent addDesignation(ExpansionProfileDesignationIncludeDesignationComponent t) { //3
          if (t == null)
            return this;
          if (this.designation == null)
            this.designation = new ArrayList<ExpansionProfileDesignationIncludeDesignationComponent>();
          this.designation.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("designation", "", "A data group for each designation to be included.", 0, java.lang.Integer.MAX_VALUE, designation));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("designation"))
          this.getDesignation().add((ExpansionProfileDesignationIncludeDesignationComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("designation")) {
          return addDesignation();
        }
        else
          return super.addChild(name);
      }

      public ExpansionProfileDesignationIncludeComponent copy() {
        ExpansionProfileDesignationIncludeComponent dst = new ExpansionProfileDesignationIncludeComponent();
        copyValues(dst);
        if (designation != null) {
          dst.designation = new ArrayList<ExpansionProfileDesignationIncludeDesignationComponent>();
          for (ExpansionProfileDesignationIncludeDesignationComponent i : designation)
            dst.designation.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ExpansionProfileDesignationIncludeComponent))
          return false;
        ExpansionProfileDesignationIncludeComponent o = (ExpansionProfileDesignationIncludeComponent) other;
        return compareDeep(designation, o.designation, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ExpansionProfileDesignationIncludeComponent))
          return false;
        ExpansionProfileDesignationIncludeComponent o = (ExpansionProfileDesignationIncludeComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (designation == null || designation.isEmpty());
      }

  public String fhirType() {
    return "ExpansionProfile.designation.include";

  }

  }

    @Block()
    public static class ExpansionProfileDesignationIncludeDesignationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The language this designation is defined for.
         */
        @Child(name = "language", type = {CodeType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Human language of the designation to be included", formalDefinition="The language this designation is defined for." )
        protected CodeType language;

        /**
         * Designation uses for inclusion in the expansion.
         */
        @Child(name = "use", type = {Coding.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Designation use", formalDefinition="Designation uses for inclusion in the expansion." )
        protected Coding use;

        private static final long serialVersionUID = 242239292L;

    /**
     * Constructor
     */
      public ExpansionProfileDesignationIncludeDesignationComponent() {
        super();
      }

        /**
         * @return {@link #language} (The language this designation is defined for.). This is the underlying object with id, value and extensions. The accessor "getLanguage" gives direct access to the value
         */
        public CodeType getLanguageElement() { 
          if (this.language == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExpansionProfileDesignationIncludeDesignationComponent.language");
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
        public ExpansionProfileDesignationIncludeDesignationComponent setLanguageElement(CodeType value) { 
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
        public ExpansionProfileDesignationIncludeDesignationComponent setLanguage(String value) { 
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
         * @return {@link #use} (Designation uses for inclusion in the expansion.)
         */
        public Coding getUse() { 
          if (this.use == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExpansionProfileDesignationIncludeDesignationComponent.use");
            else if (Configuration.doAutoCreate())
              this.use = new Coding(); // cc
          return this.use;
        }

        public boolean hasUse() { 
          return this.use != null && !this.use.isEmpty();
        }

        /**
         * @param value {@link #use} (Designation uses for inclusion in the expansion.)
         */
        public ExpansionProfileDesignationIncludeDesignationComponent setUse(Coding value) { 
          this.use = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("language", "code", "The language this designation is defined for.", 0, java.lang.Integer.MAX_VALUE, language));
          childrenList.add(new Property("use", "Coding", "Designation uses for inclusion in the expansion.", 0, java.lang.Integer.MAX_VALUE, use));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("language"))
          this.language = castToCode(value); // CodeType
        else if (name.equals("use"))
          this.use = castToCoding(value); // Coding
        else
          super.setProperty(name, value);
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

      public ExpansionProfileDesignationIncludeDesignationComponent copy() {
        ExpansionProfileDesignationIncludeDesignationComponent dst = new ExpansionProfileDesignationIncludeDesignationComponent();
        copyValues(dst);
        dst.language = language == null ? null : language.copy();
        dst.use = use == null ? null : use.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ExpansionProfileDesignationIncludeDesignationComponent))
          return false;
        ExpansionProfileDesignationIncludeDesignationComponent o = (ExpansionProfileDesignationIncludeDesignationComponent) other;
        return compareDeep(language, o.language, true) && compareDeep(use, o.use, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ExpansionProfileDesignationIncludeDesignationComponent))
          return false;
        ExpansionProfileDesignationIncludeDesignationComponent o = (ExpansionProfileDesignationIncludeDesignationComponent) other;
        return compareValues(language, o.language, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (language == null || language.isEmpty()) && (use == null || use.isEmpty())
          ;
      }

  public String fhirType() {
    return "ExpansionProfile.designation.include.designation";

  }

  }

    @Block()
    public static class ExpansionProfileDesignationExcludeComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A data group for each designation to be excluded.
         */
        @Child(name = "designation", type = {}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="The designation to be excluded", formalDefinition="A data group for each designation to be excluded." )
        protected List<ExpansionProfileDesignationExcludeDesignationComponent> designation;

        private static final long serialVersionUID = -259508446L;

    /**
     * Constructor
     */
      public ExpansionProfileDesignationExcludeComponent() {
        super();
      }

        /**
         * @return {@link #designation} (A data group for each designation to be excluded.)
         */
        public List<ExpansionProfileDesignationExcludeDesignationComponent> getDesignation() { 
          if (this.designation == null)
            this.designation = new ArrayList<ExpansionProfileDesignationExcludeDesignationComponent>();
          return this.designation;
        }

        public boolean hasDesignation() { 
          if (this.designation == null)
            return false;
          for (ExpansionProfileDesignationExcludeDesignationComponent item : this.designation)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #designation} (A data group for each designation to be excluded.)
         */
    // syntactic sugar
        public ExpansionProfileDesignationExcludeDesignationComponent addDesignation() { //3
          ExpansionProfileDesignationExcludeDesignationComponent t = new ExpansionProfileDesignationExcludeDesignationComponent();
          if (this.designation == null)
            this.designation = new ArrayList<ExpansionProfileDesignationExcludeDesignationComponent>();
          this.designation.add(t);
          return t;
        }

    // syntactic sugar
        public ExpansionProfileDesignationExcludeComponent addDesignation(ExpansionProfileDesignationExcludeDesignationComponent t) { //3
          if (t == null)
            return this;
          if (this.designation == null)
            this.designation = new ArrayList<ExpansionProfileDesignationExcludeDesignationComponent>();
          this.designation.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("designation", "", "A data group for each designation to be excluded.", 0, java.lang.Integer.MAX_VALUE, designation));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("designation"))
          this.getDesignation().add((ExpansionProfileDesignationExcludeDesignationComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("designation")) {
          return addDesignation();
        }
        else
          return super.addChild(name);
      }

      public ExpansionProfileDesignationExcludeComponent copy() {
        ExpansionProfileDesignationExcludeComponent dst = new ExpansionProfileDesignationExcludeComponent();
        copyValues(dst);
        if (designation != null) {
          dst.designation = new ArrayList<ExpansionProfileDesignationExcludeDesignationComponent>();
          for (ExpansionProfileDesignationExcludeDesignationComponent i : designation)
            dst.designation.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ExpansionProfileDesignationExcludeComponent))
          return false;
        ExpansionProfileDesignationExcludeComponent o = (ExpansionProfileDesignationExcludeComponent) other;
        return compareDeep(designation, o.designation, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ExpansionProfileDesignationExcludeComponent))
          return false;
        ExpansionProfileDesignationExcludeComponent o = (ExpansionProfileDesignationExcludeComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (designation == null || designation.isEmpty());
      }

  public String fhirType() {
    return "ExpansionProfile.designation.exclude";

  }

  }

    @Block()
    public static class ExpansionProfileDesignationExcludeDesignationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The language this designation is defined for.
         */
        @Child(name = "language", type = {CodeType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Human language of the designation to be excluded", formalDefinition="The language this designation is defined for." )
        protected CodeType language;

        /**
         * Designation uses for exclusion in the expansion.
         */
        @Child(name = "use", type = {Coding.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Designation use", formalDefinition="Designation uses for exclusion in the expansion." )
        protected Coding use;

        private static final long serialVersionUID = 242239292L;

    /**
     * Constructor
     */
      public ExpansionProfileDesignationExcludeDesignationComponent() {
        super();
      }

        /**
         * @return {@link #language} (The language this designation is defined for.). This is the underlying object with id, value and extensions. The accessor "getLanguage" gives direct access to the value
         */
        public CodeType getLanguageElement() { 
          if (this.language == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExpansionProfileDesignationExcludeDesignationComponent.language");
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
        public ExpansionProfileDesignationExcludeDesignationComponent setLanguageElement(CodeType value) { 
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
        public ExpansionProfileDesignationExcludeDesignationComponent setLanguage(String value) { 
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
         * @return {@link #use} (Designation uses for exclusion in the expansion.)
         */
        public Coding getUse() { 
          if (this.use == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExpansionProfileDesignationExcludeDesignationComponent.use");
            else if (Configuration.doAutoCreate())
              this.use = new Coding(); // cc
          return this.use;
        }

        public boolean hasUse() { 
          return this.use != null && !this.use.isEmpty();
        }

        /**
         * @param value {@link #use} (Designation uses for exclusion in the expansion.)
         */
        public ExpansionProfileDesignationExcludeDesignationComponent setUse(Coding value) { 
          this.use = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("language", "code", "The language this designation is defined for.", 0, java.lang.Integer.MAX_VALUE, language));
          childrenList.add(new Property("use", "Coding", "Designation uses for exclusion in the expansion.", 0, java.lang.Integer.MAX_VALUE, use));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("language"))
          this.language = castToCode(value); // CodeType
        else if (name.equals("use"))
          this.use = castToCoding(value); // Coding
        else
          super.setProperty(name, value);
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

      public ExpansionProfileDesignationExcludeDesignationComponent copy() {
        ExpansionProfileDesignationExcludeDesignationComponent dst = new ExpansionProfileDesignationExcludeDesignationComponent();
        copyValues(dst);
        dst.language = language == null ? null : language.copy();
        dst.use = use == null ? null : use.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ExpansionProfileDesignationExcludeDesignationComponent))
          return false;
        ExpansionProfileDesignationExcludeDesignationComponent o = (ExpansionProfileDesignationExcludeDesignationComponent) other;
        return compareDeep(language, o.language, true) && compareDeep(use, o.use, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ExpansionProfileDesignationExcludeDesignationComponent))
          return false;
        ExpansionProfileDesignationExcludeDesignationComponent o = (ExpansionProfileDesignationExcludeDesignationComponent) other;
        return compareValues(language, o.language, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (language == null || language.isEmpty()) && (use == null || use.isEmpty())
          ;
      }

  public String fhirType() {
    return "ExpansionProfile.designation.exclude.designation";

  }

  }

    /**
     * An absolute URL that is used to identify this expansion profile when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this expansion profile is (or will be) published.
     */
    @Child(name = "url", type = {UriType.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Globally unique logical identifier for  expansion profile", formalDefinition="An absolute URL that is used to identify this expansion profile when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this expansion profile is (or will be) published." )
    protected UriType url;

    /**
     * Formal identifier that is used to identify this expansion profile when it is represented in other formats, or referenced in a specification, model, design or an instance.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Additional identifier for the expansion profile (e.g. an Object Identifier)", formalDefinition="Formal identifier that is used to identify this expansion profile when it is represented in other formats, or referenced in a specification, model, design or an instance." )
    protected Identifier identifier;

    /**
     * Used to identify this version of the expansion profile when it is referenced in a specification, model, design or instance.
     */
    @Child(name = "version", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Logical identifier for this version of the expansion profile", formalDefinition="Used to identify this version of the expansion profile when it is referenced in a specification, model, design or instance." )
    protected StringType version;

    /**
     * A free text natural language name for the expansion profile.
     */
    @Child(name = "name", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Informal name for this expansion profile", formalDefinition="A free text natural language name for the expansion profile." )
    protected StringType name;

    /**
     * The status of the expansion profile.
     */
    @Child(name = "status", type = {CodeType.class}, order=4, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="draft | active | retired", formalDefinition="The status of the expansion profile." )
    protected Enumeration<ConformanceResourceStatus> status;

    /**
     * This expansion profile was authored for testing purposes (or education/evaluation/marketing), and is not intended for genuine production usage.
     */
    @Child(name = "experimental", type = {BooleanType.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="If for testing purposes, not real usage", formalDefinition="This expansion profile was authored for testing purposes (or education/evaluation/marketing), and is not intended for genuine production usage." )
    protected BooleanType experimental;

    /**
     * The name of the individual or organization that published the expansion profile.
     */
    @Child(name = "publisher", type = {StringType.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Name of the publisher (organization or individual)", formalDefinition="The name of the individual or organization that published the expansion profile." )
    protected StringType publisher;

    /**
     * Contacts to assist a user in finding and communicating with the publisher.
     */
    @Child(name = "contact", type = {}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Contact details of the publisher", formalDefinition="Contacts to assist a user in finding and communicating with the publisher." )
    protected List<ExpansionProfileContactComponent> contact;

    /**
     * The date that the expansion profile status was last changed. The date must change when the business version changes, if it does, and it must change if the status code changes.
     */
    @Child(name = "date", type = {DateTimeType.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Date for given status", formalDefinition="The date that the expansion profile status was last changed. The date must change when the business version changes, if it does, and it must change if the status code changes." )
    protected DateTimeType date;

    /**
     * A free text natural language description of the use of the expansion profile - reason for definition,  conditions of use, etc. The description may include a list of expected usages for the expansion profile and can also describe the approach taken to build the expansion profile.
     */
    @Child(name = "description", type = {StringType.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Human language description of the expansion profile", formalDefinition="A free text natural language description of the use of the expansion profile - reason for definition,  conditions of use, etc. The description may include a list of expected usages for the expansion profile and can also describe the approach taken to build the expansion profile." )
    protected StringType description;

    /**
     * A set of criteria that provide the constraints imposed on the value set expansion by including or excluding codes from specific code systems (or versions).
     */
    @Child(name = "codeSystem", type = {}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When the expansion profile imposes code system contraints", formalDefinition="A set of criteria that provide the constraints imposed on the value set expansion by including or excluding codes from specific code systems (or versions)." )
    protected ExpansionProfileCodeSystemComponent codeSystem;

    /**
     * Controls whether concept designations are to be included or excluded in value set expansions.
     */
    @Child(name = "includeDesignations", type = {BooleanType.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Whether the expansion should include concept designations", formalDefinition="Controls whether concept designations are to be included or excluded in value set expansions." )
    protected BooleanType includeDesignations;

    /**
     * A set of criteria that provide the constraints imposed on the value set expansion by including or excluding designations.
     */
    @Child(name = "designation", type = {}, order=12, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When the expansion profile imposes designation contraints", formalDefinition="A set of criteria that provide the constraints imposed on the value set expansion by including or excluding designations." )
    protected ExpansionProfileDesignationComponent designation;

    /**
     * Controls whether the value set definition is included or excluded in value set expansions.
     */
    @Child(name = "includeDefinition", type = {BooleanType.class}, order=13, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Include or exclude the value set definition in the expansion", formalDefinition="Controls whether the value set definition is included or excluded in value set expansions." )
    protected BooleanType includeDefinition;

    /**
     * Controls whether inactive concepts are included or excluded in value set expansions.
     */
    @Child(name = "includeInactive", type = {BooleanType.class}, order=14, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Include or exclude inactive concepts in the expansion", formalDefinition="Controls whether inactive concepts are included or excluded in value set expansions." )
    protected BooleanType includeInactive;

    /**
     * Controls whether or not the value set expansion includes nested codes (i.e. ValueSet.expansion.contains.contains).
     */
    @Child(name = "excludeNested", type = {BooleanType.class}, order=15, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Include or exclude nested codes in the value set expansion", formalDefinition="Controls whether or not the value set expansion includes nested codes (i.e. ValueSet.expansion.contains.contains)." )
    protected BooleanType excludeNested;

    /**
     * Controls whether or not the value set expansion includes codes which cannot be displayed in user interfaces.
     */
    @Child(name = "excludeNotForUI", type = {BooleanType.class}, order=16, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Include or exclude codes which cannot be rendered in user interfaces in the value set expansion", formalDefinition="Controls whether or not the value set expansion includes codes which cannot be displayed in user interfaces." )
    protected BooleanType excludeNotForUI;

    /**
     * Controls whether or not the value set expansion includes post coordinated codes.
     */
    @Child(name = "excludePostCoordinated", type = {BooleanType.class}, order=17, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Include or exclude codes which are post coordinated expressions in the value set expansion", formalDefinition="Controls whether or not the value set expansion includes post coordinated codes." )
    protected BooleanType excludePostCoordinated;

    /**
     * Specifies the language to be used for description in the expansions i.e. the language to be used for ValueSet.expansion.contains.display.
     */
    @Child(name = "displayLanguage", type = {CodeType.class}, order=18, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Specify the language for the display element of codes in the value set expansion", formalDefinition="Specifies the language to be used for description in the expansions i.e. the language to be used for ValueSet.expansion.contains.display." )
    protected CodeType displayLanguage;

    /**
     * If the value set being expanded is incomplete (because it is too big to expand), return a limited expansion (a subset) with an indicator that expansion is incomplete.
     */
    @Child(name = "limitedExpansion", type = {BooleanType.class}, order=19, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Controls behaviour of the value set expand operation when value sets are too large to be completely expanded", formalDefinition="If the value set being expanded is incomplete (because it is too big to expand), return a limited expansion (a subset) with an indicator that expansion is incomplete." )
    protected BooleanType limitedExpansion;

    private static final long serialVersionUID = -651123079L;

  /**
   * Constructor
   */
    public ExpansionProfile() {
      super();
    }

  /**
   * Constructor
   */
    public ExpansionProfile(Enumeration<ConformanceResourceStatus> status) {
      super();
      this.status = status;
    }

    /**
     * @return {@link #url} (An absolute URL that is used to identify this expansion profile when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this expansion profile is (or will be) published.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
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
     * @param value {@link #url} (An absolute URL that is used to identify this expansion profile when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this expansion profile is (or will be) published.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public ExpansionProfile setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return An absolute URL that is used to identify this expansion profile when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this expansion profile is (or will be) published.
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value An absolute URL that is used to identify this expansion profile when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this expansion profile is (or will be) published.
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
     * @return {@link #identifier} (Formal identifier that is used to identify this expansion profile when it is represented in other formats, or referenced in a specification, model, design or an instance.)
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
     * @param value {@link #identifier} (Formal identifier that is used to identify this expansion profile when it is represented in other formats, or referenced in a specification, model, design or an instance.)
     */
    public ExpansionProfile setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #version} (Used to identify this version of the expansion profile when it is referenced in a specification, model, design or instance.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
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
     * @param value {@link #version} (Used to identify this version of the expansion profile when it is referenced in a specification, model, design or instance.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public ExpansionProfile setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return Used to identify this version of the expansion profile when it is referenced in a specification, model, design or instance.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value Used to identify this version of the expansion profile when it is referenced in a specification, model, design or instance.
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
     * @return {@link #name} (A free text natural language name for the expansion profile.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
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
     * @param value {@link #name} (A free text natural language name for the expansion profile.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public ExpansionProfile setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return A free text natural language name for the expansion profile.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value A free text natural language name for the expansion profile.
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
     * @return {@link #status} (The status of the expansion profile.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<ConformanceResourceStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExpansionProfile.status");
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
     * @param value {@link #status} (The status of the expansion profile.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public ExpansionProfile setStatusElement(Enumeration<ConformanceResourceStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the expansion profile.
     */
    public ConformanceResourceStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the expansion profile.
     */
    public ExpansionProfile setStatus(ConformanceResourceStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<ConformanceResourceStatus>(new ConformanceResourceStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #experimental} (This expansion profile was authored for testing purposes (or education/evaluation/marketing), and is not intended for genuine production usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
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
     * @param value {@link #experimental} (This expansion profile was authored for testing purposes (or education/evaluation/marketing), and is not intended for genuine production usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public ExpansionProfile setExperimentalElement(BooleanType value) { 
      this.experimental = value;
      return this;
    }

    /**
     * @return This expansion profile was authored for testing purposes (or education/evaluation/marketing), and is not intended for genuine production usage.
     */
    public boolean getExperimental() { 
      return this.experimental == null || this.experimental.isEmpty() ? false : this.experimental.getValue();
    }

    /**
     * @param value This expansion profile was authored for testing purposes (or education/evaluation/marketing), and is not intended for genuine production usage.
     */
    public ExpansionProfile setExperimental(boolean value) { 
        if (this.experimental == null)
          this.experimental = new BooleanType();
        this.experimental.setValue(value);
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
     * @return {@link #contact} (Contacts to assist a user in finding and communicating with the publisher.)
     */
    public List<ExpansionProfileContactComponent> getContact() { 
      if (this.contact == null)
        this.contact = new ArrayList<ExpansionProfileContactComponent>();
      return this.contact;
    }

    public boolean hasContact() { 
      if (this.contact == null)
        return false;
      for (ExpansionProfileContactComponent item : this.contact)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #contact} (Contacts to assist a user in finding and communicating with the publisher.)
     */
    // syntactic sugar
    public ExpansionProfileContactComponent addContact() { //3
      ExpansionProfileContactComponent t = new ExpansionProfileContactComponent();
      if (this.contact == null)
        this.contact = new ArrayList<ExpansionProfileContactComponent>();
      this.contact.add(t);
      return t;
    }

    // syntactic sugar
    public ExpansionProfile addContact(ExpansionProfileContactComponent t) { //3
      if (t == null)
        return this;
      if (this.contact == null)
        this.contact = new ArrayList<ExpansionProfileContactComponent>();
      this.contact.add(t);
      return this;
    }

    /**
     * @return {@link #date} (The date that the expansion profile status was last changed. The date must change when the business version changes, if it does, and it must change if the status code changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
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
     * @param value {@link #date} (The date that the expansion profile status was last changed. The date must change when the business version changes, if it does, and it must change if the status code changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public ExpansionProfile setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date that the expansion profile status was last changed. The date must change when the business version changes, if it does, and it must change if the status code changes.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date that the expansion profile status was last changed. The date must change when the business version changes, if it does, and it must change if the status code changes.
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
     * @return {@link #description} (A free text natural language description of the use of the expansion profile - reason for definition,  conditions of use, etc. The description may include a list of expected usages for the expansion profile and can also describe the approach taken to build the expansion profile.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExpansionProfile.description");
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
     * @param value {@link #description} (A free text natural language description of the use of the expansion profile - reason for definition,  conditions of use, etc. The description may include a list of expected usages for the expansion profile and can also describe the approach taken to build the expansion profile.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public ExpansionProfile setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A free text natural language description of the use of the expansion profile - reason for definition,  conditions of use, etc. The description may include a list of expected usages for the expansion profile and can also describe the approach taken to build the expansion profile.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A free text natural language description of the use of the expansion profile - reason for definition,  conditions of use, etc. The description may include a list of expected usages for the expansion profile and can also describe the approach taken to build the expansion profile.
     */
    public ExpansionProfile setDescription(String value) { 
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
     * @return {@link #codeSystem} (A set of criteria that provide the constraints imposed on the value set expansion by including or excluding codes from specific code systems (or versions).)
     */
    public ExpansionProfileCodeSystemComponent getCodeSystem() { 
      if (this.codeSystem == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExpansionProfile.codeSystem");
        else if (Configuration.doAutoCreate())
          this.codeSystem = new ExpansionProfileCodeSystemComponent(); // cc
      return this.codeSystem;
    }

    public boolean hasCodeSystem() { 
      return this.codeSystem != null && !this.codeSystem.isEmpty();
    }

    /**
     * @param value {@link #codeSystem} (A set of criteria that provide the constraints imposed on the value set expansion by including or excluding codes from specific code systems (or versions).)
     */
    public ExpansionProfile setCodeSystem(ExpansionProfileCodeSystemComponent value) { 
      this.codeSystem = value;
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
     * @return {@link #includeInactive} (Controls whether inactive concepts are included or excluded in value set expansions.). This is the underlying object with id, value and extensions. The accessor "getIncludeInactive" gives direct access to the value
     */
    public BooleanType getIncludeInactiveElement() { 
      if (this.includeInactive == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExpansionProfile.includeInactive");
        else if (Configuration.doAutoCreate())
          this.includeInactive = new BooleanType(); // bb
      return this.includeInactive;
    }

    public boolean hasIncludeInactiveElement() { 
      return this.includeInactive != null && !this.includeInactive.isEmpty();
    }

    public boolean hasIncludeInactive() { 
      return this.includeInactive != null && !this.includeInactive.isEmpty();
    }

    /**
     * @param value {@link #includeInactive} (Controls whether inactive concepts are included or excluded in value set expansions.). This is the underlying object with id, value and extensions. The accessor "getIncludeInactive" gives direct access to the value
     */
    public ExpansionProfile setIncludeInactiveElement(BooleanType value) { 
      this.includeInactive = value;
      return this;
    }

    /**
     * @return Controls whether inactive concepts are included or excluded in value set expansions.
     */
    public boolean getIncludeInactive() { 
      return this.includeInactive == null || this.includeInactive.isEmpty() ? false : this.includeInactive.getValue();
    }

    /**
     * @param value Controls whether inactive concepts are included or excluded in value set expansions.
     */
    public ExpansionProfile setIncludeInactive(boolean value) { 
        if (this.includeInactive == null)
          this.includeInactive = new BooleanType();
        this.includeInactive.setValue(value);
      return this;
    }

    /**
     * @return {@link #excludeNested} (Controls whether or not the value set expansion includes nested codes (i.e. ValueSet.expansion.contains.contains).). This is the underlying object with id, value and extensions. The accessor "getExcludeNested" gives direct access to the value
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
     * @param value {@link #excludeNested} (Controls whether or not the value set expansion includes nested codes (i.e. ValueSet.expansion.contains.contains).). This is the underlying object with id, value and extensions. The accessor "getExcludeNested" gives direct access to the value
     */
    public ExpansionProfile setExcludeNestedElement(BooleanType value) { 
      this.excludeNested = value;
      return this;
    }

    /**
     * @return Controls whether or not the value set expansion includes nested codes (i.e. ValueSet.expansion.contains.contains).
     */
    public boolean getExcludeNested() { 
      return this.excludeNested == null || this.excludeNested.isEmpty() ? false : this.excludeNested.getValue();
    }

    /**
     * @param value Controls whether or not the value set expansion includes nested codes (i.e. ValueSet.expansion.contains.contains).
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
     * @return {@link #limitedExpansion} (If the value set being expanded is incomplete (because it is too big to expand), return a limited expansion (a subset) with an indicator that expansion is incomplete.). This is the underlying object with id, value and extensions. The accessor "getLimitedExpansion" gives direct access to the value
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
     * @param value {@link #limitedExpansion} (If the value set being expanded is incomplete (because it is too big to expand), return a limited expansion (a subset) with an indicator that expansion is incomplete.). This is the underlying object with id, value and extensions. The accessor "getLimitedExpansion" gives direct access to the value
     */
    public ExpansionProfile setLimitedExpansionElement(BooleanType value) { 
      this.limitedExpansion = value;
      return this;
    }

    /**
     * @return If the value set being expanded is incomplete (because it is too big to expand), return a limited expansion (a subset) with an indicator that expansion is incomplete.
     */
    public boolean getLimitedExpansion() { 
      return this.limitedExpansion == null || this.limitedExpansion.isEmpty() ? false : this.limitedExpansion.getValue();
    }

    /**
     * @param value If the value set being expanded is incomplete (because it is too big to expand), return a limited expansion (a subset) with an indicator that expansion is incomplete.
     */
    public ExpansionProfile setLimitedExpansion(boolean value) { 
        if (this.limitedExpansion == null)
          this.limitedExpansion = new BooleanType();
        this.limitedExpansion.setValue(value);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("url", "uri", "An absolute URL that is used to identify this expansion profile when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this expansion profile is (or will be) published.", 0, java.lang.Integer.MAX_VALUE, url));
        childrenList.add(new Property("identifier", "Identifier", "Formal identifier that is used to identify this expansion profile when it is represented in other formats, or referenced in a specification, model, design or an instance.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("version", "string", "Used to identify this version of the expansion profile when it is referenced in a specification, model, design or instance.", 0, java.lang.Integer.MAX_VALUE, version));
        childrenList.add(new Property("name", "string", "A free text natural language name for the expansion profile.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("status", "code", "The status of the expansion profile.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("experimental", "boolean", "This expansion profile was authored for testing purposes (or education/evaluation/marketing), and is not intended for genuine production usage.", 0, java.lang.Integer.MAX_VALUE, experimental));
        childrenList.add(new Property("publisher", "string", "The name of the individual or organization that published the expansion profile.", 0, java.lang.Integer.MAX_VALUE, publisher));
        childrenList.add(new Property("contact", "", "Contacts to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact));
        childrenList.add(new Property("date", "dateTime", "The date that the expansion profile status was last changed. The date must change when the business version changes, if it does, and it must change if the status code changes.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("description", "string", "A free text natural language description of the use of the expansion profile - reason for definition,  conditions of use, etc. The description may include a list of expected usages for the expansion profile and can also describe the approach taken to build the expansion profile.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("codeSystem", "", "A set of criteria that provide the constraints imposed on the value set expansion by including or excluding codes from specific code systems (or versions).", 0, java.lang.Integer.MAX_VALUE, codeSystem));
        childrenList.add(new Property("includeDesignations", "boolean", "Controls whether concept designations are to be included or excluded in value set expansions.", 0, java.lang.Integer.MAX_VALUE, includeDesignations));
        childrenList.add(new Property("designation", "", "A set of criteria that provide the constraints imposed on the value set expansion by including or excluding designations.", 0, java.lang.Integer.MAX_VALUE, designation));
        childrenList.add(new Property("includeDefinition", "boolean", "Controls whether the value set definition is included or excluded in value set expansions.", 0, java.lang.Integer.MAX_VALUE, includeDefinition));
        childrenList.add(new Property("includeInactive", "boolean", "Controls whether inactive concepts are included or excluded in value set expansions.", 0, java.lang.Integer.MAX_VALUE, includeInactive));
        childrenList.add(new Property("excludeNested", "boolean", "Controls whether or not the value set expansion includes nested codes (i.e. ValueSet.expansion.contains.contains).", 0, java.lang.Integer.MAX_VALUE, excludeNested));
        childrenList.add(new Property("excludeNotForUI", "boolean", "Controls whether or not the value set expansion includes codes which cannot be displayed in user interfaces.", 0, java.lang.Integer.MAX_VALUE, excludeNotForUI));
        childrenList.add(new Property("excludePostCoordinated", "boolean", "Controls whether or not the value set expansion includes post coordinated codes.", 0, java.lang.Integer.MAX_VALUE, excludePostCoordinated));
        childrenList.add(new Property("displayLanguage", "code", "Specifies the language to be used for description in the expansions i.e. the language to be used for ValueSet.expansion.contains.display.", 0, java.lang.Integer.MAX_VALUE, displayLanguage));
        childrenList.add(new Property("limitedExpansion", "boolean", "If the value set being expanded is incomplete (because it is too big to expand), return a limited expansion (a subset) with an indicator that expansion is incomplete.", 0, java.lang.Integer.MAX_VALUE, limitedExpansion));
      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("url"))
          this.url = castToUri(value); // UriType
        else if (name.equals("identifier"))
          this.identifier = castToIdentifier(value); // Identifier
        else if (name.equals("version"))
          this.version = castToString(value); // StringType
        else if (name.equals("name"))
          this.name = castToString(value); // StringType
        else if (name.equals("status"))
          this.status = new ConformanceResourceStatusEnumFactory().fromType(value); // Enumeration<ConformanceResourceStatus>
        else if (name.equals("experimental"))
          this.experimental = castToBoolean(value); // BooleanType
        else if (name.equals("publisher"))
          this.publisher = castToString(value); // StringType
        else if (name.equals("contact"))
          this.getContact().add((ExpansionProfileContactComponent) value);
        else if (name.equals("date"))
          this.date = castToDateTime(value); // DateTimeType
        else if (name.equals("description"))
          this.description = castToString(value); // StringType
        else if (name.equals("codeSystem"))
          this.codeSystem = (ExpansionProfileCodeSystemComponent) value; // ExpansionProfileCodeSystemComponent
        else if (name.equals("includeDesignations"))
          this.includeDesignations = castToBoolean(value); // BooleanType
        else if (name.equals("designation"))
          this.designation = (ExpansionProfileDesignationComponent) value; // ExpansionProfileDesignationComponent
        else if (name.equals("includeDefinition"))
          this.includeDefinition = castToBoolean(value); // BooleanType
        else if (name.equals("includeInactive"))
          this.includeInactive = castToBoolean(value); // BooleanType
        else if (name.equals("excludeNested"))
          this.excludeNested = castToBoolean(value); // BooleanType
        else if (name.equals("excludeNotForUI"))
          this.excludeNotForUI = castToBoolean(value); // BooleanType
        else if (name.equals("excludePostCoordinated"))
          this.excludePostCoordinated = castToBoolean(value); // BooleanType
        else if (name.equals("displayLanguage"))
          this.displayLanguage = castToCode(value); // CodeType
        else if (name.equals("limitedExpansion"))
          this.limitedExpansion = castToBoolean(value); // BooleanType
        else
          super.setProperty(name, value);
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
        else if (name.equals("publisher")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExpansionProfile.publisher");
        }
        else if (name.equals("contact")) {
          return addContact();
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExpansionProfile.date");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExpansionProfile.description");
        }
        else if (name.equals("codeSystem")) {
          this.codeSystem = new ExpansionProfileCodeSystemComponent();
          return this.codeSystem;
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
        else if (name.equals("includeInactive")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExpansionProfile.includeInactive");
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
        dst.publisher = publisher == null ? null : publisher.copy();
        if (contact != null) {
          dst.contact = new ArrayList<ExpansionProfileContactComponent>();
          for (ExpansionProfileContactComponent i : contact)
            dst.contact.add(i.copy());
        };
        dst.date = date == null ? null : date.copy();
        dst.description = description == null ? null : description.copy();
        dst.codeSystem = codeSystem == null ? null : codeSystem.copy();
        dst.includeDesignations = includeDesignations == null ? null : includeDesignations.copy();
        dst.designation = designation == null ? null : designation.copy();
        dst.includeDefinition = includeDefinition == null ? null : includeDefinition.copy();
        dst.includeInactive = includeInactive == null ? null : includeInactive.copy();
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
        return compareDeep(url, o.url, true) && compareDeep(identifier, o.identifier, true) && compareDeep(version, o.version, true)
           && compareDeep(name, o.name, true) && compareDeep(status, o.status, true) && compareDeep(experimental, o.experimental, true)
           && compareDeep(publisher, o.publisher, true) && compareDeep(contact, o.contact, true) && compareDeep(date, o.date, true)
           && compareDeep(description, o.description, true) && compareDeep(codeSystem, o.codeSystem, true)
           && compareDeep(includeDesignations, o.includeDesignations, true) && compareDeep(designation, o.designation, true)
           && compareDeep(includeDefinition, o.includeDefinition, true) && compareDeep(includeInactive, o.includeInactive, true)
           && compareDeep(excludeNested, o.excludeNested, true) && compareDeep(excludeNotForUI, o.excludeNotForUI, true)
           && compareDeep(excludePostCoordinated, o.excludePostCoordinated, true) && compareDeep(displayLanguage, o.displayLanguage, true)
           && compareDeep(limitedExpansion, o.limitedExpansion, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ExpansionProfile))
          return false;
        ExpansionProfile o = (ExpansionProfile) other;
        return compareValues(url, o.url, true) && compareValues(version, o.version, true) && compareValues(name, o.name, true)
           && compareValues(status, o.status, true) && compareValues(experimental, o.experimental, true) && compareValues(publisher, o.publisher, true)
           && compareValues(date, o.date, true) && compareValues(description, o.description, true) && compareValues(includeDesignations, o.includeDesignations, true)
           && compareValues(includeDefinition, o.includeDefinition, true) && compareValues(includeInactive, o.includeInactive, true)
           && compareValues(excludeNested, o.excludeNested, true) && compareValues(excludeNotForUI, o.excludeNotForUI, true)
           && compareValues(excludePostCoordinated, o.excludePostCoordinated, true) && compareValues(displayLanguage, o.displayLanguage, true)
           && compareValues(limitedExpansion, o.limitedExpansion, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (url == null || url.isEmpty()) && (identifier == null || identifier.isEmpty())
           && (version == null || version.isEmpty()) && (name == null || name.isEmpty()) && (status == null || status.isEmpty())
           && (experimental == null || experimental.isEmpty()) && (publisher == null || publisher.isEmpty())
           && (contact == null || contact.isEmpty()) && (date == null || date.isEmpty()) && (description == null || description.isEmpty())
           && (codeSystem == null || codeSystem.isEmpty()) && (includeDesignations == null || includeDesignations.isEmpty())
           && (designation == null || designation.isEmpty()) && (includeDefinition == null || includeDefinition.isEmpty())
           && (includeInactive == null || includeInactive.isEmpty()) && (excludeNested == null || excludeNested.isEmpty())
           && (excludeNotForUI == null || excludeNotForUI.isEmpty()) && (excludePostCoordinated == null || excludePostCoordinated.isEmpty())
           && (displayLanguage == null || displayLanguage.isEmpty()) && (limitedExpansion == null || limitedExpansion.isEmpty())
          ;
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
   * Description: <b>The identifier for the expansion profile</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ExpansionProfile.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="ExpansionProfile.identifier", description="The identifier for the expansion profile", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>The identifier for the expansion profile</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ExpansionProfile.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>name</b>
   * <p>
   * Description: <b>The name of the expansion profile</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ExpansionProfile.name</b><br>
   * </p>
   */
  @SearchParamDefinition(name="name", path="ExpansionProfile.name", description="The name of the expansion profile", type="string" )
  public static final String SP_NAME = "name";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>name</b>
   * <p>
   * Description: <b>The name of the expansion profile</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ExpansionProfile.name</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam NAME = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_NAME);

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
   * Search parameter: <b>description</b>
   * <p>
   * Description: <b>Text search in the description of the expansion profile</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ExpansionProfile.description</b><br>
   * </p>
   */
  @SearchParamDefinition(name="description", path="ExpansionProfile.description", description="Text search in the description of the expansion profile", type="string" )
  public static final String SP_DESCRIPTION = "description";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>description</b>
   * <p>
   * Description: <b>Text search in the description of the expansion profile</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ExpansionProfile.description</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam DESCRIPTION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_DESCRIPTION);

 /**
   * Search parameter: <b>version</b>
   * <p>
   * Description: <b>The version identifier of the expansion profile</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ExpansionProfile.version</b><br>
   * </p>
   */
  @SearchParamDefinition(name="version", path="ExpansionProfile.version", description="The version identifier of the expansion profile", type="token" )
  public static final String SP_VERSION = "version";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>version</b>
   * <p>
   * Description: <b>The version identifier of the expansion profile</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ExpansionProfile.version</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam VERSION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_VERSION);

 /**
   * Search parameter: <b>url</b>
   * <p>
   * Description: <b>The logical URL for the expansion profile</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ExpansionProfile.url</b><br>
   * </p>
   */
  @SearchParamDefinition(name="url", path="ExpansionProfile.url", description="The logical URL for the expansion profile", type="uri" )
  public static final String SP_URL = "url";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>url</b>
   * <p>
   * Description: <b>The logical URL for the expansion profile</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ExpansionProfile.url</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam URL = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_URL);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The status of the expansion profile</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ExpansionProfile.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="ExpansionProfile.status", description="The status of the expansion profile", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The status of the expansion profile</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ExpansionProfile.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

