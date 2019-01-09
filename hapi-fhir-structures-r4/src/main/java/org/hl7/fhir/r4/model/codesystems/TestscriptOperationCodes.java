package org.hl7.fhir.r4.model.codesystems;

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

// Generated on Thu, Dec 27, 2018 10:06-0500 for FHIR v4.0.0


import org.hl7.fhir.exceptions.FHIRException;

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
         * Update an existing resource by its id.
         */
        UPDATE, 
        /**
         * Update an existing resource by its id (or create it if it is new).
         */
        UPDATECREATE, 
        /**
         * Patch an existing resource by its id.
         */
        PATCH, 
        /**
         * Delete a resource.
         */
        DELETE, 
        /**
         * Conditionally delete a single resource based on search parameters.
         */
        DELETECONDSINGLE, 
        /**
         * Conditionally delete one or more resources based on search parameters.
         */
        DELETECONDMULTIPLE, 
        /**
         * Retrieve the change history for a particular resource or resource type.
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
         * Update, create or delete a set of resources as independent actions.
         */
        BATCH, 
        /**
         * Update, create or delete a set of resources as a single transaction.
         */
        TRANSACTION, 
        /**
         * Get a capability statement for the system.
         */
        CAPABILITIES, 
        /**
         * Realizes an ActivityDefinition in a specific context
         */
        APPLY, 
        /**
         * Closure Table Maintenance
         */
        CLOSURE, 
        /**
         * Finding Codes based on supplied properties
         */
        FINDMATCHES, 
        /**
         * Compare two systems CapabilityStatements
         */
        CONFORMS, 
        /**
         * Aggregates and returns the parameters and data requirements for a resource and all its dependencies as a single module definition
         */
        DATAREQUIREMENTS, 
        /**
         * Generate a Document
         */
        DOCUMENT, 
        /**
         * Request clinical decision support guidance based on a specific decision support module
         */
        EVALUATE, 
        /**
         * Invoke an eMeasure and obtain the results
         */
        EVALUATEMEASURE, 
        /**
         * Return all the related information as described in the Encounter or Patient
         */
        EVERYTHING, 
        /**
         * Value Set Expansion
         */
        EXPAND, 
        /**
         * Find a functional list
         */
        FIND, 
        /**
         * Invoke a GraphQL query
         */
        GRAPHQL, 
        /**
         * Test if a server implements a client's required operations
         */
        IMPLEMENTS, 
        /**
         * Last N Observations Query
         */
        LASTN, 
        /**
         * Concept Look Up and Decomposition
         */
        LOOKUP, 
        /**
         * Find patient matches using MPI based logic
         */
        MATCH, 
        /**
         * Access a list of profiles, tags, and security labels
         */
        META, 
        /**
         * Add profiles, tags, and security labels to a resource
         */
        METAADD, 
        /**
         * Delete profiles, tags, and security labels for a resource
         */
        METADELETE, 
        /**
         * Populate Questionnaire
         */
        POPULATE, 
        /**
         * Generate HTML for Questionnaire
         */
        POPULATEHTML, 
        /**
         * Generate a link to a Questionnaire completion webpage
         */
        POPULATELINK, 
        /**
         * Process a message according to the defined event
         */
        PROCESSMESSAGE, 
        /**
         * Build Questionnaire
         */
        QUESTIONNAIRE, 
        /**
         * Observation Statistics
         */
        STATS, 
        /**
         * Fetch a subset of the CapabilityStatement resource
         */
        SUBSET, 
        /**
         * CodeSystem Subsumption Testing
         */
        SUBSUMES, 
        /**
         * Model Instance Transformation
         */
        TRANSFORM, 
        /**
         * Concept Translation
         */
        TRANSLATE, 
        /**
         * Validate a resource
         */
        VALIDATE, 
        /**
         * ValueSet based Validation
         */
        VALIDATECODE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static TestscriptOperationCodes fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("read".equals(codeString))
          return READ;
        if ("vread".equals(codeString))
          return VREAD;
        if ("update".equals(codeString))
          return UPDATE;
        if ("updateCreate".equals(codeString))
          return UPDATECREATE;
        if ("patch".equals(codeString))
          return PATCH;
        if ("delete".equals(codeString))
          return DELETE;
        if ("deleteCondSingle".equals(codeString))
          return DELETECONDSINGLE;
        if ("deleteCondMultiple".equals(codeString))
          return DELETECONDMULTIPLE;
        if ("history".equals(codeString))
          return HISTORY;
        if ("create".equals(codeString))
          return CREATE;
        if ("search".equals(codeString))
          return SEARCH;
        if ("batch".equals(codeString))
          return BATCH;
        if ("transaction".equals(codeString))
          return TRANSACTION;
        if ("capabilities".equals(codeString))
          return CAPABILITIES;
        if ("apply".equals(codeString))
          return APPLY;
        if ("closure".equals(codeString))
          return CLOSURE;
        if ("find-matches".equals(codeString))
          return FINDMATCHES;
        if ("conforms".equals(codeString))
          return CONFORMS;
        if ("data-requirements".equals(codeString))
          return DATAREQUIREMENTS;
        if ("document".equals(codeString))
          return DOCUMENT;
        if ("evaluate".equals(codeString))
          return EVALUATE;
        if ("evaluate-measure".equals(codeString))
          return EVALUATEMEASURE;
        if ("everything".equals(codeString))
          return EVERYTHING;
        if ("expand".equals(codeString))
          return EXPAND;
        if ("find".equals(codeString))
          return FIND;
        if ("graphql".equals(codeString))
          return GRAPHQL;
        if ("implements".equals(codeString))
          return IMPLEMENTS;
        if ("lastn".equals(codeString))
          return LASTN;
        if ("lookup".equals(codeString))
          return LOOKUP;
        if ("match".equals(codeString))
          return MATCH;
        if ("meta".equals(codeString))
          return META;
        if ("meta-add".equals(codeString))
          return METAADD;
        if ("meta-delete".equals(codeString))
          return METADELETE;
        if ("populate".equals(codeString))
          return POPULATE;
        if ("populatehtml".equals(codeString))
          return POPULATEHTML;
        if ("populatelink".equals(codeString))
          return POPULATELINK;
        if ("process-message".equals(codeString))
          return PROCESSMESSAGE;
        if ("questionnaire".equals(codeString))
          return QUESTIONNAIRE;
        if ("stats".equals(codeString))
          return STATS;
        if ("subset".equals(codeString))
          return SUBSET;
        if ("subsumes".equals(codeString))
          return SUBSUMES;
        if ("transform".equals(codeString))
          return TRANSFORM;
        if ("translate".equals(codeString))
          return TRANSLATE;
        if ("validate".equals(codeString))
          return VALIDATE;
        if ("validate-code".equals(codeString))
          return VALIDATECODE;
        throw new FHIRException("Unknown TestscriptOperationCodes code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case READ: return "read";
            case VREAD: return "vread";
            case UPDATE: return "update";
            case UPDATECREATE: return "updateCreate";
            case PATCH: return "patch";
            case DELETE: return "delete";
            case DELETECONDSINGLE: return "deleteCondSingle";
            case DELETECONDMULTIPLE: return "deleteCondMultiple";
            case HISTORY: return "history";
            case CREATE: return "create";
            case SEARCH: return "search";
            case BATCH: return "batch";
            case TRANSACTION: return "transaction";
            case CAPABILITIES: return "capabilities";
            case APPLY: return "apply";
            case CLOSURE: return "closure";
            case FINDMATCHES: return "find-matches";
            case CONFORMS: return "conforms";
            case DATAREQUIREMENTS: return "data-requirements";
            case DOCUMENT: return "document";
            case EVALUATE: return "evaluate";
            case EVALUATEMEASURE: return "evaluate-measure";
            case EVERYTHING: return "everything";
            case EXPAND: return "expand";
            case FIND: return "find";
            case GRAPHQL: return "graphql";
            case IMPLEMENTS: return "implements";
            case LASTN: return "lastn";
            case LOOKUP: return "lookup";
            case MATCH: return "match";
            case META: return "meta";
            case METAADD: return "meta-add";
            case METADELETE: return "meta-delete";
            case POPULATE: return "populate";
            case POPULATEHTML: return "populatehtml";
            case POPULATELINK: return "populatelink";
            case PROCESSMESSAGE: return "process-message";
            case QUESTIONNAIRE: return "questionnaire";
            case STATS: return "stats";
            case SUBSET: return "subset";
            case SUBSUMES: return "subsumes";
            case TRANSFORM: return "transform";
            case TRANSLATE: return "translate";
            case VALIDATE: return "validate";
            case VALIDATECODE: return "validate-code";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/testscript-operation-codes";
        }
        public String getDefinition() {
          switch (this) {
            case READ: return "Read the current state of the resource.";
            case VREAD: return "Read the state of a specific version of the resource.";
            case UPDATE: return "Update an existing resource by its id.";
            case UPDATECREATE: return "Update an existing resource by its id (or create it if it is new).";
            case PATCH: return "Patch an existing resource by its id.";
            case DELETE: return "Delete a resource.";
            case DELETECONDSINGLE: return "Conditionally delete a single resource based on search parameters.";
            case DELETECONDMULTIPLE: return "Conditionally delete one or more resources based on search parameters.";
            case HISTORY: return "Retrieve the change history for a particular resource or resource type.";
            case CREATE: return "Create a new resource with a server assigned id.";
            case SEARCH: return "Search based on some filter criteria.";
            case BATCH: return "Update, create or delete a set of resources as independent actions.";
            case TRANSACTION: return "Update, create or delete a set of resources as a single transaction.";
            case CAPABILITIES: return "Get a capability statement for the system.";
            case APPLY: return "Realizes an ActivityDefinition in a specific context";
            case CLOSURE: return "Closure Table Maintenance";
            case FINDMATCHES: return "Finding Codes based on supplied properties";
            case CONFORMS: return "Compare two systems CapabilityStatements";
            case DATAREQUIREMENTS: return "Aggregates and returns the parameters and data requirements for a resource and all its dependencies as a single module definition";
            case DOCUMENT: return "Generate a Document";
            case EVALUATE: return "Request clinical decision support guidance based on a specific decision support module";
            case EVALUATEMEASURE: return "Invoke an eMeasure and obtain the results";
            case EVERYTHING: return "Return all the related information as described in the Encounter or Patient";
            case EXPAND: return "Value Set Expansion";
            case FIND: return "Find a functional list";
            case GRAPHQL: return "Invoke a GraphQL query";
            case IMPLEMENTS: return "Test if a server implements a client's required operations";
            case LASTN: return "Last N Observations Query";
            case LOOKUP: return "Concept Look Up and Decomposition";
            case MATCH: return "Find patient matches using MPI based logic";
            case META: return "Access a list of profiles, tags, and security labels";
            case METAADD: return "Add profiles, tags, and security labels to a resource";
            case METADELETE: return "Delete profiles, tags, and security labels for a resource";
            case POPULATE: return "Populate Questionnaire";
            case POPULATEHTML: return "Generate HTML for Questionnaire";
            case POPULATELINK: return "Generate a link to a Questionnaire completion webpage";
            case PROCESSMESSAGE: return "Process a message according to the defined event";
            case QUESTIONNAIRE: return "Build Questionnaire";
            case STATS: return "Observation Statistics";
            case SUBSET: return "Fetch a subset of the CapabilityStatement resource";
            case SUBSUMES: return "CodeSystem Subsumption Testing";
            case TRANSFORM: return "Model Instance Transformation";
            case TRANSLATE: return "Concept Translation";
            case VALIDATE: return "Validate a resource";
            case VALIDATECODE: return "ValueSet based Validation";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case READ: return "Read";
            case VREAD: return "Version Read";
            case UPDATE: return "Update";
            case UPDATECREATE: return "Create using Update";
            case PATCH: return "Patch";
            case DELETE: return "Delete";
            case DELETECONDSINGLE: return "Conditional Delete Single";
            case DELETECONDMULTIPLE: return "Conditional Delete Multiple";
            case HISTORY: return "History";
            case CREATE: return "Create";
            case SEARCH: return "Search";
            case BATCH: return "Batch";
            case TRANSACTION: return "Transaction";
            case CAPABILITIES: return "Capabilities";
            case APPLY: return "$apply";
            case CLOSURE: return "$closure";
            case FINDMATCHES: return "$find-matches";
            case CONFORMS: return "$conforms";
            case DATAREQUIREMENTS: return "$data-requirements";
            case DOCUMENT: return "$document";
            case EVALUATE: return "$evaluate";
            case EVALUATEMEASURE: return "$evaluate-measure";
            case EVERYTHING: return "$everything";
            case EXPAND: return "$expand";
            case FIND: return "$find";
            case GRAPHQL: return "$graphql";
            case IMPLEMENTS: return "$implements";
            case LASTN: return "$lastn";
            case LOOKUP: return "$lookup";
            case MATCH: return "$match";
            case META: return "$meta";
            case METAADD: return "$meta-add";
            case METADELETE: return "$meta-delete";
            case POPULATE: return "$populate";
            case POPULATEHTML: return "$populatehtml";
            case POPULATELINK: return "$populatelink";
            case PROCESSMESSAGE: return "$process-message";
            case QUESTIONNAIRE: return "$questionnaire";
            case STATS: return "$stats";
            case SUBSET: return "$subset";
            case SUBSUMES: return "$subsumes";
            case TRANSFORM: return "$transform";
            case TRANSLATE: return "$translate";
            case VALIDATE: return "$validate";
            case VALIDATECODE: return "$validate-code";
            default: return "?";
          }
    }


}

