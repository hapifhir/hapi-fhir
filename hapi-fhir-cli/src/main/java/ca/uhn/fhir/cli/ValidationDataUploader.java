package ca.uhn.fhir.cli;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.hl7.fhir.instance.model.Bundle;
import org.hl7.fhir.instance.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.instance.model.StructureDefinition;
import org.hl7.fhir.instance.model.ValueSet;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.IGenericClient;

public class ValidationDataUploader extends BaseCommand {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ValidationDataUploader.class);

	public static void main(String[] args) throws Exception {
		new ValidationDataUploader().execute();
	}

	private void execute() throws IOException, ClientProtocolException, UnsupportedEncodingException {
		ourLog.info("Starting...");

		FhirContext ctx = FhirContext.forDstu2Hl7Org();

		IGenericClient client = newClient(ctx,"");

		int total;
		int count;
		
		// String vsContents =
		// IOUtils.toString(ValidationDataUploader.class.getResourceAsStream("/org/hl7/fhir/instance/model/valueset/valuesets.xml"),
		// "UTF-8");
		// Bundle bundle = ctx.newXmlParser().parseResource(Bundle.class, vsContents);
		//
		// int total = bundle.getEntry().size();
		// int count = 1;
		// for (BundleEntryComponent i : bundle.getEntry()) {
		// ValueSet next = (ValueSet) i.getResource();
		// next.setId(next.getIdElement().toUnqualifiedVersionless());
		//
		// ourLog.info("Uploading ValueSet {}/{} : {}", new Object[] {count,total,next.getIdElement().getValue()});
		// client.update().resource(next).execute();
		//
		// count++;
		// }
		//
		// ourLog.info("Finished uploading ValueSets");

//		String vsContents = IOUtils.toString(ValidationDataUploader.class.getResourceAsStream("/org/hl7/fhir/instance/model/valueset/v3-codesystems.xml"), "UTF-8");
//		Bundle bundle = ctx.newXmlParser().parseResource(Bundle.class, vsContents);
//		total = bundle.getEntry().size();
//		count = 1;
//		for (BundleEntryComponent i : bundle.getEntry()) {
//			ValueSet next = (ValueSet) i.getResource();
//			next.setId(next.getIdElement().toUnqualifiedVersionless());
//
//			ourLog.info("Uploading v3-codesystems ValueSet {}/{} : {}", new Object[] { count, total, next.getIdElement().getValue() });
//			client.update().resource(next).execute();
//
//			count++;
//		}

		
		String vsContents = IOUtils.toString(ValidationDataUploader.class.getResourceAsStream("/org/hl7/fhir/instance/model/valueset/v2-tables.xml"), "UTF-8");
		Bundle bundle = ctx.newXmlParser().parseResource(Bundle.class, vsContents);
		total = bundle.getEntry().size();
		count = 1;
		for (BundleEntryComponent i : bundle.getEntry()) {
			if (count > 1900) {
			ValueSet next = (ValueSet) i.getResource();
			next.setId(next.getIdElement().toUnqualifiedVersionless());

			ourLog.info("Uploading v2-tables ValueSet {}/{} : {}", new Object[] { count, total, next.getIdElement().getValue() });
			client.update().resource(next).execute();
			}
			count++;
		}

		ourLog.info("Finished uploading ValueSets");

		ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
		Resource[] mappingLocations = patternResolver.getResources("classpath*:org/hl7/fhir/instance/model/profile/*.profile.xml");
		total = mappingLocations.length;
		count = 1;
		for (Resource i : mappingLocations) {
			if (count > 140) {
			StructureDefinition next = ctx.newXmlParser().parseResource(StructureDefinition.class, IOUtils.toString(i.getInputStream(), "UTF-8"));
			next.setId(next.getIdElement().toUnqualifiedVersionless());

			ourLog.info("Uploading StructureDefinition {}/{} : {}", new Object[] { count, total, next.getIdElement().getValue() });
			client.update().resource(next).execute();
			}
			count++;
		}

		ourLog.info("Finished uploading ValueSets");

	}

	@Override
	public String getCommandDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCommandName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Options getOptions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run(CommandLine theCommandLine) throws ParseException {
		// TODO Auto-generated method stub
		
	}

}
