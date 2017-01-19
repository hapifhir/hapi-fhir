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
import java.util.Date;
import java.util.List;

import org.hl7.fhir.dstu2016may.model.Enumerations.ConformanceResourceStatus;
import org.hl7.fhir.dstu2016may.model.Enumerations.ConformanceResourceStatusEnumFactory;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.utilities.Utilities;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
/**
 * A compartment definition that defines how resources are accessed on a server.
 */
@ResourceDef(name="CompartmentDefinition", profile="http://hl7.org/fhir/Profile/CompartmentDefinition")
public class CompartmentDefinition extends DomainResource {

    public enum CompartmentType {
        /**
         * The compartment definition is for the patient compartment
         */
        PATIENT, 
        /**
         * The compartment definition is for the encounter compartment
         */
        ENCOUNTER, 
        /**
         * The compartment definition is for the related-person compartment
         */
        RELATEDPERSON, 
        /**
         * The compartment definition is for the practitioner compartment
         */
        PRACTITIONER, 
        /**
         * The compartment definition is for the device compartment
         */
        DEVICE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static CompartmentType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("Patient".equals(codeString))
          return PATIENT;
        if ("Encounter".equals(codeString))
          return ENCOUNTER;
        if ("RelatedPerson".equals(codeString))
          return RELATEDPERSON;
        if ("Practitioner".equals(codeString))
          return PRACTITIONER;
        if ("Device".equals(codeString))
          return DEVICE;
        throw new FHIRException("Unknown CompartmentType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PATIENT: return "Patient";
            case ENCOUNTER: return "Encounter";
            case RELATEDPERSON: return "RelatedPerson";
            case PRACTITIONER: return "Practitioner";
            case DEVICE: return "Device";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PATIENT: return "http://hl7.org/fhir/compartment-type";
            case ENCOUNTER: return "http://hl7.org/fhir/compartment-type";
            case RELATEDPERSON: return "http://hl7.org/fhir/compartment-type";
            case PRACTITIONER: return "http://hl7.org/fhir/compartment-type";
            case DEVICE: return "http://hl7.org/fhir/compartment-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PATIENT: return "The compartment definition is for the patient compartment";
            case ENCOUNTER: return "The compartment definition is for the encounter compartment";
            case RELATEDPERSON: return "The compartment definition is for the related-person compartment";
            case PRACTITIONER: return "The compartment definition is for the practitioner compartment";
            case DEVICE: return "The compartment definition is for the device compartment";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PATIENT: return "Patient";
            case ENCOUNTER: return "Encounter";
            case RELATEDPERSON: return "RelatedPerson";
            case PRACTITIONER: return "Practitioner";
            case DEVICE: return "Device";
            default: return "?";
          }
        }
    }

  public static class CompartmentTypeEnumFactory implements EnumFactory<CompartmentType> {
    public CompartmentType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("Patient".equals(codeString))
          return CompartmentType.PATIENT;
        if ("Encounter".equals(codeString))
          return CompartmentType.ENCOUNTER;
        if ("RelatedPerson".equals(codeString))
          return CompartmentType.RELATEDPERSON;
        if ("Practitioner".equals(codeString))
          return CompartmentType.PRACTITIONER;
        if ("Device".equals(codeString))
          return CompartmentType.DEVICE;
        throw new IllegalArgumentException("Unknown CompartmentType code '"+codeString+"'");
        }
        public Enumeration<CompartmentType> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("Patient".equals(codeString))
          return new Enumeration<CompartmentType>(this, CompartmentType.PATIENT);
        if ("Encounter".equals(codeString))
          return new Enumeration<CompartmentType>(this, CompartmentType.ENCOUNTER);
        if ("RelatedPerson".equals(codeString))
          return new Enumeration<CompartmentType>(this, CompartmentType.RELATEDPERSON);
        if ("Practitioner".equals(codeString))
          return new Enumeration<CompartmentType>(this, CompartmentType.PRACTITIONER);
        if ("Device".equals(codeString))
          return new Enumeration<CompartmentType>(this, CompartmentType.DEVICE);
        throw new FHIRException("Unknown CompartmentType code '"+codeString+"'");
        }
    public String toCode(CompartmentType code) {
      if (code == CompartmentType.PATIENT)
        return "Patient";
      if (code == CompartmentType.ENCOUNTER)
        return "Encounter";
      if (code == CompartmentType.RELATEDPERSON)
        return "RelatedPerson";
      if (code == CompartmentType.PRACTITIONER)
        return "Practitioner";
      if (code == CompartmentType.DEVICE)
        return "Device";
      return "?";
      }
    public String toSystem(CompartmentType code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class CompartmentDefinitionContactComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The name of an individual to contact regarding the compartment definition.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Name of an individual to contact", formalDefinition="The name of an individual to contact regarding the compartment definition." )
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
      public CompartmentDefinitionContactComponent() {
        super();
      }

        /**
         * @return {@link #name} (The name of an individual to contact regarding the compartment definition.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CompartmentDefinitionContactComponent.name");
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
         * @param value {@link #name} (The name of an individual to contact regarding the compartment definition.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public CompartmentDefinitionContactComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return The name of an individual to contact regarding the compartment definition.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value The name of an individual to contact regarding the compartment definition.
         */
        public CompartmentDefinitionContactComponent setName(String value) { 
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
        public CompartmentDefinitionContactComponent addTelecom(ContactPoint t) { //3
          if (t == null)
            return this;
          if (this.telecom == null)
            this.telecom = new ArrayList<ContactPoint>();
          this.telecom.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "string", "The name of an individual to contact regarding the compartment definition.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("telecom", "ContactPoint", "Contact details for individual (if a name was provided) or the publisher.", 0, java.lang.Integer.MAX_VALUE, telecom));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -1429363305: /*telecom*/ return this.telecom == null ? new Base[0] : this.telecom.toArray(new Base[this.telecom.size()]); // ContactPoint
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToString(value); // StringType
          break;
        case -1429363305: // telecom
          this.getTelecom().add(castToContactPoint(value)); // ContactPoint
          break;
        default: super.setProperty(hash, name, value);
        }

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
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: throw new FHIRException("Cannot make property name as it is not a complex type"); // StringType
        case -1429363305:  return addTelecom(); // ContactPoint
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type CompartmentDefinition.name");
        }
        else if (name.equals("telecom")) {
          return addTelecom();
        }
        else
          return super.addChild(name);
      }

      public CompartmentDefinitionContactComponent copy() {
        CompartmentDefinitionContactComponent dst = new CompartmentDefinitionContactComponent();
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
        if (!(other instanceof CompartmentDefinitionContactComponent))
          return false;
        CompartmentDefinitionContactComponent o = (CompartmentDefinitionContactComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(telecom, o.telecom, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CompartmentDefinitionContactComponent))
          return false;
        CompartmentDefinitionContactComponent o = (CompartmentDefinitionContactComponent) other;
        return compareValues(name, o.name, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (telecom == null || telecom.isEmpty())
          ;
      }

  public String fhirType() {
    return "CompartmentDefinition.contact";

  }

  }

    @Block()
    public static class CompartmentDefinitionResourceComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The name of a resource supported by the server.
         */
        @Child(name = "code", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Name of resource type", formalDefinition="The name of a resource supported by the server." )
        protected CodeType code;

        /**
         * The name of a search parameter that represents the link to the compartment. More than one may be listed because a resource may be linked to a compartment more than one way.
         */
        @Child(name = "param", type = {StringType.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Search Parameter Name, or chained params", formalDefinition="The name of a search parameter that represents the link to the compartment. More than one may be listed because a resource may be linked to a compartment more than one way." )
        protected List<StringType> param;

        /**
         * Additional doco about the resource and compartment.
         */
        @Child(name = "documentation", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Additional doco about the resource and compartment", formalDefinition="Additional doco about the resource and compartment." )
        protected StringType documentation;

        private static final long serialVersionUID = 988080897L;

    /**
     * Constructor
     */
      public CompartmentDefinitionResourceComponent() {
        super();
      }

    /**
     * Constructor
     */
      public CompartmentDefinitionResourceComponent(CodeType code) {
        super();
        this.code = code;
      }

        /**
         * @return {@link #code} (The name of a resource supported by the server.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public CodeType getCodeElement() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CompartmentDefinitionResourceComponent.code");
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
         * @param value {@link #code} (The name of a resource supported by the server.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public CompartmentDefinitionResourceComponent setCodeElement(CodeType value) { 
          this.code = value;
          return this;
        }

        /**
         * @return The name of a resource supported by the server.
         */
        public String getCode() { 
          return this.code == null ? null : this.code.getValue();
        }

        /**
         * @param value The name of a resource supported by the server.
         */
        public CompartmentDefinitionResourceComponent setCode(String value) { 
            if (this.code == null)
              this.code = new CodeType();
            this.code.setValue(value);
          return this;
        }

        /**
         * @return {@link #param} (The name of a search parameter that represents the link to the compartment. More than one may be listed because a resource may be linked to a compartment more than one way.)
         */
        public List<StringType> getParam() { 
          if (this.param == null)
            this.param = new ArrayList<StringType>();
          return this.param;
        }

        public boolean hasParam() { 
          if (this.param == null)
            return false;
          for (StringType item : this.param)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #param} (The name of a search parameter that represents the link to the compartment. More than one may be listed because a resource may be linked to a compartment more than one way.)
         */
    // syntactic sugar
        public StringType addParamElement() {//2 
          StringType t = new StringType();
          if (this.param == null)
            this.param = new ArrayList<StringType>();
          this.param.add(t);
          return t;
        }

        /**
         * @param value {@link #param} (The name of a search parameter that represents the link to the compartment. More than one may be listed because a resource may be linked to a compartment more than one way.)
         */
        public CompartmentDefinitionResourceComponent addParam(String value) { //1
          StringType t = new StringType();
          t.setValue(value);
          if (this.param == null)
            this.param = new ArrayList<StringType>();
          this.param.add(t);
          return this;
        }

        /**
         * @param value {@link #param} (The name of a search parameter that represents the link to the compartment. More than one may be listed because a resource may be linked to a compartment more than one way.)
         */
        public boolean hasParam(String value) { 
          if (this.param == null)
            return false;
          for (StringType v : this.param)
            if (v.equals(value)) // string
              return true;
          return false;
        }

        /**
         * @return {@link #documentation} (Additional doco about the resource and compartment.). This is the underlying object with id, value and extensions. The accessor "getDocumentation" gives direct access to the value
         */
        public StringType getDocumentationElement() { 
          if (this.documentation == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CompartmentDefinitionResourceComponent.documentation");
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
         * @param value {@link #documentation} (Additional doco about the resource and compartment.). This is the underlying object with id, value and extensions. The accessor "getDocumentation" gives direct access to the value
         */
        public CompartmentDefinitionResourceComponent setDocumentationElement(StringType value) { 
          this.documentation = value;
          return this;
        }

        /**
         * @return Additional doco about the resource and compartment.
         */
        public String getDocumentation() { 
          return this.documentation == null ? null : this.documentation.getValue();
        }

        /**
         * @param value Additional doco about the resource and compartment.
         */
        public CompartmentDefinitionResourceComponent setDocumentation(String value) { 
          if (Utilities.noString(value))
            this.documentation = null;
          else {
            if (this.documentation == null)
              this.documentation = new StringType();
            this.documentation.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("code", "code", "The name of a resource supported by the server.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("param", "string", "The name of a search parameter that represents the link to the compartment. More than one may be listed because a resource may be linked to a compartment more than one way.", 0, java.lang.Integer.MAX_VALUE, param));
          childrenList.add(new Property("documentation", "string", "Additional doco about the resource and compartment.", 0, java.lang.Integer.MAX_VALUE, documentation));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeType
        case 106436749: /*param*/ return this.param == null ? new Base[0] : this.param.toArray(new Base[this.param.size()]); // StringType
        case 1587405498: /*documentation*/ return this.documentation == null ? new Base[0] : new Base[] {this.documentation}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3059181: // code
          this.code = castToCode(value); // CodeType
          break;
        case 106436749: // param
          this.getParam().add(castToString(value)); // StringType
          break;
        case 1587405498: // documentation
          this.documentation = castToString(value); // StringType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code"))
          this.code = castToCode(value); // CodeType
        else if (name.equals("param"))
          this.getParam().add(castToString(value));
        else if (name.equals("documentation"))
          this.documentation = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181: throw new FHIRException("Cannot make property code as it is not a complex type"); // CodeType
        case 106436749: throw new FHIRException("Cannot make property param as it is not a complex type"); // StringType
        case 1587405498: throw new FHIRException("Cannot make property documentation as it is not a complex type"); // StringType
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          throw new FHIRException("Cannot call addChild on a primitive type CompartmentDefinition.code");
        }
        else if (name.equals("param")) {
          throw new FHIRException("Cannot call addChild on a primitive type CompartmentDefinition.param");
        }
        else if (name.equals("documentation")) {
          throw new FHIRException("Cannot call addChild on a primitive type CompartmentDefinition.documentation");
        }
        else
          return super.addChild(name);
      }

      public CompartmentDefinitionResourceComponent copy() {
        CompartmentDefinitionResourceComponent dst = new CompartmentDefinitionResourceComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        if (param != null) {
          dst.param = new ArrayList<StringType>();
          for (StringType i : param)
            dst.param.add(i.copy());
        };
        dst.documentation = documentation == null ? null : documentation.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CompartmentDefinitionResourceComponent))
          return false;
        CompartmentDefinitionResourceComponent o = (CompartmentDefinitionResourceComponent) other;
        return compareDeep(code, o.code, true) && compareDeep(param, o.param, true) && compareDeep(documentation, o.documentation, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CompartmentDefinitionResourceComponent))
          return false;
        CompartmentDefinitionResourceComponent o = (CompartmentDefinitionResourceComponent) other;
        return compareValues(code, o.code, true) && compareValues(param, o.param, true) && compareValues(documentation, o.documentation, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (code == null || code.isEmpty()) && (param == null || param.isEmpty())
           && (documentation == null || documentation.isEmpty());
      }

  public String fhirType() {
    return "CompartmentDefinition.resource";

  }

  }

    /**
     * An absolute URL that is used to identify this compartment definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this compartment definition is (or will be) published.
     */
    @Child(name = "url", type = {UriType.class}, order=0, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Absolute URL used to reference this compartment definition", formalDefinition="An absolute URL that is used to identify this compartment definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this compartment definition is (or will be) published." )
    protected UriType url;

    /**
     * A free text natural language name identifying the compartment definition.
     */
    @Child(name = "name", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Informal name for this compartment definition", formalDefinition="A free text natural language name identifying the compartment definition." )
    protected StringType name;

    /**
     * The status of this compartment definition definition.
     */
    @Child(name = "status", type = {CodeType.class}, order=2, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="draft | active | retired", formalDefinition="The status of this compartment definition definition." )
    protected Enumeration<ConformanceResourceStatus> status;

    /**
     * A flag to indicate that this compartment definition definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    @Child(name = "experimental", type = {BooleanType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="If for testing purposes, not real usage", formalDefinition="A flag to indicate that this compartment definition definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage." )
    protected BooleanType experimental;

    /**
     * The name of the individual or organization that published the compartment definition.
     */
    @Child(name = "publisher", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Name of the publisher (Organization or individual)", formalDefinition="The name of the individual or organization that published the compartment definition." )
    protected StringType publisher;

    /**
     * Contacts to assist a user in finding and communicating with the publisher.
     */
    @Child(name = "contact", type = {}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Contact details of the publisher", formalDefinition="Contacts to assist a user in finding and communicating with the publisher." )
    protected List<CompartmentDefinitionContactComponent> contact;

    /**
     * The date  (and optionally time) when the compartment definition definition was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the compartment definition changes.
     */
    @Child(name = "date", type = {DateTimeType.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Publication Date(/time)", formalDefinition="The date  (and optionally time) when the compartment definition definition was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the compartment definition changes." )
    protected DateTimeType date;

    /**
     * A free text natural language description of the CompartmentDefinition and its use.
     */
    @Child(name = "description", type = {StringType.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Natural language description of the CompartmentDefinition", formalDefinition="A free text natural language description of the CompartmentDefinition and its use." )
    protected StringType description;

    /**
     * The Scope and Usage that this compartment definition was created to meet.
     */
    @Child(name = "requirements", type = {StringType.class}, order=8, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Why this compartment definition is defined", formalDefinition="The Scope and Usage that this compartment definition was created to meet." )
    protected StringType requirements;

    /**
     * Which compartment this definition describes.
     */
    @Child(name = "code", type = {CodeType.class}, order=9, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Patient | Encounter | RelatedPerson | Practitioner | Device", formalDefinition="Which compartment this definition describes." )
    protected Enumeration<CompartmentType> code;

    /**
     * Whether the search syntax is supported.
     */
    @Child(name = "search", type = {BooleanType.class}, order=10, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Whether the search syntax is supported", formalDefinition="Whether the search syntax is supported." )
    protected BooleanType search;

    /**
     * Information about how a resource it related to the compartment.
     */
    @Child(name = "resource", type = {}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="How resource is related to the compartment", formalDefinition="Information about how a resource it related to the compartment." )
    protected List<CompartmentDefinitionResourceComponent> resource;

    private static final long serialVersionUID = -1431357313L;

  /**
   * Constructor
   */
    public CompartmentDefinition() {
      super();
    }

  /**
   * Constructor
   */
    public CompartmentDefinition(UriType url, StringType name, Enumeration<CompartmentType> code, BooleanType search) {
      super();
      this.url = url;
      this.name = name;
      this.code = code;
      this.search = search;
    }

    /**
     * @return {@link #url} (An absolute URL that is used to identify this compartment definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this compartment definition is (or will be) published.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public UriType getUrlElement() { 
      if (this.url == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CompartmentDefinition.url");
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
     * @param value {@link #url} (An absolute URL that is used to identify this compartment definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this compartment definition is (or will be) published.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public CompartmentDefinition setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return An absolute URL that is used to identify this compartment definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this compartment definition is (or will be) published.
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value An absolute URL that is used to identify this compartment definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this compartment definition is (or will be) published.
     */
    public CompartmentDefinition setUrl(String value) { 
        if (this.url == null)
          this.url = new UriType();
        this.url.setValue(value);
      return this;
    }

    /**
     * @return {@link #name} (A free text natural language name identifying the compartment definition.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CompartmentDefinition.name");
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
     * @param value {@link #name} (A free text natural language name identifying the compartment definition.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public CompartmentDefinition setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return A free text natural language name identifying the compartment definition.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value A free text natural language name identifying the compartment definition.
     */
    public CompartmentDefinition setName(String value) { 
        if (this.name == null)
          this.name = new StringType();
        this.name.setValue(value);
      return this;
    }

    /**
     * @return {@link #status} (The status of this compartment definition definition.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<ConformanceResourceStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CompartmentDefinition.status");
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
     * @param value {@link #status} (The status of this compartment definition definition.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public CompartmentDefinition setStatusElement(Enumeration<ConformanceResourceStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of this compartment definition definition.
     */
    public ConformanceResourceStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of this compartment definition definition.
     */
    public CompartmentDefinition setStatus(ConformanceResourceStatus value) { 
      if (value == null)
        this.status = null;
      else {
        if (this.status == null)
          this.status = new Enumeration<ConformanceResourceStatus>(new ConformanceResourceStatusEnumFactory());
        this.status.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #experimental} (A flag to indicate that this compartment definition definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public BooleanType getExperimentalElement() { 
      if (this.experimental == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CompartmentDefinition.experimental");
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
     * @param value {@link #experimental} (A flag to indicate that this compartment definition definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public CompartmentDefinition setExperimentalElement(BooleanType value) { 
      this.experimental = value;
      return this;
    }

    /**
     * @return A flag to indicate that this compartment definition definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public boolean getExperimental() { 
      return this.experimental == null || this.experimental.isEmpty() ? false : this.experimental.getValue();
    }

    /**
     * @param value A flag to indicate that this compartment definition definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public CompartmentDefinition setExperimental(boolean value) { 
        if (this.experimental == null)
          this.experimental = new BooleanType();
        this.experimental.setValue(value);
      return this;
    }

    /**
     * @return {@link #publisher} (The name of the individual or organization that published the compartment definition.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public StringType getPublisherElement() { 
      if (this.publisher == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CompartmentDefinition.publisher");
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
     * @param value {@link #publisher} (The name of the individual or organization that published the compartment definition.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public CompartmentDefinition setPublisherElement(StringType value) { 
      this.publisher = value;
      return this;
    }

    /**
     * @return The name of the individual or organization that published the compartment definition.
     */
    public String getPublisher() { 
      return this.publisher == null ? null : this.publisher.getValue();
    }

    /**
     * @param value The name of the individual or organization that published the compartment definition.
     */
    public CompartmentDefinition setPublisher(String value) { 
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
    public List<CompartmentDefinitionContactComponent> getContact() { 
      if (this.contact == null)
        this.contact = new ArrayList<CompartmentDefinitionContactComponent>();
      return this.contact;
    }

    public boolean hasContact() { 
      if (this.contact == null)
        return false;
      for (CompartmentDefinitionContactComponent item : this.contact)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #contact} (Contacts to assist a user in finding and communicating with the publisher.)
     */
    // syntactic sugar
    public CompartmentDefinitionContactComponent addContact() { //3
      CompartmentDefinitionContactComponent t = new CompartmentDefinitionContactComponent();
      if (this.contact == null)
        this.contact = new ArrayList<CompartmentDefinitionContactComponent>();
      this.contact.add(t);
      return t;
    }

    // syntactic sugar
    public CompartmentDefinition addContact(CompartmentDefinitionContactComponent t) { //3
      if (t == null)
        return this;
      if (this.contact == null)
        this.contact = new ArrayList<CompartmentDefinitionContactComponent>();
      this.contact.add(t);
      return this;
    }

    /**
     * @return {@link #date} (The date  (and optionally time) when the compartment definition definition was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the compartment definition changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CompartmentDefinition.date");
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
     * @param value {@link #date} (The date  (and optionally time) when the compartment definition definition was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the compartment definition changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public CompartmentDefinition setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date  (and optionally time) when the compartment definition definition was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the compartment definition changes.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date  (and optionally time) when the compartment definition definition was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the compartment definition changes.
     */
    public CompartmentDefinition setDate(Date value) { 
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
     * @return {@link #description} (A free text natural language description of the CompartmentDefinition and its use.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CompartmentDefinition.description");
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
     * @param value {@link #description} (A free text natural language description of the CompartmentDefinition and its use.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public CompartmentDefinition setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A free text natural language description of the CompartmentDefinition and its use.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A free text natural language description of the CompartmentDefinition and its use.
     */
    public CompartmentDefinition setDescription(String value) { 
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
     * @return {@link #requirements} (The Scope and Usage that this compartment definition was created to meet.). This is the underlying object with id, value and extensions. The accessor "getRequirements" gives direct access to the value
     */
    public StringType getRequirementsElement() { 
      if (this.requirements == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CompartmentDefinition.requirements");
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
     * @param value {@link #requirements} (The Scope and Usage that this compartment definition was created to meet.). This is the underlying object with id, value and extensions. The accessor "getRequirements" gives direct access to the value
     */
    public CompartmentDefinition setRequirementsElement(StringType value) { 
      this.requirements = value;
      return this;
    }

    /**
     * @return The Scope and Usage that this compartment definition was created to meet.
     */
    public String getRequirements() { 
      return this.requirements == null ? null : this.requirements.getValue();
    }

    /**
     * @param value The Scope and Usage that this compartment definition was created to meet.
     */
    public CompartmentDefinition setRequirements(String value) { 
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
     * @return {@link #code} (Which compartment this definition describes.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
     */
    public Enumeration<CompartmentType> getCodeElement() { 
      if (this.code == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CompartmentDefinition.code");
        else if (Configuration.doAutoCreate())
          this.code = new Enumeration<CompartmentType>(new CompartmentTypeEnumFactory()); // bb
      return this.code;
    }

    public boolean hasCodeElement() { 
      return this.code != null && !this.code.isEmpty();
    }

    public boolean hasCode() { 
      return this.code != null && !this.code.isEmpty();
    }

    /**
     * @param value {@link #code} (Which compartment this definition describes.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
     */
    public CompartmentDefinition setCodeElement(Enumeration<CompartmentType> value) { 
      this.code = value;
      return this;
    }

    /**
     * @return Which compartment this definition describes.
     */
    public CompartmentType getCode() { 
      return this.code == null ? null : this.code.getValue();
    }

    /**
     * @param value Which compartment this definition describes.
     */
    public CompartmentDefinition setCode(CompartmentType value) { 
        if (this.code == null)
          this.code = new Enumeration<CompartmentType>(new CompartmentTypeEnumFactory());
        this.code.setValue(value);
      return this;
    }

    /**
     * @return {@link #search} (Whether the search syntax is supported.). This is the underlying object with id, value and extensions. The accessor "getSearch" gives direct access to the value
     */
    public BooleanType getSearchElement() { 
      if (this.search == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CompartmentDefinition.search");
        else if (Configuration.doAutoCreate())
          this.search = new BooleanType(); // bb
      return this.search;
    }

    public boolean hasSearchElement() { 
      return this.search != null && !this.search.isEmpty();
    }

    public boolean hasSearch() { 
      return this.search != null && !this.search.isEmpty();
    }

    /**
     * @param value {@link #search} (Whether the search syntax is supported.). This is the underlying object with id, value and extensions. The accessor "getSearch" gives direct access to the value
     */
    public CompartmentDefinition setSearchElement(BooleanType value) { 
      this.search = value;
      return this;
    }

    /**
     * @return Whether the search syntax is supported.
     */
    public boolean getSearch() { 
      return this.search == null || this.search.isEmpty() ? false : this.search.getValue();
    }

    /**
     * @param value Whether the search syntax is supported.
     */
    public CompartmentDefinition setSearch(boolean value) { 
        if (this.search == null)
          this.search = new BooleanType();
        this.search.setValue(value);
      return this;
    }

    /**
     * @return {@link #resource} (Information about how a resource it related to the compartment.)
     */
    public List<CompartmentDefinitionResourceComponent> getResource() { 
      if (this.resource == null)
        this.resource = new ArrayList<CompartmentDefinitionResourceComponent>();
      return this.resource;
    }

    public boolean hasResource() { 
      if (this.resource == null)
        return false;
      for (CompartmentDefinitionResourceComponent item : this.resource)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #resource} (Information about how a resource it related to the compartment.)
     */
    // syntactic sugar
    public CompartmentDefinitionResourceComponent addResource() { //3
      CompartmentDefinitionResourceComponent t = new CompartmentDefinitionResourceComponent();
      if (this.resource == null)
        this.resource = new ArrayList<CompartmentDefinitionResourceComponent>();
      this.resource.add(t);
      return t;
    }

    // syntactic sugar
    public CompartmentDefinition addResource(CompartmentDefinitionResourceComponent t) { //3
      if (t == null)
        return this;
      if (this.resource == null)
        this.resource = new ArrayList<CompartmentDefinitionResourceComponent>();
      this.resource.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("url", "uri", "An absolute URL that is used to identify this compartment definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this compartment definition is (or will be) published.", 0, java.lang.Integer.MAX_VALUE, url));
        childrenList.add(new Property("name", "string", "A free text natural language name identifying the compartment definition.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("status", "code", "The status of this compartment definition definition.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("experimental", "boolean", "A flag to indicate that this compartment definition definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.", 0, java.lang.Integer.MAX_VALUE, experimental));
        childrenList.add(new Property("publisher", "string", "The name of the individual or organization that published the compartment definition.", 0, java.lang.Integer.MAX_VALUE, publisher));
        childrenList.add(new Property("contact", "", "Contacts to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact));
        childrenList.add(new Property("date", "dateTime", "The date  (and optionally time) when the compartment definition definition was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the compartment definition changes.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("description", "string", "A free text natural language description of the CompartmentDefinition and its use.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("requirements", "string", "The Scope and Usage that this compartment definition was created to meet.", 0, java.lang.Integer.MAX_VALUE, requirements));
        childrenList.add(new Property("code", "code", "Which compartment this definition describes.", 0, java.lang.Integer.MAX_VALUE, code));
        childrenList.add(new Property("search", "boolean", "Whether the search syntax is supported.", 0, java.lang.Integer.MAX_VALUE, search));
        childrenList.add(new Property("resource", "", "Information about how a resource it related to the compartment.", 0, java.lang.Integer.MAX_VALUE, resource));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<ConformanceResourceStatus>
        case -404562712: /*experimental*/ return this.experimental == null ? new Base[0] : new Base[] {this.experimental}; // BooleanType
        case 1447404028: /*publisher*/ return this.publisher == null ? new Base[0] : new Base[] {this.publisher}; // StringType
        case 951526432: /*contact*/ return this.contact == null ? new Base[0] : this.contact.toArray(new Base[this.contact.size()]); // CompartmentDefinitionContactComponent
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateTimeType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case -1619874672: /*requirements*/ return this.requirements == null ? new Base[0] : new Base[] {this.requirements}; // StringType
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // Enumeration<CompartmentType>
        case -906336856: /*search*/ return this.search == null ? new Base[0] : new Base[] {this.search}; // BooleanType
        case -341064690: /*resource*/ return this.resource == null ? new Base[0] : this.resource.toArray(new Base[this.resource.size()]); // CompartmentDefinitionResourceComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 116079: // url
          this.url = castToUri(value); // UriType
          break;
        case 3373707: // name
          this.name = castToString(value); // StringType
          break;
        case -892481550: // status
          this.status = new ConformanceResourceStatusEnumFactory().fromType(value); // Enumeration<ConformanceResourceStatus>
          break;
        case -404562712: // experimental
          this.experimental = castToBoolean(value); // BooleanType
          break;
        case 1447404028: // publisher
          this.publisher = castToString(value); // StringType
          break;
        case 951526432: // contact
          this.getContact().add((CompartmentDefinitionContactComponent) value); // CompartmentDefinitionContactComponent
          break;
        case 3076014: // date
          this.date = castToDateTime(value); // DateTimeType
          break;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          break;
        case -1619874672: // requirements
          this.requirements = castToString(value); // StringType
          break;
        case 3059181: // code
          this.code = new CompartmentTypeEnumFactory().fromType(value); // Enumeration<CompartmentType>
          break;
        case -906336856: // search
          this.search = castToBoolean(value); // BooleanType
          break;
        case -341064690: // resource
          this.getResource().add((CompartmentDefinitionResourceComponent) value); // CompartmentDefinitionResourceComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("url"))
          this.url = castToUri(value); // UriType
        else if (name.equals("name"))
          this.name = castToString(value); // StringType
        else if (name.equals("status"))
          this.status = new ConformanceResourceStatusEnumFactory().fromType(value); // Enumeration<ConformanceResourceStatus>
        else if (name.equals("experimental"))
          this.experimental = castToBoolean(value); // BooleanType
        else if (name.equals("publisher"))
          this.publisher = castToString(value); // StringType
        else if (name.equals("contact"))
          this.getContact().add((CompartmentDefinitionContactComponent) value);
        else if (name.equals("date"))
          this.date = castToDateTime(value); // DateTimeType
        else if (name.equals("description"))
          this.description = castToString(value); // StringType
        else if (name.equals("requirements"))
          this.requirements = castToString(value); // StringType
        else if (name.equals("code"))
          this.code = new CompartmentTypeEnumFactory().fromType(value); // Enumeration<CompartmentType>
        else if (name.equals("search"))
          this.search = castToBoolean(value); // BooleanType
        else if (name.equals("resource"))
          this.getResource().add((CompartmentDefinitionResourceComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116079: throw new FHIRException("Cannot make property url as it is not a complex type"); // UriType
        case 3373707: throw new FHIRException("Cannot make property name as it is not a complex type"); // StringType
        case -892481550: throw new FHIRException("Cannot make property status as it is not a complex type"); // Enumeration<ConformanceResourceStatus>
        case -404562712: throw new FHIRException("Cannot make property experimental as it is not a complex type"); // BooleanType
        case 1447404028: throw new FHIRException("Cannot make property publisher as it is not a complex type"); // StringType
        case 951526432:  return addContact(); // CompartmentDefinitionContactComponent
        case 3076014: throw new FHIRException("Cannot make property date as it is not a complex type"); // DateTimeType
        case -1724546052: throw new FHIRException("Cannot make property description as it is not a complex type"); // StringType
        case -1619874672: throw new FHIRException("Cannot make property requirements as it is not a complex type"); // StringType
        case 3059181: throw new FHIRException("Cannot make property code as it is not a complex type"); // Enumeration<CompartmentType>
        case -906336856: throw new FHIRException("Cannot make property search as it is not a complex type"); // BooleanType
        case -341064690:  return addResource(); // CompartmentDefinitionResourceComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type CompartmentDefinition.url");
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type CompartmentDefinition.name");
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type CompartmentDefinition.status");
        }
        else if (name.equals("experimental")) {
          throw new FHIRException("Cannot call addChild on a primitive type CompartmentDefinition.experimental");
        }
        else if (name.equals("publisher")) {
          throw new FHIRException("Cannot call addChild on a primitive type CompartmentDefinition.publisher");
        }
        else if (name.equals("contact")) {
          return addContact();
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type CompartmentDefinition.date");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type CompartmentDefinition.description");
        }
        else if (name.equals("requirements")) {
          throw new FHIRException("Cannot call addChild on a primitive type CompartmentDefinition.requirements");
        }
        else if (name.equals("code")) {
          throw new FHIRException("Cannot call addChild on a primitive type CompartmentDefinition.code");
        }
        else if (name.equals("search")) {
          throw new FHIRException("Cannot call addChild on a primitive type CompartmentDefinition.search");
        }
        else if (name.equals("resource")) {
          return addResource();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "CompartmentDefinition";

  }

      public CompartmentDefinition copy() {
        CompartmentDefinition dst = new CompartmentDefinition();
        copyValues(dst);
        dst.url = url == null ? null : url.copy();
        dst.name = name == null ? null : name.copy();
        dst.status = status == null ? null : status.copy();
        dst.experimental = experimental == null ? null : experimental.copy();
        dst.publisher = publisher == null ? null : publisher.copy();
        if (contact != null) {
          dst.contact = new ArrayList<CompartmentDefinitionContactComponent>();
          for (CompartmentDefinitionContactComponent i : contact)
            dst.contact.add(i.copy());
        };
        dst.date = date == null ? null : date.copy();
        dst.description = description == null ? null : description.copy();
        dst.requirements = requirements == null ? null : requirements.copy();
        dst.code = code == null ? null : code.copy();
        dst.search = search == null ? null : search.copy();
        if (resource != null) {
          dst.resource = new ArrayList<CompartmentDefinitionResourceComponent>();
          for (CompartmentDefinitionResourceComponent i : resource)
            dst.resource.add(i.copy());
        };
        return dst;
      }

      protected CompartmentDefinition typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CompartmentDefinition))
          return false;
        CompartmentDefinition o = (CompartmentDefinition) other;
        return compareDeep(url, o.url, true) && compareDeep(name, o.name, true) && compareDeep(status, o.status, true)
           && compareDeep(experimental, o.experimental, true) && compareDeep(publisher, o.publisher, true)
           && compareDeep(contact, o.contact, true) && compareDeep(date, o.date, true) && compareDeep(description, o.description, true)
           && compareDeep(requirements, o.requirements, true) && compareDeep(code, o.code, true) && compareDeep(search, o.search, true)
           && compareDeep(resource, o.resource, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CompartmentDefinition))
          return false;
        CompartmentDefinition o = (CompartmentDefinition) other;
        return compareValues(url, o.url, true) && compareValues(name, o.name, true) && compareValues(status, o.status, true)
           && compareValues(experimental, o.experimental, true) && compareValues(publisher, o.publisher, true)
           && compareValues(date, o.date, true) && compareValues(description, o.description, true) && compareValues(requirements, o.requirements, true)
           && compareValues(code, o.code, true) && compareValues(search, o.search, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (url == null || url.isEmpty()) && (name == null || name.isEmpty())
           && (status == null || status.isEmpty()) && (experimental == null || experimental.isEmpty())
           && (publisher == null || publisher.isEmpty()) && (contact == null || contact.isEmpty()) && (date == null || date.isEmpty())
           && (description == null || description.isEmpty()) && (requirements == null || requirements.isEmpty())
           && (code == null || code.isEmpty()) && (search == null || search.isEmpty()) && (resource == null || resource.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.CompartmentDefinition;
   }

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>draft | active | retired</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CompartmentDefinition.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="CompartmentDefinition.status", description="draft | active | retired", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>draft | active | retired</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CompartmentDefinition.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);

 /**
   * Search parameter: <b>name</b>
   * <p>
   * Description: <b>Informal name for this compartment definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>CompartmentDefinition.name</b><br>
   * </p>
   */
  @SearchParamDefinition(name="name", path="CompartmentDefinition.name", description="Informal name for this compartment definition", type="string" )
  public static final String SP_NAME = "name";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>name</b>
   * <p>
   * Description: <b>Informal name for this compartment definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>CompartmentDefinition.name</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam NAME = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_NAME);

 /**
   * Search parameter: <b>resource</b>
   * <p>
   * Description: <b>Name of resource type</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CompartmentDefinition.resource.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="resource", path="CompartmentDefinition.resource.code", description="Name of resource type", type="token" )
  public static final String SP_RESOURCE = "resource";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>resource</b>
   * <p>
   * Description: <b>Name of resource type</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CompartmentDefinition.resource.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam RESOURCE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_RESOURCE);

 /**
   * Search parameter: <b>code</b>
   * <p>
   * Description: <b>Patient | Encounter | RelatedPerson | Practitioner | Device</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CompartmentDefinition.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="code", path="CompartmentDefinition.code", description="Patient | Encounter | RelatedPerson | Practitioner | Device", type="token" )
  public static final String SP_CODE = "code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>code</b>
   * <p>
   * Description: <b>Patient | Encounter | RelatedPerson | Practitioner | Device</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CompartmentDefinition.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CODE);

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>Publication Date(/time)</b><br>
   * Type: <b>date</b><br>
   * Path: <b>CompartmentDefinition.date</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="CompartmentDefinition.date", description="Publication Date(/time)", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>Publication Date(/time)</b><br>
   * Type: <b>date</b><br>
   * Path: <b>CompartmentDefinition.date</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>url</b>
   * <p>
   * Description: <b>Absolute URL used to reference this compartment definition</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>CompartmentDefinition.url</b><br>
   * </p>
   */
  @SearchParamDefinition(name="url", path="CompartmentDefinition.url", description="Absolute URL used to reference this compartment definition", type="uri" )
  public static final String SP_URL = "url";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>url</b>
   * <p>
   * Description: <b>Absolute URL used to reference this compartment definition</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>CompartmentDefinition.url</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam URL = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_URL);


}

