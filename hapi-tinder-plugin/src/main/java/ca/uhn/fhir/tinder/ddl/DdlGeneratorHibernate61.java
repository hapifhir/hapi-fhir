package ca.uhn.fhir.tinder.ddl;

import ca.uhn.fhir.jpa.migrate.util.SqlUtil;
import ca.uhn.fhir.jpa.util.ISequenceValueMassager;
import ca.uhn.fhir.util.IoUtil;
import ca.uhn.hapi.fhir.sql.hibernatesvc.HapiHibernateDialectSettingsService;
import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.MappedSuperclass;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.internal.MetadataImpl;
import org.hibernate.boot.registry.BootstrapServiceRegistry;
import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.JdbcSettings;
import org.hibernate.cfg.SchemaToolingSettings;
import org.hibernate.engine.jdbc.connections.internal.UserSuppliedConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class DdlGeneratorHibernate61 {
	private static final Logger ourLog = LoggerFactory.getLogger(DdlGeneratorHibernate61.class);
	private final Set<String> myPackages = new HashSet<>();
	private final List<GenerateDdlMojo.Dialect> myDialects = new ArrayList<>();
	private File myOutputDirectory;
	private MavenProject myProject;
	private final HapiHibernateDialectSettingsService myHapiHibernateDialectSettingsService =
			new HapiHibernateDialectSettingsService();

	public void addPackage(String thePackage) {
		Validate.notNull(thePackage, "thePackage must not be null");
		myPackages.add(thePackage);
	}

	public void addDialect(GenerateDdlMojo.Dialect theDialect) {
		Validate.notBlank(theDialect.getClassName());
		Validate.notBlank(theDialect.getTargetFileName());
		myDialects.add(theDialect);
	}

	public HapiHibernateDialectSettingsService getHapiHibernateDialectSettingsService() {
		return myHapiHibernateDialectSettingsService;
	}

	public void setOutputDirectory(File theOutputDirectory) {
		Validate.notNull(theOutputDirectory, "theOutputDirectory must not be null");
		myOutputDirectory = theOutputDirectory;
	}

	public void generateDdl() throws MojoFailureException {
		ClassLoader classLoader = getClassLoader(myProject);
		FakeConnectionConnectionProvider connectionProvider = new FakeConnectionConnectionProvider();
		Set<Class<?>> entityClasses = scanClasspathForEntityClasses(myPackages, classLoader);

		BootstrapServiceRegistryBuilder bootstrapServiceRegistryBuilder = new BootstrapServiceRegistryBuilder();
		bootstrapServiceRegistryBuilder.applyClassLoader(classLoader);
		bootstrapServiceRegistryBuilder.enableAutoClose();
		BootstrapServiceRegistry bootstrapServiceRegistry = bootstrapServiceRegistryBuilder.build();

		for (GenerateDdlMojo.Dialect nextDialect : myDialects) {
			String fileName = nextDialect.getTargetFileName();
			String dialectClassName = nextDialect.getClassName();

			StandardServiceRegistryBuilder registryBuilder =
					new StandardServiceRegistryBuilder(bootstrapServiceRegistry);
			registryBuilder.applySetting(SchemaToolingSettings.HBM2DDL_AUTO, "create");
			registryBuilder.applySetting(JdbcSettings.DIALECT, dialectClassName);
			registryBuilder.addService(ConnectionProvider.class, connectionProvider);
			registryBuilder.addService(
					ISequenceValueMassager.class, new ISequenceValueMassager.NoopSequenceValueMassager());
			registryBuilder.addService(
					HapiHibernateDialectSettingsService.class, myHapiHibernateDialectSettingsService);
			StandardServiceRegistry standardRegistry = registryBuilder.build();
			MetadataSources metadataSources = new MetadataSources(standardRegistry);

			for (Class<?> next : entityClasses) {
				metadataSources.addAnnotatedClass(next);
			}

			Metadata metadata = metadataSources.buildMetadata();

			/*
			 * This is not actually necessary for schema exporting, but we validate
			 * in order to ensure that various tests fail if the
			 * ConditionalIdMappingContributor leaves the model in an
			 * inconsistent state.
			 */
			((MetadataImpl) metadata).validate();

			EnumSet<TargetType> targetTypes = EnumSet.of(TargetType.SCRIPT);
			SchemaExport.Action action = SchemaExport.Action.CREATE;

			File outputFile = new File(myOutputDirectory, fileName);
			if (outputFile.exists()) {
				try {
					FileUtils.delete(outputFile);
				} catch (IOException e) {
					throw new MojoFailureException("Failed to delete file: " + e.getMessage(), e);
				}
			}

			writeContentsToFile(nextDialect.getPrependFile(), classLoader, outputFile);

			String outputFileName = outputFile.getAbsolutePath();
			ourLog.info("Writing to file: {}", outputFileName);

			SchemaExport schemaExport = new SchemaExport();
			schemaExport.setFormat(true);
			schemaExport.setDelimiter(";");
			schemaExport.setOutputFile(outputFileName);
			schemaExport.execute(targetTypes, action, metadata, standardRegistry);

			writeContentsToFile(nextDialect.getAppendFile(), classLoader, outputFile);

			if (nextDialect.getDropStatementsContainingRegex() != null
					&& !nextDialect.getDropStatementsContainingRegex().isEmpty()) {
				ourLog.info(
						"Dropping statements containing regex(s): {}", nextDialect.getDropStatementsContainingRegex());
				try {
					String fullFile;
					try (FileReader fr = new FileReader(outputFileName, StandardCharsets.UTF_8)) {
						fullFile = IOUtils.toString(fr);
					}

					int count = 0;
					List<String> statements = SqlUtil.splitSqlFileIntoStatements(fullFile);
					for (Iterator<String> statementIter = statements.iterator(); statementIter.hasNext(); ) {
						String statement = statementIter.next();
						if (nextDialect.getDropStatementsContainingRegex().stream()
								.anyMatch(regex -> Pattern.compile(regex)
										.matcher(statement)
										.find())) {
							statementIter.remove();
							count++;
						}
					}

					ourLog.info(
							"Filtered {} statement(s) from file for dialect: {}", count, nextDialect.getClassName());

					try (FileWriter fw = new FileWriter(outputFileName, StandardCharsets.UTF_8)) {
						for (String statement : statements) {
							fw.append(statement);
							fw.append(";\n\n");
						}
					}

				} catch (IOException theE) {
					throw new RuntimeException(theE);
				}
			}
		}

		IoUtil.closeQuietly(connectionProvider);
	}

	public void setProject(MavenProject theProject) {
		myProject = theProject;
	}

	@SuppressWarnings("unchecked")
	private ClassLoader getClassLoader(MavenProject project) throws MojoFailureException {
		if (project == null) {
			return DdlGeneratorHibernate61.class.getClassLoader();
		}

		try {
			List<String> classpathElements = project.getCompileClasspathElements();
			classpathElements.add(project.getBuild().getOutputDirectory());
			classpathElements.add(project.getBuild().getTestOutputDirectory());
			URL[] urls = new URL[classpathElements.size()];
			for (int i = 0; i < classpathElements.size(); ++i) {
				urls[i] = new File(classpathElements.get(i)).toURI().toURL();
			}
			return new URLClassLoader(urls, this.getClass().getClassLoader());
		} catch (Exception e) {
			throw new MojoFailureException("Failed to set classpath: " + e.getMessage(), e);
		}
	}

	@Nonnull
	private Set<Class<?>> scanClasspathForEntityClasses(Set<String> thePackages, ClassLoader theClassLoader)
			throws MojoFailureException {

		ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
		provider.setResourceLoader(new PathMatchingResourcePatternResolver(theClassLoader));
		provider.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
		provider.addIncludeFilter(new AnnotationTypeFilter(MappedSuperclass.class));

		Set<Class<?>> entityClassNames = new HashSet<>();
		for (final String nextPackage : thePackages) {

			boolean found = false;
			for (BeanDefinition definition : provider.findCandidateComponents(nextPackage)) {
				if (definition.getBeanClassName() != null) {
					ourLog.debug("Found entity class: {}", definition.getBeanClassName());
					Class<?> clazz;
					try {
						clazz = theClassLoader.loadClass(definition.getBeanClassName());
					} catch (ClassNotFoundException e) {
						throw new MojoFailureException("Failed to load class: " + definition.getBeanClassName(), e);
					}
					entityClassNames.add(clazz);
					found = true;
				}
			}

			if (!found) {
				throw new MojoFailureException("No @Entity classes found in package: " + nextPackage);
			}
		}
		return entityClassNames;
	}

	private static void writeContentsToFile(String prependFile, ClassLoader classLoader, File outputFile)
			throws MojoFailureException {
		if (isNotBlank(prependFile)) {
			ResourceLoader loader = new DefaultResourceLoader(classLoader);
			Resource resource = loader.getResource(prependFile);
			try (Writer w = new FileWriter(outputFile, true)) {
				w.append(resource.getContentAsString(StandardCharsets.UTF_8));
			} catch (IOException e) {
				throw new MojoFailureException("Failed to write to file " + outputFile + ": " + e.getMessage(), e);
			}
		}
	}

	/**
	 * The hibernate bootstrap process insists on having a DB connection even
	 * if it will never be used. So we just create a placeholder H2 connection
	 * here. The schema export doesn't actually touch this DB, so it doesn't
	 * matter that it doesn't correlate to the specified dialect.
	 */
	private static class FakeConnectionConnectionProvider extends UserSuppliedConnectionProviderImpl
			implements Closeable {
		private static final long serialVersionUID = 4147495169899817244L;
		private Connection connection;

		public FakeConnectionConnectionProvider() {
			try {
				connection = DriverManager.getConnection("jdbc:h2:mem:tmp", "sa", "sa");
			} catch (SQLException e) {
				connection = null;
				return;
			}

			/*
			 * The Oracle Dialect tries to query for any existing sequences, so we need to supply
			 * a fake empty table to answer that query.
			 */
			try {
				connection.setAutoCommit(true);
				connection
						.prepareStatement("create table all_sequences (PID bigint not null, primary key (PID))")
						.execute();
			} catch (SQLException e) {
				ourLog.error("Failed to create sequences table", e);
			}
		}

		@Override
		public Connection getConnection() {
			ourLog.trace("Using internal driver: {}", org.h2.Driver.class);
			return connection;
		}

		@Override
		public void closeConnection(Connection conn) {
			// ignore
		}

		@Override
		public void close() throws IOException {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new IOException(e);
			}
		}
	}
}
