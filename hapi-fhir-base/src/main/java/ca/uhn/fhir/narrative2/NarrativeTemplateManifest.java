/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.narrative2;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.ClasspathUtil;
import com.google.common.base.Charsets;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;
import jakarta.annotation.Nonnull;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class NarrativeTemplateManifest implements INarrativeTemplateManifest {
	private static final Logger ourLog = LoggerFactory.getLogger(NarrativeTemplateManifest.class);

	private final ListMultimap<String, NarrativeTemplate> myResourceTypeToTemplate;
	private final ListMultimap<String, NarrativeTemplate> myDatatypeToTemplate;
	private final ListMultimap<String, NarrativeTemplate> myNameToTemplate;
	private final ListMultimap<String, NarrativeTemplate> myFragmentNameToTemplate;
	private final ListMultimap<String, NarrativeTemplate> myClassToTemplate;
	private final int myTemplateCount;

	private NarrativeTemplateManifest(Collection<NarrativeTemplate> theTemplates) {
		ListMultimap<String, NarrativeTemplate> resourceTypeToTemplate = ArrayListMultimap.create();
		ListMultimap<String, NarrativeTemplate> datatypeToTemplate = ArrayListMultimap.create();
		ListMultimap<String, NarrativeTemplate> nameToTemplate = ArrayListMultimap.create();
		ListMultimap<String, NarrativeTemplate> classToTemplate = ArrayListMultimap.create();
		ListMultimap<String, NarrativeTemplate> fragmentNameToTemplate = ArrayListMultimap.create();

		for (NarrativeTemplate nextTemplate : theTemplates) {
			nameToTemplate.put(nextTemplate.getTemplateName(), nextTemplate);
			for (String nextResourceType : nextTemplate.getAppliesToResourceTypes()) {
				resourceTypeToTemplate.put(nextResourceType.toUpperCase(), nextTemplate);
			}
			for (String nextDataType : nextTemplate.getAppliesToDataTypes()) {
				datatypeToTemplate.put(nextDataType.toUpperCase(), nextTemplate);
			}
			for (Class<? extends IBase> nextAppliesToClass : nextTemplate.getAppliesToClasses()) {
				classToTemplate.put(nextAppliesToClass.getName(), nextTemplate);
			}
			for (String nextFragmentName : nextTemplate.getAppliesToFragmentNames()) {
				fragmentNameToTemplate.put(nextFragmentName, nextTemplate);
			}
		}

		myTemplateCount = theTemplates.size();
		myClassToTemplate = Multimaps.unmodifiableListMultimap(classToTemplate);
		myNameToTemplate = Multimaps.unmodifiableListMultimap(nameToTemplate);
		myResourceTypeToTemplate = Multimaps.unmodifiableListMultimap(resourceTypeToTemplate);
		myDatatypeToTemplate = Multimaps.unmodifiableListMultimap(datatypeToTemplate);
		myFragmentNameToTemplate = Multimaps.unmodifiableListMultimap(fragmentNameToTemplate);
	}

	public int getNamedTemplateCount() {
		return myTemplateCount;
	}

	@Override
	public List<INarrativeTemplate> getTemplateByResourceName(
			@Nonnull FhirContext theFhirContext,
			@Nonnull EnumSet<TemplateTypeEnum> theStyles,
			@Nonnull String theResourceName,
			@Nonnull Collection<String> theProfiles) {
		return getFromMap(theStyles, theResourceName.toUpperCase(), myResourceTypeToTemplate, theProfiles);
	}

	@Override
	public List<INarrativeTemplate> getTemplateByName(
			@Nonnull FhirContext theFhirContext,
			@Nonnull EnumSet<TemplateTypeEnum> theStyles,
			@Nonnull String theName) {
		return getFromMap(theStyles, theName, myNameToTemplate, Collections.emptyList());
	}

	@Override
	public List<INarrativeTemplate> getTemplateByFragmentName(
			@Nonnull FhirContext theFhirContext,
			@Nonnull EnumSet<TemplateTypeEnum> theStyles,
			@Nonnull String theFragmentName) {
		return getFromMap(theStyles, theFragmentName, myFragmentNameToTemplate, Collections.emptyList());
	}

	@SuppressWarnings("PatternVariableCanBeUsed")
	@Override
	public List<INarrativeTemplate> getTemplateByElement(
			@Nonnull FhirContext theFhirContext,
			@Nonnull EnumSet<TemplateTypeEnum> theStyles,
			@Nonnull IBase theElement) {
		List<INarrativeTemplate> retVal = Collections.emptyList();

		if (theElement instanceof IBaseResource) {
			IBaseResource resource = (IBaseResource) theElement;
			String resourceName = theFhirContext.getResourceDefinition(resource).getName();
			List<String> profiles = resource.getMeta().getProfile().stream()
					.filter(Objects::nonNull)
					.map(IPrimitiveType::getValueAsString)
					.filter(StringUtils::isNotBlank)
					.collect(Collectors.toList());
			retVal = getTemplateByResourceName(theFhirContext, theStyles, resourceName, profiles);
		}

		if (retVal.isEmpty()) {
			retVal = getFromMap(theStyles, theElement.getClass().getName(), myClassToTemplate, Collections.emptyList());
		}

		if (retVal.isEmpty()) {
			String datatypeName =
					theFhirContext.getElementDefinition(theElement.getClass()).getName();
			retVal = getFromMap(theStyles, datatypeName.toUpperCase(), myDatatypeToTemplate, Collections.emptyList());
		}
		return retVal;
	}

	public static NarrativeTemplateManifest forManifestFileLocation(String... thePropertyFilePaths) {
		return forManifestFileLocation(Arrays.asList(thePropertyFilePaths));
	}

	public static NarrativeTemplateManifest forManifestFileLocation(Collection<String> thePropertyFilePaths) {
		ourLog.debug("Loading narrative properties file(s): {}", thePropertyFilePaths);

		List<String> manifestFileContents = new ArrayList<>(thePropertyFilePaths.size());
		for (String next : thePropertyFilePaths) {
			String resource = loadResource(next);
			manifestFileContents.add(resource);
		}

		return forManifestFileContents(manifestFileContents);
	}

	public static NarrativeTemplateManifest forManifestFileContents(String... theResources) {
		return forManifestFileContents(Arrays.asList(theResources));
	}

	public static NarrativeTemplateManifest forManifestFileContents(Collection<String> theResources) {
		try {
			List<NarrativeTemplate> templates = new ArrayList<>();
			for (String next : theResources) {
				templates.addAll(loadProperties(next));
			}
			return new NarrativeTemplateManifest(templates);
		} catch (IOException e) {
			throw new InternalErrorException(Msg.code(1808) + e);
		}
	}

	@SuppressWarnings("unchecked")
	private static Collection<NarrativeTemplate> loadProperties(String theManifestText) throws IOException {
		Map<String, NarrativeTemplate> nameToTemplate = new HashMap<>();

		Properties file = new Properties();

		file.load(new StringReader(theManifestText));
		for (Object nextKeyObj : file.keySet()) {
			String nextKey = (String) nextKeyObj;
			Validate.isTrue(
					StringUtils.countMatches(nextKey, ".") == 1, "Invalid narrative property file key: %s", nextKey);
			String name = nextKey.substring(0, nextKey.indexOf('.'));
			Validate.notBlank(name, "Invalid narrative property file key: %s", nextKey);

			NarrativeTemplate nextTemplate =
					nameToTemplate.computeIfAbsent(name, t -> new NarrativeTemplate().setTemplateName(name));

			if (nextKey.endsWith(".class")) {
				String className = file.getProperty(nextKey);
				if (isNotBlank(className)) {
					try {
						nextTemplate.addAppliesToClass((Class<? extends IBase>) Class.forName(className));
					} catch (ClassNotFoundException theE) {
						throw new InternalErrorException(Msg.code(1867) + "Could not find class " + className
								+ " declared in narrative manifest");
					}
				}
			} else if (nextKey.endsWith(".profile")) {
				String profile = file.getProperty(nextKey);
				if (isNotBlank(profile)) {
					nextTemplate.addAppliesToProfile(profile);
				}
			} else if (nextKey.endsWith(".resourceType")) {
				String resourceType = file.getProperty(nextKey);
				parseValuesAndAddToMap(resourceType, nextTemplate::addAppliesToResourceType);
			} else if (nextKey.endsWith(".fragmentName")) {
				String resourceType = file.getProperty(nextKey);
				parseValuesAndAddToMap(resourceType, nextTemplate::addAppliesToFragmentName);
			} else if (nextKey.endsWith(".dataType")) {
				String dataType = file.getProperty(nextKey);
				parseValuesAndAddToMap(dataType, nextTemplate::addAppliesToDatatype);
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
				throw new ConfigurationException(Msg.code(1868) + "Invalid property name: " + nextKey
						+ " - the key must end in one of the expected extensions "
						+ "'.profile', '.resourceType', '.dataType', '.style', '.contextPath', '.narrative', '.title'");
			}
		}

		return nameToTemplate.values();
	}

	private static void parseValuesAndAddToMap(String resourceType, Consumer<String> addAppliesToResourceType) {
		Arrays.stream(resourceType.split(","))
				.map(String::trim)
				.filter(StringUtils::isNotBlank)
				.forEach(addAppliesToResourceType);
	}

	static String loadResource(String theName) {
		if (theName.startsWith("classpath:")) {
			return ClasspathUtil.loadResource(theName);
		} else if (theName.startsWith("file:")) {
			File file = new File(theName.substring("file:".length()));
			if (file.exists() == false) {
				throw new InternalErrorException(Msg.code(1870) + "File not found: " + file.getAbsolutePath());
			}
			try (FileInputStream inputStream = new FileInputStream(file)) {
				return IOUtils.toString(inputStream, Charsets.UTF_8);
			} catch (IOException e) {
				throw new InternalErrorException(Msg.code(1869) + e.getMessage(), e);
			}
		} else {
			throw new InternalErrorException(
					Msg.code(1871) + "Invalid resource name: '" + theName + "' (must start with classpath: or file: )");
		}
	}

	private static <T> List<INarrativeTemplate> getFromMap(
			EnumSet<TemplateTypeEnum> theStyles,
			T theKey,
			ListMultimap<T, NarrativeTemplate> theMap,
			Collection<String> theProfiles) {
		return theMap.get(theKey).stream()
				.filter(t -> theStyles.contains(t.getTemplateType()))
				.filter(t -> theProfiles.isEmpty()
						|| t.getAppliesToProfiles().stream().anyMatch(theProfiles::contains))
				.collect(Collectors.toList());
	}
}
