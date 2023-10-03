package ca.uhn.fhir.tinder.ddl;

import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import jakarta.persistence.Entity;
import jakarta.persistence.MappedSuperclass;
import org.apache.commons.lang3.Validate;
import org.apache.maven.plugin.MojoFailureException;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.jdbc.connections.internal.UserSuppliedConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import javax.annotation.Nonnull;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DdlGeneratorHibernate61 {
    private static final Logger ourLog = LoggerFactory.getLogger(DdlGeneratorHibernate61.class);
    private final Set<String> myPackages = new HashSet<>();
    private final List<DialectDescriptor> myDialects = new ArrayList<>();
    private File myOutputDirectory;

    public void addPackage(String thePackage) {
        Validate.notNull(thePackage);
        myPackages.add(thePackage);
    }

    public void addDialect(String theDialectClassName, String theFileName) {
        Validate.notBlank(theDialectClassName);
        Validate.notBlank(theFileName);
        myDialects.add(new DialectDescriptor(theDialectClassName, theFileName));
    }

    public void setOutputDirectory(File theOutputDirectory) {
        Validate.notNull(theOutputDirectory);
        myOutputDirectory = theOutputDirectory;
    }

    public void generateDdl() throws MojoFailureException {
        for (DialectDescriptor nextDialect : myDialects) {
            String fileName = nextDialect.getFileName();
            String dialectClassName = nextDialect.getDialectClassName();

            Set<String> entityClassNames = scanClasspathForEntityClasses(myPackages);

            /*
             * The hibernate bootstrap process insists on having a DB connection even
             * if it will never be used. So we just create a placeholder H2 connection
             * here. The schema export doesn't actually touch this DB, so it doesn't
             * matter that it doesn't correlate to the specified dialect.
             */
            ConnectionProvider connectionProvider = new UserSuppliedConnectionProviderImpl() {
                private static final long serialVersionUID = 4147495169899817244L;

                @Override
                public Connection getConnection() throws SQLException {
                    ourLog.trace("Using internal driver: {}", org.h2.Driver.class);
                    return DriverManager.getConnection("jdbc:h2:mem:tmp", "sa", "sa");
                }

                @Override
                public void closeConnection(Connection conn) throws SQLException {
                    conn.close();
                }
            };

            StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();
            registryBuilder.applySetting(AvailableSettings.HBM2DDL_AUTO, "create");
            registryBuilder.applySetting(AvailableSettings.DIALECT, dialectClassName);
            registryBuilder.addService(ConnectionProvider.class, connectionProvider);
            StandardServiceRegistry standardRegistry = registryBuilder.build();
            MetadataSources metadataSources = new MetadataSources(standardRegistry);

            for (String next : entityClassNames) {
                try {
                    metadataSources.addAnnotatedClass(Class.forName(next));
                } catch (ClassNotFoundException e) {
                    throw new MojoFailureException("Can not find class " + next, e);
                }
            }

            Metadata metadata = metadataSources.buildMetadata();

            EnumSet<TargetType> targetTypes = EnumSet.of(TargetType.SCRIPT);
            SchemaExport.Action action = SchemaExport.Action.CREATE;

            String outputFile = new File(myOutputDirectory, fileName).getAbsolutePath();
            ourLog.info("Writing to file: {}", outputFile);

            SchemaExport schemaExport = new SchemaExport();
            schemaExport.setFormat(true);
            schemaExport.setDelimiter(";");
            schemaExport.setOutputFile(outputFile);
            schemaExport.setOverrideOutputFileContent();
            schemaExport.execute(targetTypes, action, metadata, standardRegistry);
        }
    }

    @Nonnull
    private static Set<String> scanClasspathForEntityClasses(Set<String> thePackages) throws MojoFailureException {
        Set<String> entityClassNames = new HashSet<>();
        for (final String nextPackage : thePackages) {
            ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
            provider.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
            provider.addIncludeFilter(new AnnotationTypeFilter(MappedSuperclass.class));

            boolean found = false;
            for (BeanDefinition definition : provider.findCandidateComponents(nextPackage)) {
                if (definition.getBeanClassName() != null) {
                    ourLog.debug("Found entity class: {}", definition.getBeanClassName());
                    entityClassNames.add(definition.getBeanClassName());
                    found = true;
                }
            }

            if (!found) {
                throw new MojoFailureException("No @Entity classes found in package: " + nextPackage);
            }
        }
        return entityClassNames;
    }

    private static class DialectDescriptor {
        private final String myDialectClassName;
        private final String myFileName;

        public DialectDescriptor(String theDialectClassName, String theFileName) {
            myDialectClassName = theDialectClassName;
            myFileName = theFileName;
        }

        public String getDialectClassName() {
            return myDialectClassName;
        }

        public String getFileName() {
            return myFileName;
        }
    }
}
