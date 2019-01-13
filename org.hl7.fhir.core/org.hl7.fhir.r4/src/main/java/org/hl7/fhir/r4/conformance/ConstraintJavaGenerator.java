package org.hl7.fhir.r4.conformance;

/*-
 * #%L
 * org.hl7.fhir.r4
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.context.IWorkerContext;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.utilities.Utilities;

public class ConstraintJavaGenerator {

  private IWorkerContext context; // for doing expansions
  private String version; // for getting includes correct
  private String folder; //dest dir where the profile will be generated into
  private String packageName;
  
  public ConstraintJavaGenerator(IWorkerContext context, String version, String folder, String packageName) {
    super();
    this.context = context;
    this.version = version;
    this.folder = folder;
    this.packageName = packageName;
  }

  public String generate(StructureDefinition sd) throws FHIRException, IOException {
    String name = sd.hasName() ? Utilities.titleize(sd.getName().replace(".", "").replace("-", "").replace("\"", "")).replace(" ", "") : "";
    if (!Utilities.nmtokenize(name).equals(name)) {
      System.out.println("Cannot generate Java code for profile "+sd.getUrl()+" because the name \""+name+"\" is not a valid Java class name");
      return null;
    }
    File destFile = new File(Utilities.path(folder, name+".java"));
    OutputStreamWriter dest = new OutputStreamWriter(new FileOutputStream(destFile), "UTF-8");
    
    dest.write("package "+packageName+";\r\n");
    dest.write("\r\n");
    dest.write("import org.hl7.fhir.r4.model.ProfilingWrapper;\r\n");
    dest.write("\r\n");
    dest.write("public class "+name+" {\r\n");
    dest.write("\r\n");
    
    dest.write("}\r\n");
    dest.flush();
    dest.close();
    return destFile.getAbsolutePath();
  }
  
}
