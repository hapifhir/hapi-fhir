package org.hl7.fhir.dstu21.model;

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

// Generated on Sun, Dec 6, 2015 19:25-0500 for FHIR v1.1.0

import java.util.*;

import org.hl7.fhir.dstu21.model.Enumerations.*;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * A guidance request is a request to evaluate a particular knowledge module focused on decision support, providing information relevant to decision support such as workflow and user context.
 */
@ResourceDef(name="GuidanceRequest", profile="http://hl7.org/fhir/Profile/GuidanceRequest")
public class GuidanceRequest extends DomainResource {

    /**
     * A reference to a knowledge module involved in an interaction.
     */
    @Child(name = "module", type = {DecisionSupportRule.class, DecisionSupportServiceModule.class}, order=0, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="A reference to a knowledge module", formalDefinition="A reference to a knowledge module involved in an interaction." )
    protected Reference module;

    /**
     * The actual object that is the target of the reference (A reference to a knowledge module involved in an interaction.)
     */
    protected Resource moduleTarget;

    /**
     * The date and time of the request, with respect to the initiator.
     */
    @Child(name = "dateTime", type = {DateTimeType.class}, order=1, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The date and time of the request", formalDefinition="The date and time of the request, with respect to the initiator." )
    protected DateTimeType dateTime;

    /**
     * Indicates that the evaluation should be performed as though it was the given date and time. The most direct implication of this is that references to "Now" within the evaluation logic of the module should result in this value. In addition, wherever possible, the data accessed by the module should appear as though it was accessed at this time. The evaluateAtDateTime value may be any time in the past or future, enabling both retrospective and prospective scenarios. If no value is provided, the requestDateTime is assumed.
     */
    @Child(name = "evaluateAtDateTime", type = {DateTimeType.class}, order=2, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Indicates that the evaluation should be performed as though it was the given date and time", formalDefinition="Indicates that the evaluation should be performed as though it was the given date and time. The most direct implication of this is that references to \"Now\" within the evaluation logic of the module should result in this value. In addition, wherever possible, the data accessed by the module should appear as though it was accessed at this time. The evaluateAtDateTime value may be any time in the past or future, enabling both retrospective and prospective scenarios. If no value is provided, the requestDateTime is assumed." )
    protected DateTimeType evaluateAtDateTime;

    /**
     * The input parameters for a request, if any. These parameters are used to provide patient-independent information to the evaluation. Patient-specific information is either accessed directly as part of the evaluation (because the evaluation engine and the patient-data are co-located) or provided as part of the operation input in the form of resources.
     */
    @Child(name = "inputParameters", type = {Parameters.class}, order=3, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The input parameters for a request, if any", formalDefinition="The input parameters for a request, if any. These parameters are used to provide patient-independent information to the evaluation. Patient-specific information is either accessed directly as part of the evaluation (because the evaluation engine and the patient-data are co-located) or provided as part of the operation input in the form of resources." )
    protected Reference inputParameters;

    /**
     * The actual object that is the target of the reference (The input parameters for a request, if any. These parameters are used to provide patient-independent information to the evaluation. Patient-specific information is either accessed directly as part of the evaluation (because the evaluation engine and the patient-data are co-located) or provided as part of the operation input in the form of resources.)
     */
    protected Parameters inputParametersTarget;

    /**
     * The organization initiating the request.
     */
    @Child(name = "initiatingOrganization", type = {Organization.class}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="", formalDefinition="The organization initiating the request." )
    protected Reference initiatingOrganization;

    /**
     * The actual object that is the target of the reference (The organization initiating the request.)
     */
    protected Organization initiatingOrganizationTarget;

    /**
     * The person initiating the request.
     */
    @Child(name = "initiatingPerson", type = {Person.class, Patient.class, Practitioner.class, RelatedPerson.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="", formalDefinition="The person initiating the request." )
    protected Reference initiatingPerson;

    /**
     * The actual object that is the target of the reference (The person initiating the request.)
     */
    protected Resource initiatingPersonTarget;

    /**
     * The type of user initiating the request, e.g. patient, healthcare provider, or specific type of healthcare provider (physician, nurse, etc.).
     */
    @Child(name = "userType", type = {CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The type of user initiating the request", formalDefinition="The type of user initiating the request, e.g. patient, healthcare provider, or specific type of healthcare provider (physician, nurse, etc.)." )
    protected CodeableConcept userType;

    /**
     * Preferred language of the person using the system.
     */
    @Child(name = "userLanguage", type = {CodeableConcept.class}, order=7, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="", formalDefinition="Preferred language of the person using the system." )
    protected CodeableConcept userLanguage;

    /**
     * The task the system user is performing, e.g. laboratory results review, medication list review, etc. This information can be used to tailor decision support outputs, such as recommended information resources.
     */
    @Child(name = "userTaskContext", type = {CodeableConcept.class}, order=8, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The task the system user is performing", formalDefinition="The task the system user is performing, e.g. laboratory results review, medication list review, etc. This information can be used to tailor decision support outputs, such as recommended information resources." )
    protected CodeableConcept userTaskContext;

    /**
     * The organization that will receive the response.
     */
    @Child(name = "receivingOrganization", type = {Organization.class}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="", formalDefinition="The organization that will receive the response." )
    protected Reference receivingOrganization;

    /**
     * The actual object that is the target of the reference (The organization that will receive the response.)
     */
    protected Organization receivingOrganizationTarget;

    /**
     * The person in the receiving organization that will receive the response.
     */
    @Child(name = "receivingPerson", type = {Person.class, Patient.class, Practitioner.class, RelatedPerson.class}, order=10, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="", formalDefinition="The person in the receiving organization that will receive the response." )
    protected Reference receivingPerson;

    /**
     * The actual object that is the target of the reference (The person in the receiving organization that will receive the response.)
     */
    protected Resource receivingPersonTarget;

    /**
     * The type of individual that will consume the response content. This may be different from the requesting user type (e.g. if a clinician is getting disease management guidance for provision to a patient). E.g. patient, healthcare provider or specific type of healthcare provider (physician, nurse, etc.).
     */
    @Child(name = "recipientType", type = {CodeableConcept.class}, order=11, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="", formalDefinition="The type of individual that will consume the response content. This may be different from the requesting user type (e.g. if a clinician is getting disease management guidance for provision to a patient). E.g. patient, healthcare provider or specific type of healthcare provider (physician, nurse, etc.)." )
    protected CodeableConcept recipientType;

    /**
     * Preferred language of the person that will consume the content.
     */
    @Child(name = "recipientLanguage", type = {CodeableConcept.class}, order=12, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="", formalDefinition="Preferred language of the person that will consume the content." )
    protected CodeableConcept recipientLanguage;

    /**
     * The class of encounter (inpatient, outpatient, etc).
     */
    @Child(name = "encounterClass", type = {CodeableConcept.class}, order=13, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="", formalDefinition="The class of encounter (inpatient, outpatient, etc)." )
    protected CodeableConcept encounterClass;

    /**
     * The type of the encounter.
     */
    @Child(name = "encounterType", type = {CodeableConcept.class}, order=14, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="", formalDefinition="The type of the encounter." )
    protected CodeableConcept encounterType;

    private static final long serialVersionUID = -1810099048L;

  /*
   * Constructor
   */
    public GuidanceRequest() {
      super();
    }

  /*
   * Constructor
   */
    public GuidanceRequest(Reference module) {
      super();
      this.module = module;
    }

    /**
     * @return {@link #module} (A reference to a knowledge module involved in an interaction.)
     */
    public Reference getModule() { 
      if (this.module == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceRequest.module");
        else if (Configuration.doAutoCreate())
          this.module = new Reference(); // cc
      return this.module;
    }

    public boolean hasModule() { 
      return this.module != null && !this.module.isEmpty();
    }

    /**
     * @param value {@link #module} (A reference to a knowledge module involved in an interaction.)
     */
    public GuidanceRequest setModule(Reference value) { 
      this.module = value;
      return this;
    }

    /**
     * @return {@link #module} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A reference to a knowledge module involved in an interaction.)
     */
    public Resource getModuleTarget() { 
      return this.moduleTarget;
    }

    /**
     * @param value {@link #module} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A reference to a knowledge module involved in an interaction.)
     */
    public GuidanceRequest setModuleTarget(Resource value) { 
      this.moduleTarget = value;
      return this;
    }

    /**
     * @return {@link #dateTime} (The date and time of the request, with respect to the initiator.). This is the underlying object with id, value and extensions. The accessor "getDateTime" gives direct access to the value
     */
    public DateTimeType getDateTimeElement() { 
      if (this.dateTime == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceRequest.dateTime");
        else if (Configuration.doAutoCreate())
          this.dateTime = new DateTimeType(); // bb
      return this.dateTime;
    }

    public boolean hasDateTimeElement() { 
      return this.dateTime != null && !this.dateTime.isEmpty();
    }

    public boolean hasDateTime() { 
      return this.dateTime != null && !this.dateTime.isEmpty();
    }

    /**
     * @param value {@link #dateTime} (The date and time of the request, with respect to the initiator.). This is the underlying object with id, value and extensions. The accessor "getDateTime" gives direct access to the value
     */
    public GuidanceRequest setDateTimeElement(DateTimeType value) { 
      this.dateTime = value;
      return this;
    }

    /**
     * @return The date and time of the request, with respect to the initiator.
     */
    public Date getDateTime() { 
      return this.dateTime == null ? null : this.dateTime.getValue();
    }

    /**
     * @param value The date and time of the request, with respect to the initiator.
     */
    public GuidanceRequest setDateTime(Date value) { 
      if (value == null)
        this.dateTime = null;
      else {
        if (this.dateTime == null)
          this.dateTime = new DateTimeType();
        this.dateTime.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #evaluateAtDateTime} (Indicates that the evaluation should be performed as though it was the given date and time. The most direct implication of this is that references to "Now" within the evaluation logic of the module should result in this value. In addition, wherever possible, the data accessed by the module should appear as though it was accessed at this time. The evaluateAtDateTime value may be any time in the past or future, enabling both retrospective and prospective scenarios. If no value is provided, the requestDateTime is assumed.). This is the underlying object with id, value and extensions. The accessor "getEvaluateAtDateTime" gives direct access to the value
     */
    public DateTimeType getEvaluateAtDateTimeElement() { 
      if (this.evaluateAtDateTime == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceRequest.evaluateAtDateTime");
        else if (Configuration.doAutoCreate())
          this.evaluateAtDateTime = new DateTimeType(); // bb
      return this.evaluateAtDateTime;
    }

    public boolean hasEvaluateAtDateTimeElement() { 
      return this.evaluateAtDateTime != null && !this.evaluateAtDateTime.isEmpty();
    }

    public boolean hasEvaluateAtDateTime() { 
      return this.evaluateAtDateTime != null && !this.evaluateAtDateTime.isEmpty();
    }

    /**
     * @param value {@link #evaluateAtDateTime} (Indicates that the evaluation should be performed as though it was the given date and time. The most direct implication of this is that references to "Now" within the evaluation logic of the module should result in this value. In addition, wherever possible, the data accessed by the module should appear as though it was accessed at this time. The evaluateAtDateTime value may be any time in the past or future, enabling both retrospective and prospective scenarios. If no value is provided, the requestDateTime is assumed.). This is the underlying object with id, value and extensions. The accessor "getEvaluateAtDateTime" gives direct access to the value
     */
    public GuidanceRequest setEvaluateAtDateTimeElement(DateTimeType value) { 
      this.evaluateAtDateTime = value;
      return this;
    }

    /**
     * @return Indicates that the evaluation should be performed as though it was the given date and time. The most direct implication of this is that references to "Now" within the evaluation logic of the module should result in this value. In addition, wherever possible, the data accessed by the module should appear as though it was accessed at this time. The evaluateAtDateTime value may be any time in the past or future, enabling both retrospective and prospective scenarios. If no value is provided, the requestDateTime is assumed.
     */
    public Date getEvaluateAtDateTime() { 
      return this.evaluateAtDateTime == null ? null : this.evaluateAtDateTime.getValue();
    }

    /**
     * @param value Indicates that the evaluation should be performed as though it was the given date and time. The most direct implication of this is that references to "Now" within the evaluation logic of the module should result in this value. In addition, wherever possible, the data accessed by the module should appear as though it was accessed at this time. The evaluateAtDateTime value may be any time in the past or future, enabling both retrospective and prospective scenarios. If no value is provided, the requestDateTime is assumed.
     */
    public GuidanceRequest setEvaluateAtDateTime(Date value) { 
      if (value == null)
        this.evaluateAtDateTime = null;
      else {
        if (this.evaluateAtDateTime == null)
          this.evaluateAtDateTime = new DateTimeType();
        this.evaluateAtDateTime.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #inputParameters} (The input parameters for a request, if any. These parameters are used to provide patient-independent information to the evaluation. Patient-specific information is either accessed directly as part of the evaluation (because the evaluation engine and the patient-data are co-located) or provided as part of the operation input in the form of resources.)
     */
    public Reference getInputParameters() { 
      if (this.inputParameters == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceRequest.inputParameters");
        else if (Configuration.doAutoCreate())
          this.inputParameters = new Reference(); // cc
      return this.inputParameters;
    }

    public boolean hasInputParameters() { 
      return this.inputParameters != null && !this.inputParameters.isEmpty();
    }

    /**
     * @param value {@link #inputParameters} (The input parameters for a request, if any. These parameters are used to provide patient-independent information to the evaluation. Patient-specific information is either accessed directly as part of the evaluation (because the evaluation engine and the patient-data are co-located) or provided as part of the operation input in the form of resources.)
     */
    public GuidanceRequest setInputParameters(Reference value) { 
      this.inputParameters = value;
      return this;
    }

    /**
     * @return {@link #inputParameters} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The input parameters for a request, if any. These parameters are used to provide patient-independent information to the evaluation. Patient-specific information is either accessed directly as part of the evaluation (because the evaluation engine and the patient-data are co-located) or provided as part of the operation input in the form of resources.)
     */
    public Parameters getInputParametersTarget() { 
      if (this.inputParametersTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceRequest.inputParameters");
        else if (Configuration.doAutoCreate())
          this.inputParametersTarget = new Parameters(); // aa
      return this.inputParametersTarget;
    }

    /**
     * @param value {@link #inputParameters} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The input parameters for a request, if any. These parameters are used to provide patient-independent information to the evaluation. Patient-specific information is either accessed directly as part of the evaluation (because the evaluation engine and the patient-data are co-located) or provided as part of the operation input in the form of resources.)
     */
    public GuidanceRequest setInputParametersTarget(Parameters value) { 
      this.inputParametersTarget = value;
      return this;
    }

    /**
     * @return {@link #initiatingOrganization} (The organization initiating the request.)
     */
    public Reference getInitiatingOrganization() { 
      if (this.initiatingOrganization == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceRequest.initiatingOrganization");
        else if (Configuration.doAutoCreate())
          this.initiatingOrganization = new Reference(); // cc
      return this.initiatingOrganization;
    }

    public boolean hasInitiatingOrganization() { 
      return this.initiatingOrganization != null && !this.initiatingOrganization.isEmpty();
    }

    /**
     * @param value {@link #initiatingOrganization} (The organization initiating the request.)
     */
    public GuidanceRequest setInitiatingOrganization(Reference value) { 
      this.initiatingOrganization = value;
      return this;
    }

    /**
     * @return {@link #initiatingOrganization} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The organization initiating the request.)
     */
    public Organization getInitiatingOrganizationTarget() { 
      if (this.initiatingOrganizationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceRequest.initiatingOrganization");
        else if (Configuration.doAutoCreate())
          this.initiatingOrganizationTarget = new Organization(); // aa
      return this.initiatingOrganizationTarget;
    }

    /**
     * @param value {@link #initiatingOrganization} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The organization initiating the request.)
     */
    public GuidanceRequest setInitiatingOrganizationTarget(Organization value) { 
      this.initiatingOrganizationTarget = value;
      return this;
    }

    /**
     * @return {@link #initiatingPerson} (The person initiating the request.)
     */
    public Reference getInitiatingPerson() { 
      if (this.initiatingPerson == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceRequest.initiatingPerson");
        else if (Configuration.doAutoCreate())
          this.initiatingPerson = new Reference(); // cc
      return this.initiatingPerson;
    }

    public boolean hasInitiatingPerson() { 
      return this.initiatingPerson != null && !this.initiatingPerson.isEmpty();
    }

    /**
     * @param value {@link #initiatingPerson} (The person initiating the request.)
     */
    public GuidanceRequest setInitiatingPerson(Reference value) { 
      this.initiatingPerson = value;
      return this;
    }

    /**
     * @return {@link #initiatingPerson} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The person initiating the request.)
     */
    public Resource getInitiatingPersonTarget() { 
      return this.initiatingPersonTarget;
    }

    /**
     * @param value {@link #initiatingPerson} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The person initiating the request.)
     */
    public GuidanceRequest setInitiatingPersonTarget(Resource value) { 
      this.initiatingPersonTarget = value;
      return this;
    }

    /**
     * @return {@link #userType} (The type of user initiating the request, e.g. patient, healthcare provider, or specific type of healthcare provider (physician, nurse, etc.).)
     */
    public CodeableConcept getUserType() { 
      if (this.userType == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceRequest.userType");
        else if (Configuration.doAutoCreate())
          this.userType = new CodeableConcept(); // cc
      return this.userType;
    }

    public boolean hasUserType() { 
      return this.userType != null && !this.userType.isEmpty();
    }

    /**
     * @param value {@link #userType} (The type of user initiating the request, e.g. patient, healthcare provider, or specific type of healthcare provider (physician, nurse, etc.).)
     */
    public GuidanceRequest setUserType(CodeableConcept value) { 
      this.userType = value;
      return this;
    }

    /**
     * @return {@link #userLanguage} (Preferred language of the person using the system.)
     */
    public CodeableConcept getUserLanguage() { 
      if (this.userLanguage == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceRequest.userLanguage");
        else if (Configuration.doAutoCreate())
          this.userLanguage = new CodeableConcept(); // cc
      return this.userLanguage;
    }

    public boolean hasUserLanguage() { 
      return this.userLanguage != null && !this.userLanguage.isEmpty();
    }

    /**
     * @param value {@link #userLanguage} (Preferred language of the person using the system.)
     */
    public GuidanceRequest setUserLanguage(CodeableConcept value) { 
      this.userLanguage = value;
      return this;
    }

    /**
     * @return {@link #userTaskContext} (The task the system user is performing, e.g. laboratory results review, medication list review, etc. This information can be used to tailor decision support outputs, such as recommended information resources.)
     */
    public CodeableConcept getUserTaskContext() { 
      if (this.userTaskContext == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceRequest.userTaskContext");
        else if (Configuration.doAutoCreate())
          this.userTaskContext = new CodeableConcept(); // cc
      return this.userTaskContext;
    }

    public boolean hasUserTaskContext() { 
      return this.userTaskContext != null && !this.userTaskContext.isEmpty();
    }

    /**
     * @param value {@link #userTaskContext} (The task the system user is performing, e.g. laboratory results review, medication list review, etc. This information can be used to tailor decision support outputs, such as recommended information resources.)
     */
    public GuidanceRequest setUserTaskContext(CodeableConcept value) { 
      this.userTaskContext = value;
      return this;
    }

    /**
     * @return {@link #receivingOrganization} (The organization that will receive the response.)
     */
    public Reference getReceivingOrganization() { 
      if (this.receivingOrganization == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceRequest.receivingOrganization");
        else if (Configuration.doAutoCreate())
          this.receivingOrganization = new Reference(); // cc
      return this.receivingOrganization;
    }

    public boolean hasReceivingOrganization() { 
      return this.receivingOrganization != null && !this.receivingOrganization.isEmpty();
    }

    /**
     * @param value {@link #receivingOrganization} (The organization that will receive the response.)
     */
    public GuidanceRequest setReceivingOrganization(Reference value) { 
      this.receivingOrganization = value;
      return this;
    }

    /**
     * @return {@link #receivingOrganization} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The organization that will receive the response.)
     */
    public Organization getReceivingOrganizationTarget() { 
      if (this.receivingOrganizationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceRequest.receivingOrganization");
        else if (Configuration.doAutoCreate())
          this.receivingOrganizationTarget = new Organization(); // aa
      return this.receivingOrganizationTarget;
    }

    /**
     * @param value {@link #receivingOrganization} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The organization that will receive the response.)
     */
    public GuidanceRequest setReceivingOrganizationTarget(Organization value) { 
      this.receivingOrganizationTarget = value;
      return this;
    }

    /**
     * @return {@link #receivingPerson} (The person in the receiving organization that will receive the response.)
     */
    public Reference getReceivingPerson() { 
      if (this.receivingPerson == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceRequest.receivingPerson");
        else if (Configuration.doAutoCreate())
          this.receivingPerson = new Reference(); // cc
      return this.receivingPerson;
    }

    public boolean hasReceivingPerson() { 
      return this.receivingPerson != null && !this.receivingPerson.isEmpty();
    }

    /**
     * @param value {@link #receivingPerson} (The person in the receiving organization that will receive the response.)
     */
    public GuidanceRequest setReceivingPerson(Reference value) { 
      this.receivingPerson = value;
      return this;
    }

    /**
     * @return {@link #receivingPerson} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The person in the receiving organization that will receive the response.)
     */
    public Resource getReceivingPersonTarget() { 
      return this.receivingPersonTarget;
    }

    /**
     * @param value {@link #receivingPerson} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The person in the receiving organization that will receive the response.)
     */
    public GuidanceRequest setReceivingPersonTarget(Resource value) { 
      this.receivingPersonTarget = value;
      return this;
    }

    /**
     * @return {@link #recipientType} (The type of individual that will consume the response content. This may be different from the requesting user type (e.g. if a clinician is getting disease management guidance for provision to a patient). E.g. patient, healthcare provider or specific type of healthcare provider (physician, nurse, etc.).)
     */
    public CodeableConcept getRecipientType() { 
      if (this.recipientType == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceRequest.recipientType");
        else if (Configuration.doAutoCreate())
          this.recipientType = new CodeableConcept(); // cc
      return this.recipientType;
    }

    public boolean hasRecipientType() { 
      return this.recipientType != null && !this.recipientType.isEmpty();
    }

    /**
     * @param value {@link #recipientType} (The type of individual that will consume the response content. This may be different from the requesting user type (e.g. if a clinician is getting disease management guidance for provision to a patient). E.g. patient, healthcare provider or specific type of healthcare provider (physician, nurse, etc.).)
     */
    public GuidanceRequest setRecipientType(CodeableConcept value) { 
      this.recipientType = value;
      return this;
    }

    /**
     * @return {@link #recipientLanguage} (Preferred language of the person that will consume the content.)
     */
    public CodeableConcept getRecipientLanguage() { 
      if (this.recipientLanguage == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceRequest.recipientLanguage");
        else if (Configuration.doAutoCreate())
          this.recipientLanguage = new CodeableConcept(); // cc
      return this.recipientLanguage;
    }

    public boolean hasRecipientLanguage() { 
      return this.recipientLanguage != null && !this.recipientLanguage.isEmpty();
    }

    /**
     * @param value {@link #recipientLanguage} (Preferred language of the person that will consume the content.)
     */
    public GuidanceRequest setRecipientLanguage(CodeableConcept value) { 
      this.recipientLanguage = value;
      return this;
    }

    /**
     * @return {@link #encounterClass} (The class of encounter (inpatient, outpatient, etc).)
     */
    public CodeableConcept getEncounterClass() { 
      if (this.encounterClass == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceRequest.encounterClass");
        else if (Configuration.doAutoCreate())
          this.encounterClass = new CodeableConcept(); // cc
      return this.encounterClass;
    }

    public boolean hasEncounterClass() { 
      return this.encounterClass != null && !this.encounterClass.isEmpty();
    }

    /**
     * @param value {@link #encounterClass} (The class of encounter (inpatient, outpatient, etc).)
     */
    public GuidanceRequest setEncounterClass(CodeableConcept value) { 
      this.encounterClass = value;
      return this;
    }

    /**
     * @return {@link #encounterType} (The type of the encounter.)
     */
    public CodeableConcept getEncounterType() { 
      if (this.encounterType == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceRequest.encounterType");
        else if (Configuration.doAutoCreate())
          this.encounterType = new CodeableConcept(); // cc
      return this.encounterType;
    }

    public boolean hasEncounterType() { 
      return this.encounterType != null && !this.encounterType.isEmpty();
    }

    /**
     * @param value {@link #encounterType} (The type of the encounter.)
     */
    public GuidanceRequest setEncounterType(CodeableConcept value) { 
      this.encounterType = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("module", "Reference(DecisionSupportRule|DecisionSupportServiceModule)", "A reference to a knowledge module involved in an interaction.", 0, java.lang.Integer.MAX_VALUE, module));
        childrenList.add(new Property("dateTime", "dateTime", "The date and time of the request, with respect to the initiator.", 0, java.lang.Integer.MAX_VALUE, dateTime));
        childrenList.add(new Property("evaluateAtDateTime", "dateTime", "Indicates that the evaluation should be performed as though it was the given date and time. The most direct implication of this is that references to \"Now\" within the evaluation logic of the module should result in this value. In addition, wherever possible, the data accessed by the module should appear as though it was accessed at this time. The evaluateAtDateTime value may be any time in the past or future, enabling both retrospective and prospective scenarios. If no value is provided, the requestDateTime is assumed.", 0, java.lang.Integer.MAX_VALUE, evaluateAtDateTime));
        childrenList.add(new Property("inputParameters", "Reference(Parameters)", "The input parameters for a request, if any. These parameters are used to provide patient-independent information to the evaluation. Patient-specific information is either accessed directly as part of the evaluation (because the evaluation engine and the patient-data are co-located) or provided as part of the operation input in the form of resources.", 0, java.lang.Integer.MAX_VALUE, inputParameters));
        childrenList.add(new Property("initiatingOrganization", "Reference(Organization)", "The organization initiating the request.", 0, java.lang.Integer.MAX_VALUE, initiatingOrganization));
        childrenList.add(new Property("initiatingPerson", "Reference(Person|Patient|Practitioner|RelatedPerson)", "The person initiating the request.", 0, java.lang.Integer.MAX_VALUE, initiatingPerson));
        childrenList.add(new Property("userType", "CodeableConcept", "The type of user initiating the request, e.g. patient, healthcare provider, or specific type of healthcare provider (physician, nurse, etc.).", 0, java.lang.Integer.MAX_VALUE, userType));
        childrenList.add(new Property("userLanguage", "CodeableConcept", "Preferred language of the person using the system.", 0, java.lang.Integer.MAX_VALUE, userLanguage));
        childrenList.add(new Property("userTaskContext", "CodeableConcept", "The task the system user is performing, e.g. laboratory results review, medication list review, etc. This information can be used to tailor decision support outputs, such as recommended information resources.", 0, java.lang.Integer.MAX_VALUE, userTaskContext));
        childrenList.add(new Property("receivingOrganization", "Reference(Organization)", "The organization that will receive the response.", 0, java.lang.Integer.MAX_VALUE, receivingOrganization));
        childrenList.add(new Property("receivingPerson", "Reference(Person|Patient|Practitioner|RelatedPerson)", "The person in the receiving organization that will receive the response.", 0, java.lang.Integer.MAX_VALUE, receivingPerson));
        childrenList.add(new Property("recipientType", "CodeableConcept", "The type of individual that will consume the response content. This may be different from the requesting user type (e.g. if a clinician is getting disease management guidance for provision to a patient). E.g. patient, healthcare provider or specific type of healthcare provider (physician, nurse, etc.).", 0, java.lang.Integer.MAX_VALUE, recipientType));
        childrenList.add(new Property("recipientLanguage", "CodeableConcept", "Preferred language of the person that will consume the content.", 0, java.lang.Integer.MAX_VALUE, recipientLanguage));
        childrenList.add(new Property("encounterClass", "CodeableConcept", "The class of encounter (inpatient, outpatient, etc).", 0, java.lang.Integer.MAX_VALUE, encounterClass));
        childrenList.add(new Property("encounterType", "CodeableConcept", "The type of the encounter.", 0, java.lang.Integer.MAX_VALUE, encounterType));
      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("module"))
          this.module = castToReference(value); // Reference
        else if (name.equals("dateTime"))
          this.dateTime = castToDateTime(value); // DateTimeType
        else if (name.equals("evaluateAtDateTime"))
          this.evaluateAtDateTime = castToDateTime(value); // DateTimeType
        else if (name.equals("inputParameters"))
          this.inputParameters = castToReference(value); // Reference
        else if (name.equals("initiatingOrganization"))
          this.initiatingOrganization = castToReference(value); // Reference
        else if (name.equals("initiatingPerson"))
          this.initiatingPerson = castToReference(value); // Reference
        else if (name.equals("userType"))
          this.userType = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("userLanguage"))
          this.userLanguage = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("userTaskContext"))
          this.userTaskContext = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("receivingOrganization"))
          this.receivingOrganization = castToReference(value); // Reference
        else if (name.equals("receivingPerson"))
          this.receivingPerson = castToReference(value); // Reference
        else if (name.equals("recipientType"))
          this.recipientType = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("recipientLanguage"))
          this.recipientLanguage = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("encounterClass"))
          this.encounterClass = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("encounterType"))
          this.encounterType = castToCodeableConcept(value); // CodeableConcept
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("module")) {
          this.module = new Reference();
          return this.module;
        }
        else if (name.equals("dateTime")) {
          throw new FHIRException("Cannot call addChild on a primitive type GuidanceRequest.dateTime");
        }
        else if (name.equals("evaluateAtDateTime")) {
          throw new FHIRException("Cannot call addChild on a primitive type GuidanceRequest.evaluateAtDateTime");
        }
        else if (name.equals("inputParameters")) {
          this.inputParameters = new Reference();
          return this.inputParameters;
        }
        else if (name.equals("initiatingOrganization")) {
          this.initiatingOrganization = new Reference();
          return this.initiatingOrganization;
        }
        else if (name.equals("initiatingPerson")) {
          this.initiatingPerson = new Reference();
          return this.initiatingPerson;
        }
        else if (name.equals("userType")) {
          this.userType = new CodeableConcept();
          return this.userType;
        }
        else if (name.equals("userLanguage")) {
          this.userLanguage = new CodeableConcept();
          return this.userLanguage;
        }
        else if (name.equals("userTaskContext")) {
          this.userTaskContext = new CodeableConcept();
          return this.userTaskContext;
        }
        else if (name.equals("receivingOrganization")) {
          this.receivingOrganization = new Reference();
          return this.receivingOrganization;
        }
        else if (name.equals("receivingPerson")) {
          this.receivingPerson = new Reference();
          return this.receivingPerson;
        }
        else if (name.equals("recipientType")) {
          this.recipientType = new CodeableConcept();
          return this.recipientType;
        }
        else if (name.equals("recipientLanguage")) {
          this.recipientLanguage = new CodeableConcept();
          return this.recipientLanguage;
        }
        else if (name.equals("encounterClass")) {
          this.encounterClass = new CodeableConcept();
          return this.encounterClass;
        }
        else if (name.equals("encounterType")) {
          this.encounterType = new CodeableConcept();
          return this.encounterType;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "GuidanceRequest";

  }

      public GuidanceRequest copy() {
        GuidanceRequest dst = new GuidanceRequest();
        copyValues(dst);
        dst.module = module == null ? null : module.copy();
        dst.dateTime = dateTime == null ? null : dateTime.copy();
        dst.evaluateAtDateTime = evaluateAtDateTime == null ? null : evaluateAtDateTime.copy();
        dst.inputParameters = inputParameters == null ? null : inputParameters.copy();
        dst.initiatingOrganization = initiatingOrganization == null ? null : initiatingOrganization.copy();
        dst.initiatingPerson = initiatingPerson == null ? null : initiatingPerson.copy();
        dst.userType = userType == null ? null : userType.copy();
        dst.userLanguage = userLanguage == null ? null : userLanguage.copy();
        dst.userTaskContext = userTaskContext == null ? null : userTaskContext.copy();
        dst.receivingOrganization = receivingOrganization == null ? null : receivingOrganization.copy();
        dst.receivingPerson = receivingPerson == null ? null : receivingPerson.copy();
        dst.recipientType = recipientType == null ? null : recipientType.copy();
        dst.recipientLanguage = recipientLanguage == null ? null : recipientLanguage.copy();
        dst.encounterClass = encounterClass == null ? null : encounterClass.copy();
        dst.encounterType = encounterType == null ? null : encounterType.copy();
        return dst;
      }

      protected GuidanceRequest typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof GuidanceRequest))
          return false;
        GuidanceRequest o = (GuidanceRequest) other;
        return compareDeep(module, o.module, true) && compareDeep(dateTime, o.dateTime, true) && compareDeep(evaluateAtDateTime, o.evaluateAtDateTime, true)
           && compareDeep(inputParameters, o.inputParameters, true) && compareDeep(initiatingOrganization, o.initiatingOrganization, true)
           && compareDeep(initiatingPerson, o.initiatingPerson, true) && compareDeep(userType, o.userType, true)
           && compareDeep(userLanguage, o.userLanguage, true) && compareDeep(userTaskContext, o.userTaskContext, true)
           && compareDeep(receivingOrganization, o.receivingOrganization, true) && compareDeep(receivingPerson, o.receivingPerson, true)
           && compareDeep(recipientType, o.recipientType, true) && compareDeep(recipientLanguage, o.recipientLanguage, true)
           && compareDeep(encounterClass, o.encounterClass, true) && compareDeep(encounterType, o.encounterType, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof GuidanceRequest))
          return false;
        GuidanceRequest o = (GuidanceRequest) other;
        return compareValues(dateTime, o.dateTime, true) && compareValues(evaluateAtDateTime, o.evaluateAtDateTime, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (module == null || module.isEmpty()) && (dateTime == null || dateTime.isEmpty())
           && (evaluateAtDateTime == null || evaluateAtDateTime.isEmpty()) && (inputParameters == null || inputParameters.isEmpty())
           && (initiatingOrganization == null || initiatingOrganization.isEmpty()) && (initiatingPerson == null || initiatingPerson.isEmpty())
           && (userType == null || userType.isEmpty()) && (userLanguage == null || userLanguage.isEmpty())
           && (userTaskContext == null || userTaskContext.isEmpty()) && (receivingOrganization == null || receivingOrganization.isEmpty())
           && (receivingPerson == null || receivingPerson.isEmpty()) && (recipientType == null || recipientType.isEmpty())
           && (recipientLanguage == null || recipientLanguage.isEmpty()) && (encounterClass == null || encounterClass.isEmpty())
           && (encounterType == null || encounterType.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.GuidanceRequest;
   }


}

