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


import org.hl7.fhir.dstu2016may.model.EnumFactory;

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
    if ("delete".equals(codeString))
      return TestscriptOperationCodes.DELETE;
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
    if ("conformance".equals(codeString))
      return TestscriptOperationCodes.CONFORMANCE;
    if ("cancel".equals(codeString))
      return TestscriptOperationCodes.CANCEL;
    if ("cds-hook".equals(codeString))
      return TestscriptOperationCodes.CDSHOOK;
    if ("closure".equals(codeString))
      return TestscriptOperationCodes.CLOSURE;
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
    if ("place".equals(codeString))
      return TestscriptOperationCodes.PLACE;
    if ("populate".equals(codeString))
      return TestscriptOperationCodes.POPULATE;
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
    if ("stop".equals(codeString))
      return TestscriptOperationCodes.STOP;
    if ("suspend".equals(codeString))
      return TestscriptOperationCodes.SUSPEND;
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
    if (code == TestscriptOperationCodes.DELETE)
      return "delete";
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
    if (code == TestscriptOperationCodes.CONFORMANCE)
      return "conformance";
    if (code == TestscriptOperationCodes.CANCEL)
      return "cancel";
    if (code == TestscriptOperationCodes.CDSHOOK)
      return "cds-hook";
    if (code == TestscriptOperationCodes.CLOSURE)
      return "closure";
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
    if (code == TestscriptOperationCodes.PLACE)
      return "place";
    if (code == TestscriptOperationCodes.POPULATE)
      return "populate";
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
    if (code == TestscriptOperationCodes.STOP)
      return "stop";
    if (code == TestscriptOperationCodes.SUSPEND)
      return "suspend";
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

