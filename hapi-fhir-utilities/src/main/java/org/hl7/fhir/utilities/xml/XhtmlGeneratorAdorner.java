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
package org.hl7.fhir.utilities.xml;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.xml.XhtmlGeneratorAdorner.XhtmlGeneratorAdornerState;
import org.w3c.dom.Element;

public interface XhtmlGeneratorAdorner {

  public class XhtmlGeneratorAdornerState {
    private String prefix;
    private String suffix;
    private String supressionMessage;
    private String path;
    
    public XhtmlGeneratorAdornerState(String path, String prefix, String suffix) {
      super();
      this.path = path;
      this.prefix = prefix;
      this.suffix = suffix;
    }
    
    public XhtmlGeneratorAdornerState(String path, String supressionMessage) {
      super();
      this.path = path;
      this.supressionMessage = supressionMessage;
    }

    public String getPrefix() {
      return prefix;
    }
    public String getSuffix() {
      return suffix;
    }
    public boolean isSuppress() {
      return !Utilities.noString(supressionMessage);
    }
    public String getSupressionMessage() {
      return supressionMessage;
    }
    public String getPath() {
      return path;
    }
  }
  
  XhtmlGeneratorAdornerState getState(XhtmlGenerator ref, XhtmlGeneratorAdornerState state, Element node) throws Exception;
  XhtmlGeneratorAdornerState getAttributeMarkup(XhtmlGenerator xhtmlGenerator, XhtmlGeneratorAdornerState state, Element node, String nodeName, String textContent) throws Exception;
  String getLink(XhtmlGenerator ref, XhtmlGeneratorAdornerState state, Element node) throws Exception;
  String getNodeId(XhtmlGeneratorAdornerState state, Element node);

}

