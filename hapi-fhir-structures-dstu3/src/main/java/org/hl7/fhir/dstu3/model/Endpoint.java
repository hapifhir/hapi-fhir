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
 * The technical details of an endpoint that can be used for electronic services, such as for web services providing XDS.b or a REST endpoint for another FHIR server. This may include any security context information.
 */
@ResourceDef(name="Endpoint", profile="http://hl7.org/fhir/Profile/Endpoint")
public class Endpoint extends DomainResource {

    public enum EndpointStatus {
        /**
         * This endpoint is expected to be active and can be used
         */
        ACTIVE, 
        /**
         * This endpoint is temporarily unavailable
         */
        SUSPENDED, 
        /**
         * This endpoint has exceeded connectivity thresholds and is considered in an error state and should no longer be attempted to connect to until corrective action is taken
         */
        ERROR, 
        /**
         * This endpoint is no longer to be used
         */
        OFF, 
        /**
         * This instance should not have been part of this patient's medical record.
         */
        ENTEREDINERROR, 
        /**
         * This endpoint is not intended for production usage.
         */
        TEST, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static EndpointStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("suspended".equals(codeString))
          return SUSPENDED;
        if ("error".equals(codeString))
          return ERROR;
        if ("off".equals(codeString))
          return OFF;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if ("test".equals(codeString))
          return TEST;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown EndpointStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ACTIVE: return "active";
            case SUSPENDED: return "suspended";
            case ERROR: return "error";
            case OFF: return "off";
            case ENTEREDINERROR: return "entered-in-error";
            case TEST: return "test";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ACTIVE: return "http://hl7.org/fhir/endpoint-status";
            case SUSPENDED: return "http://hl7.org/fhir/endpoint-status";
            case ERROR: return "http://hl7.org/fhir/endpoint-status";
            case OFF: return "http://hl7.org/fhir/endpoint-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/endpoint-status";
            case TEST: return "http://hl7.org/fhir/endpoint-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ACTIVE: return "This endpoint is expected to be active and can be used";
            case SUSPENDED: return "This endpoint is temporarily unavailable";
            case ERROR: return "This endpoint has exceeded connectivity thresholds and is considered in an error state and should no longer be attempted to connect to until corrective action is taken";
            case OFF: return "This endpoint is no longer to be used";
            case ENTEREDINERROR: return "This instance should not have been part of this patient's medical record.";
            case TEST: return "This endpoint is not intended for production usage.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACTIVE: return "Active";
            case SUSPENDED: return "Suspended";
            case ERROR: return "Error";
            case OFF: return "Off";
            case ENTEREDINERROR: return "Entered in error";
            case TEST: return "Test";
            default: return "?";
          }
        }
    }

  public static class EndpointStatusEnumFactory implements EnumFactory<EndpointStatus> {
    public EndpointStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return EndpointStatus.ACTIVE;
        if ("suspended".equals(codeString))
          return EndpointStatus.SUSPENDED;
        if ("error".equals(codeString))
          return EndpointStatus.ERROR;
        if ("off".equals(codeString))
          return EndpointStatus.OFF;
        if ("entered-in-error".equals(codeString))
          return EndpointStatus.ENTEREDINERROR;
        if ("test".equals(codeString))
          return EndpointStatus.TEST;
        throw new IllegalArgumentException("Unknown EndpointStatus code '"+codeString+"'");
        }
        public Enumeration<EndpointStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<EndpointStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("active".equals(codeString))
          return new Enumeration<EndpointStatus>(this, EndpointStatus.ACTIVE);
        if ("suspended".equals(codeString))
          return new Enumeration<EndpointStatus>(this, EndpointStatus.SUSPENDED);
        if ("error".equals(codeString))
          return new Enumeration<EndpointStatus>(this, EndpointStatus.ERROR);
        if ("off".equals(codeString))
          return new Enumeration<EndpointStatus>(this, EndpointStatus.OFF);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<EndpointStatus>(this, EndpointStatus.ENTEREDINERROR);
        if ("test".equals(codeString))
          return new Enumeration<EndpointStatus>(this, EndpointStatus.TEST);
        throw new FHIRException("Unknown EndpointStatus code '"+codeString+"'");
        }
    public String toCode(EndpointStatus code) {
      if (code == EndpointStatus.ACTIVE)
        return "active";
      if (code == EndpointStatus.SUSPENDED)
        return "suspended";
      if (code == EndpointStatus.ERROR)
        return "error";
      if (code == EndpointStatus.OFF)
        return "off";
      if (code == EndpointStatus.ENTEREDINERROR)
        return "entered-in-error";
      if (code == EndpointStatus.TEST)
        return "test";
      return "?";
      }
    public String toSystem(EndpointStatus code) {
      return code.getSystem();
      }
    }

    /**
     * Identifier for the organization that is used to identify the endpoint across multiple disparate systems.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Identifies this endpoint across multiple systems", formalDefinition="Identifier for the organization that is used to identify the endpoint across multiple disparate systems." )
    protected List<Identifier> identifier;

    /**
     * active | suspended | error | off | test.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="active | suspended | error | off | entered-in-error | test", formalDefinition="active | suspended | error | off | test." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/endpoint-status")
    protected Enumeration<EndpointStatus> status;

    /**
     * A coded value that represents the technical details of the usage of this endpoint, such as what WSDLs should be used in what way. (e.g. XDS.b/DICOM/cds-hook).
     */
    @Child(name = "connectionType", type = {Coding.class}, order=2, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Protocol/Profile/Standard to be used with this endpoint connection", formalDefinition="A coded value that represents the technical details of the usage of this endpoint, such as what WSDLs should be used in what way. (e.g. XDS.b/DICOM/cds-hook)." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/endpoint-connection-type")
    protected Coding connectionType;

    /**
     * A friendly name that this endpoint can be referred to with.
     */
    @Child(name = "name", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="A name that this endpoint can be identified by", formalDefinition="A friendly name that this endpoint can be referred to with." )
    protected StringType name;

    /**
     * The organization that manages this endpoint (even if technically another organisation is hosting this in the cloud, it is the organisation associated with the data).
     */
    @Child(name = "managingOrganization", type = {Organization.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Organization that manages this endpoint (may not be the organization that exposes the endpoint)", formalDefinition="The organization that manages this endpoint (even if technically another organisation is hosting this in the cloud, it is the organisation associated with the data)." )
    protected Reference managingOrganization;

    /**
     * The actual object that is the target of the reference (The organization that manages this endpoint (even if technically another organisation is hosting this in the cloud, it is the organisation associated with the data).)
     */
    protected Organization managingOrganizationTarget;

    /**
     * Contact details for a human to contact about the subscription. The primary use of this for system administrator troubleshooting.
     */
    @Child(name = "contact", type = {ContactPoint.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Contact details for source (e.g. troubleshooting)", formalDefinition="Contact details for a human to contact about the subscription. The primary use of this for system administrator troubleshooting." )
    protected List<ContactPoint> contact;

    /**
     * The interval during which the endpoint is expected to be operational.
     */
    @Child(name = "period", type = {Period.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Interval the endpoint is expected to be operational", formalDefinition="The interval during which the endpoint is expected to be operational." )
    protected Period period;

    /**
     * The payload type describes the acceptable content that can be communicated on the endpoint.
     */
    @Child(name = "payloadType", type = {CodeableConcept.class}, order=7, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="The type of content that may be used at this endpoint (e.g. XDS Discharge summaries)", formalDefinition="The payload type describes the acceptable content that can be communicated on the endpoint." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/endpoint-payload-type")
    protected List<CodeableConcept> payloadType;

    /**
     * The mime type to send the payload in - e.g. application/fhir+xml, application/fhir+json. If the mime type is not specified, then the sender could send any content (including no content depending on the connectionType).
     */
    @Child(name = "payloadMimeType", type = {CodeType.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Mimetype to send. If not specified, the content could be anything (including no payload, if the connectionType defined this)", formalDefinition="The mime type to send the payload in - e.g. application/fhir+xml, application/fhir+json. If the mime type is not specified, then the sender could send any content (including no content depending on the connectionType)." )
    protected List<CodeType> payloadMimeType;

    /**
     * The uri that describes the actual end-point to connect to.
     */
    @Child(name = "address", type = {UriType.class}, order=9, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The technical base address for connecting to this endpoint", formalDefinition="The uri that describes the actual end-point to connect to." )
    protected UriType address;

    /**
     * Additional headers / information to send as part of the notification.
     */
    @Child(name = "header", type = {StringType.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Usage depends on the channel type", formalDefinition="Additional headers / information to send as part of the notification." )
    protected List<StringType> header;

    private static final long serialVersionUID = 694168955L;

  /**
   * Constructor
   */
    public Endpoint() {
      super();
    }

  /**
   * Constructor
   */
    public Endpoint(Enumeration<EndpointStatus> status, Coding connectionType, UriType address) {
      super();
      this.status = status;
      this.connectionType = connectionType;
      this.address = address;
    }

    /**
     * @return {@link #identifier} (Identifier for the organization that is used to identify the endpoint across multiple disparate systems.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Endpoint setIdentifier(List<Identifier> theIdentifier) { 
      this.identifier = theIdentifier;
      return this;
    }

    public boolean hasIdentifier() { 
      if (this.identifier == null)
        return false;
      for (Identifier item : this.identifier)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Identifier addIdentifier() { //3
      Identifier t = new Identifier();
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return t;
    }

    public Endpoint addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #identifier}, creating it if it does not already exist
     */
    public Identifier getIdentifierFirstRep() { 
      if (getIdentifier().isEmpty()) {
        addIdentifier();
      }
      return getIdentifier().get(0);
    }

    /**
     * @return {@link #status} (active | suspended | error | off | test.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<EndpointStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Endpoint.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<EndpointStatus>(new EndpointStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (active | suspended | error | off | test.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Endpoint setStatusElement(Enumeration<EndpointStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return active | suspended | error | off | test.
     */
    public EndpointStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value active | suspended | error | off | test.
     */
    public Endpoint setStatus(EndpointStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<EndpointStatus>(new EndpointStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #connectionType} (A coded value that represents the technical details of the usage of this endpoint, such as what WSDLs should be used in what way. (e.g. XDS.b/DICOM/cds-hook).)
     */
    public Coding getConnectionType() { 
      if (this.connectionType == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Endpoint.connectionType");
        else if (Configuration.doAutoCreate())
          this.connectionType = new Coding(); // cc
      return this.connectionType;
    }

    public boolean hasConnectionType() { 
      return this.connectionType != null && !this.connectionType.isEmpty();
    }

    /**
     * @param value {@link #connectionType} (A coded value that represents the technical details of the usage of this endpoint, such as what WSDLs should be used in what way. (e.g. XDS.b/DICOM/cds-hook).)
     */
    public Endpoint setConnectionType(Coding value) { 
      this.connectionType = value;
      return this;
    }

    /**
     * @return {@link #name} (A friendly name that this endpoint can be referred to with.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Endpoint.name");
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
     * @param value {@link #name} (A friendly name that this endpoint can be referred to with.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public Endpoint setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return A friendly name that this endpoint can be referred to with.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value A friendly name that this endpoint can be referred to with.
     */
    public Endpoint setName(String value) { 
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
     * @return {@link #managingOrganization} (The organization that manages this endpoint (even if technically another organisation is hosting this in the cloud, it is the organisation associated with the data).)
     */
    public Reference getManagingOrganization() { 
      if (this.managingOrganization == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Endpoint.managingOrganization");
        else if (Configuration.doAutoCreate())
          this.managingOrganization = new Reference(); // cc
      return this.managingOrganization;
    }

    public boolean hasManagingOrganization() { 
      return this.managingOrganization != null && !this.managingOrganization.isEmpty();
    }

    /**
     * @param value {@link #managingOrganization} (The organization that manages this endpoint (even if technically another organisation is hosting this in the cloud, it is the organisation associated with the data).)
     */
    public Endpoint setManagingOrganization(Reference value) { 
      this.managingOrganization = value;
      return this;
    }

    /**
     * @return {@link #managingOrganization} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The organization that manages this endpoint (even if technically another organisation is hosting this in the cloud, it is the organisation associated with the data).)
     */
    public Organization getManagingOrganizationTarget() { 
      if (this.managingOrganizationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Endpoint.managingOrganization");
        else if (Configuration.doAutoCreate())
          this.managingOrganizationTarget = new Organization(); // aa
      return this.managingOrganizationTarget;
    }

    /**
     * @param value {@link #managingOrganization} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The organization that manages this endpoint (even if technically another organisation is hosting this in the cloud, it is the organisation associated with the data).)
     */
    public Endpoint setManagingOrganizationTarget(Organization value) { 
      this.managingOrganizationTarget = value;
      return this;
    }

    /**
     * @return {@link #contact} (Contact details for a human to contact about the subscription. The primary use of this for system administrator troubleshooting.)
     */
    public List<ContactPoint> getContact() { 
      if (this.contact == null)
        this.contact = new ArrayList<ContactPoint>();
      return this.contact;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Endpoint setContact(List<ContactPoint> theContact) { 
      this.contact = theContact;
      return this;
    }

    public boolean hasContact() { 
      if (this.contact == null)
        return false;
      for (ContactPoint item : this.contact)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ContactPoint addContact() { //3
      ContactPoint t = new ContactPoint();
      if (this.contact == null)
        this.contact = new ArrayList<ContactPoint>();
      this.contact.add(t);
      return t;
    }

    public Endpoint addContact(ContactPoint t) { //3
      if (t == null)
        return this;
      if (this.contact == null)
        this.contact = new ArrayList<ContactPoint>();
      this.contact.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #contact}, creating it if it does not already exist
     */
    public ContactPoint getContactFirstRep() { 
      if (getContact().isEmpty()) {
        addContact();
      }
      return getContact().get(0);
    }

    /**
     * @return {@link #period} (The interval during which the endpoint is expected to be operational.)
     */
    public Period getPeriod() { 
      if (this.period == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Endpoint.period");
        else if (Configuration.doAutoCreate())
          this.period = new Period(); // cc
      return this.period;
    }

    public boolean hasPeriod() { 
      return this.period != null && !this.period.isEmpty();
    }

    /**
     * @param value {@link #period} (The interval during which the endpoint is expected to be operational.)
     */
    public Endpoint setPeriod(Period value) { 
      this.period = value;
      return this;
    }

    /**
     * @return {@link #payloadType} (The payload type describes the acceptable content that can be communicated on the endpoint.)
     */
    public List<CodeableConcept> getPayloadType() { 
      if (this.payloadType == null)
        this.payloadType = new ArrayList<CodeableConcept>();
      return this.payloadType;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Endpoint setPayloadType(List<CodeableConcept> thePayloadType) { 
      this.payloadType = thePayloadType;
      return this;
    }

    public boolean hasPayloadType() { 
      if (this.payloadType == null)
        return false;
      for (CodeableConcept item : this.payloadType)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addPayloadType() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.payloadType == null)
        this.payloadType = new ArrayList<CodeableConcept>();
      this.payloadType.add(t);
      return t;
    }

    public Endpoint addPayloadType(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.payloadType == null)
        this.payloadType = new ArrayList<CodeableConcept>();
      this.payloadType.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #payloadType}, creating it if it does not already exist
     */
    public CodeableConcept getPayloadTypeFirstRep() { 
      if (getPayloadType().isEmpty()) {
        addPayloadType();
      }
      return getPayloadType().get(0);
    }

    /**
     * @return {@link #payloadMimeType} (The mime type to send the payload in - e.g. application/fhir+xml, application/fhir+json. If the mime type is not specified, then the sender could send any content (including no content depending on the connectionType).)
     */
    public List<CodeType> getPayloadMimeType() { 
      if (this.payloadMimeType == null)
        this.payloadMimeType = new ArrayList<CodeType>();
      return this.payloadMimeType;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Endpoint setPayloadMimeType(List<CodeType> thePayloadMimeType) { 
      this.payloadMimeType = thePayloadMimeType;
      return this;
    }

    public boolean hasPayloadMimeType() { 
      if (this.payloadMimeType == null)
        return false;
      for (CodeType item : this.payloadMimeType)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #payloadMimeType} (The mime type to send the payload in - e.g. application/fhir+xml, application/fhir+json. If the mime type is not specified, then the sender could send any content (including no content depending on the connectionType).)
     */
    public CodeType addPayloadMimeTypeElement() {//2 
      CodeType t = new CodeType();
      if (this.payloadMimeType == null)
        this.payloadMimeType = new ArrayList<CodeType>();
      this.payloadMimeType.add(t);
      return t;
    }

    /**
     * @param value {@link #payloadMimeType} (The mime type to send the payload in - e.g. application/fhir+xml, application/fhir+json. If the mime type is not specified, then the sender could send any content (including no content depending on the connectionType).)
     */
    public Endpoint addPayloadMimeType(String value) { //1
      CodeType t = new CodeType();
      t.setValue(value);
      if (this.payloadMimeType == null)
        this.payloadMimeType = new ArrayList<CodeType>();
      this.payloadMimeType.add(t);
      return this;
    }

    /**
     * @param value {@link #payloadMimeType} (The mime type to send the payload in - e.g. application/fhir+xml, application/fhir+json. If the mime type is not specified, then the sender could send any content (including no content depending on the connectionType).)
     */
    public boolean hasPayloadMimeType(String value) { 
      if (this.payloadMimeType == null)
        return false;
      for (CodeType v : this.payloadMimeType)
        if (v.equals(value)) // code
          return true;
      return false;
    }

    /**
     * @return {@link #address} (The uri that describes the actual end-point to connect to.). This is the underlying object with id, value and extensions. The accessor "getAddress" gives direct access to the value
     */
    public UriType getAddressElement() { 
      if (this.address == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Endpoint.address");
        else if (Configuration.doAutoCreate())
          this.address = new UriType(); // bb
      return this.address;
    }

    public boolean hasAddressElement() { 
      return this.address != null && !this.address.isEmpty();
    }

    public boolean hasAddress() { 
      return this.address != null && !this.address.isEmpty();
    }

    /**
     * @param value {@link #address} (The uri that describes the actual end-point to connect to.). This is the underlying object with id, value and extensions. The accessor "getAddress" gives direct access to the value
     */
    public Endpoint setAddressElement(UriType value) { 
      this.address = value;
      return this;
    }

    /**
     * @return The uri that describes the actual end-point to connect to.
     */
    public String getAddress() { 
      return this.address == null ? null : this.address.getValue();
    }

    /**
     * @param value The uri that describes the actual end-point to connect to.
     */
    public Endpoint setAddress(String value) { 
        if (this.address == null)
          this.address = new UriType();
        this.address.setValue(value);
      return this;
    }

    /**
     * @return {@link #header} (Additional headers / information to send as part of the notification.)
     */
    public List<StringType> getHeader() { 
      if (this.header == null)
        this.header = new ArrayList<StringType>();
      return this.header;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Endpoint setHeader(List<StringType> theHeader) { 
      this.header = theHeader;
      return this;
    }

    public boolean hasHeader() { 
      if (this.header == null)
        return false;
      for (StringType item : this.header)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #header} (Additional headers / information to send as part of the notification.)
     */
    public StringType addHeaderElement() {//2 
      StringType t = new StringType();
      if (this.header == null)
        this.header = new ArrayList<StringType>();
      this.header.add(t);
      return t;
    }

    /**
     * @param value {@link #header} (Additional headers / information to send as part of the notification.)
     */
    public Endpoint addHeader(String value) { //1
      StringType t = new StringType();
      t.setValue(value);
      if (this.header == null)
        this.header = new ArrayList<StringType>();
      this.header.add(t);
      return this;
    }

    /**
     * @param value {@link #header} (Additional headers / information to send as part of the notification.)
     */
    public boolean hasHeader(String value) { 
      if (this.header == null)
        return false;
      for (StringType v : this.header)
        if (v.equals(value)) // string
          return true;
      return false;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Identifier for the organization that is used to identify the endpoint across multiple disparate systems.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("status", "code", "active | suspended | error | off | test.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("connectionType", "Coding", "A coded value that represents the technical details of the usage of this endpoint, such as what WSDLs should be used in what way. (e.g. XDS.b/DICOM/cds-hook).", 0, java.lang.Integer.MAX_VALUE, connectionType));
        childrenList.add(new Property("name", "string", "A friendly name that this endpoint can be referred to with.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("managingOrganization", "Reference(Organization)", "The organization that manages this endpoint (even if technically another organisation is hosting this in the cloud, it is the organisation associated with the data).", 0, java.lang.Integer.MAX_VALUE, managingOrganization));
        childrenList.add(new Property("contact", "ContactPoint", "Contact details for a human to contact about the subscription. The primary use of this for system administrator troubleshooting.", 0, java.lang.Integer.MAX_VALUE, contact));
        childrenList.add(new Property("period", "Period", "The interval during which the endpoint is expected to be operational.", 0, java.lang.Integer.MAX_VALUE, period));
        childrenList.add(new Property("payloadType", "CodeableConcept", "The payload type describes the acceptable content that can be communicated on the endpoint.", 0, java.lang.Integer.MAX_VALUE, payloadType));
        childrenList.add(new Property("payloadMimeType", "code", "The mime type to send the payload in - e.g. application/fhir+xml, application/fhir+json. If the mime type is not specified, then the sender could send any content (including no content depending on the connectionType).", 0, java.lang.Integer.MAX_VALUE, payloadMimeType));
        childrenList.add(new Property("address", "uri", "The uri that describes the actual end-point to connect to.", 0, java.lang.Integer.MAX_VALUE, address));
        childrenList.add(new Property("header", "string", "Additional headers / information to send as part of the notification.", 0, java.lang.Integer.MAX_VALUE, header));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<EndpointStatus>
        case 1270211384: /*connectionType*/ return this.connectionType == null ? new Base[0] : new Base[] {this.connectionType}; // Coding
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -2058947787: /*managingOrganization*/ return this.managingOrganization == null ? new Base[0] : new Base[] {this.managingOrganization}; // Reference
        case 951526432: /*contact*/ return this.contact == null ? new Base[0] : this.contact.toArray(new Base[this.contact.size()]); // ContactPoint
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // Period
        case 909929960: /*payloadType*/ return this.payloadType == null ? new Base[0] : this.payloadType.toArray(new Base[this.payloadType.size()]); // CodeableConcept
        case -1702836932: /*payloadMimeType*/ return this.payloadMimeType == null ? new Base[0] : this.payloadMimeType.toArray(new Base[this.payloadMimeType.size()]); // CodeType
        case -1147692044: /*address*/ return this.address == null ? new Base[0] : new Base[] {this.address}; // UriType
        case -1221270899: /*header*/ return this.header == null ? new Base[0] : this.header.toArray(new Base[this.header.size()]); // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case -892481550: // status
          value = new EndpointStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<EndpointStatus>
          return value;
        case 1270211384: // connectionType
          this.connectionType = castToCoding(value); // Coding
          return value;
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case -2058947787: // managingOrganization
          this.managingOrganization = castToReference(value); // Reference
          return value;
        case 951526432: // contact
          this.getContact().add(castToContactPoint(value)); // ContactPoint
          return value;
        case -991726143: // period
          this.period = castToPeriod(value); // Period
          return value;
        case 909929960: // payloadType
          this.getPayloadType().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1702836932: // payloadMimeType
          this.getPayloadMimeType().add(castToCode(value)); // CodeType
          return value;
        case -1147692044: // address
          this.address = castToUri(value); // UriType
          return value;
        case -1221270899: // header
          this.getHeader().add(castToString(value)); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("status")) {
          value = new EndpointStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<EndpointStatus>
        } else if (name.equals("connectionType")) {
          this.connectionType = castToCoding(value); // Coding
        } else if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("managingOrganization")) {
          this.managingOrganization = castToReference(value); // Reference
        } else if (name.equals("contact")) {
          this.getContact().add(castToContactPoint(value));
        } else if (name.equals("period")) {
          this.period = castToPeriod(value); // Period
        } else if (name.equals("payloadType")) {
          this.getPayloadType().add(castToCodeableConcept(value));
        } else if (name.equals("payloadMimeType")) {
          this.getPayloadMimeType().add(castToCode(value));
        } else if (name.equals("address")) {
          this.address = castToUri(value); // UriType
        } else if (name.equals("header")) {
          this.getHeader().add(castToString(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case -892481550:  return getStatusElement();
        case 1270211384:  return getConnectionType(); 
        case 3373707:  return getNameElement();
        case -2058947787:  return getManagingOrganization(); 
        case 951526432:  return addContact(); 
        case -991726143:  return getPeriod(); 
        case 909929960:  return addPayloadType(); 
        case -1702836932:  return addPayloadMimeTypeElement();
        case -1147692044:  return getAddressElement();
        case -1221270899:  return addHeaderElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -892481550: /*status*/ return new String[] {"code"};
        case 1270211384: /*connectionType*/ return new String[] {"Coding"};
        case 3373707: /*name*/ return new String[] {"string"};
        case -2058947787: /*managingOrganization*/ return new String[] {"Reference"};
        case 951526432: /*contact*/ return new String[] {"ContactPoint"};
        case -991726143: /*period*/ return new String[] {"Period"};
        case 909929960: /*payloadType*/ return new String[] {"CodeableConcept"};
        case -1702836932: /*payloadMimeType*/ return new String[] {"code"};
        case -1147692044: /*address*/ return new String[] {"uri"};
        case -1221270899: /*header*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type Endpoint.status");
        }
        else if (name.equals("connectionType")) {
          this.connectionType = new Coding();
          return this.connectionType;
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type Endpoint.name");
        }
        else if (name.equals("managingOrganization")) {
          this.managingOrganization = new Reference();
          return this.managingOrganization;
        }
        else if (name.equals("contact")) {
          return addContact();
        }
        else if (name.equals("period")) {
          this.period = new Period();
          return this.period;
        }
        else if (name.equals("payloadType")) {
          return addPayloadType();
        }
        else if (name.equals("payloadMimeType")) {
          throw new FHIRException("Cannot call addChild on a primitive type Endpoint.payloadMimeType");
        }
        else if (name.equals("address")) {
          throw new FHIRException("Cannot call addChild on a primitive type Endpoint.address");
        }
        else if (name.equals("header")) {
          throw new FHIRException("Cannot call addChild on a primitive type Endpoint.header");
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Endpoint";

  }

      public Endpoint copy() {
        Endpoint dst = new Endpoint();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.connectionType = connectionType == null ? null : connectionType.copy();
        dst.name = name == null ? null : name.copy();
        dst.managingOrganization = managingOrganization == null ? null : managingOrganization.copy();
        if (contact != null) {
          dst.contact = new ArrayList<ContactPoint>();
          for (ContactPoint i : contact)
            dst.contact.add(i.copy());
        };
        dst.period = period == null ? null : period.copy();
        if (payloadType != null) {
          dst.payloadType = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : payloadType)
            dst.payloadType.add(i.copy());
        };
        if (payloadMimeType != null) {
          dst.payloadMimeType = new ArrayList<CodeType>();
          for (CodeType i : payloadMimeType)
            dst.payloadMimeType.add(i.copy());
        };
        dst.address = address == null ? null : address.copy();
        if (header != null) {
          dst.header = new ArrayList<StringType>();
          for (StringType i : header)
            dst.header.add(i.copy());
        };
        return dst;
      }

      protected Endpoint typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Endpoint))
          return false;
        Endpoint o = (Endpoint) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(connectionType, o.connectionType, true)
           && compareDeep(name, o.name, true) && compareDeep(managingOrganization, o.managingOrganization, true)
           && compareDeep(contact, o.contact, true) && compareDeep(period, o.period, true) && compareDeep(payloadType, o.payloadType, true)
           && compareDeep(payloadMimeType, o.payloadMimeType, true) && compareDeep(address, o.address, true)
           && compareDeep(header, o.header, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Endpoint))
          return false;
        Endpoint o = (Endpoint) other;
        return compareValues(status, o.status, true) && compareValues(name, o.name, true) && compareValues(payloadMimeType, o.payloadMimeType, true)
           && compareValues(address, o.address, true) && compareValues(header, o.header, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, status, connectionType
          , name, managingOrganization, contact, period, payloadType, payloadMimeType, address
          , header);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Endpoint;
   }

 /**
   * Search parameter: <b>payload-type</b>
   * <p>
   * Description: <b>The type of content that may be used at this endpoint (e.g. XDS Discharge summaries)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Endpoint.payloadType</b><br>
   * </p>
   */
  @SearchParamDefinition(name="payload-type", path="Endpoint.payloadType", description="The type of content that may be used at this endpoint (e.g. XDS Discharge summaries)", type="token" )
  public static final String SP_PAYLOAD_TYPE = "payload-type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>payload-type</b>
   * <p>
   * Description: <b>The type of content that may be used at this endpoint (e.g. XDS Discharge summaries)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Endpoint.payloadType</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam PAYLOAD_TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_PAYLOAD_TYPE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Identifies this endpoint across multiple systems</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Endpoint.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="Endpoint.identifier", description="Identifies this endpoint across multiple systems", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Identifies this endpoint across multiple systems</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Endpoint.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>organization</b>
   * <p>
   * Description: <b>The organization that is managing the endpoint</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Endpoint.managingOrganization</b><br>
   * </p>
   */
  @SearchParamDefinition(name="organization", path="Endpoint.managingOrganization", description="The organization that is managing the endpoint", type="reference", target={Organization.class } )
  public static final String SP_ORGANIZATION = "organization";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>organization</b>
   * <p>
   * Description: <b>The organization that is managing the endpoint</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Endpoint.managingOrganization</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ORGANIZATION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ORGANIZATION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Endpoint:organization</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ORGANIZATION = new ca.uhn.fhir.model.api.Include("Endpoint:organization").toLocked();

 /**
   * Search parameter: <b>connection-type</b>
   * <p>
   * Description: <b>Protocol/Profile/Standard to be used with this endpoint connection</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Endpoint.connectionType</b><br>
   * </p>
   */
  @SearchParamDefinition(name="connection-type", path="Endpoint.connectionType", description="Protocol/Profile/Standard to be used with this endpoint connection", type="token" )
  public static final String SP_CONNECTION_TYPE = "connection-type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>connection-type</b>
   * <p>
   * Description: <b>Protocol/Profile/Standard to be used with this endpoint connection</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Endpoint.connectionType</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CONNECTION_TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CONNECTION_TYPE);

 /**
   * Search parameter: <b>name</b>
   * <p>
   * Description: <b>A name that this endpoint can be identified by</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Endpoint.name</b><br>
   * </p>
   */
  @SearchParamDefinition(name="name", path="Endpoint.name", description="A name that this endpoint can be identified by", type="string" )
  public static final String SP_NAME = "name";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>name</b>
   * <p>
   * Description: <b>A name that this endpoint can be identified by</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Endpoint.name</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam NAME = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_NAME);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The current status of the Endpoint (usually expected to be active)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Endpoint.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="Endpoint.status", description="The current status of the Endpoint (usually expected to be active)", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The current status of the Endpoint (usually expected to be active)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Endpoint.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

