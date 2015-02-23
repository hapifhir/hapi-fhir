package org.hl7.fhir.instance.model;

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

// Generated on Wed, Feb 18, 2015 12:09-0500 for FHIR v0.4.0

import java.util.*;
import java.math.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
import org.hl7.fhir.instance.model.api.IBaseBundle;
/**
 * A container for a group of resources.
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
         * The bundle is a transaction response.
         */
        TRANSACTIONRESPONSE, 
        /**
         * The bundle is a list of resources from a _history interaction on a server.
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
        public static BundleType fromCode(String codeString) throws Exception {
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
        if ("history".equals(codeString))
          return HISTORY;
        if ("searchset".equals(codeString))
          return SEARCHSET;
        if ("collection".equals(codeString))
          return COLLECTION;
        throw new Exception("Unknown BundleType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DOCUMENT: return "document";
            case MESSAGE: return "message";
            case TRANSACTION: return "transaction";
            case TRANSACTIONRESPONSE: return "transaction-response";
            case HISTORY: return "history";
            case SEARCHSET: return "searchset";
            case COLLECTION: return "collection";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case DOCUMENT: return "";
            case MESSAGE: return "";
            case TRANSACTION: return "";
            case TRANSACTIONRESPONSE: return "";
            case HISTORY: return "";
            case SEARCHSET: return "";
            case COLLECTION: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case DOCUMENT: return "The bundle is a document. The first resource is a Composition.";
            case MESSAGE: return "The bundle is a message. The first resource is a MessageHeader.";
            case TRANSACTION: return "The bundle is a transaction - intended to be processed by a server as an atomic commit.";
            case TRANSACTIONRESPONSE: return "The bundle is a transaction response.";
            case HISTORY: return "The bundle is a list of resources from a _history interaction on a server.";
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
        if ("history".equals(codeString))
          return BundleType.HISTORY;
        if ("searchset".equals(codeString))
          return BundleType.SEARCHSET;
        if ("collection".equals(codeString))
          return BundleType.COLLECTION;
        throw new IllegalArgumentException("Unknown BundleType code '"+codeString+"'");
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
      if (code == BundleType.HISTORY)
        return "history";
      if (code == BundleType.SEARCHSET)
        return "searchset";
      if (code == BundleType.COLLECTION)
        return "collection";
      return "?";
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
         * added to help the parsers
         */
        NULL;
        public static SearchEntryMode fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("match".equals(codeString))
          return MATCH;
        if ("include".equals(codeString))
          return INCLUDE;
        throw new Exception("Unknown SearchEntryMode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case MATCH: return "match";
            case INCLUDE: return "include";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case MATCH: return "";
            case INCLUDE: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case MATCH: return "This resource matched the search specification.";
            case INCLUDE: return "This resource is returned because it is referred to from another resource in the search set.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case MATCH: return "match";
            case INCLUDE: return "include";
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
        throw new IllegalArgumentException("Unknown SearchEntryMode code '"+codeString+"'");
        }
    public String toCode(SearchEntryMode code) {
      if (code == SearchEntryMode.MATCH)
        return "match";
      if (code == SearchEntryMode.INCLUDE)
        return "include";
      return "?";
      }
    }

    public enum HttpVerb {
        /**
         * HTTP GET.
         */
        GET, 
        /**
         * HTTP POST.
         */
        POST, 
        /**
         * HTTP PUT.
         */
        PUT, 
        /**
         * HTTP DELETE.
         */
        DELETE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static HttpVerb fromCode(String codeString) throws Exception {
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
        throw new Exception("Unknown HttpVerb code '"+codeString+"'");
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
            case GET: return "";
            case POST: return "";
            case PUT: return "";
            case DELETE: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case GET: return "HTTP GET.";
            case POST: return "HTTP POST.";
            case PUT: return "HTTP PUT.";
            case DELETE: return "HTTP DELETE.";
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

  public static class HttpVerbEnumFactory implements EnumFactory<HttpVerb> {
    public HttpVerb fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("GET".equals(codeString))
          return HttpVerb.GET;
        if ("POST".equals(codeString))
          return HttpVerb.POST;
        if ("PUT".equals(codeString))
          return HttpVerb.PUT;
        if ("DELETE".equals(codeString))
          return HttpVerb.DELETE;
        throw new IllegalArgumentException("Unknown HttpVerb code '"+codeString+"'");
        }
    public String toCode(HttpVerb code) {
      if (code == HttpVerb.GET)
        return "GET";
      if (code == HttpVerb.POST)
        return "POST";
      if (code == HttpVerb.PUT)
        return "PUT";
      if (code == HttpVerb.DELETE)
        return "DELETE";
      return "?";
      }
    }

    @Block()
    public static class BundleLinkComponent extends BackboneElement {
        /**
         * A name which details the functional use for this link - see [[http://www.iana.org/assignments/link-relations/link-relations.xhtml]].
         */
        @Child(name="relation", type={StringType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="http://www.iana.org/assignments/link-relations/link-relations.xhtml", formalDefinition="A name which details the functional use for this link - see [[http://www.iana.org/assignments/link-relations/link-relations.xhtml]]." )
        protected StringType relation;

        /**
         * The reference details for the link.
         */
        @Child(name="url", type={UriType.class}, order=2, min=1, max=1)
        @Description(shortDefinition="Reference details for the link", formalDefinition="The reference details for the link." )
        protected UriType url;

        private static final long serialVersionUID = -1010386066L;

      public BundleLinkComponent() {
        super();
      }

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

  }

    @Block()
    public static class BundleEntryComponent extends BackboneElement {
        /**
         * The Base URL for the resource, if different to the base URL specified for the bundle as a whole.
         */
        @Child(name="base", type={UriType.class}, order=1, min=0, max=1)
        @Description(shortDefinition="Base URL, if different to bundle base", formalDefinition="The Base URL for the resource, if different to the base URL specified for the bundle as a whole." )
        protected UriType base;

        /**
         * The Resources for the entry.
         */
        @Child(name="resource", type={Resource.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Resources in this bundle", formalDefinition="The Resources for the entry." )
        protected Resource resource;

        /**
         * Information about the search process that lead to the creation of this entry.
         */
        @Child(name="search", type={}, order=3, min=0, max=1)
        @Description(shortDefinition="Search related information", formalDefinition="Information about the search process that lead to the creation of this entry." )
        protected BundleEntrySearchComponent search;

        /**
         * Additional information about how this entry should be processed as part of a transaction.
         */
        @Child(name="transaction", type={}, order=4, min=0, max=1)
        @Description(shortDefinition="Transaction Related Information", formalDefinition="Additional information about how this entry should be processed as part of a transaction." )
        protected BundleEntryTransactionComponent transaction;

        /**
         * Additional information about how this entry should be processed as part of a transaction.
         */
        @Child(name="transactionResponse", type={}, order=5, min=0, max=1)
        @Description(shortDefinition="Transaction Related Information", formalDefinition="Additional information about how this entry should be processed as part of a transaction." )
        protected BundleEntryTransactionResponseComponent transactionResponse;

        private static final long serialVersionUID = 2068509254L;

      public BundleEntryComponent() {
        super();
      }

        /**
         * @return {@link #base} (The Base URL for the resource, if different to the base URL specified for the bundle as a whole.). This is the underlying object with id, value and extensions. The accessor "getBase" gives direct access to the value
         */
        public UriType getBaseElement() { 
          if (this.base == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BundleEntryComponent.base");
            else if (Configuration.doAutoCreate())
              this.base = new UriType(); // bb
          return this.base;
        }

        public boolean hasBaseElement() { 
          return this.base != null && !this.base.isEmpty();
        }

        public boolean hasBase() { 
          return this.base != null && !this.base.isEmpty();
        }

        /**
         * @param value {@link #base} (The Base URL for the resource, if different to the base URL specified for the bundle as a whole.). This is the underlying object with id, value and extensions. The accessor "getBase" gives direct access to the value
         */
        public BundleEntryComponent setBaseElement(UriType value) { 
          this.base = value;
          return this;
        }

        /**
         * @return The Base URL for the resource, if different to the base URL specified for the bundle as a whole.
         */
        public String getBase() { 
          return this.base == null ? null : this.base.getValue();
        }

        /**
         * @param value The Base URL for the resource, if different to the base URL specified for the bundle as a whole.
         */
        public BundleEntryComponent setBase(String value) { 
          if (Utilities.noString(value))
            this.base = null;
          else {
            if (this.base == null)
              this.base = new UriType();
            this.base.setValue(value);
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
         * @return {@link #transaction} (Additional information about how this entry should be processed as part of a transaction.)
         */
        public BundleEntryTransactionComponent getTransaction() { 
          if (this.transaction == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BundleEntryComponent.transaction");
            else if (Configuration.doAutoCreate())
              this.transaction = new BundleEntryTransactionComponent(); // cc
          return this.transaction;
        }

        public boolean hasTransaction() { 
          return this.transaction != null && !this.transaction.isEmpty();
        }

        /**
         * @param value {@link #transaction} (Additional information about how this entry should be processed as part of a transaction.)
         */
        public BundleEntryComponent setTransaction(BundleEntryTransactionComponent value) { 
          this.transaction = value;
          return this;
        }

        /**
         * @return {@link #transactionResponse} (Additional information about how this entry should be processed as part of a transaction.)
         */
        public BundleEntryTransactionResponseComponent getTransactionResponse() { 
          if (this.transactionResponse == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BundleEntryComponent.transactionResponse");
            else if (Configuration.doAutoCreate())
              this.transactionResponse = new BundleEntryTransactionResponseComponent(); // cc
          return this.transactionResponse;
        }

        public boolean hasTransactionResponse() { 
          return this.transactionResponse != null && !this.transactionResponse.isEmpty();
        }

        /**
         * @param value {@link #transactionResponse} (Additional information about how this entry should be processed as part of a transaction.)
         */
        public BundleEntryComponent setTransactionResponse(BundleEntryTransactionResponseComponent value) { 
          this.transactionResponse = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("base", "uri", "The Base URL for the resource, if different to the base URL specified for the bundle as a whole.", 0, java.lang.Integer.MAX_VALUE, base));
          childrenList.add(new Property("resource", "Resource", "The Resources for the entry.", 0, java.lang.Integer.MAX_VALUE, resource));
          childrenList.add(new Property("search", "", "Information about the search process that lead to the creation of this entry.", 0, java.lang.Integer.MAX_VALUE, search));
          childrenList.add(new Property("transaction", "", "Additional information about how this entry should be processed as part of a transaction.", 0, java.lang.Integer.MAX_VALUE, transaction));
          childrenList.add(new Property("transactionResponse", "", "Additional information about how this entry should be processed as part of a transaction.", 0, java.lang.Integer.MAX_VALUE, transactionResponse));
        }

      public BundleEntryComponent copy() {
        BundleEntryComponent dst = new BundleEntryComponent();
        copyValues(dst);
        dst.base = base == null ? null : base.copy();
        dst.resource = resource == null ? null : resource.copy();
        dst.search = search == null ? null : search.copy();
        dst.transaction = transaction == null ? null : transaction.copy();
        dst.transactionResponse = transactionResponse == null ? null : transactionResponse.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof BundleEntryComponent))
          return false;
        BundleEntryComponent o = (BundleEntryComponent) other;
        return compareDeep(base, o.base, true) && compareDeep(resource, o.resource, true) && compareDeep(search, o.search, true)
           && compareDeep(transaction, o.transaction, true) && compareDeep(transactionResponse, o.transactionResponse, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof BundleEntryComponent))
          return false;
        BundleEntryComponent o = (BundleEntryComponent) other;
        return compareValues(base, o.base, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (base == null || base.isEmpty()) && (resource == null || resource.isEmpty())
           && (search == null || search.isEmpty()) && (transaction == null || transaction.isEmpty())
           && (transactionResponse == null || transactionResponse.isEmpty());
      }

  }

    @Block()
    public static class BundleEntrySearchComponent extends BackboneElement {
        /**
         * Why this entry is in the result set - whether it's included as a match or because of an _include requirement.
         */
        @Child(name="mode", type={CodeType.class}, order=1, min=0, max=1)
        @Description(shortDefinition="match | include - why this is in the result set", formalDefinition="Why this entry is in the result set - whether it's included as a match or because of an _include requirement." )
        protected Enumeration<SearchEntryMode> mode;

        /**
         * When searching, the server's search ranking score for the entry.
         */
        @Child(name="score", type={DecimalType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Search ranking (between 0 and 1)", formalDefinition="When searching, the server's search ranking score for the entry." )
        protected DecimalType score;

        private static final long serialVersionUID = 837739866L;

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

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("mode", "code", "Why this entry is in the result set - whether it's included as a match or because of an _include requirement.", 0, java.lang.Integer.MAX_VALUE, mode));
          childrenList.add(new Property("score", "decimal", "When searching, the server's search ranking score for the entry.", 0, java.lang.Integer.MAX_VALUE, score));
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

  }

    @Block()
    public static class BundleEntryTransactionComponent extends BackboneElement {
        /**
         * The HTTP verb for this entry in either a update history, or a transaction/ transaction response.
         */
        @Child(name="method", type={CodeType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="GET | POST | PUT | DELETE", formalDefinition="The HTTP verb for this entry in either a update history, or a transaction/ transaction response." )
        protected Enumeration<HttpVerb> method;

        /**
         * A search URL for this resource that specifies how the resource is matched to an existing resource when processing a transaction (see transaction documentation).
         */
        @Child(name="url", type={UriType.class}, order=2, min=1, max=1)
        @Description(shortDefinition="The URL for the transaction", formalDefinition="A search URL for this resource that specifies how the resource is matched to an existing resource when processing a transaction (see transaction documentation)." )
        protected UriType url;

        /**
         * If the ETag values match, return a 304 Not modified status. See the read/vread interaction documentation.
         */
        @Child(name="ifNoneMatch", type={StringType.class}, order=3, min=0, max=1)
        @Description(shortDefinition="For managing cache currency", formalDefinition="If the ETag values match, return a 304 Not modified status. See the read/vread interaction documentation." )
        protected StringType ifNoneMatch;

        /**
         * Only perform the operation if the Etag value matches. For more information, see the API section "Managing Resource Contention".
         */
        @Child(name="ifMatch", type={StringType.class}, order=4, min=0, max=1)
        @Description(shortDefinition="For managing update contention", formalDefinition="Only perform the operation if the Etag value matches. For more information, see the API section 'Managing Resource Contention'." )
        protected StringType ifMatch;

        /**
         * Only perform the operation if the last updated date matches. For more information, see the API section "Managing Resource Contention".
         */
        @Child(name = "ifModifiedSince", type = {InstantType.class}, order = 5, min = 0, max = 1)
        @Description(shortDefinition="For managing update contention", formalDefinition="Only perform the operation if the last updated date matches. For more information, see the API section 'Managing Resource Contention'." )
        protected InstantType ifModifiedSince;

        /**
         * Instruct the server not to perform the create if a specified resource already exists. For further information, see "Conditional Create".
         */
        @Child(name="ifNoneExist", type={StringType.class}, order=6, min=0, max=1)
        @Description(shortDefinition="For conditional creates", formalDefinition="Instruct the server not to perform the create if a specified resource already exists. For further information, see 'Conditional Create'." )
        protected StringType ifNoneExist;

        private static final long serialVersionUID = -769185862L;

      public BundleEntryTransactionComponent() {
        super();
      }

      public BundleEntryTransactionComponent(Enumeration<HttpVerb> method, UriType url) {
        super();
        this.method = method;
        this.url = url;
      }

        /**
         * @return {@link #method} (The HTTP verb for this entry in either a update history, or a transaction/ transaction response.). This is the underlying object with id, value and extensions. The accessor "getMethod" gives direct access to the value
         */
        public Enumeration<HttpVerb> getMethodElement() { 
          if (this.method == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BundleEntryTransactionComponent.method");
            else if (Configuration.doAutoCreate())
              this.method = new Enumeration<HttpVerb>(new HttpVerbEnumFactory()); // bb
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
        public BundleEntryTransactionComponent setMethodElement(Enumeration<HttpVerb> value) { 
          this.method = value;
          return this;
        }

        /**
         * @return The HTTP verb for this entry in either a update history, or a transaction/ transaction response.
         */
        public HttpVerb getMethod() { 
          return this.method == null ? null : this.method.getValue();
        }

        /**
         * @param value The HTTP verb for this entry in either a update history, or a transaction/ transaction response.
         */
        public BundleEntryTransactionComponent setMethod(HttpVerb value) { 
            if (this.method == null)
              this.method = new Enumeration<HttpVerb>(new HttpVerbEnumFactory());
            this.method.setValue(value);
          return this;
        }

        /**
         * @return {@link #url} (A search URL for this resource that specifies how the resource is matched to an existing resource when processing a transaction (see transaction documentation).). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
         */
        public UriType getUrlElement() { 
          if (this.url == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BundleEntryTransactionComponent.url");
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
         * @param value {@link #url} (A search URL for this resource that specifies how the resource is matched to an existing resource when processing a transaction (see transaction documentation).). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
         */
        public BundleEntryTransactionComponent setUrlElement(UriType value) { 
          this.url = value;
          return this;
        }

        /**
         * @return A search URL for this resource that specifies how the resource is matched to an existing resource when processing a transaction (see transaction documentation).
         */
        public String getUrl() { 
          return this.url == null ? null : this.url.getValue();
        }

        /**
         * @param value A search URL for this resource that specifies how the resource is matched to an existing resource when processing a transaction (see transaction documentation).
         */
        public BundleEntryTransactionComponent setUrl(String value) { 
            if (this.url == null)
              this.url = new UriType();
            this.url.setValue(value);
          return this;
        }

        /**
         * @return {@link #ifNoneMatch} (If the ETag values match, return a 304 Not modified status. See the read/vread interaction documentation.). This is the underlying object with id, value and extensions. The accessor "getIfNoneMatch" gives direct access to the value
         */
        public StringType getIfNoneMatchElement() { 
          if (this.ifNoneMatch == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BundleEntryTransactionComponent.ifNoneMatch");
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
         * @param value {@link #ifNoneMatch} (If the ETag values match, return a 304 Not modified status. See the read/vread interaction documentation.). This is the underlying object with id, value and extensions. The accessor "getIfNoneMatch" gives direct access to the value
         */
        public BundleEntryTransactionComponent setIfNoneMatchElement(StringType value) { 
          this.ifNoneMatch = value;
          return this;
        }

        /**
         * @return If the ETag values match, return a 304 Not modified status. See the read/vread interaction documentation.
         */
        public String getIfNoneMatch() { 
          return this.ifNoneMatch == null ? null : this.ifNoneMatch.getValue();
        }

        /**
         * @param value If the ETag values match, return a 304 Not modified status. See the read/vread interaction documentation.
         */
        public BundleEntryTransactionComponent setIfNoneMatch(String value) { 
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
         * @return {@link #ifMatch} (Only perform the operation if the Etag value matches. For more information, see the API section "Managing Resource Contention".). This is the underlying object with id, value and extensions. The accessor "getIfMatch" gives direct access to the value
         */
        public StringType getIfMatchElement() { 
          if (this.ifMatch == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BundleEntryTransactionComponent.ifMatch");
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
         * @param value {@link #ifMatch} (Only perform the operation if the Etag value matches. For more information, see the API section "Managing Resource Contention".). This is the underlying object with id, value and extensions. The accessor "getIfMatch" gives direct access to the value
         */
        public BundleEntryTransactionComponent setIfMatchElement(StringType value) { 
          this.ifMatch = value;
          return this;
        }

        /**
         * @return Only perform the operation if the Etag value matches. For more information, see the API section "Managing Resource Contention".
         */
        public String getIfMatch() { 
          return this.ifMatch == null ? null : this.ifMatch.getValue();
        }

        /**
         * @param value Only perform the operation if the Etag value matches. For more information, see the API section "Managing Resource Contention".
         */
        public BundleEntryTransactionComponent setIfMatch(String value) { 
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
         * @return {@link #ifModifiedSince} (Only perform the operation if the last updated date matches. For more information, see the API section "Managing Resource Contention".). This is the underlying object with id, value and extensions. The accessor "getIfModifiedSince" gives direct access to the value
         */
        public InstantType getIfModifiedSinceElement() { 
          if (this.ifModifiedSince == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BundleEntryTransactionComponent.ifModifiedSince");
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
         * @param value {@link #ifModifiedSince} (Only perform the operation if the last updated date matches. For more information, see the API section "Managing Resource Contention".). This is the underlying object with id, value and extensions. The accessor "getIfModifiedSince" gives direct access to the value
         */
        public BundleEntryTransactionComponent setIfModifiedSinceElement(InstantType value) { 
          this.ifModifiedSince = value;
          return this;
        }

        /**
         * @return Only perform the operation if the last updated date matches. For more information, see the API section "Managing Resource Contention".
         */
        public Date getIfModifiedSince() { 
          return this.ifModifiedSince == null ? null : this.ifModifiedSince.getValue();
        }

        /**
         * @param value Only perform the operation if the last updated date matches. For more information, see the API section "Managing Resource Contention".
         */
        public BundleEntryTransactionComponent setIfModifiedSince(Date value) {
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
         * @return {@link #ifNoneExist} (Instruct the server not to perform the create if a specified resource already exists. For further information, see "Conditional Create".). This is the underlying object with id, value and extensions. The accessor "getIfNoneExist" gives direct access to the value
         */
        public StringType getIfNoneExistElement() { 
          if (this.ifNoneExist == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BundleEntryTransactionComponent.ifNoneExist");
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
         * @param value {@link #ifNoneExist} (Instruct the server not to perform the create if a specified resource already exists. For further information, see "Conditional Create".). This is the underlying object with id, value and extensions. The accessor "getIfNoneExist" gives direct access to the value
         */
        public BundleEntryTransactionComponent setIfNoneExistElement(StringType value) { 
          this.ifNoneExist = value;
          return this;
        }

        /**
         * @return Instruct the server not to perform the create if a specified resource already exists. For further information, see "Conditional Create".
         */
        public String getIfNoneExist() { 
          return this.ifNoneExist == null ? null : this.ifNoneExist.getValue();
        }

        /**
         * @param value Instruct the server not to perform the create if a specified resource already exists. For further information, see "Conditional Create".
         */
        public BundleEntryTransactionComponent setIfNoneExist(String value) { 
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
          childrenList.add(new Property("url", "uri", "A search URL for this resource that specifies how the resource is matched to an existing resource when processing a transaction (see transaction documentation).", 0, java.lang.Integer.MAX_VALUE, url));
          childrenList.add(new Property("ifNoneMatch", "string", "If the ETag values match, return a 304 Not modified status. See the read/vread interaction documentation.", 0, java.lang.Integer.MAX_VALUE, ifNoneMatch));
          childrenList.add(new Property("ifMatch", "string", "Only perform the operation if the Etag value matches. For more information, see the API section 'Managing Resource Contention'.", 0, java.lang.Integer.MAX_VALUE, ifMatch));
            childrenList.add(new Property("ifModifiedSince", "instant", "Only perform the operation if the last updated date matches. For more information, see the API section 'Managing Resource Contention'.", 0, java.lang.Integer.MAX_VALUE, ifModifiedSince));
          childrenList.add(new Property("ifNoneExist", "string", "Instruct the server not to perform the create if a specified resource already exists. For further information, see 'Conditional Create'.", 0, java.lang.Integer.MAX_VALUE, ifNoneExist));
        }

      public BundleEntryTransactionComponent copy() {
        BundleEntryTransactionComponent dst = new BundleEntryTransactionComponent();
        copyValues(dst);
        dst.method = method == null ? null : method.copy();
        dst.url = url == null ? null : url.copy();
        dst.ifNoneMatch = ifNoneMatch == null ? null : ifNoneMatch.copy();
        dst.ifMatch = ifMatch == null ? null : ifMatch.copy();
        dst.ifModifiedSince = ifModifiedSince == null ? null : ifModifiedSince.copy();
        dst.ifNoneExist = ifNoneExist == null ? null : ifNoneExist.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof BundleEntryTransactionComponent))
          return false;
        BundleEntryTransactionComponent o = (BundleEntryTransactionComponent) other;
        return compareDeep(method, o.method, true) && compareDeep(url, o.url, true) && compareDeep(ifNoneMatch, o.ifNoneMatch, true)
           && compareDeep(ifMatch, o.ifMatch, true) && compareDeep(ifModifiedSince, o.ifModifiedSince, true)
           && compareDeep(ifNoneExist, o.ifNoneExist, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof BundleEntryTransactionComponent))
          return false;
        BundleEntryTransactionComponent o = (BundleEntryTransactionComponent) other;
        return compareValues(method, o.method, true) && compareValues(url, o.url, true) && compareValues(ifNoneMatch, o.ifNoneMatch, true)
           && compareValues(ifMatch, o.ifMatch, true) && compareValues(ifModifiedSince, o.ifModifiedSince, true)
           && compareValues(ifNoneExist, o.ifNoneExist, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (method == null || method.isEmpty()) && (url == null || url.isEmpty())
           && (ifNoneMatch == null || ifNoneMatch.isEmpty()) && (ifMatch == null || ifMatch.isEmpty())
           && (ifModifiedSince == null || ifModifiedSince.isEmpty()) && (ifNoneExist == null || ifNoneExist.isEmpty())
          ;
      }

  }

    @Block()
    public static class BundleEntryTransactionResponseComponent extends BackboneElement {
        /**
         * The status code returned by processing this entry.
         */
        @Child(name="status", type={StringType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="Status return code for entry", formalDefinition="The status code returned by processing this entry." )
        protected StringType status;

        /**
         * The location header created by processing this operation.
         */
        @Child(name="location", type={UriType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="The location, if the operation returns a location", formalDefinition="The location header created by processing this operation." )
        protected UriType location;

        /**
         * The etag for the resource, it the operation for the entry produced a versioned resource.
         */
        @Child(name = "etag", type = {StringType.class}, order = 3, min = 0, max = 1)
        @Description(shortDefinition="The etag for the resource (if relevant)", formalDefinition="The etag for the resource, it the operation for the entry produced a versioned resource." )
        protected StringType etag;

        /**
         * The date/time that the resource was modified on the server.
         */
        @Child(name = "lastModified", type = {InstantType.class}, order = 4, min = 0, max = 1)
        @Description(shortDefinition = "Server's date time modified", formalDefinition = "The date/time that the resource was modified on the server.")
        protected InstantType lastModified;

        private static final long serialVersionUID = -1526413234L;

      public BundleEntryTransactionResponseComponent() {
        super();
      }

      public BundleEntryTransactionResponseComponent(StringType status) {
        super();
        this.status = status;
      }

        /**
         * @return {@link #status} (The status code returned by processing this entry.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
         */
        public StringType getStatusElement() { 
          if (this.status == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BundleEntryTransactionResponseComponent.status");
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
        public BundleEntryTransactionResponseComponent setStatusElement(StringType value) { 
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
        public BundleEntryTransactionResponseComponent setStatus(String value) { 
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
              throw new Error("Attempt to auto-create BundleEntryTransactionResponseComponent.location");
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
        public BundleEntryTransactionResponseComponent setLocationElement(UriType value) { 
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
        public BundleEntryTransactionResponseComponent setLocation(String value) { 
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
                  throw new Error("Attempt to auto-create BundleEntryTransactionResponseComponent.etag");
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
        public BundleEntryTransactionResponseComponent setEtagElement(StringType value) {
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
        public BundleEntryTransactionResponseComponent setEtag(String value) {
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
                    throw new Error("Attempt to auto-create BundleEntryTransactionResponseComponent.lastModified");
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
        public BundleEntryTransactionResponseComponent setLastModifiedElement(InstantType value) {
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
        public BundleEntryTransactionResponseComponent setLastModified(Date value) {
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

      public BundleEntryTransactionResponseComponent copy() {
        BundleEntryTransactionResponseComponent dst = new BundleEntryTransactionResponseComponent();
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
        if (!(other instanceof BundleEntryTransactionResponseComponent))
          return false;
        BundleEntryTransactionResponseComponent o = (BundleEntryTransactionResponseComponent) other;
        return compareDeep(status, o.status, true) && compareDeep(location, o.location, true) && compareDeep(etag, o.etag, true)
                && compareDeep(lastModified, o.lastModified, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof BundleEntryTransactionResponseComponent))
          return false;
        BundleEntryTransactionResponseComponent o = (BundleEntryTransactionResponseComponent) other;
        return compareValues(status, o.status, true) && compareValues(location, o.location, true) && compareValues(etag, o.etag, true)
                && compareValues(lastModified, o.lastModified, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (status == null || status.isEmpty()) && (location == null || location.isEmpty())
                && (etag == null || etag.isEmpty()) && (lastModified == null || lastModified.isEmpty());
      }

  }

    /**
     * Indicates the purpose of this bundle- how it was intended to be used.
     */
    @Child(name = "type", type = {CodeType.class}, order = 0, min = 1, max = 1)
    @Description(shortDefinition="document | message | transaction | transaction-response | history | searchset | collection", formalDefinition="Indicates the purpose of this bundle- how it was intended to be used." )
    protected Enumeration<BundleType> type;

    /**
     * The base URL for the service that provided these resources. All relative URLs are relative to this one (equivalent to xml:base).
     */
    @Child(name = "base", type = {UriType.class}, order = 1, min = 0, max = 1)
    @Description(shortDefinition="Stated Base URL", formalDefinition="The base URL for the service that provided these resources. All relative URLs are relative to this one (equivalent to xml:base)." )
    protected UriType base;

    /**
     * If a set of search matches, this is the total number of matches for the search (as opposed to the number of results in this bundle).
     */
    @Child(name = "total", type = {IntegerType.class}, order = 2, min = 0, max = 1)
    @Description(shortDefinition="If search, the total number of matches", formalDefinition="If a set of search matches, this is the total number of matches for the search (as opposed to the number of results in this bundle)." )
    protected IntegerType total;

    /**
     * A series of links that provide context to this bundle.
     */
    @Child(name = "link", type = {}, order = 3, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Links related to this Bundle", formalDefinition="A series of links that provide context to this bundle." )
    protected List<BundleLinkComponent> link;

    /**
     * An entry in a bundle resource - will either contain a resource, or information about a resource (transactions and history only).
     */
    @Child(name = "entry", type = {}, order = 4, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Entry in the bundle - will have a resource, or information", formalDefinition="An entry in a bundle resource - will either contain a resource, or information about a resource (transactions and history only)." )
    protected List<BundleEntryComponent> entry;

    /**
     * XML Digital Signature - base64 encoded.
     */
    @Child(name = "signature", type = {Base64BinaryType.class}, order = 5, min = 0, max = 1)
    @Description(shortDefinition="XML Digital Signature (base64 encoded)", formalDefinition="XML Digital Signature - base64 encoded." )
    protected Base64BinaryType signature;

    private static final long serialVersionUID = -1332054150L;

    public Bundle() {
      super();
    }

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
     * @return {@link #base} (The base URL for the service that provided these resources. All relative URLs are relative to this one (equivalent to xml:base).). This is the underlying object with id, value and extensions. The accessor "getBase" gives direct access to the value
     */
    public UriType getBaseElement() { 
      if (this.base == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Bundle.base");
        else if (Configuration.doAutoCreate())
          this.base = new UriType(); // bb
      return this.base;
    }

    public boolean hasBaseElement() { 
      return this.base != null && !this.base.isEmpty();
    }

    public boolean hasBase() { 
      return this.base != null && !this.base.isEmpty();
    }

    /**
     * @param value {@link #base} (The base URL for the service that provided these resources. All relative URLs are relative to this one (equivalent to xml:base).). This is the underlying object with id, value and extensions. The accessor "getBase" gives direct access to the value
     */
    public Bundle setBaseElement(UriType value) { 
      this.base = value;
      return this;
    }

    /**
     * @return The base URL for the service that provided these resources. All relative URLs are relative to this one (equivalent to xml:base).
     */
    public String getBase() { 
      return this.base == null ? null : this.base.getValue();
    }

    /**
     * @param value The base URL for the service that provided these resources. All relative URLs are relative to this one (equivalent to xml:base).
     */
    public Bundle setBase(String value) { 
      if (Utilities.noString(value))
        this.base = null;
      else {
        if (this.base == null)
          this.base = new UriType();
        this.base.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #total} (If a set of search matches, this is the total number of matches for the search (as opposed to the number of results in this bundle).). This is the underlying object with id, value and extensions. The accessor "getTotal" gives direct access to the value
     */
    public IntegerType getTotalElement() { 
      if (this.total == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Bundle.total");
        else if (Configuration.doAutoCreate())
          this.total = new IntegerType(); // bb
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
    public Bundle setTotalElement(IntegerType value) { 
      this.total = value;
      return this;
    }

    /**
     * @return If a set of search matches, this is the total number of matches for the search (as opposed to the number of results in this bundle).
     */
    public int getTotal() { 
      return this.total == null ? 0 : this.total.getValue();
    }

    /**
     * @param value If a set of search matches, this is the total number of matches for the search (as opposed to the number of results in this bundle).
     */
    public Bundle setTotal(int value) { 
        if (this.total == null)
          this.total = new IntegerType();
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

    /**
     * @return {@link #signature} (XML Digital Signature - base64 encoded.). This is the underlying object with id, value and extensions. The accessor "getSignature" gives direct access to the value
     */
    public Base64BinaryType getSignatureElement() { 
      if (this.signature == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Bundle.signature");
        else if (Configuration.doAutoCreate())
          this.signature = new Base64BinaryType(); // bb
      return this.signature;
    }

    public boolean hasSignatureElement() { 
      return this.signature != null && !this.signature.isEmpty();
    }

    public boolean hasSignature() { 
      return this.signature != null && !this.signature.isEmpty();
    }

    /**
     * @param value {@link #signature} (XML Digital Signature - base64 encoded.). This is the underlying object with id, value and extensions. The accessor "getSignature" gives direct access to the value
     */
    public Bundle setSignatureElement(Base64BinaryType value) { 
      this.signature = value;
      return this;
    }

    /**
     * @return XML Digital Signature - base64 encoded.
     */
    public byte[] getSignature() { 
      return this.signature == null ? null : this.signature.getValue();
    }

    /**
     * @param value XML Digital Signature - base64 encoded.
     */
    public Bundle setSignature(byte[] value) { 
      if (value == null)
        this.signature = null;
      else {
        if (this.signature == null)
          this.signature = new Base64BinaryType();
        this.signature.setValue(value);
      }
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("type", "code", "Indicates the purpose of this bundle- how it was intended to be used.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("base", "uri", "The base URL for the service that provided these resources. All relative URLs are relative to this one (equivalent to xml:base).", 0, java.lang.Integer.MAX_VALUE, base));
        childrenList.add(new Property("total", "integer", "If a set of search matches, this is the total number of matches for the search (as opposed to the number of results in this bundle).", 0, java.lang.Integer.MAX_VALUE, total));
        childrenList.add(new Property("link", "", "A series of links that provide context to this bundle.", 0, java.lang.Integer.MAX_VALUE, link));
        childrenList.add(new Property("entry", "", "An entry in a bundle resource - will either contain a resource, or information about a resource (transactions and history only).", 0, java.lang.Integer.MAX_VALUE, entry));
        childrenList.add(new Property("signature", "base64Binary", "XML Digital Signature - base64 encoded.", 0, java.lang.Integer.MAX_VALUE, signature));
      }

      public Bundle copy() {
        Bundle dst = new Bundle();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.base = base == null ? null : base.copy();
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
        return compareDeep(type, o.type, true) && compareDeep(base, o.base, true) && compareDeep(total, o.total, true)
           && compareDeep(link, o.link, true) && compareDeep(entry, o.entry, true) && compareDeep(signature, o.signature, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Bundle))
          return false;
        Bundle o = (Bundle) other;
        return compareValues(type, o.type, true) && compareValues(base, o.base, true) && compareValues(total, o.total, true)
           && compareValues(signature, o.signature, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (base == null || base.isEmpty())
           && (total == null || total.isEmpty()) && (link == null || link.isEmpty()) && (entry == null || entry.isEmpty())
           && (signature == null || signature.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Bundle;
   }

  @SearchParamDefinition(name="composition", path="", description="The first resource in the bundle, if the bundle type is 'document' - this is a composition, and this parameter provides access to searches it's contents", type="reference" )
  public static final String SP_COMPOSITION = "composition";
  @SearchParamDefinition(name="type", path="Bundle.type", description="document | message | transaction | transaction-response | history | searchset | collection", type="token" )
  public static final String SP_TYPE = "type";
    @SearchParamDefinition(name = "message", path = "", description = "The first resource in the bundle, if the bundle type is 'message' - this is a message header, and this parameter provides access to search it's contents", type = "reference")
    public static final String SP_MESSAGE = "message";

}

