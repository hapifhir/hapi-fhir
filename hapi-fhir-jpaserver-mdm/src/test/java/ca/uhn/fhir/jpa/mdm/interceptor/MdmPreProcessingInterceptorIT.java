package ca.uhn.fhir.jpa.mdm.interceptor;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.mdm.helper.MdmHelperConfig;
import ca.uhn.fhir.jpa.mdm.helper.MdmHelperR4;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

	private PatientNameModifierMdmPreProcessingInterceptor myPreProcessingInterceptor = new PatientNameModifierMdmPreProcessingInterceptor();

	@BeforeEach
	public void beforeEach(){
		myInterceptorRegistry.registerInterceptor(myPreProcessingInterceptor);
	}

	@AfterEach
	public void afterEach(){
		myInterceptorRegistry.unregisterInterceptor(myPreProcessingInterceptor);
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

	@Test
	public void whenPointCutModifiesBaseResourceThenBaseResourceIsNotPersisted() throws InterruptedException {

		Patient aPatient = buildPatientWithNameAndId(NAME_GIVEN_JANE, JANE_ID);

		MdmHelperR4.OutcomeAndLogMessageWrapper outcomeWrapper = myMdmHelper.createWithLatch(aPatient);
		IIdType idDt = outcomeWrapper.getDaoMethodOutcome().getEntity().getIdDt();

		IBundleProvider bundle = myPatientDao.history(idDt, null, null, 0, null);

		assertEquals(1, myPreProcessingInterceptor.getInvocationCount());
		assertEquals(1, bundle.size());

	}

	static String[] getNames() {
		return new String[]{"NewName", null};
	}

	public static class PatientNameModifierMdmPreProcessingInterceptor {

		String myNewValue = EMPTY;

		IBaseResource myReturnedValue;

		int myInvocationCount = 0;

		@Hook(Pointcut.MDM_BEFORE_PERSISTED_RESOURCE_CHECKED)
		public IBaseResource invoke(IBaseResource theResource) {
			myInvocationCount++;

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

		public int getInvocationCount() {
			return myInvocationCount;
		}
	}

}


