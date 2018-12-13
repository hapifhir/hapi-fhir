package ca.uhn.fhir.tinder;

import ca.uhn.fhir.tinder.parser.ResourceGeneratorUsingModel;
import ca.uhn.fhir.tinder.parser.ResourceGeneratorUsingSpreadsheet;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.EscapeTool;

import java.io.*;

@Mojo(name = "generate-resource", defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class TinderResourceGeneratorMojo extends AbstractGeneratorMojo {

	@Parameter(required = true, defaultValue = "${project.build.directory}/generated-resources/tinder")
	protected File targetDirectory;

	@Parameter(required = true)
	protected String fileName = "";

	@Override
	protected void doExecute(Configuration configuration) throws MojoExecutionException, MojoFailureException {
		File packageDirectoryBase = configuration.getPackageDirectoryBase();
		packageDirectoryBase.mkdirs();

		ResourceGeneratorUsingModel gen = new ResourceGeneratorUsingModel(configuration.getVersion(), configuration.getBaseDir());
		gen.setBaseResourceNames(configuration.getResourceNames());

		try {
			gen.parse();

			VelocityContext ctx = new VelocityContext();
			ctx.put("resources", gen.getResources());
			ctx.put("packageBase", configuration.getPackageBase());
			ctx.put("version", configuration.getVersion());
			ctx.put("package_suffix", configuration.getPackageSuffix());
			ctx.put("esc", new EscapeTool());

			ctx.put("resourcePackage", configuration.getResourcePackage());
			ctx.put("versionCapitalized", configuration.getVersionCapitalized());

			VelocityEngine v = new VelocityEngine();
			v.setProperty("resource.loader", "cp");
			v.setProperty("cp.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			v.setProperty("runtime.references.strict", Boolean.TRUE);

			InputStream templateIs = ResourceGeneratorUsingSpreadsheet.class.getResourceAsStream(templateName);
			InputStreamReader templateReader = new InputStreamReader(templateIs);

			File file = new File(packageDirectoryBase, fileName);
			OutputStreamWriter w = new OutputStreamWriter(new FileOutputStream(file, false), "UTF-8");
			v.evaluate(ctx, w, "", templateReader);
			w.close();

			Resource resource = new Resource();
			resource.setDirectory(packageDirectoryBase.getAbsolutePath());
			//resource.setDirectory(targetDirectory.getAbsolutePath());
			//resource.addInclude(packageBase);
			myProject.addResource(resource);

		} catch (Exception e) {
			throw new MojoFailureException("Failed to generate resources", e);
		}
	}

	@Override
	public File getTargetDirectory() {
		return targetDirectory;
	}
}
