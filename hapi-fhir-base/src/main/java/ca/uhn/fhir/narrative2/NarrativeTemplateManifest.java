package ca.uhn.fhir.narrative2;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class NarrativeTemplateManifest implements INarrativeTemplateManifest {
	private static final Logger ourLog = LoggerFactory.getLogger(NarrativeTemplateManifest.class);

	private final Map<TemplateTypeEnum, Map<String, NarrativeTemplate>> myStyleToResourceTypeToTemplate;
	private final Map<TemplateTypeEnum, Map<String, NarrativeTemplate>> myStyleToNameToTemplate;

	public NarrativeTemplateManifest(String thePropertyFilePath) throws IOException {
		Collection<NarrativeTemplate> templates = loadProperties(thePropertyFilePath);
		Map<TemplateTypeEnum, Map<String, NarrativeTemplate>> styleToResourceTypeToTemplate = new HashMap<>();
		Map<TemplateTypeEnum, Map<String, NarrativeTemplate>> styleToNameToTemplate = new HashMap<>();

		for (NarrativeTemplate nextTemplate : templates) {
			Map<String, NarrativeTemplate> resourceTypeToTemplate = styleToResourceTypeToTemplate.computeIfAbsent(nextTemplate.getTemplateType(), t -> new HashMap<>());
			Map<String, NarrativeTemplate> nameToTemplate = styleToNameToTemplate.computeIfAbsent(nextTemplate.getTemplateType(), t -> new HashMap<>());
			nameToTemplate.put(nextTemplate.getTemplateName(), nextTemplate);
			for (String nextResourceType : nextTemplate.getAppliesToResourceTypes()) {
				resourceTypeToTemplate.put(nextResourceType, nextTemplate);
			}
		}

		myStyleToNameToTemplate = makeImmutable(styleToNameToTemplate);
		myStyleToResourceTypeToTemplate = makeImmutable(styleToResourceTypeToTemplate);
	}

	private Collection<NarrativeTemplate> loadProperties(String propFileName) throws IOException {
		ourLog.debug("Loading narrative properties file: {}", propFileName);
		Map<String, NarrativeTemplate> nameToTemplate = new HashMap<>();

		Properties file = new Properties();

		String resource = loadResource(propFileName);
		file.load(new StringReader(resource));
		for (Object nextKeyObj : file.keySet()) {
			String nextKey = (String) nextKeyObj;
			Validate.isTrue(StringUtils.countMatches(nextKey, ".") == 1, "Invalid narrative property file key: %s", nextKey);
			String name = nextKey.substring(0, nextKey.indexOf('.'));
			Validate.notBlank(name, "Invalid narrative property file key: %s", nextKey);

			NarrativeTemplate nextTemplate = nameToTemplate.computeIfAbsent(name, t -> new NarrativeTemplate().setTemplateName(name));

			if (nextKey.endsWith(".profile")) {
				String profile = file.getProperty(nextKey);
				if (isNotBlank(profile)) {
					nextTemplate.addAppliesToProfile(profile);
				}
			} else if (nextKey.endsWith(".resourceType")) {
				String resourceType = file.getProperty(nextKey);
				if (isNotBlank(resourceType)) {
					nextTemplate.addAppliesToResourceType(resourceType);
				}
			} else if (nextKey.endsWith(".class")) {
				String className = file.getProperty(nextKey);
				Class<? extends IBase> clazz;
				try {
					clazz = (Class<? extends IBase>) Class.forName(className);
				} catch (ClassNotFoundException e) {
					ourLog.debug("Unknown datatype class '{}' identified in narrative file {}", name, propFileName);
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

	@Override
	public Optional<INarrativeTemplate> getTemplateByResourceName(TemplateTypeEnum theStyle, String theResourceName) {
		return getFromMap(theStyle, theResourceName, myStyleToResourceTypeToTemplate);
	}

	@Override
	public Optional<INarrativeTemplate> getTemplateByName(TemplateTypeEnum theStyle, String theName) {
		return getFromMap(theStyle, theName, myStyleToNameToTemplate);
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

	private static <T> Optional<INarrativeTemplate> getFromMap(TemplateTypeEnum theStyle, T theResourceName, Map<TemplateTypeEnum, Map<T, NarrativeTemplate>> theMap) {
		NarrativeTemplate retVal = null;
		Map<T, NarrativeTemplate> resourceTypeToTemplate = theMap.get(theStyle);
		if (resourceTypeToTemplate != null) {
			retVal = resourceTypeToTemplate.get(theResourceName);
		}
		return Optional.ofNullable(retVal);
	}

	private static <T> Map<TemplateTypeEnum, Map<T, NarrativeTemplate>> makeImmutable(Map<TemplateTypeEnum, Map<T, NarrativeTemplate>> theStyleToResourceTypeToTemplate) {
		theStyleToResourceTypeToTemplate.replaceAll((key, value) -> Collections.unmodifiableMap(value));
		return Collections.unmodifiableMap(theStyleToResourceTypeToTemplate);
	}

}
