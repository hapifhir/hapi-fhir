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

// Generated on Sat, Feb 14, 2015 16:12-0500 for FHIR v0.4.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
/**
 * A curated namespace that issues unique symbols within that namespace for the identification of concepts, people, devices, etc.  Represents a "System" used within the Identifier and Coding data types.
 */
@ResourceDef(name="NamingSystem", profile="http://hl7.org/fhir/Profile/NamingSystem")
public class NamingSystem extends DomainResource {

    public enum NamingsystemType {
        /**
         * The namingsystem is used to define concepts and symbols to represent those concepts.  E.g. UCUM, LOINC, NDC code, local lab codes, etc.
         */
        CODESYSTEM, 
        /**
         * The namingsystem is used to manage identifiers (e.g. license numbers, order numbers, etc.).
         */
        IDENTIFIER, 
        /**
         * The namingsystem is used as the root for other identifiers and namingsystems.
         */
        ROOT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static NamingsystemType fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("codesystem".equals(codeString))
          return CODESYSTEM;
        if ("identifier".equals(codeString))
          return IDENTIFIER;
        if ("root".equals(codeString))
          return ROOT;
        throw new Exception("Unknown NamingsystemType code '"+codeString+"'");
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
            case CODESYSTEM: return "";
            case IDENTIFIER: return "";
            case ROOT: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case CODESYSTEM: return "The namingsystem is used to define concepts and symbols to represent those concepts.  E.g. UCUM, LOINC, NDC code, local lab codes, etc.";
            case IDENTIFIER: return "The namingsystem is used to manage identifiers (e.g. license numbers, order numbers, etc.).";
            case ROOT: return "The namingsystem is used as the root for other identifiers and namingsystems.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CODESYSTEM: return "codesystem";
            case IDENTIFIER: return "identifier";
            case ROOT: return "root";
            default: return "?";
          }
        }
    }

  public static class NamingsystemTypeEnumFactory implements EnumFactory<NamingsystemType> {
    public NamingsystemType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("codesystem".equals(codeString))
          return NamingsystemType.CODESYSTEM;
        if ("identifier".equals(codeString))
          return NamingsystemType.IDENTIFIER;
        if ("root".equals(codeString))
          return NamingsystemType.ROOT;
        throw new IllegalArgumentException("Unknown NamingsystemType code '"+codeString+"'");
        }
    public String toCode(NamingsystemType code) {
      if (code == NamingsystemType.CODESYSTEM)
        return "codesystem";
      if (code == NamingsystemType.IDENTIFIER)
        return "identifier";
      if (code == NamingsystemType.ROOT)
        return "root";
      return "?";
      }
    }

    public enum NamingsystemStatus {
        /**
         * System has been submitted but not yet approved.
         */
        PROPOSED, 
        /**
         * System is valid for use.
         */
        ACTIVE, 
        /**
         * System should no longer be used.
         */
        RETIRED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static NamingsystemStatus fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("proposed".equals(codeString))
          return PROPOSED;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("retired".equals(codeString))
          return RETIRED;
        throw new Exception("Unknown NamingsystemStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PROPOSED: return "proposed";
            case ACTIVE: return "active";
            case RETIRED: return "retired";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PROPOSED: return "";
            case ACTIVE: return "";
            case RETIRED: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PROPOSED: return "System has been submitted but not yet approved.";
            case ACTIVE: return "System is valid for use.";
            case RETIRED: return "System should no longer be used.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PROPOSED: return "proposed";
            case ACTIVE: return "active";
            case RETIRED: return "retired";
            default: return "?";
          }
        }
    }

  public static class NamingsystemStatusEnumFactory implements EnumFactory<NamingsystemStatus> {
    public NamingsystemStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("proposed".equals(codeString))
          return NamingsystemStatus.PROPOSED;
        if ("active".equals(codeString))
          return NamingsystemStatus.ACTIVE;
        if ("retired".equals(codeString))
          return NamingsystemStatus.RETIRED;
        throw new IllegalArgumentException("Unknown NamingsystemStatus code '"+codeString+"'");
        }
    public String toCode(NamingsystemStatus code) {
      if (code == NamingsystemStatus.PROPOSED)
        return "proposed";
      if (code == NamingsystemStatus.ACTIVE)
        return "active";
      if (code == NamingsystemStatus.RETIRED)
        return "retired";
      return "?";
      }
    }

    public enum NamingsystemIdentifierType {
        /**
         * An ISO object identifier.  E.g. 1.2.3.4.5.
         */
        OID, 
        /**
         * A universally unique identifier of the form a5afddf4-e880-459b-876e-e4591b0acc11.
         */
        UUID, 
        /**
         * A uniform resource identifier (ideally a URL - uniform resource locator).  E.g. http://unitsofmeasure.org.
         */
        URI, 
        /**
         * Some other type of unique identifier.  E.g HL7-assigned reserved string such as LN for LOINC.
         */
        OTHER, 
        /**
         * added to help the parsers
         */
        NULL;
        public static NamingsystemIdentifierType fromCode(String codeString) throws Exception {
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
        throw new Exception("Unknown NamingsystemIdentifierType code '"+codeString+"'");
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
            case OID: return "";
            case UUID: return "";
            case URI: return "";
            case OTHER: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case OID: return "An ISO object identifier.  E.g. 1.2.3.4.5.";
            case UUID: return "A universally unique identifier of the form a5afddf4-e880-459b-876e-e4591b0acc11.";
            case URI: return "A uniform resource identifier (ideally a URL - uniform resource locator).  E.g. http://unitsofmeasure.org.";
            case OTHER: return "Some other type of unique identifier.  E.g HL7-assigned reserved string such as LN for LOINC.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case OID: return "oid";
            case UUID: return "uuid";
            case URI: return "uri";
            case OTHER: return "other";
            default: return "?";
          }
        }
    }

  public static class NamingsystemIdentifierTypeEnumFactory implements EnumFactory<NamingsystemIdentifierType> {
    public NamingsystemIdentifierType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("oid".equals(codeString))
          return NamingsystemIdentifierType.OID;
        if ("uuid".equals(codeString))
          return NamingsystemIdentifierType.UUID;
        if ("uri".equals(codeString))
          return NamingsystemIdentifierType.URI;
        if ("other".equals(codeString))
          return NamingsystemIdentifierType.OTHER;
        throw new IllegalArgumentException("Unknown NamingsystemIdentifierType code '"+codeString+"'");
        }
    public String toCode(NamingsystemIdentifierType code) {
      if (code == NamingsystemIdentifierType.OID)
        return "oid";
      if (code == NamingsystemIdentifierType.UUID)
        return "uuid";
      if (code == NamingsystemIdentifierType.URI)
        return "uri";
      if (code == NamingsystemIdentifierType.OTHER)
        return "other";
      return "?";
      }
    }

    @Block()
    public static class NamingSystemUniqueIdComponent extends BackboneElement {
        /**
         * Identifies the unique identifier scheme used for this particular identifier.
         */
        @Child(name="type", type={CodeType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="oid | uuid | uri | other", formalDefinition="Identifies the unique identifier scheme used for this particular identifier." )
        protected Enumeration<NamingsystemIdentifierType> type;

        /**
         * The string that should be sent over the wire to identify the code system or identifier system.
         */
        @Child(name="value", type={StringType.class}, order=2, min=1, max=1)
        @Description(shortDefinition="The unique identifier", formalDefinition="The string that should be sent over the wire to identify the code system or identifier system." )
        protected StringType value;

        /**
         * Indicates whether this identifier is the "preferred" identifier of this type.
         */
        @Child(name="preferred", type={BooleanType.class}, order=3, min=0, max=1)
        @Description(shortDefinition="Is this the id that should be used for this type", formalDefinition="Indicates whether this identifier is the 'preferred' identifier of this type." )
        protected BooleanType preferred;

        /**
         * Identifies the period of time over which this identifier is considered appropriate to refer to the namingsystem.  Outside of this window, the identifier might be non-deterministic.
         */
        @Child(name="period", type={Period.class}, order=4, min=0, max=1)
        @Description(shortDefinition="When is identifier valid?", formalDefinition="Identifies the period of time over which this identifier is considered appropriate to refer to the namingsystem.  Outside of this window, the identifier might be non-deterministic." )
        protected Period period;

        private static final long serialVersionUID = -250649344L;

      public NamingSystemUniqueIdComponent() {
        super();
      }

      public NamingSystemUniqueIdComponent(Enumeration<NamingsystemIdentifierType> type, StringType value) {
        super();
        this.type = type;
        this.value = value;
      }

        /**
         * @return {@link #type} (Identifies the unique identifier scheme used for this particular identifier.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<NamingsystemIdentifierType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NamingSystemUniqueIdComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Enumeration<NamingsystemIdentifierType>(new NamingsystemIdentifierTypeEnumFactory()); // bb
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
        public NamingSystemUniqueIdComponent setTypeElement(Enumeration<NamingsystemIdentifierType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return Identifies the unique identifier scheme used for this particular identifier.
         */
        public NamingsystemIdentifierType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value Identifies the unique identifier scheme used for this particular identifier.
         */
        public NamingSystemUniqueIdComponent setType(NamingsystemIdentifierType value) { 
            if (this.type == null)
              this.type = new Enumeration<NamingsystemIdentifierType>(new NamingsystemIdentifierTypeEnumFactory());
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
          return this.preferred == null ? false : this.preferred.getValue();
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
         * @return {@link #period} (Identifies the period of time over which this identifier is considered appropriate to refer to the namingsystem.  Outside of this window, the identifier might be non-deterministic.)
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
         * @param value {@link #period} (Identifies the period of time over which this identifier is considered appropriate to refer to the namingsystem.  Outside of this window, the identifier might be non-deterministic.)
         */
        public NamingSystemUniqueIdComponent setPeriod(Period value) { 
          this.period = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "code", "Identifies the unique identifier scheme used for this particular identifier.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("value", "string", "The string that should be sent over the wire to identify the code system or identifier system.", 0, java.lang.Integer.MAX_VALUE, value));
          childrenList.add(new Property("preferred", "boolean", "Indicates whether this identifier is the 'preferred' identifier of this type.", 0, java.lang.Integer.MAX_VALUE, preferred));
          childrenList.add(new Property("period", "Period", "Identifies the period of time over which this identifier is considered appropriate to refer to the namingsystem.  Outside of this window, the identifier might be non-deterministic.", 0, java.lang.Integer.MAX_VALUE, period));
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

    @Block()
    public static class NamingSystemContactComponent extends BackboneElement {
        /**
         * Names of the person who can be contacted.
         */
        @Child(name="name", type={HumanName.class}, order=1, min=0, max=1)
        @Description(shortDefinition="Name of person", formalDefinition="Names of the person who can be contacted." )
        protected HumanName name;

        /**
         * Identifies the mechanism(s) by which they can be contacted.
         */
        @Child(name="telecom", type={ContactPoint.class}, order=2, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Phone, email, etc.", formalDefinition="Identifies the mechanism(s) by which they can be contacted." )
        protected List<ContactPoint> telecom;

        private static final long serialVersionUID = -888464692L;

      public NamingSystemContactComponent() {
        super();
      }

        /**
         * @return {@link #name} (Names of the person who can be contacted.)
         */
        public HumanName getName() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NamingSystemContactComponent.name");
            else if (Configuration.doAutoCreate())
              this.name = new HumanName(); // cc
          return this.name;
        }

        public boolean hasName() { 
          return this.name != null && !this.name.isEmpty();
        }

        /**
         * @param value {@link #name} (Names of the person who can be contacted.)
         */
        public NamingSystemContactComponent setName(HumanName value) { 
          this.name = value;
          return this;
        }

        /**
         * @return {@link #telecom} (Identifies the mechanism(s) by which they can be contacted.)
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
         * @return {@link #telecom} (Identifies the mechanism(s) by which they can be contacted.)
         */
    // syntactic sugar
        public ContactPoint addTelecom() { //3
          ContactPoint t = new ContactPoint();
          if (this.telecom == null)
            this.telecom = new ArrayList<ContactPoint>();
          this.telecom.add(t);
          return t;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "HumanName", "Names of the person who can be contacted.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("telecom", "ContactPoint", "Identifies the mechanism(s) by which they can be contacted.", 0, java.lang.Integer.MAX_VALUE, telecom));
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
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (telecom == null || telecom.isEmpty())
          ;
      }

  }

    /**
     * Indicates the purpose for the namingsystem - what kinds of things does it make unique?.
     */
    @Child(name="type", type={CodeType.class}, order=-1, min=1, max=1)
    @Description(shortDefinition="codesystem | identifier | root", formalDefinition="Indicates the purpose for the namingsystem - what kinds of things does it make unique?." )
    protected Enumeration<NamingsystemType> type;

    /**
     * The descriptive name of this particular identifier type or code system.
     */
    @Child(name="name", type={StringType.class}, order=0, min=1, max=1)
    @Description(shortDefinition="Human-readable label", formalDefinition="The descriptive name of this particular identifier type or code system." )
    protected StringType name;

    /**
     * Indicates whether the namingsystem is "ready for use" or not.
     */
    @Child(name="status", type={CodeType.class}, order=1, min=1, max=1)
    @Description(shortDefinition="proposed | active | retired", formalDefinition="Indicates whether the namingsystem is 'ready for use' or not." )
    protected Enumeration<NamingsystemStatus> status;

    /**
     * If present, indicates that the identifier or code system is principally intended for use or applies to entities within the specified country.  For example, the country associated with a national code system.
     */
    @Child(name="country", type={CodeType.class}, order=2, min=0, max=1)
    @Description(shortDefinition="ISO 3-char country code", formalDefinition="If present, indicates that the identifier or code system is principally intended for use or applies to entities within the specified country.  For example, the country associated with a national code system." )
    protected CodeType country;

    /**
     * Categorizes a namingsystem for easier search by grouping related namingsystems.
     */
    @Child(name="category", type={CodeableConcept.class}, order=3, min=0, max=1)
    @Description(shortDefinition="driver | provider | patient | bank", formalDefinition="Categorizes a namingsystem for easier search by grouping related namingsystems." )
    protected CodeableConcept category;

    /**
     * The name of the organization that is responsible for issuing identifiers or codes for this namespace and ensuring their non-collision.
     */
    @Child(name="responsible", type={StringType.class}, order=4, min=0, max=1)
    @Description(shortDefinition="Who maintains system namespace?", formalDefinition="The name of the organization that is responsible for issuing identifiers or codes for this namespace and ensuring their non-collision." )
    protected StringType responsible;

    /**
     * Details about what the namespace identifies including scope, granularity, version labeling, etc.
     */
    @Child(name="description", type={StringType.class}, order=5, min=0, max=1)
    @Description(shortDefinition="What does namingsystem identify?", formalDefinition="Details about what the namespace identifies including scope, granularity, version labeling, etc." )
    protected StringType description;

    /**
     * Provides guidance on the use of the namespace, including the handling of formatting characters, use of upper vs. lower case, etc.
     */
    @Child(name="usage", type={StringType.class}, order=6, min=0, max=1)
    @Description(shortDefinition="How/where is it used", formalDefinition="Provides guidance on the use of the namespace, including the handling of formatting characters, use of upper vs. lower case, etc." )
    protected StringType usage;

    /**
     * Indicates how the system may be identified when referenced in electronic exchange.
     */
    @Child(name="uniqueId", type={}, order=7, min=1, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Unique identifiers used for system", formalDefinition="Indicates how the system may be identified when referenced in electronic exchange." )
    protected List<NamingSystemUniqueIdComponent> uniqueId;

    /**
     * The person who can be contacted about this system registration entry.
     */
    @Child(name="contact", type={}, order=8, min=0, max=1)
    @Description(shortDefinition="Who should be contacted for questions about namingsystem", formalDefinition="The person who can be contacted about this system registration entry." )
    protected NamingSystemContactComponent contact;

    /**
     * For namingsystems that are retired, indicates the namingsystem that should be used in their place (if any).
     */
    @Child(name="replacedBy", type={NamingSystem.class}, order=9, min=0, max=1)
    @Description(shortDefinition="Use this instead", formalDefinition="For namingsystems that are retired, indicates the namingsystem that should be used in their place (if any)." )
    protected Reference replacedBy;

    /**
     * The actual object that is the target of the reference (For namingsystems that are retired, indicates the namingsystem that should be used in their place (if any).)
     */
    protected NamingSystem replacedByTarget;

    private static final long serialVersionUID = -797915837L;

    public NamingSystem() {
      super();
    }

    public NamingSystem(Enumeration<NamingsystemType> type, StringType name, Enumeration<NamingsystemStatus> status) {
      super();
      this.type = type;
      this.name = name;
      this.status = status;
    }

    /**
     * @return {@link #type} (Indicates the purpose for the namingsystem - what kinds of things does it make unique?.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public Enumeration<NamingsystemType> getTypeElement() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create NamingSystem.type");
        else if (Configuration.doAutoCreate())
          this.type = new Enumeration<NamingsystemType>(new NamingsystemTypeEnumFactory()); // bb
      return this.type;
    }

    public boolean hasTypeElement() { 
      return this.type != null && !this.type.isEmpty();
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (Indicates the purpose for the namingsystem - what kinds of things does it make unique?.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public NamingSystem setTypeElement(Enumeration<NamingsystemType> value) { 
      this.type = value;
      return this;
    }

    /**
     * @return Indicates the purpose for the namingsystem - what kinds of things does it make unique?.
     */
    public NamingsystemType getType() { 
      return this.type == null ? null : this.type.getValue();
    }

    /**
     * @param value Indicates the purpose for the namingsystem - what kinds of things does it make unique?.
     */
    public NamingSystem setType(NamingsystemType value) { 
        if (this.type == null)
          this.type = new Enumeration<NamingsystemType>(new NamingsystemTypeEnumFactory());
        this.type.setValue(value);
      return this;
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
     * @return {@link #status} (Indicates whether the namingsystem is "ready for use" or not.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<NamingsystemStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create NamingSystem.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<NamingsystemStatus>(new NamingsystemStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (Indicates whether the namingsystem is "ready for use" or not.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public NamingSystem setStatusElement(Enumeration<NamingsystemStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return Indicates whether the namingsystem is "ready for use" or not.
     */
    public NamingsystemStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value Indicates whether the namingsystem is "ready for use" or not.
     */
    public NamingSystem setStatus(NamingsystemStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<NamingsystemStatus>(new NamingsystemStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #country} (If present, indicates that the identifier or code system is principally intended for use or applies to entities within the specified country.  For example, the country associated with a national code system.). This is the underlying object with id, value and extensions. The accessor "getCountry" gives direct access to the value
     */
    public CodeType getCountryElement() { 
      if (this.country == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create NamingSystem.country");
        else if (Configuration.doAutoCreate())
          this.country = new CodeType(); // bb
      return this.country;
    }

    public boolean hasCountryElement() { 
      return this.country != null && !this.country.isEmpty();
    }

    public boolean hasCountry() { 
      return this.country != null && !this.country.isEmpty();
    }

    /**
     * @param value {@link #country} (If present, indicates that the identifier or code system is principally intended for use or applies to entities within the specified country.  For example, the country associated with a national code system.). This is the underlying object with id, value and extensions. The accessor "getCountry" gives direct access to the value
     */
    public NamingSystem setCountryElement(CodeType value) { 
      this.country = value;
      return this;
    }

    /**
     * @return If present, indicates that the identifier or code system is principally intended for use or applies to entities within the specified country.  For example, the country associated with a national code system.
     */
    public String getCountry() { 
      return this.country == null ? null : this.country.getValue();
    }

    /**
     * @param value If present, indicates that the identifier or code system is principally intended for use or applies to entities within the specified country.  For example, the country associated with a national code system.
     */
    public NamingSystem setCountry(String value) { 
      if (Utilities.noString(value))
        this.country = null;
      else {
        if (this.country == null)
          this.country = new CodeType();
        this.country.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #category} (Categorizes a namingsystem for easier search by grouping related namingsystems.)
     */
    public CodeableConcept getCategory() { 
      if (this.category == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create NamingSystem.category");
        else if (Configuration.doAutoCreate())
          this.category = new CodeableConcept(); // cc
      return this.category;
    }

    public boolean hasCategory() { 
      return this.category != null && !this.category.isEmpty();
    }

    /**
     * @param value {@link #category} (Categorizes a namingsystem for easier search by grouping related namingsystems.)
     */
    public NamingSystem setCategory(CodeableConcept value) { 
      this.category = value;
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

    /**
     * @return {@link #contact} (The person who can be contacted about this system registration entry.)
     */
    public NamingSystemContactComponent getContact() { 
      if (this.contact == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create NamingSystem.contact");
        else if (Configuration.doAutoCreate())
          this.contact = new NamingSystemContactComponent(); // cc
      return this.contact;
    }

    public boolean hasContact() { 
      return this.contact != null && !this.contact.isEmpty();
    }

    /**
     * @param value {@link #contact} (The person who can be contacted about this system registration entry.)
     */
    public NamingSystem setContact(NamingSystemContactComponent value) { 
      this.contact = value;
      return this;
    }

    /**
     * @return {@link #replacedBy} (For namingsystems that are retired, indicates the namingsystem that should be used in their place (if any).)
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
     * @param value {@link #replacedBy} (For namingsystems that are retired, indicates the namingsystem that should be used in their place (if any).)
     */
    public NamingSystem setReplacedBy(Reference value) { 
      this.replacedBy = value;
      return this;
    }

    /**
     * @return {@link #replacedBy} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (For namingsystems that are retired, indicates the namingsystem that should be used in their place (if any).)
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
     * @param value {@link #replacedBy} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (For namingsystems that are retired, indicates the namingsystem that should be used in their place (if any).)
     */
    public NamingSystem setReplacedByTarget(NamingSystem value) { 
      this.replacedByTarget = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("type", "code", "Indicates the purpose for the namingsystem - what kinds of things does it make unique?.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("name", "string", "The descriptive name of this particular identifier type or code system.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("status", "code", "Indicates whether the namingsystem is 'ready for use' or not.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("country", "code", "If present, indicates that the identifier or code system is principally intended for use or applies to entities within the specified country.  For example, the country associated with a national code system.", 0, java.lang.Integer.MAX_VALUE, country));
        childrenList.add(new Property("category", "CodeableConcept", "Categorizes a namingsystem for easier search by grouping related namingsystems.", 0, java.lang.Integer.MAX_VALUE, category));
        childrenList.add(new Property("responsible", "string", "The name of the organization that is responsible for issuing identifiers or codes for this namespace and ensuring their non-collision.", 0, java.lang.Integer.MAX_VALUE, responsible));
        childrenList.add(new Property("description", "string", "Details about what the namespace identifies including scope, granularity, version labeling, etc.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("usage", "string", "Provides guidance on the use of the namespace, including the handling of formatting characters, use of upper vs. lower case, etc.", 0, java.lang.Integer.MAX_VALUE, usage));
        childrenList.add(new Property("uniqueId", "", "Indicates how the system may be identified when referenced in electronic exchange.", 0, java.lang.Integer.MAX_VALUE, uniqueId));
        childrenList.add(new Property("contact", "", "The person who can be contacted about this system registration entry.", 0, java.lang.Integer.MAX_VALUE, contact));
        childrenList.add(new Property("replacedBy", "Reference(NamingSystem)", "For namingsystems that are retired, indicates the namingsystem that should be used in their place (if any).", 0, java.lang.Integer.MAX_VALUE, replacedBy));
      }

      public NamingSystem copy() {
        NamingSystem dst = new NamingSystem();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.name = name == null ? null : name.copy();
        dst.status = status == null ? null : status.copy();
        dst.country = country == null ? null : country.copy();
        dst.category = category == null ? null : category.copy();
        dst.responsible = responsible == null ? null : responsible.copy();
        dst.description = description == null ? null : description.copy();
        dst.usage = usage == null ? null : usage.copy();
        if (uniqueId != null) {
          dst.uniqueId = new ArrayList<NamingSystemUniqueIdComponent>();
          for (NamingSystemUniqueIdComponent i : uniqueId)
            dst.uniqueId.add(i.copy());
        };
        dst.contact = contact == null ? null : contact.copy();
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
        return compareDeep(type, o.type, true) && compareDeep(name, o.name, true) && compareDeep(status, o.status, true)
           && compareDeep(country, o.country, true) && compareDeep(category, o.category, true) && compareDeep(responsible, o.responsible, true)
           && compareDeep(description, o.description, true) && compareDeep(usage, o.usage, true) && compareDeep(uniqueId, o.uniqueId, true)
           && compareDeep(contact, o.contact, true) && compareDeep(replacedBy, o.replacedBy, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof NamingSystem))
          return false;
        NamingSystem o = (NamingSystem) other;
        return compareValues(type, o.type, true) && compareValues(name, o.name, true) && compareValues(status, o.status, true)
           && compareValues(country, o.country, true) && compareValues(responsible, o.responsible, true) && compareValues(description, o.description, true)
           && compareValues(usage, o.usage, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (name == null || name.isEmpty())
           && (status == null || status.isEmpty()) && (country == null || country.isEmpty()) && (category == null || category.isEmpty())
           && (responsible == null || responsible.isEmpty()) && (description == null || description.isEmpty())
           && (usage == null || usage.isEmpty()) && (uniqueId == null || uniqueId.isEmpty()) && (contact == null || contact.isEmpty())
           && (replacedBy == null || replacedBy.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.NamingSystem;
   }


}

