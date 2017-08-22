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
package org.hl7.fhir.r4.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.hl7.fhir.r4.context.SimpleWorkerContext;
import org.hl7.fhir.r4.elementmodel.Element;
import org.hl7.fhir.r4.elementmodel.Manager;
import org.hl7.fhir.r4.elementmodel.Manager.FhirFormat;
import org.hl7.fhir.r4.formats.IParser;
import org.hl7.fhir.r4.formats.IParser.OutputStyle;
import org.hl7.fhir.r4.formats.JsonParser;
import org.hl7.fhir.r4.formats.XmlParser;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.test.support.TestingUtilities;
import org.hl7.fhir.exceptions.FHIRFormatError;

public class ResourceTest {

  private File source;
  private boolean json;

  public File getSource() {
    return source;
  }

  public void setSource(File source) {
    this.source = source;
  }
  
  public Resource test() throws FHIRFormatError, FileNotFoundException, IOException {
    
    IParser p;
    if (isJson())
      p = new JsonParser();
    else
      p = new XmlParser(false);
    Resource rf = p.parse(new FileInputStream(source));

    FileOutputStream out = new FileOutputStream(source.getAbsoluteFile()+".out.json");
    JsonParser json1 = new JsonParser();
    json1.setOutputStyle(OutputStyle.PRETTY);
    json1.compose(out, rf);
    out.close();

    JsonParser json = new JsonParser();
    rf = json.parse(new FileInputStream(source.getAbsoluteFile()+".out.json"));
    
    out = new FileOutputStream(source.getAbsoluteFile()+".out.xml");
    XmlParser atom = new XmlParser(); 
    atom.setOutputStyle(OutputStyle.PRETTY);
    atom.compose(out, rf, true);
    out.close();
    return rf;
    
  }

  public Element testEM() throws Exception {
    if (TestingUtilities.context == null)
      TestingUtilities.context = SimpleWorkerContext.fromPack("C:\\work\\org.hl7.fhir\\build\\publish\\definitions.xml.zip");
  	Element resource = Manager.parse(TestingUtilities.context, new FileInputStream(source), isJson() ? FhirFormat.JSON : FhirFormat.XML);
  	Manager.compose(TestingUtilities.context, resource, new FileOutputStream(source.getAbsoluteFile()+".out.json"), FhirFormat.JSON, OutputStyle.PRETTY, null);
  	Manager.compose(TestingUtilities.context, resource, new FileOutputStream(source.getAbsoluteFile()+".out.json"), FhirFormat.XML, OutputStyle.PRETTY, null);
  	return resource;
  }

  public boolean isJson() {
    return json;
  }

  public void setJson(boolean json) {
    this.json = json;
  }
  
}
