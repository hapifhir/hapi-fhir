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

// Generated on Sat, Aug 22, 2015 23:00-0400 for FHIR v0.5.0


import org.hl7.fhir.instance.model.EnumFactory;

public class OperationOutcomeEnumFactory implements EnumFactory<OperationOutcome> {

  public OperationOutcome fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("MSG_AUTH_REQUIRED".equals(codeString))
      return OperationOutcome.MSGAUTHREQUIRED;
    if ("MSG_BAD_FORMAT".equals(codeString))
      return OperationOutcome.MSGBADFORMAT;
    if ("MSG_BAD_SYNTAX".equals(codeString))
      return OperationOutcome.MSGBADSYNTAX;
    if ("MSG_CANT_PARSE_CONTENT".equals(codeString))
      return OperationOutcome.MSGCANTPARSECONTENT;
    if ("MSG_CANT_PARSE_ROOT".equals(codeString))
      return OperationOutcome.MSGCANTPARSEROOT;
    if ("MSG_CREATED".equals(codeString))
      return OperationOutcome.MSGCREATED;
    if ("MSG_DATE_FORMAT".equals(codeString))
      return OperationOutcome.MSGDATEFORMAT;
    if ("MSG_DELETED".equals(codeString))
      return OperationOutcome.MSGDELETED;
    if ("MSG_DELETED_DONE".equals(codeString))
      return OperationOutcome.MSGDELETEDDONE;
    if ("MSG_DELETED_ID".equals(codeString))
      return OperationOutcome.MSGDELETEDID;
    if ("MSG_DUPLICATE_ID".equals(codeString))
      return OperationOutcome.MSGDUPLICATEID;
    if ("MSG_ERROR_PARSING".equals(codeString))
      return OperationOutcome.MSGERRORPARSING;
    if ("MSG_ID_INVALID".equals(codeString))
      return OperationOutcome.MSGIDINVALID;
    if ("MSG_ID_TOO_LONG".equals(codeString))
      return OperationOutcome.MSGIDTOOLONG;
    if ("MSG_INVALID_ID".equals(codeString))
      return OperationOutcome.MSGINVALIDID;
    if ("MSG_JSON_OBJECT".equals(codeString))
      return OperationOutcome.MSGJSONOBJECT;
    if ("MSG_LOCAL_FAIL".equals(codeString))
      return OperationOutcome.MSGLOCALFAIL;
    if ("MSG_NO_MATCH".equals(codeString))
      return OperationOutcome.MSGNOMATCH;
    if ("MSG_NO_EXIST".equals(codeString))
      return OperationOutcome.MSGNOEXIST;
    if ("MSG_NO_MODULE".equals(codeString))
      return OperationOutcome.MSGNOMODULE;
    if ("MSG_NO_SUMMARY".equals(codeString))
      return OperationOutcome.MSGNOSUMMARY;
    if ("MSG_OP_NOT_ALLOWED".equals(codeString))
      return OperationOutcome.MSGOPNOTALLOWED;
    if ("MSG_PARAM_CHAINED".equals(codeString))
      return OperationOutcome.MSGPARAMCHAINED;
    if ("MSG_PARAM_NO_REPEAT".equals(codeString))
      return OperationOutcome.MSGPARAMNOREPEAT;
    if ("MSG_PARAM_UNKNOWN".equals(codeString))
      return OperationOutcome.MSGPARAMUNKNOWN;
    if ("MSG_RESOURCE_EXAMPLE_PROTECTED".equals(codeString))
      return OperationOutcome.MSGRESOURCEEXAMPLEPROTECTED;
    if ("MSG_RESOURCE_ID_FAIL".equals(codeString))
      return OperationOutcome.MSGRESOURCEIDFAIL;
    if ("MSG_RESOURCE_NOT_ALLOWED".equals(codeString))
      return OperationOutcome.MSGRESOURCENOTALLOWED;
    if ("MSG_RESOURCE_REQUIRED".equals(codeString))
      return OperationOutcome.MSGRESOURCEREQUIRED;
    if ("MSG_RESOURCE_ID_MISMATCH".equals(codeString))
      return OperationOutcome.MSGRESOURCEIDMISMATCH;
    if ("MSG_RESOURCE_ID_MISSING".equals(codeString))
      return OperationOutcome.MSGRESOURCEIDMISSING;
    if ("MSG_RESOURCE_TYPE_MISMATCH".equals(codeString))
      return OperationOutcome.MSGRESOURCETYPEMISMATCH;
    if ("MSG_SORT_UNKNOWN".equals(codeString))
      return OperationOutcome.MSGSORTUNKNOWN;
    if ("MSG_TRANSACTION_DUPLICATE_ID".equals(codeString))
      return OperationOutcome.MSGTRANSACTIONDUPLICATEID;
    if ("MSG_TRANSACTION_MISSING_ID".equals(codeString))
      return OperationOutcome.MSGTRANSACTIONMISSINGID;
    if ("MSG_UNHANDLED_NODE_TYPE".equals(codeString))
      return OperationOutcome.MSGUNHANDLEDNODETYPE;
    if ("MSG_UNKNOWN_CONTENT".equals(codeString))
      return OperationOutcome.MSGUNKNOWNCONTENT;
    if ("MSG_UNKNOWN_OPERATION".equals(codeString))
      return OperationOutcome.MSGUNKNOWNOPERATION;
    if ("MSG_UNKNOWN_TYPE".equals(codeString))
      return OperationOutcome.MSGUNKNOWNTYPE;
    if ("MSG_UPDATED".equals(codeString))
      return OperationOutcome.MSGUPDATED;
    if ("MSG_VERSION_AWARE".equals(codeString))
      return OperationOutcome.MSGVERSIONAWARE;
    if ("MSG_VERSION_AWARE_CONFLICT".equals(codeString))
      return OperationOutcome.MSGVERSIONAWARECONFLICT;
    if ("MSG_VERSION_AWARE_URL".equals(codeString))
      return OperationOutcome.MSGVERSIONAWAREURL;
    if ("MSG_WRONG_NS".equals(codeString))
      return OperationOutcome.MSGWRONGNS;
    if ("SEARCH_MULTIPLE".equals(codeString))
      return OperationOutcome.SEARCHMULTIPLE;
    if ("UPDATE_MULTIPLE_MATCHES".equals(codeString))
      return OperationOutcome.UPDATEMULTIPLEMATCHES;
    if ("SEARCH_NONE".equals(codeString))
      return OperationOutcome.SEARCHNONE;
    throw new IllegalArgumentException("Unknown OperationOutcome code '"+codeString+"'");
  }

  public String toCode(OperationOutcome code) {
    if (code == OperationOutcome.MSGAUTHREQUIRED)
      return "MSG_AUTH_REQUIRED";
    if (code == OperationOutcome.MSGBADFORMAT)
      return "MSG_BAD_FORMAT";
    if (code == OperationOutcome.MSGBADSYNTAX)
      return "MSG_BAD_SYNTAX";
    if (code == OperationOutcome.MSGCANTPARSECONTENT)
      return "MSG_CANT_PARSE_CONTENT";
    if (code == OperationOutcome.MSGCANTPARSEROOT)
      return "MSG_CANT_PARSE_ROOT";
    if (code == OperationOutcome.MSGCREATED)
      return "MSG_CREATED";
    if (code == OperationOutcome.MSGDATEFORMAT)
      return "MSG_DATE_FORMAT";
    if (code == OperationOutcome.MSGDELETED)
      return "MSG_DELETED";
    if (code == OperationOutcome.MSGDELETEDDONE)
      return "MSG_DELETED_DONE";
    if (code == OperationOutcome.MSGDELETEDID)
      return "MSG_DELETED_ID";
    if (code == OperationOutcome.MSGDUPLICATEID)
      return "MSG_DUPLICATE_ID";
    if (code == OperationOutcome.MSGERRORPARSING)
      return "MSG_ERROR_PARSING";
    if (code == OperationOutcome.MSGIDINVALID)
      return "MSG_ID_INVALID";
    if (code == OperationOutcome.MSGIDTOOLONG)
      return "MSG_ID_TOO_LONG";
    if (code == OperationOutcome.MSGINVALIDID)
      return "MSG_INVALID_ID";
    if (code == OperationOutcome.MSGJSONOBJECT)
      return "MSG_JSON_OBJECT";
    if (code == OperationOutcome.MSGLOCALFAIL)
      return "MSG_LOCAL_FAIL";
    if (code == OperationOutcome.MSGNOMATCH)
      return "MSG_NO_MATCH";
    if (code == OperationOutcome.MSGNOEXIST)
      return "MSG_NO_EXIST";
    if (code == OperationOutcome.MSGNOMODULE)
      return "MSG_NO_MODULE";
    if (code == OperationOutcome.MSGNOSUMMARY)
      return "MSG_NO_SUMMARY";
    if (code == OperationOutcome.MSGOPNOTALLOWED)
      return "MSG_OP_NOT_ALLOWED";
    if (code == OperationOutcome.MSGPARAMCHAINED)
      return "MSG_PARAM_CHAINED";
    if (code == OperationOutcome.MSGPARAMNOREPEAT)
      return "MSG_PARAM_NO_REPEAT";
    if (code == OperationOutcome.MSGPARAMUNKNOWN)
      return "MSG_PARAM_UNKNOWN";
    if (code == OperationOutcome.MSGRESOURCEEXAMPLEPROTECTED)
      return "MSG_RESOURCE_EXAMPLE_PROTECTED";
    if (code == OperationOutcome.MSGRESOURCEIDFAIL)
      return "MSG_RESOURCE_ID_FAIL";
    if (code == OperationOutcome.MSGRESOURCENOTALLOWED)
      return "MSG_RESOURCE_NOT_ALLOWED";
    if (code == OperationOutcome.MSGRESOURCEREQUIRED)
      return "MSG_RESOURCE_REQUIRED";
    if (code == OperationOutcome.MSGRESOURCEIDMISMATCH)
      return "MSG_RESOURCE_ID_MISMATCH";
    if (code == OperationOutcome.MSGRESOURCEIDMISSING)
      return "MSG_RESOURCE_ID_MISSING";
    if (code == OperationOutcome.MSGRESOURCETYPEMISMATCH)
      return "MSG_RESOURCE_TYPE_MISMATCH";
    if (code == OperationOutcome.MSGSORTUNKNOWN)
      return "MSG_SORT_UNKNOWN";
    if (code == OperationOutcome.MSGTRANSACTIONDUPLICATEID)
      return "MSG_TRANSACTION_DUPLICATE_ID";
    if (code == OperationOutcome.MSGTRANSACTIONMISSINGID)
      return "MSG_TRANSACTION_MISSING_ID";
    if (code == OperationOutcome.MSGUNHANDLEDNODETYPE)
      return "MSG_UNHANDLED_NODE_TYPE";
    if (code == OperationOutcome.MSGUNKNOWNCONTENT)
      return "MSG_UNKNOWN_CONTENT";
    if (code == OperationOutcome.MSGUNKNOWNOPERATION)
      return "MSG_UNKNOWN_OPERATION";
    if (code == OperationOutcome.MSGUNKNOWNTYPE)
      return "MSG_UNKNOWN_TYPE";
    if (code == OperationOutcome.MSGUPDATED)
      return "MSG_UPDATED";
    if (code == OperationOutcome.MSGVERSIONAWARE)
      return "MSG_VERSION_AWARE";
    if (code == OperationOutcome.MSGVERSIONAWARECONFLICT)
      return "MSG_VERSION_AWARE_CONFLICT";
    if (code == OperationOutcome.MSGVERSIONAWAREURL)
      return "MSG_VERSION_AWARE_URL";
    if (code == OperationOutcome.MSGWRONGNS)
      return "MSG_WRONG_NS";
    if (code == OperationOutcome.SEARCHMULTIPLE)
      return "SEARCH_MULTIPLE";
    if (code == OperationOutcome.UPDATEMULTIPLEMATCHES)
      return "UPDATE_MULTIPLE_MATCHES";
    if (code == OperationOutcome.SEARCHNONE)
      return "SEARCH_NONE";
    return "?";
  }


}

