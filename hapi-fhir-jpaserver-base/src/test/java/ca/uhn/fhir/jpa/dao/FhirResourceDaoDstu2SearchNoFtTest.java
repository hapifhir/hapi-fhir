package ca.uhn.fhir.jpa.dao;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.Test;

import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.server.Constants;

@SuppressWarnings("unchecked")
public class FhirResourceDaoDstu2SearchNoFtTest extends BaseJpaDstu2Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoDstu2SearchNoFtTest.class);
	
	@Test
	public void testSearchWithChainedParams() {
		String methodName = "testSearchWithChainedParams";
		IIdType pId1;
		{
			Patient patient = new Patient();
			patient.addName().addGiven("methodName");
			patient.addAddress().addLine("My fulltext address");
			pId1 = myPatientDao.create(patient).getId();
		}

		Observation obs = new Observation();
		obs.getSubject().setReference(pId1);
		obs.setValue(new StringDt("This is the fulltext of the observation"));
		IIdType oId1 = myObservationDao.create(obs).getId().toUnqualifiedVersionless();
		
		obs = new Observation();
		obs.getSubject().setReference(pId1);
		obs.setValue(new StringDt("Another fulltext"));
		IIdType oId2 = myObservationDao.create(obs).getId().toUnqualifiedVersionless();
		
		SearchParameterMap params = new SearchParameterMap();
		params.add(Constants.PARAM_CONTENT, new StringDt("fulltext"));
		List<IIdType> patients = toUnqualifiedVersionlessIds(myPatientDao.search(params));
		assertThat(patients, containsInAnyOrder(oId1, oId2));
	}

	
}
