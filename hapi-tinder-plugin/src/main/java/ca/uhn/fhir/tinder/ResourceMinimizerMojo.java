package ca.uhn.fhir.tinder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.ParseException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.EscapeTool;
import org.hl7.fhir.instance.model.DomainResource;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.dstu.resource.Conformance.Rest;
import ca.uhn.fhir.model.dstu.resource.Conformance.RestResource;
import ca.uhn.fhir.model.dstu.resource.Profile;
import ca.uhn.fhir.model.dstu.valueset.RestfulConformanceModeEnum;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.IBasicClient;
import ca.uhn.fhir.rest.method.MethodUtil;
import ca.uhn.fhir.tinder.model.BaseRootType;
import ca.uhn.fhir.tinder.model.RestResourceTm;
import ca.uhn.fhir.tinder.model.SearchParameter;
import ca.uhn.fhir.tinder.parser.ProfileParser;
import ca.uhn.fhir.tinder.parser.ResourceGeneratorUsingSpreadsheet;

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
			ourLog.info("Checking file: {}", nextFile);

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
			} else if (input instanceof DomainResource) {
				try {
					((DomainResource) input).getText().setDivAsString(null);
					((DomainResource) input).getText().getStatusElement().setValueAsString(null);
				} catch (Exception e) {
					ourLog.error("Failed to clear narrative", e);
				}
			} else {
				ourLog.info("Ignoring type: " + input.getClass());
			}
		}
	}

}
