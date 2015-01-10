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

import java.math.*;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
/**
 * A container for a group of resources.
 */
@ResourceDef(name="Bundle", profile="http://hl7.org/fhir/Profile/Bundle")
public class Bundle extends Resource {

    public enum BundleType implements FhirEnum {
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

      public static final BundleTypeEnumFactory ENUM_FACTORY = new BundleTypeEnumFactory();

        public static BundleType fromCode(String codeString) throws IllegalArgumentException {
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
        throw new IllegalArgumentException("Unknown BundleType code '"+codeString+"'");
        }
        @Override
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
    public String toCode(BundleType code) throws IllegalArgumentException {
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

    public enum BundleEntryStatus implements FhirEnum {
        /**
         * Transaction: perform a create operation on this resource.
         */
        CREATE, 
        /**
         * Transaction: perform an update operation on this resource.
         */
        UPDATE, 
        /**
         * Transaction: look for this resource using the search url provided. If there's no match, create it. Search: this resource is returned because it matches the search criteria.
         */
        MATCH, 
        /**
         * Search: this resource is returned because it meets an _include criteria.
         */
        INCLUDE, 
        /**
         * added to help the parsers
         */
        NULL;

      public static final BundleEntryStatusEnumFactory ENUM_FACTORY = new BundleEntryStatusEnumFactory();

        public static BundleEntryStatus fromCode(String codeString) throws IllegalArgumentException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("create".equals(codeString))
          return CREATE;
        if ("update".equals(codeString))
          return UPDATE;
        if ("match".equals(codeString))
          return MATCH;
        if ("include".equals(codeString))
          return INCLUDE;
        throw new IllegalArgumentException("Unknown BundleEntryStatus code '"+codeString+"'");
        }
        @Override
        public String toCode() {
          switch (this) {
            case CREATE: return "create";
            case UPDATE: return "update";
            case MATCH: return "match";
            case INCLUDE: return "include";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case CREATE: return "";
            case UPDATE: return "";
            case MATCH: return "";
            case INCLUDE: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case CREATE: return "Transaction: perform a create operation on this resource.";
            case UPDATE: return "Transaction: perform an update operation on this resource.";
            case MATCH: return "Transaction: look for this resource using the search url provided. If there's no match, create it. Search: this resource is returned because it matches the search criteria.";
            case INCLUDE: return "Search: this resource is returned because it meets an _include criteria.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CREATE: return "Create";
            case UPDATE: return "Update";
            case MATCH: return "Match";
            case INCLUDE: return "Include";
            default: return "?";
          }
        }
    }

  public static class BundleEntryStatusEnumFactory implements EnumFactory<BundleEntryStatus> {
    public BundleEntryStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("create".equals(codeString))
          return BundleEntryStatus.CREATE;
        if ("update".equals(codeString))
          return BundleEntryStatus.UPDATE;
        if ("match".equals(codeString))
          return BundleEntryStatus.MATCH;
        if ("include".equals(codeString))
          return BundleEntryStatus.INCLUDE;
        throw new IllegalArgumentException("Unknown BundleEntryStatus code '"+codeString+"'");
        }
    public String toCode(BundleEntryStatus code) throws IllegalArgumentException {
      if (code == BundleEntryStatus.CREATE)
        return "create";
      if (code == BundleEntryStatus.UPDATE)
        return "update";
      if (code == BundleEntryStatus.MATCH)
        return "match";
      if (code == BundleEntryStatus.INCLUDE)
        return "include";
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
              this.relation = new StringType();
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
              this.url = new UriType();
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
         * The status of a resource in the bundle. Used for search (to differentiate between resources included as a match, and resources included as an _include), for history (deleted resources), and for transactions (create/update/delete).
         */
        @Child(name="status", type={CodeType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="create | update | match | include - for search & transaction", formalDefinition="The status of a resource in the bundle. Used for search (to differentiate between resources included as a match, and resources included as an _include), for history (deleted resources), and for transactions (create/update/delete)." )
        protected Enumeration<BundleEntryStatus> status;

        /**
         * Search URL for this resource when processing a transaction (see transaction documentation).
         */
        @Child(name="search", type={UriType.class}, order=3, min=0, max=1)
        @Description(shortDefinition="Search URL (see transaction)", formalDefinition="Search URL for this resource when processing a transaction (see transaction documentation)." )
        protected UriType search;

        /**
         * When searching, the server's search ranking score for the entry.
         */
        @Child(name="score", type={DecimalType.class}, order=4, min=0, max=1)
        @Description(shortDefinition="Search ranking (between 0 and 1)", formalDefinition="When searching, the server's search ranking score for the entry." )
        protected DecimalType score;

        /**
         * If this is an entry that represents a deleted resource. Only used when the bundle is a transaction or a history type. See RESTful API documentation for further informatino.
         */
        @Child(name="deleted", type={}, order=5, min=0, max=1)
        @Description(shortDefinition="If this is a deleted resource (transaction/history)", formalDefinition="If this is an entry that represents a deleted resource. Only used when the bundle is a transaction or a history type. See RESTful API documentation for further informatino." )
        protected BundleEntryDeletedComponent deleted;

        /**
         * The Resources for the entry.
         */
        @Child(name="resource", type={Resource.class}, order=6, min=0, max=1)
        @Description(shortDefinition="Resources in this bundle", formalDefinition="The Resources for the entry." )
        protected Resource resource;

        private static final long serialVersionUID = 509077972L;

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
              this.base = new UriType();
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
         * @return {@link #status} (The status of a resource in the bundle. Used for search (to differentiate between resources included as a match, and resources included as an _include), for history (deleted resources), and for transactions (create/update/delete).). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
         */
        public Enumeration<BundleEntryStatus> getStatusElement() { 
          if (this.status == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BundleEntryComponent.status");
            else if (Configuration.doAutoCreate())
              this.status = new Enumeration<BundleEntryStatus>();
          return this.status;
        }

        public boolean hasStatusElement() { 
          return this.status != null && !this.status.isEmpty();
        }

        public boolean hasStatus() { 
          return this.status != null && !this.status.isEmpty();
        }

        /**
         * @param value {@link #status} (The status of a resource in the bundle. Used for search (to differentiate between resources included as a match, and resources included as an _include), for history (deleted resources), and for transactions (create/update/delete).). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
         */
        public BundleEntryComponent setStatusElement(Enumeration<BundleEntryStatus> value) { 
          this.status = value;
          return this;
        }

        /**
         * @return The status of a resource in the bundle. Used for search (to differentiate between resources included as a match, and resources included as an _include), for history (deleted resources), and for transactions (create/update/delete).
         */
        public BundleEntryStatus getStatus() { 
          return this.status == null ? null : this.status.getValue();
        }

        /**
         * @param value The status of a resource in the bundle. Used for search (to differentiate between resources included as a match, and resources included as an _include), for history (deleted resources), and for transactions (create/update/delete).
         */
        public BundleEntryComponent setStatus(BundleEntryStatus value) { 
          if (value == null)
            this.status = null;
          else {
            if (this.status == null)
              this.status = new Enumeration<BundleEntryStatus>(BundleEntryStatus.ENUM_FACTORY);
            this.status.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #search} (Search URL for this resource when processing a transaction (see transaction documentation).). This is the underlying object with id, value and extensions. The accessor "getSearch" gives direct access to the value
         */
        public UriType getSearchElement() { 
          if (this.search == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BundleEntryComponent.search");
            else if (Configuration.doAutoCreate())
              this.search = new UriType();
          return this.search;
        }

        public boolean hasSearchElement() { 
          return this.search != null && !this.search.isEmpty();
        }

        public boolean hasSearch() { 
          return this.search != null && !this.search.isEmpty();
        }

        /**
         * @param value {@link #search} (Search URL for this resource when processing a transaction (see transaction documentation).). This is the underlying object with id, value and extensions. The accessor "getSearch" gives direct access to the value
         */
        public BundleEntryComponent setSearchElement(UriType value) { 
          this.search = value;
          return this;
        }

        /**
         * @return Search URL for this resource when processing a transaction (see transaction documentation).
         */
        public String getSearch() { 
          return this.search == null ? null : this.search.getValue();
        }

        /**
         * @param value Search URL for this resource when processing a transaction (see transaction documentation).
         */
        public BundleEntryComponent setSearch(String value) { 
          if (Utilities.noString(value))
            this.search = null;
          else {
            if (this.search == null)
              this.search = new UriType();
            this.search.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #score} (When searching, the server's search ranking score for the entry.). This is the underlying object with id, value and extensions. The accessor "getScore" gives direct access to the value
         */
        public DecimalType getScoreElement() { 
          if (this.score == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BundleEntryComponent.score");
            else if (Configuration.doAutoCreate())
              this.score = new DecimalType();
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
        public BundleEntryComponent setScoreElement(DecimalType value) { 
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
        public BundleEntryComponent setScore(BigDecimal value) { 
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
         * @return {@link #deleted} (If this is an entry that represents a deleted resource. Only used when the bundle is a transaction or a history type. See RESTful API documentation for further informatino.)
         */
        public BundleEntryDeletedComponent getDeleted() { 
          if (this.deleted == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BundleEntryComponent.deleted");
            else if (Configuration.doAutoCreate())
              this.deleted = new BundleEntryDeletedComponent();
          return this.deleted;
        }

        public boolean hasDeleted() { 
          return this.deleted != null && !this.deleted.isEmpty();
        }

        /**
         * @param value {@link #deleted} (If this is an entry that represents a deleted resource. Only used when the bundle is a transaction or a history type. See RESTful API documentation for further informatino.)
         */
        public BundleEntryComponent setDeleted(BundleEntryDeletedComponent value) { 
          this.deleted = value;
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

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("base", "uri", "The Base URL for the resource, if different to the base URL specified for the bundle as a whole.", 0, java.lang.Integer.MAX_VALUE, base));
          childrenList.add(new Property("status", "code", "The status of a resource in the bundle. Used for search (to differentiate between resources included as a match, and resources included as an _include), for history (deleted resources), and for transactions (create/update/delete).", 0, java.lang.Integer.MAX_VALUE, status));
          childrenList.add(new Property("search", "uri", "Search URL for this resource when processing a transaction (see transaction documentation).", 0, java.lang.Integer.MAX_VALUE, search));
          childrenList.add(new Property("score", "decimal", "When searching, the server's search ranking score for the entry.", 0, java.lang.Integer.MAX_VALUE, score));
          childrenList.add(new Property("deleted", "", "If this is an entry that represents a deleted resource. Only used when the bundle is a transaction or a history type. See RESTful API documentation for further informatino.", 0, java.lang.Integer.MAX_VALUE, deleted));
          childrenList.add(new Property("resource", "Resource", "The Resources for the entry.", 0, java.lang.Integer.MAX_VALUE, resource));
        }

      public BundleEntryComponent copy() {
        BundleEntryComponent dst = new BundleEntryComponent();
        copyValues(dst);
        dst.base = base == null ? null : base.copy();
        dst.status = status == null ? null : status.copy();
        dst.search = search == null ? null : search.copy();
        dst.score = score == null ? null : score.copy();
        dst.deleted = deleted == null ? null : deleted.copy();
        dst.resource = resource == null ? null : resource.copy();
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (base == null || base.isEmpty()) && (status == null || status.isEmpty())
           && (search == null || search.isEmpty()) && (score == null || score.isEmpty()) && (deleted == null || deleted.isEmpty())
           && (resource == null || resource.isEmpty());
      }

  }

    @Block()
    public static class BundleEntryDeletedComponent extends BackboneElement {
        /**
         * The type of resource that was deleted (required to construct the identity).
         */
        @Child(name="type", type={CodeType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="Type of resource that was deleted", formalDefinition="The type of resource that was deleted (required to construct the identity)." )
        protected CodeType type;

        /**
         * The id of the resource that was deleted.
         */
        @Child(name="resourceId", type={IdType.class}, order=2, min=1, max=1)
        @Description(shortDefinition="Id of resource that was deleted", formalDefinition="The id of the resource that was deleted." )
        protected IdType resourceId;

        /**
         * Version id for releted resource.
         */
        @Child(name="versionId", type={IdType.class}, order=3, min=1, max=1)
        @Description(shortDefinition="Version id for releted resource", formalDefinition="Version id for releted resource." )
        protected IdType versionId;

        /**
         * The date/time that the resource was deleted.
         */
        @Child(name="instant", type={InstantType.class}, order=4, min=1, max=1)
        @Description(shortDefinition="When the resource was deleted", formalDefinition="The date/time that the resource was deleted." )
        protected InstantType instant;

        private static final long serialVersionUID = -1528107649L;

      public BundleEntryDeletedComponent() {
        super();
      }

      public BundleEntryDeletedComponent(CodeType type, IdType resourceId, IdType versionId, InstantType instant) {
        super();
        this.type = type;
        this.resourceId = resourceId;
        this.versionId = versionId;
        this.instant = instant;
      }

        /**
         * @return {@link #type} (The type of resource that was deleted (required to construct the identity).). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public CodeType getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BundleEntryDeletedComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeType();
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of resource that was deleted (required to construct the identity).). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public BundleEntryDeletedComponent setTypeElement(CodeType value) { 
          this.type = value;
          return this;
        }

        /**
         * @return The type of resource that was deleted (required to construct the identity).
         */
        public String getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value The type of resource that was deleted (required to construct the identity).
         */
        public BundleEntryDeletedComponent setType(String value) { 
            if (this.type == null)
              this.type = new CodeType();
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #resourceId} (The id of the resource that was deleted.). This is the underlying object with id, value and extensions. The accessor "getResourceId" gives direct access to the value
         */
        public IdType getResourceIdElement() { 
          if (this.resourceId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BundleEntryDeletedComponent.resourceId");
            else if (Configuration.doAutoCreate())
              this.resourceId = new IdType();
          return this.resourceId;
        }

        public boolean hasResourceIdElement() { 
          return this.resourceId != null && !this.resourceId.isEmpty();
        }

        public boolean hasResourceId() { 
          return this.resourceId != null && !this.resourceId.isEmpty();
        }

        /**
         * @param value {@link #resourceId} (The id of the resource that was deleted.). This is the underlying object with id, value and extensions. The accessor "getResourceId" gives direct access to the value
         */
        public BundleEntryDeletedComponent setResourceIdElement(IdType value) { 
          this.resourceId = value;
          return this;
        }

        /**
         * @return The id of the resource that was deleted.
         */
        public String getResourceId() { 
          return this.resourceId == null ? null : this.resourceId.getValue();
        }

        /**
         * @param value The id of the resource that was deleted.
         */
        public BundleEntryDeletedComponent setResourceId(String value) { 
            if (this.resourceId == null)
              this.resourceId = new IdType();
            this.resourceId.setValue(value);
          return this;
        }

        /**
         * @return {@link #versionId} (Version id for releted resource.). This is the underlying object with id, value and extensions. The accessor "getVersionId" gives direct access to the value
         */
        public IdType getVersionIdElement() { 
          if (this.versionId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BundleEntryDeletedComponent.versionId");
            else if (Configuration.doAutoCreate())
              this.versionId = new IdType();
          return this.versionId;
        }

        public boolean hasVersionIdElement() { 
          return this.versionId != null && !this.versionId.isEmpty();
        }

        public boolean hasVersionId() { 
          return this.versionId != null && !this.versionId.isEmpty();
        }

        /**
         * @param value {@link #versionId} (Version id for releted resource.). This is the underlying object with id, value and extensions. The accessor "getVersionId" gives direct access to the value
         */
        public BundleEntryDeletedComponent setVersionIdElement(IdType value) { 
          this.versionId = value;
          return this;
        }

        /**
         * @return Version id for releted resource.
         */
        public String getVersionId() { 
          return this.versionId == null ? null : this.versionId.getValue();
        }

        /**
         * @param value Version id for releted resource.
         */
        public BundleEntryDeletedComponent setVersionId(String value) { 
            if (this.versionId == null)
              this.versionId = new IdType();
            this.versionId.setValue(value);
          return this;
        }

        /**
         * @return {@link #instant} (The date/time that the resource was deleted.). This is the underlying object with id, value and extensions. The accessor "getInstant" gives direct access to the value
         */
        public InstantType getInstantElement() { 
          if (this.instant == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BundleEntryDeletedComponent.instant");
            else if (Configuration.doAutoCreate())
              this.instant = new InstantType();
          return this.instant;
        }

        public boolean hasInstantElement() { 
          return this.instant != null && !this.instant.isEmpty();
        }

        public boolean hasInstant() { 
          return this.instant != null && !this.instant.isEmpty();
        }

        /**
         * @param value {@link #instant} (The date/time that the resource was deleted.). This is the underlying object with id, value and extensions. The accessor "getInstant" gives direct access to the value
         */
        public BundleEntryDeletedComponent setInstantElement(InstantType value) { 
          this.instant = value;
          return this;
        }

        /**
         * @return The date/time that the resource was deleted.
         */
        public Date getInstant() { 
          return this.instant == null ? null : this.instant.getValue();
        }

        /**
         * @param value The date/time that the resource was deleted.
         */
        public BundleEntryDeletedComponent setInstant(Date value) { 
            if (this.instant == null)
              this.instant = new InstantType();
            this.instant.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "code", "The type of resource that was deleted (required to construct the identity).", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("resourceId", "id", "The id of the resource that was deleted.", 0, java.lang.Integer.MAX_VALUE, resourceId));
          childrenList.add(new Property("versionId", "id", "Version id for releted resource.", 0, java.lang.Integer.MAX_VALUE, versionId));
          childrenList.add(new Property("instant", "instant", "The date/time that the resource was deleted.", 0, java.lang.Integer.MAX_VALUE, instant));
        }

      public BundleEntryDeletedComponent copy() {
        BundleEntryDeletedComponent dst = new BundleEntryDeletedComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.resourceId = resourceId == null ? null : resourceId.copy();
        dst.versionId = versionId == null ? null : versionId.copy();
        dst.instant = instant == null ? null : instant.copy();
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (resourceId == null || resourceId.isEmpty())
           && (versionId == null || versionId.isEmpty()) && (instant == null || instant.isEmpty());
      }

  }

    /**
     * Indicates the purpose of this bundle- how it was intended to be used.
     */
    @Child(name="type", type={CodeType.class}, order=-1, min=1, max=1)
    @Description(shortDefinition="document | message | transaction | transaction-response | history | searchset | collection", formalDefinition="Indicates the purpose of this bundle- how it was intended to be used." )
    protected Enumeration<BundleType> type;

    /**
     * The base URL for the service that provided these resources. All relative URLs are relative to this one (equivalent to xml:base).
     */
    @Child(name="base", type={UriType.class}, order=0, min=0, max=1)
    @Description(shortDefinition="Stated Base URL", formalDefinition="The base URL for the service that provided these resources. All relative URLs are relative to this one (equivalent to xml:base)." )
    protected UriType base;

    /**
     * If a set of search matches, this is the total number of matches for the search (as opposed to the number of results in this bundle).
     */
    @Child(name="total", type={IntegerType.class}, order=1, min=0, max=1)
    @Description(shortDefinition="If search, the total number of matches", formalDefinition="If a set of search matches, this is the total number of matches for the search (as opposed to the number of results in this bundle)." )
    protected IntegerType total;

    /**
     * A series of links that provide context to this bundle.
     */
    @Child(name="link", type={}, order=2, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Links related to this Bundle", formalDefinition="A series of links that provide context to this bundle." )
    protected List<BundleLinkComponent> link;

    /**
     * An entry in a bundle resource - will either contain a resource, or a deleted entry (transaction and history bundles only).
     */
    @Child(name="entry", type={}, order=3, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Entry in the bundle - will have deleted or resource", formalDefinition="An entry in a bundle resource - will either contain a resource, or a deleted entry (transaction and history bundles only)." )
    protected List<BundleEntryComponent> entry;

    /**
     * XML Digital Signature - base64 encoded.
     */
    @Child(name="signature", type={Base64BinaryType.class}, order=4, min=0, max=1)
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
          this.type = new Enumeration<BundleType>();
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
          this.type = new Enumeration<BundleType>(BundleType.ENUM_FACTORY);
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
          this.base = new UriType();
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
          this.total = new IntegerType();
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
      return this.total == null ? null : this.total.getValue();
    }

    /**
     * @param value If a set of search matches, this is the total number of matches for the search (as opposed to the number of results in this bundle).
     */
    public Bundle setTotal(int value) { 
      if (value == -1)
        this.total = null;
      else {
        if (this.total == null)
          this.total = new IntegerType();
        this.total.setValue(value);
      }
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
     * @return {@link #entry} (An entry in a bundle resource - will either contain a resource, or a deleted entry (transaction and history bundles only).)
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
     * @return {@link #entry} (An entry in a bundle resource - will either contain a resource, or a deleted entry (transaction and history bundles only).)
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
          this.signature = new Base64BinaryType();
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
        childrenList.add(new Property("entry", "", "An entry in a bundle resource - will either contain a resource, or a deleted entry (transaction and history bundles only).", 0, java.lang.Integer.MAX_VALUE, entry));
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

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (base == null || base.isEmpty())
           && (total == null || total.isEmpty()) && (link == null || link.isEmpty()) && (entry == null || entry.isEmpty())
           && (signature == null || signature.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Bundle;
   }

  @SearchParamDefinition(name="message", path="", description="The first resource in the bundle, if the bundle type is 'message' - this is a message header, and this parameter provides access to search it's contents", type="reference" )
  public static final String SP_MESSAGE = "message";
  @SearchParamDefinition(name="composition", path="", description="The first resource in the bundle, if the bundle type is 'document' - this is a composition, and this parameter provides access to searches it's contents", type="reference" )
  public static final String SP_COMPOSITION = "composition";
  @SearchParamDefinition(name="type", path="Bundle.type", description="document | message | transaction | transaction-response | history | searchset | collection", type="token" )
  public static final String SP_TYPE = "type";

}

