package ca.uhn.fhir.cli;

import org.hl7.fhir.dstu3.hapi.validation.IValidationSupport;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.client.ServerValidationModeEnum;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;

public class LoadingValidationSupportDstu3 implements IValidationSupport {

	private static FhirContext myCtx = FhirContext.forDstu3();

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(LoadingValidationSupportDstu3.class);

	@Override
	public ValueSetExpansionComponent expandValueSet(FhirContext theContext, ConceptSetComponent theInclude) {
		return null;
	}

	@Override
	public ValueSet fetchCodeSystem(FhirContext theContext, String theSystem) {
		return null;
	}

	@Override
	public <T extends IBaseResource> T fetchResource(FhirContext theContext, Class<T> theClass, String theUri) {
		String resName = myCtx.getResourceDefinition(theClass).getName();
		ourLog.info("Attempting to fetch {} at URL: {}", resName, theUri);
		
		myCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		IGenericClient client = myCtx.newRestfulGenericClient("http://example.com");
		
		T result;
		try {
			result = client.read(theClass, theUri);
		} catch (BaseServerResponseException e) {
			throw new CommandFailureException("FAILURE: Received HTTP " + e.getStatusCode() + ": " + e.getMessage());
		}
		ourLog.info("Successfully loaded resource");
		return result;
	}

	@Override
	public boolean isCodeSystemSupported(FhirContext theContext, String theSystem) {
		return false;
	}

	@Override
	public CodeValidationResult validateCode(FhirContext theContext, String theCodeSystem, String theCode, String theDisplay) {
		return null;
	}

}
