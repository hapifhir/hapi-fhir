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
package ca.uhn.fhir.context.support;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.parser.LenientErrorHandler;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.ClasspathUtil;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

class DefaultProfileValidationSupportBundleStrategy implements IValidationSupport {

	private static final String URL_PREFIX_STRUCTURE_DEFINITION = "http://hl7.org/fhir/StructureDefinition/";
	private static final String URL_PREFIX_STRUCTURE_DEFINITION_BASE = "http://hl7.org/fhir/";
	private static final org.slf4j.Logger ourLog =
			org.slf4j.LoggerFactory.getLogger(DefaultProfileValidationSupport.class);
	private final FhirContext myCtx;

	private Map<String, IBaseResource> myCodeSystems;
	private Map<String, IBaseResource> myStructureDefinitions;
	private Map<String, IBaseResource> myValueSets;
	private List<String> myTerminologyResources;
	private List<String> myStructureDefinitionResources;

	/**
	 * Constructor
	 *
	 * @param theFhirContext The context to use
	 */
	DefaultProfileValidationSupportBundleStrategy(FhirContext theFhirContext) {
		myCtx = theFhirContext;
	}

	private void initializeResourceLists() {

		if (myTerminologyResources != null && myStructureDefinitionResources != null) {
			return;
		}

		List<String> terminologyResources = new ArrayList<>();
		List<String> structureDefinitionResources = new ArrayList<>();
		switch (getFhirContext().getVersion().getVersion()) {
			case DSTU2:
			case DSTU2_HL7ORG:
				terminologyResources.add("/org/hl7/fhir/instance/model/valueset/valuesets.xml");
				terminologyResources.add("/org/hl7/fhir/instance/model/valueset/v2-tables.xml");
				terminologyResources.add("/org/hl7/fhir/instance/model/valueset/v3-codesystems.xml");
				Properties profileNameProperties = new Properties();
				try {
					profileNameProperties.load(DefaultProfileValidationSupport.class.getResourceAsStream(
							"/org/hl7/fhir/instance/model/profile/profiles.properties"));
					for (Object nextKey : profileNameProperties.keySet()) {
						structureDefinitionResources.add("/org/hl7/fhir/instance/model/profile/" + nextKey);
					}
				} catch (IOException e) {
					throw new ConfigurationException(Msg.code(1740) + e);
				}
				break;
			case DSTU2_1:
				terminologyResources.add("/org/hl7/fhir/dstu2016may/model/valueset/valuesets.xml");
				terminologyResources.add("/org/hl7/fhir/dstu2016may/model/valueset/v2-tables.xml");
				terminologyResources.add("/org/hl7/fhir/dstu2016may/model/valueset/v3-codesystems.xml");
				structureDefinitionResources.add("/org/hl7/fhir/dstu2016may/model/profile/profiles-resources.xml");
				structureDefinitionResources.add("/org/hl7/fhir/dstu2016may/model/profile/profiles-types.xml");
				structureDefinitionResources.add("/org/hl7/fhir/dstu2016may/model/profile/profiles-others.xml");
				break;
			case DSTU3:
				terminologyResources.add("/org/hl7/fhir/dstu3/model/valueset/valuesets.xml");
				terminologyResources.add("/org/hl7/fhir/dstu3/model/valueset/v2-tables.xml");
				terminologyResources.add("/org/hl7/fhir/dstu3/model/valueset/v3-codesystems.xml");
				structureDefinitionResources.add("/org/hl7/fhir/dstu3/model/profile/profiles-resources.xml");
				structureDefinitionResources.add("/org/hl7/fhir/dstu3/model/profile/profiles-types.xml");
				structureDefinitionResources.add("/org/hl7/fhir/dstu3/model/profile/profiles-others.xml");
				structureDefinitionResources.add("/org/hl7/fhir/dstu3/model/extension/extension-definitions.xml");
				break;
			case R4:
				terminologyResources.add("/org/hl7/fhir/r4/model/valueset/valuesets.xml");
				terminologyResources.add("/org/hl7/fhir/r4/model/valueset/v2-tables.xml");
				terminologyResources.add("/org/hl7/fhir/r4/model/valueset/v3-codesystems.xml");
				structureDefinitionResources.add("/org/hl7/fhir/r4/model/profile/profiles-resources.xml");
				structureDefinitionResources.add("/org/hl7/fhir/r4/model/profile/profiles-types.xml");
				structureDefinitionResources.add("/org/hl7/fhir/r4/model/profile/profiles-others.xml");
				structureDefinitionResources.add("/org/hl7/fhir/r4/model/extension/extension-definitions.xml");
				break;
			case R4B:
				terminologyResources.add("/org/hl7/fhir/r4b/model/valueset/valuesets.xml");
				// For R4B we can re-use the same v2 and v3 files as R4, as these will not be updated and it will reduce
				// duplication.
				terminologyResources.add("/org/hl7/fhir/r4/model/valueset/v2-tables.xml");
				terminologyResources.add("/org/hl7/fhir/r4/model/valueset/v3-codesystems.xml");
				structureDefinitionResources.add("/org/hl7/fhir/r4b/model/profile/profiles-resources.xml");
				structureDefinitionResources.add("/org/hl7/fhir/r4b/model/profile/profiles-types.xml");
				structureDefinitionResources.add("/org/hl7/fhir/r4b/model/profile/profiles-others.xml");
				structureDefinitionResources.add("/org/hl7/fhir/r4b/model/extension/extension-definitions.xml");
				break;
			case R5:
				structureDefinitionResources.add("/org/hl7/fhir/r5/model/profile/profiles-resources.xml");
				structureDefinitionResources.add("/org/hl7/fhir/r5/model/profile/profiles-types.xml");
				structureDefinitionResources.add("/org/hl7/fhir/r5/model/profile/profiles-others.xml");
				structureDefinitionResources.add("/org/hl7/fhir/r5/model/extension/extension-definitions.xml");
				terminologyResources.add("/org/hl7/fhir/r5/model/valueset/valuesets.xml");
				terminologyResources.add("/org/hl7/fhir/r5/model/valueset/v2-tables.xml");
				terminologyResources.add("/org/hl7/fhir/r5/model/valueset/v3-codesystems.xml");
				break;
		}

		myTerminologyResources = terminologyResources;
		myStructureDefinitionResources = structureDefinitionResources;
	}

	@Override
	public List<IBaseResource> fetchAllConformanceResources() {
		ArrayList<IBaseResource> retVal = new ArrayList<>();
		retVal.addAll(myCodeSystems.values());
		retVal.addAll(myStructureDefinitions.values());
		retVal.addAll(myValueSets.values());
		return retVal;
	}

	@Override
	public <T extends IBaseResource> List<T> fetchAllStructureDefinitions() {
		return toList(provideStructureDefinitionMap());
	}

	@Nullable
	@Override
	public <T extends IBaseResource> List<T> fetchAllNonBaseStructureDefinitions() {
		return null;
	}

	@Override
	public IBaseResource fetchCodeSystem(String theSystem) {
		return fetchCodeSystemOrValueSet(theSystem, true);
	}

	private IBaseResource fetchCodeSystemOrValueSet(String theSystem, boolean codeSystem) {
		synchronized (this) {
			Map<String, IBaseResource> codeSystems = myCodeSystems;
			Map<String, IBaseResource> valueSets = myValueSets;
			if (codeSystems == null || valueSets == null) {
				codeSystems = new HashMap<>();
				valueSets = new HashMap<>();

				initializeResourceLists();
				for (String next : myTerminologyResources) {
					loadCodeSystems(codeSystems, valueSets, next);
				}

				myCodeSystems = codeSystems;
				myValueSets = valueSets;
			}

			// System can take the form "http://url|version"
			String system = theSystem;
			String version = null;
			int pipeIdx = system.indexOf('|');
			if (pipeIdx > 0) {
				version = system.substring(pipeIdx + 1);
				system = system.substring(0, pipeIdx);
			}

			IBaseResource candidate;
			if (codeSystem) {
				candidate = codeSystems.get(system);
			} else {
				candidate = valueSets.get(system);
			}

			if (candidate != null
					&& isNotBlank(version)
					&& !system.startsWith("http://hl7.org")
					&& !system.startsWith("http://terminology.hl7.org")) {
				if (!StringUtils.equals(
						version, myCtx.newTerser().getSinglePrimitiveValueOrNull(candidate, "version"))) {
					candidate = null;
				}
			}

			return candidate;
		}
	}

	@Override
	public IBaseResource fetchStructureDefinition(String theUrl) {
		String url = theUrl;
		if (!url.startsWith(URL_PREFIX_STRUCTURE_DEFINITION)) {
			if (url.indexOf('/') == -1) {
				url = URL_PREFIX_STRUCTURE_DEFINITION + url;
			} else if (StringUtils.countMatches(url, '/') == 1) {
				url = URL_PREFIX_STRUCTURE_DEFINITION_BASE + url;
			}
		}
		Map<String, IBaseResource> structureDefinitionMap = provideStructureDefinitionMap();
		IBaseResource retVal = structureDefinitionMap.get(url);
		if (retVal == null) {

			if (url.startsWith(URL_PREFIX_STRUCTURE_DEFINITION)) {

				/*
				 * A few built-in R4 SearchParameters have the wrong casing for primitive
				 * search parameters eg "value.as(String)" when it should be
				 * "value.as(string)". This lets us be a bit lenient about this.
				 */
				if (myCtx.getVersion().getVersion() == FhirVersionEnum.R4
						|| myCtx.getVersion().getVersion() == FhirVersionEnum.R4B
						|| myCtx.getVersion().getVersion() == FhirVersionEnum.R5) {
					String end = url.substring(URL_PREFIX_STRUCTURE_DEFINITION.length());
					if (Character.isUpperCase(end.charAt(0))) {
						String newEnd = Character.toLowerCase(end.charAt(0)) + end.substring(1);
						String alternateUrl = URL_PREFIX_STRUCTURE_DEFINITION + newEnd;
						retVal = structureDefinitionMap.get(alternateUrl);
						if (retVal != null) {
							retVal = myCtx.newTerser().clone(retVal);
							myCtx.newTerser().setElement(retVal, "type", end);
						}
					}
				}
			}
		}
		return retVal;
	}

	@Override
	public IBaseResource fetchValueSet(String theUrl) {
		IBaseResource retVal = fetchCodeSystemOrValueSet(theUrl, false);
		return retVal;
	}

	public void flush() {
		myCodeSystems = null;
		myStructureDefinitions = null;
	}

	@Override
	public FhirContext getFhirContext() {
		return myCtx;
	}

	private Map<String, IBaseResource> provideStructureDefinitionMap() {
		Map<String, IBaseResource> structureDefinitions = myStructureDefinitions;
		if (structureDefinitions == null) {
			structureDefinitions = new HashMap<>();

			initializeResourceLists();
			for (String next : myStructureDefinitionResources) {
				loadStructureDefinitions(structureDefinitions, next);
			}

			myStructureDefinitions = structureDefinitions;
		}
		return structureDefinitions;
	}

	private void loadCodeSystems(
			Map<String, IBaseResource> theCodeSystems, Map<String, IBaseResource> theValueSets, String theClasspath) {
		ourLog.info("Loading CodeSystem/ValueSet from classpath: {}", theClasspath);
		InputStream inputStream = DefaultProfileValidationSupport.class.getResourceAsStream(theClasspath);
		InputStreamReader reader = null;
		if (inputStream != null) {
			try {
				reader = new InputStreamReader(inputStream, Constants.CHARSET_UTF8);
				List<IBaseResource> resources = parseBundle(reader);
				for (IBaseResource next : resources) {

					RuntimeResourceDefinition nextDef = getFhirContext().getResourceDefinition(next);
					Map<String, IBaseResource> map = null;
					switch (nextDef.getName()) {
						case "CodeSystem":
							map = theCodeSystems;
							break;
						case "ValueSet":
							map = theValueSets;
							break;
					}

					if (map != null) {
						String urlValueString = getConformanceResourceUrl(next);
						if (isNotBlank(urlValueString)) {
							map.put(urlValueString, next);
						}

						switch (myCtx.getVersion().getVersion()) {
							case DSTU2:
							case DSTU2_HL7ORG:
								IPrimitiveType<?> codeSystem = myCtx.newTerser()
										.getSingleValueOrNull(next, "ValueSet.codeSystem.system", IPrimitiveType.class);
								if (codeSystem != null && isNotBlank(codeSystem.getValueAsString())) {
									theCodeSystems.put(codeSystem.getValueAsString(), next);
								}

								break;

							default:
							case DSTU2_1:
							case DSTU3:
							case R4:
							case R4B:
							case R5:
								break;
						}
					}
				}
			} finally {
				try {
					if (reader != null) {
						reader.close();
					}
					inputStream.close();
				} catch (IOException e) {
					ourLog.warn("Failure closing stream", e);
				}
			}
		} else {
			ourLog.warn("Unable to load resource: {}", theClasspath);
		}

		// Load built-in system

		if (myCtx.getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.DSTU3)) {
			String storageCodeEnum =
					ClasspathUtil.loadResource("ca/uhn/fhir/context/support/HapiFhirStorageResponseCode.json");
			IBaseResource storageCodeCodeSystem = myCtx.newJsonParser()
					.setParserErrorHandler(new LenientErrorHandler())
					.parseResource(storageCodeEnum);
			String url = myCtx.newTerser().getSinglePrimitiveValueOrNull(storageCodeCodeSystem, "url");
			theCodeSystems.put(url, storageCodeCodeSystem);
		}
	}

	private void loadStructureDefinitions(Map<String, IBaseResource> theCodeSystems, String theClasspath) {
		ourLog.info("Loading structure definitions from classpath: {}", theClasspath);

		String packageUserData = null;
		if (myCtx.getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.DSTU3)) {
			packageUserData = "hl7.fhir."
					+ myCtx.getVersion()
							.getVersion()
							.name()
							.replace("DSTU", "R")
							.toLowerCase(Locale.US);
		}

		try (InputStream valueSetText = DefaultProfileValidationSupport.class.getResourceAsStream(theClasspath)) {
			if (valueSetText != null) {
				try (InputStreamReader reader = new InputStreamReader(valueSetText, Constants.CHARSET_UTF8)) {

					List<IBaseResource> resources = parseBundle(reader);
					for (IBaseResource next : resources) {

						String nextType = getFhirContext().getResourceType(next);
						if ("StructureDefinition".equals(nextType)) {

							String url = getConformanceResourceUrl(next);
							if (isNotBlank(url)) {
								theCodeSystems.put(url, next);
							}
						}

						// This is used by the validator to determine which package a given SD came from.
						// I don't love this use of magic strings but that's what is expected currently
						if (packageUserData != null) {
							next.setUserData("package", packageUserData);
						}
					}
				}
			} else {
				ourLog.warn("Unable to load resource: {}", theClasspath);
			}
		} catch (IOException theE) {
			ourLog.warn("Unable to load resource: {}", theClasspath);
		}
	}

	private String getConformanceResourceUrl(IBaseResource theResource) {
		return DefaultProfileValidationSupport.getConformanceResourceUrl(getFhirContext(), theResource);
	}

	private List<IBaseResource> parseBundle(InputStreamReader theReader) {
		IBaseResource parsedObject = getFhirContext().newXmlParser().parseResource(theReader);
		if (parsedObject instanceof IBaseBundle) {
			IBaseBundle bundle = (IBaseBundle) parsedObject;
			return BundleUtil.toListOfResources(getFhirContext(), bundle);
		} else {
			return Collections.singletonList(parsedObject);
		}
	}

	static <T extends IBaseResource> List<T> toList(Map<String, IBaseResource> theMap) {
		ArrayList<IBaseResource> retVal = new ArrayList<>(theMap.values());
		return (List<T>) Collections.unmodifiableList(retVal);
	}
}
