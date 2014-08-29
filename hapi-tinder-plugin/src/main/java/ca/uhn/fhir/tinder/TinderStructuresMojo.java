package ca.uhn.fhir.tinder;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import ca.uhn.fhir.tinder.parser.DatatypeGeneratorUsingSpreadsheet;
import ca.uhn.fhir.tinder.parser.ProfileParser;
import ca.uhn.fhir.tinder.parser.ResourceGeneratorUsingSpreadsheet;

@Mojo(name = "generate-structures", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class TinderStructuresMojo extends AbstractMojo {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TinderStructuresMojo.class);

	@Parameter(required = false)
	private List<String> baseResourceNames;

	@Parameter(required = false, defaultValue = "false")
	private boolean buildDatatypes;

	@Component
	private MavenProject myProject;

	@Parameter(alias = "package", required = true)
	private String packageName;

	@Parameter(required = false)
	private List<ProfileFileDefinition> resourceProfileFiles;

	@Parameter(required = false)
	private List<ValueSetFileDefinition> resourceValueSetFiles;

	@Parameter(required = true, defaultValue = "${project.build.directory}/generated-sources/tinder")
	private String targetDirectory;

	@Parameter(required = true, defaultValue = "${project.build.directory}/generated-resources/tinder")
	private String targetResourceDirectory;

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

		File resDirectoryBase = new File(new File(targetResourceDirectory), packageName.replace('.', File.separatorChar));
		resDirectoryBase.mkdirs();
		ourLog.info(" * Output Resource Directory: " + resDirectoryBase.getAbsolutePath());

		File directoryBase = new File(new File(targetDirectory), packageName.replace('.', File.separatorChar));
		directoryBase.mkdirs();
		ourLog.info(" * Output Source Directory: " + directoryBase.getAbsolutePath());

		ValueSetGenerator vsp = new ValueSetGenerator();
		vsp.setResourceValueSetFiles(resourceValueSetFiles);
		try {
			vsp.parse();
		} catch (Exception e) {
			throw new MojoFailureException("Failed to load valuesets", e);
		}

		ourLog.info("Loading Datatypes...");

		Map<String, String> datatypeLocalImports = new HashMap<String, String>();
		DatatypeGeneratorUsingSpreadsheet dtp = new DatatypeGeneratorUsingSpreadsheet();
		if (buildDatatypes) {
			try {
				dtp.parse();
				dtp.markResourcesForImports();
			} catch (Exception e) {
				throw new MojoFailureException("Failed to load datatypes", e);
			}
			dtp.bindValueSets(vsp);

			datatypeLocalImports = dtp.getLocalImports();
		}

		ResourceGeneratorUsingSpreadsheet rp = new ResourceGeneratorUsingSpreadsheet();
		if (baseResourceNames != null && baseResourceNames.size() > 0) {
			ourLog.info("Loading Resources...");
			try {
				rp.setBaseResourceNames(baseResourceNames);
				rp.parse();
				rp.markResourcesForImports();
			} catch (Exception e) {
				throw new MojoFailureException("Failed to load resources", e);
			}

			rp.bindValueSets(vsp);
			rp.getLocalImports().putAll(datatypeLocalImports);
			datatypeLocalImports.putAll(rp.getLocalImports());

			ourLog.info("Writing Resources...");
			File resSubDirectoryBase = new File(directoryBase, "resource");
			rp.combineContentMaps(dtp);
			rp.writeAll(resSubDirectoryBase, resDirectoryBase, packageName);
		}

		ProfileParser pp = new ProfileParser();
		if (resourceProfileFiles != null) {
			ourLog.info("Loading profiles...");
			for (ProfileFileDefinition next : resourceProfileFiles) {
				ourLog.info("Parsing file: {}", next.profileFile);
				pp.parseSingleProfile(new File(next.profileFile), next.profileSourceUrl);
			}

			pp.bindValueSets(vsp);
			pp.markResourcesForImports();
			pp.getLocalImports().putAll(datatypeLocalImports);
			datatypeLocalImports.putAll(pp.getLocalImports());

			pp.combineContentMaps(rp);
			pp.combineContentMaps(dtp);
			pp.writeAll(new File(directoryBase, "resource"), resDirectoryBase, packageName);
		}

		
		if (dtp != null) {
			ourLog.info("Writing Composite Datatypes...");
			
			dtp.combineContentMaps(pp);
			dtp.combineContentMaps(rp);
			dtp.writeAll(new File(directoryBase, "composite"), resDirectoryBase, packageName);
		}

		ourLog.info("Writing ValueSet Enums...");
		vsp.writeMarkedValueSets(new File(directoryBase, "valueset"), packageName);

		myProject.addCompileSourceRoot(targetDirectory);
	}

	public List<String> getBaseResourceNames() {
		return baseResourceNames;
	}

	public String getPackageName() {
		return packageName;
	}

	public List<ProfileFileDefinition> getResourceProfileFiles() {
		return resourceProfileFiles;
	}

	public List<ValueSetFileDefinition> getResourceValueSetFiles() {
		return resourceValueSetFiles;
	}

	public String getTargetDirectory() {
		return targetDirectory;
	}

	public boolean isBuildDatatypes() {
		return buildDatatypes;
	}

	public void setBaseResourceNames(List<String> theBaseResourceNames) {
		baseResourceNames = theBaseResourceNames;
	}

	public void setBuildDatatypes(boolean theBuildDatatypes) {
		buildDatatypes = theBuildDatatypes;
	}

	public void setPackageName(String thePackageName) {
		packageName = thePackageName;
	}

	public void setResourceProfileFiles(List<ProfileFileDefinition> theResourceProfileFiles) {
		resourceProfileFiles = theResourceProfileFiles;
	}

	public void setResourceValueSetFiles(List<ValueSetFileDefinition> theResourceValueSetFiles) {
		resourceValueSetFiles = theResourceValueSetFiles;
	}

	public void setTargetDirectory(String theTargetDirectory) {
		targetDirectory = theTargetDirectory;
	}

	public static void main(String[] args) throws Exception {

//		ProfileParser pp = new ProfileParser();
//		pp.parseSingleProfile(new File("../hapi-tinder-test/src/test/resources/profile/patient.xml"), "http://foo");

		ValueSetGenerator vsp = new ValueSetGenerator();
//		 vsp.setResourceValueSetFiles(theResourceValueSetFiles);Directory("src/main/resources/vs/");
		vsp.parse();

		DatatypeGeneratorUsingSpreadsheet dtp = new DatatypeGeneratorUsingSpreadsheet();
		dtp.parse();
		dtp.bindValueSets(vsp);

		String dtOutputDir = "target/generated-sources/ca/uhn/fhir/model/dstu/composite";
		dtp.writeAll(new File(dtOutputDir), null, "ca.uhn.fhir.model.dstu");

		ResourceGeneratorUsingSpreadsheet rp = new ResourceGeneratorUsingSpreadsheet();
		rp.setBaseResourceNames(Arrays.asList("securityevent"));
		rp.parse();
		rp.bindValueSets(vsp);
		
		// rp.bindValueSets(vsp);

		String rpOutputDir = "target/generated-sources/ca/uhn/fhir/model/dstu/resource";
		String rpSOutputDir = "target/generated-resources/ca/uhn/fhir/model/dstu";
		
		rp.combineContentMaps(dtp);
		rp.writeAll(new File(rpOutputDir), new File(rpSOutputDir), "ca.uhn.fhir.model.dstu");
		
		 String vsOutputDir = "target/generated-sources/ca/uhn/fhir/model/dstu/valueset";
		 vsp.writeMarkedValueSets(new File(vsOutputDir), "ca.uhn.fhir.model.dstu");
	}

	public static class ProfileFileDefinition {
		@Parameter(required = true)
		private String profileFile;

		@Parameter(required = true)
		private String profileSourceUrl;
	}

	public static class ValueSetFileDefinition {
		@Parameter(required = true)
		private String valueSetFile;

		public String getValueSetFile() {
			return valueSetFile;
		}

		public void setValueSetFile(String theValueSetFile) {
			valueSetFile = theValueSetFile;
		}
	}

}
