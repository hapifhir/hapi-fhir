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

// Generated on Sun, May 6, 2018 17:51-0400 for FHIR v3.4.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.r4.model.Enumerations.*;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * A record of a healthcare consumer’s  choices, which permits or denies identified recipient(s) or recipient role(s) to perform one or more actions within a given policy context, for specific purposes and periods of time.
 */
@ResourceDef(name="Consent", profile="http://hl7.org/fhir/Profile/Consent")
public class Consent extends DomainResource {

    public enum ConsentState {
        /**
         * The core event has not started yet, but some staging activities have begun (e.g. surgical suite preparation).  Preparation stages may be tracked for billing purposes.
         */
        PREPARATION, 
        /**
         * The event is currently occurring
         */
        INPROGRESS, 
        /**
         * The event was terminated prior to any impact on the subject (though preparatory actions may have been taken)
         */
        NOTDONE, 
        /**
         * The event has been temporarily stopped but is expected to resume in the future
         */
        SUSPENDED, 
        /**
         * The event was  terminated prior to the full completion of the intended actions but after having at least some impact on the subject.
         */
        ABORTED, 
        /**
         * The event has now concluded
         */
        COMPLETED, 
        /**
         * This electronic record should never have existed, though it is possible that real-world decisions were based on it.  (If real-world activity has occurred, the status should be "cancelled" rather than "entered-in-error".)
         */
        ENTEREDINERROR, 
        /**
         * The authoring system does not know which of the status values currently applies for this request.  Note: This concept is not to be used for "other" - one of the listed statuses is presumed to apply, it's just not known which one.
         */
        UNKNOWN, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ConsentState fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("preparation".equals(codeString))
          return PREPARATION;
        if ("in-progress".equals(codeString))
          return INPROGRESS;
        if ("not-done".equals(codeString))
          return NOTDONE;
        if ("suspended".equals(codeString))
          return SUSPENDED;
        if ("aborted".equals(codeString))
          return ABORTED;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if ("unknown".equals(codeString))
          return UNKNOWN;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ConsentState code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PREPARATION: return "preparation";
            case INPROGRESS: return "in-progress";
            case NOTDONE: return "not-done";
            case SUSPENDED: return "suspended";
            case ABORTED: return "aborted";
            case COMPLETED: return "completed";
            case ENTEREDINERROR: return "entered-in-error";
            case UNKNOWN: return "unknown";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PREPARATION: return "http://hl7.org/fhir/event-status";
            case INPROGRESS: return "http://hl7.org/fhir/event-status";
            case NOTDONE: return "http://hl7.org/fhir/event-status";
            case SUSPENDED: return "http://hl7.org/fhir/event-status";
            case ABORTED: return "http://hl7.org/fhir/event-status";
            case COMPLETED: return "http://hl7.org/fhir/event-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/event-status";
            case UNKNOWN: return "http://hl7.org/fhir/event-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PREPARATION: return "The core event has not started yet, but some staging activities have begun (e.g. surgical suite preparation).  Preparation stages may be tracked for billing purposes.";
            case INPROGRESS: return "The event is currently occurring";
            case NOTDONE: return "The event was terminated prior to any impact on the subject (though preparatory actions may have been taken)";
            case SUSPENDED: return "The event has been temporarily stopped but is expected to resume in the future";
            case ABORTED: return "The event was  terminated prior to the full completion of the intended actions but after having at least some impact on the subject.";
            case COMPLETED: return "The event has now concluded";
            case ENTEREDINERROR: return "This electronic record should never have existed, though it is possible that real-world decisions were based on it.  (If real-world activity has occurred, the status should be \"cancelled\" rather than \"entered-in-error\".)";
            case UNKNOWN: return "The authoring system does not know which of the status values currently applies for this request.  Note: This concept is not to be used for \"other\" - one of the listed statuses is presumed to apply, it's just not known which one.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PREPARATION: return "Preparation";
            case INPROGRESS: return "In Progress";
            case NOTDONE: return "Not Done";
            case SUSPENDED: return "Suspended";
            case ABORTED: return "Aborted";
            case COMPLETED: return "Completed";
            case ENTEREDINERROR: return "Entered in Error";
            case UNKNOWN: return "Unknown";
            default: return "?";
          }
        }
    }

  public static class ConsentStateEnumFactory implements EnumFactory<ConsentState> {
    public ConsentState fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("preparation".equals(codeString))
          return ConsentState.PREPARATION;
        if ("in-progress".equals(codeString))
          return ConsentState.INPROGRESS;
        if ("not-done".equals(codeString))
          return ConsentState.NOTDONE;
        if ("suspended".equals(codeString))
          return ConsentState.SUSPENDED;
        if ("aborted".equals(codeString))
          return ConsentState.ABORTED;
        if ("completed".equals(codeString))
          return ConsentState.COMPLETED;
        if ("entered-in-error".equals(codeString))
          return ConsentState.ENTEREDINERROR;
        if ("unknown".equals(codeString))
          return ConsentState.UNKNOWN;
        throw new IllegalArgumentException("Unknown ConsentState code '"+codeString+"'");
        }
        public Enumeration<ConsentState> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ConsentState>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("preparation".equals(codeString))
          return new Enumeration<ConsentState>(this, ConsentState.PREPARATION);
        if ("in-progress".equals(codeString))
          return new Enumeration<ConsentState>(this, ConsentState.INPROGRESS);
        if ("not-done".equals(codeString))
          return new Enumeration<ConsentState>(this, ConsentState.NOTDONE);
        if ("suspended".equals(codeString))
          return new Enumeration<ConsentState>(this, ConsentState.SUSPENDED);
        if ("aborted".equals(codeString))
          return new Enumeration<ConsentState>(this, ConsentState.ABORTED);
        if ("completed".equals(codeString))
          return new Enumeration<ConsentState>(this, ConsentState.COMPLETED);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<ConsentState>(this, ConsentState.ENTEREDINERROR);
        if ("unknown".equals(codeString))
          return new Enumeration<ConsentState>(this, ConsentState.UNKNOWN);
        throw new FHIRException("Unknown ConsentState code '"+codeString+"'");
        }
    public String toCode(ConsentState code) {
      if (code == ConsentState.PREPARATION)
        return "preparation";
      if (code == ConsentState.INPROGRESS)
        return "in-progress";
      if (code == ConsentState.NOTDONE)
        return "not-done";
      if (code == ConsentState.SUSPENDED)
        return "suspended";
      if (code == ConsentState.ABORTED)
        return "aborted";
      if (code == ConsentState.COMPLETED)
        return "completed";
      if (code == ConsentState.ENTEREDINERROR)
        return "entered-in-error";
      if (code == ConsentState.UNKNOWN)
        return "unknown";
      return "?";
      }
    public String toSystem(ConsentState code) {
      return code.getSystem();
      }
    }

    public enum ConsentProvisionType {
        /**
         * Consent is denied for actions meeting these rules
         */
        DENY, 
        /**
         * Consent is provided for actions meeting these rules
         */
        PERMIT, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ConsentProvisionType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("deny".equals(codeString))
          return DENY;
        if ("permit".equals(codeString))
          return PERMIT;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ConsentProvisionType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DENY: return "deny";
            case PERMIT: return "permit";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case DENY: return "http://hl7.org/fhir/consent-provision-type";
            case PERMIT: return "http://hl7.org/fhir/consent-provision-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case DENY: return "Consent is denied for actions meeting these rules";
            case PERMIT: return "Consent is provided for actions meeting these rules";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DENY: return "Opt Out";
            case PERMIT: return "Opt In";
            default: return "?";
          }
        }
    }

  public static class ConsentProvisionTypeEnumFactory implements EnumFactory<ConsentProvisionType> {
    public ConsentProvisionType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("deny".equals(codeString))
          return ConsentProvisionType.DENY;
        if ("permit".equals(codeString))
          return ConsentProvisionType.PERMIT;
        throw new IllegalArgumentException("Unknown ConsentProvisionType code '"+codeString+"'");
        }
        public Enumeration<ConsentProvisionType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ConsentProvisionType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("deny".equals(codeString))
          return new Enumeration<ConsentProvisionType>(this, ConsentProvisionType.DENY);
        if ("permit".equals(codeString))
          return new Enumeration<ConsentProvisionType>(this, ConsentProvisionType.PERMIT);
        throw new FHIRException("Unknown ConsentProvisionType code '"+codeString+"'");
        }
    public String toCode(ConsentProvisionType code) {
      if (code == ConsentProvisionType.DENY)
        return "deny";
      if (code == ConsentProvisionType.PERMIT)
        return "permit";
      return "?";
      }
    public String toSystem(ConsentProvisionType code) {
      return code.getSystem();
      }
    }

    public enum ConsentDataMeaning {
        /**
         * The consent applies directly to the instance of the resource
         */
        INSTANCE, 
        /**
         * The consent applies directly to the instance of the resource and instances it refers to
         */
        RELATED, 
        /**
         * The consent applies directly to the instance of the resource and instances that refer to it
         */
        DEPENDENTS, 
        /**
         * The consent applies to instances of resources that are authored by
         */
        AUTHOREDBY, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ConsentDataMeaning fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("instance".equals(codeString))
          return INSTANCE;
        if ("related".equals(codeString))
          return RELATED;
        if ("dependents".equals(codeString))
          return DEPENDENTS;
        if ("authoredby".equals(codeString))
          return AUTHOREDBY;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ConsentDataMeaning code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case INSTANCE: return "instance";
            case RELATED: return "related";
            case DEPENDENTS: return "dependents";
            case AUTHOREDBY: return "authoredby";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case INSTANCE: return "http://hl7.org/fhir/consent-data-meaning";
            case RELATED: return "http://hl7.org/fhir/consent-data-meaning";
            case DEPENDENTS: return "http://hl7.org/fhir/consent-data-meaning";
            case AUTHOREDBY: return "http://hl7.org/fhir/consent-data-meaning";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case INSTANCE: return "The consent applies directly to the instance of the resource";
            case RELATED: return "The consent applies directly to the instance of the resource and instances it refers to";
            case DEPENDENTS: return "The consent applies directly to the instance of the resource and instances that refer to it";
            case AUTHOREDBY: return "The consent applies to instances of resources that are authored by";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INSTANCE: return "Instance";
            case RELATED: return "Related";
            case DEPENDENTS: return "Dependents";
            case AUTHOREDBY: return "AuthoredBy";
            default: return "?";
          }
        }
    }

  public static class ConsentDataMeaningEnumFactory implements EnumFactory<ConsentDataMeaning> {
    public ConsentDataMeaning fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("instance".equals(codeString))
          return ConsentDataMeaning.INSTANCE;
        if ("related".equals(codeString))
          return ConsentDataMeaning.RELATED;
        if ("dependents".equals(codeString))
          return ConsentDataMeaning.DEPENDENTS;
        if ("authoredby".equals(codeString))
          return ConsentDataMeaning.AUTHOREDBY;
        throw new IllegalArgumentException("Unknown ConsentDataMeaning code '"+codeString+"'");
        }
        public Enumeration<ConsentDataMeaning> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ConsentDataMeaning>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("instance".equals(codeString))
          return new Enumeration<ConsentDataMeaning>(this, ConsentDataMeaning.INSTANCE);
        if ("related".equals(codeString))
          return new Enumeration<ConsentDataMeaning>(this, ConsentDataMeaning.RELATED);
        if ("dependents".equals(codeString))
          return new Enumeration<ConsentDataMeaning>(this, ConsentDataMeaning.DEPENDENTS);
        if ("authoredby".equals(codeString))
          return new Enumeration<ConsentDataMeaning>(this, ConsentDataMeaning.AUTHOREDBY);
        throw new FHIRException("Unknown ConsentDataMeaning code '"+codeString+"'");
        }
    public String toCode(ConsentDataMeaning code) {
      if (code == ConsentDataMeaning.INSTANCE)
        return "instance";
      if (code == ConsentDataMeaning.RELATED)
        return "related";
      if (code == ConsentDataMeaning.DEPENDENTS)
        return "dependents";
      if (code == ConsentDataMeaning.AUTHOREDBY)
        return "authoredby";
      return "?";
      }
    public String toSystem(ConsentDataMeaning code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class ConsentPolicyComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Entity or Organization having regulatory jurisdiction or accountability for  enforcing policies pertaining to Consent Directives.
         */
        @Child(name = "authority", type = {UriType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Enforcement source for policy", formalDefinition="Entity or Organization having regulatory jurisdiction or accountability for  enforcing policies pertaining to Consent Directives." )
        protected UriType authority;

        /**
         * The references to the policies that are included in this consent scope. Policies may be organizational, but are often defined jurisdictionally, or in law.
         */
        @Child(name = "uri", type = {UriType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Specific policy covered by this consent", formalDefinition="The references to the policies that are included in this consent scope. Policies may be organizational, but are often defined jurisdictionally, or in law." )
        protected UriType uri;

        private static final long serialVersionUID = 672275705L;

    /**
     * Constructor
     */
      public ConsentPolicyComponent() {
        super();
      }

        /**
         * @return {@link #authority} (Entity or Organization having regulatory jurisdiction or accountability for  enforcing policies pertaining to Consent Directives.). This is the underlying object with id, value and extensions. The accessor "getAuthority" gives direct access to the value
         */
        public UriType getAuthorityElement() { 
          if (this.authority == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConsentPolicyComponent.authority");
            else if (Configuration.doAutoCreate())
              this.authority = new UriType(); // bb
          return this.authority;
        }

        public boolean hasAuthorityElement() { 
          return this.authority != null && !this.authority.isEmpty();
        }

        public boolean hasAuthority() { 
          return this.authority != null && !this.authority.isEmpty();
        }

        /**
         * @param value {@link #authority} (Entity or Organization having regulatory jurisdiction or accountability for  enforcing policies pertaining to Consent Directives.). This is the underlying object with id, value and extensions. The accessor "getAuthority" gives direct access to the value
         */
        public ConsentPolicyComponent setAuthorityElement(UriType value) { 
          this.authority = value;
          return this;
        }

        /**
         * @return Entity or Organization having regulatory jurisdiction or accountability for  enforcing policies pertaining to Consent Directives.
         */
        public String getAuthority() { 
          return this.authority == null ? null : this.authority.getValue();
        }

        /**
         * @param value Entity or Organization having regulatory jurisdiction or accountability for  enforcing policies pertaining to Consent Directives.
         */
        public ConsentPolicyComponent setAuthority(String value) { 
          if (Utilities.noString(value))
            this.authority = null;
          else {
            if (this.authority == null)
              this.authority = new UriType();
            this.authority.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #uri} (The references to the policies that are included in this consent scope. Policies may be organizational, but are often defined jurisdictionally, or in law.). This is the underlying object with id, value and extensions. The accessor "getUri" gives direct access to the value
         */
        public UriType getUriElement() { 
          if (this.uri == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConsentPolicyComponent.uri");
            else if (Configuration.doAutoCreate())
              this.uri = new UriType(); // bb
          return this.uri;
        }

        public boolean hasUriElement() { 
          return this.uri != null && !this.uri.isEmpty();
        }

        public boolean hasUri() { 
          return this.uri != null && !this.uri.isEmpty();
        }

        /**
         * @param value {@link #uri} (The references to the policies that are included in this consent scope. Policies may be organizational, but are often defined jurisdictionally, or in law.). This is the underlying object with id, value and extensions. The accessor "getUri" gives direct access to the value
         */
        public ConsentPolicyComponent setUriElement(UriType value) { 
          this.uri = value;
          return this;
        }

        /**
         * @return The references to the policies that are included in this consent scope. Policies may be organizational, but are often defined jurisdictionally, or in law.
         */
        public String getUri() { 
          return this.uri == null ? null : this.uri.getValue();
        }

        /**
         * @param value The references to the policies that are included in this consent scope. Policies may be organizational, but are often defined jurisdictionally, or in law.
         */
        public ConsentPolicyComponent setUri(String value) { 
          if (Utilities.noString(value))
            this.uri = null;
          else {
            if (this.uri == null)
              this.uri = new UriType();
            this.uri.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("authority", "uri", "Entity or Organization having regulatory jurisdiction or accountability for  enforcing policies pertaining to Consent Directives.", 0, 1, authority));
          children.add(new Property("uri", "uri", "The references to the policies that are included in this consent scope. Policies may be organizational, but are often defined jurisdictionally, or in law.", 0, 1, uri));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 1475610435: /*authority*/  return new Property("authority", "uri", "Entity or Organization having regulatory jurisdiction or accountability for  enforcing policies pertaining to Consent Directives.", 0, 1, authority);
          case 116076: /*uri*/  return new Property("uri", "uri", "The references to the policies that are included in this consent scope. Policies may be organizational, but are often defined jurisdictionally, or in law.", 0, 1, uri);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1475610435: /*authority*/ return this.authority == null ? new Base[0] : new Base[] {this.authority}; // UriType
        case 116076: /*uri*/ return this.uri == null ? new Base[0] : new Base[] {this.uri}; // UriType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1475610435: // authority
          this.authority = castToUri(value); // UriType
          return value;
        case 116076: // uri
          this.uri = castToUri(value); // UriType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("authority")) {
          this.authority = castToUri(value); // UriType
        } else if (name.equals("uri")) {
          this.uri = castToUri(value); // UriType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1475610435:  return getAuthorityElement();
        case 116076:  return getUriElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1475610435: /*authority*/ return new String[] {"uri"};
        case 116076: /*uri*/ return new String[] {"uri"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("authority")) {
          throw new FHIRException("Cannot call addChild on a primitive type Consent.authority");
        }
        else if (name.equals("uri")) {
          throw new FHIRException("Cannot call addChild on a primitive type Consent.uri");
        }
        else
          return super.addChild(name);
      }

      public ConsentPolicyComponent copy() {
        ConsentPolicyComponent dst = new ConsentPolicyComponent();
        copyValues(dst);
        dst.authority = authority == null ? null : authority.copy();
        dst.uri = uri == null ? null : uri.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ConsentPolicyComponent))
          return false;
        ConsentPolicyComponent o = (ConsentPolicyComponent) other_;
        return compareDeep(authority, o.authority, true) && compareDeep(uri, o.uri, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ConsentPolicyComponent))
          return false;
        ConsentPolicyComponent o = (ConsentPolicyComponent) other_;
        return compareValues(authority, o.authority, true) && compareValues(uri, o.uri, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(authority, uri);
      }

  public String fhirType() {
    return "Consent.policy";

  }

  }

    @Block()
    public static class ConsentVerificationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Has the instruction been verified.
         */
        @Child(name = "verified", type = {BooleanType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Has been verified", formalDefinition="Has the instruction been verified." )
        protected BooleanType verified;

        /**
         * Who verified the instruction (Patient, Relative or other Authorized Person).
         */
        @Child(name = "verifiedWith", type = {Patient.class, RelatedPerson.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Person who verified", formalDefinition="Who verified the instruction (Patient, Relative or other Authorized Person)." )
        protected Reference verifiedWith;

        /**
         * The actual object that is the target of the reference (Who verified the instruction (Patient, Relative or other Authorized Person).)
         */
        protected Resource verifiedWithTarget;

        /**
         * Date verification was collected.
         */
        @Child(name = "verificationDate", type = {DateTimeType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="When consent verified", formalDefinition="Date verification was collected." )
        protected DateTimeType verificationDate;

        private static final long serialVersionUID = 1305161458L;

    /**
     * Constructor
     */
      public ConsentVerificationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ConsentVerificationComponent(BooleanType verified) {
        super();
        this.verified = verified;
      }

        /**
         * @return {@link #verified} (Has the instruction been verified.). This is the underlying object with id, value and extensions. The accessor "getVerified" gives direct access to the value
         */
        public BooleanType getVerifiedElement() { 
          if (this.verified == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConsentVerificationComponent.verified");
            else if (Configuration.doAutoCreate())
              this.verified = new BooleanType(); // bb
          return this.verified;
        }

        public boolean hasVerifiedElement() { 
          return this.verified != null && !this.verified.isEmpty();
        }

        public boolean hasVerified() { 
          return this.verified != null && !this.verified.isEmpty();
        }

        /**
         * @param value {@link #verified} (Has the instruction been verified.). This is the underlying object with id, value and extensions. The accessor "getVerified" gives direct access to the value
         */
        public ConsentVerificationComponent setVerifiedElement(BooleanType value) { 
          this.verified = value;
          return this;
        }

        /**
         * @return Has the instruction been verified.
         */
        public boolean getVerified() { 
          return this.verified == null || this.verified.isEmpty() ? false : this.verified.getValue();
        }

        /**
         * @param value Has the instruction been verified.
         */
        public ConsentVerificationComponent setVerified(boolean value) { 
            if (this.verified == null)
              this.verified = new BooleanType();
            this.verified.setValue(value);
          return this;
        }

        /**
         * @return {@link #verifiedWith} (Who verified the instruction (Patient, Relative or other Authorized Person).)
         */
        public Reference getVerifiedWith() { 
          if (this.verifiedWith == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConsentVerificationComponent.verifiedWith");
            else if (Configuration.doAutoCreate())
              this.verifiedWith = new Reference(); // cc
          return this.verifiedWith;
        }

        public boolean hasVerifiedWith() { 
          return this.verifiedWith != null && !this.verifiedWith.isEmpty();
        }

        /**
         * @param value {@link #verifiedWith} (Who verified the instruction (Patient, Relative or other Authorized Person).)
         */
        public ConsentVerificationComponent setVerifiedWith(Reference value) { 
          this.verifiedWith = value;
          return this;
        }

        /**
         * @return {@link #verifiedWith} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Who verified the instruction (Patient, Relative or other Authorized Person).)
         */
        public Resource getVerifiedWithTarget() { 
          return this.verifiedWithTarget;
        }

        /**
         * @param value {@link #verifiedWith} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Who verified the instruction (Patient, Relative or other Authorized Person).)
         */
        public ConsentVerificationComponent setVerifiedWithTarget(Resource value) { 
          this.verifiedWithTarget = value;
          return this;
        }

        /**
         * @return {@link #verificationDate} (Date verification was collected.). This is the underlying object with id, value and extensions. The accessor "getVerificationDate" gives direct access to the value
         */
        public DateTimeType getVerificationDateElement() { 
          if (this.verificationDate == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConsentVerificationComponent.verificationDate");
            else if (Configuration.doAutoCreate())
              this.verificationDate = new DateTimeType(); // bb
          return this.verificationDate;
        }

        public boolean hasVerificationDateElement() { 
          return this.verificationDate != null && !this.verificationDate.isEmpty();
        }

        public boolean hasVerificationDate() { 
          return this.verificationDate != null && !this.verificationDate.isEmpty();
        }

        /**
         * @param value {@link #verificationDate} (Date verification was collected.). This is the underlying object with id, value and extensions. The accessor "getVerificationDate" gives direct access to the value
         */
        public ConsentVerificationComponent setVerificationDateElement(DateTimeType value) { 
          this.verificationDate = value;
          return this;
        }

        /**
         * @return Date verification was collected.
         */
        public Date getVerificationDate() { 
          return this.verificationDate == null ? null : this.verificationDate.getValue();
        }

        /**
         * @param value Date verification was collected.
         */
        public ConsentVerificationComponent setVerificationDate(Date value) { 
          if (value == null)
            this.verificationDate = null;
          else {
            if (this.verificationDate == null)
              this.verificationDate = new DateTimeType();
            this.verificationDate.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("verified", "boolean", "Has the instruction been verified.", 0, 1, verified));
          children.add(new Property("verifiedWith", "Reference(Patient|RelatedPerson)", "Who verified the instruction (Patient, Relative or other Authorized Person).", 0, 1, verifiedWith));
          children.add(new Property("verificationDate", "dateTime", "Date verification was collected.", 0, 1, verificationDate));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1994383672: /*verified*/  return new Property("verified", "boolean", "Has the instruction been verified.", 0, 1, verified);
          case -1425236050: /*verifiedWith*/  return new Property("verifiedWith", "Reference(Patient|RelatedPerson)", "Who verified the instruction (Patient, Relative or other Authorized Person).", 0, 1, verifiedWith);
          case 642233449: /*verificationDate*/  return new Property("verificationDate", "dateTime", "Date verification was collected.", 0, 1, verificationDate);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1994383672: /*verified*/ return this.verified == null ? new Base[0] : new Base[] {this.verified}; // BooleanType
        case -1425236050: /*verifiedWith*/ return this.verifiedWith == null ? new Base[0] : new Base[] {this.verifiedWith}; // Reference
        case 642233449: /*verificationDate*/ return this.verificationDate == null ? new Base[0] : new Base[] {this.verificationDate}; // DateTimeType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1994383672: // verified
          this.verified = castToBoolean(value); // BooleanType
          return value;
        case -1425236050: // verifiedWith
          this.verifiedWith = castToReference(value); // Reference
          return value;
        case 642233449: // verificationDate
          this.verificationDate = castToDateTime(value); // DateTimeType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("verified")) {
          this.verified = castToBoolean(value); // BooleanType
        } else if (name.equals("verifiedWith")) {
          this.verifiedWith = castToReference(value); // Reference
        } else if (name.equals("verificationDate")) {
          this.verificationDate = castToDateTime(value); // DateTimeType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1994383672:  return getVerifiedElement();
        case -1425236050:  return getVerifiedWith(); 
        case 642233449:  return getVerificationDateElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1994383672: /*verified*/ return new String[] {"boolean"};
        case -1425236050: /*verifiedWith*/ return new String[] {"Reference"};
        case 642233449: /*verificationDate*/ return new String[] {"dateTime"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("verified")) {
          throw new FHIRException("Cannot call addChild on a primitive type Consent.verified");
        }
        else if (name.equals("verifiedWith")) {
          this.verifiedWith = new Reference();
          return this.verifiedWith;
        }
        else if (name.equals("verificationDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type Consent.verificationDate");
        }
        else
          return super.addChild(name);
      }

      public ConsentVerificationComponent copy() {
        ConsentVerificationComponent dst = new ConsentVerificationComponent();
        copyValues(dst);
        dst.verified = verified == null ? null : verified.copy();
        dst.verifiedWith = verifiedWith == null ? null : verifiedWith.copy();
        dst.verificationDate = verificationDate == null ? null : verificationDate.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ConsentVerificationComponent))
          return false;
        ConsentVerificationComponent o = (ConsentVerificationComponent) other_;
        return compareDeep(verified, o.verified, true) && compareDeep(verifiedWith, o.verifiedWith, true)
           && compareDeep(verificationDate, o.verificationDate, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ConsentVerificationComponent))
          return false;
        ConsentVerificationComponent o = (ConsentVerificationComponent) other_;
        return compareValues(verified, o.verified, true) && compareValues(verificationDate, o.verificationDate, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(verified, verifiedWith, verificationDate
          );
      }

  public String fhirType() {
    return "Consent.verification";

  }

  }

    @Block()
    public static class provisionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Action  to take - permit or deny - when the rule conditions are met.  Not permitted in root rule, required in all nested rules.
         */
        @Child(name = "type", type = {CodeType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="deny | permit", formalDefinition="Action  to take - permit or deny - when the rule conditions are met.  Not permitted in root rule, required in all nested rules." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/consent-provision-type")
        protected Enumeration<ConsentProvisionType> type;

        /**
         * The timeframe in this rule is valid.
         */
        @Child(name = "period", type = {Period.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Timeframe for this rule", formalDefinition="The timeframe in this rule is valid." )
        protected Period period;

        /**
         * Who or what is controlled by this rule. Use group to identify a set of actors by some property they share (e.g. 'admitting officers').
         */
        @Child(name = "actor", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Who|what controlled by this rule (or group, by role)", formalDefinition="Who or what is controlled by this rule. Use group to identify a set of actors by some property they share (e.g. 'admitting officers')." )
        protected List<provisionActorComponent> actor;

        /**
         * Actions controlled by this Rule.
         */
        @Child(name = "action", type = {CodeableConcept.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Actions controlled by this rule", formalDefinition="Actions controlled by this Rule." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/consent-action")
        protected List<CodeableConcept> action;

        /**
         * A security label, comprised of 0..* security label fields (Privacy tags), which define which resources are controlled by this exception.
         */
        @Child(name = "securityLabel", type = {Coding.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Security Labels that define affected resources", formalDefinition="A security label, comprised of 0..* security label fields (Privacy tags), which define which resources are controlled by this exception." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/security-labels")
        protected List<Coding> securityLabel;

        /**
         * The context of the activities a user is taking - why the user is accessing the data - that are controlled by this rule.
         */
        @Child(name = "purpose", type = {Coding.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Context of activities covered by this rule", formalDefinition="The context of the activities a user is taking - why the user is accessing the data - that are controlled by this rule." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/v3-PurposeOfUse")
        protected List<Coding> purpose;

        /**
         * The class of information covered by this rule. The type can be a FHIR resource type, a profile on a type, or a CDA document, or some other type that indicates what sort of information the consent relates to.
         */
        @Child(name = "class", type = {Coding.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="e.g. Resource Type, Profile, CDA, etc.", formalDefinition="The class of information covered by this rule. The type can be a FHIR resource type, a profile on a type, or a CDA document, or some other type that indicates what sort of information the consent relates to." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/consent-content-class")
        protected List<Coding> class_;

        /**
         * If this code is found in an instance, then the rule applies.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="e.g. LOINC or SNOMED CT code, etc. in the content", formalDefinition="If this code is found in an instance, then the rule applies." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/consent-content-code")
        protected List<CodeableConcept> code;

        /**
         * Clinical or Operational Relevant period of time that bounds the data controlled by this rule.
         */
        @Child(name = "dataPeriod", type = {Period.class}, order=9, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Timeframe for data controlled by this rule", formalDefinition="Clinical or Operational Relevant period of time that bounds the data controlled by this rule." )
        protected Period dataPeriod;

        /**
         * The resources controlled by this rule if specific resources are referenced.
         */
        @Child(name = "data", type = {}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Data controlled by this rule", formalDefinition="The resources controlled by this rule if specific resources are referenced." )
        protected List<provisionDataComponent> data;

        /**
         * Rules which provide exceptions to the base rule or subrules.
         */
        @Child(name = "provision", type = {provisionComponent.class}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Nested Exception Rules", formalDefinition="Rules which provide exceptions to the base rule or subrules." )
        protected List<provisionComponent> provision;

        private static final long serialVersionUID = -1280172451L;

    /**
     * Constructor
     */
      public provisionComponent() {
        super();
      }

        /**
         * @return {@link #type} (Action  to take - permit or deny - when the rule conditions are met.  Not permitted in root rule, required in all nested rules.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<ConsentProvisionType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create provisionComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Enumeration<ConsentProvisionType>(new ConsentProvisionTypeEnumFactory()); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Action  to take - permit or deny - when the rule conditions are met.  Not permitted in root rule, required in all nested rules.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public provisionComponent setTypeElement(Enumeration<ConsentProvisionType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return Action  to take - permit or deny - when the rule conditions are met.  Not permitted in root rule, required in all nested rules.
         */
        public ConsentProvisionType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value Action  to take - permit or deny - when the rule conditions are met.  Not permitted in root rule, required in all nested rules.
         */
        public provisionComponent setType(ConsentProvisionType value) { 
          if (value == null)
            this.type = null;
          else {
            if (this.type == null)
              this.type = new Enumeration<ConsentProvisionType>(new ConsentProvisionTypeEnumFactory());
            this.type.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #period} (The timeframe in this rule is valid.)
         */
        public Period getPeriod() { 
          if (this.period == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create provisionComponent.period");
            else if (Configuration.doAutoCreate())
              this.period = new Period(); // cc
          return this.period;
        }

        public boolean hasPeriod() { 
          return this.period != null && !this.period.isEmpty();
        }

        /**
         * @param value {@link #period} (The timeframe in this rule is valid.)
         */
        public provisionComponent setPeriod(Period value) { 
          this.period = value;
          return this;
        }

        /**
         * @return {@link #actor} (Who or what is controlled by this rule. Use group to identify a set of actors by some property they share (e.g. 'admitting officers').)
         */
        public List<provisionActorComponent> getActor() { 
          if (this.actor == null)
            this.actor = new ArrayList<provisionActorComponent>();
          return this.actor;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public provisionComponent setActor(List<provisionActorComponent> theActor) { 
          this.actor = theActor;
          return this;
        }

        public boolean hasActor() { 
          if (this.actor == null)
            return false;
          for (provisionActorComponent item : this.actor)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public provisionActorComponent addActor() { //3
          provisionActorComponent t = new provisionActorComponent();
          if (this.actor == null)
            this.actor = new ArrayList<provisionActorComponent>();
          this.actor.add(t);
          return t;
        }

        public provisionComponent addActor(provisionActorComponent t) { //3
          if (t == null)
            return this;
          if (this.actor == null)
            this.actor = new ArrayList<provisionActorComponent>();
          this.actor.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #actor}, creating it if it does not already exist
         */
        public provisionActorComponent getActorFirstRep() { 
          if (getActor().isEmpty()) {
            addActor();
          }
          return getActor().get(0);
        }

        /**
         * @return {@link #action} (Actions controlled by this Rule.)
         */
        public List<CodeableConcept> getAction() { 
          if (this.action == null)
            this.action = new ArrayList<CodeableConcept>();
          return this.action;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public provisionComponent setAction(List<CodeableConcept> theAction) { 
          this.action = theAction;
          return this;
        }

        public boolean hasAction() { 
          if (this.action == null)
            return false;
          for (CodeableConcept item : this.action)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addAction() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.action == null)
            this.action = new ArrayList<CodeableConcept>();
          this.action.add(t);
          return t;
        }

        public provisionComponent addAction(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.action == null)
            this.action = new ArrayList<CodeableConcept>();
          this.action.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #action}, creating it if it does not already exist
         */
        public CodeableConcept getActionFirstRep() { 
          if (getAction().isEmpty()) {
            addAction();
          }
          return getAction().get(0);
        }

        /**
         * @return {@link #securityLabel} (A security label, comprised of 0..* security label fields (Privacy tags), which define which resources are controlled by this exception.)
         */
        public List<Coding> getSecurityLabel() { 
          if (this.securityLabel == null)
            this.securityLabel = new ArrayList<Coding>();
          return this.securityLabel;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public provisionComponent setSecurityLabel(List<Coding> theSecurityLabel) { 
          this.securityLabel = theSecurityLabel;
          return this;
        }

        public boolean hasSecurityLabel() { 
          if (this.securityLabel == null)
            return false;
          for (Coding item : this.securityLabel)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Coding addSecurityLabel() { //3
          Coding t = new Coding();
          if (this.securityLabel == null)
            this.securityLabel = new ArrayList<Coding>();
          this.securityLabel.add(t);
          return t;
        }

        public provisionComponent addSecurityLabel(Coding t) { //3
          if (t == null)
            return this;
          if (this.securityLabel == null)
            this.securityLabel = new ArrayList<Coding>();
          this.securityLabel.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #securityLabel}, creating it if it does not already exist
         */
        public Coding getSecurityLabelFirstRep() { 
          if (getSecurityLabel().isEmpty()) {
            addSecurityLabel();
          }
          return getSecurityLabel().get(0);
        }

        /**
         * @return {@link #purpose} (The context of the activities a user is taking - why the user is accessing the data - that are controlled by this rule.)
         */
        public List<Coding> getPurpose() { 
          if (this.purpose == null)
            this.purpose = new ArrayList<Coding>();
          return this.purpose;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public provisionComponent setPurpose(List<Coding> thePurpose) { 
          this.purpose = thePurpose;
          return this;
        }

        public boolean hasPurpose() { 
          if (this.purpose == null)
            return false;
          for (Coding item : this.purpose)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Coding addPurpose() { //3
          Coding t = new Coding();
          if (this.purpose == null)
            this.purpose = new ArrayList<Coding>();
          this.purpose.add(t);
          return t;
        }

        public provisionComponent addPurpose(Coding t) { //3
          if (t == null)
            return this;
          if (this.purpose == null)
            this.purpose = new ArrayList<Coding>();
          this.purpose.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #purpose}, creating it if it does not already exist
         */
        public Coding getPurposeFirstRep() { 
          if (getPurpose().isEmpty()) {
            addPurpose();
          }
          return getPurpose().get(0);
        }

        /**
         * @return {@link #class_} (The class of information covered by this rule. The type can be a FHIR resource type, a profile on a type, or a CDA document, or some other type that indicates what sort of information the consent relates to.)
         */
        public List<Coding> getClass_() { 
          if (this.class_ == null)
            this.class_ = new ArrayList<Coding>();
          return this.class_;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public provisionComponent setClass_(List<Coding> theClass_) { 
          this.class_ = theClass_;
          return this;
        }

        public boolean hasClass_() { 
          if (this.class_ == null)
            return false;
          for (Coding item : this.class_)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Coding addClass_() { //3
          Coding t = new Coding();
          if (this.class_ == null)
            this.class_ = new ArrayList<Coding>();
          this.class_.add(t);
          return t;
        }

        public provisionComponent addClass_(Coding t) { //3
          if (t == null)
            return this;
          if (this.class_ == null)
            this.class_ = new ArrayList<Coding>();
          this.class_.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #class_}, creating it if it does not already exist
         */
        public Coding getClass_FirstRep() { 
          if (getClass_().isEmpty()) {
            addClass_();
          }
          return getClass_().get(0);
        }

        /**
         * @return {@link #code} (If this code is found in an instance, then the rule applies.)
         */
        public List<CodeableConcept> getCode() { 
          if (this.code == null)
            this.code = new ArrayList<CodeableConcept>();
          return this.code;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public provisionComponent setCode(List<CodeableConcept> theCode) { 
          this.code = theCode;
          return this;
        }

        public boolean hasCode() { 
          if (this.code == null)
            return false;
          for (CodeableConcept item : this.code)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addCode() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.code == null)
            this.code = new ArrayList<CodeableConcept>();
          this.code.add(t);
          return t;
        }

        public provisionComponent addCode(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.code == null)
            this.code = new ArrayList<CodeableConcept>();
          this.code.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #code}, creating it if it does not already exist
         */
        public CodeableConcept getCodeFirstRep() { 
          if (getCode().isEmpty()) {
            addCode();
          }
          return getCode().get(0);
        }

        /**
         * @return {@link #dataPeriod} (Clinical or Operational Relevant period of time that bounds the data controlled by this rule.)
         */
        public Period getDataPeriod() { 
          if (this.dataPeriod == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create provisionComponent.dataPeriod");
            else if (Configuration.doAutoCreate())
              this.dataPeriod = new Period(); // cc
          return this.dataPeriod;
        }

        public boolean hasDataPeriod() { 
          return this.dataPeriod != null && !this.dataPeriod.isEmpty();
        }

        /**
         * @param value {@link #dataPeriod} (Clinical or Operational Relevant period of time that bounds the data controlled by this rule.)
         */
        public provisionComponent setDataPeriod(Period value) { 
          this.dataPeriod = value;
          return this;
        }

        /**
         * @return {@link #data} (The resources controlled by this rule if specific resources are referenced.)
         */
        public List<provisionDataComponent> getData() { 
          if (this.data == null)
            this.data = new ArrayList<provisionDataComponent>();
          return this.data;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public provisionComponent setData(List<provisionDataComponent> theData) { 
          this.data = theData;
          return this;
        }

        public boolean hasData() { 
          if (this.data == null)
            return false;
          for (provisionDataComponent item : this.data)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public provisionDataComponent addData() { //3
          provisionDataComponent t = new provisionDataComponent();
          if (this.data == null)
            this.data = new ArrayList<provisionDataComponent>();
          this.data.add(t);
          return t;
        }

        public provisionComponent addData(provisionDataComponent t) { //3
          if (t == null)
            return this;
          if (this.data == null)
            this.data = new ArrayList<provisionDataComponent>();
          this.data.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #data}, creating it if it does not already exist
         */
        public provisionDataComponent getDataFirstRep() { 
          if (getData().isEmpty()) {
            addData();
          }
          return getData().get(0);
        }

        /**
         * @return {@link #provision} (Rules which provide exceptions to the base rule or subrules.)
         */
        public List<provisionComponent> getProvision() { 
          if (this.provision == null)
            this.provision = new ArrayList<provisionComponent>();
          return this.provision;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public provisionComponent setProvision(List<provisionComponent> theProvision) { 
          this.provision = theProvision;
          return this;
        }

        public boolean hasProvision() { 
          if (this.provision == null)
            return false;
          for (provisionComponent item : this.provision)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public provisionComponent addProvision() { //3
          provisionComponent t = new provisionComponent();
          if (this.provision == null)
            this.provision = new ArrayList<provisionComponent>();
          this.provision.add(t);
          return t;
        }

        public provisionComponent addProvision(provisionComponent t) { //3
          if (t == null)
            return this;
          if (this.provision == null)
            this.provision = new ArrayList<provisionComponent>();
          this.provision.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #provision}, creating it if it does not already exist
         */
        public provisionComponent getProvisionFirstRep() { 
          if (getProvision().isEmpty()) {
            addProvision();
          }
          return getProvision().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("type", "code", "Action  to take - permit or deny - when the rule conditions are met.  Not permitted in root rule, required in all nested rules.", 0, 1, type));
          children.add(new Property("period", "Period", "The timeframe in this rule is valid.", 0, 1, period));
          children.add(new Property("actor", "", "Who or what is controlled by this rule. Use group to identify a set of actors by some property they share (e.g. 'admitting officers').", 0, java.lang.Integer.MAX_VALUE, actor));
          children.add(new Property("action", "CodeableConcept", "Actions controlled by this Rule.", 0, java.lang.Integer.MAX_VALUE, action));
          children.add(new Property("securityLabel", "Coding", "A security label, comprised of 0..* security label fields (Privacy tags), which define which resources are controlled by this exception.", 0, java.lang.Integer.MAX_VALUE, securityLabel));
          children.add(new Property("purpose", "Coding", "The context of the activities a user is taking - why the user is accessing the data - that are controlled by this rule.", 0, java.lang.Integer.MAX_VALUE, purpose));
          children.add(new Property("class", "Coding", "The class of information covered by this rule. The type can be a FHIR resource type, a profile on a type, or a CDA document, or some other type that indicates what sort of information the consent relates to.", 0, java.lang.Integer.MAX_VALUE, class_));
          children.add(new Property("code", "CodeableConcept", "If this code is found in an instance, then the rule applies.", 0, java.lang.Integer.MAX_VALUE, code));
          children.add(new Property("dataPeriod", "Period", "Clinical or Operational Relevant period of time that bounds the data controlled by this rule.", 0, 1, dataPeriod));
          children.add(new Property("data", "", "The resources controlled by this rule if specific resources are referenced.", 0, java.lang.Integer.MAX_VALUE, data));
          children.add(new Property("provision", "@Consent.provision", "Rules which provide exceptions to the base rule or subrules.", 0, java.lang.Integer.MAX_VALUE, provision));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3575610: /*type*/  return new Property("type", "code", "Action  to take - permit or deny - when the rule conditions are met.  Not permitted in root rule, required in all nested rules.", 0, 1, type);
          case -991726143: /*period*/  return new Property("period", "Period", "The timeframe in this rule is valid.", 0, 1, period);
          case 92645877: /*actor*/  return new Property("actor", "", "Who or what is controlled by this rule. Use group to identify a set of actors by some property they share (e.g. 'admitting officers').", 0, java.lang.Integer.MAX_VALUE, actor);
          case -1422950858: /*action*/  return new Property("action", "CodeableConcept", "Actions controlled by this Rule.", 0, java.lang.Integer.MAX_VALUE, action);
          case -722296940: /*securityLabel*/  return new Property("securityLabel", "Coding", "A security label, comprised of 0..* security label fields (Privacy tags), which define which resources are controlled by this exception.", 0, java.lang.Integer.MAX_VALUE, securityLabel);
          case -220463842: /*purpose*/  return new Property("purpose", "Coding", "The context of the activities a user is taking - why the user is accessing the data - that are controlled by this rule.", 0, java.lang.Integer.MAX_VALUE, purpose);
          case 94742904: /*class*/  return new Property("class", "Coding", "The class of information covered by this rule. The type can be a FHIR resource type, a profile on a type, or a CDA document, or some other type that indicates what sort of information the consent relates to.", 0, java.lang.Integer.MAX_VALUE, class_);
          case 3059181: /*code*/  return new Property("code", "CodeableConcept", "If this code is found in an instance, then the rule applies.", 0, java.lang.Integer.MAX_VALUE, code);
          case 1177250315: /*dataPeriod*/  return new Property("dataPeriod", "Period", "Clinical or Operational Relevant period of time that bounds the data controlled by this rule.", 0, 1, dataPeriod);
          case 3076010: /*data*/  return new Property("data", "", "The resources controlled by this rule if specific resources are referenced.", 0, java.lang.Integer.MAX_VALUE, data);
          case -547120939: /*provision*/  return new Property("provision", "@Consent.provision", "Rules which provide exceptions to the base rule or subrules.", 0, java.lang.Integer.MAX_VALUE, provision);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<ConsentProvisionType>
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // Period
        case 92645877: /*actor*/ return this.actor == null ? new Base[0] : this.actor.toArray(new Base[this.actor.size()]); // provisionActorComponent
        case -1422950858: /*action*/ return this.action == null ? new Base[0] : this.action.toArray(new Base[this.action.size()]); // CodeableConcept
        case -722296940: /*securityLabel*/ return this.securityLabel == null ? new Base[0] : this.securityLabel.toArray(new Base[this.securityLabel.size()]); // Coding
        case -220463842: /*purpose*/ return this.purpose == null ? new Base[0] : this.purpose.toArray(new Base[this.purpose.size()]); // Coding
        case 94742904: /*class*/ return this.class_ == null ? new Base[0] : this.class_.toArray(new Base[this.class_.size()]); // Coding
        case 3059181: /*code*/ return this.code == null ? new Base[0] : this.code.toArray(new Base[this.code.size()]); // CodeableConcept
        case 1177250315: /*dataPeriod*/ return this.dataPeriod == null ? new Base[0] : new Base[] {this.dataPeriod}; // Period
        case 3076010: /*data*/ return this.data == null ? new Base[0] : this.data.toArray(new Base[this.data.size()]); // provisionDataComponent
        case -547120939: /*provision*/ return this.provision == null ? new Base[0] : this.provision.toArray(new Base[this.provision.size()]); // provisionComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          value = new ConsentProvisionTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<ConsentProvisionType>
          return value;
        case -991726143: // period
          this.period = castToPeriod(value); // Period
          return value;
        case 92645877: // actor
          this.getActor().add((provisionActorComponent) value); // provisionActorComponent
          return value;
        case -1422950858: // action
          this.getAction().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -722296940: // securityLabel
          this.getSecurityLabel().add(castToCoding(value)); // Coding
          return value;
        case -220463842: // purpose
          this.getPurpose().add(castToCoding(value)); // Coding
          return value;
        case 94742904: // class
          this.getClass_().add(castToCoding(value)); // Coding
          return value;
        case 3059181: // code
          this.getCode().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 1177250315: // dataPeriod
          this.dataPeriod = castToPeriod(value); // Period
          return value;
        case 3076010: // data
          this.getData().add((provisionDataComponent) value); // provisionDataComponent
          return value;
        case -547120939: // provision
          this.getProvision().add((provisionComponent) value); // provisionComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          value = new ConsentProvisionTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<ConsentProvisionType>
        } else if (name.equals("period")) {
          this.period = castToPeriod(value); // Period
        } else if (name.equals("actor")) {
          this.getActor().add((provisionActorComponent) value);
        } else if (name.equals("action")) {
          this.getAction().add(castToCodeableConcept(value));
        } else if (name.equals("securityLabel")) {
          this.getSecurityLabel().add(castToCoding(value));
        } else if (name.equals("purpose")) {
          this.getPurpose().add(castToCoding(value));
        } else if (name.equals("class")) {
          this.getClass_().add(castToCoding(value));
        } else if (name.equals("code")) {
          this.getCode().add(castToCodeableConcept(value));
        } else if (name.equals("dataPeriod")) {
          this.dataPeriod = castToPeriod(value); // Period
        } else if (name.equals("data")) {
          this.getData().add((provisionDataComponent) value);
        } else if (name.equals("provision")) {
          this.getProvision().add((provisionComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getTypeElement();
        case -991726143:  return getPeriod(); 
        case 92645877:  return addActor(); 
        case -1422950858:  return addAction(); 
        case -722296940:  return addSecurityLabel(); 
        case -220463842:  return addPurpose(); 
        case 94742904:  return addClass_(); 
        case 3059181:  return addCode(); 
        case 1177250315:  return getDataPeriod(); 
        case 3076010:  return addData(); 
        case -547120939:  return addProvision(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"code"};
        case -991726143: /*period*/ return new String[] {"Period"};
        case 92645877: /*actor*/ return new String[] {};
        case -1422950858: /*action*/ return new String[] {"CodeableConcept"};
        case -722296940: /*securityLabel*/ return new String[] {"Coding"};
        case -220463842: /*purpose*/ return new String[] {"Coding"};
        case 94742904: /*class*/ return new String[] {"Coding"};
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case 1177250315: /*dataPeriod*/ return new String[] {"Period"};
        case 3076010: /*data*/ return new String[] {};
        case -547120939: /*provision*/ return new String[] {"@Consent.provision"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type Consent.type");
        }
        else if (name.equals("period")) {
          this.period = new Period();
          return this.period;
        }
        else if (name.equals("actor")) {
          return addActor();
        }
        else if (name.equals("action")) {
          return addAction();
        }
        else if (name.equals("securityLabel")) {
          return addSecurityLabel();
        }
        else if (name.equals("purpose")) {
          return addPurpose();
        }
        else if (name.equals("class")) {
          return addClass_();
        }
        else if (name.equals("code")) {
          return addCode();
        }
        else if (name.equals("dataPeriod")) {
          this.dataPeriod = new Period();
          return this.dataPeriod;
        }
        else if (name.equals("data")) {
          return addData();
        }
        else if (name.equals("provision")) {
          return addProvision();
        }
        else
          return super.addChild(name);
      }

      public provisionComponent copy() {
        provisionComponent dst = new provisionComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.period = period == null ? null : period.copy();
        if (actor != null) {
          dst.actor = new ArrayList<provisionActorComponent>();
          for (provisionActorComponent i : actor)
            dst.actor.add(i.copy());
        };
        if (action != null) {
          dst.action = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : action)
            dst.action.add(i.copy());
        };
        if (securityLabel != null) {
          dst.securityLabel = new ArrayList<Coding>();
          for (Coding i : securityLabel)
            dst.securityLabel.add(i.copy());
        };
        if (purpose != null) {
          dst.purpose = new ArrayList<Coding>();
          for (Coding i : purpose)
            dst.purpose.add(i.copy());
        };
        if (class_ != null) {
          dst.class_ = new ArrayList<Coding>();
          for (Coding i : class_)
            dst.class_.add(i.copy());
        };
        if (code != null) {
          dst.code = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : code)
            dst.code.add(i.copy());
        };
        dst.dataPeriod = dataPeriod == null ? null : dataPeriod.copy();
        if (data != null) {
          dst.data = new ArrayList<provisionDataComponent>();
          for (provisionDataComponent i : data)
            dst.data.add(i.copy());
        };
        if (provision != null) {
          dst.provision = new ArrayList<provisionComponent>();
          for (provisionComponent i : provision)
            dst.provision.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof provisionComponent))
          return false;
        provisionComponent o = (provisionComponent) other_;
        return compareDeep(type, o.type, true) && compareDeep(period, o.period, true) && compareDeep(actor, o.actor, true)
           && compareDeep(action, o.action, true) && compareDeep(securityLabel, o.securityLabel, true) && compareDeep(purpose, o.purpose, true)
           && compareDeep(class_, o.class_, true) && compareDeep(code, o.code, true) && compareDeep(dataPeriod, o.dataPeriod, true)
           && compareDeep(data, o.data, true) && compareDeep(provision, o.provision, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof provisionComponent))
          return false;
        provisionComponent o = (provisionComponent) other_;
        return compareValues(type, o.type, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, period, actor, action
          , securityLabel, purpose, class_, code, dataPeriod, data, provision);
      }

  public String fhirType() {
    return "Consent.provision";

  }

  }

    @Block()
    public static class provisionActorComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * How the individual is involved in the resources content that is described in the exception.
         */
        @Child(name = "role", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="How the actor is involved", formalDefinition="How the individual is involved in the resources content that is described in the exception." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/security-role-type")
        protected CodeableConcept role;

        /**
         * The resource that identifies the actor. To identify actors by type, use group to identify a set of actors by some property they share (e.g. 'admitting officers').
         */
        @Child(name = "reference", type = {Device.class, Group.class, CareTeam.class, Organization.class, Patient.class, Practitioner.class, RelatedPerson.class, PractitionerRole.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Resource for the actor (or group, by role)", formalDefinition="The resource that identifies the actor. To identify actors by type, use group to identify a set of actors by some property they share (e.g. 'admitting officers')." )
        protected Reference reference;

        /**
         * The actual object that is the target of the reference (The resource that identifies the actor. To identify actors by type, use group to identify a set of actors by some property they share (e.g. 'admitting officers').)
         */
        protected Resource referenceTarget;

        private static final long serialVersionUID = 1152919415L;

    /**
     * Constructor
     */
      public provisionActorComponent() {
        super();
      }

    /**
     * Constructor
     */
      public provisionActorComponent(CodeableConcept role, Reference reference) {
        super();
        this.role = role;
        this.reference = reference;
      }

        /**
         * @return {@link #role} (How the individual is involved in the resources content that is described in the exception.)
         */
        public CodeableConcept getRole() { 
          if (this.role == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create provisionActorComponent.role");
            else if (Configuration.doAutoCreate())
              this.role = new CodeableConcept(); // cc
          return this.role;
        }

        public boolean hasRole() { 
          return this.role != null && !this.role.isEmpty();
        }

        /**
         * @param value {@link #role} (How the individual is involved in the resources content that is described in the exception.)
         */
        public provisionActorComponent setRole(CodeableConcept value) { 
          this.role = value;
          return this;
        }

        /**
         * @return {@link #reference} (The resource that identifies the actor. To identify actors by type, use group to identify a set of actors by some property they share (e.g. 'admitting officers').)
         */
        public Reference getReference() { 
          if (this.reference == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create provisionActorComponent.reference");
            else if (Configuration.doAutoCreate())
              this.reference = new Reference(); // cc
          return this.reference;
        }

        public boolean hasReference() { 
          return this.reference != null && !this.reference.isEmpty();
        }

        /**
         * @param value {@link #reference} (The resource that identifies the actor. To identify actors by type, use group to identify a set of actors by some property they share (e.g. 'admitting officers').)
         */
        public provisionActorComponent setReference(Reference value) { 
          this.reference = value;
          return this;
        }

        /**
         * @return {@link #reference} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The resource that identifies the actor. To identify actors by type, use group to identify a set of actors by some property they share (e.g. 'admitting officers').)
         */
        public Resource getReferenceTarget() { 
          return this.referenceTarget;
        }

        /**
         * @param value {@link #reference} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The resource that identifies the actor. To identify actors by type, use group to identify a set of actors by some property they share (e.g. 'admitting officers').)
         */
        public provisionActorComponent setReferenceTarget(Resource value) { 
          this.referenceTarget = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("role", "CodeableConcept", "How the individual is involved in the resources content that is described in the exception.", 0, 1, role));
          children.add(new Property("reference", "Reference(Device|Group|CareTeam|Organization|Patient|Practitioner|RelatedPerson|PractitionerRole)", "The resource that identifies the actor. To identify actors by type, use group to identify a set of actors by some property they share (e.g. 'admitting officers').", 0, 1, reference));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3506294: /*role*/  return new Property("role", "CodeableConcept", "How the individual is involved in the resources content that is described in the exception.", 0, 1, role);
          case -925155509: /*reference*/  return new Property("reference", "Reference(Device|Group|CareTeam|Organization|Patient|Practitioner|RelatedPerson|PractitionerRole)", "The resource that identifies the actor. To identify actors by type, use group to identify a set of actors by some property they share (e.g. 'admitting officers').", 0, 1, reference);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3506294: /*role*/ return this.role == null ? new Base[0] : new Base[] {this.role}; // CodeableConcept
        case -925155509: /*reference*/ return this.reference == null ? new Base[0] : new Base[] {this.reference}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3506294: // role
          this.role = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -925155509: // reference
          this.reference = castToReference(value); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("role")) {
          this.role = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("reference")) {
          this.reference = castToReference(value); // Reference
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3506294:  return getRole(); 
        case -925155509:  return getReference(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3506294: /*role*/ return new String[] {"CodeableConcept"};
        case -925155509: /*reference*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("role")) {
          this.role = new CodeableConcept();
          return this.role;
        }
        else if (name.equals("reference")) {
          this.reference = new Reference();
          return this.reference;
        }
        else
          return super.addChild(name);
      }

      public provisionActorComponent copy() {
        provisionActorComponent dst = new provisionActorComponent();
        copyValues(dst);
        dst.role = role == null ? null : role.copy();
        dst.reference = reference == null ? null : reference.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof provisionActorComponent))
          return false;
        provisionActorComponent o = (provisionActorComponent) other_;
        return compareDeep(role, o.role, true) && compareDeep(reference, o.reference, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof provisionActorComponent))
          return false;
        provisionActorComponent o = (provisionActorComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(role, reference);
      }

  public String fhirType() {
    return "Consent.provision.actor";

  }

  }

    @Block()
    public static class provisionDataComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * How the resource reference is interpreted when testing consent restrictions.
         */
        @Child(name = "meaning", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="instance | related | dependents | authoredby", formalDefinition="How the resource reference is interpreted when testing consent restrictions." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/consent-data-meaning")
        protected Enumeration<ConsentDataMeaning> meaning;

        /**
         * A reference to a specific resource that defines which resources are covered by this consent.
         */
        @Child(name = "reference", type = {Reference.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The actual data reference", formalDefinition="A reference to a specific resource that defines which resources are covered by this consent." )
        protected Reference reference;

        /**
         * The actual object that is the target of the reference (A reference to a specific resource that defines which resources are covered by this consent.)
         */
        protected Resource referenceTarget;

        private static final long serialVersionUID = -424898645L;

    /**
     * Constructor
     */
      public provisionDataComponent() {
        super();
      }

    /**
     * Constructor
     */
      public provisionDataComponent(Enumeration<ConsentDataMeaning> meaning, Reference reference) {
        super();
        this.meaning = meaning;
        this.reference = reference;
      }

        /**
         * @return {@link #meaning} (How the resource reference is interpreted when testing consent restrictions.). This is the underlying object with id, value and extensions. The accessor "getMeaning" gives direct access to the value
         */
        public Enumeration<ConsentDataMeaning> getMeaningElement() { 
          if (this.meaning == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create provisionDataComponent.meaning");
            else if (Configuration.doAutoCreate())
              this.meaning = new Enumeration<ConsentDataMeaning>(new ConsentDataMeaningEnumFactory()); // bb
          return this.meaning;
        }

        public boolean hasMeaningElement() { 
          return this.meaning != null && !this.meaning.isEmpty();
        }

        public boolean hasMeaning() { 
          return this.meaning != null && !this.meaning.isEmpty();
        }

        /**
         * @param value {@link #meaning} (How the resource reference is interpreted when testing consent restrictions.). This is the underlying object with id, value and extensions. The accessor "getMeaning" gives direct access to the value
         */
        public provisionDataComponent setMeaningElement(Enumeration<ConsentDataMeaning> value) { 
          this.meaning = value;
          return this;
        }

        /**
         * @return How the resource reference is interpreted when testing consent restrictions.
         */
        public ConsentDataMeaning getMeaning() { 
          return this.meaning == null ? null : this.meaning.getValue();
        }

        /**
         * @param value How the resource reference is interpreted when testing consent restrictions.
         */
        public provisionDataComponent setMeaning(ConsentDataMeaning value) { 
            if (this.meaning == null)
              this.meaning = new Enumeration<ConsentDataMeaning>(new ConsentDataMeaningEnumFactory());
            this.meaning.setValue(value);
          return this;
        }

        /**
         * @return {@link #reference} (A reference to a specific resource that defines which resources are covered by this consent.)
         */
        public Reference getReference() { 
          if (this.reference == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create provisionDataComponent.reference");
            else if (Configuration.doAutoCreate())
              this.reference = new Reference(); // cc
          return this.reference;
        }

        public boolean hasReference() { 
          return this.reference != null && !this.reference.isEmpty();
        }

        /**
         * @param value {@link #reference} (A reference to a specific resource that defines which resources are covered by this consent.)
         */
        public provisionDataComponent setReference(Reference value) { 
          this.reference = value;
          return this;
        }

        /**
         * @return {@link #reference} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A reference to a specific resource that defines which resources are covered by this consent.)
         */
        public Resource getReferenceTarget() { 
          return this.referenceTarget;
        }

        /**
         * @param value {@link #reference} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A reference to a specific resource that defines which resources are covered by this consent.)
         */
        public provisionDataComponent setReferenceTarget(Resource value) { 
          this.referenceTarget = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("meaning", "code", "How the resource reference is interpreted when testing consent restrictions.", 0, 1, meaning));
          children.add(new Property("reference", "Reference(Any)", "A reference to a specific resource that defines which resources are covered by this consent.", 0, 1, reference));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 938160637: /*meaning*/  return new Property("meaning", "code", "How the resource reference is interpreted when testing consent restrictions.", 0, 1, meaning);
          case -925155509: /*reference*/  return new Property("reference", "Reference(Any)", "A reference to a specific resource that defines which resources are covered by this consent.", 0, 1, reference);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 938160637: /*meaning*/ return this.meaning == null ? new Base[0] : new Base[] {this.meaning}; // Enumeration<ConsentDataMeaning>
        case -925155509: /*reference*/ return this.reference == null ? new Base[0] : new Base[] {this.reference}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 938160637: // meaning
          value = new ConsentDataMeaningEnumFactory().fromType(castToCode(value));
          this.meaning = (Enumeration) value; // Enumeration<ConsentDataMeaning>
          return value;
        case -925155509: // reference
          this.reference = castToReference(value); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("meaning")) {
          value = new ConsentDataMeaningEnumFactory().fromType(castToCode(value));
          this.meaning = (Enumeration) value; // Enumeration<ConsentDataMeaning>
        } else if (name.equals("reference")) {
          this.reference = castToReference(value); // Reference
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 938160637:  return getMeaningElement();
        case -925155509:  return getReference(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 938160637: /*meaning*/ return new String[] {"code"};
        case -925155509: /*reference*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("meaning")) {
          throw new FHIRException("Cannot call addChild on a primitive type Consent.meaning");
        }
        else if (name.equals("reference")) {
          this.reference = new Reference();
          return this.reference;
        }
        else
          return super.addChild(name);
      }

      public provisionDataComponent copy() {
        provisionDataComponent dst = new provisionDataComponent();
        copyValues(dst);
        dst.meaning = meaning == null ? null : meaning.copy();
        dst.reference = reference == null ? null : reference.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof provisionDataComponent))
          return false;
        provisionDataComponent o = (provisionDataComponent) other_;
        return compareDeep(meaning, o.meaning, true) && compareDeep(reference, o.reference, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof provisionDataComponent))
          return false;
        provisionDataComponent o = (provisionDataComponent) other_;
        return compareValues(meaning, o.meaning, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(meaning, reference);
      }

  public String fhirType() {
    return "Consent.provision.data";

  }

  }

    /**
     * Unique identifier for this copy of the Consent Statement.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Identifier for this record (external references)", formalDefinition="Unique identifier for this copy of the Consent Statement." )
    protected List<Identifier> identifier;

    /**
     * Indicates the current state of this consent.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="draft | proposed | active | rejected | inactive | entered-in-error", formalDefinition="Indicates the current state of this consent." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/event-status")
    protected Enumeration<ConsentState> status;

    /**
     * A selector of the type of consent being presented: ADR, Privacy, Treatment, Research.  This list is now extensible.
     */
    @Child(name = "scope", type = {CodeableConcept.class}, order=2, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="Which of the four areas this resource covers (extensible)", formalDefinition="A selector of the type of consent being presented: ADR, Privacy, Treatment, Research.  This list is now extensible." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/consent-scope")
    protected CodeableConcept scope;

    /**
     * A classification of the type of consents found in the statement. This element supports indexing and retrieval of consent statements.
     */
    @Child(name = "category", type = {CodeableConcept.class}, order=3, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Classification of the consent statement - for indexing/retrieval", formalDefinition="A classification of the type of consents found in the statement. This element supports indexing and retrieval of consent statements." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/consent-category")
    protected List<CodeableConcept> category;

    /**
     * The patient/healthcare consumer to whom this consent applies.
     */
    @Child(name = "patient", type = {Patient.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who the consent applies to", formalDefinition="The patient/healthcare consumer to whom this consent applies." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (The patient/healthcare consumer to whom this consent applies.)
     */
    protected Patient patientTarget;

    /**
     * When this  Consent was issued / created / indexed.
     */
    @Child(name = "dateTime", type = {DateTimeType.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When this Consent was created or indexed", formalDefinition="When this  Consent was issued / created / indexed." )
    protected DateTimeType dateTime;

    /**
     * Either the Grantor, which is the entity responsible for granting the rights listed in a Consent Directive or the Grantee, which is the entity responsible for complying with the Consent Directive, including any obligations or limitations on authorizations and enforcement of prohibitions.
     */
    @Child(name = "performer", type = {Organization.class, Patient.class, Practitioner.class, RelatedPerson.class, PractitionerRole.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Who is agreeing to the policy and rules", formalDefinition="Either the Grantor, which is the entity responsible for granting the rights listed in a Consent Directive or the Grantee, which is the entity responsible for complying with the Consent Directive, including any obligations or limitations on authorizations and enforcement of prohibitions." )
    protected List<Reference> performer;
    /**
     * The actual objects that are the target of the reference (Either the Grantor, which is the entity responsible for granting the rights listed in a Consent Directive or the Grantee, which is the entity responsible for complying with the Consent Directive, including any obligations or limitations on authorizations and enforcement of prohibitions.)
     */
    protected List<Resource> performerTarget;


    /**
     * The organization that manages the consent, and the framework within which it is executed.
     */
    @Child(name = "organization", type = {Organization.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Custodian of the consent", formalDefinition="The organization that manages the consent, and the framework within which it is executed." )
    protected List<Reference> organization;
    /**
     * The actual objects that are the target of the reference (The organization that manages the consent, and the framework within which it is executed.)
     */
    protected List<Organization> organizationTarget;


    /**
     * The source on which this consent statement is based. The source might be a scanned original paper form, or a reference to a consent that links back to such a source, a reference to a document repository (e.g. XDS) that stores the original consent document.
     */
    @Child(name = "source", type = {Attachment.class, Identifier.class, Consent.class, DocumentReference.class, Contract.class, QuestionnaireResponse.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Source from which this consent is taken", formalDefinition="The source on which this consent statement is based. The source might be a scanned original paper form, or a reference to a consent that links back to such a source, a reference to a document repository (e.g. XDS) that stores the original consent document." )
    protected Type source;

    /**
     * The references to the policies that are included in this consent scope. Policies may be organizational, but are often defined jurisdictionally, or in law.
     */
    @Child(name = "policy", type = {}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Policies covered by this consent", formalDefinition="The references to the policies that are included in this consent scope. Policies may be organizational, but are often defined jurisdictionally, or in law." )
    protected List<ConsentPolicyComponent> policy;

    /**
     * A reference to the specific base computable regulation or policy.
     */
    @Child(name = "policyRule", type = {CodeableConcept.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Regulation that this consents to", formalDefinition="A reference to the specific base computable regulation or policy." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/consent-policy")
    protected CodeableConcept policyRule;

    /**
     * Whether a treatment instruction (e.g. artificial respiration yes or no) was verified with the patient, his/her family or another authorized person.
     */
    @Child(name = "verification", type = {}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Consent Verified by patient or family", formalDefinition="Whether a treatment instruction (e.g. artificial respiration yes or no) was verified with the patient, his/her family or another authorized person." )
    protected List<ConsentVerificationComponent> verification;

    /**
     * An exception to the base policy of this consent. An exception can be an addition or removal of access permissions.
     */
    @Child(name = "provision", type = {}, order=12, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Constraints to the base Consent.policyRule", formalDefinition="An exception to the base policy of this consent. An exception can be an addition or removal of access permissions." )
    protected provisionComponent provision;

    private static final long serialVersionUID = 206528051L;

  /**
   * Constructor
   */
    public Consent() {
      super();
    }

  /**
   * Constructor
   */
    public Consent(Enumeration<ConsentState> status, CodeableConcept scope) {
      super();
      this.status = status;
      this.scope = scope;
    }

    /**
     * @return {@link #identifier} (Unique identifier for this copy of the Consent Statement.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Consent setIdentifier(List<Identifier> theIdentifier) { 
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

    public Consent addIdentifier(Identifier t) { //3
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
     * @return {@link #status} (Indicates the current state of this consent.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<ConsentState> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Consent.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<ConsentState>(new ConsentStateEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (Indicates the current state of this consent.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Consent setStatusElement(Enumeration<ConsentState> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return Indicates the current state of this consent.
     */
    public ConsentState getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value Indicates the current state of this consent.
     */
    public Consent setStatus(ConsentState value) { 
        if (this.status == null)
          this.status = new Enumeration<ConsentState>(new ConsentStateEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #scope} (A selector of the type of consent being presented: ADR, Privacy, Treatment, Research.  This list is now extensible.)
     */
    public CodeableConcept getScope() { 
      if (this.scope == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Consent.scope");
        else if (Configuration.doAutoCreate())
          this.scope = new CodeableConcept(); // cc
      return this.scope;
    }

    public boolean hasScope() { 
      return this.scope != null && !this.scope.isEmpty();
    }

    /**
     * @param value {@link #scope} (A selector of the type of consent being presented: ADR, Privacy, Treatment, Research.  This list is now extensible.)
     */
    public Consent setScope(CodeableConcept value) { 
      this.scope = value;
      return this;
    }

    /**
     * @return {@link #category} (A classification of the type of consents found in the statement. This element supports indexing and retrieval of consent statements.)
     */
    public List<CodeableConcept> getCategory() { 
      if (this.category == null)
        this.category = new ArrayList<CodeableConcept>();
      return this.category;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Consent setCategory(List<CodeableConcept> theCategory) { 
      this.category = theCategory;
      return this;
    }

    public boolean hasCategory() { 
      if (this.category == null)
        return false;
      for (CodeableConcept item : this.category)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addCategory() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.category == null)
        this.category = new ArrayList<CodeableConcept>();
      this.category.add(t);
      return t;
    }

    public Consent addCategory(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.category == null)
        this.category = new ArrayList<CodeableConcept>();
      this.category.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #category}, creating it if it does not already exist
     */
    public CodeableConcept getCategoryFirstRep() { 
      if (getCategory().isEmpty()) {
        addCategory();
      }
      return getCategory().get(0);
    }

    /**
     * @return {@link #patient} (The patient/healthcare consumer to whom this consent applies.)
     */
    public Reference getPatient() { 
      if (this.patient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Consent.patient");
        else if (Configuration.doAutoCreate())
          this.patient = new Reference(); // cc
      return this.patient;
    }

    public boolean hasPatient() { 
      return this.patient != null && !this.patient.isEmpty();
    }

    /**
     * @param value {@link #patient} (The patient/healthcare consumer to whom this consent applies.)
     */
    public Consent setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The patient/healthcare consumer to whom this consent applies.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Consent.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient(); // aa
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The patient/healthcare consumer to whom this consent applies.)
     */
    public Consent setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #dateTime} (When this  Consent was issued / created / indexed.). This is the underlying object with id, value and extensions. The accessor "getDateTime" gives direct access to the value
     */
    public DateTimeType getDateTimeElement() { 
      if (this.dateTime == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Consent.dateTime");
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
     * @param value {@link #dateTime} (When this  Consent was issued / created / indexed.). This is the underlying object with id, value and extensions. The accessor "getDateTime" gives direct access to the value
     */
    public Consent setDateTimeElement(DateTimeType value) { 
      this.dateTime = value;
      return this;
    }

    /**
     * @return When this  Consent was issued / created / indexed.
     */
    public Date getDateTime() { 
      return this.dateTime == null ? null : this.dateTime.getValue();
    }

    /**
     * @param value When this  Consent was issued / created / indexed.
     */
    public Consent setDateTime(Date value) { 
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
     * @return {@link #performer} (Either the Grantor, which is the entity responsible for granting the rights listed in a Consent Directive or the Grantee, which is the entity responsible for complying with the Consent Directive, including any obligations or limitations on authorizations and enforcement of prohibitions.)
     */
    public List<Reference> getPerformer() { 
      if (this.performer == null)
        this.performer = new ArrayList<Reference>();
      return this.performer;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Consent setPerformer(List<Reference> thePerformer) { 
      this.performer = thePerformer;
      return this;
    }

    public boolean hasPerformer() { 
      if (this.performer == null)
        return false;
      for (Reference item : this.performer)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addPerformer() { //3
      Reference t = new Reference();
      if (this.performer == null)
        this.performer = new ArrayList<Reference>();
      this.performer.add(t);
      return t;
    }

    public Consent addPerformer(Reference t) { //3
      if (t == null)
        return this;
      if (this.performer == null)
        this.performer = new ArrayList<Reference>();
      this.performer.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #performer}, creating it if it does not already exist
     */
    public Reference getPerformerFirstRep() { 
      if (getPerformer().isEmpty()) {
        addPerformer();
      }
      return getPerformer().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getPerformerTarget() { 
      if (this.performerTarget == null)
        this.performerTarget = new ArrayList<Resource>();
      return this.performerTarget;
    }

    /**
     * @return {@link #organization} (The organization that manages the consent, and the framework within which it is executed.)
     */
    public List<Reference> getOrganization() { 
      if (this.organization == null)
        this.organization = new ArrayList<Reference>();
      return this.organization;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Consent setOrganization(List<Reference> theOrganization) { 
      this.organization = theOrganization;
      return this;
    }

    public boolean hasOrganization() { 
      if (this.organization == null)
        return false;
      for (Reference item : this.organization)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addOrganization() { //3
      Reference t = new Reference();
      if (this.organization == null)
        this.organization = new ArrayList<Reference>();
      this.organization.add(t);
      return t;
    }

    public Consent addOrganization(Reference t) { //3
      if (t == null)
        return this;
      if (this.organization == null)
        this.organization = new ArrayList<Reference>();
      this.organization.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #organization}, creating it if it does not already exist
     */
    public Reference getOrganizationFirstRep() { 
      if (getOrganization().isEmpty()) {
        addOrganization();
      }
      return getOrganization().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Organization> getOrganizationTarget() { 
      if (this.organizationTarget == null)
        this.organizationTarget = new ArrayList<Organization>();
      return this.organizationTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Organization addOrganizationTarget() { 
      Organization r = new Organization();
      if (this.organizationTarget == null)
        this.organizationTarget = new ArrayList<Organization>();
      this.organizationTarget.add(r);
      return r;
    }

    /**
     * @return {@link #source} (The source on which this consent statement is based. The source might be a scanned original paper form, or a reference to a consent that links back to such a source, a reference to a document repository (e.g. XDS) that stores the original consent document.)
     */
    public Type getSource() { 
      return this.source;
    }

    /**
     * @return {@link #source} (The source on which this consent statement is based. The source might be a scanned original paper form, or a reference to a consent that links back to such a source, a reference to a document repository (e.g. XDS) that stores the original consent document.)
     */
    public Attachment getSourceAttachment() throws FHIRException { 
      if (this.source == null)
        return null;
      if (!(this.source instanceof Attachment))
        throw new FHIRException("Type mismatch: the type Attachment was expected, but "+this.source.getClass().getName()+" was encountered");
      return (Attachment) this.source;
    }

    public boolean hasSourceAttachment() { 
      return this != null && this.source instanceof Attachment;
    }

    /**
     * @return {@link #source} (The source on which this consent statement is based. The source might be a scanned original paper form, or a reference to a consent that links back to such a source, a reference to a document repository (e.g. XDS) that stores the original consent document.)
     */
    public Identifier getSourceIdentifier() throws FHIRException { 
      if (this.source == null)
        return null;
      if (!(this.source instanceof Identifier))
        throw new FHIRException("Type mismatch: the type Identifier was expected, but "+this.source.getClass().getName()+" was encountered");
      return (Identifier) this.source;
    }

    public boolean hasSourceIdentifier() { 
      return this != null && this.source instanceof Identifier;
    }

    /**
     * @return {@link #source} (The source on which this consent statement is based. The source might be a scanned original paper form, or a reference to a consent that links back to such a source, a reference to a document repository (e.g. XDS) that stores the original consent document.)
     */
    public Reference getSourceReference() throws FHIRException { 
      if (this.source == null)
        return null;
      if (!(this.source instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.source.getClass().getName()+" was encountered");
      return (Reference) this.source;
    }

    public boolean hasSourceReference() { 
      return this != null && this.source instanceof Reference;
    }

    public boolean hasSource() { 
      return this.source != null && !this.source.isEmpty();
    }

    /**
     * @param value {@link #source} (The source on which this consent statement is based. The source might be a scanned original paper form, or a reference to a consent that links back to such a source, a reference to a document repository (e.g. XDS) that stores the original consent document.)
     */
    public Consent setSource(Type value) { 
      if (value != null && !(value instanceof Attachment || value instanceof Identifier || value instanceof Reference))
        throw new Error("Not the right type for Consent.source[x]: "+value.fhirType());
      this.source = value;
      return this;
    }

    /**
     * @return {@link #policy} (The references to the policies that are included in this consent scope. Policies may be organizational, but are often defined jurisdictionally, or in law.)
     */
    public List<ConsentPolicyComponent> getPolicy() { 
      if (this.policy == null)
        this.policy = new ArrayList<ConsentPolicyComponent>();
      return this.policy;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Consent setPolicy(List<ConsentPolicyComponent> thePolicy) { 
      this.policy = thePolicy;
      return this;
    }

    public boolean hasPolicy() { 
      if (this.policy == null)
        return false;
      for (ConsentPolicyComponent item : this.policy)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ConsentPolicyComponent addPolicy() { //3
      ConsentPolicyComponent t = new ConsentPolicyComponent();
      if (this.policy == null)
        this.policy = new ArrayList<ConsentPolicyComponent>();
      this.policy.add(t);
      return t;
    }

    public Consent addPolicy(ConsentPolicyComponent t) { //3
      if (t == null)
        return this;
      if (this.policy == null)
        this.policy = new ArrayList<ConsentPolicyComponent>();
      this.policy.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #policy}, creating it if it does not already exist
     */
    public ConsentPolicyComponent getPolicyFirstRep() { 
      if (getPolicy().isEmpty()) {
        addPolicy();
      }
      return getPolicy().get(0);
    }

    /**
     * @return {@link #policyRule} (A reference to the specific base computable regulation or policy.)
     */
    public CodeableConcept getPolicyRule() { 
      if (this.policyRule == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Consent.policyRule");
        else if (Configuration.doAutoCreate())
          this.policyRule = new CodeableConcept(); // cc
      return this.policyRule;
    }

    public boolean hasPolicyRule() { 
      return this.policyRule != null && !this.policyRule.isEmpty();
    }

    /**
     * @param value {@link #policyRule} (A reference to the specific base computable regulation or policy.)
     */
    public Consent setPolicyRule(CodeableConcept value) { 
      this.policyRule = value;
      return this;
    }

    /**
     * @return {@link #verification} (Whether a treatment instruction (e.g. artificial respiration yes or no) was verified with the patient, his/her family or another authorized person.)
     */
    public List<ConsentVerificationComponent> getVerification() { 
      if (this.verification == null)
        this.verification = new ArrayList<ConsentVerificationComponent>();
      return this.verification;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Consent setVerification(List<ConsentVerificationComponent> theVerification) { 
      this.verification = theVerification;
      return this;
    }

    public boolean hasVerification() { 
      if (this.verification == null)
        return false;
      for (ConsentVerificationComponent item : this.verification)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ConsentVerificationComponent addVerification() { //3
      ConsentVerificationComponent t = new ConsentVerificationComponent();
      if (this.verification == null)
        this.verification = new ArrayList<ConsentVerificationComponent>();
      this.verification.add(t);
      return t;
    }

    public Consent addVerification(ConsentVerificationComponent t) { //3
      if (t == null)
        return this;
      if (this.verification == null)
        this.verification = new ArrayList<ConsentVerificationComponent>();
      this.verification.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #verification}, creating it if it does not already exist
     */
    public ConsentVerificationComponent getVerificationFirstRep() { 
      if (getVerification().isEmpty()) {
        addVerification();
      }
      return getVerification().get(0);
    }

    /**
     * @return {@link #provision} (An exception to the base policy of this consent. An exception can be an addition or removal of access permissions.)
     */
    public provisionComponent getProvision() { 
      if (this.provision == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Consent.provision");
        else if (Configuration.doAutoCreate())
          this.provision = new provisionComponent(); // cc
      return this.provision;
    }

    public boolean hasProvision() { 
      return this.provision != null && !this.provision.isEmpty();
    }

    /**
     * @param value {@link #provision} (An exception to the base policy of this consent. An exception can be an addition or removal of access permissions.)
     */
    public Consent setProvision(provisionComponent value) { 
      this.provision = value;
      return this;
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("identifier", "Identifier", "Unique identifier for this copy of the Consent Statement.", 0, java.lang.Integer.MAX_VALUE, identifier));
        children.add(new Property("status", "code", "Indicates the current state of this consent.", 0, 1, status));
        children.add(new Property("scope", "CodeableConcept", "A selector of the type of consent being presented: ADR, Privacy, Treatment, Research.  This list is now extensible.", 0, 1, scope));
        children.add(new Property("category", "CodeableConcept", "A classification of the type of consents found in the statement. This element supports indexing and retrieval of consent statements.", 0, java.lang.Integer.MAX_VALUE, category));
        children.add(new Property("patient", "Reference(Patient)", "The patient/healthcare consumer to whom this consent applies.", 0, 1, patient));
        children.add(new Property("dateTime", "dateTime", "When this  Consent was issued / created / indexed.", 0, 1, dateTime));
        children.add(new Property("performer", "Reference(Organization|Patient|Practitioner|RelatedPerson|PractitionerRole)", "Either the Grantor, which is the entity responsible for granting the rights listed in a Consent Directive or the Grantee, which is the entity responsible for complying with the Consent Directive, including any obligations or limitations on authorizations and enforcement of prohibitions.", 0, java.lang.Integer.MAX_VALUE, performer));
        children.add(new Property("organization", "Reference(Organization)", "The organization that manages the consent, and the framework within which it is executed.", 0, java.lang.Integer.MAX_VALUE, organization));
        children.add(new Property("source[x]", "Attachment|Identifier|Reference(Consent|DocumentReference|Contract|QuestionnaireResponse)", "The source on which this consent statement is based. The source might be a scanned original paper form, or a reference to a consent that links back to such a source, a reference to a document repository (e.g. XDS) that stores the original consent document.", 0, 1, source));
        children.add(new Property("policy", "", "The references to the policies that are included in this consent scope. Policies may be organizational, but are often defined jurisdictionally, or in law.", 0, java.lang.Integer.MAX_VALUE, policy));
        children.add(new Property("policyRule", "CodeableConcept", "A reference to the specific base computable regulation or policy.", 0, 1, policyRule));
        children.add(new Property("verification", "", "Whether a treatment instruction (e.g. artificial respiration yes or no) was verified with the patient, his/her family or another authorized person.", 0, java.lang.Integer.MAX_VALUE, verification));
        children.add(new Property("provision", "", "An exception to the base policy of this consent. An exception can be an addition or removal of access permissions.", 0, 1, provision));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "Unique identifier for this copy of the Consent Statement.", 0, java.lang.Integer.MAX_VALUE, identifier);
        case -892481550: /*status*/  return new Property("status", "code", "Indicates the current state of this consent.", 0, 1, status);
        case 109264468: /*scope*/  return new Property("scope", "CodeableConcept", "A selector of the type of consent being presented: ADR, Privacy, Treatment, Research.  This list is now extensible.", 0, 1, scope);
        case 50511102: /*category*/  return new Property("category", "CodeableConcept", "A classification of the type of consents found in the statement. This element supports indexing and retrieval of consent statements.", 0, java.lang.Integer.MAX_VALUE, category);
        case -791418107: /*patient*/  return new Property("patient", "Reference(Patient)", "The patient/healthcare consumer to whom this consent applies.", 0, 1, patient);
        case 1792749467: /*dateTime*/  return new Property("dateTime", "dateTime", "When this  Consent was issued / created / indexed.", 0, 1, dateTime);
        case 481140686: /*performer*/  return new Property("performer", "Reference(Organization|Patient|Practitioner|RelatedPerson|PractitionerRole)", "Either the Grantor, which is the entity responsible for granting the rights listed in a Consent Directive or the Grantee, which is the entity responsible for complying with the Consent Directive, including any obligations or limitations on authorizations and enforcement of prohibitions.", 0, java.lang.Integer.MAX_VALUE, performer);
        case 1178922291: /*organization*/  return new Property("organization", "Reference(Organization)", "The organization that manages the consent, and the framework within which it is executed.", 0, java.lang.Integer.MAX_VALUE, organization);
        case -1698413947: /*source[x]*/  return new Property("source[x]", "Attachment|Identifier|Reference(Consent|DocumentReference|Contract|QuestionnaireResponse)", "The source on which this consent statement is based. The source might be a scanned original paper form, or a reference to a consent that links back to such a source, a reference to a document repository (e.g. XDS) that stores the original consent document.", 0, 1, source);
        case -896505829: /*source*/  return new Property("source[x]", "Attachment|Identifier|Reference(Consent|DocumentReference|Contract|QuestionnaireResponse)", "The source on which this consent statement is based. The source might be a scanned original paper form, or a reference to a consent that links back to such a source, a reference to a document repository (e.g. XDS) that stores the original consent document.", 0, 1, source);
        case 1964406686: /*sourceAttachment*/  return new Property("source[x]", "Attachment|Identifier|Reference(Consent|DocumentReference|Contract|QuestionnaireResponse)", "The source on which this consent statement is based. The source might be a scanned original paper form, or a reference to a consent that links back to such a source, a reference to a document repository (e.g. XDS) that stores the original consent document.", 0, 1, source);
        case -1985492188: /*sourceIdentifier*/  return new Property("source[x]", "Attachment|Identifier|Reference(Consent|DocumentReference|Contract|QuestionnaireResponse)", "The source on which this consent statement is based. The source might be a scanned original paper form, or a reference to a consent that links back to such a source, a reference to a document repository (e.g. XDS) that stores the original consent document.", 0, 1, source);
        case -244259472: /*sourceReference*/  return new Property("source[x]", "Attachment|Identifier|Reference(Consent|DocumentReference|Contract|QuestionnaireResponse)", "The source on which this consent statement is based. The source might be a scanned original paper form, or a reference to a consent that links back to such a source, a reference to a document repository (e.g. XDS) that stores the original consent document.", 0, 1, source);
        case -982670030: /*policy*/  return new Property("policy", "", "The references to the policies that are included in this consent scope. Policies may be organizational, but are often defined jurisdictionally, or in law.", 0, java.lang.Integer.MAX_VALUE, policy);
        case 1593493326: /*policyRule*/  return new Property("policyRule", "CodeableConcept", "A reference to the specific base computable regulation or policy.", 0, 1, policyRule);
        case -1484401125: /*verification*/  return new Property("verification", "", "Whether a treatment instruction (e.g. artificial respiration yes or no) was verified with the patient, his/her family or another authorized person.", 0, java.lang.Integer.MAX_VALUE, verification);
        case -547120939: /*provision*/  return new Property("provision", "", "An exception to the base policy of this consent. An exception can be an addition or removal of access permissions.", 0, 1, provision);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<ConsentState>
        case 109264468: /*scope*/ return this.scope == null ? new Base[0] : new Base[] {this.scope}; // CodeableConcept
        case 50511102: /*category*/ return this.category == null ? new Base[0] : this.category.toArray(new Base[this.category.size()]); // CodeableConcept
        case -791418107: /*patient*/ return this.patient == null ? new Base[0] : new Base[] {this.patient}; // Reference
        case 1792749467: /*dateTime*/ return this.dateTime == null ? new Base[0] : new Base[] {this.dateTime}; // DateTimeType
        case 481140686: /*performer*/ return this.performer == null ? new Base[0] : this.performer.toArray(new Base[this.performer.size()]); // Reference
        case 1178922291: /*organization*/ return this.organization == null ? new Base[0] : this.organization.toArray(new Base[this.organization.size()]); // Reference
        case -896505829: /*source*/ return this.source == null ? new Base[0] : new Base[] {this.source}; // Type
        case -982670030: /*policy*/ return this.policy == null ? new Base[0] : this.policy.toArray(new Base[this.policy.size()]); // ConsentPolicyComponent
        case 1593493326: /*policyRule*/ return this.policyRule == null ? new Base[0] : new Base[] {this.policyRule}; // CodeableConcept
        case -1484401125: /*verification*/ return this.verification == null ? new Base[0] : this.verification.toArray(new Base[this.verification.size()]); // ConsentVerificationComponent
        case -547120939: /*provision*/ return this.provision == null ? new Base[0] : new Base[] {this.provision}; // provisionComponent
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
          value = new ConsentStateEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<ConsentState>
          return value;
        case 109264468: // scope
          this.scope = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 50511102: // category
          this.getCategory().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -791418107: // patient
          this.patient = castToReference(value); // Reference
          return value;
        case 1792749467: // dateTime
          this.dateTime = castToDateTime(value); // DateTimeType
          return value;
        case 481140686: // performer
          this.getPerformer().add(castToReference(value)); // Reference
          return value;
        case 1178922291: // organization
          this.getOrganization().add(castToReference(value)); // Reference
          return value;
        case -896505829: // source
          this.source = castToType(value); // Type
          return value;
        case -982670030: // policy
          this.getPolicy().add((ConsentPolicyComponent) value); // ConsentPolicyComponent
          return value;
        case 1593493326: // policyRule
          this.policyRule = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1484401125: // verification
          this.getVerification().add((ConsentVerificationComponent) value); // ConsentVerificationComponent
          return value;
        case -547120939: // provision
          this.provision = (provisionComponent) value; // provisionComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("status")) {
          value = new ConsentStateEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<ConsentState>
        } else if (name.equals("scope")) {
          this.scope = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("category")) {
          this.getCategory().add(castToCodeableConcept(value));
        } else if (name.equals("patient")) {
          this.patient = castToReference(value); // Reference
        } else if (name.equals("dateTime")) {
          this.dateTime = castToDateTime(value); // DateTimeType
        } else if (name.equals("performer")) {
          this.getPerformer().add(castToReference(value));
        } else if (name.equals("organization")) {
          this.getOrganization().add(castToReference(value));
        } else if (name.equals("source[x]")) {
          this.source = castToType(value); // Type
        } else if (name.equals("policy")) {
          this.getPolicy().add((ConsentPolicyComponent) value);
        } else if (name.equals("policyRule")) {
          this.policyRule = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("verification")) {
          this.getVerification().add((ConsentVerificationComponent) value);
        } else if (name.equals("provision")) {
          this.provision = (provisionComponent) value; // provisionComponent
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case -892481550:  return getStatusElement();
        case 109264468:  return getScope(); 
        case 50511102:  return addCategory(); 
        case -791418107:  return getPatient(); 
        case 1792749467:  return getDateTimeElement();
        case 481140686:  return addPerformer(); 
        case 1178922291:  return addOrganization(); 
        case -1698413947:  return getSource(); 
        case -896505829:  return getSource(); 
        case -982670030:  return addPolicy(); 
        case 1593493326:  return getPolicyRule(); 
        case -1484401125:  return addVerification(); 
        case -547120939:  return getProvision(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -892481550: /*status*/ return new String[] {"code"};
        case 109264468: /*scope*/ return new String[] {"CodeableConcept"};
        case 50511102: /*category*/ return new String[] {"CodeableConcept"};
        case -791418107: /*patient*/ return new String[] {"Reference"};
        case 1792749467: /*dateTime*/ return new String[] {"dateTime"};
        case 481140686: /*performer*/ return new String[] {"Reference"};
        case 1178922291: /*organization*/ return new String[] {"Reference"};
        case -896505829: /*source*/ return new String[] {"Attachment", "Identifier", "Reference"};
        case -982670030: /*policy*/ return new String[] {};
        case 1593493326: /*policyRule*/ return new String[] {"CodeableConcept"};
        case -1484401125: /*verification*/ return new String[] {};
        case -547120939: /*provision*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type Consent.status");
        }
        else if (name.equals("scope")) {
          this.scope = new CodeableConcept();
          return this.scope;
        }
        else if (name.equals("category")) {
          return addCategory();
        }
        else if (name.equals("patient")) {
          this.patient = new Reference();
          return this.patient;
        }
        else if (name.equals("dateTime")) {
          throw new FHIRException("Cannot call addChild on a primitive type Consent.dateTime");
        }
        else if (name.equals("performer")) {
          return addPerformer();
        }
        else if (name.equals("organization")) {
          return addOrganization();
        }
        else if (name.equals("sourceAttachment")) {
          this.source = new Attachment();
          return this.source;
        }
        else if (name.equals("sourceIdentifier")) {
          this.source = new Identifier();
          return this.source;
        }
        else if (name.equals("sourceReference")) {
          this.source = new Reference();
          return this.source;
        }
        else if (name.equals("policy")) {
          return addPolicy();
        }
        else if (name.equals("policyRule")) {
          this.policyRule = new CodeableConcept();
          return this.policyRule;
        }
        else if (name.equals("verification")) {
          return addVerification();
        }
        else if (name.equals("provision")) {
          this.provision = new provisionComponent();
          return this.provision;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Consent";

  }

      public Consent copy() {
        Consent dst = new Consent();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.scope = scope == null ? null : scope.copy();
        if (category != null) {
          dst.category = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : category)
            dst.category.add(i.copy());
        };
        dst.patient = patient == null ? null : patient.copy();
        dst.dateTime = dateTime == null ? null : dateTime.copy();
        if (performer != null) {
          dst.performer = new ArrayList<Reference>();
          for (Reference i : performer)
            dst.performer.add(i.copy());
        };
        if (organization != null) {
          dst.organization = new ArrayList<Reference>();
          for (Reference i : organization)
            dst.organization.add(i.copy());
        };
        dst.source = source == null ? null : source.copy();
        if (policy != null) {
          dst.policy = new ArrayList<ConsentPolicyComponent>();
          for (ConsentPolicyComponent i : policy)
            dst.policy.add(i.copy());
        };
        dst.policyRule = policyRule == null ? null : policyRule.copy();
        if (verification != null) {
          dst.verification = new ArrayList<ConsentVerificationComponent>();
          for (ConsentVerificationComponent i : verification)
            dst.verification.add(i.copy());
        };
        dst.provision = provision == null ? null : provision.copy();
        return dst;
      }

      protected Consent typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof Consent))
          return false;
        Consent o = (Consent) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(scope, o.scope, true)
           && compareDeep(category, o.category, true) && compareDeep(patient, o.patient, true) && compareDeep(dateTime, o.dateTime, true)
           && compareDeep(performer, o.performer, true) && compareDeep(organization, o.organization, true)
           && compareDeep(source, o.source, true) && compareDeep(policy, o.policy, true) && compareDeep(policyRule, o.policyRule, true)
           && compareDeep(verification, o.verification, true) && compareDeep(provision, o.provision, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof Consent))
          return false;
        Consent o = (Consent) other_;
        return compareValues(status, o.status, true) && compareValues(dateTime, o.dateTime, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, status, scope
          , category, patient, dateTime, performer, organization, source, policy, policyRule
          , verification, provision);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Consent;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>When this Consent was created or indexed</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Consent.dateTime</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="Consent.dateTime", description="When this Consent was created or indexed", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>When this Consent was created or indexed</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Consent.dateTime</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Identifier for this record (external references)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Consent.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="Consent.identifier", description="Identifier for this record (external references)", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Identifier for this record (external references)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Consent.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>securitylabel</b>
   * <p>
   * Description: <b>Security Labels that define affected resources</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Consent.provision.securityLabel</b><br>
   * </p>
   */
  @SearchParamDefinition(name="securitylabel", path="Consent.provision.securityLabel", description="Security Labels that define affected resources", type="token" )
  public static final String SP_SECURITYLABEL = "securitylabel";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>securitylabel</b>
   * <p>
   * Description: <b>Security Labels that define affected resources</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Consent.provision.securityLabel</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam SECURITYLABEL = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_SECURITYLABEL);

 /**
   * Search parameter: <b>period</b>
   * <p>
   * Description: <b>Timeframe for this rule</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Consent.provision.period</b><br>
   * </p>
   */
  @SearchParamDefinition(name="period", path="Consent.provision.period", description="Timeframe for this rule", type="date" )
  public static final String SP_PERIOD = "period";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>period</b>
   * <p>
   * Description: <b>Timeframe for this rule</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Consent.provision.period</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam PERIOD = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_PERIOD);

 /**
   * Search parameter: <b>data</b>
   * <p>
   * Description: <b>The actual data reference</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Consent.provision.data.reference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="data", path="Consent.provision.data.reference", description="The actual data reference", type="reference" )
  public static final String SP_DATA = "data";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>data</b>
   * <p>
   * Description: <b>The actual data reference</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Consent.provision.data.reference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam DATA = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_DATA);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Consent:data</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_DATA = new ca.uhn.fhir.model.api.Include("Consent:data").toLocked();

 /**
   * Search parameter: <b>purpose</b>
   * <p>
   * Description: <b>Context of activities covered by this rule</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Consent.provision.purpose</b><br>
   * </p>
   */
  @SearchParamDefinition(name="purpose", path="Consent.provision.purpose", description="Context of activities covered by this rule", type="token" )
  public static final String SP_PURPOSE = "purpose";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>purpose</b>
   * <p>
   * Description: <b>Context of activities covered by this rule</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Consent.provision.purpose</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam PURPOSE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_PURPOSE);

 /**
   * Search parameter: <b>source-reference</b>
   * <p>
   * Description: <b>Search by reference to a Consent, DocumentReference, Contract  or QuestionnaireResponse</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Consent.source[x]</b><br>
   * </p>
   */
  @SearchParamDefinition(name="source-reference", path="Consent.source", description="Search by reference to a Consent, DocumentReference, Contract  or QuestionnaireResponse", type="reference", target={Consent.class, Contract.class, DocumentReference.class, QuestionnaireResponse.class } )
  public static final String SP_SOURCE_REFERENCE = "source-reference";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>source-reference</b>
   * <p>
   * Description: <b>Search by reference to a Consent, DocumentReference, Contract  or QuestionnaireResponse</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Consent.source[x]</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SOURCE_REFERENCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SOURCE_REFERENCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Consent:source-reference</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SOURCE_REFERENCE = new ca.uhn.fhir.model.api.Include("Consent:source-reference").toLocked();

 /**
   * Search parameter: <b>source-identifier</b>
   * <p>
   * Description: <b>Search by token to an identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Consent.source[x]</b><br>
   * </p>
   */
  @SearchParamDefinition(name="source-identifier", path="Consent.source", description="Search by token to an identifier", type="token" )
  public static final String SP_SOURCE_IDENTIFIER = "source-identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>source-identifier</b>
   * <p>
   * Description: <b>Search by token to an identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Consent.source[x]</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam SOURCE_IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_SOURCE_IDENTIFIER);

 /**
   * Search parameter: <b>actor</b>
   * <p>
   * Description: <b>Resource for the actor (or group, by role)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Consent.provision.actor.reference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="actor", path="Consent.provision.actor.reference", description="Resource for the actor (or group, by role)", type="reference", target={CareTeam.class, Device.class, Group.class, Organization.class, Patient.class, Practitioner.class, PractitionerRole.class, RelatedPerson.class } )
  public static final String SP_ACTOR = "actor";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>actor</b>
   * <p>
   * Description: <b>Resource for the actor (or group, by role)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Consent.provision.actor.reference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ACTOR = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ACTOR);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Consent:actor</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ACTOR = new ca.uhn.fhir.model.api.Include("Consent:actor").toLocked();

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Who the consent applies to</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Consent.patient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="Consent.patient", description="Who the consent applies to", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>Who the consent applies to</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Consent.patient</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Consent:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("Consent:patient").toLocked();

 /**
   * Search parameter: <b>organization</b>
   * <p>
   * Description: <b>Custodian of the consent</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Consent.organization</b><br>
   * </p>
   */
  @SearchParamDefinition(name="organization", path="Consent.organization", description="Custodian of the consent", type="reference", target={Organization.class } )
  public static final String SP_ORGANIZATION = "organization";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>organization</b>
   * <p>
   * Description: <b>Custodian of the consent</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Consent.organization</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ORGANIZATION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ORGANIZATION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Consent:organization</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ORGANIZATION = new ca.uhn.fhir.model.api.Include("Consent:organization").toLocked();

 /**
   * Search parameter: <b>scope</b>
   * <p>
   * Description: <b>Which of the four areas this resource covers (extensible)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Consent.scope</b><br>
   * </p>
   */
  @SearchParamDefinition(name="scope", path="Consent.scope", description="Which of the four areas this resource covers (extensible)", type="token" )
  public static final String SP_SCOPE = "scope";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>scope</b>
   * <p>
   * Description: <b>Which of the four areas this resource covers (extensible)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Consent.scope</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam SCOPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_SCOPE);

 /**
   * Search parameter: <b>action</b>
   * <p>
   * Description: <b>Actions controlled by this rule</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Consent.provision.action</b><br>
   * </p>
   */
  @SearchParamDefinition(name="action", path="Consent.provision.action", description="Actions controlled by this rule", type="token" )
  public static final String SP_ACTION = "action";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>action</b>
   * <p>
   * Description: <b>Actions controlled by this rule</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Consent.provision.action</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam ACTION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_ACTION);

 /**
   * Search parameter: <b>consentor</b>
   * <p>
   * Description: <b>Who is agreeing to the policy and rules</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Consent.performer</b><br>
   * </p>
   */
  @SearchParamDefinition(name="consentor", path="Consent.performer", description="Who is agreeing to the policy and rules", type="reference", target={Organization.class, Patient.class, Practitioner.class, PractitionerRole.class, RelatedPerson.class } )
  public static final String SP_CONSENTOR = "consentor";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>consentor</b>
   * <p>
   * Description: <b>Who is agreeing to the policy and rules</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Consent.performer</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam CONSENTOR = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_CONSENTOR);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Consent:consentor</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_CONSENTOR = new ca.uhn.fhir.model.api.Include("Consent:consentor").toLocked();

 /**
   * Search parameter: <b>category</b>
   * <p>
   * Description: <b>Classification of the consent statement - for indexing/retrieval</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Consent.category</b><br>
   * </p>
   */
  @SearchParamDefinition(name="category", path="Consent.category", description="Classification of the consent statement - for indexing/retrieval", type="token" )
  public static final String SP_CATEGORY = "category";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>category</b>
   * <p>
   * Description: <b>Classification of the consent statement - for indexing/retrieval</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Consent.category</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CATEGORY = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CATEGORY);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>draft | proposed | active | rejected | inactive | entered-in-error</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Consent.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="Consent.status", description="draft | proposed | active | rejected | inactive | entered-in-error", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>draft | proposed | active | rejected | inactive | entered-in-error</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Consent.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

