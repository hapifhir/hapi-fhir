package org.hl7.fhir.common.hapi.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.support.IContextValidationSupport;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.util.BundleUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class DefaultProfileValidationSupport extends BaseStaticResourceValidationSupport implements IContextValidationSupport {

	private static final String URL_PREFIX_STRUCTURE_DEFINITION = "http://hl7.org/fhir/StructureDefinition/";
	private static final String URL_PREFIX_STRUCTURE_DEFINITION_BASE = "http://hl7.org/fhir/";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(DefaultProfileValidationSupport.class);

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
	public DefaultProfileValidationSupport(FhirContext theFhirContext) {
		Validate.notNull(theFhirContext, "theFhirContext must not be null");
		myCtx = theFhirContext;
	}


	private void initializeResourceLists(FhirContext theContext) {

		if (myTerminologyResources != null && myStructureDefinitionResources != null) {
			return;
		}

		List<String> terminologyResources = new ArrayList<>();
		List<String> structureDefinitionResources = new ArrayList<>();
		switch (theContext.getVersion().getVersion()) {
			case DSTU2:
			case DSTU2_HL7ORG:
				terminologyResources.add("/org/hl7/fhir/instance/model/valueset/valuesets.xml");
				terminologyResources.add("/org/hl7/fhir/instance/model/valueset/v2-tables.xml");
				terminologyResources.add("/org/hl7/fhir/instance/model/valueset/v3-codesystems.xml");
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
	public List<IBaseResource> fetchAllConformanceResources(FhirContext theContext) {
		ArrayList<IBaseResource> retVal = new ArrayList<>();
		retVal.addAll(myCodeSystems.values());
		retVal.addAll(myStructureDefinitions.values());
		retVal.addAll(myValueSets.values());
		return retVal;
	}

	@Override
	public <T extends IBaseResource> List<T> fetchAllStructureDefinitions(FhirContext theContext, Class<T> theStructureDefinitionType) {
		return toList(provideStructureDefinitionMap(theContext), theStructureDefinitionType);
	}


	@Override
	public <T extends IBaseResource> T fetchCodeSystem(FhirContext theContext, String theSystem, Class<T> theCodeSystemType) {
		IBaseResource retVal = fetchCodeSystemOrValueSet(theContext, theSystem, true);
		return theCodeSystemType.cast(retVal);
	}

	private IBaseResource fetchCodeSystemOrValueSet(FhirContext theContext, String theSystem, boolean codeSystem) {
		synchronized (this) {
			Map<String, IBaseResource> codeSystems = myCodeSystems;
			Map<String, IBaseResource> valueSets = myValueSets;
			if (codeSystems == null || valueSets == null) {
				codeSystems = new HashMap<>();
				valueSets = new HashMap<>();

				initializeResourceLists(theContext);
				for (String next : myTerminologyResources) {
					loadCodeSystems(theContext, codeSystems, valueSets, next);
				}

				myCodeSystems = codeSystems;
				myValueSets = valueSets;
			}

			// System can take the form "http://url|version"
			String system = theSystem;
			if (system.contains("|")) {
				String version = system.substring(system.indexOf('|') + 1);
				if (version.matches("^[0-9.]+$")) {
					system = system.substring(0, system.indexOf('|'));
				}
			}

			if (codeSystem) {
				return codeSystems.get(system);
			} else {
				return valueSets.get(system);
			}
		}
	}

	@Override
	public IBaseResource fetchStructureDefinition(FhirContext theContext, String theUrl) {
		String url = theUrl;
		if (url.startsWith(URL_PREFIX_STRUCTURE_DEFINITION)) {
			// no change
		} else if (url.indexOf('/') == -1) {
			url = URL_PREFIX_STRUCTURE_DEFINITION + url;
		} else if (StringUtils.countMatches(url, '/') == 1) {
			url = URL_PREFIX_STRUCTURE_DEFINITION_BASE + url;
		}
		return provideStructureDefinitionMap(theContext).get(url);
	}

	@Override
	public IBaseResource fetchValueSet(FhirContext theContext, String uri) {
		return fetchCodeSystemOrValueSet(theContext, uri, false);
	}

	public void flush() {
		myCodeSystems = null;
		myStructureDefinitions = null;
	}

	@Override
	public boolean isCodeSystemSupported(FhirContext theContext, String theSystem) {
		if (isBlank(theSystem) || Constants.codeSystemNotNeeded(theSystem)) {
			return false;
		}

		RuntimeResourceDefinition codeSystem = theContext.getResourceDefinition("CodeSystem");
		Class<? extends IBaseResource> codeSystemType = codeSystem.getImplementingClass();
		IBaseResource cs = fetchCodeSystem(theContext, theSystem, codeSystemType);
		if (cs != null) {
			IPrimitiveType<?> content = theContext.newTerser().getSingleValueOrNull(cs, "content", IPrimitiveType.class);
			if (!"not-present".equals(content.getValueAsString())) {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean isValueSetSupported(FhirContext theContext, String theValueSetUrl) {
		return isNotBlank(theValueSetUrl) && fetchValueSet(theContext, theValueSetUrl) != null;
	}

	private Map<String, IBaseResource> provideStructureDefinitionMap(FhirContext theContext) {
		if (theContext.getVersion().getVersion() != myCtx.getVersion().getVersion()) {
			assert theContext.getVersion().getVersion() == myCtx.getVersion().getVersion() : "Support created for " + myCtx.getVersion().getVersion() + " but requested version: " + theContext.getVersion().getVersion();
		}
		assert theContext.getVersion().getVersion() == myCtx.getVersion().getVersion() : "Support created for " + myCtx.getVersion().getVersion() + " but requested version: " + theContext.getVersion().getVersion();

		Map<String, IBaseResource> structureDefinitions = myStructureDefinitions;
		if (structureDefinitions == null) {
			structureDefinitions = new HashMap<>();

			initializeResourceLists(theContext);
			for (String next : myStructureDefinitionResources) {
				loadStructureDefinitions(theContext, structureDefinitions, next);
			}

			myStructureDefinitions = structureDefinitions;
		}
		return structureDefinitions;
	}

	private static void loadCodeSystems(FhirContext theContext, Map<String, IBaseResource> theCodeSystems, Map<String, IBaseResource> theValueSets, String theClasspath) {
		ourLog.info("Loading CodeSystem/ValueSet from classpath: {}", theClasspath);
		InputStream inputStream = DefaultProfileValidationSupport.class.getResourceAsStream(theClasspath);
		InputStreamReader reader = null;
		if (inputStream != null) {
			try {
				reader = new InputStreamReader(inputStream, Constants.CHARSET_UTF8);
				List<IBaseResource> resources = parseBundle(theContext, reader);
				for (IBaseResource next : resources) {

					RuntimeResourceDefinition nextDef = theContext.getResourceDefinition(next);
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
						String urlValueString = getConformanceResourceUrl(theContext, next);
						if (isNotBlank(urlValueString)) {
							map.put(urlValueString, next);
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
	}

	private static void loadStructureDefinitions(FhirContext theContext, Map<String, IBaseResource> theCodeSystems, String theClasspath) {
		ourLog.info("Loading structure definitions from classpath: {}", theClasspath);
		try (InputStream valuesetText = DefaultProfileValidationSupport.class.getResourceAsStream(theClasspath)) {
			if (valuesetText != null) {
				try (InputStreamReader reader = new InputStreamReader(valuesetText, Constants.CHARSET_UTF8)) {

					List<IBaseResource> resources = parseBundle(theContext, reader);
					for (IBaseResource next : resources) {

						String nextType = theContext.getResourceDefinition(next).getName();
						if ("StructureDefinition".equals(nextType)) {

							String url = getConformanceResourceUrl(theContext, next);
							if (isNotBlank(url)) {
								theCodeSystems.put(url, next);
							}

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

	private static String getConformanceResourceUrl(FhirContext theContext, IBaseResource next) {
		String urlValueString = null;
		Optional<IBase> urlValue = theContext.getResourceDefinition(next).getChildByName("url").getAccessor().getFirstValueOrNull(next);
		if (urlValue.isPresent()) {
			IPrimitiveType<?> urlValueType = (IPrimitiveType<?>) urlValue.get();
			urlValueString = urlValueType.getValueAsString();
		}
		return urlValueString;
	}

	private static List<IBaseResource> parseBundle(FhirContext theContext, InputStreamReader theReader) {
		Class<? extends IBaseResource> bundleType = theContext.getResourceDefinition("Bundle").getImplementingClass();
		IBaseBundle bundle = (IBaseBundle) theContext.newXmlParser().parseResource(bundleType, theReader);
		return BundleUtil.toListOfResources(theContext, bundle);
	}

}
