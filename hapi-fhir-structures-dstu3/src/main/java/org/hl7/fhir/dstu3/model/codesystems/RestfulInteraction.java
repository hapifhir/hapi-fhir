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

public enum RestfulInteraction {

        /**
         * Read the current state of the resource.
         */
        READ, 
        /**
         * Read the state of a specific version of the resource.
         */
        VREAD, 
        /**
         * Update an existing resource by its id (or create it if it is new).
         */
        UPDATE, 
        /**
         * Update an existing resource by posting a set of changes to it.
         */
        PATCH, 
        /**
         * Delete a resource.
         */
        DELETE, 
        /**
         * Retrieve the change history for a particular resource, type of resource, or the entire system.
         */
        HISTORY, 
        /**
         * Retrieve the change history for a particular resource.
         */
        HISTORYINSTANCE, 
        /**
         * Retrieve the change history for all resources of a particular type.
         */
        HISTORYTYPE, 
        /**
         * Retrieve the change history for all resources on a system.
         */
        HISTORYSYSTEM, 
        /**
         * Create a new resource with a server assigned id.
         */
        CREATE, 
        /**
         * Search a resource type or all resources based on some filter criteria.
         */
        SEARCH, 
        /**
         * Search all resources of the specified type based on some filter criteria.
         */
        SEARCHTYPE, 
        /**
         * Search all resources based on some filter criteria.
         */
        SEARCHSYSTEM, 
        /**
         * Get a Capability Statement for the system.
         */
        CAPABILITIES, 
        /**
         * Update, create or delete a set of resources as a single transaction.
         */
        TRANSACTION, 
        /**
         * perform a set of a separate interactions in a single http operation
         */
        BATCH, 
        /**
         * Perform an operation as defined by an OperationDefinition.
         */
        OPERATION, 
        /**
         * added to help the parsers
         */
        NULL;
        public static RestfulInteraction fromCode(String codeString) throws FHIRException {
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
        if ("history".equals(codeString))
          return HISTORY;
        if ("history-instance".equals(codeString))
          return HISTORYINSTANCE;
        if ("history-type".equals(codeString))
          return HISTORYTYPE;
        if ("history-system".equals(codeString))
          return HISTORYSYSTEM;
        if ("create".equals(codeString))
          return CREATE;
        if ("search".equals(codeString))
          return SEARCH;
        if ("search-type".equals(codeString))
          return SEARCHTYPE;
        if ("search-system".equals(codeString))
          return SEARCHSYSTEM;
        if ("capabilities".equals(codeString))
          return CAPABILITIES;
        if ("transaction".equals(codeString))
          return TRANSACTION;
        if ("batch".equals(codeString))
          return BATCH;
        if ("operation".equals(codeString))
          return OPERATION;
        throw new FHIRException("Unknown RestfulInteraction code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case READ: return "read";
            case VREAD: return "vread";
            case UPDATE: return "update";
            case PATCH: return "patch";
            case DELETE: return "delete";
            case HISTORY: return "history";
            case HISTORYINSTANCE: return "history-instance";
            case HISTORYTYPE: return "history-type";
            case HISTORYSYSTEM: return "history-system";
            case CREATE: return "create";
            case SEARCH: return "search";
            case SEARCHTYPE: return "search-type";
            case SEARCHSYSTEM: return "search-system";
            case CAPABILITIES: return "capabilities";
            case TRANSACTION: return "transaction";
            case BATCH: return "batch";
            case OPERATION: return "operation";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/restful-interaction";
        }
        public String getDefinition() {
          switch (this) {
            case READ: return "Read the current state of the resource.";
            case VREAD: return "Read the state of a specific version of the resource.";
            case UPDATE: return "Update an existing resource by its id (or create it if it is new).";
            case PATCH: return "Update an existing resource by posting a set of changes to it.";
            case DELETE: return "Delete a resource.";
            case HISTORY: return "Retrieve the change history for a particular resource, type of resource, or the entire system.";
            case HISTORYINSTANCE: return "Retrieve the change history for a particular resource.";
            case HISTORYTYPE: return "Retrieve the change history for all resources of a particular type.";
            case HISTORYSYSTEM: return "Retrieve the change history for all resources on a system.";
            case CREATE: return "Create a new resource with a server assigned id.";
            case SEARCH: return "Search a resource type or all resources based on some filter criteria.";
            case SEARCHTYPE: return "Search all resources of the specified type based on some filter criteria.";
            case SEARCHSYSTEM: return "Search all resources based on some filter criteria.";
            case CAPABILITIES: return "Get a Capability Statement for the system.";
            case TRANSACTION: return "Update, create or delete a set of resources as a single transaction.";
            case BATCH: return "perform a set of a separate interactions in a single http operation";
            case OPERATION: return "Perform an operation as defined by an OperationDefinition.";
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
            case HISTORY: return "history";
            case HISTORYINSTANCE: return "history-instance";
            case HISTORYTYPE: return "history-type";
            case HISTORYSYSTEM: return "history-system";
            case CREATE: return "create";
            case SEARCH: return "search";
            case SEARCHTYPE: return "search-type";
            case SEARCHSYSTEM: return "search-system";
            case CAPABILITIES: return "capabilities";
            case TRANSACTION: return "transaction";
            case BATCH: return "batch";
            case OPERATION: return "operation";
            default: return "?";
          }
    }


}

