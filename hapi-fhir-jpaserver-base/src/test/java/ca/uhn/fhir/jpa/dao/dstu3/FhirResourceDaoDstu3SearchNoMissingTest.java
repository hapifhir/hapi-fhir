package ca.uhn.fhir.jpa.dao.dstu3;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class FhirResourceDaoDstu3SearchNoMissingTest extends BaseJpaDstu3Test {

	@BeforeEach
	public void beforeDisableResultReuse() {
		myDaoConfig.setReuseCachedSearchResultsForMillis(null);
		myDaoConfig.setFetchSizeDefaultMaximum(new DaoConfig().getFetchSizeDefaultMaximum());
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.DISABLED);
	}

	@Test
	// Fails
	public void testSearchByIdentitySystemNoValue() {
		Patient pt = new Patient();
		pt.addIdentifier().setSystem("urn:oid:2.16.840.1.113883.3.7418.29.5");
		IIdType ptId = myPatientDao.create(pt).getId().toUnqualifiedVersionless();

		SearchParameterMap params = new SearchParameterMap();
		TokenParam tokenParam = new TokenParam().setSystem("urn:oid:2.16.840.1.113883.3.7418.29.5");
		params.add(Patient.SP_IDENTIFIER, tokenParam);
		params.setLoadSynchronous(true);

		List<String> patientIds = toUnqualifiedVersionlessIdValues(myPatientDao.search(params));
		assertThat(patientIds, contains(ptId.toString()));
	}

	@Test
	// Passes
	public void testSearchByIdentitySystemHasValueQueryNoValue() {
		Patient pt = new Patient();
		pt.addIdentifier().setSystem("urn:oid:2.16.840.1.113883.3.7418.29.5").setValue("abc123");
		IIdType ptId = myPatientDao.create(pt).getId().toUnqualifiedVersionless();

		SearchParameterMap params = new SearchParameterMap();
		TokenParam tokenParam = new TokenParam().setSystem("urn:oid:2.16.840.1.113883.3.7418.29.5");
		params.add(Patient.SP_IDENTIFIER, tokenParam);
		params.setLoadSynchronous(true);

		List<String> patientIds = toUnqualifiedVersionlessIdValues(myPatientDao.search(params));
		assertThat(patientIds, contains(ptId.toString()));
	}

	@Test
	// Passes
	public void testSearchByIdentitySystemWithValue() {
		Patient pt = new Patient();
		pt.addIdentifier().setSystem("urn:oid:2.16.840.1.113883.3.7418.29.5").setValue("abc123");
		IIdType ptId = myPatientDao.create(pt).getId().toUnqualifiedVersionless();

		SearchParameterMap params = new SearchParameterMap();
		TokenParam tokenParam = new TokenParam().setSystem("urn:oid:2.16.840.1.113883.3.7418.29.5").setValue("abc123");
		params.add(Patient.SP_IDENTIFIER, tokenParam);
		params.setLoadSynchronous(true);

		List<String> patientIds = toUnqualifiedVersionlessIdValues(myPatientDao.search(params));
		assertThat(patientIds, contains(ptId.toString()));
	}

}
