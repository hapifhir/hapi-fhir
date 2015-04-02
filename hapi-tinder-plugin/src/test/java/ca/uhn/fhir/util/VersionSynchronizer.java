package ca.uhn.fhir.util;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.ini4j.Ini;
import org.ini4j.Profile.Section;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.composite.NarrativeDt;
import ca.uhn.fhir.model.dstu2.resource.BaseResource;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;

public class VersionSynchronizer {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(VersionSynchronizer.class);

	public static void main(String[] args) throws Exception {

		Set<String> resources = new TreeSet<String>();
		{
			String str = IOUtils.toString(new FileReader("../hapi-fhir-structures-dstu2/pom.xml"));
			Matcher m = Pattern.compile("baseResourceName.([a-zA-Z]+)..baseResourceName").matcher(str);
			while (m.find()) {
				String name = m.group(1).toLowerCase();
				resources.add(name);
			}
		}
		
		ourLog.info("POM resource names: " + resources);
		
		String buildDir = "/Users/t3903uhn/workspace/fhirbuild/trunk/build";
		ArrayList<String> lines = new ArrayList<String>(Arrays.asList(IOUtils.toString(new FileReader(new File(buildDir + "/source", "fhir.ini"))).split("\\r?\\n")));
		for (int i = 0; i < lines.size(); i++) {
			String next = lines.get(i);
			if (next.startsWith("=") || next.startsWith(" ")) {
				lines.remove(i);
				i--;
				continue;
			}

			if (isBlank(next)) {
				continue;
			}

			if (Character.isAlphabetic(next.charAt(0)) && next.indexOf('=') == -1) {
				lines.set(i, next + '=');
				continue;
			}

		}

		Ini ini = new Ini(new StringReader(StringUtils.join(lines, '\n')));
		Section resourceSects = ini.get("resources");

		ourLog.info("Copying resource spreadsheets");

		TreeSet<String> resourceNames = new TreeSet<String>(resourceSects.keySet());
		resourceNames.add("parameters");
		
		for (String nextResource : new ArrayList<String>(resourceNames)) {
			nextResource = nextResource.toLowerCase();
			ourLog.info(" * Resource: {}", nextResource);

			File spreadsheetFile = new File(buildDir + "/source/" + nextResource + "/" + nextResource + "-spreadsheet.xml");
			if (!spreadsheetFile.exists()) {
				throw new Exception("Unknown file: " + spreadsheetFile);
			}
			FileUtils.copyFile(spreadsheetFile, new File("src/main/resources/res/dstu2/" + nextResource + "-spreadsheet.xml"));
			
			if (!resources.contains(nextResource)) {
				throw new Exception("POM needs:\n<baseResourceName>" + nextResource+"</baseResourceName>");
			}
			resources.remove(nextResource);
		}

		if (resources.size() > 0) {
			throw new Exception("POM has unneeded resources: " + resources);
		}
		
		ourLog.info("Copying datatypes");
		
		Collection<File> dtFiles = FileUtils.listFiles(new File(buildDir+"/source/datatypes"), new String[] {"xml"}, false);
		for (File file : dtFiles) {
			if (file.getName().contains("-")) {
				continue;
			}
			ourLog.info("Datatype: {}", file.getName());
			File destFile = new File("src/main/resources/dt/dstu2/" + file.getName());
			FileUtils.copyFile(file, destFile);
//			ourLog.info("Copied to {}", destFile.getAbsolutePath());
		}
		
		ourLog.info("Copying ValueSets");
		
		FileUtils.copyFile(new File(buildDir + "/publish/valuesets.xml"), new File("src/main/resources/vs/dstu2/all-valuesets-bundle.xml"));
		
		{
			ourLog.info("Shrinking valueset file");
			String fileName = "src/main/resources/vs/dstu2/all-valuesets-bundle.xml";
			FileReader fr = new FileReader(fileName);
			FhirContext ctx = FhirContext.forDstu2();
			Bundle b = ctx.newXmlParser().parseResource(Bundle.class, fr);
			for (Entry nextEntry : b.getEntry()) {
				BaseResource nextRes = (BaseResource) nextEntry.getResource();
				nextRes.setText(new NarrativeDt());
			}
			
			FileWriter fw = new FileWriter(new File(fileName), false);
			ctx.newXmlParser().encodeResourceToWriter(b, fw);
			fw.close();
			
			ourLog.info("Fixed {} valuesets", b.getEntry().size());
		}
		
		ourLog.info("Copying Schematron files");
		
		Collection<File> schFiles = FileUtils.listFiles(new File(buildDir+"/publish"), new String[] {"sch"}, false);
		for (File file : schFiles) {
			ourLog.info("Schematron: {}", file.getName());
			File destFile = new File("../hapi-fhir-structures-dstu2/src/main/resources/ca/uhn/fhir/model/dstu2/schema/" + file.getName());
			FileUtils.copyFile(file, destFile);
//			ourLog.info("Copied to {}", destFile.getAbsolutePath());
		}
				
		ourLog.info("Copying XSD");
		FileUtils.copyFile(new File(buildDir + "/publish/fhir-single.xsd"), new File("../hapi-fhir-structures-dstu2/src/main/resources/ca/uhn/fhir/model/dstu2/schema/fhir-single.xsd"));
		FileUtils.copyFile(new File(buildDir + "/publish/xml.xsd"), new File("../hapi-fhir-structures-dstu2/src/main/resources/ca/uhn/fhir/model/dstu2/schema/xml.xsd"));
		FileUtils.copyFile(new File(buildDir + "/publish/fhir-xhtml.xsd"), new File("../hapi-fhir-structures-dstu2/src/main/resources/ca/uhn/fhir/model/dstu2/schema/fhir-xhtml.xsd"));
		
	}
	
	

}
