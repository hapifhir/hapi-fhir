package org.hl7.fhir.r4.utils.transform;

import org.hl7.fhir.r4.model.StructureMap;
import org.hl7.fhir.r4.utils.transform.deserializer.FhirMapProcessor;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Built to handle the overhead for parseing a structure map.
 * @Author Travis Lukach
 */
public class MappingIO {

  /**
   * holds the file object for the mapping file
   */
  private File mappingFile;

  /**
   * The text of the structure map
   */
  private String mappingText;

  /**
   * Scanner used to read the file
   */
  private Scanner reader;

  /**
   * Processor used to execute the parse of the StructureMap
   */
  private FhirMapProcessor processor;

  /**
   * Used to handle data populating the Structure Map
   */
  private MapHandler mapHandler;

  /**
   * Constructor, for as little as possible is to be initialized
   * @throws Exception
   */
  public MappingIO() throws Exception {
    this.mappingText = "";
    this.processor = new FhirMapProcessor();
    this.mapHandler = new MapHandler();
  }

  /**
   * Constructor, populates the mappingFile object directly
   * @param mappingFile file object to set the mapping file
   * @throws Exception
   */
  public MappingIO(File mappingFile) throws Exception {
    this.setMappingFile(mappingFile);
    this.mappingText = "";
    this.processor = new FhirMapProcessor();
    this.mapHandler = new MapHandler();
  }

  /**
   * Constructor using a string path for the mapping file
   * @param filePath
   * @throws Exception
   */
  public MappingIO(String filePath) throws Exception {
    this.setMappingFile(new File(filePath));
    this.mappingText = "";
    this.processor = new FhirMapProcessor();
    this.mapHandler = new MapHandler();
  }

  /**
   * gets the file object for the StructureMap
   * @return
   */
  public File getMappingFile(){
    return this.mappingFile;
  }

  /**
   * Sets and validates the mapping file
   * @param mappingFile the mappingFile
   * @throws IOException
   */
  public void setMappingFile(File mappingFile) throws IOException {
    if (!mappingFile.getAbsolutePath().endsWith(".map")&&mappingFile.exists()) {
      System.err.println("Invalid File");
      return;
    }
    this.reader = new Scanner(mappingFile);
    this.mappingFile = mappingFile;
  }

  /**
   * Accessor for the file text
   * @return file text
   */
  public String getMappingText(){
    return this.mappingText;
  }


  /**
   * used to digest the mapping file in a simple loop with Scanner
   * @return mapping text
   */
  private String readFile(){
    while (reader.hasNext()){
      this.mappingText += this.reader.nextLine()+"\n";
    }
    return this.mappingText;
  }

  /**
   * accessor for the processor
   * @return the FhirMapProcessor
   */
  public FhirMapProcessor getProcessor() {
    return this.processor;
  }

  /**
   * write method for the processor
   * @param processor a FhirMapProcessor
   */
  public void setProcessor(FhirMapProcessor processor) {
    this.processor = processor;
  }

  /**
   * returns the MapHandler
   * @return the structure map handler
   */
  public MapHandler getMapHandler() {
    return this.mapHandler;
  }

  /**
   * Executes the parse of the StructureMap using
   * @return the structure map within the processed handler
   * @throws Exception if the map handler fails to safely be processed
   */
  public StructureMap getStructureMap() throws Exception {
    this.processor.parseFhirMap(this.readFile(), this.mapHandler);
    return this.mapHandler.getStructureMap();
  }

  /**
   * Executes a parse of the structure map argument
   * @param file the file of the StructureMap
   * @return a Parsed StructureMap
   * @throws Exception if the map handler fails to safely be processed
   */
  public StructureMap getStructureMapFromFile(File file) throws Exception {
    this.setMappingFile(file);
    this.processor.parseFhirMap(this.readFile(), this.mapHandler);
    return this.mapHandler.getStructureMap();
  }
}
