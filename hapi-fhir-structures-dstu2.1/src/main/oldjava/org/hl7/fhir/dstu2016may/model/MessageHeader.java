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
 * The header for a message exchange that is either requesting or responding to an action.  The reference(s) that are the subject of the action as well as other information related to the action are typically transmitted in a bundle in which the MessageHeader resource instance is the first resource in the bundle.
 */
@ResourceDef(name="MessageHeader", profile="http://hl7.org/fhir/Profile/MessageHeader")
public class MessageHeader extends DomainResource {

    public enum ResponseType {
        /**
         * The message was accepted and processed without error.
         */
        OK, 
        /**
         * Some internal unexpected error occurred - wait and try again. Note - this is usually used for things like database unavailable, which may be expected to resolve, though human intervention may be required.
         */
        TRANSIENTERROR, 
        /**
         * The message was rejected because of some content in it. There is no point in re-sending without change. The response narrative SHALL describe the issue.
         */
        FATALERROR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ResponseType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("ok".equals(codeString))
          return OK;
        if ("transient-error".equals(codeString))
          return TRANSIENTERROR;
        if ("fatal-error".equals(codeString))
          return FATALERROR;
        throw new FHIRException("Unknown ResponseType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case OK: return "ok";
            case TRANSIENTERROR: return "transient-error";
            case FATALERROR: return "fatal-error";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case OK: return "http://hl7.org/fhir/response-code";
            case TRANSIENTERROR: return "http://hl7.org/fhir/response-code";
            case FATALERROR: return "http://hl7.org/fhir/response-code";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case OK: return "The message was accepted and processed without error.";
            case TRANSIENTERROR: return "Some internal unexpected error occurred - wait and try again. Note - this is usually used for things like database unavailable, which may be expected to resolve, though human intervention may be required.";
            case FATALERROR: return "The message was rejected because of some content in it. There is no point in re-sending without change. The response narrative SHALL describe the issue.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case OK: return "OK";
            case TRANSIENTERROR: return "Transient Error";
            case FATALERROR: return "Fatal Error";
            default: return "?";
          }
        }
    }

  public static class ResponseTypeEnumFactory implements EnumFactory<ResponseType> {
    public ResponseType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("ok".equals(codeString))
          return ResponseType.OK;
        if ("transient-error".equals(codeString))
          return ResponseType.TRANSIENTERROR;
        if ("fatal-error".equals(codeString))
          return ResponseType.FATALERROR;
        throw new IllegalArgumentException("Unknown ResponseType code '"+codeString+"'");
        }
        public Enumeration<ResponseType> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("ok".equals(codeString))
          return new Enumeration<ResponseType>(this, ResponseType.OK);
        if ("transient-error".equals(codeString))
          return new Enumeration<ResponseType>(this, ResponseType.TRANSIENTERROR);
        if ("fatal-error".equals(codeString))
          return new Enumeration<ResponseType>(this, ResponseType.FATALERROR);
        throw new FHIRException("Unknown ResponseType code '"+codeString+"'");
        }
    public String toCode(ResponseType code) {
      if (code == ResponseType.OK)
        return "ok";
      if (code == ResponseType.TRANSIENTERROR)
        return "transient-error";
      if (code == ResponseType.FATALERROR)
        return "fatal-error";
      return "?";
      }
    public String toSystem(ResponseType code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class MessageHeaderResponseComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The id of the message that this message is a response to.
         */
        @Child(name = "identifier", type = {IdType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Id of original message", formalDefinition="The id of the message that this message is a response to." )
        protected IdType identifier;

        /**
         * Code that identifies the type of response to the message - whether it was successful or not, and whether it should be resent or not.
         */
        @Child(name = "code", type = {CodeType.class}, order=2, min=1, max=1, modifier=true, summary=true)
        @Description(shortDefinition="ok | transient-error | fatal-error", formalDefinition="Code that identifies the type of response to the message - whether it was successful or not, and whether it should be resent or not." )
        protected Enumeration<ResponseType> code;

        /**
         * Full details of any issues found in the message.
         */
        @Child(name = "details", type = {OperationOutcome.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Specific list of hints/warnings/errors", formalDefinition="Full details of any issues found in the message." )
        protected Reference details;

        /**
         * The actual object that is the target of the reference (Full details of any issues found in the message.)
         */
        protected OperationOutcome detailsTarget;

        private static final long serialVersionUID = -1008716838L;

    /**
     * Constructor
     */
      public MessageHeaderResponseComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MessageHeaderResponseComponent(IdType identifier, Enumeration<ResponseType> code) {
        super();
        this.identifier = identifier;
        this.code = code;
      }

        /**
         * @return {@link #identifier} (The id of the message that this message is a response to.). This is the underlying object with id, value and extensions. The accessor "getIdentifier" gives direct access to the value
         */
        public IdType getIdentifierElement() { 
          if (this.identifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MessageHeaderResponseComponent.identifier");
            else if (Configuration.doAutoCreate())
              this.identifier = new IdType(); // bb
          return this.identifier;
        }

        public boolean hasIdentifierElement() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        public boolean hasIdentifier() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        /**
         * @param value {@link #identifier} (The id of the message that this message is a response to.). This is the underlying object with id, value and extensions. The accessor "getIdentifier" gives direct access to the value
         */
        public MessageHeaderResponseComponent setIdentifierElement(IdType value) { 
          this.identifier = value;
          return this;
        }

        /**
         * @return The id of the message that this message is a response to.
         */
        public String getIdentifier() { 
          return this.identifier == null ? null : this.identifier.getValue();
        }

        /**
         * @param value The id of the message that this message is a response to.
         */
        public MessageHeaderResponseComponent setIdentifier(String value) { 
            if (this.identifier == null)
              this.identifier = new IdType();
            this.identifier.setValue(value);
          return this;
        }

        /**
         * @return {@link #code} (Code that identifies the type of response to the message - whether it was successful or not, and whether it should be resent or not.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public Enumeration<ResponseType> getCodeElement() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MessageHeaderResponseComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new Enumeration<ResponseType>(new ResponseTypeEnumFactory()); // bb
          return this.code;
        }

        public boolean hasCodeElement() { 
          return this.code != null && !this.code.isEmpty();
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (Code that identifies the type of response to the message - whether it was successful or not, and whether it should be resent or not.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public MessageHeaderResponseComponent setCodeElement(Enumeration<ResponseType> value) { 
          this.code = value;
          return this;
        }

        /**
         * @return Code that identifies the type of response to the message - whether it was successful or not, and whether it should be resent or not.
         */
        public ResponseType getCode() { 
          return this.code == null ? null : this.code.getValue();
        }

        /**
         * @param value Code that identifies the type of response to the message - whether it was successful or not, and whether it should be resent or not.
         */
        public MessageHeaderResponseComponent setCode(ResponseType value) { 
            if (this.code == null)
              this.code = new Enumeration<ResponseType>(new ResponseTypeEnumFactory());
            this.code.setValue(value);
          return this;
        }

        /**
         * @return {@link #details} (Full details of any issues found in the message.)
         */
        public Reference getDetails() { 
          if (this.details == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MessageHeaderResponseComponent.details");
            else if (Configuration.doAutoCreate())
              this.details = new Reference(); // cc
          return this.details;
        }

        public boolean hasDetails() { 
          return this.details != null && !this.details.isEmpty();
        }

        /**
         * @param value {@link #details} (Full details of any issues found in the message.)
         */
        public MessageHeaderResponseComponent setDetails(Reference value) { 
          this.details = value;
          return this;
        }

        /**
         * @return {@link #details} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Full details of any issues found in the message.)
         */
        public OperationOutcome getDetailsTarget() { 
          if (this.detailsTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MessageHeaderResponseComponent.details");
            else if (Configuration.doAutoCreate())
              this.detailsTarget = new OperationOutcome(); // aa
          return this.detailsTarget;
        }

        /**
         * @param value {@link #details} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Full details of any issues found in the message.)
         */
        public MessageHeaderResponseComponent setDetailsTarget(OperationOutcome value) { 
          this.detailsTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("identifier", "id", "The id of the message that this message is a response to.", 0, java.lang.Integer.MAX_VALUE, identifier));
          childrenList.add(new Property("code", "code", "Code that identifies the type of response to the message - whether it was successful or not, and whether it should be resent or not.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("details", "Reference(OperationOutcome)", "Full details of any issues found in the message.", 0, java.lang.Integer.MAX_VALUE, details));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // IdType
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // Enumeration<ResponseType>
        case 1557721666: /*details*/ return this.details == null ? new Base[0] : new Base[] {this.details}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.identifier = castToId(value); // IdType
          break;
        case 3059181: // code
          this.code = new ResponseTypeEnumFactory().fromType(value); // Enumeration<ResponseType>
          break;
        case 1557721666: // details
          this.details = castToReference(value); // Reference
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.identifier = castToId(value); // IdType
        else if (name.equals("code"))
          this.code = new ResponseTypeEnumFactory().fromType(value); // Enumeration<ResponseType>
        else if (name.equals("details"))
          this.details = castToReference(value); // Reference
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: throw new FHIRException("Cannot make property identifier as it is not a complex type"); // IdType
        case 3059181: throw new FHIRException("Cannot make property code as it is not a complex type"); // Enumeration<ResponseType>
        case 1557721666:  return getDetails(); // Reference
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          throw new FHIRException("Cannot call addChild on a primitive type MessageHeader.identifier");
        }
        else if (name.equals("code")) {
          throw new FHIRException("Cannot call addChild on a primitive type MessageHeader.code");
        }
        else if (name.equals("details")) {
          this.details = new Reference();
          return this.details;
        }
        else
          return super.addChild(name);
      }

      public MessageHeaderResponseComponent copy() {
        MessageHeaderResponseComponent dst = new MessageHeaderResponseComponent();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.code = code == null ? null : code.copy();
        dst.details = details == null ? null : details.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof MessageHeaderResponseComponent))
          return false;
        MessageHeaderResponseComponent o = (MessageHeaderResponseComponent) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(code, o.code, true) && compareDeep(details, o.details, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MessageHeaderResponseComponent))
          return false;
        MessageHeaderResponseComponent o = (MessageHeaderResponseComponent) other;
        return compareValues(identifier, o.identifier, true) && compareValues(code, o.code, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (code == null || code.isEmpty())
           && (details == null || details.isEmpty());
      }

  public String fhirType() {
    return "MessageHeader.response";

  }

  }

    @Block()
    public static class MessageSourceComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Human-readable name for the source system.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Name of system", formalDefinition="Human-readable name for the source system." )
        protected StringType name;

        /**
         * May include configuration or other information useful in debugging.
         */
        @Child(name = "software", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Name of software running the system", formalDefinition="May include configuration or other information useful in debugging." )
        protected StringType software;

        /**
         * Can convey versions of multiple systems in situations where a message passes through multiple hands.
         */
        @Child(name = "version", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Version of software running", formalDefinition="Can convey versions of multiple systems in situations where a message passes through multiple hands." )
        protected StringType version;

        /**
         * An e-mail, phone, website or other contact point to use to resolve issues with message communications.
         */
        @Child(name = "contact", type = {ContactPoint.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Human contact for problems", formalDefinition="An e-mail, phone, website or other contact point to use to resolve issues with message communications." )
        protected ContactPoint contact;

        /**
         * Identifies the routing target to send acknowledgements to.
         */
        @Child(name = "endpoint", type = {UriType.class}, order=5, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Actual message source address or id", formalDefinition="Identifies the routing target to send acknowledgements to." )
        protected UriType endpoint;

        private static final long serialVersionUID = -115878196L;

    /**
     * Constructor
     */
      public MessageSourceComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MessageSourceComponent(UriType endpoint) {
        super();
        this.endpoint = endpoint;
      }

        /**
         * @return {@link #name} (Human-readable name for the source system.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MessageSourceComponent.name");
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
         * @param value {@link #name} (Human-readable name for the source system.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public MessageSourceComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return Human-readable name for the source system.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value Human-readable name for the source system.
         */
        public MessageSourceComponent setName(String value) { 
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
         * @return {@link #software} (May include configuration or other information useful in debugging.). This is the underlying object with id, value and extensions. The accessor "getSoftware" gives direct access to the value
         */
        public StringType getSoftwareElement() { 
          if (this.software == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MessageSourceComponent.software");
            else if (Configuration.doAutoCreate())
              this.software = new StringType(); // bb
          return this.software;
        }

        public boolean hasSoftwareElement() { 
          return this.software != null && !this.software.isEmpty();
        }

        public boolean hasSoftware() { 
          return this.software != null && !this.software.isEmpty();
        }

        /**
         * @param value {@link #software} (May include configuration or other information useful in debugging.). This is the underlying object with id, value and extensions. The accessor "getSoftware" gives direct access to the value
         */
        public MessageSourceComponent setSoftwareElement(StringType value) { 
          this.software = value;
          return this;
        }

        /**
         * @return May include configuration or other information useful in debugging.
         */
        public String getSoftware() { 
          return this.software == null ? null : this.software.getValue();
        }

        /**
         * @param value May include configuration or other information useful in debugging.
         */
        public MessageSourceComponent setSoftware(String value) { 
          if (Utilities.noString(value))
            this.software = null;
          else {
            if (this.software == null)
              this.software = new StringType();
            this.software.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #version} (Can convey versions of multiple systems in situations where a message passes through multiple hands.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
         */
        public StringType getVersionElement() { 
          if (this.version == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MessageSourceComponent.version");
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
         * @param value {@link #version} (Can convey versions of multiple systems in situations where a message passes through multiple hands.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
         */
        public MessageSourceComponent setVersionElement(StringType value) { 
          this.version = value;
          return this;
        }

        /**
         * @return Can convey versions of multiple systems in situations where a message passes through multiple hands.
         */
        public String getVersion() { 
          return this.version == null ? null : this.version.getValue();
        }

        /**
         * @param value Can convey versions of multiple systems in situations where a message passes through multiple hands.
         */
        public MessageSourceComponent setVersion(String value) { 
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
         * @return {@link #contact} (An e-mail, phone, website or other contact point to use to resolve issues with message communications.)
         */
        public ContactPoint getContact() { 
          if (this.contact == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MessageSourceComponent.contact");
            else if (Configuration.doAutoCreate())
              this.contact = new ContactPoint(); // cc
          return this.contact;
        }

        public boolean hasContact() { 
          return this.contact != null && !this.contact.isEmpty();
        }

        /**
         * @param value {@link #contact} (An e-mail, phone, website or other contact point to use to resolve issues with message communications.)
         */
        public MessageSourceComponent setContact(ContactPoint value) { 
          this.contact = value;
          return this;
        }

        /**
         * @return {@link #endpoint} (Identifies the routing target to send acknowledgements to.). This is the underlying object with id, value and extensions. The accessor "getEndpoint" gives direct access to the value
         */
        public UriType getEndpointElement() { 
          if (this.endpoint == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MessageSourceComponent.endpoint");
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
         * @param value {@link #endpoint} (Identifies the routing target to send acknowledgements to.). This is the underlying object with id, value and extensions. The accessor "getEndpoint" gives direct access to the value
         */
        public MessageSourceComponent setEndpointElement(UriType value) { 
          this.endpoint = value;
          return this;
        }

        /**
         * @return Identifies the routing target to send acknowledgements to.
         */
        public String getEndpoint() { 
          return this.endpoint == null ? null : this.endpoint.getValue();
        }

        /**
         * @param value Identifies the routing target to send acknowledgements to.
         */
        public MessageSourceComponent setEndpoint(String value) { 
            if (this.endpoint == null)
              this.endpoint = new UriType();
            this.endpoint.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "string", "Human-readable name for the source system.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("software", "string", "May include configuration or other information useful in debugging.", 0, java.lang.Integer.MAX_VALUE, software));
          childrenList.add(new Property("version", "string", "Can convey versions of multiple systems in situations where a message passes through multiple hands.", 0, java.lang.Integer.MAX_VALUE, version));
          childrenList.add(new Property("contact", "ContactPoint", "An e-mail, phone, website or other contact point to use to resolve issues with message communications.", 0, java.lang.Integer.MAX_VALUE, contact));
          childrenList.add(new Property("endpoint", "uri", "Identifies the routing target to send acknowledgements to.", 0, java.lang.Integer.MAX_VALUE, endpoint));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case 1319330215: /*software*/ return this.software == null ? new Base[0] : new Base[] {this.software}; // StringType
        case 351608024: /*version*/ return this.version == null ? new Base[0] : new Base[] {this.version}; // StringType
        case 951526432: /*contact*/ return this.contact == null ? new Base[0] : new Base[] {this.contact}; // ContactPoint
        case 1741102485: /*endpoint*/ return this.endpoint == null ? new Base[0] : new Base[] {this.endpoint}; // UriType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToString(value); // StringType
          break;
        case 1319330215: // software
          this.software = castToString(value); // StringType
          break;
        case 351608024: // version
          this.version = castToString(value); // StringType
          break;
        case 951526432: // contact
          this.contact = castToContactPoint(value); // ContactPoint
          break;
        case 1741102485: // endpoint
          this.endpoint = castToUri(value); // UriType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name"))
          this.name = castToString(value); // StringType
        else if (name.equals("software"))
          this.software = castToString(value); // StringType
        else if (name.equals("version"))
          this.version = castToString(value); // StringType
        else if (name.equals("contact"))
          this.contact = castToContactPoint(value); // ContactPoint
        else if (name.equals("endpoint"))
          this.endpoint = castToUri(value); // UriType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: throw new FHIRException("Cannot make property name as it is not a complex type"); // StringType
        case 1319330215: throw new FHIRException("Cannot make property software as it is not a complex type"); // StringType
        case 351608024: throw new FHIRException("Cannot make property version as it is not a complex type"); // StringType
        case 951526432:  return getContact(); // ContactPoint
        case 1741102485: throw new FHIRException("Cannot make property endpoint as it is not a complex type"); // UriType
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type MessageHeader.name");
        }
        else if (name.equals("software")) {
          throw new FHIRException("Cannot call addChild on a primitive type MessageHeader.software");
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type MessageHeader.version");
        }
        else if (name.equals("contact")) {
          this.contact = new ContactPoint();
          return this.contact;
        }
        else if (name.equals("endpoint")) {
          throw new FHIRException("Cannot call addChild on a primitive type MessageHeader.endpoint");
        }
        else
          return super.addChild(name);
      }

      public MessageSourceComponent copy() {
        MessageSourceComponent dst = new MessageSourceComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.software = software == null ? null : software.copy();
        dst.version = version == null ? null : version.copy();
        dst.contact = contact == null ? null : contact.copy();
        dst.endpoint = endpoint == null ? null : endpoint.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof MessageSourceComponent))
          return false;
        MessageSourceComponent o = (MessageSourceComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(software, o.software, true) && compareDeep(version, o.version, true)
           && compareDeep(contact, o.contact, true) && compareDeep(endpoint, o.endpoint, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MessageSourceComponent))
          return false;
        MessageSourceComponent o = (MessageSourceComponent) other;
        return compareValues(name, o.name, true) && compareValues(software, o.software, true) && compareValues(version, o.version, true)
           && compareValues(endpoint, o.endpoint, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (software == null || software.isEmpty())
           && (version == null || version.isEmpty()) && (contact == null || contact.isEmpty()) && (endpoint == null || endpoint.isEmpty())
          ;
      }

  public String fhirType() {
    return "MessageHeader.source";

  }

  }

    @Block()
    public static class MessageDestinationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Human-readable name for the target system.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Name of system", formalDefinition="Human-readable name for the target system." )
        protected StringType name;

        /**
         * Identifies the target end system in situations where the initial message transmission is to an intermediary system.
         */
        @Child(name = "target", type = {Device.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Particular delivery destination within the destination", formalDefinition="Identifies the target end system in situations where the initial message transmission is to an intermediary system." )
        protected Reference target;

        /**
         * The actual object that is the target of the reference (Identifies the target end system in situations where the initial message transmission is to an intermediary system.)
         */
        protected Device targetTarget;

        /**
         * Indicates where the message should be routed to.
         */
        @Child(name = "endpoint", type = {UriType.class}, order=3, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Actual destination address or id", formalDefinition="Indicates where the message should be routed to." )
        protected UriType endpoint;

        private static final long serialVersionUID = -2097633309L;

    /**
     * Constructor
     */
      public MessageDestinationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MessageDestinationComponent(UriType endpoint) {
        super();
        this.endpoint = endpoint;
      }

        /**
         * @return {@link #name} (Human-readable name for the target system.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MessageDestinationComponent.name");
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
         * @param value {@link #name} (Human-readable name for the target system.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public MessageDestinationComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return Human-readable name for the target system.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value Human-readable name for the target system.
         */
        public MessageDestinationComponent setName(String value) { 
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
         * @return {@link #target} (Identifies the target end system in situations where the initial message transmission is to an intermediary system.)
         */
        public Reference getTarget() { 
          if (this.target == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MessageDestinationComponent.target");
            else if (Configuration.doAutoCreate())
              this.target = new Reference(); // cc
          return this.target;
        }

        public boolean hasTarget() { 
          return this.target != null && !this.target.isEmpty();
        }

        /**
         * @param value {@link #target} (Identifies the target end system in situations where the initial message transmission is to an intermediary system.)
         */
        public MessageDestinationComponent setTarget(Reference value) { 
          this.target = value;
          return this;
        }

        /**
         * @return {@link #target} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identifies the target end system in situations where the initial message transmission is to an intermediary system.)
         */
        public Device getTargetTarget() { 
          if (this.targetTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MessageDestinationComponent.target");
            else if (Configuration.doAutoCreate())
              this.targetTarget = new Device(); // aa
          return this.targetTarget;
        }

        /**
         * @param value {@link #target} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identifies the target end system in situations where the initial message transmission is to an intermediary system.)
         */
        public MessageDestinationComponent setTargetTarget(Device value) { 
          this.targetTarget = value;
          return this;
        }

        /**
         * @return {@link #endpoint} (Indicates where the message should be routed to.). This is the underlying object with id, value and extensions. The accessor "getEndpoint" gives direct access to the value
         */
        public UriType getEndpointElement() { 
          if (this.endpoint == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MessageDestinationComponent.endpoint");
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
         * @param value {@link #endpoint} (Indicates where the message should be routed to.). This is the underlying object with id, value and extensions. The accessor "getEndpoint" gives direct access to the value
         */
        public MessageDestinationComponent setEndpointElement(UriType value) { 
          this.endpoint = value;
          return this;
        }

        /**
         * @return Indicates where the message should be routed to.
         */
        public String getEndpoint() { 
          return this.endpoint == null ? null : this.endpoint.getValue();
        }

        /**
         * @param value Indicates where the message should be routed to.
         */
        public MessageDestinationComponent setEndpoint(String value) { 
            if (this.endpoint == null)
              this.endpoint = new UriType();
            this.endpoint.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "string", "Human-readable name for the target system.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("target", "Reference(Device)", "Identifies the target end system in situations where the initial message transmission is to an intermediary system.", 0, java.lang.Integer.MAX_VALUE, target));
          childrenList.add(new Property("endpoint", "uri", "Indicates where the message should be routed to.", 0, java.lang.Integer.MAX_VALUE, endpoint));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -880905839: /*target*/ return this.target == null ? new Base[0] : new Base[] {this.target}; // Reference
        case 1741102485: /*endpoint*/ return this.endpoint == null ? new Base[0] : new Base[] {this.endpoint}; // UriType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToString(value); // StringType
          break;
        case -880905839: // target
          this.target = castToReference(value); // Reference
          break;
        case 1741102485: // endpoint
          this.endpoint = castToUri(value); // UriType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name"))
          this.name = castToString(value); // StringType
        else if (name.equals("target"))
          this.target = castToReference(value); // Reference
        else if (name.equals("endpoint"))
          this.endpoint = castToUri(value); // UriType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: throw new FHIRException("Cannot make property name as it is not a complex type"); // StringType
        case -880905839:  return getTarget(); // Reference
        case 1741102485: throw new FHIRException("Cannot make property endpoint as it is not a complex type"); // UriType
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type MessageHeader.name");
        }
        else if (name.equals("target")) {
          this.target = new Reference();
          return this.target;
        }
        else if (name.equals("endpoint")) {
          throw new FHIRException("Cannot call addChild on a primitive type MessageHeader.endpoint");
        }
        else
          return super.addChild(name);
      }

      public MessageDestinationComponent copy() {
        MessageDestinationComponent dst = new MessageDestinationComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.target = target == null ? null : target.copy();
        dst.endpoint = endpoint == null ? null : endpoint.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof MessageDestinationComponent))
          return false;
        MessageDestinationComponent o = (MessageDestinationComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(target, o.target, true) && compareDeep(endpoint, o.endpoint, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MessageDestinationComponent))
          return false;
        MessageDestinationComponent o = (MessageDestinationComponent) other;
        return compareValues(name, o.name, true) && compareValues(endpoint, o.endpoint, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (target == null || target.isEmpty())
           && (endpoint == null || endpoint.isEmpty());
      }

  public String fhirType() {
    return "MessageHeader.destination";

  }

  }

    /**
     * The time that the message was sent.
     */
    @Child(name = "timestamp", type = {InstantType.class}, order=0, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Time that the message was sent", formalDefinition="The time that the message was sent." )
    protected InstantType timestamp;

    /**
     * Code that identifies the event this message represents and connects it with its definition. Events defined as part of the FHIR specification have the system value "http://hl7.org/fhir/message-events".
     */
    @Child(name = "event", type = {Coding.class}, order=1, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="Code for the event this message represents", formalDefinition="Code that identifies the event this message represents and connects it with its definition. Events defined as part of the FHIR specification have the system value \"http://hl7.org/fhir/message-events\"." )
    protected Coding event;

    /**
     * Information about the message that this message is a response to.  Only present if this message is a response.
     */
    @Child(name = "response", type = {}, order=2, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="If this is a reply to prior message", formalDefinition="Information about the message that this message is a response to.  Only present if this message is a response." )
    protected MessageHeaderResponseComponent response;

    /**
     * The source application from which this message originated.
     */
    @Child(name = "source", type = {}, order=3, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Message Source Application", formalDefinition="The source application from which this message originated." )
    protected MessageSourceComponent source;

    /**
     * The destination application which the message is intended for.
     */
    @Child(name = "destination", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Message Destination Application(s)", formalDefinition="The destination application which the message is intended for." )
    protected List<MessageDestinationComponent> destination;

    /**
     * The person or device that performed the data entry leading to this message. Where there is more than one candidate, pick the most proximal to the message. Can provide other enterers in extensions.
     */
    @Child(name = "enterer", type = {Practitioner.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The source of the data entry", formalDefinition="The person or device that performed the data entry leading to this message. Where there is more than one candidate, pick the most proximal to the message. Can provide other enterers in extensions." )
    protected Reference enterer;

    /**
     * The actual object that is the target of the reference (The person or device that performed the data entry leading to this message. Where there is more than one candidate, pick the most proximal to the message. Can provide other enterers in extensions.)
     */
    protected Practitioner entererTarget;

    /**
     * The logical author of the message - the person or device that decided the described event should happen. Where there is more than one candidate, pick the most proximal to the MessageHeader. Can provide other authors in extensions.
     */
    @Child(name = "author", type = {Practitioner.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The source of the decision", formalDefinition="The logical author of the message - the person or device that decided the described event should happen. Where there is more than one candidate, pick the most proximal to the MessageHeader. Can provide other authors in extensions." )
    protected Reference author;

    /**
     * The actual object that is the target of the reference (The logical author of the message - the person or device that decided the described event should happen. Where there is more than one candidate, pick the most proximal to the MessageHeader. Can provide other authors in extensions.)
     */
    protected Practitioner authorTarget;

    /**
     * Allows data conveyed by a message to be addressed to a particular person or department when routing to a specific application isn't sufficient.
     */
    @Child(name = "receiver", type = {Practitioner.class, Organization.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Intended \"real-world\" recipient for the data", formalDefinition="Allows data conveyed by a message to be addressed to a particular person or department when routing to a specific application isn't sufficient." )
    protected Reference receiver;

    /**
     * The actual object that is the target of the reference (Allows data conveyed by a message to be addressed to a particular person or department when routing to a specific application isn't sufficient.)
     */
    protected Resource receiverTarget;

    /**
     * The person or organization that accepts overall responsibility for the contents of the message. The implication is that the message event happened under the policies of the responsible party.
     */
    @Child(name = "responsible", type = {Practitioner.class, Organization.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Final responsibility for event", formalDefinition="The person or organization that accepts overall responsibility for the contents of the message. The implication is that the message event happened under the policies of the responsible party." )
    protected Reference responsible;

    /**
     * The actual object that is the target of the reference (The person or organization that accepts overall responsibility for the contents of the message. The implication is that the message event happened under the policies of the responsible party.)
     */
    protected Resource responsibleTarget;

    /**
     * Coded indication of the cause for the event - indicates  a reason for the occurrence of the event that is a focus of this message.
     */
    @Child(name = "reason", type = {CodeableConcept.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Cause of event", formalDefinition="Coded indication of the cause for the event - indicates  a reason for the occurrence of the event that is a focus of this message." )
    protected CodeableConcept reason;

    /**
     * The actual data of the message - a reference to the root/focus class of the event.
     */
    @Child(name = "data", type = {}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="The actual content of the message", formalDefinition="The actual data of the message - a reference to the root/focus class of the event." )
    protected List<Reference> data;
    /**
     * The actual objects that are the target of the reference (The actual data of the message - a reference to the root/focus class of the event.)
     */
    protected List<Resource> dataTarget;


    private static final long serialVersionUID = 1429728517L;

  /**
   * Constructor
   */
    public MessageHeader() {
      super();
    }

  /**
   * Constructor
   */
    public MessageHeader(InstantType timestamp, Coding event, MessageSourceComponent source) {
      super();
      this.timestamp = timestamp;
      this.event = event;
      this.source = source;
    }

    /**
     * @return {@link #timestamp} (The time that the message was sent.). This is the underlying object with id, value and extensions. The accessor "getTimestamp" gives direct access to the value
     */
    public InstantType getTimestampElement() { 
      if (this.timestamp == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MessageHeader.timestamp");
        else if (Configuration.doAutoCreate())
          this.timestamp = new InstantType(); // bb
      return this.timestamp;
    }

    public boolean hasTimestampElement() { 
      return this.timestamp != null && !this.timestamp.isEmpty();
    }

    public boolean hasTimestamp() { 
      return this.timestamp != null && !this.timestamp.isEmpty();
    }

    /**
     * @param value {@link #timestamp} (The time that the message was sent.). This is the underlying object with id, value and extensions. The accessor "getTimestamp" gives direct access to the value
     */
    public MessageHeader setTimestampElement(InstantType value) { 
      this.timestamp = value;
      return this;
    }

    /**
     * @return The time that the message was sent.
     */
    public Date getTimestamp() { 
      return this.timestamp == null ? null : this.timestamp.getValue();
    }

    /**
     * @param value The time that the message was sent.
     */
    public MessageHeader setTimestamp(Date value) { 
        if (this.timestamp == null)
          this.timestamp = new InstantType();
        this.timestamp.setValue(value);
      return this;
    }

    /**
     * @return {@link #event} (Code that identifies the event this message represents and connects it with its definition. Events defined as part of the FHIR specification have the system value "http://hl7.org/fhir/message-events".)
     */
    public Coding getEvent() { 
      if (this.event == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MessageHeader.event");
        else if (Configuration.doAutoCreate())
          this.event = new Coding(); // cc
      return this.event;
    }

    public boolean hasEvent() { 
      return this.event != null && !this.event.isEmpty();
    }

    /**
     * @param value {@link #event} (Code that identifies the event this message represents and connects it with its definition. Events defined as part of the FHIR specification have the system value "http://hl7.org/fhir/message-events".)
     */
    public MessageHeader setEvent(Coding value) { 
      this.event = value;
      return this;
    }

    /**
     * @return {@link #response} (Information about the message that this message is a response to.  Only present if this message is a response.)
     */
    public MessageHeaderResponseComponent getResponse() { 
      if (this.response == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MessageHeader.response");
        else if (Configuration.doAutoCreate())
          this.response = new MessageHeaderResponseComponent(); // cc
      return this.response;
    }

    public boolean hasResponse() { 
      return this.response != null && !this.response.isEmpty();
    }

    /**
     * @param value {@link #response} (Information about the message that this message is a response to.  Only present if this message is a response.)
     */
    public MessageHeader setResponse(MessageHeaderResponseComponent value) { 
      this.response = value;
      return this;
    }

    /**
     * @return {@link #source} (The source application from which this message originated.)
     */
    public MessageSourceComponent getSource() { 
      if (this.source == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MessageHeader.source");
        else if (Configuration.doAutoCreate())
          this.source = new MessageSourceComponent(); // cc
      return this.source;
    }

    public boolean hasSource() { 
      return this.source != null && !this.source.isEmpty();
    }

    /**
     * @param value {@link #source} (The source application from which this message originated.)
     */
    public MessageHeader setSource(MessageSourceComponent value) { 
      this.source = value;
      return this;
    }

    /**
     * @return {@link #destination} (The destination application which the message is intended for.)
     */
    public List<MessageDestinationComponent> getDestination() { 
      if (this.destination == null)
        this.destination = new ArrayList<MessageDestinationComponent>();
      return this.destination;
    }

    public boolean hasDestination() { 
      if (this.destination == null)
        return false;
      for (MessageDestinationComponent item : this.destination)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #destination} (The destination application which the message is intended for.)
     */
    // syntactic sugar
    public MessageDestinationComponent addDestination() { //3
      MessageDestinationComponent t = new MessageDestinationComponent();
      if (this.destination == null)
        this.destination = new ArrayList<MessageDestinationComponent>();
      this.destination.add(t);
      return t;
    }

    // syntactic sugar
    public MessageHeader addDestination(MessageDestinationComponent t) { //3
      if (t == null)
        return this;
      if (this.destination == null)
        this.destination = new ArrayList<MessageDestinationComponent>();
      this.destination.add(t);
      return this;
    }

    /**
     * @return {@link #enterer} (The person or device that performed the data entry leading to this message. Where there is more than one candidate, pick the most proximal to the message. Can provide other enterers in extensions.)
     */
    public Reference getEnterer() { 
      if (this.enterer == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MessageHeader.enterer");
        else if (Configuration.doAutoCreate())
          this.enterer = new Reference(); // cc
      return this.enterer;
    }

    public boolean hasEnterer() { 
      return this.enterer != null && !this.enterer.isEmpty();
    }

    /**
     * @param value {@link #enterer} (The person or device that performed the data entry leading to this message. Where there is more than one candidate, pick the most proximal to the message. Can provide other enterers in extensions.)
     */
    public MessageHeader setEnterer(Reference value) { 
      this.enterer = value;
      return this;
    }

    /**
     * @return {@link #enterer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The person or device that performed the data entry leading to this message. Where there is more than one candidate, pick the most proximal to the message. Can provide other enterers in extensions.)
     */
    public Practitioner getEntererTarget() { 
      if (this.entererTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MessageHeader.enterer");
        else if (Configuration.doAutoCreate())
          this.entererTarget = new Practitioner(); // aa
      return this.entererTarget;
    }

    /**
     * @param value {@link #enterer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The person or device that performed the data entry leading to this message. Where there is more than one candidate, pick the most proximal to the message. Can provide other enterers in extensions.)
     */
    public MessageHeader setEntererTarget(Practitioner value) { 
      this.entererTarget = value;
      return this;
    }

    /**
     * @return {@link #author} (The logical author of the message - the person or device that decided the described event should happen. Where there is more than one candidate, pick the most proximal to the MessageHeader. Can provide other authors in extensions.)
     */
    public Reference getAuthor() { 
      if (this.author == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MessageHeader.author");
        else if (Configuration.doAutoCreate())
          this.author = new Reference(); // cc
      return this.author;
    }

    public boolean hasAuthor() { 
      return this.author != null && !this.author.isEmpty();
    }

    /**
     * @param value {@link #author} (The logical author of the message - the person or device that decided the described event should happen. Where there is more than one candidate, pick the most proximal to the MessageHeader. Can provide other authors in extensions.)
     */
    public MessageHeader setAuthor(Reference value) { 
      this.author = value;
      return this;
    }

    /**
     * @return {@link #author} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The logical author of the message - the person or device that decided the described event should happen. Where there is more than one candidate, pick the most proximal to the MessageHeader. Can provide other authors in extensions.)
     */
    public Practitioner getAuthorTarget() { 
      if (this.authorTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MessageHeader.author");
        else if (Configuration.doAutoCreate())
          this.authorTarget = new Practitioner(); // aa
      return this.authorTarget;
    }

    /**
     * @param value {@link #author} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The logical author of the message - the person or device that decided the described event should happen. Where there is more than one candidate, pick the most proximal to the MessageHeader. Can provide other authors in extensions.)
     */
    public MessageHeader setAuthorTarget(Practitioner value) { 
      this.authorTarget = value;
      return this;
    }

    /**
     * @return {@link #receiver} (Allows data conveyed by a message to be addressed to a particular person or department when routing to a specific application isn't sufficient.)
     */
    public Reference getReceiver() { 
      if (this.receiver == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MessageHeader.receiver");
        else if (Configuration.doAutoCreate())
          this.receiver = new Reference(); // cc
      return this.receiver;
    }

    public boolean hasReceiver() { 
      return this.receiver != null && !this.receiver.isEmpty();
    }

    /**
     * @param value {@link #receiver} (Allows data conveyed by a message to be addressed to a particular person or department when routing to a specific application isn't sufficient.)
     */
    public MessageHeader setReceiver(Reference value) { 
      this.receiver = value;
      return this;
    }

    /**
     * @return {@link #receiver} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Allows data conveyed by a message to be addressed to a particular person or department when routing to a specific application isn't sufficient.)
     */
    public Resource getReceiverTarget() { 
      return this.receiverTarget;
    }

    /**
     * @param value {@link #receiver} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Allows data conveyed by a message to be addressed to a particular person or department when routing to a specific application isn't sufficient.)
     */
    public MessageHeader setReceiverTarget(Resource value) { 
      this.receiverTarget = value;
      return this;
    }

    /**
     * @return {@link #responsible} (The person or organization that accepts overall responsibility for the contents of the message. The implication is that the message event happened under the policies of the responsible party.)
     */
    public Reference getResponsible() { 
      if (this.responsible == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MessageHeader.responsible");
        else if (Configuration.doAutoCreate())
          this.responsible = new Reference(); // cc
      return this.responsible;
    }

    public boolean hasResponsible() { 
      return this.responsible != null && !this.responsible.isEmpty();
    }

    /**
     * @param value {@link #responsible} (The person or organization that accepts overall responsibility for the contents of the message. The implication is that the message event happened under the policies of the responsible party.)
     */
    public MessageHeader setResponsible(Reference value) { 
      this.responsible = value;
      return this;
    }

    /**
     * @return {@link #responsible} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The person or organization that accepts overall responsibility for the contents of the message. The implication is that the message event happened under the policies of the responsible party.)
     */
    public Resource getResponsibleTarget() { 
      return this.responsibleTarget;
    }

    /**
     * @param value {@link #responsible} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The person or organization that accepts overall responsibility for the contents of the message. The implication is that the message event happened under the policies of the responsible party.)
     */
    public MessageHeader setResponsibleTarget(Resource value) { 
      this.responsibleTarget = value;
      return this;
    }

    /**
     * @return {@link #reason} (Coded indication of the cause for the event - indicates  a reason for the occurrence of the event that is a focus of this message.)
     */
    public CodeableConcept getReason() { 
      if (this.reason == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MessageHeader.reason");
        else if (Configuration.doAutoCreate())
          this.reason = new CodeableConcept(); // cc
      return this.reason;
    }

    public boolean hasReason() { 
      return this.reason != null && !this.reason.isEmpty();
    }

    /**
     * @param value {@link #reason} (Coded indication of the cause for the event - indicates  a reason for the occurrence of the event that is a focus of this message.)
     */
    public MessageHeader setReason(CodeableConcept value) { 
      this.reason = value;
      return this;
    }

    /**
     * @return {@link #data} (The actual data of the message - a reference to the root/focus class of the event.)
     */
    public List<Reference> getData() { 
      if (this.data == null)
        this.data = new ArrayList<Reference>();
      return this.data;
    }

    public boolean hasData() { 
      if (this.data == null)
        return false;
      for (Reference item : this.data)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #data} (The actual data of the message - a reference to the root/focus class of the event.)
     */
    // syntactic sugar
    public Reference addData() { //3
      Reference t = new Reference();
      if (this.data == null)
        this.data = new ArrayList<Reference>();
      this.data.add(t);
      return t;
    }

    // syntactic sugar
    public MessageHeader addData(Reference t) { //3
      if (t == null)
        return this;
      if (this.data == null)
        this.data = new ArrayList<Reference>();
      this.data.add(t);
      return this;
    }

    /**
     * @return {@link #data} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. The actual data of the message - a reference to the root/focus class of the event.)
     */
    public List<Resource> getDataTarget() { 
      if (this.dataTarget == null)
        this.dataTarget = new ArrayList<Resource>();
      return this.dataTarget;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("timestamp", "instant", "The time that the message was sent.", 0, java.lang.Integer.MAX_VALUE, timestamp));
        childrenList.add(new Property("event", "Coding", "Code that identifies the event this message represents and connects it with its definition. Events defined as part of the FHIR specification have the system value \"http://hl7.org/fhir/message-events\".", 0, java.lang.Integer.MAX_VALUE, event));
        childrenList.add(new Property("response", "", "Information about the message that this message is a response to.  Only present if this message is a response.", 0, java.lang.Integer.MAX_VALUE, response));
        childrenList.add(new Property("source", "", "The source application from which this message originated.", 0, java.lang.Integer.MAX_VALUE, source));
        childrenList.add(new Property("destination", "", "The destination application which the message is intended for.", 0, java.lang.Integer.MAX_VALUE, destination));
        childrenList.add(new Property("enterer", "Reference(Practitioner)", "The person or device that performed the data entry leading to this message. Where there is more than one candidate, pick the most proximal to the message. Can provide other enterers in extensions.", 0, java.lang.Integer.MAX_VALUE, enterer));
        childrenList.add(new Property("author", "Reference(Practitioner)", "The logical author of the message - the person or device that decided the described event should happen. Where there is more than one candidate, pick the most proximal to the MessageHeader. Can provide other authors in extensions.", 0, java.lang.Integer.MAX_VALUE, author));
        childrenList.add(new Property("receiver", "Reference(Practitioner|Organization)", "Allows data conveyed by a message to be addressed to a particular person or department when routing to a specific application isn't sufficient.", 0, java.lang.Integer.MAX_VALUE, receiver));
        childrenList.add(new Property("responsible", "Reference(Practitioner|Organization)", "The person or organization that accepts overall responsibility for the contents of the message. The implication is that the message event happened under the policies of the responsible party.", 0, java.lang.Integer.MAX_VALUE, responsible));
        childrenList.add(new Property("reason", "CodeableConcept", "Coded indication of the cause for the event - indicates  a reason for the occurrence of the event that is a focus of this message.", 0, java.lang.Integer.MAX_VALUE, reason));
        childrenList.add(new Property("data", "Reference(Any)", "The actual data of the message - a reference to the root/focus class of the event.", 0, java.lang.Integer.MAX_VALUE, data));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 55126294: /*timestamp*/ return this.timestamp == null ? new Base[0] : new Base[] {this.timestamp}; // InstantType
        case 96891546: /*event*/ return this.event == null ? new Base[0] : new Base[] {this.event}; // Coding
        case -340323263: /*response*/ return this.response == null ? new Base[0] : new Base[] {this.response}; // MessageHeaderResponseComponent
        case -896505829: /*source*/ return this.source == null ? new Base[0] : new Base[] {this.source}; // MessageSourceComponent
        case -1429847026: /*destination*/ return this.destination == null ? new Base[0] : this.destination.toArray(new Base[this.destination.size()]); // MessageDestinationComponent
        case -1591951995: /*enterer*/ return this.enterer == null ? new Base[0] : new Base[] {this.enterer}; // Reference
        case -1406328437: /*author*/ return this.author == null ? new Base[0] : new Base[] {this.author}; // Reference
        case -808719889: /*receiver*/ return this.receiver == null ? new Base[0] : new Base[] {this.receiver}; // Reference
        case 1847674614: /*responsible*/ return this.responsible == null ? new Base[0] : new Base[] {this.responsible}; // Reference
        case -934964668: /*reason*/ return this.reason == null ? new Base[0] : new Base[] {this.reason}; // CodeableConcept
        case 3076010: /*data*/ return this.data == null ? new Base[0] : this.data.toArray(new Base[this.data.size()]); // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 55126294: // timestamp
          this.timestamp = castToInstant(value); // InstantType
          break;
        case 96891546: // event
          this.event = castToCoding(value); // Coding
          break;
        case -340323263: // response
          this.response = (MessageHeaderResponseComponent) value; // MessageHeaderResponseComponent
          break;
        case -896505829: // source
          this.source = (MessageSourceComponent) value; // MessageSourceComponent
          break;
        case -1429847026: // destination
          this.getDestination().add((MessageDestinationComponent) value); // MessageDestinationComponent
          break;
        case -1591951995: // enterer
          this.enterer = castToReference(value); // Reference
          break;
        case -1406328437: // author
          this.author = castToReference(value); // Reference
          break;
        case -808719889: // receiver
          this.receiver = castToReference(value); // Reference
          break;
        case 1847674614: // responsible
          this.responsible = castToReference(value); // Reference
          break;
        case -934964668: // reason
          this.reason = castToCodeableConcept(value); // CodeableConcept
          break;
        case 3076010: // data
          this.getData().add(castToReference(value)); // Reference
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("timestamp"))
          this.timestamp = castToInstant(value); // InstantType
        else if (name.equals("event"))
          this.event = castToCoding(value); // Coding
        else if (name.equals("response"))
          this.response = (MessageHeaderResponseComponent) value; // MessageHeaderResponseComponent
        else if (name.equals("source"))
          this.source = (MessageSourceComponent) value; // MessageSourceComponent
        else if (name.equals("destination"))
          this.getDestination().add((MessageDestinationComponent) value);
        else if (name.equals("enterer"))
          this.enterer = castToReference(value); // Reference
        else if (name.equals("author"))
          this.author = castToReference(value); // Reference
        else if (name.equals("receiver"))
          this.receiver = castToReference(value); // Reference
        else if (name.equals("responsible"))
          this.responsible = castToReference(value); // Reference
        else if (name.equals("reason"))
          this.reason = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("data"))
          this.getData().add(castToReference(value));
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 55126294: throw new FHIRException("Cannot make property timestamp as it is not a complex type"); // InstantType
        case 96891546:  return getEvent(); // Coding
        case -340323263:  return getResponse(); // MessageHeaderResponseComponent
        case -896505829:  return getSource(); // MessageSourceComponent
        case -1429847026:  return addDestination(); // MessageDestinationComponent
        case -1591951995:  return getEnterer(); // Reference
        case -1406328437:  return getAuthor(); // Reference
        case -808719889:  return getReceiver(); // Reference
        case 1847674614:  return getResponsible(); // Reference
        case -934964668:  return getReason(); // CodeableConcept
        case 3076010:  return addData(); // Reference
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("timestamp")) {
          throw new FHIRException("Cannot call addChild on a primitive type MessageHeader.timestamp");
        }
        else if (name.equals("event")) {
          this.event = new Coding();
          return this.event;
        }
        else if (name.equals("response")) {
          this.response = new MessageHeaderResponseComponent();
          return this.response;
        }
        else if (name.equals("source")) {
          this.source = new MessageSourceComponent();
          return this.source;
        }
        else if (name.equals("destination")) {
          return addDestination();
        }
        else if (name.equals("enterer")) {
          this.enterer = new Reference();
          return this.enterer;
        }
        else if (name.equals("author")) {
          this.author = new Reference();
          return this.author;
        }
        else if (name.equals("receiver")) {
          this.receiver = new Reference();
          return this.receiver;
        }
        else if (name.equals("responsible")) {
          this.responsible = new Reference();
          return this.responsible;
        }
        else if (name.equals("reason")) {
          this.reason = new CodeableConcept();
          return this.reason;
        }
        else if (name.equals("data")) {
          return addData();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "MessageHeader";

  }

      public MessageHeader copy() {
        MessageHeader dst = new MessageHeader();
        copyValues(dst);
        dst.timestamp = timestamp == null ? null : timestamp.copy();
        dst.event = event == null ? null : event.copy();
        dst.response = response == null ? null : response.copy();
        dst.source = source == null ? null : source.copy();
        if (destination != null) {
          dst.destination = new ArrayList<MessageDestinationComponent>();
          for (MessageDestinationComponent i : destination)
            dst.destination.add(i.copy());
        };
        dst.enterer = enterer == null ? null : enterer.copy();
        dst.author = author == null ? null : author.copy();
        dst.receiver = receiver == null ? null : receiver.copy();
        dst.responsible = responsible == null ? null : responsible.copy();
        dst.reason = reason == null ? null : reason.copy();
        if (data != null) {
          dst.data = new ArrayList<Reference>();
          for (Reference i : data)
            dst.data.add(i.copy());
        };
        return dst;
      }

      protected MessageHeader typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof MessageHeader))
          return false;
        MessageHeader o = (MessageHeader) other;
        return compareDeep(timestamp, o.timestamp, true) && compareDeep(event, o.event, true) && compareDeep(response, o.response, true)
           && compareDeep(source, o.source, true) && compareDeep(destination, o.destination, true) && compareDeep(enterer, o.enterer, true)
           && compareDeep(author, o.author, true) && compareDeep(receiver, o.receiver, true) && compareDeep(responsible, o.responsible, true)
           && compareDeep(reason, o.reason, true) && compareDeep(data, o.data, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MessageHeader))
          return false;
        MessageHeader o = (MessageHeader) other;
        return compareValues(timestamp, o.timestamp, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (timestamp == null || timestamp.isEmpty()) && (event == null || event.isEmpty())
           && (response == null || response.isEmpty()) && (source == null || source.isEmpty()) && (destination == null || destination.isEmpty())
           && (enterer == null || enterer.isEmpty()) && (author == null || author.isEmpty()) && (receiver == null || receiver.isEmpty())
           && (responsible == null || responsible.isEmpty()) && (reason == null || reason.isEmpty())
           && (data == null || data.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.MessageHeader;
   }

 /**
   * Search parameter: <b>destination-uri</b>
   * <p>
   * Description: <b>Actual destination address or id</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>MessageHeader.destination.endpoint</b><br>
   * </p>
   */
  @SearchParamDefinition(name="destination-uri", path="MessageHeader.destination.endpoint", description="Actual destination address or id", type="uri" )
  public static final String SP_DESTINATION_URI = "destination-uri";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>destination-uri</b>
   * <p>
   * Description: <b>Actual destination address or id</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>MessageHeader.destination.endpoint</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam DESTINATION_URI = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_DESTINATION_URI);

 /**
   * Search parameter: <b>receiver</b>
   * <p>
   * Description: <b>Intended "real-world" recipient for the data</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MessageHeader.receiver</b><br>
   * </p>
   */
  @SearchParamDefinition(name="receiver", path="MessageHeader.receiver", description="Intended \"real-world\" recipient for the data", type="reference" )
  public static final String SP_RECEIVER = "receiver";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>receiver</b>
   * <p>
   * Description: <b>Intended "real-world" recipient for the data</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MessageHeader.receiver</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam RECEIVER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_RECEIVER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MessageHeader:receiver</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_RECEIVER = new ca.uhn.fhir.model.api.Include("MessageHeader:receiver").toLocked();

 /**
   * Search parameter: <b>responsible</b>
   * <p>
   * Description: <b>Final responsibility for event</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MessageHeader.responsible</b><br>
   * </p>
   */
  @SearchParamDefinition(name="responsible", path="MessageHeader.responsible", description="Final responsibility for event", type="reference" )
  public static final String SP_RESPONSIBLE = "responsible";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>responsible</b>
   * <p>
   * Description: <b>Final responsibility for event</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MessageHeader.responsible</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam RESPONSIBLE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_RESPONSIBLE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MessageHeader:responsible</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_RESPONSIBLE = new ca.uhn.fhir.model.api.Include("MessageHeader:responsible").toLocked();

 /**
   * Search parameter: <b>data</b>
   * <p>
   * Description: <b>The actual content of the message</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MessageHeader.data</b><br>
   * </p>
   */
  @SearchParamDefinition(name="data", path="MessageHeader.data", description="The actual content of the message", type="reference" )
  public static final String SP_DATA = "data";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>data</b>
   * <p>
   * Description: <b>The actual content of the message</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MessageHeader.data</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam DATA = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_DATA);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MessageHeader:data</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_DATA = new ca.uhn.fhir.model.api.Include("MessageHeader:data").toLocked();

 /**
   * Search parameter: <b>code</b>
   * <p>
   * Description: <b>ok | transient-error | fatal-error</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MessageHeader.response.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="code", path="MessageHeader.response.code", description="ok | transient-error | fatal-error", type="token" )
  public static final String SP_CODE = "code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>code</b>
   * <p>
   * Description: <b>ok | transient-error | fatal-error</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MessageHeader.response.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CODE);

 /**
   * Search parameter: <b>response-id</b>
   * <p>
   * Description: <b>Id of original message</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MessageHeader.response.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="response-id", path="MessageHeader.response.identifier", description="Id of original message", type="token" )
  public static final String SP_RESPONSE_ID = "response-id";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>response-id</b>
   * <p>
   * Description: <b>Id of original message</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MessageHeader.response.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam RESPONSE_ID = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_RESPONSE_ID);

 /**
   * Search parameter: <b>destination</b>
   * <p>
   * Description: <b>Name of system</b><br>
   * Type: <b>string</b><br>
   * Path: <b>MessageHeader.destination.name</b><br>
   * </p>
   */
  @SearchParamDefinition(name="destination", path="MessageHeader.destination.name", description="Name of system", type="string" )
  public static final String SP_DESTINATION = "destination";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>destination</b>
   * <p>
   * Description: <b>Name of system</b><br>
   * Type: <b>string</b><br>
   * Path: <b>MessageHeader.destination.name</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam DESTINATION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_DESTINATION);

 /**
   * Search parameter: <b>timestamp</b>
   * <p>
   * Description: <b>Time that the message was sent</b><br>
   * Type: <b>date</b><br>
   * Path: <b>MessageHeader.timestamp</b><br>
   * </p>
   */
  @SearchParamDefinition(name="timestamp", path="MessageHeader.timestamp", description="Time that the message was sent", type="date" )
  public static final String SP_TIMESTAMP = "timestamp";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>timestamp</b>
   * <p>
   * Description: <b>Time that the message was sent</b><br>
   * Type: <b>date</b><br>
   * Path: <b>MessageHeader.timestamp</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam TIMESTAMP = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_TIMESTAMP);

 /**
   * Search parameter: <b>author</b>
   * <p>
   * Description: <b>The source of the decision</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MessageHeader.author</b><br>
   * </p>
   */
  @SearchParamDefinition(name="author", path="MessageHeader.author", description="The source of the decision", type="reference" )
  public static final String SP_AUTHOR = "author";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>author</b>
   * <p>
   * Description: <b>The source of the decision</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MessageHeader.author</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam AUTHOR = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_AUTHOR);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MessageHeader:author</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_AUTHOR = new ca.uhn.fhir.model.api.Include("MessageHeader:author").toLocked();

 /**
   * Search parameter: <b>source-uri</b>
   * <p>
   * Description: <b>Actual message source address or id</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>MessageHeader.source.endpoint</b><br>
   * </p>
   */
  @SearchParamDefinition(name="source-uri", path="MessageHeader.source.endpoint", description="Actual message source address or id", type="uri" )
  public static final String SP_SOURCE_URI = "source-uri";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>source-uri</b>
   * <p>
   * Description: <b>Actual message source address or id</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>MessageHeader.source.endpoint</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam SOURCE_URI = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_SOURCE_URI);

 /**
   * Search parameter: <b>source</b>
   * <p>
   * Description: <b>Name of system</b><br>
   * Type: <b>string</b><br>
   * Path: <b>MessageHeader.source.name</b><br>
   * </p>
   */
  @SearchParamDefinition(name="source", path="MessageHeader.source.name", description="Name of system", type="string" )
  public static final String SP_SOURCE = "source";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>source</b>
   * <p>
   * Description: <b>Name of system</b><br>
   * Type: <b>string</b><br>
   * Path: <b>MessageHeader.source.name</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam SOURCE = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_SOURCE);

 /**
   * Search parameter: <b>enterer</b>
   * <p>
   * Description: <b>The source of the data entry</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MessageHeader.enterer</b><br>
   * </p>
   */
  @SearchParamDefinition(name="enterer", path="MessageHeader.enterer", description="The source of the data entry", type="reference" )
  public static final String SP_ENTERER = "enterer";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>enterer</b>
   * <p>
   * Description: <b>The source of the data entry</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MessageHeader.enterer</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ENTERER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ENTERER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MessageHeader:enterer</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ENTERER = new ca.uhn.fhir.model.api.Include("MessageHeader:enterer").toLocked();

 /**
   * Search parameter: <b>event</b>
   * <p>
   * Description: <b>Code for the event this message represents</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MessageHeader.event</b><br>
   * </p>
   */
  @SearchParamDefinition(name="event", path="MessageHeader.event", description="Code for the event this message represents", type="token" )
  public static final String SP_EVENT = "event";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>event</b>
   * <p>
   * Description: <b>Code for the event this message represents</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MessageHeader.event</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam EVENT = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_EVENT);

 /**
   * Search parameter: <b>target</b>
   * <p>
   * Description: <b>Particular delivery destination within the destination</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MessageHeader.destination.target</b><br>
   * </p>
   */
  @SearchParamDefinition(name="target", path="MessageHeader.destination.target", description="Particular delivery destination within the destination", type="reference" )
  public static final String SP_TARGET = "target";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>target</b>
   * <p>
   * Description: <b>Particular delivery destination within the destination</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MessageHeader.destination.target</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam TARGET = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_TARGET);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MessageHeader:target</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_TARGET = new ca.uhn.fhir.model.api.Include("MessageHeader:target").toLocked();


}

