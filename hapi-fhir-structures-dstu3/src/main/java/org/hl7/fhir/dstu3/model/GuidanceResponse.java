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
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * A guidance response is the formal response to a guidance request, including any output parameters returned by the evaluation, as well as the description of any proposed actions to be taken.
 */
@ResourceDef(name="GuidanceResponse", profile="http://hl7.org/fhir/Profile/GuidanceResponse")
public class GuidanceResponse extends DomainResource {

    public enum GuidanceResponseStatus {
        /**
         * The request was processed successfully
         */
        SUCCESS, 
        /**
         * The request was processed successfully, but more data may result in a more complete evaluation
         */
        DATAREQUESTED, 
        /**
         * The request was processed, but more data is required to complete the evaluation
         */
        DATAREQUIRED, 
        /**
         * The request is currently being processed
         */
        INPROGRESS, 
        /**
         * The request was not processed successfully
         */
        FAILURE, 
        /**
         * The response was entered in error
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static GuidanceResponseStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("success".equals(codeString))
          return SUCCESS;
        if ("data-requested".equals(codeString))
          return DATAREQUESTED;
        if ("data-required".equals(codeString))
          return DATAREQUIRED;
        if ("in-progress".equals(codeString))
          return INPROGRESS;
        if ("failure".equals(codeString))
          return FAILURE;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown GuidanceResponseStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case SUCCESS: return "success";
            case DATAREQUESTED: return "data-requested";
            case DATAREQUIRED: return "data-required";
            case INPROGRESS: return "in-progress";
            case FAILURE: return "failure";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case SUCCESS: return "http://hl7.org/fhir/guidance-response-status";
            case DATAREQUESTED: return "http://hl7.org/fhir/guidance-response-status";
            case DATAREQUIRED: return "http://hl7.org/fhir/guidance-response-status";
            case INPROGRESS: return "http://hl7.org/fhir/guidance-response-status";
            case FAILURE: return "http://hl7.org/fhir/guidance-response-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/guidance-response-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case SUCCESS: return "The request was processed successfully";
            case DATAREQUESTED: return "The request was processed successfully, but more data may result in a more complete evaluation";
            case DATAREQUIRED: return "The request was processed, but more data is required to complete the evaluation";
            case INPROGRESS: return "The request is currently being processed";
            case FAILURE: return "The request was not processed successfully";
            case ENTEREDINERROR: return "The response was entered in error";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case SUCCESS: return "Success";
            case DATAREQUESTED: return "Data Requested";
            case DATAREQUIRED: return "Data Required";
            case INPROGRESS: return "In Progress";
            case FAILURE: return "Failure";
            case ENTEREDINERROR: return "Entered In Error";
            default: return "?";
          }
        }
    }

  public static class GuidanceResponseStatusEnumFactory implements EnumFactory<GuidanceResponseStatus> {
    public GuidanceResponseStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("success".equals(codeString))
          return GuidanceResponseStatus.SUCCESS;
        if ("data-requested".equals(codeString))
          return GuidanceResponseStatus.DATAREQUESTED;
        if ("data-required".equals(codeString))
          return GuidanceResponseStatus.DATAREQUIRED;
        if ("in-progress".equals(codeString))
          return GuidanceResponseStatus.INPROGRESS;
        if ("failure".equals(codeString))
          return GuidanceResponseStatus.FAILURE;
        if ("entered-in-error".equals(codeString))
          return GuidanceResponseStatus.ENTEREDINERROR;
        throw new IllegalArgumentException("Unknown GuidanceResponseStatus code '"+codeString+"'");
        }
        public Enumeration<GuidanceResponseStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<GuidanceResponseStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("success".equals(codeString))
          return new Enumeration<GuidanceResponseStatus>(this, GuidanceResponseStatus.SUCCESS);
        if ("data-requested".equals(codeString))
          return new Enumeration<GuidanceResponseStatus>(this, GuidanceResponseStatus.DATAREQUESTED);
        if ("data-required".equals(codeString))
          return new Enumeration<GuidanceResponseStatus>(this, GuidanceResponseStatus.DATAREQUIRED);
        if ("in-progress".equals(codeString))
          return new Enumeration<GuidanceResponseStatus>(this, GuidanceResponseStatus.INPROGRESS);
        if ("failure".equals(codeString))
          return new Enumeration<GuidanceResponseStatus>(this, GuidanceResponseStatus.FAILURE);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<GuidanceResponseStatus>(this, GuidanceResponseStatus.ENTEREDINERROR);
        throw new FHIRException("Unknown GuidanceResponseStatus code '"+codeString+"'");
        }
    public String toCode(GuidanceResponseStatus code) {
      if (code == GuidanceResponseStatus.SUCCESS)
        return "success";
      if (code == GuidanceResponseStatus.DATAREQUESTED)
        return "data-requested";
      if (code == GuidanceResponseStatus.DATAREQUIRED)
        return "data-required";
      if (code == GuidanceResponseStatus.INPROGRESS)
        return "in-progress";
      if (code == GuidanceResponseStatus.FAILURE)
        return "failure";
      if (code == GuidanceResponseStatus.ENTEREDINERROR)
        return "entered-in-error";
      return "?";
      }
    public String toSystem(GuidanceResponseStatus code) {
      return code.getSystem();
      }
    }

    /**
     * The id of the request associated with this response. If an id was given as part of the request, it will be reproduced here to enable the requester to more easily identify the response in a multi-request scenario.
     */
    @Child(name = "requestId", type = {IdType.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The id of the request associated with this response, if any", formalDefinition="The id of the request associated with this response. If an id was given as part of the request, it will be reproduced here to enable the requester to more easily identify the response in a multi-request scenario." )
    protected IdType requestId;

    /**
     * Allows a service to provide a unique, business identifier for the response.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Business identifier", formalDefinition="Allows a service to provide a unique, business identifier for the response." )
    protected Identifier identifier;

    /**
     * A reference to the knowledge module that was invoked.
     */
    @Child(name = "module", type = {ServiceDefinition.class}, order=2, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="A reference to a knowledge module", formalDefinition="A reference to the knowledge module that was invoked." )
    protected Reference module;

    /**
     * The actual object that is the target of the reference (A reference to the knowledge module that was invoked.)
     */
    protected ServiceDefinition moduleTarget;

    /**
     * The status of the response. If the evaluation is completed successfully, the status will indicate success. However, in order to complete the evaluation, the engine may require more information. In this case, the status will be data-required, and the response will contain a description of the additional required information. If the evaluation completed successfully, but the engine determines that a potentially more accurate response could be provided if more data was available, the status will be data-requested, and the response will contain a description of the additional requested information.
     */
    @Child(name = "status", type = {CodeType.class}, order=3, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="success | data-requested | data-required | in-progress | failure | entered-in-error", formalDefinition="The status of the response. If the evaluation is completed successfully, the status will indicate success. However, in order to complete the evaluation, the engine may require more information. In this case, the status will be data-required, and the response will contain a description of the additional required information. If the evaluation completed successfully, but the engine determines that a potentially more accurate response could be provided if more data was available, the status will be data-requested, and the response will contain a description of the additional requested information." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/guidance-response-status")
    protected Enumeration<GuidanceResponseStatus> status;

    /**
     * The patient for which the request was processed.
     */
    @Child(name = "subject", type = {Patient.class, Group.class}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Patient the request was performed for", formalDefinition="The patient for which the request was processed." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (The patient for which the request was processed.)
     */
    protected Resource subjectTarget;

    /**
     * Allows the context of the guidance response to be provided if available. In a service context, this would likely be unavailable.
     */
    @Child(name = "context", type = {Encounter.class, EpisodeOfCare.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Encounter or Episode during which the response was returned", formalDefinition="Allows the context of the guidance response to be provided if available. In a service context, this would likely be unavailable." )
    protected Reference context;

    /**
     * The actual object that is the target of the reference (Allows the context of the guidance response to be provided if available. In a service context, this would likely be unavailable.)
     */
    protected Resource contextTarget;

    /**
     * Indicates when the guidance response was processed.
     */
    @Child(name = "occurrenceDateTime", type = {DateTimeType.class}, order=6, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="When the guidance response was processed", formalDefinition="Indicates when the guidance response was processed." )
    protected DateTimeType occurrenceDateTime;

    /**
     * Provides a reference to the device that performed the guidance.
     */
    @Child(name = "performer", type = {Device.class}, order=7, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Device returning the guidance", formalDefinition="Provides a reference to the device that performed the guidance." )
    protected Reference performer;

    /**
     * The actual object that is the target of the reference (Provides a reference to the device that performed the guidance.)
     */
    protected Device performerTarget;

    /**
     * Indicates the reason the request was initiated. This is typically provided as a parameter to the evaluation and echoed by the service, although for some use cases, such as subscription- or event-based scenarios, it may provide an indication of the cause for the response.
     */
    @Child(name = "reason", type = {CodeableConcept.class, Reference.class}, order=8, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Reason for the response", formalDefinition="Indicates the reason the request was initiated. This is typically provided as a parameter to the evaluation and echoed by the service, although for some use cases, such as subscription- or event-based scenarios, it may provide an indication of the cause for the response." )
    protected Type reason;

    /**
     * Provides a mechanism to communicate additional information about the response.
     */
    @Child(name = "note", type = {Annotation.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Additional notes about the response", formalDefinition="Provides a mechanism to communicate additional information about the response." )
    protected List<Annotation> note;

    /**
     * Messages resulting from the evaluation of the artifact or artifacts. As part of evaluating the request, the engine may produce informational or warning messages. These messages will be provided by this element.
     */
    @Child(name = "evaluationMessage", type = {OperationOutcome.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Messages resulting from the evaluation of the artifact or artifacts", formalDefinition="Messages resulting from the evaluation of the artifact or artifacts. As part of evaluating the request, the engine may produce informational or warning messages. These messages will be provided by this element." )
    protected List<Reference> evaluationMessage;
    /**
     * The actual objects that are the target of the reference (Messages resulting from the evaluation of the artifact or artifacts. As part of evaluating the request, the engine may produce informational or warning messages. These messages will be provided by this element.)
     */
    protected List<OperationOutcome> evaluationMessageTarget;


    /**
     * The output parameters of the evaluation, if any. Many modules will result in the return of specific resources such as procedure or communication requests that are returned as part of the operation result. However, modules may define specific outputs that would be returned as the result of the evaluation, and these would be returned in this element.
     */
    @Child(name = "outputParameters", type = {Parameters.class}, order=11, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The output parameters of the evaluation, if any", formalDefinition="The output parameters of the evaluation, if any. Many modules will result in the return of specific resources such as procedure or communication requests that are returned as part of the operation result. However, modules may define specific outputs that would be returned as the result of the evaluation, and these would be returned in this element." )
    protected Reference outputParameters;

    /**
     * The actual object that is the target of the reference (The output parameters of the evaluation, if any. Many modules will result in the return of specific resources such as procedure or communication requests that are returned as part of the operation result. However, modules may define specific outputs that would be returned as the result of the evaluation, and these would be returned in this element.)
     */
    protected Parameters outputParametersTarget;

    /**
     * The actions, if any, produced by the evaluation of the artifact.
     */
    @Child(name = "result", type = {CarePlan.class, RequestGroup.class}, order=12, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Proposed actions, if any", formalDefinition="The actions, if any, produced by the evaluation of the artifact." )
    protected Reference result;

    /**
     * The actual object that is the target of the reference (The actions, if any, produced by the evaluation of the artifact.)
     */
    protected Resource resultTarget;

    /**
     * If the evaluation could not be completed due to lack of information, or additional information would potentially result in a more accurate response, this element will a description of the data required in order to proceed with the evaluation. A subsequent request to the service should include this data.
     */
    @Child(name = "dataRequirement", type = {DataRequirement.class}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Additional required data", formalDefinition="If the evaluation could not be completed due to lack of information, or additional information would potentially result in a more accurate response, this element will a description of the data required in order to proceed with the evaluation. A subsequent request to the service should include this data." )
    protected List<DataRequirement> dataRequirement;

    private static final long serialVersionUID = -2107029772L;

  /**
   * Constructor
   */
    public GuidanceResponse() {
      super();
    }

  /**
   * Constructor
   */
    public GuidanceResponse(Reference module, Enumeration<GuidanceResponseStatus> status) {
      super();
      this.module = module;
      this.status = status;
    }

    /**
     * @return {@link #requestId} (The id of the request associated with this response. If an id was given as part of the request, it will be reproduced here to enable the requester to more easily identify the response in a multi-request scenario.). This is the underlying object with id, value and extensions. The accessor "getRequestId" gives direct access to the value
     */
    public IdType getRequestIdElement() { 
      if (this.requestId == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceResponse.requestId");
        else if (Configuration.doAutoCreate())
          this.requestId = new IdType(); // bb
      return this.requestId;
    }

    public boolean hasRequestIdElement() { 
      return this.requestId != null && !this.requestId.isEmpty();
    }

    public boolean hasRequestId() { 
      return this.requestId != null && !this.requestId.isEmpty();
    }

    /**
     * @param value {@link #requestId} (The id of the request associated with this response. If an id was given as part of the request, it will be reproduced here to enable the requester to more easily identify the response in a multi-request scenario.). This is the underlying object with id, value and extensions. The accessor "getRequestId" gives direct access to the value
     */
    public GuidanceResponse setRequestIdElement(IdType value) { 
      this.requestId = value;
      return this;
    }

    /**
     * @return The id of the request associated with this response. If an id was given as part of the request, it will be reproduced here to enable the requester to more easily identify the response in a multi-request scenario.
     */
    public String getRequestId() { 
      return this.requestId == null ? null : this.requestId.getValue();
    }

    /**
     * @param value The id of the request associated with this response. If an id was given as part of the request, it will be reproduced here to enable the requester to more easily identify the response in a multi-request scenario.
     */
    public GuidanceResponse setRequestId(String value) { 
      if (Utilities.noString(value))
        this.requestId = null;
      else {
        if (this.requestId == null)
          this.requestId = new IdType();
        this.requestId.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #identifier} (Allows a service to provide a unique, business identifier for the response.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceResponse.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new Identifier(); // cc
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (Allows a service to provide a unique, business identifier for the response.)
     */
    public GuidanceResponse setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #module} (A reference to the knowledge module that was invoked.)
     */
    public Reference getModule() { 
      if (this.module == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceResponse.module");
        else if (Configuration.doAutoCreate())
          this.module = new Reference(); // cc
      return this.module;
    }

    public boolean hasModule() { 
      return this.module != null && !this.module.isEmpty();
    }

    /**
     * @param value {@link #module} (A reference to the knowledge module that was invoked.)
     */
    public GuidanceResponse setModule(Reference value) { 
      this.module = value;
      return this;
    }

    /**
     * @return {@link #module} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A reference to the knowledge module that was invoked.)
     */
    public ServiceDefinition getModuleTarget() { 
      if (this.moduleTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceResponse.module");
        else if (Configuration.doAutoCreate())
          this.moduleTarget = new ServiceDefinition(); // aa
      return this.moduleTarget;
    }

    /**
     * @param value {@link #module} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A reference to the knowledge module that was invoked.)
     */
    public GuidanceResponse setModuleTarget(ServiceDefinition value) { 
      this.moduleTarget = value;
      return this;
    }

    /**
     * @return {@link #status} (The status of the response. If the evaluation is completed successfully, the status will indicate success. However, in order to complete the evaluation, the engine may require more information. In this case, the status will be data-required, and the response will contain a description of the additional required information. If the evaluation completed successfully, but the engine determines that a potentially more accurate response could be provided if more data was available, the status will be data-requested, and the response will contain a description of the additional requested information.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<GuidanceResponseStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceResponse.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<GuidanceResponseStatus>(new GuidanceResponseStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of the response. If the evaluation is completed successfully, the status will indicate success. However, in order to complete the evaluation, the engine may require more information. In this case, the status will be data-required, and the response will contain a description of the additional required information. If the evaluation completed successfully, but the engine determines that a potentially more accurate response could be provided if more data was available, the status will be data-requested, and the response will contain a description of the additional requested information.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public GuidanceResponse setStatusElement(Enumeration<GuidanceResponseStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the response. If the evaluation is completed successfully, the status will indicate success. However, in order to complete the evaluation, the engine may require more information. In this case, the status will be data-required, and the response will contain a description of the additional required information. If the evaluation completed successfully, but the engine determines that a potentially more accurate response could be provided if more data was available, the status will be data-requested, and the response will contain a description of the additional requested information.
     */
    public GuidanceResponseStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the response. If the evaluation is completed successfully, the status will indicate success. However, in order to complete the evaluation, the engine may require more information. In this case, the status will be data-required, and the response will contain a description of the additional required information. If the evaluation completed successfully, but the engine determines that a potentially more accurate response could be provided if more data was available, the status will be data-requested, and the response will contain a description of the additional requested information.
     */
    public GuidanceResponse setStatus(GuidanceResponseStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<GuidanceResponseStatus>(new GuidanceResponseStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #subject} (The patient for which the request was processed.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceResponse.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (The patient for which the request was processed.)
     */
    public GuidanceResponse setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The patient for which the request was processed.)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The patient for which the request was processed.)
     */
    public GuidanceResponse setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #context} (Allows the context of the guidance response to be provided if available. In a service context, this would likely be unavailable.)
     */
    public Reference getContext() { 
      if (this.context == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceResponse.context");
        else if (Configuration.doAutoCreate())
          this.context = new Reference(); // cc
      return this.context;
    }

    public boolean hasContext() { 
      return this.context != null && !this.context.isEmpty();
    }

    /**
     * @param value {@link #context} (Allows the context of the guidance response to be provided if available. In a service context, this would likely be unavailable.)
     */
    public GuidanceResponse setContext(Reference value) { 
      this.context = value;
      return this;
    }

    /**
     * @return {@link #context} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Allows the context of the guidance response to be provided if available. In a service context, this would likely be unavailable.)
     */
    public Resource getContextTarget() { 
      return this.contextTarget;
    }

    /**
     * @param value {@link #context} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Allows the context of the guidance response to be provided if available. In a service context, this would likely be unavailable.)
     */
    public GuidanceResponse setContextTarget(Resource value) { 
      this.contextTarget = value;
      return this;
    }

    /**
     * @return {@link #occurrenceDateTime} (Indicates when the guidance response was processed.). This is the underlying object with id, value and extensions. The accessor "getOccurrenceDateTime" gives direct access to the value
     */
    public DateTimeType getOccurrenceDateTimeElement() { 
      if (this.occurrenceDateTime == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceResponse.occurrenceDateTime");
        else if (Configuration.doAutoCreate())
          this.occurrenceDateTime = new DateTimeType(); // bb
      return this.occurrenceDateTime;
    }

    public boolean hasOccurrenceDateTimeElement() { 
      return this.occurrenceDateTime != null && !this.occurrenceDateTime.isEmpty();
    }

    public boolean hasOccurrenceDateTime() { 
      return this.occurrenceDateTime != null && !this.occurrenceDateTime.isEmpty();
    }

    /**
     * @param value {@link #occurrenceDateTime} (Indicates when the guidance response was processed.). This is the underlying object with id, value and extensions. The accessor "getOccurrenceDateTime" gives direct access to the value
     */
    public GuidanceResponse setOccurrenceDateTimeElement(DateTimeType value) { 
      this.occurrenceDateTime = value;
      return this;
    }

    /**
     * @return Indicates when the guidance response was processed.
     */
    public Date getOccurrenceDateTime() { 
      return this.occurrenceDateTime == null ? null : this.occurrenceDateTime.getValue();
    }

    /**
     * @param value Indicates when the guidance response was processed.
     */
    public GuidanceResponse setOccurrenceDateTime(Date value) { 
      if (value == null)
        this.occurrenceDateTime = null;
      else {
        if (this.occurrenceDateTime == null)
          this.occurrenceDateTime = new DateTimeType();
        this.occurrenceDateTime.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #performer} (Provides a reference to the device that performed the guidance.)
     */
    public Reference getPerformer() { 
      if (this.performer == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceResponse.performer");
        else if (Configuration.doAutoCreate())
          this.performer = new Reference(); // cc
      return this.performer;
    }

    public boolean hasPerformer() { 
      return this.performer != null && !this.performer.isEmpty();
    }

    /**
     * @param value {@link #performer} (Provides a reference to the device that performed the guidance.)
     */
    public GuidanceResponse setPerformer(Reference value) { 
      this.performer = value;
      return this;
    }

    /**
     * @return {@link #performer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Provides a reference to the device that performed the guidance.)
     */
    public Device getPerformerTarget() { 
      if (this.performerTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceResponse.performer");
        else if (Configuration.doAutoCreate())
          this.performerTarget = new Device(); // aa
      return this.performerTarget;
    }

    /**
     * @param value {@link #performer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Provides a reference to the device that performed the guidance.)
     */
    public GuidanceResponse setPerformerTarget(Device value) { 
      this.performerTarget = value;
      return this;
    }

    /**
     * @return {@link #reason} (Indicates the reason the request was initiated. This is typically provided as a parameter to the evaluation and echoed by the service, although for some use cases, such as subscription- or event-based scenarios, it may provide an indication of the cause for the response.)
     */
    public Type getReason() { 
      return this.reason;
    }

    /**
     * @return {@link #reason} (Indicates the reason the request was initiated. This is typically provided as a parameter to the evaluation and echoed by the service, although for some use cases, such as subscription- or event-based scenarios, it may provide an indication of the cause for the response.)
     */
    public CodeableConcept getReasonCodeableConcept() throws FHIRException { 
      if (!(this.reason instanceof CodeableConcept))
        throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.reason.getClass().getName()+" was encountered");
      return (CodeableConcept) this.reason;
    }

    public boolean hasReasonCodeableConcept() { 
      return this.reason instanceof CodeableConcept;
    }

    /**
     * @return {@link #reason} (Indicates the reason the request was initiated. This is typically provided as a parameter to the evaluation and echoed by the service, although for some use cases, such as subscription- or event-based scenarios, it may provide an indication of the cause for the response.)
     */
    public Reference getReasonReference() throws FHIRException { 
      if (!(this.reason instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.reason.getClass().getName()+" was encountered");
      return (Reference) this.reason;
    }

    public boolean hasReasonReference() { 
      return this.reason instanceof Reference;
    }

    public boolean hasReason() { 
      return this.reason != null && !this.reason.isEmpty();
    }

    /**
     * @param value {@link #reason} (Indicates the reason the request was initiated. This is typically provided as a parameter to the evaluation and echoed by the service, although for some use cases, such as subscription- or event-based scenarios, it may provide an indication of the cause for the response.)
     */
    public GuidanceResponse setReason(Type value) { 
      this.reason = value;
      return this;
    }

    /**
     * @return {@link #note} (Provides a mechanism to communicate additional information about the response.)
     */
    public List<Annotation> getNote() { 
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      return this.note;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public GuidanceResponse setNote(List<Annotation> theNote) { 
      this.note = theNote;
      return this;
    }

    public boolean hasNote() { 
      if (this.note == null)
        return false;
      for (Annotation item : this.note)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Annotation addNote() { //3
      Annotation t = new Annotation();
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      this.note.add(t);
      return t;
    }

    public GuidanceResponse addNote(Annotation t) { //3
      if (t == null)
        return this;
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      this.note.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #note}, creating it if it does not already exist
     */
    public Annotation getNoteFirstRep() { 
      if (getNote().isEmpty()) {
        addNote();
      }
      return getNote().get(0);
    }

    /**
     * @return {@link #evaluationMessage} (Messages resulting from the evaluation of the artifact or artifacts. As part of evaluating the request, the engine may produce informational or warning messages. These messages will be provided by this element.)
     */
    public List<Reference> getEvaluationMessage() { 
      if (this.evaluationMessage == null)
        this.evaluationMessage = new ArrayList<Reference>();
      return this.evaluationMessage;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public GuidanceResponse setEvaluationMessage(List<Reference> theEvaluationMessage) { 
      this.evaluationMessage = theEvaluationMessage;
      return this;
    }

    public boolean hasEvaluationMessage() { 
      if (this.evaluationMessage == null)
        return false;
      for (Reference item : this.evaluationMessage)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addEvaluationMessage() { //3
      Reference t = new Reference();
      if (this.evaluationMessage == null)
        this.evaluationMessage = new ArrayList<Reference>();
      this.evaluationMessage.add(t);
      return t;
    }

    public GuidanceResponse addEvaluationMessage(Reference t) { //3
      if (t == null)
        return this;
      if (this.evaluationMessage == null)
        this.evaluationMessage = new ArrayList<Reference>();
      this.evaluationMessage.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #evaluationMessage}, creating it if it does not already exist
     */
    public Reference getEvaluationMessageFirstRep() { 
      if (getEvaluationMessage().isEmpty()) {
        addEvaluationMessage();
      }
      return getEvaluationMessage().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<OperationOutcome> getEvaluationMessageTarget() { 
      if (this.evaluationMessageTarget == null)
        this.evaluationMessageTarget = new ArrayList<OperationOutcome>();
      return this.evaluationMessageTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public OperationOutcome addEvaluationMessageTarget() { 
      OperationOutcome r = new OperationOutcome();
      if (this.evaluationMessageTarget == null)
        this.evaluationMessageTarget = new ArrayList<OperationOutcome>();
      this.evaluationMessageTarget.add(r);
      return r;
    }

    /**
     * @return {@link #outputParameters} (The output parameters of the evaluation, if any. Many modules will result in the return of specific resources such as procedure or communication requests that are returned as part of the operation result. However, modules may define specific outputs that would be returned as the result of the evaluation, and these would be returned in this element.)
     */
    public Reference getOutputParameters() { 
      if (this.outputParameters == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceResponse.outputParameters");
        else if (Configuration.doAutoCreate())
          this.outputParameters = new Reference(); // cc
      return this.outputParameters;
    }

    public boolean hasOutputParameters() { 
      return this.outputParameters != null && !this.outputParameters.isEmpty();
    }

    /**
     * @param value {@link #outputParameters} (The output parameters of the evaluation, if any. Many modules will result in the return of specific resources such as procedure or communication requests that are returned as part of the operation result. However, modules may define specific outputs that would be returned as the result of the evaluation, and these would be returned in this element.)
     */
    public GuidanceResponse setOutputParameters(Reference value) { 
      this.outputParameters = value;
      return this;
    }

    /**
     * @return {@link #outputParameters} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The output parameters of the evaluation, if any. Many modules will result in the return of specific resources such as procedure or communication requests that are returned as part of the operation result. However, modules may define specific outputs that would be returned as the result of the evaluation, and these would be returned in this element.)
     */
    public Parameters getOutputParametersTarget() { 
      if (this.outputParametersTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceResponse.outputParameters");
        else if (Configuration.doAutoCreate())
          this.outputParametersTarget = new Parameters(); // aa
      return this.outputParametersTarget;
    }

    /**
     * @param value {@link #outputParameters} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The output parameters of the evaluation, if any. Many modules will result in the return of specific resources such as procedure or communication requests that are returned as part of the operation result. However, modules may define specific outputs that would be returned as the result of the evaluation, and these would be returned in this element.)
     */
    public GuidanceResponse setOutputParametersTarget(Parameters value) { 
      this.outputParametersTarget = value;
      return this;
    }

    /**
     * @return {@link #result} (The actions, if any, produced by the evaluation of the artifact.)
     */
    public Reference getResult() { 
      if (this.result == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceResponse.result");
        else if (Configuration.doAutoCreate())
          this.result = new Reference(); // cc
      return this.result;
    }

    public boolean hasResult() { 
      return this.result != null && !this.result.isEmpty();
    }

    /**
     * @param value {@link #result} (The actions, if any, produced by the evaluation of the artifact.)
     */
    public GuidanceResponse setResult(Reference value) { 
      this.result = value;
      return this;
    }

    /**
     * @return {@link #result} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The actions, if any, produced by the evaluation of the artifact.)
     */
    public Resource getResultTarget() { 
      return this.resultTarget;
    }

    /**
     * @param value {@link #result} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The actions, if any, produced by the evaluation of the artifact.)
     */
    public GuidanceResponse setResultTarget(Resource value) { 
      this.resultTarget = value;
      return this;
    }

    /**
     * @return {@link #dataRequirement} (If the evaluation could not be completed due to lack of information, or additional information would potentially result in a more accurate response, this element will a description of the data required in order to proceed with the evaluation. A subsequent request to the service should include this data.)
     */
    public List<DataRequirement> getDataRequirement() { 
      if (this.dataRequirement == null)
        this.dataRequirement = new ArrayList<DataRequirement>();
      return this.dataRequirement;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public GuidanceResponse setDataRequirement(List<DataRequirement> theDataRequirement) { 
      this.dataRequirement = theDataRequirement;
      return this;
    }

    public boolean hasDataRequirement() { 
      if (this.dataRequirement == null)
        return false;
      for (DataRequirement item : this.dataRequirement)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public DataRequirement addDataRequirement() { //3
      DataRequirement t = new DataRequirement();
      if (this.dataRequirement == null)
        this.dataRequirement = new ArrayList<DataRequirement>();
      this.dataRequirement.add(t);
      return t;
    }

    public GuidanceResponse addDataRequirement(DataRequirement t) { //3
      if (t == null)
        return this;
      if (this.dataRequirement == null)
        this.dataRequirement = new ArrayList<DataRequirement>();
      this.dataRequirement.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #dataRequirement}, creating it if it does not already exist
     */
    public DataRequirement getDataRequirementFirstRep() { 
      if (getDataRequirement().isEmpty()) {
        addDataRequirement();
      }
      return getDataRequirement().get(0);
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("requestId", "id", "The id of the request associated with this response. If an id was given as part of the request, it will be reproduced here to enable the requester to more easily identify the response in a multi-request scenario.", 0, java.lang.Integer.MAX_VALUE, requestId));
        childrenList.add(new Property("identifier", "Identifier", "Allows a service to provide a unique, business identifier for the response.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("module", "Reference(ServiceDefinition)", "A reference to the knowledge module that was invoked.", 0, java.lang.Integer.MAX_VALUE, module));
        childrenList.add(new Property("status", "code", "The status of the response. If the evaluation is completed successfully, the status will indicate success. However, in order to complete the evaluation, the engine may require more information. In this case, the status will be data-required, and the response will contain a description of the additional required information. If the evaluation completed successfully, but the engine determines that a potentially more accurate response could be provided if more data was available, the status will be data-requested, and the response will contain a description of the additional requested information.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("subject", "Reference(Patient|Group)", "The patient for which the request was processed.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("context", "Reference(Encounter|EpisodeOfCare)", "Allows the context of the guidance response to be provided if available. In a service context, this would likely be unavailable.", 0, java.lang.Integer.MAX_VALUE, context));
        childrenList.add(new Property("occurrenceDateTime", "dateTime", "Indicates when the guidance response was processed.", 0, java.lang.Integer.MAX_VALUE, occurrenceDateTime));
        childrenList.add(new Property("performer", "Reference(Device)", "Provides a reference to the device that performed the guidance.", 0, java.lang.Integer.MAX_VALUE, performer));
        childrenList.add(new Property("reason[x]", "CodeableConcept|Reference(Any)", "Indicates the reason the request was initiated. This is typically provided as a parameter to the evaluation and echoed by the service, although for some use cases, such as subscription- or event-based scenarios, it may provide an indication of the cause for the response.", 0, java.lang.Integer.MAX_VALUE, reason));
        childrenList.add(new Property("note", "Annotation", "Provides a mechanism to communicate additional information about the response.", 0, java.lang.Integer.MAX_VALUE, note));
        childrenList.add(new Property("evaluationMessage", "Reference(OperationOutcome)", "Messages resulting from the evaluation of the artifact or artifacts. As part of evaluating the request, the engine may produce informational or warning messages. These messages will be provided by this element.", 0, java.lang.Integer.MAX_VALUE, evaluationMessage));
        childrenList.add(new Property("outputParameters", "Reference(Parameters)", "The output parameters of the evaluation, if any. Many modules will result in the return of specific resources such as procedure or communication requests that are returned as part of the operation result. However, modules may define specific outputs that would be returned as the result of the evaluation, and these would be returned in this element.", 0, java.lang.Integer.MAX_VALUE, outputParameters));
        childrenList.add(new Property("result", "Reference(CarePlan|RequestGroup)", "The actions, if any, produced by the evaluation of the artifact.", 0, java.lang.Integer.MAX_VALUE, result));
        childrenList.add(new Property("dataRequirement", "DataRequirement", "If the evaluation could not be completed due to lack of information, or additional information would potentially result in a more accurate response, this element will a description of the data required in order to proceed with the evaluation. A subsequent request to the service should include this data.", 0, java.lang.Integer.MAX_VALUE, dataRequirement));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 693933066: /*requestId*/ return this.requestId == null ? new Base[0] : new Base[] {this.requestId}; // IdType
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case -1068784020: /*module*/ return this.module == null ? new Base[0] : new Base[] {this.module}; // Reference
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<GuidanceResponseStatus>
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Reference
        case 951530927: /*context*/ return this.context == null ? new Base[0] : new Base[] {this.context}; // Reference
        case -298443636: /*occurrenceDateTime*/ return this.occurrenceDateTime == null ? new Base[0] : new Base[] {this.occurrenceDateTime}; // DateTimeType
        case 481140686: /*performer*/ return this.performer == null ? new Base[0] : new Base[] {this.performer}; // Reference
        case -934964668: /*reason*/ return this.reason == null ? new Base[0] : new Base[] {this.reason}; // Type
        case 3387378: /*note*/ return this.note == null ? new Base[0] : this.note.toArray(new Base[this.note.size()]); // Annotation
        case 1081619755: /*evaluationMessage*/ return this.evaluationMessage == null ? new Base[0] : this.evaluationMessage.toArray(new Base[this.evaluationMessage.size()]); // Reference
        case 525609419: /*outputParameters*/ return this.outputParameters == null ? new Base[0] : new Base[] {this.outputParameters}; // Reference
        case -934426595: /*result*/ return this.result == null ? new Base[0] : new Base[] {this.result}; // Reference
        case 629147193: /*dataRequirement*/ return this.dataRequirement == null ? new Base[0] : this.dataRequirement.toArray(new Base[this.dataRequirement.size()]); // DataRequirement
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 693933066: // requestId
          this.requestId = castToId(value); // IdType
          return value;
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          return value;
        case -1068784020: // module
          this.module = castToReference(value); // Reference
          return value;
        case -892481550: // status
          value = new GuidanceResponseStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<GuidanceResponseStatus>
          return value;
        case -1867885268: // subject
          this.subject = castToReference(value); // Reference
          return value;
        case 951530927: // context
          this.context = castToReference(value); // Reference
          return value;
        case -298443636: // occurrenceDateTime
          this.occurrenceDateTime = castToDateTime(value); // DateTimeType
          return value;
        case 481140686: // performer
          this.performer = castToReference(value); // Reference
          return value;
        case -934964668: // reason
          this.reason = castToType(value); // Type
          return value;
        case 3387378: // note
          this.getNote().add(castToAnnotation(value)); // Annotation
          return value;
        case 1081619755: // evaluationMessage
          this.getEvaluationMessage().add(castToReference(value)); // Reference
          return value;
        case 525609419: // outputParameters
          this.outputParameters = castToReference(value); // Reference
          return value;
        case -934426595: // result
          this.result = castToReference(value); // Reference
          return value;
        case 629147193: // dataRequirement
          this.getDataRequirement().add(castToDataRequirement(value)); // DataRequirement
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("requestId")) {
          this.requestId = castToId(value); // IdType
        } else if (name.equals("identifier")) {
          this.identifier = castToIdentifier(value); // Identifier
        } else if (name.equals("module")) {
          this.module = castToReference(value); // Reference
        } else if (name.equals("status")) {
          value = new GuidanceResponseStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<GuidanceResponseStatus>
        } else if (name.equals("subject")) {
          this.subject = castToReference(value); // Reference
        } else if (name.equals("context")) {
          this.context = castToReference(value); // Reference
        } else if (name.equals("occurrenceDateTime")) {
          this.occurrenceDateTime = castToDateTime(value); // DateTimeType
        } else if (name.equals("performer")) {
          this.performer = castToReference(value); // Reference
        } else if (name.equals("reason[x]")) {
          this.reason = castToType(value); // Type
        } else if (name.equals("note")) {
          this.getNote().add(castToAnnotation(value));
        } else if (name.equals("evaluationMessage")) {
          this.getEvaluationMessage().add(castToReference(value));
        } else if (name.equals("outputParameters")) {
          this.outputParameters = castToReference(value); // Reference
        } else if (name.equals("result")) {
          this.result = castToReference(value); // Reference
        } else if (name.equals("dataRequirement")) {
          this.getDataRequirement().add(castToDataRequirement(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 693933066:  return getRequestIdElement();
        case -1618432855:  return getIdentifier(); 
        case -1068784020:  return getModule(); 
        case -892481550:  return getStatusElement();
        case -1867885268:  return getSubject(); 
        case 951530927:  return getContext(); 
        case -298443636:  return getOccurrenceDateTimeElement();
        case 481140686:  return getPerformer(); 
        case -669418564:  return getReason(); 
        case -934964668:  return getReason(); 
        case 3387378:  return addNote(); 
        case 1081619755:  return addEvaluationMessage(); 
        case 525609419:  return getOutputParameters(); 
        case -934426595:  return getResult(); 
        case 629147193:  return addDataRequirement(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 693933066: /*requestId*/ return new String[] {"id"};
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -1068784020: /*module*/ return new String[] {"Reference"};
        case -892481550: /*status*/ return new String[] {"code"};
        case -1867885268: /*subject*/ return new String[] {"Reference"};
        case 951530927: /*context*/ return new String[] {"Reference"};
        case -298443636: /*occurrenceDateTime*/ return new String[] {"dateTime"};
        case 481140686: /*performer*/ return new String[] {"Reference"};
        case -934964668: /*reason*/ return new String[] {"CodeableConcept", "Reference"};
        case 3387378: /*note*/ return new String[] {"Annotation"};
        case 1081619755: /*evaluationMessage*/ return new String[] {"Reference"};
        case 525609419: /*outputParameters*/ return new String[] {"Reference"};
        case -934426595: /*result*/ return new String[] {"Reference"};
        case 629147193: /*dataRequirement*/ return new String[] {"DataRequirement"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("requestId")) {
          throw new FHIRException("Cannot call addChild on a primitive type GuidanceResponse.requestId");
        }
        else if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("module")) {
          this.module = new Reference();
          return this.module;
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type GuidanceResponse.status");
        }
        else if (name.equals("subject")) {
          this.subject = new Reference();
          return this.subject;
        }
        else if (name.equals("context")) {
          this.context = new Reference();
          return this.context;
        }
        else if (name.equals("occurrenceDateTime")) {
          throw new FHIRException("Cannot call addChild on a primitive type GuidanceResponse.occurrenceDateTime");
        }
        else if (name.equals("performer")) {
          this.performer = new Reference();
          return this.performer;
        }
        else if (name.equals("reasonCodeableConcept")) {
          this.reason = new CodeableConcept();
          return this.reason;
        }
        else if (name.equals("reasonReference")) {
          this.reason = new Reference();
          return this.reason;
        }
        else if (name.equals("note")) {
          return addNote();
        }
        else if (name.equals("evaluationMessage")) {
          return addEvaluationMessage();
        }
        else if (name.equals("outputParameters")) {
          this.outputParameters = new Reference();
          return this.outputParameters;
        }
        else if (name.equals("result")) {
          this.result = new Reference();
          return this.result;
        }
        else if (name.equals("dataRequirement")) {
          return addDataRequirement();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "GuidanceResponse";

  }

      public GuidanceResponse copy() {
        GuidanceResponse dst = new GuidanceResponse();
        copyValues(dst);
        dst.requestId = requestId == null ? null : requestId.copy();
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.module = module == null ? null : module.copy();
        dst.status = status == null ? null : status.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.context = context == null ? null : context.copy();
        dst.occurrenceDateTime = occurrenceDateTime == null ? null : occurrenceDateTime.copy();
        dst.performer = performer == null ? null : performer.copy();
        dst.reason = reason == null ? null : reason.copy();
        if (note != null) {
          dst.note = new ArrayList<Annotation>();
          for (Annotation i : note)
            dst.note.add(i.copy());
        };
        if (evaluationMessage != null) {
          dst.evaluationMessage = new ArrayList<Reference>();
          for (Reference i : evaluationMessage)
            dst.evaluationMessage.add(i.copy());
        };
        dst.outputParameters = outputParameters == null ? null : outputParameters.copy();
        dst.result = result == null ? null : result.copy();
        if (dataRequirement != null) {
          dst.dataRequirement = new ArrayList<DataRequirement>();
          for (DataRequirement i : dataRequirement)
            dst.dataRequirement.add(i.copy());
        };
        return dst;
      }

      protected GuidanceResponse typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof GuidanceResponse))
          return false;
        GuidanceResponse o = (GuidanceResponse) other;
        return compareDeep(requestId, o.requestId, true) && compareDeep(identifier, o.identifier, true)
           && compareDeep(module, o.module, true) && compareDeep(status, o.status, true) && compareDeep(subject, o.subject, true)
           && compareDeep(context, o.context, true) && compareDeep(occurrenceDateTime, o.occurrenceDateTime, true)
           && compareDeep(performer, o.performer, true) && compareDeep(reason, o.reason, true) && compareDeep(note, o.note, true)
           && compareDeep(evaluationMessage, o.evaluationMessage, true) && compareDeep(outputParameters, o.outputParameters, true)
           && compareDeep(result, o.result, true) && compareDeep(dataRequirement, o.dataRequirement, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof GuidanceResponse))
          return false;
        GuidanceResponse o = (GuidanceResponse) other;
        return compareValues(requestId, o.requestId, true) && compareValues(status, o.status, true) && compareValues(occurrenceDateTime, o.occurrenceDateTime, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(requestId, identifier, module
          , status, subject, context, occurrenceDateTime, performer, reason, note, evaluationMessage
          , outputParameters, result, dataRequirement);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.GuidanceResponse;
   }

 /**
   * Search parameter: <b>request</b>
   * <p>
   * Description: <b>The identifier of the request associated with the response</b><br>
   * Type: <b>token</b><br>
   * Path: <b>GuidanceResponse.requestId</b><br>
   * </p>
   */
  @SearchParamDefinition(name="request", path="GuidanceResponse.requestId", description="The identifier of the request associated with the response", type="token" )
  public static final String SP_REQUEST = "request";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>request</b>
   * <p>
   * Description: <b>The identifier of the request associated with the response</b><br>
   * Type: <b>token</b><br>
   * Path: <b>GuidanceResponse.requestId</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam REQUEST = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_REQUEST);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>The identifier of the guidance response</b><br>
   * Type: <b>token</b><br>
   * Path: <b>GuidanceResponse.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="GuidanceResponse.identifier", description="The identifier of the guidance response", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>The identifier of the guidance response</b><br>
   * Type: <b>token</b><br>
   * Path: <b>GuidanceResponse.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>The identity of a patient to search for guidance response results</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>GuidanceResponse.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="GuidanceResponse.subject", description="The identity of a patient to search for guidance response results", type="reference", target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>The identity of a patient to search for guidance response results</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>GuidanceResponse.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>GuidanceResponse:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("GuidanceResponse:patient").toLocked();

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>The subject that the guidance response is about</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>GuidanceResponse.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="GuidanceResponse.subject", description="The subject that the guidance response is about", type="reference", target={Group.class, Patient.class } )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>The subject that the guidance response is about</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>GuidanceResponse.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>GuidanceResponse:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("GuidanceResponse:subject").toLocked();


}

