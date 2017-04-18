package org.hl7.fhir.dstu3.model.codesystems;

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


import org.hl7.fhir.exceptions.FHIRException;

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
         * The bundle is a transaction response. Because the response is a transaction response, the transaction has succeeded, and all responses are error free.
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
         * The bundle is a set of resources collected into a single package for ease of distribution.
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
          return "http://hl7.org/fhir/bundle-type";
        }
        public String getDefinition() {
          switch (this) {
            case DOCUMENT: return "The bundle is a document. The first resource is a Composition.";
            case MESSAGE: return "The bundle is a message. The first resource is a MessageHeader.";
            case TRANSACTION: return "The bundle is a transaction - intended to be processed by a server as an atomic commit.";
            case TRANSACTIONRESPONSE: return "The bundle is a transaction response. Because the response is a transaction response, the transaction has succeeded, and all responses are error free.";
            case BATCH: return "The bundle is a transaction - intended to be processed by a server as a group of actions.";
            case BATCHRESPONSE: return "The bundle is a batch response. Note that as a batch, some responses may indicate failure and others success.";
            case HISTORY: return "The bundle is a list of resources from a history interaction on a server.";
            case SEARCHSET: return "The bundle is a list of resources returned as a result of a search/query interaction, operation, or message.";
            case COLLECTION: return "The bundle is a set of resources collected into a single package for ease of distribution.";
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

