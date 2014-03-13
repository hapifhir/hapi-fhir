package ca.uhn.fhir.tinder;

import java.io.File;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name = "generate-structures", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class TinderStructuresMojo extends AbstractMojo {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TinderStructuresMojo.class);

	@Parameter(alias = "package", required = true)
	private String packageName;

	@Parameter(alias = "targetDirectory", required = true, defaultValue = "${project.build.directory}/generated-sources/tinder")
	private String targetDirectory;

	@Parameter(alias="resourceValueSetFiles", required = false)
	private List<String> resourceValueSetFiles;

	@Parameter(alias = "baseResourceNames", required = true)
	private List<String> baseResourceNames;

	@Parameter(alias = "resourceProfileFiles", required = false)
	private List<String> resourceProfileFiles;

	@Component
	private MavenProject myProject;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		if (StringUtils.isBlank(packageName)) {
			throw new MojoFailureException("Package not specified");
		}
		if (packageName.contains("..") || packageName.endsWith(".")) {
			throw new MojoFailureException("Invalid package specified");
		}

		ourLog.info("Beginning HAPI-FHIR Tinder Code Generation...");

		ourLog.info(" * Output Package: " + packageName);
		File directoryBase = new File(new File(targetDirectory), packageName.replace('.', File.separatorChar));
		directoryBase.mkdirs();
		ourLog.info(" * Output Directory: " + directoryBase.getAbsolutePath());

		ValueSetGenerator vsp = new ValueSetGenerator();
		vsp.setResourceValueSetFiles(resourceValueSetFiles);
		try {
			vsp.parse();
		} catch (Exception e) {
			throw new MojoFailureException("Failed to load valuesets", e);
		}

		ourLog.info("Loading Datatypes...");

		DatatypeGeneratorUsingSpreadsheet dtp = new DatatypeGeneratorUsingSpreadsheet();
		try {
			dtp.parse();
		} catch (Exception e) {
			throw new MojoFailureException("Failed to load datatypes", e);
		}
		dtp.bindValueSets(vsp);

		ourLog.info("Loading Resources...");
		ResourceGeneratorUsingSpreadsheet rp = new ResourceGeneratorUsingSpreadsheet();
		try {
			rp.setBaseResourceNames(baseResourceNames);
			rp.parse();
		} catch (Exception e) {
			throw new MojoFailureException("Failed to load resources", e);
		}
		rp.bindValueSets(vsp);

		if (resourceProfileFiles != null) {
			ourLog.info("Loading profiles...");
			ProfileParser pp = new ProfileParser();
			pp.parseBaseResources(resourceProfileFiles);
			pp.bindValueSets(vsp);
			pp.writeAll(new File(directoryBase, "resource"), packageName);
		}

		ourLog.info("Writing Resources...");
		rp.writeAll(new File(directoryBase, "resource"), packageName);

		ourLog.info("Writing Composite Datatypes...");
		dtp.writeAll(new File(directoryBase, "composite"), packageName);

		ourLog.info("Writing ValueSet Enums...");
		vsp.writeMarkedValueSets(new File(directoryBase, "valueset"), packageName);

		myProject.addCompileSourceRoot(targetDirectory);
	}

	public static void main(String[] args) throws Exception {

		// ValueSetGenerator vsp = new ValueSetGenerator();
		// vsp.setDirectory("src/test/resources/vs/");
		// vsp.parse();
		//
		// DatatypeGeneratorUsingSpreadsheet dtp = new DatatypeGeneratorUsingSpreadsheet();
		// dtp.parse();
		// dtp.bindValueSets(vsp);
		//
		// String dtOutputDir = "target/generated/valuesets/ca/uhn/fhir/model/dstu/composite";
		// dtp.writeAll(dtOutputDir);
		//
		// ResourceGeneratorUsingSpreadsheet rp = new ResourceGeneratorUsingSpreadsheet();
		// rp.parse();
		// rp.bindValueSets(vsp);
		//
		// String rpOutputDir = "target/generated/valuesets/ca/uhn/fhir/model/dstu/resource";
		// rp.writeAll(rpOutputDir);
		//
		// String vsOutputDir = "target/generated/valuesets/ca/uhn/fhir/model/dstu/valueset";
		// vsp.writeMarkedValueSets(vsOutputDir);
	}

}
