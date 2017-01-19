import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.DomainResource;
import org.hl7.fhir.dstu3.model.Narrative;
import org.hl7.fhir.dstu3.model.Resource;
import org.junit.AfterClass;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.TestUtil;

public class MinimizeResources {
private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(MinimizeResources.class);

	private static FhirContext ourCtx = FhirContext.forDstu3();

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	public static void main(String[] args) throws Exception {
		
		
		Collection<File> xml = FileUtils.listFiles(new File("../hapi-fhir-validation-resources-dstu3/src/main/resources/org/hl7/fhir/instance/model/dstu3/profile"), new String[] {"xml"}, false);
		for (File next : xml) {
			ourLog.info("Checking: {}", next.getAbsoluteFile());
			
			String inputFile = IOUtils.toString(new FileReader(next));
			Bundle bundle = (Bundle) ourCtx.newXmlParser().parseResource(inputFile);
			for (BundleEntryComponent nextEntry : bundle.getEntry()) {
				Resource resource;
				resource = nextEntry.getResource();
				if (resource instanceof DomainResource) {
					((DomainResource) resource).setText(new Narrative());
				}
			}
			
			
			String output = ourCtx.newXmlParser().setPrettyPrint(false).encodeResourceToString(bundle);
			if (!output.equals(inputFile)) {
				ourLog.info("Rewriting {}", next.getAbsolutePath());
				
				FileWriter writer = new FileWriter(next, false);
				writer.append(output);
				writer.close();
			}
		}
		
	}

}
