package org.hl7.fhir.convertors.misc;

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

import org.hl7.fhir.convertors.VersionConvertor_30_40;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.utilities.Utilities;

/*
 * load reosurces in xml format, and sve them in package format (json, with correct name
 * 
 * C:\work\fhirserver\resources\resources\dicom
 * 
 */
public class PackagePreparer {


  public static void main(String[] args) {
    for (File f : new File("C:\\work\\fhirserver\\resources\\mihin").listFiles()) {
      try {
        org.hl7.fhir.dstu3.model.Resource r = new org.hl7.fhir.dstu3.formats.JsonParser().parse(new FileInputStream(f));
        if (r instanceof Bundle) {
          Bundle b = (Bundle) r;
          for (BundleEntryComponent be : b.getEntry()) {
            try {
              org.hl7.fhir.r4.model.Resource r4 = VersionConvertor_30_40.convertResource(be.getResource(), false);
              if (r4.getId().startsWith(r4.fhirType()+"-"))
                be.getResource().setId(r4.getId().substring(r4.fhirType().length()+1));
              if (be.getResource().hasId())
                new org.hl7.fhir.r4.formats.JsonParser().compose(new FileOutputStream(Utilities.path("C:\\work\\fhirserver\\resources\\fhir.test.data\\3.5.0\\package", be.getResource().fhirType()+"-"+be.getResource().getId()+".json")), r4);
              else
                System.out.println(f.getName()+" bundle entry has no id");
            } catch (Exception e) {
              System.out.println(f.getName()+": "+e.getMessage()); 
            }
          }
        } else if (r.hasId())
          new org.hl7.fhir.r4.formats.JsonParser().compose(new FileOutputStream(Utilities.path(Utilities.getDirectoryForFile(f.getAbsolutePath()), r.fhirType()+"-"+r.getId()+".json")), VersionConvertor_30_40.convertResource(r, false));
        else
          System.out.println(f.getName()+" has no id");
      } catch (Exception e) {
        System.out.println(f.getName()+": "+e.getMessage()); 
        e.printStackTrace();
      }
    }

    System.out.println("Completed OK");
  }

}
