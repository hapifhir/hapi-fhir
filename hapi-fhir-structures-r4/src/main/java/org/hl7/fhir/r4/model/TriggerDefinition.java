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

// Generated on Thu, Dec 27, 2018 10:06-0500 for FHIR v4.0.0

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
 * A description of a triggering event. Triggering events can be named events, data events, or periodic, as determined by the type element.
 */
@DatatypeDef(name="TriggerDefinition")
public class TriggerDefinition extends Type implements ICompositeType {

    public enum TriggerType {
        /**
         * The trigger occurs in response to a specific named event, and no other information about the trigger is specified. Named events are completely pre-coordinated, and the formal semantics of the trigger are not provided.
         */
        NAMEDEVENT, 
        /**
         * The trigger occurs at a specific time or periodically as described by a timing or schedule. A periodic event cannot have any data elements, but may have a name assigned as a shorthand for the event.
         */
        PERIODIC, 
        /**
         * The trigger occurs whenever data of a particular type is changed in any way, either added, modified, or removed.
         */
        DATACHANGED, 
        /**
         * The trigger occurs whenever data of a particular type is added.
         */
        DATAADDED, 
        /**
         * The trigger occurs whenever data of a particular type is modified.
         */
        DATAMODIFIED, 
        /**
         * The trigger occurs whenever data of a particular type is removed.
         */
        DATAREMOVED, 
        /**
         * The trigger occurs whenever data of a particular type is accessed.
         */
        DATAACCESSED, 
        /**
         * The trigger occurs whenever access to data of a particular type is completed.
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
        if ("data-changed".equals(codeString))
          return DATACHANGED;
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
            case DATACHANGED: return "data-changed";
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
            case DATACHANGED: return "http://hl7.org/fhir/trigger-type";
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
            case NAMEDEVENT: return "The trigger occurs in response to a specific named event, and no other information about the trigger is specified. Named events are completely pre-coordinated, and the formal semantics of the trigger are not provided.";
            case PERIODIC: return "The trigger occurs at a specific time or periodically as described by a timing or schedule. A periodic event cannot have any data elements, but may have a name assigned as a shorthand for the event.";
            case DATACHANGED: return "The trigger occurs whenever data of a particular type is changed in any way, either added, modified, or removed.";
            case DATAADDED: return "The trigger occurs whenever data of a particular type is added.";
            case DATAMODIFIED: return "The trigger occurs whenever data of a particular type is modified.";
            case DATAREMOVED: return "The trigger occurs whenever data of a particular type is removed.";
            case DATAACCESSED: return "The trigger occurs whenever data of a particular type is accessed.";
            case DATAACCESSENDED: return "The trigger occurs whenever access to data of a particular type is completed.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case NAMEDEVENT: return "Named Event";
            case PERIODIC: return "Periodic";
            case DATACHANGED: return "Data Changed";
            case DATAADDED: return "Data Added";
            case DATAMODIFIED: return "Data Updated";
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
        if ("data-changed".equals(codeString))
          return TriggerType.DATACHANGED;
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
        if ("data-changed".equals(codeString))
          return new Enumeration<TriggerType>(this, TriggerType.DATACHANGED);
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
      if (code == TriggerType.DATACHANGED)
        return "data-changed";
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

    /**
     * The type of triggering event.
     */
    @Child(name = "type", type = {CodeType.class}, order=0, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="named-event | periodic | data-changed | data-added | data-modified | data-removed | data-accessed | data-access-ended", formalDefinition="The type of triggering event." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/trigger-type")
    protected Enumeration<TriggerType> type;

    /**
     * A formal name for the event. This may be an absolute URI that identifies the event formally (e.g. from a trigger registry), or a simple relative URI that identifies the event in a local context.
     */
    @Child(name = "name", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Name or URI that identifies the event", formalDefinition="A formal name for the event. This may be an absolute URI that identifies the event formally (e.g. from a trigger registry), or a simple relative URI that identifies the event in a local context." )
    protected StringType name;

    /**
     * The timing of the event (if this is a periodic trigger).
     */
    @Child(name = "timing", type = {Timing.class, Schedule.class, DateType.class, DateTimeType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Timing of the event", formalDefinition="The timing of the event (if this is a periodic trigger)." )
    protected Type timing;

    /**
     * The triggering data of the event (if this is a data trigger). If more than one data is requirement is specified, then all the data requirements must be true.
     */
    @Child(name = "data", type = {DataRequirement.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Triggering data of the event (multiple = 'and')", formalDefinition="The triggering data of the event (if this is a data trigger). If more than one data is requirement is specified, then all the data requirements must be true." )
    protected List<DataRequirement> data;

    /**
     * A boolean-valued expression that is evaluated in the context of the container of the trigger definition and returns whether or not the trigger fires.
     */
    @Child(name = "condition", type = {Expression.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Whether the event triggers (boolean expression)", formalDefinition="A boolean-valued expression that is evaluated in the context of the container of the trigger definition and returns whether or not the trigger fires." )
    protected Expression condition;

    private static final long serialVersionUID = -1706033335L;

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
     * @return {@link #name} (A formal name for the event. This may be an absolute URI that identifies the event formally (e.g. from a trigger registry), or a simple relative URI that identifies the event in a local context.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create TriggerDefinition.name");
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
     * @param value {@link #name} (A formal name for the event. This may be an absolute URI that identifies the event formally (e.g. from a trigger registry), or a simple relative URI that identifies the event in a local context.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public TriggerDefinition setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return A formal name for the event. This may be an absolute URI that identifies the event formally (e.g. from a trigger registry), or a simple relative URI that identifies the event in a local context.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value A formal name for the event. This may be an absolute URI that identifies the event formally (e.g. from a trigger registry), or a simple relative URI that identifies the event in a local context.
     */
    public TriggerDefinition setName(String value) { 
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
     * @return {@link #timing} (The timing of the event (if this is a periodic trigger).)
     */
    public Type getTiming() { 
      return this.timing;
    }

    /**
     * @return {@link #timing} (The timing of the event (if this is a periodic trigger).)
     */
    public Timing getTimingTiming() throws FHIRException { 
      if (this.timing == null)
        this.timing = new Timing();
      if (!(this.timing instanceof Timing))
        throw new FHIRException("Type mismatch: the type Timing was expected, but "+this.timing.getClass().getName()+" was encountered");
      return (Timing) this.timing;
    }

    public boolean hasTimingTiming() { 
      return this != null && this.timing instanceof Timing;
    }

    /**
     * @return {@link #timing} (The timing of the event (if this is a periodic trigger).)
     */
    public Reference getTimingReference() throws FHIRException { 
      if (this.timing == null)
        this.timing = new Reference();
      if (!(this.timing instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.timing.getClass().getName()+" was encountered");
      return (Reference) this.timing;
    }

    public boolean hasTimingReference() { 
      return this != null && this.timing instanceof Reference;
    }

    /**
     * @return {@link #timing} (The timing of the event (if this is a periodic trigger).)
     */
    public DateType getTimingDateType() throws FHIRException { 
      if (this.timing == null)
        this.timing = new DateType();
      if (!(this.timing instanceof DateType))
        throw new FHIRException("Type mismatch: the type DateType was expected, but "+this.timing.getClass().getName()+" was encountered");
      return (DateType) this.timing;
    }

    public boolean hasTimingDateType() { 
      return this != null && this.timing instanceof DateType;
    }

    /**
     * @return {@link #timing} (The timing of the event (if this is a periodic trigger).)
     */
    public DateTimeType getTimingDateTimeType() throws FHIRException { 
      if (this.timing == null)
        this.timing = new DateTimeType();
      if (!(this.timing instanceof DateTimeType))
        throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.timing.getClass().getName()+" was encountered");
      return (DateTimeType) this.timing;
    }

    public boolean hasTimingDateTimeType() { 
      return this != null && this.timing instanceof DateTimeType;
    }

    public boolean hasTiming() { 
      return this.timing != null && !this.timing.isEmpty();
    }

    /**
     * @param value {@link #timing} (The timing of the event (if this is a periodic trigger).)
     */
    public TriggerDefinition setTiming(Type value) { 
      if (value != null && !(value instanceof Timing || value instanceof Reference || value instanceof DateType || value instanceof DateTimeType))
        throw new Error("Not the right type for TriggerDefinition.timing[x]: "+value.fhirType());
      this.timing = value;
      return this;
    }

    /**
     * @return {@link #data} (The triggering data of the event (if this is a data trigger). If more than one data is requirement is specified, then all the data requirements must be true.)
     */
    public List<DataRequirement> getData() { 
      if (this.data == null)
        this.data = new ArrayList<DataRequirement>();
      return this.data;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public TriggerDefinition setData(List<DataRequirement> theData) { 
      this.data = theData;
      return this;
    }

    public boolean hasData() { 
      if (this.data == null)
        return false;
      for (DataRequirement item : this.data)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public DataRequirement addData() { //3
      DataRequirement t = new DataRequirement();
      if (this.data == null)
        this.data = new ArrayList<DataRequirement>();
      this.data.add(t);
      return t;
    }

    public TriggerDefinition addData(DataRequirement t) { //3
      if (t == null)
        return this;
      if (this.data == null)
        this.data = new ArrayList<DataRequirement>();
      this.data.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #data}, creating it if it does not already exist
     */
    public DataRequirement getDataFirstRep() { 
      if (getData().isEmpty()) {
        addData();
      }
      return getData().get(0);
    }

    /**
     * @return {@link #condition} (A boolean-valued expression that is evaluated in the context of the container of the trigger definition and returns whether or not the trigger fires.)
     */
    public Expression getCondition() { 
      if (this.condition == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create TriggerDefinition.condition");
        else if (Configuration.doAutoCreate())
          this.condition = new Expression(); // cc
      return this.condition;
    }

    public boolean hasCondition() { 
      return this.condition != null && !this.condition.isEmpty();
    }

    /**
     * @param value {@link #condition} (A boolean-valued expression that is evaluated in the context of the container of the trigger definition and returns whether or not the trigger fires.)
     */
    public TriggerDefinition setCondition(Expression value) { 
      this.condition = value;
      return this;
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("type", "code", "The type of triggering event.", 0, 1, type));
        children.add(new Property("name", "string", "A formal name for the event. This may be an absolute URI that identifies the event formally (e.g. from a trigger registry), or a simple relative URI that identifies the event in a local context.", 0, 1, name));
        children.add(new Property("timing[x]", "Timing|Reference(Schedule)|date|dateTime", "The timing of the event (if this is a periodic trigger).", 0, 1, timing));
        children.add(new Property("data", "DataRequirement", "The triggering data of the event (if this is a data trigger). If more than one data is requirement is specified, then all the data requirements must be true.", 0, java.lang.Integer.MAX_VALUE, data));
        children.add(new Property("condition", "Expression", "A boolean-valued expression that is evaluated in the context of the container of the trigger definition and returns whether or not the trigger fires.", 0, 1, condition));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case 3575610: /*type*/  return new Property("type", "code", "The type of triggering event.", 0, 1, type);
        case 3373707: /*name*/  return new Property("name", "string", "A formal name for the event. This may be an absolute URI that identifies the event formally (e.g. from a trigger registry), or a simple relative URI that identifies the event in a local context.", 0, 1, name);
        case 164632566: /*timing[x]*/  return new Property("timing[x]", "Timing|Reference(Schedule)|date|dateTime", "The timing of the event (if this is a periodic trigger).", 0, 1, timing);
        case -873664438: /*timing*/  return new Property("timing[x]", "Timing|Reference(Schedule)|date|dateTime", "The timing of the event (if this is a periodic trigger).", 0, 1, timing);
        case -497554124: /*timingTiming*/  return new Property("timing[x]", "Timing|Reference(Schedule)|date|dateTime", "The timing of the event (if this is a periodic trigger).", 0, 1, timing);
        case -1792466399: /*timingReference*/  return new Property("timing[x]", "Timing|Reference(Schedule)|date|dateTime", "The timing of the event (if this is a periodic trigger).", 0, 1, timing);
        case 807935768: /*timingDate*/  return new Property("timing[x]", "Timing|Reference(Schedule)|date|dateTime", "The timing of the event (if this is a periodic trigger).", 0, 1, timing);
        case -1837458939: /*timingDateTime*/  return new Property("timing[x]", "Timing|Reference(Schedule)|date|dateTime", "The timing of the event (if this is a periodic trigger).", 0, 1, timing);
        case 3076010: /*data*/  return new Property("data", "DataRequirement", "The triggering data of the event (if this is a data trigger). If more than one data is requirement is specified, then all the data requirements must be true.", 0, java.lang.Integer.MAX_VALUE, data);
        case -861311717: /*condition*/  return new Property("condition", "Expression", "A boolean-valued expression that is evaluated in the context of the container of the trigger definition and returns whether or not the trigger fires.", 0, 1, condition);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<TriggerType>
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -873664438: /*timing*/ return this.timing == null ? new Base[0] : new Base[] {this.timing}; // Type
        case 3076010: /*data*/ return this.data == null ? new Base[0] : this.data.toArray(new Base[this.data.size()]); // DataRequirement
        case -861311717: /*condition*/ return this.condition == null ? new Base[0] : new Base[] {this.condition}; // Expression
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
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case -873664438: // timing
          this.timing = castToType(value); // Type
          return value;
        case 3076010: // data
          this.getData().add(castToDataRequirement(value)); // DataRequirement
          return value;
        case -861311717: // condition
          this.condition = castToExpression(value); // Expression
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          value = new TriggerTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<TriggerType>
        } else if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("timing[x]")) {
          this.timing = castToType(value); // Type
        } else if (name.equals("data")) {
          this.getData().add(castToDataRequirement(value));
        } else if (name.equals("condition")) {
          this.condition = castToExpression(value); // Expression
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getTypeElement();
        case 3373707:  return getNameElement();
        case 164632566:  return getTiming(); 
        case -873664438:  return getTiming(); 
        case 3076010:  return addData(); 
        case -861311717:  return getCondition(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"code"};
        case 3373707: /*name*/ return new String[] {"string"};
        case -873664438: /*timing*/ return new String[] {"Timing", "Reference", "date", "dateTime"};
        case 3076010: /*data*/ return new String[] {"DataRequirement"};
        case -861311717: /*condition*/ return new String[] {"Expression"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type TriggerDefinition.type");
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type TriggerDefinition.name");
        }
        else if (name.equals("timingTiming")) {
          this.timing = new Timing();
          return this.timing;
        }
        else if (name.equals("timingReference")) {
          this.timing = new Reference();
          return this.timing;
        }
        else if (name.equals("timingDate")) {
          this.timing = new DateType();
          return this.timing;
        }
        else if (name.equals("timingDateTime")) {
          this.timing = new DateTimeType();
          return this.timing;
        }
        else if (name.equals("data")) {
          return addData();
        }
        else if (name.equals("condition")) {
          this.condition = new Expression();
          return this.condition;
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
        dst.name = name == null ? null : name.copy();
        dst.timing = timing == null ? null : timing.copy();
        if (data != null) {
          dst.data = new ArrayList<DataRequirement>();
          for (DataRequirement i : data)
            dst.data.add(i.copy());
        };
        dst.condition = condition == null ? null : condition.copy();
        return dst;
      }

      protected TriggerDefinition typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof TriggerDefinition))
          return false;
        TriggerDefinition o = (TriggerDefinition) other_;
        return compareDeep(type, o.type, true) && compareDeep(name, o.name, true) && compareDeep(timing, o.timing, true)
           && compareDeep(data, o.data, true) && compareDeep(condition, o.condition, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof TriggerDefinition))
          return false;
        TriggerDefinition o = (TriggerDefinition) other_;
        return compareValues(type, o.type, true) && compareValues(name, o.name, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, name, timing, data
          , condition);
      }


}

