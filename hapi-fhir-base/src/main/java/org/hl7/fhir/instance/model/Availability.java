package org.hl7.fhir.instance.model;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


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

// Generated on Sun, Dec 7, 2014 21:45-0500 for FHIR v0.3.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
/**
 * (informative) A container for slot(s) of time that may be available for booking appointments.
 */
@ResourceDef(name="Availability", profile="http://hl7.org/fhir/Profile/Availability")
public class Availability extends DomainResource {

    /**
     * External Ids for this item.
     */
    @Child(name="identifier", type={Identifier.class}, order=-1, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="External Ids for this item", formalDefinition="External Ids for this item." )
    protected List<Identifier> identifier;

    /**
     * The schedule type can be used for the categorization of healthcare services or other appointment types.
     */
    @Child(name="type", type={CodeableConcept.class}, order=0, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="The schedule type can be used for the categorization of healthcare services or other appointment types", formalDefinition="The schedule type can be used for the categorization of healthcare services or other appointment types." )
    protected List<CodeableConcept> type;

    /**
     * The resource this availability resource is providing availability information for. These are expected to usually be one of HealthcareService, Location, Practitioner, Device, Patient or RelatedPerson.
     */
    @Child(name="actor", type={}, order=1, min=1, max=1)
    @Description(shortDefinition="The resource this availability resource is providing availability information for. These are expected to usually be one of HealthcareService, Location, Practitioner, Device, Patient or RelatedPerson", formalDefinition="The resource this availability resource is providing availability information for. These are expected to usually be one of HealthcareService, Location, Practitioner, Device, Patient or RelatedPerson." )
    protected Reference actor;

    /**
     * The actual object that is the target of the reference (The resource this availability resource is providing availability information for. These are expected to usually be one of HealthcareService, Location, Practitioner, Device, Patient or RelatedPerson.)
     */
    protected Resource actorTarget;

    /**
     * The period of time that the slots that are attached to this availability resource cover (even if none exist). These  cover the amount of time that an organization's planning horizon; the interval for which they are currently accepting appointments. This does not define a "template" for planning outside these dates.
     */
    @Child(name="planningHorizon", type={Period.class}, order=2, min=0, max=1)
    @Description(shortDefinition="The period of time that the slots that are attached to this availability resource cover (even if none exist). These  cover the amount of time that an organization's planning horizon; the interval for which they are currently accepting appointments. This does not define a 'template' for planning outside these dates", formalDefinition="The period of time that the slots that are attached to this availability resource cover (even if none exist). These  cover the amount of time that an organization's planning horizon; the interval for which they are currently accepting appointments. This does not define a 'template' for planning outside these dates." )
    protected Period planningHorizon;

    /**
     * Comments on the availability to describe any extended information. Such as custom constraints on the slot(s) that may be associated.
     */
    @Child(name="comment", type={StringType.class}, order=3, min=0, max=1)
    @Description(shortDefinition="Comments on the availability to describe any extended information. Such as custom constraints on the slot(s) that may be associated", formalDefinition="Comments on the availability to describe any extended information. Such as custom constraints on the slot(s) that may be associated." )
    protected StringType comment;

    /**
     * When this availability was created, or last revised.
     */
    @Child(name="lastModified", type={DateTimeType.class}, order=4, min=0, max=1)
    @Description(shortDefinition="When this availability was created, or last revised", formalDefinition="When this availability was created, or last revised." )
    protected DateTimeType lastModified;

    private static final long serialVersionUID = -461457234L;

    public Availability() {
      super();
    }

    public Availability(Reference actor) {
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

    /**
     * @return {@link #actor} (The resource this availability resource is providing availability information for. These are expected to usually be one of HealthcareService, Location, Practitioner, Device, Patient or RelatedPerson.)
     */
    public Reference getActor() { 
      if (this.actor == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Availability.actor");
        else if (Configuration.doAutoCreate())
          this.actor = new Reference();
      return this.actor;
    }

    public boolean hasActor() { 
      return this.actor != null && !this.actor.isEmpty();
    }

    /**
     * @param value {@link #actor} (The resource this availability resource is providing availability information for. These are expected to usually be one of HealthcareService, Location, Practitioner, Device, Patient or RelatedPerson.)
     */
    public Availability setActor(Reference value) { 
      this.actor = value;
      return this;
    }

    /**
     * @return {@link #actor} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The resource this availability resource is providing availability information for. These are expected to usually be one of HealthcareService, Location, Practitioner, Device, Patient or RelatedPerson.)
     */
    public Resource getActorTarget() { 
      return this.actorTarget;
    }

    /**
     * @param value {@link #actor} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The resource this availability resource is providing availability information for. These are expected to usually be one of HealthcareService, Location, Practitioner, Device, Patient or RelatedPerson.)
     */
    public Availability setActorTarget(Resource value) { 
      this.actorTarget = value;
      return this;
    }

    /**
     * @return {@link #planningHorizon} (The period of time that the slots that are attached to this availability resource cover (even if none exist). These  cover the amount of time that an organization's planning horizon; the interval for which they are currently accepting appointments. This does not define a "template" for planning outside these dates.)
     */
    public Period getPlanningHorizon() { 
      if (this.planningHorizon == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Availability.planningHorizon");
        else if (Configuration.doAutoCreate())
          this.planningHorizon = new Period();
      return this.planningHorizon;
    }

    public boolean hasPlanningHorizon() { 
      return this.planningHorizon != null && !this.planningHorizon.isEmpty();
    }

    /**
     * @param value {@link #planningHorizon} (The period of time that the slots that are attached to this availability resource cover (even if none exist). These  cover the amount of time that an organization's planning horizon; the interval for which they are currently accepting appointments. This does not define a "template" for planning outside these dates.)
     */
    public Availability setPlanningHorizon(Period value) { 
      this.planningHorizon = value;
      return this;
    }

    /**
     * @return {@link #comment} (Comments on the availability to describe any extended information. Such as custom constraints on the slot(s) that may be associated.). This is the underlying object with id, value and extensions. The accessor "getComment" gives direct access to the value
     */
    public StringType getCommentElement() { 
      if (this.comment == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Availability.comment");
        else if (Configuration.doAutoCreate())
          this.comment = new StringType();
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
    public Availability setCommentElement(StringType value) { 
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
    public Availability setComment(String value) { 
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
     * @return {@link #lastModified} (When this availability was created, or last revised.). This is the underlying object with id, value and extensions. The accessor "getLastModified" gives direct access to the value
     */
    public DateTimeType getLastModifiedElement() { 
      if (this.lastModified == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Availability.lastModified");
        else if (Configuration.doAutoCreate())
          this.lastModified = new DateTimeType();
      return this.lastModified;
    }

    public boolean hasLastModifiedElement() { 
      return this.lastModified != null && !this.lastModified.isEmpty();
    }

    public boolean hasLastModified() { 
      return this.lastModified != null && !this.lastModified.isEmpty();
    }

    /**
     * @param value {@link #lastModified} (When this availability was created, or last revised.). This is the underlying object with id, value and extensions. The accessor "getLastModified" gives direct access to the value
     */
    public Availability setLastModifiedElement(DateTimeType value) { 
      this.lastModified = value;
      return this;
    }

    /**
     * @return When this availability was created, or last revised.
     */
    public Date getLastModified() { 
      return this.lastModified == null ? null : this.lastModified.getValue();
    }

    /**
     * @param value When this availability was created, or last revised.
     */
    public Availability setLastModified(Date value) { 
      if (value == null)
        this.lastModified = null;
      else {
        if (this.lastModified == null)
          this.lastModified = new DateTimeType();
        this.lastModified.setValue(value);
      }
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "External Ids for this item.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("type", "CodeableConcept", "The schedule type can be used for the categorization of healthcare services or other appointment types.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("actor", "Reference(Any)", "The resource this availability resource is providing availability information for. These are expected to usually be one of HealthcareService, Location, Practitioner, Device, Patient or RelatedPerson.", 0, java.lang.Integer.MAX_VALUE, actor));
        childrenList.add(new Property("planningHorizon", "Period", "The period of time that the slots that are attached to this availability resource cover (even if none exist). These  cover the amount of time that an organization's planning horizon; the interval for which they are currently accepting appointments. This does not define a 'template' for planning outside these dates.", 0, java.lang.Integer.MAX_VALUE, planningHorizon));
        childrenList.add(new Property("comment", "string", "Comments on the availability to describe any extended information. Such as custom constraints on the slot(s) that may be associated.", 0, java.lang.Integer.MAX_VALUE, comment));
        childrenList.add(new Property("lastModified", "dateTime", "When this availability was created, or last revised.", 0, java.lang.Integer.MAX_VALUE, lastModified));
      }

      public Availability copy() {
        Availability dst = new Availability();
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
        dst.lastModified = lastModified == null ? null : lastModified.copy();
        return dst;
      }

      protected Availability typedCopy() {
        return copy();
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (type == null || type.isEmpty())
           && (actor == null || actor.isEmpty()) && (planningHorizon == null || planningHorizon.isEmpty())
           && (comment == null || comment.isEmpty()) && (lastModified == null || lastModified.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Availability;
   }

  @SearchParamDefinition(name="actor", path="Availability.actor", description="The individual(HealthcareService, Practitioner, Location, ...) to find an availability for", type="reference" )
  public static final String SP_ACTOR = "actor";
  @SearchParamDefinition(name="date", path="Availability.planningHorizon", description="Search for availability resources that have a period that contains this date specified", type="date" )
  public static final String SP_DATE = "date";
  @SearchParamDefinition(name="type", path="Availability.type", description="The type of appointments that can be booked into associated slot(s)", type="token" )
  public static final String SP_TYPE = "type";

}

