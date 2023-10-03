package ca.uhn.fhir.tinder.ddl;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

@Mojo(
        name = "generate-ddl",
        defaultPhase = LifecyclePhase.PREPARE_PACKAGE,
        requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME,
        threadSafe = true)
public class GenerateDdlMojo extends AbstractMojo {
    private static final Logger ourLog = LoggerFactory.getLogger(GenerateDdlMojo.class);

    @Parameter
    private List<String> packageNames;
    @Parameter
    private List<String> dialects;
    @Parameter
    private String outputDirectory;

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

        for (String nextDialect : dialects) {
            String[] nextDialectSplit = nextDialect.trim().split(" +");
            String dialectClass = nextDialectSplit[0];
            String fileName = nextDialectSplit[1];
            generator.addDialect(dialectClass, fileName);
        }

        generator.setOutputDirectory(outputDirectoryFile);

        ourLog.info("Beginning DDL export");
        generator.generateDdl();
    }


    public static void main(String[] args) throws MojoExecutionException, MojoFailureException {
        GenerateDdlMojo m = new GenerateDdlMojo();
        m.packageNames = List.of("ca.uhn.fhir.jpa.model.entity");
        m.outputDirectory = "target";
        m.dialects = List.of("ca.uhn.fhir.jpa.model.dialect.HapiFhirPostgres94Dialect   hapifhirpostgres94.sql");
        m.execute();
    }


}
