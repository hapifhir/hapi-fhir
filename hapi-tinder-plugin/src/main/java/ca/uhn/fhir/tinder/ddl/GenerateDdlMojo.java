package ca.uhn.fhir.tinder.ddl;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

@Mojo(
	name = "generate-ddl",
	defaultPhase = LifecyclePhase.PROCESS_CLASSES,
	requiresDependencyCollection = ResolutionScope.COMPILE_PLUS_RUNTIME,
	requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME,
	threadSafe = true,
	requiresProject = true)
public class GenerateDdlMojo extends AbstractMojo {
	private static final Logger ourLog = LoggerFactory.getLogger(GenerateDdlMojo.class);

	@Parameter
	private List<String> packageNames;
	@Parameter
	private List<Dialect> dialects;
	@Parameter
	private String outputDirectory;
	@Parameter(defaultValue = "${project}", readonly = true)
	private transient MavenProject project;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		ourLog.info("Going to generate DDL files in directory: {}", outputDirectory);

		File outputDirectoryFile = new File(outputDirectory);
		if (outputDirectoryFile.mkdirs()) {
			ourLog.info("Created target directory");
		}

		DdlGeneratorHibernate61 generator = new DdlGeneratorHibernate61();

		for (String packageName : packageNames) {
			String t = trim(packageName);
			if (isNotBlank(t)) {
				generator.addPackage(packageName);
			}
		}

		for (Dialect nextDialect : dialects) {
			generator.addDialect(nextDialect);
		}

		generator.setOutputDirectory(outputDirectoryFile);
		generator.setProject(project);

		ourLog.info("Beginning DDL export");
		generator.generateDdl();
	}


	public static void main(String[] args) throws MojoExecutionException, MojoFailureException {
		GenerateDdlMojo m = new GenerateDdlMojo();
		m.packageNames = List.of("ca.uhn.fhir.jpa.model.entity");
		m.outputDirectory = "target";
		m.dialects = List.of(new Dialect("ca.uhn.fhir.jpa.model.dialect.HapiFhirPostgresDialect","hapifhirpostgres94.sql"));
		m.execute();
	}


	public static class Dialect {

		private String className;
		private String targetFileName;
		private String prependFile;

		public Dialect() {
			super();
		}

		public Dialect(String theClassName, String theTargetFileName) {
			super();
			setClassName(theClassName);
			setTargetFileName(theTargetFileName);
		}

		public String getClassName() {
			return className;
		}

		public void setClassName(String theClassName) {
			className = theClassName;
		}

		public String getTargetFileName() {
			return targetFileName;
		}

		public void setTargetFileName(String theTargetFileName) {
			targetFileName = theTargetFileName;
		}

		public String getPrependFile() {
			return prependFile;
		}

		public void setPrependFile(String thePrependFile) {
			prependFile = thePrependFile;
		}

	}


}
