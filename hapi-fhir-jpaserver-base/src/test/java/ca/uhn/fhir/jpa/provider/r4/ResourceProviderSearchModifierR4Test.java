package ca.uhn.fhir.jpa.provider.r4;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.rest.gclient.IQuery;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Observation.ObservationStatus;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Quantity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.client.interceptor.CapturingInterceptor;


public class ResourceProviderSearchModifierR4Test extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderSearchModifierR4Test.class);
	private CapturingInterceptor myCapturingInterceptor = new CapturingInterceptor();

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();

		myDaoConfig.setAllowMultipleDelete(new DaoConfig().isAllowMultipleDelete());
		myDaoConfig.setAllowExternalReferences(new DaoConfig().isAllowExternalReferences());
		myDaoConfig.setReuseCachedSearchResultsForMillis(new DaoConfig().getReuseCachedSearchResultsForMillis());
		myDaoConfig.setCountSearchResultsUpTo(new DaoConfig().getCountSearchResultsUpTo());
		myDaoConfig.setSearchPreFetchThresholds(new DaoConfig().getSearchPreFetchThresholds());
		myDaoConfig.setAllowContainsSearches(new DaoConfig().isAllowContainsSearches());
		myDaoConfig.setIndexMissingFields(new DaoConfig().getIndexMissingFields());
		
		myClient.unregisterInterceptor(myCapturingInterceptor);
	}

	@BeforeEach
	@Override
	public void before() throws Exception {
		super.before();
		myFhirCtx.setParserErrorHandler(new StrictErrorHandler());

		myDaoConfig.setAllowMultipleDelete(true);
		myClient.registerInterceptor(myCapturingInterceptor);
		myDaoConfig.setSearchPreFetchThresholds(new DaoConfig().getSearchPreFetchThresholds());
	}

	@BeforeEach
	public void beforeDisableResultReuse() {
		myDaoConfig.setReuseCachedSearchResultsForMillis(null);
	}

	@Test
	public void testSearch_SingleCode_not_modifier() throws Exception {

		List<IIdType> obsList = createObs(10, false);
		
		String uri = ourServerBase + "/Observation?code:not=2345-3";
		List<String> ids = searchAndReturnUnqualifiedVersionlessIdValues(uri);
		
		assertEquals(9, ids.size());
		assertEquals(obsList.get(0).toString(), ids.get(0));
		assertEquals(obsList.get(1).toString(), ids.get(1));
		assertEquals(obsList.get(2).toString(), ids.get(2));
		assertEquals(obsList.get(4).toString(), ids.get(3));
		assertEquals(obsList.get(5).toString(), ids.get(4));
		assertEquals(obsList.get(6).toString(), ids.get(5));
		assertEquals(obsList.get(7).toString(), ids.get(6));
		assertEquals(obsList.get(8).toString(), ids.get(7));
		assertEquals(obsList.get(9).toString(), ids.get(8));
	}

	@Test
	public void testSearch_SingleCode_multiple_not_modifier() throws Exception {

		List<IIdType> obsList = createObs(10, false);
		
		String uri = ourServerBase + "/Observation?code:not=2345-3&code:not=2345-7&code:not=2345-9";
		List<String> ids = searchAndReturnUnqualifiedVersionlessIdValues(uri);
		
		assertEquals(7, ids.size());
		assertEquals(obsList.get(0).toString(), ids.get(0));
		assertEquals(obsList.get(1).toString(), ids.get(1));
		assertEquals(obsList.get(2).toString(), ids.get(2));
		assertEquals(obsList.get(4).toString(), ids.get(3));
		assertEquals(obsList.get(5).toString(), ids.get(4));
		assertEquals(obsList.get(6).toString(), ids.get(5));
		assertEquals(obsList.get(8).toString(), ids.get(6));
	}
	
	@Test
	public void testSearch_SingleCode_mix_modifier() throws Exception {

		List<IIdType> obsList = createObs(10, false);
		
		// Observation?code:not=2345-3&code:not=2345-7&code:not=2345-9
		// slower than Observation?code:not=2345-3&code=2345-7&code:not=2345-9
		String uri = ourServerBase + "/Observation?code:not=2345-3&code=2345-7&code:not=2345-9";
		List<String> ids = searchAndReturnUnqualifiedVersionlessIdValues(uri);
		
		assertEquals(1, ids.size());
		assertEquals(obsList.get(7).toString(), ids.get(0));
	}
	
	@Test
	public void testSearch_SingleCode_or_not_modifier() throws Exception {

		List<IIdType> obsList = createObs(10, false);
		
		String uri = ourServerBase + "/Observation?code:not=2345-3,2345-7,2345-9";
		List<String> ids = searchAndReturnUnqualifiedVersionlessIdValues(uri);
		
		assertEquals(7, ids.size());
		assertEquals(obsList.get(0).toString(), ids.get(0));
		assertEquals(obsList.get(1).toString(), ids.get(1));
		assertEquals(obsList.get(2).toString(), ids.get(2));
		assertEquals(obsList.get(4).toString(), ids.get(3));
		assertEquals(obsList.get(5).toString(), ids.get(4));
		assertEquals(obsList.get(6).toString(), ids.get(5));
		assertEquals(obsList.get(8).toString(), ids.get(6));
	}
	
	@Test
	public void testSearch_MultiCode_not_modifier() throws Exception {

		List<IIdType> obsList = createObs(10, true);
		
		String uri = ourServerBase + "/Observation?code:not=2345-3";
		List<String> ids = searchAndReturnUnqualifiedVersionlessIdValues(uri);
		
		assertEquals(8, ids.size());
		assertEquals(obsList.get(0).toString(), ids.get(0));
		assertEquals(obsList.get(1).toString(), ids.get(1));
		assertEquals(obsList.get(4).toString(), ids.get(2));
		assertEquals(obsList.get(5).toString(), ids.get(3));
		assertEquals(obsList.get(6).toString(), ids.get(4));
		assertEquals(obsList.get(7).toString(), ids.get(5));
		assertEquals(obsList.get(8).toString(), ids.get(6));
		assertEquals(obsList.get(9).toString(), ids.get(7));
	}
	
	@Test
	public void testSearch_MultiCode_multiple_not_modifier() throws Exception {

		List<IIdType> obsList = createObs(10, true);
		
		String uri = ourServerBase + "/Observation?code:not=2345-3&code:not=2345-4";
		List<String> ids = searchAndReturnUnqualifiedVersionlessIdValues(uri);
		
		assertEquals(7, ids.size());
		assertEquals(obsList.get(0).toString(), ids.get(0));
		assertEquals(obsList.get(1).toString(), ids.get(1));
		assertEquals(obsList.get(5).toString(), ids.get(2));
		assertEquals(obsList.get(6).toString(), ids.get(3));
		assertEquals(obsList.get(7).toString(), ids.get(4));
		assertEquals(obsList.get(8).toString(), ids.get(5));
		assertEquals(obsList.get(9).toString(), ids.get(6));
	}
	
	@Test
	public void testSearch_MultiCode_mix_modifier() throws Exception {

		List<IIdType> obsList = createObs(10, true);
		
		String uri = ourServerBase + "/Observation?code:not=2345-3&code=2345-7&code:not=2345-9";
		List<String> ids = searchAndReturnUnqualifiedVersionlessIdValues(uri);
		
		assertEquals(2, ids.size());
		assertEquals(obsList.get(6).toString(), ids.get(0));
		assertEquals(obsList.get(7).toString(), ids.get(1));
	}
	
	@Test
	public void testSearch_MultiCode_or_not_modifier() throws Exception {

		List<IIdType> obsList = createObs(10, true);
		
		String uri = ourServerBase + "/Observation?code:not=2345-3,2345-7,2345-9";
		List<String> ids = searchAndReturnUnqualifiedVersionlessIdValues(uri);
		
		assertEquals(4, ids.size());
		assertEquals(obsList.get(0).toString(), ids.get(0));
		assertEquals(obsList.get(1).toString(), ids.get(1));
		assertEquals(obsList.get(4).toString(), ids.get(2));
		assertEquals(obsList.get(5).toString(), ids.get(3));
	}

	@Test
	public void testMultiCodeNote() {
		String bundleString = "{\n" +
			"    \"resourceType\": \"Bundle\",\n" +
			"    \"id\": \"obx-bundle-106-000\",\n" +
			"    \"type\": \"batch\",\n" +
			"    \"entry\": [\n" +
			"        {\n" +
			"            \"request\": {\n" +
			"                \"method\": \"POST\",\n" +
			"                \"url\": \"Observation\"\n" +
			"            },\n" +
			"            \"resource\": {\n" +
			"                \"resourceType\": \"Observation\",\n" +
			"                \"status\": \"final\",\n" +
			"                \"identifier\": [\n" +
			"                    {\n" +
			"                        \"system\": \"1\",\n" +
			"                        \"value\": \"9876\"\n" +
			"                    },\n" +
			"                    {\n" +
			"                        \"system\": \"sid-row\",\n" +
			"                        \"value\": \"106-0\"\n" +
			"                    }\n" +
			"                ],\n" +
			"                \"code\": {\n" +
			"                    \"text\": \"Total Clinic Charge\",\n" +
			"                    \"coding\": [\n" +
			"                        {\n" +
			"                            \"system\": \"1\",\n" +
			"                            \"code\": \"9876\",\n" +
			"                            \"display\": \"Total Clinic Charge\"\n" +
			"                        },\n" +
			"                        {\n" +
			"                            \"system\": \"1.mc\",\n" +
			"                            \"code\": \"m9876\",\n" +
			"                            \"display\": \"MC Total Clinic Charge\"\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                \"category\": [\n" +
			"                    {\n" +
			"                        \"text\": \"Physician\",\n" +
			"                        \"coding\": [\n" +
			"                            {\n" +
			"                                \"code\": \"PHY\",\n" +
			"                                \"display\": \"Physician\"\n" +
			"                            }\n" +
			"                        ]\n" +
			"                    }\n" +
			"                ],\n" +
			"                \"effectiveDateTime\": \"2141-06-18T21:00:00.000Z\",\n" +
			"                \"valueQuantity\": {\n" +
			"                    \"value\": 28\n" +
			"                },\n" +
			"                \"meta\": {\n" +
			"                    \"tag\": [\n" +
			"                        {\n" +
			"                            \"system\": \"urn:lf:fhir-tags\",\n" +
			"                            \"code\": \"multi-codes\"\n" +
			"                        }\n" +
			"                    ]\n" +
			"                }\n" +
			"            }\n" +
			"        },\n" +
			"        {\n" +
			"            \"request\": {\n" +
			"                \"method\": \"POST\",\n" +
			"                \"url\": \"Observation\"\n" +
			"            },\n" +
			"            \"resource\": {\n" +
			"                \"resourceType\": \"Observation\",\n" +
			"                \"status\": \"final\",\n" +
			"                \"identifier\": [\n" +
			"                    {\n" +
			"                        \"system\": \"1\",\n" +
			"                        \"value\": \"9876\"\n" +
			"                    },\n" +
			"                    {\n" +
			"                        \"system\": \"sid-row\",\n" +
			"                        \"value\": \"106-1\"\n" +
			"                    }\n" +
			"                ],\n" +
			"                \"code\": {\n" +
			"                    \"text\": \"Total Clinic Charge\",\n" +
			"                    \"coding\": [\n" +
			"                        {\n" +
			"                            \"system\": \"1\",\n" +
			"                            \"code\": \"9876\",\n" +
			"                            \"display\": \"Total Clinic Charge\"\n" +
			"                        },\n" +
			"                        {\n" +
			"                            \"system\": \"1.mc\",\n" +
			"                            \"code\": \"m9876\",\n" +
			"                            \"display\": \"MC Total Clinic Charge\"\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                \"category\": [\n" +
			"                    {\n" +
			"                        \"text\": \"Physician\",\n" +
			"                        \"coding\": [\n" +
			"                            {\n" +
			"                                \"code\": \"PHY\",\n" +
			"                                \"display\": \"Physician\"\n" +
			"                            }\n" +
			"                        ]\n" +
			"                    }\n" +
			"                ],\n" +
			"                \"effectiveDateTime\": \"2141-05-15T18:05:00.000Z\",\n" +
			"                \"valueQuantity\": {\n" +
			"                    \"value\": 38\n" +
			"                },\n" +
			"                \"meta\": {\n" +
			"                    \"tag\": [\n" +
			"                        {\n" +
			"                            \"system\": \"urn:lf:fhir-tags\",\n" +
			"                            \"code\": \"multi-codes\"\n" +
			"                        }\n" +
			"                    ]\n" +
			"                }\n" +
			"            }\n" +
			"        },\n" +
			"        {\n" +
			"            \"request\": {\n" +
			"                \"method\": \"POST\",\n" +
			"                \"url\": \"Observation\"\n" +
			"            },\n" +
			"            \"resource\": {\n" +
			"                \"resourceType\": \"Observation\",\n" +
			"                \"status\": \"final\",\n" +
			"                \"identifier\": [\n" +
			"                    {\n" +
			"                        \"system\": \"1\",\n" +
			"                        \"value\": \"9876\"\n" +
			"                    },\n" +
			"                    {\n" +
			"                        \"system\": \"sid-row\",\n" +
			"                        \"value\": \"106-2\"\n" +
			"                    }\n" +
			"                ],\n" +
			"                \"code\": {\n" +
			"                    \"text\": \"Total Clinic Charge\",\n" +
			"                    \"coding\": [\n" +
			"                        {\n" +
			"                            \"system\": \"1\",\n" +
			"                            \"code\": \"9876\",\n" +
			"                            \"display\": \"Total Clinic Charge\"\n" +
			"                        },\n" +
			"                        {\n" +
			"                            \"system\": \"1.MC\",\n" +
			"                            \"code\": \"M2-9876\",\n" +
			"                            \"display\": \"MC too Total Clinic Charge\"\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                \"category\": [\n" +
			"                    {\n" +
			"                        \"text\": \"Physician\",\n" +
			"                        \"coding\": [\n" +
			"                            {\n" +
			"                                \"code\": \"PHY\",\n" +
			"                                \"display\": \"Physician\"\n" +
			"                            }\n" +
			"                        ]\n" +
			"                    }\n" +
			"                ],\n" +
			"                \"effectiveDateTime\": \"2141-01-16T18:05:00.000Z\",\n" +
			"                \"valueQuantity\": {\n" +
			"                    \"value\": 33\n" +
			"                },\n" +
			"                \"meta\": {\n" +
			"                    \"tag\": [\n" +
			"                        {\n" +
			"                            \"system\": \"urn:lf:fhir-tags\",\n" +
			"                            \"code\": \"multi-codes\"\n" +
			"                        }\n" +
			"                    ]\n" +
			"                }\n" +
			"            }\n" +
			"        },\n" +
			"        {\n" +
			"            \"request\": {\n" +
			"                \"method\": \"POST\",\n" +
			"                \"url\": \"Observation\"\n" +
			"            },\n" +
			"            \"resource\": {\n" +
			"                \"resourceType\": \"Observation\",\n" +
			"                \"status\": \"final\",\n" +
			"                \"identifier\": [\n" +
			"                    {\n" +
			"                        \"system\": \"1\",\n" +
			"                        \"value\": \"12984\"\n" +
			"                    },\n" +
			"                    {\n" +
			"                        \"system\": \"sid-row\",\n" +
			"                        \"value\": \"106-3\"\n" +
			"                    }\n" +
			"                ],\n" +
			"                \"code\": {\n" +
			"                    \"text\": \"Oral temp F\",\n" +
			"                    \"coding\": [\n" +
			"                        {\n" +
			"                            \"system\": \"http://loinc.org\",\n" +
			"                            \"code\": \"8331-1\",\n" +
			"                            \"display\": \"Oral temp F\"\n" +
			"                        },\n" +
			"                        {\n" +
			"                            \"system\": \"http://loinc.org.mc\",\n" +
			"                            \"code\": \"m8331-1\",\n" +
			"                            \"display\": \"MC Oral temp F\"\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                \"category\": [\n" +
			"                    {\n" +
			"                        \"text\": \"Vital Signs\",\n" +
			"                        \"coding\": [\n" +
			"                            {\n" +
			"                                \"code\": \"vital-signs\",\n" +
			"                                \"display\": \"Vital Signs\"\n" +
			"                            }\n" +
			"                        ]\n" +
			"                    }\n" +
			"                ],\n" +
			"                \"effectiveDateTime\": \"2141-01-01T07:43:00.000Z\",\n" +
			"                \"valueQuantity\": {\n" +
			"                    \"value\": 98.95,\n" +
			"                    \"system\": \"http://unitsofmeasure.org\",\n" +
			"                    \"code\": \"[degF]\",\n" +
			"                    \"unit\": \"F\"\n" +
			"                },\n" +
			"                \"referenceRange\": [\n" +
			"                    {\n" +
			"                        \"low\": {\n" +
			"                            \"value\": 97.6,\n" +
			"                            \"system\": \"http://unitsofmeasure.org\",\n" +
			"                            \"code\": \"[degF]\",\n" +
			"                            \"unit\": \"F\"\n" +
			"                        },\n" +
			"                        \"high\": {\n" +
			"                            \"value\": 99,\n" +
			"                            \"system\": \"http://unitsofmeasure.org\",\n" +
			"                            \"code\": \"[degF]\",\n" +
			"                            \"unit\": \"F\"\n" +
			"                        },\n" +
			"                        \"text\": \"97.6 - 99\"\n" +
			"                    }\n" +
			"                ],\n" +
			"                \"interpretation\": [\n" +
			"                    {\n" +
			"                        \"coding\": [\n" +
			"                            {\n" +
			"                                \"system\": \"http://hl7.org/fhir/v2/0078\",\n" +
			"                                \"code\": \"N\",\n" +
			"                                \"display\": \"Normal\"\n" +
			"                            }\n" +
			"                        ]\n" +
			"                    }\n" +
			"                ],\n" +
			"                \"meta\": {\n" +
			"                    \"tag\": [\n" +
			"                        {\n" +
			"                            \"system\": \"urn:lf:fhir-tags\",\n" +
			"                            \"code\": \"multi-codes\"\n" +
			"                        }\n" +
			"                    ]\n" +
			"                }\n" +
			"            }\n" +
			"        }\n" +
			"    ]\n" +
			"}\n";
		Bundle bundle = myFhirCtx.newJsonParser().parseResource(Bundle.class, bundleString);
		Bundle transaction = mySystemDao.transaction(new SystemRequestDetails(), bundle);
		//When
		Bundle execute = myClient.search().byUrl("Observation?_tag=multi-codes&_elements=code").returnBundle(Bundle.class).execute();
		//Then
		assertEquals(execute.getEntry().size(), 4);


		//When
		execute = myClient.search().byUrl("Observation?_tag=multi-codes&code:not=m9876&_elements=code").returnBundle(Bundle.class).execute();
		//Then
		assertEquals(execute.getEntry().size(), 2);

	}


	
	private List<String> searchAndReturnUnqualifiedVersionlessIdValues(String uri) throws IOException {
		List<String> ids;
		HttpGet get = new HttpGet(uri);

		try (CloseableHttpResponse response = ourHttpClient.execute(get)) {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("Response was: {}", resp);
			Bundle bundle = myFhirCtx.newXmlParser().parseResource(Bundle.class, resp);
			ourLog.info("Bundle: \n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));
			ids = toUnqualifiedVersionlessIdValues(bundle);
		}
		return ids;
	}

	private List<IIdType> createObs(int obsNum, boolean isMultiple) {
		
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("001");
		patient.addName().setFamily("Tester").addGiven("Joe");
		IIdType pid = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		
		List<IIdType> obsIds = new ArrayList<>();
		IIdType obsId = null;
		for (int i=0; i<obsNum; i++) {
			Observation obs = new Observation();
			obs.setStatus(ObservationStatus.FINAL);
			obs.getSubject().setReferenceElement(pid);
			obs.setEffective(new DateTimeType("2001-02-01"));
		
			CodeableConcept cc = obs.getCode();
			cc.addCoding().setCode("2345-"+i).setSystem("http://loinc.org");
			obs.setValue(new Quantity().setValue(200));	

			if (isMultiple) {
				cc = obs.getCode();
				cc.addCoding().setCode("2345-"+(i+1)).setSystem("http://loinc.org");
				obs.setValue(new Quantity().setValue(300));	
			}

			obsId = myObservationDao.create(obs).getId().toUnqualifiedVersionless();
			obsIds.add(obsId);
		}
		
		return obsIds;
	}
}
