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

// Generated on Sun, May 6, 2018 17:51-0400 for FHIR v3.4.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * Information about a user's current session.
 */
@ResourceDef(name="UserSession", profile="http://hl7.org/fhir/Profile/UserSession")
public class UserSession extends DomainResource {

    public enum UserSessionStatus {
        /**
         * The user session is activating
         */
        ACTIVATING, 
        /**
         * The user session is active
         */
        ACTIVE, 
        /**
         * The user session is suspended
         */
        SUSPENDED, 
        /**
         * The user session is closing
         */
        CLOSING, 
        /**
         * The user session is closed
         */
        CLOSED, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static UserSessionStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("activating".equals(codeString))
          return ACTIVATING;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("suspended".equals(codeString))
          return SUSPENDED;
        if ("closing".equals(codeString))
          return CLOSING;
        if ("closed".equals(codeString))
          return CLOSED;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown UserSessionStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ACTIVATING: return "activating";
            case ACTIVE: return "active";
            case SUSPENDED: return "suspended";
            case CLOSING: return "closing";
            case CLOSED: return "closed";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ACTIVATING: return "http://hl7.org/fhir/usersession-status";
            case ACTIVE: return "http://hl7.org/fhir/usersession-status";
            case SUSPENDED: return "http://hl7.org/fhir/usersession-status";
            case CLOSING: return "http://hl7.org/fhir/usersession-status";
            case CLOSED: return "http://hl7.org/fhir/usersession-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ACTIVATING: return "The user session is activating";
            case ACTIVE: return "The user session is active";
            case SUSPENDED: return "The user session is suspended";
            case CLOSING: return "The user session is closing";
            case CLOSED: return "The user session is closed";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACTIVATING: return "Activating";
            case ACTIVE: return "Active";
            case SUSPENDED: return "Suspending";
            case CLOSING: return "Closing";
            case CLOSED: return "Closed";
            default: return "?";
          }
        }
    }

  public static class UserSessionStatusEnumFactory implements EnumFactory<UserSessionStatus> {
    public UserSessionStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("activating".equals(codeString))
          return UserSessionStatus.ACTIVATING;
        if ("active".equals(codeString))
          return UserSessionStatus.ACTIVE;
        if ("suspended".equals(codeString))
          return UserSessionStatus.SUSPENDED;
        if ("closing".equals(codeString))
          return UserSessionStatus.CLOSING;
        if ("closed".equals(codeString))
          return UserSessionStatus.CLOSED;
        throw new IllegalArgumentException("Unknown UserSessionStatus code '"+codeString+"'");
        }
        public Enumeration<UserSessionStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<UserSessionStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("activating".equals(codeString))
          return new Enumeration<UserSessionStatus>(this, UserSessionStatus.ACTIVATING);
        if ("active".equals(codeString))
          return new Enumeration<UserSessionStatus>(this, UserSessionStatus.ACTIVE);
        if ("suspended".equals(codeString))
          return new Enumeration<UserSessionStatus>(this, UserSessionStatus.SUSPENDED);
        if ("closing".equals(codeString))
          return new Enumeration<UserSessionStatus>(this, UserSessionStatus.CLOSING);
        if ("closed".equals(codeString))
          return new Enumeration<UserSessionStatus>(this, UserSessionStatus.CLOSED);
        throw new FHIRException("Unknown UserSessionStatus code '"+codeString+"'");
        }
    public String toCode(UserSessionStatus code) {
      if (code == UserSessionStatus.ACTIVATING)
        return "activating";
      if (code == UserSessionStatus.ACTIVE)
        return "active";
      if (code == UserSessionStatus.SUSPENDED)
        return "suspended";
      if (code == UserSessionStatus.CLOSING)
        return "closing";
      if (code == UserSessionStatus.CLOSED)
        return "closed";
      return "?";
      }
    public String toSystem(UserSessionStatus code) {
      return code.getSystem();
      }
    }

    public enum UserSessionStatusSource {
        /**
         * The status was reported by the user
         */
        USER, 
        /**
         * The status was reported by the system
         */
        SYSTEM, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static UserSessionStatusSource fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("user".equals(codeString))
          return USER;
        if ("system".equals(codeString))
          return SYSTEM;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown UserSessionStatusSource code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case USER: return "user";
            case SYSTEM: return "system";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case USER: return "http://hl7.org/fhir/usersession-status-source";
            case SYSTEM: return "http://hl7.org/fhir/usersession-status-source";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case USER: return "The status was reported by the user";
            case SYSTEM: return "The status was reported by the system";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case USER: return "User";
            case SYSTEM: return "System";
            default: return "?";
          }
        }
    }

  public static class UserSessionStatusSourceEnumFactory implements EnumFactory<UserSessionStatusSource> {
    public UserSessionStatusSource fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("user".equals(codeString))
          return UserSessionStatusSource.USER;
        if ("system".equals(codeString))
          return UserSessionStatusSource.SYSTEM;
        throw new IllegalArgumentException("Unknown UserSessionStatusSource code '"+codeString+"'");
        }
        public Enumeration<UserSessionStatusSource> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<UserSessionStatusSource>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("user".equals(codeString))
          return new Enumeration<UserSessionStatusSource>(this, UserSessionStatusSource.USER);
        if ("system".equals(codeString))
          return new Enumeration<UserSessionStatusSource>(this, UserSessionStatusSource.SYSTEM);
        throw new FHIRException("Unknown UserSessionStatusSource code '"+codeString+"'");
        }
    public String toCode(UserSessionStatusSource code) {
      if (code == UserSessionStatusSource.USER)
        return "user";
      if (code == UserSessionStatusSource.SYSTEM)
        return "system";
      return "?";
      }
    public String toSystem(UserSessionStatusSource code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class UserSessionStatusComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The current status of the user session.
         */
        @Child(name = "code", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="activating | active | suspended | closing | closed", formalDefinition="The current status of the user session." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/usersession-status")
        protected Enumeration<UserSessionStatus> code;

        /**
         * The source of the status code.
         */
        @Child(name = "source", type = {CodeType.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="user | system", formalDefinition="The source of the status code." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/usersession-status-source")
        protected Enumeration<UserSessionStatusSource> source;

        private static final long serialVersionUID = 2003820253L;

    /**
     * Constructor
     */
      public UserSessionStatusComponent() {
        super();
      }

    /**
     * Constructor
     */
      public UserSessionStatusComponent(Enumeration<UserSessionStatus> code, Enumeration<UserSessionStatusSource> source) {
        super();
        this.code = code;
        this.source = source;
      }

        /**
         * @return {@link #code} (The current status of the user session.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public Enumeration<UserSessionStatus> getCodeElement() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create UserSessionStatusComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new Enumeration<UserSessionStatus>(new UserSessionStatusEnumFactory()); // bb
          return this.code;
        }

        public boolean hasCodeElement() { 
          return this.code != null && !this.code.isEmpty();
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (The current status of the user session.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public UserSessionStatusComponent setCodeElement(Enumeration<UserSessionStatus> value) { 
          this.code = value;
          return this;
        }

        /**
         * @return The current status of the user session.
         */
        public UserSessionStatus getCode() { 
          return this.code == null ? null : this.code.getValue();
        }

        /**
         * @param value The current status of the user session.
         */
        public UserSessionStatusComponent setCode(UserSessionStatus value) { 
            if (this.code == null)
              this.code = new Enumeration<UserSessionStatus>(new UserSessionStatusEnumFactory());
            this.code.setValue(value);
          return this;
        }

        /**
         * @return {@link #source} (The source of the status code.). This is the underlying object with id, value and extensions. The accessor "getSource" gives direct access to the value
         */
        public Enumeration<UserSessionStatusSource> getSourceElement() { 
          if (this.source == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create UserSessionStatusComponent.source");
            else if (Configuration.doAutoCreate())
              this.source = new Enumeration<UserSessionStatusSource>(new UserSessionStatusSourceEnumFactory()); // bb
          return this.source;
        }

        public boolean hasSourceElement() { 
          return this.source != null && !this.source.isEmpty();
        }

        public boolean hasSource() { 
          return this.source != null && !this.source.isEmpty();
        }

        /**
         * @param value {@link #source} (The source of the status code.). This is the underlying object with id, value and extensions. The accessor "getSource" gives direct access to the value
         */
        public UserSessionStatusComponent setSourceElement(Enumeration<UserSessionStatusSource> value) { 
          this.source = value;
          return this;
        }

        /**
         * @return The source of the status code.
         */
        public UserSessionStatusSource getSource() { 
          return this.source == null ? null : this.source.getValue();
        }

        /**
         * @param value The source of the status code.
         */
        public UserSessionStatusComponent setSource(UserSessionStatusSource value) { 
            if (this.source == null)
              this.source = new Enumeration<UserSessionStatusSource>(new UserSessionStatusSourceEnumFactory());
            this.source.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("code", "code", "The current status of the user session.", 0, 1, code));
          children.add(new Property("source", "code", "The source of the status code.", 0, 1, source));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3059181: /*code*/  return new Property("code", "code", "The current status of the user session.", 0, 1, code);
          case -896505829: /*source*/  return new Property("source", "code", "The source of the status code.", 0, 1, source);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // Enumeration<UserSessionStatus>
        case -896505829: /*source*/ return this.source == null ? new Base[0] : new Base[] {this.source}; // Enumeration<UserSessionStatusSource>
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3059181: // code
          value = new UserSessionStatusEnumFactory().fromType(castToCode(value));
          this.code = (Enumeration) value; // Enumeration<UserSessionStatus>
          return value;
        case -896505829: // source
          value = new UserSessionStatusSourceEnumFactory().fromType(castToCode(value));
          this.source = (Enumeration) value; // Enumeration<UserSessionStatusSource>
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code")) {
          value = new UserSessionStatusEnumFactory().fromType(castToCode(value));
          this.code = (Enumeration) value; // Enumeration<UserSessionStatus>
        } else if (name.equals("source")) {
          value = new UserSessionStatusSourceEnumFactory().fromType(castToCode(value));
          this.source = (Enumeration) value; // Enumeration<UserSessionStatusSource>
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181:  return getCodeElement();
        case -896505829:  return getSourceElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return new String[] {"code"};
        case -896505829: /*source*/ return new String[] {"code"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          throw new FHIRException("Cannot call addChild on a primitive type UserSession.code");
        }
        else if (name.equals("source")) {
          throw new FHIRException("Cannot call addChild on a primitive type UserSession.source");
        }
        else
          return super.addChild(name);
      }

      public UserSessionStatusComponent copy() {
        UserSessionStatusComponent dst = new UserSessionStatusComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.source = source == null ? null : source.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof UserSessionStatusComponent))
          return false;
        UserSessionStatusComponent o = (UserSessionStatusComponent) other_;
        return compareDeep(code, o.code, true) && compareDeep(source, o.source, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof UserSessionStatusComponent))
          return false;
        UserSessionStatusComponent o = (UserSessionStatusComponent) other_;
        return compareValues(code, o.code, true) && compareValues(source, o.source, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(code, source);
      }

  public String fhirType() {
    return "UserSession.status";

  }

  }

    @Block()
    public static class UserSessionContextComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Indicates which type of context is being provided.
         */
        @Child(name = "type", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="What type of context value", formalDefinition="Indicates which type of context is being provided." )
        protected StringType type;

        /**
         * Provides the context value.
         */
        @Child(name = "value", type = {CodeableConcept.class, Quantity.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Value of the context", formalDefinition="Provides the context value." )
        protected Type value;

        private static final long serialVersionUID = -1035059584L;

    /**
     * Constructor
     */
      public UserSessionContextComponent() {
        super();
      }

    /**
     * Constructor
     */
      public UserSessionContextComponent(StringType type) {
        super();
        this.type = type;
      }

        /**
         * @return {@link #type} (Indicates which type of context is being provided.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public StringType getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create UserSessionContextComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new StringType(); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Indicates which type of context is being provided.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public UserSessionContextComponent setTypeElement(StringType value) { 
          this.type = value;
          return this;
        }

        /**
         * @return Indicates which type of context is being provided.
         */
        public String getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value Indicates which type of context is being provided.
         */
        public UserSessionContextComponent setType(String value) { 
            if (this.type == null)
              this.type = new StringType();
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #value} (Provides the context value.)
         */
        public Type getValue() { 
          return this.value;
        }

        /**
         * @return {@link #value} (Provides the context value.)
         */
        public CodeableConcept getValueCodeableConcept() throws FHIRException { 
          if (this.value == null)
            return null;
          if (!(this.value instanceof CodeableConcept))
            throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.value.getClass().getName()+" was encountered");
          return (CodeableConcept) this.value;
        }

        public boolean hasValueCodeableConcept() { 
          return this != null && this.value instanceof CodeableConcept;
        }

        /**
         * @return {@link #value} (Provides the context value.)
         */
        public Quantity getValueQuantity() throws FHIRException { 
          if (this.value == null)
            return null;
          if (!(this.value instanceof Quantity))
            throw new FHIRException("Type mismatch: the type Quantity was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Quantity) this.value;
        }

        public boolean hasValueQuantity() { 
          return this != null && this.value instanceof Quantity;
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (Provides the context value.)
         */
        public UserSessionContextComponent setValue(Type value) { 
          if (value != null && !(value instanceof CodeableConcept || value instanceof Quantity))
            throw new Error("Not the right type for UserSession.context.value[x]: "+value.fhirType());
          this.value = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("type", "string", "Indicates which type of context is being provided.", 0, 1, type));
          children.add(new Property("value[x]", "CodeableConcept|Quantity", "Provides the context value.", 0, 1, value));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3575610: /*type*/  return new Property("type", "string", "Indicates which type of context is being provided.", 0, 1, type);
          case -1410166417: /*value[x]*/  return new Property("value[x]", "CodeableConcept|Quantity", "Provides the context value.", 0, 1, value);
          case 111972721: /*value*/  return new Property("value[x]", "CodeableConcept|Quantity", "Provides the context value.", 0, 1, value);
          case 924902896: /*valueCodeableConcept*/  return new Property("value[x]", "CodeableConcept|Quantity", "Provides the context value.", 0, 1, value);
          case -2029823716: /*valueQuantity*/  return new Property("value[x]", "CodeableConcept|Quantity", "Provides the context value.", 0, 1, value);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // StringType
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToString(value); // StringType
          return value;
        case 111972721: // value
          this.value = castToType(value); // Type
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToString(value); // StringType
        } else if (name.equals("value[x]")) {
          this.value = castToType(value); // Type
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getTypeElement();
        case -1410166417:  return getValue(); 
        case 111972721:  return getValue(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"string"};
        case 111972721: /*value*/ return new String[] {"CodeableConcept", "Quantity"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type UserSession.type");
        }
        else if (name.equals("valueCodeableConcept")) {
          this.value = new CodeableConcept();
          return this.value;
        }
        else if (name.equals("valueQuantity")) {
          this.value = new Quantity();
          return this.value;
        }
        else
          return super.addChild(name);
      }

      public UserSessionContextComponent copy() {
        UserSessionContextComponent dst = new UserSessionContextComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof UserSessionContextComponent))
          return false;
        UserSessionContextComponent o = (UserSessionContextComponent) other_;
        return compareDeep(type, o.type, true) && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof UserSessionContextComponent))
          return false;
        UserSessionContextComponent o = (UserSessionContextComponent) other_;
        return compareValues(type, o.type, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, value);
      }

  public String fhirType() {
    return "UserSession.context";

  }

  }

    /**
     * Allows a service to provide a unique, business identifier for the session.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Business identifier", formalDefinition="Allows a service to provide a unique, business identifier for the session." )
    protected Identifier identifier;

    /**
     * A practitioner, patient, device, or related person engaged in the session.
     */
    @Child(name = "user", type = {Device.class, Practitioner.class, Patient.class, RelatedPerson.class}, order=1, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="User engaged in the session", formalDefinition="A practitioner, patient, device, or related person engaged in the session." )
    protected Reference user;

    /**
     * The actual object that is the target of the reference (A practitioner, patient, device, or related person engaged in the session.)
     */
    protected Resource userTarget;

    /**
     * Status of the session.
     */
    @Child(name = "status", type = {}, order=2, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Status of the session", formalDefinition="Status of the session." )
    protected UserSessionStatusComponent status;

    /**
     * Location that identifies the physical place at which the user's session is occurring. For the purposes of context synchronization, this is intended to represent the user's workstation.
     */
    @Child(name = "workstation", type = {Identifier.class}, order=3, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Where is the session", formalDefinition="Location that identifies the physical place at which the user's session is occurring. For the purposes of context synchronization, this is intended to represent the user's workstation." )
    protected Identifier workstation;

    /**
     * The current focus of the user's session. Common values are a reference to a Patient, Encounter, ImagingStudy, etc.
     */
    @Child(name = "focus", type = {Reference.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="What is the user's current focus", formalDefinition="The current focus of the user's session. Common values are a reference to a Patient, Encounter, ImagingStudy, etc." )
    protected List<Reference> focus;
    /**
     * The actual objects that are the target of the reference (The current focus of the user's session. Common values are a reference to a Patient, Encounter, ImagingStudy, etc.)
     */
    protected List<Resource> focusTarget;


    /**
     * Indicates the timestamp when the user session was first created.
     */
    @Child(name = "created", type = {InstantType.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="When was the session created", formalDefinition="Indicates the timestamp when the user session was first created." )
    protected InstantType created;

    /**
     * Indicates the timestamp when the user session will expire (i.e. no longer be valid).
     */
    @Child(name = "expires", type = {InstantType.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When does the session expire", formalDefinition="Indicates the timestamp when the user session will expire (i.e. no longer be valid)." )
    protected InstantType expires;

    /**
     * Provides additional information associated with the context.
     */
    @Child(name = "context", type = {}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Additional information about the session", formalDefinition="Provides additional information associated with the context." )
    protected List<UserSessionContextComponent> context;

    private static final long serialVersionUID = -1422979558L;

  /**
   * Constructor
   */
    public UserSession() {
      super();
    }

  /**
   * Constructor
   */
    public UserSession(Reference user) {
      super();
      this.user = user;
    }

    /**
     * @return {@link #identifier} (Allows a service to provide a unique, business identifier for the session.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create UserSession.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new Identifier(); // cc
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (Allows a service to provide a unique, business identifier for the session.)
     */
    public UserSession setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #user} (A practitioner, patient, device, or related person engaged in the session.)
     */
    public Reference getUser() { 
      if (this.user == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create UserSession.user");
        else if (Configuration.doAutoCreate())
          this.user = new Reference(); // cc
      return this.user;
    }

    public boolean hasUser() { 
      return this.user != null && !this.user.isEmpty();
    }

    /**
     * @param value {@link #user} (A practitioner, patient, device, or related person engaged in the session.)
     */
    public UserSession setUser(Reference value) { 
      this.user = value;
      return this;
    }

    /**
     * @return {@link #user} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A practitioner, patient, device, or related person engaged in the session.)
     */
    public Resource getUserTarget() { 
      return this.userTarget;
    }

    /**
     * @param value {@link #user} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A practitioner, patient, device, or related person engaged in the session.)
     */
    public UserSession setUserTarget(Resource value) { 
      this.userTarget = value;
      return this;
    }

    /**
     * @return {@link #status} (Status of the session.)
     */
    public UserSessionStatusComponent getStatus() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create UserSession.status");
        else if (Configuration.doAutoCreate())
          this.status = new UserSessionStatusComponent(); // cc
      return this.status;
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (Status of the session.)
     */
    public UserSession setStatus(UserSessionStatusComponent value) { 
      this.status = value;
      return this;
    }

    /**
     * @return {@link #workstation} (Location that identifies the physical place at which the user's session is occurring. For the purposes of context synchronization, this is intended to represent the user's workstation.)
     */
    public Identifier getWorkstation() { 
      if (this.workstation == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create UserSession.workstation");
        else if (Configuration.doAutoCreate())
          this.workstation = new Identifier(); // cc
      return this.workstation;
    }

    public boolean hasWorkstation() { 
      return this.workstation != null && !this.workstation.isEmpty();
    }

    /**
     * @param value {@link #workstation} (Location that identifies the physical place at which the user's session is occurring. For the purposes of context synchronization, this is intended to represent the user's workstation.)
     */
    public UserSession setWorkstation(Identifier value) { 
      this.workstation = value;
      return this;
    }

    /**
     * @return {@link #focus} (The current focus of the user's session. Common values are a reference to a Patient, Encounter, ImagingStudy, etc.)
     */
    public List<Reference> getFocus() { 
      if (this.focus == null)
        this.focus = new ArrayList<Reference>();
      return this.focus;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public UserSession setFocus(List<Reference> theFocus) { 
      this.focus = theFocus;
      return this;
    }

    public boolean hasFocus() { 
      if (this.focus == null)
        return false;
      for (Reference item : this.focus)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addFocus() { //3
      Reference t = new Reference();
      if (this.focus == null)
        this.focus = new ArrayList<Reference>();
      this.focus.add(t);
      return t;
    }

    public UserSession addFocus(Reference t) { //3
      if (t == null)
        return this;
      if (this.focus == null)
        this.focus = new ArrayList<Reference>();
      this.focus.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #focus}, creating it if it does not already exist
     */
    public Reference getFocusFirstRep() { 
      if (getFocus().isEmpty()) {
        addFocus();
      }
      return getFocus().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getFocusTarget() { 
      if (this.focusTarget == null)
        this.focusTarget = new ArrayList<Resource>();
      return this.focusTarget;
    }

    /**
     * @return {@link #created} (Indicates the timestamp when the user session was first created.). This is the underlying object with id, value and extensions. The accessor "getCreated" gives direct access to the value
     */
    public InstantType getCreatedElement() { 
      if (this.created == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create UserSession.created");
        else if (Configuration.doAutoCreate())
          this.created = new InstantType(); // bb
      return this.created;
    }

    public boolean hasCreatedElement() { 
      return this.created != null && !this.created.isEmpty();
    }

    public boolean hasCreated() { 
      return this.created != null && !this.created.isEmpty();
    }

    /**
     * @param value {@link #created} (Indicates the timestamp when the user session was first created.). This is the underlying object with id, value and extensions. The accessor "getCreated" gives direct access to the value
     */
    public UserSession setCreatedElement(InstantType value) { 
      this.created = value;
      return this;
    }

    /**
     * @return Indicates the timestamp when the user session was first created.
     */
    public Date getCreated() { 
      return this.created == null ? null : this.created.getValue();
    }

    /**
     * @param value Indicates the timestamp when the user session was first created.
     */
    public UserSession setCreated(Date value) { 
      if (value == null)
        this.created = null;
      else {
        if (this.created == null)
          this.created = new InstantType();
        this.created.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #expires} (Indicates the timestamp when the user session will expire (i.e. no longer be valid).). This is the underlying object with id, value and extensions. The accessor "getExpires" gives direct access to the value
     */
    public InstantType getExpiresElement() { 
      if (this.expires == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create UserSession.expires");
        else if (Configuration.doAutoCreate())
          this.expires = new InstantType(); // bb
      return this.expires;
    }

    public boolean hasExpiresElement() { 
      return this.expires != null && !this.expires.isEmpty();
    }

    public boolean hasExpires() { 
      return this.expires != null && !this.expires.isEmpty();
    }

    /**
     * @param value {@link #expires} (Indicates the timestamp when the user session will expire (i.e. no longer be valid).). This is the underlying object with id, value and extensions. The accessor "getExpires" gives direct access to the value
     */
    public UserSession setExpiresElement(InstantType value) { 
      this.expires = value;
      return this;
    }

    /**
     * @return Indicates the timestamp when the user session will expire (i.e. no longer be valid).
     */
    public Date getExpires() { 
      return this.expires == null ? null : this.expires.getValue();
    }

    /**
     * @param value Indicates the timestamp when the user session will expire (i.e. no longer be valid).
     */
    public UserSession setExpires(Date value) { 
      if (value == null)
        this.expires = null;
      else {
        if (this.expires == null)
          this.expires = new InstantType();
        this.expires.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #context} (Provides additional information associated with the context.)
     */
    public List<UserSessionContextComponent> getContext() { 
      if (this.context == null)
        this.context = new ArrayList<UserSessionContextComponent>();
      return this.context;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public UserSession setContext(List<UserSessionContextComponent> theContext) { 
      this.context = theContext;
      return this;
    }

    public boolean hasContext() { 
      if (this.context == null)
        return false;
      for (UserSessionContextComponent item : this.context)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public UserSessionContextComponent addContext() { //3
      UserSessionContextComponent t = new UserSessionContextComponent();
      if (this.context == null)
        this.context = new ArrayList<UserSessionContextComponent>();
      this.context.add(t);
      return t;
    }

    public UserSession addContext(UserSessionContextComponent t) { //3
      if (t == null)
        return this;
      if (this.context == null)
        this.context = new ArrayList<UserSessionContextComponent>();
      this.context.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #context}, creating it if it does not already exist
     */
    public UserSessionContextComponent getContextFirstRep() { 
      if (getContext().isEmpty()) {
        addContext();
      }
      return getContext().get(0);
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("identifier", "Identifier", "Allows a service to provide a unique, business identifier for the session.", 0, 1, identifier));
        children.add(new Property("user", "Reference(Device|Practitioner|Patient|RelatedPerson)", "A practitioner, patient, device, or related person engaged in the session.", 0, 1, user));
        children.add(new Property("status", "", "Status of the session.", 0, 1, status));
        children.add(new Property("workstation", "Identifier", "Location that identifies the physical place at which the user's session is occurring. For the purposes of context synchronization, this is intended to represent the user's workstation.", 0, 1, workstation));
        children.add(new Property("focus", "Reference(Any)", "The current focus of the user's session. Common values are a reference to a Patient, Encounter, ImagingStudy, etc.", 0, java.lang.Integer.MAX_VALUE, focus));
        children.add(new Property("created", "instant", "Indicates the timestamp when the user session was first created.", 0, 1, created));
        children.add(new Property("expires", "instant", "Indicates the timestamp when the user session will expire (i.e. no longer be valid).", 0, 1, expires));
        children.add(new Property("context", "", "Provides additional information associated with the context.", 0, java.lang.Integer.MAX_VALUE, context));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "Allows a service to provide a unique, business identifier for the session.", 0, 1, identifier);
        case 3599307: /*user*/  return new Property("user", "Reference(Device|Practitioner|Patient|RelatedPerson)", "A practitioner, patient, device, or related person engaged in the session.", 0, 1, user);
        case -892481550: /*status*/  return new Property("status", "", "Status of the session.", 0, 1, status);
        case 581588227: /*workstation*/  return new Property("workstation", "Identifier", "Location that identifies the physical place at which the user's session is occurring. For the purposes of context synchronization, this is intended to represent the user's workstation.", 0, 1, workstation);
        case 97604824: /*focus*/  return new Property("focus", "Reference(Any)", "The current focus of the user's session. Common values are a reference to a Patient, Encounter, ImagingStudy, etc.", 0, java.lang.Integer.MAX_VALUE, focus);
        case 1028554472: /*created*/  return new Property("created", "instant", "Indicates the timestamp when the user session was first created.", 0, 1, created);
        case -1309235404: /*expires*/  return new Property("expires", "instant", "Indicates the timestamp when the user session will expire (i.e. no longer be valid).", 0, 1, expires);
        case 951530927: /*context*/  return new Property("context", "", "Provides additional information associated with the context.", 0, java.lang.Integer.MAX_VALUE, context);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case 3599307: /*user*/ return this.user == null ? new Base[0] : new Base[] {this.user}; // Reference
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // UserSessionStatusComponent
        case 581588227: /*workstation*/ return this.workstation == null ? new Base[0] : new Base[] {this.workstation}; // Identifier
        case 97604824: /*focus*/ return this.focus == null ? new Base[0] : this.focus.toArray(new Base[this.focus.size()]); // Reference
        case 1028554472: /*created*/ return this.created == null ? new Base[0] : new Base[] {this.created}; // InstantType
        case -1309235404: /*expires*/ return this.expires == null ? new Base[0] : new Base[] {this.expires}; // InstantType
        case 951530927: /*context*/ return this.context == null ? new Base[0] : this.context.toArray(new Base[this.context.size()]); // UserSessionContextComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          return value;
        case 3599307: // user
          this.user = castToReference(value); // Reference
          return value;
        case -892481550: // status
          this.status = (UserSessionStatusComponent) value; // UserSessionStatusComponent
          return value;
        case 581588227: // workstation
          this.workstation = castToIdentifier(value); // Identifier
          return value;
        case 97604824: // focus
          this.getFocus().add(castToReference(value)); // Reference
          return value;
        case 1028554472: // created
          this.created = castToInstant(value); // InstantType
          return value;
        case -1309235404: // expires
          this.expires = castToInstant(value); // InstantType
          return value;
        case 951530927: // context
          this.getContext().add((UserSessionContextComponent) value); // UserSessionContextComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = castToIdentifier(value); // Identifier
        } else if (name.equals("user")) {
          this.user = castToReference(value); // Reference
        } else if (name.equals("status")) {
          this.status = (UserSessionStatusComponent) value; // UserSessionStatusComponent
        } else if (name.equals("workstation")) {
          this.workstation = castToIdentifier(value); // Identifier
        } else if (name.equals("focus")) {
          this.getFocus().add(castToReference(value));
        } else if (name.equals("created")) {
          this.created = castToInstant(value); // InstantType
        } else if (name.equals("expires")) {
          this.expires = castToInstant(value); // InstantType
        } else if (name.equals("context")) {
          this.getContext().add((UserSessionContextComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return getIdentifier(); 
        case 3599307:  return getUser(); 
        case -892481550:  return getStatus(); 
        case 581588227:  return getWorkstation(); 
        case 97604824:  return addFocus(); 
        case 1028554472:  return getCreatedElement();
        case -1309235404:  return getExpiresElement();
        case 951530927:  return addContext(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case 3599307: /*user*/ return new String[] {"Reference"};
        case -892481550: /*status*/ return new String[] {};
        case 581588227: /*workstation*/ return new String[] {"Identifier"};
        case 97604824: /*focus*/ return new String[] {"Reference"};
        case 1028554472: /*created*/ return new String[] {"instant"};
        case -1309235404: /*expires*/ return new String[] {"instant"};
        case 951530927: /*context*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("user")) {
          this.user = new Reference();
          return this.user;
        }
        else if (name.equals("status")) {
          this.status = new UserSessionStatusComponent();
          return this.status;
        }
        else if (name.equals("workstation")) {
          this.workstation = new Identifier();
          return this.workstation;
        }
        else if (name.equals("focus")) {
          return addFocus();
        }
        else if (name.equals("created")) {
          throw new FHIRException("Cannot call addChild on a primitive type UserSession.created");
        }
        else if (name.equals("expires")) {
          throw new FHIRException("Cannot call addChild on a primitive type UserSession.expires");
        }
        else if (name.equals("context")) {
          return addContext();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "UserSession";

  }

      public UserSession copy() {
        UserSession dst = new UserSession();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.user = user == null ? null : user.copy();
        dst.status = status == null ? null : status.copy();
        dst.workstation = workstation == null ? null : workstation.copy();
        if (focus != null) {
          dst.focus = new ArrayList<Reference>();
          for (Reference i : focus)
            dst.focus.add(i.copy());
        };
        dst.created = created == null ? null : created.copy();
        dst.expires = expires == null ? null : expires.copy();
        if (context != null) {
          dst.context = new ArrayList<UserSessionContextComponent>();
          for (UserSessionContextComponent i : context)
            dst.context.add(i.copy());
        };
        return dst;
      }

      protected UserSession typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof UserSession))
          return false;
        UserSession o = (UserSession) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(user, o.user, true) && compareDeep(status, o.status, true)
           && compareDeep(workstation, o.workstation, true) && compareDeep(focus, o.focus, true) && compareDeep(created, o.created, true)
           && compareDeep(expires, o.expires, true) && compareDeep(context, o.context, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof UserSession))
          return false;
        UserSession o = (UserSession) other_;
        return compareValues(created, o.created, true) && compareValues(expires, o.expires, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, user, status
          , workstation, focus, created, expires, context);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.UserSession;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>External identifiers for the user session</b><br>
   * Type: <b>token</b><br>
   * Path: <b>UserSession.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="UserSession.identifier", description="External identifiers for the user session", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>External identifiers for the user session</b><br>
   * Type: <b>token</b><br>
   * Path: <b>UserSession.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>The identity of a patient to search for user sessions</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>UserSession.focus</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="UserSession.focus", description="The identity of a patient to search for user sessions", type="reference", target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>The identity of a patient to search for user sessions</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>UserSession.focus</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>UserSession:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("UserSession:patient").toLocked();

 /**
   * Search parameter: <b>focus</b>
   * <p>
   * Description: <b>The focus of the user session</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>UserSession.focus</b><br>
   * </p>
   */
  @SearchParamDefinition(name="focus", path="UserSession.focus", description="The focus of the user session", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Encounter"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") } )
  public static final String SP_FOCUS = "focus";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>focus</b>
   * <p>
   * Description: <b>The focus of the user session</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>UserSession.focus</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam FOCUS = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_FOCUS);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>UserSession:focus</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_FOCUS = new ca.uhn.fhir.model.api.Include("UserSession:focus").toLocked();

 /**
   * Search parameter: <b>workstation</b>
   * <p>
   * Description: <b>The workstation of the session</b><br>
   * Type: <b>token</b><br>
   * Path: <b>UserSession.workstation</b><br>
   * </p>
   */
  @SearchParamDefinition(name="workstation", path="UserSession.workstation", description="The workstation of the session", type="token" )
  public static final String SP_WORKSTATION = "workstation";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>workstation</b>
   * <p>
   * Description: <b>The workstation of the session</b><br>
   * Type: <b>token</b><br>
   * Path: <b>UserSession.workstation</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam WORKSTATION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_WORKSTATION);

 /**
   * Search parameter: <b>user</b>
   * <p>
   * Description: <b>The user of the session</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>UserSession.user</b><br>
   * </p>
   */
  @SearchParamDefinition(name="user", path="UserSession.user", description="The user of the session", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Device"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner"), @ca.uhn.fhir.model.api.annotation.Compartment(name="RelatedPerson") }, target={Device.class, Patient.class, Practitioner.class, RelatedPerson.class } )
  public static final String SP_USER = "user";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>user</b>
   * <p>
   * Description: <b>The user of the session</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>UserSession.user</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam USER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_USER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>UserSession:user</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_USER = new ca.uhn.fhir.model.api.Include("UserSession:user").toLocked();

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The status of the user session</b><br>
   * Type: <b>token</b><br>
   * Path: <b>UserSession.status.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="UserSession.status.code", description="The status of the user session", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The status of the user session</b><br>
   * Type: <b>token</b><br>
   * Path: <b>UserSession.status.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

