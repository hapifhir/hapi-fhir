package org.hl7.fhir.instance.hapi.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.instance.model.*;
import org.hl7.fhir.instance.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.instance.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.instance.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueSeverity;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class DefaultProfileValidationSupport implements IValidationSupport {

	private Map<String, ValueSet> myDefaultValueSets;
	private Map<String, ValueSet> myCodeSystems;
	private static final Set<String> ourResourceNames;
	private static final FhirContext ourHl7OrgCtx;

	static {
		ourHl7OrgCtx = FhirContext.forDstu2Hl7Org();
		ourResourceNames = FhirContext.forDstu2().getResourceNames();
	}

	/**
	 * Constructor
	 */
	public DefaultProfileValidationSupport() {
		super();
	}

	@Override
	public List<StructureDefinition> allStructures() {
		ArrayList<StructureDefinition> retVal = new ArrayList<>();

		for (String next : ourResourceNames) {
			StructureDefinition profile = FhirInstanceValidator.loadProfileOrReturnNull(null, ourHl7OrgCtx, next);
			retVal.add(profile);
		}

		return retVal;
	}

	@Override
	public ValueSetExpansionComponent expandValueSet(FhirContext theContext, ConceptSetComponent theInclude) {
		return null;
	}

	@Override
	public ValueSet fetchCodeSystem(FhirContext theContext, String theSystem) {
		synchronized (this) {
			Map<String, ValueSet> valueSets = myCodeSystems;
			if (valueSets == null) {
				valueSets = new HashMap<>();

				loadValueSets(theContext, valueSets, "/org/hl7/fhir/instance/model/valueset/valuesets.xml");
				loadValueSets(theContext, valueSets, "/org/hl7/fhir/instance/model/valueset/v2-tables.xml");
				loadValueSets(theContext, valueSets, "/org/hl7/fhir/instance/model/valueset/v3-codesystems.xml");

				myCodeSystems = valueSets;
			}

			return valueSets.get(theSystem);
		}
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

				defaultValueSets = new HashMap<>();

				FhirContext ctx = FhirInstanceValidator.getHl7OrgDstu2Ctx(theContext);
				Bundle bundle = ctx.newXmlParser().parseResource(Bundle.class, reader);
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

	public void flush() {
		myDefaultValueSets = null;
		myCodeSystems = null;
	}

	@Override
	public boolean isCodeSystemSupported(FhirContext theContext, String theSystem) {
		return false;
	}

	private void loadValueSets(FhirContext theContext, Map<String, ValueSet> theValueSets, String theFile) {
		InputStream valuesetText = DefaultProfileValidationSupport.class.getResourceAsStream(theFile);
		try {
			if (valuesetText != null) {
				InputStreamReader reader = null;
				try {
					reader = new InputStreamReader(valuesetText, "UTF-8");

					FhirContext ctx = FhirInstanceValidator.getHl7OrgDstu2Ctx(theContext);
					Bundle bundle = ctx.newXmlParser().parseResource(Bundle.class, reader);
					for (BundleEntryComponent next : bundle.getEntry()) {
						ValueSet nextValueSet = (ValueSet) next.getResource();
						String system = nextValueSet.getCodeSystem().getSystem();
						if (isNotBlank(system)) {
							theValueSets.put(system, nextValueSet);
						}
					}

				} catch (UnsupportedEncodingException e) {
					// Shouldn't happen!
					throw new InternalErrorException("UTF-8 encoding not supported on this platform", e);
				} finally {
					IOUtils.closeQuietly(reader);
				}

			}
		} finally {
			IOUtils.closeQuietly(valuesetText);
		}
	}

	@Override
	public CodeValidationResult validateCode(FhirContext theContext, String theCodeSystem, String theCode, String theDisplay) {
		ValueSet vs = fetchCodeSystem(theContext, theCodeSystem);
		if (vs != null) {
			for (ValueSet.ConceptDefinitionComponent nextConcept : vs.getCodeSystem().getConcept()) {
				if (nextConcept.getCode().equals(theCode)) {
					ValueSet.ConceptDefinitionComponent component = new ValueSet.ConceptDefinitionComponent(new CodeType(theCode));
					return new CodeValidationResult(component);
				}
			}
		}

		return new CodeValidationResult(IssueSeverity.WARNING, "Unknown code: " + theCodeSystem + " / " + theCode);
	}

}
