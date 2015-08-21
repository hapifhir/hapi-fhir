package ca.uhn.fhir.tinder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.hl7.fhir.instance.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.instance.model.DomainResource;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.method.MethodUtil;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.joran.util.ConfigurationWatchListUtil;

@Mojo(name = "minimize-resources", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class ResourceMinimizerMojo extends AbstractMojo {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceMinimizerMojo.class);

	@Parameter(required = true)
	private File targetDirectory;

	@Parameter(required = true)
	private String fhirVersion;

	private FhirContext myCtx;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		ourLog.info("Starting resource minimizer");
		
		if ("DSTU".equals(fhirVersion)) {
			myCtx = FhirContext.forDstu1();
		} else if ("DSTU2".equals(fhirVersion)) {
			myCtx = FhirContext.forDstu2();
		} else if ("HL7ORG_DSTU2".equals(fhirVersion)) {
			myCtx = FhirContext.forDstu2Hl7Org();
		} else {
			throw new MojoFailureException("Unknown version: " + fhirVersion);
		}

		Collection<File> files = FileUtils.listFiles(targetDirectory, new String[] { "xml", "json" }, true);
		for (File nextFile : files) {
			ourLog.debug("Checking file: {}", nextFile);

			String inputString;
			try {
				inputString = IOUtils.toString(new FileInputStream(nextFile), "UTF-8");
			} catch (IOException e) {
				throw new MojoFailureException("Failed to read file: " + nextFile, e);
			}

			IParser parser = MethodUtil.detectEncoding(inputString).newParser(myCtx);
			IBaseResource input = parser.parseResource(inputString);

			if (input instanceof IResource) {
				((IResource) input).getText().getDiv().setValueAsString((String) null);
				((IResource) input).getText().getStatus().setValueAsString((String) null);
				if (input instanceof Bundle) {
					for (Entry nextEntry : ((Bundle) input).getEntry()) {
						if (nextEntry.getResource() != null) {
							nextEntry.getResource().getText().getDiv().setValueAsString((String) null);
							nextEntry.getResource().getText().getStatus().setValueAsString((String) null);
						}
					}
				}
			} else if (input instanceof org.hl7.fhir.instance.model.Bundle) {
				for (BundleEntryComponent nextEntry : ((org.hl7.fhir.instance.model.Bundle) input).getEntry()) {
					if (nextEntry.getResource() instanceof DomainResource) {
						((DomainResource)nextEntry.getResource()).getText().getDiv().setValueAsString((String) null);
						((DomainResource)nextEntry.getResource()).getText().getStatusElement().setValueAsString((String) null);
					}
				}
			} else if (input instanceof DomainResource) {
				try {
					((DomainResource) input).getText().setDivAsString(null);
					((DomainResource) input).getText().getStatusElement().setValueAsString(null);
				} catch (Exception e) {
					ourLog.error("Failed to clear narrative", e);
				}
			} else {
				ourLog.info("Ignoring type: " + input.getClass());
				continue;
			}

			String outputString = parser.setPrettyPrint(true).encodeResourceToString(input);
			StringBuilder b = new StringBuilder();
			for (String nextLine : outputString.split("\\n")) {
				int i;
				for (i = 0; i < nextLine.length(); i++) {
					if (nextLine.charAt(i) != ' ') {
						break;
					}
				}
				
				b.append(StringUtils.leftPad("", i / 3, ' '));
				b.append(nextLine.substring(i));
				b.append("\n");
			}
			outputString = b.toString();
			
			if (!inputString.equals(outputString)) {
				ourLog.info("Trimming contents of resource: {} - From {} to {}", nextFile, FileUtils.byteCountToDisplaySize(inputString.length()), FileUtils.byteCountToDisplaySize(outputString.length()));
				try {
					BufferedWriter writer = new BufferedWriter(new FileWriter(nextFile.getAbsolutePath(), false));
					writer.append(outputString);
					writer.close();
				} catch (IOException e) {
					throw new MojoFailureException("Failed to write " + nextFile, e);
				}

			}

		}
	}

	public static void main(String[] args) throws Exception {
		LoggerContext loggerContext = ((ch.qos.logback.classic.Logger)ourLog).getLoggerContext();
      URL mainURL = ConfigurationWatchListUtil.getMainWatchURL(loggerContext);
      System.out.println(mainURL);
      // or even
      ourLog.info("Logback used '{}' as the configuration file.", mainURL);
		
		ResourceMinimizerMojo m = new ResourceMinimizerMojo();
		m.targetDirectory = new File("/Users/t3903uhn/git/hapi-fhir/hapi-tinder-plugin/src/main/resources/vs/dstu2");
		m.fhirVersion = "DSTU2";
		m.execute();
		
		m = new ResourceMinimizerMojo();
		m.targetDirectory = new File("/Users/t3903uhn/git/hapi-fhir/hapi-fhir-structures-hl7org-dstu2/src/main/resources/org/hl7/fhir/instance/model/profile");
		m.fhirVersion = "DSTU2";
		m.execute();

	}

}
