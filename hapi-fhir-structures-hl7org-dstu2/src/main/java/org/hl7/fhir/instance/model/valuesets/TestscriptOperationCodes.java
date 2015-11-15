package org.hl7.fhir.instance.model.valuesets;

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

// Generated on Wed, Nov 11, 2015 10:54-0500 for FHIR v1.0.2


public enum TestscriptOperationCodes {

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
         * Delete a resource.
         */
        DELETE, 
        /**
         * Retrieve the update history for a particular resource or resource type.
         */
        HISTORY, 
        /**
         * Create a new resource with a server assigned id.
         */
        CREATE, 
        /**
         * Search based on some filter criteria.
         */
        SEARCH, 
        /**
         * Update, create or delete a set of resources as a single transaction.
         */
        TRANSACTION, 
        /**
         * Get a conformance statement for the system.
         */
        CONFORMANCE, 
        /**
         * Closure Table Maintenance.
         */
        CLOSURE, 
        /**
         * Generate a Document.
         */
        DOCUMENT, 
        /**
         * Fetch Encounter/Patient Record.
         */
        EVERYTHING, 
        /**
         * Value Set Expansion.
         */
        EXPAND, 
        /**
         * Find a functional list.
         */
        FIND, 
        /**
         * Concept Look Up.
         */
        LOOKUP, 
        /**
         * Access a list of profiles, tags, and security labels.
         */
        META, 
        /**
         * Add profiles, tags, and security labels to a resource.
         */
        METAADD, 
        /**
         * Delete profiles, tags, and security labels for a resource.
         */
        METADELETE, 
        /**
         * Populate Questionnaire.
         */
        POPULATE, 
        /**
         * Process Message.
         */
        PROCESSMESSAGE, 
        /**
         * Build Questionnaire.
         */
        QUESTIONNAIRE, 
        /**
         * Concept Translation.
         */
        TRANSLATE, 
        /**
         * Validate a resource.
         */
        VALIDATE, 
        /**
         * Value Set based Validation.
         */
        VALIDATECODE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static TestscriptOperationCodes fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("read".equals(codeString))
          return READ;
        if ("vread".equals(codeString))
          return VREAD;
        if ("update".equals(codeString))
          return UPDATE;
        if ("delete".equals(codeString))
          return DELETE;
        if ("history".equals(codeString))
          return HISTORY;
        if ("create".equals(codeString))
          return CREATE;
        if ("search".equals(codeString))
          return SEARCH;
        if ("transaction".equals(codeString))
          return TRANSACTION;
        if ("conformance".equals(codeString))
          return CONFORMANCE;
        if ("closure".equals(codeString))
          return CLOSURE;
        if ("document".equals(codeString))
          return DOCUMENT;
        if ("everything".equals(codeString))
          return EVERYTHING;
        if ("expand".equals(codeString))
          return EXPAND;
        if ("find".equals(codeString))
          return FIND;
        if ("lookup".equals(codeString))
          return LOOKUP;
        if ("meta".equals(codeString))
          return META;
        if ("meta-add".equals(codeString))
          return METAADD;
        if ("meta-delete".equals(codeString))
          return METADELETE;
        if ("populate".equals(codeString))
          return POPULATE;
        if ("process-message".equals(codeString))
          return PROCESSMESSAGE;
        if ("questionnaire".equals(codeString))
          return QUESTIONNAIRE;
        if ("translate".equals(codeString))
          return TRANSLATE;
        if ("validate".equals(codeString))
          return VALIDATE;
        if ("validate-code".equals(codeString))
          return VALIDATECODE;
        throw new Exception("Unknown TestscriptOperationCodes code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case READ: return "read";
            case VREAD: return "vread";
            case UPDATE: return "update";
            case DELETE: return "delete";
            case HISTORY: return "history";
            case CREATE: return "create";
            case SEARCH: return "search";
            case TRANSACTION: return "transaction";
            case CONFORMANCE: return "conformance";
            case CLOSURE: return "closure";
            case DOCUMENT: return "document";
            case EVERYTHING: return "everything";
            case EXPAND: return "expand";
            case FIND: return "find";
            case LOOKUP: return "lookup";
            case META: return "meta";
            case METAADD: return "meta-add";
            case METADELETE: return "meta-delete";
            case POPULATE: return "populate";
            case PROCESSMESSAGE: return "process-message";
            case QUESTIONNAIRE: return "questionnaire";
            case TRANSLATE: return "translate";
            case VALIDATE: return "validate";
            case VALIDATECODE: return "validate-code";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/testscript-operation-codes";
        }
        public String getDefinition() {
          switch (this) {
            case READ: return "Read the current state of the resource.";
            case VREAD: return "Read the state of a specific version of the resource.";
            case UPDATE: return "Update an existing resource by its id (or create it if it is new).";
            case DELETE: return "Delete a resource.";
            case HISTORY: return "Retrieve the update history for a particular resource or resource type.";
            case CREATE: return "Create a new resource with a server assigned id.";
            case SEARCH: return "Search based on some filter criteria.";
            case TRANSACTION: return "Update, create or delete a set of resources as a single transaction.";
            case CONFORMANCE: return "Get a conformance statement for the system.";
            case CLOSURE: return "Closure Table Maintenance.";
            case DOCUMENT: return "Generate a Document.";
            case EVERYTHING: return "Fetch Encounter/Patient Record.";
            case EXPAND: return "Value Set Expansion.";
            case FIND: return "Find a functional list.";
            case LOOKUP: return "Concept Look Up.";
            case META: return "Access a list of profiles, tags, and security labels.";
            case METAADD: return "Add profiles, tags, and security labels to a resource.";
            case METADELETE: return "Delete profiles, tags, and security labels for a resource.";
            case POPULATE: return "Populate Questionnaire.";
            case PROCESSMESSAGE: return "Process Message.";
            case QUESTIONNAIRE: return "Build Questionnaire.";
            case TRANSLATE: return "Concept Translation.";
            case VALIDATE: return "Validate a resource.";
            case VALIDATECODE: return "Value Set based Validation.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case READ: return "Read";
            case VREAD: return "Version Read";
            case UPDATE: return "Update";
            case DELETE: return "Delete";
            case HISTORY: return "History";
            case CREATE: return "Create";
            case SEARCH: return "Search";
            case TRANSACTION: return "Transaction";
            case CONFORMANCE: return "Conformance";
            case CLOSURE: return "$closure";
            case DOCUMENT: return "$document";
            case EVERYTHING: return "$everything";
            case EXPAND: return "$expand";
            case FIND: return "$find";
            case LOOKUP: return "$lookup";
            case META: return "$meta";
            case METAADD: return "$meta-add";
            case METADELETE: return "$meta-delete";
            case POPULATE: return "$populate";
            case PROCESSMESSAGE: return "$process-message";
            case QUESTIONNAIRE: return "$questionnaire";
            case TRANSLATE: return "$translate";
            case VALIDATE: return "$validate";
            case VALIDATECODE: return "$validate-code";
            default: return "?";
          }
    }


}

