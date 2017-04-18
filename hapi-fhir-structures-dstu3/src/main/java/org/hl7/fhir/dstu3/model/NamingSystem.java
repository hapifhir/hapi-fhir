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
 * A curated namespace that issues unique symbols within that namespace for the identification of concepts, people, devices, etc.  Represents a "System" used within the Identifier and Coding data types.
 */
@ResourceDef(name="NamingSystem", profile="http://hl7.org/fhir/Profile/NamingSystem")
@ChildOrder(names={"name", "status", "kind", "date", "publisher", "contact", "responsible", "type", "description", "useContext", "jurisdiction", "usage", "uniqueId", "replacedBy"})
public class NamingSystem extends MetadataResource {

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
         * added to help the parsers with the generic types
         */
        NULL;
        public static NamingSystemType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("codesystem".equals(codeString))
          return CODESYSTEM;
        if ("identifier".equals(codeString))
          return IDENTIFIER;
        if ("root".equals(codeString))
          return ROOT;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown NamingSystemType code '"+codeString+"'");
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
        public Enumeration<NamingSystemType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<NamingSystemType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("codesystem".equals(codeString))
          return new Enumeration<NamingSystemType>(this, NamingSystemType.CODESYSTEM);
        if ("identifier".equals(codeString))
          return new Enumeration<NamingSystemType>(this, NamingSystemType.IDENTIFIER);
        if ("root".equals(codeString))
          return new Enumeration<NamingSystemType>(this, NamingSystemType.ROOT);
        throw new FHIRException("Unknown NamingSystemType code '"+codeString+"'");
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
    public String toSystem(NamingSystemType code) {
      return code.getSystem();
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
         * added to help the parsers with the generic types
         */
        NULL;
        public static NamingSystemIdentifierType fromCode(String codeString) throws FHIRException {
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
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown NamingSystemIdentifierType code '"+codeString+"'");
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
        public Enumeration<NamingSystemIdentifierType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<NamingSystemIdentifierType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("oid".equals(codeString))
          return new Enumeration<NamingSystemIdentifierType>(this, NamingSystemIdentifierType.OID);
        if ("uuid".equals(codeString))
          return new Enumeration<NamingSystemIdentifierType>(this, NamingSystemIdentifierType.UUID);
        if ("uri".equals(codeString))
          return new Enumeration<NamingSystemIdentifierType>(this, NamingSystemIdentifierType.URI);
        if ("other".equals(codeString))
          return new Enumeration<NamingSystemIdentifierType>(this, NamingSystemIdentifierType.OTHER);
        throw new FHIRException("Unknown NamingSystemIdentifierType code '"+codeString+"'");
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
    public String toSystem(NamingSystemIdentifierType code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class NamingSystemUniqueIdComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Identifies the unique identifier scheme used for this particular identifier.
         */
        @Child(name = "type", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="oid | uuid | uri | other", formalDefinition="Identifies the unique identifier scheme used for this particular identifier." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/namingsystem-identifier-type")
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
         * Notes about the past or intended usage of this identifier.
         */
        @Child(name = "comment", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Notes about identifier usage", formalDefinition="Notes about the past or intended usage of this identifier." )
        protected StringType comment;

        /**
         * Identifies the period of time over which this identifier is considered appropriate to refer to the naming system.  Outside of this window, the identifier might be non-deterministic.
         */
        @Child(name = "period", type = {Period.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="When is identifier valid?", formalDefinition="Identifies the period of time over which this identifier is considered appropriate to refer to the naming system.  Outside of this window, the identifier might be non-deterministic." )
        protected Period period;

        private static final long serialVersionUID = -1458889328L;

    /**
     * Constructor
     */
      public NamingSystemUniqueIdComponent() {
        super();
      }

    /**
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
         * @return {@link #comment} (Notes about the past or intended usage of this identifier.). This is the underlying object with id, value and extensions. The accessor "getComment" gives direct access to the value
         */
        public StringType getCommentElement() { 
          if (this.comment == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NamingSystemUniqueIdComponent.comment");
            else if (Configuration.doAutoCreate())
              this.comment = new StringType(); // bb
          return this.comment;
        }

        public boolean hasCommentElement() { 
          return this.comment != null && !this.comment.isEmpty();
        }

        public boolean hasComment() { 
          return this.comment != null && !this.comment.isEmpty();
        }

        /**
         * @param value {@link #comment} (Notes about the past or intended usage of this identifier.). This is the underlying object with id, value and extensions. The accessor "getComment" gives direct access to the value
         */
        public NamingSystemUniqueIdComponent setCommentElement(StringType value) { 
          this.comment = value;
          return this;
        }

        /**
         * @return Notes about the past or intended usage of this identifier.
         */
        public String getComment() { 
          return this.comment == null ? null : this.comment.getValue();
        }

        /**
         * @param value Notes about the past or intended usage of this identifier.
         */
        public NamingSystemUniqueIdComponent setComment(String value) { 
          if (Utilities.noString(value))
            this.comment = null;
          else {
            if (this.comment == null)
              this.comment = new StringType();
            this.comment.setValue(value);
          }
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
          childrenList.add(new Property("comment", "string", "Notes about the past or intended usage of this identifier.", 0, java.lang.Integer.MAX_VALUE, comment));
          childrenList.add(new Property("period", "Period", "Identifies the period of time over which this identifier is considered appropriate to refer to the naming system.  Outside of this window, the identifier might be non-deterministic.", 0, java.lang.Integer.MAX_VALUE, period));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<NamingSystemIdentifierType>
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // StringType
        case -1294005119: /*preferred*/ return this.preferred == null ? new Base[0] : new Base[] {this.preferred}; // BooleanType
        case 950398559: /*comment*/ return this.comment == null ? new Base[0] : new Base[] {this.comment}; // StringType
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // Period
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          value = new NamingSystemIdentifierTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<NamingSystemIdentifierType>
          return value;
        case 111972721: // value
          this.value = castToString(value); // StringType
          return value;
        case -1294005119: // preferred
          this.preferred = castToBoolean(value); // BooleanType
          return value;
        case 950398559: // comment
          this.comment = castToString(value); // StringType
          return value;
        case -991726143: // period
          this.period = castToPeriod(value); // Period
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          value = new NamingSystemIdentifierTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<NamingSystemIdentifierType>
        } else if (name.equals("value")) {
          this.value = castToString(value); // StringType
        } else if (name.equals("preferred")) {
          this.preferred = castToBoolean(value); // BooleanType
        } else if (name.equals("comment")) {
          this.comment = castToString(value); // StringType
        } else if (name.equals("period")) {
          this.period = castToPeriod(value); // Period
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getTypeElement();
        case 111972721:  return getValueElement();
        case -1294005119:  return getPreferredElement();
        case 950398559:  return getCommentElement();
        case -991726143:  return getPeriod(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"code"};
        case 111972721: /*value*/ return new String[] {"string"};
        case -1294005119: /*preferred*/ return new String[] {"boolean"};
        case 950398559: /*comment*/ return new String[] {"string"};
        case -991726143: /*period*/ return new String[] {"Period"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type NamingSystem.type");
        }
        else if (name.equals("value")) {
          throw new FHIRException("Cannot call addChild on a primitive type NamingSystem.value");
        }
        else if (name.equals("preferred")) {
          throw new FHIRException("Cannot call addChild on a primitive type NamingSystem.preferred");
        }
        else if (name.equals("comment")) {
          throw new FHIRException("Cannot call addChild on a primitive type NamingSystem.comment");
        }
        else if (name.equals("period")) {
          this.period = new Period();
          return this.period;
        }
        else
          return super.addChild(name);
      }

      public NamingSystemUniqueIdComponent copy() {
        NamingSystemUniqueIdComponent dst = new NamingSystemUniqueIdComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.value = value == null ? null : value.copy();
        dst.preferred = preferred == null ? null : preferred.copy();
        dst.comment = comment == null ? null : comment.copy();
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
           && compareDeep(comment, o.comment, true) && compareDeep(period, o.period, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof NamingSystemUniqueIdComponent))
          return false;
        NamingSystemUniqueIdComponent o = (NamingSystemUniqueIdComponent) other;
        return compareValues(type, o.type, true) && compareValues(value, o.value, true) && compareValues(preferred, o.preferred, true)
           && compareValues(comment, o.comment, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, value, preferred, comment
          , period);
      }

  public String fhirType() {
    return "NamingSystem.uniqueId";

  }

  }

    /**
     * Indicates the purpose for the naming system - what kinds of things does it make unique?
     */
    @Child(name = "kind", type = {CodeType.class}, order=0, min=1, max=1, modifier=false, summary=false)
    @Description(shortDefinition="codesystem | identifier | root", formalDefinition="Indicates the purpose for the naming system - what kinds of things does it make unique?" )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/namingsystem-type")
    protected Enumeration<NamingSystemType> kind;

    /**
     * The name of the organization that is responsible for issuing identifiers or codes for this namespace and ensuring their non-collision.
     */
    @Child(name = "responsible", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Who maintains system namespace?", formalDefinition="The name of the organization that is responsible for issuing identifiers or codes for this namespace and ensuring their non-collision." )
    protected StringType responsible;

    /**
     * Categorizes a naming system for easier search by grouping related naming systems.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="e.g. driver,  provider,  patient, bank etc.", formalDefinition="Categorizes a naming system for easier search by grouping related naming systems." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/identifier-type")
    protected CodeableConcept type;

    /**
     * Provides guidance on the use of the namespace, including the handling of formatting characters, use of upper vs. lower case, etc.
     */
    @Child(name = "usage", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="How/where is it used", formalDefinition="Provides guidance on the use of the namespace, including the handling of formatting characters, use of upper vs. lower case, etc." )
    protected StringType usage;

    /**
     * Indicates how the system may be identified when referenced in electronic exchange.
     */
    @Child(name = "uniqueId", type = {}, order=4, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Unique identifiers used for system", formalDefinition="Indicates how the system may be identified when referenced in electronic exchange." )
    protected List<NamingSystemUniqueIdComponent> uniqueId;

    /**
     * For naming systems that are retired, indicates the naming system that should be used in their place (if any).
     */
    @Child(name = "replacedBy", type = {NamingSystem.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Use this instead", formalDefinition="For naming systems that are retired, indicates the naming system that should be used in their place (if any)." )
    protected Reference replacedBy;

    /**
     * The actual object that is the target of the reference (For naming systems that are retired, indicates the naming system that should be used in their place (if any).)
     */
    protected NamingSystem replacedByTarget;

    private static final long serialVersionUID = -743416513L;

  /**
   * Constructor
   */
    public NamingSystem() {
      super();
    }

  /**
   * Constructor
   */
    public NamingSystem(StringType name, Enumeration<PublicationStatus> status, Enumeration<NamingSystemType> kind, DateTimeType date) {
      super();
      this.name = name;
      this.status = status;
      this.kind = kind;
      this.date = date;
    }

    /**
     * @return {@link #name} (A natural language name identifying the naming system. This name should be usable as an identifier for the module by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
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
     * @param value {@link #name} (A natural language name identifying the naming system. This name should be usable as an identifier for the module by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public NamingSystem setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return A natural language name identifying the naming system. This name should be usable as an identifier for the module by machine processing applications such as code generation.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value A natural language name identifying the naming system. This name should be usable as an identifier for the module by machine processing applications such as code generation.
     */
    public NamingSystem setName(String value) { 
        if (this.name == null)
          this.name = new StringType();
        this.name.setValue(value);
      return this;
    }

    /**
     * @return {@link #status} (The status of this naming system. Enables tracking the life-cycle of the content.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<PublicationStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create NamingSystem.status");
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
     * @param value {@link #status} (The status of this naming system. Enables tracking the life-cycle of the content.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public NamingSystem setStatusElement(Enumeration<PublicationStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of this naming system. Enables tracking the life-cycle of the content.
     */
    public PublicationStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of this naming system. Enables tracking the life-cycle of the content.
     */
    public NamingSystem setStatus(PublicationStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<PublicationStatus>(new PublicationStatusEnumFactory());
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
     * @return {@link #date} (The date  (and optionally time) when the naming system was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the naming system changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
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
     * @param value {@link #date} (The date  (and optionally time) when the naming system was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the naming system changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public NamingSystem setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date  (and optionally time) when the naming system was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the naming system changes.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date  (and optionally time) when the naming system was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the naming system changes.
     */
    public NamingSystem setDate(Date value) { 
        if (this.date == null)
          this.date = new DateTimeType();
        this.date.setValue(value);
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
    public NamingSystem setContact(List<ContactDetail> theContact) { 
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

    public NamingSystem addContact(ContactDetail t) { //3
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
     * @return {@link #description} (A free text natural language description of the naming system from a consumer's perspective. Details about what the namespace identifies including scope, granularity, version labeling, etc.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public MarkdownType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create NamingSystem.description");
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
     * @param value {@link #description} (A free text natural language description of the naming system from a consumer's perspective. Details about what the namespace identifies including scope, granularity, version labeling, etc.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public NamingSystem setDescriptionElement(MarkdownType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A free text natural language description of the naming system from a consumer's perspective. Details about what the namespace identifies including scope, granularity, version labeling, etc.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A free text natural language description of the naming system from a consumer's perspective. Details about what the namespace identifies including scope, granularity, version labeling, etc.
     */
    public NamingSystem setDescription(String value) { 
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
     * @return {@link #useContext} (The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching for appropriate naming system instances.)
     */
    public List<UsageContext> getUseContext() { 
      if (this.useContext == null)
        this.useContext = new ArrayList<UsageContext>();
      return this.useContext;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public NamingSystem setUseContext(List<UsageContext> theUseContext) { 
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

    public NamingSystem addUseContext(UsageContext t) { //3
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
     * @return {@link #jurisdiction} (A legal or geographic region in which the naming system is intended to be used.)
     */
    public List<CodeableConcept> getJurisdiction() { 
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      return this.jurisdiction;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public NamingSystem setJurisdiction(List<CodeableConcept> theJurisdiction) { 
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

    public NamingSystem addJurisdiction(CodeableConcept t) { //3
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

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public NamingSystem setUniqueId(List<NamingSystemUniqueIdComponent> theUniqueId) { 
      this.uniqueId = theUniqueId;
      return this;
    }

    public boolean hasUniqueId() { 
      if (this.uniqueId == null)
        return false;
      for (NamingSystemUniqueIdComponent item : this.uniqueId)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public NamingSystemUniqueIdComponent addUniqueId() { //3
      NamingSystemUniqueIdComponent t = new NamingSystemUniqueIdComponent();
      if (this.uniqueId == null)
        this.uniqueId = new ArrayList<NamingSystemUniqueIdComponent>();
      this.uniqueId.add(t);
      return t;
    }

    public NamingSystem addUniqueId(NamingSystemUniqueIdComponent t) { //3
      if (t == null)
        return this;
      if (this.uniqueId == null)
        this.uniqueId = new ArrayList<NamingSystemUniqueIdComponent>();
      this.uniqueId.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #uniqueId}, creating it if it does not already exist
     */
    public NamingSystemUniqueIdComponent getUniqueIdFirstRep() { 
      if (getUniqueId().isEmpty()) {
        addUniqueId();
      }
      return getUniqueId().get(0);
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
        childrenList.add(new Property("name", "string", "A natural language name identifying the naming system. This name should be usable as an identifier for the module by machine processing applications such as code generation.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("status", "code", "The status of this naming system. Enables tracking the life-cycle of the content.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("kind", "code", "Indicates the purpose for the naming system - what kinds of things does it make unique?", 0, java.lang.Integer.MAX_VALUE, kind));
        childrenList.add(new Property("date", "dateTime", "The date  (and optionally time) when the naming system was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the naming system changes.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("publisher", "string", "The name of the individual or organization that published the naming system.", 0, java.lang.Integer.MAX_VALUE, publisher));
        childrenList.add(new Property("contact", "ContactDetail", "Contact details to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact));
        childrenList.add(new Property("responsible", "string", "The name of the organization that is responsible for issuing identifiers or codes for this namespace and ensuring their non-collision.", 0, java.lang.Integer.MAX_VALUE, responsible));
        childrenList.add(new Property("type", "CodeableConcept", "Categorizes a naming system for easier search by grouping related naming systems.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("description", "markdown", "A free text natural language description of the naming system from a consumer's perspective. Details about what the namespace identifies including scope, granularity, version labeling, etc.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("useContext", "UsageContext", "The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching for appropriate naming system instances.", 0, java.lang.Integer.MAX_VALUE, useContext));
        childrenList.add(new Property("jurisdiction", "CodeableConcept", "A legal or geographic region in which the naming system is intended to be used.", 0, java.lang.Integer.MAX_VALUE, jurisdiction));
        childrenList.add(new Property("usage", "string", "Provides guidance on the use of the namespace, including the handling of formatting characters, use of upper vs. lower case, etc.", 0, java.lang.Integer.MAX_VALUE, usage));
        childrenList.add(new Property("uniqueId", "", "Indicates how the system may be identified when referenced in electronic exchange.", 0, java.lang.Integer.MAX_VALUE, uniqueId));
        childrenList.add(new Property("replacedBy", "Reference(NamingSystem)", "For naming systems that are retired, indicates the naming system that should be used in their place (if any).", 0, java.lang.Integer.MAX_VALUE, replacedBy));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<PublicationStatus>
        case 3292052: /*kind*/ return this.kind == null ? new Base[0] : new Base[] {this.kind}; // Enumeration<NamingSystemType>
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateTimeType
        case 1447404028: /*publisher*/ return this.publisher == null ? new Base[0] : new Base[] {this.publisher}; // StringType
        case 951526432: /*contact*/ return this.contact == null ? new Base[0] : this.contact.toArray(new Base[this.contact.size()]); // ContactDetail
        case 1847674614: /*responsible*/ return this.responsible == null ? new Base[0] : new Base[] {this.responsible}; // StringType
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // MarkdownType
        case -669707736: /*useContext*/ return this.useContext == null ? new Base[0] : this.useContext.toArray(new Base[this.useContext.size()]); // UsageContext
        case -507075711: /*jurisdiction*/ return this.jurisdiction == null ? new Base[0] : this.jurisdiction.toArray(new Base[this.jurisdiction.size()]); // CodeableConcept
        case 111574433: /*usage*/ return this.usage == null ? new Base[0] : new Base[] {this.usage}; // StringType
        case -294460212: /*uniqueId*/ return this.uniqueId == null ? new Base[0] : this.uniqueId.toArray(new Base[this.uniqueId.size()]); // NamingSystemUniqueIdComponent
        case -1233035097: /*replacedBy*/ return this.replacedBy == null ? new Base[0] : new Base[] {this.replacedBy}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case -892481550: // status
          value = new PublicationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<PublicationStatus>
          return value;
        case 3292052: // kind
          value = new NamingSystemTypeEnumFactory().fromType(castToCode(value));
          this.kind = (Enumeration) value; // Enumeration<NamingSystemType>
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
        case 1847674614: // responsible
          this.responsible = castToString(value); // StringType
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
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
        case 111574433: // usage
          this.usage = castToString(value); // StringType
          return value;
        case -294460212: // uniqueId
          this.getUniqueId().add((NamingSystemUniqueIdComponent) value); // NamingSystemUniqueIdComponent
          return value;
        case -1233035097: // replacedBy
          this.replacedBy = castToReference(value); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("status")) {
          value = new PublicationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<PublicationStatus>
        } else if (name.equals("kind")) {
          value = new NamingSystemTypeEnumFactory().fromType(castToCode(value));
          this.kind = (Enumeration) value; // Enumeration<NamingSystemType>
        } else if (name.equals("date")) {
          this.date = castToDateTime(value); // DateTimeType
        } else if (name.equals("publisher")) {
          this.publisher = castToString(value); // StringType
        } else if (name.equals("contact")) {
          this.getContact().add(castToContactDetail(value));
        } else if (name.equals("responsible")) {
          this.responsible = castToString(value); // StringType
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("description")) {
          this.description = castToMarkdown(value); // MarkdownType
        } else if (name.equals("useContext")) {
          this.getUseContext().add(castToUsageContext(value));
        } else if (name.equals("jurisdiction")) {
          this.getJurisdiction().add(castToCodeableConcept(value));
        } else if (name.equals("usage")) {
          this.usage = castToString(value); // StringType
        } else if (name.equals("uniqueId")) {
          this.getUniqueId().add((NamingSystemUniqueIdComponent) value);
        } else if (name.equals("replacedBy")) {
          this.replacedBy = castToReference(value); // Reference
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707:  return getNameElement();
        case -892481550:  return getStatusElement();
        case 3292052:  return getKindElement();
        case 3076014:  return getDateElement();
        case 1447404028:  return getPublisherElement();
        case 951526432:  return addContact(); 
        case 1847674614:  return getResponsibleElement();
        case 3575610:  return getType(); 
        case -1724546052:  return getDescriptionElement();
        case -669707736:  return addUseContext(); 
        case -507075711:  return addJurisdiction(); 
        case 111574433:  return getUsageElement();
        case -294460212:  return addUniqueId(); 
        case -1233035097:  return getReplacedBy(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return new String[] {"string"};
        case -892481550: /*status*/ return new String[] {"code"};
        case 3292052: /*kind*/ return new String[] {"code"};
        case 3076014: /*date*/ return new String[] {"dateTime"};
        case 1447404028: /*publisher*/ return new String[] {"string"};
        case 951526432: /*contact*/ return new String[] {"ContactDetail"};
        case 1847674614: /*responsible*/ return new String[] {"string"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -1724546052: /*description*/ return new String[] {"markdown"};
        case -669707736: /*useContext*/ return new String[] {"UsageContext"};
        case -507075711: /*jurisdiction*/ return new String[] {"CodeableConcept"};
        case 111574433: /*usage*/ return new String[] {"string"};
        case -294460212: /*uniqueId*/ return new String[] {};
        case -1233035097: /*replacedBy*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type NamingSystem.name");
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type NamingSystem.status");
        }
        else if (name.equals("kind")) {
          throw new FHIRException("Cannot call addChild on a primitive type NamingSystem.kind");
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type NamingSystem.date");
        }
        else if (name.equals("publisher")) {
          throw new FHIRException("Cannot call addChild on a primitive type NamingSystem.publisher");
        }
        else if (name.equals("contact")) {
          return addContact();
        }
        else if (name.equals("responsible")) {
          throw new FHIRException("Cannot call addChild on a primitive type NamingSystem.responsible");
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type NamingSystem.description");
        }
        else if (name.equals("useContext")) {
          return addUseContext();
        }
        else if (name.equals("jurisdiction")) {
          return addJurisdiction();
        }
        else if (name.equals("usage")) {
          throw new FHIRException("Cannot call addChild on a primitive type NamingSystem.usage");
        }
        else if (name.equals("uniqueId")) {
          return addUniqueId();
        }
        else if (name.equals("replacedBy")) {
          this.replacedBy = new Reference();
          return this.replacedBy;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "NamingSystem";

  }

      public NamingSystem copy() {
        NamingSystem dst = new NamingSystem();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.status = status == null ? null : status.copy();
        dst.kind = kind == null ? null : kind.copy();
        dst.date = date == null ? null : date.copy();
        dst.publisher = publisher == null ? null : publisher.copy();
        if (contact != null) {
          dst.contact = new ArrayList<ContactDetail>();
          for (ContactDetail i : contact)
            dst.contact.add(i.copy());
        };
        dst.responsible = responsible == null ? null : responsible.copy();
        dst.type = type == null ? null : type.copy();
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
        return compareDeep(kind, o.kind, true) && compareDeep(responsible, o.responsible, true) && compareDeep(type, o.type, true)
           && compareDeep(usage, o.usage, true) && compareDeep(uniqueId, o.uniqueId, true) && compareDeep(replacedBy, o.replacedBy, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof NamingSystem))
          return false;
        NamingSystem o = (NamingSystem) other;
        return compareValues(kind, o.kind, true) && compareValues(responsible, o.responsible, true) && compareValues(usage, o.usage, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(kind, responsible, type
          , usage, uniqueId, replacedBy);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.NamingSystem;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>The naming system publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>NamingSystem.date</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="NamingSystem.date", description="The naming system publication date", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>The naming system publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>NamingSystem.date</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>period</b>
   * <p>
   * Description: <b>When is identifier valid?</b><br>
   * Type: <b>date</b><br>
   * Path: <b>NamingSystem.uniqueId.period</b><br>
   * </p>
   */
  @SearchParamDefinition(name="period", path="NamingSystem.uniqueId.period", description="When is identifier valid?", type="date" )
  public static final String SP_PERIOD = "period";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>period</b>
   * <p>
   * Description: <b>When is identifier valid?</b><br>
   * Type: <b>date</b><br>
   * Path: <b>NamingSystem.uniqueId.period</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam PERIOD = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_PERIOD);

 /**
   * Search parameter: <b>kind</b>
   * <p>
   * Description: <b>codesystem | identifier | root</b><br>
   * Type: <b>token</b><br>
   * Path: <b>NamingSystem.kind</b><br>
   * </p>
   */
  @SearchParamDefinition(name="kind", path="NamingSystem.kind", description="codesystem | identifier | root", type="token" )
  public static final String SP_KIND = "kind";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>kind</b>
   * <p>
   * Description: <b>codesystem | identifier | root</b><br>
   * Type: <b>token</b><br>
   * Path: <b>NamingSystem.kind</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam KIND = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_KIND);

 /**
   * Search parameter: <b>jurisdiction</b>
   * <p>
   * Description: <b>Intended jurisdiction for the naming system</b><br>
   * Type: <b>token</b><br>
   * Path: <b>NamingSystem.jurisdiction</b><br>
   * </p>
   */
  @SearchParamDefinition(name="jurisdiction", path="NamingSystem.jurisdiction", description="Intended jurisdiction for the naming system", type="token" )
  public static final String SP_JURISDICTION = "jurisdiction";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>jurisdiction</b>
   * <p>
   * Description: <b>Intended jurisdiction for the naming system</b><br>
   * Type: <b>token</b><br>
   * Path: <b>NamingSystem.jurisdiction</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam JURISDICTION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_JURISDICTION);

 /**
   * Search parameter: <b>description</b>
   * <p>
   * Description: <b>The description of the naming system</b><br>
   * Type: <b>string</b><br>
   * Path: <b>NamingSystem.description</b><br>
   * </p>
   */
  @SearchParamDefinition(name="description", path="NamingSystem.description", description="The description of the naming system", type="string" )
  public static final String SP_DESCRIPTION = "description";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>description</b>
   * <p>
   * Description: <b>The description of the naming system</b><br>
   * Type: <b>string</b><br>
   * Path: <b>NamingSystem.description</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam DESCRIPTION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_DESCRIPTION);

 /**
   * Search parameter: <b>type</b>
   * <p>
   * Description: <b>e.g. driver,  provider,  patient, bank etc.</b><br>
   * Type: <b>token</b><br>
   * Path: <b>NamingSystem.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="type", path="NamingSystem.type", description="e.g. driver,  provider,  patient, bank etc.", type="token" )
  public static final String SP_TYPE = "type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>type</b>
   * <p>
   * Description: <b>e.g. driver,  provider,  patient, bank etc.</b><br>
   * Type: <b>token</b><br>
   * Path: <b>NamingSystem.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TYPE);

 /**
   * Search parameter: <b>id-type</b>
   * <p>
   * Description: <b>oid | uuid | uri | other</b><br>
   * Type: <b>token</b><br>
   * Path: <b>NamingSystem.uniqueId.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="id-type", path="NamingSystem.uniqueId.type", description="oid | uuid | uri | other", type="token" )
  public static final String SP_ID_TYPE = "id-type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>id-type</b>
   * <p>
   * Description: <b>oid | uuid | uri | other</b><br>
   * Type: <b>token</b><br>
   * Path: <b>NamingSystem.uniqueId.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam ID_TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_ID_TYPE);

 /**
   * Search parameter: <b>responsible</b>
   * <p>
   * Description: <b>Who maintains system namespace?</b><br>
   * Type: <b>string</b><br>
   * Path: <b>NamingSystem.responsible</b><br>
   * </p>
   */
  @SearchParamDefinition(name="responsible", path="NamingSystem.responsible", description="Who maintains system namespace?", type="string" )
  public static final String SP_RESPONSIBLE = "responsible";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>responsible</b>
   * <p>
   * Description: <b>Who maintains system namespace?</b><br>
   * Type: <b>string</b><br>
   * Path: <b>NamingSystem.responsible</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam RESPONSIBLE = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_RESPONSIBLE);

 /**
   * Search parameter: <b>contact</b>
   * <p>
   * Description: <b>Name of an individual to contact</b><br>
   * Type: <b>string</b><br>
   * Path: <b>NamingSystem.contact.name</b><br>
   * </p>
   */
  @SearchParamDefinition(name="contact", path="NamingSystem.contact.name", description="Name of an individual to contact", type="string" )
  public static final String SP_CONTACT = "contact";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>contact</b>
   * <p>
   * Description: <b>Name of an individual to contact</b><br>
   * Type: <b>string</b><br>
   * Path: <b>NamingSystem.contact.name</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam CONTACT = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_CONTACT);

 /**
   * Search parameter: <b>name</b>
   * <p>
   * Description: <b>Computationally friendly name of the naming system</b><br>
   * Type: <b>string</b><br>
   * Path: <b>NamingSystem.name</b><br>
   * </p>
   */
  @SearchParamDefinition(name="name", path="NamingSystem.name", description="Computationally friendly name of the naming system", type="string" )
  public static final String SP_NAME = "name";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>name</b>
   * <p>
   * Description: <b>Computationally friendly name of the naming system</b><br>
   * Type: <b>string</b><br>
   * Path: <b>NamingSystem.name</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam NAME = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_NAME);

 /**
   * Search parameter: <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the naming system</b><br>
   * Type: <b>string</b><br>
   * Path: <b>NamingSystem.publisher</b><br>
   * </p>
   */
  @SearchParamDefinition(name="publisher", path="NamingSystem.publisher", description="Name of the publisher of the naming system", type="string" )
  public static final String SP_PUBLISHER = "publisher";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the naming system</b><br>
   * Type: <b>string</b><br>
   * Path: <b>NamingSystem.publisher</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam PUBLISHER = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_PUBLISHER);

 /**
   * Search parameter: <b>telecom</b>
   * <p>
   * Description: <b>Contact details for individual or organization</b><br>
   * Type: <b>token</b><br>
   * Path: <b>NamingSystem.contact.telecom</b><br>
   * </p>
   */
  @SearchParamDefinition(name="telecom", path="NamingSystem.contact.telecom", description="Contact details for individual or organization", type="token" )
  public static final String SP_TELECOM = "telecom";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>telecom</b>
   * <p>
   * Description: <b>Contact details for individual or organization</b><br>
   * Type: <b>token</b><br>
   * Path: <b>NamingSystem.contact.telecom</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TELECOM = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TELECOM);

 /**
   * Search parameter: <b>value</b>
   * <p>
   * Description: <b>The unique identifier</b><br>
   * Type: <b>string</b><br>
   * Path: <b>NamingSystem.uniqueId.value</b><br>
   * </p>
   */
  @SearchParamDefinition(name="value", path="NamingSystem.uniqueId.value", description="The unique identifier", type="string" )
  public static final String SP_VALUE = "value";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>value</b>
   * <p>
   * Description: <b>The unique identifier</b><br>
   * Type: <b>string</b><br>
   * Path: <b>NamingSystem.uniqueId.value</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam VALUE = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_VALUE);

 /**
   * Search parameter: <b>replaced-by</b>
   * <p>
   * Description: <b>Use this instead</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>NamingSystem.replacedBy</b><br>
   * </p>
   */
  @SearchParamDefinition(name="replaced-by", path="NamingSystem.replacedBy", description="Use this instead", type="reference", target={NamingSystem.class } )
  public static final String SP_REPLACED_BY = "replaced-by";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>replaced-by</b>
   * <p>
   * Description: <b>Use this instead</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>NamingSystem.replacedBy</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REPLACED_BY = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REPLACED_BY);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>NamingSystem:replaced-by</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REPLACED_BY = new ca.uhn.fhir.model.api.Include("NamingSystem:replaced-by").toLocked();

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The current status of the naming system</b><br>
   * Type: <b>token</b><br>
   * Path: <b>NamingSystem.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="NamingSystem.status", description="The current status of the naming system", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The current status of the naming system</b><br>
   * Type: <b>token</b><br>
   * Path: <b>NamingSystem.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

