package ca.uhn.fhir.tinder;
/*
 * #%L
 * HAPI FHIR Tinder Plug-In
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeSet;

import org.apache.maven.plugin.MojoFailureException;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.tinder.GeneratorContext.ProfileFileDefinition;
import ca.uhn.fhir.tinder.parser.DatatypeGeneratorUsingSpreadsheet;
import ca.uhn.fhir.tinder.parser.ProfileParser;
import ca.uhn.fhir.tinder.parser.ResourceGeneratorUsingSpreadsheet;

public abstract class AbstractGenerator {

	protected abstract void logInfo (String message);
	protected abstract void logDebug (String message);
	
	public void prepare (GeneratorContext context) throws ExecutionException, FailureException {

		/*
		 * Deal with the FHIR spec version
		 */
		FhirContext fhirContext;
		String packageSuffix = "";
		if ("dstu".equals(context.getVersion())) {
			fhirContext = FhirContext.forDstu1();
		} else if ("dstu2".equals(context.getVersion())) {
			fhirContext = FhirContext.forDstu2();
		} else if ("dstu3".equals(context.getVersion())) {
			fhirContext = FhirContext.forDstu3();
			packageSuffix = ".dstu3";
		} else {
			throw new FailureException("Unknown version configured: " + context.getVersion());
		}
		context.setPackageSuffix(packageSuffix);
		
		/*
		 * Deal with which resources to process
		 */
		List<String> includeResources = context.getIncludeResources();
		List<String> excludeResources = context.getExcludeResources();
		
		if (includeResources == null || includeResources.isEmpty()) {
			includeResources = new ArrayList<String>();
			
			logInfo("No resource names supplied, going to use all resources from version: "+fhirContext.getVersion().getVersion());
			
			Properties p = new Properties();
			try {
				p.load(fhirContext.getVersion().getFhirVersionPropertiesFile());
			} catch (IOException e) {
				throw new FailureException("Failed to load version property file", e);
			}

			logDebug("Property file contains: "+p);

			TreeSet<String> keys = new TreeSet<String>();
			for(Object next : p.keySet()) {
				keys.add((String) next);
			}
			for (String next : keys) {
				if (next.startsWith("resource.")) {
					includeResources.add(next.substring("resource.".length()).toLowerCase());
				}
			}
			
			/*
			 * No spreadsheet existed for Binary in DSTU1 so we don't generate it.. this
			 * is something we could work around, but at this point why bother since it's 
			 * only an issue for DSTU1
			 */
			if (fhirContext.getVersion().getVersion() == FhirVersionEnum.DSTU1) {
				includeResources.remove("binary");
			}
			if (fhirContext.getVersion().getVersion() == FhirVersionEnum.DSTU3) {
				includeResources.remove("conformance");
			}
		}

		for (int i = 0; i < includeResources.size(); i++) {
			includeResources.set(i, includeResources.get(i).toLowerCase());
		}

		if (excludeResources != null) {
			for (int i = 0; i < excludeResources.size(); i++) {
				excludeResources.set(i, excludeResources.get(i).toLowerCase());
			}
			includeResources.removeAll(excludeResources);
		}
		context.setIncludeResources(includeResources);
		
		logInfo("Including the following elements: "+includeResources);
		

		/*
		 * Fill in ValueSet and DataTypes used by the resources
		 */
		ValueSetGenerator vsp = null;
		DatatypeGeneratorUsingSpreadsheet dtp = null;
		ProfileParser pp = null;
		Map<String, String> datatypeLocalImports = new HashMap<String, String>();

		vsp = new ValueSetGenerator(context.getVersion());
		vsp.setResourceValueSetFiles(context.getValueSetFiles());
		context.setValueSetGenerator(vsp);
		try {
			vsp.parse();
		} catch (Exception e) {
			throw new FailureException("Failed to load valuesets", e);
		}

		/*
		 * A few enums are not found by default because none of the generated classes
		 * refer to them, but we still want them.
		 */
		vsp.getClassForValueSetIdAndMarkAsNeeded("NarrativeStatus");

		logInfo("Loading Datatypes...");

		dtp = new DatatypeGeneratorUsingSpreadsheet(context.getVersion(), context.getBaseDir());
		context.setDatatypeGenerator(dtp);
		try {
			dtp.parse();
			dtp.markResourcesForImports();
		} catch (Exception e) {
			throw new FailureException("Failed to load datatypes", e);
		}
		dtp.bindValueSets(vsp);

		datatypeLocalImports = dtp.getLocalImports();

		/*
		 * Load the requested resources
		 */
		ResourceGeneratorUsingSpreadsheet rp = new ResourceGeneratorUsingSpreadsheet(context.getVersion(), context.getBaseDir());
		context.setResourceGenerator(rp);
		logInfo("Loading Resources...");
		try {
			rp.setBaseResourceNames(includeResources);
			rp.parse();
			rp.markResourcesForImports();
		} catch (Exception e) {
			throw new FailureException("Failed to load resources", e);
		}

		rp.bindValueSets(vsp);
		rp.getLocalImports().putAll(datatypeLocalImports);
		datatypeLocalImports.putAll(rp.getLocalImports());
		rp.combineContentMaps(dtp);
		dtp.combineContentMaps(rp);

		if (context.getProfileFiles() != null) {
			logInfo("Loading profiles...");
			pp = new ProfileParser(context.getVersion(), context.getBaseDir());
			context.setProfileParser(pp);
			for (ProfileFileDefinition next : context.getProfileFiles()) {
				logInfo("Parsing file: "+next.profileFile);
				try {
					pp.parseSingleProfile(new File(next.profileFile), next.profileSourceUrl);
				} catch (MojoFailureException e) {
					throw new FailureException(e);
				}
			}

			pp.bindValueSets(vsp);
			pp.markResourcesForImports();
			pp.getLocalImports().putAll(datatypeLocalImports);
			datatypeLocalImports.putAll(pp.getLocalImports());

			pp.combineContentMaps(rp);
			pp.combineContentMaps(dtp);
			dtp.combineContentMaps(pp);
		}
	}

	public class FailureException extends Exception {

		public FailureException(String message, Throwable cause) {
			super(message, cause);
		}
		public FailureException(Throwable cause) {
			super(cause);
		}

		public FailureException(String message) {
			super(message);
		}

	}

	public class ExecutionException extends Exception {

		public ExecutionException(String message, Throwable cause) {
			super(message, cause);
		}

		public ExecutionException(String message) {
			super(message);
		}

	}
}
