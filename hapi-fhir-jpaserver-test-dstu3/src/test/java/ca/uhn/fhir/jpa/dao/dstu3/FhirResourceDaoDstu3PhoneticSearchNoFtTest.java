package ca.uhn.fhir.jpa.dao.dstu3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.context.phonetic.ApacheEncoder;
import ca.uhn.fhir.context.phonetic.NumericEncoder;
import ca.uhn.fhir.context.phonetic.PhoneticEncoderEnum;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaDstu3Test;
import ca.uhn.fhir.jpa.test.config.TestHSearchAddInConfig;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
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
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = TestHSearchAddInConfig.NoFT.class)
public class FhirResourceDaoDstu3PhoneticSearchNoFtTest extends BaseJpaDstu3Test {
	public static final String GALE = "Gale";
	public static final String GAIL = "Gail";
	public static final String NAME_SOUNDEX_SP = "nameSoundex";
	public static final String ADDRESS_LINE_SOUNDEX_SP = "addressLineSoundex";
	public static final String PHONE_NUMBER_SP = "phoneNumber";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoDstu3PhoneticSearchNoFtTest.class);
	private static final String BOB = "BOB";
	private static final String ADDRESS = "123 Nohili St";
	private static final String ADDRESS_CLOSE = "123 Nohily St";
	private static final String ADDRESS_FAR = "123 College St";
	private static final String PHONE = "4169671111";
	private static final String PHONE_CLOSE = "(416) 967-1111";
	private static final String PHONE_FAR = "416 421 0421";

	@Autowired
	ISearchParamRegistry mySearchParamRegistry;

	@BeforeEach
	public void beforeDisableResultReuse() {
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.DISABLED);
		myStorageSettings.setReuseCachedSearchResultsForMillis(null);
		myStorageSettings.setFetchSizeDefaultMaximum(new JpaStorageSettings().getFetchSizeDefaultMaximum());

		createPhoneticSearchParameter(NAME_SOUNDEX_SP, PhoneticEncoderEnum.SOUNDEX, "Patient.name");
		createPhoneticSearchParameter(ADDRESS_LINE_SOUNDEX_SP, PhoneticEncoderEnum.SOUNDEX, "Patient.address.line");
		createPhoneticSearchParameter(PHONE_NUMBER_SP, PhoneticEncoderEnum.NUMERIC, "Patient.telecom");
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

		// The tests below depend on these assumptions:
		assertEquals(soundex.encode(GALE), soundex.encode(GAIL));
		assertThat(GALE).isNotEqualTo(soundex.encode(GALE));
		assertThat(GALE).isNotEqualTo(soundex.encode(GAIL));
		assertThat(GAIL).isNotEqualTo(soundex.encode(GALE));
		assertThat(GAIL).isNotEqualTo(soundex.encode(GAIL));
		ourLog.info("Encoded Gale: {}", soundex.encode(GALE));
		ourLog.info("Encoded Gail: {}", soundex.encode(GAIL));
		assertThat(soundex.encode(BOB)).isNotEqualTo(soundex.encode(GALE));
		assertEquals(soundex.encode(ADDRESS), soundex.encode(ADDRESS_CLOSE));
		assertThat(soundex.encode(ADDRESS_FAR)).isNotEqualTo(soundex.encode(ADDRESS));
		ourLog.info("Encoded address: {}", soundex.encode(ADDRESS));
	}

	@Test
	public void testNumeric() {
		NumericEncoder numeric = new NumericEncoder();
		assertEquals(PHONE, numeric.encode(PHONE_CLOSE));
		assertEquals(PHONE, numeric.encode(PHONE));
		assertEquals(numeric.encode(PHONE), numeric.encode(PHONE_CLOSE));
		assertThat(numeric.encode(PHONE_FAR)).isNotEqualTo(numeric.encode(PHONE));
	}

	@Test
	public void phoneticMatch() {
		Patient patient = new Patient();
		patient.addName().addGiven(GALE);
		patient.addAddress().addLine(ADDRESS);
		patient.addTelecom().setValue(PHONE);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient));

		IIdType pId = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();

		List<ResourceIndexedSearchParamString> stringParams = myResourceIndexedSearchParamStringDao.findAll();

		assertThat(stringParams).hasSize(7);
		List<String> stringParamNames = stringParams.stream().map(ResourceIndexedSearchParamString::getParamName).collect(Collectors.toList());
		assertThat(stringParamNames).containsExactlyInAnyOrder(Patient.SP_NAME, Patient.SP_GIVEN, Patient.SP_PHONETIC, NAME_SOUNDEX_SP, Patient.SP_ADDRESS, ADDRESS_LINE_SOUNDEX_SP, PHONE_NUMBER_SP);

		assertSearchMatch(pId, Patient.SP_PHONETIC, GALE);
		assertSearchMatch(pId, Patient.SP_PHONETIC, GAIL);
		assertNoMatch(Patient.SP_PHONETIC, BOB);

		assertSearchMatch(pId, NAME_SOUNDEX_SP, GAIL);
		assertSearchMatch(pId, NAME_SOUNDEX_SP, GALE);
		assertNoMatch(NAME_SOUNDEX_SP, BOB);

		assertSearchMatch(pId, ADDRESS_LINE_SOUNDEX_SP, ADDRESS);
		assertSearchMatch(pId, ADDRESS_LINE_SOUNDEX_SP, ADDRESS_CLOSE);
		assertNoMatch(ADDRESS_LINE_SOUNDEX_SP, ADDRESS_FAR);

		assertSearchMatch(pId, PHONE_NUMBER_SP, PHONE);
		assertSearchMatch(pId, PHONE_NUMBER_SP, PHONE_CLOSE);
		assertNoMatch(PHONE_NUMBER_SP, PHONE_FAR);
	}

	@Test
	public void phoneticSearch() {
		// setup
		Patient patient = new Patient();
		patient.addName().addGiven(GALE);
		patient.addAddress().addLine(ADDRESS);
		patient.addTelecom().setValue(PHONE);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient));

		IIdType patientId = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap map = SearchParameterMap.newSynchronous();
		// Search for a different name from the original value that has the same soundex value
		map.add(NAME_SOUNDEX_SP, new StringParam(GAIL));

		// execute
		IBundleProvider result = myPatientDao.search(map, mySrd);

		// verify
		List<String> resultIds = result.getAllResourceIds();
		assertThat(resultIds).hasSize(1);
		assertEquals(patientId.getIdPart(), resultIds.get(0));
	}

	private void assertSearchMatch(IIdType thePId1, String theSp, String theValue) {
		SearchParameterMap map;
		map = new SearchParameterMap();
		map.add(theSp, new StringParam(theValue));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map))).containsExactly(toValues(thePId1));
	}

	private void assertNoMatch(String theSp, String theValue) {
		SearchParameterMap map;
		map = new SearchParameterMap();
		map.add(theSp, new StringParam(theValue));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map))).hasSize(0);
	}

	private void createPhoneticSearchParameter(String theCode, PhoneticEncoderEnum theEncoder, String theFhirPath) {
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
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(searchParameter));
		mySearchParameterDao.create(searchParameter, mySrd).getId().toUnqualifiedVersionless();
	}


}
