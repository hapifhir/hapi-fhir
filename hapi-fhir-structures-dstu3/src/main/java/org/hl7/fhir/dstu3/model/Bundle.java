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

import java.math.*;
import org.hl7.fhir.utilities.Utilities;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;

import org.hl7.fhir.dstu3.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.*;
/**
 * A container for a collection of resources.
 */
@ResourceDef(name="Bundle", profile="http://hl7.org/fhir/Profile/Bundle")
public class Bundle extends Resource implements IBaseBundle {

    public enum BundleType {
        /**
         * The bundle is a document. The first resource is a Composition.
         */
        DOCUMENT, 
        /**
         * The bundle is a message. The first resource is a MessageHeader.
         */
        MESSAGE, 
        /**
         * The bundle is a transaction - intended to be processed by a server as an atomic commit.
         */
        TRANSACTION, 
        /**
         * The bundle is a transaction response. Because the response is a transaction response, the transactionhas succeeded, and all responses are error free.
         */
        TRANSACTIONRESPONSE, 
        /**
         * The bundle is a transaction - intended to be processed by a server as a group of actions.
         */
        BATCH, 
        /**
         * The bundle is a batch response. Note that as a batch, some responses may indicate failure and others success.
         */
        BATCHRESPONSE, 
        /**
         * The bundle is a list of resources from a history interaction on a server.
         */
        HISTORY, 
        /**
         * The bundle is a list of resources returned as a result of a search/query interaction, operation, or message.
         */
        SEARCHSET, 
        /**
         * The bundle is a set of resources collected into a single document for ease of distribution.
         */
        COLLECTION, 
        /**
         * added to help the parsers
         */
        NULL;
        public static BundleType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("document".equals(codeString))
          return DOCUMENT;
        if ("message".equals(codeString))
          return MESSAGE;
        if ("transaction".equals(codeString))
          return TRANSACTION;
        if ("transaction-response".equals(codeString))
          return TRANSACTIONRESPONSE;
        if ("batch".equals(codeString))
          return BATCH;
        if ("batch-response".equals(codeString))
          return BATCHRESPONSE;
        if ("history".equals(codeString))
          return HISTORY;
        if ("searchset".equals(codeString))
          return SEARCHSET;
        if ("collection".equals(codeString))
          return COLLECTION;
        throw new FHIRException("Unknown BundleType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DOCUMENT: return "document";
            case MESSAGE: return "message";
            case TRANSACTION: return "transaction";
            case TRANSACTIONRESPONSE: return "transaction-response";
            case BATCH: return "batch";
            case BATCHRESPONSE: return "batch-response";
            case HISTORY: return "history";
            case SEARCHSET: return "searchset";
            case COLLECTION: return "collection";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case DOCUMENT: return "http://hl7.org/fhir/bundle-type";
            case MESSAGE: return "http://hl7.org/fhir/bundle-type";
            case TRANSACTION: return "http://hl7.org/fhir/bundle-type";
            case TRANSACTIONRESPONSE: return "http://hl7.org/fhir/bundle-type";
            case BATCH: return "http://hl7.org/fhir/bundle-type";
            case BATCHRESPONSE: return "http://hl7.org/fhir/bundle-type";
            case HISTORY: return "http://hl7.org/fhir/bundle-type";
            case SEARCHSET: return "http://hl7.org/fhir/bundle-type";
            case COLLECTION: return "http://hl7.org/fhir/bundle-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case DOCUMENT: return "The bundle is a document. The first resource is a Composition.";
            case MESSAGE: return "The bundle is a message. The first resource is a MessageHeader.";
            case TRANSACTION: return "The bundle is a transaction - intended to be processed by a server as an atomic commit.";
            case TRANSACTIONRESPONSE: return "The bundle is a transaction response. Because the response is a transaction response, the transactionhas succeeded, and all responses are error free.";
            case BATCH: return "The bundle is a transaction - intended to be processed by a server as a group of actions.";
            case BATCHRESPONSE: return "The bundle is a batch response. Note that as a batch, some responses may indicate failure and others success.";
            case HISTORY: return "The bundle is a list of resources from a history interaction on a server.";
            case SEARCHSET: return "The bundle is a list of resources returned as a result of a search/query interaction, operation, or message.";
            case COLLECTION: return "The bundle is a set of resources collected into a single document for ease of distribution.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DOCUMENT: return "Document";
            case MESSAGE: return "Message";
            case TRANSACTION: return "Transaction";
            case TRANSACTIONRESPONSE: return "Transaction Response";
            case BATCH: return "Batch";
            case BATCHRESPONSE: return "Batch Response";
            case HISTORY: return "History List";
            case SEARCHSET: return "Search Results";
            case COLLECTION: return "Collection";
            default: return "?";
          }
        }
    }

  public static class BundleTypeEnumFactory implements EnumFactory<BundleType> {
    public BundleType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("document".equals(codeString))
          return BundleType.DOCUMENT;
        if ("message".equals(codeString))
          return BundleType.MESSAGE;
        if ("transaction".equals(codeString))
          return BundleType.TRANSACTION;
        if ("transaction-response".equals(codeString))
          return BundleType.TRANSACTIONRESPONSE;
        if ("batch".equals(codeString))
          return BundleType.BATCH;
        if ("batch-response".equals(codeString))
          return BundleType.BATCHRESPONSE;
        if ("history".equals(codeString))
          return BundleType.HISTORY;
        if ("searchset".equals(codeString))
          return BundleType.SEARCHSET;
        if ("collection".equals(codeString))
          return BundleType.COLLECTION;
        throw new IllegalArgumentException("Unknown BundleType code '"+codeString+"'");
        }
        public Enumeration<BundleType> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("document".equals(codeString))
          return new Enumeration<BundleType>(this, BundleType.DOCUMENT);
        if ("message".equals(codeString))
          return new Enumeration<BundleType>(this, BundleType.MESSAGE);
        if ("transaction".equals(codeString))
          return new Enumeration<BundleType>(this, BundleType.TRANSACTION);
        if ("transaction-response".equals(codeString))
          return new Enumeration<BundleType>(this, BundleType.TRANSACTIONRESPONSE);
        if ("batch".equals(codeString))
          return new Enumeration<BundleType>(this, BundleType.BATCH);
        if ("batch-response".equals(codeString))
          return new Enumeration<BundleType>(this, BundleType.BATCHRESPONSE);
        if ("history".equals(codeString))
          return new Enumeration<BundleType>(this, BundleType.HISTORY);
        if ("searchset".equals(codeString))
          return new Enumeration<BundleType>(this, BundleType.SEARCHSET);
        if ("collection".equals(codeString))
          return new Enumeration<BundleType>(this, BundleType.COLLECTION);
        throw new FHIRException("Unknown BundleType code '"+codeString+"'");
        }
    public String toCode(BundleType code) {
      if (code == BundleType.DOCUMENT)
        return "document";
      if (code == BundleType.MESSAGE)
        return "message";
      if (code == BundleType.TRANSACTION)
        return "transaction";
      if (code == BundleType.TRANSACTIONRESPONSE)
        return "transaction-response";
      if (code == BundleType.BATCH)
        return "batch";
      if (code == BundleType.BATCHRESPONSE)
        return "batch-response";
      if (code == BundleType.HISTORY)
        return "history";
      if (code == BundleType.SEARCHSET)
        return "searchset";
      if (code == BundleType.COLLECTION)
        return "collection";
      return "?";
      }
    public String toSystem(BundleType code) {
      return code.getSystem();
      }
    }

    public enum SearchEntryMode {
        /**
         * This resource matched the search specification.
         */
        MATCH, 
        /**
         * This resource is returned because it is referred to from another resource in the search set.
         */
        INCLUDE, 
        /**
         * An OperationOutcome that provides additional information about the processing of a search.
         */
        OUTCOME, 
        /**
         * added to help the parsers
         */
        NULL;
        public static SearchEntryMode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("match".equals(codeString))
          return MATCH;
        if ("include".equals(codeString))
          return INCLUDE;
        if ("outcome".equals(codeString))
          return OUTCOME;
        throw new FHIRException("Unknown SearchEntryMode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case MATCH: return "match";
            case INCLUDE: return "include";
            case OUTCOME: return "outcome";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case MATCH: return "http://hl7.org/fhir/search-entry-mode";
            case INCLUDE: return "http://hl7.org/fhir/search-entry-mode";
            case OUTCOME: return "http://hl7.org/fhir/search-entry-mode";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case MATCH: return "This resource matched the search specification.";
            case INCLUDE: return "This resource is returned because it is referred to from another resource in the search set.";
            case OUTCOME: return "An OperationOutcome that provides additional information about the processing of a search.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case MATCH: return "Match";
            case INCLUDE: return "Include";
            case OUTCOME: return "Outcome";
            default: return "?";
          }
        }
    }

  public static class SearchEntryModeEnumFactory implements EnumFactory<SearchEntryMode> {
    public SearchEntryMode fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("match".equals(codeString))
          return SearchEntryMode.MATCH;
        if ("include".equals(codeString))
          return SearchEntryMode.INCLUDE;
        if ("outcome".equals(codeString))
          return SearchEntryMode.OUTCOME;
        throw new IllegalArgumentException("Unknown SearchEntryMode code '"+codeString+"'");
        }
        public Enumeration<SearchEntryMode> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("match".equals(codeString))
          return new Enumeration<SearchEntryMode>(this, SearchEntryMode.MATCH);
        if ("include".equals(codeString))
          return new Enumeration<SearchEntryMode>(this, SearchEntryMode.INCLUDE);
        if ("outcome".equals(codeString))
          return new Enumeration<SearchEntryMode>(this, SearchEntryMode.OUTCOME);
        throw new FHIRException("Unknown SearchEntryMode code '"+codeString+"'");
        }
    public String toCode(SearchEntryMode code) {
      if (code == SearchEntryMode.MATCH)
        return "match";
      if (code == SearchEntryMode.INCLUDE)
        return "include";
      if (code == SearchEntryMode.OUTCOME)
        return "outcome";
      return "?";
      }
    public String toSystem(SearchEntryMode code) {
      return code.getSystem();
      }
    }

    public enum HTTPVerb {
        /**
         * HTTP GET
         */
        GET, 
        /**
         * HTTP POST
         */
        POST, 
        /**
         * HTTP PUT
         */
        PUT, 
        /**
         * HTTP DELETE
         */
        DELETE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static HTTPVerb fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("GET".equals(codeString))
          return GET;
        if ("POST".equals(codeString))
          return POST;
        if ("PUT".equals(codeString))
          return PUT;
        if ("DELETE".equals(codeString))
          return DELETE;
        throw new FHIRException("Unknown HTTPVerb code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case GET: return "GET";
            case POST: return "POST";
            case PUT: return "PUT";
            case DELETE: return "DELETE";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case GET: return "http://hl7.org/fhir/http-verb";
            case POST: return "http://hl7.org/fhir/http-verb";
            case PUT: return "http://hl7.org/fhir/http-verb";
            case DELETE: return "http://hl7.org/fhir/http-verb";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case GET: return "HTTP GET";
            case POST: return "HTTP POST";
            case PUT: return "HTTP PUT";
            case DELETE: return "HTTP DELETE";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case GET: return "GET";
            case POST: return "POST";
            case PUT: return "PUT";
            case DELETE: return "DELETE";
            default: return "?";
          }
        }
    }

  public static class HTTPVerbEnumFactory implements EnumFactory<HTTPVerb> {
    public HTTPVerb fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("GET".equals(codeString))
          return HTTPVerb.GET;
        if ("POST".equals(codeString))
          return HTTPVerb.POST;
        if ("PUT".equals(codeString))
          return HTTPVerb.PUT;
        if ("DELETE".equals(codeString))
          return HTTPVerb.DELETE;
        throw new IllegalArgumentException("Unknown HTTPVerb code '"+codeString+"'");
        }
        public Enumeration<HTTPVerb> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("GET".equals(codeString))
          return new Enumeration<HTTPVerb>(this, HTTPVerb.GET);
        if ("POST".equals(codeString))
          return new Enumeration<HTTPVerb>(this, HTTPVerb.POST);
        if ("PUT".equals(codeString))
          return new Enumeration<HTTPVerb>(this, HTTPVerb.PUT);
        if ("DELETE".equals(codeString))
          return new Enumeration<HTTPVerb>(this, HTTPVerb.DELETE);
        throw new FHIRException("Unknown HTTPVerb code '"+codeString+"'");
        }
    public String toCode(HTTPVerb code) {
      if (code == HTTPVerb.GET)
        return "GET";
      if (code == HTTPVerb.POST)
        return "POST";
      if (code == HTTPVerb.PUT)
        return "PUT";
      if (code == HTTPVerb.DELETE)
        return "DELETE";
      return "?";
      }
    public String toSystem(HTTPVerb code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class BundleLinkComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A name which details the functional use for this link - see [[http://www.iana.org/assignments/link-relations/link-relations.xhtml]].
         */
        @Child(name = "relation", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="http://www.iana.org/assignments/link-relations/link-relations.xhtml", formalDefinition="A name which details the functional use for this link - see [[http://www.iana.org/assignments/link-relations/link-relations.xhtml]]." )
        protected StringType relation;

        /**
         * The reference details for the link.
         */
        @Child(name = "url", type = {UriType.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Reference details for the link", formalDefinition="The reference details for the link." )
        protected UriType url;

        private static final long serialVersionUID = -1010386066L;

    /**
     * Constructor
     */
      public BundleLinkComponent() {
        super();
      }

    /**
     * Constructor
     */
      public BundleLinkComponent(StringType relation, UriType url) {
        super();
        this.relation = relation;
        this.url = url;
      }

        /**
         * @return {@link #relation} (A name which details the functional use for this link - see [[http://www.iana.org/assignments/link-relations/link-relations.xhtml]].). This is the underlying object with id, value and extensions. The accessor "getRelation" gives direct access to the value
         */
        public StringType getRelationElement() { 
          if (this.relation == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BundleLinkComponent.relation");
            else if (Configuration.doAutoCreate())
              this.relation = new StringType(); // bb
          return this.relation;
        }

        public boolean hasRelationElement() { 
          return this.relation != null && !this.relation.isEmpty();
        }

        public boolean hasRelation() { 
          return this.relation != null && !this.relation.isEmpty();
        }

        /**
         * @param value {@link #relation} (A name which details the functional use for this link - see [[http://www.iana.org/assignments/link-relations/link-relations.xhtml]].). This is the underlying object with id, value and extensions. The accessor "getRelation" gives direct access to the value
         */
        public BundleLinkComponent setRelationElement(StringType value) { 
          this.relation = value;
          return this;
        }

        /**
         * @return A name which details the functional use for this link - see [[http://www.iana.org/assignments/link-relations/link-relations.xhtml]].
         */
        public String getRelation() { 
          return this.relation == null ? null : this.relation.getValue();
        }

        /**
         * @param value A name which details the functional use for this link - see [[http://www.iana.org/assignments/link-relations/link-relations.xhtml]].
         */
        public BundleLinkComponent setRelation(String value) { 
            if (this.relation == null)
              this.relation = new StringType();
            this.relation.setValue(value);
          return this;
        }

        /**
         * @return {@link #url} (The reference details for the link.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
         */
        public UriType getUrlElement() { 
          if (this.url == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BundleLinkComponent.url");
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
         * @param value {@link #url} (The reference details for the link.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
         */
        public BundleLinkComponent setUrlElement(UriType value) { 
          this.url = value;
          return this;
        }

        /**
         * @return The reference details for the link.
         */
        public String getUrl() { 
          return this.url == null ? null : this.url.getValue();
        }

        /**
         * @param value The reference details for the link.
         */
        public BundleLinkComponent setUrl(String value) { 
            if (this.url == null)
              this.url = new UriType();
            this.url.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("relation", "string", "A name which details the functional use for this link - see [[http://www.iana.org/assignments/link-relations/link-relations.xhtml]].", 0, java.lang.Integer.MAX_VALUE, relation));
          childrenList.add(new Property("url", "uri", "The reference details for the link.", 0, java.lang.Integer.MAX_VALUE, url));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("relation"))
          this.relation = castToString(value); // StringType
        else if (name.equals("url"))
          this.url = castToUri(value); // UriType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("relation")) {
          throw new FHIRException("Cannot call addChild on a primitive type Bundle.relation");
        }
        else if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type Bundle.url");
        }
        else
          return super.addChild(name);
      }

      public BundleLinkComponent copy() {
        BundleLinkComponent dst = new BundleLinkComponent();
        copyValues(dst);
        dst.relation = relation == null ? null : relation.copy();
        dst.url = url == null ? null : url.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof BundleLinkComponent))
          return false;
        BundleLinkComponent o = (BundleLinkComponent) other;
        return compareDeep(relation, o.relation, true) && compareDeep(url, o.url, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof BundleLinkComponent))
          return false;
        BundleLinkComponent o = (BundleLinkComponent) other;
        return compareValues(relation, o.relation, true) && compareValues(url, o.url, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (relation == null || relation.isEmpty()) && (url == null || url.isEmpty())
          ;
      }

  public String fhirType() {
    return "Bundle.link";

  }

  }

    @Block()
    public static class BundleEntryComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A series of links that provide context to this entry.
         */
        @Child(name = "link", type = {BundleLinkComponent.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Links related to this entry", formalDefinition="A series of links that provide context to this entry." )
        protected List<BundleLinkComponent> link;

        /**
         * The Absolute URL for the resource. This must be provided for all resources. The fullUrl SHALL not disagree with the id in the resource. The fullUrl is a version independent reference to the resource.
         */
        @Child(name = "fullUrl", type = {UriType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Absolute URL for resource (server address, or UUID/OID)", formalDefinition="The Absolute URL for the resource. This must be provided for all resources. The fullUrl SHALL not disagree with the id in the resource. The fullUrl is a version independent reference to the resource." )
        protected UriType fullUrl;

        /**
         * The Resources for the entry.
         */
        @Child(name = "resource", type = {Resource.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="A resource in the bundle", formalDefinition="The Resources for the entry." )
        protected Resource resource;

        /**
         * Information about the search process that lead to the creation of this entry.
         */
        @Child(name = "search", type = {}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Search related information", formalDefinition="Information about the search process that lead to the creation of this entry." )
        protected BundleEntrySearchComponent search;

        /**
         * Additional information about how this entry should be processed as part of a transaction.
         */
        @Child(name = "request", type = {}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Transaction Related Information", formalDefinition="Additional information about how this entry should be processed as part of a transaction." )
        protected BundleEntryRequestComponent request;

        /**
         * Additional information about how this entry should be processed as part of a transaction.
         */
        @Child(name = "response", type = {}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Transaction Related Information", formalDefinition="Additional information about how this entry should be processed as part of a transaction." )
        protected BundleEntryResponseComponent response;

        private static final long serialVersionUID = 517783054L;

    /**
     * Constructor
     */
      public BundleEntryComponent() {
        super();
      }

        /**
         * @return {@link #link} (A series of links that provide context to this entry.)
         */
        public List<BundleLinkComponent> getLink() { 
          if (this.link == null)
            this.link = new ArrayList<BundleLinkComponent>();
          return this.link;
        }

        public boolean hasLink() { 
          if (this.link == null)
            return false;
          for (BundleLinkComponent item : this.link)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #link} (A series of links that provide context to this entry.)
         */
    // syntactic sugar
        public BundleLinkComponent addLink() { //3
          BundleLinkComponent t = new BundleLinkComponent();
          if (this.link == null)
            this.link = new ArrayList<BundleLinkComponent>();
          this.link.add(t);
          return t;
        }

    // syntactic sugar
        public BundleEntryComponent addLink(BundleLinkComponent t) { //3
          if (t == null)
            return this;
          if (this.link == null)
            this.link = new ArrayList<BundleLinkComponent>();
          this.link.add(t);
          return this;
        }

        /**
         * @return {@link #fullUrl} (The Absolute URL for the resource. This must be provided for all resources. The fullUrl SHALL not disagree with the id in the resource. The fullUrl is a version independent reference to the resource.). This is the underlying object with id, value and extensions. The accessor "getFullUrl" gives direct access to the value
         */
        public UriType getFullUrlElement() { 
          if (this.fullUrl == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BundleEntryComponent.fullUrl");
            else if (Configuration.doAutoCreate())
              this.fullUrl = new UriType(); // bb
          return this.fullUrl;
        }

        public boolean hasFullUrlElement() { 
          return this.fullUrl != null && !this.fullUrl.isEmpty();
        }

        public boolean hasFullUrl() { 
          return this.fullUrl != null && !this.fullUrl.isEmpty();
        }

        /**
         * @param value {@link #fullUrl} (The Absolute URL for the resource. This must be provided for all resources. The fullUrl SHALL not disagree with the id in the resource. The fullUrl is a version independent reference to the resource.). This is the underlying object with id, value and extensions. The accessor "getFullUrl" gives direct access to the value
         */
        public BundleEntryComponent setFullUrlElement(UriType value) { 
          this.fullUrl = value;
          return this;
        }

        /**
         * @return The Absolute URL for the resource. This must be provided for all resources. The fullUrl SHALL not disagree with the id in the resource. The fullUrl is a version independent reference to the resource.
         */
        public String getFullUrl() { 
          return this.fullUrl == null ? null : this.fullUrl.getValue();
        }

        /**
         * @param value The Absolute URL for the resource. This must be provided for all resources. The fullUrl SHALL not disagree with the id in the resource. The fullUrl is a version independent reference to the resource.
         */
        public BundleEntryComponent setFullUrl(String value) { 
          if (Utilities.noString(value))
            this.fullUrl = null;
          else {
            if (this.fullUrl == null)
              this.fullUrl = new UriType();
            this.fullUrl.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #resource} (The Resources for the entry.)
         */
        public Resource getResource() { 
          return this.resource;
        }

        public boolean hasResource() { 
          return this.resource != null && !this.resource.isEmpty();
        }

        /**
         * @param value {@link #resource} (The Resources for the entry.)
         */
        public BundleEntryComponent setResource(Resource value) { 
          this.resource = value;
          return this;
        }

        /**
         * @return {@link #search} (Information about the search process that lead to the creation of this entry.)
         */
        public BundleEntrySearchComponent getSearch() { 
          if (this.search == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BundleEntryComponent.search");
            else if (Configuration.doAutoCreate())
              this.search = new BundleEntrySearchComponent(); // cc
          return this.search;
        }

        public boolean hasSearch() { 
          return this.search != null && !this.search.isEmpty();
        }

        /**
         * @param value {@link #search} (Information about the search process that lead to the creation of this entry.)
         */
        public BundleEntryComponent setSearch(BundleEntrySearchComponent value) { 
          this.search = value;
          return this;
        }

        /**
         * @return {@link #request} (Additional information about how this entry should be processed as part of a transaction.)
         */
        public BundleEntryRequestComponent getRequest() { 
          if (this.request == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BundleEntryComponent.request");
            else if (Configuration.doAutoCreate())
              this.request = new BundleEntryRequestComponent(); // cc
          return this.request;
        }

        public boolean hasRequest() { 
          return this.request != null && !this.request.isEmpty();
        }

        /**
         * @param value {@link #request} (Additional information about how this entry should be processed as part of a transaction.)
         */
        public BundleEntryComponent setRequest(BundleEntryRequestComponent value) { 
          this.request = value;
          return this;
        }

        /**
         * @return {@link #response} (Additional information about how this entry should be processed as part of a transaction.)
         */
        public BundleEntryResponseComponent getResponse() { 
          if (this.response == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BundleEntryComponent.response");
            else if (Configuration.doAutoCreate())
              this.response = new BundleEntryResponseComponent(); // cc
          return this.response;
        }

        public boolean hasResponse() { 
          return this.response != null && !this.response.isEmpty();
        }

        /**
         * @param value {@link #response} (Additional information about how this entry should be processed as part of a transaction.)
         */
        public BundleEntryComponent setResponse(BundleEntryResponseComponent value) { 
          this.response = value;
          return this;
        }

 /**
   * Returns the {@link #getLink() link} which matches a given {@link BundleLinkComponent#getRelation() relation}. 
   * If no link is found which matches the given relation, returns <code>null</code>. If more than one
   * link is found which matches the given relation, returns the first matching BundleLinkComponent.
   * 
   * @param theRelation
   *            The relation, such as "next", or "self. See the constants such as {@link IBaseBundle#LINK_SELF} and {@link IBaseBundle#LINK_NEXT}.
   * @return Returns a matching BundleLinkComponent, or <code>null</code>
   * @see IBaseBundle#LINK_NEXT
   * @see IBaseBundle#LINK_PREV
   * @see IBaseBundle#LINK_SELF
   */
  public BundleLinkComponent getLink(String theRelation) {
    org.apache.commons.lang3.Validate.notBlank(theRelation, "theRelation may not be null or empty");
    for (BundleLinkComponent next : getLink()) {
      if (theRelation.equals(next.getRelation())) {
        return next;
      }
    }
    return null;
  }

  /**
   * Returns the {@link #getLink() link} which matches a given {@link BundleLinkComponent#getRelation() relation}. 
   * If no link is found which matches the given relation, creates a new BundleLinkComponent with the
   * given relation and adds it to this Bundle. If more than one
   * link is found which matches the given relation, returns the first matching BundleLinkComponent.
   * 
   * @param theRelation
   *            The relation, such as "next", or "self. See the constants such as {@link IBaseBundle#LINK_SELF} and {@link IBaseBundle#LINK_NEXT}.
   * @return Returns a matching BundleLinkComponent, or <code>null</code>
   * @see IBaseBundle#LINK_NEXT
   * @see IBaseBundle#LINK_PREV
   * @see IBaseBundle#LINK_SELF
   */
  public BundleLinkComponent getLinkOrCreate(String theRelation) {
    org.apache.commons.lang3.Validate.notBlank(theRelation, "theRelation may not be null or empty");
    for (BundleLinkComponent next : getLink()) {
      if (theRelation.equals(next.getRelation())) {
        return next;
      }
    }
    BundleLinkComponent retVal = new BundleLinkComponent();
    retVal.setRelation(theRelation);
    getLink().add(retVal);
    return retVal;
  }
        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("link", "@Bundle.link", "A series of links that provide context to this entry.", 0, java.lang.Integer.MAX_VALUE, link));
          childrenList.add(new Property("fullUrl", "uri", "The Absolute URL for the resource. This must be provided for all resources. The fullUrl SHALL not disagree with the id in the resource. The fullUrl is a version independent reference to the resource.", 0, java.lang.Integer.MAX_VALUE, fullUrl));
          childrenList.add(new Property("resource", "Resource", "The Resources for the entry.", 0, java.lang.Integer.MAX_VALUE, resource));
          childrenList.add(new Property("search", "", "Information about the search process that lead to the creation of this entry.", 0, java.lang.Integer.MAX_VALUE, search));
          childrenList.add(new Property("request", "", "Additional information about how this entry should be processed as part of a transaction.", 0, java.lang.Integer.MAX_VALUE, request));
          childrenList.add(new Property("response", "", "Additional information about how this entry should be processed as part of a transaction.", 0, java.lang.Integer.MAX_VALUE, response));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("link"))
          this.getLink().add((BundleLinkComponent) value);
        else if (name.equals("fullUrl"))
          this.fullUrl = castToUri(value); // UriType
        else if (name.equals("resource"))
          this.resource = castToResource(value); // Resource
        else if (name.equals("search"))
          this.search = (BundleEntrySearchComponent) value; // BundleEntrySearchComponent
        else if (name.equals("request"))
          this.request = (BundleEntryRequestComponent) value; // BundleEntryRequestComponent
        else if (name.equals("response"))
          this.response = (BundleEntryResponseComponent) value; // BundleEntryResponseComponent
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("link")) {
          return addLink();
        }
        else if (name.equals("fullUrl")) {
          throw new FHIRException("Cannot call addChild on a primitive type Bundle.fullUrl");
        }
        else if (name.equals("resource")) {
          throw new FHIRException("Cannot call addChild on an abstract type Bundle.resource");
        }
        else if (name.equals("search")) {
          this.search = new BundleEntrySearchComponent();
          return this.search;
        }
        else if (name.equals("request")) {
          this.request = new BundleEntryRequestComponent();
          return this.request;
        }
        else if (name.equals("response")) {
          this.response = new BundleEntryResponseComponent();
          return this.response;
        }
        else
          return super.addChild(name);
      }

      public BundleEntryComponent copy() {
        BundleEntryComponent dst = new BundleEntryComponent();
        copyValues(dst);
        if (link != null) {
          dst.link = new ArrayList<BundleLinkComponent>();
          for (BundleLinkComponent i : link)
            dst.link.add(i.copy());
        };
        dst.fullUrl = fullUrl == null ? null : fullUrl.copy();
        dst.resource = resource == null ? null : resource.copy();
        dst.search = search == null ? null : search.copy();
        dst.request = request == null ? null : request.copy();
        dst.response = response == null ? null : response.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof BundleEntryComponent))
          return false;
        BundleEntryComponent o = (BundleEntryComponent) other;
        return compareDeep(link, o.link, true) && compareDeep(fullUrl, o.fullUrl, true) && compareDeep(resource, o.resource, true)
           && compareDeep(search, o.search, true) && compareDeep(request, o.request, true) && compareDeep(response, o.response, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof BundleEntryComponent))
          return false;
        BundleEntryComponent o = (BundleEntryComponent) other;
        return compareValues(fullUrl, o.fullUrl, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (link == null || link.isEmpty()) && (fullUrl == null || fullUrl.isEmpty())
           && (resource == null || resource.isEmpty()) && (search == null || search.isEmpty()) && (request == null || request.isEmpty())
           && (response == null || response.isEmpty());
      }

  public String fhirType() {
    return "Bundle.entry";

  }

  }

    @Block()
    public static class BundleEntrySearchComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Why this entry is in the result set - whether it's included as a match or because of an _include requirement.
         */
        @Child(name = "mode", type = {CodeType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="match | include | outcome - why this is in the result set", formalDefinition="Why this entry is in the result set - whether it's included as a match or because of an _include requirement." )
        protected Enumeration<SearchEntryMode> mode;

        /**
         * When searching, the server's search ranking score for the entry.
         */
        @Child(name = "score", type = {DecimalType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Search ranking (between 0 and 1)", formalDefinition="When searching, the server's search ranking score for the entry." )
        protected DecimalType score;

        private static final long serialVersionUID = 837739866L;

    /**
     * Constructor
     */
      public BundleEntrySearchComponent() {
        super();
      }

        /**
         * @return {@link #mode} (Why this entry is in the result set - whether it's included as a match or because of an _include requirement.). This is the underlying object with id, value and extensions. The accessor "getMode" gives direct access to the value
         */
        public Enumeration<SearchEntryMode> getModeElement() { 
          if (this.mode == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BundleEntrySearchComponent.mode");
            else if (Configuration.doAutoCreate())
              this.mode = new Enumeration<SearchEntryMode>(new SearchEntryModeEnumFactory()); // bb
          return this.mode;
        }

        public boolean hasModeElement() { 
          return this.mode != null && !this.mode.isEmpty();
        }

        public boolean hasMode() { 
          return this.mode != null && !this.mode.isEmpty();
        }

        /**
         * @param value {@link #mode} (Why this entry is in the result set - whether it's included as a match or because of an _include requirement.). This is the underlying object with id, value and extensions. The accessor "getMode" gives direct access to the value
         */
        public BundleEntrySearchComponent setModeElement(Enumeration<SearchEntryMode> value) { 
          this.mode = value;
          return this;
        }

        /**
         * @return Why this entry is in the result set - whether it's included as a match or because of an _include requirement.
         */
        public SearchEntryMode getMode() { 
          return this.mode == null ? null : this.mode.getValue();
        }

        /**
         * @param value Why this entry is in the result set - whether it's included as a match or because of an _include requirement.
         */
        public BundleEntrySearchComponent setMode(SearchEntryMode value) { 
          if (value == null)
            this.mode = null;
          else {
            if (this.mode == null)
              this.mode = new Enumeration<SearchEntryMode>(new SearchEntryModeEnumFactory());
            this.mode.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #score} (When searching, the server's search ranking score for the entry.). This is the underlying object with id, value and extensions. The accessor "getScore" gives direct access to the value
         */
        public DecimalType getScoreElement() { 
          if (this.score == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BundleEntrySearchComponent.score");
            else if (Configuration.doAutoCreate())
              this.score = new DecimalType(); // bb
          return this.score;
        }

        public boolean hasScoreElement() { 
          return this.score != null && !this.score.isEmpty();
        }

        public boolean hasScore() { 
          return this.score != null && !this.score.isEmpty();
        }

        /**
         * @param value {@link #score} (When searching, the server's search ranking score for the entry.). This is the underlying object with id, value and extensions. The accessor "getScore" gives direct access to the value
         */
        public BundleEntrySearchComponent setScoreElement(DecimalType value) { 
          this.score = value;
          return this;
        }

        /**
         * @return When searching, the server's search ranking score for the entry.
         */
        public BigDecimal getScore() { 
          return this.score == null ? null : this.score.getValue();
        }

        /**
         * @param value When searching, the server's search ranking score for the entry.
         */
        public BundleEntrySearchComponent setScore(BigDecimal value) { 
          if (value == null)
            this.score = null;
          else {
            if (this.score == null)
              this.score = new DecimalType();
            this.score.setValue(value);
          }
          return this;
        }

        /**
         * @param value When searching, the server's search ranking score for the entry.
         */
        public BundleEntrySearchComponent setScore(long value) { 
              this.score = new DecimalType();
            this.score.setValue(value);
          return this;
        }

        /**
         * @param value When searching, the server's search ranking score for the entry.
         */
        public BundleEntrySearchComponent setScore(double value) { 
              this.score = new DecimalType();
            this.score.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("mode", "code", "Why this entry is in the result set - whether it's included as a match or because of an _include requirement.", 0, java.lang.Integer.MAX_VALUE, mode));
          childrenList.add(new Property("score", "decimal", "When searching, the server's search ranking score for the entry.", 0, java.lang.Integer.MAX_VALUE, score));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("mode"))
          this.mode = new SearchEntryModeEnumFactory().fromType(value); // Enumeration<SearchEntryMode>
        else if (name.equals("score"))
          this.score = castToDecimal(value); // DecimalType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("mode")) {
          throw new FHIRException("Cannot call addChild on a primitive type Bundle.mode");
        }
        else if (name.equals("score")) {
          throw new FHIRException("Cannot call addChild on a primitive type Bundle.score");
        }
        else
          return super.addChild(name);
      }

      public BundleEntrySearchComponent copy() {
        BundleEntrySearchComponent dst = new BundleEntrySearchComponent();
        copyValues(dst);
        dst.mode = mode == null ? null : mode.copy();
        dst.score = score == null ? null : score.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof BundleEntrySearchComponent))
          return false;
        BundleEntrySearchComponent o = (BundleEntrySearchComponent) other;
        return compareDeep(mode, o.mode, true) && compareDeep(score, o.score, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof BundleEntrySearchComponent))
          return false;
        BundleEntrySearchComponent o = (BundleEntrySearchComponent) other;
        return compareValues(mode, o.mode, true) && compareValues(score, o.score, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (mode == null || mode.isEmpty()) && (score == null || score.isEmpty())
          ;
      }

  public String fhirType() {
    return "Bundle.entry.search";

  }

  }

    @Block()
    public static class BundleEntryRequestComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The HTTP verb for this entry in either a update history, or a transaction/ transaction response.
         */
        @Child(name = "method", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="GET | POST | PUT | DELETE", formalDefinition="The HTTP verb for this entry in either a update history, or a transaction/ transaction response." )
        protected Enumeration<HTTPVerb> method;

        /**
         * The URL for this entry, relative to the root (the address to which the request is posted).
         */
        @Child(name = "url", type = {UriType.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="URL for HTTP equivalent of this entry", formalDefinition="The URL for this entry, relative to the root (the address to which the request is posted)." )
        protected UriType url;

        /**
         * If the ETag values match, return a 304 Not modified status. See the API documentation for ["Conditional Read"](http.html#cread).
         */
        @Child(name = "ifNoneMatch", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="For managing cache currency", formalDefinition="If the ETag values match, return a 304 Not modified status. See the API documentation for [\"Conditional Read\"](http.html#cread)." )
        protected StringType ifNoneMatch;

        /**
         * Only perform the operation if the last updated date matches. See the API documentation for ["Conditional Read"](http.html#cread).
         */
        @Child(name = "ifModifiedSince", type = {InstantType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="For managing update contention", formalDefinition="Only perform the operation if the last updated date matches. See the API documentation for [\"Conditional Read\"](http.html#cread)." )
        protected InstantType ifModifiedSince;

        /**
         * Only perform the operation if the Etag value matches. For more information, see the API section ["Managing Resource Contention"](http.html#concurrency).
         */
        @Child(name = "ifMatch", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="For managing update contention", formalDefinition="Only perform the operation if the Etag value matches. For more information, see the API section [\"Managing Resource Contention\"](http.html#concurrency)." )
        protected StringType ifMatch;

        /**
         * Instruct the server not to perform the create if a specified resource already exists. For further information, see the API documentation for ["Conditional Create"](http.html#ccreate). This is just the query portion of the URL - what follows the "?" (not including the "?").
         */
        @Child(name = "ifNoneExist", type = {StringType.class}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="For conditional creates", formalDefinition="Instruct the server not to perform the create if a specified resource already exists. For further information, see the API documentation for [\"Conditional Create\"](http.html#ccreate). This is just the query portion of the URL - what follows the \"?\" (not including the \"?\")." )
        protected StringType ifNoneExist;

        private static final long serialVersionUID = -1349769744L;

    /**
     * Constructor
     */
      public BundleEntryRequestComponent() {
        super();
      }

    /**
     * Constructor
     */
      public BundleEntryRequestComponent(Enumeration<HTTPVerb> method, UriType url) {
        super();
        this.method = method;
        this.url = url;
      }

        /**
         * @return {@link #method} (The HTTP verb for this entry in either a update history, or a transaction/ transaction response.). This is the underlying object with id, value and extensions. The accessor "getMethod" gives direct access to the value
         */
        public Enumeration<HTTPVerb> getMethodElement() { 
          if (this.method == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BundleEntryRequestComponent.method");
            else if (Configuration.doAutoCreate())
              this.method = new Enumeration<HTTPVerb>(new HTTPVerbEnumFactory()); // bb
          return this.method;
        }

        public boolean hasMethodElement() { 
          return this.method != null && !this.method.isEmpty();
        }

        public boolean hasMethod() { 
          return this.method != null && !this.method.isEmpty();
        }

        /**
         * @param value {@link #method} (The HTTP verb for this entry in either a update history, or a transaction/ transaction response.). This is the underlying object with id, value and extensions. The accessor "getMethod" gives direct access to the value
         */
        public BundleEntryRequestComponent setMethodElement(Enumeration<HTTPVerb> value) { 
          this.method = value;
          return this;
        }

        /**
         * @return The HTTP verb for this entry in either a update history, or a transaction/ transaction response.
         */
        public HTTPVerb getMethod() { 
          return this.method == null ? null : this.method.getValue();
        }

        /**
         * @param value The HTTP verb for this entry in either a update history, or a transaction/ transaction response.
         */
        public BundleEntryRequestComponent setMethod(HTTPVerb value) { 
            if (this.method == null)
              this.method = new Enumeration<HTTPVerb>(new HTTPVerbEnumFactory());
            this.method.setValue(value);
          return this;
        }

        /**
         * @return {@link #url} (The URL for this entry, relative to the root (the address to which the request is posted).). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
         */
        public UriType getUrlElement() { 
          if (this.url == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BundleEntryRequestComponent.url");
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
         * @param value {@link #url} (The URL for this entry, relative to the root (the address to which the request is posted).). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
         */
        public BundleEntryRequestComponent setUrlElement(UriType value) { 
          this.url = value;
          return this;
        }

        /**
         * @return The URL for this entry, relative to the root (the address to which the request is posted).
         */
        public String getUrl() { 
          return this.url == null ? null : this.url.getValue();
        }

        /**
         * @param value The URL for this entry, relative to the root (the address to which the request is posted).
         */
        public BundleEntryRequestComponent setUrl(String value) { 
            if (this.url == null)
              this.url = new UriType();
            this.url.setValue(value);
          return this;
        }

        /**
         * @return {@link #ifNoneMatch} (If the ETag values match, return a 304 Not modified status. See the API documentation for ["Conditional Read"](http.html#cread).). This is the underlying object with id, value and extensions. The accessor "getIfNoneMatch" gives direct access to the value
         */
        public StringType getIfNoneMatchElement() { 
          if (this.ifNoneMatch == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BundleEntryRequestComponent.ifNoneMatch");
            else if (Configuration.doAutoCreate())
              this.ifNoneMatch = new StringType(); // bb
          return this.ifNoneMatch;
        }

        public boolean hasIfNoneMatchElement() { 
          return this.ifNoneMatch != null && !this.ifNoneMatch.isEmpty();
        }

        public boolean hasIfNoneMatch() { 
          return this.ifNoneMatch != null && !this.ifNoneMatch.isEmpty();
        }

        /**
         * @param value {@link #ifNoneMatch} (If the ETag values match, return a 304 Not modified status. See the API documentation for ["Conditional Read"](http.html#cread).). This is the underlying object with id, value and extensions. The accessor "getIfNoneMatch" gives direct access to the value
         */
        public BundleEntryRequestComponent setIfNoneMatchElement(StringType value) { 
          this.ifNoneMatch = value;
          return this;
        }

        /**
         * @return If the ETag values match, return a 304 Not modified status. See the API documentation for ["Conditional Read"](http.html#cread).
         */
        public String getIfNoneMatch() { 
          return this.ifNoneMatch == null ? null : this.ifNoneMatch.getValue();
        }

        /**
         * @param value If the ETag values match, return a 304 Not modified status. See the API documentation for ["Conditional Read"](http.html#cread).
         */
        public BundleEntryRequestComponent setIfNoneMatch(String value) { 
          if (Utilities.noString(value))
            this.ifNoneMatch = null;
          else {
            if (this.ifNoneMatch == null)
              this.ifNoneMatch = new StringType();
            this.ifNoneMatch.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #ifModifiedSince} (Only perform the operation if the last updated date matches. See the API documentation for ["Conditional Read"](http.html#cread).). This is the underlying object with id, value and extensions. The accessor "getIfModifiedSince" gives direct access to the value
         */
        public InstantType getIfModifiedSinceElement() { 
          if (this.ifModifiedSince == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BundleEntryRequestComponent.ifModifiedSince");
            else if (Configuration.doAutoCreate())
              this.ifModifiedSince = new InstantType(); // bb
          return this.ifModifiedSince;
        }

        public boolean hasIfModifiedSinceElement() { 
          return this.ifModifiedSince != null && !this.ifModifiedSince.isEmpty();
        }

        public boolean hasIfModifiedSince() { 
          return this.ifModifiedSince != null && !this.ifModifiedSince.isEmpty();
        }

        /**
         * @param value {@link #ifModifiedSince} (Only perform the operation if the last updated date matches. See the API documentation for ["Conditional Read"](http.html#cread).). This is the underlying object with id, value and extensions. The accessor "getIfModifiedSince" gives direct access to the value
         */
        public BundleEntryRequestComponent setIfModifiedSinceElement(InstantType value) { 
          this.ifModifiedSince = value;
          return this;
        }

        /**
         * @return Only perform the operation if the last updated date matches. See the API documentation for ["Conditional Read"](http.html#cread).
         */
        public Date getIfModifiedSince() { 
          return this.ifModifiedSince == null ? null : this.ifModifiedSince.getValue();
        }

        /**
         * @param value Only perform the operation if the last updated date matches. See the API documentation for ["Conditional Read"](http.html#cread).
         */
        public BundleEntryRequestComponent setIfModifiedSince(Date value) { 
          if (value == null)
            this.ifModifiedSince = null;
          else {
            if (this.ifModifiedSince == null)
              this.ifModifiedSince = new InstantType();
            this.ifModifiedSince.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #ifMatch} (Only perform the operation if the Etag value matches. For more information, see the API section ["Managing Resource Contention"](http.html#concurrency).). This is the underlying object with id, value and extensions. The accessor "getIfMatch" gives direct access to the value
         */
        public StringType getIfMatchElement() { 
          if (this.ifMatch == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BundleEntryRequestComponent.ifMatch");
            else if (Configuration.doAutoCreate())
              this.ifMatch = new StringType(); // bb
          return this.ifMatch;
        }

        public boolean hasIfMatchElement() { 
          return this.ifMatch != null && !this.ifMatch.isEmpty();
        }

        public boolean hasIfMatch() { 
          return this.ifMatch != null && !this.ifMatch.isEmpty();
        }

        /**
         * @param value {@link #ifMatch} (Only perform the operation if the Etag value matches. For more information, see the API section ["Managing Resource Contention"](http.html#concurrency).). This is the underlying object with id, value and extensions. The accessor "getIfMatch" gives direct access to the value
         */
        public BundleEntryRequestComponent setIfMatchElement(StringType value) { 
          this.ifMatch = value;
          return this;
        }

        /**
         * @return Only perform the operation if the Etag value matches. For more information, see the API section ["Managing Resource Contention"](http.html#concurrency).
         */
        public String getIfMatch() { 
          return this.ifMatch == null ? null : this.ifMatch.getValue();
        }

        /**
         * @param value Only perform the operation if the Etag value matches. For more information, see the API section ["Managing Resource Contention"](http.html#concurrency).
         */
        public BundleEntryRequestComponent setIfMatch(String value) { 
          if (Utilities.noString(value))
            this.ifMatch = null;
          else {
            if (this.ifMatch == null)
              this.ifMatch = new StringType();
            this.ifMatch.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #ifNoneExist} (Instruct the server not to perform the create if a specified resource already exists. For further information, see the API documentation for ["Conditional Create"](http.html#ccreate). This is just the query portion of the URL - what follows the "?" (not including the "?").). This is the underlying object with id, value and extensions. The accessor "getIfNoneExist" gives direct access to the value
         */
        public StringType getIfNoneExistElement() { 
          if (this.ifNoneExist == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BundleEntryRequestComponent.ifNoneExist");
            else if (Configuration.doAutoCreate())
              this.ifNoneExist = new StringType(); // bb
          return this.ifNoneExist;
        }

        public boolean hasIfNoneExistElement() { 
          return this.ifNoneExist != null && !this.ifNoneExist.isEmpty();
        }

        public boolean hasIfNoneExist() { 
          return this.ifNoneExist != null && !this.ifNoneExist.isEmpty();
        }

        /**
         * @param value {@link #ifNoneExist} (Instruct the server not to perform the create if a specified resource already exists. For further information, see the API documentation for ["Conditional Create"](http.html#ccreate). This is just the query portion of the URL - what follows the "?" (not including the "?").). This is the underlying object with id, value and extensions. The accessor "getIfNoneExist" gives direct access to the value
         */
        public BundleEntryRequestComponent setIfNoneExistElement(StringType value) { 
          this.ifNoneExist = value;
          return this;
        }

        /**
         * @return Instruct the server not to perform the create if a specified resource already exists. For further information, see the API documentation for ["Conditional Create"](http.html#ccreate). This is just the query portion of the URL - what follows the "?" (not including the "?").
         */
        public String getIfNoneExist() { 
          return this.ifNoneExist == null ? null : this.ifNoneExist.getValue();
        }

        /**
         * @param value Instruct the server not to perform the create if a specified resource already exists. For further information, see the API documentation for ["Conditional Create"](http.html#ccreate). This is just the query portion of the URL - what follows the "?" (not including the "?").
         */
        public BundleEntryRequestComponent setIfNoneExist(String value) { 
          if (Utilities.noString(value))
            this.ifNoneExist = null;
          else {
            if (this.ifNoneExist == null)
              this.ifNoneExist = new StringType();
            this.ifNoneExist.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("method", "code", "The HTTP verb for this entry in either a update history, or a transaction/ transaction response.", 0, java.lang.Integer.MAX_VALUE, method));
          childrenList.add(new Property("url", "uri", "The URL for this entry, relative to the root (the address to which the request is posted).", 0, java.lang.Integer.MAX_VALUE, url));
          childrenList.add(new Property("ifNoneMatch", "string", "If the ETag values match, return a 304 Not modified status. See the API documentation for [\"Conditional Read\"](http.html#cread).", 0, java.lang.Integer.MAX_VALUE, ifNoneMatch));
          childrenList.add(new Property("ifModifiedSince", "instant", "Only perform the operation if the last updated date matches. See the API documentation for [\"Conditional Read\"](http.html#cread).", 0, java.lang.Integer.MAX_VALUE, ifModifiedSince));
          childrenList.add(new Property("ifMatch", "string", "Only perform the operation if the Etag value matches. For more information, see the API section [\"Managing Resource Contention\"](http.html#concurrency).", 0, java.lang.Integer.MAX_VALUE, ifMatch));
          childrenList.add(new Property("ifNoneExist", "string", "Instruct the server not to perform the create if a specified resource already exists. For further information, see the API documentation for [\"Conditional Create\"](http.html#ccreate). This is just the query portion of the URL - what follows the \"?\" (not including the \"?\").", 0, java.lang.Integer.MAX_VALUE, ifNoneExist));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("method"))
          this.method = new HTTPVerbEnumFactory().fromType(value); // Enumeration<HTTPVerb>
        else if (name.equals("url"))
          this.url = castToUri(value); // UriType
        else if (name.equals("ifNoneMatch"))
          this.ifNoneMatch = castToString(value); // StringType
        else if (name.equals("ifModifiedSince"))
          this.ifModifiedSince = castToInstant(value); // InstantType
        else if (name.equals("ifMatch"))
          this.ifMatch = castToString(value); // StringType
        else if (name.equals("ifNoneExist"))
          this.ifNoneExist = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("method")) {
          throw new FHIRException("Cannot call addChild on a primitive type Bundle.method");
        }
        else if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type Bundle.url");
        }
        else if (name.equals("ifNoneMatch")) {
          throw new FHIRException("Cannot call addChild on a primitive type Bundle.ifNoneMatch");
        }
        else if (name.equals("ifModifiedSince")) {
          throw new FHIRException("Cannot call addChild on a primitive type Bundle.ifModifiedSince");
        }
        else if (name.equals("ifMatch")) {
          throw new FHIRException("Cannot call addChild on a primitive type Bundle.ifMatch");
        }
        else if (name.equals("ifNoneExist")) {
          throw new FHIRException("Cannot call addChild on a primitive type Bundle.ifNoneExist");
        }
        else
          return super.addChild(name);
      }

      public BundleEntryRequestComponent copy() {
        BundleEntryRequestComponent dst = new BundleEntryRequestComponent();
        copyValues(dst);
        dst.method = method == null ? null : method.copy();
        dst.url = url == null ? null : url.copy();
        dst.ifNoneMatch = ifNoneMatch == null ? null : ifNoneMatch.copy();
        dst.ifModifiedSince = ifModifiedSince == null ? null : ifModifiedSince.copy();
        dst.ifMatch = ifMatch == null ? null : ifMatch.copy();
        dst.ifNoneExist = ifNoneExist == null ? null : ifNoneExist.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof BundleEntryRequestComponent))
          return false;
        BundleEntryRequestComponent o = (BundleEntryRequestComponent) other;
        return compareDeep(method, o.method, true) && compareDeep(url, o.url, true) && compareDeep(ifNoneMatch, o.ifNoneMatch, true)
           && compareDeep(ifModifiedSince, o.ifModifiedSince, true) && compareDeep(ifMatch, o.ifMatch, true)
           && compareDeep(ifNoneExist, o.ifNoneExist, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof BundleEntryRequestComponent))
          return false;
        BundleEntryRequestComponent o = (BundleEntryRequestComponent) other;
        return compareValues(method, o.method, true) && compareValues(url, o.url, true) && compareValues(ifNoneMatch, o.ifNoneMatch, true)
           && compareValues(ifModifiedSince, o.ifModifiedSince, true) && compareValues(ifMatch, o.ifMatch, true)
           && compareValues(ifNoneExist, o.ifNoneExist, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (method == null || method.isEmpty()) && (url == null || url.isEmpty())
           && (ifNoneMatch == null || ifNoneMatch.isEmpty()) && (ifModifiedSince == null || ifModifiedSince.isEmpty())
           && (ifMatch == null || ifMatch.isEmpty()) && (ifNoneExist == null || ifNoneExist.isEmpty())
          ;
      }

  public String fhirType() {
    return "Bundle.entry.request";

  }

  }

    @Block()
    public static class BundleEntryResponseComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The status code returned by processing this entry.
         */
        @Child(name = "status", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Status return code for entry", formalDefinition="The status code returned by processing this entry." )
        protected StringType status;

        /**
         * The location header created by processing this operation.
         */
        @Child(name = "location", type = {UriType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The location, if the operation returns a location", formalDefinition="The location header created by processing this operation." )
        protected UriType location;

        /**
         * The etag for the resource, it the operation for the entry produced a versioned resource.
         */
        @Child(name = "etag", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The etag for the resource (if relevant)", formalDefinition="The etag for the resource, it the operation for the entry produced a versioned resource." )
        protected StringType etag;

        /**
         * The date/time that the resource was modified on the server.
         */
        @Child(name = "lastModified", type = {InstantType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Server's date time modified", formalDefinition="The date/time that the resource was modified on the server." )
        protected InstantType lastModified;

        private static final long serialVersionUID = -1526413234L;

    /**
     * Constructor
     */
      public BundleEntryResponseComponent() {
        super();
      }

    /**
     * Constructor
     */
      public BundleEntryResponseComponent(StringType status) {
        super();
        this.status = status;
      }

        /**
         * @return {@link #status} (The status code returned by processing this entry.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
         */
        public StringType getStatusElement() { 
          if (this.status == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BundleEntryResponseComponent.status");
            else if (Configuration.doAutoCreate())
              this.status = new StringType(); // bb
          return this.status;
        }

        public boolean hasStatusElement() { 
          return this.status != null && !this.status.isEmpty();
        }

        public boolean hasStatus() { 
          return this.status != null && !this.status.isEmpty();
        }

        /**
         * @param value {@link #status} (The status code returned by processing this entry.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
         */
        public BundleEntryResponseComponent setStatusElement(StringType value) { 
          this.status = value;
          return this;
        }

        /**
         * @return The status code returned by processing this entry.
         */
        public String getStatus() { 
          return this.status == null ? null : this.status.getValue();
        }

        /**
         * @param value The status code returned by processing this entry.
         */
        public BundleEntryResponseComponent setStatus(String value) { 
            if (this.status == null)
              this.status = new StringType();
            this.status.setValue(value);
          return this;
        }

        /**
         * @return {@link #location} (The location header created by processing this operation.). This is the underlying object with id, value and extensions. The accessor "getLocation" gives direct access to the value
         */
        public UriType getLocationElement() { 
          if (this.location == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BundleEntryResponseComponent.location");
            else if (Configuration.doAutoCreate())
              this.location = new UriType(); // bb
          return this.location;
        }

        public boolean hasLocationElement() { 
          return this.location != null && !this.location.isEmpty();
        }

        public boolean hasLocation() { 
          return this.location != null && !this.location.isEmpty();
        }

        /**
         * @param value {@link #location} (The location header created by processing this operation.). This is the underlying object with id, value and extensions. The accessor "getLocation" gives direct access to the value
         */
        public BundleEntryResponseComponent setLocationElement(UriType value) { 
          this.location = value;
          return this;
        }

        /**
         * @return The location header created by processing this operation.
         */
        public String getLocation() { 
          return this.location == null ? null : this.location.getValue();
        }

        /**
         * @param value The location header created by processing this operation.
         */
        public BundleEntryResponseComponent setLocation(String value) { 
          if (Utilities.noString(value))
            this.location = null;
          else {
            if (this.location == null)
              this.location = new UriType();
            this.location.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #etag} (The etag for the resource, it the operation for the entry produced a versioned resource.). This is the underlying object with id, value and extensions. The accessor "getEtag" gives direct access to the value
         */
        public StringType getEtagElement() { 
          if (this.etag == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BundleEntryResponseComponent.etag");
            else if (Configuration.doAutoCreate())
              this.etag = new StringType(); // bb
          return this.etag;
        }

        public boolean hasEtagElement() { 
          return this.etag != null && !this.etag.isEmpty();
        }

        public boolean hasEtag() { 
          return this.etag != null && !this.etag.isEmpty();
        }

        /**
         * @param value {@link #etag} (The etag for the resource, it the operation for the entry produced a versioned resource.). This is the underlying object with id, value and extensions. The accessor "getEtag" gives direct access to the value
         */
        public BundleEntryResponseComponent setEtagElement(StringType value) { 
          this.etag = value;
          return this;
        }

        /**
         * @return The etag for the resource, it the operation for the entry produced a versioned resource.
         */
        public String getEtag() { 
          return this.etag == null ? null : this.etag.getValue();
        }

        /**
         * @param value The etag for the resource, it the operation for the entry produced a versioned resource.
         */
        public BundleEntryResponseComponent setEtag(String value) { 
          if (Utilities.noString(value))
            this.etag = null;
          else {
            if (this.etag == null)
              this.etag = new StringType();
            this.etag.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #lastModified} (The date/time that the resource was modified on the server.). This is the underlying object with id, value and extensions. The accessor "getLastModified" gives direct access to the value
         */
        public InstantType getLastModifiedElement() { 
          if (this.lastModified == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BundleEntryResponseComponent.lastModified");
            else if (Configuration.doAutoCreate())
              this.lastModified = new InstantType(); // bb
          return this.lastModified;
        }

        public boolean hasLastModifiedElement() { 
          return this.lastModified != null && !this.lastModified.isEmpty();
        }

        public boolean hasLastModified() { 
          return this.lastModified != null && !this.lastModified.isEmpty();
        }

        /**
         * @param value {@link #lastModified} (The date/time that the resource was modified on the server.). This is the underlying object with id, value and extensions. The accessor "getLastModified" gives direct access to the value
         */
        public BundleEntryResponseComponent setLastModifiedElement(InstantType value) { 
          this.lastModified = value;
          return this;
        }

        /**
         * @return The date/time that the resource was modified on the server.
         */
        public Date getLastModified() { 
          return this.lastModified == null ? null : this.lastModified.getValue();
        }

        /**
         * @param value The date/time that the resource was modified on the server.
         */
        public BundleEntryResponseComponent setLastModified(Date value) { 
          if (value == null)
            this.lastModified = null;
          else {
            if (this.lastModified == null)
              this.lastModified = new InstantType();
            this.lastModified.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("status", "string", "The status code returned by processing this entry.", 0, java.lang.Integer.MAX_VALUE, status));
          childrenList.add(new Property("location", "uri", "The location header created by processing this operation.", 0, java.lang.Integer.MAX_VALUE, location));
          childrenList.add(new Property("etag", "string", "The etag for the resource, it the operation for the entry produced a versioned resource.", 0, java.lang.Integer.MAX_VALUE, etag));
          childrenList.add(new Property("lastModified", "instant", "The date/time that the resource was modified on the server.", 0, java.lang.Integer.MAX_VALUE, lastModified));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("status"))
          this.status = castToString(value); // StringType
        else if (name.equals("location"))
          this.location = castToUri(value); // UriType
        else if (name.equals("etag"))
          this.etag = castToString(value); // StringType
        else if (name.equals("lastModified"))
          this.lastModified = castToInstant(value); // InstantType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type Bundle.status");
        }
        else if (name.equals("location")) {
          throw new FHIRException("Cannot call addChild on a primitive type Bundle.location");
        }
        else if (name.equals("etag")) {
          throw new FHIRException("Cannot call addChild on a primitive type Bundle.etag");
        }
        else if (name.equals("lastModified")) {
          throw new FHIRException("Cannot call addChild on a primitive type Bundle.lastModified");
        }
        else
          return super.addChild(name);
      }

      public BundleEntryResponseComponent copy() {
        BundleEntryResponseComponent dst = new BundleEntryResponseComponent();
        copyValues(dst);
        dst.status = status == null ? null : status.copy();
        dst.location = location == null ? null : location.copy();
        dst.etag = etag == null ? null : etag.copy();
        dst.lastModified = lastModified == null ? null : lastModified.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof BundleEntryResponseComponent))
          return false;
        BundleEntryResponseComponent o = (BundleEntryResponseComponent) other;
        return compareDeep(status, o.status, true) && compareDeep(location, o.location, true) && compareDeep(etag, o.etag, true)
           && compareDeep(lastModified, o.lastModified, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof BundleEntryResponseComponent))
          return false;
        BundleEntryResponseComponent o = (BundleEntryResponseComponent) other;
        return compareValues(status, o.status, true) && compareValues(location, o.location, true) && compareValues(etag, o.etag, true)
           && compareValues(lastModified, o.lastModified, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (status == null || status.isEmpty()) && (location == null || location.isEmpty())
           && (etag == null || etag.isEmpty()) && (lastModified == null || lastModified.isEmpty());
      }

  public String fhirType() {
    return "Bundle.entry.response";

  }

  }

    /**
     * Indicates the purpose of this bundle- how it was intended to be used.
     */
    @Child(name = "type", type = {CodeType.class}, order=0, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="document | message | transaction | transaction-response | batch | batch-response | history | searchset | collection", formalDefinition="Indicates the purpose of this bundle- how it was intended to be used." )
    protected Enumeration<BundleType> type;

    /**
     * If a set of search matches, this is the total number of matches for the search (as opposed to the number of results in this bundle).
     */
    @Child(name = "total", type = {UnsignedIntType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="If search, the total number of matches", formalDefinition="If a set of search matches, this is the total number of matches for the search (as opposed to the number of results in this bundle)." )
    protected UnsignedIntType total;

    /**
     * A series of links that provide context to this bundle.
     */
    @Child(name = "link", type = {}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Links related to this Bundle", formalDefinition="A series of links that provide context to this bundle." )
    protected List<BundleLinkComponent> link;

    /**
     * An entry in a bundle resource - will either contain a resource, or information about a resource (transactions and history only).
     */
    @Child(name = "entry", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Entry in the bundle - will have a resource, or information", formalDefinition="An entry in a bundle resource - will either contain a resource, or information about a resource (transactions and history only)." )
    protected List<BundleEntryComponent> entry;

    /**
     * Digital Signature - base64 encoded. XML DigSIg or a JWT.
     */
    @Child(name = "signature", type = {Signature.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Digital Signature", formalDefinition="Digital Signature - base64 encoded. XML DigSIg or a JWT." )
    protected Signature signature;

    private static final long serialVersionUID = -2041954721L;

  /**
   * Constructor
   */
    public Bundle() {
      super();
    }

  /**
   * Constructor
   */
    public Bundle(Enumeration<BundleType> type) {
      super();
      this.type = type;
    }

    /**
     * @return {@link #type} (Indicates the purpose of this bundle- how it was intended to be used.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public Enumeration<BundleType> getTypeElement() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Bundle.type");
        else if (Configuration.doAutoCreate())
          this.type = new Enumeration<BundleType>(new BundleTypeEnumFactory()); // bb
      return this.type;
    }

    public boolean hasTypeElement() { 
      return this.type != null && !this.type.isEmpty();
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (Indicates the purpose of this bundle- how it was intended to be used.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public Bundle setTypeElement(Enumeration<BundleType> value) { 
      this.type = value;
      return this;
    }

    /**
     * @return Indicates the purpose of this bundle- how it was intended to be used.
     */
    public BundleType getType() { 
      return this.type == null ? null : this.type.getValue();
    }

    /**
     * @param value Indicates the purpose of this bundle- how it was intended to be used.
     */
    public Bundle setType(BundleType value) { 
        if (this.type == null)
          this.type = new Enumeration<BundleType>(new BundleTypeEnumFactory());
        this.type.setValue(value);
      return this;
    }

    /**
     * @return {@link #total} (If a set of search matches, this is the total number of matches for the search (as opposed to the number of results in this bundle).). This is the underlying object with id, value and extensions. The accessor "getTotal" gives direct access to the value
     */
    public UnsignedIntType getTotalElement() { 
      if (this.total == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Bundle.total");
        else if (Configuration.doAutoCreate())
          this.total = new UnsignedIntType(); // bb
      return this.total;
    }

    public boolean hasTotalElement() { 
      return this.total != null && !this.total.isEmpty();
    }

    public boolean hasTotal() { 
      return this.total != null && !this.total.isEmpty();
    }

    /**
     * @param value {@link #total} (If a set of search matches, this is the total number of matches for the search (as opposed to the number of results in this bundle).). This is the underlying object with id, value and extensions. The accessor "getTotal" gives direct access to the value
     */
    public Bundle setTotalElement(UnsignedIntType value) { 
      this.total = value;
      return this;
    }

    /**
     * @return If a set of search matches, this is the total number of matches for the search (as opposed to the number of results in this bundle).
     */
    public int getTotal() { 
      return this.total == null || this.total.isEmpty() ? 0 : this.total.getValue();
    }

    /**
     * @param value If a set of search matches, this is the total number of matches for the search (as opposed to the number of results in this bundle).
     */
    public Bundle setTotal(int value) { 
        if (this.total == null)
          this.total = new UnsignedIntType();
        this.total.setValue(value);
      return this;
    }

    /**
     * @return {@link #link} (A series of links that provide context to this bundle.)
     */
    public List<BundleLinkComponent> getLink() { 
      if (this.link == null)
        this.link = new ArrayList<BundleLinkComponent>();
      return this.link;
    }

    public boolean hasLink() { 
      if (this.link == null)
        return false;
      for (BundleLinkComponent item : this.link)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #link} (A series of links that provide context to this bundle.)
     */
    // syntactic sugar
    public BundleLinkComponent addLink() { //3
      BundleLinkComponent t = new BundleLinkComponent();
      if (this.link == null)
        this.link = new ArrayList<BundleLinkComponent>();
      this.link.add(t);
      return t;
    }

    // syntactic sugar
    public Bundle addLink(BundleLinkComponent t) { //3
      if (t == null)
        return this;
      if (this.link == null)
        this.link = new ArrayList<BundleLinkComponent>();
      this.link.add(t);
      return this;
    }

    /**
     * @return {@link #entry} (An entry in a bundle resource - will either contain a resource, or information about a resource (transactions and history only).)
     */
    public List<BundleEntryComponent> getEntry() { 
      if (this.entry == null)
        this.entry = new ArrayList<BundleEntryComponent>();
      return this.entry;
    }

    public boolean hasEntry() { 
      if (this.entry == null)
        return false;
      for (BundleEntryComponent item : this.entry)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #entry} (An entry in a bundle resource - will either contain a resource, or information about a resource (transactions and history only).)
     */
    // syntactic sugar
    public BundleEntryComponent addEntry() { //3
      BundleEntryComponent t = new BundleEntryComponent();
      if (this.entry == null)
        this.entry = new ArrayList<BundleEntryComponent>();
      this.entry.add(t);
      return t;
    }

    // syntactic sugar
    public Bundle addEntry(BundleEntryComponent t) { //3
      if (t == null)
        return this;
      if (this.entry == null)
        this.entry = new ArrayList<BundleEntryComponent>();
      this.entry.add(t);
      return this;
    }

    /**
     * @return {@link #signature} (Digital Signature - base64 encoded. XML DigSIg or a JWT.)
     */
    public Signature getSignature() { 
      if (this.signature == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Bundle.signature");
        else if (Configuration.doAutoCreate())
          this.signature = new Signature(); // cc
      return this.signature;
    }

    public boolean hasSignature() { 
      return this.signature != null && !this.signature.isEmpty();
    }

    /**
     * @param value {@link #signature} (Digital Signature - base64 encoded. XML DigSIg or a JWT.)
     */
    public Bundle setSignature(Signature value) { 
      this.signature = value;
      return this;
    }

 /**
   * Returns the {@link #getLink() link} which matches a given {@link BundleLinkComponent#getRelation() relation}. 
   * If no link is found which matches the given relation, returns <code>null</code>. If more than one
   * link is found which matches the given relation, returns the first matching BundleLinkComponent.
   * 
   * @param theRelation
   *            The relation, such as "next", or "self. See the constants such as {@link IBaseBundle#LINK_SELF} and {@link IBaseBundle#LINK_NEXT}.
   * @return Returns a matching BundleLinkComponent, or <code>null</code>
   * @see IBaseBundle#LINK_NEXT
   * @see IBaseBundle#LINK_PREV
   * @see IBaseBundle#LINK_SELF
   */
  public BundleLinkComponent getLink(String theRelation) {
    org.apache.commons.lang3.Validate.notBlank(theRelation, "theRelation may not be null or empty");
    for (BundleLinkComponent next : getLink()) {
      if (theRelation.equals(next.getRelation())) {
        return next;
      }
    }
    return null;
  }

  /**
   * Returns the {@link #getLink() link} which matches a given {@link BundleLinkComponent#getRelation() relation}. 
   * If no link is found which matches the given relation, creates a new BundleLinkComponent with the
   * given relation and adds it to this Bundle. If more than one
   * link is found which matches the given relation, returns the first matching BundleLinkComponent.
   * 
   * @param theRelation
   *            The relation, such as "next", or "self. See the constants such as {@link IBaseBundle#LINK_SELF} and {@link IBaseBundle#LINK_NEXT}.
   * @return Returns a matching BundleLinkComponent, or <code>null</code>
   * @see IBaseBundle#LINK_NEXT
   * @see IBaseBundle#LINK_PREV
   * @see IBaseBundle#LINK_SELF
   */
  public BundleLinkComponent getLinkOrCreate(String theRelation) {
    org.apache.commons.lang3.Validate.notBlank(theRelation, "theRelation may not be null or empty");
    for (BundleLinkComponent next : getLink()) {
      if (theRelation.equals(next.getRelation())) {
        return next;
      }
    }
    BundleLinkComponent retVal = new BundleLinkComponent();
    retVal.setRelation(theRelation);
    getLink().add(retVal);
    return retVal;
  }
      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("type", "code", "Indicates the purpose of this bundle- how it was intended to be used.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("total", "unsignedInt", "If a set of search matches, this is the total number of matches for the search (as opposed to the number of results in this bundle).", 0, java.lang.Integer.MAX_VALUE, total));
        childrenList.add(new Property("link", "", "A series of links that provide context to this bundle.", 0, java.lang.Integer.MAX_VALUE, link));
        childrenList.add(new Property("entry", "", "An entry in a bundle resource - will either contain a resource, or information about a resource (transactions and history only).", 0, java.lang.Integer.MAX_VALUE, entry));
        childrenList.add(new Property("signature", "Signature", "Digital Signature - base64 encoded. XML DigSIg or a JWT.", 0, java.lang.Integer.MAX_VALUE, signature));
      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type"))
          this.type = new BundleTypeEnumFactory().fromType(value); // Enumeration<BundleType>
        else if (name.equals("total"))
          this.total = castToUnsignedInt(value); // UnsignedIntType
        else if (name.equals("link"))
          this.getLink().add((BundleLinkComponent) value);
        else if (name.equals("entry"))
          this.getEntry().add((BundleEntryComponent) value);
        else if (name.equals("signature"))
          this.signature = castToSignature(value); // Signature
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type Bundle.type");
        }
        else if (name.equals("total")) {
          throw new FHIRException("Cannot call addChild on a primitive type Bundle.total");
        }
        else if (name.equals("link")) {
          return addLink();
        }
        else if (name.equals("entry")) {
          return addEntry();
        }
        else if (name.equals("signature")) {
          this.signature = new Signature();
          return this.signature;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Bundle";

  }

      public Bundle copy() {
        Bundle dst = new Bundle();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.total = total == null ? null : total.copy();
        if (link != null) {
          dst.link = new ArrayList<BundleLinkComponent>();
          for (BundleLinkComponent i : link)
            dst.link.add(i.copy());
        };
        if (entry != null) {
          dst.entry = new ArrayList<BundleEntryComponent>();
          for (BundleEntryComponent i : entry)
            dst.entry.add(i.copy());
        };
        dst.signature = signature == null ? null : signature.copy();
        return dst;
      }

      protected Bundle typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Bundle))
          return false;
        Bundle o = (Bundle) other;
        return compareDeep(type, o.type, true) && compareDeep(total, o.total, true) && compareDeep(link, o.link, true)
           && compareDeep(entry, o.entry, true) && compareDeep(signature, o.signature, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Bundle))
          return false;
        Bundle o = (Bundle) other;
        return compareValues(type, o.type, true) && compareValues(total, o.total, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (total == null || total.isEmpty())
           && (link == null || link.isEmpty()) && (entry == null || entry.isEmpty()) && (signature == null || signature.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Bundle;
   }

 /**
   * Search parameter: <b>composition</b>
   * <p>
   * Description: <b>The first resource in the bundle, if the bundle type is "document" - this is a composition, and this parameter provides access to searches its contents</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Bundle.entry.resource(0)</b><br>
   * </p>
   */
  @SearchParamDefinition(name="composition", path="Bundle.entry.resource.item(0)", description="The first resource in the bundle, if the bundle type is \"document\" - this is a composition, and this parameter provides access to searches its contents", type="reference" )
  public static final String SP_COMPOSITION = "composition";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>composition</b>
   * <p>
   * Description: <b>The first resource in the bundle, if the bundle type is "document" - this is a composition, and this parameter provides access to searches its contents</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Bundle.entry.resource(0)</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam COMPOSITION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_COMPOSITION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Bundle:composition</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_COMPOSITION = new ca.uhn.fhir.model.api.Include("Bundle:composition").toLocked();

 /**
   * Search parameter: <b>type</b>
   * <p>
   * Description: <b>document | message | transaction | transaction-response | batch | batch-response | history | searchset | collection</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Bundle.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="type", path="Bundle.type", description="document | message | transaction | transaction-response | batch | batch-response | history | searchset | collection", type="token" )
  public static final String SP_TYPE = "type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>type</b>
   * <p>
   * Description: <b>document | message | transaction | transaction-response | batch | batch-response | history | searchset | collection</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Bundle.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TYPE);

 /**
   * Search parameter: <b>message</b>
   * <p>
   * Description: <b>The first resource in the bundle, if the bundle type is "message" - this is a message header, and this parameter provides access to search its contents</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Bundle.entry.resource(0)</b><br>
   * </p>
   */
  @SearchParamDefinition(name="message", path="Bundle.entry.resource.item(0)", description="The first resource in the bundle, if the bundle type is \"message\" - this is a message header, and this parameter provides access to search its contents", type="reference" )
  public static final String SP_MESSAGE = "message";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>message</b>
   * <p>
   * Description: <b>The first resource in the bundle, if the bundle type is "message" - this is a message header, and this parameter provides access to search its contents</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Bundle.entry.resource(0)</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam MESSAGE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_MESSAGE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Bundle:message</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_MESSAGE = new ca.uhn.fhir.model.api.Include("Bundle:message").toLocked();


}

