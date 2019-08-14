package org.hl7.fhir.dstu2016may.hapi.validation;

import ca.uhn.fhir.context.FhirContext;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.dstu2016may.model.*;
import org.hl7.fhir.dstu2016may.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu2016may.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.dstu2016may.model.ValueSet.ConceptReferenceComponent;
import org.hl7.fhir.dstu2016may.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.dstu2016may.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class DefaultProfileValidationSupport implements IValidationSupport {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(DefaultProfileValidationSupport.class);

	private Map<String, CodeSystem> myCodeSystems;
	private Map<String, StructureDefinition> myStructureDefinitions;
	private Map<String, ValueSet> myValueSets;

	@Override
	public ValueSetExpansionComponent expandValueSet(FhirContext theContext, ConceptSetComponent theInclude) {
		ValueSetExpansionComponent retVal = new ValueSetExpansionComponent();

		Set<String> wantCodes = new HashSet<String>();
		for (ConceptReferenceComponent next : theInclude.getConcept()) {
			wantCodes.add(next.getCode());
		}

		CodeSystem system = fetchCodeSystem(theContext, theInclude.getSystem());
		for (ConceptDefinitionComponent next : system.getConcept()) {
			if (wantCodes.isEmpty() || wantCodes.contains(next.getCode())) {
				retVal.addContains().setSystem(theInclude.getSystem()).setCode(next.getCode()).setDisplay(next.getDisplay());
			}
		}

		return retVal;
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
	public List<StructureDefinition> fetchAllStructureDefinitions(FhirContext theContext) {
		return new ArrayList<StructureDefinition>(provideStructureDefinitionMap(theContext).values());
	}

	@Override
	public CodeSystem fetchCodeSystem(FhirContext theContext, String theSystem) {
		return (CodeSystem) fetchCodeSystemOrValueSet(theContext, theSystem, true);
	}

	private DomainResource fetchCodeSystemOrValueSet(FhirContext theContext, String theSystem, boolean codeSystem) {
		Map<String, CodeSystem> codeSystems = myCodeSystems;
		Map<String, ValueSet> valueSets = myValueSets;
		if (codeSystems == null) {
			codeSystems = new HashMap<>();
			valueSets = new HashMap<>();

			loadCodeSystems(theContext, codeSystems, valueSets, "/org/hl7/fhir/dstu2016may/model/valueset/valuesets.xml");
			loadCodeSystems(theContext, codeSystems, valueSets, "/org/hl7/fhir/dstu2016may/model/valueset/v2-tables.xml");
			loadCodeSystems(theContext, codeSystems, valueSets, "/org/hl7/fhir/dstu2016may/model/valueset/v3-codesystems.xml");

			myCodeSystems = codeSystems;
			myValueSets = valueSets;
		}

		if (codeSystem) {
			return codeSystems.get(theSystem);
		} else {
			return valueSets.get(theSystem);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends IBaseResource> T fetchResource(FhirContext theContext, Class<T> theClass, String theUri) {
		Validate.notBlank(theUri, "theUri must not be null or blank");

		if (theUri.startsWith("http://hl7.org/fhir/StructureDefinition/")) {
			return (T) fetchStructureDefinition(theContext, theUri);
		}
		if (theUri.startsWith("http://hl7.org/fhir/ValueSet/")) {
			return (T) fetchValueSet(theContext, theUri);
		}
		//		if (theUri.startsWith("http://hl7.org/fhir/ValueSet/")) {
		//			Map<String, ValueSet> defaultValueSets = myDefaultValueSets;
		//			if (defaultValueSets == null) {
		//				String path = theContext.getVersion().getPathToSchemaDefinitions().replace("/schema", "/valueset") + "/valuesets.xml";
		//				InputStream valuesetText = DefaultProfileValidationSupport.class.getResourceAsStream(path);
		//				if (valuesetText == null) {
		//					return null;
		//				}
		//				InputStreamReader reader;
		//				try {
		//					reader = new InputStreamReader(valuesetText, "UTF-8");
		//				} catch (UnsupportedEncodingException e) {
		//					// Shouldn't happen!
		//					throw new InternalErrorException("UTF-8 encoding not supported on this platform", e);
		//				}
		//
		//				defaultValueSets = new HashMap<String, ValueSet>();
		//
		//				Bundle bundle = theContext.newXmlParser().parseResource(Bundle.class, reader);
		//				for (BundleEntryComponent next : bundle.getEntry()) {
		//					IdType nextId = new IdType(next.getFullUrl());
		//					if (nextId.isEmpty() || !nextId.getValue().startsWith("http://hl7.org/fhir/ValueSet/")) {
		//						continue;
		//					}
		//					defaultValueSets.put(nextId.toVersionless().getValue(), (ValueSet) next.getResource());
		//				}
		//
		//				myDefaultValueSets = defaultValueSets;
		//			}
		//
		//			return (T) defaultValueSets.get(theUri);
		//		}

		return null;
	}

	@Override
	public StructureDefinition fetchStructureDefinition(FhirContext theContext, String theUrl) {
		return provideStructureDefinitionMap(theContext).get(theUrl);
	}

	@Override
	public ValueSet fetchValueSet(FhirContext theContext, String uri) {
		return (ValueSet) fetchCodeSystemOrValueSet(theContext, uri, false);
	}

	public void flush() {
		myCodeSystems = null;
		myStructureDefinitions = null;
	}

	@Override
	public boolean isCodeSystemSupported(FhirContext theContext, String theSystem) {
		CodeSystem cs = fetchCodeSystem(theContext, theSystem);
		return cs != null;
	}

	private void loadCodeSystems(FhirContext theContext, Map<String, CodeSystem> theCodeSystems, Map<String, ValueSet> theValueSets, String theClasspath) {
		ourLog.info("Loading CodeSystem/ValueSet from classpath: {}", theClasspath);
		InputStream inputStream = DefaultProfileValidationSupport.class.getResourceAsStream(theClasspath);
		InputStreamReader reader = null;
		if (inputStream != null) {
			try {
				reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

				Bundle bundle = theContext.newXmlParser().parseResource(Bundle.class, reader);
				for (BundleEntryComponent next : bundle.getEntry()) {
					if (next.getResource() instanceof CodeSystem) {
						CodeSystem nextValueSet = (CodeSystem) next.getResource();
						nextValueSet.getText().setDivAsString("");
						String system = nextValueSet.getUrl();
						if (isNotBlank(system)) {
							theCodeSystems.put(system, nextValueSet);
						}
					} else if (next.getResource() instanceof ValueSet) {
						ValueSet nextValueSet = (ValueSet) next.getResource();
						nextValueSet.getText().setDivAsString("");
						String system = nextValueSet.getUrl();
						if (isNotBlank(system)) {
							theValueSets.put(system, nextValueSet);
						}
					}
				}
			} finally {
				IOUtils.closeQuietly(reader);
				IOUtils.closeQuietly(inputStream);
			}
		} else {
			ourLog.warn("Unable to load resource: {}", theClasspath);
		}
	}

	private void loadStructureDefinitions(FhirContext theContext, Map<String, StructureDefinition> theCodeSystems, String theClasspath) {
		ourLog.info("Loading structure definitions from classpath: {}", theClasspath);
		InputStream valuesetText = DefaultProfileValidationSupport.class.getResourceAsStream(theClasspath);
		if (valuesetText != null) {
			InputStreamReader reader = new InputStreamReader(valuesetText, StandardCharsets.UTF_8);

			Bundle bundle = theContext.newXmlParser().parseResource(Bundle.class, reader);
			for (BundleEntryComponent next : bundle.getEntry()) {
				if (next.getResource() instanceof StructureDefinition) {
					StructureDefinition nextSd = (StructureDefinition) next.getResource();
					nextSd.getText().setDivAsString("");
					String system = nextSd.getUrl();
					if (isNotBlank(system)) {
						theCodeSystems.put(system, nextSd);
					}
				}
			}
		} else {
			ourLog.warn("Unable to load resource: {}", theClasspath);
		}
	}

	private Map<String, StructureDefinition> provideStructureDefinitionMap(FhirContext theContext) {
		Map<String, StructureDefinition> structureDefinitions = myStructureDefinitions;
		if (structureDefinitions == null) {
			structureDefinitions = new HashMap<>();

			loadStructureDefinitions(theContext, structureDefinitions, "/org/hl7/fhir/dstu2016may/model/profile/profiles-resources.xml");
			loadStructureDefinitions(theContext, structureDefinitions, "/org/hl7/fhir/dstu2016may/model/profile/profiles-types.xml");
			loadStructureDefinitions(theContext, structureDefinitions, "/org/hl7/fhir/dstu2016may/model/profile/profiles-others.xml");

			myStructureDefinitions = structureDefinitions;
		}
		return structureDefinitions;
	}

	private CodeValidationResult testIfConceptIsInList(String theCode, List<ConceptDefinitionComponent> conceptList, boolean theCaseSensitive) {
		String code = theCode;
		if (theCaseSensitive == false) {
			code = code.toUpperCase();
		}

		return testIfConceptIsInListInner(conceptList, theCaseSensitive, code);
	}

	private CodeValidationResult testIfConceptIsInListInner(List<ConceptDefinitionComponent> conceptList, boolean theCaseSensitive, String code) {
		CodeValidationResult retVal = null;
		for (ConceptDefinitionComponent next : conceptList) {
			String nextCandidate = next.getCode();
			if (theCaseSensitive == false) {
				nextCandidate = nextCandidate.toUpperCase();
			}
			if (nextCandidate.equals(code)) {
				retVal = new CodeValidationResult(next);
				break;
			}

			// recurse
			retVal = testIfConceptIsInList(code, next.getConcept(), theCaseSensitive);
			if (retVal != null) {
				break;
			}
		}

		return retVal;
	}

	@Override
	public CodeValidationResult validateCode(FhirContext theContext, String theCodeSystem, String theCode, String theDisplay) {
		CodeSystem cs = fetchCodeSystem(theContext, theCodeSystem);
		if (cs != null) {
			boolean caseSensitive = true;
			if (cs.hasCaseSensitive()) {
				caseSensitive = cs.getCaseSensitive();
			}

			CodeValidationResult retVal = testIfConceptIsInList(theCode, cs.getConcept(), caseSensitive);

			if (retVal != null) {
				return retVal;
			}
		}

		return new CodeValidationResult(OperationOutcome.IssueSeverity.WARNING, "Unknown code: " + theCodeSystem + " / " + theCode);
	}

	@Override
	public LookupCodeResult lookupCode(FhirContext theContext, String theSystem, String theCode) {
		return validateCode(theContext, theSystem, theCode, null).asLookupCodeResult(theSystem, theCode);
	}

}
