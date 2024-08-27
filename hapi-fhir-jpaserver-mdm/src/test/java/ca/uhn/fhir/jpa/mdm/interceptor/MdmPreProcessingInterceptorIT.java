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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = {MdmHelperConfig.class})
public class MdmPreProcessingInterceptorIT extends BaseMdmR4Test{

	@RegisterExtension
	@Autowired
	public MdmHelperR4 myMdmHelper;

	@Autowired
	private IInterceptorService myInterceptorService;

	private PatientNameModifierMdmPreProcessingInterceptor myPreProcessingInterceptor = new PatientNameModifierMdmPreProcessingInterceptor();
	private PatientInterceptorWrapper myPatientInterceptorWrapper;
	@BeforeEach
	public void beforeEach(){
		// we wrap the preProcessing interceptor to catch the return value;
		myPatientInterceptorWrapper = new PatientInterceptorWrapper(myPreProcessingInterceptor);
		myInterceptorService.registerInterceptor(myPatientInterceptorWrapper);
	}

	@AfterEach
	public void afterEach(){
		myInterceptorService.unregisterInterceptor(myPatientInterceptorWrapper);
	}

	@Test
	public void whenInterceptorIsRegisteredThenInterceptorIsCalled() throws InterruptedException {

		Patient aPatient = buildPatientWithNameAndId(NAME_GIVEN_JANE, JANE_ID);

		myMdmHelper.createWithLatch(aPatient);

		Patient interceptedResource = (Patient) myPatientInterceptorWrapper.getReturnedValue();

		assertThat(interceptedResource.getName()).isEmpty();

	}

	public static class PatientInterceptorWrapper {

		private PatientNameModifierMdmPreProcessingInterceptor myPatientInterceptor;

		private IBaseResource myReturnedValue;

		public PatientInterceptorWrapper(PatientNameModifierMdmPreProcessingInterceptor thePatientInterceptor) {
			myPatientInterceptor = thePatientInterceptor;
		}

		@Hook(Pointcut.MDM_BEFORE_PERSISTED_RESOURCE_CHECKED)
		public void invoke(IBaseResource theResource) {
			myPatientInterceptor.invoke(theResource);
			myReturnedValue = theResource;
		}

		public IBaseResource getReturnedValue() {
			return myReturnedValue;
		}
	}

}




