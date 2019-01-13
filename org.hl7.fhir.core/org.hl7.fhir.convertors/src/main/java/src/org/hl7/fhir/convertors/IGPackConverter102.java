package org.hl7.fhir.convertors;

/*-
 * #%L
 * org.hl7.fhir.convertors
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


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.hl7.fhir.dstu2.model.Resource;
import org.hl7.fhir.dstu3.formats.IParser.OutputStyle;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.Bundle.BundleType;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.utilities.Utilities;

public class IGPackConverter102 implements VersionConvertorAdvisor30 {
  
  public static void main(String[] args) throws Exception {
    new IGPackConverter102().process();
  }

  private Bundle cslist = new Bundle();
  
  private void process() throws FileNotFoundException, IOException, FHIRException {
    initCSList();
    for (String s : new File("C:\\temp\\igpack2").list()) {
      if (s.endsWith(".xml") && !s.startsWith("z-") &&  !Utilities.existsInList(s, "expansions.xml", "v3-codesystems.xml", "v2-tables.xml")) {
        System.out.println("process "+s);
        org.hl7.fhir.dstu2.formats.XmlParser xp = new org.hl7.fhir.dstu2.formats.XmlParser();
        org.hl7.fhir.dstu2.model.Resource r10 = xp.parse(new FileInputStream("C:\\temp\\igpack2\\"+s));
        org.hl7.fhir.dstu3.model.Resource r17 = new VersionConvertor_10_30(this).convertResource(r10);
        org.hl7.fhir.dstu3.formats.XmlParser xc = new org.hl7.fhir.dstu3.formats.XmlParser();
        xc.setOutputStyle(OutputStyle.PRETTY);
        xc.compose(new FileOutputStream("C:\\temp\\igpack2\\"+s), r17);
      }
    }
    System.out.println("save codesystems");    
    org.hl7.fhir.dstu3.formats.XmlParser xc = new org.hl7.fhir.dstu3.formats.XmlParser();
    xc.setOutputStyle(OutputStyle.PRETTY);
    xc.compose(new FileOutputStream("C:\\temp\\igpack2\\codesystems.xml"), cslist);
    System.out.println("done");    
  }

  private void initCSList() {
    cslist.setId("codesystems");
    cslist.setType(BundleType.COLLECTION);
    cslist.getMeta().setLastUpdated(new Date());
  }

  @Override
  public boolean ignoreEntry(BundleEntryComponent src) {
    return false;
  }

  @Override
  public Resource convert(org.hl7.fhir.dstu3.model.Resource resource) throws FHIRException {
    return null;
  }

  @Override
  public void handleCodeSystem(CodeSystem tgtcs, ValueSet vs) {
    cslist.addEntry().setFullUrl(tgtcs.getUrl()).setResource(tgtcs);
  }

  @Override
  public CodeSystem getCodeSystem(ValueSet src) {
    return null;
  }

}
