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

// Generated on Sat, Jul 8, 2017 23:19+1000 for FHIR v3.1.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * A description of a triggering event.
 */
@DatatypeDef(name="TriggerDefinition")
public class TriggerDefinition extends Type implements ICompositeType {

    public enum TriggerType {
        /**
         * The trigger occurs in response to a specific named event
         */
        NAMEDEVENT, 
        /**
         * The trigger occurs at a specific time or periodically as described by a timing or schedule
         */
        PERIODIC, 
        /**
         * The trigger occurs whenever data of a particular type is added
         */
        DATAADDED, 
        /**
         * The trigger occurs whenever data of a particular type is modified
         */
        DATAMODIFIED, 
        /**
         * The trigger occurs whenever data of a particular type is removed
         */
        DATAREMOVED, 
        /**
         * The trigger occurs whenever data of a particular type is accessed
         */
        DATAACCESSED, 
        /**
         * The trigger occurs whenever access to data of a particular type is completed
         */
        DATAACCESSENDED, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static TriggerType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("named-event".equals(codeString))
          return NAMEDEVENT;
        if ("periodic".equals(codeString))
          return PERIODIC;
        if ("data-added".equals(codeString))
          return DATAADDED;
        if ("data-modified".equals(codeString))
          return DATAMODIFIED;
        if ("data-removed".equals(codeString))
          return DATAREMOVED;
        if ("data-accessed".equals(codeString))
          return DATAACCESSED;
        if ("data-access-ended".equals(codeString))
          return DATAACCESSENDED;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown TriggerType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case NAMEDEVENT: return "named-event";
            case PERIODIC: return "periodic";
            case DATAADDED: return "data-added";
            case DATAMODIFIED: return "data-modified";
            case DATAREMOVED: return "data-removed";
            case DATAACCESSED: return "data-accessed";
            case DATAACCESSENDED: return "data-access-ended";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case NAMEDEVENT: return "http://hl7.org/fhir/trigger-type";
            case PERIODIC: return "http://hl7.org/fhir/trigger-type";
            case DATAADDED: return "http://hl7.org/fhir/trigger-type";
            case DATAMODIFIED: return "http://hl7.org/fhir/trigger-type";
            case DATAREMOVED: return "http://hl7.org/fhir/trigger-type";
            case DATAACCESSED: return "http://hl7.org/fhir/trigger-type";
            case DATAACCESSENDED: return "http://hl7.org/fhir/trigger-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case NAMEDEVENT: return "The trigger occurs in response to a specific named event";
            case PERIODIC: return "The trigger occurs at a specific time or periodically as described by a timing or schedule";
            case DATAADDED: return "The trigger occurs whenever data of a particular type is added";
            case DATAMODIFIED: return "The trigger occurs whenever data of a particular type is modified";
            case DATAREMOVED: return "The trigger occurs whenever data of a particular type is removed";
            case DATAACCESSED: return "The trigger occurs whenever data of a particular type is accessed";
            case DATAACCESSENDED: return "The trigger occurs whenever access to data of a particular type is completed";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case NAMEDEVENT: return "Named Event";
            case PERIODIC: return "Periodic";
            case DATAADDED: return "Data Added";
            case DATAMODIFIED: return "Data Modified";
            case DATAREMOVED: return "Data Removed";
            case DATAACCESSED: return "Data Accessed";
            case DATAACCESSENDED: return "Data Access Ended";
            default: return "?";
          }
        }
    }

  public static class TriggerTypeEnumFactory implements EnumFactory<TriggerType> {
    public TriggerType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("named-event".equals(codeString))
          return TriggerType.NAMEDEVENT;
        if ("periodic".equals(codeString))
          return TriggerType.PERIODIC;
        if ("data-added".equals(codeString))
          return TriggerType.DATAADDED;
        if ("data-modified".equals(codeString))
          return TriggerType.DATAMODIFIED;
        if ("data-removed".equals(codeString))
          return TriggerType.DATAREMOVED;
        if ("data-accessed".equals(codeString))
          return TriggerType.DATAACCESSED;
        if ("data-access-ended".equals(codeString))
          return TriggerType.DATAACCESSENDED;
        throw new IllegalArgumentException("Unknown TriggerType code '"+codeString+"'");
        }
        public Enumeration<TriggerType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<TriggerType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("named-event".equals(codeString))
          return new Enumeration<TriggerType>(this, TriggerType.NAMEDEVENT);
        if ("periodic".equals(codeString))
          return new Enumeration<TriggerType>(this, TriggerType.PERIODIC);
        if ("data-added".equals(codeString))
          return new Enumeration<TriggerType>(this, TriggerType.DATAADDED);
        if ("data-modified".equals(codeString))
          return new Enumeration<TriggerType>(this, TriggerType.DATAMODIFIED);
        if ("data-removed".equals(codeString))
          return new Enumeration<TriggerType>(this, TriggerType.DATAREMOVED);
        if ("data-accessed".equals(codeString))
          return new Enumeration<TriggerType>(this, TriggerType.DATAACCESSED);
        if ("data-access-ended".equals(codeString))
          return new Enumeration<TriggerType>(this, TriggerType.DATAACCESSENDED);
        throw new FHIRException("Unknown TriggerType code '"+codeString+"'");
        }
    public String toCode(TriggerType code) {
      if (code == TriggerType.NAMEDEVENT)
        return "named-event";
      if (code == TriggerType.PERIODIC)
        return "periodic";
      if (code == TriggerType.DATAADDED)
        return "data-added";
      if (code == TriggerType.DATAMODIFIED)
        return "data-modified";
      if (code == TriggerType.DATAREMOVED)
        return "data-removed";
      if (code == TriggerType.DATAACCESSED)
        return "data-accessed";
      if (code == TriggerType.DATAACCESSENDED)
        return "data-access-ended";
      return "?";
      }
    public String toSystem(TriggerType code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class TriggerDefinitionEventConditionComponent extends Element implements IBaseDatatypeElement {
        /**
         * A brief, natural language description of the condition that effectively communicates the intended semantics.
         */
        @Child(name = "description", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Natural language description of the condition", formalDefinition="A brief, natural language description of the condition that effectively communicates the intended semantics." )
        protected StringType description;

        /**
         * The media type of the language for the expression.
         */
        @Child(name = "language", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Language of the expression", formalDefinition="The media type of the language for the expression." )
        protected StringType language;

        /**
         * An expression that returns true or false, indicating whether or not the condition is satisfied.
         */
        @Child(name = "expression", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Boolean-valued expression", formalDefinition="An expression that returns true or false, indicating whether or not the condition is satisfied." )
        protected StringType expression;

        private static final long serialVersionUID = 1354288281L;

    /**
     * Constructor
     */
      public TriggerDefinitionEventConditionComponent() {
        super();
      }

        /**
         * @return {@link #description} (A brief, natural language description of the condition that effectively communicates the intended semantics.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TriggerDefinitionEventConditionComponent.description");
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
         * @param value {@link #description} (A brief, natural language description of the condition that effectively communicates the intended semantics.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public TriggerDefinitionEventConditionComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return A brief, natural language description of the condition that effectively communicates the intended semantics.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value A brief, natural language description of the condition that effectively communicates the intended semantics.
         */
        public TriggerDefinitionEventConditionComponent setDescription(String value) { 
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
         * @return {@link #language} (The media type of the language for the expression.). This is the underlying object with id, value and extensions. The accessor "getLanguage" gives direct access to the value
         */
        public StringType getLanguageElement() { 
          if (this.language == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TriggerDefinitionEventConditionComponent.language");
            else if (Configuration.doAutoCreate())
              this.language = new StringType(); // bb
          return this.language;
        }

        public boolean hasLanguageElement() { 
          return this.language != null && !this.language.isEmpty();
        }

        public boolean hasLanguage() { 
          return this.language != null && !this.language.isEmpty();
        }

        /**
         * @param value {@link #language} (The media type of the language for the expression.). This is the underlying object with id, value and extensions. The accessor "getLanguage" gives direct access to the value
         */
        public TriggerDefinitionEventConditionComponent setLanguageElement(StringType value) { 
          this.language = value;
          return this;
        }

        /**
         * @return The media type of the language for the expression.
         */
        public String getLanguage() { 
          return this.language == null ? null : this.language.getValue();
        }

        /**
         * @param value The media type of the language for the expression.
         */
        public TriggerDefinitionEventConditionComponent setLanguage(String value) { 
          if (Utilities.noString(value))
            this.language = null;
          else {
            if (this.language == null)
              this.language = new StringType();
            this.language.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #expression} (An expression that returns true or false, indicating whether or not the condition is satisfied.). This is the underlying object with id, value and extensions. The accessor "getExpression" gives direct access to the value
         */
        public StringType getExpressionElement() { 
          if (this.expression == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TriggerDefinitionEventConditionComponent.expression");
            else if (Configuration.doAutoCreate())
              this.expression = new StringType(); // bb
          return this.expression;
        }

        public boolean hasExpressionElement() { 
          return this.expression != null && !this.expression.isEmpty();
        }

        public boolean hasExpression() { 
          return this.expression != null && !this.expression.isEmpty();
        }

        /**
         * @param value {@link #expression} (An expression that returns true or false, indicating whether or not the condition is satisfied.). This is the underlying object with id, value and extensions. The accessor "getExpression" gives direct access to the value
         */
        public TriggerDefinitionEventConditionComponent setExpressionElement(StringType value) { 
          this.expression = value;
          return this;
        }

        /**
         * @return An expression that returns true or false, indicating whether or not the condition is satisfied.
         */
        public String getExpression() { 
          return this.expression == null ? null : this.expression.getValue();
        }

        /**
         * @param value An expression that returns true or false, indicating whether or not the condition is satisfied.
         */
        public TriggerDefinitionEventConditionComponent setExpression(String value) { 
          if (Utilities.noString(value))
            this.expression = null;
          else {
            if (this.expression == null)
              this.expression = new StringType();
            this.expression.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("description", "string", "A brief, natural language description of the condition that effectively communicates the intended semantics.", 0, 1, description));
          children.add(new Property("language", "string", "The media type of the language for the expression.", 0, 1, language));
          children.add(new Property("expression", "string", "An expression that returns true or false, indicating whether or not the condition is satisfied.", 0, 1, expression));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1724546052: /*description*/  return new Property("description", "string", "A brief, natural language description of the condition that effectively communicates the intended semantics.", 0, 1, description);
          case -1613589672: /*language*/  return new Property("language", "string", "The media type of the language for the expression.", 0, 1, language);
          case -1795452264: /*expression*/  return new Property("expression", "string", "An expression that returns true or false, indicating whether or not the condition is satisfied.", 0, 1, expression);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case -1613589672: /*language*/ return this.language == null ? new Base[0] : new Base[] {this.language}; // StringType
        case -1795452264: /*expression*/ return this.expression == null ? new Base[0] : new Base[] {this.expression}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        case -1613589672: // language
          this.language = castToString(value); // StringType
          return value;
        case -1795452264: // expression
          this.expression = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else if (name.equals("language")) {
          this.language = castToString(value); // StringType
        } else if (name.equals("expression")) {
          this.expression = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1724546052:  return getDescriptionElement();
        case -1613589672:  return getLanguageElement();
        case -1795452264:  return getExpressionElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1724546052: /*description*/ return new String[] {"string"};
        case -1613589672: /*language*/ return new String[] {"string"};
        case -1795452264: /*expression*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type TriggerDefinition.description");
        }
        else if (name.equals("language")) {
          throw new FHIRException("Cannot call addChild on a primitive type TriggerDefinition.language");
        }
        else if (name.equals("expression")) {
          throw new FHIRException("Cannot call addChild on a primitive type TriggerDefinition.expression");
        }
        else
          return super.addChild(name);
      }

      public TriggerDefinitionEventConditionComponent copy() {
        TriggerDefinitionEventConditionComponent dst = new TriggerDefinitionEventConditionComponent();
        copyValues(dst);
        dst.description = description == null ? null : description.copy();
        dst.language = language == null ? null : language.copy();
        dst.expression = expression == null ? null : expression.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TriggerDefinitionEventConditionComponent))
          return false;
        TriggerDefinitionEventConditionComponent o = (TriggerDefinitionEventConditionComponent) other;
        return compareDeep(description, o.description, true) && compareDeep(language, o.language, true)
           && compareDeep(expression, o.expression, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TriggerDefinitionEventConditionComponent))
          return false;
        TriggerDefinitionEventConditionComponent o = (TriggerDefinitionEventConditionComponent) other;
        return compareValues(description, o.description, true) && compareValues(language, o.language, true)
           && compareValues(expression, o.expression, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(description, language, expression
          );
      }

  public String fhirType() {
    return "TriggerDefinition.eventCondition";

  }

  }

    /**
     * The type of triggering event.
     */
    @Child(name = "type", type = {CodeType.class}, order=0, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="named-event | periodic | data-added | data-modified | data-removed | data-accessed | data-access-ended", formalDefinition="The type of triggering event." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/trigger-type")
    protected Enumeration<TriggerType> type;

    /**
     * The name of the event (if this is a named-event trigger).
     */
    @Child(name = "eventName", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Triggering event name", formalDefinition="The name of the event (if this is a named-event trigger)." )
    protected StringType eventName;

    /**
     * The timing of the event (if this is a period trigger).
     */
    @Child(name = "eventTiming", type = {Timing.class, Schedule.class, DateType.class, DateTimeType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Timing of the event", formalDefinition="The timing of the event (if this is a period trigger)." )
    protected Type eventTiming;

    /**
     * The triggering data of the event (if this is a data trigger).
     */
    @Child(name = "eventData", type = {DataRequirement.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Triggering data of the event", formalDefinition="The triggering data of the event (if this is a data trigger)." )
    protected DataRequirement eventData;

    /**
     * A boolean-valued expression that is evaluated in the context of the container of the trigger definition and returns whether or not the trigger fires.
     */
    @Child(name = "eventCondition", type = {}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Whether the event triggers", formalDefinition="A boolean-valued expression that is evaluated in the context of the container of the trigger definition and returns whether or not the trigger fires." )
    protected TriggerDefinitionEventConditionComponent eventCondition;

    private static final long serialVersionUID = -1377796374L;

  /**
   * Constructor
   */
    public TriggerDefinition() {
      super();
    }

  /**
   * Constructor
   */
    public TriggerDefinition(Enumeration<TriggerType> type) {
      super();
      this.type = type;
    }

    /**
     * @return {@link #type} (The type of triggering event.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public Enumeration<TriggerType> getTypeElement() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create TriggerDefinition.type");
        else if (Configuration.doAutoCreate())
          this.type = new Enumeration<TriggerType>(new TriggerTypeEnumFactory()); // bb
      return this.type;
    }

    public boolean hasTypeElement() { 
      return this.type != null && !this.type.isEmpty();
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (The type of triggering event.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public TriggerDefinition setTypeElement(Enumeration<TriggerType> value) { 
      this.type = value;
      return this;
    }

    /**
     * @return The type of triggering event.
     */
    public TriggerType getType() { 
      return this.type == null ? null : this.type.getValue();
    }

    /**
     * @param value The type of triggering event.
     */
    public TriggerDefinition setType(TriggerType value) { 
        if (this.type == null)
          this.type = new Enumeration<TriggerType>(new TriggerTypeEnumFactory());
        this.type.setValue(value);
      return this;
    }

    /**
     * @return {@link #eventName} (The name of the event (if this is a named-event trigger).). This is the underlying object with id, value and extensions. The accessor "getEventName" gives direct access to the value
     */
    public StringType getEventNameElement() { 
      if (this.eventName == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create TriggerDefinition.eventName");
        else if (Configuration.doAutoCreate())
          this.eventName = new StringType(); // bb
      return this.eventName;
    }

    public boolean hasEventNameElement() { 
      return this.eventName != null && !this.eventName.isEmpty();
    }

    public boolean hasEventName() { 
      return this.eventName != null && !this.eventName.isEmpty();
    }

    /**
     * @param value {@link #eventName} (The name of the event (if this is a named-event trigger).). This is the underlying object with id, value and extensions. The accessor "getEventName" gives direct access to the value
     */
    public TriggerDefinition setEventNameElement(StringType value) { 
      this.eventName = value;
      return this;
    }

    /**
     * @return The name of the event (if this is a named-event trigger).
     */
    public String getEventName() { 
      return this.eventName == null ? null : this.eventName.getValue();
    }

    /**
     * @param value The name of the event (if this is a named-event trigger).
     */
    public TriggerDefinition setEventName(String value) { 
      if (Utilities.noString(value))
        this.eventName = null;
      else {
        if (this.eventName == null)
          this.eventName = new StringType();
        this.eventName.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #eventTiming} (The timing of the event (if this is a period trigger).)
     */
    public Type getEventTiming() { 
      return this.eventTiming;
    }

    /**
     * @return {@link #eventTiming} (The timing of the event (if this is a period trigger).)
     */
    public Timing getEventTimingTiming() throws FHIRException { 
      if (!(this.eventTiming instanceof Timing))
        throw new FHIRException("Type mismatch: the type Timing was expected, but "+this.eventTiming.getClass().getName()+" was encountered");
      return (Timing) this.eventTiming;
    }

    public boolean hasEventTimingTiming() { 
      return this.eventTiming instanceof Timing;
    }

    /**
     * @return {@link #eventTiming} (The timing of the event (if this is a period trigger).)
     */
    public Reference getEventTimingReference() throws FHIRException { 
      if (!(this.eventTiming instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.eventTiming.getClass().getName()+" was encountered");
      return (Reference) this.eventTiming;
    }

    public boolean hasEventTimingReference() { 
      return this.eventTiming instanceof Reference;
    }

    /**
     * @return {@link #eventTiming} (The timing of the event (if this is a period trigger).)
     */
    public DateType getEventTimingDateType() throws FHIRException { 
      if (!(this.eventTiming instanceof DateType))
        throw new FHIRException("Type mismatch: the type DateType was expected, but "+this.eventTiming.getClass().getName()+" was encountered");
      return (DateType) this.eventTiming;
    }

    public boolean hasEventTimingDateType() { 
      return this.eventTiming instanceof DateType;
    }

    /**
     * @return {@link #eventTiming} (The timing of the event (if this is a period trigger).)
     */
    public DateTimeType getEventTimingDateTimeType() throws FHIRException { 
      if (!(this.eventTiming instanceof DateTimeType))
        throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.eventTiming.getClass().getName()+" was encountered");
      return (DateTimeType) this.eventTiming;
    }

    public boolean hasEventTimingDateTimeType() { 
      return this.eventTiming instanceof DateTimeType;
    }

    public boolean hasEventTiming() { 
      return this.eventTiming != null && !this.eventTiming.isEmpty();
    }

    /**
     * @param value {@link #eventTiming} (The timing of the event (if this is a period trigger).)
     */
    public TriggerDefinition setEventTiming(Type value) { 
      this.eventTiming = value;
      return this;
    }

    /**
     * @return {@link #eventData} (The triggering data of the event (if this is a data trigger).)
     */
    public DataRequirement getEventData() { 
      if (this.eventData == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create TriggerDefinition.eventData");
        else if (Configuration.doAutoCreate())
          this.eventData = new DataRequirement(); // cc
      return this.eventData;
    }

    public boolean hasEventData() { 
      return this.eventData != null && !this.eventData.isEmpty();
    }

    /**
     * @param value {@link #eventData} (The triggering data of the event (if this is a data trigger).)
     */
    public TriggerDefinition setEventData(DataRequirement value) { 
      this.eventData = value;
      return this;
    }

    /**
     * @return {@link #eventCondition} (A boolean-valued expression that is evaluated in the context of the container of the trigger definition and returns whether or not the trigger fires.)
     */
    public TriggerDefinitionEventConditionComponent getEventCondition() { 
      if (this.eventCondition == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create TriggerDefinition.eventCondition");
        else if (Configuration.doAutoCreate())
          this.eventCondition = new TriggerDefinitionEventConditionComponent(); // cc
      return this.eventCondition;
    }

    public boolean hasEventCondition() { 
      return this.eventCondition != null && !this.eventCondition.isEmpty();
    }

    /**
     * @param value {@link #eventCondition} (A boolean-valued expression that is evaluated in the context of the container of the trigger definition and returns whether or not the trigger fires.)
     */
    public TriggerDefinition setEventCondition(TriggerDefinitionEventConditionComponent value) { 
      this.eventCondition = value;
      return this;
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("type", "code", "The type of triggering event.", 0, 1, type));
        children.add(new Property("eventName", "string", "The name of the event (if this is a named-event trigger).", 0, 1, eventName));
        children.add(new Property("eventTiming[x]", "Timing|Reference(Schedule)|date|dateTime", "The timing of the event (if this is a period trigger).", 0, 1, eventTiming));
        children.add(new Property("eventData", "DataRequirement", "The triggering data of the event (if this is a data trigger).", 0, 1, eventData));
        children.add(new Property("eventCondition", "", "A boolean-valued expression that is evaluated in the context of the container of the trigger definition and returns whether or not the trigger fires.", 0, 1, eventCondition));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case 3575610: /*type*/  return new Property("type", "code", "The type of triggering event.", 0, 1, type);
        case 31228997: /*eventName*/  return new Property("eventName", "string", "The name of the event (if this is a named-event trigger).", 0, 1, eventName);
        case 1120539260: /*eventTiming[x]*/  return new Property("eventTiming[x]", "Timing|Reference(Schedule)|date|dateTime", "The timing of the event (if this is a period trigger).", 0, 1, eventTiming);
        case 125465476: /*eventTiming*/  return new Property("eventTiming[x]", "Timing|Reference(Schedule)|date|dateTime", "The timing of the event (if this is a period trigger).", 0, 1, eventTiming);
        case 1285594350: /*eventTimingTiming*/  return new Property("eventTiming[x]", "Timing|Reference(Schedule)|date|dateTime", "The timing of the event (if this is a period trigger).", 0, 1, eventTiming);
        case -171794393: /*eventTimingReference*/  return new Property("eventTiming[x]", "Timing|Reference(Schedule)|date|dateTime", "The timing of the event (if this is a period trigger).", 0, 1, eventTiming);
        case 376272210: /*eventTimingDate*/  return new Property("eventTiming[x]", "Timing|Reference(Schedule)|date|dateTime", "The timing of the event (if this is a period trigger).", 0, 1, eventTiming);
        case -1923726529: /*eventTimingDateTime*/  return new Property("eventTiming[x]", "Timing|Reference(Schedule)|date|dateTime", "The timing of the event (if this is a period trigger).", 0, 1, eventTiming);
        case 30931300: /*eventData*/  return new Property("eventData", "DataRequirement", "The triggering data of the event (if this is a data trigger).", 0, 1, eventData);
        case 94594977: /*eventCondition*/  return new Property("eventCondition", "", "A boolean-valued expression that is evaluated in the context of the container of the trigger definition and returns whether or not the trigger fires.", 0, 1, eventCondition);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<TriggerType>
        case 31228997: /*eventName*/ return this.eventName == null ? new Base[0] : new Base[] {this.eventName}; // StringType
        case 125465476: /*eventTiming*/ return this.eventTiming == null ? new Base[0] : new Base[] {this.eventTiming}; // Type
        case 30931300: /*eventData*/ return this.eventData == null ? new Base[0] : new Base[] {this.eventData}; // DataRequirement
        case 94594977: /*eventCondition*/ return this.eventCondition == null ? new Base[0] : new Base[] {this.eventCondition}; // TriggerDefinitionEventConditionComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          value = new TriggerTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<TriggerType>
          return value;
        case 31228997: // eventName
          this.eventName = castToString(value); // StringType
          return value;
        case 125465476: // eventTiming
          this.eventTiming = castToType(value); // Type
          return value;
        case 30931300: // eventData
          this.eventData = castToDataRequirement(value); // DataRequirement
          return value;
        case 94594977: // eventCondition
          this.eventCondition = (TriggerDefinitionEventConditionComponent) value; // TriggerDefinitionEventConditionComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          value = new TriggerTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<TriggerType>
        } else if (name.equals("eventName")) {
          this.eventName = castToString(value); // StringType
        } else if (name.equals("eventTiming[x]")) {
          this.eventTiming = castToType(value); // Type
        } else if (name.equals("eventData")) {
          this.eventData = castToDataRequirement(value); // DataRequirement
        } else if (name.equals("eventCondition")) {
          this.eventCondition = (TriggerDefinitionEventConditionComponent) value; // TriggerDefinitionEventConditionComponent
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getTypeElement();
        case 31228997:  return getEventNameElement();
        case 1120539260:  return getEventTiming(); 
        case 125465476:  return getEventTiming(); 
        case 30931300:  return getEventData(); 
        case 94594977:  return getEventCondition(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"code"};
        case 31228997: /*eventName*/ return new String[] {"string"};
        case 125465476: /*eventTiming*/ return new String[] {"Timing", "Reference", "date", "dateTime"};
        case 30931300: /*eventData*/ return new String[] {"DataRequirement"};
        case 94594977: /*eventCondition*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type TriggerDefinition.type");
        }
        else if (name.equals("eventName")) {
          throw new FHIRException("Cannot call addChild on a primitive type TriggerDefinition.eventName");
        }
        else if (name.equals("eventTimingTiming")) {
          this.eventTiming = new Timing();
          return this.eventTiming;
        }
        else if (name.equals("eventTimingReference")) {
          this.eventTiming = new Reference();
          return this.eventTiming;
        }
        else if (name.equals("eventTimingDate")) {
          this.eventTiming = new DateType();
          return this.eventTiming;
        }
        else if (name.equals("eventTimingDateTime")) {
          this.eventTiming = new DateTimeType();
          return this.eventTiming;
        }
        else if (name.equals("eventData")) {
          this.eventData = new DataRequirement();
          return this.eventData;
        }
        else if (name.equals("eventCondition")) {
          this.eventCondition = new TriggerDefinitionEventConditionComponent();
          return this.eventCondition;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "TriggerDefinition";

  }

      public TriggerDefinition copy() {
        TriggerDefinition dst = new TriggerDefinition();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.eventName = eventName == null ? null : eventName.copy();
        dst.eventTiming = eventTiming == null ? null : eventTiming.copy();
        dst.eventData = eventData == null ? null : eventData.copy();
        dst.eventCondition = eventCondition == null ? null : eventCondition.copy();
        return dst;
      }

      protected TriggerDefinition typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TriggerDefinition))
          return false;
        TriggerDefinition o = (TriggerDefinition) other;
        return compareDeep(type, o.type, true) && compareDeep(eventName, o.eventName, true) && compareDeep(eventTiming, o.eventTiming, true)
           && compareDeep(eventData, o.eventData, true) && compareDeep(eventCondition, o.eventCondition, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TriggerDefinition))
          return false;
        TriggerDefinition o = (TriggerDefinition) other;
        return compareValues(type, o.type, true) && compareValues(eventName, o.eventName, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, eventName, eventTiming
          , eventData, eventCondition);
      }


}

