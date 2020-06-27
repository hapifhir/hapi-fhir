package ca.uhn.fhir.jpa.dao.dstu3;

import ca.uhn.fhir.context.phonetic.ApacheEncoder;
import ca.uhn.fhir.context.phonetic.PhoneticEncoderEnum;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.rest.param.StringParam;
import org.apache.commons.codec.language.Soundex;
import org.hl7.fhir.dstu3.model.Enumerations;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.SearchParameter;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
	public static final String NAME_SOUNDEX_SP = "nameSoundex";

	@Autowired
	ISearchParamRegistry mySearchParamRegistry;

	@Before
	public void beforeDisableResultReuse() {
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.DISABLED);
		myDaoConfig.setReuseCachedSearchResultsForMillis(null);
		myDaoConfig.setFetchSizeDefaultMaximum(new DaoConfig().getFetchSizeDefaultMaximum());

		createNameSoundexSearchParameter(NAME_SOUNDEX_SP, PhoneticEncoderEnum.SOUNDEX);
		mySearchParamRegistry.forceRefresh();

		mySearchParamRegistry.setPhoneticEncoder(new ApacheEncoder(new Soundex()));
	}

	@After
	public void resetStringEncoder() {
		mySearchParamRegistry.setPhoneticEncoder(null);
	}

	@Test
	public void testSoundex() {
		Soundex soundex = new Soundex();
		assertEquals(soundex.encode(GALE), soundex.encode(GAIL));
	}

	@Test
	public void phoneticMatch() {

		Patient patient;
		SearchParameterMap map;

		patient = new Patient();
		patient.addName().addGiven(GALE);

		IIdType pId1 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();

		List<ResourceIndexedSearchParamString> stringParams = myResourceIndexedSearchParamStringDao.findAll();
		assertThat(stringParams, hasSize(4));
		List<String> stringParamNames = stringParams.stream().map(ResourceIndexedSearchParamString::getParamName).collect(Collectors.toList());
		assertThat(stringParamNames, containsInAnyOrder(Patient.SP_NAME, Patient.SP_GIVEN, Patient.SP_PHONETIC, NAME_SOUNDEX_SP));

		assertSearchMatch(pId1, Patient.SP_PHONETIC, GALE);
		assertSearchMatch(pId1, Patient.SP_PHONETIC, GAIL);
		assertSearchMatch(pId1, NAME_SOUNDEX_SP, GAIL);
		assertSearchMatch(pId1, NAME_SOUNDEX_SP, GALE);
	}

	private void assertSearchMatch(IIdType thePId1, String theSp, String theValue) {
		SearchParameterMap map;
		map = new SearchParameterMap();
		map.add(theSp, new StringParam(theValue));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map)), contains(toValues(thePId1)));
	}

	private void createNameSoundexSearchParameter(String theCode, PhoneticEncoderEnum theEncoder) {
		SearchParameter searchParameter = new SearchParameter();
		searchParameter.addBase("Patient");
		searchParameter.setCode(theCode);
		searchParameter.setType(Enumerations.SearchParamType.STRING);
		searchParameter.setTitle("Test Name Soundex");
		searchParameter.setExpression("Patient.name");
// Maybe use in the future?  RuntimeSearchParam doesn't store this...
//		searchParameter.setXpathUsage(SearchParameter.XPathUsageType.PHONETIC);
		searchParameter.setStatus(Enumerations.PublicationStatus.ACTIVE);
		searchParameter.addExtension()
			.setUrl(JpaConstants.EXT_SEARCHPARAM_PHONETIC_ENCODER)
		.setValue(new StringType(theEncoder.name()));
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(searchParameter));
		mySearchParameterDao.create(searchParameter, mySrd).getId().toUnqualifiedVersionless();
	}


}
