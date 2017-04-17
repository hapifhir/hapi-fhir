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

public class BundleTypeEnumFactory implements EnumFactory<BundleType> {

  public BundleType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("document".equals(codeString))
      return BundleType.DOCUMENT;
    if ("message".equals(codeString))
      return BundleType.MESSAGE;
    if ("transaction".equals(codeString))
      return BundleType.TRANSACTION;
    if ("transaction-response".equals(codeString))
      return BundleType.TRANSACTIONRESPONSE;
    if ("batch".equals(codeString))
      return BundleType.BATCH;
    if ("batch-response".equals(codeString))
      return BundleType.BATCHRESPONSE;
    if ("history".equals(codeString))
      return BundleType.HISTORY;
    if ("searchset".equals(codeString))
      return BundleType.SEARCHSET;
    if ("collection".equals(codeString))
      return BundleType.COLLECTION;
    throw new IllegalArgumentException("Unknown BundleType code '"+codeString+"'");
  }

  public String toCode(BundleType code) {
    if (code == BundleType.DOCUMENT)
      return "document";
    if (code == BundleType.MESSAGE)
      return "message";
    if (code == BundleType.TRANSACTION)
      return "transaction";
    if (code == BundleType.TRANSACTIONRESPONSE)
      return "transaction-response";
    if (code == BundleType.BATCH)
      return "batch";
    if (code == BundleType.BATCHRESPONSE)
      return "batch-response";
    if (code == BundleType.HISTORY)
      return "history";
    if (code == BundleType.SEARCHSET)
      return "searchset";
    if (code == BundleType.COLLECTION)
      return "collection";
    return "?";
  }

    public String toSystem(BundleType code) {
      return code.getSystem();
      }

}

