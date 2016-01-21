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
 * A curated namespace that issues unique symbols within that namespace for the identification of concepts, people, devices, etc.  Represents a "System" used within the Identifier and Coding data types.
 */
@ResourceDef(name="NamingSystem", profile="http://hl7.org/fhir/Profile/NamingSystem")
public class NamingSystem extends DomainResource {

    public enum NamingSystemType {
        /**
         * The naming system is used to define concepts and symbols to represent those concepts; e.g. UCUM, LOINC, NDC code, local lab codes, etc.
         */
        CODESYSTEM, 
        /**
         * The naming system is used to manage identifiers (e.g. license numbers, order numbers, etc.).
         */
        IDENTIFIER, 
        /**
         * The naming system is used as the root for other identifiers and naming systems.
         */
        ROOT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static NamingSystemType fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("codesystem".equals(codeString))
          return CODESYSTEM;
        if ("identifier".equals(codeString))
          return IDENTIFIER;
        if ("root".equals(codeString))
          return ROOT;
        throw new Exception("Unknown NamingSystemType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CODESYSTEM: return "codesystem";
            case IDENTIFIER: return "identifier";
            case ROOT: return "root";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case CODESYSTEM: return "http://hl7.org/fhir/namingsystem-type";
            case IDENTIFIER: return "http://hl7.org/fhir/namingsystem-type";
            case ROOT: return "http://hl7.org/fhir/namingsystem-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case CODESYSTEM: return "The naming system is used to define concepts and symbols to represent those concepts; e.g. UCUM, LOINC, NDC code, local lab codes, etc.";
            case IDENTIFIER: return "The naming system is used to manage identifiers (e.g. license numbers, order numbers, etc.).";
            case ROOT: return "The naming system is used as the root for other identifiers and naming systems.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CODESYSTEM: return "Code System";
            case IDENTIFIER: return "Identifier";
            case ROOT: return "Root";
            default: return "?";
          }
        }
    }

  public static class NamingSystemTypeEnumFactory implements EnumFactory<NamingSystemType> {
    public NamingSystemType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("codesystem".equals(codeString))
          return NamingSystemType.CODESYSTEM;
        if ("identifier".equals(codeString))
          return NamingSystemType.IDENTIFIER;
        if ("root".equals(codeString))
          return NamingSystemType.ROOT;
        throw new IllegalArgumentException("Unknown NamingSystemType code '"+codeString+"'");
        }
    public String toCode(NamingSystemType code) {
      if (code == NamingSystemType.CODESYSTEM)
        return "codesystem";
      if (code == NamingSystemType.IDENTIFIER)
        return "identifier";
      if (code == NamingSystemType.ROOT)
        return "root";
      return "?";
      }
    }

    public enum NamingSystemIdentifierType {
        /**
         * An ISO object identifier; e.g. 1.2.3.4.5.
         */
        OID, 
        /**
         * A universally unique identifier of the form a5afddf4-e880-459b-876e-e4591b0acc11.
         */
        UUID, 
        /**
         * A uniform resource identifier (ideally a URL - uniform resource locator); e.g. http://unitsofmeasure.org.
         */
        URI, 
        /**
         * Some other type of unique identifier; e.g. HL7-assigned reserved string such as LN for LOINC.
         */
        OTHER, 
        /**
         * added to help the parsers
         */
        NULL;
        public static NamingSystemIdentifierType fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("oid".equals(codeString))
          return OID;
        if ("uuid".equals(codeString))
          return UUID;
        if ("uri".equals(codeString))
          return URI;
        if ("other".equals(codeString))
          return OTHER;
        throw new Exception("Unknown NamingSystemIdentifierType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case OID: return "oid";
            case UUID: return "uuid";
            case URI: return "uri";
            case OTHER: return "other";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case OID: return "http://hl7.org/fhir/namingsystem-identifier-type";
            case UUID: return "http://hl7.org/fhir/namingsystem-identifier-type";
            case URI: return "http://hl7.org/fhir/namingsystem-identifier-type";
            case OTHER: return "http://hl7.org/fhir/namingsystem-identifier-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case OID: return "An ISO object identifier; e.g. 1.2.3.4.5.";
            case UUID: return "A universally unique identifier of the form a5afddf4-e880-459b-876e-e4591b0acc11.";
            case URI: return "A uniform resource identifier (ideally a URL - uniform resource locator); e.g. http://unitsofmeasure.org.";
            case OTHER: return "Some other type of unique identifier; e.g. HL7-assigned reserved string such as LN for LOINC.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case OID: return "OID";
            case UUID: return "UUID";
            case URI: return "URI";
            case OTHER: return "Other";
            default: return "?";
          }
        }
    }

  public static class NamingSystemIdentifierTypeEnumFactory implements EnumFactory<NamingSystemIdentifierType> {
    public NamingSystemIdentifierType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("oid".equals(codeString))
          return NamingSystemIdentifierType.OID;
        if ("uuid".equals(codeString))
          return NamingSystemIdentifierType.UUID;
        if ("uri".equals(codeString))
          return NamingSystemIdentifierType.URI;
        if ("other".equals(codeString))
          return NamingSystemIdentifierType.OTHER;
        throw new IllegalArgumentException("Unknown NamingSystemIdentifierType code '"+codeString+"'");
        }
    public String toCode(NamingSystemIdentifierType code) {
      if (code == NamingSystemIdentifierType.OID)
        return "oid";
      if (code == NamingSystemIdentifierType.UUID)
        return "uuid";
      if (code == NamingSystemIdentifierType.URI)
        return "uri";
      if (code == NamingSystemIdentifierType.OTHER)
        return "other";
      return "?";
      }
    }

    @Block()
    public static class NamingSystemContactComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The name of an individual to contact regarding the naming system.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Name of a individual to contact", formalDefinition="The name of an individual to contact regarding the naming system." )
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
      public NamingSystemContactComponent() {
        super();
      }

        /**
         * @return {@link #name} (The name of an individual to contact regarding the naming system.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NamingSystemContactComponent.name");
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
         * @param value {@link #name} (The name of an individual to contact regarding the naming system.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public NamingSystemContactComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return The name of an individual to contact regarding the naming system.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value The name of an individual to contact regarding the naming system.
         */
        public NamingSystemContactComponent setName(String value) { 
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
        public NamingSystemContactComponent addTelecom(ContactPoint t) { //3
          if (t == null)
            return this;
          if (this.telecom == null)
            this.telecom = new ArrayList<ContactPoint>();
          this.telecom.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "string", "The name of an individual to contact regarding the naming system.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("telecom", "ContactPoint", "Contact details for individual (if a name was provided) or the publisher.", 0, java.lang.Integer.MAX_VALUE, telecom));
        }

      public NamingSystemContactComponent copy() {
        NamingSystemContactComponent dst = new NamingSystemContactComponent();
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
        if (!(other instanceof NamingSystemContactComponent))
          return false;
        NamingSystemContactComponent o = (NamingSystemContactComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(telecom, o.telecom, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof NamingSystemContactComponent))
          return false;
        NamingSystemContactComponent o = (NamingSystemContactComponent) other;
        return compareValues(name, o.name, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (telecom == null || telecom.isEmpty())
          ;
      }

  }

    @Block()
    public static class NamingSystemUniqueIdComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Identifies the unique identifier scheme used for this particular identifier.
         */
        @Child(name = "type", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="oid | uuid | uri | other", formalDefinition="Identifies the unique identifier scheme used for this particular identifier." )
        protected Enumeration<NamingSystemIdentifierType> type;

        /**
         * The string that should be sent over the wire to identify the code system or identifier system.
         */
        @Child(name = "value", type = {StringType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The unique identifier", formalDefinition="The string that should be sent over the wire to identify the code system or identifier system." )
        protected StringType value;

        /**
         * Indicates whether this identifier is the "preferred" identifier of this type.
         */
        @Child(name = "preferred", type = {BooleanType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Is this the id that should be used for this type", formalDefinition="Indicates whether this identifier is the \"preferred\" identifier of this type." )
        protected BooleanType preferred;

        /**
         * Identifies the period of time over which this identifier is considered appropriate to refer to the naming system.  Outside of this window, the identifier might be non-deterministic.
         */
        @Child(name = "period", type = {Period.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="When is identifier valid?", formalDefinition="Identifies the period of time over which this identifier is considered appropriate to refer to the naming system.  Outside of this window, the identifier might be non-deterministic." )
        protected Period period;

        private static final long serialVersionUID = -193711840L;

    /*
     * Constructor
     */
      public NamingSystemUniqueIdComponent() {
        super();
      }

    /*
     * Constructor
     */
      public NamingSystemUniqueIdComponent(Enumeration<NamingSystemIdentifierType> type, StringType value) {
        super();
        this.type = type;
        this.value = value;
      }

        /**
         * @return {@link #type} (Identifies the unique identifier scheme used for this particular identifier.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<NamingSystemIdentifierType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NamingSystemUniqueIdComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Enumeration<NamingSystemIdentifierType>(new NamingSystemIdentifierTypeEnumFactory()); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Identifies the unique identifier scheme used for this particular identifier.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public NamingSystemUniqueIdComponent setTypeElement(Enumeration<NamingSystemIdentifierType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return Identifies the unique identifier scheme used for this particular identifier.
         */
        public NamingSystemIdentifierType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value Identifies the unique identifier scheme used for this particular identifier.
         */
        public NamingSystemUniqueIdComponent setType(NamingSystemIdentifierType value) { 
            if (this.type == null)
              this.type = new Enumeration<NamingSystemIdentifierType>(new NamingSystemIdentifierTypeEnumFactory());
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #value} (The string that should be sent over the wire to identify the code system or identifier system.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public StringType getValueElement() { 
          if (this.value == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NamingSystemUniqueIdComponent.value");
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
         * @param value {@link #value} (The string that should be sent over the wire to identify the code system or identifier system.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public NamingSystemUniqueIdComponent setValueElement(StringType value) { 
          this.value = value;
          return this;
        }

        /**
         * @return The string that should be sent over the wire to identify the code system or identifier system.
         */
        public String getValue() { 
          return this.value == null ? null : this.value.getValue();
        }

        /**
         * @param value The string that should be sent over the wire to identify the code system or identifier system.
         */
        public NamingSystemUniqueIdComponent setValue(String value) { 
            if (this.value == null)
              this.value = new StringType();
            this.value.setValue(value);
          return this;
        }

        /**
         * @return {@link #preferred} (Indicates whether this identifier is the "preferred" identifier of this type.). This is the underlying object with id, value and extensions. The accessor "getPreferred" gives direct access to the value
         */
        public BooleanType getPreferredElement() { 
          if (this.preferred == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NamingSystemUniqueIdComponent.preferred");
            else if (Configuration.doAutoCreate())
              this.preferred = new BooleanType(); // bb
          return this.preferred;
        }

        public boolean hasPreferredElement() { 
          return this.preferred != null && !this.preferred.isEmpty();
        }

        public boolean hasPreferred() { 
          return this.preferred != null && !this.preferred.isEmpty();
        }

        /**
         * @param value {@link #preferred} (Indicates whether this identifier is the "preferred" identifier of this type.). This is the underlying object with id, value and extensions. The accessor "getPreferred" gives direct access to the value
         */
        public NamingSystemUniqueIdComponent setPreferredElement(BooleanType value) { 
          this.preferred = value;
          return this;
        }

        /**
         * @return Indicates whether this identifier is the "preferred" identifier of this type.
         */
        public boolean getPreferred() { 
          return this.preferred == null || this.preferred.isEmpty() ? false : this.preferred.getValue();
        }

        /**
         * @param value Indicates whether this identifier is the "preferred" identifier of this type.
         */
        public NamingSystemUniqueIdComponent setPreferred(boolean value) { 
            if (this.preferred == null)
              this.preferred = new BooleanType();
            this.preferred.setValue(value);
          return this;
        }

        /**
         * @return {@link #period} (Identifies the period of time over which this identifier is considered appropriate to refer to the naming system.  Outside of this window, the identifier might be non-deterministic.)
         */
        public Period getPeriod() { 
          if (this.period == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NamingSystemUniqueIdComponent.period");
            else if (Configuration.doAutoCreate())
              this.period = new Period(); // cc
          return this.period;
        }

        public boolean hasPeriod() { 
          return this.period != null && !this.period.isEmpty();
        }

        /**
         * @param value {@link #period} (Identifies the period of time over which this identifier is considered appropriate to refer to the naming system.  Outside of this window, the identifier might be non-deterministic.)
         */
        public NamingSystemUniqueIdComponent setPeriod(Period value) { 
          this.period = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "code", "Identifies the unique identifier scheme used for this particular identifier.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("value", "string", "The string that should be sent over the wire to identify the code system or identifier system.", 0, java.lang.Integer.MAX_VALUE, value));
          childrenList.add(new Property("preferred", "boolean", "Indicates whether this identifier is the \"preferred\" identifier of this type.", 0, java.lang.Integer.MAX_VALUE, preferred));
          childrenList.add(new Property("period", "Period", "Identifies the period of time over which this identifier is considered appropriate to refer to the naming system.  Outside of this window, the identifier might be non-deterministic.", 0, java.lang.Integer.MAX_VALUE, period));
        }

      public NamingSystemUniqueIdComponent copy() {
        NamingSystemUniqueIdComponent dst = new NamingSystemUniqueIdComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.value = value == null ? null : value.copy();
        dst.preferred = preferred == null ? null : preferred.copy();
        dst.period = period == null ? null : period.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof NamingSystemUniqueIdComponent))
          return false;
        NamingSystemUniqueIdComponent o = (NamingSystemUniqueIdComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(value, o.value, true) && compareDeep(preferred, o.preferred, true)
           && compareDeep(period, o.period, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof NamingSystemUniqueIdComponent))
          return false;
        NamingSystemUniqueIdComponent o = (NamingSystemUniqueIdComponent) other;
        return compareValues(type, o.type, true) && compareValues(value, o.value, true) && compareValues(preferred, o.preferred, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (value == null || value.isEmpty())
           && (preferred == null || preferred.isEmpty()) && (period == null || period.isEmpty());
      }

  }

    /**
     * The descriptive name of this particular identifier type or code system.
     */
    @Child(name = "name", type = {StringType.class}, order=0, min=1, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Human-readable label", formalDefinition="The descriptive name of this particular identifier type or code system." )
    protected StringType name;

    /**
     * Indicates whether the naming system is "ready for use" or not.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=1, max=1, modifier=true, summary=false)
    @Description(shortDefinition="draft | active | retired", formalDefinition="Indicates whether the naming system is \"ready for use\" or not." )
    protected Enumeration<ConformanceResourceStatus> status;

    /**
     * Indicates the purpose for the naming system - what kinds of things does it make unique?
     */
    @Child(name = "kind", type = {CodeType.class}, order=2, min=1, max=1, modifier=false, summary=false)
    @Description(shortDefinition="codesystem | identifier | root", formalDefinition="Indicates the purpose for the naming system - what kinds of things does it make unique?" )
    protected Enumeration<NamingSystemType> kind;

    /**
     * The name of the individual or organization that published the naming system.
     */
    @Child(name = "publisher", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Name of the publisher (Organization or individual)", formalDefinition="The name of the individual or organization that published the naming system." )
    protected StringType publisher;

    /**
     * Contacts to assist a user in finding and communicating with the publisher.
     */
    @Child(name = "contact", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Contact details of the publisher", formalDefinition="Contacts to assist a user in finding and communicating with the publisher." )
    protected List<NamingSystemContactComponent> contact;

    /**
     * The name of the organization that is responsible for issuing identifiers or codes for this namespace and ensuring their non-collision.
     */
    @Child(name = "responsible", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Who maintains system namespace?", formalDefinition="The name of the organization that is responsible for issuing identifiers or codes for this namespace and ensuring their non-collision." )
    protected StringType responsible;

    /**
     * The date  (and optionally time) when the system was registered or published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the registration changes.
     */
    @Child(name = "date", type = {DateTimeType.class}, order=6, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Publication Date(/time)", formalDefinition="The date  (and optionally time) when the system was registered or published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the registration changes." )
    protected DateTimeType date;

    /**
     * Categorizes a naming system for easier search by grouping related naming systems.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=7, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="e.g. driver,  provider,  patient, bank etc.", formalDefinition="Categorizes a naming system for easier search by grouping related naming systems." )
    protected CodeableConcept type;

    /**
     * Details about what the namespace identifies including scope, granularity, version labeling, etc.
     */
    @Child(name = "description", type = {StringType.class}, order=8, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="What does naming system identify?", formalDefinition="Details about what the namespace identifies including scope, granularity, version labeling, etc." )
    protected StringType description;

    /**
     * The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of naming systems.
     */
    @Child(name = "useContext", type = {CodeableConcept.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Content intends to support these contexts", formalDefinition="The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of naming systems." )
    protected List<CodeableConcept> useContext;

    /**
     * Provides guidance on the use of the namespace, including the handling of formatting characters, use of upper vs. lower case, etc.
     */
    @Child(name = "usage", type = {StringType.class}, order=10, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="How/where is it used", formalDefinition="Provides guidance on the use of the namespace, including the handling of formatting characters, use of upper vs. lower case, etc." )
    protected StringType usage;

    /**
     * Indicates how the system may be identified when referenced in electronic exchange.
     */
    @Child(name = "uniqueId", type = {}, order=11, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Unique identifiers used for system", formalDefinition="Indicates how the system may be identified when referenced in electronic exchange." )
    protected List<NamingSystemUniqueIdComponent> uniqueId;

    /**
     * For naming systems that are retired, indicates the naming system that should be used in their place (if any).
     */
    @Child(name = "replacedBy", type = {NamingSystem.class}, order=12, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Use this instead", formalDefinition="For naming systems that are retired, indicates the naming system that should be used in their place (if any)." )
    protected Reference replacedBy;

    /**
     * The actual object that is the target of the reference (For naming systems that are retired, indicates the naming system that should be used in their place (if any).)
     */
    protected NamingSystem replacedByTarget;

    private static final long serialVersionUID = -1337110053L;

  /*
   * Constructor
   */
    public NamingSystem() {
      super();
    }

  /*
   * Constructor
   */
    public NamingSystem(StringType name, Enumeration<ConformanceResourceStatus> status, Enumeration<NamingSystemType> kind, DateTimeType date) {
      super();
      this.name = name;
      this.status = status;
      this.kind = kind;
      this.date = date;
    }

    /**
     * @return {@link #name} (The descriptive name of this particular identifier type or code system.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create NamingSystem.name");
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
     * @param value {@link #name} (The descriptive name of this particular identifier type or code system.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public NamingSystem setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return The descriptive name of this particular identifier type or code system.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value The descriptive name of this particular identifier type or code system.
     */
    public NamingSystem setName(String value) { 
        if (this.name == null)
          this.name = new StringType();
        this.name.setValue(value);
      return this;
    }

    /**
     * @return {@link #status} (Indicates whether the naming system is "ready for use" or not.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<ConformanceResourceStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create NamingSystem.status");
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
     * @param value {@link #status} (Indicates whether the naming system is "ready for use" or not.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public NamingSystem setStatusElement(Enumeration<ConformanceResourceStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return Indicates whether the naming system is "ready for use" or not.
     */
    public ConformanceResourceStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value Indicates whether the naming system is "ready for use" or not.
     */
    public NamingSystem setStatus(ConformanceResourceStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<ConformanceResourceStatus>(new ConformanceResourceStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #kind} (Indicates the purpose for the naming system - what kinds of things does it make unique?). This is the underlying object with id, value and extensions. The accessor "getKind" gives direct access to the value
     */
    public Enumeration<NamingSystemType> getKindElement() { 
      if (this.kind == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create NamingSystem.kind");
        else if (Configuration.doAutoCreate())
          this.kind = new Enumeration<NamingSystemType>(new NamingSystemTypeEnumFactory()); // bb
      return this.kind;
    }

    public boolean hasKindElement() { 
      return this.kind != null && !this.kind.isEmpty();
    }

    public boolean hasKind() { 
      return this.kind != null && !this.kind.isEmpty();
    }

    /**
     * @param value {@link #kind} (Indicates the purpose for the naming system - what kinds of things does it make unique?). This is the underlying object with id, value and extensions. The accessor "getKind" gives direct access to the value
     */
    public NamingSystem setKindElement(Enumeration<NamingSystemType> value) { 
      this.kind = value;
      return this;
    }

    /**
     * @return Indicates the purpose for the naming system - what kinds of things does it make unique?
     */
    public NamingSystemType getKind() { 
      return this.kind == null ? null : this.kind.getValue();
    }

    /**
     * @param value Indicates the purpose for the naming system - what kinds of things does it make unique?
     */
    public NamingSystem setKind(NamingSystemType value) { 
        if (this.kind == null)
          this.kind = new Enumeration<NamingSystemType>(new NamingSystemTypeEnumFactory());
        this.kind.setValue(value);
      return this;
    }

    /**
     * @return {@link #publisher} (The name of the individual or organization that published the naming system.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public StringType getPublisherElement() { 
      if (this.publisher == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create NamingSystem.publisher");
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
     * @param value {@link #publisher} (The name of the individual or organization that published the naming system.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public NamingSystem setPublisherElement(StringType value) { 
      this.publisher = value;
      return this;
    }

    /**
     * @return The name of the individual or organization that published the naming system.
     */
    public String getPublisher() { 
      return this.publisher == null ? null : this.publisher.getValue();
    }

    /**
     * @param value The name of the individual or organization that published the naming system.
     */
    public NamingSystem setPublisher(String value) { 
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
    public List<NamingSystemContactComponent> getContact() { 
      if (this.contact == null)
        this.contact = new ArrayList<NamingSystemContactComponent>();
      return this.contact;
    }

    public boolean hasContact() { 
      if (this.contact == null)
        return false;
      for (NamingSystemContactComponent item : this.contact)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #contact} (Contacts to assist a user in finding and communicating with the publisher.)
     */
    // syntactic sugar
    public NamingSystemContactComponent addContact() { //3
      NamingSystemContactComponent t = new NamingSystemContactComponent();
      if (this.contact == null)
        this.contact = new ArrayList<NamingSystemContactComponent>();
      this.contact.add(t);
      return t;
    }

    // syntactic sugar
    public NamingSystem addContact(NamingSystemContactComponent t) { //3
      if (t == null)
        return this;
      if (this.contact == null)
        this.contact = new ArrayList<NamingSystemContactComponent>();
      this.contact.add(t);
      return this;
    }

    /**
     * @return {@link #responsible} (The name of the organization that is responsible for issuing identifiers or codes for this namespace and ensuring their non-collision.). This is the underlying object with id, value and extensions. The accessor "getResponsible" gives direct access to the value
     */
    public StringType getResponsibleElement() { 
      if (this.responsible == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create NamingSystem.responsible");
        else if (Configuration.doAutoCreate())
          this.responsible = new StringType(); // bb
      return this.responsible;
    }

    public boolean hasResponsibleElement() { 
      return this.responsible != null && !this.responsible.isEmpty();
    }

    public boolean hasResponsible() { 
      return this.responsible != null && !this.responsible.isEmpty();
    }

    /**
     * @param value {@link #responsible} (The name of the organization that is responsible for issuing identifiers or codes for this namespace and ensuring their non-collision.). This is the underlying object with id, value and extensions. The accessor "getResponsible" gives direct access to the value
     */
    public NamingSystem setResponsibleElement(StringType value) { 
      this.responsible = value;
      return this;
    }

    /**
     * @return The name of the organization that is responsible for issuing identifiers or codes for this namespace and ensuring their non-collision.
     */
    public String getResponsible() { 
      return this.responsible == null ? null : this.responsible.getValue();
    }

    /**
     * @param value The name of the organization that is responsible for issuing identifiers or codes for this namespace and ensuring their non-collision.
     */
    public NamingSystem setResponsible(String value) { 
      if (Utilities.noString(value))
        this.responsible = null;
      else {
        if (this.responsible == null)
          this.responsible = new StringType();
        this.responsible.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #date} (The date  (and optionally time) when the system was registered or published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the registration changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create NamingSystem.date");
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
     * @param value {@link #date} (The date  (and optionally time) when the system was registered or published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the registration changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public NamingSystem setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date  (and optionally time) when the system was registered or published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the registration changes.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date  (and optionally time) when the system was registered or published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the registration changes.
     */
    public NamingSystem setDate(Date value) { 
        if (this.date == null)
          this.date = new DateTimeType();
        this.date.setValue(value);
      return this;
    }

    /**
     * @return {@link #type} (Categorizes a naming system for easier search by grouping related naming systems.)
     */
    public CodeableConcept getType() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create NamingSystem.type");
        else if (Configuration.doAutoCreate())
          this.type = new CodeableConcept(); // cc
      return this.type;
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (Categorizes a naming system for easier search by grouping related naming systems.)
     */
    public NamingSystem setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #description} (Details about what the namespace identifies including scope, granularity, version labeling, etc.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create NamingSystem.description");
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
     * @param value {@link #description} (Details about what the namespace identifies including scope, granularity, version labeling, etc.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public NamingSystem setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return Details about what the namespace identifies including scope, granularity, version labeling, etc.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value Details about what the namespace identifies including scope, granularity, version labeling, etc.
     */
    public NamingSystem setDescription(String value) { 
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
     * @return {@link #useContext} (The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of naming systems.)
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
     * @return {@link #useContext} (The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of naming systems.)
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
    public NamingSystem addUseContext(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.useContext == null)
        this.useContext = new ArrayList<CodeableConcept>();
      this.useContext.add(t);
      return this;
    }

    /**
     * @return {@link #usage} (Provides guidance on the use of the namespace, including the handling of formatting characters, use of upper vs. lower case, etc.). This is the underlying object with id, value and extensions. The accessor "getUsage" gives direct access to the value
     */
    public StringType getUsageElement() { 
      if (this.usage == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create NamingSystem.usage");
        else if (Configuration.doAutoCreate())
          this.usage = new StringType(); // bb
      return this.usage;
    }

    public boolean hasUsageElement() { 
      return this.usage != null && !this.usage.isEmpty();
    }

    public boolean hasUsage() { 
      return this.usage != null && !this.usage.isEmpty();
    }

    /**
     * @param value {@link #usage} (Provides guidance on the use of the namespace, including the handling of formatting characters, use of upper vs. lower case, etc.). This is the underlying object with id, value and extensions. The accessor "getUsage" gives direct access to the value
     */
    public NamingSystem setUsageElement(StringType value) { 
      this.usage = value;
      return this;
    }

    /**
     * @return Provides guidance on the use of the namespace, including the handling of formatting characters, use of upper vs. lower case, etc.
     */
    public String getUsage() { 
      return this.usage == null ? null : this.usage.getValue();
    }

    /**
     * @param value Provides guidance on the use of the namespace, including the handling of formatting characters, use of upper vs. lower case, etc.
     */
    public NamingSystem setUsage(String value) { 
      if (Utilities.noString(value))
        this.usage = null;
      else {
        if (this.usage == null)
          this.usage = new StringType();
        this.usage.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #uniqueId} (Indicates how the system may be identified when referenced in electronic exchange.)
     */
    public List<NamingSystemUniqueIdComponent> getUniqueId() { 
      if (this.uniqueId == null)
        this.uniqueId = new ArrayList<NamingSystemUniqueIdComponent>();
      return this.uniqueId;
    }

    public boolean hasUniqueId() { 
      if (this.uniqueId == null)
        return false;
      for (NamingSystemUniqueIdComponent item : this.uniqueId)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #uniqueId} (Indicates how the system may be identified when referenced in electronic exchange.)
     */
    // syntactic sugar
    public NamingSystemUniqueIdComponent addUniqueId() { //3
      NamingSystemUniqueIdComponent t = new NamingSystemUniqueIdComponent();
      if (this.uniqueId == null)
        this.uniqueId = new ArrayList<NamingSystemUniqueIdComponent>();
      this.uniqueId.add(t);
      return t;
    }

    // syntactic sugar
    public NamingSystem addUniqueId(NamingSystemUniqueIdComponent t) { //3
      if (t == null)
        return this;
      if (this.uniqueId == null)
        this.uniqueId = new ArrayList<NamingSystemUniqueIdComponent>();
      this.uniqueId.add(t);
      return this;
    }

    /**
     * @return {@link #replacedBy} (For naming systems that are retired, indicates the naming system that should be used in their place (if any).)
     */
    public Reference getReplacedBy() { 
      if (this.replacedBy == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create NamingSystem.replacedBy");
        else if (Configuration.doAutoCreate())
          this.replacedBy = new Reference(); // cc
      return this.replacedBy;
    }

    public boolean hasReplacedBy() { 
      return this.replacedBy != null && !this.replacedBy.isEmpty();
    }

    /**
     * @param value {@link #replacedBy} (For naming systems that are retired, indicates the naming system that should be used in their place (if any).)
     */
    public NamingSystem setReplacedBy(Reference value) { 
      this.replacedBy = value;
      return this;
    }

    /**
     * @return {@link #replacedBy} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (For naming systems that are retired, indicates the naming system that should be used in their place (if any).)
     */
    public NamingSystem getReplacedByTarget() { 
      if (this.replacedByTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create NamingSystem.replacedBy");
        else if (Configuration.doAutoCreate())
          this.replacedByTarget = new NamingSystem(); // aa
      return this.replacedByTarget;
    }

    /**
     * @param value {@link #replacedBy} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (For naming systems that are retired, indicates the naming system that should be used in their place (if any).)
     */
    public NamingSystem setReplacedByTarget(NamingSystem value) { 
      this.replacedByTarget = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("name", "string", "The descriptive name of this particular identifier type or code system.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("status", "code", "Indicates whether the naming system is \"ready for use\" or not.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("kind", "code", "Indicates the purpose for the naming system - what kinds of things does it make unique?", 0, java.lang.Integer.MAX_VALUE, kind));
        childrenList.add(new Property("publisher", "string", "The name of the individual or organization that published the naming system.", 0, java.lang.Integer.MAX_VALUE, publisher));
        childrenList.add(new Property("contact", "", "Contacts to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact));
        childrenList.add(new Property("responsible", "string", "The name of the organization that is responsible for issuing identifiers or codes for this namespace and ensuring their non-collision.", 0, java.lang.Integer.MAX_VALUE, responsible));
        childrenList.add(new Property("date", "dateTime", "The date  (and optionally time) when the system was registered or published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the registration changes.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("type", "CodeableConcept", "Categorizes a naming system for easier search by grouping related naming systems.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("description", "string", "Details about what the namespace identifies including scope, granularity, version labeling, etc.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("useContext", "CodeableConcept", "The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of naming systems.", 0, java.lang.Integer.MAX_VALUE, useContext));
        childrenList.add(new Property("usage", "string", "Provides guidance on the use of the namespace, including the handling of formatting characters, use of upper vs. lower case, etc.", 0, java.lang.Integer.MAX_VALUE, usage));
        childrenList.add(new Property("uniqueId", "", "Indicates how the system may be identified when referenced in electronic exchange.", 0, java.lang.Integer.MAX_VALUE, uniqueId));
        childrenList.add(new Property("replacedBy", "Reference(NamingSystem)", "For naming systems that are retired, indicates the naming system that should be used in their place (if any).", 0, java.lang.Integer.MAX_VALUE, replacedBy));
      }

      public NamingSystem copy() {
        NamingSystem dst = new NamingSystem();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.status = status == null ? null : status.copy();
        dst.kind = kind == null ? null : kind.copy();
        dst.publisher = publisher == null ? null : publisher.copy();
        if (contact != null) {
          dst.contact = new ArrayList<NamingSystemContactComponent>();
          for (NamingSystemContactComponent i : contact)
            dst.contact.add(i.copy());
        };
        dst.responsible = responsible == null ? null : responsible.copy();
        dst.date = date == null ? null : date.copy();
        dst.type = type == null ? null : type.copy();
        dst.description = description == null ? null : description.copy();
        if (useContext != null) {
          dst.useContext = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : useContext)
            dst.useContext.add(i.copy());
        };
        dst.usage = usage == null ? null : usage.copy();
        if (uniqueId != null) {
          dst.uniqueId = new ArrayList<NamingSystemUniqueIdComponent>();
          for (NamingSystemUniqueIdComponent i : uniqueId)
            dst.uniqueId.add(i.copy());
        };
        dst.replacedBy = replacedBy == null ? null : replacedBy.copy();
        return dst;
      }

      protected NamingSystem typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof NamingSystem))
          return false;
        NamingSystem o = (NamingSystem) other;
        return compareDeep(name, o.name, true) && compareDeep(status, o.status, true) && compareDeep(kind, o.kind, true)
           && compareDeep(publisher, o.publisher, true) && compareDeep(contact, o.contact, true) && compareDeep(responsible, o.responsible, true)
           && compareDeep(date, o.date, true) && compareDeep(type, o.type, true) && compareDeep(description, o.description, true)
           && compareDeep(useContext, o.useContext, true) && compareDeep(usage, o.usage, true) && compareDeep(uniqueId, o.uniqueId, true)
           && compareDeep(replacedBy, o.replacedBy, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof NamingSystem))
          return false;
        NamingSystem o = (NamingSystem) other;
        return compareValues(name, o.name, true) && compareValues(status, o.status, true) && compareValues(kind, o.kind, true)
           && compareValues(publisher, o.publisher, true) && compareValues(responsible, o.responsible, true) && compareValues(date, o.date, true)
           && compareValues(description, o.description, true) && compareValues(usage, o.usage, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (status == null || status.isEmpty())
           && (kind == null || kind.isEmpty()) && (publisher == null || publisher.isEmpty()) && (contact == null || contact.isEmpty())
           && (responsible == null || responsible.isEmpty()) && (date == null || date.isEmpty()) && (type == null || type.isEmpty())
           && (description == null || description.isEmpty()) && (useContext == null || useContext.isEmpty())
           && (usage == null || usage.isEmpty()) && (uniqueId == null || uniqueId.isEmpty()) && (replacedBy == null || replacedBy.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.NamingSystem;
   }

  @SearchParamDefinition(name="date", path="NamingSystem.date", description="Publication Date(/time)", type="date" )
  public static final String SP_DATE = "date";
  @SearchParamDefinition(name="period", path="NamingSystem.uniqueId.period", description="When is identifier valid?", type="date" )
  public static final String SP_PERIOD = "period";
  @SearchParamDefinition(name="kind", path="NamingSystem.kind", description="codesystem | identifier | root", type="token" )
  public static final String SP_KIND = "kind";
  @SearchParamDefinition(name="type", path="NamingSystem.type", description="e.g. driver,  provider,  patient, bank etc.", type="token" )
  public static final String SP_TYPE = "type";
  @SearchParamDefinition(name="id-type", path="NamingSystem.uniqueId.type", description="oid | uuid | uri | other", type="token" )
  public static final String SP_IDTYPE = "id-type";
  @SearchParamDefinition(name="responsible", path="NamingSystem.responsible", description="Who maintains system namespace?", type="string" )
  public static final String SP_RESPONSIBLE = "responsible";
  @SearchParamDefinition(name="contact", path="NamingSystem.contact.name", description="Name of a individual to contact", type="string" )
  public static final String SP_CONTACT = "contact";
  @SearchParamDefinition(name="name", path="NamingSystem.name", description="Human-readable label", type="string" )
  public static final String SP_NAME = "name";
  @SearchParamDefinition(name="context", path="NamingSystem.useContext", description="Content intends to support these contexts", type="token" )
  public static final String SP_CONTEXT = "context";
  @SearchParamDefinition(name="publisher", path="NamingSystem.publisher", description="Name of the publisher (Organization or individual)", type="string" )
  public static final String SP_PUBLISHER = "publisher";
  @SearchParamDefinition(name="telecom", path="NamingSystem.contact.telecom", description="Contact details for individual or publisher", type="token" )
  public static final String SP_TELECOM = "telecom";
  @SearchParamDefinition(name="value", path="NamingSystem.uniqueId.value", description="The unique identifier", type="string" )
  public static final String SP_VALUE = "value";
  @SearchParamDefinition(name="replaced-by", path="NamingSystem.replacedBy", description="Use this instead", type="reference" )
  public static final String SP_REPLACEDBY = "replaced-by";
  @SearchParamDefinition(name="status", path="NamingSystem.status", description="draft | active | retired", type="token" )
  public static final String SP_STATUS = "status";

}

