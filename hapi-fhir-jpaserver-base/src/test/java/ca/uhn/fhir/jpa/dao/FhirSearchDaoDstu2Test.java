package ca.uhn.fhir.jpa.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.StringOrListParam;
import ca.uhn.fhir.rest.param.StringParam;

@ContextConfiguration(locations = { "classpath:fhir-spring-search-config-dstu2.xml" })
public class FhirSearchDaoDstu2Test extends BaseJpaDstu2Test {
	
	@Autowired
	private ISearchDao mySearchDao;
	
	@Test
	public void testStringSearch() {
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().addGiven("testSearchStringParamWithNonNormalized_h\u00F6ra");
			patient.addName().addFamily("AAAS");
			myPatientDao.create(patient);
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("002");
			patient.addName().addGiven("testSearchStringParamWithNonNormalized_HORA");
			myPatientDao.create(patient);
		}

		SearchParameterMap map = new SearchParameterMap();
		map.add(ISearchDao.FULL_TEXT_PARAM_NAME, new StringAndListParam().addAnd(new StringOrListParam().addOr(new StringParam("AAA"))));
		mySearchDao.search(map);
	}
	
}
