package ca.uhn.fhir.test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.https.TlsAuthentication;
import ca.uhn.fhir.test.utilities.BaseRequestGeneratingCommandTestUtil;
import ca.uhn.fhir.test.utilities.BaseRestServerHelper;
import ca.uhn.fhir.test.utilities.RestServerDstu3Helper;
import ca.uhn.fhir.test.utilities.RestServerR4Helper;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.provider.Arguments;

import java.util.Optional;
import java.util.stream.Stream;

public class BaseFhirVersionParameterizedTest {

	@RegisterExtension
	public final RestServerR4Helper myRestServerR4Helper = new RestServerR4Helper();
	@RegisterExtension
	public final RestServerDstu3Helper myRestServerDstu3Helper = new RestServerDstu3Helper();
	@RegisterExtension
	public BaseRequestGeneratingCommandTestUtil myBaseRequestGeneratingCommandTestUtil = new BaseRequestGeneratingCommandTestUtil();

	protected final FhirContext myR4FhirContext = FhirContext.forR4();
	protected final FhirContext myDstu3FhirContext = FhirContext.forDstu3();

	protected static Stream<Arguments> baseParamsProvider(){
		return Stream.of(
			Arguments.arguments(FhirVersionEnum.R4),
			Arguments.arguments(FhirVersionEnum.DSTU3)
		);
	}

	protected FhirVersionParams getFhirVersionParams(FhirVersionEnum theFhirVersion){
		switch(theFhirVersion){
			case R4:
				return new FhirVersionParams(myRestServerR4Helper, myR4FhirContext);
			case DSTU3:
				return new FhirVersionParams(myRestServerDstu3Helper, myDstu3FhirContext);
			default:
				throw new RuntimeException("Unknown FHIR Version param provided: " + theFhirVersion);
		}
	}

	protected Optional<TlsAuthentication> getTlsAuthentication(){
		return myBaseRequestGeneratingCommandTestUtil.getTlsAuthentication();
	}

	protected class FhirVersionParams {
		private final BaseRestServerHelper myBaseRestServerHelper;
		private final FhirContext myFhirContext;
		private final FhirVersionEnum myFhirVersion;

		public FhirVersionParams(BaseRestServerHelper theBaseRestServerHelper, FhirContext theFhirContext) {
			myBaseRestServerHelper = theBaseRestServerHelper;
			myFhirContext = theFhirContext;
			myFhirVersion = theFhirContext.getVersion().getVersion();
		}

		public FhirContext getFhirContext() {
			return myFhirContext;
		}

		public FhirVersionEnum getFhirVersion() {
			return myFhirVersion;
		}

		public String getPatientEndpoint(){
			return myBaseRestServerHelper.getBase()+"/Patient";
		}

		public String getSecuredPatientEndpoint(){
			return myBaseRestServerHelper.getSecureBase()+"/Patient";
		}

		public IBaseResource parseResource(String json){
			return myFhirContext.newJsonParser().parseResource(json);
		}
	}

}
