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
import java.io.FileOutputStream;

import org.hl7.fhir.dstu3.formats.IParser.OutputStyle;

public class IGPackConverter140 {
  
  public static void main(String[] args) throws Exception {
    for (String s : new File("C:\\temp\\igpack").list()) {
//      if (s.endsWith(".xml") && !s.contains("z-")) {
      if (s.equals("expansions.xml")) {
        System.out.println("process "+s);
        org.hl7.fhir.dstu2016may.formats.XmlParser xp = new org.hl7.fhir.dstu2016may.formats.XmlParser();
        org.hl7.fhir.dstu2016may.model.Resource r14 = xp.parse(new FileInputStream("C:\\temp\\igpack\\"+s));
        org.hl7.fhir.dstu3.model.Resource r17 = VersionConvertor_14_30.convertResource(r14);
        org.hl7.fhir.dstu3.formats.XmlParser xc = new org.hl7.fhir.dstu3.formats.XmlParser();
        xc.setOutputStyle(OutputStyle.PRETTY);
        xc.compose(new FileOutputStream("C:\\temp\\igpack\\z-"+s), r17);
      }
    }
    System.out.println("done");
  }

}
