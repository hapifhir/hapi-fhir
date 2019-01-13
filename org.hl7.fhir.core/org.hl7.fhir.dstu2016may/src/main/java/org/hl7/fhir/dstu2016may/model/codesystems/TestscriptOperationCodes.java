package org.hl7.fhir.dstu2016may.model.codesystems;

/*-
 * #%L
 * org.hl7.fhir.dstu2016may
 * %%
 * Copyright (C) 2014 - 2019 Health Level 7
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

// Generated on Sun, May 8, 2016 03:05+1000 for FHIR v1.4.0


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
         * Update an existing resource by its id (or create it if it is new).
         */
        UPDATE, 
        /**
         * Delete a resource.
         */
        DELETE, 
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
         * Get a conformance statement for the system.
         */
        CONFORMANCE, 
        /**
         * Cancel Task
         */
        CANCEL, 
        /**
         * The CDS Hook operation is the core API request for CDS Hooks
         */
        CDSHOOK, 
        /**
         * Closure Table Maintenance
         */
        CLOSURE, 
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
         * Place OrderSet
         */
        PLACE, 
        /**
         * Populate Questionnaire
         */
        POPULATE, 
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
         * Stop Task
         */
        STOP, 
        /**
         * Suspend Task
         */
        SUSPEND, 
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
        if ("delete".equals(codeString))
          return DELETE;
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
        if ("conformance".equals(codeString))
          return CONFORMANCE;
        if ("cancel".equals(codeString))
          return CANCEL;
        if ("cds-hook".equals(codeString))
          return CDSHOOK;
        if ("closure".equals(codeString))
          return CLOSURE;
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
        if ("place".equals(codeString))
          return PLACE;
        if ("populate".equals(codeString))
          return POPULATE;
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
        if ("stop".equals(codeString))
          return STOP;
        if ("suspend".equals(codeString))
          return SUSPEND;
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
            case DELETE: return "delete";
            case HISTORY: return "history";
            case CREATE: return "create";
            case SEARCH: return "search";
            case BATCH: return "batch";
            case TRANSACTION: return "transaction";
            case CONFORMANCE: return "conformance";
            case CANCEL: return "cancel";
            case CDSHOOK: return "cds-hook";
            case CLOSURE: return "closure";
            case DOCUMENT: return "document";
            case EVALUATE: return "evaluate";
            case EVALUATEMEASURE: return "evaluate-measure";
            case EVERYTHING: return "everything";
            case EXPAND: return "expand";
            case FAIL: return "fail";
            case FIND: return "find";
            case FINISH: return "finish";
            case LOOKUP: return "lookup";
            case MATCH: return "match";
            case META: return "meta";
            case METAADD: return "meta-add";
            case METADELETE: return "meta-delete";
            case PLACE: return "place";
            case POPULATE: return "populate";
            case PROCESSMESSAGE: return "process-message";
            case QUESTIONNAIRE: return "questionnaire";
            case RELEASE: return "release";
            case RESERVE: return "reserve";
            case RESUME: return "resume";
            case SETINPUT: return "set-input";
            case SETOUTPUT: return "set-output";
            case START: return "start";
            case STOP: return "stop";
            case SUSPEND: return "suspend";
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
            case HISTORY: return "Retrieve the change history for a particular resource or resource type.";
            case CREATE: return "Create a new resource with a server assigned id.";
            case SEARCH: return "Search based on some filter criteria.";
            case BATCH: return "Update, create or delete a set of resources as independent actions.";
            case TRANSACTION: return "Update, create or delete a set of resources as a single transaction.";
            case CONFORMANCE: return "Get a conformance statement for the system.";
            case CANCEL: return "Cancel Task";
            case CDSHOOK: return "The CDS Hook operation is the core API request for CDS Hooks";
            case CLOSURE: return "Closure Table Maintenance";
            case DOCUMENT: return "Generate a Document";
            case EVALUATE: return "Evaluate DecisionSupportRule / DecisionSupportServiceModule";
            case EVALUATEMEASURE: return "Evaluate Measure";
            case EVERYTHING: return "Fetch Encounter/Patient Record";
            case EXPAND: return "Value Set Expansion";
            case FAIL: return "Fail Task";
            case FIND: return "Find a functional list";
            case FINISH: return "Finish Task";
            case LOOKUP: return "Concept Look Up";
            case MATCH: return "Find patient matches using MPI based logic";
            case META: return "Access a list of profiles, tags, and security labels";
            case METAADD: return "Add profiles, tags, and security labels to a resource";
            case METADELETE: return "Delete profiles, tags, and security labels for a resource";
            case PLACE: return "Place OrderSet";
            case POPULATE: return "Populate Questionnaire";
            case PROCESSMESSAGE: return "Process Message";
            case QUESTIONNAIRE: return "Build Questionnaire";
            case RELEASE: return "Release Task";
            case RESERVE: return "Reserve Task";
            case RESUME: return "Resume Task";
            case SETINPUT: return "Set Task Input";
            case SETOUTPUT: return "Set Task Output";
            case START: return "Start Task";
            case STOP: return "Stop Task";
            case SUSPEND: return "Suspend Task";
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
            case DELETE: return "Delete";
            case HISTORY: return "History";
            case CREATE: return "Create";
            case SEARCH: return "Search";
            case BATCH: return "Batch";
            case TRANSACTION: return "Transaction";
            case CONFORMANCE: return "Conformance";
            case CANCEL: return "$cancel";
            case CDSHOOK: return "$cds-hook";
            case CLOSURE: return "$closure";
            case DOCUMENT: return "$document";
            case EVALUATE: return "$evaluate";
            case EVALUATEMEASURE: return "$evaluate-measure";
            case EVERYTHING: return "$everything";
            case EXPAND: return "$expand";
            case FAIL: return "$fail";
            case FIND: return "$find";
            case FINISH: return "$finish";
            case LOOKUP: return "$lookup";
            case MATCH: return "$match";
            case META: return "$meta";
            case METAADD: return "$meta-add";
            case METADELETE: return "$meta-delete";
            case PLACE: return "$place";
            case POPULATE: return "$populate";
            case PROCESSMESSAGE: return "$process-message";
            case QUESTIONNAIRE: return "$questionnaire";
            case RELEASE: return "$release";
            case RESERVE: return "$reserve";
            case RESUME: return "$resume";
            case SETINPUT: return "$set-input";
            case SETOUTPUT: return "$set-output";
            case START: return "$start";
            case STOP: return "$stop";
            case SUSPEND: return "$suspend";
            case TRANSLATE: return "$translate";
            case VALIDATE: return "$validate";
            case VALIDATECODE: return "$validate-code";
            default: return "?";
          }
    }


}

