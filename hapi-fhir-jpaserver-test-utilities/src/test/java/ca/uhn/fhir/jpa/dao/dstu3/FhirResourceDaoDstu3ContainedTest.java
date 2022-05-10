package ca.uhn.fhir.jpa.dao.dstu3;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaDstu3Test;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

public class FhirResourceDaoDstu3ContainedTest extends BaseJpaDstu3Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoDstu3ContainedTest.class);


	@Test
	public void testIndexContained() {
		Patient p = new Patient();
		p.setId("#some_patient");
		p.addName().setFamily("MYFAMILY").addGiven("MYGIVEN");

		Observation o1 = new Observation();
		o1.getCode().setText("Some Observation");
		o1.setSubject(new Reference(p));
		IIdType oid1 = myObservationDao.create(o1, mySrd).getId().toUnqualifiedVersionless();

		Observation o2 = new Observation();
		o2.getCode().setText("Some Observation");
		o2.setSubject(new Reference(p));
		IIdType oid2 = myObservationDao.create(o2, mySrd).getId().toUnqualifiedVersionless();

		Patient p2 = new Patient();
		p2.addName().setFamily("MYFAMILY").addGiven("MYGIVEN");
		IIdType pid2 = myPatientDao.create(p2, mySrd).getId().toUnqualifiedVersionless();

		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(o2));


		SearchParameterMap map = new SearchParameterMap();
		map.add(Observation.SP_CODE, new TokenParam(null, "some observation").setModifier(TokenParamModifier.TEXT));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map)), containsInAnyOrder(toValues(oid1, oid2)));
	}


	// TODO: make sure match URLs don't delete

}
