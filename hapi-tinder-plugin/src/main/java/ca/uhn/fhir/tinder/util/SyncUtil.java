package ca.uhn.fhir.tinder.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.composite.NarrativeDt;
import ca.uhn.fhir.model.dstu2.resource.BaseResource;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;

public class SyncUtil {
private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SyncUtil.class);
	public static void main(String[] args) throws Exception {
		FhirContext ctx = FhirContext.forDstu2();
		
		String fileName = "src/main/resources/vs/dstu2/all-valuesets-bundle.xml";
		FileReader fr = new FileReader(fileName);
		Bundle b = ctx.newXmlParser().parseResource(Bundle.class, fr);
		for (Entry nextEntry : b.getEntry()) {
			BaseResource nextRes = (BaseResource) nextEntry.getResource();
			nextRes.setText(new NarrativeDt());
		}
		
		File f = new File(fileName);
		OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(f, false), "UTF-8");
		ctx.newXmlParser().encodeResourceToWriter(b, fw);
		fw.close();
		
		ourLog.info("Fixed {} valuesets", b.getEntry().size());
		
		
	}

}
