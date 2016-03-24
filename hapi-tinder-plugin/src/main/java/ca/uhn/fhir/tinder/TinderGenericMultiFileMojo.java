package ca.uhn.fhir.tinder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.TreeSet;

import org.apache.http.ParseException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.tinder.parser.ResourceGeneratorUsingSpreadsheet;

@Mojo(name = "generate-multi-files", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class TinderGenericMultiFileMojo extends AbstractMojo {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TinderGenericMultiFileMojo.class);

	// one of these two is required
	@Parameter(required = false)
	private String template;
	@Parameter(required = false)
	private File templateFile;
	
	@Parameter(required = true, defaultValue = "ResourceProvider")
	private String filenameSuffix;
	
	@Parameter(required = true, defaultValue = "${project.build.directory}/generated-sources/tinder")
	private File targetDirectory;

	@Parameter(required = true)
	private String packageBase;

	@Parameter(required = false)
	private List<String> baseResourceNames;

	@Parameter(required = false)
	private List<String> excludeResourceNames;

	@Parameter(required = true, defaultValue = "${project.build.directory}/..")
	private String baseDir;

	@Parameter(required = true)
	private String version;

	@Component
	private MavenProject myProject;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {

		FhirContext fhirContext;
		if ("dstu".equals(version)) {
			fhirContext = FhirContext.forDstu1();
		} else if ("dstu2".equals(version)) {
			fhirContext = FhirContext.forDstu2();
		} else if ("dstu3".equals(version)) {
			fhirContext = FhirContext.forDstu3();
		} else {
			throw new MojoFailureException("Unknown version configured: " + version);
		}
		
		if (baseResourceNames == null || baseResourceNames.isEmpty()) {
			baseResourceNames = new ArrayList<String>();
			
			ourLog.info("No resource names supplied, going to use all resources from version: {}",fhirContext.getVersion().getVersion());
			
			Properties p = new Properties();
			try {
				p.load(fhirContext.getVersion().getFhirVersionPropertiesFile());
			} catch (IOException e) {
				throw new MojoFailureException("Failed to load version property file", e);
			}

			ourLog.debug("Property file contains: {}",p);

			TreeSet<String> keys = new TreeSet<String>();
			for(Object next : p.keySet()) {
				keys.add((String) next);
			}
			for (String next : keys) {
				if (next.startsWith("resource.")) {
					baseResourceNames.add(next.substring("resource.".length()).toLowerCase());
				}
			}
		}

		for (int i = 0; i < baseResourceNames.size(); i++) {
			baseResourceNames.set(i, baseResourceNames.get(i).toLowerCase());
		}

		if (excludeResourceNames != null) {
			for (int i = 0; i < excludeResourceNames.size(); i++) {
				excludeResourceNames.set(i, excludeResourceNames.get(i).toLowerCase());
			}
			baseResourceNames.removeAll(excludeResourceNames);
		}
		
		ourLog.info("Including the following resources: {}", baseResourceNames);
		
		File packageDirectoryBase = new File(targetDirectory, packageBase.replace(".", File.separatorChar + ""));
		packageDirectoryBase.mkdirs();

		ResourceGeneratorUsingSpreadsheet gen = new ResourceGeneratorUsingSpreadsheet(version, baseDir);
		gen.setBaseResourceNames(baseResourceNames);

		try {
			gen.parse();

			gen.setFilenameSuffix(filenameSuffix);
			gen.setTemplate(template);
			gen.setTemplateFile(templateFile);
			gen.writeAll(packageDirectoryBase, null,packageBase);

		} catch (Exception e) {
			throw new MojoFailureException("Failed to generate files", e);
		}

		myProject.addCompileSourceRoot(targetDirectory.getAbsolutePath());

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

		TinderGenericMultiFileMojo mojo = new TinderGenericMultiFileMojo();
		mojo.myProject = new MavenProject();
		mojo.version = "dstu2";
		mojo.packageBase = "ca.uhn.test";
		mojo.template = "/vm/jpa_resource_provider.vm";
		mojo.targetDirectory = new File("target/generated/valuesets");
		mojo.execute();
	}

}
