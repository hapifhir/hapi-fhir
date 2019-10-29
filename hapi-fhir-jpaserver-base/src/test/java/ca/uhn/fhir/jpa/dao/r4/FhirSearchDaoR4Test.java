package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.StringOrListParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.junit.AfterClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;

public class FhirSearchDaoR4Test extends BaseJpaR4Test {

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


	@Autowired
	private IFulltextSearchSvc mySearchDao;
	
	@Test
	public void testContentSearch() {
		Long id1;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().addGiven("testSearchStringParamWithNonNormalized_h\u00F6ra");
			patient.addName().addGiven("AAAS");
			patient.addName().addGiven("CCC");
			id1 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless().getIdPartAsLong();
		}
		Long id2;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("002");
			patient.addName().addGiven("testSearchStringParamWithNonNormalized_HORA");
			patient.addName().addGiven("AAAB");
			patient.addName().addGiven("CCC");
			id2 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless().getIdPartAsLong();
		}
		Long id3;
		{
			Organization org = new Organization();
			org.setName("DDD");
			id3 = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless().getIdPartAsLong();
		}

		SearchParameterMap map = new SearchParameterMap();
		String resourceName = "Patient";

		// One term
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("AAAS")));
			
			map.add(Constants.PARAM_CONTENT, content);
			List<Long> found = mySearchDao.search(resourceName, map);
			assertThat(found, containsInAnyOrder(id1));
		}
		// OR
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("AAAS")).addOr(new StringParam("AAAB")));
			
			map.add(Constants.PARAM_CONTENT, content);
			List<Long> found = mySearchDao.search(resourceName, map);
			assertThat(found, containsInAnyOrder(id1, id2));
		}		
		// AND
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("AAAS")));
			content.addAnd(new StringOrListParam().addOr(new StringParam("CCC")));
			
			map.add(Constants.PARAM_CONTENT, content);
			List<Long> found = mySearchDao.search(resourceName, map);
			assertThat(found, containsInAnyOrder(id1));
		}
		// AND OR
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("AAAB")).addOr(new StringParam("AAAS")));
			content.addAnd(new StringOrListParam().addOr(new StringParam("CCC")));
			
			map.add(Constants.PARAM_CONTENT, content);
			List<Long> found = mySearchDao.search(resourceName, map);
			assertThat(found, containsInAnyOrder(id1, id2));
		}
		// All Resource Types
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("CCC")).addOr(new StringParam("DDD")));
			
			map.add(Constants.PARAM_CONTENT, content);
			List<Long> found = mySearchDao.search(null, map);
			assertThat(found, containsInAnyOrder(id1, id2, id3));
		}

	}
	
	@Test
	public void testNarrativeSearch() {
		Long id1;
		{
			Patient patient = new Patient();
			patient.getText().setDivAsString("<div>AAAS<p>FOO</p> CCC    </div>");
			id1 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless().getIdPartAsLong();
		}
		Long id2;
		{
			Patient patient = new Patient();
			patient.getText().setDivAsString("<div>AAAB<p>FOO</p> CCC    </div>");
			id2 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless().getIdPartAsLong();
		}
		{
			Patient patient = new Patient();
			patient.getText().setDivAsString("<div>ZZYZXY</div>");
			myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless().getIdPartAsLong();
		}

		SearchParameterMap map = new SearchParameterMap();
		String resourceName = "Patient";

		// One term
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("AAAS")));
			
			map.add(Constants.PARAM_TEXT, content);
			List<Long> found = mySearchDao.search(resourceName, map);
			assertThat(found, containsInAnyOrder(id1));
		}
		// OR
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("AAAS")).addOr(new StringParam("AAAB")));
			
			map.add(Constants.PARAM_TEXT, content);
			List<Long> found = mySearchDao.search(resourceName, map);
			assertThat(found, containsInAnyOrder(id1, id2));
		}		
		// AND
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("AAAS")));
			content.addAnd(new StringOrListParam().addOr(new StringParam("CCC")));
			
			map.add(Constants.PARAM_TEXT, content);
			List<Long> found = mySearchDao.search(resourceName, map);
			assertThat(found, containsInAnyOrder(id1));
		}
		// AND OR
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("AAAB")).addOr(new StringParam("AAAS")));
			content.addAnd(new StringOrListParam().addOr(new StringParam("CCC")));
			
			map.add(Constants.PARAM_TEXT, content);
			List<Long> found = mySearchDao.search(resourceName, map);
			assertThat(found, containsInAnyOrder(id1, id2));
		}
		// Tag Contents
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("div")));
			
			map.add(Constants.PARAM_TEXT, content);
			List<Long> found = mySearchDao.search(resourceName, map);
			assertThat(found, empty());
		}
	}

}
