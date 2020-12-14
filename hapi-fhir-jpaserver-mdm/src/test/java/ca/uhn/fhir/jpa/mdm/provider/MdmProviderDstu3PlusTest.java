package ca.uhn.fhir.jpa.mdm.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.mdm.provider.MdmProviderDstu3Plus;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.dstu3.model.DecimalType;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MdmProviderDstu3PlusTest {

	private MdmProviderDstu3Plus myProvider;

	private FhirContext myFhirContext = FhirContext.forR4();

	@BeforeEach
	public void init() {
		myProvider = new MdmProviderDstu3Plus(myFhirContext, null, null, null, null);
	}

	@Test
	public void testBuildMdmOutParametersWithCount() {
		IBaseParameters params = myProvider.buildMdmOutParametersWithCount(10);
		Assertions.assertNotNull(params);

		String paramJson = myFhirContext.newJsonParser().encodeResourceToString(params);
		System.out.println(paramJson);

		IBaseParameters checkParams = myProvider.buildMdmOutParametersWithCount(10);
		String checkJson = myFhirContext.newJsonParser().encodeResourceToString(params);

		Assertions.assertEquals(checkJson, paramJson);
	}


	private Parameters buildMdmOutParametersWithCount(long theCount) {
		Parameters parameters = new Parameters();
		parameters.addParameter()
			.setName(ProviderConstants.OPERATION_MDM_BATCH_RUN_OUT_PARAM_SUBMIT_COUNT)
			.setValue(new DecimalType(theCount));
		return parameters;
	}

}
