package ca.uhn.fhir.validation;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.hl7.fhir.instance.model.Bundle;
import org.hl7.fhir.instance.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.instance.model.IdType;
import org.hl7.fhir.instance.model.OperationOutcome.IssueSeverity;
import org.hl7.fhir.instance.model.ValueSet;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

public class DefaultProfileValidationSupport implements IValidationSupport {

	private Map<String, ValueSet> myDefaultValueSets;
	
	@Override
	public boolean isCodeSystemSupported(String theSystem) {
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends IBaseResource> T fetchResource(FhirContext theContext, Class<T> theClass, String theUri) {
		if (theUri.startsWith("http://hl7.org/fhir/StructureDefinition/")) {
			return (T) FhirInstanceValidator.loadProfileOrReturnNull(null, FhirInstanceValidator.getHl7OrgDstu2Ctx(theContext), theUri.substring("http://hl7.org/fhir/StructureDefinition/".length()));
		}
		if (theUri.startsWith("http://hl7.org/fhir/ValueSet/")) {
			Map<String, ValueSet> defaultValueSets = myDefaultValueSets;
			if (defaultValueSets == null) {
				InputStream valuesetText = DefaultProfileValidationSupport.class.getResourceAsStream("/org/hl7/fhir/instance/model/valueset/valuesets.xml");
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

  @Override
  public org.hl7.fhir.instance.utils.IWorkerContext.ValidationResult validateCode(String theCodeSystem, String theCode, String theDisplay) {
    return new org.hl7.fhir.instance.utils.IWorkerContext.ValidationResult(IssueSeverity.INFORMATION, "Unknown code: " + theCodeSystem + " / " + theCode);
  }

  @Override
  public ValueSet fetchCodeSystem(String theSystem) {
    return null;
  }

}
