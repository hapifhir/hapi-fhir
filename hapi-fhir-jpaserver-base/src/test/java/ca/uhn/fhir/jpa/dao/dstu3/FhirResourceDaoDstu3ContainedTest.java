package ca.uhn.fhir.jpa.dao.dstu3;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.Test;

import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;

public class FhirResourceDaoDstu3ContainedTest extends BaseJpaDstu3Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoDstu3ContainedTest.class);
	
	@Test
	public void before() {
		myDaoConfig.setIndexContainedResources(true);
	}
	
	@Test
	public void testIndexContained() {
		Patient p = new Patient();
		p.setId("#some_patient");
		p.addName().addFamily("MYFAMILY").addGiven("MYGIVEN");
		
		Observation o1 = new Observation();
		o1.getCode().setText("Some Observation");
		o1.setSubject(new Reference(p));
		IIdType oid1 = myObservationDao.create(o1, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		
		Observation o2 = new Observation();
		o2.getCode().setText("Some Observation");
		o2.setSubject(new Reference(p));
		IIdType oid2 = myObservationDao.create(o2, new ServletRequestDetails()).getId().toUnqualifiedVersionless();

		Patient p2 = new Patient();
		p2.addName().addFamily("MYFAMILY").addGiven("MYGIVEN");
		IIdType pid2 = myPatientDao.create(p2, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		
		ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(o2));
		
		
		SearchParameterMap map;
		
//		map = new SearchParameterMap();
//		map.add(Observation.SP_CODE, new TokenParam(null, "some observation").setModifier(TokenParamModifier.TEXT));
//		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map)), containsInAnyOrder(toValues(id1, id2)));

	}

	
	// TODO: make sure match URLs don't delete
	
}
