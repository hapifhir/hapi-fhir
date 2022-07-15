package ca.uhn.fhir.jpa.mdm.interceptor;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.mdm.helper.MdmHelperConfig;
import ca.uhn.fhir.jpa.mdm.helper.MdmHelperR4;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = {MdmHelperConfig.class})
public class MdmPreProcessingInterceptorIT extends BaseMdmR4Test{

	@RegisterExtension
	@Autowired
	public MdmHelperR4 myMdmHelper;

	@Autowired
	private IInterceptorService myInterceptorService;

	private PatientNameModifierMdmPreProcessingInterceptor myPreProcessingInterceptor = new PatientNameModifierMdmPreProcessingInterceptor();

	@BeforeEach
	public void beforeEach(){
		myInterceptorService.registerInterceptor(myPreProcessingInterceptor);
	}

	@AfterEach
	public void afterEach(){
		myInterceptorService.unregisterInterceptor(myPreProcessingInterceptor);
	}

	@ParameterizedTest
	@MethodSource("getNames")
	public void whenInterceptorIsRegisteredThenInterceptorIsCalled(String theSubstituteName) throws InterruptedException {
		myPreProcessingInterceptor.setNewValue(theSubstituteName);

		Patient aPatient = buildPatientWithNameAndId(NAME_GIVEN_JANE, JANE_ID);

		myMdmHelper.createWithLatch(aPatient);

		Patient interceptedResource = (Patient) myPreProcessingInterceptor.getReturnedValue();

		assertEquals(theSubstituteName, interceptedResource.getNameFirstRep().getFamily());

	}

	static String[] getNames() {
		return new String[]{"NewName", null};
	}

	public static class PatientNameModifierMdmPreProcessingInterceptor {

		String myNewValue = EMPTY;

		IBaseResource myReturnedValue;

		@Hook(Pointcut.MDM_BEFORE_PERSISTED_RESOURCE_CHECKED)
		public IBaseResource invoke(IBaseResource theResource) {

			((Patient)theResource).getNameFirstRep().setFamily(myNewValue);

			myReturnedValue = theResource;

			return myReturnedValue;
		}

		public void setNewValue(String theNewValue) {
			myNewValue = theNewValue;
		}

		public IBaseResource getReturnedValue() {
			return myReturnedValue;
		}

	}

}


