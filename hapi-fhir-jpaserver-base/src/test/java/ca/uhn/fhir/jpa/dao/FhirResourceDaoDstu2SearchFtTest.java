package ca.uhn.fhir.jpa.dao;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.Constants;

@ContextConfiguration(locations = { "classpath:fhir-spring-search-config-dstu2.xml" })
public class FhirResourceDaoDstu2SearchFtTest extends BaseJpaDstu2Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoDstu2SearchFtTest.class);

	@Before
	@Transactional
	public void beforeFlushFT() {
		FullTextEntityManager ftem = Search.getFullTextEntityManager(myEntityManager);
		ftem.purgeAll(ResourceTable.class);
		ftem.flushToIndexes();
	}

	@Test
	public void testSearchAndReindex() {
		Patient patient;
		SearchParameterMap map;
		
		patient = new Patient();
		patient.getText().setDiv("<div>DIVAAA</div>");
		patient.addName().addGiven("NAMEAAA");
		IIdType pId1 = myPatientDao.create(patient).getId().toUnqualifiedVersionless();
		
		map = new SearchParameterMap();
		map.add(Constants.PARAM_CONTENT, new StringParam("NAMEAAA"));
		assertThat(toUnqualifiedVersionlessIds(myPatientDao.search(map)), contains(pId1));

		map = new SearchParameterMap();
		map.add(Constants.PARAM_TEXT, new StringParam("DIVAAA"));
		assertThat(toUnqualifiedVersionlessIds(myPatientDao.search(map)), contains(pId1));
		
		/*
		 * Reindex
		 */
		
		patient = new Patient();
		patient.setId(pId1);
		patient.getText().setDiv("<div>DIVBBB</div>");
		patient.addName().addGiven("NAMEBBB");
		myPatientDao.update(patient);

		map = new SearchParameterMap();
		map.add(Constants.PARAM_CONTENT, new StringParam("NAMEAAA"));
		assertThat(toUnqualifiedVersionlessIds(myPatientDao.search(map)), empty());

		map = new SearchParameterMap();
		map.add(Patient.SP_NAME, new StringParam("NAMEBBB"));
		assertThat(toUnqualifiedVersionlessIds(myPatientDao.search(map)), contains(pId1));

		map = new SearchParameterMap();
		map.add(Constants.PARAM_CONTENT, new StringParam("NAMEBBB"));
		assertThat(toUnqualifiedVersionlessIds(myPatientDao.search(map)), contains(pId1));

		map = new SearchParameterMap();
		map.add(Constants.PARAM_TEXT, new StringParam("DIVBBB"));
		assertThat(toUnqualifiedVersionlessIds(myPatientDao.search(map)), contains(pId1));
		

	}

	@Test
	public void testSearchWithChainedParams() {
		String methodName = "testSearchWithChainedParams";
		IIdType pId1;
		{
			Patient patient = new Patient();
			patient.addName().addGiven("methodName");
			patient.addAddress().addLine("My fulltext address");
			pId1 = myPatientDao.create(patient).getId().toUnqualifiedVersionless();
		}

		Observation obs = new Observation();
		obs.getSubject().setReference(pId1);
		obs.setValue(new StringDt("This is the FULLtext of the observation"));
		IIdType oId1 = myObservationDao.create(obs).getId().toUnqualifiedVersionless();

		obs = new Observation();
		obs.getSubject().setReference(pId1);
		obs.setValue(new StringDt("Another fullText"));
		IIdType oId2 = myObservationDao.create(obs).getId().toUnqualifiedVersionless();

		List<IIdType> patients;
		SearchParameterMap params;

		params = new SearchParameterMap();
		params.add(Constants.PARAM_CONTENT, new StringParam("fulltext"));
		patients = toUnqualifiedVersionlessIds(myPatientDao.search(params));
		assertThat(patients, containsInAnyOrder(pId1));

		params = new SearchParameterMap();
		params.add(Constants.PARAM_CONTENT, new StringParam("FULLTEXT"));
		patients = toUnqualifiedVersionlessIds(myObservationDao.search(params));
		assertThat(patients, containsInAnyOrder(oId1, oId2));

	}

}
