package ca.uhn.fhir.tinder;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.tinder.parser.BaseStructureSpreadsheetParser;
import ca.uhn.fhir.tinder.parser.ResourceGeneratorUsingModel;
import ca.uhn.fhir.tinder.parser.ResourceGeneratorUsingSpreadsheet;
import org.apache.commons.lang.WordUtils;
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
import org.apache.velocity.runtime.RuntimeConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.TreeSet;

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
	private String configPackageBase;

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
		String packageSuffix = "";
		if ("dstu2".equals(version)) {
			fhirContext = FhirContext.forDstu2();
		} else if ("dstu3".equals(version)) {
			fhirContext = FhirContext.forDstu3();
			packageSuffix = ".dstu3";
		} else if ("r4".equals(version)) {
			fhirContext = FhirContext.forR4();
			packageSuffix = ".r4";
		} else if ("r5".equals(version)) {
			fhirContext = FhirContext.forR5();
			packageSuffix = ".r5";
		} else {
			throw new MojoFailureException(Msg.code(113) + "Unknown version configured: " + version);
		}

		if (baseResourceNames == null || baseResourceNames.isEmpty()) {
			baseResourceNames = new ArrayList<>();

			ourLog.info("No resource names supplied, going to use all resources from version: {}", fhirContext.getVersion().getVersion());

			Properties p = new Properties();
			try {
				p.load(fhirContext.getVersion().getFhirVersionPropertiesFile());
			} catch (IOException e) {
				throw new MojoFailureException(Msg.code(114) + "Failed to load version property file", e);
			}

			ourLog.debug("Property file contains: {}", p);

			TreeSet<String> keys = new TreeSet<>();
			for (Object next : p.keySet()) {
				keys.add((String) next);
			}
			for (String next : keys) {
				if (next.startsWith("resource.")) {
					baseResourceNames.add(next.substring("resource.".length()).toLowerCase());
				}
			}

			if (fhirContext.getVersion().getVersion() == FhirVersionEnum.DSTU3) {
				baseResourceNames.remove("conformance");
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

		File configPackageDirectoryBase = new File(targetDirectory, configPackageBase.replace(".", File.separatorChar + ""));
		configPackageDirectoryBase.mkdirs();
		File packageDirectoryBase = new File(targetDirectory, packageBase.replace(".", File.separatorChar + ""));
		packageDirectoryBase.mkdirs();

		ResourceGeneratorUsingModel gen = new ResourceGeneratorUsingModel(version, baseDir);
		gen.setBaseResourceNames(baseResourceNames);

		try {
			gen.parse();

			gen.setFilenameSuffix("ResourceProvider");
			gen.setTemplate("/vm/jpa_resource_provider.vm");
			gen.writeAll(packageDirectoryBase, null, packageBase);

			// gen.setFilenameSuffix("ResourceTable");
			// gen.setTemplate("/vm/jpa_resource_table.vm");
			// gen.writeAll(directoryBase, packageBase);

		} catch (Exception e) {
			throw new MojoFailureException(Msg.code(115) + "Failed to generate server", e);
		}

		myProject.addCompileSourceRoot(targetDirectory.getAbsolutePath());

		try {
			VelocityContext ctx = new VelocityContext();
			ctx.put("resources", gen.getResources());
			ctx.put("packageBase", packageBase);
			ctx.put("configPackageBase", configPackageBase);
			ctx.put("version", version);
			ctx.put("package_suffix", packageSuffix);
			ctx.put("esc", new TinderResourceGeneratorMojo.EscapeTool());
			if (BaseStructureSpreadsheetParser.determineVersionEnum(version).isRi()) {
				ctx.put("resourcePackage", "org.hl7.fhir." + version + ".model");
			} else {
				ctx.put("resourcePackage", "ca.uhn.fhir.model." + version + ".resource");
			}

			String capitalize = WordUtils.capitalize(version);
			if ("Dstu".equals(capitalize)) {
				capitalize = "Dstu1";
			}
			ctx.put("versionCapitalized", capitalize);

			VelocityEngine v = new VelocityEngine();
			v.setProperty(RuntimeConstants.RESOURCE_LOADERS, "cp");
			v.setProperty("resource.loader.cp.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			v.setProperty("runtime.strict_mode.enable", Boolean.TRUE);


			/*
			 * Spring XML
			 */
			InputStream templateIs = ResourceGeneratorUsingSpreadsheet.class.getResourceAsStream("/vm/jpa_spring_beans.vm");
			InputStreamReader templateReader = new InputStreamReader(templateIs);
			targetResourceDirectory.mkdirs();
			File f = new File(targetResourceDirectory, targetResourceSpringBeansFile);
			OutputStreamWriter w = new OutputStreamWriter(new FileOutputStream(f, false), "UTF-8");
			v.evaluate(ctx, w, "", templateReader);
			w.close();

			Resource resource = new Resource();
			resource.setDirectory(targetResourceDirectory.getAbsolutePath());
			resource.addInclude(targetResourceSpringBeansFile);
			myProject.addResource(resource);

			/*
			 * Spring Java
			 */
			templateIs = ResourceGeneratorUsingSpreadsheet.class.getResourceAsStream("/vm/jpa_spring_beans_java.vm");
			templateReader = new InputStreamReader(templateIs);
			f = new File(configPackageDirectoryBase, "GeneratedDaoAndResourceProviderConfig" + capitalize + ".java");
			w = new OutputStreamWriter(new FileOutputStream(f, false), "UTF-8");
			v.evaluate(ctx, w, "", templateReader);
			w.close();

		} catch (Exception e) {
			throw new MojoFailureException(Msg.code(116) + "Failed to generate server", e);
		}
	}

	public static void main(String[] args) throws IOException, MojoFailureException, MojoExecutionException {

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
		mojo.myProject = new MavenProject();
		mojo.version = "dstu2";
		mojo.packageBase = "ca.uhn.fhir.jpa.rp.r4";
		mojo.configPackageBase = "ca.uhn.fhir.jpa.config";
		mojo.baseResourceNames = new ArrayList<String>(Arrays.asList(
			"bundle",
			"observation",
//				"communicationrequest"
			"binary",
			"structuredefinition"
		));
		mojo.targetDirectory = new File("target/generated/valuesets");
		mojo.targetResourceDirectory = new File("target/generated/valuesets");
		mojo.targetResourceSpringBeansFile = "tmp_beans.xml";
		mojo.execute();
	}

}
