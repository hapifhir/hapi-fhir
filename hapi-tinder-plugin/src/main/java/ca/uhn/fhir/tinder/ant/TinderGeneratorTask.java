package ca.uhn.fhir.tinder.ant;
/*
 * #%L
 * HAPI Tinder Plugin
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.i18n.Msg;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import ca.uhn.fhir.tinder.TinderResourceGeneratorMojo;
import org.apache.commons.lang.WordUtils;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.tinder.AbstractGenerator;
import ca.uhn.fhir.tinder.AbstractGenerator.ExecutionException;
import ca.uhn.fhir.tinder.AbstractGenerator.FailureException;
import ca.uhn.fhir.tinder.GeneratorContext;
import ca.uhn.fhir.tinder.GeneratorContext.ResourceSource;
import ca.uhn.fhir.tinder.TinderStructuresMojo.ValueSetFileDefinition;
import ca.uhn.fhir.tinder.ValueSetGenerator;
import ca.uhn.fhir.tinder.VelocityHelper;
import ca.uhn.fhir.tinder.parser.BaseStructureParser;
import ca.uhn.fhir.tinder.parser.BaseStructureSpreadsheetParser;
import ca.uhn.fhir.tinder.parser.DatatypeGeneratorUsingSpreadsheet;
import ca.uhn.fhir.tinder.parser.TargetType;

/**
/**
 * Generate files from FHIR resource/composite metadata using Velocity templates.
 * <p>
 * Generates either source or resource files for each selected resource or
 * composite data type. One file is generated for each selected entity. The
 * files are generated using a Velocity template that can be taken from
 * inside the hapi-timder-plugin project or can be located in other projects
 * <p>
 * The following Ant task properties are used 
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
 *     <td valign="top">projectHome</td>
 *     <td valign="top">The project's base directory. This is used to 
 *     possibly locate other assets within the project used in file generation.</td>
 *     <td valign="top" align="center">No. Defaults to: <code>${basedir}/..</code></td>
 *   </tr>
 *   <tr>
 *     <td valign="top">generateResources</td>
 *     <td valign="top">Should files be generated from FHIR resource metadata?<br>
 *     Valid values:&nbsp;<code><b>true</b></code>&nbsp;|&nbsp;<code><b>false</b></code></td> 
 *     <td valign="top" align="center" rowspan="4">At least one of these four options must be specified as <code><b>true</b></code></td>
 *   </tr>
 *   <tr>
 *     <td valign="top">generateDataTypes</td>
 *     <td valign="top">Should files be generated from FHIR composite data type metadata?<br>
 *     Valid values:&nbsp;<code><b>true</b></code>&nbsp;|&nbsp;<code><b>false</b></code></td> 
 *   </tr>
 *   <tr>
 *     <td valign="top">generateValueSets</td>
 *     <td valign="top">Should files be generated from FHIR value set metadata?<br>
 *     This option can only be used if generating multiple files (one file per value-set.)<br>
 *     Valid values:&nbsp;<code><b>true</b></code>&nbsp;|&nbsp;<code><b>false</b></code></td> 
 *   </tr>
 *   <tr>
 *     <td valign="top">generateProfiles</td>
 *     <td valign="top">Should files be generated from FHIR profile metadata?<br>
 *     This option can only be used if generating multiple files (one file per profile.)<br>
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
 *     for FHIR resources or composite data types. Source files can be  
 *     generated for each selected entity or a single source file can
 *     be generated containing all of the selected entity. The following configuration
 *     properties control the naming of the generated source files:
 *     <p>The following properties will be used when generating multiple source files:<br>
 *     &nbsp;&nbsp;&nbsp;&nbsp;&lt;targetSourceDirectory&gt;/&lt;targetPackage&gt;/&lt;filenamePrefix&gt;<i>element-name</i>&lt;filenameSuffix&gt;<br>
 * 	   &nbsp;&nbsp;&nbsp;&nbsp;where: <i>element-name</i> is the "title-case" name of the selected resource or composite data type.
 *     <p>The following properties will be used when generating a single source file:<br>
 *     &nbsp;&nbsp;&nbsp;&nbsp;&lt;targetSourceDirectory&gt;/&lt;targetPackage&gt;/&lt;targetFile&gt;
 *     <p>
 *     Note that all dots in the targetPackage will be replaced by the path separator character when building the
 *     actual source file location. Also note that <code>.java</code> will be added to the filenameSuffix or targetFile if it is not already included.
 *     </td>
 *   </tr>
 *   <tr>
 *     <td valign="top">targetSourceDirectory</td>
 *     <td valign="top">The source directory to contain the generated Java packages and classes.</td>
 *     <td valign="top" align="center">Yes when Java source files are to be generated</td>
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
 *     <td valign="top" align="center">Yes when generating a single file containing all selected elements</td>
 *   </tr>
 *   <tr>
 *     <td valign="top">filenamePrefix</td>
 *     <td valign="top">The prefix string that is to be added onto the
 *     beginning of the resource or composite data type name to become 
 *     the Java class name or resource file name.</td>
 *     <td valign="top" align="center">No</td>
 *   </tr>
 *   <tr>
 *     <td valign="top">filenameSuffix</td>
 *     <td valign="top">Suffix that will be added onto the end of the resource 
 *     or composite data type name to become the Java class name or resource file name.</td>
 *     <td valign="top" align="center">No.</code></td>
 *   </tr>
 *   <tr>
 *     <td colspan="3" />
 *   </tr>
 *   <tr>
 *     <td valign="top" colspan="3">Resource (non-Java) files can also be generated
 *     for FHIR resources or composite data types. a file can be  
 *     generated for each selected entity or a single file can
 *     be generated containing all of the selected entity. The following configuration
 *     properties control the naming of the generated files:
 *     <p>The following properties will be used when generating multiple files (one for each selected element):<br>
 *     &nbsp;&nbsp;&nbsp;&nbsp;&lt;targetResourceDirectory&gt;/&lt;targetFolder&gt;/&lt;filenamePrefix&gt;<i>element-name</i>&lt;filenameSuffix&gt;<br>
 * 	   &nbsp;&nbsp;&nbsp;&nbsp;where: <i>element-name</i> is the "title-case" name of the selected resource or composite data type.
 *     <p>The following properties will be used when generating a single file containing all selected elements:<br>
 *     &nbsp;&nbsp;&nbsp;&nbsp;&lt;targetResourceDirectory&gt;/&lt;targetFolder&gt;/&lt;targetFile&gt;
 *     </td>
 *   </tr>
 *   <tr>
 *     <td valign="top">targetResourceDirectory</td>
 *     <td valign="top">The resource directory to contain the generated files.</td>
 *     <td valign="top" align="center">Yes when resource files are to be generated</td>
 *   </tr>
 *   <tr>
 *     <td valign="top">targetFolder</td>
 *     <td valign="top">The folder within the targetResourceDirectory where the generated files will be placed.
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
 * @author Bill Denton
 * 
 */
public class TinderGeneratorTask extends Task {

	private String version;

	private String projectHome;

	private boolean generateResources;

	private boolean generateDatatypes;

	private boolean generateValueSets;

	private boolean generateProfiles;
	
	private File targetSourceDirectory;

	private String targetPackage;

	private String packageBase;

	private String targetFile;

	private String filenamePrefix;
	
	private String filenameSuffix;
	
	private File targetResourceDirectory;

	private String targetFolder;
	
	// one of these two is required
	private String template;
	private File templateFile;

	private String velocityPath;

	private String velocityProperties;

	private List<String> includeResources;

	private List<String> excludeResources;

	private String resourceSource;

	private List<ValueSetFileDefinition> valueSetFiles;

	private boolean verbose;

	private FhirContext fhirContext; // set from version in validateAttributes
    
	/**
	 * 
	 */
	public TinderGeneratorTask () {
		super();
	}
	
	protected void cleanup () {
	}

	@Override
	public void execute () throws BuildException {
		validateAttributes();


		GeneratorContext context = new GeneratorContext();
		Generator generator = new Generator();
		try {
			context.setVersion(version);
			context.setBaseDir(projectHome);
			context.setIncludeResources(includeResources);
			context.setExcludeResources(excludeResources);
			context.setResourceSource(resourceSource);
			context.setValueSetFiles(valueSetFiles);
			if (ResourceSource.MODEL.equals(context.getResourceSource())) {
				if (generateDatatypes) {
					throw new BuildException(Msg.code(135) + "Cannot use \"generateDatatypes\" when resourceSource=model");
				}
				if (generateValueSets) {
					throw new BuildException(Msg.code(136) + "Cannot use \"generateValueSets\" when resourceSource=model");
				}
			}

			generator.prepare(context);
		} catch (MojoFailureException | FailureException e) {
			throw new BuildException(Msg.code(137) + e.getMessage(), e.getCause());
		}

		/*
		 * Deal with the generation target
		 */
		TargetType targetType = null;
		File targetDirectory = null;
		if (targetSourceDirectory != null) {
			if (targetResourceDirectory != null) {
				throw new BuildException(Msg.code(138) + "Both [targetSourceDirectory] and [targetResourceDirectory] are specified. Please choose just one.");
			}
			targetType = TargetType.SOURCE;
			if (null == targetPackage) {
				throw new BuildException(Msg.code(139) + "The [targetPackage] property must be specified when generating Java source code.");
			}
			targetDirectory = new File(targetSourceDirectory, targetPackage.replace('.', File.separatorChar));
		} else
		if (targetResourceDirectory != null) {
			if (targetSourceDirectory != null) {
				throw new BuildException(Msg.code(140) + "Both [targetSourceDirectory] and [targetResourceDirectory] are specified. Please choose just one.");
			}
			targetType = TargetType.RESOURCE;
			if (targetFolder != null) {
				targetDirectory = new File(targetResourceDirectory, targetFolder);
			} else {
				targetDirectory = targetResourceDirectory;
			}
			if (null == targetPackage) {
				targetPackage = "";
			}
		} else {
			throw new BuildException(Msg.code(141) + "Either [targetSourceDirectory] or [targetResourceDirectory] must be specified.");
		}
		targetDirectory.mkdirs();
		log(" * Output ["+targetType.toString()+"] Directory: " + targetDirectory.getAbsolutePath());

		try {
			/*
			 * Single file with all elements
			 */
			if (targetFile != null) {
				if (targetType == TargetType.SOURCE) {
					if (!targetFile.endsWith(".java")) {
						targetFile += ".java";
					}
				}
				File target = new File(targetDirectory, targetFile);
				OutputStreamWriter targetWriter = new OutputStreamWriter(new FileOutputStream(target, false), "UTF-8");

				/*
				 * Next, deal with the template and initialize velocity
				 */
				VelocityEngine v = VelocityHelper.configureVelocityEngine(templateFile, velocityPath, velocityProperties);
				InputStream templateIs = null;
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
				ctx.put("isRi", BaseStructureSpreadsheetParser.determineVersionEnum(version).isRi());
				ctx.put("hash", "#");
				ctx.put("esc", new TinderResourceGeneratorMojo.EscapeTool());
				if (BaseStructureSpreadsheetParser.determineVersionEnum(version).isRi()) {
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
					log("Writing Resources...");
					ctx.put("resources", rp.getResources());
					v.evaluate(ctx, targetWriter, "", templateReader);
					targetWriter.close();
				} else {
					DatatypeGeneratorUsingSpreadsheet dtp = context.getDatatypeGenerator();
					if (generateDatatypes && dtp != null) {
						log("Writing DataTypes...");
						ctx.put("datatypes", dtp.getResources());
						v.evaluate(ctx, targetWriter, "", templateReader);
						targetWriter.close();
					}
				}

			/*
			 * Multiple files.. one for each element
			 */
			} else {
				/*
				 * Write resources if selected
				 */
				BaseStructureParser rp = context.getResourceGenerator();
				if (generateResources && rp != null) {
					log("Writing Resources...");
					rp.setFilenamePrefix(filenamePrefix);
					rp.setFilenameSuffix(filenameSuffix);
					rp.setTemplate(template);
					rp.setTemplateFile(templateFile);
					rp.setVelocityPath(velocityPath);
					rp.setVelocityProperties(velocityProperties);
					rp.writeAll(targetType, targetDirectory, null, targetPackage);
				}

				/*
				 * Write composite datatypes
				 */
				DatatypeGeneratorUsingSpreadsheet dtp = context.getDatatypeGenerator();
				if (generateDatatypes && dtp != null) {
					log("Writing Composite Datatypes...");
					dtp.setFilenamePrefix(filenamePrefix);
					dtp.setFilenameSuffix(filenameSuffix);
					dtp.setTemplate(template);
					dtp.setTemplateFile(templateFile);
					dtp.setVelocityPath(velocityPath);
					dtp.setVelocityProperties(velocityProperties);
					dtp.writeAll(targetType, targetDirectory, null, targetPackage);
				}

				/*
				 * Write valuesets
				 */
				ValueSetGenerator vsp = context.getValueSetGenerator();
				if (generateValueSets && vsp != null) {
					log("Writing ValueSet Enums...");
					vsp.setFilenamePrefix(filenamePrefix);
					vsp.setFilenameSuffix(filenameSuffix);
					vsp.setTemplate(template);
					vsp.setTemplateFile(templateFile);
					vsp.setVelocityPath(velocityPath);
					vsp.setVelocityProperties(velocityProperties);
					vsp.writeMarkedValueSets(targetType, targetDirectory, targetPackage);
				}

			}

		} catch (Exception e) {
			if (e instanceof BuildException) {
				throw (BuildException)e;
			}
			log("Caught exception: "+e.getClass().getName()+" ["+e.getMessage()+"]", 1);
			e.printStackTrace();
			throw new BuildException(Msg.code(142) + "Error processing "+getTaskName()+" task.", e);
		} finally {
			cleanup();
		}
	}

	public void setExcludeResources(String names) {
		if (null == this.excludeResources) {
			this.excludeResources = new ArrayList<String>();
		}
		if (names != null) {
			StringTokenizer tokens = new StringTokenizer(names, ", \t\r\n");
			while (tokens.hasMoreTokens()) {
				String token = tokens.nextToken();
				this.excludeResources.add(token.trim());
			}
		}
	}

	public void setFilenamePrefix(String filenamePrefix) {
		this.filenamePrefix = filenamePrefix;
	}

	public void setFilenameSuffix(String filenameSuffix) {
		this.filenameSuffix = filenameSuffix;
	}

	public void setGenerateDatatypes(boolean generateDatatypes) {
		this.generateDatatypes = generateDatatypes;
	}

	public void setGenerateProfiles(boolean generateProfiles) {
		this.generateProfiles = generateProfiles;
	}

	public void setGenerateResources(boolean generateResources) {
		this.generateResources = generateResources;
	}

	public void setGenerateValueSets(boolean generateValueSets) {
		this.generateValueSets = generateValueSets;
	}

	public void setIncludeResources(String names) {
		if (null == this.includeResources) {
			this.includeResources = new ArrayList<String>();
		}
		if (names != null) {
			StringTokenizer tokens = new StringTokenizer(names, ", \t\r\n");
			while (tokens.hasMoreTokens()) {
				String token = tokens.nextToken();
				this.includeResources.add(token.trim());
			}
		}
	}

	public void setPackageBase(String packageBase) {
		this.packageBase = packageBase;
	}

	public void setProjectHome(String projectHome) {
		this.projectHome = projectHome;
	}

	public String getResourceSource() {
		return resourceSource;
	}

	public void setResourceSource(String resourceSource) {
		this.resourceSource = resourceSource;
	}

	public void setTargetFile(String targetFile) {
		this.targetFile = targetFile;
	}

	public void setTargetFolder(String targetFolder) {
		this.targetFolder = targetFolder;
	}

	public void setTargetPackage(String targetPackage) {
		this.targetPackage = targetPackage;
	}

	public void setTargetResourceDirectory(File targetResourceDirectory) {
		this.targetResourceDirectory = targetResourceDirectory;
	}

	public void setTargetSourceDirectory(File targetSourceDirectory) {
		this.targetSourceDirectory = targetSourceDirectory;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public void setTemplateFile(File templateFile) {
		this.templateFile = templateFile;
	}

	public void setValueSetFiles(String names) {
		if (null == this.valueSetFiles) {
			this.valueSetFiles = new ArrayList<ValueSetFileDefinition>();
		}
		if (names != null) {
			StringTokenizer tokens = new StringTokenizer(names, ", \t\r\n");
			while (tokens.hasMoreTokens()) {
				String token = tokens.nextToken();
				ValueSetFileDefinition def = new ValueSetFileDefinition();
				def.setValueSetFile(token.trim());
				this.valueSetFiles.add(def);
			}
		}
	}

	public void setVelocityPath(String velocityPath) {
		this.velocityPath = velocityPath;
	}

	public void setVelocityProperties(String velocityProperties) {
		this.velocityProperties = velocityProperties;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}

	protected void validateAttributes () throws BuildException {
		if (null == version) {
			throw new BuildException(Msg.code(143) + "The "+this.getTaskName()+" task requires \"version\" attribute.");
		}
		if (null == template) {
			if (null == templateFile) {
				throw new BuildException(Msg.code(144) + "The "+this.getTaskName()+" task requires \"template\" or \"templateFile\" attribute.");
			}
			if (!templateFile.exists()) {
				throw new BuildException(Msg.code(145) + "The Velocity template file  ["+templateFile.getAbsolutePath()+"] does not exist.");
			}
			if (!templateFile.canRead()) {
				throw new BuildException(Msg.code(146) + "The Velocity template file ["+templateFile.getAbsolutePath()+"] cannot be read.");
			}
			if (!templateFile.isFile()) {
				throw new BuildException(Msg.code(147) + "The Velocity template file ["+templateFile.getAbsolutePath()+"] is not a file.");
			}
		}

		if (null == projectHome) {
			throw new BuildException(Msg.code(148) + "The "+this.getTaskName()+" task requires \"projectHome\" attribute.");
		}
	}

	class Generator extends AbstractGenerator {
		@Override
		protected void logDebug(String message) {
			if (verbose) {
				log(message);
			}
		}

		@Override
		protected void logInfo(String message) {
			log(message);
		}
	}
	
}
