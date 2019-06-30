package org.hl7.fhir.r4.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.hl7.fhir.r4.formats.IParser;
import org.hl7.fhir.r4.formats.JsonParser;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.hl7.fhir.r4.model.Bundle.HTTPVerb;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.utils.client.FHIRToolingClient;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.utilities.Utilities;

public class BatchLoader {

	public static void main(String[] args) throws IOException, Exception {
	  if (args.length < 4) {
	  	System.out.println("Batch uploader takes 4 parameters in order: server base url, file/folder to upload, xml/json, and batch size");
	  } else {
	  	String server = args[0];
	  	String file = args[1];
	  	IParser p = new JsonParser(); // args[2].equals("json") ? new JsonParser() : new XmlParser();
	  	int size = Integer.parseInt(args[3]);
	  	size = 500;
	  	if (file.endsWith(".xml")) {
	  		throw new FHIRException("Unimplemented file type "+file);
	  	} else if (file.endsWith(".json")) {
	  		throw new FHIRException("Unimplemented file type "+file);
	  	} else if (file.endsWith(".zip")) {
	  		LoadZipFile(server, file, p, size, 0, -1);
	  	} else if (new File(file).isDirectory()) {
	  	  LoadDirectory(server, file, p, size);
	  	} else 
	  		throw new FHIRException("Unknown file type "+file);
	  }
	}

	private static void LoadDirectory(String server, String file, IParser p, int size) throws IOException, Exception {
//    LoadZipFile(server, Utilities.path(file, "Patient.json.zip"), p, size, 1000, -1);
//    LoadZipFile(server, Utilities.path(file, "Binary.json.zip"), p, size, 0, -1);
//	  LoadZipFile(server, Utilities.path(file, "DocumentReference.json.zip"), p, size, 0, -1);
//	  LoadZipFile(server, Utilities.path(file, "Encounter.json.zip"), p, size, 0, -1);
//	  LoadZipFile(server, Utilities.path(file, "Organization.json.zip"), p, size, 0, -1);
//	    LoadZipFile(server, Utilities.path(file, "Procedure.json.zip"), p, size, 0, -1);
//	  LoadZipFile(server, Utilities.path(file, "AllergyIntolerance.json.zip"), p, size, 1500, -1);
//	    LoadZipFile(server, Utilities.path(file, "Condition.json.zip"), p, size, 0, -1);
	    LoadZipFile(server, Utilities.path(file, "Immunization.json.zip"), p, size, 0, -1);
//	  LoadZipFile(server, Utilities.path(file, "MedicationStatement.json.zip"), p, size, 0, -1);
//	  LoadZipFile(server, Utilities.path(file, "Observation-res.json.zip"), p, size, 0, -1);
//	  LoadZipFile(server, Utilities.path(file, "Observation-sh.json.zip"), p, size, 0, -1);
//	  LoadZipFile(server, Utilities.path(file, "Observation-vs.json.zip"), p, size, 0, -1);
//	  LoadZipFile(server, Utilities.path(file, "Observation-gen.json.zip"), p, size, 0, -1);
//	  LoadZipFile(server, Utilities.path(file, "List.json.zip"), p, size, 6500, -1);
//	  LoadZipFile(server, Utilities.path(file, "List-res.json.zip"), p, size, 0, -1);
//	  LoadZipFile(server, Utilities.path(file, "List-vs.json.zip"), p, size, 0, -1);
  }

	
  private static void LoadZipFile(String server, String file, IParser p, int size, int start, int end) throws IOException, Exception {
		System.out.println("Load Zip file "+file);
	 	Bundle b = new Bundle();
	 	b.setType(BundleType.COLLECTION);
	 	b.setId(UUID.randomUUID().toString().toLowerCase());
	 	ZipInputStream zip = new ZipInputStream(new FileInputStream(file));
	 	ZipEntry entry;
    while((entry = zip.getNextEntry())!=null)
    {
    	try {
    	  Resource r = p.parse(zip);
    	  b.addEntry().setResource(r);
    	} catch (Exception e) {
    		throw new Exception("Error parsing "+entry.getName()+": "+e.getMessage(), e);
    	}
    }
	 	loadBundle(server, b, size, start, end);
	}

  
	private static int loadBundle(String server, Bundle b, int size, int start, int end) throws URISyntaxException {
		System.out.println("Post to "+server+". size = "+Integer.toString(size)+", start = "+Integer.toString(start)+", total = "+Integer.toString(b.getEntry().size()));
		FHIRToolingClient client = new FHIRToolingClient(server);
	  int c = start;
	  if (end == -1)
	    end = b.getEntry().size();
	  while (c < end) {
		 	Bundle bt = new Bundle();
		 	bt.setType(BundleType.BATCH);		 	
		 	bt.setId(UUID.randomUUID().toString().toLowerCase());
		 	for (int i = c; i < Math.min(b.getEntry().size(), c+size); i++) {
		 		BundleEntryComponent be = bt.addEntry();
		 		be.setResource(b.getEntry().get(i).getResource());
		 		be.getRequest().setMethod(HTTPVerb.PUT);
		 		be.getRequest().setUrl(be.getResource().getResourceType().toString()+"/"+be.getResource().getId());
		 	}
			System.out.print("  posting..");
			long ms = System.currentTimeMillis();
		 	Bundle resp = client.transaction(bt);
		 	
		 	for (int i = 0; i < resp.getEntry().size(); i++) {
		 	  BundleEntryComponent t = resp.getEntry().get(i);
		 	  if (!t.getResponse().getStatus().startsWith("2")) { 
		 	    System.out.println("failed status at "+Integer.toString(i)+": "+t.getResponse().getStatus());
		 	    return c+i;
		 	  }
		 	}
		 	c = c + size;
      System.out.println("  ..done: "+Integer.toString(c)+". ("+Long.toString(System.currentTimeMillis()-ms)+" ms)");
	  }
		System.out.println(" done");
		return c;
	}

}
