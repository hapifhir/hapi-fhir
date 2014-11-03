package ca.uhn.fhir.tinder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.ParseException;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import ca.uhn.fhir.tinder.parser.ResourceGeneratorUsingSpreadsheet;

@Mojo(name = "generate-jparest-server", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class TinderJpaRestServerMojo extends AbstractMojo {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TinderJpaRestServerMojo.class);

	@Parameter(required = true, defaultValue = "${project.build.directory}/generated-sources/tinder")
	private File targetDirectory;

	@Parameter(required = true, defaultValue = "${project.build.directory}/generated-resources/tinder")
	private File targetResourceDirectory;

	@Parameter(required = true, defaultValue = "hapi-jpaserver-springbeans.xml")
	private String targetResourceSpringBeansFile;

	@Parameter(required = true)
	private String packageBase;

	@Parameter(required = true)
	private List<String> baseResourceNames;

	@Parameter(required = true, defaultValue = "${project.build.directory}/..")
	private String baseDir;
	
	@Component
	private MavenProject myProject;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {

		File directoryBase = new File(targetDirectory, packageBase.replace(".", File.separatorChar + ""));
		directoryBase.mkdirs();

		ResourceGeneratorUsingSpreadsheet gen = new ResourceGeneratorUsingSpreadsheet("dstu", baseDir);
		gen.setBaseResourceNames(baseResourceNames);

		try {
			gen.parse();

			gen.setFilenameSuffix("ResourceProvider");
			gen.setTemplate("/vm/jpa_resource_provider.vm");
			gen.writeAll(directoryBase, null,packageBase);

			// gen.setFilenameSuffix("ResourceTable");
			// gen.setTemplate("/vm/jpa_resource_table.vm");
			// gen.writeAll(directoryBase, packageBase);

		} catch (Exception e) {
			throw new MojoFailureException("Failed to generate server", e);
		}

		myProject.addCompileSourceRoot(directoryBase.getAbsolutePath());

		try {
			VelocityContext ctx = new VelocityContext();
			ctx.put("resources", gen.getResources());

			VelocityEngine v = new VelocityEngine();
			v.setProperty("resource.loader", "cp");
			v.setProperty("cp.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			v.setProperty("runtime.references.strict", Boolean.TRUE);

			InputStream templateIs = ResourceGeneratorUsingSpreadsheet.class.getResourceAsStream("/vm/jpa_spring_beans.vm");
			InputStreamReader templateReader = new InputStreamReader(templateIs);

			targetResourceDirectory.mkdirs();
			FileWriter w = new FileWriter(new File(targetResourceDirectory, targetResourceSpringBeansFile));
			v.evaluate(ctx, w, "", templateReader);

			w.close();

			Resource resource = new Resource();
			resource.setDirectory(targetResourceDirectory.getAbsolutePath());
			resource.addInclude(targetResourceSpringBeansFile);
			myProject.addResource(resource);
		} catch (Exception e) {
			throw new MojoFailureException("Failed to generate server", e);
		}
	}

	private void write() throws IOException {
		// File directoryBase;
		// directoryBase.mkdirs();
		//
		// File file = new File(directoryBase, myClientClassSimpleName + ".java");
		// FileWriter w = new FileWriter(file, false);
		//
		// ourLog.info("Writing file: {}", file.getAbsolutePath());
		//
		// VelocityContext ctx = new VelocityContext();
		// ctx.put("packageBase", packageBase);
		// ctx.put("className", myClientClassSimpleName);
		// ctx.put("resources", myResources);
		//
		// VelocityEngine v = new VelocityEngine();
		// v.setProperty("resource.loader", "cp");
		// v.setProperty("cp.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		// v.setProperty("runtime.references.strict", Boolean.TRUE);
		//
		// InputStream templateIs = ResourceGeneratorUsingSpreadsheet.class.getResourceAsStream("/vm/client.vm");
		// InputStreamReader templateReader = new InputStreamReader(templateIs);
		// v.evaluate(ctx, w, "", templateReader);
		//
		// w.close();
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

		TinderJpaRestServerMojo mojo = new TinderJpaRestServerMojo();
		mojo.packageBase = "ca.uhn.test";
		mojo.baseResourceNames = java.util.Collections.singletonList("observation");
		mojo.targetDirectory = new File("target/generated/valuesets");
		mojo.execute();
	}

}
