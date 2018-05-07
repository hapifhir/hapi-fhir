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

import ca.uhn.fhir.tinder.TinderStructuresMojo.ValueSetFileDefinition;
import ca.uhn.fhir.tinder.parser.DatatypeGeneratorUsingSpreadsheet;
import ca.uhn.fhir.tinder.parser.ResourceGeneratorUsingSpreadsheet;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.List;

/**
 * @author Bill.Denton
 *
 */
public class GeneratorContext {
	private String version;
	private String packageSuffix;
	private String baseDir;
	private List<String> includeResources;
	private List<String> excludeResources;
	private List<ValueSetFileDefinition> valueSetFiles;
	private ResourceGeneratorUsingSpreadsheet resourceGenerator = null;
	private ValueSetGenerator valueSetGenerator = null;
	private DatatypeGeneratorUsingSpreadsheet datatypeGenerator = null;

	public String getBaseDir() {
		return baseDir;
	}

	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}

	public DatatypeGeneratorUsingSpreadsheet getDatatypeGenerator() {
		return datatypeGenerator;
	}

	public void setDatatypeGenerator(DatatypeGeneratorUsingSpreadsheet datatypeGenerator) {
		this.datatypeGenerator = datatypeGenerator;
	}

	public List<String> getExcludeResources() {
		return excludeResources;
	}

	public void setExcludeResources(List<String> excludeResources) {
		this.excludeResources = excludeResources;
	}

	public List<String> getIncludeResources() {
		return includeResources;
	}
	public void setIncludeResources(List<String> includeResources) {
		this.includeResources = includeResources;
	}

	public String getPackageSuffix() {
		return packageSuffix;
	}

	public void setPackageSuffix(String packageSuffix) {
		this.packageSuffix = packageSuffix;
	}

	public ResourceGeneratorUsingSpreadsheet getResourceGenerator() {
		return resourceGenerator;
	}

	public void setResourceGenerator(ResourceGeneratorUsingSpreadsheet resourceGenerator) {
		this.resourceGenerator = resourceGenerator;
	}

	public List<ValueSetFileDefinition> getValueSetFiles() {
		return valueSetFiles;
	}

	public void setValueSetFiles(List<ValueSetFileDefinition> valueSetFiles) {
		this.valueSetFiles = valueSetFiles;
	}

	public ValueSetGenerator getValueSetGenerator() {
		return valueSetGenerator;
	}
	public void setValueSetGenerator(ValueSetGenerator valueSetGenerator) {
		this.valueSetGenerator = valueSetGenerator;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public static class ProfileFileDefinition {
		@Parameter(required = true)
		String profileFile;

		@Parameter(required = true)
		String profileSourceUrl;

		public String getProfileFile() {
			return profileFile;
		}

		public void setProfileFile(String profileFile) {
			this.profileFile = profileFile;
		}

		public String getProfileSourceUrl() {
			return profileSourceUrl;
		}

		public void setProfileSourceUrl(String profileSourceUrl) {
			this.profileSourceUrl = profileSourceUrl;
		}
	}
}
