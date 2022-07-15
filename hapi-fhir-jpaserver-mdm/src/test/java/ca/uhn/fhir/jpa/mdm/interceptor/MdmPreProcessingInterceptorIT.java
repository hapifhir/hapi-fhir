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

import static org.junit.jupiter.api.Assertions.assertNull;

@ContextConfiguration(classes = {MdmHelperConfig.class})
public class MdmPreProcessingInterceptorIT extends BaseMdmR4Test{

	@RegisterExtension
	@Autowired
	public MdmHelperR4 myMdmHelper;

	@Autowired
	private IInterceptorService myInterceptorService;

	private PatientNameModifierMdmPreProcessingInterceptor myPreProcessingInterceptor;

	@BeforeEach
	public void beforeEach(){
		myPreProcessingInterceptor = new PatientNameModifierMdmPreProcessingInterceptor();
		myInterceptorService.registerInterceptor(myPreProcessingInterceptor);
	}

	@AfterEach
	public void afterEach(){
		myInterceptorService.unregisterInterceptor(myPreProcessingInterceptor);
	}

	@Test
	public void whenInterceptorIsRegisteredThenInterceptorIsCalled() throws InterruptedException {
		Patient aPatient = buildPatientWithNameAndId(NAME_GIVEN_JANE, JANE_ID);

		myMdmHelper.createWithLatch(aPatient);

		Patient interceptedResource = (Patient) myPreProcessingInterceptor.getReturnedValue();

		String modifiedFamilyName = interceptedResource.getNameFirstRep().getFamily();

		assertNull(modifiedFamilyName);

	}

	public static class PatientNameModifierMdmPreProcessingInterceptor {

		IBaseResource myReturnedValue;

		@Hook(Pointcut.MDM_BEFORE_PERSISTED_RESOURCE_CHECKED)
		public IBaseResource invoke(IBaseResource theResource) {

			((Patient)theResource).getNameFirstRep().setFamily(null);

			myReturnedValue = theResource;

			return myReturnedValue;
		}

		public IBaseResource getReturnedValue() {
			return myReturnedValue;
		}

	}

}


