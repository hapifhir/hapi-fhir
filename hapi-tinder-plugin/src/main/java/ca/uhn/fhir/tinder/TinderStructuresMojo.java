package ca.uhn.fhir.tinder;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.tinder.parser.DatatypeGeneratorUsingSpreadsheet;
import ca.uhn.fhir.tinder.parser.ResourceGeneratorUsingSpreadsheet;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	@Parameter(alias = "version", required = true, defaultValue="dstu")
	private String version = "dstu";

	@Parameter(required = false)
	private List<ValueSetFileDefinition> resourceValueSetFiles;

	@Parameter(required = true, defaultValue = "${project.build.directory}/generated-sources/tinder")
	private String targetDirectory;

	@Parameter(required = true, defaultValue = "${project.build.directory}/generated-resources/tinder")
	private String targetResourceDirectory;

	@Parameter(required = true, defaultValue = "${project.build.directory}/..")
	private String baseDir;
	
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {		
		if (StringUtils.isBlank(packageName)) {
			throw new MojoFailureException(Msg.code(101) + "Package not specified");
		}
		if (packageName.contains("..") || packageName.endsWith(".")) {
			throw new MojoFailureException(Msg.code(102) + "Invalid package specified");
		}

		ourLog.info("Beginning HAPI-FHIR Tinder Code Generation...");

		ourLog.info(" * Output Package: " + packageName);

		File resDirectoryBase = new File(new File(targetResourceDirectory), packageName.replace('.', File.separatorChar));
		resDirectoryBase.mkdirs();
		ourLog.info(" * Output Resource Directory: " + resDirectoryBase.getAbsolutePath());

		File directoryBase = new File(new File(targetDirectory), packageName.replace('.', File.separatorChar));
		directoryBase.mkdirs();
		ourLog.info(" * Output Source Directory: " + directoryBase.getAbsolutePath());

		ValueSetGenerator vsp = new ValueSetGenerator(version);
		vsp.setResourceValueSetFiles(resourceValueSetFiles);
		try {
			vsp.parse();
		} catch (Exception e) {
			throw new MojoFailureException(Msg.code(103) + "Failed to load valuesets", e);
		}

		/*
		 * A few enums are not found by default because none of the generated classes
		 * refer to them, but we still want them.
		 */
		vsp.getClassForValueSetIdAndMarkAsNeeded("NarrativeStatus");

		ourLog.info("Loading Datatypes...");

		Map<String, String> datatypeLocalImports = new HashMap<String, String>();
		DatatypeGeneratorUsingSpreadsheet dtp = new DatatypeGeneratorUsingSpreadsheet(version, baseDir);
		if (buildDatatypes) {
			try {
				dtp.parse();
				dtp.markResourcesForImports();
			} catch (Exception e) {
				throw new MojoFailureException(Msg.code(104) + "Failed to load datatypes", e);
			}
			dtp.bindValueSets(vsp);

			datatypeLocalImports = dtp.getLocalImports();
		}

		ResourceGeneratorUsingSpreadsheet rp = new ResourceGeneratorUsingSpreadsheet(version, baseDir);
		if (baseResourceNames != null && baseResourceNames.size() > 0) {
			ourLog.info("Loading Resources...");
			try {
				rp.setBaseResourceNames(baseResourceNames);
				rp.parse();
				rp.markResourcesForImports();
			} catch (Exception e) {
				throw new MojoFailureException(Msg.code(105) + "Failed to load resources", e);
			}

			rp.bindValueSets(vsp);
			rp.getLocalImports().putAll(datatypeLocalImports);
			datatypeLocalImports.putAll(rp.getLocalImports());

			File resSubDirectoryBase = new File(directoryBase, "resource");
			ourLog.info("Writing Resources to directory: {}", resSubDirectoryBase.getAbsolutePath());
			
			rp.combineContentMaps(dtp);
			rp.writeAll(resSubDirectoryBase, resDirectoryBase, packageName);
		}

		if (dtp != null) {
			ourLog.info("Writing Composite Datatypes...");
			
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

	public void setBaseResourceNames(List<String> theBaseResourceNames) {
		baseResourceNames = theBaseResourceNames;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String thePackageName) {
		packageName = thePackageName;
	}

	public List<ValueSetFileDefinition> getResourceValueSetFiles() {
		return resourceValueSetFiles;
	}

	public void setResourceValueSetFiles(List<ValueSetFileDefinition> theResourceValueSetFiles) {
		resourceValueSetFiles = theResourceValueSetFiles;
	}

	public String getTargetDirectory() {
		return targetDirectory;
	}

	public void setTargetDirectory(String theTargetDirectory) {
		targetDirectory = theTargetDirectory;
	}

	public boolean isBuildDatatypes() {
		return buildDatatypes;
	}

	public void setBuildDatatypes(boolean theBuildDatatypes) {
		buildDatatypes = theBuildDatatypes;
	}

	public static void main(String[] args) throws Exception {

		
//		ProfileParser pp = new ProfileParser();
//		pp.parseSingleProfile(new File("../hapi-tinder-test/src/test/resources/profile/patient.xml"), "http://foo");

		ValueSetGenerator vsp = new ValueSetGenerator("dstu2");
//		 vsp.setResourceValueSetFiles(theResourceValueSetFiles);Directory("src/main/resources/vs/");
		vsp.parse();

		DatatypeGeneratorUsingSpreadsheet dtp = new DatatypeGeneratorUsingSpreadsheet("dstu2", ".");
		dtp.parse();
		dtp.markResourcesForImports();
		dtp.bindValueSets(vsp);
		Map<String, String> datatypeLocalImports = dtp.getLocalImports();

		String dtOutputDir = "target/generated-sources/tinder/ca/uhn/fhir/model/dev/composite";

		ResourceGeneratorUsingSpreadsheet rp = new ResourceGeneratorUsingSpreadsheet("dstu2", ".");
		rp.setBaseResourceNames(Arrays.asList( "appointment"//, "auditevent" , "observation"  
//				//, "contract" 
//				"valueset", "organization", "location" 
//				, "observation", "conformance"
//				//, "referralrequest"
//				, "patient","practitioner","encounter",
//				"organization","location","relatedperson","appointment","slot","order"
//				//,"availability"
//				,"device", "valueset"
				));
		rp.parse();
		rp.bindValueSets(vsp);
		rp.markResourcesForImports();
		
		rp.bindValueSets(vsp);

		String rpOutputDir = "target/generated-sources/tinder/ca/uhn/fhir/model/dev/resource";
		String rpSOutputDir = "target/generated-resources/tinder/ca/uhn/fhir/model/dev";

		dtp.combineContentMaps(rp);
		rp.combineContentMaps(dtp);
		rp.getLocalImports().putAll(datatypeLocalImports);
		datatypeLocalImports.putAll(rp.getLocalImports());
		
		String vsOutputDir = "target/generated-sources/tinder/ca/uhn/fhir/model/dev/valueset";
		vsp.writeMarkedValueSets(new File(vsOutputDir), "ca.uhn.fhir.model.dev");
		
		dtp.writeAll(new File(dtOutputDir), null, "ca.uhn.fhir.model.dev");
		rp.writeAll(new File(rpOutputDir), new File(rpSOutputDir), "ca.uhn.fhir.model.dev");
		
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
