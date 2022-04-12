package ca.uhn.fhir.jpa.provider.dstu3;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.entity.ResourceReindexJobEntity;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.gclient.ReferenceClientParam;
import ca.uhn.fhir.rest.gclient.TokenClientParam;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.dstu3.model.Appointment;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.CapabilityStatement;
import org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestComponent;
import org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestResourceComponent;
import org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestResourceSearchParamComponent;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Observation.ObservationStatus;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.SearchParameter;
import org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

public class ResourceProviderCustomSearchParamDstu3Test extends BaseResourceProviderDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderCustomSearchParamDstu3Test.class);

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();

		myModelConfig.setDefaultSearchParamsCanBeOverridden(new ModelConfig().isDefaultSearchParamsCanBeOverridden());
	}

	@Override
	@BeforeEach
	public void beforeResetConfig() {
		super.beforeResetConfig();

		myModelConfig.setDefaultSearchParamsCanBeOverridden(new ModelConfig().isDefaultSearchParamsCanBeOverridden());
		mySearchParamRegistry.forceRefresh();
	}

	private Map<String, CapabilityStatementRestResourceSearchParamComponent> extractSearchParams(CapabilityStatement conformance, String resType) {
		Map<String, CapabilityStatementRestResourceSearchParamComponent> map = new HashMap<>();
		for (CapabilityStatementRestComponent nextRest : conformance.getRest()) {
			for (CapabilityStatementRestResourceComponent nextResource : nextRest.getResource()) {
				if (!resType.equals(nextResource.getType())) {
					continue;
				}
				for (CapabilityStatementRestResourceSearchParamComponent nextParam : nextResource.getSearchParam()) {
					map.put(nextParam.getName(), nextParam);
				}
			}
		}
		return map;
	}

	@Test
	public void saveCreateSearchParamInvalidWithMissingStatus() throws IOException {
		SearchParameter sp = new SearchParameter();
		sp.setCode("foo");
		sp.setExpression("Patient.gender");
		sp.setXpathUsage(XPathUsageType.NORMAL);
		sp.setTitle("Foo Param");

		try {
			ourClient.create().resource(sp).execute();
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals("HTTP 422 Unprocessable Entity: " + Msg.code(1112) + "SearchParameter.status is missing or invalid", e.getMessage());
		}
	}

	@Test
	public void testConformanceOverrideAllowed() {
		myModelConfig.setDefaultSearchParamsCanBeOverridden(true);

		CapabilityStatement conformance = ourClient
				.fetchConformance()
				.ofType(CapabilityStatement.class)
				.execute();
		Map<String, CapabilityStatementRestResourceSearchParamComponent> map = extractSearchParams(conformance, "Patient");

		CapabilityStatementRestResourceSearchParamComponent param = map.get("foo");
		assertNull(param);

		param = map.get("gender");
		assertNotNull(param);

		TransactionTemplate txTemplate = newTxTemplate();
		txTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				// Add a custom search parameter
				SearchParameter fooSp = new SearchParameter();
				fooSp.addBase("Patient");
				fooSp.setCode("foo");
				fooSp.setName("foo");
				fooSp.setType(org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.TOKEN);
				fooSp.setTitle("FOO SP");
				fooSp.setExpression("Patient.gender");
				fooSp.setXpathUsage(org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType.NORMAL);
				fooSp.setStatus(org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.ACTIVE);
				mySearchParameterDao.create(fooSp, mySrd);
			}
		});

		// Disable an existing parameter
		txTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				SearchParameter fooSp = new SearchParameter();
				fooSp.addBase("Patient");
				fooSp.setCode("gender");
				fooSp.setType(org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.TOKEN);
				fooSp.setTitle("Gender");
				fooSp.setExpression("Patient.gender");
				fooSp.setXpathUsage(org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType.NORMAL);
				fooSp.setStatus(org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.RETIRED);
				mySearchParameterDao.create(fooSp, mySrd);
			}
		});

		txTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				mySearchParamRegistry.forceRefresh();
			}
		});

		conformance = ourClient
				.fetchConformance()
				.ofType(CapabilityStatement.class)
				.execute();
		map = extractSearchParams(conformance, "Patient");

		param = map.get("foo");
		assertEquals("foo", param.getName());

		param = map.get("gender");
		assertNull(param);

	}


	@Test
	public void testCreatingParamMarksCorrectResourcesForReindexing() {
		Patient pat = new Patient();
		pat.setGender(AdministrativeGender.MALE);
		IIdType patId = myPatientDao.create(pat, mySrd).getId().toUnqualifiedVersionless();

		Observation obs2 = new Observation();
		obs2.setStatus(ObservationStatus.FINAL);
		IIdType obsId = myObservationDao.create(obs2, mySrd).getId().toUnqualifiedVersionless();

		ResourceTable res = myResourceTableDao.findById(patId.getIdPartAsLong()).orElseThrow(IllegalStateException::new);
		assertEquals(BaseHapiFhirDao.INDEX_STATUS_INDEXED, res.getIndexStatus().longValue());
		res = myResourceTableDao.findById(obsId.getIdPartAsLong()).orElseThrow(IllegalStateException::new);
		assertEquals(BaseHapiFhirDao.INDEX_STATUS_INDEXED, res.getIndexStatus().longValue());

		SearchParameter fooSp = new SearchParameter();
		fooSp.addBase("Patient");
		fooSp.setCode("foo");
		fooSp.setType(org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.TOKEN);
		fooSp.setTitle("FOO SP");
		fooSp.setExpression("Patient.gender");
		fooSp.setXpathUsage(org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.ACTIVE);
		mySearchParameterDao.create(fooSp, mySrd);

		runInTransaction(()->{
			List<ResourceReindexJobEntity> allJobs = myResourceReindexJobDao.findAll();
			assertEquals(1, allJobs.size());
			assertEquals("Patient", allJobs.get(0).getResourceType());
		});
	}

	@Test
	public void testIncludeExtensionReferenceAsRecurse() throws Exception {
		SearchParameter attendingSp = new SearchParameter();
		attendingSp.addBase("Patient");
		attendingSp.setCode("attending");
		attendingSp.setType(org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.REFERENCE);
		attendingSp.setTitle("Attending");
		attendingSp.setExpression("Patient.extension('http://acme.org/attending')");
		attendingSp.setXpathUsage(org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType.NORMAL);
		attendingSp.setStatus(org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.ACTIVE);
		attendingSp.getTarget().add(new CodeType("Practitioner"));
		IIdType spId = mySearchParameterDao.create(attendingSp, mySrd).getId().toUnqualifiedVersionless();

		mySearchParamRegistry.forceRefresh();

		Practitioner p1 = new Practitioner();
		p1.addName().setFamily("P1");
		IIdType p1id = myPractitionerDao.create(p1).getId().toUnqualifiedVersionless();

		Patient p2 = new Patient();
		p2.addName().setFamily("P2");
		p2.addExtension().setUrl("http://acme.org/attending").setValue(new Reference(p1id));
		IIdType p2id = myPatientDao.create(p2).getId().toUnqualifiedVersionless();

		Appointment app = new Appointment();
		app.addParticipant().getActor().setReference(p2id.getValue());
		IIdType appId = myAppointmentDao.create(app).getId().toUnqualifiedVersionless();

		SearchParameterMap map;
		IBundleProvider results;
		List<String> foundResources;

		HttpGet get = new HttpGet(ourServerBase + "/Appointment?_include:recurse=Appointment:patient&_include:recurse=Appointment:location&_include:recurse=Patient:attending&_pretty=true");
		CloseableHttpResponse response = ourHttpClient.execute(get);
		try {
			String resp = IOUtils.toString(response.getEntity().getContent(), Constants.CHARSET_UTF8);
			ourLog.info(resp);
			assertEquals(200, response.getStatusLine().getStatusCode());

			assertThat(resp, containsString("<fullUrl value=\"http://localhost:" + ourPort + "/fhir/context/Practitioner/"));
		} finally {
			IOUtils.closeQuietly(response);
		}
	}

	@Test
	public void testSearchForExtension() {
		SearchParameter eyeColourSp = new SearchParameter();
		eyeColourSp.addBase("Patient");
		eyeColourSp.setCode("eyecolour");
		eyeColourSp.setType(org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.TOKEN);
		eyeColourSp.setTitle("Eye Colour");
		eyeColourSp.setExpression("Patient.extension('http://acme.org/eyecolour')");
		eyeColourSp.setXpathUsage(org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType.NORMAL);
		eyeColourSp.setStatus(org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.ACTIVE);

		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(eyeColourSp));

		ourClient
				.create()
				.resource(eyeColourSp)
				.execute();

		mySearchParamRegistry.forceRefresh();

		Patient p1 = new Patient();
		p1.setActive(true);
		p1.addExtension().setUrl("http://acme.org/eyecolour").setValue(new CodeType("blue"));
		IIdType p1id = myPatientDao.create(p1).getId().toUnqualifiedVersionless();

		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(p1));

		Patient p2 = new Patient();
		p2.setActive(true);
		p2.addExtension().setUrl("http://acme.org/eyecolour").setValue(new CodeType("green"));
		IIdType p2id = myPatientDao.create(p2).getId().toUnqualifiedVersionless();

		Bundle bundle = ourClient
				.search()
				.forResource(Patient.class)
				.where(new TokenClientParam("eyecolour").exactly().code("blue"))
				.returnBundle(Bundle.class)
				.execute();

		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));

		List<String> foundResources = toUnqualifiedVersionlessIdValues(bundle);
		assertThat(foundResources, contains(p1id.getValue()));

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchQualifiedWithCustomReferenceParam() {

		SearchParameter fooSp = new SearchParameter();
		fooSp.addBase("Observation");
		fooSp.setCode("foo");
		fooSp.setType(org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.REFERENCE);
		fooSp.setTitle("FOO SP");
		fooSp.setExpression("Observation.subject");
		fooSp.setXpathUsage(org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.ACTIVE);
		mySearchParameterDao.create(fooSp, mySrd);

		mySearchParamRegistry.forceRefresh();

		Patient pat = new Patient();
		pat.setGender(AdministrativeGender.MALE);
		IIdType patId = myPatientDao.create(pat, mySrd).getId().toUnqualifiedVersionless();

		Observation obs1 = new Observation();
		obs1.getSubject().setReferenceElement(patId);
		IIdType obsId1 = myObservationDao.create(obs1, mySrd).getId().toUnqualifiedVersionless();

		Observation obs2 = new Observation();
		obs2.setStatus(org.hl7.fhir.dstu3.model.Observation.ObservationStatus.FINAL);
		IIdType obsId2 = myObservationDao.create(obs2, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap map;
		IBundleProvider results;
		List<String> foundResources;
		Bundle result;

		result = ourClient
				.search()
				.forResource(Observation.class)
				.where(new ReferenceClientParam("foo").hasChainedProperty(Patient.GENDER.exactly().code("male")))
				.returnBundle(Bundle.class)
				.execute();
		foundResources = toUnqualifiedVersionlessIdValues(result);
		assertThat(foundResources, contains(obsId1.getValue()));

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchWithCustomParam() {

		SearchParameter fooSp = new SearchParameter();
		fooSp.addBase("Patient");
		fooSp.setCode("foo");
		fooSp.setType(org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.TOKEN);
		fooSp.setTitle("FOO SP");
		fooSp.setExpression("Patient.gender");
		fooSp.setXpathUsage(org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.ACTIVE);
		mySearchParameterDao.create(fooSp, mySrd);

		mySearchParamRegistry.forceRefresh();

		Patient pat = new Patient();
		pat.setGender(AdministrativeGender.MALE);
		IIdType patId = myPatientDao.create(pat, mySrd).getId().toUnqualifiedVersionless();

		Patient pat2 = new Patient();
		pat2.setGender(AdministrativeGender.FEMALE);
		IIdType patId2 = myPatientDao.create(pat2, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap map;
		IBundleProvider results;
		List<String> foundResources;
		Bundle result;

		result = ourClient
				.search()
				.forResource(Patient.class)
				.where(new TokenClientParam("foo").exactly().code("male"))
				.returnBundle(Bundle.class)
				.execute();

		foundResources = toUnqualifiedVersionlessIdValues(result);
		assertThat(foundResources, contains(patId.getValue()));

	}


}
