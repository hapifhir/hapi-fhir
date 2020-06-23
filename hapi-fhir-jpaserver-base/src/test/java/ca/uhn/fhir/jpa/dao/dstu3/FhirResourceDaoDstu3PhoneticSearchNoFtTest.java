package ca.uhn.fhir.jpa.dao.dstu3;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.param.StringParam;
import org.apache.commons.codec.language.Soundex;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class FhirResourceDaoDstu3PhoneticSearchNoFtTest extends BaseJpaDstu3Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoDstu3PhoneticSearchNoFtTest.class);
	public static final String GALE = "Gale";
	public static final String GAIL = "Gail";

	@Before
	public void beforeDisableResultReuse() {
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.DISABLED);
		myDaoConfig.setReuseCachedSearchResultsForMillis(null);
		myDaoConfig.setFetchSizeDefaultMaximum(new DaoConfig().getFetchSizeDefaultMaximum());
	}

	@Test
	public void testSoundex() {
		Soundex soundex = new Soundex();
		assertEquals(soundex.encode(GALE), soundex.encode(GAIL));
	}

	@Test
	public void phoneticMatch() {
		myDaoConfig.setStringEncoder(new Soundex());

		Patient patient;
		SearchParameterMap map;

		patient = new Patient();
		patient.addName().addGiven(GALE);

		IIdType pId1 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();

		List<ResourceIndexedSearchParamString> stringParams = myResourceIndexedSearchParamStringDao.findAll();
		assertThat(stringParams, hasSize(3));
		List<String> stringParamNames = stringParams.stream().map(ResourceIndexedSearchParamString::getParamName).collect(Collectors.toList());
		assertThat(stringParamNames, containsInAnyOrder(Patient.SP_NAME, Patient.SP_GIVEN, Patient.SP_PHONETIC));

		map = new SearchParameterMap();
		map.add(Patient.SP_PHONETIC, new StringParam(GALE));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map)), contains(toValues(pId1)));

		map = new SearchParameterMap();
		map.add(Patient.SP_PHONETIC, new StringParam(GAIL));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map)), contains(toValues(pId1)));

		myDaoConfig.setStringEncoder(new DaoConfig().getStringEncoder());
	}
}
