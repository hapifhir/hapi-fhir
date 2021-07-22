package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.model.entity.ResourceIndexedComboTokenNonUnique;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.util.HapiExtensions;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Enumerations.PublicationStatus;
import org.hl7.fhir.r4.model.OrganizationAffiliation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FhirResourceDaoR4ComboNonUniqueParamTest extends BaseComboParamsR4Test {

	private void createNamesAndGenderSp() {
		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/patient-family");
		sp.setType(Enumerations.SearchParamType.STRING);
		sp.setCode("family");
		sp.setExpression("Patient.name.family + '|'");
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		mySearchParameterDao.update(sp);

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-given");
		sp.setType(Enumerations.SearchParamType.STRING);
		sp.setCode("given");
		sp.setExpression("Patient.name.given");
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		mySearchParameterDao.update(sp);

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-gender");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setCode("gender");
		sp.setExpression("Patient.gender");
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		mySearchParameterDao.update(sp);

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-names-and-gender");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("SearchParameter/patient-family");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("SearchParameter/patient-given");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("SearchParameter/patient-gender");
		sp.addExtension()
			.setUrl(HapiExtensions.EXT_SP_UNIQUE)
			.setValue(new BooleanType(false));
		mySearchParameterDao.update(sp);

		mySearchParamRegistry.forceRefresh();

		myMessages.clear();
	}


	@Test
	public void testExample() {
		String input = "{\n" +
			"  \"resourceType\": \"SearchParameter\",\n" +
			"  \"id\": \"metatagcustomerinternalid-OrganizationAffiliation\",\n" +
			"  \"url\": \"http://localhost:8000/SearchParameter/metatagcustomerinternalid-OrganizationAffiliation\",\n" +
			"  \"name\": \"metatagcustomerinternalidOrganizationAffiliation\",\n" +
			"  \"status\": \"active\",\n" +
			"  \"description\": \"Meta tag Customer Internal Id\",\n" +
			"  \"code\": \"metatagcustomerinternalid\",\n" +
			"  \"base\": [ \"OrganizationAffiliation\" ],\n" +
			"  \"type\": \"string\",\n" +
			"  \"expression\": \"OrganizationAffiliation.meta.tag.where(system = 'http://changehealthcare.com/interop/hdc/customer_internal_id').code\",\n" +
			"  \"xpathUsage\": \"normal\"\n" +
			"}";
		mySearchParameterDao.update(myFhirCtx.newJsonParser().parseResource(SearchParameter.class, input));

		input = "{\n" +
			"  \"resourceType\": \"SearchParameter\",\n" +
			"  \"id\": \"metataglocationprovider-OrganizationAffiliation\",\n" +
			"  \"url\": \"http://localhost:8000/SearchParameter/metataglocationprovider-OrganizationAffiliation\",\n" +
			"  \"name\": \"metataglocationproviderOrganizationAffiliation\",\n" +
			"  \"status\": \"active\",\n" +
			"  \"description\": \"Meta tag file location for provider\",\n" +
			"  \"code\": \"metataglocationprovider\",\n" +
			"  \"base\": [ \"OrganizationAffiliation\" ],\n" +
			"  \"type\": \"string\",\n" +
			"  \"expression\": \"OrganizationAffiliation.meta.tag.where(system = 'http://changehealthcare.com/interop/hdc/file_location' and code.endsWith('provider')).system + '|'\",\n" +
			"  \"xpathUsage\": \"normal\"\n" +
			"}";
		mySearchParameterDao.update(myFhirCtx.newJsonParser().parseResource(SearchParameter.class, input));

		input = "{\n" +
			"  \"resourceType\": \"SearchParameter\",\n" +
			"  \"id\": \"foo\",\n" +
			"  \"title\": \"Meta tag tdt_epoch\",\n" +
			"  \"status\": \"active\",\n" +
			"  \"code\": \"metatagtdtepoch\",\n" +
			"  \"base\": [ \"OrganizationAffiliation\" ],\n" +
			"  \"type\": \"string\",\n" +
			"  \"expression\": \"InsurancePlan.meta.tag.where(system = 'http://changehealthcare.com/interop/hdc/tdt_epoch').code | HealthcareService.meta.tag.where(system = 'http://changehealthcare.com/interop/hdc/tdt_epoch').code | Location.meta.tag.where(system = 'http://changehealthcare.com/interop/hdc/tdt_epoch').code | Practitioner.meta.tag.where(system = 'http://changehealthcare.com/interop/hdc/tdt_epoch').code | PractitionerRole.meta.tag.where(system = 'http://changehealthcare.com/interop/hdc/tdt_epoch').code | Organization.meta.tag.where(system = 'http://changehealthcare.com/interop/hdc/tdt_epoch').code | OrganizationAffiliation.meta.tag.where(system = 'http://changehealthcare.com/interop/hdc/tdt_epoch').code | Endpoint.meta.tag.where(system = 'http://changehealthcare.com/interop/hdc/tdt_epoch').code\",\n" +
			"  \"xpathUsage\": \"normal\"\n" +
			"}\n" +
			"\n";
		mySearchParameterDao.update(myFhirCtx.newJsonParser().parseResource(SearchParameter.class, input));

		input = "{\n" +
			"  \"resourceType\": \"SearchParameter\",\n" +
			"  \"id\": \"metacombo-OrganizationAffiliation\",\n" +
			"  \"extension\": [\n" +
			"    {\n" +
			"      \"url\": \"http://hapifhir.io/fhir/StructureDefinition/sp-unique\",\n" +
			"      \"valueBoolean\": false\n" +
			"    }\n" +
			"  ],\n" +
			"  \"status\": \"active\",\n" +
			"  \"base\": [\n" +
			"    \"OrganizationAffiliation\"\n" +
			"  ],\n" +
			"  \"type\": \"composite\",\n" +
			"  \"component\": [\n" +
			"    {\n" +
			"      \"definition\": \"SearchParameter/metatagcustomerinternalid-OrganizationAffiliation\"\n" +
			"},\n" +
			"    {\n" +
			"      \"definition\": \"SearchParameter/metataglocationprovider-OrganizationAffiliation\"\n" +
			"    },\n" +
			"    {\n" +
			"      \"definition\": \"SearchParameter/foo\"\n" +
			"    }\n" +
			"  ]\n" +
			"}\n" +
			"\n";
		mySearchParameterDao.update(myFhirCtx.newJsonParser().parseResource(SearchParameter.class, input));

		mySearchParamRegistry.forceRefresh();

		input = "{\n" +
			"      \"resourceType\": \"OrganizationAffiliation\",\n" +
			"      \"meta\": {\n" +
			"        \"lastUpdated\": \"2021-07-19T03:31:21.685+00:00\",\n" +
			"        \"profile\": [ \"http://hl7.org/fhir/us/davinci-pdex-plan-net/StructureDefinition/plannet-OrganizationAffiliation\" ],\n" +
			"        \"tag\": [ {\n" +
			"          \"system\": \"http://changehealthcare.com/interop/hdc/sender_id\",\n" +
			"          \"code\": \"2017886651f34e09800443e816e4017f\"\n" +
			"        }, {\n" +
			"          \"system\": \"http://changehealthcare.com/interop/hdc/usageindicator\",\n" +
			"          \"code\": \"t\"\n" +
			"        }, {\n" +
			"          \"system\": \"http://changehealthcare.com/interop/hdc/record_type\",\n" +
			"          \"code\": \"A\"\n" +
			"        }, {\n" +
			"          \"system\": \"http://changehealthcare.com/interop/hdc/date_time_reported\",\n" +
			"          \"code\": \"2021-06-08 11:35:24\"\n" +
			"        }, {\n" +
			"          \"system\": \"http://changehealthcare.com/interop/hdc/tdt_epoch\",\n" +
			"          \"code\": \"1626658214379\"\n" +
			"        }, {\n" +
			"          \"system\": \"http://changehealthcare.com/interop/hdc/tdt_iteration\",\n" +
			"          \"code\": \"1\"\n" +
			"        }, {\n" +
			"          \"system\": \"http://changehealthcare.com/interop/hdc/record_uuid\",\n" +
			"          \"code\": \"1b24f259-3874-4f81-b00b-a186dcbdb657\"\n" +
			"        }, {\n" +
			"          \"system\": \"http://changehealthcare.com/interop/hdc/file_location\",\n" +
			"          \"code\": \"s3://enterprise-datalake-ingress-preprod-us-east-1/interoperability/valid/provider\"\n" +
			"        }, {\n" +
			"          \"system\": \"http://changehealthcare.com/interop/hdc/customer_internal_id\",\n" +
			"          \"code\": \"7616A5EC4B344DEBA0B2A24DDC57B45F\"\n" +
			"        } ]\n" +
			"      },\n" +
			"      \"contained\": [ {\n" +
			"        \"resourceType\": \"Organization\",\n" +
			"        \"id\": \"OrganizationAffiliation-participatingOrganization-id-COUNSELING\",\n" +
			"        \"identifier\": [ {\n" +
			"          \"system\": \"http://hl7.org/fhir/sid/us-npi\",\n" +
			"          \"value\": \"0000000000\"\n" +
			"         }, {\n" +
			"          \"system\": \"http://hl7.org/fhir/sid/us-clia\"\n" +
			"        } ],\n" +
			"        \"active\": true,\n" +
			"        \"type\": [ {\n" +
			"          \"coding\": [ {\n" +
			"            \"code\": \"fac\"\n" +
			"          } ]\n" +
			"        } ],\n" +
			"        \"name\": \"COUNSELING\",\n" +
			"        \"telecom\": [ {\n" +
			"          \"system\": \"phone\",\n" +
			"          \"value\": \"1111111111\"\n" +
			"        } ],\n" +
			"        \"address\": [ {\n" +
			"          \"use\": \"work\",\n" +
			"          \"type\": \"physical\",\n" +
			"          \"text\": \"11111\",\n" +
			"          \"line\": [ \"11111\", \"AAAAA\", \"FL\", \"02926\" ],\n" +
			"          \"city\": \"AAAAA\",\n" +
			"          \"state\": \"FL\",\n" +
			"          \"postalCode\": \"02926\"\n" +
			"        } ]\n" +
			"      } ],\n" +
			"      \"active\": true,\n" +
			"      \"period\": {\n" +
			"        \"start\": \"2001-01-01T05:00:00.000Z\",\n" +
			"        \"end\": \"2018-12-01T05:00:00.000Z\"\n" +
			"      },\n" +
			"      \"participatingOrganization\": {\n" +
			"        \"reference\": \"#OrganizationAffiliation-participatingOrganization-id-COUNSELING\"\n" +
			"      },\n" +
			"      \"telecom\": [ {\n" +
			"        \"system\": \"phone\",\n" +
			"        \"value\": \"1111111111\"\n" +
			"      } ]\n" +
			"    }";
		myOrganizationAffiliationDao.create(myFhirCtx.newJsonParser().parseResource(OrganizationAffiliation.class, input));


		logAllNonUniqueIndexes();


	}



	@Test
	public void testCreateAndUse() {
		createNamesAndGenderSp();

		IIdType id1 = createPatient1();
		assertNotNull(id1);

		IIdType id2 = createPatient2();
		assertNotNull(id2);

		logAllNonUniqueIndexes();
		runInTransaction(() -> {
			List<ResourceIndexedComboTokenNonUnique> indexedTokens = myResourceIndexedComboTokensNonUniqueDao.findAll();
			indexedTokens.sort(Comparator.comparing(t -> t.getId()));
			assertEquals(2, indexedTokens.size());
			assertEquals(-7504889232313729794L, indexedTokens.get(0).getHashComplete().longValue());
		});

		myMessages.clear();
		SearchParameterMap params = SearchParameterMap.newSynchronous();
		params.add("family", new StringParam("fAmIlY1|")); // weird casing to test normalization
		params.add("given", new StringParam("gIVEn1"));
		params.add("gender", new TokenParam("http://hl7.org/fhir/administrative-gender", "male"));
		myCaptureQueriesListener.clear();
		IBundleProvider results = myPatientDao.search(params, mySrd);
		List<String> actual = toUnqualifiedVersionlessIdValues(results);
		myCaptureQueriesListener.logSelectQueries();
		assertThat(actual, containsInAnyOrder(id1.toUnqualifiedVersionless().getValue()));

		String sql = myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false);
		assertEquals("SELECT t0.RES_ID FROM HFJ_IDX_CMB_TOK_NU t0 WHERE (t0.IDX_STRING = 'Patient?family=FAMILY1%5C%7C&gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale&given=GIVEN1')", sql);

		logCapturedMessages();
		assertThat(myMessages.toString(), containsString("[INFO Using NON_UNIQUE index for query for search: Patient?family=FAMILY1%5C%7C&gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale&given=GIVEN1]"));
		myMessages.clear();

		// Remove 1, add another

		myPatientDao.delete(id1);

		IIdType id3 = createPatient1();
		assertNotNull(id3);

		params = SearchParameterMap.newSynchronous();
		params.add("family", new StringParam("fAmIlY1|")); // weird casing to test normalization
		params.add("given", new StringParam("gIVEn1"));
		params.add("gender", new TokenParam("http://hl7.org/fhir/administrative-gender", "male"));
		results = myPatientDao.search(params, mySrd);
		actual = toUnqualifiedVersionlessIdValues(results);
		myCaptureQueriesListener.logSelectQueries();
		assertThat(actual, containsInAnyOrder(id3.toUnqualifiedVersionless().getValue()));

	}

	@Test
	public void testSearchWithExtraParameters() {
		createNamesAndGenderSp();

		IIdType id1 = createPatient1();
		assertNotNull(id1);

		IIdType id2 = createPatient2();
		assertNotNull(id2);

		logAllNonUniqueIndexes();
		runInTransaction(() -> {
			List<ResourceIndexedComboTokenNonUnique> indexedTokens = myResourceIndexedComboTokensNonUniqueDao.findAll();
			indexedTokens.sort(Comparator.comparing(t -> t.getId()));
			assertEquals(2, indexedTokens.size());
			assertEquals(-7504889232313729794L, indexedTokens.get(0).getHashComplete().longValue());
		});

		myMessages.clear();
		SearchParameterMap params = SearchParameterMap.newSynchronous();
		params.add("family", new StringParam("fAmIlY1|")); // weird casing to test normalization
		params.add("given", new StringParam("gIVEn1"));
		params.add("gender", new TokenParam("http://hl7.org/fhir/administrative-gender", "male"));
		params.add("birthdate", new DateParam("2021-02-02"));
		myCaptureQueriesListener.clear();
		IBundleProvider results = myPatientDao.search(params, mySrd);
		List<String> actual = toUnqualifiedVersionlessIdValues(results);
		myCaptureQueriesListener.logSelectQueries();
		assertThat(actual, containsInAnyOrder(id1.toUnqualifiedVersionless().getValue()));

		String sql = myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false);
		assertEquals("SELECT t1.RES_ID FROM HFJ_RESOURCE t1 LEFT OUTER JOIN HFJ_IDX_CMB_TOK_NU t0 ON (t1.RES_ID = t0.RES_ID) LEFT OUTER JOIN HFJ_SPIDX_DATE t2 ON (t1.RES_ID = t2.RES_ID) WHERE ((t0.IDX_STRING = 'Patient?family=FAMILY1%5C%7C&gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale&given=GIVEN1') AND ((t2.HASH_IDENTITY = '5247847184787287691') AND ((t2.SP_VALUE_LOW_DATE_ORDINAL >= '20210202') AND (t2.SP_VALUE_HIGH_DATE_ORDINAL <= '20210202'))))", sql);

		logCapturedMessages();
		assertThat(myMessages.toString(), containsString("[INFO Using NON_UNIQUE index for query for search: Patient?family=FAMILY1%5C%7C&gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale&given=GIVEN1]"));
		myMessages.clear();

	}


	private IIdType createPatient2() {
		Patient pt2 = new Patient();
		pt2.getNameFirstRep().setFamily("Family2").addGiven("Given2");
		pt2.setGender(Enumerations.AdministrativeGender.MALE);
		pt2.setBirthDateElement(new DateType("2021-02-02"));
		IIdType id2 = myPatientDao.create(pt2).getId().toUnqualified();
		return id2;
	}

	private IIdType createPatient1() {
		Patient pt1 = new Patient();
		pt1.getNameFirstRep().setFamily("Family1").addGiven("Given1");
		pt1.setGender(Enumerations.AdministrativeGender.MALE);
		pt1.setBirthDateElement(new DateType("2021-02-02"));
		return myPatientDao.create(pt1).getId().toUnqualified();
	}


}
