package org.hl7.fhir.rdf;
//
//import org.apache.jena.riot.RDFDataMgr;
//
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.logging.Logger;

//import com.hp.hpl.jena.rdf.model.Model;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.jena.rdf.model.Model;

import es.weso.rdf.RDFReader;
import es.weso.rdf.jena.RDFAsJenaModel;
import es.weso.schema.Result;
import es.weso.schema.Schema;
import es.weso.schema.ShExSchema$;
import scala.Option;

public class ShExValidator {

//  private Logger log = Logger.getLogger(ShExValidator.class.getName());
  private Schema schema;

  public ShExValidator(String schemaFile) throws Exception {
    // load shex from the path
//    log.info("Reading ShEx file " + schemaFile);
    schema = readSchema(schemaFile);
  }

  public Schema readSchema(String schemaFile) throws Exception {
    // Create a none, see: http://stackoverflow.com/questions/1997433/how-to-use-scala-none-from-java-code
    Option<String> none = Option.apply((String) null); // Create a none
    String contents = new String(Files.readAllBytes(Paths.get(schemaFile)));
    return ShExSchema$.MODULE$.fromString(contents, "SHEXC", none).get();
  }

  public void validate(Model dataModel) {
    Option<String> none = Option.apply(null); // Create a none
    RDFReader rdf = new RDFAsJenaModel(dataModel);
    Result result = schema.validate(rdf,"TARGETDECLS",none,none, rdf.getPrefixMap(), schema.pm());
    if (result.isValid()) {
//      log.info("Result is valid");
//      System.out.println("Valid. Result: " + result.show());
    } else {
      System.out.println("Not valid");
    }
  }



//
//
//
//  public void validate(Model dataModel, Schema schema, PrefixMap pm) throws Exception {
//    RDFReader rdf = new RDFAsJenaModel(dataModel);
//    ShExMatcher matcher = new ShExMatcher(schema,rdf);
////    ShExResult result = matcher.validate();
////    if (result.isValid()) {
////      log.info("Result is valid");
////      System.out.println("Valid. Result: " + result.show(1,pm));
////    } else {
////      System.out.println("Not valid");
////    }
//  }
//
//  public void validate(String dataFile, String schemaFile) throws Exception {
////    log.info("Reading data file " + dataFile);
////    Model dataModel = RDFDataMgr.loadModel(dataFile);
////    log.info("Model read. Size = " + dataModel.size());
//
//
////
////    Schema schema = pair._1();
////    PrefixMap pm = pair._2();
//////
//////    log.info("Schema read" + schema.show());
////
////    validate(dataModel,schema,pm);
//  }
//
//
}
