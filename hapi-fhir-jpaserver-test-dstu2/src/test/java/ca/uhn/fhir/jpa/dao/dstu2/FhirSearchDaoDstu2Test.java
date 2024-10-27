package ca.uhn.fhir.jpa.dao.dstu2;

import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.dstu2.resource.Organization;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.StringOrListParam;
import ca.uhn.fhir.rest.param.StringParam;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FhirSearchDaoDstu2Test extends BaseJpaDstu2Test {

	@Autowired
	private IFulltextSearchSvc mySearchDao;

	@Test
	public void testContentSearch() {
		Long id1;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().addGiven("testSearchStringParamWithNonNormalized_h\u00F6ra");
			patient.addName().addFamily("AAAS");
			patient.addName().addFamily("CCC");
			id1 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless().getIdPartAsLong();
		}
		Long id2;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("002");
			patient.addName().addGiven("testSearchStringParamWithNonNormalized_HORA");
			patient.addName().addFamily("AAAB");
			patient.addName().addFamily("CCC");
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
			List<JpaPid> found = mySearchDao.search(resourceName, map, SystemRequestDetails.newSystemRequestAllPartitions());
			assertThat(JpaPid.toLongList(found)).containsExactlyInAnyOrder(id1);
		}
		// OR
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("AAAS")).addOr(new StringParam("AAAB")));

			map.add(Constants.PARAM_CONTENT, content);
			List<JpaPid> found = mySearchDao.search(resourceName, map, SystemRequestDetails.newSystemRequestAllPartitions());
			assertThat(JpaPid.toLongList(found)).containsExactlyInAnyOrder(id1, id2);
		}
		// AND
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("AAAS")));
			content.addAnd(new StringOrListParam().addOr(new StringParam("CCC")));

			map.add(Constants.PARAM_CONTENT, content);
			List<JpaPid> found = mySearchDao.search(resourceName, map, SystemRequestDetails.newSystemRequestAllPartitions());
			assertThat(JpaPid.toLongList(found)).containsExactlyInAnyOrder(id1);
		}
		// AND OR
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("AAAB")).addOr(new StringParam("AAAS")));
			content.addAnd(new StringOrListParam().addOr(new StringParam("CCC")));

			map.add(Constants.PARAM_CONTENT, content);
			List<JpaPid> found = mySearchDao.search(resourceName, map, SystemRequestDetails.newSystemRequestAllPartitions());
			assertThat(JpaPid.toLongList(found)).containsExactlyInAnyOrder(id1, id2);
		}
		// All Resource Types
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("CCC")).addOr(new StringParam("DDD")));

			map.add(Constants.PARAM_CONTENT, content);
			List<JpaPid> found = mySearchDao.search(null, map, SystemRequestDetails.newSystemRequestAllPartitions());
			assertThat(JpaPid.toLongList(found)).containsExactlyInAnyOrder(id1, id2, id3);
		}

	}

	@Test
	public void testNarrativeSearch() {
		Long id1;
		{
			Patient patient = new Patient();
			patient.getText().setDiv("<div>AAAS<p>FOO</p> CCC    </div>");
			id1 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless().getIdPartAsLong();
		}
		Long id2;
		{
			Patient patient = new Patient();
			patient.getText().setDiv("<div>AAAB<p>FOO</p> CCC    </div>");
			id2 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless().getIdPartAsLong();
		}

		SearchParameterMap map = new SearchParameterMap();
		String resourceName = "Patient";

		// One term
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("AAAS")));

			map.add(Constants.PARAM_TEXT, content);
			List<JpaPid> found = mySearchDao.search(resourceName, map, SystemRequestDetails.newSystemRequestAllPartitions());
			assertThat(JpaPid.toLongList(found)).containsExactlyInAnyOrder(id1);
		}
		// OR
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("AAAS")).addOr(new StringParam("AAAB")));

			map.add(Constants.PARAM_TEXT, content);
			List<JpaPid> found = mySearchDao.search(resourceName, map, SystemRequestDetails.newSystemRequestAllPartitions());
			assertThat(JpaPid.toLongList(found)).containsExactlyInAnyOrder(id1, id2);
		}
		// AND
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("AAAS")));
			content.addAnd(new StringOrListParam().addOr(new StringParam("CCC")));

			map.add(Constants.PARAM_TEXT, content);
			List<JpaPid> found = mySearchDao.search(resourceName, map, SystemRequestDetails.newSystemRequestAllPartitions());
			assertThat(JpaPid.toLongList(found)).containsExactlyInAnyOrder(id1);
		}
		// AND OR
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("AAAB")).addOr(new StringParam("AAAS")));
			content.addAnd(new StringOrListParam().addOr(new StringParam("CCC")));

			map.add(Constants.PARAM_TEXT, content);
			List<JpaPid> found = mySearchDao.search(resourceName, map, SystemRequestDetails.newSystemRequestAllPartitions());
			assertThat(JpaPid.toLongList(found)).containsExactlyInAnyOrder(id1, id2);
		}
		// Tag Contents
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("div")));

			map.add(Constants.PARAM_TEXT, content);
			List<JpaPid> found = mySearchDao.search(resourceName, map, SystemRequestDetails.newSystemRequestAllPartitions());
			assertThat(JpaPid.toLongList(found)).isEmpty();
		}
	}

}
