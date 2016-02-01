package org.hl7.fhir.dstu3.hapi.validation;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.OperationOutcome.IssueSeverity;
import org.hl7.fhir.dstu3.model.ValueSet.ConceptDefinitionComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ConceptReferenceComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

public class DefaultProfileValidationSupport implements IValidationSupport {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(DefaultProfileValidationSupport.class);
	
	private Map<String, ValueSet> myCodeSystems;
	private Map<String, ValueSet> myDefaultValueSets;

	@Override
	public ValueSetExpansionComponent expandValueSet(FhirContext theContext, ConceptSetComponent theInclude) {
		ValueSetExpansionComponent retVal = new ValueSetExpansionComponent();
		
		Set<String> wantCodes = new HashSet<String>();
		for (ConceptReferenceComponent next : theInclude.getConcept()) {
			wantCodes.add(next.getCode());
		}
		
		ValueSet system = fetchCodeSystem(theContext, theInclude.getSystem());
		for (ConceptDefinitionComponent next : system.getCodeSystem().getConcept()) {
			if (wantCodes.isEmpty() || wantCodes.contains(next.getCode())) {
				retVal.addContains().setSystem(theInclude.getSystem()).setCode(next.getCode()).setDisplay(next.getDisplay());
			}
		}
		
		return retVal;
	}

	@Override
	public ValueSet fetchCodeSystem(FhirContext theContext, String theSystem) {
		Map<String, ValueSet> codeSystems = myCodeSystems;
		if (codeSystems == null) {
			codeSystems = new HashMap<String, ValueSet>();

			loadCodeSystems(theContext, codeSystems, "/org/hl7/fhir/instance/model/dstu3/valueset/valuesets.xml");
			loadCodeSystems(theContext, codeSystems, "/org/hl7/fhir/instance/model/dstu3/valueset/v2-tables.xml");
			loadCodeSystems(theContext, codeSystems, "/org/hl7/fhir/instance/model/dstu3/valueset/v3-codesystems.xml");

			myCodeSystems = codeSystems;
		}

		return codeSystems.get(theSystem);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends IBaseResource> T fetchResource(FhirContext theContext, Class<T> theClass, String theUri) {
		if (theUri.startsWith("http://hl7.org/fhir/StructureDefinition/")) {
			return (T) FhirInstanceValidator.loadProfileOrReturnNull(null, theContext, theUri.substring("http://hl7.org/fhir/StructureDefinition/".length()));
		}
		if (theUri.startsWith("http://hl7.org/fhir/ValueSet/")) {
			Map<String, ValueSet> defaultValueSets = myDefaultValueSets;
			if (defaultValueSets == null) {
				String path = theContext.getVersion().getPathToSchemaDefinitions().replace("/schema", "/valueset") + "/valuesets.xml";
				InputStream valuesetText = DefaultProfileValidationSupport.class.getResourceAsStream(path);
				if (valuesetText == null) {
					return null;
				}
				InputStreamReader reader;
				try {
					reader = new InputStreamReader(valuesetText, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// Shouldn't happen!
					throw new InternalErrorException("UTF-8 encoding not supported on this platform", e);
				}

				defaultValueSets = new HashMap<String, ValueSet>();

				Bundle bundle = theContext.newXmlParser().parseResource(Bundle.class, reader);
				for (BundleEntryComponent next : bundle.getEntry()) {
					IdType nextId = new IdType(next.getFullUrl());
					if (nextId.isEmpty() || !nextId.getValue().startsWith("http://hl7.org/fhir/ValueSet/")) {
						continue;
					}
					defaultValueSets.put(nextId.toVersionless().getValue(), (ValueSet) next.getResource());
				}

				myDefaultValueSets = defaultValueSets;
			}

			return (T) defaultValueSets.get(theUri);
		}

		return null;
	}

	@Override
	public boolean isCodeSystemSupported(FhirContext theContext, String theSystem) {
		ValueSet cs = fetchCodeSystem(theContext, theSystem);
		return cs != null;
	}

	private void loadCodeSystems(FhirContext theContext, Map<String, ValueSet> theCodeSystems, String theClasspath) {
		ourLog.info("Loading code systems from classpath: {}", theClasspath);
		InputStream valuesetText = DefaultProfileValidationSupport.class.getResourceAsStream(theClasspath);
		if (valuesetText != null) {
			InputStreamReader reader;
			try {
				reader = new InputStreamReader(valuesetText, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// Shouldn't happen!
				throw new InternalErrorException("UTF-8 encoding not supported on this platform", e);
			}

			Bundle bundle = theContext.newXmlParser().parseResource(Bundle.class, reader);
			for (BundleEntryComponent next : bundle.getEntry()) {
				ValueSet nextValueSet = (ValueSet) next.getResource();
				nextValueSet.getText().setDivAsString("");
				String system = nextValueSet.getCodeSystem().getSystem();
				if (isNotBlank(system)) {
					theCodeSystems.put(system, nextValueSet);
				}
			}
		} else {
			ourLog.warn("Unable to load resource: {}", theClasspath);
		}
	}

	@Override
	public CodeValidationResult validateCode(FhirContext theContext, String theCodeSystem, String theCode, String theDisplay) {
		ValueSet cs = fetchCodeSystem(theContext, theCodeSystem);
		if (cs != null) {
			for (ConceptDefinitionComponent next : cs.getCodeSystem().getConcept()) {
				if (next.getCode().equals(theCode)) {
					return new CodeValidationResult(next);
				}
			}
		}
		
		return new CodeValidationResult(IssueSeverity.INFORMATION, "Unknown code: " + theCodeSystem + " / " + theCode);
	}

	public void flush() {
		myCodeSystems = null;
		myDefaultValueSets = null;
	}

}
