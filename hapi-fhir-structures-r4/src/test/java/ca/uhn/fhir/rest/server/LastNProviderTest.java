package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.rest.server.provider.BaseLastNProvider;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Bundle;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

		Bundle response = myClient
			.search()
			.byUrl(myBaseUrl + "/Observation/$lastn?subject=Patient/123&category=http://terminology.hl7.org/CodeSystem/observation-category|laboratory,http://terminology.hl7.org/CodeSystem/observation-category|vital-signs&code=http://loinc.org|1111-1,http://loinc.org|2222-2&max=15")
			.returnBundle(Bundle.class)
			.execute();
		assertEquals("abc123", response.getIdElement().getIdPart());
		assertEquals("Patient/123", myLastSubject.getReferenceElement().getValue());
		assertEquals(2, myLastCategories.size());
		assertEquals("http://terminology.hl7.org/CodeSystem/observation-category", myLastCategories.get(0).getSystem());
		assertEquals("laboratory", myLastCategories.get(0).getCode());
		assertEquals("http://terminology.hl7.org/CodeSystem/observation-category", myLastCategories.get(1).getSystem());
		assertEquals("vital-signs", myLastCategories.get(1).getCode());
		assertEquals(2, myLastCodes.size());
		assertEquals("http://loinc.org", myLastCodes.get(0).getSystem());
		assertEquals("1111-1", myLastCodes.get(0).getCode());
		assertEquals("http://loinc.org", myLastCodes.get(1).getSystem());
		assertEquals("2222-2", myLastCodes.get(1).getCode());
		assertEquals(15, myLastMax.getValue().intValue());
	}


}
