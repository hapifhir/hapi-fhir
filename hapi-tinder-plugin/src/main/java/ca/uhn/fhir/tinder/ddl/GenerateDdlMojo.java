package ca.uhn.fhir.tinder.ddl;

import ca.uhn.fhir.jpa.model.dialect.IHapiFhirDialect;
import ca.uhn.fhir.util.ReflectionUtil;
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
import java.util.ArrayList;
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
	public List<String> packageNames;

	@Parameter
	public List<Dialect> dialects;

	@Parameter
	public String outputDirectory;

	@Parameter(defaultValue = "true", required = false)
	public Boolean databasePartitionMode;

	@Parameter(defaultValue = "false")
	boolean skip;

	@Parameter(defaultValue = "${project}", readonly = true)
	private transient MavenProject project;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		if (skip) {
			return;
		}

		ourLog.info("Going to generate DDL files in directory: {}", outputDirectory);

		File outputDirectoryFile = new File(outputDirectory);
		if (outputDirectoryFile.mkdirs()) {
			ourLog.info("Created target directory");
		}

		DdlGeneratorHibernate61 generator = new DdlGeneratorHibernate61();

		/*
		 * We default to database partition mode TRUE because that mode means we don't
		 * modify the entity metadata at all before generating the DDL. When Database
		 * Partition Mode is disabled (FALSE) we are filtering elements and modifying
		 * the metadata.
		 */
		generator
				.getHapiHibernateDialectSettingsService()
				.setDatabasePartitionMode(databasePartitionMode != null ? databasePartitionMode : true);

		for (String packageName : packageNames) {
			String t = trim(packageName);
			if (isNotBlank(t)) {
				generator.addPackage(packageName);
			}
		}

		for (Dialect nextDialect : dialects) {
			IHapiFhirDialect instance = ReflectionUtil.newInstance(
					nextDialect.getClassName(), IHapiFhirDialect.class, new Class[0], new Object[0]);
			switch (instance.getDriverType()) {
				case H2_EMBEDDED:
				case DERBY_EMBEDDED:
				case MARIADB_10_1:
				case MYSQL_5_7:
				case COCKROACHDB_21_1:
					break;
				case POSTGRES_9_4:
				case MSSQL_2012:
				case ORACLE_12C:
					/*
					 * This is a hardcoded fix to remove the FK constraint from FK_RES_LINK to the
					 * target resource. This constraint hurts performance so we drop it on several
					 * platforms, but we want to leave it in for H2 so that unit tests can catch
					 * issues.
					 *
					 * In the future it may be possible to do this in a cleaner way, but for now
					 * we have to leave the constraint as-is in the entity classes because of this
					 * bug:
					 * https://hibernate.atlassian.net/browse/HHH-19046
					 */
					if (nextDialect.getDropStatementsContainingRegex() == null) {
						nextDialect.setDropStatementsContainingRegex(new ArrayList<>());
					}
					nextDialect.dropStatementsContainingRegex.add("add constraint FK_RESLINK_TARGET");
					break;
			}

			generator.addDialect(nextDialect);
		}

		generator.setOutputDirectory(outputDirectoryFile);
		generator.setProject(project);

		ourLog.info("Beginning DDL export");
		generator.generateDdl();
	}

	public static void main(String[] args) throws MojoExecutionException, MojoFailureException {
		/*
		 * Note, to execute this for real entities, add the following snippet to this module's POM. The whole project won't work with
		 * that added, but you can add it temporarily in order to debug this in IJ:
		 * 		<dependency>
		 * 			<groupId>ca.uhn.hapi.fhir</groupId>
		 * 			<artifactId>hapi-fhir-jpaserver-model</artifactId>
		 * 			<version>${project.version}</version>
		 * 		</dependency>
		 *
		 * Alternately, there is a unit test with fake entities that also runs this class.
		 */
		GenerateDdlMojo m = new GenerateDdlMojo();
		m.packageNames = List.of("ca.uhn.fhir.jpa.model.entity");
		m.outputDirectory = "hapi-tinder-plugin/target";
		m.dialects = List.of(new Dialect("ca.uhn.fhir.jpa.model.dialect.HapiFhirPostgresDialect", "postgres.sql"));
		m.execute();
	}

	public static class Dialect {

		private String className;
		private String targetFileName;
		private String prependFile;
		private String appendFile;
		private List<String> dropStatementsContainingRegex;

		public Dialect() {
			super();
		}

		public Dialect(String theClassName, String theTargetFileName) {
			super();
			setClassName(theClassName);
			setTargetFileName(theTargetFileName);
		}

		public List<String> getDropStatementsContainingRegex() {
			return dropStatementsContainingRegex;
		}

		public void setDropStatementsContainingRegex(List<String> theDropStatementsContainingRegex) {
			dropStatementsContainingRegex = theDropStatementsContainingRegex;
		}

		public String getAppendFile() {
			return appendFile;
		}

		public void setAppendFile(String theAppendFile) {
			appendFile = theAppendFile;
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
