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
import java.util.List;

import javax.security.auth.login.FailedLoginException;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugins.annotations.Parameter;

import ca.uhn.fhir.tinder.AbstractGenerator.FailureException;
import ca.uhn.fhir.tinder.TinderStructuresMojo.ValueSetFileDefinition;
import ca.uhn.fhir.tinder.parser.BaseStructureParser;
import ca.uhn.fhir.tinder.parser.DatatypeGeneratorUsingSpreadsheet;

/**
 * @author Bill.Denton
 *
 */
public class GeneratorContext {
	public enum ResourceSource {SPREADSHEET, MODEL};
	public static final ResourceSource DEFAULT_RESOURCE_SOURCE = ResourceSource.SPREADSHEET;
	
	private String version;
	private String packageSuffix;
	private String baseDir;
	private List<String> includeResources;
	private List<String> excludeResources;
	private ResourceSource resourceSource = DEFAULT_RESOURCE_SOURCE; 
	private List<ValueSetFileDefinition> valueSetFiles;
	private BaseStructureParser resourceGenerator = null;
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

	public ResourceSource getResourceSource() {
		return resourceSource;
	}

	public void setResourceSource(ResourceSource resourceSource) {
		this.resourceSource = resourceSource;
	}

	public void setResourceSource(String resourceSource) throws FailureException {
		resourceSource = StringUtils.stripToNull(resourceSource);
		if (null == resourceSource) { 
			this.resourceSource = DEFAULT_RESOURCE_SOURCE;
		} else 
		if (ResourceSource.SPREADSHEET.name().equalsIgnoreCase(resourceSource)) {
			this.resourceSource = ResourceSource.SPREADSHEET;
		} else 
		if (ResourceSource.MODEL.name().equalsIgnoreCase(resourceSource)) {
			this.resourceSource = ResourceSource.MODEL;
		} else {
			throw new FailureException(Msg.code(112) + "Unknown resource-source option: " + resourceSource);
		}
	}

	public String getPackageSuffix() {
		return packageSuffix;
	}

	public void setPackageSuffix(String packageSuffix) {
		this.packageSuffix = packageSuffix;
	}

	public BaseStructureParser getResourceGenerator() {
		return resourceGenerator;
	}

	public void setResourceGenerator(BaseStructureParser resourceGenerator) {
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
