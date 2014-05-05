package ca.uhn.fhir.jpa.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;

import com.google.common.io.PatternFilenameFilter;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.Questionnaire;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.client.IGenericClient;

public class UploadTestResources {
private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(UploadTestResources.class);
	public static void main(String[] args) throws DataFormatException, FileNotFoundException {
		
		FhirContext ctx = new FhirContext();
		IGenericClient client = ctx.newRestfulGenericClient("http://localhost:8888/fhir/context");
		
		File[] files = new File("src/test/resources/resources").listFiles(new PatternFilenameFilter(".*patient.*"));
		for (File file : files) {
			ourLog.info("Uploading: {}", file);
			Patient patient = ctx.newXmlParser().parseResource(Patient.class, new FileReader(file));
			client.create(patient);
		}
		
		files = new File("src/test/resources/resources").listFiles(new PatternFilenameFilter(".*questionnaire.*"));
		for (File file : files) {
			ourLog.info("Uploading: {}", file);
			Questionnaire patient = ctx.newXmlParser().parseResource(Questionnaire.class, new FileReader(file));
			client.create(patient);
		}

	}

}
