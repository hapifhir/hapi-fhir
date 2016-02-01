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
import org.hl7.fhir.instance.model.api.*;
/**
 * A container for slot(s) of time that may be available for booking appointments.
 */
@ResourceDef(name="Schedule", profile="http://hl7.org/fhir/Profile/Schedule")
public class Schedule extends DomainResource {

    /**
     * External Ids for this item.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="External Ids for this item", formalDefinition="External Ids for this item." )
    protected List<Identifier> identifier;

    /**
     * The schedule type can be used for the categorization of healthcare services or other appointment types.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="The schedule type can be used for the categorization of healthcare services or other appointment types", formalDefinition="The schedule type can be used for the categorization of healthcare services or other appointment types." )
    protected List<CodeableConcept> type;

    /**
     * The resource this Schedule resource is providing availability information for. These are expected to usually be one of HealthcareService, Location, Practitioner, Device, Patient or RelatedPerson.
     */
    @Child(name = "actor", type = {Patient.class, Practitioner.class, RelatedPerson.class, Device.class, HealthcareService.class, Location.class}, order=2, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The resource this Schedule resource is providing availability information for. These are expected to usually be one of HealthcareService, Location, Practitioner, Device, Patient or RelatedPerson", formalDefinition="The resource this Schedule resource is providing availability information for. These are expected to usually be one of HealthcareService, Location, Practitioner, Device, Patient or RelatedPerson." )
    protected Reference actor;

    /**
     * The actual object that is the target of the reference (The resource this Schedule resource is providing availability information for. These are expected to usually be one of HealthcareService, Location, Practitioner, Device, Patient or RelatedPerson.)
     */
    protected Resource actorTarget;

    /**
     * The period of time that the slots that are attached to this Schedule resource cover (even if none exist). These  cover the amount of time that an organization's planning horizon; the interval for which they are currently accepting appointments. This does not define a "template" for planning outside these dates.
     */
    @Child(name = "planningHorizon", type = {Period.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The period of time that the slots that are attached to this Schedule resource cover (even if none exist). These  cover the amount of time that an organization's planning horizon; the interval for which they are currently accepting appointments. This does not define a \"template\" for planning outside these dates", formalDefinition="The period of time that the slots that are attached to this Schedule resource cover (even if none exist). These  cover the amount of time that an organization's planning horizon; the interval for which they are currently accepting appointments. This does not define a \"template\" for planning outside these dates." )
    protected Period planningHorizon;

    /**
     * Comments on the availability to describe any extended information. Such as custom constraints on the slot(s) that may be associated.
     */
    @Child(name = "comment", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Comments on the availability to describe any extended information. Such as custom constraints on the slot(s) that may be associated", formalDefinition="Comments on the availability to describe any extended information. Such as custom constraints on the slot(s) that may be associated." )
    protected StringType comment;

    private static final long serialVersionUID = 158030926L;

  /**
   * Constructor
   */
    public Schedule() {
      super();
    }

  /**
   * Constructor
   */
    public Schedule(Reference actor) {
      super();
      this.actor = actor;
    }

    /**
     * @return {@link #identifier} (External Ids for this item.)
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
     * @return {@link #identifier} (External Ids for this item.)
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
    public Schedule addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #type} (The schedule type can be used for the categorization of healthcare services or other appointment types.)
     */
    public List<CodeableConcept> getType() { 
      if (this.type == null)
        this.type = new ArrayList<CodeableConcept>();
      return this.type;
    }

    public boolean hasType() { 
      if (this.type == null)
        return false;
      for (CodeableConcept item : this.type)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #type} (The schedule type can be used for the categorization of healthcare services or other appointment types.)
     */
    // syntactic sugar
    public CodeableConcept addType() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.type == null)
        this.type = new ArrayList<CodeableConcept>();
      this.type.add(t);
      return t;
    }

    // syntactic sugar
    public Schedule addType(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.type == null)
        this.type = new ArrayList<CodeableConcept>();
      this.type.add(t);
      return this;
    }

    /**
     * @return {@link #actor} (The resource this Schedule resource is providing availability information for. These are expected to usually be one of HealthcareService, Location, Practitioner, Device, Patient or RelatedPerson.)
     */
    public Reference getActor() { 
      if (this.actor == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Schedule.actor");
        else if (Configuration.doAutoCreate())
          this.actor = new Reference(); // cc
      return this.actor;
    }

    public boolean hasActor() { 
      return this.actor != null && !this.actor.isEmpty();
    }

    /**
     * @param value {@link #actor} (The resource this Schedule resource is providing availability information for. These are expected to usually be one of HealthcareService, Location, Practitioner, Device, Patient or RelatedPerson.)
     */
    public Schedule setActor(Reference value) { 
      this.actor = value;
      return this;
    }

    /**
     * @return {@link #actor} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The resource this Schedule resource is providing availability information for. These are expected to usually be one of HealthcareService, Location, Practitioner, Device, Patient or RelatedPerson.)
     */
    public Resource getActorTarget() { 
      return this.actorTarget;
    }

    /**
     * @param value {@link #actor} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The resource this Schedule resource is providing availability information for. These are expected to usually be one of HealthcareService, Location, Practitioner, Device, Patient or RelatedPerson.)
     */
    public Schedule setActorTarget(Resource value) { 
      this.actorTarget = value;
      return this;
    }

    /**
     * @return {@link #planningHorizon} (The period of time that the slots that are attached to this Schedule resource cover (even if none exist). These  cover the amount of time that an organization's planning horizon; the interval for which they are currently accepting appointments. This does not define a "template" for planning outside these dates.)
     */
    public Period getPlanningHorizon() { 
      if (this.planningHorizon == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Schedule.planningHorizon");
        else if (Configuration.doAutoCreate())
          this.planningHorizon = new Period(); // cc
      return this.planningHorizon;
    }

    public boolean hasPlanningHorizon() { 
      return this.planningHorizon != null && !this.planningHorizon.isEmpty();
    }

    /**
     * @param value {@link #planningHorizon} (The period of time that the slots that are attached to this Schedule resource cover (even if none exist). These  cover the amount of time that an organization's planning horizon; the interval for which they are currently accepting appointments. This does not define a "template" for planning outside these dates.)
     */
    public Schedule setPlanningHorizon(Period value) { 
      this.planningHorizon = value;
      return this;
    }

    /**
     * @return {@link #comment} (Comments on the availability to describe any extended information. Such as custom constraints on the slot(s) that may be associated.). This is the underlying object with id, value and extensions. The accessor "getComment" gives direct access to the value
     */
    public StringType getCommentElement() { 
      if (this.comment == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Schedule.comment");
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
     * @param value {@link #comment} (Comments on the availability to describe any extended information. Such as custom constraints on the slot(s) that may be associated.). This is the underlying object with id, value and extensions. The accessor "getComment" gives direct access to the value
     */
    public Schedule setCommentElement(StringType value) { 
      this.comment = value;
      return this;
    }

    /**
     * @return Comments on the availability to describe any extended information. Such as custom constraints on the slot(s) that may be associated.
     */
    public String getComment() { 
      return this.comment == null ? null : this.comment.getValue();
    }

    /**
     * @param value Comments on the availability to describe any extended information. Such as custom constraints on the slot(s) that may be associated.
     */
    public Schedule setComment(String value) { 
      if (Utilities.noString(value))
        this.comment = null;
      else {
        if (this.comment == null)
          this.comment = new StringType();
        this.comment.setValue(value);
      }
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "External Ids for this item.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("type", "CodeableConcept", "The schedule type can be used for the categorization of healthcare services or other appointment types.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("actor", "Reference(Patient|Practitioner|RelatedPerson|Device|HealthcareService|Location)", "The resource this Schedule resource is providing availability information for. These are expected to usually be one of HealthcareService, Location, Practitioner, Device, Patient or RelatedPerson.", 0, java.lang.Integer.MAX_VALUE, actor));
        childrenList.add(new Property("planningHorizon", "Period", "The period of time that the slots that are attached to this Schedule resource cover (even if none exist). These  cover the amount of time that an organization's planning horizon; the interval for which they are currently accepting appointments. This does not define a \"template\" for planning outside these dates.", 0, java.lang.Integer.MAX_VALUE, planningHorizon));
        childrenList.add(new Property("comment", "string", "Comments on the availability to describe any extended information. Such as custom constraints on the slot(s) that may be associated.", 0, java.lang.Integer.MAX_VALUE, comment));
      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("type"))
          this.getType().add(castToCodeableConcept(value));
        else if (name.equals("actor"))
          this.actor = castToReference(value); // Reference
        else if (name.equals("planningHorizon"))
          this.planningHorizon = castToPeriod(value); // Period
        else if (name.equals("comment"))
          this.comment = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("type")) {
          return addType();
        }
        else if (name.equals("actor")) {
          this.actor = new Reference();
          return this.actor;
        }
        else if (name.equals("planningHorizon")) {
          this.planningHorizon = new Period();
          return this.planningHorizon;
        }
        else if (name.equals("comment")) {
          throw new FHIRException("Cannot call addChild on a primitive type Schedule.comment");
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Schedule";

  }

      public Schedule copy() {
        Schedule dst = new Schedule();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        if (type != null) {
          dst.type = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : type)
            dst.type.add(i.copy());
        };
        dst.actor = actor == null ? null : actor.copy();
        dst.planningHorizon = planningHorizon == null ? null : planningHorizon.copy();
        dst.comment = comment == null ? null : comment.copy();
        return dst;
      }

      protected Schedule typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Schedule))
          return false;
        Schedule o = (Schedule) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(type, o.type, true) && compareDeep(actor, o.actor, true)
           && compareDeep(planningHorizon, o.planningHorizon, true) && compareDeep(comment, o.comment, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Schedule))
          return false;
        Schedule o = (Schedule) other;
        return compareValues(comment, o.comment, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (type == null || type.isEmpty())
           && (actor == null || actor.isEmpty()) && (planningHorizon == null || planningHorizon.isEmpty())
           && (comment == null || comment.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Schedule;
   }

 /**
   * Search parameter: <b>actor</b>
   * <p>
   * Description: <b>The individual(HealthcareService, Practitioner, Location, ...) to find a Schedule for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Schedule.actor</b><br>
   * </p>
   */
  @SearchParamDefinition(name="actor", path="Schedule.actor", description="The individual(HealthcareService, Practitioner, Location, ...) to find a Schedule for", type="reference" )
  public static final String SP_ACTOR = "actor";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>actor</b>
   * <p>
   * Description: <b>The individual(HealthcareService, Practitioner, Location, ...) to find a Schedule for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Schedule.actor</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ACTOR = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ACTOR);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Schedule:actor</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ACTOR = new ca.uhn.fhir.model.api.Include("Schedule:actor").toLocked();

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>Search for Schedule resources that have a period that contains this date specified</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Schedule.planningHorizon</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="Schedule.planningHorizon", description="Search for Schedule resources that have a period that contains this date specified", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>Search for Schedule resources that have a period that contains this date specified</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Schedule.planningHorizon</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>A Schedule Identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Schedule.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="Schedule.identifier", description="A Schedule Identifier", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>A Schedule Identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Schedule.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>type</b>
   * <p>
   * Description: <b>The type of appointments that can be booked into associated slot(s)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Schedule.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="type", path="Schedule.type", description="The type of appointments that can be booked into associated slot(s)", type="token" )
  public static final String SP_TYPE = "type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>type</b>
   * <p>
   * Description: <b>The type of appointments that can be booked into associated slot(s)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Schedule.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TYPE);


}

