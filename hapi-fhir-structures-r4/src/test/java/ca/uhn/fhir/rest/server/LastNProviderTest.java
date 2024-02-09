package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.rest.server.provider.BaseLastNProvider;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Bundle;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LastNProviderTest extends BaseR4ServerTest {

	private IBaseReference myLastSubject;
	private List<IBaseCoding> myLastCategories;
	private List<IBaseCoding> myLastCodes;
	private IPrimitiveType<Integer> myLastMax;

	@Test
	public void testAllParamsPopulated() throws Exception {

		class MyProvider extends BaseLastNProvider {

			@Override
			protected IBaseBundle processLastN(IBaseReference theSubject, List<IBaseCoding> theCategories, List<IBaseCoding> theCodes, IPrimitiveType<Integer> theMax) {
				myLastSubject = theSubject;
				myLastCategories = theCategories;
				myLastCodes = theCodes;
				myLastMax = theMax;

				Bundle retVal = new Bundle();
				retVal.setId("abc123");
				retVal.setType(Bundle.BundleType.SEARCHSET);
				return retVal;
			}
		}
		MyProvider provider = new MyProvider();
		startServer(provider);

		Bundle response = ourServer
			 .getFhirClient()
			.search()
			.byUrl(ourServer.getBaseUrl() + "/Observation/$lastn?subject=Patient/123&category=http://terminology.hl7.org/CodeSystem/observation-category|laboratory,http://terminology.hl7.org/CodeSystem/observation-category|vital-signs&code=http://loinc.org|1111-1,http://loinc.org|2222-2&max=15")
			.returnBundle(Bundle.class)
			.execute();
		assertThat(response.getIdElement().getIdPart()).isEqualTo("abc123");
		assertThat(myLastSubject.getReferenceElement().getValue()).isEqualTo("Patient/123");
		assertThat(myLastCategories).hasSize(2);
		assertThat(myLastCategories.get(0).getSystem()).isEqualTo("http://terminology.hl7.org/CodeSystem/observation-category");
		assertThat(myLastCategories.get(0).getCode()).isEqualTo("laboratory");
		assertThat(myLastCategories.get(1).getSystem()).isEqualTo("http://terminology.hl7.org/CodeSystem/observation-category");
		assertThat(myLastCategories.get(1).getCode()).isEqualTo("vital-signs");
		assertThat(myLastCodes).hasSize(2);
		assertThat(myLastCodes.get(0).getSystem()).isEqualTo("http://loinc.org");
		assertThat(myLastCodes.get(0).getCode()).isEqualTo("1111-1");
		assertThat(myLastCodes.get(1).getSystem()).isEqualTo("http://loinc.org");
		assertThat(myLastCodes.get(1).getCode()).isEqualTo("2222-2");
		assertThat(myLastMax.getValue().intValue()).isEqualTo(15);
	}


}
