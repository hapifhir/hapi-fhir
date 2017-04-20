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

public class RestfulInteractionEnumFactory implements EnumFactory<RestfulInteraction> {

  public RestfulInteraction fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("read".equals(codeString))
      return RestfulInteraction.READ;
    if ("vread".equals(codeString))
      return RestfulInteraction.VREAD;
    if ("update".equals(codeString))
      return RestfulInteraction.UPDATE;
    if ("patch".equals(codeString))
      return RestfulInteraction.PATCH;
    if ("delete".equals(codeString))
      return RestfulInteraction.DELETE;
    if ("history".equals(codeString))
      return RestfulInteraction.HISTORY;
    if ("history-instance".equals(codeString))
      return RestfulInteraction.HISTORYINSTANCE;
    if ("history-type".equals(codeString))
      return RestfulInteraction.HISTORYTYPE;
    if ("history-system".equals(codeString))
      return RestfulInteraction.HISTORYSYSTEM;
    if ("create".equals(codeString))
      return RestfulInteraction.CREATE;
    if ("search".equals(codeString))
      return RestfulInteraction.SEARCH;
    if ("search-type".equals(codeString))
      return RestfulInteraction.SEARCHTYPE;
    if ("search-system".equals(codeString))
      return RestfulInteraction.SEARCHSYSTEM;
    if ("capabilities".equals(codeString))
      return RestfulInteraction.CAPABILITIES;
    if ("transaction".equals(codeString))
      return RestfulInteraction.TRANSACTION;
    if ("batch".equals(codeString))
      return RestfulInteraction.BATCH;
    if ("operation".equals(codeString))
      return RestfulInteraction.OPERATION;
    throw new IllegalArgumentException("Unknown RestfulInteraction code '"+codeString+"'");
  }

  public String toCode(RestfulInteraction code) {
    if (code == RestfulInteraction.READ)
      return "read";
    if (code == RestfulInteraction.VREAD)
      return "vread";
    if (code == RestfulInteraction.UPDATE)
      return "update";
    if (code == RestfulInteraction.PATCH)
      return "patch";
    if (code == RestfulInteraction.DELETE)
      return "delete";
    if (code == RestfulInteraction.HISTORY)
      return "history";
    if (code == RestfulInteraction.HISTORYINSTANCE)
      return "history-instance";
    if (code == RestfulInteraction.HISTORYTYPE)
      return "history-type";
    if (code == RestfulInteraction.HISTORYSYSTEM)
      return "history-system";
    if (code == RestfulInteraction.CREATE)
      return "create";
    if (code == RestfulInteraction.SEARCH)
      return "search";
    if (code == RestfulInteraction.SEARCHTYPE)
      return "search-type";
    if (code == RestfulInteraction.SEARCHSYSTEM)
      return "search-system";
    if (code == RestfulInteraction.CAPABILITIES)
      return "capabilities";
    if (code == RestfulInteraction.TRANSACTION)
      return "transaction";
    if (code == RestfulInteraction.BATCH)
      return "batch";
    if (code == RestfulInteraction.OPERATION)
      return "operation";
    return "?";
  }

    public String toSystem(RestfulInteraction code) {
      return code.getSystem();
      }

}

