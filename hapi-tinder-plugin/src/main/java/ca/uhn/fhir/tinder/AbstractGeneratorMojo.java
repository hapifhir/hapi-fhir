package ca.uhn.fhir.tinder;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.List;

/**
 * Base class for mojo generatorss.
 */
public abstract class AbstractGeneratorMojo extends AbstractMojo {

	protected final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(getClass());

	@Parameter(required = true, defaultValue = "${project.build.directory}/..")
	protected String baseDir;

	@Parameter
	protected String packageBase = "";

	@Parameter
	protected List<String> baseResourceNames;

	@Parameter
	protected List<String> excludeResourceNames;

	@Parameter
	protected String templateName;

	@Parameter(required = true)
	protected String version;

	@Component
	protected MavenProject myProject;

	@Override
	public final void execute() throws MojoExecutionException, MojoFailureException {
		doExecute(new Configuration(this.version, baseDir, getTargetDirectory(), this.packageBase, this.baseResourceNames, this.excludeResourceNames));
	}

	protected abstract void doExecute(Configuration mavenGeneratorConfiguration) throws MojoExecutionException, MojoFailureException;

	protected abstract File getTargetDirectory();

}
