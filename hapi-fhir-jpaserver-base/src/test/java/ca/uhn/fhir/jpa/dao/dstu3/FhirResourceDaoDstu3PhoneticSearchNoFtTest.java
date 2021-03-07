package ca.uhn.fhir.jpa.dao.dstu3;

import ca.uhn.fhir.context.phonetic.ApacheEncoder;
import ca.uhn.fhir.context.phonetic.PhoneticEncoderEnum;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.util.HapiExtensions;
import org.apache.commons.codec.language.Soundex;
import org.hl7.fhir.dstu3.model.Enumerations;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.SearchParameter;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class FhirResourceDaoDstu3PhoneticSearchNoFtTest extends BaseJpaDstu3Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoDstu3PhoneticSearchNoFtTest.class);
	public static final String GALE = "Gale";
	public static final String GAIL = "Gail";
	public static final String NAME_SOUNDEX_SP = "nameSoundex";
	public static final String ADDRESS_LINE_SOUNDEX_SP = "addressLineSoundex";
	private static final String BOB = "BOB";
	private static final String ADDRESS = "123 Nohili St";
	private static final String ADDRESS_CLOSE = "123 Nohily St";
	private static final String ADDRESS_FAR = "123 College St";

	@Autowired
	ISearchParamRegistry mySearchParamRegistry;

	@BeforeEach
	public void beforeDisableResultReuse() {
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.DISABLED);
		myDaoConfig.setReuseCachedSearchResultsForMillis(null);
		myDaoConfig.setFetchSizeDefaultMaximum(new DaoConfig().getFetchSizeDefaultMaximum());

		createSoundexSearchParameter(NAME_SOUNDEX_SP, PhoneticEncoderEnum.SOUNDEX, "Patient.name");
		createSoundexSearchParameter(ADDRESS_LINE_SOUNDEX_SP, PhoneticEncoderEnum.SOUNDEX, "Patient.address.line");
		mySearchParamRegistry.forceRefresh();
		mySearchParamRegistry.setPhoneticEncoder(new ApacheEncoder(PhoneticEncoderEnum.SOUNDEX.name(), new Soundex()));
	}

	@AfterEach
	public void resetStringEncoder() {
		mySearchParamRegistry.setPhoneticEncoder(null);
	}

	@Test
	public void testSoundex() {
		Soundex soundex = new Soundex();
		assertEquals(soundex.encode(GALE), soundex.encode(GAIL));
		assertNotEquals(soundex.encode(GALE), soundex.encode(BOB));
		assertEquals(soundex.encode(ADDRESS), soundex.encode(ADDRESS_CLOSE));
		assertNotEquals(soundex.encode(ADDRESS), soundex.encode(ADDRESS_FAR));
		ourLog.info("Encoded address: {}", soundex.encode(ADDRESS));
	}

	@Test
	public void phoneticMatch() {
		Patient patient;

		patient = new Patient();
		patient.addName().addGiven(GALE);
		patient.addAddress().addLine(ADDRESS);
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient));

		IIdType pId = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();

		List<ResourceIndexedSearchParamString> stringParams = myResourceIndexedSearchParamStringDao.findAll();

		assertThat(stringParams, hasSize(6));
		List<String> stringParamNames = stringParams.stream().map(ResourceIndexedSearchParamString::getParamName).collect(Collectors.toList());
		assertThat(stringParamNames, containsInAnyOrder(Patient.SP_NAME, Patient.SP_GIVEN, Patient.SP_PHONETIC, NAME_SOUNDEX_SP, Patient.SP_ADDRESS, ADDRESS_LINE_SOUNDEX_SP));

		assertSearchMatch(pId, Patient.SP_PHONETIC, GALE);
		assertSearchMatch(pId, Patient.SP_PHONETIC, GAIL);
		assertNoMatch(Patient.SP_PHONETIC, BOB);

		assertSearchMatch(pId, NAME_SOUNDEX_SP, GAIL);
		assertSearchMatch(pId, NAME_SOUNDEX_SP, GALE);
		assertNoMatch(NAME_SOUNDEX_SP, BOB);

		assertSearchMatch(pId, ADDRESS_LINE_SOUNDEX_SP, ADDRESS);
		assertSearchMatch(pId, ADDRESS_LINE_SOUNDEX_SP, ADDRESS_CLOSE);
		assertNoMatch(ADDRESS_LINE_SOUNDEX_SP, ADDRESS_FAR);
	}

	private void assertSearchMatch(IIdType thePId1, String theSp, String theValue) {
		SearchParameterMap map;
		map = new SearchParameterMap();
		map.add(theSp, new StringParam(theValue));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map)), contains(toValues(thePId1)));
	}

	private void assertNoMatch(String theSp, String theValue) {
		SearchParameterMap map;
		map = new SearchParameterMap();
		map.add(theSp, new StringParam(theValue));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map)), hasSize(0));
	}

	private void createSoundexSearchParameter(String theCode, PhoneticEncoderEnum theEncoder, String theFhirPath) {
		SearchParameter searchParameter = new SearchParameter();
		searchParameter.addBase("Patient");
		searchParameter.setCode(theCode);
		searchParameter.setType(Enumerations.SearchParamType.STRING);
		searchParameter.setTitle("Test Soundex");
		searchParameter.setExpression(theFhirPath);
// Maybe use in the future?  RuntimeSearchParam doesn't store this...
//		searchParameter.setXpathUsage(SearchParameter.XPathUsageType.PHONETIC);
		searchParameter.setStatus(Enumerations.PublicationStatus.ACTIVE);
		searchParameter.addExtension()
			.setUrl(HapiExtensions.EXT_SEARCHPARAM_PHONETIC_ENCODER)
			.setValue(new StringType(theEncoder.name()));
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(searchParameter));
		mySearchParameterDao.create(searchParameter, mySrd).getId().toUnqualifiedVersionless();
	}


}
