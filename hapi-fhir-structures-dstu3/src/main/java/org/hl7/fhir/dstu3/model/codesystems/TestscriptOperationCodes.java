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
         * Realize a definition in a specific context
         */
        APPLY, 
        /**
         * Cancel Task
         */
        CANCEL, 
        /**
         * Closure Table Maintenance
         */
        CLOSURE, 
        /**
         * Code Composition based on supplied properties
         */
        COMPOSE, 
        /**
         * Test if a server implements a client's required operations
         */
        CONFORMS, 
        /**
         * Aggregates and return the parameters and data requirements as a single module definition library
         */
        DATAREQUIREMENTS, 
        /**
         * Generate a Document
         */
        DOCUMENT, 
        /**
         * Evaluate DecisionSupportRule / DecisionSupportServiceModule
         */
        EVALUATE, 
        /**
         * Evaluate Measure
         */
        EVALUATEMEASURE, 
        /**
         * Fetch Encounter/Patient Record
         */
        EVERYTHING, 
        /**
         * Value Set Expansion
         */
        EXPAND, 
        /**
         * Fail Task
         */
        FAIL, 
        /**
         * Find a functional list
         */
        FIND, 
        /**
         * Finish Task
         */
        FINISH, 
        /**
         * Test if a server implements a client's required operations
         */
        IMPLEMENTS, 
        /**
         * Concept Look Up
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
         * Process Message
         */
        PROCESSMESSAGE, 
        /**
         * Build Questionnaire
         */
        QUESTIONNAIRE, 
        /**
         * Release Task
         */
        RELEASE, 
        /**
         * Reserve Task
         */
        RESERVE, 
        /**
         * Resume Task
         */
        RESUME, 
        /**
         * Set Task Input
         */
        SETINPUT, 
        /**
         * Set Task Output
         */
        SETOUTPUT, 
        /**
         * Start Task
         */
        START, 
        /**
         * Observation Statistics
         */
        STATS, 
        /**
         * Stop Task
         */
        STOP, 
        /**
         * Fetch a subset of the CapabilityStatement resource
         */
        SUBSET, 
        /**
         * Determine if code A is subsumed by code B
         */
        SUBSUMES, 
        /**
         * Suspend Task
         */
        SUSPEND, 
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
         * Value Set based Validation
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
        if ("cancel".equals(codeString))
          return CANCEL;
        if ("closure".equals(codeString))
          return CLOSURE;
        if ("compose".equals(codeString))
          return COMPOSE;
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
        if ("fail".equals(codeString))
          return FAIL;
        if ("find".equals(codeString))
          return FIND;
        if ("finish".equals(codeString))
          return FINISH;
        if ("implements".equals(codeString))
          return IMPLEMENTS;
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
        if ("release".equals(codeString))
          return RELEASE;
        if ("reserve".equals(codeString))
          return RESERVE;
        if ("resume".equals(codeString))
          return RESUME;
        if ("set-input".equals(codeString))
          return SETINPUT;
        if ("set-output".equals(codeString))
          return SETOUTPUT;
        if ("start".equals(codeString))
          return START;
        if ("stats".equals(codeString))
          return STATS;
        if ("stop".equals(codeString))
          return STOP;
        if ("subset".equals(codeString))
          return SUBSET;
        if ("subsumes".equals(codeString))
          return SUBSUMES;
        if ("suspend".equals(codeString))
          return SUSPEND;
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
            case CANCEL: return "cancel";
            case CLOSURE: return "closure";
            case COMPOSE: return "compose";
            case CONFORMS: return "conforms";
            case DATAREQUIREMENTS: return "data-requirements";
            case DOCUMENT: return "document";
            case EVALUATE: return "evaluate";
            case EVALUATEMEASURE: return "evaluate-measure";
            case EVERYTHING: return "everything";
            case EXPAND: return "expand";
            case FAIL: return "fail";
            case FIND: return "find";
            case FINISH: return "finish";
            case IMPLEMENTS: return "implements";
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
            case RELEASE: return "release";
            case RESERVE: return "reserve";
            case RESUME: return "resume";
            case SETINPUT: return "set-input";
            case SETOUTPUT: return "set-output";
            case START: return "start";
            case STATS: return "stats";
            case STOP: return "stop";
            case SUBSET: return "subset";
            case SUBSUMES: return "subsumes";
            case SUSPEND: return "suspend";
            case TRANSFORM: return "transform";
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
            case UPDATE: return "Update an existing resource by its id.";
            case UPDATECREATE: return "Update an existing resource by its id (or create it if it is new).";
            case DELETE: return "Delete a resource.";
            case DELETECONDSINGLE: return "Conditionally delete a single resource based on search parameters.";
            case DELETECONDMULTIPLE: return "Conditionally delete one or more resources based on search parameters.";
            case HISTORY: return "Retrieve the change history for a particular resource or resource type.";
            case CREATE: return "Create a new resource with a server assigned id.";
            case SEARCH: return "Search based on some filter criteria.";
            case BATCH: return "Update, create or delete a set of resources as independent actions.";
            case TRANSACTION: return "Update, create or delete a set of resources as a single transaction.";
            case CAPABILITIES: return "Get a capability statement for the system.";
            case APPLY: return "Realize a definition in a specific context";
            case CANCEL: return "Cancel Task";
            case CLOSURE: return "Closure Table Maintenance";
            case COMPOSE: return "Code Composition based on supplied properties";
            case CONFORMS: return "Test if a server implements a client's required operations";
            case DATAREQUIREMENTS: return "Aggregates and return the parameters and data requirements as a single module definition library";
            case DOCUMENT: return "Generate a Document";
            case EVALUATE: return "Evaluate DecisionSupportRule / DecisionSupportServiceModule";
            case EVALUATEMEASURE: return "Evaluate Measure";
            case EVERYTHING: return "Fetch Encounter/Patient Record";
            case EXPAND: return "Value Set Expansion";
            case FAIL: return "Fail Task";
            case FIND: return "Find a functional list";
            case FINISH: return "Finish Task";
            case IMPLEMENTS: return "Test if a server implements a client's required operations";
            case LOOKUP: return "Concept Look Up";
            case MATCH: return "Find patient matches using MPI based logic";
            case META: return "Access a list of profiles, tags, and security labels";
            case METAADD: return "Add profiles, tags, and security labels to a resource";
            case METADELETE: return "Delete profiles, tags, and security labels for a resource";
            case POPULATE: return "Populate Questionnaire";
            case POPULATEHTML: return "Generate HTML for Questionnaire";
            case POPULATELINK: return "Generate a link to a Questionnaire completion webpage";
            case PROCESSMESSAGE: return "Process Message";
            case QUESTIONNAIRE: return "Build Questionnaire";
            case RELEASE: return "Release Task";
            case RESERVE: return "Reserve Task";
            case RESUME: return "Resume Task";
            case SETINPUT: return "Set Task Input";
            case SETOUTPUT: return "Set Task Output";
            case START: return "Start Task";
            case STATS: return "Observation Statistics";
            case STOP: return "Stop Task";
            case SUBSET: return "Fetch a subset of the CapabilityStatement resource";
            case SUBSUMES: return "Determine if code A is subsumed by code B";
            case SUSPEND: return "Suspend Task";
            case TRANSFORM: return "Model Instance Transformation";
            case TRANSLATE: return "Concept Translation";
            case VALIDATE: return "Validate a resource";
            case VALIDATECODE: return "Value Set based Validation";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case READ: return "Read";
            case VREAD: return "Version Read";
            case UPDATE: return "Update";
            case UPDATECREATE: return "Create using Update";
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
            case CANCEL: return "$cancel";
            case CLOSURE: return "$closure";
            case COMPOSE: return "$compose";
            case CONFORMS: return "$conforms";
            case DATAREQUIREMENTS: return "$data-requirements";
            case DOCUMENT: return "$document";
            case EVALUATE: return "$evaluate";
            case EVALUATEMEASURE: return "$evaluate-measure";
            case EVERYTHING: return "$everything";
            case EXPAND: return "$expand";
            case FAIL: return "$fail";
            case FIND: return "$find";
            case FINISH: return "$finish";
            case IMPLEMENTS: return "$implements";
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
            case RELEASE: return "$release";
            case RESERVE: return "$reserve";
            case RESUME: return "$resume";
            case SETINPUT: return "$set-input";
            case SETOUTPUT: return "$set-output";
            case START: return "$start";
            case STATS: return "$stats";
            case STOP: return "$stop";
            case SUBSET: return "$subset";
            case SUBSUMES: return "$subsumes";
            case SUSPEND: return "$suspend";
            case TRANSFORM: return "$transform";
            case TRANSLATE: return "$translate";
            case VALIDATE: return "$validate";
            case VALIDATECODE: return "$validate-code";
            default: return "?";
          }
    }


}

