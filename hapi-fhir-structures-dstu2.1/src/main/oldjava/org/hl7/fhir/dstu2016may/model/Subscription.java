package org.hl7.fhir.dstu2016may.model;

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

// Generated on Sun, May 8, 2016 03:05+1000 for FHIR v1.4.0
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.utilities.Utilities;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
/**
 * The subscription resource is used to define a push based subscription from a server to another system. Once a subscription is registered with the server, the server checks every resource that is created or updated, and if the resource matches the given criteria, it sends a message on the defined "channel" so that another system is able to take an appropriate action.
 */
@ResourceDef(name="Subscription", profile="http://hl7.org/fhir/Profile/Subscription")
public class Subscription extends DomainResource {

    public enum SubscriptionStatus {
        /**
         * The client has requested the subscription, and the server has not yet set it up.
         */
        REQUESTED, 
        /**
         * The subscription is active.
         */
        ACTIVE, 
        /**
         * The server has an error executing the notification.
         */
        ERROR, 
        /**
         * Too many errors have occurred or the subscription has expired.
         */
        OFF, 
        /**
         * added to help the parsers
         */
        NULL;
        public static SubscriptionStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("requested".equals(codeString))
          return REQUESTED;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("error".equals(codeString))
          return ERROR;
        if ("off".equals(codeString))
          return OFF;
        throw new FHIRException("Unknown SubscriptionStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case REQUESTED: return "requested";
            case ACTIVE: return "active";
            case ERROR: return "error";
            case OFF: return "off";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case REQUESTED: return "http://hl7.org/fhir/subscription-status";
            case ACTIVE: return "http://hl7.org/fhir/subscription-status";
            case ERROR: return "http://hl7.org/fhir/subscription-status";
            case OFF: return "http://hl7.org/fhir/subscription-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case REQUESTED: return "The client has requested the subscription, and the server has not yet set it up.";
            case ACTIVE: return "The subscription is active.";
            case ERROR: return "The server has an error executing the notification.";
            case OFF: return "Too many errors have occurred or the subscription has expired.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case REQUESTED: return "Requested";
            case ACTIVE: return "Active";
            case ERROR: return "Error";
            case OFF: return "Off";
            default: return "?";
          }
        }
    }

  public static class SubscriptionStatusEnumFactory implements EnumFactory<SubscriptionStatus> {
    public SubscriptionStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("requested".equals(codeString))
          return SubscriptionStatus.REQUESTED;
        if ("active".equals(codeString))
          return SubscriptionStatus.ACTIVE;
        if ("error".equals(codeString))
          return SubscriptionStatus.ERROR;
        if ("off".equals(codeString))
          return SubscriptionStatus.OFF;
        throw new IllegalArgumentException("Unknown SubscriptionStatus code '"+codeString+"'");
        }
        public Enumeration<SubscriptionStatus> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("requested".equals(codeString))
          return new Enumeration<SubscriptionStatus>(this, SubscriptionStatus.REQUESTED);
        if ("active".equals(codeString))
          return new Enumeration<SubscriptionStatus>(this, SubscriptionStatus.ACTIVE);
        if ("error".equals(codeString))
          return new Enumeration<SubscriptionStatus>(this, SubscriptionStatus.ERROR);
        if ("off".equals(codeString))
          return new Enumeration<SubscriptionStatus>(this, SubscriptionStatus.OFF);
        throw new FHIRException("Unknown SubscriptionStatus code '"+codeString+"'");
        }
    public String toCode(SubscriptionStatus code) {
      if (code == SubscriptionStatus.REQUESTED)
        return "requested";
      if (code == SubscriptionStatus.ACTIVE)
        return "active";
      if (code == SubscriptionStatus.ERROR)
        return "error";
      if (code == SubscriptionStatus.OFF)
        return "off";
      return "?";
      }
    public String toSystem(SubscriptionStatus code) {
      return code.getSystem();
      }
    }

    public enum SubscriptionChannelType {
        /**
         * The channel is executed by making a post to the URI. If a payload is included, the URL is interpreted as the service base, and an update (PUT) is made.
         */
        RESTHOOK, 
        /**
         * The channel is executed by sending a packet across a web socket connection maintained by the client. The URL identifies the websocket, and the client binds to this URL.
         */
        WEBSOCKET, 
        /**
         * The channel is executed by sending an email to the email addressed in the URI (which must be a mailto:).
         */
        EMAIL, 
        /**
         * The channel is executed by sending an SMS message to the phone number identified in the URL (tel:).
         */
        SMS, 
        /**
         * The channel is executed by sending a message (e.g. a Bundle with a MessageHeader resource etc.) to the application identified in the URI.
         */
        MESSAGE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static SubscriptionChannelType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("rest-hook".equals(codeString))
          return RESTHOOK;
        if ("websocket".equals(codeString))
          return WEBSOCKET;
        if ("email".equals(codeString))
          return EMAIL;
        if ("sms".equals(codeString))
          return SMS;
        if ("message".equals(codeString))
          return MESSAGE;
        throw new FHIRException("Unknown SubscriptionChannelType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case RESTHOOK: return "rest-hook";
            case WEBSOCKET: return "websocket";
            case EMAIL: return "email";
            case SMS: return "sms";
            case MESSAGE: return "message";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case RESTHOOK: return "http://hl7.org/fhir/subscription-channel-type";
            case WEBSOCKET: return "http://hl7.org/fhir/subscription-channel-type";
            case EMAIL: return "http://hl7.org/fhir/subscription-channel-type";
            case SMS: return "http://hl7.org/fhir/subscription-channel-type";
            case MESSAGE: return "http://hl7.org/fhir/subscription-channel-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case RESTHOOK: return "The channel is executed by making a post to the URI. If a payload is included, the URL is interpreted as the service base, and an update (PUT) is made.";
            case WEBSOCKET: return "The channel is executed by sending a packet across a web socket connection maintained by the client. The URL identifies the websocket, and the client binds to this URL.";
            case EMAIL: return "The channel is executed by sending an email to the email addressed in the URI (which must be a mailto:).";
            case SMS: return "The channel is executed by sending an SMS message to the phone number identified in the URL (tel:).";
            case MESSAGE: return "The channel is executed by sending a message (e.g. a Bundle with a MessageHeader resource etc.) to the application identified in the URI.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case RESTHOOK: return "Rest Hook";
            case WEBSOCKET: return "Websocket";
            case EMAIL: return "Email";
            case SMS: return "SMS";
            case MESSAGE: return "Message";
            default: return "?";
          }
        }
    }

  public static class SubscriptionChannelTypeEnumFactory implements EnumFactory<SubscriptionChannelType> {
    public SubscriptionChannelType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("rest-hook".equals(codeString))
          return SubscriptionChannelType.RESTHOOK;
        if ("websocket".equals(codeString))
          return SubscriptionChannelType.WEBSOCKET;
        if ("email".equals(codeString))
          return SubscriptionChannelType.EMAIL;
        if ("sms".equals(codeString))
          return SubscriptionChannelType.SMS;
        if ("message".equals(codeString))
          return SubscriptionChannelType.MESSAGE;
        throw new IllegalArgumentException("Unknown SubscriptionChannelType code '"+codeString+"'");
        }
        public Enumeration<SubscriptionChannelType> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("rest-hook".equals(codeString))
          return new Enumeration<SubscriptionChannelType>(this, SubscriptionChannelType.RESTHOOK);
        if ("websocket".equals(codeString))
          return new Enumeration<SubscriptionChannelType>(this, SubscriptionChannelType.WEBSOCKET);
        if ("email".equals(codeString))
          return new Enumeration<SubscriptionChannelType>(this, SubscriptionChannelType.EMAIL);
        if ("sms".equals(codeString))
          return new Enumeration<SubscriptionChannelType>(this, SubscriptionChannelType.SMS);
        if ("message".equals(codeString))
          return new Enumeration<SubscriptionChannelType>(this, SubscriptionChannelType.MESSAGE);
        throw new FHIRException("Unknown SubscriptionChannelType code '"+codeString+"'");
        }
    public String toCode(SubscriptionChannelType code) {
      if (code == SubscriptionChannelType.RESTHOOK)
        return "rest-hook";
      if (code == SubscriptionChannelType.WEBSOCKET)
        return "websocket";
      if (code == SubscriptionChannelType.EMAIL)
        return "email";
      if (code == SubscriptionChannelType.SMS)
        return "sms";
      if (code == SubscriptionChannelType.MESSAGE)
        return "message";
      return "?";
      }
    public String toSystem(SubscriptionChannelType code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class SubscriptionChannelComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The type of channel to send notifications on.
         */
        @Child(name = "type", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="rest-hook | websocket | email | sms | message", formalDefinition="The type of channel to send notifications on." )
        protected Enumeration<SubscriptionChannelType> type;

        /**
         * The uri that describes the actual end-point to send messages to.
         */
        @Child(name = "endpoint", type = {UriType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Where the channel points to", formalDefinition="The uri that describes the actual end-point to send messages to." )
        protected UriType endpoint;

        /**
         * The mime type to send the payload in - either application/xml+fhir, or application/json+fhir. If the mime type is blank, then there is no payload in the notification, just a notification.
         */
        @Child(name = "payload", type = {StringType.class}, order=3, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Mimetype to send, or blank for no payload", formalDefinition="The mime type to send the payload in - either application/xml+fhir, or application/json+fhir. If the mime type is blank, then there is no payload in the notification, just a notification." )
        protected StringType payload;

        /**
         * Additional headers / information to send as part of the notification.
         */
        @Child(name = "header", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Usage depends on the channel type", formalDefinition="Additional headers / information to send as part of the notification." )
        protected StringType header;

        private static final long serialVersionUID = -279715391L;

    /**
     * Constructor
     */
      public SubscriptionChannelComponent() {
        super();
      }

    /**
     * Constructor
     */
      public SubscriptionChannelComponent(Enumeration<SubscriptionChannelType> type, StringType payload) {
        super();
        this.type = type;
        this.payload = payload;
      }

        /**
         * @return {@link #type} (The type of channel to send notifications on.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<SubscriptionChannelType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubscriptionChannelComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Enumeration<SubscriptionChannelType>(new SubscriptionChannelTypeEnumFactory()); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of channel to send notifications on.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public SubscriptionChannelComponent setTypeElement(Enumeration<SubscriptionChannelType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return The type of channel to send notifications on.
         */
        public SubscriptionChannelType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value The type of channel to send notifications on.
         */
        public SubscriptionChannelComponent setType(SubscriptionChannelType value) { 
            if (this.type == null)
              this.type = new Enumeration<SubscriptionChannelType>(new SubscriptionChannelTypeEnumFactory());
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #endpoint} (The uri that describes the actual end-point to send messages to.). This is the underlying object with id, value and extensions. The accessor "getEndpoint" gives direct access to the value
         */
        public UriType getEndpointElement() { 
          if (this.endpoint == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubscriptionChannelComponent.endpoint");
            else if (Configuration.doAutoCreate())
              this.endpoint = new UriType(); // bb
          return this.endpoint;
        }

        public boolean hasEndpointElement() { 
          return this.endpoint != null && !this.endpoint.isEmpty();
        }

        public boolean hasEndpoint() { 
          return this.endpoint != null && !this.endpoint.isEmpty();
        }

        /**
         * @param value {@link #endpoint} (The uri that describes the actual end-point to send messages to.). This is the underlying object with id, value and extensions. The accessor "getEndpoint" gives direct access to the value
         */
        public SubscriptionChannelComponent setEndpointElement(UriType value) { 
          this.endpoint = value;
          return this;
        }

        /**
         * @return The uri that describes the actual end-point to send messages to.
         */
        public String getEndpoint() { 
          return this.endpoint == null ? null : this.endpoint.getValue();
        }

        /**
         * @param value The uri that describes the actual end-point to send messages to.
         */
        public SubscriptionChannelComponent setEndpoint(String value) { 
          if (Utilities.noString(value))
            this.endpoint = null;
          else {
            if (this.endpoint == null)
              this.endpoint = new UriType();
            this.endpoint.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #payload} (The mime type to send the payload in - either application/xml+fhir, or application/json+fhir. If the mime type is blank, then there is no payload in the notification, just a notification.). This is the underlying object with id, value and extensions. The accessor "getPayload" gives direct access to the value
         */
        public StringType getPayloadElement() { 
          if (this.payload == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubscriptionChannelComponent.payload");
            else if (Configuration.doAutoCreate())
              this.payload = new StringType(); // bb
          return this.payload;
        }

        public boolean hasPayloadElement() { 
          return this.payload != null && !this.payload.isEmpty();
        }

        public boolean hasPayload() { 
          return this.payload != null && !this.payload.isEmpty();
        }

        /**
         * @param value {@link #payload} (The mime type to send the payload in - either application/xml+fhir, or application/json+fhir. If the mime type is blank, then there is no payload in the notification, just a notification.). This is the underlying object with id, value and extensions. The accessor "getPayload" gives direct access to the value
         */
        public SubscriptionChannelComponent setPayloadElement(StringType value) { 
          this.payload = value;
          return this;
        }

        /**
         * @return The mime type to send the payload in - either application/xml+fhir, or application/json+fhir. If the mime type is blank, then there is no payload in the notification, just a notification.
         */
        public String getPayload() { 
          return this.payload == null ? null : this.payload.getValue();
        }

        /**
         * @param value The mime type to send the payload in - either application/xml+fhir, or application/json+fhir. If the mime type is blank, then there is no payload in the notification, just a notification.
         */
        public SubscriptionChannelComponent setPayload(String value) { 
            if (this.payload == null)
              this.payload = new StringType();
            this.payload.setValue(value);
          return this;
        }

        /**
         * @return {@link #header} (Additional headers / information to send as part of the notification.). This is the underlying object with id, value and extensions. The accessor "getHeader" gives direct access to the value
         */
        public StringType getHeaderElement() { 
          if (this.header == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubscriptionChannelComponent.header");
            else if (Configuration.doAutoCreate())
              this.header = new StringType(); // bb
          return this.header;
        }

        public boolean hasHeaderElement() { 
          return this.header != null && !this.header.isEmpty();
        }

        public boolean hasHeader() { 
          return this.header != null && !this.header.isEmpty();
        }

        /**
         * @param value {@link #header} (Additional headers / information to send as part of the notification.). This is the underlying object with id, value and extensions. The accessor "getHeader" gives direct access to the value
         */
        public SubscriptionChannelComponent setHeaderElement(StringType value) { 
          this.header = value;
          return this;
        }

        /**
         * @return Additional headers / information to send as part of the notification.
         */
        public String getHeader() { 
          return this.header == null ? null : this.header.getValue();
        }

        /**
         * @param value Additional headers / information to send as part of the notification.
         */
        public SubscriptionChannelComponent setHeader(String value) { 
          if (Utilities.noString(value))
            this.header = null;
          else {
            if (this.header == null)
              this.header = new StringType();
            this.header.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "code", "The type of channel to send notifications on.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("endpoint", "uri", "The uri that describes the actual end-point to send messages to.", 0, java.lang.Integer.MAX_VALUE, endpoint));
          childrenList.add(new Property("payload", "string", "The mime type to send the payload in - either application/xml+fhir, or application/json+fhir. If the mime type is blank, then there is no payload in the notification, just a notification.", 0, java.lang.Integer.MAX_VALUE, payload));
          childrenList.add(new Property("header", "string", "Additional headers / information to send as part of the notification.", 0, java.lang.Integer.MAX_VALUE, header));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<SubscriptionChannelType>
        case 1741102485: /*endpoint*/ return this.endpoint == null ? new Base[0] : new Base[] {this.endpoint}; // UriType
        case -786701938: /*payload*/ return this.payload == null ? new Base[0] : new Base[] {this.payload}; // StringType
        case -1221270899: /*header*/ return this.header == null ? new Base[0] : new Base[] {this.header}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = new SubscriptionChannelTypeEnumFactory().fromType(value); // Enumeration<SubscriptionChannelType>
          break;
        case 1741102485: // endpoint
          this.endpoint = castToUri(value); // UriType
          break;
        case -786701938: // payload
          this.payload = castToString(value); // StringType
          break;
        case -1221270899: // header
          this.header = castToString(value); // StringType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type"))
          this.type = new SubscriptionChannelTypeEnumFactory().fromType(value); // Enumeration<SubscriptionChannelType>
        else if (name.equals("endpoint"))
          this.endpoint = castToUri(value); // UriType
        else if (name.equals("payload"))
          this.payload = castToString(value); // StringType
        else if (name.equals("header"))
          this.header = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: throw new FHIRException("Cannot make property type as it is not a complex type"); // Enumeration<SubscriptionChannelType>
        case 1741102485: throw new FHIRException("Cannot make property endpoint as it is not a complex type"); // UriType
        case -786701938: throw new FHIRException("Cannot make property payload as it is not a complex type"); // StringType
        case -1221270899: throw new FHIRException("Cannot make property header as it is not a complex type"); // StringType
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type Subscription.type");
        }
        else if (name.equals("endpoint")) {
          throw new FHIRException("Cannot call addChild on a primitive type Subscription.endpoint");
        }
        else if (name.equals("payload")) {
          throw new FHIRException("Cannot call addChild on a primitive type Subscription.payload");
        }
        else if (name.equals("header")) {
          throw new FHIRException("Cannot call addChild on a primitive type Subscription.header");
        }
        else
          return super.addChild(name);
      }

      public SubscriptionChannelComponent copy() {
        SubscriptionChannelComponent dst = new SubscriptionChannelComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.endpoint = endpoint == null ? null : endpoint.copy();
        dst.payload = payload == null ? null : payload.copy();
        dst.header = header == null ? null : header.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SubscriptionChannelComponent))
          return false;
        SubscriptionChannelComponent o = (SubscriptionChannelComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(endpoint, o.endpoint, true) && compareDeep(payload, o.payload, true)
           && compareDeep(header, o.header, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SubscriptionChannelComponent))
          return false;
        SubscriptionChannelComponent o = (SubscriptionChannelComponent) other;
        return compareValues(type, o.type, true) && compareValues(endpoint, o.endpoint, true) && compareValues(payload, o.payload, true)
           && compareValues(header, o.header, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (endpoint == null || endpoint.isEmpty())
           && (payload == null || payload.isEmpty()) && (header == null || header.isEmpty());
      }

  public String fhirType() {
    return "Subscription.channel";

  }

  }

    /**
     * The rules that the server should use to determine when to generate notifications for this subscription.
     */
    @Child(name = "criteria", type = {StringType.class}, order=0, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Rule for server push criteria", formalDefinition="The rules that the server should use to determine when to generate notifications for this subscription." )
    protected StringType criteria;

    /**
     * Contact details for a human to contact about the subscription. The primary use of this for system administrator troubleshooting.
     */
    @Child(name = "contact", type = {ContactPoint.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Contact details for source (e.g. troubleshooting)", formalDefinition="Contact details for a human to contact about the subscription. The primary use of this for system administrator troubleshooting." )
    protected List<ContactPoint> contact;

    /**
     * A description of why this subscription is defined.
     */
    @Child(name = "reason", type = {StringType.class}, order=2, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Description of why this subscription was created", formalDefinition="A description of why this subscription is defined." )
    protected StringType reason;

    /**
     * The status of the subscription, which marks the server state for managing the subscription.
     */
    @Child(name = "status", type = {CodeType.class}, order=3, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="requested | active | error | off", formalDefinition="The status of the subscription, which marks the server state for managing the subscription." )
    protected Enumeration<SubscriptionStatus> status;

    /**
     * A record of the last error that occurred when the server processed a notification.
     */
    @Child(name = "error", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Latest error note", formalDefinition="A record of the last error that occurred when the server processed a notification." )
    protected StringType error;

    /**
     * Details where to send notifications when resources are received that meet the criteria.
     */
    @Child(name = "channel", type = {}, order=5, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The channel on which to report matches to the criteria", formalDefinition="Details where to send notifications when resources are received that meet the criteria." )
    protected SubscriptionChannelComponent channel;

    /**
     * The time for the server to turn the subscription off.
     */
    @Child(name = "end", type = {InstantType.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When to automatically delete the subscription", formalDefinition="The time for the server to turn the subscription off." )
    protected InstantType end;

    /**
     * A tag to add to any resource that matches the criteria, after the subscription is processed.
     */
    @Child(name = "tag", type = {Coding.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="A tag to add to matching resources", formalDefinition="A tag to add to any resource that matches the criteria, after the subscription is processed." )
    protected List<Coding> tag;

    private static final long serialVersionUID = -1390870804L;

  /**
   * Constructor
   */
    public Subscription() {
      super();
    }

  /**
   * Constructor
   */
    public Subscription(StringType criteria, StringType reason, Enumeration<SubscriptionStatus> status, SubscriptionChannelComponent channel) {
      super();
      this.criteria = criteria;
      this.reason = reason;
      this.status = status;
      this.channel = channel;
    }

    /**
     * @return {@link #criteria} (The rules that the server should use to determine when to generate notifications for this subscription.). This is the underlying object with id, value and extensions. The accessor "getCriteria" gives direct access to the value
     */
    public StringType getCriteriaElement() { 
      if (this.criteria == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Subscription.criteria");
        else if (Configuration.doAutoCreate())
          this.criteria = new StringType(); // bb
      return this.criteria;
    }

    public boolean hasCriteriaElement() { 
      return this.criteria != null && !this.criteria.isEmpty();
    }

    public boolean hasCriteria() { 
      return this.criteria != null && !this.criteria.isEmpty();
    }

    /**
     * @param value {@link #criteria} (The rules that the server should use to determine when to generate notifications for this subscription.). This is the underlying object with id, value and extensions. The accessor "getCriteria" gives direct access to the value
     */
    public Subscription setCriteriaElement(StringType value) { 
      this.criteria = value;
      return this;
    }

    /**
     * @return The rules that the server should use to determine when to generate notifications for this subscription.
     */
    public String getCriteria() { 
      return this.criteria == null ? null : this.criteria.getValue();
    }

    /**
     * @param value The rules that the server should use to determine when to generate notifications for this subscription.
     */
    public Subscription setCriteria(String value) { 
        if (this.criteria == null)
          this.criteria = new StringType();
        this.criteria.setValue(value);
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

    public boolean hasContact() { 
      if (this.contact == null)
        return false;
      for (ContactPoint item : this.contact)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #contact} (Contact details for a human to contact about the subscription. The primary use of this for system administrator troubleshooting.)
     */
    // syntactic sugar
    public ContactPoint addContact() { //3
      ContactPoint t = new ContactPoint();
      if (this.contact == null)
        this.contact = new ArrayList<ContactPoint>();
      this.contact.add(t);
      return t;
    }

    // syntactic sugar
    public Subscription addContact(ContactPoint t) { //3
      if (t == null)
        return this;
      if (this.contact == null)
        this.contact = new ArrayList<ContactPoint>();
      this.contact.add(t);
      return this;
    }

    /**
     * @return {@link #reason} (A description of why this subscription is defined.). This is the underlying object with id, value and extensions. The accessor "getReason" gives direct access to the value
     */
    public StringType getReasonElement() { 
      if (this.reason == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Subscription.reason");
        else if (Configuration.doAutoCreate())
          this.reason = new StringType(); // bb
      return this.reason;
    }

    public boolean hasReasonElement() { 
      return this.reason != null && !this.reason.isEmpty();
    }

    public boolean hasReason() { 
      return this.reason != null && !this.reason.isEmpty();
    }

    /**
     * @param value {@link #reason} (A description of why this subscription is defined.). This is the underlying object with id, value and extensions. The accessor "getReason" gives direct access to the value
     */
    public Subscription setReasonElement(StringType value) { 
      this.reason = value;
      return this;
    }

    /**
     * @return A description of why this subscription is defined.
     */
    public String getReason() { 
      return this.reason == null ? null : this.reason.getValue();
    }

    /**
     * @param value A description of why this subscription is defined.
     */
    public Subscription setReason(String value) { 
        if (this.reason == null)
          this.reason = new StringType();
        this.reason.setValue(value);
      return this;
    }

    /**
     * @return {@link #status} (The status of the subscription, which marks the server state for managing the subscription.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<SubscriptionStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Subscription.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<SubscriptionStatus>(new SubscriptionStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of the subscription, which marks the server state for managing the subscription.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Subscription setStatusElement(Enumeration<SubscriptionStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the subscription, which marks the server state for managing the subscription.
     */
    public SubscriptionStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the subscription, which marks the server state for managing the subscription.
     */
    public Subscription setStatus(SubscriptionStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<SubscriptionStatus>(new SubscriptionStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #error} (A record of the last error that occurred when the server processed a notification.). This is the underlying object with id, value and extensions. The accessor "getError" gives direct access to the value
     */
    public StringType getErrorElement() { 
      if (this.error == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Subscription.error");
        else if (Configuration.doAutoCreate())
          this.error = new StringType(); // bb
      return this.error;
    }

    public boolean hasErrorElement() { 
      return this.error != null && !this.error.isEmpty();
    }

    public boolean hasError() { 
      return this.error != null && !this.error.isEmpty();
    }

    /**
     * @param value {@link #error} (A record of the last error that occurred when the server processed a notification.). This is the underlying object with id, value and extensions. The accessor "getError" gives direct access to the value
     */
    public Subscription setErrorElement(StringType value) { 
      this.error = value;
      return this;
    }

    /**
     * @return A record of the last error that occurred when the server processed a notification.
     */
    public String getError() { 
      return this.error == null ? null : this.error.getValue();
    }

    /**
     * @param value A record of the last error that occurred when the server processed a notification.
     */
    public Subscription setError(String value) { 
      if (Utilities.noString(value))
        this.error = null;
      else {
        if (this.error == null)
          this.error = new StringType();
        this.error.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #channel} (Details where to send notifications when resources are received that meet the criteria.)
     */
    public SubscriptionChannelComponent getChannel() { 
      if (this.channel == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Subscription.channel");
        else if (Configuration.doAutoCreate())
          this.channel = new SubscriptionChannelComponent(); // cc
      return this.channel;
    }

    public boolean hasChannel() { 
      return this.channel != null && !this.channel.isEmpty();
    }

    /**
     * @param value {@link #channel} (Details where to send notifications when resources are received that meet the criteria.)
     */
    public Subscription setChannel(SubscriptionChannelComponent value) { 
      this.channel = value;
      return this;
    }

    /**
     * @return {@link #end} (The time for the server to turn the subscription off.). This is the underlying object with id, value and extensions. The accessor "getEnd" gives direct access to the value
     */
    public InstantType getEndElement() { 
      if (this.end == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Subscription.end");
        else if (Configuration.doAutoCreate())
          this.end = new InstantType(); // bb
      return this.end;
    }

    public boolean hasEndElement() { 
      return this.end != null && !this.end.isEmpty();
    }

    public boolean hasEnd() { 
      return this.end != null && !this.end.isEmpty();
    }

    /**
     * @param value {@link #end} (The time for the server to turn the subscription off.). This is the underlying object with id, value and extensions. The accessor "getEnd" gives direct access to the value
     */
    public Subscription setEndElement(InstantType value) { 
      this.end = value;
      return this;
    }

    /**
     * @return The time for the server to turn the subscription off.
     */
    public Date getEnd() { 
      return this.end == null ? null : this.end.getValue();
    }

    /**
     * @param value The time for the server to turn the subscription off.
     */
    public Subscription setEnd(Date value) { 
      if (value == null)
        this.end = null;
      else {
        if (this.end == null)
          this.end = new InstantType();
        this.end.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #tag} (A tag to add to any resource that matches the criteria, after the subscription is processed.)
     */
    public List<Coding> getTag() { 
      if (this.tag == null)
        this.tag = new ArrayList<Coding>();
      return this.tag;
    }

    public boolean hasTag() { 
      if (this.tag == null)
        return false;
      for (Coding item : this.tag)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #tag} (A tag to add to any resource that matches the criteria, after the subscription is processed.)
     */
    // syntactic sugar
    public Coding addTag() { //3
      Coding t = new Coding();
      if (this.tag == null)
        this.tag = new ArrayList<Coding>();
      this.tag.add(t);
      return t;
    }

    // syntactic sugar
    public Subscription addTag(Coding t) { //3
      if (t == null)
        return this;
      if (this.tag == null)
        this.tag = new ArrayList<Coding>();
      this.tag.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("criteria", "string", "The rules that the server should use to determine when to generate notifications for this subscription.", 0, java.lang.Integer.MAX_VALUE, criteria));
        childrenList.add(new Property("contact", "ContactPoint", "Contact details for a human to contact about the subscription. The primary use of this for system administrator troubleshooting.", 0, java.lang.Integer.MAX_VALUE, contact));
        childrenList.add(new Property("reason", "string", "A description of why this subscription is defined.", 0, java.lang.Integer.MAX_VALUE, reason));
        childrenList.add(new Property("status", "code", "The status of the subscription, which marks the server state for managing the subscription.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("error", "string", "A record of the last error that occurred when the server processed a notification.", 0, java.lang.Integer.MAX_VALUE, error));
        childrenList.add(new Property("channel", "", "Details where to send notifications when resources are received that meet the criteria.", 0, java.lang.Integer.MAX_VALUE, channel));
        childrenList.add(new Property("end", "instant", "The time for the server to turn the subscription off.", 0, java.lang.Integer.MAX_VALUE, end));
        childrenList.add(new Property("tag", "Coding", "A tag to add to any resource that matches the criteria, after the subscription is processed.", 0, java.lang.Integer.MAX_VALUE, tag));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1952046943: /*criteria*/ return this.criteria == null ? new Base[0] : new Base[] {this.criteria}; // StringType
        case 951526432: /*contact*/ return this.contact == null ? new Base[0] : this.contact.toArray(new Base[this.contact.size()]); // ContactPoint
        case -934964668: /*reason*/ return this.reason == null ? new Base[0] : new Base[] {this.reason}; // StringType
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<SubscriptionStatus>
        case 96784904: /*error*/ return this.error == null ? new Base[0] : new Base[] {this.error}; // StringType
        case 738950403: /*channel*/ return this.channel == null ? new Base[0] : new Base[] {this.channel}; // SubscriptionChannelComponent
        case 100571: /*end*/ return this.end == null ? new Base[0] : new Base[] {this.end}; // InstantType
        case 114586: /*tag*/ return this.tag == null ? new Base[0] : this.tag.toArray(new Base[this.tag.size()]); // Coding
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1952046943: // criteria
          this.criteria = castToString(value); // StringType
          break;
        case 951526432: // contact
          this.getContact().add(castToContactPoint(value)); // ContactPoint
          break;
        case -934964668: // reason
          this.reason = castToString(value); // StringType
          break;
        case -892481550: // status
          this.status = new SubscriptionStatusEnumFactory().fromType(value); // Enumeration<SubscriptionStatus>
          break;
        case 96784904: // error
          this.error = castToString(value); // StringType
          break;
        case 738950403: // channel
          this.channel = (SubscriptionChannelComponent) value; // SubscriptionChannelComponent
          break;
        case 100571: // end
          this.end = castToInstant(value); // InstantType
          break;
        case 114586: // tag
          this.getTag().add(castToCoding(value)); // Coding
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("criteria"))
          this.criteria = castToString(value); // StringType
        else if (name.equals("contact"))
          this.getContact().add(castToContactPoint(value));
        else if (name.equals("reason"))
          this.reason = castToString(value); // StringType
        else if (name.equals("status"))
          this.status = new SubscriptionStatusEnumFactory().fromType(value); // Enumeration<SubscriptionStatus>
        else if (name.equals("error"))
          this.error = castToString(value); // StringType
        else if (name.equals("channel"))
          this.channel = (SubscriptionChannelComponent) value; // SubscriptionChannelComponent
        else if (name.equals("end"))
          this.end = castToInstant(value); // InstantType
        else if (name.equals("tag"))
          this.getTag().add(castToCoding(value));
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1952046943: throw new FHIRException("Cannot make property criteria as it is not a complex type"); // StringType
        case 951526432:  return addContact(); // ContactPoint
        case -934964668: throw new FHIRException("Cannot make property reason as it is not a complex type"); // StringType
        case -892481550: throw new FHIRException("Cannot make property status as it is not a complex type"); // Enumeration<SubscriptionStatus>
        case 96784904: throw new FHIRException("Cannot make property error as it is not a complex type"); // StringType
        case 738950403:  return getChannel(); // SubscriptionChannelComponent
        case 100571: throw new FHIRException("Cannot make property end as it is not a complex type"); // InstantType
        case 114586:  return addTag(); // Coding
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("criteria")) {
          throw new FHIRException("Cannot call addChild on a primitive type Subscription.criteria");
        }
        else if (name.equals("contact")) {
          return addContact();
        }
        else if (name.equals("reason")) {
          throw new FHIRException("Cannot call addChild on a primitive type Subscription.reason");
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type Subscription.status");
        }
        else if (name.equals("error")) {
          throw new FHIRException("Cannot call addChild on a primitive type Subscription.error");
        }
        else if (name.equals("channel")) {
          this.channel = new SubscriptionChannelComponent();
          return this.channel;
        }
        else if (name.equals("end")) {
          throw new FHIRException("Cannot call addChild on a primitive type Subscription.end");
        }
        else if (name.equals("tag")) {
          return addTag();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Subscription";

  }

      public Subscription copy() {
        Subscription dst = new Subscription();
        copyValues(dst);
        dst.criteria = criteria == null ? null : criteria.copy();
        if (contact != null) {
          dst.contact = new ArrayList<ContactPoint>();
          for (ContactPoint i : contact)
            dst.contact.add(i.copy());
        };
        dst.reason = reason == null ? null : reason.copy();
        dst.status = status == null ? null : status.copy();
        dst.error = error == null ? null : error.copy();
        dst.channel = channel == null ? null : channel.copy();
        dst.end = end == null ? null : end.copy();
        if (tag != null) {
          dst.tag = new ArrayList<Coding>();
          for (Coding i : tag)
            dst.tag.add(i.copy());
        };
        return dst;
      }

      protected Subscription typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Subscription))
          return false;
        Subscription o = (Subscription) other;
        return compareDeep(criteria, o.criteria, true) && compareDeep(contact, o.contact, true) && compareDeep(reason, o.reason, true)
           && compareDeep(status, o.status, true) && compareDeep(error, o.error, true) && compareDeep(channel, o.channel, true)
           && compareDeep(end, o.end, true) && compareDeep(tag, o.tag, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Subscription))
          return false;
        Subscription o = (Subscription) other;
        return compareValues(criteria, o.criteria, true) && compareValues(reason, o.reason, true) && compareValues(status, o.status, true)
           && compareValues(error, o.error, true) && compareValues(end, o.end, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (criteria == null || criteria.isEmpty()) && (contact == null || contact.isEmpty())
           && (reason == null || reason.isEmpty()) && (status == null || status.isEmpty()) && (error == null || error.isEmpty())
           && (channel == null || channel.isEmpty()) && (end == null || end.isEmpty()) && (tag == null || tag.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Subscription;
   }

 /**
   * Search parameter: <b>criteria</b>
   * <p>
   * Description: <b>Rule for server push criteria</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Subscription.criteria</b><br>
   * </p>
   */
  @SearchParamDefinition(name="criteria", path="Subscription.criteria", description="Rule for server push criteria", type="string" )
  public static final String SP_CRITERIA = "criteria";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>criteria</b>
   * <p>
   * Description: <b>Rule for server push criteria</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Subscription.criteria</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam CRITERIA = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_CRITERIA);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>requested | active | error | off</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Subscription.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="Subscription.status", description="requested | active | error | off", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>requested | active | error | off</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Subscription.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);

 /**
   * Search parameter: <b>tag</b>
   * <p>
   * Description: <b>A tag to add to matching resources</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Subscription.tag</b><br>
   * </p>
   */
  @SearchParamDefinition(name="tag", path="Subscription.tag", description="A tag to add to matching resources", type="token" )
  public static final String SP_TAG = "tag";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>tag</b>
   * <p>
   * Description: <b>A tag to add to matching resources</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Subscription.tag</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TAG = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TAG);

 /**
   * Search parameter: <b>payload</b>
   * <p>
   * Description: <b>Mimetype to send, or blank for no payload</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Subscription.channel.payload</b><br>
   * </p>
   */
  @SearchParamDefinition(name="payload", path="Subscription.channel.payload", description="Mimetype to send, or blank for no payload", type="string" )
  public static final String SP_PAYLOAD = "payload";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>payload</b>
   * <p>
   * Description: <b>Mimetype to send, or blank for no payload</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Subscription.channel.payload</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam PAYLOAD = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_PAYLOAD);

 /**
   * Search parameter: <b>type</b>
   * <p>
   * Description: <b>rest-hook | websocket | email | sms | message</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Subscription.channel.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="type", path="Subscription.channel.type", description="rest-hook | websocket | email | sms | message", type="token" )
  public static final String SP_TYPE = "type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>type</b>
   * <p>
   * Description: <b>rest-hook | websocket | email | sms | message</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Subscription.channel.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TYPE);

 /**
   * Search parameter: <b>contact</b>
   * <p>
   * Description: <b>Contact details for source (e.g. troubleshooting)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Subscription.contact</b><br>
   * </p>
   */
  @SearchParamDefinition(name="contact", path="Subscription.contact", description="Contact details for source (e.g. troubleshooting)", type="token" )
  public static final String SP_CONTACT = "contact";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>contact</b>
   * <p>
   * Description: <b>Contact details for source (e.g. troubleshooting)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Subscription.contact</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CONTACT = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CONTACT);

 /**
   * Search parameter: <b>url</b>
   * <p>
   * Description: <b>Where the channel points to</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>Subscription.channel.endpoint</b><br>
   * </p>
   */
  @SearchParamDefinition(name="url", path="Subscription.channel.endpoint", description="Where the channel points to", type="uri" )
  public static final String SP_URL = "url";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>url</b>
   * <p>
   * Description: <b>Where the channel points to</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>Subscription.channel.endpoint</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam URL = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_URL);


}

