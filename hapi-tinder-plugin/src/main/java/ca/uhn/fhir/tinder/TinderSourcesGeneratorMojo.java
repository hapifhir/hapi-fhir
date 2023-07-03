package ca.uhn.fhir.tinder;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.tinder.parser.ResourceGeneratorUsingModel;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

@Mojo(name = "generate-sources", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class TinderSourcesGeneratorMojo extends AbstractGeneratorMojo {

	@Parameter(required = true, defaultValue = "${project.build.directory}/generated-sources/tinder")
	protected File targetDirectory;

	@Parameter
	private String filenameSuffix = "ResourceProvider";

	@Parameter
	private String filenamePrefix = "";

	@Override
	public void doExecute(Configuration configuration) throws MojoExecutionException, MojoFailureException {
		File packageDirectoryBase = configuration.getPackageDirectoryBase();
		packageDirectoryBase.mkdirs();

		ResourceGeneratorUsingModel gen = new ResourceGeneratorUsingModel(configuration.getVersion(), configuration.getBaseDir());
		gen.setBaseResourceNames(configuration.getResourceNames());

		try {
			gen.parse();

			gen.setFilenameSuffix(filenameSuffix);
			gen.setFilenamePrefix(filenamePrefix);
			gen.setTemplate(templateName);
			gen.writeAll(packageDirectoryBase, null, configuration.getPackageBase());
		} catch (Exception e) {
			throw new MojoFailureException(Msg.code(106) + "Failed to generate server", e);
		}

		myProject.addCompileSourceRoot(configuration.getTargetDirectory().getAbsolutePath());
	}

	@Override
	protected File getTargetDirectory() {
		return targetDirectory;
	}
}
