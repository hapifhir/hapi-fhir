package ca.uhn.fhir.jpa.dao.dstu3;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Map;
import java.util.Set;

import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.CodeSystem.CodeSystemContentMode;
import org.hl7.fhir.dstu3.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.jpa.dao.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.TestUtil;

public class FhirResourceDaoTerminologyDstu3Test extends BaseJpaDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoTerminologyDstu3Test.class);

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


	@Test
	public void testCodeSystemWithDefinedCodes() {
		//@formatter:off
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl("http://example.com/my_code_system");
		codeSystem.setContent(CodeSystemContentMode.COMPLETE);		
		codeSystem
			.addConcept().setCode("A").setDisplay("Code A")
				.addConcept(new ConceptDefinitionComponent().setCode("AA").setDisplay("Code AA"))
				.addConcept(new ConceptDefinitionComponent().setCode("AB").setDisplay("Code AB"));
		codeSystem
			.addConcept().setCode("B").setDisplay("Code A")
				.addConcept(new ConceptDefinitionComponent().setCode("BA").setDisplay("Code AA"))
				.addConcept(new ConceptDefinitionComponent().setCode("BB").setDisplay("Code AB"));
		//@formatter:on
		
		IIdType id = myCodeSystemDao.create(codeSystem, new ServletRequestDetails()).getId().toUnqualified();
		
		Set<TermConcept> codes = myTermSvc.findCodesBelow(id.getIdPartAsLong(), id.getVersionIdPartAsLong(), "A");
		assertThat(toCodes(codes), containsInAnyOrder("A", "AA", "AB"));
		
	}

	@Test
	public void testSearchCodeIn() {
		//@formatter:off
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl("http://example.com/my_code_system");
		codeSystem.setContent(CodeSystemContentMode.COMPLETE);		
		codeSystem
			.addConcept().setCode("A").setDisplay("Code A")
				.addConcept(new ConceptDefinitionComponent().setCode("AA").setDisplay("Code AA"))
				.addConcept(new ConceptDefinitionComponent().setCode("AB").setDisplay("Code AB"));
		codeSystem
			.addConcept().setCode("B").setDisplay("Code A")
				.addConcept(new ConceptDefinitionComponent().setCode("BA").setDisplay("Code AA"))
				.addConcept(new ConceptDefinitionComponent().setCode("BB").setDisplay("Code AB"));
		//@formatter:on
		myCodeSystemDao.create(codeSystem, new ServletRequestDetails());
		
		Observation obsAA = new Observation();
		obsAA.getCode().addCoding().setSystem("http://example.com/my_code_system").setCode("AA");
		IIdType idAA = myObservationDao.create(obsAA, mySrd).getId().toUnqualifiedVersionless();

		Observation obsBA = new Observation();
		obsBA.getCode().addCoding().setSystem("http://example.com/my_code_system").setCode("BA");
		IIdType idBA = myObservationDao.create(obsBA, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap params = new SearchParameterMap();
		params.add(Observation.SP_CODE, new TokenParam("http://example.com/my_code_system", "A").setModifier(TokenParamModifier.IN));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(params)), containsInAnyOrder(idAA.getValue()));
		
	}

	
	@Test
	public void testCodeSystemCreateDuplicateFails() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl("http://example.com/my_code_system");
		codeSystem.setContent(CodeSystemContentMode.COMPLETE);		
		IIdType id = myCodeSystemDao.create(codeSystem, new ServletRequestDetails()).getId().toUnqualified();

		codeSystem = new CodeSystem();
		codeSystem.setUrl("http://example.com/my_code_system");
		codeSystem.setContent(CodeSystemContentMode.COMPLETE);		
		try {
			myCodeSystemDao.create(codeSystem, new ServletRequestDetails());
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals("Can not create multiple code systems with URI \"http://example.com/my_code_system\", already have one with resource ID: CodeSystem/" + id.getIdPart(), e.getMessage());
		}
	}

	
	
	
}
