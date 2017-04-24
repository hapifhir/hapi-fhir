package org.hl7.fhir.dstu3.validation;
/*
Copyright (c) 2011+, HL7, Inc
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

import java.util.List;

import org.hl7.fhir.utilities.validation.ValidationMessage;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueSeverity;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueType;
import org.hl7.fhir.utilities.validation.ValidationMessage.Source;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ValidationErrorHandler implements ErrorHandler {

  private List<ValidationMessage> outputs;
  private String path;

  public ValidationErrorHandler(List<ValidationMessage> outputs, String path) {
    this.outputs = outputs;
    this.path = path;
  }

  @Override
public void error(SAXParseException arg0) throws SAXException {
    outputs.add(new ValidationMessage(Source.Schema, IssueType.INVALID, arg0.getLineNumber(), arg0.getColumnNumber(), path, arg0.getMessage(), IssueSeverity.ERROR));
  }

  @Override
public void fatalError(SAXParseException arg0) throws SAXException {
    outputs.add(new ValidationMessage(Source.Schema, IssueType.INVALID, arg0.getLineNumber(), arg0.getColumnNumber(), path, arg0.getMessage(), IssueSeverity.FATAL));
  }

  @Override
public void warning(SAXParseException arg0) throws SAXException {
    outputs.add(new ValidationMessage(Source.Schema, IssueType.INVALID, arg0.getLineNumber(), arg0.getColumnNumber(), path, arg0.getMessage(), IssueSeverity.WARNING));
  }

}
