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


import org.hl7.fhir.dstu3.model.EnumFactory;

public class TestscriptOperationCodesEnumFactory implements EnumFactory<TestscriptOperationCodes> {

  public TestscriptOperationCodes fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("read".equals(codeString))
      return TestscriptOperationCodes.READ;
    if ("vread".equals(codeString))
      return TestscriptOperationCodes.VREAD;
    if ("update".equals(codeString))
      return TestscriptOperationCodes.UPDATE;
    if ("updateCreate".equals(codeString))
      return TestscriptOperationCodes.UPDATECREATE;
    if ("delete".equals(codeString))
      return TestscriptOperationCodes.DELETE;
    if ("deleteCondSingle".equals(codeString))
      return TestscriptOperationCodes.DELETECONDSINGLE;
    if ("deleteCondMultiple".equals(codeString))
      return TestscriptOperationCodes.DELETECONDMULTIPLE;
    if ("history".equals(codeString))
      return TestscriptOperationCodes.HISTORY;
    if ("create".equals(codeString))
      return TestscriptOperationCodes.CREATE;
    if ("search".equals(codeString))
      return TestscriptOperationCodes.SEARCH;
    if ("batch".equals(codeString))
      return TestscriptOperationCodes.BATCH;
    if ("transaction".equals(codeString))
      return TestscriptOperationCodes.TRANSACTION;
    if ("capabilities".equals(codeString))
      return TestscriptOperationCodes.CAPABILITIES;
    if ("apply".equals(codeString))
      return TestscriptOperationCodes.APPLY;
    if ("cancel".equals(codeString))
      return TestscriptOperationCodes.CANCEL;
    if ("closure".equals(codeString))
      return TestscriptOperationCodes.CLOSURE;
    if ("compose".equals(codeString))
      return TestscriptOperationCodes.COMPOSE;
    if ("conforms".equals(codeString))
      return TestscriptOperationCodes.CONFORMS;
    if ("data-requirements".equals(codeString))
      return TestscriptOperationCodes.DATAREQUIREMENTS;
    if ("document".equals(codeString))
      return TestscriptOperationCodes.DOCUMENT;
    if ("evaluate".equals(codeString))
      return TestscriptOperationCodes.EVALUATE;
    if ("evaluate-measure".equals(codeString))
      return TestscriptOperationCodes.EVALUATEMEASURE;
    if ("everything".equals(codeString))
      return TestscriptOperationCodes.EVERYTHING;
    if ("expand".equals(codeString))
      return TestscriptOperationCodes.EXPAND;
    if ("fail".equals(codeString))
      return TestscriptOperationCodes.FAIL;
    if ("find".equals(codeString))
      return TestscriptOperationCodes.FIND;
    if ("finish".equals(codeString))
      return TestscriptOperationCodes.FINISH;
    if ("implements".equals(codeString))
      return TestscriptOperationCodes.IMPLEMENTS;
    if ("lookup".equals(codeString))
      return TestscriptOperationCodes.LOOKUP;
    if ("match".equals(codeString))
      return TestscriptOperationCodes.MATCH;
    if ("meta".equals(codeString))
      return TestscriptOperationCodes.META;
    if ("meta-add".equals(codeString))
      return TestscriptOperationCodes.METAADD;
    if ("meta-delete".equals(codeString))
      return TestscriptOperationCodes.METADELETE;
    if ("populate".equals(codeString))
      return TestscriptOperationCodes.POPULATE;
    if ("populatehtml".equals(codeString))
      return TestscriptOperationCodes.POPULATEHTML;
    if ("populatelink".equals(codeString))
      return TestscriptOperationCodes.POPULATELINK;
    if ("process-message".equals(codeString))
      return TestscriptOperationCodes.PROCESSMESSAGE;
    if ("questionnaire".equals(codeString))
      return TestscriptOperationCodes.QUESTIONNAIRE;
    if ("release".equals(codeString))
      return TestscriptOperationCodes.RELEASE;
    if ("reserve".equals(codeString))
      return TestscriptOperationCodes.RESERVE;
    if ("resume".equals(codeString))
      return TestscriptOperationCodes.RESUME;
    if ("set-input".equals(codeString))
      return TestscriptOperationCodes.SETINPUT;
    if ("set-output".equals(codeString))
      return TestscriptOperationCodes.SETOUTPUT;
    if ("start".equals(codeString))
      return TestscriptOperationCodes.START;
    if ("stats".equals(codeString))
      return TestscriptOperationCodes.STATS;
    if ("stop".equals(codeString))
      return TestscriptOperationCodes.STOP;
    if ("subset".equals(codeString))
      return TestscriptOperationCodes.SUBSET;
    if ("subsumes".equals(codeString))
      return TestscriptOperationCodes.SUBSUMES;
    if ("suspend".equals(codeString))
      return TestscriptOperationCodes.SUSPEND;
    if ("transform".equals(codeString))
      return TestscriptOperationCodes.TRANSFORM;
    if ("translate".equals(codeString))
      return TestscriptOperationCodes.TRANSLATE;
    if ("validate".equals(codeString))
      return TestscriptOperationCodes.VALIDATE;
    if ("validate-code".equals(codeString))
      return TestscriptOperationCodes.VALIDATECODE;
    throw new IllegalArgumentException("Unknown TestscriptOperationCodes code '"+codeString+"'");
  }

  public String toCode(TestscriptOperationCodes code) {
    if (code == TestscriptOperationCodes.READ)
      return "read";
    if (code == TestscriptOperationCodes.VREAD)
      return "vread";
    if (code == TestscriptOperationCodes.UPDATE)
      return "update";
    if (code == TestscriptOperationCodes.UPDATECREATE)
      return "updateCreate";
    if (code == TestscriptOperationCodes.DELETE)
      return "delete";
    if (code == TestscriptOperationCodes.DELETECONDSINGLE)
      return "deleteCondSingle";
    if (code == TestscriptOperationCodes.DELETECONDMULTIPLE)
      return "deleteCondMultiple";
    if (code == TestscriptOperationCodes.HISTORY)
      return "history";
    if (code == TestscriptOperationCodes.CREATE)
      return "create";
    if (code == TestscriptOperationCodes.SEARCH)
      return "search";
    if (code == TestscriptOperationCodes.BATCH)
      return "batch";
    if (code == TestscriptOperationCodes.TRANSACTION)
      return "transaction";
    if (code == TestscriptOperationCodes.CAPABILITIES)
      return "capabilities";
    if (code == TestscriptOperationCodes.APPLY)
      return "apply";
    if (code == TestscriptOperationCodes.CANCEL)
      return "cancel";
    if (code == TestscriptOperationCodes.CLOSURE)
      return "closure";
    if (code == TestscriptOperationCodes.COMPOSE)
      return "compose";
    if (code == TestscriptOperationCodes.CONFORMS)
      return "conforms";
    if (code == TestscriptOperationCodes.DATAREQUIREMENTS)
      return "data-requirements";
    if (code == TestscriptOperationCodes.DOCUMENT)
      return "document";
    if (code == TestscriptOperationCodes.EVALUATE)
      return "evaluate";
    if (code == TestscriptOperationCodes.EVALUATEMEASURE)
      return "evaluate-measure";
    if (code == TestscriptOperationCodes.EVERYTHING)
      return "everything";
    if (code == TestscriptOperationCodes.EXPAND)
      return "expand";
    if (code == TestscriptOperationCodes.FAIL)
      return "fail";
    if (code == TestscriptOperationCodes.FIND)
      return "find";
    if (code == TestscriptOperationCodes.FINISH)
      return "finish";
    if (code == TestscriptOperationCodes.IMPLEMENTS)
      return "implements";
    if (code == TestscriptOperationCodes.LOOKUP)
      return "lookup";
    if (code == TestscriptOperationCodes.MATCH)
      return "match";
    if (code == TestscriptOperationCodes.META)
      return "meta";
    if (code == TestscriptOperationCodes.METAADD)
      return "meta-add";
    if (code == TestscriptOperationCodes.METADELETE)
      return "meta-delete";
    if (code == TestscriptOperationCodes.POPULATE)
      return "populate";
    if (code == TestscriptOperationCodes.POPULATEHTML)
      return "populatehtml";
    if (code == TestscriptOperationCodes.POPULATELINK)
      return "populatelink";
    if (code == TestscriptOperationCodes.PROCESSMESSAGE)
      return "process-message";
    if (code == TestscriptOperationCodes.QUESTIONNAIRE)
      return "questionnaire";
    if (code == TestscriptOperationCodes.RELEASE)
      return "release";
    if (code == TestscriptOperationCodes.RESERVE)
      return "reserve";
    if (code == TestscriptOperationCodes.RESUME)
      return "resume";
    if (code == TestscriptOperationCodes.SETINPUT)
      return "set-input";
    if (code == TestscriptOperationCodes.SETOUTPUT)
      return "set-output";
    if (code == TestscriptOperationCodes.START)
      return "start";
    if (code == TestscriptOperationCodes.STATS)
      return "stats";
    if (code == TestscriptOperationCodes.STOP)
      return "stop";
    if (code == TestscriptOperationCodes.SUBSET)
      return "subset";
    if (code == TestscriptOperationCodes.SUBSUMES)
      return "subsumes";
    if (code == TestscriptOperationCodes.SUSPEND)
      return "suspend";
    if (code == TestscriptOperationCodes.TRANSFORM)
      return "transform";
    if (code == TestscriptOperationCodes.TRANSLATE)
      return "translate";
    if (code == TestscriptOperationCodes.VALIDATE)
      return "validate";
    if (code == TestscriptOperationCodes.VALIDATECODE)
      return "validate-code";
    return "?";
  }

    public String toSystem(TestscriptOperationCodes code) {
      return code.getSystem();
      }

}

