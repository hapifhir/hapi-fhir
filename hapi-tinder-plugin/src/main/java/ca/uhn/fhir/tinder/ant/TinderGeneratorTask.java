/**
 *  SOA Software, Inc. Copyright (C) 2000-2012, All rights reserved
 *
 *  This  software is the confidential and proprietary information of SOA Software, Inc. 
 *  and is subject to copyright protection under laws of the United States of America and 
 *  other countries. The  use of this software should be in accordance with the license 
 *  agreement terms you entered into with SOA Software, Inc.
 * 
 * $Id$
 */

package ca.uhn.fhir.tinder.ant;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import org.apache.commons.lang.WordUtils;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.util.FileUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.EscapeTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.tinder.parser.ResourceGeneratorUsingSpreadsheet;

/**
 *
 * @author Copyright (c) 2012, SOA Software, Inc. All rights reserved.
 * @since 6.0
 */
public class TinderGeneratorTask extends Task {

	// Parameter(required = true)
	private String version;

	// Parameter(required = true)
	private String template;
	private String templateFile;
	private File templateFileFile;
	
	// Parameter(required = true, defaultValue = "${project.build.directory}/..")
	private String projectHome;

	// Parameter(required = true, defaultValue = "${project.build.directory}/generated-sources/tinder")
	private String targetDir;
	private File targetDirectoryFile;

	private String targetPackage;

	// Parameter(required = true, defaultValue = "${project.build.directory}/generated-resources/tinder")
	private String targetFile;

	// Parameter(required = true)
	private String packageBase;
	
	private String targetClassSuffix;

	// Parameter(required = false)
	private List<String> baseResourceNames;

	// Parameter(required = false)
	private List<String> excludeResourceNames;
	
	private boolean verbose;

	private FhirContext fhirContext; // set from version in validateAttributes
    
	/**
	 * 
	 */
	public TinderGeneratorTask () {
		super();
	}
	
	@Override
	public void execute () throws BuildException {
		validateAttributes();

		try {
			
			if (baseResourceNames == null || baseResourceNames.isEmpty()) {
				baseResourceNames = new ArrayList<String>();
				
				log("No resource names supplied, going to use all resources from version: "+fhirContext.getVersion().getVersion());
				
				Properties p = new Properties();
				try {
					p.load(fhirContext.getVersion().getFhirVersionPropertiesFile());
				} catch (IOException e) {
					throw new BuildException("Failed to load version property file", e);
				}
				
				if (verbose) {
					log("Property file contains: "+p);
				}

				for(Object next : p.keySet()) {
					if (((String)next).startsWith("resource.")) {
						baseResourceNames.add(((String)next).substring("resource.".length()).toLowerCase());
					}
				}
			} else {
				for (int i = 0; i < baseResourceNames.size(); i++) {
					baseResourceNames.set(i, baseResourceNames.get(i).toLowerCase());
				}
			}

			if (excludeResourceNames != null) {
				for (int i = 0; i < excludeResourceNames.size(); i++) {
					baseResourceNames.remove(excludeResourceNames.get(i).toLowerCase());
				}
			}
			
			log("Including the following resources: "+baseResourceNames);
			
			ResourceGeneratorUsingSpreadsheet gen = new ResourceGeneratorUsingSpreadsheet(version, projectHome);
			gen.setBaseResourceNames(baseResourceNames);

			try {
				gen.parse();

//				gen.setFilenameSuffix("ResourceProvider");
//				gen.setTemplate("/vm/jpa_daos.vm");
//				gen.writeAll(packageDirectoryBase, null,packageBase);

				// gen.setFilenameSuffix("ResourceTable");
				// gen.setTemplate("/vm/jpa_resource_table.vm");
				// gen.writeAll(directoryBase, packageBase);

			} catch (Exception e) {
				throw new BuildException("Failed to parse FHIR metadata", e);
			}

			try {
				VelocityContext ctx = new VelocityContext();
				ctx.put("resources", gen.getResources());
				ctx.put("packageBase", packageBase);
				ctx.put("targetPackage", targetPackage);
				ctx.put("version", version);
				ctx.put("esc", new EscapeTool());

				String capitalize = WordUtils.capitalize(version);
				if ("Dstu".equals(capitalize)) {
					capitalize="Dstu1";
				}
				ctx.put("versionCapitalized", capitalize);

				VelocityEngine v = new VelocityEngine();
				v.setProperty("resource.loader", "cp");
				v.setProperty("cp.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
				v.setProperty("runtime.references.strict", Boolean.TRUE);

				targetDirectoryFile.mkdirs();
				
				if (targetFile != null) {
					InputStream templateIs = null;
					if (templateFileFile != null) {
						templateIs = new FileInputStream(templateFileFile);
					} else {
						templateIs = ResourceGeneratorUsingSpreadsheet.class.getResourceAsStream(template);
					}
					InputStreamReader templateReader = new InputStreamReader(templateIs);

					File target = null;
					if (targetPackage != null) {
						target = new File(targetDir, targetPackage.replace('.', File.separatorChar));
					} else {
						target = new File(targetDir);
					}
					target.mkdirs();
					File f = new File(target, targetFile);
					OutputStreamWriter w = new OutputStreamWriter(new FileOutputStream(f, false), "UTF-8");

					v.evaluate(ctx, w, "", templateReader);
					w.close();
				
				} else {
					File packageDirectoryBase = new File(targetDir, packageBase.replace(".", File.separatorChar + ""));
					packageDirectoryBase.mkdirs();

					gen.setFilenameSuffix(targetClassSuffix);
					gen.setTemplate(template);
					gen.setTemplateFile(templateFileFile);
					gen.writeAll(packageDirectoryBase, null,packageBase);
	
				}

			} catch (Exception e) {
				log("Caught exception: "+e.getClass().getName()+" ["+e.getMessage()+"]", 1);
				e.printStackTrace();
				throw new BuildException("Failed to generate file(s)", e);
			}
			
			cleanup();
		
		} catch (Exception e) {
			if (e instanceof BuildException) {
				throw (BuildException)e;
			}
			log("Caught exception: "+e.getClass().getName()+" ["+e.getMessage()+"]", 1);
			e.printStackTrace();
			throw new BuildException("Error processing "+getTaskName()+" task.", e);
		}
	}

	protected void validateAttributes () throws BuildException {
		if (null == version) {
			throw new BuildException("The "+this.getTaskName()+" task requires \"version\" attribute.");
		}
		if ("dstu".equals(version)) {
			fhirContext = FhirContext.forDstu1();
		} else if ("dstu2".equals(version)) {
			fhirContext = FhirContext.forDstu2();
		} else if ("dstu3".equals(version)) {
			fhirContext = FhirContext.forDstu3();
		} else {
			throw new BuildException("Unknown version configured: " + version);
		}

		if (null == template) {
			if (null == templateFile) {
				throw new BuildException("The "+this.getTaskName()+" task requires \"template\" or \"templateFile\" attribute.");
			}
			templateFileFile = new File(templateFile);
			if (!templateFileFile.exists()) {
				throw new BuildException("The Velocity template file  ["+templateFileFile.getAbsolutePath()+"] does not exist.");
			}
			if (!templateFileFile.canRead()) {
				throw new BuildException("The Velocity template file ["+templateFileFile.getAbsolutePath()+"] cannot be read.");
			}
			if (!templateFileFile.isFile()) {
				throw new BuildException("The Velocity template file ["+templateFileFile.getAbsolutePath()+"] is not a file.");
			}
		}

		if (null == projectHome) {
			throw new BuildException("The "+this.getTaskName()+" task requires \"projectHome\" attribute.");
		}
		
		if (null == targetDir) {
			throw new BuildException("The "+this.getTaskName()+" task requires \"targetDirectory\" attribute.");
		}
		targetDirectoryFile = new File(targetDir);

		if (targetFile != null || (packageBase != null && targetClassSuffix != null)) {
			// this is good
		} else {
			throw new BuildException("The "+this.getTaskName()+" task requires either the \"targetFile\" attribute or both the \"packageBase\" and \"targetClassSuffix\" attributes.");

		}
	}
	
	protected void cleanup () {
	}

	
	public void setBaseResourceNames(String names) {
		if (null == this.baseResourceNames) {
			this.baseResourceNames = new ArrayList<String>();
		}
		if (names != null) {
			List<String> work = new ArrayList<String>();
			String[] tokens = names.split(",");
			for (String token : tokens) {
				work.add(token.trim());
			}
			this.baseResourceNames.addAll(work);
		}
	}
	public void setExcludeResourceNames(String names) {
		if (null == this.excludeResourceNames) {
			this.excludeResourceNames = new ArrayList<String>();
		}
		if (names != null) {
			List<String> work = new ArrayList<String>();
			String[] tokens = names.split(",");
			for (String token : tokens) {
				work.add(token.trim());
			}
			this.excludeResourceNames.addAll(work);
		}
	}
    public void setTemplate(String template) {
		this.template = template;
	}
    public void setTemplateFile(String template) {
		this.templateFile = template;
	}
	public void setProjectHome(String projectHome) {
		this.projectHome = projectHome;
	}
	public void setTargetDir(String targetDirectory) {
		this.targetDir = targetDirectory;
	}
	public void setTargetFile(String targetFile) {
		this.targetFile = targetFile;
	}
	public void setTargetPackage(String targetPackage) {
		this.targetPackage = targetPackage;
	}
	public void setPackageBase(String packageBase) {
		this.packageBase = packageBase;
	}
	public void setTargetClassSuffix(String targetClassSuffix) {
		this.targetClassSuffix = targetClassSuffix;
	}
	public static class Version extends EnumeratedAttribute {
        public String[] getValues() {
            return new String[] {"dstu", "dstu2", "dstu3"};
        }
    }
    public void setVersion(Version version) {
        this.version = version.getValue();
    }

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}
}
