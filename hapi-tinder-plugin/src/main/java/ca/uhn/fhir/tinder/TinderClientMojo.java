package ca.uhn.fhir.tinder;

import static org.apache.commons.lang.StringUtils.defaultString;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
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

import ch.qos.logback.core.util.FileUtil;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.dstu.resource.Conformance.Rest;
import ca.uhn.fhir.model.dstu.resource.Conformance.RestResource;
import ca.uhn.fhir.model.dstu.resource.Profile;
import ca.uhn.fhir.model.dstu.valueset.RestfulConformanceModeEnum;
import ca.uhn.fhir.rest.client.IRestfulClientFactory;
import ca.uhn.fhir.rest.client.api.IBasicClient;
import ca.uhn.fhir.tinder.model.Extension;
import ca.uhn.fhir.tinder.model.Resource;
import ca.uhn.fhir.tinder.model.RestResourceTm;

@Mojo(name = "generate-client", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class TinderClientMojo extends AbstractMojo {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TinderClientMojo.class);

	@Parameter(alias = "clientClassName", required = true)
	private String myClientClassName;

	@Parameter(alias = "targetDirectory", required = true, defaultValue = "${project.build.directory}/generated-sources/tinder")
	private File myTargetDirectory;

	private List<RestResourceTm> myResources = new ArrayList<RestResourceTm>();

	private String myPackageBase;

	private File myDirectoryBase;

	private String myClientClassSimpleName;
	
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		determinePaths();
		
		FhirContext ctx = new FhirContext(Conformance.class);
		IRestfulClientFactory cFact = ctx.newRestfulClientFactory();
		IBasicClient client = cFact.newClient(IBasicClient.class, "http://fhir.healthintersections.com.au/open");

		Conformance conformance = client.getServerConformanceStatement();

		if (conformance.getRest().size() != 1) {
			throw new MojoFailureException("Found " + conformance.getRest().size() + " rest definitions in Conformance resource. Need exactly 1.");
		}

		Rest rest = conformance.getRest().get(0);
		if (rest.getMode().getValueAsEnum() != RestfulConformanceModeEnum.SERVER) {
			throw new MojoFailureException("Conformance mode is not server, found: " + rest.getMode().getValue());
		}

		for (RestResource nextResource : rest.getResource()) {
			RestResourceTm nextTmResource = new RestResourceTm(nextResource);
			myResources.add(nextTmResource);
			
			Profile profile;
			try {
				ourLog.info("Loading Profile: {}", nextResource.getProfile().getResourceUrl());
				profile = (Profile)nextResource.getProfile().loadResource(client);
			} catch (IOException e) {
				throw new MojoFailureException("Failed to load resource profile: "+nextResource.getProfile().getReference().getValue(),e);
			}

			ProfileParser pp = new ProfileParser();
			try {
				pp.parseSingleProfile(profile, nextResource.getProfile().getResourceUrl());
				File resourceDir = new File(myDirectoryBase, "resource");
				resourceDir.mkdirs();
				pp.writeAll(resourceDir, myPackageBase);
			} catch (Exception e) {
				throw new MojoFailureException("Failed to load resource profile: "+nextResource.getProfile().getReference().getValue(),e);
			}
			
		}
		
		try {
			write();
		} catch (IOException e) {
			throw new MojoFailureException("Failed to write", e);
		}

	}

	private void determinePaths() {
		myPackageBase = "";
		myDirectoryBase = myTargetDirectory;
		myClientClassSimpleName = myClientClassName;
		if (myClientClassName.lastIndexOf('.') > -1) {
			myPackageBase = myClientClassName.substring(0, myClientClassName.lastIndexOf('.'));
			myDirectoryBase = new File(myDirectoryBase, myPackageBase.replace(".", File.separatorChar+""));
			myClientClassSimpleName = myClientClassName.substring(myClientClassName.lastIndexOf('.')+1);
		}
		
		myDirectoryBase.mkdirs();
	}

	private void write() throws IOException {
		File file = new File(myDirectoryBase, myClientClassSimpleName + ".java");
		FileWriter w = new FileWriter(file, false);

		ourLog.info("Writing file: {}", file.getAbsolutePath());

		VelocityContext ctx = new VelocityContext();
		ctx.put("packageBase", myPackageBase);
		ctx.put("className", myClientClassSimpleName);
		ctx.put("resources", myResources);

		VelocityEngine v = new VelocityEngine();
		v.setProperty("resource.loader", "cp");
		v.setProperty("cp.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		v.setProperty("runtime.references.strict", Boolean.TRUE);

		InputStream templateIs = ResourceGeneratorUsingSpreadsheet.class.getResourceAsStream("/vm/client.vm");
		InputStreamReader templateReader = new InputStreamReader(templateIs);
		v.evaluate(ctx, w, "", templateReader);

		w.close();
	}
	
	public static void main(String[] args) throws ParseException, IOException, MojoFailureException, MojoExecutionException {

		// PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		// HttpClientBuilder builder = HttpClientBuilder.create();
		// builder.setConnectionManager(connectionManager);
		// CloseableHttpClient client = builder.build();
		//
		// HttpGet get = new HttpGet("http://fhir.healthintersections.com.au/open/metadata");
		// CloseableHttpResponse response = client.execute(get);
		//
		// String metadataString = EntityUtils.toString(response.getEntity());
		//
		// ourLog.info("Metadata String: {}", metadataString);

		// String metadataString = IOUtils.toString(new FileInputStream("src/test/resources/healthintersections-metadata.xml"));
		// Conformance conformance = new FhirContext(Conformance.class).newXmlParser().parseResource(Conformance.class, metadataString);

		TinderClientMojo mojo = new TinderClientMojo();
		mojo.myClientClassName = "ca.uhn.test.ClientClass";
		mojo.myTargetDirectory = new File("target/gen");
		mojo.execute();
	}

}
