package ca.uhn.fhir.narrative2;

/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class NarrativeTemplateManifest implements INarrativeTemplateManifest {
	private static final Logger ourLog = LoggerFactory.getLogger(NarrativeTemplateManifest.class);

	private final Map<String, List<NarrativeTemplate>> myStyleToResourceTypeToTemplate;
	private final Map<String, List<NarrativeTemplate>> myStyleToDatatypeToTemplate;
	private final Map<String, List<NarrativeTemplate>> myStyleToNameToTemplate;
	private final int myTemplateCount;

	private NarrativeTemplateManifest(Collection<NarrativeTemplate> theTemplates) {
		Map<String, List<NarrativeTemplate>> resourceTypeToTemplate = new HashMap<>();
		Map<String, List<NarrativeTemplate>> datatypeToTemplate = new HashMap<>();
		Map<String, List<NarrativeTemplate>> nameToTemplate = new HashMap<>();

		for (NarrativeTemplate nextTemplate : theTemplates) {
			nameToTemplate.computeIfAbsent(nextTemplate.getTemplateName(), t -> new ArrayList<>()).add(nextTemplate);
			for (String nextResourceType : nextTemplate.getAppliesToResourceTypes()) {
				resourceTypeToTemplate.computeIfAbsent(nextResourceType.toUpperCase(), t -> new ArrayList<>()).add(nextTemplate);
			}
			for (String nextDataType : nextTemplate.getAppliesToDataTypes()) {
				datatypeToTemplate.computeIfAbsent(nextDataType.toUpperCase(), t -> new ArrayList<>()).add(nextTemplate);
			}
		}

		myTemplateCount = theTemplates.size();
		myStyleToNameToTemplate = makeImmutable(nameToTemplate);
		myStyleToResourceTypeToTemplate = makeImmutable(resourceTypeToTemplate);
		myStyleToDatatypeToTemplate = makeImmutable(datatypeToTemplate);
	}

	public int getNamedTemplateCount() {
		return myTemplateCount;
	}

	@Override
	public List<INarrativeTemplate> getTemplateByResourceName(FhirContext theFhirContext, EnumSet<TemplateTypeEnum> theStyles, String theResourceName) {
		return getFromMap(theStyles, theResourceName.toUpperCase(), myStyleToResourceTypeToTemplate);
	}

	@Override
	public List<INarrativeTemplate> getTemplateByName(FhirContext theFhirContext, EnumSet<TemplateTypeEnum> theStyles, String theName) {
		return getFromMap(theStyles, theName, myStyleToNameToTemplate);
	}

	@Override
	public List<INarrativeTemplate> getTemplateByElement(FhirContext theFhirContext, EnumSet<TemplateTypeEnum> theStyles, IBase theElement) {
		if (theElement instanceof IBaseResource) {
			String resourceName = theFhirContext.getResourceDefinition((IBaseResource) theElement).getName();
			return getTemplateByResourceName(theFhirContext, theStyles, resourceName);
		} else {
			String datatypeName = theFhirContext.getElementDefinition(theElement.getClass()).getName();
			return getFromMap(theStyles, datatypeName.toUpperCase(), myStyleToDatatypeToTemplate);
		}
	}

	public static NarrativeTemplateManifest forManifestFileLocation(String... thePropertyFilePaths) throws IOException {
		return forManifestFileLocation(Arrays.asList(thePropertyFilePaths));
	}

	public static NarrativeTemplateManifest forManifestFileLocation(Collection<String> thePropertyFilePaths) throws IOException {
		ourLog.debug("Loading narrative properties file(s): {}", thePropertyFilePaths);

		List<String> manifestFileContents = new ArrayList<>(thePropertyFilePaths.size());
		for (String next : thePropertyFilePaths) {
			String resource = loadResource(next);
			manifestFileContents.add(resource);
		}

		return forManifestFileContents(manifestFileContents);
	}

	public static NarrativeTemplateManifest forManifestFileContents(String... theResources) throws IOException {
		return forManifestFileContents(Arrays.asList(theResources));
	}

	public static NarrativeTemplateManifest forManifestFileContents(Collection<String> theResources) throws IOException {
		List<NarrativeTemplate> templates = new ArrayList<>();
		for (String next : theResources) {
			templates.addAll(loadProperties(next));
		}
		return new NarrativeTemplateManifest(templates);
	}

	private static Collection<NarrativeTemplate> loadProperties(String theManifestText) throws IOException {
		Map<String, NarrativeTemplate> nameToTemplate = new HashMap<>();

		Properties file = new Properties();

		file.load(new StringReader(theManifestText));
		for (Object nextKeyObj : file.keySet()) {
			String nextKey = (String) nextKeyObj;
			Validate.isTrue(StringUtils.countMatches(nextKey, ".") == 1, "Invalid narrative property file key: %s", nextKey);
			String name = nextKey.substring(0, nextKey.indexOf('.'));
			Validate.notBlank(name, "Invalid narrative property file key: %s", nextKey);

			NarrativeTemplate nextTemplate = nameToTemplate.computeIfAbsent(name, t -> new NarrativeTemplate().setTemplateName(name));

			Validate.isTrue(!nextKey.endsWith(".class"), "Narrative manifest does not support specifying templates by class name - Use \"[name].resourceType=[resourceType]\" instead");

			if (nextKey.endsWith(".profile")) {
				String profile = file.getProperty(nextKey);
				if (isNotBlank(profile)) {
					nextTemplate.addAppliesToProfile(profile);
				}
			} else if (nextKey.endsWith(".resourceType")) {
				String resourceType = file.getProperty(nextKey);
				Arrays
					.stream(resourceType.split(","))
					.map(t -> t.trim())
					.filter(t -> isNotBlank(t))
					.forEach(t -> nextTemplate.addAppliesToResourceType(t));
			} else if (nextKey.endsWith(".dataType")) {
				String dataType = file.getProperty(nextKey);
				Arrays
					.stream(dataType.split(","))
					.map(t -> t.trim())
					.filter(t -> isNotBlank(t))
					.forEach(t -> nextTemplate.addAppliesToDatatype(t));
			} else if (nextKey.endsWith(".class")) {
				String className = file.getProperty(nextKey);
				Class<? extends IBase> clazz;
				try {
					clazz = (Class<? extends IBase>) Class.forName(className);
				} catch (ClassNotFoundException e) {
					ourLog.debug("Unknown datatype class '{}' identified in manifest", name);
					clazz = null;
				}
				if (clazz != null) {
					nextTemplate.addAppliesToResourceClass(clazz);
				}
			} else if (nextKey.endsWith(".style")) {
				String templateTypeName = file.getProperty(nextKey).toUpperCase();
				TemplateTypeEnum templateType = TemplateTypeEnum.valueOf(templateTypeName);
				nextTemplate.setTemplateType(templateType);
			} else if (nextKey.endsWith(".contextPath")) {
				String contextPath = file.getProperty(nextKey);
				nextTemplate.setContextPath(contextPath);
			} else if (nextKey.endsWith(".narrative")) {
				String narrativePropName = name + ".narrative";
				String narrativeName = file.getProperty(narrativePropName);
				if (StringUtils.isNotBlank(narrativeName)) {
					nextTemplate.setTemplateFileName(narrativeName);
				}
			} else if (nextKey.endsWith(".title")) {
				ourLog.debug("Ignoring title property as narrative generator no longer generates titles: {}", nextKey);
			} else {
				throw new ConfigurationException("Invalid property name: " + nextKey);
			}

		}

		return nameToTemplate.values();
	}

	static String loadResource(String name) throws IOException {
		if (name.startsWith("classpath:")) {
			String cpName = name.substring("classpath:".length());
			try (InputStream resource = DefaultThymeleafNarrativeGenerator.class.getResourceAsStream(cpName)) {
				if (resource == null) {
					try (InputStream resource2 = DefaultThymeleafNarrativeGenerator.class.getResourceAsStream("/" + cpName)) {
						if (resource2 == null) {
							throw new IOException("Can not find '" + cpName + "' on classpath");
						}
						return IOUtils.toString(resource2, Charsets.UTF_8);
					}
				}
				return IOUtils.toString(resource, Charsets.UTF_8);
			}
		} else if (name.startsWith("file:")) {
			File file = new File(name.substring("file:".length()));
			if (file.exists() == false) {
				throw new IOException("File not found: " + file.getAbsolutePath());
			}
			try (FileInputStream inputStream = new FileInputStream(file)) {
				return IOUtils.toString(inputStream, Charsets.UTF_8);
			}
		} else {
			throw new IOException("Invalid resource name: '" + name + "' (must start with classpath: or file: )");
		}
	}

	private static <T> List<INarrativeTemplate> getFromMap(EnumSet<TemplateTypeEnum> theStyles, T theKey, Map<T, List<NarrativeTemplate>> theMap) {
		return theMap
			.getOrDefault(theKey, Collections.emptyList())
			.stream()
			.filter(t->theStyles.contains(t.getTemplateType()))
			.collect(Collectors.toList());
	}

	private static <T> Map<T, List<NarrativeTemplate>> makeImmutable(Map<T, List<NarrativeTemplate>> theStyleToResourceTypeToTemplate) {
		theStyleToResourceTypeToTemplate.replaceAll((key, value) -> Collections.unmodifiableList(value));
		return Collections.unmodifiableMap(theStyleToResourceTypeToTemplate);
	}

}
