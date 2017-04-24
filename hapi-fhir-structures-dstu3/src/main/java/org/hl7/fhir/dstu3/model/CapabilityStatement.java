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
 * A Capability Statement documents a set of capabilities (behaviors) of a FHIR Server that may be used as a statement of actual server functionality or a statement of required or desired server implementation.
 */
@ResourceDef(name="CapabilityStatement", profile="http://hl7.org/fhir/Profile/CapabilityStatement")
@ChildOrder(names={"url", "version", "name", "title", "status", "experimental", "date", "publisher", "contact", "description", "useContext", "jurisdiction", "purpose", "copyright", "kind", "instantiates", "software", "implementation", "fhirVersion", "acceptUnknown", "format", "patchFormat", "implementationGuide", "profile", "rest", "messaging", "document"})
public class CapabilityStatement extends MetadataResource implements IBaseConformance {

    public enum CapabilityStatementKind {
        /**
         * The CapabilityStatement instance represents the present capabilities of a specific system instance.  This is the kind returned by OPTIONS for a FHIR server end-point.
         */
        INSTANCE, 
        /**
         * The CapabilityStatement instance represents the capabilities of a system or piece of software, independent of a particular installation.
         */
        CAPABILITY, 
        /**
         * The CapabilityStatement instance represents a set of requirements for other systems to meet; e.g. as part of an implementation guide or 'request for proposal'.
         */
        REQUIREMENTS, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static CapabilityStatementKind fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("instance".equals(codeString))
          return INSTANCE;
        if ("capability".equals(codeString))
          return CAPABILITY;
        if ("requirements".equals(codeString))
          return REQUIREMENTS;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown CapabilityStatementKind code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case INSTANCE: return "instance";
            case CAPABILITY: return "capability";
            case REQUIREMENTS: return "requirements";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case INSTANCE: return "http://hl7.org/fhir/capability-statement-kind";
            case CAPABILITY: return "http://hl7.org/fhir/capability-statement-kind";
            case REQUIREMENTS: return "http://hl7.org/fhir/capability-statement-kind";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case INSTANCE: return "The CapabilityStatement instance represents the present capabilities of a specific system instance.  This is the kind returned by OPTIONS for a FHIR server end-point.";
            case CAPABILITY: return "The CapabilityStatement instance represents the capabilities of a system or piece of software, independent of a particular installation.";
            case REQUIREMENTS: return "The CapabilityStatement instance represents a set of requirements for other systems to meet; e.g. as part of an implementation guide or 'request for proposal'.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INSTANCE: return "Instance";
            case CAPABILITY: return "Capability";
            case REQUIREMENTS: return "Requirements";
            default: return "?";
          }
        }
    }

  public static class CapabilityStatementKindEnumFactory implements EnumFactory<CapabilityStatementKind> {
    public CapabilityStatementKind fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("instance".equals(codeString))
          return CapabilityStatementKind.INSTANCE;
        if ("capability".equals(codeString))
          return CapabilityStatementKind.CAPABILITY;
        if ("requirements".equals(codeString))
          return CapabilityStatementKind.REQUIREMENTS;
        throw new IllegalArgumentException("Unknown CapabilityStatementKind code '"+codeString+"'");
        }
        public Enumeration<CapabilityStatementKind> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<CapabilityStatementKind>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("instance".equals(codeString))
          return new Enumeration<CapabilityStatementKind>(this, CapabilityStatementKind.INSTANCE);
        if ("capability".equals(codeString))
          return new Enumeration<CapabilityStatementKind>(this, CapabilityStatementKind.CAPABILITY);
        if ("requirements".equals(codeString))
          return new Enumeration<CapabilityStatementKind>(this, CapabilityStatementKind.REQUIREMENTS);
        throw new FHIRException("Unknown CapabilityStatementKind code '"+codeString+"'");
        }
    public String toCode(CapabilityStatementKind code) {
      if (code == CapabilityStatementKind.INSTANCE)
        return "instance";
      if (code == CapabilityStatementKind.CAPABILITY)
        return "capability";
      if (code == CapabilityStatementKind.REQUIREMENTS)
        return "requirements";
      return "?";
      }
    public String toSystem(CapabilityStatementKind code) {
      return code.getSystem();
      }
    }

    public enum UnknownContentCode {
        /**
         * The application does not accept either unknown elements or extensions.
         */
        NO, 
        /**
         * The application accepts unknown extensions, but not unknown elements.
         */
        EXTENSIONS, 
        /**
         * The application accepts unknown elements, but not unknown extensions.
         */
        ELEMENTS, 
        /**
         * The application accepts unknown elements and extensions.
         */
        BOTH, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static UnknownContentCode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("no".equals(codeString))
          return NO;
        if ("extensions".equals(codeString))
          return EXTENSIONS;
        if ("elements".equals(codeString))
          return ELEMENTS;
        if ("both".equals(codeString))
          return BOTH;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown UnknownContentCode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case NO: return "no";
            case EXTENSIONS: return "extensions";
            case ELEMENTS: return "elements";
            case BOTH: return "both";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case NO: return "http://hl7.org/fhir/unknown-content-code";
            case EXTENSIONS: return "http://hl7.org/fhir/unknown-content-code";
            case ELEMENTS: return "http://hl7.org/fhir/unknown-content-code";
            case BOTH: return "http://hl7.org/fhir/unknown-content-code";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case NO: return "The application does not accept either unknown elements or extensions.";
            case EXTENSIONS: return "The application accepts unknown extensions, but not unknown elements.";
            case ELEMENTS: return "The application accepts unknown elements, but not unknown extensions.";
            case BOTH: return "The application accepts unknown elements and extensions.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case NO: return "Neither Elements or Extensions";
            case EXTENSIONS: return "Unknown Extensions";
            case ELEMENTS: return "Unknown Elements";
            case BOTH: return "Unknown Elements and Extensions";
            default: return "?";
          }
        }
    }

  public static class UnknownContentCodeEnumFactory implements EnumFactory<UnknownContentCode> {
    public UnknownContentCode fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("no".equals(codeString))
          return UnknownContentCode.NO;
        if ("extensions".equals(codeString))
          return UnknownContentCode.EXTENSIONS;
        if ("elements".equals(codeString))
          return UnknownContentCode.ELEMENTS;
        if ("both".equals(codeString))
          return UnknownContentCode.BOTH;
        throw new IllegalArgumentException("Unknown UnknownContentCode code '"+codeString+"'");
        }
        public Enumeration<UnknownContentCode> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<UnknownContentCode>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("no".equals(codeString))
          return new Enumeration<UnknownContentCode>(this, UnknownContentCode.NO);
        if ("extensions".equals(codeString))
          return new Enumeration<UnknownContentCode>(this, UnknownContentCode.EXTENSIONS);
        if ("elements".equals(codeString))
          return new Enumeration<UnknownContentCode>(this, UnknownContentCode.ELEMENTS);
        if ("both".equals(codeString))
          return new Enumeration<UnknownContentCode>(this, UnknownContentCode.BOTH);
        throw new FHIRException("Unknown UnknownContentCode code '"+codeString+"'");
        }
    public String toCode(UnknownContentCode code) {
      if (code == UnknownContentCode.NO)
        return "no";
      if (code == UnknownContentCode.EXTENSIONS)
        return "extensions";
      if (code == UnknownContentCode.ELEMENTS)
        return "elements";
      if (code == UnknownContentCode.BOTH)
        return "both";
      return "?";
      }
    public String toSystem(UnknownContentCode code) {
      return code.getSystem();
      }
    }

    public enum RestfulCapabilityMode {
        /**
         * The application acts as a client for this resource.
         */
        CLIENT, 
        /**
         * The application acts as a server for this resource.
         */
        SERVER, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static RestfulCapabilityMode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("client".equals(codeString))
          return CLIENT;
        if ("server".equals(codeString))
          return SERVER;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown RestfulCapabilityMode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CLIENT: return "client";
            case SERVER: return "server";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case CLIENT: return "http://hl7.org/fhir/restful-capability-mode";
            case SERVER: return "http://hl7.org/fhir/restful-capability-mode";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case CLIENT: return "The application acts as a client for this resource.";
            case SERVER: return "The application acts as a server for this resource.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CLIENT: return "Client";
            case SERVER: return "Server";
            default: return "?";
          }
        }
    }

  public static class RestfulCapabilityModeEnumFactory implements EnumFactory<RestfulCapabilityMode> {
    public RestfulCapabilityMode fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("client".equals(codeString))
          return RestfulCapabilityMode.CLIENT;
        if ("server".equals(codeString))
          return RestfulCapabilityMode.SERVER;
        throw new IllegalArgumentException("Unknown RestfulCapabilityMode code '"+codeString+"'");
        }
        public Enumeration<RestfulCapabilityMode> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<RestfulCapabilityMode>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("client".equals(codeString))
          return new Enumeration<RestfulCapabilityMode>(this, RestfulCapabilityMode.CLIENT);
        if ("server".equals(codeString))
          return new Enumeration<RestfulCapabilityMode>(this, RestfulCapabilityMode.SERVER);
        throw new FHIRException("Unknown RestfulCapabilityMode code '"+codeString+"'");
        }
    public String toCode(RestfulCapabilityMode code) {
      if (code == RestfulCapabilityMode.CLIENT)
        return "client";
      if (code == RestfulCapabilityMode.SERVER)
        return "server";
      return "?";
      }
    public String toSystem(RestfulCapabilityMode code) {
      return code.getSystem();
      }
    }

    public enum TypeRestfulInteraction {
        /**
         * null
         */
        READ, 
        /**
         * null
         */
        VREAD, 
        /**
         * null
         */
        UPDATE, 
        /**
         * null
         */
        PATCH, 
        /**
         * null
         */
        DELETE, 
        /**
         * null
         */
        HISTORYINSTANCE, 
        /**
         * null
         */
        HISTORYTYPE, 
        /**
         * null
         */
        CREATE, 
        /**
         * null
         */
        SEARCHTYPE, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static TypeRestfulInteraction fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("read".equals(codeString))
          return READ;
        if ("vread".equals(codeString))
          return VREAD;
        if ("update".equals(codeString))
          return UPDATE;
        if ("patch".equals(codeString))
          return PATCH;
        if ("delete".equals(codeString))
          return DELETE;
        if ("history-instance".equals(codeString))
          return HISTORYINSTANCE;
        if ("history-type".equals(codeString))
          return HISTORYTYPE;
        if ("create".equals(codeString))
          return CREATE;
        if ("search-type".equals(codeString))
          return SEARCHTYPE;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown TypeRestfulInteraction code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case READ: return "read";
            case VREAD: return "vread";
            case UPDATE: return "update";
            case PATCH: return "patch";
            case DELETE: return "delete";
            case HISTORYINSTANCE: return "history-instance";
            case HISTORYTYPE: return "history-type";
            case CREATE: return "create";
            case SEARCHTYPE: return "search-type";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case READ: return "http://hl7.org/fhir/restful-interaction";
            case VREAD: return "http://hl7.org/fhir/restful-interaction";
            case UPDATE: return "http://hl7.org/fhir/restful-interaction";
            case PATCH: return "http://hl7.org/fhir/restful-interaction";
            case DELETE: return "http://hl7.org/fhir/restful-interaction";
            case HISTORYINSTANCE: return "http://hl7.org/fhir/restful-interaction";
            case HISTORYTYPE: return "http://hl7.org/fhir/restful-interaction";
            case CREATE: return "http://hl7.org/fhir/restful-interaction";
            case SEARCHTYPE: return "http://hl7.org/fhir/restful-interaction";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case READ: return "";
            case VREAD: return "";
            case UPDATE: return "";
            case PATCH: return "";
            case DELETE: return "";
            case HISTORYINSTANCE: return "";
            case HISTORYTYPE: return "";
            case CREATE: return "";
            case SEARCHTYPE: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case READ: return "read";
            case VREAD: return "vread";
            case UPDATE: return "update";
            case PATCH: return "patch";
            case DELETE: return "delete";
            case HISTORYINSTANCE: return "history-instance";
            case HISTORYTYPE: return "history-type";
            case CREATE: return "create";
            case SEARCHTYPE: return "search-type";
            default: return "?";
          }
        }
    }

  public static class TypeRestfulInteractionEnumFactory implements EnumFactory<TypeRestfulInteraction> {
    public TypeRestfulInteraction fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("read".equals(codeString))
          return TypeRestfulInteraction.READ;
        if ("vread".equals(codeString))
          return TypeRestfulInteraction.VREAD;
        if ("update".equals(codeString))
          return TypeRestfulInteraction.UPDATE;
        if ("patch".equals(codeString))
          return TypeRestfulInteraction.PATCH;
        if ("delete".equals(codeString))
          return TypeRestfulInteraction.DELETE;
        if ("history-instance".equals(codeString))
          return TypeRestfulInteraction.HISTORYINSTANCE;
        if ("history-type".equals(codeString))
          return TypeRestfulInteraction.HISTORYTYPE;
        if ("create".equals(codeString))
          return TypeRestfulInteraction.CREATE;
        if ("search-type".equals(codeString))
          return TypeRestfulInteraction.SEARCHTYPE;
        throw new IllegalArgumentException("Unknown TypeRestfulInteraction code '"+codeString+"'");
        }
        public Enumeration<TypeRestfulInteraction> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<TypeRestfulInteraction>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("read".equals(codeString))
          return new Enumeration<TypeRestfulInteraction>(this, TypeRestfulInteraction.READ);
        if ("vread".equals(codeString))
          return new Enumeration<TypeRestfulInteraction>(this, TypeRestfulInteraction.VREAD);
        if ("update".equals(codeString))
          return new Enumeration<TypeRestfulInteraction>(this, TypeRestfulInteraction.UPDATE);
        if ("patch".equals(codeString))
          return new Enumeration<TypeRestfulInteraction>(this, TypeRestfulInteraction.PATCH);
        if ("delete".equals(codeString))
          return new Enumeration<TypeRestfulInteraction>(this, TypeRestfulInteraction.DELETE);
        if ("history-instance".equals(codeString))
          return new Enumeration<TypeRestfulInteraction>(this, TypeRestfulInteraction.HISTORYINSTANCE);
        if ("history-type".equals(codeString))
          return new Enumeration<TypeRestfulInteraction>(this, TypeRestfulInteraction.HISTORYTYPE);
        if ("create".equals(codeString))
          return new Enumeration<TypeRestfulInteraction>(this, TypeRestfulInteraction.CREATE);
        if ("search-type".equals(codeString))
          return new Enumeration<TypeRestfulInteraction>(this, TypeRestfulInteraction.SEARCHTYPE);
        throw new FHIRException("Unknown TypeRestfulInteraction code '"+codeString+"'");
        }
    public String toCode(TypeRestfulInteraction code) {
      if (code == TypeRestfulInteraction.READ)
        return "read";
      if (code == TypeRestfulInteraction.VREAD)
        return "vread";
      if (code == TypeRestfulInteraction.UPDATE)
        return "update";
      if (code == TypeRestfulInteraction.PATCH)
        return "patch";
      if (code == TypeRestfulInteraction.DELETE)
        return "delete";
      if (code == TypeRestfulInteraction.HISTORYINSTANCE)
        return "history-instance";
      if (code == TypeRestfulInteraction.HISTORYTYPE)
        return "history-type";
      if (code == TypeRestfulInteraction.CREATE)
        return "create";
      if (code == TypeRestfulInteraction.SEARCHTYPE)
        return "search-type";
      return "?";
      }
    public String toSystem(TypeRestfulInteraction code) {
      return code.getSystem();
      }
    }

    public enum ResourceVersionPolicy {
        /**
         * VersionId meta-property is not supported (server) or used (client).
         */
        NOVERSION, 
        /**
         * VersionId meta-property is supported (server) or used (client).
         */
        VERSIONED, 
        /**
         * VersionId must be correct for updates (server) or will be specified (If-match header) for updates (client).
         */
        VERSIONEDUPDATE, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ResourceVersionPolicy fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("no-version".equals(codeString))
          return NOVERSION;
        if ("versioned".equals(codeString))
          return VERSIONED;
        if ("versioned-update".equals(codeString))
          return VERSIONEDUPDATE;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ResourceVersionPolicy code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case NOVERSION: return "no-version";
            case VERSIONED: return "versioned";
            case VERSIONEDUPDATE: return "versioned-update";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case NOVERSION: return "http://hl7.org/fhir/versioning-policy";
            case VERSIONED: return "http://hl7.org/fhir/versioning-policy";
            case VERSIONEDUPDATE: return "http://hl7.org/fhir/versioning-policy";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case NOVERSION: return "VersionId meta-property is not supported (server) or used (client).";
            case VERSIONED: return "VersionId meta-property is supported (server) or used (client).";
            case VERSIONEDUPDATE: return "VersionId must be correct for updates (server) or will be specified (If-match header) for updates (client).";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case NOVERSION: return "No VersionId Support";
            case VERSIONED: return "Versioned";
            case VERSIONEDUPDATE: return "VersionId tracked fully";
            default: return "?";
          }
        }
    }

  public static class ResourceVersionPolicyEnumFactory implements EnumFactory<ResourceVersionPolicy> {
    public ResourceVersionPolicy fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("no-version".equals(codeString))
          return ResourceVersionPolicy.NOVERSION;
        if ("versioned".equals(codeString))
          return ResourceVersionPolicy.VERSIONED;
        if ("versioned-update".equals(codeString))
          return ResourceVersionPolicy.VERSIONEDUPDATE;
        throw new IllegalArgumentException("Unknown ResourceVersionPolicy code '"+codeString+"'");
        }
        public Enumeration<ResourceVersionPolicy> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ResourceVersionPolicy>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("no-version".equals(codeString))
          return new Enumeration<ResourceVersionPolicy>(this, ResourceVersionPolicy.NOVERSION);
        if ("versioned".equals(codeString))
          return new Enumeration<ResourceVersionPolicy>(this, ResourceVersionPolicy.VERSIONED);
        if ("versioned-update".equals(codeString))
          return new Enumeration<ResourceVersionPolicy>(this, ResourceVersionPolicy.VERSIONEDUPDATE);
        throw new FHIRException("Unknown ResourceVersionPolicy code '"+codeString+"'");
        }
    public String toCode(ResourceVersionPolicy code) {
      if (code == ResourceVersionPolicy.NOVERSION)
        return "no-version";
      if (code == ResourceVersionPolicy.VERSIONED)
        return "versioned";
      if (code == ResourceVersionPolicy.VERSIONEDUPDATE)
        return "versioned-update";
      return "?";
      }
    public String toSystem(ResourceVersionPolicy code) {
      return code.getSystem();
      }
    }

    public enum ConditionalReadStatus {
        /**
         * No support for conditional deletes.
         */
        NOTSUPPORTED, 
        /**
         * Conditional reads are supported, but only with the If-Modified-Since HTTP Header.
         */
        MODIFIEDSINCE, 
        /**
         * Conditional reads are supported, but only with the If-None-Match HTTP Header.
         */
        NOTMATCH, 
        /**
         * Conditional reads are supported, with both If-Modified-Since and If-None-Match HTTP Headers.
         */
        FULLSUPPORT, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ConditionalReadStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("not-supported".equals(codeString))
          return NOTSUPPORTED;
        if ("modified-since".equals(codeString))
          return MODIFIEDSINCE;
        if ("not-match".equals(codeString))
          return NOTMATCH;
        if ("full-support".equals(codeString))
          return FULLSUPPORT;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ConditionalReadStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case NOTSUPPORTED: return "not-supported";
            case MODIFIEDSINCE: return "modified-since";
            case NOTMATCH: return "not-match";
            case FULLSUPPORT: return "full-support";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case NOTSUPPORTED: return "http://hl7.org/fhir/conditional-read-status";
            case MODIFIEDSINCE: return "http://hl7.org/fhir/conditional-read-status";
            case NOTMATCH: return "http://hl7.org/fhir/conditional-read-status";
            case FULLSUPPORT: return "http://hl7.org/fhir/conditional-read-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case NOTSUPPORTED: return "No support for conditional deletes.";
            case MODIFIEDSINCE: return "Conditional reads are supported, but only with the If-Modified-Since HTTP Header.";
            case NOTMATCH: return "Conditional reads are supported, but only with the If-None-Match HTTP Header.";
            case FULLSUPPORT: return "Conditional reads are supported, with both If-Modified-Since and If-None-Match HTTP Headers.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case NOTSUPPORTED: return "Not Supported";
            case MODIFIEDSINCE: return "If-Modified-Since";
            case NOTMATCH: return "If-None-Match";
            case FULLSUPPORT: return "Full Support";
            default: return "?";
          }
        }
    }

  public static class ConditionalReadStatusEnumFactory implements EnumFactory<ConditionalReadStatus> {
    public ConditionalReadStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("not-supported".equals(codeString))
          return ConditionalReadStatus.NOTSUPPORTED;
        if ("modified-since".equals(codeString))
          return ConditionalReadStatus.MODIFIEDSINCE;
        if ("not-match".equals(codeString))
          return ConditionalReadStatus.NOTMATCH;
        if ("full-support".equals(codeString))
          return ConditionalReadStatus.FULLSUPPORT;
        throw new IllegalArgumentException("Unknown ConditionalReadStatus code '"+codeString+"'");
        }
        public Enumeration<ConditionalReadStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ConditionalReadStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("not-supported".equals(codeString))
          return new Enumeration<ConditionalReadStatus>(this, ConditionalReadStatus.NOTSUPPORTED);
        if ("modified-since".equals(codeString))
          return new Enumeration<ConditionalReadStatus>(this, ConditionalReadStatus.MODIFIEDSINCE);
        if ("not-match".equals(codeString))
          return new Enumeration<ConditionalReadStatus>(this, ConditionalReadStatus.NOTMATCH);
        if ("full-support".equals(codeString))
          return new Enumeration<ConditionalReadStatus>(this, ConditionalReadStatus.FULLSUPPORT);
        throw new FHIRException("Unknown ConditionalReadStatus code '"+codeString+"'");
        }
    public String toCode(ConditionalReadStatus code) {
      if (code == ConditionalReadStatus.NOTSUPPORTED)
        return "not-supported";
      if (code == ConditionalReadStatus.MODIFIEDSINCE)
        return "modified-since";
      if (code == ConditionalReadStatus.NOTMATCH)
        return "not-match";
      if (code == ConditionalReadStatus.FULLSUPPORT)
        return "full-support";
      return "?";
      }
    public String toSystem(ConditionalReadStatus code) {
      return code.getSystem();
      }
    }

    public enum ConditionalDeleteStatus {
        /**
         * No support for conditional deletes.
         */
        NOTSUPPORTED, 
        /**
         * Conditional deletes are supported, but only single resources at a time.
         */
        SINGLE, 
        /**
         * Conditional deletes are supported, and multiple resources can be deleted in a single interaction.
         */
        MULTIPLE, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ConditionalDeleteStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("not-supported".equals(codeString))
          return NOTSUPPORTED;
        if ("single".equals(codeString))
          return SINGLE;
        if ("multiple".equals(codeString))
          return MULTIPLE;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ConditionalDeleteStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case NOTSUPPORTED: return "not-supported";
            case SINGLE: return "single";
            case MULTIPLE: return "multiple";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case NOTSUPPORTED: return "http://hl7.org/fhir/conditional-delete-status";
            case SINGLE: return "http://hl7.org/fhir/conditional-delete-status";
            case MULTIPLE: return "http://hl7.org/fhir/conditional-delete-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case NOTSUPPORTED: return "No support for conditional deletes.";
            case SINGLE: return "Conditional deletes are supported, but only single resources at a time.";
            case MULTIPLE: return "Conditional deletes are supported, and multiple resources can be deleted in a single interaction.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case NOTSUPPORTED: return "Not Supported";
            case SINGLE: return "Single Deletes Supported";
            case MULTIPLE: return "Multiple Deletes Supported";
            default: return "?";
          }
        }
    }

  public static class ConditionalDeleteStatusEnumFactory implements EnumFactory<ConditionalDeleteStatus> {
    public ConditionalDeleteStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("not-supported".equals(codeString))
          return ConditionalDeleteStatus.NOTSUPPORTED;
        if ("single".equals(codeString))
          return ConditionalDeleteStatus.SINGLE;
        if ("multiple".equals(codeString))
          return ConditionalDeleteStatus.MULTIPLE;
        throw new IllegalArgumentException("Unknown ConditionalDeleteStatus code '"+codeString+"'");
        }
        public Enumeration<ConditionalDeleteStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ConditionalDeleteStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("not-supported".equals(codeString))
          return new Enumeration<ConditionalDeleteStatus>(this, ConditionalDeleteStatus.NOTSUPPORTED);
        if ("single".equals(codeString))
          return new Enumeration<ConditionalDeleteStatus>(this, ConditionalDeleteStatus.SINGLE);
        if ("multiple".equals(codeString))
          return new Enumeration<ConditionalDeleteStatus>(this, ConditionalDeleteStatus.MULTIPLE);
        throw new FHIRException("Unknown ConditionalDeleteStatus code '"+codeString+"'");
        }
    public String toCode(ConditionalDeleteStatus code) {
      if (code == ConditionalDeleteStatus.NOTSUPPORTED)
        return "not-supported";
      if (code == ConditionalDeleteStatus.SINGLE)
        return "single";
      if (code == ConditionalDeleteStatus.MULTIPLE)
        return "multiple";
      return "?";
      }
    public String toSystem(ConditionalDeleteStatus code) {
      return code.getSystem();
      }
    }

    public enum ReferenceHandlingPolicy {
        /**
         * The server supports and populates Literal references where they are known (this code does not guarantee that all references are literal; see 'enforced')
         */
        LITERAL, 
        /**
         * The server allows logical references
         */
        LOGICAL, 
        /**
         * The server will attempt to resolve logical references to literal references (if resolution fails, the server may still accept resources; see logical)
         */
        RESOLVES, 
        /**
         * The server enforces that references have integrity - e.g. it ensures that references can always be resolved. This is typically the case for clinical record systems, but often not the case for middleware/proxy systems
         */
        ENFORCED, 
        /**
         * The server does not support references that point to other servers
         */
        LOCAL, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ReferenceHandlingPolicy fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("literal".equals(codeString))
          return LITERAL;
        if ("logical".equals(codeString))
          return LOGICAL;
        if ("resolves".equals(codeString))
          return RESOLVES;
        if ("enforced".equals(codeString))
          return ENFORCED;
        if ("local".equals(codeString))
          return LOCAL;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ReferenceHandlingPolicy code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case LITERAL: return "literal";
            case LOGICAL: return "logical";
            case RESOLVES: return "resolves";
            case ENFORCED: return "enforced";
            case LOCAL: return "local";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case LITERAL: return "http://hl7.org/fhir/reference-handling-policy";
            case LOGICAL: return "http://hl7.org/fhir/reference-handling-policy";
            case RESOLVES: return "http://hl7.org/fhir/reference-handling-policy";
            case ENFORCED: return "http://hl7.org/fhir/reference-handling-policy";
            case LOCAL: return "http://hl7.org/fhir/reference-handling-policy";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case LITERAL: return "The server supports and populates Literal references where they are known (this code does not guarantee that all references are literal; see 'enforced')";
            case LOGICAL: return "The server allows logical references";
            case RESOLVES: return "The server will attempt to resolve logical references to literal references (if resolution fails, the server may still accept resources; see logical)";
            case ENFORCED: return "The server enforces that references have integrity - e.g. it ensures that references can always be resolved. This is typically the case for clinical record systems, but often not the case for middleware/proxy systems";
            case LOCAL: return "The server does not support references that point to other servers";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case LITERAL: return "Literal References";
            case LOGICAL: return "Logical References";
            case RESOLVES: return "Resolves References";
            case ENFORCED: return "Reference Integrity Enforced";
            case LOCAL: return "Local References Only";
            default: return "?";
          }
        }
    }

  public static class ReferenceHandlingPolicyEnumFactory implements EnumFactory<ReferenceHandlingPolicy> {
    public ReferenceHandlingPolicy fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("literal".equals(codeString))
          return ReferenceHandlingPolicy.LITERAL;
        if ("logical".equals(codeString))
          return ReferenceHandlingPolicy.LOGICAL;
        if ("resolves".equals(codeString))
          return ReferenceHandlingPolicy.RESOLVES;
        if ("enforced".equals(codeString))
          return ReferenceHandlingPolicy.ENFORCED;
        if ("local".equals(codeString))
          return ReferenceHandlingPolicy.LOCAL;
        throw new IllegalArgumentException("Unknown ReferenceHandlingPolicy code '"+codeString+"'");
        }
        public Enumeration<ReferenceHandlingPolicy> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ReferenceHandlingPolicy>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("literal".equals(codeString))
          return new Enumeration<ReferenceHandlingPolicy>(this, ReferenceHandlingPolicy.LITERAL);
        if ("logical".equals(codeString))
          return new Enumeration<ReferenceHandlingPolicy>(this, ReferenceHandlingPolicy.LOGICAL);
        if ("resolves".equals(codeString))
          return new Enumeration<ReferenceHandlingPolicy>(this, ReferenceHandlingPolicy.RESOLVES);
        if ("enforced".equals(codeString))
          return new Enumeration<ReferenceHandlingPolicy>(this, ReferenceHandlingPolicy.ENFORCED);
        if ("local".equals(codeString))
          return new Enumeration<ReferenceHandlingPolicy>(this, ReferenceHandlingPolicy.LOCAL);
        throw new FHIRException("Unknown ReferenceHandlingPolicy code '"+codeString+"'");
        }
    public String toCode(ReferenceHandlingPolicy code) {
      if (code == ReferenceHandlingPolicy.LITERAL)
        return "literal";
      if (code == ReferenceHandlingPolicy.LOGICAL)
        return "logical";
      if (code == ReferenceHandlingPolicy.RESOLVES)
        return "resolves";
      if (code == ReferenceHandlingPolicy.ENFORCED)
        return "enforced";
      if (code == ReferenceHandlingPolicy.LOCAL)
        return "local";
      return "?";
      }
    public String toSystem(ReferenceHandlingPolicy code) {
      return code.getSystem();
      }
    }

    public enum SystemRestfulInteraction {
        /**
         * null
         */
        TRANSACTION, 
        /**
         * null
         */
        BATCH, 
        /**
         * null
         */
        SEARCHSYSTEM, 
        /**
         * null
         */
        HISTORYSYSTEM, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static SystemRestfulInteraction fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("transaction".equals(codeString))
          return TRANSACTION;
        if ("batch".equals(codeString))
          return BATCH;
        if ("search-system".equals(codeString))
          return SEARCHSYSTEM;
        if ("history-system".equals(codeString))
          return HISTORYSYSTEM;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown SystemRestfulInteraction code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case TRANSACTION: return "transaction";
            case BATCH: return "batch";
            case SEARCHSYSTEM: return "search-system";
            case HISTORYSYSTEM: return "history-system";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case TRANSACTION: return "http://hl7.org/fhir/restful-interaction";
            case BATCH: return "http://hl7.org/fhir/restful-interaction";
            case SEARCHSYSTEM: return "http://hl7.org/fhir/restful-interaction";
            case HISTORYSYSTEM: return "http://hl7.org/fhir/restful-interaction";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case TRANSACTION: return "";
            case BATCH: return "";
            case SEARCHSYSTEM: return "";
            case HISTORYSYSTEM: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case TRANSACTION: return "transaction";
            case BATCH: return "batch";
            case SEARCHSYSTEM: return "search-system";
            case HISTORYSYSTEM: return "history-system";
            default: return "?";
          }
        }
    }

  public static class SystemRestfulInteractionEnumFactory implements EnumFactory<SystemRestfulInteraction> {
    public SystemRestfulInteraction fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("transaction".equals(codeString))
          return SystemRestfulInteraction.TRANSACTION;
        if ("batch".equals(codeString))
          return SystemRestfulInteraction.BATCH;
        if ("search-system".equals(codeString))
          return SystemRestfulInteraction.SEARCHSYSTEM;
        if ("history-system".equals(codeString))
          return SystemRestfulInteraction.HISTORYSYSTEM;
        throw new IllegalArgumentException("Unknown SystemRestfulInteraction code '"+codeString+"'");
        }
        public Enumeration<SystemRestfulInteraction> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<SystemRestfulInteraction>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("transaction".equals(codeString))
          return new Enumeration<SystemRestfulInteraction>(this, SystemRestfulInteraction.TRANSACTION);
        if ("batch".equals(codeString))
          return new Enumeration<SystemRestfulInteraction>(this, SystemRestfulInteraction.BATCH);
        if ("search-system".equals(codeString))
          return new Enumeration<SystemRestfulInteraction>(this, SystemRestfulInteraction.SEARCHSYSTEM);
        if ("history-system".equals(codeString))
          return new Enumeration<SystemRestfulInteraction>(this, SystemRestfulInteraction.HISTORYSYSTEM);
        throw new FHIRException("Unknown SystemRestfulInteraction code '"+codeString+"'");
        }
    public String toCode(SystemRestfulInteraction code) {
      if (code == SystemRestfulInteraction.TRANSACTION)
        return "transaction";
      if (code == SystemRestfulInteraction.BATCH)
        return "batch";
      if (code == SystemRestfulInteraction.SEARCHSYSTEM)
        return "search-system";
      if (code == SystemRestfulInteraction.HISTORYSYSTEM)
        return "history-system";
      return "?";
      }
    public String toSystem(SystemRestfulInteraction code) {
      return code.getSystem();
      }
    }

    public enum EventCapabilityMode {
        /**
         * The application sends requests and receives responses.
         */
        SENDER, 
        /**
         * The application receives requests and sends responses.
         */
        RECEIVER, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static EventCapabilityMode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("sender".equals(codeString))
          return SENDER;
        if ("receiver".equals(codeString))
          return RECEIVER;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown EventCapabilityMode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case SENDER: return "sender";
            case RECEIVER: return "receiver";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case SENDER: return "http://hl7.org/fhir/event-capability-mode";
            case RECEIVER: return "http://hl7.org/fhir/event-capability-mode";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case SENDER: return "The application sends requests and receives responses.";
            case RECEIVER: return "The application receives requests and sends responses.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case SENDER: return "Sender";
            case RECEIVER: return "Receiver";
            default: return "?";
          }
        }
    }

  public static class EventCapabilityModeEnumFactory implements EnumFactory<EventCapabilityMode> {
    public EventCapabilityMode fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("sender".equals(codeString))
          return EventCapabilityMode.SENDER;
        if ("receiver".equals(codeString))
          return EventCapabilityMode.RECEIVER;
        throw new IllegalArgumentException("Unknown EventCapabilityMode code '"+codeString+"'");
        }
        public Enumeration<EventCapabilityMode> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<EventCapabilityMode>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("sender".equals(codeString))
          return new Enumeration<EventCapabilityMode>(this, EventCapabilityMode.SENDER);
        if ("receiver".equals(codeString))
          return new Enumeration<EventCapabilityMode>(this, EventCapabilityMode.RECEIVER);
        throw new FHIRException("Unknown EventCapabilityMode code '"+codeString+"'");
        }
    public String toCode(EventCapabilityMode code) {
      if (code == EventCapabilityMode.SENDER)
        return "sender";
      if (code == EventCapabilityMode.RECEIVER)
        return "receiver";
      return "?";
      }
    public String toSystem(EventCapabilityMode code) {
      return code.getSystem();
      }
    }

    public enum MessageSignificanceCategory {
        /**
         * The message represents/requests a change that should not be processed more than once; e.g., making a booking for an appointment.
         */
        CONSEQUENCE, 
        /**
         * The message represents a response to query for current information. Retrospective processing is wrong and/or wasteful.
         */
        CURRENCY, 
        /**
         * The content is not necessarily intended to be current, and it can be reprocessed, though there may be version issues created by processing old notifications.
         */
        NOTIFICATION, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static MessageSignificanceCategory fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("Consequence".equals(codeString))
          return CONSEQUENCE;
        if ("Currency".equals(codeString))
          return CURRENCY;
        if ("Notification".equals(codeString))
          return NOTIFICATION;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown MessageSignificanceCategory code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CONSEQUENCE: return "Consequence";
            case CURRENCY: return "Currency";
            case NOTIFICATION: return "Notification";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case CONSEQUENCE: return "http://hl7.org/fhir/message-significance-category";
            case CURRENCY: return "http://hl7.org/fhir/message-significance-category";
            case NOTIFICATION: return "http://hl7.org/fhir/message-significance-category";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case CONSEQUENCE: return "The message represents/requests a change that should not be processed more than once; e.g., making a booking for an appointment.";
            case CURRENCY: return "The message represents a response to query for current information. Retrospective processing is wrong and/or wasteful.";
            case NOTIFICATION: return "The content is not necessarily intended to be current, and it can be reprocessed, though there may be version issues created by processing old notifications.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CONSEQUENCE: return "Consequence";
            case CURRENCY: return "Currency";
            case NOTIFICATION: return "Notification";
            default: return "?";
          }
        }
    }

  public static class MessageSignificanceCategoryEnumFactory implements EnumFactory<MessageSignificanceCategory> {
    public MessageSignificanceCategory fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("Consequence".equals(codeString))
          return MessageSignificanceCategory.CONSEQUENCE;
        if ("Currency".equals(codeString))
          return MessageSignificanceCategory.CURRENCY;
        if ("Notification".equals(codeString))
          return MessageSignificanceCategory.NOTIFICATION;
        throw new IllegalArgumentException("Unknown MessageSignificanceCategory code '"+codeString+"'");
        }
        public Enumeration<MessageSignificanceCategory> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<MessageSignificanceCategory>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("Consequence".equals(codeString))
          return new Enumeration<MessageSignificanceCategory>(this, MessageSignificanceCategory.CONSEQUENCE);
        if ("Currency".equals(codeString))
          return new Enumeration<MessageSignificanceCategory>(this, MessageSignificanceCategory.CURRENCY);
        if ("Notification".equals(codeString))
          return new Enumeration<MessageSignificanceCategory>(this, MessageSignificanceCategory.NOTIFICATION);
        throw new FHIRException("Unknown MessageSignificanceCategory code '"+codeString+"'");
        }
    public String toCode(MessageSignificanceCategory code) {
      if (code == MessageSignificanceCategory.CONSEQUENCE)
        return "Consequence";
      if (code == MessageSignificanceCategory.CURRENCY)
        return "Currency";
      if (code == MessageSignificanceCategory.NOTIFICATION)
        return "Notification";
      return "?";
      }
    public String toSystem(MessageSignificanceCategory code) {
      return code.getSystem();
      }
    }

    public enum DocumentMode {
        /**
         * The application produces documents of the specified type.
         */
        PRODUCER, 
        /**
         * The application consumes documents of the specified type.
         */
        CONSUMER, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static DocumentMode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("producer".equals(codeString))
          return PRODUCER;
        if ("consumer".equals(codeString))
          return CONSUMER;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown DocumentMode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PRODUCER: return "producer";
            case CONSUMER: return "consumer";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PRODUCER: return "http://hl7.org/fhir/document-mode";
            case CONSUMER: return "http://hl7.org/fhir/document-mode";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PRODUCER: return "The application produces documents of the specified type.";
            case CONSUMER: return "The application consumes documents of the specified type.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PRODUCER: return "Producer";
            case CONSUMER: return "Consumer";
            default: return "?";
          }
        }
    }

  public static class DocumentModeEnumFactory implements EnumFactory<DocumentMode> {
    public DocumentMode fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("producer".equals(codeString))
          return DocumentMode.PRODUCER;
        if ("consumer".equals(codeString))
          return DocumentMode.CONSUMER;
        throw new IllegalArgumentException("Unknown DocumentMode code '"+codeString+"'");
        }
        public Enumeration<DocumentMode> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<DocumentMode>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("producer".equals(codeString))
          return new Enumeration<DocumentMode>(this, DocumentMode.PRODUCER);
        if ("consumer".equals(codeString))
          return new Enumeration<DocumentMode>(this, DocumentMode.CONSUMER);
        throw new FHIRException("Unknown DocumentMode code '"+codeString+"'");
        }
    public String toCode(DocumentMode code) {
      if (code == DocumentMode.PRODUCER)
        return "producer";
      if (code == DocumentMode.CONSUMER)
        return "consumer";
      return "?";
      }
    public String toSystem(DocumentMode code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class CapabilityStatementSoftwareComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Name software is known by.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="A name the software is known by", formalDefinition="Name software is known by." )
        protected StringType name;

        /**
         * The version identifier for the software covered by this statement.
         */
        @Child(name = "version", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Version covered by this statement", formalDefinition="The version identifier for the software covered by this statement." )
        protected StringType version;

        /**
         * Date this version of the software was released.
         */
        @Child(name = "releaseDate", type = {DateTimeType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Date this version released", formalDefinition="Date this version of the software was released." )
        protected DateTimeType releaseDate;

        private static final long serialVersionUID = 1819769027L;

    /**
     * Constructor
     */
      public CapabilityStatementSoftwareComponent() {
        super();
      }

    /**
     * Constructor
     */
      public CapabilityStatementSoftwareComponent(StringType name) {
        super();
        this.name = name;
      }

        /**
         * @return {@link #name} (Name software is known by.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementSoftwareComponent.name");
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
         * @param value {@link #name} (Name software is known by.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public CapabilityStatementSoftwareComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return Name software is known by.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value Name software is known by.
         */
        public CapabilityStatementSoftwareComponent setName(String value) { 
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          return this;
        }

        /**
         * @return {@link #version} (The version identifier for the software covered by this statement.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
         */
        public StringType getVersionElement() { 
          if (this.version == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementSoftwareComponent.version");
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
         * @param value {@link #version} (The version identifier for the software covered by this statement.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
         */
        public CapabilityStatementSoftwareComponent setVersionElement(StringType value) { 
          this.version = value;
          return this;
        }

        /**
         * @return The version identifier for the software covered by this statement.
         */
        public String getVersion() { 
          return this.version == null ? null : this.version.getValue();
        }

        /**
         * @param value The version identifier for the software covered by this statement.
         */
        public CapabilityStatementSoftwareComponent setVersion(String value) { 
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
         * @return {@link #releaseDate} (Date this version of the software was released.). This is the underlying object with id, value and extensions. The accessor "getReleaseDate" gives direct access to the value
         */
        public DateTimeType getReleaseDateElement() { 
          if (this.releaseDate == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementSoftwareComponent.releaseDate");
            else if (Configuration.doAutoCreate())
              this.releaseDate = new DateTimeType(); // bb
          return this.releaseDate;
        }

        public boolean hasReleaseDateElement() { 
          return this.releaseDate != null && !this.releaseDate.isEmpty();
        }

        public boolean hasReleaseDate() { 
          return this.releaseDate != null && !this.releaseDate.isEmpty();
        }

        /**
         * @param value {@link #releaseDate} (Date this version of the software was released.). This is the underlying object with id, value and extensions. The accessor "getReleaseDate" gives direct access to the value
         */
        public CapabilityStatementSoftwareComponent setReleaseDateElement(DateTimeType value) { 
          this.releaseDate = value;
          return this;
        }

        /**
         * @return Date this version of the software was released.
         */
        public Date getReleaseDate() { 
          return this.releaseDate == null ? null : this.releaseDate.getValue();
        }

        /**
         * @param value Date this version of the software was released.
         */
        public CapabilityStatementSoftwareComponent setReleaseDate(Date value) { 
          if (value == null)
            this.releaseDate = null;
          else {
            if (this.releaseDate == null)
              this.releaseDate = new DateTimeType();
            this.releaseDate.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "string", "Name software is known by.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("version", "string", "The version identifier for the software covered by this statement.", 0, java.lang.Integer.MAX_VALUE, version));
          childrenList.add(new Property("releaseDate", "dateTime", "Date this version of the software was released.", 0, java.lang.Integer.MAX_VALUE, releaseDate));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case 351608024: /*version*/ return this.version == null ? new Base[0] : new Base[] {this.version}; // StringType
        case 212873301: /*releaseDate*/ return this.releaseDate == null ? new Base[0] : new Base[] {this.releaseDate}; // DateTimeType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case 351608024: // version
          this.version = castToString(value); // StringType
          return value;
        case 212873301: // releaseDate
          this.releaseDate = castToDateTime(value); // DateTimeType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("version")) {
          this.version = castToString(value); // StringType
        } else if (name.equals("releaseDate")) {
          this.releaseDate = castToDateTime(value); // DateTimeType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707:  return getNameElement();
        case 351608024:  return getVersionElement();
        case 212873301:  return getReleaseDateElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return new String[] {"string"};
        case 351608024: /*version*/ return new String[] {"string"};
        case 212873301: /*releaseDate*/ return new String[] {"dateTime"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.name");
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.version");
        }
        else if (name.equals("releaseDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.releaseDate");
        }
        else
          return super.addChild(name);
      }

      public CapabilityStatementSoftwareComponent copy() {
        CapabilityStatementSoftwareComponent dst = new CapabilityStatementSoftwareComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.version = version == null ? null : version.copy();
        dst.releaseDate = releaseDate == null ? null : releaseDate.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CapabilityStatementSoftwareComponent))
          return false;
        CapabilityStatementSoftwareComponent o = (CapabilityStatementSoftwareComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(version, o.version, true) && compareDeep(releaseDate, o.releaseDate, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CapabilityStatementSoftwareComponent))
          return false;
        CapabilityStatementSoftwareComponent o = (CapabilityStatementSoftwareComponent) other;
        return compareValues(name, o.name, true) && compareValues(version, o.version, true) && compareValues(releaseDate, o.releaseDate, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(name, version, releaseDate
          );
      }

  public String fhirType() {
    return "CapabilityStatement.software";

  }

  }

    @Block()
    public static class CapabilityStatementImplementationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Information about the specific installation that this capability statement relates to.
         */
        @Child(name = "description", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Describes this specific instance", formalDefinition="Information about the specific installation that this capability statement relates to." )
        protected StringType description;

        /**
         * An absolute base URL for the implementation.  This forms the base for REST interfaces as well as the mailbox and document interfaces.
         */
        @Child(name = "url", type = {UriType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Base URL for the installation", formalDefinition="An absolute base URL for the implementation.  This forms the base for REST interfaces as well as the mailbox and document interfaces." )
        protected UriType url;

        private static final long serialVersionUID = -289238508L;

    /**
     * Constructor
     */
      public CapabilityStatementImplementationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public CapabilityStatementImplementationComponent(StringType description) {
        super();
        this.description = description;
      }

        /**
         * @return {@link #description} (Information about the specific installation that this capability statement relates to.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementImplementationComponent.description");
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
         * @param value {@link #description} (Information about the specific installation that this capability statement relates to.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public CapabilityStatementImplementationComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return Information about the specific installation that this capability statement relates to.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value Information about the specific installation that this capability statement relates to.
         */
        public CapabilityStatementImplementationComponent setDescription(String value) { 
            if (this.description == null)
              this.description = new StringType();
            this.description.setValue(value);
          return this;
        }

        /**
         * @return {@link #url} (An absolute base URL for the implementation.  This forms the base for REST interfaces as well as the mailbox and document interfaces.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
         */
        public UriType getUrlElement() { 
          if (this.url == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementImplementationComponent.url");
            else if (Configuration.doAutoCreate())
              this.url = new UriType(); // bb
          return this.url;
        }

        public boolean hasUrlElement() { 
          return this.url != null && !this.url.isEmpty();
        }

        public boolean hasUrl() { 
          return this.url != null && !this.url.isEmpty();
        }

        /**
         * @param value {@link #url} (An absolute base URL for the implementation.  This forms the base for REST interfaces as well as the mailbox and document interfaces.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
         */
        public CapabilityStatementImplementationComponent setUrlElement(UriType value) { 
          this.url = value;
          return this;
        }

        /**
         * @return An absolute base URL for the implementation.  This forms the base for REST interfaces as well as the mailbox and document interfaces.
         */
        public String getUrl() { 
          return this.url == null ? null : this.url.getValue();
        }

        /**
         * @param value An absolute base URL for the implementation.  This forms the base for REST interfaces as well as the mailbox and document interfaces.
         */
        public CapabilityStatementImplementationComponent setUrl(String value) { 
          if (Utilities.noString(value))
            this.url = null;
          else {
            if (this.url == null)
              this.url = new UriType();
            this.url.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("description", "string", "Information about the specific installation that this capability statement relates to.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("url", "uri", "An absolute base URL for the implementation.  This forms the base for REST interfaces as well as the mailbox and document interfaces.", 0, java.lang.Integer.MAX_VALUE, url));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        case 116079: // url
          this.url = castToUri(value); // UriType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else if (name.equals("url")) {
          this.url = castToUri(value); // UriType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1724546052:  return getDescriptionElement();
        case 116079:  return getUrlElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1724546052: /*description*/ return new String[] {"string"};
        case 116079: /*url*/ return new String[] {"uri"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.description");
        }
        else if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.url");
        }
        else
          return super.addChild(name);
      }

      public CapabilityStatementImplementationComponent copy() {
        CapabilityStatementImplementationComponent dst = new CapabilityStatementImplementationComponent();
        copyValues(dst);
        dst.description = description == null ? null : description.copy();
        dst.url = url == null ? null : url.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CapabilityStatementImplementationComponent))
          return false;
        CapabilityStatementImplementationComponent o = (CapabilityStatementImplementationComponent) other;
        return compareDeep(description, o.description, true) && compareDeep(url, o.url, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CapabilityStatementImplementationComponent))
          return false;
        CapabilityStatementImplementationComponent o = (CapabilityStatementImplementationComponent) other;
        return compareValues(description, o.description, true) && compareValues(url, o.url, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(description, url);
      }

  public String fhirType() {
    return "CapabilityStatement.implementation";

  }

  }

    @Block()
    public static class CapabilityStatementRestComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Identifies whether this portion of the statement is describing the ability to initiate or receive restful operations.
         */
        @Child(name = "mode", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="client | server", formalDefinition="Identifies whether this portion of the statement is describing the ability to initiate or receive restful operations." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/restful-capability-mode")
        protected Enumeration<RestfulCapabilityMode> mode;

        /**
         * Information about the system's restful capabilities that apply across all applications, such as security.
         */
        @Child(name = "documentation", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="General description of implementation", formalDefinition="Information about the system's restful capabilities that apply across all applications, such as security." )
        protected StringType documentation;

        /**
         * Information about security implementation from an interface perspective - what a client needs to know.
         */
        @Child(name = "security", type = {}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Information about security of implementation", formalDefinition="Information about security implementation from an interface perspective - what a client needs to know." )
        protected CapabilityStatementRestSecurityComponent security;

        /**
         * A specification of the restful capabilities of the solution for a specific resource type.
         */
        @Child(name = "resource", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Resource served on the REST interface", formalDefinition="A specification of the restful capabilities of the solution for a specific resource type." )
        protected List<CapabilityStatementRestResourceComponent> resource;

        /**
         * A specification of restful operations supported by the system.
         */
        @Child(name = "interaction", type = {}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="What operations are supported?", formalDefinition="A specification of restful operations supported by the system." )
        protected List<SystemInteractionComponent> interaction;

        /**
         * Search parameters that are supported for searching all resources for implementations to support and/or make use of - either references to ones defined in the specification, or additional ones defined for/by the implementation.
         */
        @Child(name = "searchParam", type = {CapabilityStatementRestResourceSearchParamComponent.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Search parameters for searching all resources", formalDefinition="Search parameters that are supported for searching all resources for implementations to support and/or make use of - either references to ones defined in the specification, or additional ones defined for/by the implementation." )
        protected List<CapabilityStatementRestResourceSearchParamComponent> searchParam;

        /**
         * Definition of an operation or a named query together with its parameters and their meaning and type.
         */
        @Child(name = "operation", type = {}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Definition of an operation or a custom query", formalDefinition="Definition of an operation or a named query together with its parameters and their meaning and type." )
        protected List<CapabilityStatementRestOperationComponent> operation;

        /**
         * An absolute URI which is a reference to the definition of a compartment that the system supports. The reference is to a CompartmentDefinition resource by its canonical URL .
         */
        @Child(name = "compartment", type = {UriType.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Compartments served/used by system", formalDefinition="An absolute URI which is a reference to the definition of a compartment that the system supports. The reference is to a CompartmentDefinition resource by its canonical URL ." )
        protected List<UriType> compartment;

        private static final long serialVersionUID = 38012979L;

    /**
     * Constructor
     */
      public CapabilityStatementRestComponent() {
        super();
      }

    /**
     * Constructor
     */
      public CapabilityStatementRestComponent(Enumeration<RestfulCapabilityMode> mode) {
        super();
        this.mode = mode;
      }

        /**
         * @return {@link #mode} (Identifies whether this portion of the statement is describing the ability to initiate or receive restful operations.). This is the underlying object with id, value and extensions. The accessor "getMode" gives direct access to the value
         */
        public Enumeration<RestfulCapabilityMode> getModeElement() { 
          if (this.mode == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementRestComponent.mode");
            else if (Configuration.doAutoCreate())
              this.mode = new Enumeration<RestfulCapabilityMode>(new RestfulCapabilityModeEnumFactory()); // bb
          return this.mode;
        }

        public boolean hasModeElement() { 
          return this.mode != null && !this.mode.isEmpty();
        }

        public boolean hasMode() { 
          return this.mode != null && !this.mode.isEmpty();
        }

        /**
         * @param value {@link #mode} (Identifies whether this portion of the statement is describing the ability to initiate or receive restful operations.). This is the underlying object with id, value and extensions. The accessor "getMode" gives direct access to the value
         */
        public CapabilityStatementRestComponent setModeElement(Enumeration<RestfulCapabilityMode> value) { 
          this.mode = value;
          return this;
        }

        /**
         * @return Identifies whether this portion of the statement is describing the ability to initiate or receive restful operations.
         */
        public RestfulCapabilityMode getMode() { 
          return this.mode == null ? null : this.mode.getValue();
        }

        /**
         * @param value Identifies whether this portion of the statement is describing the ability to initiate or receive restful operations.
         */
        public CapabilityStatementRestComponent setMode(RestfulCapabilityMode value) { 
            if (this.mode == null)
              this.mode = new Enumeration<RestfulCapabilityMode>(new RestfulCapabilityModeEnumFactory());
            this.mode.setValue(value);
          return this;
        }

        /**
         * @return {@link #documentation} (Information about the system's restful capabilities that apply across all applications, such as security.). This is the underlying object with id, value and extensions. The accessor "getDocumentation" gives direct access to the value
         */
        public StringType getDocumentationElement() { 
          if (this.documentation == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementRestComponent.documentation");
            else if (Configuration.doAutoCreate())
              this.documentation = new StringType(); // bb
          return this.documentation;
        }

        public boolean hasDocumentationElement() { 
          return this.documentation != null && !this.documentation.isEmpty();
        }

        public boolean hasDocumentation() { 
          return this.documentation != null && !this.documentation.isEmpty();
        }

        /**
         * @param value {@link #documentation} (Information about the system's restful capabilities that apply across all applications, such as security.). This is the underlying object with id, value and extensions. The accessor "getDocumentation" gives direct access to the value
         */
        public CapabilityStatementRestComponent setDocumentationElement(StringType value) { 
          this.documentation = value;
          return this;
        }

        /**
         * @return Information about the system's restful capabilities that apply across all applications, such as security.
         */
        public String getDocumentation() { 
          return this.documentation == null ? null : this.documentation.getValue();
        }

        /**
         * @param value Information about the system's restful capabilities that apply across all applications, such as security.
         */
        public CapabilityStatementRestComponent setDocumentation(String value) { 
          if (Utilities.noString(value))
            this.documentation = null;
          else {
            if (this.documentation == null)
              this.documentation = new StringType();
            this.documentation.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #security} (Information about security implementation from an interface perspective - what a client needs to know.)
         */
        public CapabilityStatementRestSecurityComponent getSecurity() { 
          if (this.security == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementRestComponent.security");
            else if (Configuration.doAutoCreate())
              this.security = new CapabilityStatementRestSecurityComponent(); // cc
          return this.security;
        }

        public boolean hasSecurity() { 
          return this.security != null && !this.security.isEmpty();
        }

        /**
         * @param value {@link #security} (Information about security implementation from an interface perspective - what a client needs to know.)
         */
        public CapabilityStatementRestComponent setSecurity(CapabilityStatementRestSecurityComponent value) { 
          this.security = value;
          return this;
        }

        /**
         * @return {@link #resource} (A specification of the restful capabilities of the solution for a specific resource type.)
         */
        public List<CapabilityStatementRestResourceComponent> getResource() { 
          if (this.resource == null)
            this.resource = new ArrayList<CapabilityStatementRestResourceComponent>();
          return this.resource;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public CapabilityStatementRestComponent setResource(List<CapabilityStatementRestResourceComponent> theResource) { 
          this.resource = theResource;
          return this;
        }

        public boolean hasResource() { 
          if (this.resource == null)
            return false;
          for (CapabilityStatementRestResourceComponent item : this.resource)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CapabilityStatementRestResourceComponent addResource() { //3
          CapabilityStatementRestResourceComponent t = new CapabilityStatementRestResourceComponent();
          if (this.resource == null)
            this.resource = new ArrayList<CapabilityStatementRestResourceComponent>();
          this.resource.add(t);
          return t;
        }

        public CapabilityStatementRestComponent addResource(CapabilityStatementRestResourceComponent t) { //3
          if (t == null)
            return this;
          if (this.resource == null)
            this.resource = new ArrayList<CapabilityStatementRestResourceComponent>();
          this.resource.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #resource}, creating it if it does not already exist
         */
        public CapabilityStatementRestResourceComponent getResourceFirstRep() { 
          if (getResource().isEmpty()) {
            addResource();
          }
          return getResource().get(0);
        }

        /**
         * @return {@link #interaction} (A specification of restful operations supported by the system.)
         */
        public List<SystemInteractionComponent> getInteraction() { 
          if (this.interaction == null)
            this.interaction = new ArrayList<SystemInteractionComponent>();
          return this.interaction;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public CapabilityStatementRestComponent setInteraction(List<SystemInteractionComponent> theInteraction) { 
          this.interaction = theInteraction;
          return this;
        }

        public boolean hasInteraction() { 
          if (this.interaction == null)
            return false;
          for (SystemInteractionComponent item : this.interaction)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public SystemInteractionComponent addInteraction() { //3
          SystemInteractionComponent t = new SystemInteractionComponent();
          if (this.interaction == null)
            this.interaction = new ArrayList<SystemInteractionComponent>();
          this.interaction.add(t);
          return t;
        }

        public CapabilityStatementRestComponent addInteraction(SystemInteractionComponent t) { //3
          if (t == null)
            return this;
          if (this.interaction == null)
            this.interaction = new ArrayList<SystemInteractionComponent>();
          this.interaction.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #interaction}, creating it if it does not already exist
         */
        public SystemInteractionComponent getInteractionFirstRep() { 
          if (getInteraction().isEmpty()) {
            addInteraction();
          }
          return getInteraction().get(0);
        }

        /**
         * @return {@link #searchParam} (Search parameters that are supported for searching all resources for implementations to support and/or make use of - either references to ones defined in the specification, or additional ones defined for/by the implementation.)
         */
        public List<CapabilityStatementRestResourceSearchParamComponent> getSearchParam() { 
          if (this.searchParam == null)
            this.searchParam = new ArrayList<CapabilityStatementRestResourceSearchParamComponent>();
          return this.searchParam;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public CapabilityStatementRestComponent setSearchParam(List<CapabilityStatementRestResourceSearchParamComponent> theSearchParam) { 
          this.searchParam = theSearchParam;
          return this;
        }

        public boolean hasSearchParam() { 
          if (this.searchParam == null)
            return false;
          for (CapabilityStatementRestResourceSearchParamComponent item : this.searchParam)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CapabilityStatementRestResourceSearchParamComponent addSearchParam() { //3
          CapabilityStatementRestResourceSearchParamComponent t = new CapabilityStatementRestResourceSearchParamComponent();
          if (this.searchParam == null)
            this.searchParam = new ArrayList<CapabilityStatementRestResourceSearchParamComponent>();
          this.searchParam.add(t);
          return t;
        }

        public CapabilityStatementRestComponent addSearchParam(CapabilityStatementRestResourceSearchParamComponent t) { //3
          if (t == null)
            return this;
          if (this.searchParam == null)
            this.searchParam = new ArrayList<CapabilityStatementRestResourceSearchParamComponent>();
          this.searchParam.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #searchParam}, creating it if it does not already exist
         */
        public CapabilityStatementRestResourceSearchParamComponent getSearchParamFirstRep() { 
          if (getSearchParam().isEmpty()) {
            addSearchParam();
          }
          return getSearchParam().get(0);
        }

        /**
         * @return {@link #operation} (Definition of an operation or a named query together with its parameters and their meaning and type.)
         */
        public List<CapabilityStatementRestOperationComponent> getOperation() { 
          if (this.operation == null)
            this.operation = new ArrayList<CapabilityStatementRestOperationComponent>();
          return this.operation;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public CapabilityStatementRestComponent setOperation(List<CapabilityStatementRestOperationComponent> theOperation) { 
          this.operation = theOperation;
          return this;
        }

        public boolean hasOperation() { 
          if (this.operation == null)
            return false;
          for (CapabilityStatementRestOperationComponent item : this.operation)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CapabilityStatementRestOperationComponent addOperation() { //3
          CapabilityStatementRestOperationComponent t = new CapabilityStatementRestOperationComponent();
          if (this.operation == null)
            this.operation = new ArrayList<CapabilityStatementRestOperationComponent>();
          this.operation.add(t);
          return t;
        }

        public CapabilityStatementRestComponent addOperation(CapabilityStatementRestOperationComponent t) { //3
          if (t == null)
            return this;
          if (this.operation == null)
            this.operation = new ArrayList<CapabilityStatementRestOperationComponent>();
          this.operation.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #operation}, creating it if it does not already exist
         */
        public CapabilityStatementRestOperationComponent getOperationFirstRep() { 
          if (getOperation().isEmpty()) {
            addOperation();
          }
          return getOperation().get(0);
        }

        /**
         * @return {@link #compartment} (An absolute URI which is a reference to the definition of a compartment that the system supports. The reference is to a CompartmentDefinition resource by its canonical URL .)
         */
        public List<UriType> getCompartment() { 
          if (this.compartment == null)
            this.compartment = new ArrayList<UriType>();
          return this.compartment;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public CapabilityStatementRestComponent setCompartment(List<UriType> theCompartment) { 
          this.compartment = theCompartment;
          return this;
        }

        public boolean hasCompartment() { 
          if (this.compartment == null)
            return false;
          for (UriType item : this.compartment)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #compartment} (An absolute URI which is a reference to the definition of a compartment that the system supports. The reference is to a CompartmentDefinition resource by its canonical URL .)
         */
        public UriType addCompartmentElement() {//2 
          UriType t = new UriType();
          if (this.compartment == null)
            this.compartment = new ArrayList<UriType>();
          this.compartment.add(t);
          return t;
        }

        /**
         * @param value {@link #compartment} (An absolute URI which is a reference to the definition of a compartment that the system supports. The reference is to a CompartmentDefinition resource by its canonical URL .)
         */
        public CapabilityStatementRestComponent addCompartment(String value) { //1
          UriType t = new UriType();
          t.setValue(value);
          if (this.compartment == null)
            this.compartment = new ArrayList<UriType>();
          this.compartment.add(t);
          return this;
        }

        /**
         * @param value {@link #compartment} (An absolute URI which is a reference to the definition of a compartment that the system supports. The reference is to a CompartmentDefinition resource by its canonical URL .)
         */
        public boolean hasCompartment(String value) { 
          if (this.compartment == null)
            return false;
          for (UriType v : this.compartment)
            if (v.equals(value)) // uri
              return true;
          return false;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("mode", "code", "Identifies whether this portion of the statement is describing the ability to initiate or receive restful operations.", 0, java.lang.Integer.MAX_VALUE, mode));
          childrenList.add(new Property("documentation", "string", "Information about the system's restful capabilities that apply across all applications, such as security.", 0, java.lang.Integer.MAX_VALUE, documentation));
          childrenList.add(new Property("security", "", "Information about security implementation from an interface perspective - what a client needs to know.", 0, java.lang.Integer.MAX_VALUE, security));
          childrenList.add(new Property("resource", "", "A specification of the restful capabilities of the solution for a specific resource type.", 0, java.lang.Integer.MAX_VALUE, resource));
          childrenList.add(new Property("interaction", "", "A specification of restful operations supported by the system.", 0, java.lang.Integer.MAX_VALUE, interaction));
          childrenList.add(new Property("searchParam", "@CapabilityStatement.rest.resource.searchParam", "Search parameters that are supported for searching all resources for implementations to support and/or make use of - either references to ones defined in the specification, or additional ones defined for/by the implementation.", 0, java.lang.Integer.MAX_VALUE, searchParam));
          childrenList.add(new Property("operation", "", "Definition of an operation or a named query together with its parameters and their meaning and type.", 0, java.lang.Integer.MAX_VALUE, operation));
          childrenList.add(new Property("compartment", "uri", "An absolute URI which is a reference to the definition of a compartment that the system supports. The reference is to a CompartmentDefinition resource by its canonical URL .", 0, java.lang.Integer.MAX_VALUE, compartment));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3357091: /*mode*/ return this.mode == null ? new Base[0] : new Base[] {this.mode}; // Enumeration<RestfulCapabilityMode>
        case 1587405498: /*documentation*/ return this.documentation == null ? new Base[0] : new Base[] {this.documentation}; // StringType
        case 949122880: /*security*/ return this.security == null ? new Base[0] : new Base[] {this.security}; // CapabilityStatementRestSecurityComponent
        case -341064690: /*resource*/ return this.resource == null ? new Base[0] : this.resource.toArray(new Base[this.resource.size()]); // CapabilityStatementRestResourceComponent
        case 1844104722: /*interaction*/ return this.interaction == null ? new Base[0] : this.interaction.toArray(new Base[this.interaction.size()]); // SystemInteractionComponent
        case -553645115: /*searchParam*/ return this.searchParam == null ? new Base[0] : this.searchParam.toArray(new Base[this.searchParam.size()]); // CapabilityStatementRestResourceSearchParamComponent
        case 1662702951: /*operation*/ return this.operation == null ? new Base[0] : this.operation.toArray(new Base[this.operation.size()]); // CapabilityStatementRestOperationComponent
        case -397756334: /*compartment*/ return this.compartment == null ? new Base[0] : this.compartment.toArray(new Base[this.compartment.size()]); // UriType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3357091: // mode
          value = new RestfulCapabilityModeEnumFactory().fromType(castToCode(value));
          this.mode = (Enumeration) value; // Enumeration<RestfulCapabilityMode>
          return value;
        case 1587405498: // documentation
          this.documentation = castToString(value); // StringType
          return value;
        case 949122880: // security
          this.security = (CapabilityStatementRestSecurityComponent) value; // CapabilityStatementRestSecurityComponent
          return value;
        case -341064690: // resource
          this.getResource().add((CapabilityStatementRestResourceComponent) value); // CapabilityStatementRestResourceComponent
          return value;
        case 1844104722: // interaction
          this.getInteraction().add((SystemInteractionComponent) value); // SystemInteractionComponent
          return value;
        case -553645115: // searchParam
          this.getSearchParam().add((CapabilityStatementRestResourceSearchParamComponent) value); // CapabilityStatementRestResourceSearchParamComponent
          return value;
        case 1662702951: // operation
          this.getOperation().add((CapabilityStatementRestOperationComponent) value); // CapabilityStatementRestOperationComponent
          return value;
        case -397756334: // compartment
          this.getCompartment().add(castToUri(value)); // UriType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("mode")) {
          value = new RestfulCapabilityModeEnumFactory().fromType(castToCode(value));
          this.mode = (Enumeration) value; // Enumeration<RestfulCapabilityMode>
        } else if (name.equals("documentation")) {
          this.documentation = castToString(value); // StringType
        } else if (name.equals("security")) {
          this.security = (CapabilityStatementRestSecurityComponent) value; // CapabilityStatementRestSecurityComponent
        } else if (name.equals("resource")) {
          this.getResource().add((CapabilityStatementRestResourceComponent) value);
        } else if (name.equals("interaction")) {
          this.getInteraction().add((SystemInteractionComponent) value);
        } else if (name.equals("searchParam")) {
          this.getSearchParam().add((CapabilityStatementRestResourceSearchParamComponent) value);
        } else if (name.equals("operation")) {
          this.getOperation().add((CapabilityStatementRestOperationComponent) value);
        } else if (name.equals("compartment")) {
          this.getCompartment().add(castToUri(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3357091:  return getModeElement();
        case 1587405498:  return getDocumentationElement();
        case 949122880:  return getSecurity(); 
        case -341064690:  return addResource(); 
        case 1844104722:  return addInteraction(); 
        case -553645115:  return addSearchParam(); 
        case 1662702951:  return addOperation(); 
        case -397756334:  return addCompartmentElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3357091: /*mode*/ return new String[] {"code"};
        case 1587405498: /*documentation*/ return new String[] {"string"};
        case 949122880: /*security*/ return new String[] {};
        case -341064690: /*resource*/ return new String[] {};
        case 1844104722: /*interaction*/ return new String[] {};
        case -553645115: /*searchParam*/ return new String[] {"@CapabilityStatement.rest.resource.searchParam"};
        case 1662702951: /*operation*/ return new String[] {};
        case -397756334: /*compartment*/ return new String[] {"uri"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("mode")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.mode");
        }
        else if (name.equals("documentation")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.documentation");
        }
        else if (name.equals("security")) {
          this.security = new CapabilityStatementRestSecurityComponent();
          return this.security;
        }
        else if (name.equals("resource")) {
          return addResource();
        }
        else if (name.equals("interaction")) {
          return addInteraction();
        }
        else if (name.equals("searchParam")) {
          return addSearchParam();
        }
        else if (name.equals("operation")) {
          return addOperation();
        }
        else if (name.equals("compartment")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.compartment");
        }
        else
          return super.addChild(name);
      }

      public CapabilityStatementRestComponent copy() {
        CapabilityStatementRestComponent dst = new CapabilityStatementRestComponent();
        copyValues(dst);
        dst.mode = mode == null ? null : mode.copy();
        dst.documentation = documentation == null ? null : documentation.copy();
        dst.security = security == null ? null : security.copy();
        if (resource != null) {
          dst.resource = new ArrayList<CapabilityStatementRestResourceComponent>();
          for (CapabilityStatementRestResourceComponent i : resource)
            dst.resource.add(i.copy());
        };
        if (interaction != null) {
          dst.interaction = new ArrayList<SystemInteractionComponent>();
          for (SystemInteractionComponent i : interaction)
            dst.interaction.add(i.copy());
        };
        if (searchParam != null) {
          dst.searchParam = new ArrayList<CapabilityStatementRestResourceSearchParamComponent>();
          for (CapabilityStatementRestResourceSearchParamComponent i : searchParam)
            dst.searchParam.add(i.copy());
        };
        if (operation != null) {
          dst.operation = new ArrayList<CapabilityStatementRestOperationComponent>();
          for (CapabilityStatementRestOperationComponent i : operation)
            dst.operation.add(i.copy());
        };
        if (compartment != null) {
          dst.compartment = new ArrayList<UriType>();
          for (UriType i : compartment)
            dst.compartment.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CapabilityStatementRestComponent))
          return false;
        CapabilityStatementRestComponent o = (CapabilityStatementRestComponent) other;
        return compareDeep(mode, o.mode, true) && compareDeep(documentation, o.documentation, true) && compareDeep(security, o.security, true)
           && compareDeep(resource, o.resource, true) && compareDeep(interaction, o.interaction, true) && compareDeep(searchParam, o.searchParam, true)
           && compareDeep(operation, o.operation, true) && compareDeep(compartment, o.compartment, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CapabilityStatementRestComponent))
          return false;
        CapabilityStatementRestComponent o = (CapabilityStatementRestComponent) other;
        return compareValues(mode, o.mode, true) && compareValues(documentation, o.documentation, true) && compareValues(compartment, o.compartment, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(mode, documentation, security
          , resource, interaction, searchParam, operation, compartment);
      }

  public String fhirType() {
    return "CapabilityStatement.rest";

  }

  }

    @Block()
    public static class CapabilityStatementRestSecurityComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Server adds CORS headers when responding to requests - this enables javascript applications to use the server.
         */
        @Child(name = "cors", type = {BooleanType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Adds CORS Headers (http://enable-cors.org/)", formalDefinition="Server adds CORS headers when responding to requests - this enables javascript applications to use the server." )
        protected BooleanType cors;

        /**
         * Types of security services that are supported/required by the system.
         */
        @Child(name = "service", type = {CodeableConcept.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="OAuth | SMART-on-FHIR | NTLM | Basic | Kerberos | Certificates", formalDefinition="Types of security services that are supported/required by the system." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/restful-security-service")
        protected List<CodeableConcept> service;

        /**
         * General description of how security works.
         */
        @Child(name = "description", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="General description of how security works", formalDefinition="General description of how security works." )
        protected StringType description;

        /**
         * Certificates associated with security profiles.
         */
        @Child(name = "certificate", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Certificates associated with security profiles", formalDefinition="Certificates associated with security profiles." )
        protected List<CapabilityStatementRestSecurityCertificateComponent> certificate;

        private static final long serialVersionUID = 1081654002L;

    /**
     * Constructor
     */
      public CapabilityStatementRestSecurityComponent() {
        super();
      }

        /**
         * @return {@link #cors} (Server adds CORS headers when responding to requests - this enables javascript applications to use the server.). This is the underlying object with id, value and extensions. The accessor "getCors" gives direct access to the value
         */
        public BooleanType getCorsElement() { 
          if (this.cors == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementRestSecurityComponent.cors");
            else if (Configuration.doAutoCreate())
              this.cors = new BooleanType(); // bb
          return this.cors;
        }

        public boolean hasCorsElement() { 
          return this.cors != null && !this.cors.isEmpty();
        }

        public boolean hasCors() { 
          return this.cors != null && !this.cors.isEmpty();
        }

        /**
         * @param value {@link #cors} (Server adds CORS headers when responding to requests - this enables javascript applications to use the server.). This is the underlying object with id, value and extensions. The accessor "getCors" gives direct access to the value
         */
        public CapabilityStatementRestSecurityComponent setCorsElement(BooleanType value) { 
          this.cors = value;
          return this;
        }

        /**
         * @return Server adds CORS headers when responding to requests - this enables javascript applications to use the server.
         */
        public boolean getCors() { 
          return this.cors == null || this.cors.isEmpty() ? false : this.cors.getValue();
        }

        /**
         * @param value Server adds CORS headers when responding to requests - this enables javascript applications to use the server.
         */
        public CapabilityStatementRestSecurityComponent setCors(boolean value) { 
            if (this.cors == null)
              this.cors = new BooleanType();
            this.cors.setValue(value);
          return this;
        }

        /**
         * @return {@link #service} (Types of security services that are supported/required by the system.)
         */
        public List<CodeableConcept> getService() { 
          if (this.service == null)
            this.service = new ArrayList<CodeableConcept>();
          return this.service;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public CapabilityStatementRestSecurityComponent setService(List<CodeableConcept> theService) { 
          this.service = theService;
          return this;
        }

        public boolean hasService() { 
          if (this.service == null)
            return false;
          for (CodeableConcept item : this.service)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addService() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.service == null)
            this.service = new ArrayList<CodeableConcept>();
          this.service.add(t);
          return t;
        }

        public CapabilityStatementRestSecurityComponent addService(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.service == null)
            this.service = new ArrayList<CodeableConcept>();
          this.service.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #service}, creating it if it does not already exist
         */
        public CodeableConcept getServiceFirstRep() { 
          if (getService().isEmpty()) {
            addService();
          }
          return getService().get(0);
        }

        /**
         * @return {@link #description} (General description of how security works.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementRestSecurityComponent.description");
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
         * @param value {@link #description} (General description of how security works.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public CapabilityStatementRestSecurityComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return General description of how security works.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value General description of how security works.
         */
        public CapabilityStatementRestSecurityComponent setDescription(String value) { 
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
         * @return {@link #certificate} (Certificates associated with security profiles.)
         */
        public List<CapabilityStatementRestSecurityCertificateComponent> getCertificate() { 
          if (this.certificate == null)
            this.certificate = new ArrayList<CapabilityStatementRestSecurityCertificateComponent>();
          return this.certificate;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public CapabilityStatementRestSecurityComponent setCertificate(List<CapabilityStatementRestSecurityCertificateComponent> theCertificate) { 
          this.certificate = theCertificate;
          return this;
        }

        public boolean hasCertificate() { 
          if (this.certificate == null)
            return false;
          for (CapabilityStatementRestSecurityCertificateComponent item : this.certificate)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CapabilityStatementRestSecurityCertificateComponent addCertificate() { //3
          CapabilityStatementRestSecurityCertificateComponent t = new CapabilityStatementRestSecurityCertificateComponent();
          if (this.certificate == null)
            this.certificate = new ArrayList<CapabilityStatementRestSecurityCertificateComponent>();
          this.certificate.add(t);
          return t;
        }

        public CapabilityStatementRestSecurityComponent addCertificate(CapabilityStatementRestSecurityCertificateComponent t) { //3
          if (t == null)
            return this;
          if (this.certificate == null)
            this.certificate = new ArrayList<CapabilityStatementRestSecurityCertificateComponent>();
          this.certificate.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #certificate}, creating it if it does not already exist
         */
        public CapabilityStatementRestSecurityCertificateComponent getCertificateFirstRep() { 
          if (getCertificate().isEmpty()) {
            addCertificate();
          }
          return getCertificate().get(0);
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("cors", "boolean", "Server adds CORS headers when responding to requests - this enables javascript applications to use the server.", 0, java.lang.Integer.MAX_VALUE, cors));
          childrenList.add(new Property("service", "CodeableConcept", "Types of security services that are supported/required by the system.", 0, java.lang.Integer.MAX_VALUE, service));
          childrenList.add(new Property("description", "string", "General description of how security works.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("certificate", "", "Certificates associated with security profiles.", 0, java.lang.Integer.MAX_VALUE, certificate));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3059629: /*cors*/ return this.cors == null ? new Base[0] : new Base[] {this.cors}; // BooleanType
        case 1984153269: /*service*/ return this.service == null ? new Base[0] : this.service.toArray(new Base[this.service.size()]); // CodeableConcept
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case 1952399767: /*certificate*/ return this.certificate == null ? new Base[0] : this.certificate.toArray(new Base[this.certificate.size()]); // CapabilityStatementRestSecurityCertificateComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3059629: // cors
          this.cors = castToBoolean(value); // BooleanType
          return value;
        case 1984153269: // service
          this.getService().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        case 1952399767: // certificate
          this.getCertificate().add((CapabilityStatementRestSecurityCertificateComponent) value); // CapabilityStatementRestSecurityCertificateComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("cors")) {
          this.cors = castToBoolean(value); // BooleanType
        } else if (name.equals("service")) {
          this.getService().add(castToCodeableConcept(value));
        } else if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else if (name.equals("certificate")) {
          this.getCertificate().add((CapabilityStatementRestSecurityCertificateComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059629:  return getCorsElement();
        case 1984153269:  return addService(); 
        case -1724546052:  return getDescriptionElement();
        case 1952399767:  return addCertificate(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059629: /*cors*/ return new String[] {"boolean"};
        case 1984153269: /*service*/ return new String[] {"CodeableConcept"};
        case -1724546052: /*description*/ return new String[] {"string"};
        case 1952399767: /*certificate*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("cors")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.cors");
        }
        else if (name.equals("service")) {
          return addService();
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.description");
        }
        else if (name.equals("certificate")) {
          return addCertificate();
        }
        else
          return super.addChild(name);
      }

      public CapabilityStatementRestSecurityComponent copy() {
        CapabilityStatementRestSecurityComponent dst = new CapabilityStatementRestSecurityComponent();
        copyValues(dst);
        dst.cors = cors == null ? null : cors.copy();
        if (service != null) {
          dst.service = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : service)
            dst.service.add(i.copy());
        };
        dst.description = description == null ? null : description.copy();
        if (certificate != null) {
          dst.certificate = new ArrayList<CapabilityStatementRestSecurityCertificateComponent>();
          for (CapabilityStatementRestSecurityCertificateComponent i : certificate)
            dst.certificate.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CapabilityStatementRestSecurityComponent))
          return false;
        CapabilityStatementRestSecurityComponent o = (CapabilityStatementRestSecurityComponent) other;
        return compareDeep(cors, o.cors, true) && compareDeep(service, o.service, true) && compareDeep(description, o.description, true)
           && compareDeep(certificate, o.certificate, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CapabilityStatementRestSecurityComponent))
          return false;
        CapabilityStatementRestSecurityComponent o = (CapabilityStatementRestSecurityComponent) other;
        return compareValues(cors, o.cors, true) && compareValues(description, o.description, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(cors, service, description
          , certificate);
      }

  public String fhirType() {
    return "CapabilityStatement.rest.security";

  }

  }

    @Block()
    public static class CapabilityStatementRestSecurityCertificateComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Mime type for a certificate.
         */
        @Child(name = "type", type = {CodeType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Mime type for certificates", formalDefinition="Mime type for a certificate." )
        protected CodeType type;

        /**
         * Actual certificate.
         */
        @Child(name = "blob", type = {Base64BinaryType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Actual certificate", formalDefinition="Actual certificate." )
        protected Base64BinaryType blob;

        private static final long serialVersionUID = 2092655854L;

    /**
     * Constructor
     */
      public CapabilityStatementRestSecurityCertificateComponent() {
        super();
      }

        /**
         * @return {@link #type} (Mime type for a certificate.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public CodeType getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementRestSecurityCertificateComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeType(); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Mime type for a certificate.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public CapabilityStatementRestSecurityCertificateComponent setTypeElement(CodeType value) { 
          this.type = value;
          return this;
        }

        /**
         * @return Mime type for a certificate.
         */
        public String getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value Mime type for a certificate.
         */
        public CapabilityStatementRestSecurityCertificateComponent setType(String value) { 
          if (Utilities.noString(value))
            this.type = null;
          else {
            if (this.type == null)
              this.type = new CodeType();
            this.type.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #blob} (Actual certificate.). This is the underlying object with id, value and extensions. The accessor "getBlob" gives direct access to the value
         */
        public Base64BinaryType getBlobElement() { 
          if (this.blob == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementRestSecurityCertificateComponent.blob");
            else if (Configuration.doAutoCreate())
              this.blob = new Base64BinaryType(); // bb
          return this.blob;
        }

        public boolean hasBlobElement() { 
          return this.blob != null && !this.blob.isEmpty();
        }

        public boolean hasBlob() { 
          return this.blob != null && !this.blob.isEmpty();
        }

        /**
         * @param value {@link #blob} (Actual certificate.). This is the underlying object with id, value and extensions. The accessor "getBlob" gives direct access to the value
         */
        public CapabilityStatementRestSecurityCertificateComponent setBlobElement(Base64BinaryType value) { 
          this.blob = value;
          return this;
        }

        /**
         * @return Actual certificate.
         */
        public byte[] getBlob() { 
          return this.blob == null ? null : this.blob.getValue();
        }

        /**
         * @param value Actual certificate.
         */
        public CapabilityStatementRestSecurityCertificateComponent setBlob(byte[] value) { 
          if (value == null)
            this.blob = null;
          else {
            if (this.blob == null)
              this.blob = new Base64BinaryType();
            this.blob.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "code", "Mime type for a certificate.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("blob", "base64Binary", "Actual certificate.", 0, java.lang.Integer.MAX_VALUE, blob));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeType
        case 3026845: /*blob*/ return this.blob == null ? new Base[0] : new Base[] {this.blob}; // Base64BinaryType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCode(value); // CodeType
          return value;
        case 3026845: // blob
          this.blob = castToBase64Binary(value); // Base64BinaryType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToCode(value); // CodeType
        } else if (name.equals("blob")) {
          this.blob = castToBase64Binary(value); // Base64BinaryType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getTypeElement();
        case 3026845:  return getBlobElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"code"};
        case 3026845: /*blob*/ return new String[] {"base64Binary"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.type");
        }
        else if (name.equals("blob")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.blob");
        }
        else
          return super.addChild(name);
      }

      public CapabilityStatementRestSecurityCertificateComponent copy() {
        CapabilityStatementRestSecurityCertificateComponent dst = new CapabilityStatementRestSecurityCertificateComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.blob = blob == null ? null : blob.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CapabilityStatementRestSecurityCertificateComponent))
          return false;
        CapabilityStatementRestSecurityCertificateComponent o = (CapabilityStatementRestSecurityCertificateComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(blob, o.blob, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CapabilityStatementRestSecurityCertificateComponent))
          return false;
        CapabilityStatementRestSecurityCertificateComponent o = (CapabilityStatementRestSecurityCertificateComponent) other;
        return compareValues(type, o.type, true) && compareValues(blob, o.blob, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, blob);
      }

  public String fhirType() {
    return "CapabilityStatement.rest.security.certificate";

  }

  }

    @Block()
    public static class CapabilityStatementRestResourceComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A type of resource exposed via the restful interface.
         */
        @Child(name = "type", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="A resource type that is supported", formalDefinition="A type of resource exposed via the restful interface." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/resource-types")
        protected CodeType type;

        /**
         * A specification of the profile that describes the solution's overall support for the resource, including any constraints on cardinality, bindings, lengths or other limitations. See further discussion in [Using Profiles](profiling.html#profile-uses).
         */
        @Child(name = "profile", type = {StructureDefinition.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Base System profile for all uses of resource", formalDefinition="A specification of the profile that describes the solution's overall support for the resource, including any constraints on cardinality, bindings, lengths or other limitations. See further discussion in [Using Profiles](profiling.html#profile-uses)." )
        protected Reference profile;

        /**
         * The actual object that is the target of the reference (A specification of the profile that describes the solution's overall support for the resource, including any constraints on cardinality, bindings, lengths or other limitations. See further discussion in [Using Profiles](profiling.html#profile-uses).)
         */
        protected StructureDefinition profileTarget;

        /**
         * Additional information about the resource type used by the system.
         */
        @Child(name = "documentation", type = {MarkdownType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Additional information about the use of the resource type", formalDefinition="Additional information about the resource type used by the system." )
        protected MarkdownType documentation;

        /**
         * Identifies a restful operation supported by the solution.
         */
        @Child(name = "interaction", type = {}, order=4, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="What operations are supported?", formalDefinition="Identifies a restful operation supported by the solution." )
        protected List<ResourceInteractionComponent> interaction;

        /**
         * This field is set to no-version to specify that the system does not support (server) or use (client) versioning for this resource type. If this has some other value, the server must at least correctly track and populate the versionId meta-property on resources. If the value is 'versioned-update', then the server supports all the versioning features, including using e-tags for version integrity in the API.
         */
        @Child(name = "versioning", type = {CodeType.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="no-version | versioned | versioned-update", formalDefinition="This field is set to no-version to specify that the system does not support (server) or use (client) versioning for this resource type. If this has some other value, the server must at least correctly track and populate the versionId meta-property on resources. If the value is 'versioned-update', then the server supports all the versioning features, including using e-tags for version integrity in the API." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/versioning-policy")
        protected Enumeration<ResourceVersionPolicy> versioning;

        /**
         * A flag for whether the server is able to return past versions as part of the vRead operation.
         */
        @Child(name = "readHistory", type = {BooleanType.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Whether vRead can return past versions", formalDefinition="A flag for whether the server is able to return past versions as part of the vRead operation." )
        protected BooleanType readHistory;

        /**
         * A flag to indicate that the server allows or needs to allow the client to create new identities on the server (e.g. that is, the client PUTs to a location where there is no existing resource). Allowing this operation means that the server allows the client to create new identities on the server.
         */
        @Child(name = "updateCreate", type = {BooleanType.class}, order=7, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="If update can commit to a new identity", formalDefinition="A flag to indicate that the server allows or needs to allow the client to create new identities on the server (e.g. that is, the client PUTs to a location where there is no existing resource). Allowing this operation means that the server allows the client to create new identities on the server." )
        protected BooleanType updateCreate;

        /**
         * A flag that indicates that the server supports conditional create.
         */
        @Child(name = "conditionalCreate", type = {BooleanType.class}, order=8, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="If allows/uses conditional create", formalDefinition="A flag that indicates that the server supports conditional create." )
        protected BooleanType conditionalCreate;

        /**
         * A code that indicates how the server supports conditional read.
         */
        @Child(name = "conditionalRead", type = {CodeType.class}, order=9, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="not-supported | modified-since | not-match | full-support", formalDefinition="A code that indicates how the server supports conditional read." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/conditional-read-status")
        protected Enumeration<ConditionalReadStatus> conditionalRead;

        /**
         * A flag that indicates that the server supports conditional update.
         */
        @Child(name = "conditionalUpdate", type = {BooleanType.class}, order=10, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="If allows/uses conditional update", formalDefinition="A flag that indicates that the server supports conditional update." )
        protected BooleanType conditionalUpdate;

        /**
         * A code that indicates how the server supports conditional delete.
         */
        @Child(name = "conditionalDelete", type = {CodeType.class}, order=11, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="not-supported | single | multiple - how conditional delete is supported", formalDefinition="A code that indicates how the server supports conditional delete." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/conditional-delete-status")
        protected Enumeration<ConditionalDeleteStatus> conditionalDelete;

        /**
         * A set of flags that defines how references are supported.
         */
        @Child(name = "referencePolicy", type = {CodeType.class}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="literal | logical | resolves | enforced | local", formalDefinition="A set of flags that defines how references are supported." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/reference-handling-policy")
        protected List<Enumeration<ReferenceHandlingPolicy>> referencePolicy;

        /**
         * A list of _include values supported by the server.
         */
        @Child(name = "searchInclude", type = {StringType.class}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="_include values supported by the server", formalDefinition="A list of _include values supported by the server." )
        protected List<StringType> searchInclude;

        /**
         * A list of _revinclude (reverse include) values supported by the server.
         */
        @Child(name = "searchRevInclude", type = {StringType.class}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="_revinclude values supported by the server", formalDefinition="A list of _revinclude (reverse include) values supported by the server." )
        protected List<StringType> searchRevInclude;

        /**
         * Search parameters for implementations to support and/or make use of - either references to ones defined in the specification, or additional ones defined for/by the implementation.
         */
        @Child(name = "searchParam", type = {}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Search parameters supported by implementation", formalDefinition="Search parameters for implementations to support and/or make use of - either references to ones defined in the specification, or additional ones defined for/by the implementation." )
        protected List<CapabilityStatementRestResourceSearchParamComponent> searchParam;

        private static final long serialVersionUID = 1271233297L;

    /**
     * Constructor
     */
      public CapabilityStatementRestResourceComponent() {
        super();
      }

    /**
     * Constructor
     */
      public CapabilityStatementRestResourceComponent(CodeType type) {
        super();
        this.type = type;
      }

        /**
         * @return {@link #type} (A type of resource exposed via the restful interface.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public CodeType getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementRestResourceComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeType(); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (A type of resource exposed via the restful interface.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public CapabilityStatementRestResourceComponent setTypeElement(CodeType value) { 
          this.type = value;
          return this;
        }

        /**
         * @return A type of resource exposed via the restful interface.
         */
        public String getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value A type of resource exposed via the restful interface.
         */
        public CapabilityStatementRestResourceComponent setType(String value) { 
            if (this.type == null)
              this.type = new CodeType();
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #profile} (A specification of the profile that describes the solution's overall support for the resource, including any constraints on cardinality, bindings, lengths or other limitations. See further discussion in [Using Profiles](profiling.html#profile-uses).)
         */
        public Reference getProfile() { 
          if (this.profile == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementRestResourceComponent.profile");
            else if (Configuration.doAutoCreate())
              this.profile = new Reference(); // cc
          return this.profile;
        }

        public boolean hasProfile() { 
          return this.profile != null && !this.profile.isEmpty();
        }

        /**
         * @param value {@link #profile} (A specification of the profile that describes the solution's overall support for the resource, including any constraints on cardinality, bindings, lengths or other limitations. See further discussion in [Using Profiles](profiling.html#profile-uses).)
         */
        public CapabilityStatementRestResourceComponent setProfile(Reference value) { 
          this.profile = value;
          return this;
        }

        /**
         * @return {@link #profile} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A specification of the profile that describes the solution's overall support for the resource, including any constraints on cardinality, bindings, lengths or other limitations. See further discussion in [Using Profiles](profiling.html#profile-uses).)
         */
        public StructureDefinition getProfileTarget() { 
          if (this.profileTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementRestResourceComponent.profile");
            else if (Configuration.doAutoCreate())
              this.profileTarget = new StructureDefinition(); // aa
          return this.profileTarget;
        }

        /**
         * @param value {@link #profile} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A specification of the profile that describes the solution's overall support for the resource, including any constraints on cardinality, bindings, lengths or other limitations. See further discussion in [Using Profiles](profiling.html#profile-uses).)
         */
        public CapabilityStatementRestResourceComponent setProfileTarget(StructureDefinition value) { 
          this.profileTarget = value;
          return this;
        }

        /**
         * @return {@link #documentation} (Additional information about the resource type used by the system.). This is the underlying object with id, value and extensions. The accessor "getDocumentation" gives direct access to the value
         */
        public MarkdownType getDocumentationElement() { 
          if (this.documentation == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementRestResourceComponent.documentation");
            else if (Configuration.doAutoCreate())
              this.documentation = new MarkdownType(); // bb
          return this.documentation;
        }

        public boolean hasDocumentationElement() { 
          return this.documentation != null && !this.documentation.isEmpty();
        }

        public boolean hasDocumentation() { 
          return this.documentation != null && !this.documentation.isEmpty();
        }

        /**
         * @param value {@link #documentation} (Additional information about the resource type used by the system.). This is the underlying object with id, value and extensions. The accessor "getDocumentation" gives direct access to the value
         */
        public CapabilityStatementRestResourceComponent setDocumentationElement(MarkdownType value) { 
          this.documentation = value;
          return this;
        }

        /**
         * @return Additional information about the resource type used by the system.
         */
        public String getDocumentation() { 
          return this.documentation == null ? null : this.documentation.getValue();
        }

        /**
         * @param value Additional information about the resource type used by the system.
         */
        public CapabilityStatementRestResourceComponent setDocumentation(String value) { 
          if (value == null)
            this.documentation = null;
          else {
            if (this.documentation == null)
              this.documentation = new MarkdownType();
            this.documentation.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #interaction} (Identifies a restful operation supported by the solution.)
         */
        public List<ResourceInteractionComponent> getInteraction() { 
          if (this.interaction == null)
            this.interaction = new ArrayList<ResourceInteractionComponent>();
          return this.interaction;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public CapabilityStatementRestResourceComponent setInteraction(List<ResourceInteractionComponent> theInteraction) { 
          this.interaction = theInteraction;
          return this;
        }

        public boolean hasInteraction() { 
          if (this.interaction == null)
            return false;
          for (ResourceInteractionComponent item : this.interaction)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ResourceInteractionComponent addInteraction() { //3
          ResourceInteractionComponent t = new ResourceInteractionComponent();
          if (this.interaction == null)
            this.interaction = new ArrayList<ResourceInteractionComponent>();
          this.interaction.add(t);
          return t;
        }

        public CapabilityStatementRestResourceComponent addInteraction(ResourceInteractionComponent t) { //3
          if (t == null)
            return this;
          if (this.interaction == null)
            this.interaction = new ArrayList<ResourceInteractionComponent>();
          this.interaction.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #interaction}, creating it if it does not already exist
         */
        public ResourceInteractionComponent getInteractionFirstRep() { 
          if (getInteraction().isEmpty()) {
            addInteraction();
          }
          return getInteraction().get(0);
        }

        /**
         * @return {@link #versioning} (This field is set to no-version to specify that the system does not support (server) or use (client) versioning for this resource type. If this has some other value, the server must at least correctly track and populate the versionId meta-property on resources. If the value is 'versioned-update', then the server supports all the versioning features, including using e-tags for version integrity in the API.). This is the underlying object with id, value and extensions. The accessor "getVersioning" gives direct access to the value
         */
        public Enumeration<ResourceVersionPolicy> getVersioningElement() { 
          if (this.versioning == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementRestResourceComponent.versioning");
            else if (Configuration.doAutoCreate())
              this.versioning = new Enumeration<ResourceVersionPolicy>(new ResourceVersionPolicyEnumFactory()); // bb
          return this.versioning;
        }

        public boolean hasVersioningElement() { 
          return this.versioning != null && !this.versioning.isEmpty();
        }

        public boolean hasVersioning() { 
          return this.versioning != null && !this.versioning.isEmpty();
        }

        /**
         * @param value {@link #versioning} (This field is set to no-version to specify that the system does not support (server) or use (client) versioning for this resource type. If this has some other value, the server must at least correctly track and populate the versionId meta-property on resources. If the value is 'versioned-update', then the server supports all the versioning features, including using e-tags for version integrity in the API.). This is the underlying object with id, value and extensions. The accessor "getVersioning" gives direct access to the value
         */
        public CapabilityStatementRestResourceComponent setVersioningElement(Enumeration<ResourceVersionPolicy> value) { 
          this.versioning = value;
          return this;
        }

        /**
         * @return This field is set to no-version to specify that the system does not support (server) or use (client) versioning for this resource type. If this has some other value, the server must at least correctly track and populate the versionId meta-property on resources. If the value is 'versioned-update', then the server supports all the versioning features, including using e-tags for version integrity in the API.
         */
        public ResourceVersionPolicy getVersioning() { 
          return this.versioning == null ? null : this.versioning.getValue();
        }

        /**
         * @param value This field is set to no-version to specify that the system does not support (server) or use (client) versioning for this resource type. If this has some other value, the server must at least correctly track and populate the versionId meta-property on resources. If the value is 'versioned-update', then the server supports all the versioning features, including using e-tags for version integrity in the API.
         */
        public CapabilityStatementRestResourceComponent setVersioning(ResourceVersionPolicy value) { 
          if (value == null)
            this.versioning = null;
          else {
            if (this.versioning == null)
              this.versioning = new Enumeration<ResourceVersionPolicy>(new ResourceVersionPolicyEnumFactory());
            this.versioning.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #readHistory} (A flag for whether the server is able to return past versions as part of the vRead operation.). This is the underlying object with id, value and extensions. The accessor "getReadHistory" gives direct access to the value
         */
        public BooleanType getReadHistoryElement() { 
          if (this.readHistory == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementRestResourceComponent.readHistory");
            else if (Configuration.doAutoCreate())
              this.readHistory = new BooleanType(); // bb
          return this.readHistory;
        }

        public boolean hasReadHistoryElement() { 
          return this.readHistory != null && !this.readHistory.isEmpty();
        }

        public boolean hasReadHistory() { 
          return this.readHistory != null && !this.readHistory.isEmpty();
        }

        /**
         * @param value {@link #readHistory} (A flag for whether the server is able to return past versions as part of the vRead operation.). This is the underlying object with id, value and extensions. The accessor "getReadHistory" gives direct access to the value
         */
        public CapabilityStatementRestResourceComponent setReadHistoryElement(BooleanType value) { 
          this.readHistory = value;
          return this;
        }

        /**
         * @return A flag for whether the server is able to return past versions as part of the vRead operation.
         */
        public boolean getReadHistory() { 
          return this.readHistory == null || this.readHistory.isEmpty() ? false : this.readHistory.getValue();
        }

        /**
         * @param value A flag for whether the server is able to return past versions as part of the vRead operation.
         */
        public CapabilityStatementRestResourceComponent setReadHistory(boolean value) { 
            if (this.readHistory == null)
              this.readHistory = new BooleanType();
            this.readHistory.setValue(value);
          return this;
        }

        /**
         * @return {@link #updateCreate} (A flag to indicate that the server allows or needs to allow the client to create new identities on the server (e.g. that is, the client PUTs to a location where there is no existing resource). Allowing this operation means that the server allows the client to create new identities on the server.). This is the underlying object with id, value and extensions. The accessor "getUpdateCreate" gives direct access to the value
         */
        public BooleanType getUpdateCreateElement() { 
          if (this.updateCreate == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementRestResourceComponent.updateCreate");
            else if (Configuration.doAutoCreate())
              this.updateCreate = new BooleanType(); // bb
          return this.updateCreate;
        }

        public boolean hasUpdateCreateElement() { 
          return this.updateCreate != null && !this.updateCreate.isEmpty();
        }

        public boolean hasUpdateCreate() { 
          return this.updateCreate != null && !this.updateCreate.isEmpty();
        }

        /**
         * @param value {@link #updateCreate} (A flag to indicate that the server allows or needs to allow the client to create new identities on the server (e.g. that is, the client PUTs to a location where there is no existing resource). Allowing this operation means that the server allows the client to create new identities on the server.). This is the underlying object with id, value and extensions. The accessor "getUpdateCreate" gives direct access to the value
         */
        public CapabilityStatementRestResourceComponent setUpdateCreateElement(BooleanType value) { 
          this.updateCreate = value;
          return this;
        }

        /**
         * @return A flag to indicate that the server allows or needs to allow the client to create new identities on the server (e.g. that is, the client PUTs to a location where there is no existing resource). Allowing this operation means that the server allows the client to create new identities on the server.
         */
        public boolean getUpdateCreate() { 
          return this.updateCreate == null || this.updateCreate.isEmpty() ? false : this.updateCreate.getValue();
        }

        /**
         * @param value A flag to indicate that the server allows or needs to allow the client to create new identities on the server (e.g. that is, the client PUTs to a location where there is no existing resource). Allowing this operation means that the server allows the client to create new identities on the server.
         */
        public CapabilityStatementRestResourceComponent setUpdateCreate(boolean value) { 
            if (this.updateCreate == null)
              this.updateCreate = new BooleanType();
            this.updateCreate.setValue(value);
          return this;
        }

        /**
         * @return {@link #conditionalCreate} (A flag that indicates that the server supports conditional create.). This is the underlying object with id, value and extensions. The accessor "getConditionalCreate" gives direct access to the value
         */
        public BooleanType getConditionalCreateElement() { 
          if (this.conditionalCreate == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementRestResourceComponent.conditionalCreate");
            else if (Configuration.doAutoCreate())
              this.conditionalCreate = new BooleanType(); // bb
          return this.conditionalCreate;
        }

        public boolean hasConditionalCreateElement() { 
          return this.conditionalCreate != null && !this.conditionalCreate.isEmpty();
        }

        public boolean hasConditionalCreate() { 
          return this.conditionalCreate != null && !this.conditionalCreate.isEmpty();
        }

        /**
         * @param value {@link #conditionalCreate} (A flag that indicates that the server supports conditional create.). This is the underlying object with id, value and extensions. The accessor "getConditionalCreate" gives direct access to the value
         */
        public CapabilityStatementRestResourceComponent setConditionalCreateElement(BooleanType value) { 
          this.conditionalCreate = value;
          return this;
        }

        /**
         * @return A flag that indicates that the server supports conditional create.
         */
        public boolean getConditionalCreate() { 
          return this.conditionalCreate == null || this.conditionalCreate.isEmpty() ? false : this.conditionalCreate.getValue();
        }

        /**
         * @param value A flag that indicates that the server supports conditional create.
         */
        public CapabilityStatementRestResourceComponent setConditionalCreate(boolean value) { 
            if (this.conditionalCreate == null)
              this.conditionalCreate = new BooleanType();
            this.conditionalCreate.setValue(value);
          return this;
        }

        /**
         * @return {@link #conditionalRead} (A code that indicates how the server supports conditional read.). This is the underlying object with id, value and extensions. The accessor "getConditionalRead" gives direct access to the value
         */
        public Enumeration<ConditionalReadStatus> getConditionalReadElement() { 
          if (this.conditionalRead == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementRestResourceComponent.conditionalRead");
            else if (Configuration.doAutoCreate())
              this.conditionalRead = new Enumeration<ConditionalReadStatus>(new ConditionalReadStatusEnumFactory()); // bb
          return this.conditionalRead;
        }

        public boolean hasConditionalReadElement() { 
          return this.conditionalRead != null && !this.conditionalRead.isEmpty();
        }

        public boolean hasConditionalRead() { 
          return this.conditionalRead != null && !this.conditionalRead.isEmpty();
        }

        /**
         * @param value {@link #conditionalRead} (A code that indicates how the server supports conditional read.). This is the underlying object with id, value and extensions. The accessor "getConditionalRead" gives direct access to the value
         */
        public CapabilityStatementRestResourceComponent setConditionalReadElement(Enumeration<ConditionalReadStatus> value) { 
          this.conditionalRead = value;
          return this;
        }

        /**
         * @return A code that indicates how the server supports conditional read.
         */
        public ConditionalReadStatus getConditionalRead() { 
          return this.conditionalRead == null ? null : this.conditionalRead.getValue();
        }

        /**
         * @param value A code that indicates how the server supports conditional read.
         */
        public CapabilityStatementRestResourceComponent setConditionalRead(ConditionalReadStatus value) { 
          if (value == null)
            this.conditionalRead = null;
          else {
            if (this.conditionalRead == null)
              this.conditionalRead = new Enumeration<ConditionalReadStatus>(new ConditionalReadStatusEnumFactory());
            this.conditionalRead.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #conditionalUpdate} (A flag that indicates that the server supports conditional update.). This is the underlying object with id, value and extensions. The accessor "getConditionalUpdate" gives direct access to the value
         */
        public BooleanType getConditionalUpdateElement() { 
          if (this.conditionalUpdate == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementRestResourceComponent.conditionalUpdate");
            else if (Configuration.doAutoCreate())
              this.conditionalUpdate = new BooleanType(); // bb
          return this.conditionalUpdate;
        }

        public boolean hasConditionalUpdateElement() { 
          return this.conditionalUpdate != null && !this.conditionalUpdate.isEmpty();
        }

        public boolean hasConditionalUpdate() { 
          return this.conditionalUpdate != null && !this.conditionalUpdate.isEmpty();
        }

        /**
         * @param value {@link #conditionalUpdate} (A flag that indicates that the server supports conditional update.). This is the underlying object with id, value and extensions. The accessor "getConditionalUpdate" gives direct access to the value
         */
        public CapabilityStatementRestResourceComponent setConditionalUpdateElement(BooleanType value) { 
          this.conditionalUpdate = value;
          return this;
        }

        /**
         * @return A flag that indicates that the server supports conditional update.
         */
        public boolean getConditionalUpdate() { 
          return this.conditionalUpdate == null || this.conditionalUpdate.isEmpty() ? false : this.conditionalUpdate.getValue();
        }

        /**
         * @param value A flag that indicates that the server supports conditional update.
         */
        public CapabilityStatementRestResourceComponent setConditionalUpdate(boolean value) { 
            if (this.conditionalUpdate == null)
              this.conditionalUpdate = new BooleanType();
            this.conditionalUpdate.setValue(value);
          return this;
        }

        /**
         * @return {@link #conditionalDelete} (A code that indicates how the server supports conditional delete.). This is the underlying object with id, value and extensions. The accessor "getConditionalDelete" gives direct access to the value
         */
        public Enumeration<ConditionalDeleteStatus> getConditionalDeleteElement() { 
          if (this.conditionalDelete == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementRestResourceComponent.conditionalDelete");
            else if (Configuration.doAutoCreate())
              this.conditionalDelete = new Enumeration<ConditionalDeleteStatus>(new ConditionalDeleteStatusEnumFactory()); // bb
          return this.conditionalDelete;
        }

        public boolean hasConditionalDeleteElement() { 
          return this.conditionalDelete != null && !this.conditionalDelete.isEmpty();
        }

        public boolean hasConditionalDelete() { 
          return this.conditionalDelete != null && !this.conditionalDelete.isEmpty();
        }

        /**
         * @param value {@link #conditionalDelete} (A code that indicates how the server supports conditional delete.). This is the underlying object with id, value and extensions. The accessor "getConditionalDelete" gives direct access to the value
         */
        public CapabilityStatementRestResourceComponent setConditionalDeleteElement(Enumeration<ConditionalDeleteStatus> value) { 
          this.conditionalDelete = value;
          return this;
        }

        /**
         * @return A code that indicates how the server supports conditional delete.
         */
        public ConditionalDeleteStatus getConditionalDelete() { 
          return this.conditionalDelete == null ? null : this.conditionalDelete.getValue();
        }

        /**
         * @param value A code that indicates how the server supports conditional delete.
         */
        public CapabilityStatementRestResourceComponent setConditionalDelete(ConditionalDeleteStatus value) { 
          if (value == null)
            this.conditionalDelete = null;
          else {
            if (this.conditionalDelete == null)
              this.conditionalDelete = new Enumeration<ConditionalDeleteStatus>(new ConditionalDeleteStatusEnumFactory());
            this.conditionalDelete.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #referencePolicy} (A set of flags that defines how references are supported.)
         */
        public List<Enumeration<ReferenceHandlingPolicy>> getReferencePolicy() { 
          if (this.referencePolicy == null)
            this.referencePolicy = new ArrayList<Enumeration<ReferenceHandlingPolicy>>();
          return this.referencePolicy;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public CapabilityStatementRestResourceComponent setReferencePolicy(List<Enumeration<ReferenceHandlingPolicy>> theReferencePolicy) { 
          this.referencePolicy = theReferencePolicy;
          return this;
        }

        public boolean hasReferencePolicy() { 
          if (this.referencePolicy == null)
            return false;
          for (Enumeration<ReferenceHandlingPolicy> item : this.referencePolicy)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #referencePolicy} (A set of flags that defines how references are supported.)
         */
        public Enumeration<ReferenceHandlingPolicy> addReferencePolicyElement() {//2 
          Enumeration<ReferenceHandlingPolicy> t = new Enumeration<ReferenceHandlingPolicy>(new ReferenceHandlingPolicyEnumFactory());
          if (this.referencePolicy == null)
            this.referencePolicy = new ArrayList<Enumeration<ReferenceHandlingPolicy>>();
          this.referencePolicy.add(t);
          return t;
        }

        /**
         * @param value {@link #referencePolicy} (A set of flags that defines how references are supported.)
         */
        public CapabilityStatementRestResourceComponent addReferencePolicy(ReferenceHandlingPolicy value) { //1
          Enumeration<ReferenceHandlingPolicy> t = new Enumeration<ReferenceHandlingPolicy>(new ReferenceHandlingPolicyEnumFactory());
          t.setValue(value);
          if (this.referencePolicy == null)
            this.referencePolicy = new ArrayList<Enumeration<ReferenceHandlingPolicy>>();
          this.referencePolicy.add(t);
          return this;
        }

        /**
         * @param value {@link #referencePolicy} (A set of flags that defines how references are supported.)
         */
        public boolean hasReferencePolicy(ReferenceHandlingPolicy value) { 
          if (this.referencePolicy == null)
            return false;
          for (Enumeration<ReferenceHandlingPolicy> v : this.referencePolicy)
            if (v.getValue().equals(value)) // code
              return true;
          return false;
        }

        /**
         * @return {@link #searchInclude} (A list of _include values supported by the server.)
         */
        public List<StringType> getSearchInclude() { 
          if (this.searchInclude == null)
            this.searchInclude = new ArrayList<StringType>();
          return this.searchInclude;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public CapabilityStatementRestResourceComponent setSearchInclude(List<StringType> theSearchInclude) { 
          this.searchInclude = theSearchInclude;
          return this;
        }

        public boolean hasSearchInclude() { 
          if (this.searchInclude == null)
            return false;
          for (StringType item : this.searchInclude)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #searchInclude} (A list of _include values supported by the server.)
         */
        public StringType addSearchIncludeElement() {//2 
          StringType t = new StringType();
          if (this.searchInclude == null)
            this.searchInclude = new ArrayList<StringType>();
          this.searchInclude.add(t);
          return t;
        }

        /**
         * @param value {@link #searchInclude} (A list of _include values supported by the server.)
         */
        public CapabilityStatementRestResourceComponent addSearchInclude(String value) { //1
          StringType t = new StringType();
          t.setValue(value);
          if (this.searchInclude == null)
            this.searchInclude = new ArrayList<StringType>();
          this.searchInclude.add(t);
          return this;
        }

        /**
         * @param value {@link #searchInclude} (A list of _include values supported by the server.)
         */
        public boolean hasSearchInclude(String value) { 
          if (this.searchInclude == null)
            return false;
          for (StringType v : this.searchInclude)
            if (v.equals(value)) // string
              return true;
          return false;
        }

        /**
         * @return {@link #searchRevInclude} (A list of _revinclude (reverse include) values supported by the server.)
         */
        public List<StringType> getSearchRevInclude() { 
          if (this.searchRevInclude == null)
            this.searchRevInclude = new ArrayList<StringType>();
          return this.searchRevInclude;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public CapabilityStatementRestResourceComponent setSearchRevInclude(List<StringType> theSearchRevInclude) { 
          this.searchRevInclude = theSearchRevInclude;
          return this;
        }

        public boolean hasSearchRevInclude() { 
          if (this.searchRevInclude == null)
            return false;
          for (StringType item : this.searchRevInclude)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #searchRevInclude} (A list of _revinclude (reverse include) values supported by the server.)
         */
        public StringType addSearchRevIncludeElement() {//2 
          StringType t = new StringType();
          if (this.searchRevInclude == null)
            this.searchRevInclude = new ArrayList<StringType>();
          this.searchRevInclude.add(t);
          return t;
        }

        /**
         * @param value {@link #searchRevInclude} (A list of _revinclude (reverse include) values supported by the server.)
         */
        public CapabilityStatementRestResourceComponent addSearchRevInclude(String value) { //1
          StringType t = new StringType();
          t.setValue(value);
          if (this.searchRevInclude == null)
            this.searchRevInclude = new ArrayList<StringType>();
          this.searchRevInclude.add(t);
          return this;
        }

        /**
         * @param value {@link #searchRevInclude} (A list of _revinclude (reverse include) values supported by the server.)
         */
        public boolean hasSearchRevInclude(String value) { 
          if (this.searchRevInclude == null)
            return false;
          for (StringType v : this.searchRevInclude)
            if (v.equals(value)) // string
              return true;
          return false;
        }

        /**
         * @return {@link #searchParam} (Search parameters for implementations to support and/or make use of - either references to ones defined in the specification, or additional ones defined for/by the implementation.)
         */
        public List<CapabilityStatementRestResourceSearchParamComponent> getSearchParam() { 
          if (this.searchParam == null)
            this.searchParam = new ArrayList<CapabilityStatementRestResourceSearchParamComponent>();
          return this.searchParam;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public CapabilityStatementRestResourceComponent setSearchParam(List<CapabilityStatementRestResourceSearchParamComponent> theSearchParam) { 
          this.searchParam = theSearchParam;
          return this;
        }

        public boolean hasSearchParam() { 
          if (this.searchParam == null)
            return false;
          for (CapabilityStatementRestResourceSearchParamComponent item : this.searchParam)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CapabilityStatementRestResourceSearchParamComponent addSearchParam() { //3
          CapabilityStatementRestResourceSearchParamComponent t = new CapabilityStatementRestResourceSearchParamComponent();
          if (this.searchParam == null)
            this.searchParam = new ArrayList<CapabilityStatementRestResourceSearchParamComponent>();
          this.searchParam.add(t);
          return t;
        }

        public CapabilityStatementRestResourceComponent addSearchParam(CapabilityStatementRestResourceSearchParamComponent t) { //3
          if (t == null)
            return this;
          if (this.searchParam == null)
            this.searchParam = new ArrayList<CapabilityStatementRestResourceSearchParamComponent>();
          this.searchParam.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #searchParam}, creating it if it does not already exist
         */
        public CapabilityStatementRestResourceSearchParamComponent getSearchParamFirstRep() { 
          if (getSearchParam().isEmpty()) {
            addSearchParam();
          }
          return getSearchParam().get(0);
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "code", "A type of resource exposed via the restful interface.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("profile", "Reference(StructureDefinition)", "A specification of the profile that describes the solution's overall support for the resource, including any constraints on cardinality, bindings, lengths or other limitations. See further discussion in [Using Profiles](profiling.html#profile-uses).", 0, java.lang.Integer.MAX_VALUE, profile));
          childrenList.add(new Property("documentation", "markdown", "Additional information about the resource type used by the system.", 0, java.lang.Integer.MAX_VALUE, documentation));
          childrenList.add(new Property("interaction", "", "Identifies a restful operation supported by the solution.", 0, java.lang.Integer.MAX_VALUE, interaction));
          childrenList.add(new Property("versioning", "code", "This field is set to no-version to specify that the system does not support (server) or use (client) versioning for this resource type. If this has some other value, the server must at least correctly track and populate the versionId meta-property on resources. If the value is 'versioned-update', then the server supports all the versioning features, including using e-tags for version integrity in the API.", 0, java.lang.Integer.MAX_VALUE, versioning));
          childrenList.add(new Property("readHistory", "boolean", "A flag for whether the server is able to return past versions as part of the vRead operation.", 0, java.lang.Integer.MAX_VALUE, readHistory));
          childrenList.add(new Property("updateCreate", "boolean", "A flag to indicate that the server allows or needs to allow the client to create new identities on the server (e.g. that is, the client PUTs to a location where there is no existing resource). Allowing this operation means that the server allows the client to create new identities on the server.", 0, java.lang.Integer.MAX_VALUE, updateCreate));
          childrenList.add(new Property("conditionalCreate", "boolean", "A flag that indicates that the server supports conditional create.", 0, java.lang.Integer.MAX_VALUE, conditionalCreate));
          childrenList.add(new Property("conditionalRead", "code", "A code that indicates how the server supports conditional read.", 0, java.lang.Integer.MAX_VALUE, conditionalRead));
          childrenList.add(new Property("conditionalUpdate", "boolean", "A flag that indicates that the server supports conditional update.", 0, java.lang.Integer.MAX_VALUE, conditionalUpdate));
          childrenList.add(new Property("conditionalDelete", "code", "A code that indicates how the server supports conditional delete.", 0, java.lang.Integer.MAX_VALUE, conditionalDelete));
          childrenList.add(new Property("referencePolicy", "code", "A set of flags that defines how references are supported.", 0, java.lang.Integer.MAX_VALUE, referencePolicy));
          childrenList.add(new Property("searchInclude", "string", "A list of _include values supported by the server.", 0, java.lang.Integer.MAX_VALUE, searchInclude));
          childrenList.add(new Property("searchRevInclude", "string", "A list of _revinclude (reverse include) values supported by the server.", 0, java.lang.Integer.MAX_VALUE, searchRevInclude));
          childrenList.add(new Property("searchParam", "", "Search parameters for implementations to support and/or make use of - either references to ones defined in the specification, or additional ones defined for/by the implementation.", 0, java.lang.Integer.MAX_VALUE, searchParam));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeType
        case -309425751: /*profile*/ return this.profile == null ? new Base[0] : new Base[] {this.profile}; // Reference
        case 1587405498: /*documentation*/ return this.documentation == null ? new Base[0] : new Base[] {this.documentation}; // MarkdownType
        case 1844104722: /*interaction*/ return this.interaction == null ? new Base[0] : this.interaction.toArray(new Base[this.interaction.size()]); // ResourceInteractionComponent
        case -670487542: /*versioning*/ return this.versioning == null ? new Base[0] : new Base[] {this.versioning}; // Enumeration<ResourceVersionPolicy>
        case 187518494: /*readHistory*/ return this.readHistory == null ? new Base[0] : new Base[] {this.readHistory}; // BooleanType
        case -1400550619: /*updateCreate*/ return this.updateCreate == null ? new Base[0] : new Base[] {this.updateCreate}; // BooleanType
        case 6401826: /*conditionalCreate*/ return this.conditionalCreate == null ? new Base[0] : new Base[] {this.conditionalCreate}; // BooleanType
        case 822786364: /*conditionalRead*/ return this.conditionalRead == null ? new Base[0] : new Base[] {this.conditionalRead}; // Enumeration<ConditionalReadStatus>
        case 519849711: /*conditionalUpdate*/ return this.conditionalUpdate == null ? new Base[0] : new Base[] {this.conditionalUpdate}; // BooleanType
        case 23237585: /*conditionalDelete*/ return this.conditionalDelete == null ? new Base[0] : new Base[] {this.conditionalDelete}; // Enumeration<ConditionalDeleteStatus>
        case 796257373: /*referencePolicy*/ return this.referencePolicy == null ? new Base[0] : this.referencePolicy.toArray(new Base[this.referencePolicy.size()]); // Enumeration<ReferenceHandlingPolicy>
        case -1035904544: /*searchInclude*/ return this.searchInclude == null ? new Base[0] : this.searchInclude.toArray(new Base[this.searchInclude.size()]); // StringType
        case -2123884979: /*searchRevInclude*/ return this.searchRevInclude == null ? new Base[0] : this.searchRevInclude.toArray(new Base[this.searchRevInclude.size()]); // StringType
        case -553645115: /*searchParam*/ return this.searchParam == null ? new Base[0] : this.searchParam.toArray(new Base[this.searchParam.size()]); // CapabilityStatementRestResourceSearchParamComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCode(value); // CodeType
          return value;
        case -309425751: // profile
          this.profile = castToReference(value); // Reference
          return value;
        case 1587405498: // documentation
          this.documentation = castToMarkdown(value); // MarkdownType
          return value;
        case 1844104722: // interaction
          this.getInteraction().add((ResourceInteractionComponent) value); // ResourceInteractionComponent
          return value;
        case -670487542: // versioning
          value = new ResourceVersionPolicyEnumFactory().fromType(castToCode(value));
          this.versioning = (Enumeration) value; // Enumeration<ResourceVersionPolicy>
          return value;
        case 187518494: // readHistory
          this.readHistory = castToBoolean(value); // BooleanType
          return value;
        case -1400550619: // updateCreate
          this.updateCreate = castToBoolean(value); // BooleanType
          return value;
        case 6401826: // conditionalCreate
          this.conditionalCreate = castToBoolean(value); // BooleanType
          return value;
        case 822786364: // conditionalRead
          value = new ConditionalReadStatusEnumFactory().fromType(castToCode(value));
          this.conditionalRead = (Enumeration) value; // Enumeration<ConditionalReadStatus>
          return value;
        case 519849711: // conditionalUpdate
          this.conditionalUpdate = castToBoolean(value); // BooleanType
          return value;
        case 23237585: // conditionalDelete
          value = new ConditionalDeleteStatusEnumFactory().fromType(castToCode(value));
          this.conditionalDelete = (Enumeration) value; // Enumeration<ConditionalDeleteStatus>
          return value;
        case 796257373: // referencePolicy
          value = new ReferenceHandlingPolicyEnumFactory().fromType(castToCode(value));
          this.getReferencePolicy().add((Enumeration) value); // Enumeration<ReferenceHandlingPolicy>
          return value;
        case -1035904544: // searchInclude
          this.getSearchInclude().add(castToString(value)); // StringType
          return value;
        case -2123884979: // searchRevInclude
          this.getSearchRevInclude().add(castToString(value)); // StringType
          return value;
        case -553645115: // searchParam
          this.getSearchParam().add((CapabilityStatementRestResourceSearchParamComponent) value); // CapabilityStatementRestResourceSearchParamComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToCode(value); // CodeType
        } else if (name.equals("profile")) {
          this.profile = castToReference(value); // Reference
        } else if (name.equals("documentation")) {
          this.documentation = castToMarkdown(value); // MarkdownType
        } else if (name.equals("interaction")) {
          this.getInteraction().add((ResourceInteractionComponent) value);
        } else if (name.equals("versioning")) {
          value = new ResourceVersionPolicyEnumFactory().fromType(castToCode(value));
          this.versioning = (Enumeration) value; // Enumeration<ResourceVersionPolicy>
        } else if (name.equals("readHistory")) {
          this.readHistory = castToBoolean(value); // BooleanType
        } else if (name.equals("updateCreate")) {
          this.updateCreate = castToBoolean(value); // BooleanType
        } else if (name.equals("conditionalCreate")) {
          this.conditionalCreate = castToBoolean(value); // BooleanType
        } else if (name.equals("conditionalRead")) {
          value = new ConditionalReadStatusEnumFactory().fromType(castToCode(value));
          this.conditionalRead = (Enumeration) value; // Enumeration<ConditionalReadStatus>
        } else if (name.equals("conditionalUpdate")) {
          this.conditionalUpdate = castToBoolean(value); // BooleanType
        } else if (name.equals("conditionalDelete")) {
          value = new ConditionalDeleteStatusEnumFactory().fromType(castToCode(value));
          this.conditionalDelete = (Enumeration) value; // Enumeration<ConditionalDeleteStatus>
        } else if (name.equals("referencePolicy")) {
          value = new ReferenceHandlingPolicyEnumFactory().fromType(castToCode(value));
          this.getReferencePolicy().add((Enumeration) value);
        } else if (name.equals("searchInclude")) {
          this.getSearchInclude().add(castToString(value));
        } else if (name.equals("searchRevInclude")) {
          this.getSearchRevInclude().add(castToString(value));
        } else if (name.equals("searchParam")) {
          this.getSearchParam().add((CapabilityStatementRestResourceSearchParamComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getTypeElement();
        case -309425751:  return getProfile(); 
        case 1587405498:  return getDocumentationElement();
        case 1844104722:  return addInteraction(); 
        case -670487542:  return getVersioningElement();
        case 187518494:  return getReadHistoryElement();
        case -1400550619:  return getUpdateCreateElement();
        case 6401826:  return getConditionalCreateElement();
        case 822786364:  return getConditionalReadElement();
        case 519849711:  return getConditionalUpdateElement();
        case 23237585:  return getConditionalDeleteElement();
        case 796257373:  return addReferencePolicyElement();
        case -1035904544:  return addSearchIncludeElement();
        case -2123884979:  return addSearchRevIncludeElement();
        case -553645115:  return addSearchParam(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"code"};
        case -309425751: /*profile*/ return new String[] {"Reference"};
        case 1587405498: /*documentation*/ return new String[] {"markdown"};
        case 1844104722: /*interaction*/ return new String[] {};
        case -670487542: /*versioning*/ return new String[] {"code"};
        case 187518494: /*readHistory*/ return new String[] {"boolean"};
        case -1400550619: /*updateCreate*/ return new String[] {"boolean"};
        case 6401826: /*conditionalCreate*/ return new String[] {"boolean"};
        case 822786364: /*conditionalRead*/ return new String[] {"code"};
        case 519849711: /*conditionalUpdate*/ return new String[] {"boolean"};
        case 23237585: /*conditionalDelete*/ return new String[] {"code"};
        case 796257373: /*referencePolicy*/ return new String[] {"code"};
        case -1035904544: /*searchInclude*/ return new String[] {"string"};
        case -2123884979: /*searchRevInclude*/ return new String[] {"string"};
        case -553645115: /*searchParam*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.type");
        }
        else if (name.equals("profile")) {
          this.profile = new Reference();
          return this.profile;
        }
        else if (name.equals("documentation")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.documentation");
        }
        else if (name.equals("interaction")) {
          return addInteraction();
        }
        else if (name.equals("versioning")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.versioning");
        }
        else if (name.equals("readHistory")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.readHistory");
        }
        else if (name.equals("updateCreate")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.updateCreate");
        }
        else if (name.equals("conditionalCreate")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.conditionalCreate");
        }
        else if (name.equals("conditionalRead")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.conditionalRead");
        }
        else if (name.equals("conditionalUpdate")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.conditionalUpdate");
        }
        else if (name.equals("conditionalDelete")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.conditionalDelete");
        }
        else if (name.equals("referencePolicy")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.referencePolicy");
        }
        else if (name.equals("searchInclude")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.searchInclude");
        }
        else if (name.equals("searchRevInclude")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.searchRevInclude");
        }
        else if (name.equals("searchParam")) {
          return addSearchParam();
        }
        else
          return super.addChild(name);
      }

      public CapabilityStatementRestResourceComponent copy() {
        CapabilityStatementRestResourceComponent dst = new CapabilityStatementRestResourceComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.profile = profile == null ? null : profile.copy();
        dst.documentation = documentation == null ? null : documentation.copy();
        if (interaction != null) {
          dst.interaction = new ArrayList<ResourceInteractionComponent>();
          for (ResourceInteractionComponent i : interaction)
            dst.interaction.add(i.copy());
        };
        dst.versioning = versioning == null ? null : versioning.copy();
        dst.readHistory = readHistory == null ? null : readHistory.copy();
        dst.updateCreate = updateCreate == null ? null : updateCreate.copy();
        dst.conditionalCreate = conditionalCreate == null ? null : conditionalCreate.copy();
        dst.conditionalRead = conditionalRead == null ? null : conditionalRead.copy();
        dst.conditionalUpdate = conditionalUpdate == null ? null : conditionalUpdate.copy();
        dst.conditionalDelete = conditionalDelete == null ? null : conditionalDelete.copy();
        if (referencePolicy != null) {
          dst.referencePolicy = new ArrayList<Enumeration<ReferenceHandlingPolicy>>();
          for (Enumeration<ReferenceHandlingPolicy> i : referencePolicy)
            dst.referencePolicy.add(i.copy());
        };
        if (searchInclude != null) {
          dst.searchInclude = new ArrayList<StringType>();
          for (StringType i : searchInclude)
            dst.searchInclude.add(i.copy());
        };
        if (searchRevInclude != null) {
          dst.searchRevInclude = new ArrayList<StringType>();
          for (StringType i : searchRevInclude)
            dst.searchRevInclude.add(i.copy());
        };
        if (searchParam != null) {
          dst.searchParam = new ArrayList<CapabilityStatementRestResourceSearchParamComponent>();
          for (CapabilityStatementRestResourceSearchParamComponent i : searchParam)
            dst.searchParam.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CapabilityStatementRestResourceComponent))
          return false;
        CapabilityStatementRestResourceComponent o = (CapabilityStatementRestResourceComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(profile, o.profile, true) && compareDeep(documentation, o.documentation, true)
           && compareDeep(interaction, o.interaction, true) && compareDeep(versioning, o.versioning, true)
           && compareDeep(readHistory, o.readHistory, true) && compareDeep(updateCreate, o.updateCreate, true)
           && compareDeep(conditionalCreate, o.conditionalCreate, true) && compareDeep(conditionalRead, o.conditionalRead, true)
           && compareDeep(conditionalUpdate, o.conditionalUpdate, true) && compareDeep(conditionalDelete, o.conditionalDelete, true)
           && compareDeep(referencePolicy, o.referencePolicy, true) && compareDeep(searchInclude, o.searchInclude, true)
           && compareDeep(searchRevInclude, o.searchRevInclude, true) && compareDeep(searchParam, o.searchParam, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CapabilityStatementRestResourceComponent))
          return false;
        CapabilityStatementRestResourceComponent o = (CapabilityStatementRestResourceComponent) other;
        return compareValues(type, o.type, true) && compareValues(documentation, o.documentation, true) && compareValues(versioning, o.versioning, true)
           && compareValues(readHistory, o.readHistory, true) && compareValues(updateCreate, o.updateCreate, true)
           && compareValues(conditionalCreate, o.conditionalCreate, true) && compareValues(conditionalRead, o.conditionalRead, true)
           && compareValues(conditionalUpdate, o.conditionalUpdate, true) && compareValues(conditionalDelete, o.conditionalDelete, true)
           && compareValues(referencePolicy, o.referencePolicy, true) && compareValues(searchInclude, o.searchInclude, true)
           && compareValues(searchRevInclude, o.searchRevInclude, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, profile, documentation
          , interaction, versioning, readHistory, updateCreate, conditionalCreate, conditionalRead
          , conditionalUpdate, conditionalDelete, referencePolicy, searchInclude, searchRevInclude
          , searchParam);
      }

  public String fhirType() {
    return "CapabilityStatement.rest.resource";

  }

  }

    @Block()
    public static class ResourceInteractionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Coded identifier of the operation, supported by the system resource.
         */
        @Child(name = "code", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="read | vread | update | patch | delete | history-instance | history-type | create | search-type", formalDefinition="Coded identifier of the operation, supported by the system resource." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/type-restful-interaction")
        protected Enumeration<TypeRestfulInteraction> code;

        /**
         * Guidance specific to the implementation of this operation, such as 'delete is a logical delete' or 'updates are only allowed with version id' or 'creates permitted from pre-authorized certificates only'.
         */
        @Child(name = "documentation", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Anything special about operation behavior", formalDefinition="Guidance specific to the implementation of this operation, such as 'delete is a logical delete' or 'updates are only allowed with version id' or 'creates permitted from pre-authorized certificates only'." )
        protected StringType documentation;

        private static final long serialVersionUID = -437507806L;

    /**
     * Constructor
     */
      public ResourceInteractionComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ResourceInteractionComponent(Enumeration<TypeRestfulInteraction> code) {
        super();
        this.code = code;
      }

        /**
         * @return {@link #code} (Coded identifier of the operation, supported by the system resource.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public Enumeration<TypeRestfulInteraction> getCodeElement() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ResourceInteractionComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new Enumeration<TypeRestfulInteraction>(new TypeRestfulInteractionEnumFactory()); // bb
          return this.code;
        }

        public boolean hasCodeElement() { 
          return this.code != null && !this.code.isEmpty();
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (Coded identifier of the operation, supported by the system resource.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public ResourceInteractionComponent setCodeElement(Enumeration<TypeRestfulInteraction> value) { 
          this.code = value;
          return this;
        }

        /**
         * @return Coded identifier of the operation, supported by the system resource.
         */
        public TypeRestfulInteraction getCode() { 
          return this.code == null ? null : this.code.getValue();
        }

        /**
         * @param value Coded identifier of the operation, supported by the system resource.
         */
        public ResourceInteractionComponent setCode(TypeRestfulInteraction value) { 
            if (this.code == null)
              this.code = new Enumeration<TypeRestfulInteraction>(new TypeRestfulInteractionEnumFactory());
            this.code.setValue(value);
          return this;
        }

        /**
         * @return {@link #documentation} (Guidance specific to the implementation of this operation, such as 'delete is a logical delete' or 'updates are only allowed with version id' or 'creates permitted from pre-authorized certificates only'.). This is the underlying object with id, value and extensions. The accessor "getDocumentation" gives direct access to the value
         */
        public StringType getDocumentationElement() { 
          if (this.documentation == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ResourceInteractionComponent.documentation");
            else if (Configuration.doAutoCreate())
              this.documentation = new StringType(); // bb
          return this.documentation;
        }

        public boolean hasDocumentationElement() { 
          return this.documentation != null && !this.documentation.isEmpty();
        }

        public boolean hasDocumentation() { 
          return this.documentation != null && !this.documentation.isEmpty();
        }

        /**
         * @param value {@link #documentation} (Guidance specific to the implementation of this operation, such as 'delete is a logical delete' or 'updates are only allowed with version id' or 'creates permitted from pre-authorized certificates only'.). This is the underlying object with id, value and extensions. The accessor "getDocumentation" gives direct access to the value
         */
        public ResourceInteractionComponent setDocumentationElement(StringType value) { 
          this.documentation = value;
          return this;
        }

        /**
         * @return Guidance specific to the implementation of this operation, such as 'delete is a logical delete' or 'updates are only allowed with version id' or 'creates permitted from pre-authorized certificates only'.
         */
        public String getDocumentation() { 
          return this.documentation == null ? null : this.documentation.getValue();
        }

        /**
         * @param value Guidance specific to the implementation of this operation, such as 'delete is a logical delete' or 'updates are only allowed with version id' or 'creates permitted from pre-authorized certificates only'.
         */
        public ResourceInteractionComponent setDocumentation(String value) { 
          if (Utilities.noString(value))
            this.documentation = null;
          else {
            if (this.documentation == null)
              this.documentation = new StringType();
            this.documentation.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("code", "code", "Coded identifier of the operation, supported by the system resource.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("documentation", "string", "Guidance specific to the implementation of this operation, such as 'delete is a logical delete' or 'updates are only allowed with version id' or 'creates permitted from pre-authorized certificates only'.", 0, java.lang.Integer.MAX_VALUE, documentation));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // Enumeration<TypeRestfulInteraction>
        case 1587405498: /*documentation*/ return this.documentation == null ? new Base[0] : new Base[] {this.documentation}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3059181: // code
          value = new TypeRestfulInteractionEnumFactory().fromType(castToCode(value));
          this.code = (Enumeration) value; // Enumeration<TypeRestfulInteraction>
          return value;
        case 1587405498: // documentation
          this.documentation = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code")) {
          value = new TypeRestfulInteractionEnumFactory().fromType(castToCode(value));
          this.code = (Enumeration) value; // Enumeration<TypeRestfulInteraction>
        } else if (name.equals("documentation")) {
          this.documentation = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181:  return getCodeElement();
        case 1587405498:  return getDocumentationElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return new String[] {"code"};
        case 1587405498: /*documentation*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.code");
        }
        else if (name.equals("documentation")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.documentation");
        }
        else
          return super.addChild(name);
      }

      public ResourceInteractionComponent copy() {
        ResourceInteractionComponent dst = new ResourceInteractionComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.documentation = documentation == null ? null : documentation.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ResourceInteractionComponent))
          return false;
        ResourceInteractionComponent o = (ResourceInteractionComponent) other;
        return compareDeep(code, o.code, true) && compareDeep(documentation, o.documentation, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ResourceInteractionComponent))
          return false;
        ResourceInteractionComponent o = (ResourceInteractionComponent) other;
        return compareValues(code, o.code, true) && compareValues(documentation, o.documentation, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(code, documentation);
      }

  public String fhirType() {
    return "CapabilityStatement.rest.resource.interaction";

  }

  }

    @Block()
    public static class CapabilityStatementRestResourceSearchParamComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The name of the search parameter used in the interface.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Name of search parameter", formalDefinition="The name of the search parameter used in the interface." )
        protected StringType name;

        /**
         * An absolute URI that is a formal reference to where this parameter was first defined, so that a client can be confident of the meaning of the search parameter (a reference to [[[SearchParameter.url]]]).
         */
        @Child(name = "definition", type = {UriType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Source of definition for parameter", formalDefinition="An absolute URI that is a formal reference to where this parameter was first defined, so that a client can be confident of the meaning of the search parameter (a reference to [[[SearchParameter.url]]])." )
        protected UriType definition;

        /**
         * The type of value a search parameter refers to, and how the content is interpreted.
         */
        @Child(name = "type", type = {CodeType.class}, order=3, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="number | date | string | token | reference | composite | quantity | uri", formalDefinition="The type of value a search parameter refers to, and how the content is interpreted." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/search-param-type")
        protected Enumeration<SearchParamType> type;

        /**
         * This allows documentation of any distinct behaviors about how the search parameter is used.  For example, text matching algorithms.
         */
        @Child(name = "documentation", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Server-specific usage", formalDefinition="This allows documentation of any distinct behaviors about how the search parameter is used.  For example, text matching algorithms." )
        protected StringType documentation;

        private static final long serialVersionUID = 72133006L;

    /**
     * Constructor
     */
      public CapabilityStatementRestResourceSearchParamComponent() {
        super();
      }

    /**
     * Constructor
     */
      public CapabilityStatementRestResourceSearchParamComponent(StringType name, Enumeration<SearchParamType> type) {
        super();
        this.name = name;
        this.type = type;
      }

        /**
         * @return {@link #name} (The name of the search parameter used in the interface.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementRestResourceSearchParamComponent.name");
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
         * @param value {@link #name} (The name of the search parameter used in the interface.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public CapabilityStatementRestResourceSearchParamComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return The name of the search parameter used in the interface.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value The name of the search parameter used in the interface.
         */
        public CapabilityStatementRestResourceSearchParamComponent setName(String value) { 
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          return this;
        }

        /**
         * @return {@link #definition} (An absolute URI that is a formal reference to where this parameter was first defined, so that a client can be confident of the meaning of the search parameter (a reference to [[[SearchParameter.url]]]).). This is the underlying object with id, value and extensions. The accessor "getDefinition" gives direct access to the value
         */
        public UriType getDefinitionElement() { 
          if (this.definition == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementRestResourceSearchParamComponent.definition");
            else if (Configuration.doAutoCreate())
              this.definition = new UriType(); // bb
          return this.definition;
        }

        public boolean hasDefinitionElement() { 
          return this.definition != null && !this.definition.isEmpty();
        }

        public boolean hasDefinition() { 
          return this.definition != null && !this.definition.isEmpty();
        }

        /**
         * @param value {@link #definition} (An absolute URI that is a formal reference to where this parameter was first defined, so that a client can be confident of the meaning of the search parameter (a reference to [[[SearchParameter.url]]]).). This is the underlying object with id, value and extensions. The accessor "getDefinition" gives direct access to the value
         */
        public CapabilityStatementRestResourceSearchParamComponent setDefinitionElement(UriType value) { 
          this.definition = value;
          return this;
        }

        /**
         * @return An absolute URI that is a formal reference to where this parameter was first defined, so that a client can be confident of the meaning of the search parameter (a reference to [[[SearchParameter.url]]]).
         */
        public String getDefinition() { 
          return this.definition == null ? null : this.definition.getValue();
        }

        /**
         * @param value An absolute URI that is a formal reference to where this parameter was first defined, so that a client can be confident of the meaning of the search parameter (a reference to [[[SearchParameter.url]]]).
         */
        public CapabilityStatementRestResourceSearchParamComponent setDefinition(String value) { 
          if (Utilities.noString(value))
            this.definition = null;
          else {
            if (this.definition == null)
              this.definition = new UriType();
            this.definition.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #type} (The type of value a search parameter refers to, and how the content is interpreted.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<SearchParamType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementRestResourceSearchParamComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Enumeration<SearchParamType>(new SearchParamTypeEnumFactory()); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of value a search parameter refers to, and how the content is interpreted.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public CapabilityStatementRestResourceSearchParamComponent setTypeElement(Enumeration<SearchParamType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return The type of value a search parameter refers to, and how the content is interpreted.
         */
        public SearchParamType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value The type of value a search parameter refers to, and how the content is interpreted.
         */
        public CapabilityStatementRestResourceSearchParamComponent setType(SearchParamType value) { 
            if (this.type == null)
              this.type = new Enumeration<SearchParamType>(new SearchParamTypeEnumFactory());
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #documentation} (This allows documentation of any distinct behaviors about how the search parameter is used.  For example, text matching algorithms.). This is the underlying object with id, value and extensions. The accessor "getDocumentation" gives direct access to the value
         */
        public StringType getDocumentationElement() { 
          if (this.documentation == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementRestResourceSearchParamComponent.documentation");
            else if (Configuration.doAutoCreate())
              this.documentation = new StringType(); // bb
          return this.documentation;
        }

        public boolean hasDocumentationElement() { 
          return this.documentation != null && !this.documentation.isEmpty();
        }

        public boolean hasDocumentation() { 
          return this.documentation != null && !this.documentation.isEmpty();
        }

        /**
         * @param value {@link #documentation} (This allows documentation of any distinct behaviors about how the search parameter is used.  For example, text matching algorithms.). This is the underlying object with id, value and extensions. The accessor "getDocumentation" gives direct access to the value
         */
        public CapabilityStatementRestResourceSearchParamComponent setDocumentationElement(StringType value) { 
          this.documentation = value;
          return this;
        }

        /**
         * @return This allows documentation of any distinct behaviors about how the search parameter is used.  For example, text matching algorithms.
         */
        public String getDocumentation() { 
          return this.documentation == null ? null : this.documentation.getValue();
        }

        /**
         * @param value This allows documentation of any distinct behaviors about how the search parameter is used.  For example, text matching algorithms.
         */
        public CapabilityStatementRestResourceSearchParamComponent setDocumentation(String value) { 
          if (Utilities.noString(value))
            this.documentation = null;
          else {
            if (this.documentation == null)
              this.documentation = new StringType();
            this.documentation.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "string", "The name of the search parameter used in the interface.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("definition", "uri", "An absolute URI that is a formal reference to where this parameter was first defined, so that a client can be confident of the meaning of the search parameter (a reference to [[[SearchParameter.url]]]).", 0, java.lang.Integer.MAX_VALUE, definition));
          childrenList.add(new Property("type", "code", "The type of value a search parameter refers to, and how the content is interpreted.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("documentation", "string", "This allows documentation of any distinct behaviors about how the search parameter is used.  For example, text matching algorithms.", 0, java.lang.Integer.MAX_VALUE, documentation));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -1014418093: /*definition*/ return this.definition == null ? new Base[0] : new Base[] {this.definition}; // UriType
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<SearchParamType>
        case 1587405498: /*documentation*/ return this.documentation == null ? new Base[0] : new Base[] {this.documentation}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case -1014418093: // definition
          this.definition = castToUri(value); // UriType
          return value;
        case 3575610: // type
          value = new SearchParamTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<SearchParamType>
          return value;
        case 1587405498: // documentation
          this.documentation = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("definition")) {
          this.definition = castToUri(value); // UriType
        } else if (name.equals("type")) {
          value = new SearchParamTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<SearchParamType>
        } else if (name.equals("documentation")) {
          this.documentation = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707:  return getNameElement();
        case -1014418093:  return getDefinitionElement();
        case 3575610:  return getTypeElement();
        case 1587405498:  return getDocumentationElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return new String[] {"string"};
        case -1014418093: /*definition*/ return new String[] {"uri"};
        case 3575610: /*type*/ return new String[] {"code"};
        case 1587405498: /*documentation*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.name");
        }
        else if (name.equals("definition")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.definition");
        }
        else if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.type");
        }
        else if (name.equals("documentation")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.documentation");
        }
        else
          return super.addChild(name);
      }

      public CapabilityStatementRestResourceSearchParamComponent copy() {
        CapabilityStatementRestResourceSearchParamComponent dst = new CapabilityStatementRestResourceSearchParamComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.definition = definition == null ? null : definition.copy();
        dst.type = type == null ? null : type.copy();
        dst.documentation = documentation == null ? null : documentation.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CapabilityStatementRestResourceSearchParamComponent))
          return false;
        CapabilityStatementRestResourceSearchParamComponent o = (CapabilityStatementRestResourceSearchParamComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(definition, o.definition, true) && compareDeep(type, o.type, true)
           && compareDeep(documentation, o.documentation, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CapabilityStatementRestResourceSearchParamComponent))
          return false;
        CapabilityStatementRestResourceSearchParamComponent o = (CapabilityStatementRestResourceSearchParamComponent) other;
        return compareValues(name, o.name, true) && compareValues(definition, o.definition, true) && compareValues(type, o.type, true)
           && compareValues(documentation, o.documentation, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(name, definition, type, documentation
          );
      }

  public String fhirType() {
    return "CapabilityStatement.rest.resource.searchParam";

  }

  }

    @Block()
    public static class SystemInteractionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A coded identifier of the operation, supported by the system.
         */
        @Child(name = "code", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="transaction | batch | search-system | history-system", formalDefinition="A coded identifier of the operation, supported by the system." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/system-restful-interaction")
        protected Enumeration<SystemRestfulInteraction> code;

        /**
         * Guidance specific to the implementation of this operation, such as limitations on the kind of transactions allowed, or information about system wide search is implemented.
         */
        @Child(name = "documentation", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Anything special about operation behavior", formalDefinition="Guidance specific to the implementation of this operation, such as limitations on the kind of transactions allowed, or information about system wide search is implemented." )
        protected StringType documentation;

        private static final long serialVersionUID = 510675287L;

    /**
     * Constructor
     */
      public SystemInteractionComponent() {
        super();
      }

    /**
     * Constructor
     */
      public SystemInteractionComponent(Enumeration<SystemRestfulInteraction> code) {
        super();
        this.code = code;
      }

        /**
         * @return {@link #code} (A coded identifier of the operation, supported by the system.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public Enumeration<SystemRestfulInteraction> getCodeElement() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SystemInteractionComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new Enumeration<SystemRestfulInteraction>(new SystemRestfulInteractionEnumFactory()); // bb
          return this.code;
        }

        public boolean hasCodeElement() { 
          return this.code != null && !this.code.isEmpty();
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (A coded identifier of the operation, supported by the system.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public SystemInteractionComponent setCodeElement(Enumeration<SystemRestfulInteraction> value) { 
          this.code = value;
          return this;
        }

        /**
         * @return A coded identifier of the operation, supported by the system.
         */
        public SystemRestfulInteraction getCode() { 
          return this.code == null ? null : this.code.getValue();
        }

        /**
         * @param value A coded identifier of the operation, supported by the system.
         */
        public SystemInteractionComponent setCode(SystemRestfulInteraction value) { 
            if (this.code == null)
              this.code = new Enumeration<SystemRestfulInteraction>(new SystemRestfulInteractionEnumFactory());
            this.code.setValue(value);
          return this;
        }

        /**
         * @return {@link #documentation} (Guidance specific to the implementation of this operation, such as limitations on the kind of transactions allowed, or information about system wide search is implemented.). This is the underlying object with id, value and extensions. The accessor "getDocumentation" gives direct access to the value
         */
        public StringType getDocumentationElement() { 
          if (this.documentation == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SystemInteractionComponent.documentation");
            else if (Configuration.doAutoCreate())
              this.documentation = new StringType(); // bb
          return this.documentation;
        }

        public boolean hasDocumentationElement() { 
          return this.documentation != null && !this.documentation.isEmpty();
        }

        public boolean hasDocumentation() { 
          return this.documentation != null && !this.documentation.isEmpty();
        }

        /**
         * @param value {@link #documentation} (Guidance specific to the implementation of this operation, such as limitations on the kind of transactions allowed, or information about system wide search is implemented.). This is the underlying object with id, value and extensions. The accessor "getDocumentation" gives direct access to the value
         */
        public SystemInteractionComponent setDocumentationElement(StringType value) { 
          this.documentation = value;
          return this;
        }

        /**
         * @return Guidance specific to the implementation of this operation, such as limitations on the kind of transactions allowed, or information about system wide search is implemented.
         */
        public String getDocumentation() { 
          return this.documentation == null ? null : this.documentation.getValue();
        }

        /**
         * @param value Guidance specific to the implementation of this operation, such as limitations on the kind of transactions allowed, or information about system wide search is implemented.
         */
        public SystemInteractionComponent setDocumentation(String value) { 
          if (Utilities.noString(value))
            this.documentation = null;
          else {
            if (this.documentation == null)
              this.documentation = new StringType();
            this.documentation.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("code", "code", "A coded identifier of the operation, supported by the system.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("documentation", "string", "Guidance specific to the implementation of this operation, such as limitations on the kind of transactions allowed, or information about system wide search is implemented.", 0, java.lang.Integer.MAX_VALUE, documentation));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // Enumeration<SystemRestfulInteraction>
        case 1587405498: /*documentation*/ return this.documentation == null ? new Base[0] : new Base[] {this.documentation}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3059181: // code
          value = new SystemRestfulInteractionEnumFactory().fromType(castToCode(value));
          this.code = (Enumeration) value; // Enumeration<SystemRestfulInteraction>
          return value;
        case 1587405498: // documentation
          this.documentation = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code")) {
          value = new SystemRestfulInteractionEnumFactory().fromType(castToCode(value));
          this.code = (Enumeration) value; // Enumeration<SystemRestfulInteraction>
        } else if (name.equals("documentation")) {
          this.documentation = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181:  return getCodeElement();
        case 1587405498:  return getDocumentationElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return new String[] {"code"};
        case 1587405498: /*documentation*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.code");
        }
        else if (name.equals("documentation")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.documentation");
        }
        else
          return super.addChild(name);
      }

      public SystemInteractionComponent copy() {
        SystemInteractionComponent dst = new SystemInteractionComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.documentation = documentation == null ? null : documentation.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SystemInteractionComponent))
          return false;
        SystemInteractionComponent o = (SystemInteractionComponent) other;
        return compareDeep(code, o.code, true) && compareDeep(documentation, o.documentation, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SystemInteractionComponent))
          return false;
        SystemInteractionComponent o = (SystemInteractionComponent) other;
        return compareValues(code, o.code, true) && compareValues(documentation, o.documentation, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(code, documentation);
      }

  public String fhirType() {
    return "CapabilityStatement.rest.interaction";

  }

  }

    @Block()
    public static class CapabilityStatementRestOperationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The name of the operation or query. For an operation, this is the name  prefixed with $ and used in the URL. For a query, this is the name used in the _query parameter when the query is called.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Name by which the operation/query is invoked", formalDefinition="The name of the operation or query. For an operation, this is the name  prefixed with $ and used in the URL. For a query, this is the name used in the _query parameter when the query is called." )
        protected StringType name;

        /**
         * Where the formal definition can be found.
         */
        @Child(name = "definition", type = {OperationDefinition.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The defined operation/query", formalDefinition="Where the formal definition can be found." )
        protected Reference definition;

        /**
         * The actual object that is the target of the reference (Where the formal definition can be found.)
         */
        protected OperationDefinition definitionTarget;

        private static final long serialVersionUID = 122107272L;

    /**
     * Constructor
     */
      public CapabilityStatementRestOperationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public CapabilityStatementRestOperationComponent(StringType name, Reference definition) {
        super();
        this.name = name;
        this.definition = definition;
      }

        /**
         * @return {@link #name} (The name of the operation or query. For an operation, this is the name  prefixed with $ and used in the URL. For a query, this is the name used in the _query parameter when the query is called.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementRestOperationComponent.name");
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
         * @param value {@link #name} (The name of the operation or query. For an operation, this is the name  prefixed with $ and used in the URL. For a query, this is the name used in the _query parameter when the query is called.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public CapabilityStatementRestOperationComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return The name of the operation or query. For an operation, this is the name  prefixed with $ and used in the URL. For a query, this is the name used in the _query parameter when the query is called.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value The name of the operation or query. For an operation, this is the name  prefixed with $ and used in the URL. For a query, this is the name used in the _query parameter when the query is called.
         */
        public CapabilityStatementRestOperationComponent setName(String value) { 
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          return this;
        }

        /**
         * @return {@link #definition} (Where the formal definition can be found.)
         */
        public Reference getDefinition() { 
          if (this.definition == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementRestOperationComponent.definition");
            else if (Configuration.doAutoCreate())
              this.definition = new Reference(); // cc
          return this.definition;
        }

        public boolean hasDefinition() { 
          return this.definition != null && !this.definition.isEmpty();
        }

        /**
         * @param value {@link #definition} (Where the formal definition can be found.)
         */
        public CapabilityStatementRestOperationComponent setDefinition(Reference value) { 
          this.definition = value;
          return this;
        }

        /**
         * @return {@link #definition} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Where the formal definition can be found.)
         */
        public OperationDefinition getDefinitionTarget() { 
          if (this.definitionTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementRestOperationComponent.definition");
            else if (Configuration.doAutoCreate())
              this.definitionTarget = new OperationDefinition(); // aa
          return this.definitionTarget;
        }

        /**
         * @param value {@link #definition} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Where the formal definition can be found.)
         */
        public CapabilityStatementRestOperationComponent setDefinitionTarget(OperationDefinition value) { 
          this.definitionTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "string", "The name of the operation or query. For an operation, this is the name  prefixed with $ and used in the URL. For a query, this is the name used in the _query parameter when the query is called.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("definition", "Reference(OperationDefinition)", "Where the formal definition can be found.", 0, java.lang.Integer.MAX_VALUE, definition));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -1014418093: /*definition*/ return this.definition == null ? new Base[0] : new Base[] {this.definition}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case -1014418093: // definition
          this.definition = castToReference(value); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("definition")) {
          this.definition = castToReference(value); // Reference
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707:  return getNameElement();
        case -1014418093:  return getDefinition(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return new String[] {"string"};
        case -1014418093: /*definition*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.name");
        }
        else if (name.equals("definition")) {
          this.definition = new Reference();
          return this.definition;
        }
        else
          return super.addChild(name);
      }

      public CapabilityStatementRestOperationComponent copy() {
        CapabilityStatementRestOperationComponent dst = new CapabilityStatementRestOperationComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.definition = definition == null ? null : definition.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CapabilityStatementRestOperationComponent))
          return false;
        CapabilityStatementRestOperationComponent o = (CapabilityStatementRestOperationComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(definition, o.definition, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CapabilityStatementRestOperationComponent))
          return false;
        CapabilityStatementRestOperationComponent o = (CapabilityStatementRestOperationComponent) other;
        return compareValues(name, o.name, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(name, definition);
      }

  public String fhirType() {
    return "CapabilityStatement.rest.operation";

  }

  }

    @Block()
    public static class CapabilityStatementMessagingComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * An endpoint (network accessible address) to which messages and/or replies are to be sent.
         */
        @Child(name = "endpoint", type = {}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Where messages should be sent", formalDefinition="An endpoint (network accessible address) to which messages and/or replies are to be sent." )
        protected List<CapabilityStatementMessagingEndpointComponent> endpoint;

        /**
         * Length if the receiver's reliable messaging cache in minutes (if a receiver) or how long the cache length on the receiver should be (if a sender).
         */
        @Child(name = "reliableCache", type = {UnsignedIntType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Reliable Message Cache Length (min)", formalDefinition="Length if the receiver's reliable messaging cache in minutes (if a receiver) or how long the cache length on the receiver should be (if a sender)." )
        protected UnsignedIntType reliableCache;

        /**
         * Documentation about the system's messaging capabilities for this endpoint not otherwise documented by the capability statement.  For example, the process for becoming an authorized messaging exchange partner.
         */
        @Child(name = "documentation", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Messaging interface behavior details", formalDefinition="Documentation about the system's messaging capabilities for this endpoint not otherwise documented by the capability statement.  For example, the process for becoming an authorized messaging exchange partner." )
        protected StringType documentation;

        /**
         * References to message definitions for messages this system can send or receive.
         */
        @Child(name = "supportedMessage", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Messages supported by this system", formalDefinition="References to message definitions for messages this system can send or receive." )
        protected List<CapabilityStatementMessagingSupportedMessageComponent> supportedMessage;

        /**
         * A description of the solution's support for an event at this end-point.
         */
        @Child(name = "event", type = {}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Declare support for this event", formalDefinition="A description of the solution's support for an event at this end-point." )
        protected List<CapabilityStatementMessagingEventComponent> event;

        private static final long serialVersionUID = 816243355L;

    /**
     * Constructor
     */
      public CapabilityStatementMessagingComponent() {
        super();
      }

        /**
         * @return {@link #endpoint} (An endpoint (network accessible address) to which messages and/or replies are to be sent.)
         */
        public List<CapabilityStatementMessagingEndpointComponent> getEndpoint() { 
          if (this.endpoint == null)
            this.endpoint = new ArrayList<CapabilityStatementMessagingEndpointComponent>();
          return this.endpoint;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public CapabilityStatementMessagingComponent setEndpoint(List<CapabilityStatementMessagingEndpointComponent> theEndpoint) { 
          this.endpoint = theEndpoint;
          return this;
        }

        public boolean hasEndpoint() { 
          if (this.endpoint == null)
            return false;
          for (CapabilityStatementMessagingEndpointComponent item : this.endpoint)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CapabilityStatementMessagingEndpointComponent addEndpoint() { //3
          CapabilityStatementMessagingEndpointComponent t = new CapabilityStatementMessagingEndpointComponent();
          if (this.endpoint == null)
            this.endpoint = new ArrayList<CapabilityStatementMessagingEndpointComponent>();
          this.endpoint.add(t);
          return t;
        }

        public CapabilityStatementMessagingComponent addEndpoint(CapabilityStatementMessagingEndpointComponent t) { //3
          if (t == null)
            return this;
          if (this.endpoint == null)
            this.endpoint = new ArrayList<CapabilityStatementMessagingEndpointComponent>();
          this.endpoint.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #endpoint}, creating it if it does not already exist
         */
        public CapabilityStatementMessagingEndpointComponent getEndpointFirstRep() { 
          if (getEndpoint().isEmpty()) {
            addEndpoint();
          }
          return getEndpoint().get(0);
        }

        /**
         * @return {@link #reliableCache} (Length if the receiver's reliable messaging cache in minutes (if a receiver) or how long the cache length on the receiver should be (if a sender).). This is the underlying object with id, value and extensions. The accessor "getReliableCache" gives direct access to the value
         */
        public UnsignedIntType getReliableCacheElement() { 
          if (this.reliableCache == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementMessagingComponent.reliableCache");
            else if (Configuration.doAutoCreate())
              this.reliableCache = new UnsignedIntType(); // bb
          return this.reliableCache;
        }

        public boolean hasReliableCacheElement() { 
          return this.reliableCache != null && !this.reliableCache.isEmpty();
        }

        public boolean hasReliableCache() { 
          return this.reliableCache != null && !this.reliableCache.isEmpty();
        }

        /**
         * @param value {@link #reliableCache} (Length if the receiver's reliable messaging cache in minutes (if a receiver) or how long the cache length on the receiver should be (if a sender).). This is the underlying object with id, value and extensions. The accessor "getReliableCache" gives direct access to the value
         */
        public CapabilityStatementMessagingComponent setReliableCacheElement(UnsignedIntType value) { 
          this.reliableCache = value;
          return this;
        }

        /**
         * @return Length if the receiver's reliable messaging cache in minutes (if a receiver) or how long the cache length on the receiver should be (if a sender).
         */
        public int getReliableCache() { 
          return this.reliableCache == null || this.reliableCache.isEmpty() ? 0 : this.reliableCache.getValue();
        }

        /**
         * @param value Length if the receiver's reliable messaging cache in minutes (if a receiver) or how long the cache length on the receiver should be (if a sender).
         */
        public CapabilityStatementMessagingComponent setReliableCache(int value) { 
            if (this.reliableCache == null)
              this.reliableCache = new UnsignedIntType();
            this.reliableCache.setValue(value);
          return this;
        }

        /**
         * @return {@link #documentation} (Documentation about the system's messaging capabilities for this endpoint not otherwise documented by the capability statement.  For example, the process for becoming an authorized messaging exchange partner.). This is the underlying object with id, value and extensions. The accessor "getDocumentation" gives direct access to the value
         */
        public StringType getDocumentationElement() { 
          if (this.documentation == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementMessagingComponent.documentation");
            else if (Configuration.doAutoCreate())
              this.documentation = new StringType(); // bb
          return this.documentation;
        }

        public boolean hasDocumentationElement() { 
          return this.documentation != null && !this.documentation.isEmpty();
        }

        public boolean hasDocumentation() { 
          return this.documentation != null && !this.documentation.isEmpty();
        }

        /**
         * @param value {@link #documentation} (Documentation about the system's messaging capabilities for this endpoint not otherwise documented by the capability statement.  For example, the process for becoming an authorized messaging exchange partner.). This is the underlying object with id, value and extensions. The accessor "getDocumentation" gives direct access to the value
         */
        public CapabilityStatementMessagingComponent setDocumentationElement(StringType value) { 
          this.documentation = value;
          return this;
        }

        /**
         * @return Documentation about the system's messaging capabilities for this endpoint not otherwise documented by the capability statement.  For example, the process for becoming an authorized messaging exchange partner.
         */
        public String getDocumentation() { 
          return this.documentation == null ? null : this.documentation.getValue();
        }

        /**
         * @param value Documentation about the system's messaging capabilities for this endpoint not otherwise documented by the capability statement.  For example, the process for becoming an authorized messaging exchange partner.
         */
        public CapabilityStatementMessagingComponent setDocumentation(String value) { 
          if (Utilities.noString(value))
            this.documentation = null;
          else {
            if (this.documentation == null)
              this.documentation = new StringType();
            this.documentation.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #supportedMessage} (References to message definitions for messages this system can send or receive.)
         */
        public List<CapabilityStatementMessagingSupportedMessageComponent> getSupportedMessage() { 
          if (this.supportedMessage == null)
            this.supportedMessage = new ArrayList<CapabilityStatementMessagingSupportedMessageComponent>();
          return this.supportedMessage;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public CapabilityStatementMessagingComponent setSupportedMessage(List<CapabilityStatementMessagingSupportedMessageComponent> theSupportedMessage) { 
          this.supportedMessage = theSupportedMessage;
          return this;
        }

        public boolean hasSupportedMessage() { 
          if (this.supportedMessage == null)
            return false;
          for (CapabilityStatementMessagingSupportedMessageComponent item : this.supportedMessage)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CapabilityStatementMessagingSupportedMessageComponent addSupportedMessage() { //3
          CapabilityStatementMessagingSupportedMessageComponent t = new CapabilityStatementMessagingSupportedMessageComponent();
          if (this.supportedMessage == null)
            this.supportedMessage = new ArrayList<CapabilityStatementMessagingSupportedMessageComponent>();
          this.supportedMessage.add(t);
          return t;
        }

        public CapabilityStatementMessagingComponent addSupportedMessage(CapabilityStatementMessagingSupportedMessageComponent t) { //3
          if (t == null)
            return this;
          if (this.supportedMessage == null)
            this.supportedMessage = new ArrayList<CapabilityStatementMessagingSupportedMessageComponent>();
          this.supportedMessage.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #supportedMessage}, creating it if it does not already exist
         */
        public CapabilityStatementMessagingSupportedMessageComponent getSupportedMessageFirstRep() { 
          if (getSupportedMessage().isEmpty()) {
            addSupportedMessage();
          }
          return getSupportedMessage().get(0);
        }

        /**
         * @return {@link #event} (A description of the solution's support for an event at this end-point.)
         */
        public List<CapabilityStatementMessagingEventComponent> getEvent() { 
          if (this.event == null)
            this.event = new ArrayList<CapabilityStatementMessagingEventComponent>();
          return this.event;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public CapabilityStatementMessagingComponent setEvent(List<CapabilityStatementMessagingEventComponent> theEvent) { 
          this.event = theEvent;
          return this;
        }

        public boolean hasEvent() { 
          if (this.event == null)
            return false;
          for (CapabilityStatementMessagingEventComponent item : this.event)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CapabilityStatementMessagingEventComponent addEvent() { //3
          CapabilityStatementMessagingEventComponent t = new CapabilityStatementMessagingEventComponent();
          if (this.event == null)
            this.event = new ArrayList<CapabilityStatementMessagingEventComponent>();
          this.event.add(t);
          return t;
        }

        public CapabilityStatementMessagingComponent addEvent(CapabilityStatementMessagingEventComponent t) { //3
          if (t == null)
            return this;
          if (this.event == null)
            this.event = new ArrayList<CapabilityStatementMessagingEventComponent>();
          this.event.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #event}, creating it if it does not already exist
         */
        public CapabilityStatementMessagingEventComponent getEventFirstRep() { 
          if (getEvent().isEmpty()) {
            addEvent();
          }
          return getEvent().get(0);
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("endpoint", "", "An endpoint (network accessible address) to which messages and/or replies are to be sent.", 0, java.lang.Integer.MAX_VALUE, endpoint));
          childrenList.add(new Property("reliableCache", "unsignedInt", "Length if the receiver's reliable messaging cache in minutes (if a receiver) or how long the cache length on the receiver should be (if a sender).", 0, java.lang.Integer.MAX_VALUE, reliableCache));
          childrenList.add(new Property("documentation", "string", "Documentation about the system's messaging capabilities for this endpoint not otherwise documented by the capability statement.  For example, the process for becoming an authorized messaging exchange partner.", 0, java.lang.Integer.MAX_VALUE, documentation));
          childrenList.add(new Property("supportedMessage", "", "References to message definitions for messages this system can send or receive.", 0, java.lang.Integer.MAX_VALUE, supportedMessage));
          childrenList.add(new Property("event", "", "A description of the solution's support for an event at this end-point.", 0, java.lang.Integer.MAX_VALUE, event));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1741102485: /*endpoint*/ return this.endpoint == null ? new Base[0] : this.endpoint.toArray(new Base[this.endpoint.size()]); // CapabilityStatementMessagingEndpointComponent
        case 897803608: /*reliableCache*/ return this.reliableCache == null ? new Base[0] : new Base[] {this.reliableCache}; // UnsignedIntType
        case 1587405498: /*documentation*/ return this.documentation == null ? new Base[0] : new Base[] {this.documentation}; // StringType
        case -1805139079: /*supportedMessage*/ return this.supportedMessage == null ? new Base[0] : this.supportedMessage.toArray(new Base[this.supportedMessage.size()]); // CapabilityStatementMessagingSupportedMessageComponent
        case 96891546: /*event*/ return this.event == null ? new Base[0] : this.event.toArray(new Base[this.event.size()]); // CapabilityStatementMessagingEventComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1741102485: // endpoint
          this.getEndpoint().add((CapabilityStatementMessagingEndpointComponent) value); // CapabilityStatementMessagingEndpointComponent
          return value;
        case 897803608: // reliableCache
          this.reliableCache = castToUnsignedInt(value); // UnsignedIntType
          return value;
        case 1587405498: // documentation
          this.documentation = castToString(value); // StringType
          return value;
        case -1805139079: // supportedMessage
          this.getSupportedMessage().add((CapabilityStatementMessagingSupportedMessageComponent) value); // CapabilityStatementMessagingSupportedMessageComponent
          return value;
        case 96891546: // event
          this.getEvent().add((CapabilityStatementMessagingEventComponent) value); // CapabilityStatementMessagingEventComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("endpoint")) {
          this.getEndpoint().add((CapabilityStatementMessagingEndpointComponent) value);
        } else if (name.equals("reliableCache")) {
          this.reliableCache = castToUnsignedInt(value); // UnsignedIntType
        } else if (name.equals("documentation")) {
          this.documentation = castToString(value); // StringType
        } else if (name.equals("supportedMessage")) {
          this.getSupportedMessage().add((CapabilityStatementMessagingSupportedMessageComponent) value);
        } else if (name.equals("event")) {
          this.getEvent().add((CapabilityStatementMessagingEventComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1741102485:  return addEndpoint(); 
        case 897803608:  return getReliableCacheElement();
        case 1587405498:  return getDocumentationElement();
        case -1805139079:  return addSupportedMessage(); 
        case 96891546:  return addEvent(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1741102485: /*endpoint*/ return new String[] {};
        case 897803608: /*reliableCache*/ return new String[] {"unsignedInt"};
        case 1587405498: /*documentation*/ return new String[] {"string"};
        case -1805139079: /*supportedMessage*/ return new String[] {};
        case 96891546: /*event*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("endpoint")) {
          return addEndpoint();
        }
        else if (name.equals("reliableCache")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.reliableCache");
        }
        else if (name.equals("documentation")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.documentation");
        }
        else if (name.equals("supportedMessage")) {
          return addSupportedMessage();
        }
        else if (name.equals("event")) {
          return addEvent();
        }
        else
          return super.addChild(name);
      }

      public CapabilityStatementMessagingComponent copy() {
        CapabilityStatementMessagingComponent dst = new CapabilityStatementMessagingComponent();
        copyValues(dst);
        if (endpoint != null) {
          dst.endpoint = new ArrayList<CapabilityStatementMessagingEndpointComponent>();
          for (CapabilityStatementMessagingEndpointComponent i : endpoint)
            dst.endpoint.add(i.copy());
        };
        dst.reliableCache = reliableCache == null ? null : reliableCache.copy();
        dst.documentation = documentation == null ? null : documentation.copy();
        if (supportedMessage != null) {
          dst.supportedMessage = new ArrayList<CapabilityStatementMessagingSupportedMessageComponent>();
          for (CapabilityStatementMessagingSupportedMessageComponent i : supportedMessage)
            dst.supportedMessage.add(i.copy());
        };
        if (event != null) {
          dst.event = new ArrayList<CapabilityStatementMessagingEventComponent>();
          for (CapabilityStatementMessagingEventComponent i : event)
            dst.event.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CapabilityStatementMessagingComponent))
          return false;
        CapabilityStatementMessagingComponent o = (CapabilityStatementMessagingComponent) other;
        return compareDeep(endpoint, o.endpoint, true) && compareDeep(reliableCache, o.reliableCache, true)
           && compareDeep(documentation, o.documentation, true) && compareDeep(supportedMessage, o.supportedMessage, true)
           && compareDeep(event, o.event, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CapabilityStatementMessagingComponent))
          return false;
        CapabilityStatementMessagingComponent o = (CapabilityStatementMessagingComponent) other;
        return compareValues(reliableCache, o.reliableCache, true) && compareValues(documentation, o.documentation, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(endpoint, reliableCache, documentation
          , supportedMessage, event);
      }

  public String fhirType() {
    return "CapabilityStatement.messaging";

  }

  }

    @Block()
    public static class CapabilityStatementMessagingEndpointComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A list of the messaging transport protocol(s) identifiers, supported by this endpoint.
         */
        @Child(name = "protocol", type = {Coding.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="http | ftp | mllp +", formalDefinition="A list of the messaging transport protocol(s) identifiers, supported by this endpoint." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/message-transport")
        protected Coding protocol;

        /**
         * The network address of the end-point. For solutions that do not use network addresses for routing, it can be just an identifier.
         */
        @Child(name = "address", type = {UriType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Network address or identifier of the end-point", formalDefinition="The network address of the end-point. For solutions that do not use network addresses for routing, it can be just an identifier." )
        protected UriType address;

        private static final long serialVersionUID = 1294656428L;

    /**
     * Constructor
     */
      public CapabilityStatementMessagingEndpointComponent() {
        super();
      }

    /**
     * Constructor
     */
      public CapabilityStatementMessagingEndpointComponent(Coding protocol, UriType address) {
        super();
        this.protocol = protocol;
        this.address = address;
      }

        /**
         * @return {@link #protocol} (A list of the messaging transport protocol(s) identifiers, supported by this endpoint.)
         */
        public Coding getProtocol() { 
          if (this.protocol == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementMessagingEndpointComponent.protocol");
            else if (Configuration.doAutoCreate())
              this.protocol = new Coding(); // cc
          return this.protocol;
        }

        public boolean hasProtocol() { 
          return this.protocol != null && !this.protocol.isEmpty();
        }

        /**
         * @param value {@link #protocol} (A list of the messaging transport protocol(s) identifiers, supported by this endpoint.)
         */
        public CapabilityStatementMessagingEndpointComponent setProtocol(Coding value) { 
          this.protocol = value;
          return this;
        }

        /**
         * @return {@link #address} (The network address of the end-point. For solutions that do not use network addresses for routing, it can be just an identifier.). This is the underlying object with id, value and extensions. The accessor "getAddress" gives direct access to the value
         */
        public UriType getAddressElement() { 
          if (this.address == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementMessagingEndpointComponent.address");
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
         * @param value {@link #address} (The network address of the end-point. For solutions that do not use network addresses for routing, it can be just an identifier.). This is the underlying object with id, value and extensions. The accessor "getAddress" gives direct access to the value
         */
        public CapabilityStatementMessagingEndpointComponent setAddressElement(UriType value) { 
          this.address = value;
          return this;
        }

        /**
         * @return The network address of the end-point. For solutions that do not use network addresses for routing, it can be just an identifier.
         */
        public String getAddress() { 
          return this.address == null ? null : this.address.getValue();
        }

        /**
         * @param value The network address of the end-point. For solutions that do not use network addresses for routing, it can be just an identifier.
         */
        public CapabilityStatementMessagingEndpointComponent setAddress(String value) { 
            if (this.address == null)
              this.address = new UriType();
            this.address.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("protocol", "Coding", "A list of the messaging transport protocol(s) identifiers, supported by this endpoint.", 0, java.lang.Integer.MAX_VALUE, protocol));
          childrenList.add(new Property("address", "uri", "The network address of the end-point. For solutions that do not use network addresses for routing, it can be just an identifier.", 0, java.lang.Integer.MAX_VALUE, address));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -989163880: /*protocol*/ return this.protocol == null ? new Base[0] : new Base[] {this.protocol}; // Coding
        case -1147692044: /*address*/ return this.address == null ? new Base[0] : new Base[] {this.address}; // UriType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -989163880: // protocol
          this.protocol = castToCoding(value); // Coding
          return value;
        case -1147692044: // address
          this.address = castToUri(value); // UriType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("protocol")) {
          this.protocol = castToCoding(value); // Coding
        } else if (name.equals("address")) {
          this.address = castToUri(value); // UriType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -989163880:  return getProtocol(); 
        case -1147692044:  return getAddressElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -989163880: /*protocol*/ return new String[] {"Coding"};
        case -1147692044: /*address*/ return new String[] {"uri"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("protocol")) {
          this.protocol = new Coding();
          return this.protocol;
        }
        else if (name.equals("address")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.address");
        }
        else
          return super.addChild(name);
      }

      public CapabilityStatementMessagingEndpointComponent copy() {
        CapabilityStatementMessagingEndpointComponent dst = new CapabilityStatementMessagingEndpointComponent();
        copyValues(dst);
        dst.protocol = protocol == null ? null : protocol.copy();
        dst.address = address == null ? null : address.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CapabilityStatementMessagingEndpointComponent))
          return false;
        CapabilityStatementMessagingEndpointComponent o = (CapabilityStatementMessagingEndpointComponent) other;
        return compareDeep(protocol, o.protocol, true) && compareDeep(address, o.address, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CapabilityStatementMessagingEndpointComponent))
          return false;
        CapabilityStatementMessagingEndpointComponent o = (CapabilityStatementMessagingEndpointComponent) other;
        return compareValues(address, o.address, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(protocol, address);
      }

  public String fhirType() {
    return "CapabilityStatement.messaging.endpoint";

  }

  }

    @Block()
    public static class CapabilityStatementMessagingSupportedMessageComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The mode of this event declaration - whether application is sender or receiver.
         */
        @Child(name = "mode", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="sender | receiver", formalDefinition="The mode of this event declaration - whether application is sender or receiver." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/event-capability-mode")
        protected Enumeration<EventCapabilityMode> mode;

        /**
         * Points to a message definition that identifies the messaging event, message structure, allowed responses, etc.
         */
        @Child(name = "definition", type = {MessageDefinition.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Message supported by this system", formalDefinition="Points to a message definition that identifies the messaging event, message structure, allowed responses, etc." )
        protected Reference definition;

        /**
         * The actual object that is the target of the reference (Points to a message definition that identifies the messaging event, message structure, allowed responses, etc.)
         */
        protected MessageDefinition definitionTarget;

        private static final long serialVersionUID = -741523869L;

    /**
     * Constructor
     */
      public CapabilityStatementMessagingSupportedMessageComponent() {
        super();
      }

    /**
     * Constructor
     */
      public CapabilityStatementMessagingSupportedMessageComponent(Enumeration<EventCapabilityMode> mode, Reference definition) {
        super();
        this.mode = mode;
        this.definition = definition;
      }

        /**
         * @return {@link #mode} (The mode of this event declaration - whether application is sender or receiver.). This is the underlying object with id, value and extensions. The accessor "getMode" gives direct access to the value
         */
        public Enumeration<EventCapabilityMode> getModeElement() { 
          if (this.mode == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementMessagingSupportedMessageComponent.mode");
            else if (Configuration.doAutoCreate())
              this.mode = new Enumeration<EventCapabilityMode>(new EventCapabilityModeEnumFactory()); // bb
          return this.mode;
        }

        public boolean hasModeElement() { 
          return this.mode != null && !this.mode.isEmpty();
        }

        public boolean hasMode() { 
          return this.mode != null && !this.mode.isEmpty();
        }

        /**
         * @param value {@link #mode} (The mode of this event declaration - whether application is sender or receiver.). This is the underlying object with id, value and extensions. The accessor "getMode" gives direct access to the value
         */
        public CapabilityStatementMessagingSupportedMessageComponent setModeElement(Enumeration<EventCapabilityMode> value) { 
          this.mode = value;
          return this;
        }

        /**
         * @return The mode of this event declaration - whether application is sender or receiver.
         */
        public EventCapabilityMode getMode() { 
          return this.mode == null ? null : this.mode.getValue();
        }

        /**
         * @param value The mode of this event declaration - whether application is sender or receiver.
         */
        public CapabilityStatementMessagingSupportedMessageComponent setMode(EventCapabilityMode value) { 
            if (this.mode == null)
              this.mode = new Enumeration<EventCapabilityMode>(new EventCapabilityModeEnumFactory());
            this.mode.setValue(value);
          return this;
        }

        /**
         * @return {@link #definition} (Points to a message definition that identifies the messaging event, message structure, allowed responses, etc.)
         */
        public Reference getDefinition() { 
          if (this.definition == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementMessagingSupportedMessageComponent.definition");
            else if (Configuration.doAutoCreate())
              this.definition = new Reference(); // cc
          return this.definition;
        }

        public boolean hasDefinition() { 
          return this.definition != null && !this.definition.isEmpty();
        }

        /**
         * @param value {@link #definition} (Points to a message definition that identifies the messaging event, message structure, allowed responses, etc.)
         */
        public CapabilityStatementMessagingSupportedMessageComponent setDefinition(Reference value) { 
          this.definition = value;
          return this;
        }

        /**
         * @return {@link #definition} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Points to a message definition that identifies the messaging event, message structure, allowed responses, etc.)
         */
        public MessageDefinition getDefinitionTarget() { 
          if (this.definitionTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementMessagingSupportedMessageComponent.definition");
            else if (Configuration.doAutoCreate())
              this.definitionTarget = new MessageDefinition(); // aa
          return this.definitionTarget;
        }

        /**
         * @param value {@link #definition} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Points to a message definition that identifies the messaging event, message structure, allowed responses, etc.)
         */
        public CapabilityStatementMessagingSupportedMessageComponent setDefinitionTarget(MessageDefinition value) { 
          this.definitionTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("mode", "code", "The mode of this event declaration - whether application is sender or receiver.", 0, java.lang.Integer.MAX_VALUE, mode));
          childrenList.add(new Property("definition", "Reference(MessageDefinition)", "Points to a message definition that identifies the messaging event, message structure, allowed responses, etc.", 0, java.lang.Integer.MAX_VALUE, definition));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3357091: /*mode*/ return this.mode == null ? new Base[0] : new Base[] {this.mode}; // Enumeration<EventCapabilityMode>
        case -1014418093: /*definition*/ return this.definition == null ? new Base[0] : new Base[] {this.definition}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3357091: // mode
          value = new EventCapabilityModeEnumFactory().fromType(castToCode(value));
          this.mode = (Enumeration) value; // Enumeration<EventCapabilityMode>
          return value;
        case -1014418093: // definition
          this.definition = castToReference(value); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("mode")) {
          value = new EventCapabilityModeEnumFactory().fromType(castToCode(value));
          this.mode = (Enumeration) value; // Enumeration<EventCapabilityMode>
        } else if (name.equals("definition")) {
          this.definition = castToReference(value); // Reference
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3357091:  return getModeElement();
        case -1014418093:  return getDefinition(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3357091: /*mode*/ return new String[] {"code"};
        case -1014418093: /*definition*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("mode")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.mode");
        }
        else if (name.equals("definition")) {
          this.definition = new Reference();
          return this.definition;
        }
        else
          return super.addChild(name);
      }

      public CapabilityStatementMessagingSupportedMessageComponent copy() {
        CapabilityStatementMessagingSupportedMessageComponent dst = new CapabilityStatementMessagingSupportedMessageComponent();
        copyValues(dst);
        dst.mode = mode == null ? null : mode.copy();
        dst.definition = definition == null ? null : definition.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CapabilityStatementMessagingSupportedMessageComponent))
          return false;
        CapabilityStatementMessagingSupportedMessageComponent o = (CapabilityStatementMessagingSupportedMessageComponent) other;
        return compareDeep(mode, o.mode, true) && compareDeep(definition, o.definition, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CapabilityStatementMessagingSupportedMessageComponent))
          return false;
        CapabilityStatementMessagingSupportedMessageComponent o = (CapabilityStatementMessagingSupportedMessageComponent) other;
        return compareValues(mode, o.mode, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(mode, definition);
      }

  public String fhirType() {
    return "CapabilityStatement.messaging.supportedMessage";

  }

  }

    @Block()
    public static class CapabilityStatementMessagingEventComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A coded identifier of a supported messaging event.
         */
        @Child(name = "code", type = {Coding.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Event type", formalDefinition="A coded identifier of a supported messaging event." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/message-events")
        protected Coding code;

        /**
         * The impact of the content of the message.
         */
        @Child(name = "category", type = {CodeType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Consequence | Currency | Notification", formalDefinition="The impact of the content of the message." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/message-significance-category")
        protected Enumeration<MessageSignificanceCategory> category;

        /**
         * The mode of this event declaration - whether an application is a sender or receiver.
         */
        @Child(name = "mode", type = {CodeType.class}, order=3, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="sender | receiver", formalDefinition="The mode of this event declaration - whether an application is a sender or receiver." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/event-capability-mode")
        protected Enumeration<EventCapabilityMode> mode;

        /**
         * A resource associated with the event.  This is the resource that defines the event.
         */
        @Child(name = "focus", type = {CodeType.class}, order=4, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Resource that's focus of message", formalDefinition="A resource associated with the event.  This is the resource that defines the event." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/resource-types")
        protected CodeType focus;

        /**
         * Information about the request for this event.
         */
        @Child(name = "request", type = {StructureDefinition.class}, order=5, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Profile that describes the request", formalDefinition="Information about the request for this event." )
        protected Reference request;

        /**
         * The actual object that is the target of the reference (Information about the request for this event.)
         */
        protected StructureDefinition requestTarget;

        /**
         * Information about the response for this event.
         */
        @Child(name = "response", type = {StructureDefinition.class}, order=6, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Profile that describes the response", formalDefinition="Information about the response for this event." )
        protected Reference response;

        /**
         * The actual object that is the target of the reference (Information about the response for this event.)
         */
        protected StructureDefinition responseTarget;

        /**
         * Guidance on how this event is handled, such as internal system trigger points, business rules, etc.
         */
        @Child(name = "documentation", type = {StringType.class}, order=7, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Endpoint-specific event documentation", formalDefinition="Guidance on how this event is handled, such as internal system trigger points, business rules, etc." )
        protected StringType documentation;

        private static final long serialVersionUID = -491306017L;

    /**
     * Constructor
     */
      public CapabilityStatementMessagingEventComponent() {
        super();
      }

    /**
     * Constructor
     */
      public CapabilityStatementMessagingEventComponent(Coding code, Enumeration<EventCapabilityMode> mode, CodeType focus, Reference request, Reference response) {
        super();
        this.code = code;
        this.mode = mode;
        this.focus = focus;
        this.request = request;
        this.response = response;
      }

        /**
         * @return {@link #code} (A coded identifier of a supported messaging event.)
         */
        public Coding getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementMessagingEventComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new Coding(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (A coded identifier of a supported messaging event.)
         */
        public CapabilityStatementMessagingEventComponent setCode(Coding value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #category} (The impact of the content of the message.). This is the underlying object with id, value and extensions. The accessor "getCategory" gives direct access to the value
         */
        public Enumeration<MessageSignificanceCategory> getCategoryElement() { 
          if (this.category == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementMessagingEventComponent.category");
            else if (Configuration.doAutoCreate())
              this.category = new Enumeration<MessageSignificanceCategory>(new MessageSignificanceCategoryEnumFactory()); // bb
          return this.category;
        }

        public boolean hasCategoryElement() { 
          return this.category != null && !this.category.isEmpty();
        }

        public boolean hasCategory() { 
          return this.category != null && !this.category.isEmpty();
        }

        /**
         * @param value {@link #category} (The impact of the content of the message.). This is the underlying object with id, value and extensions. The accessor "getCategory" gives direct access to the value
         */
        public CapabilityStatementMessagingEventComponent setCategoryElement(Enumeration<MessageSignificanceCategory> value) { 
          this.category = value;
          return this;
        }

        /**
         * @return The impact of the content of the message.
         */
        public MessageSignificanceCategory getCategory() { 
          return this.category == null ? null : this.category.getValue();
        }

        /**
         * @param value The impact of the content of the message.
         */
        public CapabilityStatementMessagingEventComponent setCategory(MessageSignificanceCategory value) { 
          if (value == null)
            this.category = null;
          else {
            if (this.category == null)
              this.category = new Enumeration<MessageSignificanceCategory>(new MessageSignificanceCategoryEnumFactory());
            this.category.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #mode} (The mode of this event declaration - whether an application is a sender or receiver.). This is the underlying object with id, value and extensions. The accessor "getMode" gives direct access to the value
         */
        public Enumeration<EventCapabilityMode> getModeElement() { 
          if (this.mode == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementMessagingEventComponent.mode");
            else if (Configuration.doAutoCreate())
              this.mode = new Enumeration<EventCapabilityMode>(new EventCapabilityModeEnumFactory()); // bb
          return this.mode;
        }

        public boolean hasModeElement() { 
          return this.mode != null && !this.mode.isEmpty();
        }

        public boolean hasMode() { 
          return this.mode != null && !this.mode.isEmpty();
        }

        /**
         * @param value {@link #mode} (The mode of this event declaration - whether an application is a sender or receiver.). This is the underlying object with id, value and extensions. The accessor "getMode" gives direct access to the value
         */
        public CapabilityStatementMessagingEventComponent setModeElement(Enumeration<EventCapabilityMode> value) { 
          this.mode = value;
          return this;
        }

        /**
         * @return The mode of this event declaration - whether an application is a sender or receiver.
         */
        public EventCapabilityMode getMode() { 
          return this.mode == null ? null : this.mode.getValue();
        }

        /**
         * @param value The mode of this event declaration - whether an application is a sender or receiver.
         */
        public CapabilityStatementMessagingEventComponent setMode(EventCapabilityMode value) { 
            if (this.mode == null)
              this.mode = new Enumeration<EventCapabilityMode>(new EventCapabilityModeEnumFactory());
            this.mode.setValue(value);
          return this;
        }

        /**
         * @return {@link #focus} (A resource associated with the event.  This is the resource that defines the event.). This is the underlying object with id, value and extensions. The accessor "getFocus" gives direct access to the value
         */
        public CodeType getFocusElement() { 
          if (this.focus == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementMessagingEventComponent.focus");
            else if (Configuration.doAutoCreate())
              this.focus = new CodeType(); // bb
          return this.focus;
        }

        public boolean hasFocusElement() { 
          return this.focus != null && !this.focus.isEmpty();
        }

        public boolean hasFocus() { 
          return this.focus != null && !this.focus.isEmpty();
        }

        /**
         * @param value {@link #focus} (A resource associated with the event.  This is the resource that defines the event.). This is the underlying object with id, value and extensions. The accessor "getFocus" gives direct access to the value
         */
        public CapabilityStatementMessagingEventComponent setFocusElement(CodeType value) { 
          this.focus = value;
          return this;
        }

        /**
         * @return A resource associated with the event.  This is the resource that defines the event.
         */
        public String getFocus() { 
          return this.focus == null ? null : this.focus.getValue();
        }

        /**
         * @param value A resource associated with the event.  This is the resource that defines the event.
         */
        public CapabilityStatementMessagingEventComponent setFocus(String value) { 
            if (this.focus == null)
              this.focus = new CodeType();
            this.focus.setValue(value);
          return this;
        }

        /**
         * @return {@link #request} (Information about the request for this event.)
         */
        public Reference getRequest() { 
          if (this.request == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementMessagingEventComponent.request");
            else if (Configuration.doAutoCreate())
              this.request = new Reference(); // cc
          return this.request;
        }

        public boolean hasRequest() { 
          return this.request != null && !this.request.isEmpty();
        }

        /**
         * @param value {@link #request} (Information about the request for this event.)
         */
        public CapabilityStatementMessagingEventComponent setRequest(Reference value) { 
          this.request = value;
          return this;
        }

        /**
         * @return {@link #request} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Information about the request for this event.)
         */
        public StructureDefinition getRequestTarget() { 
          if (this.requestTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementMessagingEventComponent.request");
            else if (Configuration.doAutoCreate())
              this.requestTarget = new StructureDefinition(); // aa
          return this.requestTarget;
        }

        /**
         * @param value {@link #request} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Information about the request for this event.)
         */
        public CapabilityStatementMessagingEventComponent setRequestTarget(StructureDefinition value) { 
          this.requestTarget = value;
          return this;
        }

        /**
         * @return {@link #response} (Information about the response for this event.)
         */
        public Reference getResponse() { 
          if (this.response == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementMessagingEventComponent.response");
            else if (Configuration.doAutoCreate())
              this.response = new Reference(); // cc
          return this.response;
        }

        public boolean hasResponse() { 
          return this.response != null && !this.response.isEmpty();
        }

        /**
         * @param value {@link #response} (Information about the response for this event.)
         */
        public CapabilityStatementMessagingEventComponent setResponse(Reference value) { 
          this.response = value;
          return this;
        }

        /**
         * @return {@link #response} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Information about the response for this event.)
         */
        public StructureDefinition getResponseTarget() { 
          if (this.responseTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementMessagingEventComponent.response");
            else if (Configuration.doAutoCreate())
              this.responseTarget = new StructureDefinition(); // aa
          return this.responseTarget;
        }

        /**
         * @param value {@link #response} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Information about the response for this event.)
         */
        public CapabilityStatementMessagingEventComponent setResponseTarget(StructureDefinition value) { 
          this.responseTarget = value;
          return this;
        }

        /**
         * @return {@link #documentation} (Guidance on how this event is handled, such as internal system trigger points, business rules, etc.). This is the underlying object with id, value and extensions. The accessor "getDocumentation" gives direct access to the value
         */
        public StringType getDocumentationElement() { 
          if (this.documentation == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementMessagingEventComponent.documentation");
            else if (Configuration.doAutoCreate())
              this.documentation = new StringType(); // bb
          return this.documentation;
        }

        public boolean hasDocumentationElement() { 
          return this.documentation != null && !this.documentation.isEmpty();
        }

        public boolean hasDocumentation() { 
          return this.documentation != null && !this.documentation.isEmpty();
        }

        /**
         * @param value {@link #documentation} (Guidance on how this event is handled, such as internal system trigger points, business rules, etc.). This is the underlying object with id, value and extensions. The accessor "getDocumentation" gives direct access to the value
         */
        public CapabilityStatementMessagingEventComponent setDocumentationElement(StringType value) { 
          this.documentation = value;
          return this;
        }

        /**
         * @return Guidance on how this event is handled, such as internal system trigger points, business rules, etc.
         */
        public String getDocumentation() { 
          return this.documentation == null ? null : this.documentation.getValue();
        }

        /**
         * @param value Guidance on how this event is handled, such as internal system trigger points, business rules, etc.
         */
        public CapabilityStatementMessagingEventComponent setDocumentation(String value) { 
          if (Utilities.noString(value))
            this.documentation = null;
          else {
            if (this.documentation == null)
              this.documentation = new StringType();
            this.documentation.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("code", "Coding", "A coded identifier of a supported messaging event.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("category", "code", "The impact of the content of the message.", 0, java.lang.Integer.MAX_VALUE, category));
          childrenList.add(new Property("mode", "code", "The mode of this event declaration - whether an application is a sender or receiver.", 0, java.lang.Integer.MAX_VALUE, mode));
          childrenList.add(new Property("focus", "code", "A resource associated with the event.  This is the resource that defines the event.", 0, java.lang.Integer.MAX_VALUE, focus));
          childrenList.add(new Property("request", "Reference(StructureDefinition)", "Information about the request for this event.", 0, java.lang.Integer.MAX_VALUE, request));
          childrenList.add(new Property("response", "Reference(StructureDefinition)", "Information about the response for this event.", 0, java.lang.Integer.MAX_VALUE, response));
          childrenList.add(new Property("documentation", "string", "Guidance on how this event is handled, such as internal system trigger points, business rules, etc.", 0, java.lang.Integer.MAX_VALUE, documentation));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // Coding
        case 50511102: /*category*/ return this.category == null ? new Base[0] : new Base[] {this.category}; // Enumeration<MessageSignificanceCategory>
        case 3357091: /*mode*/ return this.mode == null ? new Base[0] : new Base[] {this.mode}; // Enumeration<EventCapabilityMode>
        case 97604824: /*focus*/ return this.focus == null ? new Base[0] : new Base[] {this.focus}; // CodeType
        case 1095692943: /*request*/ return this.request == null ? new Base[0] : new Base[] {this.request}; // Reference
        case -340323263: /*response*/ return this.response == null ? new Base[0] : new Base[] {this.response}; // Reference
        case 1587405498: /*documentation*/ return this.documentation == null ? new Base[0] : new Base[] {this.documentation}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3059181: // code
          this.code = castToCoding(value); // Coding
          return value;
        case 50511102: // category
          value = new MessageSignificanceCategoryEnumFactory().fromType(castToCode(value));
          this.category = (Enumeration) value; // Enumeration<MessageSignificanceCategory>
          return value;
        case 3357091: // mode
          value = new EventCapabilityModeEnumFactory().fromType(castToCode(value));
          this.mode = (Enumeration) value; // Enumeration<EventCapabilityMode>
          return value;
        case 97604824: // focus
          this.focus = castToCode(value); // CodeType
          return value;
        case 1095692943: // request
          this.request = castToReference(value); // Reference
          return value;
        case -340323263: // response
          this.response = castToReference(value); // Reference
          return value;
        case 1587405498: // documentation
          this.documentation = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code")) {
          this.code = castToCoding(value); // Coding
        } else if (name.equals("category")) {
          value = new MessageSignificanceCategoryEnumFactory().fromType(castToCode(value));
          this.category = (Enumeration) value; // Enumeration<MessageSignificanceCategory>
        } else if (name.equals("mode")) {
          value = new EventCapabilityModeEnumFactory().fromType(castToCode(value));
          this.mode = (Enumeration) value; // Enumeration<EventCapabilityMode>
        } else if (name.equals("focus")) {
          this.focus = castToCode(value); // CodeType
        } else if (name.equals("request")) {
          this.request = castToReference(value); // Reference
        } else if (name.equals("response")) {
          this.response = castToReference(value); // Reference
        } else if (name.equals("documentation")) {
          this.documentation = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181:  return getCode(); 
        case 50511102:  return getCategoryElement();
        case 3357091:  return getModeElement();
        case 97604824:  return getFocusElement();
        case 1095692943:  return getRequest(); 
        case -340323263:  return getResponse(); 
        case 1587405498:  return getDocumentationElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return new String[] {"Coding"};
        case 50511102: /*category*/ return new String[] {"code"};
        case 3357091: /*mode*/ return new String[] {"code"};
        case 97604824: /*focus*/ return new String[] {"code"};
        case 1095692943: /*request*/ return new String[] {"Reference"};
        case -340323263: /*response*/ return new String[] {"Reference"};
        case 1587405498: /*documentation*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          this.code = new Coding();
          return this.code;
        }
        else if (name.equals("category")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.category");
        }
        else if (name.equals("mode")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.mode");
        }
        else if (name.equals("focus")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.focus");
        }
        else if (name.equals("request")) {
          this.request = new Reference();
          return this.request;
        }
        else if (name.equals("response")) {
          this.response = new Reference();
          return this.response;
        }
        else if (name.equals("documentation")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.documentation");
        }
        else
          return super.addChild(name);
      }

      public CapabilityStatementMessagingEventComponent copy() {
        CapabilityStatementMessagingEventComponent dst = new CapabilityStatementMessagingEventComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.category = category == null ? null : category.copy();
        dst.mode = mode == null ? null : mode.copy();
        dst.focus = focus == null ? null : focus.copy();
        dst.request = request == null ? null : request.copy();
        dst.response = response == null ? null : response.copy();
        dst.documentation = documentation == null ? null : documentation.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CapabilityStatementMessagingEventComponent))
          return false;
        CapabilityStatementMessagingEventComponent o = (CapabilityStatementMessagingEventComponent) other;
        return compareDeep(code, o.code, true) && compareDeep(category, o.category, true) && compareDeep(mode, o.mode, true)
           && compareDeep(focus, o.focus, true) && compareDeep(request, o.request, true) && compareDeep(response, o.response, true)
           && compareDeep(documentation, o.documentation, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CapabilityStatementMessagingEventComponent))
          return false;
        CapabilityStatementMessagingEventComponent o = (CapabilityStatementMessagingEventComponent) other;
        return compareValues(category, o.category, true) && compareValues(mode, o.mode, true) && compareValues(focus, o.focus, true)
           && compareValues(documentation, o.documentation, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(code, category, mode, focus
          , request, response, documentation);
      }

  public String fhirType() {
    return "CapabilityStatement.messaging.event";

  }

  }

    @Block()
    public static class CapabilityStatementDocumentComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Mode of this document declaration - whether an application is a producer or consumer.
         */
        @Child(name = "mode", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="producer | consumer", formalDefinition="Mode of this document declaration - whether an application is a producer or consumer." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/document-mode")
        protected Enumeration<DocumentMode> mode;

        /**
         * A description of how the application supports or uses the specified document profile.  For example, when documents are created, what action is taken with consumed documents, etc.
         */
        @Child(name = "documentation", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Description of document support", formalDefinition="A description of how the application supports or uses the specified document profile.  For example, when documents are created, what action is taken with consumed documents, etc." )
        protected StringType documentation;

        /**
         * A constraint on a resource used in the document.
         */
        @Child(name = "profile", type = {StructureDefinition.class}, order=3, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Constraint on a resource used in the document", formalDefinition="A constraint on a resource used in the document." )
        protected Reference profile;

        /**
         * The actual object that is the target of the reference (A constraint on a resource used in the document.)
         */
        protected StructureDefinition profileTarget;

        private static final long serialVersionUID = -1059555053L;

    /**
     * Constructor
     */
      public CapabilityStatementDocumentComponent() {
        super();
      }

    /**
     * Constructor
     */
      public CapabilityStatementDocumentComponent(Enumeration<DocumentMode> mode, Reference profile) {
        super();
        this.mode = mode;
        this.profile = profile;
      }

        /**
         * @return {@link #mode} (Mode of this document declaration - whether an application is a producer or consumer.). This is the underlying object with id, value and extensions. The accessor "getMode" gives direct access to the value
         */
        public Enumeration<DocumentMode> getModeElement() { 
          if (this.mode == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementDocumentComponent.mode");
            else if (Configuration.doAutoCreate())
              this.mode = new Enumeration<DocumentMode>(new DocumentModeEnumFactory()); // bb
          return this.mode;
        }

        public boolean hasModeElement() { 
          return this.mode != null && !this.mode.isEmpty();
        }

        public boolean hasMode() { 
          return this.mode != null && !this.mode.isEmpty();
        }

        /**
         * @param value {@link #mode} (Mode of this document declaration - whether an application is a producer or consumer.). This is the underlying object with id, value and extensions. The accessor "getMode" gives direct access to the value
         */
        public CapabilityStatementDocumentComponent setModeElement(Enumeration<DocumentMode> value) { 
          this.mode = value;
          return this;
        }

        /**
         * @return Mode of this document declaration - whether an application is a producer or consumer.
         */
        public DocumentMode getMode() { 
          return this.mode == null ? null : this.mode.getValue();
        }

        /**
         * @param value Mode of this document declaration - whether an application is a producer or consumer.
         */
        public CapabilityStatementDocumentComponent setMode(DocumentMode value) { 
            if (this.mode == null)
              this.mode = new Enumeration<DocumentMode>(new DocumentModeEnumFactory());
            this.mode.setValue(value);
          return this;
        }

        /**
         * @return {@link #documentation} (A description of how the application supports or uses the specified document profile.  For example, when documents are created, what action is taken with consumed documents, etc.). This is the underlying object with id, value and extensions. The accessor "getDocumentation" gives direct access to the value
         */
        public StringType getDocumentationElement() { 
          if (this.documentation == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementDocumentComponent.documentation");
            else if (Configuration.doAutoCreate())
              this.documentation = new StringType(); // bb
          return this.documentation;
        }

        public boolean hasDocumentationElement() { 
          return this.documentation != null && !this.documentation.isEmpty();
        }

        public boolean hasDocumentation() { 
          return this.documentation != null && !this.documentation.isEmpty();
        }

        /**
         * @param value {@link #documentation} (A description of how the application supports or uses the specified document profile.  For example, when documents are created, what action is taken with consumed documents, etc.). This is the underlying object with id, value and extensions. The accessor "getDocumentation" gives direct access to the value
         */
        public CapabilityStatementDocumentComponent setDocumentationElement(StringType value) { 
          this.documentation = value;
          return this;
        }

        /**
         * @return A description of how the application supports or uses the specified document profile.  For example, when documents are created, what action is taken with consumed documents, etc.
         */
        public String getDocumentation() { 
          return this.documentation == null ? null : this.documentation.getValue();
        }

        /**
         * @param value A description of how the application supports or uses the specified document profile.  For example, when documents are created, what action is taken with consumed documents, etc.
         */
        public CapabilityStatementDocumentComponent setDocumentation(String value) { 
          if (Utilities.noString(value))
            this.documentation = null;
          else {
            if (this.documentation == null)
              this.documentation = new StringType();
            this.documentation.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #profile} (A constraint on a resource used in the document.)
         */
        public Reference getProfile() { 
          if (this.profile == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementDocumentComponent.profile");
            else if (Configuration.doAutoCreate())
              this.profile = new Reference(); // cc
          return this.profile;
        }

        public boolean hasProfile() { 
          return this.profile != null && !this.profile.isEmpty();
        }

        /**
         * @param value {@link #profile} (A constraint on a resource used in the document.)
         */
        public CapabilityStatementDocumentComponent setProfile(Reference value) { 
          this.profile = value;
          return this;
        }

        /**
         * @return {@link #profile} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A constraint on a resource used in the document.)
         */
        public StructureDefinition getProfileTarget() { 
          if (this.profileTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CapabilityStatementDocumentComponent.profile");
            else if (Configuration.doAutoCreate())
              this.profileTarget = new StructureDefinition(); // aa
          return this.profileTarget;
        }

        /**
         * @param value {@link #profile} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A constraint on a resource used in the document.)
         */
        public CapabilityStatementDocumentComponent setProfileTarget(StructureDefinition value) { 
          this.profileTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("mode", "code", "Mode of this document declaration - whether an application is a producer or consumer.", 0, java.lang.Integer.MAX_VALUE, mode));
          childrenList.add(new Property("documentation", "string", "A description of how the application supports or uses the specified document profile.  For example, when documents are created, what action is taken with consumed documents, etc.", 0, java.lang.Integer.MAX_VALUE, documentation));
          childrenList.add(new Property("profile", "Reference(StructureDefinition)", "A constraint on a resource used in the document.", 0, java.lang.Integer.MAX_VALUE, profile));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3357091: /*mode*/ return this.mode == null ? new Base[0] : new Base[] {this.mode}; // Enumeration<DocumentMode>
        case 1587405498: /*documentation*/ return this.documentation == null ? new Base[0] : new Base[] {this.documentation}; // StringType
        case -309425751: /*profile*/ return this.profile == null ? new Base[0] : new Base[] {this.profile}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3357091: // mode
          value = new DocumentModeEnumFactory().fromType(castToCode(value));
          this.mode = (Enumeration) value; // Enumeration<DocumentMode>
          return value;
        case 1587405498: // documentation
          this.documentation = castToString(value); // StringType
          return value;
        case -309425751: // profile
          this.profile = castToReference(value); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("mode")) {
          value = new DocumentModeEnumFactory().fromType(castToCode(value));
          this.mode = (Enumeration) value; // Enumeration<DocumentMode>
        } else if (name.equals("documentation")) {
          this.documentation = castToString(value); // StringType
        } else if (name.equals("profile")) {
          this.profile = castToReference(value); // Reference
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3357091:  return getModeElement();
        case 1587405498:  return getDocumentationElement();
        case -309425751:  return getProfile(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3357091: /*mode*/ return new String[] {"code"};
        case 1587405498: /*documentation*/ return new String[] {"string"};
        case -309425751: /*profile*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("mode")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.mode");
        }
        else if (name.equals("documentation")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.documentation");
        }
        else if (name.equals("profile")) {
          this.profile = new Reference();
          return this.profile;
        }
        else
          return super.addChild(name);
      }

      public CapabilityStatementDocumentComponent copy() {
        CapabilityStatementDocumentComponent dst = new CapabilityStatementDocumentComponent();
        copyValues(dst);
        dst.mode = mode == null ? null : mode.copy();
        dst.documentation = documentation == null ? null : documentation.copy();
        dst.profile = profile == null ? null : profile.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CapabilityStatementDocumentComponent))
          return false;
        CapabilityStatementDocumentComponent o = (CapabilityStatementDocumentComponent) other;
        return compareDeep(mode, o.mode, true) && compareDeep(documentation, o.documentation, true) && compareDeep(profile, o.profile, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CapabilityStatementDocumentComponent))
          return false;
        CapabilityStatementDocumentComponent o = (CapabilityStatementDocumentComponent) other;
        return compareValues(mode, o.mode, true) && compareValues(documentation, o.documentation, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(mode, documentation, profile
          );
      }

  public String fhirType() {
    return "CapabilityStatement.document";

  }

  }

    /**
     * Explaination of why this capability statement is needed and why it has been designed as it has.
     */
    @Child(name = "purpose", type = {MarkdownType.class}, order=0, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Why this capability statement is defined", formalDefinition="Explaination of why this capability statement is needed and why it has been designed as it has." )
    protected MarkdownType purpose;

    /**
     * A copyright statement relating to the capability statement and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the capability statement.
     */
    @Child(name = "copyright", type = {MarkdownType.class}, order=1, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Use and/or publishing restrictions", formalDefinition="A copyright statement relating to the capability statement and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the capability statement." )
    protected MarkdownType copyright;

    /**
     * The way that this statement is intended to be used, to describe an actual running instance of software, a particular product (kind not instance of software) or a class of implementation (e.g. a desired purchase).
     */
    @Child(name = "kind", type = {CodeType.class}, order=2, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="instance | capability | requirements", formalDefinition="The way that this statement is intended to be used, to describe an actual running instance of software, a particular product (kind not instance of software) or a class of implementation (e.g. a desired purchase)." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/capability-statement-kind")
    protected Enumeration<CapabilityStatementKind> kind;

    /**
     * Reference to a canonical URL of another CapabilityStatement that this software implements or uses. This capability statement is a published API description that corresponds to a business service. The rest of the capability statement does not need to repeat the details of the referenced resource, but can do so.
     */
    @Child(name = "instantiates", type = {UriType.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Canonical URL of another capability statement this implements", formalDefinition="Reference to a canonical URL of another CapabilityStatement that this software implements or uses. This capability statement is a published API description that corresponds to a business service. The rest of the capability statement does not need to repeat the details of the referenced resource, but can do so." )
    protected List<UriType> instantiates;

    /**
     * Software that is covered by this capability statement.  It is used when the capability statement describes the capabilities of a particular software version, independent of an installation.
     */
    @Child(name = "software", type = {}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Software that is covered by this capability statement", formalDefinition="Software that is covered by this capability statement.  It is used when the capability statement describes the capabilities of a particular software version, independent of an installation." )
    protected CapabilityStatementSoftwareComponent software;

    /**
     * Identifies a specific implementation instance that is described by the capability statement - i.e. a particular installation, rather than the capabilities of a software program.
     */
    @Child(name = "implementation", type = {}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="If this describes a specific instance", formalDefinition="Identifies a specific implementation instance that is described by the capability statement - i.e. a particular installation, rather than the capabilities of a software program." )
    protected CapabilityStatementImplementationComponent implementation;

    /**
     * The version of the FHIR specification on which this capability statement is based.
     */
    @Child(name = "fhirVersion", type = {IdType.class}, order=6, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="FHIR Version the system uses", formalDefinition="The version of the FHIR specification on which this capability statement is based." )
    protected IdType fhirVersion;

    /**
     * A code that indicates whether the application accepts unknown elements or extensions when reading resources.
     */
    @Child(name = "acceptUnknown", type = {CodeType.class}, order=7, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="no | extensions | elements | both", formalDefinition="A code that indicates whether the application accepts unknown elements or extensions when reading resources." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/unknown-content-code")
    protected Enumeration<UnknownContentCode> acceptUnknown;

    /**
     * A list of the formats supported by this implementation using their content types.
     */
    @Child(name = "format", type = {CodeType.class}, order=8, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="formats supported (xml | json | ttl | mime type)", formalDefinition="A list of the formats supported by this implementation using their content types." )
    protected List<CodeType> format;

    /**
     * A list of the patch formats supported by this implementation using their content types.
     */
    @Child(name = "patchFormat", type = {CodeType.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Patch formats supported", formalDefinition="A list of the patch formats supported by this implementation using their content types." )
    protected List<CodeType> patchFormat;

    /**
     * A list of implementation guides that the server does (or should) support in their entirety.
     */
    @Child(name = "implementationGuide", type = {UriType.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Implementation guides supported", formalDefinition="A list of implementation guides that the server does (or should) support in their entirety." )
    protected List<UriType> implementationGuide;

    /**
     * A list of profiles that represent different use cases supported by the system. For a server, "supported by the system" means the system hosts/produces a set of resources that are conformant to a particular profile, and allows clients that use its services to search using this profile and to find appropriate data. For a client, it means the system will search by this profile and process data according to the guidance implicit in the profile. See further discussion in [Using Profiles](profiling.html#profile-uses).
     */
    @Child(name = "profile", type = {StructureDefinition.class}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Profiles for use cases supported", formalDefinition="A list of profiles that represent different use cases supported by the system. For a server, \"supported by the system\" means the system hosts/produces a set of resources that are conformant to a particular profile, and allows clients that use its services to search using this profile and to find appropriate data. For a client, it means the system will search by this profile and process data according to the guidance implicit in the profile. See further discussion in [Using Profiles](profiling.html#profile-uses)." )
    protected List<Reference> profile;
    /**
     * The actual objects that are the target of the reference (A list of profiles that represent different use cases supported by the system. For a server, "supported by the system" means the system hosts/produces a set of resources that are conformant to a particular profile, and allows clients that use its services to search using this profile and to find appropriate data. For a client, it means the system will search by this profile and process data according to the guidance implicit in the profile. See further discussion in [Using Profiles](profiling.html#profile-uses).)
     */
    protected List<StructureDefinition> profileTarget;


    /**
     * A definition of the restful capabilities of the solution, if any.
     */
    @Child(name = "rest", type = {}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="If the endpoint is a RESTful one", formalDefinition="A definition of the restful capabilities of the solution, if any." )
    protected List<CapabilityStatementRestComponent> rest;

    /**
     * A description of the messaging capabilities of the solution.
     */
    @Child(name = "messaging", type = {}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="If messaging is supported", formalDefinition="A description of the messaging capabilities of the solution." )
    protected List<CapabilityStatementMessagingComponent> messaging;

    /**
     * A document definition.
     */
    @Child(name = "document", type = {}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Document definition", formalDefinition="A document definition." )
    protected List<CapabilityStatementDocumentComponent> document;

    private static final long serialVersionUID = 227177541L;

  /**
   * Constructor
   */
    public CapabilityStatement() {
      super();
    }

  /**
   * Constructor
   */
    public CapabilityStatement(Enumeration<PublicationStatus> status, DateTimeType date, Enumeration<CapabilityStatementKind> kind, IdType fhirVersion, Enumeration<UnknownContentCode> acceptUnknown) {
      super();
      this.status = status;
      this.date = date;
      this.kind = kind;
      this.fhirVersion = fhirVersion;
      this.acceptUnknown = acceptUnknown;
    }

    /**
     * @return {@link #url} (An absolute URI that is used to identify this capability statement when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this capability statement is (or will be) published. The URL SHOULD include the major version of the capability statement. For more information see [Technical and Business Versions](resource.html#versions).). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public UriType getUrlElement() { 
      if (this.url == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CapabilityStatement.url");
        else if (Configuration.doAutoCreate())
          this.url = new UriType(); // bb
      return this.url;
    }

    public boolean hasUrlElement() { 
      return this.url != null && !this.url.isEmpty();
    }

    public boolean hasUrl() { 
      return this.url != null && !this.url.isEmpty();
    }

    /**
     * @param value {@link #url} (An absolute URI that is used to identify this capability statement when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this capability statement is (or will be) published. The URL SHOULD include the major version of the capability statement. For more information see [Technical and Business Versions](resource.html#versions).). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public CapabilityStatement setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return An absolute URI that is used to identify this capability statement when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this capability statement is (or will be) published. The URL SHOULD include the major version of the capability statement. For more information see [Technical and Business Versions](resource.html#versions).
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value An absolute URI that is used to identify this capability statement when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this capability statement is (or will be) published. The URL SHOULD include the major version of the capability statement. For more information see [Technical and Business Versions](resource.html#versions).
     */
    public CapabilityStatement setUrl(String value) { 
      if (Utilities.noString(value))
        this.url = null;
      else {
        if (this.url == null)
          this.url = new UriType();
        this.url.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #version} (The identifier that is used to identify this version of the capability statement when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the capability statement author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public StringType getVersionElement() { 
      if (this.version == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CapabilityStatement.version");
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
     * @param value {@link #version} (The identifier that is used to identify this version of the capability statement when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the capability statement author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public CapabilityStatement setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return The identifier that is used to identify this version of the capability statement when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the capability statement author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value The identifier that is used to identify this version of the capability statement when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the capability statement author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.
     */
    public CapabilityStatement setVersion(String value) { 
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
     * @return {@link #name} (A natural language name identifying the capability statement. This name should be usable as an identifier for the module by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CapabilityStatement.name");
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
     * @param value {@link #name} (A natural language name identifying the capability statement. This name should be usable as an identifier for the module by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public CapabilityStatement setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return A natural language name identifying the capability statement. This name should be usable as an identifier for the module by machine processing applications such as code generation.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value A natural language name identifying the capability statement. This name should be usable as an identifier for the module by machine processing applications such as code generation.
     */
    public CapabilityStatement setName(String value) { 
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
     * @return {@link #title} (A short, descriptive, user-friendly title for the capability statement.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public StringType getTitleElement() { 
      if (this.title == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CapabilityStatement.title");
        else if (Configuration.doAutoCreate())
          this.title = new StringType(); // bb
      return this.title;
    }

    public boolean hasTitleElement() { 
      return this.title != null && !this.title.isEmpty();
    }

    public boolean hasTitle() { 
      return this.title != null && !this.title.isEmpty();
    }

    /**
     * @param value {@link #title} (A short, descriptive, user-friendly title for the capability statement.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public CapabilityStatement setTitleElement(StringType value) { 
      this.title = value;
      return this;
    }

    /**
     * @return A short, descriptive, user-friendly title for the capability statement.
     */
    public String getTitle() { 
      return this.title == null ? null : this.title.getValue();
    }

    /**
     * @param value A short, descriptive, user-friendly title for the capability statement.
     */
    public CapabilityStatement setTitle(String value) { 
      if (Utilities.noString(value))
        this.title = null;
      else {
        if (this.title == null)
          this.title = new StringType();
        this.title.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #status} (The status of this capability statement. Enables tracking the life-cycle of the content.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<PublicationStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CapabilityStatement.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<PublicationStatus>(new PublicationStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of this capability statement. Enables tracking the life-cycle of the content.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public CapabilityStatement setStatusElement(Enumeration<PublicationStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of this capability statement. Enables tracking the life-cycle of the content.
     */
    public PublicationStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of this capability statement. Enables tracking the life-cycle of the content.
     */
    public CapabilityStatement setStatus(PublicationStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<PublicationStatus>(new PublicationStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #experimental} (A boolean value to indicate that this capability statement is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public BooleanType getExperimentalElement() { 
      if (this.experimental == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CapabilityStatement.experimental");
        else if (Configuration.doAutoCreate())
          this.experimental = new BooleanType(); // bb
      return this.experimental;
    }

    public boolean hasExperimentalElement() { 
      return this.experimental != null && !this.experimental.isEmpty();
    }

    public boolean hasExperimental() { 
      return this.experimental != null && !this.experimental.isEmpty();
    }

    /**
     * @param value {@link #experimental} (A boolean value to indicate that this capability statement is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public CapabilityStatement setExperimentalElement(BooleanType value) { 
      this.experimental = value;
      return this;
    }

    /**
     * @return A boolean value to indicate that this capability statement is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public boolean getExperimental() { 
      return this.experimental == null || this.experimental.isEmpty() ? false : this.experimental.getValue();
    }

    /**
     * @param value A boolean value to indicate that this capability statement is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public CapabilityStatement setExperimental(boolean value) { 
        if (this.experimental == null)
          this.experimental = new BooleanType();
        this.experimental.setValue(value);
      return this;
    }

    /**
     * @return {@link #date} (The date  (and optionally time) when the capability statement was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the capability statement changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CapabilityStatement.date");
        else if (Configuration.doAutoCreate())
          this.date = new DateTimeType(); // bb
      return this.date;
    }

    public boolean hasDateElement() { 
      return this.date != null && !this.date.isEmpty();
    }

    public boolean hasDate() { 
      return this.date != null && !this.date.isEmpty();
    }

    /**
     * @param value {@link #date} (The date  (and optionally time) when the capability statement was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the capability statement changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public CapabilityStatement setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date  (and optionally time) when the capability statement was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the capability statement changes.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date  (and optionally time) when the capability statement was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the capability statement changes.
     */
    public CapabilityStatement setDate(Date value) { 
        if (this.date == null)
          this.date = new DateTimeType();
        this.date.setValue(value);
      return this;
    }

    /**
     * @return {@link #publisher} (The name of the individual or organization that published the capability statement.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public StringType getPublisherElement() { 
      if (this.publisher == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CapabilityStatement.publisher");
        else if (Configuration.doAutoCreate())
          this.publisher = new StringType(); // bb
      return this.publisher;
    }

    public boolean hasPublisherElement() { 
      return this.publisher != null && !this.publisher.isEmpty();
    }

    public boolean hasPublisher() { 
      return this.publisher != null && !this.publisher.isEmpty();
    }

    /**
     * @param value {@link #publisher} (The name of the individual or organization that published the capability statement.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public CapabilityStatement setPublisherElement(StringType value) { 
      this.publisher = value;
      return this;
    }

    /**
     * @return The name of the individual or organization that published the capability statement.
     */
    public String getPublisher() { 
      return this.publisher == null ? null : this.publisher.getValue();
    }

    /**
     * @param value The name of the individual or organization that published the capability statement.
     */
    public CapabilityStatement setPublisher(String value) { 
      if (Utilities.noString(value))
        this.publisher = null;
      else {
        if (this.publisher == null)
          this.publisher = new StringType();
        this.publisher.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #contact} (Contact details to assist a user in finding and communicating with the publisher.)
     */
    public List<ContactDetail> getContact() { 
      if (this.contact == null)
        this.contact = new ArrayList<ContactDetail>();
      return this.contact;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public CapabilityStatement setContact(List<ContactDetail> theContact) { 
      this.contact = theContact;
      return this;
    }

    public boolean hasContact() { 
      if (this.contact == null)
        return false;
      for (ContactDetail item : this.contact)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ContactDetail addContact() { //3
      ContactDetail t = new ContactDetail();
      if (this.contact == null)
        this.contact = new ArrayList<ContactDetail>();
      this.contact.add(t);
      return t;
    }

    public CapabilityStatement addContact(ContactDetail t) { //3
      if (t == null)
        return this;
      if (this.contact == null)
        this.contact = new ArrayList<ContactDetail>();
      this.contact.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #contact}, creating it if it does not already exist
     */
    public ContactDetail getContactFirstRep() { 
      if (getContact().isEmpty()) {
        addContact();
      }
      return getContact().get(0);
    }

    /**
     * @return {@link #description} (A free text natural language description of the capability statement from a consumer's perspective. Typically, this is used when the capability statement describes a desired rather than an actual solution, for example as a formal expression of requirements as part of an RFP.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public MarkdownType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CapabilityStatement.description");
        else if (Configuration.doAutoCreate())
          this.description = new MarkdownType(); // bb
      return this.description;
    }

    public boolean hasDescriptionElement() { 
      return this.description != null && !this.description.isEmpty();
    }

    public boolean hasDescription() { 
      return this.description != null && !this.description.isEmpty();
    }

    /**
     * @param value {@link #description} (A free text natural language description of the capability statement from a consumer's perspective. Typically, this is used when the capability statement describes a desired rather than an actual solution, for example as a formal expression of requirements as part of an RFP.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public CapabilityStatement setDescriptionElement(MarkdownType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A free text natural language description of the capability statement from a consumer's perspective. Typically, this is used when the capability statement describes a desired rather than an actual solution, for example as a formal expression of requirements as part of an RFP.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A free text natural language description of the capability statement from a consumer's perspective. Typically, this is used when the capability statement describes a desired rather than an actual solution, for example as a formal expression of requirements as part of an RFP.
     */
    public CapabilityStatement setDescription(String value) { 
      if (value == null)
        this.description = null;
      else {
        if (this.description == null)
          this.description = new MarkdownType();
        this.description.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #useContext} (The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching for appropriate capability statement instances.)
     */
    public List<UsageContext> getUseContext() { 
      if (this.useContext == null)
        this.useContext = new ArrayList<UsageContext>();
      return this.useContext;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public CapabilityStatement setUseContext(List<UsageContext> theUseContext) { 
      this.useContext = theUseContext;
      return this;
    }

    public boolean hasUseContext() { 
      if (this.useContext == null)
        return false;
      for (UsageContext item : this.useContext)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public UsageContext addUseContext() { //3
      UsageContext t = new UsageContext();
      if (this.useContext == null)
        this.useContext = new ArrayList<UsageContext>();
      this.useContext.add(t);
      return t;
    }

    public CapabilityStatement addUseContext(UsageContext t) { //3
      if (t == null)
        return this;
      if (this.useContext == null)
        this.useContext = new ArrayList<UsageContext>();
      this.useContext.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #useContext}, creating it if it does not already exist
     */
    public UsageContext getUseContextFirstRep() { 
      if (getUseContext().isEmpty()) {
        addUseContext();
      }
      return getUseContext().get(0);
    }

    /**
     * @return {@link #jurisdiction} (A legal or geographic region in which the capability statement is intended to be used.)
     */
    public List<CodeableConcept> getJurisdiction() { 
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      return this.jurisdiction;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public CapabilityStatement setJurisdiction(List<CodeableConcept> theJurisdiction) { 
      this.jurisdiction = theJurisdiction;
      return this;
    }

    public boolean hasJurisdiction() { 
      if (this.jurisdiction == null)
        return false;
      for (CodeableConcept item : this.jurisdiction)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addJurisdiction() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      this.jurisdiction.add(t);
      return t;
    }

    public CapabilityStatement addJurisdiction(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      this.jurisdiction.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #jurisdiction}, creating it if it does not already exist
     */
    public CodeableConcept getJurisdictionFirstRep() { 
      if (getJurisdiction().isEmpty()) {
        addJurisdiction();
      }
      return getJurisdiction().get(0);
    }

    /**
     * @return {@link #purpose} (Explaination of why this capability statement is needed and why it has been designed as it has.). This is the underlying object with id, value and extensions. The accessor "getPurpose" gives direct access to the value
     */
    public MarkdownType getPurposeElement() { 
      if (this.purpose == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CapabilityStatement.purpose");
        else if (Configuration.doAutoCreate())
          this.purpose = new MarkdownType(); // bb
      return this.purpose;
    }

    public boolean hasPurposeElement() { 
      return this.purpose != null && !this.purpose.isEmpty();
    }

    public boolean hasPurpose() { 
      return this.purpose != null && !this.purpose.isEmpty();
    }

    /**
     * @param value {@link #purpose} (Explaination of why this capability statement is needed and why it has been designed as it has.). This is the underlying object with id, value and extensions. The accessor "getPurpose" gives direct access to the value
     */
    public CapabilityStatement setPurposeElement(MarkdownType value) { 
      this.purpose = value;
      return this;
    }

    /**
     * @return Explaination of why this capability statement is needed and why it has been designed as it has.
     */
    public String getPurpose() { 
      return this.purpose == null ? null : this.purpose.getValue();
    }

    /**
     * @param value Explaination of why this capability statement is needed and why it has been designed as it has.
     */
    public CapabilityStatement setPurpose(String value) { 
      if (value == null)
        this.purpose = null;
      else {
        if (this.purpose == null)
          this.purpose = new MarkdownType();
        this.purpose.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #copyright} (A copyright statement relating to the capability statement and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the capability statement.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public MarkdownType getCopyrightElement() { 
      if (this.copyright == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CapabilityStatement.copyright");
        else if (Configuration.doAutoCreate())
          this.copyright = new MarkdownType(); // bb
      return this.copyright;
    }

    public boolean hasCopyrightElement() { 
      return this.copyright != null && !this.copyright.isEmpty();
    }

    public boolean hasCopyright() { 
      return this.copyright != null && !this.copyright.isEmpty();
    }

    /**
     * @param value {@link #copyright} (A copyright statement relating to the capability statement and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the capability statement.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public CapabilityStatement setCopyrightElement(MarkdownType value) { 
      this.copyright = value;
      return this;
    }

    /**
     * @return A copyright statement relating to the capability statement and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the capability statement.
     */
    public String getCopyright() { 
      return this.copyright == null ? null : this.copyright.getValue();
    }

    /**
     * @param value A copyright statement relating to the capability statement and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the capability statement.
     */
    public CapabilityStatement setCopyright(String value) { 
      if (value == null)
        this.copyright = null;
      else {
        if (this.copyright == null)
          this.copyright = new MarkdownType();
        this.copyright.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #kind} (The way that this statement is intended to be used, to describe an actual running instance of software, a particular product (kind not instance of software) or a class of implementation (e.g. a desired purchase).). This is the underlying object with id, value and extensions. The accessor "getKind" gives direct access to the value
     */
    public Enumeration<CapabilityStatementKind> getKindElement() { 
      if (this.kind == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CapabilityStatement.kind");
        else if (Configuration.doAutoCreate())
          this.kind = new Enumeration<CapabilityStatementKind>(new CapabilityStatementKindEnumFactory()); // bb
      return this.kind;
    }

    public boolean hasKindElement() { 
      return this.kind != null && !this.kind.isEmpty();
    }

    public boolean hasKind() { 
      return this.kind != null && !this.kind.isEmpty();
    }

    /**
     * @param value {@link #kind} (The way that this statement is intended to be used, to describe an actual running instance of software, a particular product (kind not instance of software) or a class of implementation (e.g. a desired purchase).). This is the underlying object with id, value and extensions. The accessor "getKind" gives direct access to the value
     */
    public CapabilityStatement setKindElement(Enumeration<CapabilityStatementKind> value) { 
      this.kind = value;
      return this;
    }

    /**
     * @return The way that this statement is intended to be used, to describe an actual running instance of software, a particular product (kind not instance of software) or a class of implementation (e.g. a desired purchase).
     */
    public CapabilityStatementKind getKind() { 
      return this.kind == null ? null : this.kind.getValue();
    }

    /**
     * @param value The way that this statement is intended to be used, to describe an actual running instance of software, a particular product (kind not instance of software) or a class of implementation (e.g. a desired purchase).
     */
    public CapabilityStatement setKind(CapabilityStatementKind value) { 
        if (this.kind == null)
          this.kind = new Enumeration<CapabilityStatementKind>(new CapabilityStatementKindEnumFactory());
        this.kind.setValue(value);
      return this;
    }

    /**
     * @return {@link #instantiates} (Reference to a canonical URL of another CapabilityStatement that this software implements or uses. This capability statement is a published API description that corresponds to a business service. The rest of the capability statement does not need to repeat the details of the referenced resource, but can do so.)
     */
    public List<UriType> getInstantiates() { 
      if (this.instantiates == null)
        this.instantiates = new ArrayList<UriType>();
      return this.instantiates;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public CapabilityStatement setInstantiates(List<UriType> theInstantiates) { 
      this.instantiates = theInstantiates;
      return this;
    }

    public boolean hasInstantiates() { 
      if (this.instantiates == null)
        return false;
      for (UriType item : this.instantiates)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #instantiates} (Reference to a canonical URL of another CapabilityStatement that this software implements or uses. This capability statement is a published API description that corresponds to a business service. The rest of the capability statement does not need to repeat the details of the referenced resource, but can do so.)
     */
    public UriType addInstantiatesElement() {//2 
      UriType t = new UriType();
      if (this.instantiates == null)
        this.instantiates = new ArrayList<UriType>();
      this.instantiates.add(t);
      return t;
    }

    /**
     * @param value {@link #instantiates} (Reference to a canonical URL of another CapabilityStatement that this software implements or uses. This capability statement is a published API description that corresponds to a business service. The rest of the capability statement does not need to repeat the details of the referenced resource, but can do so.)
     */
    public CapabilityStatement addInstantiates(String value) { //1
      UriType t = new UriType();
      t.setValue(value);
      if (this.instantiates == null)
        this.instantiates = new ArrayList<UriType>();
      this.instantiates.add(t);
      return this;
    }

    /**
     * @param value {@link #instantiates} (Reference to a canonical URL of another CapabilityStatement that this software implements or uses. This capability statement is a published API description that corresponds to a business service. The rest of the capability statement does not need to repeat the details of the referenced resource, but can do so.)
     */
    public boolean hasInstantiates(String value) { 
      if (this.instantiates == null)
        return false;
      for (UriType v : this.instantiates)
        if (v.equals(value)) // uri
          return true;
      return false;
    }

    /**
     * @return {@link #software} (Software that is covered by this capability statement.  It is used when the capability statement describes the capabilities of a particular software version, independent of an installation.)
     */
    public CapabilityStatementSoftwareComponent getSoftware() { 
      if (this.software == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CapabilityStatement.software");
        else if (Configuration.doAutoCreate())
          this.software = new CapabilityStatementSoftwareComponent(); // cc
      return this.software;
    }

    public boolean hasSoftware() { 
      return this.software != null && !this.software.isEmpty();
    }

    /**
     * @param value {@link #software} (Software that is covered by this capability statement.  It is used when the capability statement describes the capabilities of a particular software version, independent of an installation.)
     */
    public CapabilityStatement setSoftware(CapabilityStatementSoftwareComponent value) { 
      this.software = value;
      return this;
    }

    /**
     * @return {@link #implementation} (Identifies a specific implementation instance that is described by the capability statement - i.e. a particular installation, rather than the capabilities of a software program.)
     */
    public CapabilityStatementImplementationComponent getImplementation() { 
      if (this.implementation == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CapabilityStatement.implementation");
        else if (Configuration.doAutoCreate())
          this.implementation = new CapabilityStatementImplementationComponent(); // cc
      return this.implementation;
    }

    public boolean hasImplementation() { 
      return this.implementation != null && !this.implementation.isEmpty();
    }

    /**
     * @param value {@link #implementation} (Identifies a specific implementation instance that is described by the capability statement - i.e. a particular installation, rather than the capabilities of a software program.)
     */
    public CapabilityStatement setImplementation(CapabilityStatementImplementationComponent value) { 
      this.implementation = value;
      return this;
    }

    /**
     * @return {@link #fhirVersion} (The version of the FHIR specification on which this capability statement is based.). This is the underlying object with id, value and extensions. The accessor "getFhirVersion" gives direct access to the value
     */
    public IdType getFhirVersionElement() { 
      if (this.fhirVersion == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CapabilityStatement.fhirVersion");
        else if (Configuration.doAutoCreate())
          this.fhirVersion = new IdType(); // bb
      return this.fhirVersion;
    }

    public boolean hasFhirVersionElement() { 
      return this.fhirVersion != null && !this.fhirVersion.isEmpty();
    }

    public boolean hasFhirVersion() { 
      return this.fhirVersion != null && !this.fhirVersion.isEmpty();
    }

    /**
     * @param value {@link #fhirVersion} (The version of the FHIR specification on which this capability statement is based.). This is the underlying object with id, value and extensions. The accessor "getFhirVersion" gives direct access to the value
     */
    public CapabilityStatement setFhirVersionElement(IdType value) { 
      this.fhirVersion = value;
      return this;
    }

    /**
     * @return The version of the FHIR specification on which this capability statement is based.
     */
    public String getFhirVersion() { 
      return this.fhirVersion == null ? null : this.fhirVersion.getValue();
    }

    /**
     * @param value The version of the FHIR specification on which this capability statement is based.
     */
    public CapabilityStatement setFhirVersion(String value) { 
        if (this.fhirVersion == null)
          this.fhirVersion = new IdType();
        this.fhirVersion.setValue(value);
      return this;
    }

    /**
     * @return {@link #acceptUnknown} (A code that indicates whether the application accepts unknown elements or extensions when reading resources.). This is the underlying object with id, value and extensions. The accessor "getAcceptUnknown" gives direct access to the value
     */
    public Enumeration<UnknownContentCode> getAcceptUnknownElement() { 
      if (this.acceptUnknown == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CapabilityStatement.acceptUnknown");
        else if (Configuration.doAutoCreate())
          this.acceptUnknown = new Enumeration<UnknownContentCode>(new UnknownContentCodeEnumFactory()); // bb
      return this.acceptUnknown;
    }

    public boolean hasAcceptUnknownElement() { 
      return this.acceptUnknown != null && !this.acceptUnknown.isEmpty();
    }

    public boolean hasAcceptUnknown() { 
      return this.acceptUnknown != null && !this.acceptUnknown.isEmpty();
    }

    /**
     * @param value {@link #acceptUnknown} (A code that indicates whether the application accepts unknown elements or extensions when reading resources.). This is the underlying object with id, value and extensions. The accessor "getAcceptUnknown" gives direct access to the value
     */
    public CapabilityStatement setAcceptUnknownElement(Enumeration<UnknownContentCode> value) { 
      this.acceptUnknown = value;
      return this;
    }

    /**
     * @return A code that indicates whether the application accepts unknown elements or extensions when reading resources.
     */
    public UnknownContentCode getAcceptUnknown() { 
      return this.acceptUnknown == null ? null : this.acceptUnknown.getValue();
    }

    /**
     * @param value A code that indicates whether the application accepts unknown elements or extensions when reading resources.
     */
    public CapabilityStatement setAcceptUnknown(UnknownContentCode value) { 
        if (this.acceptUnknown == null)
          this.acceptUnknown = new Enumeration<UnknownContentCode>(new UnknownContentCodeEnumFactory());
        this.acceptUnknown.setValue(value);
      return this;
    }

    /**
     * @return {@link #format} (A list of the formats supported by this implementation using their content types.)
     */
    public List<CodeType> getFormat() { 
      if (this.format == null)
        this.format = new ArrayList<CodeType>();
      return this.format;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public CapabilityStatement setFormat(List<CodeType> theFormat) { 
      this.format = theFormat;
      return this;
    }

    public boolean hasFormat() { 
      if (this.format == null)
        return false;
      for (CodeType item : this.format)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #format} (A list of the formats supported by this implementation using their content types.)
     */
    public CodeType addFormatElement() {//2 
      CodeType t = new CodeType();
      if (this.format == null)
        this.format = new ArrayList<CodeType>();
      this.format.add(t);
      return t;
    }

    /**
     * @param value {@link #format} (A list of the formats supported by this implementation using their content types.)
     */
    public CapabilityStatement addFormat(String value) { //1
      CodeType t = new CodeType();
      t.setValue(value);
      if (this.format == null)
        this.format = new ArrayList<CodeType>();
      this.format.add(t);
      return this;
    }

    /**
     * @param value {@link #format} (A list of the formats supported by this implementation using their content types.)
     */
    public boolean hasFormat(String value) { 
      if (this.format == null)
        return false;
      for (CodeType v : this.format)
        if (v.equals(value)) // code
          return true;
      return false;
    }

    /**
     * @return {@link #patchFormat} (A list of the patch formats supported by this implementation using their content types.)
     */
    public List<CodeType> getPatchFormat() { 
      if (this.patchFormat == null)
        this.patchFormat = new ArrayList<CodeType>();
      return this.patchFormat;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public CapabilityStatement setPatchFormat(List<CodeType> thePatchFormat) { 
      this.patchFormat = thePatchFormat;
      return this;
    }

    public boolean hasPatchFormat() { 
      if (this.patchFormat == null)
        return false;
      for (CodeType item : this.patchFormat)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #patchFormat} (A list of the patch formats supported by this implementation using their content types.)
     */
    public CodeType addPatchFormatElement() {//2 
      CodeType t = new CodeType();
      if (this.patchFormat == null)
        this.patchFormat = new ArrayList<CodeType>();
      this.patchFormat.add(t);
      return t;
    }

    /**
     * @param value {@link #patchFormat} (A list of the patch formats supported by this implementation using their content types.)
     */
    public CapabilityStatement addPatchFormat(String value) { //1
      CodeType t = new CodeType();
      t.setValue(value);
      if (this.patchFormat == null)
        this.patchFormat = new ArrayList<CodeType>();
      this.patchFormat.add(t);
      return this;
    }

    /**
     * @param value {@link #patchFormat} (A list of the patch formats supported by this implementation using their content types.)
     */
    public boolean hasPatchFormat(String value) { 
      if (this.patchFormat == null)
        return false;
      for (CodeType v : this.patchFormat)
        if (v.equals(value)) // code
          return true;
      return false;
    }

    /**
     * @return {@link #implementationGuide} (A list of implementation guides that the server does (or should) support in their entirety.)
     */
    public List<UriType> getImplementationGuide() { 
      if (this.implementationGuide == null)
        this.implementationGuide = new ArrayList<UriType>();
      return this.implementationGuide;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public CapabilityStatement setImplementationGuide(List<UriType> theImplementationGuide) { 
      this.implementationGuide = theImplementationGuide;
      return this;
    }

    public boolean hasImplementationGuide() { 
      if (this.implementationGuide == null)
        return false;
      for (UriType item : this.implementationGuide)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #implementationGuide} (A list of implementation guides that the server does (or should) support in their entirety.)
     */
    public UriType addImplementationGuideElement() {//2 
      UriType t = new UriType();
      if (this.implementationGuide == null)
        this.implementationGuide = new ArrayList<UriType>();
      this.implementationGuide.add(t);
      return t;
    }

    /**
     * @param value {@link #implementationGuide} (A list of implementation guides that the server does (or should) support in their entirety.)
     */
    public CapabilityStatement addImplementationGuide(String value) { //1
      UriType t = new UriType();
      t.setValue(value);
      if (this.implementationGuide == null)
        this.implementationGuide = new ArrayList<UriType>();
      this.implementationGuide.add(t);
      return this;
    }

    /**
     * @param value {@link #implementationGuide} (A list of implementation guides that the server does (or should) support in their entirety.)
     */
    public boolean hasImplementationGuide(String value) { 
      if (this.implementationGuide == null)
        return false;
      for (UriType v : this.implementationGuide)
        if (v.equals(value)) // uri
          return true;
      return false;
    }

    /**
     * @return {@link #profile} (A list of profiles that represent different use cases supported by the system. For a server, "supported by the system" means the system hosts/produces a set of resources that are conformant to a particular profile, and allows clients that use its services to search using this profile and to find appropriate data. For a client, it means the system will search by this profile and process data according to the guidance implicit in the profile. See further discussion in [Using Profiles](profiling.html#profile-uses).)
     */
    public List<Reference> getProfile() { 
      if (this.profile == null)
        this.profile = new ArrayList<Reference>();
      return this.profile;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public CapabilityStatement setProfile(List<Reference> theProfile) { 
      this.profile = theProfile;
      return this;
    }

    public boolean hasProfile() { 
      if (this.profile == null)
        return false;
      for (Reference item : this.profile)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addProfile() { //3
      Reference t = new Reference();
      if (this.profile == null)
        this.profile = new ArrayList<Reference>();
      this.profile.add(t);
      return t;
    }

    public CapabilityStatement addProfile(Reference t) { //3
      if (t == null)
        return this;
      if (this.profile == null)
        this.profile = new ArrayList<Reference>();
      this.profile.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #profile}, creating it if it does not already exist
     */
    public Reference getProfileFirstRep() { 
      if (getProfile().isEmpty()) {
        addProfile();
      }
      return getProfile().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<StructureDefinition> getProfileTarget() { 
      if (this.profileTarget == null)
        this.profileTarget = new ArrayList<StructureDefinition>();
      return this.profileTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public StructureDefinition addProfileTarget() { 
      StructureDefinition r = new StructureDefinition();
      if (this.profileTarget == null)
        this.profileTarget = new ArrayList<StructureDefinition>();
      this.profileTarget.add(r);
      return r;
    }

    /**
     * @return {@link #rest} (A definition of the restful capabilities of the solution, if any.)
     */
    public List<CapabilityStatementRestComponent> getRest() { 
      if (this.rest == null)
        this.rest = new ArrayList<CapabilityStatementRestComponent>();
      return this.rest;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public CapabilityStatement setRest(List<CapabilityStatementRestComponent> theRest) { 
      this.rest = theRest;
      return this;
    }

    public boolean hasRest() { 
      if (this.rest == null)
        return false;
      for (CapabilityStatementRestComponent item : this.rest)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CapabilityStatementRestComponent addRest() { //3
      CapabilityStatementRestComponent t = new CapabilityStatementRestComponent();
      if (this.rest == null)
        this.rest = new ArrayList<CapabilityStatementRestComponent>();
      this.rest.add(t);
      return t;
    }

    public CapabilityStatement addRest(CapabilityStatementRestComponent t) { //3
      if (t == null)
        return this;
      if (this.rest == null)
        this.rest = new ArrayList<CapabilityStatementRestComponent>();
      this.rest.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #rest}, creating it if it does not already exist
     */
    public CapabilityStatementRestComponent getRestFirstRep() { 
      if (getRest().isEmpty()) {
        addRest();
      }
      return getRest().get(0);
    }

    /**
     * @return {@link #messaging} (A description of the messaging capabilities of the solution.)
     */
    public List<CapabilityStatementMessagingComponent> getMessaging() { 
      if (this.messaging == null)
        this.messaging = new ArrayList<CapabilityStatementMessagingComponent>();
      return this.messaging;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public CapabilityStatement setMessaging(List<CapabilityStatementMessagingComponent> theMessaging) { 
      this.messaging = theMessaging;
      return this;
    }

    public boolean hasMessaging() { 
      if (this.messaging == null)
        return false;
      for (CapabilityStatementMessagingComponent item : this.messaging)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CapabilityStatementMessagingComponent addMessaging() { //3
      CapabilityStatementMessagingComponent t = new CapabilityStatementMessagingComponent();
      if (this.messaging == null)
        this.messaging = new ArrayList<CapabilityStatementMessagingComponent>();
      this.messaging.add(t);
      return t;
    }

    public CapabilityStatement addMessaging(CapabilityStatementMessagingComponent t) { //3
      if (t == null)
        return this;
      if (this.messaging == null)
        this.messaging = new ArrayList<CapabilityStatementMessagingComponent>();
      this.messaging.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #messaging}, creating it if it does not already exist
     */
    public CapabilityStatementMessagingComponent getMessagingFirstRep() { 
      if (getMessaging().isEmpty()) {
        addMessaging();
      }
      return getMessaging().get(0);
    }

    /**
     * @return {@link #document} (A document definition.)
     */
    public List<CapabilityStatementDocumentComponent> getDocument() { 
      if (this.document == null)
        this.document = new ArrayList<CapabilityStatementDocumentComponent>();
      return this.document;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public CapabilityStatement setDocument(List<CapabilityStatementDocumentComponent> theDocument) { 
      this.document = theDocument;
      return this;
    }

    public boolean hasDocument() { 
      if (this.document == null)
        return false;
      for (CapabilityStatementDocumentComponent item : this.document)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CapabilityStatementDocumentComponent addDocument() { //3
      CapabilityStatementDocumentComponent t = new CapabilityStatementDocumentComponent();
      if (this.document == null)
        this.document = new ArrayList<CapabilityStatementDocumentComponent>();
      this.document.add(t);
      return t;
    }

    public CapabilityStatement addDocument(CapabilityStatementDocumentComponent t) { //3
      if (t == null)
        return this;
      if (this.document == null)
        this.document = new ArrayList<CapabilityStatementDocumentComponent>();
      this.document.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #document}, creating it if it does not already exist
     */
    public CapabilityStatementDocumentComponent getDocumentFirstRep() { 
      if (getDocument().isEmpty()) {
        addDocument();
      }
      return getDocument().get(0);
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("url", "uri", "An absolute URI that is used to identify this capability statement when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this capability statement is (or will be) published. The URL SHOULD include the major version of the capability statement. For more information see [Technical and Business Versions](resource.html#versions).", 0, java.lang.Integer.MAX_VALUE, url));
        childrenList.add(new Property("version", "string", "The identifier that is used to identify this version of the capability statement when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the capability statement author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.", 0, java.lang.Integer.MAX_VALUE, version));
        childrenList.add(new Property("name", "string", "A natural language name identifying the capability statement. This name should be usable as an identifier for the module by machine processing applications such as code generation.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("title", "string", "A short, descriptive, user-friendly title for the capability statement.", 0, java.lang.Integer.MAX_VALUE, title));
        childrenList.add(new Property("status", "code", "The status of this capability statement. Enables tracking the life-cycle of the content.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("experimental", "boolean", "A boolean value to indicate that this capability statement is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.", 0, java.lang.Integer.MAX_VALUE, experimental));
        childrenList.add(new Property("date", "dateTime", "The date  (and optionally time) when the capability statement was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the capability statement changes.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("publisher", "string", "The name of the individual or organization that published the capability statement.", 0, java.lang.Integer.MAX_VALUE, publisher));
        childrenList.add(new Property("contact", "ContactDetail", "Contact details to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact));
        childrenList.add(new Property("description", "markdown", "A free text natural language description of the capability statement from a consumer's perspective. Typically, this is used when the capability statement describes a desired rather than an actual solution, for example as a formal expression of requirements as part of an RFP.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("useContext", "UsageContext", "The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching for appropriate capability statement instances.", 0, java.lang.Integer.MAX_VALUE, useContext));
        childrenList.add(new Property("jurisdiction", "CodeableConcept", "A legal or geographic region in which the capability statement is intended to be used.", 0, java.lang.Integer.MAX_VALUE, jurisdiction));
        childrenList.add(new Property("purpose", "markdown", "Explaination of why this capability statement is needed and why it has been designed as it has.", 0, java.lang.Integer.MAX_VALUE, purpose));
        childrenList.add(new Property("copyright", "markdown", "A copyright statement relating to the capability statement and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the capability statement.", 0, java.lang.Integer.MAX_VALUE, copyright));
        childrenList.add(new Property("kind", "code", "The way that this statement is intended to be used, to describe an actual running instance of software, a particular product (kind not instance of software) or a class of implementation (e.g. a desired purchase).", 0, java.lang.Integer.MAX_VALUE, kind));
        childrenList.add(new Property("instantiates", "uri", "Reference to a canonical URL of another CapabilityStatement that this software implements or uses. This capability statement is a published API description that corresponds to a business service. The rest of the capability statement does not need to repeat the details of the referenced resource, but can do so.", 0, java.lang.Integer.MAX_VALUE, instantiates));
        childrenList.add(new Property("software", "", "Software that is covered by this capability statement.  It is used when the capability statement describes the capabilities of a particular software version, independent of an installation.", 0, java.lang.Integer.MAX_VALUE, software));
        childrenList.add(new Property("implementation", "", "Identifies a specific implementation instance that is described by the capability statement - i.e. a particular installation, rather than the capabilities of a software program.", 0, java.lang.Integer.MAX_VALUE, implementation));
        childrenList.add(new Property("fhirVersion", "id", "The version of the FHIR specification on which this capability statement is based.", 0, java.lang.Integer.MAX_VALUE, fhirVersion));
        childrenList.add(new Property("acceptUnknown", "code", "A code that indicates whether the application accepts unknown elements or extensions when reading resources.", 0, java.lang.Integer.MAX_VALUE, acceptUnknown));
        childrenList.add(new Property("format", "code", "A list of the formats supported by this implementation using their content types.", 0, java.lang.Integer.MAX_VALUE, format));
        childrenList.add(new Property("patchFormat", "code", "A list of the patch formats supported by this implementation using their content types.", 0, java.lang.Integer.MAX_VALUE, patchFormat));
        childrenList.add(new Property("implementationGuide", "uri", "A list of implementation guides that the server does (or should) support in their entirety.", 0, java.lang.Integer.MAX_VALUE, implementationGuide));
        childrenList.add(new Property("profile", "Reference(StructureDefinition)", "A list of profiles that represent different use cases supported by the system. For a server, \"supported by the system\" means the system hosts/produces a set of resources that are conformant to a particular profile, and allows clients that use its services to search using this profile and to find appropriate data. For a client, it means the system will search by this profile and process data according to the guidance implicit in the profile. See further discussion in [Using Profiles](profiling.html#profile-uses).", 0, java.lang.Integer.MAX_VALUE, profile));
        childrenList.add(new Property("rest", "", "A definition of the restful capabilities of the solution, if any.", 0, java.lang.Integer.MAX_VALUE, rest));
        childrenList.add(new Property("messaging", "", "A description of the messaging capabilities of the solution.", 0, java.lang.Integer.MAX_VALUE, messaging));
        childrenList.add(new Property("document", "", "A document definition.", 0, java.lang.Integer.MAX_VALUE, document));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        case 351608024: /*version*/ return this.version == null ? new Base[0] : new Base[] {this.version}; // StringType
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case 110371416: /*title*/ return this.title == null ? new Base[0] : new Base[] {this.title}; // StringType
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<PublicationStatus>
        case -404562712: /*experimental*/ return this.experimental == null ? new Base[0] : new Base[] {this.experimental}; // BooleanType
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateTimeType
        case 1447404028: /*publisher*/ return this.publisher == null ? new Base[0] : new Base[] {this.publisher}; // StringType
        case 951526432: /*contact*/ return this.contact == null ? new Base[0] : this.contact.toArray(new Base[this.contact.size()]); // ContactDetail
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // MarkdownType
        case -669707736: /*useContext*/ return this.useContext == null ? new Base[0] : this.useContext.toArray(new Base[this.useContext.size()]); // UsageContext
        case -507075711: /*jurisdiction*/ return this.jurisdiction == null ? new Base[0] : this.jurisdiction.toArray(new Base[this.jurisdiction.size()]); // CodeableConcept
        case -220463842: /*purpose*/ return this.purpose == null ? new Base[0] : new Base[] {this.purpose}; // MarkdownType
        case 1522889671: /*copyright*/ return this.copyright == null ? new Base[0] : new Base[] {this.copyright}; // MarkdownType
        case 3292052: /*kind*/ return this.kind == null ? new Base[0] : new Base[] {this.kind}; // Enumeration<CapabilityStatementKind>
        case -246883639: /*instantiates*/ return this.instantiates == null ? new Base[0] : this.instantiates.toArray(new Base[this.instantiates.size()]); // UriType
        case 1319330215: /*software*/ return this.software == null ? new Base[0] : new Base[] {this.software}; // CapabilityStatementSoftwareComponent
        case 1683336114: /*implementation*/ return this.implementation == null ? new Base[0] : new Base[] {this.implementation}; // CapabilityStatementImplementationComponent
        case 461006061: /*fhirVersion*/ return this.fhirVersion == null ? new Base[0] : new Base[] {this.fhirVersion}; // IdType
        case -1862642142: /*acceptUnknown*/ return this.acceptUnknown == null ? new Base[0] : new Base[] {this.acceptUnknown}; // Enumeration<UnknownContentCode>
        case -1268779017: /*format*/ return this.format == null ? new Base[0] : this.format.toArray(new Base[this.format.size()]); // CodeType
        case 172338783: /*patchFormat*/ return this.patchFormat == null ? new Base[0] : this.patchFormat.toArray(new Base[this.patchFormat.size()]); // CodeType
        case 156966506: /*implementationGuide*/ return this.implementationGuide == null ? new Base[0] : this.implementationGuide.toArray(new Base[this.implementationGuide.size()]); // UriType
        case -309425751: /*profile*/ return this.profile == null ? new Base[0] : this.profile.toArray(new Base[this.profile.size()]); // Reference
        case 3496916: /*rest*/ return this.rest == null ? new Base[0] : this.rest.toArray(new Base[this.rest.size()]); // CapabilityStatementRestComponent
        case -1440008444: /*messaging*/ return this.messaging == null ? new Base[0] : this.messaging.toArray(new Base[this.messaging.size()]); // CapabilityStatementMessagingComponent
        case 861720859: /*document*/ return this.document == null ? new Base[0] : this.document.toArray(new Base[this.document.size()]); // CapabilityStatementDocumentComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 116079: // url
          this.url = castToUri(value); // UriType
          return value;
        case 351608024: // version
          this.version = castToString(value); // StringType
          return value;
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case 110371416: // title
          this.title = castToString(value); // StringType
          return value;
        case -892481550: // status
          value = new PublicationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<PublicationStatus>
          return value;
        case -404562712: // experimental
          this.experimental = castToBoolean(value); // BooleanType
          return value;
        case 3076014: // date
          this.date = castToDateTime(value); // DateTimeType
          return value;
        case 1447404028: // publisher
          this.publisher = castToString(value); // StringType
          return value;
        case 951526432: // contact
          this.getContact().add(castToContactDetail(value)); // ContactDetail
          return value;
        case -1724546052: // description
          this.description = castToMarkdown(value); // MarkdownType
          return value;
        case -669707736: // useContext
          this.getUseContext().add(castToUsageContext(value)); // UsageContext
          return value;
        case -507075711: // jurisdiction
          this.getJurisdiction().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -220463842: // purpose
          this.purpose = castToMarkdown(value); // MarkdownType
          return value;
        case 1522889671: // copyright
          this.copyright = castToMarkdown(value); // MarkdownType
          return value;
        case 3292052: // kind
          value = new CapabilityStatementKindEnumFactory().fromType(castToCode(value));
          this.kind = (Enumeration) value; // Enumeration<CapabilityStatementKind>
          return value;
        case -246883639: // instantiates
          this.getInstantiates().add(castToUri(value)); // UriType
          return value;
        case 1319330215: // software
          this.software = (CapabilityStatementSoftwareComponent) value; // CapabilityStatementSoftwareComponent
          return value;
        case 1683336114: // implementation
          this.implementation = (CapabilityStatementImplementationComponent) value; // CapabilityStatementImplementationComponent
          return value;
        case 461006061: // fhirVersion
          this.fhirVersion = castToId(value); // IdType
          return value;
        case -1862642142: // acceptUnknown
          value = new UnknownContentCodeEnumFactory().fromType(castToCode(value));
          this.acceptUnknown = (Enumeration) value; // Enumeration<UnknownContentCode>
          return value;
        case -1268779017: // format
          this.getFormat().add(castToCode(value)); // CodeType
          return value;
        case 172338783: // patchFormat
          this.getPatchFormat().add(castToCode(value)); // CodeType
          return value;
        case 156966506: // implementationGuide
          this.getImplementationGuide().add(castToUri(value)); // UriType
          return value;
        case -309425751: // profile
          this.getProfile().add(castToReference(value)); // Reference
          return value;
        case 3496916: // rest
          this.getRest().add((CapabilityStatementRestComponent) value); // CapabilityStatementRestComponent
          return value;
        case -1440008444: // messaging
          this.getMessaging().add((CapabilityStatementMessagingComponent) value); // CapabilityStatementMessagingComponent
          return value;
        case 861720859: // document
          this.getDocument().add((CapabilityStatementDocumentComponent) value); // CapabilityStatementDocumentComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("url")) {
          this.url = castToUri(value); // UriType
        } else if (name.equals("version")) {
          this.version = castToString(value); // StringType
        } else if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("title")) {
          this.title = castToString(value); // StringType
        } else if (name.equals("status")) {
          value = new PublicationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<PublicationStatus>
        } else if (name.equals("experimental")) {
          this.experimental = castToBoolean(value); // BooleanType
        } else if (name.equals("date")) {
          this.date = castToDateTime(value); // DateTimeType
        } else if (name.equals("publisher")) {
          this.publisher = castToString(value); // StringType
        } else if (name.equals("contact")) {
          this.getContact().add(castToContactDetail(value));
        } else if (name.equals("description")) {
          this.description = castToMarkdown(value); // MarkdownType
        } else if (name.equals("useContext")) {
          this.getUseContext().add(castToUsageContext(value));
        } else if (name.equals("jurisdiction")) {
          this.getJurisdiction().add(castToCodeableConcept(value));
        } else if (name.equals("purpose")) {
          this.purpose = castToMarkdown(value); // MarkdownType
        } else if (name.equals("copyright")) {
          this.copyright = castToMarkdown(value); // MarkdownType
        } else if (name.equals("kind")) {
          value = new CapabilityStatementKindEnumFactory().fromType(castToCode(value));
          this.kind = (Enumeration) value; // Enumeration<CapabilityStatementKind>
        } else if (name.equals("instantiates")) {
          this.getInstantiates().add(castToUri(value));
        } else if (name.equals("software")) {
          this.software = (CapabilityStatementSoftwareComponent) value; // CapabilityStatementSoftwareComponent
        } else if (name.equals("implementation")) {
          this.implementation = (CapabilityStatementImplementationComponent) value; // CapabilityStatementImplementationComponent
        } else if (name.equals("fhirVersion")) {
          this.fhirVersion = castToId(value); // IdType
        } else if (name.equals("acceptUnknown")) {
          value = new UnknownContentCodeEnumFactory().fromType(castToCode(value));
          this.acceptUnknown = (Enumeration) value; // Enumeration<UnknownContentCode>
        } else if (name.equals("format")) {
          this.getFormat().add(castToCode(value));
        } else if (name.equals("patchFormat")) {
          this.getPatchFormat().add(castToCode(value));
        } else if (name.equals("implementationGuide")) {
          this.getImplementationGuide().add(castToUri(value));
        } else if (name.equals("profile")) {
          this.getProfile().add(castToReference(value));
        } else if (name.equals("rest")) {
          this.getRest().add((CapabilityStatementRestComponent) value);
        } else if (name.equals("messaging")) {
          this.getMessaging().add((CapabilityStatementMessagingComponent) value);
        } else if (name.equals("document")) {
          this.getDocument().add((CapabilityStatementDocumentComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116079:  return getUrlElement();
        case 351608024:  return getVersionElement();
        case 3373707:  return getNameElement();
        case 110371416:  return getTitleElement();
        case -892481550:  return getStatusElement();
        case -404562712:  return getExperimentalElement();
        case 3076014:  return getDateElement();
        case 1447404028:  return getPublisherElement();
        case 951526432:  return addContact(); 
        case -1724546052:  return getDescriptionElement();
        case -669707736:  return addUseContext(); 
        case -507075711:  return addJurisdiction(); 
        case -220463842:  return getPurposeElement();
        case 1522889671:  return getCopyrightElement();
        case 3292052:  return getKindElement();
        case -246883639:  return addInstantiatesElement();
        case 1319330215:  return getSoftware(); 
        case 1683336114:  return getImplementation(); 
        case 461006061:  return getFhirVersionElement();
        case -1862642142:  return getAcceptUnknownElement();
        case -1268779017:  return addFormatElement();
        case 172338783:  return addPatchFormatElement();
        case 156966506:  return addImplementationGuideElement();
        case -309425751:  return addProfile(); 
        case 3496916:  return addRest(); 
        case -1440008444:  return addMessaging(); 
        case 861720859:  return addDocument(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116079: /*url*/ return new String[] {"uri"};
        case 351608024: /*version*/ return new String[] {"string"};
        case 3373707: /*name*/ return new String[] {"string"};
        case 110371416: /*title*/ return new String[] {"string"};
        case -892481550: /*status*/ return new String[] {"code"};
        case -404562712: /*experimental*/ return new String[] {"boolean"};
        case 3076014: /*date*/ return new String[] {"dateTime"};
        case 1447404028: /*publisher*/ return new String[] {"string"};
        case 951526432: /*contact*/ return new String[] {"ContactDetail"};
        case -1724546052: /*description*/ return new String[] {"markdown"};
        case -669707736: /*useContext*/ return new String[] {"UsageContext"};
        case -507075711: /*jurisdiction*/ return new String[] {"CodeableConcept"};
        case -220463842: /*purpose*/ return new String[] {"markdown"};
        case 1522889671: /*copyright*/ return new String[] {"markdown"};
        case 3292052: /*kind*/ return new String[] {"code"};
        case -246883639: /*instantiates*/ return new String[] {"uri"};
        case 1319330215: /*software*/ return new String[] {};
        case 1683336114: /*implementation*/ return new String[] {};
        case 461006061: /*fhirVersion*/ return new String[] {"id"};
        case -1862642142: /*acceptUnknown*/ return new String[] {"code"};
        case -1268779017: /*format*/ return new String[] {"code"};
        case 172338783: /*patchFormat*/ return new String[] {"code"};
        case 156966506: /*implementationGuide*/ return new String[] {"uri"};
        case -309425751: /*profile*/ return new String[] {"Reference"};
        case 3496916: /*rest*/ return new String[] {};
        case -1440008444: /*messaging*/ return new String[] {};
        case 861720859: /*document*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.url");
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.version");
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.name");
        }
        else if (name.equals("title")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.title");
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.status");
        }
        else if (name.equals("experimental")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.experimental");
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.date");
        }
        else if (name.equals("publisher")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.publisher");
        }
        else if (name.equals("contact")) {
          return addContact();
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.description");
        }
        else if (name.equals("useContext")) {
          return addUseContext();
        }
        else if (name.equals("jurisdiction")) {
          return addJurisdiction();
        }
        else if (name.equals("purpose")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.purpose");
        }
        else if (name.equals("copyright")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.copyright");
        }
        else if (name.equals("kind")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.kind");
        }
        else if (name.equals("instantiates")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.instantiates");
        }
        else if (name.equals("software")) {
          this.software = new CapabilityStatementSoftwareComponent();
          return this.software;
        }
        else if (name.equals("implementation")) {
          this.implementation = new CapabilityStatementImplementationComponent();
          return this.implementation;
        }
        else if (name.equals("fhirVersion")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.fhirVersion");
        }
        else if (name.equals("acceptUnknown")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.acceptUnknown");
        }
        else if (name.equals("format")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.format");
        }
        else if (name.equals("patchFormat")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.patchFormat");
        }
        else if (name.equals("implementationGuide")) {
          throw new FHIRException("Cannot call addChild on a primitive type CapabilityStatement.implementationGuide");
        }
        else if (name.equals("profile")) {
          return addProfile();
        }
        else if (name.equals("rest")) {
          return addRest();
        }
        else if (name.equals("messaging")) {
          return addMessaging();
        }
        else if (name.equals("document")) {
          return addDocument();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "CapabilityStatement";

  }

      public CapabilityStatement copy() {
        CapabilityStatement dst = new CapabilityStatement();
        copyValues(dst);
        dst.url = url == null ? null : url.copy();
        dst.version = version == null ? null : version.copy();
        dst.name = name == null ? null : name.copy();
        dst.title = title == null ? null : title.copy();
        dst.status = status == null ? null : status.copy();
        dst.experimental = experimental == null ? null : experimental.copy();
        dst.date = date == null ? null : date.copy();
        dst.publisher = publisher == null ? null : publisher.copy();
        if (contact != null) {
          dst.contact = new ArrayList<ContactDetail>();
          for (ContactDetail i : contact)
            dst.contact.add(i.copy());
        };
        dst.description = description == null ? null : description.copy();
        if (useContext != null) {
          dst.useContext = new ArrayList<UsageContext>();
          for (UsageContext i : useContext)
            dst.useContext.add(i.copy());
        };
        if (jurisdiction != null) {
          dst.jurisdiction = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : jurisdiction)
            dst.jurisdiction.add(i.copy());
        };
        dst.purpose = purpose == null ? null : purpose.copy();
        dst.copyright = copyright == null ? null : copyright.copy();
        dst.kind = kind == null ? null : kind.copy();
        if (instantiates != null) {
          dst.instantiates = new ArrayList<UriType>();
          for (UriType i : instantiates)
            dst.instantiates.add(i.copy());
        };
        dst.software = software == null ? null : software.copy();
        dst.implementation = implementation == null ? null : implementation.copy();
        dst.fhirVersion = fhirVersion == null ? null : fhirVersion.copy();
        dst.acceptUnknown = acceptUnknown == null ? null : acceptUnknown.copy();
        if (format != null) {
          dst.format = new ArrayList<CodeType>();
          for (CodeType i : format)
            dst.format.add(i.copy());
        };
        if (patchFormat != null) {
          dst.patchFormat = new ArrayList<CodeType>();
          for (CodeType i : patchFormat)
            dst.patchFormat.add(i.copy());
        };
        if (implementationGuide != null) {
          dst.implementationGuide = new ArrayList<UriType>();
          for (UriType i : implementationGuide)
            dst.implementationGuide.add(i.copy());
        };
        if (profile != null) {
          dst.profile = new ArrayList<Reference>();
          for (Reference i : profile)
            dst.profile.add(i.copy());
        };
        if (rest != null) {
          dst.rest = new ArrayList<CapabilityStatementRestComponent>();
          for (CapabilityStatementRestComponent i : rest)
            dst.rest.add(i.copy());
        };
        if (messaging != null) {
          dst.messaging = new ArrayList<CapabilityStatementMessagingComponent>();
          for (CapabilityStatementMessagingComponent i : messaging)
            dst.messaging.add(i.copy());
        };
        if (document != null) {
          dst.document = new ArrayList<CapabilityStatementDocumentComponent>();
          for (CapabilityStatementDocumentComponent i : document)
            dst.document.add(i.copy());
        };
        return dst;
      }

      protected CapabilityStatement typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CapabilityStatement))
          return false;
        CapabilityStatement o = (CapabilityStatement) other;
        return compareDeep(purpose, o.purpose, true) && compareDeep(copyright, o.copyright, true) && compareDeep(kind, o.kind, true)
           && compareDeep(instantiates, o.instantiates, true) && compareDeep(software, o.software, true) && compareDeep(implementation, o.implementation, true)
           && compareDeep(fhirVersion, o.fhirVersion, true) && compareDeep(acceptUnknown, o.acceptUnknown, true)
           && compareDeep(format, o.format, true) && compareDeep(patchFormat, o.patchFormat, true) && compareDeep(implementationGuide, o.implementationGuide, true)
           && compareDeep(profile, o.profile, true) && compareDeep(rest, o.rest, true) && compareDeep(messaging, o.messaging, true)
           && compareDeep(document, o.document, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CapabilityStatement))
          return false;
        CapabilityStatement o = (CapabilityStatement) other;
        return compareValues(purpose, o.purpose, true) && compareValues(copyright, o.copyright, true) && compareValues(kind, o.kind, true)
           && compareValues(instantiates, o.instantiates, true) && compareValues(fhirVersion, o.fhirVersion, true)
           && compareValues(acceptUnknown, o.acceptUnknown, true) && compareValues(format, o.format, true) && compareValues(patchFormat, o.patchFormat, true)
           && compareValues(implementationGuide, o.implementationGuide, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(purpose, copyright, kind
          , instantiates, software, implementation, fhirVersion, acceptUnknown, format, patchFormat
          , implementationGuide, profile, rest, messaging, document);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.CapabilityStatement;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>The capability statement publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>CapabilityStatement.date</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="CapabilityStatement.date", description="The capability statement publication date", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>The capability statement publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>CapabilityStatement.date</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>resource-profile</b>
   * <p>
   * Description: <b>A profile id invoked in a capability statement</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CapabilityStatement.rest.resource.profile</b><br>
   * </p>
   */
  @SearchParamDefinition(name="resource-profile", path="CapabilityStatement.rest.resource.profile", description="A profile id invoked in a capability statement", type="reference", target={StructureDefinition.class } )
  public static final String SP_RESOURCE_PROFILE = "resource-profile";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>resource-profile</b>
   * <p>
   * Description: <b>A profile id invoked in a capability statement</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CapabilityStatement.rest.resource.profile</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam RESOURCE_PROFILE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_RESOURCE_PROFILE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>CapabilityStatement:resource-profile</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_RESOURCE_PROFILE = new ca.uhn.fhir.model.api.Include("CapabilityStatement:resource-profile").toLocked();

 /**
   * Search parameter: <b>software</b>
   * <p>
   * Description: <b>Part of a the name of a software application</b><br>
   * Type: <b>string</b><br>
   * Path: <b>CapabilityStatement.software.name</b><br>
   * </p>
   */
  @SearchParamDefinition(name="software", path="CapabilityStatement.software.name", description="Part of a the name of a software application", type="string" )
  public static final String SP_SOFTWARE = "software";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>software</b>
   * <p>
   * Description: <b>Part of a the name of a software application</b><br>
   * Type: <b>string</b><br>
   * Path: <b>CapabilityStatement.software.name</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam SOFTWARE = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_SOFTWARE);

 /**
   * Search parameter: <b>resource</b>
   * <p>
   * Description: <b>Name of a resource mentioned in a capability statement</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CapabilityStatement.rest.resource.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="resource", path="CapabilityStatement.rest.resource.type", description="Name of a resource mentioned in a capability statement", type="token" )
  public static final String SP_RESOURCE = "resource";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>resource</b>
   * <p>
   * Description: <b>Name of a resource mentioned in a capability statement</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CapabilityStatement.rest.resource.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam RESOURCE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_RESOURCE);

 /**
   * Search parameter: <b>jurisdiction</b>
   * <p>
   * Description: <b>Intended jurisdiction for the capability statement</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CapabilityStatement.jurisdiction</b><br>
   * </p>
   */
  @SearchParamDefinition(name="jurisdiction", path="CapabilityStatement.jurisdiction", description="Intended jurisdiction for the capability statement", type="token" )
  public static final String SP_JURISDICTION = "jurisdiction";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>jurisdiction</b>
   * <p>
   * Description: <b>Intended jurisdiction for the capability statement</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CapabilityStatement.jurisdiction</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam JURISDICTION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_JURISDICTION);

 /**
   * Search parameter: <b>format</b>
   * <p>
   * Description: <b>formats supported (xml | json | ttl | mime type)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CapabilityStatement.format</b><br>
   * </p>
   */
  @SearchParamDefinition(name="format", path="CapabilityStatement.format", description="formats supported (xml | json | ttl | mime type)", type="token" )
  public static final String SP_FORMAT = "format";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>format</b>
   * <p>
   * Description: <b>formats supported (xml | json | ttl | mime type)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CapabilityStatement.format</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam FORMAT = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_FORMAT);

 /**
   * Search parameter: <b>description</b>
   * <p>
   * Description: <b>The description of the capability statement</b><br>
   * Type: <b>string</b><br>
   * Path: <b>CapabilityStatement.description</b><br>
   * </p>
   */
  @SearchParamDefinition(name="description", path="CapabilityStatement.description", description="The description of the capability statement", type="string" )
  public static final String SP_DESCRIPTION = "description";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>description</b>
   * <p>
   * Description: <b>The description of the capability statement</b><br>
   * Type: <b>string</b><br>
   * Path: <b>CapabilityStatement.description</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam DESCRIPTION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_DESCRIPTION);

 /**
   * Search parameter: <b>title</b>
   * <p>
   * Description: <b>The human-friendly name of the capability statement</b><br>
   * Type: <b>string</b><br>
   * Path: <b>CapabilityStatement.title</b><br>
   * </p>
   */
  @SearchParamDefinition(name="title", path="CapabilityStatement.title", description="The human-friendly name of the capability statement", type="string" )
  public static final String SP_TITLE = "title";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>title</b>
   * <p>
   * Description: <b>The human-friendly name of the capability statement</b><br>
   * Type: <b>string</b><br>
   * Path: <b>CapabilityStatement.title</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam TITLE = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_TITLE);

 /**
   * Search parameter: <b>fhirversion</b>
   * <p>
   * Description: <b>The version of FHIR</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CapabilityStatement.version</b><br>
   * </p>
   */
  @SearchParamDefinition(name="fhirversion", path="CapabilityStatement.version", description="The version of FHIR", type="token" )
  public static final String SP_FHIRVERSION = "fhirversion";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>fhirversion</b>
   * <p>
   * Description: <b>The version of FHIR</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CapabilityStatement.version</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam FHIRVERSION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_FHIRVERSION);

 /**
   * Search parameter: <b>version</b>
   * <p>
   * Description: <b>The business version of the capability statement</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CapabilityStatement.version</b><br>
   * </p>
   */
  @SearchParamDefinition(name="version", path="CapabilityStatement.version", description="The business version of the capability statement", type="token" )
  public static final String SP_VERSION = "version";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>version</b>
   * <p>
   * Description: <b>The business version of the capability statement</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CapabilityStatement.version</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam VERSION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_VERSION);

 /**
   * Search parameter: <b>url</b>
   * <p>
   * Description: <b>The uri that identifies the capability statement</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>CapabilityStatement.url</b><br>
   * </p>
   */
  @SearchParamDefinition(name="url", path="CapabilityStatement.url", description="The uri that identifies the capability statement", type="uri" )
  public static final String SP_URL = "url";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>url</b>
   * <p>
   * Description: <b>The uri that identifies the capability statement</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>CapabilityStatement.url</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam URL = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_URL);

 /**
   * Search parameter: <b>supported-profile</b>
   * <p>
   * Description: <b>Profiles for use cases supported</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CapabilityStatement.profile</b><br>
   * </p>
   */
  @SearchParamDefinition(name="supported-profile", path="CapabilityStatement.profile", description="Profiles for use cases supported", type="reference", target={StructureDefinition.class } )
  public static final String SP_SUPPORTED_PROFILE = "supported-profile";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>supported-profile</b>
   * <p>
   * Description: <b>Profiles for use cases supported</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CapabilityStatement.profile</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUPPORTED_PROFILE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUPPORTED_PROFILE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>CapabilityStatement:supported-profile</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUPPORTED_PROFILE = new ca.uhn.fhir.model.api.Include("CapabilityStatement:supported-profile").toLocked();

 /**
   * Search parameter: <b>mode</b>
   * <p>
   * Description: <b>Mode - restful (server/client) or messaging (sender/receiver)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CapabilityStatement.rest.mode</b><br>
   * </p>
   */
  @SearchParamDefinition(name="mode", path="CapabilityStatement.rest.mode", description="Mode - restful (server/client) or messaging (sender/receiver)", type="token" )
  public static final String SP_MODE = "mode";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>mode</b>
   * <p>
   * Description: <b>Mode - restful (server/client) or messaging (sender/receiver)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CapabilityStatement.rest.mode</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam MODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_MODE);

 /**
   * Search parameter: <b>security-service</b>
   * <p>
   * Description: <b>OAuth | SMART-on-FHIR | NTLM | Basic | Kerberos | Certificates</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CapabilityStatement.rest.security.service</b><br>
   * </p>
   */
  @SearchParamDefinition(name="security-service", path="CapabilityStatement.rest.security.service", description="OAuth | SMART-on-FHIR | NTLM | Basic | Kerberos | Certificates", type="token" )
  public static final String SP_SECURITY_SERVICE = "security-service";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>security-service</b>
   * <p>
   * Description: <b>OAuth | SMART-on-FHIR | NTLM | Basic | Kerberos | Certificates</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CapabilityStatement.rest.security.service</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam SECURITY_SERVICE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_SECURITY_SERVICE);

 /**
   * Search parameter: <b>name</b>
   * <p>
   * Description: <b>Computationally friendly name of the capability statement</b><br>
   * Type: <b>string</b><br>
   * Path: <b>CapabilityStatement.name</b><br>
   * </p>
   */
  @SearchParamDefinition(name="name", path="CapabilityStatement.name", description="Computationally friendly name of the capability statement", type="string" )
  public static final String SP_NAME = "name";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>name</b>
   * <p>
   * Description: <b>Computationally friendly name of the capability statement</b><br>
   * Type: <b>string</b><br>
   * Path: <b>CapabilityStatement.name</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam NAME = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_NAME);

 /**
   * Search parameter: <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the capability statement</b><br>
   * Type: <b>string</b><br>
   * Path: <b>CapabilityStatement.publisher</b><br>
   * </p>
   */
  @SearchParamDefinition(name="publisher", path="CapabilityStatement.publisher", description="Name of the publisher of the capability statement", type="string" )
  public static final String SP_PUBLISHER = "publisher";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the capability statement</b><br>
   * Type: <b>string</b><br>
   * Path: <b>CapabilityStatement.publisher</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam PUBLISHER = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_PUBLISHER);

 /**
   * Search parameter: <b>event</b>
   * <p>
   * Description: <b>Event code in a capability statement</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CapabilityStatement.messaging.event.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="event", path="CapabilityStatement.messaging.event.code", description="Event code in a capability statement", type="token" )
  public static final String SP_EVENT = "event";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>event</b>
   * <p>
   * Description: <b>Event code in a capability statement</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CapabilityStatement.messaging.event.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam EVENT = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_EVENT);

 /**
   * Search parameter: <b>guide</b>
   * <p>
   * Description: <b>Implementation guides supported</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>CapabilityStatement.implementationGuide</b><br>
   * </p>
   */
  @SearchParamDefinition(name="guide", path="CapabilityStatement.implementationGuide", description="Implementation guides supported", type="uri" )
  public static final String SP_GUIDE = "guide";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>guide</b>
   * <p>
   * Description: <b>Implementation guides supported</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>CapabilityStatement.implementationGuide</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam GUIDE = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_GUIDE);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The current status of the capability statement</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CapabilityStatement.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="CapabilityStatement.status", description="The current status of the capability statement", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The current status of the capability statement</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CapabilityStatement.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

