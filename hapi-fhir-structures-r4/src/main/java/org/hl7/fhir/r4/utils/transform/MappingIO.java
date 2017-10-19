package org.hl7.fhir.r4.utils.transform;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class MappingIO {

  private File mappingFile;

  private String mappingText;

  private Scanner reader;

  public MappingIO(){
    this.mappingText = "";
  }

  public File getMappingFile(){
    return this.mappingFile;
  }

  public void setMappingFile(File mappingFile) throws IOException {
    if (!mappingFile.getAbsolutePath().endsWith(".map")&&mappingFile.exists()) {
      System.err.println("Invalid File");
      return;
    }
    this.reader = new Scanner(mappingFile);
    this.mappingFile = mappingFile;
  }

  public String getMappingText(){
    return this.mappingText;
  }


  public String readFile(){
    while (reader.hasNext()){
      this.mappingText += this.reader.nextLine()+"\n";
    }
    return this.mappingText;
  }

}
