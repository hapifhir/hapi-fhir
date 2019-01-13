package org.hl7.fhir.rdf;

import java.io.IOException;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFDataMgr;
import org.hl7.fhir.utilities.Utilities;

public class RDFTests {
	private static final String prefixes =
		 "PREFIX dc: <http://purl.org/dc/elements/1.1/> \r\n"+
     "PREFIX dcterms: <http://purl.org/dc/terms/> \r\n"+
     "PREFIX owl: <http://www.w3.org/2002/07/owl#> \r\n"+
     "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \r\n"+
     "PREFIX rdfs: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \r\n"+
     "PREFIX rim: <http://hl7.org/orim/class/> \r\n"+
     "PREFIX dt: <http://hl7.org/orim/datatype/> \r\n"+
     "PREFIX vs: <http://hl7.org/orim/valueset/> \r\n"+
     "PREFIX cs: <http://hl7.org/orim/codesystem/> \r\n"+
     "PREFIX xs: <http://www.w3.org/2001/XMLSchema/> \r\n"+
     "PREFIX fhir: <http://hl7.org/fhir/> \r\n"+
     "PREFIX os: <http://open-services.net/ns/core#> \r\n";
	
	Model model;
	
  public static void main(String[] args) {
    try {

    	RDFTests tests = new RDFTests();
    	tests.load("C:\\work\\org.hl7.fhir\\build\\publish");
    	tests.execute();
      System.out.println("Completed OK");
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
  
	private void load(String path) throws IOException {
    Model rim = RDFDataMgr.loadModel(Utilities.path(path, "rim.ttl")) ;
    Model fhir = RDFDataMgr.loadModel(Utilities.path(path, "fhir.ttl")) ;
    model = rim.union(fhir);
  }

  private void execute() {
//  	assertion("All domain resources have a w5 mapping", "SELECT ?x WHERE { ?x  rdfs:subClassOf fhir:DomainResource. FILTER NOT EXISTS { ?x fhir:w5 ?anything }}", false);
  }
  
//  private Asser assertion(String desc, String sparql, boolean wantTrue) {
//  	Query query = QueryFactory.create(prefixes+sparql);
//
//  	// Execute the query and obtain results
//  	QueryExecution qe = QueryExecutionFactory.create(query, model);
//  	ResultSet results = qe.execSelect();
//  	boolean res = results.hasNext() == wantTrue;
//
//  	if (!res) {
//  		System.out.println("Sparql Assertion "+desc+" failed: ");
//  		// Output query results	
//  		ResultSetFormatter.out(System.out, results, query);
//  		System.out.println("Sparql: "+sparql);
//  	}
//
//  	// Important - free up resources used running the query
//  	qe.close();
//  	return res;
//  }


}
