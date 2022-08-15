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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.tinder.GeneratorContext.ResourceSource;
import ca.uhn.fhir.tinder.parser.DatatypeGeneratorUsingSpreadsheet;
import ca.uhn.fhir.tinder.parser.ResourceGeneratorUsingModel;
import ca.uhn.fhir.tinder.parser.ResourceGeneratorUsingSpreadsheet;
import org.apache.maven.plugin.MojoFailureException;

import java.io.IOException;
import java.util.*;

public abstract class AbstractGenerator {

	protected abstract void logDebug (String message);

	protected abstract void logInfo (String message);
	
	public void prepare (GeneratorContext context) throws FailureException, MojoFailureException {

		/*
		 * Deal with the FHIR spec version
		 */
		FhirContext fhirContext;
		String packageSuffix = "";
		if ("dstu2".equals(context.getVersion())) {
			fhirContext = FhirContext.forDstu2();
		} else if ("dstu3".equals(context.getVersion())) {
			fhirContext = FhirContext.forDstu3();
			packageSuffix = ".dstu3";
		} else if ("r4".equals(context.getVersion())) {
			fhirContext = FhirContext.forR4();
			packageSuffix = ".r4";
		} else {
			throw new FailureException(Msg.code(95) + "Unknown version configured: " + context.getVersion());
		}
		context.setPackageSuffix(packageSuffix);
		
		/*
		 * Deal with which resources to process
		 */
		List<String> includeResources = context.getIncludeResources();
		List<String> excludeResources = context.getExcludeResources();
		
		if (includeResources == null || includeResources.isEmpty()) {
			includeResources = new ArrayList<>();
			
			logInfo("No resource names supplied, going to use all resources from version: "+fhirContext.getVersion().getVersion());
			
			Properties p = new Properties();
			try {
				p.load(fhirContext.getVersion().getFhirVersionPropertiesFile());
			} catch (IOException e) {
				throw new FailureException(Msg.code(96) + "Failed to load version property file", e);
			}

			logDebug("Property file contains: "+p);

			TreeSet<String> keys = new TreeSet<>();
			for(Object next : p.keySet()) {
				keys.add((String) next);
			}
			for (String next : keys) {
				if (next.startsWith("resource.")) {
					includeResources.add(next.substring("resource.".length()).toLowerCase());
				}
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
		Map<String, String> datatypeLocalImports = new HashMap<>();

		if (ResourceSource.SPREADSHEET.equals(context.getResourceSource())) {
			vsp = new ValueSetGenerator(context.getVersion());
			vsp.setResourceValueSetFiles(context.getValueSetFiles());
			context.setValueSetGenerator(vsp);
			try {
				vsp.parse();
			} catch (Exception e) {
				throw new FailureException(Msg.code(97) + "Failed to load valuesets", e);
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
				throw new FailureException(Msg.code(98) + "Failed to load datatypes", e);
			}
			dtp.bindValueSets(vsp);
	
			datatypeLocalImports = dtp.getLocalImports();
		}

		/*
		 * Load the requested resources
		 */
		
		logInfo("Loading Resources...");
		try {
			switch (context.getResourceSource()) {
				case SPREADSHEET: {
					logInfo("... resource definitions from spreadsheets");
					ResourceGeneratorUsingSpreadsheet rp = new ResourceGeneratorUsingSpreadsheet(context.getVersion(), context.getBaseDir());
					context.setResourceGenerator(rp);

					rp.setBaseResourceNames(includeResources);
					rp.parse();

					rp.markResourcesForImports();
					rp.bindValueSets(vsp);

					rp.getLocalImports().putAll(datatypeLocalImports);
					datatypeLocalImports.putAll(rp.getLocalImports());
					
					rp.combineContentMaps(dtp);
					dtp.combineContentMaps(rp);
					break;
				}
				case MODEL: {
					logInfo("... resource definitions from model structures");
					ResourceGeneratorUsingModel rp = new ResourceGeneratorUsingModel(context.getVersion(), context.getBaseDir());
					context.setResourceGenerator(rp);
					
					rp.setBaseResourceNames(includeResources);
					rp.parse();
					rp.markResourcesForImports();
					break;
				}
			}
		} catch (Exception e) {
			throw new FailureException(Msg.code(99) + "Failed to load resources", e);
		}

	}

	public static class FailureException extends Exception {

		FailureException(String message, Throwable cause) {
			super(message, cause);
		}

		FailureException(String message) {
			super(message);
		}

	}

	public static class ExecutionException extends Exception {

		public ExecutionException(String message) {
			super(message);
		}

	}
}
