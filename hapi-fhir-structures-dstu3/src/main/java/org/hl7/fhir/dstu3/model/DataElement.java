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

// Generated on Sat, Jan 30, 2016 09:18-0500 for FHIR v1.3.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;

import org.hl7.fhir.dstu3.exceptions.FHIRException;
import org.hl7.fhir.dstu3.model.Enumerations.*;
import org.hl7.fhir.instance.model.api.*;
/**
 * The formal description of a single piece of information that can be gathered and reported.
 */
@ResourceDef(name="DataElement", profile="http://hl7.org/fhir/Profile/DataElement")
public class DataElement extends DomainResource {

    public enum DataElementStringency {
        /**
         * The data element is sufficiently well-constrained that multiple pieces of data captured according to the constraints of the data element will be comparable (though in some cases, a degree of automated conversion/normalization may be required).
         */
        COMPARABLE, 
        /**
         * The data element is fully specified down to a single value set, single unit of measure, single data type, etc.  Multiple pieces of data associated with this data element are fully comparable.
         */
        FULLYSPECIFIED, 
        /**
         * The data element allows multiple units of measure having equivalent meaning; e.g. "cc" (cubic centimeter) and "mL" (milliliter).
         */
        EQUIVALENT, 
        /**
         * The data element allows multiple units of measure that are convertable between each other (e.g. inches and centimeters) and/or allows data to be captured in multiple value sets for which a known mapping exists allowing conversion of meaning.
         */
        CONVERTABLE, 
        /**
         * A convertable data element where unit conversions are different only by a power of 10; e.g. g, mg, kg.
         */
        SCALEABLE, 
        /**
         * The data element is unconstrained in units, choice of data types and/or choice of vocabulary such that automated comparison of data captured using the data element is not possible.
         */
        FLEXIBLE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static DataElementStringency fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("comparable".equals(codeString))
          return COMPARABLE;
        if ("fully-specified".equals(codeString))
          return FULLYSPECIFIED;
        if ("equivalent".equals(codeString))
          return EQUIVALENT;
        if ("convertable".equals(codeString))
          return CONVERTABLE;
        if ("scaleable".equals(codeString))
          return SCALEABLE;
        if ("flexible".equals(codeString))
          return FLEXIBLE;
        throw new FHIRException("Unknown DataElementStringency code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case COMPARABLE: return "comparable";
            case FULLYSPECIFIED: return "fully-specified";
            case EQUIVALENT: return "equivalent";
            case CONVERTABLE: return "convertable";
            case SCALEABLE: return "scaleable";
            case FLEXIBLE: return "flexible";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case COMPARABLE: return "http://hl7.org/fhir/dataelement-stringency";
            case FULLYSPECIFIED: return "http://hl7.org/fhir/dataelement-stringency";
            case EQUIVALENT: return "http://hl7.org/fhir/dataelement-stringency";
            case CONVERTABLE: return "http://hl7.org/fhir/dataelement-stringency";
            case SCALEABLE: return "http://hl7.org/fhir/dataelement-stringency";
            case FLEXIBLE: return "http://hl7.org/fhir/dataelement-stringency";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case COMPARABLE: return "The data element is sufficiently well-constrained that multiple pieces of data captured according to the constraints of the data element will be comparable (though in some cases, a degree of automated conversion/normalization may be required).";
            case FULLYSPECIFIED: return "The data element is fully specified down to a single value set, single unit of measure, single data type, etc.  Multiple pieces of data associated with this data element are fully comparable.";
            case EQUIVALENT: return "The data element allows multiple units of measure having equivalent meaning; e.g. \"cc\" (cubic centimeter) and \"mL\" (milliliter).";
            case CONVERTABLE: return "The data element allows multiple units of measure that are convertable between each other (e.g. inches and centimeters) and/or allows data to be captured in multiple value sets for which a known mapping exists allowing conversion of meaning.";
            case SCALEABLE: return "A convertable data element where unit conversions are different only by a power of 10; e.g. g, mg, kg.";
            case FLEXIBLE: return "The data element is unconstrained in units, choice of data types and/or choice of vocabulary such that automated comparison of data captured using the data element is not possible.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case COMPARABLE: return "Comparable";
            case FULLYSPECIFIED: return "Fully Specified";
            case EQUIVALENT: return "Equivalent";
            case CONVERTABLE: return "Convertable";
            case SCALEABLE: return "Scaleable";
            case FLEXIBLE: return "Flexible";
            default: return "?";
          }
        }
    }

  public static class DataElementStringencyEnumFactory implements EnumFactory<DataElementStringency> {
    public DataElementStringency fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("comparable".equals(codeString))
          return DataElementStringency.COMPARABLE;
        if ("fully-specified".equals(codeString))
          return DataElementStringency.FULLYSPECIFIED;
        if ("equivalent".equals(codeString))
          return DataElementStringency.EQUIVALENT;
        if ("convertable".equals(codeString))
          return DataElementStringency.CONVERTABLE;
        if ("scaleable".equals(codeString))
          return DataElementStringency.SCALEABLE;
        if ("flexible".equals(codeString))
          return DataElementStringency.FLEXIBLE;
        throw new IllegalArgumentException("Unknown DataElementStringency code '"+codeString+"'");
        }
        public Enumeration<DataElementStringency> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("comparable".equals(codeString))
          return new Enumeration<DataElementStringency>(this, DataElementStringency.COMPARABLE);
        if ("fully-specified".equals(codeString))
          return new Enumeration<DataElementStringency>(this, DataElementStringency.FULLYSPECIFIED);
        if ("equivalent".equals(codeString))
          return new Enumeration<DataElementStringency>(this, DataElementStringency.EQUIVALENT);
        if ("convertable".equals(codeString))
          return new Enumeration<DataElementStringency>(this, DataElementStringency.CONVERTABLE);
        if ("scaleable".equals(codeString))
          return new Enumeration<DataElementStringency>(this, DataElementStringency.SCALEABLE);
        if ("flexible".equals(codeString))
          return new Enumeration<DataElementStringency>(this, DataElementStringency.FLEXIBLE);
        throw new FHIRException("Unknown DataElementStringency code '"+codeString+"'");
        }
    public String toCode(DataElementStringency code) {
      if (code == DataElementStringency.COMPARABLE)
        return "comparable";
      if (code == DataElementStringency.FULLYSPECIFIED)
        return "fully-specified";
      if (code == DataElementStringency.EQUIVALENT)
        return "equivalent";
      if (code == DataElementStringency.CONVERTABLE)
        return "convertable";
      if (code == DataElementStringency.SCALEABLE)
        return "scaleable";
      if (code == DataElementStringency.FLEXIBLE)
        return "flexible";
      return "?";
      }
    public String toSystem(DataElementStringency code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class DataElementContactComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The name of an individual to contact regarding the data element.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Name of a individual to contact", formalDefinition="The name of an individual to contact regarding the data element." )
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
      public DataElementContactComponent() {
        super();
      }

        /**
         * @return {@link #name} (The name of an individual to contact regarding the data element.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DataElementContactComponent.name");
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
         * @param value {@link #name} (The name of an individual to contact regarding the data element.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public DataElementContactComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return The name of an individual to contact regarding the data element.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value The name of an individual to contact regarding the data element.
         */
        public DataElementContactComponent setName(String value) { 
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
        public DataElementContactComponent addTelecom(ContactPoint t) { //3
          if (t == null)
            return this;
          if (this.telecom == null)
            this.telecom = new ArrayList<ContactPoint>();
          this.telecom.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "string", "The name of an individual to contact regarding the data element.", 0, java.lang.Integer.MAX_VALUE, name));
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
          throw new FHIRException("Cannot call addChild on a primitive type DataElement.name");
        }
        else if (name.equals("telecom")) {
          return addTelecom();
        }
        else
          return super.addChild(name);
      }

      public DataElementContactComponent copy() {
        DataElementContactComponent dst = new DataElementContactComponent();
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
        if (!(other instanceof DataElementContactComponent))
          return false;
        DataElementContactComponent o = (DataElementContactComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(telecom, o.telecom, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DataElementContactComponent))
          return false;
        DataElementContactComponent o = (DataElementContactComponent) other;
        return compareValues(name, o.name, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (telecom == null || telecom.isEmpty())
          ;
      }

  public String fhirType() {
    return "DataElement.contact";

  }

  }

    @Block()
    public static class DataElementMappingComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * An internal id that is used to identify this mapping set when specific mappings are made on a per-element basis.
         */
        @Child(name = "identity", type = {IdType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Internal id when this mapping is used", formalDefinition="An internal id that is used to identify this mapping set when specific mappings are made on a per-element basis." )
        protected IdType identity;

        /**
         * An absolute URI that identifies the specification that this mapping is expressed to.
         */
        @Child(name = "uri", type = {UriType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Identifies what this mapping refers to", formalDefinition="An absolute URI that identifies the specification that this mapping is expressed to." )
        protected UriType uri;

        /**
         * A name for the specification that is being mapped to.
         */
        @Child(name = "name", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Names what this mapping refers to", formalDefinition="A name for the specification that is being mapped to." )
        protected StringType name;

        /**
         * Comments about this mapping, including version notes, issues, scope limitations, and other important notes for usage.
         */
        @Child(name = "comments", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Versions, Issues, Scope limitations etc.", formalDefinition="Comments about this mapping, including version notes, issues, scope limitations, and other important notes for usage." )
        protected StringType comments;

        private static final long serialVersionUID = 299630820L;

    /**
     * Constructor
     */
      public DataElementMappingComponent() {
        super();
      }

    /**
     * Constructor
     */
      public DataElementMappingComponent(IdType identity) {
        super();
        this.identity = identity;
      }

        /**
         * @return {@link #identity} (An internal id that is used to identify this mapping set when specific mappings are made on a per-element basis.). This is the underlying object with id, value and extensions. The accessor "getIdentity" gives direct access to the value
         */
        public IdType getIdentityElement() { 
          if (this.identity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DataElementMappingComponent.identity");
            else if (Configuration.doAutoCreate())
              this.identity = new IdType(); // bb
          return this.identity;
        }

        public boolean hasIdentityElement() { 
          return this.identity != null && !this.identity.isEmpty();
        }

        public boolean hasIdentity() { 
          return this.identity != null && !this.identity.isEmpty();
        }

        /**
         * @param value {@link #identity} (An internal id that is used to identify this mapping set when specific mappings are made on a per-element basis.). This is the underlying object with id, value and extensions. The accessor "getIdentity" gives direct access to the value
         */
        public DataElementMappingComponent setIdentityElement(IdType value) { 
          this.identity = value;
          return this;
        }

        /**
         * @return An internal id that is used to identify this mapping set when specific mappings are made on a per-element basis.
         */
        public String getIdentity() { 
          return this.identity == null ? null : this.identity.getValue();
        }

        /**
         * @param value An internal id that is used to identify this mapping set when specific mappings are made on a per-element basis.
         */
        public DataElementMappingComponent setIdentity(String value) { 
            if (this.identity == null)
              this.identity = new IdType();
            this.identity.setValue(value);
          return this;
        }

        /**
         * @return {@link #uri} (An absolute URI that identifies the specification that this mapping is expressed to.). This is the underlying object with id, value and extensions. The accessor "getUri" gives direct access to the value
         */
        public UriType getUriElement() { 
          if (this.uri == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DataElementMappingComponent.uri");
            else if (Configuration.doAutoCreate())
              this.uri = new UriType(); // bb
          return this.uri;
        }

        public boolean hasUriElement() { 
          return this.uri != null && !this.uri.isEmpty();
        }

        public boolean hasUri() { 
          return this.uri != null && !this.uri.isEmpty();
        }

        /**
         * @param value {@link #uri} (An absolute URI that identifies the specification that this mapping is expressed to.). This is the underlying object with id, value and extensions. The accessor "getUri" gives direct access to the value
         */
        public DataElementMappingComponent setUriElement(UriType value) { 
          this.uri = value;
          return this;
        }

        /**
         * @return An absolute URI that identifies the specification that this mapping is expressed to.
         */
        public String getUri() { 
          return this.uri == null ? null : this.uri.getValue();
        }

        /**
         * @param value An absolute URI that identifies the specification that this mapping is expressed to.
         */
        public DataElementMappingComponent setUri(String value) { 
          if (Utilities.noString(value))
            this.uri = null;
          else {
            if (this.uri == null)
              this.uri = new UriType();
            this.uri.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #name} (A name for the specification that is being mapped to.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DataElementMappingComponent.name");
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
         * @param value {@link #name} (A name for the specification that is being mapped to.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public DataElementMappingComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return A name for the specification that is being mapped to.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value A name for the specification that is being mapped to.
         */
        public DataElementMappingComponent setName(String value) { 
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
         * @return {@link #comments} (Comments about this mapping, including version notes, issues, scope limitations, and other important notes for usage.). This is the underlying object with id, value and extensions. The accessor "getComments" gives direct access to the value
         */
        public StringType getCommentsElement() { 
          if (this.comments == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DataElementMappingComponent.comments");
            else if (Configuration.doAutoCreate())
              this.comments = new StringType(); // bb
          return this.comments;
        }

        public boolean hasCommentsElement() { 
          return this.comments != null && !this.comments.isEmpty();
        }

        public boolean hasComments() { 
          return this.comments != null && !this.comments.isEmpty();
        }

        /**
         * @param value {@link #comments} (Comments about this mapping, including version notes, issues, scope limitations, and other important notes for usage.). This is the underlying object with id, value and extensions. The accessor "getComments" gives direct access to the value
         */
        public DataElementMappingComponent setCommentsElement(StringType value) { 
          this.comments = value;
          return this;
        }

        /**
         * @return Comments about this mapping, including version notes, issues, scope limitations, and other important notes for usage.
         */
        public String getComments() { 
          return this.comments == null ? null : this.comments.getValue();
        }

        /**
         * @param value Comments about this mapping, including version notes, issues, scope limitations, and other important notes for usage.
         */
        public DataElementMappingComponent setComments(String value) { 
          if (Utilities.noString(value))
            this.comments = null;
          else {
            if (this.comments == null)
              this.comments = new StringType();
            this.comments.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("identity", "id", "An internal id that is used to identify this mapping set when specific mappings are made on a per-element basis.", 0, java.lang.Integer.MAX_VALUE, identity));
          childrenList.add(new Property("uri", "uri", "An absolute URI that identifies the specification that this mapping is expressed to.", 0, java.lang.Integer.MAX_VALUE, uri));
          childrenList.add(new Property("name", "string", "A name for the specification that is being mapped to.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("comments", "string", "Comments about this mapping, including version notes, issues, scope limitations, and other important notes for usage.", 0, java.lang.Integer.MAX_VALUE, comments));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identity"))
          this.identity = castToId(value); // IdType
        else if (name.equals("uri"))
          this.uri = castToUri(value); // UriType
        else if (name.equals("name"))
          this.name = castToString(value); // StringType
        else if (name.equals("comments"))
          this.comments = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identity")) {
          throw new FHIRException("Cannot call addChild on a primitive type DataElement.identity");
        }
        else if (name.equals("uri")) {
          throw new FHIRException("Cannot call addChild on a primitive type DataElement.uri");
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type DataElement.name");
        }
        else if (name.equals("comments")) {
          throw new FHIRException("Cannot call addChild on a primitive type DataElement.comments");
        }
        else
          return super.addChild(name);
      }

      public DataElementMappingComponent copy() {
        DataElementMappingComponent dst = new DataElementMappingComponent();
        copyValues(dst);
        dst.identity = identity == null ? null : identity.copy();
        dst.uri = uri == null ? null : uri.copy();
        dst.name = name == null ? null : name.copy();
        dst.comments = comments == null ? null : comments.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof DataElementMappingComponent))
          return false;
        DataElementMappingComponent o = (DataElementMappingComponent) other;
        return compareDeep(identity, o.identity, true) && compareDeep(uri, o.uri, true) && compareDeep(name, o.name, true)
           && compareDeep(comments, o.comments, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DataElementMappingComponent))
          return false;
        DataElementMappingComponent o = (DataElementMappingComponent) other;
        return compareValues(identity, o.identity, true) && compareValues(uri, o.uri, true) && compareValues(name, o.name, true)
           && compareValues(comments, o.comments, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identity == null || identity.isEmpty()) && (uri == null || uri.isEmpty())
           && (name == null || name.isEmpty()) && (comments == null || comments.isEmpty());
      }

  public String fhirType() {
    return "DataElement.mapping";

  }

  }

    /**
     * An absolute URL that is used to identify this data element when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this data element is (or will be) published.
     */
    @Child(name = "url", type = {UriType.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Globally unique logical id for data element", formalDefinition="An absolute URL that is used to identify this data element when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this data element is (or will be) published." )
    protected UriType url;

    /**
     * Formal identifier that is used to identify this data element when it is represented in other formats, or referenced in a specification, model, design or an instance.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Logical id to reference this data element", formalDefinition="Formal identifier that is used to identify this data element when it is represented in other formats, or referenced in a specification, model, design or an instance." )
    protected List<Identifier> identifier;

    /**
     * The identifier that is used to identify this version of the data element when it is referenced in a StructureDefinition, Questionnaire or instance. This is an arbitrary value managed by the definition author manually.
     */
    @Child(name = "version", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Logical id for this version of the data element", formalDefinition="The identifier that is used to identify this version of the data element when it is referenced in a StructureDefinition, Questionnaire or instance. This is an arbitrary value managed by the definition author manually." )
    protected StringType version;

    /**
     * The term used by humans to refer to the data element.  Should ideally be unique within the context in which the data element is expected to be used.
     */
    @Child(name = "name", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Descriptive label for this element definition", formalDefinition="The term used by humans to refer to the data element.  Should ideally be unique within the context in which the data element is expected to be used." )
    protected StringType name;

    /**
     * The status of the data element.
     */
    @Child(name = "status", type = {CodeType.class}, order=4, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="draft | active | retired", formalDefinition="The status of the data element." )
    protected Enumeration<ConformanceResourceStatus> status;

    /**
     * A flag to indicate that this search data element definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    @Child(name = "experimental", type = {BooleanType.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="If for testing purposes, not real usage", formalDefinition="A flag to indicate that this search data element definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage." )
    protected BooleanType experimental;

    /**
     * The name of the individual or organization that published the data element.
     */
    @Child(name = "publisher", type = {StringType.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Name of the publisher (Organization or individual)", formalDefinition="The name of the individual or organization that published the data element." )
    protected StringType publisher;

    /**
     * Contacts to assist a user in finding and communicating with the publisher.
     */
    @Child(name = "contact", type = {}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Contact details of the publisher", formalDefinition="Contacts to assist a user in finding and communicating with the publisher." )
    protected List<DataElementContactComponent> contact;

    /**
     * The date this version of the data element was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the data element  changes.
     */
    @Child(name = "date", type = {DateTimeType.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Date for this version of the data element", formalDefinition="The date this version of the data element was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the data element  changes." )
    protected DateTimeType date;

    /**
     * The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of data element definitions.
     */
    @Child(name = "useContext", type = {CodeableConcept.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Content intends to support these contexts", formalDefinition="The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of data element definitions." )
    protected List<CodeableConcept> useContext;

    /**
     * A copyright statement relating to the definition of the data element. Copyright statements are generally legal restrictions on the use and publishing of the details of the definition of the data element.
     */
    @Child(name = "copyright", type = {StringType.class}, order=10, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Use and/or publishing restrictions", formalDefinition="A copyright statement relating to the definition of the data element. Copyright statements are generally legal restrictions on the use and publishing of the details of the definition of the data element." )
    protected StringType copyright;

    /**
     * Identifies how precise the data element is in its definition.
     */
    @Child(name = "stringency", type = {CodeType.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="comparable | fully-specified | equivalent | convertable | scaleable | flexible", formalDefinition="Identifies how precise the data element is in its definition." )
    protected Enumeration<DataElementStringency> stringency;

    /**
     * Identifies a specification (other than a terminology) that the elements which make up the DataElement have some correspondence with.
     */
    @Child(name = "mapping", type = {}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="External specification mapped to", formalDefinition="Identifies a specification (other than a terminology) that the elements which make up the DataElement have some correspondence with." )
    protected List<DataElementMappingComponent> mapping;

    /**
     * Defines the structure, type, allowed values and other constraining characteristics of the data element.
     */
    @Child(name = "element", type = {ElementDefinition.class}, order=13, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Definition of element", formalDefinition="Defines the structure, type, allowed values and other constraining characteristics of the data element." )
    protected List<ElementDefinition> element;

    private static final long serialVersionUID = 2017352331L;

  /**
   * Constructor
   */
    public DataElement() {
      super();
    }

  /**
   * Constructor
   */
    public DataElement(Enumeration<ConformanceResourceStatus> status) {
      super();
      this.status = status;
    }

    /**
     * @return {@link #url} (An absolute URL that is used to identify this data element when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this data element is (or will be) published.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public UriType getUrlElement() { 
      if (this.url == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DataElement.url");
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
     * @param value {@link #url} (An absolute URL that is used to identify this data element when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this data element is (or will be) published.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public DataElement setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return An absolute URL that is used to identify this data element when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this data element is (or will be) published.
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value An absolute URL that is used to identify this data element when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this data element is (or will be) published.
     */
    public DataElement setUrl(String value) { 
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
     * @return {@link #identifier} (Formal identifier that is used to identify this data element when it is represented in other formats, or referenced in a specification, model, design or an instance.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      if (this.identifier == null)
        return false;
      for (Identifier item : this.identifier)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #identifier} (Formal identifier that is used to identify this data element when it is represented in other formats, or referenced in a specification, model, design or an instance.)
     */
    // syntactic sugar
    public Identifier addIdentifier() { //3
      Identifier t = new Identifier();
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return t;
    }

    // syntactic sugar
    public DataElement addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #version} (The identifier that is used to identify this version of the data element when it is referenced in a StructureDefinition, Questionnaire or instance. This is an arbitrary value managed by the definition author manually.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public StringType getVersionElement() { 
      if (this.version == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DataElement.version");
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
     * @param value {@link #version} (The identifier that is used to identify this version of the data element when it is referenced in a StructureDefinition, Questionnaire or instance. This is an arbitrary value managed by the definition author manually.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public DataElement setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return The identifier that is used to identify this version of the data element when it is referenced in a StructureDefinition, Questionnaire or instance. This is an arbitrary value managed by the definition author manually.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value The identifier that is used to identify this version of the data element when it is referenced in a StructureDefinition, Questionnaire or instance. This is an arbitrary value managed by the definition author manually.
     */
    public DataElement setVersion(String value) { 
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
     * @return {@link #name} (The term used by humans to refer to the data element.  Should ideally be unique within the context in which the data element is expected to be used.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DataElement.name");
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
     * @param value {@link #name} (The term used by humans to refer to the data element.  Should ideally be unique within the context in which the data element is expected to be used.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public DataElement setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return The term used by humans to refer to the data element.  Should ideally be unique within the context in which the data element is expected to be used.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value The term used by humans to refer to the data element.  Should ideally be unique within the context in which the data element is expected to be used.
     */
    public DataElement setName(String value) { 
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
     * @return {@link #status} (The status of the data element.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<ConformanceResourceStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DataElement.status");
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
     * @param value {@link #status} (The status of the data element.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public DataElement setStatusElement(Enumeration<ConformanceResourceStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the data element.
     */
    public ConformanceResourceStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the data element.
     */
    public DataElement setStatus(ConformanceResourceStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<ConformanceResourceStatus>(new ConformanceResourceStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #experimental} (A flag to indicate that this search data element definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public BooleanType getExperimentalElement() { 
      if (this.experimental == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DataElement.experimental");
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
     * @param value {@link #experimental} (A flag to indicate that this search data element definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public DataElement setExperimentalElement(BooleanType value) { 
      this.experimental = value;
      return this;
    }

    /**
     * @return A flag to indicate that this search data element definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public boolean getExperimental() { 
      return this.experimental == null || this.experimental.isEmpty() ? false : this.experimental.getValue();
    }

    /**
     * @param value A flag to indicate that this search data element definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public DataElement setExperimental(boolean value) { 
        if (this.experimental == null)
          this.experimental = new BooleanType();
        this.experimental.setValue(value);
      return this;
    }

    /**
     * @return {@link #publisher} (The name of the individual or organization that published the data element.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public StringType getPublisherElement() { 
      if (this.publisher == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DataElement.publisher");
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
     * @param value {@link #publisher} (The name of the individual or organization that published the data element.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public DataElement setPublisherElement(StringType value) { 
      this.publisher = value;
      return this;
    }

    /**
     * @return The name of the individual or organization that published the data element.
     */
    public String getPublisher() { 
      return this.publisher == null ? null : this.publisher.getValue();
    }

    /**
     * @param value The name of the individual or organization that published the data element.
     */
    public DataElement setPublisher(String value) { 
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
    public List<DataElementContactComponent> getContact() { 
      if (this.contact == null)
        this.contact = new ArrayList<DataElementContactComponent>();
      return this.contact;
    }

    public boolean hasContact() { 
      if (this.contact == null)
        return false;
      for (DataElementContactComponent item : this.contact)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #contact} (Contacts to assist a user in finding and communicating with the publisher.)
     */
    // syntactic sugar
    public DataElementContactComponent addContact() { //3
      DataElementContactComponent t = new DataElementContactComponent();
      if (this.contact == null)
        this.contact = new ArrayList<DataElementContactComponent>();
      this.contact.add(t);
      return t;
    }

    // syntactic sugar
    public DataElement addContact(DataElementContactComponent t) { //3
      if (t == null)
        return this;
      if (this.contact == null)
        this.contact = new ArrayList<DataElementContactComponent>();
      this.contact.add(t);
      return this;
    }

    /**
     * @return {@link #date} (The date this version of the data element was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the data element  changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DataElement.date");
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
     * @param value {@link #date} (The date this version of the data element was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the data element  changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DataElement setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date this version of the data element was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the data element  changes.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date this version of the data element was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the data element  changes.
     */
    public DataElement setDate(Date value) { 
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
     * @return {@link #useContext} (The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of data element definitions.)
     */
    public List<CodeableConcept> getUseContext() { 
      if (this.useContext == null)
        this.useContext = new ArrayList<CodeableConcept>();
      return this.useContext;
    }

    public boolean hasUseContext() { 
      if (this.useContext == null)
        return false;
      for (CodeableConcept item : this.useContext)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #useContext} (The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of data element definitions.)
     */
    // syntactic sugar
    public CodeableConcept addUseContext() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.useContext == null)
        this.useContext = new ArrayList<CodeableConcept>();
      this.useContext.add(t);
      return t;
    }

    // syntactic sugar
    public DataElement addUseContext(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.useContext == null)
        this.useContext = new ArrayList<CodeableConcept>();
      this.useContext.add(t);
      return this;
    }

    /**
     * @return {@link #copyright} (A copyright statement relating to the definition of the data element. Copyright statements are generally legal restrictions on the use and publishing of the details of the definition of the data element.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public StringType getCopyrightElement() { 
      if (this.copyright == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DataElement.copyright");
        else if (Configuration.doAutoCreate())
          this.copyright = new StringType(); // bb
      return this.copyright;
    }

    public boolean hasCopyrightElement() { 
      return this.copyright != null && !this.copyright.isEmpty();
    }

    public boolean hasCopyright() { 
      return this.copyright != null && !this.copyright.isEmpty();
    }

    /**
     * @param value {@link #copyright} (A copyright statement relating to the definition of the data element. Copyright statements are generally legal restrictions on the use and publishing of the details of the definition of the data element.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public DataElement setCopyrightElement(StringType value) { 
      this.copyright = value;
      return this;
    }

    /**
     * @return A copyright statement relating to the definition of the data element. Copyright statements are generally legal restrictions on the use and publishing of the details of the definition of the data element.
     */
    public String getCopyright() { 
      return this.copyright == null ? null : this.copyright.getValue();
    }

    /**
     * @param value A copyright statement relating to the definition of the data element. Copyright statements are generally legal restrictions on the use and publishing of the details of the definition of the data element.
     */
    public DataElement setCopyright(String value) { 
      if (Utilities.noString(value))
        this.copyright = null;
      else {
        if (this.copyright == null)
          this.copyright = new StringType();
        this.copyright.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #stringency} (Identifies how precise the data element is in its definition.). This is the underlying object with id, value and extensions. The accessor "getStringency" gives direct access to the value
     */
    public Enumeration<DataElementStringency> getStringencyElement() { 
      if (this.stringency == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DataElement.stringency");
        else if (Configuration.doAutoCreate())
          this.stringency = new Enumeration<DataElementStringency>(new DataElementStringencyEnumFactory()); // bb
      return this.stringency;
    }

    public boolean hasStringencyElement() { 
      return this.stringency != null && !this.stringency.isEmpty();
    }

    public boolean hasStringency() { 
      return this.stringency != null && !this.stringency.isEmpty();
    }

    /**
     * @param value {@link #stringency} (Identifies how precise the data element is in its definition.). This is the underlying object with id, value and extensions. The accessor "getStringency" gives direct access to the value
     */
    public DataElement setStringencyElement(Enumeration<DataElementStringency> value) { 
      this.stringency = value;
      return this;
    }

    /**
     * @return Identifies how precise the data element is in its definition.
     */
    public DataElementStringency getStringency() { 
      return this.stringency == null ? null : this.stringency.getValue();
    }

    /**
     * @param value Identifies how precise the data element is in its definition.
     */
    public DataElement setStringency(DataElementStringency value) { 
      if (value == null)
        this.stringency = null;
      else {
        if (this.stringency == null)
          this.stringency = new Enumeration<DataElementStringency>(new DataElementStringencyEnumFactory());
        this.stringency.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #mapping} (Identifies a specification (other than a terminology) that the elements which make up the DataElement have some correspondence with.)
     */
    public List<DataElementMappingComponent> getMapping() { 
      if (this.mapping == null)
        this.mapping = new ArrayList<DataElementMappingComponent>();
      return this.mapping;
    }

    public boolean hasMapping() { 
      if (this.mapping == null)
        return false;
      for (DataElementMappingComponent item : this.mapping)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #mapping} (Identifies a specification (other than a terminology) that the elements which make up the DataElement have some correspondence with.)
     */
    // syntactic sugar
    public DataElementMappingComponent addMapping() { //3
      DataElementMappingComponent t = new DataElementMappingComponent();
      if (this.mapping == null)
        this.mapping = new ArrayList<DataElementMappingComponent>();
      this.mapping.add(t);
      return t;
    }

    // syntactic sugar
    public DataElement addMapping(DataElementMappingComponent t) { //3
      if (t == null)
        return this;
      if (this.mapping == null)
        this.mapping = new ArrayList<DataElementMappingComponent>();
      this.mapping.add(t);
      return this;
    }

    /**
     * @return {@link #element} (Defines the structure, type, allowed values and other constraining characteristics of the data element.)
     */
    public List<ElementDefinition> getElement() { 
      if (this.element == null)
        this.element = new ArrayList<ElementDefinition>();
      return this.element;
    }

    public boolean hasElement() { 
      if (this.element == null)
        return false;
      for (ElementDefinition item : this.element)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #element} (Defines the structure, type, allowed values and other constraining characteristics of the data element.)
     */
    // syntactic sugar
    public ElementDefinition addElement() { //3
      ElementDefinition t = new ElementDefinition();
      if (this.element == null)
        this.element = new ArrayList<ElementDefinition>();
      this.element.add(t);
      return t;
    }

    // syntactic sugar
    public DataElement addElement(ElementDefinition t) { //3
      if (t == null)
        return this;
      if (this.element == null)
        this.element = new ArrayList<ElementDefinition>();
      this.element.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("url", "uri", "An absolute URL that is used to identify this data element when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this data element is (or will be) published.", 0, java.lang.Integer.MAX_VALUE, url));
        childrenList.add(new Property("identifier", "Identifier", "Formal identifier that is used to identify this data element when it is represented in other formats, or referenced in a specification, model, design or an instance.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("version", "string", "The identifier that is used to identify this version of the data element when it is referenced in a StructureDefinition, Questionnaire or instance. This is an arbitrary value managed by the definition author manually.", 0, java.lang.Integer.MAX_VALUE, version));
        childrenList.add(new Property("name", "string", "The term used by humans to refer to the data element.  Should ideally be unique within the context in which the data element is expected to be used.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("status", "code", "The status of the data element.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("experimental", "boolean", "A flag to indicate that this search data element definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.", 0, java.lang.Integer.MAX_VALUE, experimental));
        childrenList.add(new Property("publisher", "string", "The name of the individual or organization that published the data element.", 0, java.lang.Integer.MAX_VALUE, publisher));
        childrenList.add(new Property("contact", "", "Contacts to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact));
        childrenList.add(new Property("date", "dateTime", "The date this version of the data element was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the data element  changes.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("useContext", "CodeableConcept", "The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of data element definitions.", 0, java.lang.Integer.MAX_VALUE, useContext));
        childrenList.add(new Property("copyright", "string", "A copyright statement relating to the definition of the data element. Copyright statements are generally legal restrictions on the use and publishing of the details of the definition of the data element.", 0, java.lang.Integer.MAX_VALUE, copyright));
        childrenList.add(new Property("stringency", "code", "Identifies how precise the data element is in its definition.", 0, java.lang.Integer.MAX_VALUE, stringency));
        childrenList.add(new Property("mapping", "", "Identifies a specification (other than a terminology) that the elements which make up the DataElement have some correspondence with.", 0, java.lang.Integer.MAX_VALUE, mapping));
        childrenList.add(new Property("element", "ElementDefinition", "Defines the structure, type, allowed values and other constraining characteristics of the data element.", 0, java.lang.Integer.MAX_VALUE, element));
      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("url"))
          this.url = castToUri(value); // UriType
        else if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
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
          this.getContact().add((DataElementContactComponent) value);
        else if (name.equals("date"))
          this.date = castToDateTime(value); // DateTimeType
        else if (name.equals("useContext"))
          this.getUseContext().add(castToCodeableConcept(value));
        else if (name.equals("copyright"))
          this.copyright = castToString(value); // StringType
        else if (name.equals("stringency"))
          this.stringency = new DataElementStringencyEnumFactory().fromType(value); // Enumeration<DataElementStringency>
        else if (name.equals("mapping"))
          this.getMapping().add((DataElementMappingComponent) value);
        else if (name.equals("element"))
          this.getElement().add(castToElementDefinition(value));
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type DataElement.url");
        }
        else if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type DataElement.version");
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type DataElement.name");
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type DataElement.status");
        }
        else if (name.equals("experimental")) {
          throw new FHIRException("Cannot call addChild on a primitive type DataElement.experimental");
        }
        else if (name.equals("publisher")) {
          throw new FHIRException("Cannot call addChild on a primitive type DataElement.publisher");
        }
        else if (name.equals("contact")) {
          return addContact();
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type DataElement.date");
        }
        else if (name.equals("useContext")) {
          return addUseContext();
        }
        else if (name.equals("copyright")) {
          throw new FHIRException("Cannot call addChild on a primitive type DataElement.copyright");
        }
        else if (name.equals("stringency")) {
          throw new FHIRException("Cannot call addChild on a primitive type DataElement.stringency");
        }
        else if (name.equals("mapping")) {
          return addMapping();
        }
        else if (name.equals("element")) {
          return addElement();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "DataElement";

  }

      public DataElement copy() {
        DataElement dst = new DataElement();
        copyValues(dst);
        dst.url = url == null ? null : url.copy();
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.version = version == null ? null : version.copy();
        dst.name = name == null ? null : name.copy();
        dst.status = status == null ? null : status.copy();
        dst.experimental = experimental == null ? null : experimental.copy();
        dst.publisher = publisher == null ? null : publisher.copy();
        if (contact != null) {
          dst.contact = new ArrayList<DataElementContactComponent>();
          for (DataElementContactComponent i : contact)
            dst.contact.add(i.copy());
        };
        dst.date = date == null ? null : date.copy();
        if (useContext != null) {
          dst.useContext = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : useContext)
            dst.useContext.add(i.copy());
        };
        dst.copyright = copyright == null ? null : copyright.copy();
        dst.stringency = stringency == null ? null : stringency.copy();
        if (mapping != null) {
          dst.mapping = new ArrayList<DataElementMappingComponent>();
          for (DataElementMappingComponent i : mapping)
            dst.mapping.add(i.copy());
        };
        if (element != null) {
          dst.element = new ArrayList<ElementDefinition>();
          for (ElementDefinition i : element)
            dst.element.add(i.copy());
        };
        return dst;
      }

      protected DataElement typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof DataElement))
          return false;
        DataElement o = (DataElement) other;
        return compareDeep(url, o.url, true) && compareDeep(identifier, o.identifier, true) && compareDeep(version, o.version, true)
           && compareDeep(name, o.name, true) && compareDeep(status, o.status, true) && compareDeep(experimental, o.experimental, true)
           && compareDeep(publisher, o.publisher, true) && compareDeep(contact, o.contact, true) && compareDeep(date, o.date, true)
           && compareDeep(useContext, o.useContext, true) && compareDeep(copyright, o.copyright, true) && compareDeep(stringency, o.stringency, true)
           && compareDeep(mapping, o.mapping, true) && compareDeep(element, o.element, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DataElement))
          return false;
        DataElement o = (DataElement) other;
        return compareValues(url, o.url, true) && compareValues(version, o.version, true) && compareValues(name, o.name, true)
           && compareValues(status, o.status, true) && compareValues(experimental, o.experimental, true) && compareValues(publisher, o.publisher, true)
           && compareValues(date, o.date, true) && compareValues(copyright, o.copyright, true) && compareValues(stringency, o.stringency, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (url == null || url.isEmpty()) && (identifier == null || identifier.isEmpty())
           && (version == null || version.isEmpty()) && (name == null || name.isEmpty()) && (status == null || status.isEmpty())
           && (experimental == null || experimental.isEmpty()) && (publisher == null || publisher.isEmpty())
           && (contact == null || contact.isEmpty()) && (date == null || date.isEmpty()) && (useContext == null || useContext.isEmpty())
           && (copyright == null || copyright.isEmpty()) && (stringency == null || stringency.isEmpty())
           && (mapping == null || mapping.isEmpty()) && (element == null || element.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.DataElement;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>The data element publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>DataElement.date</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="DataElement.date", description="The data element publication date", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>The data element publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>DataElement.date</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>The identifier of the data element</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DataElement.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="DataElement.identifier", description="The identifier of the data element", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>The identifier of the data element</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DataElement.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>code</b>
   * <p>
   * Description: <b>A code for the data element (server may choose to do subsumption)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DataElement.element.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="code", path="DataElement.element.code", description="A code for the data element (server may choose to do subsumption)", type="token" )
  public static final String SP_CODE = "code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>code</b>
   * <p>
   * Description: <b>A code for the data element (server may choose to do subsumption)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DataElement.element.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CODE);

 /**
   * Search parameter: <b>stringency</b>
   * <p>
   * Description: <b>The stringency of the data element definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DataElement.stringency</b><br>
   * </p>
   */
  @SearchParamDefinition(name="stringency", path="DataElement.stringency", description="The stringency of the data element definition", type="token" )
  public static final String SP_STRINGENCY = "stringency";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>stringency</b>
   * <p>
   * Description: <b>The stringency of the data element definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DataElement.stringency</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STRINGENCY = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STRINGENCY);

 /**
   * Search parameter: <b>name</b>
   * <p>
   * Description: <b>Name of the data element</b><br>
   * Type: <b>string</b><br>
   * Path: <b>DataElement.name</b><br>
   * </p>
   */
  @SearchParamDefinition(name="name", path="DataElement.name", description="Name of the data element", type="string" )
  public static final String SP_NAME = "name";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>name</b>
   * <p>
   * Description: <b>Name of the data element</b><br>
   * Type: <b>string</b><br>
   * Path: <b>DataElement.name</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam NAME = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_NAME);

 /**
   * Search parameter: <b>context</b>
   * <p>
   * Description: <b>A use context assigned to the data element</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DataElement.useContext</b><br>
   * </p>
   */
  @SearchParamDefinition(name="context", path="DataElement.useContext", description="A use context assigned to the data element", type="token" )
  public static final String SP_CONTEXT = "context";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context</b>
   * <p>
   * Description: <b>A use context assigned to the data element</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DataElement.useContext</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CONTEXT = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CONTEXT);

 /**
   * Search parameter: <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the data element</b><br>
   * Type: <b>string</b><br>
   * Path: <b>DataElement.publisher</b><br>
   * </p>
   */
  @SearchParamDefinition(name="publisher", path="DataElement.publisher", description="Name of the publisher of the data element", type="string" )
  public static final String SP_PUBLISHER = "publisher";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the data element</b><br>
   * Type: <b>string</b><br>
   * Path: <b>DataElement.publisher</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam PUBLISHER = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_PUBLISHER);

 /**
   * Search parameter: <b>description</b>
   * <p>
   * Description: <b>Text search in the description of the data element.  This corresponds to the definition of the first DataElement.element.</b><br>
   * Type: <b>string</b><br>
   * Path: <b>DataElement.element.definition</b><br>
   * </p>
   */
  @SearchParamDefinition(name="description", path="DataElement.element.definition", description="Text search in the description of the data element.  This corresponds to the definition of the first DataElement.element.", type="string" )
  public static final String SP_DESCRIPTION = "description";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>description</b>
   * <p>
   * Description: <b>Text search in the description of the data element.  This corresponds to the definition of the first DataElement.element.</b><br>
   * Type: <b>string</b><br>
   * Path: <b>DataElement.element.definition</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam DESCRIPTION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_DESCRIPTION);

 /**
   * Search parameter: <b>version</b>
   * <p>
   * Description: <b>The version identifier of the data element</b><br>
   * Type: <b>string</b><br>
   * Path: <b>DataElement.version</b><br>
   * </p>
   */
  @SearchParamDefinition(name="version", path="DataElement.version", description="The version identifier of the data element", type="string" )
  public static final String SP_VERSION = "version";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>version</b>
   * <p>
   * Description: <b>The version identifier of the data element</b><br>
   * Type: <b>string</b><br>
   * Path: <b>DataElement.version</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam VERSION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_VERSION);

 /**
   * Search parameter: <b>url</b>
   * <p>
   * Description: <b>The official URL for the data element</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>DataElement.url</b><br>
   * </p>
   */
  @SearchParamDefinition(name="url", path="DataElement.url", description="The official URL for the data element", type="uri" )
  public static final String SP_URL = "url";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>url</b>
   * <p>
   * Description: <b>The official URL for the data element</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>DataElement.url</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam URL = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_URL);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The current status of the data element</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DataElement.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="DataElement.status", description="The current status of the data element", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The current status of the data element</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DataElement.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

