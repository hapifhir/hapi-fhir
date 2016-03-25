package ca.uhn.fhir.tinder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.TreeSet;

import org.apache.commons.lang.WordUtils;
import org.apache.http.ParseException;
import org.apache.maven.model.FileSet;
import org.apache.maven.model.PatternSet;
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
import org.apache.velocity.tools.generic.EscapeTool;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.tinder.parser.BaseStructureSpreadsheetParser;
import ca.uhn.fhir.tinder.parser.ResourceGeneratorUsingSpreadsheet;

@Mojo(name = "generate-single-file", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class TinderGenericSingleFileMojo extends AbstractMojo {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TinderGenericSingleFileMojo.class);

	// one of these two is required
	@Parameter(required = false)
	private String template;
	@Parameter(required = false)
	private File templateFile;

	@Parameter(required = true, defaultValue = "${project.build.directory}/generated-sources/tinder")
	private File targetDirectory;

	@Parameter(required = false)
	private String targetFolder;

	@Parameter(required = false)
	private String targetPackage;

	@Parameter(required = true)
	private String targetFile;

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
		
		ResourceGeneratorUsingSpreadsheet gen = new ResourceGeneratorUsingSpreadsheet(version, baseDir);
		gen.setBaseResourceNames(baseResourceNames);

		try {
			gen.parse();

			VelocityContext ctx = new VelocityContext();
			ctx.put("resources", gen.getResources());
			ctx.put("packageBase", packageBase);
			ctx.put("targetPackage", targetPackage);
			ctx.put("version", version);
			ctx.put("esc", new EscapeTool());
			if (BaseStructureSpreadsheetParser.determineVersionEnum(version).isRi()) {
				ctx.put("resourcePackage", "org.hl7.fhir." + version + ".model");
			} else {
				ctx.put("resourcePackage", "ca.uhn.fhir.model." + version + ".resource");
			}
			
			String capitalize = WordUtils.capitalize(version);
			if ("Dstu".equals(capitalize)) {
				capitalize="Dstu1";
			}
			ctx.put("versionCapitalized", capitalize);

			VelocityEngine v = new VelocityEngine();
			v.setProperty("resource.loader", "cp");
			v.setProperty("cp.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			v.setProperty("runtime.references.strict", Boolean.TRUE);

			InputStream templateIs = null;
			if (templateFile != null) {
				templateIs = new FileInputStream(templateFile);
			} else {
				templateIs = ResourceGeneratorUsingSpreadsheet.class.getResourceAsStream(template);
			}
			InputStreamReader templateReader = new InputStreamReader(templateIs);

			File target = targetDirectory;
			if (targetFolder != null) {
				targetFolder = targetFolder.replace('\\', '/');
				targetFolder = targetFolder.replace('/', File.separatorChar);
				target = new File(targetDirectory, targetFolder);
			} else if (targetPackage != null) {
				target = new File(targetDirectory, targetPackage.replace('.', File.separatorChar));
			}
			target.mkdirs();
			File f = new File(target, targetFile);
			OutputStreamWriter w = new OutputStreamWriter(new FileOutputStream(f, false), "UTF-8");
			
			v.evaluate(ctx, w, "", templateReader);
			w.close();
			
			if (targetFile.endsWith(".java")) {
				myProject.addCompileSourceRoot(targetDirectory.getAbsolutePath());
			} else {
				Resource resource = new Resource();
				resource.setDirectory(targetDirectory.getAbsolutePath());
				String resName = targetFile;
				if (targetFolder != null) {
					resName = targetFolder+File.separator+targetFile;
				}
				resource.addInclude(resName);
				myProject.addResource(resource);
			}

		} catch (Exception e) {
			throw new MojoFailureException("Failed to generate file", e);
		}
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

		TinderGenericSingleFileMojo mojo = new TinderGenericSingleFileMojo();
		mojo.myProject = new MavenProject();
		mojo.template = "/vm/jpa_spring_beans.vm";
		mojo.version = "dstu2";
		mojo.packageBase = "ca.uhn.test";
		mojo.targetDirectory = new File("target/generated/valuesets");
		mojo.targetFile = "tmp_beans.xml";
		mojo.execute();
	}

}
