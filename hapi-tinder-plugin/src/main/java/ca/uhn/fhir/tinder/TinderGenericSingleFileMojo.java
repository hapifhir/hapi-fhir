package ca.uhn.fhir.tinder;

import ca.uhn.fhir.i18n.Msg;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.lang.WordUtils;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import ca.uhn.fhir.tinder.AbstractGenerator.ExecutionException;
import ca.uhn.fhir.tinder.AbstractGenerator.FailureException;
import ca.uhn.fhir.tinder.GeneratorContext.ResourceSource;
import ca.uhn.fhir.tinder.TinderStructuresMojo.ValueSetFileDefinition;
import ca.uhn.fhir.tinder.parser.BaseStructureParser;
import ca.uhn.fhir.tinder.parser.BaseStructureSpreadsheetParser;
import ca.uhn.fhir.tinder.parser.DatatypeGeneratorUsingSpreadsheet;
import ca.uhn.fhir.tinder.parser.TargetType;

/**
 * Generate a single file based on resource or composite type metadata.
 * <p>
 * Generates either a source or resource file containing all selected resources or
 * composite data types. The file is
 * generated using a Velocity template that can be taken from
 * inside the hapi-timder-plugin project or can be located in other projects
 * <p>
 * The following Maven plug-in configuration properties are used with this plug-in 
 * <p>
 * <table border="1" cellpadding="2" cellspacing="0">
 *   <tr>
 *     <td valign="top"><b>Attribute</b></td>
 *     <td valign="top"><b>Description</b></td>
 *     <td align="center" valign="top"><b>Required</b></td>
 *   </tr>
 *   <tr>
 *     <td valign="top">version</td>
 *     <td valign="top">The FHIR version whose resource metadata
 *     is to be used to generate the files<br>
 *     Valid values:&nbsp;<code><b>dstu2</b></code>&nbsp;|&nbsp;<code><b>dstu3</b></code>&nbsp;|&nbsp;<code><b>r4</b></code></td>
 *     <td valign="top" align="center">Yes</td>
 *   </tr>
 *   <tr>
 *     <td valign="top">baseDir</td>
 *     <td valign="top">The Maven project's base directory. This is used to 
 *     possibly locate other assets within the project used in file generation.</td>
 *     <td valign="top" align="center">No. Defaults to: <code>${project.build.directory}/..</code></td>
 *   </tr>
 *   <tr>
 *     <td valign="top">generateResources</td>
 *     <td valign="top">Should files be generated from FHIR resource metadata?<br>
 *     Valid values:&nbsp;<code><b>true</b></code>&nbsp;|&nbsp;<code><b>false</b></code></td> 
 *     <td valign="top" align="center" rowspan="2">One of these two options must be specified as <code><b>true</b></code></td>
 *   </tr>
 *   <tr>
 *     <td valign="top">generateDataTypes</td>
 *     <td valign="top">Should files be generated from FHIR composite data type metadata?<br>
 *     Valid values:&nbsp;<code><b>true</b></code>&nbsp;|&nbsp;<code><b>false</b></code></td> 
 *   </tr>
 *   <tr>
 *     <td valign="top">resourceSource</td>
 *     <td valign="top">Which source of resource definitions should be processed? Valid values are:<br>
 *     <ul>
 *     <li><code><b>spreadsheet</b></code>&nbsp;&nbsp;to cause resources to be generated based on the FHIR spreadsheets</li>
 *     <li><code><b>model</b></code>&nbsp;&nbsp;to cause resources to be generated based on the model structure classes. Note that 
 *     <code>generateResources</code> is the only one of the above options that can be used when <code>model</code> is specified.</li></ul></td> 
 *     <td valign="top" align="center">No. Defaults to: <code><b>spreadsheet</b></code></td>
 *   </tr>
 *   <tr>
 *     <td colspan="3" />
 *   </tr>
 *   <tr>
 *     <td valign="top" colspan="3">Java source files can be generated
 *     for FHIR resources or composite data types. There is one file
 *     generated for each selected entity. The following configuration
 *     properties control the naming of the generated source files:<br>
 *     &nbsp;&nbsp;&nbsp;&nbsp;&lt;targetSourceDirectory&gt;/&lt;targetPackage&gt;/&lt;targetFile&gt;<br>
 *     Note that all dots in the targetPackage will be replaced by the path separator character when building the
 *     actual source file location. Also note that <code>.java</code> will be added to the targetFile if it is not already included.
 *     </td>
 *   </tr>
 *   <tr>
 *     <td valign="top">targetSourceDirectory</td>
 *     <td valign="top">The Maven source directory to contain the generated file.</td>
 *     <td valign="top" align="center">Yes when a Java source file is to be generated</td>
 *   </tr>
 *   <tr>
 *     <td valign="top">targetPackage</td>
 *     <td valign="top">The Java package that will contain the generated classes.
 *     This package is generated in the &lt;targetSourceDirectory&gt; if needed.</td>
 *     <td valign="top" align="center">Yes when <i>targetSourceDirectory</i> is specified</td>
 *   </tr>
 *   <tr>
 *     <td valign="top">packageBase</td>
 *     <td valign="top">The base Java package for related classes. This property
 *     can be used to reference class in other places in a folder structure.</td>
 *     <td valign="top" align="center">No</td>
 *   </tr>
 *   <tr>
 *     <td valign="top">targetFile</td>
 *     <td valign="top">The name of the file to be generated</td>
 *     <td valign="top" align="center">Yes</td>
 *   </tr>
 *   <tr>
 *     <td colspan="3" />
 *   </tr>
 *   <tr>
 *     <td valign="top" colspan="3">Maven resource files can also be generated
 *     for FHIR resources or composite data types. The following configuration
 *     properties control the naming of the generated resource files:<br>
 *     &nbsp;&nbsp;&nbsp;&nbsp;&lt;targetResourceDirectory&gt;/&lt;targetFolder&gt;/&lt;targetFile&gt;<br>
 *     </td>
 *   </tr>
 *   <tr>
 *     <td valign="top">targetResourceDirectory</td>
 *     <td valign="top">The Maven resource directory to contain the generated file.</td>
 *     <td valign="top" align="center">Yes when a resource file is to be generated</td>
 *   </tr>
 *   <tr>
 *     <td valign="top">targetFolder</td>
 *     <td valign="top">The folder within the targetResourceDirectory where the generated file will be placed.
 *     This folder is generated in the &lt;targetResourceDirectory&gt; if needed.</td>
 *     <td valign="top" align="center">No</td>
 *   </tr>
 *   <tr>
 *     <td colspan="3" />
 *   </tr>
 *   <tr>
 *     <td valign="top">template</td>
 *     <td valign="top">The path of one of the <i>Velocity</i> templates
 *     contained within the <code>hapi-tinder-plugin</code> Maven plug-in
 *     classpath that will be used to generate the files.</td>
 *     <td valign="top" align="center" rowspan="2">One of these two options must be configured</td>
 *   </tr>
 *   <tr>
 *     <td valign="top">templateFile</td>
 *     <td valign="top">The full path to the <i>Velocity</i> template that is
 *     to be used to generate the files.</td> 
 *   </tr>
 *   <tr>
 *     <td valign="top">velocityPath</td>
 *     <td valign="top">When using the <code>templateFile</code> option, this property
 *     can be used to specify where Velocity macros and other resources are located.</td>
 *     <td valign="top" align="center">No. Defaults to same directory as the template file.</td>
 *   </tr>
 *   <tr>
 *     <td valign="top">velocityProperties</td>
 *     <td valign="top">Specifies the full path to a java properties file
 *     containing Velocity configuration properties</td>
 *     <td valign="top" align="center">No.</td>
 *   </tr>
 *   <tr>
 *     <td valign="top">includeResources</td>
 *     <td valign="top">A list of the names of the resources or composite data types that should
 *     be used in the file generation</td>
 *     <td valign="top" align="center">No. Defaults to all defined resources except for DSTU2, 
 *     the <code>Binary</code> resource is excluded and
 *     for DSTU3, the <code>Conformance</code> resource is excluded.</td>
 *   </tr>
 *   <tr>
 *     <td valign="top">excludeResources</td>
 *     <td valign="top">A list of the names of the resources or composite data types that should
 *     excluded from the file generation</td>
 *     <td valign="top" align="center">No.</td>
 *   </tr>
 *   <tr>
 *     <td valign="top">valueSetFiles</td>
 *     <td valign="top">A list of files containing value-set resource definitions
 *     to be used.</td>
 *     <td valign="top" align="center">No. Defaults to all defined value-sets that 
 *     are referenced from the selected resources.</td>
 *   </tr>
 *   <tr>
 *     <td valign="top">profileFiles</td>
 *     <td valign="top">A list of files containing profile definitions
 *     to be used.</td>
 *     <td valign="top" align="center">No. Defaults to the default profile
 *     for each selected resource</td>
 *   </tr>
 * </table>
 * 
 * 
 * 
 * @author Bill.Denton
 *
 */
@Mojo(name = "generate-single-file", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class TinderGenericSingleFileMojo extends AbstractMojo {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TinderGenericSingleFileMojo.class);

	@Parameter(required = true)
	private String version;

	@Parameter(required = true, defaultValue = "${project.build.directory}/..")
	private String baseDir;

	@Parameter(required = false, defaultValue="false")
	private boolean generateResources;

	@Parameter(required = false, defaultValue = "false")
	private boolean generateDatatypes;
	
	@Parameter(required = false)
	private File targetSourceDirectory;

	@Parameter(required = false)
	private String targetPackage;

	@Parameter(required = false)
	private String packageBase;
	
	@Parameter(required = false)
	private File targetResourceDirectory;

	@Parameter(required = false)
	private String targetFolder;

	@Parameter(required = false)
	private String targetFile;
	
	// one of these two is required
	@Parameter(required = false)
	private String template;
	@Parameter(required = false)
	private File templateFile;
	@Parameter(required = false)
	private String velocityPath;
	@Parameter(required = false)
	private String velocityProperties;

	@Parameter(required = false)
	private List<String> includeResources;

	@Parameter(required = false)
	private List<String> excludeResources;
	
	@Parameter(required = false)
	private String resourceSource;

	@Parameter(required = false)
	private List<ValueSetFileDefinition> valueSetFiles;

	@Component
	private MavenProject myProject;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		
		GeneratorContext context = new GeneratorContext();
		Generator generator = new Generator();
		try {
			context.setVersion(version);
			context.setBaseDir(baseDir);
			context.setIncludeResources(includeResources);
			context.setExcludeResources(excludeResources);
			context.setResourceSource(resourceSource);
			context.setValueSetFiles(valueSetFiles);
			if (ResourceSource.MODEL.equals(context.getResourceSource())) {
				if (generateDatatypes) {
					throw new MojoFailureException(Msg.code(120) + "Cannot use \"generateDatatypes\" when resourceSource=model");
				}
			}

			generator.prepare(context);
		} catch (FailureException e) {
			throw new MojoFailureException(Msg.code(121) + e.getMessage(), e.getCause());
		}
		
		try {
			/*
			 * Deal with the generation target
			 */
			TargetType targetType;
			File targetDirectory = null;
			if (null == targetFile) {
				throw new MojoFailureException(Msg.code(122) + "The [targetFile] parameter is required.");
			}
			if (targetSourceDirectory != null) {
				if (targetResourceDirectory != null) {
					throw new MojoFailureException(Msg.code(123) + "Both [targetSourceDirectory] and [targetResourceDirectory] are specified. Please choose just one.");
				}
				targetType = TargetType.SOURCE;
				if (null == targetPackage) {
					throw new MojoFailureException(Msg.code(124) + "The [targetPackage] property must be specified when generating Java source code.");
				}
				targetDirectory = new File(targetSourceDirectory, targetPackage.replace('.', File.separatorChar));
				if (!targetFile.endsWith(".java")) {
					targetFile += ".java";
				}
			} else
			if (targetResourceDirectory != null) {
				if (targetSourceDirectory != null) {
					throw new MojoFailureException(Msg.code(125) + "Both [targetSourceDirectory] and [targetResourceDirectory] are specified. Please choose just one.");
				}
				targetType = TargetType.RESOURCE;
				if (targetFolder != null) {
					targetFolder = targetFolder.replace('\\', '/');
					targetFolder = targetFolder.replace('/', File.separatorChar);
					targetDirectory = new File(targetResourceDirectory, targetFolder);
				} else {
					targetDirectory = targetResourceDirectory;
				}
				if (null == targetPackage) {
					targetPackage = "";
				}
			} else {
				throw new MojoFailureException(Msg.code(126) + "Either [targetSourceDirectory] or [targetResourceDirectory] must be specified.");
			}
			ourLog.info(" * Output ["+targetType.toString()+"] file ["+targetFile+"] in directory: " + targetDirectory.getAbsolutePath());
			targetDirectory.mkdirs();
			File target = new File(targetDirectory, targetFile);
			OutputStreamWriter targetWriter = new OutputStreamWriter(new FileOutputStream(target, false), StandardCharsets.UTF_8);
	
			/*
			 * Next, deal with the template and initialize velocity
			 */
			VelocityEngine v = VelocityHelper.configureVelocityEngine(templateFile, velocityPath, velocityProperties);
			InputStream templateIs;
			if (templateFile != null) {
				templateIs = new FileInputStream(templateFile);
			} else {
				templateIs = this.getClass().getResourceAsStream(template);
			}
			InputStreamReader templateReader = new InputStreamReader(templateIs);
	
			/*
			 * build new Velocity Context
			 */
			VelocityContext ctx = new VelocityContext();
			if (packageBase != null) {
				ctx.put("packageBase", packageBase);
			} else
			if (targetPackage != null) {
				int ix = targetPackage.lastIndexOf('.');
				if (ix > 0) {
					ctx.put("packageBase", targetPackage.subSequence(0, ix));
				} else {
					ctx.put("packageBase", targetPackage);
				}
			}
			ctx.put("targetPackage", targetPackage);
			ctx.put("targetFolder", targetFolder);
			ctx.put("version", version);
			ctx.put("isRi", BaseStructureParser.determineVersionEnum(version).isRi());
			ctx.put("hash", "#");
			ctx.put("esc", new TinderResourceGeneratorMojo.EscapeTool());
			if (BaseStructureParser.determineVersionEnum(version).isRi()) {
				ctx.put("resourcePackage", "org.hl7.fhir." + version + ".model");
			} else {
				ctx.put("resourcePackage", "ca.uhn.fhir.model." + version + ".resource");
			}
			
			String capitalize = WordUtils.capitalize(version);
			if ("Dstu".equals(capitalize)) {
				capitalize="Dstu1";
			}
			ctx.put("versionCapitalized", capitalize);
			
			/*
			 * Write resources if selected
			 */
			BaseStructureParser rp = context.getResourceGenerator();
			if (generateResources && rp != null) {
				ourLog.info("Writing Resources...");
				ctx.put("resources", rp.getResources());
				v.evaluate(ctx, targetWriter, "", templateReader);
				targetWriter.close();
			} else {
				DatatypeGeneratorUsingSpreadsheet dtp = context.getDatatypeGenerator();
				if (generateDatatypes && dtp != null) {
					ourLog.info("Writing DataTypes...");
					ctx.put("datatypes", dtp.getResources());
					v.evaluate(ctx, targetWriter, "", templateReader);
					targetWriter.close();
				}
			}
			
			switch (targetType) {
				case SOURCE: {
					myProject.addCompileSourceRoot(targetSourceDirectory.getAbsolutePath());
					break;
				}
				case RESOURCE: {
					Resource resource = new Resource();
					resource.setDirectory(targetResourceDirectory.getAbsolutePath());
					String resName = targetFile;
					if (targetFolder != null) {
						resName = targetFolder+File.separator+targetFile;
					}
					resource.addInclude(resName);
					myProject.addResource(resource);
					break;
				}
				default:
			}

		} catch (Exception e) {
			throw new MojoFailureException(Msg.code(127) + "Failed to generate file", e);
		}
	}

	public static void main(String[] args) throws IOException, MojoFailureException, MojoExecutionException {

		// PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		// HttpClientBuilder builder = HttpClientBuilder.create();
		// builder.setConnectionManager(connectionManager);
		// CloseableHttpClient client = builder.build();
		//
		// HttpGet get = new HttpGet("http://fhir.healthintersections.com.au/open/metadata");
		// CloseableHttpResponse response = client.execute(get);
		//
		// String metadataString = EntityUtils.toString(response.getEntity());
		//
		// ourLog.info("Metadata String: {}", metadataString);

		// String metadataString = IOUtils.toString(new FileInputStream("src/test/resources/healthintersections-metadata.xml"));
		// Conformance conformance = new FhirContext(Conformance.class).newXmlParser().parseResource(Conformance.class, metadataString);

		TinderGenericSingleFileMojo mojo = new TinderGenericSingleFileMojo();
		mojo.myProject = new MavenProject();
		mojo.template = "/vm/jpa_spring_beans.vm";
		mojo.version = "dstu2";
		mojo.targetPackage = "ca.uhn.test";
		mojo.targetSourceDirectory = new File("target/generated/valuesets");
		mojo.targetFile = "tmp_beans.xml";
		mojo.execute();
	}

	class Generator extends AbstractGenerator {
		@Override
		protected void logDebug(String message) {
			ourLog.debug(message);
		}

		@Override
		protected void logInfo(String message) {
			ourLog.info(message);
		}
	}
}
