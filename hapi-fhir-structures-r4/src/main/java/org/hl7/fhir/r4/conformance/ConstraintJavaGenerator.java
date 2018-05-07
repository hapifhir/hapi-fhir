package org.hl7.fhir.r4.conformance;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

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
    String name = sd.getName();
    if (!Utilities.nmtokenize(name).equals(name))
      throw new FHIRException("Cannot generate Java code for profile "+sd.getUrl()+" because the name \""+name+"\" is not a valid Java class name");
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
    return destFile.getAbsolutePath();
  }
  
}
