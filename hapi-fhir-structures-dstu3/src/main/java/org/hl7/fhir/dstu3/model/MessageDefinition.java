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
 * Defines the characteristics of a message that can be shared between systems, including the type of event that initiates the message, the content to be transmitted and what response(s), if any, are permitted.
 */
@ResourceDef(name="MessageDefinition", profile="http://hl7.org/fhir/Profile/MessageDefinition")
@ChildOrder(names={"url", "identifier", "version", "name", "title", "status", "experimental", "date", "publisher", "contact", "description", "useContext", "jurisdiction", "purpose", "copyright", "base", "parent", "replaces", "event", "category", "focus", "responseRequired", "allowedResponse"})
public class MessageDefinition extends MetadataResource {

    public enum MessageSignificanceCategory {
        /**
         * The message represents/requests a change that should not be processed more than once; e.g., making a booking for an appointment.
         */
        CONSEQUENCE, 
        /**
         * The message represents a response to query for current information. Retrospective processing is wrong and/or wasteful.
         */
        CURRENCY, 
        /**
         * The content is not necessarily intended to be current, and it can be reprocessed, though there may be version issues created by processing old notifications.
         */
        NOTIFICATION, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static MessageSignificanceCategory fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("Consequence".equals(codeString))
          return CONSEQUENCE;
        if ("Currency".equals(codeString))
          return CURRENCY;
        if ("Notification".equals(codeString))
          return NOTIFICATION;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown MessageSignificanceCategory code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CONSEQUENCE: return "Consequence";
            case CURRENCY: return "Currency";
            case NOTIFICATION: return "Notification";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case CONSEQUENCE: return "http://hl7.org/fhir/message-significance-category";
            case CURRENCY: return "http://hl7.org/fhir/message-significance-category";
            case NOTIFICATION: return "http://hl7.org/fhir/message-significance-category";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case CONSEQUENCE: return "The message represents/requests a change that should not be processed more than once; e.g., making a booking for an appointment.";
            case CURRENCY: return "The message represents a response to query for current information. Retrospective processing is wrong and/or wasteful.";
            case NOTIFICATION: return "The content is not necessarily intended to be current, and it can be reprocessed, though there may be version issues created by processing old notifications.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CONSEQUENCE: return "Consequence";
            case CURRENCY: return "Currency";
            case NOTIFICATION: return "Notification";
            default: return "?";
          }
        }
    }

  public static class MessageSignificanceCategoryEnumFactory implements EnumFactory<MessageSignificanceCategory> {
    public MessageSignificanceCategory fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("Consequence".equals(codeString))
          return MessageSignificanceCategory.CONSEQUENCE;
        if ("Currency".equals(codeString))
          return MessageSignificanceCategory.CURRENCY;
        if ("Notification".equals(codeString))
          return MessageSignificanceCategory.NOTIFICATION;
        throw new IllegalArgumentException("Unknown MessageSignificanceCategory code '"+codeString+"'");
        }
        public Enumeration<MessageSignificanceCategory> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<MessageSignificanceCategory>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("Consequence".equals(codeString))
          return new Enumeration<MessageSignificanceCategory>(this, MessageSignificanceCategory.CONSEQUENCE);
        if ("Currency".equals(codeString))
          return new Enumeration<MessageSignificanceCategory>(this, MessageSignificanceCategory.CURRENCY);
        if ("Notification".equals(codeString))
          return new Enumeration<MessageSignificanceCategory>(this, MessageSignificanceCategory.NOTIFICATION);
        throw new FHIRException("Unknown MessageSignificanceCategory code '"+codeString+"'");
        }
    public String toCode(MessageSignificanceCategory code) {
      if (code == MessageSignificanceCategory.CONSEQUENCE)
        return "Consequence";
      if (code == MessageSignificanceCategory.CURRENCY)
        return "Currency";
      if (code == MessageSignificanceCategory.NOTIFICATION)
        return "Notification";
      return "?";
      }
    public String toSystem(MessageSignificanceCategory code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class MessageDefinitionFocusComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The kind of resource that must be the focus for this message.
         */
        @Child(name = "code", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Type of resource", formalDefinition="The kind of resource that must be the focus for this message." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/resource-types")
        protected CodeType code;

        /**
         * A profile that reflects constraints for the focal resource (and potentially for related resources).
         */
        @Child(name = "profile", type = {StructureDefinition.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Profile that must be adhered to by focus", formalDefinition="A profile that reflects constraints for the focal resource (and potentially for related resources)." )
        protected Reference profile;

        /**
         * The actual object that is the target of the reference (A profile that reflects constraints for the focal resource (and potentially for related resources).)
         */
        protected StructureDefinition profileTarget;

        /**
         * Identifies the minimum number of resources of this type that must be pointed to by a message in order for it to be valid against this MessageDefinition.
         */
        @Child(name = "min", type = {UnsignedIntType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Minimum number of focuses of this type", formalDefinition="Identifies the minimum number of resources of this type that must be pointed to by a message in order for it to be valid against this MessageDefinition." )
        protected UnsignedIntType min;

        /**
         * Identifies the maximum number of resources of this type that must be pointed to by a message in order for it to be valid against this MessageDefinition.
         */
        @Child(name = "max", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Maximum number of focuses of this type", formalDefinition="Identifies the maximum number of resources of this type that must be pointed to by a message in order for it to be valid against this MessageDefinition." )
        protected StringType max;

        private static final long serialVersionUID = 35658543L;

    /**
     * Constructor
     */
      public MessageDefinitionFocusComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MessageDefinitionFocusComponent(CodeType code) {
        super();
        this.code = code;
      }

        /**
         * @return {@link #code} (The kind of resource that must be the focus for this message.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public CodeType getCodeElement() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MessageDefinitionFocusComponent.code");
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
         * @param value {@link #code} (The kind of resource that must be the focus for this message.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public MessageDefinitionFocusComponent setCodeElement(CodeType value) { 
          this.code = value;
          return this;
        }

        /**
         * @return The kind of resource that must be the focus for this message.
         */
        public String getCode() { 
          return this.code == null ? null : this.code.getValue();
        }

        /**
         * @param value The kind of resource that must be the focus for this message.
         */
        public MessageDefinitionFocusComponent setCode(String value) { 
            if (this.code == null)
              this.code = new CodeType();
            this.code.setValue(value);
          return this;
        }

        /**
         * @return {@link #profile} (A profile that reflects constraints for the focal resource (and potentially for related resources).)
         */
        public Reference getProfile() { 
          if (this.profile == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MessageDefinitionFocusComponent.profile");
            else if (Configuration.doAutoCreate())
              this.profile = new Reference(); // cc
          return this.profile;
        }

        public boolean hasProfile() { 
          return this.profile != null && !this.profile.isEmpty();
        }

        /**
         * @param value {@link #profile} (A profile that reflects constraints for the focal resource (and potentially for related resources).)
         */
        public MessageDefinitionFocusComponent setProfile(Reference value) { 
          this.profile = value;
          return this;
        }

        /**
         * @return {@link #profile} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A profile that reflects constraints for the focal resource (and potentially for related resources).)
         */
        public StructureDefinition getProfileTarget() { 
          if (this.profileTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MessageDefinitionFocusComponent.profile");
            else if (Configuration.doAutoCreate())
              this.profileTarget = new StructureDefinition(); // aa
          return this.profileTarget;
        }

        /**
         * @param value {@link #profile} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A profile that reflects constraints for the focal resource (and potentially for related resources).)
         */
        public MessageDefinitionFocusComponent setProfileTarget(StructureDefinition value) { 
          this.profileTarget = value;
          return this;
        }

        /**
         * @return {@link #min} (Identifies the minimum number of resources of this type that must be pointed to by a message in order for it to be valid against this MessageDefinition.). This is the underlying object with id, value and extensions. The accessor "getMin" gives direct access to the value
         */
        public UnsignedIntType getMinElement() { 
          if (this.min == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MessageDefinitionFocusComponent.min");
            else if (Configuration.doAutoCreate())
              this.min = new UnsignedIntType(); // bb
          return this.min;
        }

        public boolean hasMinElement() { 
          return this.min != null && !this.min.isEmpty();
        }

        public boolean hasMin() { 
          return this.min != null && !this.min.isEmpty();
        }

        /**
         * @param value {@link #min} (Identifies the minimum number of resources of this type that must be pointed to by a message in order for it to be valid against this MessageDefinition.). This is the underlying object with id, value and extensions. The accessor "getMin" gives direct access to the value
         */
        public MessageDefinitionFocusComponent setMinElement(UnsignedIntType value) { 
          this.min = value;
          return this;
        }

        /**
         * @return Identifies the minimum number of resources of this type that must be pointed to by a message in order for it to be valid against this MessageDefinition.
         */
        public int getMin() { 
          return this.min == null || this.min.isEmpty() ? 0 : this.min.getValue();
        }

        /**
         * @param value Identifies the minimum number of resources of this type that must be pointed to by a message in order for it to be valid against this MessageDefinition.
         */
        public MessageDefinitionFocusComponent setMin(int value) { 
            if (this.min == null)
              this.min = new UnsignedIntType();
            this.min.setValue(value);
          return this;
        }

        /**
         * @return {@link #max} (Identifies the maximum number of resources of this type that must be pointed to by a message in order for it to be valid against this MessageDefinition.). This is the underlying object with id, value and extensions. The accessor "getMax" gives direct access to the value
         */
        public StringType getMaxElement() { 
          if (this.max == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MessageDefinitionFocusComponent.max");
            else if (Configuration.doAutoCreate())
              this.max = new StringType(); // bb
          return this.max;
        }

        public boolean hasMaxElement() { 
          return this.max != null && !this.max.isEmpty();
        }

        public boolean hasMax() { 
          return this.max != null && !this.max.isEmpty();
        }

        /**
         * @param value {@link #max} (Identifies the maximum number of resources of this type that must be pointed to by a message in order for it to be valid against this MessageDefinition.). This is the underlying object with id, value and extensions. The accessor "getMax" gives direct access to the value
         */
        public MessageDefinitionFocusComponent setMaxElement(StringType value) { 
          this.max = value;
          return this;
        }

        /**
         * @return Identifies the maximum number of resources of this type that must be pointed to by a message in order for it to be valid against this MessageDefinition.
         */
        public String getMax() { 
          return this.max == null ? null : this.max.getValue();
        }

        /**
         * @param value Identifies the maximum number of resources of this type that must be pointed to by a message in order for it to be valid against this MessageDefinition.
         */
        public MessageDefinitionFocusComponent setMax(String value) { 
          if (Utilities.noString(value))
            this.max = null;
          else {
            if (this.max == null)
              this.max = new StringType();
            this.max.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("code", "code", "The kind of resource that must be the focus for this message.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("profile", "Reference(StructureDefinition)", "A profile that reflects constraints for the focal resource (and potentially for related resources).", 0, java.lang.Integer.MAX_VALUE, profile));
          childrenList.add(new Property("min", "unsignedInt", "Identifies the minimum number of resources of this type that must be pointed to by a message in order for it to be valid against this MessageDefinition.", 0, java.lang.Integer.MAX_VALUE, min));
          childrenList.add(new Property("max", "string", "Identifies the maximum number of resources of this type that must be pointed to by a message in order for it to be valid against this MessageDefinition.", 0, java.lang.Integer.MAX_VALUE, max));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeType
        case -309425751: /*profile*/ return this.profile == null ? new Base[0] : new Base[] {this.profile}; // Reference
        case 108114: /*min*/ return this.min == null ? new Base[0] : new Base[] {this.min}; // UnsignedIntType
        case 107876: /*max*/ return this.max == null ? new Base[0] : new Base[] {this.max}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3059181: // code
          this.code = castToCode(value); // CodeType
          return value;
        case -309425751: // profile
          this.profile = castToReference(value); // Reference
          return value;
        case 108114: // min
          this.min = castToUnsignedInt(value); // UnsignedIntType
          return value;
        case 107876: // max
          this.max = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code")) {
          this.code = castToCode(value); // CodeType
        } else if (name.equals("profile")) {
          this.profile = castToReference(value); // Reference
        } else if (name.equals("min")) {
          this.min = castToUnsignedInt(value); // UnsignedIntType
        } else if (name.equals("max")) {
          this.max = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181:  return getCodeElement();
        case -309425751:  return getProfile(); 
        case 108114:  return getMinElement();
        case 107876:  return getMaxElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return new String[] {"code"};
        case -309425751: /*profile*/ return new String[] {"Reference"};
        case 108114: /*min*/ return new String[] {"unsignedInt"};
        case 107876: /*max*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          throw new FHIRException("Cannot call addChild on a primitive type MessageDefinition.code");
        }
        else if (name.equals("profile")) {
          this.profile = new Reference();
          return this.profile;
        }
        else if (name.equals("min")) {
          throw new FHIRException("Cannot call addChild on a primitive type MessageDefinition.min");
        }
        else if (name.equals("max")) {
          throw new FHIRException("Cannot call addChild on a primitive type MessageDefinition.max");
        }
        else
          return super.addChild(name);
      }

      public MessageDefinitionFocusComponent copy() {
        MessageDefinitionFocusComponent dst = new MessageDefinitionFocusComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.profile = profile == null ? null : profile.copy();
        dst.min = min == null ? null : min.copy();
        dst.max = max == null ? null : max.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof MessageDefinitionFocusComponent))
          return false;
        MessageDefinitionFocusComponent o = (MessageDefinitionFocusComponent) other;
        return compareDeep(code, o.code, true) && compareDeep(profile, o.profile, true) && compareDeep(min, o.min, true)
           && compareDeep(max, o.max, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MessageDefinitionFocusComponent))
          return false;
        MessageDefinitionFocusComponent o = (MessageDefinitionFocusComponent) other;
        return compareValues(code, o.code, true) && compareValues(min, o.min, true) && compareValues(max, o.max, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(code, profile, min, max
          );
      }

  public String fhirType() {
    return "MessageDefinition.focus";

  }

  }

    @Block()
    public static class MessageDefinitionAllowedResponseComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A reference to the message definition that must be adhered to by this supported response.
         */
        @Child(name = "message", type = {MessageDefinition.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Reference to allowed message definition response", formalDefinition="A reference to the message definition that must be adhered to by this supported response." )
        protected Reference message;

        /**
         * The actual object that is the target of the reference (A reference to the message definition that must be adhered to by this supported response.)
         */
        protected MessageDefinition messageTarget;

        /**
         * Provides a description of the circumstances in which this response should be used (as opposed to one of the alternative responses).
         */
        @Child(name = "situation", type = {MarkdownType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="When should this response be used", formalDefinition="Provides a description of the circumstances in which this response should be used (as opposed to one of the alternative responses)." )
        protected MarkdownType situation;

        private static final long serialVersionUID = 825230127L;

    /**
     * Constructor
     */
      public MessageDefinitionAllowedResponseComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MessageDefinitionAllowedResponseComponent(Reference message) {
        super();
        this.message = message;
      }

        /**
         * @return {@link #message} (A reference to the message definition that must be adhered to by this supported response.)
         */
        public Reference getMessage() { 
          if (this.message == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MessageDefinitionAllowedResponseComponent.message");
            else if (Configuration.doAutoCreate())
              this.message = new Reference(); // cc
          return this.message;
        }

        public boolean hasMessage() { 
          return this.message != null && !this.message.isEmpty();
        }

        /**
         * @param value {@link #message} (A reference to the message definition that must be adhered to by this supported response.)
         */
        public MessageDefinitionAllowedResponseComponent setMessage(Reference value) { 
          this.message = value;
          return this;
        }

        /**
         * @return {@link #message} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A reference to the message definition that must be adhered to by this supported response.)
         */
        public MessageDefinition getMessageTarget() { 
          if (this.messageTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MessageDefinitionAllowedResponseComponent.message");
            else if (Configuration.doAutoCreate())
              this.messageTarget = new MessageDefinition(); // aa
          return this.messageTarget;
        }

        /**
         * @param value {@link #message} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A reference to the message definition that must be adhered to by this supported response.)
         */
        public MessageDefinitionAllowedResponseComponent setMessageTarget(MessageDefinition value) { 
          this.messageTarget = value;
          return this;
        }

        /**
         * @return {@link #situation} (Provides a description of the circumstances in which this response should be used (as opposed to one of the alternative responses).). This is the underlying object with id, value and extensions. The accessor "getSituation" gives direct access to the value
         */
        public MarkdownType getSituationElement() { 
          if (this.situation == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MessageDefinitionAllowedResponseComponent.situation");
            else if (Configuration.doAutoCreate())
              this.situation = new MarkdownType(); // bb
          return this.situation;
        }

        public boolean hasSituationElement() { 
          return this.situation != null && !this.situation.isEmpty();
        }

        public boolean hasSituation() { 
          return this.situation != null && !this.situation.isEmpty();
        }

        /**
         * @param value {@link #situation} (Provides a description of the circumstances in which this response should be used (as opposed to one of the alternative responses).). This is the underlying object with id, value and extensions. The accessor "getSituation" gives direct access to the value
         */
        public MessageDefinitionAllowedResponseComponent setSituationElement(MarkdownType value) { 
          this.situation = value;
          return this;
        }

        /**
         * @return Provides a description of the circumstances in which this response should be used (as opposed to one of the alternative responses).
         */
        public String getSituation() { 
          return this.situation == null ? null : this.situation.getValue();
        }

        /**
         * @param value Provides a description of the circumstances in which this response should be used (as opposed to one of the alternative responses).
         */
        public MessageDefinitionAllowedResponseComponent setSituation(String value) { 
          if (value == null)
            this.situation = null;
          else {
            if (this.situation == null)
              this.situation = new MarkdownType();
            this.situation.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("message", "Reference(MessageDefinition)", "A reference to the message definition that must be adhered to by this supported response.", 0, java.lang.Integer.MAX_VALUE, message));
          childrenList.add(new Property("situation", "markdown", "Provides a description of the circumstances in which this response should be used (as opposed to one of the alternative responses).", 0, java.lang.Integer.MAX_VALUE, situation));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 954925063: /*message*/ return this.message == null ? new Base[0] : new Base[] {this.message}; // Reference
        case -73377282: /*situation*/ return this.situation == null ? new Base[0] : new Base[] {this.situation}; // MarkdownType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 954925063: // message
          this.message = castToReference(value); // Reference
          return value;
        case -73377282: // situation
          this.situation = castToMarkdown(value); // MarkdownType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("message")) {
          this.message = castToReference(value); // Reference
        } else if (name.equals("situation")) {
          this.situation = castToMarkdown(value); // MarkdownType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 954925063:  return getMessage(); 
        case -73377282:  return getSituationElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 954925063: /*message*/ return new String[] {"Reference"};
        case -73377282: /*situation*/ return new String[] {"markdown"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("message")) {
          this.message = new Reference();
          return this.message;
        }
        else if (name.equals("situation")) {
          throw new FHIRException("Cannot call addChild on a primitive type MessageDefinition.situation");
        }
        else
          return super.addChild(name);
      }

      public MessageDefinitionAllowedResponseComponent copy() {
        MessageDefinitionAllowedResponseComponent dst = new MessageDefinitionAllowedResponseComponent();
        copyValues(dst);
        dst.message = message == null ? null : message.copy();
        dst.situation = situation == null ? null : situation.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof MessageDefinitionAllowedResponseComponent))
          return false;
        MessageDefinitionAllowedResponseComponent o = (MessageDefinitionAllowedResponseComponent) other;
        return compareDeep(message, o.message, true) && compareDeep(situation, o.situation, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MessageDefinitionAllowedResponseComponent))
          return false;
        MessageDefinitionAllowedResponseComponent o = (MessageDefinitionAllowedResponseComponent) other;
        return compareValues(situation, o.situation, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(message, situation);
      }

  public String fhirType() {
    return "MessageDefinition.allowedResponse";

  }

  }

    /**
     * A formal identifier that is used to identify this message definition when it is represented in other formats, or referenced in a specification, model, design or an instance.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Additional identifier for the message definition", formalDefinition="A formal identifier that is used to identify this message definition when it is represented in other formats, or referenced in a specification, model, design or an instance." )
    protected Identifier identifier;

    /**
     * Explaination of why this message definition is needed and why it has been designed as it has.
     */
    @Child(name = "purpose", type = {MarkdownType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Why this message definition is defined", formalDefinition="Explaination of why this message definition is needed and why it has been designed as it has." )
    protected MarkdownType purpose;

    /**
     * A copyright statement relating to the message definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the message definition.
     */
    @Child(name = "copyright", type = {MarkdownType.class}, order=2, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Use and/or publishing restrictions", formalDefinition="A copyright statement relating to the message definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the message definition." )
    protected MarkdownType copyright;

    /**
     * The MessageDefinition that is the basis for the contents of this resource.
     */
    @Child(name = "base", type = {MessageDefinition.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Definition this one is based on", formalDefinition="The MessageDefinition that is the basis for the contents of this resource." )
    protected Reference base;

    /**
     * The actual object that is the target of the reference (The MessageDefinition that is the basis for the contents of this resource.)
     */
    protected MessageDefinition baseTarget;

    /**
     * Identifies a protocol or workflow that this MessageDefinition represents a step in.
     */
    @Child(name = "parent", type = {ActivityDefinition.class, PlanDefinition.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Protocol/workflow this is part of", formalDefinition="Identifies a protocol or workflow that this MessageDefinition represents a step in." )
    protected List<Reference> parent;
    /**
     * The actual objects that are the target of the reference (Identifies a protocol or workflow that this MessageDefinition represents a step in.)
     */
    protected List<Resource> parentTarget;


    /**
     * A MessageDefinition that is superseded by this definition.
     */
    @Child(name = "replaces", type = {MessageDefinition.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Takes the place of", formalDefinition="A MessageDefinition that is superseded by this definition." )
    protected List<Reference> replaces;
    /**
     * The actual objects that are the target of the reference (A MessageDefinition that is superseded by this definition.)
     */
    protected List<MessageDefinition> replacesTarget;


    /**
     * A coded identifier of a supported messaging event.
     */
    @Child(name = "event", type = {Coding.class}, order=6, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Event type", formalDefinition="A coded identifier of a supported messaging event." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/message-events")
    protected Coding event;

    /**
     * The impact of the content of the message.
     */
    @Child(name = "category", type = {CodeType.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Consequence | Currency | Notification", formalDefinition="The impact of the content of the message." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/message-significance-category")
    protected Enumeration<MessageSignificanceCategory> category;

    /**
     * Identifies the resource (or resources) that are being addressed by the event.  For example, the Encounter for an admit message or two Account records for a merge.
     */
    @Child(name = "focus", type = {}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Resource(s) that are the subject of the event", formalDefinition="Identifies the resource (or resources) that are being addressed by the event.  For example, the Encounter for an admit message or two Account records for a merge." )
    protected List<MessageDefinitionFocusComponent> focus;

    /**
     * Indicates whether a response is required for this message.
     */
    @Child(name = "responseRequired", type = {BooleanType.class}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Is a response required?", formalDefinition="Indicates whether a response is required for this message." )
    protected BooleanType responseRequired;

    /**
     * Indicates what types of messages may be sent as an application-level response to this message.
     */
    @Child(name = "allowedResponse", type = {}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Responses to this message", formalDefinition="Indicates what types of messages may be sent as an application-level response to this message." )
    protected List<MessageDefinitionAllowedResponseComponent> allowedResponse;

    private static final long serialVersionUID = -219916580L;

  /**
   * Constructor
   */
    public MessageDefinition() {
      super();
    }

  /**
   * Constructor
   */
    public MessageDefinition(Enumeration<PublicationStatus> status, DateTimeType date, Coding event) {
      super();
      this.status = status;
      this.date = date;
      this.event = event;
    }

    /**
     * @return {@link #url} (An absolute URI that is used to identify this message definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this message definition is (or will be) published. The URL SHOULD include the major version of the message definition. For more information see [Technical and Business Versions](resource.html#versions).). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public UriType getUrlElement() { 
      if (this.url == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MessageDefinition.url");
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
     * @param value {@link #url} (An absolute URI that is used to identify this message definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this message definition is (or will be) published. The URL SHOULD include the major version of the message definition. For more information see [Technical and Business Versions](resource.html#versions).). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public MessageDefinition setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return An absolute URI that is used to identify this message definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this message definition is (or will be) published. The URL SHOULD include the major version of the message definition. For more information see [Technical and Business Versions](resource.html#versions).
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value An absolute URI that is used to identify this message definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this message definition is (or will be) published. The URL SHOULD include the major version of the message definition. For more information see [Technical and Business Versions](resource.html#versions).
     */
    public MessageDefinition setUrl(String value) { 
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
     * @return {@link #identifier} (A formal identifier that is used to identify this message definition when it is represented in other formats, or referenced in a specification, model, design or an instance.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MessageDefinition.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new Identifier(); // cc
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (A formal identifier that is used to identify this message definition when it is represented in other formats, or referenced in a specification, model, design or an instance.)
     */
    public MessageDefinition setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #version} (The identifier that is used to identify this version of the message definition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the message definition author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public StringType getVersionElement() { 
      if (this.version == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MessageDefinition.version");
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
     * @param value {@link #version} (The identifier that is used to identify this version of the message definition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the message definition author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public MessageDefinition setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return The identifier that is used to identify this version of the message definition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the message definition author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value The identifier that is used to identify this version of the message definition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the message definition author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.
     */
    public MessageDefinition setVersion(String value) { 
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
     * @return {@link #name} (A natural language name identifying the message definition. This name should be usable as an identifier for the module by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MessageDefinition.name");
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
     * @param value {@link #name} (A natural language name identifying the message definition. This name should be usable as an identifier for the module by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public MessageDefinition setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return A natural language name identifying the message definition. This name should be usable as an identifier for the module by machine processing applications such as code generation.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value A natural language name identifying the message definition. This name should be usable as an identifier for the module by machine processing applications such as code generation.
     */
    public MessageDefinition setName(String value) { 
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
     * @return {@link #title} (A short, descriptive, user-friendly title for the message definition.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public StringType getTitleElement() { 
      if (this.title == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MessageDefinition.title");
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
     * @param value {@link #title} (A short, descriptive, user-friendly title for the message definition.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public MessageDefinition setTitleElement(StringType value) { 
      this.title = value;
      return this;
    }

    /**
     * @return A short, descriptive, user-friendly title for the message definition.
     */
    public String getTitle() { 
      return this.title == null ? null : this.title.getValue();
    }

    /**
     * @param value A short, descriptive, user-friendly title for the message definition.
     */
    public MessageDefinition setTitle(String value) { 
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
     * @return {@link #status} (The status of this message definition. Enables tracking the life-cycle of the content.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<PublicationStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MessageDefinition.status");
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
     * @param value {@link #status} (The status of this message definition. Enables tracking the life-cycle of the content.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public MessageDefinition setStatusElement(Enumeration<PublicationStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of this message definition. Enables tracking the life-cycle of the content.
     */
    public PublicationStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of this message definition. Enables tracking the life-cycle of the content.
     */
    public MessageDefinition setStatus(PublicationStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<PublicationStatus>(new PublicationStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #experimental} (A boolean value to indicate that this message definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public BooleanType getExperimentalElement() { 
      if (this.experimental == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MessageDefinition.experimental");
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
     * @param value {@link #experimental} (A boolean value to indicate that this message definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public MessageDefinition setExperimentalElement(BooleanType value) { 
      this.experimental = value;
      return this;
    }

    /**
     * @return A boolean value to indicate that this message definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public boolean getExperimental() { 
      return this.experimental == null || this.experimental.isEmpty() ? false : this.experimental.getValue();
    }

    /**
     * @param value A boolean value to indicate that this message definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public MessageDefinition setExperimental(boolean value) { 
        if (this.experimental == null)
          this.experimental = new BooleanType();
        this.experimental.setValue(value);
      return this;
    }

    /**
     * @return {@link #date} (The date  (and optionally time) when the message definition was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the message definition changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MessageDefinition.date");
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
     * @param value {@link #date} (The date  (and optionally time) when the message definition was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the message definition changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public MessageDefinition setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date  (and optionally time) when the message definition was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the message definition changes.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date  (and optionally time) when the message definition was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the message definition changes.
     */
    public MessageDefinition setDate(Date value) { 
        if (this.date == null)
          this.date = new DateTimeType();
        this.date.setValue(value);
      return this;
    }

    /**
     * @return {@link #publisher} (The name of the individual or organization that published the message definition.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public StringType getPublisherElement() { 
      if (this.publisher == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MessageDefinition.publisher");
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
     * @param value {@link #publisher} (The name of the individual or organization that published the message definition.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public MessageDefinition setPublisherElement(StringType value) { 
      this.publisher = value;
      return this;
    }

    /**
     * @return The name of the individual or organization that published the message definition.
     */
    public String getPublisher() { 
      return this.publisher == null ? null : this.publisher.getValue();
    }

    /**
     * @param value The name of the individual or organization that published the message definition.
     */
    public MessageDefinition setPublisher(String value) { 
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
    public MessageDefinition setContact(List<ContactDetail> theContact) { 
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

    public MessageDefinition addContact(ContactDetail t) { //3
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
     * @return {@link #description} (A free text natural language description of the message definition from a consumer's perspective.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public MarkdownType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MessageDefinition.description");
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
     * @param value {@link #description} (A free text natural language description of the message definition from a consumer's perspective.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public MessageDefinition setDescriptionElement(MarkdownType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A free text natural language description of the message definition from a consumer's perspective.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A free text natural language description of the message definition from a consumer's perspective.
     */
    public MessageDefinition setDescription(String value) { 
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
     * @return {@link #useContext} (The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching for appropriate message definition instances.)
     */
    public List<UsageContext> getUseContext() { 
      if (this.useContext == null)
        this.useContext = new ArrayList<UsageContext>();
      return this.useContext;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MessageDefinition setUseContext(List<UsageContext> theUseContext) { 
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

    public MessageDefinition addUseContext(UsageContext t) { //3
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
     * @return {@link #jurisdiction} (A legal or geographic region in which the message definition is intended to be used.)
     */
    public List<CodeableConcept> getJurisdiction() { 
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      return this.jurisdiction;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MessageDefinition setJurisdiction(List<CodeableConcept> theJurisdiction) { 
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

    public MessageDefinition addJurisdiction(CodeableConcept t) { //3
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
     * @return {@link #purpose} (Explaination of why this message definition is needed and why it has been designed as it has.). This is the underlying object with id, value and extensions. The accessor "getPurpose" gives direct access to the value
     */
    public MarkdownType getPurposeElement() { 
      if (this.purpose == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MessageDefinition.purpose");
        else if (Configuration.doAutoCreate())
          this.purpose = new MarkdownType(); // bb
      return this.purpose;
    }

    public boolean hasPurposeElement() { 
      return this.purpose != null && !this.purpose.isEmpty();
    }

    public boolean hasPurpose() { 
      return this.purpose != null && !this.purpose.isEmpty();
    }

    /**
     * @param value {@link #purpose} (Explaination of why this message definition is needed and why it has been designed as it has.). This is the underlying object with id, value and extensions. The accessor "getPurpose" gives direct access to the value
     */
    public MessageDefinition setPurposeElement(MarkdownType value) { 
      this.purpose = value;
      return this;
    }

    /**
     * @return Explaination of why this message definition is needed and why it has been designed as it has.
     */
    public String getPurpose() { 
      return this.purpose == null ? null : this.purpose.getValue();
    }

    /**
     * @param value Explaination of why this message definition is needed and why it has been designed as it has.
     */
    public MessageDefinition setPurpose(String value) { 
      if (value == null)
        this.purpose = null;
      else {
        if (this.purpose == null)
          this.purpose = new MarkdownType();
        this.purpose.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #copyright} (A copyright statement relating to the message definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the message definition.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public MarkdownType getCopyrightElement() { 
      if (this.copyright == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MessageDefinition.copyright");
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
     * @param value {@link #copyright} (A copyright statement relating to the message definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the message definition.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public MessageDefinition setCopyrightElement(MarkdownType value) { 
      this.copyright = value;
      return this;
    }

    /**
     * @return A copyright statement relating to the message definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the message definition.
     */
    public String getCopyright() { 
      return this.copyright == null ? null : this.copyright.getValue();
    }

    /**
     * @param value A copyright statement relating to the message definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the message definition.
     */
    public MessageDefinition setCopyright(String value) { 
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
     * @return {@link #base} (The MessageDefinition that is the basis for the contents of this resource.)
     */
    public Reference getBase() { 
      if (this.base == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MessageDefinition.base");
        else if (Configuration.doAutoCreate())
          this.base = new Reference(); // cc
      return this.base;
    }

    public boolean hasBase() { 
      return this.base != null && !this.base.isEmpty();
    }

    /**
     * @param value {@link #base} (The MessageDefinition that is the basis for the contents of this resource.)
     */
    public MessageDefinition setBase(Reference value) { 
      this.base = value;
      return this;
    }

    /**
     * @return {@link #base} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The MessageDefinition that is the basis for the contents of this resource.)
     */
    public MessageDefinition getBaseTarget() { 
      if (this.baseTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MessageDefinition.base");
        else if (Configuration.doAutoCreate())
          this.baseTarget = new MessageDefinition(); // aa
      return this.baseTarget;
    }

    /**
     * @param value {@link #base} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The MessageDefinition that is the basis for the contents of this resource.)
     */
    public MessageDefinition setBaseTarget(MessageDefinition value) { 
      this.baseTarget = value;
      return this;
    }

    /**
     * @return {@link #parent} (Identifies a protocol or workflow that this MessageDefinition represents a step in.)
     */
    public List<Reference> getParent() { 
      if (this.parent == null)
        this.parent = new ArrayList<Reference>();
      return this.parent;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MessageDefinition setParent(List<Reference> theParent) { 
      this.parent = theParent;
      return this;
    }

    public boolean hasParent() { 
      if (this.parent == null)
        return false;
      for (Reference item : this.parent)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addParent() { //3
      Reference t = new Reference();
      if (this.parent == null)
        this.parent = new ArrayList<Reference>();
      this.parent.add(t);
      return t;
    }

    public MessageDefinition addParent(Reference t) { //3
      if (t == null)
        return this;
      if (this.parent == null)
        this.parent = new ArrayList<Reference>();
      this.parent.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #parent}, creating it if it does not already exist
     */
    public Reference getParentFirstRep() { 
      if (getParent().isEmpty()) {
        addParent();
      }
      return getParent().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getParentTarget() { 
      if (this.parentTarget == null)
        this.parentTarget = new ArrayList<Resource>();
      return this.parentTarget;
    }

    /**
     * @return {@link #replaces} (A MessageDefinition that is superseded by this definition.)
     */
    public List<Reference> getReplaces() { 
      if (this.replaces == null)
        this.replaces = new ArrayList<Reference>();
      return this.replaces;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MessageDefinition setReplaces(List<Reference> theReplaces) { 
      this.replaces = theReplaces;
      return this;
    }

    public boolean hasReplaces() { 
      if (this.replaces == null)
        return false;
      for (Reference item : this.replaces)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addReplaces() { //3
      Reference t = new Reference();
      if (this.replaces == null)
        this.replaces = new ArrayList<Reference>();
      this.replaces.add(t);
      return t;
    }

    public MessageDefinition addReplaces(Reference t) { //3
      if (t == null)
        return this;
      if (this.replaces == null)
        this.replaces = new ArrayList<Reference>();
      this.replaces.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #replaces}, creating it if it does not already exist
     */
    public Reference getReplacesFirstRep() { 
      if (getReplaces().isEmpty()) {
        addReplaces();
      }
      return getReplaces().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<MessageDefinition> getReplacesTarget() { 
      if (this.replacesTarget == null)
        this.replacesTarget = new ArrayList<MessageDefinition>();
      return this.replacesTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public MessageDefinition addReplacesTarget() { 
      MessageDefinition r = new MessageDefinition();
      if (this.replacesTarget == null)
        this.replacesTarget = new ArrayList<MessageDefinition>();
      this.replacesTarget.add(r);
      return r;
    }

    /**
     * @return {@link #event} (A coded identifier of a supported messaging event.)
     */
    public Coding getEvent() { 
      if (this.event == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MessageDefinition.event");
        else if (Configuration.doAutoCreate())
          this.event = new Coding(); // cc
      return this.event;
    }

    public boolean hasEvent() { 
      return this.event != null && !this.event.isEmpty();
    }

    /**
     * @param value {@link #event} (A coded identifier of a supported messaging event.)
     */
    public MessageDefinition setEvent(Coding value) { 
      this.event = value;
      return this;
    }

    /**
     * @return {@link #category} (The impact of the content of the message.). This is the underlying object with id, value and extensions. The accessor "getCategory" gives direct access to the value
     */
    public Enumeration<MessageSignificanceCategory> getCategoryElement() { 
      if (this.category == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MessageDefinition.category");
        else if (Configuration.doAutoCreate())
          this.category = new Enumeration<MessageSignificanceCategory>(new MessageSignificanceCategoryEnumFactory()); // bb
      return this.category;
    }

    public boolean hasCategoryElement() { 
      return this.category != null && !this.category.isEmpty();
    }

    public boolean hasCategory() { 
      return this.category != null && !this.category.isEmpty();
    }

    /**
     * @param value {@link #category} (The impact of the content of the message.). This is the underlying object with id, value and extensions. The accessor "getCategory" gives direct access to the value
     */
    public MessageDefinition setCategoryElement(Enumeration<MessageSignificanceCategory> value) { 
      this.category = value;
      return this;
    }

    /**
     * @return The impact of the content of the message.
     */
    public MessageSignificanceCategory getCategory() { 
      return this.category == null ? null : this.category.getValue();
    }

    /**
     * @param value The impact of the content of the message.
     */
    public MessageDefinition setCategory(MessageSignificanceCategory value) { 
      if (value == null)
        this.category = null;
      else {
        if (this.category == null)
          this.category = new Enumeration<MessageSignificanceCategory>(new MessageSignificanceCategoryEnumFactory());
        this.category.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #focus} (Identifies the resource (or resources) that are being addressed by the event.  For example, the Encounter for an admit message or two Account records for a merge.)
     */
    public List<MessageDefinitionFocusComponent> getFocus() { 
      if (this.focus == null)
        this.focus = new ArrayList<MessageDefinitionFocusComponent>();
      return this.focus;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MessageDefinition setFocus(List<MessageDefinitionFocusComponent> theFocus) { 
      this.focus = theFocus;
      return this;
    }

    public boolean hasFocus() { 
      if (this.focus == null)
        return false;
      for (MessageDefinitionFocusComponent item : this.focus)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public MessageDefinitionFocusComponent addFocus() { //3
      MessageDefinitionFocusComponent t = new MessageDefinitionFocusComponent();
      if (this.focus == null)
        this.focus = new ArrayList<MessageDefinitionFocusComponent>();
      this.focus.add(t);
      return t;
    }

    public MessageDefinition addFocus(MessageDefinitionFocusComponent t) { //3
      if (t == null)
        return this;
      if (this.focus == null)
        this.focus = new ArrayList<MessageDefinitionFocusComponent>();
      this.focus.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #focus}, creating it if it does not already exist
     */
    public MessageDefinitionFocusComponent getFocusFirstRep() { 
      if (getFocus().isEmpty()) {
        addFocus();
      }
      return getFocus().get(0);
    }

    /**
     * @return {@link #responseRequired} (Indicates whether a response is required for this message.). This is the underlying object with id, value and extensions. The accessor "getResponseRequired" gives direct access to the value
     */
    public BooleanType getResponseRequiredElement() { 
      if (this.responseRequired == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MessageDefinition.responseRequired");
        else if (Configuration.doAutoCreate())
          this.responseRequired = new BooleanType(); // bb
      return this.responseRequired;
    }

    public boolean hasResponseRequiredElement() { 
      return this.responseRequired != null && !this.responseRequired.isEmpty();
    }

    public boolean hasResponseRequired() { 
      return this.responseRequired != null && !this.responseRequired.isEmpty();
    }

    /**
     * @param value {@link #responseRequired} (Indicates whether a response is required for this message.). This is the underlying object with id, value and extensions. The accessor "getResponseRequired" gives direct access to the value
     */
    public MessageDefinition setResponseRequiredElement(BooleanType value) { 
      this.responseRequired = value;
      return this;
    }

    /**
     * @return Indicates whether a response is required for this message.
     */
    public boolean getResponseRequired() { 
      return this.responseRequired == null || this.responseRequired.isEmpty() ? false : this.responseRequired.getValue();
    }

    /**
     * @param value Indicates whether a response is required for this message.
     */
    public MessageDefinition setResponseRequired(boolean value) { 
        if (this.responseRequired == null)
          this.responseRequired = new BooleanType();
        this.responseRequired.setValue(value);
      return this;
    }

    /**
     * @return {@link #allowedResponse} (Indicates what types of messages may be sent as an application-level response to this message.)
     */
    public List<MessageDefinitionAllowedResponseComponent> getAllowedResponse() { 
      if (this.allowedResponse == null)
        this.allowedResponse = new ArrayList<MessageDefinitionAllowedResponseComponent>();
      return this.allowedResponse;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MessageDefinition setAllowedResponse(List<MessageDefinitionAllowedResponseComponent> theAllowedResponse) { 
      this.allowedResponse = theAllowedResponse;
      return this;
    }

    public boolean hasAllowedResponse() { 
      if (this.allowedResponse == null)
        return false;
      for (MessageDefinitionAllowedResponseComponent item : this.allowedResponse)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public MessageDefinitionAllowedResponseComponent addAllowedResponse() { //3
      MessageDefinitionAllowedResponseComponent t = new MessageDefinitionAllowedResponseComponent();
      if (this.allowedResponse == null)
        this.allowedResponse = new ArrayList<MessageDefinitionAllowedResponseComponent>();
      this.allowedResponse.add(t);
      return t;
    }

    public MessageDefinition addAllowedResponse(MessageDefinitionAllowedResponseComponent t) { //3
      if (t == null)
        return this;
      if (this.allowedResponse == null)
        this.allowedResponse = new ArrayList<MessageDefinitionAllowedResponseComponent>();
      this.allowedResponse.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #allowedResponse}, creating it if it does not already exist
     */
    public MessageDefinitionAllowedResponseComponent getAllowedResponseFirstRep() { 
      if (getAllowedResponse().isEmpty()) {
        addAllowedResponse();
      }
      return getAllowedResponse().get(0);
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("url", "uri", "An absolute URI that is used to identify this message definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this message definition is (or will be) published. The URL SHOULD include the major version of the message definition. For more information see [Technical and Business Versions](resource.html#versions).", 0, java.lang.Integer.MAX_VALUE, url));
        childrenList.add(new Property("identifier", "Identifier", "A formal identifier that is used to identify this message definition when it is represented in other formats, or referenced in a specification, model, design or an instance.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("version", "string", "The identifier that is used to identify this version of the message definition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the message definition author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.", 0, java.lang.Integer.MAX_VALUE, version));
        childrenList.add(new Property("name", "string", "A natural language name identifying the message definition. This name should be usable as an identifier for the module by machine processing applications such as code generation.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("title", "string", "A short, descriptive, user-friendly title for the message definition.", 0, java.lang.Integer.MAX_VALUE, title));
        childrenList.add(new Property("status", "code", "The status of this message definition. Enables tracking the life-cycle of the content.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("experimental", "boolean", "A boolean value to indicate that this message definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.", 0, java.lang.Integer.MAX_VALUE, experimental));
        childrenList.add(new Property("date", "dateTime", "The date  (and optionally time) when the message definition was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the message definition changes.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("publisher", "string", "The name of the individual or organization that published the message definition.", 0, java.lang.Integer.MAX_VALUE, publisher));
        childrenList.add(new Property("contact", "ContactDetail", "Contact details to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact));
        childrenList.add(new Property("description", "markdown", "A free text natural language description of the message definition from a consumer's perspective.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("useContext", "UsageContext", "The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching for appropriate message definition instances.", 0, java.lang.Integer.MAX_VALUE, useContext));
        childrenList.add(new Property("jurisdiction", "CodeableConcept", "A legal or geographic region in which the message definition is intended to be used.", 0, java.lang.Integer.MAX_VALUE, jurisdiction));
        childrenList.add(new Property("purpose", "markdown", "Explaination of why this message definition is needed and why it has been designed as it has.", 0, java.lang.Integer.MAX_VALUE, purpose));
        childrenList.add(new Property("copyright", "markdown", "A copyright statement relating to the message definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the message definition.", 0, java.lang.Integer.MAX_VALUE, copyright));
        childrenList.add(new Property("base", "Reference(MessageDefinition)", "The MessageDefinition that is the basis for the contents of this resource.", 0, java.lang.Integer.MAX_VALUE, base));
        childrenList.add(new Property("parent", "Reference(ActivityDefinition|PlanDefinition)", "Identifies a protocol or workflow that this MessageDefinition represents a step in.", 0, java.lang.Integer.MAX_VALUE, parent));
        childrenList.add(new Property("replaces", "Reference(MessageDefinition)", "A MessageDefinition that is superseded by this definition.", 0, java.lang.Integer.MAX_VALUE, replaces));
        childrenList.add(new Property("event", "Coding", "A coded identifier of a supported messaging event.", 0, java.lang.Integer.MAX_VALUE, event));
        childrenList.add(new Property("category", "code", "The impact of the content of the message.", 0, java.lang.Integer.MAX_VALUE, category));
        childrenList.add(new Property("focus", "", "Identifies the resource (or resources) that are being addressed by the event.  For example, the Encounter for an admit message or two Account records for a merge.", 0, java.lang.Integer.MAX_VALUE, focus));
        childrenList.add(new Property("responseRequired", "boolean", "Indicates whether a response is required for this message.", 0, java.lang.Integer.MAX_VALUE, responseRequired));
        childrenList.add(new Property("allowedResponse", "", "Indicates what types of messages may be sent as an application-level response to this message.", 0, java.lang.Integer.MAX_VALUE, allowedResponse));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case 351608024: /*version*/ return this.version == null ? new Base[0] : new Base[] {this.version}; // StringType
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case 110371416: /*title*/ return this.title == null ? new Base[0] : new Base[] {this.title}; // StringType
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<PublicationStatus>
        case -404562712: /*experimental*/ return this.experimental == null ? new Base[0] : new Base[] {this.experimental}; // BooleanType
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateTimeType
        case 1447404028: /*publisher*/ return this.publisher == null ? new Base[0] : new Base[] {this.publisher}; // StringType
        case 951526432: /*contact*/ return this.contact == null ? new Base[0] : this.contact.toArray(new Base[this.contact.size()]); // ContactDetail
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // MarkdownType
        case -669707736: /*useContext*/ return this.useContext == null ? new Base[0] : this.useContext.toArray(new Base[this.useContext.size()]); // UsageContext
        case -507075711: /*jurisdiction*/ return this.jurisdiction == null ? new Base[0] : this.jurisdiction.toArray(new Base[this.jurisdiction.size()]); // CodeableConcept
        case -220463842: /*purpose*/ return this.purpose == null ? new Base[0] : new Base[] {this.purpose}; // MarkdownType
        case 1522889671: /*copyright*/ return this.copyright == null ? new Base[0] : new Base[] {this.copyright}; // MarkdownType
        case 3016401: /*base*/ return this.base == null ? new Base[0] : new Base[] {this.base}; // Reference
        case -995424086: /*parent*/ return this.parent == null ? new Base[0] : this.parent.toArray(new Base[this.parent.size()]); // Reference
        case -430332865: /*replaces*/ return this.replaces == null ? new Base[0] : this.replaces.toArray(new Base[this.replaces.size()]); // Reference
        case 96891546: /*event*/ return this.event == null ? new Base[0] : new Base[] {this.event}; // Coding
        case 50511102: /*category*/ return this.category == null ? new Base[0] : new Base[] {this.category}; // Enumeration<MessageSignificanceCategory>
        case 97604824: /*focus*/ return this.focus == null ? new Base[0] : this.focus.toArray(new Base[this.focus.size()]); // MessageDefinitionFocusComponent
        case 791597824: /*responseRequired*/ return this.responseRequired == null ? new Base[0] : new Base[] {this.responseRequired}; // BooleanType
        case -1130933751: /*allowedResponse*/ return this.allowedResponse == null ? new Base[0] : this.allowedResponse.toArray(new Base[this.allowedResponse.size()]); // MessageDefinitionAllowedResponseComponent
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
        case 110371416: // title
          this.title = castToString(value); // StringType
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
        case -220463842: // purpose
          this.purpose = castToMarkdown(value); // MarkdownType
          return value;
        case 1522889671: // copyright
          this.copyright = castToMarkdown(value); // MarkdownType
          return value;
        case 3016401: // base
          this.base = castToReference(value); // Reference
          return value;
        case -995424086: // parent
          this.getParent().add(castToReference(value)); // Reference
          return value;
        case -430332865: // replaces
          this.getReplaces().add(castToReference(value)); // Reference
          return value;
        case 96891546: // event
          this.event = castToCoding(value); // Coding
          return value;
        case 50511102: // category
          value = new MessageSignificanceCategoryEnumFactory().fromType(castToCode(value));
          this.category = (Enumeration) value; // Enumeration<MessageSignificanceCategory>
          return value;
        case 97604824: // focus
          this.getFocus().add((MessageDefinitionFocusComponent) value); // MessageDefinitionFocusComponent
          return value;
        case 791597824: // responseRequired
          this.responseRequired = castToBoolean(value); // BooleanType
          return value;
        case -1130933751: // allowedResponse
          this.getAllowedResponse().add((MessageDefinitionAllowedResponseComponent) value); // MessageDefinitionAllowedResponseComponent
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
        } else if (name.equals("title")) {
          this.title = castToString(value); // StringType
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
        } else if (name.equals("purpose")) {
          this.purpose = castToMarkdown(value); // MarkdownType
        } else if (name.equals("copyright")) {
          this.copyright = castToMarkdown(value); // MarkdownType
        } else if (name.equals("base")) {
          this.base = castToReference(value); // Reference
        } else if (name.equals("parent")) {
          this.getParent().add(castToReference(value));
        } else if (name.equals("replaces")) {
          this.getReplaces().add(castToReference(value));
        } else if (name.equals("event")) {
          this.event = castToCoding(value); // Coding
        } else if (name.equals("category")) {
          value = new MessageSignificanceCategoryEnumFactory().fromType(castToCode(value));
          this.category = (Enumeration) value; // Enumeration<MessageSignificanceCategory>
        } else if (name.equals("focus")) {
          this.getFocus().add((MessageDefinitionFocusComponent) value);
        } else if (name.equals("responseRequired")) {
          this.responseRequired = castToBoolean(value); // BooleanType
        } else if (name.equals("allowedResponse")) {
          this.getAllowedResponse().add((MessageDefinitionAllowedResponseComponent) value);
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
        case 110371416:  return getTitleElement();
        case -892481550:  return getStatusElement();
        case -404562712:  return getExperimentalElement();
        case 3076014:  return getDateElement();
        case 1447404028:  return getPublisherElement();
        case 951526432:  return addContact(); 
        case -1724546052:  return getDescriptionElement();
        case -669707736:  return addUseContext(); 
        case -507075711:  return addJurisdiction(); 
        case -220463842:  return getPurposeElement();
        case 1522889671:  return getCopyrightElement();
        case 3016401:  return getBase(); 
        case -995424086:  return addParent(); 
        case -430332865:  return addReplaces(); 
        case 96891546:  return getEvent(); 
        case 50511102:  return getCategoryElement();
        case 97604824:  return addFocus(); 
        case 791597824:  return getResponseRequiredElement();
        case -1130933751:  return addAllowedResponse(); 
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
        case 110371416: /*title*/ return new String[] {"string"};
        case -892481550: /*status*/ return new String[] {"code"};
        case -404562712: /*experimental*/ return new String[] {"boolean"};
        case 3076014: /*date*/ return new String[] {"dateTime"};
        case 1447404028: /*publisher*/ return new String[] {"string"};
        case 951526432: /*contact*/ return new String[] {"ContactDetail"};
        case -1724546052: /*description*/ return new String[] {"markdown"};
        case -669707736: /*useContext*/ return new String[] {"UsageContext"};
        case -507075711: /*jurisdiction*/ return new String[] {"CodeableConcept"};
        case -220463842: /*purpose*/ return new String[] {"markdown"};
        case 1522889671: /*copyright*/ return new String[] {"markdown"};
        case 3016401: /*base*/ return new String[] {"Reference"};
        case -995424086: /*parent*/ return new String[] {"Reference"};
        case -430332865: /*replaces*/ return new String[] {"Reference"};
        case 96891546: /*event*/ return new String[] {"Coding"};
        case 50511102: /*category*/ return new String[] {"code"};
        case 97604824: /*focus*/ return new String[] {};
        case 791597824: /*responseRequired*/ return new String[] {"boolean"};
        case -1130933751: /*allowedResponse*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type MessageDefinition.url");
        }
        else if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type MessageDefinition.version");
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type MessageDefinition.name");
        }
        else if (name.equals("title")) {
          throw new FHIRException("Cannot call addChild on a primitive type MessageDefinition.title");
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type MessageDefinition.status");
        }
        else if (name.equals("experimental")) {
          throw new FHIRException("Cannot call addChild on a primitive type MessageDefinition.experimental");
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type MessageDefinition.date");
        }
        else if (name.equals("publisher")) {
          throw new FHIRException("Cannot call addChild on a primitive type MessageDefinition.publisher");
        }
        else if (name.equals("contact")) {
          return addContact();
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type MessageDefinition.description");
        }
        else if (name.equals("useContext")) {
          return addUseContext();
        }
        else if (name.equals("jurisdiction")) {
          return addJurisdiction();
        }
        else if (name.equals("purpose")) {
          throw new FHIRException("Cannot call addChild on a primitive type MessageDefinition.purpose");
        }
        else if (name.equals("copyright")) {
          throw new FHIRException("Cannot call addChild on a primitive type MessageDefinition.copyright");
        }
        else if (name.equals("base")) {
          this.base = new Reference();
          return this.base;
        }
        else if (name.equals("parent")) {
          return addParent();
        }
        else if (name.equals("replaces")) {
          return addReplaces();
        }
        else if (name.equals("event")) {
          this.event = new Coding();
          return this.event;
        }
        else if (name.equals("category")) {
          throw new FHIRException("Cannot call addChild on a primitive type MessageDefinition.category");
        }
        else if (name.equals("focus")) {
          return addFocus();
        }
        else if (name.equals("responseRequired")) {
          throw new FHIRException("Cannot call addChild on a primitive type MessageDefinition.responseRequired");
        }
        else if (name.equals("allowedResponse")) {
          return addAllowedResponse();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "MessageDefinition";

  }

      public MessageDefinition copy() {
        MessageDefinition dst = new MessageDefinition();
        copyValues(dst);
        dst.url = url == null ? null : url.copy();
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.version = version == null ? null : version.copy();
        dst.name = name == null ? null : name.copy();
        dst.title = title == null ? null : title.copy();
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
        dst.purpose = purpose == null ? null : purpose.copy();
        dst.copyright = copyright == null ? null : copyright.copy();
        dst.base = base == null ? null : base.copy();
        if (parent != null) {
          dst.parent = new ArrayList<Reference>();
          for (Reference i : parent)
            dst.parent.add(i.copy());
        };
        if (replaces != null) {
          dst.replaces = new ArrayList<Reference>();
          for (Reference i : replaces)
            dst.replaces.add(i.copy());
        };
        dst.event = event == null ? null : event.copy();
        dst.category = category == null ? null : category.copy();
        if (focus != null) {
          dst.focus = new ArrayList<MessageDefinitionFocusComponent>();
          for (MessageDefinitionFocusComponent i : focus)
            dst.focus.add(i.copy());
        };
        dst.responseRequired = responseRequired == null ? null : responseRequired.copy();
        if (allowedResponse != null) {
          dst.allowedResponse = new ArrayList<MessageDefinitionAllowedResponseComponent>();
          for (MessageDefinitionAllowedResponseComponent i : allowedResponse)
            dst.allowedResponse.add(i.copy());
        };
        return dst;
      }

      protected MessageDefinition typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof MessageDefinition))
          return false;
        MessageDefinition o = (MessageDefinition) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(purpose, o.purpose, true) && compareDeep(copyright, o.copyright, true)
           && compareDeep(base, o.base, true) && compareDeep(parent, o.parent, true) && compareDeep(replaces, o.replaces, true)
           && compareDeep(event, o.event, true) && compareDeep(category, o.category, true) && compareDeep(focus, o.focus, true)
           && compareDeep(responseRequired, o.responseRequired, true) && compareDeep(allowedResponse, o.allowedResponse, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MessageDefinition))
          return false;
        MessageDefinition o = (MessageDefinition) other;
        return compareValues(purpose, o.purpose, true) && compareValues(copyright, o.copyright, true) && compareValues(category, o.category, true)
           && compareValues(responseRequired, o.responseRequired, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, purpose, copyright
          , base, parent, replaces, event, category, focus, responseRequired, allowedResponse
          );
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.MessageDefinition;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>The message definition publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>MessageDefinition.date</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="MessageDefinition.date", description="The message definition publication date", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>The message definition publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>MessageDefinition.date</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>External identifier for the message definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MessageDefinition.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="MessageDefinition.identifier", description="External identifier for the message definition", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>External identifier for the message definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MessageDefinition.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>jurisdiction</b>
   * <p>
   * Description: <b>Intended jurisdiction for the message definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MessageDefinition.jurisdiction</b><br>
   * </p>
   */
  @SearchParamDefinition(name="jurisdiction", path="MessageDefinition.jurisdiction", description="Intended jurisdiction for the message definition", type="token" )
  public static final String SP_JURISDICTION = "jurisdiction";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>jurisdiction</b>
   * <p>
   * Description: <b>Intended jurisdiction for the message definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MessageDefinition.jurisdiction</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam JURISDICTION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_JURISDICTION);

 /**
   * Search parameter: <b>description</b>
   * <p>
   * Description: <b>The description of the message definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>MessageDefinition.description</b><br>
   * </p>
   */
  @SearchParamDefinition(name="description", path="MessageDefinition.description", description="The description of the message definition", type="string" )
  public static final String SP_DESCRIPTION = "description";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>description</b>
   * <p>
   * Description: <b>The description of the message definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>MessageDefinition.description</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam DESCRIPTION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_DESCRIPTION);

 /**
   * Search parameter: <b>focus</b>
   * <p>
   * Description: <b>A resource that is a permitted focus of the message</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MessageDefinition.focus.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="focus", path="MessageDefinition.focus.code", description="A resource that is a permitted focus of the message", type="token" )
  public static final String SP_FOCUS = "focus";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>focus</b>
   * <p>
   * Description: <b>A resource that is a permitted focus of the message</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MessageDefinition.focus.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam FOCUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_FOCUS);

 /**
   * Search parameter: <b>title</b>
   * <p>
   * Description: <b>The human-friendly name of the message definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>MessageDefinition.title</b><br>
   * </p>
   */
  @SearchParamDefinition(name="title", path="MessageDefinition.title", description="The human-friendly name of the message definition", type="string" )
  public static final String SP_TITLE = "title";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>title</b>
   * <p>
   * Description: <b>The human-friendly name of the message definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>MessageDefinition.title</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam TITLE = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_TITLE);

 /**
   * Search parameter: <b>version</b>
   * <p>
   * Description: <b>The business version of the message definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MessageDefinition.version</b><br>
   * </p>
   */
  @SearchParamDefinition(name="version", path="MessageDefinition.version", description="The business version of the message definition", type="token" )
  public static final String SP_VERSION = "version";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>version</b>
   * <p>
   * Description: <b>The business version of the message definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MessageDefinition.version</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam VERSION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_VERSION);

 /**
   * Search parameter: <b>url</b>
   * <p>
   * Description: <b>The uri that identifies the message definition</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>MessageDefinition.url</b><br>
   * </p>
   */
  @SearchParamDefinition(name="url", path="MessageDefinition.url", description="The uri that identifies the message definition", type="uri" )
  public static final String SP_URL = "url";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>url</b>
   * <p>
   * Description: <b>The uri that identifies the message definition</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>MessageDefinition.url</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam URL = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_URL);

 /**
   * Search parameter: <b>name</b>
   * <p>
   * Description: <b>Computationally friendly name of the message definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>MessageDefinition.name</b><br>
   * </p>
   */
  @SearchParamDefinition(name="name", path="MessageDefinition.name", description="Computationally friendly name of the message definition", type="string" )
  public static final String SP_NAME = "name";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>name</b>
   * <p>
   * Description: <b>Computationally friendly name of the message definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>MessageDefinition.name</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam NAME = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_NAME);

 /**
   * Search parameter: <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the message definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>MessageDefinition.publisher</b><br>
   * </p>
   */
  @SearchParamDefinition(name="publisher", path="MessageDefinition.publisher", description="Name of the publisher of the message definition", type="string" )
  public static final String SP_PUBLISHER = "publisher";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the message definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>MessageDefinition.publisher</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam PUBLISHER = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_PUBLISHER);

 /**
   * Search parameter: <b>event</b>
   * <p>
   * Description: <b>The event that triggers the message</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MessageDefinition.event</b><br>
   * </p>
   */
  @SearchParamDefinition(name="event", path="MessageDefinition.event", description="The event that triggers the message", type="token" )
  public static final String SP_EVENT = "event";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>event</b>
   * <p>
   * Description: <b>The event that triggers the message</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MessageDefinition.event</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam EVENT = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_EVENT);

 /**
   * Search parameter: <b>category</b>
   * <p>
   * Description: <b>The behavior associated with the message</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MessageDefinition.category</b><br>
   * </p>
   */
  @SearchParamDefinition(name="category", path="MessageDefinition.category", description="The behavior associated with the message", type="token" )
  public static final String SP_CATEGORY = "category";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>category</b>
   * <p>
   * Description: <b>The behavior associated with the message</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MessageDefinition.category</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CATEGORY = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CATEGORY);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The current status of the message definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MessageDefinition.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="MessageDefinition.status", description="The current status of the message definition", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The current status of the message definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MessageDefinition.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

