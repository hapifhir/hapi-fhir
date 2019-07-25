package ca.uhn.fhir.jpa.dao.r4;

import static org.junit.Assert.assertEquals;

import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.*;

import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;

@SuppressWarnings({ })
public class FhirResourceDaoCustomTypeR4Test extends BaseJpaR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoCustomTypeR4Test.class);

	@Before
	public void before() {
		myFhirCtx.setDefaultTypeForProfile(CustomObservationR4.PROFILE, CustomObservationR4.class);
	}
	
	@Test
	public void testSaveAndRestore() {
		CustomObservationR4 obs = new CustomObservationR4();
		obs.setEyeColour(new StringType("blue"));
		
		IIdType id = myObservationDao.create(obs).getId().toUnqualifiedVersionless();
		
		CustomObservationR4 read = (CustomObservationR4) myObservationDao.read(id);
		assertEquals("blue", read.getEyeColour().getValue());
		
		IBundleProvider found = myObservationDao.search(new SearchParameterMap());
		assertEquals(1, found.size().intValue());
		CustomObservationR4 search = (CustomObservationR4) found.getResources(0, 1).get(0);
		assertEquals("blue", search.getEyeColour().getValue());
		
	}
	
	@After
	public void after() {
		myFhirCtx.setDefaultTypeForProfile(CustomObservationR4.PROFILE, null);
	}
}
