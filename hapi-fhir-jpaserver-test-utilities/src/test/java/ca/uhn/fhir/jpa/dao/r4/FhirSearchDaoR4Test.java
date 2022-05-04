package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.StringOrListParam;
import ca.uhn.fhir.rest.param.StringParam;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;

public class FhirSearchDaoR4Test extends BaseJpaR4Test {

	@Autowired
	private IFulltextSearchSvc mySearchDao;

	@Test
	public void testDaoCallRequiresTransaction() {

		try {
			myResourceTableDao.count();
		} catch (InvalidDataAccessApiUsageException e) {
			// good
		}

		assert !TransactionSynchronizationManager.isActualTransactionActive();
	}

	@Test
	public void testSearchReturnsExpectedPatientsWhenContentTypeUsed() {
		// setup
		String content = "yui";

		Long id1;
		{
			Patient patient = new Patient();
			patient.addName().addGiven(content).setFamily("hirasawa");
			id1 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless().getIdPartAsLong();
		}
		Long id2;
		{
			Patient patient = new Patient();
			patient.addName().addGiven("mio").setFamily("akiyama");
			id2 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless().getIdPartAsLong();
		}

		SearchParameterMap params = new SearchParameterMap();
		params.add("_content", new StringParam(content));

		// test
		List<ResourcePersistentId> ids = mySearchDao.search("Patient",
			params);

		// verify results
		Assertions.assertEquals(1, ids.size());
		Assertions.assertEquals(id1, ids.get(0).getIdAsLong());
	}

	@Test
	public void testSearchesWithAccurateCountReturnOnlyExpectedResults() {
		// create 2 patients
		Patient patient = new Patient();
		patient.addName().setFamily("hirasawa");
		myPatientDao.create(patient);

		Patient patient2 = new Patient();
		patient2.addName().setFamily("akiyama");
		myPatientDao.create(patient2);

		// construct searchmap with Accurate search mode
		SearchParameterMap map = new SearchParameterMap();
		map.add(Constants.PARAM_CONTENT, new StringParam("hirasawa"));
		map.setSearchTotalMode(SearchTotalModeEnum.ACCURATE);

		// test
		IBundleProvider ret = myPatientDao.search(map);

		// only one should be returned
		Assertions.assertEquals(1, ret.size());
		Patient retPatient = (Patient) ret.getAllResources().get(0);
		Assertions.assertEquals(patient.getName().get(0).getFamily(),
			retPatient.getName().get(0).getFamily());
	}

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

		SearchParameterMap map;
		String resourceName = "Patient";

		// One term
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("AAAS")));

			map = new SearchParameterMap();
			map.add(Constants.PARAM_CONTENT, content);
			List<ResourcePersistentId> found = mySearchDao.search(resourceName, map);
			assertThat(ResourcePersistentId.toLongList(found), containsInAnyOrder(id1));
		}
		// OR
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("AAAS")).addOr(new StringParam("AAAB")));

			map = new SearchParameterMap();
			map.add(Constants.PARAM_CONTENT, content);
			map.add(Constants.PARAM_CONTENT, content);
			List<ResourcePersistentId> found = mySearchDao.search(resourceName, map);
			assertThat(ResourcePersistentId.toLongList(found), containsInAnyOrder(id1, id2));
		}
		// AND
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("AAAS")));
			content.addAnd(new StringOrListParam().addOr(new StringParam("CCC")));

			map = new SearchParameterMap();
			map.add(Constants.PARAM_CONTENT, content);
			List<ResourcePersistentId> found = mySearchDao.search(resourceName, map);
			assertThat(ResourcePersistentId.toLongList(found), containsInAnyOrder(id1));
		}
		// AND OR
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("AAAB")).addOr(new StringParam("AAAS")));
			content.addAnd(new StringOrListParam().addOr(new StringParam("CCC")));

			map = new SearchParameterMap();
			map.add(Constants.PARAM_CONTENT, content);
			List<ResourcePersistentId> found = mySearchDao.search(resourceName, map);
			assertThat(ResourcePersistentId.toLongList(found), containsInAnyOrder(id1, id2));
		}
		// All Resource Types
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("CCC")).addOr(new StringParam("DDD")));

			map = new SearchParameterMap();
			map.add(Constants.PARAM_CONTENT, content);
			List<ResourcePersistentId> found = mySearchDao.search(null, map);
			assertThat(ResourcePersistentId.toLongList(found), containsInAnyOrder(id1, id2, id3));
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

		SearchParameterMap map;
		String resourceName = "Patient";

		// One term
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("AAAS")));

			map = new SearchParameterMap();
			map.add(Constants.PARAM_TEXT, content);
			List<ResourcePersistentId> found = mySearchDao.search(resourceName, map);
			assertThat(ResourcePersistentId.toLongList(found), containsInAnyOrder(id1));
		}
		// OR
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("AAAS")).addOr(new StringParam("AAAB")));

			map = new SearchParameterMap();
			map.add(Constants.PARAM_TEXT, content);
			List<ResourcePersistentId> found = mySearchDao.search(resourceName, map);
			assertThat(ResourcePersistentId.toLongList(found), containsInAnyOrder(id1, id2));
		}
		// AND
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("AAAS")));
			content.addAnd(new StringOrListParam().addOr(new StringParam("CCC")));

			map = new SearchParameterMap();
			map.add(Constants.PARAM_TEXT, content);
			List<ResourcePersistentId> found = mySearchDao.search(resourceName, map);
			assertThat(ResourcePersistentId.toLongList(found), containsInAnyOrder(id1));
		}
		// AND OR
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("AAAB")).addOr(new StringParam("AAAS")));
			content.addAnd(new StringOrListParam().addOr(new StringParam("CCC")));

			map = new SearchParameterMap();
			map.add(Constants.PARAM_TEXT, content);
			List<ResourcePersistentId> found = mySearchDao.search(resourceName, map);
			assertThat(ResourcePersistentId.toLongList(found), containsInAnyOrder(id1, id2));
		}
		// Tag Contents
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("div")));

			map = new SearchParameterMap();
			map.add(Constants.PARAM_TEXT, content);
			List<ResourcePersistentId> found = mySearchDao.search(resourceName, map);
			assertThat(ResourcePersistentId.toLongList(found), empty());
		}
	}

}
