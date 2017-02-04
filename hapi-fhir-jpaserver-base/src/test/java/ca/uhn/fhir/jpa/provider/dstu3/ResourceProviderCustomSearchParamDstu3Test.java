package ca.uhn.fhir.jpa.provider.dstu3;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Observation.ObservationStatus;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.SearchParameter;
import org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.rest.gclient.ReferenceClientParam;
import ca.uhn.fhir.rest.gclient.TokenClientParam;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.TestUtil;

public class ResourceProviderCustomSearchParamDstu3Test extends BaseResourceProviderDstu3Test {

	@Override
	@After
	public void after() throws Exception {
		super.after();

		myDaoConfig.setDefaultSearchParamsCanBeOverridden(new DaoConfig().isDefaultSearchParamsCanBeOverridden());
	}

	@Override
	public void before() throws Exception {
		super.before();
	}

	@Test
	public void saveCreateSearchParamInvalidWithMissingStatus() throws IOException {
		SearchParameter sp = new SearchParameter();
		sp.setCode("foo");
		sp.setXpath("Patient.gender");
		sp.setXpathUsage(XPathUsageType.NORMAL);
		sp.setTitle("Foo Param");

		try {
			ourClient.create().resource(sp).execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: Resource.status is missing or invalid: null", e.getMessage());
		}
	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchWithCustomParam() {

		SearchParameter fooSp = new SearchParameter();
		fooSp.setCode("foo");
		fooSp.setType(org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.TOKEN);
		fooSp.setTitle("FOO SP");
		fooSp.setXpath("Patient.gender");
		fooSp.setXpathUsage(org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.ACTIVE);
		mySearchParameterDao.create(fooSp, mySrd);
		
		mySearchParamRegsitry.forceRefresh();
		
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


	@Test
	public void testCreatingParamMarksCorrectResourcesForReindexing() {
		Patient pat = new Patient();
		pat.setGender(AdministrativeGender.MALE);
		IIdType patId = myPatientDao.create(pat, mySrd).getId().toUnqualifiedVersionless();

		Observation obs2 = new Observation();
		obs2.setStatus(ObservationStatus.FINAL);
		IIdType obsId = myObservationDao.create(obs2, mySrd).getId().toUnqualifiedVersionless();

		ResourceTable res = myResourceTableDao.findOne(patId.getIdPartAsLong());
		assertEquals(BaseHapiFhirDao.INDEX_STATUS_INDEXED, res.getIndexStatus().longValue());
		res = myResourceTableDao.findOne(obsId.getIdPartAsLong());
		assertEquals(BaseHapiFhirDao.INDEX_STATUS_INDEXED, res.getIndexStatus().longValue());
		
		SearchParameter fooSp = new SearchParameter();
		fooSp.setCode("foo");
		fooSp.setType(org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.TOKEN);
		fooSp.setTitle("FOO SP");
		fooSp.setXpath("Patient.gender");
		fooSp.setXpathUsage(org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.ACTIVE);
		mySearchParameterDao.create(fooSp, mySrd);

		res = myResourceTableDao.findOne(patId.getIdPartAsLong());
		assertEquals(null, res.getIndexStatus());
		res = myResourceTableDao.findOne(obsId.getIdPartAsLong());
		assertEquals(BaseHapiFhirDao.INDEX_STATUS_INDEXED, res.getIndexStatus().longValue());

	}

	
	@SuppressWarnings("unused")
	@Test
	public void testSearchQualifiedWithCustomReferenceParam() {

		SearchParameter fooSp = new SearchParameter();
		fooSp.setCode("foo");
		fooSp.setType(org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.REFERENCE);
		fooSp.setTitle("FOO SP");
		fooSp.setXpath("Observation.subject");
		fooSp.setXpathUsage(org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.ACTIVE);
		mySearchParameterDao.create(fooSp, mySrd);
		
		mySearchParamRegsitry.forceRefresh();
		
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
	
	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
