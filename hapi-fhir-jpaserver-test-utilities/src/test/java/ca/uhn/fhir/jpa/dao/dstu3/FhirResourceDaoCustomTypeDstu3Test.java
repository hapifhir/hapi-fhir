package ca.uhn.fhir.jpa.dao.dstu3;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaDstu3Test;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings({ })
public class FhirResourceDaoCustomTypeDstu3Test extends BaseJpaDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoCustomTypeDstu3Test.class);

	@BeforeEach
	public void before() {
		myFhirContext.setDefaultTypeForProfile(CustomObservationDstu3.PROFILE, CustomObservationDstu3.class);
	}
	
	@Test
	public void testSaveAndRestore() {
		CustomObservationDstu3 obs = new CustomObservationDstu3();
		obs.setEyeColour(new StringType("blue"));
		
		IIdType id = myObservationDao.create(obs).getId().toUnqualifiedVersionless();
		
		CustomObservationDstu3 read = (CustomObservationDstu3) myObservationDao.read(id);
		assertEquals("blue", read.getEyeColour().getValue());
		
		IBundleProvider found = myObservationDao.search(new SearchParameterMap());
		assertEquals(1, found.size().intValue());
		CustomObservationDstu3 search = (CustomObservationDstu3) found.getResources(0, 1).get(0);
		assertEquals("blue", search.getEyeColour().getValue());
		
	}
	
	@AfterEach
	public void after() {
		myFhirContext.setDefaultTypeForProfile(CustomObservationDstu3.PROFILE, null);
	}
}
